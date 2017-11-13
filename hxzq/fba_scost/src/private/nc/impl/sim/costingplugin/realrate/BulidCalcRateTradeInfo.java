package nc.impl.sim.costingplugin.realrate;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_sjll.sjll.actualrate.pub.QueryPubInfo;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.fba_sjll.sjll.actualrate.pub.ActualrateConst;
import nc.vo.fba_sjll.sjll.amortize.AmortizeVO;
import nc.vo.fba_sjll.sjll.calcrealrate.ActualRateBillInfo;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.ICalendar;

/**
 * 构造实际利率单据信息
 * 
 * @author zpm
 * 
 */
public class BulidCalcRateTradeInfo {

	public ActualRateBillInfo bulidBondAmortize(AmortizeVO tradevo) {
		ActualRateBillInfo info = new ActualRateBillInfo();
		info.setBill_code(tradevo.getBillno());
		info.setPk_oldbill(tradevo.getPk_amortize_b());// pk_bill主键不用赋值
		info.setPk_assetsprop(tradevo.getPk_assetsprop());
		info.setPk_stocksort(tradevo.getPk_stocksort());
		info.setPk_group(tradevo.getPk_group());
		info.setPk_org(tradevo.getPk_org());
		info.setPk_org_v(tradevo.getPk_org_v());// 最好加上这个
		info.setPk_glorgbook(tradevo.getPk_glorgbook());
		info.setPk_capaccount(tradevo.getPk_capaccount());
		info.setPk_selfsgroup(tradevo.getPk_selfsgroup());
		info.setPk_client(tradevo.getPk_client());
		info.setPk_operatesite(tradevo.getPk_operatesite());
		info.setPk_partnaccount(tradevo.getPk_partnaccount());
		info.setPk_securities(tradevo.getPk_securities());
		info.setPk_busitype(null);// 业务类型
		info.setTranstypecode(tradevo.getBilltypecode());// 单据类型
		info.setBargain_num(null);
		info.setBargain_sum(null);
		info.setFact_sum(null);
		//
		info.setRealinterest(tradevo.getRealinterest_amortize());// 实际利息 +
		info.setAccrual_sum(tradevo.getInterest_amortize());// 应收利息 +
		info.setInterestadjust(tradevo.getInterest_adjust());// 利息调整 +
		//
		info.setRealinterest_sell(null);// 实际利息 -
		info.setInterest_sell(null);// 应收利息 -
		info.setInterestadjust_sell(null);// 利息调整 -
		//
		info.setNdef1(null);
		info.setNdef2(null);
		info.setNdef3(null);
		info.setNdef4(null);
		info.setNdef5(null);
		info.setInterestdate(null);
		info.setTrade_date(tradevo.getEnd_amortize_date());
		info.setAttributeValue("dr", 0);
		return info;
	}

	public ActualRateBillInfo bulidStockTradeInfo(StocktradeVO tradevo) {
		ActualRateBillInfo info = new ActualRateBillInfo();
		info.setBill_code(tradevo.getBillno());
		info.setPk_oldbill(tradevo.getPk_stocktrade());// pk_bill主键不用赋值
		info.setPk_assetsprop(tradevo.getPk_assetsprop());
		info.setPk_stocksort(tradevo.getPk_stocksort());
		info.setPk_group(tradevo.getPk_group());
		info.setPk_org(tradevo.getPk_org());
		info.setPk_org_v(tradevo.getPk_org_v());// 最好加上这个
		info.setPk_glorgbook(tradevo.getPk_glorgbook());
		info.setPk_capaccount(tradevo.getPk_capaccount());
		info.setPk_selfsgroup(tradevo.getPk_selfsgroup());
		info.setPk_client(tradevo.getPk_client());
		info.setPk_operatesite(tradevo.getPk_operatesite());
		info.setPk_partnaccount(tradevo.getPk_partnaccount());
		info.setPk_securities(tradevo.getPk_securities());
		info.setPk_busitype(tradevo.getPk_busitype());// 业务类型
		info.setTranstypecode(tradevo.getTranstypecode());// 单据类型
		info.setBargain_num(tradevo.getBargain_num());
		info.setBargain_sum(tradevo.getBargain_sum());
		info.setFact_sum(tradevo.getFact_sum());
		if (ActualrateConst.TSC1.equals(tradevo.getTranstypecode())) {
			info.setRealinterest(null);// 实际利息 +
			info.setAccrual_sum(tradevo.getAccrual_sum());// 应收利息 +
			info.setInterestadjust(tradevo.getInterestadjust());// 利息调整 +
		} else if (ActualrateConst.TSC2.equals(tradevo.getTranstypecode())) {
			info.setRealinterest_sell(tradevo.getRealinterest_sell());// 实际利息 -
			info.setInterest_sell(tradevo.getInterest_sell());// 应收利息 -
			info.setInterestadjust_sell(tradevo.getInterestadjust_sell());// 利息调整
																			// -
			if (tradevo.getInterestdate() != null) {
				info.setInterestdate(tradevo.getInterestdate().toUFLiteralDate(ICalendar.BASE_TIMEZONE));
			}
		} else if (ActualrateConst.TSC5.equals(tradevo.getTranstypecode())) {
			info.setInterest_sell(tradevo.getInterest_sell());// 应收利息 -
		}
		info.setNdef1(null);
		info.setNdef2(null);
		info.setNdef3(null);
		info.setNdef4(null);
		info.setNdef5(null);
		info.setTrade_date(tradevo.getTrade_date().toUFLiteralDate(ICalendar.BASE_TIMEZONE));
		info.setAttributeValue("dr", 0);
		return info;
	}

