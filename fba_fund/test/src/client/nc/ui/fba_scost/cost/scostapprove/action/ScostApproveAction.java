package nc.ui.fba_scost.cost.scostapprove.action;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.KeyStroke;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_scost.cost.billcheck.IBillCheckService;
import nc.ui.fba_scost.cost.scostapprove.ace.view.ScostapproveTableModel;
import nc.ui.fba_scost.cost.scostapprove.panel.WaitDlg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.model.BatchModelDataManager;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BatchBillTable;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.scost.approve.ApproveVO;
import nc.vo.fba_sec.pub.BilltypeDetailsVO;
import nc.vo.fba_sec.pub.SecSysInitCache;
import nc.vo.pub.BusinessException;

public class ScostApproveAction extends NCAction {

	private static final long serialVersionUID = 1L;

	private BatchBillTableModel model = null;
	private BatchBillTable editor = null;
	private BatchModelDataManager modelDataManager;
	private WaitDlg waitDlg;

	private CheckParaVO checkParaVO;

	public ScostApproveAction() {

		super();
		setCode("ScostApprove");
		setBtnName("�ɱ����");
		putValue(Action.ACCELERATOR_KEY, getKeyStroke('L')); // ��ݼ�
		putValue(Action.SMALL_ICON, null); // Сͼ��

	}

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

		// 2015��6��23�� �����óɱ���������������о��棡
		SecSysInitCache.getInstance().clearCheckPlanDetails(
				checkParaVO.getPk_group(), checkParaVO.getPk_org(),
				checkParaVO.getPk_glorgbook()); // ������Bug����Ҫ����ʹ��
		Map<String, HashMap<String, BilltypeDetailsVO>> map = SecSysInitCache
				.getInstance().getCheckPlanDetails(checkParaVO.getPk_group(),
						checkParaVO.getPk_org(), checkParaVO.getPk_glorgbook());
		if (map != null && map.size() > 0) {
			Collection<HashMap<String, BilltypeDetailsVO>> colls = map.values();
			for (HashMap<String, BilltypeDetailsVO> hashMap : colls) {
				if (!hashMap.containsKey("HV9C-Cxx-01")) {
					String msg = "������ɱ�������δ���ã��������������������ݲ�һ�£��Ƿ������";
					Logger.warn(msg, this.getClass(), "doAction()");
					int res = MessageDialog.showOkCancelDlg(null, "����", msg);
					if (res != UIDialog.ID_OK) {
						return;
					}
				}
			}
		}
		// 2015��6��23��
		waitDlg = new WaitDlg(this.model.getContext().getEntranceUI());
		new Thread() {
			@Override
			public void run() {
				String isSuccess = null;
				String temp_e = null;
				try {
					IBillCheckService ibillcheckservice = NCLocator
							.getInstance().lookup(IBillCheckService.class);
					isSuccess = ibillcheckservice.checkBill(checkParaVO);
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
					// "��ʾ", "�����ϣ�������ο���־����");
					// }
					if (isSuccess != null && isSuccess.equals("success")) {
						if (temp_e != null) {
							MessageDialog.showErrorDlg(getModel().getContext()
									.getEntranceUI(), "����", temp_e);
						} else {
							// ���÷��ں�̨�����ﲻ����
							// //ͨ��ǰ̨�������ж��Ƿ���ú�̨����ʹ����˵Ľ��뵥�������ᵥ--zq
							// IBillMakeJTD bmj =
							// NCLocator.getInstance().lookup(
							// IBillMakeJTD.class);
							// try {
							// if
							// (bmj.getBooleanFromInitcode(checkParaVO.getPk_glorgbook(),
							// "Zqjd02")) {
							// bmj.MakeJTD(checkParaVO);
							// }
							// } catch (BusinessException e) {
							// // TODO �Զ����ɵ� catch ��
							// e.printStackTrace();
							// }
							MessageDialog.showHintDlg(getModel().getContext()
									.getEntranceUI(), "��ʾ", "�����ϣ�������ο���־����");
						}
					} else {
						if (temp_e != null) {
							String arr[] = temp_e.split(":");
							int len = arr.length;
							for (int i = 0; i < len - 1; i++) {
								temp_e = arr[i + 1];
							}
							MessageDialog.showErrorDlg(getModel().getContext()
									.getEntranceUI(), "����", "��˹��̳����쳣��"
									+ temp_e);
						} else {
							MessageDialog.showErrorDlg(getModel().getContext()
									.getEntranceUI(), "����", "��˹��̳����쳣��");
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

	private KeyStroke getKeyStroke(char ch) {
		return KeyStroke.getKeyStroke((int) ch, Event.CTRL_MASK);
	}

	private KeyStroke getKeyStroke(char ch, int mask) {
		return KeyStroke.getKeyStroke((int) ch, mask);
	}

	public BatchBillTable getEditor() {
		return editor;
	}

	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}

	public BatchModelDataManager getModelDataManager() {
		return modelDataManager;
	}

	public void setModelDataManager(BatchModelDataManager modelDataManager) {
		this.modelDataManager = modelDataManager;
	}

}
