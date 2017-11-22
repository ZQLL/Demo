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
 * 审核处理实现类
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
			// 错误提示信息
			throw new BusinessException("前台环境参数信息为空！请核查!");
		}
		Lock(checkParaVO);
		try {
			BilltypeGroupVO[] groupVOs = checkParaVO.getGroupvos();// 业务组
			for (int i = 0; i < groupVOs.length; i++) {
				BilltypeGroupVO billtypeGroupVO = groupVOs[i];
				checkParaVO.setTrade_date(billtypeGroupVO.getLastapprovedate());
			}

			for (int i = 0; i < groupVOs.length; i++) {
				TradeDataTool tradedatatool = new TradeDataTool();
				CheckClassTool checkclasstool = new CheckClassTool();
				BilltypeGroupVO vo = groupVOs[i];

				// 1.获取需要审核的数据 state=2 已审核单据
				List<PendingBillVO> plist = getNeedVOs(vo, checkParaVO,
						" state = " + SystemConst.statecode[3],
						SystemConst.checkstate[1]);
				String errormsg = null;
				try {
					SecSysInitCache.getInstance().setused(
							checkParaVO.getPk_group(), checkParaVO.getPk_org(),
							checkParaVO.getPk_glorgbook());
					// 2.获取待运行的插件类
					Map<String, List<PendingBillVO>> dataMap = getBillTypeClass(
							checkParaVO, plist, groupVOs, checkclasstool);
					// 3.判断需要审核的天数
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
			// 错误提示信息
			throw new BusinessException("前台环境参数信息为空！请核查!");
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
				throw new BusinessException("弃审到日期不能早于期初记账日期，请更改弃审到日期！");
			}
			BilltypeGroupVO[] groupVOs = checkParaVO.getGroupvos();// 业务组

			/**
			 * 审核业务组VO再次前后台ts校验
			 * 
			 * @author cjh
			 * @date 2015-12-28
			 */
			ApproveVO[] approvevos = new ApproveVO[groupVOs.length];
			for (int i = 0; i < groupVOs.length; i++) {
				approvevos[i] = groupVOs[i].getApproveVO();
			}
			if (!PubMethod.getInstance().checkIsAppoveUI(approvevos)) {
				throw new BusinessException("并发异常，前后台数据不一致，请刷新数据!");
			}
			// end_cjh

			boolean ischeck = false;
			for (int i = 0; i < groupVOs.length; i++) {
				BilltypeGroupVO billtypeGroupVO = groupVOs[i];
				if (billtypeGroupVO.getLastapprovedate().toLocalString()
						.compareTo(checkParaVO.getTrade_date().toLocalString()) < 0) {
					throw new BusinessException("弃审到日期不能大于选中业务组的审核日期，请更改弃审到日期！");
				}
				if (CostConstant.COSTGROUP.equals(billtypeGroupVO
						.getPk_billtypegroup())) {
					ischeck = true;
				}

				// 校验回购利息计提
				if (CostConstant.HGGROUP.equals(billtypeGroupVO
						.getPk_billtypegroup())) {// 成本审核做公允价值、应收利息的校验
					String pk_glorgbook = checkParaVO.getPk_glorgbook();
					String pk_group = checkParaVO.getPk_group();
					String pk_org = checkParaVO.getPk_org();
					UFDate trade_date = checkParaVO.getTrade_date();
					// 校验回购计提
					HgDistillCheck incheck = new HgDistillCheck();
					incheck.uncheckBill(pk_group, pk_org, pk_glorgbook,
							trade_date);
				}

				// 是否启用实际利率，校验
				if (CostConstant.COSTGROUP.equals(billtypeGroupVO
						.getPk_billtypegroup())) {
					// 是否启用参数SEC32
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
			 * 1.校验后续业务 2.弃审日期大于记账日期的 只需要弃审 3.弃审日期小于记账日期的 先取消记账 后 弃审
			 */
			if (ischeck) {// 成本审核做公允价值、应收利息的校验
				String pk_glorgbook = checkParaVO.getPk_glorgbook();
				String pk_group = checkParaVO.getPk_group();
				String pk_org = checkParaVO.getPk_org();
				UFDate trade_date = checkParaVO.getTrade_date();
				// 校验应收利息计提
				InterestDistillCheck incheck = new InterestDistillCheck();
				incheck.uncheckBill(pk_group, pk_org, pk_glorgbook, trade_date);
				// 校验公允价值计提
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
			// throw new Exception("已经提取过" + trade_date + "或以后的公允价值，请先取消！");
			// }
			// if (pm.isExist(InterestdistillVO.class, sqlwhere)) {
			// throw new Exception("已经提取过" + trade_date + "或以后的利息，请先取消！");
			// }
			// if (pm.isExist(HgdistillVO.class, sqlwhere)) {
			// throw new Exception("已经提取过" + trade_date + "或以后的回购单据利息，请先取消！");
			// }

			/**
			 * 取消记账 分业务组来干 后续考虑将 取消记账剥离开来 独立事务 取消完毕后更新 单据以及中间表 以及界面vo
			 */

			for (int i = 0; i < groupVOs.length; i++) {
				TradeDataTool tradedatatool = new TradeDataTool();
				CheckClassTool checkclasstool = new CheckClassTool();
				/**
				 * 考虑业务组内独立事务
				 */
				BilltypeGroupVO vo = groupVOs[i];
				// 1.获取需要 取消的数据 state=3 已记账单据
				List<PendingBillVO> plist = getNeedVOs(vo, checkParaVO,
						" state = " + SystemConst.statecode[4],
						SystemConst.checkstate[2]);
				// 获取需要 弃审的数据 state=2 or 3
				List<PendingBillVO> needuncheckplist = getNeedVOs(vo,
						checkParaVO, " (state = " + SystemConst.statecode[3]
								+ " or state=" + SystemConst.statecode[4]
								+ ") ", SystemConst.checkstate[2]);
				String errormsg = null;
				try {
					SecSysInitCache.getInstance().setused(
							checkParaVO.getPk_group(), checkParaVO.getPk_org(),
							checkParaVO.getPk_glorgbook());
					// 2.获取待运行的插件类
					Map<String, List<PendingBillVO>> dataMap = getBillTypeClass(
							checkParaVO, plist, groupVOs, checkclasstool);
					Map<String, List<PendingBillVO>> needuncheckdataMap = getBillTypeClass(
							checkParaVO, needuncheckplist, groupVOs,
							checkclasstool);

					// 3.判断需要审核的天数
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
			// 错误提示信息
			throw new BusinessException("前台环境参数信息为空！请核查!");
		}
		Lock(checkParaVO);
		try {
			BilltypeGroupVO[] uncheckgroupVOs = checkParaVO.getGroupvos();// 业务组
			/**
			 * 审核业务组VO再次前后台ts校验
			 * 
			 * @author cjh
			 * @date 2015-12-28
			 */
			ApproveVO[] approvevos = new ApproveVO[uncheckgroupVOs.length];
			for (int i = 0; i < uncheckgroupVOs.length; i++) {
				approvevos[i] = uncheckgroupVOs[i].getApproveVO();
			}
			if (!PubMethod.getInstance().checkIsAppoveUI(approvevos)) {
				throw new BusinessException("并发异常，前后台数据不一致，请刷新数据!");
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
				throw new BusinessException("审核日期小于业务组的审核日期，请更改审核日期！");
			}
			BilltypeGroupVO[] groupVOs = grouplist
					.toArray(new BilltypeGroupVO[] {});

			boolean isFirstPrice = SysInitCache.getInstance()
					.getSysInitValue(SystemConst.SYSINIT_NumCalculateMay)
					.equals(SystemConst.SYSINIT_NumCalculateMay_value[0]);// 先算单价后乘数量
			boolean iscalfund = SysInitCache.getInstance().getIscalFund(
					checkParaVO.getPk_group(), checkParaVO.getPk_org(),
					checkParaVO.getPk_glorgbook());
			// 5.按业务组-天-成本计算方案进行审核处理
			for (int i = 0; i < groupVOs.length; i++) {
				TradeDataTool tradedatatool = new TradeDataTool();
				CheckClassTool checkclasstool = new CheckClassTool();
				BilltypeGroupVO vo = groupVOs[i];
				/**
				 * 1.检查证券分红方案以及 自动还本计息档案 YangJie 2014-06-16
				 */
				/**
				 * 2. 插入中间表数据
				 * 
				 * 3.再新增两个插件类 插入业务数据 更新tradetool 起独立事务处理此问题
				 */
				NCLocator.getInstance().lookup(IBillCheckByGroup.class)
						.Before_RequiresNew(vo, checkParaVO, tradedatatool);
				//
				// 1.获取需要审核的数据 state=0未审核单据
				List<PendingBillVO> plist = getNeedVOs(vo, checkParaVO,
						" state = " + SystemConst.statecode[1],
						SystemConst.checkstate[0]);
				String errormsg = null;
				try {
					SecSysInitCache.getInstance().setused(
							checkParaVO.getPk_group(), checkParaVO.getPk_org(),
							checkParaVO.getPk_glorgbook());
					// 2.获取待运行的插件类
					Map<String, List<PendingBillVO>> dataMap = getBillTypeClass(
							checkParaVO, plist, groupVOs, checkclasstool);
					// 3.判断需要审核的天数
					Set<UFDate> set = getCheckdays(plist, 0);
					// 4.获取成本计算方案
					List<CostPlanVO> clist = getCostPlanVOs(checkParaVO);
					if (clist == null || clist.size() == 0) {
						throw new BusinessException(
								"本组织下成本计算方案没有设置，请去成本计算方案节点设置！");
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
					 * add by YangJie 2014-03-26 清理基础档案缓存 确保基础档案在一次审核中只查询了一次即可
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
	 * 获取中间表的数据
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
		if (state == SystemConst.checkstate[0]) {// 审核
			if (vo.getLastapprovedate() != null) {
				sb.append(" and trade_date>='");
				sb.append(vo.getLastapprovedate().getDateAfter(1)
						.toLocalString()
						+ " 00:00:00' ");
			}
			sb.append(" and trade_date<='");
			sb.append(checkParaVO.getTrade_date().toLocalString()
					+ " 23:59:59' ");
		} else if (state == SystemConst.checkstate[2]) {// 弃审
			if (vo.getLastapprovedate() != null) {
				sb.append(" and trade_date<='");
				sb.append(vo.getLastapprovedate().toLocalString()
						+ " 23:59:59' ");
			}
			sb.append(" and trade_date>='");
			sb.append(checkParaVO.getTrade_date().toLocalString()
					+ " 00:00:00' ");
		} else {// 记账
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
	 * 获取成本计算方案
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
	 * 根据待处理数据、业务组返回<日期+业务组，单据类型以及插件类>
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
			 * list排序
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
			 * 下面构建返回Map
			 */
			Map<String, List<PendingBillVO>> returnmap = new HashMap<String, List<PendingBillVO>>();
			for (int i = 0; i < list.size(); i++) {
				PendingBillVO pbvo = list.get(i);
				String key = pbvo.getTrade_date().toLocalString()
						+ pbvo.getPk_billtypegroup();
				/**
				 * 此处构建ClassList YangJie 2014-03-28
				 */
				String classname = pbvo.getCheckclass().trim();
				String transtypecode = pbvo.getPk_billtype().trim();
				IBillCheckPlugin isimcheck;
				try {
					String[] temp = classname.split("\\.");
					isimcheck = (IBillCheckPlugin) NewObjectService
							.newInstance(temp[2], classname);
				} catch (Exception e) {
					throw new BusinessException(pbvo.getCheckclass() + "实例化失败！"
							+ e.getMessage());
				}
				/***
				 * 初始化审核类
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
			throw new BusinessException("根据待处理数据、业务组返回<日期+业务组，单据类型以及插件类>报错："
					+ e.toString() + e.getMessage());
		}
	}

	/**
	 * 
	 * @param list
	 * @param order
	 *            审核 递增 记账 递增 0 弃审 递减 1
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
	 * 使用完毕解锁缓存 YangJie 2014-03-26
	 * 
	 * @throws BusinessException
	 */
	private void clearCache(CheckParaVO checkParaVO) throws BusinessException {
		SecSysInitCache.getInstance().clearused(checkParaVO.getPk_group(),
				checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook());
	}

	/**
	 * 使用完前加锁 YangJie 2014-04-26
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
			throw new BusinessException("无法加锁，当前集团组织账簿下在处理业务，请稍后再试！");
	}

	/**
	 * 使用完毕解锁缓存 YangJie 2014-04-26
	 * 
	 * @throws BusinessException
	 */
	private synchronized void UnLock(CheckParaVO checkParaVO)
			throws BusinessException {
		SecSysInitCache.getInstance().clearLock(checkParaVO.getPk_group(),
				checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook());
	}
}
