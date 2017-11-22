package nc.bs.fba_scost.cost.interest.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.fba_scost.cost.fairvaluedistill.bp.FairValueForward;
import nc.bs.fba_scost.cost.pub.CostForwardImpl;
import nc.bs.fba_scost.cost.simtools.CostingTool;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostForward;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.interest.AggInterest;
import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.ForwardVO;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_scost.pub.tools.QueryUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.fba_sec.secbd.secbilltype.BillTypeVO;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.trade.voutils.SafeCompute;
import nc.vo.trade.voutils.VOUtil;

/**
 * 自动还本付息
 * 
 */
public class AutoPay {
	BaseDAO dao = new BaseDAO();

	private QueryInterestBaseInfo queryInfo = null;

	private String fxd = "fx";
	private String hbd = "hb";
	/** 提前还本 */
	private String early = "early";
	/** 提前还本 */
	private String earlyPrefix = "EARLY";
	/** 自动还本 */
	private String autoSuffix = "ZDPAY";

	public void autoPay(CostingTool costingtool, TradeDataTool tradedatatool)
			throws BusinessException {
		CostParaVO costpara = costingtool.getCostParaVO();
		Integer checkstate = costpara.getCheckstate();
		switch (checkstate) {
		case 0:// 审核
			autoAudit(costingtool, tradedatatool);
			AutoExpire iAutoExpire = new AutoExpire();
			iAutoExpire.autoExpire(costingtool, tradedatatool);
			break;
		case 1:// 记账
			break;
		case 2:// 弃审
			autoUnAudit(costingtool);
			break;
		default:
			break;
		}
	}

	public void autoUnAudit(CostingTool costingtool) throws BusinessException {
		// UFDate trade_date = new UFDate(costingtool.getCurrdate());
		// String sql =
		// " delete from  sim_stocktrade where billno like 'ZDPAY%' and trade_date  >= '"
		// + trade_date.asBegin() + "'  ";
		// new BaseDAO().executeUpdate(sql);
	}

