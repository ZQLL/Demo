/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package nc.bs.fba_scost.cost.interest.pub;

import java.sql.ResultSet;
import java.sql.SQLException;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.pub.BusinessException;

public class ScostRealratePlugin {
	public boolean isRealTradevo(String pk_glorgbook) throws BusinessException {
		boolean isfalg = false;
		String value = queryParavalue(pk_glorgbook, "SEC32");
		if ("Y".equals(value)) {
			isfalg = true;
		}
		return isfalg;
	}

	public String queryParavalue(String pk_glorgbook, String initcode)
			throws BusinessException {
		String va = null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select value from pub_sysinit where pk_org  = '"
				+ pk_glorgbook + "' " + " and initcode = '" + initcode
				+ "' and isnull(dr,0) = 0 ");
		try {
			va = (String) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							String name = null;
							if (rs.next()) {
								name = rs.getString("value");
							}
							return name;
						}
					});
		} catch (DAOException e) {
			throw new BusinessException(e);
		}
		if (va == null)
			throw new BusinessException("当前组织下，参数:" + initcode + "，没有设置! ");
		return va;
	}
}