package nc.impl.fba_sim.costplugin.stock;

import nc.bs.framework.common.NCLocator;
import nc.impl.fba_sim.costingplugin.AbstractStock;
import nc.impl.fba_sim.pub.RealratePlugin;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.pub.SystemConst;
import nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;



/**
 * 红利 收到兑付利息 ：证券不变，资金增加
 * 
 * @author Neil
 * 
 */
public class Bonus extends AbstractStock {


	protected void calculateWhenCheck(ICostingTool costingtool,
			StocktradeVO tradevo) throws Exception {
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool  tool = costingtool.getBalancetool();
		String[] costFieldArray=costingtool.getCostParaVO().getCostplanvo().getCostFieldArray();
		String key = VOUtil.getCombinesKey(tradevo,costFieldArray);
		/**
		 * 此处拼接key必须加上业务组
		 */
		key=costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()+key;
		BanlanceQueryKeyVO queryvo=new BanlanceQueryKeyVO();
		queryvo.setKey(key);
		queryvo.setPk_assetsprop(tradevo.getPk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getPk_stocksort());
		queryvo.setTrade_date(trade_date);
		
		StockBalanceVO stockbalancevo = tool.getStockbalanceVO(queryvo,  costingtool);
	
		if (stockbalancevo == null) {
			costingtool.handleException(tradevo,null, SystemConst.error[1]);
		}
		//应收利息结转
		UFDouble lx = costcalc.forwardInterestDistill(costingtool, stockbalancevo,tradevo);
		tradevo.setInterest(lx);
		//存取应收利息
		costcalc.saveInterestDistill(costingtool, tradevo);
		//实际利率
		RealratePlugin check = new RealratePlugin();
		if(check.isRealTradevo(tradevo.getPk_glorgbook())){
			ICalcPluginMaintain mail = (ICalcPluginMaintain)NCLocator.getInstance().lookup(ICalcPluginMaintain.class);
			mail.fxPlugindeal(tradevo);
		}
	}
	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			StocktradeVO tradevo) throws Exception {
		String pk_group = costingtool.getCostParaVO().getCheckParavo().getPk_group();
		String pk_org = costingtool.getCostParaVO().getCheckParavo().getPk_org();
		String tradedate = costingtool.getCostParaVO().getCheckParavo().getTrade_date().toLocalString();
		//清空结转应收利息
		tradevo.setInterest(null);
		//清空转出应收利息
		costcalc.clearDistillInterest(costingtool);
		//实际利率
		RealratePlugin check = new RealratePlugin();
		if(check.isRealTradevo(tradevo.getPk_glorgbook())){
			ICalcPluginMaintain mail = (ICalcPluginMaintain)NCLocator.getInstance().lookup(ICalcPluginMaintain.class);
			mail.unFxPlugindeal(tradevo, pk_group, pk_org, tradedate);
		}
	}
}
