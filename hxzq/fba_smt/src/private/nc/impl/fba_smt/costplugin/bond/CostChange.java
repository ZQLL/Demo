package nc.impl.fba_smt.costplugin.bond;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.fba_smt.costingplugin.AbstractPlugin;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_smt.trade.costadjustment.ICostadjustment;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_scost.cost.pub.QuerySchemeUtil;
import nc.vo.fba_smt.trade.costadjustment.AggCostAdjustmentVO;
import nc.vo.fba_smt.trade.costadjustment.CostAdjustmentVO;
import nc.vo.fba_smt.trade.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
/**
 * 成本调整单
 * 弃审时删除生成的成本单、中间表当日该交易类型的数据清掉
 * @author	liangwei
 * @date	2014-4-28 下午4:58:19
 * @version	1.0.0
 */
public class CostChange extends AbstractPlugin{
	
	private ICostadjustment getBusiInstance() {
		ICostadjustment iCostadjustment = NCLocator.getInstance().lookup(ICostadjustment.class);
		return iCostadjustment;

	}

	@Override
	public void unCheckBills(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws Exception {
		String datakey = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + costingtool.getCurrdate() + costingtool.getCurrbilltype();
		IBill[] ibills = tradedatatool.getData(datakey);
//		new SpecialDealPendBill().delete(ibills);
		if (ibills != null && ibills.length > 0) {
			for (IBill ibill : ibills) {
				AggCostAdjustmentVO aggvo = (AggCostAdjustmentVO) ibill;
				CostAdjustmentVO vo=aggvo.getParentVO();
				vo.setAttributeValue("dr", 1);
		}
		}
	}

	
	@Override
	public IBill[] queryData(ICostingTool costingtool) throws BusinessException {
		// TODO 自动生成的方法存根
		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(SystemConst.Entity_CostAdjustment, costingtool.getCostParaVO().getCondition());
		AggCostAdjustmentVO[] aggvos = getBusiInstance().queryBillByPK(getBusiInstance().queryPKs(queryscheme));
		return aggvos;
	}

	@Override
	public IBill[] updateData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		IBill[] ibills=	tradedatatool.getData(costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + costingtool.getCurrdate()+costingtool.getCurrbilltype());
		if (ibills != null) {
			AggCostAdjustmentVO[] vos = new AggCostAdjustmentVO[ibills.length];
			for (int i = 0; i < ibills.length; i++) {
				vos[i] = (AggCostAdjustmentVO) ibills[i];
				/** START 国金成本调整单记账后单据无法查询问题。 jingqt 2016年2月18日 这里是这样的问题：即在成本调整的时候，需要删除其单据，但是NC平台在设置dr从0到1时，为了压缩流量，在对比新旧vo时，将非静态的属性值设为了默认值，如果只设置dr=1，故dr返回时仍然时0 */
				Object dr = vos[i].getParentVO().getAttributeValue("dr");
				if (dr != null && dr.toString().equals("1")) {
					vos[i].getParent().setStatus(VOStatus.DELETED);
				} else {
					vos[i].getParent().setStatus(VOStatus.UPDATED);
				}
				/** END 国金成本调整单记账后单据无法查询问题。 jingqt 2016年2月18日 这里是这样的问题：即在成本调整的时候，需要删除其单据，但是NC平台在设置dr从0到1时，为了压缩流量，在对比新旧vo时，将非静态的属性值设为了默认值，如果只设置dr=1，故dr返回时仍然时0 */
			}
			AggCostAdjustmentVO[] aggvos = getBusiInstance().update(vos);
			// 更新ts 解决界面被修改问题 YangJie 2014-04-22
			Map map = new HashMap();// key primarykey value ts
			for (AggCostAdjustmentVO aggvo : aggvos) {
				String key = aggvo.getParent().getPrimaryKey();
				Object value = aggvo.getParent().getAttributeValue("ts");
				map.put(key, value);
			}
			for (AggCostAdjustmentVO cacheaggvo : vos) {
				cacheaggvo.getParent().setAttributeValue("ts", map.get(cacheaggvo.getParent().getPrimaryKey()));
			}
			return aggvos;
		}
		return null;
	}

	@Override
	public IBill[] insertData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

	
	/**
	 * 不需要审核
	 * 覆写方法：checkBills
	 * @param costingtool
	 * @param tradedatatool
	 * @throws Exception
	 * @see nc.itf.fba_scost.cost.pub.IBillCheckPlugin#checkBills(nc.bs.fba_scost.cost.simtools.CostingTool, nc.itf.fba_scost.cost.pub.TradeDataTool)
	 */
	@Override
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// TODO 自动生成的方法存根
		
	}

	
}
