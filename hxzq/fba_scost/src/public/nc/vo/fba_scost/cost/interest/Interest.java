package nc.vo.fba_scost.cost.interest;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * ������������VO
 * 
 * @author qs
 * @since 2017-5-19����3:49:29
 */
public class Interest extends SuperVO {
	private static final long serialVersionUID = 1L;
	/**
	 * ��Ȩ�ں�Ʊ������
	 */
	public static final String AFTEXCISDATERATE = "aftexcisdaterate";
	/**
	 * ����ʱ��
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * ������
	 */
	public static final String CREATOR = "creator";
	/**
	 * �Զ�����ֵ6
	 */
	public static final String DDEF6 = "ddef6";
	/**
	 * �Զ����ַ�1
	 */
	public static final String DDEV1 = "ddev1";
	/**
	 * �Զ����ַ�2
	 */
	public static final String DDEV2 = "ddev2";
	/**
	 * �Զ����ַ�3
	 */
	public static final String DDEV3 = "ddev3";
	/**
	 * �Զ����ַ�4
	 */
	public static final String DDEV4 = "ddev4";
	/**
	 * �Զ����ַ�5
	 */
	public static final String DDEV5 = "ddev5";
	/**
	 * �ۿ���
	 */
	public static final String DISCOUNTRATE = "discountrate";
	/**
	 * ������
	 */
	public static final String ENDDATE = "enddate";
	/**
	 * ��Ȩ������
	 */
	public static final String EXERCISEDATE = "exercisedate";
	/**
	 * ����
	 */
	public static final String INTERESTSPREAD = "interestspread";
	/**
	 * �Ƿ���Ȩ
	 */
	public static final String ISEXERCISE = "isexercise";
	/**
	 * ������
	 */
	public static final String ISSUEDATE = "issuedate";
	/**
	 * �޸�ʱ��
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * �޸���
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * �Զ�����ֵ1
	 */
	public static final String NDEF1 = "ndef1";
	/**
	 * �Զ�����ֵ10
	 */
	public static final String NDEF10 = "ndef10";
	/**
	 * �Զ�����ֵ2
	 */
	public static final String NDEF2 = "ndef2";
	/**
	 * �Զ�����ֵ3
	 */
	public static final String NDEF3 = "ndef3";
	/**
	 * �Զ�����ֵ4
	 */
	public static final String NDEF4 = "ndef4";
	/**
	 * �Զ�����ֵ5
	 */
	public static final String NDEF5 = "ndef5";
	/**
	 * �Զ�����ֵ7
	 */
	public static final String NDEF7 = "ndef7";
	/**
	 * �Զ�����ֵ8
	 */
	public static final String NDEF8 = "ndef8";
	/**
	 * �Զ�����ֵ9
	 */
	public static final String NDEF9 = "ndef9";
	/**
	 * ��Ϣ��ʽ
	 */
	public static final String PAYTYPE = "paytype";
	/**
	 * ��Ϣ������
	 */
	public static final String PERIODSUM = "periodsum";
	/**
	 * ��Ϣ����
	 */
	public static final String PERIODTYPE = "periodtype";
	/**
	 * ��׼��������
	 */
	public static final String PK_CRITERIONRATETYPE = "pk_criterionratetype";
	/**
	 * ����
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * ����
	 */
	public static final String PK_INTEREST = "pk_interest";
	/**
	 * ��֯
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * ��֯�汾
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * ֤ȯ����
	 */
	public static final String PK_SECURITIES = "pk_securities";
	/**
	 * ���淽ʽ
	 */
	public static final String PROFITTYPE = "profittype";
	/**
	 * ��������
	 */
	public static final String RATETYPE = "ratetype";
	/**
	 * ��Ϣ��
	 */
	public static final String STARTDATE = "startdate";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";
	/**
	 * �Զ�����1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * �Զ�����2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * �Զ�����3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * �Զ�����4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * �Զ�����5
	 */
	public static final String VDEF5 = "vdef5";
	/**
	 * Ʊ������
	 */
	public static final String YEARRATE = "yearrate";

	/**
	 * ��ȡ��Ȩ�ں�Ʊ������
	 * 
	 * @return ��Ȩ�ں�Ʊ������
	 */
	public UFDouble getAftexcisdaterate() {
		return (UFDouble) this.getAttributeValue(Interest.AFTEXCISDATERATE);
	}

