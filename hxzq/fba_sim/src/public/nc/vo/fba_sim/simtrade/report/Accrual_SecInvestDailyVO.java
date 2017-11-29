package nc.vo.fba_sim.simtrade.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 计息品种经营日报VO
 * 
 * @author tianxingjian
 * 
 */
public class Accrual_SecInvestDailyVO extends SuperVO {
	private static final long serialVersionUID = 1L;

	private String pk_org;
	private String pk_group;
	private String pk_glorgbook;

	public String pk_sclassify;

	public String pk_capaccount;

	public String pk_securities;

	public String pk_selfsgroup;

	public String pk_partnaccount;

	public String pk_operatesite;

	public String pk_client;

	public UFDate trade_date;

	public String securitiescode;

	public String securitiesname;

	public UFDouble firstNum;

	public UFDouble firstSum;

	public UFDouble init_accrual_sum;// 期初结存应收利息

	public UFDouble buyNum;

	public UFDouble buy_accrual_sum;// 买入应收利息

	public UFDouble buySum;

	public UFDouble sellNum;

	public UFDouble sellSum;

	public UFDouble sellCost; // 成本金额

	public UFDouble sell_accrual_sum;// 卖出应收利息

	public UFDouble outputNum;

	public UFDouble outputSum;

	public UFDouble out_accrual_sum;

	public UFDouble inputNum;

	public UFDouble inputSum;

	public UFDouble in_accrual_sum;

	public UFDouble endNum;

	public UFDouble endSum;

	public UFDouble end_accrual_sum;// 期末结存应收利息

	public UFDouble init_total_win;// 本期收益

	public UFDouble totalWin;// 累计收益

	public UFDouble fairvalueSum;// 公允值余额

	public UFDouble martetprice;// 市价

	public UFDouble marketSum;// 市值

	public UFDouble f_accrual_sum;// 收到对付利息

	public UFDouble accrual_sum;// 本期计提应收利息

	public UFDouble accrual_balance;// 应收利息余额

	public UFDouble profit_loss;// 浮动盈亏

	public UFDouble fact_win;// 实际收益

	public UFDouble accrual_win;// 利息收益

	public String pk_assetsprop;

	public String pk_property;

	public String propcode;

	public String propname;

	public String pk_corp;

	public UFDouble proportion;// 期末库存数量占总股本比例%

	private String sclassifycode = null;

	private String sclassifyname = null;

	private String propertyhistoryname = null;

	// add by lihaibo 2017-05-08
	private UFDouble first_marketSum = new UFDouble(0);// 期初市值

	public UFDouble getFirst_marketSum() {
		return first_marketSum;
	}

	public void setFirst_marketSum(UFDouble first_marketSum) {
		this.first_marketSum = first_marketSum;
	}

	public String getPropertyhistoryname() {
		return propertyhistoryname;
	}

