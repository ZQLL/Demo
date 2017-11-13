package nc.ui.pf.workitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JViewport;
import javax.swing.ToolTipManager;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.wfengine.engine.ActivityInstance;
import nc.itf.uap.pf.IWorkflowDefine;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIEditorPane;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.workflownote.FlowStateRefDlg;
import nc.ui.wfengine.designer.ProcessGraph;
import nc.ui.wfengine.flowchart.FlowChart;
import nc.ui.wfengine.flowchart.UfWGraphModel;
import nc.ui.wfengine.flowchart.graph.ActivityCell;
import nc.vo.pub.BusinessException;
import nc.vo.uap.rbac.constant.INCSystemUserConst;
import nc.vo.uap.wfmonitor.ProcessRouteRes;
import nc.vo.wfengine.core.XpdlPackage;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.core.activity.GenericActivityEx;
import nc.vo.wfengine.core.parser.UfXPDLParser;
import nc.vo.wfengine.core.parser.XPDLNames;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.serializer.UfXPDLSerializer;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.pub.WfTaskOrInstanceStatus;

/**
 * 驳回 panel
 * 
 * @author dingxm
 * 
 */
public class ApproveFlowRejectPanel extends UIPanel implements ActionListener {

	private static final long serialVersionUID = -8366889170078440416L;

	private UITabbedPane m_tabbedPane;

	String m_strBillID;

	String m_strBillType;

	String m_strProcessInstance;

	int m_iWorkflowtype;

	public static Activity selectedActivity;

	private UIPopupMenu m_popMenuFlow;

	private UIMenuItem rejectMenuItem;

	private Activity rejectToActivity;

	private UILabel descriptionlabel;
	// 主流程制单活动
	private Activity startActivity;
	/** 当前Panel的UI容器 */
	private Component m_container;
	/** 流程图菜单项 */
	private UIMenuItem m_miXpdl = null;

	/** 流程图菜单项 */
	private UIMenuItem m_miGraph = null;
	/**
	 * 流程图页签与其显示样式的映射表。 显示样式：0表示正在显示XPDL;1表示正在显示流程图
	 */
	private Map<Integer, Integer> tabStyleMap = new HashMap<Integer, Integer>();
	
	/**
	 * 流程图页签与其XPDL的映射表。
	 */
	private Map<Integer, UIEditorPane> tabXpdlMap = new HashMap<Integer, UIEditorPane>();
	/**
	 * 流程图页签与其图形的映射表。
	 */
	private Map<Integer, FlowChart> tabGraphMap = new HashMap<Integer, FlowChart>();
	
	private ActivityInstance[] allActInstances;
	
	/** 主流程状态 */
//	private int m_iMainFlowStatus = -1;
	
	public ApproveFlowRejectPanel(Component container,String m_strBillID, String m_strBillType, String m_strProcessInstance, int m_iWorkflowtype) {
		m_container = container;
		setM_container(m_container);
		this.m_strBillID = m_strBillID;
		this.m_strBillType = m_strBillType;
		this.m_strProcessInstance = m_strProcessInstance;
		this.m_iWorkflowtype = m_iWorkflowtype;
		initUI();
	}
	
