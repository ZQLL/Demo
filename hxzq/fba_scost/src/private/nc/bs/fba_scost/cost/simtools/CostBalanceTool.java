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
import nc.itf.fba_scost.cost.tool.IScostCheckCache;
import nc.vo.fba_fund.interbanklending.InterbankLendingVO;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.fundbalance.FundBalanceVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.transferbalance.TransferBalanceVO;
import nc.vo.fba_scost.pub.tools.VODeepCloneUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

public class CostBalanceTool implements ICostBalanceTool {
	private static final FundBalanceVO fundbalancevo = null;
	private IScostCalCacheManager scostcalcachemanager;
	private static CostBalanceTool tool;
	private BaseDAO dao;

	public IScostCalCacheManager getScostcalcachemanager() {
		return this.scostcalcachemanager;
	}

	public void setScostcalcachemanager(
			IScostCalCacheManager scostcalcachemanager) {
		this.scostcalcachemanager = scostcalcachemanager;
	}

	private Map<String, StockBalanceVO> costchangevomap = new HashMap();
	public Map<String, Map<String, TransferBalanceVO>> transfermap = new HashMap();

	public CostBalanceTool getInstance() {
		if (tool == null) {
			tool = new CostBalanceTool();
		}
		return tool;
	}

	public CostBalanceTool() {
	}

	public CostBalanceTool(CostParaVO costParaVO) {
	}

