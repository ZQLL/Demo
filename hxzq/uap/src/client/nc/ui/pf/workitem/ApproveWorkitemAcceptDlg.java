package nc.ui.pf.workitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.wfengine.engine.ActivityInstance;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.uap.pf.IPFMobileAppServiceFacade;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.message.Attachment;
import nc.message.FileAttachment;
import nc.message.vo.AttachmentSetting;
import nc.message.vo.AttachmentVO;
import nc.ui.bd.ref.busi.UserWithClassRefModel;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.checknote.PfChecknoteEnum;
import nc.ui.pub.beans.HyperlinkLabelEvent;
import nc.ui.pub.beans.HyperlinkLabelListener;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.table.NCTableHeaderUI;
import nc.ui.pub.desktop.PfOperationNotifier;
import nc.ui.pub.pf.DispatchDialog;
import nc.ui.pub.style.Style;
import nc.ui.pub.workflownote.ApproveFlowRejectPanelTable;
import nc.ui.pub.workflownote.ApproveItemTableModel;
import nc.ui.pub.workflownote.ApproveWorkitemAcceptDataManage;
import nc.ui.pub.workflownote.ApproveWorkitemAcceptTableModel;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.pub.workflownote.FlowStateRefDlg;
import nc.ui.pub.workflowqry.WorkflowManageUtil;
import nc.ui.wfengine.ext.ApplicationRuntimeAdjustContext;
import nc.ui.wfengine.ext.ApplicationRuntimeAdjustFactory;
import nc.ui.wfengine.ext.IApplicationRuntimeAdjust;
import nc.ui.wfengine.ext.add.AddAssignDialog;
import nc.ui.wfengine.flowchart.graph.ActivityCell;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.jcom.io.FileFilterFactoryAdapter;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.msg.MessageActionVO;
import nc.vo.pf.pub.util.ArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.checknote.PfChecknoteVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.AssignableInfo;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.pf.RoleUserParaVO;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.core.parser.XPDLNames;
import nc.vo.wfengine.core.util.CoreUtilities;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nc.vo.wfengine.pub.WfTaskOrInstanceStatus;
import nc.vo.wfengine.pub.WfTaskType;
import nc.vo.workflow.admin.WorkflowManageContext;

import org.apache.commons.lang.ArrayUtils;

/**
 * 审批流的工作项处理对话框
 * 
 * @author leijun 2009-9
 * @since 6.0
 * @Modifier zhouzhenga 2010-4 添加流程的跟踪和抄送
 * @Modifier zhouzhenga 2011-1 审批支持添加附件
 * @Modifier zhouzhenga 2011-1 支持预置的批语
 * @Modifier wcj, yanke1 2011-7 跟踪、抄送功能的实现
 * @modifier yanke1 2011-9-8 根据changlx所提需求，当在消息中心打开单据并审批时，审批结束后自动关闭单据界面
 * @modifier zhangrui 2012-3-13 根据新UE要求对此进行重构
 * @modifier yanke1 2012-7-19 将自动关闭单据界面的代码移到外面PfUtilClient中做。这里只提供界面供用户选择是否关闭
 * 
 */
