package nc.bs.fba_scost.cost.interest.pub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_scost.cost.simtools.CostingTool;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.util.DBConsts;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.pub.tools.QueryUtil;
import nc.vo.fba_scost.pub.tools.VODeepCloneUtil;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * 筹资管理自动到期
 * 
 * @author QS
 * @date 2017年7月6日 下午2:38:01
 */
public class AutoExpire {
    BaseDAO dao = new BaseDAO();
    private QueryInterestBaseInfo queryInfo = null;
    // 单据类型缓存
    Map<String, String> billTypeMap = new HashMap<String, String>();

    private String expire1 = "expire1";
    private String expire2 = "expire2";
    private String expire3 = "expire3";
    private String expire4 = "expire4";
    private String expire5 = "expire5";

    /**
     * 筹资管理自动到期业务
     * 
     * @author qs
     * @since 2017-6-13上午10:54:42
     */
    public void autoExpire(CostingTool costingtool, TradeDataTool tradedatatool) throws BusinessException {
        CostParaVO costpara = costingtool.getCostParaVO();
        CostPlanVO costplanvo = costpara.getCostplanvo();
        UFDate trade_date = new UFDate(costingtool.getCurrdate());
        //
        CheckParaVO checkparavo = costpara.getCheckParavo();
        String pk_glorgbook = checkparavo.getPk_glorgbook();
        String pk_group = checkparavo.getPk_group();
        String pk_org = checkparavo.getPk_org();
        // 是否自动到期参数
        boolean flag = getQueryInfo().getBooleanFromInitcode(pk_glorgbook, CostConstant.PARAM_ISAUTOEXPIRE);
        if (!flag) {
            return;
        }
        Map<String, List<SuperVO>> map = calcAutoExpireInfo(costingtool, costplanvo, pk_group, pk_org, trade_date);
        if (map != null && map.size() > 0) {
            String[] pks1 = null;
            String[] pks2 = null;
            String[] pks3 = null;
            String[] pks4 = null;
            String[] pks5 = null;

            if (map.containsKey(expire1)) {
                pks1 = this.insertVO(expire1, map);
            }
            if (map.containsKey(expire2)) {
                pks2 = this.insertVO(expire2, map);
            }
            if (map.containsKey(expire3)) {
                pks3 = this.insertVO(expire3, map);
            }
            if (map.containsKey(expire4)) {
                pks4 = this.insertVO(expire4, map);
            }
            if (map.containsKey(expire5)) {
                pks5 = this.insertVO(expire5, map);
            }
            addExpireCache(costingtool, tradedatatool, pks1, pks2, pks3, pks4, pks5);
        }
    }

