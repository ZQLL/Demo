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
	 * 应计利息
	 */
	public static final String ACCRUAL = "accrual";
	/**
	 * 日均价
	 */
	public static final String AVERAGE_PRICE = "average_price";
	/**
	 * 收盘价格
	 */
	public static final String CLOSE_PRICE = "close_price";
	/**
	 * 创建时间
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * 创建人
	 */
	public static final String CREATOR = "creator";
	/**
	 * 净价
	 */
	public static final String J_PRICE = "j_price";
	/**
	 * 最高价格
	 */
	public static final String MAX_PRICE = "max_price";
	/**
	 * 最低价格
	 */
	public static final String MIN_PRICE = "min_price";
	/**
	 * 修改时间
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * 修改人
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
	 * 开盘价格
	 */
	public static final String OPEN_PRICE = "open_price";
	/**
	 * 集团
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * 组织
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * 组织版本
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * 交易证券
	 */
	public static final String PK_SECURITIES = "pk_securities";
	/**
	 * 主键
	 */
	public static final String PK_TRADEMARKET = "pk_trademarket";
	/**
	 * 全价
	 */
	public static final String Q_PRICE = "q_price";
	/**
	 * 交易日期
	 */
	public static final String TRADE_DATE = "trade_date";
	/**
	 * 合约单位
	 * 
	 * 为了个期权计算时乘合约单位，新增字段con_unit QS add 
	 * 
	 */
	
	public static final String CON_UNIT = "con_unit";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";
	/**
	 * 利息单价
	 */
	public static final String YJLX = "yjlx";

	/**
	 * 获取应计利息
	 * 
	 * @return 应计利息
	 */
	public static final String LISTING_CATEGORY = "listing_category";

	/**
	 * 获取应计利息
	 * 
	 * @return 应计利息
	 */
	public UFDouble getAccrual() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.ACCRUAL);
	}

	/**
	 * 设置应计利息
	 * 
	 * @param accrual 应计利息
	 */
	public void setAccrual(UFDouble accrual) {
		this.setAttributeValue(TradeMarketVO.ACCRUAL, accrual);
	}

	/**
	 * 获取日均价
	 * 
	 * @return 日均价
	 */
	public UFDouble getAverage_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.AVERAGE_PRICE);
	}

	/**
	 * 设置日均价
	 * 
	 * @param average_price 日均价
	 */
	public void setAverage_price(UFDouble average_price) {
		this.setAttributeValue(TradeMarketVO.AVERAGE_PRICE, average_price);
	}

	/**
	 * 获取收盘价格
	 * 
	 * @return 收盘价格
	 */
	public UFDouble getClose_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.CLOSE_PRICE);
	}

	/**
	 * 设置收盘价格
	 * 
	 * @param close_price 收盘价格
	 */
	public void setClose_price(UFDouble close_price) {
		this.setAttributeValue(TradeMarketVO.CLOSE_PRICE, close_price);
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(TradeMarketVO.CREATIONTIME);
	}

	/**
	 * 设置创建时间
	 * 
	 * @param creationtime 创建时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(TradeMarketVO.CREATIONTIME, creationtime);
	}

	/**
	 * 获取创建人
	 * 
	 * @return 创建人
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(TradeMarketVO.CREATOR);
	}

	/**
	 * 设置创建人
	 * 
	 * @param creator 创建人
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(TradeMarketVO.CREATOR, creator);
	}

	/**
	 * 获取净价
	 * 
	 * @return 净价
	 */
	public UFDouble getJ_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.J_PRICE);
	}

	/**
	 * 设置净价
	 * 
	 * @param j_price 净价
	 */
	public void setJ_price(UFDouble j_price) {
		this.setAttributeValue(TradeMarketVO.J_PRICE, j_price);
	}

	/**
	 * 获取最高价格
	 * 
	 * @return 最高价格
	 */
	public UFDouble getMax_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.MAX_PRICE);
	}

	/**
	 * 设置最高价格
	 * 
	 * @param max_price 最高价格
	 */
	public void setMax_price(UFDouble max_price) {
		this.setAttributeValue(TradeMarketVO.MAX_PRICE, max_price);
	}

	/**
	 * 获取最低价格
	 * 
	 * @return 最低价格
	 */
	public UFDouble getMin_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.MIN_PRICE);
	}

	/**
	 * 设置最低价格
	 * 
	 * @param min_price 最低价格
	 */
	public void setMin_price(UFDouble min_price) {
		this.setAttributeValue(TradeMarketVO.MIN_PRICE, min_price);
	}

	/**
	 * 获取修改时间
	 * 
	 * @return 修改时间
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(TradeMarketVO.MODIFIEDTIME);
	}

	/**
	 * 设置修改时间
	 * 
	 * @param modifiedtime 修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(TradeMarketVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * 获取修改人
	 * 
	 * @return 修改人
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(TradeMarketVO.MODIFIER);
	}

	/**
	 * 设置修改人
	 * 
	 * @param modifier 修改人
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(TradeMarketVO.MODIFIER, modifier);
	}

	/**
	 * 获取ngzjj
	 * 
	 * @return ngzjj
	 */
	public UFDouble getNgzjj() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.NGZJJ);
	}

	/**
	 * 设置ngzjj
	 * 
	 * @param ngzjj ngzjj
	 */
	public void setNgzjj(UFDouble ngzjj) {
		this.setAttributeValue(TradeMarketVO.NGZJJ, ngzjj);
	}

	/**
	 * 获取ngzqj
	 * 
	 * @return ngzqj
	 */
	public UFDouble getNgzqj() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.NGZQJ);
	}

	/**
	 * 设置ngzqj
	 * 
	 * @param ngzqj ngzqj
	 */
	public void setNgzqj(UFDouble ngzqj) {
		this.setAttributeValue(TradeMarketVO.NGZQJ, ngzqj);
	}

	/**
	 * 获取nyear
	 * 
	 * @return nyear
	 */
	public Integer getNyear() {
		return (Integer) this.getAttributeValue(TradeMarketVO.NYEAR);
	}

	/**
	 * 设置nyear
	 * 
	 * @param nyear nyear
	 */
	public void setNyear(Integer nyear) {
		this.setAttributeValue(TradeMarketVO.NYEAR, nyear);
	}

	/**
	 * 获取开盘价格
	 * 
	 * @return 开盘价格
	 */
	public UFDouble getOpen_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.OPEN_PRICE);
	}

	/**
	 * 设置开盘价格
	 * 
	 * @param open_price 开盘价格
	 */
	public void setOpen_price(UFDouble open_price) {
		this.setAttributeValue(TradeMarketVO.OPEN_PRICE, open_price);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group 集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(TradeMarketVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org 组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(TradeMarketVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v 组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(TradeMarketVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取交易证券
	 * 
	 * @return 交易证券
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_SECURITIES);
	}

	/**
	 * 设置交易证券
	 * 
	 * @param pk_securities 交易证券
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(TradeMarketVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public String getPk_trademarket() {
		return (String) this.getAttributeValue(TradeMarketVO.PK_TRADEMARKET);
	}

	/**
	 * 设置主键
	 * 
	 * @param pk_trademarket 主键
	 */
	public void setPk_trademarket(String pk_trademarket) {
		this.setAttributeValue(TradeMarketVO.PK_TRADEMARKET, pk_trademarket);
	}

	/**
	 * 获取全价
	 * 
	 * @return 全价
	 */
	public UFDouble getQ_price() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.Q_PRICE);
	}

	/**
	 * 设置全价
	 * 
	 * @param q_price 全价
	 */
	public void setQ_price(UFDouble q_price) {
		this.setAttributeValue(TradeMarketVO.Q_PRICE, q_price);
	}

	/**
	 * 获取交易日期
	 * 
	 * @return 交易日期
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(TradeMarketVO.TRADE_DATE);
	}

	/**
	 * 设置交易日期
	 * 
	 * @param trade_date 交易日期
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(TradeMarketVO.TRADE_DATE, trade_date);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(TradeMarketVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts 时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(TradeMarketVO.TS, ts);
	}

	/**
	 * 获取利息单价
	 * 
	 * @return 利息单价
	 */
	public UFDouble getYjlx() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.YJLX);
	}

	/**
	 * 设置利息单价
	 * 
	 * @param yjlx 利息单价
	 */
	public void setYjlx(UFDouble yjlx) {
		this.setAttributeValue(TradeMarketVO.YJLX, yjlx);
	}

	/**
	 * 获取挂牌类型
	 * 
	 * @return 挂牌类型
	 */
	public Integer getListing_category() {
		return (Integer) this.getAttributeValue(TradeMarketVO.LISTING_CATEGORY);
	}

	/**
	 * 设置挂牌类型
	 * 
	 * @param listing_category 挂牌类型
	 */
	public void setListing_category(Integer listing_category) {
		this.setAttributeValue(TradeMarketVO.LISTING_CATEGORY, listing_category);
	}
	
	/**
	 * 获取合约单元
	 * 
	 * @return 合约单元
	 */
	public UFDouble getCon_unit() {
		return (UFDouble) this.getAttributeValue(TradeMarketVO.CON_UNIT);
	}

	/**
	 * 设置合约单元
	 * 
	 * @param con_unit 合约单元
	 */
	public void setCon_unit(UFDouble con_unit) {
		this.setAttributeValue(TradeMarketVO.CON_UNIT, con_unit);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_sim.trademarket");
	}
}