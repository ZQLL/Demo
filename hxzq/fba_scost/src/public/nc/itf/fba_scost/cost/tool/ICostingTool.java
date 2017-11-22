package nc.itf.fba_scost.cost.tool;

import java.util.List;
import java.util.Map;

import nc.itf.fba_scost.cost.billvo.IVTradedPubVO;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.ForwardVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.totalwin.TotalwinVO;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.fba_scost.trade.costadjustment.CostAdjustmentVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public interface ICostingTool {
	public Map<String, ForwardVO> getFairvaluejz();

	public void setFairvaluejz(Map<String, ForwardVO> fairvaluejz);

	public Integer getDirection();

	public void setDirection(Integer direction);

	public String[] getPk_stocksort();

	public void setPk_stocksort(String[] pk_stocksort);

	public String getCurrbilltype();

	public void setCurrbilltype(String currbilltype);

	public String getCurrdate();

	public void setCurrdate(String currdate);

	public BilltypeGroupVO getCurrbilltypegroupvo();

	public void setCurrbilltypegroupvo(BilltypeGroupVO currbilltypegroupvo);

	public List<PendingBillVO> getBilltypeclass();

	public void setBilltypeclass(List<PendingBillVO> billtypeclass);

	public void setCostParaVO(CostParaVO costParaVO);

	public CostParaVO getCostParaVO();

	public ICostBalanceTool getBalancetool();

	/**
	 * �����ʽ� direction=0 �������룩 =1 ����֧����
	 * 
	 * @param tradevo
	 * @param direction
	 * @throws Exception
	 * @author libin
	 * @date 2012-10-16 ����8:06:33
	 */
	public void updateFunds(SuperVO tradevo) throws Exception;

	public void addTotalwinVO(IVTradedPubVO tradevo);

	public TotalwinVO getNewTotalwinVO(IVTradedPubVO tradevo);

	public boolean isCheckStock();

	/**
	 * ������Ϣͳһ��������ֻ����ҵ����Ϣ
	 * 
	 * @param vo
	 * @param bvo
	 * @param error
	 * @throws BusinessException
	 */
	public void handleException(SuperVO vo, SuperVO bvo, String error)
			throws BusinessException;

	public boolean getIsinit();

	public void setIsinit(boolean isinit);

	public boolean getIstally();

	public void setIstally(boolean istally);

	/**
	 * ���������ɱ��������Ŀ��
	 * 
	 * @param stockbalancevo
	 * @param costFieldArray
	 * @author liangwei
	 */
	public void addCostChangeBalanceVO(StockBalanceVO stockbalancevo,
			String[] costFieldArray);

	public void saveCostChangeVOs() throws Exception;

	/**
	 * 
	 * ���ؼ����ĳɱ������� ��57�Ĳ��죺���ﲻ�����ڳ����ܲ����ĳɱ��������ù��ܷ����ڳ����˴���
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CostAdjustmentVO> getCostChangeVOs() throws Exception;

	/**
	 * ����ɱ��������������ɳɱ�������
	 * 
	 * @param balancevo
	 * @return
	 * @throws Exception
	 *             List<CostAdjustmentVO> TODO������˵����
	 */
	public List<CostAdjustmentVO> calculateCost(StockBalanceVO balancevo)
			throws Exception;

	/**
	 * ���ɳɱ��䶯��
	 * 
	 * @param balancevo
	 * @param costFieldArray
	 * @return
	 * @throws Exception
	 */
	public CostAdjustmentVO getCostchangeVO(StockBalanceVO balancevo,
			String[] costFieldArray) throws Exception;

	/**
	 * �������۳ɱ�
	 * 
	 * @param trade_date
	 * @param pk_assetsprop
	 * @param pk_stocksort
	 * @param key
	 * @param bargain_num
	 * @param ishaszj
	 * @return
	 * @throws Exception
	 *             UFDouble TODO������˵����
	 */
	public UFDouble getSellcost(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, UFDouble bargain_num, boolean ishaszj)
			throws Exception;

	/**
	 * ������Ȩר��
	 * 
	 * @param bargain_num
	 * @param stockbalancevo
	 * @return
	 */
	public UFDouble getSellcostForOptionsTrade(UFDouble bargain_num,
			StockBalanceVO stockbalancevo) throws Exception;

	/**
	 * ˰��ת����
	 * 
	 * @param trade_date
	 * @param pk_assetsprop
	 * @param pk_stocksort
	 * @param key
	 * @param bargain_num
	 * @return
	 * @throws Exception
	 *             UFDouble TODO������˵����
	 */
	public UFDouble getTaxOutcost(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, UFDouble bargain_num) throws Exception;

	public boolean isCheckFund();

	public void setCheckFund(boolean isCheckFund);

	public Map<String, ForwardVO> getInterestjz();

	public void setInterestjz(Map<String, ForwardVO> interestjz);

}
