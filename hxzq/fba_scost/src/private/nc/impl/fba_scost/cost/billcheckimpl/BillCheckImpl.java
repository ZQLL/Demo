package nc.impl.fba_scost.cost.billcheckimpl;

import java.util.*;
import java.util.Comparator;
import java.util.TreeSet;

import nc.bs.fba_scost.cost.fairvaluedistill.bp.FairValueCheck;
import nc.bs.fba_scost.cost.hgdistill.bp.HgDistillCheck;
import nc.bs.fba_scost.cost.interest.bp.InterestDistillCheck;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup;
import nc.itf.fba_scost.cost.billcheck.IBillCheckService;
import nc.itf.fba_scost.cost.pub.IBillCheckPlugin;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.IPubQuery;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.scost.approve.ApproveVO;
import nc.vo.fba_sec.pub.BilltypeDetailsVO;
import nc.vo.fba_sec.pub.SecSysInitCache;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFDate;

/**
 * ��˴���ʵ����
 * 
 * @author LiangWei
 * @editor YangJie
 * @since 2014-04-04
 * 
 */
public class BillCheckImpl implements IBillCheckService {

	
	@Override
	public void tallyAllCheckedData(CheckParaVO checkParaVO)
			throws BusinessException {
		if (null == checkParaVO) {
			// ������ʾ��Ϣ
			throw new BusinessException("ǰ̨����������ϢΪ�գ���˲�!");
		}
		Lock(checkParaVO);
		try {
			BilltypeGroupVO[] groupVOs = checkParaVO.getGroupvos();// ҵ����
			for (int i = 0; i < groupVOs.length; i++) {
				BilltypeGroupVO billtypeGroupVO = groupVOs[i];
				checkParaVO.setTrade_date(billtypeGroupVO.getLastapprovedate());
			}

			for (int i = 0; i < groupVOs.length; i++) {
				TradeDataTool tradedatatool = new TradeDataTool();
				CheckClassTool checkclasstool = new CheckClassTool();
				BilltypeGroupVO vo = groupVOs[i];

				// 1.��ȡ��Ҫ��˵����� state=2 ����˵���
				List<PendingBillVO> plist = getNeedVOs(vo, checkParaVO,
						" state = " + SystemConst.statecode[3],
						SystemConst.checkstate[1]);
				String errormsg = null;
				try {
					SecSysInitCache.getInstance().setused(
							checkParaVO.getPk_group(), checkParaVO.getPk_org(),
							checkParaVO.getPk_glorgbook());
					// 2.��ȡ�����еĲ����
					Map<String, List<PendingBillVO>> dataMap = getBillTypeClass(
							checkParaVO, plist, groupVOs, checkclasstool);
					// 3.�ж���Ҫ��˵�����
					Set<UFDate> set = getCheckdays(plist, 0);
					NCLocator
							.getInstance()
							.lookup(IBillCheckByGroup.class)
							.tallyOneGroup_RequiresNew(vo, set, dataMap,
									checkParaVO, tradedatatool, checkclasstool);
				} catch (BusinessException e) {
					errormsg = e.getMessage();
					Logger.error(e.getMessage());
				} finally {
					clearCache(checkParaVO);

					NCLocator
							.getInstance()
							.lookup(IBillCheckByGroup.class)
							.updateApporveUI_RequiresNew(vo, checkParaVO,
									errormsg, SystemConst.checkstate[1]);
				}
			}

		} catch (Exception e) {
			throw new BusinessException(e);
		} finally {
			UnLock(checkParaVO);
		}

	}

	
	@Override
	public String unCheckBill(CheckParaVO checkParaVO) throws BusinessException {
		String isSuccess = "fail";
		if (null == checkParaVO) {
			// ������ʾ��Ϣ
			throw new BusinessException("ǰ̨����������ϢΪ�գ���˲�!");
		}
		Lock(checkParaVO);
		try {

			IPubQuery ipubquery = NCLocator.getInstance().lookup(
					IPubQuery.class);
			String tallydate = ipubquery.queryTallyVO(
					checkParaVO.getPk_group(), checkParaVO.getPk_org(),
					checkParaVO.getPk_glorgbook());
			if (tallydate
					.compareTo(checkParaVO.getTrade_date().toLocalString()) > 0) {
				throw new BusinessException("�������ڲ��������ڳ��������ڣ�������������ڣ�");
			}
			BilltypeGroupVO[] groupVOs = checkParaVO.getGroupvos();// ҵ����

			/**
			 * ���ҵ����VO�ٴ�ǰ��̨tsУ��
			 * 
			 * @author cjh
			 * @date 2015-12-28
			 */
			ApproveVO[] approvevos = new ApproveVO[groupVOs.length];
			for (int i = 0; i < groupVOs.length; i++) {
				approvevos[i] = groupVOs[i].getApproveVO();
			}
			if (!PubMethod.getInstance().checkIsAppoveUI(approvevos)) {
				throw new BusinessException("�����쳣��ǰ��̨���ݲ�һ�£���ˢ������!");
			}
			// end_cjh

			boolean ischeck = false;
			for (int i = 0; i < groupVOs.length; i++) {
				BilltypeGroupVO billtypeGroupVO = groupVOs[i];
				if (billtypeGroupVO.getLastapprovedate().toLocalString()
						.compareTo(checkParaVO.getTrade_date().toLocalString()) < 0) {
					throw new BusinessException("�������ڲ��ܴ���ѡ��ҵ�����������ڣ�������������ڣ�");
				}
				if (CostConstant.COSTGROUP.equals(billtypeGroupVO
						.getPk_billtypegroup())) {
					ischeck = true;
				}

				// У��ع���Ϣ����
				if (CostConstant.HGGROUP.equals(billtypeGroupVO
						.getPk_billtypegroup())) {// �ɱ���������ʼ�ֵ��Ӧ����Ϣ��У��
					String pk_glorgbook = checkParaVO.getPk_glorgbook();
					String pk_group = checkParaVO.getPk_group();
					String pk_org = checkParaVO.getPk_org();
					UFDate trade_date = checkParaVO.getTrade_date();
					// У��ع�����
					HgDistillCheck incheck = new HgDistillCheck();
					incheck.uncheckBill(pk_group, pk_org, pk_glorgbook,
							trade_date);
				}

				// �Ƿ�����ʵ�����ʣ�У��
				if (CostConstant.COSTGROUP.equals(billtypeGroupVO
						.getPk_billtypegroup())) {
					// �Ƿ����ò���SEC32
					String pk_glorgbook = checkParaVO.getPk_glorgbook();
					String pk_group = checkParaVO.getPk_group();
					String pk_org = checkParaVO.getPk_org();
					UFDate trade_date = checkParaVO.getTrade_date();
					String result = SysInitCache.getInstance().getSysInitValue(
							pk_glorgbook, SystemConst.SYSINIT_ISREALRATE);
					if ("Y".equals(result)) {
						ICalcPluginMaintain main = NCLocator.getInstance()
								.lookup(ICalcPluginMaintain.class);
						main.unAuditcheck(pk_group, pk_org, trade_date
								.toUFLiteralDate(ICalendar.BASE_TIMEZONE));
					}
				}
			}
			/**
			 * 1.У�����ҵ�� 2.�������ڴ��ڼ������ڵ� ֻ��Ҫ���� 3.��������С�ڼ������ڵ� ��ȡ������ �� ����
			 */
			if (ischeck) {// �ɱ���������ʼ�ֵ��Ӧ����Ϣ��У��
				String pk_glorgbook = checkParaVO.getPk_glorgbook();
				String pk_group = checkParaVO.getPk_group();
				String pk_org = checkParaVO.getPk_org();
				UFDate trade_date = checkParaVO.getTrade_date();
				// У��Ӧ����Ϣ����
				InterestDistillCheck incheck = new InterestDistillCheck();
				incheck.uncheckBill(pk_group, pk_org, pk_glorgbook, trade_date);
				// У�鹫�ʼ�ֵ����
				FairValueCheck fvcheck = new FairValueCheck();
				fvcheck.unCheckBill(pk_group, pk_org, pk_glorgbook, trade_date);
			}

			// String pk_glorgbook = checkParaVO.getPk_glorgbook();
			// String pk_group = checkParaVO.getPk_group();
			// String pk_org = checkParaVO.getPk_org();
			// StringBuffer conditions = new StringBuffer(" isnull(dr,0)=0 ");
			// if (pk_glorgbook != null) {
			// conditions.append(" and pk_glorgbook='").append(pk_glorgbook).append("' ");
			// }
			// if (pk_group != null) {
			// conditions.append(" and pk_group='").append(pk_group).append("' ");
			// }
			// if (pk_org != null) {
			// conditions.append(" and pk_org='").append(pk_org).append("' ");
			// }
			//
			// conditions.append(" and distill_date>='").append(checkParaVO.getTrade_date().toLocalString()
			// + " 00:00:00").append("' ");
			// String sqlwhere = conditions.toString();
			// if (pm.isExist(FairdistillVO.class, sqlwhere +
			// " and vdef5<>'1' and vdef5<>'3' ")) {
			// throw new Exception("�Ѿ���ȡ��" + trade_date + "���Ժ�Ĺ��ʼ�ֵ������ȡ����");
			// }
			// if (pm.isExist(InterestdistillVO.class, sqlwhere)) {
			// throw new Exception("�Ѿ���ȡ��" + trade_date + "���Ժ����Ϣ������ȡ����");
			// }
			// if (pm.isExist(HgdistillVO.class, sqlwhere)) {
			// throw new Exception("�Ѿ���ȡ��" + trade_date + "���Ժ�Ļع�������Ϣ������ȡ����");
			// }

			/**
			 * ȡ������ ��ҵ�������� �������ǽ� ȡ�����˰��뿪�� �������� ȡ����Ϻ���� �����Լ��м�� �Լ�����vo
			 */

			for (int i = 0; i < groupVOs.length; i++) {
				TradeDataTool tradedatatool = new TradeDataTool();
				CheckClassTool checkclasstool = new CheckClassTool();
				/**
				 * ����ҵ�����ڶ�������
				 */
				BilltypeGroupVO vo = groupVOs[i];
				// 1.��ȡ��Ҫ ȡ�������� state=3 �Ѽ��˵���
				List<PendingBillVO> plist = getNeedVOs(vo, checkParaVO,
						" state = " + SystemConst.statecode[4],
						SystemConst.checkstate[2]);
				// ��ȡ��Ҫ ��������� state=2 or 3
				List<PendingBillVO> needuncheckplist = getNeedVOs(vo,
						checkParaVO, " (state = " + SystemConst.statecode[3]
								+ " or state=" + SystemConst.statecode[4]
								+ ") ", SystemConst.checkstate[2]);
				String errormsg = null;
				try {
					SecSysInitCache.getInstance().setused(
							checkParaVO.getPk_group(), checkParaVO.getPk_org(),
							checkParaVO.getPk_glorgbook());
					// 2.��ȡ�����еĲ����
					Map<String, List<PendingBillVO>> dataMap = getBillTypeClass(
							checkParaVO, plist, groupVOs, checkclasstool);
					Map<String, List<PendingBillVO>> needuncheckdataMap = getBillTypeClass(
							checkParaVO, needuncheckplist, groupVOs,
							checkclasstool);

					// 3.�ж���Ҫ��˵�����
					Set<UFDate> set = getCheckdays(needuncheckplist, 1);

					NCLocator
							.getInstance()
							.lookup(IBillCheckByGroup.class)
							.uncheckOneGroup_RequiresNew(vo, set, dataMap,
									needuncheckdataMap, checkParaVO,
									tradedatatool, checkclasstool);
					isSuccess = "success";
				} catch (BusinessException e) {
					errormsg = e.getMessage();
					Logger.error(e.getMessage());
				} finally {
					clearCache(checkParaVO);

					NCLocator
							.getInstance()
							.lookup(IBillCheckByGroup.class)
							.updateApporveUI_RequiresNew(vo, checkParaVO,
									errormsg, SystemConst.checkstate[2]);
				}
			}

		} catch (Exception e) {
			throw new BusinessException(e);
		} finally {
			UnLock(checkParaVO);
		}
		return isSuccess;
	}

