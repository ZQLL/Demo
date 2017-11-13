package nc.itf.fba_scost.cost.billcheck;

import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.pub.BusinessException;


/**
 * ������˽ӿ�
 * 
 * @author liangwei
 * 
 */
public interface IBillCheckService {
	/**
	 * ��������ݷ��ͻ��ƽ̨
	 *
	 */
	public void tallyAllCheckedData(CheckParaVO checkParaVO) throws BusinessException;

	/**
	 * ��˵���
	 * 
	 * @throws Exception
	 */
	public String checkBill(CheckParaVO checkParaVO) throws BusinessException;

	/**
	 * ȡ�����
	 * 
	 * @throws Exception
	 */
	public String unCheckBill(CheckParaVO checkParaVO) throws BusinessException;

}
