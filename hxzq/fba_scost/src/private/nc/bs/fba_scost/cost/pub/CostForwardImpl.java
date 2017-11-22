package nc.bs.fba_scost.cost.pub;

import nc.bs.fba_scost.cost.fairvaluedistill.bp.FairValueForward;
import nc.bs.fba_scost.cost.fairvaluedistill.bp.FairValueSaveClear;
import nc.bs.fba_scost.cost.interest.pub.CalcBuySellInterest;
import nc.bs.fba_scost.cost.interest.pub.InterestForward;
import nc.bs.fba_scost.cost.interest.pub.InterestSaveClear;
import nc.bs.fba_scost.cost.interest.pub.QueryInterestBaseInfo;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.tool.ICostForward;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public class CostForwardImpl implements ICostForward {

	private QueryInterestBaseInfo queryInfo = null;

	
	@Override
	public UFDouble calcInterest(ICostingTool costingtool, ITrade_Data tradevo)
			throws BusinessException {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean iscalc = getQueryInfo().getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_CALCBUYSELLINTEREST);
		boolean iscover = getQueryInfo().getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_ISCONVERINTEREST);
		UFDouble lx = calcInterest(iscalc, iscover, tradevo);
		return lx;
	}

	public UFDouble calcInterestTrade(ITrade_Data tradevo)
			throws BusinessException {
		CalcBuySellInterest calc = new CalcBuySellInterest();
		UFDouble lx = calc.calcInterestNotTradedate(tradevo.getBargain_num(),
				tradevo.getPk_securities(), tradevo.getTrade_date());
		return lx;
	}

	public UFDouble calcInterest(boolean iscalc, boolean iscover,
			ITrade_Data tradevo) throws BusinessException {
		UFDouble lx = null;
		if (iscalc) {// 计算
			if (iscover || // 覆盖
					(!iscover && (tradevo.getAccrual_sum() == null // 不覆盖
					|| tradevo.getAccrual_sum().doubleValue() == 0))) {
				lx = calcInterestTrade(tradevo);
			} else {
				lx = tradevo.getAccrual_sum();
			}
		} else {
			lx = tradevo.getAccrual_sum();
		}
		return lx;
	}

	
	@Override
	public UFDouble forwardInterestDistill(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, ITrade_Data vo)
			throws BusinessException {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean falg = getQueryInfo().getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_INTERESTFORWARD);
		UFDouble lx = null;
		if (falg) {
			InterestForward forward = new InterestForward();
			lx = forward
					.forwardInterestDistill(costingtool, stockbalancevo, vo);
		}
		return lx;
	}

	@Override
	
	public void saveInterestDistill(ICostingTool costingtool, ITrade_Data vo)
			throws BusinessException {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean falg = getQueryInfo().getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_INTERESTFORWARD);
		if (falg) {
			InterestSaveClear save = new InterestSaveClear();
			save.saveInterestDistill(costingtool, vo);
		}
	}

	@Override
	
	public void clearDistillInterest(ICostingTool costingtool)
			throws BusinessException {
		InterestSaveClear save = new InterestSaveClear();
		save.clearDistillInterest(costingtool);
	}

	
	@Override
	public UFDouble forwardFairValueDistill(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, ITrade_Data vo)
			throws BusinessException {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean falg = getQueryInfo().getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_FAIRFORWARD);
		UFDouble fx = null;
		if (falg) {
			FairValueForward fv = new FairValueForward();
			fx = fv.forwardFairValueDistill(costingtool, stockbalancevo, vo);
		}
		return fx;
	}

	
	@Override
	public void saveFairValueDistill(ICostingTool costingtool, ITrade_Data vo)
			throws BusinessException {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean falg = getQueryInfo().getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_FAIRFORWARD);
		if (falg) {
			FairValueSaveClear fvz = new FairValueSaveClear();
			fvz.saveFairValueDistill(costingtool, vo);
		}
	}

	
	@Override
	public void clearDistillFv(ICostingTool costingtool)
			throws BusinessException {
		FairValueSaveClear fv = new FairValueSaveClear();
		fv.clearDistillFv(costingtool);
	}

	public QueryInterestBaseInfo getQueryInfo() {
		if (queryInfo == null) {
			queryInfo = new QueryInterestBaseInfo();
		}
		return queryInfo;
	}

	@Override
	
	public UFDouble forwardFairValueDistill_debt(ICostingTool costingtool,
			ZqjdTallyVO zqjdtallyvo, ITrade_Data vo) throws BusinessException {
		// TODO 自动生成的方法存根
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean falg = getQueryInfo().getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_FAIRFORWARD);
		UFDouble fx = null;
		if (falg) {
			FairValueForward fv = new FairValueForward();
			fx = fv.forwardFairValueDistill_debt(costingtool, zqjdtallyvo, vo);
		}
		return fx;
	}
}
