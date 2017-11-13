package nc.vo.fba_scost.cost.trademarket;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class TradeMarketVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Ӧ����Ϣ
	 */
	public static final String ACCRUAL = "accrual";
	/**
	 * �վ���
	 */
	public static final String AVERAGE_PRICE = "average_price";
	/**
	 * ���̼۸�
	 */
	public static final String CLOSE_PRICE = "close_price";
	/**
	 * ����ʱ��
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * ������
	 */
	public static final String CREATOR = "creator";
	/**
	 * ����
	 */
	public static final String J_PRICE = "j_price";
	/**
	 * ��߼۸�
	 */
	public static final String MAX_PRICE = "max_price";
	/**
	 * ��ͼ۸�
	 */
	public static final String MIN_PRICE = "min_price";
	/**
	 * �޸�ʱ��
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * �޸���
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * ngzjj
	 */
	public static final String NGZJJ = "ngzjj";
	/**
	 * ngzqj
	 */
	public static final String NGZQJ = "ngzqj";
	/**
	 * nyear
	 */
	public static final String NYEAR = "nyear";
	/**
	 * ���̼۸�
	 */
	public static final String OPEN_PRICE = "open_price";
	/**
	 * ����
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * ��֯
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * ��֯�汾
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * ����֤ȯ
	 */
	public static final String PK_SECURITIES = "pk_securities";
	/**
	 * ����
	 */
	public static final String PK_TRADEMARKET = "pk_trademarket";
	/**
	 * ȫ��
	 */
	public static final String Q_PRICE = "q_price";
	/**
	 * ��������
	 */
	public static final String TRADE_DATE = "trade_date";
	/**
	 * ��Լ��λ
	 * 
	 * Ϊ�˸���Ȩ����ʱ�˺�Լ��λ�������ֶ�con_unit QS add 
	 * 
	 */
	
	public static final String CON_UNIT = "con_unit";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";
	/**
	 * ��Ϣ����
	 */
	public static final String YJLX = "yjlx";

	/**
	 * ��ȡӦ����Ϣ
	 * 
	 * @return Ӧ����Ϣ
	 */
	public static final String LISTING_CATEGORY = "listing_category";

	/**
	 * ��ȡӦ����Ϣ
	 * 
	 * @return Ӧ����Ϣ
	 */
	public UFDouble getAccrual() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.ACCRUAL);
	}

	/**
	 * ����Ӧ����Ϣ
	 * 
	 * @param accrual Ӧ����Ϣ
	 */
	public void setAccrual(UFDouble accrual) {
		this.setAttributeValue(TradeMarketVO.ACCRUAL, accrual);
	}

	/**
	 * ��ȡ�վ���
	 * 
	 * @return �վ���
	 */
	public UFDouble getAverage_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.AVERAGE_PRICE);
	}

	/**
	 * �����վ���
	 * 
	 * @param average_price �վ���
	 */
	public void setAverage_price(UFDouble average_price) {
		this.setAttributeValue(TradeMarketVO.AVERAGE_PRICE, average_price);
	}

	/**
	 * ��ȡ���̼۸�
	 * 
	 * @return ���̼۸�
	 */
	public UFDouble getClose_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.CLOSE_PRICE);
	}

	/**
	 * �������̼۸�
	 * 
	 * @param close_price ���̼۸�
	 */
	public void setClose_price(UFDouble close_price) {
		this.setAttributeValue(TradeMarketVO.CLOSE_PRICE, close_price);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(TradeMarketVO.CREATIONTIME);
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param creationtime ����ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(TradeMarketVO.CREATIONTIME, creationtime);
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(TradeMarketVO.CREATOR);
	}

	/**
	 * ���ô�����
	 * 
	 * @param creator ������
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(TradeMarketVO.CREATOR, creator);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public UFDouble getJ_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.J_PRICE);
	}

	/**
	 * ���þ���
	 * 
	 * @param j_price ����
	 */
	public void setJ_price(UFDouble j_price) {
		this.setAttributeValue(TradeMarketVO.J_PRICE, j_price);
	}

	/**
	 * ��ȡ��߼۸�
	 * 
	 * @return ��߼۸�
	 */
	public UFDouble getMax_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.MAX_PRICE);
	}

	/**
	 * ������߼۸�
	 * 
	 * @param max_price ��߼۸�
	 */
	public void setMax_price(UFDouble max_price) {
		this.setAttributeValue(TradeMarketVO.MAX_PRICE, max_price);
	}

	/**
	 * ��ȡ��ͼ۸�
	 * 
	 * @return ��ͼ۸�
	 */
	public UFDouble getMin_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.MIN_PRICE);
	}

	/**
	 * ������ͼ۸�
	 * 
	 * @param min_price ��ͼ۸�
	 */
	public void setMin_price(UFDouble min_price) {
		this.setAttributeValue(TradeMarketVO.MIN_PRICE, min_price);
	}

	/**
	 * ��ȡ�޸�ʱ��
	 * 
	 * @return �޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(TradeMarketVO.MODIFIEDTIME);
	}

	/**
	 * �����޸�ʱ��
	 * 
	 * @param modifiedtime �޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(TradeMarketVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * ��ȡ�޸���
	 * 
	 * @return �޸���
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(TradeMarketVO.MODIFIER);
	}

	/**
	 * �����޸���
	 * 
	 * @param modifier �޸���
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(TradeMarketVO.MODIFIER, modifier);
	}

	/**
	 * ��ȡngzjj
	 * 
	 * @return ngzjj
	 */
	public UFDouble getNgzjj() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.NGZJJ);
	}

	/**
	 * ����ngzjj
	 * 
	 * @param ngzjj ngzjj
	 */
	public void setNgzjj(UFDouble ngzjj) {
		this.setAttributeValue(TradeMarketVO.NGZJJ, ngzjj);
	}

	/**
	 * ��ȡngzqj
	 * 
	 * @return ngzqj
	 */
	public UFDouble getNgzqj() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.NGZQJ);
	}

	/**
	 * ����ngzqj
	 * 
	 * @param ngzqj ngzqj
	 */
	public void setNgzqj(UFDouble ngzqj) {
		this.setAttributeValue(TradeMarketVO.NGZQJ, ngzqj);
	}

	/**
	 * ��ȡnyear
	 * 
	 * @return nyear
	 */
	public Integer getNyear() {
		return (Integer) this.getAttributeValue(TradeMarketVO.NYEAR);
	}

	/**
	 * ����nyear
	 * 
	 * @param nyear nyear
	 */
	public void setNyear(Integer nyear) {
		this.setAttributeValue(TradeMarketVO.NYEAR, nyear);
	}

	/**
	 * ��ȡ���̼۸�
	 * 
	 * @return ���̼۸�
	 */
	public UFDouble getOpen_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.OPEN_PRICE);
	}

	/**
	 * ���ÿ��̼۸�
	 * 
	 * @param open_price ���̼۸�
	 */
	public void setOpen_price(UFDouble open_price) {
		this.setAttributeValue(TradeMarketVO.OPEN_PRICE, open_price);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_GROUP);
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group ����
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(TradeMarketVO.PK_GROUP, pk_group);
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_ORG);
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org ��֯
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(TradeMarketVO.PK_ORG, pk_org);
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_ORG_V);
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(TradeMarketVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * ��ȡ����֤ȯ
	 * 
	 * @return ����֤ȯ
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_SECURITIES);
	}

	/**
	 * ���ý���֤ȯ
	 * 
	 * @param pk_securities ����֤ȯ
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(TradeMarketVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_trademarket() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_TRADEMARKET);
	}

	/**
	 * ��������
	 * 
	 * @param pk_trademarket ����
	 */
	public void setPk_trademarket(String pk_trademarket) {
		this.setAttributeValue(TradeMarketVO.PK_TRADEMARKET, pk_trademarket);
	}

	/**
	 * ��ȡȫ��
	 * 
	 * @return ȫ��
	 */
	public UFDouble getQ_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.Q_PRICE);
	}

	/**
	 * ����ȫ��
	 * 
	 * @param q_price ȫ��
	 */
	public void setQ_price(UFDouble q_price) {
		this.setAttributeValue(TradeMarketVO.Q_PRICE, q_price);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(TradeMarketVO.TRADE_DATE);
	}

	/**
	 * ���ý�������
	 * 
	 * @param trade_date ��������
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(TradeMarketVO.TRADE_DATE, trade_date);
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(TradeMarketVO.TS);
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(TradeMarketVO.TS, ts);
	}

	/**
	 * ��ȡ��Ϣ����
	 * 
	 * @return ��Ϣ����
	 */
	public UFDouble getYjlx() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.YJLX);
	}

	/**
	 * ������Ϣ����
	 * 
	 * @param yjlx ��Ϣ����
	 */
	public void setYjlx(UFDouble yjlx) {
		this.setAttributeValue(TradeMarketVO.YJLX, yjlx);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public Integer getListing_category() {
		return (Integer) this.getAttributeValue(TradeMarketVO.LISTING_CATEGORY);
	}

	/**
	 * ���ù�������
	 * 
	 * @param listing_category ��������
	 */
	public void setListing_category(Integer listing_category) {
		this.setAttributeValue(TradeMarketVO.LISTING_CATEGORY, listing_category);
	}
	
	/**
	 * ��ȡ��Լ��Ԫ
	 * 
	 * @return ��Լ��Ԫ
	 */
	public UFDouble getCon_unit() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.CON_UNIT);
	}

	/**
	 * ���ú�Լ��Ԫ
	 * 
	 * @param con_unit ��Լ��Ԫ
	 */
	public void setCon_unit(UFDouble con_unit) {
		this.setAttributeValue(TradeMarketVO.CON_UNIT, con_unit);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_sim.trademarket");
	}
}