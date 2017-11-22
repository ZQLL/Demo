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
	// 新增
	public FpxxBillVO[] pubinsertBills(FpxxBillVO[] vos)
			throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<FpxxBillVO> transferTool = new BillTransferTool<FpxxBillVO>(
					vos);
			FpxxBillVO[] mergedVO = transferTool.getClientFullInfoBill();

			// 调用BP
			AceHxzq_fpxxInsertBP action = new AceHxzq_fpxxInsertBP();
			FpxxBillVO[] retvos = action.insert(mergedVO);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(FpxxBillVO[] vos) throws BusinessException {
		try {
			// 加锁 比较ts
			BillTransferTool<FpxxBillVO> transferTool = new BillTransferTool<FpxxBillVO>(
					vos);
			FpxxBillVO[] fullBills = transferTool.getClientFullInfoBill();
			AceHxzq_fpxxDeleteBP deleteBP = new AceHxzq_fpxxDeleteBP();
			deleteBP.delete(fullBills);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public FpxxBillVO[] pubupdateBills(FpxxBillVO[] vos)
			throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<FpxxBillVO> transTool = new BillTransferTool<FpxxBillVO>(
					vos);
			// 补全前台VO
			FpxxBillVO[] fullBills = transTool.getClientFullInfoBill();
			// 获得修改前vo
			FpxxBillVO[] originBills = transTool.getOriginBills();
			// 调用BP
			AceHxzq_fpxxUpdateBP bp = new AceHxzq_fpxxUpdateBP();
			FpxxBillVO[] retBills = bp.update(fullBills, originBills);
			// 构造返回数据
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
	 * 由子类实现，查询之前对queryScheme进行加工，加入自己的逻辑
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// 查询之前对queryScheme进行加工，加入自己的逻辑
	}

}