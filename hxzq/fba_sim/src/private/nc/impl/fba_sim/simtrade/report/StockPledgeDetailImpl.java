package nc.impl.fba_sim.simtrade.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;
import nc.bs.dao.BaseDAO;
import nc.bs.fba_sim.simtrade.report.FundInOutUtils;
import nc.bs.fba_sim.simtrade.report.ReportSqlUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_sim.simtrade.report.IStockPledgeDetail;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.PubReportDataQuery;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_sabb.trade.ydsgh.AgreedbbVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.trademarket.TradeMarketVO;
import nc.vo.fba_sim.pub.SimINFOPubMethod;
import nc.vo.fba_sim.simbs.checkplan.CheckplanVO;
import nc.vo.fba_sim.simtrade.report.SecInvestDailyRepVO;
import nc.vo.fba_sim.simtrade.report.SecInvestDailyViewMeta;
import nc.vo.pub.BusinessException;
import nc.vo.pub.freereport.ReportDataVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

/**
 * 股票质押明细表实现
 * 
 * @author lihaibo 2017-03-20
 * 
 */
public class StockPledgeDetailImpl implements IStockPledgeDetail {

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
		queryResult.setDataSet(genSecInvestDataSet(context, conVos));
		return queryResult;
	}

	/**
	 * 查询期初期末和明细数据组装DataSet
	 * 
	 * @param context
	 * @param conVos
	 * @return
	 * @throws BusinessException
	 */
	private DataSet genSecInvestDataSet(IContext context, ConditionVO[] conVos)
			throws BusinessException {

		String dateValue = FundInOutUtils.getInstance().getDateCon(conVos);
		String org = FundInOutUtils.getInstance().getOrg(conVos);
		String glBook = FundInOutUtils.getInstance().getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);

		// update by lihaibo 解决期初值不能查最后一天问题
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY).asLocalEnd();
		String endDate = SimReportUtils.getEndDate(dateValue);

		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);
		List<CheckplanVO> checkPlanVOs = ReportSqlUtil.getInstance()
				.findCheckPlanVo(org, de_group, glBook);
		String condition = SimReportUtils.getQueryCondition(
				ReportConst.FUND_B_MARK, conVos, false, true);
		condition = condition.replace("and if_accrual='Y'", " ");
		
		/* 查明细并且按维度分组求和 */
		String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);

		List<StockBalanceVO> firBal = null;
		List<StockBalanceVO> endBal = null;
		List<SecInvestDailyRepVO> secLists = null;
		List<SecInvestDailyRepVO> secFirLists = null;
		PubReportDataQuery query = new PubReportDataQuery();
		try {
			//获取股票质押式购回发生数
			secLists = this.queryStockPledge(conVos);
		} catch (Exception e) {
			throw new BusinessException("查询股票质押式购回发生数出错！");
		}
		try {
			// 查询库存期初
			secFirLists = getFirst(conVos);
		} catch (BusinessException e) {
			throw new BusinessException("期初查询出错！");
		}

		List<SecInvestDailyRepVO> resultVOs = new ArrayList<SecInvestDailyRepVO>();
		PubMethod pm = PubMethod.getInstance();

		/**
		 * 获取利率、应计利息、应计利息余额、结息
		 */
		Map map = query.getFairValue(endDate.substring(0, 10) + " 00:00:00",
				costPlanVO, false);
