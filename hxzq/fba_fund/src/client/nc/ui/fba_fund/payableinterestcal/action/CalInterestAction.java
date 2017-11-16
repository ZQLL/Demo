package nc.ui.fba_fund.payableinterestcal.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_fund.IPayableinterestcalMaintain;
import nc.ui.fba_fund.payableinterestcal.model.CalBillTableModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BatchBillTable;
import nc.vo.fba_fund.payableinterestcal.PayableInterestCalVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

/**
 * Ӧ����Ϣ����
 * 
 * @author qs
 * @since 2017-6-7����6:08:52
 */
public class CalInterestAction extends NCAction {

	private static final long serialVersionUID = 1L;

	private CalBillTableModel model = null;

	private BatchBillTable editor = null;

	public CalInterestAction() {
		super();
		setCode("interestJtAction");
		setBtnName("��   ��");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		LoginContext context = getModel().getContext();
		UFDate trade_date = getModel().getTrade_date();
		if (trade_date == null) {
			throw new BusinessException("��ѡ��Ӧ����Ϣ��������!  ");
		}
		BannerDialog dialog = new BannerDialog(getModel().getContext().getEntranceUI());
		dialog.setName("InterestCal");
		dialog.setTitle("Ӧ����Ϣ����");
		dialog.setEndText("Ӧ����Ϣ���ᣬ��ʱ�����У����Ժ�...");
		dialog.start();
		Thread runThread = new SimulateThread(dialog, context, trade_date);
		runThread.start();
	}

	class SimulateThread extends Thread {

		private BannerDialog dialog;

		private LoginContext context;

		private UFDate trade_date;

		public SimulateThread(BannerDialog dialog, LoginContext context, UFDate trade_date) {
			this.dialog = dialog;
			this.context = context;
			this.trade_date = trade_date;
		}

		public void run() {
			try {
				IPayableinterestcalMaintain interest = NCLocator.getInstance().lookup(IPayableinterestcalMaintain.class);
				// ��Ϣ����
				PayableInterestCalVO[] distillvos = interest.calInterest(context, trade_date);
				if (distillvos != null && distillvos.length > 0) {
					getModel().initModel(null);// ��ս���
					getModel().addLines(distillvos);// ����
					// �������󣺲��ɱ༭
					// getModel().setUiState(UIState.EDIT);
				}
			} catch (Exception e) {
				Logger.info(e);
				MessageDialog.showErrorDlg(getModel().getContext().getEntranceUI(), "����", e.getMessage());
			} finally {
				if (dialog != null)
					this.dialog.end();
			}
		}
	}

	public CalBillTableModel getModel() {
		return model;
	}

	public void setModel(CalBillTableModel model) {
		this.model = model;
	}

	public BatchBillTable getEditor() {
		return editor;
	}

	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}

}
