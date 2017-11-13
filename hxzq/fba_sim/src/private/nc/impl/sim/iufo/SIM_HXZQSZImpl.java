package nc.impl.sim.iufo;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.fba_sim.simtrade.report.FundInOutUtils;
import nc.bs.fba_sim.simtrade.report.ReportSqlUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.sim.iufo.IufoQuerySIMVO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.SimReportUtils;
import nc.vo.fba_sabb.trade.ydsgh.AgreedbbVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.trademarket.TradeMarketVO;
import nc.vo.fba_sim.simtrade.report.SecInvestDailyRepVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.freereport.ReportDataVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

/**
 * 证券市值函数
 * @author hupl
 */
public class SIM_HXZQSZImpl extends SIMIufoResultServiceImpl {
	/**
	 * 
	 */
	public Object getFuncResult(IufoQuerySIMVO simvo,boolean isqc)throws Exception {
		try {
			
			SIM_Analysis an = new SIM_Analysis();
			String condition = an.bulidCondition(simvo, true);
			String distillDate = null;
			if(isqc){//期初
				distillDate = getQcDate(simvo);
			}else{//期末
				distillDate = getQmDate(simvo);
			}
			CostPlanVO costplan = queryCostPlanInfo(simvo.getPk_group(),simvo.getPk_org(), simvo.getPk_glBookCode());
			//查询库存
			SimStockBanlance banlace = new SimStockBanlance();
			List<StockBalanceVO> zl = this.getFir(condition, distillDate);
			UFDouble result = new UFDouble(0.0);
			if(zl != null && zl.size() > 0){
				for(StockBalanceVO z : zl){
					UFDouble stocknum = z.getStocks_num() == null ? new UFDouble(0.0) : z.getStocks_num();
					TradeMarketVO vo = queryLastMarket(z.getPk_securities(),distillDate,isqc);
					UFDouble price = vo.getClose_price() == null ? new UFDouble(0.0) : vo.getClose_price();
					result = result.add(stocknum.multiply(price));
				}
			}
			return result;
		} catch (Exception e) {
			Logger.debug("查询出错！");
			return null;
		}
	}
	
	public TradeMarketVO queryLastMarket(String pk_securities,String trade_date,boolean isqc)throws BusinessException{
		TradeMarketVO marketvo = null;
		if(pk_securities == null || trade_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select * from sim_trademarket where pk_securities = '"+pk_securities+"' ");
		sf.append(" and trade_date =  ");
		sf.append(" (select isnull(max(trade_date),'"+CostConstant.DEFAULT_DATE+"')  from sim_trademarket where ");
		if(isqc){
			sf.append(" isnull(dr, 0) = 0 and trade_date < '"+trade_date+"' ");//期初
		}else{
			sf.append(" isnull(dr, 0) = 0 and trade_date <= '"+trade_date+"' ");//期末
		}
		sf.append(" and pk_securities = '"+pk_securities+"') ");
		sf.append(" and isnull(dr, 0) = 0 ");
		List<TradeMarketVO> list = (List<TradeMarketVO>)new BaseDAO().executeQuery(sf.toString(), new BeanListProcessor(TradeMarketVO.class));
		if(list != null && list.size() > 0 ){
			marketvo = list.get(0);
		}
		return marketvo;
	}
	
	private List<StockBalanceVO> getFir(String condition, String end_date)
			throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		// 查询库存期初
		List<StockBalanceVO> sbvs = new ArrayList<StockBalanceVO>();

		List<AgreedbbVO> agreedbbs = null;
		ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
		String glsql = getglsql();
		try {
			// 查询除去所选时间段内发生的证券档案的买入总和
			String querySql1 = "select  a.pk_securities, sum(a.entrust_num) as innum, sum(a.entrust_sum) as insum "
					+ " from sim_agreedbb a "
					+ " inner join sec_securities b "
					+ " on a.pk_securities = b.pk_securities "
					+ " where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-01' and "
					+ condition
					+ " and a.trade_date <= '"
					+ end_date
					+ "' and if_accrual='N' "
					+ glsql
					+ " group by a.pk_securities";
			List<ReportDataVO> reportVos1 = (List<ReportDataVO>) queryservice
					.executeQuery(querySql1, new BeanListProcessor(
							ReportDataVO.class));
			// 查询除去所选时间段内发生的证券档案的卖出总和
			String querySql2 = "select  a.pk_securities, sum(a.entrust_num) as innum, sum(a.entrust_sum) as insum"
					+ " from sim_agreedbb a "
					+ " inner join sec_securities b "
					+ " on a.pk_securities = b.pk_securities "
					+ " where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-03' and "
					+ condition
					+ " and a.trade_date <= '"
					+ end_date
					+ "' and if_accrual='N' "
					+ glsql
					+ " group by a.pk_securities";
			List<ReportDataVO> reportVos2 = (List<ReportDataVO>) queryservice
					.executeQuery(querySql2, new BeanListProcessor(
							ReportDataVO.class));
			// 期初 = 买入-卖出
			if (reportVos1 != null && reportVos1.size() > 0) {
				if (reportVos2 != null && reportVos2.size() > 0) {
					for (ReportDataVO rdv1 : reportVos1) {
						String pk_securities = rdv1.getPk_securities();
						for (ReportDataVO rdv2 : reportVos2) {
							if (pk_securities.equals(rdv2.getPk_securities())) {
								rdv1.setInnum(rdv1.getInnum().sub(
										rdv2.getInnum()));
								rdv1.setInsum(rdv1.getInsum().sub(
										rdv2.getInsum()));
							}
						}
					}
				}
				// 循环转入map
				SecInvestDailyRepVO rprtvo2 = null;

				for (ReportDataVO rdv1 : reportVos1) {
					if (rdv1.getInnum().compareTo(new UFDouble(0)) > 0) {
						String pk_securities = rdv1.getPk_securities();
						String querySql3 = "select a.vdef9 as pk_selfsgroup,a.pk_org, a.pk_group, a.pk_glorgbook, a.vdef8 as pk_assetsprop, a.vdef7 as pk_stocksort, a.transtypecode as pk_billtype, a.vdef6 as pk_capaccount, a.vdef5 as pk_client, a.vdef4 as pk_operatesite, a.vdef3 as pk_partnaccount, a.pk_securities, a.trade_date, a.vdef2 as begin_date, a.vdef1 as end_date, a.def_interest as buyaccrual"
								+ " from sim_agreedbb a "
								+ " inner join sec_securities b "
								+ " on a.pk_securities = b.pk_securities "
								+ " where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-01' and a.pk_securities = '"
								+ pk_securities + "'";
						List<ReportDataVO> reportVos = (List<ReportDataVO>) queryservice
								.executeQuery(querySql3, new BeanListProcessor(
										ReportDataVO.class));
						ReportDataVO reportVo = new ReportDataVO();
						reportVo = reportVos.get(0);

						// 组装StockBalanceVO
						StockBalanceVO sbv = new StockBalanceVO();
						sbv.setPk_securities(pk_securities);
						sbv.setPk_org(reportVo.getPk_org());
						// sbv.setTrade_date(reportVo.getTrade_date());
						sbv.setPk_stockbalance(new UFDateTime().toString());
						sbv.setPk_group(reportVo.getPk_group());
						sbv.setPk_glorgbook(reportVo.getPk_glorgbook());
						sbv.setStocks_num(rdv1.getInnum());
						sbv.setStocks_sum(rdv1.getInsum());
						sbvs.add(sbv);
					}
				}
			}
			return sbvs;
		} catch (Exception e) {
			throw new BusinessException("期初查询出错！");
		}
	}
	
