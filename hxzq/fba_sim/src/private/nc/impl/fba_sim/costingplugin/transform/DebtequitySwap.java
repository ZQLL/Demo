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
 * ծת�ɣ�ծȯ�ĳɱ�����Ϣת������Ʊ���м�ת�룬��۽�����
 */
public class DebtequitySwap extends AbstractTransform {

	@Override
	protected void calculate(ICostingTool costingtool, TransformtradeVO tradevo)
			throws Exception {

		PubMethod pm = PubMethod.getInstance();
		boolean isFirstPrice = costingtool.getCostParaVO().getFirstPrice();
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool balanceTool = costingtool.getBalancetool();
		// ȥ�����һ��pk_securities
		String[] keys = new String[costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray().length - 1];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = costingtool.getCostParaVO().getCostplanvo()
					.getCostFieldArray()[i];
		}
		String hckey = getCombinesKey(tradevo, keys, false);
		/**
		 * �˴�ƴ��key�������ҵ����
		 */
		hckey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ hckey;
		hckey = hckey + tradevo.getPk_securities();
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(hckey);
		queryvo.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getHc_pk_stocksort());
		queryvo.setTrade_date(trade_date);
		// �������
		StockBalanceVO stockbalancevo = balanceTool.getStockbalanceVO(queryvo,
				costingtool);
		if (stockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}
		// �����תӦ����Ϣ
		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				stockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// �����ת���ʼ�ֵ
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				stockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		//
		// ˰��
		UFDouble taxOutCost = costingtool.getTaxOutcost(queryvo, costingtool,
				tradevo.getBargain_num());
		// �������
		UFDouble stocks_num;
		// �����
		UFDouble stocks_sum = stockbalancevo.getStocks_sum();

		// ���ʣ������
		stocks_num = pm.sub(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		// �ж�ת���Ƿ����
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// ����
			// ���ʣ������Ϊ0 ����ȫ������ ת�����Ϊ�����
			stocks_sum = stockbalancevo.getStocks_sum();
		} else if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			if (costingtool.isCheckStock()) {
				costingtool
						.handleException(tradevo, null, SystemConst.error[0]);
			}
		} else {// ���۳ɱ�
				// ���ת������ ����ת�����
			if (isFirstPrice) {// �ȼ��㵥��
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
		 * ծת�ɼ�¼���ʱת�������ֵ�����д�����д��ת�����=�����/�������*ת��������ԭ�߼�û�м��ж϶�ֱ�ӻ�д
		 * 
		 * @author cjh
		 * @date 2015-12-07 09��55
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
		// tradevo.setBargain_sum2(stocks_sum); // ת��ɱ�=ת���ɱ�+��Ϣ---����ʵ����Ϣ�ֶ�
		/**
		 * ת�����������ֶ�ԭת��������bargain_num2_his����¼ԭ����ת������������ʱ������ԭ
		 * 
		 * @author cjh
		 * @date 2015-12-08
		 */
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {
			tradevo.setBargain_num2(tradevo.getBargain_num());// ���ת������Ϊ�գ���ôת����������ת������,���ת������Ϊ0����Ϊת�Ƴɱ�
		}
		if (tradevo.getBargain_sum2() == null
				|| tradevo.getBargain_sum2().compareTo(new UFDouble(0)) == 0) {
			// ����������Ϊծת�ɼ�¼ʱ��ת����= ת�����-ת����� add by lihaibo start
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
			// 2014-05-20 ����˵ �㷨Ӧ��Ϊ��D.
			// ת������ת����Ĭ��Ϊ�գ���ôת����=ת�����+��תӦ����Ϣ�����ǵ�ǰ����˺�û�н���תӦ����Ϣ����ת�����С�
			// update by lihaibo end
			tradevo.setAttributeValue("bargain_sum2_his", null);// ==================================cjh
		} else {
			tradevo.setAttributeValue("bargain_sum2_his",
					tradevo.getBargain_sum2());// ==================================cjh
		}
		// �Ѽ���õ����ݱ��浽VO��
		stockbalancevo.setStocks_num(stocks_num);
		stockbalancevo.setStocks_sum(pm.sub(stockbalancevo.getStocks_sum(),
				stocks_sum));
		stockbalancevo.setStocks_tax(pm.sub(stockbalancevo.getStocks_tax(),
				taxOutCost));
		// ���¿��
		balanceTool.updateStockbalanceVO(queryvo, stockbalancevo);
		// ���ת��Ŀ��
		String hrkey = getCombinesKey(tradevo, keys, true);
		/**
		 * �˴�ƴ��key�������ҵ����
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
		// ���ת�������
		stocks_num = pm.add(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// ���ת��Ľ��
		stocks_sum = pm.add(stockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());// ��Ҫ������Ϣ
		// ����ת����Ϣ

		stockbalancevo.setStocks_num(stocks_num);
		// ת��ĳɱ� �� ת���ĳɱ� + ��ת����Ϣ
		stockbalancevo.setStocks_sum(pm.add(stocks_sum, tradevo.getInterest())); // ������Ϣ
		stockbalancevo.setStocks_tax(pm.add(stockbalancevo.getStocks_tax(),
				taxOutCost));
		// ����ת��Ŀ��
		balanceTool.updateStockbalanceVO(queryvo, stockbalancevo);
		// ������Ϣ���
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// ��ȡӦ����Ϣ---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// ���湫�ʼ�ֵ---
		costcalc.saveFairValueDistill(costingtool, tradevo);
	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			TransformtradeVO tradevo) throws Exception {
		// ��ս�תӦ����Ϣ
		tradevo.setInterest(null);
		// ��ս�ת���ʼ�ֵ
		tradevo.setFairvalue(null);
		// ���ת��Ӧ����Ϣ
		costcalc.clearDistillInterest(costingtool);
		// ��չ��ʼ�ֵ����
		costcalc.clearDistillFv(costingtool);
		// ���ת����ת˰��
		tradevo.setTaxexpense(null);
		/**
		 * ֤ȯת����¼�����ʱ��ת����ת���ת��������ԭ
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
