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
 * 凭证列表的主视图类 创建日期：(2001-8-18 11:29:19)
 * 
 * @author：王建华
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

	private boolean linkAddFlag = false;// 联查新增标志位 add by zhaoyangm 2013-06-13

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
																		 * "查询"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000242")/*
										 * @res "查询"
										 */, 2, "查询"); /* -=notranslate=- */

	private ButtonObject m_gotoButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000005")/*
																		 * @res
																		 * "定位"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000005")/*
										 * @res "定位"
										 */, 2, "定位"); /* -=notranslate=- */

	private ButtonObject m_addButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000039")/*
																		 * @res
																		 * "增加"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000039")/*
										 * @res "增加"
										 */, 2, "增加"); /* -=notranslate=- */

	private ButtonObject m_copyButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000003")/*
																		 * @res
																		 * "复制"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000003")/*
										 * @res "复制"
										 */, 2, "复制"); /* -=notranslate=- */

	private ButtonObject m_modButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000037")/*
																		 * @res
																		 * "修改"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000037")/*
										 * @res "修改"
										 */, 2, "修改"); /* -=notranslate=- */

	private ButtonObject m_saveButton = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55",
					"UC001-0000001")/* @res "保存" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55",
					"UC001-0000001")/* @res "保存" */, 2, "保存"); /*
																 * -=notranslate=
																 * -
																 */

	// private ButtonObject m_viewButton = new
	// ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
	// "UPP20021005-000038")/*
	// * @res
	// * "浏览"
	// */, nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
	// "UPP20021005-000038")/*
	// * @res
	// * "浏览"
	// */, 2, "浏览"); /*-=notranslate=-*/

	private ButtonObject m_viewButton = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002GL502",
					"UCH021")/*
							 * @res "卡片显示"
							 */, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2002GL502", "UCH021")/*
													 * @res "卡片显示"
													 */, 2, "卡片显示"); /*
																	 * -=notranslate
																	 * =-
																	 */

	private ButtonObject m_disButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000014")/*
																		 * @res
																		 * "作废"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000014")/*
										 * @res "作废"
										 */, 2, "作废"); /* -=notranslate=- */
	private ButtonObject m_unAbandoButton = new ButtonObject("取消作废", "取消作废", 2,
			"取消作废"); /* -=notranslate=- */

	private ButtonObject m_delButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000013")/*
																		 * @res
																		 * "删除"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000013")/*
										 * @res "删除"
										 */, 2, "删除"); /* -=notranslate=- */

	private ButtonObject m_refButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000477")/*
																		 * @res
																		 * "刷新"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000477")/*
										 * @res "刷新"
										 */, 2, "刷新"); /* -=notranslate=- */

	protected ButtonObject m_printButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000012")/*
																		 * @res
																		 * "打印"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000012")/*
										 * @res "打印"
										 */, 2, "打印"); /* -=notranslate=- */

	private ButtonObject m_printlistButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000500")/*
										 * @res "打印列表"
										 */, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20021005", "UPP20021005-000500")/*
																 * @res "打印列表"
																 */, 2, "打印列表"); /*
																				 * -=
																				 * notranslate
																				 * =
																				 * -
																				 */

	private ButtonObject m_printvoucherButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000501")/*
										 * @res "打印凭证"
										 */, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20021005", "UPP20021005-000501")/*
																 * @res "打印凭证"
																 */, 2, "打印凭证"); /*
																				 * -=
																				 * notranslate
																				 * =
																				 * -
																				 */

	private ButtonObject m_printlistButton2 = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000502")/*
										 * @res "打印清单"
										 */, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20021005", "UPP20021005-000502")/*
																 * @res "打印清单"
																 */, 2, "打印清单"); /*
																				 * -=
																				 * notranslate
																				 * =
																				 * -
																				 */

	private ButtonObject m_outputButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000025")/*
																		 * @res
																		 * "导出"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000025")/*
										 * @res "导出"
										 */, 2, "导出"); /* -=notranslate=- */

	private ButtonObject m_retButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000020")/*
																		 * @res
																		 * "返回"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000020")/*
										 * @res "返回"
										 */, 2, "返回"); /* -=notranslate=- */

	private ButtonObject m_breaknoButton = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000503")/*
																		 * @res
																		 * "空号查询"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000503")/*
										 * @res "空号查询"
										 */, 2, "空号查询"); /* -=notranslate=- */

	private ButtonObject m_balvoucher = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("20021005", "UPP20021005-000504")/*
																		 * @res
																		 * "调整凭证"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
					"UPP20021005-000504")/*
										 * @res "调整凭证"
										 */, 2, "调整凭证"); /* -=notranslate=- */
	private ButtonObject m_testImportAction = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0061")/* @res "导入" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0061")/* @res "导入" */, 2,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0061")/* @res "导入" */);
	private ButtonObject m_testExportAction = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0243")/* @res "导出excel" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0243"), 2, nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("glpub_0", "02002003-0243"));
	private ButtonObject importAndExport = new ButtonObject(
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0244")/* @res "导入导出" */,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0244")/* @res "导入导出" */, 2,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
					"02002003-0244")/* @res "导入导出" */);

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
	 * MainPanel 构造子注解。
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
	 * 此处插入方法说明。 创建日期：(2001-11-28 15:54:24)
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
											 * @res "复制"
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
											 * @res "作废"
											 */)
				|| button.getName().equals(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000505")/*
													 * @res "取消作废"
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
														 * @res "取消作废"
														 */);
				button.setTag(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000505")/*
														 * @res "取消作废"
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
														 * @res "作废"
														 */);
				button.setTag(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000014")/*
														 * @res "作废"
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
											 * @res "删除"
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
											 * @res "打印"
											 */)) {
			button.setHint(button.getName());
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-6 18:20:02)
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
	 * 此处插入方法说明。 创建日期：(2001-11-26 15:29:00)
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
			// * "修改"
			// */);
			//
			// } else {
			// m_cardView.invoke(getListModel().getCurrent(),
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
			// "UPP20021005-000037")/*
			// * @res
			// * "修改"
			// */);
			// }
			// new VoucherOperatLogTool().InsertLogByVoucher((VoucherVO)
			// getListModel().getCurrent(),
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
			// "UPP20021005-000037")/*
			// * @res
			// * "修改"
			// */, getModuleCode());
			// } else
			// m_cardView.invoke(getListModel().getCurrent(),
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
			// "UPP20021005-000038")/*
			// * @res
			// * "浏览"
			// */);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			// throw new
			// nc.vo.gateway60.pub.GlBusinessException(e.getMessage());
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
					this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100557",
							"UPP2002100557-000072")/*
													 * @res "出错"
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
																	 * @res "出错"
																	 */,
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"glpub_0", "02002003-0245", null,
								new String[] { voucherNo })/*
															 * @res
															 * "{0} 号凭证 已经不存在或 已被别人删除!"
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
	 * 此处插入方法说明。 创建日期：(2002-7-15 9:16:05)
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
	 * 返回 ListUi 特性值。
	 * 
	 * @return nc.ui.gl.voucherlist.ListUi
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 OutputDialog 特性值。
	 * 
	 * @return nc.ui.gl.voucherquery.VoucherQueryCondition
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 此处插入方法说明。 创建日期：(2002-6-17 8:48:33)
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
	 * 返回 OutputDialog 特性值。
	 * 
	 * @return nc.ui.gl.voucherquery.VoucherQueryCondition
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 VoucherQueryCondition1 特性值。
	 * 
	 * @return nc.ui.gl.voucherquery.VoucherQueryCondition
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
				"UPP20021005-000506")/*
									 * @res "凭证列表"
									 */;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		Logger.error(exception.getMessage(), exception);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-27 9:48:39)
	 */
	public void initButtonState(ButtonObject[] buttons) {
		if (buttons == null)
			return;
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000005")/*
												 * @res "定位"
												 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000003")/*
																	 * @res "复制"
																	 */))
				buttons[i].setVisible(false);
			if (buttons[i].getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000477")/*
												 * @res "刷新"
												 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000242")/*
																	 * @res "查询"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000039")/*
																	 * @res "增加"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000025")/*
																	 * @res "导出"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000503")/*
																	 * @res
																	 * "空号查询"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000020")/*
																	 * @res "返回"
																	 */)
					|| buttons[i].getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000504")/*
																	 * @res
																	 * "调整凭证"
																	 */)
					|| buttons[i]
							.getName()
							.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("glpub_0", "02002003-0061")/*
																			 * @res
																			 * "导入"
																			 */)
					|| buttons[i]
							.getName()
							.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("glpub_0", "02002003-0244")/*
																			 * @res
																			 * "导入导出"
																			 */))
				buttons[i].setEnabled(true);
			else
				buttons[i].setEnabled(false);
		}
		updateButtons();
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 子类实现该方法，响应按钮事件。
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
												 * @res "查询"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000064")/*
															 * @res "正在查询.."
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
				// hurh，显示查询结果后，不显示这行信息
				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555",
				// "UPP2002100555-000065")/*
				// * @res
				// * "查询结束"
				// */);

			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000005")/*
												 * @res "定位"
												 */)) {
				;
			} else if (bo.getName().equals(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2002GL502", "UCH021")/*
												 * @res "卡片显示"
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
													 * @res "浏览"
													 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000039")/*
												 * @res "增加"
												 */)) {
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				fetchNewVoucherNo();
				showNext();
				m_cardView.invoke(null, nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000039")/*
																	 * @res "增加"
																	 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"common", "UC001-0000001")/* @res "保存" */)) {

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
						.getStrByID("2002gl55", "UCH005")/* @res "保存成功" */);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000003")/*
												 * @res "复制"
												 */)) {
				;
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000037")/*
												 * @res "修改"
												 */)) {
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				enterVoucher();
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000014")/*
												 * @res "作废"
												 */)
					|| bo.getName().equals(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20021005", "UPP20021005-000505")/*
																	 * @res
																	 * "取消作废"
																	 */)) {
				if (bo.getName().equals(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000014")/*
													 * @res "作废"
													 */)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000066")/*
																			 * @res
																			 * "正在作废.."
																			 */);
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000067")/*
																			 * @res
																			 * "正在取消作废.."
																			 */);
				}
				String[] powerpks = getListModel().abandonCurrent(bo.getName());
				if (powerpks != null) {
					nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
							this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"2002100557", "UPP2002100557-000067")/*
																		 * @res
																		 * "提示"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"2002100557", "UPP2002100557-000073")/*
																		 * @res
																		 * "所选的凭证中有一部分没有被作废，原因是这些凭证上有一些科目没有权限。"
																		 */);
				}
				if (bo.getName().equals(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000014")/*
													 * @res "作废"
													 */)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000068")/*
																			 * @res
																			 * "作废结束"
																			 */);
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("2002100555", "UPP2002100555-000069")/*
																			 * @res
																			 * "取消作废结束"
																			 */);

				}

			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000013")/*
												 * @res "删除"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000070")/*
															 * @res "正在删除.."
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
																				 * "提示"
																				 */,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("2002100557",
														"UPP2002100557-000074")/*
																				 * @
																				 * res
																				 * "所选的凭证中有一部分没有被删除，原因是这些凭证上有一些科目没有权限。"
																				 */);
				}
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000071")/*
															 * @res "删除结束"
															 */);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000477")/*
												 * @res "刷新"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000072")/*
															 * @res "正在刷新.."
															 */);
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				refresh();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000042")/*
															 * @res "刷新完成"
															 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000477")/*
												 * @res "刷新"
												 */)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000072")/*
															 * @res "正在刷新.."
															 */);
				// p = ProgressWindow.showRunningProgressWindow(ListView.this);
				refresh();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2002100555", "UPP2002100555-000042")/*
															 * @res "刷新完成"
															 */);
				// p.dispose();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000012")/*
												 * @res "打印"
												 */)
					&& !getFunctionname().equals("preparevoucher")) {
				getListModel().print(0);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000500")/*
												 * @res "打印列表"
												 */)) {
				getListModel().print(0);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000501")/*
												 * @res "打印凭证"
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
												 * @res "打印清单"
												 */)) {
				getListModel().print(2);
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000020")/*
												 * @res "返回"
												 */)) {
				if (m_parent != null)
					m_parent.closeMe();
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000025")/*
												 * @res "导出"
												 */)) {
				if (showOutputDialog())
					getListModel().outputVouchers(this,
							getOutputDialog().getSaveFilePath(),
							getOutputDialog().getSaveGround(),
							getOutputDialog().getSelectionMode());
			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000503")/*
												 * @res "空号查询"
												 */)) {
				if (getQuerybreaknoDialog() != null)
					getQuerybreaknoDialog().showModal();

			} else if (bo.getName().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000504")/*
												 * @res "调整凭证"
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
																		 * "浏览"
																		 */);
					// p.dispose();
				}
			} else if (bo.getName().equals(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"glpub_0", "02002003-0061")/* @res "导入" */)) {
				final ExcelImporter i = new ExcelImporter();
				// 后来增加参数了...
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
							"glpub_0", "02002003-0243")/* @res "导出excel" */)) {
				final ExcelImporter ei = new VoucherExcelImporter();
				EditorAgent exportEditor = new EditorAgent(ListView.this, null);
				ei.exportToExcel(exportEditor);
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			nc.vo.fipub.utils.uif2.FiUif2MsgUtil.showUif2DetailMessage(
					this,
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2002gl55", "UPP2002gl55-000344")/* @res "错误" */,
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
	 * 此处插入方法说明。 创建日期：(2001-11-27 9:48:39)
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
	 * 此处插入方法说明。 创建日期：(01-8-15 11:16:24)
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
											// 打开节点时初始化按钮状态，如导入导出按钮在没有数据的情况下置灰
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
	 * 此处插入方法说明。 创建日期：(2001-12-9 18:57:40)
	 */
	public boolean showOutputDialog() {
		return getOutputDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-9 18:57:40)
	 */
	public boolean showQueryDialog() {
		return getQueryDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-26 16:56:01)
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
												 * @res "询问"
												 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000111")/*
											 * @res "真的要删除该凭证吗？"
											 */, MessageDialog.ID_NO);
		// showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021005",
		// "UPP20021005-000111")/* @res "真的要删除该凭证吗？" */);
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
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.pub.ToftPanel#getClientEnvironment()
	 */
	// public ClientEnvironment getClientEnvironment() {
	// // TODO 自动生成方法存根
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
	 * 使该界面的编辑按钮不可操作
	 */
	public void enableEdit() {
		ButtonObject[] buttons = this.getButtons();
		ArrayList<String> captions = new ArrayList<String>();
		captions.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC001-0000108")/* @res "新增" */);
		captions.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC001-0000045")/* @res "修改" */);
		captions.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC001-0000039")/* @res "删除" */);
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
	 * 快捷方式 查询方案初始化数据
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
										 * QueryAction没有使用DefaultQueryDelegator,
										 * 也没有重写getQueryConditionDLG
										 * ()方法，故不能实现简单方案查询操作！
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
	 * addBy shaoguo.wang 打开节点前进行判断
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
														 * "节点处于互斥状态，不可以使用！"
														 */;
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			return e.getMessage();
		}
		return null;
	}

	/**
	 * 使该界面的所有按钮不可操作
	 */
	public void enableEditAll() {
		ButtonObject[] buttons = this.getButtons();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setEnabled(false);
		}
		this.setButtons(buttons);
	}

}