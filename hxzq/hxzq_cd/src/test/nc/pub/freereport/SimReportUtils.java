package nc.pub.freereport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.freereport.ReportDataVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.trade.voutils.SafeCompute;
import org.apache.commons.lang.StringUtils;

public class SimReportUtils {
	public static String getEndDate(String str) {
		String endDate = null;
		Pattern pattern = Pattern.compile("^\\d.+@@.+,.+@@(.+\\d)$");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			endDate = matcher.group(1);
		}
		return endDate;
	}

	public static String getStartDate(String str) {
		String startDate = null;
		Pattern pattern = Pattern.compile("^(\\d.+)@@.+,.+@@.+\\d$");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			startDate = matcher.group(1);
		}
		return startDate;
	}

	public static String getManFieldVsql(String preCode, CostPlanVO costPlanVO) {
		String[] mamFields = getFundsFieldArray(costPlanVO);
		Set<String> fieldSet = new HashSet();
		StringBuffer fieldSql = new StringBuffer();
		for (int i = 0; i < mamFields.length; i++) {
			fieldSet.add(mamFields[i]);
		}
		if (StringUtils.isBlank(preCode)) {
			for (int j = 0; j < ReportConst.SYSINIT_itemfieldcodes.length; j++) {
				if (fieldSet.contains(ReportConst.SYSINIT_itemfieldcodes[j])) {
					fieldSql.append(" case ");
					fieldSql.append(" when "
							+ ReportConst.SYSINIT_itemfieldcodes[j]
							+ " is null then '@@' ");
					fieldSql.append(" else "
							+ ReportConst.SYSINIT_itemfieldcodes[j]);
					fieldSql.append(" end as "
							+ ReportConst.SYSINIT_itemfieldcodes[j] + ", ");
				} else {
					fieldSql.append(ReportConst.SYSINIT_itemfieldcodes[j]
							+ ", ");
				}
			}
		} else {
			for (int j = 0; j < ReportConst.SYSINIT_itemfieldcodes.length; j++) {
				if (fieldSet.contains(ReportConst.SYSINIT_itemfieldcodes[j])) {
					fieldSql.append(" case ");
					fieldSql.append(" when " + preCode + "."
							+ ReportConst.SYSINIT_itemfieldcodes[j]
							+ " is null then '@@' ");
					fieldSql.append(" else " + preCode + "."
							+ ReportConst.SYSINIT_itemfieldcodes[j]);
					fieldSql.append(" end as " + preCode + "."
							+ ReportConst.SYSINIT_itemfieldcodes[j] + ", ");
				} else {
					fieldSql.append(preCode + "."
							+ ReportConst.SYSINIT_itemfieldcodes[j] + ", ");
				}
			}
		}
		return fieldSql.toString();
	}

	public static String getGroupSql(String preCode, CostPlanVO costPlanVO) {
		String[] mamFields = getFundsFieldArray(costPlanVO);
		StringBuffer groupsb = new StringBuffer();
		if (StringUtils.isBlank(preCode)) {
			for (int i = 0; i < mamFields.length; i++) {
				groupsb.append(mamFields[i]);
				groupsb.append(",");
			}
		} else {
			for (int i = 0; i < mamFields.length; i++) {
				groupsb.append(preCode + ".");
				groupsb.append(mamFields[i]);
				groupsb.append(",");
			}
		}
		return groupsb.toString();
	}

	public static String[] getFundsFieldArray(CostPlanVO costPlanVO) {
		List<String> fieldlist = new ArrayList();

		fieldlist.add("pk_org");
		if (costPlanVO.getIf_glorgbook().booleanValue()) {
			fieldlist.add("pk_glorgbook");
		}
		if (costPlanVO.getIf_capaccount().booleanValue()) {
			fieldlist.add("pk_capaccount");
		}
		if (costPlanVO.getIf_selfsgroup().booleanValue()) {
			fieldlist.add("pk_selfsgroup");
		}
		if (costPlanVO.getIf_partnaccount().booleanValue()) {
			fieldlist.add("pk_partnaccount");
		}
		if (costPlanVO.getIf_operatesite().booleanValue()) {
			fieldlist.add("pk_operatesite");
		}
		if (costPlanVO.getIf_client().booleanValue()) {
			fieldlist.add("pk_client");
		}
		return (String[]) fieldlist.toArray(new String[fieldlist.size()]);
	}

	public static String[] getManFieldArray(CostPlanVO costPlanVO) {
		List<String> fieldlist = new ArrayList();
		fieldlist.add("pk_assetsprop");
		fieldlist.add("pk_stocksort");

		fieldlist.add("pk_group");
		fieldlist.add("pk_org");
		if (costPlanVO.getIf_glorgbook().booleanValue()) {
			fieldlist.add("pk_glorgbook");
		}
		if (costPlanVO.getIf_capaccount().booleanValue()) {
			fieldlist.add("pk_capaccount");
		}
		if (costPlanVO.getIf_selfsgroup().booleanValue()) {
			fieldlist.add("pk_selfsgroup");
		}
		if (costPlanVO.getIf_partnaccount().booleanValue()) {
			fieldlist.add("pk_partnaccount");
		}
		if (costPlanVO.getIf_operatesite().booleanValue()) {
			fieldlist.add("pk_operatesite");
		}
		if (costPlanVO.getIf_client().booleanValue()) {
			fieldlist.add("pk_client");
		}
		fieldlist.add("begin_date");
		fieldlist.add("end_date");
		fieldlist.add("pk_securities");
		return (String[]) fieldlist.toArray(new String[fieldlist.size()]);
	}

	public static String getQueryCondition(String code, ConditionVO[] conVos)
			throws BusinessException {
		StringBuffer conSB = new StringBuffer();
		for (int i = 0; i < conVos.length; i++) {
			if (ReportConst.TRADE_DATE_F.equals(conVos[i].getFieldCode())) {
				if (!ReportConst.BALA_T_MARK.equals(code)) {
					String value = conVos[i].getValue();
					String startDate = getStartDate(value);
					String endDate = getEndDate(value);
					if ((startDate == null) || (endDate == null)) {
						throw new BusinessException(
								"查询条件-交易日期格式不对或日期区间录入不完整（请同时录入开始日期和结束日期）！");
					}
					conSB.append(" and " + code).append(".")
							.append(ReportConst.TRADE_DATE_F);
					conSB.append(" >= '").append(startDate + "'");
					conSB.append(" and " + code).append(".")
							.append(ReportConst.TRADE_DATE_F);
					conSB.append(" <= '").append(endDate + "'");
				}
			} else if (conVos[i].getOperaCode().equals("in")) {
				conSB.append(" and " + code).append(".")
						.append(conVos[i].getFieldCode());
				conSB.append(" in  ").append(conVos[i].getValue()).append(" ");
			} else {
				conSB.append(" and " + code).append(".")
						.append(conVos[i].getFieldCode());
				conSB.append(" = '").append(conVos[i].getValue()).append("' ");
			}
		}
		if (StringUtils.isNotBlank(conSB.toString())) {
			return conSB.substring(4);
		}
		return conSB.toString();
	}

	public static String getQueryCondition(String code, ConditionVO[] conVos,
			boolean isBala, boolean if_accrual) throws BusinessException {
		StringBuffer conSB = new StringBuffer();
		String date = getDateCon(conVos);
		String pk_org = getOrg(conVos);
		String glBook = getGLBook(conVos);
		if (if_accrual) {
			conSB.append(" and if_accrual='Y' ");
		} else {
			conSB.append(" and if_accrual='N' ");
		}
		if (!isBala) {
			String startDate = getStartDate(date);
			String endDate = getEndDate(date);
			conSB.append(" and " + code).append(".")
					.append(ReportConst.TRADE_DATE_F);
			conSB.append(" >= '").append(startDate + "'");
			conSB.append(" and " + code).append(".")
					.append(ReportConst.TRADE_DATE_F);
			conSB.append(" <= '").append(endDate + "'");
		}
		conSB.append(" and " + code).append(".pk_org ='").append(pk_org + "' ");
		conSB.append(" and " + code).append(".pk_glorgbook ='")
				.append(glBook + "' ");
		return conSB.toString();
	}

	public static ReportDataVO mergerData(List<ReportDataVO> datavos) {
		ReportDataVO result = new ReportDataVO();
		result.copyVO((SuperVO) datavos.get(0));
		String[] fieldnames = result.getUFDoubleFieldNames();
		UFDouble sum = null;
		for (ReportDataVO vo : datavos) {
			for (String name : fieldnames) {
				sum = SafeCompute.add(
						(UFDouble) result.getAttributeValue(name),
						(UFDouble) vo.getAttributeValue(name));

				result.setAttributeValue(name, sum);
			}
		}
		return result;
	}

	public static String getDateCon(ConditionVO[] conVos)
			throws BusinessException {
		String dateValue = new UFDate().toString();
		boolean isfind = false;
		for (int i = 0; i < conVos.length; i++) {
			if (ReportConst.TRADE_DATE_F.equals(conVos[i].getFieldCode())) {
				dateValue = conVos[i].getValue();
				if (StringUtils.isBlank(dateValue)) {
					throw new BusinessException("查询条件-交易日期不能为空！");
				}
				isfind = true;
				break;
			}
		}
		if (!isfind) {
			throw new BusinessException("查询条件-交易日期不能为空！");
		}
		return dateValue;
	}

	public static String getCon(ConditionVO[] conVos, String key, boolean isNeed)
			throws BusinessException {
		String value = null;
		boolean isfind = false;
		for (int i = 0; i < conVos.length; i++) {
			if (key.equals(conVos[i].getFieldCode())) {
				value = conVos[i].getValue();
				isfind = true;
				break;
			}
		}
		if ((!isfind) && (isNeed)) {
			throw new BusinessException("查询条件-不能为空！");
		}
		return value;
	}

	public static String getOrg(ConditionVO[] conVos) throws BusinessException {
		String org = null;
		boolean isfind = false;
		for (int i = 0; i < conVos.length; i++) {
			if (ReportConst.PK_ORG_F.equals(conVos[i].getFieldCode())) {
				org = conVos[i].getValue();
				if (StringUtils.isBlank(org)) {
					throw new BusinessException("业务单元不能为空！");
				}
				isfind = true;
				break;
			}
		}
		if (!isfind) {
			throw new BusinessException("查询条件-业务单元不能为空！");
		}
		return org;
	}

	public static String getGLBook(ConditionVO[] conVos)
			throws BusinessException {
		String glBook = null;
		boolean isfind = false;
		for (int i = 0; i < conVos.length; i++) {
			if (ReportConst.PK_GLBOOK_F.equals(conVos[i].getFieldCode())) {
				glBook = conVos[i].getValue();
				if (StringUtils.isBlank(glBook)) {
					throw new BusinessException("核算账簿不能为空！");
				}
				isfind = true;
				break;
			}
		}
		if (!isfind) {
			throw new BusinessException("查询条件-账簿不能为空！");
		}
		return glBook;
	}
}
