package nc.vo.fba_fund.report;

import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;

/**
 * 筹资管理台账报表要显示的字段
 * 
 * @author qs
 * @since 2017-6-16上午9:19:47
 */
public class FundTallyViewMeta extends DataViewMeta {
    // 筹资管理台账表中的字段
    public static final String[] FUNDTALLY_FILED = { FundTallyVO.PK_BILLTYPE, FundTallyVO.FUNDTYPE,
            FundTallyVO.PRODUCTORCOUNTERPARTY, FundTallyVO.MANAGEDEPT, FundTallyVO.DENOMINATION, FundTallyVO.TIMELIMIT,
            FundTallyVO.BEGINDATE, FundTallyVO.ENDDATE, FundTallyVO.PAYTYPE, FundTallyVO.EXCUTEFACERATE, FundTallyVO.REALRATE,
            FundTallyVO.TRADE_DATE, FundTallyVO.PK_ORG, FundTallyVO.BEGINDISTILL_DATE };

    // 本金
    public static final String[] CAPITAL_FILED = { "start_balance", "cur_issue", "pay_balance", "cur_amortize ", "end_balance" };

    // 利息
    public static final String[] INTEREST_FILED = { "start_payableinterest", "cur_expend", "cur_payableinterest",
            "cur_payinterest", "end_payableinterest","daily_possession","costrate_capital" };

}
