package nc.impl.fba_sabb.costplugin.gpzyshg;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.fba_sabb.costplugin.AbstractImpawnRepo;
import nc.itf.fba_sabb.calc.IMrlxMaintain;
import nc.itf.fba_sabb.constant.SabbModuleConst;
import nc.itf.fba_sabb.trade.IGpzyshgMaintain;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.tool.fba_sabb.pub.DateTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_sabb.calc.mrlx.AggDailyInterestVO;
import nc.vo.fba_sabb.calc.mrlx.DailyInterestVO;
import nc.vo.fba_sabb.trade.gpzyshg.AggImpawnRepoVO;
import nc.vo.fba_sabb.trade.gpzyshg.ImpawnRepoVO;
import nc.vo.fba_scost.cost.pub.QuerySchemeUtil;
import nc.vo.fba_sec.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 股票质押式购回插件
 * 
 * @author Administrator
 * 
 */
public class ImpawnRepo extends AbstractImpawnRepo {

	@Override
	protected void calculateWhenCheck(ICostingTool costingtool,
			ImpawnRepoVO tradevo) throws Exception {
		/**
		 * 股票质押式回购；交易类型：股票质押式回购初始交易，股票质押式回购到期购回；不能为空字段：初始交易金额、购回金额；
		 * 
		 * @author lihbj
		 */
		if (SabbModuleConst.BILLTYPE_IMPAWNREPO_NORMAL.equals(tradevo
				.getTranstypecode())
				|| SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE
						.equals(tradevo.getTranstypecode())) {
			if (tradevo.getEntrust_sum() == null || tradevo.getBb_sum() == null) {
				throw new Exception("审核日期：" + costingtool.getCurrdate()
						+ " 交易类型：股票质押式回购初始交易，股票质押式回购到期购回；不能为空字段：初始交易金额、购回金额；");
			} else if (tradevo.getEntrust_sum().compareTo(UFDouble.ZERO_DBL) == 0
					|| tradevo.getBb_sum().compareTo(UFDouble.ZERO_DBL) == 0) {
				throw new Exception("审核日期：" + costingtool.getCurrdate()
						+ " 交易类型：股票质押式回购初始交易，股票质押式回购到期购回；不能为空字段：初始交易金额、购回金额；");
			}
		}

		// 季度结息 或者 回购到期
		/*
		 * if (SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE.equals(tradevo.
		 * getTranstypecode()) ||
		 * SabbModuleConst.BILLTYPE_IMPAWNREPO_EXT.equals(
		 * tradevo.getTranstypecode())) { UFDouble interest =
		 * sumImpInterest(costingtool, tradevo); UFDouble jdjx =
		 * sumJdjx(costingtool, tradevo); //相减
		 * tradevo.setAcc_interest(SafeCompute.sub(interest, jdjx)); }
		 */
		/**
		 * update by lihaibo 季度结息逻辑： 累计
		 */
		// 季度结息
		if (SabbModuleConst.BILLTYPE_IMPAWNREPO_EXT.equals(tradevo
				.getTranstypecode())) {
			UFDouble interest = sumImpInterest(costingtool, tradevo);
			UFDouble jdjx = sumJdjx(costingtool, tradevo);
			UFDouble ce = sumCe(costingtool, tradevo);
			UFDouble bl = UFDouble.ZERO_DBL;
			UFDouble csjj = sumCsjj(costingtool, tradevo);
			UFDouble dqgh = sumDqgh(costingtool, tradevo);
			UFDouble entrust_sum = tradevo.getEntrust_sum() == null ? UFDouble.ZERO_DBL
					: tradevo.getEntrust_sum();
			/*
			 * //计算比例 if (csjj.toDouble() != 0 && entrust_sum.toDouble() != 0) {
			 * bl = entrust_sum.div(csjj.sub(dqgh)); } // 相减*比例
			 * tradevo.setAcc_interest(SafeCompute.sub(interest,
			 * jdjx).sub(ce).multiply(bl));
			 */
			// 不用乘比例
			tradevo.setAcc_interest(SafeCompute.sub(interest, jdjx).sub(ce));
			// 已计提利息-收到利息
			tradevo.setVdef4(SafeCompute.sub(tradevo.getAcc_interest(),
					tradevo.getSettleinterest()).toString());
		}
		// 回购到期
		if (SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE.equals(tradevo
				.getTranstypecode())) {
			UFDouble interest = sumImpInterest(costingtool, tradevo);
			UFDouble jdjx = sumJdjx(costingtool, tradevo);
			UFDouble csjj = sumCsjj(costingtool, tradevo);
			UFDouble dqgh = sumDqgh(costingtool, tradevo);
			UFDouble ce = sumCe(costingtool, tradevo);
			UFDouble bb_sum = tradevo.getBb_sum() == null ? UFDouble.ZERO_DBL
					: tradevo.getBb_sum();
			// 本次购回金额
			UFDouble entrust_sum = tradevo.getEntrust_sum() == null ? UFDouble.ZERO_DBL
					: tradevo.getEntrust_sum();
			UFDouble bl = UFDouble.ZERO_DBL;
			// 若本次购回金额小于融资余额，就属于部分购回
			if (entrust_sum.compareTo(csjj.sub(dqgh)) < 0) {
				// 把已计提利息设置为0
				tradevo.setAcc_interest(UFDouble.ZERO_DBL);
			} else {
				// 计算比例
				if (csjj.toDouble() != 0 && bb_sum.toDouble() != 0) {
					bl = bb_sum.div(csjj.sub(dqgh));
				}
				// 相减*比例
				tradevo.setAcc_interest((SafeCompute.sub(interest, jdjx)
						.sub(ce)).multiply(bl));
			}
			// 已计提利息-收到利息
			tradevo.setVdef4(SafeCompute.sub(tradevo.getAcc_interest(),
					tradevo.getSettleinterest()).toString());
		}

	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			ImpawnRepoVO tradevo) throws Exception {

		// 季度结息 或者 回购到期
		if (SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE.equals(tradevo
				.getTranstypecode())
				|| SabbModuleConst.BILLTYPE_IMPAWNREPO_EXT.equals(tradevo
						.getTranstypecode())) {
			tradevo.setAcc_interest(null);
			tradevo.setVdef4(null);
		}

	}

