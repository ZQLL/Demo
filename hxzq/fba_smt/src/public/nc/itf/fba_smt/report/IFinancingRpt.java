package nc.itf.fba_smt.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * 融资融券业务报表(查询融资融券业务相关库存组织的证券结存情况)
 * @author zhengwzha
 *
 */
public interface IFinancingRpt {
	public ReportQueryResult queryDetailData(IContext context) throws BusinessException;
}
