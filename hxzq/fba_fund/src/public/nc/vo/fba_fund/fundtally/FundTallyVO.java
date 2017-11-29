package nc.vo.fba_fund.fundtally;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class FundTallyVO extends SuperVO {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	/**
	 * ���ۿ�ʼ����
	 */
	public static final String BEGIN_DATE = "begin_date";
	/**
	 * �״ν�����
	 */
	public static final String BEGINDATE = "begindate";
	/**
	 * ���Ὺʼ��
	 */
	public static final String BEGINDISTILL_DATE = "begindistill_date";
	/**
	 * ��ͬ��
	 */
	public static final String CONTRACTNO = "contractno";
	/**
	 * ���
	 */
	public static final String DENOMINATION = "denomination";
	/**
	 * ���۽�������
	 */
	public static final String END_DATE = "end_date";
	/**
	 * ���ڽ�����
	 */
	public static final String ENDDATE = "enddate";
	/**
	 * ִ��Ʊ������
	 */
	public static final String EXCUTEFACERATE = "excutefacerate";
	/**
	 * ��������
	 */
	public static final String FUNDTYPE = "fundtype";
	/**
	 * �Ѽ�����Ϣ
	 */
	public static final String HAD_INTEREST = "had_interest";
	/**
	 * ���������
	 */
	public static final String INDISTILL_DATE = "indistill_date";
	/**
	 * �Ƿ���
	 */
	public static final String ISDQ = "isdq";
	/**
	 * ����Ʊ������
	 */
	public static final String ISSUEFACERATE = "issuefacerate";
	/**
	 * ������
	 */
	public static final String MANAGEDEPT = "managedept";
	/**
	 * �г�
	 */
	public static final String MARKET = "market";
	/**
	 * �Զ�����6
	 */
	public static final String NDEF1 = "ndef1";
	/**
	 * �Զ�����7
	 */
	public static final String NDEF2 = "ndef2";
	/**
	 * �Զ�����8
	 */
	public static final String NDEF3 = "ndef3";
	/**
	 * �Զ�����9
	 */
	public static final String NDEF4 = "ndef4";
	/**
	 * �Զ�����10
	 */
	public static final String NDEF5 = "ndef5";
	/**
	 * ҵ�����
	 */
	public static final String OPERCODE = "opercode";
	/**
	 * ��������
	 */
	public static final String OPERDATE = "operdate";
	/**
	 * ����
	 */
	public static final String OPERINDEX = "operindex";
	/**
	 * ��Ϣ��ʽ
	 */
	public static final String PAYTYPE = "paytype";
	/**
	 * ��������
	 */
	public static final String PK_BILL = "pk_bill";
	/**
	 * ��������
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * ������
	 */
	public static final String PK_BOURSE = "pk_bourse";
	/**
	 * ҵ������
	 */
	public static final String PK_BUSITYPE = "pk_busitype";
	/**
	 * �ʽ��˺�
	 */
	public static final String PK_CAPACCOUNT = "pk_capaccount";
	/**
	 * ������λ
	 */
	public static final String PK_CLIENT = "pk_client";
	/**
	 * ����
	 */
	public static final String PK_CURRTYPE = "pk_currtype";
	/**
	 * ����
	 */
	public static final String PK_DEPT = "pk_dept";
	/**
	 * ����
	 */
	public static final String PK_FUNDTALLY = "pk_fundtally";
	/**
	 * �����˲�
	 */
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	/**
	 * ����
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * �ع�Ʒ��
	 */
	public static final String PK_HGTYPE = "pk_hgtype";
	/**
	 * ҵ���������
	 */
	public static final String PK_OPBILL = "pk_opbill";
	/**
	 * �ֻ��ص�
	 */
	public static final String PK_OPERATESITE = "pk_operatesite";
	/**
	 * ҵ�������������
	 */
	public static final String PK_OPTYPE = "pk_optype";
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
	 * ҵ��С��
	 */
	public static final String PK_SELFSGROUP = "pk_selfsgroup";
	/**
	 * ��Ʒ����ַ�����
	 */
	public static final String PRODUCTORCOUNTERPARTY = "productorcounterparty";
	/**
	 * ʵ������
	 */
	public static final String REALRATE = "realrate";
	/**
	 * ���μ�����Ϣ
	 */
	public static final String THIS_INTEREST = "this_interest";
	/**
	 * ����
	 */
	public static final String TIMELIMIT = "timelimit";
	/**
	 * ��������
	 */
	public static final String TRADE_DATE = "trade_date";
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
	 * ���Ϣ����
	 */
	public static final String YEARCALDAYS = "yearcaldays";

	/**
	 * ��ȡ���ۿ�ʼ����
	 * 
	 * @return ���ۿ�ʼ����
	 */
	public UFLiteralDate getBegin_date() {
		return (UFLiteralDate) this.getAttributeValue(FundTallyVO.BEGIN_DATE);
	}

	/**
	 * �������ۿ�ʼ����
	 * 
	 * @param begin_date
	 *            ���ۿ�ʼ����
	 */
	public void setBegin_date(UFLiteralDate begin_date) {
		this.setAttributeValue(FundTallyVO.BEGIN_DATE, begin_date);
	}

	/**
	 * ��ȡ�״ν�����
	 * 
	 * @return �״ν�����
	 */
	public UFDate getBegindate() {
		return (UFDate) this.getAttributeValue(FundTallyVO.BEGINDATE);
	}

	/**
	 * �����״ν�����
	 * 
	 * @param begindate
	 *            �״ν�����
	 */
	public void setBegindate(UFDate begindate) {
		this.setAttributeValue(FundTallyVO.BEGINDATE, begindate);
	}

	/**
	 * ��ȡ���Ὺʼ��
	 * 
	 * @return ���Ὺʼ��
	 */
	public UFDate getBegindistill_date() {
		return (UFDate) this.getAttributeValue(FundTallyVO.BEGINDISTILL_DATE);
	}

	/**
	 * ���ü��Ὺʼ��
	 * 
	 * @param begindistill_date
	 *            ���Ὺʼ��
	 */
	public void setBegindistill_date(UFDate begindistill_date) {
		this.setAttributeValue(FundTallyVO.BEGINDISTILL_DATE, begindistill_date);
	}

	/**
	 * ��ȡ��ͬ��
	 * 
	 * @return ��ͬ��
	 */
	public String getContractno() {
		return (String) this.getAttributeValue(FundTallyVO.CONTRACTNO);
	}

	/**
	 * ���ú�ͬ��
	 * 
	 * @param contractno
	 *            ��ͬ��
	 */
	public void setContractno(String contractno) {
		this.setAttributeValue(FundTallyVO.CONTRACTNO, contractno);
	}

	/**
	 * ��ȡ���
	 * 
	 * @return ���
	 */
	public UFDouble getDenomination() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.DENOMINATION);
	}

	/**
	 * �������
	 * 
	 * @param denomination
	 *            ���
	 */
	public void setDenomination(UFDouble denomination) {
		this.setAttributeValue(FundTallyVO.DENOMINATION, denomination);
	}

	/**
	 * ��ȡ���۽�������
	 * 
	 * @return ���۽�������
	 */
	public UFLiteralDate getEnd_date() {
		return (UFLiteralDate) this.getAttributeValue(FundTallyVO.END_DATE);
	}

	/**
	 * �������۽�������
	 * 
	 * @param end_date
	 *            ���۽�������
	 */
	public void setEnd_date(UFLiteralDate end_date) {
		this.setAttributeValue(FundTallyVO.END_DATE, end_date);
	}

	/**
	 * ��ȡ���ڽ�����
	 * 
	 * @return ���ڽ�����
	 */
	public UFDate getEnddate() {
		return (UFDate) this.getAttributeValue(FundTallyVO.ENDDATE);
	}

	/**
	 * ���õ��ڽ�����
	 * 
	 * @param enddate
	 *            ���ڽ�����
	 */
	public void setEnddate(UFDate enddate) {
		this.setAttributeValue(FundTallyVO.ENDDATE, enddate);
	}

	/**
	 * ��ȡִ��Ʊ������
	 * 
	 * @return ִ��Ʊ������
	 */
	public UFDouble getExcutefacerate() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.EXCUTEFACERATE);
	}

	/**
	 * ����ִ��Ʊ������
	 * 
	 * @param excutefacerate
	 *            ִ��Ʊ������
	 */
	public void setExcutefacerate(UFDouble excutefacerate) {
		this.setAttributeValue(FundTallyVO.EXCUTEFACERATE, excutefacerate);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 * @see String
	 */
	public Integer getFundtype() {
		return (Integer) this.getAttributeValue(FundTallyVO.FUNDTYPE);
	}

	/**
	 * ������������
	 * 
	 * @param fundtype
	 *            ��������
	 * @see String
	 */
	public void setFundtype(Integer fundtype) {
		this.setAttributeValue(FundTallyVO.FUNDTYPE, fundtype);
	}

	/**
	 * ��ȡ�Ѽ�����Ϣ
	 * 
	 * @return �Ѽ�����Ϣ
	 */
	public UFDouble getHad_interest() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.HAD_INTEREST);
	}

	/**
	 * �����Ѽ�����Ϣ
	 * 
	 * @param had_interest
	 *            �Ѽ�����Ϣ
	 */
	public void setHad_interest(UFDouble had_interest) {
		this.setAttributeValue(FundTallyVO.HAD_INTEREST, had_interest);
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return ���������
	 */
	public UFDate getIndistill_date() {
		return (UFDate) this.getAttributeValue(FundTallyVO.INDISTILL_DATE);
	}

	/**
	 * ���ü��������
	 * 
	 * @param indistill_date
	 *            ���������
	 */
	public void setIndistill_date(UFDate indistill_date) {
		this.setAttributeValue(FundTallyVO.INDISTILL_DATE, indistill_date);
	}

	/**
	 * ��ȡ�Ƿ���
	 * 
	 * @return �Ƿ���
	 */
	public UFBoolean getIsdq() {
		return (UFBoolean) this.getAttributeValue(FundTallyVO.ISDQ);
	}

	/**
	 * �����Ƿ���
	 * 
	 * @param isdq
	 *            �Ƿ���
	 */
	public void setIsdq(UFBoolean isdq) {
		this.setAttributeValue(FundTallyVO.ISDQ, isdq);
	}

	/**
	 * ��ȡ����Ʊ������
	 * 
	 * @return ����Ʊ������
	 */
	public UFDouble getIssuefacerate() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.ISSUEFACERATE);
	}

	/**
	 * ���÷���Ʊ������
	 * 
	 * @param issuefacerate
	 *            ����Ʊ������
	 */
	public void setIssuefacerate(UFDouble issuefacerate) {
		this.setAttributeValue(FundTallyVO.ISSUEFACERATE, issuefacerate);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getManagedept() {
		return (String) this.getAttributeValue(FundTallyVO.MANAGEDEPT);
	}

	/**
	 * ���ù�����
	 * 
	 * @param managedept
	 *            ������
	 */
	public void setManagedept(String managedept) {
		this.setAttributeValue(FundTallyVO.MANAGEDEPT, managedept);
	}

	/**
	 * ��ȡ�г�
	 * 
	 * @return �г�
	 */
	public Integer getMarket() {
		return (Integer) this.getAttributeValue(FundTallyVO.MARKET);
	}

	/**
	 * �����г�
	 * 
	 * @param market
	 *            �г�
	 */
	public void setMarket(Integer market) {
		this.setAttributeValue(FundTallyVO.MARKET, market);
	}

	/**
	 * ��ȡ�Զ�����6
	 * 
	 * @return �Զ�����6
	 */
	public UFDouble getNdef1() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF1);
	}

	/**
	 * �����Զ�����6
	 * 
	 * @param ndef1
	 *            �Զ�����6
	 */
	public void setNdef1(UFDouble ndef1) {
		this.setAttributeValue(FundTallyVO.NDEF1, ndef1);
	}

	/**
	 * ��ȡ�Զ�����7
	 * 
	 * @return �Զ�����7
	 */
	public UFDouble getNdef2() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF2);
	}

	/**
	 * �����Զ�����7
	 * 
	 * @param ndef2
	 *            �Զ�����7
	 */
	public void setNdef2(UFDouble ndef2) {
		this.setAttributeValue(FundTallyVO.NDEF2, ndef2);
	}

	/**
	 * ��ȡ�Զ�����8
	 * 
	 * @return �Զ�����8
	 */
	public UFDouble getNdef3() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF3);
	}

	/**
	 * �����Զ�����8
	 * 
	 * @param ndef3
	 *            �Զ�����8
	 */
	public void setNdef3(UFDouble ndef3) {
		this.setAttributeValue(FundTallyVO.NDEF3, ndef3);
	}

	/**
	 * ��ȡ�Զ�����9
	 * 
	 * @return �Զ�����9
	 */
	public UFDouble getNdef4() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF4);
	}

	/**
	 * �����Զ�����9
	 * 
	 * @param ndef4
	 *            �Զ�����9
	 */
	public void setNdef4(UFDouble ndef4) {
		this.setAttributeValue(FundTallyVO.NDEF4, ndef4);
	}

	/**
	 * ��ȡ�Զ�����10
	 * 
	 * @return �Զ�����10
	 */
	public UFDouble getNdef5() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF5);
	}

	/**
	 * �����Զ�����10
	 * 
	 * @param ndef5
	 *            �Զ�����10
	 */
	public void setNdef5(UFDouble ndef5) {
		this.setAttributeValue(FundTallyVO.NDEF5, ndef5);
	}

	/**
	 * ��ȡҵ�����
	 * 
	 * @return ҵ�����
	 */
	public String getOpercode() {
		return (String) this.getAttributeValue(FundTallyVO.OPERCODE);
	}

	/**
	 * ����ҵ�����
	 * 
	 * @param opercode
	 *            ҵ�����
	 */
	public void setOpercode(String opercode) {
		this.setAttributeValue(FundTallyVO.OPERCODE, opercode);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getOperdate() {
		return (UFDate) this.getAttributeValue(FundTallyVO.OPERDATE);
	}

	/**
	 * ���ò�������
	 * 
	 * @param operdate
	 *            ��������
	 */
	public void setOperdate(UFDate operdate) {
		this.setAttributeValue(FundTallyVO.OPERDATE, operdate);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public Integer getOperindex() {
		return (Integer) this.getAttributeValue(FundTallyVO.OPERINDEX);
	}

	/**
	 * ��������
	 * 
	 * @param operindex
	 *            ����
	 */
	public void setOperindex(Integer operindex) {
		this.setAttributeValue(FundTallyVO.OPERINDEX, operindex);
	}

	/**
	 * ��ȡ��Ϣ��ʽ
	 * 
	 * @return ��Ϣ��ʽ
	 */
	public Integer getPaytype() {
		return (Integer) this.getAttributeValue(FundTallyVO.PAYTYPE);
	}

	/**
	 * ���ø�Ϣ��ʽ
	 * 
	 * @param paytype
	 *            ��Ϣ��ʽ
	 */
	public void setPaytype(Integer paytype) {
		this.setAttributeValue(FundTallyVO.PAYTYPE, paytype);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getPk_bill() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BILL);
	}

	/**
	 * ���õ�������
	 * 
	 * @param pk_bill
	 *            ��������
	 */
	public void setPk_bill(String pk_bill) {
		this.setAttributeValue(FundTallyVO.PK_BILL, pk_bill);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BILLTYPE);
	}

	/**
	 * ���õ�������
	 * 
	 * @param pk_billtype
	 *            ��������
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(FundTallyVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getPk_bourse() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BOURSE);
	}

	/**
	 * ���ý�����
	 * 
	 * @param pk_bourse
	 *            ������
	 */
	public void setPk_bourse(String pk_bourse) {
		this.setAttributeValue(FundTallyVO.PK_BOURSE, pk_bourse);
	}

	/**
	 * ��ȡҵ������
	 * 
	 * @return ҵ������
	 */
	public String getPk_busitype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BUSITYPE);
	}

	/**
	 * ����ҵ������
	 * 
	 * @param pk_busitype
	 *            ҵ������
	 */
	public void setPk_busitype(String pk_busitype) {
		this.setAttributeValue(FundTallyVO.PK_BUSITYPE, pk_busitype);
	}

	/**
	 * ��ȡ�ʽ��˺�
	 * 
	 * @return �ʽ��˺�
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(FundTallyVO.PK_CAPACCOUNT);
	}

	/**
	 * �����ʽ��˺�
	 * 
	 * @param pk_capaccount
	 *            �ʽ��˺�
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(FundTallyVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * ��ȡ������λ
	 * 
	 * @return ������λ
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(FundTallyVO.PK_CLIENT);
	}

	/**
	 * ����������λ
	 * 
	 * @param pk_client
	 *            ������λ
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(FundTallyVO.PK_CLIENT, pk_client);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_currtype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_CURRTYPE);
	}

	/**
	 * ���ñ���
	 * 
	 * @param pk_currtype
	 *            ����
	 */
	public void setPk_currtype(String pk_currtype) {
		this.setAttributeValue(FundTallyVO.PK_CURRTYPE, pk_currtype);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_dept() {
		return (String) this.getAttributeValue(FundTallyVO.PK_DEPT);
	}

	/**
	 * ���ò���
	 * 
	 * @param pk_dept
	 *            ����
	 */
	public void setPk_dept(String pk_dept) {
		this.setAttributeValue(FundTallyVO.PK_DEPT, pk_dept);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_fundtally() {
		return (String) this.getAttributeValue(FundTallyVO.PK_FUNDTALLY);
	}

	/**
	 * ��������
	 * 
	 * @param pk_fundtally
	 *            ����
	 */
	public void setPk_fundtally(String pk_fundtally) {
		this.setAttributeValue(FundTallyVO.PK_FUNDTALLY, pk_fundtally);
	}

	/**
	 * ��ȡ�����˲�
	 * 
	 * @return �����˲�
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(FundTallyVO.PK_GLORGBOOK);
	}

	/**
	 * ���������˲�
	 * 
	 * @param pk_glorgbook
	 *            �����˲�
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(FundTallyVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(FundTallyVO.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(FundTallyVO.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ�ع�Ʒ��
	 * 
	 * @return �ع�Ʒ��
	 */
	public String getPk_hgtype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_HGTYPE);
	}

	/**
	 * ���ûع�Ʒ��
	 * 
	 * @param pk_hgtype
	 *            �ع�Ʒ��
	 */
	public void setPk_hgtype(String pk_hgtype) {
		this.setAttributeValue(FundTallyVO.PK_HGTYPE, pk_hgtype);
	}

	/**
	 * ��ȡҵ���������
	 * 
	 * @return ҵ���������
	 */
	public String getPk_opbill() {
		return (String) this.getAttributeValue(FundTallyVO.PK_OPBILL);
	}

	/**
	 * ����ҵ���������
	 * 
	 * @param pk_opbill
	 *            ҵ���������
	 */
	public void setPk_opbill(String pk_opbill) {
		this.setAttributeValue(FundTallyVO.PK_OPBILL, pk_opbill);
	}

	/**
	 * ��ȡ�ֻ��ص�
	 * 
	 * @return �ֻ��ص�
	 */
	public String getPk_operatesite() {
		return (String) this.getAttributeValue(FundTallyVO.PK_OPERATESITE);
	}

	/**
	 * ���÷ֻ��ص�
	 * 
	 * @param pk_operatesite
	 *            �ֻ��ص�
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(FundTallyVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * ��ȡҵ�������������
	 * 
	 * @return ҵ�������������
	 */
	public String getPk_optype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_OPTYPE);
	}

	/**
	 * ����ҵ�������������
	 * 
	 * @param pk_optype
	 *            ҵ�������������
	 */
	public void setPk_optype(String pk_optype) {
		this.setAttributeValue(FundTallyVO.PK_OPTYPE, pk_optype);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(FundTallyVO.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(FundTallyVO.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(FundTallyVO.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(FundTallyVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ�ɶ��˺�
	 * 
	 * @return �ɶ��˺�
	 */
	public String getPk_partnaccount() {
		return (String) this.getAttributeValue(FundTallyVO.PK_PARTNACCOUNT);
	}

	/**
	 * ���ùɶ��˺�
	 * 
	 * @param pk_partnaccount
	 *            �ɶ��˺�
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(FundTallyVO.PK_PARTNACCOUNT, pk_partnaccount);
	}

	/**
	 * ��ȡҵ��С��
	 * 
	 * @return ҵ��С��
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(FundTallyVO.PK_SELFSGROUP);
	}

	/**
	 * ����ҵ��С��
	 * 
	 * @param pk_selfsgroup
	 *            ҵ��С��
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(FundTallyVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * ��ȡ��Ʒ����ַ�����
	 * 
	 * @return ��Ʒ����ַ�����
	 */
	public String getProductorcounterparty() {
		return (String) this
				.getAttributeValue(FundTallyVO.PRODUCTORCOUNTERPARTY);
	}

	/**
	 * ���ò�Ʒ����ַ�����
	 * 
	 * @param productorcounterparty
	 *            ��Ʒ����ַ�����
	 */
	public void setProductorcounterparty(String productorcounterparty) {
		this.setAttributeValue(FundTallyVO.PRODUCTORCOUNTERPARTY,
				productorcounterparty);
	}

	/**
	 * ��ȡʵ������
	 * 
	 * @return ʵ������
	 */
	public UFDouble getRealrate() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.REALRATE);
	}

	/**
	 * ����ʵ������
	 * 
	 * @param realrate
	 *            ʵ������
	 */
	public void setRealrate(UFDouble realrate) {
		this.setAttributeValue(FundTallyVO.REALRATE, realrate);
	}

	/**
	 * ��ȡ���μ�����Ϣ
	 * 
	 * @return ���μ�����Ϣ
	 */
	public UFDouble getThis_interest() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.THIS_INTEREST);
	}

	/**
	 * ���ñ��μ�����Ϣ
	 * 
	 * @param this_interest
	 *            ���μ�����Ϣ
	 */
	public void setThis_interest(UFDouble this_interest) {
		this.setAttributeValue(FundTallyVO.THIS_INTEREST, this_interest);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public Integer getTimelimit() {
		return (Integer) this.getAttributeValue(FundTallyVO.TIMELIMIT);
	}

	/**
	 * ��������
	 * 
	 * @param timelimit
	 *            ����
	 */
	public void setTimelimit(Integer timelimit) {
		this.setAttributeValue(FundTallyVO.TIMELIMIT, timelimit);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(FundTallyVO.TRADE_DATE);
	}

	/**
	 * ���ý�������
	 * 
	 * @param trade_date
	 *            ��������
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(FundTallyVO.TRADE_DATE, trade_date);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FundTallyVO.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(FundTallyVO.TS, ts);
	}

	/**
	 * ��ȡ�Զ�����1
	 * 
	 * @return �Զ�����1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF1);
	}

	/**
	 * �����Զ�����1
	 * 
	 * @param vdef1
	 *            �Զ�����1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(FundTallyVO.VDEF1, vdef1);
	}

	/**
	 * ��ȡ�Զ�����2
	 * 
	 * @return �Զ�����2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF2);
	}

	/**
	 * �����Զ�����2
	 * 
	 * @param vdef2
	 *            �Զ�����2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(FundTallyVO.VDEF2, vdef2);
	}

	/**
	 * ��ȡ�Զ�����3
	 * 
	 * @return �Զ�����3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF3);
	}

	/**
	 * �����Զ�����3
	 * 
	 * @param vdef3
	 *            �Զ�����3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(FundTallyVO.VDEF3, vdef3);
	}

	/**
	 * ��ȡ�Զ�����4
	 * 
	 * @return �Զ�����4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF4);
	}

	/**
	 * �����Զ�����4
	 * 
	 * @param vdef4
	 *            �Զ�����4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(FundTallyVO.VDEF4, vdef4);
	}

	/**
	 * ��ȡ�Զ�����5
	 * 
	 * @return �Զ�����5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF5);
	}

	/**
	 * �����Զ�����5
	 * 
	 * @param vdef5
	 *            �Զ�����5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(FundTallyVO.VDEF5, vdef5);
	}

	/**
	 * ��ȡ���Ϣ����
	 * 
	 * @return ���Ϣ����
	 */
	public Integer getYearcaldays() {
		return (Integer) this.getAttributeValue(FundTallyVO.YEARCALDAYS);
	}

	/**
	 * �������Ϣ����
	 * 
	 * @param yearcaldays
	 *            ���Ϣ����
	 */
	public void setYearcaldays(Integer yearcaldays) {
		this.setAttributeValue(FundTallyVO.YEARCALDAYS, yearcaldays);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_fund.FundTallyVO");
	}
}