	/**
	 * ������Ȩ�ں�Ʊ������
	 * 
	 * @param aftexcisdaterate
	 *            ��Ȩ�ں�Ʊ������
	 */
	public void setAftexcisdaterate(UFDouble aftexcisdaterate) {
		this.setAttributeValue(Interest.AFTEXCISDATERATE, aftexcisdaterate);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(Interest.CREATIONTIME);
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param creationtime
	 *            ����ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(Interest.CREATIONTIME, creationtime);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(Interest.CREATOR);
	}

	/**
	 * ���ô�����
	 * 
	 * @param creator
	 *            ������
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(Interest.CREATOR, creator);
	}

	/**
	 * ��ȡ�Զ�����ֵ6
	 * 
	 * @return �Զ�����ֵ6
	 */
	public UFDouble getDdef6() {
		return (UFDouble) this.getAttributeValue(Interest.DDEF6);
	}

	/**
	 * �����Զ�����ֵ6
	 * 
	 * @param ddef6
	 *            �Զ�����ֵ6
	 */
	public void setDdef6(UFDouble ddef6) {
		this.setAttributeValue(Interest.DDEF6, ddef6);
	}

	/**
	 * ��ȡ�Զ����ַ�1
	 * 
	 * @return �Զ����ַ�1
	 */
	public String getDdev1() {
		return (String) this.getAttributeValue(Interest.DDEV1);
	}

	/**
	 * �����Զ����ַ�1
	 * 
	 * @param ddev1
	 *            �Զ����ַ�1
	 */
	public void setDdev1(String ddev1) {
		this.setAttributeValue(Interest.DDEV1, ddev1);
	}

	/**
	 * ��ȡ�Զ����ַ�2
	 * 
	 * @return �Զ����ַ�2
	 */
	public String getDdev2() {
		return (String) this.getAttributeValue(Interest.DDEV2);
	}

	/**
	 * �����Զ����ַ�2
	 * 
	 * @param ddev2
	 *            �Զ����ַ�2
	 */
	public void setDdev2(String ddev2) {
		this.setAttributeValue(Interest.DDEV2, ddev2);
	}

	/**
	 * ��ȡ�Զ����ַ�3
	 * 
	 * @return �Զ����ַ�3
	 */
	public String getDdev3() {
		return (String) this.getAttributeValue(Interest.DDEV3);
	}

	/**
	 * �����Զ����ַ�3
	 * 
	 * @param ddev3
	 *            �Զ����ַ�3
	 */
	public void setDdev3(String ddev3) {
		this.setAttributeValue(Interest.DDEV3, ddev3);
	}

	/**
	 * ��ȡ�Զ����ַ�4
	 * 
	 * @return �Զ����ַ�4
	 */
	public String getDdev4() {
		return (String) this.getAttributeValue(Interest.DDEV4);
	}

	/**
	 * �����Զ����ַ�4
	 * 
	 * @param ddev4
	 *            �Զ����ַ�4
	 */
	public void setDdev4(String ddev4) {
		this.setAttributeValue(Interest.DDEV4, ddev4);
	}

	/**
	 * ��ȡ�Զ����ַ�5
	 * 
	 * @return �Զ����ַ�5
	 */
	public String getDdev5() {
		return (String) this.getAttributeValue(Interest.DDEV5);
	}

	/**
	 * �����Զ����ַ�5
	 * 
	 * @param ddev5
	 *            �Զ����ַ�5
	 */
	public void setDdev5(String ddev5) {
		this.setAttributeValue(Interest.DDEV5, ddev5);
	}

	/**
	 * ��ȡ�ۿ���
	 * 
	 * @return �ۿ���
	 */
	public UFDouble getDiscountrate() {
		return (UFDouble) this.getAttributeValue(Interest.DISCOUNTRATE);
	}

