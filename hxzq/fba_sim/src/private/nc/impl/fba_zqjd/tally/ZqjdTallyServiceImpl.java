package nc.impl.fba_zqjd.tally;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_zqjd.tally.tool.QueryZqjdTallyTool;
import nc.bs.uif2.validation.ValidationException;
import nc.impl.fba_zqjd.pub.validation.OverSellQuantityValidation;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_zqjd.tally.IZqjdTallyService;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.fba_sec.pub.SystemConst;
import nc.vo.fba_zqjd.tally.QueryTallyParams;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

import org.apache.commons.lang.StringUtils;

public class ZqjdTallyServiceImpl implements IZqjdTallyService {

	private BaseDAO baseDAO = null;

	
	@Override
	public void saveSellTally(ITrade_Data tradeVO) throws BusinessException {

		ZqjdTallyVO tallyVO = buildBaseTallyVOFromTradeVO(tradeVO);

		getBaseDAO().insertVO(tallyVO);

	}

	
	@Override
	public void saveBuyTally(ITrade_Data tradeVO, UFDouble sellscost)
			throws BusinessException {

		ZqjdTallyVO tallyVO = buildBaseTallyVOFromTradeVO(tradeVO);

		UFDouble factor = new UFDouble(-1);

		tallyVO.setBargain_num(SafeCompute.multiply(tallyVO.getBargain_num(),
				factor));
		tallyVO.setBargain_sum(sellscost);

		checkQuantity(tallyVO);

		getBaseDAO().insertVO(tallyVO);
	}

	private void checkQuantity(ZqjdTallyVO tallyVO) throws ValidationException {

		OverSellQuantityValidation quantityValidation = new OverSellQuantityValidation();

		quantityValidation.validate(tallyVO);
	}

	
	@Override
	public void deleteSellTally(ITrade_Data tradeVO) throws BusinessException {

		deleteTally(tradeVO);

	}

	
	@Override
	public void deleteBuyTally(ITrade_Data tradeVO) throws BusinessException {

		deleteTally(tradeVO);

	}

	private void deleteTally(ITrade_Data tradeVO) throws DAOException {

		if (tradeVO == null)
			return;
		if (StringUtils.isEmpty(tradeVO.getBillno()))
			return;

		StringBuffer condition = new StringBuffer();
		condition.append(" srcbillno = '").append(tradeVO.getBillno())
				.append("'");
		getBaseDAO().deleteByClause(ZqjdTallyVO.class, condition.toString());

	}

	private ZqjdTallyVO buildBaseTallyVOFromTradeVO(ITrade_Data tradeVO) {

		ZqjdTallyVO tallyVO = new ZqjdTallyVO();
		tallyVO.setPk_securities(tradeVO.getPk_securities());
		// 状态
		tallyVO.setState(SystemConst.statecode[1]);
		// 交易日期
		tallyVO.setTrade_date(tradeVO.getTrade_date());
		// 资金账号
		tallyVO.setPk_capaccount(tradeVO.getPk_capaccount());
		// 数量金额
		tallyVO.setPk_currtype(nc.vo.fba_scost.cost.pub.SystemConst.Pk_Currtype); // 人民币
		tallyVO.setBargain_num(tradeVO.getBargain_num());
		tallyVO.setBargain_sum(tradeVO.getBargain_sum());
		// 组织信息
		tallyVO.setPk_group(tradeVO.getPk_group());
		tallyVO.setPk_org(tradeVO.getPk_org());
		tallyVO.setPk_org_v(tradeVO.getPk_org_v());
		tallyVO.setPk_glorgbook(tradeVO.getPk_glorgbook());
		// 资产属性\默认库存
		// tallyVO.setPk_assetsprop(nc.vo.fba_scost.cost.pub.SystemConst.Pk_AssetsProp1);
		tallyVO.setPk_assetsprop(tradeVO.getPk_assetsprop());
		tallyVO.setPk_stocksort(nc.vo.fba_scost.cost.pub.SystemConst.pk_defaultstocksort);
		// 来源单据号
		tallyVO.setSrcbillno(tradeVO.getBillno());

		return tallyVO;
	}

	private BaseDAO getBaseDAO() {

		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;

	}

	@Override
	
	public ZqjdTallyVO gatherSellBuyBalance(ITrade_Data tradeVO)
			throws BusinessException {
		// 查询参数
		QueryTallyParams params = new QueryTallyParams();
		params.setPk_capaccount(tradeVO.getPk_capaccount());
		params.setPk_assetsprop(tradeVO.getPk_assetsprop());
		params.setPk_glorgbook(tradeVO.getPk_glorgbook());
		params.setPk_org(tradeVO.getPk_org());
		params.setPk_securities(tradeVO.getPk_securities());
		params.setTrade_date(tradeVO.getTrade_date());

		return QueryZqjdTallyTool.querySellBuyBalance(params);
	}

}
