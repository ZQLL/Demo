package nc.ui.glrp.verify;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Comparator;

import nc.bs.logging.Logger;
import nc.ui.gl.accbook.ALLChooseTableUtil;
import nc.ui.gl.datacache.CurrencyDataCache;
import nc.ui.subjvery.ClientInfo;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.gl.accbook.ColFormatVO;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.FormatVO;
import nc.vo.glrp.verify.VerifyDetailKey;
/**
 *  ���ܣ���ʾ��������pane
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-4 15:49:59)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
public class VerifyDataPane extends nc.ui.pub.beans.UIPanel implements javax.swing.event.ListSelectionListener {

	class MyMouseMotionAdapter extends java.awt.event.MouseMotionAdapter {
		public void mouseDragged(java.awt.event.MouseEvent e) {
			if (e.getSource() == getFixTable().getTableHeader()) {
				if (getFixTable().getTableHeader().getResizingColumn() != null) {
					onColResized();
				}
			}
		}
	}

	class IvjEventHandler implements java.awt.event.MouseListener {
		public void mouseClicked(java.awt.event.MouseEvent e) {

		};
		public void mouseEntered(java.awt.event.MouseEvent e) {
		};
		public void mouseExited(java.awt.event.MouseEvent e) {

		};
		@SuppressWarnings("unchecked")
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getSource() == VerifyDataPane.this.getFixTable().getTableHeader())  {
				connEtoC1(e);
				if(null != m_FixTableModel.getModel().getData(m_bDebit)){
					Arrays.sort(m_FixTableModel.getModel().getData(m_bDebit), new Comparator(){
	
						public int compare(Object o1, Object o2) {
							// TODO Auto-generated method stub
							int iKey = m_TableFormatTackle.getColFormatVOs()[m_col.getModelIndex()].getColKey();
							
			
							Object one = ((nc.vo.glrp.verify.VerifyDetailVO)o1).getAttrByKey(iKey);
							
							Object two = ((nc.vo.glrp.verify.VerifyDetailVO)o2).getAttrByKey(iKey); 
							if(iKey == VerifyDetailKey.DISP_VOUCHERNO) {
						    if(one instanceof Integer && two instanceof Integer) {
						    	int vouchertypecmp = ((nc.vo.glrp.verify.VerifyDetailVO)o1).getPk_vouchertype().compareTo(((nc.vo.glrp.verify.VerifyDetailVO)o2).getPk_vouchertype());
						    	
						    	return vouchertypecmp != 0 ? (order *vouchertypecmp) : (order * ((Integer)one).compareTo((Integer)two));
						    }
							}
						    String str1 = one== null ? "":one.toString();
						    String str2 = two == null? "":two.toString();
							return order * str1.compareTo(str2);
						    
						}
						
					});
				}
				
				 VerifyDataPane.this.getFixTable().updateUI();
				 VerifyDataPane.this.gettablepane().getTable().updateUI();
				 order = -1 * order;
			}
			
			  
		};
		public void mouseReleased(java.awt.event.MouseEvent e) {

		};
	}
	
	private int order = 1;
	private nc.ui.pub.beans.UITablePane ivjtablepane = null;
	private TailPane ivjTailMsgPane = null;
	private nc.ui.gl.accbook.TableFormatTackle m_TableFormatTackle = null;
	/*�跽��־*/
	private boolean m_bDebit;
	/*����model*/
	private IVerifyModel m_verifyModel;
	private VerifyTableModel m_TableModel;
	/*�Ƿ�ɱ༭*/
	private boolean m_bEditable = true;
	private javax.swing.DefaultCellEditor m_Celleditor = null;
	/*�̶���tablemodel*/
	private VerifyFixTableModel m_FixTableModel;
	private nc.ui.pub.beans.UITable m_FixTable;
	/*�̶��е�ǰ��ѡ����*/
	private javax.swing.table.TableColumn m_col = null;
	private int m_maxWidth = 0;
	/*��ʽ��vo*/
	private FormatVO m_format = new FormatVO();
	private IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private nc.ui.pub.beans.UIPanel ivjUIPanel1 = null;
	private TailPane ivjTailPaneT = null;
	private TailPane ivjTailPaneU = null;
/**
 * VerifyDataPane ������ע�⡣
 */
public VerifyDataPane() {
	super();
	initialize();
}
/**
 * VerifyDataPane ������ע�⡣
 * @param p0 java.awt.LayoutManager
 */
