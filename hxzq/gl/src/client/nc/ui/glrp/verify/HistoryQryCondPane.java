package nc.ui.glrp.verify;

import java.awt.Dimension;
import java.awt.FlowLayout;

import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.logging.Logger;
import nc.gl.glconst.systemtype.SystemtypeConst;
import nc.pubitf.accperiod.AccountCalendar;
import nc.ui.gl.datacache.AccountCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.view.LayoutPanel;
import nc.ui.glverifycom.AssChooser;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UITextField;
import nc.ui.subjvery.ClientInfo;
import nc.vo.gateway60.accountbook.AccountBookUtil;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.LogFilterCondVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能：核销过滤条件pane 作者：宋涛 创建时间：(2003-5-7 15:04:36) 使用说明：以及别人可能感兴趣的介绍 注意：现存Bug
 */
public class HistoryQryCondPane extends nc.ui.pub.beans.UIPanel {
	private nc.ui.pub.beans.UIPanel ivjassPane = null;
	private nc.ui.pub.beans.UIRefPane ivjbeginDate = null;
	private nc.ui.pub.beans.UILabel ivjDateType = null;
	private nc.ui.pub.beans.UIRefPane ivjEndDate = null;
	private nc.ui.pub.beans.UILabel ivjlbCurr = null;
	private nc.ui.pub.beans.UILabel ivjlbDate = null;
	private nc.ui.pub.beans.UILabel ivjlbSubj = null;
	private nc.ui.pub.beans.UILabel ivjlbVerifyNo = null;
	private nc.ui.pub.beans.UILabel ivjLine = null;
	private javax.swing.ButtonGroup ivjradiopane = null;
	private nc.ui.pub.beans.UIRadioButton ivjrdBusinessDate = null;
	private nc.ui.pub.beans.UIRadioButton ivjrdVoucherDate = null;
	private nc.ui.pub.beans.UIRefPane ivjrefCurr = null;
	private nc.ui.pub.beans.UIRefPane ivjRefSubj = null;
	private nc.ui.pub.beans.UITextField ivjtfVerifyNo = null;
	private nc.ui.glverifycom.AssChooser m_assChooser; // @jve:decl-index=0:visual-constraint="9,327"
	private javax.swing.ButtonGroup ivjgrpType = null;
	private nc.ui.pub.beans.UILabel ivjlbType = null;
	private nc.ui.pub.beans.UIRadioButton ivjrdTypeRedBlue = null;
	private nc.ui.pub.beans.UIRadioButton ivjrdTypeVerify = null;
	private nc.ui.pub.beans.UIRadioButton ivjrdVerifyDate = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private IVerifyModel m_verifyModel;
	private UILabel UIMoneyRangeLabel = null;
	private UITextField UITextBegMoney = null;
	private UILabel UILineLabel = null;
	private UITextField UITextEndMoney = null;
	private UILabel UIDigestLabel = null;
	private UITextField UITextDigest = null;

	private String pk_accountingbook;

