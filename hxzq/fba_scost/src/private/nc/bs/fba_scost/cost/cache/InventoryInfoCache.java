package nc.bs.fba_scost.cost.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.impl.fba_scost.cost.pub.PrivateMethod;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_scost.cost.tool.IScostCheckCache;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.pub.tools.VODeepCloneUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.trade.voutils.VOUtil;

public class InventoryInfoCache
  implements IScostCheckCache
{
  private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> inventoryInfoCacheMap = null;
  private CostParaVO costParaVO = null;
  
  public Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> getCache(ICostingTool costingtool)
    throws BusinessException
  {
    if (this.inventoryInfoCacheMap == null)
    {
      this.costParaVO = costingtool.getCostParaVO();
      initInventoryInfoCache(costingtool);
    }
    else if (costingtool != null)
    {
      initInventoryInfoCache(costingtool);
    }
    return this.inventoryInfoCacheMap;
  }
  
  public void updateCache(BanlanceQueryKeyVO sbqkVO, SuperVO vo)
    throws BusinessException
  {
    InventoryInfoVO inventoryInfoVO = (InventoryInfoVO)VODeepCloneUtil.deepClone(vo);
    inventoryInfoVO.setAttributeValue("dr", Integer.valueOf(0));
    inventoryInfoVO.setIsnew(true);
    String pk_stocksort = sbqkVO.getPk_stocksort();
    String pk_assetsprop = sbqkVO.getPk_assetsprop();
    String trade_date = sbqkVO.getTrade_date();
    String key = sbqkVO.getKey();
    if (sbqkVO.getPk_stocksort() == null)
    {
      pk_stocksort = "0001SE00000000000001";
      Logger.debug("getInventoryInfoVO时: 库存组织为空,取默认库存组织!");
    }
    if (((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(pk_assetsprop) == null)
    {
      ConcurrentHashMap<String, InventoryInfoVO> childmap = new ConcurrentHashMap();
      childmap.put(key, inventoryInfoVO);
      ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>> map = new ConcurrentHashMap();
      map.put(pk_stocksort, childmap);
      ((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).put(pk_assetsprop, map);
    }
    else if (((ConcurrentHashMap)((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort) == null)
    {
      ConcurrentHashMap<String, InventoryInfoVO> childmap = new ConcurrentHashMap();
      childmap.put(key, inventoryInfoVO);
      ((ConcurrentHashMap)((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(pk_assetsprop)).put(pk_stocksort, childmap);
    }
    else
    {
      ((ConcurrentHashMap)((ConcurrentHashMap)((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort)).put(key, inventoryInfoVO);
    }
  }
  
  public void clearCache()
  {
    if (this.inventoryInfoCacheMap != null)
    {
      this.inventoryInfoCacheMap.clear();
      this.inventoryInfoCacheMap = null;
    }
  }
  
  private void initInventoryInfoCache(ICostingTool costingtool)
    throws BusinessException
  {
    String trade_date = costingtool.getCurrdate();
    Map<String, List<InventoryInfoVO>> tradedatemap2;
    if (!costingtool.getIsinit())
    {
      if (this.inventoryInfoCacheMap != null)
      {
        List<InventoryInfoVO> list = (List)PrivateMethod.getInstance().getBaseDAO()
          .executeQuery(getSql(costingtool), new BeanListProcessor(InventoryInfoVO.class));
        if ((list != null) && (list.size() > 0))
        {
          String[] hashkey = new String[1];
          hashkey[0] = "pk_assetsprop";
          Map<String, List<InventoryInfoVO>> tradedatemap = PubMethod.getInstance().hashlizeObjects(list, hashkey);
          
          hashkey[0] = "pk_stocksort";
          List<InventoryInfoVO> assetsproplist = null;
          Iterator<Map.Entry<String, List<InventoryInfoVO>>> iter = tradedatemap.entrySet().iterator();
          

          Map<String, List<InventoryInfoVO>> tradedatemap21 = null;
          

          ConcurrentHashMap<String, InventoryInfoVO> tradevomap = null;
          Iterator<Map.Entry<String, List<InventoryInfoVO>>> iter2;
          for (; iter.hasNext(); iter2.hasNext())
          {
            Map.Entry<String, List<InventoryInfoVO>> entry = (Map.Entry)iter.next();
            String key = (String)entry.getKey();
            assetsproplist = (List)tradedatemap.get(key);
            tradedatemap21 = PubMethod.getInstance().hashlizeObjects(assetsproplist, hashkey);
            
            iter2 = tradedatemap21.entrySet().iterator();
            if(iter2==null)
            	continue;
            Map.Entry<String, List<InventoryInfoVO>> entry2 = (Map.Entry)iter2.next();
            String key2 = (String)entry2.getKey();
            tradevomap = (ConcurrentHashMap)PubMethod.getInstance().hashlizeObject(
              (List)tradedatemap21.get(key2), 
              PubMethod.getInstance().getCostArrayWithBilltypeGroup(this.costParaVO.getCostplanvo().getFundCostFieldArray()));
            if (((ConcurrentHashMap)((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(key)).get(key2) == null) {
              ((ConcurrentHashMap)((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(key)).put(key2, tradevomap);
            } else {
              ((ConcurrentHashMap)((ConcurrentHashMap)((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(key)).get(key2)).putAll(tradevomap);
            }
          }
        }
      }
      else
      {
        this.inventoryInfoCacheMap = new ConcurrentHashMap();
        this.inventoryInfoCacheMap.put(trade_date, 
          new ConcurrentHashMap());
        List<InventoryInfoVO> list = (List)PrivateMethod.getInstance().getBaseDAO()
          .executeQuery(getSql(costingtool), new BeanListProcessor(InventoryInfoVO.class));
        if ((list != null) && (list.size() > 0))
        {
          String[] hashkey = new String[1];
          hashkey[0] = "pk_assetsprop";
          Map<String, List<InventoryInfoVO>> tradedatemap = PubMethod.getInstance().hashlizeObjects(list, hashkey);
          
          hashkey[0] = "pk_stocksort";
          List<InventoryInfoVO> assetsproplist = null;
          Iterator<Map.Entry<String, List<InventoryInfoVO>>> iter = tradedatemap.entrySet().iterator();
          

          tradedatemap2 = null;
          

          ConcurrentHashMap<String, InventoryInfoVO> tradevomap = null;
          Iterator<Map.Entry<String, List<InventoryInfoVO>>> iter2;
          for (; iter.hasNext(); iter2.hasNext())
          {
            Map.Entry<String, List<InventoryInfoVO>> entry = (Map.Entry)iter.next();
            String key = (String)entry.getKey();
            ((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).put(key, 
              new ConcurrentHashMap());
            assetsproplist = (List)tradedatemap.get(key);
            tradedatemap2 = PubMethod.getInstance().hashlizeObjects(assetsproplist, hashkey);
            
            iter2 = tradedatemap2.entrySet().iterator();
            if(iter2==null)
            	continue;
            Map.Entry<String, List<InventoryInfoVO>> entry2 = (Map.Entry)iter2.next();
            String key2 = (String)entry2.getKey();
            tradevomap = (ConcurrentHashMap)PubMethod.getInstance().hashlizeObject(
              (List)tradedatemap2.get(key2), 
              PubMethod.getInstance().getCostArrayWithBilltypeGroup(this.costParaVO.getCostplanvo().getFundCostFieldArray()));
            ((ConcurrentHashMap)((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).get(key)).put(key2, tradevomap);
          }
        }
      }
    }
    else if ((this.inventoryInfoCacheMap.get(trade_date) == null) || (((ConcurrentHashMap)this.inventoryInfoCacheMap.get(trade_date)).size() == 0))
    	//this.inventoryInfoCacheMap == null
   {
      UFDate beforetradedate = new UFDate(trade_date).getDateBefore(1);
      UFDate lastdate = costingtool.getCurrbilltypegroupvo().getLastapprovedate() == null ? new UFDate(0L) : costingtool
        .getCurrbilltypegroupvo().getLastapprovedate();
      while (lastdate.compareTo(beforetradedate) <= 0)
      {
        if (this.inventoryInfoCacheMap.get(beforetradedate.toLocalString()) != null)
        {
          ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>> newmap = 
            (ConcurrentHashMap)this.inventoryInfoCacheMap.get(beforetradedate.toLocalString());
          Set<String> keys = newmap.keySet();
          Iterator<String> it = keys.iterator();
          Collection<ConcurrentHashMap<String, InventoryInfoVO>> vosmap = null;
          for (; it.hasNext(); ((Iterator<ConcurrentHashMap<String,InventoryInfoVO>>) tradedatemap2).hasNext())
          {
            vosmap = ((ConcurrentHashMap)newmap.get(it.next())).values();
            tradedatemap2 = (Map<String, List<InventoryInfoVO>>) vosmap.iterator(); 
            if(tradedatemap2 == null || tradedatemap2.isEmpty())
            	continue;
            Map<String, InventoryInfoVO> map = (Map)((Iterator<ConcurrentHashMap<String,InventoryInfoVO>>) tradedatemap2).next();
            Iterator<String> iz = map.keySet().iterator();
            while (iz.hasNext())
            {
              String keya = (String)iz.next();
              InventoryInfoVO balancevo = (InventoryInfoVO)map.get(keya);
              
              UFLiteralDate enddate = balancevo.getEnd_date();
              UFLiteralDate aduitdate = new UFLiteralDate(trade_date);
              if ((enddate != null) && (aduitdate.after(enddate)))
              {
                balancevo.setBegin_date(null);
                balancevo.setEnd_date(null);
                String key11 = VOUtil.getCombinesKey(balancevo, PubMethod.getInstance()
                  .getCostArrayWithBilltypeGroup(this.costParaVO.getCostplanvo().getFundCostFieldArray()));
                map.put(key11, balancevo);
                map.remove(keya);
              }
              balancevo.setTrade_date(new UFDate(trade_date));
            }
          }
          this.inventoryInfoCacheMap.put(trade_date, newmap);
          break;
        }
        beforetradedate = beforetradedate.getDateBefore(1);
      }
    }
  }
  
  private String getSql(ICostingTool costingtool)
  {
    this.costParaVO = costingtool.getCostParaVO();
    CheckParaVO checkparavo = this.costParaVO.getCheckParavo();
    String pk_glorgbook = checkparavo.getPk_glorgbook();
    String pk_org = checkparavo.getPk_org();
    String pk_group = checkparavo.getPk_group();
    String trade_date = costingtool.getCurrdate();
    StringBuffer manItemSb = new StringBuffer();
    StringBuffer manjoinSb = new StringBuffer();
    StringBuffer sqlsb = new StringBuffer();
    String[] costItemCodes = this.costParaVO.getCostplanvo().getFundCostFieldArray();
    String[] manItemCodes = PubMethod.getInstance().getCostArrayWithBilltypeGroup(costItemCodes);
    for (int i = 0; i < manItemCodes.length; i++) {
      manjoinSb.append(" and isnull(x.").append(manItemCodes[i]).append(",'").append(manItemCodes[i]).append("') = isnull(y.").append(manItemCodes[i]).append(",'").append(manItemCodes[i]).append("')");
    }
    for (int i = 0; i < manItemCodes.length; i++)
    {
      manItemSb.append("a." + manItemCodes[i]);
      manItemSb.append(",");
    }
    sqlsb.append("select /*+ no_merge(x) leading(x) */ y.* from (select max(a.trade_date) trade_date,");
    sqlsb.append(manItemSb.toString());
    sqlsb.append("a.pk_costplan");
    sqlsb.append(" from fund_inventoryinfo a ");
    

    sqlsb.append("where a.trade_date < '" + trade_date + "' and isnull(a.dr,0)=0 ");
    sqlsb.append(" and a.pk_costplan='");
    sqlsb.append(this.costParaVO.getCostplanvo().getPk_costplan()).append("' ");
    if (pk_group != null) {
      sqlsb.append(" and a.pk_group='" + pk_group + "' ");
    }
    if (pk_org != null) {
      sqlsb.append(" and a.pk_org='" + pk_org + "' ");
    }
    if (pk_glorgbook != null) {
      sqlsb.append(" and a.pk_glorgbook='" + pk_glorgbook + "' ");
    }
    sqlsb.append("group by ");
    sqlsb.append(manItemSb.toString());
    sqlsb.append("a.pk_costplan) x ");
    sqlsb.append("inner join fund_inventoryinfo y on x.trade_date = y.trade_date");
    sqlsb.append(manjoinSb.toString());
    
    sqlsb.append(" and x.pk_costplan = y.pk_costplan and isnull(y.dr,0)=0 order by y.enddate");
    return sqlsb.toString();
  }
}
