package nc.ui.erm.hxzq_fpxx.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.ui.pubapp.pub.task.ISingleBillService;
import nc.vo.hxzq.fpxx.FpxxBillVO;

/**
 * 示例单据的删除代理，支持批删除
 * 
 * @since 6.0
 * @version 2011-8-4 上午07:22:35
 * @author duy
 */
public class AceHxzq_fpxxDeleteProxy implements ISingleBillService<FpxxBillVO> {

	
	@Override
	public FpxxBillVO operateBill(FpxxBillVO bill) throws Exception {
		nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain operator = NCLocator
				.getInstance().lookup(
						nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain.class);
		operator.delete(new FpxxBillVO[] { bill });
		return bill;
	}

}
