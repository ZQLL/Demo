package nc.impl.sim.iufo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.itf.fba_sec.pub.tools.StringUtil;
import nc.itf.sim.iufo.IufoQuerySIMVO;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.pub.BusinessException;
import java.util.*;

/**
 * ��������
 * 
 */
public class SIM_Analysis {
	/**
	 * ��װ����
	 * 
	 * @param simvo
	 *            �����б�
	 * @param isUserSecurities
	 *            �Ƿ���Ҫ���֤ȯ����
	 * @return
	 * @throws BusinessException
	 */
	public String bulidCondition(IufoQuerySIMVO simvo, boolean isUserSecurities)
			throws BusinessException {
		StringBuffer sf = new StringBuffer();
		sf.append(" 1 = 1 ");
		if (isUserSecurities) {
			OversightSec(simvo, sf);
		}
		OversightCap(simvo, sf);
		OversightSelf(simvo, sf);
		OversightPart(simvo, sf);
		OversightOperate(simvo, sf);
		OversightClient(simvo, sf);
		OversightClassify(simvo, sf);
		OversightStocksort(simvo, sf);
		OversightAssets(simvo, sf);
		/** ��ӱ��ֵ����� by jingqt */
		OversightCurrtype(simvo, sf);
		return sf.toString();
	}

	/**
	 * ��װ����
	 * 
	 * @param simvo
	 *            �����б�
	 * @param isUserSecurities
	 *            �Ƿ���Ҫ���֤ȯ����
	 * @return
	 * @throws BusinessException
	 */
	public String bulidConditionHYJZQM(IufoQuerySIMVO simvo)
			throws BusinessException {
		StringBuffer sf = new StringBuffer();
		sf.append(" 1 = 1 ");
		OversightCap(simvo, sf);
		OversightSec(simvo, sf);
		OversightStocksort(simvo, sf);
		/** ��ӱ��ֵ����� by jingqt */
		// OversightCurrtype(simvo, sf);
		return sf.toString();
	}

