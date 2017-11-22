package nc.vo.fba_scost.cost.interestdistill;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class InterestdistillVO extends SuperVO {
	/**
	 * ����ʱ��
	 */
	public static final String APPROVEDATE = "approvedate";
	/**
	 * ������
	 */
	public static final String APPROVER = "approver";
	/**
	 * �Ƶ���
	 */
	public static final String BILLMAKER = "billmaker";
	/**
	 * ���ݺ�
	 */
	public static final String BILLNO = "billno";
	/**
	 * �������ͱ���
	 */
	public static final String BILLTYPECODE = "billtypecode";
	/**
	 * ����ʱ��
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * ������
	 */
	public static final String CREATOR = "creator";
	/**
	 * ��������
	 */
	public static final String DISTILL_DATE = "distill_date";
	/**
	 * ����״̬
	 */
	public static final String GENSTATE = "genstate";
	/**
	 * ������Ϣ����
	 */
	public static final String INTERESTDISTILL = "interestdistill";
	/**
	 * �Ƶ�����
	 */
	public static final String MAKEDATE = "makedate";
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
	 * �Զ�����ֵ6
	 */
	public static final String NDEF6 = "ndef6";
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
	 * ��ĩ��Ϣ���
	 */
	public static final String NQCLX = "nqclx";
	/**
	 * �ڳ���Ϣ���
	 */
	public static final String NQMLX = "nqmlx";
	/**
	 * �����ʲ�����
	 */
	public static final String PK_ASSETSPROP = "pk_assetsprop";
	/**
	 * ��������pk
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * �ʽ��˺�
	 */
	public static final String PK_CAPACCOUNT = "pk_capaccount";
	/**
	 * ������λ
	 */
	public static final String PK_CLIENT = "pk_client";
	/**
	 * ��˾
	 */
	public static final String PK_CORP = "pk_corp";
	/**
	 * �ɱ���˷���
	 */
	public static final String PK_COSTPLAN = "pk_costplan";
	/**
	 * �˲�
	 */
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	/**
	 * ����
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * ����
	 */
	public static final String PK_INTERESTDISTILL = "pk_interestdistill";
	/**
	 * �ֻ��ص�
	 */
	public static final String PK_OPERATESITE = "pk_operatesite";
	/**
	 * ��֯
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * ��֯�汾
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * �ɶ��˺�
	 */
	public static final String PK_PARTNACCOUNT = "pk_partnaccount";
	/**
	 * ֤ȯ
	 */
	public static final String PK_SECURITIES = "pk_securities";
	/**
	 * ҵ��С��
	 */
	public static final String PK_SELFSGROUP = "pk_selfsgroup";
	/**
	 * �����֯
	 */
	public static final String PK_STOCKSORT = "pk_stocksort";
	/**
	 * ��������pk
	 */
	public static final String PK_TRANSTYPE = "pk_transtype";
	/**
	 * ƾ֤����
	 */
	public static final String PK_VOUCHER = "pk_voucher";
	/**
	 * ����״̬
	 */
	public static final String STATE = "state";
	/**
	 * �������
	 */
	public static final String STOCKS_NUM = "stocks_num";
	/**
	 * �����
	 */
	public static final String STOCKS_SUM = "stocks_sum";
	/**
	 * �������ͱ���
	 */
	public static final String TRANSTYPECODE = "transtypecode";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";
	/**
	 * �Զ����ַ�1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * �Զ����ַ�10
	 */
	public static final String VDEF10 = "vdef10";
	/**
	 * �Զ����ַ�2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * �Զ����ַ�3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * �Զ����ַ�4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * �Զ����ַ�5
	 */
	public static final String VDEF5 = "vdef5";
	/**
	 * �Զ����ַ�6
	 */
	public static final String VDEF6 = "vdef6";
	/**
	 * �Զ����ַ�7
	 */
	public static final String VDEF7 = "vdef7";
	/**
	 * �Զ����ַ�8
	 */
	public static final String VDEF8 = "vdef8";
	/**
	 * �Զ����ַ�9
	 */
	public static final String VDEF9 = "vdef9";

	/**
	 * ���ۿ�ʼ����
	 */
	public static final String BEGIN_DATE = "begin_date";
	/**
	 * ���۽�������
	 */
	public static final String END_DATE = "end_date";

	/**
	 * ��ȡ���۽�������
	 * 
	 * @return ���۽�������
	 */
	public UFLiteralDate getEnd_date() {
		return (UFLiteralDate) this
				.getAttributeValue(InterestdistillVO.END_DATE);
	}

	/**
	 * �������۽�������
	 * 
	 * @param end_date
	 *            ���۽�������
	 */
	public void setEnd_date(UFLiteralDate end_date) {
		this.setAttributeValue(InterestdistillVO.END_DATE, end_date);
	}

	/**
	 * ��ȡ���ۿ�ʼ����
	 * 
	 * @return ���ۿ�ʼ����
	 */
	public UFLiteralDate getBegin_date() {
		return (UFLiteralDate) this
				.getAttributeValue(InterestdistillVO.BEGIN_DATE);
	}

	/**
	 * �������ۿ�ʼ����
	 * 
	 * @param begin_date
	 *            ���ۿ�ʼ����
	 */
	public void setBegin_date(UFLiteralDate begin_date) {
		this.setAttributeValue(InterestdistillVO.BEGIN_DATE, begin_date);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getApprovedate() {
		return (UFDateTime) this
				.getAttributeValue(InterestdistillVO.APPROVEDATE);
	}

	/**
	 * ��������ʱ��
	 * 
	 * @param approvedate
	 *            ����ʱ��
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.setAttributeValue(InterestdistillVO.APPROVEDATE, approvedate);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getApprover() {
		return (String) this.getAttributeValue(InterestdistillVO.APPROVER);
	}

	/**
	 * ����������
	 * 
	 * @param approver
	 *            ������
	 */
	public void setApprover(String approver) {
		this.setAttributeValue(InterestdistillVO.APPROVER, approver);
	}

	/**
	 * ��ȡ�Ƶ���
	 * 
	 * @return �Ƶ���
	 */
	public String getBillmaker() {
		return (String) this.getAttributeValue(InterestdistillVO.BILLMAKER);
	}

	/**
	 * �����Ƶ���
	 * 
	 * @param billmaker
	 *            �Ƶ���
	 */
	public void setBillmaker(String billmaker) {
		this.setAttributeValue(InterestdistillVO.BILLMAKER, billmaker);
	}

	/**
	 * ��ȡ���ݺ�
	 * 
	 * @return ���ݺ�
	 */
	public String getBillno() {
		return (String) this.getAttributeValue(InterestdistillVO.BILLNO);
	}

	/**
	 * ���õ��ݺ�
	 * 
	 * @param billno
	 *            ���ݺ�
	 */
	public void setBillno(String billno) {
		this.setAttributeValue(InterestdistillVO.BILLNO, billno);
	}

	/**
	 * ��ȡ�������ͱ���
	 * 
	 * @return �������ͱ���
	 */
	public String getBilltypecode() {
		return (String) this.getAttributeValue(InterestdistillVO.BILLTYPECODE);
	}

	/**
	 * ���õ������ͱ���
	 * 
	 * @param billtypecode
	 *            �������ͱ���
	 */
	public void setBilltypecode(String billtypecode) {
		this.setAttributeValue(InterestdistillVO.BILLTYPECODE, billtypecode);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this
				.getAttributeValue(InterestdistillVO.CREATIONTIME);
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param creationtime
	 *            ����ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(InterestdistillVO.CREATIONTIME, creationtime);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(InterestdistillVO.CREATOR);
	}

	/**
	 * ���ô�����
	 * 
	 * @param creator
	 *            ������
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(InterestdistillVO.CREATOR, creator);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getDistill_date() {
		return (UFDate) this.getAttributeValue(InterestdistillVO.DISTILL_DATE);
	}

	/**
	 * ���ü�������
	 * 
	 * @param distill_date
	 *            ��������
	 */
	public void setDistill_date(UFDate distill_date) {
		this.setAttributeValue(InterestdistillVO.DISTILL_DATE, distill_date);
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @return ����״̬
	 * @see String
	 */
	public Integer getGenstate() {
		return (Integer) this.getAttributeValue(InterestdistillVO.GENSTATE);
	}

	/**
	 * ��������״̬
	 * 
	 * @param genstate
	 *            ����״̬
	 * @see String
	 */
	public void setGenstate(Integer genstate) {
		this.setAttributeValue(InterestdistillVO.GENSTATE, genstate);
	}

	/**
	 * ��ȡ������Ϣ����
	 * 
	 * @return ������Ϣ����
	 */
	public UFDouble getInterestdistill() {
		return (UFDouble) this
				.getAttributeValue(InterestdistillVO.INTERESTDISTILL);
	}

	/**
	 * ���ñ�����Ϣ����
	 * 
	 * @param interestdistill
	 *            ������Ϣ����
	 */
	public void setInterestdistill(UFDouble interestdistill) {
		this.setAttributeValue(InterestdistillVO.INTERESTDISTILL,
				interestdistill);
	}

	/**
	 * ��ȡ�Ƶ�����
	 * 
	 * @return �Ƶ�����
	 */
	public UFDateTime getMakedate() {
		return (UFDateTime) this.getAttributeValue(InterestdistillVO.MAKEDATE);
	}

	/**
	 * �����Ƶ�����
	 * 
	 * @param makedate
	 *            �Ƶ�����
	 */
	public void setMakedate(UFDateTime makedate) {
		this.setAttributeValue(InterestdistillVO.MAKEDATE, makedate);
	}

	/**
	 * ��ȡ�޸�ʱ��
	 * 
	 * @return �޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this
				.getAttributeValue(InterestdistillVO.MODIFIEDTIME);
	}

	/**
	 * �����޸�ʱ��
	 * 
	 * @param modifiedtime
	 *            �޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(InterestdistillVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * ��ȡ�޸���
	 * 
	 * @return �޸���
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(InterestdistillVO.MODIFIER);
	}

	/**
	 * �����޸���
	 * 
	 * @param modifier
	 *            �޸���
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(InterestdistillVO.MODIFIER, modifier);
	}

	/**
	 * ��ȡ�Զ�����ֵ1
	 * 
	 * @return �Զ�����ֵ1
	 */
	public UFDouble getNdef1() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF1);
	}

	/**
	 * �����Զ�����ֵ1
	 * 
	 * @param ndef1
	 *            �Զ�����ֵ1
	 */
	public void setNdef1(UFDouble ndef1) {
		this.setAttributeValue(InterestdistillVO.NDEF1, ndef1);
	}

	/**
	 * ��ȡ�Զ�����ֵ10
	 * 
	 * @return �Զ�����ֵ10
	 */
	public UFDouble getNdef10() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF10);
	}

	/**
	 * �����Զ�����ֵ10
	 * 
	 * @param ndef10
	 *            �Զ�����ֵ10
	 */
	public void setNdef10(UFDouble ndef10) {
		this.setAttributeValue(InterestdistillVO.NDEF10, ndef10);
	}

	/**
	 * ��ȡ�Զ�����ֵ2
	 * 
	 * @return �Զ�����ֵ2
	 */
	public UFDouble getNdef2() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF2);
	}

	/**
	 * �����Զ�����ֵ2
	 * 
	 * @param ndef2
	 *            �Զ�����ֵ2
	 */
	public void setNdef2(UFDouble ndef2) {
		this.setAttributeValue(InterestdistillVO.NDEF2, ndef2);
	}

	/**
	 * ��ȡ�Զ�����ֵ3
	 * 
	 * @return �Զ�����ֵ3
	 */
	public UFDouble getNdef3() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF3);
	}

	/**
	 * �����Զ�����ֵ3
	 * 
	 * @param ndef3
	 *            �Զ�����ֵ3
	 */
	public void setNdef3(UFDouble ndef3) {
		this.setAttributeValue(InterestdistillVO.NDEF3, ndef3);
	}

	/**
	 * ��ȡ�Զ�����ֵ4
	 * 
	 * @return �Զ�����ֵ4
	 */
	public UFDouble getNdef4() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF4);
	}

	/**
	 * �����Զ�����ֵ4
	 * 
	 * @param ndef4
	 *            �Զ�����ֵ4
	 */
	public void setNdef4(UFDouble ndef4) {
		this.setAttributeValue(InterestdistillVO.NDEF4, ndef4);
	}

	/**
	 * ��ȡ�Զ�����ֵ5
	 * 
	 * @return �Զ�����ֵ5
	 */
	public UFDouble getNdef5() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF5);
	}

	/**
	 * �����Զ�����ֵ5
	 * 
	 * @param ndef5
	 *            �Զ�����ֵ5
	 */
	public void setNdef5(UFDouble ndef5) {
		this.setAttributeValue(InterestdistillVO.NDEF5, ndef5);
	}

	/**
	 * ��ȡ�Զ�����ֵ6
	 * 
	 * @return �Զ�����ֵ6
	 */
	public UFDouble getNdef6() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF6);
	}

	/**
	 * �����Զ�����ֵ6
	 * 
	 * @param ndef6
	 *            �Զ�����ֵ6
	 */
	public void setNdef6(UFDouble ndef6) {
		this.setAttributeValue(InterestdistillVO.NDEF6, ndef6);
	}

	/**
	 * ��ȡ�Զ�����ֵ7
	 * 
	 * @return �Զ�����ֵ7
	 */
	public UFDouble getNdef7() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF7);
	}

	/**
	 * �����Զ�����ֵ7
	 * 
	 * @param ndef7
	 *            �Զ�����ֵ7
	 */
	public void setNdef7(UFDouble ndef7) {
		this.setAttributeValue(InterestdistillVO.NDEF7, ndef7);
	}

	/**
	 * ��ȡ�Զ�����ֵ8
	 * 
	 * @return �Զ�����ֵ8
	 */
	public UFDouble getNdef8() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF8);
	}

	/**
	 * �����Զ�����ֵ8
	 * 
	 * @param ndef8
	 *            �Զ�����ֵ8
	 */
	public void setNdef8(UFDouble ndef8) {
		this.setAttributeValue(InterestdistillVO.NDEF8, ndef8);
	}

	/**
	 * ��ȡ�Զ�����ֵ9
	 * 
	 * @return �Զ�����ֵ9
	 */
	public UFDouble getNdef9() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF9);
	}

	/**
	 * �����Զ�����ֵ9
	 * 
	 * @param ndef9
	 *            �Զ�����ֵ9
	 */
	public void setNdef9(UFDouble ndef9) {
		this.setAttributeValue(InterestdistillVO.NDEF9, ndef9);
	}

	/**
	 * ��ȡ��ĩ��Ϣ���
	 * 
	 * @return ��ĩ��Ϣ���
	 */
	public UFDouble getNqclx() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NQCLX);
	}

	/**
	 * ������ĩ��Ϣ���
	 * 
	 * @param nqclx
	 *            ��ĩ��Ϣ���
	 */
	public void setNqclx(UFDouble nqclx) {
		this.setAttributeValue(InterestdistillVO.NQCLX, nqclx);
	}

	/**
	 * ��ȡ�ڳ���Ϣ���
	 * 
	 * @return �ڳ���Ϣ���
	 */
	public UFDouble getNqmlx() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NQMLX);
	}

	/**
	 * �����ڳ���Ϣ���
	 * 
	 * @param nqmlx
	 *            �ڳ���Ϣ���
	 */
	public void setNqmlx(UFDouble nqmlx) {
		this.setAttributeValue(InterestdistillVO.NQMLX, nqmlx);
	}

	/**
	 * ��ȡ�����ʲ�����
	 * 
	 * @return �����ʲ�����
	 */
	public String getPk_assetsprop() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_ASSETSPROP);
	}

	/**
	 * ���ý����ʲ�����
	 * 
	 * @param pk_assetsprop
	 *            �����ʲ�����
	 */
	public void setPk_assetsprop(String pk_assetsprop) {
		this.setAttributeValue(InterestdistillVO.PK_ASSETSPROP, pk_assetsprop);
	}

	/**
	 * ��ȡ��������pk
	 * 
	 * @return ��������pk
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_BILLTYPE);
	}

	/**
	 * ���õ�������pk
	 * 
	 * @param pk_billtype
	 *            ��������pk
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(InterestdistillVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * ��ȡ�ʽ��˺�
	 * 
	 * @return �ʽ��˺�
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_CAPACCOUNT);
	}

	/**
	 * �����ʽ��˺�
	 * 
	 * @param pk_capaccount
	 *            �ʽ��˺�
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(InterestdistillVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * ��ȡ������λ
	 * 
	 * @return ������λ
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_CLIENT);
	}

	/**
	 * ����������λ
	 * 
	 * @param pk_client
	 *            ������λ
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(InterestdistillVO.PK_CLIENT, pk_client);
	}

	/**
	 * ��ȡ��˾
	 * 
	 * @return ��˾
	 */
	public String getPk_corp() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_CORP);
	}

	/**
	 * ���ù�˾
	 * 
	 * @param pk_corp
	 *            ��˾
	 */
	public void setPk_corp(String pk_corp) {
		this.setAttributeValue(InterestdistillVO.PK_CORP, pk_corp);
	}

	/**
	 * ��ȡ�ɱ���˷���
	 * 
	 * @return �ɱ���˷���
	 */
	public String getPk_costplan() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_COSTPLAN);
	}

	/**
	 * ���óɱ���˷���
	 * 
	 * @param pk_costplan
	 *            �ɱ���˷���
	 */
	public void setPk_costplan(String pk_costplan) {
		this.setAttributeValue(InterestdistillVO.PK_COSTPLAN, pk_costplan);
	}

	/**
	 * ��ȡ�˲�
	 * 
	 * @return �˲�
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_GLORGBOOK);
	}

	/**
	 * �����˲�
	 * 
	 * @param pk_glorgbook
	 *            �˲�
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(InterestdistillVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(InterestdistillVO.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_interestdistill() {
		return (String) this
				.getAttributeValue(InterestdistillVO.PK_INTERESTDISTILL);
	}

	/**
	 * ��������
	 * 
	 * @param pk_interestdistill
	 *            ����
	 */
	public void setPk_interestdistill(String pk_interestdistill) {
		this.setAttributeValue(InterestdistillVO.PK_INTERESTDISTILL,
				pk_interestdistill);
	}

	/**
	 * ��ȡ�ֻ��ص�
	 * 
	 * @return �ֻ��ص�
	 */
	public String getPk_operatesite() {
		return (String) this
				.getAttributeValue(InterestdistillVO.PK_OPERATESITE);
	}

	/**
	 * ���÷ֻ��ص�
	 * 
	 * @param pk_operatesite
	 *            �ֻ��ص�
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(InterestdistillVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(InterestdistillVO.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(InterestdistillVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ�ɶ��˺�
	 * 
	 * @return �ɶ��˺�
	 */
	public String getPk_partnaccount() {
		return (String) this
				.getAttributeValue(InterestdistillVO.PK_PARTNACCOUNT);
	}

	/**
	 * ���ùɶ��˺�
	 * 
	 * @param pk_partnaccount
	 *            �ɶ��˺�
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(InterestdistillVO.PK_PARTNACCOUNT,
				pk_partnaccount);
	}

	/**
	 * ��ȡ֤ȯ
	 * 
	 * @return ֤ȯ
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_SECURITIES);
	}

	/**
	 * ����֤ȯ
	 * 
	 * @param pk_securities
	 *            ֤ȯ
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(InterestdistillVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * ��ȡҵ��С��
	 * 
	 * @return ҵ��С��
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_SELFSGROUP);
	}

	/**
	 * ����ҵ��С��
	 * 
	 * @param pk_selfsgroup
	 *            ҵ��С��
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(InterestdistillVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * ��ȡ�����֯
	 * 
	 * @return �����֯
	 */
	public String getPk_stocksort() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_STOCKSORT);
	}

	/**
	 * ���ÿ����֯
	 * 
	 * @param pk_stocksort
	 *            �����֯
	 */
	public void setPk_stocksort(String pk_stocksort) {
		this.setAttributeValue(InterestdistillVO.PK_STOCKSORT, pk_stocksort);
	}

	/**
	 * ��ȡ��������pk
	 * 
	 * @return ��������pk
	 */
	public String getPk_transtype() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_TRANSTYPE);
	}

	/**
	 * ���ý�������pk
	 * 
	 * @param pk_transtype
	 *            ��������pk
	 */
	public void setPk_transtype(String pk_transtype) {
		this.setAttributeValue(InterestdistillVO.PK_TRANSTYPE, pk_transtype);
	}

	/**
	 * ��ȡƾ֤����
	 * 
	 * @return ƾ֤����
	 */
	public String getPk_voucher() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_VOUCHER);
	}

	/**
	 * ����ƾ֤����
	 * 
	 * @param pk_voucher
	 *            ƾ֤����
	 */
	public void setPk_voucher(String pk_voucher) {
		this.setAttributeValue(InterestdistillVO.PK_VOUCHER, pk_voucher);
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @return ����״̬
	 * @see String
	 */
	public Integer getState() {
		return (Integer) this.getAttributeValue(InterestdistillVO.STATE);
	}

	/**
	 * ���õ���״̬
	 * 
	 * @param state
	 *            ����״̬
	 * @see String
	 */
	public void setState(Integer state) {
		this.setAttributeValue(InterestdistillVO.STATE, state);
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return �������
	 */
	public UFDouble getStocks_num() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.STOCKS_NUM);
	}

	/**
	 * ���ÿ������
	 * 
	 * @param stocks_num
	 *            �������
	 */
	public void setStocks_num(UFDouble stocks_num) {
		this.setAttributeValue(InterestdistillVO.STOCKS_NUM, stocks_num);
	}

	/**
	 * ��ȡ�����
	 * 
	 * @return �����
	 */
	public UFDouble getStocks_sum() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.STOCKS_SUM);
	}

	/**
	 * ���ÿ����
	 * 
	 * @param stocks_sum
	 *            �����
	 */
	public void setStocks_sum(UFDouble stocks_sum) {
		this.setAttributeValue(InterestdistillVO.STOCKS_SUM, stocks_sum);
	}

	/**
	 * ��ȡ�������ͱ���
	 * 
	 * @return �������ͱ���
	 */
	public String getTranstypecode() {
		return (String) this.getAttributeValue(InterestdistillVO.TRANSTYPECODE);
	}

	/**
	 * ���ý������ͱ���
	 * 
	 * @param transtypecode
	 *            �������ͱ���
	 */
	public void setTranstypecode(String transtypecode) {
		this.setAttributeValue(InterestdistillVO.TRANSTYPECODE, transtypecode);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(InterestdistillVO.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(InterestdistillVO.TS, ts);
	}

	/**
	 * ��ȡ�Զ����ַ�1
	 * 
	 * @return �Զ����ַ�1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF1);
	}

	/**
	 * �����Զ����ַ�1
	 * 
	 * @param vdef1
	 *            �Զ����ַ�1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(InterestdistillVO.VDEF1, vdef1);
	}

	/**
	 * ��ȡ�Զ����ַ�10
	 * 
	 * @return �Զ����ַ�10
	 */
	public String getVdef10() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF10);
	}

	/**
	 * �����Զ����ַ�10
	 * 
	 * @param vdef10
	 *            �Զ����ַ�10
	 */
	public void setVdef10(String vdef10) {
		this.setAttributeValue(InterestdistillVO.VDEF10, vdef10);
	}

	/**
	 * ��ȡ�Զ����ַ�2
	 * 
	 * @return �Զ����ַ�2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF2);
	}

	/**
	 * �����Զ����ַ�2
	 * 
	 * @param vdef2
	 *            �Զ����ַ�2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(InterestdistillVO.VDEF2, vdef2);
	}

	/**
	 * ��ȡ�Զ����ַ�3
	 * 
	 * @return �Զ����ַ�3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF3);
	}

	/**
	 * �����Զ����ַ�3
	 * 
	 * @param vdef3
	 *            �Զ����ַ�3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(InterestdistillVO.VDEF3, vdef3);
	}

	/**
	 * ��ȡ�Զ����ַ�4
	 * 
	 * @return �Զ����ַ�4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF4);
	}

	/**
	 * �����Զ����ַ�4
	 * 
	 * @param vdef4
	 *            �Զ����ַ�4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(InterestdistillVO.VDEF4, vdef4);
	}

	/**
	 * ��ȡ�Զ����ַ�5
	 * 
	 * @return �Զ����ַ�5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF5);
	}

	/**
	 * �����Զ����ַ�5
	 * 
	 * @param vdef5
	 *            �Զ����ַ�5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(InterestdistillVO.VDEF5, vdef5);
	}

	/**
	 * ��ȡ�Զ����ַ�6
	 * 
	 * @return �Զ����ַ�6
	 */
	public String getVdef6() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF6);
	}

	/**
	 * �����Զ����ַ�6
	 * 
	 * @param vdef6
	 *            �Զ����ַ�6
	 */
	public void setVdef6(String vdef6) {
		this.setAttributeValue(InterestdistillVO.VDEF6, vdef6);
	}

	/**
	 * ��ȡ�Զ����ַ�7
	 * 
	 * @return �Զ����ַ�7
	 */
	public String getVdef7() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF7);
	}

	/**
	 * �����Զ����ַ�7
	 * 
	 * @param vdef7
	 *            �Զ����ַ�7
	 */
	public void setVdef7(String vdef7) {
		this.setAttributeValue(InterestdistillVO.VDEF7, vdef7);
	}

	/**
	 * ��ȡ�Զ����ַ�8
	 * 
	 * @return �Զ����ַ�8
	 */
	public String getVdef8() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF8);
	}

	/**
	 * �����Զ����ַ�8
	 * 
	 * @param vdef8
	 *            �Զ����ַ�8
	 */
	public void setVdef8(String vdef8) {
		this.setAttributeValue(InterestdistillVO.VDEF8, vdef8);
	}

	/**
	 * ��ȡ�Զ����ַ�9
	 * 
	 * @return �Զ����ַ�9
	 */
	public String getVdef9() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF9);
	}

	/**
	 * �����Զ����ַ�9
	 * 
	 * @param vdef9
	 *            �Զ����ַ�9
	 */
	public void setVdef9(String vdef9) {
		this.setAttributeValue(InterestdistillVO.VDEF9, vdef9);
	}

	@Override
	
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta(
				"fba_scost.sim_interestdistill");
	}
}