package nc.itf.fba_scost.cost.billcheck;

import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.pub.lang.UFDate;

public interface IBillMakeJTD {
	public void MakeJTD(CheckParaVO checkParaVO);

	public void DeleteJTD(CheckParaVO checkParaVO);

	public void MakeJTD(CheckParaVO checkParaVO, UFDate date);

	public void DeleteJTD(CheckParaVO checkParaVO, UFDate date);

	public boolean getBooleanFromInitcode(String pk_glorgbook, String initcode);
}
