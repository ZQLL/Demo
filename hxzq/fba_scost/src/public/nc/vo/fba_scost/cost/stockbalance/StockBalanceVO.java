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
	
	//实际利率使用，保留
	private Map<String,UFDouble> stock_map = new HashMap<String,UFDouble>();

	/**
	 * 应收利息
	 */
	public static final String ACCRUAL_SUM = "accrual_sum";
	/**
	 * 估值增值
	 */
	public static final String ASSESS_ADD = "assess_add";
	/**
	 * 限售开始日期
	 */
	public static final String BEGIN_DATE = "begin_date";
	/**
	 * 创建时间
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * 创建人
	 */
	public static final String CREATOR = "creator";
	/**
	 * 限售结束日期
	 */
	public static final String END_DATE = "end_date";
	/**
	 * 冻结数量
	 */
	public static final String FROZEN_NUM = "frozen_num";
	/**
	 * 冻结金额
	 */
	public static final String FROZEN_SUM = "frozen_sum";
	/**
	 * 冻结税金
	 */
	public static final String FROZEN_TAX = "frozen_tax";
	/**
	 * 修改时间
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * 修改人
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * 自定义字段10
	 */
	public static final String NDEF10 = "ndef10";
	/**
	 * 自定义字段11
	 */
	public static final String NDEF11 = "ndef11";
	/**
	 * 自定义字段12
	 */
	public static final String NDEF12 = "ndef12";
	/**
	 * 自定义字段13
	 */
	public static final String NDEF13 = "ndef13";
	/**
	 * 自定义字段14
	 */
	public static final String NDEF14 = "ndef14";
	/**
	 * 自定义字段6
	 */
	public static final String NDEF6 = "ndef6";
	/**
	 * 自定义字段7
	 */
	public static final String NDEF7 = "ndef7";
	/**
	 * 自定义字段8
	 */
	public static final String NDEF8 = "ndef8";
	/**
	 * 自定义字段9
	 */
	public static final String NDEF9 = "ndef9";
	/**
	 * 资产分类
	 */
	public static final String PK_ASSETSPROP = "pk_assetsprop";
	/**
	 * 单据类型
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * 审核业务分组
	 */
	public static final String PK_BILLTYPEGROUP = "pk_billtypegroup";
	/**
	 * 资金账号
	 */
	public static final String PK_CAPACCOUNT = "pk_capaccount";
	/**
	 * 往来单位
	 */
	public static final String PK_CLIENT = "pk_client";
	/**
	 * 计算方案
	 */
	public static final String PK_COSTPLAN = "pk_costplan";
	/**
	 * 账簿
	 */
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	/**
	 * 集团
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * 分户地点
	 */
	public static final String PK_OPERATESITE = "pk_operatesite";
	/**
	 * 组织
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * 组织版本
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * 股东账号
	 */
	public static final String PK_PARTNACCOUNT = "pk_partnaccount";
	/**
	 * 交易证券
	 */
	public static final String PK_SECURITIES = "pk_securities";
	/**
	 * 业务小组
	 */
	public static final String PK_SELFSGROUP = "pk_selfsgroup";
	/**
	 * 主键
	 */
	public static final String PK_STOCKBALANCE = "pk_stockbalance";
	/**
	 * 库存组织
	 */
	public static final String PK_STOCKSORT = "pk_stocksort";
	/**
	 * 状态
	 */
	public static final String STATE = "state";
	/**
	 * 结存数量
	 */
	public static final String STOCKS_NUM = "stocks_num";
	/**
	 * 结存金额
	 */
	public static final String STOCKS_SUM = "stocks_sum";
	/**
	 * 结存税金
	 */
	public static final String STOCKS_TAX = "stocks_tax";
	/**
	 * 累计收益
	 */
	public static final String TOTAL_WIN = "total_win";
	/**
	 * 交易日期
	 */
	public static final String TRADE_DATE = "trade_date";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";
	/**
	 * 自定义字段1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * 自定义字段2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * 自定义字段3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * 自定义字段4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * 自定义字段5
	 */
	public static final String VDEF5 = "vdef5";

	/**
	 * 获取应收利息
	 * 
	 * @return 应收利息
	 */
	public UFDouble getAccrual_sum() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.ACCRUAL_SUM);
	}

	/**
	 * 设置应收利息
	 * 
	 * @param accrual_sum
	 *            应收利息
	 */
	public void setAccrual_sum(UFDouble accrual_sum) {
		this.setAttributeValue(StockBalanceVO.ACCRUAL_SUM, accrual_sum);
	}

	/**
	 * 获取估值增值
	 * 
	 * @return 估值增值
	 */
	public UFDouble getAssess_add() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.ASSESS_ADD);
	}

	/**
	 * 设置估值增值
	 * 
	 * @param assess_add
	 *            估值增值
	 */
	public void setAssess_add(UFDouble assess_add) {
		this.setAttributeValue(StockBalanceVO.ASSESS_ADD, assess_add);
	}

	/**
	 * 获取限售开始日期
	 * 
	 * @return 限售开始日期
	 */
	public UFLiteralDate getBegin_date() {
		return (UFLiteralDate) this.getAttributeValue(StockBalanceVO.BEGIN_DATE);
	}

	/**
	 * 设置限售开始日期
	 * 
	 * @param begin_date
	 *            限售开始日期
	 */
	public void setBegin_date(UFLiteralDate begin_date) {
		this.setAttributeValue(StockBalanceVO.BEGIN_DATE, begin_date);
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(StockBalanceVO.CREATIONTIME);
	}

	/**
	 * 设置创建时间
	 * 
	 * @param creationtime
	 *            创建时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(StockBalanceVO.CREATIONTIME, creationtime);
	}

	/**
	 * 获取创建人
	 * 
	 * @return 创建人
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(StockBalanceVO.CREATOR);
	}

	/**
	 * 设置创建人
	 * 
	 * @param creator
	 *            创建人
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(StockBalanceVO.CREATOR, creator);
	}

	/**
	 * 获取限售结束日期
	 * 
	 * @return 限售结束日期
	 */
	public UFLiteralDate getEnd_date() {
		return (UFLiteralDate) this.getAttributeValue(StockBalanceVO.END_DATE);
	}

	/**
	 * 设置限售结束日期
	 * 
	 * @param end_date
	 *            限售结束日期
	 */
	public void setEnd_date(UFLiteralDate end_date) {
		this.setAttributeValue(StockBalanceVO.END_DATE, end_date);
	}

	/**
	 * 获取冻结数量
	 * 
	 * @return 冻结数量
	 */
	public UFDouble getFrozen_num() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.FROZEN_NUM);
	}

	/**
	 * 设置冻结数量
	 * 
	 * @param frozen_num
	 *            冻结数量
	 */
	public void setFrozen_num(UFDouble frozen_num) {
		this.setAttributeValue(StockBalanceVO.FROZEN_NUM, frozen_num);
	}

	/**
	 * 获取冻结金额
	 * 
	 * @return 冻结金额
	 */
	public UFDouble getFrozen_sum() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.FROZEN_SUM);
	}

	/**
	 * 设置冻结金额
	 * 
	 * @param frozen_sum
	 *            冻结金额
	 */
	public void setFrozen_sum(UFDouble frozen_sum) {
		this.setAttributeValue(StockBalanceVO.FROZEN_SUM, frozen_sum);
	}

	/**
	 * 获取冻结税金
	 * 
	 * @return 冻结税金
	 */
	public UFDouble getFrozen_tax() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.FROZEN_TAX);
	}

	/**
	 * 设置冻结税金
	 * 
	 * @param frozen_tax
	 *            冻结税金
	 */
	public void setFrozen_tax(UFDouble frozen_tax) {
		this.setAttributeValue(StockBalanceVO.FROZEN_TAX, frozen_tax);
	}

	/**
	 * 获取修改时间
	 * 
	 * @return 修改时间
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(StockBalanceVO.MODIFIEDTIME);
	}

	/**
	 * 设置修改时间
	 * 
	 * @param modifiedtime
	 *            修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(StockBalanceVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * 获取修改人
	 * 
	 * @return 修改人
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(StockBalanceVO.MODIFIER);
	}

	/**
	 * 设置修改人
	 * 
	 * @param modifier
	 *            修改人
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(StockBalanceVO.MODIFIER, modifier);
	}

	/**
	 * 获取自定义字段10
	 * 
	 * @return 自定义字段10
	 */
	public UFDouble getNdef10() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF10);
	}

	/**
	 * 设置自定义字段10
	 * 
	 * @param ndef10
	 *            自定义字段10
	 */
	public void setNdef10(UFDouble ndef10) {
		this.setAttributeValue(StockBalanceVO.NDEF10, ndef10);
	}

	/**
	 * 获取自定义字段11
	 * 
	 * @return 自定义字段11
	 */
	public Integer getNdef11() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF11);
	}

	/**
	 * 设置自定义字段11
	 * 
	 * @param ndef11
	 *            自定义字段11
	 */
	public void setNdef11(Integer ndef11) {
		this.setAttributeValue(StockBalanceVO.NDEF11, ndef11);
	}

	/**
	 * 获取自定义字段12
	 * 
	 * @return 自定义字段12
	 */
	public Integer getNdef12() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF12);
	}

	/**
	 * 设置自定义字段12
	 * 
	 * @param ndef12
	 *            自定义字段12
	 */
	public void setNdef12(Integer ndef12) {
		this.setAttributeValue(StockBalanceVO.NDEF12, ndef12);
	}

	/**
	 * 获取自定义字段13
	 * 
	 * @return 自定义字段13
	 */
	public Integer getNdef13() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF13);
	}

	/**
	 * 设置自定义字段13
	 * 
	 * @param ndef13
	 *            自定义字段13
	 */
	public void setNdef13(Integer ndef13) {
		this.setAttributeValue(StockBalanceVO.NDEF13, ndef13);
	}

	/**
	 * 获取自定义字段14
	 * 
	 * @return 自定义字段14
	 */
	public Integer getNdef14() {
		return (Integer) this.getAttributeValue(StockBalanceVO.NDEF14);
	}

	/**
	 * 设置自定义字段14
	 * 
	 * @param ndef14
	 *            自定义字段14
	 */
	public void setNdef14(Integer ndef14) {
		this.setAttributeValue(StockBalanceVO.NDEF14, ndef14);
	}

	/**
	 * 获取自定义字段6
	 * 
	 * @return 自定义字段6
	 */
	public UFDouble getNdef6() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF6);
	}

	/**
	 * 设置自定义字段6
	 * 
	 * @param ndef6
	 *            自定义字段6
	 */
	public void setNdef6(UFDouble ndef6) {
		this.setAttributeValue(StockBalanceVO.NDEF6, ndef6);
	}

	/**
	 * 获取自定义字段7
	 * 
	 * @return 自定义字段7
	 */
	public UFDouble getNdef7() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF7);
	}

	/**
	 * 设置自定义字段7
	 * 
	 * @param ndef7
	 *            自定义字段7
	 */
	public void setNdef7(UFDouble ndef7) {
		this.setAttributeValue(StockBalanceVO.NDEF7, ndef7);
	}

	/**
	 * 获取自定义字段8
	 * 
	 * @return 自定义字段8
	 */
	public UFDouble getNdef8() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF8);
	}

	/**
	 * 设置自定义字段8
	 * 
	 * @param ndef8
	 *            自定义字段8
	 */
	public void setNdef8(UFDouble ndef8) {
		this.setAttributeValue(StockBalanceVO.NDEF8, ndef8);
	}

	/**
	 * 获取自定义字段9
	 * 
	 * @return 自定义字段9
	 */
	public UFDouble getNdef9() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.NDEF9);
	}

	/**
	 * 设置自定义字段9
	 * 
	 * @param ndef9
	 *            自定义字段9
	 */
	public void setNdef9(UFDouble ndef9) {
		this.setAttributeValue(StockBalanceVO.NDEF9, ndef9);
	}

	/**
	 * 获取资产分类
	 * 
	 * @return 资产分类
	 */
	public String getPk_assetsprop() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_ASSETSPROP);
	}

	/**
	 * 设置资产分类
	 * 
	 * @param pk_assetsprop
	 *            资产分类
	 */
	public void setPk_assetsprop(String pk_assetsprop) {
		this.setAttributeValue(StockBalanceVO.PK_ASSETSPROP, pk_assetsprop);
	}

	/**
	 * 获取单据类型
	 * 
	 * @return 单据类型
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_BILLTYPE);
	}

	/**
	 * 设置单据类型
	 * 
	 * @param pk_billtype
	 *            单据类型
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(StockBalanceVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * 获取审核业务分组
	 * 
	 * @return 审核业务分组
	 */
	public String getPk_billtypegroup() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_BILLTYPEGROUP);
	}

	/**
	 * 设置审核业务分组
	 * 
	 * @param pk_billtypegroup
	 *            审核业务分组
	 */
	public void setPk_billtypegroup(String pk_billtypegroup) {
		this.setAttributeValue(StockBalanceVO.PK_BILLTYPEGROUP, pk_billtypegroup);
	}

	/**
	 * 获取资金账号
	 * 
	 * @return 资金账号
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_CAPACCOUNT);
	}

	/**
	 * 设置资金账号
	 * 
	 * @param pk_capaccount
	 *            资金账号
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(StockBalanceVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * 获取往来单位
	 * 
	 * @return 往来单位
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_CLIENT);
	}

	/**
	 * 设置往来单位
	 * 
	 * @param pk_client
	 *            往来单位
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(StockBalanceVO.PK_CLIENT, pk_client);
	}

	/**
	 * 获取计算方案
	 * 
	 * @return 计算方案
	 */
	public String getPk_costplan() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_COSTPLAN);
	}

	/**
	 * 设置计算方案
	 * 
	 * @param pk_costplan
	 *            计算方案
	 */
	public void setPk_costplan(String pk_costplan) {
		this.setAttributeValue(StockBalanceVO.PK_COSTPLAN, pk_costplan);
	}

	/**
	 * 获取账簿
	 * 
	 * @return 账簿
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_GLORGBOOK);
	}

	/**
	 * 设置账簿
	 * 
	 * @param pk_glorgbook
	 *            账簿
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(StockBalanceVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(StockBalanceVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取分户地点
	 * 
	 * @return 分户地点
	 */
	public String getPk_operatesite() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_OPERATESITE);
	}

	/**
	 * 设置分户地点
	 * 
	 * @param pk_operatesite
	 *            分户地点
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(StockBalanceVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(StockBalanceVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(StockBalanceVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取股东账号
	 * 
	 * @return 股东账号
	 */
	public String getPk_partnaccount() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_PARTNACCOUNT);
	}

	/**
	 * 设置股东账号
	 * 
	 * @param pk_partnaccount
	 *            股东账号
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(StockBalanceVO.PK_PARTNACCOUNT, pk_partnaccount);
	}

	/**
	 * 获取交易证券
	 * 
	 * @return 交易证券
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_SECURITIES);
	}

	/**
	 * 设置交易证券
	 * 
	 * @param pk_securities
	 *            交易证券
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(StockBalanceVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * 获取业务小组
	 * 
	 * @return 业务小组
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_SELFSGROUP);
	}

	/**
	 * 设置业务小组
	 * 
	 * @param pk_selfsgroup
	 *            业务小组
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(StockBalanceVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public String getPk_stockbalance() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_STOCKBALANCE);
	}

	/**
	 * 设置主键
	 * 
	 * @param pk_stockbalance
	 *            主键
	 */
	public void setPk_stockbalance(String pk_stockbalance) {
		this.setAttributeValue(StockBalanceVO.PK_STOCKBALANCE, pk_stockbalance);
	}

	/**
	 * 获取库存组织
	 * 
	 * @return 库存组织
	 */
	public String getPk_stocksort() {
		return (String) this.getAttributeValue(StockBalanceVO.PK_STOCKSORT);
	}

	/**
	 * 设置库存组织
	 * 
	 * @param pk_stocksort
	 *            库存组织
	 */
	public void setPk_stocksort(String pk_stocksort) {
		this.setAttributeValue(StockBalanceVO.PK_STOCKSORT, pk_stocksort);
	}

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public Integer getState() {
		return (Integer) this.getAttributeValue(StockBalanceVO.STATE);
	}

	/**
	 * 设置状态
	 * 
	 * @param state
	 *            状态
	 */
	public void setState(Integer state) {
		this.setAttributeValue(StockBalanceVO.STATE, state);
	}

	/**
	 * 获取结存数量
	 * 
	 * @return 结存数量
	 */
	public UFDouble getStocks_num() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.STOCKS_NUM);
	}

	/**
	 * 设置结存数量
	 * 
	 * @param stocks_num
	 *            结存数量
	 */
	public void setStocks_num(UFDouble stocks_num) {
		this.setAttributeValue(StockBalanceVO.STOCKS_NUM, stocks_num);
	}

	/**
	 * 获取结存金额
	 * 
	 * @return 结存金额
	 */
	public UFDouble getStocks_sum() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.STOCKS_SUM);
	}

	/**
	 * 设置结存金额
	 * 
	 * @param stocks_sum
	 *            结存金额
	 */
	public void setStocks_sum(UFDouble stocks_sum) {
		this.setAttributeValue(StockBalanceVO.STOCKS_SUM, stocks_sum);
	}

	/**
	 * 获取结存税金
	 * 
	 * @return 结存税金
	 */
	public UFDouble getStocks_tax() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.STOCKS_TAX);
	}

	/**
	 * 设置结存税金
	 * 
	 * @param stocks_tax
	 *            结存税金
	 */
	public void setStocks_tax(UFDouble stocks_tax) {
		this.setAttributeValue(StockBalanceVO.STOCKS_TAX, stocks_tax);
	}

	/**
	 * 获取累计收益
	 * 
	 * @return 累计收益
	 */
	public UFDouble getTotal_win() {
		return (UFDouble) this.getAttributeValue(StockBalanceVO.TOTAL_WIN);
	}

	/**
	 * 设置累计收益
	 * 
	 * @param total_win
	 *            累计收益
	 */
	public void setTotal_win(UFDouble total_win) {
		this.setAttributeValue(StockBalanceVO.TOTAL_WIN, total_win);
	}

	/**
	 * 获取交易日期
	 * 
	 * @return 交易日期
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(StockBalanceVO.TRADE_DATE);
	}

	/**
	 * 设置交易日期
	 * 
	 * @param trade_date
	 *            交易日期
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(StockBalanceVO.TRADE_DATE, trade_date);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(StockBalanceVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(StockBalanceVO.TS, ts);
	}

	/**
	 * 获取自定义字段1
	 * 
	 * @return 自定义字段1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF1);
	}

	/**
	 * 设置自定义字段1
	 * 
	 * @param vdef1
	 *            自定义字段1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(StockBalanceVO.VDEF1, vdef1);
	}

	/**
	 * 获取自定义字段2
	 * 
	 * @return 自定义字段2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF2);
	}

	/**
	 * 设置自定义字段2
	 * 
	 * @param vdef2
	 *            自定义字段2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(StockBalanceVO.VDEF2, vdef2);
	}

	/**
	 * 获取自定义字段3
	 * 
	 * @return 自定义字段3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF3);
	}

	/**
	 * 设置自定义字段3
	 * 
	 * @param vdef3
	 *            自定义字段3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(StockBalanceVO.VDEF3, vdef3);
	}

	/**
	 * 获取自定义字段4
	 * 
	 * @return 自定义字段4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF4);
	}

	/**
	 * 设置自定义字段4
	 * 
	 * @param vdef4
	 *            自定义字段4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(StockBalanceVO.VDEF4, vdef4);
	}

	/**
	 * 获取自定义字段5
	 * 
	 * @return 自定义字段5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(StockBalanceVO.VDEF5);
	}

	/**
	 * 设置自定义字段5
	 * 
	 * @param vdef5
	 *            自定义字段5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(StockBalanceVO.VDEF5, vdef5);
	}

	/***
	 * YangJie 2014-04-02 非数据库字段 是否新增
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
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getBillno() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setBillno(String billno) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getBilltypecode() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setBilltypecode(String billtypecode) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public UFDouble getFact_sum() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setFact_sum(UFDouble fact_sum) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public UFDouble getFairvalue() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setFairvalue(UFDouble fairvalue) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public UFDouble getInterest() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setInterest(UFDouble interest) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getPk_transtype() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setPk_transtype(String pk_transtype) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getTranstypecode() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setTranstypecode(String transtypecode) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public UFLiteralDate getHr_begin_date() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_begin_date(UFLiteralDate hr_begin_date) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public UFLiteralDate getHr_end_date() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_end_date(UFLiteralDate hr_end_date) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getHr_pk_assetsprop() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_pk_assetsprop(String hr_pk_assetsprop) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getHr_pk_capaccount() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_pk_capaccount(String hr_pk_capaccount) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getHr_pk_client() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_pk_client(String hr_pk_client) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getHr_pk_operatesite() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_pk_operatesite(String hr_pk_operatesite) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getHr_pk_partnaccount() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_pk_partnaccount(String hr_pk_partnaccount) {
		// TODO 自动生成的方法存根
		
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
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setHr_pk_selfsgroup(String hr_pk_selfsgroup) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getHr_pk_stocksort() {
		// TODO 自动生成的方法存根
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