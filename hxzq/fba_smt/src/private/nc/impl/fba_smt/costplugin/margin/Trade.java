package nc.impl.fba_smt.costplugin.margin;

import nc.bs.fba_smt.pub.TaxTransfer;
import nc.impl.fba_smt.costingplugin.AbstractMargin;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_smt.trade.margintrade.MarginTradeVO;
import nc.vo.fba_smt.trade.pub.SystemConst;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

/**
 * 融券交易：融出、逾期归还等，一个库存减少，一个库存增加
 * 
 * @author	liangwei
 * @date	2014-4-15 上午9:04:35
 * @version	1.0.0
 */
public class Trade extends AbstractMargin {

	private PubMethod pm = PubMethod.getInstance();
	
	protected void calculate(ICostingTool costingtool, MarginTradeVO tradevo)
			throws Exception {
		String trade_date = costingtool.getCurrdate();
		String[] costFieldArray=costingtool.getCostParaVO().getCostplanvo().getCostFieldArray();
		boolean isFirstPrice = costingtool.getCostParaVO().getFirstPrice();
		String pk_group=costingtool.getCostParaVO().getCheckParavo().getPk_group();
		String pk_org=costingtool.getCostParaVO().getCheckParavo().getPk_org();
		String pk_glorgbook=costingtool.getCostParaVO().getCheckParavo().getPk_glorgbook();
		boolean istransfeftax=SysInitCache.getInstance().getIsTaxTransfer(pk_group, pk_org, pk_glorgbook);
		// 划出
		ICostBalanceTool  tool = costingtool.getBalancetool();
		//查询需要库存组织，先给赋值
		tradevo.setPk_stocksort(costingtool.getPk_stocksort()[0]);
		String key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()+VOUtil.getCombinesKey(tradevo,costFieldArray);
		BanlanceQueryKeyVO sbqkvo=new BanlanceQueryKeyVO();
		sbqkvo.setKey(key);
		sbqkvo.setPk_assetsprop(tradevo.getPk_assetsprop());
		sbqkvo.setPk_stocksort(costingtool.getPk_stocksort()[0]);
		sbqkvo.setTrade_date(trade_date);
		StockBalanceVO stockbalancevo = tool.getStockbalanceVO(sbqkvo, costingtool);
		if (stockbalancevo == null) {
			costingtool.handleException(tradevo,null, SystemConst.error[0]);
			return;
		}
		//计算结转公允价值
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,stockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		// 库存数量
		UFDouble stocks_num = stockbalancevo.getStocks_num();
		// 库存金额
		UFDouble stocks_sum = stockbalancevo.getStocks_sum();
		// 库存利息
		UFDouble accrual_sum = null;
		//税费
		UFDouble taxOutCost = null;
		//交易类型
		String transtype_code = tradevo.getTranstypecode();
		//买入利息
		UFDouble interest = stockbalancevo.getAccrual_sum();
		// 获得剩余数量
		stocks_num = pm.sub(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		
		
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// 卖空
			stocks_sum = stockbalancevo.getStocks_sum();
			taxOutCost = stockbalancevo.getStocks_tax();
			if(null==tradevo.getBargain_sum()){
				tradevo.setBargain_sum(stocks_sum);
				stocks_sum = new UFDouble(0);//库存金额卖空
			}
			
		} else if (stocks_num.compareTo(new UFDouble(0)) < 0) {
//			if (costingtool.isCheckStock()) {
				costingtool.handleException(tradevo, null,SystemConst.error[0]);
//			}
		} else {// 成本
			if (isFirstPrice) {// 先计算单价
				stocks_sum = pm.div(stockbalancevo.getStocks_sum(),
						stockbalancevo.getStocks_num());
				stocks_sum = pm.multiply(stocks_sum, tradevo.getBargain_num());
				accrual_sum = pm.div(accrual_sum, stockbalancevo.getStocks_num());
				accrual_sum = pm.multiply(accrual_sum, tradevo.getBargain_num());
				interest = pm.div(interest, stockbalancevo.getStocks_num());
				interest = pm.multiply(interest, tradevo.getBargain_num());
				tradevo.setBargain_sum(pm.setScale(stocks_sum, true, true));
				stocks_sum = pm.sub(stockbalancevo.getStocks_sum(),tradevo.getBargain_sum());
			} else {
				//若未输入成交金额，根据库存成本计算  
				if(null==tradevo.getBargain_sum()){
					UFDouble div = pm.div(tradevo.getBargain_num().multiply(10000), stockbalancevo.getStocks_num());
					UFDouble multi = pm.multiply(stockbalancevo.getStocks_sum(),div);
					multi = pm.setScale(multi.div(10000),true,true);
					tradevo.setBargain_sum(multi);
					//tradevo.setBargain_sum(pm.setScale(pm.multiply(stockbalancevo.getStocks_sum(), pm.div(tradevo.getBargain_num(), stockbalancevo.getStocks_num())), true, true));
					//按比例进行更新 
					stocks_sum=pm.sub(stockbalancevo.getStocks_sum(), tradevo.getBargain_sum());
					//更新tradevo回写Bargain_sum字段值
				}else{
					stocks_sum = pm.sub(stockbalancevo.getStocks_sum(),
							tradevo.getBargain_sum());
				}
				
				accrual_sum = pm.multiply(accrual_sum, tradevo.getBargain_num());
				accrual_sum = pm.div(accrual_sum, stockbalancevo.getStocks_num());
				interest = pm.multiply(interest, tradevo.getBargain_num());
				interest = pm.div(interest, stockbalancevo.getStocks_num());
			}
			//税费计算
			if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH1.getCode())){
				//可供转可融记录：计算方法为权重(可融方)计算法
				stocks_sum = pm.multiply(stockbalancevo.getStocks_sum(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum, stockbalancevo.getStocks_num());
//				taxOutCost = pm.multiply(stockbalancevo.getStocks_tax(),
//						tradevo.getBargain_num());
//				taxOutCost = pm.div(taxOutCost, stockbalancevo.getStocks_num());
				taxOutCost=new TaxTransfer().getTransferTax(tradevo, stockbalancevo);
			}else if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH2.getCode())){
				//可融转可供记录：同一机构间的已融权重计算
				stocks_sum = pm.multiply(stockbalancevo.getNdef8(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum, stockbalancevo.getNdef6());
				if(istransfeftax){
					taxOutCost = pm.multiply(stockbalancevo.getNdef7(),
							tradevo.getBargain_num());
					taxOutCost = pm.div(taxOutCost, stockbalancevo.getNdef6());
				}
			}else if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH3.getCode()) || transtype_code.equals(SystemConst.SMT_TransType.MarginTSH4.getCode())){
				//融出记录，融出归还记录：只转移数量，不转移税费
				taxOutCost = new UFDouble(0.0);
			}
			stocks_sum = pm.setScale(stocks_sum, true, true);
			taxOutCost = pm.setScale(taxOutCost, true, true);
			interest = pm.setScale(interest, true, true);
		}
		tradevo.setTaxexpense(taxOutCost);
		tradevo.setInterest(interest);
		tradevo.setAccrual_sum(accrual_sum);
		if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH1.getCode())){
		    stockbalancevo.setStocks_tax(pm.sub(stockbalancevo.getStocks_tax(),
				taxOutCost));
		}else if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH2.getCode())){
			//可融转可供
			UFDouble vndef6 = pm.sub(stockbalancevo.getNdef6(), tradevo.getBargain_num());
			UFDouble vndef7 = pm.sub(stockbalancevo.getNdef7(), taxOutCost);
			UFDouble vndef8 = pm.sub(stockbalancevo.getNdef8(), tradevo.getBargain_sum());
			stockbalancevo.setNdef6(pm.setScale(vndef6, true, true));//用自定义项6存储转出的数量
			stockbalancevo.setNdef7(pm.setScale(vndef7, true, true));//用自定义项6存储转出的税费
			stockbalancevo.setNdef8(pm.setScale(vndef8, true, true));//用自定义项6存储转出的价格
		}
		stockbalancevo.setStocks_sum(stocks_sum);
		stockbalancevo.setStocks_num(stocks_num);
		stockbalancevo.setAccrual_sum(pm.sub(stockbalancevo.getAccrual_sum(),
				interest));
		tool.updateStockbalanceVO(sbqkvo, stockbalancevo);
		costingtool.addCostChangeBalanceVO(stockbalancevo,costFieldArray);
		
		// 划入
		tradevo.setPk_stocksort(costingtool.getPk_stocksort()[1]);
		key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()+VOUtil.getCombinesKey(tradevo,costFieldArray);
		sbqkvo.setKey(key);
		sbqkvo.setPk_stocksort(tradevo.getPk_stocksort());
		stockbalancevo = tool.getStockbalanceVO(sbqkvo, costingtool);
		if (stockbalancevo == null) {
			stockbalancevo = tool.getStockbalanceVOByVO(tradevo,costingtool);
		}
		
		
		if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH1.getCode())){
			//可供转可融
			UFDouble vndef6 = pm.add(stockbalancevo.getNdef6(), tradevo.getBargain_num());
			UFDouble vndef7 = pm.add(stockbalancevo.getNdef7(), taxOutCost);
			UFDouble vndef8 = pm.add(stockbalancevo.getNdef8(), tradevo.getBargain_sum());
			stockbalancevo.setNdef6(pm.setScale(vndef6, true, true));//用自定义项6存储转出的数量
			stockbalancevo.setNdef7(pm.setScale(vndef7, true, true));//用自定义项6存储转出的税费
			stockbalancevo.setNdef8(pm.setScale(vndef8, true, true));//用自定义项6存储转出的价格
			stockbalancevo.setPk_billtype(transtype_code);//存储来源的单据类型
		}else if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH2.getCode())){
			//可融转可供
	    	stocks_sum = pm.add(stockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum());
    		stockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));
	    	stockbalancevo.setStocks_tax(pm.add(stockbalancevo.getStocks_tax(), taxOutCost));
 		}
		stocks_num = pm.add(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		stocks_sum = pm.add(stockbalancevo.getStocks_sum(),
					tradevo.getBargain_sum());
		
		stockbalancevo.setStocks_num(stocks_num);
		stockbalancevo.setStocks_sum(stocks_sum);
		stockbalancevo.setAccrual_sum(pm.add(stockbalancevo.getAccrual_sum(), interest));
		tool.updateStockbalanceVO(sbqkvo, stockbalancevo);
		costingtool.addCostChangeBalanceVO(stockbalancevo, costFieldArray);
		//清空库存组织
		tradevo.setPk_stocksort(null);
		//保存公允价值---
		MarginTradeVO tradevo1 = (MarginTradeVO)tradevo.clone();
		tradevo1.setPk_stocksort(costingtool.getPk_stocksort()[0]);
		tradevo1.setHr_pk_stocksort(costingtool.getPk_stocksort()[1]);
		costcalc.saveFairValueDistill(costingtool, tradevo1);
	}
	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			MarginTradeVO tradevo) throws Exception{
		//清空结转公允价值
		tradevo.setFairvalue(null);
		//清空公允价值数据
		costcalc.clearDistillFv(costingtool);
	}
	
}
