package nc.ui.erm.matterapp.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import common.Logger;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.uap.rbac.core.dataperm.DataPermissionFacade;
import nc.ui.erm.matterapp.model.MAppModel;
import nc.ui.erm.matterapp.view.MatterAppMNBillForm;
import nc.ui.erm.util.ErUiUtil;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.uif.pub.exception.UifException;
import nc.vo.arap.bx.util.ActionUtils;
import nc.vo.er.exception.BugetAlarmBusinessException;
import nc.vo.er.util.StringUtils;
import nc.vo.erm.common.MessageVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.erm.matterapp.MatterAppVO;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.sm.UserVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uif2.LoginContext;

/**
 * 费用申请单管理-跳过审批按钮
 * 
 * @author lihaibo
 * 
 */
public class MatterJumpAppAction extends NCAction {
	private static final long serialVersionUID = -5011329856005930604L;

	private transient MAppModel model;

	private MatterAppMNBillForm billForm;

	// 以下实现数据权限需要
	private String mdOperateCode = null; // 元数据操作编码
	private String operateCode = null; // 资源对象操作编码，以上两者注入其一，都不注入，则不进行数据权限控制。
	private String resourceCode = null; // 业务实体资源编码

	public MatterJumpAppAction() {
		super();
		setCode("跳过审核");
		setBtnName("跳过审核");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		try {
			if (!checkDataPermission()) {
				throw new BusinessException(
						IShowMsgConstant.getDataPermissionInfo());
			}

			Object objs[] = getModel().getSelectedOperaDatas();

			if (objs == null || objs.length == 0) {
				return;
			}

			// 审核较验信息
			MessageVO[] msgs = new MessageVO[objs.length];
			List<AggMatterAppVO> auditList = new ArrayList<AggMatterAppVO>();

			for (int i = 0; i < objs.length; i++) {
				AggMatterAppVO vo = (AggMatterAppVO) objs[i];

				msgs[i] = checkApprove(vo);

				if (msgs[i].isSuccess()) {
					auditList.add(vo);
				}
			}

			if (!auditList.isEmpty()) {
				MessageVO[] returnMsgs = auditOneByOne(auditList);
				List<AggregatedValueObject> auditedVos = ErUiUtil.combineMsgs(
						msgs, returnMsgs);
				getModel().directlyUpdate(
						auditedVos.toArray(new AggregatedValueObject[auditedVos
								.size()]));
			}
			ErUiUtil.showBatchResults(getModel().getContext(), msgs);
		} catch (Exception e2) {
			exceptionHandler.handlerExeption(e2);
		}
	}

	/**
	 * 校验审批数据
	 * 
	 * @param billvo
	 * @return
	 */
	private MessageVO checkApprove(AggMatterAppVO billvo) {
		MessageVO msgVO = new MessageVO(billvo, ActionUtils.AUDIT);

		// 审核日期校验
		UFDate shrq = new UFDate(InvocationInfoProxy.getInstance()
				.getBizDateTime());
		if (billvo.getParentVO().getBilldate().afterDate(shrq)) {
			msgVO.setSuccess(false);
			msgVO.setErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2011", "UPP2011-000336")/* @res "审核日期不能早于单据录入日期" */);
			return msgVO;
		}

		// 审批状态校验
		NCObject ncObj = NCObject.newInstance(billvo);
		IFlowBizItf itf = (IFlowBizItf) ncObj
				.getBizInterface(nc.itf.uap.pf.metadata.IFlowBizItf.class);
		Integer approveStatus = itf.getApproveStatus();// 审批状态

		if (!(approveStatus.equals(IBillStatus.CHECKGOING) || approveStatus
				.equals(IBillStatus.COMMIT))) {
			msgVO.setSuccess(false);
			msgVO.setErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("201212_0", "0201212-0008")/*
															 * @res
															 * "该单据当前状态不能进行审核！"
															 */);
			return msgVO;
		}

