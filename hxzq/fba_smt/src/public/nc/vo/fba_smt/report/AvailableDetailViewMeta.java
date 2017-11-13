package nc.vo.fba_smt.report;

import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;

public class AvailableDetailViewMeta extends DataViewMeta {
	public static final String[] FUNDTRADE_S_FIELD = new String[] { "pk_org",
			"pk_group", "pk_glorgbook", "pk_stocksort", "pk_securities",
			"pk_capaccount", "pk_selfsgroup", "pk_partnaccount",
			"pk_operatesite", "pk_client", "pk_assetsprop" };

	public static final String[] EXT_D_FIELD = new String[] { "firstnum",
			"firstsum", "firstMarket", "kgzkrnum", "kgzkrsum", "zrtjrnum",
			"zrtjrsum", "rcghnum", "rcghsum", "hgqyhznum", "hgqyhzsum",
			"rczqnum", "rczqsum", "krzkgnum", "krzkgsum", "zrtghnum",
			"zrtghsum", "hgrcnum", "hgrcsum", "endnum", "endsum", "closeprice",
			"marketValue", "profit" };

	public static final String[] EXT_DATE_FIELD = new String[] { "trade_date" };
}
