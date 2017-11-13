package nc.impl.fba_scost.cost.interest;

import nc.bs.fba_scost.cost.interest.ace.rule.DataUniqueCheckRule;
import nc.bs.fba_scost.cost.interest.bp.Sim_interestdistillBP;
import nc.impl.pub.ace.AceSim_interestdistillPubServiceImpl;
import nc.impl.pubapp.pub.smart.BatchSaveAction;
import nc.itf.fba_scost.cost.interest.InterestdistillMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

public class InterestdistillMaintainImpl extends
		AceSim_interestdistillPubServiceImpl implements InterestdistillMaintain {
	public InterestdistillVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybasedoc(queryScheme);
	}

	public BatchOperateVO batchSave(BatchOperateVO batchVO)
			throws BusinessException {
		BatchSaveAction<InterestdistillVO> saveAction = new BatchSaveAction();
		BatchOperateVO retData = saveAction.batchSave(batchVO);

		new DataUniqueCheckRule().process(new BatchOperateVO[] { batchVO });
		return retData;
	}

	public InterestdistillVO[] distill(LoginContext context, UFDate trade_date)
			throws BusinessException {
		Sim_interestdistillBP bp = new Sim_interestdistillBP();
		return bp.distill(context, trade_date);
	}

	public void unDistill(LoginContext context, UFDate trade_date)
			throws BusinessException {
		Sim_interestdistillBP bp = new Sim_interestdistillBP();
		bp.unDistill(context, trade_date);
	}

	public void tally(LoginContext context) throws BusinessException {
		Sim_interestdistillBP bp = new Sim_interestdistillBP();
		bp.tally(context);
	}
}
