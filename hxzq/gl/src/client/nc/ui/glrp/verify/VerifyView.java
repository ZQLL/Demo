package nc.ui.glrp.verify;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.ui.gl.cachefeed.CacheRequestFactory;
import nc.ui.gl.datacache.AccountCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.remotecall.GlRemoteCallProxy;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.vo.gateway60.accountbook.AccountBookUtil;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.gl.remotecall.context.GLRemoteCallContext;
import nc.vo.glcom.ass.AssVO;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.FilterCondVO;
import nc.vo.glrp.verify.LogFilterCondVO;
import nc.vo.glrp.verify.VerifyDetailVO;
import nc.vo.glrp.verifyobj.VerifyObjHeaderVO;
import nc.vo.glrp.verifyobj.VerifyObjItemVO;
import nc.vo.glrp.verifyobj.VerifyObjVO;

/**
 * @ClassName: VerifyView 
 * @Description: TODO(��������������) 
 * @author suhtb
 * @date 2013-5-25 ����10:50:22
 */
public class VerifyView extends nc.ui.pub.beans.UIPanel implements IVerifyView, nc.ui.pub.beans.UIDialogListener {

	private static final long serialVersionUID = 3629327908059529998L;

	private GlVerifyUI ivjverifyui = null;

	private DetailQueryDlg ivjdetaildlg = null;

	private IVerifyModel ivjverifyModel = null;

	private IVerifyConsumer m_parent = null;

	private VerifyStandardDlg ivjm_verifyStandardDlg = null;

	private HistoryQryDlg ivjHistoryDlg = null;

	private HistoryPane ivjHistoryPane = null;

	private nc.ui.glpub.IParent i_parent;

	nc.ui.gl.pubvoucher.VoucherBridge m_VoucherBridge;

	// ��¼��ǰ�������İ�ť
	private int m_ibutton = 0;

	public VerifyView() {
		super();
		initialize();
	}

	public VerifyView(IVerifyConsumer parent) {
		this();
		setparent(parent);
	}

