package nc.itf.fba_sjll.sjll.calcrealrate;

import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFLiteralDate;
/**
 * Ϊ�˳��̲��������
 * scost,sjll��ģ������ͬ�Ľӿڡ�ICalcPluginMaintain���ࡣ
 *
 */
public interface ICalcPluginMaintain {
	/**
	 * ȡ�����У��
	 */
	public void unAuditcheck(String pk_group,String pk_org,UFLiteralDate date)throws BusinessException;
	
	/**
	 * ����������
	 */
	public void buyPlugindeal(SuperVO stocktrade) throws BusinessException;
	/**
	 * ȡ������������
	 */
	public void unBuyPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	
	/**
	 * ���� �������
	 */
	public void sellPlugindeal(StockBalanceVO stockbalancevo,SuperVO stocktrade) throws BusinessException;
	/**
	 * ȡ������ �������
	 */
	public void unSellPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	
	/**
	 * ��Ϣ�������
	 */
	public void fxPlugindeal(SuperVO stocktrade) throws BusinessException;
	/**
	 * ȡ����Ϣ�������
	 */
	public void unFxPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	
	/**
	 * ת�йܲ������
	 */
	public void ztgPlugindeal(StockBalanceVO stockbalancevo,SuperVO tradevo) throws BusinessException;
	/**
	 * ȡ��ת�йܲ������
	 */
	public void unZtgPlugindeal(SuperVO tradevo,String pk_group,String pk_org,String tradedate) throws BusinessException;
	/**
	 * ���ʼ�ֵȡ��
	 */
	public void setFairvalueByRate(UFLiteralDate tradedate,StockBalanceVO sbvo,String pk_group,String pk_org) throws BusinessException;
	/**
	 * ���ʼ�ֵУ��
	 */
	public void fairCheck(String pk_group,String pk_org, String distill_date) throws BusinessException;
}