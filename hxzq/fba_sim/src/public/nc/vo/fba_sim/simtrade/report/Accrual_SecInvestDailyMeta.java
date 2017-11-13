package nc.vo.fba_sim.simtrade.report;

import nc.vo.fba_sim.simtrade.fundtrade.FundTradeVO;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;

public class Accrual_SecInvestDailyMeta extends DataViewMeta {
	public static final String[] FUNDTRADE_S_FIELD = new String[] { "pk_org",
			"pk_group", "pk_glorgbook", "pk_capaccount", "pk_selfsgroup",
			"pk_partnaccount", "pk_operatesite", "pk_client", "pk_sclassify",
			"pk_securities", "pk_assetsprop", "summary", "pk_currtype",
			"propertyhistoryname" };

	public static final String[] EXT_D_FIELD = new String[] { "firstNum",
			"firstSum", "init_accrual_sum", "buyNum", "buy_accrual_sum",
			"buySum", "sellNum", "sellSum", "sellCost", "sell_accrual_sum",
			"f_accrual_sum", "endNum", "endSum", "end_accrual_sum",
			"accrual_sum", "fairvalueSum", "marketSum", "proportion",
			"init_total_win", "totalWin", "first_marketSum" };

	public static final String[] EXT_DATE_FIELD = new String[] {
			FundTradeVO.TRADE_DATE, "begin_date", "end_date" };
}
