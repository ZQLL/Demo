package nc.vo.hxzq.fdll;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class FdllBillVOMeta extends AbstractBillMeta {
  public FdllBillVOMeta() {
    this.init();
  }
  private void init() {
    this.setParent(nc.vo.hxzq.fdll.FdllHVO.class);
  }
}