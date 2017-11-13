package nc.impl.fba_zqjd.trade.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class StockChangeVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 资金账号
	 */
	private String pk_capaccount;
	/**
	 * 交易证券
	 */
	private String pk_securities;
	/**
	 * 期初库存融入数量
	 */
	private UFDouble start_bow_num;
	/**
	 * 期初库存卖出数量
	 */
	private UFDouble start_sell_num;
	/**
	 * 期初库存负债金额
	 */
	private UFDouble start_debt;
	/**
	 * 本期融入
	 */
	private UFDouble now_bow;
	/**
	 * 本期融入归还
	 */
	private UFDouble now_rtn;
	/**
	 * 本期卖出数量
	 */
	private UFDouble now_sell_num;
	/**
	 * 本期卖出金额
	 */
	private UFDouble now_sell_sum;
	/**
	 * 本期买入数量
	 */
	private UFDouble now_buy_num;
	/**
	 * 本期买入金额
	 */
	private UFDouble now_buy_sum;
	/**
	 * 本期负债成本
	 */
	private UFDouble now_debt;
	/**
	 * 收到兑付利息
	 */
	private UFDouble rec_interest;
	/**
	 * 转付兑付利息
	 */
	private UFDouble turn_interest;
	/**
	 * 期末库存融入数量
	 */
	private UFDouble end_bow_num;
	/**
	 * 期末库存卖出数量
	 */
	private UFDouble end_sell_num;
	/**
	 * 期末库存负债金额
	 */
	private UFDouble end_debt;

	/**
	 * 开始日期
	 */
	private UFDateTime begin_date;
	/**
	 * 结束日期
	 */
	private UFDateTime end_date;
	/**
	 * 资产属性
	 */
	private String pk_assetsprop;
	/**
	 * 集团
	 */
	private String pk_group;
	/**
	 * 组织
	 */
	private String pk_org;
	/**
	 * 库存组织
	 */
	private String pk_stocksort;
	public String getPk_capaccount() {
		return pk_capaccount;
	}
	public void setPk_capaccount(String pk_capaccount) {
		this.pk_capaccount = pk_capaccount;
	}
	public String getPk_securities() {
		return pk_securities;
	}
	public void setPk_securities(String pk_securities) {
		this.pk_securities = pk_securities;
	}
	public UFDouble getStart_bow_num() {
		return start_bow_num;
	}
	public void setStart_bow_num(UFDouble start_bow_num) {
		this.start_bow_num = start_bow_num;
	}
	public UFDouble getStart_sell_num() {
		return start_sell_num;
	}
	public void setStart_sell_num(UFDouble start_sell_num) {
		this.start_sell_num = start_sell_num;
	}
	public UFDouble getStart_debt() {
		return start_debt;
	}
	public void setStart_debt(UFDouble start_debt) {
		this.start_debt = start_debt;
	}
	public UFDouble getNow_bow() {
		return now_bow;
	}
	public void setNow_bow(UFDouble now_bow) {
		this.now_bow = now_bow;
	}
	public UFDouble getNow_rtn() {
		return now_rtn;
	}
	public void setNow_rtn(UFDouble now_rtn) {
		this.now_rtn = now_rtn;
	}
	public UFDouble getNow_sell_num() {
		return now_sell_num;
	}
	public void setNow_sell_num(UFDouble now_sell_num) {
		this.now_sell_num = now_sell_num;
	}
	public UFDouble getNow_sell_sum() {
		return now_sell_sum;
	}
	public void setNow_sell_sum(UFDouble now_sell_sum) {
		this.now_sell_sum = now_sell_sum;
	}
	public UFDouble getNow_buy_num() {
		return now_buy_num;
	}
	public void setNow_buy_num(UFDouble now_buy_num) {
		this.now_buy_num = now_buy_num;
	}
	public UFDouble getNow_buy_sum() {
		return now_buy_sum;
	}
	public void setNow_buy_sum(UFDouble now_buy_sum) {
		this.now_buy_sum = now_buy_sum;
	}
	public UFDouble getNow_debt() {
		return now_debt;
	}
	public void setNow_debt(UFDouble now_debt) {
		this.now_debt = now_debt;
	}
	public UFDouble getRec_interest() {
		return rec_interest;
	}
	public void setRec_interest(UFDouble rec_interest) {
		this.rec_interest = rec_interest;
	}
	public UFDouble getTurn_interest() {
		return turn_interest;
	}
	public void setTurn_interest(UFDouble turn_interest) {
		this.turn_interest = turn_interest;
	}
	public UFDouble getEnd_bow_num() {
		return end_bow_num;
	}
	public void setEnd_bow_num(UFDouble end_bow_num) {
		this.end_bow_num = end_bow_num;
	}
	public UFDouble getEnd_sell_num() {
		return end_sell_num;
	}
	public void setEnd_sell_num(UFDouble end_sell_num) {
		this.end_sell_num = end_sell_num;
	}
	public UFDouble getEnd_debt() {
		return end_debt;
	}
	public void setEnd_debt(UFDouble end_debt) {
		this.end_debt = end_debt;
	}
	public UFDateTime getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(UFDateTime begin_date) {
		this.begin_date = begin_date;
	}
	public UFDateTime getEnd_date() {
		return end_date;
	}
	public void setEnd_date(UFDateTime end_date) {
		this.end_date = end_date;
	}
	public String getPk_assetsprop() {
		return pk_assetsprop;
	}
	public void setPk_assetsprop(String pk_assetsprop) {
		this.pk_assetsprop = pk_assetsprop;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getPk_stocksort() {
		return pk_stocksort;
	}
	public void setPk_stocksort(String pk_stocksort) {
		this.pk_stocksort = pk_stocksort;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pk_capaccount == null) ? 0 : pk_capaccount.hashCode());
		result = prime * result
				+ ((pk_group == null) ? 0 : pk_group.hashCode());
		result = prime * result + ((pk_org == null) ? 0 : pk_org.hashCode());
		result = prime * result
				+ ((pk_securities == null) ? 0 : pk_securities.hashCode());
		result = prime * result
				+ ((pk_stocksort == null) ? 0 : pk_stocksort.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockChangeVO other = (StockChangeVO) obj;
		if (pk_capaccount == null) {
			if (other.pk_capaccount != null)
				return false;
		} else if (!pk_capaccount.equals(other.pk_capaccount))
			return false;
		if (pk_group == null) {
			if (other.pk_group != null)
				return false;
		} else if (!pk_group.equals(other.pk_group))
			return false;
		if (pk_org == null) {
			if (other.pk_org != null)
				return false;
		} else if (!pk_org.equals(other.pk_org))
			return false;
		if (pk_securities == null) {
			if (other.pk_securities != null)
				return false;
		} else if (!pk_securities.equals(other.pk_securities))
			return false;
		if (pk_stocksort == null) {
			if (other.pk_stocksort != null)
				return false;
		} else if (!pk_stocksort.equals(other.pk_stocksort))
			return false;
		return true;
	}
	

}
