package nc.ui.fba_scost.cost.jiti.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_scost.cost.interest.InterestdistillMaintain;
import nc.ui.fba_scost.cost.interestdistill.model.InterestTableModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BatchBillTable;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

@SuppressWarnings("restriction")
public class JTAction extends NCAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5495624610887668337L;
	
	private InterestTableModel model = null;
	
	private BatchBillTable editor = null;

	public JTAction() {
		super();
		setCode("interestJtAction");
		setBtnName("计   提");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		LoginContext context = getModel().getContext();
		UFDate trade_date = getModel().getTrade_date();
		if(trade_date == null){
			throw new BusinessException("请选择应收利息计提日期!  ");
		}
		BannerDialog dialog = new BannerDialog(getModel().getContext().getEntranceUI());
		dialog.setName("InterestJt");
		dialog.setTitle("应收利息计提");
		dialog.setEndText("应收利息计提，耗时操作中，请稍候...");
		dialog.start();
		Thread runThread = new SimulateThread(dialog,context,trade_date);
		runThread.start();
	}
	
	class SimulateThread extends Thread{
		
		private BannerDialog dialog;
		
		private LoginContext context;
		
		private UFDate trade_date;

		public SimulateThread(BannerDialog dialog,LoginContext context,UFDate trade_date) {
			this.dialog = dialog;
			this.context = context;
			this.trade_date = trade_date;
		}
		
		public void run(){
			try {
				InterestdistillMaintain interest = NCLocator.getInstance().lookup(InterestdistillMaintain.class);
				InterestdistillVO[] distillvos =  interest.distill(context, trade_date);
				if(distillvos != null && distillvos.length > 0){
//					getModel().initModel(distillvos);
					getModel().initModel(null);//清空界面
					getModel().addLines(distillvos);//增行
					getModel().setUiState(UIState.EDIT);
				}
			}catch(Exception e){
				Logger.info(e);
				MessageDialog.showErrorDlg(getModel().getContext().getEntranceUI(), "错误", e.getMessage());
			}finally{
				if(dialog != null)
					this.dialog.end();
			}
		}
	}
	
	public InterestTableModel getModel() {
		return model;
	}

	public void setModel(InterestTableModel model) {
		this.model = model;
	}

	public BatchBillTable getEditor() {
		return editor;
	}

	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}
}