	@Override
	
	public String checkBill(CheckParaVO checkParaVO) throws BusinessException {
		String isSuccess = "fail";
		if (null == checkParaVO) {
			// ������ʾ��Ϣ
			throw new BusinessException("ǰ̨����������ϢΪ�գ���˲�!");
		}
		Lock(checkParaVO);
		try {
			BilltypeGroupVO[] uncheckgroupVOs = checkParaVO.getGroupvos();// ҵ����
			/**
			 * ���ҵ����VO�ٴ�ǰ��̨tsУ��
			 * 
			 * @author cjh
			 * @date 2015-12-28
			 */
			ApproveVO[] approvevos = new ApproveVO[uncheckgroupVOs.length];
			for (int i = 0; i < uncheckgroupVOs.length; i++) {
				approvevos[i] = uncheckgroupVOs[i].getApproveVO();
			}
			if (!PubMethod.getInstance().checkIsAppoveUI(approvevos)) {
				throw new BusinessException("�����쳣��ǰ��̨���ݲ�һ�£���ˢ������!");
			}
			// end_cjh
			List<BilltypeGroupVO> grouplist = new ArrayList<BilltypeGroupVO>();
			for (int i = 0; i < uncheckgroupVOs.length; i++) {
				BilltypeGroupVO billtypeGroupVO = uncheckgroupVOs[i];
				// if (billtypeGroupVO.getLastapprovedate() == null
				// ||
				// billtypeGroupVO.getLastapprovedate().toLocalString().compareTo(checkParaVO.getTrade_date().toLocalString())
				// < 0) {
				if (billtypeGroupVO.getLastapprovedate() == null
						|| billtypeGroupVO.getLastapprovedate().asBegin()
								.before(checkParaVO.getTrade_date().asBegin())) {

					grouplist.add(billtypeGroupVO);
				}
			}
			if (grouplist.size() == 0) {
				throw new BusinessException("�������С��ҵ�����������ڣ������������ڣ�");
			}
			BilltypeGroupVO[] groupVOs = grouplist
					.toArray(new BilltypeGroupVO[] {});

			boolean isFirstPrice = SysInitCache.getInstance()
					.getSysInitValue(SystemConst.SYSINIT_NumCalculateMay)
					.equals(SystemConst.SYSINIT_NumCalculateMay_value[0]);// ���㵥�ۺ������
			boolean iscalfund = SysInitCache.getInstance().getIscalFund(
					checkParaVO.getPk_group(), checkParaVO.getPk_org(),
					checkParaVO.getPk_glorgbook());
			// 5.��ҵ����-��-�ɱ����㷽��������˴���
			for (int i = 0; i < groupVOs.length; i++) {
				TradeDataTool tradedatatool = new TradeDataTool();
				CheckClassTool checkclasstool = new CheckClassTool();
				BilltypeGroupVO vo = groupVOs[i];
				/**
				 * 1.���֤ȯ�ֺ췽���Լ� �Զ�������Ϣ���� YangJie 2014-06-16
				 */
				/**
				 * 2. �����м������
				 * 
				 * 3.��������������� ����ҵ������ ����tradetool ����������������
				 */
				NCLocator.getInstance().lookup(IBillCheckByGroup.class)
						.Before_RequiresNew(vo, checkParaVO, tradedatatool);
				//
				// 1.��ȡ��Ҫ��˵����� state=0δ��˵���
				List<PendingBillVO> plist = getNeedVOs(vo, checkParaVO,
						" state = " + SystemConst.statecode[1],
						SystemConst.checkstate[0]);
				String errormsg = null;
				try {
					SecSysInitCache.getInstance().setused(
							checkParaVO.getPk_group(), checkParaVO.getPk_org(),
							checkParaVO.getPk_glorgbook());
					// 2.��ȡ�����еĲ����
					Map<String, List<PendingBillVO>> dataMap = getBillTypeClass(
							checkParaVO, plist, groupVOs, checkclasstool);
					// 3.�ж���Ҫ��˵�����
					Set<UFDate> set = getCheckdays(plist, 0);
					// 4.��ȡ�ɱ����㷽��
					List<CostPlanVO> clist = getCostPlanVOs(checkParaVO);
					if (clist == null || clist.size() == 0) {
						throw new BusinessException(
								"����֯�³ɱ����㷽��û�����ã���ȥ�ɱ����㷽���ڵ����ã�");
					}
					NCLocator
							.getInstance()
							.lookup(IBillCheckByGroup.class)
							.checkOneGroup_RequiresNew(vo, set, dataMap,
									checkParaVO, tradedatatool, checkclasstool,
									clist, isFirstPrice, iscalfund);
					isSuccess = "success";
				} catch (BusinessException e) {
					errormsg = e.getMessage();
					Logger.error(e.getMessage());
				} finally {
					/**
					 * add by YangJie 2014-03-26 ��������������� ȷ������������һ�������ֻ��ѯ��һ�μ���
					 */
					clearCache(checkParaVO);

					NCLocator
							.getInstance()
							.lookup(IBillCheckByGroup.class)
							.updateApporveUI_RequiresNew(vo, checkParaVO,
									errormsg, SystemConst.checkstate[0]);
				}
			}

		} catch (Exception e) {
			throw new BusinessException(e);
		} finally {
			UnLock(checkParaVO);
		}
		return isSuccess;
	}

