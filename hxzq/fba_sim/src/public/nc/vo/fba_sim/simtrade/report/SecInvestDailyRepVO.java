package nc.vo.fba_sim.simtrade.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class SecInvestDailyRepVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	private String pk_org;
	private String pk_group;
	private String pk_glorgbook;
	private String pk_sclassify = null;
	private String pk_capaccount = null;
	private String pk_selfsgroup = null;
	private String pk_partnaccount = null;
	private String pk_operatesite = null;
	private String pk_client = null;
	private String securitiescode = null;
	private String securitiesname = null;
	private UFDouble firstNum = new UFDouble(0);
	private UFDouble firstSum = new UFDouble(0);
	private UFDouble firstPrice = new UFDouble(0);
	private UFDouble buyNum = new UFDouble(0);
	private UFDouble buySum = new UFDouble(0);
	private UFDouble buyPrice = new UFDouble(0);
	private UFDouble sellNum = new UFDouble(0);
	private UFDouble sellSum = new UFDouble(0);
	private UFDouble sellPrice = new UFDouble(0);
	private UFDouble sellCost = new UFDouble(0);
	private UFDouble endNum = new UFDouble(0);
	private UFDouble endSum = new UFDouble(0);
	private UFDouble endPrice = new UFDouble(0);
	private UFDouble localwin = new UFDouble(0);
	private UFDouble marketPrice = new UFDouble(0);
	private UFDouble marketSum = new UFDouble(0);
	private UFDouble profitLoss = new UFDouble(0);
	private UFDate begin_date = null;
	private UFDate end_date = null;
	private String pk_securities = null;
	private UFDate trade_date = null;
	private UFDouble gyz = null;// 公允值
	private UFDouble zgbzb = null;// 期末库存数量占总股本比例

	/*********** added by cwj 2011-09-23 ********************/
	private String pk_assetsprop;
	private String pk_stocksort;

	/** JINGQT 2015年7月15日 投资经营日报中添加红股入账，并将其数据添加入本期收益中 ADD */
	private UFDouble bonus = new UFDouble(0.0);

	/**
	 * JINGQT 2015年8月19日 证券投资经营日报的增加公允价值变动列 公允价值变动=‘期末结存期末市值’-‘期末结存金额’；ADD START
	 */
	private UFDouble fairValueChange = new UFDouble(0.0);

	/** JINGQT 2015年7月15日 证券投资经营日报的增加公允价值变动列 公允价值变动=‘期末结存期末市值’-‘期末结存金额’； ADD END */

	// 期初市值 add by lihaibo 2017-05-08
	private UFDouble first_marketSum = new UFDouble(0);
	// 份额分红 add by lihaibo 2017-06-13
	private UFDouble fefh = new UFDouble(0);

	public UFDouble getFefh() {
		return fefh;
	}

	public void setFefh(UFDouble fefh) {
		this.fefh = fefh;
	}

	public UFDouble getFirst_marketSum() {
		return first_marketSum;
	}

	public void setFirst_marketSum(UFDouble first_marketSum) {
		this.first_marketSum = first_marketSum;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_glorgbook() {
		return pk_glorgbook;
	}

	public void setPk_glorgbook(String pk_glorgbook) {
		this.pk_glorgbook = pk_glorgbook;
	}

	public UFDouble getGyz() {
		return gyz;
	}

	public void setGyz(UFDouble gyz) {
		this.gyz = gyz;
	}

	public UFDouble getZgbzb() {
		return zgbzb;
	}

	public void setZgbzb(UFDouble zgbzb) {
		this.zgbzb = zgbzb;
	}

	public UFDate getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(UFDate trade_date) {
		this.trade_date = trade_date;
	}

	public String getPk_securities() {
		return pk_securities;
	}

	public void setPk_securities(String pk_securities) {
		this.pk_securities = pk_securities;
	}

	public UFDate getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(UFDate begin_date) {
		this.begin_date = begin_date;
	}

	public UFDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(UFDate end_date) {
		this.end_date = end_date;
	}

	public UFDouble getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(UFDouble buyNum) {
		this.buyNum = buyNum;
	}

	public UFDouble getBuySum() {
		return buySum;
	}

	public void setBuySum(UFDouble buySum) {
		this.buySum = buySum;
	}

	public UFDouble getEndNum() {
		return endNum;
	}

	public void setEndNum(UFDouble endNum) {
		this.endNum = endNum;
	}

	public UFDouble getEndSum() {
		return endSum;
	}

	public void setEndSum(UFDouble endSum) {
		this.endSum = endSum;
	}

	public UFDouble getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(UFDouble firstNum) {
		this.firstNum = firstNum;
	}

	public UFDouble getFirstSum() {
		return firstSum;
	}

	public void setFirstSum(UFDouble firstSum) {
		this.firstSum = firstSum;
	}

	public UFDouble getLocalwin() {
		return localwin;
	}

	public void setLocalwin(UFDouble localwin) {
		this.localwin = localwin;
	}

	public UFDouble getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(UFDouble marketPrice) {
		this.marketPrice = marketPrice;
	}

	public UFDouble getMarketSum() {
		return marketSum;
	}

	public void setMarketSum(UFDouble marketSum) {
		this.marketSum = marketSum;
	}

	public void setProfitLoss(UFDouble profitLoss) {
		this.profitLoss = profitLoss;
	}

	public void setSellCost(UFDouble sellCost) {
		this.sellCost = sellCost;
	}

	public void setSellNum(UFDouble sellNum) {
		this.sellNum = sellNum;
	}

	public void setSellSum(UFDouble sellSum) {
		this.sellSum = sellSum;
	}

	public String getPk_capaccount() {
		return pk_capaccount;
	}

	public void setPk_capaccount(String pk_capaccount) {
		this.pk_capaccount = pk_capaccount;
	}

	public String getPk_client() {
		return pk_client;
	}

	public void setPk_client(String pk_client) {
		this.pk_client = pk_client;
	}

	public String getPk_operatesite() {
		return pk_operatesite;
	}

	public void setPk_operatesite(String pk_operatesite) {
		this.pk_operatesite = pk_operatesite;
	}

	public String getPk_partnaccount() {
		return pk_partnaccount;
	}

	public void setPk_partnaccount(String pk_partnaccount) {
		this.pk_partnaccount = pk_partnaccount;
	}

	public String getPk_sclassify() {
		return pk_sclassify;
	}

	public void setPk_sclassify(String pk_sclassify) {
		this.pk_sclassify = pk_sclassify;
	}

	public String getPk_selfsgroup() {
		return pk_selfsgroup;
	}

	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.pk_selfsgroup = pk_selfsgroup;
	}

	public UFDouble getProfitLoss() {
		return profitLoss;
	}

	public UFDouble getSellCost() {
		return sellCost;
	}

	public UFDouble getSellNum() {
		return sellNum;
	}

	public UFDouble getSellSum() {
		return sellSum;
	}

	public String getSecuritiescode() {
		return securitiescode;
	}

	public void setSecuritiescode(String securitiescode) {
		this.securitiescode = securitiescode;
	}

	public String getSecuritiesname() {
		return securitiesname;
	}

	public void setSecuritiesname(String securitiesname) {
		this.securitiesname = securitiesname;
	}

	@Override
	public String getPKFieldName() {

		return null;
	}

	@Override
	public String getParentPKFieldName() {

		return null;
	}

	@Override
	public String getTableName() {

		return null;
	}

	public String getPk_assetsprop() {
		return pk_assetsprop;
	}

	public void setPk_assetsprop(String pk_assetsprop) {
		this.pk_assetsprop = pk_assetsprop;
	}

	public UFDouble getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(UFDouble firstPrice) {
		this.firstPrice = firstPrice;
	}

	public UFDouble getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(UFDouble buyPrice) {
		this.buyPrice = buyPrice;
	}

	public UFDouble getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(UFDouble sellPrice) {
		this.sellPrice = sellPrice;
	}

	public UFDouble getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(UFDouble endPrice) {
		this.endPrice = endPrice;
	}

	public String getPk_stocksort() {
		return pk_stocksort;
	}

	public void setPk_stocksort(String pk_stocksort) {
		this.pk_stocksort = pk_stocksort;
	}

	/**
	 * @return 红股入账
	 */
	public UFDouble getBonus() {
		return bonus;
	}

	/**
	 * @param bonus
	 *            红股入账
	 */
	public void setBonus(UFDouble bonus) {
		this.bonus = bonus;
	}

	/**
	 * @return 公允价值变动
	 */
	public UFDouble getFairValueChange() {
		return fairValueChange;
	}

	/**
	 * @param fairValueChange
	 *            公允价值变动
	 */
	public void setFairValueChange(UFDouble fairValueChange) {
		this.fairValueChange = fairValueChange;
	}
}
