package nc.impl.fba_smt.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.fba_smt.report.IAvailableDetailRpt;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.PubReportDataQuery;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.report.SMTReportUtil;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.pub.SimINFOPubMethod;
import nc.vo.fba_smt.report.AvailableDetailVO;
import nc.vo.fba_smt.report.AvailableDetailViewMeta;
import nc.vo.fba_smt.trade.margintrade.MarginTradeVO;
import nc.vo.fba_smt.trade.refinancing.RefinancingVO;
import nc.vo.fba_smt.trade.transferrecord.TransferRecordVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

public class AvailableDetailRptImpl implements IAvailableDetailRpt {

	/**
	 * 不清楚当年米雪为什么要加这个没有用到的方法，并且此方法导致了依赖错误，故删除之 <br>
	 * 
	 * @author JINGQT 2016年11月23日
	 */
	public AvailableDetailRptImpl() {
		// getQueryConditionDLGInitializer();
	}

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		ConditionVO[] conVos = (ConditionVO[]) context
				.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
		if (qryconutil.isNull() && (conVos == null || conVos.length == 0)) {
			ReportQueryResult result = new ReportQueryResult();
			DataSet dataset = new DataSet();
			dataset.setMetaData(this.genDataSet());
			result.setDataSet(dataset);
			return result;
		}

