package nc.vo.fba_smt.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 可融证券增减结存明细表VO
 */

public class AvailableDetailVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String pk_org;
	private String pk_group;
	private String pk_selfsgroup;
	private String pk_partnaccount;
	private String pk_glorgbook;// 账簿
	private String pk_sclassify;// 证券分类
	private String pk_securities;// 证券

	private String securitiescode;// 证券编码
	private String securitiesname;// 证券名称

	private String pk_assetsprop;// 资产属性
	private String pk_stocksort;// 库存组织
	private String pk_capaccount;// 资金账号
	private UFDate trade_date;// 交易日期

	// 期初
	private UFDouble firstnum;// 可融库期初数量
	private UFDouble firstsum;// 可融库期初金额
	private UFDouble firstMarket;// 期初市值
	// 本期增加
	private UFDouble kgzkrnum;// 可供转可融数量
	private UFDouble kgzkrsum;// 可供转可融金额

	private UFDouble zrtjrnum;// 转融通借入数量
	private UFDouble zrtjrsum;// 转融通借入金额

	private UFDouble rcghnum;// 融出归还数量
	private UFDouble rcghsum;// 融出归还金额

	private UFDouble hgqyhznum;// 红股券源划转数量
	private UFDouble hgqyhzsum;// 红股券源划转金额
	// 本期减少
	private UFDouble rczqnum;// 融出证券数量
	private UFDouble rczqsum;// 融出证券金额

	private UFDouble krzkgnum;// 可融转可供数量
	private UFDouble krzkgsum;// 可融转可供金额

	private UFDouble zrtghnum;// 转融通归还数量
	private UFDouble zrtghsum;// 转融通归还金额

	private UFDouble hgrcnum;// 红股融出数量
	private UFDouble hgrcsum;// 红股融出金额

	// 期末结存
	private UFDouble endnum;// 可融库期末结存数量
	private UFDouble endsum;// 可融库期末结存金额

	// 期末结存盈亏
	private UFDouble closeprice;// 收盘价
	private UFDouble marketValue;// 证券市值
	private UFDouble profit;// 盈亏

	public UFDouble getFirstMarket() {
		return firstMarket;
	}

	public void setFirstMarket(UFDouble firstMarket) {
		this.firstMarket = firstMarket;
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

	public String getPk_sclassify() {
		return pk_sclassify;
	}

	public void setPk_sclassify(String pk_sclassify) {
		this.pk_sclassify = pk_sclassify;
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

	public String getPk_assetsprop() {
		return pk_assetsprop;
	}

	public void setPk_assetsprop(String pk_assetsprop) {
		this.pk_assetsprop = pk_assetsprop;
	}

	public String getPk_stocksort() {
		return pk_stocksort;
	}

	public void setPk_stocksort(String pk_stocksort) {
		this.pk_stocksort = pk_stocksort;
	}

	public String getPk_capaccount() {
		return pk_capaccount;
	}

	public void setPk_capaccount(String pk_capaccount) {
		this.pk_capaccount = pk_capaccount;
	}

	public UFDate getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(UFDate trade_date) {
		this.trade_date = trade_date;
	}

	public UFDouble getFirstnum() {
		return firstnum;
	}

	public void setFirstnum(UFDouble firstnum) {
		this.firstnum = firstnum;
	}

	public UFDouble getFirstsum() {
		return firstsum;
	}

	public void setFirstsum(UFDouble firstsum) {
		this.firstsum = firstsum;
	}

	public UFDouble getKgzkrnum() {
		return kgzkrnum;
	}

	public void setKgzkrnum(UFDouble kgzkrnum) {
		this.kgzkrnum = kgzkrnum;
	}

	public UFDouble getKgzkrsum() {
		return kgzkrsum;
	}

	public void setKgzkrsum(UFDouble kgzkrsum) {
		this.kgzkrsum = kgzkrsum;
	}

	public UFDouble getZrtjrnum() {
		return zrtjrnum;
	}

	public void setZrtjrnum(UFDouble zrtjrnum) {
		this.zrtjrnum = zrtjrnum;
	}

	public UFDouble getZrtjrsum() {
		return zrtjrsum;
	}

	public void setZrtjrsum(UFDouble zrtjrsum) {
		this.zrtjrsum = zrtjrsum;
	}

	public UFDouble getRcghnum() {
		return rcghnum;
	}

	public void setRcghnum(UFDouble rcghnum) {
		this.rcghnum = rcghnum;
	}

	public UFDouble getRcghsum() {
		return rcghsum;
	}

	public void setRcghsum(UFDouble rcghsum) {
		this.rcghsum = rcghsum;
	}

	public UFDouble getHgqyhznum() {
		return hgqyhznum;
	}

	public void setHgqyhznum(UFDouble hgqyhznum) {
		this.hgqyhznum = hgqyhznum;
	}

	public UFDouble getHgqyhzsum() {
		return hgqyhzsum;
	}

	public void setHgqyhzsum(UFDouble hgqyhzsum) {
		this.hgqyhzsum = hgqyhzsum;
	}

	public UFDouble getRczqnum() {
		return rczqnum;
	}

	public void setRczqnum(UFDouble rczqnum) {
		this.rczqnum = rczqnum;
	}

	public UFDouble getRczqsum() {
		return rczqsum;
	}

	public void setRczqsum(UFDouble rczqsum) {
		this.rczqsum = rczqsum;
	}

	public UFDouble getKrzkgnum() {
		return krzkgnum;
	}

	public void setKrzkgnum(UFDouble krzkgnum) {
		this.krzkgnum = krzkgnum;
	}

	public UFDouble getKrzkgsum() {
		return krzkgsum;
	}

	public void setKrzkgsum(UFDouble krzkgsum) {
		this.krzkgsum = krzkgsum;
	}

	public UFDouble getZrtghnum() {
		return zrtghnum;
	}

	public void setZrtghnum(UFDouble zrtghnum) {
		this.zrtghnum = zrtghnum;
	}

	public UFDouble getZrtghsum() {
		return zrtghsum;
	}

	public void setZrtghsum(UFDouble zrtghsum) {
		this.zrtghsum = zrtghsum;
	}

	public UFDouble getHgrcnum() {
		return hgrcnum;
	}

	public void setHgrcnum(UFDouble hgrcnum) {
		this.hgrcnum = hgrcnum;
	}

	public UFDouble getHgrcsum() {
		return hgrcsum;
	}

	public void setHgrcsum(UFDouble hgrcsum) {
		this.hgrcsum = hgrcsum;
	}

	public UFDouble getEndnum() {
		return endnum;
	}

	public void setEndnum(UFDouble endnum) {
		this.endnum = endnum;
	}

	public UFDouble getEndsum() {
		return endsum;
	}

	public void setEndsum(UFDouble endsum) {
		this.endsum = endsum;
	}

	public UFDouble getCloseprice() {
		return closeprice;
	}

	public void setCloseprice(UFDouble closeprice) {
		this.closeprice = closeprice;
	}

	public UFDouble getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(UFDouble marketValue) {
		this.marketValue = marketValue;
	}

	public UFDouble getProfit() {
		return profit;
	}

	public void setProfit(UFDouble profit) {
		this.profit = profit;
	}

	public String getPk_securities() {
		return pk_securities;
	}

	public void setPk_securities(String pk_securities) {
		this.pk_securities = pk_securities;
	}

	public String getPk_selfsgroup() {
		return pk_selfsgroup;
	}

	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.pk_selfsgroup = pk_selfsgroup;
	}

	public String getPk_partnaccount() {
		return pk_partnaccount;
	}

	public void setPk_partnaccount(String pk_partnaccount) {
		this.pk_partnaccount = pk_partnaccount;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
