package nc.impl.fba_fund.plugin.approve;

import nc.impl.fba_fund.plugin.abstractapprove.AbstractLendingApprove;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_fund.fundtally.FundTallyVO;
import nc.vo.fba_fund.interbanklending.InterbankLendingVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

public class LendingApprove extends AbstractLendingApprove {
	@Override
	protected void calculateWhenCheck(ICostingTool costingtool,
			InterbankLendingVO tradevo) throws Exception {
		boolean bln = "HV8A-0xx-02".equals(tradevo.getTranstypecode());
		// boolean bln = "HV8A-0xx-03".equals(tradevo.getTranstypecode());
		ICostBalanceTool tool = costingtool.getBalancetool();

		String trade_date = costingtool.getCurrdate();
		String pk_glorgbook = costingtool.getCostParaVO().getCostplanvo()
				.getPk_glorgbook();

		String[] costFieldArray = costingtool.getCostParaVO().getCostplanvo()
				.getFundCostFieldArray();
		String key = VOUtil.getCombinesKey(tradevo, costFieldArray);

		key = costingtool.getCurrbilltypegroupvo().getPk_billtypegroup() + key;

		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(key);
		queryvo.setPk_assetsprop(tradevo.getPk_assetsprop());
		queryvo.setPk_stocksort(tradevo.getPk_stocksort());
		queryvo.setTrade_date(trade_date);

		InventoryInfoVO inventoryInfoVO = tool.getInventoryInfoVO(queryvo,
				costingtool);
		if (inventoryInfoVO == null) {
			if (bln) {
				costingtool.handleException(tradevo, null,
						nc.vo.fba_fund.pub.SystemConst.error[0]);
				return;
			}
			inventoryInfoVO = tool.getInventoryInfoVOByVO(tradevo, costingtool);
		}
		calInventory(inventoryInfoVO, tradevo, trade_date);

		buildTally(tradevo, pk_glorgbook, new UFDate(trade_date));

		tool.updateInventoryInfoVO(queryvo, inventoryInfoVO);
	}

	@Override
	protected void calculateWhenUnCheck(ICostingTool costingtool,
			InterbankLendingVO vo) throws Exception {
		String wherestr = " pk_opbill='" + vo.getPk_interbanklending()
				+ "' and isnull(dr,0)=0";
		getBaseDao().deleteByClause(FundTallyVO.class, wherestr);
		String transtypecode = vo.getTranstypecode();
		if ("HV8A-0xx-02".equals(transtypecode)) {
			vo.setAttributeValue("dr", Integer.valueOf(1));
		}
	}

	private void calInventory(InventoryInfoVO inventoryInfoVO,
			InterbankLendingVO tradevo, String trade_date) throws Exception {
		PubMethod pm = PubMethod.getInstance();

		String transtype = tradevo.getTranstypecode();
		if (transtype != null) {
			if (transtype.equals("HV8A-0xx-02")) {
				UFDouble denomination = tradevo.getDenomination();

				UFDouble stocks_sum = inventoryInfoVO.getStocks_sum();
				if (pm.sub(stocks_sum, denomination).compareTo(new UFDouble(0)) < 0) {
					throw new BusinessException(" 交易日期:" + trade_date + " "
							+ nc.vo.fba_fund.pub.SystemConst.error[1]);
				}
				inventoryInfoVO.setStocks_sum(pm.sub(
						inventoryInfoVO.getStocks_sum(),
						tradevo.getDenomination()));

				inventoryInfoVO.setTranstypecode(tradevo.getTranstypecode()
						.replace("01", "02"));
			} else {
				inventoryInfoVO.setStocks_sum(pm.add(
						inventoryInfoVO.getStocks_sum(),
						tradevo.getDenomination()));
			}
		}
	}

	private void buildTally(InterbankLendingVO vo, String pk_glorgbook,
			UFDate trade_date) throws Exception {
		FundTallyVO fundTallyVO = new FundTallyVO();

		String transtypecode = vo.getTranstypecode();
		String num = transtypecode.substring(10, 11);
		if ("2".equals(num)) {
			fundTallyVO.setIsdq(new UFBoolean(true));
		} else {
			fundTallyVO.setIsdq(new UFBoolean(false));
		}
		fundTallyVO.setDenomination(vo.getDenomination());

		fundTallyVO.setExcutefacerate(vo.getExecutefacerate());

		fundTallyVO.setProductorcounterparty(vo.getProductorcounterparty());

		fundTallyVO.setMarket(vo.getMarket());

		fundTallyVO.setBegindate(vo.getStartdate().asBegin());

		fundTallyVO.setEnddate(vo.getEnddate().asBegin());

		fundTallyVO.setBegindistill_date(vo.getStartdate());

		fundTallyVO.setOperdate(vo.getTrade_date().asBegin());

		fundTallyVO.setPk_opbill(vo.getPk_interbanklending());

		fundTallyVO.setYearcaldays(vo.getYearcaldays());

		fundTallyVO.setPk_optype(vo.getBilltypecode());

		fundTallyVO.setTimelimit(vo.getTimelimit());

		fundTallyVO.setContractno(vo.getContractno());

		fundTallyVO.setTrade_date(vo.getTrade_date());

		fundTallyVO.setFundtype(vo.getFundtype());

		fundTallyVO.setRealrate(vo.getRealrate());

		fundTallyVO.setManagedept(vo.getManagedept());

		fundTallyVO.setPaytype(vo.getPaytype());

		fundTallyVO.setPk_group(vo.getPk_group());
		fundTallyVO.setPk_org(vo.getPk_org());
		fundTallyVO.setPk_org_v(vo.getPk_org_v());
		fundTallyVO.setPk_bourse(vo.getPk_bourse());

		fundTallyVO.setOpercode(vo.getBillno());
		fundTallyVO.setPk_billtype(vo.getPk_billtype());
		fundTallyVO.setPk_capaccount(vo.getPk_capaccount());
		fundTallyVO.setPk_operatesite(vo.getPk_operatesite());
		fundTallyVO.setPk_client(vo.getPk_client());
		fundTallyVO.setPk_selfsgroup(vo.getPk_selfsgroup());
		fundTallyVO.setPk_glorgbook(pk_glorgbook);
		fundTallyVO.setPk_busitype(vo.getPk_busitype());
		fundTallyVO.setPk_currtype(vo.getPk_currtype());
		fundTallyVO.setOperindex(Integer.valueOf(0));
		fundTallyVO.setAttributeValue("dr", Integer.valueOf(0));

		getBaseDao().insertVO(fundTallyVO);
	}
}
