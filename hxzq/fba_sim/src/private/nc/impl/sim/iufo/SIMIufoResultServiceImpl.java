package nc.impl.sim.iufo;

import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.itf.sim.iufo.ISIMIufoResultService;
import nc.itf.sim.iufo.IufoQuerySIMVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sim.iufo.SIMIufoFuncConst;

/**
 * SIM 模块 IUFO 函数后台总体类
 * 
 * @author hupl
 * 
 */
public class SIMIufoResultServiceImpl implements ISIMIufoResultService {
	/**
	 * SIM UIFO 函数后台入口
	 */
	@Override
	public Object getFuncResult(String funName, IufoQuerySIMVO simvo)
			throws Exception {
		// String pk_group = AppContextUtil.getPk_group();// 取默认值
		//
		// simvo.setPk_group(pk_group);// 当前登录集团
		if (SIMIufoFuncConst.SIM_ZQQCSL.equals(funName)) {// 库存期初数量
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, true, true);
		} else if (SIMIufoFuncConst.SIM_ZQQMSL.equals(funName)) {// 库存期末数量
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, false, true);
		} else if (SIMIufoFuncConst.SIM_ZQQCJE.equals(funName)) {// 库存期初金额
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, true, false);
		} else if (SIMIufoFuncConst.SIM_ZQQMJE.equals(funName)) {// 库存期末金额
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, false, false);
		} else if (SIMIufoFuncConst.SIM_ZQSZQC.equals(funName)) {// 市值期初金额
			SIM_ZQSZImpl szimp = new SIM_ZQSZImpl();
			return szimp.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_ZQSZQM.equals(funName)) {// 市值期末金额
			SIM_ZQSZImpl szimp = new SIM_ZQSZImpl();
			return szimp.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_HXZQSZQM.equals(funName)) {// 约定式购回-市值期末金额
			SIM_HXZQSZImpl szimp = new SIM_HXZQSZImpl();
			return szimp.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_ZQGYQC.equals(funName)) {// 公允价值期初金额
			SimStockFairValue sf = new SimStockFairValue();
			return sf.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_ZQGYQM.equals(funName)) {// 公允价值期末金额
			SimStockFairValue sf = new SimStockFairValue();
			return sf.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_ZQCJFS.equals(funName)) {// 本期发生成交金额
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			UFDouble qc = (UFDouble) slimp.getFuncResult(simvo, true, false);
			UFDouble qm = (UFDouble) slimp.getFuncResult(simvo, false, false);
			return qm.sub(qc);
		} else if (SIMIufoFuncConst.SIM_ZQXSCB.equals(funName)) {// 本期发生销售成本
			SIM_SellCostImpl slimp = new SIM_SellCostImpl();
			return slimp.getFuncResult(simvo);
			/** 2016年1月28日 jingqt 国金添加的5个函数 */
		} else if (SIMIufoFuncConst.SIM_QCCYBL.equals(funName)) {// 期初持有比例函数
			SIM_CYBL rate = new SIM_CYBL();
			return rate.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_QMCYBL.equals(funName)) {// 期末持有比例函数
			SIM_CYBL rate = new SIM_CYBL();
			return rate.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_JYRIPYL.equals(funName)) {// 交易日期偏移量
			SIM_JYRIPYL offset = new SIM_JYRIPYL();
			return offset.getFuncResult(simvo);
		} else if (SIMIufoFuncConst.SIM_CCZBPMBM.equals(funName)) {// 持仓数量/总股本占比证券排名（返回编码）
			return SIM_CCZBPMImpl.getInstance().getFuncResult(simvo, 1);
		} else if (SIMIufoFuncConst.SIM_CCZBPMMC.equals(funName)) {// 持仓数量/总股本占比证券排名（返回名称）
			return SIM_CCZBPMImpl.getInstance().getFuncResult(simvo, 2);
		} else if (SIMIufoFuncConst.SIM_CCZBPMPK.equals(funName)) {// 持仓数量/总股本占比证券排名（返回PK）
			return SIM_CCZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_CBZBPMBM.equals(funName)) {// 成本/净资本占比证券排名（返回编码）
			return SIM_CBZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_CBZBPMMC.equals(funName)) {// 成本/净资本占比证券排名（返回名称）
			return SIM_CBZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_CBZBPMPK.equals(funName)) {// 成本/净资本占比证券排名（返回PK）
			return SIM_CBZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_MRHYJZQM.equals(funName)) {
			SIM_MRHYJZQMImpl slimp = new SIM_MRHYJZQMImpl();
			return slimp.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_MCHYJZQM.equals(funName)) {
			SIM_MRHYJZQMImpl slimp = new SIM_MRHYJZQMImpl();
			return slimp.getFuncResult(simvo, false);
		}
		return 0;
	}

	/**
	 * 查询审核方案<br>
	 * <B>由于现在规定账薄也需要传值，故这个方法需要增加一个账薄的参数 modify by jingqt</B>
	 * 
	 * @param pk_group
	 * @param pk_org
	 * @param pk_glorybook
	 * @return
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2016-1-29 下午3:34:40
	 */
	public CostPlanVO queryCostPlanInfo(String pk_group, String pk_org,
			String pk_glorybook) throws BusinessException {
		if (pk_group == null || pk_org == null)
			return null;
		CostPlanVO[] planvos = null;
		StringBuffer condition = new StringBuffer();
		condition.append("  pk_org = '" + pk_org + "' ");
		condition.append("  and pk_group = '" + pk_group
				+ "' and isnull(dr,0) = 0 ");
		condition.append("  and isbusinessplan = 'Y' ");
		condition.append("  and pk_glorgbook = '" + pk_glorybook + "' ");

		planvos = (CostPlanVO[]) new HYSuperDMO().queryByWhereClause(
				CostPlanVO.class, condition.toString());
		if (planvos.length == 0 || planvos == null) {
			Logger.debug("查询出错！ 当前组织成本计算方案为空！！");
			return null;
		}

		return planvos[0];
	}

	/**
	 * 取期初日期
	 */
	public String getQcDate(IufoQuerySIMVO simvo) throws BusinessException {
		String period = simvo.getPeriod();// "2015-02-01"
		if (period == null || "".equals(period))
			throw new BusinessException("参数 '期间' 为空！，请设置！  ");
		String periodtype = simvo.getPeriodType();// { "年", "月", "日" };
		String pix_y = period.substring(0, 4);
		String pix_m = period.substring(0, 7);
		if ("年".equals(periodtype)) {
			period = pix_y + "-01-01 00:00:00";
		} else if ("月".equals(periodtype)) {
			period = pix_m + "-01 00:00:00";
		} else if ("日".equals(periodtype)) {
			period = period + " 00:00:00";
		}
		return period;
	}

	/**
	 * 取期末日期
	 */
	public String getQmDate(IufoQuerySIMVO simvo) throws BusinessException {
		String period = simvo.getPeriod();// "2015-02-01"
		if (period == null || "".equals(period))
			throw new BusinessException("参数 '期间' 为空！，请设置！  ");
		String periodtype = simvo.getPeriodType();// { "年", "月", "日" };
		String pix_y = period.substring(0, 4);
		String pix_m = period.substring(0, 7);
		if ("年".equals(periodtype)) {
			period = pix_y + "-12-31 23:59:59";
		} else if ("月".equals(periodtype)) {// 即使是02-31，字符型没有问题。
			period = pix_m + "-31 23:59:59";
		} else if ("日".equals(periodtype)) {
			period = period + " 23:59:59";
		}
		return period;
	}
}
