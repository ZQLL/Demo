package nc.ui.pub.workflownote;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.WindowConstants;

import nc.ui.ml.NCLangRes;
import nc.ui.pf.workitem.ApproveFlowRejectPanel;
import nc.ui.pf.workitem.ApproveWorkitemAcceptDlg;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.component.ButtonPanel;
import nc.ui.wfengine.designer.OperatingSystem;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nc.vo.workflow.admin.WorkflowManageContext;

public class FlowStateRefDlg extends UIDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UIPanel bottomPanel = null;
	private ButtonPanel btnPanel = null;
	private WorkflownoteVO worknoteVO = null;
	private boolean isInWorkflow = false;
	
	public FlowStateRefDlg(Container parent,WorkflownoteVO worknoteVO,boolean isInWorkflow) {
		this.worknoteVO = worknoteVO;
		this.isInWorkflow = isInWorkflow;
		
		initialize();
		initUI();
		initEventListener();
//		setLocationRelativeTo(parent);
//		setSize(1000, 600);
//		setBounds(150, 100, 1000, 700);
	}
	
	private void initialize(){
		setName("FlowStateRefDlg");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(true);
		Rectangle screenRect = OperatingSystem.getScreenBounds();
		//setSize(720, 400);
		setBounds(150,100,screenRect.width - 300, screenRect.height - 300);
		setTitle(isInWorkflow ? NCLangRes.getInstance().getStrByID("102220", "UPP102220-000216")/*工作流处理情况*/ : NCLangRes.getInstance().getStrByID("102220", "UPP102220-000217")/*审批流处理情况*/);
	}
	
	private void initUI(){
//		this.add(super.getUIDialogContentPane(),BorderLayout.CENTER);
		this.add(getPnTopTitle(), BorderLayout.NORTH);//ApproveFlowRejectPanelTop
//		this.add(getPnTitle(), BorderLayout.NORTH);//ApproveFlowRejectPanelTop
		this.add(getSpCenter(),BorderLayout.CENTER);
		this.add(getBottomPanel(),BorderLayout.SOUTH);
	}
	
	ApproveFlowRejectPanelTop topPanel;
	public ApproveFlowRejectPanelTop getPnTopTitle(){
		if(topPanel == null){
			topPanel = new ApproveFlowRejectPanelTop(this,getWorkflowManageContext());
		}
		return topPanel;
	}
	
	WorkflowManageContext context;
	protected WorkflowManageContext getWorkflowManageContext() {
		if(context == null){
			context = new WorkflowManageContext();
			
			context.setBillType(worknoteVO.getPk_billtype());//m_strBillType
			context.setBillId(worknoteVO.getBillid());//m_strBillID
			context.setBillNo("");
			context.setFlowType(worknoteVO.getWorkflow_type());//m_iWorkflowtype
			
		}
		return context;
	}
	
	private UISplitPane spCenter = null;
	public UISplitPane getSpCenter() {
		if (spCenter == null) {
			spCenter = new UISplitPane(UISplitPane.VERTICAL_SPLIT);
			spCenter.add(getPanelRejectFlow(), UISplitPane.TOP);
			spCenter.add(getPanelRejectTable(), UISplitPane.BOTTOM);
			spCenter.setDividerLocation(300);
		}
		return spCenter;
	}
	
	ApproveFlowRejectPanel rejectPanel;

	public ApproveFlowRejectPanel getPanelRejectFlow() {
		if (rejectPanel == null) {
			String processInstancePK = null;
			if (isInWorkflow) {
				processInstancePK = worknoteVO.getTaskInfo().getTask()
						.getWfProcessInstancePK();
			}
			rejectPanel = new ApproveFlowRejectPanel(this,worknoteVO.getBillVersionPK(),
					worknoteVO.getPk_billtype(), processInstancePK,
					WorkflowTypeEnum.Approveflow.getIntValue());
		}
		ApproveWorkitemAcceptDlg.rejectPanel = rejectPanel;
		return rejectPanel;
	}
	
	ApproveFlowRejectPanelTable rejectTable;
	public ApproveFlowRejectPanelTable getPanelRejectTable(){
		if(rejectTable == null){
			rejectTable = new ApproveFlowRejectPanelTable(this,getWorkflowManageContext());
		}
		return rejectTable;
	}
	
	private UIPanel getBottomPanel(){
		if(bottomPanel == null){
			bottomPanel = new UIPanel();
			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(getButtonPanel());
		}
		return bottomPanel;
	}
	
	private ButtonPanel getButtonPanel() {
		if (btnPanel == null) {
			btnPanel = new ButtonPanel();
			btnPanel.setBorder(BorderFactory.createEmptyBorder());
			btnPanel.setOpaque(false);
			btnPanel.getBtnOK().setPreferredSize(new Dimension(70, 25));
			btnPanel.getBtnCancel().setPreferredSize(new Dimension(70, 25));
		}
		return btnPanel;
	}

	private void initEventListener() {
		btnPanel.getBtnOK().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeOK();
			}
		});

		btnPanel.getBtnCancel().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeCancel();
			}
		});
	}
	
	public Activity getSelectActivity(){
		return getPanelRejectFlow().getSelectedActivity();
	}
	
	
	public void onShow(boolean isShow){
		if (isShow) {
			getSpCenter().add(getPanelRejectFlow(), UISplitPane.TOP);
			getSpCenter().setDividerLocation(300);
		} else{
			getSpCenter().remove(getPanelRejectFlow());
		}
	}
}