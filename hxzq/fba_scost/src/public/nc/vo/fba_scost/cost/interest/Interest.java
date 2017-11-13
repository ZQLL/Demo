package nc.vo.fba_scost.cost.interest;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class Interest extends SuperVO {
/**
*创建时间
*/
public static final String CREATIONTIME="creationtime";
/**
*创建人
*/
public static final String CREATOR="creator";
/**
*到息日
*/
public static final String ENDDATE="enddate";
/**
*发行日
*/
public static final String ISSUEDATE="issuedate";
/**
*修改时间
*/
public static final String MODIFIEDTIME="modifiedtime";
/**
*修改人
*/
public static final String MODIFIER="modifier";
/**
*自定义数值1
*/
public static final String NDEF1="ndef1";
/**
*自定义数值2
*/
public static final String NDEF2="ndef2";
/**
*自定义数值3
*/
public static final String NDEF3="ndef3";
/**
*自定义数值4
*/
public static final String NDEF4="ndef4";
/**
*自定义数值5
*/
public static final String NDEF5="ndef5";
/**
*付息方式
*/
public static final String PAYTYPE="paytype";
/**
*计息总期数
*/
public static final String PERIODSUM="periodsum";
/**
*付息周期
*/
public static final String PERIODTYPE="periodtype";
/**
*集团
*/
public static final String PK_GROUP="pk_group";
/**
*主键
*/
public static final String PK_INTEREST="pk_interest";
/**
*组织
*/
public static final String PK_ORG="pk_org";
/**
*组织版本
*/
public static final String PK_ORG_V="pk_org_v";
/**
*证券档案
*/
public static final String PK_SECURITIES="pk_securities";
/**
*收益方式
*/
public static final String PROFITTYPE="profittype";
/**
*利率种类
*/
public static final String RATETYPE="ratetype";
/**
*起息日
*/
public static final String STARTDATE="startdate";
/**
*时间戳
*/
public static final String TS="ts";
/**
*自定义项1
*/
public static final String VDEF1="vdef1";
/**
*自定义项2
*/
public static final String VDEF2="vdef2";
/**
*自定义项3
*/
public static final String VDEF3="vdef3";
/**
*自定义项4
*/
public static final String VDEF4="vdef4";
/**
*自定义项5
*/
public static final String VDEF5="vdef5";
/**
*票面利率
*/
public static final String YEARRATE="yearrate";
/** 
* 获取创建时间
*
* @return 创建时间
*/
public UFDateTime getCreationtime () {
return (UFDateTime) this.getAttributeValue( Interest.CREATIONTIME);
 } 

/** 
* 设置创建时间
*
* @param creationtime 创建时间
*/
public void setCreationtime ( UFDateTime creationtime) {
this.setAttributeValue( Interest.CREATIONTIME,creationtime);
 } 

/** 
* 获取创建人
*
* @return 创建人
*/
public String getCreator () {
return (String) this.getAttributeValue( Interest.CREATOR);
 } 

/** 
* 设置创建人
*
* @param creator 创建人
*/
public void setCreator ( String creator) {
this.setAttributeValue( Interest.CREATOR,creator);
 } 

/** 
* 获取到息日
*
* @return 到息日
*/
public UFDate getEnddate () {
return (UFDate) this.getAttributeValue( Interest.ENDDATE);
 } 

/** 
* 设置到息日
*
* @param enddate 到息日
*/
public void setEnddate ( UFDate enddate) {
this.setAttributeValue( Interest.ENDDATE,enddate);
 } 

/** 
* 获取发行日
*
* @return 发行日
*/
public UFDate getIssuedate () {
return (UFDate) this.getAttributeValue( Interest.ISSUEDATE);
 } 

/** 
* 设置发行日
*
* @param issuedate 发行日
*/
public void setIssuedate ( UFDate issuedate) {
this.setAttributeValue( Interest.ISSUEDATE,issuedate);
 } 

/** 
* 获取修改时间
*
* @return 修改时间
*/
public UFDateTime getModifiedtime () {
return (UFDateTime) this.getAttributeValue( Interest.MODIFIEDTIME);
 } 

/** 
* 设置修改时间
*
* @param modifiedtime 修改时间
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.setAttributeValue( Interest.MODIFIEDTIME,modifiedtime);
 } 

/** 
* 获取修改人
*
* @return 修改人
*/
public String getModifier () {
return (String) this.getAttributeValue( Interest.MODIFIER);
 } 

/** 
* 设置修改人
*
* @param modifier 修改人
*/
public void setModifier ( String modifier) {
this.setAttributeValue( Interest.MODIFIER,modifier);
 } 

/** 
* 获取自定义数值1
*
* @return 自定义数值1
*/
public UFDouble getNdef1 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF1);
 } 

