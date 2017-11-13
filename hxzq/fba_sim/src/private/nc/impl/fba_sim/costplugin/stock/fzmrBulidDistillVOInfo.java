package nc.impl.fba_sim.costplugin.stock;

import java.lang.reflect.Array;
import java.util.Collection;

import nc.bs.framework.common.NCLocator;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.ui.dbcache.DBCacheFacade;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 构造应收利息计提VO信息
 *
 */
public class fzmrBulidDistillVOInfo {

/*	*//**
	 * 根据库存构造应收利息计提单据
	 *//*
	protected InterestdistillVO buildInterestdistillVO(LoginContext context,UFDate trade_date,
			StockBalanceVO vs,UFDouble qm,UFDouble qc) throws BusinessException{
		InterestdistillVO distilvo = new InterestdistillVO();
		distilvo.setBillmaker(AppContext.getInstance().getPkUser());
		distilvo.setMakedate(AppContext.getInstance().getServerTime());
		distilvo.setCreationtime(AppContext.getInstance().getServerTime());
		distilvo.setCreator(AppContext.getInstance().getPkUser());
		distilvo.setDistill_date(new UFDate(trade_date.toLocalString()));
		String pk_group = context.getPk_group();
		String pk_org = context.getPk_org();
		distilvo.setBillno(getBillNO(pk_group,pk_org,distilvo));
		distilvo.setGenstate(CostConstant.INTEREST_GENSTATE.JITI.getIndex());
		distilvo.setState(SystemConst.statecode[1]);
		distilvo.setStocks_num(vs.getStocks_num());
		distilvo.setStocks_sum(vs.getStocks_sum());
		distilvo.setNqclx(qc);
		distilvo.setNqmlx(qm);
		distilvo.setInterestdistill(SafeCompute.sub(qm, qc));
		distilvo.setPk_costplan(vs.getPk_costplan());
		distilvo.setPk_glorgbook(vs.getPk_glorgbook());
		distilvo.setPk_assetsprop(vs.getPk_assetsprop());
		distilvo.setPk_stocksort(vs.getPk_stocksort());
		distilvo.setPk_capaccount(vs.getPk_capaccount());
		distilvo.setPk_client(vs.getPk_client());
		distilvo.setPk_operatesite(vs.getPk_operatesite());
		distilvo.setPk_partnaccount(vs.getPk_partnaccount());
		distilvo.setPk_selfsgroup(vs.getPk_selfsgroup());
		distilvo.setPk_securities(vs.getPk_securities());
		distilvo.setPk_group(vs.getPk_group());
		distilvo.setPk_org(vs.getPk_org());
		distilvo.setPk_transtype(getBilltypePK(CostConstant.CJT_BILLTYPE));
		distilvo.setTranstypecode(CostConstant.CJT_BILLTYPE);
		distilvo.setPk_billtype(getBilltypePK(CostConstant.CJT_BILLTYPE));
		distilvo.setBilltypecode(CostConstant.CJT_BILLTYPE);
		distilvo.setAttributeValue("dr", 0);
//		distilvo.setPk_corp(pk_corp);
//		distilvo.setPk_interestdistill(pk_interestdistill);
//		distilvo.setPk_org_v(pk_org_v);
//		distilvo.setPk_voucher(pk_voucher);
//		distilvo.setApprovedate(approvedate);
//		distilvo.setApprover(approver);
//		distilvo.setModifiedtime(modifiedtime);
//		distilvo.setModifier(modifier);
		return distilvo;
	}*/
	/**
	 * 根据库存构造应收利息计提单据
	 * 20150615性能优化by:mx
	 */
	protected InterestdistillVO buildInterestdistillVO(UFDate trade_date,
			StockBalanceVO vs,UFDouble qm,UFDouble qc,int order,String[] vbillcodes,String billtype) throws BusinessException{
		InterestdistillVO distilvo = new InterestdistillVO();
		distilvo.setBillmaker(AppContext.getInstance().getPkUser());
		distilvo.setMakedate(AppContext.getInstance().getServerTime());
		distilvo.setCreationtime(AppContext.getInstance().getServerTime());
		distilvo.setCreator(AppContext.getInstance().getPkUser());
		distilvo.setDistill_date(new UFDate(trade_date.toLocalString()));
		distilvo.setBillno(getBillNO(vbillcodes, order));
		distilvo.setGenstate(CostConstant.INTEREST_GENSTATE.JITI.getIndex());
		distilvo.setState(SystemConst.statecode[1]);
		distilvo.setStocks_num(vs.getStocks_num());
		distilvo.setStocks_sum(vs.getStocks_sum());
		distilvo.setNqclx(qc);
		distilvo.setNqmlx(qm);
		distilvo.setInterestdistill(SafeCompute.sub(qm, qc));
		distilvo.setPk_costplan(vs.getPk_costplan());
		distilvo.setPk_glorgbook(vs.getPk_glorgbook());
		distilvo.setPk_assetsprop(vs.getPk_assetsprop());
		distilvo.setPk_stocksort(vs.getPk_stocksort());
		distilvo.setPk_capaccount(vs.getPk_capaccount());
		distilvo.setPk_client(vs.getPk_client());
		distilvo.setPk_operatesite(vs.getPk_operatesite());
		distilvo.setPk_partnaccount(vs.getPk_partnaccount());
		distilvo.setPk_selfsgroup(vs.getPk_selfsgroup());
		distilvo.setPk_securities(vs.getPk_securities());
		distilvo.setPk_group(vs.getPk_group());
		distilvo.setPk_org(vs.getPk_org());
		distilvo.setPk_transtype(billtype);
		distilvo.setTranstypecode(CostConstant.CJT_BILLTYPE);
		distilvo.setPk_billtype(billtype);
		distilvo.setBilltypecode(CostConstant.CJT_BILLTYPE);
		distilvo.setAttributeValue("dr", 0);
		return distilvo;
	}
	/**
	 * 构造应收利息计提单据---转出、买入、卖出
	 */
	public InterestdistillVO buildInterestdistill(ICostingTool costingtool,	StockBalanceVO stockbalancevo,ITrade_Data vs) 
			throws BusinessException{
		InterestdistillVO distilvo = new InterestdistillVO();
		distilvo.setBillmaker(AppContext.getInstance().getPkUser());
		distilvo.setMakedate(AppContext.getInstance().getServerTime());
		distilvo.setCreationtime(AppContext.getInstance().getServerTime());
		distilvo.setCreator(AppContext.getInstance().getPkUser());
		distilvo.setDistill_date(new UFDate(vs.getTrade_date().toLocalString()));
		String pk_group = costingtool.getCostParaVO().getCheckParavo().getPk_group();
		String pk_org = costingtool.getCostParaVO().getCheckParavo().getPk_org();
		distilvo.setBillno(getBillNO(pk_group,pk_org,distilvo));
		distilvo.setGenstate(getGenstate(vs));//单据生成状态
		distilvo.setState(SystemConst.statecode[1]);
		distilvo.setStocks_num(vs.getBargain_num().sub(vs.getBargain_num()==null?new UFDouble(0):vs.getBargain_num()));
		distilvo.setStocks_sum(null);
		distilvo.setInterestdistill(getLxRecord(vs));
		distilvo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo().getPk_costplan());
		distilvo.setPk_glorgbook(vs.getPk_glorgbook());
		distilvo.setPk_assetsprop(vs.getPk_assetsprop());
		distilvo.setPk_stocksort(vs.getPk_stocksort());
		distilvo.setPk_capaccount(vs.getPk_capaccount());
		distilvo.setPk_client(vs.getPk_client());
		distilvo.setPk_operatesite(vs.getPk_operatesite());
		distilvo.setPk_partnaccount(vs.getPk_partnaccount());
		distilvo.setPk_selfsgroup(vs.getPk_selfsgroup());
		distilvo.setPk_securities(vs.getPk_securities());
		distilvo.setPk_group(vs.getPk_group());
		distilvo.setPk_org(vs.getPk_org());
		distilvo.setPk_transtype(vs.getPk_transtype());
		distilvo.setTranstypecode(vs.getTranstypecode());
		distilvo.setPk_billtype(vs.getPk_billtype());
		distilvo.setBilltypecode(vs.getBilltypecode());
		distilvo.setAttributeValue("dr", 0);
		return distilvo;
	}
	/**
	 * 构造应收利息计提单据---转入
	 */
	public InterestdistillVO buildInterestdistillVO_in(ICostingTool costingtool,ITrade_Data vs) 
			throws BusinessException{
		InterestdistillVO distilvo = new InterestdistillVO();
		distilvo.setBillmaker(AppContext.getInstance().getPkUser());
		distilvo.setMakedate(AppContext.getInstance().getServerTime());
		distilvo.setCreationtime(AppContext.getInstance().getServerTime());
		distilvo.setCreator(AppContext.getInstance().getPkUser());
		distilvo.setDistill_date(new UFDate(vs.getTrade_date().toLocalString()));
		String pk_group = costingtool.getCostParaVO().getCheckParavo().getPk_group();
		String pk_org = costingtool.getCostParaVO().getCheckParavo().getPk_org();
		distilvo.setBillno(getBillNO(pk_group,pk_org,distilvo));
		distilvo.setGenstate(CostConstant.INTEREST_GENSTATE.ZHUANRU.getIndex());//转入
		distilvo.setState(SystemConst.statecode[1]);
		distilvo.setStocks_num(vs.getHr_bargain_num());//转入数量
		distilvo.setStocks_sum(null);
		distilvo.setInterestdistill(vs.getInterest());
		distilvo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo().getPk_costplan());
		distilvo.setPk_glorgbook(vs.getPk_glorgbook());
		distilvo.setPk_assetsprop(vs.getHr_pk_assetsprop());
		distilvo.setPk_stocksort(vs.getHr_pk_stocksort());
		distilvo.setPk_capaccount(vs.getHr_pk_capaccount());
		distilvo.setPk_client(vs.getHr_pk_client());
		distilvo.setPk_operatesite(vs.getHr_pk_operatesite());
		distilvo.setPk_partnaccount(vs.getHr_pk_partnaccount());
		distilvo.setPk_selfsgroup(vs.getHr_pk_selfsgroup());
		distilvo.setPk_securities(vs.getHr_pk_securities());//转入证券
		distilvo.setPk_group(vs.getPk_group());
		distilvo.setPk_org(vs.getPk_org());
		distilvo.setPk_transtype(vs.getPk_transtype());
		distilvo.setTranstypecode(vs.getTranstypecode());
		distilvo.setPk_billtype(vs.getPk_billtype());
		distilvo.setBilltypecode(vs.getBilltypecode());
		distilvo.setAttributeValue("dr", 0);
		return distilvo;
	}
	
	public String getBillNO(String pk_group ,String pk_org ,InterestdistillVO fv)throws BusinessException{
		String vbillcode = null;
		try {
			IBillcodeManage ser = NCLocator.getInstance().lookup(IBillcodeManage.class);
			vbillcode = ser.getBillCode_RequiresNew(CostConstant.CJT_BILLTYPE, pk_group,pk_org, fv);
		} catch (BusinessException be) {
			ExceptionHandler.handleException(be);
		}
		return vbillcode;
	}
	/**
	 * 20150615性能优化by:mx
	 * 在数据量大的时候根据所要计提的单据数量批量生成单据号 避免重复查询数据库
	 */
	public String[] getBillNOs(String pk_group ,String pk_org ,InterestdistillVO fv,int length)throws BusinessException{
		String[] vbillcodes = null;
		try {
			IBillcodeManage ser = NCLocator.getInstance().lookup(IBillcodeManage.class);
			vbillcodes = ser.getBatchBillCodes_RequiresNew(CostConstant.CJT_BILLTYPE, pk_group,pk_org, fv,length);
		} catch (BusinessException be) {
			ExceptionHandler.handleException(be);
		}
		return vbillcodes;
	}
	/**
	 * 20150615性能优化by:mx
	 * @param vbillcodes 批量获得的单据号
	 * @param order 
	 * @return
	 * @throws BusinessException
	 */
	public String getBillNO(String[] vbillcodes,int order)throws BusinessException{
		String billno = vbillcodes[order];
		return billno;
	}
	
	public UFDouble getLxRecord(ITrade_Data vs){
		UFDouble z = new UFDouble(0);
		if(CostConstant.BUY_TRANSTYPE.equals(vs.getTranstypecode())){//买入
			z = vs.getAccrual_sum();
		}else{
			z = vs.getInterest();
		}
		return z;
	}
	
	public int getGenstate(ITrade_Data vs){
		int index = -1;
		if(CostConstant.BUY_TRANSTYPE.equals(vs.getTranstypecode())){//买入
			index = CostConstant.INTEREST_GENSTATE.ZHUANRU.getIndex();
		}else{
			index = CostConstant.INTEREST_GENSTATE.ZHUANCHU.getIndex();
		}
		return index;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getBilltypePK(String pk_billtype)throws BusinessException{
		//start 20150615 性能优化 by:mx
//		String where = " pk_billtypecode = '"+pk_billtype+"' and isnull(dr,0) = 0 ";
//		BilltypeVO[] typevos = (BilltypeVO[])new HYSuperDMO().queryByWhereClause(BilltypeVO.class, where);
		String sql = "SELECT * FROM BD_BILLTYPE WHERE PK_BILLTYPECODE = '"+pk_billtype+"' and isnull(dr,0) = 0";
		Collection values = (Collection)DBCacheFacade
				.runQuery(sql, null, new BeanListProcessor(
						BilltypeVO.class));
		SuperVO[] superVos = (SuperVO[]) values.toArray((SuperVO[]) Array.newInstance(BilltypeVO.class, 0));
		BilltypeVO[] typevos = (BilltypeVO[])superVos;
		//end 20150615 性能优化 by:mx
		if(typevos == null || typevos.length == 0)
			throw new BusinessException(" 应收利息单据类型["+CostConstant.CJT_BILLTYPE+"]没有在系统里注册 ! ");
		return typevos[0].getPk_billtypeid();
	}
}
