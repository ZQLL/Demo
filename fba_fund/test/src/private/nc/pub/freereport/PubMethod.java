package nc.pub.freereport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;
import nc.vo.trade.voutils.VOUtil;

public class PubMethod {
	private static PubMethod pubmethod;

	private static IUAPQueryBS queryservice;

	private Map<String, String> pluginMap = null;

	public static PubMethod getInstance() {
		if (pubmethod == null) {
			pubmethod = new PubMethod();
		}
		return pubmethod;
	}

	private IUAPQueryBS getQueryservice() {
		if (queryservice == null) {
			queryservice = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return queryservice;
	}

	public UFDouble sub(UFDouble d1, UFDouble d2) {
		return SafeCompute.sub(d1, d2);
	}

	/**
	 * UFDouble数据相加
	 */
	public UFDouble add(UFDouble d1, UFDouble d2) {
		return SafeCompute.add(d1, d2);
	}

	public UFDouble add(UFDouble d1, double d2) {
		return add(d1, new UFDouble(d2));
	}

	/**
	 * UFDouble数据相乘
	 */
	public UFDouble multiply(UFDouble d1, UFDouble d2) {
		return SafeCompute.multiply(d1, d2);
	}

	/**
	 * UFDouble数据相乘
	 */
	public UFDouble multiply(UFDouble d1, double d2) {
		return SafeCompute.multiply(d1, new UFDouble(d2));
	}

	/**
	 * UFDouble数据相除
	 * 
	 */
	public UFDouble div(UFDouble d1, UFDouble d2) {
		return SafeCompute.div(d1, d2);
	}

	/**
	 * 对除数为零的进行处理
	 * 
	 * @param dividend
	 * @param divisor
	 * @return
	 */
	public UFDouble divCal(UFDouble dividend, UFDouble divisor) {
		UFDouble result = null;
		if (divisor == null || divisor.compareTo(UFDouble.ZERO_DBL) <= 0) {
			result = null;
		} else {
			result = div(dividend, divisor);
		}
		return result;
	}

	/**
	 * UFDouble取绝对值
	 */
	public UFDouble abs(UFDouble d1) {
		if (d1 != null) {
			return d1.abs();
		}
		return null;
	}

	/**
	 * 查询公司所有成本计算方案
	 * 
	 * @param pk_corp
	 * @param pk_glorgbook
	 * @return
	 * @throws Exception
	 * @author zhengwzha
	 * @throws BusinessException
	 * @date 2014-04-17 上午10:26:32
	 */
	public List<CostPlanVO> getCostPlanVOs(String pk_org, String pk_glorgbook,
			String pk_group) throws BusinessException {
		StringBuffer sqlwhere = new StringBuffer();
		sqlwhere.append(" isnull(dr,0)=0 ");
		sqlwhere.append(" and pk_org = '");
		sqlwhere.append(pk_org);
		sqlwhere.append("' and pk_group = '");
		sqlwhere.append(pk_group);
		sqlwhere.append("' and pk_glorgbook = '");
		sqlwhere.append(pk_glorgbook + "' ");

		return (List<CostPlanVO>) getQueryservice().retrieveByClause(
				CostPlanVO.class, sqlwhere.toString());
	}

	/**
	 * 查询公司所有成本计算方案 通过业务单元，集团，账簿能够唯一确定成本计算方案
	 * 
	 * @param pk_org
	 * @param pk_glorgbook
	 * @param pk_group
	 * @return
	 * @throws BusinessException
	 */
	public CostPlanVO getCostPlanVO(String pk_org, String pk_glorgbook,
			String pk_group) throws BusinessException {
		List<CostPlanVO> list = getCostPlanVOs(pk_org, pk_glorgbook, pk_group);
		if (list == null || list.size() == 0) {
			throw new BusinessException("未设置成本计算方案！");
		}
		return list.get(0);
	}

	/**
	 * 通过业务单元pk取对应的集团
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getDeGroup(String pk_org) throws BusinessException {
		OrgVO orgVo = (OrgVO) getQueryservice().retrieveByPK(OrgVO.class,
				pk_org);
		if (orgVo != null)
			return orgVo.getPk_group();
		else
			return " ";
	}

	/**
	 * 分组
	 * 
	 * @param objs
	 * @param keys
	 * @return
	 * @throws BusinessException
	 */
	public <T extends CircularlyAccessibleValueObject> Map<String, T> hashlizeObject(
			List<T> objs, String[] keys) throws BusinessException {
		if (objs == null || objs.isEmpty()) {
			return new HashMap<String, T>();
		}
		if (keys == null || keys.length == 0) {
			throw new BusinessException("分组字段不能为空。");
		}
		Map<String, T> result = new HashMap<String, T>();
		String key = null;
		for (int i = 0; i < objs.size(); i++) {
			key = VOUtil.getCombinesKey(objs.get(i), keys);
			result.put(key, objs.get(i));
		}
		return result;
	}

	/**
	 * 分组
	 * 
	 * @param objs
	 * @param iHashKey
	 * @return
	 */
	public <T extends CircularlyAccessibleValueObject> Map<String, List<T>> hashlizeObjects(
			List<T> objs, String[] keys) {
		if (objs == null || objs.isEmpty()) {
			return new HashMap<String, List<T>>();
		}
		Map<String, List<T>> resultMap = new HashMap<String, List<T>>();
		String key = null;
		for (int i = 0; i < objs.size(); i++) {
			key = VOUtil.getCombinesKey(objs.get(i), keys);
			List<T> temp = resultMap.get(key);
			if (temp == null) {
				temp = new ArrayList<T>();
				temp.add(objs.get(i));
				resultMap.put(key, temp);
			} else {
				temp.add(objs.get(i));
			}
		}
		return resultMap;
		// 妹的， Hashlize.hashlizeObjects处理有问题
		// return Hashlize.hashlizeObjects(objs.toArray(), new VOHashKeyAdapter(
		// keys));
	}

	public DataSet convertVOToArray(MetaData metaData,
			List<? extends SuperVO> vos) {
		Field[] fields = metaData.getFields();
		Object[][] results = new Object[vos.size()][];
		for (int i = 0; i < vos.size(); i++) {
			Object[] row = new Object[fields.length];
			for (int j = 0; j < fields.length; j++) {
				row[j] = vos.get(i).getAttributeValue(fields[j].getFldname());
			}
			results[i] = row;
		}
		DataSet dataSet = new DataSet();
		dataSet.setMetaData(metaData);
		dataSet.setDatas(results);

		return dataSet;
	}
	
	public DataSet convertVOToArraynew(MetaData metaData,
			List<? extends SuperVO> vos) {
		Field[] fields = metaData.getFields();
		Object[][] results = new Object[vos.size()][];
		for (int i = 0; i < vos.size(); i++) {
			Object[] row = new Object[fields.length];
			for (int j = 0; j < fields.length; j++) {
				if(j==17){
					row[j] = vos.get(i).getAttributeValue("cur_amortize");
				}else
					row[j] = vos.get(i).getAttributeValue(fields[j].getFldname());
				
			}
			results[i] = row;
		}
		DataSet dataSet = new DataSet();
		dataSet.setMetaData(metaData);
		dataSet.setDatas(results);

		return dataSet;
	}

	/**
	 * 根据组织获取档案
	 * 
	 * @param org
	 * @return
	 * @throws BusinessException
	 * @author cjh
	 * @date 2016-03-03
	 */
	public Map<String, SecuritiesVO> getSecuritiesVOSByOrg(String org)
			throws BusinessException {
		String wheresql = "nvl(dr,0)=0 and nvl(state,0)=0";
		List<SecuritiesVO> result = (List<SecuritiesVO>) getQueryservice()
				.retrieveByClause(SecuritiesVO.class, wheresql);
		Map<String, SecuritiesVO> securitesMap = new HashMap<String, SecuritiesVO>();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				securitesMap.put(result.get(i).getPk_securities(),
						result.get(i));
			}
		}
		return securitesMap;
	}
}
