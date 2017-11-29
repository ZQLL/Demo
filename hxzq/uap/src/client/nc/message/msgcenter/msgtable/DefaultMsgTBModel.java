package nc.message.msgcenter.msgtable;

import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.ui.ml.NCLangRes;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.util.RbacCacheUtil;

@SuppressWarnings("unchecked")
public class DefaultMsgTBModel extends AbstractMsgTBModel {

	private static final long serialVersionUID = 1000508642264549758L;

	// private String[] colNames = {
	// "",
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000042")/* 状态
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000043")/* 优先级
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000044")/* 附件
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000016")/* 主题
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000045")/* 发件人
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000046")/*
	// 发送时间 */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000020") /*
	// 消息类型 */};

	// update by lihaibo
	private String[] colNames = {
			"",
			NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000042")/* 状态 */,
			NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000016")/* 主题 */,
			NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000045") /* 发件人 */};

	private String statetype = "read";

	public DefaultMsgTBModel(Class<MsgTableVO> cls, String statetype) {
		super(cls);
		this.statetype = statetype;
	}

	public DefaultMsgTBModel(Class<MsgTableVO> cls) {
		super(cls);
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	/**
	 * update by lihaibo
	 */

	@Override
	public Object getValueAt(int row, int column) {

		NCMessage msg = getMessage(row);
		MessageVO msgvo = msg.getMessage();

		switch (column) {
		case 1:
			boolean state = false;
			if (statetype.equals("read")) {
				state = msgvo.getIsread() == null ? false : msgvo.getIsread()
						.booleanValue();
			} else {
				state = msgvo.getIshandled() == null ? false : msgvo
						.getIshandled().booleanValue();
			}
			return Boolean.valueOf(state);
		case 2:
			return msgvo.getSubject();
			// return msgvo.getPriority();
		case 3:
			if (msgvo.getSender().trim().length() == 20) {
				UserVO uvo = RbacCacheUtil.getUserByID(msgvo.getSender());
				return DispMultiLangUtil.getUserName(uvo);
			} else {
				return msgvo.getSender();
			}
			// int attach = msg.getAttachmentSetting().getAttachments().length;
			// return attach > 0 ? Boolean.TRUE : Boolean.FALSE;
			// case 4:
			// return msgvo.getSubject();
			// case 5:
			// if (msgvo.getSender().trim().length() == 20) {
			// UserVO uvo = RbacCacheUtil.getUserByID(msgvo.getSender());
			// return DispMultiLangUtil.getUserName(uvo);
			// } else {
			// return msgvo.getSender();
			// }
			// case 6:
			// try {
			// FormatResult result = NCFormater.formatDateTime(msgvo
			// .getSendtime());
			// return result.getValue();
			// } catch (FormatException e) {
			// Logger.error(msgvo, e);
			// return msgvo.getSendtime().toLocalString();
			// }
			//
			// case 7:
			// MsgTypeVO typevo = MsgTypeCache.getInstance().getMsgTypeByCode(
			// msgvo.getMsgsourcetype());
			// return typevo == null ? "" : typevo.getDispName();
		}
		return super.getValueAt(row, column);
	}

}
