package nc.impl.fba_smt.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.fba_smt.report.IAvailableDetailRpt;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.PubReportDataQuery;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.report.SMTReportUtil;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.pub.SimINFOPubMethod;
import nc.vo.fba_smt.report.AvailableDetailVO;
import nc.vo.fba_smt.report.AvailableDetailViewMeta;
import nc.vo.fba_smt.trade.margintrade.MarginTradeVO;
import nc.vo.fba_smt.trade.refinancing.RefinancingVO;
import nc.vo.fba_smt.trade.transferrecord.TransferRecordVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

public class AvailableDetailRptImpl implements IAvailableDetailRpt {

	/**
	 * �����������ѩΪʲôҪ�����û���õ��ķ��������Ҵ˷����������������󣬹�ɾ��֮ <br>
	 * 
	 * @author JINGQT 2016��11��23��
	 */
	public AvailableDetailRptImpl() {
		// getQueryConditionDLGInitializer();
	}

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		ConditionVO[] conVos = (ConditionVO[]) context
				.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
		if (qryconutil.isNull() && (conVos == null || conVos.length == 0)) {
			ReportQueryResult result = new ReportQueryResult();
			DataSet dataset = new DataSet();
			dataset.setMetaData(this.genDataSet());
			result.setDataSet(dataset);
			return result;
		}

