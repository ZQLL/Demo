package nc.vo.fba_scost.cost.interest;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class Interest extends SuperVO {
/**
*����ʱ��
*/
public static final String CREATIONTIME="creationtime";
/**
*������
*/
public static final String CREATOR="creator";
/**
*��Ϣ��
*/
public static final String ENDDATE="enddate";
/**
*������
*/
public static final String ISSUEDATE="issuedate";
/**
*�޸�ʱ��
*/
public static final String MODIFIEDTIME="modifiedtime";
/**
*�޸���
*/
public static final String MODIFIER="modifier";
/**
*�Զ�����ֵ1
*/
public static final String NDEF1="ndef1";
/**
*�Զ�����ֵ2
*/
public static final String NDEF2="ndef2";
/**
*�Զ�����ֵ3
*/
public static final String NDEF3="ndef3";
/**
*�Զ�����ֵ4
*/
public static final String NDEF4="ndef4";
/**
*�Զ�����ֵ5
*/
public static final String NDEF5="ndef5";
/**
*��Ϣ��ʽ
*/
public static final String PAYTYPE="paytype";
/**
*��Ϣ������
*/
public static final String PERIODSUM="periodsum";
/**
*��Ϣ����
*/
public static final String PERIODTYPE="periodtype";
/**
*����
*/
public static final String PK_GROUP="pk_group";
/**
*����
*/
public static final String PK_INTEREST="pk_interest";
/**
*��֯
*/
public static final String PK_ORG="pk_org";
/**
*��֯�汾
*/
public static final String PK_ORG_V="pk_org_v";
/**
*֤ȯ����
*/
public static final String PK_SECURITIES="pk_securities";
/**
*���淽ʽ
*/
public static final String PROFITTYPE="profittype";
/**
*��������
*/
public static final String RATETYPE="ratetype";
/**
*��Ϣ��
*/
public static final String STARTDATE="startdate";
/**
*ʱ���
*/
public static final String TS="ts";
/**
*�Զ�����1
*/
public static final String VDEF1="vdef1";
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
*Ʊ������
*/
public static final String YEARRATE="yearrate";
/** 
* ��ȡ����ʱ��
*
* @return ����ʱ��
*/
public UFDateTime getCreationtime () {
return (UFDateTime) this.getAttributeValue( Interest.CREATIONTIME);
 } 

/** 
* ���ô���ʱ��
*
* @param creationtime ����ʱ��
*/
public void setCreationtime ( UFDateTime creationtime) {
this.setAttributeValue( Interest.CREATIONTIME,creationtime);
 } 

/** 
* ��ȡ������
*
* @return ������
*/
public String getCreator () {
return (String) this.getAttributeValue( Interest.CREATOR);
 } 

/** 
* ���ô�����
*
* @param creator ������
*/
public void setCreator ( String creator) {
this.setAttributeValue( Interest.CREATOR,creator);
 } 

/** 
* ��ȡ��Ϣ��
*
* @return ��Ϣ��
*/
public UFDate getEnddate () {
return (UFDate) this.getAttributeValue( Interest.ENDDATE);
 } 

/** 
* ���õ�Ϣ��
*
* @param enddate ��Ϣ��
*/
public void setEnddate ( UFDate enddate) {
this.setAttributeValue( Interest.ENDDATE,enddate);
 } 

/** 
* ��ȡ������
*
* @return ������
*/
public UFDate getIssuedate () {
return (UFDate) this.getAttributeValue( Interest.ISSUEDATE);
 } 

/** 
* ���÷�����
*
* @param issuedate ������
*/
public void setIssuedate ( UFDate issuedate) {
this.setAttributeValue( Interest.ISSUEDATE,issuedate);
 } 

/** 
* ��ȡ�޸�ʱ��
*
* @return �޸�ʱ��
*/
public UFDateTime getModifiedtime () {
return (UFDateTime) this.getAttributeValue( Interest.MODIFIEDTIME);
 } 

/** 
* �����޸�ʱ��
*
* @param modifiedtime �޸�ʱ��
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.setAttributeValue( Interest.MODIFIEDTIME,modifiedtime);
 } 

/** 
* ��ȡ�޸���
*
* @return �޸���
*/
public String getModifier () {
return (String) this.getAttributeValue( Interest.MODIFIER);
 } 

/** 
* �����޸���
*
* @param modifier �޸���
*/
public void setModifier ( String modifier) {
this.setAttributeValue( Interest.MODIFIER,modifier);
 } 

/** 
* ��ȡ�Զ�����ֵ1
*
* @return �Զ�����ֵ1
*/
public UFDouble getNdef1 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF1);
 } 

