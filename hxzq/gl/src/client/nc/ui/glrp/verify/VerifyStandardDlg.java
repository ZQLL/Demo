package nc.ui.glrp.verify;

import java.awt.BorderLayout;

import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.pub.beans.UIPanel;
import nc.vo.ml.Language;

/**
 *  功能：核销标准dlg
 *  作者：宋涛
 *  创建时间：(2003-5-12 14:50:20)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
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
 * VerifyStandardDlg 构造子注解。
 */
@SuppressWarnings("deprecation")
public VerifyStandardDlg() {
	super();
	initialize();
}
/**
 * VerifyStandardDlg 构造子注解。
 * @param parent java.awt.Container
 */
public VerifyStandardDlg(java.awt.Container parent) {
	super(parent);
	initialize();
}

/**
 * VerifyStandardDlg 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public VerifyStandardDlg(java.awt.Container parent, String title) {
	super(parent, title);
	initialize();
}
/**
 * VerifyStandardDlg 构造子注解。
 * @param owner java.awt.Frame
 */
public VerifyStandardDlg(java.awt.Frame owner) {
	super(owner);
	initialize();
}
/**
 * VerifyStandardDlg 构造子注解。
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
		nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000103")/*@res "提示"*/,sMsg);
	}else{
		getModel().setVerifyStandardVO(getStandardPane().getStandardVO());
		closeOK();
	}
	return;
}
/**
 * connEtoC1:  (btnOk.action. --> VerifyStandardDlg.btnOk_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
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
/* 警告：此方法将重新生成。 */
private void connEtoC2() {
	try {
		this.btnCancel_ActionEvents();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * 返回 btnCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnCancel() {
	if (ivjbtnCancel == null) {
		try {
			ivjbtnCancel = new nc.ui.pub.beans.UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000093")/*@res "取消"*/);
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
 * 返回 btnOk 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnOk() {
	if (ivjbtnOk == null) {
		try {
			ivjbtnOk = new nc.ui.pub.beans.UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000094")/*@res "确定"*/);
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
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-12 14:54:50)
 *  参数：<|>
 *  返回值：
 *  算法：
 *
 * @return nc.ui.glrp.verify.IVerifyModel
 */
public IVerifyModel getModel() {
	return m_verifyModel;
}
/**
 * 返回 StandardPane 特性值。
 * @return nc.ui.glrp.verify.VerifyStandardPane
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// nc.bs.logging.Logger.debug("--------- 未捕捉到的异常 ---------");
	// nc.bs.logging.Logger.error(exception.getMessage(), exception);
}
/**
 * 初始化连接
 * @exception java.lang.Exception 异常说明。
 */
/* 警告：此方法将重新生成。 */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getbtnOk().addActionListener(ivjEventHandler);
	getbtnCancel().addActionListener(ivjEventHandler);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
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
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000292")/*@res "核销标准："*/);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
		System.err.println("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
		nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}
}
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-12 14:54:50)
 *  参数：<|>
 *  返回值：
 *  算法：
 *
 * @param newModel nc.ui.glrp.verify.IVerifyModel
 */
public void setModel(IVerifyModel newModel) {
	m_verifyModel = newModel;
	getStandardPane().setVerifyModel(newModel);
}
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-13 15:56:12)
 *  参数：<|>
 *  返回值：
 *  算法：
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