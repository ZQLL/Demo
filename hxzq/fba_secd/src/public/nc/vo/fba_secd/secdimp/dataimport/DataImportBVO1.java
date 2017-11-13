package nc.vo.fba_secd.secdimp.dataimport;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class DataImportBVO1 extends SuperVO {
/**
*元数据字段
*/
public static final String AIM_FIELD="aim_field";
/**
*元数据字段名称
*/
public static final String AIM_FIELD_CN="aim_field_cn";
/**
*系数
*/
public static final String COEFFICIENT="coefficient";
/**
*自定义默认值
*/
public static final String CUSTDEFAULTVAL="custdefaultval";
/**
*数据类型
*/
public static final String DATATYPE="datatype";
/**
*自定义项1
*/
public static final String DEF1="def1";
/**
*自定义项2
*/
public static final String DEF2="def2";
/**
*自定义项3
*/
public static final String DEF3="def3";
/**
*自定义项4
*/
public static final String DEF4="def4";
/**
*自定义项5
*/
public static final String DEF5="def5";
/**
*外系统
*/
public static final String DEF_EXPSYS="def_expsys";
/**
*参照基本档案
*/
public static final String DEF_PKBDINFO="def_pkbdinfo";
/**
*默认值
*/
public static final String DEFAULTVALUE="defaultvalue";
/**
*导入对应字段
*/
public static final String DS_FIELD="ds_field";
/**
*外系统信息
*/
public static final String EXPSYS="expsys";
/**
*允许覆盖
*/
public static final String IF_COVER="if_cover";
/**
*是否公式
*/
public static final String IF_FORMULAR="if_formular";
/**
*允许为空
*/
public static final String IF_NULL="if_null";
/**
*需要参照
*/
public static final String IF_REF="if_ref";
/**
*是否关联字段
*/
public static final String IF_RELEVANCE="if_relevance";
/**
*允许重复
*/
public static final String IF_REPEAT="if_repeat";
/**
*导入公式
*/
public static final String IMPFORMULAR="impformular";
/**
*运算
*/
public static final String OPERATION="operation";
/**
*基本档案主键
*/
public static final String PK_BDINFO="pk_bdinfo";
/**
*上层单据主键
*/
public static final String PK_DATAIMPORT="pk_dataimport";
/**
*主键
*/
public static final String PK_DATAIMPORT_B1="pk_dataimport_b1";
/**
*备注
*/
public static final String REMARK="remark";
/**
*时间戳
*/
public static final String TS="ts";
/** 
* 获取元数据字段
*
* @return 元数据字段
*/
public String getAim_field () {
return (String) this.getAttributeValue( DataImportBVO1.AIM_FIELD);
 } 

/** 
* 设置元数据字段
*
* @param aim_field 元数据字段
*/
public void setAim_field ( String aim_field) {
this.setAttributeValue( DataImportBVO1.AIM_FIELD,aim_field);
 } 

/** 
* 获取元数据字段名称
*
* @return 元数据字段名称
*/
public String getAim_field_cn () {
return (String) this.getAttributeValue( DataImportBVO1.AIM_FIELD_CN);
 } 

/** 
* 设置元数据字段名称
*
* @param aim_field_cn 元数据字段名称
*/
public void setAim_field_cn ( String aim_field_cn) {
this.setAttributeValue( DataImportBVO1.AIM_FIELD_CN,aim_field_cn);
 } 

/** 
* 获取系数
*
* @return 系数
*/
public UFDouble getCoefficient () {
return (UFDouble) this.getAttributeValue( DataImportBVO1.COEFFICIENT);
 } 

/** 
* 设置系数
*
* @param coefficient 系数
*/
public void setCoefficient ( UFDouble coefficient) {
this.setAttributeValue( DataImportBVO1.COEFFICIENT,coefficient);
 } 

/** 
* 获取自定义默认值
*
* @return 自定义默认值
*/
public String getCustdefaultval () {
return (String) this.getAttributeValue( DataImportBVO1.CUSTDEFAULTVAL);
 } 

/** 
* 设置自定义默认值
*
* @param custdefaultval 自定义默认值
*/
public void setCustdefaultval ( String custdefaultval) {
this.setAttributeValue( DataImportBVO1.CUSTDEFAULTVAL,custdefaultval);
 } 

/** 
* 获取数据类型
*
* @return 数据类型
* @see String
*/
public String getDatatype () {
return (String) this.getAttributeValue( DataImportBVO1.DATATYPE);
 } 

/** 
* 设置数据类型
*
* @param datatype 数据类型
* @see String
*/
public void setDatatype ( String datatype) {
this.setAttributeValue( DataImportBVO1.DATATYPE,datatype);
 } 

/** 
* 获取自定义项1
*
* @return 自定义项1
*/
public String getDef1 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF1);
 } 

