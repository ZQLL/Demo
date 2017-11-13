package nc.ui.glrp.verify;

/**
 *  ���ܣ����˺���ui
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-8 13:41:31)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.gl.datacache.AccountCache;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.vo.bd.account.AccountVO;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.VerifyDetailKey;
import nc.vo.glrp.verifyobj.VerifyObjHeaderVO;



@SuppressWarnings("serial")
public class GlVerifyUI extends nc.ui.pub.beans.UIPanel implements IVerifyUI {
	private nc.ui.pub.beans.UIDesktopPane ivjDeskTopPane = null;
	private nc.ui.pub.beans.UIInternalFrame ivjBottomFrame = null;
	private nc.ui.pub.beans.UIPanel ivjJInternalFrameContentPane = null;
	private nc.ui.pub.beans.UIPanel ivjJInternalFrameContentPane1 = null;
	private nc.ui.pub.beans.UIInternalFrame ivjTopFrame = null;
	private VerifyDataPane ivjBottomPane = null;
	private VerifyDataPane ivjTopPane = null;
	private IVerifyModel m_verifyModel;
	private IVerifyView m_verifyView;
	/*���״̬*/
	private int m_iStyle = nc.vo.glrp.pub.VerifyMsg.TYPE_VERIFY;
	/*�����������ʾ���*/
	private boolean m_bVertical= false;

//	private JScrollBar bar_1 = null, bar_2 = null;
/**
 * GlVerifyUI ������ע�⡣
 */
public GlVerifyUI() {
	super();
	initialize();
}
/**
 * GlVerifyUI ������ע�⡣
 * @param p0 java.awt.LayoutManager
 */
public GlVerifyUI(java.awt.LayoutManager p0) {
	super(p0);
}
/**
 * GlVerifyUI ������ע�⡣
 * @param p0 java.awt.LayoutManager
 * @param p1 boolean
 */
public GlVerifyUI(java.awt.LayoutManager p0, boolean p1) {
	super(p0, p1);
}
/**
 * GlVerifyUI ������ע�⡣
 * @param p0 boolean
 */
public GlVerifyUI(boolean p0) {
	super(p0);
}
/**
 * ���� BottomFrame ����ֵ��
 * @return javax.swing.JInternalFrame
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIInternalFrame getBottomFrame() {
	if (ivjBottomFrame == null) {
		try {
			ivjBottomFrame = new nc.ui.pub.beans.UIInternalFrame(VerifyMsg.getDISP_CREDIT(),true,false,true,true);
			ivjBottomFrame.setName("BottomFrame");
			ivjBottomFrame.setBounds(0, 209, 769, 207);
			ivjBottomFrame.setMaximizable(true);
			ivjBottomFrame.setResizable(true);
			getBottomFrame().setContentPane(getJInternalFrameContentPane1());
			// user code begin {1}
			//getBottomFrame().setSelected(false);
			getBottomFrame().setTitle(VerifyMsg.getDISP_CREDIT());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBottomFrame;
}
/**
 * ���� BottomPane ����ֵ��
 * @return nc.ui.glrp.verify.VerifyDataPane
 */
