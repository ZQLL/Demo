package nc.bs.fba_scost.cost.simtools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_scost.cost.tool.IScostCalCacheManager;
import nc.vo.fba_scost.cost.fundbalance.FundBalanceVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.transferbalance.TransferBalanceVO;
import nc.vo.fba_scost.pub.tools.VODeepCloneUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

/**
 * 期初工具类，单据审核反审核时使用此类来处理期初期末
 * 
 * @author liangwei
 * 
 */
public class CostBalanceTool implements ICostBalanceTool{
	private static final FundBalanceVO fundbalancevo = null;
	private IScostCalCacheManager scostcalcachemanager;

	public IScostCalCacheManager getScostcalcachemanager() {
		return scostcalcachemanager;
	}

	public void setScostcalcachemanager(IScostCalCacheManager scostcalcachemanager) {
		this.scostcalcachemanager = scostcalcachemanager;
	}

	private static CostBalanceTool tool;
	private BaseDAO dao;

	// 可能产生成本变动的库存
	private Map<String, StockBalanceVO> costchangevomap = new HashMap<String, StockBalanceVO>();

	public Map<String, Map<String, TransferBalanceVO>> transfermap = new HashMap<String, Map<String, TransferBalanceVO>>();// 保存当前最新的券源划转余额表

	/**
	 * 单例生成
	 * 
	 * @return
	 */
	public CostBalanceTool getInstance() {
		if (null == tool) {
			tool = new CostBalanceTool();
		}
		return tool;
	}

	public CostBalanceTool() {

	}

