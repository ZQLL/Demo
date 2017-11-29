package nc.bs.fba_scost.cost.interest.pub;

import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.vo.fba_scost.cost.interest.AggInterest;
import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import java.util.*;

/**
 * 债券借贷库存变动表工具类
 * 
 * @author mx
 * 
 */
public class zqjdStockChangeTool {
	/**
	 * 根据证券档案ID查询利率信息
	 */
	public AggInterest queryInterestsetInfo(String pk_securities)
			throws BusinessException {
		// start 20150612 性能优化 by:mx
		// String str =
		// " pk_securities = '"+pk_securities+"' and isnull(dr,0) = 0 ";
		String sql = "select * from sim_interest where pk_securities = ?  and isnull(dr,0) = 0";
		SQLParameter param = new SQLParameter();
		param.addParam(pk_securities);
		/** JINGQT 2015年7月6日 性能优化产生的BUG修正 DEL START */
		// Interest[] vos = (Interest[])DBCacheQueryFacade.runQuery(sql, param,
		// new BeanListProcessor(Interest.class));
		/** JINGQT 2015年7月6日 性能优化产生的BUG修正 DEL END */
		/** JINGQT 2015年7月6日 性能优化产生的BUG修正 ADD START */
		List<Interest> vos = (List<Interest>) DBCacheQueryFacade.runQuery(sql,
				param, new BeanListProcessor(Interest.class));
		/** JINGQT 2015年7月6日 性能优化产生的BUG修正 ADD END */
		// Interest[] vos = (Interest[])new
		// HYSuperDMO().queryByWhereClause(Interest.class,str);
		// end 20150612 性能优化 by:mx
		if (vos == null || vos.size() == 0) {
			SecuritiesVO vo = querySecuritiesVO(pk_securities);
			throw new BusinessException(" 证券档案 [" + vo.getCode() + "、"
					+ vo.getName() + "] 利率没有设置! ");
		}
		// start 20150612 性能优化 by:mx
		// String condition =
		// " pk_interest = '"+vos[0].getPk_interest()+"' and isnull(dr,0) = 0  ";
		// fulq 后续需要计算 提前还款 根据 开始日期 排序
		// String sql_rate =
		// "select * from sim_rateperiod where pk_interest = ?  and isnull(dr,0) = 0";
		String sql_rate = "select * from sim_rateperiod where pk_interest = ?  and isnull(dr,0) = 0  order by start_day  ";

		SQLParameter param_rate = new SQLParameter();
		/** JINGQT 2015年7月8日 性能优化产生的BUG修正 DEL START */
		// param.addParam(vos.get(0).getPk_interest());
		// Rateperiod[] ratevos =
		// (Rateperiod[])DBCacheQueryFacade.runQuery(sql_rate, param_rate, new
		// BeanListProcessor(Rateperiod.class));
		/** JINGQT 2015年7月8日 性能优化产生的BUG修正 DEL END */
		/** JINGQT 2015年7月8日 性能优化产生的BUG修正 ADD START */
		param_rate.addParam(vos.get(0).getPk_interest());
		/** JINGQT 2015年7月8日 性能优化产生的BUG修正 ADD END */
		List<Rateperiod> ratevos = (List<Rateperiod>) DBCacheQueryFacade
				.runQuery(sql_rate, param_rate, new BeanListProcessor(
						Rateperiod.class));
		// end 20150612 性能优化 by:mx
		if (ratevos == null || ratevos.size() == 0) {
			SecuritiesVO vo = querySecuritiesVO(pk_securities);
			throw new BusinessException(" 证券档案 [" + vo.getCode() + "、"
					+ vo.getName() + "] 利率表体信息没有设置! ");
		}
		AggInterest inest = new AggInterest();
		inest.setParent(vos.get(0));
		inest.setChildrenVO(ratevos.toArray(new Rateperiod[ratevos.size()]));
		return inest;
	}

	/**
	 * 根据证券档案ID查询证券档案
	 */
	public SecuritiesVO querySecuritiesVO(String pk_securities)
			throws BusinessException {
		// start 20150612 性能优化 by:mx
		String sql = "Select * from sec_securities where pk_securities = ?";
		SQLParameter param = new SQLParameter();
		param.addParam(pk_securities);
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 ADD START */
		Object obj = DBCacheFacade.runQuery(sql, param, new BeanListProcessor(
				SecuritiesVO.class));
		List<SecuritiesVO> voo = (List<SecuritiesVO>) obj;
		if (null == voo || voo.size() == 0) {
			return new SecuritiesVO();
		}
		return voo.get(0);
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 ADD END */
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 DEL START */
		// SecuritiesVO vo = (SecuritiesVO)DBCacheFacade.runQuery(sql,param,new
		// BeanListProcessor(SecuritiesVO.class));
		// end 20150612 by:mx
		// SecuritiesVO vo = (SecuritiesVO)new
		// HYSuperDMO().queryByPrimaryKey(SecuritiesVO.class,pk_securities);
		// return vo;
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 DEL END */

	}

