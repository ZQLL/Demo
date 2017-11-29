package nc.impl.fba_sim.trade.costplugin;

import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;

/**
 * 费用计提
 * 
 * @author Administrator
 * 
 */
public class BondlendingFeeImpl extends AbstractZqjdTrade {

	@Override
	protected void calculateWhenCheck(ICostingTool costingtool, ZqjdVO tradevo)
			throws Exception {
		// Nothing to do
	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool, ZqjdVO tradevo)
			throws Exception {
	}

}
