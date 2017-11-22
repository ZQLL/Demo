package nc.impl.fba_sim.costplugin.stock;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.fba_sim.costingplugin.AbstractStock;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

/**
 * 份额分红 ：证券数量和金额增加，资金不变
 * 
 * @author Neil
 * 
 */
public class StockShares extends AbstractStock {

	@Override
	protected void calculateWhenCheck(ICostingTool costingtool,
			StocktradeVO tradevo) throws Exception {
		PubMethod pm = PubMethod.getInstance();
		String trade_date = costingtool.getCurrdate();
		ICostBalanceTool tool = costingtool.getBalancetool();
		String[] costFieldArray = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		String key = VOUtil.getCombinesKey(tradevo, costFieldArray);

		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		/** 2016年5月6日 JINGQT 这个KEY的值缺少业务组。此处拼接key必须加上业务组 ADD START */
		key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + key;
		/** 2016年5月6日 JINGQT 这个KEY的值缺少业务组。此处拼接key必须加上业务组 ADD END */
		queryvo.setKey(key);
		queryvo.setPk_assetsprop(tradevo.getPk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getPk_stocksort());
		queryvo.setTrade_date(trade_date);

		StockBalanceVO stockbalancevo = tool.getStockbalanceVO(queryvo,
				costingtool);

		if (stockbalancevo == null) {
			stockbalancevo = tool.getStockbalanceVOByVO(tradevo, costingtool);
			stockbalancevo.setBegin_date(tradevo.getBegin_date());
			stockbalancevo.setEnd_date(tradevo.getEnd_date());
		}
		UFDouble stocks_num = pm.add(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		UFDouble stocks_sum = null;
		/***** update by lihbj 华西证券 若交易类型为份额分红 直接取成交金额字段 start *****/
		/*
		 * if (SystemConst.assetsprop[0] ==
		 * pm.getAssetsProp(tradevo.getPk_assetsprop(), tradevo.getPk_org())) {
		 * stocks_sum = pm.add(stockbalancevo.getStocks_sum(),
		 * tradevo.getBargain_sum()); } else { stocks_sum =
		 * pm.add(stockbalancevo.getStocks_sum(), tradevo.getFact_sum()); }
		 */
		stocks_sum = pm.add(stockbalancevo.getStocks_sum(),
				tradevo.getBargain_sum());
		/***** update by lihbj 华西证券 若交易类型为份额分红 直接取成交金额字段 end *****/
		stockbalancevo.setStocks_num(stocks_num);
		stockbalancevo.setStocks_sum(pm.setScale(stocks_sum, true, true));

		tool.updateStockbalanceVO(queryvo, stockbalancevo);

		/**
		 * 计算结转未付收益 add by lhbj 结转未付收益=应收股利=季末计提未付收益-结转未付收益VDEF3+转换转入-转换转出
		 */
		tradevo.setVdef3(sumJZWFSY(tradevo).toString());
	}

	/**
	 * 新增弃审逻辑 -- 将结转未付收益置空vdef3
	 * 
	 * @author lihbj
	 */
	
	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			StocktradeVO tradevo) throws Exception {
		tradevo.setVdef3(null);
	}

	/**
	 * 计算上一次份额分红之后，所有计提未付收益相加
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumJZWFSY(StocktradeVO tradevo) {
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
	 * 计算 转换转入-转换转出 转换转入（转入证券对应的VDEF2） 转换转出（转出证券对应的VDEF2）
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumJZWFSY_ZH(StocktradeVO repVo) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		// 转出证券
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
	 * 计算 份额分红交易类型总的计提未付收益
	 * 
	 * @author lihbj
	 * @return
	 */
	private UFDouble sumVDEF3(StocktradeVO repVo) {
		UFDouble total_wfsy = UFDouble.ZERO_DBL;
		// 份额分红
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
		// 赎回
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
