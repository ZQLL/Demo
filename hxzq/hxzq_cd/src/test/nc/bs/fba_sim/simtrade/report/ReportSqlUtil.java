package nc.bs.fba_sim.simtrade.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.fba_sec.secbd.secbilltype.BillTypeVO;
import nc.vo.fba_sim.simbs.checkplan.CheckplanVO;
import nc.vo.pub.BusinessException;

public class ReportSqlUtil {
	private static ReportSqlUtil instance;
	private Map<String, String> fundSqlMap = null;
	private Map<String, String> reportSqlMap = null;
	private Map<String, String> billTypeMap = null;

	public static ReportSqlUtil getInstance() {
		if (instance == null) {
			instance = new ReportSqlUtil();
		}
		return instance;
	}

	public String getFundSql(String busiTypeCode) throws BusinessException {
		if (this.fundSqlMap == null) {
			initSql();
		}
		return (String) this.fundSqlMap.get(busiTypeCode);
	}

	public String getReportSql(String busiTypeCode) throws BusinessException {
		if (this.reportSqlMap == null) {
			initSql();
		}
		return (String) this.reportSqlMap.get(busiTypeCode);
	}

	public String getBillTypeName(String busiTypeCode) throws BusinessException {
		if (this.billTypeMap == null) {
			initSql();
		}
		return (String) this.billTypeMap.get(busiTypeCode);
	}

	private void initSql() throws BusinessException {
		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		synchronized (this) {
			StringBuffer initSql = new StringBuffer();
			initSql.append("select * from sec_billtype where isnull(dr,0) = 0");
			List<BillTypeVO> vos = (List) queryservice
					.executeQuery(initSql.toString(), new BeanListProcessor(
							BillTypeVO.class));
			BillTypeVO[] vosArry = (BillTypeVO[]) vos
					.toArray(new BillTypeVO[0]);
			this.fundSqlMap = new HashMap();
			this.reportSqlMap = new HashMap();
			this.billTypeMap = new HashMap();
			for (int i = 0; i < vosArry.length; i++) {
				this.fundSqlMap.put(vosArry[i].getPk_billtypecode(),
						vosArry[i].getFund_sql());
				this.reportSqlMap.put(vosArry[i].getPk_billtypecode(),
						vosArry[i].getReport_sql());
				this.billTypeMap.put(vosArry[i].getPk_billtypecode(),
						vosArry[i].getBilltypename());
			}
		}
	}

	public String getQuerySql(String condition, String querySql) {
		String result = null;
		int index = querySql.indexOf("@@condition@@");
		if ((index != -1) && (condition == null)) {
			result = querySql.replaceAll("@@condition@@", " ");
		} else if (index != -1) {
			result = querySql.replaceAll("@@condition@@", condition);
		} else {
			result = querySql + condition;
		}
		return result;
	}

	public List<CheckplanVO> findCheckPlanVo(String org, String group,
			String glBook) throws BusinessException {
		StringBuffer checkSql = new StringBuffer();

		checkSql.append(" select splan.pk_org, splan.pk_group, ");
		checkSql.append(" splan.pk_glorgbook, splan.checkorder, ");
		checkSql.append(" splan.pk_billtype, billtype.billtypename as vdef2, ");
		checkSql.append(" billtype.direction as vdef1 ,");
		checkSql.append(" billtype.pk_stocksort1 ,");
		checkSql.append(" billtype.pk_stocksort2 ");
		checkSql.append(" from sim_checkplan splan ");
		checkSql.append(" inner join sec_billtype billtype ");
		checkSql.append(" on splan.pk_billtype = billtype.pk_billtypecode ");
		checkSql.append(" where isnull(splan.dr, 0) = 0 ");
		checkSql.append(" and isnull(billtype.dr, 0) = 0 ");
		checkSql.append(" and splan.pk_org = '" + org + "' ");
		checkSql.append(" and splan.pk_glorgbook = '" + glBook + "' ");
		checkSql.append(" and splan.pk_group = '" + group + "' ");
		checkSql.append(" and isnull(splan.ischeck, 'N') = 'Y' ");
		checkSql.append(" order by checkorder");

		IUAPQueryBS queryservice = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());

		return (List) queryservice.executeQuery(checkSql.toString(),
				new BeanListProcessor(CheckplanVO.class));
	}
}