	/**
	 * 获取过滤sql 查询'HV5A-0xx-03'类型的交易记录
	 */
	private String getglsql() throws BusinessException {

		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		String querydbbSql = 
				"select a.contract_id, a.pk_aggreedbb\n" +
						"  from sim_agreedbb a\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and a.contract_id not in\n" + 
						"       (select contract_id\n" + 
						"          from (select count(*), contract_id\n" + 
						"                  from sim_agreedbb a\n" + 
						"                 where a.state > 1\n" + 
						"                   and nvl(a.dr, 0) = 0\n" + 
						"                 group by contract_id\n" + 
						"                having count(*) > 1))\n" + 
						"   and a.transtypecode = 'HV5A-0xx-03'";

		String glsql = "";
		try {
			List<AgreedbbVO> greedbbVos = (List<AgreedbbVO>) queryservice
					.executeQuery(querydbbSql, new BeanListProcessor(
							AgreedbbVO.class));
			if (greedbbVos.size() > 0 && greedbbVos != null) {
				List<String> samepks = new ArrayList<String>();
				for (int k = 0; k < greedbbVos.size(); k++) {
					// 主键
					samepks.add(greedbbVos.get(k).getPk_aggreedbb());
				}
				// 组合sql条件
				if (samepks.size() > 0) {
					// 去除重复数据
					samepks = array_unique(samepks);
					glsql += "and a.pk_aggreedbb not in (";
					for (int gl = 0; gl < samepks.size(); gl++) {
						if (gl < samepks.size() - 1) {
							glsql += "'" + samepks.get(gl).toString() + "',";
						} else {
							glsql += "'" + samepks.get(gl).toString() + "')";
						}
					}
				}
			}

		} catch (Exception e) {
			throw new BusinessException("明细查询错误!" + e.getMessage());
		}
		return glsql;
	}
	
	// 去重复数据
	public static List<String> array_unique(List<String> a) {
		// array_unique
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < a.size(); i++) {
			if (!list.contains(a.get(i))) {
				list.add(a.get(i));
			}
		}
		return list;
	}

	// 去除VO重复主键
	public List<String> vo_unique(List<AgreedbbVO> greedbbVos) {
		List<AgreedbbVO> vos = new ArrayList<AgreedbbVO>();
		List<String> list = new ArrayList<String>();
		if (greedbbVos != null && greedbbVos.size() > 0) {
			for (AgreedbbVO vo : greedbbVos) {
				list.add(vo.getPk_securities());
			}
		}
		return list;
	}
}
