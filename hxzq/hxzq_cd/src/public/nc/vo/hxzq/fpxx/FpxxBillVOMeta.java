package nc.vo.hxzq.fpxx;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class FpxxBillVOMeta extends AbstractBillMeta {
	public FpxxBillVOMeta() {
		this.init();
	}

	private void init() {
		this.setParent(nc.vo.hxzq.fpxx.FpxxHVO.class);
	}
}