package nc.itf.fba_smt.report;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * ������ȯҵ�񱨱�(��ѯ������ȯҵ����ؿ����֯��֤ȯ������)--�����м䷢��
 * @author lihbj
 *
 */
public interface IFinancingHappenRpt {
	public ReportQueryResult queryDetailData(IContext context) throws BusinessException;
}
