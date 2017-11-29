package nc.bs.fba_scost.cost.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.logging.Logger;
import nc.impl.fba_scost.cost.pub.PrivateMethod;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_scost.cost.tool.IScostCheckCache;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.pub.tools.VODeepCloneUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.trade.voutils.VOUtil;

/**
 * 筹资管理期初库存缓存
 * 
 * @author qs
 * @since 2017-6-5下午4:51:34
 */
public class InventoryInfoCache implements IScostCheckCache {
	private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> inventoryInfoCacheMap = null;
	private CostParaVO costParaVO = null;

	@Override
	public Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>> getCache(
			ICostingTool costingtool) throws BusinessException {
		if (inventoryInfoCacheMap == null) {
			this.costParaVO = costingtool.getCostParaVO();
			initInventoryInfoCache(costingtool);
		} else if (costingtool != null) {
			initInventoryInfoCache(costingtool);
		}
		return inventoryInfoCacheMap;
	}

	@Override
	public void updateCache(BanlanceQueryKeyVO sbqkVO, SuperVO vo)
			throws BusinessException {
		InventoryInfoVO inventoryInfoVO = (InventoryInfoVO) VODeepCloneUtil
				.deepClone(vo);
		inventoryInfoVO.setAttributeValue("dr", 0);
		inventoryInfoVO.setIsnew(true);
		String pk_stocksort = sbqkVO.getPk_stocksort();
		String pk_assetsprop = sbqkVO.getPk_assetsprop();
		String trade_date = sbqkVO.getTrade_date();
		String key = sbqkVO.getKey();
		if (sbqkVO.getPk_stocksort() == null) {
			pk_stocksort = SystemConst.pk_defaultstocksort;
			Logger.debug("getInventoryInfoVO时: 库存组织为空,取默认库存组织!");
		}

		if (inventoryInfoCacheMap.get(trade_date).get(pk_assetsprop) == null) {
			ConcurrentHashMap<String, InventoryInfoVO> childmap = new ConcurrentHashMap<String, InventoryInfoVO>();
			childmap.put(key, inventoryInfoVO);
			ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>> map = new ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>();
			map.put(pk_stocksort, childmap);
			inventoryInfoCacheMap.get(trade_date).put(pk_assetsprop, map);
		} else if (inventoryInfoCacheMap.get(trade_date).get(pk_assetsprop)
				.get(pk_stocksort) == null) {
			ConcurrentHashMap<String, InventoryInfoVO> childmap = new ConcurrentHashMap<String, InventoryInfoVO>();
			childmap.put(key, inventoryInfoVO);
			inventoryInfoCacheMap.get(trade_date).get(pk_assetsprop)
					.put(pk_stocksort, childmap);
		} else {
			inventoryInfoCacheMap.get(trade_date).get(pk_assetsprop)
					.get(pk_stocksort).put(key, inventoryInfoVO);
		}

	}

	@Override
	public void clearCache() {
		if (inventoryInfoCacheMap != null) {
			inventoryInfoCacheMap.clear();
			inventoryInfoCacheMap = null;
		}
	}