/** 
* 设置自定义项1
*
* @param def1 自定义项1
*/
public void setDef1 ( String def1) {
this.setAttributeValue( DataImportBVO1.DEF1,def1);
 } 

/** 
* 获取自定义项2
*
* @return 自定义项2
*/
public String getDef2 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF2);
 } 

/** 
* 设置自定义项2
*
* @param def2 自定义项2
*/
public void setDef2 ( String def2) {
this.setAttributeValue( DataImportBVO1.DEF2,def2);
 } 

/** 
* 获取自定义项3
*
* @return 自定义项3
*/
public String getDef3 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF3);
 } 

/** 
* 设置自定义项3
*
* @param def3 自定义项3
*/
public void setDef3 ( String def3) {
this.setAttributeValue( DataImportBVO1.DEF3,def3);
 } 

/** 
* 获取自定义项4
*
* @return 自定义项4
*/
public String getDef4 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF4);
 } 

/** 
* 设置自定义项4
*
* @param def4 自定义项4
*/
public void setDef4 ( String def4) {
this.setAttributeValue( DataImportBVO1.DEF4,def4);
 } 

/** 
* 获取自定义项5
*
* @return 自定义项5
*/
public String getDef5 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF5);
 } 

/** 
* 设置自定义项5
*
* @param def5 自定义项5
*/
public void setDef5 ( String def5) {
this.setAttributeValue( DataImportBVO1.DEF5,def5);
 } 

/** 
* 获取外系统
*
* @return 外系统
*/
public String getDef_expsys () {
return (String) this.getAttributeValue( DataImportBVO1.DEF_EXPSYS);
 } 

/** 
* 设置外系统
*
* @param def_expsys 外系统
*/
public void setDef_expsys ( String def_expsys) {
this.setAttributeValue( DataImportBVO1.DEF_EXPSYS,def_expsys);
 } 

/** 
* 获取参照基本档案
*
* @return 参照基本档案
*/
public String getDef_pkbdinfo () {
return (String) this.getAttributeValue( DataImportBVO1.DEF_PKBDINFO);
 } 

/** 
* 设置参照基本档案
*
* @param def_pkbdinfo 参照基本档案
*/
public void setDef_pkbdinfo ( String def_pkbdinfo) {
this.setAttributeValue( DataImportBVO1.DEF_PKBDINFO,def_pkbdinfo);
 } 

/** 
* 获取默认值
*
* @return 默认值
* @see String
*/
public String getDefaultvalue () {
return (String) this.getAttributeValue( DataImportBVO1.DEFAULTVALUE);
 } 

/** 
* 设置默认值
*
* @param defaultvalue 默认值
* @see String
*/
public void setDefaultvalue ( String defaultvalue) {
this.setAttributeValue( DataImportBVO1.DEFAULTVALUE,defaultvalue);
 } 

/** 
* 获取导入对应字段
*
* @return 导入对应字段
*/
public String getDs_field () {
return (String) this.getAttributeValue( DataImportBVO1.DS_FIELD);
 } 

/** 
* 设置导入对应字段
*
* @param ds_field 导入对应字段
*/
public void setDs_field ( String ds_field) {
this.setAttributeValue( DataImportBVO1.DS_FIELD,ds_field);
 } 

/** 
* 获取外系统信息
*
* @return 外系统信息
*/
public String getExpsys () {
return (String) this.getAttributeValue( DataImportBVO1.EXPSYS);
 } 

/** 
* 设置外系统信息
*
* @param expsys 外系统信息
*/
public void setExpsys ( String expsys) {
this.setAttributeValue( DataImportBVO1.EXPSYS,expsys);
 } 

/** 
* 获取允许覆盖
*
* @return 允许覆盖
*/
public UFBoolean getIf_cover () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_COVER);
 } 

/** 
* 设置允许覆盖
*
* @param if_cover 允许覆盖
*/
public void setIf_cover ( UFBoolean if_cover) {
this.setAttributeValue( DataImportBVO1.IF_COVER,if_cover);
 } 

/** 
* 获取是否公式
*
* @return 是否公式
*/
public UFBoolean getIf_formular () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_FORMULAR);
 } 

/** 
* 设置是否公式
*
* @param if_formular 是否公式
*/
public void setIf_formular ( UFBoolean if_formular) {
this.setAttributeValue( DataImportBVO1.IF_FORMULAR,if_formular);
 } 