	public StockBalanceVO getStockbalanceVO(BanlanceQueryKeyVO sbqkVO,
			ICostingTool costingtool) throws BusinessException {
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>> stockmap = this.scostcalcachemanager
				.getCacheByTypeCode("stockbalance", costingtool);
		String pk_stocksort = sbqkVO.getPk_stocksort();
		String pk_assetsprop = sbqkVO.getPk_assetsprop();
		String trade_date = sbqkVO.getTrade_date();
		String key = sbqkVO.getKey();
		if (sbqkVO.getPk_stocksort() == null) {
			pk_stocksort = "0001SE00000000000001";
			Logger.debug("getStockbalanceVO时: 库存组织为空,取默认库存组织!");
		}
		if (((ConcurrentHashMap) stockmap.get(trade_date)).get(pk_assetsprop) == null) {
			return null;
		}
		if (((ConcurrentHashMap) ((ConcurrentHashMap) stockmap.get(trade_date))
				.get(pk_assetsprop)).get(pk_stocksort) == null) {
			return null;
		}
		SuperVO vo = (SuperVO) ((ConcurrentHashMap) ((ConcurrentHashMap) ((ConcurrentHashMap) stockmap
				.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort))
				.get(key);
		return (StockBalanceVO) VODeepCloneUtil.deepClone(vo);
	}

	public TransferBalanceVO getTransferbalanceVO(String key,
			ICostingTool costingtool) throws BusinessException {
		Map<String, Map<String, TransferBalanceVO>> transfermap = this.scostcalcachemanager
				.getCacheByTypeCode("transferbalance", costingtool);
		String trade_date = costingtool.getCurrdate();
		if (transfermap.get(trade_date) != null) {
			return (TransferBalanceVO) VODeepCloneUtil
					.deepClone((SuperVO) ((Map) transfermap.get(trade_date))
							.get(key));
		}
		return null;
	}

	public TransferBalanceVO getTransferBalanceVOByVO(SuperVO tradevo,
			ICostingTool costingtool) throws BusinessException {
		try {
			Map<String, Map<String, TransferBalanceVO>> transfermap = this.scostcalcachemanager
					.getCacheByTypeCode("transferbalance", costingtool);
			String trade_date = costingtool.getCurrdate();
			if (transfermap.get(trade_date) == null) {
				transfermap.put(trade_date, new ConcurrentHashMap());
			}
			TransferBalanceVO vo = new TransferBalanceVO();

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

	public FundBalanceVO getFundbalanceVO(String key, ICostingTool costingtool)
			throws BusinessException {
		Map<String, Map<String, FundBalanceVO>> fundBalancemap = this.scostcalcachemanager
				.getCacheByTypeCode("fundbalance", costingtool);
		String trade_date = costingtool.getCurrdate();
		if (fundBalancemap.get(trade_date) != null) {
			return (FundBalanceVO) VODeepCloneUtil
					.deepClone((SuperVO) ((Map) fundBalancemap.get(trade_date))
							.get(key));
		}
		return null;
	}

	public FundBalanceVO getFundbalanceVOByVO(SuperVO tradevo,
			ICostingTool costingtool) {
		String[] keys = costingtool.getCostParaVO().getCostplanvo()
				.getFundsFieldArray();
		FundBalanceVO vo = new FundBalanceVO();
		vo.setStocks_sum(new UFDouble(0));
		for (String key : keys) {
			vo.setAttributeValue(key, tradevo.getAttributeValue(key));
		}
		vo.setAttributeValue("pk_capaccount",
				tradevo.getAttributeValue("pk_capaccount"));
		return vo;
	}

	public void updateStockbalanceVO(BanlanceQueryKeyVO sbqkVO,
			StockBalanceVO vo) throws BusinessException {
		this.scostcalcachemanager.getCacheInstanceBytypeCode("stockbalance")
				.updateCache(sbqkVO, vo);
	}

	public void updateTransferBalanceVO(BanlanceQueryKeyVO sbqkVO,
			TransferBalanceVO vo) throws BusinessException {
		this.scostcalcachemanager.getCacheInstanceBytypeCode("transferbalance")
				.updateCache(sbqkVO, vo);
	}

	public void updateFundbalanceVO(BanlanceQueryKeyVO sbqkVO, FundBalanceVO vo)
			throws BusinessException {
		this.scostcalcachemanager.getCacheInstanceBytypeCode("fundbalance")
				.updateCache(sbqkVO, vo);
	}

	public StockBalanceVO getStockbalanceVOByVO(SuperVO tradevo,
			ICostingTool costingtool) throws BusinessException {
		String[] manFieldV = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		String pk_assetsprop = (String) tradevo
				.getAttributeValue("pk_assetsprop");
		String pk_stocksort = (String) tradevo
				.getAttributeValue("pk_stocksort");
		StockBalanceVO vo = new StockBalanceVO();
		vo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo()
				.getPk_costplan());
		if ("HV3F".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field,
						tradevo.getAttributeValue("hr_" + field));
			}
		} else if ("HV3G".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field,
						tradevo.getAttributeValue("hr_" + field));
			}
			vo.setAttributeValue("pk_securities",
					tradevo.getAttributeValue("pk_securities"));
		} else if ("HV9B".equals(tradevo.getAttributeValue("billtypecode"))) {
			for (String field : manFieldV) {
				vo.setAttributeValue(field,
						tradevo.getAttributeValue("hr_" + field));
			}
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
		vo.setState(Integer.valueOf(0));
		vo.getPk_assetsprop();
		String trade_date = costingtool.getCurrdate();
		String pk_billtypegroup = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup();
		String key = VOUtil.getCombinesKey(vo, manFieldV);
		key = pk_billtypegroup + key;
		vo.setPk_billtypegroup(pk_billtypegroup);
		pk_assetsprop = vo.getPk_assetsprop();
		pk_stocksort = vo.getPk_stocksort();

		Object stockmap = this.scostcalcachemanager.getCacheByTypeCode(
				"stockbalance", costingtool);
		if (((ConcurrentHashMap) ((Map) stockmap).get(trade_date))
				.get(pk_assetsprop) == null) {
			((ConcurrentHashMap) ((Map) stockmap).get(trade_date)).put(
					pk_assetsprop, new ConcurrentHashMap());
		}
		if (((ConcurrentHashMap) ((ConcurrentHashMap) ((Map) stockmap)
				.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort) == null) {
			((ConcurrentHashMap) ((ConcurrentHashMap) ((Map) stockmap)
					.get(trade_date)).get(pk_assetsprop)).put(pk_stocksort,
					new ConcurrentHashMap());
		}
		((ConcurrentHashMap) ((ConcurrentHashMap) ((ConcurrentHashMap) ((Map) stockmap)
				.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort)).put(
				key, vo);
		return vo;
	}

	public InventoryInfoVO getInventoryInfoVOByVO(SuperVO tradevo,
			ICostingTool costingtool) throws BusinessException {
		String[] manFieldV = costingtool.getCostParaVO().getCostplanvo()
				.getFundCostFieldArray();
		String pk_assetsprop = (String) tradevo
				.getAttributeValue("pk_assetsprop");
		String pk_stocksort = (String) tradevo
				.getAttributeValue("pk_stocksort");
		InventoryInfoVO vo = new InventoryInfoVO();
		vo.setPk_costplan(costingtool.getCostParaVO().getCostplanvo()
				.getPk_costplan());
		for (String field : manFieldV) {
			vo.setAttributeValue(field, tradevo.getAttributeValue(field));
		}
		vo.setTranstypecode((String) tradevo.getAttributeValue("transtypecode"));

		vo.setPk_transtype((String) tradevo.getAttributeValue("pk_transtype"));

		vo.setBilltypecode((String) tradevo.getAttributeValue("billtypecode"));

		vo.setPk_billtype((String) tradevo.getAttributeValue("pk_billtype"));

		vo.setStartdate(((UFDate) tradevo.getAttributeValue("trade_date"))
				.asBegin());

		vo.setEnddate(((UFDate) tradevo.getAttributeValue("enddate")).asBegin());
		if (vo.getTranstypecode().equals("HV8E-0xx-01")) {
			Integer timelimit = Integer.valueOf(UFDate.getDaysBetween(
					(UFDate) tradevo.getAttributeValue("trade_date"),
					(UFDate) tradevo.getAttributeValue("enddate")) + 1);

			vo.setTimelimit(timelimit);
		} else {
			vo.setTimelimit((Integer) tradevo.getAttributeValue("timelimit"));
		}
		vo.setPaytype((Integer) tradevo.getAttributeValue("paytype"));

		vo.setExecutefacerate((UFDouble) tradevo
				.getAttributeValue("contractrate"));

		vo.setFundtype((Integer) tradevo.getAttributeValue("fundtype"));

		vo.setProductorcounterparty((String) tradevo
				.getAttributeValue("productorcounterparty"));

		vo.setManagedept((String) tradevo.getAttributeValue("managedept"));

		vo.setRealrate((UFDouble) tradevo.getAttributeValue("realrate"));

		vo.setPk_org_v((String) tradevo.getAttributeValue("pk_org_v"));
		vo.setPk_glorgbook((String) tradevo.getAttributeValue("pk_glorgbook"));
		vo.setTrade_date(new UFDate(costingtool.getCurrdate()));
		vo.setStocks_sum(new UFDouble(0));
		vo.setStocks_num(new UFDouble(0));
		vo.setState(Integer.valueOf(0));
		vo.getPk_assetsprop();
		String trade_date = costingtool.getCurrdate();
		String pk_billtypegroup = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup();
		String key = VOUtil.getCombinesKey(vo, manFieldV);
		key = pk_billtypegroup + key;
		vo.setPk_billtypegroup(pk_billtypegroup);
		pk_assetsprop = vo.getPk_assetsprop();
		pk_stocksort = vo.getPk_stocksort();
		Object stockmap = this.scostcalcachemanager.getCacheByTypeCode(
				"inventoryinfo", costingtool);
		if(stockmap != null){
			if (((ConcurrentHashMap) ((Map) stockmap).get(trade_date))
					.get(pk_assetsprop) == null) {
				((ConcurrentHashMap) ((Map) stockmap).get(trade_date)).put(
						pk_assetsprop, new ConcurrentHashMap());
			}
			if (((ConcurrentHashMap) ((ConcurrentHashMap) ((Map) stockmap)
					.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort) == null) {
				((ConcurrentHashMap) ((ConcurrentHashMap) ((Map) stockmap)
						.get(trade_date)).get(pk_assetsprop)).put(pk_stocksort,
						new ConcurrentHashMap());
			}
			((ConcurrentHashMap) ((ConcurrentHashMap) ((ConcurrentHashMap) ((Map) stockmap)
					.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort)).put(
					key, vo);
		}
		return vo;
	}

	private void initAllBalance() {
	}

	public void clearAllCache() {
	}

	public void updateAll(String trade_date) throws Exception {
		updateStockBalancecache(trade_date);
		updateFundBalancecache(trade_date);
		updateTransferBalancecache(trade_date);
		updateInventoryInfocache(trade_date);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateStockBalancecache(String trade_date) throws Exception {
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>> newmap = this.scostcalcachemanager
				.getCacheInstanceBytypeCode("stockbalance").getCache(null);
		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = ((ConcurrentHashMap) newmap.get(trade_date))
				.keySet();
		Iterator<String> it = keys.iterator();
		Collection<ConcurrentHashMap<String, StockBalanceVO>> vosmap = null;
		Collection<StockBalanceVO> vos = null;
		List<StockBalanceVO> updatevos = new ArrayList();
		Iterator localIterator1;
		for (; it.hasNext(); localIterator1.hasNext()) {
			vosmap = ((ConcurrentHashMap) ((ConcurrentHashMap) newmap
					.get(trade_date)).get(it.next())).values();
			localIterator1 = vosmap.iterator();
			if (vosmap == null || vosmap.isEmpty()) {
				continue;
			}
			Map<String, StockBalanceVO> map = (Map) localIterator1.next();
			vos = map.values();
			if ((vos != null) && (!vos.isEmpty())) {
				for (StockBalanceVO balancevo : vos) {
					if ((balancevo.getStocks_num() == null)
							|| (balancevo.getStocks_num().doubleValue() < 0.0D)
							|| (balancevo.getStocks_sum() == null)
							|| (balancevo.getStocks_sum().doubleValue() < 0.0D)) {
						return;
					}
					if (balancevo.getIsnew()) {
						updatevos.add(balancevo);
						balancevo.setTrade_date(new UFDate(trade_date));

						balancevo.setIsnew(false);
					}
				}
			}
		}
		getBaseDao().insertVOArray(
				(SuperVO[]) updatevos.toArray(new StockBalanceVO[updatevos
						.size()]));
	}

	private void updateFundBalancecache(String trade_date) throws Exception {
		Map<String, Map<String, FundBalanceVO>> newmap = this.scostcalcachemanager
				.getCacheInstanceBytypeCode("fundbalance").getCache(null);
		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = ((Map) newmap.get(trade_date)).keySet();
		Iterator<String> it = keys.iterator();
		List<FundBalanceVO> updatevos = new ArrayList();
		while (it.hasNext()) {
			FundBalanceVO vo = (FundBalanceVO) ((Map) newmap.get(trade_date))
					.get(it.next());
			if (vo.getIsnew()) {
				updatevos.add(vo);
				vo.setIsnew(false);
			}
		}
		getBaseDao().insertVOArray(
				(SuperVO[]) updatevos.toArray(new FundBalanceVO[updatevos
						.size()]));
	}

	private void updateTransferBalancecache(String trade_date) throws Exception {
		Map<String, Map<String, TransferBalanceVO>> newmap = this.scostcalcachemanager
				.getCacheInstanceBytypeCode("transferbalance").getCache(null);
		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = ((Map) newmap.get(trade_date)).keySet();
		Iterator<String> it = keys.iterator();
		List<TransferBalanceVO> updatevos = new ArrayList();
		while (it.hasNext()) {
			TransferBalanceVO vo = (TransferBalanceVO) ((Map) newmap
					.get(trade_date)).get(it.next());
			if (vo.getIsnew()) {
				updatevos.add(vo);
				vo.setIsnew(false);
			}
		}
		getBaseDao().insertVOArray(
				(SuperVO[]) updatevos.toArray(new TransferBalanceVO[updatevos
						.size()]));
	}

	private BaseDAO getBaseDao() {
		if (this.dao == null) {
			this.dao = new BaseDAO();
		}
		return this.dao;
	}

	public InventoryInfoVO getInventoryInfoVO(BanlanceQueryKeyVO sbqkVO,
			ICostingTool costingtool) throws BusinessException {
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> stockmap = this.scostcalcachemanager
				.getCacheByTypeCode("inventoryinfo", costingtool);
		String pk_stocksort = sbqkVO.getPk_stocksort();
		String pk_assetsprop = sbqkVO.getPk_assetsprop();
		String trade_date = sbqkVO.getTrade_date();
		String key = sbqkVO.getKey();
		if (sbqkVO.getPk_stocksort() == null) {
			pk_stocksort = "0001SE00000000000001";
			Logger.debug("getInventoryInfoVO时: 库存组织为空,取默认库存组织!");
		}
		if(stockmap==null){
			return null;
		}
		if (((ConcurrentHashMap) stockmap.get(trade_date)).get(pk_assetsprop) == null) {
			return null;
		}
		if (((ConcurrentHashMap) ((ConcurrentHashMap) stockmap.get(trade_date))
				.get(pk_assetsprop)).get(pk_stocksort) == null) {
			return null;
		}
		SuperVO vo = (SuperVO) ((ConcurrentHashMap) ((ConcurrentHashMap) ((ConcurrentHashMap) stockmap
				.get(trade_date)).get(pk_assetsprop)).get(pk_stocksort))
				.get(key);
		return (InventoryInfoVO) VODeepCloneUtil.deepClone(vo);
	}

	public void updateInventoryInfoVO(BanlanceQueryKeyVO sbqkVO,
			InventoryInfoVO vo) throws BusinessException {
		this.scostcalcachemanager.getCacheInstanceBytypeCode("inventoryinfo")
				.updateCache(sbqkVO, vo);
	}

	private void updateInventoryInfocache(String trade_date) throws Exception {
		Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> newmap = this.scostcalcachemanager
				.getCacheInstanceBytypeCode("inventoryinfo").getCache(null);
		if (newmap.get(trade_date) == null) {
			return;
		}
		Set<String> keys = ((ConcurrentHashMap) newmap.get(trade_date))
				.keySet();
		Iterator<String> it = keys.iterator();
		Collection<ConcurrentHashMap<String, InventoryInfoVO>> vosmap = null;
		Collection<InventoryInfoVO> vos = null;
		List<InventoryInfoVO> updatevos = new ArrayList();
		Iterator localIterator1;
		for (; it.hasNext(); localIterator1.hasNext()) {
			vosmap = ((ConcurrentHashMap) ((ConcurrentHashMap) newmap
					.get(trade_date)).get(it.next())).values();
			localIterator1 = vosmap.iterator();
			if (vosmap == null || vosmap.isEmpty()) {
				continue;
			}
			Map<String, InventoryInfoVO> map = (Map) localIterator1.next();
			vos = map.values();
			if ((vos != null) && (!vos.isEmpty())) {
				for (InventoryInfoVO inventoryInfoVO : vos) {
					if ((inventoryInfoVO.getStocks_num() == null)
							|| (inventoryInfoVO.getStocks_num().doubleValue() < 0.0D)
							|| (inventoryInfoVO.getStocks_sum() == null)
							|| (inventoryInfoVO.getStocks_sum().doubleValue() < 0.0D)) {
						return;
					}
					if (inventoryInfoVO.getIsnew()) {
						updatevos.add(inventoryInfoVO);
						inventoryInfoVO.setTrade_date(new UFDate(trade_date));

						inventoryInfoVO.setIsnew(false);
					}
				}
			}
		}
		getBaseDao().insertVOArray(
				(SuperVO[]) updatevos.toArray(new InventoryInfoVO[updatevos
						.size()]));
	}
}
