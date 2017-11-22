package nc.ui.er.djlx;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.bd.ref.RefVO_mlang;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;

public class ErmTrantypeRefModel extends AbstractRefModel {
	final String wherePart = " systemcode = 'erm' and pk_billtypecode like '26%' and istransaction = 'Y' and (pk_group='"
			+ getPk_group() + "' or pk_org = '" + "GLOBLE00000000000000" + "')";

	public ErmTrantypeRefModel() {
		setAddEnableStateWherePart(true);
	}

	@Override
	protected String getDisableDataWherePart(boolean isDisableDataShow) {
		if (isDisableDataShow) {
			return " islock = 'Y' or isnull(islock,'N') = 'N' ";
		}
		return " isnull(islock,'N') = 'N' ";
	}

	@Override
	public int getDefaultFieldCount() {
		return getFieldCode().length;
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { "pk_billtypecode", "billtypename",
				"pk_billtypeid" };
	}

	@Override
	public String getRefCodeField() {
		return "pk_billtypecode";
	}

	@Override
	public String getRefNameField() {
		return "billtypename";
	}

	@Override
	public String[] getFieldName() {
		return new String[] {
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UCMD1-000172"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("ersetting_0",
						"02011001-0023"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UCMD1-000026") };
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] { "pk_billtypeid" };
	}

	@Override
	public String getPkFieldCode() {
		return "pk_billtypeid";
	}

	@Override
	public String getRefTitle() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("ersetting_0",
				"02011001-0024");
	}

	@Override
	public String getTableName() {
		return " bd_billtype ";
	}

	@Override
	public String getWherePart() {
		if (StringUtil.isEmpty(super.getWherePart())) {
			return this.wherePart;
		}
		return this.wherePart + " and " + super.getWherePart();
	}

	@Override
	protected RefVO_mlang[] getRefVO_mlang() {
		RefVO_mlang refVO_mlang = new RefVO_mlang();
		refVO_mlang.setDirName("billtype");
		refVO_mlang.setFieldName("billtypename");
		refVO_mlang.setResIDFieldNames(new String[] { "pk_billtypecode" });
		refVO_mlang.setPreStr("D");

		return new RefVO_mlang[] { refVO_mlang };
	}
}
