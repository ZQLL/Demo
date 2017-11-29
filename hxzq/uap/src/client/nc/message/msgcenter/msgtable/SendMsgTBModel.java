package nc.message.msgcenter.msgtable;

import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.ui.ml.NCLangRes;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.util.RbacCacheUtil;

@SuppressWarnings("unchecked")
public class SendMsgTBModel extends AbstractMsgTBModel {

	private static final long serialVersionUID = -2625117949468770839L;

	// String[] colNames = {
	// "",
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000043")/* 优先级
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000044")/* 附件
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000016")/* 主题
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000047")/* 收件人
	// */,
	// NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000046") /*
	// 发送时间 */};

	// update by lihaibo
	String[] colNames = {
			"",
			NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000016")/* 主题 */,
			NCLangRes.getInstance().getStrByID("ncmessage", "msgboxres-000047") /* 收件人 */};

	public SendMsgTBModel(Class<MsgTableVO> cls) {
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

	@Override
	public Object getValueAt(int row, int column) {

		NCMessage msg = getMessage(row);
		MessageVO msgvo = msg.getMessage();

		switch (column) {
		case 1:
			return msgvo.getSubject();
			// return msgvo.getPriority();
		case 2:
			String receivers = msgvo.getReceiver();
			return getReceivDisp(receivers);
			// int attach = msg.getAttachmentSetting().getAttachments().length;
			// return attach > 0 ? Boolean.TRUE : Boolean.FALSE;
			// case 3:
			// return msgvo.getSubject();
			// case 4:
			// String receivers = msgvo.getReceiver();
			// return getReceivDisp(receivers);
			// case 5:
			// try {
			// FormatResult result = NCFormater.formatDateTime(msgvo
			// .getSendtime());
			// return result.getValue();
			// } catch (FormatException e) {
			// Logger.error(msgvo, e);
			// return msgvo.getSendtime().toLocalString();
			// }
		}
		return super.getValueAt(row, column);
	}

	private String getReceivDisp(String receivers) {
		if (receivers == null)
			return NCLangRes.getInstance().getStrByID("ncmessage",
					"msgboxres-000048")/* 无收件人 */;
		String[] users = receivers.split(",");
		StringBuilder sbd = new StringBuilder();
		UserVO uvo = null;
		for (String userpk : users) {
			uvo = RbacCacheUtil.getUserByID(userpk);
			sbd.append(DispMultiLangUtil.getUserName(uvo) + ",");
		}
		return sbd.substring(0, sbd.length() - 1);
	}
}
