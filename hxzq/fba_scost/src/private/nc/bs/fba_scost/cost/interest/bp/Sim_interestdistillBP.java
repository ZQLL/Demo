package nc.bs.fba_scost.cost.interest.bp;

import java.util.ArrayList;
import java.util.List;

import nc.bs.bd.cache.CacheProxy;
import nc.bs.fba_scost.cost.interest.pub.CalcInterestByConverse;
import nc.bs.fba_scost.cost.interest.pub.QueryInterestBaseInfo;
import nc.impl.pubapp.pattern.data.vo.SchemeVOQuery;
import nc.itf.fba_scost.cost.pub.DistillSendVoucher;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.interestdistill.AggInterestDist;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.uif2.LoginContext;

public class Sim_interestdistillBP {
	/**
	 * 查询
	 */
	public InterestdistillVO[] queryByQueryScheme(IQueryScheme querySheme) {
		QuerySchemeProcessor p = new QuerySchemeProcessor(querySheme);
		p.appendFuncPermissionOrgSql();
		return new SchemeVOQuery<InterestdistillVO>(InterestdistillVO.class)
				.query(querySheme, null);
	}

	/**
	 * 应收利息计提
	 */
	public InterestdistillVO[] distill(LoginContext context, UFDate trade_date)
			throws BusinessException {
		// 校验
		InterestDistillCheck check = new InterestDistillCheck();
		check.checkJt(context, trade_date);
		// 查询成本计算方案
		QueryInterestBaseInfo base = new QueryInterestBaseInfo();
		CostPlanVO[] planvos = base.getCostPlanInfo(context);
		if (planvos == null || planvos.length == 0)
			return null;
		List<InterestdistillVO> lists = new ArrayList<InterestdistillVO>();
		CalcInterestByConverse conver = new CalcInterestByConverse();
		// start 20150612 性能优化：计提利息之前更新一次缓存 by:mx
		CacheProxy.fireDataUpdated("SIM_INTERESTDISTILL");
		CacheProxy.fireDataUpdated("SIM_RATEPERIOD");
		CacheProxy.fireDataUpdated("SIM_INTEREST");
		CacheProxy.fireDataUpdated("SIM_TRADEMARKET");
		// end 20150612 性能优化：计提利息之前更新一次缓存 by:mx
		for (CostPlanVO costplanvo : planvos) {
			List<InterestdistillVO> list = conver.calcInterest(costplanvo,
					context, trade_date);
			if (list != null && list.size() > 0)
				lists.addAll(list);
		}

		return lists.toArray(new InterestdistillVO[0]);
	}

	/**
	 * 取消计提
	 */
	public void unDistill(LoginContext context, UFDate trade_date)
			throws BusinessException {
		try {
			// 校验
			InterestDistillCheck check = new InterestDistillCheck();
			check.unCheckJt(context, trade_date);
			// 查询生成实时凭证的单据
			QueryInterestBaseInfo fv = new QueryInterestBaseInfo();
			AggInterestDist[] interestdata = fv.queryDelVoucherData(context,
					trade_date);
			// 清空凭证数据
			if (interestdata != null && interestdata.length > 0)
				new DistillSendVoucher().deleteRTVoucher(interestdata);
			// 清空数据
			fv.clearData(context, trade_date);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 记账
	 */
	public void tally(LoginContext context) throws BusinessException {
		try {
			// 查询未生成实时凭证的单据
			QueryInterestBaseInfo fv = new QueryInterestBaseInfo();
			AggInterestDist[] fairdata = fv.querytallVoucherData(context);
			if (fairdata != null && fairdata.length > 0) {
				new DistillSendVoucher().addRTVoucher(fairdata);
				// 更改单据状态
				fv.updatetallVoucherData(fairdata);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
}
