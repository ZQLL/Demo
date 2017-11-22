package nc.impl.fba_sim.trade.costplugin;

import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.tool.fba_zqjd.pub.ZqjdStockBalanceTool;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

/**
 * ծȯ������뵥
 * 
 * @author Administrator
 * 
 */
public class BondlendingInImpl extends AbstractZqjdTrade {

	private ZqjdStockBalanceTool stockBalanceTool = null;

	
	@Override
	protected void calculateWhenCheck(ICostingTool costingtool, ZqjdVO tradevo)
			throws Exception {
		// ����Ĭ��ֵ
		// tradevo = DefaultValTool.setDefaultVal(tradevo);

		BanlanceQueryKeyVO queryKeyVO = getStockBalanceTool()
				.createQryBalanceVO(costingtool, tradevo);
		// ��ѯ���
		StockBalanceVO stockBalanceVO = getStockBalanceTool().qryStockBalnce(
				queryKeyVO, costingtool, tradevo);
		// ������ ����T
		UFDouble befStockNum = stockBalanceVO.getStocks_num();
		UFDouble befStockSum = stockBalanceVO.getStocks_sum();
		stockBalanceVO.setStocks_num(SafeCompute.add(befStockNum,
				tradevo.getBargain_num()));
		stockBalanceVO.setStocks_sum(SafeCompute.add(befStockSum,
				tradevo.getBargain_sum()));
		// ���¿��
		getStockBalanceTool().updateStockBalace(queryKeyVO, costingtool,
				stockBalanceVO);
	}

	
	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool, ZqjdVO tradevo)
			throws Exception {

		// ˵�������¿���߼����Ѽ����������߼�����
		// @see
		// �ࣺBillCheckByGroupImpl
		// ������uncheckOneGroup_RequiresNew
		// �У�PrivateMethod.getInstance().getBaseDAO().deleteByClause(StockBalanceVO.class,
		// sqlwhere);

	}

	private ZqjdStockBalanceTool getStockBalanceTool() {
		if (stockBalanceTool == null) {
			stockBalanceTool = ZqjdStockBalanceTool.getInstance();
		}
		return stockBalanceTool;
	}

}
