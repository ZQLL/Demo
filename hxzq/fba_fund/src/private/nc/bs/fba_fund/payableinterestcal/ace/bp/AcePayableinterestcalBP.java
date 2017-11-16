package nc.bs.fba_fund.payableinterestcal.ace.bp;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.fba_fund.interest.PayableInterestCalCheck;
import nc.bs.fba_fund.interest.QueryInterestBaseInfo;
import nc.bs.pf.pub.PfDataCache;
import nc.impl.pubapp.pattern.data.vo.SchemeVOQuery;
import nc.itf.fba_scost.cost.pub.DistillSendVoucher;
import nc.itf.fba_scost.cost.pub.SendVoucherTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.fba_fund.payableinterestcal.AggPayableInterestCalVO;
import nc.vo.fba_fund.payableinterestcal.PayableInterestCalVO;
import nc.vo.fba_fund.pub.SystemConst;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.uif2.LoginContext;

/**
 * Ӧ����Ϣ����BP
 * 
 * @author qs
 * @since 2017-7-4����11:19:30
 */
public class AcePayableinterestcalBP {
    private PubMethod pm = PubMethod.getInstance();
    private BaseDAO basedao = new BaseDAO();
    private BillNoGenerator bng = null;

    public PayableInterestCalVO[] queryByQueryScheme(IQueryScheme querySheme) {
        QuerySchemeProcessor p = new QuerySchemeProcessor(querySheme);
        p.appendFuncPermissionOrgSql();
        return new SchemeVOQuery<PayableInterestCalVO>(PayableInterestCalVO.class).query(querySheme, null);
    }

