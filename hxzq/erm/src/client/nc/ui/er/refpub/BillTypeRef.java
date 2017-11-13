package nc.ui.er.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * ������Ȩ����-�������Ͳ���
 * @author lihaibo
 *
 */
public class BillTypeRef extends nc.ui.pf.pub.TranstypeRefModel{
	public BillTypeRef() {
		super();
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { "pk_billtypecode", "billtypename" };
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "�������ͱ���", "������������" };
	}

	/**
	 * ����
	 */
	@Override
	public String getTableName() {
		return "bd_billtype";
	}

	/**
	 * ����
	 */
	@Override
	public String getPkFieldCode() {
		return "pk_billtypeid";
	}

	@Override
	public String getWherePart() {
		return "pk_billtypecode like '263X-Cxx-%' or pk_billtypecode like '264X-Cxx-%' or pk_billtypecode = '2647'";// ��ѯ����
	}

	// �ñ༭�򱣴浽���ݿ��е�����
	public String[] getHiddenFieldCode() {

		return new String[] { "pk_billtypeid" };
	}

	// �����뿪����ʾ����
	public String getRefNameField() {
		return "billtypename";
	}

	// ���㿿£ʱ����ʾ����
	public String getRefCodeField() {
		return "pk_billtypecode";
	}

	@Override
	public String getRefTitle() {
		return "�������Ͳ���";
	}
}