		ReportQueryResult queryResult = new ReportQueryResult();
		queryResult.setDataSet(getAvailableDetailDataSet(context, conVos));
		return queryResult;

	}

	private DataSet getAvailableDetailDataSet(IContext context,
			ConditionVO[] conVos) throws BusinessException {
		String dateValue = SimReportUtils.getDateCon(conVos);// 取得查询条件中的日期条件
		String org = SimReportUtils.getOrg(conVos);
		String glBook = SimReportUtils.getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);
		boolean isrzrq = false;
		try {
			isrzrq = SysInitCache.getInstance()
					.getIsRzrq(de_group, org, glBook);
		} catch (Exception e1) {
			Logger.error(e1.getMessage());
		}
		if (!isrzrq) {
			throw new BusinessException("当前业务单元不是融资融券部门！");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("pk_org", org);
		map.put("pk_group", de_group);
		map.put("pk_glorgbook", glBook);

		// UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
		// .getDateBefore(ReportConst.DAY);//开始日期为什么要取前一天？
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue));
		String endDate = SimReportUtils.getEndDate(dateValue);
		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);

		PubReportDataQuery query = new PubReportDataQuery();
		SmtReportQueryUtil querySmt = new SmtReportQueryUtil();
		List<StockBalanceVO> firBal = null;
		// 增加查询库存条件(账簿 组织 库存)
		String condition = SMTReportUtil.getStockBalaCondition(
				ReportConst.FUND_B_MARK, conVos, ReportConst.STOCKKR);
		try {
			// 查询库存期初
			firBal = query.getStockBalance(condition, startDate.toString(),
					costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("期初查询出错！");
		}

		String key = null;
		// 迭代期初，如果发生中存在，则直接赋值，如果不存在，则新增
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
		AvailableDetailVO drv = null;
		StockBalanceVO sbv = null;
		// String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		// update by lihbj 增加pk_stocksort维度
		String[] manFieldV = new String[] { "pk_stocksort", "pk_assetsprop",
				"pk_group", "pk_org", "pk_glorgbook", "pk_capaccount",
				"begin_date", "end_date", "pk_securities" };
		Map<String, AvailableDetailVO> trademap = new HashMap<String, AvailableDetailVO>();
		PubMethod pm = PubMethod.getInstance();
		for (int i = 0; i < firSize; i++) {
			sbv = firBal.get(i);
			if (zero.equals(sbv.getStocks_num())
					&& zero.equals(sbv.getStocks_sum())) {
				continue;
			}
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			if (trademap.containsKey(key)) {
				drv = trademap.get(key);
			} else {
				drv = new AvailableDetailVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drv.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drv);
			}

			drv.setFirstnum(pm.add(drv.getFirstnum(), sbv.getStocks_num()));
			drv.setFirstsum(pm.add(drv.getFirstsum(), sbv.getStocks_sum()));
			drv.setTrade_date(startDate);// 由于自由报表还会通过时间过滤，这里写死时间
		}
		//Map<String, UFDouble> closeMap = query.getClosePrice(endDate);
		// 可供出售证券转入(可供转可融) 0 +券源划转
		List<SuperVO> kgzkrvos = null;
		List<SuperVO> qyhzhrvos = null;
		try {
			kgzkrvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.KGZKR);// 查询可供转可融数据
			qyhzhrvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO, "QYHZHR");// 查询券源划转划入的数据
			kgzkrvos.addAll(qyhzhrvos);
			trademap = assignTransferData(trademap, kgzkrvos, manFieldV, 0,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询可供出售证券转入数据出错！");
		}
		// 融入证券(转融通借入) 1
		List<SuperVO> zrtjrvos = null;
		try {
			zrtjrvos = querySmt.getRefinancingData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.ZRTJR);
			trademap = assignTransferData(trademap, zrtjrvos, manFieldV, 1,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询转融通借入数据出错！");
		}
		// 客户归还证券(融出归还) 2
		List<SuperVO> rcghvos = null;
		try {
			rcghvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCGHJL);
			rcghvos = setKCZZ(rcghvos, "0002");
			trademap = assignTransferData(trademap, rcghvos, manFieldV, 2,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询客户归还证券出错！");
		}
		// 其他增加(红股入账 红股券源划转) 3 + 逾期归还可融记录
		List<SuperVO> hgvos = null;// 红股券源划转
		List<SuperVO> hgkgzkr = null;// 红股可供转可融
		List<SuperVO> yqghkr = null;// 逾期归还可融记录
		try {
			hgvos = querySmt.getTransferData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGQYHZ);// 查询红股券源划转
			hgkgzkr = querySmt.getTransferData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGKGZKR);// 查询红股可供转可融
			yqghkr = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.YQGHKR);// 逾期归还可融记录 flag ==
																// 8
			yqghkr = setKCZZ(yqghkr, "0002");
			hgvos.addAll(hgkgzkr);
			trademap = assignTransferData(trademap, yqghkr, manFieldV, 8,
					startDate);// 逾期归还可融记录
			trademap = assignTransferData(trademap, hgvos, manFieldV, 3,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询其他增加出错！");
		}
		// 向客户融出证券(融出证券) 4
		List<SuperVO> rcvos = null;
		try {
			rcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCJL);
			rcvos = setKCZZ(rcvos, "0002");
			trademap = assignTransferData(trademap, rcvos, manFieldV, 4,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询向客户融出证券出错！");
		}
		// 转回可供出售证券 (可融转可供) 5
		List<SuperVO> krzkgvos = null;
		List<SuperVO> qyhzhcvos = null;
		try {
			krzkgvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.KRZKG);// 查询可融转可供
			qyhzhcvos = querySmt.getTransferData(condition,
					startDate.toString(), endDate, costPlanVO, "QYHZHC");// 查询券源划转从可融库划出单
			krzkgvos.addAll(qyhzhcvos);
			trademap = assignTransferData(trademap, krzkgvos, manFieldV, 5,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询转回可供出售证券出错！");
		}
		// 归还融入证券(转融通归还)6
		List<SuperVO> zrtghvos = null;
		try {
			zrtghvos = querySmt.getRefinancingData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.ZRTGH);// 查询转融通归还
			trademap = assignTransferData(trademap, zrtghvos, manFieldV, 6,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询转融通归还出错！");
		}
		// 其他减少(红股融出) 7
		List<SuperVO> hgrcvos = null;
		try {
			hgrcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGRCJL);
			hgrcvos = setKCZZ(hgrcvos, "0002");
			trademap = assignTransferData(trademap, hgrcvos, manFieldV, 7,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询其他减少数据出错！");
		}

		/******* add by lihbj 华西证券 将已融数据加入 一起按库存组织维度体现 start *******/

		/******* add by lihbj 华西证券 将已融数据加入 一起按库存组织维度体现 end *******/

		for (AvailableDetailVO vo : trademap.values()) {
			// 增加数量 = 期初+本期增加
			UFDouble addnum = pm.add(vo.getFirstnum(),// 期初
					pm.add(vo.getKgzkrnum(),// 可供转可融
							pm.add(vo.getZrtjrnum(),// 转融通借入
									pm.add(vo.getRcghnum(),// 融出归还
											vo.getHgqyhznum()))));// 红股券源划转
			UFDouble addsum = pm.add(vo.getFirstsum(),// 期初
					pm.add(vo.getKgzkrsum(),// 可供转可融
							pm.add(vo.getZrtjrsum(),// 转融通借入
									pm.add(vo.getRcghsum(),// 融出归还
											vo.getHgqyhzsum()))));// 红股券源划转
			// 减少数量
			UFDouble subnum = pm.add(
					vo.getRczqnum(),
					pm.add(vo.getKrzkgnum(),
							pm.add(vo.getZrtghnum(), vo.getHgrcnum())));
			UFDouble subsum = pm.add(
					vo.getRczqsum(),
					pm.add(vo.getKrzkgsum(),
							pm.add(vo.getZrtghsum(), vo.getHgrcsum())));
			// 期末结存= 增加数量 - 减少数量
			UFDouble endnum = pm.add(vo.getEndnum(), addnum).sub(subnum);
			UFDouble endsum = pm.add(vo.getEndsum(), addsum).sub(subsum);
			vo.setEndnum(endnum);
			vo.setEndsum(endsum);
			// 收盘价
			// UFDouble closeprice = closeMap.get(vo.getPk_securities());
			UFDouble closeprice = UFDouble.ZERO_DBL;
			try {
				closeprice = SimINFOPubMethod.getInstance().getClosePrice(
						endDate.substring(0, 10), vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			vo.setCloseprice(closeprice);
			// 证券市值 = 收盘价*期末结存数量
			UFDouble marketvalue = pm.multiply(closeprice, endnum);
			vo.setMarketValue(marketvalue);
			// 潜盈(亏) = 证券市值-期末结存金额
			vo.setProfit(pm.sub(marketvalue, endsum));
			/**
			 * 算期初市值
			 */
			UFDouble firstprice = UFDouble.ZERO_DBL;
			try {
				firstprice = SimINFOPubMethod.getInstance().getClosePrice(
						startDate.toString().substring(0, 10),
						vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 证券期初市值 = 收盘价*期初数量
			UFDouble firstnum = vo.getFirstnum() == null ? UFDouble.ZERO_DBL
					: vo.getFirstnum();
			UFDouble firstMarket = pm.multiply(firstprice, firstnum);
			vo.setFirstMarket(firstMarket);
		}
		Collection<AvailableDetailVO> c = trademap.values();
		// 取已融数据
		Collection<AvailableDetailVO> yr_c = getStockOutDetailDataSet(context,
				conVos);
		List<AvailableDetailVO> kr_list = new ArrayList<AvailableDetailVO>(c);
		List<AvailableDetailVO> yr_list = new ArrayList<AvailableDetailVO>(yr_c);
		kr_list.addAll(yr_list);
		// c.addAll(yr_c);
		return PubMethod.getInstance().convertVOToArray(genDataSet(), kr_list);
	}

	/**
	 * 取已融数据
	 * 
	 * @author lihbj
	 * @param context
	 * @param conVos
	 * @return
	 * @throws BusinessException
	 */
	private Collection<AvailableDetailVO> getStockOutDetailDataSet(
			IContext context, ConditionVO[] conVos) throws BusinessException {
		String dateValue = SimReportUtils.getDateCon(conVos);// 取得查询条件中的日期条件
		String org = SimReportUtils.getOrg(conVos);
		String glBook = SimReportUtils.getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);
		boolean isrzrq = false;
		try {
			isrzrq = SysInitCache.getInstance()
					.getIsRzrq(de_group, org, glBook);
		} catch (Exception e1) {
			Logger.error(e1.getMessage());
		}
		if (!isrzrq) {
			throw new BusinessException("当前业务单元不是融资融券部门！");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("pk_org", org);
		map.put("pk_group", de_group);
		map.put("pk_glorgbook", glBook);

		// UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
		// .getDateBefore(ReportConst.DAY);
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue));
		String endDate = SimReportUtils.getEndDate(dateValue);
		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);

		PubReportDataQuery query = new PubReportDataQuery();
		SmtReportQueryUtil querySmt = new SmtReportQueryUtil();
		List<StockBalanceVO> firBal = null;
		// 增加查询库存条件(账簿 组织 库存)
		String condition = SMTReportUtil.getStockBalaCondition(
				ReportConst.FUND_B_MARK, conVos, ReportConst.STOCKYR);// 查询已融库存
		try {
			// 查询库存期初
			firBal = query.getStockBalance(condition, startDate.toString(),
					costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("期初查询出错！");
		}

		String key = null;
		// 迭代期初，如果发生中存在，则直接赋值，如果不存在，则新增
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
		AvailableDetailVO drv = null;
		StockBalanceVO sbv = null;
		// String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		String[] manFieldV = new String[] { "pk_stocksort", "pk_assetsprop",
				"pk_group", "pk_org", "pk_glorgbook", "pk_capaccount",
				"begin_date", "end_date", "pk_securities" };
		Map<String, AvailableDetailVO> trademap = new HashMap<String, AvailableDetailVO>();
		PubMethod pm = PubMethod.getInstance();
		for (int i = 0; i < firSize; i++) {
			sbv = firBal.get(i);
			if (zero.equals(sbv.getStocks_num())
					&& zero.equals(sbv.getStocks_sum())) {
				continue;
			}
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			if (trademap.containsKey(key)) {
				drv = trademap.get(key);
			} else {
				drv = new AvailableDetailVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drv.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drv);
			}

			drv.setFirstnum(pm.add(drv.getFirstnum(), sbv.getStocks_num()));
			drv.setFirstsum(pm.add(drv.getFirstsum(), sbv.getStocks_sum()));
			drv.setTrade_date(startDate);// 由于自由报表还会通过时间过滤，这里写死时间
		}
		// Map<String, UFDouble> closeMap = query.getClosePrice(endDate);
		// 向客户融出证券(融出证券)1
		List<SuperVO> yr_rcvos = null;
		try {
			yr_rcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCJL);
			yr_rcvos = setKCZZ(yr_rcvos, "0003");
			trademap = yr_assignTransferData(trademap, yr_rcvos, manFieldV, 1,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询向客户融出证券出错！");
		}
		// 其他减少(红股融出) 2
		List<SuperVO> yr_hgrcvos = null;
		try {
			yr_hgrcvos = querySmt.getMarginData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.HGRCJL);
			yr_hgrcvos = setKCZZ(yr_hgrcvos, "0003");
			trademap = yr_assignTransferData(trademap, yr_hgrcvos, manFieldV,
					2, startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询已融库存其他增加数据出错！");
		}
		// 客户归还证券(融出归还) 3
		List<SuperVO> yr_rcghvos = null;
		try {
			yr_rcghvos = querySmt.getMarginData(condition,
					startDate.toString(), endDate, costPlanVO,
					ReportConst.RCGHJL);
			yr_rcghvos = setKCZZ(yr_rcghvos, "0003");
			trademap = yr_assignTransferData(trademap, yr_rcghvos, manFieldV,
					3, startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询客户归还证券出错！");
		}
		// 转为逾期证券(记录) 4
		List<SuperVO> yqzqvos = null;
		try {
			yqzqvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.YQJL);
			yqzqvos = setKCZZ(yqzqvos, "0003");
			trademap = yr_assignTransferData(trademap, yqzqvos, manFieldV, 4,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询转为逾期记录出错！");
		}

		for (AvailableDetailVO vo : trademap.values()) {
			// 增加数量 = 期初+本期增加
			UFDouble addnum = pm.add(vo.getFirstnum(),// 期初
					pm.add(vo.getRczqnum(),// 融出证券
							vo.getHgrcnum()));// 红股融出
			UFDouble addsum = pm.add(vo.getFirstsum(),// 期初
					pm.add(vo.getRczqsum(),// 融出证券
							vo.getHgrcsum()));// 红股融出
			// 减少数量
			// UFDouble subnum = pm.add(vo.getRcghnum(), vo.getYqjlnum());//
			// 融出归还+逾期记录
			// UFDouble subsum = pm.add(vo.getRcghsum(), vo.getYqjlsum());//
			// 融出归还+逾期记录
			// 华西没有逾期业务 update by lihbj
			UFDouble subnum = pm.add(vo.getRcghnum(), UFDouble.ZERO_DBL);// 融出归还
			UFDouble subsum = pm.add(vo.getRcghsum(), UFDouble.ZERO_DBL);// 融出归还
			// 期末结存= 增加数量 - 减少数量
			UFDouble endnum = pm.add(vo.getEndnum(), addnum).sub(subnum);
			UFDouble endsum = pm.add(vo.getEndsum(), addsum).sub(subsum);
			vo.setEndnum(endnum);
			vo.setEndsum(endsum);
			// 收盘价
			// UFDouble closeprice = closeMap.get(vo.getPk_securities());
			UFDouble closeprice = UFDouble.ZERO_DBL;
			try {
				closeprice = SimINFOPubMethod.getInstance().getClosePrice(
						endDate.substring(0, 10), vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			vo.setCloseprice(closeprice);
			// 证券市值 = 收盘价*期末结存数量
			UFDouble marketvalue = pm.multiply(closeprice, endnum);
			vo.setMarketValue(marketvalue);
			// 潜盈(亏) = 证券市值-期末结存金额
			vo.setProfit(pm.sub(marketvalue, endsum));
			/**
			 * 算期初市值
			 */
			UFDouble firstprice = UFDouble.ZERO_DBL;
			try {
				firstprice = SimINFOPubMethod.getInstance().getClosePrice(
						startDate.toString().substring(0, 10),
						vo.getPk_securities());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 证券期初市值 = 收盘价*期初数量
			UFDouble firstnum = vo.getFirstnum() == null ? UFDouble.ZERO_DBL
					: vo.getFirstnum();
			UFDouble firstMarket = pm.multiply(firstprice, firstnum);
			vo.setFirstMarket(firstMarket);
		}
		Collection<AvailableDetailVO> c = trademap.values();
		return c;
	}

	private MetaData genDataSet() {
		List<Field> list = new ArrayList<Field>();

		for (String key : AvailableDetailViewMeta.FUNDTRADE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		for (String key : AvailableDetailViewMeta.EXT_D_FIELD) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}

		for (String key : AvailableDetailViewMeta.EXT_DATE_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		Field[] fields = new Field[list.size()];
		list.toArray(fields);
		return new MetaData(fields);
	}

	/**
	 * 
	 * @param trademap
	 *            保存所有VO的map
	 * @param mtvos
	 *            可供转可融(flag=0) 可融转可供的List
	 * @param manFieldV
	 * @param closeMap
	 * @return
	 */
	private Map<String, AvailableDetailVO> assignTransferData(
			Map<String, AvailableDetailVO> trademap, List<SuperVO> mtvos,
			String[] manFieldV, int flag, UFDate startDate) {
		SuperVO mvto = null;
		AvailableDetailVO drv = null;
		PubMethod pm = PubMethod.getInstance();
		// 这里去掉库存组织条件 因为转融通借入可融库 归还证金库 会分两条数据显示出来
		manFieldV = new String[] { "pk_stocksort", "pk_assetsprop", "pk_group",
				"pk_org", "pk_glorgbook", "pk_capaccount", "begin_date",
				"end_date", "pk_securities" };
		if (mtvos != null && mtvos.size() > 0) {
			for (int i = 0; i < mtvos.size(); i++) {
				if (0 == flag || 3 == flag) {// 可供转可融
												// 红股券源划转 or 红股可供转可融
					mvto = (TransferRecordVO) mtvos.get(i);
					manFieldV = new String[] { "pk_stocksort",
							"hr_pk_assetsprop", "pk_group", "pk_org",
							"pk_glorgbook", "hr_pk_capaccount", "begin_date",
							"end_date", "pk_securities" };
				} else if (2 == flag || 4 == flag || 7 == flag || 8 == flag) {// 融出记录
																				// or
					// 融出归还记录 or
					// 红股融出记录
					mvto = (MarginTradeVO) mtvos.get(i);
				} else if (1 == flag || 6 == flag) {// 转融通借入 or 转融通归还
					mvto = (RefinancingVO) mtvos.get(i);

				} else if (5 == flag) {// 可融转可供
					mvto = (TransferRecordVO) mtvos.get(i);
					manFieldV = new String[] { "pk_stocksort",
							"hc_pk_assetsprop", "pk_group", "pk_org",
							"pk_glorgbook", "hc_pk_capaccount", "begin_date",
							"end_date", "pk_securities" };
				}
				String key = VOUtil.getCombinesKey(mvto, manFieldV);
				if (trademap.containsKey(key)) {// 已经存在
					drv = trademap.get(key);
				}/*
				 * else if (flag == 0 || flag == 3 || flag == 5) {
				 * 
				 * key = VOUtil.getCombinesKey(mvto, manFieldV); if
				 * (trademap.containsKey(key)) {// 已经存在 drv = trademap.get(key);
				 * } }
				 */else {
					drv = new AvailableDetailVO();

					for (int j = 0; j < manFieldV.length; j++) {
						drv.setAttributeValue(manFieldV[j],
								mvto.getAttributeValue(manFieldV[j]));

					}
					if (0 == flag || 3 == flag) {// 可供转可融 or 红股可供转可融资金账号为空
						String pk_capaccount = (String) mvto
								.getAttributeValue("hr_pk_capaccount");
						String pk_assetsprop = (String) mvto
								.getAttributeValue("hr_pk_assetsprop");
						drv.setAttributeValue("pk_capaccount", pk_capaccount);
						drv.setAttributeValue("pk_assetsprop", pk_assetsprop);
					}
					if (5 == flag) {// 可供转可融 or 红股可供转可融资金账号为空
						String pk_capaccount = (String) mvto
								.getAttributeValue("hc_pk_capaccount");
						String pk_assetsprop = (String) mvto
								.getAttributeValue("hc_pk_assetsprop");
						drv.setAttributeValue("pk_capaccount", pk_capaccount);
						drv.setAttributeValue("pk_assetsprop", pk_assetsprop);
					}

					trademap.put(key, drv);
				}
				if (0 == flag) {// 可供转可融
					drv.setKgzkrnum(pm.add(drv.getKgzkrnum(),
							((TransferRecordVO) mvto).getBargain_num()));
					drv.setKgzkrsum(pm.add(drv.getKgzkrsum(),
							((TransferRecordVO) mvto).getBargain_sum()));
				} else if (1 == flag) {// 转融通借入
					drv.setZrtjrnum(pm.add(drv.getZrtjrnum(),
							((RefinancingVO) mvto).getBargain_num()));
					drv.setZrtjrsum(pm.add(drv.getZrtjrsum(),
							((RefinancingVO) mvto).getBargain_sum()));
				} else if (2 == flag) {// 融出归还
					drv.setRcghnum(pm.add(drv.getRcghnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRcghsum(pm.add(drv.getRcghsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (3 == flag) {// 其他增加(红股券源划转+红股可供转可融)
					drv.setHgqyhznum(pm.add(drv.getHgqyhznum(),
							((TransferRecordVO) mvto).getBargain_num()));
				} else if (4 == flag) {// 融出
					drv.setRczqnum(pm.add(drv.getRczqnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRczqsum(pm.add(drv.getRczqsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (5 == flag) {// 可融转可供
					drv.setKrzkgnum(pm.add(drv.getKrzkgnum(),
							((TransferRecordVO) mvto).getBargain_num()));
					drv.setKrzkgsum(pm.add(drv.getKrzkgsum(),
							((TransferRecordVO) mvto).getBargain_sum()));
				} else if (6 == flag) {// 转融通归还
					drv.setZrtghnum(pm.add(drv.getZrtghnum(),
							((RefinancingVO) mvto).getBargain_num()));
					drv.setZrtghsum(pm.add(drv.getZrtghsum(),
							((RefinancingVO) mvto).getBargain_sum()));
				} else if (7 == flag) {// 其他减少(红股融出)
					drv.setHgrcnum(pm.add(drv.getHgrcnum(),
							((MarginTradeVO) mvto).getBargain_num()));

				} else if (8 == flag) {// 逾期归还可融记录 （其他增加）
					drv.setHgqyhznum(pm.add(drv.getHgqyhznum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setHgqyhzsum(pm.add(drv.getHgqyhzsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				}

				drv.setTrade_date(startDate);// 由于自由报表还会通过时间过滤，这里写死时间
			}
		}
		return trademap;
	}

	/**
	 * 获得查询模板初化化接口,可复写
	 * 
	 * @return
	 */
	// protected IQueryConditionDLGInitializer getQueryConditionDLGInitializer()
	// {
	// SmtQueryConditionDLGInitializer dlgInit = new
	// SmtQueryConditionDLGInitializer();
	// return dlgInit;
	// }

	/**
	 * 
	 * @param trademap
	 *            保存所有VO的map
	 * @param mtvos
	 *            可供转可融(flag=0) 可融转可供的List
	 * @param manFieldV
	 * @param closeMap
	 * @return
	 */
	private Map<String, AvailableDetailVO> yr_assignTransferData(
			Map<String, AvailableDetailVO> trademap, List<SuperVO> mtvos,
			String[] manFieldV, int flag, UFDate startDate) {
		SuperVO mvto = null;
		AvailableDetailVO drv = null;
		PubMethod pm = PubMethod.getInstance();
		// 这里去掉库存组织条件 因为转融通借入可融库 归还证金库 会分两条数据显示出来
		manFieldV = new String[] { "pk_stocksort", "pk_assetsprop", "pk_group",
				"pk_org", "pk_glorgbook", "pk_capaccount", "begin_date",
				"end_date", "pk_securities" };
		if (mtvos != null && mtvos.size() > 0) {
			for (int i = 0; i < mtvos.size(); i++) {
				mvto = (MarginTradeVO) mtvos.get(i);
				String key = VOUtil.getCombinesKey(mvto, manFieldV);
				if (trademap.containsKey(key)) {// 已经存在
					drv = trademap.get(key);
				} else {
					drv = new AvailableDetailVO();
					for (int j = 0; j < manFieldV.length; j++) {
						drv.setAttributeValue(manFieldV[j],
								mvto.getAttributeValue(manFieldV[j]));
					}
					trademap.put(key, drv);
				}
				if (1 == flag) {// 融出记录
					drv.setRczqnum(pm.add(drv.getRczqnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRczqsum(pm.add(drv.getRczqsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (2 == flag) {// 其他增加(红股融出)
					drv.setHgrcnum(pm.add(drv.getHgrcnum(),
							((MarginTradeVO) mvto).getBargain_num()));
				} else if (3 == flag) {// 融出归还
					drv.setRcghnum(pm.add(drv.getRcghnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRcghsum(pm.add(drv.getRcghsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (4 == flag) {// 逾期记录--华西没有该业务
					// drv.setYqjlnum(pm.add(drv.getYqjlnum(), ((MarginTradeVO)
					// mvto).getBargain_num()));
					// drv.setYqjlsum(pm.add(drv.getYqjlsum(), ((MarginTradeVO)
					// mvto).getBargain_sum()));
				}
				drv.setTrade_date(startDate);// 由于自由报表还会通过时间过滤，这里写死时间
			}
		}
		return trademap;
	}

	/**
	 * 设置库存组织
	 * 
	 * @param svos
	 * @param code
	 *            0002--可融 0003--已融
	 * @return
	 */
	private List<SuperVO> setKCZZ(List<SuperVO> svos, String code) {
		if (svos != null && svos.size() > 0) {
			for (SuperVO svo : svos) {
				if (code.equals("0002")) {// 可融
					((MarginTradeVO) svo)
							.setPk_stocksort("0001SE00000000000002");
				} else if (code.equals("0003")) {// 已融
					((MarginTradeVO) svo)
							.setPk_stocksort("0001SE00000000000003");
				}
			}
		}
		return svos;
	}
}