	public void autoAudit(CostingTool costingtool, TradeDataTool tradedatatool)
			throws BusinessException {
		List<PendingBillVO> pendlist = costingtool.getBilltypeclass();
		if (!isFxpay(pendlist))
			return;
		CostParaVO costpara = costingtool.getCostParaVO();
		CostPlanVO costplanvo = costpara.getCostplanvo();
		UFDate trade_date = new UFDate(costingtool.getCurrdate());
		//
		CheckParaVO checkparavo = costpara.getCheckParavo();
		String pk_glorgbook = checkparavo.getPk_glorgbook();
		String pk_group = checkparavo.getPk_group();
		String pk_org = checkparavo.getPk_org();
		boolean falg = getQueryInfo().getBooleanFromInitcode(pk_glorgbook,
				CostConstant.PARAM_HBFX);
		if (!falg)
			return;
		Map<String, List<SuperVO>> map = calcAutoPayInfo(costingtool,
				costplanvo, pk_group, pk_org, trade_date);
		if (map != null) {
			List<SuperVO> fx = map.get(fxd);
			String[] pks1 = new BaseDAO().insertVOList(fx);
			List<SuperVO> hb = map.get(hbd);
			String[] pks2 = new BaseDAO().insertVOList(hb);
			List<SuperVO> early = map.get(this.early);
			// 因为生成的单据已经审核，故无需再次审核
			new BaseDAO().insertVOList(early);
			addCache(costingtool, tradedatatool, pks1, pks2);
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
						.forName("nc.vo.fba_sim.simtrade.stocktrade.AggStocktradeVO");
				// 查询最新数据1
				if (pks1 != null && pks1.length > 0) {
					IBill[] ibills = QueryUtil.queryBillVOByPks(cs, pks1, true);
					for (int j = 0; j < ibills.length; j++) {
						IBill ibill = ibills[j];
						String key = costingtool.getCurrbilltypegroupvo()
								.getPk_billtypegroup()
								+ ((UFDate) (ibill.getParent()
										.getAttributeValue("trade_date")))
										.toLocalString()
								+ CostConstant.FX_TRANSTYPE;
						tradedatatool.addData(key, ibill);
					}
				}
				if (pks2 != null && pks2.length > 0) {
					// 查询最新数据2
					IBill[] ibills = QueryUtil.queryBillVOByPks(cs, pks2, true);
					for (int j = 0; j < ibills.length; j++) {
						IBill ibill = ibills[j];
						String key = costingtool.getCurrbilltypegroupvo()
								.getPk_billtypegroup()
								+ ((UFDate) (ibill.getParent()
										.getAttributeValue("trade_date")))
										.toLocalString()
								+ CostConstant.SELL_TRANSTYPE;
						tradedatatool.addData(key, ibill);
					}
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public boolean isFxpay(List<PendingBillVO> pendlist) {
		boolean falg = false;
		if (pendlist != null && pendlist.size() > 0) {
			for (PendingBillVO v : pendlist) {
				if (v.getPk_billtype().equals(CostConstant.FX_TRANSTYPE)) {
					falg = true;
				}
			}
		}
		return falg;
	}

	public Map<String, List<SuperVO>> calcAutoPayInfo(CostingTool costingtool,
			CostPlanVO costplanvo, String pk_group, String pk_org,
			UFDate trade_date) throws BusinessException {
		List<StockBalanceVO> list = getQueryInfo().queryLastStock(costplanvo,
				pk_group, pk_org, trade_date);
		if (list == null || list.size() == 0)
			return null;
		Map<String, List<SuperVO>> map = new HashMap<String, List<SuperVO>>();
		for (StockBalanceVO v : list) {
			buildHbFxBill(pk_group, pk_org, v, trade_date, map);
			this.buildEarlyRepayment(costingtool, pk_group, pk_org, v,
					trade_date, map);
		}
		return map;
	}

	/**
	 * 查询当前债券是否还本到期
	 */
	public void buildHbFxBill(String pk_group, String pk_org,
			StockBalanceVO stockvo, UFDate trade_date,
			Map<String, List<SuperVO>> map) throws BusinessException {
		AggInterest agginfo = getQueryInfo().queryInterestsetInfo(
				stockvo.getPk_securities());
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		boolean hbfx = InterestTools.isHbFx(ratevos, trade_date);
		boolean fx = InterestTools.isFx(ratevos, trade_date);
		List<SuperVO> list1 = new ArrayList<SuperVO>();
		List<SuperVO> list2 = new ArrayList<SuperVO>();
		if (hbfx) {
			UFDouble lx = calcLX(stockvo, agginfo, trade_date);
			SuperVO v1 = genFxBill(pk_group, pk_org, stockvo, trade_date, lx);
			SuperVO v2 = genHbFxBill(pk_group, pk_org, stockvo, trade_date);
			list1.add(v1);
			list2.add(v2);
		} else if (fx) {
			UFDouble lx = calcLX(stockvo, agginfo, trade_date);
			SuperVO v1 = genFxBill(pk_group, pk_org, stockvo, trade_date, lx);
			list1.add(v1);
		}
		if (map.containsKey(fxd)) {
			map.get(fxd).addAll(list1);
		} else {
			map.put(fxd, list1);
		}
		if (map.containsKey(hbd)) {
			map.get(hbd).addAll(list2);
		} else {
			map.put(hbd, list2);
		}
	}

	private void buildEarlyRepayment(CostingTool costingtool, String pk_group,
			String pk_org, StockBalanceVO stockvo, UFDate trade_date,
			Map<String, List<SuperVO>> map) throws BusinessException {
		List<SuperVO> lst = new ArrayList<SuperVO>();
		AggInterest agginfo = getQueryInfo().queryInterestsetInfo(
				stockvo.getPk_securities());
		// 获取此证券的利率设置信息
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		// 是否需要提前还本并取到提前还本比例
		UFDouble earlyPercent = this.isEarlyRepayment(ratevos, trade_date);
		/** =============qs 从现场代码迁过来的 20170518 add start============== */
		// 取提前还本比例/（1-本次还本前已归还的比例）
		UFDouble earlyPercent2 = this.isEarlyRepayment2(ratevos, trade_date);
		/** =============qs 从现场代码迁过来的 20170518 add end============== */
		// 此时是否需要生成还本单（最后一期）
		boolean hbfx = InterestTools.isHbFx(ratevos, trade_date);
		// 不需要提前还本
		if (earlyPercent.equals(UFDouble.ZERO_DBL) || hbfx) {
			if (map.containsKey(this.early)) {
				map.get(this.early).addAll(lst);
			} else {
				map.put(this.early, lst);
			}
			return;
		}
		// 需要提前还本
		// 1、生成卖出数量为0的卖出单，其中的公允价值和成本需要正常计算
		/**
		 * =============qs 从现场代码迁过来的：加了一个参数earlyPercent2。 20170518 add
		 * start==============
		 */
		SuperVO bill = this.getEarlyBill(costingtool, pk_group, pk_org,
				stockvo, trade_date, earlyPercent, earlyPercent2);
		/**
		 * =============qs 从现场代码迁过来的：加了一个参数earlyPercent2。 20170518 add
		 * end==============
		 */
		lst.add(bill);
		if (map.containsKey(this.early)) {
			map.get(this.early).addAll(lst);
		} else {
			map.put(this.early, lst);
		}
	}

	/**
	 * 生成提前还本单
	 * 
	 * @param costingtool
	 *            审核工具类
	 * @param pk_group
	 *            组织
	 * @param pk_org
	 *            集团
	 * @param stockvo
	 *            库存
	 * @param trade_date
	 *            还本日期
	 * @param earlyPercent
	 *            提前还本比例
	 * @return 审核的单据（此单据是用EARLY开头的）
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2015-10-30 上午11:05:47
	 */
	private SuperVO getEarlyBill(CostingTool costingtool, String pk_group,
			String pk_org, StockBalanceVO stockvo, UFDate trade_date,
			UFDouble earlyPercent, UFDouble earlyPercent2)
			throws BusinessException {
		SuperVO vo = constructVO();
		// 交易数量为0
		vo.setAttributeValue("bargain_num", UFDouble.ZERO_DBL);
		// 交易金额为0
		vo.setAttributeValue("bargain_sum", UFDouble.ZERO_DBL);
		vo.setAttributeValue("billno",
				this.getBillNO(pk_group, pk_org, vo, this.earlyPrefix));
		vo.setAttributeValue("creationtime", AppContext.getInstance()
				.getServerTime());
		vo.setAttributeValue("creator", AppContext.getInstance().getPkUser());
		vo.setAttributeValue("billmaker", AppContext.getInstance().getPkUser());
		//
		BilltypeVO bd = this.getBdBilltypeVO(CostConstant.FXHB_BILLTYPE);
		// BillTypeVO sec = getSecBilltypeVO(CostConstant.SELL_TRANSTYPE);
		BilltypeVO billtypeVO = PfDataCache
				.getBillTypeInfo(CostConstant.SELL_TRANSTYPE);
		vo.setAttributeValue("pk_transtype", billtypeVO.getPk_billtypeid());
		vo.setAttributeValue("transtypecode", CostConstant.SELL_TRANSTYPE);

		vo.setAttributeValue("pk_billtype", bd.getPk_billtypeid());
		vo.setAttributeValue("billtypecode", bd.getPk_billtypecode());
		//
		vo.setAttributeValue("pk_glorgbook", stockvo.getPk_glorgbook());
		// 写入资产属性
		String assetsprop = stockvo.getPk_assetsprop();
		vo.setAttributeValue("pk_assetsprop", assetsprop);
		vo.setAttributeValue("pk_stocksort", stockvo.getPk_stocksort());
		vo.setAttributeValue("pk_partnaccount", stockvo.getPk_partnaccount());
		vo.setAttributeValue("pk_capaccount", stockvo.getPk_capaccount());
		vo.setAttributeValue("pk_client", stockvo.getPk_client());
		vo.setAttributeValue("pk_operatesite", stockvo.getPk_operatesite());
		vo.setAttributeValue("pk_selfsgroup", stockvo.getPk_selfsgroup());
		vo.setAttributeValue("pk_securities", stockvo.getPk_securities());

		SecuritiesVO secvo = (SecuritiesVO) new BaseDAO().retrieveByPK(
				SecuritiesVO.class, stockvo.getPk_securities());
		vo.setAttributeValue("stockcode", secvo.getCode());
		vo.setAttributeValue("pk_bourse", secvo.getPk_bourse());
		vo.setAttributeValue("pk_currtype", secvo.getPk_currtype());

		vo.setAttributeValue("pk_group", stockvo.getPk_group());
		vo.setAttributeValue("pk_org", stockvo.getPk_org());
		vo.setAttributeValue("pk_org_v", stockvo.getPk_org_v());

		/** 填写基本数据 2015年10月30日 */
		// // 制单日期就是还本日期
		// vo.setAttributeValue("trade_date", trade_date);
		// vo.setAttributeValue("dr", 0);
		// // 制单日期就是还本日期
		// vo.setAttributeValue("makedate", trade_date);
		// // COMMENT 销售成本等比例结转
		// UFDouble sum = this.calcTi(stockvo.getStocks_sum(), earlyPercent);
		// vo.setAttributeValue("sellcost", sum);
		/** qs 从现场代码合并过来的：销售成本等比例结转时，改传earlyPercent2 20170518 update start */
		// 交易日期就是还本日期
		vo.setAttributeValue("trade_date", trade_date);
		vo.setAttributeValue("dr", 0);
		// 制单日期就是登录日期
		vo.setAttributeValue("makedate", AppContext.getInstance()
				.getServerTime());
		// COMMENT 销售成本等比例结转
		UFDouble sum = this.calcTi(stockvo.getStocks_sum(), earlyPercent2);
		vo.setAttributeValue("sellcost", sum);
		/** qs 从现场代码合并过来的：销售成本等比例结转时，改传earlyPercent2 20170518 update end */
		// 回写销售成本
		ICostBalanceTool tool = costingtool.getBalancetool();
		// /////////////////////////////////////////////
		String[] costFieldArray = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		String key = VOUtil.getCombinesKey(vo, costFieldArray);
		// 此处拼接key必须加上业务组
		key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + key;
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(key);
		queryvo.setPk_assetsprop(stockvo.getPk_assetsprop());
		queryvo.setPk_stocksort(stockvo.getPk_stocksort());
		queryvo.setTrade_date(trade_date.toLocalString());
		// //////////////////////////////////////////////
		stockvo.setStocks_sum(stockvo.getStocks_sum().sub(sum));
		stockvo.setTrade_date(trade_date);
		tool.updateStockbalanceVO(queryvo, stockvo);

		// COMMENT 公允价值等比例结转
		FairValueForward fvf = new FairValueForward();
		ForwardVO fvo = fvf.forwardVOinfo(costingtool, stockvo,
				(ITrade_Data) vo);
		// 结转的值
		UFDouble ti = this.calcTi(fvo.getFairvalue(), earlyPercent);
		// 回写公允价值
		fvo.setFairvalue(fvo.getFairvalue().sub(ti));
		vo.setAttributeValue("fairvalue", ti);
		vo.setAttributeValue("fairvaluedate", AppContext.getInstance()
				.getServerTime());
		// 保存公允价值---
		ICostForward costcalc = new CostForwardImpl();
		costcalc.saveFairValueDistill(costingtool, (ITrade_Data) vo);
		// COMMENT 杨雪说实际收付需要是票面价值x还本比例
		UFDouble fact_sum = this.calcTi(
				stockvo.getStocks_num().multiply(new UFDouble(100.0D)),
				earlyPercent);
		vo.setAttributeValue("fact_sum", fact_sum);
		/** 债券有提前还本比例的，让其生成的单据卖出单（还本）的单据上的成交金额等于实际收付 xuxmb添加 20170217 start */
		// 成交金额为实际收付
		vo.setAttributeValue("bargain_sum", fact_sum);
		/** 债券有提前还本比例的，让其生成的单据卖出单（还本）的单据上的成交金额等于实际收付 xuxmb添加 20170217 end */
		try {
			// 这个的这个单据已经审核了
			vo.setAttributeValue("state", SystemConst.statecode[3]);
			// 审核的字段
			vo.setAttributeValue("approver", AppContext.getInstance()
					.getPkUser());
			vo.setAttributeValue("approvedate", AppContext.getInstance()
					.getServerTime());
			/** 如果是可供或持有到期，则需要判断实际利率pk_assetsprop */
			/** 判断是资产类还是负债类，如果是负债类，则暂时先不做处理----by yangxue，2015年10月30日 */
			if (this.isAsset(assetsprop)) {
				// COMMENT 实际利率
				String result = SysInitCache.getInstance().getSysInitValue(
						stockvo.getPk_glorgbook(),
						SystemConst.SYSINIT_ISREALRATE);
				if ("Y".equals(result)) {
					ICalcPluginMaintain mail = NCLocator.getInstance().lookup(
							ICalcPluginMaintain.class);
					mail.sellPlugindeal(stockvo, vo);
				}
			}
			// 更新资金变动
			costingtool.updateFunds(vo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException(e);
		}
		return vo;
	}

	/**
	 * 通过 金融属性 判断是资产 还是负债
	 * 
	 * @return
	 */
	private boolean isAsset(String pk_assetsprop) {
		boolean isAsset = false;

		if (pk_assetsprop.equals(SystemConst.PK_ASSETSPROP_0001)
				|| pk_assetsprop.equals(SystemConst.PK_ASSETSPROP_0002)
				|| pk_assetsprop.equals(SystemConst.PK_ASSETSPROP_0003)) {
			isAsset = true;
		}
		return isAsset;
	}

	/**
	 * 计算要提前还本的结转数据
	 * 
	 * @param target
	 *            要结转的数据
	 * @param earlyPercent
	 *            提前还本比例
	 * @return 数据
	 */
	private UFDouble calcTi(UFDouble target, UFDouble earlyPercent) {
		return SafeCompute.div(SafeCompute.multiply(target, earlyPercent),
				new UFDouble(100)).setScale(2, UFDouble.ROUND_UP);
	}

	/**
	 * 判断指定的日期是否有提前还本的信息
	 * 
	 * @param dates
	 * @param trade_date
	 * @return 是否需要提前还本 0:不需要，其他：提前还本比例
	 * @date 2015年10月29日
	 */
	private UFDouble isEarlyRepayment(Rateperiod[] ratevos, UFDate trade_date) {
		UFDouble falg = UFDouble.ZERO_DBL;
		if (trade_date == null || ratevos == null || ratevos.length == 0) {
			return falg;
		}
		for (Rateperiod rateperiod : ratevos) {
			UFDate end = rateperiod.getEnd_day();
			UFDouble pay = rateperiod.getPaypercent();
			// 如果日期合适，并且提前还本比例有值
			if (trade_date.isSameDate(end) && pay != null
					&& !pay.equals(UFDouble.ZERO_DBL)) {
				falg = pay;
				break;
			}
		}
		return falg;
	}

	/**
	 * 判断指定的日期是否有提前还本的信息
	 * 
	 * @param dates
	 * @param trade_date
	 * @return 是否需要提前还本 0:不需要，其他：提前还本比例
	 * @date 2017年04月19日 add by lihaibo
	 */
	private UFDouble isEarlyRepayment2(Rateperiod[] ratevos, UFDate trade_date) {
		UFDouble falg = UFDouble.ZERO_DBL;
		// 本次还本前已归还的比例 add by lihaibo
		UFDouble sum_before = UFDouble.ZERO_DBL;
		if (trade_date == null || ratevos == null || ratevos.length == 0) {
			return falg;
		}
		for (Rateperiod rateperiod : ratevos) {
			UFDate end = rateperiod.getEnd_day();
			UFDouble pay = rateperiod.getPaypercent();
			// 计算本次还本前已归还的比例的和
			if (end.compareTo(trade_date) < 0) {
				sum_before = sum_before
						.add(rateperiod.getPaypercent() == null ? UFDouble.ZERO_DBL
								: rateperiod.getPaypercent());
			}
			// 如果日期合适，并且提前还本比例有值
			if (trade_date.isSameDate(end) && pay != null
					&& !pay.equals(UFDouble.ZERO_DBL)) {
				falg = pay;
				break;
			}
		}
		// 本次还本比例/（1-本次还本前已归还的比例）
		falg = falg.div(new UFDouble(1).sub(sum_before.div(new UFDouble(100))));
		return falg;
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
			vbillcode = ser.getBillCode_RequiresNew(CostConstant.FXHB_BILLTYPE,
					pk_group, pk_org, fv);
		} catch (BusinessException be) {
			ExceptionHandler.handleException(be);
		}
		if (vbillcode != null) {
			vbillcode = suffix + vbillcode;
		}
		return vbillcode;
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
	 * @throws BusinessException
	 * @update jingqt <i>2016年4月22日14:50:52 </i>
	 */
	public UFDouble calcLX(StockBalanceVO stockvo, AggInterest agginfo,
			UFDate trade_date) throws BusinessException {
		if (agginfo == null || stockvo == null)
			return null;
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		Interest headvo = agginfo.getParent();
		if (headvo == null || ratevos == null || ratevos.length == 0)
			return null;
		UFDouble lx = null;
		if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {// 到期一次还本付息----自定义
			for (Rateperiod vo : ratevos) {
				UFDouble lx1 = SafeCompute.multiply(stockvo.getStocks_num(),
						vo.getYear_rate());
				/** 2016年5月6日 JINGQT 取实际的年天数。如果跨年，则取最大的年天数 ADD START */
				// UFDouble days = this.getRealDays(headvo, vo.getStart_day(),
				// vo.getEnd_day());
				// lx = SafeCompute.add(lx,
				// lx1.multiply(vo.getDays_num()).div(days));
				/** 2016年5月6日 JINGQT 取实际的年天数。如果跨年，则取最大的年天数 ADD END */
				/** 2017年2月21日 xuxmb 兑息期间不足一年的，应按实际天数计算应兑付金额 ADD START */
				/** 2017年7月3日 小明这里写的分支是一样的。并且这里应该是按照到期的，肯定是只会循环一次。删除一半JINGQT */
				// if (!isAllYear(vo.getStart_day(), vo.getEnd_day())) {
				// // 实际计息天数
				// UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
				// vo.getEnd_day());
				// // 年计息天数
				// UFDouble allDays = this.getYearDays(headvo,
				// vo.getStart_day(), vo.getEnd_day());
				// lx = SafeCompute.multiply(lx1, SafeCompute.div(realDays,
				// allDays));
				// } else {
				// 实际计息天数
				UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
						vo.getEnd_day());
				// 年计息天数
				UFDouble days = this.getYearDays(headvo, vo.getStart_day(),
						vo.getEnd_day());
				lx = SafeCompute.add(lx, lx1.multiply(realDays).div(days));
				// }
				/** 2017年2月21日 xuxmb 兑息期间不足一年的，应按实际天数计算应兑付金额ADD END */
			}
		} else {
			// Rateperiod vo = InterestTools.getContainsDate(ratevos,
			// trade_date);
			/** 2017年7月3日 这里将利息计算的计算方式统一了。JINGQT ADD START */
			CalcBuySellInterest calc = new CalcBuySellInterest();
			// COMMENT 这里计算利息应该按照包含当天的方式计算。
			lx = calc.calcInterestTradedate(stockvo.getStocks_num(),
					stockvo.getPk_securities(), trade_date,
					stockvo.getPk_glorgbook());
			/** 2017年7月3日 这里将利息计算的计算方式统一了。JINGQT ADD END */
		}
		return lx;
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
	// private boolean isAllYear(UFDate startDay, UFDate endDay) {
	// if (UFDate.getDaysBetween(startDay, endDay) + 1 < 365) {
	// return false;
	// }
	// return true;
	// }

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
	 * 获取年利率对应的实际天数。
	 * 
	 * @param headvo
	 *            年利率表头
	 * @param startDay
	 *            开始日期
	 * @param endDay
	 *            结束日期
	 * @return 实际天数
	 * @author 米雪 update by jingqt
	 * @since 2016-5-5 上午8:59:21
	 */
	// private UFDouble getRealDays(Interest headvo, UFDate startDay, UFDate
	// endDay) {
	// UFDouble days = UFDouble.ZERO_DBL;
	// if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {//
	// 自定义--按照程序处理，这里应该是 到期一次还本付息
	// for (int i = startDay.getYear() + 1; i < endDay.getYear(); i++) {
	// if (UFDate.isLeapYear(i)) {
	// days = new UFDouble(366);
	// return days;
	// }
	// }
	// // 开始日期是闰年并且包含2月
	// if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {
	// days = new UFDouble(366);
	// // 结束日期是闰年并且包含2月
	// } else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2)
	// {
	// days = new UFDouble(366);
	// } else {
	// days = new UFDouble(365);
	// }
	// } else {// 按期付息
	// if (startDay.getYear() == endDay.getYear()) {// 不跨年
	// if (UFDate.isLeapYear(startDay.getYear()))
	// days = new UFDouble(366);
	// else
	// days = new UFDouble(365);
	// } else {// 跨年
	// if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {//
	// 开始日期是闰年并且包含2月
	// days = new UFDouble(366);
	// } else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2)
	// {// 结束日期是闰年并且包含2月
	// days = new UFDouble(366);
	// } else {
	// days = new UFDouble(365);
	// }
	// }
	// }
	// return days;
	// }

	/**
	 * 查询影响因素单据类型VO
	 */
	public BilltypeVO getBdBilltypeVO(String code) throws BusinessException {
		QueryBillTypeInfo info = new QueryBillTypeInfo();
		return info.queryBdBilltypeVO(code);
	}

	/**
	 * 查询证券单据类型VO
	 */
	public BillTypeVO getSecBilltypeVO(String code) throws BusinessException {
		QueryBillTypeInfo info = new QueryBillTypeInfo();
		return info.querySecBilltypeVO(code);
	}

	/**
	 * 生成付息单
	 */
	public SuperVO genFxBill(String pk_group, String pk_org,
			StockBalanceVO stockvo, UFDate trade_date, UFDouble lx)
			throws BusinessException {
		SuperVO vo = constructVO();
		vo.setAttributeValue("bargain_num", stockvo.getStocks_num());
		vo.setAttributeValue("bargain_sum", lx);
		vo.setAttributeValue("billno",
				getBillNO(pk_group, pk_org, vo, this.autoSuffix));
		vo.setAttributeValue("creationtime", AppContext.getInstance()
				.getServerTime());
		vo.setAttributeValue("creator", AppContext.getInstance().getPkUser());
		vo.setAttributeValue("billmaker", AppContext.getInstance().getPkUser());
		vo.setAttributeValue("fact_sum", lx);
		vo.setAttributeValue("pk_glorgbook", stockvo.getPk_glorgbook());
		vo.setAttributeValue("pk_assetsprop", stockvo.getPk_assetsprop());
		vo.setAttributeValue("pk_stocksort", stockvo.getPk_stocksort());
		vo.setAttributeValue("pk_partnaccount", stockvo.getPk_partnaccount());
		vo.setAttributeValue("pk_capaccount", stockvo.getPk_capaccount());
		vo.setAttributeValue("pk_client", stockvo.getPk_client());
		vo.setAttributeValue("pk_operatesite", stockvo.getPk_operatesite());
		vo.setAttributeValue("pk_selfsgroup", stockvo.getPk_selfsgroup());
		vo.setAttributeValue("pk_securities", stockvo.getPk_securities());
		// 获取证券信息
		SecuritiesVO secvo = (SecuritiesVO) new BaseDAO().retrieveByPK(
				SecuritiesVO.class, stockvo.getPk_securities());
		vo.setAttributeValue("stockcode", secvo.getCode());
		vo.setAttributeValue("pk_bourse", secvo.getPk_bourse());
		vo.setAttributeValue("pk_currtype", secvo.getPk_currtype());

		BilltypeVO bd = getBdBilltypeVO(CostConstant.FXHB_BILLTYPE);
		// BillTypeVO sec = getSecBilltypeVO(CostConstant.FX_TRANSTYPE);
		BilltypeVO billtypeVO = PfDataCache
				.getBillTypeInfo(CostConstant.FX_TRANSTYPE);
		vo.setAttributeValue("pk_transtype", billtypeVO.getPk_billtypeid());
		vo.setAttributeValue("transtypecode", CostConstant.FX_TRANSTYPE);
		vo.setAttributeValue("pk_billtype", bd.getPk_billtypeid());
		vo.setAttributeValue("billtypecode", bd.getPk_billtypecode());

		vo.setAttributeValue("pk_group", stockvo.getPk_group());
		vo.setAttributeValue("pk_org", stockvo.getPk_org());
		vo.setAttributeValue("pk_org_v", stockvo.getPk_org_v());
		vo.setAttributeValue("state", SystemConst.statecode[1]);
		vo.setAttributeValue("trade_date", trade_date);
		vo.setAttributeValue("dr", 0);
		vo.setAttributeValue("makedate", AppContext.getInstance()
				.getServerTime());
		return vo;
	}

	/**
	 * 生成还本单
	 */
	public SuperVO genHbFxBill(String pk_group, String pk_org,
			StockBalanceVO stockvo, UFDate trade_date) throws BusinessException {
		/** qs 从现场代码迁过来的：对于付息方式是【贴现】的做了特殊处 20170518 add start */
		/**
		 * add by lihbj 2017-03-07 start 若为贴现 则bargain_sum（成交金额）和fact_sum（实际收付）
		 * = 数量 乘以折扣率
		 */
		// 查询当前债券对应的利率设置
		AggInterest agginfo = getQueryInfo().queryInterestsetInfo(
				stockvo.getPk_securities());
		Interest intervo = agginfo.getParent();
		UFDouble mz = UFDouble.ZERO_DBL;
		// 折扣率
		if (intervo.getDiscountrate() != null
				&& intervo.getPaytype().equals("03")) {
			mz = SafeCompute.multiply(stockvo.getStocks_num(), new UFDouble(
					intervo.getDiscountrate()));
		} else
			mz = SafeCompute.multiply(stockvo.getStocks_num(),
					new UFDouble(100));
		/** add by lihbj 2017-03-07 end */
		/** qs 从现场代码迁过来的：对于付息方式是【贴现】的做了特殊处 20170518 add end */
		SuperVO vo = constructVO();
		// UFDouble mz = SafeCompute.multiply(stockvo.getStocks_num(), new
		// UFDouble(100));

		vo.setAttributeValue("bargain_num", stockvo.getStocks_num());
		vo.setAttributeValue("bargain_sum", mz);
		vo.setAttributeValue("billno",
				getBillNO(pk_group, pk_org, vo, this.autoSuffix));
		vo.setAttributeValue("creationtime", AppContext.getInstance()
				.getServerTime());
		vo.setAttributeValue("creator", AppContext.getInstance().getPkUser());
		vo.setAttributeValue("billmaker", AppContext.getInstance().getPkUser());
		vo.setAttributeValue("fact_sum", mz);
		//
		BilltypeVO bd = getBdBilltypeVO(CostConstant.FXHB_BILLTYPE);
		// BillTypeVO sec = getSecBilltypeVO(CostConstant.SELL_TRANSTYPE);
		BilltypeVO billtypeVO = PfDataCache
				.getBillTypeInfo(CostConstant.SELL_TRANSTYPE);
		vo.setAttributeValue("pk_transtype", billtypeVO.getPk_billtypeid());
		vo.setAttributeValue("transtypecode", CostConstant.SELL_TRANSTYPE);

		vo.setAttributeValue("pk_billtype", bd.getPk_billtypeid());
		vo.setAttributeValue("billtypecode", bd.getPk_billtypecode());
		// vo.setAttributeValue("pk_transtype", sec.getPk_billtype());
		// vo.setAttributeValue("transtypecode", sec.getPk_billtypecode());
		// vo.setPk_busitype(pk_busitype);//业务类型为空
		//
		vo.setAttributeValue("pk_glorgbook", stockvo.getPk_glorgbook());
		vo.setAttributeValue("pk_assetsprop", stockvo.getPk_assetsprop());
		vo.setAttributeValue("pk_stocksort", stockvo.getPk_stocksort());
		vo.setAttributeValue("pk_partnaccount", stockvo.getPk_partnaccount());
		vo.setAttributeValue("pk_capaccount", stockvo.getPk_capaccount());
		vo.setAttributeValue("pk_client", stockvo.getPk_client());
		vo.setAttributeValue("pk_operatesite", stockvo.getPk_operatesite());
		vo.setAttributeValue("pk_selfsgroup", stockvo.getPk_selfsgroup());
		vo.setAttributeValue("pk_securities", stockvo.getPk_securities());

		SecuritiesVO secvo = (SecuritiesVO) new BaseDAO().retrieveByPK(
				SecuritiesVO.class, stockvo.getPk_securities());
		vo.setAttributeValue("stockcode", secvo.getCode());
		vo.setAttributeValue("pk_bourse", secvo.getPk_bourse());
		vo.setAttributeValue("pk_currtype", secvo.getPk_currtype());

		vo.setAttributeValue("pk_group", stockvo.getPk_group());
		vo.setAttributeValue("pk_org", stockvo.getPk_org());
		vo.setAttributeValue("pk_org_v", stockvo.getPk_org_v());
		vo.setAttributeValue("state", SystemConst.statecode[1]);
		vo.setAttributeValue("trade_date", trade_date);
		vo.setAttributeValue("dr", 0);
		vo.setAttributeValue("makedate", AppContext.getInstance()
				.getServerTime());
		return vo;
	}

	public SuperVO constructVO() throws BusinessException {
		Class<?> cls = null;
		SuperVO vo = null;
		try {
			cls = Class.forName(CostConstant.STOCKTRADEVONAME);
			vo = (SuperVO) cls.newInstance();
		} catch (Exception e) {
			throw new BusinessException("StocktradeVO 类实例化错误!");
		}
		return vo;
	}

	public QueryInterestBaseInfo getQueryInfo() {
		if (queryInfo == null) {
			queryInfo = new QueryInterestBaseInfo();
		}
		return queryInfo;
	}
}