    /**
     * 加入缓存
     */
    private void addExpireCache(CostingTool costingtool, TradeDataTool tradedatatool, String[] pks1, String[] pks2,
            String[] pks3, String[] pks4, String[] pks5) throws BusinessException {
        try {
            if (tradedatatool.isIsinit()) {
                // 查询最新数据1
                if (pks1 != null && pks1.length > 0) {
                    this.addExpireData(costingtool, tradedatatool, CostConstant.AGGBONDSPAYABLEVONAME, pks1);
                }
                if (pks2 != null && pks2.length > 0) {
                    this.addExpireData(costingtool, tradedatatool, CostConstant.AGGGOFINANCINGVONAME, pks2);
                }
                if (pks3 != null && pks3.length > 0) {
                    this.addExpireData(costingtool, tradedatatool, CostConstant.AGGINTERBANKLENDINGVONAME, pks3);
                }
                if (pks4 != null && pks4.length > 0) {
                    this.addExpireData(costingtool, tradedatatool, CostConstant.AGGPROFITTRANSFERREPOVONAME, pks4);
                }
                if (pks5 != null && pks5.length > 0) {
                    this.addExpireData(costingtool, tradedatatool, CostConstant.AGGSHORTFUNDVONAME, pks5);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 
     * 
     * @author qs
     * @throws BusinessException
     * @since 2017-6-14上午9:29:12
     */
    @SuppressWarnings("unchecked")
    private void addExpireData(CostingTool costingtool, TradeDataTool tradedatatool, String aggName, String[] pks)
            throws BusinessException {
        Class<IBill> cs;
        try {
            cs = (Class<IBill>) Class.forName(aggName);
        } catch (ClassNotFoundException e) {
            throw new BusinessException("找不到类!" + e.getMessage());
        }
        IBill[] ibills = QueryUtil.queryBillVOByPks(cs, pks, true);
        for (int j = 0; j < ibills.length; j++) {
            IBill ibill = ibills[j];
            String key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
                    + ((UFDate) (ibill.getParent().getAttributeValue("trade_date"))).toLocalString()
                    + ibill.getParent().getAttributeValue("transtypecode");
            tradedatatool.addData(key, ibill);
        }
    }

    /**
     * 插入VO到数据库
     * 
     * @author qs
     * @throws DAOException
     * @since 2017-6-14上午9:43:51
     */
    private String[] insertVO(String expire, Map<String, List<SuperVO>> map) throws DAOException {
        List<SuperVO> list = map.get(expire);
        return dao.insertVOList(list);
    }

    /**
     * 自动到期信息
     * 
     * @author qs
     * @since 2017-6-13上午11:00:44
     */
    private Map<String, List<SuperVO>> calcAutoExpireInfo(CostingTool costingtool, CostPlanVO costplanvo, String pk_group,
            String pk_org, UFDate trade_date) throws BusinessException {
        // 查询单据类型bd_billtype
        String sql = "isnull(dr,0)=0 and pk_billtypecode like 'HV8%' ";
        List<BilltypeVO> billTypeList = (List<BilltypeVO>) dao.retrieveByClause(BilltypeVO.class, sql);
        for (BilltypeVO vo : billTypeList) {
            // key-单据类型编码 vlue-单据类型编码对应的pk
            billTypeMap.put(vo.getPk_billtypecode(), vo.getPk_billtypeid());
        }
        List<InventoryInfoVO> list = this.getQueryInfo().queryLastInventory(costplanvo, pk_group, pk_org, trade_date);
        if (list == null || list.size() == 0) {
            return null;
        }

        Map<String, List<SuperVO>> map = new HashMap<String, List<SuperVO>>();
        for (InventoryInfoVO v : list) {
            // list中的数据可能有的已经生成了到期单，就不能重新生成了。
            boolean bln = this.checkWhetherAutoCreateBill(v);
            // 返回true说明需要生成到期单,否则结束了。
            if (bln) {
                this.buildExpireBill(pk_group, pk_org, v, trade_date, map);
            }
        }
        return map;
    }

    /**
     * 检查单子是否需要生成相应的到期单。
     * 
     * @param checkVO 要检查的VO
     * @return false说明不需要生成到期单了
     * @author qs
     * @throws DAOException
     * @since 2017-6-13下午10:02:40
     */
    private boolean checkWhetherAutoCreateBill(InventoryInfoVO checkVO) throws DAOException {
        String[] tables = { "FUND_BONDSPAYABLE", "FUND_GOFINANCING", "FUND_INTERBANKLENDING", "FUND_PROFITTRANSFERREPO",
                "FUND_SHORTFUND" };
        StringBuilder sb = new StringBuilder();
        for (String tableName : tables) {
            sb.append(this.createUnionSql(tableName, checkVO.getContractno()));
            sb.append(" union all ");
        }
        String sql = sb.substring(0, sb.length() - 11);
        boolean bln = (Boolean) this.dao.executeQuery(sql, new ResultSetProcessor() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object handleResultSet(ResultSet rs) throws SQLException {
                if (null == rs) {
                    return false;
                }
                Set<String> set = new HashSet<String>();
                while (rs.next()) {
                    String tmp = rs.getString(1);
                    if (null == tmp || tmp.length() == 0) {
                        return false;
                    }
                    // 如果set中有重复值了，则直接返回false，不再自动生成到期单了
                    boolean bln = set.add(tmp);
                    if (!bln) {
                        return false;
                    }
                }
                return true;
            }
        });
        return bln;
    }

    /**
     * 查询当前筹资是否到期
     */
    private void buildExpireBill(String pk_group, String pk_org, InventoryInfoVO stockvo, UFDate trade_date,
            Map<String, List<SuperVO>> map) throws BusinessException {
        // 到期单
        SuperVO vo = this.getExpireBill(pk_group, pk_org, stockvo, trade_date);
        String transtypecode = (String) vo.getAttributeValue("transtypecode");
        if (transtypecode==SystemConst.BONDSPAYABLE_02){
            this.updateMapInfo(map, expire1, vo);
            return;
        }
        else if (transtypecode==SystemConst.GOFINANCING_02){
            this.updateMapInfo(map, expire2, vo);
            return;
        }
        else if (transtypecode==SystemConst.INTERBANKLENDING_02){
            this.updateMapInfo(map, expire3, vo);
            return;
		}	
        else if (transtypecode==SystemConst.PROFITTRANSFERREPO_02){
            this.updateMapInfo(map, expire4, vo);
            return;
        }
        else if (transtypecode==SystemConst.SHORTFUND_02){
            this.updateMapInfo(map, expire5, vo);
            return;
        }else
        	return;
        
    }


    /**
     * 减少重复操作
     * 
     * @author qs
     * @since 2017-6-13下午10:47:51
     */
    private void updateMapInfo(Map<String, List<SuperVO>> map, String key, SuperVO vo) {
        if (map.containsKey(key)) {
            map.get(key).add(vo);
        } else {
            List<SuperVO> list = new ArrayList<SuperVO>();
            list.add(vo);
            map.put(key, list);
        }
    }

    /**
     * 构建到期单vo
     * 
     * @author qs
     * @since 2017-6-13下午12:28:44
     */
    private SuperVO getExpireBill(String pk_group, String pk_org, InventoryInfoVO stockvo, UFDate trade_date)
            throws BusinessException {
        // 交易类型编码
        String trans_type = stockvo.getTranstypecode();
        // 构建对应VO实例
        SuperVO vo = constructDiffVO(trans_type);
        // 查询该合同号的原始单据
        String whereSql = "isnull(dr,0)=0 and contractno = " + "'" + stockvo.getContractno() + "'";
        List<?> list = (List<?>) dao.retrieveByClause(vo.getClass(), whereSql);
        vo = VODeepCloneUtil.deepClone((SuperVO) list.get(0));
        // 启用状态
        vo.setAttributeValue("state", 0);
        // 设置到期单的交易日期为成交单的到期日期
        vo.setAttributeValue("trade_date", stockvo.getEnddate());
        // 为了能再后期的审核当中能自动生成单据号，现在需要 清空。
        vo.setAttributeValue("billno", DBConsts.NULL_WAVE);
        // 根据成交单交易类型设置到期单的交易类型编码和和交易类型pk
        if(trans_type==SystemConst.BONDSPAYABLE_01){
            vo.setAttributeValue("transtypecode", SystemConst.BONDSPAYABLE_02);
            vo.setAttributeValue("pk_transtype", billTypeMap.get(SystemConst.BONDSPAYABLE_02));
            return vo;
        }
        else if(trans_type==SystemConst.GOFINANCING_01){
            vo.setAttributeValue("transtypecode", SystemConst.GOFINANCING_02);
            vo.setAttributeValue("pk_transtype", billTypeMap.get(SystemConst.GOFINANCING_02));
            return vo;
        }
        else if(trans_type==SystemConst.INTERBANKLENDING_01){
            vo.setAttributeValue("transtypecode", SystemConst.INTERBANKLENDING_02);
            vo.setAttributeValue("pk_transtype", billTypeMap.get(SystemConst.INTERBANKLENDING_02));
            return vo;
        }
        else if(trans_type==SystemConst.PROFITTRANSFERREPO_01){
            vo.setAttributeValue("transtypecode", SystemConst.PROFITTRANSFERREPO_02);
            vo.setAttributeValue("pk_transtype", billTypeMap.get(SystemConst.PROFITTRANSFERREPO_02));
            return vo;
        }
        else if(trans_type==SystemConst.SHORTFUND_01){
            vo.setAttributeValue("transtypecode", SystemConst.SHORTFUND_02);
            vo.setAttributeValue("pk_transtype", billTypeMap.get(SystemConst.SHORTFUND_02));
            return vo;
        }
        else
        	return vo;
        }

    

    /**
     * 根据交易类型构造对应VO实例
     * 
     * @author qs
     * @since 2017-6-13下午12:30:32
     */
    public SuperVO constructDiffVO(String trans_type) throws BusinessException {
        Class<?> cls = null;
        SuperVO vo = null;
        try {
            if(trans_type==SystemConst.BONDSPAYABLE_01){
                cls = Class.forName(CostConstant.BONDSPAYABLEVONAME);
            }
            else if(trans_type==SystemConst.GOFINANCING_01){
                cls = Class.forName(CostConstant.GOFINANCINGVONAME);
            }
            else if(trans_type==SystemConst.INTERBANKLENDING_01){
                cls = Class.forName(CostConstant.INTERBANKLENDINGVONAME);
            }
            else if(trans_type==SystemConst.PROFITTRANSFERREPO_01){
                cls = Class.forName(CostConstant.PROFITTRANSFERREPOVONAME);
            }
            else if(trans_type==SystemConst.SHORTFUND_01){
                cls = Class.forName(CostConstant.SHORTFUNDVONAME);
            }
            vo = (SuperVO) cls.newInstance();
        } catch (Exception e) {
            throw new BusinessException("VO类实例化错误!");
        }
        return vo;
    }

    /**
     * 创建查询合同号的SQL
     * 
     * @param tableName 表名
     * @param contractno 合同号
     * @author qs
     * @since 2017-6-13下午10:16:33
     */
    private String createUnionSql(String tableName, String contractno) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONTRACTNO FROM ");
        sb.append(tableName);
        sb.append(" where contractno = '" + contractno + "'");
        sb.append(" and isnull(dr,0)=0");
        return sb.toString();
    }

    public QueryInterestBaseInfo getQueryInfo() {
        if (queryInfo == null) {
            queryInfo = new QueryInterestBaseInfo();
        }
        return queryInfo;
    }
}
