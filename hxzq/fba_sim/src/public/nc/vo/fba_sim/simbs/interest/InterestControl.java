package nc.vo.fba_sim.simbs.interest;

import java.util.Calendar;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_sim.simbs.floatingrate.IFloatingrateMaintain;
import nc.vo.fba_sim.simbs.floatingrate.FloatingRateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

public class InterestControl {
	public void check(Interest intervo) throws BusinessException {
		UFDate start_date = intervo.getStartdate();
		UFDate end_date = intervo.getEnddate();
		UFDouble yearrate = intervo.getYearrate();
		String periodtype = intervo.getPeriodtype();
		Integer periodsum = intervo.getPeriodsum();
		String paytype = intervo.getPaytype();
		if ((start_date == null) || (end_date == null) || (yearrate == null)
				|| (periodtype == null) || (periodsum == null)
				|| (paytype == null)) {
			throw new BusinessException(
					"��������������Ϣ��ȫ���������������� [��Ϣ��]��[������]��[Ʊ������]��[��Ϣ��ʽ]��[��Ϣ����]��[��Ϣ������] ! ");
		}
		if ((end_date.before(start_date)) || (end_date.equals(start_date))) {
			throw new BusinessException("������ ����С�� ��Ϣ�� ! ");
		}
	}

	private int calcMonth(Interest intervo) {
		int mon = 0;
		UFDate start_date = intervo.getStartdate();
		UFDate end_date = intervo.getEnddate();
		Integer periodsum = intervo.getPeriodsum();
		String periodtype = intervo.getPeriodtype();
		if (periodtype.equals("06")) {
			mon = (end_date.getYear() - start_date.getYear()) * 12
					+ (end_date.getMonth() - start_date.getMonth());
			mon /= periodsum.intValue();
		} else if (periodtype.equals("02")) {
			mon = 6;
		} else if ((periodtype.equals("01")) || (periodtype.equals("05"))) {
			mon = 12;
		} else if (periodtype.equals("03")) {
			mon = 3;
		} else if (periodtype.equals("04")) {
			mon = 1;
		}
		return mon;
	}

