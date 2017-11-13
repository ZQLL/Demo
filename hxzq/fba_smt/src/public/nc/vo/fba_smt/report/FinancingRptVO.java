package nc.vo.fba_smt.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class FinancingRptVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	
	private String pk_org;
	private String pk_group;
	private String pk_glorgbook;

	private String pk_sclassify;

	private String securitiescode;

	private String securitiesname;

	private UFDouble firstnum;

	private UFDouble firstsum;

	private UFDouble endnum;

	private UFDouble endsum;

	private UFDouble fair_market;// ¹«ÔÊÖµ

	private String pk_stocksort;
	private String boursecode;
	private String pk_securities;
	private String pk_capaccount;
	private String pk_selfsgroup;
	private String pk_partnaccount;
	private String pk_assetsprop;
	private UFDate trade_date;
	
	public UFDate getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(UFDate trade_date) {
		this.trade_date = trade_date;
	}

	public String getPk_stocksort() {
		return pk_stocksort;
	}

	public void setPk_stocksort(String pk_stocksort) {
		this.pk_stocksort = pk_stocksort;
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

	public String getPk_assetsprop() {
		return pk_assetsprop;
	}

	public void setPk_assetsprop(String pk_assetsprop) {
		this.pk_assetsprop = pk_assetsprop;
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

	public UFDouble getFair_market() {
		return fair_market;
	}

	public void setFair_market(UFDouble fair_market) {
		this.fair_market = fair_market;
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

	public String getBoursecode() {
		return boursecode;
	}

	public void setBoursecode(String boursecode) {
		this.boursecode = boursecode;
	}

	public String getPk_securities() {
		return pk_securities;
	}

	public void setPk_securities(String pk_securities) {
		this.pk_securities = pk_securities;
	}
}