	public void dialogClosed(nc.ui.pub.beans.UIDialogEvent event) {
		try {
			if (event.getSource() == getdetaildlg()) {
				if (event.m_Operation == UIDialogEvent.WINDOW_CANCEL) {
					return;
				} else if (event.m_Operation == UIDialogEvent.WINDOW_OK) {
					/* �����ѯ */
					onquery();
					m_parent.updateTitles(getverifyui().getTitleName());
				}
			}

			if (event.getSource() == getVerifyStandardDlg()) {
				if (event.m_Operation == UIDialogEvent.WINDOW_CANCEL) {
					return;
				} else if (event.m_Operation == UIDialogEvent.WINDOW_OK) {
					/* �Զ����� */
					if (m_ibutton == VerifyButtonKey.KEY_AUTOVERIFY) {
						onAutoVerify();
					} else if (m_ibutton == VerifyButtonKey.KEY_AUTOREDBLUE) {
						onAutoRedBlue();
					}
				}
			}
			if (event.getSource() == getHistoryDlg()) {
				if (event.m_Operation == UIDialogEvent.WINDOW_CANCEL) {
					return;
				} else if (event.m_Operation == UIDialogEvent.WINDOW_OK) {
					/* ��ʷ��ѯ */
					onQueryHisRecord();
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	private IConditionPane getConditionPane() {
		getdetaildlg().getconditionPane().setVerifyModel(getverifyModel());
		return getdetaildlg().getconditionPane();
	}
	
	private DetailQueryDlg getdetaildlg(){
		if (ivjdetaildlg == null) {
			try {
				ivjdetaildlg = new nc.ui.glrp.verify.DetailQueryDlg(getverifyui(), NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000037")/** @res "��ѯ"*/);
				ivjdetaildlg.setName("detaildlg");
				ivjdetaildlg.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				ivjdetaildlg.addUIDialogListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019")/* @res "����" */, ivjExc.getMessage());
			}
		}
		return ivjdetaildlg;
	}

	private HistoryQryDlg getHistoryDlg() {
		if (ivjHistoryDlg == null) {
			try {
				ivjHistoryDlg = new nc.ui.glrp.verify.HistoryQryDlg(this, NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000266")/** @res "��ʷ��ѯ"*/);
				ivjHistoryDlg.setName("HistoryDlg");
				ivjHistoryDlg.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				ivjHistoryDlg.addUIDialogListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjHistoryDlg;
	}

	private HistoryPane getHistoryPane() {
		if (ivjHistoryPane == null) {
			try {
				ivjHistoryPane = new nc.ui.glrp.verify.HistoryPane();
				ivjHistoryPane.setName("HistoryPane");
				ivjHistoryPane.setLocation(792, 18);
				ivjHistoryPane.setVisible(false);
				ivjHistoryPane.setVerifyModel(getverifyModel());
				ivjHistoryPane.setView(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjHistoryPane;
	}

	public String getModuleName() {
		return m_parent.getModuleName();
	}

	public IVerifyModel getverifyModel() {
		if (ivjverifyModel == null) {
			try {
				ivjverifyModel = new nc.ui.glrp.verify.GlVerifyModel();
				ivjverifyModel.setVerifyUI(getverifyui());
				ivjverifyModel.setHistoryUI(getHistoryPane());
				ivjverifyModel.addPropertyChangeListener(getverifyui());
				ivjverifyModel.addPropertyChangeListener(getHistoryPane());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjverifyModel;
	}

	private VerifyStandardDlg getVerifyStandardDlg() {
		if (ivjm_verifyStandardDlg == null) {
			try {
				ivjm_verifyStandardDlg = new nc.ui.glrp.verify.VerifyStandardDlg(this);
				ivjm_verifyStandardDlg.setName("m_verifyStandardDlg");
				ivjm_verifyStandardDlg.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				ivjm_verifyStandardDlg.addUIDialogListener(this);
				ivjm_verifyStandardDlg.setModel(getverifyModel());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjm_verifyStandardDlg;
	}

	private GlVerifyUI getverifyui() {
		if (ivjverifyui == null) {
			try {
				ivjverifyui = new nc.ui.glrp.verify.GlVerifyUI();
				ivjverifyui.setName("verifyui");
				ivjverifyui.setVisible(true);
				ivjverifyui.setVerifyModel(getverifyModel());
				ivjverifyui.setView(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjverifyui;
	}

	private void handleException(java.lang.Throwable exception) {
		 nc.bs.logging.Logger.debug("--------- δ��׽�����쳣 ---------");
		 nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}
	
	private void initialize() {
		try {
			setName("VerifyView");
			setLayout(new java.awt.BorderLayout());
			add(getHistoryPane(), BorderLayout.CENTER);
			add(getverifyui(), BorderLayout.CENTER);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * ���ܣ��Զ����� ���ߣ����� ����ʱ�䣺(2003-5-12 13:38:47) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void onAutoVerify() throws Exception {
		getverifyModel().onAutoVerify();
		onquery();
	}

	// v5.0
	private void onAutoRedBlue() throws Exception {
		getverifyModel().onAutoRedBlue(getverifyui().getSelectedPane());
		onquery();
	}

	public void onButtonClicked(int iBtnIdx) throws Exception {
		try {
			getverifyui().stopEdit();
			switch (iBtnIdx) {
			case VerifyButtonKey.KEY_QUERY:
				this.showMsg(NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000064")/*

																												 * @res "���ڲ�ѯ.."
																												 */);
				//�������ã����л�ҵ������ͬ����
				getdetaildlg().getconditionPane().setMonBeginData();
				getdetaildlg().getconditionPane().getEndDate().setValueObj(GlWorkBench.getBusiDate());

				getdetaildlg().showModal();
				this.showMsg(NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000065")/*
																												 * @res "��ѯ����"
																												 */);
				break;
			case VerifyButtonKey.KEY_AUTOVERIFY:
				m_ibutton = VerifyButtonKey.KEY_AUTOVERIFY;
				getVerifyStandardDlg().setStatus(VerifyButtonKey.KEY_AUTOVERIFY);
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000230")/*
																											 * @res "�Զ�����"
																											 */);
				getVerifyStandardDlg().showModal();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000326")/*
																											 * @res "�������"
																											 */);
				break;
			case VerifyButtonKey.KEY_PRINTHISTORY_TEMPLET:
				onPrintHistory(false);
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000044")/*
																											 * @res "��ӡ"
																											 */);
				break;
			case VerifyButtonKey.KEY_PRINTHISTORY_DIRECT:
				onPrintHistory(true);
				break;
			case VerifyButtonKey.KEY_HISTORYQRY:
				onSwitchState(false);
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000266")/*
																											 * @res "��ʷ��ѯ"
																											 */);
				break;
			case VerifyButtonKey.KEY_REDBLUE:
				onRedBlue();
				break;
			case VerifyButtonKey.KEY_UNVERIFY:
				if (MessageDialog.showOkCancelDlg(this, NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000294")/*
																																		 * @res "�Ƿ�ȷ�Ϸ�������"
																																		 */, NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000295")/*
																									 * @res "�Ƿ񷴺�����"
																									 */) == MessageDialog.ID_OK) {
					onUnVerify();
				}
				break;
			case VerifyButtonKey.KEY_VERIFY:
				onVerify();
				break;
			case VerifyButtonKey.KEY_QUERYHISTORY:
				this.showMsg(NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000064")/*
																												 * @res "���ڲ�ѯ.."
																												 */);
				getHistoryDlg().getHistoryCondPane().resatBusDate();
				getHistoryDlg().showModal();
				this.showMsg(NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000065")/*
																												 * @res "��ѯ����"
																												 */);
				break;
			case VerifyButtonKey.KEY_SWITCH:
				getverifyui().switchForm();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000268")/*
																											 * @res "�л�"
																											 */);
				break;
			case VerifyButtonKey.KEY_VOUCHER: /* ����ƾ֤ */
				showVoucher();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000267")/*
																											 * @res "����ƾ֤"
																											 */);
				break;
			case VerifyButtonKey.KEY_SUM: /* �л�������ϸ״̬ */
				if (getverifyModel().getSelectedLogs() == null) {
					nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this, "", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002GL502","UPP2002GL502-000108")/*@res "��������ѡ��һ����¼��"*/);
					return;
				}
				getverifyModel().setHistoryState(!getverifyModel().getHistoryState());
				if (getverifyModel().getHistoryState()) {
					m_parent.updateButton(VerifyButtonKey.STATE_HIS_SUM);
					this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000041")/*
																												 * @res "����"
																												 */);
				} else {
					m_parent.updateButton(VerifyButtonKey.STATE_HIS_DETAIL);
					this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000271")/*
																												 * @res "��ϸ"
																												 */);
				}
				break;
			case VerifyButtonKey.KEY_RETURN:
				onSwitchState(true);
				break;
			case VerifyButtonKey.KEY_PRINT:
				getverifyui().print();
				break;
			case VerifyButtonKey.KEY_REFRESH:
				onquery();
				break;
			case VerifyButtonKey.KEY_HISTORYSELECTALL:
				getverifyModel().onHistorySelectAll();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000201")/*
																											 * @res "ȫѡ"
																											 */);
				break;
			case VerifyButtonKey.KEY_HISTORYSELECTNONE:
				getverifyModel().onHistorySelectNone();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000202")/*
																											 * @res "ȫ��"
																											 */);
				break;
			case VerifyButtonKey.KEY_QUERYALL:
				onQueryALL();
				break;
			case VerifyButtonKey.KEY_SELECTALL:
				onSelectALL();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000201")/*
																											 * @res "ȫѡ"
																											 */);
				break;
			case VerifyButtonKey.KEY_SELECTNONE:
				onSelectNONE();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000202")/*
																											 * @res "ȫ��"
																											 */);
				break;
			case VerifyButtonKey.KEY_AUTOREDBLUE:
				m_ibutton = VerifyButtonKey.KEY_AUTOREDBLUE;
				getVerifyStandardDlg().setStatus(VerifyButtonKey.KEY_AUTOREDBLUE);
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000265")/*
																											 * @res "�Զ������Գ�"
																											 */);
				getVerifyStandardDlg().showModal();
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000326")/*
																											 * @res "�������"
																											 */);
				break;
			case VerifyButtonKey.KEY_FETCH:
				getverifyModel().onFetch();
				m_parent.updateButton(VerifyButtonKey.STATE_FETCH);
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000269")/*
																											 * @res "����"
																											 */);
				break;
			case VerifyButtonKey.KEY_CANCELFETCH:
				getverifyModel().onCancelFetch();
				m_parent.updateButton(VerifyButtonKey.STATE_CANCELFETCH);
				this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000279")/*
																											 * @res "ȡ������"
																											 */);
				break;

			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * ���ܣ���ʷ��ѯ��ӡ ���ߣ����� ����ʱ�䣺(2003-5-13 10:57:36) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void onPrintHistory(boolean bDirect) {
		getHistoryPane().print(bDirect);
	}

	/**
	 * ���ܣ�ִ�в�ѯ ���ߣ����� ����ʱ�䣺(2003-5-12 12:07:53) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void onquery() throws Exception {
		getverifyModel().onQuery(getConditionPane().getFilterVO());
		try {
			if (getConditionPane().getType() == nc.vo.glrp.pub.VerifyMsg.TYPE_TIMELY) {
				getverifyui().selectData(getverifyModel().getVerifyDetailvo());
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

		try{
			String pk_accountingbook = getConditionPane().getFilterVO().getCreditCond().getPk_glorgbook();
			String acccode = getConditionPane().getFilterVO().getCreditCond().getSubjCode();
			if (acccode!=null) {
				getverifyui().getlblAccountingbookValue().setText(new AccountBookUtil().getAccountingBookNameByPk(pk_accountingbook));
				getverifyui().getlblVerifyObjValue().setText(AccountCache.getInstance().getAccountVOByCode(pk_accountingbook, acccode).getName());
			}
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
		}

		getverifyui().updateUI();
		if (getverifyModel().getData(true) == null && getverifyModel().getData(false) == null) {
			m_parent.updateButton(VerifyButtonKey.STATE_NODATA);
		} else if (getverifyModel().getData(true) != null && getverifyModel().getData(false) != null) {
			m_parent.updateButton(VerifyButtonKey.STATE_HASDATA);
		} else {
			if (!getverifyModel().isFetch()) {
				m_parent.updateButton(VerifyButtonKey.STATE_HASSINGLEDATA);
			}
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-8-25 16:32:27) ���ߣ����� ���ܣ� ����˵����
	 */
	private void onQueryALL() throws Exception {
		// ������û�а취����conditionpane��ֵ

		nc.vo.glrp.verify.FilterCondVO l_filterVO = getConditionPane().getFilterVO();

		// ��ѯ�跽�ĺ�������--���밴getSubjCode--��Ϊsubjpk���ܶ�Ϊ��
		if (l_filterVO.getDebitCond().getPk_glorgbook() != null || (l_filterVO.getDebitCond().getSubjCode() != null && l_filterVO.getDebitCond().getSubjCode().length() > 0) || (l_filterVO.getDebitCond().getSubjPk() != null && l_filterVO.getDebitCond().getSubjPk().length() > 0)) {
			l_filterVO.getDebitCond().setBeginDate(null);
			// ��ȡ��������
			l_filterVO.getDebitCond().setEndDate(this.getverifyModel().getVerifyDetailvo().getPrepareddate().toString());
			l_filterVO.getDebitCond().setMnyBegin(null);
			l_filterVO.getDebitCond().setMnyEnd(null);
			l_filterVO.getDebitCond().sethasTally(true);

			// 2007-09-18 GBH
			/*
			 * ���������ϸ����ʱ�����˳����и�������ͬ�ļ�¼���������������óɸ������㲻�ϸ����ʱ�����˳��ú����������еĽ��������¼��
			 */
			if (l_filterVO.getDebitCond().getSubjPk() != null && l_filterVO.getDebitCond().getSubjPk().length() > 0) {
				try {
					VerifyObjVO vob = GLPubProxy.getRemoteVerifyobj().findBySubjPk(l_filterVO.getDebitCond().getSubjPk());
					VerifyObjHeaderVO hvo = (VerifyObjHeaderVO) vob.getParentVO();
					if (hvo.getBcontrol().booleanValue()) {
						VerifyObjItemVO[] items = (VerifyObjItemVO[]) vob.getChildrenVO();
						HashMap<String, String> map = new HashMap<String, String>();
						if (items != null) {
							for (int i = 0; i < items.length; i++) {
								map.put(items[i].getPk_subjass(), items[i].getPk_subjass());
							}
						}
						Vector<AssVO> vecass = new Vector<AssVO>();
						AssVO[] ass = this.getverifyModel().getVerifyDetailvo().getAss();
						if (ass != null) {
							for (int i = 0; i < ass.length; i++) {
								AssVO assvo = (AssVO) ass[i].clone();
								if (map.get(assvo.getPk_Checktype()) == null)
									assvo.setPk_Checkvalue(null);
								vecass.addElement(assvo);
							}
						}
						if (vecass.size() > 0) {
							AssVO[] tass = new AssVO[vecass.size()];
							vecass.copyInto(tass);
							l_filterVO.getCreditCond().setAssvos(tass);
						} else
							l_filterVO.getCreditCond().setAssvos(null);
					} else {
						l_filterVO.getDebitCond().setAssvos(null);
					}
				} catch (Exception e) {
				}
			}
		} else {
			l_filterVO.getCreditCond().setBeginDate(null);
			// ��ȡ��������
			l_filterVO.getCreditCond().setEndDate(this.getverifyModel().getVerifyDetailvo().getPrepareddate().toString());
			l_filterVO.getCreditCond().setMnyBegin(null);
			l_filterVO.getCreditCond().setMnyEnd(null);
			l_filterVO.getCreditCond().sethasTally(true);

			// 2007-09-18 GBH
			/*
			 * ���������ϸ����ʱ�����˳����и�������ͬ�ļ�¼���������������óɸ������㲻�ϸ����ʱ�����˳��ú����������еĽ��������¼��
			 */
			if (l_filterVO.getCreditCond().getSubjPk() != null && l_filterVO.getCreditCond().getSubjPk().length() > 0) {
				try {
					VerifyObjVO vob = GLPubProxy.getRemoteVerifyobj().findBySubjPk(l_filterVO.getCreditCond().getSubjPk());
					VerifyObjHeaderVO hvo = (VerifyObjHeaderVO) vob.getParentVO();
					if (hvo.getBcontrol().booleanValue()) {
						VerifyObjItemVO[] items = (VerifyObjItemVO[]) vob.getChildrenVO();
						HashMap<String, String> map = new HashMap<String, String>();
						if (items != null) {
							for (int i = 0; i < items.length; i++) {
								map.put(items[i].getPk_subjass(), items[i].getPk_subjass());
							}
						}
						Vector<AssVO> vecass = new Vector<AssVO>();
						AssVO[] ass = this.getverifyModel().getVerifyDetailvo().getAss();
						if (ass != null) {
							for (int i = 0; i < ass.length; i++) {
								AssVO assvo = (AssVO) ass[i].clone();
								if (map.get(assvo.getPk_Checktype()) == null)
									assvo.setPk_Checkvalue(null);
								vecass.addElement(assvo);
							}
						}
						if (vecass.size() > 0) {
							AssVO[] tass = new AssVO[vecass.size()];
							vecass.copyInto(tass);
							l_filterVO.getCreditCond().setAssvos(tass);
						} else
							l_filterVO.getCreditCond().setAssvos(null);
					} else {
						l_filterVO.getCreditCond().setAssvos(null);
					}
				} catch (Exception e) {
				}
			}
		}
		getverifyModel().onQuery(l_filterVO);

		try {
			if (getConditionPane().getType() == nc.vo.glrp.pub.VerifyMsg.TYPE_TIMELY) {
				getverifyui().selectData(getverifyModel().getVerifyDetailvo());
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		getverifyui().updateUI();
		if (getverifyModel().getData(true) == null && getverifyModel().getData(false) == null) {
			m_parent.updateButton(VerifyButtonKey.STATE_NODATA);
		} else if (getverifyModel().getData(true) != null && getverifyModel().getData(false) != null) {
			m_parent.updateButton(VerifyButtonKey.STATE_HASDATA);
		} else {
			m_parent.updateButton(VerifyButtonKey.STATE_HASSINGLEDATA);
		}

	}

	/**
	 * ���ܣ���ʷ��ѯ ���ߣ����� ����ʱ�䣺(2003-5-12 12:50:13) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void onQueryHisRecord() throws Exception {
		LogFilterCondVO condVO = getHistoryDlg().getQueryCond();
		getverifyModel().setPk_accountingbook(condVO.getPk_glorgbook());
		getverifyModel().onQueryHisRecord(condVO);
		if (getverifyModel().getHistroyDataCount() == 0) {
			m_parent.updateButton(VerifyButtonKey.STATE_HIS_NODATA);
		} else if (getverifyModel().getHistoryState()) {
			m_parent.updateButton(VerifyButtonKey.STATE_HIS_SUM);
		} else {
			m_parent.updateButton(VerifyButtonKey.STATE_HIS_SUM);
		}
	}

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-12 12:52:10) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void onRedBlue() throws Exception {
		getverifyui().stopEdit();
		try {
			getverifyModel().onRedBlue(false);
		} catch (nc.vo.pub.BusinessException ex) {
			if (MessageDialog.showOkCancelDlg(this, NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000296")/*
																																	 * @res "�Ƿ�ǿ�ƺ���"
																																	 */, ex.getMessage()) == MessageDialog.ID_OK) {
				getverifyModel().onRedBlue(true);
			} else {
				return;
			}
		}
		onquery();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-03-30 16:32:27) ���ߣ�lwg ���ܣ� ����˵���� version:v30
	 */
	private void onSelectALL() throws Exception {
		getverifyModel().onSelectAll_2();
		getverifyui().updateUI();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-03-30 16:32:27) ���ߣ�lwg ���ܣ� ����˵���� version:v30
	 */
	private void onSelectNONE() throws Exception {
		getverifyModel().onSelectNone_2();
		getverifyui().updateUI();
	}

	/**
	 * ���ܣ�ת������״̬ ���ߣ����� ����ʱ�䣺(2003-5-13 10:59:34) ������<|> ����ֵ�� �㷨��
	 *
	 * @param bQuery
	 *            boolean
	 */
	private void onSwitchState(boolean bQuery) {
		getHistoryPane().setVisible(!bQuery);
		getverifyui().setVisible(bQuery);
		if (bQuery) {
			add(getverifyui(), "Center");
			m_parent.updateTitles(getverifyui().getTitleName());
			try {
				onquery();
			} catch (Exception e) {

			}
			if (getverifyModel().getData(true) == null && getverifyModel().getData(false) == null) {
				m_parent.updateButton(VerifyButtonKey.STATE_NODATA);
			} else if (getverifyModel().getData(true) == null || getverifyModel().getData(false) == null) {
				m_parent.updateButton(VerifyButtonKey.STATE_HASSINGLEDATA);
			} else {
				m_parent.updateButton(VerifyButtonKey.STATE_HASDATA);
			}
		} else {
			add(getHistoryPane(), "Center");
			m_parent.updateTitles(getHistoryPane().getTitleName());
			try {
				if (getverifyModel().getHistroyDataCount() == 0) {
					m_parent.updateButton(VerifyButtonKey.STATE_HIS_NODATA);
				} else {
					if (getverifyModel().getHistoryState()) {
						m_parent.updateButton(VerifyButtonKey.STATE_HIS_SUM);
					} else {
						m_parent.updateButton(VerifyButtonKey.STATE_HIS_DETAIL);
					}
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				m_parent.updateButton(VerifyButtonKey.STATE_HIS_NODATA);
			}
		}
	}

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-12 15:05:18) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void onUnVerify() throws Exception {
		getverifyModel().unVerify();
		onQueryHisRecord();
	}

	/**
	 * ���ܣ����� ���ߣ����� ����ʱ�䣺(2003-5-12 12:52:21) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void onVerify() throws Exception {
		try {
			getverifyui().stopEdit();
			try {
				getverifyModel().onVerify(false);
			} catch (nc.vo.pub.BusinessException ex) {
				if (MessageDialog.showOkCancelDlg(this, NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000296")/*
																																		 * @res "�Ƿ�ǿ�ƺ���"
																																		 */, ex.getMessage()) == MessageDialog.ID_OK) {
					getverifyModel().onVerify(true);
				} else {
					return;
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
		onquery();
		this.showMsg(NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000326")/*
																									 * @res "�������"
																									 */);
	}

	/**
	 * ���ܣ���ʱ�����������ݣ������ǰƾ֤��¼���ܲμӺ������׳��쳣�� ���ߣ����� ����ʱ�䣺(2003-5-7 16:32:55) ������<|> ����ֵ�� �㷨��
	 *
	 * @param detailvo
	 *            nc.vo.gl.pubvoucher.DetailVO
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public void queryData(nc.vo.gl.pubvoucher.DetailVO detailvo) throws Exception {
		if (detailvo == null) {
			throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_CANNTVERIFY());
		}
		try {
			getConditionPane().setType(nc.vo.glrp.pub.VerifyMsg.TYPE_TIMELY);
			if (getverifyModel().isDataLegal(getverifyModel().getVerifyDetailByDetailpk(detailvo.getPk_detail()), getverifyModel().getVerifyObject(detailvo.getPk_accasoa()))) {
				nc.vo.glrp.verify.FilterCondVO voCond = getConditionPane().getFilterVO(getverifyModel().getVerifyDetailvo());
				getverifyModel().onQuery(voCond);
				getverifyui().selectData(getverifyModel().getVerifyDetailvo());
			} else {
				throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_CANNTVERIFY());
			}

			if (getverifyModel().getData(true) == null && getverifyModel().getData(false) == null) {
				m_parent.updateButton(VerifyButtonKey.STATE_NODATA);
			} else if (getverifyModel().getData(true) != null && getverifyModel().getData(false) != null) {
				m_parent.updateButton(VerifyButtonKey.STATE_HASDATA);
			} else {
				m_parent.updateButton(VerifyButtonKey.STATE_HASSINGLEDATA);
			}

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}


	/**
	 * ���ܣ���ʱ�����������ݣ������ǰƾ֤��¼���ܲμӺ������׳��쳣�� ���ߣ����� ����ʱ�䣺(2003-5-7 16:32:55) ������<|> ����ֵ�� �㷨��
	 *
	 * @param detailvo
	 *            nc.vo.gl.pubvoucher.DetailVO
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public void queryDataOnServer(nc.vo.gl.pubvoucher.DetailVO detailvo) throws Exception {
		if (detailvo == null||detailvo.getPk_accasoa()==null) {
			throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_CANNTVERIFY());
		}
		try {
			getConditionPane().setType(nc.vo.glrp.pub.VerifyMsg.TYPE_TIMELY);
			// hurh 2011-04-25
			getdetaildlg().getAccountingbookRefPane().setEnabled(false);
			getdetaildlg().getAccountingbookRefPane().setPk_accountingbook(detailvo.getPk_accountingbook());
			getverifyui().getlblAccountingbookValue().setText(new AccountBookUtil().getAccountingBookNameByPk(detailvo.getPk_accountingbook()));
			getverifyui().getlblVerifyObjValue().setText(AccountCache.getInstance().getAccountVOByPK(detailvo.getPk_accountingbook(), detailvo.getPk_accasoa(), detailvo.getPrepareddate().toStdString()).getName());
			QryCondPane qryCondPane = (QryCondPane)getConditionPane();
			qryCondPane.setPk_accountingbook(detailvo.getPk_accountingbook());
			GLRemoteCallContext rslt = GlRemoteCallProxy.callProxy(CacheRequestFactory.rtVerify(detailvo,qryCondPane.getFilterVOEx()));
			if(rslt.getBizzData() == null)
				throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_CANNTVERIFY());
			Object[] r = (Object[])rslt.getBizzData();

			((GlVerifyModel)getverifyModel()).setVerifyDetailVO((VerifyDetailVO)r[0]);
			((GlVerifyModel)getverifyModel()).setVerifyObject((VerifyObjVO)r[1]);

			if (getverifyModel().isDataLegal((VerifyDetailVO)r[0], (VerifyObjVO)r[1])) {
			     getConditionPane().getFilterVO(getverifyModel().getVerifyDetailvo());
			     GlVerifyModel glVerifyModel = (GlVerifyModel)getverifyModel();
			     glVerifyModel.setPk_accountingbook(detailvo.getPk_accountingbook());
			     glVerifyModel.setRslt((FilterCondVO)r[2],(nc.vo.glrp.verify.VerifyDetailVO[][])r[3]);
				getverifyui().selectData(getverifyModel().getVerifyDetailvo());
			} else {
				throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_CANNTVERIFY());
			}

			if (getverifyModel().getData(true) == null && getverifyModel().getData(false) == null) {
				m_parent.updateButton(VerifyButtonKey.STATE_NODATA);
			} else if (getverifyModel().getData(true) != null && getverifyModel().getData(false) != null) {
				m_parent.updateButton(VerifyButtonKey.STATE_HASDATA);
			} else {
				m_parent.updateButton(VerifyButtonKey.STATE_HASSINGLEDATA);
			}

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	public void setIParent(nc.ui.glpub.IParent parent) {
		i_parent = parent;
	}

	/**
	 * ���ܣ����ø����� ���ߣ����� ����ʱ�䣺(2003-5-12 11:42:32) ������<|> ����ֵ�� �㷨��
	 *
	 * @param parent
	 *            nc.ui.glrp.verify.IVerifyConsumer
	 */
	public void setparent(IVerifyConsumer parent) {
		m_parent = parent;
	}

	/**
	 * ���ܣ����÷��Ϊ��ʱ���������º���� ���ߣ����� ����ʱ�䣺(2003-5-7 16:28:19) ������<|> ����ֵ�� �㷨��
	 *
	 * @param iStyle
	 *            int
	 */
	public void setStyle(int iStyle) {
		getverifyui().setStyle(iStyle);
		getConditionPane().setType(iStyle);
	}

	/**
	 * ���ܣ���ʾ������Ϣ ���ߣ����� ����ʱ�䣺(2003-5-8 16:59:42) ������<|> ����ֵ�� �㷨��
	 *
	 * @param sMsg
	 *            java.lang.String
	 */
	public void showErrorMsg(String sMsg) throws Exception {
		m_parent.showErrorMsg(sMsg);
	}

	/**
	 * ���ܣ���ʾ��Ϣ ���ߣ����� ����ʱ�䣺(2003-5-10 11:33:52) ������<|> ����ֵ�� �㷨��
	 *
	 * @param sMsg
	 *            java.lang.String
	 */
	public void showMsg(String sMsg) {
		m_parent.showMsg(sMsg);
	}

	/**
	 * ���ܣ�����ƾ֤ ���ߣ����� ����ʱ�䣺(2003-5-13 11:09:19) ������<|> ����ֵ�� �㷨��
	 *
	 */
	private void showVoucher() throws Exception {
		String voucherid = null;
		try {
			voucherid = getverifyui().getCurrVouchId();
			if (voucherid == null || voucherid.trim().length() == 0) {
				showErrorMsg(VerifyMsg.getMSG_CANNTSHOWVOUCHER());
				return;
			}
		} catch (nc.vo.pub.BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			showErrorMsg(ex.getMessage());
			return;
		}
		if (m_VoucherBridge == null)
			m_VoucherBridge = (nc.ui.gl.pubvoucher.VoucherBridge) i_parent.showNext("nc.ui.gl.pubvoucher.VoucherBridge", new Integer[] { new Integer(2) });
		else
			i_parent.showNext(m_VoucherBridge);
		try {
			m_VoucherBridge.setVoucherPk(voucherid);
		} catch (Exception err) {
			showErrorMsg(err.getMessage());
			throw err;
		}
	}

	/*
	 * ���� Javadoc��
	 *
	 * @see nc.ui.glrp.verify.IVerifyView#showSuccessMsg(java.lang.String)
	 */
	public void showSuccessMsg(String sMsg) {
		// TODO �Զ����ɷ������
		m_parent.showSuccessMsg(sMsg);
	}

}