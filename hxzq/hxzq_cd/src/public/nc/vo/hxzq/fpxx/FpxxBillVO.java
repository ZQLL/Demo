package nc.vo.hxzq.fpxx;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.hxzq.fpxx.FpxxHVO")
public class FpxxBillVO extends AbstractBill {

  @Override
  public IBillMeta getMetaData() {
    IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(FpxxBillVOMeta.class);
    return billMeta;
  }

  @Override
  public FpxxHVO getParentVO() {
    return (FpxxHVO) this.getParent();
  }
}