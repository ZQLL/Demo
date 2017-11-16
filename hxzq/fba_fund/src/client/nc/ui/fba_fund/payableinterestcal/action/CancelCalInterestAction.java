package nc.ui.fba_fund.payableinterestcal.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.fba_fund.IPayableinterestcalMaintain;
import nc.ui.fba_fund.payableinterestcal.model.CalBillTableModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BatchBillTable;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

/**
 * 取消应付利息计提
 * 
 * @author qs
 * @since 2017-6-9上午9:42:56
 */
public class CancelCalInterestAction extends NCAction {

	private static final long serialVersionUID = 1L;

	private CalBillTableModel model = null;

	private BatchBillTable editor = null;

	public CancelCalInterestAction() {
		super();
		setCode("cancelCalInterestAction");
		setBtnName("取消计提");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		LoginContext context = getModel().getContext();
		UFDate trade_date = getModel().getTrade_date();
		IPayableinterestcalMaintain interest = NCLocator.getInstance().lookup(IPayableinterestcalMaintain.class);
		// 取消计提
		interest.cancelCal(context, trade_date);
		getModel().initModel(null);// 清空界面
		MessageDialog.showHintDlg(this.getModel().getContext().getEntranceUI(), "提示", "取消计提完成！");
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
