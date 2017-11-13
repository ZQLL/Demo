package nc.ui.pub.workflownote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.ui.format.NCFormater;
import nc.ui.pub.beans.table.VOTableModel;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.msg.MessageVO;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pub.workflownote.WorkitemColumnInfo;
import nc.vo.wfengine.pub.WFTask;
import nc.vo.wfengine.pub.WfTaskOrInstanceStatus;

/**
 * 审批状况表模型
 * 
 * @author leijun 2006-6-8
 * @since NC50
 * @modifier 闫克(yanke1) 2011-3-11 使用工作日历类计算历时
 * @modifier yanke1 2011-3-31 将列表中的时间转换为本地时区时间
 * @modifier yanke1 2011-6-2 列表中包含一列“历时”，其值在每次调用getValueAt()时都重复计算，
 *           造成列表界面非常卡。因此设计了一个数据缓存，只计算一次历时。在gatValueAt时直接从缓存中取数据
 */
public class ApproveItemTableModel extends VOTableModel implements
		TableModelListener {

	/**
	 * 表格变化后不能频繁获取服务器时间，这里缓存一个类级变量
	 */
	private UFDateTime m_serverTime = null;

	/**
	 * 缓存，用于保存处理过后的列表行
	 */
	private Map<String, String[]> cacheMap = null;

	/**
	 * 列名
	 */
	private String[] strColNames = null;

	public ApproveItemTableModel() {
		super(WorkflownoteVO.class);

		// 多语初始化
		strColNames = new String[] { WorkitemColumnInfo.SENDER.toString(),
				WorkitemColumnInfo.SENDDATE.toString(),
				WorkitemColumnInfo.CHECKER.toString(),
				WorkitemColumnInfo.CHECKDATE.toString(),
				WorkitemColumnInfo.DURATION.toString(),
				WorkitemColumnInfo.APPROVESTATUS.toString(),
				WorkitemColumnInfo.APPROVERESULT.toString(),
				WorkitemColumnInfo.APPROVENOTE.toString(),
				WorkitemColumnInfo.TITLE.toString() };

		addTableModelListener(this);
	}

	public Integer[] getRowNumberOfActivity(String activity) {
		if (StringUtil.isEmptyWithTrim(activity)) {
			return new Integer[0];
		}

		List<Integer> rowNumberList = new ArrayList<Integer>();

		for (int i = 0, count = getRowCount(); i < count; i++) {
			WorkflownoteVO meta = (WorkflownoteVO) getVO(i);

			if (activity.equals(meta.getActivityID())) {// meta.getTaskInfo().getTask().getActivityID()
				rowNumberList.add(i);
			}
		}

		return rowNumberList.toArray(new Integer[rowNumberList.size()]);
	}
	
	/**
	 * 根据索引获取activity add by lihaibo
	 * @param activity
	 * @return
	 */
	public String getActivityOfRowNumber(Integer rowNumber) {

		WorkflownoteVO meta = (WorkflownoteVO) getVO(rowNumber);

		return meta.getActivityID();
	}

	private UFDateTime getCurrServerTime() {
		if (m_serverTime == null) {
			m_serverTime = WorkbenchEnvironment.getServerTime();
		}
		return m_serverTime;
	}

	private Map<String, String[]> getCacheMap() {
		if (cacheMap == null) {
			cacheMap = new HashMap<String, String[]>();
		}
		return cacheMap;
	}

	public int[] getColumnWidth() {
		int[] iWidths = new int[] { 60, 120, 60, 120, 95, 60, 60, 200, 260 };
		return iWidths;
	}

	public Class getColumnClass(int columnIndex) {
		if (columnIndex < 9) {
			return String.class;
		} else {
			return UFDouble.class;
		}
	}

	public int getColumnCount() {
		return strColNames.length;
	}

	public String getColumnName(int column) {
		return strColNames[column];
	}

	public Object getValueAt(int row, int col) {
		WorkflownoteVO flownote = (WorkflownoteVO) getVO(row);

		// 从缓存中获取数据

		if (!getCacheMap().containsKey(flownote.getPrimaryKey())) {

			try {
				Map<String, String[]> map = getRowDataMap(new WorkflownoteVO[] { flownote });
				getCacheMap().putAll(map);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}

		}
		String[] rowData = (String[]) getCacheMap().get(
				flownote.getPrimaryKey());

		return rowData == null ? "" : rowData[col];
	}

	public boolean isCellEditable(int row, int col) {
		return col == 7;
		// return false;
	}

	public void setValueAt(Object obj, int row, int col) {
		// Noop!
	}

	@Override
	public void tableChanged(TableModelEvent e) {

		WorkflownoteVO[] vos = (WorkflownoteVO[]) getVOArray();

		switch (e.getType()) {
		case TableModelEvent.UPDATE:
			getCacheMap().clear();

			if (vos != null && vos.length > 0) {

				try {
					Map<String, String[]> map = getRowDataMap(vos);
					getCacheMap().putAll(map);
				} catch (BusinessException e1) {
					Logger.error(e1.getMessage(), e1);
				}
			}
			break;
		case TableModelEvent.INSERT:
			if (vos != null && vos.length > 0) {

				List<WorkflownoteVO> toBeConverted = new ArrayList<WorkflownoteVO>();
				for (WorkflownoteVO vo : vos) {
					if (!getCacheMap().containsKey(vo.getPrimaryKey())) {
						toBeConverted.add(vo);
					}
				}

				try {
					Map<String, String[]> map = getRowDataMap(toBeConverted
							.toArray(new WorkflownoteVO[0]));
					getCacheMap().putAll(map);
				} catch (BusinessException e1) {
					Logger.error(e1.getMessage(), e1);
				}

			}
			break;
		}
	}

	private Map<String, String[]> getRowDataMap(WorkflownoteVO[] notes)
			throws BusinessException {
		String[][] rows = new String[notes.length][];

		String[] pk_orgs = new String[notes.length];
		UFDateTime[] beginTimes = new UFDateTime[notes.length];
		UFDateTime[] endTimes = new UFDateTime[notes.length];

		for (int i = 0; i < notes.length; i++) {
			WorkflownoteVO flownote = notes[i];

			String[] row = new String[getColumnCount()];

			// 检查该工作项是否为修单
			boolean isMakebill = false;
			if (WorkflownoteVO.WORKITEM_TYPE_MAKEBILL.equalsIgnoreCase(flownote
					.getActiontype()))
				isMakebill = true;

			for (int col = 0; col < getColumnCount(); col++) {
				Object obj = null;
				try {
					switch (col) {
					case 0:
						obj = flownote.getSendernameml();
						if (StringUtil.isEmptyWithTrim((String) obj))
							obj = flownote.getSendername();
						break;
					case 1:
						obj = flownote.getSenddate();
						/*
						 * @modifier yanke1 2011-3-31 转换为本地时区时间
						 */
						if (obj != null && obj instanceof UFDateTime) {
							String datetime = ((UFDateTime) obj)
									.toLocalString();
							datetime = NCFormater.formatDateTime(datetime)
									.getValue();
							obj = datetime;
						}
						break;
					case 2:
						obj = flownote.getChecknameml();
						if (StringUtil.isEmptyWithTrim((String) obj))
							obj = flownote.getCheckname();
						break;
					case 3:
						obj = flownote.getDealdate();
						/*
						 * @modifier yanke1 2011-3-31 转换为本地时区时间
						 */
						if (obj != null && obj instanceof UFDateTime) {
							String datetime = ((UFDateTime) obj)
									.toLocalString();
							datetime = NCFormater.formatDateTime(datetime)
									.getValue();
							obj = datetime;
						}
						break;
					case 4:

						/*
						 * modified by 闫克 (yanke1) at 2011-3-11 使用工作日历类计算历时
						 */

						UFDateTime beginTime = flownote.getSenddate();
						UFDateTime endTime = flownote.getDealdate();

						if (endTime == null) {
							int iStatus = flownote.getApprovestatus()
									.intValue();
							if (iStatus == WfTaskOrInstanceStatus.Inefficient
									.getIntValue()) {
								endTime = null;
							} else {
								endTime = getCurrServerTime();
							}
						}

						pk_orgs[i] = flownote.getPk_org();
						beginTimes[i] = beginTime;
						endTimes[i] = endTime;

						obj = "";
						break;

					case 5:
						// 状态
						int status = flownote.getApprovestatus().intValue();
						obj = WFTask.resolveApproveStatus(status, isMakebill);
						break;
					case 6:
						// 审核意见
						Object result = flownote.getApproveresult();
						obj = WFTask.resolveApproveResult(isMakebill ? null
								: result);
						break;
					case 7:
						obj = flownote.getChecknote();
						break;
					case 8:
						// 解析多语化的消息
						obj = MessageVO.getMessageNoteAfterI18N(flownote
								.getMessagenote());
						break;

					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					obj = "";
				}

				row[col] = obj == null ? "" : String.valueOf(obj);
			}

			rows[i] = row;
		}

		String[] durations = NCLocator
				.getInstance()
				.lookup(IPFWorkflowQry.class)
				.getElapsedTimeInWorkCalendarBatch(pk_orgs, beginTimes,
						endTimes);

		Map<String, String[]> map = new HashMap<String, String[]>();

		for (int i = 0; i < notes.length; i++) {

			String[] row = rows[i];
			String duration = durations[i];

			row[4] = duration;

			map.put(notes[i].getPrimaryKey(), row);
		}

		return map;
	}

}