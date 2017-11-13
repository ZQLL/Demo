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
*Ԫ�����ֶ�
*/
public static final String AIM_FIELD="aim_field";
/**
*Ԫ�����ֶ�����
*/
public static final String AIM_FIELD_CN="aim_field_cn";
/**
*ϵ��
*/
public static final String COEFFICIENT="coefficient";
/**
*�Զ���Ĭ��ֵ
*/
public static final String CUSTDEFAULTVAL="custdefaultval";
/**
*��������
*/
public static final String DATATYPE="datatype";
/**
*�Զ�����1
*/
public static final String DEF1="def1";
/**
*�Զ�����2
*/
public static final String DEF2="def2";
/**
*�Զ�����3
*/
public static final String DEF3="def3";
/**
*�Զ�����4
*/
public static final String DEF4="def4";
/**
*�Զ�����5
*/
public static final String DEF5="def5";
/**
*��ϵͳ
*/
public static final String DEF_EXPSYS="def_expsys";
/**
*���ջ�������
*/
public static final String DEF_PKBDINFO="def_pkbdinfo";
/**
*Ĭ��ֵ
*/
public static final String DEFAULTVALUE="defaultvalue";
/**
*�����Ӧ�ֶ�
*/
public static final String DS_FIELD="ds_field";
/**
*��ϵͳ��Ϣ
*/
public static final String EXPSYS="expsys";
/**
*������
*/
public static final String IF_COVER="if_cover";
/**
*�Ƿ�ʽ
*/
public static final String IF_FORMULAR="if_formular";
/**
*����Ϊ��
*/
public static final String IF_NULL="if_null";
/**
*��Ҫ����
*/
public static final String IF_REF="if_ref";
/**
*�Ƿ�����ֶ�
*/
public static final String IF_RELEVANCE="if_relevance";
/**
*�����ظ�
*/
public static final String IF_REPEAT="if_repeat";
/**
*���빫ʽ
*/
public static final String IMPFORMULAR="impformular";
/**
*����
*/
public static final String OPERATION="operation";
/**
*������������
*/
public static final String PK_BDINFO="pk_bdinfo";
/**
*�ϲ㵥������
*/
public static final String PK_DATAIMPORT="pk_dataimport";
/**
*����
*/
public static final String PK_DATAIMPORT_B1="pk_dataimport_b1";
/**
*��ע
*/
public static final String REMARK="remark";
/**
*ʱ���
*/
public static final String TS="ts";
/** 
* ��ȡԪ�����ֶ�
*
* @return Ԫ�����ֶ�
*/
public String getAim_field () {
return (String) this.getAttributeValue( DataImportBVO1.AIM_FIELD);
 } 

/** 
* ����Ԫ�����ֶ�
*
* @param aim_field Ԫ�����ֶ�
*/
public void setAim_field ( String aim_field) {
this.setAttributeValue( DataImportBVO1.AIM_FIELD,aim_field);
 } 

/** 
* ��ȡԪ�����ֶ�����
*
* @return Ԫ�����ֶ�����
*/
public String getAim_field_cn () {
return (String) this.getAttributeValue( DataImportBVO1.AIM_FIELD_CN);
 } 

/** 
* ����Ԫ�����ֶ�����
*
* @param aim_field_cn Ԫ�����ֶ�����
*/
public void setAim_field_cn ( String aim_field_cn) {
this.setAttributeValue( DataImportBVO1.AIM_FIELD_CN,aim_field_cn);
 } 

/** 
* ��ȡϵ��
*
* @return ϵ��
*/
public UFDouble getCoefficient () {
return (UFDouble) this.getAttributeValue( DataImportBVO1.COEFFICIENT);
 } 

/** 
* ����ϵ��
*
* @param coefficient ϵ��
*/
public void setCoefficient ( UFDouble coefficient) {
this.setAttributeValue( DataImportBVO1.COEFFICIENT,coefficient);
 } 

/** 
* ��ȡ�Զ���Ĭ��ֵ
*
* @return �Զ���Ĭ��ֵ
*/
public String getCustdefaultval () {
return (String) this.getAttributeValue( DataImportBVO1.CUSTDEFAULTVAL);
 } 

/** 
* �����Զ���Ĭ��ֵ
*
* @param custdefaultval �Զ���Ĭ��ֵ
*/
public void setCustdefaultval ( String custdefaultval) {
this.setAttributeValue( DataImportBVO1.CUSTDEFAULTVAL,custdefaultval);
 } 

/** 
* ��ȡ��������
*
* @return ��������
* @see String
*/
public String getDatatype () {
return (String) this.getAttributeValue( DataImportBVO1.DATATYPE);
 } 

/** 
* ������������
*
* @param datatype ��������
* @see String
*/
public void setDatatype ( String datatype) {
this.setAttributeValue( DataImportBVO1.DATATYPE,datatype);
 } 

