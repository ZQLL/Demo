package nc.itf.fba_scost.cost.billcheck;

import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.pub.BusinessException;


/**
 * 单据审核接口
 * 
 * @author liangwei
 * 
 */
public interface IBillCheckService {
	/**
	 * 已审核数据发送会计平台
	 *
	 */
	public void tallyAllCheckedData(CheckParaVO checkParaVO) throws BusinessException;

	/**
	 * 审核单据
	 * 
	 * @throws Exception
	 */
	public String checkBill(CheckParaVO checkParaVO) throws BusinessException;

	/**
	 * 取消审核
	 * 
	 * @throws Exception
	 */
	public String unCheckBill(CheckParaVO checkParaVO) throws BusinessException;

}