	/**
	 * @param str
	 * @return 开始日期格式转换和完整日期拼接
	 */
	public static UFDateTime getBeginDate(String str) {

		UFDateTime beginDate = new UFDateTime();
		if (str.length() != 0 && str != null) {
			if (str.length() <= 10) {
				str = str.concat(" 00:00:00");
			}
			beginDate = new UFDateTime(str);
		}
		return beginDate;
	}

	/**
	 * @param str
	 * @return 结束日期格式转换和完整日期拼接
	 */
	public static UFDateTime getEndDate(String str) {

		UFDateTime endDate = new UFDateTime();
		if (str.length() != 0 && str != null) {
			if (str.length() <= 10) {
				str = str.concat(" 23:59:59");
			}
			endDate = new UFDateTime(str);
		}
		return endDate;
	}

	/**
	 * @param transtypecode
	 *            交易类型
	 * @param beginDate
	 *            开始日期
	 * @return 查询期初库存SQL(证券交易表)
	 */
	public static String getStartSql(String transtypecode, String beginDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(a.pk_group) pk_group,");
		sb.append("max(a.pk_org) pk_org,");
		sb.append("max(a.pk_assetsprop) pk_assetsprop,");
		sb.append("max(a.pk_stocksort) pk_stocksort,");
		sb.append("max(a.pk_securities) pk_securities,");
		sb.append("max(a.pk_capaccount) pk_capaccount,");
		sb.append("a.pk_securities,");
		sb.append("sum(a.bargain_num) bargain_num ");
		sb.append("from sim_zqjd a ");
		sb.append("where a.state >= 2 ");
		sb.append("and isnull(a.dr,0) = 0 ");
		sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
		sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
		sb.append("and trade_date < \'" + beginDate + "\' ");
		sb.append("group by a.pk_securities");

		return sb.toString();
	}

	/**
	 * @param transtypecode
	 *            交易类型
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询本期库存SQL(证券交易表)
	 */
	public static String getNowSql(String transtypecode, String trade_date) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(a.pk_group) pk_group,");
		sb.append("max(a.pk_org) pk_org,");
		sb.append("max(a.pk_org_v) pk_org_v,");
		// sb.append("max(a.pk_assetsprop) pk_assetsprop,");
		sb.append("max(a.pk_stocksort) pk_stocksort,");
		// sb.append("max(a.pk_securities) pk_securities,");
		// sb.append("max(a.pk_capaccount) pk_capaccount,");
		sb.append("a.pk_securities,");
		sb.append("a.pk_assetsprop,");
		sb.append("a.pk_capaccount,");
		sb.append("a.pk_glorgbook,");

