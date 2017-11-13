package nc.impl.fba_zqjd.trade.report;

import java.util.Map;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.fba_zqjd.trade.report.Izqjd_stockchange;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.vo.fba_zqjd.trade.report.NewStockChangeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.olap4j.impl.ArrayMap;

public class zqjd_stockchangeImpl implements Izqjd_stockchange {
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ�֤ȯ���׼�¼֤ȯ����������
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQCZQJY(NewStockChangeVO scvo, String begin_date) {
		UFDouble MRMC = new UFDouble(0);
		UFDouble MRMCJE = new UFDouble(0);
		Map map = new ArrayMap();
		UFDate begindate = new UFDate(begin_date);
		String pkcapa = scvo.getPk_capaccount();
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pksto = scvo.getPk_stocksort();
		Vector vecmc = new Vector();
		Vector vecmr = new Vector();
		BaseDAO dao = new BaseDAO();
		// ���Ҽ�������֮ǰ������֤ȯ���׼�¼�����ȯ�Ľ������� ��������� -zq
		String sql1 = "select sum(bargain_num),sum(bargain_sum) from sim_stocktrade where pk_securities ='"
				+ pksec
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ "0001SE00000000000004"
				+ "' and pk_capaccount ='"
				+ pkcapa
				+ "' and transtypecode = 'HV3A-0xx-22"
				+ "' and nvl(dr,0)=0 and trade_date < '" + begindate + "'";

		String sql2 = "select sum(bargain_num),sum(sellcost) from sim_stocktrade where pk_securities ='"
				+ pksec
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ "0001SE00000000000004"
				+ "' and pk_capaccount ='"
				+ pkcapa
				+ "' and transtypecode = 'HV3A-0xx-21"
				+ "' and nvl(dr,0)=0 and trade_date < '" + begindate + "'";

		try {
			vecmc = (Vector) dao.executeQuery(sql1, new VectorProcessor());

			vecmr = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vecmc.get(0)).get(0) != null) {
			MRMC = new UFDouble(((Vector) vecmc.get(0)).get(0).toString());
			MRMCJE = new UFDouble(((Vector) vecmc.get(0)).get(1).toString());
		}
		if (((Vector) vecmr.get(0)).get(0) != null) {
			MRMC = MRMC.sub(new UFDouble(((Vector) vecmr.get(0)).get(0)
					.toString()));
			MRMCJE = MRMCJE.sub(new UFDouble(((Vector) vecmr.get(0)).get(1)
					.toString()));
		}
		map.put("MRMC", MRMC);
		map.put("MRMCJE", MRMCJE);
		return map;

	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ��ʲ�ת��ծ,��ծת�ʲ���֤ȯת����¼
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQCZQZH(NewStockChangeVO scvo, String begin_date) {
		UFDouble ZRZC = new UFDouble(0);
		UFDouble ZRZCJE = new UFDouble(0);
		Map map = new ArrayMap();
		UFDate begindate = new UFDate(begin_date);
		String pkcapa = scvo.getPk_capaccount();
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pksto = scvo.getPk_stocksort();
		Vector veczc = new Vector();
		Vector veczr = new Vector();
		BaseDAO dao = new BaseDAO();
		// �ʲ�ת��ծ���������-ת����;ת��֤ȯ�����ǵ�ǰ���ͣ�ת�����ʲ����ԣ�ת��Ϊ��ծ����;
		String sql1 = "select sum(bargain_num2),sum(bargain_sum2) "
				+ "from sim_transformtrade "
				+ "where transtypecode = 'HV3F-0xx-01'"
				+ " and nvl(dr,0)=0"
				+ " and state >= 2"
				+ " and hr_pk_assetsprop = '0001SE00000000000004'"
				+ " and hr_pk_capaccount = '"
				+ pkcapa
				+ "' and hc_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and pk_securities2 ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <'" + begindate + "'";

		String sql2 = "select sum(bargain_num),sum(bargain_sum) "
				+ "from sim_transformtrade "
				+ "where transtypecode = 'HV3F-0xx-01'"
				+ " and nvl(dr,0)=0"
				+ " and state >= 2"
				+ " and hr_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and hc_pk_capaccount = '" + pkcapa
				+ "' and hc_pk_assetsprop = '0001SE00000000000004'"
				+ " and pk_securities ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <='" + begindate + "'";

		try {
			veczr = (Vector) dao.executeQuery(sql1, new VectorProcessor());

			veczc = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) veczc.get(0)).get(0) != null) {
			ZRZC = new UFDouble(((Vector) veczc.get(0)).get(0).toString());
			ZRZCJE = new UFDouble(((Vector) veczc.get(0)).get(1).toString());
		}
		if (((Vector) veczr.get(0)).get(0) != null) {
			ZRZC = ZRZC.sub(new UFDouble(((Vector) veczr.get(0)).get(0)
					.toString()));
			ZRZCJE = ZRZCJE.sub(new UFDouble(((Vector) veczr.get(0)).get(1)
					.toString()));
		}

		map.put("ZRZC", ZRZC);
		map.put("ZRZCJE", ZRZCJE);
		return map;

	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO ��ĩ֤ȯ���׼�¼֤ȯ����������
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQMZQJY(NewStockChangeVO scvo, String end_date) {
		UFDouble MRMC = new UFDouble(0);
		UFDouble MRMCJE = new UFDouble(0);
		Map map = new ArrayMap();
		UFDate enddate = new UFDate(end_date);
		String pkcapa = scvo.getPk_capaccount();
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pksto = scvo.getPk_stocksort();
		Vector vecmc = new Vector();
		Vector vecmr = new Vector();
		BaseDAO dao = new BaseDAO();
		// ���Ҽ�������֮ǰ������֤ȯ���׼�¼�����ȯ�Ľ������� ��������� -zq
		String sql1 = "select sum(bargain_num),sum(bargain_sum) from sim_stocktrade where pk_securities ='"
				+ pksec
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ "0001SE00000000000004"
				+ "' and pk_capaccount ='"
				+ pkcapa
				+ "' and transtypecode = 'HV3A-0xx-22"
				+ "' and nvl(dr,0)=0 and trade_date <= '"
				+ enddate.toString().substring(0, 10) + " 23:59:59" + "'";

		String sql2 = "select sum(bargain_num),sum(sellcost) from sim_stocktrade where pk_securities ='"
				+ pksec
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ "0001SE00000000000004"
				+ "' and pk_capaccount ='"
				+ pkcapa
				+ "' and transtypecode = 'HV3A-0xx-21"
				+ "' and nvl(dr,0)=0 and trade_date <= '"
				+ enddate.toString().substring(0, 10) + " 23:59:59" + "'";

		try {
			vecmc = (Vector) dao.executeQuery(sql1, new VectorProcessor());

			vecmr = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vecmc.get(0)).get(0) != null) {
			MRMC = new UFDouble(((Vector) vecmc.get(0)).get(0).toString());
			MRMCJE = new UFDouble(((Vector) vecmc.get(0)).get(1).toString());
		}
		if (((Vector) vecmr.get(0)).get(0) != null) {
			MRMC = MRMC.sub(new UFDouble(((Vector) vecmr.get(0)).get(0)
					.toString()));
			MRMCJE = MRMCJE.sub(new UFDouble(((Vector) vecmr.get(0)).get(1)
					.toString()));
		}
		map.put("MRMC", MRMC);
		map.put("MRMCJE", MRMCJE);
		return map;

	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO ��ĩ�ʲ�ת��ծ,��ծת�ʲ���֤ȯת����¼
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQMZQZH(NewStockChangeVO scvo, String end_date) {
		UFDouble ZRZC = new UFDouble(0);
		UFDouble ZRZCJE = new UFDouble(0);
		Map map = new ArrayMap();
		UFDate enddate = new UFDate(end_date);
		String pkcapa = scvo.getPk_capaccount();
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pksto = scvo.getPk_stocksort();
		Vector veczc = new Vector();
		Vector veczr = new Vector();
		BaseDAO dao = new BaseDAO();
		// �ʲ�ת��ծ���������-ת����;ת��֤ȯ�����ǵ�ǰ���ͣ�ת�����ʲ����ԣ�ת��Ϊ��ծ����;
		String sql1 = "select sum(bargain_num2),sum(bargain_sum2) "
				+ "from sim_transformtrade "
				+ "where transtypecode = 'HV3F-0xx-01'"
				+ " and nvl(dr,0)=0"
				+ " and state >= 2"
				+ " and hr_pk_assetsprop = '0001SE00000000000004'"
				+ " and hr_pk_capaccount = '"
				+ pkcapa
				+ "' and hc_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and pk_securities2 ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <='"
				+ enddate.toString().substring(0, 10) + " 23:59:59" + "'";

		String sql2 = "select sum(bargain_num),sum(bargain_sum) "
				+ "from sim_transformtrade "
				+ "where transtypecode = 'HV3F-0xx-01'"
				+ " and nvl(dr,0)=0"
				+ " and state >= 2"
				+ " and hr_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and hc_pk_capaccount = '" + pkcapa
				+ "' and hc_pk_assetsprop = '0001SE00000000000004'"
				+ " and pk_securities ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <='"
				+ enddate.toString().substring(0, 10) + " 23:59:59" + "'";

		try {
			veczr = (Vector) dao.executeQuery(sql1, new VectorProcessor());

			veczc = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) veczc.get(0)).get(0) != null) {
			ZRZC = new UFDouble(((Vector) veczc.get(0)).get(0).toString());
			ZRZCJE = new UFDouble(((Vector) veczc.get(0)).get(1).toString());
		}
		if (((Vector) veczr.get(0)).get(0) != null) {
			ZRZC = ZRZC.sub(new UFDouble(((Vector) veczr.get(0)).get(0)
					.toString()));
			ZRZCJE = ZRZCJE.sub(new UFDouble(((Vector) veczr.get(0)).get(1)
					.toString()));
		}

		map.put("ZRZC", ZRZC);
		map.put("ZRZCJE", ZRZCJE);
		return map;

	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ�ʵ�ʸ�ծ����
	 */
	@SuppressWarnings("rawtypes")
	public UFDouble SearchQCActual_num(NewStockChangeVO scvo, String begin_date) {
		UFDouble actualnum = new UFDouble(0);
		BaseDAO dao = new BaseDAO();
		Vector vec = new Vector();
		String pkcapa = scvo.getPk_capaccount();
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pksto = scvo.getPk_stocksort();
		String pkasset = "0001SE00000000000004";
		String sql = "select stocks_num from sim_stockbalance where pk_securities ='"
				+ pksec
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ pkasset
				+ "' and pk_capaccount ='"
				+ pkcapa
				+ "' and nvl(dr,0)=0 and trade_date < '"
				+ begin_date.toString()
				+ "' and rownum <= 1 order by trade_date desc";

		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (vec.size() > 0) {
			if (((Vector) vec.get(0)).get(0) != null)
				actualnum = new UFDouble(((Vector) vec.get(0)).get(0)
						.toString());
		}
		return actualnum;
	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO ��ĩʵ�ʸ�ծ����
	 */
	@SuppressWarnings("rawtypes")
	public UFDouble SearchQMActual_num(NewStockChangeVO scvo, String end_date) {
		UFDouble actualnum = new UFDouble(0);
		BaseDAO dao = new BaseDAO();
		Vector vec = new Vector();
		String pkcapa = scvo.getPk_capaccount();
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pksto = scvo.getPk_stocksort();
		String pkasset = "0001SE00000000000004";
		String sql = "select stocks_num from sim_stockbalance where pk_securities ='"
				+ pksec
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ pkasset
				+ "' and pk_capaccount ='"
				+ pkcapa
				+ "' and nvl(dr,0)=0 and trade_date <= '"
				+ end_date.toString().substring(0, 10)
				+ " 23:59:59"
				+ "' and rownum <= 1 order by trade_date desc";

		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec.get(0)).get(0) != null)
			actualnum = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return actualnum;
	}

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO �ڳ���ծ��ֵ����--�ҵ���ȯ��ʱ������̼�
	 * ��ծ����*��������trade_dateΪ��ʼ���ڵ��������sim_trademarket�����̼�close_price
	 */
	@SuppressWarnings("rawtypes")
	public UFDouble SearchQCclose_price(NewStockChangeVO scvo, String begin_date) {
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		BaseDAO dao = new BaseDAO();
		Vector vec = new Vector();
		UFDouble closeprice = new UFDouble(0);
		String sql = "select close_price from sim_trademarket where nvl(dr,0)=0"
				+ " and pk_securities ='"
				+ pksec
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and trade_date < '"
				+ begin_date.toString()
				+ "' and rownum <=1 order by trade_date desc";
		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec.get(0)).get(0) != null)
			closeprice = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return closeprice;
	}

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO ��ĩ��ծ��ֵ����--�ҵ���ȯ��ʱ������̼�
	 * ��ծ����*��������trade_dateΪ��ʼ���ڵ��������sim_trademarket�����̼�close_price
	 */
	@SuppressWarnings("rawtypes")
	public UFDouble SearchQMclose_price(NewStockChangeVO scvo, String end_date) {
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		BaseDAO dao = new BaseDAO();
		Vector vec = new Vector();
		UFDouble closeprice = new UFDouble(0);
		String sql = "select close_price from sim_trademarket where nvl(dr,0)=0"
				+ " and pk_securities ='"
				+ pksec
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and trade_date <= '"
				+ end_date.toString().substring(0, 10)
				+ " 23:59:59"
				+ "' and rownum <=1 order by trade_date desc";
		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec.get(0)).get(0) != null)
			closeprice = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return closeprice;
	}

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date ���ڼ�����Ϣ���� ��������HV3A-
	 * 0xx-21��ծ���뽻���еĽ�תӦ����Ϣinterest+��ת�룺��ծת�й�HV3F-Cxx-04+ҵ������Ϊ��ծת��0404�ģ�interest��
	 */
	@SuppressWarnings("rawtypes")
	public UFDouble SearchBQJT(NewStockChangeVO scvo, String begin_date,
			String end_date) {
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pkass = scvo.getPk_assetsprop();
		String pkcap = scvo.getPk_capaccount();
		String pksto = scvo.getPk_stocksort();
		BaseDAO dao = new BaseDAO();
		Vector vec1 = new Vector();
		Vector vec2 = new Vector();
		UFDouble BQJT = new UFDouble(0);
		String sql = "select sum(interestdistill) from sim_interestdistill where nvl(dr,0)=0"
				+ " and pk_securities ='"
				+ pksec
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ pkass
				+ "' and pk_capaccount ='"
				+ pkcap
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and distill_date <= '"
				+ end_date.toString().substring(0, 10)
				+ " 23:59:59"
				+ "' and distill_date >= '"
				+ end_date.toString()
				+ "' and genstate = 2";

		String sql1 = "select sum(interest) " + "from sim_transformtrade "
				+ "where nvl(dr,0)=0 " + "and billtypecode = '0404' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and pk_assetsprop ='" + pkass
				+ "' and pk_capaccount ='" + pkcap + "' and pk_stocksort ='"
				+ pksto + "' and trade_date <= '"
				+ end_date.toString().substring(0, 10) + " 23:59:59"
				+ "' and trade_date >= '" + end_date.toString() + "'";
		try {
			vec1 = (Vector) dao.executeQuery(sql, new VectorProcessor());
			vec2 = (Vector) dao.executeQuery(sql1, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec1.get(0)).get(0) != null)
			BQJT = new UFDouble(((Vector) vec1.get(0)).get(0).toString());
		if (((Vector) vec2.get(0)).get(0) != null)
			BQJT = BQJT.add(new UFDouble(((Vector) vec2.get(0)).get(0)
					.toString()));
		return BQJT;
	}

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date ��������Ӧ����Ϣ
	 */
	@SuppressWarnings("rawtypes")
	public UFDouble SearchBQinterest(NewStockChangeVO scvo, String begin_date,
			String end_date) {
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pkass = scvo.getPk_assetsprop();
		String pkcap = scvo.getPk_capaccount();
		String pksto = scvo.getPk_stocksort();
		BaseDAO dao = new BaseDAO();
		Vector vec = new Vector();
		Vector vec1 = new Vector();
		Vector vec2 = new Vector();
		UFDouble bqinterest = new UFDouble(0);
		String sql = "select sum(accrual_sum) from sim_stocktrade where nvl(dr,0)=0"
				+ " and pk_securities ='"
				+ pksec
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ pkass
				+ "' and pk_capaccount ='"
				+ pkcap
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and trade_date <= '"
				+ end_date.toString().substring(0, 10)
				+ " 23:59:59"
				+ "' and trade_date >= '"
				+ end_date.toString()
				+ "' and transtypecode ='HV3A-0xx-22'";

		String sql1 = "select sum(interest) " + "from sim_transformtrade "
				+ "where nvl(dr,0)=0 " + "and billtypecode = '0404' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and pk_assetsprop ='" + pkass
				+ "' and pk_capaccount ='" + pkcap + "' and pk_stocksort ='"
				+ pksto + "' and trade_date <= '"
				+ end_date.toString().substring(0, 10) + " 23:59:59"
				+ "' and trade_date >= '" + end_date.toString() + "'";

		String sql2 = "select sum(vdef10) " + "from sim_transformtrade "
				+ "where nvl(dr,0)=0 " + "and billtypecode = '0401' "
				+ "and transtypecode ='HV3F-0xx-01'" + " and pk_securities ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and pk_assetsprop ='" + pkass
				+ "' and pk_capaccount ='" + pkcap + "' and pk_stocksort ='"
				+ pksto + "' and trade_date <= '"
				+ end_date.toString().substring(0, 10) + " 23:59:59"
				+ "' and trade_date >= '" + end_date.toString() + "'";
		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
			vec1 = (Vector) dao.executeQuery(sql1, new VectorProcessor());
			vec2 = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec.get(0)).get(0) != null)
			bqinterest = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		if (((Vector) vec1.get(0)).get(0) != null)
			bqinterest = bqinterest.add(new UFDouble(((Vector) vec1.get(0))
					.get(0).toString()));
		if (((Vector) vec2.get(0)).get(0) != null)
			bqinterest = bqinterest.add(new UFDouble(((Vector) vec2.get(0))
					.get(0).toString()));
		return bqinterest;
	}

	/*
	 * @author zq
	 * 
	 * @param StockChangeVO, begin_date, end_date ���������תӦ����Ϣ
	 */
	@SuppressWarnings("rawtypes")
	public UFDouble SearchBQJZ(NewStockChangeVO scvo, String begin_date,
			String end_date) {
		String pksec = scvo.getPk_securities();
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		String pkass = scvo.getPk_assetsprop();
		String pkcap = scvo.getPk_capaccount();
		String pksto = scvo.getPk_stocksort();
		BaseDAO dao = new BaseDAO();
		Vector vec = new Vector();
		UFDouble bqjz = new UFDouble(0);
		String sql = "select sum(interest) from sim_stocktrade where nvl(dr,0)=0"
				+ " and pk_securities ='"
				+ pksec
				+ "' and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and pk_assetsprop ='"
				+ pkass
				+ "' and pk_capaccount ='"
				+ pkcap
				+ "' and pk_stocksort ='"
				+ pksto
				+ "' and trade_date <= '"
				+ end_date.toString().substring(0, 10)
				+ " 23:59:59"
				+ "' and trade_date >= '"
				+ end_date.toString()
				+ "' and transtypecode ='HV3A-0xx-21'";
		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec.get(0)).get(0) != null)
			bqjz = new UFDouble(((Vector) vec.get(0)).get(0).toString());
		return bqjz;
	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ڳ���ծת�й�,�й�ת��ծ��֤ȯת����¼
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQCZQZHTG(NewStockChangeVO stvo, String begin_date) {
		UFDouble ZRZC = new UFDouble(0);
		UFDouble ZRZCJE = new UFDouble(0);
		Map map = new ArrayMap();
		String pkass = stvo.getPk_assetsprop();
		String pkcapa = stvo.getPk_capaccount();
		String pksec = stvo.getPk_securities();
		String pkorg = stvo.getPk_org();
		String pkgro = stvo.getPk_group();
		String pksto = stvo.getPk_stocksort();
		Vector vec1 = new Vector();
		Vector vec2 = new Vector();
		BaseDAO dao = new BaseDAO();
		// ��ծת�йܣ��������+ת����;
		String sql = "select sum(bargain_num2),sum(bargain_sum2) "
				+ "from sim_transformtrade " + "where nvl(dr,0)=0 "
				+ "and billtypecode = '0404' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities2 ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and hr_pk_assetsprop ='" + pkass
				+ "' and hr_pk_capaccount ='" + pkcapa
				+ "' and hr_pk_stocksort ='" + pksto + "' and trade_date < '"
				+ begin_date + "'";
		// ��ծת�йܣ��������-ת����;
		String sql2 = "select sum(bargain_num),sum(bargain_sum) "
				+ "from sim_transformtrade " + "where nvl(dr,0)=0 "
				+ "and billtypecode = '0404' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities2 ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and hc_pk_assetsprop ='" + pkass
				+ "' and hc_pk_capaccount ='" + pkcapa
				+ "' and hc_pk_stocksort ='" + pksto + "' and trade_date < '"
				+ begin_date + "'";
		try {
			vec1 = (Vector) dao.executeQuery(sql, new VectorProcessor());
			vec2 = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec1.get(0)).get(0) != null
				&& ((Vector) vec1.get(0)).get(1) != null) {
			ZRZC = new UFDouble(((Vector) vec1.get(0)).get(0).toString());
			ZRZCJE = new UFDouble(((Vector) vec1.get(0)).get(1).toString());
		}
		if (((Vector) vec2.get(0)).get(0) != null
				&& ((Vector) vec2.get(0)).get(1) != null) {
			ZRZC = ZRZC.sub(new UFDouble(((Vector) vec2.get(0)).get(0)
					.toString()));
			ZRZCJE = ZRZCJE.sub(new UFDouble(((Vector) vec2.get(0)).get(1)
					.toString()));
		}

		map.put("ZRZC", ZRZC);
		map.put("ZRZCJE", ZRZCJE);
		return map;

	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO ��ĩ��ծת�й�,�й�ת��ծ��֤ȯת����¼
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQMZQZHTG(NewStockChangeVO stvo, String end_date) {
		UFDouble ZRZC = new UFDouble(0);
		UFDouble ZRZCJE = new UFDouble(0);
		Map map = new ArrayMap();
		String pkass = stvo.getPk_assetsprop();
		String pkcapa = stvo.getPk_capaccount();
		String pksec = stvo.getPk_securities();
		String pkorg = stvo.getPk_org();
		String pkgro = stvo.getPk_group();
		String pksto = stvo.getPk_stocksort();
		Vector vec1 = new Vector();
		Vector vec2 = new Vector();
		BaseDAO dao = new BaseDAO();
		// ��ծת�йܣ��������+ת����;
		String sql = "select sum(bargain_num2),sum(bargain_sum2) "
				+ "from sim_transformtrade " + "where nvl(dr,0)=0 "
				+ "and billtypecode = '0404' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities2 ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and hr_pk_assetsprop ='" + pkass
				+ "' and hr_pk_capaccount ='" + pkcapa
				+ "' and hr_pk_stocksort ='" + pksto + "' and trade_date <= '"
				+ end_date.substring(0, 10) + " 23:59:59" + "'";
		// ��ծת�йܣ��������-ת����;
		String sql2 = "select sum(bargain_num),sum(bargain_sum) "
				+ "from sim_transformtrade " + "where nvl(dr,0)=0 "
				+ "and billtypecode = '0404' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities2 ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and hc_pk_assetsprop ='" + pkass
				+ "' and hc_pk_capaccount ='" + pkcapa
				+ "' and hc_pk_stocksort ='" + pksto + "' and trade_date <= '"
				+ end_date.substring(0, 10) + " 23:59:59" + "'";
		try {
			vec1 = (Vector) dao.executeQuery(sql, new VectorProcessor());
			vec2 = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if (((Vector) vec1.get(0)).get(0) != null
				&& ((Vector) vec1.get(0)).get(1) != null) {
			ZRZC = new UFDouble(((Vector) vec1.get(0)).get(0).toString());
			ZRZCJE = new UFDouble(((Vector) vec1.get(0)).get(1).toString());
		}
		if (((Vector) vec2.get(0)).get(0) != null
				&& ((Vector) vec2.get(0)).get(1) != null) {
			ZRZC = ZRZC.sub(new UFDouble(((Vector) vec2.get(0)).get(0)
					.toString()));
			ZRZCJE = ZRZCJE.sub(new UFDouble(((Vector) vec2.get(0)).get(1)
					.toString()));
		}

		map.put("ZRZC", ZRZC);
		map.put("ZRZCJE", ZRZCJE);
		return map;

	}
}
