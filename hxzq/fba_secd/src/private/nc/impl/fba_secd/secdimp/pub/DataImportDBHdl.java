package nc.impl.fba_secd.secdimp.pub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.taglibs.standard.tag.el.fmt.ParseDateTag;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.itf.fba_secd.secdimp.pub.IDataImportBusiService;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IComponent;
import nc.md.model.access.javamap.AggVOStyle;
import nc.md.model.impl.BusinessEntity;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pubitf.org.IOrgUnitPubService;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.fba_sec.pub.PendingBillVO;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO1;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO2;
import nc.vo.fba_secd.secdimp.dataimport.DataImportVO;
import nc.vo.fba_secd.secdimp.importproj.ImportProjVO;
import nc.vo.fba_secd.secdimp.pub.AppContextUtil;
import nc.vo.fba_secd.secdimp.pub.SystemConst;
import nc.vo.fi.rateconfig.RatePeriodVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.tool.performance.DeepCloneTool;
import nc.vo.tmpub.ia.InterestVO;

public class DataImportDBHdl {

	private Map<String, String> orgInfoMap = new HashMap<String, String>();

	private IComponent clsComp = null;

	/**
	 * ���ݽ������ļ���������Դ �õ�����ȷ�����ݣ������Ӧ��VO��
	 * 
	 * @param successDataLst
	 * @param unNormalMetaDataCache
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "rawtypes" })
	public List constructImpVO(Map successDataLst, Map unNormalMetaDataCache)
			throws BusinessException {
		List saveVOS = new ArrayList();
		DataImportVO importVO = (DataImportVO) (((Map) unNormalMetaDataCache
				.get(SystemConst.TOP)).get(SystemConst.TOP));
		String pk_metadata = importVO.getPk_metadata();
		String relevaFld = importVO.getRelevafieldname();
		initOrgInfo();
		// ����Ԫ���ݲ�ѯ��Ӧ��class
		IComponent clsComp = MDBaseQueryFacade.getInstance()
				.getComponentByTypeID(pk_metadata);
		// 2015��6��11�� ��ֵ
		this.clsComp = clsComp;

		List cmpLst = clsComp.getBusinessEntities();
		if (cmpLst != null && cmpLst.size() > 1) {
			// ˵�������ӱ��ʽ
			// ȡ�ۺ�VO
			handleAggVO(successDataLst, saveVOS, relevaFld, cmpLst,
					unNormalMetaDataCache);
			if (genBillNo(unNormalMetaDataCache)) {
				int voSize = saveVOS.size();
				if (voSize > 0) {
					AbstractBill vo = (AbstractBill) saveVOS.get(0);
					SuperVO spvo = (SuperVO) vo.getParentVO();
					String billtypecode = (String) spvo
							.getAttributeValue("billtypecode");
					if (billtypecode != null) {
						String pk_group = (String) spvo
								.getAttributeValue("pk_group");
						String pk_org = (String) spvo
								.getAttributeValue("pk_org");
						IBillcodeManage codeManagerService = NCLocator
								.getInstance().lookup(IBillcodeManage.class);
						String[] codeArr = codeManagerService
								.getBatchBillCodes_RequiresNew(billtypecode,
										pk_group, pk_org, spvo, voSize);
						for (int i = 0; i < voSize; i++) {
							AbstractBill aggvo = (AbstractBill) saveVOS.get(i);
							SuperVO svo = (SuperVO) aggvo.getParentVO();
							svo.setAttributeValue("billno", codeArr[i]);
						}
					}
				}
			}

		} else if (cmpLst != null && cmpLst.size() == 1) {
			// �������ʽ
			handleSingleVO(successDataLst, saveVOS, cmpLst,
					unNormalMetaDataCache);
			if (genBillNo(unNormalMetaDataCache) || isSfsModule()) { // ����ģ�飬Ҫ�����õ��ݺ�Ҳ���ɵ��ݺţ�
																		// ����ˣ�caofr
				int voSize = saveVOS.size();
				if (voSize > 0) {
					SuperVO vo = (SuperVO) saveVOS.get(0);
					String billtypecode = (String) vo
							.getAttributeValue("billtypecode");
					if (billtypecode != null) {
						String pk_group = (String) vo
								.getAttributeValue("pk_group");
						String pk_org = (String) vo.getAttributeValue("pk_org");
						IBillcodeManage codeManagerService = NCLocator
								.getInstance().lookup(IBillcodeManage.class);
						String[] codeArr = codeManagerService
								.getBatchBillCodes_RequiresNew(billtypecode,
										pk_group, pk_org, vo, voSize);
						for (int i = 0; i < voSize; i++) {
							SuperVO svo = (SuperVO) saveVOS.get(i);
							svo.setAttributeValue("billno", codeArr[i]);
						}
					}
				}
			}
		} else {
			throw new BusinessException("����Ԫ�����޷��ҵ���Ӧ��VO��.");
		}
		return saveVOS;
	}

	public List constructImpVOHead(Map successDataLst, Map unNormalMetaDataCache)
			throws BusinessException {
		List saveVOS = new ArrayList();
		DataImportVO importVO = (DataImportVO) (((Map) unNormalMetaDataCache
				.get(SystemConst.TOP)).get(SystemConst.TOP));
		String pk_metadata = importVO.getPk_metadata();
		// ͨ��Ʊ�������ֶ�ƥ��ǿ�л��ֵ����ӱ� -zq
		String relevaFld = "Ʊ������";
		initOrgInfo();
		// ����Ԫ���ݲ�ѯ��Ӧ��class
		IComponent clsComp = MDBaseQueryFacade.getInstance()
				.getComponentByTypeID(pk_metadata);
		// 2015��6��11�� ��ֵ
		this.clsComp = clsComp;

		List cmpLst = clsComp.getBusinessEntities();
		if (cmpLst != null && cmpLst.size() > 1) {
			// ˵�������ӱ��ʽ
			// ȡ�ۺ�VO
			handleAggVOHead(successDataLst, saveVOS, relevaFld, cmpLst,
					unNormalMetaDataCache);
			if (genBillNo(unNormalMetaDataCache)) {
				int voSize = saveVOS.size();
				if (voSize > 0) {
					AbstractBill vo = (AbstractBill) saveVOS.get(0);
					SuperVO spvo = (SuperVO) vo.getParentVO();
					String billtypecode = (String) spvo
							.getAttributeValue("billtypecode");
					if (billtypecode != null) {
						String pk_group = (String) spvo
								.getAttributeValue("pk_group");
						String pk_org = (String) spvo
								.getAttributeValue("pk_org");
						IBillcodeManage codeManagerService = NCLocator
								.getInstance().lookup(IBillcodeManage.class);
						String[] codeArr = codeManagerService
								.getBatchBillCodes_RequiresNew(billtypecode,
										pk_group, pk_org, spvo, voSize);
						for (int i = 0; i < voSize; i++) {
							AbstractBill aggvo = (AbstractBill) saveVOS.get(i);
							SuperVO svo = (SuperVO) aggvo.getParentVO();
							svo.setAttributeValue("billno", codeArr[i]);
						}
					}
				}
			}

		} else if (cmpLst != null && cmpLst.size() == 1) {
			// �������ʽ
			handleSingleVO(successDataLst, saveVOS, cmpLst,
					unNormalMetaDataCache);
			if (genBillNo(unNormalMetaDataCache) || isSfsModule()) { // ����ģ�飬Ҫ�����õ��ݺ�Ҳ���ɵ��ݺţ�
																		// ����ˣ�caofr
				int voSize = saveVOS.size();
				if (voSize > 0) {
					SuperVO vo = (SuperVO) saveVOS.get(0);
					String billtypecode = (String) vo
							.getAttributeValue("billtypecode");
					if (billtypecode != null) {
						String pk_group = (String) vo
								.getAttributeValue("pk_group");
						String pk_org = (String) vo.getAttributeValue("pk_org");
						IBillcodeManage codeManagerService = NCLocator
								.getInstance().lookup(IBillcodeManage.class);
						String[] codeArr = codeManagerService
								.getBatchBillCodes_RequiresNew(billtypecode,
										pk_group, pk_org, vo, voSize);
						for (int i = 0; i < voSize; i++) {
							SuperVO svo = (SuperVO) saveVOS.get(i);
							svo.setAttributeValue("billno", codeArr[i]);
						}
					}
				}
			}
		} else {
			throw new BusinessException("����Ԫ�����޷��ҵ���Ӧ��VO��.");
		}
		return saveVOS;
	}

	private boolean genBillNo(Map unNormalMetaDataCache) {
		Map fldMap = (Map) unNormalMetaDataCache.get(SystemConst.HEAD);
		Set keySet = fldMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object obj = fldMap.get(key);
			if (obj != null) {
				DataImportBVO1 bvo1 = (DataImportBVO1) obj;
				if (bvo1.getDefaultvalue() != null
						&& SystemConst.DEFAULT_BILLNO.equals(bvo1
								.getDefaultvalue())) {
					return true;
				}
			}
		}
		return false;
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

	/**
	 * ���ӱ����� AGGVO ������װ
	 * 
	 * @param successDataLst
	 * @param saveVOS
	 * @param relevaFld
	 * @param cmpLst
	 * @param unNormalMetaDataCache
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleAggVO(Map successDataLst, List saveVOS,
			String relevaFld, List cmpLst, Map unNormalMetaDataCache)
			throws BusinessException {

		AggVOStyle as = null;
		String aggVOName = null;
		String headClassName = null;
		String bodyClassName = null;
		Class aggCls = null;
		Class headCls = null;
		Class bodyCls = null;
		AbstractBill aggVO = null;
		SuperVO headVO = null;
		SuperVO bodyVO = null;
		Map metaHeadMap = (Map) unNormalMetaDataCache.get(SystemConst.HEAD);
		Map metaBodyMap = (Map) unNormalMetaDataCache.get(SystemConst.BODY);
		BusinessEntity be0 = (BusinessEntity) cmpLst.get(0);
		BusinessEntity be1 = (BusinessEntity) cmpLst.get(1);
		if (be0.isPrimary()) {
			as = (AggVOStyle) be0.getBeanStyle();
			aggVOName = as.getAggVOClassName();
			headClassName = be0.getFullClassName();
			bodyClassName = be1.getFullClassName();
		} else {
			as = (AggVOStyle) be1.getBeanStyle();
			aggVOName = as.getAggVOClassName();
			headClassName = be1.getFullClassName();
			bodyClassName = be0.getFullClassName();
		}

		try {
			aggCls = Class.forName(aggVOName);
			headCls = Class.forName(headClassName);
			bodyCls = Class.forName(bodyClassName);

		} catch (ClassNotFoundException e) {
			throw new BusinessException("û���ҵ���Ӧ��VO��" + e);
		}

		List headLst = (List) successDataLst.get(SystemConst.HEAD);

		List bodyLst = (List) successDataLst.get(SystemConst.BODY);

		if (headLst != null && headLst.size() > 0) {
			int headSize = headLst.size();
			try {
				for (int i = 0; i < headSize; i++) {
					aggVO = (AbstractBill) aggCls.newInstance();
					headVO = (SuperVO) headCls.newInstance();
					List bodyVOS = new ArrayList();
					Map rowMap = (Map) headLst.get(i);
					Set keySet = rowMap.keySet();
					Iterator keyIt = keySet.iterator();
					while (keyIt.hasNext()) {
						String key = (String) keyIt.next();
						// ��ת�� NC��ӦVO�е��ֶΡ�
						if (rowMap.get(key) == null) {
							continue;
						}
						String headRowFldVal = rowMap.get(key).toString();
						if (metaHeadMap.get(key) != null) {
							String trueKey = ((SuperVO) metaHeadMap.get(key))
									.getAttributeValue(DataImportBVO1.AIM_FIELD)
									.toString();
							Object dataTypeObj = ((SuperVO) metaHeadMap
									.get(key))
									.getAttributeValue(DataImportBVO1.DATATYPE);
							String dataType = null;
							if (dataTypeObj != null) {
								dataType = ((SuperVO) metaHeadMap.get(key))
										.getAttributeValue(
												DataImportBVO1.DATATYPE)
										.toString();
							}
							Object fldVal = DataImportCommonUtil
									.handlerDataByType(rowMap.get(key),
											dataType);

							headVO.setAttributeValue(trueKey, fldVal);
						}

						// ��������ӱ��룬���ݹ����ֶν����ж�
						if (relevaFld != null && relevaFld.equals(key)) {
							int bodySize = bodyLst.size();
							for (int j = bodySize - 1; j >= 0; j--) {
								Map bodyRowMap = (Map) bodyLst.get(j);
								/**
								 * =======�������Ͳ�ƥ�䣬����.toString QianShuang
								 * 20161128 ===
								 */
								if (headRowFldVal.equals(bodyRowMap.get(key)
										.toString())) {
									// ��Ҫ�����ӱ�VO����
									// ��ʵ�����ӱ�VO
									bodyVO = (SuperVO) bodyCls.newInstance();
									// Map bodyMap = (Map) bodyLst.get(j);
									Set bodyKeySet = bodyRowMap.keySet();
									Iterator bodyKeyIt = bodyKeySet.iterator();
									while (bodyKeyIt.hasNext()) {
										String bodyFldKey = (String) bodyKeyIt
												.next();
										// ��ת�� NC��ӦVO�е��ֶΡ�
										if (metaBodyMap.get(bodyFldKey) != null) {
											// String trueBodyKey =
											// ((SuperVO)metaBodyMap.get(bodyFldKey)).getAttributeValue(DataImportBVO2.AIM_FIELD).toString();
											// bodyVO.setAttributeValue(trueBodyKey,
											// bodyRowMap.get(bodyFldKey));
											String trueKey = ((SuperVO) metaBodyMap
													.get(bodyFldKey))
													.getAttributeValue(
															DataImportBVO2.AIM_FIELD)
													.toString();
											Object dataTypeObj = ((SuperVO) metaBodyMap
													.get(bodyFldKey))
													.getAttributeValue(DataImportBVO2.DATATYPE);
											String dataType = null;
											if (dataTypeObj != null) {
												dataType = ((SuperVO) metaBodyMap
														.get(bodyFldKey))
														.getAttributeValue(
																DataImportBVO2.DATATYPE)
														.toString();
											}
											Object fldVal = DataImportCommonUtil
													.handlerDataByType(
															bodyRowMap
																	.get(bodyFldKey),
															dataType);
											bodyVO.setAttributeValue(trueKey,
													fldVal);
										}
									}
									fillCommDefaultValForVO(bodyVO);
									bodyLst.remove(j);// ������ɾ����
									bodyVOS.add(bodyVO);
								}
							}
						}
					}
					fillCommDefaultValForVO(headVO);
					fillHeadDefaultValForVO(headVO);
					SuperVO[] cavos = (SuperVO[]) bodyVOS
							.toArray(new SuperVO[] {});
					aggVO.setParent(headVO);
					aggVO.setChildrenVO(cavos);
					saveVOS.add(aggVO);
				}
			} catch (InstantiationException e) {
				throw new BusinessException("ʵ����VO�쳣" + e);
			} catch (IllegalAccessException e) {
				throw new BusinessException(e);
			}

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleAggVOHead(Map successDataLst, List saveVOS,
			String relevaFld, List cmpLst, Map unNormalMetaDataCache)
			throws BusinessException {

		AggVOStyle as = null;
		String aggVOName = null;
		String headClassName = null;
		String bodyClassName = null;
		Class aggCls = null;
		Class headCls = null;
		Class bodyCls = null;
		AbstractBill aggVO = null;
		SuperVO headVO = null;
		SuperVO bodyVO = null;
		Map metaHeadMap = (Map) unNormalMetaDataCache.get(SystemConst.HEAD);
		Map metaBodyMap = (Map) unNormalMetaDataCache.get(SystemConst.BODY);
		BusinessEntity be0 = (BusinessEntity) cmpLst.get(0);
		BusinessEntity be1 = (BusinessEntity) cmpLst.get(1);
		if (be0.isPrimary()) {
			as = (AggVOStyle) be0.getBeanStyle();
			aggVOName = as.getAggVOClassName();
			headClassName = be0.getFullClassName();
			bodyClassName = be1.getFullClassName();
		} else {
			as = (AggVOStyle) be1.getBeanStyle();
			aggVOName = as.getAggVOClassName();
			headClassName = be1.getFullClassName();
			bodyClassName = be0.getFullClassName();
		}

		try {
			aggCls = Class.forName(aggVOName);
			headCls = Class.forName(headClassName);
			bodyCls = Class.forName(bodyClassName);

		} catch (ClassNotFoundException e) {
			throw new BusinessException("û���ҵ���Ӧ��VO��" + e);
		}

		List headLst = (List) successDataLst.get(SystemConst.HEAD);

		List bodyLst = (List) successDataLst.get(SystemConst.BODY);

		if (headLst != null && headLst.size() > 0) {
			int headSize = headLst.size();
			try {
				for (int i = 0; i < headSize; i++) {
					aggVO = (AbstractBill) aggCls.newInstance();
					headVO = (SuperVO) headCls.newInstance();
					List bodyVOS = new ArrayList();
					Map rowMap = (Map) headLst.get(i);
					Set keySet = rowMap.keySet();
					Iterator keyIt = keySet.iterator();
					while (keyIt.hasNext()) {
						String key = (String) keyIt.next();
						// ��ת�� NC��ӦVO�е��ֶΡ�
						if (rowMap.get(key) == null) {
							continue;
						}
						String headRowFldVal = rowMap.get(key).toString();
						if (metaHeadMap.get(key) != null) {
							String trueKey = ((SuperVO) metaHeadMap.get(key))
									.getAttributeValue(DataImportBVO1.AIM_FIELD)
									.toString();
							Object dataTypeObj = ((SuperVO) metaHeadMap
									.get(key))
									.getAttributeValue(DataImportBVO1.DATATYPE);
							String dataType = null;
							if (dataTypeObj != null) {
								dataType = ((SuperVO) metaHeadMap.get(key))
										.getAttributeValue(
												DataImportBVO1.DATATYPE)
										.toString();
							}
							Object fldVal = DataImportCommonUtil
									.handlerDataByType(rowMap.get(key),
											dataType);

							headVO.setAttributeValue(trueKey, fldVal);
						}

						// ��������ӱ��룬���ݹ����ֶν����ж�
						if (relevaFld != null && relevaFld.equals(key)) {
							Map bodyRowMap = (Map) bodyLst.get(i);
							/**
							 * =======�������Ͳ�ƥ�䣬����.toString QianShuang 20161128
							 * ===
							 */
							if (headRowFldVal.equals(bodyRowMap.get(key)
									.toString())) {
								// ��Ҫ�����ӱ�VO����
								// ��ʵ�����ӱ�VO
								bodyVO = (SuperVO) bodyCls.newInstance();
								// Map bodyMap = (Map) bodyLst.get(j);
								Set bodyKeySet = bodyRowMap.keySet();
								Iterator bodyKeyIt = bodyKeySet.iterator();
								while (bodyKeyIt.hasNext()) {
									String bodyFldKey = (String) bodyKeyIt
											.next();
									// ��ת�� NC��ӦVO�е��ֶΡ�
									if (metaBodyMap.get(bodyFldKey) != null) {
										// String trueBodyKey =
										// ((SuperVO)metaBodyMap.get(bodyFldKey)).getAttributeValue(DataImportBVO2.AIM_FIELD).toString();
										// bodyVO.setAttributeValue(trueBodyKey,
										// bodyRowMap.get(bodyFldKey));
										String trueKey = ((SuperVO) metaBodyMap
												.get(bodyFldKey))
												.getAttributeValue(
														DataImportBVO2.AIM_FIELD)
												.toString();
										Object dataTypeObj = ((SuperVO) metaBodyMap
												.get(bodyFldKey))
												.getAttributeValue(DataImportBVO2.DATATYPE);
										String dataType = null;
										if (dataTypeObj != null) {
											dataType = ((SuperVO) metaBodyMap
													.get(bodyFldKey))
													.getAttributeValue(
															DataImportBVO2.DATATYPE)
													.toString();
										}
										Object fldVal = DataImportCommonUtil
												.handlerDataByType(bodyRowMap
														.get(bodyFldKey),
														dataType);
										bodyVO.setAttributeValue(trueKey,
												fldVal);
									}
								}
								fillCommDefaultValForVO(bodyVO);
								bodyVOS.add(bodyVO);
							}

						}
					}
					fillCommDefaultValForVO(headVO);
					fillHeadDefaultValForVO(headVO);
					SuperVO[] cavos = (SuperVO[]) bodyVOS
							.toArray(new SuperVO[] {});
					aggVO.setParent(headVO);
					aggVO.setChildrenVO(cavos);
					saveVOS.add(aggVO);
				}
			} catch (InstantiationException e) {
				throw new BusinessException("ʵ����VO�쳣" + e);
			} catch (IllegalAccessException e) {
				throw new BusinessException(e);
			}

		}
	}

	/**
	 * ��������Ҫ�����MAP�е����ݣ���ϳɶ�Ӧ��VO���顣(��������)
	 * 
	 * @param successDataLst
	 * @param saveVOS
	 * @param cmpLst
	 * @param unNormalMetaDataCache
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleSingleVO(Map successDataLst, List saveVOS, List cmpLst,
			Map unNormalMetaDataCache) throws BusinessException {

		Map metaMap = (Map) unNormalMetaDataCache.get(SystemConst.HEAD);

		BusinessEntity be0 = (BusinessEntity) cmpLst.get(0);
		String headClass = null;
		SuperVO headvo = null;
		headClass = be0.getFullClassName();
		Class headCls = null;
		try {
			headCls = Class.forName(headClass);
		} catch (ClassNotFoundException e) {
			throw new BusinessException("�޷��ҵ���Ӧ��VO��" + e);
		}
		List headLst = (List) successDataLst.get(SystemConst.HEAD);
		if (headLst != null && headLst.size() > 0) {
			int headSize = headLst.size();
			try {
				for (int i = 0; i < headSize; i++) {
					headvo = (SuperVO) headCls.newInstance();
					Map rowMap = (Map) headLst.get(i);
					Set keySet = rowMap.keySet();
					Iterator keyIt = keySet.iterator();
					while (keyIt.hasNext()) {
						String key = (String) keyIt.next();
						if (metaMap.get(key) != null) {
							String trueKey = ((SuperVO) metaMap.get(key))
									.getAttributeValue(DataImportBVO1.AIM_FIELD)
									.toString();
							Object dataTypeObj = ((SuperVO) metaMap.get(key))
									.getAttributeValue(DataImportBVO1.DATATYPE);
							String dataType = null;
							if (dataTypeObj != null) {
								dataType = ((SuperVO) metaMap.get(key))
										.getAttributeValue(
												DataImportBVO1.DATATYPE)
										.toString();
							}
							Object fldVal = DataImportCommonUtil
									.handlerDataByType(rowMap.get(key),
											dataType);
							headvo.setAttributeValue(trueKey, fldVal);
						}
					}
					fillCommDefaultValForVO(headvo);
					fillHeadDefaultValForVO(headvo);
					saveVOS.add(headvo);
				}
			} catch (InstantiationException e) {
				throw new BusinessException("ʵ����VOʱ�쳣" + e);
			} catch (IllegalAccessException e) {
				throw new BusinessException(e);
			}
		}
	}

	private void fillCommDefaultValForVO(SuperVO supervo) {
		supervo.setAttributeValue("dr", new Integer(0));
	}

	private void fillHeadDefaultValForVO(SuperVO supervo) {

		if (supervo.getAttributeValue("transtypecode") != null
				|| supervo.getAttributeValue("billtypecode") != null) {
			String pk_transtype = null;
			if (supervo.getAttributeValue("transtypecode") != null) {
				pk_transtype = (String) supervo
						.getAttributeValue("transtypecode");
			} else {
				if (supervo.getAttributeValue("billtypecode") != null) {
					pk_transtype = (String) supervo
							.getAttributeValue("billtypecode");
				}
			}
			if (pk_transtype != null) {
				pk_transtype = pk_transtype.trim();
			}
			BilltypeVO bTypeVO = PfDataCache.getBillType(pk_transtype);
			supervo.setAttributeValue("transtypecode", pk_transtype);
			if (bTypeVO != null) {
				supervo.setAttributeValue("pk_transtype",
						bTypeVO.getPk_billtypeid());
				supervo.setAttributeValue("billtypecode",
						bTypeVO.getParentbilltype() == null ? pk_transtype
								: bTypeVO.getParentbilltype());
				String billtypecode = bTypeVO.getParentbilltype() == null ? pk_transtype
						: bTypeVO.getParentbilltype();
				BilltypeVO billTypeVO = PfDataCache.getBillType(billtypecode);
				supervo.setAttributeValue("pk_billtype",
						billTypeVO.getPk_billtypeid());
			}
		}
		// 2015��6��11�� �ڻ�ģ�� �˴���Ӧ��д����Ӧ��ȡbmf�ӿ�������Ϣ,ֹͣ��ҵ��ģ������� start
		if (clsComp != null) {
			if (isSfsModule()) {
				if (supervo.getAttributeValue("billstate") == null) {
					supervo.setAttributeValue("billstate", 0);
				}
				if (supervo.getAttributeValue("billdate") == null) {// ��������
																	// 2015��6��23��
					supervo.setAttributeValue("billdate", AppContext
							.getInstance().getServerTime());
				}
			}
		}
		// 2015��6��11�� end
		supervo.setAttributeValue("state", 0);
		supervo.setAttributeValue("approvestatus", -1);

		if (supervo.getAttributeValue(SystemConst.ORG_PK_GROUP) == null) {
			supervo.setAttributeValue(SystemConst.ORG_PK_GROUP,
					orgInfoMap.get(SystemConst.ORG_PK_GROUP));
		}
		if (supervo.getAttributeValue(SystemConst.ORG_PK_ORG) == null) {
			/**
			 * JINGQT ����ABU������⣺������ͽ��������룬���һ���Ͳ��������µ���BUG����������ԭ����Ϊ֤ȯ��ȫ�ֵ�����
			 * ��������ORG���õ���һ�㼯�ŵ��� ADD START
			 */
			// �����֤ȯ�������빦��
			if (supervo instanceof SecuritiesVO) {
				supervo.setAttributeValue(SystemConst.ORG_PK_ORG,
						SystemConst.GLOBAL_ORG);
			} else {
				/**
				 * JINGQT ����ABU������⣺������ͽ��������룬���һ���Ͳ��������µ���BUG����������ԭ����Ϊ֤ȯ��ȫ�ֵ�����
				 * ��������ORG���õ���һ�㼯�ŵ��� ADD END
				 */
				supervo.setAttributeValue(SystemConst.ORG_PK_ORG,
						orgInfoMap.get(SystemConst.ORG_PK_ORG));
			}
		}
		if (supervo.getAttributeValue(SystemConst.ORG_PK_GLORGBOOK) == null) {
			supervo.setAttributeValue(SystemConst.ORG_PK_GLORGBOOK,
					orgInfoMap.get(SystemConst.ORG_PK_GLORGBOOK));
		}
		if (supervo.getAttributeValue(SystemConst.ORG_PK_ORG_V) == null) {
			supervo.setAttributeValue(SystemConst.ORG_PK_ORG_V,
					orgInfoMap.get(SystemConst.ORG_PK_ORG_V));
		}
		if (supervo.getAttributeValue("creator") == null) {
			AppContext context = AppContext.getInstance();
			String pk_user = context.getPkUser();
			supervo.setAttributeValue("creator", pk_user);
			if (supervo.getAttributeValue("creationtime") == null) {
				supervo.setAttributeValue("creationtime",
						context.getServerTime());
			}
		}
		if (supervo.getAttributeValue("billmaker") == null) {
			AppContext context = AppContext.getInstance();
			String pk_user = context.getPkUser();
			supervo.setAttributeValue("billmaker", pk_user);
			if (supervo.getAttributeValue("makedate") == null) {
				supervo.setAttributeValue("makedate", context.getServerTime());
			}
		}

	}

	private boolean isSfsModule() {
		return ("fba_sfs").equalsIgnoreCase(clsComp.getNamespace());
	}

	/**
	 * �������е����ݣ����������ݿ⡣
	 * 
	 * @param dataLst
	 * @param unNormalMetaDataCache
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String importToDB(List dataLst, Map unNormalMetaDataCache)
			throws BusinessException {
		Integer successNum = 0;
		Integer updateNum = 0;
		BaseDAO dao = new BaseDAO();
		Map<String, String> pendingMap = new HashMap<String, String>();
		DataImportVO importVO = (DataImportVO) (((Map) unNormalMetaDataCache
				.get(SystemConst.TOP)).get(SystemConst.TOP));
		String pk_metadata = importVO.getPk_metadata();
		String pk_importproj = importVO.getPk_importproj();
		UFBoolean isBusiBill = importVO.getIsbusibill();
		IComponent clsComp = MDBaseQueryFacade.getInstance()
				.getComponentByTypeID(pk_metadata);
		List cmpLst = clsComp.getBusinessEntities();
		ImportProjVO projvo = getImportProjByPK(pk_importproj);

		if (cmpLst != null && cmpLst.size() > 1) {
			// ˵�������ӱ��ʽ
			if (projvo != null && projvo.getImpmode() == 0) {
				// ȡ�ۺ�VO
				AbstractBill vos[] = (AbstractBill[]) dataLst
						.toArray(new AbstractBill[] {});
				nc.vo.pubapp.pattern.model.entity.bill.IBill[] bills = new BillInsert()
						.insert(vos);
				successNum = bills.length;
				if (isBusiBill != null && isBusiBill.booleanValue()) {
					initPendingMap(pendingMap);
					List<PendingBillVO> pendingLst = constructPendingBillByAggVOs(
							vos, pendingMap);
					savePendingBill(pendingLst);
				}
			} else if (projvo != null && projvo.getImpmode() == 2) {// ��д���ǲ�����������ڣ����ǣ����������������
				AbstractBill vos[] = (AbstractBill[]) dataLst
						.toArray(new AbstractBill[] {});
				int t = vos.length;
				AbstractBill originBills[] = new AbstractBill[t];
				String tablename = vos[0].getParent().getClass().getName();
				// ������ݵ������ʱ� --zq
				if (tablename == "nc.vo.fba_sim.simbs.interest.Interest") {
					for (int i = 0; i < t; i++) {
						// String temp = (String)
						// vos[i].getParent().getAttributeValue("temp");
						String code = (String) vos[i].getParent()
								.getAttributeValue("pk_securities");
						CircularlyAccessibleValueObject[] cvo = vos[i]
								.getAllChildrenVO();
						CircularlyAccessibleValueObject pvo = vos[i]
								.getParentVO();
						int m = cvo.length;
						String sql1 = "select pk_interest from sim_interest where nvl(dr,0)=0 and pk_securities = '"
								+ code + "'";
						Vector vec1 = (Vector) dao.executeQuery(sql1,
								new VectorProcessor());
						Date sdate = null;
						Date edate = null;
						int daysnum = 0;
						// ȡ���������ñ��pk
						if (vec1 != null && vec1.size() == 1) {
							String pk_interest = ((Vector) vec1.get(0)).get(0)
									.toString();
							for (int j = 0; j < m; j++) {
								String paypercent = cvo[j].getAttributeValue(
										"paypercent").toString();
								String yearrate = cvo[j].getAttributeValue(
										"year_rate").toString();
								String sd = pvo.getAttributeValue("startdate")
										.toString();
								String ed = pvo.getAttributeValue("enddate")
										.toString();
								UFDate startd = new UFDate(sd);
								UFDate endd = new UFDate(ed);
//								SimpleDateFormat sdf = new SimpleDateFormat(
//										"yyyy-MM-dd");
//
//								try {
//									sdate = sdf.parse(sd.substring(0, 10));
//									edate = sdf.parse(ed.substring(0, 10));
//
//								} catch (ParseException e) {
//									// TODO �Զ����ɵ� catch ��
//									e.printStackTrace();
//								}
//								Calendar cal = Calendar.getInstance();
//								cal.setTime(sdate);
//								long time1 = cal.getTimeInMillis();
//								cal.setTime(edate);
//								long time2 = cal.getTimeInMillis();
//								long between_days = (time2 - time1)
//										/ (1000 * 3600 * 24);
								// ʹ��UFDate�������������ȡ
								daysnum = UFDate.getDaysBetween(startd, endd) + 1;
								if (UFDate.isLeapYear(startd.getYear() + 1)
										&& startd.compareTo(new UFDate(startd.getYear()
												+ "-02-28")) > 0) {
									daysnum = 366;
								} else if (UFDate.isLeapYear(startd.getYear())
										&& startd.compareTo(new UFDate(startd.getYear()
												+ "-02-28")) < 0) {
									daysnum = 366;
								} else {
									daysnum = 365;
								}
								
								String sql2 = "select start_day from SIM_RATEPERIOD where nvl(dr,0)=0 and pk_interest = '"
										+ pk_interest + "'";
								String sql3 = "select end_day from SIM_RATEPERIOD where nvl(dr,0)=0 and pk_interest = '"
										+ pk_interest + "'";
								Vector vec2 = (Vector) dao.executeQuery(sql2,
										new VectorProcessor());
								Vector vec3 = (Vector) dao.executeQuery(sql3,
										new VectorProcessor());
								// У���ظ�
								if (CheckReapet(sd, vec2, ed, vec3)) {
									// �ظ������
									String sql4 = "update SIM_RATEPERIOD set days_num ='"
											+ daysnum
											+ "', paypercent = '"
											+ paypercent
											+ "' , year_rate ='"
											+ yearrate
											+ "',end_day ='"
											+ ed
											+ "' where start_day like '%"
											+ sd.substring(0, 10)
											+ "%' and pk_interest = '"
											+ pk_interest + "'";
									dao.executeUpdate(sql4);
									updateNum++;
								} else {
									// �����ڵ�����ֱ�����
									cvo[j].setAttributeValue("days_num",
											daysnum);
									cvo[j].setAttributeValue("pk_interest",
											pk_interest);
									dao.insertVO((SuperVO) cvo[j]);
									successNum++;
								}
							}

						} else {
							AbstractBill[] vos1 = new AbstractBill[1];
							for (int j = 0; j < vos[i].getAllChildrenVO().length; j++) {
								String sd = pvo.getAttributeValue("startdate")
										.toString();
								String ed = pvo.getAttributeValue("enddate")
										.toString();
								UFDate startd = new UFDate(sd);
								UFDate endd = new UFDate(ed);
//								SimpleDateFormat sdf = new SimpleDateFormat(
//										"yyyy-MM-dd");
//
//								try {
//									sdate = sdf.parse(sd.substring(0, 10));
//									edate = sdf.parse(ed.substring(0, 10));
//
//								} catch (ParseException e) {
//									// TODO �Զ����ɵ� catch ��
//									e.printStackTrace();
//								}
//								Calendar cal = Calendar.getInstance();
//								cal.setTime(sdate);
//								long time1 = cal.getTimeInMillis();
//								cal.setTime(edate);
//								long time2 = cal.getTimeInMillis();
//								long between_days = (time2 - time1)
//										/ (1000 * 3600 * 24);
								//ʱ�������㷨�޸ģ�����ϵͳ����
								daysnum = UFDate.getDaysBetween(startd, endd) + 1;
								if (UFDate.isLeapYear(startd.getYear() + 1)
										&& startd.compareTo(new UFDate(startd.getYear()
												+ "-02-28")) > 0) {
									daysnum = 366;
								} else if (UFDate.isLeapYear(startd.getYear())
										&& startd.compareTo(new UFDate(startd.getYear()
												+ "-02-28")) < 0) {
									daysnum = 366;
								} else {
									daysnum = 365;
								}
								vos[i].getChildrenVO()[j].setAttributeValue(
										"days_num", daysnum);
							}
							vos1[0] = vos[i];
							nc.vo.pubapp.pattern.model.entity.bill.IBill[] bills = new BillInsert()
									.insert(vos1);
							successNum++;
						}
					}
				}
			} else {
				throw new BusinessException(
						"���ӱ����ݵ��벻֧�� �ظ��������ݴ������ֵ��뷽ʽ���뵽������Ŀ���޸ĵ��뷽ʽ��");
			}

		} else if (cmpLst != null && cmpLst.size() == 1) {
			SuperVO vos[] = (SuperVO[]) dataLst.toArray(new SuperVO[] {});
			List<SuperVO> updateLst = null;
			List<SuperVO> insertLst = null;
			if (projvo != null && projvo.getImpmode() != 0) {

				if (projvo.getAttributeValue("projcode").equals("0101")
						|| projvo.getAttributeValue("projcode").equals("0114")) {
					IDataImportBusiService id = NCLocator.getInstance().lookup(
							IDataImportBusiService.class);
					for (int i = 0; i < vos.length; i++) {
						String pk_securities;
						if (vos[i].getAttributeValue("pk_securities") != null) {
							pk_securities = vos[i].getAttributeValue(
									"pk_securities").toString();
						} else
							pk_securities = "";

						String trade_date = vos[i].getAttributeValue(
								"trade_date").toString();
						Map cfmap = id.queryData(pk_securities, trade_date);
						if (cfmap != null) {
							id.delete(pk_securities, trade_date);
							dao.insertVO(vos[i]);
							updateNum++;
						} else {
							dao.insertVO(vos[i]);
							successNum++;
						}

					}
				} else {
					// �����뷽ʽ��������� ���������ظ�������ȫ�����롱 ���ַ�ʽ����Ҫ�����ݿ���в�ѯ�ȶԴ���
					Map<String, String[]> fldMap = getRepeatFldByMetaData(unNormalMetaDataCache);
					String[] repeatFld = fldMap
							.get(SystemConst.METADATE_REPEAT);
					String[] updateFld = fldMap.get(SystemConst.METADATE_COVER);
					Map<String, List<SuperVO>> rsMap = getUpdateLstByImpMode(
							vos, projvo, repeatFld, updateFld);
					if (rsMap != null) {
						updateLst = rsMap.get(SystemConst.UPDATELST);
						if (updateLst != null && updateLst.size() > 0) {
							SuperVO[] updateVOs = updateLst
									.toArray(new SuperVO[0]);
							if (updateFld != null && updateFld.length > 0) {
								new BaseDAO().updateVOArray(updateVOs,
										updateFld);
							} else {
								new BaseDAO().updateVOArray(updateVOs);
							}
							updateNum = updateVOs.length;
						}
						insertLst = rsMap.get(SystemConst.INSERTLST);
						vos = null;
						vos = insertLst.toArray(new SuperVO[0]);
					}
					Map<String, SuperVO[]> vosMap = handlerVosByPk(vos);
					if (vosMap.get(SystemConst.PKLST) != null) {// ��� ��PKֱ�� ��PK��
						String oriPks[] = new BaseDAO()
								.insertVOArrayWithPK(vosMap
										.get(SystemConst.PKLST));
						successNum += oriPks.length;
					}
					if (vosMap.get(SystemConst.NOPKLST) != null) {// ���û��PK �Զ�����
						String pks[] = new BaseDAO().insertVOArray(vosMap
								.get(SystemConst.NOPKLST));
						successNum += pks.length;
					}
					if (isBusiBill != null && isBusiBill.booleanValue()) {
						initPendingMap(pendingMap);
						List<PendingBillVO> pendingLst = constructPendingBillBySuperVOs(
								vos, pendingMap);
						savePendingBill(pendingLst);
					}
				}
			} else {
				Map<String, SuperVO[]> vosMap = handlerVosByPk(vos);
				if (vosMap.get(SystemConst.PKLST) != null) {// ��� ��PKֱ�� ��PK��
					String oriPks[] = new BaseDAO().insertVOArrayWithPK(vosMap
							.get(SystemConst.PKLST));
					successNum += oriPks.length;
				}
				if (vosMap.get(SystemConst.NOPKLST) != null) {// ���û��PK �Զ�����
					String pks[] = new BaseDAO().insertVOArray(vosMap
							.get(SystemConst.NOPKLST));
					successNum += pks.length;
				}
				if (isBusiBill != null && isBusiBill.booleanValue()) {
					initPendingMap(pendingMap);
					List<PendingBillVO> pendingLst = constructPendingBillBySuperVOs(
							vos, pendingMap);
					savePendingBill(pendingLst);
				}
			}

		}
		DataImportBusiDAO.clearContrastMap();
		return "�ɹ� ���� " + successNum + "�����ݣ��ɹ� ���� " + updateNum
				+ "�����ݣ��뵽��Ӧ�ڵ��ѯ��";
	}

	private boolean CheckReapet(String startday, Vector vec1, String endday,
			Vector vec2) {
		boolean b = false;
		// �������ݵĿ�ʼʱ��У�飬��ѯ����Ŀ�ʼʱ��
		String sd = "";
		int m = vec1.size();
		int n = vec2.size();
		if (m != n || (m <= 0 && n <= 0)) {
			return false;
		} else {
			int x = 0;
			if (m > n) {
				x = n;
			} else
				x = m;
			for (int i = 0; i < x; i++) {
				sd = ((Vector) vec1.get(i)).get(0).toString();
				// ͨ����ʼʱ�����ͬ��ȷ���Ƿ���ʱ���ӱ���
				if (startday.substring(0, 10).equals(sd.substring(0, 10))) {
					b = true;
					break;
				}
			}
		}
		return b;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, String[]> getRepeatFldByMetaData(
			Map unNormalMetaDataCache) {
		Map<String, String[]> rMap = new HashMap<String, String[]>();
		List<String> repeatLst = new ArrayList<String>();
		List<String> coverLst = new ArrayList<String>();
		// �������������Ϣ����ȡ�������ظ��ֶκ͸����ֶ�
		Map<String, Object> headMap = (Map<String, Object>) unNormalMetaDataCache
				.get(SystemConst.HEAD);
		Set keySet = headMap.keySet();
		Iterator keyIt = keySet.iterator();
		while (keyIt.hasNext()) {
			String key = (String) keyIt.next();
			Object obj = headMap.get(key);
			if (obj != null && obj instanceof DataImportBVO1) {
				DataImportBVO1 vo = (DataImportBVO1) obj;
				UFBoolean repeatVal = vo.getIf_repeat();
				UFBoolean coverVal = vo.getIf_cover();
				if (!repeatVal.booleanValue()) {
					repeatLst.add(vo.getAim_field());
				}
				if (coverVal.booleanValue()) {
					coverLst.add(vo.getAim_field());
				}
			}
		}
		String[] repeatArr = repeatLst.toArray(new String[0]);
		String[] coverArr = coverLst.toArray(new String[0]);
		rMap.put(SystemConst.METADATE_REPEAT, repeatArr);
		rMap.put(SystemConst.METADATE_COVER, coverArr);
		return rMap;
	}

	private Map<String, List<SuperVO>> getUpdateLstByImpMode(SuperVO[] vos,
			ImportProjVO projvo, String[] repeatFld, String[] updateFld)
			throws BusinessException {
		if (repeatFld == null || repeatFld.length == 0) {
			throw new BusinessException("��ǰ���뷽ʽ���������ظ��ֶβ���Ϊ��");
		}
		SuperVO baseVO = vos[0];
		repeatFld = fillFieldArrOrgFld(repeatFld, baseVO.getPKFieldName(), true);
		updateFld = fillFieldArrOrgFld(updateFld, baseVO.getPKFieldName(),
				false);
		String[] queryFldArr = new String[repeatFld.length + 1];
		fillQueryFldArrVal(repeatFld, queryFldArr, baseVO.getPKFieldName());
		int impmode = projvo.getImpmode();// 1�����������ǣ�2�������ָ���

		DataImportByImpMode dataImportByImpMode = new DataImportByImpMode();
		// �Ա����ݿ�����������Ҫ��������
		Map<String, List<SuperVO>> rsMap = dataImportByImpMode.compareData(vos,
				baseVO, queryFldArr, repeatFld, updateFld, impmode);
		return rsMap;
	}

	private String[] fillFieldArrOrgFld(String[] fieldArr, String pkFld,
			boolean orgFlag) {
		List<String> fldLst = new ArrayList<String>();
		boolean pkOrgFlag = false;
		int fldLen = fieldArr.length;
		for (int i = 0; i < fldLen; i++) {
			if (fieldArr[i].equals(pkFld)) {
				continue;
			}
			if (SystemConst.ORG_PK_ORG.equals(fieldArr[i])) {
				pkOrgFlag = true;
			}
			fldLst.add(fieldArr[i]);
		}

		if (orgFlag) {
			if (pkOrgFlag) {
				return fldLst.toArray(new String[0]);
			} else {
				fldLst.add(SystemConst.ORG_PK_ORG);
				return fldLst.toArray(new String[0]);
			}
		} else {
			return fldLst.toArray(new String[0]);
		}
	}

	private void fillQueryFldArrVal(String[] fieldArr, String[] queryFldArr,
			String pkFldName) {
		for (int i = 0; i < fieldArr.length; i++) {
			queryFldArr[i] = fieldArr[i];
		}
		queryFldArr[fieldArr.length] = pkFldName;

	}

	private void contractUpdateLst(List<SuperVO> updateLst, SuperVO vo,
			SuperVO dataBaseVO, String[] updateFld) {
		if (updateFld != null && updateFld.length > 0) {
			int fldLen = updateFld.length;
			for (int i = 0; i < fldLen; i++) {
				dataBaseVO.setAttributeValue(updateFld[i],
						vo.getAttributeValue(updateFld[i]));
			}
			updateLst.add(dataBaseVO);
		} else {
			vo.setPrimaryKey(dataBaseVO.getPrimaryKey());
			vo.setStatus(VOStatus.UPDATED);
			updateLst.add(vo);
		}
	}

	/**
	 * �������ݿ� ���� ���� ������Ŀ���õ� �Ա��ֶΣ�ȡֵ����ֵ ��ΪKEY����MAP�С�
	 * 
	 * @param dataBaseVOs
	 * @param compareKeyMap
	 * @param fieldArr
	 */
	private void initCompareKeyMap(SuperVO[] dataBaseVOs,
			Map<String, SuperVO> compareKeyMap, String[] fieldArr) {
		int voSize = dataBaseVOs.length;
		for (int i = 0; i < voSize; i++) {
			SuperVO vo = dataBaseVOs[i];
			String fldValBuf = getCompareKey(fieldArr, vo);
			compareKeyMap.put(fldValBuf.toString(), vo);
		}

	}

	private String getCompareKey(String[] fieldArr, SuperVO vo) {
		StringBuffer fldValBuf = new StringBuffer();
		int compareFldSize = fieldArr.length;
		for (int j = 0; j < compareFldSize; j++) {
			fldValBuf.append(vo.getAttributeValue(fieldArr[j]).toString());
			fldValBuf.append(SystemConst.JOINTAG);
		}
		return fldValBuf.toString();
	}

	/**
	 * ���� ������Ŀ���� ��ѯ������Ŀ��Ϣ
	 * 
	 * @param pk_importproj
	 * @return ImportProjVO
	 * @throws BusinessException
	 */
	private ImportProjVO getImportProjByPK(String pk_importproj)
			throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.retrieveByPK(ImportProjVO.class, pk_importproj);
		if (obj != null) {
			return (ImportProjVO) obj;
		} else {
			return null;
		}
	}

	private Map<String, SuperVO[]> handlerVosByPk(SuperVO[] vos) {
		List<SuperVO> pkLst = new ArrayList<SuperVO>();
		List<SuperVO> noPkLst = new ArrayList<SuperVO>();
		Map<String, SuperVO[]> retMap = new HashMap<String, SuperVO[]>();
		int vosSize = vos.length;
		for (int i = 0; i < vosSize; i++) {
			SuperVO vo = vos[i];
			if (vo.getPrimaryKey() != null) {
				pkLst.add(vo);
			} else {
				noPkLst.add(vo);
			}
		}

		if (pkLst.size() > 0) {
			SuperVO pkvos[] = pkLst.toArray(new SuperVO[0]);
			retMap.put(SystemConst.PKLST, pkvos);
		}
		if (noPkLst.size() > 0) {
			SuperVO pkvos[] = noPkLst.toArray(new SuperVO[0]);
			retMap.put(SystemConst.NOPKLST, pkvos);
		}
		return retMap;
	}

	private void initPendingMap(Map<String, String> pendingMap)
			throws BusinessException {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" isnull(dr,0)=0");
		/**
		 * YangJie2014-09-24 ȥ��state=0��
		 */
		// sqlBuf.append(" and state=0");
		Collection colData = new BaseDAO().retrieveByClause(
				PendingBillVO.class, sqlBuf.toString());
		if (colData != null && colData.size() > 0) {
			int colSize = colData.size();
			for (int i = 0; i < colSize; i++) {
				Iterator it = colData.iterator();
				while (it.hasNext()) {
					PendingBillVO vo = (PendingBillVO) it.next();
					String pk_group = vo.getPk_group();
					String pk_org = vo.getPk_org();
					String pk_glbook = vo.getPk_glorgbook();
					String pk_billtype = vo.getPk_billtype();
					UFDate trade_date = vo.getTrade_date();
					StringBuffer keyBuf = new StringBuffer();
					keyBuf.append(pk_group);
					keyBuf.append(pk_org);
					keyBuf.append(pk_glbook);
					keyBuf.append(pk_billtype);
					if (trade_date != null) {
						keyBuf.append(trade_date.toLocalString());
					}
					pendingMap.put(keyBuf.toString(), keyBuf.toString());
				}
			}
		}
	}

	private void savePendingBill(List<PendingBillVO> pendingLst)
			throws BusinessException {
		/**
		 * YangJie 2014-06-24 �ֺ췽�����⴦�� ��Ͷ�ʷֺ췽���Ͳ�һ��������ȯ�ֺ췽�� HVBU��һ��HVBV
		 */
		List<PendingBillVO> otherlist = new ArrayList<PendingBillVO>();
		DeepCloneTool deepCloneTool = new DeepCloneTool();
		for (Iterator iterator = pendingLst.iterator(); iterator.hasNext();) {
			PendingBillVO pendingBillVO = (PendingBillVO) iterator.next();
			if (pendingBillVO.getPk_billtype().equals("HVBU")) {
				PendingBillVO sbpendingBillVO = (PendingBillVO) deepCloneTool
						.deepClone(pendingBillVO);
				sbpendingBillVO.setPk_billtype("HVBV");
				otherlist.add(sbpendingBillVO);
			}
		}
		if (otherlist.size() > 0) {
			pendingLst.addAll(otherlist);
		}
		PendingBillVO[] pendingVOs = pendingLst.toArray(new PendingBillVO[] {});
		new BaseDAO().insertVOArray(pendingVOs);
	}

	private List<PendingBillVO> constructPendingBillBySuperVOs(SuperVO[] vos,
			Map<String, String> pendingMap) throws BusinessException {
		List<PendingBillVO> pendingLst = new ArrayList<PendingBillVO>();
		int vosLen = vos.length;

		for (int i = 0; i < vosLen; i++) {
			SuperVO vo = vos[i];
			getPendingBillVO(pendingLst, vo, pendingMap);
		}
		return pendingLst;
	}

	private List<PendingBillVO> constructPendingBillByAggVOs(
			AbstractBill[] vos, Map<String, String> pendingMap)
			throws BusinessException {
		List<PendingBillVO> pendingLst = new ArrayList<PendingBillVO>();
		int vosLen = vos.length;
		for (int i = 0; i < vosLen; i++) {
			SuperVO vo = (SuperVO) vos[i].getParent();
			getPendingBillVO(pendingLst, vo, pendingMap);
		}
		return pendingLst;
	}

	private void getPendingBillVO(List<PendingBillVO> pendingLst,
			SuperVO supervo, Map<String, String> pendingMap)
			throws BusinessException {
		// ���ڣ��������ͣ����š���֯���˲� �ж� �Ƿ��м���������ݡ�
		StringBuffer pendingKey = new StringBuffer();
		Object groupObj = supervo.getAttributeValue("pk_group");
		Object orgObj = supervo.getAttributeValue("pk_org");
		;
		Object glorgBookObj = supervo
				.getAttributeValue(SystemConst.ORG_PK_GLORGBOOK);
		;
		Object orgvObj = supervo.getAttributeValue(SystemConst.ORG_PK_ORG_V);
		;
		checkOrgInfo(orgObj, groupObj, glorgBookObj, orgvObj);
		String pk_group = groupObj.toString();
		pendingKey.append(pk_group);
		String pk_org = orgObj.toString();
		pendingKey.append(pk_org);
		String pk_glbook = glorgBookObj.toString();
		pendingKey.append(pk_glbook);
		String pk_org_v = orgvObj.toString();
		Object transObj = supervo.getAttributeValue("transtypecode");
		Object tdObj = supervo.getAttributeValue("trade_date");

		PendingBillVO pendVO = new PendingBillVO();
		if (transObj != null) {
			String pk_billtype = transObj.toString();
			pk_billtype = pk_billtype.trim();
			pendingKey.append(pk_billtype);
			pendVO.setPk_billtype(pk_billtype);
		}
		if (tdObj != null) {
			String trade_date = tdObj.toString();
			UFDate tradeDate = new UFDate(trade_date);
			pendingKey.append(tradeDate.toLocalString());
			pendVO.setTrade_date(tradeDate);
		}
		if (pendingMap.get(pendingKey.toString()) != null) {
			return;
		} else {
			pendingMap.put(pendingKey.toString(), pendingKey.toString());
		}
		pendVO.setPk_group(pk_group);
		pendVO.setPk_org(pk_org);
		pendVO.setState(0);
		pendVO.setPk_glorgbook(pk_glbook);
		pendVO.setPk_org_v(pk_org_v);
		pendVO.setAttributeValue("dr", 0);
		pendingLst.add(pendVO);
	}

	private void checkOrgInfo(Object orgObj, Object groupObj,
			Object glorgBookObj, Object orgvObj) throws BusinessException {
		if (orgObj == null) {
			throw new BusinessException("��֯����Ϊ��!");
		}
		if (groupObj == null) {
			throw new BusinessException("���Ų���Ϊ�գ�");
		}
		if (glorgBookObj == null) {
			throw new BusinessException("�˱�����Ϊ�գ�");
		}

		if (orgvObj == null) {
			throw new BusinessException("��֯�汾����Ϊ�գ�");
		}
	}
}