public class ApproveWorkitemAcceptDlg extends UIDialog implements
		ActionListener {

	private static final long serialVersionUID = -9104313190799304533L;

	public static final int STATUS_APPROVE = 0;// 批准
	public static final int STATUS_UNAPPROVE = 1;// 不批准
	public static final int STATUS_REJECT = 2;// 驳回
	public static final int STATUS_TRANSFER = 3;// 改派
	public static final int STATUS_ADDASSIGN = 4;// 加签

	private final Color background = new Color(237, 237, 237);

	private WorkflownoteVO worknoteVO = null;
	private boolean isInWorkflow = false;
	private String hintMessage;

	TextListPane paneDispatchPerson = null;
	UILabel lblDipatchTo = null;
	UIPanel pnlDispatch = null;

	// update by lihaibo 用于FlowStateRefDlg类 赋值
	public static ApproveFlowRejectPanel rejectPanel;
	public static Object[] cells;
	public static ActivityInstance[] allActInstances;

	// UISplitPane splitReject = null;
	// UIPanel pnlRejectDownHalf = null;
	UIPanel pnlReject = null;

	UILabel lblTransfer = null;
	TextListPane paneTransferPerson = null;
	UIPanel pnlTransferRef = null;

	UILabel lblAddAssign = null;
	UIRefPane paneAddAssignPerson = null;
	UIPanel pnlAddAssignRef = null;

	UICheckBox chkTrack = null;

	ImageIcon iconCopySend = null;
	UILabel lblCopySend = null;
	ImageIcon iconAttach = null;
	UILabel lblAddAttach = null;

	BlockListPanel pnlAttachs = null;
	BlockListPanel pnlCopySenders = null;

	// 审批结束后是否关闭单据界面
	UICheckBox chkAutoClose = null;

	// UILabel lblViewFlow = null;

	UIButton btnCancel = null;

	List<Attachment> attchlist = new ArrayList<Attachment>();

	// yanke1+ 2011-9-8
	// /** 单据节点的funcletWindow */
	// yanke1 2012-7-18
	// private IFuncletWindow parentFuncletWindow = null;

	private boolean isOpenedInDialog = false;
	private boolean isAllreadyTracked = false;

	/** 加签对话框 */
	private AddAssignDialog addAssignDialog = null;

	/* 改派的 用户选择panel */
	// private AppointUserPanel transferPanel;
	// private TransferUserDialog transferDialog;

	/* 抄送的用户选择panel */
	private CpySendUserTree2List copySendPanel;
	private CopySendDialog copySendDialog;

	// 指派的面板
	private DispatchDialog dispatchDialog;

	private JFileChooser fileChooser = null;

	private UIRefPane refRemark = null;

	private String passCheckNote = null;
	private String nopassCheckNote = null;
	private String rejectCheckNote = null;
	// private String allCheckNote = null;

	private static boolean DEFAULT_CLOSE_AFTER_APPROVE = true;

	private List<PfChecknoteVO> checkNoteVO = null;

	// 2013-1-18 add
	private UIPanel configPanel = null;
	private SpringLayout totalSpring = null;

	private UIRadioButton radioApprove = null;
	private UIRadioButton radioUnApprove = null;
	private UIRadioButton radioReject = null;
	private UIRadioButton radioTransfer = null;
	private UIRadioButton radioAddAssign = null;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private UIPanel radioPanel = null;

	private UIPanel refPanel = null;

	private UILabel lblReject;
	TextListPane paneRejectPerson = null;
	private SpringLayout rejectRefSpring = null;

	private SpringLayout transferRefSpring = null;

	private SpringLayout addAssignRefSpring = null;

	private UIScrollPane txtPane = null;

	private UIPanel operationsPanel = null;
	private SpringLayout operationSpring = null;

	private UIPanel btnPanel = null;

	private UIButton btnOK = null;
	// private UIButton btnViewFlow = null;
	private JToggleButton btnViewFlow = null;

	private UIPanel bottomPanel = null;
	private UIScrollPane hintMessagePanel = null;
	private UIPanel tailPanel = null;

	// 用来标记当前状态
	private int condition;

	private FlowStateRefDlg rejectDlg;
	private FlowStateDlg viewMoreDialog;
	private UIScrollPane tableScrollPanel = null;
	private UITextArea hintMsgTxtAr = null;
	private UITablePane tablePanel = null;
	private UIPanel pnlViewMore = null;
	private SpringLayout pnlViewMoreSpring = null;
	private UILabel lblViewMore = null;

	/**
	 * 构造
	 * 
	 * @param parent
	 * @param noteVO
	 *            审批工作项VO
	 * @param isInWorkflow
	 *            是否为工作流中审批子流程的工作项
	 */
	public ApproveWorkitemAcceptDlg(Container parent, WorkflownoteVO noteVO,
			boolean isInWorkflow) {
		this(parent, noteVO, isInWorkflow, false);
	}

	/**
	 * 构造
	 * 
	 * @param parent
	 * @param noteVO
	 *            审批工作项VO
	 * @param isInWorkflow
	 *            是否为工作流中审批子流程的工作项
	 * @author yanke1 2012-7-18 添加参数isOpenedIndialog。
	 *         如果此参数为true，那么在界面上提示用户审批完成后是否关闭单据界面
	 */
	public ApproveWorkitemAcceptDlg(Container parent, WorkflownoteVO noteVO,
			boolean isInWorkflow, boolean isOpenedInDialog) {
		this(parent, noteVO, isInWorkflow, isOpenedInDialog, null);
	}

	public ApproveWorkitemAcceptDlg(Container parent, WorkflownoteVO noteVO,
			boolean isInWorkflow, boolean isOpenedInDialog, String hintMessage) {
		super(parent);

		this.isOpenedInDialog = isOpenedInDialog;
		this.hintMessage = hintMessage;
		this.worknoteVO = noteVO;
		this.isInWorkflow = isInWorkflow;
		// 启动一个线程,初始化随机数
		CoreUtilities.dummyThread4Performance();
		// 初始化界面
		// initialize();

		condition = STATUS_APPROVE;// 设置初始化，用来控制refpanel的初始化,STATUS_TRANSFER/STATUS_ADDASSIGN

		initialize();

		attachEventListener();

		checkNoteVO = initIndividualCheckNote();// 用来设置note文本框预置

		initData();

		// 默认是审批页签
		setStatus(condition);
	}

	/**
	 * 构造
	 * */
	public ApproveWorkitemAcceptDlg(Container parent) {
		super(parent);
	}

	/*
	 * protected boolean isStatusShowOnTab(int status) { return
	 * statusPanelMap.containsKey(status); }
	 * 
	 * protected int getTabIndexByStatus(int status) { UIPanel panel =
	 * statusPanelMap.get(status); return tabPane.indexOfComponent(panel); }
	 */

	/**
	 * 初始化界面控件
	 */
	protected void initialize() {
		setResizable(true);
		setName("PfWorkFlowCheck");
		setBackground(background);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ApproveLangUtil.getApproveDealStatus());
		add(getConfigInfo());

		List<AttachmentVO> attSetting = worknoteVO.getAttachmentSetting();
		if (attSetting != null) {
			AttachmentSetting attachSetting = new AttachmentSetting(attSetting);
			Attachment[] attchments = attachSetting.getAttachments();
			for (int count = 0; count < attchments.length; count++) {
				attchlist.add(attchments[count]);
			}
		}

		setSelectedAction();
	}

	public void initData() {
		getDataManager().initDataByApproveflow(getWorkFlow());
	}

	private void setSelectedAction() {

		if (Context.getDefaultAction() == null) {
			return;
		}

		if (Context.getDefaultAction().equals(MessageActionVO.PASS)) {
			// 选中批准
			setStatus(STATUS_APPROVE);
		} else if (Context.getDefaultAction().equals(MessageActionVO.NO_PASS)) {
			// 选中不批准
			setStatus(STATUS_UNAPPROVE);
		} else if (Context.getDefaultAction().equals(MessageActionVO.REJECT)) {
			// 选中驳回
			setStatus(STATUS_REJECT);
		} else if (Context.getDefaultAction().equals(
				MessageActionVO.ADD_APPROVER)) {
			// 选中加签
			setStatus(STATUS_ADDASSIGN);
		} else if (Context.getDefaultAction().equals(MessageActionVO.TRANSFER)) {
			// 选中改派
			setStatus(STATUS_TRANSFER);
		}

	}

	private UIPanel getConfigInfo() {
		if (configPanel == null) {
			totalSpring = new SpringLayout();
			configPanel = new UIPanel();
			configPanel.setBackground(background);
			configPanel.setLayout(totalSpring);
			configPanel.add(getRadioPanel());// 单选钮面板
			configPanel.add(getRefPanel());// 参照面板
			configPanel.add(getTxtPane());// 文本输入框面板
			configPanel.add(getOperationsPanel());// 操作面板
			configPanel.add(getPanelCopySenders());// 抄送人面板
			configPanel.add(getPanelAttach());// 附件面板
			configPanel.add(getBtnPanel());// 按钮面板
			configPanel.add(getBottomPanel());// 底部面板含文本及表格
		}
		return configPanel;
	}

	private UIPanel getRefPanel() {
		if (refPanel == null) {
			refPanel = new UIPanel();
			refPanel.setLayout(new BorderLayout());
			refPanel.setBackground(background);
			refPanel.setPreferredSize(new Dimension(getWidth(), 0));
			// refPanel.setBorder(BorderFactory.createLineBorder(Color.green));

			if (getRefContent() != null)
				refPanel.add(getRefContent());

			totalSpring.putConstraint(SpringLayout.NORTH, refPanel, 0,
					SpringLayout.SOUTH, getRadioPanel());
			totalSpring.putConstraint(SpringLayout.WEST, refPanel, -16,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, refPanel, 0,
					SpringLayout.EAST, getConfigInfo());
		}
		return refPanel;
	}

	private UIPanel getRefContent() {
		switch (condition) {
		case STATUS_APPROVE:// 批准
			return null;
		case STATUS_UNAPPROVE:// 不批准
			return null;
		case STATUS_REJECT:// 驳回
			return getPanelRejectRef();
		case STATUS_TRANSFER:// 改派
			return getPnlTransferRef();
		case STATUS_ADDASSIGN:// 加签
			return getPnlAddAssignRef();
		default:
			return null;
		}
	}

	private void respringRadioPanel() {
		SpringLayout radioSpring = new SpringLayout();
		getRadioPanel().setLayout(radioSpring);

		Component[] comps = getRadioPanel().getComponents();

		if (ArrayUtil.isNull(comps)) {
			return;
		}

		Component lastComp = comps[0];

		radioSpring.putConstraint(SpringLayout.NORTH, lastComp, 0,
				SpringLayout.NORTH, getRadioPanel());
		radioSpring.putConstraint(SpringLayout.WEST, lastComp, 20,
				SpringLayout.WEST, getRadioPanel());

		for (int i = 1; i < comps.length; i++) {
			Component currComp = comps[i];
			radioSpring.putConstraint(SpringLayout.NORTH, currComp, 0,
					SpringLayout.NORTH, getRadioPanel());
			radioSpring.putConstraint(SpringLayout.WEST, currComp, 20,
					SpringLayout.EAST, lastComp);

			lastComp = currComp;
		}

	}

	private UIPanel getRadioPanel() {
		if (radioPanel == null) {
			FlowLayout radiozLayout = new FlowLayout(FlowLayout.LEFT);
			radioPanel = new UIPanel();
			radioPanel.setBackground(background);
			radioPanel.setLayout(radiozLayout);

			buttonGroup.add(getRadioApprove());
			radioPanel.add(getRadioApprove());

			buttonGroup.add(getRadioUnApprove());
			radioPanel.add(getRadioUnApprove());

			// radioSpring.putConstraint(SpringLayout.NORTH, radioApprove, 0,
			// SpringLayout.NORTH, getRadioPanel());
			// radioSpring.putConstraint(SpringLayout.WEST, radioApprove, 20,
			// SpringLayout.WEST, getRadioPanel());
			//
			// radioSpring.putConstraint(SpringLayout.NORTH, radioUnApprove, 0,
			// SpringLayout.NORTH, getRadioPanel());
			// radioSpring.putConstraint(SpringLayout.WEST, radioUnApprove, 20,
			// SpringLayout.EAST, getRadioApprove());
			//
			// Component lastComp = getRadioUnApprove();

			// 判断是否可驳回
			if (canReject()) {
				buttonGroup.add(getRadioReject());
				radioPanel.add(getRadioReject());

				// radioSpring.putConstraint(SpringLayout.NORTH,
				// getRadioReject(), 0,
				// SpringLayout.NORTH, getRadioPanel());
				// radioSpring.putConstraint(SpringLayout.WEST,
				// getRadioReject(), 20,
				// SpringLayout.EAST, lastComp);
				//
				// lastComp = getRadioReject();
			}
			// 判断是否可改派
			if (canTransfer()) {
				buttonGroup.add(getRadioTransfer());
				radioPanel.add(getRadioTransfer());

				// radioSpring.putConstraint(SpringLayout.NORTH,
				// getRadioTransfer(), 0,
				// SpringLayout.NORTH, getRadioPanel());
				// radioSpring.putConstraint(SpringLayout.WEST,
				// getRadioTransfer(), 20,
				// SpringLayout.EAST, lastComp);
				//
				// lastComp = getRadioTransfer();
			}
			// 判断是否可加签
			if (canAddApprover()) {
				buttonGroup.add(getRadioAddAssign());
				radioPanel.add(getRadioAddAssign());

				// radioSpring.putConstraint(SpringLayout.NORTH,
				// getRadioAddAssign(), 0,
				// SpringLayout.NORTH, getRadioPanel());
				// radioSpring.putConstraint(SpringLayout.WEST,
				// getRadioAddAssign(), 20,
				// SpringLayout.EAST, lastComp);
				//
				// lastComp = getRadioAddAssign();
			}

			// radioPanel.setBorder(BorderFactory.createLineBorder(Color.red));
			totalSpring.putConstraint(SpringLayout.NORTH, radioPanel, 5,
					SpringLayout.NORTH, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.WEST, radioPanel, -16,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, radioPanel, 0,
					SpringLayout.EAST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.SOUTH, radioPanel, 35,
					SpringLayout.NORTH, getConfigInfo());

			this.respringRadioPanel();
			radioPanel.repaint();
		}
		return radioPanel;
	}

	private UIRadioButton getRadioApprove() {
		if (radioApprove == null) {
			radioApprove = new UIRadioButton(ApproveLangUtil.getPass());// ApproveLangUtil.getApprove()"批准"
			radioApprove.setOpaque(false);
		}
		return radioApprove;
	}

	private UIRadioButton getRadioUnApprove() {
		if (radioUnApprove == null) {
			radioUnApprove = new UIRadioButton(ApproveLangUtil.getNoPass()/*
																		 * NCLangRes
																		 * .
																		 * getInstance
																		 * ().
																		 * getStrByID
																		 * (
																		 * "approveworkitem"
																		 * ,
																		 * "ApproveWorkitemAcceptDlg-0000"
																		 * )
																		 *//* 不批准 */);
			radioUnApprove.setOpaque(false);
		}
		return radioUnApprove;
	}

	private UIRadioButton getRadioReject() {
		if (radioReject == null) {
			radioReject = new UIRadioButton(ApproveLangUtil.getReject());
			radioReject.setOpaque(false);
		}
		return radioReject;
	}

	private UIRadioButton getRadioTransfer() {
		if (radioTransfer == null) {
			radioTransfer = new UIRadioButton(ApproveLangUtil.getTransfer());
			radioTransfer.setOpaque(false);
		}
		return radioTransfer;
	}

	private UIRadioButton getRadioAddAssign() {
		if (radioAddAssign == null) {
			radioAddAssign = new UIRadioButton(ApproveLangUtil.getAddAssign());
			radioAddAssign.setOpaque(false);
		}
		return radioAddAssign;
	}

	public WorkflownoteVO getWorkFlow() {
		return worknoteVO;
	}

	private boolean canReject() {
		// 加签的用户不允许驳回
		return !this.worknoteVO.getActiontype().endsWith(
				WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX);
	}

	private boolean canTransfer() {
		Object value = this.worknoteVO.getRelaProperties().get(
				XPDLNames.CAN_TRANSFER);
		if (value != null && "true".equalsIgnoreCase(value.toString())) {
			if (this.worknoteVO.actiontype
					.equalsIgnoreCase(WorkflownoteVO.WORKITEM_TYPE_APPROVE
							+ WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX))
				return false;
			else
				return true;
		} else
			return false;
	}

	private boolean canAddApprover() {// 是否可以加签
		Object value = this.worknoteVO.getRelaProperties().get(
				XPDLNames.CAN_ADDAPPROVER);
		if (value != null && "true".equalsIgnoreCase(value.toString())) {
			if (this.worknoteVO.actiontype
					.equalsIgnoreCase(WorkflownoteVO.WORKITEM_TYPE_APPROVE
							+ WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX))
				return false;
			else
				return true;
		} else
			return false;
	}

	private void setBtnViewFlowStatus(boolean flag) {
		if (flag) {
			getBtnViewFlow().setIcon(
					new ImageIcon(getClass().getResource(
							"/images/message/dispquery.png")));
			getBtnViewFlow().setSelected(flag);
			int tailHight = (int) (getHintMessagePanel().getPreferredSize()
					.getHeight()
					+ getTableScrollPanel().getPreferredSize().getHeight() + getViewMoreLinkPanel()
					.getPreferredSize().getHeight());
			getTailPanel().setPreferredSize(
					new Dimension(getWidth(), tailHight));
			setStatus(condition);
			initData();
		} else {
			getBtnViewFlow().setIcon(
					new ImageIcon(getClass().getResource(
							"/images/message/hidequery.png")));
			getBtnViewFlow().setSelected(flag);
			getTailPanel().setPreferredSize(new Dimension(getWidth(), 0));
			setStatus(condition);
		}
	}

	protected void attachEventListener() {
		getRadioApprove().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus(STATUS_APPROVE);
				setBtnViewFlowStatus(true);
			}
		});
		getRadioUnApprove().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus(STATUS_UNAPPROVE);
				setBtnViewFlowStatus(true);
			}
		});
		// 判断是否可驳回
		if (canReject()) {
			getRadioReject().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setStatus(STATUS_REJECT);
					setBtnViewFlowStatus(false);
				}
			});
		}
		// 判断是否可改派
		if (canTransfer()) {
			getRadioTransfer().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setStatus(STATUS_TRANSFER);
					setBtnViewFlowStatus(false);
				}
			});
		}
		// 判断是否可加签
		if (canAddApprover()) {
			getRadioAddAssign().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setStatus(STATUS_ADDASSIGN);
					setBtnViewFlowStatus(false);
				}
			});
		}

		// 确认对话框
		getBtnOK().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getRadioApprove().isSelected()) {
					onApprove(true);// 审批通过
				} else if (radioUnApprove.isSelected()) {
					onApprove(false);// 审批不通过
				}
				if (getRadioReject().isSelected()) {
					onReject();// 驳回
				}
				if (getRadioTransfer().isSelected()) {
					onTransfer();// 改派
				}
				if (getRadioAddAssign().isSelected()) {
					onAddAssign();// 加签
				}
			}
		});

		// 取消对话框
		getBtnCancel().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeCancel();
			}
		});

		// 抄送
		getLblCopySend().addHyperlinkLabelListener(
				new HyperlinkLabelListener() {
					@Override
					public void hyperlinkClicked(HyperlinkLabelEvent event) {
						onCopySend();
						setStatus(condition);
					}
				});
		/*
		 * // 选择指派人 getPaneDispatchPerson().setActionListener(new
		 * ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * getDispatchDialog().showModal(); } }); // 选择指派人完毕
		 * getDispatchDialog().addUIDialogListener(new UIDialogListener() {
		 * 
		 * @Override public void dialogClosed(UIDialogEvent event) {
		 * if(event.m_Operation == UIDialogEvent.WINDOW_OK) {
		 * onChooseDispatchOK(); } } });
		 */
		// 删除抄送人
		getPanelCopySenders().setRemovePaneListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockPaneEvent event = (BlockPaneEvent) e;
				event.getValue();
				int deleteIndex = -1;
				for (int i = 0; i < getCpySendPanel().getListModelMsg()
						.getSize()
						+ getCpySendPanel().getListModelMail().getSize(); i++) {
					Object object = null;
					if (i < getCpySendPanel().getListModelMsg().getSize()) {
						object = getCpySendPanel().getListModelMsg().get(i);
						if (object == event.getValue()) {
							deleteIndex = i;
							if (deleteIndex >= 0) {
								getCpySendPanel().getMsgUserRoleList()
										.setSelectedIndex(deleteIndex);
								getCpySendPanel().onMsgRemoveBtnClick();
							}
							break;
						}
					} else {
						object = getCpySendPanel().getListModelMail().get(
								i
										- getCpySendPanel().getListModelMsg()
												.getSize());
						if (object == event.getValue()) {
							deleteIndex = i;
							if (deleteIndex >= 0) {
								getCpySendPanel().getMailUserRoleList()
										.setSelectedIndex(deleteIndex);
								getCpySendPanel().onMailRemoveBtnClick();
							}
							break;
						}
					}
				}
				if (getCpySendPanel().getListModelMsg().getSize()
						+ getCpySendPanel().getListModelMail().getSize() == 0) {
					getPanelCopySenders().setPreferredSize(
							new Dimension(getWidth(), 0));
					setStatus(condition);
				}
			}
		});

		/*
		 * //联查审批流 getLinkViewFlow().addHyperlinkLabelListener(new
		 * HyperlinkLabelListener() {
		 * 
		 * @Override public void hyperlinkClicked(HyperlinkLabelEvent event) {
		 * showFlowinfo(); } });
		 */

		getViewMoreLinkBtn().addHyperlinkLabelListener(
				new HyperlinkLabelListener() {
					@Override
					public void hyperlinkClicked(HyperlinkLabelEvent event) {
						showFlowinfo();
					}
				});

		// 历史审批
		getBtnViewFlow().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getBtnViewFlow().isSelected()) {

					setBtnViewFlowStatus(true);
				} else {
					setBtnViewFlowStatus(false);
				}
			}
		});

		// 上传附件
		getLblAddAttach().addHyperlinkLabelListener(
				new HyperlinkLabelListener() {
					@Override
					public void hyperlinkClicked(HyperlinkLabelEvent event) {
						if (getChooser().showOpenDialog(
								ApproveWorkitemAcceptDlg.this) == JFileChooser.APPROVE_OPTION) {
							File[] files = getChooser().getSelectedFiles();
							addAttachPane(files);
						}
						setStatus(condition);
					}
				});

		getPanelAttach().setRemovePaneListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockPaneEvent event = (BlockPaneEvent) e;
				BlockPane sourcePane = (BlockPane) event.getSource();
				Container blockPaneParent = sourcePane.getParent();
				if (blockPaneParent.getComponentCount() <= 1) {
					getPanelAttach().setPreferredSize(
							new Dimension(getWidth(), 0));
					setStatus(condition);
				}
			}
		});
	}

	private void onChooseRejectOK() {
		getPaneRejectPerson().clearTexts();
		/**
		 * update by lihaibo 驳回改为选择下面表格人员来对应流程图
		 */
		ApproveFlowRejectPanelTable prt = ((FlowStateRefDlg) rejectPanel
				.getM_container()).getPanelRejectTable();
		ApproveItemTableModel appitm = (ApproveItemTableModel) ((FlowStateRefDlg) rejectPanel
				.getM_container()).getPanelRejectTable().getTableState()
				.getModel();
		ListSelectionModel lsm = prt.getTableState().getSelectionModel();
		Integer rowNumber = lsm.getAnchorSelectionIndex();
		Activity rejActivity = null;
		String activityid = "";
		if (rowNumber == -1) {
			rejActivity = (Activity) ((ActivityCell) cells[0]).getUserObject();
			activityid = rejActivity.getId();
		} else {
			activityid = appitm.getActivityOfRowNumber(rowNumber);
			for (Object c : cells) {
				if (c instanceof ActivityCell) {
					Object o = ((ActivityCell) c).getUserObject();
					if (o instanceof Activity) {
						if (((Activity) o).getId().equals(activityid)) {
							rejActivity = (Activity) o;
							break;
						}
					}
				}
			}
		}

		// Activity rejectActivity = getPanelRejectFlow().getRejectToActivity();
		Activity rejectActivity = getRejectDialog().getSelectActivity();
		// OrganizeUnit[] users = getRejectDialog().getAdminPanel().get;
		// if(users != null && users.length > 0) {
		// for(OrganizeUnit user : users) {
		// getPaneTransferPerson().addTextItem(user.getName(), user);
		// }
		// }
		if (checkRejectValidity(allActInstances, activityid)) {
			if (rejActivity != null) {
				ApproveFlowRejectPanel.selectedActivity = rejActivity;
				getPaneRejectPerson().addTextItem(rejActivity.getName(),
						rejActivity);
			}
		}
	}

	/**
	 * 校验 add by lihaibo
	 * 
	 * @param allActInstances
	 * @param actid
	 * @return
	 */
	private boolean checkRejectValidity(ActivityInstance[] allActInstances,
			String actid) {
		ArrayList<String> finishActId = new ArrayList<String>();
		for (int start = 0, end = allActInstances == null ? 0
				: allActInstances.length; start < end; start++) {
			if (allActInstances[start].getStatus() == WfTaskOrInstanceStatus.Finished
					.getIntValue()) {
				finishActId.add(allActInstances[start].getActivityID());
			}
		}
		boolean isValidity = finishActId.contains(actid);
		if (!isValidity) {
			MessageDialog
					.showWarningDlg(
							null,
							null,
							NCLangRes.getInstance().getStrByID("pfworkflow",
									"ApproveFlowRejectPanel-000000")/*
																	 * 不可驳回到当前环节的后续环节
																	 * ！
																	 */);
		}
		return isValidity;
	}

	/*
	 * private void onChooseTransferOK() { getPaneTransferPerson().clearTexts();
	 * OrganizeUnit[] users = getTransferUserPanel().getResultVOs(); if (users
	 * != null && users.length > 0) { for (OrganizeUnit user : users) {
	 * getPaneTransferPerson().addTextItem(user.getName(), user); } } }
	 */

	// private void onChooseDispatchOK() {
	// //获取Panel每个页签及其对应的名称
	// StringBuffer sbDispatchUser = new StringBuffer();
	// UITabbedPane tabPane = getDisPatchPanel().getUITabbedPane();
	// for (int i = 0; i < tabPane.getTabCount(); i++) {
	// //名称
	// sbDispatchUser.append(tabPane.getTitleAt(i));
	// sbDispatchUser.append("：");
	//
	// UIListToList ltl = (UIListToList) tabPane.getTabComponentAt(i);
	// Object[] rightData = ltl.getRightData();
	// if(rightData != null) {
	// for(int j=0; j < rightData.length; j++) {
	// OrganizeUnit selectUser = (OrganizeUnit) rightData[j];
	// sbDispatchUser.append(selectUser.getName());
	// if(j < rightData.length - 1) {
	// sbDispatchUser.append(selectUser.getName());
	// }
	// }
	// sbDispatchUser.append("；");
	// }
	// }
	// getPaneDispatchPerson().setText(sbDispatchUser.toString());
	// }

	@Override
	protected void hotKeyPressed(KeyStroke hotKey, KeyEvent e) {
		if (hotKey.getKeyCode() == KeyEvent.VK_F2) {
			getRefRemark().onButtonClicked();

			if (!StringUtil.isEmpty(getRefRemark().getRefName())) {
				int pos = getCurrentTextArea().getCaretPosition();
				getCurrentTextArea().insert("\n" + getRefRemark().getRefName(),
						pos);
				e.consume();
			}
		}
	}

	private boolean beforeButtonOperate() {
		// 抄送
		copySend();

		// 添加附件
		List<AttachmentVO> Attachments = updateAttachment2DocServer();
		worknoteVO.setAttachmentSetting(Attachments);

		String note = getCheckNotePulsApproveResult();
		String result = ApproveWorkitemAssistor.sign(worknoteVO, note);
		// XXXX
		Logger.debug("上传附件成功！附件个数为" + Attachments.size());
		return StringUtil.isEmptyWithTrim(result);

	}

	/**
	 * 预先获得批语
	 * */
	private String getCheckNotePulsApproveResult() {
		String checkNote = "";
		if (getRadioApprove().isSelected()) {
			checkNote = getTxtNote().getText();
			if (StringUtil.isEmpty(checkNote))
				checkNote = getPassCheckNote()
						+ UFBoolean.valueOf(true).toString();
		} else if (getRadioUnApprove().isSelected()) {
			checkNote = getTxtNote().getText();
			if (StringUtil.isEmpty(checkNote))
				checkNote = getNoPassCheckNote()
						+ UFBoolean.valueOf(false).toString();
			;
		}
		if (getRadioReject().isSelected()) {
			checkNote = getTxtNote().getText();
			if (StringUtil.isEmpty(checkNote)) {
				checkNote = getRejectCheckNote() + "R";
			}
		}
		if (getRadioTransfer().isSelected()) {
			checkNote = getTxtNote().getText();
		}
		if (getRadioAddAssign().isSelected()) {
			checkNote = getTxtNote().getText();
		}
		return checkNote;

	}

	// private void onChooseAddAssignOK() {
	// List<OrganizeUnit> userList = getAddAssignDialog().getAddAssignPanel()
	// .getSelectedOrg();
	// getPaneAddAssignPerson().clearTexts();
	// if (userList != null) {
	// for (int i = 0; i < userList.size(); i++) {
	// OrganizeUnit user = userList.get(i);
	// getPaneAddAssignPerson().addTextItem(user.getName(), user);
	// }
	// }
	// }

	// 根据参照所选用户，构造出加签的OrganizeUnit
	private List<OrganizeUnit> getSelectedUsers() {
		List<OrganizeUnit> assignedList = new ArrayList<OrganizeUnit>();
		// String [] refPKs = getPaneAddAssignPerson().getRefPKs();
		// String [] refCodes = getPaneAddAssignPerson().getRefCodes();
		Vector<Vector> selectData = getPaneAddAssignPerson().getRefModel()
				.getSelectedData();
		for (Vector data : selectData) {
			String pk = (String) data.get(4);
			String userCode = (String) data.get(0);
			String pk_org = (String) data.get(3);
			String name = (String) data.get(1);
			OrganizeUnit unit = new OrganizeUnit();
			unit.setOrgUnitType(OrganizeUnitTypes.Operator_INT);
			unit.setPk(pk);
			unit.setCode(userCode);
			unit.setPkOrg(pk_org);
			unit.setName(name);
			assignedList.add(unit);
		}
		return assignedList;
	}

	private void onAddAssign() {

		List<OrganizeUnit> assignedList = getSelectedUsers();
		if (ArrayUtil.isNull(assignedList)) {
			new PfOperationNotifier(this).fireOperationPerformed(
					0,
					false,
					NCLangRes.getInstance().getStrByID("pfworkflow1",
							"ApproveWorkitemAcceptDlg-000000")/* 请选择要加签的用户 */);
			return;
		}

		if (!beforeButtonOperate())
			return;

		worknoteVO.setChecknote(getTxtNote().getText());

		ApplicationRuntimeAdjustContext context = new ApplicationRuntimeAdjustContext();
		context.setStyle(getAddAssignDialog().getAddAssignStyle());// style被去掉了
		context.setUserObject(assignedList);
		context.setWorkFlow(worknoteVO);

		IApplicationRuntimeAdjust runtimeObj = ApplicationRuntimeAdjustFactory
				.createAdjust(IApplicationRuntimeAdjust.ADJUST_TYPE_ADDASSIGN);
		try {
			runtimeObj.adjust(context);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			MessageDialog.showErrorDlg(this, null, message);
			return;
		}
		this.closeOK();
	}

	private void onTransfer() {
		if (!beforeButtonOperate())
			return;
		worknoteVO.setChecknote(getTxtNote().getText());
		// 改派
		boolean doneSuccessed = true;
		try {
			doneSuccessed = runTransfer();
		} catch (BusinessException e) {
			doneSuccessed = false;
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null,
					ApproveLangUtil.getExceptionWhenTransfer(e.getMessage()));
		}
		if (doneSuccessed) {
			this.closeOK();
		}
	}

	/**
	 * 改派 处理
	 * 
	 * @Modify zhouzheng 2010-4同一修改为调用WorkflowManageUtil
	 *         .doAlterSender(Container container, WorkflowManageContext
	 *         context)方法
	 * @return
	 * @throws BusinessException
	 */
	private boolean runTransfer() throws BusinessException {
		WorkflowManageContext context = new WorkflowManageContext();
		context.setBillId(worknoteVO.getBillVersionPK());
		context.setApproveStatus(WfTaskOrInstanceStatus.Started.getIntValue());
		context.setSendman(worknoteVO.getCheckman());
		context.setWorkflownotePk(worknoteVO.getPk_checkflow());
		context.setBillType(worknoteVO.getPk_billtype());
		context.setFlowType(worknoteVO.getWorkflow_type());
		// /////////////////////////////////////////////////////////////////////
		// getTransferDialog().getTransferUserPanel().getResultVOs();
		// hanyanwei pks ----- uservos cache serv
		String[] userPKs = getPaneTransferPersonRef().getRefPKs();
		// 1.
		/*
		 * UserVO[] userVos = findUserByIDs(userPKs); OrganizeUnit[] orgUnits =
		 * new OrganizeUnit[userVos.length]; for (int i = 0; i < userVos.length;
		 * i++) { UserVO user = userVos[i]; OrganizeUnit selectedUser = new
		 * OrganizeUnit(user); orgUnits[i] = selectedUser; }
		 */
		// 2.
		OrganizeUnit[] orgUnits = findUserByIDs(userPKs);// 应该选择个

		String checknote = getCurrentTextArea().getText();
		// /////////////////////////现为用户参照
		if (new WorkflowManageUtil().doAlterSend(this, context, orgUnits,
				checknote)) {
			worknoteVO.getTaskInfo().getTask().setAppoint(true);
			return true;
		}
		return false;

		/*
		 * if (new WorkflowManageUtil().doAlterSend(this, context,
		 * selectedUsers)) {
		 * worknoteVO.getTaskInfo().getTask().setAppoint(true); return true; }
		 * return false;
		 */
	}

	private IPFMobileAppServiceFacade service;

	private IPFMobileAppServiceFacade getService() {
		if (service == null) {
			service = NCLocator.getInstance().lookup(
					IPFMobileAppServiceFacade.class);
		}
		return service;
	}

	private void test() {
		try {
			UserVO userVO = WorkbenchEnvironment.getInstance().getLoginUser();
			// Object obj = getService().getTaskList(userVO.getPk_group(),
			// userVO.getCuserid(), null, "submit", "unhandled", 1, 25);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("actioncode", "doReject");
			param.put("statuskey", "ishandled");
			List<String> ids = new ArrayList<String>();
			ids.add("e04d7df5-ccc3-11e2-8feb-cf5dbfd3a38f");
			param.put("rejectmarks", ids);
			param.put("taskid", "0001E41000000002C61A");
			param.put("statuscode", "unhandled");
			params.add(param);
			getService().doAction(userVO.getPk_group(), userVO.getCuserid(),
					params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 驳回
	 */
	private void onReject() {
		if (!beforeButtonOperate())
			return;

		// boolean isSubmitToRejectTache = chkSubmitToRejectTache.isSelected();

		String checkNote = getTxtNote().getText();
		if (StringUtil.isEmpty(checkNote)) {
			checkNote = getRejectCheckNote();
		}
		worknoteVO.setChecknote(checkNote);
		// 驳回
		worknoteVO.getTaskInfo().getTask()
				.setTaskType(WfTaskType.Backward.getIntValue());

		// worknoteVO.getTaskInfo().getTask().setSubmit2RjectTache(isSubmitToRejectTache);
		Activity rejectActivity = getRejectDialog().getSelectActivity();
		// Activity rejectActivity = getPanelRejectFlow().getRejectToActivity();
		if (rejectActivity == null) {
			// update by lihaibo

			MessageDialog.showErrorDlg(null, "警告", "请选择需要驳回至哪里！");
			return;
			// worknoteVO.getTaskInfo().getTask().setBackToFirstActivity(true);
		} else {
			worknoteVO.getTaskInfo().getTask().setBackToFirstActivity(false);
		}
		worknoteVO
				.getTaskInfo()
				.getTask()
				.setJumpToActivity(
						rejectActivity == null ? null : rejectActivity.getId());
		worknoteVO.setApproveresult("R");

		this.closeOK();
	}

	private void onCopySend() {
		getCopySendDialog().showModal();
	}

	private AddAssignDialog getAddAssignDialog() {
		if (addAssignDialog == null) {
			addAssignDialog = new AddAssignDialog(this, worknoteVO);
			// 选择加签完毕
			addAssignDialog.addUIDialogListener(new UIDialogListener() {
				@Override
				public void dialogClosed(UIDialogEvent event) {
					if (event.m_Operation == UIDialogEvent.WINDOW_OK) {
						// onChooseAddAssignOK();
					}
				}
			});
		}
		return addAssignDialog;
	}

	private CopySendDialog getCopySendDialog() {
		if (copySendDialog == null) {
			copySendDialog = new CopySendDialog(this, getPanelCopySenders());
		}
		return copySendDialog;
	}

	/*
	 * private TransferUserDialog getTransferDialog() { if (transferDialog ==
	 * null) { transferDialog = new TransferUserDialog(this); // 选择改派人完毕
	 * transferDialog.addUIDialogListener(new UIDialogListener() {
	 * 
	 * @Override public void dialogClosed(UIDialogEvent event) {
	 * onChooseTransferOK(); } }); } return transferDialog; }
	 */

	private FlowStateRefDlg getRejectDialog() {
		if (rejectDlg == null) {
			rejectDlg = new FlowStateRefDlg(this, worknoteVO, isInWorkflow);

			// 选择驳回人完毕
			rejectDlg.addUIDialogListener(new UIDialogListener() {
				@Override
				public void dialogClosed(UIDialogEvent event) {
					if (event.m_Operation == UIDialogEvent.WINDOW_OK) {
						onChooseRejectOK();
					}
				}
			});
		}
		return rejectDlg;

	}

	private FlowStateDlg getFlowStateDlg() {
		if (viewMoreDialog == null) {
			viewMoreDialog = new FlowStateDlg(this,
					worknoteVO.getPk_billtype(), worknoteVO.getBillVersionPK(),
					isInWorkflow ? WorkflowTypeEnum.Workflow.getIntValue()
							: WorkflowTypeEnum.Approveflow.getIntValue());
		}
		return viewMoreDialog;
	}

	private DispatchDialog getDispatchDialog() {
		if (dispatchDialog == null) {
			dispatchDialog = new DispatchDialog(this);
		}
		return dispatchDialog;
	}

	/**
	 * 抄送的panel
	 * */
	private CpySendUserTree2List getCpySendPanel() {
		if (copySendPanel == null) {
			copySendPanel = new CpySendUserTree2List(getRoleUserParaVO());
			copySendPanel.getRoleUserTree().setRootVisible(false);
		}
		return copySendPanel;
	}

	// 提供加签，改派，抄送面板初始化的参数
	private RoleUserParaVO getRoleUserParaVO() {
		RoleUserParaVO paravo = new RoleUserParaVO();
		paravo.setCorppk(PfUtilUITools.getLoginGroup());
		SQLParameter sqlParam = new SQLParameter();
		sqlParam.addParam(PfUtilUITools.getLoginGroup());
		ArrayList<UserVO> users = null;
		users = (ArrayList<UserVO>) DBCacheQueryFacade.runQuery(
				"select * from sm_user where pk_group =?", sqlParam,
				new BeanListProcessor(UserVO.class));
		if (users != null && users.size() > 0)
			paravo.setRoleuservos(OrganizeUnit.fromUserVOs(users
					.toArray(new UserVO[0])));
		paravo.setShowCorp(false);
		paravo.setSelectRole(false);
		return paravo;
	}

	/**
	 * 显示流程信息 <li>可能为审批流 <li>也可能为工作流
	 */
	protected void showFlowinfo() {
		getFlowStateDlg().setVisible(true);
		// getPanelRejectFlow().setVisible(true);
	}

	private void copySend() {
		// @modifier yanke1 2011-7-15 设置抄送人信息
		// 无论是否批准都进行抄送
		worknoteVO.setMailExtCpySenders(getCpySendPanel().getMailVOs());
		worknoteVO.setMsgExtCpySenders(getCpySendPanel().getMsgVOs());
	}

	/**
	 * 审批通过或不通过
	 */
	protected void onApprove(boolean pass) {

		// 不批准
		boolean isNeedDispatch = pass ? isExistAssignableInfoWhenPass()
				: isExistAssignableInfoWhenNopass();
		if (isNeedDispatch) {
			// 填充指派信息
			getDispatchDialog().initByWorknoteVO(
					worknoteVO,
					pass ? AssignableInfo.CRITERION_PASS
							: AssignableInfo.CRITERION_NOPASS);
			int result = getDispatchDialog().showModal();
			if (result == UIDialog.ID_CANCEL) {
				// 如果指派对话框点击了取消，那么取消这次审批操作，重新回到审批面板 （changlx需求定义）modified by
				// zhangrui 2012-04-17
				return;
			}
		}
		if (!beforeButtonOperate())
			return;

		// yanke1+ 2011-7-15 设置当前审批人是否对流程进行跟踪
		boolean isTrack = chkTrack.isSelected();
		worknoteVO.setTrack(isTrack);

		String checkNote = getTxtNote().getText();
		if (StringUtil.isEmpty(checkNote)) {
			checkNote = pass ? getPassCheckNote() : getNoPassCheckNote();
		}
		worknoteVO.setChecknote(checkNote);
		worknoteVO.setApproveresult(UFBoolean.valueOf(pass).toString());
		this.closeOK();
	}

	/**
	 * 不批准时，是否存在可指派的后继活动
	 * 
	 * @return
	 */
	private boolean isExistAssignableInfoWhenNopass() {
		if (worknoteVO.getActiontype().endsWith(
				WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX))
			return false;

		Vector<AssignableInfo> assignInfos = worknoteVO.getTaskInfo()
				.getAssignableInfos();
		if (assignInfos != null && assignInfos.size() > 0) {
			String strCriterion = null;
			for (AssignableInfo ai : assignInfos) {
				strCriterion = ai.getCheckResultCriterion();
				if (AssignableInfo.CRITERION_NOTGIVEN.equals(strCriterion)
						|| AssignableInfo.CRITERION_NOPASS.equals(strCriterion))
					return true;
			}
		}
		return false;
	}

	/**
	 * 批准时，是否存在可指派的后继活动
	 * 
	 * @return
	 */
	private boolean isExistAssignableInfoWhenPass() {
		if (worknoteVO.getActiontype().endsWith(
				WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX))
			return false;

		Vector<AssignableInfo> assignInfos = worknoteVO.getTaskInfo()
				.getAssignableInfos();
		if (assignInfos != null && assignInfos.size() > 0) {
			String strCriterion = null;
			for (AssignableInfo ai : assignInfos) {
				strCriterion = ai.getCheckResultCriterion();
				if (AssignableInfo.CRITERION_NOTGIVEN.equals(strCriterion)
						|| AssignableInfo.CRITERION_PASS.equals(strCriterion))
					return true;
			}
		}
		return false;
	}

	public void setShowPass(boolean isShow) {
		// 设置审批按钮可用性
		radioApprove.setEnabled(isShow);
		if (isShow) {
			// 选中审批页签
			// radioApprove.setSelected(true);
			condition = STATUS_APPROVE;
			setStatus(condition);
			// tabPane.setSelectedIndex(STATUS_APPROVE);
		}
	}

	public void setShowNoPass(boolean isShow) {
		// 隐藏不批准
		if (!isShow) {
			buttonGroup.remove(getRadioUnApprove());
			radioPanel.remove(getRadioUnApprove());
		}
	}

	public void setShowReject(boolean isShow) {
		// if(isStatusShowOnTab(STATUS_REJECT)) {
		// 设置驳回页签可用性
		// tabPane.setEnabledAt(getTabIndexByStatus(STATUS_REJECT), isShow);
		radioReject.setEnabled(isShow);
		if (isShow) {
			// 选中驳回页签
			// radioReject.setSelected(true);
			condition = STATUS_REJECT;
			setStatus(condition);
			// tabPane.setSelectedIndex(STATUS_REJECT);
		}
		// }
	}

	/**
	 * 当前是否处于某个状态
	 * 
	 * @param status
	 * @return
	 */
	public boolean isCurrentInStatus(int status) {
		// int tabIndex = getTabIndexByStatus(status);
		// return tabPane.getSelectedIndex() == tabIndex;
		return condition == status;
	}

	private UITextArea getCurrentTextArea() {
		// if(isCurrentInStatus(STATUS_APPROVE)) {
		// return getTxtApproveNote();
		// } else if(isCurrentInStatus(STATUS_REJECT)) {
		// return getTxtRejectNote();
		// } else if(isCurrentInStatus(STATUS_TRANSFER)) {
		// return getTxtTransferNote();
		// } else if(isCurrentInStatus(STATUS_ADDASSIGN)) {
		// return getTxtAddAssignNote();
		// }
		// return getTxtApproveNote();
		return getTxtNote();
	}

	private String getCurrentNote() {
		if (isCurrentInStatus(STATUS_APPROVE)) {
			return getApproveNote();
		} else if (isCurrentInStatus(STATUS_UNAPPROVE)) {
			return getNoPassCheckNote();
		} else if (isCurrentInStatus(STATUS_REJECT)) {
			return getRejectCheckNote();
		} else if (isCurrentInStatus(STATUS_TRANSFER)) {
			return NCLangRes.getInstance().getStrByID("approveworkitem",
					"ApproveWorkitemAcceptDlg-0001")/* 改派至 */;
		} else if (isCurrentInStatus(STATUS_ADDASSIGN)) {
			return NCLangRes.getInstance().getStrByID("approveworkitem",
					"ApproveWorkitemAcceptDlg-0002")/* 加签至 */;
		}
		return "";
	}

	private String getApproveNote() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"pfworkflow61_0", "0pfworkflow61-0008")/* @res "请输入审批批语。默认为：" */
				+ getPassCheckNote();
	}

	/**
	 * 设置当前的预置批语
	 * 
	 * @param noteMessage
	 */
	public void setCheckNote(String noteMessage) {
		String note = getCurrentNote();

		String checkNote = note;
		if (!StringUtil.isEmptyWithTrim(hintMessage))
			checkNote = note + "\n" + ApproveLangUtil.getHintTo() + " : "
					+ hintMessage;
		getCurrentTextArea().setText(checkNote);
	}

	private String getPassCheckNote() {
		if (passCheckNote == null) {
			passCheckNote = getIndividualCheckNote(PfChecknoteEnum.PASS);
			if (passCheckNote == null)
				passCheckNote = "";
		}
		return passCheckNote;
	}

	private String getNoPassCheckNote() {
		if (nopassCheckNote == null) {
			nopassCheckNote = getIndividualCheckNote(PfChecknoteEnum.NOPASS);
			if (nopassCheckNote == null)
				nopassCheckNote = "";
		}
		return nopassCheckNote;
	}

	private String getRejectCheckNote() {
		if (rejectCheckNote == null) {
			rejectCheckNote = getIndividualCheckNote(PfChecknoteEnum.REJECT);
			if (rejectCheckNote == null)
				rejectCheckNote = "";
		}
		return rejectCheckNote;
	}

	public void setHintMessage(String hintMessage) {
		this.hintMessage = hintMessage;
	}

	/**
	 * 查找个性化中心设置的批语，如果未设置，则采用系统预置的。
	 * 
	 * @param type
	 *            批语类型
	 * @return 批语
	 * */
	private String getIndividualCheckNote(PfChecknoteEnum type) {
		if (checkNoteVO != null) {
			for (PfChecknoteVO vo : checkNoteVO) {
				if (vo.getNotetype() == type.toInt())
					return vo.getNote();
			}
		}
		return type.toString();
	}

	private List<PfChecknoteVO> initIndividualCheckNote() {
		String sqlCond = "select * from pub_wf_checknote where pk_user in(?,?)";
		SQLParameter sqlParam = new SQLParameter();
		sqlParam.addParam(PfUtilUITools.getLoginUser());
		sqlParam.addParam("SYSTEM");
		sqlCond += " order by pk_user";// system肯定排在后面？
		ArrayList<PfChecknoteVO> notes = (ArrayList<PfChecknoteVO>) DBCacheQueryFacade
				.runQuery(sqlCond, sqlParam, new BeanListProcessor(
						PfChecknoteVO.class));
		return notes;
	}

	public boolean isAutoCloseParentDialog() {
		return getChkAutoClose().isSelected();
	}

	// private void closeParentFuncletWindow() {
	// boolean isAutoClose = getChkAutoClose().isSelected();
	//
	// if (isOpenedInDialog && isAutoClose) {
	// parentFuncletWindow.closeWindow();
	// }
	// }

	// // yanke1+ 2011-9-8
	// // 点击ok时被调用
	// // 首先判断单据界面是否为对话框（即是否由消息中心打开）
	// // 若为对话框则一并关闭单据界面
	// @Override
	// public void closeOK() {
	//
	// super.closeOK();
	// closeParentFuncletWindow();
	// }

	/**
	 * 以‘取消’模式关闭对话框 业务节点根据需要修改
	 */
	@Override
	public void closeCancel() {
		setResult(ID_CANCEL);
		close();
		fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_CANCEL));
		return;
	}

	public void setStatus(int status) {
		// getChkAutoClose().setVisible(isOpenedInDialog);

		if (status == STATUS_APPROVE) {// 批准STATUS_APPROVE
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 0));
			radioApprove.setSelected(true);
		} else if (status == STATUS_UNAPPROVE) {// 不批准STATUS_UNAPPROVE
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 0));
			radioUnApprove.setSelected(true);
		} else if (status == STATUS_REJECT) {// 驳回STATUS_REJECT
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 32));
			radioReject.setSelected(true);
		} else if (status == STATUS_TRANSFER) {// 改派STATUS_TRANSFER
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 32));
			radioTransfer.setSelected(true);
		} else if (status == STATUS_ADDASSIGN) {// 改派STATUS_ADDASSIGN
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 32));
			radioAddAssign.setSelected(true);
		}

		int extendHight = (int) getRefPanel().getPreferredSize().getHeight()
				+ (int) (getTailPanel().getPreferredSize().getHeight())
				+ (int) (getPanelCopySenders().getPreferredSize().getHeight())
				+ (int) (getPanelAttach().getPreferredSize().getHeight());
		setSize(500, 150 + extendHight);// +25 file

		if (getRefContent() != null)
			getRefPanel().remove(getRefContent());
		condition = status;
		if (getRefContent() != null)
			getRefPanel().add(getRefContent());

		// getRefPanel().updateUI();
		getRefPanel().repaint();
		// getTxtNote().updateUI();//txt，updateUI()会造成边框去不掉
		getTxtNote().repaint();
		// getTailPanel().updateUI();
		getTailPanel().repaint();

		validate();
		invalidate();
	}

	private void setWfInstanceIsTracked(String pk_wf_instance, String supervisor) {
		try {
			isAllreadyTracked = new WorkflowManageUtil().checkIsAlreadyTracked(
					pk_wf_instance, supervisor);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	private UIPanel getPanelDispatch() {
		if (pnlDispatch == null) {
			pnlDispatch = new UIPanel(new FlowLayout());
			pnlDispatch.add(getLblDispatchTo(), FlowLayout.LEFT);
			pnlDispatch.add(getPaneDispatchPerson(), FlowLayout.CENTER);
		}
		return pnlDispatch;
	}

	private UILabel getLblDispatchTo() {
		if (lblDipatchTo == null) {
			lblDipatchTo = new UILabel(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pfworkflow61_0",
							"0pfworkflow61-0010")/* @res "指派至" */);
			lblDipatchTo.setName("lblDipatchTo");
		}
		return lblDipatchTo;
	}

	private TextListPane getPaneDispatchPerson() {
		if (paneDispatchPerson == null) {
			paneDispatchPerson = new TextListPane();
			paneDispatchPerson.setName("paneDispatchPerson");
		}
		return paneDispatchPerson;
	}

	/**
	 * 上传附件到文档服务器。上传失败，不影响审批流程
	 * */
	private List<AttachmentVO> updateAttachment2DocServer() {
		List<AttachmentVO> vos = new ArrayList<AttachmentVO>();
		try {
			for (Attachment attachment : attchlist) {
				AttachmentVO vo = attachment.uploadToFileServer();
				vo.setFilesize(attachment.getSize());
				vo.setFilename(attachment.getName());
				vos.add(vo);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage() + "上传附件失败！");
		}
		return vos;
	}

	private void addAttachPane(File[] files) {
		if (files != null && files.length > 0) {
			for (File file : files) {
				Attachment attch = new FileAttachment(file);
				addAttachPane(attch);
			}
		}
		getPanelAttach().setPreferredSize(new Dimension(getWidth(), 25));
		getPanelAttach().updateUI();
	}

	private void addAttachPane(Attachment attch) {
		getPanelAttach().addNewBlock(attch.getName(), attch);
		attchlist.add(attch);
	}

	private UIRefPane getRefRemark() {
		if (refRemark == null) {
			refRemark = new UIRefPane();
			refRemark.setName("refRemark");
			refRemark.setVisible(false);
			String remarkRefName = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pfworkflow61_0", "0pfworkflow61-0011")/*
																		 * @res
																		 * "常用摘要"
																		 */;
			refRemark.setRefNodeName(remarkRefName);
		}
		return refRemark;
	}

	// 用来设置当审批完成后是否自动关闭单据界面
	private UICheckBox getChkAutoClose() {
		if (chkAutoClose == null) {
			chkAutoClose = new UICheckBox();
			chkAutoClose.setOpaque(false);
			chkAutoClose.setName("chkAutoClose");
			chkAutoClose.setSelected(DEFAULT_CLOSE_AFTER_APPROVE);

			chkAutoClose.setText(ApproveLangUtil.getCloseBillAfterApprove());
			// int w = chkAutoClose.getFontMetrics(chkAutoClose.getFont())
			// .stringWidth(chkAutoClose.getText());
			//
			// chkAutoClose.setPreferredSize(new Dimension(w + 32, chkAutoClose
			// .getSize().height));
		}
		return chkAutoClose;
	}

	private JFileChooser getChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			// 文件选择方式,只能选择文件
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// 是否提供"所有文件"过滤
			fileChooser.setAcceptAllFileFilterUsed(true);
			// 设置文件过滤器
			fileChooser.setFileFilter(FileFilterFactoryAdapter
					.suffixFileFilter(new String[] { ".doc", ".txt", ".pdf",
							".xls" }));
			fileChooser.setApproveButtonText(ApproveLangUtil.getUploadAttach());
			fileChooser.setMultiSelectionEnabled(true);
		}
		return fileChooser;
	}

	private BlockListPanel getPanelCopySenders() {
		if (pnlCopySenders == null) {
			pnlCopySenders = new BlockListPanel(ApproveLangUtil.getCopySend());
			pnlCopySenders.setBackground(background);
			pnlCopySenders.setPreferredSize(new Dimension(getWidth(), 0));
			pnlCopySenders.setName("pnlCopySenders");
			// pnlCopySenders.setBorder(/*BorderFactory.createEmptyBorder(0, 10,
			// 0, 0)*/BorderFactory.createLineBorder(Color.PINK));

			totalSpring.putConstraint(SpringLayout.NORTH, pnlCopySenders, 0,
					SpringLayout.SOUTH, getOperationsPanel());
			totalSpring.putConstraint(SpringLayout.WEST, pnlCopySenders, 30,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, pnlCopySenders, 0,
					SpringLayout.EAST, getConfigInfo());
			// totalSpring.putConstraint(SpringLayout.SOUTH, pnlCopySenders, 0,
			// SpringLayout.NORTH, pnlCopySenders);//25
		}
		return pnlCopySenders;
	}

	private BlockListPanel getPanelAttach() {
		if (pnlAttachs == null) {
			pnlAttachs = new BlockListPanel(ApproveLangUtil.getAttach());
			pnlAttachs.setBackground(background);
			pnlAttachs.setName("pnlAttachs");
			pnlAttachs.setPreferredSize(new Dimension(getWidth(), 0));
			// pnlAttachs.setBorder(/*BorderFactory.createEmptyBorder(0,10,0,0)*/BorderFactory.createLineBorder(Color.green));

			totalSpring.putConstraint(SpringLayout.NORTH, pnlAttachs, 0,
					SpringLayout.SOUTH, getPanelCopySenders());
			totalSpring.putConstraint(SpringLayout.WEST, pnlAttachs, 20,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, pnlAttachs, 0,
					SpringLayout.EAST, getConfigInfo());
			// totalSpring.putConstraint(SpringLayout.SOUTH, pnlAttachs, 25,
			// SpringLayout.NORTH, pnlAttachs);//25
		}
		return pnlAttachs;
	}

	/**
	 * 改派的 panel
	 */
	/*
	 * private AppointUserPanel getTransferUserPanel() { if (transferPanel ==
	 * null) { transferPanel = new AppointUserPanel(getRoleUserParaVO()); //
	 * transferPanel.getRoleUserTree().setRootVisible(false); } return
	 * transferPanel; }
	 */

	private UIPanel getOperationsPanel() {
		if (operationsPanel == null) {
			operationsPanel = new UIPanel();
			// operationsPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
			operationsPanel.setBackground(background);
			operationSpring = new SpringLayout();
			operationsPanel.setLayout(operationSpring);
			operationsPanel.setName("operationsPanel");

			operationsPanel.add(getChkTrack());
			// panelContent.add(getChkSubmitToRejectTache());
			operationsPanel.add(getLblCopySend());
			operationsPanel.add(getLblAddAttach());

			totalSpring.putConstraint(SpringLayout.NORTH, operationsPanel, 0,
					SpringLayout.SOUTH, getTxtPane());
			totalSpring.putConstraint(SpringLayout.WEST, operationsPanel, 0,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, operationsPanel, 0,
					SpringLayout.EAST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.SOUTH, operationsPanel, 25,
					SpringLayout.NORTH, operationsPanel);
		}
		return operationsPanel;
	}

	private ImageIcon getIconCopySend() {
		if (iconCopySend == null) {
			iconCopySend = ThemeResourceCenter.getInstance().getImage(
					"themeres/control/approve/copysend.png");
		}
		return iconCopySend;
	}

	private ImageIcon getIconAttach() {
		if (iconAttach == null) {
			iconAttach = ThemeResourceCenter.getInstance().getImage(
					"themeres/control/approve/close.png");
		}
		return iconAttach;
	}

	private UICheckBox getChkTrack() {
		if (chkTrack == null) {
			chkTrack = new UICheckBox(ApproveLangUtil.getTrack());
			chkTrack.setOpaque(false);
			chkTrack.setName("chkTrack");
			setWfInstanceIsTracked(worknoteVO.getTaskInfo().getTask()
					.getWfProcessInstancePK(), PfUtilUITools.getLoginUser());
			chkTrack.setSelected(isAllreadyTracked);

			int w = chkTrack.getFontMetrics(chkTrack.getFont()).stringWidth(
					chkTrack.getText());
			chkTrack.setPreferredSize(new Dimension(w + 32, 22));

			operationSpring.putConstraint(SpringLayout.NORTH, chkTrack, 0,
					SpringLayout.NORTH, getOperationsPanel());
			operationSpring.putConstraint(SpringLayout.WEST, chkTrack, 7,
					SpringLayout.WEST, getOperationsPanel());
		}
		return chkTrack;
	}

	private UILabel getLblCopySend() {
		if (lblCopySend == null) {
			lblCopySend = createLinkButton(ApproveLangUtil.getCopySend(),
					getIconCopySend());
			lblCopySend.setName("copySend");

			operationSpring.putConstraint(SpringLayout.NORTH, lblCopySend, 2,
					SpringLayout.NORTH, getOperationsPanel());
			operationSpring.putConstraint(SpringLayout.WEST, lblCopySend, 9,
					SpringLayout.EAST, getChkTrack());
		}
		return lblCopySend;
	}

	private UILabel getLblAddAttach() {
		if (lblAddAttach == null) {
			lblAddAttach = createLinkButton(ApproveLangUtil.getAddAttach(),
					getIconAttach());
			lblAddAttach.setName("lblAddAttach");

			operationSpring.putConstraint(SpringLayout.NORTH, lblAddAttach, 2,
					SpringLayout.NORTH, getOperationsPanel());
			operationSpring.putConstraint(SpringLayout.WEST, lblAddAttach, 16,
					SpringLayout.EAST, getLblCopySend());
		}
		return lblAddAttach;
	}

	/**
	 * 得到驳回面板
	 * 
	 * @return
	 */
	private UIPanel getPanelRejectRef() {
		if (pnlReject == null) {
			pnlReject = new UIPanel();
			pnlReject.setBackground(background);
			pnlReject.setName("pnlReject");
			rejectRefSpring = new SpringLayout();
			pnlReject.setLayout(rejectRefSpring);
			pnlReject.add(getLblReject());
			pnlReject.add(getPaneRejectPerson());
		}
		return pnlReject;
	}

	private UILabel getLblReject() {
		if (lblReject == null) {
			lblReject = new UILabel(NCLangRes.getInstance().getStrByID(
					"approveworkitem", "ApproveWorkitemAcceptDlg-0003")/* 驳回至 */);
			rejectRefSpring.putConstraint(SpringLayout.NORTH, lblReject, 8,
					SpringLayout.NORTH, getPanelRejectRef());
			rejectRefSpring.putConstraint(SpringLayout.WEST, lblReject, 25,
					SpringLayout.WEST, getPanelRejectRef());
		}
		return lblReject;
	}

	private TextListPane getPaneRejectPerson() {
		if (paneRejectPerson == null) {
			paneRejectPerson = new TextListPane();
			// paneRejectPerson.setPreferredSize(new Dimension(322, 22));
			rejectRefSpring.putConstraint(SpringLayout.NORTH, paneRejectPerson,
					5, SpringLayout.NORTH, getPanelRejectRef());
			rejectRefSpring.putConstraint(SpringLayout.WEST, paneRejectPerson,
					10, SpringLayout.EAST, getLblReject());

			paneRejectPerson.setActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getRejectDialog().setVisible(true);
				}
			});
		}
		return paneRejectPerson;
	}

	/*
	 * private ApproveFlowRejectPanel getPanelRejectFlow() { if (rejectPanel ==
	 * null) { String processInstancePK = null; if (isInWorkflow) {
	 * processInstancePK = worknoteVO.getTaskInfo().getTask()
	 * .getWfProcessInstancePK(); } rejectPanel = new ApproveFlowRejectPanel(
	 * worknoteVO.getBillVersionPK(), worknoteVO.getPk_billtype(),
	 * processInstancePK, WorkflowTypeEnum.Approveflow.getIntValue()); } return
	 * rejectPanel; }
	 */

	private UIScrollPane getTxtPane() {
		if (txtPane == null) {
			txtPane = new UIScrollPane();
			txtPane.setBackground(background);
			txtPane.setViewportView(getTxtNote());
			// txtPane.setBorder(BorderFactory.createLineBorder(Color.red));
			totalSpring.putConstraint(SpringLayout.NORTH, txtPane, 0,
					SpringLayout.SOUTH, getRefPanel());
			totalSpring.putConstraint(SpringLayout.WEST, txtPane, 8,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, txtPane, -8,
					SpringLayout.EAST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.SOUTH, txtPane, 45,
					SpringLayout.NORTH, txtPane);
		}
		return txtPane;
	}

	UITextArea txtNote = null;

	private UITextArea getTxtNote() {
		if (txtNote == null) {
			txtNote = new UITextArea();
			txtNote.setName("txtNote");
			txtNote.setLineWrap(true);
			txtNote.setMaxLength(1024);
			txtNote.setOpaque(false);
		}
		txtNote.setHitStr(getCurrentNote());
		return txtNote;
	}

	/**
	 * 得到改派面板
	 * 
	 * @return
	 */

	private UIPanel getPnlTransferRef() {
		if (pnlTransferRef == null) {
			pnlTransferRef = new UIPanel();
			pnlTransferRef.setBackground(background);
			transferRefSpring = new SpringLayout();
			pnlTransferRef.setLayout(transferRefSpring);
			pnlTransferRef.add(getLblTransfer());
			// pnlTransferRef.add(getPaneTransferPerson());
			pnlTransferRef.add(getPaneTransferPersonRef());
		}
		return pnlTransferRef;
	}

	private UILabel getLblTransfer() {
		if (lblTransfer == null) {
			lblTransfer = new UILabel(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pfworkflow61_0",
							"0pfworkflow61-0013")/* @res "改派至" */);
			transferRefSpring.putConstraint(SpringLayout.NORTH, lblTransfer, 8,
					SpringLayout.NORTH, getPnlTransferRef());
			transferRefSpring.putConstraint(SpringLayout.WEST, lblTransfer, 25,
					SpringLayout.WEST, getPnlTransferRef());
		}
		return lblTransfer;
	}

	private UIRefPane personRef = null;

	private UIRefPane getPaneTransferPersonRef() {
		if (personRef == null) {
			personRef = new UIRefPane(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pfworkflow63",
							"0pfworkflow630029")/* @res "用户" */);/*
																 * NCLangRes.
																 * getInstance
																 * ().
																 * getStrByID(
																 * "approveworkitem"
																 * ,
																 * "ApproveWorkitemAcceptDlg-0004"
																 * )
																 */
			UserWithClassRefModel refModel = new UserWithClassRefModel();
			refModel.setShowGroupAdmin(false);
			refModel.setShowSysAdmin(false);
			refModel.setMutiGroup(true);
			personRef.setRefModel(refModel);
			personRef.setMultiCorpRef(true);
			personRef.setMultiSelectedEnabled(false);
			personRef.setPreferredSize(new Dimension(322, 25));
			transferRefSpring.putConstraint(SpringLayout.NORTH, personRef, 5,
					SpringLayout.NORTH, getPnlTransferRef());
			transferRefSpring.putConstraint(SpringLayout.WEST, personRef, 10,
					SpringLayout.EAST, getLblTransfer());
		}
		return personRef;
	}

	/**
	 * 得到加签面板
	 * 
	 * @return
	 */

	private UIPanel getPnlAddAssignRef() {
		if (pnlAddAssignRef == null) {
			pnlAddAssignRef = new UIPanel();
			pnlAddAssignRef.setBackground(background);
			addAssignRefSpring = new SpringLayout();
			pnlAddAssignRef.setLayout(addAssignRefSpring);
			pnlAddAssignRef.add(getLblAddAssign());
			pnlAddAssignRef.add(getPaneAddAssignPerson());
		}
		return pnlAddAssignRef;
	}

	private UIRefPane getPaneAddAssignPerson() {
		if (paneAddAssignPerson == null) {
			paneAddAssignPerson = new UIRefPane();
			paneAddAssignPerson.setPreferredSize(new Dimension(322, 22));
			// paneAddAssignPerson.setBorder(border)
			addAssignRefSpring.putConstraint(SpringLayout.NORTH,
					paneAddAssignPerson, 5, SpringLayout.NORTH,
					getPnlAddAssignRef());
			addAssignRefSpring.putConstraint(SpringLayout.WEST,
					paneAddAssignPerson, 10, SpringLayout.EAST,
					getLblAddAssign());

			UserWithClassRefModel refModel = new UserWithClassRefModel();
			refModel.setShowGroupAdmin(false);
			refModel.setShowSysAdmin(false);
			refModel.setMutiGroup(true);
			paneAddAssignPerson.setRefModel(refModel);
			paneAddAssignPerson.setMultiCorpRef(true);
			paneAddAssignPerson.setMultiSelectedEnabled(true);
			// // 选择加签人
			// paneAddAssignPerson.setActionListener(new ActionListener() {
			// @Override
			// public void actionPerformed(ActionEvent e) {
			// getAddAssignDialog().showModal();
			// }
			// });
		}
		return paneAddAssignPerson;
	}

	private UILabel getLblAddAssign() {
		if (lblAddAssign == null) {
			lblAddAssign = new UILabel(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pfworkflow61_0",
							"0pfworkflow61-0014")/* @res "加签至" */);
			addAssignRefSpring.putConstraint(SpringLayout.NORTH, lblAddAssign,
					8, SpringLayout.NORTH, getPnlAddAssignRef());
			addAssignRefSpring.putConstraint(SpringLayout.WEST, lblAddAssign,
					25, SpringLayout.WEST, getPnlAddAssignRef());
		}
		return lblAddAssign;
	}

	/**
	 * 得到链接按钮，目前由Label实现
	 * 
	 * @param text
	 * @return
	 */
	private UILabel createLinkButton(String text, Icon icon) {
		UILabel label = new UILabel(text, null, JLabel.LEFT);
		if (icon != null) {
			label.setIconTextGap(6);
		}
		label.setHyperlinkLabel(true);
		label.setSize(label.getFont().getSize() * text.length() + 10, label
				.getFont().getSize());
		return label;
	}

	/*
	 * private UILabel getLinkViewFlow() { if(lblViewFlow == null) { //查看审批流
	 * lblViewFlow =
	 * createLinkButton(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().
	 * getStrByID("pfworkflow61_0","0pfworkflow61-0015")@res "查看审批流", null); }
	 * return lblViewFlow; }
	 */

	private UIPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new UIPanel();
			btnPanel.setBackground(background);
			SpringLayout btnPanelSpring = new SpringLayout();
			btnPanel.setLayout(btnPanelSpring);

			// pnlBottom.add(getChkAutoClose(), BorderLayout.CENTER);
			btnPanel.add(getBtnViewFlow());

			if (isOpenedInDialog) {
				btnPanel.add(getChkAutoClose());
			}

			btnPanel.add(getBtnOK());
			btnPanel.add(getBtnCancel());
			btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0,
					new Color(166, 166, 166)));

			// btnPanel.setBorder(BorderFactory.createLineBorder(Color.red));

			totalSpring.putConstraint(SpringLayout.NORTH, btnPanel, 5,
					SpringLayout.SOUTH, getPanelAttach());
			totalSpring.putConstraint(SpringLayout.WEST, btnPanel, 0,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, btnPanel, 13,
					SpringLayout.EAST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.SOUTH, btnPanel, 40,
					SpringLayout.NORTH, btnPanel);

			btnPanelSpring.putConstraint(SpringLayout.NORTH, btnViewFlow, 9,
					SpringLayout.NORTH, getBtnPanel());
			btnPanelSpring.putConstraint(SpringLayout.WEST, btnViewFlow, 9,
					SpringLayout.WEST, getBtnPanel());

			if (isOpenedInDialog) {
				btnPanelSpring.putConstraint(SpringLayout.WEST,
						getChkAutoClose(), 5, SpringLayout.EAST,
						getBtnViewFlow());

				int height1 = getBtnViewFlow().getPreferredSize().height;
				int height2 = getChkAutoClose().getPreferredSize().height;
				int pad = (height1 - height2) / 2;
				pad = pad > 0 ? pad : 0;
				btnPanelSpring.putConstraint(SpringLayout.NORTH,
						getChkAutoClose(), pad, SpringLayout.NORTH,
						getBtnViewFlow());
			}
			btnPanelSpring.putConstraint(SpringLayout.NORTH, btnOK, 9,
					SpringLayout.NORTH, getBtnPanel());
			btnPanelSpring.putConstraint(SpringLayout.EAST, btnOK, -20,
					SpringLayout.WEST, getBtnCancel());
			btnPanelSpring.putConstraint(SpringLayout.NORTH, btnCancel, 9,
					SpringLayout.NORTH, getBtnPanel());
			btnPanelSpring.putConstraint(SpringLayout.EAST, btnCancel, -20,
					SpringLayout.EAST, getBtnPanel());
		}
		return btnPanel;
	}

	private JToggleButton getBtnViewFlow() {
		if (btnViewFlow == null) {
			btnViewFlow = new JToggleButton();
			btnViewFlow
					.setText(NCLangRes.getInstance().getStrByID(
							"approveworkitem", "ApproveWorkitemAcceptDlg-0005")/* 历史审批 */);
			btnViewFlow.setIcon(new ImageIcon(getClass().getResource(
					"/images/message/dispquery.png")));
			btnViewFlow.setPreferredSize(new Dimension((int) btnViewFlow
					.getPreferredSize().getWidth() - 13, 19));
			btnViewFlow.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnViewFlow.setUI(new UIButton().getUI());
			btnViewFlow.setSelected(true);
			btnViewFlow.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
		}
		return btnViewFlow;
	}

	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton(ApproveLangUtil.getOK());
			btnOK.setPreferredSize(new Dimension(75, 20));
		}
		return btnOK;
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton(ApproveLangUtil.getCancel());
			btnCancel.setPreferredSize(new Dimension(75, 20));
		}
		return btnCancel;
	}

	private UIPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new UIPanel();
			bottomPanel.setBackground(background);
			bottomPanel.setLayout(new BorderLayout());
			bottomPanel.add(getTailPanel(), BorderLayout.CENTER);

			totalSpring.putConstraint(SpringLayout.NORTH, bottomPanel, 0,
					SpringLayout.SOUTH, getBtnPanel());
			totalSpring.putConstraint(SpringLayout.WEST, bottomPanel, -20,
					SpringLayout.WEST, getConfigInfo());
			totalSpring.putConstraint(SpringLayout.EAST, bottomPanel, 20,
					SpringLayout.EAST, getConfigInfo());
			// totalSpring.putConstraint(SpringLayout.SOUTH, bottomPanel, 100,
			// SpringLayout.NORTH, bottomPanel);
		}
		return bottomPanel;
	}

	private UIPanel getTailPanel() {
		if (tailPanel == null) {
			tailPanel = new UIPanel(new BorderLayout());
			tailPanel.setBackground(background);
			// tailPanel.setPreferredSize(new Dimension(getWidth(), 195));
			// tailPanel.setBorder(BorderFactory.createLineBorder(Color.red));
			tailPanel.add(getHintMessagePanel(), BorderLayout.NORTH);
			tailPanel.add(getTableScrollPanel(), BorderLayout.CENTER);
			tailPanel.add(getViewMoreLinkPanel(), BorderLayout.SOUTH);
		}
		return tailPanel;
	}

	private UIScrollPane getHintMessagePanel() {
		if (hintMessagePanel == null) {
			hintMessagePanel = new UIScrollPane();
			// hintMessage =
			// "预算金额提示：全面预算组织的执行数金额2,300.00>预算控制数700.00。\n已经超出金额，不允许再进行开支。";
			if (hintMessage != null) {
				hintMessagePanel.setViewportView(getHintMsgTxtAr());
				hintMessagePanel
						.setPreferredSize(new Dimension(getWidth(), 59));
				hintMessagePanel.setBorder(BorderFactory.createEmptyBorder(5,
						20, 0, 20));
			}
			// hintMessagePanel.setBorder(BorderFactory.createLineBorder(Color.red));
		}
		return hintMessagePanel;
	}

	private UIScrollPane getTableScrollPanel() {
		if (tableScrollPanel == null) {
			tableScrollPanel = new UIScrollPane();
			tableScrollPanel.setBackground(background);
			tableScrollPanel.setViewportView(getTablePanel());
			// tableScrollPanel.setViewportView(getTabelPnl());
			tableScrollPanel.setPreferredSize(new Dimension(getWidth(), 115));
			tableScrollPanel.setBorder(BorderFactory.createEmptyBorder(3, 20,
					0, 20));
		}
		return tableScrollPanel;
	}

	private UITextArea getHintMsgTxtAr() {
		if (hintMsgTxtAr == null) {
			hintMsgTxtAr = new UITextArea();
			hintMsgTxtAr.setEditable(false);
			hintMsgTxtAr.setLineWrap(true);
			hintMsgTxtAr.setOpaque(true);
			hintMsgTxtAr.setBackground(new Color(255, 226, 208));
			// hintMsgTxtAr.setBorder(BorderFactory.createEmptyBorder());
		}
		hintMsgTxtAr.setText(hintMessage);
		return hintMsgTxtAr;
	}

	private UITablePane getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new UITablePane();
			tablePanel.setBackground(background);
			// tablePanel.setBorder(BorderFactory.createLineBorder(Color.orange));
			UITable table = tablePanel.getTable();
			table.getTableHeader().setUI(new NCTableHeaderUI());
			table.getTableHeader().setPreferredSize(
					new Dimension(getWidth() - 50, 25));

			table.setModel(getTableModel());
			// 调整列宽
			table.setColumnWidth(getTableModel().getColumnWidth());
			table.getColumnModel().getColumn(0).setMaxWidth(15);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.addSortListener();
			tablePanel.setPreferredSize(new Dimension(getTableScrollPanel()
					.getWidth() - 50, 55));
		}
		return tablePanel;
	}

	private UIPanel getViewMoreLinkPanel() {
		if (pnlViewMore == null) {
			pnlViewMore = new UIPanel();
			pnlViewMore.setBackground(background);
			pnlViewMoreSpring = new SpringLayout();
			pnlViewMore.setLayout(pnlViewMoreSpring);
			// pnlViewMore.setLayout(new FlowLayout(FlowLayout.RIGHT));
			pnlViewMore.add(getViewMoreLinkBtn());
			pnlViewMore.setPreferredSize(new Dimension(getTableScrollPanel()
					.getWidth(), 21));
			// pnlViewMore.setBorder(BorderFactory.createEmptyBorder(0, 20, 0,
			// 20));
			// pnlViewMore.setBorder(BorderFactory.createLineBorder(Color.red));
		}
		return pnlViewMore;
	}

	private UILabel getViewMoreLinkBtn() {
		if (lblViewMore == null) {
			lblViewMore = createLinkButton(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"pfworkflow63", "0pfworkflow630083")/* @res "审批详情" */,
					null);
			pnlViewMoreSpring.putConstraint(SpringLayout.NORTH, lblViewMore, 0,
					SpringLayout.NORTH, getViewMoreLinkPanel());
			pnlViewMoreSpring.putConstraint(SpringLayout.EAST, lblViewMore,
					-23, SpringLayout.EAST, getViewMoreLinkPanel());
			// pnlViewMoreSpring.putConstraint(SpringLayout.SOUTH, lblViewMore,
			// 0, SpringLayout.SOUTH, getViewMoreLinkPanel());
			// lblViewMore.setBorder(BorderFactory.createLineBorder(Color.red));
		}
		return lblViewMore;
	}

	ApproveWorkitemAcceptTableModel model = null;

	public ApproveWorkitemAcceptTableModel getTableModel() {
		if (model == null) {
			model = new ApproveWorkitemAcceptTableModel();
		}
		return model;
	}

	ApproveWorkitemAcceptDataManage dataManager = null;// 用来获取数据

	public ApproveWorkitemAcceptDataManage getDataManager() {
		if (dataManager == null) {
			dataManager = new ApproveWorkitemAcceptDataManage(this,
					getTableModel());
		}
		return dataManager;
	}

	/*
	 * public UserVO[] findUserByIDs(String[] userPKs) throws BusinessException
	 * { IUserManageQuery_C userManageQuery_C = NCLocator.getInstance().lookup(
	 * IUserManageQuery_C.class);
	 * 
	 * UserVO[] users = userManageQuery_C.findUserByIDs(userPKs); return
	 * UserExVOUtil.setUserLockedState(users); }
	 */

	public OrganizeUnit[] findUserByIDs(String[] userPKs)
			throws BusinessException {
		if (!ArrayUtils.isEmpty(userPKs)) {
			String sqlWhere = getSelectWherePart(userPKs);
			SQLParameter sqlParam = new SQLParameter();
			sqlParam.addParam(sqlWhere);
			ArrayList<UserVO> users = null;
			/*
			 * users = (ArrayList<UserVO>) DBCacheQueryFacade.runQuery(
			 * "select * from sm_user where cuserid in (?)", sqlParam, new
			 * BeanListProcessor(UserVO.class));
			 */
			users = (ArrayList<UserVO>) DBCacheQueryFacade
					.runQuery("select * from sm_user where cuserid in ("
							+ sqlWhere + ")", new BeanListProcessor(
							UserVO.class));
			OrganizeUnit[] orgs = null;
			if (users != null && users.size() > 0)
				orgs = OrganizeUnit.fromUserVOs(users.toArray(new UserVO[0]));
			return orgs;
		}
		return null;
	}

	public String getSelectWherePart(String[] userPKs) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < userPKs.length - 1; i++) {
			sb.append("'" + userPKs[i] + "',");
		}
		sb.append("'" + userPKs[userPKs.length - 1] + "'");
		return sb.toString();
	};

	/**
	 * 用于设置本对话框初始化后默认选中的操作 主要用于消息中心快速审批栏
	 * 
	 * @author yanke1 2013-1-4
	 * 
	 */
	public static class Context {

		private static String defaultAction = null;

		public static String getDefaultAction() {
			return defaultAction;
		}

		/**
		 * @see MessageActionVO
		 */
		public static void setDefaultAction(String defaultAction) {
			Context.defaultAction = defaultAction;
		}

		public static void resetDefaultAction() {
			setDefaultAction(null);
		}

	}

	public ApproveFlowRejectPanel getRejectPanel() {
		return rejectPanel;
	}

	public void setRejectPanel(ApproveFlowRejectPanel rejectPanel) {
		this.rejectPanel = rejectPanel;
	}

}