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
	/** 工作项列表 */
	private UITablePane ivjTablePnState = null;
	/** 审批工作项 */
	private FlowAdminVO adminVO = null;
	/** 单据类型 */
	private String billType = null;
	/** 单据ID */
	private String billId = null;

	/** 流程类型 */
	private int m_iWorkflowtype;
	/** 当前Panel的UI容器 */
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

		// 设置批语列的渲染器
		TableColumn noteColumn = getTableState().getColumn(
				NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000205")/* @res "批语" */);
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
	 * 初始化 审批流处理情况
	 */
	private void initByApproveflow() {
		ApproveItemTableModel fsTM = new ApproveItemTableModel();
		getTableState().setModel(fsTM);
		// 调整列宽
		getTableState().setColumnWidth(fsTM.getColumnWidth());

		// 查询审批工作项
		queryApproveItems();
		// 填充到表模型
		populateWithWorkitems(fsTM);
	}

	/**
	 * 初始化 工作流处理情况
	 */
	private void initByWorkflow() {
		WorkflowItemTableModel fsTM = new WorkflowItemTableModel();

		getTableState().setModel(fsTM);
		// 调整列宽
		getTableState().setColumnWidth(fsTM.getColumnWidth());
		// 查询工作流工作项
		queryWorkflowItems();
		// 填充到表模型
		populateWithWorkitems(fsTM);
	}

	private UITablePane getTablePnState() {
		if (ivjTablePnState == null) {
			ivjTablePnState = new UITablePane();
			ivjTablePnState.setName("TablePnState");
			// 设置表格行单选模式
			ivjTablePnState.getTable().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			ivjTablePnState.getTable().addSortListener();
			// 设置列调整模式
			ivjTablePnState.getTable()
					.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return ivjTablePnState;
	}

	/**
	 * 获得表格
	 */
	public UITable getTableState() {
		return getTablePnState().getTable();

	}

	private PfOperationNotifier notifier = new PfOperationNotifier(this);

	private void queryWorkflowItems() {
		// 查询该单据的历史工作项处理信息
		// 包括已经处理完成的 或尚未处理的工作项
		try {
			adminVO = NCLocator
					.getInstance()
					.lookup(IPFWorkflowQry.class)
					.queryWorkitemForAdmin(billType, billId,
							WorkflowTypeEnum.Workflow.getIntValue());

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			notifier.showError(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000505")/* 查询该单据的审批历史信息出现异常： */
					+ e.getMessage());
		}
	}

	private void queryApproveItems() {
		// 查询该单据的历史审批信息
		// 包括已经审批完成的 或尚未审批的工作项
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
					"UPPpfworkflow-000022")/* 查询该单据的历史工作流处理信息出现异常： */
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

				// 将WorkflownoteAttVO转换为AttachmentVO放在WorkflownoteVO中
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
	 * 将工作项填充到表模型
	 */
	private void populateWithWorkitems(VOTableModel fsTM) {
		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();

		if (noteVOs == null || noteVOs.length < 1)
			return;

		// 更新表模型
		fsTM.clearTable();

		// 将VO数组添加到表模型
		fsTM.addVO(noteVOs);

	}

	public void onPrint() {

		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();

		if (noteVOs == null || noteVOs.length == 0) {
			return;
		}

		onPrintTemplate();

		// 关闭对话框 leijun 2004-12-10//没理解什么意思？
		if (m_container instanceof FlowStateDlg)
			((FlowStateDlg) m_container).dispose();
	}

	/**
	 * 使用打印模板打印
	 * 
	 * @since NC5.0
	 */
	private void onPrintTemplate() {
		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();
		if (noteVOs == null || noteVOs.length < 1)
			return;

		// 打印预览
		PrintEntry pe = new PrintEntry(this, generateDataSource());

		// 设置打印模板ID的查询条件
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
		// add by lihaibo 选择流程图后 改为 -1
		lsm.setAnchorSelectionIndex(-1);
	}

	/**
	 * 根据审批信息表格中的数据填充打印数据源
	 * 
	 * @return
	 */
	private IDataSource generateDataSource() {

		// yanke1 2911-10-21 由HashMap<String, String[]>改为HashMap<String,
		// Object[]>
		// 以便适配WorkitemPrintDataOfBill的新构造器
		// @see IPFWorkflowQry.getApproveWorkitemPrintDs()
		HashMap<String, Object[]> hmDatas = new HashMap<String, Object[]>();

		WorkflownoteVO[] noteVOs = adminVO.getWorkflowNotes();

		VOTableModel ftm = (VOTableModel) getTableState().getModel();// 获取表格

		int rowCount = ftm.getRowCount();

		// 单据类型名 - 从缓存中获取
		BilltypeVO billtypeVO = PfUIDataCache.getBillTypeUI(billType);
		String billtypeName = Pfi18nTools.i18nBilltypeNameByVO(
				billtypeVO.getPk_billtypecode(), billtypeVO);
		String[] colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = billtypeName;
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLTYPE, colValues);

		// 单据号
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = noteVOs[0].getBillno();
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLNO, colValues);

		// 公司名称 -
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

		// 发送时间
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 1);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDDATE, colValues);

		// 发送人名称
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 0);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDMAN, colValues);

		// 处理时间
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 3);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DEALDATE, colValues);

		// 历时
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 4);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DURATION, colValues);

		// 审批人名称
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 2);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_CHECKMAN, colValues);

		// 审批人PK
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = noteVOs[j].getCheckman();
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_PK_CHECKMAN, colValues);

		// 审批批语
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
			// 审批意见
			colValues = new String[rowCount];
			for (int j = 0; j < rowCount; j++) {
				Object value = ftm.getValueAt(j, 6);
				colValues[j] = String.valueOf(value == null ? "" : value);
			}
			hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_APPROVERESULT,
					colValues);
		}

		// 处理状况
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
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}
}
