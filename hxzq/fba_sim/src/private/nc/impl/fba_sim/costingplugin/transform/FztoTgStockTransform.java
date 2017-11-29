package nc.impl.fba_sim.costingplugin.transform;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_zqjd.tally.tool.QueryZqjdTallyTool;
import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
import nc.bs.uif2.validation.ValidationException;
import nc.impl.fba_sim.costingplugin.AbstractTransform;
import nc.impl.fba_sim.costplugin.stock.TransformFzmrYjlxUtils;
import nc.impl.fba_sim.costplugin.stock.jisuanjdkcUtils;
import nc.impl.fba_sim.pub.RealratePlugin;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.itf.fba_zqjd.tally.IZqjdTallyService;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.fba_sec.secbd.busitype.BusitypeVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.fba_zqjd.tally.QueryTallyParams;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 负载转托管 ：两种情况： 1、负债转换时 ,一个同转出同负债买入，转入同负债卖出，需要按份额占比计算出转出负债的应收计提利息。
 * 2、融入转换时，一个同融入，一个同融出
 * 
 */
public class FztoTgStockTransform extends AbstractTransform {
	private BaseDAO baseDAO;

	@Override
	protected void calculate(ICostingTool costingtool, TransformtradeVO tradevo)
			throws Exception {

		if (tradevo.getPk_busitype() != null) {
			BusitypeVO busivo = (BusitypeVO) new HYPubBO().queryByPrimaryKey(
					BusitypeVO.class, tradevo.getPk_busitype());
			// 负载转换时，先判断转出的负债库存是否足够(不修改实际库存)，然后按照份额占比结转应收计提利息,
			if (busivo != null && busivo.getCode().equals("0404")) {

				PubMethod pm = PubMethod.getInstance();
				String trade_date = costingtool.getCurrdate();
				ICostBalanceTool balanceTool = costingtool.getBalancetool();
				String[] keys = new String[costingtool.getCostParaVO()
						.getCostplanvo().getCostFieldArray().length - 1];
				// 获取审核方案维度方案
				for (int i = 0; i < keys.length; i++) {
					keys[i] = costingtool.getCostParaVO().getCostplanvo()
							.getCostFieldArray()[i];
				}
				// 拼接key
				String hckey = getCombinesKey(tradevo, keys, false);
				/**
				 * 此处拼接key必须加上业务组
				 */
				hckey = costingtool.getCurrbilltypegroupvo()
						.getPk_billtypegroup() + hckey;
				hckey = hckey + tradevo.getPk_securities();
				BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
				queryvo.setKey(hckey);
				queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
				queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
				queryvo.setTrade_date(trade_date);
				// 库存余额表
				StockBalanceVO outstockbalancevo = balanceTool
						.getStockbalanceVO(queryvo, costingtool);
				if (outstockbalancevo == null) {
					costingtool.handleException(tradevo, null,
							SystemConst.error[0]);
					return;
				}
				// zpm 实际利率 增加开始，位置不要动
				if (outstockbalancevo.getStock_map().get(
						tradevo.getTrade_date().toLocalString()) == null) {
					outstockbalancevo.getStock_map().put(
							tradevo.getTrade_date().toLocalString(),
							outstockbalancevo.getStocks_num());
				}
				// 根据台账 结转公允价值 20150417 ln
				ZqjdTallyVO zqjdtallyvo = this.gatherzcSellBuyBalance(tradevo);
				if (zqjdtallyvo == null
						|| (zqjdtallyvo != null && zqjdtallyvo.getBargain_num()
								.compareTo(UFDouble.ZERO_DBL) <= 0)) {
					costingtool.handleException(tradevo, null, "转出债券没有负债");
				}
				// 如果为空，说明是第一次获取负债库存，则要通过计算算出利息，如果不为空，则说明已经把初始的库存信息提取出来了
				UFDouble lastnum = new UFDouble(0);
				UFDouble lastsum = new UFDouble(0);
				if (outstockbalancevo.getVdef1() == null) {
					jisuanjdkcUtils utis = new jisuanjdkcUtils();
					// 数组，第一个为数量，第二个为金额
					UFDouble[] jsfz = utis.jisuanfzqc(outstockbalancevo);
					lastnum = jsfz[0];
					lastsum = jsfz[1];

				} else {
					lastnum = new UFDouble(outstockbalancevo.getVdef1());
					lastsum = new UFDouble(outstockbalancevo.getVdef2());

				}
				if (lastnum.compareTo(new UFDouble(0)) != 0) {
					// 计算转出金额 =本次转出数量*(负债库存金额/负债库存数量)
					UFDouble zcsum = (lastsum.div(lastnum)).multiply(tradevo
							.getBargain_num());
					tradevo.setBargain_sum(zcsum);
				}
				// 计算出负债的应收利息计提
				TransformFzmrYjlxUtils utils = new TransformFzmrYjlxUtils();
				// 总的应收利息
				UFDouble lx = utils.forwardInterestDistill(costingtool,
						outstockbalancevo, tradevo);
				// 负债库存数量要大于0才行
				if (lastnum.compareTo(new UFDouble(0)) > 0) {
					tradevo.setInterest(lx
							.multiply(
									tradevo.getBargain_num().div(
											new UFDouble(lastnum))).setScale(2,
									4));
				} else {
					tradevo.setInterest(new UFDouble(0));
				}
				// 存取应收计提利息
				utils.saveInterestDistill(costingtool, outstockbalancevo,
						tradevo);

				// 减去转出负债库存的数量和金额
				outstockbalancevo.setVdef1(lastnum
						.sub(tradevo.getBargain_num()).toString());
				outstockbalancevo.setVdef2(lastsum
						.sub(tradevo.getBargain_sum()).toString());

				// 计算结转公允价值
				// UFDouble fairvalue =
				// costcalc.forwardFairValueDistill_debt(costingtool,
				// zqjdtallyvo, tradevo);
				// tradevo.setFairvalue(fairvalue);
				// 负债类 减少台账 20150417 ln
				UFDouble sellcost = pm.multiply(tradevo.getBargain_num(),
						zqjdtallyvo.getBargain_sum());
				sellcost = pm.div(sellcost, zqjdtallyvo.getBargain_num());
				tradevo.setSellcost(sellcost);// 根据台账反写销售成本
				this.savesellbalance(tradevo,
						sellcost.multiply(new UFDouble(-1)));
				// 保存公允价值
				// costcalc.saveFairValueDistill(costingtool, tradevo);

				/**
				 * 转入数量增加字段原转入数量（bargain_num2_his）记录原来的转入数量，弃审时用来还原
				 * 
				 * @author cjh
				 * @date 2015-12-08
				 */
				tradevo.setAttributeValue("bargain_num2_his",
						tradevo.getBargain_num2());
				if (tradevo.getBargain_num2() == null
						|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

					tradevo.setBargain_num2(tradevo.getBargain_num());// 如果转入数量为空，那么转入数量等于转出数量
				}

				// 应税费
				UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo,
						costingtool, tradevo.getBargain_num());
				tradevo.setTaxexpense(taxOutCost);
				/**
				 * 证券转换记录审核时转出金额有值则不需回写否则回写：转出金额=库存金额/库存数量*转出数量，原逻辑没有加判断都直接回写
				 * 
				 * @author cjh
				 * @date 2015-12-07 09：55
				 */
				tradevo.setAttributeValue("bargain_sum_his",
						tradevo.getBargain_sum());

				/**
				 * 负债转换交易记录审核时转入金额有值则不需回写否则回写,原逻辑没有加判断都直接回写
				 * 
				 * @author cjh
				 * @date 2015-12-07 09：55
				 */
				if (tradevo.getBargain_sum2() == null
						|| tradevo.getBargain_sum2().compareTo(new UFDouble(0)) <= 0) {
					tradevo.setBargain_sum2(tradevo.getBargain_sum());
					tradevo.setAttributeValue("bargain_sum2_his", null);
				} else {
					tradevo.setAttributeValue("bargain_sum2_his",
							tradevo.getBargain_sum2());
				}

				outstockbalancevo.setStocks_tax(pm.sub(
						outstockbalancevo.getStocks_tax(), taxOutCost));
				// 更新转出库存
				balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);

				// 获得转入的库存
				String hrkey = getCombinesKey(tradevo, keys, true);
				/**
				 * 此处拼接key必须加上业务组
				 */
				hrkey = costingtool.getCurrbilltypegroupvo()
						.getPk_billtypegroup() + hrkey;
				hrkey = hrkey + tradevo.getPk_securities2();
				queryvo.setKey(hrkey);
				queryvo.setPk_assetsprop(tradevo.getHr_pk_assetsprop());
				queryvo.setPk_stocksort(tradevo.getHr_pk_stocksort());
				StockBalanceVO instockbalancevo = balanceTool
						.getStockbalanceVO(queryvo, costingtool);
				if (instockbalancevo == null) {
					instockbalancevo = balanceTool.getStockbalanceVOByVO(
							tradevo, costingtool);
					instockbalancevo.setPk_securities(tradevo
							.getPk_securities2());
				}
				// 如果为空，说明是第一次获取负债库存，如果不为空，则说明已经把初始的库存信息提取出来了
				UFDouble lastnum2 = new UFDouble(0);
				UFDouble lastsum2 = new UFDouble(0);
				if (instockbalancevo.getVdef1() == null) {
					jisuanjdkcUtils utis = new jisuanjdkcUtils();
					// 数组，第一个为数量，第二个为金额
					UFDouble[] jsfz = utis.jisuanfzqc(instockbalancevo);
					lastnum2 = jsfz[0];
					lastsum2 = jsfz[1];

				} else {
					lastnum2 = new UFDouble(instockbalancevo.getVdef1());
					lastsum2 = new UFDouble(instockbalancevo.getVdef2());

				}
				instockbalancevo.setVdef1(lastnum2.add(
						tradevo.getBargain_num2()).toString());
				instockbalancevo.setVdef2(lastsum2.add(
						tradevo.getBargain_sum2()).toString());
				// 记录台账

				this.saveSellTally(tradevo);
				// 更新库存
				balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
			}
			// 融入转换时，只是融入的实际库存数量，不变负债库存数量
			else if (busivo != null && busivo.getCode().equals("0403")) {
				PubMethod pm = PubMethod.getInstance();
				boolean isFirstPrice = costingtool.getCostParaVO()
						.getFirstPrice();
				String trade_date = costingtool.getCurrdate();
				ICostBalanceTool balanceTool = costingtool.getBalancetool();
				String[] keys = new String[costingtool.getCostParaVO()
						.getCostplanvo().getCostFieldArray().length - 1];
				for (int i = 0; i < keys.length; i++) {
					keys[i] = costingtool.getCostParaVO().getCostplanvo()
							.getCostFieldArray()[i];
				}
				String hckey = getCombinesKey(tradevo, keys, false);
				/**
				 * 此处拼接key必须加上业务组
				 */
				hckey = costingtool.getCurrbilltypegroupvo()
						.getPk_billtypegroup() + hckey;
				hckey = hckey + tradevo.getPk_securities();
				BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
				queryvo.setKey(hckey);
				queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
				queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
				queryvo.setTrade_date(trade_date);
				// 库存余额表
				StockBalanceVO outstockbalancevo = balanceTool
						.getStockbalanceVO(queryvo, costingtool);
				if (outstockbalancevo == null) {
					costingtool.handleException(tradevo, null,
							SystemConst.error[0]);
					return;
				}
				// zpm 实际利率 增加开始，位置不要动
				if (outstockbalancevo.getStock_map().get(
						tradevo.getTrade_date().toLocalString()) == null) {
					outstockbalancevo.getStock_map().put(
							tradevo.getTrade_date().toLocalString(),
							outstockbalancevo.getStocks_num());
				}
				//
				// 库存数量
				UFDouble stocks_num = outstockbalancevo.getStocks_num();
				// 库存金额
				UFDouble stocks_sum = outstockbalancevo.getStocks_sum();
				/**
				 * 转入数量增加字段原转入数量（bargain_num2_his）记录原来的转入数量，弃审时用来还原
				 * 
				 * @author cjh
				 * @date 2015-12-08
				 */
				tradevo.setAttributeValue("bargain_num2_his",
						tradevo.getBargain_num2());
				if (tradevo.getBargain_num2() == null
						|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

					tradevo.setBargain_num2(tradevo.getBargain_num());// 如果转入数量为空，那么转入数量等于转出数量
				}

				// 获得剩余数量
				stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
						tradevo.getBargain_num());
				// 判断转出是否合理
				if (stocks_num.compareTo(new UFDouble(0)) < 0) {
					costingtool.handleException(tradevo, null,
							SystemConst.error[1]);
				}
				if (stocks_num.compareTo(new UFDouble(0)) == 0) {// 卖空
					// 如果剩余数量为0 代表全部卖出 转出金额为库存金额
					stocks_sum = outstockbalancevo.getStocks_sum();
				} else {// 销售成本
					// 如果转出合理 计算转出金额
					if (isFirstPrice) {// 先计算单价
						stocks_sum = pm.div(outstockbalancevo.getStocks_sum(),
								outstockbalancevo.getStocks_num());
						stocks_sum = pm.multiply(stocks_sum,
								tradevo.getBargain_num());
					} else {
						stocks_sum = pm.multiply(
								outstockbalancevo.getStocks_sum(),
								tradevo.getBargain_num());
						stocks_sum = pm.div(stocks_sum,
								outstockbalancevo.getStocks_num());
					}
					stocks_sum = pm.setScale(stocks_sum, true, true);
				}
				UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo,
						costingtool, tradevo.getBargain_num());
				tradevo.setTaxexpense(taxOutCost);
				/**
				 * 证券转换记录审核时转出金额有值则不需回写否则回写：转出金额=库存金额/库存数量*转出数量，原逻辑没有加判断都直接回写
				 * 
				 * @author cjh
				 * @date 2015-12-07 09：55
				 */
				if (tradevo.getBargain_sum() == null
						|| tradevo.getBargain_sum().compareTo(new UFDouble(0)) <= 0) {
					tradevo.setBargain_sum(stocks_sum);
					tradevo.setAttributeValue("bargain_sum_his", null);
				} else {
					tradevo.setAttributeValue("bargain_sum_his",
							tradevo.getBargain_sum());
				}
				// tradevo.setBargain_sum(stocks_sum);

				/**
				 * 证券交易记录审核时转入金额有值则不需回写否则回写,原逻辑没有加判断都直接回写
				 * 
				 * @author cjh
				 * @date 2015-12-07 09：55
				 */
				if (tradevo.getBargain_sum2() == null
						|| tradevo.getBargain_sum2().compareTo(new UFDouble(0)) <= 0) {
					tradevo.setBargain_sum2(tradevo.getBargain_sum());
					tradevo.setAttributeValue("bargain_sum2_his", null);
				} else {
					tradevo.setAttributeValue("bargain_sum2_his",
							tradevo.getBargain_sum2());
					// tradevo.setBargain_sum2(tradevo.getBargain_sum());
				}
				// tradevo.setBargain_sum2(stocks_sum);
				tradevo.setSellcost(stocks_sum);
				outstockbalancevo.setStocks_num(stocks_num);
				outstockbalancevo.setStocks_sum(pm.sub(
						outstockbalancevo.getStocks_sum(), stocks_sum));
				outstockbalancevo.setStocks_tax(pm.sub(
						outstockbalancevo.getStocks_tax(), taxOutCost));
				// 更新转出库存
				balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);
				// 获得转入的库存
				String hrkey = getCombinesKey(tradevo, keys, true);
				/**
				 * 此处拼接key必须加上业务组
				 */
				hrkey = costingtool.getCurrbilltypegroupvo()
						.getPk_billtypegroup() + hrkey;
				hrkey = hrkey + tradevo.getPk_securities2();
				queryvo.setKey(hrkey);
				queryvo.setPk_assetsprop(tradevo.getHr_pk_assetsprop());
				queryvo.setPk_stocksort(tradevo.getHr_pk_stocksort());
				StockBalanceVO instockbalancevo = balanceTool
						.getStockbalanceVO(queryvo, costingtool);
				if (instockbalancevo == null) {
					instockbalancevo = balanceTool.getStockbalanceVOByVO(
							tradevo, costingtool);
					instockbalancevo.setPk_securities(tradevo
							.getPk_securities2());
				}
				// 获得转入的数量
				stocks_num = pm.add(instockbalancevo.getStocks_num(),
						tradevo.getBargain_num2());
				// 获得转入的金额
				stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
						tradevo.getBargain_sum2());
				// 保存转入信息
				instockbalancevo.setStocks_num(stocks_num);
				instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true,
						true));
				instockbalancevo.setStocks_tax(pm.add(
						instockbalancevo.getStocks_tax(), taxOutCost));
				// 更新转入的库存
				balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);

			}
		}

	}

	private void saveSellTally(TransformtradeVO tradevo) throws DAOException {

		ZqjdTallyVO tallyVO = zrbuildBaseTallyVOFromTradeVO(tradevo);

		getBaseDAO().insertVO(tallyVO);

	}

	private ZqjdTallyVO zrbuildBaseTallyVOFromTradeVO(TransformtradeVO tradeVO) {
		ZqjdTallyVO tallyVO = new ZqjdTallyVO();
		tallyVO.setPk_securities(tradeVO.getPk_securities2());

		tallyVO.setState(Integer
				.valueOf(nc.vo.fba_sec.pub.SystemConst.statecode[1]));

		tallyVO.setTrade_date(tradeVO.getTrade_date());

		tallyVO.setPk_capaccount(tradeVO.getHr_pk_capaccount());

		tallyVO.setPk_currtype("1002Z0100000000001K1");
		tallyVO.setBargain_num(tradeVO.getBargain_num2());
		tallyVO.setBargain_sum(tradeVO.getBargain_sum2());

		tallyVO.setPk_group(tradeVO.getPk_group());
		tallyVO.setPk_org(tradeVO.getPk_org());
		tallyVO.setPk_org_v(tradeVO.getPk_org_v());
		tallyVO.setPk_glorgbook(tradeVO.getPk_glorgbook());

		tallyVO.setPk_assetsprop(tradeVO.getHr_pk_assetsprop());
		tallyVO.setPk_stocksort("0001SE00000000000001");

		tallyVO.setSrcbillno(tradeVO.getBillno());

		return tallyVO;
	}

	private void savesellbalance(TransformtradeVO tradevo, UFDouble sellscost)
			throws DAOException, ValidationException {

		ZqjdTallyVO tallyVO = buildBaseTallyVOFromTradeVO(tradevo);

		UFDouble factor = new UFDouble(-1);

		tallyVO.setBargain_num(SafeCompute.multiply(tallyVO.getBargain_num(),
				factor));
		tallyVO.setBargain_sum(sellscost);

		checkQuantity(tallyVO);

		getBaseDAO().insertVO(tallyVO);

	}

	private BaseDAO getBaseDAO() {
		if (this.baseDAO == null) {
			this.baseDAO = new BaseDAO();
		}
		return this.baseDAO;
	}

	private ZqjdTallyVO buildBaseTallyVOFromTradeVO(TransformtradeVO tradeVO) {
		ZqjdTallyVO tallyVO = new ZqjdTallyVO();
		tallyVO.setPk_securities(tradeVO.getPk_securities());

		tallyVO.setState(Integer
				.valueOf(nc.vo.fba_sec.pub.SystemConst.statecode[1]));

		tallyVO.setTrade_date(tradeVO.getTrade_date());

		tallyVO.setPk_capaccount(tradeVO.getHc_pk_capaccount());

		tallyVO.setPk_currtype("1002Z0100000000001K1");
		tallyVO.setBargain_num(tradeVO.getBargain_num());
		tallyVO.setBargain_sum(tradeVO.getBargain_sum());

		tallyVO.setPk_group(tradeVO.getPk_group());
		tallyVO.setPk_org(tradeVO.getPk_org());
		tallyVO.setPk_org_v(tradeVO.getPk_org_v());
		tallyVO.setPk_glorgbook(tradeVO.getPk_glorgbook());

		tallyVO.setPk_assetsprop(tradeVO.getHc_pk_assetsprop());
		tallyVO.setPk_stocksort("0001SE00000000000001");

		tallyVO.setSrcbillno(tradeVO.getBillno());

		return tallyVO;
	}

	private void checkQuantity(ZqjdTallyVO tallyVO) throws ValidationException {
		TransformOverSellQuantityValidation quantityValidation = new TransformOverSellQuantityValidation();

		quantityValidation.validate(tallyVO);
	}

	// 获取转出库存

	private ZqjdTallyVO gatherzcSellBuyBalance(TransformtradeVO tradeVO)
			throws BusinessException {
		QueryTallyParams params = new QueryTallyParams();
		params.setPk_capaccount(tradeVO.getHc_pk_capaccount());
		params.setPk_assetsprop(tradeVO.getHc_pk_assetsprop());
		params.setPk_glorgbook(tradeVO.getPk_glorgbook());
		params.setPk_org(tradeVO.getPk_org());
		params.setPk_securities(tradeVO.getPk_securities());
		params.setTrade_date(tradeVO.getTrade_date());
		return QueryZqjdTallyTool.querySellBuyBalance(params);
	}

	/**
	 * 默认审核 业务有特殊需求的话需要重写
	 */
	@Override
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// 处理单据上的应收利息、转出利息
		// bicbo.calcBondTradeBillArrayInterest(costingtool,
		// getData().toArray(new SuperVO[0]));
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		String datakey = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup()
				+ costingtool.getCurrdate()
				+ costingtool.getCurrbilltype();
		IBill[] ibills = tradedatatool.getData(datakey);
		if (ibills != null && ibills.length > 0) {
			for (IBill aggvo : ibills) {
				TransformtradeVO fathervo = (TransformtradeVO) aggvo
						.getParent();// 无孩子
				if (fathervo.getHc_pk_stocksort() == null) {
					fathervo.setHc_pk_stocksort(costingtool.getPk_stocksort()[0]);// 影响库存1
				}
				if (fathervo.getHr_pk_stocksort() == null) {
					fathervo.setHr_pk_stocksort(costingtool.getPk_stocksort()[1]);// 影响库存2
				}
				// vo.setPk_stocksort(vo.getHr_pk_stocksort());// 转入库存2
				// vo.setPk_assetsprop(vo.getHr_pk_assetsprop());// 转入库存2
				calculate(costingtool, fathervo);
				costingtool.updateFunds(fathervo);

				fathervo.setApprover(pk_user);
				fathervo.setApprovedate(PubMethod.getInstance().getDateTime(
						costingtool.getCurrdate()));
				fathervo.setState(SystemConst.statecode[3]);
			}
		}
	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			TransformtradeVO tradevo) throws Exception {
		String pk_group = costingtool.getCostParaVO().getCheckParavo()
				.getPk_group();
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();
		String tradedate = costingtool.getCostParaVO().getCheckParavo()
				.getTrade_date().toLocalString();
		// 清空结转应收利息
		tradevo.setInterest(null);
		// 清空结转公允价值
		tradevo.setFairvalue(null);
		// 清空转出应收利息
		costcalc.clearDistillInterest(costingtool);
		// 清空公允价值数据
		costcalc.clearDistillFv(costingtool);
		// 清空转出结转税费
		tradevo.setTaxexpense(null);
		/**
		 * 证券转换记录反审核时将转出金额、转入金额、转入数量还原
		 * 
		 * @author cjh
		 * @date 2015-12-07
		 */
		tradevo.setBargain_sum((UFDouble) tradevo
				.getAttributeValue("bargain_sum_his"));
		tradevo.setBargain_sum2((UFDouble) tradevo
				.getAttributeValue("bargain_sum2_his"));
		tradevo.setBargain_num2((UFDouble) tradevo
				.getAttributeValue("bargain_num2_his"));

		// 实际利率
		RealratePlugin check = new RealratePlugin();
		if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
			ICalcPluginMaintain mail = NCLocator.getInstance().lookup(
					ICalcPluginMaintain.class);
			mail.unZtgPlugindeal(tradevo, pk_group, pk_org, tradedate);
		}

		// 负债类 增加台账 20150417 ln
		IZqjdTallyService iZqjd = NCLocator.getInstance().lookup(
				IZqjdTallyService.class);
		iZqjd.deleteBuyTally(tradevo);
	}
}
