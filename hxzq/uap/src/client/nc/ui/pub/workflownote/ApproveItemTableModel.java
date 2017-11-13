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
 * ����״����ģ��
 * 
 * @author leijun 2006-6-8
 * @since NC50
 * @modifier �ƿ�(yanke1) 2011-3-11 ʹ�ù��������������ʱ
 * @modifier yanke1 2011-3-31 ���б��е�ʱ��ת��Ϊ����ʱ��ʱ��
 * @modifier yanke1 2011-6-2 �б��а���һ�С���ʱ������ֵ��ÿ�ε���getValueAt()ʱ���ظ����㣬
 *           ����б����ǳ�������������һ�����ݻ��棬ֻ����һ����ʱ����gatValueAtʱֱ�Ӵӻ�����ȡ����
 */
public class ApproveItemTableModel extends VOTableModel implements
		TableModelListener {

	/**
	 * ���仯����Ƶ����ȡ������ʱ�䣬���ﻺ��һ���༶����
	 */
	private UFDateTime m_serverTime = null;

	/**
	 * ���棬���ڱ��洦�������б���
	 */
	private Map<String, String[]> cacheMap = null;

	/**
	 * ����
	 */
	private String[] strColNames = null;

	public ApproveItemTableModel() {
		super(WorkflownoteVO.class);

		// �����ʼ��
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
	 * ����������ȡactivity add by lihaibo
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

		// �ӻ����л�ȡ����

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

			// ���ù������Ƿ�Ϊ�޵�
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
						 * @modifier yanke1 2011-3-31 ת��Ϊ����ʱ��ʱ��
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
						 * @modifier yanke1 2011-3-31 ת��Ϊ����ʱ��ʱ��
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
						 * modified by �ƿ� (yanke1) at 2011-3-11 ʹ�ù��������������ʱ
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
						// ״̬
						int status = flownote.getApprovestatus().intValue();
						obj = WFTask.resolveApproveStatus(status, isMakebill);
						break;
					case 6:
						// ������
						Object result = flownote.getApproveresult();
						obj = WFTask.resolveApproveResult(isMakebill ? null
								: result);
						break;
					case 7:
						obj = flownote.getChecknote();
						break;
					case 8:
						// �������ﻯ����Ϣ
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