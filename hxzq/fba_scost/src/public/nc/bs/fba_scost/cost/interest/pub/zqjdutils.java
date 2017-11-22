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
	// /** ��ǰ���� */
	// private String early = "early";
	// /** ��ǰ���� */
	// private String earlyPrefix = "EARLY";
	// /** �Զ����� */
	// private String autoSuffix = "ZDPAY";
	//
	protected ICostForward costcalc;

	public void zqjdautoAudit(BilltypeGroupVO vo, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {

		CostParaVO costpara = costingtool.getCostParaVO();
		Integer checkstate = costpara.getCheckstate();
		switch (checkstate) {
		case 0:// ���
			this.auto(vo, costingtool, tradedatatool, checkclasstool);
			break;
		}
	}

	private void auto(BilltypeGroupVO vo, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		List<PendingBillVO> pendlist = costingtool.getBilltypeclass();
		// �ڴ��������ծȯ���׼�¼�������� lqm

		CostParaVO costpara = costingtool.getCostParaVO();
		CostPlanVO costplanvo = costpara.getCostplanvo();
		UFDate trade_date = new UFDate(costingtool.getCurrdate());
		//
		CheckParaVO checkparavo = costpara.getCheckParavo();
		String pk_glorgbook = checkparavo.getPk_glorgbook();
		String pk_group = checkparavo.getPk_group();
		String pk_org = checkparavo.getPk_org();
		// ��У���Ƿ����ɹ黹��
		// this.autoreturn(vo, dataMap ,costingtool, tradedatatool,
		// checkclasstool);
		// ���������Ƿ����ɶҸ���
		boolean falg = this.getBooleanFromInitcode(pk_glorgbook, "SEC40");
		if (!falg)
			return;
		// ������ص��� ת�Ҹ�������ת����
		Map<String, List<SuperVO>> map = calcAutoPayInfo(costingtool,
				costplanvo, pk_group, pk_org, trade_date, vo, tradedatatool);
		if (map != null) {
			List<SuperVO> fx = map.get(zdfd);
			String[] pks1 = new BaseDAO().insertVOList(fx);
			// List<SuperVO> hb = map.get(hbd);
			// String[] pks2 = new BaseDAO().insertVOList(hb);
			// List<SuperVO> early = map.get(this.early);
			// // ��Ϊ���ɵĵ����Ѿ���ˣ��������ٴ����
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
		case 0:// ���
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
		// ���黹��������λ����ֲ�������� LQM
		List<String> plugins = new ArrayList<String>();
		boolean enable = InitGroupQuery.isEnabled(checkParaVO.getPk_group(),
				"HV17"); // ծȯ���:��ֹģ��δ���ã����䱨��
		if (enable) {
			plugins.add("nc.bs.fba_scost.cost.interest.pub.GZZqjdAutoReturn");
		}
		// 2015��5��28�� zhaoxmc end
		for (int j = 0; j < plugins.size(); j++) {
			GZISpecialBusiness ispbusiness;
			String sbimpl = plugins.get(j);
			try {
				String[] temp = sbimpl.split("\\.");
				ispbusiness = (GZISpecialBusiness) NewObjectService
						.newInstance(temp[2], sbimpl);
			} catch (Exception e) {
				throw new BusinessException(sbimpl + "ʵ����ʧ�ܣ�" + e.getMessage());
			}
			ISuperVO[] vos = ispbusiness.query(vo, costingtool, tradedatatool,
					checkclasstool);
			if (null != vos && vos.length > 0) {
				// �������ݲ�����ȡ���黹���� pks
				String[] returnpks = ispbusiness.insert(vo, costingtool, vos);
				// ��ȡ����ת������pks
				List<String> zhdlistpks = ispbusiness.queryzhd();
				// �ȴ���ת������pks����ˣ��ٴ���黹����pks�����
				if (zhdlistpks != null && zhdlistpks.size() > 0) {
					this.autozhd(zhdlistpks, vo, dataMap, costingtool,
							tradedatatool, checkclasstool);
				}
				// �ٴ���黹��
				if (returnpks != null && returnpks.length > 0) {
					autoghd(returnpks, vo, dataMap, costingtool, tradedatatool,
							checkclasstool);
				}
			}

		}
	}

	// ���ת����
	private void autozhd(List<String> pks, BilltypeGroupVO vo,
			Map<String, List<PendingBillVO>> dataMap, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		// ����pk���²�ѯת����
		AggTransformtradeVO[] aggs = getBusiInstance().queryBillByPK(
				pks.toArray(new String[0]));
		// ���ת����
		this.costcalc = ((ICostForward) NCLocator.getInstance().lookup(
				ICostForward.class.getName()));
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		if (aggs != null && aggs.length > 0) {
			// ���֮ǰ���Ƚ�ϵͳ�ı����������� checkclasstool costingtool
			// ��ϵͳ��������
			SetaddreturnUtils setUtils = new SetaddreturnUtils();
			setUtils.AddBillTypeClass(dataMap, "HV3F-0xx-01", vo, costingtool,
					tradedatatool, checkclasstool);
			List billtypeclass = costingtool.getBilltypeclass();
			Map<String, IBillCheckPlugin> checkclassmap = checkclasstool
					.getCheckClassMap();
			if (billtypeclass != null) {
				for (int i = 0; i < billtypeclass.size(); i++) {
					PendingBillVO pbvo = (PendingBillVO) billtypeclass.get(i);
					// ֻ���������ת���� �����������Ѿ������
					if (pbvo.getPk_billtype().equals("HV3F-0xx-01")) {
						costingtool.setCurrbilltype(pbvo.getPk_billtype());// ���õ�ǰ��������
						costingtool.setPk_stocksort(new String[] {
								pbvo.getPk_stocksort1(),
								pbvo.getPk_stocksort2() });
						costingtool.setDirection(pbvo.getDirection());
						costingtool.setIsinit(false);// �˴� �������û����־ �ǵ�һ�γ�ʼ�� ���
														// ����Ҫ
														// ���������Լ����� YangJie
														// 2014-04-24
						IBillCheckPlugin ibillcheck = checkclassmap
								.get(pbvo.getPk_billtype() + "##"
										+ pbvo.getCheckclass());
						try {
							for (IBill ibill : aggs) {
								TransformtradeVO fathervo = (TransformtradeVO) ibill
										.getParent();// �޺���
								if (fathervo.getHc_pk_stocksort() == null) {
									fathervo.setHc_pk_stocksort(costingtool
											.getPk_stocksort()[0]);// Ӱ����1
								}
								if (fathervo.getHr_pk_stocksort() == null) {
									fathervo.setHr_pk_stocksort(costingtool
											.getPk_stocksort()[1]);// Ӱ����2
								}
								// vo.setPk_stocksort(vo.getHr_pk_stocksort());//
								// ת����2
								// vo.setPk_assetsprop(vo.getHr_pk_assetsprop());//
								// ת����2
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
							throw new Exception("������ڣ�"
									+ costingtool.getCurrdate() + "������ڲ�����:"
									+ ibillcheck.toString() + "������־��"
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

	// ��˹黹��
	private void autoghd(String[] pks, BilltypeGroupVO vo,
			Map<String, List<PendingBillVO>> dataMap, CostingTool costingtool,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws Exception {
		// ����pk���²�ѯת����
		AggZqjdVO[] aggs = getBusiInstance1().queryBillByPK(pks);
		// AggTransformtradeVO[] aggs =
		// getBusiInstance().queryBillByPK(zhdlistpks.toArray(new String[0]));
		// ���ת����
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		if (aggs != null && aggs.length > 0) {
			// ���֮ǰ���Ƚ�ϵͳ�ı����������� checkclasstool costingtool
			// ��ϵͳ��������
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
					// ֻ��˹黹�������������Ѿ������
					if (pbvo.getPk_billtype().equals("HV7A-0xx-02")
							&& (pbvo.getTrade_date().toString()
									.substring(0, 10)).equals(costingtool
									.getCurrdate())) {
						costingtool.setCurrbilltype(pbvo.getPk_billtype());// ���õ�ǰ��������
						costingtool.setPk_stocksort(new String[] {
								pbvo.getPk_stocksort1(),
								pbvo.getPk_stocksort2() });
						costingtool.setDirection(pbvo.getDirection());
						costingtool.setIsinit(false);// �˴� �������û����־ �ǵ�һ�γ�ʼ�� ���
														// ����Ҫ
														// ���������Լ����� YangJie
														// 2014-04-24
						IBillCheckPlugin ibillcheck = checkclassmap
								.get(pbvo.getPk_billtype() + "##"
										+ pbvo.getCheckclass());
						try {
							for (IBill ibill : aggs) {

								AggZqjdVO aggvo = (AggZqjdVO) ibill;
								ZqjdVO vo1 = aggvo.getParentVO();
								// ��������ɵĹ黹������Ҫ���Ƿ����㹻�����㹻ʱ����ת��������������Ȼ����¶�ȡ
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
							throw new Exception("������ڣ�"
									+ costingtool.getCurrdate() + "������ڲ�����:"
									+ ibillcheck.toString() + "������־��"
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
		// У�����Ƿ��㹻
		zqjdStockEnoughValidation stockEnoughValidation = new zqjdStockEnoughValidation();
		stockEnoughValidation.setStockBalanceVO(stockBalanceVO);
		valList.add(stockEnoughValidation);
		// У���Ƿ񳬹黹
		zqjdOverReturnValidation overReturnValidation = new zqjdOverReturnValidation();
		valList.add(overReturnValidation);

		UtilsZqjdCompositeValidation validations = new UtilsZqjdCompositeValidation();
		validations.setValidators(valList);

		validations.validate(tradevo);

	}

	private void calculateWhenCheck1(ICostingTool costingtool, ZqjdVO tradevo)
			throws Exception {
		// ����Ĭ��ֵ
		// tradevo = DefaultValTool.setDefaultVal(tradevo);

		// ��ѯ�����¶���ҪqryVO
		BanlanceQueryKeyVO queryKeyVO = getStockBalanceTool()
				.createQryBalanceVO(costingtool, tradevo);
		// ��ѯ���
		StockBalanceVO stockBalanceVO = getStockBalanceTool().qryStockBalnce(
				queryKeyVO, costingtool, tradevo);

		checkQuantityAndMoney(stockBalanceVO, tradevo);

		// ������ ����
		UFDouble befStockNum = stockBalanceVO.getStocks_num();
		UFDouble befStockSum = stockBalanceVO.getStocks_sum();
		stockBalanceVO.setStocks_num(SafeCompute.sub(befStockNum,
				tradevo.getBargain_num()));
		stockBalanceVO.setStocks_sum(SafeCompute.sub(befStockSum,
				tradevo.getBargain_sum()));
		// ���¿��
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

	// ���ɵ�ת���������Ǵ���
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
		 * �˴�ƴ��key�������ҵ����
		 */
		hckey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hckey;
		hckey = hckey + tradevo.getPk_securities();
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(hckey);
		queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
		queryvo.setTrade_date(trade_date);
		// �������
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}

		// zpm ʵ������ ���ӿ�ʼ��λ�ò�Ҫ��
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}

		// �����תӦ����Ϣ

		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// �����ת���ʼ�ֵ
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
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
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

			tradevo.setBargain_num2(tradevo.getBargain_num());// ���ת������Ϊ�գ���ôת����������ת������
		}
		// ���ʣ������
		stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		// �ж�ת���Ƿ����
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// ����
			// ���ʣ������Ϊ0 ����ȫ������ ת�����Ϊ�����
			stocks_sum = outstockbalancevo.getStocks_sum();
		} else {// ���۳ɱ�
			// ���ת������ ����ת�����
			if (isFirstPrice) {// �ȼ��㵥��
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
		/******* �ʲ�ת��ծʱ�����ٸ�ծ������� ***************/
		// ת��Ŀ϶���ծȯ���ж�ծȯ�Ƿ��и�ծ�����û�У���ȡ��ʼ��ծ��Ϣ
		if (instockbalancevo.getVdef1() == null) {
			scoxtjisuanjdkcUtils utis = new scoxtjisuanjdkcUtils();
			// ���飬��һ��Ϊ�������ڶ���Ϊ���
			UFDouble[] jsfz = utis.jisuanfzqc(instockbalancevo);
			lastnum = jsfz[0];
			lastsum = jsfz[1];
		} else {
			lastnum = new UFDouble(instockbalancevo.getVdef1());
			lastsum = new UFDouble(instockbalancevo.getVdef2());

		}

		/*******************************************/

		// ���ת�������
		stocks_num = pm.add(instockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// ���ת��Ľ��
		stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());
		// ����ת����Ϣ
		instockbalancevo.setStocks_num(stocks_num);
		instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));
		instockbalancevo.setStocks_tax(pm.add(instockbalancevo.getStocks_tax(),
				taxOutCost));
		// ����ת��Ŀ��
		balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
		// ������Ϣ���
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// if(tradevo.getPk_busitype()!=null){
		// �鿴�Ƿ�Ϊ�ʲ�ת��ծ,������ʲ�ת����,��ת��ĸ�ծҪ���ո�ծ������תӦ�ռ�����Ϣ
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
		// ����̨��
		this.saveSellTally(tradevo);
		// ��ȡӦ����Ϣ �ʲ�ת��ծ0401 �ʲ���Ҫ��תӦ����Ϣ��ת��1�����٣�����ծҲ��Ҫ��תӦ����Ϣ��ת��1�����٣�lqm---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// ���湫�ʼ�ֵ---
		costcalc.saveFairValueDistill(costingtool, tradevo);
		// ʵ������
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
		// ��ѯծȯ��������Ϣ
		String sql_bow_now = zqjdStockChangeTool.getNowSql("HV7A-0xx-01",
				trade_date.toString());
		ArrayList<ZqjdVO> nowBowResult = (ArrayList<ZqjdVO>) baseDAO
				.executeQuery(sql_bow_now, new BeanListProcessor(ZqjdVO.class));

		if (nowBowResult == null || nowBowResult.size() == 0)
			return null;
		Map<String, List<SuperVO>> map = new HashMap<String, List<SuperVO>>();
		// ��ѭ�����Ϣ
		for (ZqjdVO v : nowBowResult) {
			buildHbFxBill(pk_group, pk_org, v, trade_date, map, vo,
					tradedatatool, costingtool);
		}

		return map;
	}

	/**
	 * ��ѯ��ǰծȯ�Ƿ񻹱�����
	 */
	private void buildHbFxBill(String pk_group, String pk_org, ZqjdVO zqjdvo,
			UFDate trade_date, Map<String, List<SuperVO>> map,
			BilltypeGroupVO vo, TradeDataTool tradedatatool,
			CostingTool costingtool) throws BusinessException {
		// ��ǰʱ�ڵĽ��뵥�Ƿ���Ϣ��ת����
		AggInterest agginfo = getQueryInfo().queryInterestsetInfo(
				zqjdvo.getPk_securities());
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		// �Ƿ��Ǹ�Ϣ��
		boolean fx = InterestTools.isFx(ratevos, trade_date);
		List<SuperVO> list1 = new ArrayList<SuperVO>();
		if (fx) {
			// ����ת�Ҹ���
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
	 * ���뻺��
	 */
	@SuppressWarnings("unchecked")
	public void addCache(CostingTool costingtool, TradeDataTool tradedatatool,
			String[] pks1, String[] pks2) throws BusinessException {

		try {
			if (tradedatatool.isIsinit()) {
				Class<IBill> cs = (Class<IBill>) Class
						.forName("nc.vo.fba_zqjd.trade.zqjdtrade.AggZqjdVO");
				// ��ѯ��������1
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
	 * ����ת�Ը���
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
		// ��ȡ֤ȯ��Ϣ
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
	 * @update jingqt <i>2016��4��22��14:50:52 </i>
	 */
	public UFDouble calcLX(UFDouble num, AggInterest agginfo, UFDate trade_date) {
		if (agginfo == null || num == null)
			return null;
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		Interest headvo = agginfo.getParent();
		if (headvo == null || ratevos == null || ratevos.length == 0)
			return null;
		UFDouble lx = null;
		if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {// ����һ�λ�����Ϣ----�Զ���
			for (Rateperiod vo : ratevos) {
				UFDouble lx1 = SafeCompute.multiply(num, vo.getYear_rate());
				// /** 2016��5��6�� JINGQT ȡʵ�ʵ���������������꣬��ȡ���������� ADD START */
				// UFDouble days = this.getRealDays(headvo, vo.getStart_day(),
				// vo.getEnd_day());
				// lx = SafeCompute.add(lx,
				// lx1.multiply(vo.getDays_num()).div(days));
				// /** 2016��5��6�� JINGQT ȡʵ�ʵ���������������꣬��ȡ���������� ADD END */
				/** 2017��2��21�� xuxmb ��Ϣ�ڼ䲻��һ��ģ�Ӧ��ʵ����������Ӧ�Ҹ���� ADD START */
				if (!isAllYear(vo.getStart_day(), vo.getEnd_day())) {
					// ʵ�ʼ�Ϣ����
					UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
							vo.getEnd_day());
					// ���Ϣ����
					UFDouble allDays = this.getYearDays(headvo,
							vo.getStart_day(), vo.getEnd_day());
					lx = lx1.multiply(realDays).div(allDays);// update lihaibo
																// ��������㷨С����λ������
					// lx = SafeCompute.multiply(lx1,
					// SafeCompute.div(realDays, allDays));
				} else {
					// ʵ�ʼ�Ϣ����
					UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
							vo.getEnd_day());
					// ���Ϣ����
					UFDouble days = this.getYearDays(headvo, vo.getStart_day(),
							vo.getEnd_day());
					lx = SafeCompute.add(lx, lx1.multiply(realDays).div(days));
				}
				/** 2017��2��21�� xuxmb ��Ϣ�ڼ䲻��һ��ģ�Ӧ��ʵ����������Ӧ�Ҹ����ADD END */
			}

		} else {
			Rateperiod vo = InterestTools.getContainsDate(ratevos, trade_date);
			/** 2016��4��22�� JINGQT �����Ϣ��Ҫ����ǰ�����������ǽ�ȥ ADD START */
			UFDouble hasSurplusPercent = InterestTools.getCostSurplus(ratevos,
					vo);
			/** 2016��4��22�� JINGQT �����Ϣ��Ҫ����ǰ�����������ǽ�ȥ ADD END */
			lx = SafeCompute.multiply(num, vo.getYear_rate()).multiply(
					hasSurplusPercent);
			// update by lihaibo ע�͵�--�����������ּ��㷽ʽ
			/*
			 * if (CostConstant.PAYPREYEAR.equals(headvo.getPeriodtype()) ||
			 * CostConstant.SCP.equals(headvo.getPeriodtype())) {// ���긶Ϣ�������� lx
			 * = SafeCompute.div(lx, new UFDouble(1)); } else if
			 * (CostConstant.PAYPREHALFYEAR.equals(headvo .getPeriodtype())) {//
			 * �����긶Ϣ lx = SafeCompute.div(lx, new UFDouble(2)); } else if
			 * (CostConstant.PAYPREQUARTER .equals(headvo.getPeriodtype())) {//
			 * ������Ϣ lx = SafeCompute.div(lx, new UFDouble(4)); } else if
			 * (CostConstant.PAYPREMONTH.equals(headvo.getPeriodtype())) {//
			 * ���¸�Ϣ lx = SafeCompute.div(lx, new UFDouble(12)); }
			 */
			/** 2017��2��21�� xuxmb ��Ϣ�ڼ䲻��һ��ģ�Ӧ��ʵ����������Ӧ�Ҹ���� ADD START */
			if (!isAllYear(vo.getStart_day(), vo.getEnd_day())) {
				UFDouble realDays = this.getRealDaysByVO(vo.getStart_day(),
						vo.getEnd_day());// ʵ�ʼ�Ϣ����
				// UFDouble allDays = this.getYearDays(headvo,
				// vo.getStart_day(),
				// vo.getEnd_day());// ������
				UFDouble allDays = new UFDouble(vo.getDays_num());
				lx = lx.multiply(realDays).div(allDays);// update lihaibo
														// ��������㷨С����λ������
				// lx = SafeCompute.multiply(lx,
				// SafeCompute.div(realDays, allDays));
			}
			/** 2017��2��21�� xuxmb ��Ϣ�ڼ䲻��һ��ģ�Ӧ��ʵ����������Ӧ�Ҹ����ADD END */
		}
		return lx;
	}

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
	private boolean isAllYear(UFDate startDay, UFDate endDay) {
		if (UFDate.getDaysBetween(startDay, endDay) + 1 < 365) {
			return false;
		}
		return true;
	}

	/**
	 * ��ѯӰ�����ص�������VO
	 */
	private BilltypeVO getBdBilltypeVO(String code) throws BusinessException {
		QueryBillTypeInfo info = new QueryBillTypeInfo();
		return info.queryBdBilltypeVO(code);
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
			throw new BusinessException("StocktradeVO ��ʵ��������!");
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
			throw ExceptionHandler.createException("��ǰ��֯�£�����:" + initcode
					+ "��û������! ");
		if ("Y".equals(va))
			falg = true;
		return falg;
	}
}