	/**
	 * ��ȡ�м�������
	 * 
	 * @param CheckParaVO
	 * @return List<CostPlanVO>
	 */
	private List<PendingBillVO> getNeedVOs(BilltypeGroupVO vo,
			CheckParaVO checkParaVO, String addcondition, int state)
			throws BusinessException {
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
		if (state == SystemConst.checkstate[0]) {// ���
			if (vo.getLastapprovedate() != null) {
				sb.append(" and trade_date>='");
				sb.append(vo.getLastapprovedate().getDateAfter(1)
						.toLocalString()
						+ " 00:00:00' ");
			}
			sb.append(" and trade_date<='");
			sb.append(checkParaVO.getTrade_date().toLocalString()
					+ " 23:59:59' ");
		} else if (state == SystemConst.checkstate[2]) {// ����
			if (vo.getLastapprovedate() != null) {
				sb.append(" and trade_date<='");
				sb.append(vo.getLastapprovedate().toLocalString()
						+ " 23:59:59' ");
			}
			sb.append(" and trade_date>='");
			sb.append(checkParaVO.getTrade_date().toLocalString()
					+ " 00:00:00' ");
		} else {// ����
			if (vo.getLastapprovedate() != null) {
				sb.append(" and trade_date<='");
				sb.append(vo.getLastapprovedate().toLocalString()
						+ " 23:59:59' ");
			}
			if (vo.getLasttallydate() != null) {
				sb.append(" and trade_date>='");
				sb.append(vo.getLasttallydate().getDateAfter(1).toLocalString()
						+ " 00:00:00' ");
			}
		}

		List<PendingBillVO> plist = PubMethod.getInstance().queryAllByCond(
				new PendingBillVO(), sb.toString());
		return plist;
	}

