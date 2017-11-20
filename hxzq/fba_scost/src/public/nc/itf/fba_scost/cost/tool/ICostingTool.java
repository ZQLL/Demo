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

public abstract interface ICostingTool
{
  public abstract Map<String, ForwardVO> getFairvaluejz();
  
  public abstract void setFairvaluejz(Map<String, ForwardVO> paramMap);
  
  public abstract Integer getDirection();
  
  public abstract void setDirection(Integer paramInteger);
  
  public abstract String[] getPk_stocksort();
  
  public abstract void setPk_stocksort(String[] paramArrayOfString);
  
  public abstract String getCurrbilltype();
  
  public abstract void setCurrbilltype(String paramString);
  
  public abstract String getCurrdate();
  
  public abstract void setCurrdate(String paramString);
  
  public abstract BilltypeGroupVO getCurrbilltypegroupvo();
  
  public abstract void setCurrbilltypegroupvo(BilltypeGroupVO paramBilltypeGroupVO);
  
  public abstract List<PendingBillVO> getBilltypeclass();
  
  public abstract void setBilltypeclass(List<PendingBillVO> paramList);
  
  public abstract void setCostParaVO(CostParaVO paramCostParaVO);
  
  public abstract CostParaVO getCostParaVO();
  
  public abstract ICostBalanceTool getBalancetool();
  
  public abstract void updateFunds(SuperVO paramSuperVO)
    throws Exception;
  
  public abstract void addTotalwinVO(IVTradedPubVO paramIVTradedPubVO);
  
  public abstract TotalwinVO getNewTotalwinVO(IVTradedPubVO paramIVTradedPubVO);
  
  public abstract boolean isCheckStock();
  
  public abstract void handleException(SuperVO paramSuperVO1, SuperVO paramSuperVO2, String paramString)
    throws BusinessException;
  
  public abstract boolean getIsinit();
  
  public abstract void setIsinit(boolean paramBoolean);
  
  public abstract boolean getIstally();
  
  public abstract void setIstally(boolean paramBoolean);
  
  public abstract void addCostChangeBalanceVO(StockBalanceVO paramStockBalanceVO, String[] paramArrayOfString);
  
  public abstract void saveCostChangeVOs()
    throws Exception;
  
  public abstract List<CostAdjustmentVO> getCostChangeVOs()
    throws Exception;
  
  public abstract List<CostAdjustmentVO> calculateCost(StockBalanceVO paramStockBalanceVO)
    throws Exception;
  
  public abstract CostAdjustmentVO getCostchangeVO(StockBalanceVO paramStockBalanceVO, String[] paramArrayOfString)
    throws Exception;
  
  public abstract UFDouble getSellcost(BanlanceQueryKeyVO paramBanlanceQueryKeyVO, ICostingTool paramICostingTool, UFDouble paramUFDouble, boolean paramBoolean)
    throws Exception;
  
  public abstract UFDouble getTaxOutcost(BanlanceQueryKeyVO paramBanlanceQueryKeyVO, ICostingTool paramICostingTool, UFDouble paramUFDouble)
    throws Exception;
  
  public abstract boolean isCheckFund();
  
  public abstract void setCheckFund(boolean paramBoolean);
  
  public abstract Map<String, ForwardVO> getInterestjz();
  
  public abstract void setInterestjz(Map<String, ForwardVO> paramMap);
}
