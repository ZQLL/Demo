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
 * �ɱ�������
 * ����ʱɾ�����ɵĳɱ������м���ոý������͵��������
 * @author	liangwei
 * @date	2014-4-28 ����4:58:19
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
		// TODO �Զ����ɵķ������
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
				/** START ����ɱ����������˺󵥾��޷���ѯ���⡣ jingqt 2016��2��18�� ���������������⣺���ڳɱ�������ʱ����Ҫɾ���䵥�ݣ�����NCƽ̨������dr��0��1ʱ��Ϊ��ѹ���������ڶԱ��¾�voʱ�����Ǿ�̬������ֵ��Ϊ��Ĭ��ֵ�����ֻ����dr=1����dr����ʱ��Ȼʱ0 */
				Object dr = vos[i].getParentVO().getAttributeValue("dr");
				if (dr != null && dr.toString().equals("1")) {
					vos[i].getParent().setStatus(VOStatus.DELETED);
				} else {
					vos[i].getParent().setStatus(VOStatus.UPDATED);
				}
				/** END ����ɱ����������˺󵥾��޷���ѯ���⡣ jingqt 2016��2��18�� ���������������⣺���ڳɱ�������ʱ����Ҫɾ���䵥�ݣ�����NCƽ̨������dr��0��1ʱ��Ϊ��ѹ���������ڶԱ��¾�voʱ�����Ǿ�̬������ֵ��Ϊ��Ĭ��ֵ�����ֻ����dr=1����dr����ʱ��Ȼʱ0 */
			}
			AggCostAdjustmentVO[] aggvos = getBusiInstance().update(vos);
			// ����ts ������汻�޸����� YangJie 2014-04-22
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
		// TODO �Զ����ɵķ������
		return null;
	}

	
	/**
	 * ����Ҫ���
	 * ��д������checkBills
	 * @param costingtool
	 * @param tradedatatool
	 * @throws Exception
	 * @see nc.itf.fba_scost.cost.pub.IBillCheckPlugin#checkBills(nc.bs.fba_scost.cost.simtools.CostingTool, nc.itf.fba_scost.cost.pub.TradeDataTool)
	 */
	@Override
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		// TODO �Զ����ɵķ������
		
	}

	
}
