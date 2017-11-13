package nc.impl.sim.iufo;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.itf.sim.iufo.IufoQuerySIMVO;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.trademarket.TradeMarketVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 证券市值函数
 * @author hupl
 */
public class SIM_ZQSZImpl extends SIMIufoResultServiceImpl {
	/**
	 * 
	 */
	public Object getFuncResult(IufoQuerySIMVO simvo,boolean isqc)throws Exception {
		try {
			
			SIM_Analysis an = new SIM_Analysis();
			String condition = an.bulidCondition(simvo, true);
			String distillDate = null;
			if(isqc){//期初
				distillDate = getQcDate(simvo);
			}else{//期末
				distillDate = getQmDate(simvo);
			}
			CostPlanVO costplan = queryCostPlanInfo(simvo.getPk_group(),simvo.getPk_org(), simvo.getPk_glBookCode());
			//查询库存
			SimStockBanlance banlace = new SimStockBanlance();
			List<StockBalanceVO> zl = banlace.queryLastStock(costplan, distillDate, condition, simvo, isqc);
			UFDouble result = new UFDouble(0.0);
			if(zl != null && zl.size() > 0){
				for(StockBalanceVO z : zl){
					UFDouble stocksum = z.getStocks_sum() == null ? new UFDouble(0.0) : z.getStocks_sum();
					TradeMarketVO vo = queryLastMarket(z.getPk_securities(),distillDate,isqc);
					UFDouble price = vo.getClose_price() == null ? new UFDouble(0.0) : vo.getClose_price();
					result = result.add(stocksum.multiply(price).setScale(2, UFDouble.ROUND_HALF_UP));
				}
			}
			return result;
		} catch (Exception e) {
			Logger.debug("查询出错！");
			return null;
		}
	}
	
	public TradeMarketVO queryLastMarket(String pk_securities,String trade_date,boolean isqc)throws BusinessException{
		TradeMarketVO marketvo = null;
		if(pk_securities == null || trade_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select * from sim_trademarket where pk_securities = '"+pk_securities+"' ");
		sf.append(" and trade_date =  ");
		sf.append(" (select isnull(max(trade_date),'"+CostConstant.DEFAULT_DATE+"')  from sim_trademarket where ");
		if(isqc){
			sf.append(" isnull(dr, 0) = 0 and trade_date < '"+trade_date+"' ");//期初
		}else{
			sf.append(" isnull(dr, 0) = 0 and trade_date <= '"+trade_date+"' ");//期末
		}
		sf.append(" and pk_securities = '"+pk_securities+"') ");
		sf.append(" and isnull(dr, 0) = 0 ");
		List<TradeMarketVO> list = (List<TradeMarketVO>)new BaseDAO().executeQuery(sf.toString(), new BeanListProcessor(TradeMarketVO.class));
		if(list != null && list.size() > 0 ){
			marketvo = list.get(0);
		}
		return marketvo;
	}
}
