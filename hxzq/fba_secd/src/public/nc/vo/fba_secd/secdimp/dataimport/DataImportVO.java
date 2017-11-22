package nc.vo.fba_secd.secdimp.dataimport;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class DataImportVO extends SuperVO {
	/**
	 * Ŀ���
	 */
	public static final String AIM_TABLE = "aim_table";
	/**
	 * ����ʱ��
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * ������
	 */
	public static final String CREATOR = "creator";
	/**
	 * ������Դ
	 */
	public static final String DATAFROM = "datafrom";
	/**
	 * �Զ�����1
	 */
	public static final String DEF1 = "def1";
	/**
	 * �Զ�����2
	 */
	public static final String DEF2 = "def2";
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
	 * �Ƿ�ҵ�񵥾�
	 */
	public static final String ISBUSIBILL = "isbusibill";
	/**
	 * �޸�ʱ��
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * �޸���
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * ��������
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * ����
	 */
	public static final String PK_DATAIMPORT = "pk_dataimport";
	/**
	 * ����
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * ������Ŀ����
	 */
	public static final String PK_IMPORTPROJ = "pk_importproj";
	/**
	 * Ԫ����
	 */
	public static final String PK_METADATA = "pk_metadata";
	/**
	 * ��֯
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * ��֯�汾
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * �����ֶ�
	 */
	public static final String RELEVAFIELDNAME = "relevafieldname";
	/**
	 * Դ����
	 */
	public static final String SD_DESCRIBE = "sd_describe";
	/**
	 * ���⴦����
	 */
	public static final String SPECIALCLASS = "specialclass";
	/**
	 * ���⴦���ļ�
	 */
	public static final String SPECIALFILE = "specialfile";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";

	/**
	 * ��ȡĿ���
	 * 
	 * @return Ŀ���
	 */
	public String getAim_table() {
		return (String) this.getAttributeValue(DataImportVO.AIM_TABLE);
	}

	/**
	 * ����Ŀ���
	 * 
	 * @param aim_table
	 *            Ŀ���
	 */
	public void setAim_table(String aim_table) {
		this.setAttributeValue(DataImportVO.AIM_TABLE, aim_table);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(DataImportVO.CREATIONTIME);
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param creationtime
	 *            ����ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(DataImportVO.CREATIONTIME, creationtime);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(DataImportVO.CREATOR);
	}

	/**
	 * ���ô�����
	 * 
	 * @param creator
	 *            ������
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(DataImportVO.CREATOR, creator);
	}

	/**
	 * ��ȡ������Դ
	 * 
	 * @return ������Դ
	 * @see String
	 */
	public Integer getDatafrom() {
		return (Integer) this.getAttributeValue(DataImportVO.DATAFROM);
	}

	/**
	 * ����������Դ
	 * 
	 * @param datafrom
	 *            ������Դ
	 * @see String
	 */
	public void setDatafrom(Integer datafrom) {
		this.setAttributeValue(DataImportVO.DATAFROM, datafrom);
	}

	/**
	 * ��ȡ�Զ�����1
	 * 
	 * @return �Զ�����1
	 */
	public String getDef1() {
		return (String) this.getAttributeValue(DataImportVO.DEF1);
	}

	/**
	 * �����Զ�����1
	 * 
	 * @param def1
	 *            �Զ�����1
	 */
	public void setDef1(String def1) {
		this.setAttributeValue(DataImportVO.DEF1, def1);
	}

	/**
	 * ��ȡ�Զ�����2
	 * 
	 * @return �Զ�����2
	 */
	public String getDef2() {
		return (String) this.getAttributeValue(DataImportVO.DEF2);
	}

	/**
	 * �����Զ�����2
	 * 
	 * @param def2
	 *            �Զ�����2
	 */
	public void setDef2(String def2) {
		this.setAttributeValue(DataImportVO.DEF2, def2);
	}

	/**
	 * ��ȡ�Զ�����3
	 * 
	 * @return �Զ�����3
	 */
	public String getDef3() {
		return (String) this.getAttributeValue(DataImportVO.DEF3);
	}

	/**
	 * �����Զ�����3
	 * 
	 * @param def3
	 *            �Զ�����3
	 */
	public void setDef3(String def3) {
		this.setAttributeValue(DataImportVO.DEF3, def3);
	}

	/**
	 * ��ȡ�Զ�����4
	 * 
	 * @return �Զ�����4
	 */
	public String getDef4() {
		return (String) this.getAttributeValue(DataImportVO.DEF4);
	}

	/**
	 * �����Զ�����4
	 * 
	 * @param def4
	 *            �Զ�����4
	 */
	public void setDef4(String def4) {
		this.setAttributeValue(DataImportVO.DEF4, def4);
	}

	/**
	 * ��ȡ�Զ�����5
	 * 
	 * @return �Զ�����5
	 */
	public String getDef5() {
		return (String) this.getAttributeValue(DataImportVO.DEF5);
	}

	/**
	 * �����Զ�����5
	 * 
	 * @param def5
	 *            �Զ�����5
	 */
	public void setDef5(String def5) {
		this.setAttributeValue(DataImportVO.DEF5, def5);
	}

	/**
	 * ��ȡ�Ƿ�ҵ�񵥾�
	 * 
	 * @return �Ƿ�ҵ�񵥾�
	 */
	public UFBoolean getIsbusibill() {
		return (UFBoolean) this.getAttributeValue(DataImportVO.ISBUSIBILL);
	}

	/**
	 * �����Ƿ�ҵ�񵥾�
	 * 
	 * @param isbusibill
	 *            �Ƿ�ҵ�񵥾�
	 */
	public void setIsbusibill(UFBoolean isbusibill) {
		this.setAttributeValue(DataImportVO.ISBUSIBILL, isbusibill);
	}

	/**
	 * ��ȡ�޸�ʱ��
	 * 
	 * @return �޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(DataImportVO.MODIFIEDTIME);
	}

	/**
	 * �����޸�ʱ��
	 * 
	 * @param modifiedtime
	 *            �޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(DataImportVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * ��ȡ�޸���
	 * 
	 * @return �޸���
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(DataImportVO.MODIFIER);
	}

	/**
	 * �����޸���
	 * 
	 * @param modifier
	 *            �޸���
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(DataImportVO.MODIFIER, modifier);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(DataImportVO.PK_BILLTYPE);
	}

	/**
	 * ���õ�������
	 * 
	 * @param pk_billtype
	 *            ��������
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(DataImportVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_dataimport() {
		return (String) this.getAttributeValue(DataImportVO.PK_DATAIMPORT);
	}

	/**
	 * ��������
	 * 
	 * @param pk_dataimport
	 *            ����
	 */
	public void setPk_dataimport(String pk_dataimport) {
		this.setAttributeValue(DataImportVO.PK_DATAIMPORT, pk_dataimport);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(DataImportVO.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(DataImportVO.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ������Ŀ����
	 * 
	 * @return ������Ŀ����
	 */
	public String getPk_importproj() {
		return (String) this.getAttributeValue(DataImportVO.PK_IMPORTPROJ);
	}

	/**
	 * ���õ�����Ŀ����
	 * 
	 * @param pk_importproj
	 *            ������Ŀ����
	 */
	public void setPk_importproj(String pk_importproj) {
		this.setAttributeValue(DataImportVO.PK_IMPORTPROJ, pk_importproj);
	}

	/**
	 * ��ȡԪ����
	 * 
	 * @return Ԫ����
	 */
	public String getPk_metadata() {
		return (String) this.getAttributeValue(DataImportVO.PK_METADATA);
	}

	/**
	 * ����Ԫ����
	 * 
	 * @param pk_metadata
	 *            Ԫ����
	 */
	public void setPk_metadata(String pk_metadata) {
		this.setAttributeValue(DataImportVO.PK_METADATA, pk_metadata);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(DataImportVO.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(DataImportVO.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(DataImportVO.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(DataImportVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ�����ֶ�
	 * 
	 * @return �����ֶ�
	 * @see String
	 */
	public String getRelevafieldname() {
		return (String) this.getAttributeValue(DataImportVO.RELEVAFIELDNAME);
	}

	/**
	 * ���ù����ֶ�
	 * 
	 * @param relevafieldname
	 *            �����ֶ�
	 * @see String
	 */
	public void setRelevafieldname(String relevafieldname) {
		this.setAttributeValue(DataImportVO.RELEVAFIELDNAME, relevafieldname);
	}

	/**
	 * ��ȡԴ����
	 * 
	 * @return Դ����
	 * @see String
	 */
	public String getSd_describe() {
		return (String) this.getAttributeValue(DataImportVO.SD_DESCRIBE);
	}

	/**
	 * ����Դ����
	 * 
	 * @param sd_describe
	 *            Դ����
	 * @see String
	 */
	public void setSd_describe(String sd_describe) {
		this.setAttributeValue(DataImportVO.SD_DESCRIBE, sd_describe);
	}

	/**
	 * ��ȡ���⴦����
	 * 
	 * @return ���⴦����
	 */
	public String getSpecialclass() {
		return (String) this.getAttributeValue(DataImportVO.SPECIALCLASS);
	}

	/**
	 * �������⴦����
	 * 
	 * @param specialclass
	 *            ���⴦����
	 */
	public void setSpecialclass(String specialclass) {
		this.setAttributeValue(DataImportVO.SPECIALCLASS, specialclass);
	}

	/**
	 * ��ȡ���⴦���ļ�
	 * 
	 * @return ���⴦���ļ�
	 */
	public String getSpecialfile() {
		return (String) this.getAttributeValue(DataImportVO.SPECIALFILE);
	}

	/**
	 * �������⴦���ļ�
	 * 
	 * @param specialfile
	 *            ���⴦���ļ�
	 */
	public void setSpecialfile(String specialfile) {
		this.setAttributeValue(DataImportVO.SPECIALFILE, specialfile);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(DataImportVO.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(DataImportVO.TS, ts);
	}

	@Override
	
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_secd.DataImportVO");
	}
}