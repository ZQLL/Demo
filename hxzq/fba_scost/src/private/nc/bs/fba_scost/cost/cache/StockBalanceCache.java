package nc.bs.fba_scost.cost.cache;

import java.util.concurrent.ConcurrentHashMap;

import nc.bs.fba_scost.cost.pub.StockLimitValue;
import nc.bs.logging.Logger;
import nc.impl.fba_scost.cost.pub.PrivateMethod;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_scost.cost.tool.IScostCheckCache;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.pub.tools.VODeepCloneUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.trade.voutils.VOUtil;
import java.util.*;
import java.util.Map.Entry;
/**
 * 库存缓存
 * 
 * @author yangjie
 * 
 */
public class StockBalanceCache implements IScostCheckCache {

	private Map<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>> stockBalCacheMap = null;
	private CostParaVO costParaVO = null;

	/**
	 * YangJie 2014-04-04 costingtool 为空时 直接返回 缓存map costingtool 不为空时
	 * 按照规则初始化缓存（1.isinit为true，增量初始化缓存2.isinit为false，用当前日初始化下一日缓存）
	 */
	@Override
	
	public Map getCache(ICostingTool costingtool) throws BusinessException {
		if (stockBalCacheMap == null) {
			this.costParaVO = costingtool.getCostParaVO();
			initStockBalanceCache(costingtool);
		} else if (costingtool != null) {
			initStockBalanceCache(costingtool);
		}
		return stockBalCacheMap;
	}

