package nc.itf.fba_fund.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * ���ʹ���̨��
 * 
 * @author qs
 * @since 2017-6-16����9:04:32
 */
public interface IFundTallyReport {
	public ReportQueryResult queryDetailData(IContext context) throws BusinessException;
}
