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
 * ��ȯ���ף��ڳ������ڹ黹�ȣ�һ�������٣�һ���������
 * 
 * @author	liangwei
 * @date	2014-4-15 ����9:04:35
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
		// ����
		ICostBalanceTool  tool = costingtool.getBalancetool();
		//��ѯ��Ҫ�����֯���ȸ���ֵ
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
		//�����ת���ʼ�ֵ
		UFDouble fv = costcalc.forwardFairValueDistill(costingtool,stockbalancevo, tradevo);
		tradevo.setFairvalue(fv);
		// �������
		UFDouble stocks_num = stockbalancevo.getStocks_num();
		// �����
		UFDouble stocks_sum = stockbalancevo.getStocks_sum();
		// �����Ϣ
		UFDouble accrual_sum = null;
		//˰��
		UFDouble taxOutCost = null;
		//��������
		String transtype_code = tradevo.getTranstypecode();
		//������Ϣ
		UFDouble interest = stockbalancevo.getAccrual_sum();
		// ���ʣ������
		stocks_num = pm.sub(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		
		
		if (stocks_num.compareTo(new UFDouble(0)) == 0) {// ����
			stocks_sum = stockbalancevo.getStocks_sum();
			taxOutCost = stockbalancevo.getStocks_tax();
			if(null==tradevo.getBargain_sum()){
				tradevo.setBargain_sum(stocks_sum);
				stocks_sum = new UFDouble(0);//���������
			}
			
		} else if (stocks_num.compareTo(new UFDouble(0)) < 0) {
//			if (costingtool.isCheckStock()) {
				costingtool.handleException(tradevo, null,SystemConst.error[0]);
//			}
		} else {// �ɱ�
			if (isFirstPrice) {// �ȼ��㵥��
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
				//��δ����ɽ������ݿ��ɱ�����  
				if(null==tradevo.getBargain_sum()){
					UFDouble div = pm.div(tradevo.getBargain_num().multiply(10000), stockbalancevo.getStocks_num());
					UFDouble multi = pm.multiply(stockbalancevo.getStocks_sum(),div);
					multi = pm.setScale(multi.div(10000),true,true);
					tradevo.setBargain_sum(multi);
					//tradevo.setBargain_sum(pm.setScale(pm.multiply(stockbalancevo.getStocks_sum(), pm.div(tradevo.getBargain_num(), stockbalancevo.getStocks_num())), true, true));
					//���������и��� 
					stocks_sum=pm.sub(stockbalancevo.getStocks_sum(), tradevo.getBargain_sum());
					//����tradevo��дBargain_sum�ֶ�ֵ
				}else{
					stocks_sum = pm.sub(stockbalancevo.getStocks_sum(),
							tradevo.getBargain_sum());
				}
				
				accrual_sum = pm.multiply(accrual_sum, tradevo.getBargain_num());
				accrual_sum = pm.div(accrual_sum, stockbalancevo.getStocks_num());
				interest = pm.multiply(interest, tradevo.getBargain_num());
				interest = pm.div(interest, stockbalancevo.getStocks_num());
			}
			//˰�Ѽ���
			if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH1.getCode())){
				//�ɹ�ת���ڼ�¼�����㷽��ΪȨ��(���ڷ�)���㷨
				stocks_sum = pm.multiply(stockbalancevo.getStocks_sum(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum, stockbalancevo.getStocks_num());
//				taxOutCost = pm.multiply(stockbalancevo.getStocks_tax(),
//						tradevo.getBargain_num());
//				taxOutCost = pm.div(taxOutCost, stockbalancevo.getStocks_num());
				taxOutCost=new TaxTransfer().getTransferTax(tradevo, stockbalancevo);
			}else if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH2.getCode())){
				//����ת�ɹ���¼��ͬһ�����������Ȩ�ؼ���
				stocks_sum = pm.multiply(stockbalancevo.getNdef8(),
						tradevo.getBargain_num());
				stocks_sum = pm.div(stocks_sum, stockbalancevo.getNdef6());
				if(istransfeftax){
					taxOutCost = pm.multiply(stockbalancevo.getNdef7(),
							tradevo.getBargain_num());
					taxOutCost = pm.div(taxOutCost, stockbalancevo.getNdef6());
				}
			}else if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH3.getCode()) || transtype_code.equals(SystemConst.SMT_TransType.MarginTSH4.getCode())){
				//�ڳ���¼���ڳ��黹��¼��ֻת����������ת��˰��
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
			//����ת�ɹ�
			UFDouble vndef6 = pm.sub(stockbalancevo.getNdef6(), tradevo.getBargain_num());
			UFDouble vndef7 = pm.sub(stockbalancevo.getNdef7(), taxOutCost);
			UFDouble vndef8 = pm.sub(stockbalancevo.getNdef8(), tradevo.getBargain_sum());
			stockbalancevo.setNdef6(pm.setScale(vndef6, true, true));//���Զ�����6�洢ת��������
			stockbalancevo.setNdef7(pm.setScale(vndef7, true, true));//���Զ�����6�洢ת����˰��
			stockbalancevo.setNdef8(pm.setScale(vndef8, true, true));//���Զ�����6�洢ת���ļ۸�
		}
		stockbalancevo.setStocks_sum(stocks_sum);
		stockbalancevo.setStocks_num(stocks_num);
		stockbalancevo.setAccrual_sum(pm.sub(stockbalancevo.getAccrual_sum(),
				interest));
		tool.updateStockbalanceVO(sbqkvo, stockbalancevo);
		costingtool.addCostChangeBalanceVO(stockbalancevo,costFieldArray);
		
		// ����
		tradevo.setPk_stocksort(costingtool.getPk_stocksort()[1]);
		key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup()+VOUtil.getCombinesKey(tradevo,costFieldArray);
		sbqkvo.setKey(key);
		sbqkvo.setPk_stocksort(tradevo.getPk_stocksort());
		stockbalancevo = tool.getStockbalanceVO(sbqkvo, costingtool);
		if (stockbalancevo == null) {
			stockbalancevo = tool.getStockbalanceVOByVO(tradevo,costingtool);
		}
		
		
		if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH1.getCode())){
			//�ɹ�ת����
			UFDouble vndef6 = pm.add(stockbalancevo.getNdef6(), tradevo.getBargain_num());
			UFDouble vndef7 = pm.add(stockbalancevo.getNdef7(), taxOutCost);
			UFDouble vndef8 = pm.add(stockbalancevo.getNdef8(), tradevo.getBargain_sum());
			stockbalancevo.setNdef6(pm.setScale(vndef6, true, true));//���Զ�����6�洢ת��������
			stockbalancevo.setNdef7(pm.setScale(vndef7, true, true));//���Զ�����6�洢ת����˰��
			stockbalancevo.setNdef8(pm.setScale(vndef8, true, true));//���Զ�����6�洢ת���ļ۸�
			stockbalancevo.setPk_billtype(transtype_code);//�洢��Դ�ĵ�������
		}else if(transtype_code.equals(SystemConst.SMT_TransType.MarginTSH2.getCode())){
			//����ת�ɹ�
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
		//��տ����֯
		tradevo.setPk_stocksort(null);
		//���湫�ʼ�ֵ---
		MarginTradeVO tradevo1 = (MarginTradeVO)tradevo.clone();
		tradevo1.setPk_stocksort(costingtool.getPk_stocksort()[0]);
		tradevo1.setHr_pk_stocksort(costingtool.getPk_stocksort()[1]);
		costcalc.saveFairValueDistill(costingtool, tradevo1);
	}
	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			MarginTradeVO tradevo) throws Exception{
		//��ս�ת���ʼ�ֵ
		tradevo.setFairvalue(null);
		//��չ��ʼ�ֵ����
		costcalc.clearDistillFv(costingtool);
	}
	
}