//		while (iter.hasNext()) {
		if (secLists != null && secLists.size() > 0) {
		for (int i = 0; i < secLists.size(); i++) {
			
			SecInvestDailyRepVO repVo = secLists.get(i);
			String contract_id = repVo.getPk_operatesite();//合同编号
			repVo.setTrade_date(new UFDate(endDate.substring(0, 10)));
			if (secFirLists != null && secFirLists.size() > 0) {
				for (int j = 0; j<secFirLists.size(); j++) {
					if (secFirLists.get(j).getPk_operatesite().equals(contract_id)) {
						repVo.setFirstNum(secFirLists.get(j).getFirstNum());
						repVo.setFirstSum(secFirLists.get(j).getFirstSum());
						break;
					} else {
						repVo.setFirstNum(UFDouble.ZERO_DBL);
						repVo.setFirstSum(UFDouble.ZERO_DBL);
					}
				}
			}
			
			ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
			//查利率 -- 若每日利息里面没有满足查询条件的数据，就默认去股票质押式回购里的期初交易利率，若有取交易日期最新利率
			String lvsql = "select a.rate as sellCost\n" +
							"  from sim_dailyinterest a, sec_securities sec\n" + 
							" where a.pk_securities = sec.pk_securities\n" + 
							"   and a.state in (2, 3)\n" + 
							"   and nvl(a.dr, 0) = 0\n"+
							"   and a.contract_id = '"+contract_id+"' and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ endDate+"' \n";
			lvsql = lvsql + "   order by a.trade_date desc";
			List<ReportDataVO> reportVos1 = (List<ReportDataVO>) getService()
					.executeQuery(lvsql, new BeanListProcessor(
							ReportDataVO.class));
			if (reportVos1 != null && reportVos1.size() > 0) {
				repVo.setSellCost(reportVos1.get(0).getSellcost() == null ? UFDouble.ZERO_DBL : reportVos1.get(0).getSellcost());
			} else {
				String lvsql_v = "select a.rate as sellCost\n" +
						"  from sim_impawnrepo a, sec_securities sec\n" + 
						" where a.pk_securities = sec.pk_securities\n" + 
						"   and a.state in (2, 3)\n" + 
						"   and nvl(a.dr, 0) = 0\n"+
						"   and a.contract_id = '"+contract_id+"' "+
						"   and a.transtypecode = 'HV5B-0xx-01' and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' ";
				lvsql = lvsql + "   order by a.trade_date desc";
				List<ReportDataVO> reportVos_v = (List<ReportDataVO>) getService()
						.executeQuery(lvsql_v, new BeanListProcessor(
								ReportDataVO.class));
				if (reportVos_v != null && reportVos_v.size() > 0) {
					repVo.setSellCost(reportVos_v.get(0).getSellcost() == null ? UFDouble.ZERO_DBL : reportVos_v.get(0).getSellcost());
				}
			}
			//查应计利息 -- 每日利息表里面的本日计提利息字段的和
			String yjlxsql =	"select sum(t.localwin) as localwin from ("+ 
							" select sum(a.brjtlx) as localwin\n" +
							"  from sim_dailyinterest a, sec_securities sec\n" + 
							" where a.pk_securities = sec.pk_securities\n" + 
							"   and nvl(a.dr, 0) = 0" +
							"   and a.contract_id = '"+contract_id+"' ";
			yjlxsql = reportUtil.getQuerySql(condition, yjlxsql);
			yjlxsql = yjlxsql + " union select -sum(a.vdef4) as localwin\n" +
					"  from sim_impawnrepo a, sec_securities sec\n" + 
					" where a.pk_securities = sec.pk_securities\n" + 
					"   and nvl(a.dr, 0) = 0" +
					"   and a.vdef4 <> '~'" +
					"   and a.contract_id = '"+contract_id+"' ";
			yjlxsql = reportUtil.getQuerySql(condition, yjlxsql);
			yjlxsql = yjlxsql + " ) t";
			List<SecInvestDailyRepVO> reportVos2 = (List<SecInvestDailyRepVO>) getService()
					.executeQuery(yjlxsql, new BeanListProcessor(
							SecInvestDailyRepVO.class));
			if (reportVos2 != null && reportVos2.size() > 0) {
				repVo.setLocalwin(reportVos2.get(0).getLocalwin() == null ? UFDouble.ZERO_DBL : reportVos2.get(0).getLocalwin());
			}
			//查期初应计利息 -- 每日利息表里面的本日计提利息字段的和
			String qcyjlxsql = "select sum(t.firstPrice) as firstPrice from ("+
							" select sum(a.brjtlx) as firstPrice\n" +
							"  from sim_dailyinterest a, sec_securities sec\n" + 
							" where a.pk_securities = sec.pk_securities\n" + 
							"   and nvl(a.dr, 0) = 0" +
							"   and a.contract_id = '"+contract_id+"' and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate+"' \n"+
							" union \n" +
							" select -sum(a.vdef4) as firstPrice\n" +
							"  from sim_impawnrepo a, sec_securities sec\n" + 
							" where a.pk_securities = sec.pk_securities\n" + 
							"   and nvl(a.dr, 0) = 0" +
							"   and a.vdef4 <> '~'" +
							"   and a.contract_id = '"+contract_id+"' and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate+"' \n"+
							" union \n" +
							" select -sum(a.settleinterest) as firstPrice\n" +
							"  from sim_impawnrepo a, sec_securities sec\n" + 
							" where a.pk_securities = sec.pk_securities\n" + 
							"   and nvl(a.dr, 0) = 0" +
							"   and a.contract_id = '"+contract_id+"' and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate+"' ) t";
			List<SecInvestDailyRepVO> reportVos3 = (List<SecInvestDailyRepVO>) getService()
					.executeQuery(qcyjlxsql, new BeanListProcessor(
							SecInvestDailyRepVO.class));
			if (reportVos3 != null && reportVos3.size() > 0) {
				repVo.setFirstPrice(reportVos3.get(0).getFirstPrice() == null ? UFDouble.ZERO_DBL : reportVos3.get(0).getFirstPrice());
			}
			//查结息 -- 股票质押式购回表里面的收到利息字段的和
			String jxsql = "select sum(a.settleinterest) as gyz\n" +
							"  from sim_impawnrepo a, sec_securities sec\n" + 
							" where a.pk_securities = sec.pk_securities\n" + 
							"   and nvl(a.dr, 0) = 0" +
							"   and a.contract_id = '"+contract_id+"' ";
			jxsql = reportUtil.getQuerySql(condition, jxsql);
			List<SecInvestDailyRepVO> reportVos4 = (List<SecInvestDailyRepVO>) getService()
					.executeQuery(jxsql, new BeanListProcessor(
							SecInvestDailyRepVO.class));
			if (reportVos4 != null && reportVos4.size() > 0) {
				repVo.setGyz(reportVos4.get(0).getGyz() == null ? UFDouble.ZERO_DBL : reportVos4.get(0).getGyz());
			}
			
			resultVOs.add(repVo);
			} 
		}
		//将期初为0，并且发生数为0的去掉
		if (resultVOs != null && resultVOs.size() > 0) {
			for (int i = 0; i < resultVOs.size(); i++) {
				SecInvestDailyRepVO sec = resultVOs.get(i);
				UFDouble firstsum = sec.getFirstSum() == null ? UFDouble.ZERO_DBL : sec.getFirstSum();
				UFDouble buysum = sec.getBuySum() == null ? UFDouble.ZERO_DBL : sec.getBuySum();
				UFDouble sellsum = sec.getSellSum() == null ? UFDouble.ZERO_DBL : sec.getSellSum();
				if (firstsum.toDouble() == 0 && buysum.toDouble() == 0 && sellsum.toDouble() == 0) {
					resultVOs.remove(i);
				}
			}
		}

		return PubMethod.getInstance().convertVOToArray(getMetaData(),
				resultVOs);
	}




	private MetaData getMetaData() {
		List<Field> list = new ArrayList<Field>();

		for (String key : SecInvestDailyViewMeta.FUNDTRADE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		for (String key : SecInvestDailyViewMeta.EXT_D_FIELD) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}

		for (String key : SecInvestDailyViewMeta.EXT_DATE_FIELD) {
			Field field = ReportDataUtil.createDateFiled(key);
			list.add(field);
		}

		Field[] fields = new Field[list.size()];
		list.toArray(fields);
		return new MetaData(fields);
	}

	/**
	 * 查询股票质押式回购发生数
	 * @param conVos
	 * @throws BusinessException
	 */
	private List<SecInvestDailyRepVO> queryStockPledge(ConditionVO[] conVos) throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		// 查询库存期初
		List<StockBalanceVO> sbvs = new ArrayList<StockBalanceVO>();

		List<AgreedbbVO> agreedbbs = null;
		String dateValue = FundInOutUtils.getInstance().getDateCon(conVos);
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY).asLocalEnd();
		String endDate = SimReportUtils.getEndDate(dateValue);
		ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
		String condition = SimReportUtils.getQueryCondition(
				ReportConst.FUND_B_MARK, conVos, false, true);
		String querySql = 
				"select temp.pk_operatesite,\n" +
						"       temp.pk_partnaccount,\n" + 
						"       temp.pk_securities,\n" + 
						"       sum(temp.buysum) as buysum,\n" + 
						"       sum(temp.buynum) as buynum,\n" + 
						"       sum(temp.sellsum) as sellsum,\n" + 
						"       sum(temp.sellnum) as sellnum,\n" + 
						"       temp.pk_org,\n" + 
						"       temp.pk_group,\n" + 
						"       temp.pk_glorgbook\n" + 
						"  from (select a.contract_id   as pk_operatesite,\n" + 
						"               a.vdef2         as pk_partnaccount,\n" + 
						"               a.pk_securities,\n" + 
						"               a.transtypecode as pk_billtype,\n" + 
						"               a.entrust_sum   as buysum,\n" + 
						"               a.entrust_num   as buynum,\n" + 
						"               0               as sellsum,\n" + 
						"               0               as sellnum,\n" + 
						"               a.pk_org,\n" + 
						"               a.pk_group,\n" + 
						"               a.pk_glorgbook,\n" + 
						"               a.trade_date\n" + 
						"          from sim_impawnrepo a\n" + 
						"          left join sec_securities sec\n" + 
						"            on a.pk_securities = sec.pk_securities\n" + 
						"         where a.state in (2, 3)\n" + 
						"           and nvl(a.dr, 0) = 0\n" + 
						"           and a.transtypecode = 'HV5B-0xx-01'\n"; 
						querySql = reportUtil.getQuerySql(condition, querySql);
						querySql += "\n" +
