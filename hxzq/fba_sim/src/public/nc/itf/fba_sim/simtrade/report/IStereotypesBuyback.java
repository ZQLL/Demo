package nc.itf.fba_sim.simtrade.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * 约定式购回报表接口
 * 
 * @author lihaibo
 * 
 */
public abstract interface IStereotypesBuyback {
	public abstract ReportQueryResult queryDetailData(IContext paramIContext)
			throws BusinessException;
}