/* ���棺�˷������������ɡ� */
private VerifyDataPane getBottomPane() {
	if (ivjBottomPane == null) {
		try {
			ivjBottomPane = new nc.ui.glrp.verify.VerifyDataPane();
			ivjBottomPane.setName("BottomPane");
			// user code begin {1}
			//ivjBottomPane.setDebit(false);
			ivjBottomPane.setModel(getModel());
			ivjBottomPane.setEditable(true);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBottomPane;
}
/**
 *  ���ܣ��õ��������ݽ���
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:28:46)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.glrp.verify.VerifyDataPane
 */
private VerifyDataPane getCreditPane() {
	if (!getBottomPane().isDebit()) {
		return getBottomPane();
	} else {
		return getTopPane();
	}
}
/**
 *  ���ܣ��õ���ǰѡ�е�ƾ֤id
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-13 11:31:00)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return java.lang.String
 */
public String getCurrVouchId() throws Exception {
	if (getTopFrame().isSelected()) {
		nc.vo.glrp.verify.VerifyDetailVO vo = getTopPane().getCurData();
		if (vo != null) {
			return vo.getPk_voucher();
		} else {
			throw new nc.vo.pub.BusinessException(VerifyMsg.getMSG_NOSELECTDATA());
		}
	} else {
		nc.vo.glrp.verify.VerifyDetailVO vo = getBottomPane().getCurData();
		if (vo != null) {
			return vo.getPk_voucher();
		} else {
			throw new nc.vo.pub.BusinessException(VerifyMsg.getMSG_NOSELECTDATA());
		}
	}
}
/**
 *  ���ܣ��õ��跽���ݽ���
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:28:46)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.glrp.verify.VerifyDataPane
 */
private VerifyDataPane getDebitPane() {
	if (getTopPane().isDebit()) {
		return getTopPane();
	} else {
		return getBottomPane();
	}
}
/**
 * ���� DeskTopPane ����ֵ��
 * @return javax.swing.JDesktopPane
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIDesktopPane getDeskTopPane() {
	if (ivjDeskTopPane == null) {
		try {
			ivjDeskTopPane = new nc.ui.pub.beans.UIDesktopPane();
			ivjDeskTopPane.setName("DeskTopPane");
			//ivjDeskTopPane.setBackground(new java.awt.Color(245,233,209));
			getDeskTopPane().add(getTopFrame(), getTopFrame().getName());
			getDeskTopPane().add(getBottomFrame(), getBottomFrame().getName());
			// user code begin {1}
			getTopFrame().setVisible(true);
			getBottomFrame().setVisible(true);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDeskTopPane;
}
/**
 * ���� JInternalFrameContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIPanel getJInternalFrameContentPane() {
	if (ivjJInternalFrameContentPane == null) {
		try {
			ivjJInternalFrameContentPane = new nc.ui.pub.beans.UIPanel();
			ivjJInternalFrameContentPane.setName("JInternalFrameContentPane");
			ivjJInternalFrameContentPane.setLayout(new java.awt.BorderLayout());
			getJInternalFrameContentPane().add(getTopPane(), BorderLayout.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJInternalFrameContentPane;
}
/**
 * ���� JInternalFrameContentPane1 ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIPanel getJInternalFrameContentPane1() {
	if (ivjJInternalFrameContentPane1 == null) {
		try {
			ivjJInternalFrameContentPane1 = new nc.ui.pub.beans.UIPanel();
			ivjJInternalFrameContentPane1.setName("JInternalFrameContentPane1");
			ivjJInternalFrameContentPane1.setLayout(new java.awt.BorderLayout());
			getJInternalFrameContentPane1().add(getBottomPane(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJInternalFrameContentPane1;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 12:27:39)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.glrp.verify.IVerifyModel
 */
public IVerifyModel getModel() {
	if(m_verifyModel==null){
		m_verifyModel = new GlVerifyModel();
	}
	return m_verifyModel;
}
/**
 *  ���ܣ��õ���ѯ�������
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-23 15:59:24)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return java.lang.String
 */
private String getObjTitle() throws Exception {
    String sTitle = "";
    //nc.vo.glrp.verify.FilterCondVO voCond = null;
    //if (getModel().getQryVo() != null) {
    //if (getModel().getQryVo().getDebitCond() != null
    //&& getModel().getQryVo().getDebitCond().getVoucherDetailPk() == null) {
    //voCond = getModel().getQryVo().getDebitCond();
    //} else {
    //voCond = getModel().getQryVo().getCreditCond();
    //}
    //if (voCond == null) {
    //return sTitle;
    //}
    //if (voCond.getAssvos() != null) {
    //sTitle =
    //ShowStyleTool.getShowAss(
    //ClientInfo.getInstance().getPk_corp(),
    //voCond.getAssvos())
    //+ sTitle;
    //}
    //AccountVO voSubj = null;
    //if (voCond.getSubjPk() != null) {
    //voSubj = AccsubjDataCache.getInstance().getAccountVOByPK(voCond.getSubjPk());
    //} else if (voCond.getSubjCode() != null) {
    //voSubj =
    //AccsubjDataCache.getInstance().getAccountVOByCode(
    //voCond.getPkcorp(),
    //voCond.getSubjCode());
    //}
    //if (voSubj != null) {
    //sTitle = voSubj.getCode() + " " + voSubj.getName() + sTitle;
    //}
    //}
    try {
        if (getModel().getVerifyObject() != null) {
            VerifyObjHeaderVO voObj =
                (VerifyObjHeaderVO) getModel().getVerifyObject().getParentVO();

            AccountVO voSubj =
                AccountCache.getInstance().getAccountVOByPK(voObj.getPk_glorgbook(),voObj.getPk_accsubj());
            if (voSubj != null) {
            	sTitle = voSubj.getName() + sTitle;
            }
        }
    } catch (Exception e) {
        Logger.error(e.getMessage(), e);
    }
    return sTitle;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-9 14:12:11)
 * ���ܣ���ȡѡ�е�pane
 * ���ߣ�lwg
 * version:V30
 */
public boolean getSelectedPane() {
	if(getTopFrame().isSelected()){
		return true;
	}
	else{
		return false;
	}
}
/**
 *  ���ܣ��õ�����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-23 15:33:37)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return java.lang.String
 */
public String getTitleName() {
    String sTitle = VerifyMsg.getMSG_TITLE();
    try {
        sTitle = getObjTitle() + sTitle;
    } catch (Exception e) {
        Logger.error(e.getMessage(), e);
    }
    return sTitle;
}
/**
 * ���� TopFrame ����ֵ��
 * @return javax.swing.JInternalFrame
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIInternalFrame getTopFrame() {
	if (ivjTopFrame == null) {
		try {
			ivjTopFrame = new nc.ui.pub.beans.UIInternalFrame(VerifyMsg.getDISP_DEBIT(),true,false,true,true);
			ivjTopFrame.setName("TopFrame");
			ivjTopFrame.setMaximum(false);
			ivjTopFrame.setBounds(0, 0, 769, 207);
			ivjTopFrame.setMaximizable(true);
			ivjTopFrame.setResizable(true);
			getTopFrame().setContentPane(getJInternalFrameContentPane());
			// user code begin {1}
			getTopFrame().setTitle(VerifyMsg.getDISP_DEBIT());
			//getTopFrame().setSelected(true);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTopFrame;
}
/**
 * ���� TopPane ����ֵ��
 * @return nc.ui.glrp.verify.VerifyDataPane
 */
/* ���棺�˷������������ɡ� */
private VerifyDataPane getTopPane() {
	if (ivjTopPane == null) {
		try {
			ivjTopPane = new nc.ui.glrp.verify.VerifyDataPane();
			ivjTopPane.setName("TopPane");
			// user code begin {1}
			//ivjTopPane.setDebit(true);
			ivjTopPane.setModel(getModel());
			ivjTopPane.setEditable(true);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTopPane;
}
	private void initConnections(){
	}
/**
 *  ���ܣ��õ�view
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:11:52)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return nc.ui.glrp.verify.IVerifyView
 */
private IVerifyView getView() {
	return m_verifyView;
}

	/**
	 * ÿ�������׳��쳣ʱ������
	 *
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		nc.bs.logging.Logger.debug("--------- δ��׽�����쳣 ---------");
		nc.bs.logging.Logger.error(exception.getMessage(), exception);
		Logger.error(exception.getMessage(), exception);
		throw new GlBusinessException(exception.getMessage());
	}
/**
 * ��ʼ������
 * �������ڣ�(2003-5-10 22:37:11)
 */
public void initData() {
	getTopPane().setDebit(true);
	getTopPane().setEditable(true);
	getTopPane().initData();
	getBottomPane().setDebit(false);
	getBottomPane().setEditable(true);
	getBottomPane().initData();
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("GlVerifyUI");
		setLayout(new java.awt.BorderLayout());
		add(getNorthPane(), BorderLayout.NORTH);
		add(getDeskTopPane(), BorderLayout.CENTER);
		//getBottomFrame().setSelected(true);
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent e) {
				switchForm(); // TODO Auto-generated Event stub componentResized()
			}
		});
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	initData();
	getTopFrame().setVisible(true);
	getBottomFrame().setVisible(true);
	//switchForm();
	// user code end
	initConnections();
}

/**
 *  ���ܣ���ӡ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:09:08)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 */
public void print() {
	getTopPane().print();
	getBottomPane().print();
}
/**
 * This method gets called when a bound property is changed.
 * @param evt A PropertyChangeEvent object describing the event source
 *   	and the property that has changed.
 */
public void propertyChange(java.beans.PropertyChangeEvent evt) {
    int iKey = Integer.parseInt(evt.getPropertyName());
    switch (iKey) {
        case VerifyDetailKey.SELECTED :
            getDebitPane().propertyChange(evt);
            getCreditPane().propertyChange(evt);
            break;
        case VerifyDetailKey.DEBIT_JS_S :
        case VerifyDetailKey.DEBIT_JS_Y :
//        case VerifyDetailKey.DEBIT_JS_F :
        case VerifyDetailKey.DEBIT_JS_B :
//        case VerifyDetailKey.DEBIT_JS_J :
//        case VerifyDetailKey.DEBIT_JS_Q :
        case VerifyDetailKey.DEBIT_SUM_S :
        case VerifyDetailKey.DEBIT_SUM_Y :
//        case VerifyDetailKey.DEBIT_SUM_F :
        case VerifyDetailKey.DEBIT_SUM_B :
//        case VerifyDetailKey.DEBIT_SUM_J :
//        case VerifyDetailKey.DEBIT_SUM_Q :
        case VerifyDetailKey.ENT_DEBIT_Y :
//        case VerifyDetailKey.ENT_DEBIT_F :
        case VerifyDetailKey.ENT_DEBIT_B :
//        case VerifyDetailKey.ENT_DEBIT_J :
//        case VerifyDetailKey.ENT_DEBIT_Q :
        case VerifyDetailKey.DEBIT_SUMT_Y:
        case VerifyDetailKey.DEBIT_SUMU_Y:
//        case VerifyDetailKey.DEBIT_SUMU_F:
        case VerifyDetailKey.DEBIT_SUMU_B:
//        case VerifyDetailKey.DEBIT_SUMU_J:
//        case VerifyDetailKey.DEBIT_SUMU_Q:
            getDebitPane().propertyChange(evt);
            break;
        case VerifyDetailKey.CREDIT_JS_S :
        case VerifyDetailKey.CREDIT_JS_Y :
//        case VerifyDetailKey.CREDIT_JS_F :
        case VerifyDetailKey.CREDIT_JS_B :
//        case VerifyDetailKey.CREDIT_JS_J :
//        case VerifyDetailKey.CREDIT_JS_Q :
        case VerifyDetailKey.CREDIT_SUM_S :
        case VerifyDetailKey.CREDIT_SUM_Y :
//        case VerifyDetailKey.CREDIT_SUM_F :
        case VerifyDetailKey.CREDIT_SUM_B :
//        case VerifyDetailKey.CREDIT_SUM_J :
//        case VerifyDetailKey.CREDIT_SUM_Q :
        case VerifyDetailKey.ENT_CREDIT_Y :
//        case VerifyDetailKey.ENT_CREDIT_F :
        case VerifyDetailKey.ENT_CREDIT_B :
//        case VerifyDetailKey.ENT_CREDIT_J :
//        case VerifyDetailKey.ENT_CREDIT_Q :

        case VerifyDetailKey.CREDIT_SUMU_Y:
//        case VerifyDetailKey.CREDIT_SUMU_F:
        case VerifyDetailKey.CREDIT_SUMU_B:
//        case VerifyDetailKey.CREDIT_SUMU_J:
        case VerifyDetailKey.CREDIT_SUMU_Q:
            getCreditPane().propertyChange(evt);
            break;
        case VerifyDetailKey.STAT_DISP_CHANGE :
            getDebitPane().propertyChange(evt);
            getCreditPane().propertyChange(evt);
            break;
    }
    //try{
    //getBottomFrame().setSelected(true);
    //getTopFrame().setSelected(true);

    //}catch(Exception e){
    //}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-12 22:41:03)
 */
public void selectAll() {
	getTopPane().selectAll();
	getBottomPane().selectAll();
}
/**
 *  ���ܣ�ѡ��ƾ֤��¼���ݶ�Ӧ����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:36:42)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param voDetail nc.vo.gl.pubvoucher.DetailVO
 * @exception java.lang.Exception �쳣˵����
 */
public void selectData(nc.vo.glrp.verify.VerifyDetailVO voDetail)
    throws java.lang.Exception {
    int iDirection = voDetail.getDirect().intValue();
    if (iDirection >= 0) {
        getTopPane().setDebit(true);
        getTopPane().setEditable(false);
        getBottomPane().setDebit(false);
        getBottomPane().setEditable(true);
        getBottomFrame().setTitle(VerifyMsg.getDISP_CREDIT());
        getTopFrame().setTitle(VerifyMsg.getDISP_DEBIT());
    } else {
        getTopPane().setDebit(false);
        getTopPane().setEditable(false);
        getBottomPane().setDebit(true);
        getBottomPane().setEditable(true);
        getBottomFrame().setTitle(VerifyMsg.getDISP_DEBIT());
        getTopFrame().setTitle(VerifyMsg.getDISP_CREDIT());
    }
    getTopPane().setFirstDataSelected();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-12 22:41:15)
 */
public void selectNone() {
	getTopPane().selectNone();
	getBottomPane().selectNone();
}
/**
 *  ���ܣ����÷��״̬
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:02:46)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param iStyle int
 */
public void setStyle(int iStyle) {
	m_iStyle = iStyle;
	switchForm(m_bVertical);
}
/**
 *  ���ܣ�����model
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 12:27:39)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param newModel nc.ui.glrp.verify.IVerifyModel
 */
public void setVerifyModel(IVerifyModel newModel) {
	m_verifyModel = newModel;
	getTopPane().setModel(getModel());
	getBottomPane().setModel(getModel());
}
/**
 *  ���ܣ�����view
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:07:32)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param newView nc.ui.glrp.verify.IVerifyView
 */
public void setView(IVerifyView newView){
	m_verifyView =newView;
}
/**
 *  ���ܣ�������ʾ������Ϣ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:01:20)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param sMsg java.lang.String
 */
public void showErrorMsg(String sMsg) throws Exception {
    getView().showErrorMsg(sMsg);
}
/**
 *  ���ܣ�������ʾ��Ϣ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:01:20)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param sMsg java.lang.String
 */
public void showMsg(String sMsg) {
	getView().showMsg(sMsg);
}
/**
 * �����༭״̬
 * �������ڣ�(2003-5-11 15:24:42)
 */
public void stopEdit() {
	getTopPane().stopedit();
	getBottomPane().stopedit();
}
/**
 *  ���ܣ��л�ҳǩ��ʾ���
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:41:44)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 */
public void switchForm() {
	m_bVertical = !m_bVertical;
	switchForm(m_bVertical);
}
/**
 *  ���ܣ��л�ҳǩ��ʾ���
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:41:44)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 */
private void switchForm(boolean bFrom) {
	try {
		getTopFrame().setMaximum(false);
		getBottomFrame().setMaximum(false);
		int iWidth = getWidth();
		int iHeigh = getHeight()-30;
		if (m_iStyle == VerifyMsg.TYPE_VERIFY) {
			if (bFrom) { /*��ֱ�ֲ�*/
				getTopFrame().setBounds(0, 0, iWidth - 1, iHeigh / 2 - 1);
				getTopFrame().setSize(iWidth - 1, iHeigh / 2 - 1);
				getBottomFrame().setBounds(0, iHeigh / 2, iWidth - 1, iHeigh / 2);
				getBottomFrame().setSize(iWidth - 1, iHeigh / 2);
			} else { /*ˮƽ�ֲ�*/
				getTopFrame().setBounds(0, 0, iWidth / 2 - 1, iHeigh - 1);
				getTopFrame().setSize(iWidth / 2 - 1, iHeigh - 1);
				getBottomFrame().setBounds(iWidth / 2, 0, iWidth / 2, iHeigh - 1);
				getBottomFrame().setSize(iWidth / 2, iHeigh - 1);
			}
		} else {
			getTopFrame().setBounds(0, 0, iWidth - 1, iHeigh / 3 - 1);
			getTopFrame().setSize(iWidth - 1, iHeigh / 3 - 1);
			getBottomFrame().setBounds(0, iHeigh / 3, iWidth - 1, iHeigh - iHeigh / 3);
			getBottomFrame().setSize(iWidth - 1, iHeigh - iHeigh / 3);
		}

		getBottomFrame().setSelected(true);
		getTopFrame().setSelected(true);

	} catch (Exception e) {

	}
}
	/* ���� Javadoc��
	 * @see nc.ui.glrp.verify.IVerifyUI#showSuccessMsg(java.lang.String)
	 */
	public void showSuccessMsg(String sMsg) {
		// TODO �Զ����ɷ������
		getView().showSuccessMsg(sMsg);
	}

	private UIPanel northPane = null;
	private UILabel lblAccountingbook = null;
	private UILabel lblAccountingbookValue = null;
	private UILabel lblVerifyObj = null;
	private UILabel lblVerifyObjValue = null;

	public UIPanel getNorthPane() {
		if (northPane == null) {
			northPane = new UIPanel();
			FlowLayout northPaneLayout = new FlowLayout();
			northPaneLayout.setAlignment(FlowLayout.LEFT);
			northPane.setLayout(northPaneLayout);
			northPane.setPreferredSize(new java.awt.Dimension(830, 35));
			UIPanel p0 = new UIPanel(new FlowLayout(FlowLayout.LEFT,5,10));
			setFixSize(p0, new  Dimension(200, 80));
			northPane.add(p0);
			p0.setPreferredSize(new java.awt.Dimension(396, 31));
			p0.add(getlblAccountingbook());
			p0.add(getlblAccountingbookValue());
			UIPanel p1 = new UIPanel(new FlowLayout(FlowLayout.LEFT,20,10));
			setFixSize(p1, new  Dimension(200, 80));
			northPane.add(p1);
			p1.setPreferredSize(new java.awt.Dimension(396, 31));
			p1.add(getlblVerifyObj());
			p1.add(getlblVerifyObjValue());
		}
		return northPane;
	}

	public UILabel getlblVerifyObjValue() {
		if(lblVerifyObjValue==null){
			lblVerifyObjValue = new UILabel();
			lblVerifyObjValue.setBounds(176, 0, 150, 23);
			lblVerifyObjValue.setPreferredSize(new java.awt.Dimension(187, 18));
		}
		return lblVerifyObjValue;
	}
	private UILabel getlblVerifyObj() {
		if(lblVerifyObj==null){
			lblVerifyObj = new UILabel();
			lblVerifyObj.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("subjverify_0","02002004-0018")/*@res "��������:"*/);
			lblVerifyObj.setBounds(12, 0, 51, 18);
		}
		return lblVerifyObj;
	}
	public UILabel getlblAccountingbookValue() {
		if(lblAccountingbookValue==null){
			lblAccountingbookValue = new UILabel();
			lblAccountingbookValue.setBounds(47, 4, 135, 14);

		}
		return lblAccountingbookValue;
	}
	private UILabel getlblAccountingbook() {
		if(lblAccountingbook==null){
			lblAccountingbook = new UILabel();
//			lblAccountingbook.setPreferredSize(new java.awt.Dimension(81, 20));
			lblAccountingbook.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("subjverify_0","02002004-0019")/*@res "�����˲�:"*/);
//			lblAccountingbook.add(getlblAccountingbookValue());
		}
		return lblAccountingbook;
	}
	/**
	 * ���ø����ؼ��̶���С
	 */
	private void setFixSize(JComponent c, Dimension d) {
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
	}
}