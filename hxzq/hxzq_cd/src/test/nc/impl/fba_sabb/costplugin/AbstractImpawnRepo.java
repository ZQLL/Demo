package nc.impl.fba_sabb.costplugin;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.fba_sabb.constant.SabbModuleConst;
import nc.itf.fba_sabb.trade.IGpzyshgMaintain;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_sabb.trade.gpzyshg.AggImpawnRepoVO;
import nc.vo.fba_sabb.trade.gpzyshg.ImpawnRepoVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.QuerySchemeUtil;
import nc.vo.fba_sec.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public abstract class AbstractImpawnRepo extends AbstractPlugin {

	private IGpzyshgMaintain getBusiInstance() {
		IGpzyshgMaintain iBondbuy = NCLocator.getInstance().lookup(
				IGpzyshgMaintain.class);
		return iBondbuy;
	}

	/**
	 * ��ѯ����
	 * 
	 * @param costingtool
	 * @return
	 * @throws BusinessException
	 * @see nc.itf.fba_scost.cost.pub.IBillCheckPlugin#queryData(nc.bs.fba_scost.cost.simtools.CostingTool)
	 */
	public IBill[] queryData(ICostingTool costingtool) throws BusinessException {
		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SabbModuleConst.Entity_ImpawnRepo, costingtool.getCostParaVO()
						.getCondition());
		AggImpawnRepoVO[] aggvos = getBusiInstance().queryBillByPK(
				getBusiInstance().queryPKs(queryscheme));
		return aggvos;
	}

	/**
	 * ���ģʽ����
	 * 
	 * @param costingtool
	 * @throws Exception
	 */
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		IBill[] ibills = tradedatatool.getData(getCalcCostKey(costingtool));
		if (ibills != null && ibills.length > 0) {
			for (IBill ibill : ibills) {
				AggImpawnRepoVO aggvo = (AggImpawnRepoVO) ibill;
				ImpawnRepoVO vo = aggvo.getParentVO();
				calculateWhenCheck(costingtool, vo);
				// costingtool.updateFunds(vo);
				vo.setApprover(pk_user);// ���������
				vo.setApprovedate(PubMethod.getInstance().getDateTime(
						costingtool.getCurrdate()));// �����������
				vo.setState(SystemConst.statecode[3]);
			}
		}
	}

	/**
	 * Ĭ������ ҵ������������Ļ���Ҫ��д
	 * 
	 * @param costingtool
	 * @throws Exception
	 * @author libin
	 * @date 2012-11-2 ����2:55:47
	 */
	public void unCheckBills(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws Exception {
		IBill[] ibills = tradedatatool.getData(getCalcCostKey(costingtool));
		if (ibills != null && ibills.length > 0) {
			for (IBill ibill : ibills) {
				AggImpawnRepoVO aggvo = (AggImpawnRepoVO) ibill;
				ImpawnRepoVO vo = aggvo.getParentVO();
				calculateWhenUnCheck(costingtool, vo);
				vo.setApprover(null);
				vo.setApprovedate(null);
				vo.setState(SystemConst.statecode[1]);
			}
		}
	}

	@Override
	public IBill[] updateData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		IBill[] ibills = tradedatatool.getData(getCalcCostKey(costingtool));
		if (ibills != null) {
			AggImpawnRepoVO[] vos = new AggImpawnRepoVO[ibills.length];
			for (int i = 0; i < ibills.length; i++) {
				vos[i] = (AggImpawnRepoVO) ibills[i];
				vos[i].getParent().setStatus(VOStatus.UPDATED);
			}
			AggImpawnRepoVO[] aggvos = getBusiInstance().update(vos);
			// ����ts ������汻�޸����� YangJie 2014-04-22
			Map<String, Object> map = new HashMap<String, Object>();// key
																	// primarykey
																	// value ts
			for (AggImpawnRepoVO aggvo : aggvos) {
				String key = aggvo.getParent().getPrimaryKey();
				Object value = aggvo.getParent().getAttributeValue("ts");
				map.put(key, value);
			}
			for (AggImpawnRepoVO cacheaggvo : vos) {
				cacheaggvo.getParent().setAttributeValue("ts",
						map.get(cacheaggvo.getParent().getPrimaryKey()));
			}
			return aggvos;
		}
		return null;
	}

	/**
	 * ���ʱ����,ҵ��ʵ����������ʵ���Լ���ҵ���߼�
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @throws Exception
	 */
	protected abstract void calculateWhenCheck(ICostingTool costingtool,
			ImpawnRepoVO tradevo) throws Exception;

	/**
	 * ����ҵ����
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @throws Exception
	 */
	protected abstract void calculateWhenUnCheck(ICostingTool costingtool,
			ImpawnRepoVO tradevo) throws Exception;

	@Override
	public IBill[] insertData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		return null;
	}
}