	private UFDouble sumJdjx(ICostingTool costingtool, ImpawnRepoVO tradevo) {

		UFDouble result = UFDouble.ZERO_DBL;
		// 合同号
		String contractNO = tradevo.getContract_id();
		// 审核日期
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// 当前组织
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		// update by lihaibo 不计算当天数据
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		// 季度结算利息 和 回购到期
		qrySql.append(" and transtypecode in ('")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_EXT).append("','")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE)
				.append("')");
		// 考虑封存情况
		qrySql.append(" and state !='").append(SystemConst.statecode[2])
				.append("'");
		qrySql.append(" and pk_org='").append(pk_org).append("'");
		qrySql.append(" and isnull(dr,0)=0");

		IGpzyshgMaintain ydsgh = NCLocator.getInstance().lookup(
				IGpzyshgMaintain.class);

		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SabbModuleConst.Entity_ImpawnRepo, qrySql.toString());
		try {
			AggImpawnRepoVO[] aggvos = ydsgh.queryBillByPK(ydsgh
					.queryPKs(queryscheme));
			if (aggvos != null) {
				for (int i = 0; i < aggvos.length; i++) {
					ImpawnRepoVO impawnrepo = (ImpawnRepoVO) aggvos[i]
							.getParent();
					result = SafeCompute.add(result,
							impawnrepo.getSettleinterest());
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException("汇总季度结息失败，请查询日志。");
		}
		return result;

	}

	private UFDouble sumImpInterest(ICostingTool costingtool,
			ImpawnRepoVO tradevo) {

		UFDouble result = UFDouble.ZERO_DBL;
		// 合同号
		String contractNO = tradevo.getContract_id();
		// 审核日期
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// 当前组织
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		if (contractNO == null || enddate == null || pk_org == null) {
			return result;
		}

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		// update by lihaibo 算已计提利息时不计算当天的每日利息数据
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_MRJX).append("'");
		qrySql.append(" and pk_org='").append(pk_org).append("'");
		qrySql.append(" and state !='").append(SystemConst.statecode[2])
				.append("'");
		qrySql.append(" and isnull(dr,0)=0");
		// 交易日期 < 审核日期

		IMrlxMaintain mrlx = NCLocator.getInstance()
				.lookup(IMrlxMaintain.class);
		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SabbModuleConst.Entity_DailyInterest, qrySql.toString());
		try {
			AggDailyInterestVO[] aggvos = mrlx.queryBillByPK(mrlx
					.queryPKs(queryscheme));
			if (aggvos != null) {
				for (int i = 0; i < aggvos.length; i++) {
					DailyInterestVO dailyInterestVO = (DailyInterestVO) aggvos[i]
							.getParent();

					result = SafeCompute.add(result,
							dailyInterestVO.getBrjtlx());
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException("汇总每日利息失败，请查询日志。");
		}
		return result;
	}

	/**
	 * 取初始交易总额 add by lihaibo
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	private UFDouble sumCsjj(ICostingTool costingtool, ImpawnRepoVO tradevo) {

		UFDouble result = UFDouble.ZERO_DBL;
		// 合同号
		String contractNO = tradevo.getContract_id();
		// 审核日期
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// 当前组织
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		qrySql.append(" and trade_date <='").append(DateTool.asEnd(enddate))
				.append("'");
		// 初始交易类型
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_NORMAL).append("'");
		// 考虑封存情况
		qrySql.append(" and state !='").append(SystemConst.statecode[2])
				.append("'");
		qrySql.append(" and pk_org='").append(pk_org).append("'");
		qrySql.append(" and isnull(dr,0)=0");

		IGpzyshgMaintain ydsgh = NCLocator.getInstance().lookup(
				IGpzyshgMaintain.class);

		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SabbModuleConst.Entity_ImpawnRepo, qrySql.toString());
		try {
			AggImpawnRepoVO[] aggvos = ydsgh.queryBillByPK(ydsgh
					.queryPKs(queryscheme));
			if (aggvos != null) {
				for (int i = 0; i < aggvos.length; i++) {
					ImpawnRepoVO impawnrepo = (ImpawnRepoVO) aggvos[i]
							.getParent();
					result = SafeCompute.add(result,
							impawnrepo.getEntrust_sum());
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException("汇总委托金额失败，请查询日志。");
		}
		return result;

	}

	/**
	 * 取到期购回总额 截止到前一天 add by lihaibo
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	private UFDouble sumDqgh(ICostingTool costingtool, ImpawnRepoVO tradevo) {

		UFDouble result = UFDouble.ZERO_DBL;
		// 合同号
		String contractNO = tradevo.getContract_id();
		// 审核日期
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// 当前组织
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		// 截止到前一天
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		// 到期购回类型
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE)
				.append("'");
		// 考虑封存情况
		qrySql.append(" and state !='").append(SystemConst.statecode[2])
				.append("'");
		qrySql.append(" and pk_org='").append(pk_org).append("'");
		qrySql.append(" and isnull(dr,0)=0");

		IGpzyshgMaintain ydsgh = NCLocator.getInstance().lookup(
				IGpzyshgMaintain.class);

		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SabbModuleConst.Entity_ImpawnRepo, qrySql.toString());
		try {
			AggImpawnRepoVO[] aggvos = ydsgh.queryBillByPK(ydsgh
					.queryPKs(queryscheme));
			if (aggvos != null) {
				for (int i = 0; i < aggvos.length; i++) {
					ImpawnRepoVO impawnrepo = (ImpawnRepoVO) aggvos[i]
							.getParent();
					result = SafeCompute.add(result,
							impawnrepo.getEntrust_sum());
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException("汇总期购回金额失败，请查询日志。");
		}
		return result;

	}

	/**
	 * 取差额汇总 add by lihaibo
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	private UFDouble sumCe(ICostingTool costingtool, ImpawnRepoVO tradevo) {

		UFDouble result = UFDouble.ZERO_DBL;
		// 合同号
		String contractNO = tradevo.getContract_id();
		// 审核日期
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// 当前组织
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		// update by lihaibo 不计算当天数据
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		// 考虑封存情况
		qrySql.append(" and state !='").append(SystemConst.statecode[2])
				.append("'");
		qrySql.append(" and pk_org='").append(pk_org).append("'");
		qrySql.append(" and isnull(dr,0)=0");

		IGpzyshgMaintain ydsgh = NCLocator.getInstance().lookup(
				IGpzyshgMaintain.class);

		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SabbModuleConst.Entity_ImpawnRepo, qrySql.toString());
		try {
			AggImpawnRepoVO[] aggvos = ydsgh.queryBillByPK(ydsgh
					.queryPKs(queryscheme));
			if (aggvos != null) {
				for (int i = 0; i < aggvos.length; i++) {
					ImpawnRepoVO impawnrepo = (ImpawnRepoVO) aggvos[i]
							.getParent();
					result = SafeCompute.add(result,
							impawnrepo.getVdef4() == null ? UFDouble.ZERO_DBL
									: new UFDouble(impawnrepo.getVdef4()));
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException("取差额汇总 失败，请查询日志。");
		}
		return result;

	}
}
