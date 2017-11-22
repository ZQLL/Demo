package nc.ui.sec.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class BillTypeRefModel extends AbstractRefModel {
	public BillTypeRefModel() {
		super();
		setRefNodeName("֤ȯ��������");
	}

	public BillTypeRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
	}

	@Override
	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// *��������������Ӧ����
		setFieldCode(new String[] { "pk_billtypecode", "billtypename" });
		setFieldName(new String[] { "����", "����" });
		setHiddenFieldCode(new String[] { "systemcode" });
		setPkFieldCode("pk_billtypecode");
		setTableName("bd_billtype");
		setWherePart(" isnull(dr,0)=0 and systemcode='SIM'");
		setRefTitle("��������");
		resetFieldName();
	}
}