/** 
* ��ȡ�Զ�����1
*
* @return �Զ�����1
*/
public String getDef1 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF1);
 } 

/** 
* �����Զ�����1
*
* @param def1 �Զ�����1
*/
public void setDef1 ( String def1) {
this.setAttributeValue( DataImportBVO1.DEF1,def1);
 } 

/** 
* ��ȡ�Զ�����2
*
* @return �Զ�����2
*/
public String getDef2 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF2);
 } 

/** 
* �����Զ�����2
*
* @param def2 �Զ�����2
*/
public void setDef2 ( String def2) {
this.setAttributeValue( DataImportBVO1.DEF2,def2);
 } 

/** 
* ��ȡ�Զ�����3
*
* @return �Զ�����3
*/
public String getDef3 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF3);
 } 

/** 
* �����Զ�����3
*
* @param def3 �Զ�����3
*/
public void setDef3 ( String def3) {
this.setAttributeValue( DataImportBVO1.DEF3,def3);
 } 

/** 
* ��ȡ�Զ�����4
*
* @return �Զ�����4
*/
public String getDef4 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF4);
 } 

/** 
* �����Զ�����4
*
* @param def4 �Զ�����4
*/
public void setDef4 ( String def4) {
this.setAttributeValue( DataImportBVO1.DEF4,def4);
 } 

/** 
* ��ȡ�Զ�����5
*
* @return �Զ�����5
*/
public String getDef5 () {
return (String) this.getAttributeValue( DataImportBVO1.DEF5);
 } 

/** 
* �����Զ�����5
*
* @param def5 �Զ�����5
*/
public void setDef5 ( String def5) {
this.setAttributeValue( DataImportBVO1.DEF5,def5);
 } 

/** 
* ��ȡ��ϵͳ
*
* @return ��ϵͳ
*/
public String getDef_expsys () {
return (String) this.getAttributeValue( DataImportBVO1.DEF_EXPSYS);
 } 

/** 
* ������ϵͳ
*
* @param def_expsys ��ϵͳ
*/
public void setDef_expsys ( String def_expsys) {
this.setAttributeValue( DataImportBVO1.DEF_EXPSYS,def_expsys);
 } 

/** 
* ��ȡ���ջ�������
*
* @return ���ջ�������
*/
public String getDef_pkbdinfo () {
return (String) this.getAttributeValue( DataImportBVO1.DEF_PKBDINFO);
 } 

/** 
* ���ò��ջ�������
*
* @param def_pkbdinfo ���ջ�������
*/
public void setDef_pkbdinfo ( String def_pkbdinfo) {
this.setAttributeValue( DataImportBVO1.DEF_PKBDINFO,def_pkbdinfo);
 } 

/** 
* ��ȡĬ��ֵ
*
* @return Ĭ��ֵ
* @see String
*/
public String getDefaultvalue () {
return (String) this.getAttributeValue( DataImportBVO1.DEFAULTVALUE);
 } 

/** 
* ����Ĭ��ֵ
*
* @param defaultvalue Ĭ��ֵ
* @see String
*/
public void setDefaultvalue ( String defaultvalue) {
this.setAttributeValue( DataImportBVO1.DEFAULTVALUE,defaultvalue);
 } 

/** 
* ��ȡ�����Ӧ�ֶ�
*
* @return �����Ӧ�ֶ�
*/
public String getDs_field () {
return (String) this.getAttributeValue( DataImportBVO1.DS_FIELD);
 } 

/** 
* ���õ����Ӧ�ֶ�
*
* @param ds_field �����Ӧ�ֶ�
*/
public void setDs_field ( String ds_field) {
this.setAttributeValue( DataImportBVO1.DS_FIELD,ds_field);
 } 

/** 
* ��ȡ��ϵͳ��Ϣ
*
* @return ��ϵͳ��Ϣ
*/
public String getExpsys () {
return (String) this.getAttributeValue( DataImportBVO1.EXPSYS);
 } 

/** 
* ������ϵͳ��Ϣ
*
* @param expsys ��ϵͳ��Ϣ
*/
public void setExpsys ( String expsys) {
this.setAttributeValue( DataImportBVO1.EXPSYS,expsys);
 } 

/** 
* ��ȡ������
*
* @return ������
*/
public UFBoolean getIf_cover () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_COVER);
 } 

/** 
* ����������
*
* @param if_cover ������
*/
public void setIf_cover ( UFBoolean if_cover) {
this.setAttributeValue( DataImportBVO1.IF_COVER,if_cover);
 } 

/** 
* ��ȡ�Ƿ�ʽ
*
* @return �Ƿ�ʽ
*/
public UFBoolean getIf_formular () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_FORMULAR);
 } 