	@Deprecated
	public ApproveFlowRejectPanel(String m_strBillID, String m_strBillType, String m_strProcessInstance, int m_iWorkflowtype) {
		this.m_strBillID = m_strBillID;
		this.m_strBillType = m_strBillType;
		this.m_strProcessInstance = m_strProcessInstance;
		this.m_iWorkflowtype = m_iWorkflowtype;
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());
		add(constructGraph(), BorderLayout.CENTER);
		add(getDescriptionLabel(), BorderLayout.SOUTH);
	}

	private UILabel getDescriptionLabel() {
		if (descriptionlabel == null) {
			descriptionlabel = new UILabel();
		}
		return descriptionlabel;
	}

	private UITabbedPane constructGraph() {
		if (m_tabbedPane == null) {
			m_tabbedPane = new UITabbedPane();

			// 找到WFTask所属的流程定义及其父流程定义
			ProcessRouteRes processRoute = null;
			IWorkflowDefine wfDefine = (IWorkflowDefine) NCLocator.getInstance().lookup(IWorkflowDefine.class.getName());
			try {
				processRoute = wfDefine.queryProcessRoute(m_strBillID, m_strBillType, m_strProcessInstance, m_iWorkflowtype);
				startActivity = PfDataCache.getWorkflowProcess(processRoute.getProcessDefPK()).findStartActivity();
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000237")/*
																														 * @
																														 * res
																														 * "错误"
																														 */, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000494")/*
																																																	 * 查询单据的流程图出现异常
																																																	 * ：
																																																	 */
						+ e.getMessage());
			} catch (XPDLParserException e) {
				Logger.error(e.getMessage());
			}

			if (processRoute == null || processRoute.getXpdlString() == null)
				// WARN::说明该单据就没有流程实例
				return m_tabbedPane;

			// 获取主流程状态
//			 m_iMainFlowStatus = processRoute.getProcStatus();

			// 构造一个临时包
			XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
			pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");

			// 构造流程图页签
			constructGraphTab(processRoute, m_tabbedPane, pkg);

			addTabListener();
		}
		return m_tabbedPane;
	}
	
	private void addTabListener() {
		constructGraph().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// XXX:流程图和XPDL之间切换
				if (((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)) {
					// 如果是右键点击,则弹出菜单
					UIPopupMenu menu = new UIPopupMenu();

					int index = constructGraph().getSelectedIndex();
					Object obj = tabStyleMap.get(index);

					if (obj == null || ((Integer) obj) == 1)
						menu.add(getMiXpdl());
					else
						menu.add(getMiGraph());
					menu.show(constructGraph(), e.getX(), e.getY());
				}
			}
		});
	}

	/**
	 * 构造流程图页签
	 * 
	 * @param lhm
	 * @return
	 */
	private void constructGraphTab(ProcessRouteRes processRoute, UITabbedPane tabPane, XpdlPackage pkg) {
		String def_xpdl = null;
		ProcessRouteRes currentRoute = processRoute;
		if (currentRoute.getXpdlString() != null)
			def_xpdl = currentRoute.getXpdlString().toString();

		WorkflowProcess wp = null;
		try {
			// 前台解析XML串为对象
			wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
		} catch (XPDLParserException e) {
			Logger.error(e.getMessage(), e);
			return;
		}
		wp.setPackage(pkg);

		// 初始化Graph
		final FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
		// 启用工具提示
		ToolTipManager.sharedInstance().registerComponent(auditChart);
		// auditChart.setEnabled(false);
		auditChart.populateByWorkflowProcess(wp, false);
		// auditChart.setBorder(BorderFactory.createEtchedBorder());
		//循环加入活动实例，而不是直接赋值，这样在遍历子流程的时候会把主流程的活动实例清空
		List<ActivityInstance> instanceList = new ArrayList<ActivityInstance>();
		if(allActInstances != null){
			for(ActivityInstance instance : allActInstances){
				instanceList.add(instance);
			}
		}
		for(ActivityInstance instance : currentRoute.getActivityInstance()){
			instanceList.add(instance);
		}
		allActInstances = instanceList.toArray(new ActivityInstance[0]);
		String[] startedActivityDefIds = new String[allActInstances.length];

		// 当前正运行的活动
		HashSet hsRunningActs = new HashSet();
		for (int i = 0; i < allActInstances.length; i++) {
			startedActivityDefIds[i] = allActInstances[i].getActivityID();
			if (allActInstances[i].getStatus() == WfTaskOrInstanceStatus.Started.getIntValue())
				hsRunningActs.add(startedActivityDefIds[i]);
		}
		auditChart.setActivityRouteHighView(hsRunningActs, startedActivityDefIds, currentRoute.getActivityRelations(), Color.RED, Color.BLUE);

		UIScrollPane graphScrollPane = new UIScrollPane(auditChart);
		JViewport vport = graphScrollPane.getViewport();
		vport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

		//////////////////////////////////nameMap没有被用到啊！
		HashMap<String, String> nameMap = new HashMap<String, String>();
		Object[] cells = auditChart.getRoots();
		// add by lihaibo 赋值
		ApproveWorkitemAcceptDlg.cells = cells;
		ApproveWorkitemAcceptDlg.allActInstances = allActInstances;
		
		for (Object c : cells) {
			if (c instanceof ActivityCell) {
				Object o = ((ActivityCell) c).getUserObject();
				if (o instanceof Activity) {
					nameMap.put(((Activity) o).getId(), ((Activity) o).getName());
				}
			}
		}

		auditChart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					Object[] cells = auditChart.getRoots();
					for (Object cell : cells) {
						if ((cell instanceof ActivityCell) && auditChart.getCellBounds(cell).contains(e.getPoint())) {
							Object o = ((ActivityCell) cell).getUserObject();
							if (o instanceof Activity) {//
								if (checkRejectValidity(allActInstances, ((Activity) o).getId())) {
									selectedActivity = ((Activity) o);
									Component srcComp = (Component) e.getSource();
									getFlowPopup().show(srcComp, e.getX(), e.getY());
								}

							}
							break;
						}
					}
				}
				//后增
				
				else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
					Object[] cells = auditChart.getRoots();
					boolean selected = false;
					for (Object cell : cells) {
						if ((cell instanceof ActivityCell) && auditChart.getCellBounds(cell).contains(e.getPoint())) {
							Object o = ((ActivityCell) cell).getUserObject();
							if (o instanceof Activity) {//
								if (checkRejectValidity(allActInstances, ((Activity) o).getId())){
								selectedActivity =(Activity) o;
								((FlowStateRefDlg)m_container).getPanelRejectTable().onActivitySelected(((Activity) o).getId());
								selected = true;
								}
							}
							break;
						}
					}
					
					if (!selected) {
						((FlowStateRefDlg)m_container).getPanelRejectTable().onActivitySelected(null);
					}
				}
			}
		});

		String title = auditChart.getWorkflowProcess().getName() + " " + processRoute.getProcessDefVersion();

		tabPane.addTab(title, graphScrollPane);
		tabGraphMap.put(tabPane.getTabCount() - 1, auditChart);
		ProcessRouteRes[] subRoutes = processRoute.getSubProcessRoute();
		for (int i = 0; i < (subRoutes == null ? 0 : subRoutes.length); i++) {
			currentRoute = subRoutes[i]; // 取子流程，继续循环
			constructGraphTab(currentRoute, tabPane, pkg);
		}
	}

	private boolean checkRejectValidity(ActivityInstance[] allActInstances, String actid) {
		ArrayList<String> finishActId = new ArrayList<String>();
		for (int start = 0, end = allActInstances == null ? 0 : allActInstances.length; start < end; start++) {
			if (allActInstances[start].getStatus() == WfTaskOrInstanceStatus.Finished.getIntValue()) {
				finishActId.add(allActInstances[start].getActivityID());
			}
		}
		boolean isValidity = finishActId.contains(actid);
		if (!isValidity) {
			MessageDialog.showWarningDlg(null, null, NCLangRes.getInstance().getStrByID("pfworkflow", "ApproveFlowRejectPanel-000000")/*
																																	 * 不可驳回到当前环节的后续环节
																																	 * ！
																																	 */);
		}
		return isValidity;
	}

	private UIPopupMenu getFlowPopup() {
		// 驳回至该环节
		if (m_popMenuFlow == null) {
			m_popMenuFlow = new UIPopupMenu();
			rejectMenuItem = new UIMenuItem(NCLangRes.getInstance().getStrByID("pfgraph", "ApproveFlowRejectPanel-000000")/* 驳回至该环节 */);
			rejectMenuItem.addActionListener(this);
			m_popMenuFlow.add(rejectMenuItem);

		}
		return m_popMenuFlow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object evtSource = e.getSource();
		if (evtSource == rejectMenuItem) {
			if (!checkValidation(selectedActivity)) {
				return;
			}
			rejectToActivity = selectedActivity;
			getDescriptionLabel().setText(NCLangRes.getInstance().getStrByID("pfgraph", "ApproveFlowRejectPanel-000002", null, new String[] { rejectToActivity.getName() })/*
																																											 * 驳回到
																																											 * ：
																																											 * {
																																											 * 0
																																											 * }
																																											 */);
		}else if (evtSource == getMiXpdl()) {
			int iSelectedIndex = constructGraph().getSelectedIndex();
			// XXX:显示当前流程图的XPDL定义
			tabStyleMap.put(iSelectedIndex, 0);
			UIScrollPane graphScrollPane = (UIScrollPane) constructGraph()
					.getSelectedComponent();
			if (tabXpdlMap.get(iSelectedIndex) == null) {
				FlowChart flowChart = (FlowChart) graphScrollPane.getViewport()
						.getView();
				String strXpdl = "Error occured,please see log!";
				try {
					strXpdl = UfXPDLSerializer.getInstance().serialize(
							flowChart.getWorkflowProcess());
				} catch (Exception ex) {
					Logger.error(ex.getMessage(), ex);
					strXpdl += "----------------------------\n"
							+ ex.getMessage();
				}
				UIEditorPane ep = new UIEditorPane();
				ep.setEditable(false);
				ep.setText(strXpdl);
				tabXpdlMap.put(iSelectedIndex, ep);
			}
			graphScrollPane.setViewportView((UIEditorPane) tabXpdlMap
					.get(iSelectedIndex));
		} else if (evtSource == getMiGraph()) {
			int iSelectedIndex = constructGraph().getSelectedIndex();
			// XXX:显示当前XPDL定义的流程图
			tabStyleMap.put(iSelectedIndex, 1);
			UIScrollPane graphScrollPane = (UIScrollPane) constructGraph()
					.getSelectedComponent();
			graphScrollPane.setViewportView((FlowChart) tabGraphMap
					.get(iSelectedIndex));
		}
	}

	/**
	 * 不能驳回到加签环节,不可驳回给NC系统用户
	 * */
	private boolean checkValidation(Activity activity) {
		if (activity instanceof GenericActivityEx) {
			if (((GenericActivityEx) activity).getOrganizeTransferObj().getOrgUnitPK().equals(INCSystemUserConst.NC_USER_PK)) {
				MessageDialog.showHintDlg(ApproveFlowRejectPanel.this, null, NCLangRes.getInstance().getStrByID("pfgraph", "ApproveFlowRejectPanel-000003")/*
																																							 * 不可驳回到NC用户
																																							 * ，
																																							 * 请重新选择其它环节
																																							 * ！
																																							 */);
				return false;
			}
		}
		return true;
	}

	public Activity getRejectToActivity() {
		if(rejectToActivity==null){
			rejectToActivity=startActivity;
		}
		return rejectToActivity;
	}
	
	/////////////////////////
	public Activity getSelectedActivity() {
		return selectedActivity;
	}
	
	public ActivityInstance[] getAllActInstances(){
		return allActInstances;
	}
	
	private UIMenuItem getMiXpdl() {
		if (m_miXpdl == null) {
			m_miXpdl = new UIMenuItem("Xpdl");
			m_miXpdl.addActionListener(this);
		}
		return m_miXpdl;
	}

	private UIMenuItem getMiGraph() {
		if (m_miGraph == null) {
			m_miGraph = new UIMenuItem("Graph");
			m_miGraph.addActionListener(this);
		}
		return m_miGraph;
	}

	public Component getM_container() {
		return m_container;
	}

	public void setM_container(Component m_container) {
		this.m_container = m_container;
	}

//	public int getMainFlowStatus() {
//		return m_iMainFlowStatus;
//	}

}
