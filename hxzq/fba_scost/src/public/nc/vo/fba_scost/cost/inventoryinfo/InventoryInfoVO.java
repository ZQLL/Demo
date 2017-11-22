package nc.vo.fba_scost.cost.inventoryinfo;

import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class InventoryInfoVO extends SuperVO implements ITrade_Data {
	public static final String ACCRUAL_SUM = "accrual_sum";
	public static final String PAYTYPE = "paytype";
	public static final String ASSESS_ADD = "assess_add";
	public static final String ADJUST_RATE = "adjust_rate";
	public static final String BARGIN_NUM = "bargin_num";
	public static final String BARGIN_SUM = "bargin_sum";
	public static final String BEGIN_DATE = "begin_date";
	public static final String TIMELIMIT = "timelimit";
	public static final String RATETYPE = "ratetype";
	public static final String ISSUEFACERATE = "issuefacerate";
	public static final String EXECUTEFACERATE = "executefacerate";
	public static final String ISSUE_FEE = "issue_fee";
	public static final String BILLTYPECODE = "billtypecode";
	public static final String CONTRACTNO = "contractno";
	public static final String CREATIONTIME = "creationtime";
	public static final String CREATOR = "creator";
	public static final String END_DATE = "end_date";
	public static final String ENDDATE = "enddate";
	public static final String FACT_SUM = "fact_sum";
	public static final String FROZEN_NUM = "frozen_num";
	public static final String FROZEN_SUM = "frozen_sum";
	public static final String FROZEN_TAX = "frozen_tax";
	public static final String FUNDTYPE = "fundtype";
	public static final String MANAGEDEPT = "managedept";
	public static final String MARKET = "market";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String MODIFIER = "modifier";
	public static final String NDEF10 = "ndef10";
	public static final String NDEF11 = "ndef11";
	public static final String NDEF12 = "ndef12";
	public static final String NDEF13 = "ndef13";
	public static final String NDEF14 = "ndef14";
	public static final String NDEF6 = "ndef6";
	public static final String NDEF7 = "ndef7";
	public static final String NDEF8 = "ndef8";
	public static final String NDEF9 = "ndef9";
	public static final String OTHER_FEE = "other_fee";
	public static final String PK_ASSETSPROP = "pk_assetsprop";
	public static final String PK_BILLTYPE = "pk_billtype";
	public static final String PK_BILLTYPEGROUP = "pk_billtypegroup";
	public static final String PK_CAPACCOUNT = "pk_capaccount";
	public static final String PK_CLIENT = "pk_client";
	public static final String PK_COSTPLAN = "pk_costplan";
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_INVENTORYINFO = "pk_inventoryinfo";
	public static final String PK_OPERATESITE = "pk_operatesite";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String PK_PARTNACCOUNT = "pk_partnaccount";
	public static final String PK_SECURITIES = "pk_securities";
	public static final String PK_SELFSGROUP = "pk_selfsgroup";
	public static final String PK_STOCKSORT = "pk_stocksort";
	public static final String PK_TRANSTYPE = "pk_transtype";
	public static final String PRODUCTORCOUNTERPARTY = "productorcounterparty";
	public static final String REALRATE = "realrate";
	public static final String RISK_FEE = "risk_fee";
	public static final String STARTDATE = "startdate";
	public static final String STATE = "state";
	public static final String STOCKS_NUM = "stocks_num";
	public static final String STOCKS_SUM = "stocks_sum";
	public static final String STOCKS_TAX = "stocks_tax";
	public static final String TAXEXPENSE = "taxexpense";
	public static final String TOTAL_WIN = "total_win";
	public static final String TRADE_DATE = "trade_date";
	public static final String TRANSTYPECODE = "transtypecode";
	public static final String TS = "ts";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF5 = "vdef5";

	@Override
	public UFDouble getAccrual_sum() {
		return (UFDouble) getAttributeValue("accrual_sum");
	}

	@Override
	public void setAccrual_sum(UFDouble accrual_sum) {
		setAttributeValue("accrual_sum", accrual_sum);
	}

	public Integer getPaytype() {
		return (Integer) getAttributeValue("paytype");
	}

	public void setPaytype(Integer paytype) {
		setAttributeValue("paytype", paytype);
	}

	public UFDouble getAssess_add() {
		return (UFDouble) getAttributeValue("assess_add");
	}

	public void setAssess_add(UFDouble assess_add) {
		setAttributeValue("assess_add", assess_add);
	}

	public UFDouble getAdjust_rate() {
		return (UFDouble) getAttributeValue("adjust_rate");
	}

	public void setAdjust_rate(UFDouble adjust_rate) {
		setAttributeValue("adjust_rate", adjust_rate);
	}

	public UFDouble getBargin_num() {
		return (UFDouble) getAttributeValue("bargin_num");
	}

	public void setBargin_num(UFDouble bargin_num) {
		setAttributeValue("bargin_num", bargin_num);
	}

	public UFDouble getBargin_sum() {
		return (UFDouble) getAttributeValue("bargin_sum");
	}

	public void setBargin_sum(UFDouble bargin_sum) {
		setAttributeValue("bargin_sum", bargin_sum);
	}

	@Override
	public UFLiteralDate getBegin_date() {
		return (UFLiteralDate) getAttributeValue("begin_date");
	}

	@Override
	public void setBegin_date(UFLiteralDate begin_date) {
		setAttributeValue("begin_date", begin_date);
	}

	@Override
	public String getBilltypecode() {
		return (String) getAttributeValue("billtypecode");
	}

	public Integer getTimelimit() {
		return (Integer) getAttributeValue("timelimit");
	}

	public void setTimelimit(Integer timelimit) {
		setAttributeValue("timelimit", timelimit);
	}

	public Integer getRatetype() {
		return (Integer) getAttributeValue("ratetype");
	}

	public void setRatetype(Integer ratetype) {
		setAttributeValue("ratetype", ratetype);
	}

	public UFDouble getIssuefacerate() {
		return (UFDouble) getAttributeValue("issuefacerate");
	}

	public void setIssuefacerate(UFDouble issuefacerate) {
		setAttributeValue("issuefacerate", issuefacerate);
	}

	public UFDouble getExecutefacerate() {
		return (UFDouble) getAttributeValue("executefacerate");
	}

	public void setExecutefacerate(UFDouble executefacerate) {
		setAttributeValue("executefacerate", executefacerate);
	}

	@Override
	public void setBilltypecode(String billtypecode) {
		setAttributeValue("billtypecode", billtypecode);
	}

	public UFDouble getIssue_fee() {
		return (UFDouble) getAttributeValue("issue_fee");
	}

	public void setIssue_fee(UFDouble issue_fee) {
		setAttributeValue("issue_fee", issue_fee);
	}

	public String getContractno() {
		return (String) getAttributeValue("contractno");
	}

	public void setContractno(String contractno) {
		setAttributeValue("contractno", contractno);
	}

	public UFDateTime getCreationtime() {
		return (UFDateTime) getAttributeValue("creationtime");
	}

	public void setCreationtime(UFDateTime creationtime) {
		setAttributeValue("creationtime", creationtime);
	}

	public String getCreator() {
		return (String) getAttributeValue("creator");
	}

	public void setCreator(String creator) {
		setAttributeValue("creator", creator);
	}

	@Override
	public UFLiteralDate getEnd_date() {
		return (UFLiteralDate) getAttributeValue("end_date");
	}

	@Override
	public void setEnd_date(UFLiteralDate end_date) {
		setAttributeValue("end_date", end_date);
	}

	public UFDate getEnddate() {
		return (UFDate) getAttributeValue("enddate");
	}

	public void setEnddate(UFDate enddate) {
		setAttributeValue("enddate", enddate);
	}

	@Override
	public UFDouble getFact_sum() {
		return (UFDouble) getAttributeValue("fact_sum");
	}

	@Override
	public void setFact_sum(UFDouble fact_sum) {
		setAttributeValue("fact_sum", fact_sum);
	}

	public UFDouble getFrozen_num() {
		return (UFDouble) getAttributeValue("frozen_num");
	}

	public void setFrozen_num(UFDouble frozen_num) {
		setAttributeValue("frozen_num", frozen_num);
	}

	public UFDouble getFrozen_sum() {
		return (UFDouble) getAttributeValue("frozen_sum");
	}

	public void setFrozen_sum(UFDouble frozen_sum) {
		setAttributeValue("frozen_sum", frozen_sum);
	}

	public UFDouble getFrozen_tax() {
		return (UFDouble) getAttributeValue("frozen_tax");
	}

	public void setFrozen_tax(UFDouble frozen_tax) {
		setAttributeValue("frozen_tax", frozen_tax);
	}

	public Integer getFundtype() {
		return (Integer) getAttributeValue("fundtype");
	}

	public void setFundtype(Integer fundtype) {
		setAttributeValue("fundtype", fundtype);
	}

	public String getManagedept() {
		return (String) getAttributeValue("managedept");
	}

	public void setManagedept(String managedept) {
		setAttributeValue("managedept", managedept);
	}

	public Integer getMarket() {
		return (Integer) getAttributeValue("market");
	}

	public void setMarket(Integer market) {
		setAttributeValue("market", market);
	}

	public UFDateTime getModifiedtime() {
		return (UFDateTime) getAttributeValue("modifiedtime");
	}

	public void setModifiedtime(UFDateTime modifiedtime) {
		setAttributeValue("modifiedtime", modifiedtime);
	}

	public String getModifier() {
		return (String) getAttributeValue("modifier");
	}

	public void setModifier(String modifier) {
		setAttributeValue("modifier", modifier);
	}

	public UFDouble getNdef10() {
		return (UFDouble) getAttributeValue("ndef10");
	}

	public void setNdef10(UFDouble ndef10) {
		setAttributeValue("ndef10", ndef10);
	}

	public Integer getNdef11() {
		return (Integer) getAttributeValue("ndef11");
	}

	public void setNdef11(Integer ndef11) {
		setAttributeValue("ndef11", ndef11);
	}

	public Integer getNdef12() {
		return (Integer) getAttributeValue("ndef12");
	}

	public void setNdef12(Integer ndef12) {
		setAttributeValue("ndef12", ndef12);
	}

	public Integer getNdef13() {
		return (Integer) getAttributeValue("ndef13");
	}

	public void setNdef13(Integer ndef13) {
		setAttributeValue("ndef13", ndef13);
	}

	public Integer getNdef14() {
		return (Integer) getAttributeValue("ndef14");
	}

	public void setNdef14(Integer ndef14) {
		setAttributeValue("ndef14", ndef14);
	}

	public UFDouble getNdef6() {
		return (UFDouble) getAttributeValue("ndef6");
	}

	public void setNdef6(UFDouble ndef6) {
		setAttributeValue("ndef6", ndef6);
	}

	public UFDouble getNdef7() {
		return (UFDouble) getAttributeValue("ndef7");
	}

	public void setNdef7(UFDouble ndef7) {
		setAttributeValue("ndef7", ndef7);
	}

	public UFDouble getNdef8() {
		return (UFDouble) getAttributeValue("ndef8");
	}

	public void setNdef8(UFDouble ndef8) {
		setAttributeValue("ndef8", ndef8);
	}

	public UFDouble getNdef9() {
		return (UFDouble) getAttributeValue("ndef9");
	}

	public void setNdef9(UFDouble ndef9) {
		setAttributeValue("ndef9", ndef9);
	}

	public UFDouble getOther_fee() {
		return (UFDouble) getAttributeValue("other_fee");
	}

	public void setOther_fee(UFDouble other_fee) {
		setAttributeValue("other_fee", other_fee);
	}

	@Override
	public String getPk_assetsprop() {
		return (String) getAttributeValue("pk_assetsprop");
	}

	@Override
	public void setPk_assetsprop(String pk_assetsprop) {
		setAttributeValue("pk_assetsprop", pk_assetsprop);
	}

	@Override
	public String getPk_billtype() {
		return (String) getAttributeValue("pk_billtype");
	}

	@Override
	public void setPk_billtype(String pk_billtype) {
		setAttributeValue("pk_billtype", pk_billtype);
	}

	public String getPk_billtypegroup() {
		return (String) getAttributeValue("pk_billtypegroup");
	}

	public void setPk_billtypegroup(String pk_billtypegroup) {
		setAttributeValue("pk_billtypegroup", pk_billtypegroup);
	}

	@Override
	public String getPk_capaccount() {
		return (String) getAttributeValue("pk_capaccount");
	}

	@Override
	public void setPk_capaccount(String pk_capaccount) {
		setAttributeValue("pk_capaccount", pk_capaccount);
	}

	@Override
	public String getPk_client() {
		return (String) getAttributeValue("pk_client");
	}

	@Override
	public void setPk_client(String pk_client) {
		setAttributeValue("pk_client", pk_client);
	}

	public String getPk_costplan() {
		return (String) getAttributeValue("pk_costplan");
	}

	public void setPk_costplan(String pk_costplan) {
		setAttributeValue("pk_costplan", pk_costplan);
	}

	@Override
	public String getPk_glorgbook() {
		return (String) getAttributeValue("pk_glorgbook");
	}

	@Override
	public void setPk_glorgbook(String pk_glorgbook) {
		setAttributeValue("pk_glorgbook", pk_glorgbook);
	}

	@Override
	public String getPk_group() {
		return (String) getAttributeValue("pk_group");
	}

	@Override
	public void setPk_group(String pk_group) {
		setAttributeValue("pk_group", pk_group);
	}

	public String getPk_inventoryinfo() {
		return (String) getAttributeValue("pk_inventoryinfo");
	}

	public void setPk_inventoryinfo(String pk_inventoryinfo) {
		setAttributeValue("pk_inventoryinfo", pk_inventoryinfo);
	}

	@Override
	public String getPk_operatesite() {
		return (String) getAttributeValue("pk_operatesite");
	}

	@Override
	public void setPk_operatesite(String pk_operatesite) {
		setAttributeValue("pk_operatesite", pk_operatesite);
	}

	@Override
	public String getPk_org() {
		return (String) getAttributeValue("pk_org");
	}

	@Override
	public void setPk_org(String pk_org) {
		setAttributeValue("pk_org", pk_org);
	}

	@Override
	public String getPk_org_v() {
		return (String) getAttributeValue("pk_org_v");
	}

	@Override
	public void setPk_org_v(String pk_org_v) {
		setAttributeValue("pk_org_v", pk_org_v);
	}

	@Override
	public String getPk_partnaccount() {
		return (String) getAttributeValue("pk_partnaccount");
	}

	@Override
	public void setPk_partnaccount(String pk_partnaccount) {
		setAttributeValue("pk_partnaccount", pk_partnaccount);
	}

	@Override
	public String getPk_securities() {
		return (String) getAttributeValue("pk_securities");
	}

	@Override
	public void setPk_securities(String pk_securities) {
		setAttributeValue("pk_securities", pk_securities);
	}

	@Override
	public String getPk_selfsgroup() {
		return (String) getAttributeValue("pk_selfsgroup");
	}

	@Override
	public void setPk_selfsgroup(String pk_selfsgroup) {
		setAttributeValue("pk_selfsgroup", pk_selfsgroup);
	}

	@Override
	public String getPk_stocksort() {
		return (String) getAttributeValue("pk_stocksort");
	}

	@Override
	public void setPk_stocksort(String pk_stocksort) {
		setAttributeValue("pk_stocksort", pk_stocksort);
	}

	@Override
	public String getPk_transtype() {
		return (String) getAttributeValue("pk_transtype");
	}

	@Override
	public void setPk_transtype(String pk_transtype) {
		setAttributeValue("pk_transtype", pk_transtype);
	}

	public String getProductorcounterparty() {
		return (String) getAttributeValue("productorcounterparty");
	}

	public void setProductorcounterparty(String productorcounterparty) {
		setAttributeValue("productorcounterparty", productorcounterparty);
	}

	public UFDouble getRealrate() {
		return (UFDouble) getAttributeValue("realrate");
	}

	public void setRealrate(UFDouble realrate) {
		setAttributeValue("realrate", realrate);
	}

	public UFDouble getRisk_fee() {
		return (UFDouble) getAttributeValue("risk_fee");
	}

	public void setRisk_fee(UFDouble risk_fee) {
		setAttributeValue("risk_fee", risk_fee);
	}

	public UFDate getStartdate() {
		return (UFDate) getAttributeValue("startdate");
	}

	public void setStartdate(UFDate startdate) {
		setAttributeValue("startdate", startdate);
	}

	@Override
	public Integer getState() {
		return (Integer) getAttributeValue("state");
	}

	@Override
	public void setState(Integer state) {
		setAttributeValue("state", state);
	}

	public UFDouble getStocks_num() {
		return (UFDouble) getAttributeValue("stocks_num");
	}

	public void setStocks_num(UFDouble stocks_num) {
		setAttributeValue("stocks_num", stocks_num);
	}

	public UFDouble getStocks_sum() {
		return (UFDouble) getAttributeValue("stocks_sum");
	}

	public void setStocks_sum(UFDouble stocks_sum) {
		setAttributeValue("stocks_sum", stocks_sum);
	}

	public UFDouble getStocks_tax() {
		return (UFDouble) getAttributeValue("stocks_tax");
	}

	public void setStocks_tax(UFDouble stocks_tax) {
		setAttributeValue("stocks_tax", stocks_tax);
	}

	public UFDouble getTaxexpense() {
		return (UFDouble) getAttributeValue("taxexpense");
	}

	public void setTaxexpense(UFDouble taxexpense) {
		setAttributeValue("taxexpense", taxexpense);
	}

	public UFDouble getTotal_win() {
		return (UFDouble) getAttributeValue("total_win");
	}

	public void setTotal_win(UFDouble total_win) {
		setAttributeValue("total_win", total_win);
	}

	@Override
	public UFDate getTrade_date() {
		return (UFDate) getAttributeValue("trade_date");
	}

	@Override
	public void setTrade_date(UFDate trade_date) {
		setAttributeValue("trade_date", trade_date);
	}

	@Override
	public String getTranstypecode() {
		return (String) getAttributeValue("transtypecode");
	}

	@Override
	public void setTranstypecode(String transtypecode) {
		setAttributeValue("transtypecode", transtypecode);
	}

	public UFDateTime getTs() {
		return (UFDateTime) getAttributeValue("ts");
	}

	public void setTs(UFDateTime ts) {
		setAttributeValue("ts", ts);
	}

	public String getVdef1() {
		return (String) getAttributeValue("vdef1");
	}

	public void setVdef1(String vdef1) {
		setAttributeValue("vdef1", vdef1);
	}

	public String getVdef2() {
		return (String) getAttributeValue("vdef2");
	}

	public void setVdef2(String vdef2) {
		setAttributeValue("vdef2", vdef2);
	}

	public String getVdef3() {
		return (String) getAttributeValue("vdef3");
	}

	public void setVdef3(String vdef3) {
		setAttributeValue("vdef3", vdef3);
	}

	public String getVdef4() {
		return (String) getAttributeValue("vdef4");
	}

	public void setVdef4(String vdef4) {
		setAttributeValue("vdef4", vdef4);
	}

	public String getVdef5() {
		return (String) getAttributeValue("vdef5");
	}

	public void setVdef5(String vdef5) {
		setAttributeValue("vdef5", vdef5);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance()
				.getVOMeta("fba_fund.InventoryInfoVO");
	}

	private boolean isnew = false;

	public boolean getIsnew() {
		return this.isnew;
	}

	public void setIsnew(boolean isnew) {
		this.isnew = isnew;
	}

	@Override
	public UFDouble getBargain_num() {
		return null;
	}

	@Override
	public void setBargain_num(UFDouble bargain_num) {
	}

	@Override
	public UFDouble getBargain_sum() {
		return null;
	}

	@Override
	public void setBargain_sum(UFDouble bargain_sum) {
	}

	@Override
	public String getBillno() {
		return null;
	}

	@Override
	public void setBillno(String billno) {
	}

	@Override
	public UFDouble getFairvalue() {
		return null;
	}

	@Override
	public void setFairvalue(UFDouble fairvalue) {
	}

	@Override
	public UFDouble getInterest() {
		return null;
	}

	@Override
	public void setInterest(UFDouble interest) {
	}

	@Override
	public Integer getGenway() {
		return null;
	}

	@Override
	public UFLiteralDate getHr_begin_date() {
		return null;
	}

	@Override
	public void setHr_begin_date(UFLiteralDate hr_begin_date) {
	}

	@Override
	public UFLiteralDate getHr_end_date() {
		return null;
	}

	@Override
	public void setHr_end_date(UFLiteralDate hr_end_date) {
	}

	@Override
	public String getHr_pk_assetsprop() {
		return null;
	}

	@Override
	public void setHr_pk_assetsprop(String hr_pk_assetsprop) {
	}

	@Override
	public String getHr_pk_capaccount() {
		return null;
	}

	@Override
	public void setHr_pk_capaccount(String hr_pk_capaccount) {
	}

	@Override
	public String getHr_pk_client() {
		return null;
	}

	@Override
	public void setHr_pk_client(String hr_pk_client) {
	}

	@Override
	public String getHr_pk_operatesite() {
		return null;
	}

	@Override
	public void setHr_pk_operatesite(String hr_pk_operatesite) {
	}

	@Override
	public String getHr_pk_partnaccount() {
		return null;
	}

	@Override
	public void setHr_pk_partnaccount(String hr_pk_partnaccount) {
	}

	@Override
	public String getHr_pk_securities() {
		return null;
	}

	@Override
	public void setHr_pk_securities(String hr_Pk_securities) {
	}

	@Override
	public String getHr_pk_selfsgroup() {
		return null;
	}

	@Override
	public void setHr_pk_selfsgroup(String hr_pk_selfsgroup) {
	}

	@Override
	public String getHr_pk_stocksort() {
		return null;
	}

	@Override
	public void setHr_pk_stocksort(String hr_pk_stocksort) {
	}

	@Override
	public UFDouble getHr_bargain_num() {
		return null;
	}

	@Override
	public void setHr_bargain_num(UFDouble bargain_num) {
	}
}
