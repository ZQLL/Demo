package nc.itf.fba_sim.simtrade.report;

import com.ufida.dataset.IContext;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

public abstract interface ISecInvestDetailQuery {
	public abstract ReportQueryResult queryDetailData(IContext paramIContext)
			throws BusinessException;
}