	public ActualRateBillInfo bulidTransfromInfo(TransformtradeVO tradevo, String flag) {
		ActualRateBillInfo info = new ActualRateBillInfo();
		info.setBill_code(tradevo.getBillno());
		info.setPk_group(tradevo.getPk_group());
		info.setPk_org(tradevo.getPk_org());
		info.setPk_org_v(tradevo.getPk_org_v());// 最好加上这个
		info.setPk_glorgbook(tradevo.getPk_glorgbook());
		info.setPk_oldbill(tradevo.getPk_transformtrade());// pk_bill主键不用赋值
		//
		if (ActualrateConst.TOUT.equals(flag)) {// 转出
			info.setPk_assetsprop(tradevo.getHc_pk_assetsprop());
			info.setPk_stocksort(tradevo.getHc_pk_stocksort());
			info.setPk_capaccount(tradevo.getHc_pk_capaccount());
			info.setPk_selfsgroup(tradevo.getHc_pk_selfsgroup());
			info.setPk_client(tradevo.getHc_pk_client());
			info.setPk_operatesite(tradevo.getHc_pk_operatesite());
			info.setPk_partnaccount(tradevo.getHc_pk_partnaccount());
			info.setPk_securities(tradevo.getPk_securities());
			//
			info.setInterest_sell(tradevo.getInterest_sell());// 应收利息 -
			info.setInterestadjust_sell(tradevo.getInterestadjust_sell());// 利息调整
																			// -
			info.setRealinterest_sell(tradevo.getRealinterest_sell());// 实际利息 -
			//
			info.setPk_busitype(tradevo.getPk_busitype());// 业务类型
			info.setTranstypecode(ActualrateConst.TOUT);// 单据类型
			info.setInterestdate(tradevo.getInterestdate().toUFLiteralDate(ICalendar.BASE_TIMEZONE));
		} else if (ActualrateConst.TIN.equals(flag)) {
			info.setPk_assetsprop(tradevo.getHr_pk_assetsprop());
			info.setPk_stocksort(tradevo.getHr_pk_stocksort());
			info.setPk_capaccount(tradevo.getHr_pk_capaccount());
			info.setPk_selfsgroup(tradevo.getHr_pk_selfsgroup());
			info.setPk_client(tradevo.getHr_pk_client());
			info.setPk_operatesite(tradevo.getHr_pk_operatesite());
			info.setPk_partnaccount(tradevo.getHr_pk_partnaccount());
			info.setPk_securities(tradevo.getPk_securities2());
			//
			info.setAccrual_sum(tradevo.getInterest_sell());// 应收利息 (+)
			info.setInterestadjust(tradevo.getInterestadjust_sell());// 利息调整 (+)
			info.setRealinterest(tradevo.getRealinterest_sell());// 实际利息 (+)
			//
			info.setPk_busitype(tradevo.getPk_busitype());// 业务类型
			info.setTranstypecode(ActualrateConst.TIN);// 单据类型
		}
		//
		info.setBargain_num(tradevo.getBargain_num());
		info.setBargain_sum(tradevo.getBargain_sum());
		info.setFact_sum(tradevo.getFact_sum());
		//
		info.setNdef1(null);
		info.setNdef2(null);
		info.setNdef3(null);
		info.setNdef4(null);
		info.setNdef5(null);
		info.setTrade_date(tradevo.getTrade_date().toUFLiteralDate(ICalendar.BASE_TIMEZONE));
		info.setAttributeValue("dr", 0);
		return info;
	}

