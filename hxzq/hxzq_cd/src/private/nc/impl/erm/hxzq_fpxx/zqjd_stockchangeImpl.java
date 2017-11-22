package nc.impl.erm.hxzq_fpxx;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.fba_zqjd.trade.report.StockChangeVO;
import nc.itf.erm.hxzq_fpxx.Izqjd_stockchange;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.olap4j.impl.ArrayMap;
import java.util.*;
public class zqjd_stockchangeImpl implements Izqjd_stockchange {
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 期初证券交易记录证券的买入卖出
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQCZQJY(StockChangeVO scvo, String begin_date) {
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
		// 查找计提日期之前的所有证券交易记录里面该券的交易数量 买入和卖出 -zq
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
			// TODO 自动生成的 catch 块
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
	 * @param StockBalanceVO 期初资产转负债,负债转资产的证券转换记录
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQCZQZH(StockChangeVO scvo, String begin_date) {
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
		// 资产转负债，库存数量-转入量;转出证券类型是当前类型，转入是资产属性，转出为负债属性;
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
			// TODO 自动生成的 catch 块
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
	 * @param StockBalanceVO 期末证券交易记录证券的买入卖出
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQMZQJY(StockChangeVO scvo, String end_date) {
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
		// 查找计提日期之前的所有证券交易记录里面该券的交易数量 买入和卖出 -zq
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
			// TODO 自动生成的 catch 块
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
	 * @param StockBalanceVO 期末资产转负债,负债转资产的证券转换记录
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchQMZQZH(StockChangeVO scvo, String end_date) {
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
		// 资产转负债，库存数量-转入量;转出证券类型是当前类型，转入是资产属性，转出为负债属性;
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
			// TODO 自动生成的 catch 块
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

}