	class IvjEventHandler implements nc.ui.pub.beans.ValueChangedListener {
		public void valueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
			if (event.getSource() == HistoryQryCondPane.this.getRefSubj())
				connEtoC1(event);
		};
	};

	/**
	 * QryCondPane 构造子注解。
	 */
	public HistoryQryCondPane() {
		super();
		initialize();
	}

	/**
	 * QryCondPane 构造子注解。
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 */
	public HistoryQryCondPane(java.awt.LayoutManager p0) {
		super(p0);
	}

	/**
	 * QryCondPane 构造子注解。
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 * @param p1
	 *            boolean
	 */
	public HistoryQryCondPane(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
	}

	/**
	 * QryCondPane 构造子注解。
	 * 
	 * @param p0
	 *            boolean
	 */
	public HistoryQryCondPane(boolean p0) {
		super(p0);
	}

	/**
	 * 检查录入条件的合法性，null合法，否则返回错误原因 创建日期：(2003-5-11 20:30:59)
	 * 
	 * @return java.lang.String
	 */
	public String checkCondition() {
		stopEdit();
		if (getrefCurr().getRefPK() == null) {
			return VerifyMsg.getMSG_NOCURR_ERROR();
		}
		if (getRefSubj().getRefPK() == null) {
			return VerifyMsg.getMSG_NOACSUBJ_ERROR();
		}
		if (getbeginDate().getDateBegin() != null && getEndDate().getDateEnd() != null) {
			if (getbeginDate().getDateBegin().after(getEndDate().getDateEnd())) {
				return VerifyMsg.getMSG_ENDDATE_EARLY_THAN_STARTDATE_ERROR();
			}
		}

		/* 检查辅助核算 */
		// try {
		// if (!getModel()
		// .isAssLegal(getChooser().getAssResults(), getModel().getVerifyObject())) {
		// return "辅助核算不符合核销对象设置！";
		// }
		// } catch (Exception e) {
		// Logger.error(e.getMessage(), e);
		// }
		return null;
	}

	/**
	 * connEtoC1: (RefSubj.valueChanged.valueChanged(nc.ui.pub.beans.ValueChangedEvent) -->
	 * HistoryQryCondPane.refSubj_ValueChanged(Lnc.ui.pub.beans.ValueChangedEvent;)V)
	 * 
	 * @param arg1
	 *            nc.ui.pub.beans.ValueChangedEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(nc.ui.pub.beans.ValueChangedEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.refSubj_ValueChanged(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 返回 assPane 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getassPane() {
		if (ivjassPane == null) {
			try {
				ivjassPane = new nc.ui.pub.beans.UIPanel();
				ivjassPane.setName("assPane");
//				ivjassPane.setPreferredSize(new Dimension(530, 100));
				ivjassPane.setLayout(new java.awt.BorderLayout());
//				ivjassPane.setBounds(16, 37, 319, 20);
				// user code begin {1}
				ivjassPane.add(getChooser(), "Center");
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjassPane;
	}

	/**
	 * 返回 beginDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getbeginDate() {
		if (ivjbeginDate == null) {
			try {
				ivjbeginDate = new nc.ui.pub.beans.UIRefPane();
				ivjbeginDate.setName("beginDate");
//				ivjbeginDate.setBounds(74, 145, 113, 22);
				ivjbeginDate.setRefNodeName("日历");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbeginDate;
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-7 15:58:15) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.ui.glverifycom.AssChooser
	 */
	public nc.ui.glverifycom.AssChooser getChooser() {
		if (m_assChooser == null) {
			try {
				m_assChooser = new AssChooser(false);
				m_assChooser.setName("ccc");
				m_assChooser.setPreferredSize(new Dimension(450, 100));
				// m_assChooer.setLocation(38, 24);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return m_assChooser;
	}

	/**
	 * 功能：得到过滤条件 作者：宋涛 创建时间：(2003-5-7 16:20:42) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.FilterCondVO
	 * @param voDetail
	 *            nc.vo.gl.pubvoucher.DetailVO
	 */
	public nc.vo.glrp.verify.LogFilterCondVO getCondVO() {
		LogFilterCondVO voCond = new LogFilterCondVO();
		nc.vo.bd.account.AccountVO voSubj = AccountCache.getInstance().getAccountVOByPK(getPk_glorgbook(),
				getRefSubj().getRefPK());
		if (voSubj.getEndflag().booleanValue()) {
			voCond.setSubjPk(voSubj.getPk_accasoa());
			voCond.setSubjCode(null);
		} else {
			voCond.setSubjCode(getRefSubj().getRefCode());
			voCond.setSubjPk(null);
		}

		// voCond.setSubjCode(getRefSubj().getRefCode());
		// voCond.setPkcorp(getPk_corp());
		voCond.setPk_glorgbook(getPk_glorgbook());
		voCond.setPkSob(getPk_Sob());
		getChooser().stopEditing();
		nc.vo.glcom.ass.AssVO[] assvos = getChooser().getAssResults();
		if (assvos != null && assvos.length > 0) {
			voCond.setAssvos(getChooser().getAssResults());
		} else {
			voCond.setAssvos(null);
		}
		if (getbeginDate().getDateBegin() != null) {
			voCond.setBeginDate(getbeginDate().getDateBegin().toString());
		} else {
			voCond.setBeginDate(null);
		}
		if (getEndDate().getDateEnd() != null) {
			voCond.setEndDate(getEndDate().getDateEnd().toString());
		} else {
			voCond.setEndDate(null);
		}
		if (getrdVoucherDate().isSelected()) {
			voCond.setDateType("voucherDate");
		} else if (getrdVerifyDate().isSelected()) {
			voCond.setDateType("opdate");
		} else {
			voCond.setDateType("Businessdate");
		}
		/* 币种 */
		voCond.setCurPk(getrefCurr().getRefPK());
		/* 核销号 */
		if (gettfVerifyNo().getText() != null && gettfVerifyNo().getText().length() > 0) {
			voCond.setVerifyNo(gettfVerifyNo().getText());
		} else {
			voCond.setVerifyNo(null);
		}
		voCond.setRedBlue(getrdTypeRedBlue().isSelected());

		/* 金额范围 */
		if (getUITextBegMoney().getText() != null && getUITextBegMoney().getText().trim().length() > 0) {
			voCond.setM_MnyBeginHis(new UFDouble(getUITextBegMoney().getText()));
		} else {
			voCond.setM_MnyBeginHis(null);
		}
		if (getUITextEndMoney().getText() != null && getUITextEndMoney().getText().trim().length() > 0) {
			voCond.setM_MnyEndHis(new UFDouble(getUITextEndMoney().getText()));
		} else {
			voCond.setM_MnyEndHis(null);
		}
		/* 摘要 */
		if (getUITextDigest().getText() != null && getUITextDigest().getText().trim().length() > 0) {
			voCond.setM_DigestHis(getUITextDigest().getText().trim());
		} else {
			voCond.setM_DigestHis(null);
		}
		return voCond;
	}

	/**
	 * 返回 DateType 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getDateType() {
		if (ivjDateType == null) {
			try {
				ivjDateType = new nc.ui.pub.beans.UILabel();
				ivjDateType.setName("DateType");
				ivjDateType
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000250")/* @res "分析日期：" */);
//				ivjDateType.setBounds(16, 171, 61, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjDateType;
	}

	/**
	 * 返回 EndDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getEndDate() {
		if (ivjEndDate == null) {
			try {
				ivjEndDate = new nc.ui.pub.beans.UIRefPane();
				ivjEndDate.setName("EndDate");
//				ivjEndDate.setBounds(221, 143, 114, 22);
				ivjEndDate.setRefNodeName("日历");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjEndDate;
	}

	/**
	 * 返回 grpType 特性值。
	 * 
	 * @return javax.swing.ButtonGroup
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.ButtonGroup getgrpType() {
		if (ivjgrpType == null) {
			try {
				ivjgrpType = new javax.swing.ButtonGroup();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjgrpType;
	}

	/**
	 * 返回 lbCurr 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbCurr() {
		if (ivjlbCurr == null) {
			try {
				ivjlbCurr = new nc.ui.pub.beans.UILabel();
				ivjlbCurr.setName("lbCurr");
				ivjlbCurr.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000198")/* @res "币种：" */);
