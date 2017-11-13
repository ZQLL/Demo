package nc.yyjr.finerp.bs;

import java.text.NumberFormat;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.ep.bx.BxcontrastVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class FINHtmlTools {
	public static String getHtmlByBillType(AggregatedValueObject aggvo,
			BaseDAO dao) throws DAOException {
		StringBuffer sb = new StringBuffer();
		CircularlyAccessibleValueObject parentVO = null;
		CircularlyAccessibleValueObject[] childVO = null;

		parentVO = aggvo.getParentVO();
		childVO = aggvo.getChildrenVO();

		String isnull = "&nbsp";
		String billtype = "";
		List<BxcontrastVO> bvo = null;
		try {
			bvo = (List) dao.retrieveByClause(BxcontrastVO.class, " pk_bxd = '"
					+ parentVO.getAttributeValue("pk_jkbx") + "'");
		} catch (Exception localException) {
		}
		if (parentVO.getAttributeValue("djlxbm") != null) {
			billtype = parentVO.getAttributeValue("djlxbm").toString();
		}
		if (parentVO.getAttributeValue("billtype") != null) {
			billtype = parentVO.getAttributeValue("billtype").toString();
		}
		if (parentVO.getAttributeValue("pk_billtype") != null) {
			billtype = parentVO.getAttributeValue("pk_billtype").toString();
		}
		if (parentVO.getAttributeValue("pk_tradetype") != null) {
			billtype = parentVO.getAttributeValue("pk_tradetype").toString();
		}
		if (parentVO.getAttributeValue("vtranstype") != null) {
			billtype = parentVO.getAttributeValue("vtranstype").toString();
		}
		if (billtype.startsWith("264X")) {
			return FINHtmlTableBodyHdl.getTableBodyBy264XCxx01(aggvo, dao, sb,
					childVO, isnull, billtype, bvo);
		}
		if (billtype.startsWith("261X")) {
			return FINHtmlTableBodyHdl.getTableBodyBy261XCxx01(aggvo, dao, sb,
					childVO, isnull, billtype, bvo);
		}
		if (billtype.startsWith("263X")) {
			return FINHtmlTableBodyHdl.getTableBodyBy263XCxx01(aggvo, dao, sb,
					childVO, isnull, billtype, bvo);
		}
		sb.append("<table width=\"100%\" align=\"center\" cellpadding=\"8\" cellspacing=\"0\">");
		sb.append(" <tr><td    class=\"td\"     colspan=\"9\" align=\"center\"><font color=\"#ffffff\">该交易类型未配置模板</font></td></tr >");
		sb.append("</table>");

		return sb.toString();
	}

	public static String getTableHeadOfBX(AggregatedValueObject aggvo,
			BaseDAO dao) throws DAOException {
		CircularlyAccessibleValueObject parentVO = aggvo.getParentVO();
		String isnull = "&nbsp";
		StringBuffer sb = new StringBuffer();
		String billtype = "";
		if (parentVO.getAttributeValue("djlxbm") != null) {
			billtype = parentVO.getAttributeValue("djlxbm").toString();
		} else if (parentVO.getAttributeValue("pk_tradetype") != null) {
			billtype = parentVO.getAttributeValue("pk_tradetype").toString();
		} else if (parentVO.getAttributeValue("pk_billtype") != null) {
			billtype = parentVO.getAttributeValue("pk_billtype").toString();
		} else if (parentVO.getAttributeValue("billtype") != null) {
			billtype = parentVO.getAttributeValue("billtype").toString();
		} else if (parentVO.getAttributeValue("vtranstype") != null) {
			billtype = parentVO.getAttributeValue("vtranstype").toString();
		}
		String sqlstr = " SELECT DISTINCT T.BILLTYPENAME FROM BD_BILLTYPE T WHERE T.PK_BILLTYPECODE = '"
				+ billtype + "'";
		Object[] obj = (Object[]) dao
				.executeQuery(sqlstr, new ArrayProcessor());
		sb.append("<table width=\"100%\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\">");
		sb.append(" <tr><td    class=\"td\"     colspan=\"8\" align=\"center\"><font color=\"#ffffff\">");
		if (obj == null) {
			sb.append(isnull);
		} else {
			sb.append(obj[0].toString());
		}
		sb.append("</font></td></tr >");
		if (billtype.startsWith("264X")) {
			FINHtmlTableHeadHdl
					.getTableHead264XCxx01(dao, parentVO, isnull, sb);
		} else if (billtype.startsWith("261X")) {
			FINHtmlTableHeadHdl
					.getTableHead261XCxx01(dao, parentVO, isnull, sb);
		} else if (billtype.startsWith("263X")) {
			FINHtmlTableHeadHdl
					.getTableHead263XCxx01(dao, parentVO, isnull, sb);
		}
		sb.append("</table>");
		return sb.toString();
	}

	public static String getCurrencyNumber(String number) {
		if (("".equals(number)) || (number == null) || ("&nbsp".equals(number))) {
			number = "0";
		}
		Double db = Double.valueOf(Double.parseDouble(number));
		if (db.doubleValue() >= 0.0D) {
			number = NumberFormat.getCurrencyInstance().format(db);
			number = number.substring(1, number.length());
		} else {
			number = NumberFormat.getCurrencyInstance().format(db);
			number = "-" + number.substring(2, number.length());
		}
		return number;
	}

	public static String getValueByVO(CircularlyAccessibleValueObject parentVO,
			String constr) {
		String isnull = "&nbsp";
		Object obj = parentVO.getAttributeValue(constr);
		if (obj != null) {
			return obj.toString();
		}
		return isnull;
	}
}
