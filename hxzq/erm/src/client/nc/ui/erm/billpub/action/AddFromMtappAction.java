package nc.ui.erm.billpub.action;

import java.awt.event.ActionEvent;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.pubitf.erm.matterapp.IErmMatterAppBillQuery;
import nc.pubitf.erm.matterappctrl.IMatterAppCtrlService;
import nc.ui.arap.bx.BXQryTplUtil;
import nc.ui.er.util.BXUiUtil;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.erm.billpub.view.MatterSourceRefDlg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.uif2.actions.AddAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.erm.matterapp.MatterAppConvResVO;
import nc.vo.erm.matterapp.MatterAppConvVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.uif2.LoginContext;

public class AddFromMtappAction
  extends AddAction
{
  private static final long serialVersionUID = 1L;
  private BillForm editor;
  private QueryConditionDLG queryDialog;
  private MatterSourceRefDlg maSourceDlg;
  
  public AddFromMtappAction()
  {
    setCode("AddFromMtapp");
    setBtnName(NCLangRes4VoTransl.getNCLangRes().getStrByID("201107_0", "0201107-0142"));
    putValue("AcceleratorKey", null);
  }
  
  public void doAction(ActionEvent e)
    throws Exception
  {
    checkAddFromMtapp();
    if (1 == getQryDlg().showModal())
    {
      UIRefPane orgRefPane = BXQryTplUtil.getRefPaneByFieldCode(getQryDlg(), "pk_org");
      UIRefPane currRefPane = BXQryTplUtil.getRefPaneByFieldCode(getQryDlg(), "pk_currtype");
      String pk_org = ((String[])(String[])orgRefPane.getValueObj())[0];
      String pk_currtype = ((String[])(String[])currRefPane.getValueObj())[0];
      
      AggMatterAppVO[] aggMattervos = ((IErmMatterAppBillQuery)NCLocator.getInstance().lookup(IErmMatterAppBillQuery.class)).queryBillFromMtapp(getQryDlg().getWhereSQL(), ((ErmBillBillManageModel)getModel()).getCurrentBillTypeCode(), orgRefPane.getRefPK(), BXUiUtil.getPk_psndoc());
      

      getMaSourceDlg().setAggMtappVOS(aggMattervos);
      if (getMaSourceDlg().showModal() == 1)
      {
        AggMatterAppVO retvo = getMaSourceDlg().getRetvo();
        if ((retvo != null) && (retvo.getChildrenVO() != null) && (retvo.getChildrenVO().length > 0))
        {
          String pk_group = BXUiUtil.getPK_group();
          
          MatterAppConvResVO resVO = convertAggMattappVO(pk_org, retvo);
          
          JKBXVO vo = (JKBXVO)resVO.getBusiobj();
          vo.setMt_aggvos(new AggMatterAppVO[] { retvo });
          vo.getParentVO().setBzbm(pk_currtype);
          vo.getParentVO().setPk_org(pk_org);
          vo.getParentVO().setPk_group(pk_group);
          ((ErmBillBillForm)getEditor()).setResVO(resVO);
          super.doAction(e);
        }
      }
    }
  }
  
  private void checkAddFromMtapp()
    throws BusinessException
  {
    checkUser();
    
    checkBillType();
    
    checkBodyTemplate();
  }
  
  private void checkUser()
    throws BusinessException
  {
    String pkPsn = BXUiUtil.getPk_psndoc();
    if (pkPsn == null) {
      throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2011ermpub0316_0", "02011ermpub0316-0000"));
    }
  }
  
  private void checkBodyTemplate()
    throws BusinessException
  {
    BillTabVO[] billTabVOs = getEditor().getBillCardPanel().getBillData().getBillTabVOs(1);
    boolean isEist = true;
    if ((billTabVOs != null) && (billTabVOs.length > 0))
    {
      if ((billTabVOs[0].getMetadatapath() != null) && (!billTabVOs[0].getMetadatapath().equals("er_busitem")) && (!billTabVOs[0].getMetadatapath().equals("jk_busitem"))) {
        isEist = false;
      }
    }
    else {
      isEist = false;
    }
    if (!isEist) {
      throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("expensepub_0", "02011002-0171"));
    }
  }
  
  private void checkBillType()
    throws BusinessException
  {
    String selectBillTypeCode = ((ErmBillBillManageModel)getModel()).getSelectBillTypeCode();
    if (((ErmBillBillManageModel)getModel()).getCurrentDjlx(selectBillTypeCode).getFcbz().booleanValue()) {
      throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2011", "UPP2011-000171"));
    }
  }
  
  protected MatterAppConvResVO convertAggMattappVO(String pk_org, AggMatterAppVO retvo)
    throws BusinessException
  {
    String billTypeCode = ((ErmBillBillManageModel)getModel()).getCurrentBillTypeCode();
    
    IMatterAppCtrlService ctrlService = (IMatterAppCtrlService)NCLocator.getInstance().lookup(IMatterAppCtrlService.class);
    return ctrlService.getConvertBusiVOs(billTypeCode, pk_org, retvo);
  }
  
  protected MatterAppConvVO getMatterAppConvVO(String pk_org, String pk_group, AggMatterAppVO retvo)
  {
    MatterAppConvVO mcvo = new MatterAppConvVO();
    mcvo.setAggMapp(new AggMatterAppVO[] { retvo });
    String selectBillTypeCode = ((ErmBillBillManageModel)getModel()).getSelectBillTypeCode();
    mcvo.setBuBillType(selectBillTypeCode);
    mcvo.setPk_org(pk_org);
    mcvo.setPk_group(pk_group);
    String beanId = "d9b9f860-4dc7-47fa-a7d5-7a5d91f39290";
    String moneyField = "er_busitem.amount";
    String fkField = "er_busitem.pk_item";
    String srcBillTypeField = "er_busitem.srcbilltype";
    String srcTypeField = "er_busitem.srctype";
    
    String djdl = ((DjLXVO)((ErmBillBillManageModel)getEditor().getModel()).getBillTypeMapCache().get(selectBillTypeCode)).getDjdl();
    if ("jk".equals(djdl))
    {
      beanId = "e0499b58-c604-48a6-825b-9a7e4d6dacca";
      moneyField = "jk_busitem.amount";
      fkField = "jk_busitem.pk_item";
      srcBillTypeField = "jk_busitem.srcbilltype";
      srcTypeField = "jk_busitem.srctype";
    }
    mcvo.setBeanId(beanId);
    mcvo.setMoneyField(moneyField);
    mcvo.setFkField(fkField);
    mcvo.setSrcTradeTypeField(srcBillTypeField);
    mcvo.setSrcTypeField(srcTypeField);
    return mcvo;
  }
  
  protected MatterSourceRefDlg getMaSourceDlg()
  {
    if (this.maSourceDlg == null) {
      this.maSourceDlg = new MatterSourceRefDlg(getModel().getContext());
    }
    return this.maSourceDlg;
  }
  
  private QueryConditionDLG getQryDlg()
  {
    if (this.queryDialog == null)
    {
      TemplateInfo tempinfo = new TemplateInfo();
      tempinfo.setPk_Org(getModel().getContext().getPk_group());
      tempinfo.setFunNode("20110MTAMN");
      tempinfo.setUserid(getModel().getContext().getPk_loginUser());
      tempinfo.setNodekey("mtTOjkbxQry");
      


      this.queryDialog = new QueryConditionDLG(getEditor(), null, tempinfo, NCLangRes.getInstance().getStrByID("common", "UC000-0002782"));
      

      this.queryDialog.registerCriteriaEditorListener(new MaQueryCriteriaChangedListener(getModel()));
    }
    return this.queryDialog;
  }
  
  public BillForm getEditor()
  {
    return this.editor;
  }
  
  public void setEditor(BillForm editor)
  {
    this.editor = editor;
  }
}
