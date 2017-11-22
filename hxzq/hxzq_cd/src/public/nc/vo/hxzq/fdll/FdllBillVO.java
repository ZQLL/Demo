package nc.vo.hxzq.fdll;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.hxzq.fdll.FdllHVO")
public class FdllBillVO extends AbstractBill {

	
	@Override
	public IBillMeta getMetaData() {
		IBillMeta billMeta = BillMetaFactory.getInstance().getBillMeta(
				FdllBillVOMeta.class);
		return billMeta;
	}

	
	@Override
	public FdllHVO getParentVO() {
		return (FdllHVO) this.getParent();
	}
}