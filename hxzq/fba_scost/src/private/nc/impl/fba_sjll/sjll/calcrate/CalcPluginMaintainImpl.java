package nc.impl.fba_sjll.sjll.calcrate;

import nc.bs.fba_sjll.sjll.actualrate.pub.QueryPubInfo;
import nc.impl.sim.costingplugin.realrate.BulidCalcRateTradeInfo;
import nc.impl.sim.costingplugin.realrate.CalcRealRate;
import nc.impl.sim.costingplugin.realrate.CalcRealRate1;
import nc.impl.sim.costingplugin.realrate.CalcRealRate2;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.fba_sjll.sjll.actualrate.pub.ActualrateConst;
import nc.vo.fba_sjll.sjll.actualrate.pub.utils.ActualrateTools;
import nc.vo.fba_sjll.sjll.calcrealrate.ActualRateBillInfo;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.trade.voutils.SafeCompute;

public class CalcPluginMaintainImpl implements ICalcPluginMaintain {

	// 买入插件处理
	@Override
	public void buyPlugindeal(SuperVO tradevo1) throws BusinessException {
		StocktradeVO tradevo = (StocktradeVO) tradevo1;
		BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
		boolean falg = real.isRealTradevo(tradevo);
		if (falg) {
			// 计算买入利息调整
			UFDouble lxtz = ActualrateTools.getBuyInterestAdjust(tradevo);
			tradevo.setInterestadjust(SafeCompute.sub(lxtz,
					tradevo.getTaxexpense()));
			// 回写实际利率单据信息
			ActualRateBillInfo billvo = real.bulidStockTradeInfo(tradevo);
			real.save(billvo);
		}
	}

	// 取消买入插件处理

