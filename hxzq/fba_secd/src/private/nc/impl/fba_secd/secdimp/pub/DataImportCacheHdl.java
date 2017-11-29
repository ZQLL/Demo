package nc.impl.fba_secd.secdimp.pub;

import java.util.ArrayList;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.pubitf.org.IOrgUnitPubService;
import nc.vo.fba_secd.secdimp.dataimport.AggDataImportVO;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO1;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO2;
import nc.vo.fba_secd.secdimp.dataimport.DataImportVO;
import nc.vo.fba_secd.secdimp.pub.AppContextUtil;
import nc.vo.fba_secd.secdimp.pub.FieldPropVO;
import nc.vo.fba_secd.secdimp.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import java.util.*;

/**
 * �����̨���洦�� ��װ��ǰ̨���浼���������Ϣ 1.ԭʼ�������� _busi_DataLst
 * 2.�ֶζ�����Ϣ��������գ�������ա�_normal_MetaData_Cache|_unNormal_MetaData_Cache
 * 3.����ɹ������ݣ�����ɹ��ĺ���Ϊ�����ա���ʽ�������ȫ���ܴ���ɹ������ݡ� 4.����ʧ�ܵ����ݣ�����ʧ�ܵĺ���Ϊ�����ջ��߹�ʽ������������쳣�����ݡ�
 * 5.ǰ̨����չ���õ��ֶ���Ϣ��
 * 
 * @author wyg
 */

public class DataImportCacheHdl {

	private static Map<String, DataImportCacheHdl> _user_Cache_Instance = new HashMap<String, DataImportCacheHdl>();

	/*
	 * �����黺���ֶ�������Ϣ(���� aim field-->sd field)�� Map<String,Map<String,Object>>
	 * ͷ/����/�ӱ� AIM�ֶ����� BVO T/H/B
	 */
	private Map<String, Map<String, Object>> _normal_MetaData_Cache = new HashMap<String, Map<String, Object>>();

	/*
	 * �����黺���ֶ�������Ϣ(���� sd field-->aim field)�� Map<String,Map<String,Object>>
	 * ͷ/����/�ӱ� SD�ֶ����� BVO T/H/B
	 */
	private Map<String, Map<String, Object>> _unNormal_MetaData_Cache = new HashMap<String, Map<String, Object>>();

	/*
	 * �������ݻ��� List��ÿ��ά��Ϊһ�����ݣ� Map<String,List<Map<String,Object>>>
	 * ������Map<����/�ӱ�,List<Map<�ֶΣ�ֵ>>> ������;
	 * ����֧�����ӱ��룬���Ե�һ��MapΪ��������ӱ�ҳǩ����KEY��ΪH_���ӱ�KEY��ΪB_
	 */
	private Map<String, List<Map<String, Object>>> _busi_DataMapLst = new HashMap<String, List<Map<String, Object>>>();

	/*
	 * ���ݶ���û���������ݻ��� ���ڱ���ʱ�洢ʹ��q
	 */
	private Map<String, List<Map<String, Object>>> _success_DataMapLst = new HashMap<String, List<Map<String, Object>>>();

	/*
	 * ���ݶ��մ������������ ���ص�ǰ̨���ж�������
	 */
	private Map<String, List<Map<String, FieldPropVO>>> _fail_DataMapLst = new HashMap<String, List<Map<String, FieldPropVO>>>();

	/*
	 * ԭʼ���������ֶΡ�
	 */
	private Map<String, Map> _title_Map = new HashMap<String, Map>();

	private Map<String, String> orgInfoMap = new HashMap<String, String>();

	private DataImportCacheHdl() {
	}

