package nc.vo.fba_secd.secdimp.importproj;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class ImportProjVO extends SuperVO {
/**
*�㼶
*/
public static final String CODE_LEVEL="code_level";
/**
*�Ա��ֶ�
*/
public static final String COMPAREFIELD="comparefield";
/**
*����ʱ��
*/
public static final String CREATIONTIME="creationtime";
/**
*������
*/
public static final String CREATOR="creator";
/**
*�Ƿ�Ҷ�ӽڵ�
*/
public static final String IF_LEAF="if_leaf";
/**
*���뷽ʽ
*/
public static final String IMPMODE="impmode";
/**
*��������
*/
public static final String IMPPROP="impprop";
/**
*�޸�ʱ��
*/
public static final String MODIFIEDTIME="modifiedtime";
/**
*�޸���
*/
public static final String MODIFIER="modifier";
/**
*��ID
*/
public static final String PARENT_ID="parent_id";
/**
*����
*/
public static final String PK_GROUP="pk_group";
/**
*����
*/
public static final String PK_IMPORTPROJ="pk_importproj";
/**
*��֯
*/
public static final String PK_ORG="pk_org";
/**
*��֯�汾
*/
public static final String PK_ORG_V="pk_org_v";
/**
*����
*/
public static final String PROJCODE="projcode";
/**
*����
*/
public static final String PROJNAME="projname";
/**
*��ע
*/
public static final String REMARK="remark";
/**
*ʱ���
*/
public static final String TS="ts";
/**
*�Զ�����1
*/
public static final String VDEF1="vdef1";
/**
*�Զ�����10
*/
public static final String VDEF10="vdef10";
/**
*�Զ�����2
*/
public static final String VDEF2="vdef2";
/**
*�Զ�����3
*/
public static final String VDEF3="vdef3";
/**
*�Զ�����4
*/
public static final String VDEF4="vdef4";
/**
*�Զ�����5
*/
public static final String VDEF5="vdef5";
/**
*�Զ�����6
*/
public static final String VDEF6="vdef6";
/**
*�Զ�����7
*/
public static final String VDEF7="vdef7";
/**
*�Զ�����8
*/
public static final String VDEF8="vdef8";
/**
*�Զ�����9
*/
public static final String VDEF9="vdef9";
/** 
* ��ȡ�㼶
*
* @return �㼶
*/
public Integer getCode_level () {
return (Integer) this.getAttributeValue( ImportProjVO.CODE_LEVEL);
 } 

/** 
* ���ò㼶
*
* @param code_level �㼶
*/
public void setCode_level ( Integer code_level) {
this.setAttributeValue( ImportProjVO.CODE_LEVEL,code_level);
 } 

/** 
* ��ȡ�Ա��ֶ�
*
* @return �Ա��ֶ�
*/
public String getComparefield () {
return (String) this.getAttributeValue( ImportProjVO.COMPAREFIELD);
 } 

/** 
* ���öԱ��ֶ�
*
* @param comparefield �Ա��ֶ�
*/
public void setComparefield ( String comparefield) {
this.setAttributeValue( ImportProjVO.COMPAREFIELD,comparefield);
 } 

/** 
* ��ȡ����ʱ��
*
* @return ����ʱ��
*/
public UFDateTime getCreationtime () {
return (UFDateTime) this.getAttributeValue( ImportProjVO.CREATIONTIME);
 } 

/** 
* ���ô���ʱ��
*
* @param creationtime ����ʱ��
*/
public void setCreationtime ( UFDateTime creationtime) {
this.setAttributeValue( ImportProjVO.CREATIONTIME,creationtime);
 } 

/** 
* ��ȡ������
*
* @return ������
*/
public String getCreator () {
return (String) this.getAttributeValue( ImportProjVO.CREATOR);
 } 

/** 
* ���ô�����
*
* @param creator ������
*/
public void setCreator ( String creator) {
this.setAttributeValue( ImportProjVO.CREATOR,creator);
 } 

/** 
* ��ȡ�Ƿ�Ҷ�ӽڵ�
*
* @return �Ƿ�Ҷ�ӽڵ�
*/
public UFBoolean getIf_leaf () {
return (UFBoolean) this.getAttributeValue( ImportProjVO.IF_LEAF);
 } 

/** 
* �����Ƿ�Ҷ�ӽڵ�
*
* @param if_leaf �Ƿ�Ҷ�ӽڵ�
*/
public void setIf_leaf ( UFBoolean if_leaf) {
this.setAttributeValue( ImportProjVO.IF_LEAF,if_leaf);
 } 

/** 
* ��ȡ���뷽ʽ
*
* @return ���뷽ʽ
* @see String
*/
public Integer getImpmode () {
return (Integer) this.getAttributeValue( ImportProjVO.IMPMODE);
 } 

/** 
* ���õ��뷽ʽ
*
* @param impmode ���뷽ʽ
* @see String
*/
public void setImpmode ( Integer impmode) {
this.setAttributeValue( ImportProjVO.IMPMODE,impmode);
 } 

/** 
* ��ȡ��������
*
* @return ��������
* @see String
*/
public Integer getImpprop () {
return (Integer) this.getAttributeValue( ImportProjVO.IMPPROP);
 } 

/** 
* ���õ�������
*
* @param impprop ��������
* @see String
*/
public void setImpprop ( Integer impprop) {
this.setAttributeValue( ImportProjVO.IMPPROP,impprop);
 } 

/** 
* ��ȡ�޸�ʱ��
*
* @return �޸�ʱ��
*/
public UFDateTime getModifiedtime () {
return (UFDateTime) this.getAttributeValue( ImportProjVO.MODIFIEDTIME);
 } 

/** 
* �����޸�ʱ��
*
* @param modifiedtime �޸�ʱ��
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.setAttributeValue( ImportProjVO.MODIFIEDTIME,modifiedtime);
 } 

/** 
* ��ȡ�޸���
*
* @return �޸���
*/
public String getModifier () {
return (String) this.getAttributeValue( ImportProjVO.MODIFIER);
 } 

/** 
* �����޸���
*
* @param modifier �޸���
*/
public void setModifier ( String modifier) {
this.setAttributeValue( ImportProjVO.MODIFIER,modifier);
 } 

/** 
* ��ȡ��ID
*
* @return ��ID
*/
public String getParent_id () {
return (String) this.getAttributeValue( ImportProjVO.PARENT_ID);
 } 

/** 
* ���ø�ID
*
* @param parent_id ��ID
*/
public void setParent_id ( String parent_id) {
this.setAttributeValue( ImportProjVO.PARENT_ID,parent_id);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getPk_group () {
return (String) this.getAttributeValue( ImportProjVO.PK_GROUP);
 } 

/** 
* ���ü���
*
* @param pk_group ����
*/
public void setPk_group ( String pk_group) {
this.setAttributeValue( ImportProjVO.PK_GROUP,pk_group);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getPk_importproj () {
return (String) this.getAttributeValue( ImportProjVO.PK_IMPORTPROJ);
 } 

/** 
* ��������
*
* @param pk_importproj ����
*/
public void setPk_importproj ( String pk_importproj) {
this.setAttributeValue( ImportProjVO.PK_IMPORTPROJ,pk_importproj);
 } 

/** 
* ��ȡ��֯
*
* @return ��֯
*/
public String getPk_org () {
return (String) this.getAttributeValue( ImportProjVO.PK_ORG);
 } 

/** 
* ������֯
*
* @param pk_org ��֯
*/
public void setPk_org ( String pk_org) {
this.setAttributeValue( ImportProjVO.PK_ORG,pk_org);
 } 

/** 
* ��ȡ��֯�汾
*
* @return ��֯�汾
*/
public String getPk_org_v () {
return (String) this.getAttributeValue( ImportProjVO.PK_ORG_V);
 } 

/** 
* ������֯�汾
*
* @param pk_org_v ��֯�汾
*/
public void setPk_org_v ( String pk_org_v) {
this.setAttributeValue( ImportProjVO.PK_ORG_V,pk_org_v);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getProjcode () {
return (String) this.getAttributeValue( ImportProjVO.PROJCODE);
 } 

/** 
* ���ñ���
*
* @param projcode ����
*/
public void setProjcode ( String projcode) {
this.setAttributeValue( ImportProjVO.PROJCODE,projcode);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getProjname () {
return (String) this.getAttributeValue( ImportProjVO.PROJNAME);
 } 

/** 
* ��������
*
* @param projname ����
*/
public void setProjname ( String projname) {
this.setAttributeValue( ImportProjVO.PROJNAME,projname);
 } 

/** 
* ��ȡ��ע
*
* @return ��ע
*/
public String getRemark () {
return (String) this.getAttributeValue( ImportProjVO.REMARK);
 } 

/** 
* ���ñ�ע
*
* @param remark ��ע
*/
public void setRemark ( String remark) {
this.setAttributeValue( ImportProjVO.REMARK,remark);
 } 

/** 
* ��ȡʱ���
*
* @return ʱ���
*/
public UFDateTime getTs () {
return (UFDateTime) this.getAttributeValue( ImportProjVO.TS);
 } 

/** 
* ����ʱ���
*
* @param ts ʱ���
*/
public void setTs ( UFDateTime ts) {
this.setAttributeValue( ImportProjVO.TS,ts);
 } 

/** 
* ��ȡ�Զ�����1
*
* @return �Զ�����1
*/
public String getVdef1 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF1);
 } 

/** 
* �����Զ�����1
*
* @param vdef1 �Զ�����1
*/
public void setVdef1 ( String vdef1) {
this.setAttributeValue( ImportProjVO.VDEF1,vdef1);
 } 

/** 
* ��ȡ�Զ�����10
*
* @return �Զ�����10
*/
public String getVdef10 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF10);
 } 

/** 
* �����Զ�����10
*
* @param vdef10 �Զ�����10
*/
public void setVdef10 ( String vdef10) {
this.setAttributeValue( ImportProjVO.VDEF10,vdef10);
 } 

/** 
* ��ȡ�Զ�����2
*
* @return �Զ�����2
*/
public String getVdef2 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF2);
 } 

