package nc.vo.fba_secd.secdimp.importproj;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class ImportProjVO extends SuperVO {
/**
*层级
*/
public static final String CODE_LEVEL="code_level";
/**
*对比字段
*/
public static final String COMPAREFIELD="comparefield";
/**
*创建时间
*/
public static final String CREATIONTIME="creationtime";
/**
*创建人
*/
public static final String CREATOR="creator";
/**
*是否叶子节点
*/
public static final String IF_LEAF="if_leaf";
/**
*导入方式
*/
public static final String IMPMODE="impmode";
/**
*导入属性
*/
public static final String IMPPROP="impprop";
/**
*修改时间
*/
public static final String MODIFIEDTIME="modifiedtime";
/**
*修改人
*/
public static final String MODIFIER="modifier";
/**
*父ID
*/
public static final String PARENT_ID="parent_id";
/**
*集团
*/
public static final String PK_GROUP="pk_group";
/**
*主键
*/
public static final String PK_IMPORTPROJ="pk_importproj";
/**
*组织
*/
public static final String PK_ORG="pk_org";
/**
*组织版本
*/
public static final String PK_ORG_V="pk_org_v";
/**
*编码
*/
public static final String PROJCODE="projcode";
/**
*名称
*/
public static final String PROJNAME="projname";
/**
*备注
*/
public static final String REMARK="remark";
/**
*时间戳
*/
public static final String TS="ts";
/**
*自定义项1
*/
public static final String VDEF1="vdef1";
/**
*自定义项10
*/
public static final String VDEF10="vdef10";
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
*自定义项6
*/
public static final String VDEF6="vdef6";
/**
*自定义项7
*/
public static final String VDEF7="vdef7";
/**
*自定义项8
*/
public static final String VDEF8="vdef8";
/**
*自定义项9
*/
public static final String VDEF9="vdef9";
/** 
* 获取层级
*
* @return 层级
*/
public Integer getCode_level () {
return (Integer) this.getAttributeValue( ImportProjVO.CODE_LEVEL);
 } 

/** 
* 设置层级
*
* @param code_level 层级
*/
public void setCode_level ( Integer code_level) {
this.setAttributeValue( ImportProjVO.CODE_LEVEL,code_level);
 } 

/** 
* 获取对比字段
*
* @return 对比字段
*/
public String getComparefield () {
return (String) this.getAttributeValue( ImportProjVO.COMPAREFIELD);
 } 

/** 
* 设置对比字段
*
* @param comparefield 对比字段
*/
public void setComparefield ( String comparefield) {
this.setAttributeValue( ImportProjVO.COMPAREFIELD,comparefield);
 } 

/** 
* 获取创建时间
*
* @return 创建时间
*/
public UFDateTime getCreationtime () {
return (UFDateTime) this.getAttributeValue( ImportProjVO.CREATIONTIME);
 } 

/** 
* 设置创建时间
*
* @param creationtime 创建时间
*/
public void setCreationtime ( UFDateTime creationtime) {
this.setAttributeValue( ImportProjVO.CREATIONTIME,creationtime);
 } 

/** 
* 获取创建人
*
* @return 创建人
*/
public String getCreator () {
return (String) this.getAttributeValue( ImportProjVO.CREATOR);
 } 

/** 
* 设置创建人
*
* @param creator 创建人
*/
public void setCreator ( String creator) {
this.setAttributeValue( ImportProjVO.CREATOR,creator);
 } 

/** 
* 获取是否叶子节点
*
* @return 是否叶子节点
*/
public UFBoolean getIf_leaf () {
return (UFBoolean) this.getAttributeValue( ImportProjVO.IF_LEAF);
 } 

/** 
* 设置是否叶子节点
*
* @param if_leaf 是否叶子节点
*/
public void setIf_leaf ( UFBoolean if_leaf) {
this.setAttributeValue( ImportProjVO.IF_LEAF,if_leaf);
 } 

/** 
* 获取导入方式
*
* @return 导入方式
* @see String
*/
public Integer getImpmode () {
return (Integer) this.getAttributeValue( ImportProjVO.IMPMODE);
 } 

/** 
* 设置导入方式
*
* @param impmode 导入方式
* @see String
*/
public void setImpmode ( Integer impmode) {
this.setAttributeValue( ImportProjVO.IMPMODE,impmode);
 } 

/** 
* 获取导入属性
*
* @return 导入属性
* @see String
*/
public Integer getImpprop () {
return (Integer) this.getAttributeValue( ImportProjVO.IMPPROP);
 } 

/** 
* 设置导入属性
*
* @param impprop 导入属性
* @see String
*/
public void setImpprop ( Integer impprop) {
this.setAttributeValue( ImportProjVO.IMPPROP,impprop);
 } 

/** 
* 获取修改时间
*
* @return 修改时间
*/
public UFDateTime getModifiedtime () {
return (UFDateTime) this.getAttributeValue( ImportProjVO.MODIFIEDTIME);
 } 

/** 
* 设置修改时间
*
* @param modifiedtime 修改时间
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.setAttributeValue( ImportProjVO.MODIFIEDTIME,modifiedtime);
 } 

/** 
* 获取修改人
*
* @return 修改人
*/
public String getModifier () {
return (String) this.getAttributeValue( ImportProjVO.MODIFIER);
 } 

/** 
* 设置修改人
*
* @param modifier 修改人
*/
public void setModifier ( String modifier) {
this.setAttributeValue( ImportProjVO.MODIFIER,modifier);
 } 

/** 
* 获取父ID
*
* @return 父ID
*/
public String getParent_id () {
return (String) this.getAttributeValue( ImportProjVO.PARENT_ID);
 } 

/** 
* 设置父ID
*
* @param parent_id 父ID
*/
public void setParent_id ( String parent_id) {
this.setAttributeValue( ImportProjVO.PARENT_ID,parent_id);
 } 

/** 
* 获取集团
*
* @return 集团
*/
public String getPk_group () {
return (String) this.getAttributeValue( ImportProjVO.PK_GROUP);
 } 

/** 
* 设置集团
*
* @param pk_group 集团
*/
public void setPk_group ( String pk_group) {
this.setAttributeValue( ImportProjVO.PK_GROUP,pk_group);
 } 

/** 
* 获取主键
*
* @return 主键
*/
public String getPk_importproj () {
return (String) this.getAttributeValue( ImportProjVO.PK_IMPORTPROJ);
 } 

/** 
* 设置主键
*
* @param pk_importproj 主键
*/
public void setPk_importproj ( String pk_importproj) {
this.setAttributeValue( ImportProjVO.PK_IMPORTPROJ,pk_importproj);
 } 

/** 
* 获取组织
*
* @return 组织
*/
public String getPk_org () {
return (String) this.getAttributeValue( ImportProjVO.PK_ORG);
 } 

/** 
* 设置组织
*
* @param pk_org 组织
*/
public void setPk_org ( String pk_org) {
this.setAttributeValue( ImportProjVO.PK_ORG,pk_org);
 } 

/** 
* 获取组织版本
*
* @return 组织版本
*/
public String getPk_org_v () {
return (String) this.getAttributeValue( ImportProjVO.PK_ORG_V);
 } 

/** 
* 设置组织版本
*
* @param pk_org_v 组织版本
*/
public void setPk_org_v ( String pk_org_v) {
this.setAttributeValue( ImportProjVO.PK_ORG_V,pk_org_v);
 } 

/** 
* 获取编码
*
* @return 编码
*/
public String getProjcode () {
return (String) this.getAttributeValue( ImportProjVO.PROJCODE);
 } 

/** 
* 设置编码
*
* @param projcode 编码
*/
public void setProjcode ( String projcode) {
this.setAttributeValue( ImportProjVO.PROJCODE,projcode);
 } 

/** 
* 获取名称
*
* @return 名称
*/
public String getProjname () {
return (String) this.getAttributeValue( ImportProjVO.PROJNAME);
 } 

/** 
* 设置名称
*
* @param projname 名称
*/
public void setProjname ( String projname) {
this.setAttributeValue( ImportProjVO.PROJNAME,projname);
 } 

/** 
* 获取备注
*
* @return 备注
*/
public String getRemark () {
return (String) this.getAttributeValue( ImportProjVO.REMARK);
 } 

/** 
* 设置备注
*
* @param remark 备注
*/
public void setRemark ( String remark) {
this.setAttributeValue( ImportProjVO.REMARK,remark);
 } 

/** 
* 获取时间戳
*
* @return 时间戳
*/
public UFDateTime getTs () {
return (UFDateTime) this.getAttributeValue( ImportProjVO.TS);
 } 

/** 
* 设置时间戳
*
* @param ts 时间戳
*/
public void setTs ( UFDateTime ts) {
this.setAttributeValue( ImportProjVO.TS,ts);
 } 

/** 
* 获取自定义项1
*
* @return 自定义项1
*/
public String getVdef1 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF1);
 } 

