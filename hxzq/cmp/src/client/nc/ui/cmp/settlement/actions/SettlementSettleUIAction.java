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
 * ǰ̨����
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
		setBtnName(CmpSelfDefButtonNameConst.getInstance().getSettleName());// �ֹ�����
		putValue(Action.SHORT_DESCRIPTION, getBtnName());
		putValue(INCAction.CODE, "����");/* -=notranslate=- */
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
							"3607set1_0", "03607set1-0078")/* @res "��ʾ" */,
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"3607set1_0", "03607set1-0083")/*
															 * @res
															 * "û�п��ֹ�����ı����¼"
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
	 * ����¼����
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
																		 * "�����ɹ�"
																		 */,
				getModel().getContext());
		return;
	}

	/**
	 * ���¼����
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
								"3607set_0", "03607set-0022")/* @res "�����ɹ�" */,
						sbMsg.toString(), this.getModel().getContext());
				// throw new BusinessException(sbMsg.toString());

			} else {
				showSettleUIInfo(this.getModel(), selectedAggVOs);
			}
		} catch (CmpAuthorizationException exp) {
			// ������������������
			throw new BusinessException(exp.getMessage());
		}
	}

	/**
	 * ����vo����
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
			// ���ʷ��ŵ���,�����ֹ�����
			String pk_tradetype = headvo.getPk_tradetype();

			if ("DS".equals(pk_tradetype)) {
				String errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("3607set1_0", "03607set1-0222", null,
								new String[] { headvo.getBillcode() })/*
																	 * @res
																	 * "ҵ�񵥾ݺ�Ϊ{0}Ϊ���ʷ��ŵ���,�����ֹ�����
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
																	 * "���ڲ��˻��ĵ��ݣ������ֹ����㣬������ѡ��"
																	 */;
					if (StringUtils.isNotNullWithTrim(billcode)) {
						errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("3607set1_0", "03607set1-0223",
										null,
										new String[] { headvo.getBillcode() })/*
																			 * @res
																			 * "���ݺ�Ϊ{0}�ĵ��� ���ڲ��˻��������ֹ����㣬������ѡ��"
																			 */;
					}
					ExceptionHandler.cteateandthrowException(errMsg);
				}
			}
		}
	}

	/**
	 * ���״̬
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
																	 * "ҵ�񵥾ݺ�Ϊ[{0}]����ǩ��̬����,���ܽ���\n"
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
				// �ʽ���֯���ɵĽ�����Ϣ�ֹ�����
				return false;
			}
			if (SettleUtils.isExistInnerAccount(settlementAggVO)) {
				// ���ڲ��˻��Ĳ����ֹ�����
				return false;
			}
			Integer settleStatus = headvo.getSettlestatus();
			settleStatus = settleStatus == null ? SettleStatus.NONESETTLE
					.getStatus() : settleStatus;

			if (headvo.getBusistatus() == null
					|| !headvo.getBusistatus().equals(
							BusiStatus.Sign.getBillStatusKind())) {
				// ��ǩ��̬��
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

	// ��ʾ�ֹ�����ɹ���Ϣ
	public static void showSettleUIInfo(AbstractUIAppModel model,
			SettlementAggVO... bills) throws BusinessException {

		Map<String, UFDouble> map = new HashMap<String, UFDouble>();
		int size = 0;
		// �տ� ���� ���� ȡ�Ľ���ֶβ�һ��
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
							new String[] { size + "" })/* @res "����ɹ�{0}�ŵ��ݡ�" */);
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