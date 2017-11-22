package nc.impl.pub.ace;

import nc.bs.erm.hxzq_fpxx.ace.bp.AceHxzq_fpxxDeleteBP;
import nc.bs.erm.hxzq_fpxx.ace.bp.AceHxzq_fpxxInsertBP;
import nc.bs.erm.hxzq_fpxx.ace.bp.AceHxzq_fpxxUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.hxzq.fpxx.FpxxBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceHxzq_fpxxPubServiceImpl {
	// ����
	public FpxxBillVO[] pubinsertBills(FpxxBillVO[] vos)
			throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<FpxxBillVO> transferTool = new BillTransferTool<FpxxBillVO>(
					vos);
			FpxxBillVO[] mergedVO = transferTool.getClientFullInfoBill();

			// ����BP
			AceHxzq_fpxxInsertBP action = new AceHxzq_fpxxInsertBP();
			FpxxBillVO[] retvos = action.insert(mergedVO);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(FpxxBillVO[] vos) throws BusinessException {
		try {
			// ���� �Ƚ�ts
			BillTransferTool<FpxxBillVO> transferTool = new BillTransferTool<FpxxBillVO>(
					vos);
			FpxxBillVO[] fullBills = transferTool.getClientFullInfoBill();
			AceHxzq_fpxxDeleteBP deleteBP = new AceHxzq_fpxxDeleteBP();
			deleteBP.delete(fullBills);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public FpxxBillVO[] pubupdateBills(FpxxBillVO[] vos)
			throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<FpxxBillVO> transTool = new BillTransferTool<FpxxBillVO>(
					vos);
			// ��ȫǰ̨VO
			FpxxBillVO[] fullBills = transTool.getClientFullInfoBill();
			// ����޸�ǰvo
			FpxxBillVO[] originBills = transTool.getOriginBills();
			// ����BP
			AceHxzq_fpxxUpdateBP bp = new AceHxzq_fpxxUpdateBP();
			FpxxBillVO[] retBills = bp.update(fullBills, originBills);
			// ���췵������
			retBills = transTool.getBillForToClient(retBills);
			return retBills;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public FpxxBillVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		FpxxBillVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<FpxxBillVO> query = new BillLazyQuery<FpxxBillVO>(
					FpxxBillVO.class);
			bills = query.query(queryScheme, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}

	/**
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

}