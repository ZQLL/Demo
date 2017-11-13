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

		sb.append(" <td    class=\"td2\" align=\"center\"> 单据编号</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("djbh") + "</td>");

		sb.append("<td   class=\"td2\"   align=\"center\"><nobr>部门名称</nobr> </td>");
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
		sb.append(" <td    class=\"td2\" align=\"center\">单据类型</td>");
		sb.append("<td   class=\"td1\" >报销单</td>");

		sb.append("</tr>");

		sb.append(" <tr class=\"tr1\">");

		sb.append("<td   class=\"td2\"   align=\"center\">报销人</td>");
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

		sb.append(" <td    class=\"td2\" align=\"center\"> 报销金额</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("total") + "</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> 事由</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("zy") + "</td>");

		sb.append("</tr>");
		sb.append(" <tr class=\"tr1\">");
	}

	public static void getTableHead261XCxx01(BaseDAO dao,
			CircularlyAccessibleValueObject parentVO, String isnull,
			StringBuffer sb) throws DAOException {
		sb.append("<tr class=\"tr1\" >");

		sb.append(" <td    class=\"td2\" align=\"center\"> 单据编号</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("billno") + "</td>");

		sb.append("<td   class=\"td2\"   align=\"center\"><nobr>部门名称</nobr> </td>");
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
		sb.append(" <td    class=\"td2\" align=\"center\">单据类型</td>");
		sb.append("<td   class=\"td1\" >差旅申请单</td>");

		sb.append("</tr>");

		sb.append(" <tr class=\"tr1\">");

		sb.append("<td   class=\"td2\"   align=\"center\">申请人</td>");
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

		sb.append(" <td    class=\"td2\" align=\"center\"> 申请金额</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("orig_amount") + "</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> 事由</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("reason") + "</td>");

		sb.append("</tr>");
		sb.append(" <tr class=\"tr1\">");
	}
	
	public static void getTableHead263XCxx01(BaseDAO dao,
			CircularlyAccessibleValueObject parentVO, String isnull,
			StringBuffer sb) throws DAOException {
		sb.append("<tr class=\"tr1\" >");

		sb.append(" <td    class=\"td2\" align=\"center\"> 单据编号</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("djbh") + "</td>");

		sb.append("<td   class=\"td2\"   align=\"center\"><nobr>部门名称</nobr> </td>");
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
		sb.append(" <td    class=\"td2\" align=\"center\">单据类型</td>");
		sb.append("<td   class=\"td1\" >借款单</td>");

		sb.append("</tr>");

		sb.append(" <tr class=\"tr1\">");

		sb.append("<td   class=\"td2\"   align=\"center\">借款人</td>");
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

		sb.append(" <td    class=\"td2\" align=\"center\"> 借款金额</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("total") + "</td>");

		sb.append(" <td    class=\"td2\" align=\"center\"> 事由</td>");
		sb.append("<td   class=\"td1\" >"
				+ parentVO.getAttributeValue("zy") + "</td>");

		sb.append("</tr>");
		sb.append(" <tr class=\"tr1\">");
	}
}
