package nc.vo.fba_fund.gofinancing;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class GoFinancingVO extends SuperVO {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	/**
	 * ����ʱ��
	 */
	public static final String APPROVEDATE = "approvedate";
	/**
	 * ������
	 */
	public static final String APPROVER = "approver";
	/**
	 * �ɽ�����
	 */
	public static final String BARGIN_NUM = "bargin_num";
	/**
	 * �ɽ����
	 */
	public static final String BARGIN_SUM = "bargin_sum";
	/**
	 * �Ƶ���
	 */
	public static final String BILLMAKER = "billmaker";
	/**
	 * ���ݺ�
	 */
	public static final String BILLNO = "billno";
	/**
	 * ��������
	 */
	public static final String BILLTYPECODE = "billtypecode";
	/**
	 * ��ͬ��
	 */
	public static final String CONTRACTNO = "contractno";
	/**
	 * ��Լ����
	 */
	public static final String CONTRACTRATE = "contractrate";
	/**
	 * ����ʱ��
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * ������
	 */
	public static final String CREATOR = "creator";
	/**
	 * ������
	 */
	public static final String ENDDATE = "enddate";
	/**
	 * ί�к�ͬ
	 */
	public static final String ENTRUSTCONTRACT = "entrustcontract";
	/**
	 * ʵ���ո�
	 */
	public static final String FACT_SUM = "fact_sum";
	/**
	 * ��������
	 */
	public static final String FUNDTYPE = "fundtype";
	/**
	 * �Ƶ�ʱ��
	 */
	public static final String MAKEDATE = "makedate";
	/**
	 * ������
	 */
	public static final String MANAGEDEPT = "managedept";
	/**
	 * �г�
	 */
	public static final String MARKET = "market";
	/**
	 * �޸�ʱ��
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * �޸���
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * ��ֵ�Զ�����1
	 */
	public static final String NDEF1 = "ndef1";
	/**
	 * ��ֵ�Զ�����10
	 */
	public static final String NDEF10 = "ndef10";
	/**
	 * ��ֵ�Զ�����2
	 */
	public static final String NDEF2 = "ndef2";
	/**
	 * ��ֵ�Զ�����3
	 */
	public static final String NDEF3 = "ndef3";
	/**
	 * ��ֵ�Զ�����4
	 */
	public static final String NDEF4 = "ndef4";
	/**
	 * ��ֵ�Զ�����5
	 */
	public static final String NDEF5 = "ndef5";
	/**
	 * ��ֵ�Զ�����6
	 */
	public static final String NDEF6 = "ndef6";
	/**
	 * ��ֵ�Զ�����7
	 */
	public static final String NDEF7 = "ndef7";
	/**
	 * ��ֵ�Զ�����8
	 */
	public static final String NDEF8 = "ndef8";
	/**
	 * ��ֵ�Զ�����9
	 */
	public static final String NDEF9 = "ndef9";
	/**
	 * ԭί�к�ͬ
	 */
	public static final String ORIGINAL_ENTRUSTCONTRACT = "original_entrustcontract";
	/**
	 * ��������
	 */
	public static final String OTHER_FEE = "other_fee";
	/**
	 * ��Ϣ��ʽ
	 */
	public static final String PAYTYPE = "paytype";
	/**
	 * �ʲ�����
	 */
	public static final String PK_ASSETSPROP = "pk_assetsprop";
	/**
	 * ��������pk
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
	 * ��������˲�
	 */
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	/**
	 * ����
	 */
	public static final String PK_GOFINANCING = "pk_gofinancing";
	/**
	 * ����
	 */
	public static final String PK_GROUP = "pk_group";
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
	 * ����֤ȯ
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
	 * ��Ʒ����ַ�����
	 */
	public static final String PRODUCTORCOUNTERPARTY = "productorcounterparty";
	/**
	 * ʵ������
	 */
	public static final String REALRATE = "realrate";
	/**
	 * ��ע
	 */
	public static final String REMARK = "remark";
	/**
	 * ���շ�
	 */
	public static final String RISK_FEE = "risk_fee";
	/**
	 * ��ˮ��
	 */
	public static final String SERIALNUMBER = "serialnumber";
	/**
	 * ������Դ
	 */
	public static final String SOURCE = "source";
	/**
	 * ��Ϣ��
	 */
	public static final String STARTDATE = "startdate";
	/**
	 * ״̬
	 */
	public static final String STATE = "state";
	/**
	 * ֤ȯ����
	 */
	public static final String STOCKCODE = "stockcode";
	/**
	 * ˰��
	 */
	public static final String TAXEXPENSE = "taxexpense";
	/**
	 * ��������
	 */
	public static final String TRADE_DATE = "trade_date";
	/**
	 * ��������
	 */
	public static final String TRANSTYPECODE = "transtypecode";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";
	/**
	 * ��ͷ�Զ�����1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * ��ͷ�Զ�����10
	 */
	public static final String VDEF10 = "vdef10";
	/**
	 * ��ͷ�Զ�����2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * ��ͷ�Զ�����3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * ��ͷ�Զ�����4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * ��ͷ�Զ�����5
	 */
	public static final String VDEF5 = "vdef5";
	/**
	 * ��ͷ�Զ�����6
	 */
	public static final String VDEF6 = "vdef6";
	/**
	 * ��ͷ�Զ�����7
	 */
	public static final String VDEF7 = "vdef7";
	/**
	 * ��ͷ�Զ�����8
	 */
	public static final String VDEF8 = "vdef8";
	/**
	 * ��ͷ�Զ�����9
	 */
	public static final String VDEF9 = "vdef9";
	/**
	 * ���Ϣ����
	 */
	public static final String YEARCALDAYS = "yearcaldays";

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getApprovedate() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.APPROVEDATE);
	}

	/**
	 * ��������ʱ��
	 * 
	 * @param approvedate
	 *            ����ʱ��
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.setAttributeValue(GoFinancingVO.APPROVEDATE, approvedate);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getApprover() {
		return (String) this.getAttributeValue(GoFinancingVO.APPROVER);
	}

	/**
	 * ����������
	 * 
	 * @param approver
	 *            ������
	 */
	public void setApprover(String approver) {
		this.setAttributeValue(GoFinancingVO.APPROVER, approver);
	}

	/**
	 * ��ȡ�ɽ�����
	 * 
	 * @return �ɽ�����
	 */
	public UFDouble getBargin_num() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.BARGIN_NUM);
	}

	/**
	 * ���óɽ�����
	 * 
	 * @param bargin_num
	 *            �ɽ�����
	 */
	public void setBargin_num(UFDouble bargin_num) {
		this.setAttributeValue(GoFinancingVO.BARGIN_NUM, bargin_num);
	}

	/**
	 * ��ȡ�ɽ����
	 * 
	 * @return �ɽ����
	 */
	public UFDouble getBargin_sum() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.BARGIN_SUM);
	}

	/**
	 * ���óɽ����
	 * 
	 * @param bargin_sum
	 *            �ɽ����
	 */
	public void setBargin_sum(UFDouble bargin_sum) {
		this.setAttributeValue(GoFinancingVO.BARGIN_SUM, bargin_sum);
	}

	/**
	 * ��ȡ�Ƶ���
	 * 
	 * @return �Ƶ���
	 */
	public String getBillmaker() {
		return (String) this.getAttributeValue(GoFinancingVO.BILLMAKER);
	}

	/**
	 * �����Ƶ���
	 * 
	 * @param billmaker
	 *            �Ƶ���
	 */
	public void setBillmaker(String billmaker) {
		this.setAttributeValue(GoFinancingVO.BILLMAKER, billmaker);
	}

	/**
	 * ��ȡ���ݺ�
	 * 
	 * @return ���ݺ�
	 */
	public String getBillno() {
		return (String) this.getAttributeValue(GoFinancingVO.BILLNO);
	}

	/**
	 * ���õ��ݺ�
	 * 
	 * @param billno
	 *            ���ݺ�
	 */
	public void setBillno(String billno) {
		this.setAttributeValue(GoFinancingVO.BILLNO, billno);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getBilltypecode() {
		return (String) this.getAttributeValue(GoFinancingVO.BILLTYPECODE);
	}

	/**
	 * ���õ�������
	 * 
	 * @param billtypecode
	 *            ��������
	 */
	public void setBilltypecode(String billtypecode) {
		this.setAttributeValue(GoFinancingVO.BILLTYPECODE, billtypecode);
	}

	/**
	 * ��ȡ��ͬ��
	 * 
	 * @return ��ͬ��
	 */
	public String getContractno() {
		return (String) this.getAttributeValue(GoFinancingVO.CONTRACTNO);
	}

	/**
	 * ���ú�ͬ��
	 * 
	 * @param contractno
	 *            ��ͬ��
	 */
	public void setContractno(String contractno) {
		this.setAttributeValue(GoFinancingVO.CONTRACTNO, contractno);
	}

	/**
	 * ��ȡ��Լ����
	 * 
	 * @return ��Լ����
	 */
	public UFDouble getContractrate() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.CONTRACTRATE);
	}

	/**
	 * ���ú�Լ����
	 * 
	 * @param contractrate
	 *            ��Լ����
	 */
	public void setContractrate(UFDouble contractrate) {
		this.setAttributeValue(GoFinancingVO.CONTRACTRATE, contractrate);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.CREATIONTIME);
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param creationtime
	 *            ����ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(GoFinancingVO.CREATIONTIME, creationtime);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(GoFinancingVO.CREATOR);
	}

	/**
	 * ���ô�����
	 * 
	 * @param creator
	 *            ������
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(GoFinancingVO.CREATOR, creator);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public UFDate getEnddate() {
		return (UFDate) this.getAttributeValue(GoFinancingVO.ENDDATE);
	}

	/**
	 * ���õ�����
	 * 
	 * @param enddate
	 *            ������
	 */
	public void setEnddate(UFDate enddate) {
		this.setAttributeValue(GoFinancingVO.ENDDATE, enddate);
	}

	/**
	 * ��ȡί�к�ͬ
	 * 
	 * @return ί�к�ͬ
	 */
	public String getEntrustcontract() {
		return (String) this.getAttributeValue(GoFinancingVO.ENTRUSTCONTRACT);
	}

	/**
	 * ����ί�к�ͬ
	 * 
	 * @param entrustcontract
	 *            ί�к�ͬ
	 */
	public void setEntrustcontract(String entrustcontract) {
		this.setAttributeValue(GoFinancingVO.ENTRUSTCONTRACT, entrustcontract);
	}

	/**
	 * ��ȡʵ���ո�
	 * 
	 * @return ʵ���ո�
	 */
	public UFDouble getFact_sum() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.FACT_SUM);
	}

	/**
	 * ����ʵ���ո�
	 * 
	 * @param fact_sum
	 *            ʵ���ո�
	 */
	public void setFact_sum(UFDouble fact_sum) {
		this.setAttributeValue(GoFinancingVO.FACT_SUM, fact_sum);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 * @see String
	 */
	public Integer getFundtype() {
		return (Integer) this.getAttributeValue(GoFinancingVO.FUNDTYPE);
	}

	/**
	 * ������������
	 * 
	 * @param fundtype
	 *            ��������
	 * @see String
	 */
	public void setFundtype(Integer fundtype) {
		this.setAttributeValue(GoFinancingVO.FUNDTYPE, fundtype);
	}

	/**
	 * ��ȡ�Ƶ�ʱ��
	 * 
	 * @return �Ƶ�ʱ��
	 */
	public UFDateTime getMakedate() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.MAKEDATE);
	}

	/**
	 * �����Ƶ�ʱ��
	 * 
	 * @param makedate
	 *            �Ƶ�ʱ��
	 */
	public void setMakedate(UFDateTime makedate) {
		this.setAttributeValue(GoFinancingVO.MAKEDATE, makedate);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getManagedept() {
		return (String) this.getAttributeValue(GoFinancingVO.MANAGEDEPT);
	}

	/**
	 * ���ù�����
	 * 
	 * @param managedept
	 *            ������
	 */
	public void setManagedept(String managedept) {
		this.setAttributeValue(GoFinancingVO.MANAGEDEPT, managedept);
	}

	/**
	 * ��ȡ�г�
	 * 
	 * @return �г�
	 * @see String
	 */
	public Integer getMarket() {
		return (Integer) this.getAttributeValue(GoFinancingVO.MARKET);
	}

	/**
	 * �����г�
	 * 
	 * @param market
	 *            �г�
	 * @see String
	 */
	public void setMarket(Integer market) {
		this.setAttributeValue(GoFinancingVO.MARKET, market);
	}

	/**
	 * ��ȡ�޸�ʱ��
	 * 
	 * @return �޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.MODIFIEDTIME);
	}

	/**
	 * �����޸�ʱ��
	 * 
	 * @param modifiedtime
	 *            �޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(GoFinancingVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * ��ȡ�޸���
	 * 
	 * @return �޸���
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(GoFinancingVO.MODIFIER);
	}

	/**
	 * �����޸���
	 * 
	 * @param modifier
	 *            �޸���
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(GoFinancingVO.MODIFIER, modifier);
	}

	/**
	 * ��ȡ��ֵ�Զ�����1
	 * 
	 * @return ��ֵ�Զ�����1
	 */
	public UFDouble getNdef1() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF1);
	}

	/**
	 * ������ֵ�Զ�����1
	 * 
	 * @param ndef1
	 *            ��ֵ�Զ�����1
	 */
	public void setNdef1(UFDouble ndef1) {
		this.setAttributeValue(GoFinancingVO.NDEF1, ndef1);
	}

	/**
	 * ��ȡ��ֵ�Զ�����10
	 * 
	 * @return ��ֵ�Զ�����10
	 */
	public UFDouble getNdef10() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF10);
	}

	/**
	 * ������ֵ�Զ�����10
	 * 
	 * @param ndef10
	 *            ��ֵ�Զ�����10
	 */
	public void setNdef10(UFDouble ndef10) {
		this.setAttributeValue(GoFinancingVO.NDEF10, ndef10);
	}

	/**
	 * ��ȡ��ֵ�Զ�����2
	 * 
	 * @return ��ֵ�Զ�����2
	 */
	public UFDouble getNdef2() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF2);
	}

	/**
	 * ������ֵ�Զ�����2
	 * 
	 * @param ndef2
	 *            ��ֵ�Զ�����2
	 */
	public void setNdef2(UFDouble ndef2) {
		this.setAttributeValue(GoFinancingVO.NDEF2, ndef2);
	}

	/**
	 * ��ȡ��ֵ�Զ�����3
	 * 
	 * @return ��ֵ�Զ�����3
	 */
	public UFDouble getNdef3() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF3);
	}

	/**
	 * ������ֵ�Զ�����3
	 * 
	 * @param ndef3
	 *            ��ֵ�Զ�����3
	 */
	public void setNdef3(UFDouble ndef3) {
		this.setAttributeValue(GoFinancingVO.NDEF3, ndef3);
	}

	/**
	 * ��ȡ��ֵ�Զ�����4
	 * 
	 * @return ��ֵ�Զ�����4
	 */
	public UFDouble getNdef4() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF4);
	}

	/**
	 * ������ֵ�Զ�����4
	 * 
	 * @param ndef4
	 *            ��ֵ�Զ�����4
	 */
	public void setNdef4(UFDouble ndef4) {
		this.setAttributeValue(GoFinancingVO.NDEF4, ndef4);
	}

	/**
	 * ��ȡ��ֵ�Զ�����5
	 * 
	 * @return ��ֵ�Զ�����5
	 */
	public UFDouble getNdef5() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF5);
	}

	/**
	 * ������ֵ�Զ�����5
	 * 
	 * @param ndef5
	 *            ��ֵ�Զ�����5
	 */
	public void setNdef5(UFDouble ndef5) {
		this.setAttributeValue(GoFinancingVO.NDEF5, ndef5);
	}

	/**
	 * ��ȡ��ֵ�Զ�����6
	 * 
	 * @return ��ֵ�Զ�����6
	 */
	public UFDouble getNdef6() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF6);
	}

	/**
	 * ������ֵ�Զ�����6
	 * 
	 * @param ndef6
	 *            ��ֵ�Զ�����6
	 */
	public void setNdef6(UFDouble ndef6) {
		this.setAttributeValue(GoFinancingVO.NDEF6, ndef6);
	}

	/**
	 * ��ȡ��ֵ�Զ�����7
	 * 
	 * @return ��ֵ�Զ�����7
	 */
	public UFDouble getNdef7() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF7);
	}

	/**
	 * ������ֵ�Զ�����7
	 * 
	 * @param ndef7
	 *            ��ֵ�Զ�����7
	 */
	public void setNdef7(UFDouble ndef7) {
		this.setAttributeValue(GoFinancingVO.NDEF7, ndef7);
	}

	/**
	 * ��ȡ��ֵ�Զ�����8
	 * 
	 * @return ��ֵ�Զ�����8
	 */
	public UFDouble getNdef8() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF8);
	}

	/**
	 * ������ֵ�Զ�����8
	 * 
	 * @param ndef8
	 *            ��ֵ�Զ�����8
	 */
	public void setNdef8(UFDouble ndef8) {
		this.setAttributeValue(GoFinancingVO.NDEF8, ndef8);
	}

	/**
	 * ��ȡ��ֵ�Զ�����9
	 * 
	 * @return ��ֵ�Զ�����9
	 */
	public UFDouble getNdef9() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF9);
	}

	/**
	 * ������ֵ�Զ�����9
	 * 
	 * @param ndef9
	 *            ��ֵ�Զ�����9
	 */
	public void setNdef9(UFDouble ndef9) {
		this.setAttributeValue(GoFinancingVO.NDEF9, ndef9);
	}

	/**
	 * ��ȡԭί�к�ͬ
	 * 
	 * @return ԭί�к�ͬ
	 */
	public String getOriginal_entrustcontract() {
		return (String) this
				.getAttributeValue(GoFinancingVO.ORIGINAL_ENTRUSTCONTRACT);
	}

	/**
	 * ����ԭί�к�ͬ
	 * 
	 * @param original_entrustcontract
	 *            ԭί�к�ͬ
	 */
	public void setOriginal_entrustcontract(String original_entrustcontract) {
		this.setAttributeValue(GoFinancingVO.ORIGINAL_ENTRUSTCONTRACT,
				original_entrustcontract);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDouble getOther_fee() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.OTHER_FEE);
	}

	/**
	 * ������������
	 * 
	 * @param other_fee
	 *            ��������
	 */
	public void setOther_fee(UFDouble other_fee) {
		this.setAttributeValue(GoFinancingVO.OTHER_FEE, other_fee);
	}

	/**
	 * ��ȡ��Ϣ��ʽ
	 * 
	 * @return ��Ϣ��ʽ
	 * @see String
	 */
	public Integer getPaytype() {
		return (Integer) this.getAttributeValue(GoFinancingVO.PAYTYPE);
	}

	/**
	 * ���ø�Ϣ��ʽ
	 * 
	 * @param paytype
	 *            ��Ϣ��ʽ
	 * @see String
	 */
	public void setPaytype(Integer paytype) {
		this.setAttributeValue(GoFinancingVO.PAYTYPE, paytype);
	}

	/**
	 * ��ȡ�ʲ�����
	 * 
	 * @return �ʲ�����
	 */
	public String getPk_assetsprop() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_ASSETSPROP);
	}

	/**
	 * �����ʲ�����
	 * 
	 * @param pk_assetsprop
	 *            �ʲ�����
	 */
	public void setPk_assetsprop(String pk_assetsprop) {
		this.setAttributeValue(GoFinancingVO.PK_ASSETSPROP, pk_assetsprop);
	}

	/**
	 * ��ȡ��������pk
	 * 
	 * @return ��������pk
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_BILLTYPE);
	}

	/**
	 * ���õ�������pk
	 * 
	 * @param pk_billtype
	 *            ��������pk
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(GoFinancingVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getPk_bourse() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_BOURSE);
	}

	/**
	 * ���ý�����
	 * 
	 * @param pk_bourse
	 *            ������
	 */
	public void setPk_bourse(String pk_bourse) {
		this.setAttributeValue(GoFinancingVO.PK_BOURSE, pk_bourse);
	}

	/**
	 * ��ȡҵ������
	 * 
	 * @return ҵ������
	 */
	public String getPk_busitype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_BUSITYPE);
	}

	/**
	 * ����ҵ������
	 * 
	 * @param pk_busitype
	 *            ҵ������
	 */
	public void setPk_busitype(String pk_busitype) {
		this.setAttributeValue(GoFinancingVO.PK_BUSITYPE, pk_busitype);
	}

	/**
	 * ��ȡ�ʽ��˺�
	 * 
	 * @return �ʽ��˺�
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_CAPACCOUNT);
	}

	/**
	 * �����ʽ��˺�
	 * 
	 * @param pk_capaccount
	 *            �ʽ��˺�
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(GoFinancingVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * ��ȡ������λ
	 * 
	 * @return ������λ
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_CLIENT);
	}

	/**
	 * ����������λ
	 * 
	 * @param pk_client
	 *            ������λ
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(GoFinancingVO.PK_CLIENT, pk_client);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_currtype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_CURRTYPE);
	}

	/**
	 * ���ñ���
	 * 
	 * @param pk_currtype
	 *            ����
	 */
	public void setPk_currtype(String pk_currtype) {
		this.setAttributeValue(GoFinancingVO.PK_CURRTYPE, pk_currtype);
	}

	/**
	 * ��ȡ��������˲�
	 * 
	 * @return ��������˲�
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_GLORGBOOK);
	}

	/**
	 * ���ò�������˲�
	 * 
	 * @param pk_glorgbook
	 *            ��������˲�
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(GoFinancingVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_gofinancing() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_GOFINANCING);
	}

	/**
	 * ��������
	 * 
	 * @param pk_gofinancing
	 *            ����
	 */
	public void setPk_gofinancing(String pk_gofinancing) {
		this.setAttributeValue(GoFinancingVO.PK_GOFINANCING, pk_gofinancing);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(GoFinancingVO.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ�ֻ��ص�
	 * 
	 * @return �ֻ��ص�
	 */
	public String getPk_operatesite() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_OPERATESITE);
	}

	/**
	 * ���÷ֻ��ص�
	 * 
	 * @param pk_operatesite
	 *            �ֻ��ص�
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(GoFinancingVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(GoFinancingVO.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(GoFinancingVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ�ɶ��˺�
	 * 
	 * @return �ɶ��˺�
	 */
	public String getPk_partnaccount() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_PARTNACCOUNT);
	}

	/**
	 * ���ùɶ��˺�
	 * 
	 * @param pk_partnaccount
	 *            �ɶ��˺�
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(GoFinancingVO.PK_PARTNACCOUNT, pk_partnaccount);
	}

	/**
	 * ��ȡ����֤ȯ
	 * 
	 * @return ����֤ȯ
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_SECURITIES);
	}

	/**
	 * ���ý���֤ȯ
	 * 
	 * @param pk_securities
	 *            ����֤ȯ
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(GoFinancingVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * ��ȡҵ��С��
	 * 
	 * @return ҵ��С��
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_SELFSGROUP);
	}

	/**
	 * ����ҵ��С��
	 * 
	 * @param pk_selfsgroup
	 *            ҵ��С��
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(GoFinancingVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * ��ȡ�����֯
	 * 
	 * @return �����֯
	 */
	public String getPk_stocksort() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_STOCKSORT);
	}

	/**
	 * ���ÿ����֯
	 * 
	 * @param pk_stocksort
	 *            �����֯
	 */
	public void setPk_stocksort(String pk_stocksort) {
		this.setAttributeValue(GoFinancingVO.PK_STOCKSORT, pk_stocksort);
	}

	/**
	 * ��ȡ��������pk
	 * 
	 * @return ��������pk
	 */
	public String getPk_transtype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_TRANSTYPE);
	}

	/**
	 * ���ý�������pk
	 * 
	 * @param pk_transtype
	 *            ��������pk
	 */
	public void setPk_transtype(String pk_transtype) {
		this.setAttributeValue(GoFinancingVO.PK_TRANSTYPE, pk_transtype);
	}

	/**
	 * ��ȡƾ֤����
	 * 
	 * @return ƾ֤����
	 */
	public String getPk_voucher() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_VOUCHER);
	}

	/**
	 * ����ƾ֤����
	 * 
	 * @param pk_voucher
	 *            ƾ֤����
	 */
	public void setPk_voucher(String pk_voucher) {
		this.setAttributeValue(GoFinancingVO.PK_VOUCHER, pk_voucher);
	}

	/**
	 * ��ȡ��Ʒ����ַ�����
	 * 
	 * @return ��Ʒ����ַ�����
	 */
	public String getProductorcounterparty() {
		return (String) this
				.getAttributeValue(GoFinancingVO.PRODUCTORCOUNTERPARTY);
	}

	/**
	 * ���ò�Ʒ����ַ�����
	 * 
	 * @param productorcounterparty
	 *            ��Ʒ����ַ�����
	 */
	public void setProductorcounterparty(String productorcounterparty) {
		this.setAttributeValue(GoFinancingVO.PRODUCTORCOUNTERPARTY,
				productorcounterparty);
	}

	/**
	 * ��ȡʵ������
	 * 
	 * @return ʵ������
	 */
	public UFDouble getRealrate() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.REALRATE);
	}

	/**
	 * ����ʵ������
	 * 
	 * @param realrate
	 *            ʵ������
	 */
	public void setRealrate(UFDouble realrate) {
		this.setAttributeValue(GoFinancingVO.REALRATE, realrate);
	}

	/**
	 * ��ȡ��ע
	 * 
	 * @return ��ע
	 */
	public String getRemark() {
		return (String) this.getAttributeValue(GoFinancingVO.REMARK);
	}

	/**
	 * ���ñ�ע
	 * 
	 * @param remark
	 *            ��ע
	 */
	public void setRemark(String remark) {
		this.setAttributeValue(GoFinancingVO.REMARK, remark);
	}

	/**
	 * ��ȡ���շ�
	 * 
	 * @return ���շ�
	 */
	public UFDouble getRisk_fee() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.RISK_FEE);
	}

	/**
	 * ���÷��շ�
	 * 
	 * @param risk_fee
	 *            ���շ�
	 */
	public void setRisk_fee(UFDouble risk_fee) {
		this.setAttributeValue(GoFinancingVO.RISK_FEE, risk_fee);
	}

	/**
	 * ��ȡ��ˮ��
	 * 
	 * @return ��ˮ��
	 */
	public String getSerialnumber() {
		return (String) this.getAttributeValue(GoFinancingVO.SERIALNUMBER);
	}

	/**
	 * ������ˮ��
	 * 
	 * @param serialnumber
	 *            ��ˮ��
	 */
	public void setSerialnumber(String serialnumber) {
		this.setAttributeValue(GoFinancingVO.SERIALNUMBER, serialnumber);
	}

	/**
	 * ��ȡ������Դ
	 * 
	 * @return ������Դ
	 */
	public String getSource() {
		return (String) this.getAttributeValue(GoFinancingVO.SOURCE);
	}

	/**
	 * ����������Դ
	 * 
	 * @param source
	 *            ������Դ
	 */
	public void setSource(String source) {
		this.setAttributeValue(GoFinancingVO.SOURCE, source);
	}

	/**
	 * ��ȡ��Ϣ��
	 * 
	 * @return ��Ϣ��
	 */
	public UFDate getStartdate() {
		return (UFDate) this.getAttributeValue(GoFinancingVO.STARTDATE);
	}

	/**
	 * ������Ϣ��
	 * 
	 * @param startdate
	 *            ��Ϣ��
	 */
	public void setStartdate(UFDate startdate) {
		this.setAttributeValue(GoFinancingVO.STARTDATE, startdate);
	}

	/**
	 * ��ȡ״̬
	 * 
	 * @return ״̬
	 * @see String
	 */
	public Integer getState() {
		return (Integer) this.getAttributeValue(GoFinancingVO.STATE);
	}

	/**
	 * ����״̬
	 * 
	 * @param state
	 *            ״̬
	 * @see String
	 */
	public void setState(Integer state) {
		this.setAttributeValue(GoFinancingVO.STATE, state);
	}

	/**
	 * ��ȡ֤ȯ����
	 * 
	 * @return ֤ȯ����
	 */
	public String getStockcode() {
		return (String) this.getAttributeValue(GoFinancingVO.STOCKCODE);
	}

	/**
	 * ����֤ȯ����
	 * 
	 * @param stockcode
	 *            ֤ȯ����
	 */
	public void setStockcode(String stockcode) {
		this.setAttributeValue(GoFinancingVO.STOCKCODE, stockcode);
	}

	/**
	 * ��ȡ˰��
	 * 
	 * @return ˰��
	 */
	public UFDouble getTaxexpense() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.TAXEXPENSE);
	}

	/**
	 * ����˰��
	 * 
	 * @param taxexpense
	 *            ˰��
	 */
	public void setTaxexpense(UFDouble taxexpense) {
		this.setAttributeValue(GoFinancingVO.TAXEXPENSE, taxexpense);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(GoFinancingVO.TRADE_DATE);
	}

	/**
	 * ���ý�������
	 * 
	 * @param trade_date
	 *            ��������
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(GoFinancingVO.TRADE_DATE, trade_date);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getTranstypecode() {
		return (String) this.getAttributeValue(GoFinancingVO.TRANSTYPECODE);
	}

	/**
	 * ���ý�������
	 * 
	 * @param transtypecode
	 *            ��������
	 */
	public void setTranstypecode(String transtypecode) {
		this.setAttributeValue(GoFinancingVO.TRANSTYPECODE, transtypecode);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(GoFinancingVO.TS, ts);
	}

	/**
	 * ��ȡ��ͷ�Զ�����1
	 * 
	 * @return ��ͷ�Զ�����1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF1);
	}

	/**
	 * ���ñ�ͷ�Զ�����1
	 * 
	 * @param vdef1
	 *            ��ͷ�Զ�����1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(GoFinancingVO.VDEF1, vdef1);
	}

	/**
	 * ��ȡ��ͷ�Զ�����10
	 * 
	 * @return ��ͷ�Զ�����10
	 */
	public String getVdef10() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF10);
	}

	/**
	 * ���ñ�ͷ�Զ�����10
	 * 
	 * @param vdef10
	 *            ��ͷ�Զ�����10
	 */
	public void setVdef10(String vdef10) {
		this.setAttributeValue(GoFinancingVO.VDEF10, vdef10);
	}

	/**
	 * ��ȡ��ͷ�Զ�����2
	 * 
	 * @return ��ͷ�Զ�����2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF2);
	}

	/**
	 * ���ñ�ͷ�Զ�����2
	 * 
	 * @param vdef2
	 *            ��ͷ�Զ�����2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(GoFinancingVO.VDEF2, vdef2);
	}

	/**
	 * ��ȡ��ͷ�Զ�����3
	 * 
	 * @return ��ͷ�Զ�����3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF3);
	}

	/**
	 * ���ñ�ͷ�Զ�����3
	 * 
	 * @param vdef3
	 *            ��ͷ�Զ�����3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(GoFinancingVO.VDEF3, vdef3);
	}

	/**
	 * ��ȡ��ͷ�Զ�����4
	 * 
	 * @return ��ͷ�Զ�����4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF4);
	}

	/**
	 * ���ñ�ͷ�Զ�����4
	 * 
	 * @param vdef4
	 *            ��ͷ�Զ�����4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(GoFinancingVO.VDEF4, vdef4);
	}

	/**
	 * ��ȡ��ͷ�Զ�����5
	 * 
	 * @return ��ͷ�Զ�����5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF5);
	}

	/**
	 * ���ñ�ͷ�Զ�����5
	 * 
	 * @param vdef5
	 *            ��ͷ�Զ�����5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(GoFinancingVO.VDEF5, vdef5);
	}

	/**
	 * ��ȡ��ͷ�Զ�����6
	 * 
	 * @return ��ͷ�Զ�����6
	 */
	public String getVdef6() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF6);
	}

	/**
	 * ���ñ�ͷ�Զ�����6
	 * 
	 * @param vdef6
	 *            ��ͷ�Զ�����6
	 */
	public void setVdef6(String vdef6) {
		this.setAttributeValue(GoFinancingVO.VDEF6, vdef6);
	}

	/**
	 * ��ȡ��ͷ�Զ�����7
	 * 
	 * @return ��ͷ�Զ�����7
	 */
	public String getVdef7() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF7);
	}

	/**
	 * ���ñ�ͷ�Զ�����7
	 * 
	 * @param vdef7
	 *            ��ͷ�Զ�����7
	 */
	public void setVdef7(String vdef7) {
		this.setAttributeValue(GoFinancingVO.VDEF7, vdef7);
	}

	/**
	 * ��ȡ��ͷ�Զ�����8
	 * 
	 * @return ��ͷ�Զ�����8
	 */
	public String getVdef8() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF8);
	}

	/**
	 * ���ñ�ͷ�Զ�����8
	 * 
	 * @param vdef8
	 *            ��ͷ�Զ�����8
	 */
	public void setVdef8(String vdef8) {
		this.setAttributeValue(GoFinancingVO.VDEF8, vdef8);
	}

	/**
	 * ��ȡ��ͷ�Զ�����9
	 * 
	 * @return ��ͷ�Զ�����9
	 */
	public String getVdef9() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF9);
	}

	/**
	 * ���ñ�ͷ�Զ�����9
	 * 
	 * @param vdef9
	 *            ��ͷ�Զ�����9
	 */
	public void setVdef9(String vdef9) {
		this.setAttributeValue(GoFinancingVO.VDEF9, vdef9);
	}

	/**
	 * ��ȡ���Ϣ����
	 * 
	 * @return ���Ϣ����
	 */
	public Integer getYearcaldays() {
		return (Integer) this.getAttributeValue(GoFinancingVO.YEARCALDAYS);
	}

	/**
	 * �������Ϣ����
	 * 
	 * @param yearcaldays
	 *            ���Ϣ����
	 */
	public void setYearcaldays(Integer yearcaldays) {
		this.setAttributeValue(GoFinancingVO.YEARCALDAYS, yearcaldays);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_fund.GoFinancingVO");
	}
}