package nc.bs.fba_scost.cost.interest.bp;

import nc.bs.fba_scost.cost.interest.pub.QueryInterestBaseInfo;
import nc.vo.fba_scost.cost.pub.AppContextUtil;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uif2.LoginContext;

/**
 * ծȯӦ����Ϣ���� У����
 * 
 */
public class InterestDistillCheck {
	/**
	 * ����У��
	 */
	public void checkJt(LoginContext context, UFDate trade_date)
			throws BusinessException {
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		// 1���ж��Ƿ����
		String pk_orgbook = AppContextUtil.getDefaultOrgBook();
		UFDate checkdate = info.queryInitTallyVO(context, pk_orgbook,
				CostConstant.COSTGROUP);
		if (checkdate == null)
			throw new BusinessException("�ڳ�û�м���,�������Ϊ�գ�");
		// 2���������ڱ������������֮ǰ
		if (trade_date.after(checkdate.asEnd()))
			throw new BusinessException("����:" + trade_date.toLocalString()
					+ "��û����ˣ����ܼ���Ӧ����Ϣ");
		// 3���жϵ�ǰ�����Ƿ��Ѿ�����
		UFDate maxjtdate = info.getMaxjtDate(context);
		if (trade_date.isSameDate(maxjtdate)
				|| trade_date.before(maxjtdate.asBegin()))
			throw new BusinessException("����:" + trade_date.toLocalString()
					+ ",�Ѿ�����Ӧ����Ϣ");
	}

	/**
	 * ȡ������У��
	 */
	public void unCheckJt(LoginContext context, UFDate trade_date)
			throws BusinessException {
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		// 1���ж��Ƿ����
		String pk_orgbook = AppContextUtil.getDefaultOrgBook();
		UFDate checkdate = info.queryInitTallyVO(context, pk_orgbook,
				CostConstant.COSTGROUP);
		if (checkdate == null)
			throw new BusinessException("�ڳ�û�м���,�������Ϊ�գ�");
		// 2���������ڱ������������֮ǰ
		if (trade_date.after(checkdate.asEnd()))
			throw new BusinessException("����:" + trade_date.toLocalString()
					+ "��û����ˣ�����ȡ��Ӧ����Ϣ����");
		// 3���жϵ�ǰ�����Ƿ��Ѿ�����
		UFDate maxjtdate = info.getMaxjtDate(context);
		if (trade_date.after(maxjtdate.asEnd()))
			throw new BusinessException("����:" + trade_date.toLocalString()
					+ "����û�м��ᣬ����ȡ��Ӧ����Ϣ����");
		// 4���жϼ������ڵ�Ӧ����Ϣ�Ƿ�����ƾ֤
		boolean falg = info.isGenInterestVoucher(context, trade_date);
		if (falg)
			throw new BusinessException("����:" + trade_date.toLocalString()
					+ "���Ѿ����ɻ��ƾ֤������ȡ��Ӧ����Ϣ");
	}

	/**
	 * ����У��
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
			throw new BusinessException("���ڵ�������:" + trade_date.toLocalString()
					+ ",�Ѿ�����Ӧ����Ϣ,����ȡ����");
	}
}
