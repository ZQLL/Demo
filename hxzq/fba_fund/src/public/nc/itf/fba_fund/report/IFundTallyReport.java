package nc.itf.fba_fund.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * 筹资管理台账
 * 
 * @author qs
 * @since 2017-6-16上午9:04:32
 */
public interface IFundTallyReport {
	public ReportQueryResult queryDetailData(IContext context) throws BusinessException;
}
