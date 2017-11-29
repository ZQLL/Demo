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
 * 筹资管理台账
 * 
 * @author qs
 * @since 2017-6-16上午9:12:23
 */
public class NewFundTallyReportImpl implements NewIFundTallyReport {
	PubMethod pm = PubMethod.getInstance();

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		// // 查询条件
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
	 * 通过查询条件获取查询结果
	 * 
	 * @author qs
	 * @throws BusinessException
	 * @since 2017-6-16上午9:29:12
	 */
	private DataSet genFundTallyReportDataSet(IContext context,
			ConditionVO[] conVos) throws BusinessException {
		// 查询筹资管理台账表数据
		List<FundTallyVO> fundTallyVOs;
		try {
			fundTallyVOs = this.queryFundTallyInfo(conVos);
		} catch (DAOException e) {
			throw new BusinessException("查询出错！");
		}
		List<NewFundTallyReportVO> result = null;
		// 判空，查询结果有值才进行后续处理
		if (null != fundTallyVOs) {
			// 处理数据
			result = this.processData(fundTallyVOs, conVos);
		}
		return PubMethod.getInstance().convertVOToArraynew(getMetaData(), result);
	}

	/**
	 * 查询筹资管理库存表数据
	 * 
	 * @author qs
	 * @throws DAOException
	 * @since 2017-6-16上午9:52:00
	 */
	@SuppressWarnings("unchecked")
	private List<FundTallyVO> queryFundTallyInfo(ConditionVO[] conVos)
			throws DAOException {
		// 获得查询条件sql
		String querySql = this.getQueryCondition(conVos);
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from fund_fundtally t where");
		sql.append(" isnull(dr,0)=0 ");
		// sql.append(" and t.denomination>0 ");
		// sql.append(" and t.isdq='N' ");
		// sql.append(" and t.operindex  <> 0 ");
		sql.append("and (t.operindex  <> 0 or (t.operindex = 0 and t.isdq='N')) ");
		// 拼接上查询条件
		if (StringUtils.isNotBlank(querySql)) {
			sql.append(" and ");
			sql.append(querySql);
		}
		sql.append("order by t.operindex asc");
		return (List<FundTallyVO>) new BaseDAO().executeQuery(sql.toString(),
				new BeanListProcessor(FundTallyVO.class));
	}

