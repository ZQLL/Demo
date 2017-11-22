/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package nc.bs.fba_scost.cost.interest.pub;

import java.util.*;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.tool.fba_zqjd.pub.DateTool;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

public class zqjdOverReturnValidation implements IValidationService {
	private ZqjdVO zqjdVO;

	public zqjdOverReturnValidation() {
		this.zqjdVO = null;
	}

	@Override
	public void validate(Object obj) throws ValidationException {
		this.zqjdVO = ((ZqjdVO) obj);

		List vlist = new ArrayList();
		List failureList = new ArrayList();
		BaseDAO baseDAO = new BaseDAO();
		try {
			StringBuffer qry = new StringBuffer();
			qry.append(" pk_org ='").append(this.zqjdVO.getPk_org())
					.append("'");
			qry.append(" and pk_glorgbook ='")
					.append(this.zqjdVO.getPk_glorgbook()).append("'");
			qry.append(" and contractno= '")
					.append(this.zqjdVO.getContractno()).append("'");
			qry.append(" and pk_assetsprop ='")
					.append(this.zqjdVO.getPk_assetsprop()).append("'");
			qry.append(" and pk_capaccount ='")
					.append(this.zqjdVO.getPk_capaccount()).append("'");
			qry.append(" and trade_date<= '")
					.append(DateTool.asEnd(this.zqjdVO.getTrade_date()))
					.append("'");
			qry.append(" and state!=").append(
					nc.vo.fba_sec.pub.SystemConst.statecode[2]);
			qry.append(" and isnull(dr,0) = 0 ");

			Collection vos = baseDAO.retrieveByClause(ZqjdVO.class,
					qry.toString());

			if (vos == null) {
				ValidationFailure failure = new ValidationFailure();
				failure.setMessage("合同号：" + this.zqjdVO.getContractno()
						+ ",查询债券借贷借入与归还信息失败！");
				failureList.add(failure);
				throw new ValidationException(failureList);
			}

			validateQuantity(vos, vlist);

			if ((null != vlist) && (vlist.size() > 0)) {
				ValidationFailure failure = new ValidationFailure();
				failure.setMessage(vlist.toString());
				failureList.add(failure);
				throw new ValidationException(failureList);
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage());
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	private void validateQuantity(Collection<ZqjdVO> vos, List<String> vlist) {
		if (SafeCompute.sub(getOutQuantity(vos), getInQuantity(vos)).toDouble()
				.doubleValue() <= 0.0D) {
			return;
		}
		String errMsg = new String();
		errMsg = errMsg.concat("合同号：").concat(this.zqjdVO.getContractno())
				.concat(",归还数量超过借入数量！");
		vlist.add(errMsg);
	}

	private UFDouble getOutQuantity(Collection<ZqjdVO> vos) {
		UFDouble result = UFDouble.ZERO_DBL;
		if (vos == null) {
			return result;
		}
		for (ZqjdVO zqjdVO : vos) {
			if ("HV7A-0xx-02".equals(zqjdVO.getTranstypecode())) {
				result = SafeCompute.add(result, zqjdVO.getBargain_num());
			}
		}
		return result;
	}

	private UFDouble getInQuantity(Collection<ZqjdVO> vos) {
		UFDouble result = UFDouble.ZERO_DBL;
		if (vos == null)
			return result;
		for (ZqjdVO zqjdVO : vos) {
			if ("HV7A-0xx-01".equals(zqjdVO.getTranstypecode())) {
				result = SafeCompute.add(result, zqjdVO.getBargain_num());
			}
		}
		return result;
	}
}