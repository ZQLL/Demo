package nc.vo.hxzq.fdll;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class FdllHVO extends SuperVO {
	/**
	 * ������
	 */
	public static final String APPROVER = "approver";
	/**
	 * ҵ������
	 */
	public static final String BUSITYPE = "busitype";
	/**
	 * ����ʱ��
	 */
	public static final String CHANGETIME = "changetime";
	/**
	 * �Ƶ�ʱ��
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * �Ƶ���
	 */
	public static final String CREATOR = "creator";
	/**
	 * ��Դ���ݱ���ID
	 */
	public static final String CSRCBID = "csrcbid";
	/**
	 * ��Դ����ID
	 */
	public static final String CSRCID = "csrcid";
	/**
	 * ��Դ�������ͱ���
	 */
	public static final String CSRCTYPE = "csrctype";
	/**
	 * ��������
	 */
	public static final String CTRANTYPEID = "ctrantypeid";
	/**
	 * ��������
	 */
	public static final String DBILLDATE = "dbilldate";
	/**
	 * �Զ�����1
	 */
	public static final String DEF1 = "def1";
	/**
	 * �Զ�����10
	 */
	public static final String DEF10 = "def10";
	/**
	 * �Զ�����11
	 */
	public static final String DEF11 = "def11";
	/**
	 * �Զ�����12
	 */
	public static final String DEF12 = "def12";
	/**
	 * �Զ�����13
	 */
	public static final String DEF13 = "def13";
	/**
	 * �Զ�����14
	 */
	public static final String DEF14 = "def14";
	/**
	 * �Զ�����15
	 */
	public static final String DEF15 = "def15";
	/**
	 * �Զ�����16
	 */
	public static final String DEF16 = "def16";
	/**
	 * �Զ�����17
	 */
	public static final String DEF17 = "def17";
	/**
	 * �Զ�����18
	 */
	public static final String DEF18 = "def18";
	/**
	 * �Զ�����19
	 */
	public static final String DEF19 = "def19";
	/**
	 * �Զ�����2
	 */
	public static final String DEF2 = "def2";
	/**
	 * �Զ�����20
	 */
	public static final String DEF20 = "def20";
	/**
	 * �Զ�����3
	 */
	public static final String DEF3 = "def3";
	/**
	 * �Զ�����4
	 */
	public static final String DEF4 = "def4";
	/**
	 * �Զ�����5
	 */
	public static final String DEF5 = "def5";
	/**
	 * �Զ�����6
	 */
	public static final String DEF6 = "def6";
	/**
	 * �Զ�����7
	 */
	public static final String DEF7 = "def7";
	/**
	 * �Զ�����8
	 */
	public static final String DEF8 = "def8";
	/**
	 * �Զ�����9
	 */
	public static final String DEF9 = "def9";
	/**
	 * ����״̬
	 */
	public static final String FSTATUSFLAG = "fstatusflag";
	/**
	 * �Ƿ���Ч
	 */
	public static final String ISEFFECT = "iseffect";
	/**
	 * ��׼��������
	 */
	public static final String LL_TPYE = "ll_tpye";
	/**
	 * ����޸�ʱ��
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * ����޸���
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * �������ͱ���
	 */
	public static final String PK_BILLTYPECODE = "pk_billtypecode";
	/**
	 * ��������
	 */
	public static final String PK_BILLTYPEID = "pk_billtypeid";
	/**
	 * ����
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * ������������
	 */
	public static final String PK_HEAD = "pk_head";
	/**
	 * ��֯
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * ��֯�汾
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * ��׼����
	 */
	public static final String RATE = "rate";
	/**
	 * ����ʱ��
	 */
	public static final String TAUDITTIME = "taudittime";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";
	/**
	 * ��������
	 */
	public static final String VAPPROVENOTE = "vapprovenote";
	/**
	 * ���ݺ�
	 */
	public static final String VBILLCODE = "vbillcode";
	/**
	 * ��ע
	 */
	public static final String VNOTE = "vnote";
	/**
	 * �������ͱ���
	 */
	public static final String VTRANTYPECODE = "vtrantypecode";

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getApprover() {
		return (String) this.getAttributeValue(FdllHVO.APPROVER);
	}

	/**
	 * ����������
	 * 
	 * @param approver
	 *            ������
	 */
	public void setApprover(String approver) {
		this.setAttributeValue(FdllHVO.APPROVER, approver);
	}

	/**
	 * ��ȡҵ������
	 * 
	 * @return ҵ������
	 */
	public String getBusitype() {
		return (String) this.getAttributeValue(FdllHVO.BUSITYPE);
	}

	/**
	 * ����ҵ������
	 * 
	 * @param busitype
	 *            ҵ������
	 */
	public void setBusitype(String busitype) {
		this.setAttributeValue(FdllHVO.BUSITYPE, busitype);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getChangetime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.CHANGETIME);
	}

	/**
	 * ���õ���ʱ��
	 * 
	 * @param changetime
	 *            ����ʱ��
	 */
	public void setChangetime(UFDateTime changetime) {
		this.setAttributeValue(FdllHVO.CHANGETIME, changetime);
	}

	/**
	 * ��ȡ�Ƶ�ʱ��
	 * 
	 * @return �Ƶ�ʱ��
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.CREATIONTIME);
	}

	/**
	 * �����Ƶ�ʱ��
	 * 
	 * @param creationtime
	 *            �Ƶ�ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(FdllHVO.CREATIONTIME, creationtime);
	}

	/**
	 * ��ȡ�Ƶ���
	 * 
	 * @return �Ƶ���
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(FdllHVO.CREATOR);
	}

	/**
	 * �����Ƶ���
	 * 
	 * @param creator
	 *            �Ƶ���
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(FdllHVO.CREATOR, creator);
	}

	/**
	 * ��ȡ��Դ���ݱ���ID
	 * 
	 * @return ��Դ���ݱ���ID
	 */
	public String getCsrcbid() {
		return (String) this.getAttributeValue(FdllHVO.CSRCBID);
	}

	/**
	 * ������Դ���ݱ���ID
	 * 
	 * @param csrcbid
	 *            ��Դ���ݱ���ID
	 */
	public void setCsrcbid(String csrcbid) {
		this.setAttributeValue(FdllHVO.CSRCBID, csrcbid);
	}

	/**
	 * ��ȡ��Դ����ID
	 * 
	 * @return ��Դ����ID
	 */
	public String getCsrcid() {
		return (String) this.getAttributeValue(FdllHVO.CSRCID);
	}

	/**
	 * ������Դ����ID
	 * 
	 * @param csrcid
	 *            ��Դ����ID
	 */
	public void setCsrcid(String csrcid) {
		this.setAttributeValue(FdllHVO.CSRCID, csrcid);
	}

	/**
	 * ��ȡ��Դ�������ͱ���
	 * 
	 * @return ��Դ�������ͱ���
	 */
	public String getCsrctype() {
		return (String) this.getAttributeValue(FdllHVO.CSRCTYPE);
	}

	/**
	 * ������Դ�������ͱ���
	 * 
	 * @param csrctype
	 *            ��Դ�������ͱ���
	 */
	public void setCsrctype(String csrctype) {
		this.setAttributeValue(FdllHVO.CSRCTYPE, csrctype);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getCtrantypeid() {
		return (String) this.getAttributeValue(FdllHVO.CTRANTYPEID);
	}

	/**
	 * ���ý�������
	 * 
	 * @param ctrantypeid
	 *            ��������
	 */
	public void setCtrantypeid(String ctrantypeid) {
		this.setAttributeValue(FdllHVO.CTRANTYPEID, ctrantypeid);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getDbilldate() {
		return (UFDate) this.getAttributeValue(FdllHVO.DBILLDATE);
	}

	/**
	 * ���õ�������
	 * 
	 * @param dbilldate
	 *            ��������
	 */
	public void setDbilldate(UFDate dbilldate) {
		this.setAttributeValue(FdllHVO.DBILLDATE, dbilldate);
	}

	/**
	 * ��ȡ�Զ�����1
	 * 
	 * @return �Զ�����1
	 */
	public String getDef1() {
		return (String) this.getAttributeValue(FdllHVO.DEF1);
	}

	/**
	 * �����Զ�����1
	 * 
	 * @param def1
	 *            �Զ�����1
	 */
	public void setDef1(String def1) {
		this.setAttributeValue(FdllHVO.DEF1, def1);
	}

	/**
	 * ��ȡ�Զ�����10
	 * 
	 * @return �Զ�����10
	 */
	public String getDef10() {
		return (String) this.getAttributeValue(FdllHVO.DEF10);
	}

	/**
	 * �����Զ�����10
	 * 
	 * @param def10
	 *            �Զ�����10
	 */
	public void setDef10(String def10) {
		this.setAttributeValue(FdllHVO.DEF10, def10);
	}

	/**
	 * ��ȡ�Զ�����11
	 * 
	 * @return �Զ�����11
	 */
	public String getDef11() {
		return (String) this.getAttributeValue(FdllHVO.DEF11);
	}

	/**
	 * �����Զ�����11
	 * 
	 * @param def11
	 *            �Զ�����11
	 */
	public void setDef11(String def11) {
		this.setAttributeValue(FdllHVO.DEF11, def11);
	}

	/**
	 * ��ȡ�Զ�����12
	 * 
	 * @return �Զ�����12
	 */
	public String getDef12() {
		return (String) this.getAttributeValue(FdllHVO.DEF12);
	}

	/**
	 * �����Զ�����12
	 * 
	 * @param def12
	 *            �Զ�����12
	 */
	public void setDef12(String def12) {
		this.setAttributeValue(FdllHVO.DEF12, def12);
	}

	/**
	 * ��ȡ�Զ�����13
	 * 
	 * @return �Զ�����13
	 */
	public String getDef13() {
		return (String) this.getAttributeValue(FdllHVO.DEF13);
	}

	/**
	 * �����Զ�����13
	 * 
	 * @param def13
	 *            �Զ�����13
	 */
	public void setDef13(String def13) {
		this.setAttributeValue(FdllHVO.DEF13, def13);
	}

	/**
	 * ��ȡ�Զ�����14
	 * 
	 * @return �Զ�����14
	 */
	public String getDef14() {
		return (String) this.getAttributeValue(FdllHVO.DEF14);
	}

	/**
	 * �����Զ�����14
	 * 
	 * @param def14
	 *            �Զ�����14
	 */
	public void setDef14(String def14) {
		this.setAttributeValue(FdllHVO.DEF14, def14);
	}

	/**
	 * ��ȡ�Զ�����15
	 * 
	 * @return �Զ�����15
	 */
	public String getDef15() {
		return (String) this.getAttributeValue(FdllHVO.DEF15);
	}

	/**
	 * �����Զ�����15
	 * 
	 * @param def15
	 *            �Զ�����15
	 */
	public void setDef15(String def15) {
		this.setAttributeValue(FdllHVO.DEF15, def15);
	}

	/**
	 * ��ȡ�Զ�����16
	 * 
	 * @return �Զ�����16
	 */
	public String getDef16() {
		return (String) this.getAttributeValue(FdllHVO.DEF16);
	}

	/**
	 * �����Զ�����16
	 * 
	 * @param def16
	 *            �Զ�����16
	 */
	public void setDef16(String def16) {
		this.setAttributeValue(FdllHVO.DEF16, def16);
	}

	/**
	 * ��ȡ�Զ�����17
	 * 
	 * @return �Զ�����17
	 */
	public String getDef17() {
		return (String) this.getAttributeValue(FdllHVO.DEF17);
	}

	/**
	 * �����Զ�����17
	 * 
	 * @param def17
	 *            �Զ�����17
	 */
	public void setDef17(String def17) {
		this.setAttributeValue(FdllHVO.DEF17, def17);
	}

	/**
	 * ��ȡ�Զ�����18
	 * 
	 * @return �Զ�����18
	 */
	public String getDef18() {
		return (String) this.getAttributeValue(FdllHVO.DEF18);
	}

	/**
	 * �����Զ�����18
	 * 
	 * @param def18
	 *            �Զ�����18
	 */
	public void setDef18(String def18) {
		this.setAttributeValue(FdllHVO.DEF18, def18);
	}

	/**
	 * ��ȡ�Զ�����19
	 * 
	 * @return �Զ�����19
	 */
	public String getDef19() {
		return (String) this.getAttributeValue(FdllHVO.DEF19);
	}

	/**
	 * �����Զ�����19
	 * 
	 * @param def19
	 *            �Զ�����19
	 */
	public void setDef19(String def19) {
		this.setAttributeValue(FdllHVO.DEF19, def19);
	}

	/**
	 * ��ȡ�Զ�����2
	 * 
	 * @return �Զ�����2
	 */
	public String getDef2() {
		return (String) this.getAttributeValue(FdllHVO.DEF2);
	}

	/**
	 * �����Զ�����2
	 * 
	 * @param def2
	 *            �Զ�����2
	 */
	public void setDef2(String def2) {
		this.setAttributeValue(FdllHVO.DEF2, def2);
	}

	/**
	 * ��ȡ�Զ�����20
	 * 
	 * @return �Զ�����20
	 */
	public String getDef20() {
		return (String) this.getAttributeValue(FdllHVO.DEF20);
	}

	/**
	 * �����Զ�����20
	 * 
	 * @param def20
	 *            �Զ�����20
	 */
	public void setDef20(String def20) {
		this.setAttributeValue(FdllHVO.DEF20, def20);
	}

	/**
	 * ��ȡ�Զ�����3
	 * 
	 * @return �Զ�����3
	 */
	public String getDef3() {
		return (String) this.getAttributeValue(FdllHVO.DEF3);
	}

	/**
	 * �����Զ�����3
	 * 
	 * @param def3
	 *            �Զ�����3
	 */
	public void setDef3(String def3) {
		this.setAttributeValue(FdllHVO.DEF3, def3);
	}

	/**
	 * ��ȡ�Զ�����4
	 * 
	 * @return �Զ�����4
	 */
	public String getDef4() {
		return (String) this.getAttributeValue(FdllHVO.DEF4);
	}

	/**
	 * �����Զ�����4
	 * 
	 * @param def4
	 *            �Զ�����4
	 */
	public void setDef4(String def4) {
		this.setAttributeValue(FdllHVO.DEF4, def4);
	}

	/**
	 * ��ȡ�Զ�����5
	 * 
	 * @return �Զ�����5
	 */
	public String getDef5() {
		return (String) this.getAttributeValue(FdllHVO.DEF5);
	}

	/**
	 * �����Զ�����5
	 * 
	 * @param def5
	 *            �Զ�����5
	 */
	public void setDef5(String def5) {
		this.setAttributeValue(FdllHVO.DEF5, def5);
	}

	/**
	 * ��ȡ�Զ�����6
	 * 
	 * @return �Զ�����6
	 */
	public String getDef6() {
		return (String) this.getAttributeValue(FdllHVO.DEF6);
	}

	/**
	 * �����Զ�����6
	 * 
	 * @param def6
	 *            �Զ�����6
	 */
	public void setDef6(String def6) {
		this.setAttributeValue(FdllHVO.DEF6, def6);
	}

	/**
	 * ��ȡ�Զ�����7
	 * 
	 * @return �Զ�����7
	 */
	public String getDef7() {
		return (String) this.getAttributeValue(FdllHVO.DEF7);
	}

	/**
	 * �����Զ�����7
	 * 
	 * @param def7
	 *            �Զ�����7
	 */
	public void setDef7(String def7) {
		this.setAttributeValue(FdllHVO.DEF7, def7);
	}

	/**
	 * ��ȡ�Զ�����8
	 * 
	 * @return �Զ�����8
	 */
	public String getDef8() {
		return (String) this.getAttributeValue(FdllHVO.DEF8);
	}

	/**
	 * �����Զ�����8
	 * 
	 * @param def8
	 *            �Զ�����8
	 */
	public void setDef8(String def8) {
		this.setAttributeValue(FdllHVO.DEF8, def8);
	}

	/**
	 * ��ȡ�Զ�����9
	 * 
	 * @return �Զ�����9
	 */
	public String getDef9() {
		return (String) this.getAttributeValue(FdllHVO.DEF9);
	}

	/**
	 * �����Զ�����9
	 * 
	 * @param def9
	 *            �Զ�����9
	 */
	public void setDef9(String def9) {
		this.setAttributeValue(FdllHVO.DEF9, def9);
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @return ����״̬
	 * @see String
	 */
	public Integer getFstatusflag() {
		return (Integer) this.getAttributeValue(FdllHVO.FSTATUSFLAG);
	}

	/**
	 * ���õ���״̬
	 * 
	 * @param fstatusflag
	 *            ����״̬
	 * @see String
	 */
	public void setFstatusflag(Integer fstatusflag) {
		this.setAttributeValue(FdllHVO.FSTATUSFLAG, fstatusflag);
	}

	/**
	 * ��ȡ�Ƿ���Ч
	 * 
	 * @return �Ƿ���Ч
	 */
	public UFBoolean getIseffect() {
		return (UFBoolean) this.getAttributeValue(FdllHVO.ISEFFECT);
	}

	/**
	 * �����Ƿ���Ч
	 * 
	 * @param iseffect
	 *            �Ƿ���Ч
	 */
	public void setIseffect(UFBoolean iseffect) {
		this.setAttributeValue(FdllHVO.ISEFFECT, iseffect);
	}

	/**
	 * ��ȡ��׼��������
	 * 
	 * @return ��׼��������
	 */
	public String getLl_tpye() {
		return (String) this.getAttributeValue(FdllHVO.LL_TPYE);
	}

	/**
	 * ���û�׼��������
	 * 
	 * @param ll_tpye
	 *            ��׼��������
	 */
	public void setLl_tpye(String ll_tpye) {
		this.setAttributeValue(FdllHVO.LL_TPYE, ll_tpye);
	}

	/**
	 * ��ȡ����޸�ʱ��
	 * 
	 * @return ����޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.MODIFIEDTIME);
	}

	/**
	 * ��������޸�ʱ��
	 * 
	 * @param modifiedtime
	 *            ����޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(FdllHVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * ��ȡ����޸���
	 * 
	 * @return ����޸���
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(FdllHVO.MODIFIER);
	}

	/**
	 * ��������޸���
	 * 
	 * @param modifier
	 *            ����޸���
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(FdllHVO.MODIFIER, modifier);
	}

	/**
	 * ��ȡ�������ͱ���
	 * 
	 * @return �������ͱ���
	 */
	public String getPk_billtypecode() {
		return (String) this.getAttributeValue(FdllHVO.PK_BILLTYPECODE);
	}

	/**
	 * ���õ������ͱ���
	 * 
	 * @param pk_billtypecode
	 *            �������ͱ���
	 */
	public void setPk_billtypecode(String pk_billtypecode) {
		this.setAttributeValue(FdllHVO.PK_BILLTYPECODE, pk_billtypecode);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getPk_billtypeid() {
		return (String) this.getAttributeValue(FdllHVO.PK_BILLTYPEID);
	}

	/**
	 * ���õ�������
	 * 
	 * @param pk_billtypeid
	 *            ��������
	 */
	public void setPk_billtypeid(String pk_billtypeid) {
		this.setAttributeValue(FdllHVO.PK_BILLTYPEID, pk_billtypeid);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(FdllHVO.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(FdllHVO.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ������������
	 * 
	 * @return ������������
	 */
	public String getPk_head() {
		return (String) this.getAttributeValue(FdllHVO.PK_HEAD);
	}

	/**
	 * ���ø�����������
	 * 
	 * @param pk_head
	 *            ������������
	 */
	public void setPk_head(String pk_head) {
		this.setAttributeValue(FdllHVO.PK_HEAD, pk_head);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(FdllHVO.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(FdllHVO.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(FdllHVO.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(FdllHVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ��׼����
	 * 
	 * @return ��׼����
	 */
	public UFDouble getRate() {
		return (UFDouble) this.getAttributeValue(FdllHVO.RATE);
	}

	/**
	 * ���û�׼����
	 * 
	 * @param rate
	 *            ��׼����
	 */
	public void setRate(UFDouble rate) {
		this.setAttributeValue(FdllHVO.RATE, rate);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getTaudittime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.TAUDITTIME);
	}

	/**
	 * ��������ʱ��
	 * 
	 * @param taudittime
	 *            ����ʱ��
	 */
	public void setTaudittime(UFDateTime taudittime) {
		this.setAttributeValue(FdllHVO.TAUDITTIME, taudittime);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(FdllHVO.TS, ts);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getVapprovenote() {
		return (String) this.getAttributeValue(FdllHVO.VAPPROVENOTE);
	}

	/**
	 * ������������
	 * 
	 * @param vapprovenote
	 *            ��������
	 */
	public void setVapprovenote(String vapprovenote) {
		this.setAttributeValue(FdllHVO.VAPPROVENOTE, vapprovenote);
	}

	/**
	 * ��ȡ���ݺ�
	 * 
	 * @return ���ݺ�
	 */
	public String getVbillcode() {
		return (String) this.getAttributeValue(FdllHVO.VBILLCODE);
	}

	/**
	 * ���õ��ݺ�
	 * 
	 * @param vbillcode
	 *            ���ݺ�
	 */
	public void setVbillcode(String vbillcode) {
		this.setAttributeValue(FdllHVO.VBILLCODE, vbillcode);
	}

	/**
	 * ��ȡ��ע
	 * 
	 * @return ��ע
	 */
	public String getVnote() {
		return (String) this.getAttributeValue(FdllHVO.VNOTE);
	}

	/**
	 * ���ñ�ע
	 * 
	 * @param vnote
	 *            ��ע
	 */
	public void setVnote(String vnote) {
		this.setAttributeValue(FdllHVO.VNOTE, vnote);
	}

	/**
	 * ��ȡ�������ͱ���
	 * 
	 * @return �������ͱ���
	 */
	public String getVtrantypecode() {
		return (String) this.getAttributeValue(FdllHVO.VTRANTYPECODE);
	}

	/**
	 * ���ý������ͱ���
	 * 
	 * @param vtrantypecode
	 *            �������ͱ���
	 */
	public void setVtrantypecode(String vtrantypecode) {
		this.setAttributeValue(FdllHVO.VTRANTYPECODE, vtrantypecode);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hxzq.FdllHVO");
	}
}