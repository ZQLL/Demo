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
	 * ����
	 */
	public InterestdistillVO[] distill(LoginContext context,UFDate trade_date) throws BusinessException;

	/**
	 * ȡ������
	 */
	public void unDistill(LoginContext context,UFDate trade_date) throws BusinessException;

	/**
	 * ����
	 */
	public void tally(LoginContext context) throws BusinessException;
}
