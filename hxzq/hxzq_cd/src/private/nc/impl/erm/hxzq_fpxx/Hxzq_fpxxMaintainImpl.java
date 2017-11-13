package nc.impl.erm.hxzq_fpxx;

import nc.impl.pub.ace.AceHxzq_fpxxPubServiceImpl;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.vo.hxzq.fpxx.FpxxBillVO;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.bill.pagination.PaginationTransferObject;
import nc.vo.pubapp.bill.pagination.util.PaginationUtils;
import nc.impl.pubapp.pattern.data.bill.BillQuery;

public class Hxzq_fpxxMaintainImpl extends AceHxzq_fpxxPubServiceImpl implements nc.itf.erm.hxzq_fpxx.IHxzq_fpxxMaintain {

      @Override
    public void delete(FpxxBillVO[] vos) throws BusinessException {
        super.pubdeleteBills(vos);
    }
  
      @Override
    public FpxxBillVO[] insert(FpxxBillVO[] vos) throws BusinessException {
        return super.pubinsertBills(vos);
    }
    
      @Override
    public FpxxBillVO[] update(FpxxBillVO[] vos) throws BusinessException {
        return super.pubupdateBills(vos);    
    }
  

  @Override
  public FpxxBillVO[] query(IQueryScheme queryScheme)
      throws BusinessException {
      return super.pubquerybills(queryScheme);
  }







}
