package nc.itf.fba_scost.cost.billcheck;

import nc.ui.uif2.AppEvent;
import nc.ui.uif2.model.AppEventConst;
import nc.ui.uif2.model.RowOperationInfo;
import nc.vo.bd.meta.IBDObject;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.pub.BusinessException;

public interface IBillMakeJTD {
	public void MakeJTD(CheckParaVO checkParaVO);
	public void DeleteJTD(CheckParaVO checkParaVO);
	public boolean getBooleanFromInitcode(String pk_glorgbook,String initcode);
}
