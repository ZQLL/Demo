package nc.bs.fba_scost.cost.interest.pub;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.dbcache.DBCacheFacade;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.ForwardVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_scost.pub.tools.CostTools;
import nc.vo.fba_sec.secbd.securities.SecuritiesVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;
import nc.vo.uif2.LoginContext;

public class scostZrlxUtils {
	public UFDouble forwardInterestDistill(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, TransformtradeVO vo)
			throws BusinessException {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean falg = this.getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_INTERESTFORWARD);
		UFDouble lx = null;
		if (falg) {

			lx = this.forwardInterestDistill1(costingtool, stockbalancevo, vo);
		}
		return lx;
	}

	public boolean getBooleanFromInitcode(String pk_glorgbook, String initcode)
			throws BusinessException {
		boolean falg = false;
		String va = null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select value from pub_sysinit where pk_org  = '"
				+ pk_glorgbook + "' " + " and initcode = '" + initcode
				+ "' and isnull(dr,0) = 0 ");
		try {
			va = (String) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							String name = null;
							if (rs.next()) {
								name = rs.getString("value");
							}
							return name;
						}
					});
		} catch (DAOException e) {
			throw ExceptionHandler.handleException(super.getClass(), e);
		}
		if (va == null)
			throw ExceptionHandler.createException("当前组织下，参数:" + initcode
					+ "，没有设置! ");
		if ("Y".equals(va))
			falg = true;
		return falg;
	}

	/**
	 * 根据证券档案ID查询是否债券
	 */
	public boolean isSecuritiesAcc(String pk_securities)
			throws BusinessException {
		SecuritiesVO vo = querySecuritiesVO(pk_securities);
		boolean falg = false;
		if (vo != null)
			falg = vo.getIf_accrual() == null ? false : vo.getIf_accrual()
					.booleanValue();
		return falg;
	}

	/**
	 * 根据证券档案ID查询证券档案
	 */
	public SecuritiesVO querySecuritiesVO(String pk_securities)
			throws BusinessException {
		// start 20150612 性能优化 by:mx
		String sql = "Select * from sec_securities where pk_securities = ?";
		SQLParameter param = new SQLParameter();
		param.addParam(pk_securities);
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 ADD START */
		Object obj = DBCacheFacade.runQuery(sql, param, new BeanListProcessor(
				SecuritiesVO.class));
		List<SecuritiesVO> voo = (List<SecuritiesVO>) obj;
		if (null == voo || voo.size() == 0) {
			return new SecuritiesVO();
		}
		return voo.get(0);
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 ADD END */
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 DEL START */
		// SecuritiesVO vo = (SecuritiesVO)DBCacheFacade.runQuery(sql,param,new
		// BeanListProcessor(SecuritiesVO.class));
		// end 20150612 by:mx
		// SecuritiesVO vo = (SecuritiesVO)new
		// HYSuperDMO().queryByPrimaryKey(SecuritiesVO.class,pk_securities);
		// return vo;
		/** JINGQT 2015年7月3日 性能优化产生的BUG修正 DEL END */

	}

	/**
	 * 加权平均运算 运算规则：num1/num2 = mny1 / ? //\\ ? = num2 * mny1 / num1 scale
	 * 代表保留精度位数
	 * 
	 */
	public static UFDouble averageMny1(UFDouble num1, UFDouble num2,
			UFDouble mny1, int scale) {
		UFDouble sum = SafeCompute.multiply(mny1, num2);
		UFDouble mny2 = SafeCompute.div(sum, num1);
		mny2 = mny2.setScale(scale, UFDouble.ROUND_HALF_UP);
		return mny2;
	}

	public UFDouble forwardInterestDistill1(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, TransformtradeVO vo)
			throws BusinessException {

		String pk_securities = vo.getPk_securities2();
		boolean falg = this.isSecuritiesAcc(pk_securities);
		if (!(falg)) {
			return null;
		}
		ForwardVO fvo = this.forwardVOinfo1(costingtool, stockbalancevo, vo);

		if (fvo == null)
			return null;
		UFDouble lx = scostZrlxUtils.averageMny1(new UFDouble(1), new UFDouble(
				1), fvo.getInterest(), 8);

		lx = lx.setScale(2, 4);
		fvo.setInterest(SafeCompute.sub(fvo.getInterest(), lx));

		return lx;
	}

	public ForwardVO forwardVOinfo1(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, TransformtradeVO vo)
			throws BusinessException {
		String key = this.getBuildKey(costingtool, vo);
		String date = vo.getTrade_date().toLocalString();
		String key1 = key + "," + date;
		// ForwardVO fvo = (ForwardVO) costingtool.getInterestjz().get(key1);
		ForwardVO fvo = new ForwardVO();
		// if (fvo == null) {
		UFDouble interst = null;
		UFDouble stocknum = null;
		stocknum = stockbalancevo.getStocks_num();
		interst = queryInterestDate(costingtool, vo);
		fvo = new ForwardVO();
		fvo.setTrade_date(new UFDate(date));
		fvo.setInterest(interst);
		fvo.setStock_num(stocknum);
		costingtool.getInterestjz().put(key1, fvo);
		// }
		return fvo;
	}

	public String getBuildKey(ICostingTool costingtool, ITrade_Data vo) {
		CostPlanVO costplanvo = costingtool.getCostParaVO().getCostplanvo();
		String key = CostTools.getCombinesKey(vo,
				costplanvo.getCostFieldArray());
		return key;
	}

	public UFDouble queryInterestDate(ICostingTool costingtool,
			TransformtradeVO vo) throws BusinessException {

		UFDate date = vo.getTrade_date();
		CostPlanVO costplanvo = costingtool.getCostParaVO().getCostplanvo();
		LoginContext context = buildContext(costingtool);
		UFDouble interest = this.calcQcInterest(vo, costplanvo, context, date);
		return interest;
	}

	/**
	 * 计算单支债券的期初应收利息
	 */
	public UFDouble calcQcInterest(TransformtradeVO stvoz,
			CostPlanVO costplanvo, LoginContext context, UFDate trade_date)
			throws BusinessException {

		// String pk_securities = stvoz.getPk_securities();
		// UFDate firstdate =
		// info.queryFirstFxDate(pk_securities,trade_date);//查询当前债券的起息日期，考虑牌价倒闸，不需要考虑利率设置档案，上述期初应收利息可以做一次性查询，考虑效率问题。
		UFDate firstdate = new UFDate(CostConstant.DEFAULT_DATE);
		InterestdistillVO[] distillvos = this.queryQcInterest(firstdate,
				costplanvo, context, trade_date, stvoz);// wangshzh,在当天有还款，体现到第二天
		UFDouble zu = this.calcQcInterest(distillvos);// 应收利息
		return zu;
	}

	/**
	 * 查询本期期初应收利息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InterestdistillVO[] queryQcInterest(UFDate firstdate,
			CostPlanVO costplanvo, LoginContext context, UFDate trade_date,
			TransformtradeVO stvo) throws BusinessException {
		if (firstdate == null || costplanvo == null || context == null
				|| trade_date == null)
			return null;
		StringBuffer sf = new StringBuffer();
		// start 20150612 性能优化 by:mx
		sf.append("select * from sim_interestdistill where ");
		// end 20150612 性能优化 by:mx
		sf.append(" distill_date >= '" + firstdate.asBegin()
				+ "' and distill_date <= '" + trade_date.asEnd() + "' ");
		sf.append(" and pk_costplan  = '" + costplanvo.getPk_costplan() + "' ");
		// sf.append(" and genstate = (1,2,3) ");//生成状态全部都算
		sf.append(" and isnull(dr,0) =  0 ");
		sf.append(" and pk_org = '" + context.getPk_org() + "' ");
		sf.append(" and pk_group = '" + context.getPk_group() + "' ");
		sf.append(" and pk_assetsprop = '" + stvo.getHr_pk_assetsprop() + "' ");
		sf.append(" and pk_stocksort = '" + stvo.getPk_stocksort() + "'   ");
		sf.append(" and pk_securities = '" + stvo.getPk_securities2() + "'  ");
		sf.append(" and pk_glorgbook = '" + AppContextUtil.getDefaultOrgBook()
				+ "'  ");

		if (costplanvo.getIf_glorgbook() != null
				&& costplanvo.getIf_glorgbook().booleanValue()) {
			sf.append(" and  pk_glorgbook = '" + stvo.getPk_glorgbook() + "' ");
		}
		if (costplanvo.getIf_capaccount() != null
				&& costplanvo.getIf_capaccount().booleanValue()) {
			sf.append(" and  pk_capaccount = '" + stvo.getHr_pk_capaccount()
					+ "' ");
		}
		if (costplanvo.getIf_selfsgroup() != null
				&& costplanvo.getIf_selfsgroup().booleanValue()) {
			sf.append(" and  pk_selfsgroup =  '" + stvo.getPk_selfsgroup()
					+ "'  ");
		}
		if (costplanvo.getIf_partnaccount() != null
				&& costplanvo.getIf_partnaccount().booleanValue()) {
			sf.append(" and  pk_partnaccount =  '" + stvo.getPk_partnaccount()
					+ "'  ");
		}
		if (costplanvo.getIf_operatesite() != null
				&& costplanvo.getIf_operatesite().booleanValue()) {
			sf.append(" and  pk_operatesite =  '" + stvo.getPk_operatesite()
					+ "'  ");
		}
		if (costplanvo.getIf_client() != null
				&& costplanvo.getIf_client().booleanValue()) {
			sf.append(" and  pk_client =  '" + stvo.getPk_client() + "'  ");
		}
		// TODO fulq 倒序排序
		sf.append("   order by distill_date desc  ");
		// start 20150612 性能优化 by:mx
		// Collection values = (Collection)DBCacheQueryFacade
		// .runQuery(sf.toString(), new BeanListProcessor(
		// InterestdistillVO.class));
		// SuperVO[] superVos = (SuperVO[]) values.toArray((SuperVO[])
		// Array.newInstance(InterestdistillVO.class, 0));
		// InterestdistillVO[] vos = (InterestdistillVO[])superVos;
		Collection values = (Collection) DBCacheFacade.runQuery(sf.toString(),
				new BeanListProcessor(InterestdistillVO.class));
		SuperVO[] superVos = (SuperVO[]) values.toArray((SuperVO[]) Array
				.newInstance(InterestdistillVO.class, 0));
		InterestdistillVO[] vos = (InterestdistillVO[]) superVos;
		// end 20150612 性能优化 by:mx

		// InterestdistillVO[] vos = (InterestdistillVO[])new HYSuperDMO().
		// queryByWhereClause(InterestdistillVO.class, sf.toString());
		return vos;
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

	public LoginContext buildContext(ICostingTool costingtool) {
		String pk_group = costingtool.getCostParaVO().getCheckParavo()
				.getPk_group();
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();
		LoginContext text = new LoginContext();
		text.setPk_group(pk_group);
		text.setPk_org(pk_org);
		return text;
	}

	public void saveInterestDistill(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, TransformtradeVO vs)
			throws BusinessException {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean falg = this.getBooleanFromInitcode(pk_glorg,
				CostConstant.PARAM_INTERESTFORWARD);
		if (falg) {

			this.saveInterestDistill1(costingtool, stockbalancevo, vs);
		}
	}

	public void saveInterestDistill1(ICostingTool costingtool,
			StockBalanceVO stockbalancevo, TransformtradeVO vo)
			throws BusinessException {
		// 校验
		if (check(vo))
			return;
		// 开始存数
		ScostTransformBulidDistillVOInfo info = new ScostTransformBulidDistillVOInfo();
		List<InterestdistillVO> list = new ArrayList<InterestdistillVO>();
		InterestdistillVO distillvo = info.buildInterestdistill(costingtool,
				stockbalancevo, vo);
		if ((distillvo != null) && (distillvo.getInterestdistill() != null)
				&& (distillvo.getInterestdistill().doubleValue() != 0.0D)) {
			list.add(distillvo);
		}
		if (list.size() > 0) {
			new BaseDAO().insertVOArray(list.toArray(new InterestdistillVO[0]));
		}
	}

	public boolean check(ITrade_Data vo) throws BusinessException {
		boolean falg = true;
		String pk_securities = vo.getPk_securities();

		boolean iszq = this.isSecuritiesAcc(pk_securities);
		if (iszq)
			falg = false;
		return falg;
	}
}
