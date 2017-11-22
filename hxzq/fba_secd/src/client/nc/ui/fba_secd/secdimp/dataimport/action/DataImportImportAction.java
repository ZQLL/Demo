package nc.ui.fba_secd.secdimp.dataimport.action;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.fba_secd.secdimp.pub.IDataImportBusiService;
import nc.itf.fba_secd.secdimp.pub.IDataImportQueryService;
import nc.pubitf.setting.defaultdata.OrgSettingAccessor;
import nc.ui.fba_secd.secdimp.dataimport.DataContrastDlg;
import nc.ui.fba_secd.secdimp.dataimport.DataImportMidDataListDlg;
import nc.ui.fba_secd.secdimp.dataimport.model.DataImportAppModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pubapp.uif2app.actions.AddAction;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.fba_secd.secdimp.dataimport.AggDataImportVO;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO1;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO2;
import nc.vo.fba_secd.secdimp.dataimport.DataImportVO;
import nc.vo.fba_secd.secdimp.datasourceset.DataSourceSetVO;
import nc.vo.fba_secd.secdimp.importfileset.ImportFileSetVO;
import nc.vo.fba_secd.secdimp.importproj.ImportProjVO;
import nc.vo.fba_secd.secdimp.pub.DataImportCache;
import nc.vo.fba_secd.secdimp.pub.DataImportMethod;
import nc.vo.fba_secd.secdimp.pub.FieldPropVO;
import nc.vo.fba_secd.secdimp.pub.PubMethod;
import nc.vo.fba_secd.secdimp.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pubapp.AppContext;

public class DataImportImportAction extends AddAction {

	private static final long serialVersionUID = -745837781088023096L;

	private HierachicalDataAppModel treeModel = null;

	nc.ui.uif2.TangramContainer container = null;

	String currdate = AppContext.getInstance().getServerTime().toLocalString();
	String pk_glorgbook = null;
	String dsname = WorkbenchEnvironment.getInstance().getDSName();

	private UIComboBox m_importWay = null;
	private int importWay = 0;
	// private ImportProjVO importProjVO=null;
	private AggDataImportVO aggDataImportVO = null;

	// private ImportFileSetVO filesetvo = null;

	public String getPk_glorgbook() {
		try {
			pk_glorgbook = OrgSettingAccessor.getDefaultAccountingBookID(
					getModel().getContext().getPk_loginUser(), getModel()
							.getContext().getPk_group());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return pk_glorgbook;
	}

	public void setPk_glorgbook(String pk_glorgbook) {
		this.pk_glorgbook = pk_glorgbook;
	}

	public HierachicalDataAppModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;
	}

	public nc.ui.uif2.TangramContainer getContainer() {
		return container;
	}

	public void setContainer(nc.ui.uif2.TangramContainer container) {
		this.container = container;
	}

	public DataImportImportAction() {
		super();
		setCode("DataImportAction");
		setBtnName("导入");

		initVariable();
	}