	/**
	 * �����ۿ���
	 * 
	 * @param discountrate
	 *            �ۿ���
	 */
	public void setDiscountrate(UFDouble discountrate) {
		this.setAttributeValue(Interest.DISCOUNTRATE, discountrate);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public UFDate getEnddate() {
		return (UFDate) this.getAttributeValue(Interest.ENDDATE);
	}

	/**
	 * ���õ�����
	 * 
	 * @param enddate
	 *            ������
	 */
	public void setEnddate(UFDate enddate) {
		this.setAttributeValue(Interest.ENDDATE, enddate);
	}

	/**
	 * ��ȡ��Ȩ������
	 * 
	 * @return ��Ȩ������
	 */
	public UFDate getExercisedate() {
		return (UFDate) this.getAttributeValue(Interest.EXERCISEDATE);
	}

	/**
	 * ������Ȩ������
	 * 
	 * @param exercisedate
	 *            ��Ȩ������
	 */
	public void setExercisedate(UFDate exercisedate) {
		this.setAttributeValue(Interest.EXERCISEDATE, exercisedate);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public UFDouble getInterestspread() {
		return (UFDouble) this.getAttributeValue(Interest.INTERESTSPREAD);
	}

	/**
	 * ��������
	 * 
	 * @param interestspread
	 *            ����
	 */
	public void setInterestspread(UFDouble interestspread) {
		this.setAttributeValue(Interest.INTERESTSPREAD, interestspread);
	}

	/**
	 * ��ȡ�Ƿ���Ȩ
	 * 
	 * @return �Ƿ���Ȩ
	 * @see String
	 */
	public String getIsexercise() {
		return (String) this.getAttributeValue(Interest.ISEXERCISE);
	}

	/**
	 * �����Ƿ���Ȩ
	 * 
	 * @param isexercise
	 *            �Ƿ���Ȩ
	 * @see String
	 */
	public void setIsexercise(String isexercise) {
		this.setAttributeValue(Interest.ISEXERCISE, isexercise);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public UFDate getIssuedate() {
		return (UFDate) this.getAttributeValue(Interest.ISSUEDATE);
	}

	/**
	 * ���÷�����
	 * 
	 * @param issuedate
	 *            ������
	 */
	public void setIssuedate(UFDate issuedate) {
		this.setAttributeValue(Interest.ISSUEDATE, issuedate);
	}

	/**
	 * ��ȡ�޸�ʱ��
	 * 
	 * @return �޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(Interest.MODIFIEDTIME);
	}

	/**
	 * �����޸�ʱ��
	 * 
	 * @param modifiedtime
	 *            �޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(Interest.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * ��ȡ�޸���
	 * 
	 * @return �޸���
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(Interest.MODIFIER);
	}

	/**
	 * �����޸���
	 * 
	 * @param modifier
	 *            �޸���
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(Interest.MODIFIER, modifier);
	}

	/**
	 * ��ȡ�Զ�����ֵ1
	 * 
	 * @return �Զ�����ֵ1
	 */
	public UFDouble getNdef1() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF1);
	}

	/**
	 * �����Զ�����ֵ1
	 * 
	 * @param ndef1
	 *            �Զ�����ֵ1
	 */
	public void setNdef1(UFDouble ndef1) {
		this.setAttributeValue(Interest.NDEF1, ndef1);
	}

	/**
	 * ��ȡ�Զ�����ֵ10
	 * 
	 * @return �Զ�����ֵ10
	 */
	public UFDouble getNdef10() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF10);
	}

	/**
	 * �����Զ�����ֵ10
	 * 
	 * @param ndef10
	 *            �Զ�����ֵ10
	 */
	public void setNdef10(UFDouble ndef10) {
		this.setAttributeValue(Interest.NDEF10, ndef10);
	}

	/**
	 * ��ȡ�Զ�����ֵ2
	 * 
	 * @return �Զ�����ֵ2
	 */
	public UFDouble getNdef2() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF2);
	}

	/**
	 * �����Զ�����ֵ2
	 * 
	 * @param ndef2
	 *            �Զ�����ֵ2
	 */
	public void setNdef2(UFDouble ndef2) {
		this.setAttributeValue(Interest.NDEF2, ndef2);
	}

	/**
	 * ��ȡ�Զ�����ֵ3
	 * 
	 * @return �Զ�����ֵ3
	 */
	public UFDouble getNdef3() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF3);
	}

	/**
	 * �����Զ�����ֵ3
	 * 
	 * @param ndef3
	 *            �Զ�����ֵ3
	 */
	public void setNdef3(UFDouble ndef3) {
		this.setAttributeValue(Interest.NDEF3, ndef3);
	}

	/**
	 * ��ȡ�Զ�����ֵ4
	 * 
	 * @return �Զ�����ֵ4
	 */
	public UFDouble getNdef4() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF4);
	}

	/**
	 * �����Զ�����ֵ4
	 * 
	 * @param ndef4
	 *            �Զ�����ֵ4
	 */
	public void setNdef4(UFDouble ndef4) {
		this.setAttributeValue(Interest.NDEF4, ndef4);
	}

	/**
	 * ��ȡ�Զ�����ֵ5
	 * 
	 * @return �Զ�����ֵ5
	 */
	public UFDouble getNdef5() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF5);
	}

	/**
	 * �����Զ�����ֵ5
	 * 
	 * @param ndef5
	 *            �Զ�����ֵ5
	 */
	public void setNdef5(UFDouble ndef5) {
		this.setAttributeValue(Interest.NDEF5, ndef5);
	}

	/**
	 * ��ȡ�Զ�����ֵ7
	 * 
	 * @return �Զ�����ֵ7
	 */
	public UFDouble getNdef7() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF7);
	}

	/**
	 * �����Զ�����ֵ7
	 * 
	 * @param ndef7
	 *            �Զ�����ֵ7
	 */
	public void setNdef7(UFDouble ndef7) {
		this.setAttributeValue(Interest.NDEF7, ndef7);
	}

	/**
	 * ��ȡ�Զ�����ֵ8
	 * 
	 * @return �Զ�����ֵ8
	 */
	public UFDouble getNdef8() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF8);
	}

	/**
	 * �����Զ�����ֵ8
	 * 
	 * @param ndef8
	 *            �Զ�����ֵ8
	 */
	public void setNdef8(UFDouble ndef8) {
		this.setAttributeValue(Interest.NDEF8, ndef8);
	}

	/**
	 * ��ȡ�Զ�����ֵ9
	 * 
	 * @return �Զ�����ֵ9
	 */
	public UFDouble getNdef9() {
		return (UFDouble) this.getAttributeValue(Interest.NDEF9);
	}

	/**
	 * �����Զ�����ֵ9
	 * 
	 * @param ndef9
	 *            �Զ�����ֵ9
	 */
	public void setNdef9(UFDouble ndef9) {
		this.setAttributeValue(Interest.NDEF9, ndef9);
	}

	/**
	 * ��ȡ��Ϣ��ʽ
	 * 
	 * @return ��Ϣ��ʽ
	 * @see String
	 */
	public String getPaytype() {
		return (String) this.getAttributeValue(Interest.PAYTYPE);
	}

	/**
	 * ���ø�Ϣ��ʽ
	 * 
	 * @param paytype
	 *            ��Ϣ��ʽ
	 * @see String
	 */
	public void setPaytype(String paytype) {
		this.setAttributeValue(Interest.PAYTYPE, paytype);
	}

	/**
	 * ��ȡ��Ϣ������
	 * 
	 * @return ��Ϣ������
	 */
	public Integer getPeriodsum() {
		return (Integer) this.getAttributeValue(Interest.PERIODSUM);
	}

	/**
	 * ���ü�Ϣ������
	 * 
	 * @param periodsum
	 *            ��Ϣ������
	 */
	public void setPeriodsum(Integer periodsum) {
		this.setAttributeValue(Interest.PERIODSUM, periodsum);
	}

	/**
	 * ��ȡ��Ϣ����
	 * 
	 * @return ��Ϣ����
	 * @see String
	 */
	public String getPeriodtype() {
		return (String) this.getAttributeValue(Interest.PERIODTYPE);
	}

	/**
	 * ���ø�Ϣ����
	 * 
	 * @param periodtype
	 *            ��Ϣ����
	 * @see String
	 */
	public void setPeriodtype(String periodtype) {
		this.setAttributeValue(Interest.PERIODTYPE, periodtype);
	}

	/**
	 * ��ȡ��׼��������
	 * 
	 * @return ��׼��������
	 */
	public String getPk_criterionratetype() {
		return (String) this.getAttributeValue(Interest.PK_CRITERIONRATETYPE);
	}

	/**
	 * ���û�׼��������
	 * 
	 * @param pk_criterionratetype
	 *            ��׼��������
	 */
	public void setPk_criterionratetype(String pk_criterionratetype) {
		this.setAttributeValue(Interest.PK_CRITERIONRATETYPE,
				pk_criterionratetype);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(Interest.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(Interest.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_interest() {
		return (String) this.getAttributeValue(Interest.PK_INTEREST);
	}

	/**
	 * ��������
	 * 
	 * @param pk_interest
	 *            ����
	 */
	public void setPk_interest(String pk_interest) {
		this.setAttributeValue(Interest.PK_INTEREST, pk_interest);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(Interest.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(Interest.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(Interest.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(Interest.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ֤ȯ����
	 * 
	 * @return ֤ȯ����
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(Interest.PK_SECURITIES);
	}

	/**
	 * ����֤ȯ����
	 * 
	 * @param pk_securities
	 *            ֤ȯ����
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(Interest.PK_SECURITIES, pk_securities);
	}

	/**
	 * ��ȡ���淽ʽ
	 * 
	 * @return ���淽ʽ
	 * @see String
	 */
	public String getProfittype() {
		return (String) this.getAttributeValue(Interest.PROFITTYPE);
	}

	/**
	 * �������淽ʽ
	 * 
	 * @param profittype
	 *            ���淽ʽ
	 * @see String
	 */
	public void setProfittype(String profittype) {
		this.setAttributeValue(Interest.PROFITTYPE, profittype);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 * @see String
	 */
	public String getRatetype() {
		return (String) this.getAttributeValue(Interest.RATETYPE);
	}

	/**
	 * ������������
	 * 
	 * @param ratetype
	 *            ��������
	 * @see String
	 */
	public void setRatetype(String ratetype) {
		this.setAttributeValue(Interest.RATETYPE, ratetype);
	}

	/**
	 * ��ȡ��Ϣ��
	 * 
	 * @return ��Ϣ��
	 */
	public UFDate getStartdate() {
		return (UFDate) this.getAttributeValue(Interest.STARTDATE);
	}

	/**
	 * ������Ϣ��
	 * 
	 * @param startdate
	 *            ��Ϣ��
	 */
	public void setStartdate(UFDate startdate) {
		this.setAttributeValue(Interest.STARTDATE, startdate);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(Interest.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(Interest.TS, ts);
	}

	/**
	 * ��ȡ�Զ�����1
	 * 
	 * @return �Զ�����1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(Interest.VDEF1);
	}

	/**
	 * �����Զ�����1
	 * 
	 * @param vdef1
	 *            �Զ�����1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(Interest.VDEF1, vdef1);
	}

	/**
	 * ��ȡ�Զ�����2
	 * 
	 * @return �Զ�����2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(Interest.VDEF2);
	}

	/**
	 * �����Զ�����2
	 * 
	 * @param vdef2
	 *            �Զ�����2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(Interest.VDEF2, vdef2);
	}

	/**
	 * ��ȡ�Զ�����3
	 * 
	 * @return �Զ�����3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(Interest.VDEF3);
	}

	/**
	 * �����Զ�����3
	 * 
	 * @param vdef3
	 *            �Զ�����3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(Interest.VDEF3, vdef3);
	}

	/**
	 * ��ȡ�Զ�����4
	 * 
	 * @return �Զ�����4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(Interest.VDEF4);
	}

	/**
	 * �����Զ�����4
	 * 
	 * @param vdef4
	 *            �Զ�����4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(Interest.VDEF4, vdef4);
	}

	/**
	 * ��ȡ�Զ�����5
	 * 
	 * @return �Զ�����5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(Interest.VDEF5);
	}

	/**
	 * �����Զ�����5
	 * 
	 * @param vdef5
	 *            �Զ�����5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(Interest.VDEF5, vdef5);
	}

	/**
	 * ��ȡƱ������
	 * 
	 * @return Ʊ������
	 */
	public UFDouble getYearrate() {
		return (UFDouble) this.getAttributeValue(Interest.YEARRATE);
	}

	/**
	 * ����Ʊ������
	 * 
	 * @param yearrate
	 *            Ʊ������
	 */
	public void setYearrate(UFDouble yearrate) {
		this.setAttributeValue(Interest.YEARRATE, yearrate);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_sim.sim_interest");
	}
}