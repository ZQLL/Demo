package nc.impl.fba_scost.cost.billcheckimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.fba_scost.cost.cache.ScostCalCacheManager;
import nc.bs.fba_scost.cost.interest.pub.AutoPay;
import nc.bs.fba_scost.cost.interest.pub.zqjdutils;
import nc.bs.fba_scost.cost.simtools.CostingTool;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.impl.fba_scost.cost.pub.PrivateMethod;
import nc.itf.fba_scost.cost.billcheck.IBillCheckByGroup;
import nc.itf.fba_scost.cost.billcheck.IBillMakeJTD;
import nc.itf.fba_scost.cost.billcheck.ISpecialBusiness;
import nc.itf.fba_scost.cost.pub.IBillCheckPlugin;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.fundbalance.FundBalanceVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.transferbalance.TransferBalanceVO;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * 审核组实现类
 * 
 * @author YangJie
 * @since 2014-04-04
 * 
 */
public class BillCheckByGroupImpl implements IBillCheckByGroup {

	public void tallyOneGroup_RequiresNew(BilltypeGroupVO vo, Set<UFDate> set, Map<String, List<PendingBillVO>> dataMap, CheckParaVO checkParaVO,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws BusinessException {
		String pk_billtypegroup = vo.getPk_billtypegroup();
		String msg = null;
		try {
			for (UFDate date : set) {
				CostParaVO costParaVO = new CostParaVO();
				costParaVO.setCheckParavo(checkParaVO);
				costParaVO.setCheckstate(SystemConst.checkstate[1]);// 记账方法
				String key = date.toLocalString().concat(pk_billtypegroup);
				List<PendingBillVO> billtypeclass = dataMap.get(key);
				CostingTool costingtool = new CostingTool();
				costingtool.setCostParaVO(costParaVO);
				costingtool.setBilltypeclass(billtypeclass);
				costingtool.setCurrbilltypegroupvo(vo);// 当前业务组
				costingtool.setCurrdate(date.toLocalString());// 当记账业务时间
				costingtool.setIstally(true);// 发送记账消息
				checkTally(pk_billtypegroup, date);
				
				
				
				beforeCheck(costingtool, tradedatatool, checkclasstool);
				runTallyClass(costingtool, tradedatatool, checkclasstool);
				updateTradeDatas(costingtool, tradedatatool, checkclasstool);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			if (e.getMessage() == null) {
				msg = "记账程序异常";
			} else {
				msg = e.getMessage();
			}
			throw new BusinessException(msg);
		} finally {
			NCLocator.getInstance().lookup(IBillCheckByGroup.class)
					.updateMiddleAndApporveUI_RequiresNew(dataMap, pk_billtypegroup, SystemConst.statecode[4], msg);
		}
	}

	public void uncheckOneGroup_RequiresNew(BilltypeGroupVO vo, Set<UFDate> set, Map<String, List<PendingBillVO>> dataMap,
			Map<String, List<PendingBillVO>> needuncheckdataMap, CheckParaVO checkParaVO, TradeDataTool tradedatatool, CheckClassTool checkclasstool)
			throws BusinessException {
		String pk_billtypegroup = vo.getPk_billtypegroup();
		String msg = null;
		try {
			//zq
			//通过前台参数来判断是否调用后台方法使已审核的借入单产生的计提单进行删除
			IBillMakeJTD bmj = NCLocator.getInstance().lookup(IBillMakeJTD.class);
			if (bmj.getBooleanFromInitcode(checkParaVO.getPk_glorgbook(), "Zqjd02")) {
				bmj.DeleteJTD(checkParaVO);
			}
			for (UFDate date : set) {
				CostParaVO costParaVO = new CostParaVO();
				costParaVO.setCheckParavo(checkParaVO);
				costParaVO.setCheckstate(SystemConst.checkstate[2]); // 弃审方法
				String key = date.toLocalString().concat(pk_billtypegroup);
				CostingTool costingtool = new CostingTool();
				costingtool.setCostParaVO(costParaVO);
				costingtool.setCurrbilltypegroupvo(vo);// 当前业务组
				costingtool.setCurrdate(date.toLocalString());// 当记账业务时间
				costingtool.setIstally(false);// 发送取消记账消息
				checkTally(pk_billtypegroup, date);
				if (dataMap != null && vo.getLasttallydate() != null && date.toLocalString().compareTo(vo.getLasttallydate().toLocalString()) <= 0) {
					// 走取消记账逻辑
					List<PendingBillVO> billtypeclass = dataMap.get(key);
					costingtool.setBilltypeclass(billtypeclass);
					beforeCheck(costingtool, tradedatatool, checkclasstool);
					runTallyClass(costingtool, tradedatatool, checkclasstool);
				}
				/**
				 * 弃审 循环调用插件类弃审
				 */
				/**
				 * 1.获取数据 2.执行插件内弃审 3.弃审完毕后更新 单据以及中间表 以及界面vo 4.删除库存 3个库存
				 */
				if (needuncheckdataMap != null) {
					List<PendingBillVO> billtypeclass = needuncheckdataMap.get(key);
					costingtool.setBilltypeclass(billtypeclass);
					beforeCheck(costingtool, tradedatatool, checkclasstool);
					runUncheckClass(costingtool, tradedatatool, checkclasstool);
				}
				updateTradeDatas(costingtool, tradedatatool, checkclasstool);
				/**
				 * 考虑放到业务组内处理 删除各种库存
				 */
				// // 删除生成的卖出冲回公允价值计提单
				// String wherecond = "pk_fairvaluedistill in " +
				// "(select pk_fairvaluedistill from sim_fairvaluedistill where "
				// +
				// sqlwhere + ")";
				// basedao.deleteByClause(FairvaluedistillBVO.class, wherecond);
				// basedao.deleteByClause(FairvaluedistillVO.class, sqlwhere);
				// // 删除自动生成的还本/付息单
				// payBO.deleteAutoPayBill(pk_corp, pk_glorgbook, pk_user,
				// trade_date);
				// // 删除当天之后的所有记录
				// sqlwhere = sqlwhere.replaceFirst("distill_date",
				// "trade_date");
				String pk_glorgbook = checkParaVO.getPk_glorgbook();
				String pk_group = checkParaVO.getPk_group();
				String pk_org = checkParaVO.getPk_org();
				StringBuffer conditions = new StringBuffer(" isnull(dr,0)=0 ");
				if (pk_glorgbook != null) {
					conditions.append(" and pk_glorgbook='").append(pk_glorgbook).append("' ");
				}
				if (pk_group != null) {
					conditions.append(" and pk_group='").append(pk_group).append("' ");
				}
				if (pk_org != null) {
					conditions.append(" and pk_org='").append(pk_org).append("' ");
				}
				conditions.append(" and pk_billtypegroup='").append(pk_billtypegroup).append("' ");
				conditions.append(" and trade_date>='").append(checkParaVO.getTrade_date().toLocalString() + " 00:00:00").append("' ");
				String sqlwhere = conditions.toString();
				// 删除库余额表
				PrivateMethod.getInstance().getBaseDAO().deleteByClause(StockBalanceVO.class, sqlwhere);
				// 删除资金日余额表
				PrivateMethod.getInstance().getBaseDAO().deleteByClause(FundBalanceVO.class, sqlwhere);
				// 删除券源划转余额表
				PrivateMethod.getInstance().getBaseDAO().deleteByClause(TransferBalanceVO.class, sqlwhere);

				// basedao.deleteByClause(TotalwinVO.class, sqlwhere);// 删除累计收益表
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			if (e.getMessage() == null) {
				msg = "弃审程序异常";
			} else {
				msg = e.getMessage();
			}

			throw new BusinessException(msg);
		} finally {
			/**
			 * 弃审 目前采用的也是插入数据的方式 后续可能会考虑删除历史的方式 YangJie 2014-04-16
			 */
			NCLocator.getInstance().lookup(IBillCheckByGroup.class)
					.updateMiddleAndApporveUI_RequiresNew(needuncheckdataMap, pk_billtypegroup, SystemConst.statecode[1], msg);
		}
	}

	public void checkOneGroup_RequiresNew(BilltypeGroupVO vo, Set<UFDate> set, Map<String, List<PendingBillVO>> dataMap, CheckParaVO checkParaVO,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool, List<CostPlanVO> clist, boolean isFirstPrice, boolean iscalfund)
			throws BusinessException {
		int stockflag = 0;// YangJie 2014-04-03 库存初始标志 对于同一个成本计算方案
		// 只需要查一次库存缓存
		String pk_billtypegroup = vo.getPk_billtypegroup();
		ScostCalCacheManager scostcalcachemanager = new ScostCalCacheManager();
		String msg = null;
		try {
			for (UFDate date : set) {
				for (int j = 0; j < clist.size(); j++) {
					CostPlanVO tempvo = clist.get(j);
					CostParaVO costParaVO = new CostParaVO();
					costParaVO.setCheckParavo(checkParaVO);
					costParaVO.setCostplanvo(tempvo);
					costParaVO.setFirstPrice(isFirstPrice);
					costParaVO.setCheckstate(SystemConst.checkstate[0]);// 审核方法
					String key = date.toLocalString().concat(pk_billtypegroup);
					List<PendingBillVO> billtypeclass = dataMap.get(key);
					CostingTool costingtool = new CostingTool();
					costingtool.setCheckFund(iscalfund);
					costingtool.setCostParaVO(costParaVO);
					costingtool.getBalancetool().setScostcalcachemanager(scostcalcachemanager);// 设置库存缓存
					costingtool.setBilltypeclass(billtypeclass);
					costingtool.setCurrbilltypegroupvo(vo);// 当前业务组
					costingtool.setCurrdate(date.toLocalString());// 当前审核业务时间
					/**
					 * YangJie 2014-04-08 库存缓存初始化策略 1.当成本计算方案内循环需要全面初始化
					 * 2.当成本计算方案外循环需要片面初始化：即根据当前时间回溯前几日缓存来使用
					 */
					if (stockflag < clist.size()) {
						costingtool.setIsinit(true);
						initCost(costingtool);
					} else {
						costingtool.setIsinit(false);
						initCost(costingtool);
					}
						
					checkTally(pk_billtypegroup, date);
					/*************在此添加生成债券交易记录后续单据  lqm**************/
					zqjdutils utis=new zqjdutils();
					utis.zqjdautoAudit(vo,costingtool, tradedatatool,checkclasstool);
					/**********在此添加生成债券交易记录后续单据  end***/
				
					beforeCheck(costingtool, tradedatatool, checkclasstool);
		
					runCheckClass(costingtool, tradedatatool, checkclasstool);
					//审核时自动生成归还单  lqm
					utis.autoreturn(vo,  dataMap, costingtool, tradedatatool, checkclasstool)	;
					
					
				
					/**
					 * 审核完毕 1.更新库存 2.更新业务单据
					 */
					afterCheck(costingtool, tradedatatool, checkclasstool);
					stockflag++;
				}
			}
			//通过前台参数来判断是否调用后台方法使已审核的借入单产生计提单--zq
			IBillMakeJTD bmj = NCLocator.getInstance().lookup(
					IBillMakeJTD.class);
			if (bmj.getBooleanFromInitcode(checkParaVO.getPk_glorgbook(), "Zqjd02")) {
					bmj.MakeJTD(checkParaVO);
			}
			
		} catch (Exception e) {
			Logger.error(e.getMessage());
			if (e.getMessage() == null) {
				msg = "审核程序异常";
			} else {
				msg = e.getMessage();
			}
			throw new BusinessException(msg);
		} finally {
			/**
			 * 清理库存缓存
			 */
			clearBalanceCache(scostcalcachemanager);
			NCLocator.getInstance().lookup(IBillCheckByGroup.class)
					.updateMiddleAndApporveUI_RequiresNew(dataMap, pk_billtypegroup, SystemConst.statecode[3], msg);

		}

	}
	

	/**
	 * 审核前检查指定业务组的 date的合法性 核对台账信息
	 * 
	 * @param pk_billtypegroup
	 * @param date
	 * @throws Exception
	 */
	private void checkTally(String pk_billtypegroup, UFDate date) throws Exception {

	}

	/**
	 * 审核前预处理
	 * 
	 * @throws Exception
	 */
	private void beforeCheck(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		//自动还本付息
		AutoPay pay = new AutoPay();
		pay.autoPay(costingtool,tradedatatool);
		// 查询相关未审核/已审核/已记账数据
		initTradeData(costingtool, tradedatatool, checkclasstool);

	}

	/**
	 * 审核数据查询 YangJie 2014-03-28
	 * 
	 * @throws Exception
	 */
	private void initTradeData(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		/**
		 * 只要初始化一次数据即可 YangJie 2014-05-22
		 */
		if (!tradedatatool.isIsinit()) {
			CostParaVO costpara = costingtool.getCostParaVO();
			CheckParaVO checkparavo = costpara.getCheckParavo();
			Integer checkstate = costpara.getCheckstate();
			// List billtypeclass = costingtool.getBilltypeclass();
			String pk_glorgbook = checkparavo.getPk_glorgbook();
			String pk_org = checkparavo.getPk_org();
			String pk_group = checkparavo.getPk_group();
			UFDate trade_date = checkparavo.getTrade_date();

			Map<String, IBillCheckPlugin> checkclassmap = checkclasstool.getCheckClassMap();
			if (checkclassmap != null) {
				Set keyset = checkclassmap.keySet();
				for (Iterator ite = keyset.iterator(); ite.hasNext();) {
					// PendingBillVO pbvo = (PendingBillVO)
					// billtypeclass.get(i);
					String key = (String) ite.next();
					String[] temp = key.split("##");
					IBillCheckPlugin ibillcheck = checkclassmap.get(key);
					String billtype = temp[0];
					StringBuffer conditionbuffer = new StringBuffer();
					conditionbuffer.append(" isnull(dr,0)=0 ");
					conditionbuffer.append(" and pk_group='");
					conditionbuffer.append(pk_group).append("' ");
					conditionbuffer.append(" and pk_org='");
					conditionbuffer.append(pk_org).append("' ");
					/**
					 * 查询粒度为交易类型 无交易类型单据 交易类型处塞单据类型 YangJie 2014-03-28
					 */
					conditionbuffer.append(" and transtypecode='");
					conditionbuffer.append(billtype).append("' ");

					if (pk_glorgbook != null) {
						conditionbuffer.append(" and pk_glorgbook='");
						conditionbuffer.append(pk_glorgbook).append("'");
					}

					switch (checkstate) {
					case 0:// 审核
						conditionbuffer.append(" and state=0 ");
						/**
						 * 日期 查询以区间为准 待修改 YangJie 2014-03-28
						 */
						conditionbuffer.append(" and trade_date<'");
						conditionbuffer.append(trade_date.getDateAfter(1).toLocalString() + " 00:00:00").append("'");
						UFDate lastapprovedate = costingtool.getCurrbilltypegroupvo().getLastapprovedate();
						if (lastapprovedate != null) {
							conditionbuffer.append(" and trade_date>='");
							conditionbuffer.append(
									costingtool.getCurrbilltypegroupvo().getLastapprovedate().getDateAfter(1).toLocalString() + " 00:00:00").append(
									"'");
						}
						break;
					case 1:// 记账
						conditionbuffer.append(" and state=2 ");
						/**
						 * 日期 查询以区间为准 待修改 YangJie 2014-03-28
						 */
						conditionbuffer.append(" and trade_date<'");
						conditionbuffer.append(trade_date.getDateAfter(1).toLocalString() + " 00:00:00").append("'");
						UFDate lasttallydate = costingtool.getCurrbilltypegroupvo().getLasttallydate();
						if (lasttallydate != null) {
							conditionbuffer.append(" and trade_date>='");
							conditionbuffer.append(
									costingtool.getCurrbilltypegroupvo().getLasttallydate().getDateAfter(1).toLocalString() + " 00:00:00")
									.append("'");
						}
						break;
					case 2:// 弃审
						conditionbuffer.append(" and state>=2 ");
						conditionbuffer.append(" and trade_date>='");
						conditionbuffer.append(trade_date.toLocalString() + " 00:00:00").append("'");
						break;
					default:
						break;

					}
					String condition = conditionbuffer.toString();

					costingtool.getCostParaVO().setCondition(condition);
					IBill[] aggs = ibillcheck.queryData(costingtool);
					/**
					 * 设置数据缓存 TradeDataTool key: 业务组+单据日期+单据类型
					 */
					if (aggs != null) {
						for (int j = 0; j < aggs.length; j++) {
							IBill ibill = aggs[j];
							ISuperVO parent = ibill.getParent();
							// 每条单据的交易日期
							UFDate history_trade_date = (UFDate) parent.getAttributeValue("trade_date");
							tradedatatool.addData(costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + history_trade_date.toLocalString()
									+ billtype, ibill);
						}
					}

				}
			}
			tradedatatool.setIsinit(true);
		}

	}

	/**
	 * 运行记账处理类
	 * 
	 * @param checkClass
	 * @throws Exception
	 */
	private void runTallyClass(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		List billtypeclass = costingtool.getBilltypeclass();
		Map<String, IBillCheckPlugin> checkclassmap = checkclasstool.getCheckClassMap();
		if (billtypeclass != null) {
			for (int i = 0; i < billtypeclass.size(); i++) {
				PendingBillVO pbvo = (PendingBillVO) billtypeclass.get(i);
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// 设置当前交易类型
				IBillCheckPlugin ibillcheck = checkclassmap.get(pbvo.getPk_billtype() + "##" + pbvo.getCheckclass());
				try {
					ibillcheck.tallyBills(costingtool, tradedatatool);
				} catch (Exception e) {
					throw new Exception("记账日期：" + costingtool.getCurrdate() + "插件类内部报错:" + ibillcheck.toString() + "错误日志：" + e.getMessage());
				}
			}
		}
	}

	private void updateTradeDatas(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws BusinessException {
		List billtypeclass = costingtool.getBilltypeclass();
		Map<String, IBillCheckPlugin> checkclassmap = checkclasstool.getCheckClassMap();
		if (billtypeclass != null) {
			for (int i = 0; i < billtypeclass.size(); i++) {
				PendingBillVO pbvo = (PendingBillVO) billtypeclass.get(i);
				IBillCheckPlugin ibillcheck = checkclassmap.get(pbvo.getPk_billtype() + "##" + pbvo.getCheckclass());
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// 设置当前交易类型
				ibillcheck.updateData(costingtool, tradedatatool);
			}
		}

	}

	/**
	 * 库存缓存使用完毕清理缓存 YangJie 2014-03-26
	 * 
	 * @throws BusinessException
	 */
	private void clearBalanceCache(ScostCalCacheManager scostcalcachemanager) {
		scostcalcachemanager.clear(SystemConst.StockBalance);
		scostcalcachemanager.clear(SystemConst.FundBalance);
	}

	/**
	 * 初始化成本计算数据 各种库存表
	 * 
	 * @throws Exception
	 */
	private void initCost(CostingTool costingtool) throws Exception {
		/**
		 * 初始化库存缓存
		 */
		costingtool.getBalancetool().getScostcalcachemanager().getCacheByTypeCode(SystemConst.StockBalance, costingtool);
		/**
		 * 初始化资金缓存
		 */
		costingtool.getBalancetool().getScostcalcachemanager().getCacheByTypeCode(SystemConst.FundBalance, costingtool);
		/**
		 * 初始化券源划转缓存
		 */
		costingtool.getBalancetool().getScostcalcachemanager().getCacheByTypeCode(SystemConst.TransferBalance, costingtool);

	}

	/**
	 * 运行审核处理类
	 * 
	 * @param checkClass
	 * @throws Exception
	 */
	private void runCheckClass(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		List billtypeclass = costingtool.getBilltypeclass();
		Map<String, IBillCheckPlugin> checkclassmap = checkclasstool.getCheckClassMap();
		if (billtypeclass != null) {
			for (int i = 0; i < billtypeclass.size(); i++) {
				PendingBillVO pbvo = (PendingBillVO) billtypeclass.get(i);
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// 设置当前交易类型
				costingtool.setPk_stocksort(new String[] { pbvo.getPk_stocksort1(), pbvo.getPk_stocksort2() });
				costingtool.setDirection(pbvo.getDirection());
				costingtool.setIsinit(false);// 此处 重新设置缓存标志 非第一次初始化 这个 很重要
												// 考虑弃审以及记账 YangJie 2014-04-24
				IBillCheckPlugin ibillcheck = checkclassmap.get(pbvo.getPk_billtype() + "##" + pbvo.getCheckclass());
				try {
					ibillcheck.checkBills(costingtool, tradedatatool);
				} catch (Exception e) {
					throw new Exception("审核日期：" + costingtool.getCurrdate() + "插件类内部报错:" + ibillcheck.toString() + "错误日志：" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 审核后处理（如数据库持久化、缓存清除）
	 * 
	 * @throws Exception
	 */
	private void afterCheck(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		String trade_date = costingtool.getCurrdate();
		// 先处理成本调整单
		costingtool.saveCostChangeVOs();
		/**
		 * 1.更新库存数据库信息
		 */
		costingtool.getBalancetool().updateAll(trade_date);

		/**
		 * 2.更新业务记录数据库信息
		 */
		updateTradeDatas(costingtool, tradedatatool, checkclasstool);

	}

	/**
	 * 运行弃审处理类
	 * 
	 * @param checkClass
	 * @throws Exception
	 */
	private void runUncheckClass(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		List billtypeclass = costingtool.getBilltypeclass();
		Map<String, IBillCheckPlugin> checkclassmap = checkclasstool.getCheckClassMap();
		if (billtypeclass != null) {
			for (int i = billtypeclass.size()-1; i >=0 ; i--) {
				PendingBillVO pbvo = (PendingBillVO) billtypeclass.get(i);
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// 设置当前交易类型
				IBillCheckPlugin ibillcheck = checkclassmap.get(pbvo.getPk_billtype() + "##" + pbvo.getCheckclass());
				try {
					ibillcheck.unCheckBills(costingtool, tradedatatool);
				} catch (Exception e) {
					throw new Exception("弃审日期：" + costingtool.getCurrdate() + "插件类内部报错:" + ibillcheck.toString() + "错误日志：" + e.getMessage());
				}

			}
		}
	}

	/**
	 * 更新中间表数据以及界面数据 flag :true 为审核 或者弃审 false为记账
	 */
	public void updateMiddleAndApporveUI_RequiresNew(Map<String, List<PendingBillVO>> dataMap, String pk_billtypegroup, int state, String returnmsg)
			throws BusinessException {
		try {
			/**
			 * 1.更新中间表数据 记账成功更新中间表
			 */
			if (returnmsg == null)
				PrivateMethod.getInstance().updateMiddle(dataMap, pk_billtypegroup, state);
			// /**
			// * 2: ApproveVO 插入一行最新的 更新以前的为非最新
			// */
			// PrivateMethod.getInstance().updateApproveUI(vo, checkParaVO,
			// returnmsg, checkflag);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public void updateApporveUI_RequiresNew(BilltypeGroupVO vo, CheckParaVO checkParaVO, String returnmsg, Integer checkflag)
			throws BusinessException {
		try {
			/**
			 * 2: ApproveVO 插入一行最新的 更新以前的为非最新
			 */
			PrivateMethod.getInstance().updateApproveUI(vo, checkParaVO, returnmsg, checkflag);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}
	

	public void Before_RequiresNew(BilltypeGroupVO vo, CheckParaVO checkParaVO, TradeDataTool tradedatatool )
			throws BusinessException {
		
		// 2015年5月28日 zhaoxmc start 这里不能写死，预留给其他模块插入逻辑的机会
		List<String> plugins = new ArrayList<String>();
		plugins.addAll(Arrays.asList(SystemConst.SpecialBusinessImpls));
//		boolean enable = InitGroupQuery.isEnabled(checkParaVO.getPk_group(),"HV17"); //债券借贷:防止模块未启用，反射报错
//		if (enable) {
//			plugins.add("nc.impl.fba_sim.trade.autoplugin.ZqjdAutoReturn");
//		}
		// 2015年5月28日 zhaoxmc end
		
		for (int j = 0; j < plugins.size(); j++) {
			ISpecialBusiness ispbusiness;
			String sbimpl = plugins.get(j);
			try {
				String[] temp = sbimpl.split("\\.");
				ispbusiness = (ISpecialBusiness) NewObjectService.newInstance(temp[2], sbimpl);
			} catch (Exception e) {
				throw new BusinessException(sbimpl + "实例化失败！" + e.getMessage());
			}
			ISuperVO[] vos = ispbusiness.query(vo, checkParaVO,  tradedatatool );
			if (null != vos && vos.length > 0) {
				Map<String, IBill[]> returnmap = ispbusiness.insert(vo, checkParaVO, vos);
			}
			// if (returnmap != null) {
			// Set<String> set = returnmap.keySet();
			// for (Iterator iterator = set.iterator();
			// iterator.hasNext();) {
			// String key = (String) iterator.next();
			// ispbusiness.update(tradedatatool, key,
			// returnmap.get(key));
			// }
			// }
		}

	}

}