		ReportQueryResult queryResult = new ReportQueryResult();
		queryResult.setDataSet(getAvailableDetailDataSet(context, conVos));
		return queryResult;

	}

	private DataSet getAvailableDetailDataSet(IContext context,
			ConditionVO[] conVos) throws BusinessException {
		String dateValue = SimReportUtils.getDateCon(conVos);// ȡ�ò�ѯ�����е���������
		String org = SimReportUtils.getOrg(conVos);
		String glBook = SimReportUtils.getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);
		boolean isrzrq = false;
		try {
			isrzrq = SysInitCache.getInstance()
					.getIsRzrq(de_group, org, glBook);
		} catch (Exception e1) {
			Logger.error(e1.getMessage());
		}
		if (!isrzrq) {
			throw new BusinessException("��ǰҵ��Ԫ����������ȯ���ţ�");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("pk_org", org);
		map.put("pk_group", de_group);
		map.put("pk_glorgbook", glBook);

		// UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
		// .getDateBefore(ReportConst.DAY);//��ʼ����ΪʲôҪȡǰһ�죿
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue));
		String endDate = SimReportUtils.getEndDate(dateValue);
		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);

		PubReportDataQuery query = new PubReportDataQuery();
		SmtReportQueryUtil querySmt = new SmtReportQueryUtil();
		List<StockBalanceVO> firBal = null;
		// ���Ӳ�ѯ�������(�˲� ��֯ ���)
		String condition = SMTReportUtil.getStockBalaCondition(
				ReportConst.FUND_B_MARK, conVos, ReportConst.STOCKKR);
		try {
			// ��ѯ����ڳ�
			firBal = query.getStockBalance(condition, startDate.toString(),
					costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("�ڳ���ѯ����");
		}

		String key = null;
		// �����ڳ�����������д��ڣ���ֱ�Ӹ�ֵ����������ڣ�������
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
		AvailableDetailVO drv = null;
		StockBalanceVO sbv = null;
		// String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		// update by lihbj ����pk_stocksortά��
		String[] manFieldV = new String[] { "pk_stocksort", "pk_assetsprop",
				"pk_group", "pk_org", "pk_glorgbook", "pk_capaccount",
				"begin_date", "end_date", "pk_securities" };
		Map<String, AvailableDetailVO> trademap = new HashMap<String, AvailableDetailVO>();
		PubMethod pm = PubMethod.getInstance();
		for (int i = 0; i < firSize; i++) {
			sbv = firBal.get(i);
			if (zero.equals(sbv.getStocks_num())
					&& zero.equals(sbv.getStocks_sum())) {
				continue;
			}
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			if (trademap.containsKey(key)) {
				drv = trademap.get(key);
			} else {
				drv = new AvailableDetailVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drv.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drv);
			}

			drv.setFirstnum(pm.add(drv.getFirstnum(), sbv.getStocks_num()));
			drv.setFirstsum(pm.add(drv.getFirstsum(), sbv.getStocks_sum()));
			drv.setTrade_date(startDate);// �������ɱ�����ͨ��ʱ����ˣ�����д��ʱ��
		}
		//Map<String, UFDouble> closeMap = query.getClosePrice(endDate);
		// �ɹ�����֤ȯת��(�ɹ�ת����) 0 +ȯԴ��ת
		List<SuperVO> kgzkrvos = null;
		List<SuperVO> qyhzhrvos = null;
		try {
			kgzkrvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.KGZKR);// ��ѯ�ɹ�ת��������
			qyhzhrvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO, "QYHZHR");// ��ѯȯԴ��ת���������
			kgzkrvos.addAll(qyhzhrvos);
			trademap = assignTransferData(trademap, kgzkrvos, manFieldV, 0,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ�ɹ�����֤ȯת�����ݳ���");
		}
		// ����֤ȯ(ת��ͨ����) 1
		List<SuperVO> zrtjrvos = null;
		try {
			zrtjrvos = querySmt.getRefinancingData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.ZRTJR);
			trademap = assignTransferData(trademap, zrtjrvos, manFieldV, 1,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯת��ͨ�������ݳ���");
		}
		// �ͻ��黹֤ȯ(�ڳ��黹) 2
		List<SuperVO> rcghvos = null;
		try {
			rcghvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCGHJL);
			rcghvos = setKCZZ(rcghvos, "0002");
			trademap = assignTransferData(trademap, rcghvos, manFieldV, 2,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ�ͻ��黹֤ȯ����");
		}
		// ��������(������� ���ȯԴ��ת) 3 + ���ڹ黹���ڼ�¼
		List<SuperVO> hgvos = null;// ���ȯԴ��ת
		List<SuperVO> hgkgzkr = null;// ��ɿɹ�ת����
		List<SuperVO> yqghkr = null;// ���ڹ黹���ڼ�¼
		try {
			hgvos = querySmt.getTransferData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGQYHZ);// ��ѯ���ȯԴ��ת
			hgkgzkr = querySmt.getTransferData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGKGZKR);// ��ѯ��ɿɹ�ת����
			yqghkr = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.YQGHKR);// ���ڹ黹���ڼ�¼ flag ==
																// 8
			yqghkr = setKCZZ(yqghkr, "0002");
			hgvos.addAll(hgkgzkr);
			trademap = assignTransferData(trademap, yqghkr, manFieldV, 8,
					startDate);// ���ڹ黹���ڼ�¼
			trademap = assignTransferData(trademap, hgvos, manFieldV, 3,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ�������ӳ���");
		}
		// ��ͻ��ڳ�֤ȯ(�ڳ�֤ȯ) 4
		List<SuperVO> rcvos = null;
		try {
			rcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCJL);
			rcvos = setKCZZ(rcvos, "0002");
			trademap = assignTransferData(trademap, rcvos, manFieldV, 4,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ��ͻ��ڳ�֤ȯ����");
		}
		// ת�ؿɹ�����֤ȯ (����ת�ɹ�) 5
		List<SuperVO> krzkgvos = null;
		List<SuperVO> qyhzhcvos = null;
		try {
			krzkgvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.KRZKG);// ��ѯ����ת�ɹ�
			qyhzhcvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO, "QYHZHC");// ��ѯȯԴ��ת�ӿ��ڿ⻮����
			krzkgvos.addAll(qyhzhcvos);
			trademap = assignTransferData(trademap, krzkgvos, manFieldV, 5,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯת�ؿɹ�����֤ȯ����");
		}
		// �黹����֤ȯ(ת��ͨ�黹)6
		List<SuperVO> zrtghvos = null;
		try {
			zrtghvos = querySmt.getRefinancingData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.ZRTGH);// ��ѯת��ͨ�黹
			trademap = assignTransferData(trademap, zrtghvos, manFieldV, 6,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯת��ͨ�黹����");
		}
		// ��������(����ڳ�) 7
		List<SuperVO> hgrcvos = null;
		try {
			hgrcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGRCJL);
			hgrcvos = setKCZZ(hgrcvos, "0002");
			trademap = assignTransferData(trademap, hgrcvos, manFieldV, 7,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ�����������ݳ���");
		}

		/******* add by lihbj ����֤ȯ ���������ݼ��� һ�𰴿����֯ά������ start *******/

		/******* add by lihbj ����֤ȯ ���������ݼ��� һ�𰴿����֯ά������ end *******/

		for (AvailableDetailVO vo : trademap.values()) {
			// �������� = �ڳ�+��������
			UFDouble addnum = pm.add(vo.getFirstnum(),// �ڳ�
					pm.add(vo.getKgzkrnum(),// �ɹ�ת����
							pm.add(vo.getZrtjrnum(),// ת��ͨ����
									pm.add(vo.getRcghnum(),// �ڳ��黹
											vo.getHgqyhznum()))));// ���ȯԴ��ת
			UFDouble addsum = pm.add(vo.getFirstsum(),// �ڳ�
					pm.add(vo.getKgzkrsum(),// �ɹ�ת����
							pm.add(vo.getZrtjrsum(),// ת��ͨ����
									pm.add(vo.getRcghsum(),// �ڳ��黹
											vo.getHgqyhzsum()))));// ���ȯԴ��ת
			// ��������
			UFDouble subnum = pm.add(
					vo.getRczqnum(),
					pm.add(vo.getKrzkgnum(),
							pm.add(vo.getZrtghnum(), vo.getHgrcnum())));
			UFDouble subsum = pm.add(
					vo.getRczqsum(),
					pm.add(vo.getKrzkgsum(),
							pm.add(vo.getZrtghsum(), vo.getHgrcsum())));
			// ��ĩ���= �������� - ��������
			UFDouble endnum = pm.add(vo.getEndnum(), addnum).sub(subnum);
			UFDouble endsum = pm.add(vo.getEndsum(), addsum).sub(subsum);
			vo.setEndnum(endnum);
			vo.setEndsum(endsum);
			// ���̼�
			// UFDouble closeprice = closeMap.get(vo.getPk_securities());
			UFDouble closeprice = UFDouble.ZERO_DBL;
			try {
				closeprice = SimINFOPubMethod.getInstance().getClosePrice(
						endDate.substring(0, 10), vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			vo.setCloseprice(closeprice);
			// ֤ȯ��ֵ = ���̼�*��ĩ�������
			UFDouble marketvalue = pm.multiply(closeprice, endnum);
			vo.setMarketValue(marketvalue);
			// Ǳӯ(��) = ֤ȯ��ֵ-��ĩ�����
			vo.setProfit(pm.sub(marketvalue, endsum));
			/**
			 * ���ڳ���ֵ
			 */
			UFDouble firstprice = UFDouble.ZERO_DBL;
			try {
				firstprice = SimINFOPubMethod.getInstance().getClosePrice(
						startDate.toString().substring(0, 10),
						vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ֤ȯ�ڳ���ֵ = ���̼�*�ڳ�����
			UFDouble firstnum = vo.getFirstnum() == null ? UFDouble.ZERO_DBL
					: vo.getFirstnum();
			UFDouble firstMarket = pm.multiply(firstprice, firstnum);
			vo.setFirstMarket(firstMarket);
		}
		Collection<AvailableDetailVO> c = trademap.values();
		// ȡ��������
		Collection<AvailableDetailVO> yr_c = getStockOutDetailDataSet(context,
				conVos);
		List<AvailableDetailVO> kr_list = new ArrayList<AvailableDetailVO>(c);
		List<AvailableDetailVO> yr_list = new ArrayList<AvailableDetailVO>(yr_c);
		kr_list.addAll(yr_list);
		// c.addAll(yr_c);
		return PubMethod.getInstance().convertVOToArray(genDataSet(), kr_list);
	}

	/**
	 * ȡ��������
	 * 
	 * @author lihbj
	 * @param context
	 * @param conVos
	 * @return
	 * @throws BusinessException
	 */
	private Collection<AvailableDetailVO> getStockOutDetailDataSet(
			IContext context, ConditionVO[] conVos) throws BusinessException {
		String dateValue = SimReportUtils.getDateCon(conVos);// ȡ�ò�ѯ�����е���������
		String org = SimReportUtils.getOrg(conVos);
		String glBook = SimReportUtils.getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);
		boolean isrzrq = false;
		try {
			isrzrq = SysInitCache.getInstance()
					.getIsRzrq(de_group, org, glBook);
		} catch (Exception e1) {
			Logger.error(e1.getMessage());
		}
		if (!isrzrq) {
			throw new BusinessException("��ǰҵ��Ԫ����������ȯ���ţ�");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("pk_org", org);
		map.put("pk_group", de_group);
		map.put("pk_glorgbook", glBook);

		// UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
		// .getDateBefore(ReportConst.DAY);
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue));
		String endDate = SimReportUtils.getEndDate(dateValue);
		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);

		PubReportDataQuery query = new PubReportDataQuery();
		SmtReportQueryUtil querySmt = new SmtReportQueryUtil();
		List<StockBalanceVO> firBal = null;
		// ���Ӳ�ѯ�������(�˲� ��֯ ���)
		String condition = SMTReportUtil.getStockBalaCondition(
				ReportConst.FUND_B_MARK, conVos, ReportConst.STOCKYR);// ��ѯ���ڿ��
		try {
			// ��ѯ����ڳ�
			firBal = query.getStockBalance(condition, startDate.toString(),
					costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("�ڳ���ѯ����");
		}

		String key = null;
		// �����ڳ�����������д��ڣ���ֱ�Ӹ�ֵ����������ڣ�������
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
		AvailableDetailVO drv = null;
		StockBalanceVO sbv = null;
		// String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		String[] manFieldV = new String[] { "pk_stocksort", "pk_assetsprop",
				"pk_group", "pk_org", "pk_glorgbook", "pk_capaccount",
				"begin_date", "end_date", "pk_securities" };
		Map<String, AvailableDetailVO> trademap = new HashMap<String, AvailableDetailVO>();
		PubMethod pm = PubMethod.getInstance();
		for (int i = 0; i < firSize; i++) {
			sbv = firBal.get(i);
			if (zero.equals(sbv.getStocks_num())
					&& zero.equals(sbv.getStocks_sum())) {
				continue;
			}
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			if (trademap.containsKey(key)) {
				drv = trademap.get(key);
			} else {
				drv = new AvailableDetailVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drv.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drv);
			}

			drv.setFirstnum(pm.add(drv.getFirstnum(), sbv.getStocks_num()));
			drv.setFirstsum(pm.add(drv.getFirstsum(), sbv.getStocks_sum()));
			drv.setTrade_date(startDate);// �������ɱ�����ͨ��ʱ����ˣ�����д��ʱ��
		}
		// Map<String, UFDouble> closeMap = query.getClosePrice(endDate);
		// ��ͻ��ڳ�֤ȯ(�ڳ�֤ȯ)1
		List<SuperVO> yr_rcvos = null;
		try {
			yr_rcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCJL);
			yr_rcvos = setKCZZ(yr_rcvos, "0003");
			trademap = yr_assignTransferData(trademap, yr_rcvos, manFieldV, 1,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ��ͻ��ڳ�֤ȯ����");
		}
		// ��������(����ڳ�) 2
		List<SuperVO> yr_hgrcvos = null;
		try {
			yr_hgrcvos = querySmt.getMarginData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.HGRCJL);
			yr_hgrcvos = setKCZZ(yr_hgrcvos, "0003");
			trademap = yr_assignTransferData(trademap, yr_hgrcvos, manFieldV,
					2, startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ���ڿ�������������ݳ���");
		}
		// �ͻ��黹֤ȯ(�ڳ��黹) 3
		List<SuperVO> yr_rcghvos = null;
		try {
			yr_rcghvos = querySmt.getMarginData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.RCGHJL);
			yr_rcghvos = setKCZZ(yr_rcghvos, "0003");
			trademap = yr_assignTransferData(trademap, yr_rcghvos, manFieldV,
					3, startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ�ͻ��黹֤ȯ����");
		}
		// תΪ����֤ȯ(��¼) 4
		List<SuperVO> yqzqvos = null;
		try {
			yqzqvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.YQJL);
			yqzqvos = setKCZZ(yqzqvos, "0003");
			trademap = yr_assignTransferData(trademap, yqzqvos, manFieldV, 4,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯתΪ���ڼ�¼����");
		}

		for (AvailableDetailVO vo : trademap.values()) {
			// �������� = �ڳ�+��������
			UFDouble addnum = pm.add(vo.getFirstnum(),// �ڳ�
					pm.add(vo.getRczqnum(),// �ڳ�֤ȯ
							vo.getHgrcnum()));// ����ڳ�
			UFDouble addsum = pm.add(vo.getFirstsum(),// �ڳ�
					pm.add(vo.getRczqsum(),// �ڳ�֤ȯ
							vo.getHgrcsum()));// ����ڳ�
			// ��������
			// UFDouble subnum = pm.add(vo.getRcghnum(), vo.getYqjlnum());//
			// �ڳ��黹+���ڼ�¼
			// UFDouble subsum = pm.add(vo.getRcghsum(), vo.getYqjlsum());//
			// �ڳ��黹+���ڼ�¼
			// ����û������ҵ�� update by lihbj
			UFDouble subnum = pm.add(vo.getRcghnum(), UFDouble.ZERO_DBL);// �ڳ��黹
			UFDouble subsum = pm.add(vo.getRcghsum(), UFDouble.ZERO_DBL);// �ڳ��黹
			// ��ĩ���= �������� - ��������
			UFDouble endnum = pm.add(vo.getEndnum(), addnum).sub(subnum);
			UFDouble endsum = pm.add(vo.getEndsum(), addsum).sub(subsum);
			vo.setEndnum(endnum);
			vo.setEndsum(endsum);
			// ���̼�
			// UFDouble closeprice = closeMap.get(vo.getPk_securities());
			UFDouble closeprice = UFDouble.ZERO_DBL;
			try {
				closeprice = SimINFOPubMethod.getInstance().getClosePrice(
						endDate.substring(0, 10), vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			vo.setCloseprice(closeprice);
			// ֤ȯ��ֵ = ���̼�*��ĩ�������
			UFDouble marketvalue = pm.multiply(closeprice, endnum);
			vo.setMarketValue(marketvalue);
			// Ǳӯ(��) = ֤ȯ��ֵ-��ĩ�����
			vo.setProfit(pm.sub(marketvalue, endsum));
			/**
			 * ���ڳ���ֵ
			 */
			UFDouble firstprice = UFDouble.ZERO_DBL;
			try {
				firstprice = SimINFOPubMethod.getInstance().getClosePrice(
						startDate.toString().substring(0, 10),
						vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ֤ȯ�ڳ���ֵ = ���̼�*�ڳ�����
			UFDouble firstnum = vo.getFirstnum() == null ? UFDouble.ZERO_DBL
					: vo.getFirstnum();
			UFDouble firstMarket = pm.multiply(firstprice, firstnum);
			vo.setFirstMarket(firstMarket);
		}
		Collection<AvailableDetailVO> c = trademap.values();
		return c;
	}

	private MetaData genDataSet() {
		List<Field> list = new ArrayList<Field>();

		for (String key : AvailableDetailViewMeta.FUNDTRADE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		for (String key : AvailableDetailViewMeta.EXT_D_FIELD) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}

		for (String key : AvailableDetailViewMeta.EXT_DATE_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		Field[] fields = new Field[list.size()];
		list.toArray(fields);
		return new MetaData(fields);
	}

	/**
	 * 
	 * @param trademap
	 *            ��������VO��map
	 * @param mtvos
	 *            �ɹ�ת����(flag=0) ����ת�ɹ���List
	 * @param manFieldV
	 * @param closeMap
	 * @return
	 */
	private Map<String, AvailableDetailVO> assignTransferData(
			Map<String, AvailableDetailVO> trademap, List<SuperVO> mtvos,
			String[] manFieldV, int flag, UFDate startDate) {
		SuperVO mvto = null;
		AvailableDetailVO drv = null;
		PubMethod pm = PubMethod.getInstance();
		// ����ȥ�������֯���� ��Ϊת��ͨ������ڿ� �黹֤��� �������������ʾ����
		manFieldV = new String[] { "pk_stocksort", "pk_assetsprop", "pk_group",
				"pk_org", "pk_glorgbook", "pk_capaccount", "begin_date",
				"end_date", "pk_securities" };
		if (mtvos != null && mtvos.size() > 0) {
			for (int i = 0; i < mtvos.size(); i++) {
				if (0 == flag || 3 == flag) {// �ɹ�ת����
												// ���ȯԴ��ת or ��ɿɹ�ת����
					mvto = (TransferRecordVO) mtvos.get(i);
					manFieldV = new String[] { "pk_stocksort",
							"hr_pk_assetsprop", "pk_group", "pk_org",
							"pk_glorgbook", "hr_pk_capaccount", "begin_date",
							"end_date", "pk_securities" };
				} else if (2 == flag || 4 == flag || 7 == flag || 8 == flag) {// �ڳ���¼
																				// or
					// �ڳ��黹��¼ or
					// ����ڳ���¼
					mvto = (MarginTradeVO) mtvos.get(i);
				} else if (1 == flag || 6 == flag) {// ת��ͨ���� or ת��ͨ�黹
					mvto = (RefinancingVO) mtvos.get(i);

				} else if (5 == flag) {// ����ת�ɹ�
					mvto = (TransferRecordVO) mtvos.get(i);
					manFieldV = new String[] { "pk_stocksort",
							"hc_pk_assetsprop", "pk_group", "pk_org",
							"pk_glorgbook", "hc_pk_capaccount", "begin_date",
							"end_date", "pk_securities" };
				}
				String key = VOUtil.getCombinesKey(mvto, manFieldV);
				if (trademap.containsKey(key)) {// �Ѿ�����
					drv = trademap.get(key);
				}/*
				 * else if (flag == 0 || flag == 3 || flag == 5) {
				 * 
				 * key = VOUtil.getCombinesKey(mvto, manFieldV); if
				 * (trademap.containsKey(key)) {// �Ѿ����� drv = trademap.get(key);
				 * } }
				 */else {
					drv = new AvailableDetailVO();

					for (int j = 0; j < manFieldV.length; j++) {
						drv.setAttributeValue(manFieldV[j],
								mvto.getAttributeValue(manFieldV[j]));

					}
					if (0 == flag || 3 == flag) {// �ɹ�ת���� or ��ɿɹ�ת�����ʽ��˺�Ϊ��
						String pk_capaccount = (String) mvto
								.getAttributeValue("hr_pk_capaccount");
						String pk_assetsprop = (String) mvto
								.getAttributeValue("hr_pk_assetsprop");
						drv.setAttributeValue("pk_capaccount", pk_capaccount);
						drv.setAttributeValue("pk_assetsprop", pk_assetsprop);
					}
					if (5 == flag) {// �ɹ�ת���� or ��ɿɹ�ת�����ʽ��˺�Ϊ��
						String pk_capaccount = (String) mvto
								.getAttributeValue("hc_pk_capaccount");
						String pk_assetsprop = (String) mvto
								.getAttributeValue("hc_pk_assetsprop");
						drv.setAttributeValue("pk_capaccount", pk_capaccount);
						drv.setAttributeValue("pk_assetsprop", pk_assetsprop);
					}

					trademap.put(key, drv);
				}
				if (0 == flag) {// �ɹ�ת����
					drv.setKgzkrnum(pm.add(drv.getKgzkrnum(),
							((TransferRecordVO) mvto).getBargain_num()));
					drv.setKgzkrsum(pm.add(drv.getKgzkrsum(),
							((TransferRecordVO) mvto).getBargain_sum()));
				} else if (1 == flag) {// ת��ͨ����
					drv.setZrtjrnum(pm.add(drv.getZrtjrnum(),
							((RefinancingVO) mvto).getBargain_num()));
					drv.setZrtjrsum(pm.add(drv.getZrtjrsum(),
							((RefinancingVO) mvto).getBargain_sum()));
				} else if (2 == flag) {// �ڳ��黹
					drv.setRcghnum(pm.add(drv.getRcghnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRcghsum(pm.add(drv.getRcghsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (3 == flag) {// ��������(���ȯԴ��ת+��ɿɹ�ת����)
					drv.setHgqyhznum(pm.add(drv.getHgqyhznum(),
							((TransferRecordVO) mvto).getBargain_num()));
				} else if (4 == flag) {// �ڳ�
					drv.setRczqnum(pm.add(drv.getRczqnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRczqsum(pm.add(drv.getRczqsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (5 == flag) {// ����ת�ɹ�
					drv.setKrzkgnum(pm.add(drv.getKrzkgnum(),
							((TransferRecordVO) mvto).getBargain_num()));
					drv.setKrzkgsum(pm.add(drv.getKrzkgsum(),
							((TransferRecordVO) mvto).getBargain_sum()));
				} else if (6 == flag) {// ת��ͨ�黹
					drv.setZrtghnum(pm.add(drv.getZrtghnum(),
							((RefinancingVO) mvto).getBargain_num()));
					drv.setZrtghsum(pm.add(drv.getZrtghsum(),
							((RefinancingVO) mvto).getBargain_sum()));
				} else if (7 == flag) {// ��������(����ڳ�)
					drv.setHgrcnum(pm.add(drv.getHgrcnum(),
							((MarginTradeVO) mvto).getBargain_num()));

				} else if (8 == flag) {// ���ڹ黹���ڼ�¼ ���������ӣ�
					drv.setHgqyhznum(pm.add(drv.getHgqyhznum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setHgqyhzsum(pm.add(drv.getHgqyhzsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				}

				drv.setTrade_date(startDate);// �������ɱ�����ͨ��ʱ����ˣ�����д��ʱ��
			}
		}
		return trademap;
	}

	/**
	 * ��ò�ѯģ��������ӿ�,�ɸ�д
	 * 
	 * @return
	 */
	// protected IQueryConditionDLGInitializer getQueryConditionDLGInitializer()
	// {
	// SmtQueryConditionDLGInitializer dlgInit = new
	// SmtQueryConditionDLGInitializer();
	// return dlgInit;
	// }

	/**
	 * 
	 * @param trademap
	 *            ��������VO��map
	 * @param mtvos
	 *            �ɹ�ת����(flag=0) ����ת�ɹ���List
	 * @param manFieldV
	 * @param closeMap
	 * @return
	 */
	private Map<String, AvailableDetailVO> yr_assignTransferData(
			Map<String, AvailableDetailVO> trademap, List<SuperVO> mtvos,
			String[] manFieldV, int flag, UFDate startDate) {
		SuperVO mvto = null;
		AvailableDetailVO drv = null;
		PubMethod pm = PubMethod.getInstance();
		// ����ȥ�������֯���� ��Ϊת��ͨ������ڿ� �黹֤��� �������������ʾ����
		manFieldV = new String[] { "pk_stocksort", "pk_assetsprop", "pk_group",
				"pk_org", "pk_glorgbook", "pk_capaccount", "begin_date",
				"end_date", "pk_securities" };
		if (mtvos != null && mtvos.size() > 0) {
			for (int i = 0; i < mtvos.size(); i++) {
				mvto = (MarginTradeVO) mtvos.get(i);
				String key = VOUtil.getCombinesKey(mvto, manFieldV);
				if (trademap.containsKey(key)) {// �Ѿ�����
					drv = trademap.get(key);
				} else {
					drv = new AvailableDetailVO();
					for (int j = 0; j < manFieldV.length; j++) {
						drv.setAttributeValue(manFieldV[j],
								mvto.getAttributeValue(manFieldV[j]));
					}
					trademap.put(key, drv);
				}
				if (1 == flag) {// �ڳ���¼
					drv.setRczqnum(pm.add(drv.getRczqnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRczqsum(pm.add(drv.getRczqsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (2 == flag) {// ��������(����ڳ�)
					drv.setHgrcnum(pm.add(drv.getHgrcnum(),
							((MarginTradeVO) mvto).getBargain_num()));
				} else if (3 == flag) {// �ڳ��黹
					drv.setRcghnum(pm.add(drv.getRcghnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRcghsum(pm.add(drv.getRcghsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (4 == flag) {// ���ڼ�¼--����û�и�ҵ��
					// drv.setYqjlnum(pm.add(drv.getYqjlnum(), ((MarginTradeVO)
					// mvto).getBargain_num()));
					// drv.setYqjlsum(pm.add(drv.getYqjlsum(), ((MarginTradeVO)
					// mvto).getBargain_sum()));
				}
				drv.setTrade_date(startDate);// �������ɱ�����ͨ��ʱ����ˣ�����д��ʱ��
			}
		}
		return trademap;
	}

	/**
	 * ���ÿ����֯
	 * 
	 * @param svos
	 * @param code
	 *            0002--���� 0003--����
	 * @return
	 */
	private List<SuperVO> setKCZZ(List<SuperVO> svos, String code) {
		if (svos != null && svos.size() > 0) {
			for (SuperVO svo : svos) {
				if (code.equals("0002")) {// ����
					((MarginTradeVO) svo)
							.setPk_stocksort("0001SE00000000000002");
				} else if (code.equals("0003")) {// ����
					((MarginTradeVO) svo)
							.setPk_stocksort("0001SE00000000000003");
				}
			}
		}
		return svos;
	}
}
