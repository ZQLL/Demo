package nc.pub.freereport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.fairvaluedistill.Sim_fairvaluedistillVO;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.CostConstant.FAIR_GENSTATE;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_smt.trade.margintrade.MarginTradeVO;
import nc.vo.fba_smt.trade.refinancing.RefinancingVO;
import nc.vo.fba_smt.trade.transferrecord.TransferRecordVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

public class PubReportDataQuery {
	private static IUAPQueryBS queryservice;

	private IUAPQueryBS getQueryservice() {
		if (queryservice == null) {
			queryservice = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return queryservice;
	}

	public List<StockBalanceVO> getStockBalance(String condition,
			String tradeDate, CostPlanVO costplanvo) throws BusinessException {
		String pk_org = costplanvo.getPk_org();
		String pk_glorgbook = costplanvo.getPk_glorgbook();

		StringBuffer manItemSb = new StringBuffer();
		StringBuffer manjoinSb = new StringBuffer();
		StringBuffer sqlsb = new StringBuffer();
		String[] manItemCodes = (String[]) null;

		manItemCodes = SimReportUtils.getManFieldArray(costplanvo);
		for (int i = 0; i < manItemCodes.length; i++) {
			manjoinSb.append(" and isnull(x.").append(manItemCodes[i])
					.append(",'").append(manItemCodes[i])
					.append("') = isnull(y.").append(manItemCodes[i])
					.append(",'").append(manItemCodes[i]).append("')");
		}
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append("a." + manItemCodes[i]);
			manItemSb.append(",");
		}
		sqlsb.append("select /*+ no_merge(x) leading(x) */ y.* from (select max(a.trade_date) trade_date,");
		sqlsb.append(manItemSb.toString());
		sqlsb.append("a.pk_costplan");
		sqlsb.append(" from sim_stockbalance a inner join ");
		sqlsb.append("sec_securities b on a.pk_securities=b.pk_securities ");

		sqlsb.append("where a.trade_date < '" + tradeDate
				+ "' and isnull(a.dr,0)=0 ");
		if (pk_org != null) {
			if (!"@@@@".equals(pk_org)) {
				sqlsb.append("and a.pk_org='" + pk_org + "' ");
				sqlsb.append(" and a.pk_costplan='");
				sqlsb.append(costplanvo.getPk_costplan()).append("' ");
			} else {
				sqlsb.append(" and a.pk_costplan in (select pk_costplan from sim_costplan where dr=0 and isbusinessplan ='Y' ) ");
			}
		}
		sqlsb.append("and a.pk_glorgbook='" + pk_glorgbook + "' ");
		if ((condition != null) && (!"".equals(condition.trim()))) {
			sqlsb.append(condition);
		}
		sqlsb.append("group by ");
		sqlsb.append(manItemSb.toString());
		sqlsb.append("a.pk_costplan) x ");
		sqlsb.append("inner join sim_stockbalance y on x.trade_date = y.trade_date");
		sqlsb.append(manjoinSb.toString());
		sqlsb.append(" and x.pk_costplan = y.pk_costplan and isnull(y.dr,0)=0 where y.stocks_num > 0  order by y.end_date");
		return (List) getQueryservice().executeQuery(sqlsb.toString(),
				new BeanListProcessor(StockBalanceVO.class));
	}

	public Map<String, UFDouble> getClosePrice(String distill_date) {
		Map<String, UFDouble> ht = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.pk_securities, a.close_price from sim_trademarket a,");
		sql.append("(select max(trade_date) trade_date,pk_securities from sim_trademarket ");
		sql.append("where pk_securities in (select pk_securities from sec_securities ");
		sql.append("where if_fairdistill='Y') and isnull(dr,0)=0");
		sql.append(" and close_price > 0 and trade_date<='");
		sql.append(distill_date);
		sql.append("' group by pk_securities) b where isnull(a.dr,0)=0 and ");
		sql.append("a.trade_date=b.trade_date and a.pk_securities=b.pk_securities ");
		try {
			ArrayList<ArrayList<Object>> listArr = (ArrayList) queryservice
					.executeQuery(sql.toString(), new BaseProcessor() {
						private static final long serialVersionUID = 1L;

						public Object processResultSet(ResultSet rs)
								throws SQLException {
							ArrayList<ArrayList<Object>> listArr = new ArrayList();
							while (rs.next()) {
								int index = 1;
								ArrayList<Object> rowArr = new ArrayList();
								while (index <= 2) {
									rowArr.add(rs.getObject(index++));
								}
								listArr.add(rowArr);
							}
							return listArr;
						}
					});
			if ((listArr == null) || (listArr.isEmpty())) {
				return ht;
			}
			for (int i = 0; i < listArr.size(); i++) {
				List<Object> rowArr = (ArrayList) listArr.get(i);
				if ((rowArr != null) && (!rowArr.isEmpty())) {
					String pk_securities = (String) rowArr.get(0);
					Object close_price = rowArr.get(1);
					ht.put(pk_securities, new UFDouble(close_price.toString()));
				}
			}
		} catch (Exception e) {
			Logger.debug(e.getMessage());
		}
		return ht;
	}

