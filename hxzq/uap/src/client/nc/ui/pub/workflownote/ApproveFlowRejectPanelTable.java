package nc.ui.pub.workflownote;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.message.vo.AttachmentVO;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.table.VOTableModel;
import nc.ui.pub.desktop.PfOperationNotifier;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.workflowqry.FlowQryTableCellRenderer;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.workflownote.WorkflownoteAttVO;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pub.workflowqry.FlowAdminVO;
import nc.vo.uap.pf.WorkitemPrintDataOfBill;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nc.vo.workflow.admin.WorkflowManageContext;

public class ApproveFlowRejectPanelTable extends UIPanel implements
		MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** �������б� */
	private UITablePane ivjTablePnState = null;
	/** ���������� */
	private FlowAdminVO adminVO = null;
	/** �������� */
	private String billType = null;
	/** ����ID */
	private String billId = null;

	/** �������� */
	private int m_iWorkflowtype;
	/** ��ǰPanel��UI���� */
	private Component m_container;

	public ApproveFlowRejectPanelTable(Component container,
			WorkflowManageContext context) {
		m_container = container;
		billType = context.getBillType();
		billId = context.getBillId();
		m_iWorkflowtype = context.getFlowType();
		initUI();

		if (WorkflowTypeEnum.Approveflow.getIntValue() == m_iWorkflowtype
				|| WorkflowTypeEnum.SubApproveflow.getIntValue() == m_iWorkflowtype) {
			initByApproveflow();
		} else {
			initByWorkflow();
		}

		// ���������е���Ⱦ��
		TableColumn noteColumn = getTableState().getColumn(
				NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000205")/* @res "����" */);
		noteColumn.setCellRenderer(new FlowQryTableCellRenderer());
		noteColumn.setCellEditor(new CEditor());
	}

	private void initUI() {
		this.setLayout(new BorderLayout());
		this.add(getTablePnState());
	}

	class CEditor extends AbstractCellEditor implements TableCellEditor {

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			return table.getCellRenderer(row, column)
					.getTableCellRendererComponent(table, value, true, true,
							row, column);
		}

	}

	/**
	 * ��ʼ�� �������������
	 */
	private void initByApproveflow() {
		ApproveItemTableModel fsTM = new ApproveItemTableModel();
		getTableState().setModel(fsTM);
		// �����п�
		getTableState().setColumnWidth(fsTM.getColumnWidth());

		// ��ѯ����������
		queryApproveItems();
		// ��䵽��ģ��
		populateWithWorkitems(fsTM);
	}

	/**
	 * ��ʼ�� �������������
	 */
	private void initByWorkflow() {
		WorkflowItemTableModel fsTM = new WorkflowItemTableModel();

		getTableState().setModel(fsTM);
		// �����п�
		getTableState().setColumnWidth(fsTM.getColumnWidth());
		// ��ѯ������������
		queryWorkflowItems();
		// ��䵽��ģ��
		populateWithWorkitems(fsTM);
	}

	private UITablePane getTablePnState() {
		if (ivjTablePnState == null) {
			ivjTablePnState = new UITablePane();
			ivjTablePnState.setName("TablePnState");
			// ���ñ���е�ѡģʽ
			ivjTablePnState.getTable().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			ivjTablePnState.getTable().addSortListener();
			// �����е���ģʽ
			ivjTablePnState.getTable()
					.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return ivjTablePnState;
	}

	/**
	 * ��ñ��
	 */
	public UITable getTableState() {
		return getTablePnState().getTable();

	}

	private PfOperationNotifier notifier = new PfOperationNotifier(this);

	private void queryWorkflowItems() {
		// ��ѯ�õ��ݵ���ʷ���������Ϣ
		// �����Ѿ�������ɵ� ����δ����Ĺ�����
		try {
			adminVO = NCLocator
					.getInstance()
					.lookup(IPFWorkflowQry.class)
					.queryWorkitemForAdmin(billType, billId,
							WorkflowTypeEnum.Workflow.getIntValue());

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			notifier.showError(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000505")/* ��ѯ�õ��ݵ�������ʷ��Ϣ�����쳣�� */
					+ e.getMessage());
		}
	}

	private void queryApproveItems() {
		// ��ѯ�õ��ݵ���ʷ������Ϣ
		// �����Ѿ�������ɵ� ����δ�����Ĺ�����
		try {
			adminVO = NCLocator
					.getInstance()
					.lookup(IPFWorkflowQry.class)
					.queryWorkitemForAdmin(billType, billId,
							WorkflowTypeEnum.Approveflow.getIntValue());

			fillNoteWithAttachment();
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			notifier.showError(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000022")/* ��ѯ�õ��ݵ���ʷ������������Ϣ�����쳣�� */
					+ e.getMessage());
		}
	}

	private void fillNoteWithAttachment() {
		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();
		Map<String, List<WorkflownoteAttVO>> attMap = adminVO
				.getWorkflowNoteAttVOs();

		if (noteVOs == null || noteVOs.length == 0) {
			return;
		}
		if (attMap == null) {
			return;
		}

		for (WorkflownoteVO vo : noteVOs) {
			List<WorkflownoteAttVO> noteAttVOs = attMap.get(vo.getPrimaryKey());

			if (noteAttVOs != null && noteAttVOs.size() > 0) {
				List<AttachmentVO> attVOList = new ArrayList<AttachmentVO>();

				// ��WorkflownoteAttVOת��ΪAttachmentVO����WorkflownoteVO��
				for (WorkflownoteAttVO noteAttVO : noteAttVOs) {
					AttachmentVO attVO = new AttachmentVO();

					attVO.setPk_file(noteAttVO.getPk_file());
					attVO.setFilename(noteAttVO.getFilename());
					attVO.setFilesize(noteAttVO.getFilesize());

					attVOList.add(attVO);
				}

				vo.setAttachmentSetting(attVOList);
			}
		}
	}

	/**
	 * ����������䵽��ģ��
	 */
	private void populateWithWorkitems(VOTableModel fsTM) {
		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();

		if (noteVOs == null || noteVOs.length < 1)
			return;

		// ���±�ģ��
		fsTM.clearTable();

		// ��VO������ӵ���ģ��
		fsTM.addVO(noteVOs);

	}

	public void onPrint() {

		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();

		if (noteVOs == null || noteVOs.length == 0) {
			return;
		}

		onPrintTemplate();

		// �رնԻ��� leijun 2004-12-10//û���ʲô��˼��
		if (m_container instanceof FlowStateDlg)
			((FlowStateDlg) m_container).dispose();
	}

	/**
	 * ʹ�ô�ӡģ���ӡ
	 * 
	 * @since NC5.0
	 */
	private void onPrintTemplate() {
		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();
		if (noteVOs == null || noteVOs.length < 1)
			return;

		// ��ӡԤ��
		PrintEntry pe = new PrintEntry(this, generateDataSource());

		// ���ô�ӡģ��ID�Ĳ�ѯ����
		if (m_iWorkflowtype == WorkflowTypeEnum.Approveflow.getIntValue()) {
			pe.setTemplateID(PfUtilUITools.getLoginGroup(), "101609",
					WorkbenchEnvironment.getInstance().getLoginUser()
							.getPrimaryKey(), null, "approve");
		} else {
			pe.setTemplateID(PfUtilUITools.getLoginGroup(), "101609",
					WorkbenchEnvironment.getInstance().getLoginUser()
							.getPrimaryKey(), null, "work");
		}

		pe.preview();
	}

	public void onActivitySelected(String activityId) {
		Integer[] rows = ((ApproveItemTableModel) getTableState().getModel())
				.getRowNumberOfActivity(activityId);

		ListSelectionModel lsm = getTableState().getSelectionModel();

		lsm.clearSelection();
		for (Integer i : rows) {
			lsm.addSelectionInterval(i, i);
		}
		// add by lihaibo ѡ������ͼ�� ��Ϊ -1
		lsm.setAnchorSelectionIndex(-1);
	}

	/**
	 * ����������Ϣ����е���������ӡ����Դ
	 * 
	 * @return
	 */
	private IDataSource generateDataSource() {

		// yanke1 2911-10-21 ��HashMap<String, String[]>��ΪHashMap<String,
		// Object[]>
		// �Ա�����WorkitemPrintDataOfBill���¹�����
		// @see IPFWorkflowQry.getApproveWorkitemPrintDs()
		HashMap<String, Object[]> hmDatas = new HashMap<String, Object[]>();

		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();

		VOTableModel ftm = (VOTableModel) getTableState().getModel();// ��ȡ���

		int rowCount = ftm.getRowCount();

		// ���������� - �ӻ����л�ȡ
		BilltypeVO billtypeVO = PfUIDataCache.getBillTypeUI(billType);
		String billtypeName = Pfi18nTools.i18nBilltypeNameByVO(
				billtypeVO.getPk_billtypecode(), billtypeVO);
		String[] colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = billtypeName;
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLTYPE, colValues);

		// ���ݺ�
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = noteVOs[0].getBillno();
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLNO, colValues);

		// ��˾���� -
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			String strCorp = noteVOs[j].getPk_org();
			if (StringUtil.isEmptyWithTrim(strCorp))
				colValues[j] = "";
			else {
				try {
					colValues[j] = "Error";// DapCall.getCorpInfoByPK(strCorp)[1];
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_CORP, colValues);

		// ����ʱ��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 1);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDDATE, colValues);

		// ����������
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 0);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDMAN, colValues);

		// ����ʱ��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 3);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DEALDATE, colValues);

		// ��ʱ
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 4);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DURATION, colValues);

		// ����������
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 2);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_CHECKMAN, colValues);

		// ������PK
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = noteVOs[j].getCheckman();
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_PK_CHECKMAN, colValues);

		// ��������
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = null;
			if (ftm instanceof WorkflowItemTableModel) {
				value = ftm.getValueAt(j, 6);
			} else {
				value = ftm.getValueAt(j, 7);
			}
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_NOTE, colValues);

		if (ftm instanceof ApproveItemTableModel) {
			// �������
			colValues = new String[rowCount];
			for (int j = 0; j < rowCount; j++) {
				Object value = ftm.getValueAt(j, 6);
				colValues[j] = String.valueOf(value == null ? "" : value);
			}
			hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_APPROVERESULT,
					colValues);
		}

		// ����״��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 5);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}

		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_STATUS, colValues);

		return new WorkitemPrintDataOfBill(hmDatas);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int i = 0;
		int t = i + 1;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}
}