    /**
     * Ӧ����Ϣ����
     * 
     * @param context
     * @param trade_date ��������
     * @return Ӧ����Ϣ��������
     * @throws
     * @author QS
     * @date 2017��6��22�� ����10:40:12
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PayableInterestCalVO[] calInterest(LoginContext context, UFDate trade_date) throws BusinessException {
        try {
            // 1.У��
            PayableInterestCalCheck check = new PayableInterestCalCheck();
            check.checkCal(context, trade_date);
            QueryInterestBaseInfo info = new QueryInterestBaseInfo();
            // 2.��ѯ�ɼ��������
            List list = info.getTallyVOS(context, trade_date, 0);
            // 3.��ʼ��Ϣ
            UFDate distilldate = trade_date;
            // ������̨��
            List<FundTallyVO> tallyList = new ArrayList<FundTallyVO>();
            // ���
            List<PayableInterestCalVO> distillList = new ArrayList<PayableInterestCalVO>();
            if (list != null && list.size() > 0) {
                FundTallyVO[] vos = new FundTallyVO[list.size()];
                vos = (FundTallyVO[]) list.toArray(new FundTallyVO[0]);
                this.initBNG(list.size(), vos[0]);
                for (FundTallyVO vo : vos) {
                    // ���ڵ����
                    UFDouble dq_denomination = null;
                    List dqlist = info.getDqTallyVOs(context, distilldate, vo);
                    if (dqlist != null && dqlist.size() > 0) {
                        FundTallyVO[] dqvos = new FundTallyVO[dqlist.size()];
                        dqvos = (FundTallyVO[]) dqlist.toArray(new FundTallyVO[0]);
                        dq_denomination = dqvos[0].getDenomination();
                    }
                    // ���㱾�μ���������
                    int caldays = this.getCalDays(distilldate, vo);
                    // ����̨��
                    List<FundTallyVO> tally = this.calInterestAndBuildTally(distilldate, vo, info, caldays, dq_denomination);
                    if (null != tally && tally.size() != 0) {
                        // ���μ�������Ϣ
                        UFDouble this_allinterest = new UFDouble(0.00);
                        for (FundTallyVO tallyVO : tally) {
                            // �ۼ�
                            this_allinterest = pm.add(this_allinterest, tallyVO.getThis_interest());
                        }
                        FundTallyVO lastTally = tally.get(tally.size() - 1);
                        // ���ع�����Ӧ����Ϣ����VO
                        PayableInterestCalVO distillvo = this.buildDistillVO(lastTally, vo, this_allinterest, caldays,
                                dq_denomination, distilldate);
                        tallyList.addAll(tally);
                        // ��̨��������ӵ�Ӧ����Ϣ���ᵥ��vo�У��Թ����浥������ʱ����̨������
                        distillvo.setObj(tallyList);
                        // ѭ����vo�����distillList���Թ�������ʾ�������ݼ�����
                        distillList.add(distillvo);
                    }
                }
            }
            return distillList.toArray(new PayableInterestCalVO[0]);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * ���㱾�μ���������
     * 
     * @param distilldate ��������
     * @param vo Ҫ�����̨������
     * @return int ���μ���������
     * @author QS
     * @date 2017��6��22�� ����10:33:54
     */
    private int getCalDays(UFDate distilldate, FundTallyVO vo) {
        // ��Ϣ����
        int caldays = 0;
        // ���������ڵ�����֮ǰ,˵��û����
        if (distilldate.asBegin().before(vo.getEnddate().asBegin())) {
            // �����������ֵ
            if (null != vo.getIndistill_date()) {
                caldays = UFDate.getDaysBetween(vo.getIndistill_date().asBegin(), distilldate);
            } else {
                // ������=��Ϣ�գ���Ϣһ��
                if (distilldate.isSameDate(vo.getBegindate())) {
                    caldays = 1;
                } else {
                    // ��һ�μ��᣺��ͷ��β
                    caldays = UFDate.getDaysBetween(vo.getBegindate().asBegin(), distilldate) + 1;
                }
            }
        } else {
            if (null != vo.getIndistill_date()) {
                // �������ڲ��ڵ�����֮ǰ����ȡ�����ռ���Ӧ����Ϣ����
                caldays = UFDate.getDaysBetween(vo.getIndistill_date().asBegin(), vo.getEnddate().asBegin());
            } else {
                // ���������Ϊ�գ�˵��û�м������ȡ��Ϣ�գ�������������
                caldays = UFDate.getDaysBetween(vo.getBegindate().asBegin(), vo.getEnddate().asBegin()) + 1;
            }
        }
        return caldays;
    }

    /**
     * ����̨����Ϣ
     * 
     * @param distilldate ��������
     * @param vo Ҫ�����̨������
     * @param info ̨�˲�ѯ������
     * @param info ���ڵ����
     * @return List<FundTallyVO> �������� ̨��VO��list
     * @throws BusinessException
     * @author QS
     * @date 2017��6��21�� ����3:35:21
     */
    private List<FundTallyVO> calInterestAndBuildTally(UFDate distilldate, FundTallyVO vo, QueryInterestBaseInfo info,
            int caldays, UFDouble dq_denomination) throws BusinessException {
        // ��̨�ʼ���
        List<FundTallyVO> tallyList = new ArrayList<FundTallyVO>();
        // ��������
        UFDate distillbegindate = null;
        // ��������ղ�Ϊ��
        if (null != vo.getIndistill_date()) {
            distillbegindate = vo.getIndistill_date();
        } else {
            distillbegindate = vo.getBegindate();
        }
        // ���
        UFDouble denomination = vo.getDenomination();
        // ִ��Ʊ������/100
        UFDouble cal_rate = pm.div(vo.getExcutefacerate(), new UFDouble(100));
        // ���μ�����Ϣ
        UFDouble this_interest = new UFDouble(0.00);
        // �Ѽ�����Ϣ
        UFDouble had_interest = new UFDouble(0.00);
        // ���μ�����Ϣ=�����*ִ��Ʊ�����ʣ�/���Ϣ����
        this_interest = pm.div(pm.multiply(denomination, cal_rate), new UFDouble(vo.getYearcaldays()));
        // ������ڼ�������ղ�Ϊ�գ�˵�����ǵ�һ�μ���
        if (null != vo.getIndistill_date()) {
            // �Ѽ�����Ϣ=�ϴ��Ѽ�����Ϣ
            had_interest = vo.getHad_interest();
        }
        FundTallyVO tally = null;
        // ѭ�����μ�Ϣ����������ÿ���̨�ˣ���β����ͷ��
        for (int i = 0; i < caldays; i++) {
            // ��Ϣ��=��Ϣ�գ�����һ�������̨������
            if (null == tally && distillbegindate.isSameDate(vo.getBegindate()) && vo.getOperindex().equals(0)) {
                distillbegindate = distillbegindate.getDateAfter(0);
            } else {
                // ��Ϣ������Ϊ��һ��
                distillbegindate = distillbegindate.getDateAfter(1);
            }
            if (i == 0) {
                // ��һ��ʹ��̨�˱����������ݽ��й���
                tally = this.buildTallyVO(distillbegindate, vo, this_interest, had_interest, 0, dq_denomination);
            } else {
                // �ڶ��쿪ʼʹ���ϴι�����̨��VO���й���
                tally = this.buildTallyVO(distillbegindate, tally, this_interest, had_interest, 1, dq_denomination);
            }
            tallyList.add(tally);
        }
        return tallyList;
    }