	private void initVariable() {
		currdate = AppContext.getInstance().getServerTime().toLocalString();
		pk_glorgbook = null;
		dsname = WorkbenchEnvironment.getInstance().getDSName();
	}

	
	@Override
	protected boolean isActionEnable() {
		if (getTreeModel().getSelectedNode() != null
				&& getTreeModel().getSelectedNode().isLeaf()
				&& model.getUiState() == UIState.NOT_EDIT
				&& ((DataImportAppModel) model).getData().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 导入三种方式
	 * 
	 * @return UIComboBox
	 */
	public UIComboBox getImportWay() {
		if (m_importWay == null) {
			try {
				m_importWay = new UIComboBox();
				m_importWay.setName("importWay");
				String[] n = { "不考虑重复，数据全部导入", "考虑重复，增添方式导入", "考虑重复，覆盖方式导入" };
				for (int i = 0; i < n.length; i++) {
					m_importWay.addItem(n[i]);
				}
				// m_importWay.setSelectedIndex(1);
			} catch (java.lang.Throwable ivjExc) {
				Logger.error(ivjExc.getMessage());
			}
		}
		return m_importWay;
	}

	private UIPanel getNormalPanel() {

		StringBuilder field = new StringBuilder();
		// boolean if_repeat = false;
		// 重复字段
		// DataImportAppModel dataModel = (DataImportAppModel) getModel();
		// List aggVOLst = dataModel.getData();
		// if (aggVOLst != null && aggVOLst.size() > 0) {
		// AggDataImportVO aggDataImportVO = (AggDataImportVO) aggVOLst.get(0);
		//
		// DataImportBVO1[] dataImportBVO1=(DataImportBVO1[])
		// aggDataImportVO.getChildren(DataImportBVO1.class);
		// DataImportBVO2[] dataImportBVO2=(DataImportBVO2[])
		// aggDataImportVO.getChildren(DataImportBVO2.class);
		// if(null!=dataImportBVO1&&dataImportBVO1.length>0){
		// int bvo1Len = dataImportBVO1.length;
		// for(int i=0;i<bvo1Len;i++){
		// if_repeat = dataImportBVO1[i].getIf_repeat().booleanValue();
		// if (!if_repeat) {
		// field.append(", ").append(dataImportBVO1[i].getAim_field()).append(" ");//(mei
		// add: 如果中文名称为空,显示字段英文名,否则界面上显示为null)
		// }
		// }
		// }
		// if(null!=dataImportBVO2&&dataImportBVO2.length>0){
		// int bvo1Len = dataImportBVO2.length;
		// for(int i=0;i<bvo1Len;i++){
		// if_repeat = dataImportBVO2[i].getIf_repeat().booleanValue();
		// if (!if_repeat) {
		// field.append(", ").append(dataImportBVO2[i].getAim_field()).append(" ");//(mei
		// add: 如果中文名称为空,显示字段英文名,否则界面上显示为null)
		// }
		// }
		// }
		// }
		if (field.toString().length() > 0)
			field = new StringBuilder(field.toString().substring(1));
		else
			field = new StringBuilder("空");
		UIPanel panel = new UIPanel();
		panel.setPreferredSize(new Dimension(500, 20));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 2, 5, 2);
		c.anchor = GridBagConstraints.WEST;
		// 第一行
		c.gridx = 0;
		c.gridy = 0;
		UILabel m_label1 = new UILabel("导 入 方 式 ：");
		panel.add(m_label1, c);
		c.gridx++;
		getImportWay().setPreferredSize(new Dimension(360, 20));
		panel.add(getImportWay(), c);
		// 第二行
		c.gridx = 0;
		c.gridy++;
		UILabel m_label = new UILabel("不重复字段：");
		panel.add(m_label, c);
		c.gridx++;
		UILabel m_label2 = new UILabel("<html>" + field.toString() + "</html>");// 这里增加html，label可以自动换行
		m_label2.setPreferredSize(new Dimension(360, 160));
		panel.add(m_label2, c);
		return panel;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		ImportProjVO importProjVO = null;
		// DataImportParaVO paraVO = new DataImportParaVO();
		importProjVO = (ImportProjVO) getTreeModel().getSelectedData();
		// paraVO.setImportProjVO(importProjVO);
		DataImportAppModel dataModel = (DataImportAppModel) getModel();
		List aggVOLst = dataModel.getData();
		boolean isShowHint = true;
		boolean isShowData = true;
		AggDataImportVO aggDataImportVO = null;
		if (aggVOLst != null && aggVOLst.size() >= 1) {
			aggDataImportVO = (AggDataImportVO) aggVOLst.get(0);
			String hintMsg = execImpAction(importProjVO, isShowHint,
					isShowData, aggDataImportVO);
			MessageDialog.showHintDlg(model.getContext().getEntranceUI(),
					"提示信息", hintMsg);
		}
	}

	public String execImpAction(ImportProjVO importProjVO, boolean isShowHint,
			boolean isShowData, AggDataImportVO aggDataImportVO)
			throws Exception {
		DataImportMethod dataImportMethod = DataImportMethod.getInstance();
		IDataImportBusiService busiService = NCLocator.getInstance().lookup(
				IDataImportBusiService.class);
		ImportFileSetVO filesetvo = null;

		importProjVO = (ImportProjVO) HYPubBO_Client.queryByPrimaryKey(
				ImportProjVO.class, importProjVO.getPrimaryKey());
		if (importProjVO.getImpmode() != 0) {
			// 说明是需要判断重复数据
			String title = "";
			if (importProjVO.getImpmode() == SystemConst.IMPMODE_INSERT) {
				title = SystemConst.IMPMODE_INSERT_STR;
			} else {
				title = SystemConst.IMPMODE_UPDATE_STR;
			}
			String showRepeatFld = handlerRepeatFld(aggDataImportVO);
			if (showRepeatFld == null || "".equals(showRepeatFld)) {
				throw new BusinessException("当导入方式 为 [" + title
						+ "] 时，请设置不允许重复字段.");
			}
			int btn = MessageDialog.showOkCancelDlg(getContainer(), title,
					"不允许重复的字段为：" + showRepeatFld);
			if (btn == UIDialog.ID_CANCEL) {
				return "取消导入";
			}

		}
		@SuppressWarnings("rawtypes")
		Map hm_info;
		// 1.根据界面数据关系构建各字段对应关系
		hm_info = dataImportMethod.getConstInfoByDataImportVO(aggDataImportVO);

		// 2.根据导入配置信息主表查询出导入项目导入方式（文件、数据源），并区分服务器端还是本地端
		DataImportVO dataImportVO = aggDataImportVO.getParentVO();
		Integer dataFrom = dataImportVO.getDatafrom();// 导入方式 0:数据源，1：文件
		String pk_importproj = importProjVO.getPk_importproj();// 导入项目
		if (SystemConst.DATAFORM_FILE.equals(dataFrom)) {
			// 文件导入，查询文件导入设置，获取该导入文件设置相关
			String where = ImportFileSetVO.PK_IMPORTPROJ + "='" + pk_importproj
					+ "'";
			ImportFileSetVO[] importFileSetVO = (ImportFileSetVO[]) HYPubBO_Client
					.queryByCondition(ImportFileSetVO.class, where);
			if (importFileSetVO == null || importFileSetVO.length == 0) {
				throw new BusinessException("导入项目没有找到对应的文件，请检查导入文件设置功能节点");
			}
			filesetvo = importFileSetVO[0];
			Boolean isserver = filesetvo.getIsserver().booleanValue();
			String filePath = filesetvo.getFilepath();
			// 导入文件
			if (importProjVO.getAttributeValue("projcode").toString()
					.equals("0199")) {
				// 利率信息导入，走新写的方法 -zq
				getImportDataByFileHead(filePath, isserver, aggDataImportVO,
						hm_info);
				getDataConstrastRefHashMap(hm_info, aggDataImportVO);
			} else {
				getImportDataByFile(filePath, isserver, aggDataImportVO,
						hm_info);
				getDataConstrastRefHashMap(hm_info, aggDataImportVO);
			}
		} else if (SystemConst.DATAFORM_DATASOURCE.equals(dataFrom)) {
			// 数据源导入，根据导入条件设置，构建sql语句,用于过滤
			String strWhere = DataSourceSetVO.PK_DATASOURCESET + " in (select "
					+ DataSourceSetVO.PK_DATASOURCESET
					+ " from sec_datasourceassign where pk_importproj='"
					+ pk_importproj + "' and isnull(dr,0)=0)";
			DataSourceSetVO[] dataSourceSetVOs = (DataSourceSetVO[]) HYPubBO_Client
					.queryByCondition(DataSourceSetVO.class, strWhere);
			if (dataSourceSetVOs == null || dataSourceSetVOs.length == 0) {
				throw new BusinessException("没有找到导入项目对应的数据源设置");
			}
			String where = "";
			String dsname = dataSourceSetVOs[0].getDstype();
			boolean isserver = true;
			if (dsname.equals("4")) {
				// 如果是DBF需单独处理.
				isserver = false;
			}
			// 根据数据源获取对应
			dataImportMethod.getImportDataByDS(dataSourceSetVOs[0], isserver,
					aggDataImportVO, where, hm_info);
			// // 对照前进行数据过滤,更新后台缓存数据
			// getNewVByCondAndV(dataImportVO,hm_info, true);
			// 根据取得的数据，判断是否需要进行基础数据对照
			getDataConstrastRefHashMap(hm_info, aggDataImportVO);
			// 对照后进行数据过滤,更新后台缓存数据,这个不能放到getImportDataByDS的最后，否则，如果存在需要进行基础数据对照的数据，则这些数据不能在getQueryCondAfterConstrast方法中得到
			// getNewVByCondAndV(dataImportVO,hm_info, false);
		}
		// 没有做任何改变的原始数据，只是增加了查询条件的处理，这些数据需要展现在ui，方便用户对比
		Map v_ListData = dataImportMethod.getImportDataByCache();

		if (v_ListData == null) {
			return "文件或者数据源没有导入数据";
		}
		if (v_ListData.size() == 0) {
			throw new Exception("没有满足条件的导入数据。");
		}
		Map title = dataImportMethod.getImportTitle();
		if (title == null || title.size() <= 0) {
			throw new RuntimeException("导入文件格式不正确");
		}
		// 用fields作为表头，展现数据
		if (isShowData) {
			DataImportMidDataListDlg dlg = new DataImportMidDataListDlg(
					getContainer(), v_ListData, title, hm_info);
			if (dlg.showModal() != UIDialog.ID_OK) {
				// 清对照关系的缓存
				DataImportCache.getInstance().clearCache();
				busiService.clearBusiCache();
				return "取消导入";
			}
		}
		// 导入数据
		String hitMsg = null;
		try {
			// 利率导入节点的处理
			if (importProjVO.getAttributeValue("projcode").equals("0199")) {
				hitMsg = busiService.importToDBHead();
			} else
				hitMsg = busiService.importToDB();
		} catch (Exception ex) {
			busiService.clearBusiCache();
			throw ex;
		}
		// 备份文件
		if (SystemConst.DATAFORM_FILE.equals(dataFrom)) {
			dataImportMethod.bakFile(filesetvo, hitMsg);
		}
		busiService.clearBusiCache();

		return hitMsg;
	}

	private String handlerRepeatFld(AggDataImportVO aggDataImportVO) {

		StringBuffer fldBuf = new StringBuffer();

		DataImportBVO1[] dataImportBVO1 = (DataImportBVO1[]) aggDataImportVO
				.getChildren(DataImportBVO1.class);

		int voLen = dataImportBVO1.length;
		for (int i = 0; i < voLen; i++) {
			DataImportBVO1 vo = dataImportBVO1[i];
			if (!vo.getIf_repeat().booleanValue()) {
				fldBuf.append(" [");
				fldBuf.append(vo.getAim_field_cn());
				fldBuf.append("] ");
			}
		}

		return fldBuf.toString();

	}

	/**
	 * 根据文件
	 * 
	 * @param filepath
	 * @param isserver
	 * @param metaVO
	 * @param hm_info
	 * @throws Exception
	 */
	public void getImportDataByFile(String filepath, boolean isserver,
			AggDataImportVO aggDataImportVO, Map hm_info) throws Exception {
		DataImportMethod dataImportMethod = DataImportMethod.getInstance();
		if (filepath == null || null == aggDataImportVO)
			return;
		if (isserver) {
			IDataImportQueryService service = (IDataImportQueryService) NCLocator
					.getInstance().lookup(
							IDataImportQueryService.class.getName());
			service.getDataByFilePath(aggDataImportVO, filepath, hm_info);
		} else {
			dataImportMethod.getDataByFilePath(aggDataImportVO, filepath,
					hm_info);
		}
	}

	public void getImportDataByFileHead(String filepath, boolean isserver,
			AggDataImportVO aggDataImportVO, Map hm_info) throws Exception {
		DataImportMethod dataImportMethod = DataImportMethod.getInstance();
		if (filepath == null || null == aggDataImportVO)
			return;
		if (isserver) {
			IDataImportQueryService service = (IDataImportQueryService) NCLocator
					.getInstance().lookup(
							IDataImportQueryService.class.getName());
			service.getDataByFilePath(aggDataImportVO, filepath, hm_info);
		} else {
			// 新方法 -zq
			dataImportMethod.getDataByFilePathHead(aggDataImportVO, filepath,
					hm_info);
		}
	}

	public String getWhereSqlByDS(Map hm_info, boolean isNotFromFile) {

		// 根据选择的导入项目，默认导入方式
		ImportProjVO vo = (ImportProjVO) getTreeModel().getSelectedData();
		;
		if (vo.getVdef2() == null)
			getImportWay().setSelectedIndex(0);
		else
			getImportWay().setSelectedIndex(Integer.parseInt(vo.getVdef2()));

		Map ht_sd_aim = (Map) hm_info.get(SystemConst.HT_SD_AIM);
		// 构造查询界面，如果从文件导入，不用设置查询条件，数据全部导入，如果从数据源导入，根据查询条件取数：0，文件；1，数据源
		QueryConditionClient dlg = new QueryConditionClient();
		dlg.getUITabbedPane().setComponentAt(0, getNormalPanel());

		QueryConditionVO[] qvos = null;
		if (isNotFromFile) {

			// 组合查询模板，如果查询模板为空，则插入构造的查询模板，这样处理是为了利用查询模板保存查询条件。否则为临时构造的查询模板，不能加载历史查询条件
			String nodecode = "HS_HPL_" + "SECD" + "_" + vo.getProjcode();
			try {
				qvos = PubMethod.getInstance().autoGenQueryTemplet(nodecode,
						vo.getProjcode(), aggDataImportVO);
				// 如果为空，表示已经存在查询模板，从数据库直接取数，并从缓存得到qvos；否则自动生成查询模板，这时没有历史查询条件
				if (qvos == null) {
					// 这里必须使用setTempletID，这样才能给类QueryConditionClient中m_curPKCorp，m_curFunCode，m_curUserID，m_templetID赋值;否则不能取历史查询条件
					dlg.setTempletID(getModel().getContext().getPk_org(),
							nodecode,
							getModel().getContext().getPk_loginUser(), null);
					qvos = dlg.getConditionDatas();
				} else {
					dlg.initTempletDatas(qvos);
				}
				// 查询条件的特殊处理,如增加参照,日期等等
				// dataSpecialDealUI(qvos, ht_sd_aim, pk_corp, currdate,
				// userid);
				// 修改了查询模板，更新缓存数据
				dlg.setConditionDatas(qvos);
			} catch (Exception e) {
				dlg.setShowDefine(false);
			}
		} else {
			dlg.setShowDefine(false);
		}
		dlg.showModal();
		if (dlg.getResult() == UIDialog.ID_OK) {
			importWay = getImportWay().getSelectedIndex();// 导入方式
			if (isNotFromFile) {
				String wherecond = dlg.getWhereSQL();
				if (wherecond == null) {
					String question = "数据量可能很大，可能需要等待很长时间。建议增加查询条件，继续导入吗？";
					int flag = MessageDialog.showYesNoCancelDlg(getContainer(),
							"提示信息", question);
					if (flag == UIDialog.ID_YES)
						return "1=1";
					else if (flag == UIDialog.ID_NO)
						return getWhereSqlByDS(ht_sd_aim, isNotFromFile);
					else
						return null;
				}
				return wherecond;
			} else {
				return "1=1";
			}
		}
		return null;// 这里返回null表示是点击取消按钮

	}

	@SuppressWarnings("rawtypes")
	private void getDataConstrastRefHashMap(Map hm_info,
			AggDataImportVO aggDataImpVO) throws Exception {
		IDataImportBusiService dataImportBS = (IDataImportBusiService) NCLocator
				.getInstance().lookup(IDataImportBusiService.class.getName());
		Map hm_ref = dataImportBS.getFailRefData();// 存在没有对照的
		// 显示基础数据对照dlg
		List headMap = (List) hm_ref.get(SystemConst.HEAD);
		List bodyMap = (List) hm_ref.get(SystemConst.BODY);
		if ((headMap != null && headMap.size() > 0)
				|| (bodyMap != null && bodyMap.size() > 0)) { // 表示存在没有设置对照的外系统数据，这时需要弹出基础数据对照界面进行设置。
			showDataContrastDlg(hm_ref, hm_info, aggDataImpVO);
			dataImportBS.setHdlData(hm_ref);
		}
	}

	private boolean showDataContrastDlg(Map dataMap, Map hm_info,
			AggDataImportVO aggDataImpVO) throws Exception {
		boolean isReturn = false;
		// 第一个Key：pk_bdinfo$extsys,第二个KEY: H_,B_ 值：List<FieldPropVO>
		Map<String, Map<String, List>> pendingRef = new HashMap<String, Map<String, List>>();
		// 根据主子表配置，取出所有参照字段
		Map<String, List<String>> refFld = handleRefFld(aggDataImpVO);
		// 处理主表问题数据
		handleHeadRefData(dataMap, pendingRef, refFld);
		// 处理子表问题数据
		handleBodyRefData(dataMap, pendingRef, refFld);

		Set<String> keySet = pendingRef.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while (keyIt.hasNext()) {
			String keyVal = keyIt.next();
			Map refDataMap = pendingRef.get(keyVal);
			// 弹出基础数据对照界面进行设置。
			/** JINGQT 2016年4月12日 过滤未对照功能完善 ADD START */
			/**
			 * 这里添加一个处理，在dataMap中因为过滤了未对照数据，故未对照的数据可能已经没了，但是refDataMap里面还是有值，
			 * 导致其会重新显示此对话框。
			 */
			/** 故这里需要再去对照一下dataMap中是否还有需要对照的数据，做一下过滤即可 */
			if (this.filterRefDataMapByDataMap(refDataMap, dataMap, refFld,
					keyVal) == null) {
				// 如果是空的，则说明不需要再弹出框处理未对照数据了
				continue;
			}
			/** JINGQT 2016年4月12日 过滤未对照功能完善 ADD END */
			DataContrastDlg dlg = new DataContrastDlg(this.getContainer(),
					refDataMap, dataMap, hm_info, keyVal);
			dlg.setTitle("未配置基础数据对照的外系统数据");
			if (dlg.showModal() == UIDialog.ID_CANCEL) {
				isReturn = true;
			}
		}
		return isReturn;
	}

	/**
	 * 这里添加一个处理，在dataMap中因为过滤了未对照数据，故未对照的数据可能已经没了，但是refDataMap里面还是有值，
	 * 导致其会重新显示此对话框 ,这里使用了系统既存的方法重新生成未对照的数据，然后去判断<br>
	 * <b>虽然代码有冗余，但是因为数据都是在内存中，故性能和原来基本一致</b>
	 * 
	 * @param refDataMap
	 *            此次弹出的未对照的数据
	 * @param dataMap
	 *            所有未对照的数据
	 * @param refFld
	 *            存在未对照数据的参照信息
	 * @param key
	 *            此次未对照数据的key值
	 * @return 此次弹出框需要显示的数据key，如果为空，则不弹出框了，否则正常显示。
	 * @author jingqt
	 * @date 2016-4-12 上午9:58:11
	 */
	private String filterRefDataMapByDataMap(Map refDataMap, Map dataMap,
			Map<String, List<String>> refFld, String key) {
		Map<String, Map<String, List>> pendingRef = new HashMap<String, Map<String, List>>();
		// 处理主表问题数据
		handleHeadRefData(dataMap, pendingRef, refFld);
		// 处理子表问题数据
		handleBodyRefData(dataMap, pendingRef, refFld);
		// 筛选,如果重新生成的pendingRef中没有再包含了refDataMap数据了，则说明dataMap中的此种数据已经删除掉了。
		// 这里使用pendingRef.containsValue(refDataMap) 无法达到预期的目的，故重新生成refdatamap的方法
		Iterator<String> keyIt = pendingRef.keySet().iterator();
		// 清空此数据即可
		refDataMap.clear();
		// 如果仍然有未对照的数据
		while (keyIt.hasNext()) {
			// 这里不用循环，只需要取一个即可返回，因为这个方法在循环中
			// 重新赋值 这里不可用=赋值，因为=无法改变map的内部值，即，=无法实现地址传递。
			if (pendingRef.get(key) == null) {
				return null;
			}
			refDataMap.putAll(pendingRef.get(key));
			return key;
		}
		return null;
	}

	private void handleBodyRefData(Map dataMap,
			Map<String, Map<String, List>> pendingRef,
			Map<String, List<String>> refFld) {
		List bodyDataLst = (List) dataMap.get(SystemConst.BODY);// 子表问题数据
		List<String> bodyFldLst = refFld.get(SystemConst.BODY);// 子表参照字段
		if (bodyDataLst != null && bodyDataLst.size() > 0) {// 判断主表是否有参照不到的数据
			if (bodyFldLst != null && bodyFldLst.size() > 0) {// 判断主表是否有设置参照的字段
				int bodyFldSize = bodyFldLst.size();
				for (int i = 0; i < bodyFldSize; i++) {
					String fldVal = bodyFldLst.get(i);// 取参照字段名
					int headDataSize = bodyDataLst.size();
					for (int j = 0; j < headDataSize; j++) {
						Map fldRefVal = (Map) bodyDataLst.get(j);// 取参照出问题数据,先取一行
						Object refObj = fldRefVal.get(fldVal);
						if (refObj instanceof FieldPropVO) {// 如果是FieldPropVO说明没有对照上
							FieldPropVO fldProp = (FieldPropVO) refObj;
							String pk_bdinfo = fldProp.getPk_bdinfo();
							String expsys = fldProp.getDef_expsys();
							String pk_org = fldProp.getPk_org();
							String key = pk_bdinfo + "$" + expsys + "$"
									+ pk_org;
							if (pendingRef.get(key) != null) {
								Map pendingRefData = pendingRef.get(key);
								if (pendingRefData.get(SystemConst.BODY) != null) {
									List bodyLst = (List) pendingRefData
											.get(SystemConst.BODY);
									bodyLst.add(fldProp);
								} else {
									List bodyLst = new ArrayList();
									bodyLst.add(fldProp);
									pendingRefData.put(SystemConst.BODY,
											bodyLst);
								}
							} else {
								Map tmpDataMap = new HashMap();
								List bodyLst = new ArrayList();
								bodyLst.add(fldProp);
								tmpDataMap.put(SystemConst.BODY, bodyLst);
								pendingRef.put(key, tmpDataMap);
							}
						}
					}
				}
			}
		}
	}

	private void handleHeadRefData(Map dataMap,
			Map<String, Map<String, List>> pendingRef,
			Map<String, List<String>> refFld) {
		// 处理主表问题数据
		List headDataLst = (List) dataMap.get(SystemConst.HEAD);// 主表问题数据
		List<String> headFldLst = refFld.get(SystemConst.HEAD);// 主表参照字段
		if (headDataLst != null && headDataLst.size() > 0) {// 判断主表是否有参照不到的数据
			if (headFldLst != null && headFldLst.size() > 0) {// 判断主表是否有设置参照的字段
				int headFldSize = headFldLst.size();
				for (int i = 0; i < headFldSize; i++) {
					String fldVal = headFldLst.get(i);// 取参照字段名
					int headDataSize = headDataLst.size();
					for (int j = 0; j < headDataSize; j++) {
						Map fldRefVal = (Map) headDataLst.get(j);// 取参照出问题数据,先取一行
						Object refObj = fldRefVal.get(fldVal);
						if (refObj instanceof FieldPropVO) {// 如果是FieldPropVO说明没有对照上
							FieldPropVO fldProp = (FieldPropVO) refObj;
							String pk_bdinfo = fldProp.getPk_bdinfo();
							String expsys = fldProp.getDef_expsys();
							String pk_org = fldProp.getPk_org();
							String pk_group = fldProp.getPk_group();
							String pk_org_v = fldProp.getPk_org_v();
							String key = pk_bdinfo + "$" + expsys + "$"
									+ pk_org + "$" + pk_group + "$" + pk_org_v;
							if (pendingRef.get(key) != null) {
								Map pendingRefData = pendingRef.get(key);
								if (pendingRefData.get(SystemConst.HEAD) != null) {
									List headLst = (List) pendingRefData
											.get(SystemConst.HEAD);
									headLst.add(fldProp);
								} else {
									List headLst = new ArrayList();
									headLst.add(fldProp);
									pendingRefData.put(SystemConst.HEAD,
											headLst);
								}
							} else {
								Map tmpDataMap = new HashMap();
								List headLst = new ArrayList();
								headLst.add(fldProp);
								tmpDataMap.put(SystemConst.HEAD, headLst);
								pendingRef.put(key, tmpDataMap);
							}
						}
					}
				}
			}
		}
	}

	private Map<String, List<String>> handleRefFld(AggDataImportVO aggDataImpVO) {
		Map<String, List<String>> refFldMap = new HashMap<String, List<String>>();
		DataImportBVO1[] dataImportBVO1 = (DataImportBVO1[]) aggDataImpVO
				.getChildren(DataImportBVO1.class);
		DataImportBVO2[] dataImportBVO2 = (DataImportBVO2[]) aggDataImpVO
				.getChildren(DataImportBVO2.class);
		if (dataImportBVO1 != null && dataImportBVO1.length > 0) {
			int bvo1Len = dataImportBVO1.length;
			for (int i = 0; i < bvo1Len; i++) {
				DataImportBVO1 vo1 = dataImportBVO1[i];
				if (vo1.getIf_ref().booleanValue()) {
					if (refFldMap.get(SystemConst.HEAD) != null) {
						List headFldLst = refFldMap.get(SystemConst.HEAD);
						headFldLst.add(vo1.getDs_field());
						refFldMap.put(SystemConst.HEAD, headFldLst);
					} else {
						List headFldLst = new ArrayList();
						headFldLst.add(vo1.getDs_field());
						refFldMap.put(SystemConst.HEAD, headFldLst);
					}
				}
			}
		}

		if (dataImportBVO2 != null && dataImportBVO2.length > 0) {
			int bvo2Len = dataImportBVO2.length;
			for (int i = 0; i < bvo2Len; i++) {
				DataImportBVO2 vo2 = dataImportBVO2[i];
				if (vo2.getIf_ref().booleanValue()) {
					if (refFldMap.get(SystemConst.BODY) != null) {
						List bodyFldLst = refFldMap.get(SystemConst.BODY);
						bodyFldLst.add(vo2.getDs_field());
						refFldMap.put(SystemConst.BODY, bodyFldLst);
					} else {
						List bodyFldLst = new ArrayList();
						bodyFldLst.add(vo2.getDs_field());
						refFldMap.put(SystemConst.BODY, bodyFldLst);
					}
				}
			}
		}
		return refFldMap;
	}

}
