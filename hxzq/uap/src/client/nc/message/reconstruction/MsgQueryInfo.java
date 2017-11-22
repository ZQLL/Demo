package nc.message.reconstruction;

public class MsgQueryInfo {
	private String quikdate;

	/**
	 * 等同于isRead isHandle和isRead后续会统一归为isRead字段
	 */
	private boolean isHandle = false;
	private boolean isShowQry = false;

	private String subject;
	private String[] detail;
	private String[] billtype;
	private String[] sender;
	private String from;
	private String senddate;

	private boolean isQiukQry = true;

	public String getQuikdate() {
		return quikdate;
	}

	public void setQuikdate(String quikdate) {
		this.quikdate = quikdate;
	}

	public boolean isHandle() {
		return isHandle;
	}

	public void setHandle(boolean isHandle) {
		this.isHandle = isHandle;
	}

	public boolean isShowQry() {
		return isShowQry;
	}

	public void setShowQry(boolean isShowQry) {
		this.isShowQry = isShowQry;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getDetail() {
		return detail;
	}

	public void setDetail(String[] detail) {
		this.detail = detail;
	}

	public String[] getBillType() {
		return billtype;
	}

	public void setBillType(String[] billtype) {
		this.billtype = billtype;
	}

	public String[] getSender() {
		return sender;
	}

	public void setSender(String[] sender) {
		this.sender = sender;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSenddate() {
		return senddate;
	}

	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}

	public boolean isQiukQry() {
		return isQiukQry;
	}

	public void setQiukQry(boolean isQiukQry) {
		this.isQiukQry = isQiukQry;
	}
}
