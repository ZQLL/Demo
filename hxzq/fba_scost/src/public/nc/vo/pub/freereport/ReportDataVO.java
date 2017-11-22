package nc.vo.pub.freereport;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;

/**
 * 报表查询交易记录时使用的VO，供插件类查询填充使用，最后复制到报表VO中。
 * 
 * @version V5.0
 * @author zhengwzha
 * @date 2011-12-1
 */
public class ReportDataVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String pk_org;

	private String pk_group;

	private String pk_glorgbook;

	private String pk_billtype;

	private String pk_securities;

	private String pk_capaccount;

	private String pk_selfsgroup;

	private String pk_partnaccount;

	private String pk_operatesite;

	private String pk_client;

	private String pk_assetsprop;

	private String pk_stocksort;

	private String pk_stocksort2;

	private String pk_property;

	private String pk_sclassify;

	private UFLiteralDate begin_date;

	private UFLiteralDate end_date;

	private UFDate trade_date;

	private UFDouble buynum;

	private UFDouble buysum;

	private UFDouble buyaccrual;

	private UFDouble innum;

	private UFDouble insum;

	private UFDouble sellnum;

	private UFDouble sellsum;

	private UFDouble outnum;

	private UFDouble outsum;

	private UFDouble sellaccrual;

	private UFDouble sellcost;

	private UFDouble total_win;

	private UFDouble fact_sum;

	private UFDouble faccrual;

	private UFDouble hcnum;

	private UFDouble hcsum;

	private UFDouble hrnum;

	private UFDouble hrsum;

	private UFDouble num;

	private UFDouble sum;

	private String summary;

	/** JINGQT 2015年7月15日 投资经营日报中添加红股入账，并将其数据添加入本期收益中 ADD */
	private UFDouble bonus = new UFDouble(0.0);

	/**
	 * JINGQT 2015年8月19日 证券投资经营日报的增加公允价值变动列 公允价值变动=‘期末结存期末市值’-‘期末结存金额’；ADD START
	 */
	private UFDouble fairValueChange = new UFDouble(0.0);
	/** JINGQT 2015年7月15日 证券投资经营日报的增加公允价值变动列 公允价值变动=‘期末结存期末市值’-‘期末结存金额’； ADD END */

	private UFDouble lxtz;

	// 份额分红 add by lihaibo 2017-06-13
	private UFDouble fefh = new UFDouble(0);

	public UFDouble getFefh() {
		return fefh;
	}

	public void setFefh(UFDouble fefh) {
		this.fefh = fefh;
	}

	public UFDouble getLxtz() {
		return lxtz;
	}

	public void setLxtz(UFDouble lxtz) {
		this.lxtz = lxtz;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public String getPk_glorgbook() {
		return pk_glorgbook;
	}

	public void setPk_glorgbook(String pk_glorgbook) {
		this.pk_glorgbook = pk_glorgbook;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getPk_securities() {
		return pk_securities;
	}

	public void setPk_securities(String pk_securities) {
		this.pk_securities = pk_securities;
	}

	public String getPk_capaccount() {
		return pk_capaccount;
	}

	public void setPk_capaccount(String pk_capaccount) {
		this.pk_capaccount = pk_capaccount;
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

	public String getPk_operatesite() {
		return pk_operatesite;
	}

	public void setPk_operatesite(String pk_operatesite) {
		this.pk_operatesite = pk_operatesite;
	}

	public String getPk_client() {
		return pk_client;
	}

	public void setPk_client(String pk_client) {
		this.pk_client = pk_client;
	}

	public String getPk_assetsprop() {
		return pk_assetsprop;
	}

	public void setPk_assetsprop(String pk_assetsprop) {
		this.pk_assetsprop = pk_assetsprop;
	}

	public String getPk_property() {
		return pk_property;
	}

	public void setPk_property(String pk_property) {
		this.pk_property = pk_property;
	}

	public String getPk_sclassify() {
		return pk_sclassify;
	}

	public void setPk_sclassify(String pk_sclassify) {
		this.pk_sclassify = pk_sclassify;
	}

	public String getPk_stocksort() {
		return pk_stocksort;
	}

	public void setPk_stocksort(String pk_stocksort) {
		this.pk_stocksort = pk_stocksort;
	}

	public String getPk_stocksort2() {
		return pk_stocksort2;
	}

	public void setPk_stocksort2(String pk_stocksort2) {
		this.pk_stocksort2 = pk_stocksort2;
	}

	public UFLiteralDate getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(UFLiteralDate begin_date) {
		this.begin_date = begin_date;
	}

	public UFLiteralDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(UFLiteralDate end_date) {
		this.end_date = end_date;
	}

	public UFDate getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(UFDate trade_date) {
		this.trade_date = trade_date;
	}

	public UFDouble getBuynum() {
		return buynum;
	}

	public void setBuynum(UFDouble buynum) {
		this.buynum = buynum;
	}

	public UFDouble getBuysum() {
		return buysum;
	}

	public void setBuysum(UFDouble buysum) {
		this.buysum = buysum;
	}

	public UFDouble getBuyaccrual() {
		return buyaccrual;
	}

	public void setBuyaccrual(UFDouble buyaccrual) {
		this.buyaccrual = buyaccrual;
	}

	public UFDouble getSellnum() {
		return sellnum;
	}

	public void setSellnum(UFDouble sellnum) {
		this.sellnum = sellnum;
	}

	public UFDouble getSellsum() {
		return sellsum;
	}

	public void setSellsum(UFDouble sellsum) {
		this.sellsum = sellsum;
	}

	public UFDouble getSellaccrual() {
		return sellaccrual;
	}

	public void setSellaccrual(UFDouble sellaccrual) {
		this.sellaccrual = sellaccrual;
	}

	public UFDouble getSellcost() {
		return sellcost;
	}

	public void setSellcost(UFDouble sellcost) {
		this.sellcost = sellcost;
	}

	public UFDouble getTotal_win() {
		return total_win;
	}

	public void setTotal_win(UFDouble total_win) {
		this.total_win = total_win;
	}

	public UFDouble getFact_sum() {
		return fact_sum;
	}

	public void setFact_sum(UFDouble fact_sum) {
		this.fact_sum = fact_sum;
	}

	public UFDouble getFaccrual() {
		return faccrual;
	}

	public void setFaccrual(UFDouble faccrual) {
		this.faccrual = faccrual;
	}

	public UFDouble getHcnum() {
		return hcnum;
	}

	public void setHcnum(UFDouble hcnum) {
		this.hcnum = hcnum;
	}

	public UFDouble getHcsum() {
		return hcsum;
	}

	public void setHcsum(UFDouble hcsum) {
		this.hcsum = hcsum;
	}

	public UFDouble getHrnum() {
		return hrnum;
	}

	public void setHrnum(UFDouble hrnum) {
		this.hrnum = hrnum;
	}

	public UFDouble getHrsum() {
		return hrsum;
	}

	public void setHrsum(UFDouble hrsum) {
		this.hrsum = hrsum;
	}

	public UFDouble getNum() {
		return num;
	}

	public void setNum(UFDouble num) {
		this.num = num;
	}

	public UFDouble getSum() {
		return sum;
	}

	public void setSum(UFDouble sum) {
		this.sum = sum;
	}

	/**
	 * 2015年7月16日 jingqt添加红股入账的记录 bonus<BR>
	 * 这个数组很重要...
	 * 这个景群涛加了一个红股记录，陈建辉加了一个lxtz，导致很多证券无缘无故多出来了很多的利息调整的数据。这里修正一下。把sum字段放在最后。
	 * 
	 * @return
	 * @update jingqt 2017年1月18日
	 */
	public String[] getUFDoubleFieldNames() {
		return new String[] { "buynum", "buysum", "buyaccrual", "innum",
				"insum", "inaccrual", "outnum", "outsum", "outaccrual",
				"sellnum", "sellsum", "sellaccrual", "sellcost", "total_win",
				"fact_sum", "faccrual", "hrnum", "hrsum", "hcnum", "hcsum",
				"num", "bonus", "lxtz", "sum", "fefh" };
	}

	public void copyVO(SuperVO vo) {
		setPk_org((String) vo.getAttributeValue("pk_org"));
		setPk_group((String) vo.getAttributeValue("pk_group"));
		setPk_glorgbook((String) vo.getAttributeValue("pk_glorgbook"));
		setPk_assetsprop((String) vo.getAttributeValue("pk_assetsprop"));
		setPk_stocksort((String) vo.getAttributeValue("pk_stocksort"));
		setPk_billtype((String) vo.getAttributeValue("transtypecode"));
		setPk_capaccount((String) vo.getAttributeValue("pk_capaccount"));
		setPk_client((String) vo.getAttributeValue("pk_client"));
		setPk_operatesite((String) vo.getAttributeValue("pk_operatesite"));
		setPk_partnaccount((String) vo.getAttributeValue("pk_partnaccount"));
		setPk_securities((String) vo.getAttributeValue("pk_securities"));
		setPk_selfsgroup((String) vo.getAttributeValue("pk_selfsgroup"));
		setTrade_date((UFDate) vo.getAttributeValue("trade_date"));
		setBegin_date((UFLiteralDate) vo.getAttributeValue("begin_date"));
		setEnd_date((UFLiteralDate) vo.getAttributeValue("end_date"));
	}

	public void copyVO(SuperVO vo, String str) {
		setPk_org((String) vo.getAttributeValue("pk_org"));
		setPk_group((String) vo.getAttributeValue("pk_group"));
		setPk_assetsprop((String) vo.getAttributeValue(str + "pk_assetsprop"));
		setPk_stocksort((String) vo.getAttributeValue(str + "pk_stocksort"));
		setPk_billtype((String) vo.getAttributeValue("transtypecode"));
		setPk_capaccount((String) vo.getAttributeValue(str + "pk_capaccount"));
		setPk_client((String) vo.getAttributeValue(str + "pk_client"));
		setPk_operatesite((String) vo.getAttributeValue(str + "pk_operatesite"));
		setPk_partnaccount((String) vo.getAttributeValue(str
				+ "pk_partnaccount"));
		setPk_selfsgroup((String) vo.getAttributeValue(str + "pk_selfsgroup"));
		setTrade_date((UFDate) vo.getAttributeValue("trade_date"));
		setPk_securities((String) vo.getAttributeValue("pk_securities"));
	}

	public UFDouble getInnum() {
		return innum;
	}

	public void setInnum(UFDouble innum) {
		this.innum = innum;
	}

	public UFDouble getInsum() {
		return insum;
	}

	public void setInsum(UFDouble insum) {
		this.insum = insum;
	}

	public UFDouble getOutnum() {
		return outnum;
	}

	public void setOutnum(UFDouble outnum) {
		this.outnum = outnum;
	}

	public UFDouble getOutsum() {
		return outsum;
	}

	public void setOutsum(UFDouble outsum) {
		this.outsum = outsum;
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
	 * 公允价值变动
	 * 
	 * @return
	 */
	public UFDouble getFairValueChange() {
		return fairValueChange;
	}

	/**
	 * 公允价值变动
	 * 
	 * @param fairValueChange
	 */
	public void setFairValueChange(UFDouble fairValueChange) {
		this.fairValueChange = fairValueChange;
	}
}
