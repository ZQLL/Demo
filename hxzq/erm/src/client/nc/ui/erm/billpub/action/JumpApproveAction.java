package nc.ui.erm.billpub.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.erm.billpub.btnstatus.BxApproveBtnStatusListener;
import nc.ui.erm.util.ErUiUtil;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.uif2.annoations.MethodType;
import nc.uif2.annoations.ModelMethod;
import nc.uif2.annoations.ModelType;
import nc.vo.arap.bx.util.ActionUtils;
import nc.vo.arap.bx.util.BXStatusConst;
import nc.vo.cmp.exception.CmpAuthorizationException;
import nc.vo.cmp.exception.ErmException;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.exception.BugetAlarmBusinessException;
import nc.vo.er.exception.ProjBudgetAlarmBusinessException;
import nc.vo.er.util.StringUtils;
import nc.vo.erm.common.MessageVO;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * ���ݲ�ѯ����������ť
 * 
 * @author lihaibo
 * 
 */
public class JumpApproveAction extends NCAction {

	private static final long serialVersionUID = -6119660306745996573L;

	private nc.ui.erm.billpub.model.ErmBillBillManageModel model;

	private nc.ui.erm.billpub.view.ErmBillBillForm editor;

	private nc.ui.uif2.actions.RefreshAction listRefresh;

	private nc.ui.erm.billpub.action.ERMBillSingleRefreshAction cardRefresh;

	private BxApproveBtnStatusListener auditstausListener;

	public JumpApproveAction() {
		super();
		setCode("�������");
		setBtnName("�������");
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
		Object[] vos = (Object[]) getModel().getSelectedOperaDatas();

		if (vos == null ) {
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("2006030102", "UPP2006030102-000116")/*
																	 * @res
																	 * "û�пɹ���˵ĵ���,����ʧ��"
																	 */);
		}
		// ��˽�����Ϣ
		MessageVO[] msgs = new MessageVO[vos.length];

		List<JKBXVO> auditVOs = new ArrayList<JKBXVO>();

		JKBXVO[] jkbxVos = Arrays.asList(vos).toArray(new JKBXVO[0]);

		for (int i = 0; i < jkbxVos.length; i++) {

			JKBXVO vo = jkbxVos[i];
			// ���������ȫ�����������������֧��״̬Ӧ������Ϊ��ȫ������
			if (vo.getParentVO().zfybje.doubleValue() == 0
					&& vo.getParentVO().hkybje.doubleValue() == 0) {
				vo.getParentVO().setPayflag(BXStatusConst.ALL_CONTRAST);
			}

			vo.setNCClient(true);

			msgs[i] = checkVo(vo);

			if (!msgs[i].isSuccess()) {
				continue;
			}

			auditVOs.add(vo);
		}

		if (auditVOs.size() > 0) {
			MessageVO[] returnMsgs = auditOneByOne(auditVOs);
			List<AggregatedValueObject> resultVos = ErUiUtil.combineMsgs(msgs,
					returnMsgs);
			getModel().directlyUpdate(
					resultVos.toArray(new AggregatedValueObject[0]));
		}

		// ��ʾԤ�㣬�����Ƶ���ʾ��Ϣ
		if (!StringUtils.isNullWithTrim(jkbxVos[0].getWarningMsg())
				&& jkbxVos[0].getWarningMsg().length() > 0) {
			MessageDialog.showWarningDlg(getEditor(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("smcomm", "UPP1005-000070")/*
																		 * *
																		 * 
																		 * @ res
																		 * *
																		 * "����"
																		 */,
					jkbxVos[0].getWarningMsg());
			jkbxVos[0].setWarningMsg(null);
		}

		ErUiUtil.showBatchResults(getModel().getContext(), msgs);
		
	}

	private MessageVO[] auditOneByOne(List<JKBXVO> auditVOs) {
		List<MessageVO> resultList = new ArrayList<MessageVO>();
		for (JKBXVO bxvo : auditVOs) {
			MessageVO msgReturn = auditSingle(bxvo);
			if (msgReturn != null && msgReturn.getSuccessVO() != null) {
				JKBXVO vo = (JKBXVO) msgReturn.getSuccessVO();
				vo.setHasJkCheck(false);// ������
				vo.setHasNtbCheck(false);// Ԥ�����
				vo.setHasProBudgetCheck(false);// ��ĿԤ�����
			}
			resultList.add(msgReturn);
		}
		return resultList.toArray(new MessageVO[0]);
	}

	/**
	 * @param ����˶�������
	 * @throws BusinessException
	 *             -Exception
	 * @see ע��:����actionCode���Բ������ַ�ʽ (1)��������+getBxParam().getPk_user ����userObj
	 *      �û��Զ���������Ϊ �� (2)�������� ����userObj �û��Զ���������Ϊ getBxParam().getPk_user
	 * @author liansg
	 */