		return msgVO;
	}

	private MessageVO[] auditOneByOne(List<AggMatterAppVO> auditVOs)
			throws Exception {
		List<MessageVO> resultList = new ArrayList<MessageVO>();
		for (AggMatterAppVO aggVo : auditVOs) {
			MessageVO msgReturn = approveSingle(aggVo);

			resultList.add(msgReturn);
		}
		return resultList.toArray(new MessageVO[resultList.size()]);
	}

	private MessageVO approveSingle(AggMatterAppVO appVO) throws Exception {
		MessageVO result = null;
		try {
			String billno = appVO.getParentVO().getBillno();// 单据编号
			String checkman = null;
			// 查询审批流
			WorkflownoteVO[] workflownoteVOs = (WorkflownoteVO[]) HYPubBO_Client
					.queryByCondition(WorkflownoteVO.class, "billno = '"
							+ billno
							+ "' and checknote is null and approvestatus = 0");
			if (workflownoteVOs != null && workflownoteVOs.length > 0) {
				checkman = workflownoteVOs[0].getCheckman();
			}
			// 审核动作处理
			Object returnObj = PfUtilClient.runAction(getModel().getContext()
					.getEntranceUI(), "APPROVE" + checkman, appVO.getParentVO()
					.getPk_tradetype(), appVO, null, null, null, null);

			if (returnObj == null) {// 在审批过程中，弹出审核界面，然后直接点右上角的关闭
				result = new MessageVO(appVO, ActionUtils.AUDIT);
				result.setSuccess(false);
				result.setErrorMessage(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("2011", "UPP2011-000339")/*
																			 * @res
																			 * "用户取消操作"
																			 */);
			} else {
				String warnMsg = null;
				if (returnObj instanceof MessageVO[]) {
					MessageVO[] msgVos = (MessageVO[]) returnObj;
					MatterAppVO parentVO = (MatterAppVO) msgVos[0]
							.getSuccessVO().getParentVO();
					warnMsg = parentVO.getWarningmsg();
					parentVO.setWarningmsg(null);
					result = msgVos[0];
				} else if (returnObj instanceof AggMatterAppVO) {// 改签和加签的情况下会出现返回AggVo
					warnMsg = ((AggMatterAppVO) returnObj).getParentVO()
							.getWarningmsg();
					((AggMatterAppVO) returnObj).getParentVO().setWarningmsg(
							null);
					result = new MessageVO((AggMatterAppVO) returnObj,
							ActionUtils.AUDIT);
				}

				if (!StringUtils.isNullWithTrim(warnMsg)) {
					MessageDialog
							.showWarningDlg(
									getBillForm().getParent(),
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("201212_0",
													"0201212-0000")/* @res "提示" */,
									warnMsg);
				}
			}

		} catch (BugetAlarmBusinessException e) {
			if (MessageDialog.showYesNoDlg(
					getBillForm().getParent(),
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2011", "UPP2011-000049")/*
													 * @ res "提示"
													 */, e.getMessage()
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("2011", "UPP2011-000341")/*
																		 * @ res
																		 * " 是否继续审核？"
																		 */) == MessageDialog.ID_YES) {
				appVO.getParentVO().setHasntbcheck(UFBoolean.TRUE); // 不检查
				result = approveSingle(appVO);

			} else {
				result = new MessageVO(appVO, ActionUtils.AUDIT, false,
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"2011", "UPP2011-000405")/*
														 * @res "预算申请失败"
														 */);
			}
		} catch (Exception e) {
			ExceptionHandler.consume(e);
			String errMsg = e.getMessage();
			final String lockErrMsg = NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000602")/*
																	 * @ res
																	 * "当前单据已进行加锁处理"
																	 */;
			if (e instanceof PFBusinessException && lockErrMsg.equals(errMsg)) {
				errMsg = e.getMessage()
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("2011v61013_0", "02011v61013-0000")/*
																				 * @
																				 * res
																				 * "，该单据可能同时被他人操作中，请刷新后再试"
																				 */;
			}

			result = new MessageVO(appVO, ActionUtils.AUDIT, false, errMsg);
		}
		return result;
	}

	@Override
	protected boolean isActionEnable() {
		if (getModel().getUiState() != UIState.NOT_EDIT)
			return false;
		Object[] selectedData = getModel().getSelectedOperaDatas();
		if (selectedData == null)
			return false;
		List<String> usercode = new ArrayList<String>();
		usercode.add("hx001");
		usercode.add("hx002");
		usercode.add("hx003");
		usercode.add("lvsy");
		String user = getModel().getContext().getPk_loginUser();
		try {
			UserVO[] userVOs = (nc.vo.sm.UserVO[]) HYPubBO_Client
					.queryByCondition(UserVO.class, "cuserid = '" + user + "'");
			String code = userVOs[0].getUser_code();
			if(!usercode.contains(code)) {
				return false;
			}
		} catch (UifException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < selectedData.length; i++) {
			AggMatterAppVO aggBean = (AggMatterAppVO) selectedData[i];
			Integer appStatus = ((MatterAppVO) aggBean.getParentVO())
					.getApprstatus();
			// 审核中
			if (appStatus.equals(IBillStatus.CHECKGOING)
					|| appStatus.equals(IBillStatus.COMMIT)) {
				return true;
			}
		}

		return false;
	}

	protected boolean checkDataPermission() {
		if (StringUtil.isEmptyWithTrim(getOperateCode())
				&& StringUtil.isEmptyWithTrim(getMdOperateCode())
				|| StringUtil.isEmptyWithTrim(getResourceCode()))
			return true;

		LoginContext context = getModel().getContext();
		String userId = context.getPk_loginUser();
		String pkgroup = context.getPk_group();
		Object data = getModel().getSelectedData();
		boolean hasp = true;
		if (!StringUtil.isEmptyWithTrim(getMdOperateCode()))
			hasp = DataPermissionFacade.isUserHasPermissionByMetaDataOperation(
					userId, getResourceCode(), getMdOperateCode(), pkgroup,
					data);
		else
			hasp = DataPermissionFacade.isUserHasPermission(userId,
					getResourceCode(), getOperateCode(), pkgroup, data);
		return hasp;
	}

	public MAppModel getModel() {
		return model;
	}

	public void setModel(MAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public MatterAppMNBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(MatterAppMNBillForm billForm) {
		this.billForm = billForm;
	}

	public String getMdOperateCode() {
		return mdOperateCode;
	}

	public void setMdOperateCode(String mdOperateCode) {
		this.mdOperateCode = mdOperateCode;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

}
