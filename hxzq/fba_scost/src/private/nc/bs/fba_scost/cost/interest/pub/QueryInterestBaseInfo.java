package nc.bs.fba_scost.cost.interest.pub;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_scost.cost.pub.QueryBasePubInfo;
import nc.bs.trade.business.HYSuperDMO;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.interest.AggInterest;
import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_scost.cost.interestdistill.AggInterestDist;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

/**
 * 查询 应收利息基本信息
 * 
 */
public class QueryInterestBaseInfo extends QueryBasePubInfo {

	/**
	 * 查询应收利息是否生成凭证
	 */
	public boolean isGenInterestVoucher(LoginContext context, UFDate trade_date)
			throws BusinessException {
		if (context == null || trade_date == null)
			return false;
		boolean flag = false;
		StringBuffer sf = new StringBuffer();
		sf.append("  select count(*) num from sim_interestdistill ee  ");
		sf.append("  where ee.state = " + SystemConst.statecode[5] + "   ");// 生成凭证状态
		sf.append("  and ee.pk_group = '" + context.getPk_group()
				+ "' and ee.pk_org = '" + context.getPk_org() + "' ");
		sf.append("  and ee.pk_glorgbook = '"
				+ AppContextUtil.getDefaultOrgBook() + "'  ");
		sf.append("  and isnull(ee.dr,0) = 0  and ee.distill_date >= '"
				+ trade_date.asBegin() + "' ");
		try {
			flag = (Boolean) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						@Override
						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							boolean flag = false;
							int num = 0;
							if (rs.next()) {
								num = rs.getInt("num");
							}
							if (num > 0)
								flag = true;
							return new Boolean(flag);
						}
					});
		} catch (DAOException e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return flag;
	}

	/**
	 * 查询应收利息最大计提日期
	 */
	public UFDate getMaxjtDate(LoginContext context) throws BusinessException {
		UFDate date = new UFDate(CostConstant.DEFAULT_DATE);
		StringBuffer sf = new StringBuffer();
		sf.append(" select max(t.distill_date)  distill_date from sim_interestdistill t ");
		sf.append("  where t.pk_org = '" + context.getPk_org() + "'    ");
		sf.append("   and t.pk_group = '" + context.getPk_group()
				+ "'  and isnull(dr,0) = 0 ");
		sf.append("   and t.genstate = "
				+ CostConstant.INTEREST_GENSTATE.JITI.getIndex() + "   ");
		sf.append("   and t.pk_glorgbook = '"
				+ AppContextUtil.getDefaultOrgBook() + "'  ");
		try {
			date = (UFDate) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						@Override
						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							UFDate date = new UFDate(CostConstant.DEFAULT_DATE);
							if (rs.next()) {
								String distill_date = rs
										.getString("distill_date");
								date = distill_date == null ? new UFDate(
										CostConstant.DEFAULT_DATE)
										: new UFDate(distill_date);
							}
							return date;
						}
					});
		} catch (DAOException e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return date;
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
	 * 根据证券档案ID查询是否债券
	 */
	public boolean isSecuritiesAcc(String pk_securities)
			throws BusinessException {
		SecuritiesVO vo = querySecuritiesVO(pk_securities);
		boolean falg = false;
		if (vo != null)
			falg = vo.getIf_accrual() == null ? false : vo.getIf_accrual()
					.booleanValue();
		return falg;
	}

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

	public AggInterestDist[] queryDelVoucherData(LoginContext context,
			UFDate trade_date) throws BusinessException {
		StringBuffer sf = new StringBuffer();
		sf.append(" pk_org = '" + context.getPk_org() + "'  ");
		sf.append(" and pk_group = '" + context.getPk_group()
				+ "' and isnull(dr,0) = 0  ");
		sf.append(" and pk_glorgbook = '" + AppContextUtil.getDefaultOrgBook()
				+ "'  ");
		sf.append(" and distill_date >= '" + trade_date.asBegin() + "' ");
		sf.append(" and genstate = "
				+ CostConstant.FAIR_GENSTATE.JITI.getIndex() + " ");
		sf.append(" and state = " + SystemConst.statecode[4] + " ");// 记账状态
		InterestdistillVO[] fairstillvos = (InterestdistillVO[]) new HYSuperDMO()
				.queryByWhereClause(InterestdistillVO.class, sf.toString());
		if (fairstillvos == null || fairstillvos.length == 0)
			return null;
		AggInterestDist[] agfair = new AggInterestDist[fairstillvos.length];
		for (int i = 0; i < fairstillvos.length; i++) {
			agfair[i] = new AggInterestDist();
			agfair[i].setParent(fairstillvos[i]);
			agfair[i]
					.setChildrenVO(new InterestdistillVO[] { fairstillvos[i] });
		}
		return agfair;
	}

	public AggInterestDist[] querytallVoucherData(LoginContext context)
			throws BusinessException {
		StringBuffer sf = new StringBuffer();
		sf.append(" pk_org = '" + context.getPk_org() + "'  ");
		sf.append(" and pk_group = '" + context.getPk_group()
				+ "' and isnull(dr,0) = 0  ");
		sf.append(" and pk_glorgbook = '" + AppContextUtil.getDefaultOrgBook()
				+ "'  ");
		sf.append(" and genstate = "
				+ CostConstant.FAIR_GENSTATE.JITI.getIndex() + " ");// 计提单据
		sf.append(" and state = " + SystemConst.statecode[1] + " ");// 启用状态
		InterestdistillVO[] interestvos = (InterestdistillVO[]) new HYSuperDMO()
				.queryByWhereClause(InterestdistillVO.class, sf.toString());
		if (interestvos == null || interestvos.length == 0)
			return null;
		AggInterestDist[] agfair = new AggInterestDist[interestvos.length];
		for (int i = 0; i < interestvos.length; i++) {
			agfair[i] = new AggInterestDist();
			agfair[i].setParent(interestvos[i]);
			agfair[i].setChildrenVO(new InterestdistillVO[] { interestvos[i] });
		}
		return agfair;
	}

	/**
	 * 查询债券的最近库存记录
	 */
	@SuppressWarnings("unchecked")
	public List<StockBalanceVO> queryLastStock(CostPlanVO costplanvo,
			String pk_group, String pk_org, UFDate trade_date)
			throws BusinessException {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		if (costplanvo.getIf_glorgbook() != null
				&& costplanvo.getIf_glorgbook().booleanValue()) {
			sb.append(" a.pk_glorgbook, ");
			sb1.append(" and isnull(x.pk_glorgbook, 'pk_glorgbook') =isnull(y.pk_glorgbook, 'pk_glorgbook') ");
		}
		if (costplanvo.getIf_capaccount() != null
				&& costplanvo.getIf_capaccount().booleanValue()) {
			sb.append(" a.pk_capaccount, ");
			sb1.append(" and isnull(x.pk_capaccount, 'pk_capaccount') =isnull(y.pk_capaccount, 'pk_capaccount') ");
		}
		if (costplanvo.getIf_selfsgroup() != null
				&& costplanvo.getIf_selfsgroup().booleanValue()) {
			sb.append(" a.pk_selfsgroup, ");
			sb1.append(" and isnull(x.pk_selfsgroup, 'pk_selfsgroup') =isnull(y.pk_selfsgroup, 'pk_selfsgroup') ");
		}
		if (costplanvo.getIf_partnaccount() != null
				&& costplanvo.getIf_partnaccount().booleanValue()) {
			sb.append(" a.pk_partnaccount,  ");
			sb1.append(" and isnull(x.pk_partnaccount, 'pk_partnaccount') =isnull(y.pk_partnaccount, 'pk_partnaccount') ");
		}
		if (costplanvo.getIf_operatesite() != null
				&& costplanvo.getIf_operatesite().booleanValue()) {
			sb.append(" a.pk_operatesite, ");
			sb1.append(" and isnull(x.pk_operatesite, 'pk_operatesite') =isnull(y.pk_operatesite, 'pk_operatesite') ");
		}
		if (costplanvo.getIf_client() != null
				&& costplanvo.getIf_client().booleanValue()) {
			sb.append(" a.pk_client, ");
			sb1.append(" and isnull(x.pk_client, 'pk_client') =isnull(y.pk_client, 'pk_client') ");
		}
		StringBuffer sf = new StringBuffer();
		sf.append(" select y.* from (select max(a.trade_date) trade_date,  ");
		sf.append("            a.pk_org, a.pk_group,a.pk_assetsprop,a.pk_stocksort, ");
		sf.append(sb);
		sf.append("            a.pk_securities ");
		sf.append("        from sim_stockbalance a ");
		sf.append("      join sec_securities b on a.pk_securities = b.pk_securities ");
		sf.append("      where a.trade_date <='" + trade_date.asEnd() + "' ");
		sf.append("         and isnull(a.dr, 0) = 0 ");
		sf.append("         and isnull(b.dr, 0) = 0 ");
		sf.append("         and b.if_accrual  = 'Y' ");
		sf.append("         and a.pk_org = '" + pk_org + "' ");
		sf.append("         and a.pk_group = '" + pk_group + "' ");
		sf.append(" 		and a.pk_glorgbook = '"
				+ AppContextUtil.getDefaultOrgBook() + "'  ");
		sf.append("       group by  ");
		sf.append("               a.pk_org, a.pk_group,a.pk_assetsprop,a.pk_stocksort, ");
		sf.append(sb);
		sf.append("               a.pk_securities) x ");
		sf.append(" join sim_stockbalance y ");
		sf.append("    on x.trade_date = y.trade_date ");
		sf.append("   and isnull(x.pk_org, 'pk_org') = isnull(y.pk_org, 'pk_org') ");
		sf.append("   and isnull(x.pk_group, 'pk_group') = isnull(y.pk_group, 'pk_group') ");
		sf.append(sb1);
		sf.append("   and isnull(x.pk_securities, 'pk_securities') = isnull(y.pk_securities, 'pk_securities') ");
		sf.append("   and isnull(x.pk_assetsprop, 'pk_assetsprop') = isnull(y.pk_assetsprop, 'pk_assetsprop') ");
		sf.append("   and isnull(x.pk_stocksort, 'pk_stocksort') = isnull(y.pk_stocksort, 'pk_stocksort') ");
		sf.append("   and y.stocks_num > 0 ");
		sf.append("   and y.pk_costplan  = '" + costplanvo.getPk_costplan()
				+ "' ");
		List<StockBalanceVO> list = null;
		try {
			list = (List<StockBalanceVO>) new BaseDAO().executeQuery(
					sf.toString(), new BeanListProcessor(StockBalanceVO.class));
		} catch (DAOException e) {
			throw new BusinessException(e);
		}
		return list;
	}

	/**
	 * 查询筹资库存记录:审核日期当天及之前所有库存金额不为0的记录
	 */
	@SuppressWarnings("unchecked")
	public List<InventoryInfoVO> queryLastInventory(CostPlanVO costplanvo,
			String pk_group, String pk_org, UFDate trade_date)
			throws BusinessException {
		StringBuffer sf = new StringBuffer();
		sf.append(" select a.* from fund_inventoryinfo a");
		sf.append("   where a.enddate <= ");
		sf.append("'" + trade_date.asEnd() + "'");
		sf.append("   and a.enddate >= ");
		sf.append("'" + trade_date.asBegin() + "'");
		sf.append(" and a.stocks_sum > 0 ");
		sf.append("         and a.pk_org = '" + pk_org + "' ");
		sf.append("         and a.pk_group = '" + pk_group + "' ");
		sf.append("   and a.pk_costplan  = '" + costplanvo.getPk_costplan()
				+ "' ");
		sf.append("   and isnull(dr,0)=0 ");
		// 添加一个条件，保证只是查询买入的单子，不查询到期的单子
		sf.append("   and a.transtypecode like 'HV8_-0xx-01' ");

		List<InventoryInfoVO> list = null;
		try {
			list = (List<InventoryInfoVO>) new BaseDAO()
					.executeQuery(sf.toString(), new BeanListProcessor(
							InventoryInfoVO.class));
		} catch (DAOException e) {
			throw new BusinessException(e);
		}
		return list;
	}

	// /**
	// * 查询债券的起息日期
	// */
	// public UFDate queryFirstFxDate(String pk_securities,UFDate
	// trade_date)throws BusinessException{
	// if(pk_securities == null || trade_date == null)
	// return null;
	// AggInterest agginfo = queryInterestsetInfo(pk_securities);
	// Rateperiod[] ratevos = (Rateperiod[])agginfo.getChildrenVO();
	// Rateperiod vo = InterestTools.getContainsDate(ratevos, trade_date);
	// return vo.getStart_day();
	// }

	/**
	 * 查询本期期初应收利息
	 */
	public String getQcInterestSql(UFDate firstdate, CostPlanVO costplanvo,
			LoginContext context, UFDate trade_date, ITrade_Data stvo)
			throws BusinessException {
		if (firstdate == null || costplanvo == null || context == null
				|| trade_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		// start 20150612 性能优化 by:mx
		sf.append("select * from sim_interestdistill where ");
		// end 20150612 性能优化 by:mx
		sf.append(" distill_date >= '" + firstdate.asBegin()
				+ "' and distill_date <= '" + trade_date.asEnd() + "' ");
		sf.append(" and pk_costplan  = '" + costplanvo.getPk_costplan() + "' ");
		// sf.append(" and genstate = (1,2,3) ");//生成状态全部都算
		sf.append(" and isnull(dr,0) =  0 ");
		sf.append(" and pk_org = '" + context.getPk_org() + "'");
		sf.append(" and pk_group = '" + context.getPk_group() + "' ");
		sf.append(" and pk_assetsprop = '" + stvo.getPk_assetsprop() + "' ");
		sf.append(" and pk_stocksort = '" + stvo.getPk_stocksort() + "'   ");
		sf.append(" and pk_securities = '" + stvo.getPk_securities() + "'  ");
		sf.append(" and pk_glorgbook = '" + AppContextUtil.getDefaultOrgBook()
				+ "'  ");

		if (costplanvo.getIf_glorgbook() != null
				&& costplanvo.getIf_glorgbook().booleanValue()) {
			sf.append(" and  pk_glorgbook = '" + stvo.getPk_glorgbook() + "' ");
		}
		if (costplanvo.getIf_capaccount() != null
				&& costplanvo.getIf_capaccount().booleanValue()) {
			sf.append(" and  pk_capaccount = '" + stvo.getPk_capaccount()
					+ "' ");
		}
		if (costplanvo.getIf_selfsgroup() != null
				&& costplanvo.getIf_selfsgroup().booleanValue()) {
			sf.append(" and  pk_selfsgroup =  '" + stvo.getPk_selfsgroup()
					+ "'  ");
		}
		if (costplanvo.getIf_partnaccount() != null
				&& costplanvo.getIf_partnaccount().booleanValue()) {
			sf.append(" and  pk_partnaccount =  '" + stvo.getPk_partnaccount()
					+ "'  ");
		}
		if (costplanvo.getIf_operatesite() != null
				&& costplanvo.getIf_operatesite().booleanValue()) {
			sf.append(" and  pk_operatesite =  '" + stvo.getPk_operatesite()
					+ "'  ");
		}
		if (costplanvo.getIf_client() != null
				&& costplanvo.getIf_client().booleanValue()) {
			sf.append(" and  pk_client =  '" + stvo.getPk_client() + "'  ");
		}
		return sf.toString();
	}

	/**
	 * 查询本期期初应收利息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InterestdistillVO[] queryQcInterest(UFDate firstdate,
			CostPlanVO costplanvo, LoginContext context, UFDate trade_date,
			ITrade_Data stvo) throws BusinessException {
		if (firstdate == null || costplanvo == null || context == null
				|| trade_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		// start 20150612 性能优化 by:mx
		sf.append("select * from sim_interestdistill where ");
		// end 20150612 性能优化 by:mx
		sf.append(" distill_date >= '" + firstdate.asBegin()
				+ "' and distill_date <= '" + trade_date.asEnd() + "' ");
		sf.append(" and pk_costplan  = '" + costplanvo.getPk_costplan() + "' ");
		// sf.append(" and genstate = (1,2,3) ");//生成状态全部都算
		sf.append(" and isnull(dr,0) =  0 ");
		sf.append(" and pk_org = '" + context.getPk_org() + "' ");
		sf.append(" and pk_group = '" + context.getPk_group() + "' ");
		sf.append(" and pk_assetsprop = '" + stvo.getPk_assetsprop() + "' ");
		sf.append(" and pk_stocksort = '" + stvo.getPk_stocksort() + "'   ");
		sf.append(" and pk_securities = '" + stvo.getPk_securities() + "'  ");
		sf.append(" and pk_glorgbook = '" + AppContextUtil.getDefaultOrgBook()
				+ "'  ");

		if (costplanvo.getIf_glorgbook() != null
				&& costplanvo.getIf_glorgbook().booleanValue()) {
			// sf.append(" and  pk_glorgbook = '"+stvo.getPk_glorgbook()+"' ");
			sf.append(" and  pk_glorgbook = '"
					+ checkAttributeValue(stvo.getPk_glorgbook()) + "' ");
		}
		if (costplanvo.getIf_capaccount() != null
				&& costplanvo.getIf_capaccount().booleanValue()) {
			// sf.append(" and  pk_capaccount = '"+stvo.getPk_capaccount()+"' ");
			sf.append(" and  pk_capaccount = '"
					+ checkAttributeValue(stvo.getPk_capaccount()) + "' ");

		}
		if (costplanvo.getIf_selfsgroup() != null
				&& costplanvo.getIf_selfsgroup().booleanValue()) {
			// sf.append(" and  pk_selfsgroup =  '"+stvo.getPk_selfsgroup()+"'  ");
			sf.append(" and  pk_selfsgroup =  '"
					+ checkAttributeValue(stvo.getPk_selfsgroup()) + "'  ");
		}
		if (costplanvo.getIf_partnaccount() != null
				&& costplanvo.getIf_partnaccount().booleanValue()) {
			// sf.append(" and  pk_partnaccount =  '"+stvo.getPk_partnaccount()+"'  ");
			sf.append(" and  pk_partnaccount =  '"
					+ checkAttributeValue(stvo.getPk_partnaccount()) + "'  ");
		}
		if (costplanvo.getIf_operatesite() != null
				&& costplanvo.getIf_operatesite().booleanValue()) {
			// sf.append(" and  pk_operatesite =  '"+stvo.getPk_operatesite()+"'  ");
			sf.append(" and  pk_operatesite =  '"
					+ checkAttributeValue(stvo.getPk_operatesite()) + "'  ");
		}
		if (costplanvo.getIf_client() != null
				&& costplanvo.getIf_client().booleanValue()) {
			// sf.append(" and  pk_client =  '"+stvo.getPk_client()+"'  ");
			sf.append(" and  pk_client =  '"
					+ checkAttributeValue(stvo.getPk_client()) + "'  ");
		}
		// TODO fulq 倒序排序
		sf.append("   order by distill_date desc  ");
		// start 20150612 性能优化 by:mx
		// Collection values = (Collection)DBCacheQueryFacade
		// .runQuery(sf.toString(), new BeanListProcessor(
		// InterestdistillVO.class));
		// SuperVO[] superVos = (SuperVO[]) values.toArray((SuperVO[])
		// Array.newInstance(InterestdistillVO.class, 0));
		// InterestdistillVO[] vos = (InterestdistillVO[])superVos;
		Collection values = (Collection) DBCacheFacade.runQuery(sf.toString(),
				new BeanListProcessor(InterestdistillVO.class));
		SuperVO[] superVos = (SuperVO[]) values.toArray((SuperVO[]) Array
				.newInstance(InterestdistillVO.class, 0));
		InterestdistillVO[] vos = (InterestdistillVO[]) superVos;
		// end 20150612 性能优化 by:mx

		// InterestdistillVO[] vos = (InterestdistillVO[])new HYSuperDMO().
		// queryByWhereClause(InterestdistillVO.class, sf.toString());
		return vos;
	}

	/**
	 * 有些数据数据库存的控制为“~”查询的条件拼接时用的是字符串“null”导致查询数据不准确
	 * 
	 * @author cjh
	 * @date 2015-11-27 15：09：00
	 * @param value
	 * @return
	 */
	public String checkAttributeValue(String value) {
		if (value == null || "".equals(value.trim())
				|| "null".equals(value.trim())) {
			value = "~";
		}
		return value;
	}

	public void clearData(LoginContext context, UFDate trade_date)
			throws BusinessException {
		StringBuffer sf = new StringBuffer();
		sf.append(" delete from sim_interestdistill  where pk_org = '"
				+ context.getPk_org() + "'  ");
		sf.append("  and pk_group = '" + context.getPk_group()
				+ "' and isnull(dr,0) = 0   ");
		sf.append("  and pk_glorgbook = '" + AppContextUtil.getDefaultOrgBook()
				+ "'  ");
		sf.append("  and distill_date >= '" + trade_date.asBegin() + "'  ");
		sf.append("  and genstate = "
				+ CostConstant.INTEREST_GENSTATE.JITI.getIndex() + " ");// 计提
		new BaseDAO().executeUpdate(sf.toString());
	}

	public InterestdistillVO[] updatetallVoucherData(AggInterestDist[] aggvos)
			throws BusinessException {
		if (aggvos == null || aggvos.length == 0)
			return null;
		InterestdistillVO[] tillvos = new InterestdistillVO[aggvos.length];
		for (int i = 0; i < tillvos.length; i++) {
			InterestdistillVO distillvo = (InterestdistillVO) aggvos[i]
					.getParent();
			distillvo.setState(SystemConst.statecode[4]);
			tillvos[i] = distillvo;
		}
		new BaseDAO().updateVOArray(tillvos);
		return tillvos;
	}

}
