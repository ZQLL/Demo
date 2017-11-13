package nc.bs.fba_scost.cost.interest.pub;

import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

/**
 * Ӧ����Ϣ������
 *
 */
public class InterestTools {
	/**
	 * trade_date���ڰ�����List<Rateperiod>��Ϣ�꿪ʼ���ڡ��������ڷ�Χ�ڵ���Ϣ
	 */
	public static Rateperiod getContainsDate(Rateperiod[] dates,UFDate trade_date){
		if(trade_date == null || dates == null || dates.length == 0)
			return null;
		Rateperiod vo = null;
		for(int i = 0 ;i < dates.length; i++){
			UFDate start = dates[i].getStart_day();
			UFDate end = dates[i].getEnd_day();
			if((trade_date.isSameDate(start) || trade_date.after(start.asBegin())) &&  
					(trade_date.before(end.asEnd()) || trade_date.isSameDate(end))){
				vo = dates[i];
				break;
			}
		}
		return vo;
	}

	/**
	 * ��ȡʣ��ı���ı������������ڡ�<b>���ﷵ�ص���С��</b>
	 * 
	 * @param dates �������õ�������Ϣ
	 * @param targetVO ����Ӧ��ʹ�õ�������Ϣ
	 * @return ʣ��ı���ı�������������
	 * @author jingqt
	 * @date 2016-4-22 ����3:13:36
	 */
	public static UFDouble getCostSurplus(Rateperiod[] dates, Rateperiod targetVO) {
		// ���ߵ����������˵���ϸ�������getContainsDate���϶��Ѿ��߹��ˣ���ȡ���˷���ֵ����dates������null��
		if (targetVO == null) {
			return UFDouble.ZERO_DBL;
		}
		UFDouble result = UFDouble.ZERO_DBL;
		// �ҵ����еı�Ŀ�����ʿ�ʼ����С����ǰ��������������
		for (Rateperiod tempVO : dates) {
			// �����������Ϣ�Ŀ�ʼ����С��Ŀ��Ŀ�ʼ���ڣ���ȡ������ǰ��������������
			if (tempVO.getStart_day().before(targetVO.getStart_day())) {
				result = PubMethod.getInstance().add(tempVO.getPaypercent(), result);
			}
		}
		// ���û�л��������򷵻�1��
		if (result.intValue() == 0) {
			return UFDouble.ONE_DBL;
		}
		// �������ٷֱȵ�ֵ��
		return UFDouble.ONE_DBL.sub(PubMethod.getInstance().div(result, new UFDouble(100.0d)));
	}

	public static boolean isFx(Rateperiod[] dates,UFDate trade_date){
		if(trade_date == null || dates == null || dates.length == 0)
			return false;
		boolean falg = false;
		for(int i = 0 ;i < dates.length; i++){
			UFDate end = dates[i].getEnd_day();
			if(trade_date.isSameDate(end)){
				falg = true;
				break;
			}
		}
		return falg;
	}
	
	public static boolean isHbFx(Rateperiod[] dates,UFDate trade_date){
		if(trade_date == null || dates == null || dates.length == 0)
			return false;
		boolean falg = false;
		UFDate end = dates[dates.length -1].getEnd_day();
		if(trade_date.isSameDate(end)){
			falg = true;
		}
		return falg;
	}

