package nc.itf.erm.hxzq_fpxx;

import java.util.Map;

import nc.impl.fba_zqjd.trade.report.StockChangeVO;

public interface Izqjd_stockchange {
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 期初证券交易记录证券的买入卖出
	 */
	public Map SearchQCZQJY(StockChangeVO scvo,String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 期初资产转负债,负债转资产的证券转换记录
	 */
	
	public Map SearchQCZQZH(StockChangeVO scvo,String begin_date);
	
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 期末证券交易记录证券的买入卖出
	 */
	public Map SearchQMZQJY(StockChangeVO scvo,String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 期末资产转负债,负债转资产的证券转换记录
	 */
	
	public Map SearchQMZQZH(StockChangeVO scvo,String end_date);


}
