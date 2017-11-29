package nc.impl.fba_sim.simtrade.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.fba_sim.simtrade.report.FundInOutUtils;
import nc.bs.fba_sim.simtrade.report.ReportSqlUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_sim.simtrade.report.IACCSecInvestDailyQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.PubReportDataQuery;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sec.secbd.secpropertyhistory.PropertyHistoryVO;
import nc.vo.fba_sim.pub.SimINFOPubMethod;
import nc.vo.fba_sim.simbs.checkplan.CheckplanVO;
import nc.vo.fba_sim.simtrade.report.Accrual_SecInvestDailyMeta;
import nc.vo.fba_sim.simtrade.report.Accrual_SecInvestDailyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.freereport.ReportDataVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

public class ACCSecInvestDailyQueryImpl implements IACCSecInvestDailyQuery {

	/**
	 * ֤ȯ������ʷ��Map<֤ȯ����,֤ȯ��������>
	 * 
	 * @author cjh
	 */
	private static Map<String, String> secpropertyMap = null;

	/**
	 * ���¸���ʱ���
	 * 
	 * @author cjh
	 */
	private static String newestts = "";

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		ConditionVO[] conVos = (ConditionVO[]) context
				.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
		if (qryconutil.isNull() && (conVos == null || conVos.length == 0)) {
			ReportQueryResult result = new ReportQueryResult();
			DataSet dataset = new DataSet();
			dataset.setMetaData(this.getMetaData());
			result.setDataSet(dataset);
			return result;
		}

