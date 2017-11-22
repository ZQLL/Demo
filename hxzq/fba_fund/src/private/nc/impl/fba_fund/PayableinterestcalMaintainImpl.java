package nc.impl.fba_fund;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.fba_fund.payableinterestcal.ace.bp.AcePayableinterestcalBP;
import nc.impl.pub.ace.AcePayableinterestcalPubServiceImpl;
import nc.impl.pubapp.pub.smart.BatchSaveAction;
import nc.itf.fba_fund.IPayableinterestcalMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.fba_fund.payableinterestcal.PayableInterestCalVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

public class PayableinterestcalMaintainImpl extends
		AcePayableinterestcalPubServiceImpl implements
		IPayableinterestcalMaintain {

	
	@Override
	public PayableInterestCalVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybasedoc(queryScheme);
	}

	
	@Override
	public BatchOperateVO batchSave(BatchOperateVO batchVO)
			throws BusinessException {
		BatchSaveAction<PayableInterestCalVO> saveAction = new BatchSaveAction<PayableInterestCalVO>();
		BatchOperateVO retData = saveAction.batchSave(batchVO);
		// 这个把台账也在这里保存了
		Object[] addVOs = batchVO.getAddObjs();
		@SuppressWarnings("unchecked")
		List<FundTallyVO> tallyList = (List<FundTallyVO>) ((PayableInterestCalVO) addVOs[0])
				.getObj();
		this.saveTally(tallyList);
		return retData;
	}

	
	@Override
	public PayableInterestCalVO[] calInterest(LoginContext context,
			UFDate trade_date) throws BusinessException {
		AcePayableinterestcalBP bp = new AcePayableinterestcalBP();
		return bp.calInterest(context, trade_date);
	}

	
	@Override
	public void cancelCal(LoginContext context, UFDate trade_date)
			throws BusinessException {
		AcePayableinterestcalBP bp = new AcePayableinterestcalBP();
		bp.cancelCal(context, trade_date);
	}

	
	@Override
	public void tally(LoginContext context) throws BusinessException {
		AcePayableinterestcalBP bp = new AcePayableinterestcalBP();
		bp.tally(context);
	}

	@Override
	public void saveTally(List<FundTallyVO> tallyList) throws BusinessException {
		BaseDAO basedao = new BaseDAO();
		basedao.insertVOList(tallyList);
	}

	
}
