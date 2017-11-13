package nc.ui.glrp.verify;

import java.awt.BorderLayout;
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
 * 凭证信息过滤对话框 创建日期：(2003-5-11 18:39:54)
 * 
 * @author：宋涛
 */
public class DetailQueryDlg extends nc.ui.pub.beans.UIDialog {

	private static final long serialVersionUID = -8777318653014189189L;
	private nc.ui.pub.beans.UIButton ivjbtnCancel = null;
	private nc.ui.pub.beans.UIButton ivjbtnOk = null;
	private QryCondPane ivjconditionPane = null;
	private nc.ui.pub.beans.UIPanel ivjUIDialogContentPane = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private AccountingbookRefPanel accountingbookRefPane;

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DetailQueryDlg.this.getbtnOk())
				connEtoC1();
			if (e.getSource() == DetailQueryDlg.this.getbtnCancel())
				connEtoC2();
		};
	};

	public DetailQueryDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	public DetailQueryDlg(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();
	}

	public DetailQueryDlg(java.awt.Frame owner) {
		super(owner);
		initialize();
	}

	public DetailQueryDlg(java.awt.Frame owner, String title) {
		super(owner, title);
		initialize();
	}

	public void btnCancel_ActionEvents() {
		closeCancel();
		return;
	}

	public void btnOk_ActionEvents() {
		String sMsg = getconditionPane().checkCondition();
		if (sMsg != null) {
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
					this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
							"UPP20021201-000103")/* @res "提示" */, sMsg);
		} else {
			closeOK();
		}
	}

	private void connEtoC1() {
		try {
			this.btnOk_ActionEvents();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC2() {
		try {
			this.btnCancel_ActionEvents();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private nc.ui.pub.beans.UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			try {
				ivjbtnCancel = new nc.ui.pub.beans.UIButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"20021201", "UPP20021201-000093")/* @res "取消" */);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnCancel;
	}

	private nc.ui.pub.beans.UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			try {
				ivjbtnOk = new nc.ui.pub.beans.UIButton();
				ivjbtnOk.setName("btnOk");
				ivjbtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021201", "UPP20021201-000094")/* @res "确定" */);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnOk;
	}

	public QryCondPane getconditionPane() {
		if (ivjconditionPane == null) {
			try {
				ivjconditionPane = new nc.ui.glrp.verify.QryCondPane();
				ivjconditionPane.setName("conditionPane");
				ivjconditionPane.setOpaque(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjconditionPane;
	}

	private nc.ui.pub.beans.UIPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new nc.ui.pub.beans.UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				UIPanel panel = new UIPanel(new FlowLayout(FlowLayout.LEFT));
				panel.setOpaque(false);
				panel.add(getAccountingbookRefPane().getLabel());
				panel.add(getAccountingbookRefPane().getAccBookRefpanel());
				getUIDialogContentPane().add(panel, BorderLayout.NORTH);
				getUIDialogContentPane().add(getconditionPane(),
						BorderLayout.CENTER);
				getUIDialogContentPane().setOpaque(false);
				UIPanel buttonPane = new UIPanel(new FlowLayout());
				buttonPane.setOpaque(false);
				buttonPane.add(getbtnOk());
				buttonPane.add(getbtnCancel());
				getUIDialogContentPane().add(buttonPane, BorderLayout.SOUTH);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	private void handleException(java.lang.Throwable e) {
		Logger.error(e.getMessage(), e);
		throw new GlBusinessException(e.getMessage());
	}

	private void initConnections() throws java.lang.Exception {
		getbtnOk().addActionListener(ivjEventHandler);
		getbtnCancel().addActionListener(ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("DetailQueryDlg");
			if (Language.SIMPLE_CHINESE_CODE.equals(GlWorkBench
					.getCurrLanguage().getCode())
					|| Language.TRAD_CHINESE_CODE.equals(GlWorkBench
							.getCurrLanguage().getCode())) {
				setSize(520, 440);
			} else {
				setSize(640, 440);
			}
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setResizable(false);
			setContentPane(getUIDialogContentPane());
			initConnections();
			String pk_accountingbook = GlWorkBench.getDefaultMainOrg();
			if (pk_accountingbook != null) {
				getAccountingbookRefPane().setPk_accountingbook(
						pk_accountingbook);
				getconditionPane().setPk_accountingbook(pk_accountingbook);
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}

	}

	public AccountingbookRefPanel getAccountingbookRefPane() {
		if (accountingbookRefPane == null) {
			accountingbookRefPane = new AccountingbookRefPanel();
			accountingbookRefPane.setFuncCode(GlNodeConst.GLNODE_VERIFY_TACKLE);
			accountingbookRefPane.setMaximumSize(accountingbookRefPane
					.getPreferredSize());
			String text = accountingbookRefPane.getLabel().getText();
			accountingbookRefPane.getLabel().setText(text + "：");/*
																 * -=notranslate=
																 * -
																 */
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
							getconditionPane().setPk_accountingbook(
									new_pk_accountingbook);
							// add by hurh 2011-04-10
							String pk_currtype = GLParaDataCache.getInstance()
									.PkLocalCurr(new_pk_accountingbook);
							getconditionPane().setCurrType(pk_currtype);

						}
					});
		}
		return accountingbookRefPane;
	}
}