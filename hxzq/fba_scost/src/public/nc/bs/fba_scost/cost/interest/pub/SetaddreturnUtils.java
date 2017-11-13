package nc.bs.fba_scost.cost.interest.pub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.fba_scost.cost.simtools.CostingTool;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.trade.business.HYPubBO;
import nc.itf.fba_scost.cost.pub.IBillCheckPlugin;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_sec.pub.BilltypeDetailsVO;
import nc.vo.fba_sec.pub.SecSysInitCache;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class SetaddreturnUtils {

	private HYPubBO server = new HYPubBO();
	/**
	 * 根据待处理数据、业务组返回<日期+业务组，单据类型以及插件类>
	 * 
	 * @param billVO
	 * @param groupVO
	 * @return
	 * @throws Exception 
	 */
	public Map<String, List<PendingBillVO>>    AddBillTypeClass(Map<String, List<PendingBillVO>> returnmap,String pk_billtype,BilltypeGroupVO vo,CostingTool costingtool, TradeDataTool tradedatatool,CheckClassTool checkclasstool) throws Exception {
				 List<PendingBillVO> plist = this.getNeedVOs(costingtool,pk_billtype, vo, costingtool.getCostParaVO().getCheckParavo(),  " state = " + SystemConst.statecode[1], SystemConst.checkstate[0]);
					Map<String, HashMap<String, BilltypeDetailsVO>> map = new HashMap();
					CheckParaVO checkParaVO = costingtool.getCostParaVO().getCheckParavo();
					map = SecSysInitCache.getInstance()
							.getCheckPlanDetails(checkParaVO.getPk_group(), checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook());
				 for (int i = 0; i < plist.size(); i++) {
				 
					 
					 PendingBillVO  pbvo=plist.get(i);

						BilltypeDetailsVO bdvo = (BilltypeDetailsVO) ((map.get(checkParaVO.getPk_group() + checkParaVO.getPk_org()
								+ checkParaVO.getPk_glorgbook())).get(pbvo.getPk_billtype()));
						if (bdvo != null) {
							pbvo.setPk_billtypegroup(bdvo.getPk_billtypegroup());
							pbvo.setCheckclass(bdvo.getClassname());
							pbvo.setCheckorder(bdvo.getCheckorder());
							pbvo.setDirection(bdvo.getDirection());
							pbvo.setPk_stocksort1(bdvo.getPk_stocksort1());
							pbvo.setPk_stocksort2(bdvo.getPk_stocksort2());
						}
						String key = pbvo.getTrade_date().toLocalString() + pbvo.getPk_billtypegroup();
						/**
						 * 此处构建ClassList YangJie 2014-03-28
						 */
						String classname = pbvo.getCheckclass().trim();
						String transtypecode = pbvo.getPk_billtype().trim();
						IBillCheckPlugin isimcheck;
						try {
							String[] temp = classname.split("\\.");
							isimcheck = (IBillCheckPlugin) NewObjectService.newInstance(temp[2], classname);
						} catch (Exception e) {
							throw new BusinessException(pbvo.getCheckclass() + "实例化失败！" + e.getMessage());
						}
						/***
						 * 初始化审核类
						 */
						checkclasstool.setCheckClassMap(transtypecode + "##" + classname, isimcheck);
						if (!returnmap.containsKey(key)) {
							List innerlist = new ArrayList();
							innerlist.add(pbvo);
							returnmap.put(key, innerlist);
						} else {
							UFBoolean  flag=new UFBoolean(true);
							
							List innerlist = returnmap.get(key);
							for (Object object : innerlist) {
								PendingBillVO billvo = (PendingBillVO )(object);
								if(billvo.getPk_billtype().equals(pbvo.getPk_billtype())){
									  flag=new UFBoolean(false);
								}
							
								
							}
							if(flag.booleanValue()){
								innerlist.add(pbvo);
							}
							
							
						}
					
				}
				 return returnmap;

 						
		} 
	/**
	 * 获取中间表的数据,
	 * 
	 * @param CheckParaVO
	 * @return List<CostPlanVO>
	 */
	private List<PendingBillVO> getNeedVOs(CostingTool costingtool,String pk_billtype, BilltypeGroupVO vo, CheckParaVO checkParaVO, String addcondition, int state) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append(" ( (");
		sb.append(addcondition);
		sb.append(" and pk_group='");
		sb.append(checkParaVO.getPk_group());
		sb.append("' and pk_org='");
		sb.append(checkParaVO.getPk_org());
		sb.append("' and pk_glorgbook='");
		sb.append(checkParaVO.getPk_glorgbook());
		sb.append("')");
		sb.append(" or ( pk_group    ='GLOBLE00000000000000'");
		sb.append(" and pk_org      ='GLOBLE00000000000000'");
		sb.append(" and pk_glorgbook='GLOBLE00000000000000') ) ");
		sb.append(" and pk_billtype      ='"+pk_billtype+"'");
		// 审核
			if (vo.getLastapprovedate() != null) {
				sb.append(" and trade_date>='");
				sb.append(new UFDate(costingtool.getCurrdate()).toLocalString().substring(0, 10) + " 00:00:00' ");
			}
			sb.append(" and trade_date<='");
			sb.append(new UFDate(costingtool.getCurrdate()).toLocalString().substring(0, 10) + " 23:59:59' ");
	 
		List<PendingBillVO> plist = PubMethod.getInstance().queryAllByCond(new PendingBillVO(), sb.toString());
		if(plist==null||plist.size()==0){
			PendingBillVO pb =this.bulidPendingBill(vo, checkParaVO, pk_billtype, new UFDate(costingtool.getCurrdate()));
			String pk = server.insert(pb) ;
			PendingBillVO	pb1=(PendingBillVO) server.queryByPrimaryKey(PendingBillVO.class, pk);
			if(plist==null){
				plist=	new ArrayList();
			}
			plist.add(pb1);
		}
		return plist;
	}
	
	public PendingBillVO bulidPendingBill(BilltypeGroupVO vo, CheckParaVO checkParaVO,
			String transtypecode,UFDate trade_date){
		PendingBillVO pbvo = new PendingBillVO();
		String pk_group = checkParaVO.getPk_group();
		String pk_org = checkParaVO.getPk_org();
		String pk_org_v = vo.getApproveVO().getPk_org_v();
		String pk_glorgbook = checkParaVO.getPk_glorgbook();
		trade_date = new UFDate(trade_date.toLocalString());
		pbvo.setAttributeValue("dr", 0);
		pbvo.setAttributeValue("pk_group", pk_group);
		pbvo.setAttributeValue("pk_org", pk_org);
		pbvo.setAttributeValue("pk_org_v", pk_org_v);
		pbvo.setAttributeValue("pk_glorgbook", pk_glorgbook);
		pbvo.setAttributeValue("pk_billtype", transtypecode);
		pbvo.setAttributeValue("trade_date", trade_date);
		pbvo.setAttributeValue("state", 0);
		return pbvo;
	}

}
