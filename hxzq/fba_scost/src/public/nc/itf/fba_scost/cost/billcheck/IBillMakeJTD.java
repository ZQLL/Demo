package nc.itf.fba_scost.cost.billcheck;

import nc.vo.fba_scost.cost.checkpara.CheckParaVO;

public interface IBillMakeJTD {
	public void MakeJTD(CheckParaVO checkParaVO);

	public void DeleteJTD(CheckParaVO checkParaVO);

	public boolean getBooleanFromInitcode(String pk_glorgbook, String initcode);
}
