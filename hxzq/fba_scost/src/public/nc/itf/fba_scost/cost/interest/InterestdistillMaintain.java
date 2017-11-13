package nc.itf.fba_scost.cost.interest;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;
import nc.itf.pubapp.pub.smart.ISmartService;

public interface InterestdistillMaintain extends ISmartService{
	
    public InterestdistillVO[] query(IQueryScheme queryScheme)
      throws BusinessException;
    
    
	/**
	 * 计提
	 */
	public InterestdistillVO[] distill(LoginContext context,UFDate trade_date) throws BusinessException;

	/**
	 * 取消计提
	 */
	public void unDistill(LoginContext context,UFDate trade_date) throws BusinessException;

	/**
	 * 记账
	 */
	public void tally(LoginContext context) throws BusinessException;
}