/** 
* �����Զ�����2
*
* @param vdef2 �Զ�����2
*/
public void setVdef2 ( String vdef2) {
this.setAttributeValue( ImportProjVO.VDEF2,vdef2);
 } 

/** 
* ��ȡ�Զ�����3
*
* @return �Զ�����3
*/
public String getVdef3 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF3);
 } 

/** 
* �����Զ�����3
*
* @param vdef3 �Զ�����3
*/
public void setVdef3 ( String vdef3) {
this.setAttributeValue( ImportProjVO.VDEF3,vdef3);
 } 

/** 
* ��ȡ�Զ�����4
*
* @return �Զ�����4
*/
public String getVdef4 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF4);
 } 

/** 
* �����Զ�����4
*
* @param vdef4 �Զ�����4
*/
public void setVdef4 ( String vdef4) {
this.setAttributeValue( ImportProjVO.VDEF4,vdef4);
 } 

/** 
* ��ȡ�Զ�����5
*
* @return �Զ�����5
*/
public String getVdef5 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF5);
 } 

/** 
* �����Զ�����5
*
* @param vdef5 �Զ�����5
*/
public void setVdef5 ( String vdef5) {
this.setAttributeValue( ImportProjVO.VDEF5,vdef5);
 } 

/** 
* ��ȡ�Զ�����6
*
* @return �Զ�����6
*/
public String getVdef6 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF6);
 } 