/** 
* �����Զ�����ֵ1
*
* @param ndef1 �Զ�����ֵ1
*/
public void setNdef1 ( UFDouble ndef1) {
this.setAttributeValue( Interest.NDEF1,ndef1);
 } 

/** 
* ��ȡ�Զ�����ֵ2
*
* @return �Զ�����ֵ2
*/
public UFDouble getNdef2 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF2);
 } 

/** 
* �����Զ�����ֵ2
*
* @param ndef2 �Զ�����ֵ2
*/
public void setNdef2 ( UFDouble ndef2) {
this.setAttributeValue( Interest.NDEF2,ndef2);
 } 

/** 
* ��ȡ�Զ�����ֵ3
*
* @return �Զ�����ֵ3
*/
public UFDouble getNdef3 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF3);
 } 

/** 
* �����Զ�����ֵ3
*
* @param ndef3 �Զ�����ֵ3
*/
public void setNdef3 ( UFDouble ndef3) {
this.setAttributeValue( Interest.NDEF3,ndef3);
 } 

/** 
* ��ȡ�Զ�����ֵ4
*
* @return �Զ�����ֵ4
*/
public UFDouble getNdef4 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF4);
 } 

/** 
* �����Զ�����ֵ4
*
* @param ndef4 �Զ�����ֵ4
*/
public void setNdef4 ( UFDouble ndef4) {
this.setAttributeValue( Interest.NDEF4,ndef4);
 } 

/** 
* ��ȡ�Զ�����ֵ5
*
* @return �Զ�����ֵ5
*/
public UFDouble getNdef5 () {
return (UFDouble) this.getAttributeValue( Interest.NDEF5);
 } 

/** 
* �����Զ�����ֵ5
*
* @param ndef5 �Զ�����ֵ5
*/
public void setNdef5 ( UFDouble ndef5) {
this.setAttributeValue( Interest.NDEF5,ndef5);
 } 

/** 
* ��ȡ��Ϣ��ʽ
*
* @return ��Ϣ��ʽ
* @see String
*/
public String getPaytype () {
return (String) this.getAttributeValue( Interest.PAYTYPE);
 } 

/** 
* ���ø�Ϣ��ʽ
*
* @param paytype ��Ϣ��ʽ
* @see String
*/
public void setPaytype ( String paytype) {
this.setAttributeValue( Interest.PAYTYPE,paytype);
 } 

/** 
* ��ȡ��Ϣ������
*
* @return ��Ϣ������
*/
public Integer getPeriodsum () {
return (Integer) this.getAttributeValue( Interest.PERIODSUM);
 } 

/** 
* ���ü�Ϣ������
*
* @param periodsum ��Ϣ������
*/
public void setPeriodsum ( Integer periodsum) {
this.setAttributeValue( Interest.PERIODSUM,periodsum);
 } 

/** 
* ��ȡ��Ϣ����
*
* @return ��Ϣ����
* @see String
*/
public String getPeriodtype () {
return (String) this.getAttributeValue( Interest.PERIODTYPE);
 } 

