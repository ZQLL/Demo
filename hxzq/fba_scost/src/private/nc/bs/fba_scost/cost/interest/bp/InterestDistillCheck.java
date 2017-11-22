package nc.bs.fba_scost.cost.interest.bp;

import nc.bs.fba_scost.cost.interest.pub.QueryInterestBaseInfo;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

/**
 * 债券应收利息计提 校验类
 * 
 */
public class InterestDistillCheck {
	/**
	 * 计提校验
	 */
	public void checkJt(LoginContext context, UFDate trade_date)
			throws BusinessException {
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		// 1、判断是否记账
		String pk_orgbook = AppContextUtil.getDefaultOrgBook();
		UFDate checkdate = info.queryInitTallyVO(context, pk_orgbook,
				CostConstant.COSTGROUP);
		if (checkdate == null)
			throw new BusinessException("期初没有记账,审核日期为空！");
		// 2、计提日期必须在审核日期之前
		if (trade_date.after(checkdate.asEnd()))
			throw new BusinessException("日期:" + trade_date.toLocalString()
					+ "，没有审核，不能计提应收利息");
		// 3、判断当前日期是否已经计提
		UFDate maxjtdate = info.getMaxjtDate(context);
		if (trade_date.isSameDate(maxjtdate)
				|| trade_date.before(maxjtdate.asBegin()))
			throw new BusinessException("日期:" + trade_date.toLocalString()
					+ ",已经计提应收利息");
	}

	/**
	 * 取消计提校验
	 */
	public void unCheckJt(LoginContext context, UFDate trade_date)
			throws BusinessException {
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		// 1、判断是否记账
		String pk_orgbook = AppContextUtil.getDefaultOrgBook();
		UFDate checkdate = info.queryInitTallyVO(context, pk_orgbook,
				CostConstant.COSTGROUP);
		if (checkdate == null)
			throw new BusinessException("期初没有记账,审核日期为空！");
		// 2、计提日期必须在审核日期之前
		if (trade_date.after(checkdate.asEnd()))
			throw new BusinessException("日期:" + trade_date.toLocalString()
					+ "，没有审核，不能取消应收利息计提");
		// 3、判断当前日期是否已经计提
		UFDate maxjtdate = info.getMaxjtDate(context);
		if (trade_date.after(maxjtdate.asEnd()))
			throw new BusinessException("日期:" + trade_date.toLocalString()
					+ "，还没有计提，不能取消应收利息计提");
		// 4、判断计提日内的应收利息是否生成凭证
		boolean falg = info.isGenInterestVoucher(context, trade_date);
		if (falg)
			throw new BusinessException("日期:" + trade_date.toLocalString()
					+ "，已经生成会计凭证，不能取消应收利息");
	}

	/**
	 * 弃审校验
	 */
	public void uncheckBill(String pk_group, String pk_org,
			String pk_glorgbook, UFDate trade_date) throws BusinessException {
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		LoginContext context = new LoginContext();
		context.setPk_group(pk_group);
		context.setPk_org(pk_org);
		UFDate maxjtdate = info.getMaxjtDate(context);
		if (trade_date.isSameDate(maxjtdate)
				|| trade_date.before(maxjtdate.asBegin()))
			throw new BusinessException("大于等于日期:" + trade_date.toLocalString()
					+ ",已经计提应收利息,请先取消！");
	}
}
