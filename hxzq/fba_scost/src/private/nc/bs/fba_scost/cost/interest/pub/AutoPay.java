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
 * �Զ�������Ϣ
 * 
 */
public class AutoPay {
	BaseDAO dao = new BaseDAO();

	private QueryInterestBaseInfo queryInfo = null;

	private String fxd = "fx";
	private String hbd = "hb";
	/** ��ǰ���� */
	private String early = "early";
	/** ��ǰ���� */
	private String earlyPrefix = "EARLY";
	/** �Զ����� */
	private String autoSuffix = "ZDPAY";

	public void autoPay(CostingTool costingtool, TradeDataTool tradedatatool)
			throws BusinessException {
		CostParaVO costpara = costingtool.getCostParaVO();
		Integer checkstate = costpara.getCheckstate();
		switch (checkstate) {
		case 0:// ���
			autoAudit(costingtool, tradedatatool);
			AutoExpire iAutoExpire = new AutoExpire();
			iAutoExpire.autoExpire(costingtool, tradedatatool);
			break;
		case 1:// ����
			break;
		case 2:// ����
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
			// ��Ϊ���ɵĵ����Ѿ���ˣ��������ٴ����
			new BaseDAO().insertVOList(early);
			addCache(costingtool, tradedatatool, pks1, pks2);
		}
	}

