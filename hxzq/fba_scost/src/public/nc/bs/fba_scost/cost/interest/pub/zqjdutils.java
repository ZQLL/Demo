package nc.bs.fba_scost.cost.interest.pub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_scost.cost.simtools.CostingTool;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.itf.fba_scost.cost.billcheck.GZISpecialBusiness;
import nc.itf.fba_scost.cost.pub.IBillCheckPlugin;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostForward;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_sim.simtrade.transformtrade.ITransformtrade;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.itf.fba_zqjd.trade.IZqjdtradeMaintain;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pubitf.initgroup.InitGroupQuery;
import nc.tool.fba_zqjd.pub.ZqjdStockBalanceTool;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.interest.AggInterest;
import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_scost.pub.tools.QueryUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.fba_sim.simtrade.transformtrade.AggTransformtradeVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.fba_zqjd.trade.zqjdtrade.AggZqjdVO;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.trade.voutils.SafeCompute;
import nc.vo.trade.voutils.VOUtil;

public class zqjdutils {

	private zqjdStockChangeTool queryInfo = null;
	BaseDAO baseDAO = new BaseDAO();
	private ZqjdStockBalanceTool stockBalanceTool = null;
	private String zdfd = "zdfd";
	// private String hbd = "hb";
	// /** 提前还本 */
	// private String early = "early";
	// /** 提前还本 */
	// private String earlyPrefix = "EARLY";
	// /** 自动还本 */
	// private String autoSuffix = "ZDPAY";
	//
	protected ICostForward costcalc;