    /**
     * ����̨��
     * 
     * @param distillbegindate ���Ὺʼ��
     * @param vo Ҫ�����̨�����ݣ��򹹽�����һ��̨������
     * @param this_interest ����õı��˼�����Ϣ
     * @param had_interest Ҫ�����̨���е��Ѽ�����Ϣ������Ϊ0��
     * @param caldays ���μ�Ϣ������
     * @param type ����̨�˷�ʽ��0-���ι����ĵ�һ�죬ʹ��vo������1-ʹ�ù�������һ�����ݹ���
     * @param info ���ڵ����
     * @return ������̨��VO
     * @author QS
     * @date 2017��6��21�� ����4:06:12
     */
    private FundTallyVO buildTallyVO(UFDate distillbegindate, FundTallyVO vo, UFDouble this_interest, UFDouble had_interest,
            int type, UFDouble dq_denomination) {
        // ȡ��ǰϵͳʱ����Ϊҵ��������
        UFDate operdate = AppContext.getInstance().getServerTime().getDate();
        // ��¡vo
        FundTallyVO tallyVO = (FundTallyVO) vo.clone();
        // ������Ϊ��
        tallyVO.setPk_fundtally(null);
        // ҵ��������
        tallyVO.setOperdate(operdate);
        /**
         * ����Ϊ������Ϣ
         */
        // ���μ��Ὺʼ����
        tallyVO.setBegindistill_date(distillbegindate);
        // ���μ�������
        tallyVO.setIndistill_date(distillbegindate);
        // ���μ�����Ϣ
        tallyVO.setThis_interest(this_interest);
        if (type == 0) {
            // �Ѽ�����Ϣ=������Ϣ+�Ѽ�����Ϣ
            tallyVO.setHad_interest(pm.add(vo.getThis_interest(), had_interest));
        } else if (type == 1) {
            // �Ѽ�����Ϣ=�ϴμ�����Ϣ+��������һ���̨�����Ѽ�����Ϣ
            tallyVO.setHad_interest(pm.add(vo.getThis_interest(), vo.getHad_interest()));
        }
        // ����������ڵ��ڵ����գ���������
        if (null != dq_denomination && distillbegindate.isSameDate(vo.getEnddate())) {
            // ���
            tallyVO.setDenomination(pm.sub(vo.getDenomination(), dq_denomination));
            // ���ڱ�ʶ
            tallyVO.setIsdq(new UFBoolean(true));
        }
        if (type == 0) {
            // ���������1
            tallyVO.setOperindex(vo.getOperindex() + 1);
        } else if (type == 1) {
            // ���������1
            tallyVO.setOperindex(tallyVO.getOperindex() + 1);
        }
        tallyVO.setAttributeValue("dr", 0);
        return tallyVO;
    }

