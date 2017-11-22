package nc.ui.er.djlx;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;

public class ErmAllBilltypeRefModel extends AbstractRefModel {
	final String wherePart = " pk_billtypecode like '26%' and isnull(islock,'N') ='N' and (pk_group='"
			+ getPk_group() + "' or pk_org='GLOBLE00000000000000' )";

	@Override
	public int getDefaultFieldCount() {
		return 2;
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { "pk_billtypecode", "billtypename",
				"pk_billtypeid" };
	}

	@Override
	public String[] getFieldName() {
		return new String[] {
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UPTcommon-000202"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030501",
						"UPTcommon-000197") };
	}

	@Override
	public String getWherePart() {
		if (StringUtil.isEmpty(super.getWherePart())) {
			return this.wherePart;
		}
		return this.wherePart + " and " + super.getWherePart();
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
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("_Bill",
				"UPP_Bill-000380");
	}

	@Override
	public String getTableName() {
		return " bd_billtype ";
	}
}
