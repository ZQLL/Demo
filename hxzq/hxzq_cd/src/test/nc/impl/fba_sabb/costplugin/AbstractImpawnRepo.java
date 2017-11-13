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
	 * 查询数据
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
	 * 审核模式处理
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
				vo.setApprover(pk_user);// 设置审核人
				vo.setApprovedate(PubMethod.getInstance().getDateTime(
						costingtool.getCurrdate()));// 设置审核日期
				vo.setState(SystemConst.statecode[3]);
			}
		}
	}

	/**
	 * 默认弃审 业务有特殊需求的话需要重写
	 * 
	 * @param costingtool
	 * @throws Exception
	 * @author libin
	 * @date 2012-11-2 下午2:55:47
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
			// 更新ts 解决界面被修改问题 YangJie 2014-04-22
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
	 * 审核时调用,业务实现类在这里实现自己的业务逻辑
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @throws Exception
	 */
	protected abstract void calculateWhenCheck(ICostingTool costingtool,
			ImpawnRepoVO tradevo) throws Exception;

	/**
	 * 弃审业务处理
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