	/**
	 * 根据查询条件生成sql
	 * 
	 * @author qs
	 * @return
	 * @since 2017-6-16下午3:11:15
	 */
	private String getQueryCondition(ConditionVO[] conVos) {
		StringBuilder sql = new StringBuilder();
		if (null != conVos) {
			for (int i = 0; i < conVos.length; i++) {
				// 查询期间：与台账表计提日期比较
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
	 * 根据查询出的库存数据，转换成报表显示结果
	 * 
	 * @author qs
	 * @return
	 * @throws BusinessException
	 * @since 2017-6-16上午9:57:46
	 */
	private List<NewFundTallyReportVO> processData(List<FundTallyVO> vos,
			ConditionVO[] conVos) throws BusinessException {
		List<NewFundTallyReportVO> result = new ArrayList<NewFundTallyReportVO>();
		// 台账数据缓存：key-合同号 ，value-此合同号的台账数据
		HashMap<String, List<FundTallyVO>> map = new HashMap<String, List<FundTallyVO>>();
		for (FundTallyVO vo : vos) {
			// 合同号
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
			// 构建报表显示数据
			reportVO = this.buildReportVO(tallylist, conVos);
			result.add(reportVO);
		}
		return result;

	}

	/**
	 * 构建报表显示数据
	 * 
	 * @author qs
	 * @throws BusinessException
	 * @throws DAOException
	 * @since 2017-6-22下午6:48:04
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
		boolean b = false;// 判断单子是否是长期融资类型
		if (null != conVos) {
			// 获取查询条件中查询期间的起始日和结束日（此条件用的台账表中的计提开始日期begindistill_date）
			for (int i = 0; i < conVos.length; i++) {
				// 计提开始日期
				if (conVos[i].getFieldCode().equals("begindistill_date")) {
					// 起始日
					startDate = SimReportUtils.getStartDate(conVos[i]
							.getValue());
					// 结束日
					endDate = SimReportUtils.getEndDate(conVos[i].getValue());
				}
			}
		}
		UFDate startdate = null;
		UFDate enddate = null;
		UFDouble pay_balance = new UFDouble(0);
		try {
			// 查询到期数据
			List<FundTallyVO> list = this.queryDqTallyInfo(enddate, tallylist
					.get(0).getContractno());
			for (int i = 0; i < list.size(); i++) {
				pay_balance = pm
						.add(pay_balance, list.get(i).getDenomination());
			}

		} catch (DAOException e) {
			throw new BusinessException("查询出错！");
		}

		for (FundTallyVO vo : tallylist) {
			if (null != startDate) {
				// 将startDate转换成UFDate
				startdate = new UFDate(startDate);
			}
			// 如果查询开始日当天有数据，则期初应付利息余额有值
			if (vo.getBegindistill_date().equals(startdate)) {
				// 【期初应付利息余额】=期初的已计提利息
				start_interest = vo.getHad_interest();
			}
			// 【本期应付利息】
			if (null != vo.getThis_interest()) {
				// 累加
				this_interest = pm.add(this_interest, vo.getThis_interest());
			}
		}
		
		if (null != startDate) {
			// 将startDate转换成UFDate
			startdate = new UFDate(startDate);
			// 将endDate转换成UFDate
			enddate = new UFDate(endDate);
			// 如果计提日期=查询起始日，则期初余额有值，否则为本期发行
			if (tallylist.get(0).getBegindistill_date().asBegin()
					.equals(startdate)) {
				// 【期初余额/摊余成本】
				reportVO.setStart_balance(tallylist.get(0).getDenomination());
			} else {
				// 【本期发行】
				if (inbaleVO != null)
					reportVO.setCur_issue(tallylist.get(0).getDenomination()
							.sub(new UFDouble(inbaleVO.getVdef2())));
			}
			// 如果查询条件的查询期间有值，则将begindistill_date设置值，否则最终界面查询结果
			reportVO.setBegindistill_date(startDate);
		} else {
			// 【期初余额/摊余成本】=本期发行-开始日期前所有归还金+所有摊销(即开始日期前所有利息支出-应付利息)
			reportVO.setStart_balance(tallylist.get(0).getDenomination()
					.sub(new UFDouble(inbaleVO.getVdef2()))
					.sub(getpay_balance(fundVO, startDate, null))
					.add(getcur_expend(fundVO, startDate, null))
					.sub(getinterest_payable(fundVO, startDate, null)));
		}
		// 【期末余额/摊余成本】
		reportVO.setEnd_balance(tallylist.get(tallylist.size() - 1)
				.getDenomination());
		// 起息日
		reportVO.setBegindate(tallylist.get(0).getBegindate());
		// 到期日
		reportVO.setEnddate(tallylist.get(0).getEnddate());
		// 【兑付本金】
		reportVO.setPay_balance(getpay_balance(fundVO, startDate, endDate));
		// 业务分类
		reportVO.setPk_billtype(tallylist.get(0).getPk_billtype());
		// 产品或对手方名称
		reportVO.setProductorcounterparty(tallylist.get(0)
				.getProductorcounterparty());
		// 管理部门
		reportVO.setManagedept(tallylist.get(0).getManagedept());
		// 结存金额=面额
		reportVO.setDenomination(tallylist.get(0).getDenomination());
		// 期限
		reportVO.setTimelimit(tallylist.get(0).getTimelimit());
		//如果开始计提才存在利息情况
		if(tallylist.size()>1){
			// 【期初应付利息余额】
			reportVO.setStart_payableinterest(start_interest);
			// 【本期应付利息】
			reportVO.setCur_payableinterest(this_interest);
			// 【本期利息支出金额】=本期应付利息（没有实际利率情况下）
			reportVO.setCur_expend(getcur_expend(fundVO, startDate, enddate.toString()));
			//本期摊销值 --zq = 本期利息支出-本期应付利息
			if(reportVO.getCur_expend()!=null && reportVO.getCur_payableinterest()!=null){
				reportVO.setCur_amortize(pm.sub(reportVO.getCur_expend(), reportVO.getCur_payableinterest()));
			}
			// 【本期兑付利息】
			reportVO.setCur_payinterest(getRedemption_interest(fundVO,startDate, endDate));
			// 【期初应付利息余额】--zq 期初前所有利息支出-所有兑付利息
			//reportVO.setStart_payableinterest(getcur_expend(fundVO, startDate, null).sub(getRedemption_interest(fundVO, startDate, null)));
			// 【期末应付利息余额】=期初应付利息余额+本期应付利息-本期兑付--zq
			reportVO.setEnd_payableinterest(reportVO.getStart_payableinterest().add(reportVO.getCur_expend()).sub(reportVO.getCur_payableinterest()));
			// 日均占有--zq
			reportVO.setDaily_possession(getdaily_possession(reportVO, startDate,
					endDate));
			// 资金成本率 --zq
			if (reportVO.getCur_expend() != null
					&& reportVO.getDaily_possession() != null) {
				if (!reportVO.getDaily_possession().equals(new UFDouble(0)))
					reportVO.setCostrate_capital(reportVO.getCur_expend().div(
							reportVO.getDaily_possession()));
			}
		}
		// 如果期初期末面额不等，说明存在兑付或到期
		if (!(tallylist.get(0).getDenomination().equals(tallylist.get(
				tallylist.size() - 1).getDenomination()))) {
			// 【兑付本金】
			reportVO.setPay_balance(getpay_balance(fundVO, startDate, endDate));
			//reportVO.setPay_balance(pay_balance);
			// 【本期兑付利息】
			reportVO.setCur_payinterest(getRedemption_interest(fundVO,startDate, endDate));
		}
		if (null != tallylist.get(0).getPaytype()) {
			// 付息方式
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
			// 融资类型
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
		//判断融资类型--zq
		if (b) {
			// 执行票面利率
			if (!pk_securities.equals("")) {
				reportVO.setExcutefacerate(getInterestrate(pk_securities));
				// 实际利率=执行票面利率*实际利率比例
				if (reportVO.getExcutefacerate() != null
						&& tallylist.get(0).getRealrate() != null)
					reportVO.setRealrate(reportVO.getExcutefacerate().multiply(
							tallylist.get(0).getRealrate()));
			}
		} else {
			// 执行票面利率
			reportVO.setExcutefacerate(tallylist.get(0).getExcutefacerate());
			// 实际利率=执行票面利率*实际利率比例
			if (tallylist.get(0).getExcutefacerate() != null
					&& tallylist.get(0).getRealrate() != null)
				reportVO.setRealrate(tallylist.get(0).getExcutefacerate()
						.multiply(tallylist.get(0).getRealrate()));
		}

		// 交易日期
		reportVO.setTrade_date(tallylist.get(0).getTrade_date());
		// 组织
		reportVO.setPk_org(tallylist.get(0).getPk_org());
		return reportVO;
	}

	/**
	 * @author ZQ 查询出该台账对应的同业拆借
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (list.size() > 0 && list != null)
			inventVO = list.get(list.size()-1);
		return inventVO;
	}

	/**
	 * 期初期末归还本金 考虑到按期归还的情况，计算期初余额的时候要减去归还本金,期末加上本期归还
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			pay_balance = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return pay_balance;

	}
	
	/**
	 * 获取利息支出
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			cur_expend = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return cur_expend;

	}
	
	/**
	 * 获取同业拆借兑付利息
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			Redemption_interest = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return Redemption_interest;

	}

	/**
	 * 获取台账应付利息
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (vec.size() > 0 && vec.get(0) != null&&((Vector) vec.get(0)).get(0)!=null)
			this_interest = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return this_interest;

	}

	/**
	 * 获取占用天数
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
		UFDouble Start_balance = reportVO.getStart_balance();// 期初余额
		UFDouble End_balance = reportVO.getEnd_balance();// 期末余额
		UFDate stdate = new UFDate(startdate.substring(0, 10));// 查询开始日期
		UFDate endate = new UFDate(enddate.substring(0, 10));// 查询结束日期
		UFDate bd = new UFDate(reportVO.getBegindate().toString()
				.substring(0, 10));// 发行开始日期
		UFDate ed = new UFDate(reportVO.getEnddate().toString()
				.substring(0, 10));// 发行结束日期
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
	 * @author ZQ 获取日均占有参与资金成本率计算
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
		UFDouble Sigma = scale_fina.multiply(days);// 积数
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
	 * @author ZQ 获取长期融资证券的利率
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (vec.get(0) != null)
			rate = new UFDouble(((Vector) vec.get(0)).get(0).toString());

		return rate;

	}

	/**
	 * @author ZQ 判断融资类型是否是长期融资，true为是，false为短期
	 * @param reportVO
	 * @return
	 */
	private boolean getLongfundtype(NewFundTallyReportVO reportVO) {
		boolean Longtype = false;
		String fundtype = reportVO.getFundtype();
		String[] typeshort = { "同业拆借", "转融通", "收益权转让回购" };
		String[] typelong = { "短期融资券", "短期收益凭证", "短期次级债", "短期公司债", "长期次级债",
				"长期收益凭证", "长期公司债" };
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
	 * 查询本期兑付或到期信息
	 * 
	 * @author qs
	 * @since 2017-6-26下午3:53:09
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
	 * 获取要显示的字段
	 * 
	 * @author qs
	 * @since 2017-6-16上午9:20:38
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