	/**
	 * ���뻺��
	 */
	@SuppressWarnings("unchecked")
	public void addCache(CostingTool costingtool, TradeDataTool tradedatatool,
			String[] pks1, String[] pks2) throws BusinessException {
		try {
			if (tradedatatool.isIsinit()) {
				Class<IBill> cs = (Class<IBill>) Class
						.forName("nc.vo.fba_sim.simtrade.stocktrade.AggStocktradeVO");
				// ��ѯ��������1
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
					// ��ѯ��������2
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
	 * ��ѯ��ǰծȯ�Ƿ񻹱�����
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
		// ��ȡ��֤ȯ������������Ϣ
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		// �Ƿ���Ҫ��ǰ������ȡ����ǰ��������
		UFDouble earlyPercent = this.isEarlyRepayment(ratevos, trade_date);
		/** =============qs ���ֳ�����Ǩ������ 20170518 add start============== */
		// ȡ��ǰ��������/��1-���λ���ǰ�ѹ黹�ı�����
		UFDouble earlyPercent2 = this.isEarlyRepayment2(ratevos, trade_date);
		/** =============qs ���ֳ�����Ǩ������ 20170518 add end============== */
		// ��ʱ�Ƿ���Ҫ���ɻ����������һ�ڣ�
		boolean hbfx = InterestTools.isHbFx(ratevos, trade_date);
		// ����Ҫ��ǰ����
		if (earlyPercent.equals(UFDouble.ZERO_DBL) || hbfx) {
			if (map.containsKey(this.early)) {
				map.get(this.early).addAll(lst);
			} else {
				map.put(this.early, lst);
			}
			return;
		}
		// ��Ҫ��ǰ����
		// 1��������������Ϊ0�������������еĹ��ʼ�ֵ�ͳɱ���Ҫ��������
		/**
		 * =============qs ���ֳ�����Ǩ�����ģ�����һ������earlyPercent2�� 20170518 add
		 * start==============
		 */
		SuperVO bill = this.getEarlyBill(costingtool, pk_group, pk_org,
				stockvo, trade_date, earlyPercent, earlyPercent2);
		/**
		 * =============qs ���ֳ�����Ǩ�����ģ�����һ������earlyPercent2�� 20170518 add
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
	 * ������ǰ������
	 * 
	 * @param costingtool
	 *            ��˹�����
	 * @param pk_group
	 *            ��֯
	 * @param pk_org
	 *            ����
	 * @param stockvo
	 *            ���
	 * @param trade_date
	 *            ��������
	 * @param earlyPercent
	 *            ��ǰ��������
	 * @return ��˵ĵ��ݣ��˵�������EARLY��ͷ�ģ�
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2015-10-30 ����11:05:47
	 */
	private SuperVO getEarlyBill(CostingTool costingtool, String pk_group,
			String pk_org, StockBalanceVO stockvo, UFDate trade_date,
			UFDouble earlyPercent, UFDouble earlyPercent2)
			throws BusinessException {
		SuperVO vo = constructVO();
		// ��������Ϊ0
		vo.setAttributeValue("bargain_num", UFDouble.ZERO_DBL);
		// ���׽��Ϊ0
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
		// д���ʲ�����
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

		/** ��д�������� 2015��10��30�� */
		// // �Ƶ����ھ��ǻ�������
		// vo.setAttributeValue("trade_date", trade_date);
		// vo.setAttributeValue("dr", 0);
		// // �Ƶ����ھ��ǻ�������
		// vo.setAttributeValue("makedate", trade_date);
		// // COMMENT ���۳ɱ��ȱ�����ת
		// UFDouble sum = this.calcTi(stockvo.getStocks_sum(), earlyPercent);
		// vo.setAttributeValue("sellcost", sum);
		/** qs ���ֳ�����ϲ������ģ����۳ɱ��ȱ�����תʱ���Ĵ�earlyPercent2 20170518 update start */
		// �������ھ��ǻ�������
		vo.setAttributeValue("trade_date", trade_date);
		vo.setAttributeValue("dr", 0);
		// �Ƶ����ھ��ǵ�¼����
		vo.setAttributeValue("makedate", AppContext.getInstance()
				.getServerTime());
		// COMMENT ���۳ɱ��ȱ�����ת
		UFDouble sum = this.calcTi(stockvo.getStocks_sum(), earlyPercent2);
		vo.setAttributeValue("sellcost", sum);
		/** qs ���ֳ�����ϲ������ģ����۳ɱ��ȱ�����תʱ���Ĵ�earlyPercent2 20170518 update end */
		// ��д���۳ɱ�
		ICostBalanceTool tool = costingtool.getBalancetool();
		// /////////////////////////////////////////////
		String[] costFieldArray = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		String key = VOUtil.getCombinesKey(vo, costFieldArray);
		// �˴�ƴ��key�������ҵ����
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

		// COMMENT ���ʼ�ֵ�ȱ�����ת
		FairValueForward fvf = new FairValueForward();
		ForwardVO fvo = fvf.forwardVOinfo(costingtool, stockvo,
				(ITrade_Data) vo);
		// ��ת��ֵ
		UFDouble ti = this.calcTi(fvo.getFairvalue(), earlyPercent);
		// ��д���ʼ�ֵ
		fvo.setFairvalue(fvo.getFairvalue().sub(ti));
		vo.setAttributeValue("fairvalue", ti);
		vo.setAttributeValue("fairvaluedate", AppContext.getInstance()
				.getServerTime());
		// ���湫�ʼ�ֵ---
		ICostForward costcalc = new CostForwardImpl();
		costcalc.saveFairValueDistill(costingtool, (ITrade_Data) vo);
		// COMMENT ��ѩ˵ʵ���ո���Ҫ��Ʊ���ֵx��������
		UFDouble fact_sum = this.calcTi(
				stockvo.getStocks_num().multiply(new UFDouble(100.0D)),
				earlyPercent);
		vo.setAttributeValue("fact_sum", fact_sum);
		/** ծȯ����ǰ���������ģ��������ɵĵ������������������ĵ����ϵĳɽ�������ʵ���ո� xuxmb��� 20170217 start */
		// �ɽ����Ϊʵ���ո�
		vo.setAttributeValue("bargain_sum", fact_sum);
		/** ծȯ����ǰ���������ģ��������ɵĵ������������������ĵ����ϵĳɽ�������ʵ���ո� xuxmb��� 20170217 end */
		try {
			// �������������Ѿ������
			vo.setAttributeValue("state", SystemConst.statecode[3]);
			// ��˵��ֶ�
			vo.setAttributeValue("approver", AppContext.getInstance()
					.getPkUser());
			vo.setAttributeValue("approvedate", AppContext.getInstance()
					.getServerTime());
			/** ����ǿɹ�����е��ڣ�����Ҫ�ж�ʵ������pk_assetsprop */
			/** �ж����ʲ��໹�Ǹ�ծ�࣬����Ǹ�ծ�࣬����ʱ�Ȳ�������----by yangxue��2015��10��30�� */
			if (this.isAsset(assetsprop)) {
				// COMMENT ʵ������
				String result = SysInitCache.getInstance().getSysInitValue(
						stockvo.getPk_glorgbook(),
						SystemConst.SYSINIT_ISREALRATE);
				if ("Y".equals(result)) {
					ICalcPluginMaintain mail = NCLocator.getInstance().lookup(
							ICalcPluginMaintain.class);
					mail.sellPlugindeal(stockvo, vo);
				}
			}
			// �����ʽ�䶯
			costingtool.updateFunds(vo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException(e);
		}
		return vo;
	}

	/**
	 * ͨ�� �������� �ж����ʲ� ���Ǹ�ծ
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
	 * ����Ҫ��ǰ�����Ľ�ת����
	 * 
	 * @param target
	 *            Ҫ��ת������
	 * @param earlyPercent
	 *            ��ǰ��������
	 * @return ����
	 */
	private UFDouble calcTi(UFDouble target, UFDouble earlyPercent) {
		return SafeCompute.div(SafeCompute.multiply(target, earlyPercent),
				new UFDouble(100)).setScale(2, UFDouble.ROUND_UP);
	}

	/**
	 * �ж�ָ���������Ƿ�����ǰ��������Ϣ
	 * 
	 * @param dates
	 * @param trade_date
	 * @return �Ƿ���Ҫ��ǰ���� 0:����Ҫ����������ǰ��������
	 * @date 2015��10��29��
	 */
	private UFDouble isEarlyRepayment(Rateperiod[] ratevos, UFDate trade_date) {
		UFDouble falg = UFDouble.ZERO_DBL;
		if (trade_date == null || ratevos == null || ratevos.length == 0) {
			return falg;
		}
		for (Rateperiod rateperiod : ratevos) {
			UFDate end = rateperiod.getEnd_day();
			UFDouble pay = rateperiod.getPaypercent();
			// ������ں��ʣ�������ǰ����������ֵ
			if (trade_date.isSameDate(end) && pay != null
					&& !pay.equals(UFDouble.ZERO_DBL)) {
				falg = pay;
				break;
			}
		}
		return falg;
	}

	/**
	 * �ж�ָ���������Ƿ�����ǰ��������Ϣ
	 * 
	 * @param dates
	 * @param trade_date
	 * @return �Ƿ���Ҫ��ǰ���� 0:����Ҫ����������ǰ��������
	 * @date 2017��04��19�� add by lihaibo
	 */
	private UFDouble isEarlyRepayment2(Rateperiod[] ratevos, UFDate trade_date) {
		UFDouble falg = UFDouble.ZERO_DBL;
		// ���λ���ǰ�ѹ黹�ı��� add by lihaibo
		UFDouble sum_before = UFDouble.ZERO_DBL;
		if (trade_date == null || ratevos == null || ratevos.length == 0) {
			return falg;
		}
		for (Rateperiod rateperiod : ratevos) {
			UFDate end = rateperiod.getEnd_day();
			UFDouble pay = rateperiod.getPaypercent();
			// ���㱾�λ���ǰ�ѹ黹�ı����ĺ�
			if (end.compareTo(trade_date) < 0) {
				sum_before = sum_before
						.add(rateperiod.getPaypercent() == null ? UFDouble.ZERO_DBL
								: rateperiod.getPaypercent());
			}
			// ������ں��ʣ�������ǰ����������ֵ
			if (trade_date.isSameDate(end) && pay != null
					&& !pay.equals(UFDouble.ZERO_DBL)) {
				falg = pay;
				break;
			}
		}
		// ���λ�������/��1-���λ���ǰ�ѹ黹�ı�����
		falg = falg.div(new UFDouble(1).sub(sum_before.div(new UFDouble(100))));
		return falg;
	}

	/**
	 * ��ȡ���ݺ� fv���ÿգ�����ֱ�ӷ���Ϊ��
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
	 * ������Ϣ<br>
	 * <b>������µ�ԭ����������ǰ������������Ϣ�ļ��㲢û�а�����ǰ�����ı���ȥ���¡�</b>
	 * 
	 * @param stockvo
	 *            �����Ϣ
	 * @param agginfo
	 *            ����������Ϣ
	 * @param trade_date
	 *            ��������
	 * @author δ֪
	 * @throws BusinessException
	 * @update jingqt <i>2016��4��22��14:50:52 </i>
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
		if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {// ����һ�λ�����Ϣ----�Զ���
			for (Rateperiod vo : ratevos) {
				UFDouble lx1 = SafeCompute.multiply(stockvo.getStocks_num(),
						vo.getYear_rate());
				/** 2016��5��6�� JINGQT ȡʵ�ʵ���������������꣬��ȡ���������� ADD START */
				// UFDouble days = this.getRealDays(headvo, vo.getStart_day(),
				// vo.getEnd_day());
				// lx = SafeCompute.add(lx,
				// lx1.multiply(vo.getDays_num()).div(days));
				/** 2016��5��6�� JINGQT ȡʵ�ʵ���������������꣬��ȡ���������� ADD END */
				/** 2017��2��21�� xuxmb ��Ϣ�ڼ䲻��һ��ģ�Ӧ��ʵ����������Ӧ�Ҹ���� ADD START */
				/** 2017��7��3�� С������д�ķ�֧��һ���ġ���������Ӧ���ǰ��յ��ڵģ��϶���ֻ��ѭ��һ�Ρ�ɾ��һ��JINGQT */
				// if (!isAllYear(vo.getStart_day(), vo.getEnd_day())) {
				// // ʵ�ʼ�Ϣ����
				// UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
				// vo.getEnd_day());
				// // ���Ϣ����
				// UFDouble allDays = this.getYearDays(headvo,
				// vo.getStart_day(), vo.getEnd_day());
				// lx = SafeCompute.multiply(lx1, SafeCompute.div(realDays,
				// allDays));
				// } else {
				// ʵ�ʼ�Ϣ����
				UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
						vo.getEnd_day());
				// ���Ϣ����
				UFDouble days = this.getYearDays(headvo, vo.getStart_day(),
						vo.getEnd_day());
				lx = SafeCompute.add(lx, lx1.multiply(realDays).div(days));
				// }
				/** 2017��2��21�� xuxmb ��Ϣ�ڼ䲻��һ��ģ�Ӧ��ʵ����������Ӧ�Ҹ����ADD END */
			}
		} else {
			// Rateperiod vo = InterestTools.getContainsDate(ratevos,
			// trade_date);
			/** 2017��7��3�� ���ｫ��Ϣ����ļ��㷽ʽͳһ�ˡ�JINGQT ADD START */
			CalcBuySellInterest calc = new CalcBuySellInterest();
			// COMMENT ���������ϢӦ�ð��հ�������ķ�ʽ���㡣
			lx = calc.calcInterestTradedate(stockvo.getStocks_num(),
					stockvo.getPk_securities(), trade_date,
					stockvo.getPk_glorgbook());
			/** 2017��7��3�� ���ｫ��Ϣ����ļ��㷽ʽͳһ�ˡ�JINGQT ADD END */
		}
		return lx;
	}

