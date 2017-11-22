package nc.impl.fba_fund.plugin.abstractapprove;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.fba_fund.plugin.AbstractPlugin;
import nc.itf.fba_fund.IInterbanklendingMaintain;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_fund.interbanklending.AggInterbankLendingVO;
import nc.vo.fba_fund.interbanklending.InterbankLendingVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.QuerySchemeUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class AbstractLendingApprove extends AbstractPlugin {
	public BaseDAO dao = null;

	private IInterbanklendingMaintain getBusiInstance() {
		IInterbanklendingMaintain iInterbanklendingMaintain = NCLocator
				.getInstance().lookup(IInterbanklendingMaintain.class);
		return iInterbanklendingMaintain;
	}

	@Override
	public IBill[] queryData(ICostingTool costingtool) throws BusinessException {
		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				"fba_fund.InterbankLendingVO", costingtool.getCostParaVO()
						.getCondition());
		AggInterbankLendingVO[] aggvos = getBusiInstance().queryBillByPK(
				getBusiInstance().queryPKs(queryscheme));
		return aggvos;
	}

	@Override
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		String datakey = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup()
				+ costingtool.getCurrdate()
				+ costingtool.getCurrbilltype();
		IBill[] ibills = tradedatatool.getData(datakey);
		if ((ibills != null) && (ibills.length > 0)) {
			for (IBill ibill : ibills) {
				AggInterbankLendingVO aggvo = (AggInterbankLendingVO) ibill;
				InterbankLendingVO vo = aggvo.getParentVO();
				if (vo.getPk_stocksort() == null) {
					vo.setPk_stocksort(costingtool.getPk_stocksort()[0]);
				}
				calculateWhenCheck(costingtool, vo);

				vo.setApprover(costingtool.getCostParaVO().getCheckParavo()
						.getPk_user());
				vo.setApprovedate(PubMethod.getInstance().getDateTime(
						costingtool.getCurrdate()));
				vo.setState(Integer
						.valueOf(nc.vo.fba_fund.pub.SystemConst.statecode[3]));
			}
		}
	}

	protected void calculateWhenCheck(ICostingTool costingtool,
			InterbankLendingVO vo) throws Exception {
	}

	protected void calculateWhenUnCheck(ICostingTool costingtool,
			InterbankLendingVO InterbankLendingVO) throws Exception {
	}

	@Override
	public void unCheckBills(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws Exception {
		String datakey = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup()
				+ costingtool.getCurrdate()
				+ costingtool.getCurrbilltype();

		IBill[] ibills = tradedatatool.getData(datakey);
		if ((ibills != null) && (ibills.length > 0)) {
			for (IBill ibill : ibills) {
				AggInterbankLendingVO aggvo = (AggInterbankLendingVO) ibill;
				InterbankLendingVO vo = aggvo.getParentVO();
				calculateWhenUnCheck(costingtool, vo);
				vo.setApprover(null);
				vo.setApprovedate(null);
				vo.setState(Integer
						.valueOf(nc.vo.fba_fund.pub.SystemConst.statecode[1]));
			}
		}
	}

	@Override
	public IBill[] updateData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		IBill[] ibills = tradedatatool.getData(costingtool
				.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ costingtool.getCurrdate() + costingtool.getCurrbilltype());
		if (ibills != null) {
			AggInterbankLendingVO[] vos = new AggInterbankLendingVO[ibills.length];
			for (int i = 0; i < ibills.length; i++) {
				vos[i] = ((AggInterbankLendingVO) ibills[i]);
				Object dr = vos[i].getParent().getAttributeValue("dr");
				if ((dr != null) && (dr.equals(Integer.valueOf(1)))) {
					vos[i].getParent().setStatus(3);
				} else {
					vos[i].getParent().setStatus(1);
				}
			}
			AggInterbankLendingVO[] aggvos = getBusiInstance().update(vos);

			Map<String, Object> map = new HashMap();
			for (AggInterbankLendingVO aggvo : aggvos) {
				String key = aggvo.getParent().getPrimaryKey();
				Object value = aggvo.getParent().getAttributeValue("ts");
				map.put(key, value);
			}
			for (AggInterbankLendingVO cacheaggvo : vos) {
				cacheaggvo.getParent().setAttributeValue("ts",
						map.get(cacheaggvo.getParent().getPrimaryKey()));
			}
			return aggvos;
		}
		return null;
	}

	@Override
	public IBill[] insertData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		return null;
	}

	protected BaseDAO getBaseDao() {
		if (this.dao == null) {
			this.dao = new BaseDAO();
		}
		return this.dao;
	}
}
