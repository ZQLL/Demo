package nc.impl.fba_sabb.costplugin;

import nc.bs.framework.common.NCLocator;
import nc.itf.fba_scost.cost.pub.IBillCheckPlugin;
import nc.itf.fba_scost.cost.pub.SendVoucherTool;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostForward;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public abstract class AbstractPlugin implements IBillCheckPlugin {

	protected ICostForward costcalc = (ICostForward) NCLocator.getInstance().lookup(ICostForward.class.getName());

	protected PubMethod pm = new PubMethod();

	// 单据类型
	private String billtype = null;

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	/**
	 * 统一查询条件处理
	 * 
	 * @param condition
	 * @return
	 */
	protected String addCondition(String condition) {
		condition += " and a.pk_billtype='" + getBilltype() + "'";
		return condition;
	}

	/**
	 * 批量记账单据/批量取消记账 CostingTool istally true:发送增加消息 false:发送减少消息
	 * 
	 * @param costingtool
	 * 
	 * @throws Exception
	 * @author YangJie
	 */
	public void tallyBills(ICostingTool costingtool, TradeDataTool tradedatatool) throws Exception {
		// 业务组+业务日期+交易类型
		IBill[] ibills = tradedatatool.getData(getCalcCostKey(costingtool));
		if (ibills != null) {
			if (costingtool.getIstally()) { // 审核操作
				addVoucher(ibills);
			} else { // 弃审操作
				deleteVoucher(costingtool, ibills);
			}
		}
	}

	private void deleteVoucher(ICostingTool costingtool, IBill[] ibills) throws Exception {
		/**
		 * 取参数配置中是否弃审删除凭证
		 */
		boolean isDeleteVoucher = SysInitCache.getInstance().getIsDeleteVoucher(
				costingtool.getCostParaVO().getCheckParavo().getPk_group(),
				costingtool.getCostParaVO().getCheckParavo().getPk_org(),
				costingtool.getCostParaVO().getCheckParavo().getPk_glorgbook());
		if (isDeleteVoucher) {
			SendVoucherTool.getInstance().deleteRTVoucher(ibills);
			for (int i = 0; i < ibills.length; i++) {
				IBill iBill = ibills[i];
				iBill.getParent().setAttributeValue("state", SystemConst.statecode[3]);
				iBill.getParent().setStatus(VOStatus.UPDATED);
			}

		}
	}

	private void addVoucher(IBill[] ibills) throws Exception {
		SendVoucherTool.getInstance().addRTVoucher(ibills);
		for (int i = 0; i < ibills.length; i++) {
			IBill iBill = ibills[i];
			iBill.getParent().setAttributeValue("state", SystemConst.statecode[4]);
			iBill.getParent().setStatus(VOStatus.UPDATED);
		}
	}

	/**
	 * 获取计算成本的键值
	 * 
	 * @param costingtool
	 * @return
	 */
	protected String getCalcCostKey(ICostingTool costingtool) {

		if (costingtool == null)
			return null;

		// 业务组+业务日期+交易类型
		return costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + costingtool.getCurrdate()
				+ costingtool.getCurrbilltype();
	}
}