//				ivjlbCurr.setBounds(16, 218, 67, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbCurr;
	}

	/**
	 * 返回 lbDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbDate() {
		if (ivjlbDate == null) {
			try {
				ivjlbDate = new nc.ui.pub.beans.UILabel();
				ivjlbDate.setName("lbDate");
				ivjlbDate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000251")/* @res "日期范围：" */);
//				ivjlbDate.setBounds(16, 145, 67, 22);
//				ivjlbDate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbDate;
	}

	/**
	 * 返回 lbSubj 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbSubj() {
		if (ivjlbSubj == null) {
			try {
				ivjlbSubj = new nc.ui.pub.beans.UILabel();
				ivjlbSubj.setName("lbSubj");
				ivjlbSubj.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000252")/*
																													 * @res
																													 * "核销对象科以及下级科目"
																													 */);
				ivjlbSubj
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000163")/* @res "核销对象科目：" */);
//				ivjlbSubj.setBounds(16, 10, 119, 22);
				ivjlbSubj.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbSubj;
	}

	/**
	 * 返回 lbType 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbType() {
		if (ivjlbType == null) {
			try {
				ivjlbType = new nc.ui.pub.beans.UILabel();
				ivjlbType.setName("lbType");
				ivjlbType
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000253")/* @res "处理方式：" */);
//				ivjlbType.setBounds(16, 196, 71, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbType;
	}

	/**
	 * 返回 lbVerifyNo 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbVerifyNo() {
		if (ivjlbVerifyNo == null) {
			try {
				ivjlbVerifyNo = new nc.ui.pub.beans.UILabel();
				ivjlbVerifyNo.setName("lbVerifyNo");
				ivjlbVerifyNo
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000254")/* @res "核销号：" */);
//				ivjlbVerifyNo.setBounds(199, 218, 52, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbVerifyNo;
	}

	/**
	 * 返回 Line 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLine() {
		if (ivjLine == null) {
			try {
				ivjLine = new nc.ui.pub.beans.UILabel();
				ivjLine.setName("Line");
				ivjLine.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000249")/* @res "——" */);
