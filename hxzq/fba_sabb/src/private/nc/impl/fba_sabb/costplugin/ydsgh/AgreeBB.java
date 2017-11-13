package nc.impl.fba_sabb.costplugin.ydsgh;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.fba_sabb.costplugin.AbstractAgreeBB;
import nc.itf.fba_sabb.calc.IMrlxMaintain;
import nc.itf.fba_sabb.constant.SabbModuleConst;
import nc.itf.fba_sabb.trade.IYdsghMaintain;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.tool.fba_sabb.pub.DateTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_sabb.calc.mrlx.AggDailyInterestVO;
import nc.vo.fba_sabb.calc.mrlx.DailyInterestVO;
import nc.vo.fba_sabb.trade.ydsgh.AggAgreedbbVO;
import nc.vo.fba_sabb.trade.ydsgh.AgreedbbVO;
import nc.vo.fba_scost.cost.pub.QuerySchemeUtil;
import nc.vo.fba_sec.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

/**
 * Լ��ʽ���ز��
 * 
 * @author Administrator
 * 
 */
public class AgreeBB extends AbstractAgreeBB {

	@Override
	protected void calculateWhenCheck(ICostingTool costingtool,
			AgreedbbVO tradevo) throws Exception {
		/**
		 * Լ��ʽ���أ��������ͣ�Լ����ʼ���ס�Լ�����ڹ��أ�����Ϊ���ֶΣ�ί�н����ؽ�
		 * 
		 * @author lihbj
		 */
		if (SabbModuleConst.BILLTYPE_AGREEDBBZC.equals(tradevo
				.getTranstypecode())
				|| SabbModuleConst.BILLTYPE_AGREEDBBTQGH.equals(tradevo
						.getTranstypecode())) {
			if (tradevo.getEntrust_sum() == null || tradevo.getBb_sum() == null) {
				throw new Exception("������ڣ�" + costingtool.getCurrdate()
						+ " �������ͣ�Լ����ʼ���ס�Լ�����ڹ��أ�����Ϊ���ֶΣ�ί�н����ؽ�");
			} else if (tradevo.getEntrust_sum().compareTo(UFDouble.ZERO_DBL) == 0
					|| tradevo.getBb_sum().compareTo(UFDouble.ZERO_DBL) == 0) {
				throw new Exception("������ڣ�" + costingtool.getCurrdate()
						+ " �������ͣ�Լ����ʼ���ס�Լ�����ڹ��أ�����Ϊ���ֶΣ�ί�н����ؽ�");
			}
		}

		if (SabbModuleConst.BILLTYPE_AGREEDBBTQGH.equals(tradevo
				.getTranstypecode())) {
			// ���ܹ�Ϣ
			UFDouble divs = sumDividend(costingtool, tradevo);
			// ���ܼ�����Ϣ
			UFDouble ints = sumAccInterest(costingtool, tradevo);
			tradevo.setBook_interest(divs);
			tradevo.setAcc_interest(ints);
		}

	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			AgreedbbVO tradevo) throws Exception {

		if (SabbModuleConst.BILLTYPE_AGREEDBBTQGH.equals(tradevo
				.getTranstypecode())) {
			tradevo.setBook_interest(null);
			tradevo.setAcc_interest(null);
		}

	}

	/**
	 * ������Ϣ
	 * 
	 * @param costingtool
	 * @return
	 */
	private UFDouble sumAccInterest(ICostingTool costingtool, AgreedbbVO tradevo) {

		UFDouble result = UFDouble.ZERO_DBL;
		// ��ͬ��
		String contractNO = tradevo.getContract_id();
		// �������
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// ��ǰ��֯
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		if (contractNO == null || enddate == null || pk_org == null) {
			return result;
		}

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		qrySql.append(" and trade_date <='").append(DateTool.asEnd(enddate))
				.append("'");
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_AGREEDBB_MRJX).append("'");
		qrySql.append(" and pk_org='").append(pk_org).append("'");
		qrySql.append(" and state !='").append(SystemConst.statecode[2])
				.append("'");
		qrySql.append(" and isnull(dr,0)=0");
		// �������� < �������

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
			ExceptionUtils.wrappBusinessException("����ÿ����Ϣʧ�ܣ����ѯ��־��");
		}
		return result;
	}

	/**
	 * ���ܹ�Ϣ���˺�
	 * 
	 * @param costingtool
	 * @return
	 */
	private UFDouble sumDividend(ICostingTool costingtool, AgreedbbVO tradevo) {

		UFDouble result = UFDouble.ZERO_DBL;
		// ��ͬ��
		String contractNO = tradevo.getContract_id();
		// �������
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// ��ǰ��֯
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		qrySql.append(" and trade_date <='").append(DateTool.asEnd(enddate))
				.append("'");
		// ȡ��Ϣ���˵�
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_AGREEDBBZQ).append("'");
		// ���Ƿ�����
		qrySql.append(" and state !='").append(SystemConst.statecode[2])
				.append("'");
		qrySql.append(" and pk_org='").append(pk_org).append("'");
		qrySql.append(" and isnull(dr,0)=0");

		IYdsghMaintain ydsgh = NCLocator.getInstance().lookup(
				IYdsghMaintain.class);

		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SabbModuleConst.Entity_AgreedBB, qrySql.toString());
		try {
			AggAgreedbbVO[] aggvos = ydsgh.queryBillByPK(ydsgh
					.queryPKs(queryscheme));
			if (aggvos != null) {
				for (int i = 0; i < aggvos.length; i++) {
					AgreedbbVO agreedbbVO = (AgreedbbVO) aggvos[i].getParent();
					result = SafeCompute.add(result, agreedbbVO.getDividend());
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException("���ܹ�Ϣ����ʧ�ܣ����ѯ��־��");
		}
		return result;
	}

}
