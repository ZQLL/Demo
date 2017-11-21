package nc.bs.fba_scost.cost.cache;

import java.util.Map;

import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_scost.cost.tool.IScostCalCacheManager;
import nc.itf.fba_scost.cost.tool.IScostCheckCache;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.pub.BusinessException;

public class ScostCalCacheManager implements IScostCalCacheManager {

    StockBalanceCache stockbalancecache = null;
    FundBalanceCache fundbalancecache = null;
    TransferBalanceCache transferbalancecache = null;
    InventoryInfoCache inventoryinfocache = null;

    public Map getCacheByTypeCode(String typecode, ICostingTool costingtool) throws BusinessException {
        if (typecode != null && !"".equals(typecode)) {
            IScostCheckCache cache = getCacheInstanceBytypeCode(typecode);
            if (cache != null) {
                return cache.getCache(costingtool);
            }
        }
        return null;
    }

    public void clear(String typecode) {
        if (typecode != null && !"".equals(typecode)) {
            IScostCheckCache cache = getCacheInstanceBytypeCode(typecode);
            if (cache != null) {
                cache.clearCache();
            }
        }
    }

    public IScostCheckCache getCacheInstanceBytypeCode(String typeCode) {
        if (typeCode.equals(SystemConst.StockBalance)) {// ��� ����
            if (stockbalancecache == null) {
                stockbalancecache = new StockBalanceCache();
            }
            return stockbalancecache;
        } else if (typeCode.equals(SystemConst.FundBalance)) {// �ʽ𻺴�
            if (fundbalancecache == null) {
                fundbalancecache = new FundBalanceCache();
            }
            return fundbalancecache;
        } else if (typeCode.equals(SystemConst.TransferBalance)) {// ȯԴ��ת����
            if (transferbalancecache == null) {
                transferbalancecache = new TransferBalanceCache();
            }
            return transferbalancecache;
        } else if (typeCode.equals(SystemConst.InventoryInfo)) {// ���ʹ����滺��
            if (inventoryinfocache == null) {
                inventoryinfocache = new InventoryInfoCache();
            }
            return inventoryinfocache;
        }
        return null;
    }

}
