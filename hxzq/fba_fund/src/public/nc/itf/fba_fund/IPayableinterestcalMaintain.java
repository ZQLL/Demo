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
	 * 计提
	 */
	public PayableInterestCalVO[] calInterest(LoginContext context,
			UFDate trade_date) throws BusinessException;

	/**
	 * 取消计提
	 */
	public void cancelCal(LoginContext context, UFDate trade_date)
			throws BusinessException;

	/**
	 * 记账
	 */
	public void tally(LoginContext context) throws BusinessException;

	/**
	 * 保存台账数据
	 */
	public void saveTally(List<FundTallyVO> tallyList) throws BusinessException;
}