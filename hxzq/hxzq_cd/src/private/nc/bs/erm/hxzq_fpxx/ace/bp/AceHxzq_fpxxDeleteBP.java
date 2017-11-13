package nc.bs.erm.hxzq_fpxx.ace.bp;

import nc.bs.erm.hxzq_fpxx.plugin.bpplugin.Hxzq_fpxxPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.hxzq.fpxx.FpxxBillVO;

/**
 * 标准单据删除BP
 */
public class AceHxzq_fpxxDeleteBP {

  public void delete(FpxxBillVO[] bills) {

      DeleteBPTemplate<FpxxBillVO> bp =
          new DeleteBPTemplate<FpxxBillVO>(Hxzq_fpxxPluginPoint.DELETE);
      bp.delete(bills);
  }
}
