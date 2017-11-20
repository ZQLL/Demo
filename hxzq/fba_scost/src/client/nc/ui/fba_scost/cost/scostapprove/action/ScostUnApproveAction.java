package nc.ui.fba_scost.cost.scostapprove.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_scost.cost.billcheck.IBillCheckService;
import nc.itf.fba_scost.cost.billcheck.IBillMakeJTD;
import nc.ui.fba_scost.cost.scostapprove.ace.view.ScostapproveTableModel;
import nc.ui.fba_scost.cost.scostapprove.panel.WaitDlg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.BatchModelDataManager;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BatchBillTable;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.scost.approve.ApproveVO;
import nc.vo.pub.BusinessException;

public class ScostUnApproveAction extends NCAction {

	private static final long serialVersionUID = 1L;

	private BatchBillTableModel model = null;
	private BatchBillTable editor = null;
	private WaitDlg waitDlg;
	private CheckParaVO checkParaVO;
	private BatchModelDataManager modelDataManager;

	public BatchModelDataManager getModelDataManager() {
		return modelDataManager;
	}

	public void setModelDataManager(BatchModelDataManager modelDataManager) {
		this.modelDataManager = modelDataManager;
	}

	public ScostUnApproveAction() {

		super();
		setCode("ScostUnApprove");
		setBtnName("�ɱ�����");

	}

	@SuppressWarnings("restriction")
	@Override
	public void doAction(ActionEvent e) throws Exception {
		int rowno = getEditor().getBillCardPanel().getRowCount();
		if (rowno <= 0) {
			MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),
					"��ʾ", "�ڳ�δ���ˣ����ȵ��ڳ����˽ڵ���ˣ�");
			return;
		}
		checkParaVO = new CheckParaVO();
		/**
		 * ǰ̨�����ȡҵ������
		 */
		checkParaVO.setTrade_date(((ScostapproveTableModel) model)
				.getTradedate());
		AppContextUtil.setAppContext(checkParaVO);
		ApproveVO[] vos = (ApproveVO[]) this
				.getEditor()
				.getBillCardPanel()
				.getBillModel()
				.getBodySelectedVOs(
						nc.vo.fba_scost.scost.approve.ApproveVO.class.getName());
		if (vos == null || vos.length <= 0) {
			throw new Exception("�빴ѡҪ��˵�ҵ���飡");
		}

		/**
		 * ���ǰ��̨ts��־ YangJie 2014-04-25
		 */
		if (!PubMethod.getInstance().checkIsAppoveUI(vos)) {
			MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),
					"��ʾ", "ǰ��̨���ݲ�һ�£���ˢ������!");
			return;
		}

		BilltypeGroupVO[] groupvos = new BilltypeGroupVO[vos.length];
		for (int i = 0; i < vos.length; i++) {
			ApproveVO approveVO = vos[i];
			BilltypeGroupVO groupvo = new BilltypeGroupVO();
			groupvo.setPk_billtypegroup(approveVO.getBilltypegroup());
			groupvo.setLastapprovedate(approveVO.getLastapprovedate());// ��¼���һ�����ʱ��
			groupvo.setLasttallydate(approveVO.getLasttallydate());// ��¼���һ�μ���ʱ��
			groupvo.setApproveVO(approveVO);
			groupvos[i] = groupvo;
		}
		checkParaVO.setGroupvos(groupvos);

		waitDlg = new WaitDlg(this.model.getContext().getEntranceUI());
		new Thread() {
			public void run() {
				String temp_e = null;
				String isSuccess = null;
				try {
					//�{�÷��ں�̨���˴������е�����
//					//zq
//					//ͨ��ǰ̨�������ж��Ƿ���ú�̨����ʹ����˵Ľ��뵥�����ļ��ᵥ����ɾ��
//					IBillMakeJTD bmj = NCLocator.getInstance().lookup(IBillMakeJTD.class);
//					if (bmj.getBooleanFromInitcode(checkParaVO.getPk_glorgbook(), "Zqjd02")) {
//						bmj.DeleteJTD(checkParaVO);
//					}
					IBillCheckService ibillcheckservice = NCLocator
							.getInstance().lookup(IBillCheckService.class);
					isSuccess = ibillcheckservice.unCheckBill(checkParaVO);
					modelDataManager.initModelBySqlWhere("  and pk_group ='"
							+ AppContextUtil.getPk_group() + "' and pk_org='"
							+ AppContextUtil.getPk_org()
							+ "' and pk_glorgbook='"
							+ AppContextUtil.getDefaultOrgBook()
							+ "' and islastest='Y' ");
				} catch (BusinessException e) {
					Logger.error("�쳣��" + e.getMessage(), e);
					temp_e = e.getMessage();

				} finally {
					waitDlg.destroy();
					waitDlg = null;
					// if (temp_e != null) {
					// MessageDialog.showErrorDlg(getModel().getContext().getEntranceUI(),
					// "����", temp_e);
					// } else {
					// MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),
					// "��ʾ", "������ϣ�������ο���־����");
					// }
					if (isSuccess != null && isSuccess.equals("success")) {
						if (temp_e != null) {
							MessageDialog.showErrorDlg(getModel().getContext()
									.getEntranceUI(), "����", temp_e);
						} else {
							MessageDialog.showHintDlg(getModel().getContext()
									.getEntranceUI(), "��ʾ", "������ϣ�������ο���־����");
						}
					} else {
						if (temp_e != null) {
							String arr[] = temp_e.split(":");
							int len = arr.length;
							for (int i = 0; i < len - 1; i++) {
								temp_e = arr[i + 1];
							}
							MessageDialog.showErrorDlg(getModel().getContext()
									.getEntranceUI(), "����", "������̳����쳣��"
									+ temp_e);
						} else {
							MessageDialog.showHintDlg(getModel().getContext()
									.getEntranceUI(), "����", "������̳����쳣��");
						}
					}
				}
			}
		}.start();
		waitDlg.showModal();

	}

	public BatchBillTableModel getModel() {
		return model;
	}

	public void setModel(BatchBillTableModel model) {
		this.model = model;
	}

	public BatchBillTable getEditor() {
		return editor;
	}

	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}

}
