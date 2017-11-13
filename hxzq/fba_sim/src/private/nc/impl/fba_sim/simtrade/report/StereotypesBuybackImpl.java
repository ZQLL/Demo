package nc.impl.fba_sim.simtrade.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

import nc.bs.dao.BaseDAO;
import nc.bs.fba_sim.simtrade.report.FundInOutUtils;
import nc.bs.fba_sim.simtrade.report.ReportSqlUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_sim.simtrade.report.IStereotypesBuyback;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.PubReportDataQuery;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_sabb.trade.ydsgh.AgreedbbVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.trademarket.TradeMarketVO;
import nc.vo.fba_sim.pub.SimINFOPubMethod;
import nc.vo.fba_sim.simbs.checkplan.CheckplanVO;
import nc.vo.fba_sim.simtrade.report.SecInvestDailyRepVO;
import nc.vo.fba_sim.simtrade.report.SecInvestDailyViewMeta;
import nc.vo.pub.BusinessException;
import nc.vo.pub.freereport.ReportDataVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

/**
 * 约定式回购报表实现
 * 
 * @author lihaibo
 * 
 */
public class StereotypesBuybackImpl implements IStereotypesBuyback {

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		ConditionVO[] conVos = (ConditionVO[]) context
				.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
		if (qryconutil.isNull() && (conVos == null || conVos.length == 0)) {
			ReportQueryResult result = new ReportQueryResult();
			DataSet dataset = new DataSet();
			dataset.setMetaData(this.getMetaData());
			result.setDataSet(dataset);
			return result;
		}

