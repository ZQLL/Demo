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
		setBtnName("����");

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
	 * �������ַ�ʽ
	 * 
	 * @return UIComboBox
	 */
	public UIComboBox getImportWay() {
		if (m_importWay == null) {
			try {
				m_importWay = new UIComboBox();
				m_importWay.setName("importWay");
				String[] n = { "�������ظ�������ȫ������", "�����ظ�������ʽ����", "�����ظ������Ƿ�ʽ����" };
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
		// �ظ��ֶ�
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
		// add: �����������Ϊ��,��ʾ�ֶ�Ӣ����,�����������ʾΪnull)
		// }
		// }
		// }
		// if(null!=dataImportBVO2&&dataImportBVO2.length>0){
		// int bvo1Len = dataImportBVO2.length;
		// for(int i=0;i<bvo1Len;i++){
		// if_repeat = dataImportBVO2[i].getIf_repeat().booleanValue();
		// if (!if_repeat) {
		// field.append(", ").append(dataImportBVO2[i].getAim_field()).append(" ");//(mei
		// add: �����������Ϊ��,��ʾ�ֶ�Ӣ����,�����������ʾΪnull)
		// }
		// }
		// }
		// }
		if (field.toString().length() > 0)
			field = new StringBuilder(field.toString().substring(1));
		else
			field = new StringBuilder("��");
		UIPanel panel = new UIPanel();
		panel.setPreferredSize(new Dimension(500, 20));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 2, 5, 2);
		c.anchor = GridBagConstraints.WEST;
		// ��һ��
		c.gridx = 0;
		c.gridy = 0;
		UILabel m_label1 = new UILabel("�� �� �� ʽ ��");
		panel.add(m_label1, c);
		c.gridx++;
		getImportWay().setPreferredSize(new Dimension(360, 20));
		panel.add(getImportWay(), c);
		// �ڶ���
		c.gridx = 0;
		c.gridy++;
		UILabel m_label = new UILabel("���ظ��ֶΣ�");
		panel.add(m_label, c);
		c.gridx++;
		UILabel m_label2 = new UILabel("<html>" + field.toString() + "</html>");// ��������html��label�����Զ�����
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
					"��ʾ��Ϣ", hintMsg);
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
			// ˵������Ҫ�ж��ظ�����
			String title = "";
			if (importProjVO.getImpmode() == SystemConst.IMPMODE_INSERT) {
				title = SystemConst.IMPMODE_INSERT_STR;
			} else {
				title = SystemConst.IMPMODE_UPDATE_STR;
			}
			String showRepeatFld = handlerRepeatFld(aggDataImportVO);
			if (showRepeatFld == null || "".equals(showRepeatFld)) {
				throw new BusinessException("�����뷽ʽ Ϊ [" + title
						+ "] ʱ�������ò������ظ��ֶ�.");
			}
			int btn = MessageDialog.showOkCancelDlg(getContainer(), title,
					"�������ظ����ֶ�Ϊ��" + showRepeatFld);
			if (btn == UIDialog.ID_CANCEL) {
				return "ȡ������";
			}

		}
		@SuppressWarnings("rawtypes")
		Map hm_info;
		// 1.���ݽ������ݹ�ϵ�������ֶζ�Ӧ��ϵ
		hm_info = dataImportMethod.getConstInfoByDataImportVO(aggDataImportVO);

		// 2.���ݵ���������Ϣ�����ѯ��������Ŀ���뷽ʽ���ļ�������Դ���������ַ������˻��Ǳ��ض�
		DataImportVO dataImportVO = aggDataImportVO.getParentVO();
		Integer dataFrom = dataImportVO.getDatafrom();// ���뷽ʽ 0:����Դ��1���ļ�
		String pk_importproj = importProjVO.getPk_importproj();// ������Ŀ
		if (SystemConst.DATAFORM_FILE.equals(dataFrom)) {
			// �ļ����룬��ѯ�ļ��������ã���ȡ�õ����ļ��������
			String where = ImportFileSetVO.PK_IMPORTPROJ + "='" + pk_importproj
					+ "'";
			ImportFileSetVO[] importFileSetVO = (ImportFileSetVO[]) HYPubBO_Client
					.queryByCondition(ImportFileSetVO.class, where);
			if (importFileSetVO == null || importFileSetVO.length == 0) {
				throw new BusinessException("������Ŀû���ҵ���Ӧ���ļ������鵼���ļ����ù��ܽڵ�");
			}
			filesetvo = importFileSetVO[0];
			Boolean isserver = filesetvo.getIsserver().booleanValue();
			String filePath = filesetvo.getFilepath();
			// �����ļ�
			if (importProjVO.getAttributeValue("projcode").toString()
					.equals("0199")) {
				// ������Ϣ���룬����д�ķ��� -zq
				getImportDataByFileHead(filePath, isserver, aggDataImportVO,
						hm_info);
				getDataConstrastRefHashMap(hm_info, aggDataImportVO);
			} else {
				getImportDataByFile(filePath, isserver, aggDataImportVO,
						hm_info);
				getDataConstrastRefHashMap(hm_info, aggDataImportVO);
			}
		} else if (SystemConst.DATAFORM_DATASOURCE.equals(dataFrom)) {
			// ����Դ���룬���ݵ����������ã�����sql���,���ڹ���
			String strWhere = DataSourceSetVO.PK_DATASOURCESET + " in (select "
					+ DataSourceSetVO.PK_DATASOURCESET
					+ " from sec_datasourceassign where pk_importproj='"
					+ pk_importproj + "' and isnull(dr,0)=0)";
			DataSourceSetVO[] dataSourceSetVOs = (DataSourceSetVO[]) HYPubBO_Client
					.queryByCondition(DataSourceSetVO.class, strWhere);
			if (dataSourceSetVOs == null || dataSourceSetVOs.length == 0) {
				throw new BusinessException("û���ҵ�������Ŀ��Ӧ������Դ����");
			}
			String where = "";
			String dsname = dataSourceSetVOs[0].getDstype();
			boolean isserver = true;
			if (dsname.equals("4")) {
				// �����DBF�赥������.
				isserver = false;
			}
			// ��������Դ��ȡ��Ӧ
			dataImportMethod.getImportDataByDS(dataSourceSetVOs[0], isserver,
					aggDataImportVO, where, hm_info);
			// // ����ǰ�������ݹ���,���º�̨��������
			// getNewVByCondAndV(dataImportVO,hm_info, true);
			// ����ȡ�õ����ݣ��ж��Ƿ���Ҫ���л������ݶ���
			getDataConstrastRefHashMap(hm_info, aggDataImportVO);
			// ���պ�������ݹ���,���º�̨��������,������ܷŵ�getImportDataByDS����󣬷������������Ҫ���л������ݶ��յ����ݣ�����Щ���ݲ�����getQueryCondAfterConstrast�����еõ�
			// getNewVByCondAndV(dataImportVO,hm_info, false);
		}
		// û�����κθı��ԭʼ���ݣ�ֻ�������˲�ѯ�����Ĵ�����Щ������Ҫչ����ui�������û��Ա�
		Map v_ListData = dataImportMethod.getImportDataByCache();

		if (v_ListData == null) {
			return "�ļ���������Դû�е�������";
		}
		if (v_ListData.size() == 0) {
			throw new Exception("û�����������ĵ������ݡ�");
		}
		Map title = dataImportMethod.getImportTitle();
		if (title == null || title.size() <= 0) {
			throw new RuntimeException("�����ļ���ʽ����ȷ");
		}
		// ��fields��Ϊ��ͷ��չ������
		if (isShowData) {
			DataImportMidDataListDlg dlg = new DataImportMidDataListDlg(
					getContainer(), v_ListData, title, hm_info);
			if (dlg.showModal() != UIDialog.ID_OK) {
				// ����չ�ϵ�Ļ���
				DataImportCache.getInstance().clearCache();
				busiService.clearBusiCache();
				return "ȡ������";
			}
		}
		// ��������
		String hitMsg = null;
		try {
			// ���ʵ���ڵ�Ĵ���
			if (importProjVO.getAttributeValue("projcode").equals("0199")) {
				hitMsg = busiService.importToDBHead();
			} else
				hitMsg = busiService.importToDB();
		} catch (Exception ex) {
			busiService.clearBusiCache();
			throw ex;
		}
		// �����ļ�
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
	 * �����ļ�
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
			// �·��� -zq
			dataImportMethod.getDataByFilePathHead(aggDataImportVO, filepath,
					hm_info);
		}
	}

	public String getWhereSqlByDS(Map hm_info, boolean isNotFromFile) {

		// ����ѡ��ĵ�����Ŀ��Ĭ�ϵ��뷽ʽ
		ImportProjVO vo = (ImportProjVO) getTreeModel().getSelectedData();
		;
		if (vo.getVdef2() == null)
			getImportWay().setSelectedIndex(0);
		else
			getImportWay().setSelectedIndex(Integer.parseInt(vo.getVdef2()));

		Map ht_sd_aim = (Map) hm_info.get(SystemConst.HT_SD_AIM);
		// �����ѯ���棬������ļ����룬�������ò�ѯ����������ȫ�����룬���������Դ���룬���ݲ�ѯ����ȡ����0���ļ���1������Դ
		QueryConditionClient dlg = new QueryConditionClient();
		dlg.getUITabbedPane().setComponentAt(0, getNormalPanel());

		QueryConditionVO[] qvos = null;
		if (isNotFromFile) {

			// ��ϲ�ѯģ�壬�����ѯģ��Ϊ�գ�����빹��Ĳ�ѯģ�壬����������Ϊ�����ò�ѯģ�屣���ѯ����������Ϊ��ʱ����Ĳ�ѯģ�壬���ܼ�����ʷ��ѯ����
			String nodecode = "HS_HPL_" + "SECD" + "_" + vo.getProjcode();
			try {
				qvos = PubMethod.getInstance().autoGenQueryTemplet(nodecode,
						vo.getProjcode(), aggDataImportVO);
				// ���Ϊ�գ���ʾ�Ѿ����ڲ�ѯģ�壬�����ݿ�ֱ��ȡ�������ӻ���õ�qvos�������Զ����ɲ�ѯģ�壬��ʱû����ʷ��ѯ����
				if (qvos == null) {
					// �������ʹ��setTempletID���������ܸ���QueryConditionClient��m_curPKCorp��m_curFunCode��m_curUserID��m_templetID��ֵ;������ȡ��ʷ��ѯ����
					dlg.setTempletID(getModel().getContext().getPk_org(),
							nodecode,
							getModel().getContext().getPk_loginUser(), null);
					qvos = dlg.getConditionDatas();
				} else {
					dlg.initTempletDatas(qvos);
				}
				// ��ѯ���������⴦��,�����Ӳ���,���ڵȵ�
				// dataSpecialDealUI(qvos, ht_sd_aim, pk_corp, currdate,
				// userid);
				// �޸��˲�ѯģ�壬���»�������
				dlg.setConditionDatas(qvos);
			} catch (Exception e) {
				dlg.setShowDefine(false);
			}
		} else {
			dlg.setShowDefine(false);
		}
		dlg.showModal();
		if (dlg.getResult() == UIDialog.ID_OK) {
			importWay = getImportWay().getSelectedIndex();// ���뷽ʽ
			if (isNotFromFile) {
				String wherecond = dlg.getWhereSQL();
				if (wherecond == null) {
					String question = "���������ܴܺ󣬿�����Ҫ�ȴ��ܳ�ʱ�䡣�������Ӳ�ѯ����������������";
					int flag = MessageDialog.showYesNoCancelDlg(getContainer(),
							"��ʾ��Ϣ", question);
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
		return null;// ���ﷵ��null��ʾ�ǵ��ȡ����ť

	}

	@SuppressWarnings("rawtypes")
	private void getDataConstrastRefHashMap(Map hm_info,
			AggDataImportVO aggDataImpVO) throws Exception {
		IDataImportBusiService dataImportBS = (IDataImportBusiService) NCLocator
				.getInstance().lookup(IDataImportBusiService.class.getName());
		Map hm_ref = dataImportBS.getFailRefData();// ����û�ж��յ�
		// ��ʾ�������ݶ���dlg
		List headMap = (List) hm_ref.get(SystemConst.HEAD);
		List bodyMap = (List) hm_ref.get(SystemConst.BODY);
		if ((headMap != null && headMap.size() > 0)
				|| (bodyMap != null && bodyMap.size() > 0)) { // ��ʾ����û�����ö��յ���ϵͳ���ݣ���ʱ��Ҫ�����������ݶ��ս���������á�
			showDataContrastDlg(hm_ref, hm_info, aggDataImpVO);
			dataImportBS.setHdlData(hm_ref);
		}
	}

	private boolean showDataContrastDlg(Map dataMap, Map hm_info,
			AggDataImportVO aggDataImpVO) throws Exception {
		boolean isReturn = false;
		// ��һ��Key��pk_bdinfo$extsys,�ڶ���KEY: H_,B_ ֵ��List<FieldPropVO>
		Map<String, Map<String, List>> pendingRef = new HashMap<String, Map<String, List>>();
		// �������ӱ����ã�ȡ�����в����ֶ�
		Map<String, List<String>> refFld = handleRefFld(aggDataImpVO);
		// ����������������
		handleHeadRefData(dataMap, pendingRef, refFld);
		// �����ӱ���������
		handleBodyRefData(dataMap, pendingRef, refFld);

		Set<String> keySet = pendingRef.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while (keyIt.hasNext()) {
			String keyVal = keyIt.next();
			Map refDataMap = pendingRef.get(keyVal);
			// �����������ݶ��ս���������á�
			/** JINGQT 2016��4��12�� ����δ���չ������� ADD START */
			/**
			 * �������һ��������dataMap����Ϊ������δ�������ݣ���δ���յ����ݿ����Ѿ�û�ˣ�����refDataMap���滹����ֵ��
			 * �������������ʾ�˶Ի���
			 */
			/** ��������Ҫ��ȥ����һ��dataMap���Ƿ�����Ҫ���յ����ݣ���һ�¹��˼��� */
			if (this.filterRefDataMapByDataMap(refDataMap, dataMap, refFld,
					keyVal) == null) {
				// ����ǿյģ���˵������Ҫ�ٵ�������δ����������
				continue;
			}
			/** JINGQT 2016��4��12�� ����δ���չ������� ADD END */
			DataContrastDlg dlg = new DataContrastDlg(this.getContainer(),
					refDataMap, dataMap, hm_info, keyVal);
			dlg.setTitle("δ���û������ݶ��յ���ϵͳ����");
			if (dlg.showModal() == UIDialog.ID_CANCEL) {
				isReturn = true;
			}
		}
		return isReturn;
	}

	/**
	 * �������һ��������dataMap����Ϊ������δ�������ݣ���δ���յ����ݿ����Ѿ�û�ˣ�����refDataMap���滹����ֵ��
	 * �������������ʾ�˶Ի��� ,����ʹ����ϵͳ�ȴ�ķ�����������δ���յ����ݣ�Ȼ��ȥ�ж�<br>
	 * <b>��Ȼ���������࣬������Ϊ���ݶ������ڴ��У������ܺ�ԭ������һ��</b>
	 * 
	 * @param refDataMap
	 *            �˴ε�����δ���յ�����
	 * @param dataMap
	 *            ����δ���յ�����
	 * @param refFld
	 *            ����δ�������ݵĲ�����Ϣ
	 * @param key
	 *            �˴�δ�������ݵ�keyֵ
	 * @return �˴ε�������Ҫ��ʾ������key�����Ϊ�գ��򲻵������ˣ�����������ʾ��
	 * @author jingqt
	 * @date 2016-4-12 ����9:58:11
	 */
	private String filterRefDataMapByDataMap(Map refDataMap, Map dataMap,
			Map<String, List<String>> refFld, String key) {
		Map<String, Map<String, List>> pendingRef = new HashMap<String, Map<String, List>>();
		// ����������������
		handleHeadRefData(dataMap, pendingRef, refFld);
		// �����ӱ���������
		handleBodyRefData(dataMap, pendingRef, refFld);
		// ɸѡ,����������ɵ�pendingRef��û���ٰ�����refDataMap�����ˣ���˵��dataMap�еĴ��������Ѿ�ɾ�����ˡ�
		// ����ʹ��pendingRef.containsValue(refDataMap) �޷��ﵽԤ�ڵ�Ŀ�ģ�����������refdatamap�ķ���
		Iterator<String> keyIt = pendingRef.keySet().iterator();
		// ��մ����ݼ���
		refDataMap.clear();
		// �����Ȼ��δ���յ�����
		while (keyIt.hasNext()) {
			// ���ﲻ��ѭ����ֻ��Ҫȡһ�����ɷ��أ���Ϊ���������ѭ����
			// ���¸�ֵ ���ﲻ����=��ֵ����Ϊ=�޷��ı�map���ڲ�ֵ������=�޷�ʵ�ֵ�ַ���ݡ�
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
		List bodyDataLst = (List) dataMap.get(SystemConst.BODY);// �ӱ���������
		List<String> bodyFldLst = refFld.get(SystemConst.BODY);// �ӱ�����ֶ�
		if (bodyDataLst != null && bodyDataLst.size() > 0) {// �ж������Ƿ��в��ղ���������
			if (bodyFldLst != null && bodyFldLst.size() > 0) {// �ж������Ƿ������ò��յ��ֶ�
				int bodyFldSize = bodyFldLst.size();
				for (int i = 0; i < bodyFldSize; i++) {
					String fldVal = bodyFldLst.get(i);// ȡ�����ֶ���
					int headDataSize = bodyDataLst.size();
					for (int j = 0; j < headDataSize; j++) {
						Map fldRefVal = (Map) bodyDataLst.get(j);// ȡ���ճ���������,��ȡһ��
						Object refObj = fldRefVal.get(fldVal);
						if (refObj instanceof FieldPropVO) {// �����FieldPropVO˵��û�ж�����
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
		// ����������������
		List headDataLst = (List) dataMap.get(SystemConst.HEAD);// ������������
		List<String> headFldLst = refFld.get(SystemConst.HEAD);// ��������ֶ�
		if (headDataLst != null && headDataLst.size() > 0) {// �ж������Ƿ��в��ղ���������
			if (headFldLst != null && headFldLst.size() > 0) {// �ж������Ƿ������ò��յ��ֶ�
				int headFldSize = headFldLst.size();
				for (int i = 0; i < headFldSize; i++) {
					String fldVal = headFldLst.get(i);// ȡ�����ֶ���
					int headDataSize = headDataLst.size();
					for (int j = 0; j < headDataSize; j++) {
						Map fldRefVal = (Map) headDataLst.get(j);// ȡ���ճ���������,��ȡһ��
						Object refObj = fldRefVal.get(fldVal);
						if (refObj instanceof FieldPropVO) {// �����FieldPropVO˵��û�ж�����
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
