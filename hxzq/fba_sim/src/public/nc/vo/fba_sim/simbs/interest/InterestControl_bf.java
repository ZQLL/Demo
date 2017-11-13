package nc.vo.fba_sim.simbs.interest;

import java.util.Calendar;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_sim.simbs.floatingrate.IFloatingrateMaintain;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_sim.simbs.floatingrate.FloatingRateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

public class InterestControl_bf {

	/**
	 * 界面校验
	 */
	public void check(Interest intervo) throws BusinessException {
		UFDate start_date = intervo.getStartdate();// 起息日
		UFDate end_date = intervo.getEnddate();// 到期日
		UFDouble yearrate = intervo.getYearrate();// 票面利率
		String periodtype = intervo.getPeriodtype();// 付息周期
		Integer periodsum = intervo.getPeriodsum();// 付息总期数
		String paytype = intervo.getPaytype();// 付息方式
		// 行权到期日
		/*
		 * UFDate exerciseDate = intervo.getExercisedate(); if (null !=
		 * exerciseDate) { if (exerciseDate.before(end_date) ||
		 * exerciseDate.equals(end_date)) { throw new
		 * BusinessException("行权到期日必须在到期日之后！"); } }
		 */
		if (start_date == null || end_date == null || yearrate == null
				|| periodtype == null || periodsum == null || paytype == null) {
			throw new BusinessException("利率设置主表信息不全！请检查以下设置项 [起息日]、"
					+ "[到期日]、[票面利率]、[付息方式]、[付息周期]、[付息总期数] ! ");
		}
		if (end_date.before(start_date) || end_date.equals(start_date)) {
			throw new BusinessException("到期日 不能小于 起息日 ! ");
		}
	}

	/**
	 * 计算相隔月份
	 */
	private int calcMonth(Interest intervo) {
		int mon = 0;
		UFDate start_date = intervo.getStartdate();// 起息日
		UFDate end_date = intervo.getEnddate();// 到期日
		Integer periodsum = intervo.getPeriodsum();// 付息总期数
		String periodtype = intervo.getPeriodtype();// 付息周期
		if (periodtype.equals(CostConstant.PAYSELF)) {// 自定义
			mon = (end_date.getYear() - start_date.getYear()) * 12
					+ (end_date.getMonth() - start_date.getMonth());
			mon = mon / periodsum.intValue();
		} else if (periodtype.equals(CostConstant.PAYPREHALFYEAR)) {// 半年付息
			mon = 6;
		} else if (periodtype.equals(CostConstant.PAYPREYEAR)
				|| periodtype.equals(CostConstant.SCP)) {// 一年付息,超短融
			mon = 12;
		} else if (periodtype.equals(CostConstant.PAYPREQUARTER)) {// 按季付息
			mon = 3;
		} else if (periodtype.equals(CostConstant.PAYPREMONTH)) {// 按月付息
			mon = 1;
		}
		return mon;
	}

