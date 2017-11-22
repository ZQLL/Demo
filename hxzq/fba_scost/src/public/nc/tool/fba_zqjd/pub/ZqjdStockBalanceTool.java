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
	 * ���¿��
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
	 * ��ѯ��ǰ������
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @return
	 */
	public StockBalanceVO qryStockBalnce(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, ZqjdVO tradevo) {

		// ��湤���ࣨ���湦�ܣ�
		ICostBalanceTool tool = costingtool.getBalancetool();
		StockBalanceVO stockbalancevo = null;
		try {
			stockbalancevo = tool.getStockbalanceVO(queryvo, costingtool);
			// ��һ�ν���
			if (stockbalancevo == null) {
				checkStocksort(tradevo);// �������֯�Ƿ�Ϊ��
				stockbalancevo = tool.getStockbalanceVOByVO(tradevo,
						costingtool);
				// Ĭ�����ø�ծ��� ����1 ����
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
	 * �������֯
	 * 
	 * @param tradevo
	 * @throws BusinessException
	 */
	private void checkStocksort(ZqjdVO tradevo) throws BusinessException {
		if (tradevo == null)
			return;
		if (StringUtils.isEmpty(tradevo.getPk_stocksort())) {
			throw new BusinessException("���ݺţ�" + tradevo.getBillno()
					+ "��Ĭ�Ͽ����֯Ϊ�գ������ⲿ���ݵ������ÿ����֯��Ϣ��");
		}
	}

	public BanlanceQueryKeyVO createQryBalanceVO(ICostingTool costingtool,
			ZqjdVO tradevo) {

		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		// ���С��+�ɱ�ά��
		queryvo.setKey(getCostKey(costingtool, tradevo));
		// �ʲ����Բ���Ϊ��
		queryvo.setPk_assetsprop(tradevo.getPk_assetsprop());
		String pk_stocksort = (tradevo.getPk_stocksort() == null ? SystemConst.Pk_StockSort1
				: tradevo.getPk_stocksort());
		queryvo.setPk_stocksort(pk_stocksort);
		// ��ǰ�������
		String trade_date = costingtool.getCurrdate();
		queryvo.setTrade_date(trade_date);

		return queryvo;

	}

	private String getCostKey(ICostingTool costingtool, ZqjdVO tradevo) {
		// �ɱ���˷���ά��
		String[] costFieldArray = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		// String[] newArray = new String[costFieldArray.length + 1];
		// ���鿽��
		// System.arraycopy(costFieldArray, 0, newArray, 0,
		// costFieldArray.length);
		// ���� �ʲ���ծά��
		// newArray[costFieldArray.length] = ZqjdModuleConst.Fields_AssetDebt;
		String costKey = VOUtil.getCombinesKey(tradevo, costFieldArray);
		// String costKey = VOUtil.getCombinesKey(tradevo, newArray);
		// ���������
		String pk_billtypegroup = costingtool.getCurrbilltypegroupvo()
				.getPk_billtypegroup();
		return pk_billtypegroup.concat(costKey);
	}

}
