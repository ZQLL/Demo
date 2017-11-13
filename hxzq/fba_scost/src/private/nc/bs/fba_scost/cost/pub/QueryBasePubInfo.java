package nc.bs.fba_scost.cost.pub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.HYSuperDMO;
import nc.itf.fba_scost.cost.pub.IQueryBaseInfo;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.vo.fba_scost.cost.checkplan.CheckplanVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.trademarket.TradeMarketVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_scost.scost.approve.ApproveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

public class QueryBasePubInfo implements IQueryBaseInfo{
	/**
	 * 查询成本计算方案
	 */
	public CostPlanVO[] getCostPlanInfo(LoginContext context)throws BusinessException{
		if(context == null)
			return null;
		StringBuffer condition = new StringBuffer();
		condition.append("  pk_org = '"+context.getPk_org()+"' ");
		condition.append("  and pk_group = '"+context.getPk_group()+"' and isnull(dr,0) = 0 ");
		condition.append("  and isbusinessplan = 'Y' ");	
		condition.append("  and pk_glorgbook = '"+AppContextUtil.getDefaultOrgBook()+"' ");
		CostPlanVO[] planvos = (CostPlanVO[])new HYSuperDMO().queryByWhereClause(CostPlanVO.class, condition.toString());
		return planvos;
	}
	
