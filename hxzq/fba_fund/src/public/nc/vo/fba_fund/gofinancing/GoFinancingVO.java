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
	 * 审批时间
	 */
	public static final String APPROVEDATE = "approvedate";
	/**
	 * 审批人
	 */
	public static final String APPROVER = "approver";
	/**
	 * 成交数量
	 */
	public static final String BARGIN_NUM = "bargin_num";
	/**
	 * 成交金额
	 */
	public static final String BARGIN_SUM = "bargin_sum";
	/**
	 * 制单人
	 */
	public static final String BILLMAKER = "billmaker";
	/**
	 * 单据号
	 */
	public static final String BILLNO = "billno";
	/**
	 * 单据类型
	 */
	public static final String BILLTYPECODE = "billtypecode";
	/**
	 * 合同号
	 */
	public static final String CONTRACTNO = "contractno";
	/**
	 * 合约利率
	 */
	public static final String CONTRACTRATE = "contractrate";
	/**
	 * 创建时间
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * 创建人
	 */
	public static final String CREATOR = "creator";
	/**
	 * 到期日
	 */
	public static final String ENDDATE = "enddate";
	/**
	 * 委托合同
	 */
	public static final String ENTRUSTCONTRACT = "entrustcontract";
	/**
	 * 实际收付
	 */
	public static final String FACT_SUM = "fact_sum";
	/**
	 * 融资类型
	 */
	public static final String FUNDTYPE = "fundtype";
	/**
	 * 制单时间
	 */
	public static final String MAKEDATE = "makedate";
	/**
	 * 管理部门
	 */
	public static final String MANAGEDEPT = "managedept";
	/**
	 * 市场
	 */
	public static final String MARKET = "market";
	/**
	 * 修改时间
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * 修改人
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * 数值自定义项1
	 */
	public static final String NDEF1 = "ndef1";
	/**
	 * 数值自定义项10
	 */
	public static final String NDEF10 = "ndef10";
	/**
	 * 数值自定义项2
	 */
	public static final String NDEF2 = "ndef2";
	/**
	 * 数值自定义项3
	 */
	public static final String NDEF3 = "ndef3";
	/**
	 * 数值自定义项4
	 */
	public static final String NDEF4 = "ndef4";
	/**
	 * 数值自定义项5
	 */
	public static final String NDEF5 = "ndef5";
	/**
	 * 数值自定义项6
	 */
	public static final String NDEF6 = "ndef6";
	/**
	 * 数值自定义项7
	 */
	public static final String NDEF7 = "ndef7";
	/**
	 * 数值自定义项8
	 */
	public static final String NDEF8 = "ndef8";
	/**
	 * 数值自定义项9
	 */
	public static final String NDEF9 = "ndef9";
	/**
	 * 原委托合同
	 */
	public static final String ORIGINAL_ENTRUSTCONTRACT = "original_entrustcontract";
	/**
	 * 其他费用
	 */
	public static final String OTHER_FEE = "other_fee";
	/**
	 * 付息方式
	 */
	public static final String PAYTYPE = "paytype";
	/**
	 * 资产属性
	 */
	public static final String PK_ASSETSPROP = "pk_assetsprop";
	/**
	 * 单据类型pk
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * 交易所
	 */
	public static final String PK_BOURSE = "pk_bourse";
	/**
	 * 业务类型
	 */
	public static final String PK_BUSITYPE = "pk_busitype";
	/**
	 * 资金账号
	 */
	public static final String PK_CAPACCOUNT = "pk_capaccount";
	/**
	 * 往来单位
	 */
	public static final String PK_CLIENT = "pk_client";
	/**
	 * 币种
	 */
	public static final String PK_CURRTYPE = "pk_currtype";
	/**
	 * 财务核算账簿
	 */
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	/**
	 * 主键
	 */
	public static final String PK_GOFINANCING = "pk_gofinancing";
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
	 * 库存组织
	 */
	public static final String PK_STOCKSORT = "pk_stocksort";
	/**
	 * 交易类型pk
	 */
	public static final String PK_TRANSTYPE = "pk_transtype";
	/**
	 * 凭证主键
	 */
	public static final String PK_VOUCHER = "pk_voucher";
	/**
	 * 产品或对手方名称
	 */
	public static final String PRODUCTORCOUNTERPARTY = "productorcounterparty";
	/**
	 * 实际利率
	 */
	public static final String REALRATE = "realrate";
	/**
	 * 备注
	 */
	public static final String REMARK = "remark";
	/**
	 * 风险费
	 */
	public static final String RISK_FEE = "risk_fee";
	/**
	 * 流水号
	 */
	public static final String SERIALNUMBER = "serialnumber";
	/**
	 * 数据来源
	 */
	public static final String SOURCE = "source";
	/**
	 * 起息日
	 */
	public static final String STARTDATE = "startdate";
	/**
	 * 状态
	 */
	public static final String STATE = "state";
	/**
	 * 证券编码
	 */
	public static final String STOCKCODE = "stockcode";
	/**
	 * 税金
	 */
	public static final String TAXEXPENSE = "taxexpense";
	/**
	 * 交易日期
	 */
	public static final String TRADE_DATE = "trade_date";
	/**
	 * 交易类型
	 */
	public static final String TRANSTYPECODE = "transtypecode";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";
	/**
	 * 表头自定义项1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * 表头自定义项10
	 */
	public static final String VDEF10 = "vdef10";
	/**
	 * 表头自定义项2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * 表头自定义项3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * 表头自定义项4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * 表头自定义项5
	 */
	public static final String VDEF5 = "vdef5";
	/**
	 * 表头自定义项6
	 */
	public static final String VDEF6 = "vdef6";
	/**
	 * 表头自定义项7
	 */
	public static final String VDEF7 = "vdef7";
	/**
	 * 表头自定义项8
	 */
	public static final String VDEF8 = "vdef8";
	/**
	 * 表头自定义项9
	 */
	public static final String VDEF9 = "vdef9";
	/**
	 * 年计息天数
	 */
	public static final String YEARCALDAYS = "yearcaldays";

	/**
	 * 获取审批时间
	 * 
	 * @return 审批时间
	 */
	public UFDateTime getApprovedate() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.APPROVEDATE);
	}

	/**
	 * 设置审批时间
	 * 
	 * @param approvedate
	 *            审批时间
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.setAttributeValue(GoFinancingVO.APPROVEDATE, approvedate);
	}

	/**
	 * 获取审批人
	 * 
	 * @return 审批人
	 */
	public String getApprover() {
		return (String) this.getAttributeValue(GoFinancingVO.APPROVER);
	}

	/**
	 * 设置审批人
	 * 
	 * @param approver
	 *            审批人
	 */
	public void setApprover(String approver) {
		this.setAttributeValue(GoFinancingVO.APPROVER, approver);
	}

	/**
	 * 获取成交数量
	 * 
	 * @return 成交数量
	 */
	public UFDouble getBargin_num() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.BARGIN_NUM);
	}

	/**
	 * 设置成交数量
	 * 
	 * @param bargin_num
	 *            成交数量
	 */
	public void setBargin_num(UFDouble bargin_num) {
		this.setAttributeValue(GoFinancingVO.BARGIN_NUM, bargin_num);
	}

	/**
	 * 获取成交金额
	 * 
	 * @return 成交金额
	 */
	public UFDouble getBargin_sum() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.BARGIN_SUM);
	}

	/**
	 * 设置成交金额
	 * 
	 * @param bargin_sum
	 *            成交金额
	 */
	public void setBargin_sum(UFDouble bargin_sum) {
		this.setAttributeValue(GoFinancingVO.BARGIN_SUM, bargin_sum);
	}

	/**
	 * 获取制单人
	 * 
	 * @return 制单人
	 */
	public String getBillmaker() {
		return (String) this.getAttributeValue(GoFinancingVO.BILLMAKER);
	}

	/**
	 * 设置制单人
	 * 
	 * @param billmaker
	 *            制单人
	 */
	public void setBillmaker(String billmaker) {
		this.setAttributeValue(GoFinancingVO.BILLMAKER, billmaker);
	}

	/**
	 * 获取单据号
	 * 
	 * @return 单据号
	 */
	public String getBillno() {
		return (String) this.getAttributeValue(GoFinancingVO.BILLNO);
	}

	/**
	 * 设置单据号
	 * 
	 * @param billno
	 *            单据号
	 */
	public void setBillno(String billno) {
		this.setAttributeValue(GoFinancingVO.BILLNO, billno);
	}

	/**
	 * 获取单据类型
	 * 
	 * @return 单据类型
	 */
	public String getBilltypecode() {
		return (String) this.getAttributeValue(GoFinancingVO.BILLTYPECODE);
	}

	/**
	 * 设置单据类型
	 * 
	 * @param billtypecode
	 *            单据类型
	 */
	public void setBilltypecode(String billtypecode) {
		this.setAttributeValue(GoFinancingVO.BILLTYPECODE, billtypecode);
	}

	/**
	 * 获取合同号
	 * 
	 * @return 合同号
	 */
	public String getContractno() {
		return (String) this.getAttributeValue(GoFinancingVO.CONTRACTNO);
	}

	/**
	 * 设置合同号
	 * 
	 * @param contractno
	 *            合同号
	 */
	public void setContractno(String contractno) {
		this.setAttributeValue(GoFinancingVO.CONTRACTNO, contractno);
	}

	/**
	 * 获取合约利率
	 * 
	 * @return 合约利率
	 */
	public UFDouble getContractrate() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.CONTRACTRATE);
	}

	/**
	 * 设置合约利率
	 * 
	 * @param contractrate
	 *            合约利率
	 */
	public void setContractrate(UFDouble contractrate) {
		this.setAttributeValue(GoFinancingVO.CONTRACTRATE, contractrate);
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.CREATIONTIME);
	}

	/**
	 * 设置创建时间
	 * 
	 * @param creationtime
	 *            创建时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(GoFinancingVO.CREATIONTIME, creationtime);
	}

	/**
	 * 获取创建人
	 * 
	 * @return 创建人
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(GoFinancingVO.CREATOR);
	}

	/**
	 * 设置创建人
	 * 
	 * @param creator
	 *            创建人
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(GoFinancingVO.CREATOR, creator);
	}

	/**
	 * 获取到期日
	 * 
	 * @return 到期日
	 */
	public UFDate getEnddate() {
		return (UFDate) this.getAttributeValue(GoFinancingVO.ENDDATE);
	}

	/**
	 * 设置到期日
	 * 
	 * @param enddate
	 *            到期日
	 */
	public void setEnddate(UFDate enddate) {
		this.setAttributeValue(GoFinancingVO.ENDDATE, enddate);
	}

	/**
	 * 获取委托合同
	 * 
	 * @return 委托合同
	 */
	public String getEntrustcontract() {
		return (String) this.getAttributeValue(GoFinancingVO.ENTRUSTCONTRACT);
	}

	/**
	 * 设置委托合同
	 * 
	 * @param entrustcontract
	 *            委托合同
	 */
	public void setEntrustcontract(String entrustcontract) {
		this.setAttributeValue(GoFinancingVO.ENTRUSTCONTRACT, entrustcontract);
	}

	/**
	 * 获取实际收付
	 * 
	 * @return 实际收付
	 */
	public UFDouble getFact_sum() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.FACT_SUM);
	}

	/**
	 * 设置实际收付
	 * 
	 * @param fact_sum
	 *            实际收付
	 */
	public void setFact_sum(UFDouble fact_sum) {
		this.setAttributeValue(GoFinancingVO.FACT_SUM, fact_sum);
	}

	/**
	 * 获取融资类型
	 * 
	 * @return 融资类型
	 * @see String
	 */
	public Integer getFundtype() {
		return (Integer) this.getAttributeValue(GoFinancingVO.FUNDTYPE);
	}

	/**
	 * 设置融资类型
	 * 
	 * @param fundtype
	 *            融资类型
	 * @see String
	 */
	public void setFundtype(Integer fundtype) {
		this.setAttributeValue(GoFinancingVO.FUNDTYPE, fundtype);
	}

	/**
	 * 获取制单时间
	 * 
	 * @return 制单时间
	 */
	public UFDateTime getMakedate() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.MAKEDATE);
	}

	/**
	 * 设置制单时间
	 * 
	 * @param makedate
	 *            制单时间
	 */
	public void setMakedate(UFDateTime makedate) {
		this.setAttributeValue(GoFinancingVO.MAKEDATE, makedate);
	}

	/**
	 * 获取管理部门
	 * 
	 * @return 管理部门
	 */
	public String getManagedept() {
		return (String) this.getAttributeValue(GoFinancingVO.MANAGEDEPT);
	}

	/**
	 * 设置管理部门
	 * 
	 * @param managedept
	 *            管理部门
	 */
	public void setManagedept(String managedept) {
		this.setAttributeValue(GoFinancingVO.MANAGEDEPT, managedept);
	}

	/**
	 * 获取市场
	 * 
	 * @return 市场
	 * @see String
	 */
	public Integer getMarket() {
		return (Integer) this.getAttributeValue(GoFinancingVO.MARKET);
	}

	/**
	 * 设置市场
	 * 
	 * @param market
	 *            市场
	 * @see String
	 */
	public void setMarket(Integer market) {
		this.setAttributeValue(GoFinancingVO.MARKET, market);
	}

	/**
	 * 获取修改时间
	 * 
	 * @return 修改时间
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.MODIFIEDTIME);
	}

	/**
	 * 设置修改时间
	 * 
	 * @param modifiedtime
	 *            修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(GoFinancingVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * 获取修改人
	 * 
	 * @return 修改人
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(GoFinancingVO.MODIFIER);
	}

	/**
	 * 设置修改人
	 * 
	 * @param modifier
	 *            修改人
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(GoFinancingVO.MODIFIER, modifier);
	}

	/**
	 * 获取数值自定义项1
	 * 
	 * @return 数值自定义项1
	 */
	public UFDouble getNdef1() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF1);
	}

	/**
	 * 设置数值自定义项1
	 * 
	 * @param ndef1
	 *            数值自定义项1
	 */
	public void setNdef1(UFDouble ndef1) {
		this.setAttributeValue(GoFinancingVO.NDEF1, ndef1);
	}

	/**
	 * 获取数值自定义项10
	 * 
	 * @return 数值自定义项10
	 */
	public UFDouble getNdef10() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF10);
	}

	/**
	 * 设置数值自定义项10
	 * 
	 * @param ndef10
	 *            数值自定义项10
	 */
	public void setNdef10(UFDouble ndef10) {
		this.setAttributeValue(GoFinancingVO.NDEF10, ndef10);
	}

	/**
	 * 获取数值自定义项2
	 * 
	 * @return 数值自定义项2
	 */
	public UFDouble getNdef2() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF2);
	}

	/**
	 * 设置数值自定义项2
	 * 
	 * @param ndef2
	 *            数值自定义项2
	 */
	public void setNdef2(UFDouble ndef2) {
		this.setAttributeValue(GoFinancingVO.NDEF2, ndef2);
	}

	/**
	 * 获取数值自定义项3
	 * 
	 * @return 数值自定义项3
	 */
	public UFDouble getNdef3() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF3);
	}

	/**
	 * 设置数值自定义项3
	 * 
	 * @param ndef3
	 *            数值自定义项3
	 */
	public void setNdef3(UFDouble ndef3) {
		this.setAttributeValue(GoFinancingVO.NDEF3, ndef3);
	}

	/**
	 * 获取数值自定义项4
	 * 
	 * @return 数值自定义项4
	 */
	public UFDouble getNdef4() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF4);
	}

	/**
	 * 设置数值自定义项4
	 * 
	 * @param ndef4
	 *            数值自定义项4
	 */
	public void setNdef4(UFDouble ndef4) {
		this.setAttributeValue(GoFinancingVO.NDEF4, ndef4);
	}

	/**
	 * 获取数值自定义项5
	 * 
	 * @return 数值自定义项5
	 */
	public UFDouble getNdef5() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF5);
	}

	/**
	 * 设置数值自定义项5
	 * 
	 * @param ndef5
	 *            数值自定义项5
	 */
	public void setNdef5(UFDouble ndef5) {
		this.setAttributeValue(GoFinancingVO.NDEF5, ndef5);
	}

	/**
	 * 获取数值自定义项6
	 * 
	 * @return 数值自定义项6
	 */
	public UFDouble getNdef6() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF6);
	}

	/**
	 * 设置数值自定义项6
	 * 
	 * @param ndef6
	 *            数值自定义项6
	 */
	public void setNdef6(UFDouble ndef6) {
		this.setAttributeValue(GoFinancingVO.NDEF6, ndef6);
	}

	/**
	 * 获取数值自定义项7
	 * 
	 * @return 数值自定义项7
	 */
	public UFDouble getNdef7() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF7);
	}

	/**
	 * 设置数值自定义项7
	 * 
	 * @param ndef7
	 *            数值自定义项7
	 */
	public void setNdef7(UFDouble ndef7) {
		this.setAttributeValue(GoFinancingVO.NDEF7, ndef7);
	}

	/**
	 * 获取数值自定义项8
	 * 
	 * @return 数值自定义项8
	 */
	public UFDouble getNdef8() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF8);
	}

	/**
	 * 设置数值自定义项8
	 * 
	 * @param ndef8
	 *            数值自定义项8
	 */
	public void setNdef8(UFDouble ndef8) {
		this.setAttributeValue(GoFinancingVO.NDEF8, ndef8);
	}

	/**
	 * 获取数值自定义项9
	 * 
	 * @return 数值自定义项9
	 */
	public UFDouble getNdef9() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.NDEF9);
	}

	/**
	 * 设置数值自定义项9
	 * 
	 * @param ndef9
	 *            数值自定义项9
	 */
	public void setNdef9(UFDouble ndef9) {
		this.setAttributeValue(GoFinancingVO.NDEF9, ndef9);
	}

	/**
	 * 获取原委托合同
	 * 
	 * @return 原委托合同
	 */
	public String getOriginal_entrustcontract() {
		return (String) this
				.getAttributeValue(GoFinancingVO.ORIGINAL_ENTRUSTCONTRACT);
	}

	/**
	 * 设置原委托合同
	 * 
	 * @param original_entrustcontract
	 *            原委托合同
	 */
	public void setOriginal_entrustcontract(String original_entrustcontract) {
		this.setAttributeValue(GoFinancingVO.ORIGINAL_ENTRUSTCONTRACT,
				original_entrustcontract);
	}

	/**
	 * 获取其他费用
	 * 
	 * @return 其他费用
	 */
	public UFDouble getOther_fee() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.OTHER_FEE);
	}

	/**
	 * 设置其他费用
	 * 
	 * @param other_fee
	 *            其他费用
	 */
	public void setOther_fee(UFDouble other_fee) {
		this.setAttributeValue(GoFinancingVO.OTHER_FEE, other_fee);
	}

	/**
	 * 获取付息方式
	 * 
	 * @return 付息方式
	 * @see String
	 */
	public Integer getPaytype() {
		return (Integer) this.getAttributeValue(GoFinancingVO.PAYTYPE);
	}

	/**
	 * 设置付息方式
	 * 
	 * @param paytype
	 *            付息方式
	 * @see String
	 */
	public void setPaytype(Integer paytype) {
		this.setAttributeValue(GoFinancingVO.PAYTYPE, paytype);
	}

	/**
	 * 获取资产属性
	 * 
	 * @return 资产属性
	 */
	public String getPk_assetsprop() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_ASSETSPROP);
	}

	/**
	 * 设置资产属性
	 * 
	 * @param pk_assetsprop
	 *            资产属性
	 */
	public void setPk_assetsprop(String pk_assetsprop) {
		this.setAttributeValue(GoFinancingVO.PK_ASSETSPROP, pk_assetsprop);
	}

	/**
	 * 获取单据类型pk
	 * 
	 * @return 单据类型pk
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_BILLTYPE);
	}

	/**
	 * 设置单据类型pk
	 * 
	 * @param pk_billtype
	 *            单据类型pk
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(GoFinancingVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * 获取交易所
	 * 
	 * @return 交易所
	 */
	public String getPk_bourse() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_BOURSE);
	}

	/**
	 * 设置交易所
	 * 
	 * @param pk_bourse
	 *            交易所
	 */
	public void setPk_bourse(String pk_bourse) {
		this.setAttributeValue(GoFinancingVO.PK_BOURSE, pk_bourse);
	}

	/**
	 * 获取业务类型
	 * 
	 * @return 业务类型
	 */
	public String getPk_busitype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_BUSITYPE);
	}

	/**
	 * 设置业务类型
	 * 
	 * @param pk_busitype
	 *            业务类型
	 */
	public void setPk_busitype(String pk_busitype) {
		this.setAttributeValue(GoFinancingVO.PK_BUSITYPE, pk_busitype);
	}

	/**
	 * 获取资金账号
	 * 
	 * @return 资金账号
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_CAPACCOUNT);
	}

	/**
	 * 设置资金账号
	 * 
	 * @param pk_capaccount
	 *            资金账号
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(GoFinancingVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * 获取往来单位
	 * 
	 * @return 往来单位
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_CLIENT);
	}

	/**
	 * 设置往来单位
	 * 
	 * @param pk_client
	 *            往来单位
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(GoFinancingVO.PK_CLIENT, pk_client);
	}

	/**
	 * 获取币种
	 * 
	 * @return 币种
	 */
	public String getPk_currtype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_CURRTYPE);
	}

	/**
	 * 设置币种
	 * 
	 * @param pk_currtype
	 *            币种
	 */
	public void setPk_currtype(String pk_currtype) {
		this.setAttributeValue(GoFinancingVO.PK_CURRTYPE, pk_currtype);
	}

	/**
	 * 获取财务核算账簿
	 * 
	 * @return 财务核算账簿
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_GLORGBOOK);
	}

	/**
	 * 设置财务核算账簿
	 * 
	 * @param pk_glorgbook
	 *            财务核算账簿
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(GoFinancingVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public String getPk_gofinancing() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_GOFINANCING);
	}

	/**
	 * 设置主键
	 * 
	 * @param pk_gofinancing
	 *            主键
	 */
	public void setPk_gofinancing(String pk_gofinancing) {
		this.setAttributeValue(GoFinancingVO.PK_GOFINANCING, pk_gofinancing);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(GoFinancingVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取分户地点
	 * 
	 * @return 分户地点
	 */
	public String getPk_operatesite() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_OPERATESITE);
	}

	/**
	 * 设置分户地点
	 * 
	 * @param pk_operatesite
	 *            分户地点
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(GoFinancingVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(GoFinancingVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(GoFinancingVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取股东账号
	 * 
	 * @return 股东账号
	 */
	public String getPk_partnaccount() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_PARTNACCOUNT);
	}

	/**
	 * 设置股东账号
	 * 
	 * @param pk_partnaccount
	 *            股东账号
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(GoFinancingVO.PK_PARTNACCOUNT, pk_partnaccount);
	}

	/**
	 * 获取交易证券
	 * 
	 * @return 交易证券
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_SECURITIES);
	}

	/**
	 * 设置交易证券
	 * 
	 * @param pk_securities
	 *            交易证券
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(GoFinancingVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * 获取业务小组
	 * 
	 * @return 业务小组
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_SELFSGROUP);
	}

	/**
	 * 设置业务小组
	 * 
	 * @param pk_selfsgroup
	 *            业务小组
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(GoFinancingVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * 获取库存组织
	 * 
	 * @return 库存组织
	 */
	public String getPk_stocksort() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_STOCKSORT);
	}

	/**
	 * 设置库存组织
	 * 
	 * @param pk_stocksort
	 *            库存组织
	 */
	public void setPk_stocksort(String pk_stocksort) {
		this.setAttributeValue(GoFinancingVO.PK_STOCKSORT, pk_stocksort);
	}

	/**
	 * 获取交易类型pk
	 * 
	 * @return 交易类型pk
	 */
	public String getPk_transtype() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_TRANSTYPE);
	}

	/**
	 * 设置交易类型pk
	 * 
	 * @param pk_transtype
	 *            交易类型pk
	 */
	public void setPk_transtype(String pk_transtype) {
		this.setAttributeValue(GoFinancingVO.PK_TRANSTYPE, pk_transtype);
	}

	/**
	 * 获取凭证主键
	 * 
	 * @return 凭证主键
	 */
	public String getPk_voucher() {
		return (String) this.getAttributeValue(GoFinancingVO.PK_VOUCHER);
	}

	/**
	 * 设置凭证主键
	 * 
	 * @param pk_voucher
	 *            凭证主键
	 */
	public void setPk_voucher(String pk_voucher) {
		this.setAttributeValue(GoFinancingVO.PK_VOUCHER, pk_voucher);
	}

	/**
	 * 获取产品或对手方名称
	 * 
	 * @return 产品或对手方名称
	 */
	public String getProductorcounterparty() {
		return (String) this
				.getAttributeValue(GoFinancingVO.PRODUCTORCOUNTERPARTY);
	}

	/**
	 * 设置产品或对手方名称
	 * 
	 * @param productorcounterparty
	 *            产品或对手方名称
	 */
	public void setProductorcounterparty(String productorcounterparty) {
		this.setAttributeValue(GoFinancingVO.PRODUCTORCOUNTERPARTY,
				productorcounterparty);
	}

	/**
	 * 获取实际利率
	 * 
	 * @return 实际利率
	 */
	public UFDouble getRealrate() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.REALRATE);
	}

	/**
	 * 设置实际利率
	 * 
	 * @param realrate
	 *            实际利率
	 */
	public void setRealrate(UFDouble realrate) {
		this.setAttributeValue(GoFinancingVO.REALRATE, realrate);
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark() {
		return (String) this.getAttributeValue(GoFinancingVO.REMARK);
	}

	/**
	 * 设置备注
	 * 
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.setAttributeValue(GoFinancingVO.REMARK, remark);
	}

	/**
	 * 获取风险费
	 * 
	 * @return 风险费
	 */
	public UFDouble getRisk_fee() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.RISK_FEE);
	}

	/**
	 * 设置风险费
	 * 
	 * @param risk_fee
	 *            风险费
	 */
	public void setRisk_fee(UFDouble risk_fee) {
		this.setAttributeValue(GoFinancingVO.RISK_FEE, risk_fee);
	}

	/**
	 * 获取流水号
	 * 
	 * @return 流水号
	 */
	public String getSerialnumber() {
		return (String) this.getAttributeValue(GoFinancingVO.SERIALNUMBER);
	}

	/**
	 * 设置流水号
	 * 
	 * @param serialnumber
	 *            流水号
	 */
	public void setSerialnumber(String serialnumber) {
		this.setAttributeValue(GoFinancingVO.SERIALNUMBER, serialnumber);
	}

	/**
	 * 获取数据来源
	 * 
	 * @return 数据来源
	 */
	public String getSource() {
		return (String) this.getAttributeValue(GoFinancingVO.SOURCE);
	}

	/**
	 * 设置数据来源
	 * 
	 * @param source
	 *            数据来源
	 */
	public void setSource(String source) {
		this.setAttributeValue(GoFinancingVO.SOURCE, source);
	}

	/**
	 * 获取起息日
	 * 
	 * @return 起息日
	 */
	public UFDate getStartdate() {
		return (UFDate) this.getAttributeValue(GoFinancingVO.STARTDATE);
	}

	/**
	 * 设置起息日
	 * 
	 * @param startdate
	 *            起息日
	 */
	public void setStartdate(UFDate startdate) {
		this.setAttributeValue(GoFinancingVO.STARTDATE, startdate);
	}

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 * @see String
	 */
	public Integer getState() {
		return (Integer) this.getAttributeValue(GoFinancingVO.STATE);
	}

	/**
	 * 设置状态
	 * 
	 * @param state
	 *            状态
	 * @see String
	 */
	public void setState(Integer state) {
		this.setAttributeValue(GoFinancingVO.STATE, state);
	}

	/**
	 * 获取证券编码
	 * 
	 * @return 证券编码
	 */
	public String getStockcode() {
		return (String) this.getAttributeValue(GoFinancingVO.STOCKCODE);
	}

	/**
	 * 设置证券编码
	 * 
	 * @param stockcode
	 *            证券编码
	 */
	public void setStockcode(String stockcode) {
		this.setAttributeValue(GoFinancingVO.STOCKCODE, stockcode);
	}

	/**
	 * 获取税金
	 * 
	 * @return 税金
	 */
	public UFDouble getTaxexpense() {
		return (UFDouble) this.getAttributeValue(GoFinancingVO.TAXEXPENSE);
	}

	/**
	 * 设置税金
	 * 
	 * @param taxexpense
	 *            税金
	 */
	public void setTaxexpense(UFDouble taxexpense) {
		this.setAttributeValue(GoFinancingVO.TAXEXPENSE, taxexpense);
	}

	/**
	 * 获取交易日期
	 * 
	 * @return 交易日期
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(GoFinancingVO.TRADE_DATE);
	}

	/**
	 * 设置交易日期
	 * 
	 * @param trade_date
	 *            交易日期
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(GoFinancingVO.TRADE_DATE, trade_date);
	}

	/**
	 * 获取交易类型
	 * 
	 * @return 交易类型
	 */
	public String getTranstypecode() {
		return (String) this.getAttributeValue(GoFinancingVO.TRANSTYPECODE);
	}

	/**
	 * 设置交易类型
	 * 
	 * @param transtypecode
	 *            交易类型
	 */
	public void setTranstypecode(String transtypecode) {
		this.setAttributeValue(GoFinancingVO.TRANSTYPECODE, transtypecode);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(GoFinancingVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(GoFinancingVO.TS, ts);
	}

	/**
	 * 获取表头自定义项1
	 * 
	 * @return 表头自定义项1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF1);
	}

	/**
	 * 设置表头自定义项1
	 * 
	 * @param vdef1
	 *            表头自定义项1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(GoFinancingVO.VDEF1, vdef1);
	}

	/**
	 * 获取表头自定义项10
	 * 
	 * @return 表头自定义项10
	 */
	public String getVdef10() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF10);
	}

	/**
	 * 设置表头自定义项10
	 * 
	 * @param vdef10
	 *            表头自定义项10
	 */
	public void setVdef10(String vdef10) {
		this.setAttributeValue(GoFinancingVO.VDEF10, vdef10);
	}

	/**
	 * 获取表头自定义项2
	 * 
	 * @return 表头自定义项2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF2);
	}

	/**
	 * 设置表头自定义项2
	 * 
	 * @param vdef2
	 *            表头自定义项2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(GoFinancingVO.VDEF2, vdef2);
	}

	/**
	 * 获取表头自定义项3
	 * 
	 * @return 表头自定义项3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF3);
	}

	/**
	 * 设置表头自定义项3
	 * 
	 * @param vdef3
	 *            表头自定义项3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(GoFinancingVO.VDEF3, vdef3);
	}

	/**
	 * 获取表头自定义项4
	 * 
	 * @return 表头自定义项4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF4);
	}

	/**
	 * 设置表头自定义项4
	 * 
	 * @param vdef4
	 *            表头自定义项4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(GoFinancingVO.VDEF4, vdef4);
	}

	/**
	 * 获取表头自定义项5
	 * 
	 * @return 表头自定义项5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF5);
	}

	/**
	 * 设置表头自定义项5
	 * 
	 * @param vdef5
	 *            表头自定义项5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(GoFinancingVO.VDEF5, vdef5);
	}

	/**
	 * 获取表头自定义项6
	 * 
	 * @return 表头自定义项6
	 */
	public String getVdef6() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF6);
	}

	/**
	 * 设置表头自定义项6
	 * 
	 * @param vdef6
	 *            表头自定义项6
	 */
	public void setVdef6(String vdef6) {
		this.setAttributeValue(GoFinancingVO.VDEF6, vdef6);
	}

	/**
	 * 获取表头自定义项7
	 * 
	 * @return 表头自定义项7
	 */
	public String getVdef7() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF7);
	}

	/**
	 * 设置表头自定义项7
	 * 
	 * @param vdef7
	 *            表头自定义项7
	 */
	public void setVdef7(String vdef7) {
		this.setAttributeValue(GoFinancingVO.VDEF7, vdef7);
	}

	/**
	 * 获取表头自定义项8
	 * 
	 * @return 表头自定义项8
	 */
	public String getVdef8() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF8);
	}

	/**
	 * 设置表头自定义项8
	 * 
	 * @param vdef8
	 *            表头自定义项8
	 */
	public void setVdef8(String vdef8) {
		this.setAttributeValue(GoFinancingVO.VDEF8, vdef8);
	}

	/**
	 * 获取表头自定义项9
	 * 
	 * @return 表头自定义项9
	 */
	public String getVdef9() {
		return (String) this.getAttributeValue(GoFinancingVO.VDEF9);
	}

	/**
	 * 设置表头自定义项9
	 * 
	 * @param vdef9
	 *            表头自定义项9
	 */
	public void setVdef9(String vdef9) {
		this.setAttributeValue(GoFinancingVO.VDEF9, vdef9);
	}

	/**
	 * 获取年计息天数
	 * 
	 * @return 年计息天数
	 */
	public Integer getYearcaldays() {
		return (Integer) this.getAttributeValue(GoFinancingVO.YEARCALDAYS);
	}

	/**
	 * 设置年计息天数
	 * 
	 * @param yearcaldays
	 *            年计息天数
	 */
	public void setYearcaldays(Integer yearcaldays) {
		this.setAttributeValue(GoFinancingVO.YEARCALDAYS, yearcaldays);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_fund.GoFinancingVO");
	}
}