/** 
* �����Ƿ�ʽ
*
* @param if_formular �Ƿ�ʽ
*/
public void setIf_formular ( UFBoolean if_formular) {
this.setAttributeValue( DataImportBVO1.IF_FORMULAR,if_formular);
 } 

/** 
* ��ȡ����Ϊ��
*
* @return ����Ϊ��
*/
public UFBoolean getIf_null () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_NULL);
 } 

/** 
* ��������Ϊ��
*
* @param if_null ����Ϊ��
*/
public void setIf_null ( UFBoolean if_null) {
this.setAttributeValue( DataImportBVO1.IF_NULL,if_null);
 } 

/** 
* ��ȡ��Ҫ����
*
* @return ��Ҫ����
*/
public UFBoolean getIf_ref () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_REF);
 } 

/** 
* ������Ҫ����
*
* @param if_ref ��Ҫ����
*/
public void setIf_ref ( UFBoolean if_ref) {
this.setAttributeValue( DataImportBVO1.IF_REF,if_ref);
 } 

/** 
* ��ȡ�Ƿ�����ֶ�
*
* @return �Ƿ�����ֶ�
*/
public UFBoolean getIf_relevance () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_RELEVANCE);
 } 

/** 
* �����Ƿ�����ֶ�
*
* @param if_relevance �Ƿ�����ֶ�
*/
public void setIf_relevance ( UFBoolean if_relevance) {
this.setAttributeValue( DataImportBVO1.IF_RELEVANCE,if_relevance);
 } 

/** 
* ��ȡ�����ظ�
*
* @return �����ظ�
*/
public UFBoolean getIf_repeat () {
return (UFBoolean) this.getAttributeValue( DataImportBVO1.IF_REPEAT);
 } 

/** 
* ���������ظ�
*
* @param if_repeat �����ظ�
*/
public void setIf_repeat ( UFBoolean if_repeat) {
this.setAttributeValue( DataImportBVO1.IF_REPEAT,if_repeat);
 } 

/** 
* ��ȡ���빫ʽ
*
* @return ���빫ʽ
*/
public String getImpformular () {
return (String) this.getAttributeValue( DataImportBVO1.IMPFORMULAR);
 } 

/** 
* ���õ��빫ʽ
*
* @param impformular ���빫ʽ
*/
public void setImpformular ( String impformular) {
this.setAttributeValue( DataImportBVO1.IMPFORMULAR,impformular);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getOperation () {
return (String) this.getAttributeValue( DataImportBVO1.OPERATION);
 } 

/** 
* ��������
*
* @param operation ����
*/
public void setOperation ( String operation) {
this.setAttributeValue( DataImportBVO1.OPERATION,operation);
 } 

/** 
* ��ȡ������������
*
* @return ������������
*/
public String getPk_bdinfo () {
return (String) this.getAttributeValue( DataImportBVO1.PK_BDINFO);
 } 

/** 
* ���û�����������
*
* @param pk_bdinfo ������������
*/
public void setPk_bdinfo ( String pk_bdinfo) {
this.setAttributeValue( DataImportBVO1.PK_BDINFO,pk_bdinfo);
 } 

/** 
* ��ȡ�ϲ㵥������
*
* @return �ϲ㵥������
*/
public String getPk_dataimport () {
return (String) this.getAttributeValue( DataImportBVO1.PK_DATAIMPORT);
 } 

/** 
* �����ϲ㵥������
*
* @param pk_dataimport �ϲ㵥������
*/
public void setPk_dataimport ( String pk_dataimport) {
this.setAttributeValue( DataImportBVO1.PK_DATAIMPORT,pk_dataimport);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getPk_dataimport_b1 () {
return (String) this.getAttributeValue( DataImportBVO1.PK_DATAIMPORT_B1);
 } 

/** 
* ��������
*
* @param pk_dataimport_b1 ����
*/
public void setPk_dataimport_b1 ( String pk_dataimport_b1) {
this.setAttributeValue( DataImportBVO1.PK_DATAIMPORT_B1,pk_dataimport_b1);
 } 

/** 
* ��ȡ��ע
*
* @return ��ע
*/
public String getRemark () {
return (String) this.getAttributeValue( DataImportBVO1.REMARK);
 } 

/** 
* ���ñ�ע
*
* @param remark ��ע
*/
public void setRemark ( String remark) {
this.setAttributeValue( DataImportBVO1.REMARK,remark);
 } 

/** 
* ��ȡʱ���
*
* @return ʱ���
*/
public UFDateTime getTs () {
return (UFDateTime) this.getAttributeValue( DataImportBVO1.TS);
 } 

/** 
* ����ʱ���
*
* @param ts ʱ���
*/
public void setTs ( UFDateTime ts) {
this.setAttributeValue( DataImportBVO1.TS,ts);
 } 


  @Override
  public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("fba_secd.DataImportBVO1");
  }
}