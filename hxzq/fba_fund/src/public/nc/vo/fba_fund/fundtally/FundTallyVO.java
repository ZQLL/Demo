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
	 * 限售开始日期
	 */
	public static final String BEGIN_DATE = "begin_date";
	/**
	 * 首次结算日
	 */
	public static final String BEGINDATE = "begindate";
	/**
	 * 计提开始日
	 */
	public static final String BEGINDISTILL_DATE = "begindistill_date";
	/**
	 * 合同号
	 */
	public static final String CONTRACTNO = "contractno";
	/**
	 * 面额
	 */
	public static final String DENOMINATION = "denomination";
	/**
	 * 限售结束日期
	 */
	public static final String END_DATE = "end_date";
	/**
	 * 到期结算日
	 */
	public static final String ENDDATE = "enddate";
	/**
	 * 执行票面利率
	 */
	public static final String EXCUTEFACERATE = "excutefacerate";
	/**
	 * 融资类型
	 */
	public static final String FUNDTYPE = "fundtype";
	/**
	 * 已计提利息
	 */
	public static final String HAD_INTEREST = "had_interest";
	/**
	 * 计提结束日
	 */
	public static final String INDISTILL_DATE = "indistill_date";
	/**
	 * 是否到期
	 */
	public static final String ISDQ = "isdq";
	/**
	 * 发行票面利率
	 */
	public static final String ISSUEFACERATE = "issuefacerate";
	/**
	 * 管理部门
	 */
	public static final String MANAGEDEPT = "managedept";
	/**
	 * 市场
	 */
	public static final String MARKET = "market";
	/**
	 * 自定义项6
	 */
	public static final String NDEF1 = "ndef1";
	/**
	 * 自定义项7
	 */
	public static final String NDEF2 = "ndef2";
	/**
	 * 自定义项8
	 */
	public static final String NDEF3 = "ndef3";
	/**
	 * 自定义项9
	 */
	public static final String NDEF4 = "ndef4";
	/**
	 * 自定义项10
	 */
	public static final String NDEF5 = "ndef5";
	/**
	 * 业务编码
	 */
	public static final String OPERCODE = "opercode";
	/**
	 * 操作日期
	 */
	public static final String OPERDATE = "operdate";
	/**
	 * 索引
	 */
	public static final String OPERINDEX = "operindex";
	/**
	 * 付息方式
	 */
	public static final String PAYTYPE = "paytype";
	/**
	 * 单据主键
	 */
	public static final String PK_BILL = "pk_bill";
	/**
	 * 单据类型
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
	 * 部门
	 */
	public static final String PK_DEPT = "pk_dept";
	/**
	 * 主键
	 */
	public static final String PK_FUNDTALLY = "pk_fundtally";
	/**
	 * 主体账簿
	 */
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	/**
	 * 集团
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * 回购品种
	 */
	public static final String PK_HGTYPE = "pk_hgtype";
	/**
	 * 业务操作单据
	 */
	public static final String PK_OPBILL = "pk_opbill";
	/**
	 * 分户地点
	 */
	public static final String PK_OPERATESITE = "pk_operatesite";
	/**
	 * 业务操作单据类型
	 */
	public static final String PK_OPTYPE = "pk_optype";
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
	 * 业务小组
	 */
	public static final String PK_SELFSGROUP = "pk_selfsgroup";
	/**
	 * 产品或对手方名称
	 */
	public static final String PRODUCTORCOUNTERPARTY = "productorcounterparty";
	/**
	 * 实际利率
	 */
	public static final String REALRATE = "realrate";
	/**
	 * 本次计提利息
	 */
	public static final String THIS_INTEREST = "this_interest";
	/**
	 * 期限
	 */
	public static final String TIMELIMIT = "timelimit";
	/**
	 * 交易日期
	 */
	public static final String TRADE_DATE = "trade_date";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";
	/**
	 * 自定义项1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * 自定义项2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * 自定义项3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * 自定义项4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * 自定义项5
	 */
	public static final String VDEF5 = "vdef5";
	/**
	 * 年计息天数
	 */
	public static final String YEARCALDAYS = "yearcaldays";

	/**
	 * 获取限售开始日期
	 * 
	 * @return 限售开始日期
	 */
	public UFLiteralDate getBegin_date() {
		return (UFLiteralDate) this.getAttributeValue(FundTallyVO.BEGIN_DATE);
	}

	/**
	 * 设置限售开始日期
	 * 
	 * @param begin_date
	 *            限售开始日期
	 */
	public void setBegin_date(UFLiteralDate begin_date) {
		this.setAttributeValue(FundTallyVO.BEGIN_DATE, begin_date);
	}

	/**
	 * 获取首次结算日
	 * 
	 * @return 首次结算日
	 */
	public UFDate getBegindate() {
		return (UFDate) this.getAttributeValue(FundTallyVO.BEGINDATE);
	}

	/**
	 * 设置首次结算日
	 * 
	 * @param begindate
	 *            首次结算日
	 */
	public void setBegindate(UFDate begindate) {
		this.setAttributeValue(FundTallyVO.BEGINDATE, begindate);
	}

	/**
	 * 获取计提开始日
	 * 
	 * @return 计提开始日
	 */
	public UFDate getBegindistill_date() {
		return (UFDate) this.getAttributeValue(FundTallyVO.BEGINDISTILL_DATE);
	}

	/**
	 * 设置计提开始日
	 * 
	 * @param begindistill_date
	 *            计提开始日
	 */
	public void setBegindistill_date(UFDate begindistill_date) {
		this.setAttributeValue(FundTallyVO.BEGINDISTILL_DATE, begindistill_date);
	}

	/**
	 * 获取合同号
	 * 
	 * @return 合同号
	 */
	public String getContractno() {
		return (String) this.getAttributeValue(FundTallyVO.CONTRACTNO);
	}

	/**
	 * 设置合同号
	 * 
	 * @param contractno
	 *            合同号
	 */
	public void setContractno(String contractno) {
		this.setAttributeValue(FundTallyVO.CONTRACTNO, contractno);
	}

	/**
	 * 获取面额
	 * 
	 * @return 面额
	 */
	public UFDouble getDenomination() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.DENOMINATION);
	}

	/**
	 * 设置面额
	 * 
	 * @param denomination
	 *            面额
	 */
	public void setDenomination(UFDouble denomination) {
		this.setAttributeValue(FundTallyVO.DENOMINATION, denomination);
	}

	/**
	 * 获取限售结束日期
	 * 
	 * @return 限售结束日期
	 */
	public UFLiteralDate getEnd_date() {
		return (UFLiteralDate) this.getAttributeValue(FundTallyVO.END_DATE);
	}

	/**
	 * 设置限售结束日期
	 * 
	 * @param end_date
	 *            限售结束日期
	 */
	public void setEnd_date(UFLiteralDate end_date) {
		this.setAttributeValue(FundTallyVO.END_DATE, end_date);
	}

	/**
	 * 获取到期结算日
	 * 
	 * @return 到期结算日
	 */
	public UFDate getEnddate() {
		return (UFDate) this.getAttributeValue(FundTallyVO.ENDDATE);
	}

	/**
	 * 设置到期结算日
	 * 
	 * @param enddate
	 *            到期结算日
	 */
	public void setEnddate(UFDate enddate) {
		this.setAttributeValue(FundTallyVO.ENDDATE, enddate);
	}

	/**
	 * 获取执行票面利率
	 * 
	 * @return 执行票面利率
	 */
	public UFDouble getExcutefacerate() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.EXCUTEFACERATE);
	}

	/**
	 * 设置执行票面利率
	 * 
	 * @param excutefacerate
	 *            执行票面利率
	 */
	public void setExcutefacerate(UFDouble excutefacerate) {
		this.setAttributeValue(FundTallyVO.EXCUTEFACERATE, excutefacerate);
	}

	/**
	 * 获取融资类型
	 * 
	 * @return 融资类型
	 * @see String
	 */
	public Integer getFundtype() {
		return (Integer) this.getAttributeValue(FundTallyVO.FUNDTYPE);
	}

	/**
	 * 设置融资类型
	 * 
	 * @param fundtype
	 *            融资类型
	 * @see String
	 */
	public void setFundtype(Integer fundtype) {
		this.setAttributeValue(FundTallyVO.FUNDTYPE, fundtype);
	}

	/**
	 * 获取已计提利息
	 * 
	 * @return 已计提利息
	 */
	public UFDouble getHad_interest() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.HAD_INTEREST);
	}

	/**
	 * 设置已计提利息
	 * 
	 * @param had_interest
	 *            已计提利息
	 */
	public void setHad_interest(UFDouble had_interest) {
		this.setAttributeValue(FundTallyVO.HAD_INTEREST, had_interest);
	}

	/**
	 * 获取计提结束日
	 * 
	 * @return 计提结束日
	 */
	public UFDate getIndistill_date() {
		return (UFDate) this.getAttributeValue(FundTallyVO.INDISTILL_DATE);
	}

	/**
	 * 设置计提结束日
	 * 
	 * @param indistill_date
	 *            计提结束日
	 */
	public void setIndistill_date(UFDate indistill_date) {
		this.setAttributeValue(FundTallyVO.INDISTILL_DATE, indistill_date);
	}

	/**
	 * 获取是否到期
	 * 
	 * @return 是否到期
	 */
	public UFBoolean getIsdq() {
		return (UFBoolean) this.getAttributeValue(FundTallyVO.ISDQ);
	}

	/**
	 * 设置是否到期
	 * 
	 * @param isdq
	 *            是否到期
	 */
	public void setIsdq(UFBoolean isdq) {
		this.setAttributeValue(FundTallyVO.ISDQ, isdq);
	}

	/**
	 * 获取发行票面利率
	 * 
	 * @return 发行票面利率
	 */
	public UFDouble getIssuefacerate() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.ISSUEFACERATE);
	}

	/**
	 * 设置发行票面利率
	 * 
	 * @param issuefacerate
	 *            发行票面利率
	 */
	public void setIssuefacerate(UFDouble issuefacerate) {
		this.setAttributeValue(FundTallyVO.ISSUEFACERATE, issuefacerate);
	}

	/**
	 * 获取管理部门
	 * 
	 * @return 管理部门
	 */
	public String getManagedept() {
		return (String) this.getAttributeValue(FundTallyVO.MANAGEDEPT);
	}

	/**
	 * 设置管理部门
	 * 
	 * @param managedept
	 *            管理部门
	 */
	public void setManagedept(String managedept) {
		this.setAttributeValue(FundTallyVO.MANAGEDEPT, managedept);
	}

	/**
	 * 获取市场
	 * 
	 * @return 市场
	 */
	public Integer getMarket() {
		return (Integer) this.getAttributeValue(FundTallyVO.MARKET);
	}

	/**
	 * 设置市场
	 * 
	 * @param market
	 *            市场
	 */
	public void setMarket(Integer market) {
		this.setAttributeValue(FundTallyVO.MARKET, market);
	}

	/**
	 * 获取自定义项6
	 * 
	 * @return 自定义项6
	 */
	public UFDouble getNdef1() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF1);
	}

	/**
	 * 设置自定义项6
	 * 
	 * @param ndef1
	 *            自定义项6
	 */
	public void setNdef1(UFDouble ndef1) {
		this.setAttributeValue(FundTallyVO.NDEF1, ndef1);
	}

	/**
	 * 获取自定义项7
	 * 
	 * @return 自定义项7
	 */
	public UFDouble getNdef2() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF2);
	}

	/**
	 * 设置自定义项7
	 * 
	 * @param ndef2
	 *            自定义项7
	 */
	public void setNdef2(UFDouble ndef2) {
		this.setAttributeValue(FundTallyVO.NDEF2, ndef2);
	}

	/**
	 * 获取自定义项8
	 * 
	 * @return 自定义项8
	 */
	public UFDouble getNdef3() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF3);
	}

	/**
	 * 设置自定义项8
	 * 
	 * @param ndef3
	 *            自定义项8
	 */
	public void setNdef3(UFDouble ndef3) {
		this.setAttributeValue(FundTallyVO.NDEF3, ndef3);
	}

	/**
	 * 获取自定义项9
	 * 
	 * @return 自定义项9
	 */
	public UFDouble getNdef4() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF4);
	}

	/**
	 * 设置自定义项9
	 * 
	 * @param ndef4
	 *            自定义项9
	 */
	public void setNdef4(UFDouble ndef4) {
		this.setAttributeValue(FundTallyVO.NDEF4, ndef4);
	}

	/**
	 * 获取自定义项10
	 * 
	 * @return 自定义项10
	 */
	public UFDouble getNdef5() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.NDEF5);
	}

	/**
	 * 设置自定义项10
	 * 
	 * @param ndef5
	 *            自定义项10
	 */
	public void setNdef5(UFDouble ndef5) {
		this.setAttributeValue(FundTallyVO.NDEF5, ndef5);
	}

	/**
	 * 获取业务编码
	 * 
	 * @return 业务编码
	 */
	public String getOpercode() {
		return (String) this.getAttributeValue(FundTallyVO.OPERCODE);
	}

	/**
	 * 设置业务编码
	 * 
	 * @param opercode
	 *            业务编码
	 */
	public void setOpercode(String opercode) {
		this.setAttributeValue(FundTallyVO.OPERCODE, opercode);
	}

	/**
	 * 获取操作日期
	 * 
	 * @return 操作日期
	 */
	public UFDate getOperdate() {
		return (UFDate) this.getAttributeValue(FundTallyVO.OPERDATE);
	}

	/**
	 * 设置操作日期
	 * 
	 * @param operdate
	 *            操作日期
	 */
	public void setOperdate(UFDate operdate) {
		this.setAttributeValue(FundTallyVO.OPERDATE, operdate);
	}

	/**
	 * 获取索引
	 * 
	 * @return 索引
	 */
	public Integer getOperindex() {
		return (Integer) this.getAttributeValue(FundTallyVO.OPERINDEX);
	}

	/**
	 * 设置索引
	 * 
	 * @param operindex
	 *            索引
	 */
	public void setOperindex(Integer operindex) {
		this.setAttributeValue(FundTallyVO.OPERINDEX, operindex);
	}

	/**
	 * 获取付息方式
	 * 
	 * @return 付息方式
	 */
	public Integer getPaytype() {
		return (Integer) this.getAttributeValue(FundTallyVO.PAYTYPE);
	}

	/**
	 * 设置付息方式
	 * 
	 * @param paytype
	 *            付息方式
	 */
	public void setPaytype(Integer paytype) {
		this.setAttributeValue(FundTallyVO.PAYTYPE, paytype);
	}

	/**
	 * 获取单据主键
	 * 
	 * @return 单据主键
	 */
	public String getPk_bill() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BILL);
	}

	/**
	 * 设置单据主键
	 * 
	 * @param pk_bill
	 *            单据主键
	 */
	public void setPk_bill(String pk_bill) {
		this.setAttributeValue(FundTallyVO.PK_BILL, pk_bill);
	}

	/**
	 * 获取单据类型
	 * 
	 * @return 单据类型
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BILLTYPE);
	}

	/**
	 * 设置单据类型
	 * 
	 * @param pk_billtype
	 *            单据类型
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(FundTallyVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * 获取交易所
	 * 
	 * @return 交易所
	 */
	public String getPk_bourse() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BOURSE);
	}

	/**
	 * 设置交易所
	 * 
	 * @param pk_bourse
	 *            交易所
	 */
	public void setPk_bourse(String pk_bourse) {
		this.setAttributeValue(FundTallyVO.PK_BOURSE, pk_bourse);
	}

	/**
	 * 获取业务类型
	 * 
	 * @return 业务类型
	 */
	public String getPk_busitype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_BUSITYPE);
	}

	/**
	 * 设置业务类型
	 * 
	 * @param pk_busitype
	 *            业务类型
	 */
	public void setPk_busitype(String pk_busitype) {
		this.setAttributeValue(FundTallyVO.PK_BUSITYPE, pk_busitype);
	}

	/**
	 * 获取资金账号
	 * 
	 * @return 资金账号
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(FundTallyVO.PK_CAPACCOUNT);
	}

	/**
	 * 设置资金账号
	 * 
	 * @param pk_capaccount
	 *            资金账号
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(FundTallyVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * 获取往来单位
	 * 
	 * @return 往来单位
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(FundTallyVO.PK_CLIENT);
	}

	/**
	 * 设置往来单位
	 * 
	 * @param pk_client
	 *            往来单位
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(FundTallyVO.PK_CLIENT, pk_client);
	}

	/**
	 * 获取币种
	 * 
	 * @return 币种
	 */
	public String getPk_currtype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_CURRTYPE);
	}

	/**
	 * 设置币种
	 * 
	 * @param pk_currtype
	 *            币种
	 */
	public void setPk_currtype(String pk_currtype) {
		this.setAttributeValue(FundTallyVO.PK_CURRTYPE, pk_currtype);
	}

	/**
	 * 获取部门
	 * 
	 * @return 部门
	 */
	public String getPk_dept() {
		return (String) this.getAttributeValue(FundTallyVO.PK_DEPT);
	}

	/**
	 * 设置部门
	 * 
	 * @param pk_dept
	 *            部门
	 */
	public void setPk_dept(String pk_dept) {
		this.setAttributeValue(FundTallyVO.PK_DEPT, pk_dept);
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public String getPk_fundtally() {
		return (String) this.getAttributeValue(FundTallyVO.PK_FUNDTALLY);
	}

	/**
	 * 设置主键
	 * 
	 * @param pk_fundtally
	 *            主键
	 */
	public void setPk_fundtally(String pk_fundtally) {
		this.setAttributeValue(FundTallyVO.PK_FUNDTALLY, pk_fundtally);
	}

	/**
	 * 获取主体账簿
	 * 
	 * @return 主体账簿
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(FundTallyVO.PK_GLORGBOOK);
	}

	/**
	 * 设置主体账簿
	 * 
	 * @param pk_glorgbook
	 *            主体账簿
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(FundTallyVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(FundTallyVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(FundTallyVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取回购品种
	 * 
	 * @return 回购品种
	 */
	public String getPk_hgtype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_HGTYPE);
	}

	/**
	 * 设置回购品种
	 * 
	 * @param pk_hgtype
	 *            回购品种
	 */
	public void setPk_hgtype(String pk_hgtype) {
		this.setAttributeValue(FundTallyVO.PK_HGTYPE, pk_hgtype);
	}

	/**
	 * 获取业务操作单据
	 * 
	 * @return 业务操作单据
	 */
	public String getPk_opbill() {
		return (String) this.getAttributeValue(FundTallyVO.PK_OPBILL);
	}

	/**
	 * 设置业务操作单据
	 * 
	 * @param pk_opbill
	 *            业务操作单据
	 */
	public void setPk_opbill(String pk_opbill) {
		this.setAttributeValue(FundTallyVO.PK_OPBILL, pk_opbill);
	}

	/**
	 * 获取分户地点
	 * 
	 * @return 分户地点
	 */
	public String getPk_operatesite() {
		return (String) this.getAttributeValue(FundTallyVO.PK_OPERATESITE);
	}

	/**
	 * 设置分户地点
	 * 
	 * @param pk_operatesite
	 *            分户地点
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(FundTallyVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * 获取业务操作单据类型
	 * 
	 * @return 业务操作单据类型
	 */
	public String getPk_optype() {
		return (String) this.getAttributeValue(FundTallyVO.PK_OPTYPE);
	}

	/**
	 * 设置业务操作单据类型
	 * 
	 * @param pk_optype
	 *            业务操作单据类型
	 */
	public void setPk_optype(String pk_optype) {
		this.setAttributeValue(FundTallyVO.PK_OPTYPE, pk_optype);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(FundTallyVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(FundTallyVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(FundTallyVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(FundTallyVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取股东账号
	 * 
	 * @return 股东账号
	 */
	public String getPk_partnaccount() {
		return (String) this.getAttributeValue(FundTallyVO.PK_PARTNACCOUNT);
	}

	/**
	 * 设置股东账号
	 * 
	 * @param pk_partnaccount
	 *            股东账号
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(FundTallyVO.PK_PARTNACCOUNT, pk_partnaccount);
	}

	/**
	 * 获取业务小组
	 * 
	 * @return 业务小组
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(FundTallyVO.PK_SELFSGROUP);
	}

	/**
	 * 设置业务小组
	 * 
	 * @param pk_selfsgroup
	 *            业务小组
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(FundTallyVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * 获取产品或对手方名称
	 * 
	 * @return 产品或对手方名称
	 */
	public String getProductorcounterparty() {
		return (String) this
				.getAttributeValue(FundTallyVO.PRODUCTORCOUNTERPARTY);
	}

	/**
	 * 设置产品或对手方名称
	 * 
	 * @param productorcounterparty
	 *            产品或对手方名称
	 */
	public void setProductorcounterparty(String productorcounterparty) {
		this.setAttributeValue(FundTallyVO.PRODUCTORCOUNTERPARTY,
				productorcounterparty);
	}

	/**
	 * 获取实际利率
	 * 
	 * @return 实际利率
	 */
	public UFDouble getRealrate() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.REALRATE);
	}

	/**
	 * 设置实际利率
	 * 
	 * @param realrate
	 *            实际利率
	 */
	public void setRealrate(UFDouble realrate) {
		this.setAttributeValue(FundTallyVO.REALRATE, realrate);
	}

	/**
	 * 获取本次计提利息
	 * 
	 * @return 本次计提利息
	 */
	public UFDouble getThis_interest() {
		return (UFDouble) this.getAttributeValue(FundTallyVO.THIS_INTEREST);
	}

	/**
	 * 设置本次计提利息
	 * 
	 * @param this_interest
	 *            本次计提利息
	 */
	public void setThis_interest(UFDouble this_interest) {
		this.setAttributeValue(FundTallyVO.THIS_INTEREST, this_interest);
	}

	/**
	 * 获取期限
	 * 
	 * @return 期限
	 */
	public Integer getTimelimit() {
		return (Integer) this.getAttributeValue(FundTallyVO.TIMELIMIT);
	}

	/**
	 * 设置期限
	 * 
	 * @param timelimit
	 *            期限
	 */
	public void setTimelimit(Integer timelimit) {
		this.setAttributeValue(FundTallyVO.TIMELIMIT, timelimit);
	}

	/**
	 * 获取交易日期
	 * 
	 * @return 交易日期
	 */
	public UFDate getTrade_date() {
		return (UFDate) this.getAttributeValue(FundTallyVO.TRADE_DATE);
	}

	/**
	 * 设置交易日期
	 * 
	 * @param trade_date
	 *            交易日期
	 */
	public void setTrade_date(UFDate trade_date) {
		this.setAttributeValue(FundTallyVO.TRADE_DATE, trade_date);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FundTallyVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(FundTallyVO.TS, ts);
	}

	/**
	 * 获取自定义项1
	 * 
	 * @return 自定义项1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF1);
	}

	/**
	 * 设置自定义项1
	 * 
	 * @param vdef1
	 *            自定义项1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(FundTallyVO.VDEF1, vdef1);
	}

	/**
	 * 获取自定义项2
	 * 
	 * @return 自定义项2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF2);
	}

	/**
	 * 设置自定义项2
	 * 
	 * @param vdef2
	 *            自定义项2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(FundTallyVO.VDEF2, vdef2);
	}

	/**
	 * 获取自定义项3
	 * 
	 * @return 自定义项3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF3);
	}

	/**
	 * 设置自定义项3
	 * 
	 * @param vdef3
	 *            自定义项3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(FundTallyVO.VDEF3, vdef3);
	}

	/**
	 * 获取自定义项4
	 * 
	 * @return 自定义项4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF4);
	}

	/**
	 * 设置自定义项4
	 * 
	 * @param vdef4
	 *            自定义项4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(FundTallyVO.VDEF4, vdef4);
	}

	/**
	 * 获取自定义项5
	 * 
	 * @return 自定义项5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(FundTallyVO.VDEF5);
	}

	/**
	 * 设置自定义项5
	 * 
	 * @param vdef5
	 *            自定义项5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(FundTallyVO.VDEF5, vdef5);
	}

	/**
	 * 获取年计息天数
	 * 
	 * @return 年计息天数
	 */
	public Integer getYearcaldays() {
		return (Integer) this.getAttributeValue(FundTallyVO.YEARCALDAYS);
	}

	/**
	 * 设置年计息天数
	 * 
	 * @param yearcaldays
	 *            年计息天数
	 */
	public void setYearcaldays(Integer yearcaldays) {
		this.setAttributeValue(FundTallyVO.YEARCALDAYS, yearcaldays);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_fund.FundTallyVO");
	}
}