package nc.bs.erm.hxzq_fpxx.ace.bp;

import nc.bs.erm.hxzq_fpxx.plugin.bpplugin.Hxzq_fpxxPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.hxzq.fpxx.FpxxBillVO;

/**
 * 标准单据新增BP
 */
public class AceHxzq_fpxxInsertBP {

  public FpxxBillVO[] insert(FpxxBillVO[] bills) {

    InsertBPTemplate<FpxxBillVO> bp =
        new InsertBPTemplate<FpxxBillVO>(Hxzq_fpxxPluginPoint.INSERT);
    this.addBeforeRule(bp.getAroundProcesser());
    return bp.insert(bills);
  }
  /**
   * 新增前规则
   * 
   * @param processor
   */
  private void addBeforeRule(AroundProcesser<FpxxBillVO> processer) {
   IRule<FpxxBillVO> rule=null;
				  				   				     rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
				   				   				    				   				   
				   				     processer.addBeforeRule(rule);
				   				   
  				   				     rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
				   				   				    				     ((nc.bs.pubapp.pub.rule.CreateBillCodeRule)rule).setCbilltype("2011");
				    				   				    				     ((nc.bs.pubapp.pub.rule.CreateBillCodeRule)rule).setCodeItem("vbillcode");
				    				   				    				     ((nc.bs.pubapp.pub.rule.CreateBillCodeRule)rule).setGroupItem("pk_group");
				    				   				    				     ((nc.bs.pubapp.pub.rule.CreateBillCodeRule)rule).setOrgItem("pk_org");
				    				   				   
				   				     processer.addBeforeRule(rule);
				   				   
  				   				     rule = new nc.bs.pubapp.pub.rule.FieldLengthCheckRule();
				   				   				    				   				   
				   				     processer.addBeforeRule(rule);
				   				   
    }
}
