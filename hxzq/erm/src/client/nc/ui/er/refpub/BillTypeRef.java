package nc.ui.er.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 个人授权管理-单据类型参照
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
		return new String[] { "交易类型编码", "交易类型名称" };
	}

	/**
	 * 表名
	 */
	@Override
	public String getTableName() {
		return "bd_billtype";
	}

	/**
	 * 主键
	 */
	@Override
	public String getPkFieldCode() {
		return "pk_billtypeid";
	}

	@Override
	public String getWherePart() {
		return "pk_billtypecode like '263X-Cxx-%' or pk_billtypecode like '264X-Cxx-%' or pk_billtypecode = '2647'";// 查询条件
	}

	// 该编辑框保存到数据库中的内容
	public String[] getHiddenFieldCode() {

		return new String[] { "pk_billtypeid" };
	}

	// 焦点离开后显示内容
	public String getRefNameField() {
		return "billtypename";
	}

	// 焦点靠拢时后显示内容
	public String getRefCodeField() {
		return "pk_billtypecode";
	}

	@Override
	public String getRefTitle() {
		return "交易类型参照";
	}
}
