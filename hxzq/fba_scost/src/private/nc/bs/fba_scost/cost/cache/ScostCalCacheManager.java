package nc.bs.fba_scost.cost.cache;

import java.util.Map;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_scost.cost.tool.IScostCalCacheManager;
import nc.itf.fba_scost.cost.tool.IScostCheckCache;
import nc.vo.pub.BusinessException;

public class ScostCalCacheManager
  implements IScostCalCacheManager
{
  StockBalanceCache stockbalancecache = null;
  FundBalanceCache fundbalancecache = null;
  TransferBalanceCache transferbalancecache = null;
  InventoryInfoCache inventoryinfocache = null;
  
  public Map getCacheByTypeCode(String typecode, ICostingTool costingtool)
    throws BusinessException
  {
    if ((typecode != null) && (!"".equals(typecode)))
    {
      IScostCheckCache cache = getCacheInstanceBytypeCode(typecode);
      if (cache != null) {
        return cache.getCache(costingtool);
      }
    }
    return null;
  }
  
  public void clear(String typecode)
  {
    if ((typecode != null) && (!"".equals(typecode)))
    {
      IScostCheckCache cache = getCacheInstanceBytypeCode(typecode);
      if (cache != null) {
        cache.clearCache();
      }
    }
  }
  
  public IScostCheckCache getCacheInstanceBytypeCode(String typeCode)
  {
    if (typeCode.equals("stockbalance"))
    {
      if (this.stockbalancecache == null) {
        this.stockbalancecache = new StockBalanceCache();
      }
      return this.stockbalancecache;
    }
    if (typeCode.equals("fundbalance"))
    {
      if (this.fundbalancecache == null) {
        this.fundbalancecache = new FundBalanceCache();
      }
      return this.fundbalancecache;
    }
    if (typeCode.equals("transferbalance"))
    {
      if (this.transferbalancecache == null) {
        this.transferbalancecache = new TransferBalanceCache();
      }
      return this.transferbalancecache;
    }
    if (typeCode.equals("inventoryinfo"))
    {
      if (this.inventoryinfocache == null) {
        this.inventoryinfocache = new InventoryInfoCache();
      }
      return this.inventoryinfocache;
    }
    return null;
  }
}
