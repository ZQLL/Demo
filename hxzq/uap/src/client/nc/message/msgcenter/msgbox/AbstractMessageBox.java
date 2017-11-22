package nc.message.msgcenter.msgbox;

import nc.message.msgcenter.msgtable.MsgTableVO;
import nc.message.reconstruction.MessageBusiDelegator;
import nc.message.reconstruction.MsgQueryInfo;
import nc.message.reconstruction.NCMessageSqlBuilder;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.ui.uif2.components.pagination.PaginationModel;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.collections.CollectionUtils;
import java.util.*;

import javax.swing.SwingWorker;
public abstract class AbstractMessageBox implements IMessageBox {
	private String lastcondition = null;
	private Integer unreadnum = 0;
	private MessageBusiDelegator delegator = null;
	private MsgQueryInfo qryinfo = null;
	private MessageVO[] topmessages = null;

	// 分页的model
	private PaginationModel pagemodel = null;

	public PaginationModel getPagemodel() {
		return pagemodel;
	}

	public void setPagemodel(PaginationModel pagemodel) {
		this.pagemodel = pagemodel;
	}

	protected abstract String getBoxCondition();

	public String getLastcondition() {
		// 默认查询条件为查询7天内邮件
		if (lastcondition == null)
			lastcondition = getDefaultCondition();
		return lastcondition;
	}

	@Override
	public MsgQueryInfo getMsgQueryInfo() {
		if (qryinfo == null) {
			qryinfo = new MsgQueryInfo();
			qryinfo.setQuikdate("7");
			qryinfo.setHandle(false);
			qryinfo.setShowQry(false);
		}
		return qryinfo;
	}

	@Override
	public void setMsgQueryInfo(MsgQueryInfo qryinfo) {
		this.qryinfo = qryinfo;
	}

	protected String getDefaultCondition() {
		UFDate date = new UFDate().getDateBefore(7);
		String begindate = date.asLocalBegin().toLocalString();
		return NCMessageSqlBuilder.buildSendDateSql(begindate, "");
	}

	public void setLastcondition(String lastcondition) {
		this.lastcondition = lastcondition;
	}

	@Override
	public int getUnReadCount() {
		return unreadnum;
	}

	@Override
	public void setUnReadCount(Integer unreadnum) {
		this.unreadnum = unreadnum;
	}

	protected MessageBusiDelegator getBusiDelegator() {
		if (delegator == null)
			delegator = new MessageBusiDelegator();
		return delegator;
	}

	protected void loadTableModelData() {
		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
			List<NCMessage> msglist = null;

			@Override
			protected Object doInBackground() throws Exception {
				msglist = getBusiDelegator().qryMessageByCondition(
						getCondition());
				return null;
			}

			@Override
			protected void done() {
				super.done();
				getTableModel().refreshMessage(msglist);
			}

		};
		worker.execute();
	}

	public String getCondition() {
		String lastsql = getLastcondition();
		if (lastsql != null && lastsql.length() > 0)
			return getBoxCondition() + " AND " + lastsql;
		else
			return getBoxCondition();
	}

	public String convertToSQL(MsgQueryInfo qryinfo) {
		StringBuilder sbd = new StringBuilder();

		if (qryinfo.isQiukQry()) {
			sbd.append(NCMessageSqlBuilder.buildSendDateSql(
					convertTODate(qryinfo.getQuikdate()), null));
		} else {
			sbd.append(NCMessageSqlBuilder.buildSubjectSql(
					qryinfo.getSubject(), null));

			if (sbd.length() > 0)
				sbd.append(NCMessageSqlBuilder.buildSubjectSql(
						qryinfo.getSubject(), "AND"));
			else
				sbd.append(NCMessageSqlBuilder.buildSubjectSql(
						qryinfo.getSubject(), null));

			if (sbd.length() > 0)
				sbd.append(NCMessageSqlBuilder.buildSenderSql(
						qryinfo.getSender(), "AND"));
			else
				sbd.append(NCMessageSqlBuilder.buildSenderSql(
						qryinfo.getSender(), null));

			// add by lihaibo 部门参照
			if (sbd.length() > 0)
				sbd.append(NCMessageSqlBuilder.buildDetailSql(
						qryinfo.getDetail(), "AND"));
			else
				sbd.append(NCMessageSqlBuilder.buildDetailSql(
						qryinfo.getDetail(), null));

			// add by lihaibo 单据类型参照
			if (sbd.length() > 0)
				sbd.append(NCMessageSqlBuilder.buildBillTypeSql(
						qryinfo.getBillType(), "AND"));
			else
				sbd.append(NCMessageSqlBuilder.buildBillTypeSql(
						qryinfo.getBillType(), null));

			if (sbd.length() > 0)
				sbd.append(NCMessageSqlBuilder.buildSendDateSql(
						convertTODate(qryinfo.getSenddate()), "and"));
			else
				sbd.append(NCMessageSqlBuilder.buildSendDateSql(
						convertTODate(qryinfo.getSenddate()), null));

			if (sbd.length() > 0)
				sbd.append(NCMessageSqlBuilder.buildSourceSql(
						qryinfo.getFrom(), "AND"));
			else
				sbd.append(NCMessageSqlBuilder.buildSourceSql(
						qryinfo.getFrom(), null));
		}

		String stateSQL = getStateSQL(qryinfo);
		if (stateSQL != null && stateSQL.trim().length() > 0) {
			if (sbd.length() > 0)
				sbd.append(" AND ").append(stateSQL);
			else
				sbd.append(stateSQL);
		}

		return sbd.toString();
	}

	protected String getStateSQL(MsgQueryInfo qryinfo) {
		/**
		 * update by lihaibo 处理已处理消息sql
		 */
		if (!qryinfo.isHandle()) {
			return " ISNULL(" + MessageVO.ISREAD + ",'N') = 'N'";
		} else {
			return " " + MessageVO.ISREAD + " = 'Y'";
		}
	}

	private String convertTODate(String days) {
		if (days == null)
			return null;

		Integer dayint = Integer.valueOf(days);
		if (dayint < 0)
			return null;

		UFDate date = new UFDate().getDateBefore(dayint);

		return date.asLocalBegin().toLocalString();
	}

	@Override
	public MessageVO[] getLastMessages() {
		return this.topmessages;
	}

	@Override
	public void setLastMessage(MessageVO[] msgvo) {
		this.topmessages = msgvo;
	}

	@Override
	public void reQuery() {
		List<NCMessage> messages = getBusiDelegator().qryMessageByCondition(
				getCondition());
		getTableModel().refreshMessage(messages);
	}

	public void processStateChange(List<NCMessage> messages) {
		for (NCMessage message2 : messages) {
			NCMessage ncmsg = getTableModel().getMessageByPK(
					message2.getMessage().getPk_message());
			if (ncmsg != null) {
				ncmsg.getMessage().setIsread(message2.getMessage().getIsread());
				ncmsg.getMessage().setIshandled(
						message2.getMessage().getIshandled());
			}
		}
		getBusiDelegator().updateMessages(messages.toArray(new NCMessage[0]));
	}

	@Override
	public boolean deleteMessages() {
		List<NCMessage> selected = getTableModel().getSelectMessage();
		if (CollectionUtils.isEmpty(selected))
			return false;

		for (NCMessage message : selected)
			message.getMessage().setIsdelete(UFBoolean.TRUE);

		List<MsgTableVO> selvos = getTableModel().getSelectedVO();
		for (MsgTableVO msgTableVO : selvos)
			getTableModel().removeVO(msgTableVO);

		getBusiDelegator().updateMessages(selected.toArray(new NCMessage[0]));

		return true;
	}
}
