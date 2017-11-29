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

	// ����������
	@Override
	public void buyPlugindeal(SuperVO tradevo1) throws BusinessException {
		StocktradeVO tradevo = (StocktradeVO) tradevo1;
		BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
		boolean falg = real.isRealTradevo(tradevo);
		if (falg) {
			// ����������Ϣ����
			UFDouble lxtz = ActualrateTools.getBuyInterestAdjust(tradevo);
			tradevo.setInterestadjust(SafeCompute.sub(lxtz,
					tradevo.getTaxexpense()));
			// ��дʵ�����ʵ�����Ϣ
			ActualRateBillInfo billvo = real.bulidStockTradeInfo(tradevo);
			real.save(billvo);
		}
	}

	// ȡ������������

	@Override
	public void unBuyPlugindeal(SuperVO tradevo1, String pk_group,
			String pk_org, String tradedate) throws BusinessException {
		if (tradevo1 != null) {
			StocktradeVO tradevo = (StocktradeVO) tradevo1;
			// ������Ϣ�������
			tradevo.setInterestadjust(null);
			tradevo.setInterest_sell(null);
			tradevo.setRealinterest_sell(null);
			tradevo.setInterestadjust_sell(null);
			tradevo.setInterestdate(null);
			tradevo.setRealinterest(null);
			tradevo.setRealrate(null);
			// ʵ�����ʴ���������
			BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
			boolean falg = real.isRealTradevo(tradevo);
			if (falg) {
				real.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// �����������
	@Override
	public void sellPlugindeal(StockBalanceVO stockbalancevo, SuperVO tradevo1)
			throws BusinessException {
		// ʵ�����ʴ�����
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

	// ȡ�������������

	@Override
	public void unSellPlugindeal(SuperVO tradevo1, String pk_group,
			String pk_org, String tradedate) throws BusinessException {
		if (tradevo1 != null) {
			StocktradeVO tradevo = (StocktradeVO) tradevo1;
			// ������Ϣ�������
			tradevo.setInterestadjust(null);
			tradevo.setInterest_sell(null);
			tradevo.setRealinterest_sell(null);
			tradevo.setInterestadjust_sell(null);
			tradevo.setInterestdate(null);
			tradevo.setRealinterest(null);
			tradevo.setRealrate(null);
			// ʵ�����ʴ�����
			BulidCalcRateTradeInfo real1 = new BulidCalcRateTradeInfo();
			boolean falg1 = real1.isRealTradevo(tradevo);
			if (falg1) {
				// ʵ�����ʴ���������
				real1.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// ��Ϣ
	@Override
	public void fxPlugindeal(SuperVO tradevo1) throws BusinessException {
		// ʵ�����ʴ�����
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

	// ȡ����Ϣ
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
			// ʵ�����ʴ���������
			BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
			boolean falg = real.isRealTradevo(tradevo);
			if (falg) {
				// ʵ�����ʴ���������
				real.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// ת�й�

	@Override
	public void ztgPlugindeal(StockBalanceVO stockbalancevo, SuperVO tradevo1)
			throws BusinessException {
		// ʵ�����ʴ�����
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

	// ȡ��ת�й�
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
			// ʵ�����ʴ���������
			BulidCalcRateTradeInfo real = new BulidCalcRateTradeInfo();
			boolean falg = real.isRealTradevo(tradevo);
			if (falg) {
				// ʵ�����ʴ���������
				real.delete(pk_group, pk_org, tradedate);
			}
		}
	}

	// date Ϊȡ���������
	@Override
	public void unAuditcheck(String pk_group, String pk_org, UFLiteralDate date)
			throws BusinessException {
		// �����������
		QueryPubInfo info = new QueryPubInfo();
		String calcdate = info.getLastCalcDate(pk_group, pk_org);
		if (!new UFLiteralDate(calcdate).before(date)) {
			throw new BusinessException("��ǰ��֯����ʵ�����ʣ�ʵ�����ʼ�������Ϊ: [" + calcdate
					+ "]������ȡ��ʵ�����ʼ��㣡");
		}
	}

	// ����̯��ɱ�

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

	// ����У��
	@Override
	public void fairCheck(String pk_group, String pk_org, String distill_date)
			throws BusinessException {
		// �Ƿ�����ʵ������
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