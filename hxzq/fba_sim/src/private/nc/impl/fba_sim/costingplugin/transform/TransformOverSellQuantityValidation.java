package nc.impl.fba_sim.costingplugin.transform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.fba_zqjd.tally.tool.QueryZqjdTallyTool;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.fba_zqjd.tally.QueryTallyParams;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 校验台账 ， 是否存在超买问题
 * @author Administrator
 *
 */
public class TransformOverSellQuantityValidation implements IValidationService {

	private ZqjdTallyVO tallyVO = null;

	@Override
	public void validate(Object obj) throws ValidationException {

		tallyVO = (ZqjdTallyVO) obj;

		List<String> vlist = new ArrayList<String>();

		List<ValidationFailure> failureList = new ArrayList<ValidationFailure>();

		// 查询参数
		QueryTallyParams params = new QueryTallyParams();
		params.setPk_capaccount(tallyVO.getPk_capaccount());
		params.setPk_assetsprop(tallyVO.getPk_assetsprop());
		params.setPk_glorgbook(tallyVO.getPk_glorgbook());
		params.setPk_org(tallyVO.getPk_org());
		params.setPk_securities(tallyVO.getPk_securities());
		params.setTrade_date(tallyVO.getTrade_date());

		Collection<ZqjdTallyVO> tallyVOs = QueryZqjdTallyTool.querySellBuyDetails(params);

		if (tallyVOs == null || tallyVOs.size() == 0) {
			ValidationFailure failure = new ValidationFailure();
			failure.setMessage("单据号：" + tallyVO.getSrcbillno() + ",负债转换负债转出数量大于负债数量！");
			failureList.add(failure);
			throw new ValidationException(failureList);
		}

		validateQuantity(tallyVOs, vlist);

		// 校验不通过信息封装&抛出
		if (null != vlist && vlist.size() > 0) {
			ValidationFailure failure = new ValidationFailure();
			failure.setMessage(vlist.toString());
			failureList.add(failure);
			throw new ValidationException(failureList);
		}

	}

	/**
	 * 卖出 +100
	 * 买入 -50
	 * 
	 * 
	 * @param tallyVOs
	 * @param vlist
	 */
	private void validateQuantity(Collection<ZqjdTallyVO> tallyVOs, List<String> vlist) {
		UFDouble result = UFDouble.ZERO_DBL;
		// 汇总台账数量
		for (ZqjdTallyVO tallyVO : tallyVOs) {
			result = SafeCompute.add(result, tallyVO.getBargain_num());
		}
		// 加上本次买入数量
		result = SafeCompute.add(result, tallyVO.getBargain_num());
		if (result.doubleValue() < 0) {
			vlist.add("单据号：" + tallyVO.getSrcbillno() + ",债券借贷交易（负债类）买入数量大于卖出数量！");
		}
	}

}
