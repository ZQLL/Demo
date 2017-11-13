package nc.ui.glrp.verify;

/**
 *  ���ܣ��º�����������������������Գ壬��ʷ��¼��ѯ�ͷ�����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:22:41)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
import nc.bs.logging.Logger;
import nc.ui.gl.common.NCHoteKeyRegistCenter;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.SeparatorButtonObject;
import nc.ui.pub.ToftPanel;
import nc.vo.glrp.pub.VerifyMsg;

public class ToftView extends nc.ui.pub.ToftPanel implements IVerifyConsumer,
		nc.ui.glpub.IUiPanel {
	private VerifyView ivjVerifyView = null;

	private ButtonObject m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000037")/*
																		 * @res
																		 * "��ѯ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000037")/* @res "��ѯ" */, 2, "��ѯ"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boVerify = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000244")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000244")/* @res "����" */, 2, "����"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boRedBlue = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000255")/*
																		 * @res
																		 * "�����Գ�"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000255")/* @res "�����Գ�" */, 2, "�����Գ�"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boAutoVerify = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000230")/*
																		 * @res
																		 * "�Զ�����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000230")/* @res "�Զ�����" */, 2, "�Զ�����"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boAutoRedBlue = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000265")/*
																		 * @res
																		 * "�Զ������Գ�"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000265")/* @res "�Զ������Գ�" */, 2, "�Զ������Գ�"); /*
																			 * -=
																			 * notranslate
																			 * =
																			 * -
																			 */

	private ButtonObject m_boHisQuery = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000266")/*
																		 * @res
																		 * "��ʷ��ѯ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000266")/* @res "��ʷ��ѯ" */, 2, "��ʷ��ѯ"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boVoucher = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000267")/*
																		 * @res
																		 * "����ƾ֤"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000267")/* @res "����ƾ֤" */, 2, "����ƾ֤"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boPrintGrp = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000044")/*
																		 * @res
																		 * "��ӡ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000044")/* @res "��ӡ" */, 2, "��ӡ"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSwitch = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000268")/*
																		 * @res
																		 * "�л�"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000268")/* @res "�л�" */, 2, "�л�"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boFetch = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000269")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000269")/* @res "����" */, 2, "����"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boUnVerify = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000270")/*
																		 * @res
																		 * "������"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000270")/* @res "������" */, 2, "������"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boReturn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000203")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000203")/* @res "����" */, 2, "����"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSum = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000271")/*
																		 * @res
																		 * "��ϸ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000272")/* @res "����/��ϸ" */, 2, "��ϸ"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boHisQry = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000037")/*
																		 * @res
																		 * "��ѯ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000273")/* @res "��ʷ��¼��ѯ" */, 2, "��ѯ02"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boHisSelectAll = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000201")/*
																		 * @res
																		 * "ȫѡ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000275")/* @res "��ʷ��¼ȫѡ" */, 2, "ȫѡ"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boHisSelectNone = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000202")/* @res "ȫ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000276")/* @res "��ʷ��¼ȫ��" */, 2, "ȫ��"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boPrintDirect = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000044")/*
																		 * @res
																		 * "��ӡ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000044")/* @res "��ӡ" */, 2, "��ӡ"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boPrintTemplet = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000044")/*
																		 * @res
																		 * "��ӡ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000044")/* @res "��ӡ" */, 2, "��ӡ"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSelectAll = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000201")/*
																		 * @res
																		 * "ȫѡ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000201")/* @res "ȫѡ" */, 2, "ȫѡ02"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSelectNone = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000202")/*
																		 * @res
																		 * "ȫ��"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000202")/* @res "ȫ��" */, 2, "ȫ��02"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boCancelFetch = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000279")/*
																		 * @res
																		 * "ȡ������"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000279")/* @res "ȡ������" */, 2, "ȡ������"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	public nc.ui.glpub.IParent p_parent;

	public nc.ui.glpub.IUiPanel m_next;

	/**
	 * ToftView ������ע�⡣
	 */
	public ToftView() {
		super();
		initialize();
	}

	/**
	 * addListener ����ע�⡣
	 */
	public void addListener(Object objListener, Object objUserdata) {
	}

	/* ����ģ������ */
	public String getModuleName() {
		return null;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return VerifyMsg.getMSG_TITLE();
	}

	/**
	 * ���� VerifyView ����ֵ��
	 * 
	 * @return nc.ui.glrp.verify.VerifyView
	 */
	/* ���棺�˷������������ɡ� */
	private VerifyView getVerifyView() {
		if (ivjVerifyView == null) {
			try {
				ivjVerifyView = new nc.ui.glrp.verify.VerifyView(this);
				ivjVerifyView.setName("VerifyView");
				// user code begin {1}
				/* �º������� */
				// ivjVerifyView.setparent(this);
				ivjVerifyView.setStyle(nc.vo.glrp.pub.VerifyMsg.TYPE_VERIFY);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjVerifyView;
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
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ToftView");
			setLayout(new java.awt.BorderLayout());
			setSize(774, 419);
			add(getVerifyView(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// m_boPrintGrp.addChildButton(m_boPrintDirect);
		// m_boPrintGrp.addChildButton(m_boPrintTemplet);
		// setButtons(new ButtonObject[] { m_boQuery, m_boHisQry, m_boVerify,
		// m_boRedBlue, m_boAutoVerify, m_boAutoRedBlue, m_boHisQuery,
		// m_boVoucher, m_boSwitch,
		// // m_boFetch,
		// m_boUnVerify, m_boSum, m_boPrintGrp, m_boHisSelectAll,
		// m_boHisSelectNone, m_boSelectAll, m_boSelectNone, m_boFetch,
		// m_boCancelFetch,m_boReturn});
		updateButton(nc.ui.glrp.verify.VerifyButtonKey.STATE_NODATA);
		// user code end
	}

	/**
	 * invoke ����ע�⡣
	 */
	public Object invoke(Object objData, Object objUserData) {
		return null;
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			ToftView aToftView;
			aToftView = new ToftView();
			frame.setContentPane(aToftView);
			frame.setSize(aToftView.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right,
					frame.getHeight() + insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.ToftPanel �� main() �з����쳣");
			nc.bs.logging.Logger.error(exception.getMessage(), exception);
		}
	}

	/**
	 * nextClosed ����ע�⡣
	 */
	public void nextClosed() {
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		try {
			showMsg("");
			if (bo == m_boAutoVerify) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_AUTOVERIFY);
			} else if (bo == m_boFetch) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_FETCH);
			} else if (bo == m_boHisQry) {
				getVerifyView().onButtonClicked(
						VerifyButtonKey.KEY_QUERYHISTORY);
			} else if (bo == m_boHisQuery) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_HISTORYQRY);
			} else if (bo == m_boPrintDirect) {
				getVerifyView().onButtonClicked(
						VerifyButtonKey.KEY_PRINTHISTORY_DIRECT);
			} else if (bo == m_boPrintTemplet) {
				getVerifyView().onButtonClicked(
						VerifyButtonKey.KEY_PRINTHISTORY_TEMPLET);
			} else if (bo == m_boPrintGrp) {
				getVerifyView().onButtonClicked(
						VerifyButtonKey.KEY_PRINTHISTORY_TEMPLET);
			} else if (bo == m_boQuery) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_QUERY);
			} else if (bo == m_boRedBlue) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_REDBLUE);
			} else if (bo == m_boReturn) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_RETURN);
			} else if (bo == m_boSum) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_SUM);
			} else if (bo == m_boSwitch) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_SWITCH);
			} else if (bo == m_boUnVerify) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_UNVERIFY);
			} else if (bo == m_boVerify) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_VERIFY);
			} else if (bo == m_boVoucher) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_VOUCHER);
			} else if (bo == m_boHisSelectAll) {
				getVerifyView().onButtonClicked(
						VerifyButtonKey.KEY_HISTORYSELECTALL);
			} else if (bo == m_boHisSelectNone) {
				getVerifyView().onButtonClicked(
						VerifyButtonKey.KEY_HISTORYSELECTNONE);
			} else if (bo == m_boSelectAll) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_SELECTALL);
			} else if (bo == m_boSelectNone) {
				getVerifyView().onButtonClicked(VerifyButtonKey.KEY_SELECTNONE);
			} else if (bo == m_boAutoRedBlue) {
				getVerifyView()
						.onButtonClicked(VerifyButtonKey.KEY_AUTOREDBLUE);
			} else if (bo == m_boCancelFetch) {
				getVerifyView()
						.onButtonClicked(VerifyButtonKey.KEY_CANCELFETCH);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			if (e.getMessage() != null && e.getMessage().length() > 0) {
				showErrorMessage(e.getMessage());
			}
			// nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this,null,e.getMessage());
		}
	}

	/**
	 * removeListener ����ע�⡣
	 */
	public void removeListener(Object objListener, Object objUserdata) {
	}

	/**
	 * ���ܣ���ʾ������Ϣ ���ߣ����� ����ʱ�䣺(2003-5-8 16:59:42) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	public void showErrorMsg(String sMsg) {
		nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
				this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
						"UPP20021201-000142")/* @res "����" */, sMsg);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-8-15 11:15:42)
	 * 
	 * @param parent
	 *            IParent
	 * @function:��һ��UiPanel����parent�н�����ʾ��
	 */
	public void showMe(nc.ui.glpub.IParent parent) {
		parent.getUiManager().removeAll();
		parent.getUiManager().add(this, this.getName());
		p_parent = parent;
		getVerifyView().setIParent(p_parent);
		setFrame(parent.getFrame());
		if (parent != null && parent instanceof ToftPanel) {
			try {
				ToftPanel new_name = (ToftPanel) parent;
				// String orgbookname = .getName();
				// //new_name.//updateStatusBarAccountMsg(orgbookname);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ���ܣ���ʾ��Ϣ ���ߣ����� ����ʱ�䣺(2003-5-10 11:33:52) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	public void showMsg(String sMsg) {
		showHintMessage(sMsg);
		// showMsg(sMsg);
		// showOkCancelMessage(sMsg);
	}

	/**
	 * ���ܣ��޸İ�ť״̬ ���ߣ����� ����ʱ�䣺(2003-5-7 16:44:26) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param iState
	 *            int
	 */
	public void updateButton(int iState) {
		switch (iState) {
		case VerifyButtonKey.STATE_NODATA:
			m_boAutoVerify.setEnabled(false);
			m_boFetch.setEnabled(false);
			m_boHisQuery.setEnabled(true);
			m_boQuery.setEnabled(true);
			m_boRedBlue.setEnabled(false);
			m_boSwitch.setEnabled(true);
			m_boVerify.setEnabled(false);
			m_boVoucher.setEnabled(false);
			m_boAutoVerify.setVisible(true);
			m_boFetch.setVisible(true);
			m_boHisQuery.setVisible(true);
			m_boQuery.setVisible(true);
			m_boRedBlue.setVisible(true);
			m_boSwitch.setVisible(true);
			m_boVerify.setVisible(true);
			m_boVoucher.setVisible(true);

			m_boUnVerify.setVisible(false);
			m_boReturn.setVisible(false);
			m_boSum.setVisible(false);
			m_boHisQry.setVisible(false);
			m_boPrintGrp.setVisible(false);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);

			m_boSelectAll.setVisible(true);
			m_boSelectAll.setEnabled(false);
			m_boSelectNone.setVisible(true);
			m_boSelectNone.setEnabled(false);
			m_boAutoRedBlue.setVisible(true);
			m_boAutoRedBlue.setEnabled(false);

			m_boCancelFetch.setEnabled(false);

			NCHoteKeyRegistCenter.buildAction(this, new ButtonObject[] {
					m_boQuery, m_boVerify, m_boRedBlue, m_boAutoVerify,
					m_boAutoRedBlue, m_boHisQuery, m_boVoucher, m_boSelectAll,
					m_boSelectNone, m_boFetch, m_boSwitch, m_boCancelFetch });
			setButtons(new ButtonObject[] { m_boQuery,
					new SeparatorButtonObject(), m_boVerify, m_boRedBlue,
					m_boAutoVerify, m_boAutoRedBlue,
					new SeparatorButtonObject(), m_boVoucher,
					new SeparatorButtonObject(), m_boHisQuery,
					new SeparatorButtonObject(), m_boSelectAll, m_boSelectNone,
					new SeparatorButtonObject(), m_boFetch, m_boCancelFetch,
					new SeparatorButtonObject(), m_boSwitch });
			break;
		case VerifyButtonKey.STATE_HASDATA:
			m_boAutoVerify.setEnabled(true);
			m_boFetch.setEnabled(true);
			m_boHisQuery.setEnabled(true);
			m_boQuery.setEnabled(true);
			m_boRedBlue.setEnabled(true);
			m_boSwitch.setEnabled(true);
			m_boVerify.setEnabled(true);
			m_boVoucher.setEnabled(true);
			m_boAutoVerify.setVisible(true);
			m_boFetch.setVisible(true);
			m_boHisQuery.setVisible(true);
			m_boQuery.setVisible(true);
			m_boRedBlue.setVisible(true);
			m_boSwitch.setVisible(true);
			m_boVerify.setVisible(true);
			m_boVoucher.setVisible(true);

			m_boUnVerify.setVisible(false);
			m_boReturn.setVisible(false);
			m_boSum.setVisible(false);
			m_boHisQry.setVisible(false);
			m_boPrintGrp.setVisible(false);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);

			m_boSelectAll.setVisible(true);
			m_boSelectAll.setEnabled(true);
			m_boSelectNone.setVisible(true);
			m_boSelectNone.setEnabled(true);
			m_boAutoRedBlue.setVisible(true);
			m_boAutoRedBlue.setEnabled(true);

			m_boCancelFetch.setEnabled(false);
			setButtons(new ButtonObject[] { m_boQuery, m_boVerify, m_boRedBlue,
					m_boAutoVerify, m_boAutoRedBlue, m_boHisQuery, m_boVoucher,
					m_boSelectAll, m_boSelectNone, m_boFetch, m_boCancelFetch,
					new SeparatorButtonObject(), m_boSwitch });
			break;
		case VerifyButtonKey.STATE_HASSINGLEDATA:
			m_boAutoVerify.setEnabled(false);
			m_boFetch.setEnabled(false);
			m_boHisQuery.setEnabled(true);
			m_boQuery.setEnabled(true);
			m_boRedBlue.setEnabled(true);
			m_boSwitch.setEnabled(true);
			m_boVerify.setEnabled(false);
			m_boVoucher.setEnabled(true);
			m_boAutoVerify.setVisible(true);
			m_boFetch.setVisible(true);
			m_boHisQuery.setVisible(true);
			m_boQuery.setVisible(true);
			m_boRedBlue.setVisible(true);
			m_boSwitch.setVisible(true);
			m_boVerify.setVisible(true);
			m_boVoucher.setVisible(true);

			m_boUnVerify.setVisible(false);
			m_boReturn.setVisible(false);
			m_boSum.setVisible(false);
			m_boHisQry.setVisible(false);
			m_boPrintGrp.setVisible(false);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);

			m_boSelectAll.setVisible(true);
			m_boSelectAll.setEnabled(true);
			m_boSelectNone.setVisible(true);
			m_boSelectNone.setEnabled(true);
			m_boAutoRedBlue.setVisible(true);
			m_boAutoRedBlue.setEnabled(true);

			m_boCancelFetch.setEnabled(false);

			setButtons(new ButtonObject[] { m_boQuery, m_boVerify, m_boRedBlue,
					m_boAutoVerify, m_boAutoRedBlue, m_boHisQuery, m_boVoucher,
					m_boSelectAll, m_boSelectNone, m_boFetch, m_boCancelFetch,
					new SeparatorButtonObject(), m_boSwitch });
			break;
		case VerifyButtonKey.STATE_HIS_NODATA:
			m_boAutoVerify.setVisible(false);
			m_boFetch.setVisible(false);
			m_boHisQuery.setVisible(false);
			m_boQuery.setVisible(false);
			m_boRedBlue.setVisible(false);
			m_boSwitch.setVisible(false);
			m_boVerify.setVisible(false);
			m_boVoucher.setVisible(false);
			m_boSum.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"20021201", "UPP20021201-000271")/* @res "��ϸ" */);
			m_boUnVerify.setEnabled(false);
			m_boReturn.setEnabled(true);
			m_boSum.setEnabled(false);
			m_boPrintGrp.setEnabled(false);
			m_boHisQry.setEnabled(true);
			m_boUnVerify.setVisible(true);
			m_boReturn.setVisible(true);
			m_boSum.setVisible(true);
			m_boHisQry.setVisible(true);
			m_boPrintGrp.setVisible(true);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);
			m_boHisSelectAll.setEnabled(false);
			m_boHisSelectNone.setEnabled(false);

			m_boSelectAll.setVisible(false);
			m_boSelectAll.setEnabled(false);
			m_boSelectNone.setVisible(false);
			m_boSelectNone.setEnabled(false);
			m_boAutoRedBlue.setVisible(false);
			m_boAutoRedBlue.setEnabled(false);
			m_boCancelFetch.setVisible(false);

			setButtons(new ButtonObject[] { m_boHisQry,
					new SeparatorButtonObject(), m_boSum,
					new SeparatorButtonObject(), m_boPrintGrp,
					new SeparatorButtonObject(), m_boReturn });
			break;
		case VerifyButtonKey.STATE_HIS_DETAIL:
			m_boAutoVerify.setVisible(false);
			m_boFetch.setVisible(false);
			m_boCancelFetch.setVisible(false);
			m_boHisQuery.setVisible(false);
			m_boQuery.setVisible(false);
			m_boRedBlue.setVisible(false);
			m_boSwitch.setVisible(false);
			m_boVerify.setVisible(false);
			m_boVoucher.setVisible(false);

			m_boUnVerify.setEnabled(true);
			m_boReturn.setEnabled(true);
			m_boPrintGrp.setEnabled(true);
			m_boSum.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"20021201", "UPP20021201-000041")/* @res "����" */);
			m_boSum.setEnabled(true);
			m_boHisQry.setEnabled(true);
			m_boUnVerify.setVisible(true);
			m_boReturn.setVisible(true);
			m_boSum.setVisible(true);
			m_boHisQry.setVisible(true);
			m_boPrintGrp.setVisible(true);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);
			m_boHisSelectAll.setEnabled(false);
			m_boHisSelectNone.setEnabled(false);

			m_boSelectAll.setVisible(false);
			m_boSelectAll.setEnabled(false);
			m_boSelectNone.setVisible(false);
			m_boSelectNone.setEnabled(false);
			m_boAutoRedBlue.setVisible(false);
			m_boAutoRedBlue.setEnabled(false);
			setButtons(new ButtonObject[] { m_boHisQry,
					new SeparatorButtonObject(), m_boUnVerify,
					new SeparatorButtonObject(), m_boSum,
					new SeparatorButtonObject(), m_boPrintGrp,
					new SeparatorButtonObject(), m_boReturn });
			break;
		case VerifyButtonKey.STATE_HIS_SUM:
			m_boAutoVerify.setVisible(false);
			m_boFetch.setVisible(false);
			m_boCancelFetch.setVisible(false);
			m_boHisQuery.setVisible(false);
			m_boQuery.setVisible(false);
			m_boRedBlue.setVisible(false);
			m_boSwitch.setVisible(false);
			m_boVerify.setVisible(false);
			m_boVoucher.setVisible(false);

			m_boUnVerify.setEnabled(true);
			m_boReturn.setEnabled(true);
			m_boPrintGrp.setEnabled(true);
			m_boSum.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"20021201", "UPP20021201-000271")/* @res "��ϸ" */);
			m_boSum.setEnabled(true);
			m_boHisQry.setEnabled(true);
			m_boUnVerify.setVisible(true);
			m_boReturn.setVisible(true);
			m_boSum.setVisible(true);
			m_boHisQry.setVisible(true);
			m_boPrintGrp.setVisible(true);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);
			m_boHisSelectAll.setEnabled(false);
			m_boHisSelectNone.setEnabled(false);

			m_boSelectAll.setVisible(false);
			m_boSelectAll.setEnabled(false);
			m_boSelectNone.setVisible(false);
			m_boSelectNone.setEnabled(false);
			m_boAutoRedBlue.setVisible(false);
			m_boAutoRedBlue.setEnabled(false);
			setButtons(new ButtonObject[] { m_boHisQry,
					new SeparatorButtonObject(), m_boUnVerify,
					new SeparatorButtonObject(), m_boSum,
					new SeparatorButtonObject(), m_boPrintGrp,
					new SeparatorButtonObject(), m_boReturn,
					new SeparatorButtonObject(), });
			break;
		// v31
		case VerifyButtonKey.STATE_FETCH:
			m_boAutoVerify.setEnabled(false);
			m_boFetch.setEnabled(false);
			m_boHisQuery.setEnabled(false);
			m_boQuery.setEnabled(true);
			m_boRedBlue.setEnabled(false);
			m_boSwitch.setEnabled(true);
			m_boVerify.setEnabled(true);
			m_boVoucher.setEnabled(false);
			m_boAutoVerify.setVisible(true);
			m_boFetch.setVisible(true);
			m_boHisQuery.setVisible(true);
			m_boQuery.setVisible(true);
			m_boRedBlue.setVisible(true);
			m_boSwitch.setVisible(true);
			m_boVerify.setVisible(true);
			m_boVoucher.setVisible(true);

			m_boUnVerify.setVisible(false);
			m_boReturn.setVisible(false);
			m_boSum.setVisible(false);
			m_boHisQry.setVisible(false);
			m_boPrintGrp.setVisible(false);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);

			m_boSelectAll.setVisible(true);
			m_boSelectAll.setEnabled(false);
			m_boSelectNone.setVisible(true);
			m_boSelectNone.setEnabled(false);
			m_boAutoRedBlue.setVisible(true);
			m_boAutoRedBlue.setEnabled(false);

			m_boCancelFetch.setEnabled(true);

			setButtons(new ButtonObject[] { m_boQuery, m_boVerify, m_boRedBlue,
					m_boAutoVerify, m_boAutoRedBlue, m_boHisQuery, m_boVoucher,
					m_boSelectAll, m_boSelectNone, m_boFetch, m_boCancelFetch,
					new SeparatorButtonObject(), m_boSwitch });
			break;
		case VerifyButtonKey.STATE_CANCELFETCH:
			m_boAutoVerify.setEnabled(true);
			m_boFetch.setEnabled(true);
			m_boHisQuery.setEnabled(true);
			m_boQuery.setEnabled(true);
			m_boRedBlue.setEnabled(true);
			m_boSwitch.setEnabled(true);
			m_boVerify.setEnabled(true);
			m_boVoucher.setEnabled(true);
			m_boAutoVerify.setVisible(true);
			m_boFetch.setVisible(true);
			m_boHisQuery.setVisible(true);
			m_boQuery.setVisible(true);
			m_boRedBlue.setVisible(true);
			m_boSwitch.setVisible(true);
			m_boVerify.setVisible(true);
			m_boVoucher.setVisible(true);

			m_boUnVerify.setVisible(false);
			m_boReturn.setVisible(false);
			m_boSum.setVisible(false);
			m_boHisQry.setVisible(false);
			m_boPrintGrp.setVisible(false);
			m_boHisSelectAll.setVisible(false);
			m_boHisSelectNone.setVisible(false);

			m_boSelectAll.setVisible(true);
			m_boSelectAll.setEnabled(true);
			m_boSelectNone.setVisible(true);
			m_boSelectNone.setEnabled(true);
			m_boAutoRedBlue.setVisible(true);
			m_boAutoRedBlue.setEnabled(true);

			m_boCancelFetch.setEnabled(false);
			setButtons(new ButtonObject[] { m_boQuery, m_boVerify, m_boRedBlue,
					m_boAutoVerify, m_boAutoRedBlue, m_boHisQuery, m_boVoucher,
					m_boSelectAll, m_boSelectNone, m_boFetch, m_boCancelFetch,
					new SeparatorButtonObject(), m_boSwitch });
			break;
		}
		updateButtons();
	}

	/**
	 * ���ܣ����ı��� ���ߣ����� ����ʱ�䣺(2003-5-23 15:36:33) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sNewTitle
	 *            java.lang.String
	 */
	public void updateTitles(String sNewTitle) {
		if (sNewTitle != null) {
			setTitleText(sNewTitle);
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.glrp.verify.IVerifyConsumer#showSuccessMsg(java.lang.String)
	 */
	public void showSuccessMsg(String sMsg) {
		// TODO �Զ����ɷ������
		nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this, "",
				sMsg);
	}
}