	public Rateperiod[] calcRateperiodInfo(Interest intervo, boolean isException)
			throws BusinessException {
		try {
			check(intervo);
		} catch (Exception e) {
			if (isException) {
				throw new BusinessException(e.getMessage());
			}
			Logger.info(e);
			return null;
		}
		UFDate start_date = intervo.getStartdate();
		UFDate end_date = intervo.getEnddate();
		UFDouble yearrate = intervo.getYearrate();
		Integer periodsum = intervo.getPeriodsum();
		String periodtype = intervo.getPeriodtype();
		// �ۿ��ʣ�%��
		UFDouble zkl = UFDouble.ZERO_DBL;
		// ʵ������
		int tianshu = UFDate.getDaysBetween(start_date, end_date) + 1;
		/** QS ��ȡ��ͷ������ݣ��������� 2017-04-28 ADD START */
		// ��������
		String ratetype = intervo.getRatetype();
		// ��׼��������
		String pk_criterionratetype = intervo.getPk_criterionratetype();
		// ����
		UFDouble interestspread = intervo.getInterestspread();
		/** QS ��ȡ��ͷ������ݣ��������� 2017-04-28 ADD END */

		if ((periodtype.equals("06")) && (periodsum.intValue() != 1)) {
			throw new BusinessException("��Ϣ�������Զ���ʱ����Ϣ��������������Ϊ1��");
		}
		int mon = calcMonth(intervo);
		Rateperiod[] rateArray = new Rateperiod[periodsum.intValue()];
		int rowno = 10;
		for (int i = 0; i < rateArray.length; i++) {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(start_date.toDate());
			startCalendar.add(2, mon);
			UFDate nextDate = new UFDate(startCalendar.getTime());
			UFDate enddate = null;
			nextDate = nextDate.getDateBefore(1);
			if ((end_date.after(nextDate)) && (!periodtype.equals("06"))) {
				enddate = nextDate;
			} else {
				enddate = end_date;
			}
			String paytype = intervo.getPaytype();
			int days_sum = UFDate.getDaysBetween(start_date, enddate) + 1;
			// if (("02".equals(paytype)) && (days_sum <= 366)) {
			// days_sum = 365;
			// }
			if ("02".equals(paytype)) {
				for (int k = start_date.getYear(); k < end_date.getYear(); k++) {
					if (UFDate.isLeapYear(k)) {
						days_sum = 366;
					} else {
						days_sum = 365;
					}
				}
			} else if (UFDate.isLeapYear(start_date.getYear() + 1)
					&& start_date.compareTo(new UFDate(start_date.getYear()
							+ "-02-28")) > 0) {
				days_sum = 366;
			} else if (UFDate.isLeapYear(start_date.getYear())
					&& start_date.compareTo(new UFDate(start_date.getYear()
							+ "-02-28")) < 0) {
				days_sum = 366;
			} else {
				days_sum = 365;
			}

			// �����Ϣ��ʽ�����֣����������ʵ��ڣ�100-���۱�������yiʵ��������yi������
			if (intervo.getVdef1() != null && intervo.getPaytype().equals("03")) {
				zkl = new UFDouble(intervo.getVdef1());
				zkl = (new UFDouble(100).sub(zkl))
						.multiply(new UFDouble(10000)).div(
								new UFDouble(tianshu));
				zkl = zkl.multiply(new UFDouble(days_sum)).div(
						new UFDouble(10000));
			}
			// ����Ϣ����Ϊ���긶Ϣ 02�� ������������㷽ʽΪ (����������-��ʼ���ڣ�+1)*2
			if ("02".equals(periodtype)) {
				int days = UFDate.getDaysBetween(start_date, enddate) + 1;
				days_sum = days * 2;
			}
			/** QS 2017-04-28 �����������Ϊ��������02������б���Ʊ�������ʵļ��� ADD START */
			if (ratetype.equals("02")) {
				yearrate = calYearRate(pk_criterionratetype, interestspread,
						start_date);
			}
			/** QS 2017-04-28 �����������Ϊ��������02������б���Ʊ�������ʵļ��� ADD END */

			Rateperiod v1 = new Rateperiod();
			v1.setRowno(String.valueOf(rowno));
			v1.setStart_day(start_date);
			v1.setEnd_day(enddate);
			if (zkl.compareTo(new UFDouble(0)) != 0) {
				v1.setYear_rate(zkl);
			} else {
				v1.setYear_rate(yearrate);
			}
			v1.setPk_interest(intervo.getPk_interest());
			v1.setDays_num(Integer.valueOf(days_sum));
			v1.setStatus(2);
			start_date = enddate.getDateAfter(1);
			rowno += 10;
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

	public Integer calcPeriodsum(Interest vo) {
		if ((vo == null) || (vo.getStartdate() == null)
				|| (vo.getEnddate() == null) || (vo.getPeriodtype() == null)) {
			return Integer.valueOf(0);
		}
		UFDate startdate = vo.getStartdate();
		UFDate enddate = vo.getEnddate();
		int days = enddate.getDaysAfter(startdate);
		int halfyear = 183;
		int oneyear = 366;
		int aquarter = 92;
		int amonth = 31;
		int i = 0;
		if ("02".equals(vo.getPeriodtype())) {
			i = days / halfyear;
			if (days > i * halfyear) {
				i++;
			}
		} else if (("01".equals(vo.getPeriodtype()))
				|| ("05".equals(vo.getPeriodtype()))) {
			i = days / oneyear;
			if (days > i * oneyear) {
				i++;
			}
		} else if ("03".equals(vo.getPeriodtype())) {
			i = days / aquarter;
			if (days > i * aquarter) {
				i++;
			}
		} else if ("04".equals(vo.getPeriodtype())) {
			i = days / amonth;
			if (days > i * amonth) {
				i++;
			}
		} else if ("06".equals(vo.getPeriodtype())) {
			i = 1;
		}
		return Integer.valueOf(i);
	}
}
