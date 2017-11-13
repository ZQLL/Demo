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
 * �����ʵ����
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
				costParaVO.setCheckstate(SystemConst.checkstate[1]);// ���˷���
				String key = date.toLocalString().concat(pk_billtypegroup);
				List<PendingBillVO> billtypeclass = dataMap.get(key);
				CostingTool costingtool = new CostingTool();
				costingtool.setCostParaVO(costParaVO);
				costingtool.setBilltypeclass(billtypeclass);
				costingtool.setCurrbilltypegroupvo(vo);// ��ǰҵ����
				costingtool.setCurrdate(date.toLocalString());// ������ҵ��ʱ��
				costingtool.setIstally(true);// ���ͼ�����Ϣ
				checkTally(pk_billtypegroup, date);
				
				
				
				beforeCheck(costingtool, tradedatatool, checkclasstool);
				runTallyClass(costingtool, tradedatatool, checkclasstool);
				updateTradeDatas(costingtool, tradedatatool, checkclasstool);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			if (e.getMessage() == null) {
				msg = "���˳����쳣";
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
			//ͨ��ǰ̨�������ж��Ƿ���ú�̨����ʹ����˵Ľ��뵥�����ļ��ᵥ����ɾ��
			IBillMakeJTD bmj = NCLocator.getInstance().lookup(IBillMakeJTD.class);
			if (bmj.getBooleanFromInitcode(checkParaVO.getPk_glorgbook(), "Zqjd02")) {
				bmj.DeleteJTD(checkParaVO);
			}
			for (UFDate date : set) {
				CostParaVO costParaVO = new CostParaVO();
				costParaVO.setCheckParavo(checkParaVO);
				costParaVO.setCheckstate(SystemConst.checkstate[2]); // ���󷽷�
				String key = date.toLocalString().concat(pk_billtypegroup);
				CostingTool costingtool = new CostingTool();
				costingtool.setCostParaVO(costParaVO);
				costingtool.setCurrbilltypegroupvo(vo);// ��ǰҵ����
				costingtool.setCurrdate(date.toLocalString());// ������ҵ��ʱ��
				costingtool.setIstally(false);// ����ȡ��������Ϣ
				checkTally(pk_billtypegroup, date);
				if (dataMap != null && vo.getLasttallydate() != null && date.toLocalString().compareTo(vo.getLasttallydate().toLocalString()) <= 0) {
					// ��ȡ�������߼�
					List<PendingBillVO> billtypeclass = dataMap.get(key);
					costingtool.setBilltypeclass(billtypeclass);
					beforeCheck(costingtool, tradedatatool, checkclasstool);
					runTallyClass(costingtool, tradedatatool, checkclasstool);
				}
				/**
				 * ���� ѭ�����ò��������
				 */
				/**
				 * 1.��ȡ���� 2.ִ�в�������� 3.������Ϻ���� �����Լ��м�� �Լ�����vo 4.ɾ����� 3�����
				 */
				if (needuncheckdataMap != null) {
					List<PendingBillVO> billtypeclass = needuncheckdataMap.get(key);
					costingtool.setBilltypeclass(billtypeclass);
					beforeCheck(costingtool, tradedatatool, checkclasstool);
					runUncheckClass(costingtool, tradedatatool, checkclasstool);
				}
				updateTradeDatas(costingtool, tradedatatool, checkclasstool);
				/**
				 * ���Ƿŵ�ҵ�����ڴ��� ɾ�����ֿ��
				 */
				// // ɾ�����ɵ�������ع��ʼ�ֵ���ᵥ
				// String wherecond = "pk_fairvaluedistill in " +
				// "(select pk_fairvaluedistill from sim_fairvaluedistill where "
				// +
				// sqlwhere + ")";
				// basedao.deleteByClause(FairvaluedistillBVO.class, wherecond);
				// basedao.deleteByClause(FairvaluedistillVO.class, sqlwhere);
				// // ɾ���Զ����ɵĻ���/��Ϣ��
				// payBO.deleteAutoPayBill(pk_corp, pk_glorgbook, pk_user,
				// trade_date);
				// // ɾ������֮������м�¼
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
				// ɾ��������
				PrivateMethod.getInstance().getBaseDAO().deleteByClause(StockBalanceVO.class, sqlwhere);
				// ɾ���ʽ�������
				PrivateMethod.getInstance().getBaseDAO().deleteByClause(FundBalanceVO.class, sqlwhere);
				// ɾ��ȯԴ��ת����
				PrivateMethod.getInstance().getBaseDAO().deleteByClause(TransferBalanceVO.class, sqlwhere);

				// basedao.deleteByClause(TotalwinVO.class, sqlwhere);// ɾ���ۼ������
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			if (e.getMessage() == null) {
				msg = "��������쳣";
			} else {
				msg = e.getMessage();
			}

			throw new BusinessException(msg);
		} finally {
			/**
			 * ���� Ŀǰ���õ�Ҳ�ǲ������ݵķ�ʽ �������ܻῼ��ɾ����ʷ�ķ�ʽ YangJie 2014-04-16
			 */
			NCLocator.getInstance().lookup(IBillCheckByGroup.class)
					.updateMiddleAndApporveUI_RequiresNew(needuncheckdataMap, pk_billtypegroup, SystemConst.statecode[1], msg);
		}
	}

	public void checkOneGroup_RequiresNew(BilltypeGroupVO vo, Set<UFDate> set, Map<String, List<PendingBillVO>> dataMap, CheckParaVO checkParaVO,
			TradeDataTool tradedatatool, CheckClassTool checkclasstool, List<CostPlanVO> clist, boolean isFirstPrice, boolean iscalfund)
			throws BusinessException {
		int stockflag = 0;// YangJie 2014-04-03 ����ʼ��־ ����ͬһ���ɱ����㷽��
		// ֻ��Ҫ��һ�ο�滺��
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
					costParaVO.setCheckstate(SystemConst.checkstate[0]);// ��˷���
					String key = date.toLocalString().concat(pk_billtypegroup);
					List<PendingBillVO> billtypeclass = dataMap.get(key);
					CostingTool costingtool = new CostingTool();
					costingtool.setCheckFund(iscalfund);
					costingtool.setCostParaVO(costParaVO);
					costingtool.getBalancetool().setScostcalcachemanager(scostcalcachemanager);// ���ÿ�滺��
					costingtool.setBilltypeclass(billtypeclass);
					costingtool.setCurrbilltypegroupvo(vo);// ��ǰҵ����
					costingtool.setCurrdate(date.toLocalString());// ��ǰ���ҵ��ʱ��
					/**
					 * YangJie 2014-04-08 ��滺���ʼ������ 1.���ɱ����㷽����ѭ����Ҫȫ���ʼ��
					 * 2.���ɱ����㷽����ѭ����ҪƬ���ʼ���������ݵ�ǰʱ�����ǰ���ջ�����ʹ��
					 */
					if (stockflag < clist.size()) {
						costingtool.setIsinit(true);
						initCost(costingtool);
					} else {
						costingtool.setIsinit(false);
						initCost(costingtool);
					}
						
					checkTally(pk_billtypegroup, date);
					/*************�ڴ��������ծȯ���׼�¼��������  lqm**************/
					zqjdutils utis=new zqjdutils();
					utis.zqjdautoAudit(vo,costingtool, tradedatatool,checkclasstool);
					/**********�ڴ��������ծȯ���׼�¼��������  end***/
				
					beforeCheck(costingtool, tradedatatool, checkclasstool);
		
					runCheckClass(costingtool, tradedatatool, checkclasstool);
					//���ʱ�Զ����ɹ黹��  lqm
					utis.autoreturn(vo,  dataMap, costingtool, tradedatatool, checkclasstool)	;
					
					
				
					/**
					 * ������ 1.���¿�� 2.����ҵ�񵥾�
					 */
					afterCheck(costingtool, tradedatatool, checkclasstool);
					stockflag++;
				}
			}
			//ͨ��ǰ̨�������ж��Ƿ���ú�̨����ʹ����˵Ľ��뵥�������ᵥ--zq
			IBillMakeJTD bmj = NCLocator.getInstance().lookup(
					IBillMakeJTD.class);
			if (bmj.getBooleanFromInitcode(checkParaVO.getPk_glorgbook(), "Zqjd02")) {
					bmj.MakeJTD(checkParaVO);
			}
			
		} catch (Exception e) {
			Logger.error(e.getMessage());
			if (e.getMessage() == null) {
				msg = "��˳����쳣";
			} else {
				msg = e.getMessage();
			}
			throw new BusinessException(msg);
		} finally {
			/**
			 * �����滺��
			 */
			clearBalanceCache(scostcalcachemanager);
			NCLocator.getInstance().lookup(IBillCheckByGroup.class)
					.updateMiddleAndApporveUI_RequiresNew(dataMap, pk_billtypegroup, SystemConst.statecode[3], msg);

		}

	}
	

	/**
	 * ���ǰ���ָ��ҵ����� date�ĺϷ��� �˶�̨����Ϣ
	 * 
	 * @param pk_billtypegroup
	 * @param date
	 * @throws Exception
	 */
	private void checkTally(String pk_billtypegroup, UFDate date) throws Exception {

	}

	/**
	 * ���ǰԤ����
	 * 
	 * @throws Exception
	 */
	private void beforeCheck(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		//�Զ�������Ϣ
		AutoPay pay = new AutoPay();
		pay.autoPay(costingtool,tradedatatool);
		// ��ѯ���δ���/�����/�Ѽ�������
		initTradeData(costingtool, tradedatatool, checkclasstool);

	}

	/**
	 * ������ݲ�ѯ YangJie 2014-03-28
	 * 
	 * @throws Exception
	 */
	private void initTradeData(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		/**
		 * ֻҪ��ʼ��һ�����ݼ��� YangJie 2014-05-22
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
					 * ��ѯ����Ϊ�������� �޽������͵��� �������ʹ����������� YangJie 2014-03-28
					 */
					conditionbuffer.append(" and transtypecode='");
					conditionbuffer.append(billtype).append("' ");

					if (pk_glorgbook != null) {
						conditionbuffer.append(" and pk_glorgbook='");
						conditionbuffer.append(pk_glorgbook).append("'");
					}

					switch (checkstate) {
					case 0:// ���
						conditionbuffer.append(" and state=0 ");
						/**
						 * ���� ��ѯ������Ϊ׼ ���޸� YangJie 2014-03-28
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
					case 1:// ����
						conditionbuffer.append(" and state=2 ");
						/**
						 * ���� ��ѯ������Ϊ׼ ���޸� YangJie 2014-03-28
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
					case 2:// ����
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
					 * �������ݻ��� TradeDataTool key: ҵ����+��������+��������
					 */
					if (aggs != null) {
						for (int j = 0; j < aggs.length; j++) {
							IBill ibill = aggs[j];
							ISuperVO parent = ibill.getParent();
							// ÿ�����ݵĽ�������
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
	 * ���м��˴�����
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
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// ���õ�ǰ��������
				IBillCheckPlugin ibillcheck = checkclassmap.get(pbvo.getPk_billtype() + "##" + pbvo.getCheckclass());
				try {
					ibillcheck.tallyBills(costingtool, tradedatatool);
				} catch (Exception e) {
					throw new Exception("�������ڣ�" + costingtool.getCurrdate() + "������ڲ�����:" + ibillcheck.toString() + "������־��" + e.getMessage());
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
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// ���õ�ǰ��������
				ibillcheck.updateData(costingtool, tradedatatool);
			}
		}

	}

	/**
	 * ��滺��ʹ����������� YangJie 2014-03-26
	 * 
	 * @throws BusinessException
	 */
	private void clearBalanceCache(ScostCalCacheManager scostcalcachemanager) {
		scostcalcachemanager.clear(SystemConst.StockBalance);
		scostcalcachemanager.clear(SystemConst.FundBalance);
	}

	/**
	 * ��ʼ���ɱ��������� ���ֿ���
	 * 
	 * @throws Exception
	 */
	private void initCost(CostingTool costingtool) throws Exception {
		/**
		 * ��ʼ����滺��
		 */
		costingtool.getBalancetool().getScostcalcachemanager().getCacheByTypeCode(SystemConst.StockBalance, costingtool);
		/**
		 * ��ʼ���ʽ𻺴�
		 */
		costingtool.getBalancetool().getScostcalcachemanager().getCacheByTypeCode(SystemConst.FundBalance, costingtool);
		/**
		 * ��ʼ��ȯԴ��ת����
		 */
		costingtool.getBalancetool().getScostcalcachemanager().getCacheByTypeCode(SystemConst.TransferBalance, costingtool);

	}

	/**
	 * ������˴�����
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
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// ���õ�ǰ��������
				costingtool.setPk_stocksort(new String[] { pbvo.getPk_stocksort1(), pbvo.getPk_stocksort2() });
				costingtool.setDirection(pbvo.getDirection());
				costingtool.setIsinit(false);// �˴� �������û����־ �ǵ�һ�γ�ʼ�� ��� ����Ҫ
												// ���������Լ����� YangJie 2014-04-24
				IBillCheckPlugin ibillcheck = checkclassmap.get(pbvo.getPk_billtype() + "##" + pbvo.getCheckclass());
				try {
					ibillcheck.checkBills(costingtool, tradedatatool);
				} catch (Exception e) {
					throw new Exception("������ڣ�" + costingtool.getCurrdate() + "������ڲ�����:" + ibillcheck.toString() + "������־��" + e.getMessage());
				}
			}
		}
	}

	/**
	 * ��˺��������ݿ�־û������������
	 * 
	 * @throws Exception
	 */
	private void afterCheck(CostingTool costingtool, TradeDataTool tradedatatool, CheckClassTool checkclasstool) throws Exception {
		String trade_date = costingtool.getCurrdate();
		// �ȴ���ɱ�������
		costingtool.saveCostChangeVOs();
		/**
		 * 1.���¿�����ݿ���Ϣ
		 */
		costingtool.getBalancetool().updateAll(trade_date);

		/**
		 * 2.����ҵ���¼���ݿ���Ϣ
		 */
		updateTradeDatas(costingtool, tradedatatool, checkclasstool);

	}

	/**
	 * ������������
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
				costingtool.setCurrbilltype(pbvo.getPk_billtype());// ���õ�ǰ��������
				IBillCheckPlugin ibillcheck = checkclassmap.get(pbvo.getPk_billtype() + "##" + pbvo.getCheckclass());
				try {
					ibillcheck.unCheckBills(costingtool, tradedatatool);
				} catch (Exception e) {
					throw new Exception("�������ڣ�" + costingtool.getCurrdate() + "������ڲ�����:" + ibillcheck.toString() + "������־��" + e.getMessage());
				}

			}
		}
	}

	/**
	 * �����м�������Լ��������� flag :true Ϊ��� �������� falseΪ����
	 */
	public void updateMiddleAndApporveUI_RequiresNew(Map<String, List<PendingBillVO>> dataMap, String pk_billtypegroup, int state, String returnmsg)
			throws BusinessException {
		try {
			/**
			 * 1.�����м������ ���˳ɹ������м��
			 */
			if (returnmsg == null)
				PrivateMethod.getInstance().updateMiddle(dataMap, pk_billtypegroup, state);
			// /**
			// * 2: ApproveVO ����һ�����µ� ������ǰ��Ϊ������
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
			 * 2: ApproveVO ����һ�����µ� ������ǰ��Ϊ������
			 */
			PrivateMethod.getInstance().updateApproveUI(vo, checkParaVO, returnmsg, checkflag);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}
	

	public void Before_RequiresNew(BilltypeGroupVO vo, CheckParaVO checkParaVO, TradeDataTool tradedatatool )
			throws BusinessException {
		
		// 2015��5��28�� zhaoxmc start ���ﲻ��д����Ԥ��������ģ������߼��Ļ���
		List<String> plugins = new ArrayList<String>();
		plugins.addAll(Arrays.asList(SystemConst.SpecialBusinessImpls));
//		boolean enable = InitGroupQuery.isEnabled(checkParaVO.getPk_group(),"HV17"); //ծȯ���:��ֹģ��δ���ã����䱨��
//		if (enable) {
//			plugins.add("nc.impl.fba_sim.trade.autoplugin.ZqjdAutoReturn");
//		}
		// 2015��5��28�� zhaoxmc end
		
		for (int j = 0; j < plugins.size(); j++) {
			ISpecialBusiness ispbusiness;
			String sbimpl = plugins.get(j);
			try {
				String[] temp = sbimpl.split("\\.");
				ispbusiness = (ISpecialBusiness) NewObjectService.newInstance(temp[2], sbimpl);
			} catch (Exception e) {
				throw new BusinessException(sbimpl + "ʵ����ʧ�ܣ�" + e.getMessage());
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
