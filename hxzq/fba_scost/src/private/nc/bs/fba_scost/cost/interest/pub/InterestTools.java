package nc.bs.fba_scost.cost.interest.pub;

import nc.vo.fba_scost.cost.interest.Interest;
import nc.vo.fba_scost.cost.interest.Rateperiod;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 应收利息工具类
 *
 */
public class InterestTools {
	/**
	 * trade_date日期包括在List<Rateperiod>计息年开始日期、结束日期范围内的信息
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
	 * 获取剩余的本金的比例，包括本期。<b>这里返回的是小数</b>
	 * 
	 * @param dates 利率设置的所有信息
	 * @param targetVO 本期应该使用的利率信息
	 * @return 剩余的本金的比例，包括本期
	 * @author jingqt
	 * @date 2016-4-22 下午3:13:36
	 */
	public static UFDouble getCostSurplus(Rateperiod[] dates, Rateperiod targetVO) {
		// 能走到这个方法，说明上个方法（getContainsDate）肯定已经走过了，并取得了返回值，故dates不会是null了
		if (targetVO == null) {
			return UFDouble.ZERO_DBL;
		}
		UFDouble result = UFDouble.ZERO_DBL;
		// 找到所有的比目标利率开始日期小的提前还款比例，并相加
		for (Rateperiod tempVO : dates) {
			// 如果此利率信息的开始日期小于目标的开始日期，则取得其提前还款比例，并相加
			if (tempVO.getStart_day().before(targetVO.getStart_day())) {
				result = PubMethod.getInstance().add(tempVO.getPaypercent(), result);
			}
		}
		// 如果没有还过本，则返回1。
		if (result.intValue() == 0) {
			return UFDouble.ONE_DBL;
		}
		// 这个计算百分比的值。
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
	 * 计算每期利息<br>
	 * <b>2016年4月25日 添加----计算每期利息前，应该将其之前已经还本的部分除去。</b>
	 * 影响范围：{@link CalcBuySellInterest}中计算利息的方式。
	 * 
	 * @see CalcBuySellInterest
	 * @update JINGQT
	 * @date 2016年4月25日 09:32:38
	 */
	public static UFDouble calcLxByTerm(UFDouble bargain_num,Interest headvo,Rateperiod[] ratevos,Rateperiod vo){
		if(headvo == null || ratevos == null || ratevos.length == 0 || vo == null)
			return new UFDouble(0);
		UFDouble lx = null;
		if(CostConstant.PAYONCEATEND.equals(headvo.getPaytype())){//自定义--按照程序处理，这里应该是 到期一次还本付息
			for(Rateperiod vo1 : ratevos){
				UFDouble lx1 = SafeCompute.multiply(bargain_num, vo1.getYear_rate());
				UFDouble days = getRealDays(headvo, vo.getStart_day(), vo.getEnd_day());
				lx = SafeCompute.add(lx, lx1.multiply(vo.getDays_num()).div(days));
			}
		}else{
			/** 2016年4月25日 JINGQT 计算计息需要将提前还本比例考虑进去 ADD START */
			UFDouble hasSurplusPercent = InterestTools.getCostSurplus(ratevos, vo);
			/** 2016年4月25日 JINGQT 计算计息需要将提前还本比例考虑进去 ADD END */
			lx = SafeCompute.multiply(bargain_num, vo.getYear_rate()).multiply(hasSurplusPercent);
			if(CostConstant.PAYPREYEAR.equals(headvo.getPeriodtype())
					|| CostConstant.SCP.equals(headvo.getPeriodtype())){//按年付息、超短融  
				lx = SafeCompute.div(lx, new UFDouble(1));
			}else if(CostConstant.PAYPREHALFYEAR.equals(headvo.getPeriodtype())){//按半年付息
				lx = SafeCompute.div(lx, new UFDouble(2));
			}else if(CostConstant.PAYPREQUARTER.equals(headvo.getPeriodtype())){//按季付息
				lx = SafeCompute.div(lx, new UFDouble(4));
			}else if(CostConstant.PAYPREMONTH.equals(headvo.getPeriodtype())){//按月付息
				lx = SafeCompute.div(lx, new UFDouble(12));
			}
		}
		return lx;
	}


	/**
	 * 获取年利率对应的实际天数。
	 * 
	 * @param headvo 年利率表头
	 * @param startDay 开始日期
	 * @param endDay 结束日期
	 * @return 实际天数
	 * @author 米雪 update by jingqt
	 * @since 2016-5-5 上午8:59:21
	 */
	protected static UFDouble getRealDays(Interest headvo, UFDate startDay, UFDate endDay) {
		UFDouble days = UFDouble.ZERO_DBL;
		if (CostConstant.PAYONCEATEND.equals(headvo.getPaytype())) {// 自定义--按照程序处理，这里应该是 到期一次还本付息
			for (int i = startDay.getYear() + 1; i < endDay.getYear(); i++) {
				if (UFDate.isLeapYear(i)) {
					days = new UFDouble(366);
					return days;
				}
			}
			// 开始日期是闰年并且包含2月
			if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {
				days = new UFDouble(366);
				// 结束日期是闰年并且包含2月
			} else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2) {
				days = new UFDouble(366);
			} else {
				days = new UFDouble(365);
			}
		} else {// 按期付息
			if (startDay.getYear() == endDay.getYear()) {// 不跨年
				if (UFDate.isLeapYear(startDay.getYear()))
					days = new UFDouble(366);
				else
					days = new UFDouble(365);
			} else {// 跨年
				if (UFDate.isLeapYear(startDay.getYear()) && startDay.getMonth() < 3) {// 开始日期是闰年并且包含2月
					days = new UFDouble(366);
				} else if (UFDate.isLeapYear(endDay.getYear()) && endDay.getMonth() > 2) {// 结束日期是闰年并且包含2月
					days = new UFDouble(366);
				} else {
					days = new UFDouble(365);
				}
			}
		}
		return days;
	}

	/**
	 * 加权平均运算
	 * 运算规则：num1/(num1-num2) = mny1 / ?   ? = (num1-num2) * mny1 / num1
	 * scale 代表保留精度位数
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
	 * 加权平均运算
	 * 运算规则：num1/num2 = mny1 / ?  //\\   ? = num2 * mny1 / num1
	 * scale 代表保留精度位数
	 * 
	 */
	public static UFDouble averageMny1(UFDouble num1,UFDouble num2,UFDouble mny1,int scale){
		UFDouble sum = SafeCompute.multiply(mny1, num2);
		UFDouble mny2 = SafeCompute.div(sum, num1);
		mny2 = mny2.setScale(scale, UFDouble.ROUND_HALF_UP);
		return mny2;
	}
	
}