	/**
	 * ����ÿ����Ϣ<br>
	 * <b>2016��4��25�� ���----����ÿ����Ϣǰ��Ӧ�ý���֮ǰ�Ѿ������Ĳ��ֳ�ȥ��</b>
	 * Ӱ�췶Χ��{@link CalcBuySellInterest}�м�����Ϣ�ķ�ʽ��
	 * 
	 * @see CalcBuySellInterest
	 * @update JINGQT
	 * @date 2016��4��25�� 09:32:38
	 */
	public static UFDouble calcLxByTerm(UFDouble bargain_num,Interest headvo,Rateperiod[] ratevos,Rateperiod vo){
		if(headvo == null || ratevos == null || ratevos.length == 0 || vo == null)
			return new UFDouble(0);
		UFDouble lx = null;
		if(CostConstant.PAYONCEATEND.equals(headvo.getPaytype())){//�Զ���--���ճ���������Ӧ���� ����һ�λ�����Ϣ
			for(Rateperiod vo1 : ratevos){
				UFDouble lx1 = SafeCompute.multiply(bargain_num, vo1.getYear_rate());
				UFDouble days = getRealDays(headvo, vo.getStart_day(), vo.getEnd_day());
				lx = SafeCompute.add(lx, lx1.multiply(vo.getDays_num()).div(days));
			}
		}else{
			/** 2016��4��25�� JINGQT �����Ϣ��Ҫ����ǰ�����������ǽ�ȥ ADD START */
			UFDouble hasSurplusPercent = InterestTools.getCostSurplus(ratevos, vo);
			/** 2016��4��25�� JINGQT �����Ϣ��Ҫ����ǰ�����������ǽ�ȥ ADD END */
			lx = SafeCompute.multiply(bargain_num, vo.getYear_rate()).multiply(hasSurplusPercent);
			if(CostConstant.PAYPREYEAR.equals(headvo.getPeriodtype())
					|| CostConstant.SCP.equals(headvo.getPeriodtype())){//���긶Ϣ��������  
				lx = SafeCompute.div(lx, new UFDouble(1));
			}else if(CostConstant.PAYPREHALFYEAR.equals(headvo.getPeriodtype())){//�����긶Ϣ
				lx = SafeCompute.div(lx, new UFDouble(2));
			}else if(CostConstant.PAYPREQUARTER.equals(headvo.getPeriodtype())){//������Ϣ
				lx = SafeCompute.div(lx, new UFDouble(4));
			}else if(CostConstant.PAYPREMONTH.equals(headvo.getPeriodtype())){//���¸�Ϣ
				lx = SafeCompute.div(lx, new UFDouble(12));
			}
		}
		return lx;
	}


	/**
	 * ��ȡ�����ʶ�Ӧ��ʵ��������
	 * 
	 * @param headvo �����ʱ�ͷ
	 * @param startDay ��ʼ����
	 * @param endDay ��������
	 * @return ʵ������
	 * @author ��ѩ update by jingqt
	 * @since 2016-5-5 ����8:59:21
	 */
	protected static UFDouble getRealDays(Interest headvo, UFDate startDay, UFDate endDay) {
		UFDouble days = UFDouble.ZERO_DBL;
		if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {// �Զ���--���ճ���������Ӧ���� ����һ�λ�����Ϣ
			for (int i = startDay.getYear() + 1; i < endDay.getYear(); i++) {
				if (UFDate.isLeapYear(i)) {
					days = new UFDouble(366);
					return days;
				}
			}
			// ��ʼ���������겢�Ұ���2��
			if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {
				days = new UFDouble(366);
				// �������������겢�Ұ���2��
			} else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2) {
				days = new UFDouble(366);
			} else {
				days = new UFDouble(365);
			}
		} else {// ���ڸ�Ϣ
			if (startDay.getYear() == endDay.getYear()) {// ������
				if (UFDate.isLeapYear(startDay.getYear()))
					days = new UFDouble(366);
				else
					days = new UFDouble(365);
			} else {// ����
				if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {// ��ʼ���������겢�Ұ���2��
					days = new UFDouble(366);
				} else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2) {// �������������겢�Ұ���2��
					days = new UFDouble(366);
				} else {
					days = new UFDouble(365);
				}
			}
		}
		return days;
	}

	/**
	 * ��Ȩƽ������
	 * �������num1/(num1-num2) = mny1 / ?   ? = (num1-num2) * mny1 / num1
	 * scale ����������λ��
	 * 
	 */
	public static UFDouble averageMny(UFDouble num1,UFDouble num2,UFDouble mny1,int scale){
		UFDouble num = SafeCompute.sub(num1, num2);
		UFDouble sum = SafeCompute.multiply(mny1, num);
		UFDouble mny2 = SafeCompute.div(sum, num1);
		mny2 = mny2.setScale(scale, UFDouble.ROUND_HALF_UP);
		return mny2;
	}
	/**
	 * ��Ȩƽ������
	 * �������num1/num2 = mny1 / ?  //\\   ? = num2 * mny1 / num1
	 * scale ����������λ��
	 * 
	 */
	public static UFDouble averageMny1(UFDouble num1,UFDouble num2,UFDouble mny1,int scale){
		UFDouble sum = SafeCompute.multiply(mny1, num2);
		UFDouble mny2 = SafeCompute.div(sum, num1);
		mny2 = mny2.setScale(scale, UFDouble.ROUND_HALF_UP);
		return mny2;
	}
	
}