//						"           and a.trade_date >= '2017-03-27 00:00:00'\n" + 
//						"           and a.trade_date <= '2017-03-27 23:59:59'\n" + 
						"        union\n" + 
						"        select a.contract_id   as pk_operatesite,\n" + 
						"               a.vdef2         as pk_partnaccount,\n" + 
						"               a.pk_securities,\n" + 
						"               a.transtypecode as pk_billtype,\n" + 
						"               0               as buysum,\n" + 
						"               0               as buynum,\n" + 
						"               a.entrust_sum   as sellsum,\n" + 
						"               a.entrust_num   as sellnum,\n" + 
						"               a.pk_org,\n" + 
						"               a.pk_group,\n" + 
						"               a.pk_glorgbook,\n" + 
						"               a.trade_date\n" + 
						"          from sim_impawnrepo a, sec_securities sec\n" + 
						"         where a.pk_securities = sec.pk_securities\n" + 
						"           and a.state in (2, 3)\n" + 
						"           and nvl(a.dr, 0) = 0\n" + 
						"           and a.transtypecode = 'HV5B-0xx-03'\n";
						querySql = reportUtil.getQuerySql(condition, querySql);
						querySql += "\n" +
//						"           and a.trade_date >= '2017-03-27 00:00:00'\n" + 
//						"           and a.trade_date <= '2017-03-27 23:59:59'\n" + 
						"        union\n" + 
						"        select a.contract_id   as pk_operatesite,\n" + 
						"               a.vdef2         as pk_partnaccount,\n" + 
						"               a.pk_securities,\n" + 
						"               a.transtypecode as pk_billtype,\n" + 
						"               0               as buysum,\n" + 
						"               0               as buynum,\n" + 
						"               0               as sellsum,\n" + 
						"               0               as sellnum,\n" + 
						"               a.pk_org,\n" + 
						"               a.pk_group,\n" + 
						"               a.pk_glorgbook,\n" + 
						"               a.trade_date\n" + 
						"          from sim_impawnrepo a\n" + 
						"          left join sec_securities sec\n" + 
						"            on a.pk_securities = sec.pk_securities\n" + 
						"         where a.state in (2, 3)\n" + 
						"           and nvl(a.dr, 0) = 0\n" + 
						"           and a.transtypecode = 'HV5B-0xx-01'\n and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate + "' \n" +
						"        union\n" + 
						"        select a.contract_id   as pk_operatesite,\n" + 
						"               a.vdef2         as pk_partnaccount,\n" + 
						"               a.pk_securities,\n" + 
						"               a.transtypecode as pk_billtype,\n" + 
						"               0               as buysum,\n" + 
						"               0               as buynum,\n" + 
						"               0               as sellsum,\n" + 
						"               0               as sellnum,\n" + 
						"               a.pk_org,\n" + 
						"               a.pk_group,\n" + 
						"               a.pk_glorgbook,\n" + 
						"               a.trade_date\n" + 
						"          from sim_impawnrepo a, sec_securities sec\n" + 
						"         where a.pk_securities = sec.pk_securities\n" + 
						"           and a.state in (2, 3)\n" + 
						"           and nvl(a.dr, 0) = 0\n" + 
						"           and a.transtypecode = 'HV5B-0xx-03'\n and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate + "' " +
						"  ) temp\n" + 
						" group by temp.pk_operatesite,\n" + 
						"          temp.pk_partnaccount,\n" + 
						"          temp.pk_securities,\n" + 
						"          temp.pk_group,\n" + 
						"          temp.pk_org,\n" + 
						"          temp.pk_glorgbook";
						querySql = querySql.replace("and if_accrual='Y'", " ");
		List<SecInvestDailyRepVO> secVos = (List<SecInvestDailyRepVO>) getService()
								.executeQuery(querySql, new BeanListProcessor(
										SecInvestDailyRepVO.class));
		return secVos;
	}
	
	/**
	 * 查询期初
	 * @param conVos
	 * @throws BusinessException
	 */
	private List<SecInvestDailyRepVO> getFirst(ConditionVO[] conVos) throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		// 查询库存期初
		List<StockBalanceVO> sbvs = new ArrayList<StockBalanceVO>();

		List<AgreedbbVO> agreedbbs = null;
		String dateValue = FundInOutUtils.getInstance().getDateCon(conVos);
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY).asLocalEnd();
		String endDate = SimReportUtils.getEndDate(dateValue);
		ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
		String querySql = 
				"select temp.pk_operatesite,\n" +
						"       temp.pk_partnaccount,\n" + 
						"       temp.pk_securities,\n" + 
						"       sum(temp.firstSum) as firstsum,\n" + 
						"       sum(temp.firstNum) as firstnum\n" + 
						"  from (select a.contract_id as pk_operatesite,\n" + 
						"               a.vdef2 as pk_partnaccount,\n" + 
						"               a.pk_securities,\n" + 
						"               sum(entrust_sum) as firstSum,\n" + 
						"               sum(entrust_num) as firstNum\n" + 
						"          from sim_impawnrepo a\n" + 
						"          left join sec_securities sec\n" + 
						"            on a.pk_securities = sec.pk_securities\n" + 
						"         where a.state in (2, 3)\n" + 
						"           and nvl(a.dr, 0) = 0\n" + 
						"           and a.transtypecode = 'HV5B-0xx-01'\n and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate + "' \n" +
						"         group by a.contract_id, a.vdef2, a.pk_securities\n" + 
						"        union\n" + 
						"        select a.contract_id as pk_operatesite,\n" + 
						"               a.vdef2 as pk_partnaccount,\n" + 
						"               a.pk_securities,\n" + 
						"               -sum(entrust_sum) as firstSum,\n" + 
						"               -sum(entrust_num) as firstNum\n" + 
						"          from sim_impawnrepo a\n" + 
						"          left join sec_securities sec\n" + 
						"            on a.pk_securities = sec.pk_securities\n" + 
						"         where a.state in (2, 3)\n" + 
						"           and nvl(a.dr, 0) = 0\n" + 
						"           and a.transtypecode = 'HV5B-0xx-03'\n and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate + "' \n" +
						"         group by a.contract_id, a.vdef2, a.pk_securities) temp\n" + 
						" group by temp.pk_operatesite, temp.pk_partnaccount, temp.pk_securities";
		List<SecInvestDailyRepVO> secVos = (List<SecInvestDailyRepVO>) getService()
				.executeQuery(querySql, new BeanListProcessor(
						SecInvestDailyRepVO.class));
		return secVos;
	}
	
	private IUAPQueryBS getService() {
		IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		return service;
	}

}
