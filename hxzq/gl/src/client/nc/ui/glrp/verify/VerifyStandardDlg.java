package nc.ui.glrp.verify;

import java.awt.BorderLayout;

import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.pub.beans.UIPanel;
import nc.vo.ml.Language;

/**
 *  ���ܣ�������׼dlg
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 14:50:20)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
@SuppressWarnings("serial")
public class VerifyStandardDlg extends nc.ui.pub.beans.UIDialog {
	private nc.ui.pub.beans.UIButton ivjbtnCancel = null;
	private nc.ui.pub.beans.UIButton ivjbtnOk = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private VerifyStandardPane ivjStandardPane = null;
	private nc.ui.pub.beans.UIPanel ivjUIDialogContentPane = null;
	private IVerifyModel m_verifyModel;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == VerifyStandardDlg.this.getbtnOk())
				connEtoC1();
			if (e.getSource() == VerifyStandardDlg.this.getbtnCancel())
				connEtoC2();
		};
	};
/**
 * VerifyStandardDlg ������ע�⡣
 */
@SuppressWarnings("deprecation")
public VerifyStandardDlg() {
	super();
	initialize();
}
/**
 * VerifyStandardDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public VerifyStandardDlg(java.awt.Container parent) {
	super(parent);
	initialize();
}

/**
 * VerifyStandardDlg ������ע�⡣
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public VerifyStandardDlg(java.awt.Container parent, String title) {
	super(parent, title);
	initialize();
}
/**
 * VerifyStandardDlg ������ע�⡣
 * @param owner java.awt.Frame
 */
public VerifyStandardDlg(java.awt.Frame owner) {
	super(owner);
	initialize();
}
/**
 * VerifyStandardDlg ������ע�⡣
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public VerifyStandardDlg(java.awt.Frame owner, String title) {
	super(owner, title);
	initialize();
}
/**
 * Comment
 */
public void btnCancel_ActionEvents() {
	closeCancel();
	return;
}
/**
 * Comment
 */
public void btnOk_ActionEvents() {
	String sMsg = getStandardPane().check();
	//adjust--lwg--7.11
	if(sMsg!= null){
		nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000103")/*@res "��ʾ"*/,sMsg);
	}else{
		getModel().setVerifyStandardVO(getStandardPane().getStandardVO());
		closeOK();
	}
	return;
}
/**
 * connEtoC1:  (btnOk.action. --> VerifyStandardDlg.btnOk_ActionEvents()V)
 */
/* ���棺�˷������������ɡ� */
private void connEtoC1() {
	try {
		this.btnOk_ActionEvents();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (btnCancel.action. --> VerifyStandardDlg.btnCancel_ActionEvents()V)
 */
/* ���棺�˷������������ɡ� */
private void connEtoC2() {
	try {
		this.btnCancel_ActionEvents();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * ���� btnCancel ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnCancel() {
	if (ivjbtnCancel == null) {
		try {
			ivjbtnCancel = new nc.ui.pub.beans.UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000093")/*@res "ȡ��"*/);
			ivjbtnCancel.setLocation(166, 224);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjbtnCancel;
}
/**
 * ���� btnOk ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnOk() {
	if (ivjbtnOk == null) {
		try {
			ivjbtnOk = new nc.ui.pub.beans.UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000094")/*@res "ȷ��"*/);
			ivjbtnOk.setLocation(69, 224);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjbtnOk;
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 14:54:50)
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
 * ���� StandardPane ����ֵ��
 * @return nc.ui.glrp.verify.VerifyStandardPane
 */
/* ���棺�˷������������ɡ� */
private VerifyStandardPane getStandardPane() {
	if (ivjStandardPane == null) {
		try {
			ivjStandardPane = new nc.ui.glrp.verify.VerifyStandardPane();
			ivjStandardPane.setName("StandardPane");
//			ivjStandardPane.setBounds(1, 3, 302, 216);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStandardPane;
}
/**
 * ���� UIDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIPanel getUIDialogContentPane() {
	if (ivjUIDialogContentPane == null) {
		try {
			ivjUIDialogContentPane = new nc.ui.pub.beans.UIPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			getUIDialogContentPane().add(getStandardPane(), BorderLayout.CENTER);
			UIPanel buttonPanel = new UIPanel();
			buttonPanel.add(getbtnOk());
			buttonPanel.add(getbtnCancel());
			getUIDialogContentPane().add(buttonPanel, BorderLayout.SOUTH);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIDialogContentPane;
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// nc.bs.logging.Logger.debug("--------- δ��׽�����쳣 ---------");
	// nc.bs.logging.Logger.error(exception.getMessage(), exception);
}
/**
 * ��ʼ������
 * @exception java.lang.Exception �쳣˵����
 */
/* ���棺�˷������������ɡ� */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getbtnOk().addActionListener(ivjEventHandler);
	getbtnCancel().addActionListener(ivjEventHandler);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		setName("VerifyStandardDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		if(Language.SIMPLE_CHINESE_CODE.equals(GlWorkBench.getCurrLanguage().getCode())
				||Language.TRAD_CHINESE_CODE.equals(GlWorkBench.getCurrLanguage().getCode())){
			setSize(330, 330);
		}else{
			setSize(402, 330);
		}
//		setResizable(true);
		setContentPane(getUIDialogContentPane());
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000292")/*@res "������׼��"*/);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		VerifyStandardDlg aVerifyStandardDlg;
		aVerifyStandardDlg = new VerifyStandardDlg();
		aVerifyStandardDlg.setModal(true);
		aVerifyStandardDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aVerifyStandardDlg.show();
		java.awt.Insets insets = aVerifyStandardDlg.getInsets();
		aVerifyStandardDlg.setSize(aVerifyStandardDlg.getWidth() + insets.left + insets.right, aVerifyStandardDlg.getHeight() + insets.top + insets.bottom);
		aVerifyStandardDlg.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("nc.ui.pub.beans.UIDialog �� main() �з����쳣");
		nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 14:54:50)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param newModel nc.ui.glrp.verify.IVerifyModel
 */
public void setModel(IVerifyModel newModel) {
	m_verifyModel = newModel;
	getStandardPane().setVerifyModel(newModel);
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-13 15:56:12)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @return int
 */
public int showModal() {
	getStandardPane().initData();
	return super.showModal();
}

public void setStatus(int iButton){
	getStandardPane().setStatus(iButton);
}
}