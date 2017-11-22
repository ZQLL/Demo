package nc.impl.fba_sim.costplugin.stock;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.fba_sim.costingplugin.AbstractStock;
import nc.impl.fba_sim.pub.RealratePlugin;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.itf.fba_sjll.sjll.calcrealrate.ICalcPluginMaintain;
import nc.itf.fba_zqjd.tally.IZqjdTallyService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.fba_sim.pub.SystemConst;
import nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

/**
 * ֤ȯ���룺֤ȯ�����ͽ�����ӣ��ʽ���١�
 * 
 * @author Neil
 * 
 */
public class Purchase extends AbstractStock {

	/**
	 * ���ʱ���������
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @throws Exception
	 * @author libin
	 * @date 2012-10-15 ����4:59:33
	 */
	@Override
	protected void calculateWhenCheck(ICostingTool costingtool,
			StocktradeVO tradevo) throws Exception {
		PubMethod pm = PubMethod.getInstance();
		ICostBalanceTool tool = costingtool.getBalancetool();
		String trade_date = costingtool.getCurrdate();
		String[] costFieldArray = costingtool.getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		String key = VOUtil.getCombinesKey(tradevo, costFieldArray);
		/**
		 * �˴�ƴ��key�������ҵ����
		 */
		key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + key;
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(key);
		queryvo.setPk_assetsprop(tradevo.getPk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getPk_stocksort());
		queryvo.setTrade_date(trade_date);

		StockBalanceVO stockbalancevo = tool.getStockbalanceVO(queryvo,
				costingtool);
		if (stockbalancevo == null) {
			stockbalancevo = tool.getStockbalanceVOByVO(tradevo, costingtool);
			stockbalancevo.setBegin_date(tradevo.getBegin_date());
			stockbalancevo.setEnd_date(tradevo.getEnd_date());
		}
		UFDouble stocks_num = pm.add(stockbalancevo.getStocks_num(),
				tradevo.getBargain_num());
		UFDouble stocks_sum = null;

		// �ʲ��� ��תӦ����Ϣ ����ʵ������ 20150417 ln
		if (isAsset(tradevo.getPk_assetsprop())) {
			// ��������Ӧ����Ϣ
			UFDouble lx = costcalc.calcInterest(costingtool, tradevo);
			tradevo.setAccrual_sum(lx);

			if (SystemConst.assetsprop[0] == pm.getAssetsProp(
					tradevo.getPk_assetsprop(), tradevo.getPk_org())) {
				stocks_sum = pm.add(stockbalancevo.getStocks_sum(),
						tradevo.getBargain_sum());
			} else {
				stocks_sum = pm
						.add(stockbalancevo.getStocks_sum(),
								pm.sub(tradevo.getFact_sum(),
										tradevo.getAccrual_sum()));
			}
			// ˰��
			UFDouble stocks_tax = pm.add(stockbalancevo.getStocks_tax(),
					tradevo.getTaxexpense());
			stockbalancevo.setStocks_num(stocks_num);
			stocks_sum = stocks_sum.setScale(2, UFDouble.ROUND_HALF_UP);
			stockbalancevo.setStocks_sum(stocks_sum);
			stockbalancevo.setStocks_tax(stocks_tax);
			stockbalancevo.setPk_billtypegroup(costingtool
					.getCurrbilltypegroupvo().getPk_billtypegroup());

			// ��ȡӦ����Ϣ
			costcalc.saveInterestDistill(costingtool, tradevo);
			// ʵ������
			RealratePlugin check = new RealratePlugin();
			if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
				ICalcPluginMaintain mail = NCLocator.getInstance().lookup(
						ICalcPluginMaintain.class);
				mail.buyPlugindeal(tradevo);
			}
		} else {

			if (tradevo.getTranstypecode().equals("HV3A-0xx-21")) {
				if (stockbalancevo == null) {
					costingtool.handleException(tradevo, null,
							SystemConst.error[1]);
				}
				/*********** �й�ծȯ�����֤ȯ���ڴ˼���Ӧ����Ϣ lqm **************/
				BaseDAO dao = new BaseDAO();
				String str = "select max(a.pk_group) pk_group,"
						+ "max(a.pk_org) pk_org,max(a.pk_org_v) pk_org_v,"
						+ "max(a.pk_stocksort) pk_stocksort,"
						+ "a.pk_securities,a.pk_assetsprop,a.pk_capaccount,"
						+ "a.pk_glorgbook,sum(a.bargain_num) bargain_num ,"
						+ "sum(a.bargain_sum) bargain_sum from sim_zqjd a "
						+ "where a.state >= 2 and isnull(a.dr,0) = 0 and "
						+ "a.pk_assetsprop = '"
						+ tradevo.getPk_assetsprop()
						+ "' "
						+ "and  a.transtypecode = 'HV7A-0xx-01' "
						+ "and  a.pk_securities = '"
						+ tradevo.getPk_securities()
						+ "' "
						+ "and '"
						+ tradevo.getTrade_date()
						+ "' > trade_date and '"
						+ tradevo.getTrade_date().toString().substring(0, 10)
						+ "' < date_end group by   "
						+ "a.pk_group,a.pk_org,a.pk_org_v,a.pk_securities,a.pk_assetsprop,a.pk_capaccount,a.pk_glorgbook";
				ArrayList<ZqjdVO> nowBowResult = (ArrayList<ZqjdVO>) dao
						.executeQuery(str, new BeanListProcessor(ZqjdVO.class));

				if ((nowBowResult != null && nowBowResult.size() > 0)) {
					// Ӧ����Ϣ��ת
					// Ӧ����Ϣ��ת lqm
					// ZqYjlxUtils utils
					// =NCLocator.getInstance().lookup(ZqYjlxUtils.class);
					FzmrYjlxUtils utils = new FzmrYjlxUtils();

					UFDouble lx = utils.forwardInterestDistill(costingtool,
							stockbalancevo, tradevo);
					tradevo.setInterest(lx.multiply(tradevo.getBargain_num()
							.div(nowBowResult.get(0).getBargain_num()
									.sub(stockbalancevo.getStocks_num()))));
					// ��ȡӦ����Ϣ
					utils.saveInterestDistill(costingtool, stockbalancevo,
							tradevo);
				}
				/*********** �й�ծȯ�����֤ȯ���ڴ˼���Ӧ����Ϣ lqm end **************/

			}

			IZqjdTallyService iZqjd = NCLocator.getInstance().lookup(
					IZqjdTallyService.class);
			// ����̨�� ��ת���ʼ�ֵ 20150417 ln
			ZqjdTallyVO zqjdtallyvo = iZqjd.gatherSellBuyBalance(tradevo);
			if (zqjdtallyvo == null
					|| (zqjdtallyvo != null && zqjdtallyvo.getBargain_num()
							.compareTo(UFDouble.ZERO_DBL) <= 0)) {
				costingtool.handleException(tradevo, null, "û����Ҫ�黹�ĸ�ծ��ծȯ");
			}
			stocks_sum = pm.multiply(stocks_num, new UFDouble(100));// �����=�������*100
			stocks_sum = stocks_sum.setScale(2, UFDouble.ROUND_HALF_UP);
			stockbalancevo.setStocks_sum(stocks_sum);
			stockbalancevo.setStocks_num(stocks_num);
			// �����ת���ʼ�ֵ
			UFDouble fairvalue = costcalc.forwardFairValueDistill_debt(
					costingtool, zqjdtallyvo, tradevo);
			tradevo.setFairvalue(fairvalue);
			// ��ծ�� ����̨�� 20150417 ln
			UFDouble sellcost = pm.multiply(tradevo.getBargain_num(),
					zqjdtallyvo.getBargain_sum());
			sellcost = pm.div(sellcost, zqjdtallyvo.getBargain_num());
			tradevo.setSellcost(sellcost);// ����̨�˷�д���۳ɱ�
			iZqjd.saveBuyTally(tradevo, sellcost.multiply(new UFDouble(-1)));
			// ���湫�ʼ�ֵ
			costcalc.saveFairValueDistill(costingtool, tradevo);
		}
		tool.updateStockbalanceVO(queryvo, stockbalancevo);
	}

	/**
	 * �����ʱ����Ӧ����Ϣ
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @throws Exception
	 * @author liangwei
	 * @date 2013-03-15 ����4:59:33
	 */
	
	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			StocktradeVO tradevo) throws Exception {
		if (isAsset(tradevo.getPk_assetsprop())) {
			String pk_group = costingtool.getCostParaVO().getCheckParavo()
					.getPk_group();
			String pk_glorgbook = costingtool.getCostParaVO().getCheckParavo()
					.getPk_glorgbook();
			String pk_org = costingtool.getCostParaVO().getCheckParavo()
					.getPk_org();
			String tradedate = costingtool.getCostParaVO().getCheckParavo()
					.getTrade_date().toLocalString();
			String calc = sysinit.getSysInitValue(pk_glorgbook,
					CostConstant.PARAM_CALCBUYSELLINTEREST);
			boolean iscalc = "Y".equals(calc) ? true : false;
			String conver = sysinit.getSysInitValue(pk_glorgbook,
					CostConstant.PARAM_ISCONVERINTEREST);
			boolean iscover = "Y".equals(conver) ? true : false;
			if (iscalc && iscover) {
				// ����Ӧ����Ϣ���
				tradevo.setAccrual_sum(null);
			}
			// ���ת��Ӧ����Ϣ
			costcalc.clearDistillInterest(costingtool);

			// ʵ������
			RealratePlugin check = new RealratePlugin();
			if (check.isRealTradevo(tradevo.getPk_glorgbook())) {
				ICalcPluginMaintain mail = NCLocator.getInstance().lookup(
						ICalcPluginMaintain.class);
				mail.unBuyPlugindeal(tradevo, pk_group, pk_org, tradedate);
			}
		} else {

			if (tradevo.getTranstypecode().equals("HV3A-0xx-21")) {

				BaseDAO dao = new BaseDAO();
				String str = "select max(a.pk_group) pk_group,"
						+ "max(a.pk_org) pk_org,max(a.pk_org_v) pk_org_v,"
						+ "max(a.pk_stocksort) pk_stocksort,"
						+ "a.pk_securities,a.pk_assetsprop,a.pk_capaccount,"
						+ "a.pk_glorgbook,sum(a.bargain_num) bargain_num ,"
						+ "sum(a.bargain_sum) bargain_sum from sim_zqjd a "
						+ "where a.state >= 2 and isnull(a.dr,0) = 0 and "
						+ "a.pk_assetsprop = '"
						+ tradevo.getPk_assetsprop()
						+ "' "
						+ "and  a.transtypecode = 'HV7A-0xx-01' "
						+ "and  a.pk_securities = '"
						+ tradevo.getPk_securities()
						+ "' "
						+ "and '"
						+ tradevo.getTrade_date()
						+ "' > trade_date and '"
						+ tradevo.getTrade_date().toString().substring(0, 10)
						+ "' < date_end group by   "
						+ "a.pk_group,a.pk_org,a.pk_org_v,a.pk_securities,a.pk_assetsprop,a.pk_capaccount,a.pk_glorgbook";
				ArrayList<ZqjdVO> nowBowResult = (ArrayList<ZqjdVO>) dao
						.executeQuery(str, new BeanListProcessor(ZqjdVO.class));

				if ((nowBowResult != null && nowBowResult.size() > 0)) {
					// ��ս�תӦ����Ϣ
					tradevo.setInterest(null);
					// ���ת��Ӧ����Ϣ
					costcalc.clearDistillInterest(costingtool);
				}

			}

			// ��ծ�� ����̨�� 20150417 ln
			IZqjdTallyService iZqjd = NCLocator.getInstance().lookup(
					IZqjdTallyService.class);
			iZqjd.deleteBuyTally(tradevo);

			// ����ѽ�ת�Ĺ��ʼ�ֵ 20150417 ln
			tradevo.setFairvalue(null);
			// ��չ��ʼ�ֵ����
			costcalc.clearDistillFv(costingtool);
		}
	}
}
