package nc.vo.fba_scost.cost.stockbalance;

import java.util.HashMap;
import java.util.Map;

import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

@SuppressWarnings("serial")
public class StockBalanceVO extends SuperVO implements ITrade_Data{
	
	//ʵ������ʹ�ã�����
	private Map<String,UFDouble> stock_map = new HashMap<String,UFDouble>();

	/**
	 * Ӧ����Ϣ
	 */
	public static final String ACCRUAL_SUM = "accrual_sum";
	/**
	 * ��ֵ��ֵ
	 */
	public static final String ASSESS_ADD = "assess_add";
	/**
	 * ���ۿ�ʼ����
	 */
	public static final String BEGIN_DATE = "begin_date";
	/**
	 * ����ʱ��
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * ������
	 */
	public static final String CREATOR = "creator";
	/**
	 * ���۽�������
	 */
	public static final String END_DATE = "end_date";
	/**
	 * ��������
	 */
	public static final String FROZEN_NUM = "frozen_num";
	/**
	 * ������
	 */
	public static final String FROZEN_SUM = "frozen_sum";
	/**
	 * ����˰��
	 */
	public static final String FROZEN_TAX = "frozen_tax";
	/**
	 * �޸�ʱ��
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * �޸���
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * �Զ����ֶ�10
	 */
	public static final String NDEF10 = "ndef10";
	/**
	 * �Զ����ֶ�11
	 */
	public static final String NDEF11 = "ndef11";
	/**
	 * �Զ����ֶ�12
	 */
	public static final String NDEF12 = "ndef12";
	/**
	 * �Զ����ֶ�13
	 */
	public static final String NDEF13 = "ndef13";
	/**
	 * �Զ����ֶ�14
	 */
	public static final String NDEF14 = "ndef14";
	/**
	 * �Զ����ֶ�6
	 */
	public static final String NDEF6 = "ndef6";
	/**
	 * �Զ����ֶ�7
	 */
	public static final String NDEF7 = "ndef7";
	/**
	 * �Զ����ֶ�8
	 */
	public static final String NDEF8 = "ndef8";
	/**
	 * �Զ����ֶ�9
	 */
	public static final String NDEF9 = "ndef9";
	/**
	 * �ʲ�����
	 */
	public static final String PK_ASSETSPROP = "pk_assetsprop";
	/**
	 * ��������
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * ���ҵ�����
	 */
	public static final String PK_BILLTYPEGROUP = "pk_billtypegroup";
	/**
	 * �ʽ��˺�
	 */
	public static final String PK_CAPACCOUNT = "pk_capaccount";
	/**
	 * ������λ
	 */
	public static final String PK_CLIENT = "pk_client";
	/**
	 * ���㷽��
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
	 * ����
	 */
	public static final String PK_STOCKBALANCE = "pk_stockbalance";
	/**
	 * �����֯
	 */
	public static final String PK_STOCKSORT = "pk_stocksort";
	/**
	 * ״̬
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
	 * ���˰��
	 */
	public static final String STOCKS_TAX = "stocks_tax";
	/**
	 * �ۼ�����
	 */
	public static final String TOTAL_WIN = "total_win";
	/**
	 * ��������
	 */
	public static final String TRADE_DATE = "trade_date";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";
	/**
	 * �Զ����ֶ�1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * �Զ����ֶ�2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * �Զ����ֶ�3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * �Զ����ֶ�4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * �Զ����ֶ�5
	 */
	public static final String VDEF5 = "vdef5";

