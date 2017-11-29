package nc.impl.fba_sim.pub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.fba_sim.pub.ISimPubService;
import nc.itf.fba_sim.pub.ISimPubStockInfoService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.trademarket.TradeMarketVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_sim.pub.SimINFOPubMethod;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class SimPubStockInfoImpl implements ISimPubStockInfoService {
	/**
	 * 取市值和流通盘
	 */
	@Override
	public Hashtable<String, UFDouble[]> getCaptialHT(String trade_date)
			throws Exception {
		String sql = "select a.pk_securities,capital,pass_num from sim_securities_b a inner join "
				+ "(select b.pk_securities,max(b.trade_date) as max_date from sim_securities_b b "
				+ "where b.trade_date<='"
				+ trade_date
				+ "' group by b.pk_securities) c on a.trade_date=c.max_date "
				+ "and a.pk_securities=c.pk_securities";
		Hashtable<String, UFDouble[]> returnHt = new Hashtable<String, UFDouble[]>();
		ISimPubService service = (ISimPubService) NCLocator.getInstance()
				.lookup(ISimPubService.class.getName());
		ArrayList<ArrayList<Object>> listArr = service
				.queryData(sql.toString());
		if (listArr == null || listArr.isEmpty())
			return returnHt;
		ArrayList<Object> rowArr;
		String pk_securities;
		Object captial;
		Object pass_num;
		for (int i = 0; i < listArr.size(); i++) {
			rowArr = listArr.get(i);
			if (rowArr == null || rowArr.isEmpty())
				continue;
			pk_securities = (String) rowArr.get(0);
			captial = rowArr.get(1);
			captial = captial == null ? 0 : captial;
			pass_num = rowArr.get(2);
			pass_num = pass_num == null ? 0 : pass_num;

			UFDouble capDoub = new UFDouble(captial.toString());
			UFDouble passDoub = new UFDouble(pass_num.toString());
			returnHt.put(pk_securities, new UFDouble[] { capDoub, passDoub });
		}
		return returnHt;
	}

	/**
	 * 行情增加联合索引
	 */

	@Override
	public UFDouble getStockClosePrice(String distill_date, String pk_securities)
			throws Exception {
		TradeMarketVO marketvo = null;
		if (pk_securities == null || distill_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select * from sim_trademarket where pk_securities = '"
				+ pk_securities + "' ");
		sf.append(" and trade_date =  ");
		sf.append(" (select isnull(max(trade_date),'"
				+ CostConstant.DEFAULT_DATE + "')  from sim_trademarket where ");
		sf.append(" isnull(dr, 0) = 0 and trade_date <= '" + distill_date
				+ " 59:59:59' ");
		sf.append(" and pk_securities = '" + pk_securities + "') ");
		sf.append(" and isnull(dr, 0) = 0 ");
		List<TradeMarketVO> list = (List<TradeMarketVO>) new BaseDAO()
				.executeQuery(sf.toString(), new BeanListProcessor(
						TradeMarketVO.class));
		if (list != null && list.size() > 0)
			marketvo = list.get(0);
		if (marketvo == null)
			throw new BusinessException("证券["
					+ querySecuritesName(pk_securities) + "],日期:"
					+ distill_date + "收盘价为空 !");
		return marketvo.getClose_price();
	}

	/**
	 * 查询证券档案名称
	 */
	@Override
	public String querySecuritesName(String pk_securities)
			throws BusinessException {
		String name = null;
		StringBuffer sf = new StringBuffer();
		sf.append("select name from  sec_securities where pk_securities = '"
				+ pk_securities + "' ");
		try {
			name = (String) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							String name = null;
							if (rs.next()) {
								name = rs.getString("name");
							}
							return name;
						}
					});
		} catch (DAOException e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return name;
	}

	@Override
	public void removeCaptialHt(UFDate trade_date) throws Exception {
		SimINFOPubMethod.getInstance().removeCaptialHt(null);
	}
}
