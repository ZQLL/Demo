package nc.impl.sim.iufo;

import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.itf.sim.iufo.ISIMIufoResultService;
import nc.itf.sim.iufo.IufoQuerySIMVO;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sim.iufo.SIMIufoFuncConst;

/**
 * SIM ģ�� IUFO ������̨������
 * 
 * @author hupl
 * 
 */
public class SIMIufoResultServiceImpl implements ISIMIufoResultService {
	/**
	 * SIM UIFO ������̨���
	 */
	@Override
	public Object getFuncResult(String funName, IufoQuerySIMVO simvo)
			throws Exception {
		// String pk_group = AppContextUtil.getPk_group();// ȡĬ��ֵ
		//
		// simvo.setPk_group(pk_group);// ��ǰ��¼����
		if (SIMIufoFuncConst.SIM_ZQQCSL.equals(funName)) {// ����ڳ�����
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, true, true);
		} else if (SIMIufoFuncConst.SIM_ZQQMSL.equals(funName)) {// �����ĩ����
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, false, true);
		} else if (SIMIufoFuncConst.SIM_ZQQCJE.equals(funName)) {// ����ڳ����
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, true, false);
		} else if (SIMIufoFuncConst.SIM_ZQQMJE.equals(funName)) {// �����ĩ���
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			return slimp.getFuncResult(simvo, false, false);
		} else if (SIMIufoFuncConst.SIM_ZQSZQC.equals(funName)) {// ��ֵ�ڳ����
			SIM_ZQSZImpl szimp = new SIM_ZQSZImpl();
			return szimp.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_ZQSZQM.equals(funName)) {// ��ֵ��ĩ���
			SIM_ZQSZImpl szimp = new SIM_ZQSZImpl();
			return szimp.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_HXZQSZQM.equals(funName)) {// Լ��ʽ����-��ֵ��ĩ���
			SIM_HXZQSZImpl szimp = new SIM_HXZQSZImpl();
			return szimp.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_ZQGYQC.equals(funName)) {// ���ʼ�ֵ�ڳ����
			SimStockFairValue sf = new SimStockFairValue();
			return sf.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_ZQGYQM.equals(funName)) {// ���ʼ�ֵ��ĩ���
			SimStockFairValue sf = new SimStockFairValue();
			return sf.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_ZQCJFS.equals(funName)) {// ���ڷ����ɽ����
			SIM_ZQSLImpl slimp = new SIM_ZQSLImpl();
			UFDouble qc = (UFDouble) slimp.getFuncResult(simvo, true, false);
			UFDouble qm = (UFDouble) slimp.getFuncResult(simvo, false, false);
			return qm.sub(qc);
		} else if (SIMIufoFuncConst.SIM_ZQXSCB.equals(funName)) {// ���ڷ������۳ɱ�
			SIM_SellCostImpl slimp = new SIM_SellCostImpl();
			return slimp.getFuncResult(simvo);
			/** 2016��1��28�� jingqt ������ӵ�5������ */
		} else if (SIMIufoFuncConst.SIM_QCCYBL.equals(funName)) {// �ڳ����б�������
			SIM_CYBL rate = new SIM_CYBL();
			return rate.getFuncResult(simvo, false);
		} else if (SIMIufoFuncConst.SIM_QMCYBL.equals(funName)) {// ��ĩ���б�������
			SIM_CYBL rate = new SIM_CYBL();
			return rate.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_JYRIPYL.equals(funName)) {// ��������ƫ����
			SIM_JYRIPYL offset = new SIM_JYRIPYL();
			return offset.getFuncResult(simvo);
		} else if (SIMIufoFuncConst.SIM_CCZBPMBM.equals(funName)) {// �ֲ�����/�ܹɱ�ռ��֤ȯ���������ر��룩
			return SIM_CCZBPMImpl.getInstance().getFuncResult(simvo, 1);
		} else if (SIMIufoFuncConst.SIM_CCZBPMMC.equals(funName)) {// �ֲ�����/�ܹɱ�ռ��֤ȯ�������������ƣ�
			return SIM_CCZBPMImpl.getInstance().getFuncResult(simvo, 2);
		} else if (SIMIufoFuncConst.SIM_CCZBPMPK.equals(funName)) {// �ֲ�����/�ܹɱ�ռ��֤ȯ����������PK��
			return SIM_CCZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_CBZBPMBM.equals(funName)) {// �ɱ�/���ʱ�ռ��֤ȯ���������ر��룩
			return SIM_CBZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_CBZBPMMC.equals(funName)) {// �ɱ�/���ʱ�ռ��֤ȯ�������������ƣ�
			return SIM_CBZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_CBZBPMPK.equals(funName)) {// �ɱ�/���ʱ�ռ��֤ȯ����������PK��
			return SIM_CBZBPMImpl.getInstance().getFuncResult(simvo, 0);
		} else if (SIMIufoFuncConst.SIM_MRHYJZQM.equals(funName)) {
			SIM_MRHYJZQMImpl slimp = new SIM_MRHYJZQMImpl();
			return slimp.getFuncResult(simvo, true);
		} else if (SIMIufoFuncConst.SIM_MCHYJZQM.equals(funName)) {
			SIM_MRHYJZQMImpl slimp = new SIM_MRHYJZQMImpl();
			return slimp.getFuncResult(simvo, false);
		}
		return 0;
	}

	/**
	 * ��ѯ��˷���<br>
	 * <B>�������ڹ涨�˱�Ҳ��Ҫ��ֵ�������������Ҫ����һ���˱��Ĳ��� modify by jingqt</B>
	 * 
	 * @param pk_group
	 * @param pk_org
	 * @param pk_glorybook
	 * @return
	 * @throws BusinessException
	 * @author jingqt
	 * @date 2016-1-29 ����3:34:40
	 */
	public CostPlanVO queryCostPlanInfo(String pk_group, String pk_org,
			String pk_glorybook) throws BusinessException {
		if (pk_group == null || pk_org == null)
			return null;
		CostPlanVO[] planvos = null;
		StringBuffer condition = new StringBuffer();
		condition.append("  pk_org = '" + pk_org + "' ");
		condition.append("  and pk_group = '" + pk_group
				+ "' and isnull(dr,0) = 0 ");
		condition.append("  and isbusinessplan = 'Y' ");
		condition.append("  and pk_glorgbook = '" + pk_glorybook + "' ");

		planvos = (CostPlanVO[]) new HYSuperDMO().queryByWhereClause(
				CostPlanVO.class, condition.toString());
		if (planvos.length == 0 || planvos == null) {
			Logger.debug("��ѯ���� ��ǰ��֯�ɱ����㷽��Ϊ�գ���");
			return null;
		}

		return planvos[0];
	}

	/**
	 * ȡ�ڳ�����
	 */
	public String getQcDate(IufoQuerySIMVO simvo) throws BusinessException {
		String period = simvo.getPeriod();// "2015-02-01"
		if (period == null || "".equals(period))
			throw new BusinessException("���� '�ڼ�' Ϊ�գ��������ã�  ");
		String periodtype = simvo.getPeriodType();// { "��", "��", "��" };
		String pix_y = period.substring(0, 4);
		String pix_m = period.substring(0, 7);
		if ("��".equals(periodtype)) {
			period = pix_y + "-01-01 00:00:00";
		} else if ("��".equals(periodtype)) {
			period = pix_m + "-01 00:00:00";
		} else if ("��".equals(periodtype)) {
			period = period + " 00:00:00";
		}
		return period;
	}

	/**
	 * ȡ��ĩ����
	 */
	public String getQmDate(IufoQuerySIMVO simvo) throws BusinessException {
		String period = simvo.getPeriod();// "2015-02-01"
		if (period == null || "".equals(period))
			throw new BusinessException("���� '�ڼ�' Ϊ�գ��������ã�  ");
		String periodtype = simvo.getPeriodType();// { "��", "��", "��" };
		String pix_y = period.substring(0, 4);
		String pix_m = period.substring(0, 7);
		if ("��".equals(periodtype)) {
			period = pix_y + "-12-31 23:59:59";
		} else if ("��".equals(periodtype)) {// ��ʹ��02-31���ַ���û�����⡣
			period = pix_m + "-31 23:59:59";
		} else if ("��".equals(periodtype)) {
			period = period + " 23:59:59";
		}
		return period;
	}
}
