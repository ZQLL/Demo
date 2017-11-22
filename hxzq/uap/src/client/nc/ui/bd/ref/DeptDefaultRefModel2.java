package nc.ui.bd.ref;

import nc.bs.logging.Logger;
import nc.pubitf.setting.defaultdata.OrgSettingAccessor;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.DeptVO;
import nc.vo.org.util.OrgTreeCellRendererIconPolicy;

public class DeptDefaultRefModel2 extends AbstractRefTreeModel {
	public DeptDefaultRefModel2() {
		reset();
	}

	@Override
	public void reset() {
		setRefNodeName("部门");

		setFieldCode(new String[] { "code", "name", "mnecode" });
		setFieldName(new String[] {
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UC000-0003279"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UC000-0001155"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UC000-0000703") });

		setHiddenFieldCode(new String[] { "pk_dept", "pk_fatherorg",
				"displayorder", "innercode" });
		setPkFieldCode("pk_dept");
		setRefCodeField("code");
		setRefNameField("name");
		setTableName(DeptVO.getDefaultTableName());
		setFatherField("pk_fatherorg");
		setChildField("pk_dept");
		setMnecode(new String[] { "mnecode", "name" });

		setAddEnableStateWherePart(true);

		setResourceID("dept");

		setFilterRefNodeName(new String[] { "业务单元" });

		setOrderPart("displayorder, code");

		resetFieldName();

		setTreeIconPolicy(new OrgTreeCellRendererIconPolicy("Department"));
	}

	@Override
	public void filterValueChanged(ValueChangedEvent changedValue) {
		String[] selectedPKs = (String[]) changedValue.getNewValue();
		if ((selectedPKs != null) && (selectedPKs.length > 0)) {
			setPk_org(selectedPKs[0]);
		}
	}

	@Override
	protected String getEnvWherePart() {
		String sql = null;
		try {
			String pk_org = OrgSettingAccessor.getDefaultOrgUnit();
			sql = "(pk_group = '" + getPk_group() + "' and " + "pk_org"
					+ " = '" + pk_org + "')";
		} catch (Exception e) {
			Logger.error("查询默认组织失败！请检查！");
		}
		return sql;
	}
}
