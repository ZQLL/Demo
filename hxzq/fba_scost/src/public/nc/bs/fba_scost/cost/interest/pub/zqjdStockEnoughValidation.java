/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package nc.bs.fba_scost.cost.interest.pub;

import java.util.ArrayList;
import java.util.List;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

public class zqjdStockEnoughValidation implements IValidationService {
	private ZqjdVO zqjdVO;
	private StockBalanceVO stockBalanceVO;

	public zqjdStockEnoughValidation() {
		this.zqjdVO = null;
	}

	public void validate(Object obj) throws ValidationException {
		this.zqjdVO = ((ZqjdVO) obj);

		List vlist = new ArrayList();

		List failureList = new ArrayList();

		validateQuantity(vlist);

//		validateMoneySum(vlist);

		if ((null != vlist) && (vlist.size() > 0)) {
			ValidationFailure failure = new ValidationFailure();
			failure.setMessage(vlist.toString());
			failureList.add(failure);
			throw new ValidationException(failureList);
		}
	}

	private void validateMoneySum(List<String> vlist) {
		if (SafeCompute.sub(this.zqjdVO.getBargain_sum(),
				this.stockBalanceVO.getStocks_sum()).doubleValue() <= 0.0D)
			return;
		String errMsg = new String();
		errMsg = errMsg.concat("合同号：").concat(this.zqjdVO.getContractno())
				.concat(",归还金额超过库存金额！");
		vlist.add(errMsg);
	}

	private void validateQuantity(List<String> vlist) {
		if (SafeCompute.sub(this.zqjdVO.getBargain_num(),
				this.stockBalanceVO.getStocks_num()).doubleValue() <= 0.0D)
			return;
		String errMsg = new String();
		errMsg = errMsg.concat("合同号：").concat(this.zqjdVO.getContractno())
				.concat(",归还数量超过库存数量！");
		vlist.add(errMsg);
	}

	public StockBalanceVO getStockBalanceVO() {
		return this.stockBalanceVO;
	}

	public void setStockBalanceVO(StockBalanceVO stockBalanceVO) {
		this.stockBalanceVO = stockBalanceVO;
	}
}