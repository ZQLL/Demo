package nc.impl.fba_smt.report;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.freereport.ReportConst;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_smt.trade.margintrade.MarginTradeVO;
import nc.vo.fba_smt.trade.refinancing.RefinancingVO;
import nc.vo.fba_smt.trade.transferrecord.TransferRecordVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b>这个的历史：由于米雪将这三个方法写在了Scost中，导致依赖错误，故这里创建了新类来容纳这三个方法</b><br>
 * 但是为什么不写在{@link nc.pub.report.SMTReportUtil}中，因为SMTReportUtil在pub中去查询DB了。<br>
 * <i>方法作者：米雪</i>
 * 
 * @author 景群涛
 * @since 2016年5月5日10:21:59
 * @version 1.0.1
 */
public class SmtReportQueryUtil {

	/**
	 * 查询本期可融转可供和可供转可融数据
	 * 
	 * @param condition
	 * @param startDate
	 * @param endDate
	 * @param costplanvo
	 * @param transtypecode
	 * @return
	 * @throws BusinessException
	 */
	public List<SuperVO> getTransferData(String condition, String startDate, String endDate, CostPlanVO costplanvo,
			String transtypecode) throws BusinessException {
		StringBuffer cons = new StringBuffer();
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer manfiled = new StringBuffer();
		String[] manItemCodes = null;
		String[] groupfield = null;
		String stockCondition = null;
		// 可供转可融 红股券源划转 红股可供转可融 券源划转划入可融库 可融库面值金额增加
		if (transtypecode.equals(ReportConst.KGZKR) || transtypecode.equals(ReportConst.HGQYHZ)
				|| transtypecode.equals(ReportConst.HGKGZKR)) {
			manItemCodes = new String[] { "a.hr_pk_assetsprop ", "a.hr_pk_stocksort pk_stocksort ", "a.hr_pk_capaccount  " };
			groupfield = new String[] { "a.hr_pk_assetsprop ", "a.hr_pk_stocksort ", "a.hr_pk_capaccount" };
			stockCondition = "and a.hr_pk_stocksort= '" + ReportConst.STOCKKR + "'";// 划入库存组织为可融库
		} else {// 可融转可供 || 券源划转可融库划出单 可融库面值减少
			manItemCodes = new String[] { "a.hc_pk_assetsprop ", "a.hc_pk_stocksort pk_stocksort ", "a.hc_pk_capaccount  " };
			groupfield = new String[] { "a.hc_pk_assetsprop ", "a.hc_pk_stocksort ", "a.hc_pk_capaccount" };
			stockCondition = "and a.hc_pk_stocksort= '" + ReportConst.STOCKKR + "'";// 划出库存组织为可融库
		}
		if (transtypecode.equals("QYHZHR")) {// 券源划转划入可融库单据
			transtypecode = ReportConst.QYHZ;
			manItemCodes = new String[] { "a.hr_pk_assetsprop ", "a.hr_pk_stocksort pk_stocksort ", "a.hr_pk_capaccount  " };
			groupfield = new String[] { "a.hr_pk_assetsprop ", "a.hr_pk_stocksort ", "a.hr_pk_capaccount" };
			stockCondition = "and a.hr_pk_stocksort= '" + ReportConst.STOCKKR + "'";// 划入库存组织为可融库
		} else if (transtypecode.equals("QYHZHC")) {// 划出可融库单据
			transtypecode = ReportConst.QYHZ;
			manItemCodes = new String[] { "a.hc_pk_assetsprop ", "a.hc_pk_stocksort pk_stocksort ", "a.hc_pk_capaccount  " };
			groupfield = new String[] { "a.hc_pk_assetsprop ", "a.hc_pk_stocksort ", "a.hc_pk_capaccount" };
			stockCondition = "and a.hc_pk_stocksort= '" + ReportConst.STOCKKR + "'";// 划出库存组织为可融库
		}
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		for (int i = 0; i < groupfield.length; i++) {
			manfiled.append(groupfield[i]);
			if (i < manItemCodes.length - 1) {
				manfiled.append(",");
			}
		}

		cons.append("select sum(a.bargain_num) bargain_num,sum(a.bargain_sum) bargain_sum,a.pk_securities,");
		cons.append(" a.pk_group, a.pk_org, a.pk_glorgbook,");
		cons.append(manItemSb.toString());
		cons.append(" from smt_transferecord a ");
		cons.append("where isnull(dr,0)=0 and a.state>=2 ");
		cons.append("and a.transtypecode = '" + transtypecode + "' ");
		cons.append(stockCondition);
		cons.append("and a.pk_org = '" + costplanvo.getPk_org() + "'");
		cons.append("and a.pk_glorgbook = '" + costplanvo.getPk_glorgbook() + "'");
		cons.append("and a.trade_date >='" + startDate + "' ");
		cons.append("and a.trade_date <='" + endDate + "' ");
		cons.append("group by a.pk_securities,a.pk_org, a.pk_glorgbook, a.pk_group,");
		cons.append(manfiled.toString());
		List<SuperVO> list = (List<SuperVO>) new BaseDAO().executeQuery(cons.toString(), new BeanListProcessor(
				TransferRecordVO.class));
		return list;
	}

