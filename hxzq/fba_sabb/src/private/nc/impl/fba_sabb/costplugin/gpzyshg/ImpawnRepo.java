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
 * ��Ʊ��Ѻʽ���ز��
 * 
 * @author Administrator
 * 
 */
public class ImpawnRepo extends AbstractImpawnRepo {

	@Override
	protected void calculateWhenCheck(ICostingTool costingtool,
			ImpawnRepoVO tradevo) throws Exception {
		/**
		 * ��Ʊ��Ѻʽ�ع����������ͣ���Ʊ��Ѻʽ�ع���ʼ���ף���Ʊ��Ѻʽ�ع����ڹ��أ�����Ϊ���ֶΣ���ʼ���׽����ؽ�
		 * 
		 * @author lihbj
		 */
		if (SabbModuleConst.BILLTYPE_IMPAWNREPO_NORMAL.equals(tradevo
				.getTranstypecode())
				|| SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE
						.equals(tradevo.getTranstypecode())) {
			if (tradevo.getEntrust_sum() == null || tradevo.getBb_sum() == null) {
				throw new Exception("������ڣ�" + costingtool.getCurrdate()
						+ " �������ͣ���Ʊ��Ѻʽ�ع���ʼ���ף���Ʊ��Ѻʽ�ع����ڹ��أ�����Ϊ���ֶΣ���ʼ���׽����ؽ�");
			} else if (tradevo.getEntrust_sum().compareTo(UFDouble.ZERO_DBL) == 0
					|| tradevo.getBb_sum().compareTo(UFDouble.ZERO_DBL) == 0) {
				throw new Exception("������ڣ�" + costingtool.getCurrdate()
						+ " �������ͣ���Ʊ��Ѻʽ�ع���ʼ���ף���Ʊ��Ѻʽ�ع����ڹ��أ�����Ϊ���ֶΣ���ʼ���׽����ؽ�");
			}
		}

		// ���Ƚ�Ϣ ���� �ع�����
		/*
		 * if (SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE.equals(tradevo.
		 * getTranstypecode()) ||
		 * SabbModuleConst.BILLTYPE_IMPAWNREPO_EXT.equals(
		 * tradevo.getTranstypecode())) { UFDouble interest =
		 * sumImpInterest(costingtool, tradevo); UFDouble jdjx =
		 * sumJdjx(costingtool, tradevo); //���
		 * tradevo.setAcc_interest(SafeCompute.sub(interest, jdjx)); }
		 */
		/**
		 * update by lihaibo ���Ƚ�Ϣ�߼��� �ۼ�
		 */
		// ���Ƚ�Ϣ
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
			 * //������� if (csjj.toDouble() != 0 && entrust_sum.toDouble() != 0) {
			 * bl = entrust_sum.div(csjj.sub(dqgh)); } // ���*����
			 * tradevo.setAcc_interest(SafeCompute.sub(interest,
			 * jdjx).sub(ce).multiply(bl));
			 */
			// ���ó˱���
			tradevo.setAcc_interest(SafeCompute.sub(interest, jdjx).sub(ce));
			// �Ѽ�����Ϣ-�յ���Ϣ
			tradevo.setVdef4(SafeCompute.sub(tradevo.getAcc_interest(),
					tradevo.getSettleinterest()).toString());
		}
		// �ع�����
		if (SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE.equals(tradevo
				.getTranstypecode())) {
			UFDouble interest = sumImpInterest(costingtool, tradevo);
			UFDouble jdjx = sumJdjx(costingtool, tradevo);
			UFDouble csjj = sumCsjj(costingtool, tradevo);
			UFDouble dqgh = sumDqgh(costingtool, tradevo);
			UFDouble ce = sumCe(costingtool, tradevo);
			UFDouble bb_sum = tradevo.getBb_sum() == null ? UFDouble.ZERO_DBL
					: tradevo.getBb_sum();
			// ���ι��ؽ��
			UFDouble entrust_sum = tradevo.getEntrust_sum() == null ? UFDouble.ZERO_DBL
					: tradevo.getEntrust_sum();
			UFDouble bl = UFDouble.ZERO_DBL;
			// �����ι��ؽ��С�������������ڲ��ֹ���
			if (entrust_sum.compareTo(csjj.sub(dqgh)) < 0) {
				// ���Ѽ�����Ϣ����Ϊ0
				tradevo.setAcc_interest(UFDouble.ZERO_DBL);
			} else {
				// �������
				if (csjj.toDouble() != 0 && bb_sum.toDouble() != 0) {
					bl = bb_sum.div(csjj.sub(dqgh));
				}
				// ���*����
				tradevo.setAcc_interest((SafeCompute.sub(interest, jdjx)
						.sub(ce)).multiply(bl));
			}
			// �Ѽ�����Ϣ-�յ���Ϣ
			tradevo.setVdef4(SafeCompute.sub(tradevo.getAcc_interest(),
					tradevo.getSettleinterest()).toString());
		}

	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			ImpawnRepoVO tradevo) throws Exception {

		// ���Ƚ�Ϣ ���� �ع�����
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
		// ��ͬ��
		String contractNO = tradevo.getContract_id();
		// �������
		UFDate enddate = new UFDate(costingtool.getCurrdate());
		// ��ǰ��֯
		String pk_org = costingtool.getCostParaVO().getCheckParavo()
				.getPk_org();

		StringBuffer qrySql = new StringBuffer();
		qrySql.append(" contract_id ='").append(contractNO).append("'");
		// update by lihaibo �����㵱������
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		// ���Ƚ�����Ϣ �� �ع�����
		qrySql.append(" and transtypecode in ('")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_EXT).append("','")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE)
				.append("')");
		// ���Ƿ�����
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
			ExceptionUtils.wrappBusinessException("���ܼ��Ƚ�Ϣʧ�ܣ����ѯ��־��");
		}
		return result;

	}

	private UFDouble sumImpInterest(ICostingTool costingtool,
			ImpawnRepoVO tradevo) {

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
		// update by lihaibo ���Ѽ�����Ϣʱ�����㵱���ÿ����Ϣ����
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_MRJX).append("'");
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
	 * ȡ��ʼ�����ܶ� add by lihaibo
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	private UFDouble sumCsjj(ICostingTool costingtool, ImpawnRepoVO tradevo) {

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
		// ��ʼ��������
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_NORMAL).append("'");
		// ���Ƿ�����
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
			ExceptionUtils.wrappBusinessException("����ί�н��ʧ�ܣ����ѯ��־��");
		}
		return result;

	}

	/**
	 * ȡ���ڹ����ܶ� ��ֹ��ǰһ�� add by lihaibo
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	private UFDouble sumDqgh(ICostingTool costingtool, ImpawnRepoVO tradevo) {

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
		// ��ֹ��ǰһ��
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		// ���ڹ�������
		qrySql.append(" and transtypecode='")
				.append(SabbModuleConst.BILLTYPE_IMPAWNREPO_PRECOMPLETE)
				.append("'");
		// ���Ƿ�����
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
			ExceptionUtils.wrappBusinessException("�����ڹ��ؽ��ʧ�ܣ����ѯ��־��");
		}
		return result;

	}

	/**
	 * ȡ������ add by lihaibo
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	private UFDouble sumCe(ICostingTool costingtool, ImpawnRepoVO tradevo) {

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
		// update by lihaibo �����㵱������
		qrySql.append(" and trade_date <='")
				.append(DateTool.asEnd(enddate.getDateBefore(1))).append("'");
		// ���Ƿ�����
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
			ExceptionUtils.wrappBusinessException("ȡ������ ʧ�ܣ����ѯ��־��");
		}
		return result;

	}
}
