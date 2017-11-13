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
 * ֤ȯת�� ��һ��ͬ���룬һ��ͬ�����������ɱ�����Ϣ���ʽ𲻱�
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
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}

		// zpm ʵ������ ���ӿ�ʼ��λ�ò�Ҫ��
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}

		// �����תӦ����Ϣ
		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// �����ת���ʼ�ֵ
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		//
		// �������
		UFDouble stocks_num = outstockbalancevo.getStocks_num();
		// �����
		UFDouble stocks_sum = outstockbalancevo.getStocks_sum();
		/**
		 * ת�����������ֶ�ԭת��������bargain_num2_his����¼ԭ����ת������������ʱ������ԭ
		 * 
		 * @author cjh
		 * @date 2015-12-08
		 */
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

			tradevo.setBargain_num2(tradevo.getBargain_num());// ���ת������Ϊ�գ���ôת����������ת������
		}

		// ���ʣ������
		stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		/******����ת��ռ�ȼ���ת��֤ȯ�ļ���δ������ add by lihbj start*******/
		if (hasSameTradeDate(tradevo)) {
			tradevo.setVdef2(UFDouble.ZERO_DBL.toString());
		} else {
			Double bl = tradevo.getBargain_num().div(outstockbalancevo.getStocks_num()).toDouble();
			UFDouble sum_jzwfsy = sumJZWFSY(tradevo).multiply(bl).setScale(2, UFDouble.ROUND_HALF_UP);
			tradevo.setVdef2(sum_jzwfsy.toString());
		}
		/******����ת��ռ�ȼ���ת��֤ȯ�ļ���δ������ add by lihbj end*******/
		// �ж�ת���Ƿ����
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// ����
			// ���ʣ������Ϊ0 ����ȫ������ ת�����Ϊ�����
			stocks_sum = outstockbalancevo.getStocks_sum();
		} else {// ���۳ɱ�
			// ���ת������ ����ת�����
			if (isFirstPrice) {// �ȼ��㵥��
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
		 * ֤ȯת����¼���ʱת�������ֵ�����д�����д��ת�����=�����/�������*ת��������ԭ�߼�û�м��ж϶�ֱ�ӻ�д
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

		/**
		 * ֤ȯ���׼�¼���ʱת������ֵ�����д�����д,ԭ�߼�û�м��ж϶�ֱ�ӻ�д
		 * 
		 * @author cjh
		 * @date 2015-12-07 09��55
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
		// ����ת�����
		balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);
		// ���ת��Ŀ��
		String hrkey = getCombinesKey(tradevo, keys, true);
		/**
		 * �˴�ƴ��key�������ҵ����
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
		// ���ת�������
		stocks_num = pm.add(instockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// ���ת��Ľ��
		stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());
		// ����ת����Ϣ
		instockbalancevo.setStocks_num(stocks_num);
		instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));
		instockbalancevo.setStocks_tax(pm.add(instockbalancevo.getStocks_tax(),
				taxOutCost));
		// ����ת��Ŀ��
		balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
		// ������Ϣ���
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// ��ȡӦ����Ϣ---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// ���湫�ʼ�ֵ---
		costcalc.saveFairValueDistill(costingtool, tradevo);
		// ʵ������
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
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return;
		}

		// zpm ʵ������ ���ӿ�ʼ��λ�ò�Ҫ��
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}

		// �����תӦ����Ϣ
		UFDouble lx = costcalc.forwardInterestDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setInterest(lx);
		// �����ת���ʼ�ֵ
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,
				outstockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		//
		// �������
		UFDouble stocks_num = outstockbalancevo.getStocks_num();
		// �����
		UFDouble stocks_sum = outstockbalancevo.getStocks_sum();
		/**
		 * ת�����������ֶ�ԭת��������bargain_num2_his����¼ԭ����ת������������ʱ������ԭ
		 * 
		 * @author cjh
		 * @date 2015-12-08
		 */
		tradevo.setAttributeValue("bargain_num2_his", tradevo.getBargain_num2());
		if (tradevo.getBargain_num2() == null
				|| tradevo.getBargain_num2().compareTo(new UFDouble(0)) == 0) {

			tradevo.setBargain_num2(tradevo.getBargain_num());// ���ת������Ϊ�գ���ôת����������ת������
		}

		// ���ʣ������
		stocks_num = pm.sub(outstockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		/****** ����ת��ռ�ȼ���ת��֤ȯ�ļ���δ������ add by lihbj start *******/
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
		//�������ڵ��յ�ת������������֮��count��allstock��λ
		/****** ����ת��ռ�ȼ���ת��֤ȯ�ļ���δ������ add by lihbj end *******/
		// �ж�ת���Ƿ����
		if (stocks_num.compareTo(new UFDouble(0)) < 0) {
			costingtool.handleException(tradevo, null, SystemConst.error[1]);
		}
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// ����
			// ���ʣ������Ϊ0 ����ȫ������ ת�����Ϊ�����
			stocks_sum = outstockbalancevo.getStocks_sum();
		} else {// ���۳ɱ�
			// ���ת������ ����ת�����
			if (isFirstPrice) {// �ȼ��㵥��
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
		 * ֤ȯת����¼���ʱת�������ֵ�����д�����д��ת�����=�����/�������*ת��������ԭ�߼�û�м��ж϶�ֱ�ӻ�д
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

		/**
		 * ֤ȯ���׼�¼���ʱת������ֵ�����д�����д,ԭ�߼�û�м��ж϶�ֱ�ӻ�д
		 * 
		 * @author cjh
		 * @date 2015-12-07 09��55
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
		// ����ת�����
		balanceTool.updateStockbalanceVO(queryvo, outstockbalancevo);
		// ���ת��Ŀ��
		String hrkey = getCombinesKey(tradevo, keys, true);
		/**
		 * �˴�ƴ��key�������ҵ����
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
		// ���ת�������
		stocks_num = pm.add(instockbalancevo.getStocks_num(),
				tradevo.getBargain_num2());
		// ���ת��Ľ��
		stocks_sum = pm.add(instockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum2());
		// ����ת����Ϣ
		instockbalancevo.setStocks_num(stocks_num);
		instockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));
		instockbalancevo.setStocks_tax(pm.add(instockbalancevo.getStocks_tax(),
				taxOutCost));
		// ����ת��Ŀ��
		balanceTool.updateStockbalanceVO(queryvo, instockbalancevo);
		// ������Ϣ���
		// BondBalanceBO.getInstance().handleBondBalanceByBillType(costingtool,
		// this.getBilltype(), tradevo, SystemConst.ActionType_Audit);

		// ��ȡӦ����Ϣ---
		costcalc.saveInterestDistill(costingtool, tradevo);
		// ���湫�ʼ�ֵ---
		costcalc.saveFairValueDistill(costingtool, tradevo);
		// ʵ������
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
		StockBalanceVO outstockbalancevo = balanceTool.getStockbalanceVO(
				queryvo, costingtool);
		
		if (outstockbalancevo == null) {
			costingtool.handleException(tradevo, null, SystemConst.error[0]);
			return new UFDouble(0);
		}

		// zpm ʵ������ ���ӿ�ʼ��λ�ò�Ҫ��
		if (outstockbalancevo.getStock_map().get(
				tradevo.getTrade_date().toLocalString()) == null) {
			outstockbalancevo.getStock_map().put(
					tradevo.getTrade_date().toLocalString(),
					outstockbalancevo.getStocks_num());
		}
		return  outstockbalancevo.getStocks_num();
	}

	/*
	 * �жϽ������ڵ������д��ڵ�ת������֤ȯ��¼
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return list;

	}

	
	
	/**
	 * Ĭ����� ҵ������������Ļ���Ҫ��д
	 */
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// �������ϵ�Ӧ����Ϣ��ת����Ϣ
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
						.getParent();// �޺���
				if (fathervo.getHc_pk_stocksort() == null) {
					fathervo.setHc_pk_stocksort(costingtool.getPk_stocksort()[0]);// Ӱ����1
				}
				if (fathervo.getHr_pk_stocksort() == null) {
					fathervo.setHr_pk_stocksort(costingtool.getPk_stocksort()[1]);// Ӱ����2
				}
				// vo.setPk_stocksort(vo.getHr_pk_stocksort());// ת����2
				// vo.setPk_assetsprop(vo.getHr_pk_assetsprop());// ת����2
				List<TransformtradeVO> alllist = selectAllTransformtradeVO(fathervo);//�ҵ����׵�������ת����¼
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
		// ���vdef2 add by lihbj
		tradevo.setVdef2(null);
		/**
		 * ֤ȯת����¼�����ʱ��ת����ת���ת��������ԭ
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

		// ʵ������
		RealratePlugin check = new RealratePlugin();
		if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
			ICalcPluginMaintain mail = (ICalcPluginMaintain) NCLocator
					.getInstance().lookup(ICalcPluginMaintain.class);
			mail.unZtgPlugindeal(tradevo, pk_group, pk_org, tradedate);
		}
	}

	/**
	 * �ж���ת�����ڵ�ͬʱ�Ƿ����ݶ�ֺ� ���� vdef2 ֱ�Ӹ�ֵ0������ԭ���߼�ȡ��
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
	 * ������һ�ηݶ�ֺ�֮�����м���δ���������
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
	 * ���� ת��ת��-ת��ת�� ת��ת�루ת��֤ȯ��Ӧ��VDEF2�� ת��ת����ת��֤ȯ��Ӧ��VDEF2��
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumJZWFSY_ZH(TransformtradeVO repVo) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		// ת��֤ȯ
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
	 * ���� �ݶ�ֺ콻�������ܵļ���δ������
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumVDEF3(TransformtradeVO repVo) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		// �ݶ�ֺ�
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
		// ���
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