	public void zqjdautoAudit(BilltypeGroupVO vo, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {

		CostParaVO costpara = costingtool.getCostParaVO();
		Integer checkstate = costpara.getCheckstate();
		switch (checkstate) {
		case 0:// 审核
			this.auto(vo, costingtool, tradedatatool, checkclasstool);
			break;
		}
	}

	private void auto(BilltypeGroupVO vo, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		List<PendingBillVO> pendlist = costingtool.getBilltypeclass();
		// 在此添加生成债券交易记录后续单据 lqm

		CostParaVO costpara = costingtool.getCostParaVO();
		CostPlanVO costplanvo = costpara.getCostplanvo();
		UFDate trade_date = new UFDate(costingtool.getCurrdate());
		//
		CheckParaVO checkparavo = costpara.getCheckParavo();
		String pk_glorgbook = checkparavo.getPk_glorgbook();
		String pk_group = checkparavo.getPk_group();
		String pk_org = checkparavo.getPk_org();
		// 先校验是否生成归还单
		// this.autoreturn(vo, dataMap ,costingtool, tradedatatool,
		// checkclasstool);
		// 参数控制是否生成兑付单
		boolean falg = this.getBooleanFromInitcode(pk_glorgbook, "SEC40");
		if (!falg)
			return;
		// 生成相关单据 转兑付单、结转单等
		Map<String, List<SuperVO>> map = calcAutoPayInfo(costingtool,
				costplanvo, pk_group, pk_org, trade_date, vo, tradedatatool);
		if (map != null) {
			List<SuperVO> fx = map.get(zdfd);
			String[] pks1 = new BaseDAO().insertVOList(fx);
			// List<SuperVO> hb = map.get(hbd);
			// String[] pks2 = new BaseDAO().insertVOList(hb);
			// List<SuperVO> early = map.get(this.early);
			// // 因为生成的单据已经审核，故无需再次审核
			// new BaseDAO().insertVOList(early);

			addCache(costingtool, tradedatatool, pks1, null);
		}
	}

	public void autoreturn(BilltypeGroupVO vo,
			Map<String, List<PendingBillVO>> dataMap, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		CostParaVO costpara = costingtool.getCostParaVO();
		Integer checkstate = costpara.getCheckstate();
		switch (checkstate) {
		case 0:// 审核
			this.setreturn(vo, dataMap, costingtool, tradedatatool,
					checkclasstool);
			break;
		}

	}

	private void setreturn(BilltypeGroupVO vo,
			Map<String, List<PendingBillVO>> dataMap, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		CostParaVO costpara = costingtool.getCostParaVO();
		//
		CheckParaVO checkParaVO = costpara.getCheckParavo();
		// 将归还单的生成位置移植到这里来 LQM
		List<String> plugins = new ArrayList<String>();
		boolean enable = InitGroupQuery.isEnabled(checkParaVO.getPk_group(),
				"HV17"); // 债券借贷:防止模块未启用，反射报错
		if (enable) {
			plugins.add("nc.bs.fba_scost.cost.interest.pub.GZZqjdAutoReturn");
		}
		// 2015年5月28日 zhaoxmc end
		for (int j = 0; j < plugins.size(); j++) {
			GZISpecialBusiness ispbusiness;
			String sbimpl = plugins.get(j);
			try {
				String[] temp = sbimpl.split("\\.");
				ispbusiness = (GZISpecialBusiness) NewObjectService
						.newInstance(temp[2], sbimpl);
			} catch (Exception e) {
				throw new BusinessException(sbimpl + "实例化失败！" + e.getMessage());
			}
			ISuperVO[] vos = ispbusiness.query(vo, costingtool, tradedatatool,
					checkclasstool);
			if (null != vos && vos.length > 0) {
				// 插入数据并，获取到归还单的 pks
				String[] returnpks = ispbusiness.insert(vo, costingtool, vos);
				// 获取到的转换单的pks
				List<String> zhdlistpks = ispbusiness.queryzhd();
				// 先处理转换单的pks，审核，再处理归还单的pks，审核
				if (zhdlistpks != null && zhdlistpks.size() > 0) {
					this.autozhd(zhdlistpks, vo, dataMap, costingtool,
							tradedatatool, checkclasstool);
				}
				// 再处理归还单
				if (returnpks != null && returnpks.length > 0) {
					autoghd(returnpks, vo, dataMap, costingtool, tradedatatool,
							checkclasstool);
				}
			}

		}
	}

	// 审核转换单
	private void autozhd(List<String> pks, BilltypeGroupVO vo,
			Map<String, List<PendingBillVO>> dataMap, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		// 根据pk重新查询转换单
		AggTransformtradeVO[] aggs = getBusiInstance().queryBillByPK(
				pks.toArray(new String[0]));
		// 审核转换单
		this.costcalc = ((ICostForward) NCLocator.getInstance().lookup(
				ICostForward.class.getName()));
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		if (aggs != null && aggs.length > 0) {
			// 审核之前，先将系统的变量设置完整 checkclasstool costingtool
			// 将系统变量更新
			SetaddreturnUtils setUtils = new SetaddreturnUtils();
			setUtils.AddBillTypeClass(dataMap, "HV3F-0xx-01", vo, costingtool,
					tradedatatool, checkclasstool);
			List billtypeclass = costingtool.getBilltypeclass();
			Map<String, IBillCheckPlugin> checkclassmap = checkclasstool
					.getCheckClassMap();
			if (billtypeclass != null) {
				for (int i = 0; i < billtypeclass.size(); i++) {
					PendingBillVO pbvo = (PendingBillVO) billtypeclass.get(i);
					// 只审核新增的转换单 ，其他单据已经审核了
					if (pbvo.getPk_billtype().equals("HV3F-0xx-01")) {
						costingtool.setCurrbilltype(pbvo.getPk_billtype());// 设置当前交易类型
						costingtool.setPk_stocksort(new String[] {
								pbvo.getPk_stocksort1(),
								pbvo.getPk_stocksort2() });
						costingtool.setDirection(pbvo.getDirection());
						costingtool.setIsinit(false);// 此处 重新设置缓存标志 非第一次初始化 这个
														// 很重要
														// 考虑弃审以及记账 YangJie
														// 2014-04-24
						IBillCheckPlugin ibillcheck = checkclassmap
								.get(pbvo.getPk_billtype() + "##"
										+ pbvo.getCheckclass());
						try {
							for (IBill ibill : aggs) {
								TransformtradeVO fathervo = (TransformtradeVO) ibill
										.getParent();// 无孩子
								if (fathervo.getHc_pk_stocksort() == null) {
									fathervo.setHc_pk_stocksort(costingtool
											.getPk_stocksort()[0]);// 影响库存1
								}
								if (fathervo.getHr_pk_stocksort() == null) {
									fathervo.setHr_pk_stocksort(costingtool
											.getPk_stocksort()[1]);// 影响库存2
								}
								// vo.setPk_stocksort(vo.getHr_pk_stocksort());//
								// 转入库存2
								// vo.setPk_assetsprop(vo.getHr_pk_assetsprop());//
								// 转入库存2
								calculate(costingtool, fathervo);
								costingtool.updateFunds(fathervo);
								fathervo.setApprover(pk_user);
								fathervo.setApprovedate(PubMethod.getInstance()
										.getDateTime(costingtool.getCurrdate()));
								fathervo.setState(SystemConst.statecode[3]);
							}
							// s ibillcheck.checkBills(costingtool,
							// tradedatatool);
						} catch (Exception e) {
							throw new Exception("审核日期："
									+ costingtool.getCurrdate() + "插件类内部报错:"
									+ ibillcheck.toString() + "错误日志："
									+ e.getMessage());
						}
					}

				}
			}
			String datakey = costingtool.getCurrbilltypegroupvo()
					.getPk_billtypegroup()
					+ costingtool.getCurrdate()
					+ costingtool.getCurrbilltype();
			tradedatatool.addData(datakey, aggs);

		}

	}

	// 审核归还单
	private void autoghd(String[] pks, BilltypeGroupVO vo,
			Map<String, List<PendingBillVO>> dataMap, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		// 根据pk重新查询转换单
		AggZqjdVO[] aggs = getBusiInstance1().queryBillByPK(pks);
		// AggTransformtradeVO[] aggs =
		// getBusiInstance().queryBillByPK(zhdlistpks.toArray(new String[0]));
		// 审核转换单
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		if (aggs != null && aggs.length > 0) {
			// 审核之前，先将系统的变量设置完整 checkclasstool costingtool
			// 将系统变量更新
			SetaddreturnUtils setUtils = new SetaddreturnUtils();
			List billtypeclass1 = costingtool.getBilltypeclass();
			setUtils.AddBillTypeClass(dataMap, "HV7A-0xx-02", vo, costingtool,
					tradedatatool, checkclasstool);

			List billtypeclass = costingtool.getBilltypeclass();

			Map<String, IBillCheckPlugin> checkclassmap = checkclasstool
					.getCheckClassMap();
			if (billtypeclass != null) {
				for (int i = 0; i < billtypeclass.size(); i++) {
					PendingBillVO pbvo = (PendingBillVO) billtypeclass.get(i);
					// 只审核归还单，其他单据已经审核了
					if (pbvo.getPk_billtype().equals("HV7A-0xx-02")
							&& (pbvo.getTrade_date().toString()
									.substring(0, 10)).equals(costingtool
									.getCurrdate())) {
						costingtool.setCurrbilltype(pbvo.getPk_billtype());// 设置当前交易类型
						costingtool.setPk_stocksort(new String[] {
								pbvo.getPk_stocksort1(),
								pbvo.getPk_stocksort2() });
						costingtool.setDirection(pbvo.getDirection());
						costingtool.setIsinit(false);// 此处 重新设置缓存标志 非第一次初始化 这个
														// 很重要
														// 考虑弃审以及记账 YangJie
														// 2014-04-24
						IBillCheckPlugin ibillcheck = checkclassmap
								.get(pbvo.getPk_billtype() + "##"
										+ pbvo.getCheckclass());
						try {
							for (IBill ibill : aggs) {

								AggZqjdVO aggvo = (AggZqjdVO) ibill;
								ZqjdVO vo1 = aggvo.getParentVO();
								// 如果是生成的归还单，则要看是否库存足够，不足够时生成转换单，并审批，然后从新读取
								calculateWhenCheck1(costingtool, vo1);
								costingtool.updateFunds(vo1);
								vo1.setApprover(pk_user);
								vo1.setApprovedate(PubMethod.getInstance()
										.getDateTime(costingtool.getCurrdate()));
								vo1.setState(Integer
										.valueOf(nc.vo.fba_sec.pub.SystemConst.statecode[3]));

							}
							// s ibillcheck.checkBills(costingtool,
							// tradedatatool);
						} catch (Exception e) {
							throw new Exception("审核日期："
									+ costingtool.getCurrdate() + "插件类内部报错:"
									+ ibillcheck.toString() + "错误日志："
									+ e.getMessage());
						}
					}

				}
			}
			String datakey = costingtool.getCurrbilltypegroupvo()
					.getPk_billtypegroup()
					+ costingtool.getCurrdate()
					+ costingtool.getCurrbilltype();
			tradedatatool.addData(datakey, aggs);
		}
	}

	private ZqjdStockBalanceTool getStockBalanceTool() {
		if (stockBalanceTool == null) {
			stockBalanceTool = ZqjdStockBalanceTool.getInstance();
		}
		return stockBalanceTool;
	}

	private void checkQuantityAndMoney(StockBalanceVO stockBalanceVO,
			ZqjdVO tradevo) throws ValidationException {

		List<IValidationService> valList = new ArrayList<IValidationService>();
		// 校验库存是否足够
		zqjdStockEnoughValidation stockEnoughValidation = new zqjdStockEnoughValidation();
		stockEnoughValidation.setStockBalanceVO(stockBalanceVO);
		valList.add(stockEnoughValidation);
		// 校验是否超归还
		zqjdOverReturnValidation overReturnValidation = new zqjdOverReturnValidation();
		valList.add(overReturnValidation);

		UtilsZqjdCompositeValidation validations = new UtilsZqjdCompositeValidation();
		validations.setValidators(valList);

		validations.validate(tradevo);

	}

	private void calculateWhenCheck1(ICostingTool costingtool, ZqjdVO tradevo)
			throws Exception {
		// 补充默认值
		// tradevo = DefaultValTool.setDefaultVal(tradevo);

		// 查询、更新都需要qryVO
		BanlanceQueryKeyVO queryKeyVO = getStockBalanceTool()
				.createQryBalanceVO(costingtool, tradevo);
		// 查询库存
		StockBalanceVO stockBalanceVO = getStockBalanceTool().qryStockBalnce(
				queryKeyVO, costingtool, tradevo);

		checkQuantityAndMoney(stockBalanceVO, tradevo);

		// 处理库存 减少
		UFDouble befStockNum = stockBalanceVO.getStocks_num();
		UFDouble befStockSum = stockBalanceVO.getStocks_sum();
		stockBalanceVO.setStocks_num(SafeCompute.sub(befStockNum,
				tradevo.getBargain_num()));
		stockBalanceVO.setStocks_sum(SafeCompute.sub(befStockSum,
				tradevo.getBargain_sum()));
		// 更新库存
		getStockBalanceTool().updateStockBalace(queryKeyVO, costingtool,
				stockBalanceVO);

	}

	private IZqjdtradeMaintain getBusiInstance1() {
		IZqjdtradeMaintain iBondbuy = NCLocator.getInstance().lookup(
				IZqjdtradeMaintain.class);
		return iBondbuy;
	}

	protected String getCombinesKey(TransformtradeVO tradevo, String[] keys,
			boolean ishr) {
		List<String> newkeys = new ArrayList<String>();
		if (ishr) {
			for (String key : keys) {
				if (key.equals("pk_group") || key.equals("pk_org")
						|| key.equals("pk_glorgbook")) {
					newkeys.add(key);
				} else {
					newkeys.add("hr_" + key);
				}
			}

		} else {
			for (String key : keys) {
				if (key.equals("pk_group") || key.equals("pk_org")
						|| key.equals("pk_glorgbook")) {
					newkeys.add(key);
				} else {
					newkeys.add("hc_" + key);
				}
			}
		}
		return VOUtil.getCombinesKey(tradevo, newkeys.toArray(new String[0]));
	}

	// 生成的转换单审批是处理
	protected void calculate(ICostingTool costingtool, TransformtradeVO tradevo)
			throws Exception {
		PubMethod pm = PubMethod.getInstance();
		boolean isFirstPrice = costingtool.getCostParaVO().getFirstPrice();
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool balanceTool = costingtool.getBalancetool();
		String[] keys = new String[costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray().length - 1];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = costingtool.getCostParaVO().getCostplanvo()
					.getCostFieldArray()[i];
		}
		String hckey = getCombinesKey(tradevo, keys, false);
		/**
		 * 此处拼接key必须加上业务组
		 */
		hckey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hckey;
		hckey = hckey + tradevo.getPk_securities();
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(hckey);
		queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
		queryvo.setTrade_date(trade_date);
		// 库存余额表
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}

