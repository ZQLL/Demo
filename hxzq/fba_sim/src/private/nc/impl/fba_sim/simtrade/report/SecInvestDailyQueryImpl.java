package nc.impl.fba_sim.simtrade.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_sim.simtrade.report.FundInOutUtils;
import nc.bs.fba_sim.simtrade.report.ReportSqlUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_sim.simtrade.report.ISecInvestDailyQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.PubReportDataQuery;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.pub.SimINFOPubMethod;
import nc.vo.fba_sim.simbs.checkplan.CheckplanVO;
import nc.vo.fba_sim.simtrade.report.SecInvestDailyRepVO;
import nc.vo.fba_sim.simtrade.report.SecInvestDailyViewMeta;
import nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.freereport.ReportDataVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

public class SecInvestDailyQueryImpl implements ISecInvestDailyQuery {

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
				manFieldV);

		List<StockBalanceVO> firBal = null;
		List<StockBalanceVO> endBal = null;
		PubReportDataQuery query = new PubReportDataQuery();
		try {
			// 查询库存期初
			firBal = query.getStockBalance(SimReportUtils.getQueryCondition(
					ReportConst.FUND_B_MARK, conVos, true, false), startDate
					.toString(), costPlanVO);
			// 查期末
			endBal = query.getStockBalance(SimReportUtils.getQueryCondition(
					ReportConst.FUND_B_MARK, conVos, true, false), endDate,
					costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("期初查询出错！");
		}

		SecInvestDailyRepVO drv = null;
		String key = null;
		StockBalanceVO sbv = null;

		// 迭代期初，如果发生中存在，则直接赋值，如果不存在，则新增
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
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
			drv.setFirstNum(sbv.getStocks_num());
			drv.setFirstSum(sbv.getStocks_sum());
			drv.setBegin_date(sbv.getBegin_date() == null ? null : new UFDate(
					sbv.getBegin_date().getMillis()));
			drv.setEnd_date(sbv.getEnd_date() == null ? null : new UFDate(sbv
					.getEnd_date().getMillis()));
			trademap.put(key, drv);
		}

		// 迭代期末，如果发生中存在，则直接赋值，如果不存在，则新增
		int endSize = endBal.size();
		for (int i = 0; i < endSize; i++) {
			sbv = endBal.get(i);
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
			drv.setEndNum(sbv.getStocks_num());
			drv.setEndSum(sbv.getStocks_sum());
			drv.setBegin_date(sbv.getBegin_date() == null ? null : new UFDate(
					sbv.getBegin_date().getMillis()));
			drv.setEnd_date(sbv.getEnd_date() == null ? null : new UFDate(sbv
					.getEnd_date().getMillis()));
			trademap.put(key, drv);
		}

		Collection<SecInvestDailyRepVO> c = trademap.values();
		Iterator<SecInvestDailyRepVO> iter = c.iterator();
		// String cond = "";
		// cond = " a.pk_glorgbook='" + glBook + "'";
		// UFDouble gyz = zero;
		List<SecInvestDailyRepVO> resultVOs = new ArrayList<SecInvestDailyRepVO>();
		PubMethod pm = PubMethod.getInstance();

		/**
		 * 获取公允价值 YangJie2014-07-10
		 */
		Map map = query.getFairValue(endDate.substring(0, 10) + " 00:00:00",
				costPlanVO, false);
		while (iter.hasNext()) {
			SecInvestDailyRepVO repVo = iter.next();
			repVo.setEnd_date(new UFDate(endDate));
			repVo.setFirstPrice(PubMethod.getInstance().divCal(
					repVo.getFirstSum(), repVo.getFirstNum()));
			repVo.setBuyPrice(PubMethod.getInstance().divCal(repVo.getBuySum(),
					repVo.getBuyNum()));
			repVo.setSellPrice(PubMethod.getInstance().divCal(
					repVo.getSellSum(), repVo.getSellNum()));
			repVo.setEndPrice(PubMethod.getInstance().divCal(repVo.getEndSum(),
					repVo.getEndNum()));
			String pk_securities = repVo.getPk_securities();
			
			//期初市值
			try {
				UFDouble first_marketPrice = SimINFOPubMethod.getInstance()
						.getClosePrice(startDate.toString().substring(0, 10),
								repVo.getPk_securities());
				UFDouble first_marketSum = pm.multiply(first_marketPrice,
						repVo.getFirstNum());
				first_marketSum = first_marketSum.setScale(2, UFDouble.ROUND_HALF_UP);
				repVo.setFirst_marketSum(first_marketSum);
				
				
			} catch (Exception e) {
				Logger.error(e);
			}
			
			//查份额分红数据,并按维度设置份额分红数据 add by lihaibo start 2017-06-13
			try {
				String querySql = this.getfefhsql();
				if (StringUtils.isBlank(querySql)) {
					continue;
				}
				ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
				IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
				String condition = SimReportUtils.getQueryCondition(
						ReportConst.FUND_B_MARK, conVos, false, false);
				querySql = reportUtil.getQuerySql(condition, querySql);
				querySql += 
						" group by a.pk_org,\n" +
								"       a.pk_group,\n" + 
								"       a.pk_glorgbook,\n" + 
								"       a.pk_assetsprop,\n" + 
								"       a.pk_capaccount,\n" + 
								"       a.pk_securities";
				@SuppressWarnings("unchecked")
				List<SecInvestDailyRepVO> fefhVos = (List<SecInvestDailyRepVO>) queryservice
						.executeQuery(querySql, new BeanListProcessor(
								SecInvestDailyRepVO.class));
				if (fefhVos != null && fefhVos.size() > 0) {
					for (SecInvestDailyRepVO fefhVo : fefhVos) {
						if (fefhVo.getPk_assetsprop().equals(repVo.getPk_assetsprop()) && 
								fefhVo.getPk_capaccount().equals(repVo.getPk_capaccount()) && 
								fefhVo.getPk_securities().equals(repVo.getPk_securities())) {
								repVo.setFefh(fefhVo.getFefh());
								break;
						}
					}
				}
				/*//加上计提的未付收益
				String querySql_jtwfsy = this.getfefhsql_jtwfsy();
				querySql_jtwfsy = reportUtil.getQuerySql(condition, querySql_jtwfsy);
				querySql_jtwfsy += 
						" group by a.pk_org,\n" +
								"       a.pk_group,\n" + 
								"       a.pk_glorgbook,\n" + 
								"       a.pk_assetsprop,\n" + 
								"       a.pk_capaccount,\n" + 
								"       a.pk_securities";
				List<SecInvestDailyRepVO> fefhVos_jtwfsy = (List<SecInvestDailyRepVO>) queryservice
						.executeQuery(querySql_jtwfsy, new BeanListProcessor(
								SecInvestDailyRepVO.class));
				if (fefhVos_jtwfsy != null && fefhVos_jtwfsy.size() > 0) {
					for (SecInvestDailyRepVO fefhVo : fefhVos_jtwfsy) {
						if (fefhVo.getPk_assetsprop().equals(repVo.getPk_assetsprop()) && 
								fefhVo.getPk_capaccount().equals(repVo.getPk_capaccount()) && 
								fefhVo.getPk_securities().equals(repVo.getPk_securities())) {
								repVo.setFefh(repVo.getFefh().add(fefhVo.getFefh()));
								break;
						}
					}
				}*/
			} catch (Exception e) {
				Logger.error(e);
			}
			//计算转入证券-转出证券vdef2
			UFDouble tatal_wfsy = sumWFSY(repVo, startDate, new UFDate(endDate));
			UFDouble tatal_vdef4 = sumVDEF4(repVo, startDate, new UFDate(endDate));
			repVo.setFefh(pm.add(repVo.getFefh(), tatal_vdef4.add(tatal_wfsy)));
			/***********end*********/
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
		ReportSqlUtil reportUtil = ReportSqlUtil.getInstance();
		for (CheckplanVO planVO : checkPlanVOs) {
			// String dirction = planVO.getVdef1();
			String billType = planVO.getPk_billtype();

			querySql = reportUtil.getReportSql(billType);
			if (StringUtils.isBlank(querySql)) {
				continue;
			}

			querySql = reportUtil.getQuerySql(condition, querySql);
			try {
				List<ReportDataVO> reportVos = (List<ReportDataVO>) queryservice
						.executeQuery(querySql, new BeanListProcessor(
								ReportDataVO.class));
				
				
				resultDatas.addAll(reportVos);
			} catch (Exception e) {
				throw new BusinessException("明细查询错误!" + e.getMessage());
			}
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
			List<ReportDataVO> datavos, String[] manFieldV) {
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
	
	//查询份额分红sql
	private String getfefhsql() {
		String sql = 
				"select a.pk_org,\n" +
						"       a.pk_group,\n" + 
						"       a.pk_glorgbook,\n" + 
						"       a.pk_assetsprop,\n" + 
						"       a.pk_capaccount,\n" + 
						"       a.pk_securities,\n" + 
						"       sum(round(a.bargain_sum,2)) fefh"+
						"  from sim_stocktrade a\n" + 
						" inner join sec_securities b\n" + 
						"    on a.pk_securities = b.pk_securities\n" + 
						" inner join sec_busitype c\n" + 
						"    on a.pk_busitype = c.pk_busitype\n" + 
						"   and nvl(c.dr, 0) = 0\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and c.code in ('104','0184','205')\n" ;
						//"   and a.transtypecode = 'HV3A-0xx-05'";
		return sql;
	}
	
	//查询计提未付收益sql
	private String getfefhsql_jtwfsy() {
		String sql = 
				"select a.pk_org,\n" +
						"       a.pk_group,\n" + 
						"       a.pk_glorgbook,\n" + 
						"       a.pk_assetsprop,\n" + 
						"       a.pk_capaccount,\n" + 
						"       a.pk_securities,\n" + 
						"       sum(round(a.bargain_sum,2)) fefh"+
						"  from sim_stocktrade a\n" + 
						" inner join sec_securities b\n" + 
						"    on a.pk_securities = b.pk_securities\n" + 
						" inner join sec_busitype c\n" + 
						"    on a.pk_busitype = c.pk_busitype\n" + 
						"   and nvl(c.dr, 0) = 0\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and c.code in ('206','106')\n"+
						"   and a.transtypecode = 'HV3A-0xx-14'";
		return sql;
	}
	
	/**
	 * 计算 转换转入-转换转出
	 * 转换转入（转入证券对应的VDEF2）
		转换转出（转出证券对应的VDEF2）
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumJZWFSY(SecInvestDailyRepVO repVo, UFDate start_date, UFDate end_date) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		//转出证券
		String sql = 
				"select to_char(nvl(sum(case\n" +
						"                         when a.vdef2 is null then\n" + 
						"                          0\n" + 
						"                         when a.vdef2 = '~' then\n" + 
						"                          0\n" + 
						"                         else\n" + 
						"                          to_number(a.vdef2)\n" + 
						"                       end),\n" + 
						"                   0)) total_zc\n" + 
						"  from sim_transformtrade a\n" + 
						" inner join sec_securities b\n" + 
						"    on a.pk_securities = b.pk_securities\n" + 
						" inner join sec_billtype c\n" + 
						"    on a.transtypecode = c.pk_billtypecode\n" + 
						"   and nvl(c.dr, 0) = 0\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and a.transtypecode = 'HV3F-0xx-01'\n" + 
						"   and a.trade_date > ?\n" + 
						"   and a.trade_date < ?\n" + 
						"   and a.pk_securities = ?\n" + 
						"   and a.hc_pk_assetsprop = ?\n" + 
						"   and a.hc_pk_capaccount = ?\n" + 
						"   and a.pk_org = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(start_date.toString());
		para.addParam(end_date.getDateAfter(1).toString());
		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total = (String) new BaseDAO().executeQuery(sql, para, new ColumnProcessor());
			total_wfsy = new UFDouble(total);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		String sql_zr = 
				"select to_char(nvl(sum(case\n" +
						"                         when a.vdef2 is null then\n" + 
						"                          0\n" + 
						"                         when a.vdef2 = '~' then\n" + 
						"                          0\n" + 
						"                         else\n" + 
						"                          to_number(a.vdef2)\n" + 
						"                       end),\n" + 
						"                   0)) total_zr\n" + 
						"  from sim_transformtrade a\n" + 
						" inner join sec_securities b\n" + 
						"    on a.pk_securities = b.pk_securities\n" + 
						" inner join sec_billtype c\n" + 
						"    on a.transtypecode = c.pk_billtypecode\n" + 
						"   and nvl(c.dr, 0) = 0\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and a.transtypecode = 'HV3F-0xx-01'\n" + 
						"   and a.trade_date > ?\n" + 
						"   and a.trade_date < ?\n" + 
						"   and a.pk_securities2 = ?\n" + 
						"   and a.hr_pk_assetsprop = ?\n" + 
						"   and a.hr_pk_capaccount = ?\n" + 
						"   and a.pk_org = ?";
		para.clearParams();
		para.addParam(start_date.toString());
		para.addParam(end_date.getDateAfter(1).toString());
		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total_zr = (String) new BaseDAO().executeQuery(sql_zr, para, new ColumnProcessor());
			total_wfsy = new UFDouble(total_zr).sub(total_wfsy);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return total_wfsy;
	}
	
	/**
	 * 计算 份额分红交易类型总的计提未付收益
	 * @author lihbj
	 * @return
	 */
	@SuppressWarnings("unused")
	private UFDouble sumVDEF3(SecInvestDailyRepVO repVo, UFDate start_date, UFDate end_date) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		String sql = 
				"select to_char(nvl(sum(case\n" +
						"                         when a.vdef3 is null then\n" + 
						"                          0\n" + 
						"                         when a.vdef3 = '~' then\n" + 
						"                          0\n" + 
						"                         else\n" + 
						"                          to_number(a.vdef3)\n" + 
						"                       end),\n" + 
						"                   0)) total_zc\n" + 
						"  from sim_stocktrade a\n" + 
						" inner join sec_securities b\n" + 
						"    on a.pk_securities = b.pk_securities\n" + 
						" inner join sec_billtype c\n" + 
						"    on a.transtypecode = c.pk_billtypecode\n" + 
						"   and nvl(c.dr, 0) = 0\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and a.transtypecode = 'HV3A-0xx-05'\n" + 
						"   and a.trade_date > ?\n" + 
						"   and a.trade_date < ?\n" + 
						"   and a.pk_securities = ?\n" + 
						"   and a.pk_assetsprop = ?\n" + 
						"   and a.pk_capaccount = ?\n" + 
						"   and a.pk_org = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(start_date.toString());
		para.addParam(end_date.toString());
		
		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total = (String) new BaseDAO().executeQuery(sql, para, new ColumnProcessor());
			total_wfsy = new UFDouble(total);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return total_wfsy;
	}
	
	/**
	 * 计算 证券卖出记录交易类型总的应收股利
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumVDEF4(SecInvestDailyRepVO repVo, UFDate start_date, UFDate end_date) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		String sql = 
				"select to_char(nvl(sum(case\n" +
						"                         when a.vdef4 is null then\n" + 
						"                          0\n" + 
						"                         when a.vdef4 = '~' then\n" + 
						"                          0\n" + 
						"                         else\n" + 
						"                          to_number(a.vdef4)\n" + 
						"                       end),\n" + 
						"                   0)) total_zc\n" + 
						"  from sim_stocktrade a\n" + 
						" inner join sec_securities b\n" + 
						"    on a.pk_securities = b.pk_securities\n" + 
						" inner join sec_busitype c\n" + 
						"    on a.pk_busitype = c.pk_busitype\n" + 
						"   and nvl(c.dr, 0) = 0\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n" + 
						"   and a.transtypecode = 'HV3A-0xx-02'\n" + 
						"   and c.code in ('202','103','0182','0122')\n" + 
						"   and a.trade_date > ?\n" + 
						"   and a.trade_date < ?\n" + 
						"   and a.pk_securities = ?\n" + 
						"   and a.pk_assetsprop = ?\n" + 
						"   and a.pk_capaccount = ?\n" + 
						"   and a.pk_org = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(start_date.toString());
		para.addParam(end_date.getDateAfter(1).toString());
		
		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total = (String) new BaseDAO().executeQuery(sql, para, new ColumnProcessor());
			total_wfsy = new UFDouble(total);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return total_wfsy;
	}
	
	/**
	 * 计算上一次份额分红或者证券卖出记录之后，所有计提未付收益相加
	 * @author lihbj
	 * @return
	 */
	/**
	 * @param repVo
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	/**
	 * @param repVo
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	private UFDouble sumWFSY(SecInvestDailyRepVO repVo, UFDate start_date, UFDate end_date) {
		UFDate startDate = start_date;
		UFDouble sum_jzwfsy = UFDouble.ZERO_DBL;
		UFDate lastFefh = new UFDate();//上一次份额分红日期
		UFDate lastZqmc = new UFDate();//上一次证券卖出记录日期
		//首先找到该支券上一次的份额分红交易时间
		String fefh = 
				"select nvl(max(a.trade_date), '2000-01-01 15:15:08') trade_date\n" + 
				"                         from sim_stocktrade a\n" + 
				"                        where nvl(a.dr, 0) = 0\n" + 
				"                          and a.trade_date < ?\n" + 
				"                          and a.transtypecode = 'HV3A-0xx-05'\n" + 
				"                          and a.state > 1\n" + 
				"                          and a.pk_org = ?\n" + 
				"                          and a.pk_assetsprop = ?\n" + 
				"                          and a.pk_capaccount = ?\n" + 
				"                          and a.pk_stocksort = ?\n" + 
				"                          and a.pk_securities = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(end_date.getDateAfter(1).toString());
		para.addParam(repVo.getPk_org());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_stocksort());
		para.addParam(repVo.getPk_securities());
		try {
			String last_fefh = (String) new BaseDAO().executeQuery(fefh, para, new ColumnProcessor());
			lastFefh = new UFDate(last_fefh);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		// 找到上一次证券卖出记录的交易日期
		String mcjl = 
				"select nvl(max(a.trade_date), '2000-01-01 15:15:08') trade_date\n" + 
				" from sim_stocktrade a\n" + 
				" inner join sec_busitype c\n" + 
				"    on a.pk_busitype = c.pk_busitype\n" + 
				"   and nvl(c.dr, 0) = 0\n" + 
				" where nvl(a.dr, 0) = 0\n" + 
				" and a.trade_date < ?\n" + 
				" and a.transtypecode = 'HV3A-0xx-02'\n" + 
				" and c.code in ('202','103')\n" + 
				" and a.state > 1\n" + 
				" and a.pk_org = ?\n" + 
				" and a.pk_assetsprop = ?\n" + 
				" and a.pk_capaccount = ?\n" + 
				" and a.pk_stocksort = ?\n" + 
				" and a.pk_securities = ?";
		para.clearParams();
		para.addParam(end_date.getDateAfter(1).toString());
		para.addParam(repVo.getPk_org());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_stocksort());
		para.addParam(repVo.getPk_securities());
		try {
			String last_zqmc = (String) new BaseDAO().executeQuery(mcjl, para, new ColumnProcessor());
			lastZqmc = new UFDate(last_zqmc);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		//取最近的一个交易日期
		if (lastFefh.compareTo(lastZqmc) >= 0) {
			start_date = lastFefh;
		} else {
			start_date = lastZqmc;
		}
		// 改为直接取最后一次的份额分红日期 ，不动以前代码
		start_date = lastFefh;
		
		String sql = 
				"select to_char(nvl(sum(a.bargain_sum), 0))\n" +
						"  from sim_stocktrade a\n" + 
						" inner join sec_busitype b\n" + 
						"    on a.pk_busitype = b.pk_busitype\n" + 
						" where nvl(a.dr, 0) = 0\n" + 
						"   and nvl(b.dr, 0) = 0\n" + 
						"   and a.trade_date > ?\n" + 
						"   and a.trade_date < ?\n" + 
						"   and b.code in ('206','106')\n" + 
						"   and a.transtypecode = 'HV3A-0xx-14'\n" + 
						"   and a.state > 1\n" + 
						"   and a.pk_org = ?\n" + 
						"   and a.pk_assetsprop = ?\n" + 
						"   and a.pk_capaccount = ?\n" + 
						"   and a.pk_stocksort = ?\n" + 
						"   and a.pk_securities = ?";
		para.clearParams();
		para.addParam(startDate.toString());
		para.addParam(end_date.getDateAfter(1).toString());
		para.addParam(repVo.getPk_org());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_stocksort());
		para.addParam(repVo.getPk_securities());
		try {
			//最后一次份额分红 需要改为累计
			Double total = 0.0;
			Vector tt = (Vector)new BaseDAO().executeQuery(sql, para, new VectorProcessor());
			if(tt!=null){
				for(int x = 0;x<tt.size();x++){
					total += Double.valueOf(((Vector)tt.get(x)).get(0).toString());
				}
			}
			sum_jzwfsy = new UFDouble(total);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		// 最后一次赎回+份额分红 vdef3 需要改为累计
		/*
		 * @author zq
		 * */
		String sql_sh = 
				"select a.vdef3"+
						"  from sim_stocktrade a\n" + 
						" inner join sec_securities b\n" + 
						"    on a.pk_securities = b.pk_securities\n" + 
						" inner join sec_busitype c\n" + 
						"    on a.pk_busitype = c.pk_busitype\n" + 
						"   and nvl(c.dr, 0) = 0\n" + 
						" where a.state > 1\n" + 
						"   and nvl(a.dr, 0) = 0\n and a.vdef3 <> '~'" + 
						"   and a.transtypecode in ('HV3A-0xx-02','HV3A-0xx-05')\n" + 
						"   and c.code in ('104','0184','205','202','103','0182','0122')\n" + 
						"   and a.trade_date > ?\n" + 
						"   and a.trade_date < ?\n" + 
						"   and a.pk_securities = ?\n" + 
						"   and a.pk_assetsprop = ?\n" + 
						"   and a.pk_capaccount = ?\n" + 
						"   and a.pk_org = ?";
		para.clearParams();
		para.addParam(startDate.toString());
		para.addParam(end_date.getDateAfter(1).toString());
		
		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			Double total = 0.0;
			Vector tt = (Vector)new BaseDAO().executeQuery(sql_sh, para, new VectorProcessor());
			if(tt.size()!=0){
				for(int x = 0;x<tt.size();x++){
					total += Double.valueOf(((Vector)tt.get(x)).get(0).toString());
				}
			}
			sum_jzwfsy = sum_jzwfsy.sub(new UFDouble(total));
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		return sum_jzwfsy;
	}

}
