package nc.impl.fba_sim.costingplugin.transform;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.impl.fba_sim.costingplugin.AbstractTransform;
import nc.impl.fba_sim.pub.RealratePlugin;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * 证券转换 ：一个同买入，一个同卖出，包含成本和利息，资金不变
 */
public class StockTransform extends AbstractTransform {
	protected void calculate(ICostingTool costingtool, TransformtradeVO tradevo)
			throws Exception {
		PubMethod pm = PubMethod.getInstance();
		boolean isFirstPrice = costingtool.getCostParaVO().getFirstPrice();
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool balanceTool = costingtool.getBalancetool();
		String[] keys = new String[costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray().length - 1];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = costingtool.getCostParaVO().getCostplanvo()
					.getCostFieldArray()[i];
		}
		String hckey = getCombinesKey(tradevo, keys, false);
		/**
		 * 此处拼接key必须加上业务组
		 */
		hckey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hckey;
		hckey = hckey + tradevo.getPk_securities();
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(hckey);
		queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
		queryvo.setTrade_date(trade_date);
		// 库存余额表
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}

		// zpm 实际利率 增加开始，位置不要动
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}

		// 计算结转应收利息
		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// 计算结转公允价值
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		//
		// 库存数量
		UFDouble stocks_num = outstockbalancevo.getStocks_num();
		// 库存金额
		UFDouble stocks_sum = outstockbalancevo.getStocks_sum();
		/**
		 * 转入数量增加字段原转入数量（bargain_num2_his）记录原来的转入数量，弃审时用来还原
		 * 
		 * @author cjh
		 * @date 2015-12-08
		 */
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

			tradevo.setBargain_num2(tradevo.getBargain_num());// 如果转入数量为空，那么转入数量等于转出数量
		}

		// 获得剩余数量
		stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		/******根据转出占比计算转出证券的计提未付收益 add by lihbj start*******/
		if (hasSameTradeDate(tradevo)) {
			tradevo.setVdef2(UFDouble.ZERO_DBL.toString());
		} else {
			Double bl = tradevo.getBargain_num().div(outstockbalancevo.getStocks_num()).toDouble();
			UFDouble sum_jzwfsy = sumJZWFSY(tradevo).multiply(bl).setScale(2, UFDouble.ROUND_HALF_UP);
			tradevo.setVdef2(sum_jzwfsy.toString());
		}
		/******根据转出占比计算转出证券的计提未付收益 add by lihbj end*******/
		// 判断转出是否合理
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// 卖空
			// 如果剩余数量为0 代表全部卖出 转出金额为库存金额
			stocks_sum = outstockbalancevo.getStocks_sum();
		} else {// 销售成本
			// 如果转出合理 计算转出金额
			if (isFirstPrice) {// 先计算单价
				stocks_sum = pm.div(outstockbalancevo.getStocks_sum(),
						outstockbalancevo.getStocks_num());
				stocks_sum = pm.multiply(stocks_sum, tradevo.getBargain_num());
			} else {
				stocks_sum = pm.multiply(outstockbalancevo.getStocks_sum(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum,
						outstockbalancevo.getStocks_num());
			}
			stocks_sum = pm.setScale(stocks_sum, true, true);
		}
		UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo, costingtool,
				tradevo.getBargain_num());
		tradevo.setTaxexpense(taxOutCost);
		/**
		 * 证券转换记录审核时转出金额有值则不需回写否则回写：转出金额=库存金额/库存数量*转出数量，原逻辑没有加判断都直接回写
		 * 
		 * @author cjh
		 * @date 2015-12-07 09：55
		 */
		if (tradevo.getBargain_sum() == null
				|| tradevo.getBargain_sum().compareTo(new UFDouble(0)) <= 0) {
			tradevo.setBargain_sum(stocks_sum);
			tradevo.setAttributeValue("bargain_sum_his", null);
		} else {
			tradevo.setAttributeValue("bargain_sum_his",
					tradevo.getBargain_sum());
		}
		// tradevo.setBargain_sum(stocks_sum);

		/**
		 * 证券交易记录审核时转入金额有值则不需回写否则回写,原逻辑没有加判断都直接回写
		 * 
		 * @author cjh
		 * @date 2015-12-07 09：55
		 */
		if (tradevo.getBargain_sum2() == null
				|| tradevo.getBargain_sum2().compareTo(new UFDouble(0)) <= 0) {
			tradevo.setBargain_sum2(tradevo.getBargain_sum());
			tradevo.setAttributeValue("bargain_sum2_his", null);
		} else {
			tradevo.setAttributeValue("bargain_sum2_his",
					tradevo.getBargain_sum2());
			// tradevo.setBargain_sum2(tradevo.getBargain_sum());
		}
		// tradevo.setBargain_sum2(stocks_sum);
		tradevo.setSellcost(stocks_sum);
		outstockbalancevo.setStocks_num(stocks_num);
		outstockbalancevo.setStocks_sum(pm.sub(
				outstockbalancevo.getStocks_sum(), stocks_sum));
		outstockbalancevo.setStocks_tax(pm.sub(
				outstockbalancevo.getStocks_tax(), taxOutCost));
		// 更新转出库存
		balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);
		// 获得转入的库存
		String hrkey = getCombinesKey(tradevo, keys, true);
		/**
		 * 此处拼接key必须加上业务组
		 */
		hrkey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hrkey;
		hrkey = hrkey + tradevo.getPk_securities2();
		queryvo.setKey(hrkey);
		queryvo.setPk_assetsprop(tradevo.getHr_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHr_pk_stocksort());
		StockBalanceVO instockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (instockbalancevo == null) {
			instockbalancevo = balanceTool.getStockbalanceVOByVO(tradevo,
					costingtool);
			instockbalancevo.setPk_securities(tradevo.getPk_securities2());
		}
		// 获得转入的数量
		stocks_num = pm.add(instockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// 获得转入的金额
		stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());
		// 保存转入信息
		instockbalancevo.setStocks_num(stocks_num);
		instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));
		instockbalancevo.setStocks_tax(pm.add(instockbalancevo.getStocks_tax(),
				taxOutCost));
		// 更新转入的库存
		balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
		// 处理利息余额
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// 存取应收利息---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// 保存公允价值---
		costcalc.saveFairValueDistill(costingtool, tradevo);
		// 实际利率
		RealratePlugin check = new RealratePlugin();
		if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
			ICalcPluginMaintain mail = (ICalcPluginMaintain) NCLocator
					.getInstance().lookup(ICalcPluginMaintain.class);
			mail.ztgPlugindeal(outstockbalancevo, tradevo);
		}
	}
	protected void calculate(ICostingTool costingtool, TransformtradeVO tradevo,UFDouble allstock,int count,List<UFDouble> midvdeflist,List<TransformtradeVO> alllist)
			throws Exception {
		PubMethod pm = PubMethod.getInstance();
		boolean isFirstPrice = costingtool.getCostParaVO().getFirstPrice();
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool balanceTool = costingtool.getBalancetool();
		String[] keys = new String[costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray().length - 1];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = costingtool.getCostParaVO().getCostplanvo()
					.getCostFieldArray()[i];
		}
		String hckey = getCombinesKey(tradevo, keys, false);
		/**
		 * 此处拼接key必须加上业务组
		 */
		hckey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hckey;
		hckey = hckey + tradevo.getPk_securities();
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(hckey);
		queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
		queryvo.setTrade_date(trade_date);
		// 库存余额表
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}

		// zpm 实际利率 增加开始，位置不要动
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}

		// 计算结转应收利息
		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// 计算结转公允价值
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		//
		// 库存数量
		UFDouble stocks_num = outstockbalancevo.getStocks_num();
		// 库存金额
		UFDouble stocks_sum = outstockbalancevo.getStocks_sum();
		/**
		 * 转入数量增加字段原转入数量（bargain_num2_his）记录原来的转入数量，弃审时用来还原
		 * 
		 * @author cjh
		 * @date 2015-12-08
		 */
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

			tradevo.setBargain_num2(tradevo.getBargain_num());// 如果转入数量为空，那么转入数量等于转出数量
		}

		// 获得剩余数量
		stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		/****** 根据转出占比计算转出证券的计提未付收益 add by lihbj start *******/
		UFDouble sum_jzwfsy = null;
		UFDouble per_jzwfsy = null;
		int allt = 0;
		if (hasSameTradeDate(tradevo)) {
			tradevo.setVdef2(UFDouble.ZERO_DBL.toString());
		} else {
			allt = alllist.size();	
			TransformtradeVO[] alltftvo = new TransformtradeVO[allt];
				
			for (int i = 0; i < allt; i++) {
				alltftvo[i] = alllist.get(i);
			}
				
			UFDouble ZCall = new UFDouble(0);
			for(int i = 0; i<allt;i++){
				ZCall = ZCall.add(alltftvo[i].getBargain_num());
			}
			alltftvo[0].setBargain_num(ZCall);
			Double b2 = ZCall
					.div(allstock).toDouble();
			sum_jzwfsy = sumJZWFSY(alltftvo[0]).multiply(b2).setScale(2,
					UFDouble.ROUND_HALF_UP);
			if(count == allt-1){
				for(int i = 0; i<allt-1;i++){
					sum_jzwfsy = sum_jzwfsy.sub(new UFDouble(midvdeflist.get(i).toString()));
					per_jzwfsy = sum_jzwfsy;
				}
			}else{
				per_jzwfsy = tradevo.getBargain_num().div(ZCall).multiply(sum_jzwfsy).setScale(2,
						UFDouble.ROUND_HALF_UP);
				midvdeflist.add(per_jzwfsy);
			}
			tradevo.setVdef2(per_jzwfsy.toString());
		}
		//交易日期当日的转出数据审核完成之后，count，allstock复位
		/****** 根据转出占比计算转出证券的计提未付收益 add by lihbj end *******/
		// 判断转出是否合理
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// 卖空
			// 如果剩余数量为0 代表全部卖出 转出金额为库存金额
			stocks_sum = outstockbalancevo.getStocks_sum();
		} else {// 销售成本
			// 如果转出合理 计算转出金额
			if (isFirstPrice) {// 先计算单价
				stocks_sum = pm.div(outstockbalancevo.getStocks_sum(),
						outstockbalancevo.getStocks_num());
				stocks_sum = pm.multiply(stocks_sum, tradevo.getBargain_num());
			} else {
				stocks_sum = pm.multiply(outstockbalancevo.getStocks_sum(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum,
						outstockbalancevo.getStocks_num());
			}
			stocks_sum = pm.setScale(stocks_sum, true, true);
		}
		UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo, costingtool,
				tradevo.getBargain_num());
		tradevo.setTaxexpense(taxOutCost);
		/**
		 * 证券转换记录审核时转出金额有值则不需回写否则回写：转出金额=库存金额/库存数量*转出数量，原逻辑没有加判断都直接回写
		 * 
		 * @author cjh
		 * @date 2015-12-07 09：55
		 */
		if (tradevo.getBargain_sum() == null
				|| tradevo.getBargain_sum().compareTo(new UFDouble(0)) <= 0) {
			tradevo.setBargain_sum(stocks_sum);
			tradevo.setAttributeValue("bargain_sum_his", null);
		} else {
			tradevo.setAttributeValue("bargain_sum_his",
					tradevo.getBargain_sum());
		}
		// tradevo.setBargain_sum(stocks_sum);

		/**
		 * 证券交易记录审核时转入金额有值则不需回写否则回写,原逻辑没有加判断都直接回写
		 * 
		 * @author cjh
		 * @date 2015-12-07 09：55
		 */
		if (tradevo.getBargain_sum2() == null
				|| tradevo.getBargain_sum2().compareTo(new UFDouble(0)) <= 0) {
			tradevo.setBargain_sum2(tradevo.getBargain_sum());
			tradevo.setAttributeValue("bargain_sum2_his", null);
		} else {
			tradevo.setAttributeValue("bargain_sum2_his",
					tradevo.getBargain_sum2());
			// tradevo.setBargain_sum2(tradevo.getBargain_sum());
		}
		// tradevo.setBargain_sum2(stocks_sum);
		tradevo.setSellcost(stocks_sum);
		outstockbalancevo.setStocks_num(stocks_num);
		outstockbalancevo.setStocks_sum(pm.sub(
				outstockbalancevo.getStocks_sum(), stocks_sum));
		outstockbalancevo.setStocks_tax(pm.sub(
				outstockbalancevo.getStocks_tax(), taxOutCost));
		// 更新转出库存
		balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);
		// 获得转入的库存
		String hrkey = getCombinesKey(tradevo, keys, true);
		/**
		 * 此处拼接key必须加上业务组
		 */
		hrkey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hrkey;
		hrkey = hrkey + tradevo.getPk_securities2();
		queryvo.setKey(hrkey);
		queryvo.setPk_assetsprop(tradevo.getHr_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHr_pk_stocksort());
		StockBalanceVO instockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (instockbalancevo == null) {
			instockbalancevo = balanceTool.getStockbalanceVOByVO(tradevo,
					costingtool);
			instockbalancevo.setPk_securities(tradevo.getPk_securities2());
		}
		// 获得转入的数量
		stocks_num = pm.add(instockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// 获得转入的金额
		stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());
		// 保存转入信息
		instockbalancevo.setStocks_num(stocks_num);
		instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));
		instockbalancevo.setStocks_tax(pm.add(instockbalancevo.getStocks_tax(),
				taxOutCost));
		// 更新转入的库存
		balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
		// 处理利息余额
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// 存取应收利息---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// 保存公允价值---
		costcalc.saveFairValueDistill(costingtool, tradevo);
		// 实际利率
		RealratePlugin check = new RealratePlugin();
		if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
			ICalcPluginMaintain mail = (ICalcPluginMaintain) NCLocator
					.getInstance().lookup(ICalcPluginMaintain.class);
			mail.ztgPlugindeal(outstockbalancevo, tradevo);
		}
	}
	
	protected UFDouble calculatelaststock(ICostingTool costingtool, TransformtradeVO tradevo)
			throws Exception {
		PubMethod pm = PubMethod.getInstance();
		boolean isFirstPrice = costingtool.getCostParaVO().getFirstPrice();
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool balanceTool = costingtool.getBalancetool();
		String[] keys = new String[costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray().length - 1];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = costingtool.getCostParaVO().getCostplanvo()
					.getCostFieldArray()[i];
		}
		String hckey = getCombinesKey(tradevo, keys, false);
		/**
		 * 此处拼接key必须加上业务组
		 */
		hckey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hckey;
		hckey = hckey + tradevo.getPk_securities();
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(hckey);
		queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
		queryvo.setTrade_date(trade_date);
		// 库存余额表
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return new UFDouble(0);
		}

		// zpm 实际利率 增加开始，位置不要动
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}
		return  outstockbalancevo.getStocks_num();
	}

	/*
	 * 判断交易日期当天所有存在的转出交易证券记录
	 *  * 
	 * @author zq
	 * 
	 * @param tradevo
	 */
	
	@SuppressWarnings("unchecked")
	private List<TransformtradeVO> selectAllTransformtradeVO(TransformtradeVO tradevo) {
		List<TransformtradeVO> list = new ArrayList<TransformtradeVO>();
		String sql = "select *\n" + "  from sim_transformtrade a\n"
				+ " where nvl(dr, 0) = 0\n"+"and trade_date=?\n"
				+ "   and pk_org = ?\n" + "   and hc_pk_assetsprop = ?\n"
				+ "   and hc_pk_capaccount = ?\n"
				+ "   and hc_pk_stocksort = ?\n" + "   and pk_securities = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(tradevo.getTrade_date());
		para.addParam(tradevo.getPk_org());
		para.addParam(tradevo.getPk_assetsprop());
		para.addParam(tradevo.getPk_capaccount());
		para.addParam(tradevo.getPk_stocksort());
		para.addParam(tradevo.getPk_securities());
		BaseDAO dao = new BaseDAO();
		try {
			 list = (List<TransformtradeVO>) dao.executeQuery(sql, para, new BeanListProcessor(TransformtradeVO.class));
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return list;

	}

	
	
	/**
	 * 默认审核 业务有特殊需求的话需要重写
	 */
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// 处理单据上的应收利息、转出利息
		// bicbo.calcBondTradeBillArrayInterest(costingtool,
		// getData().toArray(new SuperVO[0]));
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		String datakey = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup()
				+ costingtool.getCurrdate()
				+ costingtool.getCurrbilltype();
		IBill[] ibills = tradedatatool.getData(datakey);
		if (ibills != null && ibills.length > 0) {
			List<UFDouble> midvdeflist = new ArrayList<UFDouble>();
			int count = 0;
			UFDouble allstock = new UFDouble();
			for (IBill aggvo : ibills) {
				TransformtradeVO fathervo = (TransformtradeVO) aggvo
						.getParent();// 无孩子
				if (fathervo.getHc_pk_stocksort() == null) {
					fathervo.setHc_pk_stocksort(costingtool.getPk_stocksort()[0]);// 影响库存1
				}
				if (fathervo.getHr_pk_stocksort() == null) {
					fathervo.setHr_pk_stocksort(costingtool.getPk_stocksort()[1]);// 影响库存2
				}
				// vo.setPk_stocksort(vo.getHr_pk_stocksort());// 转入库存2
				// vo.setPk_assetsprop(vo.getHr_pk_assetsprop());// 转入库存2
				List<TransformtradeVO> alllist = selectAllTransformtradeVO(fathervo);//找到交易当天所有转出记录
				int allt = alllist.size();
				if(allt>1){
					if(fathervo.getBillno().equals(alllist.get(0).getBillno())){
						allstock = calculatelaststock(costingtool, fathervo);
					}
					calculate(costingtool, fathervo,allstock,count,midvdeflist,alllist);
					count++;
				}else if(allt<=1){
					calculate(costingtool, fathervo);
				}
				if(count == alllist.size()){
					count = 0;
					allstock = new UFDouble(0);
					midvdeflist.clear();
				}
				costingtool.updateFunds(fathervo);
				fathervo.setApprover(pk_user);
				fathervo.setApprovedate(PubMethod.getInstance().getDateTime(
						costingtool.getCurrdate()));
				fathervo.setState(SystemConst.statecode[3]);
			}
		}
	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			TransformtradeVO tradevo) throws Exception {
		String pk_group = costingtool.getCostParaVO().getCheckParavo()
				.getPk_group();
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();
		String tradedate = costingtool.getCostParaVO().getCheckParavo()
				.getTrade_date().toLocalString();
		// 清空结转应收利息
		tradevo.setInterest(null);
		// 清空结转公允价值
		tradevo.setFairvalue(null);
		// 清空转出应收利息
		costcalc.clearDistillInterest(costingtool);
		// 清空公允价值数据
		costcalc.clearDistillFv(costingtool);
		// 清空转出结转税费
		tradevo.setTaxexpense(null);
		// 清空vdef2 add by lihbj
		tradevo.setVdef2(null);
		/**
		 * 证券转换记录反审核时将转出金额、转入金额、转入数量还原
		 * 
		 * @author cjh
		 * @date 2015-12-07
		 */
		tradevo.setBargain_sum((UFDouble) tradevo
				.getAttributeValue("bargain_sum_his"));
		tradevo.setBargain_sum2((UFDouble) tradevo
				.getAttributeValue("bargain_sum2_his"));
		tradevo.setBargain_num2((UFDouble) tradevo
				.getAttributeValue("bargain_num2_his"));

		// 实际利率
		RealratePlugin check = new RealratePlugin();
		if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
			ICalcPluginMaintain mail = (ICalcPluginMaintain) NCLocator
					.getInstance().lookup(ICalcPluginMaintain.class);
			mail.unZtgPlugindeal(tradevo, pk_group, pk_org, tradedate);
		}
	}

	/**
	 * 判断在转换日期的同时是否发生份额分红 若有 vdef2 直接赋值0，否则按原来逻辑取数
	 * 
	 * @author lihbj
	 * @return
	 */
	private boolean hasSameTradeDate(TransformtradeVO tradevo) {
		boolean hasSameTradeDate = false;
		String sql = "select count(a.pk_securities)\n"
				+ "  from sim_stocktrade a\n" + "  inner join sec_busitype b\n"
				+ "  on a.pk_busitype = b.pk_busitype\n"
				+ " where nvl(a.dr, 0) = 0\n"
				+ "  and a.trade_date like ?\n"
				+ "  and b.code in ('0184','104','205')\n"
				+
				// "  and a.transtypecode = 'HV3A-0xx-05'\n" +
				"  and a.pk_org = ?\n" + "  and a.pk_assetsprop = ?\n"
				+ "  and a.pk_capaccount = ?\n" + "  and a.pk_stocksort = ?\n"
				+ "  and a.pk_securities = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(tradevo.getTrade_date().toString().substring(0, 10) + "%");
		para.addParam(tradevo.getPk_org());
		para.addParam(tradevo.getPk_assetsprop());
		para.addParam(tradevo.getPk_capaccount());
		para.addParam(tradevo.getPk_stocksort());
		para.addParam(tradevo.getPk_securities());
		try {
			int total = (Integer) new BaseDAO().executeQuery(sql, para,
					new ColumnProcessor());
			if (total > 0) {
				hasSameTradeDate = true;
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return hasSameTradeDate;
	}

	/**
	 * 计算上一次份额分红之后，所有计提未付收益相加
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumJZWFSY(TransformtradeVO tradevo) {
		UFDouble sum_jzwfsy = UFDouble.ZERO_DBL;
		String sql = "select to_char(nvl(sum(a.bargain_sum), 0))\n"
				+ "  from sim_stocktrade a\n" + " inner join sec_busitype b\n"
				+ "    on a.pk_busitype = b.pk_busitype\n"
				+ " where nvl(a.dr, 0) = 0\n" + "   and nvl(b.dr, 0) = 0\n"
				+ "   and a.trade_date < ?\n" +
				/*
				 * "   and a.trade_date > (select nvl(max(a.trade_date), '2000-01-01 15:15:08') trade_date\n"
				 * + "                         from sim_stocktrade a\n" +
				 * "                        where nvl(a.dr, 0) = 0\n" +
				 * "                          and a.trade_date < ?\n" +
				 * "                          and a.transtypecode = 'HV3A-0xx-05'\n"
				 * + "                          and a.state > 1\n" +
				 * "                          and a.pk_org = ?\n" +
				 * "                          and a.pk_assetsprop = ?\n" +
				 * "                          and a.pk_capaccount = ?\n" +
				 * "                          and a.pk_stocksort = ?\n" +
				 * "                          and a.pk_securities = ?)\n" +
				 */
				"   and b.code in ('206','106')\n" + "   and a.state > 1\n"
				+ "   and a.pk_org = ?\n" + "   and a.pk_assetsprop = ?\n"
				+ "   and a.pk_capaccount = ?\n"
				+ "   and a.pk_stocksort = ?\n" + "   and a.pk_securities = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(tradevo.getTrade_date());
		/*
		 * para.addParam(tradevo.getTrade_date());
		 * para.addParam(tradevo.getPk_org());
		 * para.addParam(tradevo.getPk_assetsprop());
		 * para.addParam(tradevo.getPk_capaccount());
		 * para.addParam(tradevo.getPk_stocksort());
		 * para.addParam(tradevo.getPk_securities());
		 */
		para.addParam(tradevo.getPk_org());
		para.addParam(tradevo.getPk_assetsprop());
		para.addParam(tradevo.getPk_capaccount());
		para.addParam(tradevo.getPk_stocksort());
		para.addParam(tradevo.getPk_securities());
		try {
			String total = (String) new BaseDAO().executeQuery(sql, para,
					new ColumnProcessor());
			sum_jzwfsy = new UFDouble(total);
			sum_jzwfsy = sum_jzwfsy.add(sumJZWFSY_ZH(tradevo)).sub(
					sumVDEF3(tradevo));
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return sum_jzwfsy;
	}

	/**
	 * 计算 转换转入-转换转出 转换转入（转入证券对应的VDEF2） 转换转出（转出证券对应的VDEF2）
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumJZWFSY_ZH(TransformtradeVO repVo) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		// 转出证券
		String sql = "select to_char(nvl(sum(case\n"
				+ "                         when a.vdef2 is null then\n"
				+ "                          0\n"
				+ "                         when a.vdef2 = '~' then\n"
				+ "                          0\n"
				+ "                         else\n"
				+ "                          to_number(a.vdef2)\n"
				+ "                       end),\n"
				+ "                   0)) total_zc\n"
				+ "  from sim_transformtrade a\n"
				+ " inner join sec_securities b\n"
				+ "    on a.pk_securities = b.pk_securities\n"
				+ " inner join sec_billtype c\n"
				+ "    on a.transtypecode = c.pk_billtypecode\n"
				+ "   and nvl(c.dr, 0) = 0\n" + " where a.state > 1\n"
				+ "   and nvl(a.dr, 0) = 0\n"
				+ "   and a.transtypecode = 'HV3F-0xx-01'\n"
				+ "   and a.trade_date < ?\n" +
				/*
				 * "   and a.trade_date > (select nvl(max(a.trade_date), '2000-01-01 15:15:08') trade_date\n"
				 * + "                         from sim_stocktrade a\n" +
				 * "                        where nvl(a.dr, 0) = 0\n" +
				 * "                          and a.trade_date < ?\n" +
				 * "                          and a.transtypecode = 'HV3A-0xx-05'\n"
				 * + "                          and a.state > 1\n" +
				 * "                          and a.pk_org = ?\n" +
				 * "                          and a.pk_assetsprop = ?\n" +
				 * "                          and a.pk_capaccount = ?\n" +
				 * "                          and a.pk_stocksort = ?\n" +
				 * "                          and a.pk_securities = ?)\n" +
				 */
				"   and a.pk_securities = ?\n"
				+ "   and a.hc_pk_assetsprop = ?\n"
				+ "   and a.hc_pk_capaccount = ?\n" + "   and a.pk_org = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(repVo.getTrade_date());

		/*
		 * para.addParam(repVo.getTrade_date());
		 * para.addParam(repVo.getPk_org());
		 * para.addParam(repVo.getPk_assetsprop());
		 * para.addParam(repVo.getPk_capaccount());
		 * para.addParam(repVo.getPk_stocksort());
		 * para.addParam(repVo.getPk_securities());
		 */

		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total = (String) new BaseDAO().executeQuery(sql, para,
					new ColumnProcessor());
			total_wfsy = new UFDouble(total);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		String sql_zr = "select to_char(nvl(sum(case\n"
				+ "                         when a.vdef2 is null then\n"
				+ "                          0\n"
				+ "                         when a.vdef2 = '~' then\n"
				+ "                          0\n"
				+ "                         else\n"
				+ "                          to_number(a.vdef2)\n"
				+ "                       end),\n"
				+ "                   0)) total_zr\n"
				+ "  from sim_transformtrade a\n"
				+ " inner join sec_securities b\n"
				+ "    on a.pk_securities = b.pk_securities\n"
				+ " inner join sec_billtype c\n"
				+ "    on a.transtypecode = c.pk_billtypecode\n"
				+ "   and nvl(c.dr, 0) = 0\n" + " where a.state > 1\n"
				+ "   and nvl(a.dr, 0) = 0\n"
				+ "   and a.transtypecode = 'HV3F-0xx-01'\n"
				+ "   and a.trade_date < ?\n" +
				/*
				 * "   and a.trade_date > (select nvl(max(a.trade_date), '2000-01-01 15:15:08') trade_date\n"
				 * + "                         from sim_stocktrade a\n" +
				 * "                        where nvl(a.dr, 0) = 0\n" +
				 * "                          and a.trade_date < ?\n" +
				 * "                          and a.transtypecode = 'HV3A-0xx-05'\n"
				 * + "                          and a.state > 1\n" +
				 * "                          and a.pk_org = ?\n" +
				 * "                          and a.pk_assetsprop = ?\n" +
				 * "                          and a.pk_capaccount = ?\n" +
				 * "                          and a.pk_stocksort = ?\n" +
				 * "                          and a.pk_securities = ?)\n" +
				 */
				"   and a.pk_securities2 = ?\n"
				+ "   and a.hr_pk_assetsprop = ?\n"
				+ "   and a.hr_pk_capaccount = ?\n" + "   and a.pk_org = ?";
		para.clearParams();
		para.addParam(repVo.getTrade_date());

		/*
		 * para.addParam(repVo.getTrade_date());
		 * para.addParam(repVo.getPk_org());
		 * para.addParam(repVo.getPk_assetsprop());
		 * para.addParam(repVo.getPk_capaccount());
		 * para.addParam(repVo.getPk_stocksort());
		 * para.addParam(repVo.getPk_securities());
		 */

		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total_zr = (String) new BaseDAO().executeQuery(sql_zr, para,
					new ColumnProcessor());
			total_wfsy = new UFDouble(total_zr).sub(total_wfsy);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return total_wfsy;
	}

	/**
	 * 计算 份额分红交易类型总的计提未付收益
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumVDEF3(TransformtradeVO repVo) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		// 份额分红
		String sql = "select to_char(nvl(sum(case\n"
				+ "                         when a.vdef3 is null then\n"
				+ "                          0\n"
				+ "                         when a.vdef3 = '~' then\n"
				+ "                          0\n"
				+ "                         else\n"
				+ "                          to_number(a.vdef3)\n"
				+ "                       end),\n"
				+ "                   0)) total_zc\n"
				+ "  from sim_stocktrade a\n"
				+ " inner join sec_securities b\n"
				+ "    on a.pk_securities = b.pk_securities\n"
				+ " inner join sec_busitype c\n"
				+ "    on a.pk_busitype = c.pk_busitype\n"
				+ "   and nvl(c.dr, 0) = 0\n"
				+ " where a.state > 1\n"
				+ "   and nvl(a.dr, 0) = 0\n"
				+ "   and c.code in ('0184','104','205')\n"
				+
				// "   and a.transtypecode = 'HV3A-0xx-05'\n" +
				"   and a.trade_date < ?\n" + "   and a.pk_securities = ?\n"
				+ "   and a.pk_assetsprop = ?\n"
				+ "   and a.pk_capaccount = ?\n" + "   and a.pk_org = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(repVo.getTrade_date());

		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total = (String) new BaseDAO().executeQuery(sql, para,
					new ColumnProcessor());
			total_wfsy = new UFDouble(total);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		// 赎回
		String sql_sh = "select to_char(nvl(sum(case\n"
				+ "                         when a.vdef3 is null then\n"
				+ "                          0\n"
				+ "                         when a.vdef3 = '~' then\n"
				+ "                          0\n"
				+ "                         else\n"
				+ "                          to_number(a.vdef3)\n"
				+ "                       end),\n"
				+ "                   0)) total_zc\n"
				+ "  from sim_stocktrade a\n"
				+ " inner join sec_securities b\n"
				+ "    on a.pk_securities = b.pk_securities\n"
				+ " inner join sec_busitype c\n"
				+ "    on a.pk_busitype = c.pk_busitype\n"
				+ "   and nvl(c.dr, 0) = 0\n" + " where a.state > 1\n"
				+ "   and nvl(a.dr, 0) = 0\n"
				+ "   and a.transtypecode = 'HV3A-0xx-02'\n"
				+ "   and c.code in ('202','103','0182','0122')\n"
				+ "   and a.trade_date < ?\n" + "   and a.pk_securities = ?\n"
				+ "   and a.pk_assetsprop = ?\n"
				+ "   and a.pk_capaccount = ?\n" + "   and a.pk_org = ?";
		para.clearParams();
		para.addParam(repVo.getTrade_date());

		para.addParam(repVo.getPk_securities());
		para.addParam(repVo.getPk_assetsprop());
		para.addParam(repVo.getPk_capaccount());
		para.addParam(repVo.getPk_org());
		try {
			String total = (String) new BaseDAO().executeQuery(sql_sh, para,
					new ColumnProcessor());
			total_wfsy = total_wfsy.add(new UFDouble(total));
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return total_wfsy;
	}
}
