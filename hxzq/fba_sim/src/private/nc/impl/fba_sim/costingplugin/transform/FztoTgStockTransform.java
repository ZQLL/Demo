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
 * ����ת�й� ����������� 1����ծת��ʱ ,һ��ͬת��ͬ��ծ���룬ת��ͬ��ծ��������Ҫ���ݶ�ռ�ȼ����ת����ծ��Ӧ�ռ�����Ϣ��
 * 2������ת��ʱ��һ��ͬ���룬һ��ͬ�ڳ�
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
			// ����ת��ʱ�����ж�ת���ĸ�ծ����Ƿ��㹻(���޸�ʵ�ʿ��)��Ȼ���շݶ�ռ�Ƚ�תӦ�ռ�����Ϣ,
			if (busivo != null && busivo.getCode().equals("0404")) {

				PubMethod pm = PubMethod.getInstance();
				String trade_date = costingtool.getCurrdate();
				ICostBalanceTool balanceTool = costingtool.getBalancetool();
				String[] keys = new String[costingtool.getCostParaVO()
						.getCostplanvo().getCostFieldArray().length - 1];
				// ��ȡ��˷���ά�ȷ���
				for (int i = 0; i < keys.length; i++) {
					keys[i] = costingtool.getCostParaVO().getCostplanvo()
							.getCostFieldArray()[i];
				}
				// ƴ��key
				String hckey = getCombinesKey(tradevo, keys, false);
				/**
				 * �˴�ƴ��key�������ҵ����
				 */
				hckey = costingtool.getCurrbilltypegroupvo()
						.getPk_billtypegroup() + hckey;
				hckey = hckey + tradevo.getPk_securities();
				BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
				queryvo.setKey(hckey);
				queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
				queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
				queryvo.setTrade_date(trade_date);
				// �������
				StockBalanceVO outstockbalancevo = balanceTool
						.getStockbalanceVO(queryvo, costingtool);
				if (outstockbalancevo == null) {
					costingtool.handleException(tradevo, null,
							SystemConst.error[0]);
					return;
				}
				// zpm ʵ������ ���ӿ�ʼ��λ�ò�Ҫ��
				if (outstockbalancevo.getStock_map().get(
						tradevo.getTrade_date().toLocalString()) == null) {
					outstockbalancevo.getStock_map().put(
							tradevo.getTrade_date().toLocalString(),
							outstockbalancevo.getStocks_num());
				}
				// ����̨�� ��ת���ʼ�ֵ 20150417 ln
				ZqjdTallyVO zqjdtallyvo = this.gatherzcSellBuyBalance(tradevo);
				if (zqjdtallyvo == null
						|| (zqjdtallyvo != null && zqjdtallyvo.getBargain_num()
								.compareTo(UFDouble.ZERO_DBL) <= 0)) {
					costingtool.handleException(tradevo, null, "ת��ծȯû�и�ծ");
				}
				// ���Ϊ�գ�˵���ǵ�һ�λ�ȡ��ծ��棬��Ҫͨ�����������Ϣ�������Ϊ�գ���˵���Ѿ��ѳ�ʼ�Ŀ����Ϣ��ȡ������
				UFDouble lastnum = new UFDouble(0);
				UFDouble lastsum = new UFDouble(0);
				if (outstockbalancevo.getVdef1() == null) {
					jisuanjdkcUtils utis = new jisuanjdkcUtils();
					// ���飬��һ��Ϊ�������ڶ���Ϊ���
					UFDouble[] jsfz = utis.jisuanfzqc(outstockbalancevo);
					lastnum = jsfz[0];
					lastsum = jsfz[1];

				} else {
					lastnum = new UFDouble(outstockbalancevo.getVdef1());
					lastsum = new UFDouble(outstockbalancevo.getVdef2());

				}
				if (lastnum.compareTo(new UFDouble(0)) != 0) {
					// ����ת����� =����ת������*(��ծ�����/��ծ�������)
					UFDouble zcsum = (lastsum.div(lastnum)).multiply(tradevo
							.getBargain_num());
					tradevo.setBargain_sum(zcsum);
				}
				// �������ծ��Ӧ����Ϣ����
				TransformFzmrYjlxUtils utils = new TransformFzmrYjlxUtils();
				// �ܵ�Ӧ����Ϣ
				UFDouble lx = utils.forwardInterestDistill(costingtool,
						outstockbalancevo, tradevo);
				// ��ծ�������Ҫ����0����
				if (lastnum.compareTo(new UFDouble(0)) > 0) {
					tradevo.setInterest(lx
							.multiply(
									tradevo.getBargain_num().div(
											new UFDouble(lastnum))).setScale(2,
									4));
				} else {
					tradevo.setInterest(new UFDouble(0));
				}
				// ��ȡӦ�ռ�����Ϣ
				utils.saveInterestDistill(costingtool, outstockbalancevo,
						tradevo);

				// ��ȥת����ծ���������ͽ��
				outstockbalancevo.setVdef1(lastnum
						.sub(tradevo.getBargain_num()).toString());
				outstockbalancevo.setVdef2(lastsum
						.sub(tradevo.getBargain_sum()).toString());

				// �����ת���ʼ�ֵ
				// UFDouble fairvalue =
				// costcalc.forwardFairValueDistill_debt(costingtool,
				// zqjdtallyvo, tradevo);
				// tradevo.setFairvalue(fairvalue);
				// ��ծ�� ����̨�� 20150417 ln
				UFDouble sellcost = pm.multiply(tradevo.getBargain_num(),
						zqjdtallyvo.getBargain_sum());
				sellcost = pm.div(sellcost, zqjdtallyvo.getBargain_num());
				tradevo.setSellcost(sellcost);// ����̨�˷�д���۳ɱ�
				this.savesellbalance(tradevo,
						sellcost.multiply(new UFDouble(-1)));
				// ���湫�ʼ�ֵ
				// costcalc.saveFairValueDistill(costingtool, tradevo);

				/**
				 * ת�����������ֶ�ԭת��������bargain_num2_his����¼ԭ����ת������������ʱ������ԭ
				 * 
				 * @author cjh
				 * @date 2015-12-08
				 */
				tradevo.setAttributeValue("bargain_num2_his",
						tradevo.getBargain_num2());
				if (tradevo.getBargain_num2() == null
						|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

					tradevo.setBargain_num2(tradevo.getBargain_num());// ���ת������Ϊ�գ���ôת����������ת������
				}

				// Ӧ˰��
				UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo,
						costingtool, tradevo.getBargain_num());
				tradevo.setTaxexpense(taxOutCost);
				/**
				 * ֤ȯת����¼���ʱת�������ֵ�����д�����д��ת�����=�����/�������*ת��������ԭ�߼�û�м��ж϶�ֱ�ӻ�д
				 * 
				 * @author cjh
				 * @date 2015-12-07 09��55
				 */
				tradevo.setAttributeValue("bargain_sum_his",
						tradevo.getBargain_sum());

				/**
				 * ��ծת�����׼�¼���ʱת������ֵ�����д�����д,ԭ�߼�û�м��ж϶�ֱ�ӻ�д
				 * 
				 * @author cjh
				 * @date 2015-12-07 09��55
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
				// ����ת�����
				balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);

				// ���ת��Ŀ��
				String hrkey = getCombinesKey(tradevo, keys, true);
				/**
				 * �˴�ƴ��key�������ҵ����
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
				// ���Ϊ�գ�˵���ǵ�һ�λ�ȡ��ծ��棬�����Ϊ�գ���˵���Ѿ��ѳ�ʼ�Ŀ����Ϣ��ȡ������
				UFDouble lastnum2 = new UFDouble(0);
				UFDouble lastsum2 = new UFDouble(0);
				if (instockbalancevo.getVdef1() == null) {
					jisuanjdkcUtils utis = new jisuanjdkcUtils();
					// ���飬��һ��Ϊ�������ڶ���Ϊ���
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
				// ��¼̨��

				this.saveSellTally(tradevo);
				// ���¿��
				balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
			}
			// ����ת��ʱ��ֻ�������ʵ�ʿ�����������为ծ�������
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
				 * �˴�ƴ��key�������ҵ����
				 */
				hckey = costingtool.getCurrbilltypegroupvo()
						.getPk_billtypegroup() + hckey;
				hckey = hckey + tradevo.getPk_securities();
				BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
				queryvo.setKey(hckey);
				queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
				queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
				queryvo.setTrade_date(trade_date);
				// �������
				StockBalanceVO outstockbalancevo = balanceTool
						.getStockbalanceVO(queryvo, costingtool);
				if (outstockbalancevo == null) {
					costingtool.handleException(tradevo, null,
							SystemConst.error[0]);
					return;
				}
				// zpm ʵ������ ���ӿ�ʼ��λ�ò�Ҫ��
				if (outstockbalancevo.getStock_map().get(
						tradevo.getTrade_date().toLocalString()) == null) {
					outstockbalancevo.getStock_map().put(
							tradevo.getTrade_date().toLocalString(),
							outstockbalancevo.getStocks_num());
				}
				//
				// �������
				UFDouble stocks_num = outstockbalancevo.getStocks_num();
				// �����
				UFDouble stocks_sum = outstockbalancevo.getStocks_sum();
				/**
				 * ת�����������ֶ�ԭת��������bargain_num2_his����¼ԭ����ת������������ʱ������ԭ
				 * 
				 * @author cjh
				 * @date 2015-12-08
				 */
				tradevo.setAttributeValue("bargain_num2_his",
						tradevo.getBargain_num2());
				if (tradevo.getBargain_num2() == null
						|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

					tradevo.setBargain_num2(tradevo.getBargain_num());// ���ת������Ϊ�գ���ôת����������ת������
				}

				// ���ʣ������
				stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
						tradevo.getBargain_num());
				// �ж�ת���Ƿ����
				if (stocks_num.compareTo(new UFDouble(0)) < 0) {
					costingtool.handleException(tradevo, null,
							SystemConst.error[1]);
				}
				if (stocks_num.compareTo(new UFDouble(0)) == 0) {// ����
					// ���ʣ������Ϊ0 ����ȫ������ ת�����Ϊ�����
					stocks_sum = outstockbalancevo.getStocks_sum();
				} else {// ���۳ɱ�
					// ���ת������ ����ת�����
					if (isFirstPrice) {// �ȼ��㵥��
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
				 * ֤ȯת����¼���ʱת�������ֵ�����д�����д��ת�����=�����/�������*ת��������ԭ�߼�û�м��ж϶�ֱ�ӻ�д
				 * 
				 * @author cjh
				 * @date 2015-12-07 09��55
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
				 * ֤ȯ���׼�¼���ʱת������ֵ�����д�����д,ԭ�߼�û�м��ж϶�ֱ�ӻ�д
				 * 
				 * @author cjh
				 * @date 2015-12-07 09��55
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
				// ����ת�����
				balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);
				// ���ת��Ŀ��
				String hrkey = getCombinesKey(tradevo, keys, true);
				/**
				 * �˴�ƴ��key�������ҵ����
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
				// ���ת�������
				stocks_num = pm.add(instockbalancevo.getStocks_num(),
						tradevo.getBargain_num2());
				// ���ת��Ľ��
				stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
						tradevo.getBargain_sum2());
				// ����ת����Ϣ
				instockbalancevo.setStocks_num(stocks_num);
				instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true,
						true));
				instockbalancevo.setStocks_tax(pm.add(
						instockbalancevo.getStocks_tax(), taxOutCost));
				// ����ת��Ŀ��
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

	// ��ȡת�����

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
	 * Ĭ����� ҵ������������Ļ���Ҫ��д
	 */
	@Override
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// �������ϵ�Ӧ����Ϣ��ת����Ϣ
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
						.getParent();// �޺���
				if (fathervo.getHc_pk_stocksort() == null) {
					fathervo.setHc_pk_stocksort(costingtool.getPk_stocksort()[0]);// Ӱ����1
				}
				if (fathervo.getHr_pk_stocksort() == null) {
					fathervo.setHr_pk_stocksort(costingtool.getPk_stocksort()[1]);// Ӱ����2
				}
				// vo.setPk_stocksort(vo.getHr_pk_stocksort());// ת����2
				// vo.setPk_assetsprop(vo.getHr_pk_assetsprop());// ת����2
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
		// ��ս�תӦ����Ϣ
		tradevo.setInterest(null);
		// ��ս�ת���ʼ�ֵ
		tradevo.setFairvalue(null);
		// ���ת��Ӧ����Ϣ
		costcalc.clearDistillInterest(costingtool);
		// ��չ��ʼ�ֵ����
		costcalc.clearDistillFv(costingtool);
		// ���ת����ת˰��
		tradevo.setTaxexpense(null);
		/**
		 * ֤ȯת����¼�����ʱ��ת����ת���ת��������ԭ
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

		// ʵ������
		RealratePlugin check = new RealratePlugin();
		if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
			ICalcPluginMaintain mail = NCLocator.getInstance().lookup(
					ICalcPluginMaintain.class);
			mail.unZtgPlugindeal(tradevo, pk_group, pk_org, tradedate);
		}

		// ��ծ�� ����̨�� 20150417 ln
		IZqjdTallyService iZqjd = NCLocator.getInstance().lookup(
				IZqjdTallyService.class);
		iZqjd.deleteBuyTally(tradevo);
	}
}
