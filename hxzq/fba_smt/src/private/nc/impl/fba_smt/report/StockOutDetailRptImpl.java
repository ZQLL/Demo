package nc.impl.fba_smt.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

import nc.bs.logging.Logger;
import nc.itf.fba_smt.report.IStockOutDetailRpt;
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
import nc.vo.fba_smt.report.StockOutDetailVO;
import nc.vo.fba_smt.report.StockOutDetailViewMeta;
import nc.vo.fba_smt.trade.margintrade.MarginTradeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

public class StockOutDetailRptImpl implements IStockOutDetailRpt {

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
		queryResult.setDataSet(getStockOutDetailDataSet(context, conVos));
		return queryResult;

	}

	private DataSet getStockOutDetailDataSet(IContext context,
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
		StockOutDetailVO drv = null;
		StockBalanceVO sbv = null;
		// String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		String[] manFieldV = new String[] { "pk_assetsprop", "pk_group",
				"pk_org", "pk_glorgbook", "pk_capaccount", "begin_date",
				"end_date", "pk_securities" };
		Map<String, StockOutDetailVO> trademap = new HashMap<String, StockOutDetailVO>();
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
				drv = new StockOutDetailVO();
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
		Map<String, UFDouble> closeMap = query.getClosePrice(endDate);
		// 向客户融出证券(融出证券)1
		List<SuperVO> rcvos = null;
		try {
			rcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCJL);
			trademap = assignTransferData(trademap, rcvos, manFieldV, 1,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询向客户融出证券出错！");
		}
		// 其他减少(红股融出) 2
		List<SuperVO> hgrcvos = null;
		try {
			hgrcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGRCJL);
			trademap = assignTransferData(trademap, hgrcvos, manFieldV, 2,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询已融库存其他增加数据出错！");
		}
		// 客户归还证券(融出归还) 3
		List<SuperVO> rcghvos = null;
		try {
			rcghvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCGHJL);
			trademap = assignTransferData(trademap, rcghvos, manFieldV, 3,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询客户归还证券出错！");
		}
		// 转为逾期证券(记录) 4
		List<SuperVO> yqzqvos = null;
		try {
			yqzqvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.YQJL);
			trademap = assignTransferData(trademap, yqzqvos, manFieldV, 4,
					startDate);// 填充数据
		} catch (Exception e) {
			throw new BusinessException("查询转为逾期记录出错！");
		}

		for (StockOutDetailVO vo : trademap.values()) {
			// 增加数量 = 期初+本期增加
			UFDouble addnum = pm.add(vo.getFirstnum(),// 期初
					pm.add(vo.getRczqnum(),// 融出证券
							vo.getHgrcnum()));// 红股融出
			UFDouble addsum = pm.add(vo.getFirstsum(),// 期初
					pm.add(vo.getRczqsum(),// 融出证券
							vo.getHgrcsum()));// 红股融出
			// 减少数量
			UFDouble subnum = pm.add(vo.getRcghnum(), vo.getYqjlnum());// 融出归还+逾期记录
			UFDouble subsum = pm.add(vo.getRcghsum(), vo.getYqjlsum());// 融出归还+逾期记录
			// 期末结存= 增加数量 - 减少数量
			UFDouble endnum = pm.add(vo.getEndnum(), addnum).sub(subnum);
			UFDouble endsum = pm.add(vo.getEndsum(), addsum).sub(subsum);
			vo.setEndnum(endnum);
			vo.setEndsum(endsum);
			// 收盘价
			UFDouble closeprice = closeMap.get(vo.getPk_securities());
			vo.setCloseprice(closeprice);
			// 证券市值 = 收盘价*期末结存数量
			UFDouble marketvalue = pm.multiply(closeprice, endnum);
			vo.setMarketValue(marketvalue);
			// 潜盈(亏) = 证券市值-期末结存金额
			vo.setProfit(pm.sub(marketvalue, endsum));
		}
		Collection<StockOutDetailVO> c = trademap.values();
		return PubMethod.getInstance().convertVOToArray(genDataSet(),
				new ArrayList(c));
	}

	private MetaData genDataSet() {
		List<Field> list = new ArrayList<Field>();

		for (String key : StockOutDetailViewMeta.FUNDTRADE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		for (String key : StockOutDetailViewMeta.EXT_D_FIELD) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}

		for (String key : StockOutDetailViewMeta.EXT_DATE_FIELD) {
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
	private Map<String, StockOutDetailVO> assignTransferData(
			Map<String, StockOutDetailVO> trademap, List<SuperVO> mtvos,
			String[] manFieldV, int flag, UFDate startDate) {
		SuperVO mvto = null;
		StockOutDetailVO drv = null;
		PubMethod pm = PubMethod.getInstance();
		// 这里去掉库存组织条件 因为转融通借入可融库 归还证金库 会分两条数据显示出来
		manFieldV = new String[] { "pk_assetsprop", "pk_group", "pk_org",
				"pk_glorgbook", "pk_capaccount", "begin_date", "end_date",
				"pk_securities" };
		if (mtvos != null && mtvos.size() > 0) {
			for (int i = 0; i < mtvos.size(); i++) {
				mvto = (MarginTradeVO) mtvos.get(i);
				String key = VOUtil.getCombinesKey(mvto, manFieldV);
				if (trademap.containsKey(key)) {// 已经存在
					drv = trademap.get(key);
				} else {
					drv = new StockOutDetailVO();
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
				} else if (4 == flag) {// 逾期记录
					drv.setYqjlnum(pm.add(drv.getYqjlnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setYqjlsum(pm.add(drv.getYqjlsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				}
				drv.setTrade_date(startDate);// 由于自由报表还会通过时间过滤，这里写死时间
			}
		}
		return trademap;
	}

}