/** 
* 获取允许为空
*
* @return 允许为空
*/
public UFBoolean getIf_null () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_NULL);
 } 

/** 
* 设置允许为空
*
* @param if_null 允许为空
*/
public void setIf_null ( UFBoolean if_null) {
this.setAttributeValue( DataImportBVO1.IF_NULL,if_null);
 } 

/** 
* 获取需要参照
*
* @return 需要参照
*/
public UFBoolean getIf_ref () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_REF);
 } 

/** 
* 设置需要参照
*
* @param if_ref 需要参照
*/
public void setIf_ref ( UFBoolean if_ref) {
this.setAttributeValue( DataImportBVO1.IF_REF,if_ref);
 } 

/** 
* 获取是否关联字段
*
* @return 是否关联字段
*/
public UFBoolean getIf_relevance () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_RELEVANCE);
 } 

/** 
* 设置是否关联字段
*
* @param if_relevance 是否关联字段
*/
public void setIf_relevance ( UFBoolean if_relevance) {
this.setAttributeValue( DataImportBVO1.IF_RELEVANCE,if_relevance);
 } 

/** 
* 获取允许重复
*
* @return 允许重复
*/
public UFBoolean getIf_repeat () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_REPEAT);
 } 

/** 
* 设置允许重复
*
* @param if_repeat 允许重复
*/
public void setIf_repeat ( UFBoolean if_repeat) {
this.setAttributeValue( DataImportBVO1.IF_REPEAT,if_repeat);
 } 

/** 
* 获取导入公式
*
* @return 导入公式
*/
public String getImpformular () {
return (String) this.getAttributeValue( DataImportBVO1.IMPFORMULAR);
 } 

/** 
* 设置导入公式
*
* @param impformular 导入公式
*/
public void setImpformular ( String impformular) {
this.setAttributeValue( DataImportBVO1.IMPFORMULAR,impformular);
 } 

/** 
* 获取运算
*
* @return 运算
*/
public String getOperation () {
return (String) this.getAttributeValue( DataImportBVO1.OPERATION);
 } 

/** 
* 设置运算
*
* @param operation 运算
*/
public void setOperation ( String operation) {
this.setAttributeValue( DataImportBVO1.OPERATION,operation);
 } 

/** 
* 获取基本档案主键
*
* @return 基本档案主键
*/
public String getPk_bdinfo () {
return (String) this.getAttributeValue( DataImportBVO1.PK_BDINFO);
 } 

/** 
* 设置基本档案主键
*
* @param pk_bdinfo 基本档案主键
*/
public void setPk_bdinfo ( String pk_bdinfo) {
this.setAttributeValue( DataImportBVO1.PK_BDINFO,pk_bdinfo);
 } 

/** 
* 获取上层单据主键
*
* @return 上层单据主键
*/
public String getPk_dataimport () {
return (String) this.getAttributeValue( DataImportBVO1.PK_DATAIMPORT);
 } 

/** 
* 设置上层单据主键
*
* @param pk_dataimport 上层单据主键
*/
public void setPk_dataimport ( String pk_dataimport) {
this.setAttributeValue( DataImportBVO1.PK_DATAIMPORT,pk_dataimport);
 } 

/** 
* 获取主键
*
* @return 主键
*/
public String getPk_dataimport_b1 () {
return (String) this.getAttributeValue( DataImportBVO1.PK_DATAIMPORT_B1);
 } 

/** 
* 设置主键
*
* @param pk_dataimport_b1 主键
*/
public void setPk_dataimport_b1 ( String pk_dataimport_b1) {
this.setAttributeValue( DataImportBVO1.PK_DATAIMPORT_B1,pk_dataimport_b1);
 } 

/** 
* 获取备注
*
* @return 备注
*/
public String getRemark () {
return (String) this.getAttributeValue( DataImportBVO1.REMARK);
 } 

/** 
* 设置备注
*
* @param remark 备注
*/
public void setRemark ( String remark) {
this.setAttributeValue( DataImportBVO1.REMARK,remark);
 } 

/** 
* 获取时间戳
*
* @return 时间戳
*/
public UFDateTime getTs () {
return (UFDateTime) this.getAttributeValue( DataImportBVO1.TS);
 } 

/** 
* 设置时间戳
*
* @param ts 时间戳
*/
public void setTs ( UFDateTime ts) {
this.setAttributeValue( DataImportBVO1.TS,ts);
 } 


  @Override
  public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("fba_secd.DataImportBVO1");
  }
}