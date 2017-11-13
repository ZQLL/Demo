package nc.bs.fba_scost.cost.interest.pub;

import nc.bs.logging.Logger;
import nc.vo.fba_scost.cost.interest.AggInterest;
import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.trade.voutils.SafeCompute;

/**
 * ���㵥������Ӧ����Ϣ
 * 
 */
public class CalcBuySellInterestold {

	private static final String FXZQ = "����Ϣ����";

	private String paystyle;

	/**
	 * ����[������]���������Ӧ����Ϣ---------������˼�����
	 */
	public UFDouble calcInterestNotTradedate(UFDouble bargain_num,
			String pk_securities, UFDate trade_date) throws BusinessException {
		if (bargain_num == null || pk_securities == null || trade_date == null)
			return null;
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		boolean falg = info.isSecuritiesAcc(pk_securities);
		if (!falg)
			return null;
		AggInterest agginfo = info.queryInterestsetInfo(pk_securities);
		Interest headvo = agginfo.getParent();
		Rateperiod[] ratevos = (Rateperiod[]) agginfo.getChildrenVO();
		Rateperiod vo = InterestTools.getContainsDate(ratevos, trade_date);
		if (vo == null) {
			throw new BusinessException("֤ȯ������"
					+ info.querySecuritesName(pk_securities) + " �Ҳ����������ñ���");
		}
		UFDouble days = new UFDouble(vo.getDays_num());
		UFDouble day = new UFDouble(trade_date.asBegin().getDaysAfter(
				vo.getStart_day().asBegin()));
		// UFDouble percent = SafeCompute.div(day, days);
		UFDouble termlx = InterestTools.calcLxByTerm(bargain_num, headvo,
				ratevos, vo);
		// UFDouble lx = SafeCompute.multiply(termlx, percent);
		UFDouble lx = SafeCompute.multiply(termlx, day).div(days);
		lx = lx.setScale(2, UFDouble.ROUND_HALF_UP);
		return lx;
	}

	/**
	 * ����[����]���������Ӧ����Ϣ------------Ӧ����Ϣ������
	 */
	public UFDouble calcInterestTradedate(UFDouble bargain_num,
			String pk_securities, UFDate trade_date, String pk_org)
			throws BusinessException {
		if (bargain_num == null || pk_securities == null || trade_date == null)
			return null;
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		boolean falg = info.isSecuritiesAcc(pk_securities);
		if (!falg)
			return null;
		AggInterest agginfo = info.queryInterestsetInfo(pk_securities);
		Interest headvo = agginfo.getParent();
		Rateperiod[] ratevos = (Rateperiod[]) agginfo.getChildrenVO();
		Rateperiod vo = InterestTools.getContainsDate(ratevos, trade_date);
		UFDouble day = new UFDouble(trade_date.asBegin().getDaysAfter(
				vo.getStart_day().asBegin()) + 1);

		UFDouble days = UFDouble.ZERO_DBL;
		UFDouble termlx = UFDouble.ZERO_DBL;
		UFDouble lx = UFDouble.ZERO_DBL;
		// TODO fulq begin
		UFDouble percentAccount = UFDouble.ZERO_DBL;
		for (Rateperiod ratevo : ratevos) {
			if (ratevo.getPk_rateperiod().equalsIgnoreCase(
					vo.getPk_rateperiod()))
				break;
			if (ratevo.getPaypercent() != null)
				percentAccount = percentAccount.add(ratevo.getPaypercent());

		}
		bargain_num = bargain_num.multiply(UFDouble.ONE_DBL.sub(percentAccount
				.div(100)));
		// end
		if (getPaystyle(pk_org).equals(FXZQ)) {// ����Ϣ���ڼ��㣨����Ϣ*��������/����ϢƵ��*����������������
			days = new UFDouble(vo.getDays_num());
			termlx = InterestTools.calcLxByTerm(bargain_num, headvo, ratevos,
					vo);
		} else {// ��ʵ������(��������/��ǰ������)
			// fulq Ӧ��ȡ �����ϵ�����
			// days = getDaysOfCurYear();
			days = new UFDouble(vo.getDays_num().intValue());

			termlx = SafeCompute.multiply(bargain_num, vo.getYear_rate());
		}
		lx = SafeCompute.div(SafeCompute.multiply(termlx, day), days);
		lx = lx.setScale(2, UFDouble.ROUND_HALF_UP);
		return lx;
	}

	private String getPaystyle(String pk_org) {

		if (paystyle == null) {
			try {
				paystyle = SysInitCache.getInstance().getSysInitValue(pk_org,
						SystemConst.SYSINIT_PAYSTYLE);
			} catch (Exception e) {
				paystyle = FXZQ;
				Logger.error("��ѯSEC101ʧ�ܣ�");
				Logger.error(e.getMessage());
			}

		}
		return paystyle;
	}

	/**
	 * ��ȡһ���ж����죨���꣩
	 * 
	 * @return
	 */
	private UFDouble getDaysOfCurYear() {

		UFDouble percent = new UFDouble(365);

		if (isLeapYear()) {
			percent = new UFDouble(366);
		}

		return percent;

	}

	/**
	 * �ж��ǲ�������
	 * 
	 * @return
	 */
	private boolean isLeapYear() {

		UFDate curDate = new UFDate();

		if (curDate.getYear() % 400 == 0) {
			return true;
		}
		return false;

	}
}
