package nc.vo.fba_zqjd.trade.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * @author ZQ
 * 
 */
public class NewStockChangeVO implements Serializable {
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
	 * 期初实际数量
	 */
	private UFDouble QCactual_num;
	/**
	 * 期初负债数量
	 */
	private UFDouble QCbargain_num;
	/**
	 * 期初库存负债金额
	 */
	private UFDouble start_debt;
	/**
	 * 负债市值
	 */
	private UFDouble QCfzsz;
	/**
	 * 应收利息
	 */
	private UFDouble QCinterest;
	/**
	 * 本期融入
	 */
	private UFDouble now_bow;
	/**
	 * 本期融入归还
	 */
	private UFDouble now_rtn;
	/**
	 * 本期计提应收利息
	 */
	private UFDouble BQJT;
	/**
	 * 本期卖出数量
	 */
	private UFDouble now_sell_num;
	/**
	 * 本期卖出金额
	 */
	private UFDouble now_sell_sum;
	/**
	 * 应收利息
	 */
	private UFDouble BQinterest;
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
	 * 结转应收利息
	 */
	private UFDouble BQJZ;
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
	 * 期末实际数量
	 */
	private UFDouble QMactual_num;
	/**
	 * 期末负债数量
	 */
	private UFDouble QMbargain_num;
	/**
	 * 期末库存负债金额
	 */
	private UFDouble end_debt;
	/**
	 * 负债市值
	 */
	private UFDouble QMfzsz;
	/**
	 * 应收利息
	 */
	private UFDouble QMinterest;
	/**
	 * 累计损益
	 */
	private UFDouble LJSY;
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

	public UFDouble getQCactual_num() {
		return QCactual_num;
	}

	public void setQCactual_num(UFDouble qCactual_num) {
		QCactual_num = qCactual_num;
	}

	public UFDouble getQCbargain_num() {
		return QCbargain_num;
	}

	public void setQCbargain_num(UFDouble qCbargain_num) {
		QCbargain_num = qCbargain_num;
	}

	public UFDouble getStart_debt() {
		return start_debt;
	}

	public void setStart_debt(UFDouble start_debt) {
		this.start_debt = start_debt;
	}

	public UFDouble getQCfzsz() {
		return QCfzsz;
	}

	public void setQCfzsz(UFDouble qCfzsz) {
		QCfzsz = qCfzsz;
	}

	public UFDouble getQCinterest() {
		return QCinterest;
	}

	public void setQCinterest(UFDouble qCinterest) {
		QCinterest = qCinterest;
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

	public UFDouble getBQJT() {
		return BQJT;
	}

	public void setBQJT(UFDouble bQJT) {
		BQJT = bQJT;
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

	public UFDouble getBQinterest() {
		return BQinterest;
	}

	public void setBQinterest(UFDouble bQinterest) {
		BQinterest = bQinterest;
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

	public UFDouble getBQJZ() {
		return BQJZ;
	}

	public void setBQJZ(UFDouble bQJZ) {
		BQJZ = bQJZ;
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

	public UFDouble getQMactual_num() {
		return QMactual_num;
	}

	public void setQMactual_num(UFDouble qMactual_num) {
		QMactual_num = qMactual_num;
	}

	public UFDouble getQMbargain_num() {
		return QMbargain_num;
	}

	public void setQMbargain_num(UFDouble qMbargain_num) {
		QMbargain_num = qMbargain_num;
	}

	public UFDouble getEnd_debt() {
		return end_debt;
	}

	public void setEnd_debt(UFDouble end_debt) {
		this.end_debt = end_debt;
	}

	public UFDouble getQMfzsz() {
		return QMfzsz;
	}

	public void setQMfzsz(UFDouble qMfzsz) {
		QMfzsz = qMfzsz;
	}

	public UFDouble getQMinterest() {
		return QMinterest;
	}

	public void setQMinterest(UFDouble qMinterest) {
		QMinterest = qMinterest;
	}

	public UFDouble getLJSY() {
		return LJSY;
	}

	public void setLJSY(UFDouble lJSY) {
		LJSY = lJSY;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		NewStockChangeVO other = (NewStockChangeVO) obj;
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