	/**
	 * 根据表头计算表体数据
	 */
	public Rateperiod[] calcRateperiodInfo(Interest intervo, boolean isException)
			throws BusinessException {
		try {
			check(intervo);
		} catch (Exception e) {
			if (isException) {
				throw new BusinessException(e.getMessage());
			} else {
				Logger.info(e);
				return null;
			}
		}
		UFDate start_date = intervo.getStartdate();// 起息日
		UFDate end_date = intervo.getEnddate();// 到期日
		UFDouble yearrate = intervo.getYearrate();// 票面利率
		Integer periodsum = intervo.getPeriodsum();// 付息总期数
		String periodtype = intervo.getPeriodtype();// 付息周期
		/** QS 从现场迁过来的代码 2017-05-18 ADD START */
		// 折扣率（%）
		UFDouble zkl = UFDouble.ZERO_DBL;
		// 实际天数
		int tianshu = UFDate.getDaysBetween(start_date, end_date) + 1;
		// 付息方式
		String paytype = intervo.getPaytype();
		/** QS 从现场迁过来的代码 2017-05-18 ADD END */
		/** QS 获取表头相关数据：浮动利率 2017-04-28 ADD START */
		// 利率种类
		String ratetype = intervo.getRatetype();
		// 基准利率类型
		String pk_criterionratetype = intervo.getPk_criterionratetype();
		// 利差
		UFDouble interestspread = intervo.getInterestspread();
		/** QS 获取表头相关数据：浮动利率 2017-04-28 ADD END */
		/** QS 获取表头相关数据：含权债核算业务 2017-05-22 ADD START */
		// 是否行权
		String isExercise = intervo.getIsexercise();
		// 行权到期日
		UFDate exerciseDate = intervo.getExercisedate();
		// 行权后票面利率
		UFDouble aftExcisDateRate = intervo.getAftexcisdaterate();
		/** QS 获取表头相关数据：含权债核算业务 2017-04-22 ADD END */
		/** JINGQT 2016年3月11日 添加校验：付息周期是自定义的时候，付息总期数必须是1 ADD START */
		// if (periodtype.equals(CostConstant.PAYSELF) && periodsum.intValue()
		// != 1) {
		// throw new BusinessException("付息周期是自定义时，付息总期数必须设置为1期");
		// }
		/** JINGQT 2016年3月11日 添加校验：付息周期是自定义的时候，付息总期数必须是1 ADD END */
		int mon = calcMonth(intervo);
		Rateperiod[] rateArray = new Rateperiod[periodsum];
		int rowno = 10;
		for (int i = 0; i < rateArray.length; i++) {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(start_date.toDate());
			startCalendar.add(Calendar.MONTH, mon);
			UFDate nextDate = new UFDate(startCalendar.getTime());
			UFDate enddate = null;
			nextDate = nextDate.getDateBefore(1);
			// /** JINGQT 2016年3月11日 在付息周期是自定义的时候，其总期数肯定是1期，故结束日期直接取到期日即可 添加判断条件
			// ADD START */
			// if (end_date.after(nextDate)) {
			// if (exerciseDate.after(nextDate) &&
			// !periodtype.equals(CostConstant.PAYSELF)) {
			// /** JINGQT 2016年3月11日 在付息周期是自定义的时候，其总期数肯定是1期，故结束日期直接取到期日即可 添加判断条件
			// ADD END */
			// enddate = nextDate;
			// }
			/** QS 如果是否行权和行权到期日不为空，则进行下一步判断 2017-05-22 UPDATE START */
			if (null != exerciseDate && null != isExercise) {
				// 如果是否行权选择“否”且下一个日期在行权到期日之前，则取下一个日期作为结束日期，否则取行权到期日为结束日期
				if (isExercise.equals("1") && exerciseDate.after(nextDate)) {
					enddate = nextDate;
				} else if (nextDate.after(exerciseDate)) {
					enddate = exerciseDate;
				}
			} else if (end_date.after(nextDate)) {
				enddate = nextDate;
			}
			/** QS 如果是否行权和行权到期日不为空，则进行下一步判断 2017-05-22 UPDATE END */
			else {
				enddate = end_date;
			}
			int days_sum = UFDate.getDaysBetween(start_date, enddate) + 1;
			/**
			 * 2016年5月6日 JINGQT
			 * 这个到期一次还本付息会影响之后的利息兑付单、买卖应收利息、应收利息计提，这里应该和其他的保持一致性，即显示实际天数
			 */
			// String paytype = intervo.getPaytype();// 付息方式
			// if(CostConstant.PAYONCEATEND.equals(paytype) && days_sum <= 366){
			// days_sum = 365;
			// }
			/**
			 * 2016年5月6日 JINGQT
			 * 这个到期一次还本付息会影响之后的利息兑付单、买卖应收利息、应收利息计提，这里应该和其他的保持一致性，即显示实际天数
			 */
			/** QS 2017-04-28 如果利率种类为浮动利率02，则进行表体票面年利率的计算 ADD START */
			if (ratetype.equals("02")) {
				yearrate = calYearRate(pk_criterionratetype, interestspread,
						start_date);
			}
			/** QS 2017-04-28 如果利率种类为浮动利率02，则进行表体票面年利率的计算 ADD END */
			/**
			 * qs 从现场迁移过来的代码：对付息方式是【到期一次还本付息02】【贴现03】、付息周期是【半年付息】的情况都做了处理
			 * 2017-05-18 add start
			 */
			// if ("02".equals(paytype)) {
			// for (int k = start_date.getYear(); k < end_date.getYear(); k++) {
			// if (UFDate.isLeapYear(k)) {
			// days_sum = 366;
			// } else {
			// days_sum = 365;
			// }
			// }
			// }
			// else if (UFDate.isLeapYear(start_date.getYear() + 1) &&
			// start_date.compareTo(new UFDate(start_date.getYear() + "-02-28"))
			// > 0) {
			// days_sum = 366;
			// } else if (UFDate.isLeapYear(start_date.getYear()) &&
			// start_date.compareTo(new UFDate(start_date.getYear() + "-02-28"))
			// < 0) {
			// days_sum = 366;
			// } else {
			// days_sum = 365;
			// }

			// 如果付息方式是贴现，则表体的利率等于（100-打折比例）/实际天数*年天数
			/** QS 折扣率discountrate，不启用自定义项VDEF1 2017-05-22 UPDATE START */
			if (intervo.getDiscountrate() != null
					&& intervo.getPaytype().equals("03")) {
				zkl = new UFDouble(intervo.getDiscountrate());
				/** QS 折扣率discountrate，不启用自定义项VDEF1 2017-05-22 UPDATE END */
				zkl = (new UFDouble(100).sub(zkl))
						.multiply(new UFDouble(10000)).div(
								new UFDouble(tianshu));
				zkl = zkl.multiply(new UFDouble(days_sum)).div(
						new UFDouble(10000));
			}
			// 若付息周期为半年付息 02， 则表体天数计算方式为 (（结束日期-开始日期）+1)*2
			// if ("02".equals(periodtype)) {
			// int days = UFDate.getDaysBetween(start_date, enddate) + 1;
			// days_sum = days * 2;
			// }
			/**
			 * qs 从现场迁移过来的代码：对付息方式是【到期一次还本付息02】【贴现03】、付息周期是【半年付息】的情况都做了处理
			 * 2017-05-18 add end
			 */
			Rateperiod v1 = new Rateperiod();
			v1.setRowno(String.valueOf(rowno));
			v1.setStart_day(start_date);
			v1.setEnd_day(enddate);
			/** qs 从现场迁移过来的代码：如果折扣率不等于0，则年利率直接赋值zkl 2017-05-18 update start */
			if (zkl.compareTo(new UFDouble(0)) != 0) {
				v1.setYear_rate(zkl);
			} else {
				v1.setYear_rate(yearrate);
			}
			/** qs 从现场迁移过来的代码：如果折扣率不等于0，则年利率直接赋值zkl 2017-05-18 update start */
			/**
			 * QS 如果开始日期>=到期日，说明为行权债追加的表体信息：它的年利率为行权期后票面利率；启用VBDEF4作为标识，
			 * 以便后期对行权信息进行修改时处理 2017-04-28 ADD START
			 */
			// if (start_date.after(end_date) ||
			// start_date.asBegin().toString().substring(0,
			// 10).equals(end_date.toString().substring(0, 10))) {
			if (start_date.after(end_date)
					|| start_date.asBegin().equals(end_date.asBegin())) {
				v1.setVbdef4("1");
				v1.setYear_rate(aftExcisDateRate);
			}
			/**
			 * QS 如果开始日期>=到期日，说明为行权债追加的表体信息：它的年利率为行权期后票面利率；启用VBDEF4作为标识，
			 * 以便后期对行权信息进行修改 2017-04-28 ADD END
			 */
			v1.setPk_interest(intervo.getPk_interest());
			v1.setDays_num(days_sum);
			v1.setStatus(VOStatus.NEW);
			start_date = enddate.getDateAfter(1);
			rowno = rowno + 10;
			rateArray[i] = v1;
		}
		return rateArray;
	}

