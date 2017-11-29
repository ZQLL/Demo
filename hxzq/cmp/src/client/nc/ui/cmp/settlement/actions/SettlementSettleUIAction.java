package nc.ui.cmp.settlement.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.cmp.utils.CmpUtils;
import nc.cmp.utils.DataUtil;
import nc.cmp.utils.Lists;
import nc.cmp.utils.SettleUtils;
import nc.cmp.utils.UFDoubleUtils;
import nc.funcnode.ui.action.INCAction;
import nc.itf.cmp.pub.CmpSelfDefButtonNameConst;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.ui.cmp.settlement.exception.AccountExceptionHandler;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.cmp.BusiStatus;
import nc.vo.cmp.SettleStatus;
import nc.vo.cmp.SettleType;
import nc.vo.cmp.exception.CmpAuthorizationException;
import nc.vo.cmp.exception.ExceptionHandler;
import nc.vo.cmp.settlement.SettleContext;
import nc.vo.cmp.settlement.SettleEnumCollection.Direction;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.cmp.util.StringUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import java.util.*;

/**
 * 前台结算
 * 
 * @author jiaweib
 * @version 1.0 2011-6-1
 * @since NC6.0
 */
public class SettlementSettleUIAction extends SettleDefaultAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SettlementSettleUIAction() {
		setBtnName(CmpSelfDefButtonNameConst.getInstance().getSettleName());// 手工结算
		putValue(Action.SHORT_DESCRIPTION, getBtnName());
		putValue(INCAction.CODE, "结算");/* -=notranslate=- */
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		validate();
		SettlementAggVO[] selectedAggVOs = getSelectedAggVOs();

