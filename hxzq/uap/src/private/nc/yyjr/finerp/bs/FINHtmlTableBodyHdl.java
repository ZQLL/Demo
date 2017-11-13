package nc.yyjr.finerp.bs;

import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.ep.bx.BxcontrastVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class FINHtmlTableBodyHdl {
	public static String getTableBodyBy264XCxx01(AggregatedValueObject aggvo,
			BaseDAO dao, StringBuffer sb,
			CircularlyAccessibleValueObject[] childVO, String isnull,
			String billtype, List<BxcontrastVO> bvo) throws DAOException {
		if (childVO.length > 0) {
			sb.append("<table width = \"100%\"  cellpadding=\"5\" cellspacing=\"0\">");
			sb.append("<tr><td    height=\"1px\" align=\"center\"></td></tr>");

			sb.append("<tr class=\"tr2\">");
			sb.append("<td   class=\"td5\" align=\"center\" nowrap>费用名称</td>");
			sb.append("<td   class=\"td5\" align=\"center\" nowrap>项目</td>");
			sb.append("</tr>");
			for (int i = 0; i < childVO.length; i++) {
				sb.append("<tr class=\"tr3\" >");

				String sqlstr = " SELECT T.NAME FROM BD_INOUTBUSICLASS T WHERE T.PK_INOUTBUSICLASS = '"
						+ childVO[i].getAttributeValue("szxmid") + "'";
				Object[] obj = (Object[]) dao.executeQuery(sqlstr,
						new ArrayProcessor());
				sb.append("<td   class=\"td5\" align=\"center\" >");
				if (obj == null) {
					sb.append(isnull);
				} else {
					sb.append(obj[0].toString());
				}
				sb.append("</td>");

				sqlstr = " select t.project_name from bd_project t where t.pk_project =  '"
						+ childVO[i].getAttributeValue("jobid") + "'";
				obj = (Object[]) dao.executeQuery(sqlstr, new ArrayProcessor());
				sb.append("<td   class=\"td5\" align=\"center\" >");
				if (obj == null) {
					sb.append(isnull);
				} else {
					sb.append(obj[0].toString());
				}
				sb.append("</td>");

				sb.append("</tr>");
			}
			sb.append("</table>");
		}
		return FINHtmlTools.getTableHeadOfBX(aggvo, dao) + sb.toString();
	}

	public static String getTableBodyBy263XCxx01(AggregatedValueObject aggvo,
			BaseDAO dao, StringBuffer sb,
			CircularlyAccessibleValueObject[] childVO, String isnull,
			String billtype, List<BxcontrastVO> bvo) throws DAOException {
		if (childVO.length > 0) {
			sb.append("<table width = \"100%\"  cellpadding=\"5\" cellspacing=\"0\">");
			sb.append("<tr><td    height=\"1px\" align=\"center\"></td></tr>");

			sb.append("<tr class=\"tr2\">");
			sb.append("<td   class=\"td5\" align=\"center\" nowrap>借款类型</td>");
			sb.append("<td   class=\"td5\" align=\"center\" nowrap>应收应付项目</td>");
			sb.append("</tr>");
			for (int i = 0; i < childVO.length; i++) {
				sb.append("<tr class=\"tr3\" >");

				String sqlstr = " SELECT T.NAME FROM bd_defdoc T WHERE T.pk_defdoc = '"
						+ childVO[i].getAttributeValue("defitem5") + "'";
				Object[] obj = (Object[]) dao.executeQuery(sqlstr,
						new ArrayProcessor());
				sb.append("<td   class=\"td5\" align=\"center\" >");
				if (obj == null) {
					sb.append(isnull);
				} else {
					sb.append(obj[0].toString());
				}
				sb.append("</td>");

				sqlstr = " SELECT T.NAME FROM bd_defdoc T WHERE T.pk_defdoc = '"
						+ childVO[i].getAttributeValue("defitem6") + "'";
				obj = (Object[]) dao.executeQuery(sqlstr, new ArrayProcessor());
				sb.append("<td   class=\"td5\" align=\"center\" >");
				if (obj == null) {
					sb.append(isnull);
				} else {
					sb.append(obj[0].toString());
				}
				sb.append("</td>");

				sb.append("</tr>");
			}
			sb.append("</table>");
		}
		return FINHtmlTools.getTableHeadOfBX(aggvo, dao) + sb.toString();
	}

	public static String getTableBodyBy261XCxx01(AggregatedValueObject aggvo,
			BaseDAO dao, StringBuffer sb,
			CircularlyAccessibleValueObject[] childVO, String isnull,
			String billtype, List<BxcontrastVO> bvo) throws DAOException {
		if (childVO.length > 0) {
			sb.append("<table width = \"100%\"  cellpadding=\"5\" cellspacing=\"0\">");
			sb.append("<tr><td    height=\"1px\" align=\"center\"></td></tr>");

			sb.append("<tr class=\"tr2\">");
			sb.append("<td   class=\"td5\" align=\"center\" nowrap>费用名称</td>");
			sb.append("<td   class=\"td5\" align=\"center\" nowrap>项目</td>");
			sb.append("</tr>");
			for (int i = 0; i < childVO.length; i++) {
				sb.append("<tr class=\"tr3\" >");

				String sqlstr = " SELECT T.NAME FROM BD_INOUTBUSICLASS T WHERE T.PK_INOUTBUSICLASS = '"
						+ childVO[i].getAttributeValue("pk_iobsclass") + "'";
				Object[] obj = (Object[]) dao.executeQuery(sqlstr,
						new ArrayProcessor());
				sb.append("<td   class=\"td5\" align=\"center\" >");
				if (obj == null) {
					sb.append(isnull);
				} else {
					sb.append(obj[0].toString());
				}
				sb.append("</td>");

				sqlstr = " select t.project_name from bd_project t where t.pk_project =  '"
						+ childVO[i].getAttributeValue("pk_project") + "'";
				obj = (Object[]) dao.executeQuery(sqlstr, new ArrayProcessor());
				sb.append("<td   class=\"td5\" align=\"center\" >");
				if (obj == null) {
					sb.append(isnull);
				} else {
					sb.append(obj[0].toString());
				}
				sb.append("</td>");

				sb.append("</tr>");
			}
			sb.append("</table>");
		}
		return FINHtmlTools.getTableHeadOfBX(aggvo, dao) + sb.toString();
	}
}