	/**
	 * ��ȡ�ɱ����㷽��
	 * 
	 * @param CheckParaVO
	 * @return List<CostPlanVO>
	 */
	private List<CostPlanVO> getCostPlanVOs(CheckParaVO checkParaVO)
			throws BusinessException {
		String costplansql = " pk_group='" + checkParaVO.getPk_group()
				+ "' and pk_org='" + checkParaVO.getPk_org()
				+ "' and pk_glorgbook='" + checkParaVO.getPk_glorgbook()
				+ "' and isnull(dr,0)=0";
		List<CostPlanVO> clist = PubMethod.getInstance().queryAllByCond(
				new CostPlanVO(), costplansql);
		return clist;
	}

	/**
	 * ���ݴ��������ݡ�ҵ���鷵��<����+ҵ���飬���������Լ������>
	 * 
	 * @param billVO
	 * @param groupVO
	 * @return
	 */
	private Map<String, List<PendingBillVO>> getBillTypeClass(
			CheckParaVO checkParaVO, List<PendingBillVO> list,
			BilltypeGroupVO[] groupVOs, CheckClassTool checkclasstool)
			throws BusinessException {
		if (list == null || list.size() <= 0) {
			return null;
		}
		try {
			Map<String, HashMap<String, BilltypeDetailsVO>> map = new HashMap();
			map = SecSysInitCache.getInstance().getCheckPlanDetails(
					checkParaVO.getPk_group(), checkParaVO.getPk_org(),
					checkParaVO.getPk_glorgbook());
			List<Integer> removenum = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				PendingBillVO pbvo = list.get(i);
				BilltypeDetailsVO bdvo = (BilltypeDetailsVO) ((map
						.get(checkParaVO.getPk_group()
								+ checkParaVO.getPk_org()
								+ checkParaVO.getPk_glorgbook())).get(pbvo
						.getPk_billtype()));
				if (bdvo != null) {
					pbvo.setPk_billtypegroup(bdvo.getPk_billtypegroup());
					pbvo.setCheckclass(bdvo.getClassname());
					pbvo.setCheckorder(bdvo.getCheckorder());
					pbvo.setDirection(bdvo.getDirection());
					pbvo.setPk_stocksort1(bdvo.getPk_stocksort1());
					pbvo.setPk_stocksort2(bdvo.getPk_stocksort2());
				} else {
					removenum.add(i);
				}
			}
			for (int j = removenum.size() - 1; j >= 0; j--) {
				list.remove(removenum.get(j).intValue());
			}
			/**
			 * list����
			 */
			Collections.sort(list, new Comparator<PendingBillVO>() {
				@Override
				public int compare(PendingBillVO pbo1, PendingBillVO pbo2) {
					int index = pbo1.getPk_billtypegroup().compareTo(
							pbo2.getPk_billtypegroup());
					index = index == 0 ? pbo1.getTrade_date().compareTo(
							pbo2.getTrade_date()) : index;
					index = index == 0 ? pbo1.getCheckorder().compareTo(
							pbo2.getCheckorder()) : index;
					return index;
				}

			});
			/**
			 * ���湹������Map
			 */
			Map<String, List<PendingBillVO>> returnmap = new HashMap<String, List<PendingBillVO>>();
			for (int i = 0; i < list.size(); i++) {
				PendingBillVO pbvo = list.get(i);
				String key = pbvo.getTrade_date().toLocalString()
						+ pbvo.getPk_billtypegroup();
				/**
				 * �˴�����ClassList YangJie 2014-03-28
				 */
				String classname = pbvo.getCheckclass().trim();
				String transtypecode = pbvo.getPk_billtype().trim();
				IBillCheckPlugin isimcheck;
				try {
					String[] temp = classname.split("\\.");
					isimcheck = (IBillCheckPlugin) NewObjectService
							.newInstance(temp[2], classname);
				} catch (Exception e) {
					throw new BusinessException(pbvo.getCheckclass() + "ʵ����ʧ�ܣ�"
							+ e.getMessage());
				}
				/***
				 * ��ʼ�������
				 */
				checkclasstool.setCheckClassMap(transtypecode + "##"
						+ classname, isimcheck);
				if (!returnmap.containsKey(key)) {
					List innerlist = new ArrayList();
					innerlist.add(pbvo);
					returnmap.put(key, innerlist);
				} else {
					List innerlist = returnmap.get(key);
					innerlist.add(pbvo);
				}
			}
			return returnmap;
		} catch (Exception e) {
			throw new BusinessException("���ݴ��������ݡ�ҵ���鷵��<����+ҵ���飬���������Լ������>����"
					+ e.toString() + e.getMessage());
		}
	}

	/**
	 * 
	 * @param list
	 * @param order
	 *            ��� ���� ���� ���� 0 ���� �ݼ� 1
	 * @return
	 */
	private Set<UFDate> getCheckdays(List<PendingBillVO> list, final int order) {
		int size = list.size();
		Set<UFDate> set = new TreeSet<UFDate>(new Comparator<UFDate>() {
			@Override
			public int compare(UFDate date1, UFDate date2) {
				if (order == 0)
					return date1.compareTo(date2);
				else {
					return date2.compareTo(date1);
				}
			}
		});
		for (int i = 0; i < size; i++) {
			UFDate trade_date = new UFDate(list.get(i).getTrade_date()
					.toLocalString());
			set.add(trade_date);
		}
		return set;
	}

	/**
	 * ʹ����Ͻ������� YangJie 2014-03-26
	 * 
	 * @throws BusinessException
	 */
	private void clearCache(CheckParaVO checkParaVO) throws BusinessException {
		SecSysInitCache.getInstance().clearused(checkParaVO.getPk_group(),
				checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook());
	}

	/**
	 * ʹ����ǰ���� YangJie 2014-04-26
	 * 
	 * @throws BusinessException
	 */
	private synchronized void Lock(CheckParaVO checkParaVO)
			throws BusinessException {
		if (SecSysInitCache.getInstance().canLock(checkParaVO.getPk_group(),
				checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook()))
			SecSysInitCache.getInstance().setLock(checkParaVO.getPk_group(),
					checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook());
		else
			throw new BusinessException("�޷���������ǰ������֯�˲����ڴ���ҵ�����Ժ����ԣ�");
	}

	/**
	 * ʹ����Ͻ������� YangJie 2014-04-26
	 * 
	 * @throws BusinessException
	 */
	private synchronized void UnLock(CheckParaVO checkParaVO)
			throws BusinessException {
		SecSysInitCache.getInstance().clearLock(checkParaVO.getPk_group(),
				checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook());
	}
}
