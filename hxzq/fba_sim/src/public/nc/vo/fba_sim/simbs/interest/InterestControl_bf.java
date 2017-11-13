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
	 * ����У��
	 */
	public void check(Interest intervo) throws BusinessException {
		UFDate start_date = intervo.getStartdate();// ��Ϣ��
		UFDate end_date = intervo.getEnddate();// ������
		UFDouble yearrate = intervo.getYearrate();// Ʊ������
		String periodtype = intervo.getPeriodtype();// ��Ϣ����
		Integer periodsum = intervo.getPeriodsum();// ��Ϣ������
		String paytype = intervo.getPaytype();// ��Ϣ��ʽ
		// ��Ȩ������
		/*
		 * UFDate exerciseDate = intervo.getExercisedate(); if (null !=
		 * exerciseDate) { if (exerciseDate.before(end_date) ||
		 * exerciseDate.equals(end_date)) { throw new
		 * BusinessException("��Ȩ�����ձ����ڵ�����֮��"); } }
		 */
		if (start_date == null || end_date == null || yearrate == null
				|| periodtype == null || periodsum == null || paytype == null) {
			throw new BusinessException("��������������Ϣ��ȫ���������������� [��Ϣ��]��"
					+ "[������]��[Ʊ������]��[��Ϣ��ʽ]��[��Ϣ����]��[��Ϣ������] ! ");
		}
		if (end_date.before(start_date) || end_date.equals(start_date)) {
			throw new BusinessException("������ ����С�� ��Ϣ�� ! ");
		}
	}

	/**
	 * ��������·�
	 */
	private int calcMonth(Interest intervo) {
		int mon = 0;
		UFDate start_date = intervo.getStartdate();// ��Ϣ��
		UFDate end_date = intervo.getEnddate();// ������
		Integer periodsum = intervo.getPeriodsum();// ��Ϣ������
		String periodtype = intervo.getPeriodtype();// ��Ϣ����
		if (periodtype.equals(CostConstant.PAYSELF)) {// �Զ���
			mon = (end_date.getYear() - start_date.getYear()) * 12
					+ (end_date.getMonth() - start_date.getMonth());
			mon = mon / periodsum.intValue();
		} else if (periodtype.equals(CostConstant.PAYPREHALFYEAR)) {// ���긶Ϣ
			mon = 6;
		} else if (periodtype.equals(CostConstant.PAYPREYEAR)
				|| periodtype.equals(CostConstant.SCP)) {// һ�긶Ϣ,������
			mon = 12;
		} else if (periodtype.equals(CostConstant.PAYPREQUARTER)) {// ������Ϣ
			mon = 3;
		} else if (periodtype.equals(CostConstant.PAYPREMONTH)) {// ���¸�Ϣ
			mon = 1;
		}
		return mon;
	}

	/**
	 * ���ݱ�ͷ�����������
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
		UFDate start_date = intervo.getStartdate();// ��Ϣ��
		UFDate end_date = intervo.getEnddate();// ������
		UFDouble yearrate = intervo.getYearrate();// Ʊ������
		Integer periodsum = intervo.getPeriodsum();// ��Ϣ������
		String periodtype = intervo.getPeriodtype();// ��Ϣ����
		/** QS ���ֳ�Ǩ�����Ĵ��� 2017-05-18 ADD START */
		// �ۿ��ʣ�%��
		UFDouble zkl = UFDouble.ZERO_DBL;
		// ʵ������
		int tianshu = UFDate.getDaysBetween(start_date, end_date) + 1;
		// ��Ϣ��ʽ
		String paytype = intervo.getPaytype();
		/** QS ���ֳ�Ǩ�����Ĵ��� 2017-05-18 ADD END */
		/** QS ��ȡ��ͷ������ݣ��������� 2017-04-28 ADD START */
		// ��������
		String ratetype = intervo.getRatetype();
		// ��׼��������
		String pk_criterionratetype = intervo.getPk_criterionratetype();
		// ����
		UFDouble interestspread = intervo.getInterestspread();
		/** QS ��ȡ��ͷ������ݣ��������� 2017-04-28 ADD END */
		/** QS ��ȡ��ͷ������ݣ���Ȩծ����ҵ�� 2017-05-22 ADD START */
		// �Ƿ���Ȩ
		String isExercise = intervo.getIsexercise();
		// ��Ȩ������
		UFDate exerciseDate = intervo.getExercisedate();
		// ��Ȩ��Ʊ������
		UFDouble aftExcisDateRate = intervo.getAftexcisdaterate();
		/** QS ��ȡ��ͷ������ݣ���Ȩծ����ҵ�� 2017-04-22 ADD END */
		/** JINGQT 2016��3��11�� ���У�飺��Ϣ�������Զ����ʱ�򣬸�Ϣ������������1 ADD START */
		// if (periodtype.equals(CostConstant.PAYSELF) && periodsum.intValue()
		// != 1) {
		// throw new BusinessException("��Ϣ�������Զ���ʱ����Ϣ��������������Ϊ1��");
		// }
		/** JINGQT 2016��3��11�� ���У�飺��Ϣ�������Զ����ʱ�򣬸�Ϣ������������1 ADD END */
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
			// /** JINGQT 2016��3��11�� �ڸ�Ϣ�������Զ����ʱ�����������϶���1�ڣ��ʽ�������ֱ��ȡ�����ռ��� ����ж�����
			// ADD START */
			// if (end_date.after(nextDate)) {
			// if (exerciseDate.after(nextDate) &&
			// !periodtype.equals(CostConstant.PAYSELF)) {
			// /** JINGQT 2016��3��11�� �ڸ�Ϣ�������Զ����ʱ�����������϶���1�ڣ��ʽ�������ֱ��ȡ�����ռ��� ����ж�����
			// ADD END */
			// enddate = nextDate;
			// }
			/** QS ����Ƿ���Ȩ����Ȩ�����ղ�Ϊ�գ��������һ���ж� 2017-05-22 UPDATE START */
			if (null != exerciseDate && null != isExercise) {
				// ����Ƿ���Ȩѡ�񡰷�����һ����������Ȩ������֮ǰ����ȡ��һ��������Ϊ�������ڣ�����ȡ��Ȩ������Ϊ��������
				if (isExercise.equals("1") && exerciseDate.after(nextDate)) {
					enddate = nextDate;
				} else if (nextDate.after(exerciseDate)) {
					enddate = exerciseDate;
				}
			} else if (end_date.after(nextDate)) {
				enddate = nextDate;
			}
			/** QS ����Ƿ���Ȩ����Ȩ�����ղ�Ϊ�գ��������һ���ж� 2017-05-22 UPDATE END */
			else {
				enddate = end_date;
			}
			int days_sum = UFDate.getDaysBetween(start_date, enddate) + 1;
			/**
			 * 2016��5��6�� JINGQT
			 * �������һ�λ�����Ϣ��Ӱ��֮�����Ϣ�Ҹ���������Ӧ����Ϣ��Ӧ����Ϣ���ᣬ����Ӧ�ú������ı���һ���ԣ�����ʾʵ������
			 */
			// String paytype = intervo.getPaytype();// ��Ϣ��ʽ
			// if(CostConstant.PAYONCEATEND.equals(paytype) && days_sum <= 366){
			// days_sum = 365;
			// }
			/**
			 * 2016��5��6�� JINGQT
			 * �������һ�λ�����Ϣ��Ӱ��֮�����Ϣ�Ҹ���������Ӧ����Ϣ��Ӧ����Ϣ���ᣬ����Ӧ�ú������ı���һ���ԣ�����ʾʵ������
			 */
			/** QS 2017-04-28 �����������Ϊ��������02������б���Ʊ�������ʵļ��� ADD START */
			if (ratetype.equals("02")) {
				yearrate = calYearRate(pk_criterionratetype, interestspread,
						start_date);
			}
			/** QS 2017-04-28 �����������Ϊ��������02������б���Ʊ�������ʵļ��� ADD END */
			/**
			 * qs ���ֳ�Ǩ�ƹ����Ĵ��룺�Ը�Ϣ��ʽ�ǡ�����һ�λ�����Ϣ02��������03������Ϣ�����ǡ����긶Ϣ������������˴���
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

			// �����Ϣ��ʽ�����֣����������ʵ��ڣ�100-���۱�����/ʵ������*������
			/** QS �ۿ���discountrate���������Զ�����VDEF1 2017-05-22 UPDATE START */
			if (intervo.getDiscountrate() != null
					&& intervo.getPaytype().equals("03")) {
				zkl = new UFDouble(intervo.getDiscountrate());
				/** QS �ۿ���discountrate���������Զ�����VDEF1 2017-05-22 UPDATE END */
				zkl = (new UFDouble(100).sub(zkl))
						.multiply(new UFDouble(10000)).div(
								new UFDouble(tianshu));
				zkl = zkl.multiply(new UFDouble(days_sum)).div(
						new UFDouble(10000));
			}
			// ����Ϣ����Ϊ���긶Ϣ 02�� ������������㷽ʽΪ (����������-��ʼ���ڣ�+1)*2
			// if ("02".equals(periodtype)) {
			// int days = UFDate.getDaysBetween(start_date, enddate) + 1;
			// days_sum = days * 2;
			// }
			/**
			 * qs ���ֳ�Ǩ�ƹ����Ĵ��룺�Ը�Ϣ��ʽ�ǡ�����һ�λ�����Ϣ02��������03������Ϣ�����ǡ����긶Ϣ������������˴���
			 * 2017-05-18 add end
			 */
			Rateperiod v1 = new Rateperiod();
			v1.setRowno(String.valueOf(rowno));
			v1.setStart_day(start_date);
			v1.setEnd_day(enddate);
			/** qs ���ֳ�Ǩ�ƹ����Ĵ��룺����ۿ��ʲ�����0����������ֱ�Ӹ�ֵzkl 2017-05-18 update start */
			if (zkl.compareTo(new UFDouble(0)) != 0) {
				v1.setYear_rate(zkl);
			} else {
				v1.setYear_rate(yearrate);
			}
			/** qs ���ֳ�Ǩ�ƹ����Ĵ��룺����ۿ��ʲ�����0����������ֱ�Ӹ�ֵzkl 2017-05-18 update start */
			/**
			 * QS �����ʼ����>=�����գ�˵��Ϊ��Ȩծ׷�ӵı�����Ϣ������������Ϊ��Ȩ�ں�Ʊ�����ʣ�����VBDEF4��Ϊ��ʶ��
			 * �Ա���ڶ���Ȩ��Ϣ�����޸�ʱ���� 2017-04-28 ADD START
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
			 * QS �����ʼ����>=�����գ�˵��Ϊ��Ȩծ׷�ӵı�����Ϣ������������Ϊ��Ȩ�ں�Ʊ�����ʣ�����VBDEF4��Ϊ��ʶ��
			 * �Ա���ڶ���Ȩ��Ϣ�����޸� 2017-04-28 ADD END
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
	 * �������Ʊ�������ʣ�ǰ������������Ϊ��������
	 * 
	 * @author qs
	 * @throws DAOException
	 * @since 2017-4-28����7:25:01
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
			ExceptionUtils.wrappBusinessException("��ǰ��Ϣ��֮ǰû�н��и������ʻ�׼����!");
		}
		// ��ȡ����ʱ����뱾�ڿ�ʼʱ������Ļ�׼����
		UFDouble criterionrate = ((FloatingRateVO) obj[0]).getCriterionrate();// ��׼����
		if (null == criterionrate || criterionrate.equals("")) {
			ExceptionUtils.wrappBusinessException("��ǰѡ��Ļ�׼��������û�����û�׼����!");
		}
		// Ʊ��������=��׼����+����
		yearRate = SafeCompute.add(criterionrate, interestspread);
		return yearRate;

	}

	/**
	 * �����Ϣ������
	 */
	public Integer calcPeriodsum(Interest vo) {
		if (vo == null || vo.getStartdate() == null || vo.getEnddate() == null
				|| vo.getPeriodtype() == null)
			return 0;
		UFDate startdate = vo.getStartdate();
		UFDate enddate = vo.getEnddate();
		/** QS ����Ƿ���Ȩѡ�񡰷񡱣��������Ȩ�����ռ����Ϣ������ UPDATE��START */
		if (null != vo.getIsexercise()) {
			if (vo.getIsexercise().equals("1") && null != vo.getExercisedate()) {
				enddate = vo.getExercisedate();
			}
		}
		/** QS ����Ƿ���Ȩѡ�񡰷񡱣��������Ȩ�����ռ����Ϣ������ UPDATE��END */
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
