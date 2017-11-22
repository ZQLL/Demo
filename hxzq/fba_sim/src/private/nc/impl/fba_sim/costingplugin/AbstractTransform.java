package nc.impl.fba_sim.costingplugin;

import java.util.ArrayList;
import java.util.*;

import nc.bs.framework.common.NCLocator;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_sim.simtrade.transformtrade.ITransformtrade;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.QuerySchemeUtil;
import nc.vo.fba_sim.pub.SystemConst;
import nc.vo.fba_sim.simtrade.transformtrade.AggTransformtradeVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.trade.voutils.VOUtil;

/**
 * 证券转换审核处理插件类
 */

public abstract class AbstractTransform extends AbstractPlugin {

	private ITransformtrade getBusiInstance() {
		ITransformtrade itransformtrade = NCLocator.getInstance().lookup(
				ITransformtrade.class);
		return itransformtrade;

	}

	
	@Override
	public IBill[] queryData(ICostingTool costingtool) throws BusinessException {
		IQueryScheme queryscheme = QuerySchemeUtil.createQueryScheme(
				SystemConst.Entity_TransForm, costingtool.getCostParaVO()
						.getCondition());
		AggTransformtradeVO[] aggs = getBusiInstance().queryBillByPK(
				getBusiInstance().queryPKs(queryscheme));
		return aggs;
	}

	/**
	 * 默认更新 业务有特殊需求的话需要重写
	 * 
	 * @param condition
	 * @param ischecked
	 * @return
	 * @author libin
	 * @date 2012-11-2 下午2:51:32
	 */
	@Override
	public IBill[] updateData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		IBill[] ibills = tradedatatool.getData(costingtool
				.getCurrbilltypegroupvo().getPk_billtypegroup()
				+ costingtool.getCurrdate() + costingtool.getCurrbilltype());
		if (ibills != null) {
			AggTransformtradeVO[] vos = new AggTransformtradeVO[ibills.length];
			for (int i = 0; i < ibills.length; i++) {
				vos[i] = (AggTransformtradeVO) ibills[i];
				vos[i].getParent().setStatus(VOStatus.UPDATED);
			}
			AggTransformtradeVO[] aggvos = getBusiInstance().update(vos);
			// 更新ts 解决界面被修改问题 YangJie 2014-04-22
			Map map = new HashMap();// key primarykey value ts
			for (AggTransformtradeVO aggvo : aggvos) {
				String key = aggvo.getParent().getPrimaryKey();
				Object value = aggvo.getParent().getAttributeValue("ts");
				map.put(key, value);
			}
			for (AggTransformtradeVO cacheaggvo : vos) {
				cacheaggvo.getParent().setAttributeValue("ts",
						map.get(cacheaggvo.getParent().getPrimaryKey()));
			}
			return aggvos;
		}
		return null;
	}

	/**
	 * 默认插入业务有特殊需求的话需要重写
	 * 
	 * @param condition
	 * @param ischecked
	 * @return
	 * @author libin
	 * @date 2012-11-2 下午2:51:32
	 */
	@Override
	public IBill[] insertData(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws BusinessException {
		// IQueryScheme queryscheme =
		// QuerySchemeUtil.createQueryScheme(SystemConst.Entity_FundSplit,
		// costingtool.getCostParaVO().getCondition());
		// AggFundSplitVO[] aggvos = getBusiInstance().query(queryscheme);
		// return aggvos;
		return null;
	}

	@Override
	
	public void checkBills(ICostingTool costingtool, TradeDataTool tradedatatool)
			throws Exception {
		String pk_user = costingtool.getCostParaVO().getCheckParavo()
				.getPk_user();
		String datakey = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup()
				+ costingtool.getCurrdate()
				+ costingtool.getCurrbilltype();
		IBill[] ibills = tradedatatool.getData(datakey);
		if (ibills != null && ibills.length > 0) {
			for (IBill ibill : ibills) {
				AggTransformtradeVO aggvo = (AggTransformtradeVO) ibill;
				TransformtradeVO fathervo = aggvo.getParentVO();
				if (fathervo.getHc_pk_stocksort() == null) {
					fathervo.setHc_pk_stocksort(costingtool.getPk_stocksort()[0]);
				}
				if (fathervo.getHr_pk_stocksort() == null) {
					fathervo.setHr_pk_stocksort(costingtool.getPk_stocksort()[1]);
				}
				// vo.setpk_stocksort(vo.getHr_pk_stocksort());
				// vo.setPk_assetsprop(vo.getHr_pk_assetsprop());
				calculate(costingtool, fathervo);
				costingtool.updateFunds(fathervo);
				fathervo.setApprover(pk_user);
				fathervo.setApprovedate(PubMethod.getInstance().getDateTime(
						costingtool.getCurrdate()));
				fathervo.setState(SystemConst.statecode[3]);
			}
		}
	}

	protected void calculate(ICostingTool costingtool, TransformtradeVO tradevo)
			throws Exception {
	}

	@Override
	
	public void unCheckBills(ICostingTool costingtool,
			TradeDataTool tradedatatool) throws Exception {
		String datakey = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup()
				+ costingtool.getCurrdate()
				+ costingtool.getCurrbilltype();
		IBill[] ibills = tradedatatool.getData(datakey);
		if (ibills != null && ibills.length > 0) {
			for (IBill ibill : ibills) {
				AggTransformtradeVO aggvo = (AggTransformtradeVO) ibill;
				TransformtradeVO fathervo = (TransformtradeVO) aggvo
						.getParent();// 无孩子
				calculateWhenUnCheck(costingtool, fathervo);
				/**
				 * 注释掉转出金额、转入金额清空逻辑，该字段逻辑在calculateWhenUnCheck（costingtool,
				 * fathervo）方法里已处理
				 * 
				 * @author cjh
				 * @2015-12-07
				 */
				// fathervo.setBargain_sum(null);
				// fathervo.setBargain_sum2(null);

				fathervo.setSellcost(null);
				fathervo.setAccrual_sum(null);
				fathervo.setApprover(null);
				fathervo.setApprovedate(null);
				fathervo.setState(SystemConst.statecode[1]);
			}
		}
	}

	protected void calculateWhenUnCheck(ICostingTool costingtool,
			TransformtradeVO vo) throws Exception {

	}

	protected String getCombinesKey(TransformtradeVO tradevo, String[] keys,
			boolean ishr) {
		List<String> newkeys = new ArrayList<String>();
		if (ishr) {
			for (String key : keys) {
				if (key.equals("pk_group") || key.equals("pk_org")
						|| key.equals("pk_glorgbook")) {
					newkeys.add(key);
				} else {
					newkeys.add("hr_" + key);
				}
			}

		} else {
			for (String key : keys) {
				if (key.equals("pk_group") || key.equals("pk_org")
						|| key.equals("pk_glorgbook")) {
					newkeys.add(key);
				} else {
					newkeys.add("hc_" + key);
				}
			}
		}
		return VOUtil.getCombinesKey(tradevo, newkeys.toArray(new String[0]));
	}
}
