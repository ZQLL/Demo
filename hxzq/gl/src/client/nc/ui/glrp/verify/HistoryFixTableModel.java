package nc.ui.glrp.verify;

/**
 *  ���ܣ�HistoryPane��fixtable�� model
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 15:04:36)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
import nc.bs.logging.Logger;
import nc.ui.gl.accbook.TableFormatTackle;
import nc.ui.subjvery.ClientInfo;
import nc.vo.glrp.verify.VerifyDetailKey;


public class HistoryFixTableModel extends javax.swing.table.AbstractTableModel {
	TableFormatTackle vo = new TableFormatTackle();
	//VerifyDetailVO[] data = new VerifyDetailVO[0];

	/*����table�����ã��������ñ༭��*/
	private nc.ui.pub.beans.UITable m_uiTable;
	private IVerifyModel m_verifyModel;

	/*�����Ƿ�ɱ༭*/
	private boolean m_bEditable =true;
/**
 * TriAccTableModel ������ע�⡣
 * @param c java.lang.Class
 */
public HistoryFixTableModel() {
	super();
}
public Class getColumnClass(int col) {
	int iStyle = vo.getColFormatVOs()[col].getDataType();
	switch (iStyle) {
		case nc.vo.gl.accbook.DataTypeConst.STRING :
			return String.class;
		case nc.vo.gl.accbook.DataTypeConst.NUMBER :
			return Double.class;
		case 100 :
			return Boolean.class;
		//case 66 :
		//case 67 :
		//case 68 :
		//case 69 :
			//return Double.class;
		default :
			return String.class;
	}
}
/**
 * getColumnCount ����ע�⡣
 */
public int getColumnCount() {
	return vo.getFixedColSize();
}
/**
 * �����е�����
 */
public String getColumnName(int col) {
	return vo.getColFormatVOs()[col].getColName();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-9-5 14:57:22)
 * @return nc.ui.gl.balancebooks.BalanceTableFormatVO
 */
public TableFormatTackle getFormatVO() {
	return vo;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 10:47:21)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @return nc.ui.glrp.verify.IVerifyModel
 */
public IVerifyModel getModel() {
	//if(m_verifyModel==null){
		//m_verifyModel = new GlVerifyModel();
	//}
	return m_verifyModel;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-8-22 15:26:34)
 * @return int
 */
public int getRowCount() {
	try {
		//if (getModel().getHistroyData() == null)
			//return 0;
		//else
			//return getModel().getHistroyData().length;
			return getModel().getHistroyDataCount();
	} catch (Exception e) {
		Logger.error(e.getMessage(), e);
		return 0;
	}
}
/**
 * getValueAt ����ע�⡣
 */
public Object getValueAt(int arg1, int arg2) {
	int iKey = vo.getColFormatVOs()[arg2].getColKey();
	int iStyle=vo.getColFormatVOs()[arg2].getDataType();

	return getValueAt(arg1,iKey,iStyle);
}
/**
 * getValueAt ����ע�⡣
 */
public Object getValueAt(int arg1, int iKey,int iStyle) {
	Object objtemp = null;
	try {
		objtemp = getModel().getHistoryValueAt(arg1,iKey,iStyle);
	}
	catch (Exception e) {
		Logger.error(e.getMessage(), e);
		return null;
	}
	return objtemp;
}
/**
 * ָ�������Ƿ���Ա༭��
 * �������ڣ�(2001-5-15 18:13:00)
 * @return boolean
 */
public boolean isCellEditable(int row, int col) {
	if (!isEditable()) {
		return false;
	}
	try {
		int iKey = vo.getColFormatVOs()[col].getColKey();

		if (iKey == nc.vo.glrp.verify.GlVerifyDispLogKey.SELECTED) {
			/*���ñ༭��*/
			//getTable().getColumnModel().getColumn(col).setCellEditor(null);
			return true;
		}
		//if (!((nc.vo.pub.lang.UFBoolean) getValueAt(row,
		//VerifyDetailKey.SELECTED,
		//100))
		//.booleanValue()) {
		//return false;
		//}
		///*���ñ༭��*/
		////getTable().getColumnModel().getColumn(col).setCellEditor(null);
		//switch (iKey) {
		//case VerifyDetailKey.DEBIT_JS_Y :
		//case VerifyDetailKey.CREDIT_JS_Y :
		//return true;
		//case VerifyDetailKey.DEBIT_JS_F :
		//case VerifyDetailKey.CREDIT_JS_F :
		//return isFracEditable(row);
		//case VerifyDetailKey.DEBIT_JS_B :
		//case VerifyDetailKey.CREDIT_JS_B :
		//return isLocalEditable(row);
		//}
	} catch (Exception e) {
		Logger.error(e.getMessage(), e);
	}
	return false;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:44:43)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @return boolean
 */
public boolean isEditable() {
	return m_bEditable;
}
/**
 * �����Ƿ�ɱ༭
 * �������ڣ�(2003-5-11 18:01:40)
 * @return boolean
 * @param iRow int
 */
private boolean isFracEditable(int iRow) {
//	if (!ClientInfo.getInstance().bFracUsed()) {
//		return false;
//	}
//	try {
//		String sCurr = getValueAt(iRow, VerifyDetailKey.CURPK, 0).toString();
//		if (sCurr.equals(ClientInfo.getInstance().getPk_fracpk())) {
//			return false;
//		} else {
//			return true;
//		}
//	} catch (Exception e) {
//		Logger.error(e.getMessage(), e);
//	}
//	return true;
	return false;
}
/**
 * �����Ƿ�ɱ༭
 * �������ڣ�(2003-5-11 18:01:40)
 * @return boolean
 * @param iRow int
 */
private boolean isLocalEditable(int iRow) {
	try {
		String sCurr = getValueAt(iRow, VerifyDetailKey.CURPK, 0).toString();
		if (sCurr.equals(ClientInfo.getInstance().getLocalPk())) {
			return false;
		} else {
			return true;
		}
	} catch (Exception e) {
		Logger.error(e.getMessage(), e);
	}
	return true;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:44:43)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param newEditable boolean
 */
public void setEditable(boolean newEditable) {
	m_bEditable = newEditable;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-9-5 14:57:52)
 * @param p_vo nc.ui.gl.balancebooks.BalanceTableFormatVO
 */
public void setFormatVO(TableFormatTackle p_vo) {
	vo=p_vo;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 10:47:21)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param newModel nc.ui.glrp.verify.IVerifyModel
 */
public void setModel(IVerifyModel newModel) {
	m_verifyModel = newModel;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 10:20:39)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param newTable nc.ui.pub.beans.UITable
 */
public void setTable(nc.ui.pub.beans.UITable newTable) {
	m_uiTable = newTable;
}
/**
 *  ���ܣ�����ֵ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 10:51:15)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param oValue java.lang.Object
 * @param iRow int
 * @param iCol int
 */
public void setValueAt(Object oValue, int iRow, int iCol) {
	try {
		int iKey = vo.getColFormatVOs()[iCol].getColKey();
		int iStyle = vo.getColFormatVOs()[iCol].getDataType();
		getModel().setHistoryValueAt(oValue,iRow, iKey);
		fireTableCellUpdated(iRow, iCol);
	} catch (Exception e) {
		Logger.error(e.getMessage(), e);
	}
}
}