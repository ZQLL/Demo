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
 * 应付利息计提BP
 * 
 * @author qs
 * @since 2017-7-4上午11:19:30
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
     * 应付利息计提
     * 
     * @param context
     * @param trade_date 交易日期
     * @return 应付利息计提数据
     * @throws
     * @author QS
     * @date 2017年6月22日 上午10:40:12
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PayableInterestCalVO[] calInterest(LoginContext context, UFDate trade_date) throws BusinessException {
        try {
            // 1.校验
            PayableInterestCalCheck check = new PayableInterestCalCheck();
            check.checkCal(context, trade_date);
            QueryInterestBaseInfo info = new QueryInterestBaseInfo();
            // 2.查询可计提的数据
            List list = info.getTallyVOS(context, trade_date, 0);
            // 3.开始计息
            UFDate distilldate = trade_date;
            // 构建的台账
            List<FundTallyVO> tallyList = new ArrayList<FundTallyVO>();
            // 结果
            List<PayableInterestCalVO> distillList = new ArrayList<PayableInterestCalVO>();
            if (list != null && list.size() > 0) {
                FundTallyVO[] vos = new FundTallyVO[list.size()];
                vos = (FundTallyVO[]) list.toArray(new FundTallyVO[0]);
                this.initBNG(list.size(), vos[0]);
                for (FundTallyVO vo : vos) {
                    // 到期的面额
                    UFDouble dq_denomination = null;
                    List dqlist = info.getDqTallyVOs(context, distilldate, vo);
                    if (dqlist != null && dqlist.size() > 0) {
                        FundTallyVO[] dqvos = new FundTallyVO[dqlist.size()];
                        dqvos = (FundTallyVO[]) dqlist.toArray(new FundTallyVO[0]);
                        dq_denomination = dqvos[0].getDenomination();
                    }
                    // 计算本次计提总天数
                    int caldays = this.getCalDays(distilldate, vo);
                    // 构建台账
                    List<FundTallyVO> tally = this.calInterestAndBuildTally(distilldate, vo, info, caldays, dq_denomination);
                    if (null != tally && tally.size() != 0) {
                        // 本次计提总利息
                        UFDouble this_allinterest = new UFDouble(0.00);
                        for (FundTallyVO tallyVO : tally) {
                            // 累加
                            this_allinterest = pm.add(this_allinterest, tallyVO.getThis_interest());
                        }
                        FundTallyVO lastTally = tally.get(tally.size() - 1);
                        // 返回构建的应付利息计提VO
                        PayableInterestCalVO distillvo = this.buildDistillVO(lastTally, vo, this_allinterest, caldays,
                                dq_denomination, distilldate);
                        tallyList.addAll(tally);
                        // 将台账数据添加到应付利息计提单据vo中，以供保存单据数据时保存台账数据
                        distillvo.setObj(tallyList);
                        // 循环将vo添加至distillList，以供后期显示单据数据及保存
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
     * 计算本次计提总天数
     * 
     * @param distilldate 计提日期
     * @param vo 要计提的台账数据
     * @return int 本次计提总天数
     * @author QS
     * @date 2017年6月22日 上午10:33:54
     */
    private int getCalDays(UFDate distilldate, FundTallyVO vo) {
        // 计息天数
        int caldays = 0;
        // 计提日期在到期日之前,说明没到期
        if (distilldate.asBegin().before(vo.getEnddate().asBegin())) {
            // 计提结束日有值
            if (null != vo.getIndistill_date()) {
                caldays = UFDate.getDaysBetween(vo.getIndistill_date().asBegin(), distilldate);
            } else {
                // 计提日=起息日，计息一天
                if (distilldate.isSameDate(vo.getBegindate())) {
                    caldays = 1;
                } else {
                    // 第一次计提：算头算尾
                    caldays = UFDate.getDaysBetween(vo.getBegindate().asBegin(), distilldate) + 1;
                }
            }
        } else {
            if (null != vo.getIndistill_date()) {
                // 计提日期不在到期日之前，则取到期日计算应付利息天数
                caldays = UFDate.getDaysBetween(vo.getIndistill_date().asBegin(), vo.getEnddate().asBegin());
            } else {
                // 计提结束日为空，说明没有计提过，取起息日，到期日来计算
                caldays = UFDate.getDaysBetween(vo.getBegindate().asBegin(), vo.getEnddate().asBegin()) + 1;
            }
        }
        return caldays;
    }

    /**
     * 计算台账利息
     * 
     * @param distilldate 计提日期
     * @param vo 要计提的台账数据
     * @param info 台账查询方法类
     * @param info 到期的面额
     * @return List<FundTallyVO> 返回类型 台账VO的list
     * @throws BusinessException
     * @author QS
     * @date 2017年6月21日 下午3:35:21
     */
    private List<FundTallyVO> calInterestAndBuildTally(UFDate distilldate, FundTallyVO vo, QueryInterestBaseInfo info,
            int caldays, UFDouble dq_denomination) throws BusinessException {
        // 新台帐集合
        List<FundTallyVO> tallyList = new ArrayList<FundTallyVO>();
        // 计提日期
        UFDate distillbegindate = null;
        // 计提结束日不为空
        if (null != vo.getIndistill_date()) {
            distillbegindate = vo.getIndistill_date();
        } else {
            distillbegindate = vo.getBegindate();
        }
        // 面额
        UFDouble denomination = vo.getDenomination();
        // 执行票面利率/100
        UFDouble cal_rate = pm.div(vo.getExcutefacerate(), new UFDouble(100));
        // 本次计提利息
        UFDouble this_interest = new UFDouble(0.00);
        // 已计提利息
        UFDouble had_interest = new UFDouble(0.00);
        // 本次计提利息=（面额*执行票面利率）/年计息天数
        this_interest = pm.div(pm.multiply(denomination, cal_rate), new UFDouble(vo.getYearcaldays()));
        // 如果上期计提结束日不为空，说明不是第一次计提
        if (null != vo.getIndistill_date()) {
            // 已计提利息=上次已计提利息
            had_interest = vo.getHad_interest();
        }
        FundTallyVO tally = null;
        // 循环本次计息天数，构建每天的台账（算尾不算头）
        for (int i = 0; i < caldays; i++) {
            // 计息日=起息日，构建一条当天的台账数据
            if (null == tally && distillbegindate.isSameDate(vo.getBegindate()) && vo.getOperindex().equals(0)) {
                distillbegindate = distillbegindate.getDateAfter(0);
            } else {
                // 计息日期设为下一天
                distillbegindate = distillbegindate.getDateAfter(1);
            }
            if (i == 0) {
                // 第一天使用台账表里最新数据进行构建
                tally = this.buildTallyVO(distillbegindate, vo, this_interest, had_interest, 0, dq_denomination);
            } else {
                // 第二天开始使用上次构建的台账VO进行构建
                tally = this.buildTallyVO(distillbegindate, tally, this_interest, had_interest, 1, dq_denomination);
            }
            tallyList.add(tally);
        }
        return tallyList;
    }

    /**
     * 构建台账
     * 
     * @param distillbegindate 计提开始日
     * @param vo 要计提的台账数据，或构建的上一天台账数据
     * @param this_interest 计算好的本此计提利息
     * @param had_interest 要计提的台账中的已计提利息（可能为0）
     * @param caldays 本次计息总天数
     * @param type 构建台账方式：0-本次构建的第一天，使用vo构建。1-使用构建的上一天数据构建
     * @param info 到期的面额
     * @return 构建的台账VO
     * @author QS
     * @date 2017年6月21日 下午4:06:12
     */
    private FundTallyVO buildTallyVO(UFDate distillbegindate, FundTallyVO vo, UFDouble this_interest, UFDouble had_interest,
            int type, UFDouble dq_denomination) {
        // 取当前系统时间作为业务发生日期
        UFDate operdate = AppContext.getInstance().getServerTime().getDate();
        // 克隆vo
        FundTallyVO tallyVO = (FundTallyVO) vo.clone();
        // 主键设为空
        tallyVO.setPk_fundtally(null);
        // 业务发生日期
        tallyVO.setOperdate(operdate);
        /**
         * 以下为计提信息
         */
        // 本次计提开始日期
        tallyVO.setBegindistill_date(distillbegindate);
        // 本次计提日期
        tallyVO.setIndistill_date(distillbegindate);
        // 本次计提利息
        tallyVO.setThis_interest(this_interest);
        if (type == 0) {
            // 已计提利息=计提利息+已计提利息
            tallyVO.setHad_interest(pm.add(vo.getThis_interest(), had_interest));
        } else if (type == 1) {
            // 已计提利息=上次计提利息+构建的上一天的台账中已计提利息
            tallyVO.setHad_interest(pm.add(vo.getThis_interest(), vo.getHad_interest()));
        }
        // 如果计提日期等于到期日，则计算面额
        if (null != dq_denomination && distillbegindate.isSameDate(vo.getEnddate())) {
            // 面额
            tallyVO.setDenomination(pm.sub(vo.getDenomination(), dq_denomination));
            // 到期标识
            tallyVO.setIsdq(new UFBoolean(true));
        }
        if (type == 0) {
            // 操作次序加1
            tallyVO.setOperindex(vo.getOperindex() + 1);
        } else if (type == 1) {
            // 操作次序加1
            tallyVO.setOperindex(tallyVO.getOperindex() + 1);
        }
        tallyVO.setAttributeValue("dr", 0);
        return tallyVO;
    }

    /**
     * 构建应付利息计提单据VO
     * 
     * @param lastTally 本机构建的最后一条台账
     * @param vo 要计提的台账数据
     * @param distilldate 计提日期
     * @param this_allinterest 本次计提总利息
     * @param caldays 本次计息总天数
     * @param info 到期的面额
     * @return 应付利息计提单据VO
     * @throws BusinessException
     * @author QS
     * @date 2017年6月22日 上午9:41:09
     */
    private PayableInterestCalVO buildDistillVO(FundTallyVO lastTally, FundTallyVO vo, UFDouble this_allinterest, int caldays,
            UFDouble dq_denomination, UFDate distilldate) throws BusinessException {
        String pk_org = vo.getPk_org();
        String pk_group = lastTally.getPk_group();
        PayableInterestCalVO distillvo = new PayableInterestCalVO();
        // 计提日期
        distillvo.setDistill_date(distilldate);
        // 产品或对手名称
        distillvo.setProductorcounterparty(lastTally.getProductorcounterparty());
        // 市场
        distillvo.setMarket(vo.getMarket());
        // 面额
        distillvo.setDenomination(vo.getDenomination());
        // 面额
        if (null != dq_denomination) {
            distillvo.setDenomination(pm.sub(vo.getDenomination(), dq_denomination));
        }
        // 合同号
        distillvo.setContractno(vo.getContractno());
        // 已启用
        distillvo.setState(SystemConst.statecode[1]);
        /**
         * 以下为所有利息信息
         */
        // 所有利息=期限*((面额*执行票面利率)/年计息天数)
        UFDouble all_interest = pm.multiply(
                new UFDouble(vo.getTimelimit()),
                pm.div(pm.multiply(vo.getDenomination(), pm.div(vo.getExcutefacerate(), new UFDouble(100))),
                        new UFDouble(vo.getYearcaldays())));
        // 本次计提利息
        distillvo.setThis_cal_interest(this_allinterest);
        // 已计提利息
        distillvo.setAlready_cal_interest(pm.add(vo.getThis_interest(), vo.getHad_interest()));
        // 未计提利息=所有利息-本次计提利息-已计提利息
        distillvo.setNot_cal_interest(pm.sub(pm.sub(all_interest, this_allinterest),
                pm.add(vo.getThis_interest(), vo.getHad_interest())));
        // 计提开始日
        distillvo.setCal_startdate(null == vo.getIndistill_date() ? vo.getBegindistill_date().asBegin() : vo.getIndistill_date()
                .asBegin().getDateAfter(1));
        // 计提结束日
        distillvo.setCal_enddate(lastTally.getIndistill_date());
        // 计提天数
        distillvo.setCal_days(caldays);
        // 获取应付利息计提的单据类型VO
        BilltypeVO billtypeVO = PfDataCache.getBillTypeInfo("HV8F");
        // 单据类型
        distillvo.setPk_billtype(billtypeVO.getPk_billtypeid());
        // 单据类型
        distillvo.setBilltypecode("HV8F");
        // 交易类型编码
        distillvo.setTranstypecode("HV8F-0xx-01");
        distillvo.setPk_transtype(PfDataCache.getBillTypeInfo("HV8F-0xx-01").getPk_billtypeid());
        // 单据号
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
     * 取消应付利息计提
     * 
     * @author qs
     * @since 2017-6-9上午9:51:03
     */
    public void cancelCal(LoginContext context, UFDate trade_date) throws BusinessException {
        // 校验
        try {
            PayableInterestCalCheck check = new PayableInterestCalCheck();
            check.unCheckJt(context, trade_date);
            // 查询生成实时凭证的单据
            QueryInterestBaseInfo info = new QueryInterestBaseInfo();
            AggPayableInterestCalVO[] data = info.queryDelVoucherData(context, trade_date);
            // 查询相应的回购台账数据
            List<?> list = info.getTallyVOS(context, trade_date, 1);
            // 清空凭证数据
            if (data != null && data.length > 0)
                SendVoucherTool.getInstance().deleteRTVoucher(data);
            // 清空台账数据
            basedao.deleteVOList(list);
            // 清空应付利息计提数据
            info.clearData(context, trade_date);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 记账
     * 
     * @param context
     * @throws BusinessException
     */
    public void tally(LoginContext context) throws BusinessException {
        try {
            QueryInterestBaseInfo info = new QueryInterestBaseInfo();
            // 查询未生成实时凭证的单据
            AggPayableInterestCalVO[] data = info.querytallVoucherData(context);
            if (data != null && data.length > 0) {
                // 生成凭证
                new DistillSendVoucher().addRTVoucher(data);
                // 更改单据状态
                info.updatetallVoucherData(data);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 单据号生成器
     * 
     * @param size 理论上一次生成的个数
     * @param vo 单据号的VO
     * @throws BusinessException
     * 
     * @author JINGQT
     * @since 2017年7月10日 上午11:49:24
     */
    private void initBNG(int size, FundTallyVO vo) throws BusinessException {
        if (null == bng) {
            this.bng = new BillNoGenerator(size, vo);
        }
    }

    class BillNoGenerator {
        /** 初始大小 */
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
         * 缓存值。
         * 
         * @throws BusinessException
         * @author JINGQT
         * @since 2017年7月10日 上午11:18:32
         */
        private void init() throws BusinessException {
            try {
                this.lst = PubMethod.getInstance().getBatchBillCodes("HV8F", vo.getPk_group(), vo.getPk_org(), null, size);
            } catch (BusinessException e) {
                throw new BusinessException("生成单据号失败！台账数据错误", e);
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
