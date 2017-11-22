package nc.itf.fba_scost.cost.pub;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;

/**
 * ��������----��Ϣ�ӿ�
 * 
 */
public interface ITrade_Data {

	public UFDouble getAccrual_sum();// ����Ӧ����Ϣ

	public void setAccrual_sum(UFDouble accrual_sum);

	public UFDouble getBargain_num();// �ɽ�����

	public void setBargain_num(UFDouble bargain_num);

	public UFDouble getBargain_sum();// �ɽ����

	public void setBargain_sum(UFDouble bargain_sum);

	public UFLiteralDate getBegin_date();// ���ۿ�ʼ����

	public void setBegin_date(UFLiteralDate begin_date);

	public UFLiteralDate getEnd_date();// ���۽�������

	public void setEnd_date(UFLiteralDate end_date);

	public String getBillno();// ���ݺ�

	public void setBillno(String billno);

	public String getBilltypecode();// ��������

	public void setBilltypecode(String billtypecode);

	public UFDouble getFact_sum();// ʵ���ո�

	public void setFact_sum(UFDouble fact_sum);

	public UFDouble getFairvalue();// ��ת����ֵ

	public void setFairvalue(UFDouble fairvalue);

	public UFDouble getInterest();// ��תӦ����Ϣ

	public void setInterest(UFDouble interest);

	public String getPk_assetsprop();// �ʲ�����

	public void setPk_assetsprop(String pk_assetsprop);

	public String getPk_billtype();// ��������pk

	public void setPk_billtype(String pk_billtype);

	public String getPk_capaccount(); // �ʽ��˺�

	public void setPk_capaccount(String pk_capaccount);

	public String getPk_client();// ������λ

	public void setPk_client(String pk_client);

	public String getPk_glorgbook();// �����˲�

	public void setPk_glorgbook(String pk_glorgbook);

	public String getPk_group();// ����

	public void setPk_group(String pk_group);

	public String getPk_operatesite();// �ֻ��ص�

	public void setPk_operatesite(String pk_operatesite);

	public String getPk_org();// ��֯

	public void setPk_org(String pk_org);

	public String getPk_org_v(); // ��֯�汾

	public void setPk_org_v(String pk_org_v);

	public String getPk_partnaccount();// �ɶ��˺�

	public void setPk_partnaccount(String pk_partnaccount);

	public String getPk_securities();// ����֤ȯ

	public void setPk_securities(String pk_securities);

	public String getPk_selfsgroup(); // ҵ��С��

	public void setPk_selfsgroup(String pk_selfsgroup);

	public String getPk_stocksort();// �����֯

	public void setPk_stocksort(String pk_stocksort);// �����֯

	public String getPk_transtype();// ��������pk

	public void setPk_transtype(String pk_transtype);

	public Integer getState();// ״̬

	public void setState(Integer state);

	public UFDate getTrade_date();// ��������

	public void setTrade_date(UFDate trade_date);

	public String getTranstypecode();// ��������

	public void setTranstypecode(String transtypecode);

	public Integer getGenway();// [ת�� ---1]��[ת��---- 2]��[ת��ת�� ----3]

	public UFLiteralDate getHr_begin_date();

	public void setHr_begin_date(UFLiteralDate hr_begin_date);

	public UFLiteralDate getHr_end_date();

	public void setHr_end_date(UFLiteralDate hr_end_date);

	public String getHr_pk_assetsprop();

	public void setHr_pk_assetsprop(String hr_pk_assetsprop);

	public String getHr_pk_capaccount();

	public void setHr_pk_capaccount(String hr_pk_capaccount);

	public String getHr_pk_client();

	public void setHr_pk_client(String hr_pk_client);

	public String getHr_pk_operatesite();

	public void setHr_pk_operatesite(String hr_pk_operatesite);

	public String getHr_pk_partnaccount();

	public void setHr_pk_partnaccount(String hr_pk_partnaccount);

	public String getHr_pk_securities();

	public void setHr_pk_securities(String hr_Pk_securities);

	public String getHr_pk_selfsgroup();

	public void setHr_pk_selfsgroup(String hr_pk_selfsgroup);

	public String getHr_pk_stocksort();

	public void setHr_pk_stocksort(String hr_pk_stocksort);

	public UFDouble getHr_bargain_num();

	public void setHr_bargain_num(UFDouble bargain_num);

}