/** 
* 设置自定义数值1
*
* @param ndef1 自定义数值1
*/
public void setNdef1 ( UFDouble ndef1) {
this.setAttributeValue( Interest.NDEF1,ndef1);
 } 

/** 
* 获取自定义数值2
*
* @return 自定义数值2
*/
public UFDouble getNdef2 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF2);
 } 

/** 
* 设置自定义数值2
*
* @param ndef2 自定义数值2
*/
public void setNdef2 ( UFDouble ndef2) {
this.setAttributeValue( Interest.NDEF2,ndef2);
 } 

/** 
* 获取自定义数值3
*
* @return 自定义数值3
*/
public UFDouble getNdef3 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF3);
 } 

/** 
* 设置自定义数值3
*
* @param ndef3 自定义数值3
*/
public void setNdef3 ( UFDouble ndef3) {
this.setAttributeValue( Interest.NDEF3,ndef3);
 } 

/** 
* 获取自定义数值4
*
* @return 自定义数值4
*/
public UFDouble getNdef4 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF4);
 } 

/** 
* 设置自定义数值4
*
* @param ndef4 自定义数值4
*/
public void setNdef4 ( UFDouble ndef4) {
this.setAttributeValue( Interest.NDEF4,ndef4);
 } 

/** 
* 获取自定义数值5
*
* @return 自定义数值5
*/
public UFDouble getNdef5 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF5);
 } 

/** 
* 设置自定义数值5
*
* @param ndef5 自定义数值5
*/
public void setNdef5 ( UFDouble ndef5) {
this.setAttributeValue( Interest.NDEF5,ndef5);
 } 

/** 
* 获取付息方式
*
* @return 付息方式
* @see String
*/
public String getPaytype () {
return (String) this.getAttributeValue( Interest.PAYTYPE);
 } 

/** 
* 设置付息方式
*
* @param paytype 付息方式
* @see String
*/
public void setPaytype ( String paytype) {
this.setAttributeValue( Interest.PAYTYPE,paytype);
 } 

/** 
* 获取计息总期数
*
* @return 计息总期数
*/
public Integer getPeriodsum () {
return (Integer) this.getAttributeValue( Interest.PERIODSUM);
 } 

/** 
* 设置计息总期数
*
* @param periodsum 计息总期数
*/
public void setPeriodsum ( Integer periodsum) {
this.setAttributeValue( Interest.PERIODSUM,periodsum);
 } 

/** 
* 获取付息周期
*
* @return 付息周期
* @see String
*/
public String getPeriodtype () {
return (String) this.getAttributeValue( Interest.PERIODTYPE);
 } 

/** 
* 设置付息周期
*
* @param periodtype 付息周期
* @see String
*/
public void setPeriodtype ( String periodtype) {
this.setAttributeValue( Interest.PERIODTYPE,periodtype);
 } 

/** 
* 获取集团
*
* @return 集团
*/
public String getPk_group () {
return (String) this.getAttributeValue( Interest.PK_GROUP);
 } 

/** 
* 设置集团
*
* @param pk_group 集团
*/
public void setPk_group ( String pk_group) {
this.setAttributeValue( Interest.PK_GROUP,pk_group);
 } 

/** 
* 获取主键
*
* @return 主键
*/
public String getPk_interest () {
return (String) this.getAttributeValue( Interest.PK_INTEREST);
 } 

/** 
* 设置主键
*
* @param pk_interest 主键
*/
public void setPk_interest ( String pk_interest) {
this.setAttributeValue( Interest.PK_INTEREST,pk_interest);
 } 

/** 
* 获取组织
*
* @return 组织
*/
public String getPk_org () {
return (String) this.getAttributeValue( Interest.PK_ORG);
 } 

/** 
* 设置组织
*
* @param pk_org 组织
*/
public void setPk_org ( String pk_org) {
this.setAttributeValue( Interest.PK_ORG,pk_org);
 } 

/** 
* 获取组织版本
*
* @return 组织版本
*/
public String getPk_org_v () {
return (String) this.getAttributeValue( Interest.PK_ORG_V);
 } 

