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
 * ���ʹ����Զ�����
 * 
 * @author QS
 * @date 2017��7��6�� ����2:38:01
 */
public class AutoExpire {
    BaseDAO dao = new BaseDAO();
    private QueryInterestBaseInfo queryInfo = null;
    // �������ͻ���
    Map<String, String> billTypeMap = new HashMap<String, String>();

    private String expire1 = "expire1";
    private String expire2 = "expire2";
    private String expire3 = "expire3";
    private String expire4 = "expire4";
    private String expire5 = "expire5";

    /**
     * ���ʹ����Զ�����ҵ��
     * 
     * @author qs
     * @since 2017-6-13����10:54:42
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
        // �Ƿ��Զ����ڲ���
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
     * ���뻺��
     */
    private void addExpireCache(CostingTool costingtool, TradeDataTool tradedatatool, String[] pks1, String[] pks2,
            String[] pks3, String[] pks4, String[] pks5) throws BusinessException {
        try {
            if (tradedatatool.isIsinit()) {
                // ��ѯ��������1
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
     * @since 2017-6-14����9:29:12
     */
    @SuppressWarnings("unchecked")
    private void addExpireData(CostingTool costingtool, TradeDataTool tradedatatool, String aggName, String[] pks)
            throws BusinessException {
        Class<IBill> cs;
        try {
            cs = (Class<IBill>) Class.forName(aggName);
        } catch (ClassNotFoundException e) {
            throw new BusinessException("�Ҳ�����!" + e.getMessage());
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
     * ����VO�����ݿ�
     * 
     * @author qs
     * @throws DAOException
     * @since 2017-6-14����9:43:51
     */
    private String[] insertVO(String expire, Map<String, List<SuperVO>> map) throws DAOException {
        List<SuperVO> list = map.get(expire);
        return dao.insertVOList(list);
    }

    /**
     * �Զ�������Ϣ
     * 
     * @author qs
     * @since 2017-6-13����11:00:44
     */
    private Map<String, List<SuperVO>> calcAutoExpireInfo(CostingTool costingtool, CostPlanVO costplanvo, String pk_group,
            String pk_org, UFDate trade_date) throws BusinessException {
        // ��ѯ��������bd_billtype
        String sql = "isnull(dr,0)=0 and pk_billtypecode like 'HV8%' ";
        List<BilltypeVO> billTypeList = (List<BilltypeVO>) dao.retrieveByClause(BilltypeVO.class, sql);
        for (BilltypeVO vo : billTypeList) {
            // key-�������ͱ��� vlue-�������ͱ����Ӧ��pk
            billTypeMap.put(vo.getPk_billtypecode(), vo.getPk_billtypeid());
        }
        List<InventoryInfoVO> list = this.getQueryInfo().queryLastInventory(costplanvo, pk_group, pk_org, trade_date);
        if (list == null || list.size() == 0) {
            return null;
        }

        Map<String, List<SuperVO>> map = new HashMap<String, List<SuperVO>>();
        for (InventoryInfoVO v : list) {
            // list�е����ݿ����е��Ѿ������˵��ڵ����Ͳ������������ˡ�
            boolean bln = this.checkWhetherAutoCreateBill(v);
            // ����true˵����Ҫ���ɵ��ڵ�,��������ˡ�
            if (bln) {
                this.buildExpireBill(pk_group, pk_org, v, trade_date, map);
            }
        }
        return map;
    }

    /**
     * ��鵥���Ƿ���Ҫ������Ӧ�ĵ��ڵ���
     * 
     * @param checkVO Ҫ����VO
     * @return false˵������Ҫ���ɵ��ڵ���
     * @author qs
     * @throws DAOException
     * @since 2017-6-13����10:02:40
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
                    // ���set�����ظ�ֵ�ˣ���ֱ�ӷ���false�������Զ����ɵ��ڵ���
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
     * ��ѯ��ǰ�����Ƿ���
     */
    private void buildExpireBill(String pk_group, String pk_org, InventoryInfoVO stockvo, UFDate trade_date,
            Map<String, List<SuperVO>> map) throws BusinessException {
        // ���ڵ�
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
     * �����ظ�����
     * 
     * @author qs
     * @since 2017-6-13����10:47:51
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
     * �������ڵ�vo
     * 
     * @author qs
     * @since 2017-6-13����12:28:44
     */
    private SuperVO getExpireBill(String pk_group, String pk_org, InventoryInfoVO stockvo, UFDate trade_date)
            throws BusinessException {
        // �������ͱ���
        String trans_type = stockvo.getTranstypecode();
        // ������ӦVOʵ��
        SuperVO vo = constructDiffVO(trans_type);
        // ��ѯ�ú�ͬ�ŵ�ԭʼ����
        String whereSql = "isnull(dr,0)=0 and contractno = " + "'" + stockvo.getContractno() + "'";
        List<?> list = (List<?>) dao.retrieveByClause(vo.getClass(), whereSql);
        vo = VODeepCloneUtil.deepClone((SuperVO) list.get(0));
        // ����״̬
        vo.setAttributeValue("state", 0);
        // ���õ��ڵ��Ľ�������Ϊ�ɽ����ĵ�������
        vo.setAttributeValue("trade_date", stockvo.getEnddate());
        // Ϊ�����ٺ��ڵ���˵������Զ����ɵ��ݺţ�������Ҫ ��ա�
        vo.setAttributeValue("billno", DBConsts.NULL_WAVE);
        // ���ݳɽ��������������õ��ڵ��Ľ������ͱ���ͺͽ�������pk
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
     * ���ݽ������͹����ӦVOʵ��
     * 
     * @author qs
     * @since 2017-6-13����12:30:32
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
            throw new BusinessException("VO��ʵ��������!");
        }
        return vo;
    }

    /**
     * ������ѯ��ͬ�ŵ�SQL
     * 
     * @param tableName ����
     * @param contractno ��ͬ��
     * @author qs
     * @since 2017-6-13����10:16:33
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
