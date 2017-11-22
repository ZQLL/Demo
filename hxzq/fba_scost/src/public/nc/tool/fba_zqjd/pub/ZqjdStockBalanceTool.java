package nc.tool.fba_zqjd.pub;

import nc.bs.logging.Logger;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.VOUtil;

import org.apache.commons.lang.StringUtils;

public class ZqjdStockBalanceTool {

	private static Object locker = new Object();
	private static ZqjdStockBalanceTool stockBalanceTool = null;

	private ZqjdStockBalanceTool() {
		super();
	}

	public static ZqjdStockBalanceTool getInstance() {

		if (stockBalanceTool == null) {
			synchronized (locker) {
				if (stockBalanceTool == null) {
					stockBalanceTool = new ZqjdStockBalanceTool();
				}
			}
		}
		return stockBalanceTool;

	}

	/**
	 * 更新库存
	 * 
	 * @param costingtool
	 * @param stockBalanceVO
	 */
	public void updateStockBalace(BanlanceQueryKeyVO queryKeyVO,
			ICostingTool costingtool, StockBalanceVO stockBalanceVO) {
		ICostBalanceTool tool = costingtool.getBalancetool();
		try {
			tool.updateStockbalanceVO(queryKeyVO, stockBalanceVO);
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	/**
	 * 查询当前库存余额
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	public StockBalanceVO qryStockBalnce(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, ZqjdVO tradevo) {

		// 库存工具类（缓存功能）
		ICostBalanceTool tool = costingtool.getBalancetool();
		StockBalanceVO stockbalancevo = null;
		try {
			stockbalancevo = tool.getStockbalanceVO(queryvo, costingtool);
			// 第一次借入
			if (stockbalancevo == null) {
				checkStocksort(tradevo);// 检查库存组织是否为空
				stockbalancevo = tool.getStockbalanceVOByVO(tradevo,
						costingtool);
				// 默认设置负债库存 方案1 遗留
				// stockbalancevo.setAttributeValue(SystemConst.Fields_AssetDebt,
				// SystemConst.Values_AssetDebt[1]) ;
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return stockbalancevo;

	}

	/**
	 * 检查库存组织
	 * 
	 * @param tradevo
	 * @throws BusinessException
	 */
	private void checkStocksort(ZqjdVO tradevo) throws BusinessException {
		if (tradevo == null)
			return;
		if (StringUtils.isEmpty(tradevo.getPk_stocksort())) {
			throw new BusinessException("单据号：" + tradevo.getBillno()
					+ "，默认库存组织为空，请检查外部数据导入配置库存组织信息！");
		}
	}

	public BanlanceQueryKeyVO createQryBalanceVO(ICostingTool costingtool,
			ZqjdVO tradevo) {

		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		// 审核小组+成本维度
		queryvo.setKey(getCostKey(costingtool, tradevo));
		// 资产属性不能为空
		queryvo.setPk_assetsprop(tradevo.getPk_assetsprop());
		String pk_stocksort = (tradevo.getPk_stocksort() == null ? SystemConst.Pk_StockSort1
				: tradevo.getPk_stocksort());
		queryvo.setPk_stocksort(pk_stocksort);
		// 当前审核日期
		String trade_date = costingtool.getCurrdate();
		queryvo.setTrade_date(trade_date);

		return queryvo;

	}

	private String getCostKey(ICostingTool costingtool, ZqjdVO tradevo) {
		// 成本审核方案维度
		String[] costFieldArray = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		// String[] newArray = new String[costFieldArray.length + 1];
		// 数组拷贝
		// System.arraycopy(costFieldArray, 0, newArray, 0,
		// costFieldArray.length);
		// 增加 资产负债维度
		// newArray[costFieldArray.length] = ZqjdModuleConst.Fields_AssetDebt;
		String costKey = VOUtil.getCombinesKey(tradevo, costFieldArray);
		// String costKey = VOUtil.getCombinesKey(tradevo, newArray);
		// 审核组主键
		String pk_billtypegroup = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup();
		return pk_billtypegroup.concat(costKey);
	}

}