	public static DataImportCacheHdl getInstance() {
		DataImportCacheHdl cacheInstance = null;
		// ������û�ͬʱ�õ������⣬ÿ���û������Ե����ݻ��棬�Ա������ݳ�ͻ.
		String currentUser = InvocationInfoProxy.getInstance().getUserCode();
		cacheInstance = _user_Cache_Instance.get(currentUser);
		if (cacheInstance == null) {
			cacheInstance = new DataImportCacheHdl();
			_user_Cache_Instance.put(currentUser, cacheInstance);
		}
		return cacheInstance;
	}

	/**
	 * ���Ȼ���ҵ������ ��Щҵ������δ��������ԭʼ����
	 * 
	 * @param hm_info
	 * @param result
	 *            0 title,1 tablename,2 datalist
	 * @param aggDataImportVO
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "rawtypes" })
	public void cacheBusiData(AggDataImportVO aggDataImportVO,
			Map<String, List> result, Map hm_info) throws BusinessException {
		initOrgInfo();
		// �����ֶζ��ջ���
		initMetaDataCache(aggDataImportVO, hm_info);
		// ��ʼ�����ݻ���
		initBusiDataCache(result);
		// ������ռ���ʽ������
		filterRefData();

	}

	/**
	 * ������գ��������û�ж��չ�ϵ�����ݽ������ failDataLst�� ������ȷ�ķ��� successDataLst
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void filterRefData() throws BusinessException {

		List _headLst = _busi_DataMapLst.get(SystemConst.HEAD);
		List _bodyLst = _busi_DataMapLst.get(SystemConst.BODY);

		convertRefData(_headLst, SystemConst.HEAD);

		convertRefData(_bodyLst, SystemConst.BODY);
	}

	/**
	 * ������ն��չ�ϵ
	 * 
	 * @param dataLst
	 * @param head
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void convertRefData(List<Map> dataLst, String tabNo)
			throws BusinessException {
		List successLst = new ArrayList();
		List failLst = new ArrayList();
		if (dataLst != null && dataLst.size() > 0) {
			int dataSize = dataLst.size();
			for (int i = 0; i < dataSize; i++) {
				// ����ֶ�ȫ��ת���ɹ������뵽successDataLst
				if (canConvert(dataLst.get(i), tabNo)) {
					successLst.add(dataLst.get(i));
				} else {
					// ������뵽failDataLst
					failLst.add(dataLst.get(i));
				}
			}
		}
		_success_DataMapLst.put(tabNo, successLst);
		_fail_DataMapLst.put(tabNo, failLst);
		DataImportBusiDAO.clearContrastMap();
	}

	@SuppressWarnings({ "rawtypes" })
	private boolean canConvert(Map rowMap, String tabNo)
			throws BusinessException {
		boolean canConvert = true;
		if (rowMap != null && rowMap.size() > 0) {
			Set keySet = rowMap.keySet();
			Iterator keyIt = keySet.iterator();
			// ��Ҫ����һ��Key,�ò��յ������Ƚ���ȡֵ��
			List<String> refLst = new ArrayList<String>();
			List<String> otherLst = new ArrayList<String>();
			Map fldMap = _unNormal_MetaData_Cache.get(tabNo);
			while (keyIt.hasNext()) {
				String fldKey = (String) keyIt.next();
				if (fldMap.get(fldKey) != null) {
					SuperVO metaDataVO = (SuperVO) fldMap.get(fldKey);
					UFBoolean if_ref = (UFBoolean) metaDataVO
							.getAttributeValue(DataImportBVO1.IF_REF);
					if (if_ref.booleanValue()) {
						refLst.add(fldKey);
					} else {
						otherLst.add(fldKey);
					}
				}
			}
			refLst.addAll(otherLst);
			int refLstSize = refLst.size();
			for (int i = 0; i < refLstSize; i++) {
				String fldKeyOrder = refLst.get(i);
				if (!getValueFromRef(rowMap, fldKeyOrder, tabNo)) {
					canConvert = false;
				}
			}
		}
		return canConvert;
	}

	/**
	 * @param rowMap
	 *            Key:�ֶ��� value:ֵ(�ļ���������Դ�õ���)
	 * @param fldKey
	 * @param tabNo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean getValueFromRef(Map rowMap, String fldKey, String tabNo)
			throws BusinessException {
		boolean canRefValue = true;
		// ���ȸ����ֶ�����ȡ�ֶζ��չ�ϵ
		Map fldMap = _unNormal_MetaData_Cache.get(tabNo);
		String orivalue = null;
		Object objVal = rowMap.get(fldKey);
		if (objVal != null) {
			orivalue = objVal.toString();
		}

		if (fldMap.get(fldKey) != null) {
			SuperVO metaDataVO = (SuperVO) fldMap.get(fldKey);
			UFBoolean if_ref = (UFBoolean) metaDataVO
					.getAttributeValue(DataImportBVO1.IF_REF);
			UFBoolean if_formular = (UFBoolean) metaDataVO
					.getAttributeValue(DataImportBVO1.IF_FORMULAR);
			String defaultValSet = (String) metaDataVO
					.getAttributeValue(DataImportBVO1.DEFAULTVALUE);
			// �ж� �Ƿ����ò���
			if (if_formular.booleanValue() || if_ref.booleanValue()
					|| defaultValSet != null) {
				if (if_formular.booleanValue()) {
					handleFormular(rowMap, fldKey, true, metaDataVO, orivalue);
				}
				if (if_ref.booleanValue()) {
					objVal = rowMap.get(fldKey);
					if (objVal != null) {
						orivalue = objVal.toString();
					}
					// ����expsys��pk_bdinfo��ѯ�������ݶ��ձ�;
					canRefValue = handleRef(rowMap, fldKey, true, metaDataVO,
							orivalue);
					if (!canRefValue) {
						return canRefValue;
					}
				}
				if (defaultValSet != null) {
					handlerDefaultVal(rowMap, fldKey, metaDataVO, orivalue);
				}
			} else {
				rowMap.put(fldKey, orivalue);
			}
		} else {
			rowMap.put(fldKey, orivalue);
		}
		return canRefValue;
	}

	@SuppressWarnings("unchecked")
	private void handlerDefaultVal(Map rowMap, String fldKey,
			SuperVO metaDataVO, String orivalue) {
		String defaultVal = (String) metaDataVO
				.getAttributeValue(DataImportBVO1.DEFAULTVALUE);
		Object custDefVal = metaDataVO
				.getAttributeValue(DataImportBVO1.CUSTDEFAULTVAL);
		if (SystemConst.DEFAULT_DATE.equals(defaultVal)) {
			rowMap.put(fldKey, new UFDate());
		} else if (SystemConst.DEFAULT_USER.equals(defaultVal)) {
			AppContext context = AppContext.getInstance();
			String pk_user = context.getPkUser();
			rowMap.put(fldKey, pk_user);
		} else if (SystemConst.DEFAULT_GLBOOK.equals(defaultVal)) {
			rowMap.put(fldKey, orgInfoMap.get(SystemConst.ORG_PK_GLORGBOOK));
		} else if (SystemConst.DEFAULT_GROUP.equals(defaultVal)) {
			rowMap.put(fldKey, orgInfoMap.get(SystemConst.ORG_PK_GROUP));
		} else if (SystemConst.DEFAULT_ORG.equals(defaultVal)) {
			rowMap.put(fldKey, orgInfoMap.get(SystemConst.ORG_PK_ORG));
		} else if (SystemConst.DEFAULT_ORG_V.equals(defaultVal)) {
			rowMap.put(fldKey, orgInfoMap.get(SystemConst.ORG_PK_ORG_V));
		} else if (SystemConst.DEFAUKT_CUST.equals(defaultVal)) {
			if (custDefVal != null) {
				rowMap.put(fldKey, custDefVal);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean handleFormular(Map rowMap, String fldKey,
			boolean canRefValue, SuperVO metaDataVO, String orivalue)
			throws BusinessException {
		boolean bln = false;
		FormulaParseFather formulaParse = new nc.bs.pub.formulaparse.FormulaParse();
		String impformular = (String) metaDataVO
				.getAttributeValue(DataImportBVO1.IMPFORMULAR);
		if (impformular == null || "".equals(impformular)) {
			throw new BusinessException(fldKey + "û�����ù�ʽ����ȷ��!");
		}
		formulaParse.setExpress(impformular);
		VarryVO[] varrys = formulaParse.getVarryArray();
		if (varrys == null || varrys.length == 0) {
			throw new BusinessException(fldKey + "���õĹ�ʽ��" + impformular
					+ "����ȷ����ȷ�ϣ���ʽ���ô�����߲�֧�ִ˹�ʽ��");
		}
		for (int i = 0; i < varrys.length; i++) {
			String[] varries = varrys[i].getVarry();
			if (varries != null) {
				for (int j = 0; j < varries.length; j++) {
					Object varryValue = getValByDataType(rowMap, metaDataVO,
							varries[j]);

					formulaParse.addVariable(varries[j], varryValue);
				}
			}
		}
		Object res = formulaParse.getValueAsObject();
		if (res == null) {
			if (metaDataVO.getAttributeValue(DataImportBVO1.DEFAULTVALUE) != null) {
				handlerDefaultVal(rowMap, fldKey, metaDataVO, orivalue);// ��ʽȡ�������鿴�Ƿ�������Ĭ��ֵ��
			} else {
				rowMap.put(fldKey, res);
			}
		} else {
			rowMap.put(fldKey, res);
		}

		return bln;
	}

	private Object getValByDataType(Map rowMap, SuperVO metaDataVO,
			String varStr) throws BusinessException {
		String varryValue = null;
		if (rowMap.get(varStr) != null
				|| rowMap.get(varStr.toUpperCase()) != null) {
			Object varObj = rowMap.get(varStr);
			if (varObj == null) {
				varObj = rowMap.get(varStr.toUpperCase());
			}
			if (varObj instanceof FieldPropVO) {
				FieldPropVO fpvo = (FieldPropVO) varObj;
				varryValue = fpvo.getFieldValue();
			} else {
				varryValue = varObj.toString();
			}
		}
		String dataType = (String) metaDataVO
				.getAttributeValue(DataImportBVO1.DATATYPE);

		return DataImportCommonUtil.handlerDataByType(varryValue, dataType);
	}

	/**
	 * �������.
	 * 
	 * @param rowMap
	 * @param fldKey
	 * @param canRefValue
	 * @param metaDataVO
	 * @param orivalue
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean handleRef(Map rowMap, String fldKey, boolean canRefValue,
			SuperVO metaDataVO, String orivalue) throws BusinessException {
		String expsys = (String) metaDataVO
				.getAttributeValue(DataImportBVO1.DEF_EXPSYS);
		String pk_bdinfo = (String) metaDataVO
				.getAttributeValue(DataImportBVO1.DEF_PKBDINFO);
		Map topMap = _normal_MetaData_Cache.get(SystemConst.TOP);
		DataImportVO tVO = (DataImportVO) topMap.get(SystemConst.TOP);

		String trasferData = DataImportBusiDAO.getContrastValue(expsys,
				pk_bdinfo, orivalue, tVO);
		if (trasferData == null) {
			canRefValue = false;
			FieldPropVO propVo = new FieldPropVO();
			propVo.setFail(false);
			propVo.setDef_expsys(expsys);
			propVo.setFieldValue(orivalue);
			propVo.setPk_bdinfo(pk_bdinfo);
			propVo.setFieldName(fldKey);
			propVo.setPk_org(tVO.getPk_org());
			propVo.setPk_group(tVO.getPk_group());
			propVo.setPk_org_v(tVO.getPk_org_v());
			rowMap.put(fldKey, propVo);
		} else {
			rowMap.put(fldKey, trasferData);
		}
		return canRefValue;
	}

	/**
	 * ��ʼ��ҵ�����ݻ���
	 * 
	 * @param result
	 */
	@SuppressWarnings({ "rawtypes" })
	private void initBusiDataCache(Map<String, List> result) {
		List hLst = result.get(SystemConst.HEAD);
		List bLst = result.get(SystemConst.BODY);
		// �����洦��
		initBusiDataCacheByLst(hLst, SystemConst.HEAD);
		// �ӱ��洦��
		initBusiDataCacheByLst(bLst, SystemConst.BODY);
	}

