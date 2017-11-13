package nc.bs.fba_scost.cost.interest.pub;

import nc.bs.logging.Logger;
import nc.vo.fba_scost.cost.interest.AggInterest;
import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

public class CalcBuySellInterest {
	private static final String FXZQ = "按付息周期";
	private String paystyle;

	public UFDouble calcInterestNotTradedate(UFDouble bargain_num,
			String pk_securities, UFDate trade_date) throws BusinessException {
		if ((bargain_num == null) || (pk_securities == null)
				|| (trade_date == null)) {
			return null;
		}
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		boolean falg = info.isSecuritiesAcc(pk_securities);
		if (!falg) {
			return null;
		}
		AggInterest agginfo = info.queryInterestsetInfo(pk_securities);
		Interest headvo = agginfo.getParent();
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		Rateperiod vo = InterestTools.getContainsDate(ratevos, trade_date);
		if (vo == null) {
			throw new BusinessException("证券档案："
					+ info.querySecuritesName(pk_securities) + " 找不到利率设置表体");
		}
		UFDouble days = new UFDouble(vo.getDays_num().intValue());
		UFDouble day = new UFDouble(trade_date.asBegin().getDaysAfter(
				vo.getStart_day().asBegin()));

		UFDouble termlx = InterestTools.calcLxByTerm(bargain_num, headvo,
				ratevos, vo);

		UFDouble lx = SafeCompute.multiply(termlx, day).div(days);
		lx = lx.setScale(2, 4);
		return lx;
	}
	
	/*修改 by zq
	 * 出现未知错误---日期为0，被除数为0的情况
	 * */
	public UFDouble calcInterestTradedate(UFDouble bargain_num,
			String pk_securities, UFDate trade_date, String pk_org)
			throws BusinessException {
		if ((bargain_num == null) || (pk_securities == null)
				|| (trade_date == null)) {
			return null;
		}
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();

		boolean falg = info.isSecuritiesAcc(pk_securities);
		if (!falg) {
			return null;
		}
		AggInterest agginfo = info.queryInterestsetInfo(pk_securities);
		Interest headvo = agginfo.getParent();
		Rateperiod[] ratevos = agginfo.getChildrenVO();
		Rateperiod vo = InterestTools.getContainsDate(ratevos, trade_date);
		if(vo == null){
			return new UFDouble(365);
		}
		else{
		UFDouble day = new UFDouble(trade_date.asBegin().getDaysAfter(
				vo.getStart_day().asBegin()) + 1);

		UFDouble days = UFDouble.ZERO_DBL;
		UFDouble termlx = UFDouble.ZERO_DBL;
		UFDouble lx = UFDouble.ZERO_DBL;

		UFDate startDay = vo.getStart_day();
		UFDate endDay = vo.getEnd_day();

		UFDouble percentAccount = UFDouble.ZERO_DBL;
		for (Rateperiod ratevo : ratevos) {
			if (ratevo.getPk_rateperiod().equalsIgnoreCase(
					vo.getPk_rateperiod())) {
				break;
			}
			if (ratevo.getPaypercent() != null) {
				percentAccount = percentAccount.add(ratevo.getPaypercent());
			}
		}
		bargain_num = bargain_num.multiply(UFDouble.ONE_DBL.sub(percentAccount
				.div(100.0D)));
		if ("02".equals(headvo.getPaytype())) {
			for (int i = startDay.getYear(); i <= endDay.getYear(); i++) {
				if (UFDate.isLeapYear(i)) {
					days = new UFDouble(366);
				} else {
					days = new UFDouble(365);
				}
			}
		} else if (startDay.getYear() == endDay.getYear()) {
			if (UFDate.isLeapYear(startDay.getYear())) {
				days = new UFDouble(366);
			} else {
				days = new UFDouble(365);
			}
		} else if ((UFDate.isLeapYear(startDay.getYear()))
				&& (startDay.getMonth() < 3)) {
			days = new UFDouble(366);
		} else if ((UFDate.isLeapYear(endDay.getYear()))
				&& (endDay.getMonth() > 2)) {
			days = new UFDouble(366);
		} else {
			days = new UFDouble(365);
		}
		if (getPaystyle(pk_org).equals("按付息周期")) {
			termlx = InterestTools.calcLxByTerm(bargain_num, headvo, ratevos,
					vo);
		} else {
			termlx = SafeCompute.multiply(bargain_num, vo.getYear_rate());
		}
		lx = SafeCompute.div(SafeCompute.multiply(termlx, day), days);
		lx = lx.setScale(2, 4);
		return lx;
		}
	}

	private String getPaystyle(String pk_org) {
		if (this.paystyle == null) {
			try {
				this.paystyle = SysInitCache.getInstance().getSysInitValue(
						pk_org, "SEC101");
			} catch (Exception e) {
				this.paystyle = "按付息周期";
				Logger.error("查询SEC101失败！");
				Logger.error(e.getMessage());
			}
		}
		return this.paystyle;
	}
}
