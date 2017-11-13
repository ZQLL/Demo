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
 * �������Ĺ������Ի���
 * 
 * @author leijun 2009-9
 * @since 6.0
 * @Modifier zhouzhenga 2010-4 ������̵ĸ��ٺͳ���
 * @Modifier zhouzhenga 2011-1 ����֧����Ӹ���
 * @Modifier zhouzhenga 2011-1 ֧��Ԥ�õ�����
 * @Modifier wcj, yanke1 2011-7 ���١����͹��ܵ�ʵ��
 * @modifier yanke1 2011-9-8 ����changlx�������󣬵�����Ϣ���Ĵ򿪵��ݲ�����ʱ�������������Զ��رյ��ݽ���
 * @modifier zhangrui 2012-3-13 ������UEҪ��Դ˽����ع�
 * @modifier yanke1 2012-7-19 ���Զ��رյ��ݽ���Ĵ����Ƶ�����PfUtilClient����������ֻ�ṩ���湩�û�ѡ���Ƿ�ر�
 * 
 */
public class ApproveWorkitemAcceptDlg extends UIDialog implements
		ActionListener {

	private static final long serialVersionUID = -9104313190799304533L;

	public static final int STATUS_APPROVE = 0;// ��׼
	public static final int STATUS_UNAPPROVE = 1;// ����׼
	public static final int STATUS_REJECT = 2;// ����
	public static final int STATUS_TRANSFER = 3;// ����
	public static final int STATUS_ADDASSIGN = 4;// ��ǩ

	private final Color background = new Color(237, 237, 237);

	private WorkflownoteVO worknoteVO = null;
	private boolean isInWorkflow = false;
	private String hintMessage;

	TextListPane paneDispatchPerson = null;
	UILabel lblDipatchTo = null;
	UIPanel pnlDispatch = null;

	// update by lihaibo ����FlowStateRefDlg�� ��ֵ
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

	// �����������Ƿ�رյ��ݽ���
	UICheckBox chkAutoClose = null;

	// UILabel lblViewFlow = null;

	UIButton btnCancel = null;

	List<Attachment> attchlist = new ArrayList<Attachment>();

	// yanke1+ 2011-9-8
	// /** ���ݽڵ��funcletWindow */
	// yanke1 2012-7-18
	// private IFuncletWindow parentFuncletWindow = null;

	private boolean isOpenedInDialog = false;
	private boolean isAllreadyTracked = false;

	/** ��ǩ�Ի��� */
	private AddAssignDialog addAssignDialog = null;

	/* ���ɵ� �û�ѡ��panel */
	// private AppointUserPanel transferPanel;
	// private TransferUserDialog transferDialog;

	/* ���͵��û�ѡ��panel */
	private CpySendUserTree2List copySendPanel;
	private CopySendDialog copySendDialog;

	// ָ�ɵ����
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

	// ������ǵ�ǰ״̬
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
	 * ����
	 * 
	 * @param parent
	 * @param noteVO
	 *            ����������VO
	 * @param isInWorkflow
	 *            �Ƿ�Ϊ�����������������̵Ĺ�����
	 */
	public ApproveWorkitemAcceptDlg(Container parent, WorkflownoteVO noteVO,
			boolean isInWorkflow) {
		this(parent, noteVO, isInWorkflow, false);
	}

	/**
	 * ����
	 * 
	 * @param parent
	 * @param noteVO
	 *            ����������VO
	 * @param isInWorkflow
	 *            �Ƿ�Ϊ�����������������̵Ĺ�����
	 * @author yanke1 2012-7-18 ��Ӳ���isOpenedIndialog��
	 *         ����˲���Ϊtrue����ô�ڽ�������ʾ�û�������ɺ��Ƿ�رյ��ݽ���
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
		// ����һ���߳�,��ʼ�������
		CoreUtilities.dummyThread4Performance();
		// ��ʼ������
		// initialize();

		condition = STATUS_APPROVE;// ���ó�ʼ������������refpanel�ĳ�ʼ��,STATUS_TRANSFER/STATUS_ADDASSIGN

		initialize();

		attachEventListener();

		checkNoteVO = initIndividualCheckNote();// ��������note�ı���Ԥ��

		initData();

		// Ĭ��������ҳǩ
		setStatus(condition);
	}

	/**
	 * ����
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
	 * ��ʼ������ؼ�
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
			// ѡ����׼
			setStatus(STATUS_APPROVE);
		} else if (Context.getDefaultAction().equals(MessageActionVO.NO_PASS)) {
			// ѡ�в���׼
			setStatus(STATUS_UNAPPROVE);
		} else if (Context.getDefaultAction().equals(MessageActionVO.REJECT)) {
			// ѡ�в���
			setStatus(STATUS_REJECT);
		} else if (Context.getDefaultAction().equals(
				MessageActionVO.ADD_APPROVER)) {
			// ѡ�м�ǩ
			setStatus(STATUS_ADDASSIGN);
		} else if (Context.getDefaultAction().equals(MessageActionVO.TRANSFER)) {
			// ѡ�и���
			setStatus(STATUS_TRANSFER);
		}

	}

	private UIPanel getConfigInfo() {
		if (configPanel == null) {
			totalSpring = new SpringLayout();
			configPanel = new UIPanel();
			configPanel.setBackground(background);
			configPanel.setLayout(totalSpring);
			configPanel.add(getRadioPanel());// ��ѡť���
			configPanel.add(getRefPanel());// �������
			configPanel.add(getTxtPane());// �ı���������
			configPanel.add(getOperationsPanel());// �������
			configPanel.add(getPanelCopySenders());// ���������
			configPanel.add(getPanelAttach());// �������
			configPanel.add(getBtnPanel());// ��ť���
			configPanel.add(getBottomPanel());// �ײ���庬�ı������
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
		case STATUS_APPROVE:// ��׼
			return null;
		case STATUS_UNAPPROVE:// ����׼
			return null;
		case STATUS_REJECT:// ����
			return getPanelRejectRef();
		case STATUS_TRANSFER:// ����
			return getPnlTransferRef();
		case STATUS_ADDASSIGN:// ��ǩ
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

			// �ж��Ƿ�ɲ���
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
			// �ж��Ƿ�ɸ���
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
			// �ж��Ƿ�ɼ�ǩ
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
			radioApprove = new UIRadioButton(ApproveLangUtil.getPass());// ApproveLangUtil.getApprove()"��׼"
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
																		 *//* ����׼ */);
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
		// ��ǩ���û���������
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

	private boolean canAddApprover() {// �Ƿ���Լ�ǩ
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
		// �ж��Ƿ�ɲ���
		if (canReject()) {
			getRadioReject().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setStatus(STATUS_REJECT);
					setBtnViewFlowStatus(false);
				}
			});
		}
		// �ж��Ƿ�ɸ���
		if (canTransfer()) {
			getRadioTransfer().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setStatus(STATUS_TRANSFER);
					setBtnViewFlowStatus(false);
				}
			});
		}
		// �ж��Ƿ�ɼ�ǩ
		if (canAddApprover()) {
			getRadioAddAssign().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setStatus(STATUS_ADDASSIGN);
					setBtnViewFlowStatus(false);
				}
			});
		}

		// ȷ�϶Ի���
		getBtnOK().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getRadioApprove().isSelected()) {
					onApprove(true);// ����ͨ��
				} else if (radioUnApprove.isSelected()) {
					onApprove(false);// ������ͨ��
				}
				if (getRadioReject().isSelected()) {
					onReject();// ����
				}
				if (getRadioTransfer().isSelected()) {
					onTransfer();// ����
				}
				if (getRadioAddAssign().isSelected()) {
					onAddAssign();// ��ǩ
				}
			}
		});

		// ȡ���Ի���
		getBtnCancel().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeCancel();
			}
		});

		// ����
		getLblCopySend().addHyperlinkLabelListener(
				new HyperlinkLabelListener() {
					@Override
					public void hyperlinkClicked(HyperlinkLabelEvent event) {
						onCopySend();
						setStatus(condition);
					}
				});
		/*
		 * // ѡ��ָ���� getPaneDispatchPerson().setActionListener(new
		 * ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * getDispatchDialog().showModal(); } }); // ѡ��ָ�������
		 * getDispatchDialog().addUIDialogListener(new UIDialogListener() {
		 * 
		 * @Override public void dialogClosed(UIDialogEvent event) {
		 * if(event.m_Operation == UIDialogEvent.WINDOW_OK) {
		 * onChooseDispatchOK(); } } });
		 */
		// ɾ��������
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
		 * //���������� getLinkViewFlow().addHyperlinkLabelListener(new
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

		// ��ʷ����
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

		// �ϴ�����
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
		 * update by lihaibo ���ظ�Ϊѡ����������Ա����Ӧ����ͼ
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
	 * У�� add by lihaibo
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
																	 * ���ɲ��ص���ǰ���ڵĺ�������
																	 * ��
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
	// //��ȡPanelÿ��ҳǩ�����Ӧ������
	// StringBuffer sbDispatchUser = new StringBuffer();
	// UITabbedPane tabPane = getDisPatchPanel().getUITabbedPane();
	// for (int i = 0; i < tabPane.getTabCount(); i++) {
	// //����
	// sbDispatchUser.append(tabPane.getTitleAt(i));
	// sbDispatchUser.append("��");
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
	// sbDispatchUser.append("��");
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
		// ����
		copySend();

		// ��Ӹ���
		List<AttachmentVO> Attachments = updateAttachment2DocServer();
		worknoteVO.setAttachmentSetting(Attachments);

		String note = getCheckNotePulsApproveResult();
		String result = ApproveWorkitemAssistor.sign(worknoteVO, note);
		// XXXX
		Logger.debug("�ϴ������ɹ�����������Ϊ" + Attachments.size());
		return StringUtil.isEmptyWithTrim(result);

	}

	/**
	 * Ԥ�Ȼ������
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

	// ���ݲ�����ѡ�û����������ǩ��OrganizeUnit
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
							"ApproveWorkitemAcceptDlg-000000")/* ��ѡ��Ҫ��ǩ���û� */);
			return;
		}

		if (!beforeButtonOperate())
			return;

		worknoteVO.setChecknote(getTxtNote().getText());

		ApplicationRuntimeAdjustContext context = new ApplicationRuntimeAdjustContext();
		context.setStyle(getAddAssignDialog().getAddAssignStyle());// style��ȥ����
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
		// ����
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
	 * ���� ����
	 * 
	 * @Modify zhouzheng 2010-4ͬһ�޸�Ϊ����WorkflowManageUtil
	 *         .doAlterSender(Container container, WorkflowManageContext
	 *         context)����
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
		OrganizeUnit[] orgUnits = findUserByIDs(userPKs);// Ӧ��ѡ���

		String checknote = getCurrentTextArea().getText();
		// /////////////////////////��Ϊ�û�����
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
	 * ����
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
		// ����
		worknoteVO.getTaskInfo().getTask()
				.setTaskType(WfTaskType.Backward.getIntValue());

		// worknoteVO.getTaskInfo().getTask().setSubmit2RjectTache(isSubmitToRejectTache);
		Activity rejectActivity = getRejectDialog().getSelectActivity();
		// Activity rejectActivity = getPanelRejectFlow().getRejectToActivity();
		if (rejectActivity == null) {
			// update by lihaibo

			MessageDialog.showErrorDlg(null, "����", "��ѡ����Ҫ���������");
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
			// ѡ���ǩ���
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
	 * null) { transferDialog = new TransferUserDialog(this); // ѡ����������
	 * transferDialog.addUIDialogListener(new UIDialogListener() {
	 * 
	 * @Override public void dialogClosed(UIDialogEvent event) {
	 * onChooseTransferOK(); } }); } return transferDialog; }
	 */

	private FlowStateRefDlg getRejectDialog() {
		if (rejectDlg == null) {
			rejectDlg = new FlowStateRefDlg(this, worknoteVO, isInWorkflow);

			// ѡ�񲵻������
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
	 * ���͵�panel
	 * */
	private CpySendUserTree2List getCpySendPanel() {
		if (copySendPanel == null) {
			copySendPanel = new CpySendUserTree2List(getRoleUserParaVO());
			copySendPanel.getRoleUserTree().setRootVisible(false);
		}
		return copySendPanel;
	}

	// �ṩ��ǩ�����ɣ���������ʼ���Ĳ���
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
	 * ��ʾ������Ϣ <li>����Ϊ������ <li>Ҳ����Ϊ������
	 */
	protected void showFlowinfo() {
		getFlowStateDlg().setVisible(true);
		// getPanelRejectFlow().setVisible(true);
	}

	private void copySend() {
		// @modifier yanke1 2011-7-15 ���ó�������Ϣ
		// �����Ƿ���׼�����г���
		worknoteVO.setMailExtCpySenders(getCpySendPanel().getMailVOs());
		worknoteVO.setMsgExtCpySenders(getCpySendPanel().getMsgVOs());
	}

	/**
	 * ����ͨ����ͨ��
	 */
	protected void onApprove(boolean pass) {

		// ����׼
		boolean isNeedDispatch = pass ? isExistAssignableInfoWhenPass()
				: isExistAssignableInfoWhenNopass();
		if (isNeedDispatch) {
			// ���ָ����Ϣ
			getDispatchDialog().initByWorknoteVO(
					worknoteVO,
					pass ? AssignableInfo.CRITERION_PASS
							: AssignableInfo.CRITERION_NOPASS);
			int result = getDispatchDialog().showModal();
			if (result == UIDialog.ID_CANCEL) {
				// ���ָ�ɶԻ�������ȡ������ôȡ������������������»ص�������� ��changlx�����壩modified by
				// zhangrui 2012-04-17
				return;
			}
		}
		if (!beforeButtonOperate())
			return;

		// yanke1+ 2011-7-15 ���õ�ǰ�������Ƿ�����̽��и���
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
	 * ����׼ʱ���Ƿ���ڿ�ָ�ɵĺ�̻
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
	 * ��׼ʱ���Ƿ���ڿ�ָ�ɵĺ�̻
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
		// ����������ť������
		radioApprove.setEnabled(isShow);
		if (isShow) {
			// ѡ������ҳǩ
			// radioApprove.setSelected(true);
			condition = STATUS_APPROVE;
			setStatus(condition);
			// tabPane.setSelectedIndex(STATUS_APPROVE);
		}
	}

	public void setShowNoPass(boolean isShow) {
		// ���ز���׼
		if (!isShow) {
			buttonGroup.remove(getRadioUnApprove());
			radioPanel.remove(getRadioUnApprove());
		}
	}

	public void setShowReject(boolean isShow) {
		// if(isStatusShowOnTab(STATUS_REJECT)) {
		// ���ò���ҳǩ������
		// tabPane.setEnabledAt(getTabIndexByStatus(STATUS_REJECT), isShow);
		radioReject.setEnabled(isShow);
		if (isShow) {
			// ѡ�в���ҳǩ
			// radioReject.setSelected(true);
			condition = STATUS_REJECT;
			setStatus(condition);
			// tabPane.setSelectedIndex(STATUS_REJECT);
		}
		// }
	}

	/**
	 * ��ǰ�Ƿ���ĳ��״̬
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
					"ApproveWorkitemAcceptDlg-0001")/* ������ */;
		} else if (isCurrentInStatus(STATUS_ADDASSIGN)) {
			return NCLangRes.getInstance().getStrByID("approveworkitem",
					"ApproveWorkitemAcceptDlg-0002")/* ��ǩ�� */;
		}
		return "";
	}

	private String getApproveNote() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"pfworkflow61_0", "0pfworkflow61-0008")/* @res "�������������Ĭ��Ϊ��" */
				+ getPassCheckNote();
	}

	/**
	 * ���õ�ǰ��Ԥ������
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
	 * ���Ҹ��Ի��������õ�������δ���ã������ϵͳԤ�õġ�
	 * 
	 * @param type
	 *            ��������
	 * @return ����
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
		sqlCond += " order by pk_user";// system�϶����ں��棿
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
	// // ���okʱ������
	// // �����жϵ��ݽ����Ƿ�Ϊ�Ի��򣨼��Ƿ�����Ϣ���Ĵ򿪣�
	// // ��Ϊ�Ի�����һ���رյ��ݽ���
	// @Override
	// public void closeOK() {
	//
	// super.closeOK();
	// closeParentFuncletWindow();
	// }

	/**
	 * �ԡ�ȡ����ģʽ�رնԻ��� ҵ��ڵ������Ҫ�޸�
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

		if (status == STATUS_APPROVE) {// ��׼STATUS_APPROVE
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 0));
			radioApprove.setSelected(true);
		} else if (status == STATUS_UNAPPROVE) {// ����׼STATUS_UNAPPROVE
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 0));
			radioUnApprove.setSelected(true);
		} else if (status == STATUS_REJECT) {// ����STATUS_REJECT
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 32));
			radioReject.setSelected(true);
		} else if (status == STATUS_TRANSFER) {// ����STATUS_TRANSFER
			getRefPanel().setPreferredSize(new Dimension(getWidth(), 32));
			radioTransfer.setSelected(true);
		} else if (status == STATUS_ADDASSIGN) {// ����STATUS_ADDASSIGN
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
		// getTxtNote().updateUI();//txt��updateUI()����ɱ߿�ȥ����
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
							"0pfworkflow61-0010")/* @res "ָ����" */);
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
	 * �ϴ��������ĵ����������ϴ�ʧ�ܣ���Ӱ����������
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
			Logger.error(e.getMessage() + "�ϴ�����ʧ�ܣ�");
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
																		 * "����ժҪ"
																		 */;
			refRemark.setRefNodeName(remarkRefName);
		}
		return refRemark;
	}

	// �������õ�������ɺ��Ƿ��Զ��رյ��ݽ���
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
			// �ļ�ѡ��ʽ,ֻ��ѡ���ļ�
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// �Ƿ��ṩ"�����ļ�"����
			fileChooser.setAcceptAllFileFilterUsed(true);
			// �����ļ�������
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
	 * ���ɵ� panel
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
	 * �õ��������
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
					"approveworkitem", "ApproveWorkitemAcceptDlg-0003")/* ������ */);
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
	 * �õ��������
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
							"0pfworkflow61-0013")/* @res "������" */);
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
							"0pfworkflow630029")/* @res "�û�" */);/*
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
	 * �õ���ǩ���
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
			// // ѡ���ǩ��
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
							"0pfworkflow61-0014")/* @res "��ǩ��" */);
			addAssignRefSpring.putConstraint(SpringLayout.NORTH, lblAddAssign,
					8, SpringLayout.NORTH, getPnlAddAssignRef());
			addAssignRefSpring.putConstraint(SpringLayout.WEST, lblAddAssign,
					25, SpringLayout.WEST, getPnlAddAssignRef());
		}
		return lblAddAssign;
	}

	/**
	 * �õ����Ӱ�ť��Ŀǰ��Labelʵ��
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
	 * private UILabel getLinkViewFlow() { if(lblViewFlow == null) { //�鿴������
	 * lblViewFlow =
	 * createLinkButton(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().
	 * getStrByID("pfworkflow61_0","0pfworkflow61-0015")@res "�鿴������", null); }
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
							"approveworkitem", "ApproveWorkitemAcceptDlg-0005")/* ��ʷ���� */);
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
			// "Ԥ������ʾ��ȫ��Ԥ����֯��ִ�������2,300.00>Ԥ�������700.00��\n�Ѿ��������������ٽ��п�֧��";
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
			// �����п�
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
							"pfworkflow63", "0pfworkflow630083")/* @res "��������" */,
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

	ApproveWorkitemAcceptDataManage dataManager = null;// ������ȡ����

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
	 * �������ñ��Ի����ʼ����Ĭ��ѡ�еĲ��� ��Ҫ������Ϣ���Ŀ���������
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