package nc.itf.erm.hxzq_fpxx;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.hxzq.fpxx.FpxxBillVO;
import nc.vo.pub.BusinessException;

public interface IHxzq_fpxxMaintain {

    public void delete(FpxxBillVO[] vos) throws BusinessException ;

    public FpxxBillVO[] insert(FpxxBillVO[] vos) throws BusinessException ;
  
    public FpxxBillVO[] update(FpxxBillVO[] vos) throws BusinessException ;


    public FpxxBillVO[] query(IQueryScheme queryScheme)
      throws BusinessException;

}
