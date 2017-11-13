package nc.itf.fba_sim.simtrade.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * 股票质押明细表接口
 * 
 * @author lihaibo
 * 
 */
public abstract interface IStockPledgeDetail {
	public abstract ReportQueryResult queryDetailData(IContext paramIContext)
			throws BusinessException;
}
