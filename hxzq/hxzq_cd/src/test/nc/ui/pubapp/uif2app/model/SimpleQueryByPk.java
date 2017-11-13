package nc.ui.pubapp.uif2app.model;

import nc.bs.framework.common.NCLocator;
import nc.itf.pubapp.pub.smart.IQueryBillService;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class SimpleQueryByPk
  implements DefaultFuncNodeInitDataListener.IQueryServiceWithFunCode, DefaultFuncNodeInitDataListener.IQueryServiceWithFuncCodeExt, DefaultFuncNodeInitDataListener.IQueryServiceExt
{
  public SimpleQueryByPk(DefaultFuncNodeInitDataListener plistener)
  {
    setListener(plistener);
  }
  
  private DefaultFuncNodeInitDataListener listener = null;
  
  public DefaultFuncNodeInitDataListener getListener()
  {
    return this.listener;
  }
  
  public void setListener(DefaultFuncNodeInitDataListener listener)
  {
    this.listener = listener;
  }
  
  public Object queryByPk(String pk)
  {
    try
    {
      Class<?> voClass = Class.forName(getListener().getVoClassName());
      IQueryBillService queryBillService = (IQueryBillService)NCLocator.getInstance().lookup(IQueryBillService.class);
      
      return queryBillService.queryBill(voClass, pk);
    }
    catch (Exception e)
    {
      ExceptionUtils.wrappException(e);
    }
    return null;
  }
  
  public Object queryByPkWithFunCode(String pk, String nodeCode)
  {
    try
    {
      Class<?> voClass = Class.forName(getListener().getVoClassName());
      IQueryBillService queryBillService = (IQueryBillService)NCLocator.getInstance().lookup(IQueryBillService.class);
      
      return queryBillService.queryBill(voClass, pk, nodeCode);
    }
    catch (Exception e)
    {
      ExceptionUtils.wrappException(e);
    }
    return null;
  }
  
  public Object[] queryByPk(String[] pk)
  {
    try
    {
      Class<?> voClass = Class.forName(getListener().getVoClassName());
      IQueryBillService queryBillService = (IQueryBillService)NCLocator.getInstance().lookup(IQueryBillService.class);
      
      return queryBillService.queryBills(voClass, pk);
    }
    catch (Exception e)
    {
      ExceptionUtils.wrappException(e);
    }
    return null;
  }
  
  public Object[] queryByPksWithFunCode(String[] pks, String nodeCode)
  {
    try
    {
      Class<?> voClass = Class.forName(getListener().getVoClassName());
      IQueryBillService queryBillService = (IQueryBillService)NCLocator.getInstance().lookup(IQueryBillService.class);
      return queryBillService.queryBills(voClass, pks, nodeCode);
    }
    catch (Exception e)
    {
      ExceptionUtils.wrappException(e);
    }
    return null;
  }
}
