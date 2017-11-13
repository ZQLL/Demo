package nc.ui.gl.voucherlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.AbstractFunclet;
import nc.funcnode.ui.FuncletInitData;
import nc.itf.gl.voucher.IVoucher;
import nc.pubitf.accperiod.AccountCalendar;
import nc.ui.gl.cachefeed.CacheRequestFactory;
import nc.ui.gl.common.NCHoteKeyRegistCenter;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.pubvoucher.ListModel;
import nc.ui.gl.pubvoucher.VoucherToftPanel;
import nc.ui.gl.remotecall.GlRemoteCallProxy;
import nc.ui.gl.voucher.IPara;
import nc.ui.gl.voucher.VoucherChangeListener;
import nc.ui.gl.voucher.dlg.BalancePeriodDialog;
import nc.ui.gl.voucher.dlg.ProgressWindow;
import nc.ui.gl.voucher.dlg.VoucherPrintDialog;
import nc.ui.gl.voucher.reg.VoucherFunctionRegister;
import nc.ui.gl.vouchercard.VoucherModel;
import nc.ui.gl.vouchertools.VoucherDataCenter;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.SeparatorButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.querytemplate.queryarea.IQueryExecutor;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.trade.excelimport.DataImporter;
import nc.ui.trade.excelimport.ExcelImportInfo;
import nc.ui.trade.excelimport.ExcelImporter;
import nc.ui.trade.excelimport.ImportProgressDlg;
import nc.ui.uif2.IFuncNodeInitDataListener;
import nc.ui.uif2.actions.DefaultQueryDelegator;
import nc.ui.uif2.actions.IQueryDelegator;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.gateway60.itfs.CalendarUtilGL;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.gl.mutex.MutexStatusVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.gl.voucherlist.VoucherIndexVO;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.querytemplate.queryscheme.SimpleQuerySchemeVO;

import org.apache.commons.lang.StringUtils;
import org.flexdock.util.SwingUtility;

/**
 * ƾ֤�б������ͼ�� �������ڣ�(2001-8-18 11:29:19)
 * 
 * @author��������
 */