	/**
	 * 查询当前审核日期
	 */
	public UFDate queryInitTallyVO(LoginContext context,String pk_glorgbook,String billtypegroup) throws BusinessException{
		UFDate date = new UFDate(CostConstant.DEFAULT_DATE);
		StringBuffer sb = new StringBuffer();
		sb.append(" select lastapprovedate from scost_approve ");
		sb.append(" where isnull(dr,0)=0 ");
		sb.append(" and islastest='Y' ");
		sb.append(" and pk_group ='"+context.getPk_group()+"'");
		sb.append(" and pk_org ='"+context.getPk_org()+"' ");
		sb.append(" and pk_glorgbook ='"+pk_glorgbook+"' ");
		sb.append(" and billtypegroup ='"+billtypegroup+"' ");
		try{
			date = (UFDate)new BaseDAO().executeQuery(sb.toString(), new ResultSetProcessor (){
				private static final long serialVersionUID = 1L;
				public Object handleResultSet(ResultSet rs) throws SQLException{
					 UFDate date =  new UFDate(CostConstant.DEFAULT_DATE);
					 if(rs.next()){
						 String distill_date =  rs.getString("lastapprovedate");
						 date = distill_date == null ? null : new UFDate(distill_date);
					 }
					 return date;
				 }
			});
		}catch(DAOException e){
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		return date;
	}
	
	/**
	 * 查询证券档案名称
	 */
	public String querySecuritesName(String pk_securities)throws BusinessException{
		String name =  null;
		StringBuffer sf = new StringBuffer();
		sf.append("select name from  sec_securities where pk_securities = '"+pk_securities+"' ");
		try{
			//start 性能优化:查询缓存  2015-06-12 by:mx
//			name = (String)new BaseDAO().executeQuery(sf.toString(), new ResultSetProcessor ()
			name = (String)DBCacheQueryFacade.runQuery(sf.toString(),new ResultSetProcessor()
			//end 性能优化:查询缓存  2015-06-12 by:mx
			{
				private static final long serialVersionUID = 1L;
				public Object handleResultSet(ResultSet rs) throws SQLException{
					 String name = null;
					 if(rs.next()){
						 name =  rs.getString("name");
					 }
					 return name;
				 }
			});
		}catch(Exception e){
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		return name;
	}
	
	/**
	 * 根据传进来的证券档案pk_securities，查询证券档案编码
	 * @param pk_securities
	 * @return code
	 * @throws BusinessException
	 * @author qs
	 * @since 2016-12-14 15:51:35
	 */
	public String querySecuritesCode(String pk_securities)throws BusinessException{
		//定义证券档案编码code
		String code =  null;
		StringBuffer sf = new StringBuffer();
		//根据pk_securities从证券档案表sec_securities中查询code
		sf.append("select code from  sec_securities where pk_securities = '"+pk_securities+"' ");
		try{
			//性能考虑，查询缓存
			code = (String)DBCacheQueryFacade.runQuery(sf.toString(),new ResultSetProcessor()
			{
				private static final long serialVersionUID = 1L;
				public Object handleResultSet(ResultSet rs) throws SQLException{
					 String code = null;
					 if(rs.next()){
						 code =  rs.getString("code");
					 }
					 return code;
				 }
			});
		}catch(Exception e){
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		return code;
	}
	
	
	
	/**
	 * 公允价值使用
	 * 应收利息计提使用
	 * 行情增加联合索引
	 */
	public TradeMarketVO queryLastMarket(String pk_securities,UFDate trade_date)throws BusinessException{
		TradeMarketVO marketvo = null;
		if(pk_securities == null || trade_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		//start 性能优化  2015-06-12 by:mx
		/*sf.append(" select * from sim_trademarket where pk_securities = '"+pk_securities+"' ");
		sf.append(" and trade_date =  ");
		sf.append(" (select isnull(max(trade_date),'"+CostConstant.DEFAULT_DATE+"')  from sim_trademarket where ");
		sf.append(" isnull(dr, 0) = 0 and trade_date <= '"+trade_date.asEnd()+"' ");
		sf.append(" and pk_securities = '"+pk_securities+"') ");
		sf.append(" and isnull(dr, 0) = 0 ");*/
		sf.append(" select * from sim_trademarket where pk_securities = '"+pk_securities+"'");
		sf.append(" and trade_date =  ");
		sf.append(" (select isnull(max(trade_date),'"+CostConstant.DEFAULT_DATE+"')  from sim_trademarket where ");
		sf.append(" isnull(dr, 0) = 0 and trade_date <= '"+trade_date.asEnd()+"' ");
		sf.append(" and pk_securities = '"+pk_securities+"') ");
		sf.append(" and isnull(dr, 0) = 0 ");
		String sql = sf.toString();
		@SuppressWarnings("unchecked")
		List<TradeMarketVO> list = (List<TradeMarketVO>)DBCacheFacade.runQuery(sql, new BeanListProcessor(TradeMarketVO.class));
		//end 2015-06-12 by:mx
		if(list != null && list.size() > 0 ){
			marketvo = list.get(0);
		}
		return marketvo;
	}
	
	/**
	 * 根据参数编码校验-----返回结果仅对布尔类型生效，非布尔类型不得使用
	 * 账薄级参数
	 */
	public boolean getBooleanFromInitcode(String pk_glorgbook,String initcode)throws BusinessException {
		boolean falg = false;
		String va =  null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select value from pub_sysinit where pk_org  = '"+pk_glorgbook+"' " +
				" and initcode = '"+initcode+"' and isnull(dr,0) = 0 ");
		try{
			va = (String)new BaseDAO().executeQuery(sf.toString(), new ResultSetProcessor (){
				private static final long serialVersionUID = 1L;
				public Object handleResultSet(ResultSet rs) throws SQLException{
					 String name = null;
					 if(rs.next()){
						 name =  rs.getString("value");
					 }
					 return name;
				 }
			});
		}catch(DAOException e){
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		if(va == null)
			throw ExceptionHandler.createException("当前组织下，参数:"+initcode+"，没有设置! ");
		if("Y".equals(va))
			falg = true;
		return falg;
	}
	/**
	 * 根据参数编码校验-----返回结果字符串
	 */
	public String getStringFromInitcode(String pk_glorgbook,String initcode)throws BusinessException {
		String va =  null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select value from pub_sysinit where pk_org  = '"+pk_glorgbook+"' " +
				" and initcode = '"+initcode+"' and isnull(dr,0) = 0 ");
		try{
			va = (String)new BaseDAO().executeQuery(sf.toString(), new ResultSetProcessor (){
				private static final long serialVersionUID = 1L;
				public Object handleResultSet(ResultSet rs) throws SQLException{
					 String name = null;
					 if(rs.next()){
						 name =  rs.getString("value");
					 }
					 return name;
				 }
			});
		}catch(DAOException e){
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		if(va == null)
			throw ExceptionHandler.createException("当前组织下，参数:"+initcode+"，没有设置! ");
		return va;
	}
//	/**
//	 * 根据证券查询当前证券行情-----这种效率比较低
//	 */
//	public Map<String,TradeMarketVO> queryMarket(String[] pk_securities,UFDate trade_date) throws BusinessException{
//		if(pk_securities == null || pk_securities.length == 0 || trade_date == null)
//			return null;
//		TradeMarketVO[] vos = null;
//		StringBuffer sf = new StringBuffer();
//		if(pk_securities.length < 1000){
//			sf.append(" isnull(dr, 0) = 0 and trade_date <= '"+trade_date.asEnd()+"' ");
//			String where = CostTools.getInWhereClause(pk_securities);
//			sf.append(" and pk_securities  "+where);
//			sf.append(" order by trade_date desc ");
//			vos = (TradeMarketVO[])new HYSuperDMO().queryByWhereClause(TradeMarketVO.class, sf.toString());
//		}else{//采用临时表
//			String tablename = null;
//			try{
//				tablename = TempTableUtils.getTempTablename("temp_trademarket");
//				tablename = TempTableUtils.createTempTable(tablename," pk_securities varchar(60),ts char(19)", null);
//				TempTableUtils.insertIntoTable(tablename, "pk_securities", pk_securities);
//				sf.append(" select t1.* from sim_trademarket t1 , "+tablename+" t2 ");
//				sf.append(" where t1.pk_securities = t2.pk_securities and isnull(t1.dr, 0) = 0 ");
//				sf.append("  and trade_date <= '"+trade_date.asEnd()+"' order by trade_date desc ");
//				List<TradeMarketVO> list = (List<TradeMarketVO>)new BaseDAO().executeQuery(sf.toString(), new BeanListProcessor(TradeMarketVO.class));
//				vos = list.toArray(new TradeMarketVO[0]);
//			}catch(SQLException e){
//				throw ExceptionHandler.handleException(this.getClass(),e);
//			}finally{
//				try{
//					TempTableUtils.dropTempTable(tablename);
//				}catch(SQLException e){
//					throw ExceptionHandler.handleException(this.getClass(),e);
//				}
//			}
//		}
//		if(vos == null || vos.length == 0)
//			return null;
//		Map<String,TradeMarketVO> map = new HashMap<String, TradeMarketVO>();
//		for(TradeMarketVO v : vos){
//			if(!map.containsKey(v.getPk_securities())){
//				map.put(v.getPk_securities(), v);
//			}
//		}
//		return map;
//	}

	@Override
	public ApproveVO[] getLastapprovedate(LoginContext context) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append(" isnull(dr,0)=0 ");
		sb.append(" and islastest='Y' ");
		sb.append(" and pk_group ='"+context.getPk_group()+"'");
		sb.append(" and pk_org ='"+context.getPk_org()+"' ");
		sb.append(" and pk_glorgbook ='"+AppContextUtil.getDefaultOrgBook()+"' ");
		ApproveVO[] vos = (ApproveVO[])new HYSuperDMO().queryByWhereClause(ApproveVO.class, sb.toString());
		return vos;
	}

	@Override
	public CheckplanVO getCheckplanVO(LoginContext context,String pk_transtypecode)throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append(" isnull(dr,0)=0 ");
		sb.append(" and pk_group = '"+context.getPk_group()+"'");
		sb.append(" and pk_org = '"+context.getPk_org()+"' ");
		sb.append(" and pk_glorgbook = '"+AppContextUtil.getDefaultOrgBook()+"' ");
		sb.append(" and pk_billtype = '"+pk_transtypecode+"' ");
		CheckplanVO[] vos = (CheckplanVO[])new HYSuperDMO().queryByWhereClause(CheckplanVO.class, sb.toString());
		if(vos == null || vos.length == 0)
			throw new BusinessException("当前组织审核方案无当前交易类型，请确认！ ");
		return vos[0];
	}
}
