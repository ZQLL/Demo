package nc.itf.erm.hxzq_fpxx;

import java.util.Map;

import nc.impl.fba_zqjd.trade.report.StockChangeVO;

public interface Izqjd_stockchange {
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ�֤ȯ���׼�¼֤ȯ����������
	 */
	public Map SearchQCZQJY(StockChangeVO scvo,String begin_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ��ʲ�ת��ծ,��ծת�ʲ���֤ȯת����¼
	 */
	
	public Map SearchQCZQZH(StockChangeVO scvo,String begin_date);
	
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO ��ĩ֤ȯ���׼�¼֤ȯ����������
	 */
	public Map SearchQMZQJY(StockChangeVO scvo,String end_date);

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO ��ĩ�ʲ�ת��ծ,��ծת�ʲ���֤ȯת����¼
	 */
	
	public Map SearchQMZQZH(StockChangeVO scvo,String end_date);


}
