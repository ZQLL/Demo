package nc.impl.erm.hxzq_fpxx;

import nc.impl.pub.ace.AceHxzq_fpxxPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.hxzq.fpxx.FpxxBillVO;
import nc.vo.pub.BusinessException;

public class Hxzq_fpxxMaintainImpl extends AceHxzq_fpxxPubServiceImpl implements
		nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain {

	@Override
	public void delete(FpxxBillVO[] vos) throws BusinessException {
		super.pubdeleteBills(vos);
	}

	@Override
	public FpxxBillVO[] insert(FpxxBillVO[] vos) throws BusinessException {
		return super.pubinsertBills(vos);
	}

	@Override
	public FpxxBillVO[] update(FpxxBillVO[] vos) throws BusinessException {
		return super.pubupdateBills(vos);
	}

	@Override
	public FpxxBillVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

}
