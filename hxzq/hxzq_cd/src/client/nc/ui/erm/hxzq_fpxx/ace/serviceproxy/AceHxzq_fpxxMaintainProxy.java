package nc.ui.erm.hxzq_fpxx.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.ui.pubapp.uif2app.actions.IDataOperationService;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.hxzq.fpxx.FpxxBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * ʾ�����ݵĲ�������
 * 
 * @since 6.0
 * @version 2011-7-6 ����08:31:09
 * @author duy
 */
public class AceHxzq_fpxxMaintainProxy implements IDataOperationService,
		IQueryService {

	@Override
	public IBill[] insert(IBill[] value) throws BusinessException {
		nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain operator = NCLocator
				.getInstance().lookup(
						nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain.class);
		FpxxBillVO[] vos = operator.insert((FpxxBillVO[]) value);
		return vos;
	}

	@Override
	public IBill[] update(IBill[] value) throws BusinessException {
		nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain operator = NCLocator
				.getInstance().lookup(
						nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain.class);
		FpxxBillVO[] vos = operator.update((FpxxBillVO[]) value);
		return vos;
	}

	@Override
	public IBill[] delete(IBill[] value) throws BusinessException {
		// Ŀǰ��ɾ�����������������������pubapp��֧�ִ����������ִ��ɾ������
		// ���ݵ�ɾ��ʵ����ʹ�õ��ǣ�nc.ui.mmpd.samplebill.serviceproxy.SampleDeleteProxy
		nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain operator = NCLocator
				.getInstance().lookup(
						nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain.class);
		operator.delete((FpxxBillVO[]) value);
		return value;
	}

	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain query = NCLocator.getInstance()
				.lookup(nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain.class);
		return query.query(queryScheme);
	}
}
