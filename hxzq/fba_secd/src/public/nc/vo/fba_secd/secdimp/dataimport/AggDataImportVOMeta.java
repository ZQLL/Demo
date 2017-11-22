package nc.vo.fba_secd.secdimp.dataimport;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggDataImportVOMeta extends AbstractBillMeta {
	public AggDataImportVOMeta() {
		this.init();
	}

	private void init() {
		this.setParent(nc.vo.fba_secd.secdimp.dataimport.DataImportVO.class);
		this.addChildren(nc.vo.fba_secd.secdimp.dataimport.DataImportBVO2.class);
		this.addChildren(nc.vo.fba_secd.secdimp.dataimport.DataImportBVO1.class);
	}
}