	/**
	 * 计算表体票面年利率：前提是利率种类为浮动利率
	 * 
	 * @author qs
	 * @throws DAOException
	 * @since 2017-4-28下午7:25:01
	 */
	public UFDouble calYearRate(String pk_criterionratetype,
			UFDouble interestspread, UFDate start_date) throws DAOException {
		UFDouble yearRate;
		IFloatingrateMaintain iFloatingrateMaintain = NCLocator.getInstance()
				.lookup(IFloatingrateMaintain.class);
		List list = iFloatingrateMaintain.queryFloatingRate(start_date,
				pk_criterionratetype);
		Object[] obj = list.toArray();
		if (obj.length == 0) {
			ExceptionUtils.wrappBusinessException("当前起息日之前没有进行浮动利率基准设置!");
		}
		// 获取调整时间距离本期开始时间最近的基准利率
		UFDouble criterionrate = ((FloatingRateVO) obj[0]).getCriterionrate();// 基准利率
		if (null == criterionrate || criterionrate.equals("")) {
			ExceptionUtils.wrappBusinessException("当前选择的基准利率类型没有设置基准利率!");
		}
		// 票面年利率=基准利率+利差
		yearRate = SafeCompute.add(criterionrate, interestspread);
		return yearRate;

	}

	/**
	 * 计算计息总期数
	 */
	public Integer calcPeriodsum(Interest vo) {
		if (vo == null || vo.getStartdate() == null || vo.getEnddate() == null
				|| vo.getPeriodtype() == null)
			return 0;
		UFDate startdate = vo.getStartdate();
		UFDate enddate = vo.getEnddate();
		/** QS 如果是否行权选择“否”，则根据行权到期日计算计息总其数 UPDATE　START */
		if (null != vo.getIsexercise()) {
			if (vo.getIsexercise().equals("1") && null != vo.getExercisedate()) {
				enddate = vo.getExercisedate();
			}
		}
		/** QS 如果是否行权选择“否”，则根据行权到期日计算计息总其数 UPDATE　END */
		int days = enddate.getDaysAfter(startdate);
		int halfyear = 183;
		int oneyear = 366;
		int aquarter = 92;
		int amonth = 31;
		int i = 0;
		if (CostConstant.PAYPREHALFYEAR.equals(vo.getPeriodtype())) {
			i = days / halfyear;
			if (days > i * halfyear)
				i++;
		} else if (CostConstant.PAYPREYEAR.equals(vo.getPeriodtype())
				|| CostConstant.SCP.equals(vo.getPeriodtype())) {
			i = days / oneyear;
			if (days > i * oneyear)
				i++;
		} else if (CostConstant.PAYPREQUARTER.equals(vo.getPeriodtype())) {
			i = days / aquarter;
			if (days > i * aquarter)
				i++;
		} else if (CostConstant.PAYPREMONTH.equals(vo.getPeriodtype())) {
			i = days / amonth;
			if (days > i * amonth)
				i++;
		}
		// else if (CostConstant.PAYSELF.equals(vo.getPeriodtype())) {
		// i = 1;
		// }
		return i;
	}
}
