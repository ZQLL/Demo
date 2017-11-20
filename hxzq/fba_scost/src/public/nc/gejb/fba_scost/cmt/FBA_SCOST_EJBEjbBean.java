package nc.gejb.fba_scost.cmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nc.bs.framework.server._ServerForEJBNCLocator;
import nc.bs.pub.BusinessObject;
import nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup;
import nc.itf.fba_scost.cost.billcheck.IBillCheckService;
import nc.itf.fba_scost.cost.billcheck.IBillMakeJTD;
import nc.itf.fba_scost.cost.pub.IQueryBaseInfo;
import nc.itf.fba_scost.cost.pub.ISysInit;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.itf.fba_scost.cost.tool.ICostForward;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.checkplan.CheckplanVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.IPubQuery;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.scost.approve.ApproveVO;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uif2.LoginContext;

public class FBA_SCOST_EJBEjbBean
  extends BusinessObject
{
  private Object IBillMakeJTD_1897766058;
  private Object IBillCheckService_1105286915;
  private Object IBillCheckByGroup_564614986;
  private Object IPubQuery_746898886;
  private Object ISysInit_1252688754;
  private Object ICostForward_820423736;
  private Object IQueryBaseInfo_369929668;
  private HashMap expMap = new HashMap();
  
  public void ejbActivate() {}
  
  public void ejbPassivate() {}
  
  public void ejbRemove() {}
  
  public void ejbCreate()
  {
    if (this.IBillMakeJTD_1897766058 == null) {
      try
      {
        this.IBillMakeJTD_1897766058 = _ServerForEJBNCLocator.lookup("nc.itf.fba_scost.cost.billcheck.IBillMakeJTD");
      }
      catch (RuntimeException thr)
      {
        this.expMap.put("nc.itf.fba_scost.cost.billcheck.IBillMakeJTD", thr);
      }
    }
    if (this.IBillCheckService_1105286915 == null) {
      try
      {
        this.IBillCheckService_1105286915 = _ServerForEJBNCLocator.lookup("nc.itf.fba_scost.cost.billcheck.IBillCheckService");
      }
      catch (RuntimeException thr)
      {
        this.expMap.put("nc.itf.fba_scost.cost.billcheck.IBillCheckService", thr);
      }
    }
    if (this.IBillCheckByGroup_564614986 == null) {
      try
      {
        this.IBillCheckByGroup_564614986 = _ServerForEJBNCLocator.lookup("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup");
      }
      catch (RuntimeException thr)
      {
        this.expMap.put("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup", thr);
      }
    }
    if (this.IPubQuery_746898886 == null) {
      try
      {
        this.IPubQuery_746898886 = _ServerForEJBNCLocator.lookup("nc.vo.fba_scost.cost.pub.IPubQuery");
      }
      catch (RuntimeException thr)
      {
        this.expMap.put("nc.vo.fba_scost.cost.pub.IPubQuery", thr);
      }
    }
    if (this.ISysInit_1252688754 == null) {
      try
      {
        this.ISysInit_1252688754 = _ServerForEJBNCLocator.lookup("nc.itf.fba_scost.cost.pub.ISysInit");
      }
      catch (RuntimeException thr)
      {
        this.expMap.put("nc.itf.fba_scost.cost.pub.ISysInit", thr);
      }
    }
    if (this.ICostForward_820423736 == null) {
      try
      {
        this.ICostForward_820423736 = _ServerForEJBNCLocator.lookup("nc.itf.fba_scost.cost.tool.ICostForward");
      }
      catch (RuntimeException thr)
      {
        this.expMap.put("nc.itf.fba_scost.cost.tool.ICostForward", thr);
      }
    }
    if (this.IQueryBaseInfo_369929668 == null) {
      try
      {
        this.IQueryBaseInfo_369929668 = _ServerForEJBNCLocator.lookup("nc.itf.fba_scost.cost.pub.IQueryBaseInfo");
      }
      catch (RuntimeException thr)
      {
        this.expMap.put("nc.itf.fba_scost.cost.pub.IQueryBaseInfo", thr);
      }
    }
  }
  
  public void MakeJTD(CheckParaVO arg0)
    throws BusinessException
  {
    if (this.IBillMakeJTD_1897766058 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillMakeJTD"));
    }
    ((IBillMakeJTD)this.IBillMakeJTD_1897766058).MakeJTD(arg0);
  }
  
  public void DeleteJTD(CheckParaVO arg0)
    throws BusinessException
  {
    if (this.IBillMakeJTD_1897766058 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillMakeJTD"));
    }
    ((IBillMakeJTD)this.IBillMakeJTD_1897766058).DeleteJTD(arg0);
  }
  
  public boolean getBooleanFromInitcode(String arg0, String arg1)
    throws BusinessException
  {
    if (this.IBillMakeJTD_1897766058 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillMakeJTD"));
    }
    return ((IBillMakeJTD)this.IBillMakeJTD_1897766058).getBooleanFromInitcode(arg0, arg1);
  }
  
  public void tallyAllCheckedData(CheckParaVO arg0)
    throws BusinessException
  {
    if (this.IBillCheckService_1105286915 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckService"));
    }
    ((IBillCheckService)this.IBillCheckService_1105286915).tallyAllCheckedData(arg0);
  }
  
  public String checkBill(CheckParaVO arg0)
    throws BusinessException
  {
    if (this.IBillCheckService_1105286915 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckService"));
    }
    return ((IBillCheckService)this.IBillCheckService_1105286915).checkBill(arg0);
  }
  
  public String unCheckBill(CheckParaVO arg0)
    throws BusinessException
  {
    if (this.IBillCheckService_1105286915 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckService"));
    }
    return ((IBillCheckService)this.IBillCheckService_1105286915).unCheckBill(arg0);
  }
  
  public void tallyOneGroup_RequiresNew(BilltypeGroupVO arg0, Set<UFDate> arg1, Map<String, List<PendingBillVO>> arg2, CheckParaVO arg3, TradeDataTool arg4, CheckClassTool arg5)
    throws BusinessException
  {
    if (this.IBillCheckByGroup_564614986 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup"));
    }
    ((IBillCheckByGroup)this.IBillCheckByGroup_564614986).tallyOneGroup_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5);
  }
  
  public void uncheckOneGroup_RequiresNew(BilltypeGroupVO arg0, Set<UFDate> arg1, Map<String, List<PendingBillVO>> arg2, Map<String, List<PendingBillVO>> arg3, CheckParaVO arg4, TradeDataTool arg5, CheckClassTool arg6)
    throws BusinessException
  {
    if (this.IBillCheckByGroup_564614986 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup"));
    }
    ((IBillCheckByGroup)this.IBillCheckByGroup_564614986).uncheckOneGroup_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
  }
  
  public void checkOneGroup_RequiresNew(BilltypeGroupVO arg0, Set<UFDate> arg1, Map<String, List<PendingBillVO>> arg2, CheckParaVO arg3, TradeDataTool arg4, CheckClassTool arg5, List<CostPlanVO> arg6, boolean arg7, boolean arg8)
    throws BusinessException
  {
    if (this.IBillCheckByGroup_564614986 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup"));
    }
    ((IBillCheckByGroup)this.IBillCheckByGroup_564614986).checkOneGroup_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }
  
  public void updateMiddleAndApporveUI_RequiresNew(Map<String, List<PendingBillVO>> arg0, String arg1, int arg2, String arg3)
    throws BusinessException
  {
    if (this.IBillCheckByGroup_564614986 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup"));
    }
    ((IBillCheckByGroup)this.IBillCheckByGroup_564614986).updateMiddleAndApporveUI_RequiresNew(arg0, arg1, arg2, arg3);
  }
  
  public void updateApporveUI_RequiresNew(BilltypeGroupVO arg0, CheckParaVO arg1, String arg2, Integer arg3)
    throws BusinessException
  {
    if (this.IBillCheckByGroup_564614986 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup"));
    }
    ((IBillCheckByGroup)this.IBillCheckByGroup_564614986).updateApporveUI_RequiresNew(arg0, arg1, arg2, arg3);
  }
  
  public void Before_RequiresNew(BilltypeGroupVO arg0, CheckParaVO arg1, TradeDataTool arg2)
    throws BusinessException
  {
    if (this.IBillCheckByGroup_564614986 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup"));
    }
    ((IBillCheckByGroup)this.IBillCheckByGroup_564614986).Before_RequiresNew(arg0, arg1, arg2);
  }
  
  public String queryAccountingBookIDByFinanceOrgIDWithMainAssetBook(String arg0)
    throws BusinessException
  {
    if (this.IPubQuery_746898886 == null) {
      throw ((RuntimeException)this.expMap.get("nc.vo.fba_scost.cost.pub.IPubQuery"));
    }
    return ((IPubQuery)this.IPubQuery_746898886).queryAccountingBookIDByFinanceOrgIDWithMainAssetBook(arg0);
  }
  
  public String queryTallyVO(String arg0, String arg1, String arg2)
    throws BusinessException
  {
    if (this.IPubQuery_746898886 == null) {
      throw ((RuntimeException)this.expMap.get("nc.vo.fba_scost.cost.pub.IPubQuery"));
    }
    return ((IPubQuery)this.IPubQuery_746898886).queryTallyVO(arg0, arg1, arg2);
  }
  
  public ApproveVO getApproveVO(String arg0, String arg1, String arg2, String arg3)
    throws Exception
  {
    if (this.ISysInit_1252688754 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.ISysInit"));
    }
    return ((ISysInit)this.ISysInit_1252688754).getApproveVO(arg0, arg1, arg2, arg3);
  }
  
  public void clearSysInit(String arg0)
    throws Exception
  {
    if (this.ISysInit_1252688754 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.ISysInit"));
    }
    ((ISysInit)this.ISysInit_1252688754).clearSysInit(arg0);
  }
  
  public String getSysInitValue(String arg0)
    throws Exception
  {
    if (this.ISysInit_1252688754 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.ISysInit"));
    }
    return ((ISysInit)this.ISysInit_1252688754).getSysInitValue(arg0);
  }
  
  public String getSysInitValue(String arg0, String arg1)
    throws Exception
  {
    if (this.ISysInit_1252688754 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.ISysInit"));
    }
    return ((ISysInit)this.ISysInit_1252688754).getSysInitValue(arg0, arg1);
  }
  
  public UFDouble calcInterest(ICostingTool arg0, ITrade_Data arg1)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    return ((ICostForward)this.ICostForward_820423736).calcInterest(arg0, arg1);
  }
  
  public UFDouble forwardInterestDistill(ICostingTool arg0, StockBalanceVO arg1, ITrade_Data arg2)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    return ((ICostForward)this.ICostForward_820423736).forwardInterestDistill(arg0, arg1, arg2);
  }
  
  public void saveInterestDistill(ICostingTool arg0, ITrade_Data arg1)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    ((ICostForward)this.ICostForward_820423736).saveInterestDistill(arg0, arg1);
  }
  
  public void clearDistillInterest(ICostingTool arg0)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    ((ICostForward)this.ICostForward_820423736).clearDistillInterest(arg0);
  }
  
  public UFDouble forwardFairValueDistill(ICostingTool arg0, StockBalanceVO arg1, ITrade_Data arg2)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    return ((ICostForward)this.ICostForward_820423736).forwardFairValueDistill(arg0, arg1, arg2);
  }
  
  public UFDouble forwardFairValueDistill_debt(ICostingTool arg0, ZqjdTallyVO arg1, ITrade_Data arg2)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    return ((ICostForward)this.ICostForward_820423736).forwardFairValueDistill_debt(arg0, arg1, arg2);
  }
  
  public void saveFairValueDistill(ICostingTool arg0, ITrade_Data arg1)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    ((ICostForward)this.ICostForward_820423736).saveFairValueDistill(arg0, arg1);
  }
  
  public void clearDistillFv(ICostingTool arg0)
    throws BusinessException
  {
    if (this.ICostForward_820423736 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.tool.ICostForward"));
    }
    ((ICostForward)this.ICostForward_820423736).clearDistillFv(arg0);
  }
  
  public ApproveVO[] getLastapprovedate(LoginContext arg0)
    throws BusinessException
  {
    if (this.IQueryBaseInfo_369929668 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.IQueryBaseInfo"));
    }
    return ((IQueryBaseInfo)this.IQueryBaseInfo_369929668).getLastapprovedate(arg0);
  }
  
  public CostPlanVO[] getCostPlanInfo(LoginContext arg0)
    throws BusinessException
  {
    if (this.IQueryBaseInfo_369929668 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.IQueryBaseInfo"));
    }
    return ((IQueryBaseInfo)this.IQueryBaseInfo_369929668).getCostPlanInfo(arg0);
  }
  
  public CheckplanVO getCheckplanVO(LoginContext arg0, String arg1)
    throws BusinessException
  {
    if (this.IQueryBaseInfo_369929668 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.IQueryBaseInfo"));
    }
    return ((IQueryBaseInfo)this.IQueryBaseInfo_369929668).getCheckplanVO(arg0, arg1);
  }
  
  public String querySecuritesName(String arg0)
    throws BusinessException
  {
    if (this.IQueryBaseInfo_369929668 == null) {
      throw ((RuntimeException)this.expMap.get("nc.itf.fba_scost.cost.pub.IQueryBaseInfo"));
    }
    return ((IQueryBaseInfo)this.IQueryBaseInfo_369929668).querySecuritesName(arg0);
  }
}