	public boolean isRealTradevo(StockBalanceVO vo) throws BusinessException {
		boolean isfalg = false;
		QueryPubInfo info = new QueryPubInfo();
		SecuritiesVO v1 = info.querySecuritiesVO(vo.getPk_securities());
		String value = info.queryParavalue(vo.getPk_glorgbook(), ActualrateConst.PARAM_REAL);// 是否启用实际利率
		if ("Y".equals(value) && v1.getIf_accrual() != null && v1.getIf_accrual().booleanValue()) {
			if (ActualrateConst.PROPERTY_ATTRIBUTE2.equals(vo.getPk_assetsprop())
					|| ActualrateConst.PROPERTY_ATTRIBUTE3.equals(vo.getPk_assetsprop()))
				isfalg = true;
		}
		return isfalg;
	}

	public boolean isRealTradevo(StocktradeVO vo) throws BusinessException {
		boolean isfalg = false;
		QueryPubInfo info = new QueryPubInfo();
		SecuritiesVO v1 = info.querySecuritiesVO(vo.getPk_securities());
		String value = info.queryParavalue(vo.getPk_glorgbook(), ActualrateConst.PARAM_REAL);// 是否启用实际利率
		if ("Y".equals(value) && v1.getIf_accrual() != null && v1.getIf_accrual().booleanValue()) {
			// 可供出售金融资产、持有至到期金融资产 采用实际利率算法
			if (ActualrateConst.PROPERTY_ATTRIBUTE2.equals(vo.getPk_assetsprop())
					|| ActualrateConst.PROPERTY_ATTRIBUTE3.equals(vo.getPk_assetsprop()))
				isfalg = true;
		}
		return isfalg;
	}

	public boolean isRealTradevo(AmortizeVO vo) throws BusinessException {
		boolean isfalg = false;
		QueryPubInfo info = new QueryPubInfo();
		SecuritiesVO v1 = info.querySecuritiesVO(vo.getPk_securities());
		String value = info.queryParavalue(vo.getPk_glorgbook(), ActualrateConst.PARAM_REAL);// 是否启用实际利率
		if ("Y".equals(value) && v1.getIf_accrual() != null && v1.getIf_accrual().booleanValue()) {
			if (ActualrateConst.PROPERTY_ATTRIBUTE2.equals(vo.getPk_assetsprop())
					|| ActualrateConst.PROPERTY_ATTRIBUTE3.equals(vo.getPk_assetsprop()))
				isfalg = true;
		}
		return isfalg;
	}

	public boolean isRealTradevo(TransformtradeVO vo) throws BusinessException {
		boolean isfalg = false;
		QueryPubInfo info = new QueryPubInfo();
		SecuritiesVO v1 = info.querySecuritiesVO(vo.getPk_securities());
		String value = info.queryParavalue(vo.getPk_glorgbook(), ActualrateConst.PARAM_REAL);// 是否启用实际利率
		if ("Y".equals(value) && v1.getIf_accrual() != null && v1.getIf_accrual().booleanValue()) {
			if (ActualrateConst.PROPERTY_ATTRIBUTE2.equals(vo.getHc_pk_assetsprop())
					|| ActualrateConst.PROPERTY_ATTRIBUTE3.equals(vo.getHc_pk_assetsprop()))
				isfalg = true;
		}
		return isfalg;
	}

	public void save(ActualRateBillInfo vo) throws DAOException {
		if (vo == null)
			return;
		BaseDAO dao = new BaseDAO();
		dao.insertVO(vo);
	}

	public void save(ActualRateBillInfo[] vos) throws DAOException {
		if (vos == null || vos.length == 0)
			return;
		BaseDAO dao = new BaseDAO();
		dao.insertVOArray(vos);
	}

	public void delete(String pk_group, String pk_org, String tradedate) throws DAOException {
		if (pk_group == null || pk_org == null || tradedate == null)
			return;
		BaseDAO dao = new BaseDAO();
		dao.deleteByClause(ActualRateBillInfo.class, " trade_date >= '" + tradedate + "' and pk_group = '" + pk_group
				+ "' and pk_org = '" + pk_org + "' ");
	}

	public void delete(String pk_group, String pk_org, String tradedate, String billtypecode) throws DAOException {
		if (pk_group == null || pk_org == null || tradedate == null)
			return;
		String where = " transtypecode = '" + billtypecode + "' and trade_date >= '" + tradedate + "' and pk_group = '"
				+ pk_group + "' and pk_org = '" + pk_org + "' ";
		BaseDAO dao = new BaseDAO();
		dao.deleteByClause(ActualRateBillInfo.class, where);
	}
}
