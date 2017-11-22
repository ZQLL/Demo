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
	 * 天数
	 */
	public static final String DAYS_NUM = "days_num";
	/**
	 * 结束日期
	 */
	public static final String END_DAY = "end_day";
	/**
	 * 表体自定义数值1
	 */
	public static final String NBDEF1 = "nbdef1";
	/**
	 * 表体自定义数值2
	 */
	public static final String NBDEF2 = "nbdef2";
	/**
	 * 表体自定义数值3
	 */
	public static final String NBDEF3 = "nbdef3";
	/**
	 * 表体自定义数值4
	 */
	public static final String NBDEF4 = "nbdef4";
	/**
	 * 表体自定义数值5
	 */
	public static final String NBDEF5 = "nbdef5";
	/**
	 * 上层单据主键
	 */
	public static final String PK_INTEREST = "pk_interest";
	/**
	 * 子表主键
	 */
	public static final String PK_RATEPERIOD = "pk_rateperiod";
	/**
	 * 行号
	 */
	public static final String ROWNO = "rowno";
	/**
	 * 开始日期
	 */
	public static final String START_DAY = "start_day";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";
	/**
	 * 表体自定义项1
	 */
	public static final String VBDEF1 = "vbdef1";
	/**
	 * 表体自定义项2
	 */
	public static final String VBDEF2 = "vbdef2";
	/**
	 * 表体自定义项3
	 */
	public static final String VBDEF3 = "vbdef3";
	/**
	 * 表体自定义项4
	 */
	public static final String VBDEF4 = "vbdef4";
	/**
	 * 表体自定义项5
	 */
	public static final String VBDEF5 = "vbdef5";
	/**
	 * 备注
	 */
	public static final String VNOTE = "vnote";
	/**
	 * 票面年利率
	 */
	public static final String YEAR_RATE = "year_rate";
	/**
	 * 提前还款比例
	 */
	public static final String PAYPERCENT = "paypercent";

	/**
	 * 提前还款比例
	 */
	public void setPaypercent(UFDouble paypercent) {
		this.setAttributeValue(Rateperiod.PAYPERCENT, paypercent);
	}

	/**
	 * 提前还款比例
	 */
	public UFDouble getPaypercent() {
		return (UFDouble) this.getAttributeValue(Rateperiod.PAYPERCENT);
	}

	/**
	 * 获取天数
	 * 
	 * @return 天数
	 */
	public Integer getDays_num() {
		return (Integer) this.getAttributeValue(Rateperiod.DAYS_NUM);
	}

	/**
	 * 设置天数
	 * 
	 * @param days_num
	 *            天数
	 */
	public void setDays_num(Integer days_num) {
		this.setAttributeValue(Rateperiod.DAYS_NUM, days_num);
	}

	/**
	 * 获取结束日期
	 * 
	 * @return 结束日期
	 */
	public UFDate getEnd_day() {
		return (UFDate) this.getAttributeValue(Rateperiod.END_DAY);
	}

	/**
	 * 设置结束日期
	 * 
	 * @param end_day
	 *            结束日期
	 */
	public void setEnd_day(UFDate end_day) {
		this.setAttributeValue(Rateperiod.END_DAY, end_day);
	}

	/**
	 * 获取表体自定义数值1
	 * 
	 * @return 表体自定义数值1
	 */
	public UFDouble getNbdef1() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF1);
	}

	/**
	 * 设置表体自定义数值1
	 * 
	 * @param nbdef1
	 *            表体自定义数值1
	 */
	public void setNbdef1(UFDouble nbdef1) {
		this.setAttributeValue(Rateperiod.NBDEF1, nbdef1);
	}

	/**
	 * 获取表体自定义数值2
	 * 
	 * @return 表体自定义数值2
	 */
	public UFDouble getNbdef2() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF2);
	}

	/**
	 * 设置表体自定义数值2
	 * 
	 * @param nbdef2
	 *            表体自定义数值2
	 */
	public void setNbdef2(UFDouble nbdef2) {
		this.setAttributeValue(Rateperiod.NBDEF2, nbdef2);
	}

	/**
	 * 获取表体自定义数值3
	 * 
	 * @return 表体自定义数值3
	 */
	public UFDouble getNbdef3() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF3);
	}

	/**
	 * 设置表体自定义数值3
	 * 
	 * @param nbdef3
	 *            表体自定义数值3
	 */
	public void setNbdef3(UFDouble nbdef3) {
		this.setAttributeValue(Rateperiod.NBDEF3, nbdef3);
	}

	/**
	 * 获取表体自定义数值4
	 * 
	 * @return 表体自定义数值4
	 */
	public UFDouble getNbdef4() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF4);
	}

	/**
	 * 设置表体自定义数值4
	 * 
	 * @param nbdef4
	 *            表体自定义数值4
	 */
	public void setNbdef4(UFDouble nbdef4) {
		this.setAttributeValue(Rateperiod.NBDEF4, nbdef4);
	}

	/**
	 * 获取表体自定义数值5
	 * 
	 * @return 表体自定义数值5
	 */
	public UFDouble getNbdef5() {
		return (UFDouble) this.getAttributeValue(Rateperiod.NBDEF5);
	}

	/**
	 * 设置表体自定义数值5
	 * 
	 * @param nbdef5
	 *            表体自定义数值5
	 */
	public void setNbdef5(UFDouble nbdef5) {
		this.setAttributeValue(Rateperiod.NBDEF5, nbdef5);
	}

	/**
	 * 获取上层单据主键
	 * 
	 * @return 上层单据主键
	 */
	public String getPk_interest() {
		return (String) this.getAttributeValue(Rateperiod.PK_INTEREST);
	}

	/**
	 * 设置上层单据主键
	 * 
	 * @param pk_interest
	 *            上层单据主键
	 */
	public void setPk_interest(String pk_interest) {
		this.setAttributeValue(Rateperiod.PK_INTEREST, pk_interest);
	}

	/**
	 * 获取子表主键
	 * 
	 * @return 子表主键
	 */
	public String getPk_rateperiod() {
		return (String) this.getAttributeValue(Rateperiod.PK_RATEPERIOD);
	}

	/**
	 * 设置子表主键
	 * 
	 * @param pk_rateperiod
	 *            子表主键
	 */
	public void setPk_rateperiod(String pk_rateperiod) {
		this.setAttributeValue(Rateperiod.PK_RATEPERIOD, pk_rateperiod);
	}

	/**
	 * 获取行号
	 * 
	 * @return 行号
	 */
	public String getRowno() {
		return (String) this.getAttributeValue(Rateperiod.ROWNO);
	}

	/**
	 * 设置行号
	 * 
	 * @param rowno
	 *            行号
	 */
	public void setRowno(String rowno) {
		this.setAttributeValue(Rateperiod.ROWNO, rowno);
	}

	/**
	 * 获取开始日期
	 * 
	 * @return 开始日期
	 */
	public UFDate getStart_day() {
		return (UFDate) this.getAttributeValue(Rateperiod.START_DAY);
	}

	/**
	 * 设置开始日期
	 * 
	 * @param start_day
	 *            开始日期
	 */
	public void setStart_day(UFDate start_day) {
		this.setAttributeValue(Rateperiod.START_DAY, start_day);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(Rateperiod.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(Rateperiod.TS, ts);
	}

	/**
	 * 获取表体自定义项1
	 * 
	 * @return 表体自定义项1
	 */
	public String getVbdef1() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF1);
	}

	/**
	 * 设置表体自定义项1
	 * 
	 * @param vbdef1
	 *            表体自定义项1
	 */
	public void setVbdef1(String vbdef1) {
		this.setAttributeValue(Rateperiod.VBDEF1, vbdef1);
	}

	/**
	 * 获取表体自定义项2
	 * 
	 * @return 表体自定义项2
	 */
	public String getVbdef2() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF2);
	}

	/**
	 * 设置表体自定义项2
	 * 
	 * @param vbdef2
	 *            表体自定义项2
	 */
	public void setVbdef2(String vbdef2) {
		this.setAttributeValue(Rateperiod.VBDEF2, vbdef2);
	}

	/**
	 * 获取表体自定义项3
	 * 
	 * @return 表体自定义项3
	 */
	public String getVbdef3() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF3);
	}

	/**
	 * 设置表体自定义项3
	 * 
	 * @param vbdef3
	 *            表体自定义项3
	 */
	public void setVbdef3(String vbdef3) {
		this.setAttributeValue(Rateperiod.VBDEF3, vbdef3);
	}

	/**
	 * 获取表体自定义项4
	 * 
	 * @return 表体自定义项4
	 */
	public String getVbdef4() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF4);
	}

	/**
	 * 设置表体自定义项4
	 * 
	 * @param vbdef4
	 *            表体自定义项4
	 */
	public void setVbdef4(String vbdef4) {
		this.setAttributeValue(Rateperiod.VBDEF4, vbdef4);
	}

	/**
	 * 获取表体自定义项5
	 * 
	 * @return 表体自定义项5
	 */
	public String getVbdef5() {
		return (String) this.getAttributeValue(Rateperiod.VBDEF5);
	}

	/**
	 * 设置表体自定义项5
	 * 
	 * @param vbdef5
	 *            表体自定义项5
	 */
	public void setVbdef5(String vbdef5) {
		this.setAttributeValue(Rateperiod.VBDEF5, vbdef5);
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getVnote() {
		return (String) this.getAttributeValue(Rateperiod.VNOTE);
	}

	/**
	 * 设置备注
	 * 
	 * @param vnote
	 *            备注
	 */
	public void setVnote(String vnote) {
		this.setAttributeValue(Rateperiod.VNOTE, vnote);
	}

	/**
	 * 获取票面年利率
	 * 
	 * @return 票面年利率
	 */
	public UFDouble getYear_rate() {
		return (UFDouble) this.getAttributeValue(Rateperiod.YEAR_RATE);
	}

	/**
	 * 设置票面年利率
	 * 
	 * @param year_rate
	 *            票面年利率
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