	public Map getFairValue(String tradedate, CostPlanVO costplanvo,
			boolean ifaccrual) throws BusinessException {
		String pk_org = costplanvo.getPk_org();
		String pk_group = costplanvo.getPk_group();
		String pk_glorgbook = costplanvo.getPk_glorgbook();

		UFDate date = new UFDate(CostConstant.DEFAULT_DATE);
		StringBuffer sf = new StringBuffer();
		sf.append(" select max(t.distill_date)  distill_date from sim_fairvaluedistill t ");
		sf.append("  where t.pk_org = '" + pk_org + "'    ");
		sf.append("   and t.pk_group = '" + pk_group
				+ "'  and isnull(dr,0) = 0 ");
		sf.append("   and t.genstate = "
				+ CostConstant.FAIR_GENSTATE.JITI.getIndex() + "   ");
		sf.append("   and t.pk_glorgbook = '"
				+ AppContextUtil.getDefaultOrgBook() + "' ");
		sf.append("   and t.distill_date <= '" + new UFDate(tradedate).asEnd()
				+ "' ");
		date = (UFDate) getQueryservice().executeQuery(sf.toString(),
				new ResultSetProcessor() {
					private static final long serialVersionUID = 1L;

					public Object handleResultSet(ResultSet rs)
							throws SQLException {
						UFDate date = new UFDate(CostConstant.DEFAULT_DATE);
						if (rs.next()) {
							String distill_date = rs.getString("distill_date");
							date = distill_date == null ? new UFDate(
									CostConstant.DEFAULT_DATE) : new UFDate(
									distill_date);
						}
						return date;
					}
				});
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer manSelectkey = new StringBuffer();
		String[] manItemCodes = SimReportUtils.getManFieldArray(costplanvo);
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" select sum(case genstate when 2 then fair_balance when 3 then fair_balance else fair_balance * -1 end ) fair_balance ,");
		sb.append(manItemSb.toString());
		sb.append(" from sim_fairvaluedistill  where isnull(dr,0)=0 ");
		if (pk_org != null) {
			if (!"@@@@".equals(pk_org)) {
				sb.append(" and pk_org='" + pk_org + "' ");
				sb.append(" and pk_costplan='");
				sb.append(costplanvo.getPk_costplan()).append("' ");
			} else {
				sb.append(" and pk_costplan in (select pk_costplan from sim_costplan where isnull(dr,0)=0 and isbusinessplan ='y' ) ");
			}
		}
		sb.append(" and pk_glorgbook='" + pk_glorgbook + "' ");
		sb.append(" and ((distill_date = '" + date
				+ "' and genstate = 2) or (distill_date > '" + date
				+ "' and genstate in (1, 3)))");
		sb.append(" and distill_date<='");
		sb.append(tradedate + "'");
		sb.append(" group by ");
		sb.append(manItemSb.toString());
		List<Sim_fairvaluedistillVO> list = (List) getQueryservice()
				.executeQuery(sb.toString(),
						new BeanListProcessor(Sim_fairvaluedistillVO.class));
		Map map = new HashMap();
		String key = null;
		if ((list != null) && (list.size() > 0)) {
			for (int i = 0; i < list.size(); i++) {
				Sim_fairvaluedistillVO vo = (Sim_fairvaluedistillVO) list
						.get(i);
				key = VOUtil.getCombinesKey(vo, manItemCodes);
				map.put(key, vo.getFair_balance());
			}
		}
		return map;
	}

	public Map getInterest(String tradedate, CostPlanVO costplanvo,
			boolean ifaccrual) throws BusinessException {
		String pk_org = costplanvo.getPk_org();
		String pk_glorgbook = costplanvo.getPk_glorgbook();

		StringBuffer manItemSb = new StringBuffer();
		StringBuffer manSelectkey = new StringBuffer();
		UFDate firstdate = new UFDate(CostConstant.DEFAULT_DATE);
		String[] manItemCodes = SimReportUtils.getManFieldArray(costplanvo);
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" select sum(case genstate when 2 then interestdistill when 3 then interestdistill else interestdistill * -1 end ) interestdistill ,");
		sb.append(manItemSb.toString());
		sb.append(" from sim_interestdistill  where isnull(dr,0)=0 ");
		if (pk_org != null) {
			if (!"@@@@".equals(pk_org)) {
				sb.append(" and pk_org='" + pk_org + "' ");
				sb.append(" and pk_costplan='");
				sb.append(costplanvo.getPk_costplan()).append("' ");
			} else {
				sb.append(" and pk_costplan in (select pk_costplan from sim_costplan where isnull(dr,0)=0 and isbusinessplan ='y' ) ");
			}
		}
		sb.append(" and pk_glorgbook='" + pk_glorgbook + "' ");
		sb.append("  and distill_date >= '" + firstdate.asBegin()
				+ "' and distill_date <= '" + tradedate + "' ");
		sb.append(" group by ");
		sb.append(manItemSb.toString());
		List<InterestdistillVO> splist = (List) getQueryservice().executeQuery(
				sb.toString(), new BeanListProcessor(InterestdistillVO.class));
		Map map = new HashMap();
		String key = null;
		if ((splist != null) && (splist.size() > 0)) {
			for (int i = 0; i < splist.size(); i++) {
				InterestdistillVO vo = (InterestdistillVO) splist.get(i);
				key = VOUtil.getCombinesKey(vo, manItemCodes);
				map.put(key, vo);
			}
		}
		return map;
	}

	public List<SuperVO> getTransferData(String condition, String startDate,
			String endDate, CostPlanVO costplanvo, String transtypecode)
			throws BusinessException {
		StringBuffer cons = new StringBuffer();
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer manfiled = new StringBuffer();
		String[] manItemCodes = (String[]) null;
		String[] groupfield = (String[]) null;
		String stockCondition = null;
		if ((transtypecode.equals(ReportConst.KGZKR))
				|| (transtypecode.equals(ReportConst.HGQYHZ))
				|| (transtypecode.equals(ReportConst.HGKGZKR))) {
			manItemCodes = new String[] { "a.hr_pk_assetsprop ",
					"a.hr_pk_stocksort pk_stocksort ", "a.hr_pk_capaccount  " };
			groupfield = new String[] { "a.hr_pk_assetsprop ",
					"a.hr_pk_stocksort ", "a.hr_pk_capaccount" };
			stockCondition = "and a.hr_pk_stocksort= '" + ReportConst.STOCKKR
					+ "'";
		} else {
			manItemCodes = new String[] { "a.hc_pk_assetsprop ",
					"a.hc_pk_stocksort pk_stocksort ", "a.hc_pk_capaccount  " };
			groupfield = new String[] { "a.hc_pk_assetsprop ",
					"a.hc_pk_stocksort ", "a.hc_pk_capaccount" };
			stockCondition = "and a.hc_pk_stocksort= '" + ReportConst.STOCKKR
					+ "'";
		}
		if (transtypecode.equals("QYHZHR")) {
			transtypecode = ReportConst.QYHZ;
			manItemCodes = new String[] { "a.hr_pk_assetsprop ",
					"a.hr_pk_stocksort pk_stocksort ", "a.hr_pk_capaccount  " };
			groupfield = new String[] { "a.hr_pk_assetsprop ",
					"a.hr_pk_stocksort ", "a.hr_pk_capaccount" };
			stockCondition = "and a.hr_pk_stocksort= '" + ReportConst.STOCKKR
					+ "'";
		} else if (transtypecode.equals("QYHZHC")) {
			transtypecode = ReportConst.QYHZ;
			manItemCodes = new String[] { "a.hc_pk_assetsprop ",
					"a.hc_pk_stocksort pk_stocksort ", "a.hc_pk_capaccount  " };
			groupfield = new String[] { "a.hc_pk_assetsprop ",
					"a.hc_pk_stocksort ", "a.hc_pk_capaccount" };
			stockCondition = "and a.hc_pk_stocksort= '" + ReportConst.STOCKKR
					+ "'";
		}
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		for (int i = 0; i < groupfield.length; i++) {
			manfiled.append(groupfield[i]);
			if (i < manItemCodes.length - 1) {
				manfiled.append(",");
			}
		}
		cons.append("select sum(a.bargain_num) bargain_num,sum(a.bargain_sum) bargain_sum,a.pk_securities,");
		cons.append(" a.pk_group, a.pk_org, a.pk_glorgbook,");
		cons.append(manItemSb.toString());
		cons.append(" from smt_transferecord a ");
		cons.append("where isnull(dr,0)=0 and a.state>=2 ");
		cons.append("and a.transtypecode = '" + transtypecode + "' ");
		cons.append(stockCondition);
		cons.append("and a.pk_org = '" + costplanvo.getPk_org() + "'");
		cons.append("and a.pk_glorgbook = '" + costplanvo.getPk_glorgbook()
				+ "'");
		cons.append("and a.trade_date >='" + startDate + "' ");
		cons.append("and a.trade_date <='" + endDate + "' ");
		cons.append("group by a.pk_securities,a.pk_org, a.pk_glorgbook, a.pk_group,");
		cons.append(manfiled.toString());
		List<SuperVO> list = (List) getQueryservice().executeQuery(
				cons.toString(), new BeanListProcessor(TransferRecordVO.class));
		return list;
	}

	public List<SuperVO> getMarginData(String condition, String startDate,
			String endDate, CostPlanVO costplanvo, String transtypecode)
			throws BusinessException {
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String[] manItemCodes = { " pk_assetsprop", "pk_stocksort",
				"  pk_group", " pk_org", "   pk_glorgbook", "   pk_capaccount",
				"   pk_securities" };
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		sql.append("select sum(bargain_num) bargain_num,sum(bargain_sum) bargain_sum,");
		sql.append(manItemSb.toString());
		sql.append(" from smt_margintrade ");
		sql.append(" where isnull(dr,0)=0 and state>=2");
		sql.append(" and transtypecode = '" + transtypecode + "' ");
		sql.append(" and pk_org = '" + costplanvo.getPk_org() + "'");
		sql.append(" and pk_glorgbook = '" + costplanvo.getPk_glorgbook() + "'");
		sql.append(" and trade_date >='" + startDate + "' ");
		sql.append(" and trade_date <='" + endDate + "' ");
		sql.append(" group by ");
		sql.append(manItemSb.toString());

		List<SuperVO> list = (List) getQueryservice().executeQuery(
				sql.toString(), new BeanListProcessor(MarginTradeVO.class));
		return list;
	}

	public List<SuperVO> getRefinancingData(String condition, String startDate,
			String endDate, CostPlanVO costplanvo, String transtypecode)
			throws BusinessException {
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String[] manItemCodes = { " pk_assetsprop", "pk_stocksort",
				"  pk_group", " pk_org", "   pk_glorgbook", "   pk_capaccount",
				"   pk_securities" };
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		sql.append("select sum(bargain_num) bargain_num,sum(bargain_sum) bargain_sum,");
		sql.append(manItemSb.toString());
		sql.append(" from smt_refinancing ");
		sql.append(" where isnull(dr,0)=0 and state>=2");
		sql.append(" and transtypecode = '" + transtypecode + "' ");
		sql.append(" and pk_org = '" + costplanvo.getPk_org() + "'");
		sql.append(" and pk_glorgbook = '" + costplanvo.getPk_glorgbook() + "'");
		sql.append(" and trade_date >='" + startDate + "' ");
		sql.append(" and trade_date <='" + endDate + "' ");
		sql.append(" group by ");
		sql.append(manItemSb.toString());

		List<SuperVO> list = (List) getQueryservice().executeQuery(
				sql.toString(), new BeanListProcessor(RefinancingVO.class));
		return list;
	}
}
