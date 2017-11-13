package nc.bs.fba_scost.cost.interest.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_scost.cost.pub.QueryBasePubInfo;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.tool.fba_zqjd.pub.AssetsPropTool;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.trademarket.TradeMarketVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;
import nc.vo.uif2.LoginContext;

import org.olap4j.impl.ArrayMap;

/**
 * 应收利息计提
 * 
 * @update zq 证券借贷利息计提;
 */
public class CalcInterestByConverse {
	/**
	 * 应收利息计提
	 */
	public List<InterestdistillVO> calcInterest(CostPlanVO costplanvo,
			LoginContext context, UFDate trade_date) throws BusinessException {
		QueryInterestBaseInfo qi = new QueryInterestBaseInfo();
		List<StockBalanceVO> list = (List<StockBalanceVO>) qi.queryLastStock(
				costplanvo, context.getPk_group(), context.getPk_org(),
				trade_date);
		if (list == null || list.size() == 0)
			return null;
		List<InterestdistillVO> zlist = new ArrayList<InterestdistillVO>();
		String pk_org = AppContextUtil.getDefaultOrgBook();
		// 判断是否启用实际利率法
		boolean isreal = false;
		String result = qi.getStringFromInitcode(pk_org,
				SystemConst.SYSINIT_ISREALRATE);
		if ("Y".equals(result))
			isreal = true;
		// 查询期初应收利息汇总
		// --这里不做债券的一次性查询，而是单独查询。---原因，每支债券的起息日期不一致，而我只想查询本次付息期间账面利息。
		// --已可以将所有债券一次性查询，不用管起息日期---此方案也好，考虑效率。有机会在改吧。
		String way = qi.getStringFromInitcode(pk_org,
				CostConstant.PARAM_JTINTEREST);
		// start 20150615 性能优化 mx
		BulidDistillVOInfo info = new BulidDistillVOInfo();
		int order = 0;
		InterestdistillVO tempvo = new InterestdistillVO();
		String billtype = info.getBilltypePK(CostConstant.CJT_BILLTYPE);
		tempvo.setPk_group(context.getPk_group());
		tempvo.setPk_org(context.getPk_org());
		tempvo.setPk_billtype(billtype);
		// 批量获取单据号
		String[] vbillcodes = info.getBillNOs(context.getPk_group(),
				context.getPk_org(), tempvo, list.size());
		BulidDistillVOInfo in = new BulidDistillVOInfo();
		for (int i = 0; i < list.size(); i++) {
			list.get(i);
			list.size();
			StockBalanceVO stvo = (StockBalanceVO) list.get(i);
			if (isreal
					&& !SystemConst.pk_defaultstocksort.equals(stvo
							.getPk_assetsprop()))// 启用实际利率
				continue;
			// 2015年4月15日 zhaoxmc start 负债类资产，不计提应收利息
			AssetsPropTool propTool = new AssetsPropTool();
			if (propTool.whetherDebtAssetsContainIt(stvo.getPk_assetsprop()))
				continue;
			TradeMarketVO vo = qi.queryLastMarket(stvo.getPk_securities(),
					trade_date);
			if ("0001SE00000000000004".equals(stvo.getPk_assetsprop())) {
				order++;
				continue;
 			
//				UFDouble lastnum = new UFDouble(0);
//				UFDouble lastJE = new UFDouble(0);
//				UFDouble MRMC = new UFDouble(SearchZQJY(stvo, trade_date)
//						.get("MRMC").toString()); // 计算出债券的买入卖出量；
//				UFDouble ZRZC = new UFDouble(SearchZQZH(stvo, trade_date)
//						.get("ZRZC").toString()); // 计算出债券的转入转出量；
//				UFDouble ZRZCTG = new UFDouble(SearchZQZHTG(stvo, trade_date)
//						.get("ZRZC").toString()); // 计算出债券的转入转出托管量；
//				UFDouble MRMCJE = new UFDouble(SearchZQJY(stvo, trade_date)
//						.get("MRMCJE").toString()); // 计算出债券的买入卖出金额；
//				UFDouble ZRZCJE = new UFDouble(SearchZQZH(stvo, trade_date)
//						.get("ZRZCJE").toString()); // 计算出债券的转入转出金额；
//				UFDouble ZRZCJETG = new UFDouble(SearchZQZHTG(stvo, trade_date)
//						.get("ZRZCJE").toString()); // 计算出债券的转入转出金额；
//				// 存在买入卖出或者债券转换，进行余量计算
//				if (!MRMC.equals(UFDouble.ZERO_DBL)
//						|| !ZRZC.equals(UFDouble.ZERO_DBL) || !ZRZCTG.equals(UFDouble.ZERO_DBL)) {
//					lastnum = MRMC.add(ZRZC).add(ZRZCTG);
//					lastJE = MRMCJE.add(ZRZCJE).add(ZRZCJETG);
//					stvo.setStocks_num(lastnum);
//					stvo.setStocks_sum(lastJE);
//				} else{
//					order++;
//					continue;
//				}
			}

			UFDouble qm = calcQmInterest(stvo, vo, trade_date, way);
			UFDouble qc = calcQcInterest(stvo, costplanvo, context, trade_date);

			InterestdistillVO zvo = in.buildInterestdistillVO(trade_date, stvo,
					qm, qc, order, vbillcodes, billtype);
			zlist.add(zvo);
			order++;
		}

		List<StockBalanceVO> fzlist = queryfzStock(costplanvo,
				context.getPk_group(), context.getPk_org(), trade_date);
		String[] fzvbillcodes = info.getBillNOs(context.getPk_group(),
				context.getPk_org(), tempvo, fzlist.size());
		int fzorder = 0;
		/*
		 * @author by zq 添加交易性资产负债，进行应收利息计提
		 */
		for (StockBalanceVO stblvo : fzlist) {
			if ("0001SE00000000000004".equals(stblvo.getPk_assetsprop())) {
//				UFDouble lastnum = new UFDouble(0);
//				UFDouble lastJE = new UFDouble(0);
//				UFDouble MRMC = new UFDouble(SearchZQJY(stblvo, trade_date)
//						.get("MRMC").toString()); // 计算出债券的买入卖出量；
//				UFDouble ZRZC = new UFDouble(SearchZQZH(stblvo, trade_date)
//						.get("ZRZC").toString()); // 计算出债券的转入转出量；
//				UFDouble ZRZCTG = new UFDouble(SearchZQZHTG(stblvo, trade_date)
//						.get("ZRZC").toString()); // 计算出债券的转入转出托管量；
//				UFDouble MRMCJE = new UFDouble(SearchZQJY(stblvo, trade_date)
//						.get("MRMCJE").toString()); // 计算出债券的买入卖出金额；
//				UFDouble ZRZCJE = new UFDouble(SearchZQZH(stblvo, trade_date)
//						.get("ZRZCJE").toString()); // 计算出债券的转入转出金额；
//				UFDouble ZRZCJETG = new UFDouble(SearchZQZHTG(stblvo, trade_date)
//						.get("ZRZCJE").toString()); // 计算出债券的转入转出金额；
//				// 存在买入卖出或者债券转换，进行余量计算
//				if (!MRMC.equals(UFDouble.ZERO_DBL)
//						|| !ZRZC.equals(UFDouble.ZERO_DBL) || !ZRZCTG.equals(UFDouble.ZERO_DBL)) {
//					lastnum = MRMC.add(ZRZC).add(ZRZCTG);
//					lastJE = MRMCJE.add(ZRZCJE).add(ZRZCJETG);
					stblvo.setStocks_num(new UFDouble(stblvo.getVdef1()));
					stblvo.setStocks_sum(new UFDouble(stblvo.getVdef2()));
//				} else
//					continue;
			}
			TradeMarketVO vo = qi.queryLastMarket(stblvo.getPk_securities(),
					trade_date);

			UFDouble qm = calcQmInterest(stblvo, vo, trade_date, way);
			UFDouble qc = calcQcInterest(stblvo, costplanvo, context,
					trade_date);

			InterestdistillVO zvo = in.buildInterestdistillVO(trade_date,
					stblvo, qm, qc, fzorder, fzvbillcodes, billtype);
			zlist.add(zvo);
			fzorder++;
		}

		return zlist;
	}