		ReportQueryResult queryResult = new ReportQueryResult();
		queryResult.setDataSet(genACCSecInvestDailySet(context, conVos));
		return queryResult;
	}

	private DataSet genACCSecInvestDailySet(IContext context,
			ConditionVO[] conVos) throws BusinessException {

		String dateValue = FundInOutUtils.getInstance().getDateCon(conVos);
		String enddate = FundInOutUtils.getInstance().getEnddate(dateValue);
		String org = FundInOutUtils.getInstance().getOrg(conVos);
		String glBook = FundInOutUtils.getInstance().getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);

		// update by lihbj ����ڶ����ѯ�����ڳ�
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY).asLocalEnd();

		String endDate = SimReportUtils.getEndDate(dateValue);

		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);
		List<CheckplanVO> checkPlanVOs = ReportSqlUtil.getInstance()
				.findCheckPlanVo(org, de_group, glBook);

		/* ����ϸ���Ұ�ά�ȷ������ */
		String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		List<ReportDataVO> reportDatas = this.queryData(checkPlanVOs, conVos,
				costPlanVO);
		Map<String, Accrual_SecInvestDailyVO> trademap = mergerData(
				reportDatas, manFieldV);

		List<StockBalanceVO> firBal = null;
		List<StockBalanceVO> endBal = null;
		String conditon = SimReportUtils.getQueryCondition(
				ReportConst.FUND_B_MARK, conVos, true, true);
		PubReportDataQuery query = new PubReportDataQuery();
		try {
			// ��ѯ����ڳ�
			firBal = query.getStockBalance(conditon, startDate.toString(),
					costPlanVO);
			// ����ĩ
			endBal = query.getStockBalance(conditon, endDate, costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("�ڳ���ѯ����");
		}
		/**
		 * ���ڳ����Ӧ����Ϣ YangJie 2014-07-11
		 */
		Map firstibvomap = query.getInterest(startDate.toLocalString()
				+ " 00:00:00", costPlanVO, true);
		// // ��ѯ�ڳ������Ϣ(�ڳ������Ϣȡֵ������)
		// Map<String, InterestdistillVO> firstibvomap = new
		// QueryInterestUtil().getInterestBalanceMap(conditon,
		// startDate.toString(), costPlanVO);
		// �����ڳ������Ϣ�������ϢΪ0�����ԣ���Ϊ0�����ڼ�¼�в�����Ϣ
		Iterator<Entry<String, InterestdistillVO>> it = firstibvomap.entrySet()
				.iterator();
		Entry<String, InterestdistillVO> entry;
		String key = null;
		InterestdistillVO ibvo = null;
		Accrual_SecInvestDailyVO drvo = null;
		while (it.hasNext()) {
			entry = it.next();
			key = entry.getKey();
			ibvo = (InterestdistillVO) firstibvomap.get(key);
			// if (ibvo.getNqclx() == null || new
			// UFDouble(0).compareTo(ibvo.getNqclx()) == 0) {
			// continue;
			// }
			drvo = trademap.get(key);
			if (drvo == null) {
				drvo = new Accrual_SecInvestDailyVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drvo.setAttributeValue(manFieldV[j],
							ibvo.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drvo);
			}
			drvo.setInit_accrual_sum(ibvo.getInterestdistill());
		}
		firstibvomap.clear();
		UFDouble zero = new UFDouble(0);
		// �����ڳ�����������д��ڣ���ֱ�Ӹ�ֵ����������ڣ�����
		int firSize = firBal.size();
		StockBalanceVO sbv = null;
		PubMethod pm = PubMethod.getInstance();
		for (int i = 0; i < firSize; i++) {
			sbv = firBal.get(i);
			if (sbv.getStocks_num() == null
					|| new UFDouble(0).compareTo(sbv.getStocks_num()) == 0) {
				continue;
			}
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			if (trademap.containsKey(key)) {
				drvo = trademap.get(key);
			} else {
				drvo = new Accrual_SecInvestDailyVO();

				for (int j = 0; j < manFieldV.length; j++) {
					drvo.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drvo);
			}
			drvo.setFirstNum(sbv.getStocks_num());
			drvo.setFirstSum(sbv.getStocks_sum());
			// drvo.setInit_total_win(drvo.getTotalWin());
			drvo.setTotalWin(pm.add(drvo.getInit_total_win(),
					sbv.getTotal_win()));// �ۼ�����=�������� +��ѯ��ʼ���ڵ��ڳ��ۼ�����
		}
		firBal.clear();
		/**
		 * ��ȡ���ʼ�ֵ YangJie2014-07-10
		 */
		Map map = query.getFairValue(endDate.substring(0, 10) + " 00:00:00",
				costPlanVO, true);
		// ������ĩ����������д��ڣ���ֱ�Ӹ�ֵ����������ڣ�������
		int endSize = endBal.size();
		for (int i = 0; i < endSize; i++) {
			sbv = endBal.get(i);
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			// UFDouble nowAccrual = new UFDouble(0);// �����г���Ϣ
			if (trademap.containsKey(key)) {
				drvo = trademap.get(key);
				drvo.setEndNum(sbv.getStocks_num());
				drvo.setEndSum(sbv.getStocks_sum());
				drvo.setTrade_date(sbv.getTrade_date());
				try {
					// �м� = ���̼�
					drvo.setMartetprice(SimINFOPubMethod.getInstance()
							.getClosePrice(endDate, sbv.getPk_securities()));
					// ��ֵ = �м� * ��ĩ�������
					// �������뱣����λС�� update by lihaibo
					UFDouble marketSum = pm.multiply(drvo.getMartetprice(),
							drvo.getEndNum());
					marketSum = marketSum.setScale(2, UFDouble.ROUND_HALF_UP);
					drvo.setMarketSum(marketSum);

					// ���ʼ�ֵȡ��ֵ
					drvo.setFairvalueSum((UFDouble) map.get(key));
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		}
		endBal.clear();

		Set<String> c = trademap.keySet();
		Iterator<String> iter = c.iterator();
		UFDouble endAccrualSum = new UFDouble(0);// ��ĩӦ����Ϣ
		/**
		 * ���ڳ����Ӧ����Ϣ YangJie 2014-07-11
		 */
		Map endvomap = query.getInterest(
				endDate.substring(0, 10) + " 00:00:00", costPlanVO, true);
		// Map<String, InterestdistillVO> endvomap = new
		// QueryInterestUtil().getInterestBalanceMap(conditon, endDate,
		// costPlanVO);
		List<Accrual_SecInvestDailyVO> returnV = new ArrayList<Accrual_SecInvestDailyVO>();
		try {
			SimINFOPubMethod.getInstance().removeCaptialHt(endDate);
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			Logger.error(e);
		}

		secpropertyMap = getPropertyhistoryNames(enddate);
		while (iter.hasNext()) {
			key = iter.next();
			InterestdistillVO interVO = (InterestdistillVO) endvomap.get(key);
			drvo = trademap.get(key);

			if (interVO != null) {
				// ��ĩӦ����Ϣ
				endAccrualSum = interVO.getInterestdistill();
			}
			// // ���μ���Ӧ����Ϣ
			// drvo.setAccrual_sum(pm.sub(endAccrualSum,drvo.getInit_accrual_sum()));
			// ������Ϣ������ =��ĩ-�ڳ�-����+����+�Ҹ�
			UFDouble accrual_sum = pm.add(pm.add(pm.sub(
					pm.sub(endAccrualSum, drvo.getInit_accrual_sum()),
					drvo.getBuy_accrual_sum()), drvo.getSell_accrual_sum()),
					drvo.getF_accrual_sum());
			drvo.setAccrual_sum(accrual_sum);
			// ����ӯ�� = ������ֵ �C �����
			drvo.setProfit_loss(pm.sub(
					pm.add(drvo.getFairvalueSum(), new UFDouble(0.0))
							.compareTo(new UFDouble(0)) == 0 ? drvo
							.getMarketSum() : drvo.getFairvalueSum(), drvo
							.getEndSum()));
			// ��ĩӦ����Ϣ
			drvo.setEnd_accrual_sum(endAccrualSum);
			// �ڳ�Ӧ����Ϣ
			// drvo.setInit_accrual_sum(initAccrualSum);
			drvo.setAccrual_balance(pm.add(drvo.getEnd_accrual_sum(),
					drvo.getAccrual_sum()));
			drvo.setTrade_date(UFDate.fromPersisted(endDate));// YangJie
																// 2014-06-12
																// ��ʾ����
			// �����ĩ�ܹɱ� ����û���ܹɱ�
			UFDouble captial = null;
			try {
				captial = SimINFOPubMethod.getInstance().getCaptial(endDate,
						drvo.getPk_securities());
				// �ڳ���ֵ add by lihaibo
				UFDouble first_marketPrice = SimINFOPubMethod.getInstance()
						.getClosePrice(startDate.toString().substring(0, 10),
								drvo.getPk_securities());
				UFDouble first_marketSum = pm.multiply(first_marketPrice,
						drvo.getFirstNum());
				first_marketSum = first_marketSum.setScale(2,
						UFDouble.ROUND_HALF_UP);
				drvo.setFirst_marketSum(first_marketSum);
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				Logger.error(e);
			}
			if (captial != null && !zero.equals(captial)
					&& drvo.getEndNum() != null) {
				drvo.setProportion(pm.div(pm.multiply(drvo.getEndNum(), 100),
						captial));// �ܹɱ�ռ��
			}
			// ���֤ȯ������ʷ
			// String
			// propertyhistoryname=getPropertyhistoryNameBySecurities(drvo.getPk_securities(),enddate);
			String propertyhistoryname = secpropertyMap.get(drvo
					.getPk_securities());
			if (propertyhistoryname != null) {
				drvo.setPropertyhistoryname(propertyhistoryname);
			}
			SuperVO tempvo = filetVO(drvo);// ����vo
			if (null != tempvo) {
				returnV.add((Accrual_SecInvestDailyVO) tempvo);
			}
		}

		return PubMethod.getInstance().convertVOToArray(getMetaData(), returnV);
	}

	/**
	 * �����Ż���֮ǰ����forѭ����ͨ��֤ȯ������ѯ֤ȯ���ƣ��ֲ�ѯ���з�������֤ȯ���Ʒŵ�map��<֤ȯ������֤ȯ����>
	 * 
	 * @author cjh
	 * @param enddate
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, String> getPropertyhistoryNames(String enddate)
			throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		String queryTsSQL = " SELECT TS FROM sec_secpropertyhistory WHERE NVL(DR,0)=0 AND pk_securities is not null ORDER BY TS DESC ";
		ArrayList<Object[]> tslist = (ArrayList<Object[]>) queryservice
				.executeQuery(queryTsSQL.toString(), new ArrayListProcessor());
		if (secpropertyMap != null && tslist != null && tslist.size() > 0) {
			if (newestts.equals(tslist.get(0)[0].toString())) {
				return secpropertyMap;
			}
		}
		if (tslist != null && tslist.size() > 0)
			newestts = tslist.get(0)[0].toString();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sec_secpropertyhistory.pk_securities, ");
		sql.append("       sec_secpropertyhistory.name ");
		sql.append("  FROM sec_secpropertyhistory ");
		sql.append(" WHERE sec_secpropertyhistory.dr = 0 ");
		sql.append("   AND pk_securities is not null ");
		sql.append("   AND to_date(substr(historydate,0,10),'yyyy-mm-dd') <= to_date('"
				+ enddate + "','yyyy-mm-dd')");
		sql.append(" ORDER by to_date(substr(historydate, 0, 10),'yyyy-mm-dd') desc");
		ArrayList<Object[]> list = (ArrayList<Object[]>) queryservice
				.executeQuery(sql.toString(), new ArrayListProcessor());
		secpropertyMap = new HashMap<String, String>();
		if (list == null || list.size() == 0 || list.get(0) == null)
			return secpropertyMap;
		// throw new BusinessException("֤ȯ������ʷ��Ϊ�գ����ʵ���ݣ�");
		for (int i = 0; i < list.size(); i++) {
			String pk_securities = list.get(i)[0].toString().trim();
			if (!(secpropertyMap.get(pk_securities) == null)) {

				continue;
			}
			String name = list.get(i)[1] == null ? null : list.get(i)[1]
					.toString();
			secpropertyMap.put(pk_securities, name);
		}
		return secpropertyMap;
	}

	private String getPropertyhistoryNameBySecurities(String pk_securities,
			String enddate) throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		Collection vos = queryservice
				.retrieveByClause(
						PropertyHistoryVO.class,
						" pk_securities ='"
								+ pk_securities
								+ "' and dr = 0 and to_date(substr(historydate,0,10),'yyyy-mm-dd') <= to_date('"
								+ enddate
								+ "','yyyy-mm-dd')  order by to_date(substr(historydate, 0, 10),'yyyy-mm-dd') desc");
		if (vos != null && vos.size() != 0) {
			Iterator iterator = vos.iterator();
			while (iterator.hasNext()) {
				PropertyHistoryVO vo = (PropertyHistoryVO) iterator.next();
				return vo.getName();
			}
		}
		return null;
	}

	private Map<String, Accrual_SecInvestDailyVO> mergerData(
			List<ReportDataVO> datavos, String[] manFieldV)
			throws BusinessException {
		PubMethod pm = PubMethod.getInstance();
		Map<String, Accrual_SecInvestDailyVO> map = new HashMap<String, Accrual_SecInvestDailyVO>();
		if (datavos.isEmpty()) {
			return map;
		}
		Map<String, List<ReportDataVO>> result = pm.hashlizeObjects(datavos,
				manFieldV);
		List<ReportDataVO> list = null;
		Accrual_SecInvestDailyVO rprtvo = null;
		ReportDataVO datavo = null;
		Iterator<Entry<String, List<ReportDataVO>>> iter = result.entrySet()
				.iterator();
		Entry<String, List<ReportDataVO>> entry;
		String key;
		;
		while (iter.hasNext()) {
			entry = iter.next();
			key = entry.getKey();
			list = entry.getValue();
			if (list.isEmpty()) {
				continue;
			}
			datavo = SimReportUtils.mergerData(list);
			rprtvo = copytoRprtVO(datavo);
			map.put(key, rprtvo);
		}
		return map;
	}

	/**
	 * ReportDataVO ת����ϢƷ���ձ�������
	 * 
	 * @param datavo
	 * @return
	 * @throws Exception
	 */
	private Accrual_SecInvestDailyVO copytoRprtVO(ReportDataVO datavo)
			throws BusinessException {
		PubMethod pm = PubMethod.getInstance();
		Accrual_SecInvestDailyVO rprtvo = new Accrual_SecInvestDailyVO();

		rprtvo.setPk_org(datavo.getPk_org());
		rprtvo.setPk_group(datavo.getPk_group());
		rprtvo.setPk_glorgbook(datavo.getPk_glorgbook());

		rprtvo.setTrade_date(datavo.getTrade_date());
		rprtvo.setBuy_accrual_sum(datavo.getBuyaccrual());
		rprtvo.setBuyNum(datavo.getBuynum());
		rprtvo.setBuySum(datavo.getBuysum());
		rprtvo.setF_accrual_sum(datavo.getFaccrual());
		rprtvo.setPk_capaccount(datavo.getPk_capaccount());
		rprtvo.setPk_client(datavo.getPk_client());
		rprtvo.setPk_operatesite(datavo.getPk_operatesite());
		rprtvo.setPk_partnaccount(datavo.getPk_partnaccount());
		rprtvo.setPk_selfsgroup(datavo.getPk_selfsgroup());

		rprtvo.setPk_assetsprop(datavo.getPk_assetsprop());
		rprtvo.setPk_glorgbook(datavo.getPk_glorgbook());
		rprtvo.setPk_assetsprop(datavo.getPk_assetsprop());
		rprtvo.setPk_securities(datavo.getPk_securities());
		rprtvo.setSellCost(datavo.getSellcost());
		rprtvo.setSellNum(datavo.getSellnum());
		rprtvo.setSellSum(datavo.getSellsum());
		rprtvo.setSell_accrual_sum(datavo.getSellaccrual());
		rprtvo.setInit_total_win(pm.sub(datavo.getSellsum(),
				datavo.getSellcost()));
		return rprtvo;
	}

	/**
	 * ��ѯ����
	 * 
	 * @param checkPlanVOs
	 * @param conVos
	 * @param costPlanVO
	 * @return
	 * @throws BusinessException
	 */
	private List<ReportDataVO> queryData(List<CheckplanVO> checkPlanVOs,
			ConditionVO[] conVos, CostPlanVO costPlanVO)
			throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		List<ReportDataVO> resultDatas = new ArrayList<ReportDataVO>();

		String condition = SimReportUtils.getQueryCondition(
				ReportConst.FUND_B_MARK, conVos, false, true);
		String querySql = null;
		ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
		for (CheckplanVO planVO : checkPlanVOs) {
			// String dirction = planVO.getVdef1();
			String billType = planVO.getPk_billtype();

			querySql = reportUtil.getReportSql(billType);
			if (StringUtils.isBlank(querySql)) {
				continue;
			}

			querySql = reportUtil.getQuerySql(condition, querySql);
			try {
				List<ReportDataVO> reportVos = (List<ReportDataVO>) queryservice
						.executeQuery(querySql, new BeanListProcessor(
								ReportDataVO.class));
				resultDatas.addAll(reportVos);
			} catch (Exception e) {
				throw new BusinessException("��ϸ��ѯ����!" + e.getMessage());
			}
		}
		return resultDatas;
	}

	/**
	 * ��װ����Ԫ����
	 * 
	 * @return
	 */
	private MetaData getMetaData() {
		List<Field> list = new ArrayList<Field>();

		for (String key : Accrual_SecInvestDailyMeta.FUNDTRADE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		for (String key : Accrual_SecInvestDailyMeta.EXT_D_FIELD) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}

		for (String key : Accrual_SecInvestDailyMeta.EXT_DATE_FIELD) {
			Field field = ReportDataUtil.createDateFiled(key);
			list.add(field);
		}

		Field[] fields = new Field[list.size()];
		list.toArray(fields);
		return new MetaData(fields);
	}

	/**
	 * ���vo�������ֶ�Ϊ��ֵ,��Ϊ0.0����null �򷵻�null
	 * 
	 * @param vo
	 */
	public SuperVO filetVO(SuperVO vo) {
		String pk_securities = vo.getAttributeValue("pk_securities").toString();
		Class c;
		Object obj;
		java.lang.reflect.Field[] fields = vo.getClass().getDeclaredFields();
		boolean flag = true;
		for (int i = 0; i < fields.length; i++) {
			c = fields[i].getType();
			if (c.equals(UFDouble.class)) {
				obj = vo.getAttributeValue(fields[i].getName());
				if (null != obj) {
					UFDouble temp = (UFDouble) obj;
					temp = temp.setScale(2, UFDouble.ROUND_HALF_UP);
					if (temp.compareTo(new UFDouble(0.00)) != 0) {
						flag = false;// ������0.00���˳�����
					}
					if (!flag) {
						break;
					}
				}
			}
		}
		if (flag) {
			vo = null;
		}
		return vo;
	}
}
