package nc.vo.fba_smt.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ����֤ȯ���������ϸ��VO
 */

public class AvailableDetailVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String pk_org;
	private String pk_group;
	private String pk_selfsgroup;
	private String pk_partnaccount;
	private String pk_glorgbook;// �˲�
	private String pk_sclassify;// ֤ȯ����
	private String pk_securities;// ֤ȯ

	private String securitiescode;// ֤ȯ����
	private String securitiesname;// ֤ȯ����

	private String pk_assetsprop;// �ʲ�����
	private String pk_stocksort;// �����֯
	private String pk_capaccount;// �ʽ��˺�
	private UFDate trade_date;// ��������

	// �ڳ�
	private UFDouble firstnum;// ���ڿ��ڳ�����
	private UFDouble firstsum;// ���ڿ��ڳ����
	private UFDouble firstMarket;// �ڳ���ֵ
	// ��������
	private UFDouble kgzkrnum;// �ɹ�ת��������
	private UFDouble kgzkrsum;// �ɹ�ת���ڽ��

	private UFDouble zrtjrnum;// ת��ͨ��������
	private UFDouble zrtjrsum;// ת��ͨ������

	private UFDouble rcghnum;// �ڳ��黹����
	private UFDouble rcghsum;// �ڳ��黹���

	private UFDouble hgqyhznum;// ���ȯԴ��ת����
	private UFDouble hgqyhzsum;// ���ȯԴ��ת���
	// ���ڼ���
	private UFDouble rczqnum;// �ڳ�֤ȯ����
	private UFDouble rczqsum;// �ڳ�֤ȯ���

	private UFDouble krzkgnum;// ����ת�ɹ�����
	private UFDouble krzkgsum;// ����ת�ɹ����

	private UFDouble zrtghnum;// ת��ͨ�黹����
	private UFDouble zrtghsum;// ת��ͨ�黹���

	private UFDouble hgrcnum;// ����ڳ�����
	private UFDouble hgrcsum;// ����ڳ����

	// ��ĩ���
	private UFDouble endnum;// ���ڿ���ĩ�������
	private UFDouble endsum;// ���ڿ���ĩ�����

	// ��ĩ���ӯ��
	private UFDouble closeprice;// ���̼�
	private UFDouble marketValue;// ֤ȯ��ֵ
	private UFDouble profit;// ӯ��

	public UFDouble getFirstMarket() {
		return firstMarket;
	}

	public void setFirstMarket(UFDouble firstMarket) {
		this.firstMarket = firstMarket;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_glorgbook() {
		return pk_glorgbook;
	}

	public void setPk_glorgbook(String pk_glorgbook) {
		this.pk_glorgbook = pk_glorgbook;
	}

	public String getPk_sclassify() {
		return pk_sclassify;
	}

	public void setPk_sclassify(String pk_sclassify) {
		this.pk_sclassify = pk_sclassify;
	}

	public String getSecuritiescode() {
		return securitiescode;
	}

	public void setSecuritiescode(String securitiescode) {
		this.securitiescode = securitiescode;
	}

	public String getSecuritiesname() {
		return securitiesname;
	}

	public void setSecuritiesname(String securitiesname) {
		this.securitiesname = securitiesname;
	}

	public String getPk_assetsprop() {
		return pk_assetsprop;
	}

	public void setPk_assetsprop(String pk_assetsprop) {
		this.pk_assetsprop = pk_assetsprop;
	}

	public String getPk_stocksort() {
		return pk_stocksort;
	}

	public void setPk_stocksort(String pk_stocksort) {
		this.pk_stocksort = pk_stocksort;
	}

	public String getPk_capaccount() {
		return pk_capaccount;
	}

	public void setPk_capaccount(String pk_capaccount) {
		this.pk_capaccount = pk_capaccount;
	}

	public UFDate getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(UFDate trade_date) {
		this.trade_date = trade_date;
	}

	public UFDouble getFirstnum() {
		return firstnum;
	}

	public void setFirstnum(UFDouble firstnum) {
		this.firstnum = firstnum;
	}

	public UFDouble getFirstsum() {
		return firstsum;
	}

	public void setFirstsum(UFDouble firstsum) {
		this.firstsum = firstsum;
	}

	public UFDouble getKgzkrnum() {
		return kgzkrnum;
	}

	public void setKgzkrnum(UFDouble kgzkrnum) {
		this.kgzkrnum = kgzkrnum;
	}

	public UFDouble getKgzkrsum() {
		return kgzkrsum;
	}

	public void setKgzkrsum(UFDouble kgzkrsum) {
		this.kgzkrsum = kgzkrsum;
	}

	public UFDouble getZrtjrnum() {
		return zrtjrnum;
	}

	public void setZrtjrnum(UFDouble zrtjrnum) {
		this.zrtjrnum = zrtjrnum;
	}

	public UFDouble getZrtjrsum() {
		return zrtjrsum;
	}

	public void setZrtjrsum(UFDouble zrtjrsum) {
		this.zrtjrsum = zrtjrsum;
	}

	public UFDouble getRcghnum() {
		return rcghnum;
	}

	public void setRcghnum(UFDouble rcghnum) {
		this.rcghnum = rcghnum;
	}

	public UFDouble getRcghsum() {
		return rcghsum;
	}

	public void setRcghsum(UFDouble rcghsum) {
		this.rcghsum = rcghsum;
	}

	public UFDouble getHgqyhznum() {
		return hgqyhznum;
	}

	public void setHgqyhznum(UFDouble hgqyhznum) {
		this.hgqyhznum = hgqyhznum;
	}

	public UFDouble getHgqyhzsum() {
		return hgqyhzsum;
	}

	public void setHgqyhzsum(UFDouble hgqyhzsum) {
		this.hgqyhzsum = hgqyhzsum;
	}

	public UFDouble getRczqnum() {
		return rczqnum;
	}

	public void setRczqnum(UFDouble rczqnum) {
		this.rczqnum = rczqnum;
	}

	public UFDouble getRczqsum() {
		return rczqsum;
	}

	public void setRczqsum(UFDouble rczqsum) {
		this.rczqsum = rczqsum;
	}

	public UFDouble getKrzkgnum() {
		return krzkgnum;
	}

	public void setKrzkgnum(UFDouble krzkgnum) {
		this.krzkgnum = krzkgnum;
	}

	public UFDouble getKrzkgsum() {
		return krzkgsum;
	}

	public void setKrzkgsum(UFDouble krzkgsum) {
		this.krzkgsum = krzkgsum;
	}

	public UFDouble getZrtghnum() {
		return zrtghnum;
	}

	public void setZrtghnum(UFDouble zrtghnum) {
		this.zrtghnum = zrtghnum;
	}

	public UFDouble getZrtghsum() {
		return zrtghsum;
	}

	public void setZrtghsum(UFDouble zrtghsum) {
		this.zrtghsum = zrtghsum;
	}

	public UFDouble getHgrcnum() {
		return hgrcnum;
	}

	public void setHgrcnum(UFDouble hgrcnum) {
		this.hgrcnum = hgrcnum;
	}

	public UFDouble getHgrcsum() {
		return hgrcsum;
	}

	public void setHgrcsum(UFDouble hgrcsum) {
		this.hgrcsum = hgrcsum;
	}

	public UFDouble getEndnum() {
		return endnum;
	}

	public void setEndnum(UFDouble endnum) {
		this.endnum = endnum;
	}

	public UFDouble getEndsum() {
		return endsum;
	}

	public void setEndsum(UFDouble endsum) {
		this.endsum = endsum;
	}

	public UFDouble getCloseprice() {
		return closeprice;
	}

	public void setCloseprice(UFDouble closeprice) {
		this.closeprice = closeprice;
	}

	public UFDouble getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(UFDouble marketValue) {
		this.marketValue = marketValue;
	}

	public UFDouble getProfit() {
		return profit;
	}

	public void setProfit(UFDouble profit) {
		this.profit = profit;
	}

	public String getPk_securities() {
		return pk_securities;
	}

	public void setPk_securities(String pk_securities) {
		this.pk_securities = pk_securities;
	}

	public String getPk_selfsgroup() {
		return pk_selfsgroup;
	}

	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.pk_selfsgroup = pk_selfsgroup;
	}

	public String getPk_partnaccount() {
		return pk_partnaccount;
	}

	public void setPk_partnaccount(String pk_partnaccount) {
		this.pk_partnaccount = pk_partnaccount;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