	/**
	 * ��ȡӦ����Ϣ
	 * 
	 * @return Ӧ����Ϣ
	 */
	public UFDouble getAccrual_sum() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.ACCRUAL_SUM);
	}

	/**
	 * ����Ӧ����Ϣ
	 * 
	 * @param accrual_sum
	 *            Ӧ����Ϣ
	 */
	public void setAccrual_sum(UFDouble accrual_sum) {
		this.setAttributeValue(StockBalanceVO.ACCRUAL_SUM, accrual_sum);
	}

	/**
	 * ��ȡ��ֵ��ֵ
	 * 
	 * @return ��ֵ��ֵ
	 */
	public UFDouble getAssess_add() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.ASSESS_ADD);
	}

	/**
	 * ���ù�ֵ��ֵ
	 * 
	 * @param assess_add
	 *            ��ֵ��ֵ
	 */
	public void setAssess_add(UFDouble assess_add) {
		this.setAttributeValue(StockBalanceVO.ASSESS_ADD, assess_add);
	}

	/**
	 * ��ȡ���ۿ�ʼ����
	 * 
	 * @return ���ۿ�ʼ����
	 */
	public UFLiteralDate getBegin_date() {
		return (UFLiteralDate) this.getAttributeValue(StockBalanceVO.BEGIN_DATE);
	}

	/**
	 * �������ۿ�ʼ����
	 * 
	 * @param begin_date
	 *            ���ۿ�ʼ����
	 */
	public void setBegin_date(UFLiteralDate begin_date) {
		this.setAttributeValue(StockBalanceVO.BEGIN_DATE, begin_date);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(StockBalanceVO.CREATIONTIME);
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param creationtime
	 *            ����ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(StockBalanceVO.CREATIONTIME, creationtime);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(StockBalanceVO.CREATOR);
	}

	/**
	 * ���ô�����
	 * 
	 * @param creator
	 *            ������
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(StockBalanceVO.CREATOR, creator);
	}

	/**
	 * ��ȡ���۽�������
	 * 
	 * @return ���۽�������
	 */
	public UFLiteralDate getEnd_date() {
		return (UFLiteralDate) this.getAttributeValue(StockBalanceVO.END_DATE);
	}

	/**
	 * �������۽�������
	 * 
	 * @param end_date
	 *            ���۽�������
	 */
	public void setEnd_date(UFLiteralDate end_date) {
		this.setAttributeValue(StockBalanceVO.END_DATE, end_date);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDouble getFrozen_num() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.FROZEN_NUM);
	}

	/**
	 * ���ö�������
	 * 
	 * @param frozen_num
	 *            ��������
	 */
	public void setFrozen_num(UFDouble frozen_num) {
		this.setAttributeValue(StockBalanceVO.FROZEN_NUM, frozen_num);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public UFDouble getFrozen_sum() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.FROZEN_SUM);
	}

	/**
	 * ���ö�����
	 * 
	 * @param frozen_sum
	 *            ������
	 */
	public void setFrozen_sum(UFDouble frozen_sum) {
		this.setAttributeValue(StockBalanceVO.FROZEN_SUM, frozen_sum);
	}

	/**
	 * ��ȡ����˰��
	 * 
	 * @return ����˰��
	 */
	public UFDouble getFrozen_tax() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.FROZEN_TAX);
	}

	/**
	 * ���ö���˰��
	 * 
	 * @param frozen_tax
	 *            ����˰��
	 */
	public void setFrozen_tax(UFDouble frozen_tax) {
		this.setAttributeValue(StockBalanceVO.FROZEN_TAX, frozen_tax);
	}

	/**
	 * ��ȡ�޸�ʱ��
	 * 
	 * @return �޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(StockBalanceVO.MODIFIEDTIME);
	}

	/**
	 * �����޸�ʱ��
	 * 
	 * @param modifiedtime
	 *            �޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(StockBalanceVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * ��ȡ�޸���
	 * 
	 * @return �޸���
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(StockBalanceVO.MODIFIER);
	}

	/**
	 * �����޸���
	 * 
	 * @param modifier
	 *            �޸���
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(StockBalanceVO.MODIFIER, modifier);
	}

	/**
	 * ��ȡ�Զ����ֶ�10
	 * 
	 * @return �Զ����ֶ�10
	 */
	public UFDouble getNdef10() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF10);
	}

	/**
	 * �����Զ����ֶ�10
	 * 
	 * @param ndef10
	 *            �Զ����ֶ�10
	 */
	public void setNdef10(UFDouble ndef10) {
		this.setAttributeValue(StockBalanceVO.NDEF10, ndef10);
	}

	/**
	 * ��ȡ�Զ����ֶ�11
	 * 
	 * @return �Զ����ֶ�11
	 */
	public Integer getNdef11() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF11);
	}

	/**
	 * �����Զ����ֶ�11
	 * 
	 * @param ndef11
	 *            �Զ����ֶ�11
	 */
	public void setNdef11(Integer ndef11) {
		this.setAttributeValue(StockBalanceVO.NDEF11, ndef11);
	}

	/**
	 * ��ȡ�Զ����ֶ�12
	 * 
	 * @return �Զ����ֶ�12
	 */
	public Integer getNdef12() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF12);
	}

	/**
	 * �����Զ����ֶ�12
	 * 
	 * @param ndef12
	 *            �Զ����ֶ�12
	 */
	public void setNdef12(Integer ndef12) {
		this.setAttributeValue(StockBalanceVO.NDEF12, ndef12);
	}

	/**
	 * ��ȡ�Զ����ֶ�13
	 * 
	 * @return �Զ����ֶ�13
	 */
	public Integer getNdef13() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF13);
	}

	/**
	 * �����Զ����ֶ�13
	 * 
	 * @param ndef13
	 *            �Զ����ֶ�13
	 */
	public void setNdef13(Integer ndef13) {
		this.setAttributeValue(StockBalanceVO.NDEF13, ndef13);
	}

	/**
	 * ��ȡ�Զ����ֶ�14
	 * 
	 * @return �Զ����ֶ�14
	 */
	public Integer getNdef14() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF14);
	}

	/**
	 * �����Զ����ֶ�14
	 * 
	 * @param ndef14
	 *            �Զ����ֶ�14
	 */
	public void setNdef14(Integer ndef14) {
		this.setAttributeValue(StockBalanceVO.NDEF14, ndef14);
	}

	/**
	 * ��ȡ�Զ����ֶ�6
	 * 
	 * @return �Զ����ֶ�6
	 */
	public UFDouble getNdef6() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF6);
	}

	/**
	 * �����Զ����ֶ�6
	 * 
	 * @param ndef6
	 *            �Զ����ֶ�6
	 */
	public void setNdef6(UFDouble ndef6) {
		this.setAttributeValue(StockBalanceVO.NDEF6, ndef6);
	}

	/**
	 * ��ȡ�Զ����ֶ�7
	 * 
	 * @return �Զ����ֶ�7
	 */
	public UFDouble getNdef7() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF7);
	}

	/**
	 * �����Զ����ֶ�7
	 * 
	 * @param ndef7
	 *            �Զ����ֶ�7
	 */
	public void setNdef7(UFDouble ndef7) {
		this.setAttributeValue(StockBalanceVO.NDEF7, ndef7);
	}

	/**
	 * ��ȡ�Զ����ֶ�8
	 * 
	 * @return �Զ����ֶ�8
	 */
	public UFDouble getNdef8() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF8);
	}

	/**
	 * �����Զ����ֶ�8
	 * 
	 * @param ndef8
	 *            �Զ����ֶ�8
	 */
	public void setNdef8(UFDouble ndef8) {
		this.setAttributeValue(StockBalanceVO.NDEF8, ndef8);
	}

	/**
	 * ��ȡ�Զ����ֶ�9
	 * 
	 * @return �Զ����ֶ�9
	 */
	public UFDouble getNdef9() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF9);
	}

	/**
	 * �����Զ����ֶ�9
	 * 
	 * @param ndef9
	 *            �Զ����ֶ�9
	 */
	public void setNdef9(UFDouble ndef9) {
		this.setAttributeValue(StockBalanceVO.NDEF9, ndef9);
	}

	/**
	 * ��ȡ�ʲ�����
	 * 
	 * @return �ʲ�����
	 */
	public String getPk_assetsprop() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_ASSETSPROP);
	}

	/**
	 * �����ʲ�����
	 * 
	 * @param pk_assetsprop
	 *            �ʲ�����
	 */
	public void setPk_assetsprop(String pk_assetsprop) {
		this.setAttributeValue(StockBalanceVO.PK_ASSETSPROP, pk_assetsprop);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_BILLTYPE);
	}

	/**
	 * ���õ�������
	 * 
	 * @param pk_billtype
	 *            ��������
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(StockBalanceVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * ��ȡ���ҵ�����
	 * 
	 * @return ���ҵ�����
	 */
	public String getPk_billtypegroup() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_BILLTYPEGROUP);
	}

	/**
	 * �������ҵ�����
	 * 
	 * @param pk_billtypegroup
	 *            ���ҵ�����
	 */
	public void setPk_billtypegroup(String pk_billtypegroup) {
		this.setAttributeValue(StockBalanceVO.PK_BILLTYPEGROUP, pk_billtypegroup);
	}

	/**
	 * ��ȡ�ʽ��˺�
	 * 
	 * @return �ʽ��˺�
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_CAPACCOUNT);
	}

	/**
	 * �����ʽ��˺�
	 * 
	 * @param pk_capaccount
	 *            �ʽ��˺�
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(StockBalanceVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * ��ȡ������λ
	 * 
	 * @return ������λ
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_CLIENT);
	}

	/**
	 * ����������λ
	 * 
	 * @param pk_client
	 *            ������λ
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(StockBalanceVO.PK_CLIENT, pk_client);
	}

	/**
	 * ��ȡ���㷽��
	 * 
	 * @return ���㷽��
	 */
	public String getPk_costplan() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_COSTPLAN);
	}

	/**
	 * ���ü��㷽��
	 * 
	 * @param pk_costplan
	 *            ���㷽��
	 */
	public void setPk_costplan(String pk_costplan) {
		this.setAttributeValue(StockBalanceVO.PK_COSTPLAN, pk_costplan);
	}

	/**
	 * ��ȡ�˲�
	 * 
	 * @return �˲�
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_GLORGBOOK);
	}

	/**
	 * �����˲�
	 * 
	 * @param pk_glorgbook
	 *            �˲�
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(StockBalanceVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(StockBalanceVO.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ�ֻ��ص�
	 * 
	 * @return �ֻ��ص�
	 */
	public String getPk_operatesite() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_OPERATESITE);
	}

	/**
	 * ���÷ֻ��ص�
	 * 
	 * @param pk_operatesite
	 *            �ֻ��ص�
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(StockBalanceVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(StockBalanceVO.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(StockBalanceVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ�ɶ��˺�
	 * 
	 * @return �ɶ��˺�
	 */
	public String getPk_partnaccount() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_PARTNACCOUNT);
	}

	/**
	 * ���ùɶ��˺�
	 * 
	 * @param pk_partnaccount
	 *            �ɶ��˺�
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(StockBalanceVO.PK_PARTNACCOUNT, pk_partnaccount);
	}

	/**
	 * ��ȡ����֤ȯ
	 * 
	 * @return ����֤ȯ
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_SECURITIES);
	}

	/**
	 * ���ý���֤ȯ
	 * 
	 * @param pk_securities
	 *            ����֤ȯ
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(StockBalanceVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * ��ȡҵ��С��
	 * 
	 * @return ҵ��С��
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_SELFSGROUP);
	}

	/**
	 * ����ҵ��С��
	 * 
	 * @param pk_selfsgroup
	 *            ҵ��С��
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(StockBalanceVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_stockbalance() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_STOCKBALANCE);
	}

	/**
	 * ��������
	 * 
	 * @param pk_stockbalance
	 *            ����
	 */
	public void setPk_stockbalance(String pk_stockbalance) {
		this.setAttributeValue(StockBalanceVO.PK_STOCKBALANCE, pk_stockbalance);
	}

	/**
	 * ��ȡ�����֯
	 * 
	 * @return �����֯
	 */
	public String getPk_stocksort() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_STOCKSORT);
	}

	/**
	 * ���ÿ����֯
	 * 
	 * @param pk_stocksort
	 *            �����֯
	 */
	public void setPk_stocksort(String pk_stocksort) {
		this.setAttributeValue(StockBalanceVO.PK_STOCKSORT, pk_stocksort);
	}

	/**
	 * ��ȡ״̬
	 * 
	 * @return ״̬
	 */
	public Integer getState() {
		return (Integer) this.getAttributeValue(StockBalanceVO.STATE);
	}

	/**
	 * ����״̬
	 * 
	 * @param state
	 *            ״̬
	 */
	public void setState(Integer state) {
		this.setAttributeValue(StockBalanceVO.STATE, state);
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return �������
	 */
	public UFDouble getStocks_num() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.STOCKS_NUM);
	}

	/**
	 * ���ý������
	 * 
	 * @param stocks_num
	 *            �������
	 */
	public void setStocks_num(UFDouble stocks_num) {
		this.setAttributeValue(StockBalanceVO.STOCKS_NUM, stocks_num);
	}

	/**
	 * ��ȡ�����
	 * 
	 * @return �����
	 */
	public UFDouble getStocks_sum() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.STOCKS_SUM);
	}

	/**
	 * ���ý����
	 * 
	 * @param stocks_sum
	 *            �����
	 */
	public void setStocks_sum(UFDouble stocks_sum) {
		this.setAttributeValue(StockBalanceVO.STOCKS_SUM, stocks_sum);
	}

	/**
	 * ��ȡ���˰��
	 * 
	 * @return ���˰��
	 */
	public UFDouble getStocks_tax() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.STOCKS_TAX);
	}

	/**
	 * ���ý��˰��
	 * 
	 * @param stocks_tax
	 *            ���˰��
	 */
	public void setStocks_tax(UFDouble stocks_tax) {
		this.setAttributeValue(StockBalanceVO.STOCKS_TAX, stocks_tax);
	}

	/**
	 * ��ȡ�ۼ�����
	 * 
	 * @return �ۼ�����
	 */
	public UFDouble getTotal_win() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.TOTAL_WIN);
	}

	/**
	 * �����ۼ�����
	 * 
	 * @param total_win
	 *            �ۼ�����
	 */
	public void setTotal_win(UFDouble total_win) {
		this.setAttributeValue(StockBalanceVO.TOTAL_WIN, total_win);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(StockBalanceVO.TRADE_DATE);
	}

	/**
	 * ���ý�������
	 * 
	 * @param trade_date
	 *            ��������
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(StockBalanceVO.TRADE_DATE, trade_date);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(StockBalanceVO.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(StockBalanceVO.TS, ts);
	}

	/**
	 * ��ȡ�Զ����ֶ�1
	 * 
	 * @return �Զ����ֶ�1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF1);
	}

	/**
	 * �����Զ����ֶ�1
	 * 
	 * @param vdef1
	 *            �Զ����ֶ�1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(StockBalanceVO.VDEF1, vdef1);
	}

	/**
	 * ��ȡ�Զ����ֶ�2
	 * 
	 * @return �Զ����ֶ�2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF2);
	}

	/**
	 * �����Զ����ֶ�2
	 * 
	 * @param vdef2
	 *            �Զ����ֶ�2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(StockBalanceVO.VDEF2, vdef2);
	}

	/**
	 * ��ȡ�Զ����ֶ�3
	 * 
	 * @return �Զ����ֶ�3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF3);
	}

	/**
	 * �����Զ����ֶ�3
	 * 
	 * @param vdef3
	 *            �Զ����ֶ�3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(StockBalanceVO.VDEF3, vdef3);
	}

	/**
	 * ��ȡ�Զ����ֶ�4
	 * 
	 * @return �Զ����ֶ�4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF4);
	}

	/**
	 * �����Զ����ֶ�4
	 * 
	 * @param vdef4
	 *            �Զ����ֶ�4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(StockBalanceVO.VDEF4, vdef4);
	}

	/**
	 * ��ȡ�Զ����ֶ�5
	 * 
	 * @return �Զ����ֶ�5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF5);
	}

	/**
	 * �����Զ����ֶ�5
	 * 
	 * @param vdef5
	 *            �Զ����ֶ�5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(StockBalanceVO.VDEF5, vdef5);
	}

	/***
	 * YangJie 2014-04-02 �����ݿ��ֶ� �Ƿ�����
	 */
	private boolean isnew = false;

	public boolean getIsnew() {
		return isnew;
	}

	public void setIsnew(boolean isnew) {
		this.isnew = isnew;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_sim.StockBalanceVO");
	}

	@Override
	public UFDouble getBargain_num() {
		return getStocks_num();
	}

	@Override
	public void setBargain_num(UFDouble bargain_num) {
		
	}

	@Override
	public UFDouble getBargain_sum() {
		return getStocks_sum();
	}

	@Override
	public void setBargain_sum(UFDouble bargain_sum) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getBillno() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setBillno(String billno) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getBilltypecode() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setBilltypecode(String billtypecode) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public UFDouble getFact_sum() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setFact_sum(UFDouble fact_sum) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public UFDouble getFairvalue() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setFairvalue(UFDouble fairvalue) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public UFDouble getInterest() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setInterest(UFDouble interest) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getPk_transtype() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setPk_transtype(String pk_transtype) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getTranstypecode() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setTranstypecode(String transtypecode) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public UFLiteralDate getHr_begin_date() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_begin_date(UFLiteralDate hr_begin_date) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public UFLiteralDate getHr_end_date() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_end_date(UFLiteralDate hr_end_date) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getHr_pk_assetsprop() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_pk_assetsprop(String hr_pk_assetsprop) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getHr_pk_capaccount() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_pk_capaccount(String hr_pk_capaccount) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getHr_pk_client() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_pk_client(String hr_pk_client) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getHr_pk_operatesite() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_pk_operatesite(String hr_pk_operatesite) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getHr_pk_partnaccount() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_pk_partnaccount(String hr_pk_partnaccount) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getHr_pk_securities() {
		return getPk_securities();
	}

	@Override
	public void setHr_pk_securities(String hr_Pk_securities) {
		setPk_securities(hr_Pk_securities);
	}

	@Override
	public String getHr_pk_selfsgroup() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_pk_selfsgroup(String hr_pk_selfsgroup) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public String getHr_pk_stocksort() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setHr_pk_stocksort(String hr_pk_stocksort) {
	}

	@Override
	public UFDouble getHr_bargain_num() {
		return getBargain_num();
	}

	@Override
	public void setHr_bargain_num(UFDouble bargain_num) {
		setBargain_num(bargain_num);
	}
	
	private Integer genway = CostConstant.FORWARD_GENSTATE.ZHUANCHU.getIndex();

	public Integer getGenway() {
		return genway;
	}

	public void setGenway(Integer genway) {
		this.genway = genway;
	}

	public Map<String, UFDouble> getStock_map() {
		return stock_map;
	}

	public void setStock_map(Map<String, UFDouble> stock_map) {
		this.stock_map = stock_map;
	}
	
}