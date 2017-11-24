package nc.vo.fba_fund.report;

import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;

/**
 * ���ʹ���̨�˱���Ҫ��ʾ���ֶ�
 * 
 * @author qs
 * @since 2017-6-16����9:19:47
 */
public class FundTallyViewMeta extends DataViewMeta {
    // ���ʹ���̨�˱��е��ֶ�
    public static final String[] FUNDTALLY_FILED = { FundTallyVO.PK_BILLTYPE, FundTallyVO.FUNDTYPE,
            FundTallyVO.PRODUCTORCOUNTERPARTY, FundTallyVO.MANAGEDEPT, FundTallyVO.DENOMINATION, FundTallyVO.TIMELIMIT,
            FundTallyVO.BEGINDATE, FundTallyVO.ENDDATE, FundTallyVO.PAYTYPE, FundTallyVO.EXCUTEFACERATE, FundTallyVO.REALRATE,
            FundTallyVO.TRADE_DATE, FundTallyVO.PK_ORG, FundTallyVO.BEGINDISTILL_DATE };

    // ����
    public static final String[] CAPITAL_FILED = { "start_balance", "cur_issue", "pay_balance", "cur_amortize ", "end_balance" };

    // ��Ϣ
    public static final String[] INTEREST_FILED = { "start_payableinterest", "cur_expend", "cur_payableinterest",
            "cur_payinterest", "end_payableinterest","daily_possession","costrate_capital" };

}
