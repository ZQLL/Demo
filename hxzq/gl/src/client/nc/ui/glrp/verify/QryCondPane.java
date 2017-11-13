package nc.ui.glrp.verify;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.logging.Logger;
import nc.gl.glconst.systemtype.SystemtypeConst;
import nc.pubitf.accperiod.AccountCalendar;
import nc.ui.gl.datacache.AccountCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.view.LayoutPanel;
import nc.ui.glverifycom.AssChooser;
import nc.ui.pub.beans.UIPanel;
import nc.ui.subjvery.ClientInfo;
import nc.vo.fipub.utils.AccountCalendarUtils;
import nc.vo.gateway60.itfs.Currency;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.FilterCondVO;
import nc.vo.glrp.verifyobj.VerifyObjHeaderVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能：核销过滤条件pane 作者：宋涛 创建时间：(2003-5-7 15:04:36) 使用说明：以及别人可能感兴趣的介绍 注意：现存Bug
 */
@SuppressWarnings("serial")
public class QryCondPane extends nc.ui.pub.beans.UIPanel implements
		IConditionPane {
	private nc.ui.pub.beans.UIPanel ivjassPane = null;

	private nc.ui.pub.beans.UIRefPane ivjbeginDate = null;

	private nc.ui.pub.beans.UICheckBox ivjchbUnSign = null;

	private nc.ui.pub.beans.UIComboBox ivjcmbDirect = null;

	private nc.ui.pub.beans.UIComboBox ivjcmbMnyType = null;

	private nc.ui.pub.beans.UILabel ivjDateType = null;

	private nc.ui.pub.beans.UIRefPane ivjEndDate = null;

	private nc.ui.pub.beans.UILabel ivjlbCurr = null;

	private nc.ui.pub.beans.UILabel ivjlbDate = null;

	private nc.ui.pub.beans.UILabel ivjlbDigest = null;

	private nc.ui.pub.beans.UILabel ivjlbDirect = null;

	private nc.ui.pub.beans.UILabel ivjlbSubj = null;

	private nc.ui.pub.beans.UILabel ivjlbVerifyNo = null;

	private nc.ui.pub.beans.UILabel ivjlbYe = null;

	private nc.ui.pub.beans.UILabel ivjLine = null;

	private nc.ui.pub.beans.UILabel ivjline1 = null;

	private nc.ui.pub.beans.UITextField ivjMny_begin = null;

	private nc.ui.pub.beans.UITextField ivjMny_end = null;

	private javax.swing.ButtonGroup ivjradiopane = null;

	private nc.ui.pub.beans.UIRadioButton ivjrdBusinessDate = null;

	private nc.ui.pub.beans.UIRadioButton ivjrdVoucherDate = null;

	private nc.ui.pub.beans.UIRefPane ivjrefCurr = null;

	private nc.ui.pub.beans.UIRefPane ivjRefSubj = null;

	private nc.ui.pub.beans.UITextField ivjtfDigest = null;

	private nc.ui.pub.beans.UITextField ivjtfVerifyNo = null;

	private nc.ui.glverifycom.AssChooser m_assChooser;

	/* 应用类型 0-即时核销，1-借贷核销 */
	private int m_iType = nc.vo.glrp.pub.VerifyMsg.TYPE_VERIFY;

	/* 核销model */
	private IVerifyModel m_verifyModel;

	private String pk_accountingbook;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements nc.ui.pub.beans.ValueChangedListener {
		public void valueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
			if (event.getSource() == QryCondPane.this.getRefSubj())
				connEtoC1(event);
		};
	};

	public QryCondPane() {
		super();
		initialize();
	}

	public QryCondPane(java.awt.LayoutManager p0) {
		super(p0);
	}

	public QryCondPane(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
	}

	public QryCondPane(boolean p0) {
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

		UFDate beginDateTemp = null;
		UFDate endDateTemp = null;

		if (getEndDate().getDateEnd() == null) {
			return VerifyMsg.getMSG_NO_ENDDATE_ERROR();
		} else {
			endDateTemp = getEndDate().getDateEnd();
		}
		
		//add by lihaibo 校验开始日期
		if (getbeginDate().getDateBegin() == null) {
			return "开始日期不能为空！";
		} else {
			beginDateTemp = getbeginDate().getDateBegin();
		}

//		if (getbeginDate().getDateBegin() != null) {
//			beginDateTemp = getbeginDate().getDateBegin();
//		}

		if (endDateTemp.after(GlWorkBench.getBusiDate().asEnd())) {
			return VerifyMsg.getMSG_LOGONDATE_EARLY_THAN_ENDDATE_ERROR();
		}

		if (beginDateTemp != null && beginDateTemp.after(endDateTemp)) {
			return VerifyMsg.getMSG_ENDDATE_EARLY_THAN_STARTDATE_ERROR();
		}

		if (getMny_begin().getText() != null
				&& getMny_begin().getText().trim().length() > 0
				&& getMny_end().getText() != null
				&& getMny_end().getText().trim().length() > 0) {
			if (new UFDouble(getMny_begin().getText().trim())
					.compareTo(new UFDouble(getMny_end().getText().trim())) > 0) {
				return VerifyMsg.getMSG_MNYRANGE_ERROR();
			}
		}

		getChooser().stopEditing();
		/* 检查辅助核算 */
		try {
			if (!getModel().isAssLegal(getChooser().getAssResults(),
					getModel().getVerifyObject())) {
				return VerifyMsg.getMSG_ASS_ERROR();
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return VerifyMsg.getMSG_ASS_ERROR();
		}
		return null;
	}

	private void connEtoC1(nc.ui.pub.beans.ValueChangedEvent arg1) {
		try {
			this.refSubj_ValueChanged(arg1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private nc.ui.pub.beans.UIPanel getassPane() {
		if (ivjassPane == null) {
			try {
				ivjassPane = new nc.ui.pub.beans.UIPanel();
				ivjassPane.setName("assPane");
				ivjassPane.setLayout(new java.awt.BorderLayout());
				ivjassPane.add(getChooser(), BorderLayout.CENTER);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjassPane;
	}

	private nc.ui.pub.beans.UIRefPane getbeginDate() {
		if (ivjbeginDate == null) {
			try {
				ivjbeginDate = new nc.ui.pub.beans.UIRefPane();
				ivjbeginDate.setName("beginDate");
				ivjbeginDate.setRefNodeName("日历");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbeginDate;
	}

	private nc.ui.pub.beans.UICheckBox getchbUnSign() {
		if (ivjchbUnSign == null) {
			try {
				ivjchbUnSign = new nc.ui.pub.beans.UICheckBox();
				ivjchbUnSign.setName("chbUnSign");
				ivjchbUnSign.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000257")/**
				 * @res*
				 *      "包含未记账凭证"
				 */
				);
				ivjchbUnSign.setOpaque(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjchbUnSign;
	}

	public nc.ui.glverifycom.AssChooser getChooser() {
		if (m_assChooser == null) {
			try {
				m_assChooser = new AssChooser(false);
				m_assChooser.setName("ccc");
				m_assChooser.setPreferredSize(new Dimension(400, 120));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return m_assChooser;
	}

	private nc.ui.pub.beans.UIComboBox getcmbDirect() {
		if (ivjcmbDirect == null) {
			try {
				ivjcmbDirect = new nc.ui.pub.beans.UIComboBox();
				ivjcmbDirect.setName("cmbDirect");
				ivjcmbDirect.addItem(VerifyMsg.getDISP_DEBIT());
				ivjcmbDirect.addItem(VerifyMsg.getDISP_CREDIT());
				ivjcmbDirect.addItem(VerifyMsg.getDISP_BOTHDIRECT());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjcmbDirect;
	}

	private nc.ui.pub.beans.UIComboBox getcmbMnyType() {
		if (ivjcmbMnyType == null) {
			try {
				ivjcmbMnyType = new nc.ui.pub.beans.UIComboBox();
				ivjcmbMnyType.setName("cmbMnyType");
				ivjcmbMnyType.setMaximumRowCount(3);
				ivjcmbMnyType.setSelectedIndex(-1);
				ivjcmbMnyType.addItem(VerifyMsg.getDISP_ORIGIN());
				ivjcmbMnyType.addItem(VerifyMsg.getDISP_LOCAL());
				if (Currency.isStartGroupCurr(GlWorkBench.getLoginGroup())) {
					ivjcmbMnyType.addItem(VerifyMsg.getDISP_GROUP_AMOUNT());
				}
				if (Currency.isStartGlobalCurr()) {
					ivjcmbMnyType.addItem(VerifyMsg.getDISP_GLOBAL_AMOUNT());
				}
				ivjcmbMnyType.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {// 当选择项变化时，重新设置未核销余额开始和结束框数字精度
						// TODO Auto-generated method stub
						String item = e.getItem().toString();
						getMny_begin().setNumPoint(getSelectCurrDigit(item));
						getMny_end().setNumPoint(getSelectCurrDigit(item));
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjcmbMnyType;
	}

	private nc.ui.pub.beans.UILabel getDateType() {
		if (ivjDateType == null) {
			try {
				ivjDateType = new nc.ui.pub.beans.UILabel();
				ivjDateType.setName("DateType");
				ivjDateType.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000250")/**
				 * @res*
				 *      "分析日期："
				 */
				);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjDateType;
	}

	public nc.ui.pub.beans.UIRefPane getEndDate() {
		if (ivjEndDate == null) {
			try {
				ivjEndDate = new nc.ui.pub.beans.UIRefPane();
				ivjEndDate.setName("DateEnd");
				ivjEndDate.setRefNodeName("日历");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjEndDate;
	}

	public nc.vo.glrp.verify.FilterCondVO getFilterVOEx() {
		if (ClientInfo.getInstance(getPk_accountingbook()).getDateName()
				.equalsIgnoreCase("VoucherDate")) {
			getrdVoucherDate().setSelected(true);
			getrdBusinessDate().setSelected(false);
		} else {
			getrdVoucherDate().setSelected(false);
			getrdBusinessDate().setSelected(true);
		}
		if (getType() == nc.vo.glrp.pub.VerifyMsg.TYPE_TIMELY) {
			getcmbDirect().setEnabled(false);
			getrefCurr().setEnabled(false);
			getRefSubj().setEnabled(false);
		}
		getEndDate().setValueObj(GlWorkBench.getBusiDate());

		getbeginDate().setValueObj(null);

		getrefCurr().setPK(
				ClientInfo.getInstance(getPk_accountingbook()).getLocalPk());

		FilterCondVO voCond = new FilterCondVO();
		FilterCondVO debitCond = new FilterCondVO();
		FilterCondVO creditCond = new FilterCondVO();

		// added by chengsc
		creditCond.setPkcorp(getPk_corp());
		creditCond.setPkuser(getPkUser());

		creditCond.setPk_glorgbook(getPk_glorgbook());
		creditCond.setPkSob(getPk_Sob());
		creditCond.setLogonDate(ClientInfo.getInstance(getPk_accountingbook())
				.getLogonDate().toString());

		nc.vo.glcom.ass.AssVO[] assvos = getChooser().getAssResults();
		if (assvos != null && assvos.length > 0) {
			creditCond.setAssvos(assvos);
		} else {
			creditCond.setAssvos(null);
		}

		if (getbeginDate().getValueObj() != null) {
			creditCond.setBeginDate(getbeginDate().getValueObj().toString());
		} else {
			creditCond.setBeginDate(null);
		}
		if (getEndDate().getDateEnd() != null) {
			creditCond.setEndDate(getEndDate().getDateEnd().toString());
		} else {
			creditCond.setEndDate(null);
		}
		creditCond.setDateType(getrdVoucherDate().isSelected() ? "prepareddate"
				: "Businessdate");
		creditCond.setMnyType(getcmbMnyType().getSelectedItem().toString());
		creditCond.setDigest(null);
		/* 币种 */
		creditCond.setCurPk(getrefCurr().getRefPK());
		creditCond.setVerifyNo(null);
		creditCond.sethasTally(true);
		if (getcmbDirect().getSelectedIndex() == 0) {
			creditCond.setDirect(1);
		} else {
			creditCond.setDirect(-1);
		}
		voCond.setCreditCond(creditCond);
		voCond.setDebitCond(debitCond);
		return voCond;
	}

	public nc.vo.glrp.verify.FilterCondVO getFilterVO() {
		FilterCondVO voCond = new FilterCondVO();
		FilterCondVO debitCond = new FilterCondVO();
		FilterCondVO creditCond = new FilterCondVO();
		nc.vo.bd.account.AccountVO voSubj = AccountCache.getInstance()
				.getAccountVOByPK(getPk_accountingbook(),
						getRefSubj().getRefPK());
		creditCond.setSubjPk(voSubj.getPk_accasoa());
		creditCond.setSubjCode(getRefSubj().getRefCode());
		creditCond.setPkcorp(getPk_corp());
		creditCond.setPkuser(getPkUser());
		creditCond.setPk_glorgbook(getPk_glorgbook());
		creditCond.setPkSob(getPk_Sob());
		creditCond.setLogonDate(GlWorkBench.getBusiDate().asEnd().toString());
		try {
			creditCond.setObjBeginDate(new UFDate(getbeginDate().getDateBegin()
					.toLocalString()).toString());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		nc.vo.glcom.ass.AssVO[] assvos = getChooser().getAssResults();
		if (assvos != null && assvos.length > 0) {
			creditCond.setAssvos(assvos);
		} else {
			creditCond.setAssvos(null);
		}
		if (getbeginDate().getDateBegin() != null) {
			creditCond.setBeginDate(new UFDate(getbeginDate().getDateBegin()
					.toLocalString()).toString());
		} else {
			creditCond.setBeginDate(null);
		}
		if (getEndDate().getDateEnd() != null) {
			creditCond.setEndDate(getEndDate().getDateEnd().asEnd().toString());
		} else {
			creditCond.setEndDate(null);
		}
		creditCond.setDateType(getrdVoucherDate().isSelected() ? "prepareddate"
				: "Businessdate");
		creditCond.setMnyType(getcmbMnyType().getSelectedItem().toString());
		if (getMny_begin().getText() != null
				&& getMny_begin().getText().length() > 0) {
			creditCond.setMnyBegin(new nc.vo.pub.lang.UFDouble(getMny_begin()
					.getText()));
		} else {
			creditCond.setMnyBegin(null);
		}
		if (getMny_end().getText() != null
				&& getMny_end().getText().length() > 0) {
			creditCond.setMnyEnd(new nc.vo.pub.lang.UFDouble(getMny_end()
					.getText()));
		} else {
			creditCond.setMnyEnd(null);
		}
		/* 摘要 */
		if (gettfDigest().getText() != null
				&& gettfDigest().getText().length() > 0) {
			creditCond.setDigest(gettfDigest().getText());
		} else {
			creditCond.setDigest(null);
		}
		/* 币种 */
		creditCond.setCurPk(getrefCurr().getRefPK());
		if (gettfVerifyNo().getText() != null
				&& gettfVerifyNo().getText().length() > 0) {
			creditCond.setVerifyNo(gettfVerifyNo().getText());
		} else {
			creditCond.setVerifyNo(null);
		}
		creditCond.sethasTally(getchbUnSign().isSelected());
		/* 即时核销 */
		if (getType() == VerifyMsg.TYPE_TIMELY) {
			if (getcmbDirect().getSelectedIndex() == 0) {
				creditCond.setDirect(1);
			} else {
				creditCond.setDirect(-1);
			}
			debitCond.setVoucherDetailPk(getModel().getVerifyDetailvo()
					.getPk_detail());
			if (getcmbDirect().getSelectedIndex() == 0) { /* 借方 */
				voCond.setCreditCond(debitCond);
				voCond.setDebitCond(creditCond);
			} else {
				voCond.setCreditCond(creditCond);
				voCond.setDebitCond(debitCond);
			}
		} else { /* 事后核销 */
			if (getcmbDirect().getSelectedIndex() == 0) {
				voCond.setCreditCond(creditCond);
				creditCond.setDirect(1);
				voCond.setDebitCond(creditCond);
			} else if (getcmbDirect().getSelectedIndex() == 1) {
				creditCond.setDirect(-1);
				voCond.setCreditCond(creditCond);
				voCond.setDebitCond(null);
			} else {
				creditCond.setDirect(-1);
				debitCond.setAssvos(creditCond.getAssvos());
				debitCond.setPkcorp(creditCond.getPkcorp());
				debitCond.setPkuser(creditCond.getPkuser());
				debitCond.setPk_glorgbook(creditCond.getPk_glorgbook());
				debitCond.setPkSob(creditCond.getPkSob());
				debitCond.setSubjCode(creditCond.getSubjCode());
				debitCond.setSubjPk(creditCond.getSubjPk());
				debitCond.setBeginDate(creditCond.getBeginDate());
				debitCond.setEndDate(creditCond.getEndDate());
				debitCond.setLogonDate(creditCond.getLogonDate());
				debitCond.setObjBeginDate(creditCond.getObjBeginDate());
				debitCond.setDateType(creditCond.getDateType());
				debitCond.setMnyType(creditCond.getMnyType());
				debitCond.setMnyBegin(creditCond.getMnyBegin());
				debitCond.setMnyEnd(creditCond.getMnyEnd());
				debitCond.setDigest(creditCond.getDigest());
				debitCond.setDirect(1);
				debitCond.setCurPk(creditCond.getCurPk());
				debitCond.setVerifyNo(creditCond.getVerifyNo());
				debitCond.sethasTally(creditCond.hasTally());
				voCond.setCreditCond(creditCond);
				voCond.setDebitCond(debitCond);
			}
		}
		return voCond;
	}

	public nc.vo.glrp.verify.FilterCondVO getFilterVO(
			nc.vo.glrp.verify.VerifyDetailVO voDetail) throws Exception {
		setPk_accountingbook(voDetail.getPk_glorgbook());
		initData();
		setDefaultData();
		FilterCondVO voCond = getFilterVO();
		String sMsg = checkCondition();
		if (sMsg != null) {
			throw new nc.vo.pub.BusinessException(
					VerifyMsg.getMSG_CONDITION_ERROR() + ":" + sMsg);
		} else {
			return voCond;
		}
	}

	private nc.ui.pub.beans.UILabel getlbCurr() {
		if (ivjlbCurr == null) {
			try {
				ivjlbCurr = new nc.ui.pub.beans.UILabel();
				ivjlbCurr.setName("lbCurr");
				ivjlbCurr.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("subjverify_0", "02002004-0020")/*
																	 * @res
																	 * "原币币种："
																	 */);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjlbCurr;
	}

	private nc.ui.pub.beans.UILabel getlbDate() {
		if (ivjlbDate == null) {
			try {
				ivjlbDate = new nc.ui.pub.beans.UILabel();
				ivjlbDate.setName("lbDate");
				ivjlbDate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021201", "UPP20021201-000251")/** @res* "日期范围：" */
				);
				ivjlbDate
						.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjlbDate;
	}

	private nc.ui.pub.beans.UILabel getlbDigest() {
		if (ivjlbDigest == null) {
			try {
				ivjlbDigest = new nc.ui.pub.beans.UILabel();
				ivjlbDigest.setName("lbDigest");
				ivjlbDigest.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000258")/**
				 * @res*
				 *      "摘要："
				 */
				);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjlbDigest;
	}

	private nc.ui.pub.beans.UILabel getlbDirect() {
		if (ivjlbDirect == null) {
			try {
				ivjlbDirect = new nc.ui.pub.beans.UILabel();
				ivjlbDirect.setName("lbDirect");
				ivjlbDirect.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000166")/**
				 * @res*
				 *      "方向："
				 */
				);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjlbDirect;
	}

	private nc.ui.pub.beans.UILabel getlbSubj() {
		if (ivjlbSubj == null) {
			try {
				ivjlbSubj = new nc.ui.pub.beans.UILabel();
				ivjlbSubj.setName("lbSubj");
				ivjlbSubj.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000252")/**
				 * @res*
				 *      "核销对象科以及下级科目"
				 */
				);
				ivjlbSubj.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021201", "UPP20021201-000163")/** @res* "核销对象科目：" */
				);
				ivjlbSubj
						.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjlbSubj;
	}

	private nc.ui.pub.beans.UILabel getlbVerifyNo() {
		if (ivjlbVerifyNo == null) {
			try {
				ivjlbVerifyNo = new nc.ui.pub.beans.UILabel();
				ivjlbVerifyNo.setName("lbVerifyNo");
				ivjlbVerifyNo.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000254")/**
				 * @res*
				 *      "核销号："
				 */
				);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjlbVerifyNo;
	}

	private nc.ui.pub.beans.UILabel getlbYe() {
		if (ivjlbYe == null) {
			try {
				ivjlbYe = new nc.ui.pub.beans.UILabel();
				ivjlbYe.setName("lbYe");
				ivjlbYe.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021201", "UPP20021201-000259")/** @res* "未核销余额范围：" */
				);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjlbYe;
	}

	private nc.ui.pub.beans.UILabel getLine() {
		if (ivjLine == null) {
			try {
				ivjLine = new nc.ui.pub.beans.UILabel();
				ivjLine.setName("Line");
				ivjLine.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021201", "UPP20021201-000249")/** @res* "——" */
				);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjLine;
	}

	private nc.ui.pub.beans.UILabel getline1() {
		if (ivjline1 == null) {
			try {
				ivjline1 = new nc.ui.pub.beans.UILabel();
				ivjline1.setName("line1");
				ivjline1.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021201", "UPP20021201-000260")/** @res* "—" */
				);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjline1;
	}

	private nc.ui.pub.beans.UITextField getMny_begin() {
		if (ivjMny_begin == null) {
			try {
				ivjMny_begin = new nc.ui.pub.beans.UITextField();
				ivjMny_begin.setName("Mny_begin");
				ivjMny_begin.setPreferredSize(new Dimension(100, 20));
				ivjMny_begin.setTextType("TextDbl");
				ivjMny_begin.setNumPoint(getSelectCurrDigit(VerifyMsg
						.getDISP_ORIGIN()));// 默认选择是原币
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjMny_begin;
	}

	private nc.ui.pub.beans.UITextField getMny_end() {
		if (ivjMny_end == null) {
			try {
				ivjMny_end = new nc.ui.pub.beans.UITextField();
				ivjMny_end.setName("Mny_end");
				ivjMny_end.setPreferredSize(new Dimension(100, 20));
				ivjMny_end.setTextType("TextDbl");
				ivjMny_end.setNumPoint(getSelectCurrDigit(VerifyMsg
						.getDISP_ORIGIN()));// 默认选择是原币
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjMny_end;
	}

	public IVerifyModel getModel() {
		return m_verifyModel;
	}

	private String getPk_corp() {
		return null;
	}

	private String getPk_glorgbook() {
		if (pk_accountingbook == null) {
			pk_accountingbook = GlWorkBench.getDefaultMainOrg();
		}
		return pk_accountingbook;
	}

	private String getPkUser() {
		return GlWorkBench.getLoginUser();
	}

	private String getPk_Sob() {
		return nc.ui.subjvery.ClientInfo.getInstance(getPk_accountingbook())
				.getPk_Sob();
	}

	private javax.swing.ButtonGroup getradiopane() {
		if (ivjradiopane == null) {
			try {
				ivjradiopane = new javax.swing.ButtonGroup();
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjradiopane;
	}

	private nc.ui.pub.beans.UIRadioButton getrdBusinessDate() {
		if (ivjrdBusinessDate == null) {
			try {
				ivjrdBusinessDate = new nc.ui.pub.beans.UIRadioButton();
				ivjrdBusinessDate.setName("rdBusinessDate");
				ivjrdBusinessDate.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000090")/**
				 * @res*
				 *      "业务日期"
				 */
				);
				ivjrdBusinessDate.setFocusPainted(false);
				ivjrdBusinessDate.setOpaque(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjrdBusinessDate;
	}

	private nc.ui.pub.beans.UIRadioButton getrdVoucherDate() {
		if (ivjrdVoucherDate == null) {
			try {
				ivjrdVoucherDate = new nc.ui.pub.beans.UIRadioButton();
				ivjrdVoucherDate.setName("rdVoucherDate");
				ivjrdVoucherDate.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021201", "UPP20021201-000015")/**
				 * @res*
				 *      "凭证日期"
				 */
				);
				ivjrdVoucherDate.setFocusPainted(false);
				ivjrdVoucherDate.setOpaque(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjrdVoucherDate;
	}

	private nc.ui.pub.beans.UIRefPane getrefCurr() {
		if (ivjrefCurr == null) {
			try {
				ivjrefCurr = new nc.ui.pub.beans.UIRefPane();
				ivjrefCurr.setName("refCurr");
				ivjrefCurr.setRefNodeName("币种档案");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjrefCurr;
	}

	private nc.ui.pub.beans.UIRefPane getRefSubj() {
		if (ivjRefSubj == null) {
			try {
				ivjRefSubj = new nc.ui.pub.beans.UIRefPane();
				ivjRefSubj.setName("RefSubj");
				ivjRefSubj.setRefNodeName("会计科目");
				ivjRefSubj.setDataPowerOperation_code(SystemtypeConst.FI);
				ivjRefSubj.getRefModel().setRefCardInfoComponentImplClass(null);
				ivjRefSubj.setCacheEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjRefSubj;
	}

	private nc.ui.pub.beans.UITextField gettfDigest() {
		if (ivjtfDigest == null) {
			try {
				ivjtfDigest = new nc.ui.pub.beans.UITextField();
				ivjtfDigest.setName("tfDigest");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjtfDigest;
	}

	private nc.ui.pub.beans.UITextField gettfVerifyNo() {
		if (ivjtfVerifyNo == null) {
			try {
				ivjtfVerifyNo = new nc.ui.pub.beans.UITextField();
				ivjtfVerifyNo.setName("tfVerifyNo");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjtfVerifyNo;
	}

	public int getType() {
		return m_iType;
	}

	private void handleException(java.lang.Throwable exception) {
		Logger.error(exception.getMessage(), exception);
		throw new GlBusinessException(exception.getMessage());
	}

	private void initConnections() throws java.lang.Exception {
		getradiopane().add(getrdBusinessDate());
		getradiopane().add(getrdVoucherDate());
		getrdBusinessDate().getModel().setGroup(getradiopane());
		getrdVoucherDate().getModel().setGroup(getradiopane());
		getRefSubj().addValueChangedListener(ivjEventHandler);
	}

	private void initData() {
		setSelectbuDate();
		if (getType() == nc.vo.glrp.pub.VerifyMsg.TYPE_TIMELY) {
			getcmbDirect().setEnabled(false);
			getrefCurr().setEnabled(false);
			getRefSubj().setEnabled(false);
		} else {
			getcmbDirect().setEnabled(true);
			getrefCurr().setEnabled(true);
			getRefSubj().setEnabled(true);
		}
		setMonBeginData();
		getEndDate().setValueObj(GlWorkBench.getBusiDate());
		getrefCurr().setPK(
				ClientInfo.getInstance(getPk_accountingbook()).getLocalPk());
		getcmbDirect().setSelectedIndex(2);
	}

	private void setSelectbuDate() {
		if (getPk_accountingbook() == null
				|| ClientInfo.getInstance(getPk_accountingbook()).getDateName()
						.equalsIgnoreCase("VoucherDate")) {
			getrdVoucherDate().setSelected(true);
			getrdBusinessDate().setSelected(false);
		} else {
			getrdVoucherDate().setSelected(false);
			getrdBusinessDate().setSelected(true);
		}
	}

	public void setMonBeginData() {
		AccountCalendar calendar = AccountCalendarUtils.getDefaultCalendar();
		try {
			calendar.setDate(GlWorkBench.getBusiDate());
		} catch (InvalidAccperiodExcetion e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
		//update by lihaibo 制空开始日期
//		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate beginDate = null;
		getbeginDate().setValueObj(beginDate);
	}

	private void initialize() {
		try {
			setName("QryCondPane");
			setLayout(new BorderLayout());
			add(getassPane());
			LayoutPanel pane = new LayoutPanel(8, 4, LayoutPanel.LEFT);
			pane.add(1, 1, getlbSubj());
			pane.add(1, 2, getRefSubj());
			pane.add(2, 1, getassPane(), 1, 4);
			pane.add(3, 1, getlbDate());
			pane.add(3, 2, getbeginDate());
			pane.add(3, 3, getLine());
			pane.add(3, 4, getEndDate());
			pane.add(4, 1, getDateType());
			pane.add(4, 2, getrdVoucherDate());
			pane.add(4, 3, getrdBusinessDate());
			pane.add(5, 1, getlbYe());
			pane.add(5, 2, getcmbMnyType());
			UIPanel rangePane = new UIPanel(new FlowLayout(FlowLayout.LEFT, 0,
					0));
			rangePane.setOpaque(false);
			rangePane.add(getMny_begin());
			rangePane.add(getline1());
			rangePane.add(getMny_end());
			pane.add(5, 3, rangePane, 1, 2);
			pane.add(6, 1, getlbDigest());
			pane.add(6, 2, gettfDigest(), 1, 3);
			pane.add(7, 1, getlbDirect());
			pane.add(7, 2, getcmbDirect());
			pane.add(7, 3, getlbCurr());
			pane.add(7, 4, getrefCurr());
			pane.add(8, 1, getlbVerifyNo());
			pane.add(8, 2, gettfVerifyNo());
			pane.add(8, 3, getchbUnSign(), 1, 2);
			add(pane, BorderLayout.CENTER);
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		initData();
	}

	public void refSubj_ValueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
		getChooser().setPk_glorgbook(getPk_glorgbook());
		getChooser().setPk_Subj(getRefSubj().getRefPK(), true);
		getChooser().stopEditing();
		try {
			getModel().setPk_accountingbook(getPk_glorgbook());
			getModel().getVerifyObject(getRefSubj().getRefPK());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
		return;
	}

	public void setChooser(nc.ui.glverifycom.AssChooser newChooer) {
		m_assChooser = newChooer;
	}

	private void setDefaultData() throws Exception {
		try {
			if (getType() == VerifyMsg.TYPE_TIMELY) {
				nc.vo.glrp.verifyobj.VerifyObjVO voObj = m_verifyModel
						.getVerifyObject();
				int iDirect = getModel().getVerifyDetailvo().getDirect()
						.intValue();
				if (iDirect > 0) {
					getcmbDirect().setSelectedIndex(1);
					getMny_begin().setText(
							getModel().getVerifyDetailvo()
									.getBalancedebitamount().toString());
					getMny_end().setText(
							getModel().getVerifyDetailvo()
									.getBalancedebitamount().toString());
				} else {
					getcmbDirect().setSelectedIndex(0);
					getMny_begin().setText(
							getModel().getVerifyDetailvo()
									.getBalancecreditamount().toString());
					getMny_end().setText(
							getModel().getVerifyDetailvo()
									.getBalancecreditamount().toString());
				}
				if (getrdVoucherDate().isSelected()) {
					getEndDate().setValueObj(
							getModel().getVerifyDetailvo().getPrepareddate());
				} else {
					getEndDate().setValueObj(
							getModel().getVerifyDetailvo().getBusinessdate());
				}
				getrefCurr().setPK(getModel().getVerifyDetailvo().getCurrPk());
				getRefSubj().setPK(
						((VerifyObjHeaderVO) voObj.getParentVO())
								.getPk_accsubj());
				/* 可以添加对辅助核算在过滤的处理 */
				getChooser().setPk_glorgbook(getPk_glorgbook());
				getChooser()
						.setPk_Subj(
								((VerifyObjHeaderVO) voObj.getParentVO())
										.getPk_accsubj(),
								true);
				getChooser().setAssVOs(getModel().getVerifyDetailvo().getAss());
				getchbUnSign().setSelected(true);
			} else {
				getEndDate().setValueObj(GlWorkBench.getBusiDate());
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new nc.vo.pub.BusinessException(
					VerifyMsg.getMSG_CANNTVERIFY());
		}

	}

	public void setType(int newType) {
		m_iType = newType;
		initData();
	}

	public void setVerifyModel(IVerifyModel verifyModel) {
		m_verifyModel = verifyModel;
	}

	private void stopEdit() {
		getRefSubj().processFocusLost();
		getrefCurr().processFocusLost();
		getEndDate().processFocusLost();
		getbeginDate().processFocusLost();
		getChooser().stopEditing();
	}

	public String getPk_accountingbook() {
		if (pk_accountingbook == null) {
			pk_accountingbook = GlWorkBench.getDefaultMainOrg();
		}
		return pk_accountingbook;
	}

	public void setPk_accountingbook(String pkAccountingbook) {
		pk_accountingbook = pkAccountingbook;
		refreshWhenAccountingbookNotNull(pk_accountingbook);
		setSelectbuDate();
	}

	private void refreshWhenAccountingbookNotNull(String pk_accountingbook) {
		getRefSubj().setPK(null);
		getChooser().setAssVOs(null);
		String[] versionInfo = new String[2];
		versionInfo[0] = pk_accountingbook;
		versionInfo[1] = GlWorkBench.getBusiDate().toLocalString();
		getRefSubj().getRefModel().setPara(versionInfo);
		String[] accountpks = nc.ui.gl.ageanalysis.NormalConditionPanel
				.getAccountPks(new String[] { getPk_glorgbook() });
		getRefSubj().getRefModel().setFilterPks(accountpks);
	}

	public void setCurrType(String pk_currtype) {
		getrefCurr().setPK(pk_currtype);
	}

	/**
	 * 根据未核销余额范围选择币种（原币时取币种选择框选择币种，组织本币、集团本币、全局本币时取该核算账簿相对应的币种）获取币种精度
	 * 
	 * @param currtypeName
	 *            原币、组织本币、集团本币、全局本币
	 * @return
	 * @author suhtb
	 */
	private int getSelectCurrDigit(String currtypeName) {
		if (VerifyMsg.getDISP_ORIGIN().equals(
				getcmbMnyType().getSelectedItem().toString())) {// 选择原币
			return Currency.getCurrDigit(getrefCurr().getRefPK());
		} else if (VerifyMsg.getDISP_LOCAL().equals(
				getcmbMnyType().getSelectedItem().toString())) {// 选择组织本币
			return Currency.getCurrDigit(getLocalCurr());
		} else if (VerifyMsg.getDISP_GROUP_AMOUNT().equals(
				getcmbMnyType().getSelectedItem().toString())) {// 选择集团原币
			return Currency.getCurrDigit(getGroupCurr());
		} else if (VerifyMsg.getDISP_GLOBAL_AMOUNT().equals(
				getcmbMnyType().getSelectedItem().toString())) {// 选择全局原币
			return Currency.getCurrDigit(getGlobalCurr());
		}
		return 2;
	}

	/**
	 * 获取组织本币
	 * 
	 * @param vo
	 * @return
	 * @throws GlBusinessException
	 * @author suhtb
	 */
	private java.lang.String getLocalCurr() throws GlBusinessException {
		String m_strLocalCurr = null;
		try {
			m_strLocalCurr = nc.itf.fi.pub.Currency
					.getLocalCurrPK(getPk_accountingbook());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20020401", "UPP20020401-000109")/**
			 * @res*
			 *      "请先设置本位币!"
			 */
			);
		}
		return m_strLocalCurr;
	}

	/**
	 * 获取集团本币
	 * 
	 * @param vo
	 * @return
	 * @throws GlBusinessException
	 * @author suhtb
	 */
	private java.lang.String getGroupCurr() throws GlBusinessException {
		String m_strGroupCurr = null;
		try {
			m_strGroupCurr = nc.itf.fi.pub.Currency
					.getGroupCurrpk(getPk_accountingbook());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20020401", "UPP20020401-000109")/**
			 * @res*
			 *      "请先设置本位币!"
			 */
			);
		}
		return m_strGroupCurr;
	}

	/**
	 * 获取全局本币
	 * 
	 * @param vo
	 * @return
	 * @throws GlBusinessException
	 * @author suhtb
	 */
	private java.lang.String getGlobalCurr() throws GlBusinessException {
		String m_strGlobalCurr = null;
		try {
			m_strGlobalCurr = nc.itf.fi.pub.Currency
					.getGlobalCurrPk(getPk_accountingbook());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20020401", "UPP20020401-000109")/**
			 * @res*
			 *      "请先设置本位币!"
			 */
			);
		}
		return m_strGlobalCurr;
	}

}