/** 
* ���ø�Ϣ����
*
* @param periodtype ��Ϣ����
* @see String
*/
public void setPeriodtype ( String periodtype) {
this.setAttributeValue( Interest.PERIODTYPE,periodtype);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getPk_group () {
return (String) this.getAttributeValue( Interest.PK_GROUP);
 } 

/** 
* ���ü���
*
* @param pk_group ����
*/
public void setPk_group ( String pk_group) {
this.setAttributeValue( Interest.PK_GROUP,pk_group);
 } 

/** 
* ��ȡ����
*
* @return ����
*/
public String getPk_interest () {
return (String) this.getAttributeValue( Interest.PK_INTEREST);
 } 

/** 
* ��������
*
* @param pk_interest ����
*/
public void setPk_interest ( String pk_interest) {
this.setAttributeValue( Interest.PK_INTEREST,pk_interest);
 } 

/** 
* ��ȡ��֯
*
* @return ��֯
*/
public String getPk_org () {
return (String) this.getAttributeValue( Interest.PK_ORG);
 } 

/** 
* ������֯
*
* @param pk_org ��֯
*/
public void setPk_org ( String pk_org) {
this.setAttributeValue( Interest.PK_ORG,pk_org);
 } 

/** 
* ��ȡ��֯�汾
*
* @return ��֯�汾
*/
public String getPk_org_v () {
return (String) this.getAttributeValue( Interest.PK_ORG_V);
 } 

/** 
* ������֯�汾
*
* @param pk_org_v ��֯�汾
*/
public void setPk_org_v ( String pk_org_v) {
this.setAttributeValue( Interest.PK_ORG_V,pk_org_v);
 } 

/** 
* ��ȡ֤ȯ����
*
* @return ֤ȯ����
*/
public String getPk_securities () {
return (String) this.getAttributeValue( Interest.PK_SECURITIES);
 } 

/** 
* ����֤ȯ����
*
* @param pk_securities ֤ȯ����
*/
public void setPk_securities ( String pk_securities) {
this.setAttributeValue( Interest.PK_SECURITIES,pk_securities);
 } 

/** 
* ��ȡ���淽ʽ
*
* @return ���淽ʽ
* @see String
*/
public String getProfittype () {
return (String) this.getAttributeValue( Interest.PROFITTYPE);
 } 

/** 
* �������淽ʽ
*
* @param profittype ���淽ʽ
* @see String
*/
public void setProfittype ( String profittype) {
this.setAttributeValue( Interest.PROFITTYPE,profittype);
 } 

/** 
* ��ȡ��������
*
* @return ��������
* @see String
*/
public String getRatetype () {
return (String) this.getAttributeValue( Interest.RATETYPE);
 } 

/** 
* ������������
*
* @param ratetype ��������
* @see String
*/
public void setRatetype ( String ratetype) {
this.setAttributeValue( Interest.RATETYPE,ratetype);
 } 

/** 
* ��ȡ��Ϣ��
*
* @return ��Ϣ��
*/
public UFDate getStartdate () {
return (UFDate) this.getAttributeValue( Interest.STARTDATE);
 } 

/** 
* ������Ϣ��
*
* @param startdate ��Ϣ��
*/
public void setStartdate ( UFDate startdate) {
this.setAttributeValue( Interest.STARTDATE,startdate);
 } 

/** 
* ��ȡʱ���
*
* @return ʱ���
*/
public UFDateTime getTs () {
return (UFDateTime) this.getAttributeValue( Interest.TS);
 } 

/** 
* ����ʱ���
*
* @param ts ʱ���
*/
public void setTs ( UFDateTime ts) {
this.setAttributeValue( Interest.TS,ts);
 } 

/** 
* ��ȡ�Զ�����1
*
* @return �Զ�����1
*/
public String getVdef1 () {
return (String) this.getAttributeValue( Interest.VDEF1);
 } 

/** 
* �����Զ�����1
*
* @param vdef1 �Զ�����1
*/
public void setVdef1 ( String vdef1) {
this.setAttributeValue( Interest.VDEF1,vdef1);
 } 

/** 
* ��ȡ�Զ�����2
*
* @return �Զ�����2
*/
public String getVdef2 () {
return (String) this.getAttributeValue( Interest.VDEF2);
 } 

/** 
* �����Զ�����2
*
* @param vdef2 �Զ�����2
*/
public void setVdef2 ( String vdef2) {
this.setAttributeValue( Interest.VDEF2,vdef2);
 } 

/** 
* ��ȡ�Զ�����3
*
* @return �Զ�����3
*/
public String getVdef3 () {
return (String) this.getAttributeValue( Interest.VDEF3);
 } 

/** 
* �����Զ�����3
*
* @param vdef3 �Զ�����3
*/
public void setVdef3 ( String vdef3) {
this.setAttributeValue( Interest.VDEF3,vdef3);
 } 

/** 
* ��ȡ�Զ�����4
*
* @return �Զ�����4
*/
public String getVdef4 () {
return (String) this.getAttributeValue( Interest.VDEF4);
 } 

/** 
* �����Զ�����4
*
* @param vdef4 �Զ�����4
*/
public void setVdef4 ( String vdef4) {
this.setAttributeValue( Interest.VDEF4,vdef4);
 } 

/** 
* ��ȡ�Զ�����5
*
* @return �Զ�����5
*/
public String getVdef5 () {
return (String) this.getAttributeValue( Interest.VDEF5);
 } 

/** 
* �����Զ�����5
*
* @param vdef5 �Զ�����5
*/
public void setVdef5 ( String vdef5) {
this.setAttributeValue( Interest.VDEF5,vdef5);
 } 

/** 
* ��ȡƱ������
*
* @return Ʊ������
*/
public UFDouble getYearrate () {
return (UFDouble) this.getAttributeValue( Interest.YEARRATE);
 } 

/** 
* ����Ʊ������
*
* @param yearrate Ʊ������
*/
public void setYearrate ( UFDouble yearrate) {
this.setAttributeValue( Interest.YEARRATE,yearrate);
 } 


  @Override
  public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("fba_sim.sim_interest");
  }
}