/** 
* 设置自定义项1
*
* @param vdef1 自定义项1
*/
public void setVdef1 ( String vdef1) {
this.setAttributeValue( ImportProjVO.VDEF1,vdef1);
 } 

/** 
* 获取自定义项10
*
* @return 自定义项10
*/
public String getVdef10 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF10);
 } 

/** 
* 设置自定义项10
*
* @param vdef10 自定义项10
*/
public void setVdef10 ( String vdef10) {
this.setAttributeValue( ImportProjVO.VDEF10,vdef10);
 } 

/** 
* 获取自定义项2
*
* @return 自定义项2
*/
public String getVdef2 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF2);
 } 

/** 
* 设置自定义项2
*
* @param vdef2 自定义项2
*/
public void setVdef2 ( String vdef2) {
this.setAttributeValue( ImportProjVO.VDEF2,vdef2);
 } 

/** 
* 获取自定义项3
*
* @return 自定义项3
*/
public String getVdef3 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF3);
 } 

/** 
* 设置自定义项3
*
* @param vdef3 自定义项3
*/
public void setVdef3 ( String vdef3) {
this.setAttributeValue( ImportProjVO.VDEF3,vdef3);
 } 

/** 
* 获取自定义项4
*
* @return 自定义项4
*/
public String getVdef4 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF4);
 } 

