package nc.impl.fba_fund.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.fba_fund.report.IFundTallyReport;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.fba_fund.pub.CostConstant;
import nc.vo.fba_fund.report.FundTallyReportVO;
import nc.vo.fba_fund.report.FundTallyViewMeta;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

/**
 * ���ʹ���̨��
 * 
 * @author qs
 * @since 2017-6-16����9:12:23
 */
public class FundTallyReportImpl implements IFundTallyReport {
	PubMethod pm = PubMethod.getInstance();

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		// // ��ѯ����
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
		queryResult.setDataSet(genFundTallyReportDataSet(context, conVos));
		return queryResult;
	}

	/**
	 * ͨ����ѯ������ȡ��ѯ���
	 * 
	 * @author qs
	 * @throws BusinessException
	 * @since 2017-6-16����9:29:12
	 */
	private DataSet genFundTallyReportDataSet(IContext context,
			ConditionVO[] conVos) throws BusinessException {
		// ��ѯ���ʹ���̨�˱�����
		List<FundTallyVO> fundTallyVOs;
		try {
			fundTallyVOs = this.queryFundTallyInfo(conVos);
		} catch (DAOException e) {
			throw new BusinessException("��ѯ����");
		}
		List<FundTallyReportVO> result = null;
		// �пգ���ѯ�����ֵ�Ž��к�������
		if (null != fundTallyVOs) {
			// ��������
			result = this.processData(fundTallyVOs, conVos);
		}
		return PubMethod.getInstance().convertVOToArray(getMetaData(), result);
	}

	/**
	 * ��ѯ���ʹ����������
	 * 
	 * @author qs
	 * @throws DAOException
	 * @since 2017-6-16����9:52:00
	 */
	@SuppressWarnings("unchecked")
	private List<FundTallyVO> queryFundTallyInfo(ConditionVO[] conVos)
			throws DAOException {
		// ��ò�ѯ����sql
		String querySql = this.getQueryCondition(conVos);
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from fund_fundtally t where");
		sql.append(" isnull(dr,0)=0 ");
		// sql.append(" and t.denomination>0 ");
		// sql.append(" and t.isdq='N' ");
		// sql.append(" and t.operindex  <> 0 ");
		sql.append("and (t.operindex  <> 0 or (t.operindex = 0 and t.isdq='N')) ");
		// ƴ���ϲ�ѯ����
		if (StringUtils.isNotBlank(querySql)) {
			sql.append(" and ");
			sql.append(querySql);
		}
		sql.append("order by t.operindex asc");
		return (List<FundTallyVO>) new BaseDAO().executeQuery(sql.toString(),
				new BeanListProcessor(FundTallyVO.class));
	}

	/**
	 * ���ݲ�ѯ��������sql
	 * 
	 * @author qs
	 * @return
	 * @since 2017-6-16����3:11:15
	 */
	private String getQueryCondition(ConditionVO[] conVos) {
		StringBuilder sql = new StringBuilder();
		if (null != conVos) {
			for (int i = 0; i < conVos.length; i++) {
				// ��ѯ�ڼ䣺��̨�˱�������ڱȽ�
				if (conVos[i].getFieldCode().equals("begindistill_date")) {
					String startDate = SimReportUtils.getStartDate(conVos[i]
							.getValue());
					String endDate = SimReportUtils.getEndDate(conVos[i]
							.getValue());
					// sql.append(" and t").append(".").append(conVos[i].getFieldCode());
					sql.append(" and t.begindistill_date");
					sql.append(" >= '").append(startDate + "'");
					// sql.append(" and t").append(".").append(conVos[i].getFieldCode());
					sql.append(" and t.begindistill_date");
					sql.append(" <= '").append(endDate + "'");
				}
				if (conVos[i].getFieldCode().equals("pk_org")) {
					sql.append(" and t").append(".pk_org ='")
							.append(conVos[i].getValue() + "' ");
				}
				if (conVos[i].getFieldCode().equals("pk_billtype")) {
					sql.append(" and t").append(".pk_billtype ='")
							.append(conVos[i].getValue() + "' ");
				}
			}
		}

		if (StringUtils.isNotBlank(sql.toString())) {
			return sql.substring(4);
		} else {
			return sql.toString();
		}
	}

	/**
	 * ���ݲ�ѯ���Ŀ�����ݣ�ת���ɱ�����ʾ���
	 * 
	 * @author qs
	 * @return
	 * @throws BusinessException
	 * @since 2017-6-16����9:57:46
	 */
	private List<FundTallyReportVO> processData(List<FundTallyVO> vos,
			ConditionVO[] conVos) throws BusinessException {
		List<FundTallyReportVO> result = new ArrayList<FundTallyReportVO>();
		// ̨�����ݻ��棺key-��ͬ�� ��value-�˺�ͬ�ŵ�̨������
		HashMap<String, List<FundTallyVO>> map = new HashMap<String, List<FundTallyVO>>();
		for (FundTallyVO vo : vos) {
			// ��ͬ��
			String contractno = vo.getContractno();
			if (map.containsKey(contractno)) {
				map.get(contractno).add(vo);
			} else {
				List<FundTallyVO> list = new ArrayList<FundTallyVO>();
				list.add(vo);
				map.put(contractno, list);
			}
		}
		Set<?> set = map.entrySet();
		Iterator<?> iterator = set.iterator();
		FundTallyReportVO reportVO = null;
		while (iterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mapentry = (Map.Entry) iterator.next();
			@SuppressWarnings("unchecked")
			List<FundTallyVO> tallylist = (List<FundTallyVO>) mapentry
					.getValue();
			// ����������ʾ����
			reportVO = this.buildReportVO(tallylist, conVos);
			result.add(reportVO);
		}
		return result;

	}

	/**
	 * ����������ʾ����
	 * 
	 * @author qs
	 * @throws BusinessException
	 * @throws DAOException
	 * @since 2017-6-22����6:48:04
	 */
	private FundTallyReportVO buildReportVO(List<FundTallyVO> tallylist,
			ConditionVO[] conVos) throws BusinessException {
		FundTallyReportVO reportVO = new FundTallyReportVO();
		String startDate = null;
		String endDate = null;
		if (null != conVos) {
			// ��ȡ��ѯ�����в�ѯ�ڼ����ʼ�պͽ����գ��������õ�̨�˱��еļ��Ὺʼ����begindistill_date��
			for (int i = 0; i < conVos.length; i++) {
				// ���Ὺʼ����
				if (conVos[i].getFieldCode().equals("begindistill_date")) {
					// ��ʼ��
					startDate = SimReportUtils.getStartDate(conVos[i]
							.getValue());
					// ������
					endDate = SimReportUtils.getEndDate(conVos[i].getValue());
				}
			}
		}
		UFDate startdate = null;
		UFDate enddate = null;
		if (null != startDate) {
			// ��startDateת����UFDate
			startdate = new UFDate(startDate);
			// ��endDateת����UFDate
			enddate = new UFDate(endDate);
			// �����������=��ѯ��ʼ�գ����ڳ������ֵ������Ϊ���ڷ���
			if (tallylist.get(0).getBegindistill_date().asBegin()
					.equals(startdate)) {
				// ���ڳ����/̯��ɱ���
				reportVO.setStart_balance(tallylist.get(0).getDenomination());
			} else {
				// �����ڷ��С�
				reportVO.setCur_issue(tallylist.get(0).getDenomination());
			}
			// �����ѯ�����Ĳ�ѯ�ڼ���ֵ����begindistill_date����ֵ���������ս����ѯ���
			reportVO.setBegindistill_date(startDate);
		} else {
			// ���ڳ����/̯��ɱ���
			reportVO.setStart_balance(tallylist.get(0).getDenomination());
		}
		// ����ĩ���/̯��ɱ���
		reportVO.setEnd_balance(tallylist.get(tallylist.size() - 1)
				.getDenomination());
		UFDouble start_interest = null;
		UFDouble this_interest = null;
		for (FundTallyVO vo : tallylist) {
			if (null != startDate) {
				// ��startDateת����UFDate
				startdate = new UFDate(startDate);
			}
			// �����ѯ��ʼ�յ��������ݣ����ڳ�Ӧ����Ϣ�����ֵ
			if (vo.getBegindistill_date().equals(startdate)) {
				// ���ڳ�Ӧ����Ϣ��=�ڳ����Ѽ�����Ϣ
				start_interest = vo.getHad_interest();
			}
			// ������Ӧ����Ϣ��
			if (null != vo.getThis_interest()) {
				// �ۼ�
				this_interest = pm.add(this_interest, vo.getThis_interest());
			}
		}
		// ���ڳ�Ӧ����Ϣ��
		reportVO.setStart_payableinterest(start_interest);
		// ������Ӧ����Ϣ��
		reportVO.setCur_payableinterest(this_interest);
		// ��������Ϣ֧����=����Ӧ����Ϣ��û��ʵ����������£�
		reportVO.setCur_expend(this_interest);
		// ����ڳ���ĩ���ȣ�˵�����ڶҸ�����
		if (!(tallylist.get(0).getDenomination().equals(tallylist.get(
				tallylist.size() - 1).getDenomination()))) {
			try {
				// ��ѯ��������
				List<FundTallyVO> list = this.queryDqTallyInfo(enddate,
						tallylist.get(0).getContractno());
				UFDouble pay_balance = null;
				for (int i = 0; i < list.size(); i++) {
					pay_balance = pm.add(pay_balance, list.get(i)
							.getDenomination());
				}
				// ���Ҹ�����
				reportVO.setPay_balance(pay_balance);
			} catch (DAOException e) {
				throw new BusinessException("��ѯ����");
			}
			// �����ڶҸ���Ϣ��
			reportVO.setCur_payinterest(pm.add(
					reportVO.getStart_payableinterest(),
					reportVO.getCur_payableinterest()));
		}
		// ����ĩӦ����Ϣ��=�ڳ�Ӧ����Ϣ���+����Ӧ����Ϣ-���ڶҸ�
		reportVO.setEnd_payableinterest(pm.sub(
				pm.add(start_interest, this_interest),
				reportVO.getCur_payinterest()));
		// ҵ�����
		reportVO.setPk_billtype(tallylist.get(0).getPk_billtype());
		// ��Ʒ����ַ�����
		reportVO.setProductorcounterparty(tallylist.get(0)
				.getProductorcounterparty());
		// ������
		reportVO.setManagedept(tallylist.get(0).getManagedept());
		// �����=���
		reportVO.setDenomination(tallylist.get(0).getDenomination());
		// ����
		reportVO.setTimelimit(tallylist.get(0).getTimelimit());
		// ��Ϣ��
		reportVO.setBegindate(tallylist.get(0).getBegindate());
		// ������
		reportVO.setEnddate(tallylist.get(0).getEnddate());
		if (null != tallylist.get(0).getPaytype()) {
			// ��Ϣ��ʽ
			int paytype = tallylist.get(0).getPaytype();
			switch (paytype) {
			case 0:
				reportVO.setPaytype(CostConstant.PAYTYPE.SEASON.getName());
				break;
			case 1:
				reportVO.setPaytype(CostConstant.PAYTYPE.ONETIME.getName());
				break;
			case 2:
				reportVO.setPaytype(CostConstant.PAYTYPE.YEAR.getName());
				break;
			}
		}
		if (null != tallylist.get(0).getFundtype()) {
			// ��������
			int fundtype = tallylist.get(0).getFundtype().intValue();
			switch (fundtype) {
			case 0:
				// reportVO.setFundtype(FundEnum.ENUM0.getName());
				reportVO.setFundtype(CostConstant.FUNDTYPE.INTERBANKLENDING
						.getName());
				break;
			case 1:
				reportVO.setFundtype(CostConstant.FUNDTYPE.GOFINANCING
						.getName());
				break;
			case 2:
				reportVO.setFundtype(CostConstant.FUNDTYPE.SHORTFUND.getName());
				break;
			case 3:
				reportVO.setFundtype(CostConstant.FUNDTYPE.SHORTPROFIT
						.getName());
				break;
			case 4:
				reportVO.setFundtype(CostConstant.FUNDTYPE.SHORTSUBBOND
						.getName());
				break;
			case 5:
				reportVO.setFundtype(CostConstant.FUNDTYPE.SHORTCOMPONYBOND
						.getName());
				break;
			case 6:
				reportVO.setFundtype(CostConstant.FUNDTYPE.LONGSUBBOND
						.getName());
				break;
			case 7:
				reportVO.setFundtype(CostConstant.FUNDTYPE.LONGPROFIT.getName());
				break;
			case 8:
				reportVO.setFundtype(CostConstant.FUNDTYPE.LONGCOMPONYBOND
						.getName());
				break;
			default:
				reportVO.setFundtype(CostConstant.FUNDTYPE.PROFITTRANSFERREPO
						.getName());
				break;
			}
		}
		// ִ��Ʊ������
		reportVO.setExcutefacerate(tallylist.get(0).getExcutefacerate());
		// ʵ������
		reportVO.setRealrate(tallylist.get(0).getRealrate());
		// ��������
		reportVO.setTrade_date(tallylist.get(0).getTrade_date());
		// ��֯
		reportVO.setPk_org(tallylist.get(0).getPk_org());
		return reportVO;
	}

	/**
	 * ��ѯ���ڶҸ�������Ϣ
	 * 
	 * @author qs
	 * @since 2017-6-26����3:53:09
	 */
	@SuppressWarnings("unchecked")
	private List<FundTallyVO> queryDqTallyInfo(UFDate enddate, String contractno)
			throws DAOException {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from fund_fundtally t where");
		sql.append(" isnull(dr,0)=0 ");
		sql.append(" and t.isdq='Y' ");
		sql.append(" and t.operindex='0' ");
		sql.append(" and t.enddate<='" + enddate + "'");
		sql.append(" and t.contractno='" + contractno + "'");
		sql.append("order by t.operindex asc");
		return (List<FundTallyVO>) new BaseDAO().executeQuery(sql.toString(),
				new BeanListProcessor(FundTallyVO.class));
	}

	/**
	 * ��ȡҪ��ʾ���ֶ�
	 * 
	 * @author qs
	 * @since 2017-6-16����9:20:38
	 */
	private MetaData getMetaData() {
		List<Field> list = new ArrayList<Field>();

		for (String key : FundTallyViewMeta.FUNDTALLY_FILED) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}
		for (String key : FundTallyViewMeta.CAPITAL_FILED) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}
		for (String key : FundTallyViewMeta.INTEREST_FILED) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}
		Field[] fields = new Field[list.size()];
		list.toArray(fields);
		return new MetaData(fields);
	}

}
