package nc.yyjr.finerp.bs;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class FINHtmlTableHeadHdl {
	public static void getTableHead264XCxx01(BaseDAO dao,
			CircularlyAccessibleValueObject parentVO, String isnull,
			StringBuffer sb) throws DAOException {
		sb.append("<tr class=\"tr1\" >");

		sb.append(" <td    class=\"td2\" align=\"center\"> ���ݱ��</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("djbh") + "</td>");

		sb.append("<td   class=\"td2\"   align=\"center\"><nobr>��������</nobr> </td>");
		String sqlstr = "   SELECT T.NAME FROM org_dept T WHERE T.PK_DEPT = '"
				+ parentVO.getAttributeValue("deptid") + "'";
		Object[] obj = (Object[]) dao
				.executeQuery(sqlstr, new ArrayProcessor());
		sb.append("<td      class=\"td1\">");
		if (obj == null) {
			sb.append(isnull);
		} else {
			sb.append(obj[0].toString());
		}
		sb.append("</td>");
		sb.append(" <td    class=\"td2\" align=\"center\">��������</td>");
		sb.append("<td   class=\"td1\" >������</td>");

		sb.append("</tr>");

		sb.append(" <tr class=\"tr1\">");

		sb.append("<td   class=\"td2\"   align=\"center\">������</td>");
		sqlstr = " SELECT T.NAME FROM BD_PSNDOC T WHERE T.PK_PSNDOC = '"
				+ parentVO.getAttributeValue("jkbxr") + "'";
		obj = (Object[]) dao.executeQuery(sqlstr, new ArrayProcessor());
		sb.append("<td      class=\"td1\">");
		if (obj == null) {
			sb.append(isnull);
		} else {
			sb.append(obj[0].toString());
		}
		sb.append("</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> �������</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("total") + "</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> ����</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("zy") + "</td>");

		sb.append("</tr>");
		sb.append(" <tr class=\"tr1\">");
	}

	public static void getTableHead261XCxx01(BaseDAO dao,
			CircularlyAccessibleValueObject parentVO, String isnull,
			StringBuffer sb) throws DAOException {
		sb.append("<tr class=\"tr1\" >");

		sb.append(" <td    class=\"td2\" align=\"center\"> ���ݱ��</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("billno") + "</td>");

		sb.append("<td   class=\"td2\"   align=\"center\"><nobr>��������</nobr> </td>");
		String sqlstr = "   SELECT T.NAME FROM org_dept T WHERE T.PK_DEPT = '"
				+ parentVO.getAttributeValue("apply_dept") + "'";
		Object[] obj = (Object[]) dao
				.executeQuery(sqlstr, new ArrayProcessor());
		sb.append("<td      class=\"td1\">");
		if (obj == null) {
			sb.append(isnull);
		} else {
			sb.append(obj[0].toString());
		}
		sb.append("</td>");
		sb.append(" <td    class=\"td2\" align=\"center\">��������</td>");
		sb.append("<td   class=\"td1\" >�������뵥</td>");

		sb.append("</tr>");

		sb.append(" <tr class=\"tr1\">");

		sb.append("<td   class=\"td2\"   align=\"center\">������</td>");
		sqlstr = " SELECT T.NAME FROM BD_PSNDOC T WHERE T.PK_PSNDOC = '"
				+ parentVO.getAttributeValue("billmaker") + "'";
		obj = (Object[]) dao.executeQuery(sqlstr, new ArrayProcessor());
		sb.append("<td      class=\"td1\">");
		if (obj == null) {
			sb.append(isnull);
		} else {
			sb.append(obj[0].toString());
		}
		sb.append("</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> ������</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("orig_amount") + "</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> ����</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("reason") + "</td>");

		sb.append("</tr>");
		sb.append(" <tr class=\"tr1\">");
	}
	
	public static void getTableHead263XCxx01(BaseDAO dao,
			CircularlyAccessibleValueObject parentVO, String isnull,
			StringBuffer sb) throws DAOException {
		sb.append("<tr class=\"tr1\" >");

		sb.append(" <td    class=\"td2\" align=\"center\"> ���ݱ��</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("djbh") + "</td>");

		sb.append("<td   class=\"td2\"   align=\"center\"><nobr>��������</nobr> </td>");
		String sqlstr = "   SELECT T.NAME FROM org_dept T WHERE T.PK_DEPT = '"
				+ parentVO.getAttributeValue("deptid") + "'";
		Object[] obj = (Object[]) dao
				.executeQuery(sqlstr, new ArrayProcessor());
		sb.append("<td      class=\"td1\">");
		if (obj == null) {
			sb.append(isnull);
		} else {
			sb.append(obj[0].toString());
		}
		sb.append("</td>");
		sb.append(" <td    class=\"td2\" align=\"center\">��������</td>");
		sb.append("<td   class=\"td1\" >��</td>");

		sb.append("</tr>");

		sb.append(" <tr class=\"tr1\">");

		sb.append("<td   class=\"td2\"   align=\"center\">�����</td>");
		sqlstr = " SELECT T.NAME FROM BD_PSNDOC T WHERE T.PK_PSNDOC = '"
				+ parentVO.getAttributeValue("jkbxr") + "'";
		obj = (Object[]) dao.executeQuery(sqlstr, new ArrayProcessor());
		sb.append("<td      class=\"td1\">");
		if (obj == null) {
			sb.append(isnull);
		} else {
			sb.append(obj[0].toString());
		}
		sb.append("</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> �����</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("total") + "</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> ����</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("zy") + "</td>");

		sb.append("</tr>");
		sb.append(" <tr class=\"tr1\">");
	}
}