//				ivjLine.setBounds(190, 145, 32, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLine;
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-16 9:40:00) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.ui.glrp.verify.IVerifyModel
	 */
	public IVerifyModel getModel() {
		if (m_verifyModel == null) {
			m_verifyModel = new nc.ui.glrp.verify.GlVerifyModel();
		}
		return m_verifyModel;
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-12 10:09:41) 参数：<|> 返回值： 算法：
	 * 
	 * @return java.lang.String
	 */
	private String getPk_corp() {
		// return nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp();
		return AccountBookUtil.getPk_org(getPk_glorgbook());
	}

	// private String getPk_glorgbook(){
	// return GlWorkBench.getDefaultMainOrg(); //null;
	// //((SetOfBookVO)nc.ui.pub.ClientEnvironment.getInstance().getValue(ClientEnvironment.GLORGBOOKPK)).getPk_accountingbook();
	// }
	private String getPk_glorgbook() {
		if (pk_accountingbook == null) {
			pk_accountingbook = GlWorkBench.getDefaultMainOrg();
		}
		return pk_accountingbook; // ((SetOfBookVO)
									// nc.ui.pub.ClientEnvironment.getInstance().getValue(ClientEnvironment.GLORGBOOKPK)).getPk_accountingbook();
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-12 10:36:49) 参数：<|> 返回值： 算法：
	 * 
	 * @return java.lang.String
	 */
	private String getPk_Sob() {
		return nc.ui.subjvery.ClientInfo.getInstance().getPk_Sob();
	}

	/**
	 * 返回 radiopane 特性值。
	 * 
	 * @return javax.swing.ButtonGroup
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.ButtonGroup getradiopane() {
		if (ivjradiopane == null) {
			try {
				ivjradiopane = new javax.swing.ButtonGroup();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjradiopane;
	}

	/**
	 * 返回 rdBusinessDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRadioButton getrdBusinessDate() {
		if (ivjrdBusinessDate == null) {
			try {
				ivjrdBusinessDate = new nc.ui.pub.beans.UIRadioButton();
				ivjrdBusinessDate.setName("rdBusinessDate");
				ivjrdBusinessDate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000090")/*
																														 * @res
																														 * "业务日期"
																														 */);
//				ivjrdBusinessDate.setBounds(256, 171, 79, 22);
				ivjrdBusinessDate.setFocusPainted(false);
				ivjrdBusinessDate.setOpaque(false);
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrdBusinessDate;
	}

	/**
	 * 返回 rdTypeRedBlue 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRadioButton getrdTypeRedBlue() {
		if (ivjrdTypeRedBlue == null) {
			try {
				ivjrdTypeRedBlue = new nc.ui.pub.beans.UIRadioButton();
				ivjrdTypeRedBlue.setName("rdTypeRedBlue");
				ivjrdTypeRedBlue
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000255")/* @res "红蓝对冲" */);
//				ivjrdTypeRedBlue.setBounds(172, 196, 80, 22);
				ivjrdTypeRedBlue.setFocusPainted(false);
				ivjrdTypeRedBlue.setOpaque(false);
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrdTypeRedBlue;
	}

	/**
	 * 返回 rdTypeVerify 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRadioButton getrdTypeVerify() {
		if (ivjrdTypeVerify == null) {
			try {
				ivjrdTypeVerify = new nc.ui.pub.beans.UIRadioButton();
				ivjrdTypeVerify.setName("rdTypeVerify");
				ivjrdTypeVerify
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000244")/* @res "核销" */);
//				ivjrdTypeVerify.setBounds(83, 196, 66, 22);
				ivjrdTypeVerify.setFocusPainted(false);
				ivjrdTypeVerify.setOpaque(false);
				// user code begin {1}
				ivjrdTypeVerify.setSelected(true);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrdTypeVerify;
	}

	/**
	 * 返回 rdVerifyDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRadioButton getrdVerifyDate() {
		if (ivjrdVerifyDate == null) {
			try {
				ivjrdVerifyDate = new nc.ui.pub.beans.UIRadioButton();
				ivjrdVerifyDate.setName("rdVerifyDate");
				ivjrdVerifyDate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000256")/* @res "核销日期" */);
//				ivjrdVerifyDate.setBounds(83, 171, 79, 22);
				ivjrdVerifyDate.setFocusPainted(false);
				ivjrdVerifyDate.setOpaque(false);
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrdVerifyDate;
	}

	/**
	 * 返回 rdVoucherDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRadioButton getrdVoucherDate() {
		if (ivjrdVoucherDate == null) {
			try {
				ivjrdVoucherDate = new nc.ui.pub.beans.UIRadioButton();
				ivjrdVoucherDate.setName("rdVoucherDate");
				ivjrdVoucherDate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000015")/* @res "凭证日期" */);