	/**
	 * 
	 * @param dataLst
	 *            ��ʽ��0 List<������> 1 ���� 2 MAP< �ֶ� ,λ��>
	 * @param tabNo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initBusiDataCacheByLst(List dataLst, String tabNo) {
		if (dataLst != null && dataLst.size() > 0) {
			List<Map<String, Object>> lst = _busi_DataMapLst.get(tabNo);
			if (lst == null || lst.size() == 0) {
				lst = new ArrayList();
			} else {
				lst.clear();
			}
			List busiData = (List) dataLst.get(0);// ����List ��������
			dataLst.get(1);
			Map fldMap = (Map) dataLst.get(2);// �ֶ�MAP<KEY,POSI>

			if (busiData != null && busiData.size() > 0) {
				int busiSize = busiData.size();
				for (int i = 0; i < busiSize; i++) {
					List rowData = (List) busiData.get(i);
					Map fldV = new HashMap();
					Set<String> keySet = fldMap.keySet();
					Iterator<String> itKey = keySet.iterator();
					while (itKey.hasNext()) {
						String key = itKey.next();
						String posiVal = fldMap.get(key).toString();
						String mapField = key.substring(0, key.length()
								- posiVal.length());
						fldV.put(mapField,
								rowData.get(Integer.parseInt(posiVal)));

					}
					lst.add(fldV);
				}
			}
			_busi_DataMapLst.put(tabNo, lst);
			_title_Map.put(tabNo, fldMap);
		}

	}

	/**
	 * ��ʼ�������ֶζ��չ�ϵ��
	 * 
	 * @param aggDataImportVO
	 * @param hm_info
	 */
	@SuppressWarnings({ "rawtypes" })
	private void initMetaDataCache(AggDataImportVO aggDataImportVO, Map hm_info) {
		Map<String, Object> _tMap = new HashMap<String, Object>();
		Map<String, Object> _h_n_Map = new HashMap<String, Object>();
		Map<String, Object> _b_n_Map = new HashMap<String, Object>();
		Map<String, Object> _h_un_Map = new HashMap<String, Object>();
		Map<String, Object> _b_un_Map = new HashMap<String, Object>();
		DataImportVO headVO = aggDataImportVO.getParentVO();
		DataImportBVO1[] bvo1 = (DataImportBVO1[]) aggDataImportVO
				.getChildren(DataImportBVO1.class);
		DataImportBVO2[] bvo2 = (DataImportBVO2[]) aggDataImportVO
				.getChildren(DataImportBVO2.class);

		_tMap.put(SystemConst.TOP, headVO);// ������Ϣά��

		if (bvo1 != null && bvo1.length > 0) {
			int bvo1Len = bvo1.length;
			for (int i = 0; i < bvo1Len; i++) {
				if (bvo1[i].getDs_field() != null
						&& !"".equals(bvo1[i].getDs_field())) {
					_h_n_Map.put(bvo1[i].getAim_field(), bvo1[i]);
					_h_un_Map.put(bvo1[i].getDs_field(), bvo1[i]);
				}
			}
		}

		if (bvo2 != null && bvo2.length > 0) {
			int bvo2Len = bvo2.length;
			for (int i = 0; i < bvo2Len; i++) {
				if (bvo2[i].getDs_field() != null
						&& !"".equals(bvo2[i].getDs_field())) {
					_b_n_Map.put(bvo2[i].getAim_field(), bvo2[i]);
					_b_un_Map.put(bvo2[i].getDs_field(), bvo2[i]);
				}
			}
		}

		_normal_MetaData_Cache.put(SystemConst.TOP, _tMap);
		_normal_MetaData_Cache.put(SystemConst.HEAD, _h_n_Map);
		_normal_MetaData_Cache.put(SystemConst.BODY, _b_n_Map);

		_unNormal_MetaData_Cache.put(SystemConst.TOP, _tMap);
		_unNormal_MetaData_Cache.put(SystemConst.HEAD, _h_un_Map);
		_unNormal_MetaData_Cache.put(SystemConst.BODY, _b_un_Map);

	}

