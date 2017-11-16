package nc.ui.er.djlx;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.bd.ref.RefVO_mlang;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;

public class ErmTrantypeRefModel
  extends AbstractRefModel
{
  final String wherePart = " systemcode = 'erm' and pk_billtypecode like '26%' and istransaction = 'Y' and (pk_group='" + getPk_group() + "' or pk_org = '" + "GLOBLE00000000000000" + "')";
  
  public ErmTrantypeRefModel()
  {
    setAddEnableStateWherePart(true);
  }
  
  protected String getDisableDataWherePart(boolean isDisableDataShow)
  {
    if (isDisableDataShow) {
      return " islock = 'Y' or isnull(islock,'N') = 'N' ";
    }
    return " isnull(islock,'N') = 'N' ";
  }
  
  public int getDefaultFieldCount()
  {
    return getFieldCode().length;
  }
  
  public String[] getFieldCode()
  {
    return new String[] { "pk_billtypecode", "billtypename", "pk_billtypeid" };
  }
  
  public String getRefCodeField()
  {
    return "pk_billtypecode";
  }
  
  public String getRefNameField()
  {
    return "billtypename";
  }
  
  public String[] getFieldName()
  {
    return new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UCMD1-000172"), NCLangRes4VoTransl.getNCLangRes().getStrByID("ersetting_0", "02011001-0023"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UCMD1-000026") };
  }
  
  public String[] getHiddenFieldCode()
  {
    return new String[] { "pk_billtypeid" };
  }
  
  public String getPkFieldCode()
  {
    return "pk_billtypeid";
  }
  
  public String getRefTitle()
  {
    return NCLangRes4VoTransl.getNCLangRes().getStrByID("ersetting_0", "02011001-0024");
  }
  
  public String getTableName()
  {
    return " bd_billtype ";
  }
  
  public String getWherePart()
  {
    if (StringUtil.isEmpty(super.getWherePart())) {
      return this.wherePart;
    }
    return this.wherePart + " and " + super.getWherePart();
  }
  
  protected RefVO_mlang[] getRefVO_mlang()
  {
    RefVO_mlang refVO_mlang = new RefVO_mlang();
    refVO_mlang.setDirName("billtype");
    refVO_mlang.setFieldName("billtypename");
    refVO_mlang.setResIDFieldNames(new String[] { "pk_billtypecode" });
    refVO_mlang.setPreStr("D");
    
    return new RefVO_mlang[] { refVO_mlang };
  }
}
