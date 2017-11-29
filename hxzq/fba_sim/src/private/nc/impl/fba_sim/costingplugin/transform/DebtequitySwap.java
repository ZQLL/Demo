package nc.impl.fba_sim.costingplugin.transform;

import nc.impl.fba_sim.costingplugin.AbstractTransform;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 债转股：债券的成本和利息转出，股票按市价转入，差价进收益
 */
public class DebtequitySwap extends AbstractTransform {

	@Override
	protected void calculate(ICostingTool costingtool, TransformtradeVO tradevo)
			throws Exception {

		PubMethod pm = PubMethod.getInstance();
		boolean isFirstPrice = costingtool.getCostParaVO().getFirstPrice();
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool balanceTool = costingtool.getBalancetool();
		// 去掉最后一项pk_securities
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
		StockBalanceVO stockbalancevo = balanceTool.getStockbalanceVO(queryvo,
				costingtool);
		if (stockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}
		// 计算结转应收利息
		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				stockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// 计算结转公允价值
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				stockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		//
		// 税金
		UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo, costingtool,
				tradevo.getBargain_num());
		// 库存数量
		UFDouble stocks_num;
		// 库存金额
		UFDouble stocks_sum = stockbalancevo.getStocks_sum();

		// 获得剩余数量
		stocks_num = pm.sub(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		// 判断转出是否合理
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// 卖空
			// 如果剩余数量为0 代表全部卖出 转出金额为库存金额
			stocks_sum = stockbalancevo.getStocks_sum();
		} else if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			if (costingtool.isCheckStock()) {
				costingtool
						.handleException(tradevo, null, SystemConst.error[0]);
			}
		} else {// 销售成本
				// 如果转出合理 计算转出金额
			if (isFirstPrice) {// 先计算单价
				stocks_sum = pm.div(stockbalancevo.getStocks_sum(),
						stockbalancevo.getStocks_num());
				stocks_sum = pm.multiply(stocks_sum, tradevo.getBargain_num());
			} else {
				stocks_sum = pm.multiply(stockbalancevo.getStocks_sum(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum, stockbalancevo.getStocks_num());
			}
			stocks_sum = pm.setScale(stocks_sum, true, true);
		}

		/**
		 * 债转股记录审核时转出金额有值则不需回写否则回写：转出金额=库存金额/库存数量*转出数量，原逻辑没有加判断都直接回写
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

		tradevo.setTaxexpense(taxOutCost);
		tradevo.setSellcost(stocks_sum);
		// tradevo.setBargain_sum2(stocks_sum); // 转入成本=转出成本+利息---暂无实际利息字段
		/**
		 * 转入数量增加字段原转入数量（bargain_num2_his）记录原来的转入数量，弃审时用来还原
		 * 
		 * @author cjh
		 * @date 2015-12-08
		 */
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {
			tradevo.setBargain_num2(tradevo.getBargain_num());// 如果转入数量为空，那么转入数量等于转出数量,如果转出数量为0，视为转移成本
		}
		if (tradevo.getBargain_sum2() == null
				|| tradevo.getBargain_sum2().compareTo(new UFDouble(0)) == 0) {
			// 若交易类型为债转股记录时，转入金额= 转出金额-转股领款 add by lihaibo start
			String typecode = tradevo.getAttributeValue("transtypecode")
					.toString();
			if ("HV3F-0xx-02".equals(typecode)) {
				UFDouble zcje = tradevo.getAttributeValue("bargain_sum") == null ? UFDouble.ZERO_DBL
						: new UFDouble(tradevo.getAttributeValue("bargain_sum")
								.toString());
				UFDouble zglk = tradevo.getAttributeValue("vdef1") == null ? UFDouble.ZERO_DBL
						: new UFDouble(tradevo.getAttributeValue("vdef1")
								.toString());
				if (zcje.sub(zglk).compareTo(UFDouble.ZERO_DBL) >= 0) {
					tradevo.setAttributeValue("bargain_sum2", zcje.sub(zglk));
				}
			}
			// tradevo.setBargain_sum2(pm.add(tradevo.getBargain_sum(),tradevo.getInterest()));
			// 2014-05-20 曲宁说 算法应该为：D.
			// 转入金额：如果转入金额默认为空，那么转入金额=转出金额+结转应收利息；但是当前的审核后没有将结转应收利息计入转入金额中。
			// update by lihaibo end
			tradevo.setAttributeValue("bargain_sum2_his", null);// ==================================cjh
		} else {
			tradevo.setAttributeValue("bargain_sum2_his",
					tradevo.getBargain_sum2());// ==================================cjh
		}
		// 把计算好的数据保存到VO中
		stockbalancevo.setStocks_num(stocks_num);
		stockbalancevo.setStocks_sum(pm.sub(stockbalancevo.getStocks_sum(),
				stocks_sum));
		stockbalancevo.setStocks_tax(pm.sub(stockbalancevo.getStocks_tax(),
				taxOutCost));
		// 更新库存
		balanceTool.updateStockbalanceVO(queryvo, stockbalancevo);
		// 获得转入的库存
		String hrkey = getCombinesKey(tradevo, keys, true);
		/**
		 * 此处拼接key必须加上业务组
		 */
		hrkey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hrkey;
		hrkey = hrkey + tradevo.getPk_securities2();
		queryvo.setKey(hrkey);
		stockbalancevo = balanceTool.getStockbalanceVO(queryvo, costingtool);
		if (stockbalancevo == null) {
			stockbalancevo = balanceTool.getStockbalanceVOByVO(tradevo,
					costingtool);
			stockbalancevo.setPk_securities(tradevo.getPk_securities2());
		}
		// 获得转入的数量
		stocks_num = pm.add(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// 获得转入的金额
		stocks_sum = pm.add(stockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());// 需要增加利息
		// 保存转入信息

		stockbalancevo.setStocks_num(stocks_num);
		// 转入的成本 ＝ 转出的成本 + 结转的利息
		stockbalancevo.setStocks_sum(pm.add(stocks_sum, tradevo.getInterest())); // 增加利息
		stockbalancevo.setStocks_tax(pm.add(stockbalancevo.getStocks_tax(),
				taxOutCost));
		// 更新转入的库存
		balanceTool.updateStockbalanceVO(queryvo, stockbalancevo);
		// 处理利息余额
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// 存取应收利息---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// 保存公允价值---
		costcalc.saveFairValueDistill(costingtool, tradevo);
	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			TransformtradeVO tradevo) throws Exception {
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
		/**
		 * 证券转换记录反审核时将转出金额、转入金额、转入数量还原
		 * 
		 * @author cjh
		 * @date 2015-12-08
		 */
		tradevo.setBargain_sum((UFDouble) tradevo
				.getAttributeValue("bargain_sum_his"));
		tradevo.setBargain_sum2((UFDouble) tradevo
				.getAttributeValue("bargain_sum2_his"));
		tradevo.setBargain_num2((UFDouble) tradevo
				.getAttributeValue("bargain_num2_his"));

	}
}
