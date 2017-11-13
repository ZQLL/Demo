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
 * У��̨�� �� �Ƿ���ڳ�������
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

		// ��ѯ����
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
			failure.setMessage("���ݺţ�" + tallyVO.getSrcbillno() + ",��ծת����ծת���������ڸ�ծ������");
			failureList.add(failure);
			throw new ValidationException(failureList);
		}

		validateQuantity(tallyVOs, vlist);

		// У�鲻ͨ����Ϣ��װ&�׳�
		if (null != vlist && vlist.size() > 0) {
			ValidationFailure failure = new ValidationFailure();
			failure.setMessage(vlist.toString());
			failureList.add(failure);
			throw new ValidationException(failureList);
		}

	}

	/**
	 * ���� +100
	 * ���� -50
	 * 
	 * 
	 * @param tallyVOs
	 * @param vlist
	 */
	private void validateQuantity(Collection<ZqjdTallyVO> tallyVOs, List<String> vlist) {
		UFDouble result = UFDouble.ZERO_DBL;
		// ����̨������
		for (ZqjdTallyVO tallyVO : tallyVOs) {
			result = SafeCompute.add(result, tallyVO.getBargain_num());
		}
		// ���ϱ�����������
		result = SafeCompute.add(result, tallyVO.getBargain_num());
		if (result.doubleValue() < 0) {
			vlist.add("���ݺţ�" + tallyVO.getSrcbillno() + ",ծȯ������ף���ծ�ࣩ����������������������");
		}
	}

}
