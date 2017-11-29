package nc.itf.fba_zqjd.trade.report;

import java.util.*;
import nc.vo.fba_zqjd.trade.report.NewStockChangeVO;
import nc.vo.pub.lang.UFDouble;

public interface Izqjd_stockchange {
	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期初证券交易记录证券的买入卖出
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQCZQJY(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期初资产转负债,负债转资产的证券转换记录
	 */

	@SuppressWarnings("rawtypes")
	public Map SearchQCZQZH(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 期初负债转托管,托管转负债的证券转换记录
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQCZQZHTG(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期末证券交易记录证券的买入卖出
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQMZQJY(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期末资产转负债,负债转资产的证券转换记录
	 */

	@SuppressWarnings("rawtypes")
	public Map SearchQMZQZH(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 期初负债转托管,托管转负债的证券转换记录
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQMZQZHTG(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期初证券交易记录证券实际数量
	 */
	public UFDouble SearchQCActual_num(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期末证券交易记录证券实际数量
	 */
	public UFDouble SearchQMActual_num(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期初负债市值计算
	 * 负债数量*交易日期trade_date为开始日期当天的行情sim_trademarket的收盘价close_price
	 */
	public UFDouble SearchQCclose_price(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO 期末负债市值计算
	 * 负债数量*交易日期trade_date为开始日期当天的行情sim_trademarket的收盘价close_price
	 */
	public UFDouble SearchQMclose_price(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date 本期计提利息计算
	 */
	public UFDouble SearchBQJT(NewStockChangeVO scvo, String begin_date,
			String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date 本期卖出应收利息
	 */
	public UFDouble SearchBQinterest(NewStockChangeVO scvo, String begin_date,
			String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date 本期买入结转应收利息
	 */
	public UFDouble SearchBQJZ(NewStockChangeVO scvo, String begin_date,
			String end_date);
}