    /**
     * ����Ӧ����Ϣ���ᵥ��VO
     * 
     * @param lastTally �������������һ��̨��
     * @param vo Ҫ�����̨������
     * @param distilldate ��������
     * @param this_allinterest ���μ�������Ϣ
     * @param caldays ���μ�Ϣ������
     * @param info ���ڵ����
     * @return Ӧ����Ϣ���ᵥ��VO
     * @throws BusinessException
     * @author QS
     * @date 2017��6��22�� ����9:41:09
     */
    private PayableInterestCalVO buildDistillVO(FundTallyVO lastTally, FundTallyVO vo, UFDouble this_allinterest, int caldays,
            UFDouble dq_denomination, UFDate distilldate) throws BusinessException {
        String pk_org = vo.getPk_org();
        String pk_group = lastTally.getPk_group();
        PayableInterestCalVO distillvo = new PayableInterestCalVO();
        // ��������
        distillvo.setDistill_date(distilldate);
        // ��Ʒ���������
        distillvo.setProductorcounterparty(lastTally.getProductorcounterparty());
        // �г�
        distillvo.setMarket(vo.getMarket());
        // ���
        distillvo.setDenomination(vo.getDenomination());
        // ���
        if (null != dq_denomination) {
            distillvo.setDenomination(pm.sub(vo.getDenomination(), dq_denomination));
        }
        // ��ͬ��
        distillvo.setContractno(vo.getContractno());
        // ������
        distillvo.setState(SystemConst.statecode[1]);
        /**
         * ����Ϊ������Ϣ��Ϣ
         */
        // ������Ϣ=����*((���*ִ��Ʊ������)/���Ϣ����)
        UFDouble all_interest = pm.multiply(
                new UFDouble(vo.getTimelimit()),
                pm.div(pm.multiply(vo.getDenomination(), pm.div(vo.getExcutefacerate(), new UFDouble(100))),
                        new UFDouble(vo.getYearcaldays())));
        // ���μ�����Ϣ
        distillvo.setThis_cal_interest(this_allinterest);
        // �Ѽ�����Ϣ
        distillvo.setAlready_cal_interest(pm.add(vo.getThis_interest(), vo.getHad_interest()));
        // δ������Ϣ=������Ϣ-���μ�����Ϣ-�Ѽ�����Ϣ
        distillvo.setNot_cal_interest(pm.sub(pm.sub(all_interest, this_allinterest),
                pm.add(vo.getThis_interest(), vo.getHad_interest())));
        // ���Ὺʼ��
        distillvo.setCal_startdate(null == vo.getIndistill_date() ? vo.getBegindistill_date().asBegin() : vo.getIndistill_date()
                .asBegin().getDateAfter(1));
        // ���������
        distillvo.setCal_enddate(lastTally.getIndistill_date());
        // ��������
        distillvo.setCal_days(caldays);
        // ��ȡӦ����Ϣ����ĵ�������VO
        BilltypeVO billtypeVO = PfDataCache.getBillTypeInfo("HV8F");
        // ��������
        distillvo.setPk_billtype(billtypeVO.getPk_billtypeid());
        // ��������
        distillvo.setBilltypecode("HV8F");
        // �������ͱ���
        distillvo.setTranstypecode("HV8F-0xx-01");
        distillvo.setPk_transtype(PfDataCache.getBillTypeInfo("HV8F-0xx-01").getPk_billtypeid());
        // ���ݺ�
        distillvo.setBillno(this.bng.next());
        distillvo.setPk_capaccount(vo.getPk_capaccount());
        distillvo.setPk_client(vo.getPk_client());
        distillvo.setPk_org(pk_org);
        distillvo.setPk_group(pk_group);
        distillvo.setPk_org_v(vo.getPk_org_v());
        distillvo.setPk_glorgbook(vo.getPk_glorgbook());
        distillvo.setPk_operatesite(vo.getPk_operatesite());
        distillvo.setPk_partnaccount(vo.getPk_partnaccount());
        distillvo.setPk_selfsgroup(vo.getPk_selfsgroup());
        distillvo.setStartdate(vo.getBegindate());
        distillvo.setEnddate(vo.getEnddate());
        distillvo.setBillmaker(AppContext.getInstance().getPkUser());
        distillvo.setMakedate(AppContext.getInstance().getServerTime());
        distillvo.setPk_busitype(vo.getPk_busitype());
        distillvo.setAttributeValue("dr", 0);
        return distillvo;
    }