	/**
	 * �жϿ�ʼ���������������Ƿ���һ��
	 * 
	 * @param startDay
	 *            ��ʼ����
	 * @param endDay
	 *            ��������
	 * @return true:��һ�� false:����һ��
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
	 * ���ͻ�Ҫ������ӻ�ȡ���������ķ�����
	 * 
	 * @param headvo
	 *            �����ʱ�ͷ
	 * @param startDay
	 *            ��ʼ����
	 * @param endDay
	 *            ��������
	 * @return ʵ������
	 * @author xuxmb
	 * @since 2017-2-24 ����8:59:21
	 */
	private UFDouble getYearDays(Interest headvo, UFDate start_date,
			UFDate end_date) {
		String paytype = headvo.getPaytype();
		int days_sum = 365;
		if ("02".equals(paytype)) {// ����һ�λ�����Ϣ
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
	 * ��ȡ�����ʶ�Ӧ��ʵ������,��ʼ�������������ڵ�ʵ������
	 * 
	 * @param startDay
	 *            ��ʼ����
	 * @param endDay
	 *            ��������
	 * @return ʵ������
	 * @author xuxmb
	 * @since 20170221
	 */
	private UFDouble getRealDaysByVO(UFDate startDay, UFDate endDay) {
		int days_sum = UFDate.getDaysBetween(startDay, endDay) + 1;
		return new UFDouble(days_sum);
	}

	/**
	 * ��ȡ�����ʶ�Ӧ��ʵ��������
	 * 
	 * @param headvo
	 *            �����ʱ�ͷ
	 * @param startDay
	 *            ��ʼ����
	 * @param endDay
	 *            ��������
	 * @return ʵ������
	 * @author ��ѩ update by jingqt
	 * @since 2016-5-5 ����8:59:21
	 */
	// private UFDouble getRealDays(Interest headvo, UFDate startDay, UFDate
	// endDay) {
	// UFDouble days = UFDouble.ZERO_DBL;
	// if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {//
	// �Զ���--���ճ���������Ӧ���� ����һ�λ�����Ϣ
	// for (int i = startDay.getYear() + 1; i < endDay.getYear(); i++) {
	// if (UFDate.isLeapYear(i)) {
	// days = new UFDouble(366);
	// return days;
	// }
	// }
	// // ��ʼ���������겢�Ұ���2��
	// if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {
	// days = new UFDouble(366);
	// // �������������겢�Ұ���2��
	// } else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2)
	// {
	// days = new UFDouble(366);
	// } else {
	// days = new UFDouble(365);
	// }
	// } else {// ���ڸ�Ϣ
	// if (startDay.getYear() == endDay.getYear()) {// ������
	// if (UFDate.isLeapYear(startDay.getYear()))
	// days = new UFDouble(366);
	// else
	// days = new UFDouble(365);
	// } else {// ����
	// if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {//
	// ��ʼ���������겢�Ұ���2��
	// days = new UFDouble(366);
	// } else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2)
	// {// �������������겢�Ұ���2��
	// days = new UFDouble(366);
	// } else {
	// days = new UFDouble(365);
	// }
	// }
	// }
	// return days;
	// }

	/**
	 * ��ѯӰ�����ص�������VO
	 */
	public BilltypeVO getBdBilltypeVO(String code) throws BusinessException {
		QueryBillTypeInfo info = new QueryBillTypeInfo();
		return info.queryBdBilltypeVO(code);
	}

	/**
	 * ��ѯ֤ȯ��������VO
	 */
	public BillTypeVO getSecBilltypeVO(String code) throws BusinessException {
		QueryBillTypeInfo info = new QueryBillTypeInfo();
		return info.querySecBilltypeVO(code);
	}

	/**
	 * ���ɸ�Ϣ��
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
		// ��ȡ֤ȯ��Ϣ
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
	 * ���ɻ�����
	 */
	public SuperVO genHbFxBill(String pk_group, String pk_org,
			StockBalanceVO stockvo, UFDate trade_date) throws BusinessException {
		/** qs ���ֳ�����Ǩ�����ģ����ڸ�Ϣ��ʽ�ǡ����֡����������⴦ 20170518 add start */
		/**
		 * add by lihbj 2017-03-07 start ��Ϊ���� ��bargain_sum���ɽ�����fact_sum��ʵ���ո���
		 * = ���� �����ۿ���
		 */
		// ��ѯ��ǰծȯ��Ӧ����������
		AggInterest agginfo = getQueryInfo().queryInterestsetInfo(
				stockvo.getPk_securities());
		Interest intervo = agginfo.getParent();
		UFDouble mz = UFDouble.ZERO_DBL;
		// �ۿ���
		if (intervo.getDiscountrate() != null
				&& intervo.getPaytype().equals("03")) {
			mz = SafeCompute.multiply(stockvo.getStocks_num(), new UFDouble(
					intervo.getDiscountrate()));
		} else
			mz = SafeCompute.multiply(stockvo.getStocks_num(),
					new UFDouble(100));
		/** add by lihbj 2017-03-07 end */
		/** qs ���ֳ�����Ǩ�����ģ����ڸ�Ϣ��ʽ�ǡ����֡����������⴦ 20170518 add end */
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
		// vo.setPk_busitype(pk_busitype);//ҵ������Ϊ��
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
			throw new BusinessException("StocktradeVO ��ʵ��������!");
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
