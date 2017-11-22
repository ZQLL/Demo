package nc.itf.fba_zqjd.trade.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * ?????????
 * 
 * @author mx
 * 
 */
public interface IStockChanges {

	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException;

}
