package nc.impl.fba_fund.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.fba_fund.report.NewIFundTallyReport;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.fba_fund.interbanklending.InterbankLendingVO;
import nc.vo.fba_fund.pub.CostConstant;
import nc.vo.fba_fund.report.FundTallyViewMeta;
import nc.vo.fba_fund.report.NewFundTallyReportVO;
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
public class NewFundTallyReportImpl implements NewIFundTallyReport {
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
		List<NewFundTallyReportVO> result = null;
		// �пգ���ѯ�����ֵ�Ž��к�������
		if (null != fundTallyVOs) {
			// ��������
			result = this.processData(fundTallyVOs, conVos);
		}
		return PubMethod.getInstance().convertVOToArraynew(getMetaData(), result);
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
	private List<NewFundTallyReportVO> processData(List<FundTallyVO> vos,
			ConditionVO[] conVos) throws BusinessException {
		List<NewFundTallyReportVO> result = new ArrayList<NewFundTallyReportVO>();
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
		NewFundTallyReportVO reportVO = null;
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
	private NewFundTallyReportVO buildReportVO(List<FundTallyVO> tallylist,
			ConditionVO[] conVos) throws BusinessException {
		NewFundTallyReportVO reportVO = new NewFundTallyReportVO();
		FundTallyVO fundVO = tallylist.get(0);
		InterbankLendingVO inbaleVO = getInterbankLendingVO(fundVO);
		String pk_securities = "";
		if (inbaleVO.getVdef1() != null)
			pk_securities = inbaleVO.getVdef1();
		String startDate = null;
		String endDate = null;
		UFDouble start_interest = new UFDouble(0);
		UFDouble this_interest = new UFDouble(0);
		boolean b = false;// �жϵ����Ƿ��ǳ�����������
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
		UFDouble pay_balance = new UFDouble(0);
		try {
			// ��ѯ��������
			List<FundTallyVO> list = this.queryDqTallyInfo(enddate, tallylist
					.get(0).getContractno());
			for (int i = 0; i < list.size(); i++) {
				pay_balance = pm
						.add(pay_balance, list.get(i).getDenomination());
			}

		} catch (DAOException e) {
			throw new BusinessException("��ѯ����");
		}

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
				if (inbaleVO != null)
					reportVO.setCur_issue(tallylist.get(0).getDenomination()
							.sub(new UFDouble(inbaleVO.getVdef2())));
			}
			// �����ѯ�����Ĳ�ѯ�ڼ���ֵ����begindistill_date����ֵ���������ս����ѯ���
			reportVO.setBegindistill_date(startDate);
		} else {
			// ���ڳ����/̯��ɱ���=���ڷ���-��ʼ����ǰ���й黹��+����̯��(����ʼ����ǰ������Ϣ֧��-Ӧ����Ϣ)
			reportVO.setStart_balance(tallylist.get(0).getDenomination()
					.sub(new UFDouble(inbaleVO.getVdef2()))
					.sub(getpay_balance(fundVO, startDate, null))
					.add(getcur_expend(fundVO, startDate, null))
					.sub(getinterest_payable(fundVO, startDate, null)));
		}
		// ����ĩ���/̯��ɱ���
		reportVO.setEnd_balance(tallylist.get(tallylist.size() - 1)
				.getDenomination());
		// ��Ϣ��
		reportVO.setBegindate(tallylist.get(0).getBegindate());
		// ������
		reportVO.setEnddate(tallylist.get(0).getEnddate());
		// ���Ҹ�����
		reportVO.setPay_balance(getpay_balance(fundVO, startDate, endDate));
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
		//�����ʼ����Ŵ�����Ϣ���
		if(tallylist.size()>1){
			// ���ڳ�Ӧ����Ϣ��
			reportVO.setStart_payableinterest(start_interest);
			// ������Ӧ����Ϣ��
			reportVO.setCur_payableinterest(this_interest);
			// ��������Ϣ֧����=����Ӧ����Ϣ��û��ʵ����������£�
			reportVO.setCur_expend(getcur_expend(fundVO, startDate, enddate.toString()));
			//����̯��ֵ --zq = ������Ϣ֧��-����Ӧ����Ϣ
			if(reportVO.getCur_expend()!=null && reportVO.getCur_payableinterest()!=null){
				reportVO.setCur_amortize(pm.sub(reportVO.getCur_expend(), reportVO.getCur_payableinterest()));
			}
			// �����ڶҸ���Ϣ��
			reportVO.setCur_payinterest(getRedemption_interest(fundVO,startDate, endDate));
			// ���ڳ�Ӧ����Ϣ��--zq �ڳ�ǰ������Ϣ֧��-���жҸ���Ϣ
			//reportVO.setStart_payableinterest(getcur_expend(fundVO, startDate, null).sub(getRedemption_interest(fundVO, startDate, null)));
			// ����ĩӦ����Ϣ��=�ڳ�Ӧ����Ϣ���+����Ӧ����Ϣ-���ڶҸ�--zq
			reportVO.setEnd_payableinterest(reportVO.getStart_payableinterest().add(reportVO.getCur_expend()).sub(reportVO.getCur_payableinterest()));
			// �վ�ռ��--zq
			reportVO.setDaily_possession(getdaily_possession(reportVO, startDate,
					endDate));
			// �ʽ�ɱ��� --zq
			if (reportVO.getCur_expend() != null
					&& reportVO.getDaily_possession() != null) {
				if (!reportVO.getDaily_possession().equals(new UFDouble(0)))
					reportVO.setCostrate_capital(reportVO.getCur_expend().div(
							reportVO.getDaily_possession()));
			}
		}
		// ����ڳ���ĩ���ȣ�˵�����ڶҸ�����
		if (!(tallylist.get(0).getDenomination().equals(tallylist.get(
				tallylist.size() - 1).getDenomination()))) {
			// ���Ҹ�����
			reportVO.setPay_balance(getpay_balance(fundVO, startDate, endDate));
			//reportVO.setPay_balance(pay_balance);
			// �����ڶҸ���Ϣ��
			reportVO.setCur_payinterest(getRedemption_interest(fundVO,startDate, endDate));
		}
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
			b = getLongfundtype(reportVO);
		}
		//�ж���������--zq
		if (b) {
			// ִ��Ʊ������
			if (!pk_securities.equals("")) {
				reportVO.setExcutefacerate(getInterestrate(pk_securities));
				// ʵ������=ִ��Ʊ������*ʵ�����ʱ���
				if (reportVO.getExcutefacerate() != null
						&& tallylist.get(0).getRealrate() != null)
					reportVO.setRealrate(reportVO.getExcutefacerate().multiply(
							tallylist.get(0).getRealrate()));
			}
		} else {
			// ִ��Ʊ������
			reportVO.setExcutefacerate(tallylist.get(0).getExcutefacerate());
			// ʵ������=ִ��Ʊ������*ʵ�����ʱ���
			if (tallylist.get(0).getExcutefacerate() != null
					&& tallylist.get(0).getRealrate() != null)
				reportVO.setRealrate(tallylist.get(0).getExcutefacerate()
						.multiply(tallylist.get(0).getRealrate()));
		}

		// ��������
		reportVO.setTrade_date(tallylist.get(0).getTrade_date());
		// ��֯
		reportVO.setPk_org(tallylist.get(0).getPk_org());
		return reportVO;
	}

	/**
	 * @author ZQ ��ѯ����̨�˶�Ӧ��ͬҵ���
	 * @param fundVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private InterbankLendingVO getInterbankLendingVO(FundTallyVO fundVO) {
		List<InterbankLendingVO> list = new ArrayList<InterbankLendingVO>();
		InterbankLendingVO inventVO = new InterbankLendingVO();
		BaseDAO dao = new BaseDAO();
		String sql = "select * from fund_interbanklending "
				+ "where nvl(dr,0)=0 " + "and contractno ='"
				+ fundVO.getContractno() + "'";
		try {
			list = (List<InterbankLendingVO>) dao.executeQuery(sql,
					new BeanListProcessor(InterbankLendingVO.class));
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (list.size() > 0 && list != null)
			inventVO = list.get(list.size()-1);
		return inventVO;
	}

	/**
	 * �ڳ���ĩ�黹���� ���ǵ����ڹ黹������������ڳ�����ʱ��Ҫ��ȥ�黹����,��ĩ���ϱ��ڹ黹
	 * 
	 * @author ZQ
	 * @param fundVO
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private UFDouble getpay_balance(FundTallyVO fundVO, String begindate,
			String enddate) {
		String contractno = fundVO.getContractno();
		UFDouble pay_balance = new UFDouble(0);
		BaseDAO dao = new BaseDAO();
		StringBuffer sb = new StringBuffer();
		Vector vec = new Vector();
		sb.append("select sum(denomination) from fund_interbanklending where nvl(dr,0)=0 and contractno = '");
		sb.append(contractno);
		sb.append("' and transtypecode = 'HV8A-0xx-02'");
		if (enddate == null) {
			sb.append(" and trade_date <'" + begindate.substring(0, 10)
					+ " 00:00:00'");
		} else {
			sb.append(" and trade_date >='" + begindate.substring(0, 10)
					+ " 00:00:00'");
			sb.append(" and trade_date <='" + enddate.substring(0, 10)
					+ " 23:59:59");
		}
		try {
			vec = (Vector) dao.executeQuery(sb.toString(),
					new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			pay_balance = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return pay_balance;

	}
	
	/**
	 * ��ȡ��Ϣ֧��
	 * @param fundVO
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private UFDouble getcur_expend(FundTallyVO fundVO, String begindate,
			String enddate) {
		String contractno = fundVO.getContractno();
		UFDouble cur_expend = new UFDouble(0);
		BaseDAO dao = new BaseDAO();
		StringBuffer sb = new StringBuffer();
		Vector vec = new Vector();
		sb.append("select sum(vdef4) from fund_fundtally where nvl(dr,0)=0 and contractno = '");
		sb.append(contractno);
		sb.append("'");
		if (enddate == null) {
			sb.append(" and trade_date <'" + begindate.substring(0, 10)
					+ " 00:00:00'");
		} else {
			sb.append(" and trade_date >='" + begindate.substring(0, 10)
					+ " 00:00:00'");
			sb.append(" and trade_date <='" + enddate.substring(0, 10)
					+ " 23:59:59");
		}
		try {
			vec = (Vector) dao.executeQuery(sb.toString(),
					new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			cur_expend = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return cur_expend;

	}
	
	/**
	 * ��ȡͬҵ���Ҹ���Ϣ
	 * @param fundVO
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private UFDouble getRedemption_interest(FundTallyVO fundVO, String begindate,
			String enddate) {
		String contractno = fundVO.getContractno();
		UFDouble Redemption_interest = new UFDouble(0);
		BaseDAO dao = new BaseDAO();
		StringBuffer sb = new StringBuffer();
		Vector vec = new Vector();
		sb.append("select sum(vdef2) from fund_interbanklending where nvl(dr,0)=0 and contractno = '");
		sb.append(contractno);
		sb.append("' and transtypecode = 'HV8A-Cxx-03'");
		if (enddate == null) {
			sb.append(" and trade_date <'" + begindate.substring(0, 10)
					+ " 00:00:00'");
		} else {
			sb.append(" and trade_date >='" + begindate.substring(0, 10)
					+ " 00:00:00'");
			sb.append(" and trade_date <='" + enddate.substring(0, 10)
					+ " 23:59:59");
		}
		try {
			vec = (Vector) dao.executeQuery(sb.toString(),
					new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			Redemption_interest = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return Redemption_interest;

	}

	/**
	 * ��ȡ̨��Ӧ����Ϣ
	 * @param fundVO
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private UFDouble getinterest_payable(FundTallyVO fundVO, String begindate,
			String enddate) {
		String contractno = fundVO.getContractno();
		UFDouble this_interest = new UFDouble(0);
		BaseDAO dao = new BaseDAO();
		StringBuffer sb = new StringBuffer();
		Vector vec = new Vector();
		sb.append("select sum(this_interest) from fund_fundtally where nvl(dr,0)=0 and contractno = '");
		sb.append(contractno);
		sb.append("'");
		if (enddate == null) {
			sb.append(" and trade_date <'" + begindate.substring(0, 10)
					+ " 00:00:00'");
		} else {
			sb.append(" and trade_date >='" + begindate.substring(0, 10)
					+ " 00:00:00'");
			sb.append(" and trade_date <='" + enddate.substring(0, 10)
					+ " 23:59:59");
		}
		try {
			vec = (Vector) dao.executeQuery(sb.toString(),
					new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			this_interest = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return this_interest;

	}

	/**
	 * ��ȡռ������
	 * 
	 * @author ZQ
	 * @param reportVO
	 * @param startdate
	 * @param enddate
	 * @return days
	 */
	private int getOccupiedDays(NewFundTallyReportVO reportVO,
			String startdate, String enddate) {
		int days = 0;
		UFDouble Start_balance = reportVO.getStart_balance();// �ڳ����
		UFDouble End_balance = reportVO.getEnd_balance();// ��ĩ���
		UFDate stdate = new UFDate(startdate.substring(0, 10));// ��ѯ��ʼ����
		UFDate endate = new UFDate(enddate.substring(0, 10));// ��ѯ��������
		UFDate bd = new UFDate(reportVO.getBegindate().toString()
				.substring(0, 10));// ���п�ʼ����
		UFDate ed = new UFDate(reportVO.getEnddate().toString()
				.substring(0, 10));// ���н�������
		if ((Start_balance == null || Start_balance.equals(new UFDouble(0)))
				&& !End_balance.equals(new UFDouble(0))) {
			days = endate.getDaysAfter(bd) + 1;
		} else if ((Start_balance == null || Start_balance.equals(new UFDouble(
				0)))
				&& (End_balance == null || End_balance.equals(new UFDouble(0)))) {
			days = ed.getDaysAfter(bd) + 1;
		} else if (!Start_balance.equals(new UFDouble(0))
				&& !End_balance.equals(new UFDouble(0))) {
			days = endate.getDaysAfter(stdate) + 1;
		} else if (!Start_balance.equals(new UFDouble(0))
				&& (End_balance == null || End_balance.equals(new UFDouble(0)))) {
			days = ed.getDaysAfter(stdate) + 1;
		}
		return days;

	}

	/**
	 * @author ZQ ��ȡ�վ�ռ�в����ʽ�ɱ��ʼ���
	 * @param reportVO
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	private UFDouble getdaily_possession(NewFundTallyReportVO reportVO,
			String startdate, String enddate) {
		UFDouble daily_poss = new UFDouble(0);
		UFDouble scale_fina = reportVO.getDenomination();
		int days = getOccupiedDays(reportVO, startdate, enddate);
		UFDouble Sigma = scale_fina.multiply(days);// ����
		UFDate startd = new UFDate(startdate);
		int daysnum = UFDate.getDaysBetween(startd, new UFDate(enddate)) + 1;
		if (UFDate.isLeapYear(startd.getYear() + 1)
				&& startd.compareTo(new UFDate(startd.getYear() + "-02-28")) > 0) {
			daysnum = 366;
		} else if (UFDate.isLeapYear(startd.getYear())
				&& startd.compareTo(new UFDate(startd.getYear() + "-02-28")) < 0) {
			daysnum = 366;
		} else {
			daysnum = 365;
		}
		daily_poss = Sigma.div(daysnum);
		return daily_poss;

	}

	/**
	 * @author ZQ ��ȡ��������֤ȯ������
	 * @param inventVO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private UFDouble getInterestrate(String pk_securities) {
		UFDouble rate = new UFDouble(0);
		BaseDAO dao = new BaseDAO();
		Vector vec = new Vector();
		String sql = "";
		sql = "select yearrate from sim_interest " + "where nvl(dr,0)=0 "
				+ "and pk_securities = '" + pk_securities + "'";
		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (vec.get(0) != null)
			rate = new UFDouble(((Vector) vec.get(0)).get(0).toString());

		return rate;

	}

	/**
	 * @author ZQ �ж����������Ƿ��ǳ������ʣ�trueΪ�ǣ�falseΪ����
	 * @param reportVO
	 * @return
	 */
	private boolean getLongfundtype(NewFundTallyReportVO reportVO) {
		boolean Longtype = false;
		String fundtype = reportVO.getFundtype();
		String[] typeshort = { "ͬҵ���", "ת��ͨ", "����Ȩת�ûع�" };
		String[] typelong = { "��������ȯ", "��������ƾ֤", "���ڴμ�ծ", "���ڹ�˾ծ", "���ڴμ�ծ",
				"��������ƾ֤", "���ڹ�˾ծ" };
		for (int i = 0; i < typeshort.length; i++) {
			if (fundtype == typeshort[i]) {
				break;
			}
		}
		for (int i = 0; i < typelong.length; i++) {
			if (fundtype == typelong[i]) {
				Longtype = true;
				break;
			}
		}
		return Longtype;

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