	/**
	 * 查询本期融出和融出归还数据
	 * 
	 * @param condition
	 * @param startDate
	 * @param endDate
	 * @param costplanvo
	 * @param transtypecode
	 * @return
	 * @throws BusinessException
	 */
	public List<SuperVO> getMarginData(String condition, String startDate, String endDate, CostPlanVO costplanvo,
			String transtypecode) throws BusinessException {
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String[] manItemCodes = new String[] { " pk_assetsprop", "pk_stocksort", "  pk_group", " pk_org", "   pk_glorgbook",
				"   pk_capaccount", "   pk_securities" };
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		sql.append("select sum(bargain_num) bargain_num,sum(bargain_sum) bargain_sum,");
		sql.append(manItemSb.toString());
		sql.append(" from smt_margintrade ");
		sql.append(" where isnull(dr,0)=0 and state>=2");
		sql.append(" and transtypecode = '" + transtypecode + "' ");
		sql.append(" and pk_org = '" + costplanvo.getPk_org() + "'");
		sql.append(" and pk_glorgbook = '" + costplanvo.getPk_glorgbook() + "'");
		sql.append(" and trade_date >='" + startDate + "' ");
		sql.append(" and trade_date <='" + endDate + "' ");
		sql.append(" group by ");
		sql.append(manItemSb.toString());

		List<SuperVO> list = (List<SuperVO>) new BaseDAO().executeQuery(sql.toString(),
				new BeanListProcessor(MarginTradeVO.class));
		return list;
	}

	/**
	 * 查询转融通借入和转融通归还
	 * 
	 * @param condition
	 * @param startDate
	 * @param endDate
	 * @param costplanvo
	 * @param transtypecode
	 * @return
	 * @throws BusinessException
	 */
	public List<SuperVO> getRefinancingData(String condition, String startDate, String endDate, CostPlanVO costplanvo,
			String transtypecode) throws BusinessException {
		StringBuffer manItemSb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String[] manItemCodes = new String[] { " pk_assetsprop", "pk_stocksort", "  pk_group", " pk_org", "   pk_glorgbook",
				"   pk_capaccount", "   pk_securities" };
		for (int i = 0; i < manItemCodes.length; i++) {
			manItemSb.append(manItemCodes[i]);
			if (i < manItemCodes.length - 1) {
				manItemSb.append(",");
			}
		}
		sql.append("select sum(bargain_num) bargain_num,sum(bargain_sum) bargain_sum,");
		sql.append(manItemSb.toString());
		sql.append(" from smt_refinancing ");
		sql.append(" where isnull(dr,0)=0 and state>=2");
		sql.append(" and transtypecode = '" + transtypecode + "' ");
		sql.append(" and pk_org = '" + costplanvo.getPk_org() + "'");
		sql.append(" and pk_glorgbook = '" + costplanvo.getPk_glorgbook() + "'");
		sql.append(" and trade_date >='" + startDate + "' ");
		sql.append(" and trade_date <='" + endDate + "' ");
		sql.append(" group by ");
		sql.append(manItemSb.toString());

		List<SuperVO> list = (List<SuperVO>) new BaseDAO().executeQuery(sql.toString(),
				new BeanListProcessor(RefinancingVO.class));
		return list;
	}

}