public VerifyDataPane(java.awt.LayoutManager p0) {
	super(p0);
}
/**
 * VerifyDataPane ������ע�⡣
 * @param p0 java.awt.LayoutManager
 * @param p1 boolean
 */
public VerifyDataPane(java.awt.LayoutManager p0, boolean p1) {
	super(p0, p1);
}
/**
 * VerifyDataPane ������ע�⡣
 * @param p0 boolean
 */
public VerifyDataPane(boolean p0) {
	super(p0);
}
/**
 *  ���ܣ����ݱ����������Ľ��༭�������С��λ��
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-21 9:28:06)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param sPk_curr java.lang.String
 */
private void changeEditorPrec(String sPk_curr) {
	if (sPk_curr == null || sPk_curr.trim().length() == 0) {
		return;
	}
	CurrtypeVO voCurr=CurrencyDataCache.getInstance().getCurrtypeBypk(ClientInfo.getInstance().getPk_corp(),sPk_curr);
	if (voCurr != null) {
		nc.ui.pub.beans.UITextField tf =
			(nc.ui.pub.beans.UITextField) getCelleditor().getComponent();
		tf.setNumPoint(voCurr.getCurrdigit().intValue());
	}
}
/* ���棺�˷������������ɡ� */
private void connEtoC1(java.awt.event.MouseEvent e) {
    try {
        // user code begin {1}
        m_col =
            getFixTable().getColumnModel().getColumn(
                getFixTable().getColumnModel().getColumnIndexAtX(e.getX()));
        m_maxWidth =
            getTablepane().getWidth()
                - (getFixTable().getColumnModel().getTotalColumnWidth()
                    - m_col.getPreferredWidth())
                - 10;
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
}
/**
 *  ���ܣ�double���͵��ı��༭��
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-14 14:05:12)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return javax.swing.DefaultCellEditor
 */
public javax.swing.DefaultCellEditor getCelleditor() {
	if(m_Celleditor==null){
		nc.ui.pub.beans.UITextField tf = new nc.ui.pub.beans.UITextField();
		tf.setTextType("TextDbl");
		m_Celleditor = new javax.swing.DefaultCellEditor(tf);
	}
	return m_Celleditor;
}
/**
 *  ���ܣ��õ���ǰѡ�е�����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-13 11:36:43)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.vo.glrp.verify.VerifyDetailVO
 */
public nc.vo.glrp.verify.VerifyDetailVO getCurData() {
	int iRow = getTablepane().getTable().getSelectedRow();
	if(iRow>=0){
		return getModel().getData(isDebit())[iRow];
	}
	return null;
}
/**
 *  ���ܣ��̶���table
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-19 14:24:49)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.pub.beans.UITable
 */
public nc.ui.pub.beans.UITable getFixTable() {
	if(m_FixTable==null){
		m_FixTable = new nc.ui.pub.beans.UITable();
		m_FixTable.setModel(getFixTableModel());
		m_FixTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		m_FixTable.getSelectionModel().addListSelectionListener(this);
		m_FixTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
	}
	return m_FixTable;
}
/**
 *  ���ܣ��̶��е�tablemodel
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-19 14:24:00)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.glrp.verify.VerifyFixTableModel
 */
public VerifyFixTableModel getFixTableModel() {
	if(m_FixTableModel==null){
		m_FixTableModel = new VerifyFixTableModel();
	}
	return m_FixTableModel;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 9:29:20)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.glrp.verify.IVerifyModel
 */
public IVerifyModel getModel() {
	return m_verifyModel;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-12 19:37:01)
 * @return nc.ui.gl.accbook.TableFormatTackle
 */
private nc.ui.gl.accbook.TableFormatTackle getTableFormatTackle() {
	if(m_TableFormatTackle==null){
		m_TableFormatTackle = new nc.ui.gl.accbook.TableFormatTackle();
	}
	return m_TableFormatTackle;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 9:44:48)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.glrp.verify.VerifyTableModel
 */
private VerifyTableModel getTableModel() {
	if(m_TableModel==null){
		m_TableModel = new VerifyTableModel();
		m_TableModel.setDebit(isDebit());
		m_TableModel.setTable(getTablepane().getTable());
	}
	return m_TableModel;
}
/**
 * ���� tablepane ����ֵ��
 * @return nc.ui.pub.beans.UITablePane
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITablePane gettablepane() {
	if (ivjtablepane == null) {
		try {
			ivjtablepane = new nc.ui.pub.beans.UITablePane();
			ivjtablepane.setName("tablepane");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjtablepane;
}
/**
 * ���� tablepane ����ֵ��
 * @return nc.ui.pub.beans.UITablePane
 */
/* ���棺�˷������������ɡ� */
public nc.ui.pub.beans.UITablePane getTablepane() {
	if (ivjtablepane == null) {
		try {
			ivjtablepane = new nc.ui.pub.beans.UITablePane();
			ivjtablepane.setName("tablepane");
			ivjtablepane.setAutoscrolls(true);
			// user code begin {1}
			ivjtablepane.getTable().getSelectionModel().addListSelectionListener(this);
			
			getTablepane().setCorner(javax.swing.JScrollPane.UPPER_LEFT_CORNER, getFixTable().getTableHeader());
			getTablepane().setRowHeaderView(getFixTable());

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjtablepane;
}
/**
 * ���� TailMsgPane ����ֵ��
 * @return nc.ui.glrp.verify.TailPane
 */
/* ���棺�˷������������ɡ� */
private TailPane getTailMsgPane() {
	if (ivjTailMsgPane == null) {
		try {
			ivjTailMsgPane = new nc.ui.glrp.verify.TailPane();
			ivjTailMsgPane.setName("TailMsgPane");
			// user code begin {1}
			ivjTailMsgPane.setMsgName(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000280")/*@res "���κ������:"*/);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTailMsgPane;
}
/**
 * ���� TailPane2 ����ֵ��
 * @return nc.ui.glrp.verify.TailPane
 */
/* ���棺�˷������������ɡ� */
private TailPane getTailPaneT() {
	if (ivjTailPaneT == null) {
		try {
			ivjTailPaneT = new nc.ui.glrp.verify.TailPane();
			ivjTailPaneT.setName("TailPaneT");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTailPaneT;
}
/**
 * ���� TailPane1 ����ֵ��
 * @return nc.ui.glrp.verify.TailPane
 */
/* ���棺�˷������������ɡ� */
private TailPane getTailPaneU() {
	if (ivjTailPaneU == null) {
		try {
			ivjTailPaneU = new nc.ui.glrp.verify.TailPane();
			ivjTailPaneU.setName("TailPaneU");
			// user code begin {1}
			ivjTailPaneU.setMsgName(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000281")/*@res "δ�������:"*/);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTailPaneU;
}
/**
 * ���� UIPanel1 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIPanel getUIPanel1() {
	if (ivjUIPanel1 == null) {
		try {
			ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
			ivjUIPanel1.setName("UIPanel1");
			ivjUIPanel1.setLayout(new java.awt.GridLayout());
			getUIPanel1().add(getTailMsgPane(), getTailMsgPane().getName());
			getUIPanel1().add(getTailPaneU(), getTailPaneU().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPanel1;
}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		Logger.error(exception.getMessage(), exception);
		throw new GlBusinessException(exception.getMessage());
	}
	
	private ColFormatVO[] initColFormatVO(boolean bFlag) {
	    ColFormatVO[] colFormats = new ColFormatVO[31];
	    int iCol = -1;

	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.SELECTED);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_SELECT());
	    colFormats[iCol].setColWidth(30);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.CENTER);
	    colFormats[iCol].setDataType(100);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.SUBJCODE);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_SUBJCODE());
	    colFormats[iCol].setColWidth(60);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.SUBJNAME);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_SUBJNAME());
	    colFormats[iCol].setColWidth(60);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CURCODE);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_CURRCODE());
	    colFormats[iCol].setColWidth(50);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CURNAME);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_CURRNAME());
	    colFormats[iCol].setColWidth(50);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.VOUCHDATE);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_VOUCHERDATE());
	    colFormats[iCol].setColWidth(70);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.BUSINESSDATE);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_BUSINESSDATE());
	    colFormats[iCol].setColWidth(70);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.VERIFYNO);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_VERIFYNO());
	    colFormats[iCol].setColWidth(40);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	    
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.NETBANKFLAG);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_NETBANKFLAG());
	    colFormats[iCol].setColWidth(100);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DISP_VOUCHERNO);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_VOUCHERNO());
	    colFormats[iCol].setColWidth(60);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DETAILINDEX);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_DETAILIDX());
	    colFormats[iCol].setColWidth(40);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.ASSVO);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_ASS());
	    colFormats[iCol].setColWidth(100);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DIGEST);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_DIGEST());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(true);
	    colFormats[iCol].setVisiablity(true);
	    colFormats[iCol].setMultiHeadStr(null);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_MNY_S);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_QUANTITY());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_NAME());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_QUANTITY);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_MNY_Y);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_NAME());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_MNY_B);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_NAME());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);
	 
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_MNY_S);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_QUANTITY());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_NAME());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_QUANTITY);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_MNY_Y);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(!isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_NAME());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_MNY_B);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(!isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_NAME());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_JS_S);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_QUANTITY());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CURRENT_VERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_QUANTITY);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_JS_Y);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CURRENT_VERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);
	    
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_JS_B);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CURRENT_VERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_JS_S);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_QUANTITY());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CURRENT_VERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_QUANTITY);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_JS_Y);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(!isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CURRENT_VERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_JS_B);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(!isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CURRENT_VERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);
	  
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_UNJS_S);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_QUANTITY());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_UNVERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_QUANTITY);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_UNJS_Y);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_UNVERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.DEBIT_UNJS_B);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_UNVERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);
	    
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_UNJS_S);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_QUANTITY());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(false);
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_UNVERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_QUANTITY);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_UNJS_Y);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(!isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_UNVERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);
	
	    iCol++;
	    colFormats[iCol] = new ColFormatVO();
	    colFormats[iCol].setColKey(VerifyDetailKey.CREDIT_UNJS_B);
	    colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
	    colFormats[iCol].setColWidth(80);
	    colFormats[iCol].setFrozen(false);
	    colFormats[iCol].setVisiablity(!isDebit());
	    colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
	    colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_MNY_UNVERIFY());
	    colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);
	
	    return colFormats;
	}
/**
 * ��ʼ������
 * @exception java.lang.Exception �쳣˵����
 */
/* ���棺�˷������������ɡ� */
private void initConnections() throws java.lang.Exception {
    // user code begin {1}
    getFixTable().getTableHeader().addMouseMotionListener(
        new MyMouseMotionAdapter());
    getFixTable().getTableHeader().addMouseListener(ivjEventHandler);
    // user code end
//    gettablepane().getTable().getTableHeader().addMouseListener(ivjEventHandler);

}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-10 22:27:51)
 */
public void initData() {
	getTailMsgPane().initData();
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		// user code begin {1}
		initConnections();
		// user code end
		setName("VerifyDataPane");
		setLayout(new java.awt.BorderLayout());
		add(getTablepane(), BorderLayout.CENTER);
		add(getUIPanel1(), BorderLayout.SOUTH);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	initTable(isDebit());
	initData();
	// user code end
}
private void initTable(boolean bFlag) {
	nc.ui.pub.beans.UITable table = getTablepane().getTable();
	//nc.ui.gl.balancesubjass.TableModel model=new nc.ui.gl.balancesubjass.TableModel();
	table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
	table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
	//�õ�TableFormatVO
	ColFormatVO[] colVOs = initColFormatVO(bFlag);
	//m_TableFormatTackle =new nc.ui.gl.accbook.TableFormatTackle();
	getTableFormatTackle().setColFormatVO(colVOs);
	table.setAutoscrolls(true);
	//��TableModel������TableFormatVO
	getTableModel().setFormatVO(getTableFormatTackle());
	getFixTableModel().setFormatVO(getTableFormatTackle());

	//��Table������TableModel
	table.setModel(getTableModel());

	getTableModel().fireTableStructureChanged();
	getFixTableModel().fireTableStructureChanged();

	//����Table,fixTable�ĸ�ʽ
	getTableFormatTackle().setColWidth(table);
	getTableFormatTackle().setVisiablity(table);
	getTableFormatTackle().setMultiHead(table);

	getTableFormatTackle().setFixColWidth(getFixTable());
	getTableFormatTackle().setFixVisiablity(getFixTable());
	getTableFormatTackle().setFixMultiHead(getFixTable());
	getTableFormatTackle().setAlignment(table);
	ALLChooseTableUtil.addCheckCellRenderer(getFixTable(),0);
	setColEditor(colVOs, table);
	
}
/**
 *  ���ܣ��Ƿ�跽
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 9:27:55)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return boolean
 */
public boolean isDebit() {
	return m_bDebit;
}
/**
 *  ���ܣ��Ƿ�ɱ༭
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:40:30)
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
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		VerifyDataPane aVerifyDataPane;
		aVerifyDataPane = new VerifyDataPane();
		frame.setContentPane(aVerifyDataPane);
		frame.setSize(aVerifyDataPane.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("nc.ui.pub.beans.UIPanel �� main() �з����쳣");
		nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}
}
private void onColResized() {
	if (getFixTable().getColumnModel().getTotalColumnWidth()
		> getTablepane().getWidth() - 10) {
		m_col.setPreferredWidth(m_maxWidth);
		m_col.setWidth(m_maxWidth);
	}
	getFixTable().setPreferredScrollableViewportSize(
		new java.awt.Dimension(
			getFixTable().getColumnModel().getTotalColumnWidth(),
			getFixTable().getHeight()));
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:09:30)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 */
public void print() {}
/**
 * ��Ӧ���Ա仯�¼�
 */
public void propertyChange(java.beans.PropertyChangeEvent evt) {
	int iKey = Integer.parseInt(evt.getPropertyName());
	switch (iKey) {
		case VerifyDetailKey.SELECTED :
		case VerifyDetailKey.DEBIT_JS_S :
		case VerifyDetailKey.DEBIT_JS_Y :
		case VerifyDetailKey.DEBIT_JS_B :
		case VerifyDetailKey.CREDIT_JS_S :
		case VerifyDetailKey.CREDIT_JS_Y :
		case VerifyDetailKey.CREDIT_JS_B :
			updateui(evt);
			break;
		case VerifyDetailKey.DEBIT_SUM_S :
		case VerifyDetailKey.CREDIT_SUM_S :
			break;
		case VerifyDetailKey.DEBIT_SUM_Y :
		case VerifyDetailKey.CREDIT_SUM_Y :
			getTailMsgPane().setValue_y(evt.getNewValue().toString());
			break;
			
		case VerifyDetailKey.DEBIT_SUM_B :
		case VerifyDetailKey.CREDIT_SUM_B :
			getTailMsgPane().setValue_b(evt.getNewValue().toString());
			break;
		case VerifyDetailKey.ENT_CREDIT_Y :
		case VerifyDetailKey.ENT_DEBIT_Y :
			changeEditorPrec(evt.getNewValue().toString());
			break;
		case VerifyDetailKey.ENT_CREDIT_B :
		case VerifyDetailKey.ENT_DEBIT_B :
			changeEditorPrec(ClientInfo.getInstance().getLocalPk());
			break;
		case VerifyDetailKey.STAT_DISP_CHANGE :
			resetFormat((FormatVO) evt.getNewValue());
			break;

		/*add by lwg*/
		case VerifyDetailKey.DEBIT_SUMT_Y:
		case VerifyDetailKey.CREDIT_SUMT_Y:
			getTailPaneT().setValue_y(evt.getNewValue().toString());
			break;

		case VerifyDetailKey.DEBIT_SUMU_Y:
		case VerifyDetailKey.CREDIT_SUMU_Y:
			getTailPaneU().setValue_y(evt.getNewValue().toString());
			break;

		case VerifyDetailKey.DEBIT_SUMU_B:
		case VerifyDetailKey.CREDIT_SUMU_B:
			getTailPaneU().setValue_b(evt.getNewValue().toString());
			break;

	}
}
	public void resetFormat(nc.vo.glrp.verify.FormatVO format) {
		m_format = format;
		java.util.ArrayList arr = new java.util.ArrayList();
	
		arr.add(new int[] { VerifyDetailKey.SELECTED });
		if (format.isShowSubjCode() || !isEditable()) {
			arr.add(new int[] { VerifyDetailKey.SUBJCODE });
			arr.add(new int[] { VerifyDetailKey.SUBJNAME });
		}
		arr.add(new int[] { VerifyDetailKey.VOUCHDATE });
		arr.add(new int[] { VerifyDetailKey.BUSINESSDATE });
		arr.add(new int[] { VerifyDetailKey.VERIFYNO });
		arr.add(new int[] { VerifyDetailKey.DISP_VOUCHERNO });
		arr.add(new int[] { VerifyDetailKey.DETAILINDEX });
		arr.add(new int[] { VerifyDetailKey.ASSVO });
		arr.add(new int[] { VerifyDetailKey.DIGEST });
		arr.add(new int[] { VerifyDetailKey.NETBANKFLAG });
	
		if (isDebit()) {
			arr.add(new int[] { VerifyDetailKey.DEBIT_MNY_Y });
			arr.add(new int[] { VerifyDetailKey.DEBIT_MNY_B });
			arr.add(new int[] { VerifyDetailKey.DEBIT_JS_Y });
			arr.add(new int[] { VerifyDetailKey.DEBIT_JS_B });
			arr.add(new int[] { VerifyDetailKey.DEBIT_UNJS_Y });
			arr.add(new int[] { VerifyDetailKey.DEBIT_UNJS_B });
		} else {
			arr.add(new int[] { VerifyDetailKey.CREDIT_MNY_Y });
			arr.add(new int[] { VerifyDetailKey.CREDIT_MNY_B });
			arr.add(new int[] { VerifyDetailKey.CREDIT_JS_Y });
			arr.add(new int[] { VerifyDetailKey.CREDIT_JS_B });
			arr.add(new int[] { VerifyDetailKey.CREDIT_UNJS_Y });
			arr.add(new int[] { VerifyDetailKey.CREDIT_UNJS_B });
		}
		setTableColVisibility(arr);
		updateUI();
	
	}
/**
 * ȫѡ
 * �������ڣ�(2003-5-12 22:33:16)
 */
public void selectAll() {
	int iRow = getTableModel().getRowCount();
	if (iRow <= 0)
		return;
	nc.vo.pub.lang.UFBoolean TRUE = nc.vo.pub.lang.UFBoolean.TRUE;
	for (int i = 0; i < iRow; i++) {
		getFixTableModel().setValueAt(TRUE, i, 0);
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-12 22:34:03)
 */
public void selectNone() {
	int iRow = getTableModel().getRowCount();
	if (iRow <= 0)
		return;
	nc.vo.pub.lang.UFBoolean FALSE = nc.vo.pub.lang.UFBoolean.FALSE;
	for(int i=0;i<iRow;i++){
		getFixTableModel().setValueAt(FALSE,i,0);
	}
}
/**
 *  ���ܣ������и�ʽ����Ϣ�е��Ƿ���ʾ��Ϣ������������table���б༭��
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-15 10:25:04)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param colFormatvos nc.vo.gl.accbook.ColFormatVO[]
 * @param table nc.ui.pub.beans.UITable
 */
private void setColEditor(
	ColFormatVO[] colFormatvos,
	nc.ui.pub.beans.UITable table) {
	if (colFormatvos == null || colFormatvos.length < 1 || table == null) {
		return;
	}
	int j = 0;
	int length = colFormatvos.length;
	for (int i = 0; i < length; i++) {
		if (colFormatvos[i].isVisiablity()) {
			if (colFormatvos[i].getDataType() == IGlVerifyConst.STYLE_QUANTITY
//				|| colFormatvos[i].getDataType() == 68
				|| colFormatvos[i].getDataType() == IGlVerifyConst.STYLE_AMOUNT
				|| colFormatvos[i].getDataType() == IGlVerifyConst.STYLE_LOCAL_AMOUNT
				|| colFormatvos[i].getDataType() == IGlVerifyConst.STYLE_GROUP_AMOUNT
				|| colFormatvos[i].getDataType() == IGlVerifyConst.STYLE_GLOBAL_AMOUNT) {
				table.getColumnModel().getColumn(j-getFixTable().getColumnCount()).setCellEditor(getCelleditor());
			}
			j++;
		}
	}
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 9:27:55)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param newDebit boolean
 */
public void setDebit(boolean newDebit) {
	//if(newDebit = m_bDebit){
		//return ;
	//}
	m_bDebit = newDebit;
	//initTable(newDebit);
	resetFormat(m_format);
	getTableModel().setDebit(isDebit());
	getFixTableModel().setDebit(isDebit());
	getTailMsgPane().setDebit(newDebit);
	updateMsg();

}
/**
 *  ���ܣ������Ƿ�ɱ༭
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:40:30)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param newEditable boolean
 */
public void setEditable(boolean newEditable) {
	m_bEditable = newEditable;
	getTableModel().setEditable(newEditable);
	getFixTableModel().setEditable(newEditable);
}
/**
 *  ���ܣ�ѡ�е�һ������
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 10:40:42)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 */
public void setFirstDataSelected() throws Exception {
    try {
        if (getFixTableModel().getRowCount() > 0) {
            getFixTableModel().setValueAt(VerifyMsg.TRUE, 0, 0);
        }
    } catch (Exception e) {
        Logger.error(e.getMessage(), e);
        throw new nc.vo.pub.BusinessException(VerifyMsg.getMSG_SELECTDATA_ERROR());
    }
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-19 14:24:49)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param newFixTable nc.ui.pub.beans.UITable
 */
public void setFixTable(nc.ui.pub.beans.UITable newFixTable) {
	m_FixTable = newFixTable;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 9:29:20)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param newModel nc.ui.glrp.verify.IVerifyModel
 */
public void setModel(IVerifyModel newModel) {
	m_verifyModel = newModel;
	getTableModel().setModel(newModel);
	getFixTableModel().setModel(newModel);
}
/**
 *�Ѳ����е�������Ϊ�ɼ�����
 *������Ϊһ��һάint���飬��������еĵ�һ��Ԫ��==BalanceSubjAssBSKey.K_AssVOs���ڶ���Ԫ��Ϊ��Ӧ�������������е�λ�ã�������Ԫ��Ϊ��������Ƽ�ֵ
 *		���� ������ֻ����һ��Ԫ��Ϊ�е�Key
 */
private void setTableColVisibility(java.util.ArrayList colsNum) {
	nc.ui.pub.beans.UITable table = getTablepane().getTable();
	//nc.ui.pub.beans.UITable fixTable = (nc.ui.pub.beans.UITable) (getUITablePane().getRowHeader().getComponents()[0]);
	nc.ui.gl.accbook.TableFormatTackle vo =
		((VerifyTableModel) table.getModel()).getFormatVO();
	ColFormatVO[] colFormats = vo.getColFormatVOs();

	for (int i = 0; i < colFormats.length; i++) {
		boolean flag = false;

		for (int j = 0; j < colsNum.size(); j++) {
			int[] iKey = (int[]) colsNum.get(j);
			if (colFormats[i].getColKey() == iKey[0]) {
				//if (colFormats[i].getColKey() == BalanceSubjAssBSKey.K_AssVOs) {
				//int[] info = (int[]) colFormats[i].getUserData();
				//if (info[0] == iKey[1] && info[1] == iKey[2])
				//flag = true;
				//}
				//else
				flag = true;

				if (flag)
					break;
			}

		}

		colFormats[i].setVisiablity(flag);

	}

	vo.setFixVisiablity(getFixTable());
	vo.setVisiablity(table);
	vo.setMultiHead(table);

	vo.setAlignment(table);
	vo.setFixMultiHead(getFixTable());
}
/**
 *  ���ܣ������༭
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 13:36:34)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 */
public void stopedit() {
	if(getTablepane().getTable().getCellEditor()!=null){
		getTablepane().getTable().getCellEditor().stopCellEditing();
		getTablepane().getTable().setCellEditor(null);
	}
	if(getFixTable().getCellEditor()!=null){
		getFixTable().getCellEditor().stopCellEditing();
		getFixTable().setCellEditor(null);
	}

}
/**
 *  ���ܣ����Է���
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 12:53:45)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 */
public final void updateMsg() {
	//if(isDebit()){
		//getTailMsgPane().setValue("�跽");
	//}else{
		//getTailMsgPane().setValue("����");
	//}
}
/**
 * ˢ��ui
 * �������ڣ�(2003-5-10 23:05:30)
 */
private void updateui(java.beans.PropertyChangeEvent evt) {
	getTablepane().getTable().setRowHeight(
		getTablepane().getTable().getRowHeight());
	getFixTable().setRowHeight(getFixTable().getRowHeight());
	//getTablepane().getTable().invalidate();
	//getTablepane().getTable().setRowHeight(getTablepane().getTable().getRowHeight());
}
 /**
   * Called whenever the value of the selection changes.
   * @param e the event that characterizes the change.
   */
public void valueChanged(javax.swing.event.ListSelectionEvent e) {
	try{
	if(e.getSource()==getFixTable().getSelectionModel())
	{
		int i=getFixTable().getSelectedRow();
		getTablepane().getTable().setRowSelectionInterval(i,i);//getSelectionModel().set
	}
	if(e.getSource()==getTablepane().getTable().getSelectionModel())
	{
		int i=getTablepane().getTable().getSelectedRow();
		getFixTable().setRowSelectionInterval(i,i);//getSelectionModel().set
	}
	}catch(Exception ex)
	{
	}
}
}