package nc.ui.erm.hxzq_fpxx.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.ui.pubapp.uif2app.actions.IDataOperationService;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.hxzq.fpxx.FpxxBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * 示例单据的操作代理
 * 
 * @since 6.0
 * @version 2011-7-6 上午08:31:09
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
		// 目前的删除并不是走这个方法，由于pubapp不支持从这个服务中执行删除操作
		// 单据的删除实际上使用的是：nc.ui.mmpd.samplebill.serviceproxy.SampleDeleteProxy
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
