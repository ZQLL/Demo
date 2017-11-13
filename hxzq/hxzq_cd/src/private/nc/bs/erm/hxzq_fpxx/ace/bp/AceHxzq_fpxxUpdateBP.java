package nc.bs.erm.hxzq_fpxx.ace.bp;

import nc.bs.erm.hxzq_fpxx.plugin.bpplugin.Hxzq_fpxxPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.hxzq.fpxx.FpxxBillVO;

/**
 * 修改保存的BP
 * 
 */
public class AceHxzq_fpxxUpdateBP {

  public FpxxBillVO[] update(FpxxBillVO[] bills, FpxxBillVO[] originBills) {

    		    // 调用修改模板
        UpdateBPTemplate<FpxxBillVO> bp = new UpdateBPTemplate<FpxxBillVO>(Hxzq_fpxxPluginPoint.UPDATE);

        // 执行前规则
        this.addBeforeRule(bp.getAroundProcesser());
        return bp.update(bills, originBills);
  }
  private void addBeforeRule(CompareAroundProcesser<FpxxBillVO> processer) {
   IRule<FpxxBillVO> rule=null;
					  				  				   				     rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
				    				   				    				     				    				   				   				   				    				     processer.addBeforeRule(rule);
				    				     				  				   				     ICompareRule<FpxxBillVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
				    				   				    				     ((nc.bs.pubapp.pub.rule.UpdateBillCodeRule)ruleCom).setCbilltype("2011");
				    				   				    				     ((nc.bs.pubapp.pub.rule.UpdateBillCodeRule)ruleCom).setCodeItem("vbillcode");
				    				   				    				     ((nc.bs.pubapp.pub.rule.UpdateBillCodeRule)ruleCom).setGroupItem("pk_group");
				    				   				    				     ((nc.bs.pubapp.pub.rule.UpdateBillCodeRule)ruleCom).setOrgItem("pk_org");
				    				   				   				   				    				     processer.addBeforeRule(ruleCom);
				    				     				  				   				     rule = new nc.bs.pubapp.pub.rule.FieldLengthCheckRule();
				    				   				    				     				    				   				   				   				    				     processer.addBeforeRule(rule);
				    				       }

}
