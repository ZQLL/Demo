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
 * ���� panel
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
	// �������Ƶ��
	private Activity startActivity;
	/** ��ǰPanel��UI���� */
	private Component m_container;
	/** ����ͼ�˵��� */
	private UIMenuItem m_miXpdl = null;

	/** ����ͼ�˵��� */
	private UIMenuItem m_miGraph = null;
	/**
	 * ����ͼҳǩ������ʾ��ʽ��ӳ��� ��ʾ��ʽ��0��ʾ������ʾXPDL;1��ʾ������ʾ����ͼ
	 */
	private Map<Integer, Integer> tabStyleMap = new HashMap<Integer, Integer>();
	
	/**
	 * ����ͼҳǩ����XPDL��ӳ���
	 */
	private Map<Integer, UIEditorPane> tabXpdlMap = new HashMap<Integer, UIEditorPane>();
	/**
	 * ����ͼҳǩ����ͼ�ε�ӳ���
	 */
	private Map<Integer, FlowChart> tabGraphMap = new HashMap<Integer, FlowChart>();
	
	private ActivityInstance[] allActInstances;
	
	/** ������״̬ */
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

			// �ҵ�WFTask���������̶��弰�丸���̶���
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
																														 * "����"
																														 */, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000494")/*
																																																	 * ��ѯ���ݵ�����ͼ�����쳣
																																																	 * ��
																																																	 */
						+ e.getMessage());
			} catch (XPDLParserException e) {
				Logger.error(e.getMessage());
			}

			if (processRoute == null || processRoute.getXpdlString() == null)
				// WARN::˵���õ��ݾ�û������ʵ��
				return m_tabbedPane;

			// ��ȡ������״̬
//			 m_iMainFlowStatus = processRoute.getProcStatus();

			// ����һ����ʱ��
			XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
			pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");

			// ��������ͼҳǩ
			constructGraphTab(processRoute, m_tabbedPane, pkg);

			addTabListener();
		}
		return m_tabbedPane;
	}
	
	private void addTabListener() {
		constructGraph().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// XXX:����ͼ��XPDL֮���л�
				if (((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)) {
					// ������Ҽ����,�򵯳��˵�
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
	 * ��������ͼҳǩ
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
			// ǰ̨����XML��Ϊ����
			wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
		} catch (XPDLParserException e) {
			Logger.error(e.getMessage(), e);
			return;
		}
		wp.setPackage(pkg);

		// ��ʼ��Graph
		final FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
		// ���ù�����ʾ
		ToolTipManager.sharedInstance().registerComponent(auditChart);
		// auditChart.setEnabled(false);
		auditChart.populateByWorkflowProcess(wp, false);
		// auditChart.setBorder(BorderFactory.createEtchedBorder());
		//ѭ������ʵ����������ֱ�Ӹ�ֵ�������ڱ��������̵�ʱ���������̵Ļʵ�����
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

		// ��ǰ�����еĻ
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

		//////////////////////////////////nameMapû�б��õ�����
		HashMap<String, String> nameMap = new HashMap<String, String>();
		Object[] cells = auditChart.getRoots();
		// add by lihaibo ��ֵ
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
				//����
				
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
			currentRoute = subRoutes[i]; // ȡ�����̣�����ѭ��
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
																																	 * ���ɲ��ص���ǰ���ڵĺ�������
																																	 * ��
																																	 */);
		}
		return isValidity;
	}

	private UIPopupMenu getFlowPopup() {
		// �������û���
		if (m_popMenuFlow == null) {
			m_popMenuFlow = new UIPopupMenu();
			rejectMenuItem = new UIMenuItem(NCLangRes.getInstance().getStrByID("pfgraph", "ApproveFlowRejectPanel-000000")/* �������û��� */);
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
																																											 * ���ص�
																																											 * ��
																																											 * {
																																											 * 0
																																											 * }
																																											 */);
		}else if (evtSource == getMiXpdl()) {
			int iSelectedIndex = constructGraph().getSelectedIndex();
			// XXX:��ʾ��ǰ����ͼ��XPDL����
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
			// XXX:��ʾ��ǰXPDL���������ͼ
			tabStyleMap.put(iSelectedIndex, 1);
			UIScrollPane graphScrollPane = (UIScrollPane) constructGraph()
					.getSelectedComponent();
			graphScrollPane.setViewportView((FlowChart) tabGraphMap
					.get(iSelectedIndex));
		}
	}

	/**
	 * ���ܲ��ص���ǩ����,���ɲ��ظ�NCϵͳ�û�
	 * */
	private boolean checkValidation(Activity activity) {
		if (activity instanceof GenericActivityEx) {
			if (((GenericActivityEx) activity).getOrganizeTransferObj().getOrgUnitPK().equals(INCSystemUserConst.NC_USER_PK)) {
				MessageDialog.showHintDlg(ApproveFlowRejectPanel.this, null, NCLangRes.getInstance().getStrByID("pfgraph", "ApproveFlowRejectPanel-000003")/*
																																							 * ���ɲ��ص�NC�û�
																																							 * ��
																																							 * ������ѡ����������
																																							 * ��
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
