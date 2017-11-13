package nc.vo.fba_secd.secdimp.dataimport;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.fba_secd.secdimp.dataimport.DataImportVO")
public class AggDataImportVO extends AbstractBill {

  @Override
  public IBillMeta getMetaData() {
    IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggDataImportVOMeta.class);
    return billMeta;
  }

  @Override
  public DataImportVO getParentVO() {
    return (DataImportVO) this.getParent();
  }
}