//				ivjrdVoucherDate.setBounds(172, 170, 79, 22);
				ivjrdVoucherDate.setFocusPainted(false);
				ivjrdVoucherDate.setOpaque(false);
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrdVoucherDate;
	}

	/**
	 * 返回 refCurr 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getrefCurr() {
		if (ivjrefCurr == null) {
			try {
				ivjrefCurr = new nc.ui.pub.beans.UIRefPane();
				ivjrefCurr.setName("refCurr");
//				ivjrefCurr.setBounds(77, 218, 100, 22);
				ivjrefCurr.setRefNodeName("币种档案");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrefCurr;
	}

	/**
	 * 返回 RefSubj 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getRefSubj() {
		if (ivjRefSubj == null) {
			try {
				String[] versionInfo = new String[2];
				versionInfo[0] = getPk_glorgbook();
				versionInfo[1] = GlWorkBench.getBusiDate().toStdString();

				ivjRefSubj = new nc.ui.pub.beans.UIRefPane();
				ivjRefSubj.setName("RefSubj");
//				ivjRefSubj.setBounds(139, 9, 180, 22);
				ivjRefSubj.setRefNodeName("会计科目");
				ivjRefSubj.setDataPowerOperation_code(SystemtypeConst.FI);
				ivjRefSubj.getRefModel().setPara(versionInfo);
				// user code begin {1}
				// ivjRefSubj.getRef().setRefType(0/** 表格参照*/);
				// ivjRefSubj.getRefModel().setPk_GlOrgBook(OrgnizeTypeVO.ZHUZHANG_TYPE,getPk_glorgbook());
//				String sWhere = ivjRefSubj.getRefModel().getWherePart();
//				if (sWhere != null && sWhere.length() > 0) {
//					sWhere += " and ";
//				} else if (sWhere != null) {
//					sWhere = "";
//				}
				
//				String[] pks = nc.ui.gl.ageanalysis.NormalConditionPanel.getAccountPks(new String[]{getPk_glorgbook()});
//				ivjRefSubj.getRefModel().setFilterPks(pks);
//				StringBuffer wherePart = new StringBuffer();
//				wherePart.append(" exists (select subj1.pk_accasoa from( ");
//				wherePart
//						.append(" 		select s1.pk_accasoa,s2.code from bd_accasoa s1,bd_account s2 where s1.pk_account = s2.pk_account ");
//				wherePart.append(" 		and s1.pk_accchart in( ");
//				wherePart.append(" 			select pk_curraccchart from org_accountingbook where pk_accountingbook='"
//						+ getPk_glorgbook() + "' ");
//				wherePart.append(" 		) ");
//				wherePart.append(" 		and s2.enablestate = 2 ");
//				wherePart.append(" ) subj1 ");
//				wherePart.append(" INNER JOIN gl_verifyObj ON gl_verifyObj.Pk_accasoa = subj1.Pk_accasoa ");
//				wherePart.append(" where gl_verifyObj.pk_accountingbook='" + getPk_glorgbook() + "' ");
//				wherePart.append(" and len(bd_account.code) >= len(subj1.code) ");
//				wherePart.append(" and substring(bd_account.code,1,len(subj1.code)) = subj1.code ");
//				wherePart.append(" and gl_verifyObj.userFlag='Y' ");
//				wherePart.append(" ) ");
//
//				sWhere = sWhere + wherePart.toString();
//
//				ivjRefSubj.setWhereString(sWhere); 
				// 显示封存科目
				// ivjRefSubj.getRefModel().setSealedDataShow(true);
				ivjRefSubj.getRefModel().setRefCardInfoComponentImplClass(null);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjRefSubj;
	}

	/**
	 * 返回 tfVerifyNo 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextField gettfVerifyNo() {
		if (ivjtfVerifyNo == null) {
			try {
				ivjtfVerifyNo = new nc.ui.pub.beans.UITextField();
				ivjtfVerifyNo.setName("tfVerifyNo");
//				ivjtfVerifyNo.setBounds(251, 218, 84, 20);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtfVerifyNo;
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
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		getradiopane().add(getrdVerifyDate());
		getradiopane().add(getrdBusinessDate());
		getradiopane().add(getrdVoucherDate());
		getgrpType().add(getrdTypeVerify());
		getgrpType().add(getrdTypeRedBlue());

		getrdVoucherDate().getModel().setGroup(getradiopane());
		getrdVerifyDate().getModel().setGroup(getradiopane());
		getrdBusinessDate().getModel().setGroup(getradiopane());
		getrdTypeRedBlue().getModel().setGroup(getgrpType());
		getrdTypeVerify().getModel().setGroup(getgrpType());

		// user code end
		getRefSubj().addValueChangedListener(ivjEventHandler);
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-7 16:03:56) 参数：<|> 返回值： 算法：
	 * 
	 */
	private void initData() {
		getrdVerifyDate().setSelected(true);
		getrdVoucherDate().setSelected(false);
		getrdBusinessDate().setSelected(false);
		getrdTypeVerify().setSelected(true);
		getrdTypeRedBlue().setSelected(false);
		////注意不可以在此设置结束日期值
		//getEndDate().setValueObj(GlWorkBench.getBusiDate());

		getrefCurr().setPK(ClientInfo.getInstance().getLocalPk());
	}
	
	//重置业务日期
	public void resatBusDate(){
		//setMonBeginData();
		getEndDate().setValueObj(GlWorkBench.getBusiDate());
	}
	
	public void setMonBeginData() {
		String Pk_accountperiod = AccountBookUtil.getAccountingBookVOByPrimaryKey(getPk_glorgbook()).getPk_accountperiod();
		AccountCalendar calendar = null;
		try {
			calendar = AccountCalendar.getInstanceByAccperiodMonth(Pk_accountperiod);
		} catch (InvalidAccperiodExcetion e) {
			Logger.error(e.getMessage(), e);
		}
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		getbeginDate().setValueObj(beginDate);
	}
	
	

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			setName("QryCondPane");
			UIDigestLabel = new UILabel();
			UIDigestLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000258")/* @res "摘要：" */);
			UILineLabel = new UILabel();
			UILineLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000249")/* @res "——" */);
			UIMoneyRangeLabel = new UILabel();
			UIMoneyRangeLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000359")/* @res* "金额范围："*/);
			setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
			
			LayoutPanel pane = new LayoutPanel(6, 4, LayoutPanel.LEFT);
			pane.setOpaque(false);
			pane.add(1, 1, getlbSubj());
			pane.add(1, 2, getRefSubj());
			pane.add(2, 1, getassPane(), 1, 4);
			pane.add(3, 1, getlbDate());
			pane.add(3, 2, getbeginDate());
			pane.add(3, 3, LayoutPanel.CENTER,getLine());
			pane.add(3, 4, getEndDate());
			pane.add(4, 1, getlbCurr());
			pane.add(4, 2, getrefCurr());
			pane.add(4, 3, getlbVerifyNo());
			pane.add(4, 4, gettfVerifyNo());
			pane.add(5, 1, UIMoneyRangeLabel);
			pane.add(5, 2, getUITextBegMoney());
			pane.add(5, 3, LayoutPanel.CENTER,UILineLabel);
			pane.add(5, 4, getUITextEndMoney());
			pane.add(6, 1, UIDigestLabel);
			pane.add(6, 2, getUITextDigest());
			
			LayoutPanel panel3 = new LayoutPanel(2, 4, LayoutPanel.LEFT);
			panel3.setOpaque(false);
			panel3.add(1, 1, getDateType());
			panel3.add(1, 2, getrdVoucherDate());
			panel3.add(1, 3, getrdBusinessDate());
			panel3.add(1, 4, getrdVerifyDate());
			panel3.add(2, 1, getlbType());
			panel3.add(2, 2, getrdTypeVerify());
			panel3.add(2, 3, getrdTypeRedBlue());
			
			add(pane);
			add(panel3);
			
			
			
			