		List<SettlementAggVO> agglist = SettleUtils
				.filterSettleInfo4HandSettleFlag4UnSettle(selectedAggVOs);
		if (agglist.size() == 0) {
			ShowStatusBarMsgUtil.showErrorMsg(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"3607set1_0", "03607set1-0078")/* @res "提示" */,
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"3607set1_0", "03607set1-0083")/*
															 * @res
															 * "没有可手工结算的表体记录"
															 */, getModel()
							.getContext());
			return;
		}
		checkState(selectedAggVOs);

		if (selectedAggVOs.length > 1) {
			processListSettle(selectedAggVOs);
		} else {
			processCardSettle(selectedAggVOs[0]);
		}
	}

	/**
	 * 单记录结算
	 * 
	 * @param settlementAggVO
	 * @throws BusinessException
	 */
	private void processCardSettle(SettlementAggVO settlementAggVO)
			throws BusinessException {
		SettlementHeadVO head = (SettlementHeadVO) settlementAggVO
				.getParentVO();
		while (true) {
			try {
				proceccAttribute(settlementAggVO);
				SettleContext context = new SettleContext();
				context.setBeanList(Lists.newArrayList(settlementAggVO));

				context = service.handleSettle(context);
				getEdit().setValue(context.getBeanList().get(0));

				this.getEdit().setLoadBean(context.getBeanList().get(0));
				getModel().directlyUpdate(
						CmpUtils.covertListToArrays(context.getBeanList(),
								SettlementAggVO.class));
				getEdit().loadNotenumber();
				getListView().loadNotenumber();
				break;
			} catch (CmpAuthorizationException exp) {

				head.setSettledate(null);
				head.setPk_executor(null);
				AccountExceptionHandler handler = new AccountExceptionHandler(
						getEdit());
				handler.setSettlementInfo(settlementAggVO);
				boolean b = handler.handleExceptionNew(exp);
				if (!b) {
					break;
				}
			}
		}
		ShowStatusBarMsgUtil.showStatusBarMsg(nc.vo.ml.NCLangRes4VoTransl
				.getNCLangRes().getStrByID("3607set1_0", "03607set1-0075")/*
																		 * @res
																		 * "操作成功"
																		 */,
				getModel().getContext());
		return;
	}

	/**
	 * 多记录结算
	 * 
	 * @param selectedAggVOs
	 * @throws BusinessException
	 */
	private void processListSettle(SettlementAggVO[] selectedAggVOs)
			throws BusinessException {

		try {
			proceccAttribute(selectedAggVOs);
			SettleContext context = new SettleContext();
			context.setBeanList(Lists.newArrayList(selectedAggVOs));

			context = service.handleSettle(context);
			getEdit().setValue(context.getBeanList().get(0));

			this.getEdit().setLoadBean(context.getBeanList().get(0));
			getModel().directlyUpdate(
					CmpUtils.covertListToArrays(context.getBeanList(),
							SettlementAggVO.class));
			if (context.getErrMap() != null) {
				StringBuilder sbMsg = new StringBuilder();
				for (String msgStr : context.getErrMap().keySet()) {
					sbMsg.append(msgStr);
				}
				ShowStatusBarMsgUtil.showErrorMsg(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"3607set_0", "03607set-0022")/* @res "操作成功" */,
						sbMsg.toString(), this.getModel().getContext());
				// throw new BusinessException(sbMsg.toString());

			} else {
				showSettleUIInfo(this.getModel(), selectedAggVOs);
			}
		} catch (CmpAuthorizationException exp) {
			// 采用批量处理，不控制
			throw new BusinessException(exp.getMessage());
		}
	}

	/**
	 * 设置vo属性
	 * 
	 * @param selectedAggVOs
	 * @throws BusinessException
	 */
	private void proceccAttribute(SettlementAggVO... selectedAggVOs)
			throws BusinessException {
		for (SettlementAggVO settlementAggVO : selectedAggVOs) {
			SettlementHeadVO headvo = (SettlementHeadVO) settlementAggVO
					.getParentVO();
			headvo.setSettletype(SettleType.HAND_SETTLE.getStatus());
			// headvo.setSettlestatus(SettleStatus.SUCCESSSETTLE.getStatus());
		}
		SettleUtils.fillSettleDate(DataUtil.getUFDate(), selectedAggVOs);
	}

	private void validate() throws BusinessException {
		checkData();

		SettlementAggVO[] settlementAggVOs = getSelectedAggVOs();

		for (SettlementAggVO settlementAggVO : settlementAggVOs) {
			SettlementHeadVO headvo = (SettlementHeadVO) settlementAggVO
					.getParentVO();
			// 工资发放单据,不能手工结算
			String pk_tradetype = headvo.getPk_tradetype();

			if ("DS".equals(pk_tradetype)) {
				String errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("3607set1_0", "03607set1-0222", null,
								new String[] { headvo.getBillcode() })/*
																	 * @res
																	 * "业务单据号为{0}为工资发放单据,不能手工结算
																	 */;
				ExceptionHandler.cteateandthrowException(errMsg);
			}
			SettlementBodyVO[] bodyvos = (SettlementBodyVO[]) settlementAggVO
					.getChildrenVO();
			for (SettlementBodyVO body : bodyvos) {
				if (body.getPk_Inneraccount() != null) {
					String billcode = ((SettlementHeadVO) settlementAggVO
							.getParentVO()).getBillcode();
					String errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("3607set_0", "03607set-0017")/*
																	 * @res
																	 * "有内部账户的单据，不能手工结算，请重新选择！"
																	 */;
					if (StringUtils.isNotNullWithTrim(billcode)) {
						errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("3607set1_0", "03607set1-0223",
										null,
										new String[] { headvo.getBillcode() })/*
																			 * @res
																			 * "单据号为{0}的单据 有内部账户，不能手工结算，请重新选择！"
																			 */;
					}
					ExceptionHandler.cteateandthrowException(errMsg);
				}
			}
		}
	}

	/**
	 * 检查状态
	 * 
	 * @param selectedAggVOs
	 * @throws BusinessException
	 */
	private void checkState(SettlementAggVO... selectedAggVOs)
			throws BusinessException {
		StringBuffer sbmsg = new StringBuffer();
		for (SettlementAggVO settlementAggVO : selectedAggVOs) {
			SettlementHeadVO headvo = (SettlementHeadVO) settlementAggVO
					.getParentVO();

			Integer busistatus = headvo.getBusistatus() == null ? BusiStatus.Save
					.getBillStatusKind() : headvo.getBusistatus();
			if (!BusiStatus.convertInt2Enum(busistatus).equals(BusiStatus.Sign)) {
				sbmsg.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("3607set_0", "03607set-0894", null,
								new String[] { headvo.getBillcode() })/*
																	 * @res
																	 * "业务单据号为[{0}]不是签字态单据,不能结算\n"
																	 */);
			}
		}
		if (!sbmsg.toString().equals("")) {
			throw new BusinessException(sbmsg.toString());
		}

	}

	@Override
	public boolean isActionEnable() {
		if (UIState.ADD == this.getModel().getUiState()
				|| UIState.EDIT == this.getModel().getUiState()) {
			return false;
		}
		SettlementAggVO[] selectedAggVOs = getSelectedAggVOs();
		if (CmpUtils.isListNull(selectedAggVOs)) {
			return false;
		}

		for (SettlementAggVO settlementAggVO : selectedAggVOs) {
			SettlementHeadVO headvo = (SettlementHeadVO) settlementAggVO
					.getParentVO();
			if (headvo.getPk_ftsbill() != null) {
				// 资金组织生成的结算信息手工结算
				return false;
			}
			if (SettleUtils.isExistInnerAccount(settlementAggVO)) {
				// 有内部账户的不能手工结算
				return false;
			}
			Integer settleStatus = headvo.getSettlestatus();
			settleStatus = settleStatus == null ? SettleStatus.NONESETTLE
					.getStatus() : settleStatus;

			if (headvo.getBusistatus() == null
					|| !headvo.getBusistatus().equals(
							BusiStatus.Sign.getBillStatusKind())) {
				// 非签字态的
				return false;
			}
			if ((settleStatus == SettleStatus.SUCCESSSETTLE.getStatus()
					|| settleStatus == SettleStatus.SUCCESSPART.getStatus()
					|| settleStatus == SettleStatus.PAYING.getStatus()
					|| settleStatus == SettleStatus.CHANGEING.getStatus() || settleStatus == SettleStatus.RECEIVING
					.getStatus()) && headvo.getSigndate() != null) {
				return false;
			}
		}
		return super.isActionEnable();
	}

	// 提示手工结算成功信息
	public static void showSettleUIInfo(AbstractUIAppModel model,
			SettlementAggVO... bills) throws BusinessException {

		Map<String, UFDouble> map = new HashMap<String, UFDouble>();
		int size = 0;
		// 收款 付款 划账 取的金额字段不一样
		for (SettlementAggVO aggvo : bills) {
			size++;
			SettlementBodyVO[] children = (SettlementBodyVO[]) aggvo
					.getChildrenVO();

			for (SettlementBodyVO child : children) {
				String currtype = child.getPk_currtype();
				UFDouble money = child.getPay();

				if (Direction.REC.VALUE.equals(child.getDirection())) {
					money = child.getReceive();
				}
				if (map.containsKey(currtype)) {
					UFDouble sumMoney = map.get(currtype);
					map.put(currtype, UFDoubleUtils.add(sumMoney, money));

				} else {
					map.put(currtype, money);
				}
			}
		}

		if (size > 0) {
			CurrtypeQuery currtypeQuery = CurrtypeQuery.getInstance();
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("3607set_0", "03607set-0895", null,
							new String[] { size + "" })/* @res "结算成功{0}张单据。" */);
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				sBuffer.append("	")
						.append(currtypeQuery.getCurrtypeName((String) entry
								.getKey())).append(": ");
				sBuffer.append(entry.getValue()).append("\n");
			}

			ShowStatusBarMsgUtil.showStatusBarMsg(sBuffer.toString(),
					model.getContext());
		}
	}
}