package nc.ui.glrp.verify;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import nc.bs.logging.Logger;
import nc.ui.gl.datacache.GLParaDataCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.ref.AccountingbookRefPanel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.glcom.nodecode.GlNodeConst;
import nc.vo.ml.Language;

/**
 * �˴���������˵���� �������ڣ�(2003-5-12 22:12:31)
 * 
 * @author������
 */
public class HistoryQryDlg extends nc.ui.pub.beans.UIDialog {
	private nc.ui.pub.beans.UIButton ivjbtnCancel = null;
	private nc.ui.pub.beans.UIButton ivjbtnOk = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private HistoryQryCondPane ivjHistoryCondPane = null;
	private nc.ui.pub.beans.UIPanel ivjUIDialogContentPane = null;
	private boolean m_bFirstTime = true;
	private AccountingbookRefPanel accountingbookRefPane;

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == HistoryQryDlg.this.getbtnOk())
				connEtoC1();
			if (e.getSource() == HistoryQryDlg.this.getbtnCancel())
				connEtoC2();
		};
	};

	/**
	 * HistoryQryDlg ������ע�⡣
	 */
	public HistoryQryDlg() {
		super();
		initialize();
	}

	/**
	 * HistoryQryDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public HistoryQryDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * HistoryQryDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public HistoryQryDlg(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();
	}

	/**
	 * HistoryQryDlg ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public HistoryQryDlg(java.awt.Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * HistoryQryDlg ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public HistoryQryDlg(java.awt.Frame owner, String title) {
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
		String sMsg = getHistoryCondPane().checkCondition();
		if (sMsg != null) {
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
					this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
							"UPP20021201-000103")/* @res "��ʾ" */, sMsg);
		} else {
			closeOK();
		}
		return;
	}

	/**
	 * connEtoC1: (btnOk.action. --> HistoryQryDlg.btnOk_ActionEvents()V)
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1() {
		try {
			// user code begin {1}
			// user code end
			this.btnOk_ActionEvents();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (btnCancel.action. -->
	 * HistoryQryDlg.btnCancel_ActionEvents()V)
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC2() {
		try {
			// user code begin {1}
			// user code end
			this.btnCancel_ActionEvents();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * ���� btnCancel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			try {
				ivjbtnCancel = new nc.ui.pub.beans.UIButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"20021201", "UPP20021201-000093")/* @res "ȡ��" */);
				// ivjbtnCancel.setLocation(200, 304);
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
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			try {
				ivjbtnOk = new nc.ui.pub.beans.UIButton();
				ivjbtnOk.setName("btnOk");
				ivjbtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021201", "UPP20021201-000094")/* @res "ȷ��" */);
				// ivjbtnOk.setLocation(80, 303);
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
	 * ���� HistoryCondPane ����ֵ��
	 * 
	 * @return nc.ui.glrp.verify.HistoryQryCondPane
	 */
	/* ���棺�˷������������ɡ� */
	public HistoryQryCondPane getHistoryCondPane() {
		if (ivjHistoryCondPane == null) {
			try {
				ivjHistoryCondPane = new nc.ui.glrp.verify.HistoryQryCondPane();
				ivjHistoryCondPane.setName("HistoryCondPane");
				// ivjHistoryCondPane.setBounds(1, 1, 350, 300);
				ivjHistoryCondPane.setOpaque(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjHistoryCondPane;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-5-12 22:18:05)
	 * 
	 * @return nc.vo.glrp.verify.LogFilterCondVO
	 */
	public nc.vo.glrp.verify.LogFilterCondVO getQueryCond() {
		return getHistoryCondPane().getCondVO();
	}

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new nc.ui.pub.beans.UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setOpaque(false);
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				UIPanel panel = new UIPanel(new FlowLayout(FlowLayout.LEFT));
				panel.setOpaque(false);
				panel.add(getAccountingbookRefPane().getLabel());
				panel.add(getAccountingbookRefPane().getAccBookRefpanel());
				getUIDialogContentPane().add(panel, BorderLayout.NORTH);
				getUIDialogContentPane().add(getHistoryCondPane(),
						BorderLayout.CENTER);
				UIPanel buttonPane = new UIPanel();
				buttonPane.setOpaque(false);
				buttonPane.setLayout(new FlowLayout());
				buttonPane.add(getbtnOk());
				buttonPane.add(getbtnCancel());
				getUIDialogContentPane().add(buttonPane, BorderLayout.SOUTH);
				getUIDialogContentPane().setOpaque(false);

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
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		// nc.bs.logging.Logger.debug("--------- δ��׽�����쳣 ---------");
		// nc.bs.logging.Logger.error(exception.getMessage(), exception);
		Logger.error(exception.getMessage(), exception);
		throw new GlBusinessException(exception.getMessage());
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
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
			// user code begin {1}
			// user code end
			setName("HistoryQryDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			if (Language.SIMPLE_CHINESE_CODE.equals(GlWorkBench
					.getCurrLanguage().getCode())
					|| Language.TRAD_CHINESE_CODE.equals(GlWorkBench
							.getCurrLanguage().getCode())) {
				setSize(480, 430);
			} else {
				setSize(550, 430);
			}
			setResizable(false);
			setContentPane(getUIDialogContentPane());
			initConnections();
			String pk_accountingbook = GlWorkBench.getDefaultMainOrg();
			if (pk_accountingbook != null) {
				getAccountingbookRefPane().setPk_accountingbook(
						pk_accountingbook);
				getHistoryCondPane().setPk_accountingbook(pk_accountingbook);
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			HistoryQryDlg aHistoryQryDlg;
			aHistoryQryDlg = new HistoryQryDlg();
			aHistoryQryDlg.setModal(true);
			aHistoryQryDlg
					.addWindowListener(new java.awt.event.WindowAdapter() {
						public void windowClosing(java.awt.event.WindowEvent e) {
							System.exit(0);
						};
					});
			aHistoryQryDlg.show();
			java.awt.Insets insets = aHistoryQryDlg.getInsets();
			aHistoryQryDlg.setSize(aHistoryQryDlg.getWidth() + insets.left
					+ insets.right, aHistoryQryDlg.getHeight() + insets.top
					+ insets.bottom);
			aHistoryQryDlg.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.beans.UIDialog �� main() �з����쳣");
			nc.bs.logging.Logger.error(exception.getMessage(), exception);
		}
	}

	/**
	 * ���ܣ�����model ���ߣ����� ����ʱ�䣺(2003-5-16 9:44:09) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param newModel
	 *            nc.ui.glrp.verify.IVerifyModel
	 */
	public void setVerifyModel(IVerifyModel newModel) {
		getHistoryCondPane().setModel(newModel);
	}

	/**
	 * ���ܣ���ʾ�������һ����ʾ������Ĭ������Ϊƾ֤�������� ���ߣ����� ����ʱ�䣺(2003-5-19 19:40:16) ������<|> ����ֵ��
	 * �㷨��
	 * 
	 * @return int
	 */
	public int showModal() {
		if (m_bFirstTime) {
			m_bFirstTime = false;
			getHistoryCondPane().setDefaultValues();
		}

		return super.showModal();
	}

	public AccountingbookRefPanel getAccountingbookRefPane() {
		if (accountingbookRefPane == null) {
			accountingbookRefPane = new AccountingbookRefPanel();
			accountingbookRefPane.setFuncCode(GlNodeConst.GLNODE_VERIFY_TACKLE);
			accountingbookRefPane.setMaximumSize(accountingbookRefPane
					.getPreferredSize());
			String text = accountingbookRefPane.getLabel().getText();
			accountingbookRefPane.getLabel().setText(text + "��");/*
																 * -=notranslate=
																 * -
																 */
			// accountingbookRefPane.getLabel().setBounds(20, 5, 80, 23);
			// accountingbookRefPane.getAccBookRefpanel().setBounds(115, 5, 120,
			// 23);
			accountingbookRefPane.setOpaque(false);
			accountingbookRefPane
					.addValueChangedListener(new ValueChangedListener() {
						@Override
						public void valueChanged(ValueChangedEvent event) {
							String new_pk_accountingbook = null;
							if (event.getNewValue() != null) {
								new_pk_accountingbook = ((String[]) event
										.getNewValue())[0];
							} else {
								new_pk_accountingbook = "";
							}
							getHistoryCondPane().setPk_accountingbook(
									new_pk_accountingbook);

							// add by hurh 2011-04-10
							String pk_currtype = GLParaDataCache.getInstance()
									.PkLocalCurr(new_pk_accountingbook);
							getHistoryCondPane().setCurrType(pk_currtype);
							getHistoryCondPane().setMonBeginData();
						}
					});
		}
		accountingbookRefPane.setPreferredSize(new Dimension(256, 30));
		return accountingbookRefPane;
	}

} // @jve:decl-index=0:visual-constraint="10,10"