	private void initStockBalanceCache(ICostingTool costingtool)
			throws BusinessException {
		/**
		 * 判定是否初始化缓存 true 初始化缓存 false 利用昨日缓存初始化下日缓存并清掉前日缓存
		 */
		String trade_date = costingtool.getCurrdate();
		if (costingtool.getIsinit()) {
			if (stockBalCacheMap != null) {
				List<StockBalanceVO> list = (List<StockBalanceVO>) PrivateMethod
						.getInstance()
						.getBaseDAO()
						.executeQuery(getSql(costingtool),
								new BeanListProcessor(StockBalanceVO.class));
				list = new StockLimitValue().doLimitFairValue(list, trade_date,
						costParaVO.getCostplanvo().getCostFieldArray());
				// stockBalCacheMap.get(trade_date);
				if (list != null && list.size() > 0) {
					// 按金融资产属性分组
					String[] hashkey = new String[1];
					hashkey[0] = "pk_assetsprop";
					Map<String, List<StockBalanceVO>> tradedatemap = PubMethod
							.getInstance().hashlizeObjects(list, hashkey);
					// 按库存组织分组
					hashkey[0] = "pk_stocksort";
					List<StockBalanceVO> assetsproplist = null;// 某金融资产属性所有库存
					Iterator<Entry<String, List<StockBalanceVO>>> iter = tradedatemap
							.entrySet().iterator();
					Entry<String, List<StockBalanceVO>> entry;
					String key;
					Map<String, List<StockBalanceVO>> tradedatemap2 = null;// 某金融资产属性库存按库存组织分组
					Iterator<Entry<String, List<StockBalanceVO>>> iter2;
					Entry<String, List<StockBalanceVO>> entry2;
					ConcurrentHashMap<String, StockBalanceVO> tradevomap = null;
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
							tradevomap = (ConcurrentHashMap<String, StockBalanceVO>) PubMethod
									.getInstance()
									.hashlizeObject(
											tradedatemap2.get(key2),
											PubMethod
													.getInstance()
													.getCostArrayWithBilltypeGroup(
															costParaVO
																	.getCostplanvo()
																	.getCostFieldArray()));// 按分组字段分组
							if (stockBalCacheMap.get(trade_date).get(key)
									.get(key2) == null) {
								stockBalCacheMap.get(trade_date).get(key)
										.put(key2, tradevomap);
							} else {
								stockBalCacheMap.get(trade_date).get(key)
										.get(key2).putAll(tradevomap);
							}
						}
					}
				}

			} else {
				stockBalCacheMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>>();
				stockBalCacheMap
						.put(trade_date,
								new ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>>());
				List<StockBalanceVO> list = (List<StockBalanceVO>) PrivateMethod
						.getInstance()
						.getBaseDAO()
						.executeQuery(getSql(costingtool),
								new BeanListProcessor(StockBalanceVO.class));
				list = new StockLimitValue().doLimitFairValue(list, trade_date,
						costParaVO.getCostplanvo().getCostFieldArray());
				if (list != null && list.size() > 0) {
					// 按金融资产属性分组
					String[] hashkey = new String[1];
					hashkey[0] = "pk_assetsprop";
					Map<String, List<StockBalanceVO>> tradedatemap = PubMethod
							.getInstance().hashlizeObjects(list, hashkey);
					// 按库存组织分组
					hashkey[0] = "pk_stocksort";
					List<StockBalanceVO> assetsproplist = null;// 某金融资产属性所有库存
					Iterator<Entry<String, List<StockBalanceVO>>> iter = tradedatemap
							.entrySet().iterator();
					Entry<String, List<StockBalanceVO>> entry;
					String key;
					Map<String, List<StockBalanceVO>> tradedatemap2 = null;// 某金融资产属性库存按库存组织分组
					Iterator<Entry<String, List<StockBalanceVO>>> iter2;
					Entry<String, List<StockBalanceVO>> entry2;
					ConcurrentHashMap<String, StockBalanceVO> tradevomap = null;
					String key2;
					while (iter.hasNext()) {// 遍历金融资产属性
						entry = iter.next();
						key = entry.getKey();
						stockBalCacheMap
								.get(trade_date)
								.put(key,
										new ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>());
						assetsproplist = tradedatemap.get(key);
						tradedatemap2 = PubMethod.getInstance()
								.hashlizeObjects(assetsproplist, hashkey);
						// keyset2 = tradedatemap2.keySet();
						iter2 = tradedatemap2.entrySet().iterator();

						while (iter2.hasNext()) {// 遍历库存组织
							entry2 = iter2.next();
							key2 = entry2.getKey();
							tradevomap = (ConcurrentHashMap<String, StockBalanceVO>) PubMethod
									.getInstance()
									.hashlizeObject(
											tradedatemap2.get(key2),
											PubMethod
													.getInstance()
													.getCostArrayWithBilltypeGroup(
															costParaVO
																	.getCostplanvo()
																	.getCostFieldArray()));// 按分组字段分组
							stockBalCacheMap.get(trade_date).get(key)
									.put(key2, tradevomap);
						}
					}
				}
			}
		} else {
			/**
			 * 若当日有缓存直接拿出来 否则回溯 回溯上一数据日缓存 YangJie 2014-04-04
			 */
			if (stockBalCacheMap.get(trade_date) == null
					|| stockBalCacheMap.get(trade_date).size() == 0) {
				UFDate beforetradedate = new UFDate(trade_date)
						.getDateBefore(1);
				UFDate lastdate = costingtool.getCurrbilltypegroupvo()
						.getLastapprovedate() == null ? new UFDate(0L)
						: costingtool.getCurrbilltypegroupvo()
								.getLastapprovedate();
				while (!(lastdate.compareTo(beforetradedate) > 0)) {
					if (stockBalCacheMap.get(beforetradedate.toLocalString()) != null) {
						ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>> newmap = stockBalCacheMap
								.get(beforetradedate.toLocalString());
						Set<String> keys = newmap.keySet();
						Iterator<String> it = keys.iterator();
						Collection<ConcurrentHashMap<String, StockBalanceVO>> vosmap = null;
						while (it.hasNext()) {
							vosmap = newmap.get(it.next()).values();
							for (Map<String, StockBalanceVO> map : vosmap) {
								Iterator<String> iz = map.keySet().iterator();
								while (iz.hasNext()) {
									String keya = iz.next();
									StockBalanceVO balancevo = map.get(keya);
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
																				.getCostFieldArray()));
										map.put(key11, balancevo);
										map.remove(keya);
									}
									balancevo.setTrade_date(new UFDate(
											trade_date));
								}
							}
						}
						stockBalCacheMap.put(trade_date, newmap);
						break;
					}
					beforetradedate = beforetradedate.getDateBefore(1);
				}
			}
		}
	}

	@Override
	
	public void updateCache(BanlanceQueryKeyVO sbqkVO, SuperVO vo)
			throws BusinessException {

		StockBalanceVO sbvo = (StockBalanceVO) VODeepCloneUtil.deepClone(vo);
		sbvo.setAttributeValue("dr", 0);
		sbvo.setIsnew(true);
		String pk_stocksort = sbqkVO.getPk_stocksort();
		String pk_assetsprop = sbqkVO.getPk_assetsprop();
		String trade_date = sbqkVO.getTrade_date();
		String key = sbqkVO.getKey();
		if (sbqkVO.getPk_stocksort() == null) {
			pk_stocksort = SystemConst.pk_defaultstocksort;
			Logger.debug("getStockbalanceVO时: 库存组织为空,取默认库存组织!");
		}

		if (stockBalCacheMap.get(trade_date).get(pk_assetsprop) == null) {
			ConcurrentHashMap<String, StockBalanceVO> childmap = new ConcurrentHashMap<String, StockBalanceVO>();
			childmap.put(key, sbvo);
			ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>> map = new ConcurrentHashMap<String, ConcurrentHashMap<String, StockBalanceVO>>();
			map.put(pk_stocksort, childmap);
			stockBalCacheMap.get(trade_date).put(pk_assetsprop, map);
		} else if (stockBalCacheMap.get(trade_date).get(pk_assetsprop)
				.get(pk_stocksort) == null) {
			ConcurrentHashMap<String, StockBalanceVO> childmap = new ConcurrentHashMap<String, StockBalanceVO>();
			childmap.put(key, sbvo);
			stockBalCacheMap.get(trade_date).get(pk_assetsprop)
					.put(pk_stocksort, childmap);
		} else {
			stockBalCacheMap.get(trade_date).get(pk_assetsprop)
					.get(pk_stocksort).put(key, sbvo);
		}

	}

	
	@Override
	public synchronized void clearCache() {
		if (stockBalCacheMap != null) {
			stockBalCacheMap.clear();
			stockBalCacheMap = null;
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
		String[] costItemCodes = costParaVO.getCostplanvo().getCostFieldArray();// 管理项目设置的字段
		String[] manItemCodes = PubMethod.getInstance()
				.getCostArrayWithBilltypeGroup(costItemCodes);

		for (int i = 0; i < manItemCodes.length; i++) {
			// if (manItemCodes[i].equals("begin_date") ||
			// manItemCodes[i].equals("end_date")) {
			// continue;
			// }
			manjoinSb.append(" and isnull(x.").append(manItemCodes[i])
					.append(",'").append(manItemCodes[i])
					.append("') = isnull(y.").append(manItemCodes[i])
					.append(",'").append(manItemCodes[i]).append("')");
		}

		for (int i = 0; i < manItemCodes.length; i++) {
			// if (manItemCodes[i].equals("begin_date") ||
			// manItemCodes[i].equals("end_date")) {
			// continue;
			// }
			manItemSb.append("a." + manItemCodes[i]);
			manItemSb.append(",");
		}

		sqlsb.append("select /*+ no_merge(x) leading(x) */ y.* from (select max(a.trade_date) trade_date,");
		sqlsb.append(manItemSb.toString());
		sqlsb.append("a.pk_costplan");
		sqlsb.append(" from sim_stockbalance a inner join ");
		sqlsb.append("sec_securities b on a.pk_securities=b.pk_securities ");
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
		sqlsb.append("inner join sim_stockbalance y on x.trade_date = y.trade_date");
		sqlsb.append(manjoinSb.toString());
		sqlsb.append(" and x.pk_costplan = y.pk_costplan and isnull(y.dr,0)=0 order by y.end_date");
		return sqlsb.toString();

	}
}
