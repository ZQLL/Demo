package nc.vo.fba_sim.simtrade.report;

import nc.vo.fba_sim.simtrade.fundtrade.FundTradeVO;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;

public class SecInvestDailyViewMeta extends DataViewMeta {

	public static final String[] FUNDTRADE_S_FIELD = new String[] { "pk_org",
			"pk_group", "pk_glorgbook", "pk_capaccount", "pk_selfsgroup",
			"pk_partnaccount", "pk_operatesite", "pk_client", "pk_sclassify",
			"pk_securities", "pk_assetsprop", "summary", "pk_currtype" };

	public static final String[] EXT_D_FIELD = new String[] { "firstNum",
			"firstSum", "firstPrice", "buyNum", "buySum", "buyPrice",
			"sellNum", "sellSum", "sellPrice", "sellCost", "endNum", "endSum",
			"endPrice", "localwin", "marketPrice", "marketSum", "profitLoss",
			"gyz", "zgbzb", "bonus", "fairValueChange", "first_marketSum", "fefh" };

	public static final String[] EXT_DATE_FIELD = new String[] {
			FundTradeVO.TRADE_DATE, "begin_date", "end_date" };

}