	/**
	 * 初始化trade_date当天的筹资管理库存期初
	 * 
	 * @date 2012-9-10 下午3:58:30
	 */
	@SuppressWarnings("unchecked")
	private void initInventoryInfoCache(ICostingTool costingtool)
			throws BusinessException {
		/**
		 * 判定是否初始化缓存 true 初始化缓存 false 利用昨日缓存初始化下日缓存并清掉前日缓存
		 */
		String trade_date = costingtool.getCurrdate();
		if (costingtool.getIsinit()) {
			if (inventoryInfoCacheMap != null) {
				List<InventoryInfoVO> list = (List<InventoryInfoVO>) PrivateMethod
						.getInstance()
						.getBaseDAO()
						.executeQuery(getSql(costingtool),
								new BeanListProcessor(InventoryInfoVO.class));
				if (list != null && list.size() > 0) {
					// 按金融资产属性分组
					String[] hashkey = new String[1];
					hashkey[0] = "pk_assetsprop";
					Map<String, List<InventoryInfoVO>> tradedatemap = PubMethod
							.getInstance().hashlizeObjects(list, hashkey);
					// 按库存组织分组
					hashkey[0] = "pk_stocksort";
					List<InventoryInfoVO> assetsproplist = null;// 某金融资产属性所有库存
					Iterator<Entry<String, List<InventoryInfoVO>>> iter = tradedatemap
							.entrySet().iterator();
					Entry<String, List<InventoryInfoVO>> entry;
					String key;
					Map<String, List<InventoryInfoVO>> tradedatemap2 = null;// 某金融资产属性库存按库存组织分组
					Iterator<Entry<String, List<InventoryInfoVO>>> iter2;
					Entry<String, List<InventoryInfoVO>> entry2;
					ConcurrentHashMap<String, InventoryInfoVO> tradevomap = null;
					String key2;
					while (iter.hasNext()) {// 遍历金融资产属性
						entry = iter.next();
						key = entry.getKey();
						assetsproplist = tradedatemap.get(key);
						tradedatemap2 = PubMethod.getInstance()
								.hashlizeObjects(assetsproplist, hashkey);
						// keyset2 = tradedatemap2.keySet();
						iter2 = tradedatemap2.entrySet().iterator();

						while (iter2.hasNext()) {// 遍历库存组织
							entry2 = iter2.next();
							key2 = entry2.getKey();
							tradevomap = (ConcurrentHashMap<String, InventoryInfoVO>) PubMethod
									.getInstance()
									.hashlizeObject(
											tradedatemap2.get(key2),
											PubMethod
													.getInstance()
													.getCostArrayWithBilltypeGroup(
															costParaVO
																	.getCostplanvo()
																	.getFundCostFieldArray()));// 按分组字段分组
							if (inventoryInfoCacheMap.get(trade_date).get(key)
									.get(key2) == null) {
								inventoryInfoCacheMap.get(trade_date).get(key)
										.put(key2, tradevomap);
							} else {
								inventoryInfoCacheMap.get(trade_date).get(key)
										.get(key2).putAll(tradevomap);
							}
						}
					}
				}
			} else {
				inventoryInfoCacheMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>>();
				inventoryInfoCacheMap
						.put(trade_date,
								new ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>>());
				List<InventoryInfoVO> list = (List<InventoryInfoVO>) PrivateMethod
						.getInstance()
						.getBaseDAO()
						.executeQuery(getSql(costingtool),
								new BeanListProcessor(InventoryInfoVO.class));
				if (list != null && list.size() > 0) {
					// 按金融资产属性分组
					String[] hashkey = new String[1];
					hashkey[0] = "pk_assetsprop";
					Map<String, List<InventoryInfoVO>> tradedatemap = PubMethod
							.getInstance().hashlizeObjects(list, hashkey);
					// 按库存组织分组
					hashkey[0] = "pk_stocksort";
					List<InventoryInfoVO> assetsproplist = null;// 某金融资产属性所有库存
					Iterator<Entry<String, List<InventoryInfoVO>>> iter = tradedatemap
							.entrySet().iterator();
					Entry<String, List<InventoryInfoVO>> entry;
					String key;
					Map<String, List<InventoryInfoVO>> tradedatemap2 = null;// 某金融资产属性库存按库存组织分组
					Iterator<Entry<String, List<InventoryInfoVO>>> iter2;
					Entry<String, List<InventoryInfoVO>> entry2;
					ConcurrentHashMap<String, InventoryInfoVO> tradevomap = null;
					String key2;
					while (iter.hasNext()) {// 遍历金融资产属性
						entry = iter.next();
						key = entry.getKey();
						inventoryInfoCacheMap
								.get(trade_date)
								.put(key,
										new ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>());
						assetsproplist = tradedatemap.get(key);
						tradedatemap2 = PubMethod.getInstance()
								.hashlizeObjects(assetsproplist, hashkey);
						// keyset2 = tradedatemap2.keySet();
						iter2 = tradedatemap2.entrySet().iterator();

						while (iter2.hasNext()) {// 遍历库存组织
							entry2 = iter2.next();
							key2 = entry2.getKey();
							tradevomap = (ConcurrentHashMap<String, InventoryInfoVO>) PubMethod
									.getInstance()
									.hashlizeObject(
											tradedatemap2.get(key2),
											PubMethod
													.getInstance()
													.getCostArrayWithBilltypeGroup(
															costParaVO
																	.getCostplanvo()
																	.getFundCostFieldArray()));// 按分组字段分组
							inventoryInfoCacheMap.get(trade_date).get(key)
									.put(key2, tradevomap);
						}
					}
				}
			}
		} else {
			/**
			 * 若当日有缓存直接拿出来 否则回溯 回溯上一数据日缓存 YangJie 2014-04-04
			 */
			if (inventoryInfoCacheMap.get(trade_date) == null
					|| inventoryInfoCacheMap.get(trade_date).size() == 0) {
				UFDate beforetradedate = new UFDate(trade_date)
						.getDateBefore(1);
				UFDate lastdate = costingtool.getCurrbilltypegroupvo()
						.getLastapprovedate() == null ? new UFDate(0L)
						: costingtool.getCurrbilltypegroupvo()
								.getLastapprovedate();
				while (!(lastdate.compareTo(beforetradedate) > 0)) {
					if (inventoryInfoCacheMap.get(beforetradedate
							.toLocalString()) != null) {
						ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, InventoryInfoVO>>> newmap = inventoryInfoCacheMap
								.get(beforetradedate.toLocalString());
						Set<String> keys = newmap.keySet();
						Iterator<String> it = keys.iterator();
						Collection<ConcurrentHashMap<String, InventoryInfoVO>> vosmap = null;
						while (it.hasNext()) {
							vosmap = newmap.get(it.next()).values();
							for (Map<String, InventoryInfoVO> map : vosmap) {
								Iterator<String> iz = map.keySet().iterator();
								while (iz.hasNext()) {
									String keya = iz.next();
									InventoryInfoVO balancevo = map.get(keya);
									// 限售期增加
									UFLiteralDate enddate = balancevo
											.getEnd_date();
									UFLiteralDate aduitdate = new UFLiteralDate(
											trade_date);
									if (enddate != null
											&& aduitdate.after(enddate)) {
										balancevo.setBegin_date(null);
										balancevo.setEnd_date(null);
										String key11 = VOUtil
												.getCombinesKey(
														balancevo,
														PubMethod
																.getInstance()
																.getCostArrayWithBilltypeGroup(
																		costParaVO
																				.getCostplanvo()
																				.getFundCostFieldArray()));
										map.put(key11, balancevo);
										map.remove(keya);
									}
									balancevo.setTrade_date(new UFDate(
											trade_date));
								}
							}
						}
						inventoryInfoCacheMap.put(trade_date, newmap);
						break;
					}
					beforetradedate = beforetradedate.getDateBefore(1);
				}
			}
		}
	}