		// zpm 实际利率 增加开始，位置不要动
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}

		// 计算结转应收利息

		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// 计算结转公允价值
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
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
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

			tradevo.setBargain_num2(tradevo.getBargain_num());// 如果转入数量为空，那么转入数量等于转出数量
		}
		// 获得剩余数量
		stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		// 判断转出是否合理
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// 卖空
			// 如果剩余数量为0 代表全部卖出 转出金额为库存金额
			stocks_sum = outstockbalancevo.getStocks_sum();
		} else {// 销售成本
			// 如果转出合理 计算转出金额
			if (isFirstPrice) {// 先计算单价
				stocks_sum = pm.div(outstockbalancevo.getStocks_sum(),
						outstockbalancevo.getStocks_num());
				stocks_sum = pm.multiply(stocks_sum, tradevo.getBargain_num());
			} else {
				stocks_sum = pm.multiply(outstockbalancevo.getStocks_sum(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum,
						outstockbalancevo.getStocks_num());
			}
			stocks_sum = pm.setScale(stocks_sum, true, true);
		}
		UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo, costingtool,
				tradevo.getBargain_num());
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
		UFDouble lastnum = new UFDouble(0);
		UFDouble lastsum = new UFDouble(0);
		hrkey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hrkey;
		hrkey = hrkey + tradevo.getPk_securities2();
		queryvo.setKey(hrkey);
		queryvo.setPk_assetsprop(tradevo.getHr_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHr_pk_stocksort());
		StockBalanceVO instockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (instockbalancevo == null) {
			instockbalancevo = balanceTool.getStockbalanceVOByVO(tradevo,
					costingtool);
			instockbalancevo.setPk_securities(tradevo.getPk_securities2());
		}
		/******* 资产转负债时，减少负债库存数量 ***************/
		// 转入的肯定是债券，判断债券是否有负债，如果没有，获取初始负债消息
		if (instockbalancevo.getVdef1() == null) {
			scoxtjisuanjdkcUtils utis = new scoxtjisuanjdkcUtils();
			// 数组，第一个为数量，第二个为金额
			UFDouble[] jsfz = utis.jisuanfzqc(instockbalancevo);
			lastnum = jsfz[0];
			lastsum = jsfz[1];
		} else {
			lastnum = new UFDouble(instockbalancevo.getVdef1());
			lastsum = new UFDouble(instockbalancevo.getVdef2());

		}

		/*******************************************/

		// 获得转入的数量
		stocks_num = pm.add(instockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// 获得转入的金额
		stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());
		// 保存转入信息
		instockbalancevo.setStocks_num(stocks_num);
		instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));
		instockbalancevo.setStocks_tax(pm.add(instockbalancevo.getStocks_tax(),
				taxOutCost));
		// 更新转入的库存
		balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
		// 处理利息余额
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// if(tradevo.getPk_busitype()!=null){
		// 查看是否为资产转负债,如果是资产转负载,则转入的负债要按照负债比例结转应收计提利息
		scostZrlxUtils utils = new scostZrlxUtils();
		UFDouble zrlx = utils.forwardInterestDistill(costingtool,
				outstockbalancevo, tradevo);
		if (lastnum.compareTo(new UFDouble(0)) > 0) {
			tradevo.setVdef10(((zrlx.multiply(tradevo.getBargain_num2().div(
					new UFDouble(lastnum)))).setScale(2, 4)).toString());
		} else {
			tradevo.setVdef10(new UFDouble(0).toString());
		}

		instockbalancevo.setVdef1(lastnum.sub(tradevo.getBargain_num2())
				.toString());
		instockbalancevo.setVdef2(lastsum.sub(tradevo.getBargain_sum2())
				.toString());
		// 保存台账
		this.saveSellTally(tradevo);
		// 存取应收利息 资产转负债0401 资产需要结转应收利息（转出1，减少），负债也需要结转应收利息（转出1，减少）lqm---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// 保存公允价值---
		costcalc.saveFairValueDistill(costingtool, tradevo);
		// 实际利率
		ScostRealratePlugin check = new ScostRealratePlugin();
		if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
			ICalcPluginMaintain mail = NCLocator.getInstance().lookup(
					ICalcPluginMaintain.class);
			mail.ztgPlugindeal(outstockbalancevo, tradevo);
		}
	}

	private BaseDAO getBaseDAO() {
		if (this.baseDAO == null) {
			this.baseDAO = new BaseDAO();
		}
		return this.baseDAO;
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

	private ITransformtrade getBusiInstance() {
		ITransformtrade itransformtrade = NCLocator.getInstance().lookup(
				ITransformtrade.class);
		return itransformtrade;

	}

	private Map<String, List<SuperVO>> calcAutoPayInfo(CostingTool costingtool,
			CostPlanVO costplanvo, String pk_group, String pk_org,
			UFDate trade_date, BilltypeGroupVO vo, TradeDataTool tradedatatool)
			throws BusinessException {

		getQueryInfo();
		// 查询债券借贷库存信息
		String sql_bow_now = zqjdStockChangeTool.getNowSql("HV7A-0xx-01",
				trade_date.toString());
		ArrayList<ZqjdVO> nowBowResult = (ArrayList<ZqjdVO>) baseDAO
				.executeQuery(sql_bow_now, new BeanListProcessor(ZqjdVO.class));

		if (nowBowResult == null || nowBowResult.size() == 0)
			return null;
		Map<String, List<SuperVO>> map = new HashMap<String, List<SuperVO>>();
		// 轮循库存信息
		for (ZqjdVO v : nowBowResult) {
			buildHbFxBill(pk_group, pk_org, v, trade_date, map, vo,
					tradedatatool, costingtool);
		}

		return map;
	}

	/**
	 * 查询当前债券是否还本到期
	 */
	private void buildHbFxBill(String pk_group, String pk_org, ZqjdVO zqjdvo,
			UFDate trade_date, Map<String, List<SuperVO>> map,
			BilltypeGroupVO vo, TradeDataTool tradedatatool,
			CostingTool costingtool) throws BusinessException {
		// 当前时期的借入单是否利息结转日期
		AggInterest agginfo = getQueryInfo().queryInterestsetInfo(
				zqjdvo.getPk_securities());
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		// 是否是付息日
		boolean fx = InterestTools.isFx(ratevos, trade_date);
		List<SuperVO> list1 = new ArrayList<SuperVO>();
		if (fx) {
			// 生成转兑付单
			SuperVO v1 = genZDFBill(pk_group, pk_org, zqjdvo, trade_date);
			list1.add(v1);
		}

		if (map.containsKey(zdfd)) {
			map.get(zdfd).addAll(list1);
		} else {
			map.put(zdfd, list1);
		}

	}

	/**
	 * 加入缓存
	 */
	@SuppressWarnings("unchecked")
	public void addCache(CostingTool costingtool, TradeDataTool tradedatatool,
			String[] pks1, String[] pks2) throws BusinessException {

		try {
			if (tradedatatool.isIsinit()) {
				Class<IBill> cs = (Class<IBill>) Class
						.forName("nc.vo.fba_zqjd.trade.zqjdtrade.AggZqjdVO");
				// 查询最新数据1
				if (pks1 != null && pks1.length > 0) {
					IBill[] ibills = QueryUtil.queryBillVOByPks(cs, pks1, true);
					for (int j = 0; j < ibills.length; j++) {
						IBill ibill = ibills[j];
						String key = costingtool.getCurrbilltypegroupvo()
								.getPk_billtypegroup()
								+ ((UFDate) (ibill.getParent()
										.getAttributeValue("trade_date")))
										.toLocalString() + "HV7A-0xx-06";
						tradedatatool.addData(key, ibill);
					}
				}

			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 生成转对付单
	 */
	public SuperVO genZDFBill(String pk_group, String pk_org, ZqjdVO zqjdvo,
			UFDate trade_date) throws BusinessException {
		SuperVO vo = constructVO();

		vo.setAttributeValue("billno", getBillNO(pk_group, pk_org, vo, "ZDF"));
		vo.setAttributeValue("transtypecode", "HV7A-0xx-06");
		vo.setAttributeValue("pk_assetsprop", zqjdvo.getPk_assetsprop());
		vo.setAttributeValue("pk_capaccount", zqjdvo.getPk_capaccount());
		vo.setAttributeValue("pk_securities", zqjdvo.getPk_securities());
		vo.setAttributeValue("buildofstate", "N");
		vo.setAttributeValue("vbillstatus", -1);
		vo.setAttributeValue("pk_glorgbook", zqjdvo.getPk_glorgbook());
		vo.setAttributeValue("pk_stocksort", zqjdvo.getPk_stocksort());
		vo.setAttributeValue("bargain_num", zqjdvo.getBargain_num());
		// 获取证券信息
		SecuritiesVO secvo = (SecuritiesVO) new BaseDAO().retrieveByPK(
				SecuritiesVO.class, zqjdvo.getPk_securities());
		AggInterest agginfo = getQueryInfo().queryInterestsetInfo(
				zqjdvo.getPk_securities());
		// zqjdvo.getBargain_sum()
		vo.setAttributeValue("bargain_sum",
				calcLX(zqjdvo.getBargain_num(), agginfo, trade_date));
		vo.setAttributeValue("pk_bourse", secvo.getPk_bourse());
		vo.setAttributeValue("pk_currtype", secvo.getPk_currtype());
		BilltypeVO bd = getBdBilltypeVO("HV7A");
		BilltypeVO billtypeVO = PfDataCache.getBillTypeInfo("HV7A-0xx-06");
		vo.setAttributeValue("pk_transtype", billtypeVO.getPk_billtypeid());
		vo.setAttributeValue("transtypecode", "HV7A-0xx-06");
		vo.setAttributeValue("pk_billtype", bd.getPk_billtypeid());
		vo.setAttributeValue("billtypecode", bd.getPk_billtypecode());
		vo.setAttributeValue("creationtime", AppContext.getInstance()
				.getServerTime());
		vo.setAttributeValue("creator", AppContext.getInstance().getPkUser());
		vo.setAttributeValue("billmaker", AppContext.getInstance().getPkUser());
		// vo.setAttributeValue("approver",
		// AppContext.getInstance().getPkUser());
		vo.setAttributeValue("pk_group", zqjdvo.getPk_group());
		vo.setAttributeValue("pk_org", zqjdvo.getPk_org());
		vo.setAttributeValue("pk_org_v", zqjdvo.getPk_org_v());
		vo.setAttributeValue("state", SystemConst.statecode[1]);
		vo.setAttributeValue("trade_date", trade_date);
		vo.setAttributeValue("dr", 0);
		vo.setAttributeValue("makedate", AppContext.getInstance()
				.getServerTime());
		// vo.setAttributeValue("approvedate",
		// AppContext.getInstance().getServerTime());
		return vo;
	}

	/**
	 * 计算利息<br>
	 * <b>这个更新的原因是做了提前还本，但是利息的计算并没有按照提前还本的比例去更新。</b>
	 * 
	 * @param stockvo
	 *            库存信息
	 * @param agginfo
	 *            利率设置信息
	 * @param trade_date
	 *            交易日期
	 * @author 未知
	 * @update jingqt <i>2016年4月22日14:50:52 </i>
	 */
	public UFDouble calcLX(UFDouble num, AggInterest agginfo, UFDate trade_date) {
		if (agginfo == null || num == null)
			return null;
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		Interest headvo = agginfo.getParent();
		if (headvo == null || ratevos == null || ratevos.length == 0)
			return null;
		UFDouble lx = null;
		if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {// 到期一次还本付息----自定义
			for (Rateperiod vo : ratevos) {
				UFDouble lx1 = SafeCompute.multiply(num, vo.getYear_rate());
				// /** 2016年5月6日 JINGQT 取实际的年天数。如果跨年，则取最大的年天数 ADD START */
				// UFDouble days = this.getRealDays(headvo, vo.getStart_day(),
				// vo.getEnd_day());
				// lx = SafeCompute.add(lx,
				// lx1.multiply(vo.getDays_num()).div(days));
				// /** 2016年5月6日 JINGQT 取实际的年天数。如果跨年，则取最大的年天数 ADD END */
				/** 2017年2月21日 xuxmb 兑息期间不足一年的，应按实际天数计算应兑付金额 ADD START */
				if (!isAllYear(vo.getStart_day(), vo.getEnd_day())) {
					// 实际计息天数
					UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
							vo.getEnd_day());
					// 年计息天数
					UFDouble allDays = this.getYearDays(headvo,
							vo.getStart_day(), vo.getEnd_day());
					lx = lx1.multiply(realDays).div(allDays);// update lihaibo
																// 用下面的算法小数点位数不对
					// lx = SafeCompute.multiply(lx1,
					// SafeCompute.div(realDays, allDays));
				} else {
					// 实际计息天数
					UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
							vo.getEnd_day());
					// 年计息天数
					UFDouble days = this.getYearDays(headvo, vo.getStart_day(),
							vo.getEnd_day());
					lx = SafeCompute.add(lx, lx1.multiply(realDays).div(days));
				}
				/** 2017年2月21日 xuxmb 兑息期间不足一年的，应按实际天数计算应兑付金额ADD END */
			}

		} else {
			Rateperiod vo = InterestTools.getContainsDate(ratevos, trade_date);
			/** 2016年4月22日 JINGQT 计算计息需要将提前还本比例考虑进去 ADD START */
			UFDouble hasSurplusPercent = InterestTools.getCostSurplus(ratevos,
					vo);
			/** 2016年4月22日 JINGQT 计算计息需要将提前还本比例考虑进去 ADD END */
			lx = SafeCompute.multiply(num, vo.getYear_rate()).multiply(
					hasSurplusPercent);
			// update by lihaibo 注释掉--采用下面那种计算方式
			/*
			 * if (CostConstant.PAYPREYEAR.equals(headvo.getPeriodtype()) ||
			 * CostConstant.SCP.equals(headvo.getPeriodtype())) {// 按年付息、超短融 lx
			 * = SafeCompute.div(lx, new UFDouble(1)); } else if
			 * (CostConstant.PAYPREHALFYEAR.equals(headvo .getPeriodtype())) {//
			 * 按半年付息 lx = SafeCompute.div(lx, new UFDouble(2)); } else if
			 * (CostConstant.PAYPREQUARTER .equals(headvo.getPeriodtype())) {//
			 * 按季付息 lx = SafeCompute.div(lx, new UFDouble(4)); } else if
			 * (CostConstant.PAYPREMONTH.equals(headvo.getPeriodtype())) {//
			 * 按月付息 lx = SafeCompute.div(lx, new UFDouble(12)); }
			 */
			/** 2017年2月21日 xuxmb 兑息期间不足一年的，应按实际天数计算应兑付金额 ADD START */
			if (!isAllYear(vo.getStart_day(), vo.getEnd_day())) {
				UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
						vo.getEnd_day());// 实际计息天数
				// UFDouble allDays = this.getYearDays(headvo,
				// vo.getStart_day(),
				// vo.getEnd_day());// 年天数
				UFDouble allDays = new UFDouble(vo.getDays_num());
				lx = lx.multiply(realDays).div(allDays);// update lihaibo
														// 用下面的算法小数点位数不对
				// lx = SafeCompute.multiply(lx,
				// SafeCompute.div(realDays, allDays));
			}
			/** 2017年2月21日 xuxmb 兑息期间不足一年的，应按实际天数计算应兑付金额ADD END */
		}
		return lx;
	}

	/**
	 * 按客户要求新添加获取整年天数的方法。
	 * 
	 * @param headvo
	 *            年利率表头
	 * @param startDay
	 *            开始日期
	 * @param endDay
	 *            结束日期
	 * @return 实际天数
	 * @author xuxmb
	 * @since 2017-2-24 上午8:59:21
	 */
	private UFDouble getYearDays(Interest headvo, UFDate start_date,
			UFDate end_date) {
		String paytype = headvo.getPaytype();
		int days_sum = 365;
		if ("02".equals(paytype)) {// 到期一次还本付息
			for (int k = start_date.getYear(); k < end_date.getYear(); k++) {
				if (UFDate.isLeapYear(k)) {
					days_sum = 366;
				} else {
					days_sum = 365;
				}
			}
		} else if (UFDate.isLeapYear(start_date.getYear() + 1)
				&& start_date.compareTo(new UFDate(start_date.getYear()
						+ "-02-28")) > 0) {
			days_sum = 366;
		} else if (UFDate.isLeapYear(start_date.getYear())
				&& start_date.compareTo(new UFDate(start_date.getYear()
						+ "-02-28")) < 0) {
			days_sum = 366;
		} else {
			days_sum = 365;
		}
		return new UFDouble(days_sum);
	}

	/**
	 * 获取年利率对应的实际天数,开始日期至结束日期的实际天数
	 * 
	 * @param startDay
	 *            开始日期
	 * @param endDay
	 *            结束日期
	 * @return 实际天数
	 * @author xuxmb
	 * @since 20170221
	 */
	private UFDouble getRealDaysByVO(UFDate startDay, UFDate endDay) {
		int days_sum = UFDate.getDaysBetween(startDay, endDay) + 1;
		return new UFDouble(days_sum);
	}

	/**
	 * 判断开始日期至结束日期是否满一年
	 * 
	 * @param startDay
	 *            开始日期
	 * @param endDay
	 *            结束日期
	 * @return true:满一年 false:不足一年
	 * @author xuxmb
	 * @since 20170221
	 */
	private boolean isAllYear(UFDate startDay, UFDate endDay) {
		if (UFDate.getDaysBetween(startDay, endDay) + 1 < 365) {
			return false;
		}
		return true;
	}

	/**
	 * 查询影响因素单据类型VO
	 */
	private BilltypeVO getBdBilltypeVO(String code) throws BusinessException {
		QueryBillTypeInfo info = new QueryBillTypeInfo();
		return info.queryBdBilltypeVO(code);
	}

	/**
	 * 获取单据号 fv不得空，否则直接返回为空
	 */
	public String getBillNO(String pk_group, String pk_org, SuperVO fv,
			String suffix) throws BusinessException {
		String vbillcode = null;
		try {
			IBillcodeManage ser = NCLocator.getInstance().lookup(
					IBillcodeManage.class);
			vbillcode = ser.getBillCode_RequiresNew("HV7A", pk_group, pk_org,
					fv);
		} catch (BusinessException be) {
			ExceptionHandler.handleException(be);
		}
		if (vbillcode != null) {
			vbillcode = suffix + vbillcode;
		}
		return vbillcode;
	}

	public SuperVO constructVO() throws BusinessException {
		Class<?> cls = null;
		SuperVO vo = null;
		try {
			cls = Class.forName("nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO");
			vo = (SuperVO) cls.newInstance();
		} catch (Exception e) {
			throw new BusinessException("StocktradeVO 类实例化错误!");
		}
		return vo;
	}

	public zqjdStockChangeTool getQueryInfo() {
		if (queryInfo == null) {
			queryInfo = new zqjdStockChangeTool();
		}
		return queryInfo;
	}

	private boolean getBooleanFromInitcode(String pk_glorgbook, String initcode)
			throws BusinessException {
		boolean falg = false;
		String va = null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select value from pub_sysinit where pk_org  = '"
				+ pk_glorgbook + "' " + " and initcode = '" + initcode
				+ "' and isnull(dr,0) = 0 ");
		try {
			va = (String) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							String name = null;
							if (rs.next()) {
								name = rs.getString("value");
							}
							return name;
						}
					});
		} catch (DAOException e) {
			throw ExceptionHandler.handleException(super.getClass(), e);
		}
		if (va == null)
			throw ExceptionHandler.createException("当前组织下，参数:" + initcode
					+ "，没有设置! ");
		if ("Y".equals(va))
			falg = true;
		return falg;
	}
}
