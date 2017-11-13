package nc.impl.fba_sim.costingplugin.backbuy;

import nc.impl.fba_sim.costingplugin.AbstractBackbuy;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class BackBuy extends AbstractBackbuy{
	
	/*
	 * 融资回购/融券购回 记录 插件类
	 */

	@Override
	public IBill[] queryData(ICostingTool costingtool) throws BusinessException {
		// TODO 自动生成的方法存根
		return super.queryData(costingtool);
	}

	@Override
	public IBill[] updateData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		// TODO 自动生成的方法存根
		return super.updateData(costingtool, tradedatatool);
	}

	@Override
	public IBill[] insertData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		// TODO 自动生成的方法存根
		return super.insertData(costingtool, tradedatatool);
	}

	@Override
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// TODO 自动生成的方法存根
		super.checkBills(costingtool, tradedatatool);
	}


}
