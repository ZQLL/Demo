package nc.itf.fba_sim.simtrade.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * Լ��ʽ���ر���ӿ�
 * 
 * @author lihaibo
 * 
 */
public abstract interface IStereotypesBuyback {
	public abstract ReportQueryResult queryDetailData(IContext paramIContext)
			throws BusinessException;
}