	/**
	 * ����֤ȯ����
	 */
	public String OversightSec(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_securities() != null) {
			String sql1 = " select pk_securities from sec_securities where code = '"
					+ simvo.getPk_securities() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ע��ԭ��SQL���� δ��ȷ������
				// sql = " and pk_securities = '"+id+"' ";
				sql = " and a.pk_securities = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * �����ʽ��˺�
	 */
	public String OversightCap(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_capaccount() != null) {
			String sql1 = " select pk_capaccount from sec_capaccount where code = '"
					+ simvo.getPk_capaccount()
					+ "'"
					+ " and pk_group = '"
					+ simvo.getPk_group()
					+ "' and pk_org = '"
					+ simvo.getPk_org() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ע��ԭ��SQL���� δ��ȷ������
				// sql = " and pk_capaccount = '"+id+"' ";
				sql = " and a.pk_capaccount = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * ����ҵ��С��
	 */
	public String OversightSelf(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_selfsgroup() != null) {
			String sql1 = "  select pk_selfsgroup from sec_selfsgroup where code  = '"
					+ simvo.getPk_selfsgroup()
					+ "'"
					+ " and pk_group = '"
					+ simvo.getPk_group()
					+ "' and pk_org = '"
					+ simvo.getPk_org() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ע��ԭ��SQL���� δ��ȷ������
				// sql = " and pk_selfsgroup = '"+id+"' ";
				sql = " and a.pk_selfsgroup = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * �����ɶ��˺�
	 */
	public String OversightPart(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_partnaccount() != null) {
			String sql1 = "  select pk_partnaccount from sec_partnaccount where code  = '"
					+ simvo.getPk_partnaccount()
					+ "'"
					+ " and pk_group = '"
					+ simvo.getPk_group()
					+ "' and pk_org = '"
					+ simvo.getPk_org() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ע��ԭ��SQL���� δ��ȷ������
				// sql = " and pk_partnaccount = '"+id+"' ";
				sql = " and a.pk_partnaccount = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * �����ֻ��ص�
	 */
	public String OversightOperate(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_operatesite() != null) {
			String sql1 = "  select pk_operatesite from sec_operatesite where code  = '"
					+ simvo.getPk_operatesite()
					+ "'"
					+ " and pk_group = '"
					+ simvo.getPk_group()
					+ "' and pk_org = '"
					+ simvo.getPk_org() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ע��ԭ��SQL���� δ��ȷ������
				// sql = " and pk_operatesite = '"+id+"' ";
				sql = " and a.pk_operatesite = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * ����������λ
	 */
	public String OversightClient(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_client() != null) {
			String sql1 = "  select pk_client from sec_client where custcode  = '"
					+ simvo.getPk_client()
					+ "'"
					+ " and pk_group = '"
					+ simvo.getPk_group()
					+ "' and pk_org = '"
					+ simvo.getPk_org() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ע��ԭ��SQL���� δ��ȷ������
				// sql = " and pk_client = '"+id+"' ";
				sql = " and a.pk_client = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * ����֤ȯ����
	 */
	public String OversightClassify(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_classify() != null) {
			String temps[] = simvo.getPk_classify().split("[+]");
			List<String> tempList = new ArrayList<String>();
			if (temps != null && temps.length > 0) {
				for (int i = 0; i < temps.length; i++) {
					String sql1 = "  select pk_sclassify from sec_sclassify where code  = '"
							+ temps[i].trim() + "' and isnull(dr,0) = 0 ";
					String id = queryResult(sql1);
					if (id != null && !"".equals(id)) {
						tempList.add(id);
					}
				}
				if (tempList != null && tempList.size() > 0) {
					if (tempList.size() == 1) {
						// ƴװSQL����δ��ȷ������
						// sql =
						// " and pk_securities in (select pk_securities from sec_securities where pk_sclassify = '"+id+"' and isnull(dr,0) = 0 ) ";
						sql = " and a.pk_securities in (select pk_securities from sec_securities where pk_sclassify = '"
								+ tempList.get(0) + "' and isnull(dr,0) = 0 ) ";
					} else {
						String temp1 = "('" + tempList.get(0) + "',";
						for (int j = 1; j < tempList.size() - 1; j++) {
							temp1 += "'" + tempList.get(j) + "',";
						}
						temp1 += "'" + tempList.get(tempList.size() - 1) + "')";
						sql = " and a.pk_securities in (select pk_securities from sec_securities where pk_sclassify in "
								+ temp1 + " and isnull(dr,0) = 0 ) ";
					}
					sf.append(sql);
				} else {
					sql = " and 1=2 ";
					sf.append(sql);
				}
			} else {
				sql = " and 1=2 ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * ���������֯
	 */
	public String OversightStocksort(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getStocksortcode() != null) {
			String sql1 = " select pk_stocksort from sec_stocksort  where stocksortcode = '"
					+ simvo.getStocksortcode() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ƴװSQL����δ��ȷ������
				// sql = " and a.pk_stocksort = '"+id+"' ";
				sql = " and a.pk_stocksort = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * �����ʲ�����
	 */
	public String OversightAssets(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getAssetspropcode() != null) {
			String sql1 = " select pk_assetsprop from sec_assetsprop where code = '"
					+ simvo.getAssetspropcode() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql1);
			if (id != null && !"".equals(id)) {
				// ƴװSQL����δ��ȷ������
				// sql = " and pk_assetsprop = '"+id+"' ";
				sql = " and a.pk_assetsprop = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	public String queryResult(String sql) throws BusinessException {
		String id = (String) new BaseDAO().executeQuery(sql,
				new ResultSetProcessor() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object handleResultSet(ResultSet rs)
							throws SQLException {
						String id = null;
						if (rs.next()) {
							id = rs.getString(1);
						}
						return id;
					}
				});
		return id;
	}

	/**
	 * ��ӱ��ֵ���������
	 * 
	 * @param simvo
	 * @param sf
	 * @return
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2016-1-29 ����10:44:38
	 */
	public String OversightCurrtype(IufoQuerySIMVO simvo, StringBuffer sf)
			throws BusinessException {
		String sql = null;
		if (simvo.getPk_currtype() != null) {
			sql = "SELECT t.pk_currtype from bd_currtype t where t.code='"
					+ simvo.getPk_currtype() + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql);
			if (id != null && !"".equals(id)) {
				sql = " and b.pk_currtype = '" + id + "' ";
				sf.append(sql);
			}
		}
		return sql;
	}

	/**
	 * ���ݹ�˾code��ѯ�������������code�Ǽ��ţ����账��
	 * 
	 * @param orgCode
	 *            ��֯����
	 * @return ��֯����
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2016-1-29 ����3:10:02
	 */
	public String getPK_orgFromCode(String orgCode) throws BusinessException {
		String sql = null;
		if (!StringUtil.isEmptyWithTrim(orgCode)) {
			sql = " SELECT t.pk_org FROM org_orgs t WHERE t.code='" + orgCode
					+ "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql);
			if (!StringUtil.isEmptyWithTrim(id)) {
				return id;
			}
		}
		return null;
	}

	/**
	 * �����˱�code��ѯ������
	 * 
	 * @param bookCode
	 *            �˱�����
	 * @return �˱�����
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2016-1-29 ����3:10:02
	 */
	public String getPK_glBookFromCode(String bookCode)
			throws BusinessException {
		String sql = null;
		if (!StringUtil.isEmptyWithTrim(bookCode)) {
			sql = " SELECT t.PK_ACCOUNTINGBOOK FROM org_accountingbook t WHERE t.code='"
					+ bookCode + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql);
			if (!StringUtil.isEmptyWithTrim(id)) {
				return id;
			}
		}
		return null;
	}

	/**
	 * ������֯������ѯ��������
	 * 
	 * @param pk
	 *            ��֯����
	 * @return ��������
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2016-1-29 ����3:10:02
	 */
	public String getPK_GroupFromCode(String pk) throws BusinessException {
		String sql = null;
		if (!StringUtil.isEmptyWithTrim(pk)) {
			sql = " SELECT t.pk_group FROM org_orgs t WHERE t.pk_org='" + pk
					+ "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql);
			if (!StringUtil.isEmptyWithTrim(id)) {
				return id;
			}
		}
		return null;
	}

	/**
	 * ����֤ȯ�����ѯ֤ȯ����
	 * 
	 * @param code
	 *            ֤ȯ����
	 * @return pk ֤ȯ����
	 * @throws BusinessException
	 * @author mx
	 * @date 2016-1-29 ����3:10:02
	 */
	public String getPk_securitiesFromCode(String code)
			throws BusinessException {
		String sql = null;
		if (!StringUtil.isEmptyWithTrim(code)) {
			sql = " select pk_securities from sec_securities where code = '"
					+ code + "' and isnull(dr,0) = 0 ";
			String id = queryResult(sql);
			if (!StringUtil.isEmptyWithTrim(id)) {
				return id;
			}
		}
		return null;
	}
}