		sb.append("sum(a.bargain_num) bargain_num ,");
		sb.append("sum(a.bargain_sum) bargain_sum ");
		sb.append("from sim_zqjd a ");
		sb.append("where a.state >= 2 ");
		sb.append("and isnull(a.dr,0) = 0 ");
		sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
		sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
		sb.append("and '" + trade_date + "' > trade_date ");
		sb.append("and '" + trade_date.substring(0, 10) + "' < date_end ");
		// sb.append("and trade_date between \'" + beginDate + "\' and \'" +
		// endDate + "|| 23:59:59\'");
		sb.append("group by   a.pk_group,a.pk_org,a.pk_org_v,a.pk_securities,a.pk_assetsprop,a.pk_capaccount,a.pk_glorgbook");
		return sb.toString();
	}

	/**
	 * @param field
	 *            查询字段 数量or金额
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询相应字段本期卖出SQL
	 */
	public static String getNowSellSql(String beginDate, String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(a.pk_group) pk_group,");
		sb.append("max(a.pk_org) pk_org,");
		sb.append("max(a.pk_assetsprop) pk_assetsprop,");
		sb.append("max(a.pk_stocksort) pk_stocksort,");
		sb.append("max(a.pk_securities) pk_securities,");
		sb.append("max(a.pk_capaccount) pk_capaccount,");
		sb.append("a.pk_securities,");
		sb.append("sum(a.bargain_num) bargain_num, ");
		sb.append("sum(a.fact_sum) bargain_sum ");
		sb.append("from sim_stocktrade a ");
		sb.append("where isnull(a.dr,0) = 0 ");
		// 2015年5月25日 zhaoxmc 增加审批状态判断
		sb.append("and a.state >='2'");
		sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
		sb.append("and a.transtypecode = 'HV3A-0xx-22' ");// 卖出单
		sb.append("and trade_date between \'" + beginDate + "\' and \'"
				+ endDate + "|| 23:59:59\'");
		sb.append("group by a.pk_securities");

		return sb.toString();
		// String sql = "select a.pk_securities,sum(a." + field + ") " + field
		// + " from sim_zqjdtally a where " + field + ">0"
		// + "and trade_date between \'" + beginDate + "\' and \'"
		// + endDate + "\'" + "group by a.pk_securities";
		// return sql;
	}

	/**
	 * @param field
	 *            查询字段 数量or金额
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询相应字段本期买入SQL(交易记录负债买入单)
	 */
	public static String getNowBuySql(String beginDate, String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(a.pk_group) pk_group,");
		sb.append("max(a.pk_org) pk_org,");
		sb.append("max(a.pk_assetsprop) pk_assetsprop,");
		sb.append("max(a.pk_stocksort) pk_stocksort,");
		sb.append("max(a.pk_securities) pk_securities,");
		sb.append("max(a.pk_capaccount) pk_capaccount,");
		sb.append("a.pk_securities,");
		sb.append("sum(a.bargain_num) bargain_num, ");
		sb.append("sum(a.fact_sum) bargain_sum ");
		sb.append("from sim_stocktrade a ");
		sb.append("where isnull(a.dr,0) = 0 ");
		sb.append("and a.state >='2'");
		sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
		sb.append("and a.transtypecode = 'HV3A-0xx-21' ");// 买入单
		sb.append("and trade_date between \'" + beginDate + "\' and \'"
				+ endDate + "|| 23:59:59\'");
		sb.append("group by a.pk_securities");

		return sb.toString();
		// String sql = "select a.pk_securities,sum(a." + field + ") " + field
		// + " from sim_zqjdtally a where " + field + "<0"
		// + "and trade_date between \'" + beginDate + "\' and \'"
		// + endDate + "\'" + "group by a.pk_securities";
		// return sql;
	}

	/**
	 * @param string
	 * @param beginDate
	 * @param endDate
	 * @return 本期买入负债成本sql
	 */
	public static String getNowDebtSql(String transtypecode, String beginDate,
			String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(a.pk_group) pk_group,");
		sb.append("max(a.pk_org) pk_org,");
		sb.append("max(a.pk_assetsprop) pk_assetsprop,");
		sb.append("max(a.pk_stocksort) pk_stocksort,");
		sb.append("max(a.pk_securities) pk_securities,");
		sb.append("max(a.pk_capaccount) pk_capaccount,");
		sb.append("a.pk_securities,");
		sb.append("sum(a.sellcost) sellcost ");
		sb.append("from sim_stocktrade a ");
		sb.append("where a.state >= 2 ");
		sb.append("and isnull(a.dr,0) = 0 ");
		sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
		sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
		sb.append("and trade_date between \'" + beginDate + "\' and \'"
				+ endDate + "|| 23:59:59\'");
		sb.append("group by a.pk_securities");

		return sb.toString();
	}

	// 利息SQL
	public static String getInterestSql(String transtypecode, String beginDate,
			String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(a.pk_group) pk_group,");
		sb.append("max(a.pk_org) pk_org,");
		sb.append("max(a.pk_assetsprop) pk_assetsprop,");
		sb.append("max(a.pk_stocksort) pk_stocksort,");
		sb.append("max(a.pk_securities) pk_securities,");
		sb.append("max(a.pk_capaccount) pk_capaccount,");
		sb.append("a.pk_securities,");
		sb.append("sum(a.interest_sum) interest_sum ");
		sb.append("from sim_zqjd a ");
		sb.append("where a.state >= 2 ");
		sb.append("and isnull(a.dr,0) = 0 ");
		sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
		sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
		sb.append("and trade_date between \'" + beginDate + "\' and \'"
				+ endDate + "|| 23:59:59\'");
		sb.append("group by a.pk_securities");

		return sb.toString();
		// String sql = "select a.pk_securities,a.interest_sum from sim_zqjd a";
		// return sql;
	}

	// /**
	// * 填充数值字段 方便期末库存计算
	// * @param lastResult
	// * @return
	// */
	// public static List<StockChangeVO> fullWithZero(List<StockChangeVO>
	// lastResult) {
	// for (StockChangeVO scvo : lastResult) {
	//
	// UFDouble uf = new UFDouble(0);
	// if(scvo.getStart_bow_num()==null){
	// scvo.setStart_bow_num(uf);
	// }
	// if(scvo.getNow_bow()==null){
	// scvo.setNow_bow(uf);
	// }
	// if(scvo.getNow_rtn()==null){
	// scvo.setNow_rtn(uf);
	// }
	// if(scvo.getStart_sell_num()==null){
	// scvo.setStart_sell_num(uf);
	// }
	// if(scvo.getNow_sell_num()==null){
	// scvo.setNow_sell_num(uf);
	// }
	// if(scvo.getNow_buy_num()==null)
	// {
	// scvo.setNow_buy_num(uf);
	// }
	// if(scvo.getStart_debt()==null){
	// scvo.setStart_debt(uf);
	// }
	// if(scvo.getNow_sell_sum()==null)
	// {
	// scvo.setNow_sell_sum(uf);
	// }
	// if(scvo.getNow_debt()==null){
	// scvo.setNow_debt(uf);
	// }
	// }
	// return lastResult;
	//
	// }

}