	/*
	 * -zq查出负债库存
	 */
	@SuppressWarnings("unchecked")
	private List<StockBalanceVO> queryfzStock(CostPlanVO costplanvo,
			String pk_group, String pk_org, UFDate trade_date)
			throws BusinessException {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		if (costplanvo.getIf_glorgbook() != null
				&& costplanvo.getIf_glorgbook().booleanValue()) {
			sb.append(" a.pk_glorgbook, ");
			sb1.append(" and isnull(x.pk_glorgbook, 'pk_glorgbook') =isnull(y.pk_glorgbook, 'pk_glorgbook') ");
		}
		if (costplanvo.getIf_capaccount() != null
				&& costplanvo.getIf_capaccount().booleanValue()) {
			sb.append(" a.pk_capaccount, ");
			sb1.append(" and isnull(x.pk_capaccount, 'pk_capaccount') =isnull(y.pk_capaccount, 'pk_capaccount') ");
		}
		if (costplanvo.getIf_selfsgroup() != null
				&& costplanvo.getIf_selfsgroup().booleanValue()) {
			sb.append(" a.pk_selfsgroup, ");
			sb1.append(" and isnull(x.pk_selfsgroup, 'pk_selfsgroup') =isnull(y.pk_selfsgroup, 'pk_selfsgroup') ");
		}
		if (costplanvo.getIf_partnaccount() != null
				&& costplanvo.getIf_partnaccount().booleanValue()) {
			sb.append(" a.pk_partnaccount,  ");
			sb1.append(" and isnull(x.pk_partnaccount, 'pk_partnaccount') =isnull(y.pk_partnaccount, 'pk_partnaccount') ");
		}
		if (costplanvo.getIf_operatesite() != null
				&& costplanvo.getIf_operatesite().booleanValue()) {
			sb.append(" a.pk_operatesite, ");
			sb1.append(" and isnull(x.pk_operatesite, 'pk_operatesite') =isnull(y.pk_operatesite, 'pk_operatesite') ");
		}
		if (costplanvo.getIf_client() != null
				&& costplanvo.getIf_client().booleanValue()) {
			sb.append(" a.pk_client, ");
			sb1.append(" and isnull(x.pk_client, 'pk_client') =isnull(y.pk_client, 'pk_client') ");
		}
		StringBuffer sf = new StringBuffer();
		sf.append(" select y.* from (select max(a.trade_date) trade_date,  ");
		sf.append("            a.pk_org, a.pk_group,a.pk_assetsprop,a.pk_stocksort, ");
		sf.append(sb);
		sf.append("            a.pk_securities ");
		sf.append("        from sim_stockbalance a ");
		sf.append("      join sec_securities b on a.pk_securities = b.pk_securities ");
		sf.append("      where a.trade_date <='" + trade_date.asEnd() + "' ");
		sf.append("         and isnull(a.dr, 0) = 0 ");
		sf.append("         and isnull(b.dr, 0) = 0 ");
		sf.append("         and b.if_accrual  = 'Y' ");
		sf.append("         and a.pk_org = '" + pk_org + "' ");
		sf.append("         and a.pk_group = '" + pk_group + "' ");
		sf.append(" 		and a.pk_glorgbook = '"
				+ AppContextUtil.getDefaultOrgBook() + "'  ");
		sf.append("       group by  ");
		sf.append("               a.pk_org, a.pk_group,a.pk_assetsprop,a.pk_stocksort, ");
		sf.append(sb);
		sf.append("               a.pk_securities) x ");
		sf.append(" join sim_stockbalance y ");
		sf.append("    on x.trade_date = y.trade_date ");
		sf.append("   and isnull(x.pk_org, 'pk_org') = isnull(y.pk_org, 'pk_org') ");
		sf.append("   and isnull(x.pk_group, 'pk_group') = isnull(y.pk_group, 'pk_group') ");
		sf.append(sb1);
		sf.append("   and isnull(x.pk_securities, 'pk_securities') = isnull(y.pk_securities, 'pk_securities') ");
		sf.append("   and isnull(x.pk_assetsprop, 'pk_assetsprop') = isnull(y.pk_assetsprop, 'pk_assetsprop') ");
		sf.append("   and isnull(x.pk_stocksort, 'pk_stocksort') = isnull(y.pk_stocksort, 'pk_stocksort') ");
		sf.append("   and y.vdef1 <> '0.00000000' ");
		sf.append("   and y.vdef1 <> '~'");
		sf.append("   and y.pk_costplan  = '" + costplanvo.getPk_costplan()
				+ "' ");
		List<StockBalanceVO> list = null;
		try {
			list = (List<StockBalanceVO>) new BaseDAO().executeQuery(
					sf.toString(), new BeanListProcessor(StockBalanceVO.class));
		} catch (DAOException e) {
			throw new BusinessException(e);
		}
		return list;
	}

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 证券交易记录证券的买入卖出
	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public Map SearchZQJY(StockBalanceVO stvo, UFDate trade_date) {
//		UFDouble MRMC = new UFDouble(0);
//		UFDouble MRMCJE = new UFDouble(0);
//		Map map = new ArrayMap();
//		String pkcapa = stvo.getPk_capaccount();
//		String pkglo = stvo.getPk_glorgbook();
//		String pksec = stvo.getPk_securities();
//		String pkorg = stvo.getPk_org();
//		String pkgro = stvo.getPk_group();
//		String pksto = stvo.getPk_stocksort();
//		Vector vecmc = new Vector();
//		Vector vecmr = new Vector();
//		BaseDAO dao = new BaseDAO();
//		// 查找计提日期之前的所有证券交易记录里面该券的交易数量 买入和卖出 -zq
//		String sql1 = "select sum(bargain_num),sum(bargain_sum) from sim_stocktrade where pk_securities ='"
//				+ pksec
//				+ "' and pk_stocksort ='"
//				+ pksto
//				+ "' and pk_org ='"
//				+ pkorg
//				+ "' and pk_group ='"
//				+ pkgro
//				+ "' and pk_glorgbook ='"
//				+ pkglo
//				+ "' and pk_assetsprop ='"
//				+ "0001SE00000000000004"
//				+ "' and pk_capaccount ='"
//				+ pkcapa
//				+ "' and transtypecode = 'HV3A-0xx-22"
//				+ "' and nvl(dr,0)=0 and trade_date <= '"
//				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";
//
//		String sql2 = "select sum(bargain_num),sum(sellcost) from sim_stocktrade where pk_securities ='"
//				+ pksec
//				+ "' and pk_stocksort ='"
//				+ pksto
//				+ "' and pk_org ='"
//				+ pkorg
//				+ "' and pk_group ='"
//				+ pkgro
//				+ "' and pk_glorgbook ='"
//				+ pkglo
//				+ "' and pk_assetsprop ='"
//				+ "0001SE00000000000004"
//				+ "' and pk_capaccount ='"
//				+ pkcapa
//				+ "' and transtypecode = 'HV3A-0xx-21"
//				+ "' and nvl(dr,0)=0 and trade_date <= '"
//				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";
//
//		try {
//			vecmc = (Vector) dao.executeQuery(sql1, new VectorProcessor());
//
//			vecmr = (Vector) dao.executeQuery(sql2, new VectorProcessor());
//		} catch (DAOException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		if (((Vector) vecmc.get(0)).get(0) != null) {
//			MRMC = new UFDouble(((Vector) vecmc.get(0)).get(0).toString());
//			MRMCJE = new UFDouble(((Vector) vecmc.get(0)).get(1).toString());
//		}
//		if (((Vector) vecmr.get(0)).get(0) != null) {
//			MRMC = MRMC.sub(new UFDouble(((Vector) vecmr.get(0)).get(0)
//					.toString()));
//			MRMCJE = MRMCJE.sub(new UFDouble(((Vector) vecmr.get(0)).get(1)
//					.toString()));
//		}
//		map.put("MRMC", MRMC);
//		map.put("MRMCJE", MRMCJE);
//		return map;
//
//	}

/*	
	 * @author zq
	 * 
	 * @param StockBalanceVO 资产转负债,负债转资产的证券转换记录
	 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchZQZH(StockBalanceVO stvo, UFDate trade_date) {
		UFDouble ZRZC = new UFDouble(0);
		UFDouble ZRZCJE = new UFDouble(0);
		Map map = new ArrayMap();
		String pkcapa = stvo.getPk_capaccount();
		String pkglo = stvo.getPk_glorgbook();
		String pksec = stvo.getPk_securities();
		String pkorg = stvo.getPk_org();
		String pkgro = stvo.getPk_group();
		String pksto = stvo.getPk_stocksort();
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
				+ "' and pk_glorgbook='"
				+ pkglo
				+ "' and hc_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and pk_securities2 ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <='"
				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";

		String sql2 = "select sum(bargain_num),sum(bargain_sum) "
				+ "from sim_transformtrade "
				+ "where transtypecode = 'HV3F-0xx-01'"
				+ " and nvl(dr,0)=0"
				+ " and state >= 2"
				+ " and hr_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and hc_pk_capaccount = '" + pkcapa + "' and pk_glorgbook='"
				+ pkglo + "' and hc_pk_assetsprop = '0001SE00000000000004'"
				+ " and pk_securities ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <='"
				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";

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

	}*/

	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO 负债转托管,托管转负债的证券转换记录
	 */
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SearchZQZHTG(StockBalanceVO stvo, UFDate trade_date) {
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
		// 负债转托管，库存数量+转入量;
		String sql = "select sum(bargain_num2),sum(bargain_sum2) " + "from sim_transformtrade "
				+ "where nvl(dr,0)=0 " + "and pk_busitype = '1001A21000000014ZPP7' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities2 ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and hr_pk_assetsprop ='" + pkass
				+ "' and hr_pk_capaccount ='" + pkcapa + "' and hr_pk_stocksort ='"
				+ pksto + "' and trade_date <= '"
				+ trade_date.toString().substring(0, 10) + " 23:59:59"
				 + "'";
		// 负债转托管，库存数量-转出量;
		String sql2 = "select sum(bargain_num),sum(bargain_sum) " + "from sim_transformtrade "
				+ "where nvl(dr,0)=0 " + "and pk_busitype = '1001A21000000014ZPP7' "
				+ "and transtypecode ='HV3F-Cxx-04'" + " and pk_securities2 ='"
				+ pksec + "' and pk_org ='" + pkorg + "' and pk_group ='"
				+ pkgro + "' and hc_pk_assetsprop ='" + pkass
				+ "' and hc_pk_capaccount ='" + pkcapa + "' and hc_pk_stocksort ='"
				+ pksto + "' and trade_date <= '"
				+ trade_date.toString().substring(0, 10) + " 23:59:59"
				 + "'";
		try {
			vec1 = (Vector) dao.executeQuery(sql, new VectorProcessor());
			vec2 = (Vector) dao.executeQuery(sql2, new VectorProcessor());
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (((Vector) vec1.get(0)).get(0) != null&&((Vector) vec1.get(0)).get(1) != null) {
			ZRZC = new UFDouble(((Vector) vec1.get(0)).get(0).toString());
			ZRZCJE = new UFDouble(((Vector) vec1.get(0)).get(1).toString());
		}
		if (((Vector) vec2.get(0)).get(0) != null&&((Vector) vec2.get(0)).get(1) != null) {
			ZRZC = ZRZC.sub(new UFDouble(((Vector) vec2.get(0)).get(0).toString()));
			ZRZCJE = ZRZCJE.sub(new UFDouble(((Vector) vec2.get(0)).get(1).toString()));
		}

		map.put("ZRZC", ZRZC);
		map.put("ZRZCJE", ZRZCJE);
		return map;

	}*/
	//
	// public UFDouble getaccrual_sum(StockBalanceVO stvo) {
	// UFDouble accrual_sum = null;
	// String sql =
	// "select sum(accrual_sum) from sim_stocktrade where nvl(dr,0) =0 "
	// + "and pk_securities = '"
	// + stvo.getPk_securities()
	// + "' and trade_date like '%"
	// + stvo.getTrade_date().toString().substring(0, 10)
	// + "%' and pk_capaccount = '"
	// + stvo.getPk_capaccount()
	// + "' and transtypecode = 'HV3A-0xx-02'";
	// BaseDAO dao = new BaseDAO();
	// Vector vec = null;
	// try {
	// vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
	// } catch (DAOException e) {
	// // TODO 自动生成的 catch 块
	// e.printStackTrace();
	// }
	// accrual_sum = (UFDouble) ((Vector) vec.get(0)).get(0);
	// return accrual_sum;
	// }

	/**
	 * 计算单支债券的期初应收利息
	 */
	public UFDouble calcQcInterest(ITrade_Data stvoz, CostPlanVO costplanvo,
			LoginContext context, UFDate trade_date) throws BusinessException {
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		// String pk_securities = stvoz.getPk_securities();
		// UFDate firstdate =
		// info.queryFirstFxDate(pk_securities,trade_date);//查询当前债券的起息日期，考虑牌价倒闸，不需要考虑利率设置档案，上述期初应收利息可以做一次性查询，考虑效率问题。
		UFDate firstdate = new UFDate(CostConstant.DEFAULT_DATE);
		InterestdistillVO[] distillvos = info.queryQcInterest(firstdate,
				costplanvo, context, trade_date, stvoz);
		UFDouble zu = calcQcInterest(distillvos);// 应收利息
		return zu;
	}

	/**
	 * 计算单支债券的期初应收利息
	 */
	public UFDouble calcQcInterest(InterestdistillVO[] distillvos) {
		if (distillvos == null || distillvos.length == 0)
			return null;
		UFDouble zu = new UFDouble(0);
		for (InterestdistillVO vo : distillvos) {
			int genstate = vo.getGenstate();
			if (genstate == CostConstant.INTEREST_GENSTATE.JITI.getIndex()// "计提2"
					|| genstate == CostConstant.INTEREST_GENSTATE.ZHUANRU
							.getIndex()) {// "转入3"
				zu = SafeCompute.add(zu, vo.getInterestdistill());
			}
			if (genstate == CostConstant.INTEREST_GENSTATE.ZHUANCHU.getIndex()) {// "转出1"
				zu = SafeCompute.sub(zu, vo.getInterestdistill());
			}
		}
		return zu;
	}

	/**
	 * 计算单支债券的期末应收利息
	 */
	public UFDouble calcQmInterest(StockBalanceVO stvo, TradeMarketVO vo,
			UFDate trade_date, String calcway) throws BusinessException {
		if (stvo == null || trade_date == null)
			return null;
		UFDouble qmlx = null;
		/*
		 * @author by zq 当交易性资产属性为负债的时候，计算方法会有不同，通过交易记录里面的不同类型成交数量来得到参与计算利息的量
		 */
		if ("0001SE00000000000004".equals(stvo.getPk_assetsprop())) {
			UFDouble lastnum = new UFDouble(0);
//			UFDouble MRMC = new UFDouble(SearchZQJY(stvo, trade_date).get(
//					"MRMC").toString()); // 计算出债券的买入卖出量；
//			UFDouble ZRZC = new UFDouble(SearchZQZH(stvo, trade_date).get(
//					"ZRZC").toString()); // 计算出债券的转入转出量；
//			UFDouble ZRZCTG = new UFDouble(SearchZQZHTG(stvo, trade_date)
//					.get("ZRZC").toString()); // 计算出债券的转入转出托管量；
			// 存在买入卖出或者债券转换，进行余量计算
			
				lastnum =new UFDouble(stvo.getVdef1());
				if (CostConstant.CALC_INTEREST_WAY1.equals(calcway)) {// 牌价倒扎
					if (vo == null || vo.getYjlx() == null) {
						QueryBasePubInfo info = new QueryBasePubInfo();
						String name = info.querySecuritesName(stvo
								.getPk_securities());
						throw new BusinessException("证券:[" + name + "]，日期:["
								+ trade_date.toLocalString()
								+ "]行情应收利息没有录入，按牌价倒扎无法计提应收利息！");
					}
					UFDouble lxprice = vo.getYjlx();
					qmlx = SafeCompute.multiply(lastnum, lxprice);
				} else {// 票面利率正算
					CalcBuySellInterest buysell = new CalcBuySellInterest();
					qmlx = buysell.calcInterestTradedate(lastnum,
							stvo.getPk_securities(), trade_date,
							stvo.getPk_glorgbook());
				}
				if (qmlx != null) {
					qmlx = qmlx.setScale(2, UFDouble.ROUND_HALF_UP);
				}
			
		} else {
			if (CostConstant.CALC_INTEREST_WAY1.equals(calcway)) {// 牌价倒扎
				if (vo == null || vo.getYjlx() == null) {
					QueryBasePubInfo info = new QueryBasePubInfo();
					String name = info.querySecuritesName(stvo
							.getPk_securities());
					throw new BusinessException("证券:[" + name + "]，日期:["
							+ trade_date.toLocalString()
							+ "]行情应收利息没有录入，按牌价倒扎无法计提应收利息！");
				}
				UFDouble lxprice = vo.getYjlx();
				qmlx = SafeCompute.multiply(stvo.getStocks_num(), lxprice);
			} else {// 票面利率正算
				CalcBuySellInterest buysell = new CalcBuySellInterest();
				qmlx = buysell.calcInterestTradedate(stvo.getStocks_num(),
						stvo.getPk_securities(), trade_date,
						stvo.getPk_glorgbook());
			}
			if (qmlx != null) {
				qmlx = qmlx.setScale(2, UFDouble.ROUND_HALF_UP);
			}
		}
		return qmlx;
	}

}
