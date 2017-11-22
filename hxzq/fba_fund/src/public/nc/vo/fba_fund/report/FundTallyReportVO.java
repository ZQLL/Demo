package nc.vo.fba_fund.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ���ʹ���̨�˱���VO
 * 
 * @author qs
 * @since 2017-6-16����9:55:08
 */
public class FundTallyReportVO extends SuperVO {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * ���ݺ�
     */
    public String pk_billtype;
    /**
     * ��������
     */
    public String fundtype;
    /**
     * ��Ʒ����ַ�����
     */
    public String productorcounterparty;
    /**
     * ������
     */
    public String managedept;
    /**
     * �����
     */
    public UFDouble denomination;
    /**
     * ����
     */
    public Integer timelimit;
    /**
     * ��Ϣ��
     */
    public UFDate begindate;
    /**
     * ������
     */
    public UFDate enddate;
    /**
     * ��Ϣ��ʽ
     */
    public String paytype;
    /**
     * ִ��Ʊ������
     */
    public UFDouble excutefacerate;
    /**
     * ʵ������
     */
    public UFDouble realrate;
    /**
     * ����ʱ��
     */
    public UFDate trade_date;
    /**
     * ��֯
     */
    public String pk_org;
    /**
     * ��Ϣ����
     */
    public String begindistill_date;
    /**
     * �ڳ����/̯��ɱ�
     */
    public UFDouble start_balance;
    /**
     * ���ڷ���
     */
    public UFDouble cur_issue;
    /**
     * �Ҹ�����
     */
    public UFDouble pay_balance;
    /**
     * ����̯��
     */
    public UFDouble cur_amortize;
    /**
     * ��ĩ���/̯��ɱ�
     */
    public UFDouble end_balance;
    /**
     * Ӧ����Ϣ�ڳ�
     */
    public UFDouble start_payableinterest;
    /**
     * ������Ϣ֧�����
     */
    public UFDouble cur_expend;
    /**
     * ����Ӧ����Ϣ
     */
    public UFDouble cur_payableinterest;
    /**
     * ���ڶҸ���Ϣ
     */
    public UFDouble cur_payinterest;
    /**
     * Ӧ����Ϣ��ĩ���
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
