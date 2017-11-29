package nc.itf.fba_zqjd.trade.report;

import java.util.*;
import nc.vo.fba_zqjd.trade.report.NewStockChangeVO;
import nc.vo.pub.lang.UFDouble;

public interface Izqjd_stockchange {
	/*
	 * @author zq
	 * 
	 * @param StockChangeVO �ڳ�֤ȯ���׼�¼֤ȯ����������
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQCZQJY(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO �ڳ��ʲ�ת��ծ,��ծת�ʲ���֤ȯת����¼
	 */

	@SuppressWarnings("rawtypes")
	public Map SearchQCZQZH(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ���ծת�й�,�й�ת��ծ��֤ȯת����¼
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQCZQZHTG(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO ��ĩ֤ȯ���׼�¼֤ȯ����������
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQMZQJY(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO ��ĩ�ʲ�ת��ծ,��ծת�ʲ���֤ȯת����¼
	 */

	@SuppressWarnings("rawtypes")
	public Map SearchQMZQZH(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ���ծת�й�,�й�ת��ծ��֤ȯת����¼
	 */
	@SuppressWarnings("rawtypes")
	public Map SearchQMZQZHTG(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO �ڳ�֤ȯ���׼�¼֤ȯʵ������
	 */
	public UFDouble SearchQCActual_num(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO ��ĩ֤ȯ���׼�¼֤ȯʵ������
	 */
	public UFDouble SearchQMActual_num(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO �ڳ���ծ��ֵ����
	 * ��ծ����*��������trade_dateΪ��ʼ���ڵ��������sim_trademarket�����̼�close_price
	 */
	public UFDouble SearchQCclose_price(NewStockChangeVO scvo, String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO ��ĩ��ծ��ֵ����
	 * ��ծ����*��������trade_dateΪ��ʼ���ڵ��������sim_trademarket�����̼�close_price
	 */
	public UFDouble SearchQMclose_price(NewStockChangeVO scvo, String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date ���ڼ�����Ϣ����
	 */
	public UFDouble SearchBQJT(NewStockChangeVO scvo, String begin_date,
			String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date ��������Ӧ����Ϣ
	 */
	public UFDouble SearchBQinterest(NewStockChangeVO scvo, String begin_date,
			String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date ���������תӦ����Ϣ
	 */
	public UFDouble SearchBQJZ(NewStockChangeVO scvo, String begin_date,
			String end_date);
}