	private MessageVO auditSingle(JKBXVO bxvo) {
		JKBXHeaderVO head = bxvo.getParentVO();
		MessageVO result = null;
		try {
			String billno = head.getDjbh();// ���ݱ��
			String checkman = null; 
			// ��ѯ������
			WorkflownoteVO[] workflownoteVOs = (WorkflownoteVO[]) HYPubBO_Client
					.queryByCondition(WorkflownoteVO.class, "billno = '"
							+ billno
							+ "' and checknote is null and approvestatus = 0");
			if(workflownoteVOs != null && workflownoteVOs.length > 0) {
				checkman = workflownoteVOs[0].getCheckman();
			}
			// ��˶�������
			Object msgReturn = PfUtilClient.runAction(getModel().getContext()
					.getEntranceUI(), "APPROVE"
					+ checkman, head.getDjlxbm(), bxvo, null, null,
					null, null);

			if (msgReturn == null) {
				result = new MessageVO(bxvo, ActionUtils.AUDIT, false,
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"2011", "UPP2011-000339")/*
														 * @res "�û�ȡ������"
														 */);
			} else {
				// ��ʾԤ�㣬�����Ƶ���ʾ��Ϣ
				String warningMsg = null;
				if (msgReturn instanceof MessageVO[]) {
					MessageVO[] msgVos = (MessageVO[]) msgReturn;
					JKBXVO jkbxvo = (JKBXVO) msgVos[0].getSuccessVO();
					warningMsg = jkbxvo.getWarningMsg();
					jkbxvo.setWarningMsg(null);
					result = msgVos[0];
				} else if (msgReturn instanceof JKBXVO) {// ��ǩ�ͼ�ǩ������»���ַ���AggVo
					warningMsg = ((JKBXVO) msgReturn).getWarningMsg();
					((JKBXVO) msgReturn).setWarningMsg(null);
					result = new MessageVO((JKBXVO) msgReturn,
							ActionUtils.AUDIT);
				}

				if (!StringUtils.isNullWithTrim(warningMsg)) {
					MessageDialog
							.showWarningDlg(
									getEditor(),
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("smcomm",
													"UPP1005-000070")/*
																	 * @ res
																	 * "����"
																	 */,
									warningMsg);
				}
			}

		} catch (BugetAlarmBusinessException e) {
			if (MessageDialog.showYesNoDlg(
					getEditor(),
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2011", "UPP2011-000049")/*
													 * @ res "��ʾ"
													 */, e.getMessage()
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("2011", "UPP2011-000341")/*
																		 * @ res
																		 * " �Ƿ������ˣ�"
																		 */) == MessageDialog.ID_YES) {
				bxvo.setHasNtbCheck(Boolean.TRUE); // �����
				result = auditSingle(bxvo);
			} else {
				result = new MessageVO(bxvo, ActionUtils.AUDIT, false,
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"2011", "UPP2011-000405")/*
														 * @res "Ԥ������ʧ��"
														 */);
			}
		} catch (ErmException e) {
			if (!Boolean.TRUE.equals(bxvo.getHasJkCheck())) {
				if (MessageDialog
						.showYesNoDlg(
								getEditor(),
								nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
										.getStrByID("2011", "UPP2011-000049")/*
																			 * @
																			 * res
																			 * "��ʾ"
																			 */,
								e.getMessage()
										+ nc.vo.ml.NCLangRes4VoTransl
												.getNCLangRes().getStrByID(
														"2011",
														"UPP2011-000341")/*
																		 * @ res
																		 * " �Ƿ������ˣ�"
																		 */) == MessageDialog.ID_YES) {
					bxvo.setHasJkCheck(Boolean.TRUE); // �����
					result = auditSingle(bxvo);
				} else {
					result = new MessageVO(bxvo, ActionUtils.AUDIT, false,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("2011", "UPP2011-000406")/*
																		 * @res
																		 * "�������ʧ��"
																		 */);
				}
			}
		} catch (nc.vo.cmp.exception.CmpAuthorizationException exp) {
			try {
				Class<?> handler = Class
						.forName("nc.ui.cmp.settlement.exception.AccountExceptionHandler");
				Constructor<?> constructor = handler
						.getConstructor(new Class[] { Component.class });
				Object handlerobj = constructor.newInstance(getEditor());
				Method handleexception = handler.getMethod("handleException",
						new Class[] { CmpAuthorizationException.class });

				// �Ƿ�������
				Object pass = handleexception.invoke(handlerobj, exp);
				if ((Boolean) pass) {
					handleexception = handler.getMethod("getAccountList");
					@SuppressWarnings("unchecked")
					List<String> authList = (List<String>) handleexception
							.invoke(handlerobj);
					bxvo.authList = authList;
					result = auditSingle(bxvo);
				} else {
					result = new MessageVO(bxvo, ActionUtils.AUDIT, false,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("2011", "UPP2011-000340")/*
																		 * @res
																		 * "�˻���������"
																		 */);
				}
			} catch (Exception e) {
				String msg = e.getMessage();
				if (e instanceof java.lang.reflect.InvocationTargetException) {
					msg = e.getCause().getMessage();
				}
				Logger.error(msg, e);
				throw new BusinessRuntimeException(msg);
			}
		} catch (ProjBudgetAlarmBusinessException e) {// ��ĿԤ��ɻָ��쳣
			if (MessageDialog.showYesNoDlg(
					getEditor(),
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2011", "UPP2011-000049")/*
													 * @ res "��ʾ"
													 */, e.getMessage()
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("2011", "UPP2011-000341")/*
																		 * @ res
																		 * " �Ƿ������ˣ�"
																		 */) == MessageDialog.ID_YES) {
				bxvo.setHasProBudgetCheck(Boolean.TRUE); // �����
				result = auditSingle(bxvo);

			} else {
				result = new MessageVO(bxvo, ActionUtils.AUDIT, false,
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"201107_0", "0201107-0000")/*
															 * @res "��ĿԤ������ʧ��"
															 */);
			}
		} catch (Exception e) {
			ExceptionHandler.consume(e);
			String errMsg = e.getMessage();
			final String lockErrMsg = NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000602")/*
																	 * @ res
																	 * "��ǰ�����ѽ��м�������"
																	 */;
			if (e instanceof PFBusinessException && lockErrMsg.equals(errMsg)) {
				errMsg = modifyAddLockErrShowMsg(e);
			}
			result = new MessageVO(bxvo, ActionUtils.AUDIT, false, errMsg);
		}
		return result;
	}

	@Override
	protected boolean isActionEnable() {
		boolean isenable =  super.isActionEnable();
		if(getModel().getData() != null) {
			isenable = true;
		} else {
			isenable = false;
		}
		return isenable;
	}
	
	/**
	 * �޸ļ���ʧ�ܵ���ʾ��Ϣ
	 * 
	 * @author chendya
	 */
	private String modifyAddLockErrShowMsg(Exception e) {
		StringBuffer msg = new StringBuffer();
		msg.append(e.getMessage()).append(
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"2011v61013_0", "02011v61013-0000")/*
															 * @ res
															 * "���õ��ݿ���ͬʱ�����˲����У���ˢ�º�����"
															 */);
		return msg.toString();
	}

	/**
	 * @param bxvo
	 * @param background
	 * @return
	 * 
	 *         У�������Ϣ
	 */
	private MessageVO checkVo(JKBXVO bxvo) {

		JKBXHeaderVO head = null;

		head = (bxvo.getParentVO());

		MessageVO msgVO = new MessageVO(bxvo, ActionUtils.AUDIT);

		UFDate djrq = head.getDjrq();

		// begin-- modified by chendya@ufida.com.cn ���Ƚ�ʱ���룬ֻ�Ƚ�Y-M-D
		if (djrq.afterDate(WorkbenchEnvironment.getInstance().getBusiDate())) {
			// --end
			msgVO.setSuccess(false);
			msgVO.setErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2011", "UPP2011-000000")/*
														 * @ res
														 * "������ڲ������ڵ���¼������,�������"
														 */);
			return msgVO;
		}

		return msgVO;
	}

	@ModelMethod(modelType = ModelType.BILLMANAGEMODEL, methodType = MethodType.GETTER)
	public nc.ui.erm.billpub.model.ErmBillBillManageModel getModel() {
		return model;
	}

	@ModelMethod(modelType = ModelType.BILLMANAGEMODEL, methodType = MethodType.SETTER)
	public void setModel(nc.ui.erm.billpub.model.ErmBillBillManageModel model) {
		this.model = model;
	}

	public nc.ui.erm.billpub.view.ErmBillBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.erm.billpub.view.ErmBillBillForm editor) {
		this.editor = editor;
	}

	public nc.ui.uif2.actions.RefreshAction getListRefresh() {
		return listRefresh;
	}

	public void setListRefresh(nc.ui.uif2.actions.RefreshAction listRefresh) {
		this.listRefresh = listRefresh;
	}

	public nc.ui.erm.billpub.action.ERMBillSingleRefreshAction getCardRefresh() {
		return cardRefresh;
	}

	public void setCardRefresh(
			nc.ui.erm.billpub.action.ERMBillSingleRefreshAction cardRefresh) {
		this.cardRefresh = cardRefresh;
	}

	public BxApproveBtnStatusListener getAuditstausListener() {
		return auditstausListener;
	}

	public void setAuditstausListener(
			BxApproveBtnStatusListener auditstausListener) {
		this.auditstausListener = auditstausListener;
	}

}
