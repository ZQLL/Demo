package nc.itf.fba_sim.simtrade.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

public abstract interface ISecInvestDetailQuery {
	public abstract ReportQueryResult queryDetailData(IContext paramIContext)
			throws BusinessException;
}
