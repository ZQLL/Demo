package nc.itf.fba_sjll.sjll.calcrealrate;

import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFLiteralDate;
/**
 * 为了出盘不报错，因此
 * scost,sjll两模块有相同的接口【ICalcPluginMaintain】类。
 *
 */
public interface ICalcPluginMaintain {
	/**
	 * 取消审核校验
	 */
	public void unAuditcheck(String pk_group,String pk_org,UFLiteralDate date)throws BusinessException;
	
	/**
	 * 买入插件处理
	 */
	public void buyPlugindeal(SuperVO stocktrade) throws BusinessException;
	/**
	 * 取消买入插件处理
	 */
	public void unBuyPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	
	/**
	 * 卖出 插件处理
	 */
	public void sellPlugindeal(StockBalanceVO stockbalancevo,SuperVO stocktrade) throws BusinessException;
	/**
	 * 取消卖出 插件处理
	 */
	public void unSellPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	
	/**
	 * 付息插件处理
	 */
	public void fxPlugindeal(SuperVO stocktrade) throws BusinessException;
	/**
	 * 取消付息插件处理
	 */
	public void unFxPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	
	/**
	 * 转托管插件处理
	 */
	public void ztgPlugindeal(StockBalanceVO stockbalancevo,SuperVO tradevo) throws BusinessException;
	/**
	 * 取消转托管插件处理
	 */
	public void unZtgPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	/**
	 * 公允价值取数
	 */
	public void setFairvalueByRate(UFLiteralDate tradedate,StockBalanceVO sbvo,String pk_group,String pk_org) throws BusinessException;
	/**
	 * 公允价值校验
	 */
	public void fairCheck(String pk_group,String pk_org, String distill_date) throws BusinessException;
}