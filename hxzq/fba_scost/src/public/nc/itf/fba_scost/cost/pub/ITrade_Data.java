package nc.itf.fba_scost.cost.pub;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;

/**
 * 交易数据----信息接口
 * 
 */
public interface ITrade_Data {

	public UFDouble getAccrual_sum();// 买、卖应收利息

	public void setAccrual_sum(UFDouble accrual_sum);

	public UFDouble getBargain_num();// 成交数量

	public void setBargain_num(UFDouble bargain_num);

	public UFDouble getBargain_sum();// 成交金额

	public void setBargain_sum(UFDouble bargain_sum);

	public UFLiteralDate getBegin_date();// 限售开始日期

	public void setBegin_date(UFLiteralDate begin_date);

	public UFLiteralDate getEnd_date();// 限售结束日期

	public void setEnd_date(UFLiteralDate end_date);

	public String getBillno();// 单据号

	public void setBillno(String billno);

	public String getBilltypecode();// 单据类型

	public void setBilltypecode(String billtypecode);

	public UFDouble getFact_sum();// 实际收付

	public void setFact_sum(UFDouble fact_sum);

	public UFDouble getFairvalue();// 结转公允值

	public void setFairvalue(UFDouble fairvalue);

	public UFDouble getInterest();// 结转应收利息

	public void setInterest(UFDouble interest);

	public String getPk_assetsprop();// 资产属性

	public void setPk_assetsprop(String pk_assetsprop);

	public String getPk_billtype();// 单据类型pk

	public void setPk_billtype(String pk_billtype);

	public String getPk_capaccount(); // 资金账号

	public void setPk_capaccount(String pk_capaccount);

	public String getPk_client();// 往来单位

	public void setPk_client(String pk_client);

	public String getPk_glorgbook();// 核算账簿

	public void setPk_glorgbook(String pk_glorgbook);

	public String getPk_group();// 集团

	public void setPk_group(String pk_group);

	public String getPk_operatesite();// 分户地点

	public void setPk_operatesite(String pk_operatesite);

	public String getPk_org();// 组织

	public void setPk_org(String pk_org);

	public String getPk_org_v(); // 组织版本

	public void setPk_org_v(String pk_org_v);

	public String getPk_partnaccount();// 股东账号

	public void setPk_partnaccount(String pk_partnaccount);

	public String getPk_securities();// 交易证券

	public void setPk_securities(String pk_securities);

	public String getPk_selfsgroup(); // 业务小组

	public void setPk_selfsgroup(String pk_selfsgroup);

	public String getPk_stocksort();// 库存组织

	public void setPk_stocksort(String pk_stocksort);// 库存组织

	public String getPk_transtype();// 交易类型pk

	public void setPk_transtype(String pk_transtype);

	public Integer getState();// 状态

	public void setState(Integer state);

	public UFDate getTrade_date();// 交易日期

	public void setTrade_date(UFDate trade_date);

	public String getTranstypecode();// 交易类型

	public void setTranstypecode(String transtypecode);

	public Integer getGenway();// [转出 ---1]、[转入---- 2]、[转出转入 ----3]

	public UFLiteralDate getHr_begin_date();

	public void setHr_begin_date(UFLiteralDate hr_begin_date);

	public UFLiteralDate getHr_end_date();

	public void setHr_end_date(UFLiteralDate hr_end_date);

	public String getHr_pk_assetsprop();

	public void setHr_pk_assetsprop(String hr_pk_assetsprop);

	public String getHr_pk_capaccount();

	public void setHr_pk_capaccount(String hr_pk_capaccount);

	public String getHr_pk_client();

	public void setHr_pk_client(String hr_pk_client);

	public String getHr_pk_operatesite();

	public void setHr_pk_operatesite(String hr_pk_operatesite);

	public String getHr_pk_partnaccount();

	public void setHr_pk_partnaccount(String hr_pk_partnaccount);

	public String getHr_pk_securities();

	public void setHr_pk_securities(String hr_Pk_securities);

	public String getHr_pk_selfsgroup();

	public void setHr_pk_selfsgroup(String hr_pk_selfsgroup);

	public String getHr_pk_stocksort();

	public void setHr_pk_stocksort(String hr_pk_stocksort);

	public UFDouble getHr_bargain_num();

	public void setHr_bargain_num(UFDouble bargain_num);

}
