package nc.itf.fba_smt.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;
/**
 * �ڳ�֤ȯ���������ϸ��
 * @author mx
 *
 */
public interface IStockOutDetailRpt {
	public ReportQueryResult queryDetailData(IContext context) throws BusinessException;
}