	private String getSql(ICostingTool costingtool) {
		costParaVO = costingtool.getCostParaVO();
		CheckParaVO checkparavo = costParaVO.getCheckParavo();
		String pk_glorgbook = checkparavo.getPk_glorgbook();
		String pk_org = checkparavo.getPk_org();
		String pk_group = checkparavo.getPk_group();
		String trade_date = costingtool.getCurrdate();
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer manjoinSb = new StringBuffer();
		StringBuffer sqlsb = new StringBuffer();
		String[] costItemCodes = costParaVO.getCostplanvo()
				.getFundCostFieldArray();// 管理项目设置的字段
		String[] manItemCodes = PubMethod.getInstance()
				.getCostArrayWithBilltypeGroup(costItemCodes);
		for (int i = 0; i < manItemCodes.length; i++) {
			manjoinSb.append(" and isnull(x.").append(manItemCodes[i])
					.append(",'").append(manItemCodes[i])
					.append("') = isnull(y.").append(manItemCodes[i])
					.append(",'").append(manItemCodes[i]).append("')");
		}
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append("a." + manItemCodes[i]);
			manItemSb.append(",");
		}
		sqlsb.append("select /*+ no_merge(x) leading(x) */ y.* from (select max(a.trade_date) trade_date,");
		sqlsb.append(manItemSb.toString());
		sqlsb.append("a.pk_costplan");
		sqlsb.append(" from fund_inventoryinfo a ");
		// sqlsb.append(" from fund_inventoryinfo a inner join ");
		// sqlsb.append("sec_securities b on a.pk_securities=b.pk_securities ");
		sqlsb.append("where a.trade_date < '" + trade_date
				+ "' and isnull(a.dr,0)=0 ");
		sqlsb.append(" and a.pk_costplan='");
		sqlsb.append(costParaVO.getCostplanvo().getPk_costplan()).append("' ");

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
		// sqlsb.append(" and x.pk_costplan = y.pk_costplan and isnull(y.dr,0)=0 order by y.end_date");
		sqlsb.append(" and x.pk_costplan = y.pk_costplan and isnull(y.dr,0)=0 order by y.enddate");
		return sqlsb.toString();

	}

}