//			setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
//			LayoutPanel panel1 = new LayoutPanel(1,2);
//			panel1.setOpaque(false);
//			panel1.add(1, 1, getlbSubj());
//			panel1.add(1, 2, getRefSubj());
//			LayoutPanel panel2 = new LayoutPanel(4,4);
//			panel2.setOpaque(false);
//			panel2.add(1, 1, getlbDate());
//			panel2.add(1, 2, getbeginDate());
//			panel2.add(1, 3, LayoutPanel.CENTER,getLine());
//			panel2.add(1, 4, getEndDate());
//			panel2.add(2, 1, getlbCurr());
//			panel2.add(2, 2, getrefCurr());
//			panel2.add(2, 3, getlbVerifyNo());
//			panel2.add(2, 4, gettfVerifyNo());
//			panel2.add(3, 1, UIMoneyRangeLabel);
//			panel2.add(3, 2, getUITextBegMoney());
//			panel2.add(3, 3, LayoutPanel.CENTER,UILineLabel);
//			panel2.add(3, 4, getUITextEndMoney());
//			panel2.add(4, 1, UIDigestLabel);
//			panel2.add(4, 2, getUITextDigest());
//			LayoutPanel panel3 = new LayoutPanel(2,4);
//			panel3.setOpaque(false);
//			panel3.add(1, 1, getDateType());
//			panel3.add(1, 2, getrdVoucherDate());
//			panel3.add(1, 3, getrdBusinessDate());
//			panel3.add(1, 4, getrdVerifyDate());
//			panel3.add(2, 1, getlbType());
//			panel3.add(2, 2, getrdTypeVerify());
//			panel3.add(2, 3, getrdTypeRedBlue());
//
//			add(panel1);
//			add(getassPane());
//			add(panel2);
//			add(panel3);
			
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initData();
		// user code end
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
			QryCondPane aQryCondPane;
			aQryCondPane = new QryCondPane();
			frame.setContentPane(aQryCondPane);
			frame.setSize(aQryCondPane.getSize());
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
			System.err.println("nc.ui.pub.beans.UIPanel 的 main() 中发生异常");
			nc.bs.logging.Logger.error(exception.getMessage(), exception);
		}
	}

	/**
	 * Comment
	 */
	public void refSubj_ValueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
