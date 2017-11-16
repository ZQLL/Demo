package nc.ui.sec.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class SecBillTypeRefModel extends AbstractRefModel {
	public SecBillTypeRefModel() {
		super();
		setRefNodeName("��������");
		setFieldCode(new String[] { "pk_billtypecode", "billtypename" });
		setFieldName(new String[] { "��������", "������������" });
		setTableName("sec_billtype");
		setDefaultFieldCount(2);
		setPkFieldCode("pk_billtypecode");
		setWherePart(" isnull(dr,0)=0 and is_enable = 'Y' ");
	}
	
	
}