		ReportQueryResult queryResult = new ReportQueryResult();
		queryResult.setDataSet(genSecInvestDataSet(context, conVos));
		return queryResult;
	}

	/**
	 * 查询期初期末和明细数据组装DataSet
	 * 
	 * @param context
	 * @param conVos
	 * @return
	 * @throws BusinessException
	 */
	private DataSet genSecInvestDataSet(IContext context, ConditionVO[] conVos)
			throws BusinessException {

		String dateValue = FundInOutUtils.getInstance().getDateCon(conVos);
		String org = FundInOutUtils.getInstance().getOrg(conVos);
		String glBook = FundInOutUtils.getInstance().getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);

		// update by lihaibo 解决期初值不能查最后一天问题
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY).asLocalEnd();
		String endDate = SimReportUtils.getEndDate(dateValue);

		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);
		List<CheckplanVO> checkPlanVOs = ReportSqlUtil.getInstance()
				.findCheckPlanVo(org, de_group, glBook);

		/* 查明细并且按维度分组求和 */
		String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		List<ReportDataVO> reportDatas = this.queryData(checkPlanVOs, conVos,
				costPlanVO);
		Map<String, SecInvestDailyRepVO> trademap = mergerData(reportDatas,
				manFieldV, conVos);

		List<StockBalanceVO> firBal = null;
		List<StockBalanceVO> endBal = null;
		PubReportDataQuery query = new PubReportDataQuery();
		try {
			// 查询库存期初
			firBal = getFir(conVos);
			// 查期末
			endBal = getFir(conVos);
		} catch (BusinessException e) {
			throw new BusinessException("期初查询出错！");
		}

		SecInvestDailyRepVO drv = null;
		String key = null;

		// 迭代期初，如果发生中存在，则直接赋值，如果不存在，则新增
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
		for (int i = 0; i < firSize; i++) {
			StockBalanceVO sbv = new StockBalanceVO();
			sbv = firBal.get(i);
			if (zero.equals(sbv.getStocks_num())
					&& zero.equals(sbv.getStocks_sum())) {
				continue;
			}
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			if (trademap.containsKey(key)) {
				drv = trademap.get(key);
			} else {
				drv = new SecInvestDailyRepVO();
				for (int j = 0; j < manFieldV.length; j++) {
					if (manFieldV[j].equals("begin_date")
							|| manFieldV[j].equals("end_date")) {
						if (sbv.getAttributeValue(manFieldV[j]) != null)
							drv.setAttributeValue(
									manFieldV[j],
									new UFDate(sbv.getAttributeValue(
											manFieldV[j]).toString()));
					} else {
						drv.setAttributeValue(manFieldV[j],
								sbv.getAttributeValue(manFieldV[j]));
					}
				}
				// drv.setTrade_date(sbv.getTrade_date());
				drv.setTrade_date(new UFDate(endDate));
			}
			drv.setTrade_date(new UFDate(endDate));
			drv.setFirstNum(sbv.getStocks_num());
			drv.setFirstSum(sbv.getStocks_sum());
			drv.setBegin_date(sbv.getBegin_date() == null ? null : new UFDate(
					sbv.getBegin_date().getMillis()));
			drv.setEnd_date(sbv.getEnd_date() == null ? null : new UFDate(sbv
					.getEnd_date().getMillis()));
			trademap.put(key, drv);
		}
		//
		// // 迭代期末，如果发生中存在，则直接赋值，如果不存在，则新增
		// int endSize = endBal.size();
		// for (int i = 0; i < endSize; i++) {
		// sbv = endBal.get(i);
		// if (zero.equals(sbv.getStocks_num())
		// && zero.equals(sbv.getStocks_sum())) {
		// continue;
		// }
		// key = VOUtil.getCombinesKey(sbv, manFieldV);
		// if (trademap.containsKey(key)) {
		// drv = trademap.get(key);
		//
		// } else {
		// drv = new SecInvestDailyRepVO();
		// for (int j = 0; j < manFieldV.length; j++) {
		// if (manFieldV[j].equals("begin_date")
		// || manFieldV[j].equals("end_date")) {
		// if (sbv.getAttributeValue(manFieldV[j]) != null)
		// drv.setAttributeValue(
		// manFieldV[j],
		// new UFDate(sbv.getAttributeValue(
		// manFieldV[j]).toString()));
		// } else {
		// drv.setAttributeValue(manFieldV[j],
		// sbv.getAttributeValue(manFieldV[j]));
		// }
		// }
		// // drv.setTrade_date(sbv.getTrade_date());
		// drv.setTrade_date(new UFDate(endDate));
		//
		// }
		// drv.setEndNum(sbv.getStocks_num());
		// drv.setEndSum(sbv.getStocks_sum());
		// drv.setBegin_date(sbv.getBegin_date() == null ? null : new UFDate(
		// sbv.getBegin_date().getMillis()));
		// drv.setEnd_date(sbv.getEnd_date() == null ? null : new UFDate(sbv
		// .getEnd_date().getMillis()));
		// trademap.put(key, drv);
		// }

		Collection<SecInvestDailyRepVO> c = trademap.values();
		Iterator<SecInvestDailyRepVO> iter = c.iterator();
		List<SecInvestDailyRepVO> resultVOs = new ArrayList<SecInvestDailyRepVO>();
		PubMethod pm = PubMethod.getInstance();

		/**
		 * 获取公允价值 YangJie2014-07-10
		 */
		Map map = query.getFairValue(endDate.substring(0, 10) + " 00:00:00",
				costPlanVO, false);
		while (iter.hasNext()) {
			SecInvestDailyRepVO repVo = iter.next();
			repVo.setFirstPrice(PubMethod.getInstance().divCal(
					repVo.getFirstSum(), repVo.getFirstNum()));
			repVo.setBuyPrice(PubMethod.getInstance().divCal(repVo.getBuySum(),
					repVo.getBuyNum()));
			repVo.setSellPrice(PubMethod.getInstance().divCal(
					repVo.getSellSum(), repVo.getSellNum()));
			repVo.setEndPrice(PubMethod.getInstance().divCal(repVo.getEndSum(),
					repVo.getEndNum()));
			String pk_securities = repVo.getPk_securities();

			// 算市值，公允价值，浮动盈亏，股本等
			if (repVo.getEndNum() != null && !repVo.getEndNum().equals(zero)) {
				try {
					repVo.setMarketPrice(SimINFOPubMethod.getInstance()
							.getClosePrice(endDate.substring(0, 10),
									repVo.getPk_securities()));
					//四舍五入保留两位小数
					UFDouble marketSum = pm.multiply(repVo.getMarketPrice(),
							repVo.getEndNum());
					marketSum = marketSum.setScale(2, UFDouble.ROUND_HALF_UP);
					repVo.setMarketSum(marketSum);
					
					//期初市值
					UFDouble first_marketPrice = SimINFOPubMethod.getInstance()
							.getClosePrice(startDate.toString().substring(0, 10),
									repVo.getPk_securities());
					UFDouble first_marketSum = pm.multiply(first_marketPrice,
							repVo.getFirstNum());
					first_marketSum = first_marketSum.setScale(2, UFDouble.ROUND_HALF_UP);
					repVo.setFirst_marketSum(first_marketSum);

					/**
					 * YangJie 2014-07-10 截止当天计提了 就取当天市值 截止当天不计提 就去最近一次计提 的
					 * 加转入的减转出的
					 */
					key = VOUtil.getCombinesKey(repVo, manFieldV);
					repVo.setGyz((UFDouble) map.get(key));
					repVo.setProfitLoss(pm
							.sub(repVo.getGyz().compareTo(new UFDouble(0)) == 0 ? repVo
									.getMarketSum() : repVo.getGyz(), repVo
									.getEndSum()));// 浮动盈亏
					// 获得期末总股本 妹的 6没有总股本了
					UFDouble captial = SimINFOPubMethod.getInstance()
							.getCaptial(endDate, pk_securities);
					if (captial != null && !zero.equals(captial)
							&& repVo.getEndNum() != null) {
						repVo.setZgbzb(pm.divCal(
								pm.multiply(repVo.getEndNum(), 100), captial));// 总股本占比
					}
				} catch (Exception e) {
					Logger.error(e);
				}

			}

			// 本期收益
			repVo.setLocalwin(pm.add(repVo.getLocalwin(), repVo.getSellSum()));
			/** JINGQT 2015年7月16日 本期收益中添加红股入账的金额 ADD START */
			repVo.setLocalwin(pm.add(repVo.getLocalwin(), repVo.getBonus()));
			/** JINGQT 2015年7月16日 本期收益中添加红股入账的金额 ADD END */
			repVo.setLocalwin(pm.sub(repVo.getLocalwin(), repVo.getSellCost()));

			/** JINGQT 2015年8月19日 证券投资经营日报的增加公允价值变动列， ADD START */
			// 公允价值变动=‘期末结存期末市值’-‘期末结存金额’；
			repVo.setFairValueChange(pm.sub(repVo.getMarketSum(),
					repVo.getEndSum()));
			/** JINGQT 2015年8月19日 证券投资经营日报的增加公允价值变动列， ADD END */
			resultVOs.add(repVo);
		}

		return PubMethod.getInstance().convertVOToArray(getMetaData(),
				resultVOs);
	}

	/**
	 * 查询数据
	 * 
	 * @param checkPlanVOs
	 * @param conVos
	 * @param costPlanVO
	 * @return
	 * @throws BusinessException
	 */
	private List<ReportDataVO> queryData(List<CheckplanVO> checkPlanVOs,
			ConditionVO[] conVos, CostPlanVO costPlanVO)
			throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		List<ReportDataVO> resultDatas = new ArrayList<ReportDataVO>();

		String condition = SimReportUtils.getQueryCondition(
				ReportConst.FUND_B_MARK, conVos, false, false);
		String querySql = null;
		String dateValue = FundInOutUtils.getInstance().getDateCon(conVos);
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY).asLocalEnd();
		String endDate = SimReportUtils.getEndDate(dateValue);
		ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
		try {
			String glsql = getglsql();

			/**
			 * 查询约定式回购交易记录（除去老系统记录）
			 */
			for (int i = 0; i < 4; i++) {
				if (i == 0) {
					querySql = "select a.vdef9 as pk_selfsgroup,a.pk_org, a.pk_group, a.pk_glorgbook, a.vdef8 as pk_assetsprop, a.vdef7 as pk_stocksort, a.transtypecode as pk_billtype, a.vdef6 as pk_capaccount, a.vdef5 as pk_client, a.vdef4 as pk_operatesite, a.vdef3 as pk_partnaccount, a.pk_securities, a.trade_date, a.vdef2 as begin_date, a.vdef1 as end_date, a.entrust_num as buynum, a.def_interest as buyaccrual,"
							+ "case a.transtypecode when 'HV5A-0xx-01' then a.entrust_sum else a.entrust_sum end  as buysum "
							+ "from sim_agreedbb a "
							+ "inner join sec_securities b "
							+ "on a.pk_securities = b.pk_securities "
							+ "where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-01' ";
					querySql = reportUtil.getQuerySql(condition, querySql);
				} else if (i == 1) {
					querySql = "select a.vdef9 as pk_selfsgroup,a.pk_org, a.pk_group, a.pk_glorgbook, a.vdef8 as pk_assetsprop, a.vdef7 as pk_stocksort, a.transtypecode as pk_billtype, a.vdef6 as pk_capaccount, a.vdef5 as pk_client, a.vdef4 as pk_operatesite, a.vdef3 as pk_partnaccount, a.pk_securities, a.trade_date, a.vdef2 as begin_date, a.vdef1 as end_date, a.entrust_num as sellnum, a.def_interest as buyaccrual,"
							+ "case a.transtypecode when 'HV5A-0xx-01' then a.entrust_sum else a.entrust_sum end  as sellsum "
							+ "from sim_agreedbb a "
							+ "inner join sec_securities b "
							+ "on a.pk_securities = b.pk_securities "
							+ "where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-03' "
							+ glsql;
					querySql = reportUtil.getQuerySql(condition, querySql);
				} else if (i == 2) {
					querySql = "select a.vdef9 as pk_selfsgroup,a.pk_org, a.pk_group, a.pk_glorgbook, a.vdef8 as pk_assetsprop, a.vdef7 as pk_stocksort, a.transtypecode as pk_billtype, a.vdef6 as pk_capaccount, a.vdef5 as pk_client, a.vdef4 as pk_operatesite, a.vdef3 as pk_partnaccount, a.pk_securities, a.trade_date, a.vdef2 as begin_date, a.vdef1 as end_date, a.entrust_num as innum,"
							+ "case a.transtypecode when 'HV5A-0xx-01' then a.entrust_sum else a.entrust_sum end  as insum "
							+ "from sim_agreedbb a "
							+ "inner join sec_securities b "
							+ "on a.pk_securities = b.pk_securities "
							+ "where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-01' and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate
							+ "' and if_accrual='N'" + glsql;
				} else if (i == 3) {
					querySql = "select a.vdef9 as pk_selfsgroup,a.pk_org, a.pk_group, a.pk_glorgbook, a.vdef8 as pk_assetsprop, a.vdef7 as pk_stocksort, a.transtypecode as pk_billtype, a.vdef6 as pk_capaccount, a.vdef5 as pk_client, a.vdef4 as pk_operatesite, a.vdef3 as pk_partnaccount, a.pk_securities, a.trade_date, a.vdef2 as begin_date, a.vdef1 as end_date, 0-a.entrust_num as innum,"
							+ "case a.transtypecode when 'HV5A-0xx-01' then a.entrust_sum else 0-a.entrust_sum end  as insum "
							+ "from sim_agreedbb a "
							+ "inner join sec_securities b "
							+ "on a.pk_securities = b.pk_securities "
							+ "where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-03' and a.pk_org = '"
							+ SimReportUtils.getOrg(conVos)
							+ "' and a.pk_glorgbook = '"
							+ SimReportUtils.getGLBook(conVos)
							+ "' and a.trade_date <= '"
							+ startDate
							+ "' and if_accrual='N'" + glsql;
				}

				List<ReportDataVO> reportVos = (List<ReportDataVO>) queryservice
						.executeQuery(querySql, new BeanListProcessor(
								ReportDataVO.class));
				resultDatas.addAll(reportVos);
			}
		} catch (Exception e) {
			throw new BusinessException("明细查询错误!" + e.getMessage());
		}
		return resultDatas;
	}

	/**
	 * 按维度合并数据
	 * 
	 * @param datavos
	 * @param manFieldV
	 * @return
	 * @throws Exception
	 */
	private Map<String, SecInvestDailyRepVO> mergerData(
			List<ReportDataVO> datavos, String[] manFieldV, ConditionVO[] conVos)
			throws BusinessException {
		PubMethod pm = PubMethod.getInstance();
		Map<String, SecInvestDailyRepVO> map = new HashMap<String, SecInvestDailyRepVO>();
		if (datavos.isEmpty()) {
			return map;
		}
		Map<String, List<ReportDataVO>> result = pm.hashlizeObjects(datavos,
				manFieldV);
		List<ReportDataVO> list = null;
		SecInvestDailyRepVO rprtvo = null;
		ReportDataVO datavo = null;
		Iterator<Entry<String, List<ReportDataVO>>> iter = result.entrySet()
				.iterator();
		Entry<String, List<ReportDataVO>> entry;
		String key;
		while (iter.hasNext()) {
			entry = iter.next();
			key = entry.getKey();
			list = result.get(key);
			if (list.isEmpty()) {
				continue;
			}
			datavo = SimReportUtils.mergerData(list);
			rprtvo = copytoRprtVO(datavo);
			rprtvo.setFirstNum(datavo.getInnum());
			rprtvo.setFirstSum(datavo.getInsum());
			rprtvo.setEndNum((datavo.getInnum().add(datavo.getBuynum()))
					.sub(datavo.getSellnum()));
			rprtvo.setEndSum((datavo.getInsum().add(datavo.getBuysum()))
					.sub(datavo.getSellsum()));
			map.put(key, rprtvo);
		}

		return map;
	}

	/**
	 * ReportDataVO 转换SecInvestDailyRepVO
	 * 
	 * @param datavo
	 * @return
	 * @throws Exception
	 */
	private SecInvestDailyRepVO copytoRprtVO(ReportDataVO datavo) {
		SecInvestDailyRepVO rprtvo = new SecInvestDailyRepVO();
		rprtvo.setPk_org(datavo.getPk_org());
		rprtvo.setPk_group(datavo.getPk_group());
		rprtvo.setPk_glorgbook(datavo.getPk_glorgbook());
		rprtvo.setTrade_date(datavo.getTrade_date());
		if (datavo.getBegin_date() != null)
			rprtvo.setBegin_date(new UFDate(datavo.getBegin_date().getMillis()));
		if (datavo.getEnd_date() != null)
			rprtvo.setEnd_date(new UFDate(datavo.getEnd_date().getMillis()));
		rprtvo.setPk_capaccount(datavo.getPk_capaccount());
		rprtvo.setPk_client(datavo.getPk_client());
		rprtvo.setPk_operatesite(datavo.getPk_operatesite());
		rprtvo.setPk_partnaccount(datavo.getPk_partnaccount());
		rprtvo.setPk_assetsprop(datavo.getPk_assetsprop());
		rprtvo.setPk_stocksort(datavo.getPk_stocksort());
		rprtvo.setPk_securities(datavo.getPk_securities());
		rprtvo.setPk_selfsgroup(datavo.getPk_selfsgroup());
		rprtvo.setSellCost(datavo.getSellcost());
		rprtvo.setBuyNum(datavo.getBuynum());
		rprtvo.setBuySum(datavo.getBuysum());
		rprtvo.setSellNum(datavo.getSellnum());
		rprtvo.setSellSum(datavo.getSellsum());
		/** JINGQT 2015年7月16日 本期收益中添加红股入账 ADD START */
		rprtvo.setBonus(datavo.getBonus());
		/** JINGQT 2015年7月16日 本期收益中添加红股入账 ADD END */
		/**
		 * JINGQT 2015年8月19日 证券投资经营日报的增加公允价值变动列 公允价值变动=‘期末结存期末市值’-‘期末结存金额’； ADD
		 * START
		 */
//		rprtvo.setFairValueChange(datavo.getFairValueChange());
		/**
		 * JINGQT 2015年8月19日 证券投资经营日报的增加公允价值变动列 公允价值变动=‘期末结存期末市值’-‘期末结存金额’； ADD
		 * END
		 */
		rprtvo.setLocalwin(datavo.getFaccrual());// ETF退补款和红利
		return rprtvo;
	}

	private MetaData getMetaData() {
		List<Field> list = new ArrayList<Field>();

		for (String key : SecInvestDailyViewMeta.FUNDTRADE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		for (String key : SecInvestDailyViewMeta.EXT_D_FIELD) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}

		for (String key : SecInvestDailyViewMeta.EXT_DATE_FIELD) {
			Field field = ReportDataUtil.createDateFiled(key);
			list.add(field);
		}

		Field[] fields = new Field[list.size()];
		list.toArray(fields);
		return new MetaData(fields);
	}

	/**
	 * 获取过滤sql 查询'HV5A-0xx-03'类型的交易记录
	 */
	private String getglsql() throws BusinessException {

		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		String querydbbSql = 
				"select a.contract_id, a.pk_aggreedbb\n" +
						"  from sim_agreedbb a\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and a.contract_id not in\n" + 
						"       (select contract_id\n" + 
						"          from (select count(*), contract_id\n" + 
						"                  from sim_agreedbb a\n" + 
						"                 where a.state > 1\n" + 
						"                   and nvl(a.dr, 0) = 0\n" + 
						"                 group by contract_id\n" + 
						"                having count(*) > 1))\n" + 
						"   and a.transtypecode = 'HV5A-0xx-03'";

		String glsql = "";
		try {
			List<AgreedbbVO> greedbbVos = (List<AgreedbbVO>) queryservice
					.executeQuery(querydbbSql, new BeanListProcessor(
							AgreedbbVO.class));
			if (greedbbVos.size() > 0 && greedbbVos != null) {
				List<String> samepks = new ArrayList<String>();
				for (int k = 0; k < greedbbVos.size(); k++) {
					// 主键
					samepks.add(greedbbVos.get(k).getPk_aggreedbb());
				}
				// 组合sql条件
				if (samepks.size() > 0) {
					// 去除重复数据
					samepks = array_unique(samepks);
					glsql += "and a.pk_aggreedbb not in (";
					for (int gl = 0; gl < samepks.size(); gl++) {
						if (gl < samepks.size() - 1) {
							glsql += "'" + samepks.get(gl).toString() + "',";
						} else {
							glsql += "'" + samepks.get(gl).toString() + "')";
						}
					}
				}
			}

		} catch (Exception e) {
			throw new BusinessException("明细查询错误!" + e.getMessage());
		}
		return glsql;
	}

	private List<StockBalanceVO> getFir(ConditionVO[] conVos)
			throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		// 查询库存期初
		List<StockBalanceVO> sbvs = new ArrayList<StockBalanceVO>();

		List<AgreedbbVO> agreedbbs = null;
		String dateValue = FundInOutUtils.getInstance().getDateCon(conVos);
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY).asLocalEnd();
		String endDate = SimReportUtils.getEndDate(dateValue);
		ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
		String glsql = getglsql();
		String condition = SimReportUtils.getQueryCondition(
				ReportConst.FUND_B_MARK, conVos, false, false);
		try {
			/*String querydbbSql = "select a.pk_securities,a.pk_aggreedbb from sim_agreedbb a where a.state>1 and nvl(a.dr, 0) = 0 and a.pk_org = '"
					+ SimReportUtils.getOrg(conVos)
					+ "' and a.pk_glorgbook = '"
					+ SimReportUtils.getGLBook(conVos)
					+ "' and a.trade_date > '" + startDate + "'";
			// querydbbSql = reportUtil.getQuerySql(condition, querydbbSql);
			List<AgreedbbVO> greedbbVos = (List<AgreedbbVO>) queryservice
					.executeQuery(querydbbSql, new BeanListProcessor(
							AgreedbbVO.class));
			if (greedbbVos != null && greedbbVos.size() > 0) {
				// 去除重复的pk_securities
				List<String> pk_securities = vo_unique(greedbbVos);
				pk_securities = array_unique(pk_securities);
				glsql += " and a.pk_securities not in ('~',";
				for (int gl = 0; gl < pk_securities.size(); gl++) {
					if (gl < pk_securities.size() - 1) {
						glsql += "'" + pk_securities.get(gl) + "',";
					} else {
						glsql += "'" + pk_securities.get(gl) + "')";
					}
				}
			}*/
			// 查询除去所选时间段内发生的证券档案的买入总和
			String querySql1 = "select  a.pk_securities, sum(a.entrust_num) as innum, sum(a.entrust_sum) as insum "
					+ " from sim_agreedbb a "
					+ " inner join sec_securities b "
					+ " on a.pk_securities = b.pk_securities "
					+ " where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-01' and a.pk_org = '"
					+ SimReportUtils.getOrg(conVos)
					+ "' and a.pk_glorgbook = '"
					+ SimReportUtils.getGLBook(conVos)
					+ "' and a.trade_date <= '"
					+ startDate
					+ "' and if_accrual='N' "
					+ glsql
					+ " group by a.pk_securities";
			List<ReportDataVO> reportVos1 = (List<ReportDataVO>) queryservice
					.executeQuery(querySql1, new BeanListProcessor(
							ReportDataVO.class));
			// 查询除去所选时间段内发生的证券档案的卖出总和
			String querySql2 = "select  a.pk_securities, sum(a.entrust_num) as innum, sum(a.entrust_sum) as insum"
					+ " from sim_agreedbb a "
					+ " inner join sec_securities b "
					+ " on a.pk_securities = b.pk_securities "
					+ " where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-03' and a.pk_org = '"
					+ SimReportUtils.getOrg(conVos)
					+ "' and a.pk_glorgbook = '"
					+ SimReportUtils.getGLBook(conVos)
					+ "' and a.trade_date <= '"
					+ startDate
					+ "' and if_accrual='N' "
					+ glsql
					+ " group by a.pk_securities";
			List<ReportDataVO> reportVos2 = (List<ReportDataVO>) queryservice
					.executeQuery(querySql2, new BeanListProcessor(
							ReportDataVO.class));
			// 期初 = 买入-卖出
			if (reportVos1 != null && reportVos1.size() > 0) {
				if (reportVos2 != null && reportVos2.size() > 0) {
					for (ReportDataVO rdv1 : reportVos1) {
						String pk_securities = rdv1.getPk_securities();
						for (ReportDataVO rdv2 : reportVos2) {
							if (pk_securities.equals(rdv2.getPk_securities())) {
								rdv1.setInnum(rdv1.getInnum().sub(
										rdv2.getInnum()));
								rdv1.setInsum(rdv1.getInsum().sub(
										rdv2.getInsum()));
							}
						}
					}
				}
				// 循环转入map
				SecInvestDailyRepVO rprtvo2 = null;

				for (ReportDataVO rdv1 : reportVos1) {
					if (rdv1.getInnum().compareTo(new UFDouble(0)) > 0) {
						String pk_securities = rdv1.getPk_securities();
						String querySql3 = "select a.vdef9 as pk_selfsgroup,a.pk_org, a.pk_group, a.pk_glorgbook, a.vdef8 as pk_assetsprop, a.vdef7 as pk_stocksort, a.transtypecode as pk_billtype, a.vdef6 as pk_capaccount, a.vdef5 as pk_client, a.vdef4 as pk_operatesite, a.vdef3 as pk_partnaccount, a.pk_securities, a.trade_date, a.vdef2 as begin_date, a.vdef1 as end_date, a.def_interest as buyaccrual"
								+ " from sim_agreedbb a "
								+ " inner join sec_securities b "
								+ " on a.pk_securities = b.pk_securities "
								+ " where a.state>1 and nvl(a.dr, 0) = 0 and a.transtypecode = 'HV5A-0xx-01' and a.pk_securities = '"
								+ pk_securities + "'";
						List<ReportDataVO> reportVos = (List<ReportDataVO>) queryservice
								.executeQuery(querySql3, new BeanListProcessor(
										ReportDataVO.class));
						ReportDataVO reportVo = new ReportDataVO();
						reportVo = reportVos.get(0);

						// 组装StockBalanceVO
						StockBalanceVO sbv = new StockBalanceVO();
						sbv.setPk_securities(pk_securities);
						sbv.setPk_org(reportVo.getPk_org());
						// sbv.setTrade_date(reportVo.getTrade_date());
						sbv.setPk_stockbalance(new UFDateTime().toString());
						sbv.setPk_group(reportVo.getPk_group());
						sbv.setPk_glorgbook(reportVo.getPk_glorgbook());
						sbv.setStocks_num(rdv1.getInnum());
						sbv.setStocks_sum(rdv1.getInsum());
						sbvs.add(sbv);
					}
				}
			}
			return sbvs;
		} catch (Exception e) {
			throw new BusinessException("期初查询出错！");
		}
	}

	// 去重复数据
	public static List<String> array_unique(List<String> a) {
		// array_unique
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < a.size(); i++) {
			if (!list.contains(a.get(i))) {
				list.add(a.get(i));
			}
		}
		return list;
	}

	// 去除VO重复主键
	public List<String> vo_unique(List<AgreedbbVO> greedbbVos) {
		List<AgreedbbVO> vos = new ArrayList<AgreedbbVO>();
		List<String> list = new ArrayList<String>();
		if (greedbbVos != null && greedbbVos.size() > 0) {
			for (AgreedbbVO vo : greedbbVos) {
				list.add(vo.getPk_securities());
			}
		}
		return list;
	}
	
	public TradeMarketVO queryLastMarket(String pk_securities,String trade_date,boolean isqc)throws BusinessException{
		TradeMarketVO marketvo = null;
		if(pk_securities == null || trade_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select * from sim_trademarket where pk_securities = '"+pk_securities+"' ");
		sf.append(" and trade_date =  ");
		sf.append(" (select isnull(max(trade_date),'"+CostConstant.DEFAULT_DATE+"')  from sim_trademarket where ");
		if(isqc){
			sf.append(" isnull(dr, 0) = 0 and trade_date < '"+trade_date+"' ");//期初
		}else{
			sf.append(" isnull(dr, 0) = 0 and trade_date <= '"+trade_date+"' ");//期末
		}
		sf.append(" and pk_securities = '"+pk_securities+"') ");
		sf.append(" and isnull(dr, 0) = 0 ");
		List<TradeMarketVO> list = (List<TradeMarketVO>)new BaseDAO().executeQuery(sf.toString(), new BeanListProcessor(TradeMarketVO.class));
		if(list != null && list.size() > 0 ){
			marketvo = list.get(0);
		}
		return marketvo;
	}

}