//		getChooser().setCorp(getPk_corp());
		getChooser().setPk_glorgbook(getPk_glorgbook());
		getChooser().setPk_Subj(getRefSubj().getRefPK(), true);
		return;
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-7 15:58:15) 参数：<|> 返回值： 算法：
	 * 
	 * @param newChooer
	 *            nc.ui.glverifycom.AssChooser
	 */
	public void setChooser(nc.ui.glverifycom.AssChooser newChooer) {
		m_assChooser = newChooer;
	}

	/**
	 * 功能：设置查询条件为凭证过滤的查询条件 作者：宋涛 创建时间：(2003-5-19 19:42:47) 参数：<|> 返回值： 算法：
	 * 
	 */
	public void setDefaultValues() {
		nc.vo.glrp.verify.FilterCondVO qryVo = getModel().getQryVo();
		if (qryVo != null) {
			if (qryVo.getDebitCond() != null && qryVo.getDebitCond().getVoucherDetailPk() == null) {
				qryVo = qryVo.getDebitCond();

			} else if (qryVo.getCreditCond() != null && qryVo.getCreditCond().getVoucherDetailPk() == null) {
				qryVo = qryVo.getCreditCond();
			} else {
				qryVo = null;
			}
			if (qryVo != null) {
				if (qryVo.getSubjPk() != null) {
					getRefSubj().setPK(qryVo.getSubjPk());
				} else if (qryVo.getSubjCode() != null) {
					try {
						getRefSubj().setPK(
								AccountCache.getInstance().getAccountVOByCode(qryVo.getPk_glorgbook(), qryVo.getSubjCode())
										.getPk_accasoa());
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
					}
				}
				if (qryVo.getAssvos() != null) {
					try {
						getChooser().setPk_Subj(getRefSubj().getRefPK(), true);
						getChooser().setAssVOs(qryVo.getAssvos());
					} catch (Exception ex) {
nc.bs.logging.Logger.error(ex.getMessage(), ex);
					}
				}
				if (qryVo.getBeginDate() != null) {
					getbeginDate().setValueObj(new UFDate(qryVo.getBeginDate()));
				}
				if (qryVo.getEndDate() != null) {
					getEndDate().setValueObj(new UFDate(qryVo.getEndDate()));
				}
				if (qryVo.getCurPk() != null) {
					getrefCurr().setPK(qryVo.getCurPk());
				}
				if (qryVo.getVerifyNo() != null) {
					gettfVerifyNo().setText(qryVo.getVerifyNo());
				}
				if (qryVo.getDateType().equalsIgnoreCase("prepareddate")) {
					getrdVoucherDate().setSelected(true);
				} else {
					getrdBusinessDate().setSelected(true);
				}
			}
		}
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-16 9:40:00) 参数：<|> 返回值： 算法：
	 * 
	 * @param newModel
	 *            nc.ui.glrp.verify.IVerifyModel
	 */
	public void setModel(IVerifyModel newModel) {
		m_verifyModel = newModel;
	}

	/**
	 * 功能：结束编辑 作者：宋涛 创建时间：(2003-5-29 10:32:44) 参数：<|> 返回值： 算法：
	 * 
	 */
	private void stopEdit() {
		getRefSubj().processFocusLost();
		getrefCurr().processFocusLost();
		getChooser().stopEditing();
	}

	/**
	 * This method initializes UITextBegMoney
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	private UITextField getUITextBegMoney() {
		if (UITextBegMoney == null) {
			UITextBegMoney = new UITextField();
//			UITextBegMoney.setBounds(new Rectangle(100, 247, 77, 20));
			UITextBegMoney.setTextType("TextDbl");
			UITextBegMoney.setNumPoint(4);
			// UITextBegMoney.setAllowNumeric(true);
		}
		return UITextBegMoney;
	}

	/**
	 * This method initializes UITextEndMoney
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	private UITextField getUITextEndMoney() {
		if (UITextEndMoney == null) {
			UITextEndMoney = new UITextField();
//			UITextEndMoney.setBounds(new Rectangle(251, 247, 84, 20));
			UITextEndMoney.setTextType("TextDbl");
			UITextEndMoney.setNumPoint(4);
			// UITextEndMoney.setAllowNumeric(true);
		}
		return UITextEndMoney;
	}

	/**
	 * This method initializes UITextDigest
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	private UITextField getUITextDigest() {
		if (UITextDigest == null) {
			UITextDigest = new UITextField();
//			UITextDigest.setBounds(new Rectangle(100, 275, 235, 20));
		}
		return UITextDigest;
	}

	public void setPk_accountingbook(String pkAccountingbook) {
		pk_accountingbook = pkAccountingbook;
		refreshWhenAccountingbookNotNull(pk_accountingbook);
	}

	private void refreshWhenAccountingbookNotNull(String pk_accountingbook) {

		getRefSubj().setPK(null);
		String[] versionInfo = new String[2];
		versionInfo[0] = getPk_glorgbook();
		versionInfo[1] = GlWorkBench.getBusiDate().toStdString();
		getRefSubj().getRefModel().setPara(versionInfo);
//		String sWhere = getRefSubj().getRefModel().getWherePart();
//		if (sWhere != null && sWhere.length() > 0) {
//			sWhere += " and ";
//		} else if (sWhere != null) {
//			sWhere = "";
//		}

//		StringBuffer wherePart = new StringBuffer();
		// wherePart.append(" exists (select subj1.pk_accasoa from( ");
		// modified by hurh 2011-04-07 exists 过滤失效
		// wherePart.append(" bd_accasoa.pk_accasoa in (select subj1.pk_accasoa from( ");
		// wherePart.append(" 		select s1.pk_accasoa,s2.code from bd_accasoa s1,bd_account s2 where s1.pk_account = s2.pk_account ");
		// wherePart.append(" 		and s1.pk_accchart in( ");
		// wherePart.append(" 			select pk_curraccchart from org_accountingbook where pk_accountingbook='" + getPk_glorgbook() +
		// "' ");
		// wherePart.append(" 		) ");
		// wherePart.append(" 		and s2.enablestate = 2 ");
		// wherePart.append(" ) subj1 ");
		// wherePart.append(" INNER JOIN gl_verifyObj ON gl_verifyObj.Pk_accasoa = subj1.Pk_accasoa ");
		// wherePart.append(" where gl_verifyObj.pk_accountingbook='" + getPk_glorgbook() + "' ");
		// wherePart.append(" and len(bd_account.code) >= len(subj1.code) ");
		// wherePart.append(" and substring(bd_account.code,1,len(subj1.code)) = subj1.code ");
		// wherePart.append(" and gl_verifyObj.userFlag='Y' ");
		// wherePart.append(" ) ");

		// hurh 2011-04-23
//		wherePart.append(" bd_accasoa.pk_accasoa in ( ");
//		wherePart.append(" select acca.pk_accasoa from ( ");
//		wherePart.append(" select pk_accasoa, pk_account, pk_accchart from bd_accasoa ");
//		wherePart.append(" union all select pk_accasoa, pk_account, pk_accchart from bd_accasoahistory ");
//		wherePart.append(" ) acca, bd_account acco, ( ");
//		wherePart.append(" select subj.code ");
//		wherePart.append(" from bd_accasoa con inner join bd_account subj ");
//		wherePart.append(" on con.pk_account = subj.pk_account ");
//		wherePart.append(" where con.pk_accasoa in ( ");
//		wherePart.append(" select pk_accasoa ");
//		wherePart.append(" from gl_verifyobj obj ");
//		wherePart.append(" where obj.pk_accountingbook = '").append(getPk_glorgbook()).append("' ");
//		wherePart.append(" and obj.userflag = 'Y')) subj ");
//		wherePart.append(" where acca.pk_account = acco.pk_account ");
//		wherePart.append(" and len (rtrim (acco.code)) >= len (rtrim (subj.code)) ");
//		wherePart.append(" and substring (acco.code, 1, len (rtrim (subj.code))) = subj.code ");
//		wherePart.append(" and acca.pk_accchart in ( ");
//		wherePart.append(" select pk_curraccchart ");
//		wherePart.append(" from org_accountingbook ");
//		wherePart.append(" WHERE pk_accountingbook = '").append(getPk_glorgbook()).append("')) ");

//		sWhere += wherePart.toString();
//		getRefSubj().setWhereString(sWhere);
		
		
		String[] pks = nc.ui.gl.ageanalysis.NormalConditionPanel.getAccountPks(new String[]{getPk_glorgbook()});
		ivjRefSubj.getRefModel().setFilterPks(pks);

	}

	/**
	 * 
	 * 方法说明：
	 * <p>
	 * 修改记录：
	 * </p>
	 * 
	 * @param pk_currtype
	 * @see
	 * @since V6.0
	 */
	public void setCurrType(String pk_currtype) {
		getrefCurr().setPK(pk_currtype);
	}

} // @jve:decl-index=0:visual-constraint="10,10"