package nc.message.reconstruction;

import nc.message.vo.MessageVO;

import org.apache.commons.lang.ArrayUtils;

import uap.apppf.util.SQLTransferMeaningUtil;

public class NCMessageSqlBuilder {
	public static String buildSendDateSql(String date, String logic) {
		if (date == null)
			return "";

		String sql = MessageVO.SENDTIME + ">'" + date + "'";

		sql = appendLogic(sql, logic);

		return sql;
	}

	public static String buildSubjectSql(String subject, String logic) {
		if (subject == null || subject.length() == 0)
			return "";

		String sql = MessageVO.SUBJECT + " LIKE '%" + subject + "%'";

		sql = appendLogic(sql, logic);

		return sql;
	}

	public static String buildSenderSql(String[] senders, String logic) {
		if (ArrayUtils.isEmpty(senders))
			return "";

		String sql = MessageVO.SENDER + getInSql(senders);

		sql = appendLogic(sql, logic);

		return sql;
	}

	/**
	 * ADD by lihaibo
	 * 
	 * @param details
	 *            选择部门后查询出的details
	 * @param logic
	 * @return
	 */
	public static String buildDetailSql(String[] details, String logic) {
		if (ArrayUtils.isEmpty(details))
			return "";

		String sql = MessageVO.DETAIL + getInSql(details);

		sql = appendLogic(sql, logic);

		return sql;
	}

	/**
	 * ADD by lihaibo
	 * 
	 * @param billtypes
	 *            选择单据类型后查询出的billtypes
	 * @param logic
	 * @return
	 */
	public static String buildBillTypeSql(String[] billtypes, String logic) {
		if (ArrayUtils.isEmpty(billtypes))
			return "";

		String sql = MessageVO.DETAIL + " LIKE '%" + billtypes[0] + "%'";

		sql = appendLogic(sql, logic);

		return sql;
	}

	public static String buildSourceSql(String source, String logic) {
		if (source == null || source.length() == 0)
			return "";

		String sql = MessageVO.MSGSOURCETYPE + "= '"
				+ SQLTransferMeaningUtil.tmsql(source) + "'";

		sql = appendLogic(sql, logic);

		return sql;
	}

	private static String getInSql(String[] pks) {
		StringBuilder sbd = new StringBuilder(" IN (");

		for (String pk : pks)
			sbd.append("'" + pk + "',");

		String insql = sbd.substring(0, sbd.length() - 1);

		return insql + ")";
	}

	private static String appendLogic(String sql, String logic) {
		if (logic != null && sql != null && sql.length() > 0) {
			if (logic.equalsIgnoreCase("AND"))
				return " AND " + sql;

			if (logic.equalsIgnoreCase("OR"))
				return " OR " + sql;
		}
		return sql;
	}
}
