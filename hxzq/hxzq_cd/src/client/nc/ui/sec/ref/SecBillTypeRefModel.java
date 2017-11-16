package nc.ui.sec.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class SecBillTypeRefModel extends AbstractRefModel {
	public SecBillTypeRefModel() {
		super();
		setRefNodeName("单据类型");
		setFieldCode(new String[] { "pk_billtypecode", "billtypename" });
		setFieldName(new String[] { "单据类型", "单据类型名称" });
		setTableName("sec_billtype");
		setDefaultFieldCount(2);
		setPkFieldCode("pk_billtypecode");
		setWherePart(" isnull(dr,0)=0 and is_enable = 'Y' ");
	}
	
	
}
