package nc.itf.fba_smt.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * 可融证券增减结存明细表
 * @author mx
 *
 */
public interface IAvailableDetailRpt {
	public ReportQueryResult queryDetailData(IContext context) throws BusinessException;
}