/** 
* 设置自定义项4
*
* @param vdef4 自定义项4
*/
public void setVdef4 ( String vdef4) {
this.setAttributeValue( ImportProjVO.VDEF4,vdef4);
 } 

/** 
* 获取自定义项5
*
* @return 自定义项5
*/
public String getVdef5 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF5);
 } 

/** 
* 设置自定义项5
*
* @param vdef5 自定义项5
*/
public void setVdef5 ( String vdef5) {
this.setAttributeValue( ImportProjVO.VDEF5,vdef5);
 } 

/** 
* 获取自定义项6
*
* @return 自定义项6
*/
public String getVdef6 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF6);
 } 

/** 
* 设置自定义项6
*
* @param vdef6 自定义项6
*/
public void setVdef6 ( String vdef6) {
this.setAttributeValue( ImportProjVO.VDEF6,vdef6);
 } 

/** 
* 获取自定义项7
*
* @return 自定义项7
*/
public String getVdef7 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF7);
 } 

/** 
* 设置自定义项7
*
* @param vdef7 自定义项7
*/
public void setVdef7 ( String vdef7) {
this.setAttributeValue( ImportProjVO.VDEF7,vdef7);
 } 

/** 
* 获取自定义项8
*
* @return 自定义项8
*/
public String getVdef8 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF8);
 } 

/** 
* 设置自定义项8
*
* @param vdef8 自定义项8
*/
public void setVdef8 ( String vdef8) {
this.setAttributeValue( ImportProjVO.VDEF8,vdef8);
 } 

/** 
* 获取自定义项9
*
* @return 自定义项9
*/
public String getVdef9 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF9);
 } 

/** 
* 设置自定义项9
*
* @param vdef9 自定义项9
*/
public void setVdef9 ( String vdef9) {
this.setAttributeValue( ImportProjVO.VDEF9,vdef9);
 } 


  @Override
  public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("fba_secd.ImportProj");
  }
}