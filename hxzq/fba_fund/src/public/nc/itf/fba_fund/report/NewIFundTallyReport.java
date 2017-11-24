package nc.itf.fba_fund.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * 筹资管理台账
 * 
 * @author zq
 * @since 2017-11-22
 */
public interface NewIFundTallyReport {
	public ReportQueryResult queryDetailData(IContext context) throws BusinessException;
}
