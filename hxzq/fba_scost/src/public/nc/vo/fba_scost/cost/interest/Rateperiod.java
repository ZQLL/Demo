package nc.vo.fba_scost.cost.interest;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class Rateperiod extends SuperVO {
	/** UID */
	private static final long serialVersionUID = 4693917948513888640L;
	/**
	 * ����
	 */
	public static final String DAYS_NUM = "days_num";
	/**
	 * ��������
	 */
	public static final String END_DAY = "end_day";
	/**
	 * �����Զ�����ֵ1
	 */
	public static final String NBDEF1 = "nbdef1";
	/**
	 * �����Զ�����ֵ2
	 */
	public static final String NBDEF2 = "nbdef2";
	/**
	 * �����Զ�����ֵ3
	 */
	public static final String NBDEF3 = "nbdef3";
	/**
	 * �����Զ�����ֵ4
	 */
	public static final String NBDEF4 = "nbdef4";
	/**
	 * �����Զ�����ֵ5
	 */
	public static final String NBDEF5 = "nbdef5";
	/**
	 * �ϲ㵥������
	 */
	public static final String PK_INTEREST = "pk_interest";
	/**
	 * �ӱ�����
	 */
	public static final String PK_RATEPERIOD = "pk_rateperiod";
	/**
	 * �к�
	 */
	public static final String ROWNO = "rowno";
	/**
	 * ��ʼ����
	 */
	public static final String START_DAY = "start_day";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";
	/**
	 * �����Զ�����1
	 */
	public static final String VBDEF1 = "vbdef1";
	/**
	 * �����Զ�����2
	 */
	public static final String VBDEF2 = "vbdef2";
	/**
	 * �����Զ�����3
	 */
	public static final String VBDEF3 = "vbdef3";
	/**
	 * �����Զ�����4
	 */
	public static final String VBDEF4 = "vbdef4";
	/**
	 * �����Զ�����5
	 */
	public static final String VBDEF5 = "vbdef5";
	/**
	 * ��ע
	 */
	public static final String VNOTE = "vnote";
	/**
	 * Ʊ��������
	 */
	public static final String YEAR_RATE = "year_rate";
	/**
	 * ��ǰ�������
	 */
	public static final String PAYPERCENT = "paypercent";

	/**
	 * ��ǰ�������
	 */
	public void setPaypercent(UFDouble paypercent) {
		this.setAttributeValue(Rateperiod.PAYPERCENT, paypercent);
	}

	/**
	 * ��ǰ�������
	 */
	public UFDouble getPaypercent() {
		return (UFDouble) this.getAttributeValue(Rateperiod.PAYPERCENT);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public Integer getDays_num() {
		return (Integer) this.getAttributeValue(Rateperiod.DAYS_NUM);
	}

	/**
	 * ��������
	 * 
	 * @param days_num
	 *            ����
	 */
	public void setDays_num(Integer days_num) {
		this.setAttributeValue(Rateperiod.DAYS_NUM, days_num);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getEnd_day() {
		return (UFDate) this.getAttributeValue(Rateperiod.END_DAY);
	}

	/**
	 * ���ý�������
	 * 
	 * @param end_day
	 *            ��������
	 */
	public void setEnd_day(UFDate end_day) {
		this.setAttributeValue(Rateperiod.END_DAY, end_day);
	}

	/**
	 * ��ȡ�����Զ�����ֵ1
	 * 
	 * @return �����Զ�����ֵ1
	 */
	public UFDouble getNbdef1() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF1);
	}

	/**
	 * ���ñ����Զ�����ֵ1
	 * 
	 * @param nbdef1
	 *            �����Զ�����ֵ1
	 */
	public void setNbdef1(UFDouble nbdef1) {
		this.setAttributeValue(Rateperiod.NBDEF1, nbdef1);
	}

	/**
	 * ��ȡ�����Զ�����ֵ2
	 * 
	 * @return �����Զ�����ֵ2
	 */
	public UFDouble getNbdef2() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF2);
	}

	/**
	 * ���ñ����Զ�����ֵ2
	 * 
	 * @param nbdef2
	 *            �����Զ�����ֵ2
	 */
	public void setNbdef2(UFDouble nbdef2) {
		this.setAttributeValue(Rateperiod.NBDEF2, nbdef2);
	}

	/**
	 * ��ȡ�����Զ�����ֵ3
	 * 
	 * @return �����Զ�����ֵ3
	 */
	public UFDouble getNbdef3() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF3);
	}

	/**
	 * ���ñ����Զ�����ֵ3
	 * 
	 * @param nbdef3
	 *            �����Զ�����ֵ3
	 */
	public void setNbdef3(UFDouble nbdef3) {
		this.setAttributeValue(Rateperiod.NBDEF3, nbdef3);
	}

	/**
	 * ��ȡ�����Զ�����ֵ4
	 * 
	 * @return �����Զ�����ֵ4
	 */
	public UFDouble getNbdef4() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF4);
	}

	/**
	 * ���ñ����Զ�����ֵ4
	 * 
	 * @param nbdef4
	 *            �����Զ�����ֵ4
	 */
	public void setNbdef4(UFDouble nbdef4) {
		this.setAttributeValue(Rateperiod.NBDEF4, nbdef4);
	}

	/**
	 * ��ȡ�����Զ�����ֵ5
	 * 
	 * @return �����Զ�����ֵ5
	 */
	public UFDouble getNbdef5() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF5);
	}

	/**
	 * ���ñ����Զ�����ֵ5
	 * 
	 * @param nbdef5
	 *            �����Զ�����ֵ5
	 */
	public void setNbdef5(UFDouble nbdef5) {
		this.setAttributeValue(Rateperiod.NBDEF5, nbdef5);
	}

	/**
	 * ��ȡ�ϲ㵥������
	 * 
	 * @return �ϲ㵥������
	 */
	public String getPk_interest() {
		return (String) this.getAttributeValue(Rateperiod.PK_INTEREST);
	}

	/**
	 * �����ϲ㵥������
	 * 
	 * @param pk_interest
	 *            �ϲ㵥������
	 */
	public void setPk_interest(String pk_interest) {
		this.setAttributeValue(Rateperiod.PK_INTEREST, pk_interest);
	}

	/**
	 * ��ȡ�ӱ�����
	 * 
	 * @return �ӱ�����
	 */
	public String getPk_rateperiod() {
		return (String) this.getAttributeValue(Rateperiod.PK_RATEPERIOD);
	}

	/**
	 * �����ӱ�����
	 * 
	 * @param pk_rateperiod
	 *            �ӱ�����
	 */
	public void setPk_rateperiod(String pk_rateperiod) {
		this.setAttributeValue(Rateperiod.PK_RATEPERIOD, pk_rateperiod);
	}

	/**
	 * ��ȡ�к�
	 * 
	 * @return �к�
	 */
	public String getRowno() {
		return (String) this.getAttributeValue(Rateperiod.ROWNO);
	}

	/**
	 * �����к�
	 * 
	 * @param rowno
	 *            �к�
	 */
	public void setRowno(String rowno) {
		this.setAttributeValue(Rateperiod.ROWNO, rowno);
	}

	/**
	 * ��ȡ��ʼ����
	 * 
	 * @return ��ʼ����
	 */
	public UFDate getStart_day() {
		return (UFDate) this.getAttributeValue(Rateperiod.START_DAY);
	}

	/**
	 * ���ÿ�ʼ����
	 * 
	 * @param start_day
	 *            ��ʼ����
	 */
	public void setStart_day(UFDate start_day) {
		this.setAttributeValue(Rateperiod.START_DAY, start_day);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(Rateperiod.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(Rateperiod.TS, ts);
	}

	/**
	 * ��ȡ�����Զ�����1
	 * 
	 * @return �����Զ�����1
	 */
	public String getVbdef1() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF1);
	}

	/**
	 * ���ñ����Զ�����1
	 * 
	 * @param vbdef1
	 *            �����Զ�����1
	 */
	public void setVbdef1(String vbdef1) {
		this.setAttributeValue(Rateperiod.VBDEF1, vbdef1);
	}

	/**
	 * ��ȡ�����Զ�����2
	 * 
	 * @return �����Զ�����2
	 */
	public String getVbdef2() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF2);
	}

	/**
	 * ���ñ����Զ�����2
	 * 
	 * @param vbdef2
	 *            �����Զ�����2
	 */
	public void setVbdef2(String vbdef2) {
		this.setAttributeValue(Rateperiod.VBDEF2, vbdef2);
	}

	/**
	 * ��ȡ�����Զ�����3
	 * 
	 * @return �����Զ�����3
	 */
	public String getVbdef3() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF3);
	}

	/**
	 * ���ñ����Զ�����3
	 * 
	 * @param vbdef3
	 *            �����Զ�����3
	 */
	public void setVbdef3(String vbdef3) {
		this.setAttributeValue(Rateperiod.VBDEF3, vbdef3);
	}

	/**
	 * ��ȡ�����Զ�����4
	 * 
	 * @return �����Զ�����4
	 */
	public String getVbdef4() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF4);
	}

	/**
	 * ���ñ����Զ�����4
	 * 
	 * @param vbdef4
	 *            �����Զ�����4
	 */
	public void setVbdef4(String vbdef4) {
		this.setAttributeValue(Rateperiod.VBDEF4, vbdef4);
	}

	/**
	 * ��ȡ�����Զ�����5
	 * 
	 * @return �����Զ�����5
	 */
	public String getVbdef5() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF5);
	}

	/**
	 * ���ñ����Զ�����5
	 * 
	 * @param vbdef5
	 *            �����Զ�����5
	 */
	public void setVbdef5(String vbdef5) {
		this.setAttributeValue(Rateperiod.VBDEF5, vbdef5);
	}

	/**
	 * ��ȡ��ע
	 * 
	 * @return ��ע
	 */
	public String getVnote() {
		return (String) this.getAttributeValue(Rateperiod.VNOTE);
	}

	/**
	 * ���ñ�ע
	 * 
	 * @param vnote
	 *            ��ע
	 */
	public void setVnote(String vnote) {
		this.setAttributeValue(Rateperiod.VNOTE, vnote);
	}

	/**
	 * ��ȡƱ��������
	 * 
	 * @return Ʊ��������
	 */
	public UFDouble getYear_rate() {
		return (UFDouble) this.getAttributeValue(Rateperiod.YEAR_RATE);
	}

	/**
	 * ����Ʊ��������
	 * 
	 * @param year_rate
	 *            Ʊ��������
	 */
	public void setYear_rate(UFDouble year_rate) {
		this.setAttributeValue(Rateperiod.YEAR_RATE, year_rate);
	}

	
	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_sim.sim_rateperiod");
	}

	
	@Override
	public String getParentPKFieldName() {
		return "pk_interest";
	}
}