@SuppressWarnings("deprecation")
public class ListView extends ToftPanel implements
		java.beans.PropertyChangeListener, IPara, IUiPanel, IListView,
		VoucherChangeListener, ILinkMaintain, IFuncNodeInitDataListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2094036938701766435L;

	protected ListModel m_ListModel = null;

	public IParent m_parent;

	private boolean m_isEditable = true;

	private boolean linkQueryFlag = false;

	private boolean linkAddFlag = false;// ����������־λ add by zhaoyangm 2013-06-13

	public boolean isLinkAddFlag() {
		return linkAddFlag;
	}

	public void setLinkAddFlag(boolean linkAddFlag) {
		this.linkAddFlag = linkAddFlag;
	}

	private ButtonObject[] m_arryCurrentButtons;

	private SeparatorButtonObject m_nullButton = new SeparatorButtonObject();

	private ButtonObject m_qryButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000242")/*
																		 * @res
																		 * "��ѯ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000242")/*
										 * @res "��ѯ"
										 */, 2, "��ѯ"); /* -=notranslate=- */

	private ButtonObject m_gotoButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000005")/*
																		 * @res
																		 * "��λ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000005")/*
										 * @res "��λ"
										 */, 2, "��λ"); /* -=notranslate=- */

	private ButtonObject m_addButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000039")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000039")/*
										 * @res "����"
										 */, 2, "����"); /* -=notranslate=- */

	private ButtonObject m_copyButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000003")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000003")/*
										 * @res "����"
										 */, 2, "����"); /* -=notranslate=- */

	private ButtonObject m_modButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000037")/*
																		 * @res
																		 * "�޸�"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000037")/*
										 * @res "�޸�"
										 */, 2, "�޸�"); /* -=notranslate=- */

	private ButtonObject m_saveButton = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55",
					"UC001-0000001")/* @res "����" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55",
					"UC001-0000001")/* @res "����" */, 2, "����"); /*
																 * -=notranslate=
																 * -
																 */

	// private ButtonObject m_viewButton = new
	// ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
	// "UPP20021005-000038")/*
	// * @res
	// * "���"
	// */, nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
	// "UPP20021005-000038")/*
	// * @res
	// * "���"
	// */, 2, "���"); /*-=notranslate=-*/

	private ButtonObject m_viewButton = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002GL502",
					"UCH021")/*
							 * @res "��Ƭ��ʾ"
							 */, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2002GL502", "UCH021")/*
													 * @res "��Ƭ��ʾ"
													 */, 2, "��Ƭ��ʾ"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_disButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000014")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000014")/*
										 * @res "����"
										 */, 2, "����"); /* -=notranslate=- */
	private ButtonObject m_unAbandoButton = new ButtonObject("ȡ������", "ȡ������", 2,
			"ȡ������"); /* -=notranslate=- */

	private ButtonObject m_delButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000013")/*
																		 * @res
																		 * "ɾ��"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000013")/*
										 * @res "ɾ��"
										 */, 2, "ɾ��"); /* -=notranslate=- */

	private ButtonObject m_refButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000477")/*
																		 * @res
																		 * "ˢ��"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000477")/*
										 * @res "ˢ��"
										 */, 2, "ˢ��"); /* -=notranslate=- */

	protected ButtonObject m_printButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000012")/*
																		 * @res
																		 * "��ӡ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000012")/*
										 * @res "��ӡ"
										 */, 2, "��ӡ"); /* -=notranslate=- */

	private ButtonObject m_printlistButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000500")/*
										 * @res "��ӡ�б�"
										 */, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20021005", "UPP20021005-000500")/*
																 * @res "��ӡ�б�"
																 */, 2, "��ӡ�б�"); /*
																				 * -=
																				 * notranslate
																				 * =
																				 * -
																				 */

	private ButtonObject m_printvoucherButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000501")/*
										 * @res "��ӡƾ֤"
										 */, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20021005", "UPP20021005-000501")/*
																 * @res "��ӡƾ֤"
																 */, 2, "��ӡƾ֤"); /*
																				 * -=
																				 * notranslate
																				 * =
																				 * -
																				 */

	private ButtonObject m_printlistButton2 = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000502")/*
										 * @res "��ӡ�嵥"
										 */, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20021005", "UPP20021005-000502")/*
																 * @res "��ӡ�嵥"
																 */, 2, "��ӡ�嵥"); /*
																				 * -=
																				 * notranslate
																				 * =
																				 * -
																				 */

	private ButtonObject m_outputButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000025")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000025")/*
										 * @res "����"
										 */, 2, "����"); /* -=notranslate=- */

	private ButtonObject m_retButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000020")/*
																		 * @res
																		 * "����"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000020")/*
										 * @res "����"
										 */, 2, "����"); /* -=notranslate=- */

	private ButtonObject m_breaknoButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000503")/*
																		 * @res
																		 * "�պŲ�ѯ"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000503")/*
										 * @res "�պŲ�ѯ"
										 */, 2, "�պŲ�ѯ"); /* -=notranslate=- */

	private ButtonObject m_balvoucher = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000504")/*
																		 * @res
																		 * "����ƾ֤"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000504")/*
										 * @res "����ƾ֤"
										 */, 2, "����ƾ֤"); /* -=notranslate=- */
	private ButtonObject m_testImportAction = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0061")/* @res "����" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0061")/* @res "����" */, 2,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0061")/* @res "����" */);
	private ButtonObject m_testExportAction = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0243")/* @res "����excel" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0243"), 2, nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("glpub_0", "02002003-0243"));
	private ButtonObject importAndExport = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0244")/* @res "���뵼��" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0244")/* @res "���뵼��" */, 2,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0244")/* @res "���뵼��" */);

	protected IUiPanel m_cardView;

	protected ListUi ivjListUi = null;

	private nc.ui.gl.voucherquery.QueryConditionDialog m_QueryDialog = null;

	private OutputDialog ivjOutputDialog = null;

	private VoucherPrintDialog m_printDialog;

	private QueryBreakNoDialog m_breaknodialog;

	private HashMap buttonFunctions = new HashMap();

	private String fipsavebtn;

	private boolean canEdit = true;

	/**
	 * MainPanel ������ע�⡣
	 */
	public ListView() {
		super();
		// ProgressWindow p = ProgressWindow.showRunningProgressWindow(this);
		initialize();
		// p.dispose();
	}

	public void addListener(Object objListener, Object objUserdata) {
		return;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-28 15:54:24)
	 * 
	 * @param button
	 *            nc.ui.pub.ButtonObject
	 * @param vo
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 */
	public void adjustButtonState(ButtonObject button, VoucherVO vo) {
		if (button.getName().equals(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000003")/*
											 * @res "����"
											 */)) {
			button.setVisible(false);
			if (vo.getPk_casher() != null || vo.getPk_checked() != null
					|| vo.getPk_manager() != null)
				button.setEnabled(false);
			if (VoucherDataCenter.isVoucherSelfEditDelete(vo
					.getPk_accountingbook())
					&& !vo.getPk_prepared().equals(GlWorkBench.getLoginUser()))
				button.setEnabled(false);
		} else if (button.getName().equals(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000014")/*
											 * @res "����"
											 */)
				|| button.getName().equals(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000505")/*
													 * @res "ȡ������"
													 */)) {
			if (vo.getPk_casher() != null
					|| vo.getPk_checked() != null
					|| vo.getPk_manager() != null
					|| (vo.getIsmatched() != null && vo.getIsmatched()
							.booleanValue())
					|| (vo.getErrmessage() != null && !vo.getErrmessage()
							.equals(""))
					|| (vo.getTempsaveflag() != null && vo.getTempsaveflag()
							.booleanValue()))
				button.setEnabled(false);
			if (vo.getDiscardflag() != null
					&& vo.getDiscardflag().booleanValue()) {
				if (!vo.getPk_prepared().equals(GlWorkBench.getLoginUser()))
					button.setEnabled(false);
				else if (VoucherDataCenter.getGLSettlePeriod(vo
						.getPk_accountingbook()) != null
						&& (vo.getYear().compareTo(
								VoucherDataCenter.getGLSettlePeriod(vo
										.getPk_accountingbook())[0]) < 0 || (vo
								.getYear().compareTo(
										VoucherDataCenter.getGLSettlePeriod(vo
												.getPk_accountingbook())[0]) == 0 && vo
								.getPeriod().compareTo(
										VoucherDataCenter.getGLSettlePeriod(vo
												.getPk_accountingbook())[1]) <= 0))
						&& vo.getVoucherkind().intValue() != 1)
					button.setEnabled(false);

				// button.setCode(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
				// "UPP20021005-000505"));
				button.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000505")/*
														 * @res "ȡ������"
														 */);
				button.setTag(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000505")/*
														 * @res "ȡ������"
														 */);
				button.setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000505"));

				// button.setHotKey(NCHoteKeyRegistCenter.HOTKEY_UNABANDONACTION.toString());
				// button.setModifiers(NCHoteKeyRegistCenter.MASK_UNABANDONACTION);
				// button.setHint(button.getHint()+"(Alt+J)");
				// button.setCode(m_disButton.getCode());
				// button.getAction().putValue(Action.SHORT_DESCRIPTION,
				// button.getName() + "Alt+J");
				button.setHint(button.getHint() + "(Alt+K)");

				updateButtons();

			} else {
				if (VoucherDataCenter.isVoucherSelfEditDelete(vo
						.getPk_accountingbook())
						&& !vo.getPk_prepared().equals(
								GlWorkBench.getLoginUser()))
					button.setEnabled(false);
				// button.setCode(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
				// "UPP20021005-000014"));
				button.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000014")/*
														 * @res "����"
														 */);
				button.setTag(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000014")/*
														 * @res "����"
														 */);
				button.setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000014"));

				// button.setHotKey(NCHoteKeyRegistCenter.HOTKEY_ABANDONACTION.toString());
				// button.setModifiers(NCHoteKeyRegistCenter.MASK_ABANDONACTION);
				// button.setCode(m_unAbandoButton.getCode());
				button.setHint(button.getHint() + "(Alt+K)");

				updateButtons();
			}
		} else if (button.getName().equals(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000013")/*
											 * @res "ɾ��"
											 */)) {
			if (VoucherDataCenter.isVoucherSelfEditDelete(vo
					.getPk_accountingbook())
					&& !vo.getPk_prepared().equals(GlWorkBench.getLoginUser()))
				button.setEnabled(false);
			if (vo.getPk_casher() != null
					|| vo.getPk_checked() != null
					|| (vo.getIsmatched() != null && vo.getIsmatched()
							.booleanValue()) || vo.getPk_manager() != null)
				button.setEnabled(false);
		} else if (button.getName().equals(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000012")/*
											 * @res "��ӡ"
											 */)) {
			button.setHint(button.getName());
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-6 18:20:02)
	 * 
	 * @param bns
	 *            nc.ui.pub.ButtonObject[]
	 */
	protected void catButtonTag(ButtonObject[] bns) {
		if (bns == null)
			return;
		for (int i = 0; i < bns.length; i++) {
			bns[i].setTag(bns[i].getName());
			if (bns[i].getChildButtonGroup() != null)
				catButtonTag(bns[i].getChildButtonGroup());
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-26 15:29:00)
	 */
	public void enterVoucher() {
		try {
			openVoucher();
			// if (getListModel().getCurrent() == null)
			// return;
			// showNext();
			// String strFunctionName;
			// try {
			// strFunctionName = this.getParameter("functionname");
			// } catch (Exception e) {
			// strFunctionName = "voucherbridge";
			// }
			// if (strFunctionName == null)
			// strFunctionName = "voucherbridge";
			// if (strFunctionName.equals("preparevoucher") ||
			// strFunctionName.equals("voucherbridge")) {
			// if (getListModel().getCurrent() != null &&
			// getListModel().getCurrent() instanceof VoucherVO && ((VoucherVO)
			// getListModel().getCurrent()).getPk_voucher() != null) {
			// m_cardView.invoke(((VoucherVO)
			// getListModel().getCurrent()).getPk_voucher(),
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
			// "UPP20021005-000037")/*
			// * @res
			// * "�޸�"
			// */);
			//
			// } else {
			// m_cardView.invoke(getListModel().getCurrent(),
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
			// "UPP20021005-000037")/*
			// * @res
			// * "�޸�"
			// */);
			// }
			// new VoucherOperatLogTool().InsertLogByVoucher((VoucherVO)
			// getListModel().getCurrent(),
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
			// "UPP20021005-000037")/*
			// * @res
			// * "�޸�"
			// */, getModuleCode());
			// } else
			// m_cardView.invoke(getListModel().getCurrent(),
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
			// "UPP20021005-000038")/*
			// * @res
			// * "���"
			// */);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			// throw new
			// nc.vo.gateway60.pub.GlBusinessException(e.getMessage());
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
					this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100557",
							"UPP2002100557-000072")/*
													 * @res "����"
													 */, e.getMessage());
		}
	}

	public void openVoucher() throws BusinessException {
		String strFunctionName = null;
		try {
			strFunctionName = this.getParameter("functionname");
			if (strFunctionName == null)
				strFunctionName = this.getParameter("functionname");
		} catch (Exception e) {
			strFunctionName = "voucherbridge";
		}
		if (strFunctionName == null)
			strFunctionName = "voucherbridge";
		if (getListModel().getCurrentIndexs() == null
				|| getListModel().getCurrentIndexs().length == 0
				|| getListModel().m_VoucherLists == null
				|| getListModel().m_VoucherLists.size() == 0)
			return;
		VoucherIndexVO temp = (VoucherIndexVO) getListModel().m_VoucherLists
				.elementAt(getListModel().getCurrentIndexs()[0]);
		if (temp.getVoucherVO() != null
				&& temp.getVoucherVO().getPk_voucher() != null
				&& temp.getVoucherVO().getNumDetails() == 0) {
			VoucherVO voucher = (VoucherVO) GlRemoteCallProxy.callProxy(
					CacheRequestFactory.openVoucher(temp, getFuncletContext()
							.getFuncCode())).getBizzData();
			if (voucher == null) {
				String voucherNo = " ";
				if (temp.getVoucherVO().getNo() != null) {
					voucherNo = String.valueOf(temp.getVoucherVO().getNo());
				}
				nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
						this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"2002100557", "UPP2002100557-000072")/*
																	 * @res "����"
																	 */,
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"glpub_0", "02002003-0245", null,
								new String[] { voucherNo })/*
															 * @res
															 * "{0} ��ƾ֤ �Ѿ������ڻ� �ѱ�����ɾ��!"
															 */);
				return;
			}

			temp.setVoucherVO(voucher);
		}
		showNext();
		((VoucherToftPanel) m_cardView).getVoucherPanel().getVoucherUI()
				.setEditable(isEditable());
		((VoucherToftPanel) m_cardView).openVoucher(
				temp.getVoucherVO(),
				strFunctionName.equals("preparevoucher")
						|| strFunctionName.equals("voucherbridge"));
		if (!this.isCanEdit()) {
			((VoucherToftPanel) m_cardView).disableEdit();
		}
		if (isLinkQueryFlag()) {
			((VoucherToftPanel) m_cardView).setLinkButtons();
		}

		if (isLinkAddFlag()) {
			((VoucherToftPanel) m_cardView).setLinkAddButtons();
		}
	}

	/**
	 * 
	 * @version (00-6-8 16:17:27)
	 * 
	 * @return ButtonObject[]
	 */
	public ButtonObject[] getButtons() {
		if (isLinkQueryFlag()) {
			return getLinkButtons();
		}

		String strFunctionName;
		try {
			strFunctionName = this.getParameter("functionname");
		} catch (Exception e) {
			strFunctionName = "voucherbridge";
		}
		if (m_arryCurrentButtons != null)
			return m_arryCurrentButtons;
		if (strFunctionName == null)
			strFunctionName = "voucherbridge";
		if (strFunctionName.equals("preparevoucher")
				&& !StringUtils.isEmpty(fipsavebtn)
				&& fipsavebtn.equals("fipsavebtn")) {
			m_arryCurrentButtons = new ButtonObject[] { m_modButton, /*
																	 * m_retButton,
																	 */
					m_saveButton };
		} else if (strFunctionName.equals("preparevoucher"))
			m_arryCurrentButtons = new ButtonObject[] { m_addButton, /*
																	 * m_copyButton,
																	 */
					m_modButton, m_delButton, getNullButton(), m_qryButton,
					m_refButton, getNullButton(), m_disButton, m_breaknoButton,
					getNullButton(), importAndExport, m_printButton };//
		else if (strFunctionName.equals("voucherbridge")
				|| strFunctionName.equals("")) {
			if (fipsavebtn == null) {
				m_arryCurrentButtons = new ButtonObject[] { m_modButton, /*
																		 * m_retButton
																		 * ,
																		 */};
			} else if (fipsavebtn.equals("fipsavebtn")) {
				m_arryCurrentButtons = new ButtonObject[] { m_modButton,
						m_retButton, m_saveButton };
			}
		} else if (strFunctionName.equals("queryvoucher"))
			m_arryCurrentButtons = new ButtonObject[] { m_qryButton,
					m_refButton, getNullButton(), m_viewButton,
					getNullButton(), m_printButton };
		else if (strFunctionName.equals("copyvoucher"))
			m_arryCurrentButtons = new ButtonObject[] { m_qryButton,
					m_viewButton, m_refButton };
		catButtonTag(m_arryCurrentButtons);
		initButtonState(m_arryCurrentButtons);
		m_testImportAction.setEnabled(true);
		m_testExportAction.setEnabled(true);
		importAndExport.setEnabled(true);
		return m_arryCurrentButtons;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-7-15 9:16:05)
	 * 
	 * @return java.lang.String
	 */
	public String getFunctionname() {
		String strFunctionName;
		try {
			strFunctionName = this.getParameter("functionname");
		} catch (Exception e) {
			strFunctionName = "voucherbridge";
		}
		if (strFunctionName == null || strFunctionName.trim().equals(""))
			strFunctionName = "voucherbridge";
		return strFunctionName;
	}

	protected ListModel getListModel() {
		if (m_ListModel == null) {
			m_ListModel = new ListModel((IPara) this);
			m_ListModel.addPropertyChangeListener(getListUi());
			// hurh
			m_ListModel.addPropertyChangeListener(this);
		}
		return m_ListModel;
	}

	/**
	 * ���� ListUi ����ֵ��
	 * 
	 * @return nc.ui.gl.voucherlist.ListUi
	 */
	/* ���棺�˷������������ɡ� */
	protected ListUi getListUi() {
		if (ivjListUi == null) {
			try {
				ivjListUi = new nc.ui.gl.voucherlist.ListUi(this);

				ivjListUi.setName("ListUi");
				ivjListUi.setBounds(6, 8, 759, 400);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListUi;
	}

	/**
	 * ���� OutputDialog ����ֵ��
	 * 
	 * @return nc.ui.gl.voucherquery.VoucherQueryCondition
	 */
	/* ���棺�˷������������ɡ� */
	private OutputDialog getOutputDialog() {
		if (ivjOutputDialog == null) {
			try {
				ivjOutputDialog = new OutputDialog(this);
				ivjOutputDialog.setName("OutputDialog");
				ivjOutputDialog
						.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjOutputDialog;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-6-17 8:48:33)
	 * 
	 * @return nc.ui.gl.voucher.VoucherPrintDialog
	 */
	public VoucherPrintDialog getPrintDialog() {
		if (m_printDialog == null) {
			m_printDialog = new VoucherPrintDialog(this);
		}
		return m_printDialog;
	}

	/**
	 * ���� OutputDialog ����ֵ��
	 * 
	 * @return nc.ui.gl.voucherquery.VoucherQueryCondition
	 */
	/* ���棺�˷������������ɡ� */
	private QueryBreakNoDialog getQuerybreaknoDialog() {
		if (m_breaknodialog == null) {
			try {
				m_breaknodialog = new QueryBreakNoDialog(this);
				// VoucherVO vo = (VoucherVO) getListModel().getCurrent();
				// if(null != vo)
				// m_breaknodialog.setPk_accountingbook(vo.getPk_accountingbook());
				m_breaknodialog.setModal(false);
				m_breaknodialog
						.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
				// throw ivjExc;
			}
		}
		VoucherVO vo;
		try {
			vo = (VoucherVO) getListModel().getCurrent();
			if (null != vo)
				m_breaknodialog.setPk_accountingbook(vo.getPk_accountingbook());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return m_breaknodialog;
	}

	/**
	 * ���� VoucherQueryCondition1 ����ֵ��
	 * 
	 * @return nc.ui.gl.voucherquery.VoucherQueryCondition
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.gl.voucherquery.QueryConditionDialog getQueryDialog() {
		if (m_QueryDialog == null) {
			try {

				m_QueryDialog = new nc.ui.gl.voucherquery.QueryConditionDialog(
						this);
				m_QueryDialog.setModulecode(this.getModuleCode());
				m_QueryDialog.setPk_orgbook(VoucherDataCenter
						.getClientPk_orgbook(), GlWorkBench.getBusiDate()
						.toStdString());
				m_QueryDialog.setPk_user(GlWorkBench.getLoginUser());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		} else {
			// m_QueryDialog.set
		}
		return m_QueryDialog;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
				"UPP20021005-000506")/*
									 * @res "ƾ֤�б�"
									 */;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		Logger.error(exception.getMessage(), exception);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-27 9:48:39)
	 */
	public void initButtonState(ButtonObject[] buttons) {
		if (buttons == null)
			return;
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000005")/*
												 * @res "��λ"
												 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000003")/*
																	 * @res "����"
																	 */))
				buttons[i].setVisible(false);
			if (buttons[i].getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000477")/*
												 * @res "ˢ��"
												 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000242")/*
																	 * @res "��ѯ"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000039")/*
																	 * @res "����"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000025")/*
																	 * @res "����"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000503")/*
																	 * @res
																	 * "�պŲ�ѯ"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000020")/*
																	 * @res "����"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000504")/*
																	 * @res
																	 * "����ƾ֤"
																	 */)
					|| buttons[i]
							.getName()
							.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("glpub_0", "02002003-0061")/*
																			 * @res
																			 * "����"
																			 */)
					|| buttons[i]
							.getName()
							.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("glpub_0", "02002003-0244")/*
																			 * @res
																			 * "���뵼��"
																			 */))
				buttons[i].setEnabled(true);
			else
				buttons[i].setEnabled(false);
		}
		updateButtons();
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("MainPanel");
			setLayout(new java.awt.BorderLayout());
			setSize(774, 419);
			add(getListUi(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getListUi().addListViewListener(this);
		getListUi().setListmodel(getListModel());
		getListModel().addVoucherChangeListener(this);
		getListModel().setListUI(getListUi());
		m_printButton.addChildButton(m_printlistButton);
		m_printButton.addChildButton(m_printvoucherButton);
		m_printButton.addChildButton(m_printlistButton2);

		importAndExport.addChildButton(m_outputButton);
		importAndExport.addChildButton(m_testImportAction);
		importAndExport.addChildButton(m_testExportAction);
		installButtonFunction(m_printvoucherButton,
				VoucherFunctionRegister.FUNCTION_PRINTVOUCHER);

		// initQuerydlg();
		// user code end
	}

	private void initQuerydlg() {
		try {
			GlRemoteCallProxy.callProxyAsyn(CacheRequestFactory.newContextVO());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this,
					"", e1.getMessage());
			Logger.error(e1);
		}
		Thread newthread = new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Logger.error(e.getMessage(), e);
				}
				// try {
				// new
				// GlPeriodForClient().getPeriod(VoucherDataCenter.getClientPk_orgbook(),
				// ClientEnvironment.getInstance().getBusinessDate());
				// } catch (GLBusinessException e) {
				// // TODO Auto-generated catch block
				// Logger.error(e.getMessage(), e);
				// }
				getQueryDialog();
			}
		};
		newthread.start();
	}

	public void installButtonFunction(ButtonObject button, String functioncode) {
		getListModel().installOperationModel(functioncode,
				VoucherFunctionRegister.getOperationClass(functioncode));
		buttonFunctions.put(button, functioncode);
	}

	public Object invoke(Object objData, Object objUserData) {

		try {
			if (objUserData == null
					|| (objUserData != null && objUserData.toString().equals(
							"FIPBATCHSAVE"))
					|| (objUserData != null && objUserData.toString().equals(
							"addMessageListener"))) {
				fipsavebtn = "fipsavebtn";
				m_arryCurrentButtons = null;
				ButtonObject array[] = getButtons();
				for (int i = 0; array != null && i < array.length; i++) {
					NCHoteKeyRegistCenter.getAction(this, array[i]);
				}
				setButtons(getButtons());

				VoucherVO voucher = (VoucherVO) getListModel().getCurrent();
				if (voucher != null) {
					setButtonState(voucher);
				}

				return null;
			}
			getListModel().setFuncCode(
					(((AbstractFunclet) m_parent).getFuncCode()));
			return getListModel().invoke(objData, objUserData);
		} catch (Exception e) {
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this,
					null, e.getMessage());
			return null;
		}
	}

	public void nextClosed() {
		nc.bs.logging.Logger.debug("Next Closed,What I can do for it?");
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {
		ProgressWindow p = null;
		try {
			Object kkey = buttonFunctions.get(bo);
			if (kkey != null) {
				getListModel().doOperation(kkey);
				return;
			}
			if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000242")/*
												 * @res "��ѯ"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000064")/*
															 * @res "���ڲ�ѯ.."
															 */);
				if (showQueryDialog()) {
					// p =
					// ProgressWindow.showRunningProgressWindow(ListView.this);
					if (getQueryDialog().isAddQuery())
						getListModel().addQueryByVO(
								getQueryDialog().getConditionVO());
					else
						getListModel().QueryByVO(
								getQueryDialog().getConditionVO());
					// p.dispose();
				}
				// hurh����ʾ��ѯ����󣬲���ʾ������Ϣ
				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555",
				// "UPP2002100555-000065")/*
				// * @res
				// * "��ѯ����"
				// */);

			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000005")/*
												 * @res "��λ"
												 */)) {
				;
			} else if (bo.getName().equals(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2002GL502", "UCH021")/*
												 * @res "��Ƭ��ʾ"
												 */)) {
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				if (getListModel().getCurrent() == null)
					return;
				if (getListModel().getCurrentIndexs() != null
						&& getListModel().getCurrentIndexs().length != 1)
					return;
				showNext();
				m_cardView.invoke(
						getListModel().getCurrent(),
						nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000038")/*
													 * @res "���"
													 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000039")/*
												 * @res "����"
												 */)) {
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				fetchNewVoucherNo();
				showNext();
				m_cardView.invoke(null, nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000039")/*
																	 * @res "����"
																	 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"common", "UC001-0000001")/* @res "����" */)) {

				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				int[] indexs = getListModel().getCurrentIndexs();
				List<String> pk_voucher_list = new ArrayList<String>();
				String pk_user = WorkbenchEnvironment.getInstance()
						.getLoginUser().getPrimaryKey();

				Map<Integer, VoucherVO> voucherVOMap = new HashMap<Integer, VoucherVO>();

				for (int i : indexs) {
					VoucherVO voucherVOTemp = getListModel().getVoucherVO(i);
					voucherVOTemp.clearDetail();
					voucherVOTemp.setPk_prepared(pk_user);

					voucherVOMap.put(Integer.valueOf(i), voucherVOTemp);

				}
				Map<Integer, String> batchSaveMap = NCLocator.getInstance()
						.lookup(IVoucher.class).batchSave(voucherVOMap, true);

				if (batchSaveMap != null && batchSaveMap.size() > 0) {
					for (Integer row : batchSaveMap.keySet()) {
						String pk_voucher = batchSaveMap.get(row);
						((VoucherIndexVO) getListModel().m_VoucherLists
								.elementAt(row)).setPk_voucher(pk_voucher);
						pk_voucher_list.add(pk_voucher);
					}
				}

				getListModel().removeVOByPks(
						pk_voucher_list.toArray(new String[0]));

				showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("2002gl55", "UCH005")/* @res "����ɹ�" */);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000003")/*
												 * @res "����"
												 */)) {
				;
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000037")/*
												 * @res "�޸�"
												 */)) {
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				enterVoucher();
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000014")/*
												 * @res "����"
												 */)
					|| bo.getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000505")/*
																	 * @res
																	 * "ȡ������"
																	 */)) {
				if (bo.getName().equals(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000014")/*
													 * @res "����"
													 */)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000066")/*
																			 * @res
																			 * "��������.."
																			 */);
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000067")/*
																			 * @res
																			 * "����ȡ������.."
																			 */);
				}
				String[] powerpks = getListModel().abandonCurrent(bo.getName());
				if (powerpks != null) {
					nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
							this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"2002100557", "UPP2002100557-000067")/*
																		 * @res
																		 * "��ʾ"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"2002100557", "UPP2002100557-000073")/*
																		 * @res
																		 * "��ѡ��ƾ֤����һ����û�б����ϣ�ԭ������Щƾ֤����һЩ��Ŀû��Ȩ�ޡ�"
																		 */);
				}
				if (bo.getName().equals(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000014")/*
													 * @res "����"
													 */)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000068")/*
																			 * @res
																			 * "���Ͻ���"
																			 */);
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000069")/*
																			 * @res
																			 * "ȡ�����Ͻ���"
																			 */);

				}

			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000013")/*
												 * @res "ɾ��"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000070")/*
															 * @res "����ɾ��.."
															 */);
				if (verifyDelete()) {
					String[] powerpks = getListModel().delCurrent();
					if (powerpks != null)
						nc.vo.fipub.utils.uif2.FiUif2MsgUtil
								.showUif2DetailMessage(
										this,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("2002100557",
														"UPP2002100557-000067")/*
																				 * @
																				 * res
																				 * "��ʾ"
																				 */,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("2002100557",
														"UPP2002100557-000074")/*
																				 * @
																				 * res
																				 * "��ѡ��ƾ֤����һ����û�б�ɾ����ԭ������Щƾ֤����һЩ��Ŀû��Ȩ�ޡ�"
																				 */);
				}
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000071")/*
															 * @res "ɾ������"
															 */);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000477")/*
												 * @res "ˢ��"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000072")/*
															 * @res "����ˢ��.."
															 */);
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				refresh();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000042")/*
															 * @res "ˢ�����"
															 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000477")/*
												 * @res "ˢ��"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000072")/*
															 * @res "����ˢ��.."
															 */);
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				refresh();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000042")/*
															 * @res "ˢ�����"
															 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000012")/*
												 * @res "��ӡ"
												 */)
					&& !getFunctionname().equals("preparevoucher")) {
				getListModel().print(0);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000500")/*
												 * @res "��ӡ�б�"
												 */)) {
				getListModel().print(0);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000501")/*
												 * @res "��ӡƾ֤"
												 */)) {
				if (getPrintDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					getListModel().setPrintSubjLevel(
							getPrintDialog().getSubjLevel());
					getListModel().setSumAss(getPrintDialog().isSumAss());
					getListModel().print(1);
				}
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000502")/*
												 * @res "��ӡ�嵥"
												 */)) {
				getListModel().print(2);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000020")/*
												 * @res "����"
												 */)) {
				if (m_parent != null)
					m_parent.closeMe();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000025")/*
												 * @res "����"
												 */)) {
				if (showOutputDialog())
					getListModel().outputVouchers(this,
							getOutputDialog().getSaveFilePath(),
							getOutputDialog().getSaveGround(),
							getOutputDialog().getSelectionMode());
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000503")/*
												 * @res "�պŲ�ѯ"
												 */)) {
				if (getQuerybreaknoDialog() != null)
					getQuerybreaknoDialog().showModal();

			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000504")/*
												 * @res "����ƾ֤"
												 */)) {
				BalancePeriodDialog dlg = new BalancePeriodDialog(this);
				// dlg.setYear(VoucherDataCenter.getClientYear());
				if (dlg.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					showNext();
					VoucherVO vo = new VoucherVO();
					vo.setPk_org(GlWorkBench.getDefaultOrg()/*
															 * nc.ui.pub.
															 * ClientEnvironment
															 * .getInstance().
															 * getCorporation
															 * ().getPk_corp()
															 */);
					// vo.setCorpname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getName());
					vo.setPk_system("GL");
					vo.setPrepareddate(GlWorkBench.getBusiDate()/*
																 * nc.ui.pub.
																 * ClientEnvironment
																 * .
																 * getInstance()
																 * .getDate()
																 */);
					vo.setPeriod(VoucherDataCenter.getClientPeriod());
					vo.setNo(new Integer(0));
					vo.setModifyflag("YYY");
					vo.setAttachment(new Integer(0));
					vo.setTotalcredit(new nc.vo.pub.lang.UFDouble(0));
					vo.setTotaldebit(new nc.vo.pub.lang.UFDouble(0));
					vo.setTotalcreditglobal(UFDouble.ZERO_DBL);
					vo.setTotaldebitglobal(UFDouble.ZERO_DBL);
					vo.setTotalcreditgroup(UFDouble.ZERO_DBL);
					vo.setTotaldebitgroup(UFDouble.ZERO_DBL);

					vo.setYear(VoucherDataCenter.getClientYear());
					vo.setPk_prepared(GlWorkBench.getLoginUser()/*
																 * nc.ui.pub.
																 * ClientEnvironment
																 * .
																 * getInstance()
																 * .getUser().
																 * getPrimaryKey
																 * ()
																 */);
					// vo.setPreparedname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUser_name());
					vo.setVoucherkind(new Integer(0));
					vo.setDiscardflag(nc.vo.pub.lang.UFBoolean.FALSE);
					vo.setDetailmodflag(nc.vo.pub.lang.UFBoolean.TRUE);
					vo.setVoucherkind(new Integer(1));
					if (dlg.getBalancePeriod() != null) {
						vo.setM_adjustperiod(dlg.getBalancePeriod());
						vo.setPeriod(dlg.getEndPeriod());
						try {
							AccountCalendar calendar = CalendarUtilGL
									.getAccountCalendarByAccountBook(vo
											.getPk_accountingbook());
							calendar.set(vo.getYear(), vo.getPeriod());
							AccperiodmonthVO month = calendar.getMonthVO();

							vo.setPrepareddate(month.getEnddate());
							// nc.vo.bd.period.AccperiodVO yearvo =
							// nc.ui.bd.period.AccperiodBO_Client.queryByYear(vo.getYear());
							//
							// if (yearvo != null)
							// {
							// if (yearvo.getVosMonth() != null)
							// for (int i = 0; i < yearvo.getVosMonth().length;
							// i++)
							// {
							// if
							// (yearvo.getVosMonth()[i].getMonth().equals(vo.getPeriod()))
							// {
							// vo.setPrepareddate(yearvo.getVosMonth()[i].getEnddate());
							// break;
							// }
							// }
							// }
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new GlBusinessException(e.getMessage());
						}
					}
					java.util.Vector vecdetails = new java.util.Vector();
					// vecdetails.addElement(new DetailVO());
					vo.setDetail(vecdetails);
					m_cardView.invoke(vo, nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("20021005", "UPP20021005-000038")/*
																		 * @res
																		 * "���"
																		 */);
					// p.dispose();
				}
			} else if (bo.getName().equals(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"glpub_0", "02002003-0061")/* @res "����" */)) {
				final ExcelImporter i = new ExcelImporter();
				// �������Ӳ�����...
				final ExcelImportInfo importInfo = i
						.importFromExcel(this, null);
				if (importInfo == null)
					return;
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						EditorAgent editorAgent = new EditorAgent(
								ListView.this, importInfo);
						DataImporter importer = new GlDataImporter(editorAgent,
								importInfo, i.getLogFileName());
						final ImportProgressDlg dlg = new ImportProgressDlg(
								ListView.this, importer);
						dlg.setModal(false);
						SwingUtility.centerOnScreen(dlg);
						dlg.setVisible(true);
						dlg.start();
						dlg.setFunclet(ListView.this);
					}
				});
			} else if (bo.getName().equals(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"glpub_0", "02002003-0243")/* @res "����excel" */)) {
				final ExcelImporter ei = new VoucherExcelImporter();
				EditorAgent exportEditor = new EditorAgent(ListView.this, null);
				ei.exportToExcel(exportEditor);
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
					this,
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2002gl55", "UPP2002gl55-000344")/* @res "����" */,
					e.getMessage());
			// nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this,null,e.getMessage());
		} finally {
			if (p != null) {
				p.dispose();
			}
		}
		this.updateUI();
	}

	private void fetchNewVoucherNo() throws BusinessException {
		GlRemoteCallProxy.callProxy(CacheRequestFactory
				.newFetchVoucherNoContextVO(getModuleCode()));
	}

	public boolean onClosing() {
		try {
			nc.ui.gl.vouchertools.VoucherDataCenter.getInstance().clearAll();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * This method gets called when a bound property is changed.
	 * 
	 * @param evt
	 *            A PropertyChangeEvent object describing the event source and
	 *            the property that has changed.
	 */
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		// hurh
		if (evt.getPropertyName().equals("message")) {
			showHintMessage(evt.getNewValue() == null ? "" : evt.getNewValue()
					.toString());
		}

		// try
		// {
		// if (evt.getPropertyName().equals("CurrentIndex"))
		// {
		// Integer iTemp = (Integer) evt.getNewValue();
		// if (getListModel().getValue(VoucherIndexKey.V_CHECKD) == null)
		// {

		// }
		// }
		// }
		// catch (Exception e)
		// {
		// nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(this,null,e.getMessage());
		// }
	}

	protected void refresh() {
		try {
			if (!StringUtils.isEmpty(getQueryDialog().getPk_accountingbook())) {
				getListModel().refresh();
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public void removeListener(Object objListener, Object objUserdata) {
		return;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-27 9:48:39)
	 */
	public void setButtonState(VoucherVO vo) {
		ButtonObject[] buttons = getButtons();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setEnabled(true);
			adjustButtonState(buttons[i], vo);
		}
		updateButtons();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-8-15 11:16:24)
	 * 
	 * @param p_obj
	 *            java.lang.Object
	 */
	public void showMe(IParent parent) {
		parent.getUiManager().removeAll();
		parent.getUiManager().add(this, this.getName());
		m_parent = parent;
		if (parent instanceof ToftPanel) {
			// ToftPanel new_name = (ToftPanel) parent;
			// if
			// (!getClientEnvironment().getCorporation().getPk_corp().equals("0001"))
			// {
			// String orgbookname = ((AccountingBookVO)
			// ClientEnvironment.getInstance().getValue(ClientEnvironment.GLORGBOOKPK)).getName();
			// //60x //new_name.//updateStatusBarAccountMsg(orgbookname);
			// }

		}
		// if(parent != null)
		// setFrame(parent.getFrame());
		init(((ToftPanel) parent.getUiManager()).getFuncletContext());
		if (m_cardView == null)
			initButtonState(getButtons());// add by zhaoyangm
											// �򿪽ڵ�ʱ��ʼ����ť״̬���絼�뵼����ť��û�����ݵ�������û�
	}

	protected IUiPanel showNext() {
		if (m_cardView == null) {
			m_cardView = (IUiPanel) m_parent
					.showNext("nc.ui.gl.pubvoucher.VoucherToftPanel");
			setEditable(isEditable());
		} else {
			m_parent.showNext(m_cardView);
		}

		m_cardView.addListener(getListModel(), "IPeerListener");
		if (!this.isCanEdit())
			((nc.ui.gl.pubvoucher.VoucherToftPanel) m_cardView).disableEdit();
		return m_cardView;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-12-9 18:57:40)
	 */
	public boolean showOutputDialog() {
		return getOutputDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-12-9 18:57:40)
	 */
	public boolean showQueryDialog() {
		return getQueryDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-12-26 16:56:01)
	 */
	public void updateButtons() {
		if (m_parent != null)
			m_parent.updateButtons();
	}

	private boolean verifyDelete() throws Exception {
		int iResult = MessageDialog.showYesNoDlg(
				this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555",
						"UPP2002100555-000073")/*
												 * @res "ѯ��"
												 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000111")/*
											 * @res "���Ҫɾ����ƾ֤��"
											 */, MessageDialog.ID_NO);
		// showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
		// "UPP20021005-000111")/* @res "���Ҫɾ����ƾ֤��" */);
		return iResult == 4;
	}

	/**
	 * This method gets called when a bound property is changed.
	 * 
	 * @param evt
	 *            A PropertyChangeEvent object describing the event source and
	 *            the property that has changed.
	 */
	public void voucherChange(nc.ui.gl.voucher.VoucherChangeEvent evt) {
		if (evt.getNewValue() == null)
			initButtonState(getButtons());
		else
			setButtonState((VoucherVO) evt.getNewValue());
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.pub.ToftPanel#getClientEnvironment()
	 */
	// public ClientEnvironment getClientEnvironment() {
	// // TODO �Զ����ɷ������
	// return super.getClientEnvironment();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.ui.pub.linkoperate.ILinkMaintain#doMaintainAction(nc.ui.pub.linkoperate
	 * .ILinkMaintainData)
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		// TODO Auto-generated method stub
		String voucherid = maintaindata.getBillID();
		if (voucherid != null) {
			IVoucher iv = (IVoucher) NCLocator.getInstance().lookup(
					IVoucher.class.getName());
			try {
				getListModel().addVoucher(iv.queryByPk(voucherid));
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				Logger.error(e.getMessage(), e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.error(e.getMessage(), e);
			}
			getListModel().setCurrentIndexByPKs(new String[] { voucherid },
					true);

		}

		enterVoucher();
	}

	protected void setButtons(ButtonObject[] btns) {
		for (int i = 0; btns != null && i < btns.length; i++) {
			NCHoteKeyRegistCenter.getAction(this, btns[i]);
		}
		super.setButtons(btns);
	}

	public void setLinkButtons() {
		setLinkQueryFlag(true);
		setButtons(getLinkButtons());
	}

	private ButtonObject[] getLinkButtons() {
		m_printButton.addChildButton(m_printlistButton);
		m_printButton.addChildButton(m_printvoucherButton);
		m_printButton.addChildButton(m_printlistButton2);

		importAndExport.addChildButton(m_outputButton);
		// importAndExport.addChildButton(m_testImportAction);
		importAndExport.addChildButton(m_testExportAction);

		return new ButtonObject[] { m_printButton, importAndExport };
	}

	public void setButtonArray(ButtonObject[] btns) {
		this.setButtons(btns);
	}

	/**
	 * ʹ�ý���ı༭��ť���ɲ���
	 */
	public void enableEdit() {
		ButtonObject[] buttons = this.getButtons();
		ArrayList<String> captions = new ArrayList<String>();
		captions.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC001-0000108")/* @res "����" */);
		captions.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC001-0000045")/* @res "�޸�" */);
		captions.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC001-0000039")/* @res "ɾ��" */);
		for (int i = 0; i < buttons.length; i++) {
			if (captions.contains(buttons[i].getName()))
				buttons[i].setEnabled(false);
		}
		this.setButtons(buttons);
	}

	@Override
	public void initData(FuncletInitData data) {
		if (data != null) {
			Object initData = data.getInitData();
			if (initData != null && initData instanceof SimpleQuerySchemeVO) {
				initQuerySchemeData((SimpleQuerySchemeVO) initData);
			} else {
				super.initData(data);
			}
		}
	}

	/**
	 * ��ݷ�ʽ ��ѯ������ʼ������
	 * 
	 * @param schemeVo
	 */
	private void initQuerySchemeData(SimpleQuerySchemeVO schemeVo) {
		IQueryDelegator queryDelegator = getListUi().getQueryDelegator();
		IQueryExecutor queryExecutor = getListUi().getQueryExecutor();
		QueryConditionDLG qcd = ((DefaultQueryDelegator) queryDelegator)
				.getQueryDlg();
		if (qcd != null) {
			try {
				qcd.initUIData();
				IQueryScheme scheme = qcd.getQuerySchemeByPK(schemeVo
						.getPrimaryKey());
				queryExecutor.doQuery(scheme);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		} else {
			Logger.warn(NCLangRes.getInstance().getStrByID("uif2",
					"QueryAction-000001")/*
										 * QueryActionû��ʹ��DefaultQueryDelegator,
										 * Ҳû����дgetQueryConditionDLG
										 * ()�������ʲ���ʵ�ּ򵥷�����ѯ������
										 */);
		}
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
		this.enableEdit();

	}

	public void setEditable(boolean m_isEditable) {
		this.m_isEditable = m_isEditable;
		if (m_cardView != null) {
			((VoucherModel) ((VoucherToftPanel) m_cardView).getVoucherPanel()
					.getMasterModel()).setListViewEditable(m_isEditable);
		}
	}

	public boolean isEditable() {
		return m_isEditable;
	}

	public void setLinkQueryFlag(boolean linkQueryFlag) {
		this.linkQueryFlag = linkQueryFlag;
	}

	public boolean isLinkQueryFlag() {
		return linkQueryFlag;
	}

	public SeparatorButtonObject getNullButton() {
		m_nullButton.setName("");
		m_nullButton.setCode("");
		return m_nullButton;
	}

	/**
	 * addBy shaoguo.wang �򿪽ڵ�ǰ�����ж�
	 */
	public String checkPrerequisite() {
		try {
			MutexStatusVO ms = GLPubProxy.getRemoteIMutexPub()
					.findMutexStatusByCode("GL");
			if (ms != null) {
				enableEditAll();
				return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"2002gl55", "UPP2002gl55-000307")/*
														 * @res
														 * "�ڵ㴦�ڻ���״̬��������ʹ�ã�"
														 */;
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			return e.getMessage();
		}
		return null;
	}

	/**
	 * ʹ�ý�������а�ť���ɲ���
	 */
	public void enableEditAll() {
		ButtonObject[] buttons = this.getButtons();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setEnabled(false);
		}
		this.setButtons(buttons);
	}

}