/** 
* 设置组织版本
*
* @param pk_org_v 组织版本
*/
public void setPk_org_v ( String pk_org_v) {
this.setAttributeValue( Interest.PK_ORG_V,pk_org_v);
 } 

/** 
* 获取证券档案
*
* @return 证券档案
*/
public String getPk_securities () {
return (String) this.getAttributeValue( Interest.PK_SECURITIES);
 } 

/** 
* 设置证券档案
*
* @param pk_securities 证券档案
*/
public void setPk_securities ( String pk_securities) {
this.setAttributeValue( Interest.PK_SECURITIES,pk_securities);
 } 

/** 
* 获取收益方式
*
* @return 收益方式
* @see String
*/
public String getProfittype () {
return (String) this.getAttributeValue( Interest.PROFITTYPE);
 } 

/** 
* 设置收益方式
*
* @param profittype 收益方式
* @see String
*/
public void setProfittype ( String profittype) {
this.setAttributeValue( Interest.PROFITTYPE,profittype);
 } 

/** 
* 获取利率种类
*
* @return 利率种类
* @see String
*/
public String getRatetype () {
return (String) this.getAttributeValue( Interest.RATETYPE);
 } 

/** 
* 设置利率种类
*
* @param ratetype 利率种类
* @see String
*/
public void setRatetype ( String ratetype) {
this.setAttributeValue( Interest.RATETYPE,ratetype);
 } 

/** 
* 获取起息日
*
* @return 起息日
*/
public UFDate getStartdate () {
return (UFDate) this.getAttributeValue( Interest.STARTDATE);
 } 

/** 
* 设置起息日
*
* @param startdate 起息日
*/
public void setStartdate ( UFDate startdate) {
this.setAttributeValue( Interest.STARTDATE,startdate);
 } 

/** 
* 获取时间戳
*
* @return 时间戳
*/
public UFDateTime getTs () {
return (UFDateTime) this.getAttributeValue( Interest.TS);
 } 

/** 
* 设置时间戳
*
* @param ts 时间戳
*/
public void setTs ( UFDateTime ts) {
this.setAttributeValue( Interest.TS,ts);
 } 

/** 
* 获取自定义项1
*
* @return 自定义项1
*/
public String getVdef1 () {
return (String) this.getAttributeValue( Interest.VDEF1);
 } 

/** 
* 设置自定义项1
*
* @param vdef1 自定义项1
*/
public void setVdef1 ( String vdef1) {
this.setAttributeValue( Interest.VDEF1,vdef1);
 } 

/** 
* 获取自定义项2
*
* @return 自定义项2
*/
public String getVdef2 () {
return (String) this.getAttributeValue( Interest.VDEF2);
 } 

/** 
* 设置自定义项2
*
* @param vdef2 自定义项2
*/
public void setVdef2 ( String vdef2) {
this.setAttributeValue( Interest.VDEF2,vdef2);
 } 

/** 
* 获取自定义项3
*
* @return 自定义项3
*/
public String getVdef3 () {
return (String) this.getAttributeValue( Interest.VDEF3);
 } 

/** 
* 设置自定义项3
*
* @param vdef3 自定义项3
*/
public void setVdef3 ( String vdef3) {
this.setAttributeValue( Interest.VDEF3,vdef3);
 } 

/** 
* 获取自定义项4
*
* @return 自定义项4
*/
public String getVdef4 () {
return (String) this.getAttributeValue( Interest.VDEF4);
 } 

/** 
* 设置自定义项4
*
* @param vdef4 自定义项4
*/
public void setVdef4 ( String vdef4) {
this.setAttributeValue( Interest.VDEF4,vdef4);
 } 

/** 
* 获取自定义项5
*
* @return 自定义项5
*/
public String getVdef5 () {
return (String) this.getAttributeValue( Interest.VDEF5);
 } 

/** 
* 设置自定义项5
*
* @param vdef5 自定义项5
*/
public void setVdef5 ( String vdef5) {
this.setAttributeValue( Interest.VDEF5,vdef5);
 } 

/** 
* 获取票面利率
*
* @return 票面利率
*/
public UFDouble getYearrate () {
return (UFDouble) this.getAttributeValue( Interest.YEARRATE);
 } 

/** 
* 设置票面利率
*
* @param yearrate 票面利率
*/
public void setYearrate ( UFDouble yearrate) {
this.setAttributeValue( Interest.YEARRATE,yearrate);
 } 


  @Override
  public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("fba_sim.sim_interest");
  }
}