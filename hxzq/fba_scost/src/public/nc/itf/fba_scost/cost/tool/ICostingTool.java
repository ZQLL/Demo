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
	 * 处理资金 direction=0 正向（收入） =1 反向（支出）
	 * 
	 * @param tradevo
	 * @param direction
	 * @throws Exception
	 * @author libin
	 * @date 2012-10-16 下午8:06:33
	 */
	public void updateFunds(SuperVO tradevo) throws Exception;

	public void addTotalwinVO(IVTradedPubVO tradevo);

	public TotalwinVO getNewTotalwinVO(IVTradedPubVO tradevo);

	public boolean isCheckStock();

	/**
	 * 错误信息统一处理，这里只处理业务信息
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
	 * 缓存会产生成本调整单的库存
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
	 * 返回计算后的成本调整单 与57的差异：这里不处理期初可能产生的成本调整，该功能放在期初记账处理
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CostAdjustmentVO> getCostChangeVOs() throws Exception;

	/**
	 * 计算成本调整单，并生成成本调整单
	 * 
	 * @param balancevo
	 * @return
	 * @throws Exception
	 *             List<CostAdjustmentVO> TODO（参数说明）
	 */
	public List<CostAdjustmentVO> calculateCost(StockBalanceVO balancevo)
			throws Exception;

	/**
	 * 生成成本变动单
	 * 
	 * @param balancevo
	 * @param costFieldArray
	 * @return
	 * @throws Exception
	 */
	public CostAdjustmentVO getCostchangeVO(StockBalanceVO balancevo,
			String[] costFieldArray) throws Exception;

	/**
	 * 计算销售成本
	 * 
	 * @param trade_date
	 * @param pk_assetsprop
	 * @param pk_stocksort
	 * @param key
	 * @param bargain_num
	 * @param ishaszj
	 * @return
	 * @throws Exception
	 *             UFDouble TODO（参数说明）
	 */
	public UFDouble getSellcost(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, UFDouble bargain_num, boolean ishaszj)
			throws Exception;

	/**
	 * 个股期权专用
	 * 
	 * @param bargain_num
	 * @param stockbalancevo
	 * @return
	 */
	public UFDouble getSellcostForOptionsTrade(UFDouble bargain_num,
			StockBalanceVO stockbalancevo) throws Exception;

	/**
	 * 税金转出额
	 * 
	 * @param trade_date
	 * @param pk_assetsprop
	 * @param pk_stocksort
	 * @param key
	 * @param bargain_num
	 * @return
	 * @throws Exception
	 *             UFDouble TODO（参数说明）
	 */
	public UFDouble getTaxOutcost(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, UFDouble bargain_num) throws Exception;

	public boolean isCheckFund();

	public void setCheckFund(boolean isCheckFund);

	public Map<String, ForwardVO> getInterestjz();

	public void setInterestjz(Map<String, ForwardVO> interestjz);

}