	public Map<String, Map<String, Object>> get_unNormal_MetaData_Cache() {
		return _unNormal_MetaData_Cache;
	}

	/**
	 * ���ض��ղ��ϵ����ݡ�
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, List<Map<String, FieldPropVO>>> getFailRefData()
			throws BusinessException {
		return _fail_DataMapLst;
	}

	public Map<String, List<Map<String, Object>>> getSuccessBusiData() {

		return _success_DataMapLst;

	}

	public Map<String, List<Map<String, Object>>> getOriBusiCacheData() {
		return _busi_DataMapLst;
	}

	@SuppressWarnings("rawtypes")
	public Map getTitleCacheMap() {
		return _title_Map;
	}

	public void clear() {
		String currentUser = InvocationInfoProxy.getInstance().getUserCode();
		DataImportCacheHdl cacheInstance = _user_Cache_Instance
				.get(currentUser);
		if (cacheInstance != null) {
			cacheInstance = null;
			_user_Cache_Instance.put(currentUser, cacheInstance);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setHdlCacheData(Map dataMap) throws BusinessException {
		List headLst = (List) dataMap.get(SystemConst.HEAD);
		// ������ɺ���Ҫ�ٴ���һ�ι�ʽ����Ϊ����ʽ�õ����յ�PKʱ��û����ǰ��ʽ�޷���ȡֵΪ�գ����պ�������ݣ�������ִ��һ�ι�ʽ��
		List bodyLst = (List) dataMap.get(SystemConst.BODY);
		// ͬ�ϣ�ͬ����Ҫִ��һ�ι�ʽ����
		List _successHeadLst = _success_DataMapLst.get(SystemConst.HEAD);
		List _successBodyLst = _success_DataMapLst.get(SystemConst.BODY);
		if (headLst != null && headLst.size() > 0) {
			int headSize = headLst.size();
			for (int i = 0; i < headSize; i++) {
				_successHeadLst.add(headLst.get(i));
			}
		}
		if (bodyLst != null && bodyLst.size() > 0) {
			int bodySize = bodyLst.size();
			for (int i = 0; i < bodySize; i++) {
				_successBodyLst.add(bodyLst.get(i));
			}
		}
	}

	private void initOrgInfo() {

		try {
			String pk_group = AppContextUtil.getPk_group();
			String pk_org = AppContextUtil.getPk_org();
			String pk_book = AppContextUtil.getDefaultOrgBook();
			UFDate busiDate = AppContext.getInstance().getBusiDate();
			Map<String, String> map = NCLocator
					.getInstance()
					.lookup(IOrgUnitPubService.class)
					.getNewVIDSByOrgIDSAndDate(new String[] { pk_org },
							busiDate);
			String pk_org_v = map.get(pk_org);
			orgInfoMap.put(SystemConst.ORG_PK_ORG, pk_org);
			orgInfoMap.put(SystemConst.ORG_PK_GLORGBOOK, pk_book);
			orgInfoMap.put(SystemConst.ORG_PK_ORG_V, pk_org_v);
			orgInfoMap.put(SystemConst.ORG_PK_GROUP, pk_group);
		} catch (BusinessException e) {
			throw new RuntimeException(e);
		}
	}
}