/** 
* �����Զ�����6
*
* @param vdef6 �Զ�����6
*/
public void setVdef6 ( String vdef6) {
this.setAttributeValue( ImportProjVO.VDEF6,vdef6);
 } 

/** 
* ��ȡ�Զ�����7
*
* @return �Զ�����7
*/
public String getVdef7 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF7);
 } 

/** 
* �����Զ�����7
*
* @param vdef7 �Զ�����7
*/
public void setVdef7 ( String vdef7) {
this.setAttributeValue( ImportProjVO.VDEF7,vdef7);
 } 

/** 
* ��ȡ�Զ�����8
*
* @return �Զ�����8
*/
public String getVdef8 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF8);
 } 

/** 
* �����Զ�����8
*
* @param vdef8 �Զ�����8
*/
public void setVdef8 ( String vdef8) {
this.setAttributeValue( ImportProjVO.VDEF8,vdef8);
 } 

/** 
* ��ȡ�Զ�����9
*
* @return �Զ�����9
*/
public String getVdef9 () {
return (String) this.getAttributeValue( ImportProjVO.VDEF9);
 } 

/** 
* �����Զ�����9
*
* @param vdef9 �Զ�����9
*/
public void setVdef9 ( String vdef9) {
this.setAttributeValue( ImportProjVO.VDEF9,vdef9);
 } 


  @Override
  public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("fba_secd.ImportProj");
  }
}