	/**
	 * 根据成本计算vo进行初始化
	 * 
	 * @param costParaVO
	 */
	public CostBalanceTool(CostParaVO costParaVO) {

	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	public StockBalanceVO getStockbalanceVO(BanlanceQueryKeyVO sbqkVO, ICostingTool costingtool) throws BusinessException {
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>> stockmap = scostcalcachemanager
				.getCacheByTypeCode(SystemConst.StockBalance, costingtool);
		String pk_stocksort = sbqkVO.getPk_stocksort();
		String pk_assetsprop = sbqkVO.getPk_assetsprop();
		String trade_date = sbqkVO.getTrade_date();
		String key = sbqkVO.getKey();
		if (sbqkVO.getPk_stocksort() == null) {
			pk_stocksort = SystemConst.pk_defaultstocksort;
			Logger.debug("getStockbalanceVO时: 库存组织为空,取默认库存组织!");
		}
		if (stockmap.get(trade_date).get(pk_assetsprop) == null) {
			return null;
		}
		if (stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort) == null) {
			return null;
		}
		SuperVO vo = stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort).get(key);
		return (StockBalanceVO) VODeepCloneUtil.deepClone(vo);
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	public TransferBalanceVO getTransferbalanceVO(String key, ICostingTool costingtool) throws BusinessException {
		Map<String, Map<String, TransferBalanceVO>> transfermap = scostcalcachemanager.getCacheByTypeCode(SystemConst.TransferBalance, costingtool);
		String trade_date = costingtool.getCurrdate();
		if (transfermap.get(trade_date) != null)
			return (TransferBalanceVO) VODeepCloneUtil.deepClone(transfermap.get(trade_date).get(key));
		return null;
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	public TransferBalanceVO getTransferBalanceVOByVO(SuperVO tradevo, ICostingTool costingtool) throws BusinessException {
		try {
			Map<String, Map<String, TransferBalanceVO>> transfermap = scostcalcachemanager.getCacheByTypeCode(SystemConst.TransferBalance,
					costingtool);
			String trade_date = costingtool.getCurrdate();
			if (transfermap.get(trade_date) == null) {
				transfermap.put(trade_date, new ConcurrentHashMap<String, TransferBalanceVO>());
			}
			TransferBalanceVO vo = new TransferBalanceVO();
			/**
			 * hc_pk_group 可能没有 后续找人加 YangJie
			 */
			String hc_pk_group = (String) tradevo.getAttributeValue("hc_pk_group");
			String hc_pk_org = (String) tradevo.getAttributeValue("hc_pk_org");
			String hc_pk_glorgbook = (String) tradevo.getAttributeValue("hc_pk_glorgbook");
			String pk_group = (String) tradevo.getAttributeValue("pk_group");
			String pk_org = (String) tradevo.getAttributeValue("pk_org");
			String pk_org_v = (String) tradevo.getAttributeValue("pk_org_v");
			String pk_glorgbook = (String) tradevo.getAttributeValue("pk_glorgbook");
			String[] keys = PubMethod.getInstance().getTransferBalanceKeys(hc_pk_group, hc_pk_org, hc_pk_glorgbook, pk_group, pk_org, pk_glorgbook);
			vo.setPk_org(pk_org);
			vo.setPk_group(pk_group);
			vo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo().getPk_costplan());
			vo.setPk_glorgbook(pk_glorgbook);
			vo.setPk_org_v(pk_org_v);
			vo.setPk_billtypegroup(costingtool.getCurrbilltypegroupvo().getPk_billtypegroup());
			for (String key : keys) {
				vo.setAttributeValue(key, tradevo.getAttributeValue(key));
			}
			return vo;
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	/**
	 * YangJie 2014-04-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	public FundBalanceVO getFundbalanceVO(String key, ICostingTool costingtool) throws BusinessException {
		Map<String, Map<String, FundBalanceVO>> fundBalancemap = scostcalcachemanager.getCacheByTypeCode(SystemConst.FundBalance, costingtool);
		String trade_date = costingtool.getCurrdate();
		if (fundBalancemap.get(trade_date) != null)
			return (FundBalanceVO) VODeepCloneUtil.deepClone(fundBalancemap.get(trade_date).get(key));
		return null;
	}

	/**
	 * YangJie 2014-04-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	public FundBalanceVO getFundbalanceVOByVO(SuperVO tradevo, ICostingTool costingtool) {
		String[] keys = costingtool.getCostParaVO().getCostplanvo().getFundsFieldArray();
		FundBalanceVO vo = new FundBalanceVO();
		vo.setStocks_sum(new UFDouble(0));
		for (String key : keys) {
			vo.setAttributeValue(key, tradevo.getAttributeValue(key));
		}
		/**
		 * 新增资金必须要存资金账号
		 * YangJie
		 * 2014-07-01
		 */
		vo.setAttributeValue("pk_capaccount", tradevo.getAttributeValue("pk_capaccount"));
		return vo;
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	public void updateStockbalanceVO(BanlanceQueryKeyVO sbqkVO, StockBalanceVO vo) throws BusinessException {
		scostcalcachemanager.getCacheInstanceBytypeCode(SystemConst.StockBalance).updateCache(sbqkVO, vo);
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	public void updateTransferBalanceVO(BanlanceQueryKeyVO sbqkVO, TransferBalanceVO vo) throws BusinessException {
		scostcalcachemanager.getCacheInstanceBytypeCode(SystemConst.TransferBalance).updateCache(sbqkVO, vo);
	}

	/**
	 * YangJie 2014-04-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	public void updateFundbalanceVO(BanlanceQueryKeyVO sbqkVO, FundBalanceVO vo) throws BusinessException {
		scostcalcachemanager.getCacheInstanceBytypeCode(SystemConst.FundBalance).updateCache(sbqkVO, vo);
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	public StockBalanceVO getStockbalanceVOByVO(SuperVO tradevo, ICostingTool costingtool) throws BusinessException {
		String[] manFieldV = costingtool.getCostParaVO().getCostplanvo().getCostFieldArray();
		String pk_assetsprop = (String) tradevo.getAttributeValue("pk_assetsprop");// 资产属性[转入]
		String pk_stocksort = (String) tradevo.getAttributeValue("pk_stocksort");// 库存组织[转入]
		StockBalanceVO vo = new StockBalanceVO();
		vo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo().getPk_costplan());
		if ("HV3F".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field, tradevo.getAttributeValue("hr_" + field));
			}
			//证券档案赋值
			vo.setAttributeValue("pk_securities", tradevo.getAttributeValue("pk_securities"));
		} else if ("HV3G".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field, tradevo.getAttributeValue("hr_" + field));
			}
			//证券档案赋值
			vo.setAttributeValue("pk_securities", tradevo.getAttributeValue("pk_securities"));
		} else if ("HV9B".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field, tradevo.getAttributeValue("hr_" + field));
			}
			//证券档案赋值
			vo.setAttributeValue("pk_securities", tradevo.getAttributeValue("pk_securities"));
		} else {
			for (String field : manFieldV) {
				vo.setAttributeValue(field, tradevo.getAttributeValue(field));
			}
		}
		vo.setPk_group((String) tradevo.getAttributeValue("pk_group"));
		vo.setPk_org((String) tradevo.getAttributeValue("pk_org"));
		vo.setPk_org_v((String) tradevo.getAttributeValue("pk_org_v"));
		vo.setPk_glorgbook((String) tradevo.getAttributeValue("pk_glorgbook"));
		vo.setTrade_date(new UFDate(costingtool.getCurrdate()));
		vo.setStocks_sum(new UFDouble(0));
		vo.setStocks_num(new UFDouble(0));
		vo.setState(0);
		vo.getPk_assetsprop();
		String trade_date = costingtool.getCurrdate();
		String pk_billtypegroup = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup();
		String key = VOUtil.getCombinesKey(vo, manFieldV);
		key = pk_billtypegroup + key;
		vo.setPk_billtypegroup(pk_billtypegroup);
		pk_assetsprop = vo.getPk_assetsprop();
		pk_stocksort = vo.getPk_stocksort();
		// 构造证券转换转入库存jinhd
		// if (tradevo instanceof TransformtradeVO) {
		// String[] keys = new String[manFieldV.length - 1];
		// for (int i = 0; i < keys.length; i++) {
		// keys[i] = manFieldV[i];
		// }
		// key = new StockTransform().getCombinesKey((TransformtradeVO) tradevo,
		// keys, true);
		// key = key + ((TransformtradeVO) tradevo).getPk_securities2();
		// String pk2 = ((TransformtradeVO) tradevo).getPk_securities2();
		// vo.setPk_securities(pk2);
		// }
		// // jinhd end
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>> stockmap = scostcalcachemanager
				.getCacheByTypeCode(SystemConst.StockBalance, costingtool);
		if (stockmap.get(trade_date).get(pk_assetsprop) == null) {
			stockmap.get(trade_date).put(pk_assetsprop, new ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>());
		}
		if (stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort) == null) {
			stockmap.get(trade_date).get(pk_assetsprop).put(pk_stocksort, new ConcurrentHashMap<String, StockBalanceVO>());
		}
		//update by lihaibo 注释掉--影响库存改动
//		stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort).put(key, vo);
		return vo;

	}

	private void initAllBalance() {

	}

	/**
	 * 清除所有缓存
	 * 
	 * @date 2012-9-10 下午4:00:03
	 */
	public void clearAllCache() {

	}

	/**
	 * 更新所有余额表
	 * 
	 * @param costplanvo
	 * @throws Exception
	 */
	public void updateAll(String trade_date) throws Exception {
		updateStockBalancecache(trade_date);
		updateFundBalancecache(trade_date);
		updateTransferBalancecache(trade_date);
	}

	private void updateStockBalancecache(String trade_date) throws Exception {

		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>> newmap = scostcalcachemanager
				.getCacheInstanceBytypeCode(SystemConst.StockBalance).getCache(null);

		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = newmap.get(trade_date).keySet();
		Iterator<String> it = keys.iterator();
		Collection<ConcurrentHashMap<String, StockBalanceVO>> vosmap = null;
		Collection<StockBalanceVO> vos = null;
		List<StockBalanceVO> updatevos = new ArrayList<StockBalanceVO>();
		while (it.hasNext()) {
			vosmap = newmap.get(trade_date).get(it.next()).values();
			for (Map<String, StockBalanceVO> map : vosmap) {
				vos = map.values();
				if (vos == null || vos.isEmpty()) {
					continue;
				}
				for (StockBalanceVO balancevo : vos) {
					String pk_sec = balancevo.getPk_securities();
					if (pk_sec.equals("1001A110000000003Q7S")) {
						int i = 0;
					}
					if (balancevo.getStocks_num() == null || balancevo.getStocks_num().doubleValue() < 0 || balancevo.getStocks_sum() == null
							|| balancevo.getStocks_sum().doubleValue() < 0) {
						return;
					}
					/**
					 * 只插入新增的
					 */
					if (balancevo.getIsnew()) {
						updatevos.add(balancevo);
						balancevo.setTrade_date(new UFDate(trade_date));
						/**
						 * 使用完毕后 还原 防止下一日仍然插入 YangJie 2014-04-15
						 */
						balancevo.setIsnew(false);
					}
				}

			}
		}
		getBaseDao().insertVOArray(updatevos.toArray(new StockBalanceVO[updatevos.size()]));

	}

	private void updateFundBalancecache(String trade_date) throws Exception {
		Map<String, Map<String, FundBalanceVO>> newmap = scostcalcachemanager.getCacheInstanceBytypeCode(SystemConst.FundBalance).getCache(null);
		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = newmap.get(trade_date).keySet();
		Iterator<String> it = keys.iterator();
		List<FundBalanceVO> updatevos = new ArrayList<FundBalanceVO>();
		while (it.hasNext()) {
			FundBalanceVO vo = newmap.get(trade_date).get(it.next());
			/**
			 * 只插入新增的
			 */
			if (vo.getIsnew()) {
				updatevos.add(vo);
				vo.setIsnew(false);
			}
		}
		getBaseDao().insertVOArray(updatevos.toArray(new FundBalanceVO[updatevos.size()]));
	}

	private void updateTransferBalancecache(String trade_date) throws Exception {
		Map<String, Map<String, TransferBalanceVO>> newmap = scostcalcachemanager.getCacheInstanceBytypeCode(SystemConst.TransferBalance).getCache(
				null);
		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = newmap.get(trade_date).keySet();
		Iterator<String> it = keys.iterator();
		List<TransferBalanceVO> updatevos = new ArrayList<TransferBalanceVO>();
		while (it.hasNext()) {
			TransferBalanceVO vo = newmap.get(trade_date).get(it.next());
			/**
			 * 只插入新增的
			 */
			if (vo.getIsnew()) {
				updatevos.add(vo);
				vo.setIsnew(false);
			}
		}
		getBaseDao().insertVOArray(updatevos.toArray(new TransferBalanceVO[updatevos.size()]));

	}

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

}
