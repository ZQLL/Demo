package nc.impl.pubapp.pub.smart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uap.apppf.util.SQLTransferMeaningUtil;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.pubapp.pub.smart.IBillQueryService;
import nc.itf.pubapp.pub.smart.IQueryBillService;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.md.model.type.ICollectionType;
import nc.md.persist.framework.MDPersistenceService;
import nc.md.util.MDUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.query2.sql.process.PowerSqlUtils;

public class QueryBillServiceImpl implements IQueryBillService {

	@Override
	public Object queryBill(Class<?> clazz, String primaryKey) {
		return this.queryBill(clazz, primaryKey, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object queryBill(Class<?> clazz, String primaryKey, String nodeCode) {
		Object vo = null;
		try {

			if (null == clazz || null == primaryKey) {
				BusinessException be = new BusinessException("参数错误");/*
																	 * -=notranslate
																	 * =-
																	 */
				ExceptionUtils.wrappException(be);
			}
			if (!this.hasBillPrivilege(clazz, primaryKey, nodeCode)) {
				return null;
			}
			if (SuperVO.class.isAssignableFrom(clazz)) {
				vo = MDPersistenceService.lookupPersistenceQueryService()
						.queryBillOfNCObjectByPK(clazz, primaryKey)
						.getContainmentObject();
			} else if (AbstractBill.class.isAssignableFrom(clazz)) {
				Class<? extends AbstractBill> bill = (Class<? extends AbstractBill>) clazz;
				vo = NCLocator.getInstance().lookup(IBillQueryService.class)
						.querySingleBillByPk(bill, primaryKey);
			} else {
				BusinessException be = new BusinessException(
						"目前只支持SuperVO和AbstractBill结构的数据");/* -=notranslate=- */
				ExceptionUtils.wrappException(be);

			}
		} catch (MetaDataException e) {
			ExceptionUtils.wrappException(e);
		}
		return vo;
	}

	@Override
	public Object[] queryBills(Class<?> clazz, String[] pks, String nodecode) {

		if (clazz == null || (pks == null || pks.length == 0)) {
			BusinessException be = new BusinessException("参数错误");/*
																 * -=notranslate=
																 * -
																 */
			ExceptionUtils.wrappException(be);
		}
		for (String pk : pks) {
			if (!this.hasBillPrivilege(clazz, pk, nodecode)) {
				return null;
			}
		}
		Object[] objs = null;
		try {
			if (SuperVO.class.isAssignableFrom(clazz)) {
				NCObject[] ncvos = MDPersistenceService
						.lookupPersistenceQueryService()
						.queryBillOfNCObjectByPKs(clazz, pks, null, false);
				objs = new Object[ncvos.length];
				for (int i = 0; i < objs.length; i++)
					objs[i] = ncvos[i].getContainmentObject();
			} else if (AbstractBill.class.isAssignableFrom(clazz)) {

				Class<? extends AbstractBill> bill = (Class<? extends AbstractBill>) clazz;
				objs = NCLocator.getInstance().lookup(IBillQueryService.class)
						.queryAbstractBillsByPks(bill, pks);
			} else {
				BusinessException be = new BusinessException(
						"目前只支持SuperVO和AbstractBill结构的数据");/* -=notranslate=- */
				ExceptionUtils.wrappException(be);

			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return objs;
	}

	@Override
	public Object[] queryBills(Class<?> clazz, String[] pks) {
		return queryBills(clazz, pks, null);
	}

	private String generatePrivilegeSql(IBean bean, String whereSql) {
		String privilegeSql = "";
		String mainBeanPkName = null;
		String mainTableName = null;
		// List<String> childTbName = new ArrayList<String>();
		Map<String, String> fPKAndTableMap = new HashMap<String, String>();
		List<IAttribute> mainAttributes = bean.getAttributes();
		for (IAttribute attribute : mainAttributes) {
			if (attribute.getColumn() != null && attribute.getColumn().isPkey()) {
				mainBeanPkName = attribute.getColumn().getName();
				mainTableName = bean.getTable().getName();
			} else if (MDUtil.isCollectionType(attribute.getDataType())) {
				ICollectionType ctype = (ICollectionType) attribute
						.getDataType();
				IBean childBean = (IBean) ctype.getElementType();
				// attribute.get
				String subTableName = childBean.getTable().getName();
				// childTbName.add(subTableName);
				fPKAndTableMap.put(subTableName, attribute.getColumn()
						.getName());

			}
		}
		if (mainBeanPkName != null && mainTableName != null) {
			privilegeSql = "select " + mainTableName + "." + mainBeanPkName
					+ " from  " + mainTableName;

			// 2011-10-13 chenyyb from部分应该用左联接，否则的话如果关联不到子表记录的话会查不出来数据
			// 2011-10-19 chenyyb 在主子表关联的时候子表外键不一定和主表子键一致，需要从元数据属性中查找
			Set<Entry<String, String>> entrySet = fPKAndTableMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String childname = entry.getKey();
				privilegeSql += " left join " + childname + " on "
						+ mainTableName + "." + mainBeanPkName + " = "
						+ childname + "." + entry.getValue();
			}
			// if (childTbName.size() > 0) {
			// for (String childname : childTbName) {
			// // privilegeSql += " ," + childname;
			// privilegeSql +=
			// " left join " + childname + " on " + mainTableName + "."
			// + mainBeanPkName + " = " + childname + "." +
			// fPKAndTableMap.get(childname);
			// }
			// }
			privilegeSql += " where 1=1 ";
			// 2011-10-13 chenyyb from部分应该用左联接之后把where条件关联去掉
			// if (childTbName.size() > 0) {
			// for (String childname : childTbName) {
			// privilegeSql +=
			// " and " + mainTableName + "." + mainBeanPkName + " = "
			// + childname + "." + mainBeanPkName;
			// }
			// }

		}
		privilegeSql += whereSql;
		return privilegeSql;
	}

	// 是否有单据权限
	private boolean hasBillPrivilege(Class<?> clazz, String primaryKey,
			String nodeCode) {
		try {
			final IBean bean = MDBaseQueryFacade.getInstance()
					.getBeanByFullClassName(clazz.getName());
			String generatePowerSql = new PowerSqlUtils(bean)
					.generatePowerSql();
			String sqlAfterWhere = generatePowerSql
					+ new PowerSqlUtils(bean).getFuncPermissionOrgSql(nodeCode);
			String sql = this.generatePrivilegeSql(bean, sqlAfterWhere);
			String primaryKeyField = bean.getPrimaryKey().getPKColumn()
					.getName();
			sql += " and " + bean.getTable().getName() + "." + primaryKeyField
					+ "='" + SQLTransferMeaningUtil.tmsql(primaryKey) + "' ";
			IRowSet rowSet = new DataAccessUtils().query(sql);
			if (rowSet.size() > 0) {
				return true;
			}

		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return false;
	}

}
