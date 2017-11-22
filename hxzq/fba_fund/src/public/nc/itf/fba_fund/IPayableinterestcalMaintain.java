package nc.itf.fba_fund;

import java.util.List;

import nc.itf.pubapp.pub.smart.ISmartService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.fba_fund.payableinterestcal.PayableInterestCalVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

public interface IPayableinterestcalMaintain extends ISmartService {

	public PayableInterestCalVO[] query(IQueryScheme queryScheme)
			throws BusinessException, Exception;

	/**
	 * ����
	 */
	public PayableInterestCalVO[] calInterest(LoginContext context,
			UFDate trade_date) throws BusinessException;

	/**
	 * ȡ������
	 */
	public void cancelCal(LoginContext context, UFDate trade_date)
			throws BusinessException;

	/**
	 * ����
	 */
	public void tally(LoginContext context) throws BusinessException;

	/**
	 * ����̨������
	 */
	public void saveTally(List<FundTallyVO> tallyList) throws BusinessException;
}