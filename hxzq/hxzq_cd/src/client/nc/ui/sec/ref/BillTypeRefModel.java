package nc.ui.sec.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class BillTypeRefModel extends AbstractRefModel {
	public BillTypeRefModel() {
		super();
		setRefNodeName("证券单据类型");
	}

	public BillTypeRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
	}

	@Override
	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// *根据需求设置相应参数
		setFieldCode(new String[] { "pk_billtypecode", "billtypename" });
		setFieldName(new String[] { "编码", "名称" });
		setHiddenFieldCode(new String[] { "systemcode" });
		setPkFieldCode("pk_billtypecode");
		setTableName("bd_billtype");
		setWherePart(" isnull(dr,0)=0 and systemcode='SIM'");
		setRefTitle("单据类型");
		resetFieldName();
	}
}
