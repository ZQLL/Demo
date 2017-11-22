package nc.vo.fba_fund.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 筹资管理台账报表VO
 * 
 * @author qs
 * @since 2017-6-16上午9:55:08
 */
public class FundTallyReportVO extends SuperVO {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 单据号
     */
    public String pk_billtype;
    /**
     * 融资类型
     */
    public String fundtype;
    /**
     * 产品或对手方名称
     */
    public String productorcounterparty;
    /**
     * 管理部门
     */
    public String managedept;
    /**
     * 结存金额
     */
    public UFDouble denomination;
    /**
     * 期限
     */
    public Integer timelimit;
    /**
     * 起息日
     */
    public UFDate begindate;
    /**
     * 到期日
     */
    public UFDate enddate;
    /**
     * 付息方式
     */
    public String paytype;
    /**
     * 执行票面利率
     */
    public UFDouble excutefacerate;
    /**
     * 实际利率
     */
    public UFDouble realrate;
    /**
     * 交易时间
     */
    public UFDate trade_date;
    /**
     * 组织
     */
    public String pk_org;
    /**
     * 计息日期
     */
    public String begindistill_date;
    /**
     * 期初余额/摊余成本
     */
    public UFDouble start_balance;
    /**
     * 本期发行
     */
    public UFDouble cur_issue;
    /**
     * 兑付本金
     */
    public UFDouble pay_balance;
    /**
     * 本期摊销
     */
    public UFDouble cur_amortize;
    /**
     * 期末余额/摊余成本
     */
    public UFDouble end_balance;
    /**
     * 应付利息期初
     */
    public UFDouble start_payableinterest;
    /**
     * 本期利息支出金额
     */
    public UFDouble cur_expend;
    /**
     * 本期应付利息
     */
    public UFDouble cur_payableinterest;
    /**
     * 本期兑付利息
     */
    public UFDouble cur_payinterest;
    /**
     * 应付利息期末余额
     */
    public UFDouble end_payableinterest;

    public String getPk_billtype() {
        return pk_billtype;
    }

    public void setPk_billtype(String pk_billtype) {
        this.pk_billtype = pk_billtype;
    }

    public String getProductorcounterparty() {
        return productorcounterparty;
    }

    public void setProductorcounterparty(String productorcounterparty) {
        this.productorcounterparty = productorcounterparty;
    }

    public String getManagedept() {
        return managedept;
    }

    public void setManagedept(String managedept) {
        this.managedept = managedept;
    }

    public Integer getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
    }

    public UFDate getEnddate() {
        return enddate;
    }

    public void setEnddate(UFDate enddate) {
        this.enddate = enddate;
    }

    public String getFundtype() {
        return fundtype;
    }

    public void setFundtype(String fundtype) {
        this.fundtype = fundtype;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public UFDouble getRealrate() {
        return realrate;
    }

    public void setRealrate(UFDouble realrate) {
        this.realrate = realrate;
    }

    public UFDate getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(UFDate trade_date) {
        this.trade_date = trade_date;
    }

    public String getPk_org() {
        return pk_org;
    }

    public void setPk_org(String pk_org) {
        this.pk_org = pk_org;
    }

    public UFDouble getStart_balance() {
        return start_balance;
    }

    public void setStart_balance(UFDouble start_balance) {
        this.start_balance = start_balance;
    }

    public UFDouble getCur_issue() {
        return cur_issue;
    }

    public void setCur_issue(UFDouble cur_issue) {
        this.cur_issue = cur_issue;
    }

    public UFDouble getPay_balance() {
        return pay_balance;
    }

    public void setPay_balance(UFDouble pay_balance) {
        this.pay_balance = pay_balance;
    }

    public UFDouble getCur_amortize() {
        return cur_amortize;
    }

    public void setCur_amortize(UFDouble cur_amortize) {
        this.cur_amortize = cur_amortize;
    }

    public UFDouble getEnd_balance() {
        return end_balance;
    }

    public void setEnd_balance(UFDouble end_balance) {
        this.end_balance = end_balance;
    }

    public UFDouble getStart_payableinterest() {
        return start_payableinterest;
    }

    public void setStart_payableinterest(UFDouble start_payableinterest) {
        this.start_payableinterest = start_payableinterest;
    }

    public UFDouble getCur_expend() {
        return cur_expend;
    }

    public void setCur_expend(UFDouble cur_expend) {
        this.cur_expend = cur_expend;
    }

    public UFDouble getCur_payableinterest() {
        return cur_payableinterest;
    }

    public void setCur_payableinterest(UFDouble cur_payableinterest) {
        this.cur_payableinterest = cur_payableinterest;
    }

    public UFDouble getCur_payinterest() {
        return cur_payinterest;
    }

    public void setCur_payinterest(UFDouble cur_payinterest) {
        this.cur_payinterest = cur_payinterest;
    }

    public UFDouble getEnd_payableinterest() {
        return end_payableinterest;
    }

    public void setEnd_payableinterest(UFDouble end_payableinterest) {
        this.end_payableinterest = end_payableinterest;
    }

    public UFDouble getDenomination() {
        return denomination;
    }

    public void setDenomination(UFDouble denomination) {
        this.denomination = denomination;
    }

    public UFDate getBegindate() {
        return begindate;
    }

    public void setBegindate(UFDate begindate) {
        this.begindate = begindate;
    }

    public UFDouble getExcutefacerate() {
        return excutefacerate;
    }

    public void setExcutefacerate(UFDouble excutefacerate) {
        this.excutefacerate = excutefacerate;
    }

    public String getBegindistill_date() {
        return begindistill_date;
    }

    public void setBegindistill_date(String begindistill_date) {
        this.begindistill_date = begindistill_date;
    }

}