    /**
     * ȡ��Ӧ����Ϣ����
     * 
     * @author qs
     * @since 2017-6-9����9:51:03
     */
    public void cancelCal(LoginContext context, UFDate trade_date) throws BusinessException {
        // У��
        try {
            PayableInterestCalCheck check = new PayableInterestCalCheck();
            check.unCheckJt(context, trade_date);
            // ��ѯ����ʵʱƾ֤�ĵ���
            QueryInterestBaseInfo info = new QueryInterestBaseInfo();
            AggPayableInterestCalVO[] data = info.queryDelVoucherData(context, trade_date);
            // ��ѯ��Ӧ�Ļع�̨������
            List<?> list = info.getTallyVOS(context, trade_date, 1);
            // ���ƾ֤����
            if (data != null && data.length > 0)
                SendVoucherTool.getInstance().deleteRTVoucher(data);
            // ���̨������
            basedao.deleteVOList(list);
            // ���Ӧ����Ϣ��������
            info.clearData(context, trade_date);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * ����
     * 
     * @param context
     * @throws BusinessException
     */
    public void tally(LoginContext context) throws BusinessException {
        try {
            QueryInterestBaseInfo info = new QueryInterestBaseInfo();
            // ��ѯδ����ʵʱƾ֤�ĵ���
            AggPayableInterestCalVO[] data = info.querytallVoucherData(context);
            if (data != null && data.length > 0) {
                // ����ƾ֤
                new DistillSendVoucher().addRTVoucher(data);
                // ���ĵ���״̬
                info.updatetallVoucherData(data);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * ���ݺ�������
     * 
     * @param size ������һ�����ɵĸ���
     * @param vo ���ݺŵ�VO
     * @throws BusinessException
     * 
     * @author JINGQT
     * @since 2017��7��10�� ����11:49:24
     */
    private void initBNG(int size, FundTallyVO vo) throws BusinessException {
        if (null == bng) {
            this.bng = new BillNoGenerator(size, vo);
        }
    }

    class BillNoGenerator {
        /** ��ʼ��С */
        private int size = 0;
        private FundTallyVO vo;
        private String[] lst;
        private int loc = 0;

        public BillNoGenerator(int size, FundTallyVO vo) throws BusinessException {
            this.size = size;
            this.vo = vo;
            this.init();
        }

        /**
         * ����ֵ��
         * 
         * @throws BusinessException
         * @author JINGQT
         * @since 2017��7��10�� ����11:18:32
         */
        private void init() throws BusinessException {
            try {
                this.lst = PubMethod.getInstance().getBatchBillCodes("HV8F", vo.getPk_group(), vo.getPk_org(), null, size);
            } catch (BusinessException e) {
                throw new BusinessException("���ɵ��ݺ�ʧ�ܣ�̨�����ݴ���", e);
            }
        }

        public String next() throws BusinessException {
            String rtn = null;
            if (loc == size) {
                this.init();
            }
            rtn = lst[loc];
            loc++;
            return rtn;
        }
    }
}
