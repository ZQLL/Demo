package nc.itf.fba_sim;

import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public abstract interface  ZqYjlxUtils {

	public UFDouble forwardInterestDistill(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, ITrade_Data vo)
			throws BusinessException;
	public void saveInterestDistill(ICostingTool costingtool, 	StockBalanceVO stockbalancevo, ITrade_Data vs)
			throws BusinessException;
	
}
