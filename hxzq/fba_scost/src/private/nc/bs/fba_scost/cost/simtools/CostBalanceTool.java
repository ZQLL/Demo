package nc.bs.fba_scost.cost.simtools;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_scost.cost.tool.IScostCalCacheManager;
import nc.vo.fba_scost.cost.fundbalance.FundBalanceVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
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
public class CostBalanceTool implements ICostBalanceTool {
	private static final FundBalanceVO fundbalancevo = null;
	private IScostCalCacheManager scostcalcachemanager;

	@Override
	public IScostCalCacheManager getScostcalcachemanager() {
		return scostcalcachemanager;
	}

	@Override
	public void setScostcalcachemanager(
			IScostCalCacheManager scostcalcachemanager) {
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
	@Override
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
	@Override
	public StockBalanceVO getStockbalanceVO(BanlanceQueryKeyVO sbqkVO,
			ICostingTool costingtool) throws BusinessException {
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
		SuperVO vo = stockmap.get(trade_date).get(pk_assetsprop)
				.get(pk_stocksort).get(key);
		return (StockBalanceVO) VODeepCloneUtil.deepClone(vo);
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	@Override
	public TransferBalanceVO getTransferbalanceVO(String key,
			ICostingTool costingtool) throws BusinessException {
		Map<String, Map<String, TransferBalanceVO>> transfermap = scostcalcachemanager
				.getCacheByTypeCode(SystemConst.TransferBalance, costingtool);
		String trade_date = costingtool.getCurrdate();
		if (transfermap.get(trade_date) != null)
			return (TransferBalanceVO) VODeepCloneUtil.deepClone(transfermap
					.get(trade_date).get(key));
		return null;
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	@Override
	public TransferBalanceVO getTransferBalanceVOByVO(SuperVO tradevo,
			ICostingTool costingtool) throws BusinessException {
		try {
			Map<String, Map<String, TransferBalanceVO>> transfermap = scostcalcachemanager
					.getCacheByTypeCode(SystemConst.TransferBalance,
							costingtool);
			String trade_date = costingtool.getCurrdate();
			if (transfermap.get(trade_date) == null) {
				transfermap.put(trade_date,
						new ConcurrentHashMap<String, TransferBalanceVO>());
			}
			TransferBalanceVO vo = new TransferBalanceVO();
			/**
			 * hc_pk_group 可能没有 后续找人加 YangJie
			 */
			String hc_pk_group = (String) tradevo
					.getAttributeValue("hc_pk_group");
			String hc_pk_org = (String) tradevo.getAttributeValue("hc_pk_org");
			String hc_pk_glorgbook = (String) tradevo
					.getAttributeValue("hc_pk_glorgbook");
			String pk_group = (String) tradevo.getAttributeValue("pk_group");
			String pk_org = (String) tradevo.getAttributeValue("pk_org");
			String pk_org_v = (String) tradevo.getAttributeValue("pk_org_v");
			String pk_glorgbook = (String) tradevo
					.getAttributeValue("pk_glorgbook");
			String[] keys = PubMethod.getInstance().getTransferBalanceKeys(
					hc_pk_group, hc_pk_org, hc_pk_glorgbook, pk_group, pk_org,
					pk_glorgbook);
			vo.setPk_org(pk_org);
			vo.setPk_group(pk_group);
			vo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo()
					.getPk_costplan());
			vo.setPk_glorgbook(pk_glorgbook);
			vo.setPk_org_v(pk_org_v);
			vo.setPk_billtypegroup(costingtool.getCurrbilltypegroupvo()
					.getPk_billtypegroup());
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
	@Override
	public FundBalanceVO getFundbalanceVO(String key, ICostingTool costingtool)
			throws BusinessException {
		Map<String, Map<String, FundBalanceVO>> fundBalancemap = scostcalcachemanager
				.getCacheByTypeCode(SystemConst.FundBalance, costingtool);
		String trade_date = costingtool.getCurrdate();
		if (fundBalancemap.get(trade_date) != null)
			return (FundBalanceVO) VODeepCloneUtil.deepClone(fundBalancemap
					.get(trade_date).get(key));
		return null;
	}

	/**
	 * YangJie 2014-04-18
	 * 
	 * @return 根据主键获取库存vo 若查找不到库存 考虑默认为新增库存
	 * @throws BusinessException
	 */
	@Override
	public FundBalanceVO getFundbalanceVOByVO(SuperVO tradevo,
			ICostingTool costingtool) {
		String[] keys = costingtool.getCostParaVO().getCostplanvo()
				.getFundsFieldArray();
		FundBalanceVO vo = new FundBalanceVO();
		vo.setStocks_sum(new UFDouble(0));
		for (String key : keys) {
			vo.setAttributeValue(key, tradevo.getAttributeValue(key));
		}
		/**
		 * 新增资金必须要存资金账号 YangJie 2014-07-01
		 */
		vo.setAttributeValue("pk_capaccount",
				tradevo.getAttributeValue("pk_capaccount"));
		return vo;
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	@Override
	public void updateStockbalanceVO(BanlanceQueryKeyVO sbqkVO,
			StockBalanceVO vo) throws BusinessException {
		scostcalcachemanager.getCacheInstanceBytypeCode(
				SystemConst.StockBalance).updateCache(sbqkVO, vo);
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	@Override
	public void updateTransferBalanceVO(BanlanceQueryKeyVO sbqkVO,
			TransferBalanceVO vo) throws BusinessException {
		scostcalcachemanager.getCacheInstanceBytypeCode(
				SystemConst.TransferBalance).updateCache(sbqkVO, vo);
	}

	/**
	 * YangJie 2014-04-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	@Override
	public void updateFundbalanceVO(BanlanceQueryKeyVO sbqkVO, FundBalanceVO vo)
			throws BusinessException {
		scostcalcachemanager
				.getCacheInstanceBytypeCode(SystemConst.FundBalance)
				.updateCache(sbqkVO, vo);
	}

	/**
	 * YangJie 2014-03-18
	 * 
	 * @return 根据主键获取库存vo
	 * @throws BusinessException
	 */
	@Override
	public StockBalanceVO getStockbalanceVOByVO(SuperVO tradevo,
			ICostingTool costingtool) throws BusinessException {
		String[] manFieldV = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		String pk_assetsprop = (String) tradevo
				.getAttributeValue("pk_assetsprop");// 资产属性[转入]
		String pk_stocksort = (String) tradevo
				.getAttributeValue("pk_stocksort");// 库存组织[转入]
		StockBalanceVO vo = new StockBalanceVO();
		vo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo()
				.getPk_costplan());
		if ("HV3F".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field,
						tradevo.getAttributeValue("hr_" + field));
			}
			// 证券档案赋值
			/** 目前看来这句话导致了无法将原来的库存信息做变化。 2017年5月16日 JIGNQT DEL START */
			// vo.setAttributeValue("pk_securities",
			// tradevo.getAttributeValue("pk_securities"));
			/** 目前看来这句话导致了无法将原来的库存信息做变化。 2017年5月16日 JIGNQT DEL END */
		} else if ("HV3G".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field,
						tradevo.getAttributeValue("hr_" + field));
			}
			// 证券档案赋值
			vo.setAttributeValue("pk_securities",
					tradevo.getAttributeValue("pk_securities"));
		} else if ("HV9B".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field,
						tradevo.getAttributeValue("hr_" + field));
			}
			// 证券档案赋值
			vo.setAttributeValue("pk_securities",
					tradevo.getAttributeValue("pk_securities"));
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
		String pk_billtypegroup = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup();
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
			stockmap.get(trade_date)
					.put(pk_assetsprop,
							new ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>());
		}
		if (stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort) == null) {
			stockmap.get(trade_date)
					.get(pk_assetsprop)
					.put(pk_stocksort,
							new ConcurrentHashMap<String, StockBalanceVO>());
		}
		stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort)
				.put(key, vo);
		return vo;

	}

	/**
	 * 根据单据vo构建筹资管理库存表vo
	 * 
	 * @author qs
	 * @since 2017-6-5下午5:54:19
	 */
	@Override
	public InventoryInfoVO getInventoryInfoVOByVO(SuperVO tradevo,
			ICostingTool costingtool) throws BusinessException {
		String[] manFieldV = costingtool.getCostParaVO().getCostplanvo()
				.getFundCostFieldArray();
		String pk_assetsprop = (String) tradevo
				.getAttributeValue("pk_assetsprop");// 资产属性[转入]
		String pk_stocksort = (String) tradevo
				.getAttributeValue("pk_stocksort");// 库存组织[转入]
		InventoryInfoVO vo = new InventoryInfoVO();
		vo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo()
				.getPk_costplan());
		for (String field : manFieldV) {
			vo.setAttributeValue(field, tradevo.getAttributeValue(field));
		}
		// 交易类型
		vo.setTranstypecode((String) tradevo.getAttributeValue("transtypecode"));
		// 交易类型pk
		vo.setPk_transtype((String) tradevo.getAttributeValue("pk_transtype"));
		// 单据类型
		vo.setBilltypecode((String) tradevo.getAttributeValue("billtypecode"));
		// 单据类型pk
		vo.setPk_billtype((String) tradevo.getAttributeValue("pk_billtype"));
		// 起息日
		vo.setStartdate(((UFDate) tradevo.getAttributeValue("trade_date"))
				.asBegin());
		// 到期日
		vo.setEnddate(((UFDate) tradevo.getAttributeValue("enddate")).asBegin());
		// 转融通期限=起息日至到期日
		if (vo.getTranstypecode().equals(SystemConst.GOFINANCING_01)) {
			// 起息日至到期日
			Integer timelimit = UFDate.getDaysBetween(
					(UFDate) tradevo.getAttributeValue("trade_date"),
					(UFDate) tradevo.getAttributeValue("enddate")) + 1;
			// 期限
			vo.setTimelimit(timelimit);
		} else {
			vo.setTimelimit((Integer) tradevo.getAttributeValue("timelimit"));
		}

		// 付息方式
		vo.setPaytype((Integer) tradevo.getAttributeValue("paytype"));
		// 执行票面利率
		vo.setExecutefacerate((UFDouble) tradevo
				.getAttributeValue("contractrate"));
		// 融资类型
		vo.setFundtype((Integer) tradevo.getAttributeValue("fundtype"));
		// 产品或对手方名称
		vo.setProductorcounterparty((String) tradevo
				.getAttributeValue("productorcounterparty"));
		// 管理部门
		vo.setManagedept((String) tradevo.getAttributeValue("managedept"));
		// 实际利率
		vo.setRealrate((UFDouble) tradevo.getAttributeValue("realrate"));

		// vo.setPk_group((String) tradevo.getAttributeValue("pk_group"));
		// vo.setPk_org((String) tradevo.getAttributeValue("pk_org"));
		vo.setPk_org_v((String) tradevo.getAttributeValue("pk_org_v"));
		vo.setPk_glorgbook((String) tradevo.getAttributeValue("pk_glorgbook"));
		vo.setTrade_date(new UFDate(costingtool.getCurrdate()));
		vo.setStocks_sum(new UFDouble(0));
		vo.setStocks_num(new UFDouble(0));
		vo.setState(0);
		vo.getPk_assetsprop();
		String trade_date = costingtool.getCurrdate();
		String pk_billtypegroup = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup();
		String key = VOUtil.getCombinesKey(vo, manFieldV);
		key = pk_billtypegroup + key;
		vo.setPk_billtypegroup(pk_billtypegroup);
		pk_assetsprop = vo.getPk_assetsprop();
		pk_stocksort = vo.getPk_stocksort();
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> stockmap = scostcalcachemanager
				.getCacheByTypeCode(SystemConst.InventoryInfo, costingtool);
		if (stockmap.get(trade_date).get(pk_assetsprop) == null) {
			stockmap.get(trade_date)
					.put(pk_assetsprop,
							new ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>());
		}
		if (stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort) == null) {
			stockmap.get(trade_date)
					.get(pk_assetsprop)
					.put(pk_stocksort,
							new ConcurrentHashMap<String, InventoryInfoVO>());
		}
		stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort)
				.put(key, vo);
		return vo;
	}

	private void initAllBalance() {

	}

	/**
	 * 清除所有缓存
	 * 
	 * @date 2012-9-10 下午4:00:03
	 */
	@Override
	public void clearAllCache() {

	}

	/**
	 * 更新所有余额表
	 * 
	 * @param costplanvo
	 * @throws Exception
	 */
	@Override
	public void updateAll(String trade_date) throws Exception {
		updateStockBalancecache(trade_date);
		updateFundBalancecache(trade_date);
		updateTransferBalancecache(trade_date);
		updateInventoryInfocache(trade_date);
	}

	private void updateStockBalancecache(String trade_date) throws Exception {

		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>> newmap = scostcalcachemanager
				.getCacheInstanceBytypeCode(SystemConst.StockBalance).getCache(
						null);

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
					if (balancevo.getStocks_num() == null
							|| balancevo.getStocks_num().doubleValue() < 0
							|| balancevo.getStocks_sum() == null
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
		getBaseDao().insertVOArray(
				updatevos.toArray(new StockBalanceVO[updatevos.size()]));

	}

	private void updateFundBalancecache(String trade_date) throws Exception {
		Map<String, Map<String, FundBalanceVO>> newmap = scostcalcachemanager
				.getCacheInstanceBytypeCode(SystemConst.FundBalance).getCache(
						null);
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
		getBaseDao().insertVOArray(
				updatevos.toArray(new FundBalanceVO[updatevos.size()]));
	}

	private void updateTransferBalancecache(String trade_date) throws Exception {
		Map<String, Map<String, TransferBalanceVO>> newmap = scostcalcachemanager
				.getCacheInstanceBytypeCode(SystemConst.TransferBalance)
				.getCache(null);
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
		getBaseDao().insertVOArray(
				updatevos.toArray(new TransferBalanceVO[updatevos.size()]));

	}

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 根据主键获取筹资管理库存表vo。若查找不到库存，考虑默认为新增库存。
	 * 
	 * @author qs
	 * @since 2017-6-5下午4:27:18
	 */
	@Override
	
	public InventoryInfoVO getInventoryInfoVO(BanlanceQueryKeyVO sbqkVO,
			ICostingTool costingtool) throws BusinessException {
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> stockmap = scostcalcachemanager
				.getCacheByTypeCode(SystemConst.InventoryInfo, costingtool);
		String pk_stocksort = sbqkVO.getPk_stocksort();
		String pk_assetsprop = sbqkVO.getPk_assetsprop();
		String trade_date = sbqkVO.getTrade_date();
		String key = sbqkVO.getKey();
		if (sbqkVO.getPk_stocksort() == null) {
			pk_stocksort = SystemConst.pk_defaultstocksort;
			Logger.debug("getInventoryInfoVO时: 库存组织为空,取默认库存组织!");
		}
		if (stockmap.get(trade_date).get(pk_assetsprop) == null) {
			return null;
		}
		if (stockmap.get(trade_date).get(pk_assetsprop).get(pk_stocksort) == null) {
			return null;
		}
		SuperVO vo = stockmap.get(trade_date).get(pk_assetsprop)
				.get(pk_stocksort).get(key);
		return (InventoryInfoVO) VODeepCloneUtil.deepClone(vo);
	}

	/**
	 * 根据主键更新库存
	 * 
	 * @author qs
	 * @since 2017-6-5下午6:11:56
	 */
	
	@Override
	public void updateInventoryInfoVO(BanlanceQueryKeyVO sbqkVO,
			InventoryInfoVO vo) throws BusinessException {
		scostcalcachemanager.getCacheInstanceBytypeCode(
				SystemConst.InventoryInfo).updateCache(sbqkVO, vo);
	}

	/**
	 * 更新筹资管理库存缓存
	 * 
	 * @author qs
	 * @since 2017-6-6上午9:25:20
	 */
	private void updateInventoryInfocache(String trade_date) throws Exception {

		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> newmap = scostcalcachemanager
				.getCacheInstanceBytypeCode(SystemConst.InventoryInfo)
				.getCache(null);

		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = newmap.get(trade_date).keySet();
		Iterator<String> it = keys.iterator();
		Collection<ConcurrentHashMap<String, InventoryInfoVO>> vosmap = null;
		Collection<InventoryInfoVO> vos = null;
		List<InventoryInfoVO> updatevos = new ArrayList<InventoryInfoVO>();
		while (it.hasNext()) {
			vosmap = newmap.get(trade_date).get(it.next()).values();
			for (Map<String, InventoryInfoVO> map : vosmap) {
				vos = map.values();
				if (vos == null || vos.isEmpty()) {
					continue;
				}
				for (InventoryInfoVO inventoryInfoVO : vos) {
					if (inventoryInfoVO.getStocks_num() == null
							|| inventoryInfoVO.getStocks_num().doubleValue() < 0
							|| inventoryInfoVO.getStocks_sum() == null
							|| inventoryInfoVO.getStocks_sum().doubleValue() < 0) {
						return;
					}
					/**
					 * 只插入新增的
					 */
					if (inventoryInfoVO.getIsnew()) {
						updatevos.add(inventoryInfoVO);
						inventoryInfoVO.setTrade_date(new UFDate(trade_date));
						/**
						 * 使用完毕后 还原 防止下一日仍然插入 YangJie 2014-04-15
						 */
						inventoryInfoVO.setIsnew(false);
					}
				}

			}
		}
		getBaseDao().insertVOArray(
				updatevos.toArray(new InventoryInfoVO[updatevos.size()]));

	}

}
