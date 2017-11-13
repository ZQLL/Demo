package nc.ui.glrp.verify;

/**
 *  功能：事后核销，包括核销处理，红蓝对冲，历史纪录查询和反核销
 *  作者：宋涛
 *  创建时间：(2003-5-12 15:22:41)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
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
																		 * "查询"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000037")/* @res "查询" */, 2, "查询"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boVerify = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000244")/*
																		 * @res
																		 * "核销"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000244")/* @res "核销" */, 2, "核销"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boRedBlue = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000255")/*
																		 * @res
																		 * "红蓝对冲"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000255")/* @res "红蓝对冲" */, 2, "红蓝对冲"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boAutoVerify = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000230")/*
																		 * @res
																		 * "自动核销"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000230")/* @res "自动核销" */, 2, "自动核销"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boAutoRedBlue = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000265")/*
																		 * @res
																		 * "自动红蓝对冲"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000265")/* @res "自动红蓝对冲" */, 2, "自动红蓝对冲"); /*
																			 * -=
																			 * notranslate
																			 * =
																			 * -
																			 */

	private ButtonObject m_boHisQuery = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000266")/*
																		 * @res
																		 * "历史查询"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000266")/* @res "历史查询" */, 2, "历史查询"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boVoucher = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000267")/*
																		 * @res
																		 * "联查凭证"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000267")/* @res "联查凭证" */, 2, "联查凭证"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boPrintGrp = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000044")/*
																		 * @res
																		 * "打印"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000044")/* @res "打印" */, 2, "打印"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSwitch = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000268")/*
																		 * @res
																		 * "切换"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000268")/* @res "切换" */, 2, "切换"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boFetch = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000269")/*
																		 * @res
																		 * "对照"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000269")/* @res "对照" */, 2, "对照"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boUnVerify = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000270")/*
																		 * @res
																		 * "反核销"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000270")/* @res "反核销" */, 2, "反核销"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boReturn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000203")/*
																		 * @res
																		 * "返回"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000203")/* @res "返回" */, 2, "返回"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSum = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000271")/*
																		 * @res
																		 * "详细"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000272")/* @res "汇总/详细" */, 2, "详细"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boHisQry = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000037")/*
																		 * @res
																		 * "查询"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000273")/* @res "历史纪录查询" */, 2, "查询02"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boHisSelectAll = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000201")/*
																		 * @res
																		 * "全选"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000275")/* @res "历史纪录全选" */, 2, "全选"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boHisSelectNone = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000202")/* @res "全消" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000276")/* @res "历史纪录全消" */, 2, "全消"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	private ButtonObject m_boPrintDirect = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000044")/*
																		 * @res
																		 * "打印"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000044")/* @res "打印" */, 2, "打印"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boPrintTemplet = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000044")/*
																		 * @res
																		 * "打印"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000044")/* @res "打印" */, 2, "打印"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSelectAll = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000201")/*
																		 * @res
																		 * "全选"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000201")/* @res "全选" */, 2, "全选02"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boSelectNone = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000202")/*
																		 * @res
																		 * "全消"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000202")/* @res "全消" */, 2, "全消02"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_boCancelFetch = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021201", "UPP20021201-000279")/*
																		 * @res
																		 * "取消对照"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
					"UPP20021201-000279")/* @res "取消对照" */, 2, "取消对照"); /*
																		 * -=
																		 * notranslate
																		 * =-
																		 */

	public nc.ui.glpub.IParent p_parent;

	public nc.ui.glpub.IUiPanel m_next;

	/**
	 * ToftView 构造子注解。
	 */
	public ToftView() {
		super();
		initialize();
	}

	/**
	 * addListener 方法注解。
	 */
	public void addListener(Object objListener, Object objUserdata) {
	}

	/* 返回模块名称 */
	public String getModuleName() {
		return null;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return VerifyMsg.getMSG_TITLE();
	}

	/**
	 * 返回 VerifyView 特性值。
	 * 
	 * @return nc.ui.glrp.verify.VerifyView
	 */
	/* 警告：此方法将重新生成。 */
	private VerifyView getVerifyView() {
		if (ivjVerifyView == null) {
			try {
				ivjVerifyView = new nc.ui.glrp.verify.VerifyView(this);
				ivjVerifyView.setName("VerifyView");
				// user code begin {1}
				/* 事后核销风格 */
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// nc.bs.logging.Logger.debug("--------- 未捕捉到的异常 ---------");
		// nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
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
	 * invoke 方法注解。
	 */
	public Object invoke(Object objData, Object objUserData) {
		return null;
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
			System.err.println("nc.ui.pub.ToftPanel 的 main() 中发生异常");
			nc.bs.logging.Logger.error(exception.getMessage(), exception);
		}
	}

	/**
	 * nextClosed 方法注解。
	 */
	public void nextClosed() {
	}

	/**
	 * 子类实现该方法，响应按钮事件。
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
	 * removeListener 方法注解。
	 */
	public void removeListener(Object objListener, Object objUserdata) {
	}

	/**
	 * 功能：显示错误信息 作者：宋涛 创建时间：(2003-5-8 16:59:42) 参数：<|> 返回值： 算法：
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	public void showErrorMsg(String sMsg) {
		nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
				this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201",
						"UPP20021201-000142")/* @res "错误" */, sMsg);
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-15 11:15:42)
	 * 
	 * @param parent
	 *            IParent
	 * @function:将一个UiPanel在其parent中进行显示。
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
	 * 功能：显示信息 作者：宋涛 创建时间：(2003-5-10 11:33:52) 参数：<|> 返回值： 算法：
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
	 * 功能：修改按钮状态 作者：宋涛 创建时间：(2003-5-7 16:44:26) 参数：<|> 返回值： 算法：
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
					"20021201", "UPP20021201-000271")/* @res "详细" */);
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
					"20021201", "UPP20021201-000041")/* @res "汇总" */);
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
					"20021201", "UPP20021201-000271")/* @res "详细" */);
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
	 * 功能：更改标题 作者：宋涛 创建时间：(2003-5-23 15:36:33) 参数：<|> 返回值： 算法：
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
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.glrp.verify.IVerifyConsumer#showSuccessMsg(java.lang.String)
	 */
	public void showSuccessMsg(String sMsg) {
		// TODO 自动生成方法存根
		nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this, "",
				sMsg);
	}
}