	public void setPropertyhistoryname(String propertyhistoryname) {
		this.propertyhistoryname = propertyhistoryname;
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

	public String getSclassifycode() {
		return sclassifycode;
	}

	public void setSclassifycode(String sclassifycode) {
		this.sclassifycode = sclassifycode;
	}

	public String getSclassifyname() {
		return sclassifyname;
	}

	public void setSclassifyname(String sclassifyname) {
		this.sclassifyname = sclassifyname;
	}

	public UFDouble getAccrual_balance() {
		return accrual_balance;
	}

	public void setAccrual_balance(UFDouble accrual_balance) {
		this.accrual_balance = accrual_balance;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_glorgbook() {
		return pk_glorgbook;
	}

	public void setPk_glorgbook(String pk_glorgbook) {
		this.pk_glorgbook = pk_glorgbook;
	}

	public String getPk_property() {
		return pk_property;
	}

	public void setPk_property(String pk_property) {
		this.pk_property = pk_property;
	}

	public String getPropcode() {
		return propcode;
	}

	public void setPropcode(String propcode) {
		this.propcode = propcode;
	}

	public String getPropname() {
		return propname;
	}

	public void setPropname(String propname) {
		this.propname = propname;
	}

	public String getPk_assetsprop() {
		return pk_assetsprop;
	}

	public void setPk_assetsprop(String pk_assetsprop) {
		this.pk_assetsprop = pk_assetsprop;
	}

	public String getPk_sclassify() {
		return pk_sclassify;
	}

	public void setPk_sclassify(String pk_sclassify) {
		this.pk_sclassify = pk_sclassify;
	}

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

	public UFDate getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(UFDate trade_date) {
		this.trade_date = trade_date;
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

	public UFDouble getInit_accrual_sum() {
		return init_accrual_sum;
	}

	public void setInit_accrual_sum(UFDouble init_accrual_sum) {
		this.init_accrual_sum = init_accrual_sum;
	}

	public UFDouble getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(UFDouble buyNum) {
		this.buyNum = buyNum;
	}

	public UFDouble getBuy_accrual_sum() {
		return buy_accrual_sum;
	}

	public void setBuy_accrual_sum(UFDouble buy_accrual_sum) {
		this.buy_accrual_sum = buy_accrual_sum;
	}

	public UFDouble getBuySum() {
		return buySum;
	}

	public void setBuySum(UFDouble buySum) {
		this.buySum = buySum;
	}

	public UFDouble getSellNum() {
		return sellNum;
	}

	public void setSellNum(UFDouble sellNum) {
		this.sellNum = sellNum;
	}

	public UFDouble getSellSum() {
		return sellSum;
	}

	public void setSellSum(UFDouble sellSum) {
		this.sellSum = sellSum;
	}

	public UFDouble getSellCost() {
		return sellCost;
	}

	public void setSellCost(UFDouble sellCost) {
		this.sellCost = sellCost;
	}

	public UFDouble getSell_accrual_sum() {
		return sell_accrual_sum;
	}

	public void setSell_accrual_sum(UFDouble sell_accrual_sum) {
		this.sell_accrual_sum = sell_accrual_sum;
	}

	public UFDouble getOutputNum() {
		return outputNum;
	}

	public void setOutputNum(UFDouble outputNum) {
		this.outputNum = outputNum;
	}

	public UFDouble getOutputSum() {
		return outputSum;
	}

	public void setOutputSum(UFDouble outputSum) {
		this.outputSum = outputSum;
	}

	public UFDouble getOut_accrual_sum() {
		return out_accrual_sum;
	}

	public void setOut_accrual_sum(UFDouble out_accrual_sum) {
		this.out_accrual_sum = out_accrual_sum;
	}

	public UFDouble getInputNum() {
		return inputNum;
	}

	public void setInputNum(UFDouble inputNum) {
		this.inputNum = inputNum;
	}

	public UFDouble getInputSum() {
		return inputSum;
	}

	public void setInputSum(UFDouble inputSum) {
		this.inputSum = inputSum;
	}

	public UFDouble getIn_accrual_sum() {
		return in_accrual_sum;
	}

	public void setIn_accrual_sum(UFDouble in_accrual_sum) {
		this.in_accrual_sum = in_accrual_sum;
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

	public UFDouble getEnd_accrual_sum() {
		return end_accrual_sum;
	}

	public void setEnd_accrual_sum(UFDouble end_accrual_sum) {
		this.end_accrual_sum = end_accrual_sum;
	}

	public UFDouble getInit_total_win() {
		return init_total_win;
	}

	public void setInit_total_win(UFDouble init_total_win) {
		this.init_total_win = init_total_win;
	}

	public UFDouble getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(UFDouble totalWin) {
		this.totalWin = totalWin;
	}

	public UFDouble getFairvalueSum() {
		return fairvalueSum;
	}

	public void setFairvalueSum(UFDouble fairvalueSum) {
		this.fairvalueSum = fairvalueSum;
	}

	public UFDouble getMartetprice() {
		return martetprice;
	}

	public void setMartetprice(UFDouble martetprice) {
		this.martetprice = martetprice;
	}

	public UFDouble getMarketSum() {
		return marketSum;
	}

	public void setMarketSum(UFDouble marketSum) {
		this.marketSum = marketSum;
	}

	public UFDouble getF_accrual_sum() {
		return f_accrual_sum;
	}

	public void setF_accrual_sum(UFDouble f_accrual_sum) {
		this.f_accrual_sum = f_accrual_sum;
	}

	public UFDouble getAccrual_sum() {
		return accrual_sum;
	}

	public void setAccrual_sum(UFDouble accrual_sum) {
		this.accrual_sum = accrual_sum;
	}

	public UFDouble getProfit_loss() {
		return profit_loss;
	}

	public void setProfit_loss(UFDouble profit_loss) {
		this.profit_loss = profit_loss;
	}

	public UFDouble getFact_win() {
		return fact_win;
	}

	public void setFact_win(UFDouble fact_win) {
		this.fact_win = fact_win;
	}

	public UFDouble getAccrual_win() {
		return accrual_win;
	}

	public void setAccrual_win(UFDouble accrual_win) {
		this.accrual_win = accrual_win;
	}

	public UFDouble getProportion() {
		return proportion;
	}

	public void setProportion(UFDouble proportion) {
		this.proportion = proportion;
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

}
