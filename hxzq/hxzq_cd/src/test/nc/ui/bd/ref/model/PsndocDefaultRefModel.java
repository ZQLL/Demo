package nc.ui.bd.ref.model;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.util.ObjectCreator;
import nc.bs.logging.Logger;
import nc.itf.bd.pub.IBDResourceIDConst;
import nc.itf.org.IOrgResourceCodeConst;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.bd.ref.AbstractRefGridTreeBigDataModel;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.IRefDocEdit;
import nc.ui.bd.ref.IRefMaintenanceHandler;
import nc.ui.fba_scap.scaptrade.pub.PubClient;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.org.DeptVO;
import nc.vo.pub.BusinessException;
import nc.vo.util.SqlWhereUtil;

/**
 * ��Ա�������� <br>
 * <b>ע:<b> ����ʾ��˾���չ�������(������UIRefPane.setMultiCorpRef(true))������������pk_orgΪ����ĳһ��˾
 * ����ǿ缯�ŵĻ���������setIsMutiGroup(true),����Ĭ�ϵļ����ǵ�½����
 * 
 * @author jiangjuna
 * 
 */
public class PsndocDefaultRefModel extends AbstractRefGridTreeBigDataModel {
	private boolean isMutiGroup = false;

	public PsndocDefaultRefModel() {
		reset();
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void reset() {
		setRefNodeName("��Ա");/* -=notranslate=- */
		setRootName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"10140psn", "010140psn0085"));// ����
		setClassFieldCode(new String[] { DeptVO.CODE, DeptVO.NAME,
				DeptVO.PK_DEPT, DeptVO.PK_FATHERORG });
		setFatherField(DeptVO.PK_FATHERORG);
		setChildField(PsnjobVO.PK_DEPT);
		setClassJoinField(PsnjobVO.PK_DEPT);
		setClassTableName(new DeptVO().getDefaultTableName());
		setClassDefaultFieldCount(2);
		setClassDataPower(true);
		setFieldCode(new String[] { "bd_psndoc.code", "bd_psndoc.name" });
		setFieldName(new String[] {
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0065"),
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0066") });// "����", "����", "֤������",
														// "֤����"
		setDefaultFieldCount(2);
		// ���������ص�pk_dept�ֶΣ�ƥ��ʱ����ֱ��ѡ����Ӧ�����ڵ�
		setHiddenFieldCode(new String[] { "bd_psndoc.pk_psndoc",
				"bd_psnjob.pk_psnjob", "bd_psnjob.pk_dept", "bd_psndoc.idtype",
				"bd_psndoc.id" });
		setTableName("bd_psndoc left join bd_psnjob on bd_psndoc.pk_psndoc = bd_psnjob.pk_psndoc");
		setPkFieldCode("bd_psndoc.pk_psndoc");
		setDocJoinField("bd_psnjob.pk_dept");
		setRefCodeField("bd_psndoc.code");
		setRefNameField("bd_psndoc.name");
		setCommonDataBasDocTableName(new PsnjobVO().getTableName());
		setCommonDataBasDocPkField("bd_psnjob.pk_psnjob");
		setCommonDataTableName(new PsndocVO().getTableName());
		if (!isMutiGroup())
			setFilterRefNodeName(new String[] { "ҵ��Ԫ" });/* -=notranslate=- */
		else
			setFilterRefNodeName(new String[] { "����", "ҵ��Ԫ" });/*
																 * -=notranslate=
																 * -
																 */
		setResourceID(IBDResourceIDConst.PSNDOC);
		setClassResouceID(IOrgResourceCodeConst.DEPT);
		setAddEnableStateWherePart(true);
		// �����ظ�����
		setStrPatch(IRefConst.DISTINCT);
		resetFieldName();
		Hashtable content = new Hashtable();
		content.put(
				"0",
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0098"));// ���֤
		content.put(
				"1",
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0099"));// ����֤
		content.put(
				"2",
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0100"));// ����
		Hashtable convert = new Hashtable();
		convert.put("bd_psndoc.idtype", content);
		setDispConvertor(convert);
		setBlurQueryTableName("bd_psndoc");
		setRefMaintenanceHandler(new IRefMaintenanceHandler() {

			@Override
			public String[] getFucCodes() {
				return new String[] { "10140PSN" };
			}

			@Override
			public IRefDocEdit getRefDocEdit() {
				return null;
			}
		});
	}

	@Override
	protected Vector getConvertedData(boolean isDataFromCache, Vector v,
			boolean isDefConverted) {
		Vector rows = super
				.getConvertedData(isDataFromCache, v, isDefConverted);
		if (rows == null || rows.size() == 0) {
			return rows;
		}
		int pkIndex = getFieldIndex(getPkFieldCode());
		Set<String> existPkSet = new HashSet<String>();
		Vector<Vector> newRows = new Vector<Vector>();
		for (int i = 0; i < rows.size(); i++) {
			Vector row = (Vector) rows.get(i);
			if (!existPkSet.contains(row.get(pkIndex))) {
				existPkSet.add((String) row.get(pkIndex));
				newRows.add(row);
			}
		}
		return newRows;
	}

	@Override
	public void filterValueChanged(ValueChangedEvent changedValue) {
		String[] pk_orgs = (String[]) changedValue.getNewValue();
		if (pk_orgs != null && pk_orgs.length > 0 && pk_orgs[0] != null) {
			try {
				Class<?> uip = Class.forName("nc.ui.pub.beans.UIRefPane");// �����������
				Object refnodename = uip.getMethod("getRefNodeName", null)
						.invoke(changedValue.getSource(), null);
				if ("����".equals(refnodename)) {// �缯�Ų���ʱ�����ڼ��Ź���Ա��ݴ����գ�
					getFilterRefModel("ҵ��Ԫ").setPk_group(pk_orgs[0]);/*
																	 * -=notranslate
																	 * =-
																	 */
					getFilterRefModel("ҵ��Ԫ").reloadData();/* -=notranslate=- */
					setPk_org(null);
				} else {
					setPk_org(pk_orgs[0]);
				}
			} catch (ClassNotFoundException e) {
				Logger.error(e.getMessage());
			} catch (SecurityException e) {
				Logger.error(e.getMessage());
			} catch (NoSuchMethodException e) {
				Logger.error(e.getMessage());
			} catch (IllegalArgumentException e) {
				Logger.error(e.getMessage());
			} catch (IllegalAccessException e) {
				Logger.error(e.getMessage());
			} catch (InvocationTargetException e) {
				Logger.error(e.getMessage());
			}
		} else {
			if (getFilterRefModel("ҵ��Ԫ").getPkValue() != null) {/*
																 * -=notranslate=
																 * -
																 */
				getFilterRefModel("ҵ��Ԫ").setPk_group(null);/* -=notranslate=- */
			}
			setPk_org(null);
		}
	}

	@Override
	public String getClassWherePart() {
		SqlWhereUtil sw = new SqlWhereUtil(super.getClassWherePart());
		sw.and(DeptVO.PK_ORG + " = '" + getPk_org() + "' and "
				+ DeptVO.ENABLESTATE + "=2");
		return sw.getSQLWhere();
	}

	protected String getEnvWherePart() {
		/**
		 * update by lihaibo ��ѯ������Ȩ���������Ա
		 */
		String pk_user = getModelHandler().getPk_user();
		String users = null;
		try {
			Vector vec = (Vector) NCLocator
					.getInstance()
					.lookup(IUAPQueryBS.class)
					.executeQuery(
							"select h.pk_psndoc from bd_psndoc h left join sm_user g on g.pk_psndoc = h.pk_psndoc where g.cuserid  in (select distinct(c.pk_operator) from er_indauthorize c left join bd_psndoc a on a.pk_psndoc = c.pk_user left join sm_user b on a.pk_psndoc = b.pk_psndoc where b.cuserid = '"
									+ pk_user + "')", new VectorProcessor());
			if (vec != null && vec.size() > 0) {
				users = " and bd_psndoc.pk_psndoc in (";
				for (int i = 0; i < vec.size(); i++) {
					if (i < vec.size() - 1) {
						users += "'"
								+ ((Vector) (vec.get(i))).get(0).toString()
								+ "',";
					} else if (i == vec.size() - 1) {
						users += "'"
								+ ((Vector) (vec.get(i))).get(0).toString()
								+ "'";
					}
				}
				users += ")";
			} else {
				users = " and bd_psndoc.pk_psndoc = '0000'";
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		// ��������Ϣ����ְҵ��Ԫ������ʾ������
		return "bd_psnjob.pk_org = '" + getPk_org() + "'";
	}

	public boolean isMutiGroup() {
		return isMutiGroup;
	}

	public void setMutiGroup(boolean isMutiGroup) {
		this.isMutiGroup = isMutiGroup;
		if (isMutiGroup()) {
			setFilterRefNodeName(new String[] { "����", "ҵ��Ԫ" });/*
																 * -=notranslate=
																 * -
																 */
		} else {
			setFilterRefNodeName(new String[] { "ҵ��Ԫ" });/* -=notranslate=- */
		}
	}
}
