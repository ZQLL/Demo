package nc.impl.fba_sim.costingplugin.backbuy;

import nc.impl.fba_sim.costingplugin.AbstractBackbuy;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class BackBuy extends AbstractBackbuy{
	
	/*
	 * ���ʻع�/��ȯ���� ��¼ �����
	 */

	@Override
	public IBill[] queryData(ICostingTool costingtool) throws BusinessException {
		// TODO �Զ����ɵķ������
		return super.queryData(costingtool);
	}

	@Override
	public IBill[] updateData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		// TODO �Զ����ɵķ������
		return super.updateData(costingtool, tradedatatool);
	}

	@Override
	public IBill[] insertData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		// TODO �Զ����ɵķ������
		return super.insertData(costingtool, tradedatatool);
	}

	@Override
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// TODO �Զ����ɵķ������
		super.checkBills(costingtool, tradedatatool);
	}


}