	@Override
	public void unBuyPlugindeal(SuperVO tradevo1, String pk_group,
			String pk_org, String tradedate) throws BusinessException {
		if (tradevo1 != null) {
			StocktradeVO tradevo = (StocktradeVO) tradevo1;
			// 买入利息调整清空
			tradevo.setInterestadjust(null);
			tradevo.setInterest_sell(null);
			tradevo.setRealinterest_sell(null);
			tradevo.setInterestadjust_sell(null);
			tradevo.setInterestdate(null);
			tradevo.setRealinterest(null);
			tradevo.setRealrate(null);
			// 实际利率处理单据弃审
			BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
			boolean falg = real.isRealTradevo(tradevo);
			if (falg) {
				real.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// 卖出插件处理
	@Override
	public void sellPlugindeal(StockBalanceVO stockbalancevo, SuperVO tradevo1)
			throws BusinessException {
		// 实际利率处理单据
		StocktradeVO stocktrade = (StocktradeVO) tradevo1;
		BulidCalcRateTradeInfo real1 = new BulidCalcRateTradeInfo();
		boolean falg1 = real1.isRealTradevo(stocktrade);
		if (falg1) {
			CalcRealRate real = new CalcRealRate();
			real.calcRealRateInfo(stockbalancevo, stocktrade);
			//
			ActualRateBillInfo billvo = real1.bulidStockTradeInfo(stocktrade);
			real1.save(billvo);
		}
	}

	// 取消卖出插件处理

	@Override
	public void unSellPlugindeal(SuperVO tradevo1, String pk_group,
			String pk_org, String tradedate) throws BusinessException {
		if (tradevo1 != null) {
			StocktradeVO tradevo = (StocktradeVO) tradevo1;
			// 买入利息调整清空
			tradevo.setInterestadjust(null);
			tradevo.setInterest_sell(null);
			tradevo.setRealinterest_sell(null);
			tradevo.setInterestadjust_sell(null);
			tradevo.setInterestdate(null);
			tradevo.setRealinterest(null);
			tradevo.setRealrate(null);
			// 实际利率处理单据
			BulidCalcRateTradeInfo real1 = new BulidCalcRateTradeInfo();
			boolean falg1 = real1.isRealTradevo(tradevo);
			if (falg1) {
				// 实际利率处理单据弃审
				real1.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// 付息
	@Override
	public void fxPlugindeal(SuperVO tradevo1) throws BusinessException {
		// 实际利率处理单据
		StocktradeVO stocktrade = (StocktradeVO) tradevo1;
		BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
		boolean falg = real.isRealTradevo(stocktrade);
		if (falg) {
			CalcRealRate2 real2 = new CalcRealRate2();
			real2.calcRealRateInfo(stocktrade);
			ActualRateBillInfo billvo = real.bulidStockTradeInfo(stocktrade);
			real.save(billvo);
		}
	}

	// 取消付息
	@Override
	public void unFxPlugindeal(SuperVO tradevo1, String pk_group,
			String pk_org, String tradedate) throws BusinessException {
		if (tradevo1 != null) {
			StocktradeVO tradevo = (StocktradeVO) tradevo1;
			tradevo.setInterestadjust(null);
			tradevo.setInterest_sell(null);
			tradevo.setRealinterest_sell(null);
			tradevo.setInterestadjust_sell(null);
			tradevo.setInterestdate(null);
			tradevo.setRealinterest(null);
			tradevo.setRealrate(null);
			// 实际利率处理单据弃审
			BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
			boolean falg = real.isRealTradevo(tradevo);
			if (falg) {
				// 实际利率处理单据弃审
				real.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// 转托管

	@Override
	public void ztgPlugindeal(StockBalanceVO stockbalancevo, SuperVO tradevo1)
			throws BusinessException {
		// 实际利率处理单据
		TransformtradeVO tradevo = (TransformtradeVO) tradevo1;
		BulidCalcRateTradeInfo real1 = new BulidCalcRateTradeInfo();
		boolean falg1 = real1.isRealTradevo(tradevo);
		if (falg1) {
			CalcRealRate1 real = new CalcRealRate1();
			real.calcRealRateInfo(stockbalancevo, tradevo);
			//
			ActualRateBillInfo billvo1 = real1.bulidTransfromInfo(tradevo,
					ActualrateConst.TOUT);
			ActualRateBillInfo billvo2 = real1.bulidTransfromInfo(tradevo,
					ActualrateConst.TIN);
			ActualRateBillInfo[] vos = new ActualRateBillInfo[] { billvo1,
					billvo2 };
			real1.save(vos);
		}
	}

	// 取消转托管
	@Override
	public void unZtgPlugindeal(SuperVO tradevo1, String pk_group,
			String pk_org, String tradedate) throws BusinessException {
		if (tradevo1 != null) {
			TransformtradeVO tradevo = (TransformtradeVO) tradevo1;
			tradevo.setInterest_sell(null);
			tradevo.setRealinterest_sell(null);
			tradevo.setInterestadjust_sell(null);
			tradevo.setInterestdate(null);
			tradevo.setRealinterest(null);
			tradevo.setRealrate(null);
			// 实际利率处理单据弃审
			BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
			boolean falg = real.isRealTradevo(tradevo);
			if (falg) {
				// 实际利率处理单据弃审
				real.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// date 为取消审核日期
	@Override
	public void unAuditcheck(String pk_group, String pk_org, UFLiteralDate date)
			throws BusinessException {
		// 最近计算日期
		QueryPubInfo info = new QueryPubInfo();
		String calcdate = info.getLastCalcDate(pk_group, pk_org);
		if (!new UFLiteralDate(calcdate).before(date)) {
			throw new BusinessException("当前组织启用实际利率，实际利率计算日期为: [" + calcdate
					+ "]，请先取消实际利率计算！");
		}
	}

	// 公允摊余成本

	@Override
	public void setFairvalueByRate(UFLiteralDate tradedate,
			StockBalanceVO sbvo, String pk_group, String pk_org)
			throws BusinessException {
		BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
		boolean falg = real.isRealTradevo(sbvo);
		if (falg) {
			FairValueRate rate = new FairValueRate();
			UFDouble zy = rate.getTodayCost(tradedate, sbvo, pk_group, pk_org);
			sbvo.setStocks_sum(zy);
		}
	}

	// 公允校验
	@Override
	public void fairCheck(String pk_group, String pk_org, String distill_date)
			throws BusinessException {
		// 是否启用实际利率
		QueryPubInfo info = new QueryPubInfo();
		String pk_orgbook = AppContextUtil.getDefaultOrgBook();
		String value = info.queryParavalue(pk_orgbook,
				ActualrateConst.PARAM_REAL);
		if ("Y".equals(value)) {
			FairValueRate rate = new FairValueRate();
			rate.check(pk_group, pk_org, distill_date);
		}
	}
}