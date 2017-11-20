package nc.gejb.fba_scost.cmt;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nc.bs.framework.core.TxMark;
import nc.bs.framework.exception.FrameworkEJBException;
import nc.bs.logging.Logger;
import nc.bs.mw.naming.BeanBase;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.checkplan.CheckplanVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.scost.approve.ApproveVO;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uif2.LoginContext;

public class FBA_SCOST_EJB_Local
  extends BeanBase
  implements FBA_SCOST_EJBEjbObject
{
  private FBA_SCOST_EJBEjbBean _getBeanObject()
    throws RemoteException
  {
    return (FBA_SCOST_EJBEjbBean)getEjb();
  }
  
  public void MakeJTD(CheckParaVO arg0)
    throws BusinessException
  {
    beforeCallMethod(200);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().MakeJTD(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(200, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void DeleteJTD(CheckParaVO arg0)
    throws BusinessException
  {
    beforeCallMethod(201);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().DeleteJTD(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(201, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public boolean getBooleanFromInitcode(String arg0, String arg1)
    throws BusinessException
  {
    beforeCallMethod(202);
    Exception er = null;
    boolean o = false;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().getBooleanFromInitcode(arg0, arg1);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(202, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public void tallyAllCheckedData(CheckParaVO arg0)
    throws BusinessException
  {
    beforeCallMethod(203);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().tallyAllCheckedData(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(203, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public String checkBill(CheckParaVO arg0)
    throws BusinessException
  {
    beforeCallMethod(204);
    Exception er = null;
    String o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().checkBill(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(204, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public String unCheckBill(CheckParaVO arg0)
    throws BusinessException
  {
    beforeCallMethod(205);
    Exception er = null;
    String o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().unCheckBill(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(205, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public void tallyOneGroup_RequiresNew(BilltypeGroupVO arg0, Set<UFDate> arg1, Map<String, List<PendingBillVO>> arg2, CheckParaVO arg3, TradeDataTool arg4, CheckClassTool arg5)
    throws BusinessException
  {
    beforeCallMethod(206);
    Exception er = null;
    try
    {
      TxMark.enterTx("RequiresNew");
      _getBeanObject().tallyOneGroup_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(206, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("RequiresNew");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void uncheckOneGroup_RequiresNew(BilltypeGroupVO arg0, Set<UFDate> arg1, Map<String, List<PendingBillVO>> arg2, Map<String, List<PendingBillVO>> arg3, CheckParaVO arg4, TradeDataTool arg5, CheckClassTool arg6)
    throws BusinessException
  {
    beforeCallMethod(207);
    Exception er = null;
    try
    {
      TxMark.enterTx("RequiresNew");
      _getBeanObject().uncheckOneGroup_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(207, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("RequiresNew");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void checkOneGroup_RequiresNew(BilltypeGroupVO arg0, Set<UFDate> arg1, Map<String, List<PendingBillVO>> arg2, CheckParaVO arg3, TradeDataTool arg4, CheckClassTool arg5, List<CostPlanVO> arg6, boolean arg7, boolean arg8)
    throws BusinessException
  {
    beforeCallMethod(208);
    Exception er = null;
    try
    {
      TxMark.enterTx("RequiresNew");
      _getBeanObject().checkOneGroup_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(208, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("RequiresNew");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void updateMiddleAndApporveUI_RequiresNew(Map<String, List<PendingBillVO>> arg0, String arg1, int arg2, String arg3)
    throws BusinessException
  {
    beforeCallMethod(209);
    Exception er = null;
    try
    {
      TxMark.enterTx("RequiresNew");
      _getBeanObject().updateMiddleAndApporveUI_RequiresNew(arg0, arg1, arg2, arg3);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(209, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("RequiresNew");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void updateApporveUI_RequiresNew(BilltypeGroupVO arg0, CheckParaVO arg1, String arg2, Integer arg3)
    throws BusinessException
  {
    beforeCallMethod(210);
    Exception er = null;
    try
    {
      TxMark.enterTx("RequiresNew");
      _getBeanObject().updateApporveUI_RequiresNew(arg0, arg1, arg2, arg3);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(210, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("RequiresNew");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void Before_RequiresNew(BilltypeGroupVO arg0, CheckParaVO arg1, TradeDataTool arg2)
    throws BusinessException
  {
    beforeCallMethod(211);
    Exception er = null;
    try
    {
      TxMark.enterTx("RequiresNew");
      _getBeanObject().Before_RequiresNew(arg0, arg1, arg2);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(211, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("RequiresNew");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public String queryAccountingBookIDByFinanceOrgIDWithMainAssetBook(String arg0)
    throws BusinessException
  {
    beforeCallMethod(212);
    Exception er = null;
    String o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().queryAccountingBookIDByFinanceOrgIDWithMainAssetBook(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(212, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public String queryTallyVO(String arg0, String arg1, String arg2)
    throws BusinessException
  {
    beforeCallMethod(213);
    Exception er = null;
    String o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().queryTallyVO(arg0, arg1, arg2);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(213, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public ApproveVO getApproveVO(String arg0, String arg1, String arg2, String arg3)
    throws Exception
  {
    beforeCallMethod(214);
    Exception er = null;
    ApproveVO o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().getApproveVO(arg0, arg1, arg2, arg3);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(214, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof Exception)) {
        throw er;
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public void clearSysInit(String arg0)
    throws Exception
  {
    beforeCallMethod(215);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().clearSysInit(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(215, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof Exception)) {
        throw er;
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public String getSysInitValue(String arg0)
    throws Exception
  {
    beforeCallMethod(216);
    Exception er = null;
    String o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().getSysInitValue(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(216, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof Exception)) {
        throw er;
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public String getSysInitValue(String arg0, String arg1)
    throws Exception
  {
    beforeCallMethod(217);
    Exception er = null;
    String o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().getSysInitValue(arg0, arg1);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(217, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof Exception)) {
        throw er;
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public UFDouble calcInterest(ICostingTool arg0, ITrade_Data arg1)
    throws BusinessException
  {
    beforeCallMethod(218);
    Exception er = null;
    UFDouble o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().calcInterest(arg0, arg1);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(218, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public UFDouble forwardInterestDistill(ICostingTool arg0, StockBalanceVO arg1, ITrade_Data arg2)
    throws BusinessException
  {
    beforeCallMethod(219);
    Exception er = null;
    UFDouble o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().forwardInterestDistill(arg0, arg1, arg2);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(219, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public void saveInterestDistill(ICostingTool arg0, ITrade_Data arg1)
    throws BusinessException
  {
    beforeCallMethod(220);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().saveInterestDistill(arg0, arg1);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(220, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void clearDistillInterest(ICostingTool arg0)
    throws BusinessException
  {
    beforeCallMethod(221);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().clearDistillInterest(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(221, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public UFDouble forwardFairValueDistill(ICostingTool arg0, StockBalanceVO arg1, ITrade_Data arg2)
    throws BusinessException
  {
    beforeCallMethod(222);
    Exception er = null;
    UFDouble o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().forwardFairValueDistill(arg0, arg1, arg2);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(222, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public UFDouble forwardFairValueDistill_debt(ICostingTool arg0, ZqjdTallyVO arg1, ITrade_Data arg2)
    throws BusinessException
  {
    beforeCallMethod(223);
    Exception er = null;
    UFDouble o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().forwardFairValueDistill_debt(arg0, arg1, arg2);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(223, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public void saveFairValueDistill(ICostingTool arg0, ITrade_Data arg1)
    throws BusinessException
  {
    beforeCallMethod(224);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().saveFairValueDistill(arg0, arg1);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(224, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public void clearDistillFv(ICostingTool arg0)
    throws BusinessException
  {
    beforeCallMethod(225);
    Exception er = null;
    try
    {
      TxMark.enterTx("Required");
      _getBeanObject().clearDistillFv(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(225, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }
  
  public ApproveVO[] getLastapprovedate(LoginContext arg0)
    throws BusinessException
  {
    beforeCallMethod(226);
    Exception er = null;
    ApproveVO[] o = null;
    try
    {
      TxMark.enterTx("Required");
      o = (ApproveVO[])_getBeanObject().getLastapprovedate(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(226, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public CostPlanVO[] getCostPlanInfo(LoginContext arg0)
    throws BusinessException
  {
    beforeCallMethod(227);
    Exception er = null;
    CostPlanVO[] o = null;
    try
    {
      TxMark.enterTx("Required");
      o = (CostPlanVO[])_getBeanObject().getCostPlanInfo(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(227, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public CheckplanVO getCheckplanVO(LoginContext arg0, String arg1)
    throws BusinessException
  {
    beforeCallMethod(228);
    Exception er = null;
    CheckplanVO o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().getCheckplanVO(arg0, arg1);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(228, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  
  public String querySecuritesName(String arg0)
    throws BusinessException
  {
    beforeCallMethod(229);
    Exception er = null;
    String o = null;
    try
    {
      TxMark.enterTx("Required");
      o = _getBeanObject().querySecuritesName(arg0);
    }
    catch (Exception e)
    {
      er = e;
      TxMark.recordCause(er);
    }
    catch (Throwable thr)
    {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
      TxMark.recordCause(er);
    }
    try
    {
      afterCallMethod(229, er);
    }
    catch (RemoteException remoteException)
    {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    }
    finally
    {
      TxMark.leaveTx("Required");
    }
    if (null != er)
    {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
}
