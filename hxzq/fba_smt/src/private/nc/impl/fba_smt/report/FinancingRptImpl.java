package nc.impl.fba_smt.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.fba_smt.report.IFinancingRpt;
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
import nc.vo.fba_scost.cost.fairvaluedistill.Sim_fairvaluedistillVO;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_smt.report.FinancingRptVO;
import nc.vo.fba_smt.report.FinancingRptViewMeta;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

public class FinancingRptImpl implements IFinancingRpt {

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		ConditionVO[] conVos = (ConditionVO[])context.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
	    if (qryconutil.isNull() && (conVos == null || conVos.length ==0)){
	    	ReportQueryResult result = new ReportQueryResult();
	    	DataSet dataset = new DataSet();
	        dataset.setMetaData(this.getMetaData());
	        result.setDataSet(dataset);
	    	return result;
	    }
	    
	    ReportQueryResult queryResult = new ReportQueryResult();
	    queryResult.setDataSet(genFinaceDataSet(context, conVos));
	    return queryResult;
	}

	private DataSet genFinaceDataSet(IContext context, ConditionVO[] conVos) throws BusinessException {
		
		String dateValue = SimReportUtils.getDateCon(conVos);
		String org = SimReportUtils.getOrg(conVos);
		String glBook = SimReportUtils.getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);
		boolean isrzrq=false;
		try {
			isrzrq=SysInitCache.getInstance().getIsRzrq(de_group,org, glBook);
		} catch (Exception e1) {
			Logger.error(e1.getMessage());
		}
		if(!isrzrq){
			throw new BusinessException("当前业务单元不是融资融券部门！");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("pk_org", org);
		map.put("pk_group", de_group);
		map.put("pk_glorgbook", glBook);

		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
				.getDateBefore(ReportConst.DAY);
		String endDate = SimReportUtils.getEndDate(dateValue);
		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);
		
		PubReportDataQuery query = new PubReportDataQuery();
		List<StockBalanceVO> firBal = null;
		List<StockBalanceVO> endBal = null;
		String condition = SMTReportUtil.getStockBalaCondition(ReportConst.FUND_B_MARK, conVos);
		try {
			//查询库存期初
			firBal = query.getStockBalance(condition,startDate.toString(), costPlanVO);
			//查询库存期末
			endBal = query.getStockBalance(condition, endDate, costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("期初查询出错！");
		}
		
//		List<Sim_fairvaluedistillVO> fairBal = null;
//		try {
//			fairBal = SMTReportUtil.queryLastFairValue(map, costPlanVO, new UFDate(endDate));
//		} catch (Exception e) {
//			throw new BusinessException("公允价值查询出错！");
//		}
		String key = null;
		// 迭代期初，如果发生中存在，则直接赋值，如果不存在，则新增
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
		FinancingRptVO drv = null;
		StockBalanceVO sbv = null;
		Sim_fairvaluedistillVO fdv = null;
		String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		Map<String, FinancingRptVO> trademap = new HashMap<String, FinancingRptVO>();
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
				drv = new FinancingRptVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drv.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drv);
			}
			
			drv.setFirstnum(pm.add(drv.getFirstnum(), sbv.getStocks_num()));
			drv.setFirstsum(pm.add(drv.getFirstsum(), sbv.getStocks_sum()));
			drv.setTrade_date(startDate.getDateAfter(ReportConst.DAY));//由于自由报表还会通过时间过滤，这里写死时间
		}
		
		/**
		 * 获取期末公允价值
		 * YangJie2014-07-10
		 */
		Map gyjzmap=query.getFairValue(endDate.substring(0,10)+" 00:00:00", costPlanVO, false);
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
				drv = new FinancingRptVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drv.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drv);
			}
			drv.setFair_market((UFDouble) gyjzmap.get(key));
			drv.setEndnum(pm.add(drv.getEndnum(), sbv.getStocks_num()));
			drv.setEndsum(pm.add(drv.getEndsum(), sbv.getStocks_sum()));
			drv.setTrade_date(startDate.getDateAfter(ReportConst.DAY));
		}
		// 迭代公允值

//		int fairSize = fairBal.size();
//		for (int i = 0; i < fairSize; i++) {
//			fdv = fairBal.get(i);
//			key = VOUtil.getCombinesKey(fdv, manFieldV);
//			drv = trademap.get(key);
//			if (drv == null) {
//				continue;
//			}
//			drv.setTrade_date(startDate.getDateAfter(ReportConst.DAY));
//		}
		Collection<FinancingRptVO> c = trademap.values();
		
		return PubMethod.getInstance().convertVOToArray(getMetaData(), new ArrayList(c));
	}

	private MetaData getMetaData() {
		List<Field> list = new ArrayList<Field>();
		
		for(String key : FinancingRptViewMeta.FUNDTRADE_S_FIELD){
			Field field = ReportDataUtil.createStringFiled(key);
		    list.add(field);
		}
		
		for(String key : FinancingRptViewMeta.EXT_D_FIELD){
			Field field = ReportDataUtil.createDoubleField(key);
		    list.add(field);
		}
		
		for(String key : FinancingRptViewMeta.EXT_DATE_FIELD){
			Field field = ReportDataUtil.createStringFiled(key);
		    list.add(field);
		}
		
		Field[] fields = new Field[list.size()];
	    list.toArray(fields);
	    return new MetaData(fields);
	}

}
