package nc.vo.hxzq.fpxx;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class FpxxHVO extends SuperVO {
	/**
	 * 审批人
	 */
	public static final String APPROVER = "approver";
	/**
	 * 业务类型
	 */
	public static final String BUSITYPE = "busitype";
	/**
	 * 报销单标识
	 */
	public static final String BXDKEY = "bxdkey";
	/**
	 * 报销单号
	 */
	public static final String BXDNUM = "bxdnum";
	/**
	 * 制单时间
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * 制单人
	 */
	public static final String CREATOR = "creator";
	/**
	 * 来源单据表体ID
	 */
	public static final String CSRCBID = "csrcbid";
	/**
	 * 来源单据ID
	 */
	public static final String CSRCID = "csrcid";
	/**
	 * 来源单据类型编码
	 */
	public static final String CSRCTYPE = "csrctype";
	/**
	 * 交易类型
	 */
	public static final String CTRANTYPEID = "ctrantypeid";
	/**
	 * 单据日期
	 */
	public static final String DBILLDATE = "dbilldate";
	/**
	 * 自定义项1
	 */
	public static final String DEF1 = "def1";
	/**
	 * 自定义项10
	 */
	public static final String DEF10 = "def10";
	/**
	 * 自定义项11
	 */
	public static final String DEF11 = "def11";
	/**
	 * 自定义项12
	 */
	public static final String DEF12 = "def12";
	/**
	 * 自定义项13
	 */
	public static final String DEF13 = "def13";
	/**
	 * 自定义项14
	 */
	public static final String DEF14 = "def14";
	/**
	 * 自定义项15
	 */
	public static final String DEF15 = "def15";
	/**
	 * 自定义项16
	 */
	public static final String DEF16 = "def16";
	/**
	 * 自定义项17
	 */
	public static final String DEF17 = "def17";
	/**
	 * 自定义项18
	 */
	public static final String DEF18 = "def18";
	/**
	 * 自定义项19
	 */
	public static final String DEF19 = "def19";
	/**
	 * 自定义项2
	 */
	public static final String DEF2 = "def2";
	/**
	 * 自定义项20
	 */
	public static final String DEF20 = "def20";
	/**
	 * 自定义项3
	 */
	public static final String DEF3 = "def3";
	/**
	 * 自定义项4
	 */
	public static final String DEF4 = "def4";
	/**
	 * 自定义项5
	 */
	public static final String DEF5 = "def5";
	/**
	 * 自定义项6
	 */
	public static final String DEF6 = "def6";
	/**
	 * 自定义项7
	 */
	public static final String DEF7 = "def7";
	/**
	 * 自定义项8
	 */
	public static final String DEF8 = "def8";
	/**
	 * 自定义项9
	 */
	public static final String DEF9 = "def9";
	/**
	 * 电子发票金额
	 */
	public static final String EINVOICEMONEY = "einvoicemoney";
	/**
	 * 单据状态
	 */
	public static final String FSTATUSFLAG = "fstatusflag";
	/**
	 * 发票代码
	 */
	public static final String INVOICECODE = "invoicecode";
	/**
	 * 发票信息
	 */
	public static final String INVOICEINFO = "invoiceinfo";
	/**
	 * 发票号码
	 */
	public static final String INVOICENUM = "invoicenum";
	/**
	 * 发票类型
	 */
	public static final String INVOICETYPE = "invoicetype";
	/**
	 * 发票用途
	 */
	public static final String INVOICEUSE = "invoiceuse";
	/**
	 * 进项分类
	 */
	public static final String JXTYPE = "jxtype";
	/**
	 * 开票日期
	 */
	public static final String KPDATE = "kpdate";
	/**
	 * 最后修改时间
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * 最后修改人
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * 不含税金额
	 */
	public static final String MONEY = "money";
	/**
	 * 进项转出税额
	 */
	public static final String OUTMONEY = "outmoney";
	/**
	 * 进项转出类型
	 */
	public static final String OUTTYPE = "outtype";
	/**
	 * 单据类型编码
	 */
	public static final String PK_BILLTYPECODE = "pk_billtypecode";
	/**
	 * 单据类型
	 */
	public static final String PK_BILLTYPEID = "pk_billtypeid";
	/**
	 * 集团
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * 发票信息主键
	 */
	public static final String PK_HEAD = "pk_head";
	/**
	 * 组织
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * 组织版本
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * 供应商名称
	 */
	public static final String PROVIDERNAME = "providername";
	/**
	 * 税率
	 */
	public static final String RATE = "rate";
	/**
	 * 纳税人识别号
	 */
	public static final String RATEPAYERKEY = "ratepayerkey";
	/**
	 * 审批时间
	 */
	public static final String TAUDITTIME = "taudittime";
	/**
	 * 税金
	 */
	public static final String TAX = "tax";
	/**
	 * 税价合计
	 */
	public static final String TAXTOTAL = "taxtotal";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";
	/**
	 * 审批批语
	 */
	public static final String VAPPROVENOTE = "vapprovenote";
	/**
	 * 单据号
	 */
	public static final String VBILLCODE = "vbillcode";
	/**
	 * 备注
	 */
	public static final String VNOTE = "vnote";
	/**
	 * 交易类型编码
	 */
	public static final String VTRANTYPECODE = "vtrantypecode";

	/**
	 * 获取审批人
	 * 
	 * @return 审批人
	 */
	public String getApprover() {
		return (String) this.getAttributeValue(FpxxHVO.APPROVER);
	}

	/**
	 * 设置审批人
	 * 
	 * @param approver
	 *            审批人
	 */
	public void setApprover(String approver) {
		this.setAttributeValue(FpxxHVO.APPROVER, approver);
	}

	/**
	 * 获取业务类型
	 * 
	 * @return 业务类型
	 */
	public String getBusitype() {
		return (String) this.getAttributeValue(FpxxHVO.BUSITYPE);
	}

	/**
	 * 设置业务类型
	 * 
	 * @param busitype
	 *            业务类型
	 */
	public void setBusitype(String busitype) {
		this.setAttributeValue(FpxxHVO.BUSITYPE, busitype);
	}

	/**
	 * 获取报销单标识
	 * 
	 * @return 报销单标识
	 */
	public String getBxdkey() {
		return (String) this.getAttributeValue(FpxxHVO.BXDKEY);
	}

	/**
	 * 设置报销单标识
	 * 
	 * @param bxdkey
	 *            报销单标识
	 */
	public void setBxdkey(String bxdkey) {
		this.setAttributeValue(FpxxHVO.BXDKEY, bxdkey);
	}

	/**
	 * 获取报销单号
	 * 
	 * @return 报销单号
	 */
	public String getBxdnum() {
		return (String) this.getAttributeValue(FpxxHVO.BXDNUM);
	}

	/**
	 * 设置报销单号
	 * 
	 * @param bxdnum
	 *            报销单号
	 */
	public void setBxdnum(String bxdnum) {
		this.setAttributeValue(FpxxHVO.BXDNUM, bxdnum);
	}

	/**
	 * 获取制单时间
	 * 
	 * @return 制单时间
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(FpxxHVO.CREATIONTIME);
	}

	/**
	 * 设置制单时间
	 * 
	 * @param creationtime
	 *            制单时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(FpxxHVO.CREATIONTIME, creationtime);
	}

	/**
	 * 获取制单人
	 * 
	 * @return 制单人
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(FpxxHVO.CREATOR);
	}

	/**
	 * 设置制单人
	 * 
	 * @param creator
	 *            制单人
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(FpxxHVO.CREATOR, creator);
	}

	/**
	 * 获取来源单据表体ID
	 * 
	 * @return 来源单据表体ID
	 */
	public String getCsrcbid() {
		return (String) this.getAttributeValue(FpxxHVO.CSRCBID);
	}

	/**
	 * 设置来源单据表体ID
	 * 
	 * @param csrcbid
	 *            来源单据表体ID
	 */
	public void setCsrcbid(String csrcbid) {
		this.setAttributeValue(FpxxHVO.CSRCBID, csrcbid);
	}

	/**
	 * 获取来源单据ID
	 * 
	 * @return 来源单据ID
	 */
	public String getCsrcid() {
		return (String) this.getAttributeValue(FpxxHVO.CSRCID);
	}

	/**
	 * 设置来源单据ID
	 * 
	 * @param csrcid
	 *            来源单据ID
	 */
	public void setCsrcid(String csrcid) {
		this.setAttributeValue(FpxxHVO.CSRCID, csrcid);
	}

	/**
	 * 获取来源单据类型编码
	 * 
	 * @return 来源单据类型编码
	 */
	public String getCsrctype() {
		return (String) this.getAttributeValue(FpxxHVO.CSRCTYPE);
	}

	/**
	 * 设置来源单据类型编码
	 * 
	 * @param csrctype
	 *            来源单据类型编码
	 */
	public void setCsrctype(String csrctype) {
		this.setAttributeValue(FpxxHVO.CSRCTYPE, csrctype);
	}

	/**
	 * 获取交易类型
	 * 
	 * @return 交易类型
	 */
	public String getCtrantypeid() {
		return (String) this.getAttributeValue(FpxxHVO.CTRANTYPEID);
	}

	/**
	 * 设置交易类型
	 * 
	 * @param ctrantypeid
	 *            交易类型
	 */
	public void setCtrantypeid(String ctrantypeid) {
		this.setAttributeValue(FpxxHVO.CTRANTYPEID, ctrantypeid);
	}

	/**
	 * 获取单据日期
	 * 
	 * @return 单据日期
	 */
	public UFDate getDbilldate() {
		return (UFDate) this.getAttributeValue(FpxxHVO.DBILLDATE);
	}

	/**
	 * 设置单据日期
	 * 
	 * @param dbilldate
	 *            单据日期
	 */
	public void setDbilldate(UFDate dbilldate) {
		this.setAttributeValue(FpxxHVO.DBILLDATE, dbilldate);
	}

	/**
	 * 获取自定义项1
	 * 
	 * @return 自定义项1
	 */
	public String getDef1() {
		return (String) this.getAttributeValue(FpxxHVO.DEF1);
	}

	/**
	 * 设置自定义项1
	 * 
	 * @param def1
	 *            自定义项1
	 */
	public void setDef1(String def1) {
		this.setAttributeValue(FpxxHVO.DEF1, def1);
	}

	/**
	 * 获取自定义项10
	 * 
	 * @return 自定义项10
	 */
	public String getDef10() {
		return (String) this.getAttributeValue(FpxxHVO.DEF10);
	}

	/**
	 * 设置自定义项10
	 * 
	 * @param def10
	 *            自定义项10
	 */
	public void setDef10(String def10) {
		this.setAttributeValue(FpxxHVO.DEF10, def10);
	}

	/**
	 * 获取自定义项11
	 * 
	 * @return 自定义项11
	 */
	public String getDef11() {
		return (String) this.getAttributeValue(FpxxHVO.DEF11);
	}

	/**
	 * 设置自定义项11
	 * 
	 * @param def11
	 *            自定义项11
	 */
	public void setDef11(String def11) {
		this.setAttributeValue(FpxxHVO.DEF11, def11);
	}

	/**
	 * 获取自定义项12
	 * 
	 * @return 自定义项12
	 */
	public String getDef12() {
		return (String) this.getAttributeValue(FpxxHVO.DEF12);
	}

	/**
	 * 设置自定义项12
	 * 
	 * @param def12
	 *            自定义项12
	 */
	public void setDef12(String def12) {
		this.setAttributeValue(FpxxHVO.DEF12, def12);
	}

	/**
	 * 获取自定义项13
	 * 
	 * @return 自定义项13
	 */
	public String getDef13() {
		return (String) this.getAttributeValue(FpxxHVO.DEF13);
	}

	/**
	 * 设置自定义项13
	 * 
	 * @param def13
	 *            自定义项13
	 */
	public void setDef13(String def13) {
		this.setAttributeValue(FpxxHVO.DEF13, def13);
	}

	/**
	 * 获取自定义项14
	 * 
	 * @return 自定义项14
	 */
	public String getDef14() {
		return (String) this.getAttributeValue(FpxxHVO.DEF14);
	}

	/**
	 * 设置自定义项14
	 * 
	 * @param def14
	 *            自定义项14
	 */
	public void setDef14(String def14) {
		this.setAttributeValue(FpxxHVO.DEF14, def14);
	}

	/**
	 * 获取自定义项15
	 * 
	 * @return 自定义项15
	 */
	public String getDef15() {
		return (String) this.getAttributeValue(FpxxHVO.DEF15);
	}

	/**
	 * 设置自定义项15
	 * 
	 * @param def15
	 *            自定义项15
	 */
	public void setDef15(String def15) {
		this.setAttributeValue(FpxxHVO.DEF15, def15);
	}

	/**
	 * 获取自定义项16
	 * 
	 * @return 自定义项16
	 */
	public String getDef16() {
		return (String) this.getAttributeValue(FpxxHVO.DEF16);
	}

	/**
	 * 设置自定义项16
	 * 
	 * @param def16
	 *            自定义项16
	 */
	public void setDef16(String def16) {
		this.setAttributeValue(FpxxHVO.DEF16, def16);
	}

	/**
	 * 获取自定义项17
	 * 
	 * @return 自定义项17
	 */
	public String getDef17() {
		return (String) this.getAttributeValue(FpxxHVO.DEF17);
	}

	/**
	 * 设置自定义项17
	 * 
	 * @param def17
	 *            自定义项17
	 */
	public void setDef17(String def17) {
		this.setAttributeValue(FpxxHVO.DEF17, def17);
	}

	/**
	 * 获取自定义项18
	 * 
	 * @return 自定义项18
	 */
	public String getDef18() {
		return (String) this.getAttributeValue(FpxxHVO.DEF18);
	}

	/**
	 * 设置自定义项18
	 * 
	 * @param def18
	 *            自定义项18
	 */
	public void setDef18(String def18) {
		this.setAttributeValue(FpxxHVO.DEF18, def18);
	}

	/**
	 * 获取自定义项19
	 * 
	 * @return 自定义项19
	 */
	public String getDef19() {
		return (String) this.getAttributeValue(FpxxHVO.DEF19);
	}

	/**
	 * 设置自定义项19
	 * 
	 * @param def19
	 *            自定义项19
	 */
	public void setDef19(String def19) {
		this.setAttributeValue(FpxxHVO.DEF19, def19);
	}

	/**
	 * 获取自定义项2
	 * 
	 * @return 自定义项2
	 */
	public String getDef2() {
		return (String) this.getAttributeValue(FpxxHVO.DEF2);
	}

	/**
	 * 设置自定义项2
	 * 
	 * @param def2
	 *            自定义项2
	 */
	public void setDef2(String def2) {
		this.setAttributeValue(FpxxHVO.DEF2, def2);
	}

	/**
	 * 获取自定义项20
	 * 
	 * @return 自定义项20
	 */
	public String getDef20() {
		return (String) this.getAttributeValue(FpxxHVO.DEF20);
	}

	/**
	 * 设置自定义项20
	 * 
	 * @param def20
	 *            自定义项20
	 */
	public void setDef20(String def20) {
		this.setAttributeValue(FpxxHVO.DEF20, def20);
	}

	/**
	 * 获取自定义项3
	 * 
	 * @return 自定义项3
	 */
	public String getDef3() {
		return (String) this.getAttributeValue(FpxxHVO.DEF3);
	}

	/**
	 * 设置自定义项3
	 * 
	 * @param def3
	 *            自定义项3
	 */
	public void setDef3(String def3) {
		this.setAttributeValue(FpxxHVO.DEF3, def3);
	}

	/**
	 * 获取自定义项4
	 * 
	 * @return 自定义项4
	 */
	public String getDef4() {
		return (String) this.getAttributeValue(FpxxHVO.DEF4);
	}

	/**
	 * 设置自定义项4
	 * 
	 * @param def4
	 *            自定义项4
	 */
	public void setDef4(String def4) {
		this.setAttributeValue(FpxxHVO.DEF4, def4);
	}

	/**
	 * 获取自定义项5
	 * 
	 * @return 自定义项5
	 */
	public String getDef5() {
		return (String) this.getAttributeValue(FpxxHVO.DEF5);
	}

	/**
	 * 设置自定义项5
	 * 
	 * @param def5
	 *            自定义项5
	 */
	public void setDef5(String def5) {
		this.setAttributeValue(FpxxHVO.DEF5, def5);
	}

	/**
	 * 获取自定义项6
	 * 
	 * @return 自定义项6
	 */
	public String getDef6() {
		return (String) this.getAttributeValue(FpxxHVO.DEF6);
	}

	/**
	 * 设置自定义项6
	 * 
	 * @param def6
	 *            自定义项6
	 */
	public void setDef6(String def6) {
		this.setAttributeValue(FpxxHVO.DEF6, def6);
	}

	/**
	 * 获取自定义项7
	 * 
	 * @return 自定义项7
	 */
	public String getDef7() {
		return (String) this.getAttributeValue(FpxxHVO.DEF7);
	}

	/**
	 * 设置自定义项7
	 * 
	 * @param def7
	 *            自定义项7
	 */
	public void setDef7(String def7) {
		this.setAttributeValue(FpxxHVO.DEF7, def7);
	}

	/**
	 * 获取自定义项8
	 * 
	 * @return 自定义项8
	 */
	public String getDef8() {
		return (String) this.getAttributeValue(FpxxHVO.DEF8);
	}

	/**
	 * 设置自定义项8
	 * 
	 * @param def8
	 *            自定义项8
	 */
	public void setDef8(String def8) {
		this.setAttributeValue(FpxxHVO.DEF8, def8);
	}

	/**
	 * 获取自定义项9
	 * 
	 * @return 自定义项9
	 */
	public String getDef9() {
		return (String) this.getAttributeValue(FpxxHVO.DEF9);
	}

	/**
	 * 设置自定义项9
	 * 
	 * @param def9
	 *            自定义项9
	 */
	public void setDef9(String def9) {
		this.setAttributeValue(FpxxHVO.DEF9, def9);
	}

	/**
	 * 获取电子发票金额
	 * 
	 * @return 电子发票金额
	 */
	public UFDouble getEinvoicemoney() {
		return (UFDouble) this.getAttributeValue(FpxxHVO.EINVOICEMONEY);
	}

	/**
	 * 设置电子发票金额
	 * 
	 * @param einvoicemoney
	 *            电子发票金额
	 */
	public void setEinvoicemoney(UFDouble einvoicemoney) {
		this.setAttributeValue(FpxxHVO.EINVOICEMONEY, einvoicemoney);
	}

	/**
	 * 获取单据状态
	 * 
	 * @return 单据状态
	 * @see String
	 */
	public Integer getFstatusflag() {
		return (Integer) this.getAttributeValue(FpxxHVO.FSTATUSFLAG);
	}

	/**
	 * 设置单据状态
	 * 
	 * @param fstatusflag
	 *            单据状态
	 * @see String
	 */
	public void setFstatusflag(Integer fstatusflag) {
		this.setAttributeValue(FpxxHVO.FSTATUSFLAG, fstatusflag);
	}

	/**
	 * 获取发票代码
	 * 
	 * @return 发票代码
	 */
	public String getInvoicecode() {
		return (String) this.getAttributeValue(FpxxHVO.INVOICECODE);
	}

	/**
	 * 设置发票代码
	 * 
	 * @param invoicecode
	 *            发票代码
	 */
	public void setInvoicecode(String invoicecode) {
		this.setAttributeValue(FpxxHVO.INVOICECODE, invoicecode);
	}

	/**
	 * 获取发票信息
	 * 
	 * @return 发票信息
	 */
	public String getInvoiceinfo() {
		return (String) this.getAttributeValue(FpxxHVO.INVOICEINFO);
	}

	/**
	 * 设置发票信息
	 * 
	 * @param invoiceinfo
	 *            发票信息
	 */
	public void setInvoiceinfo(String invoiceinfo) {
		this.setAttributeValue(FpxxHVO.INVOICEINFO, invoiceinfo);
	}

	/**
	 * 获取发票号码
	 * 
	 * @return 发票号码
	 */
	public String getInvoicenum() {
		return (String) this.getAttributeValue(FpxxHVO.INVOICENUM);
	}

	/**
	 * 设置发票号码
	 * 
	 * @param invoicenum
	 *            发票号码
	 */
	public void setInvoicenum(String invoicenum) {
		this.setAttributeValue(FpxxHVO.INVOICENUM, invoicenum);
	}

	/**
	 * 获取发票类型
	 * 
	 * @return 发票类型
	 */
	public String getInvoicetype() {
		return (String) this.getAttributeValue(FpxxHVO.INVOICETYPE);
	}

	/**
	 * 设置发票类型
	 * 
	 * @param invoicetype
	 *            发票类型
	 */
	public void setInvoicetype(String invoicetype) {
		this.setAttributeValue(FpxxHVO.INVOICETYPE, invoicetype);
	}

	/**
	 * 获取发票用途
	 * 
	 * @return 发票用途
	 */
	public String getInvoiceuse() {
		return (String) this.getAttributeValue(FpxxHVO.INVOICEUSE);
	}

	/**
	 * 设置发票用途
	 * 
	 * @param invoiceuse
	 *            发票用途
	 */
	public void setInvoiceuse(String invoiceuse) {
		this.setAttributeValue(FpxxHVO.INVOICEUSE, invoiceuse);
	}

	/**
	 * 获取进项分类
	 * 
	 * @return 进项分类
	 */
	public String getJxtype() {
		return (String) this.getAttributeValue(FpxxHVO.JXTYPE);
	}

	/**
	 * 设置进项分类
	 * 
	 * @param jxtype
	 *            进项分类
	 */
	public void setJxtype(String jxtype) {
		this.setAttributeValue(FpxxHVO.JXTYPE, jxtype);
	}

	/**
	 * 获取开票日期
	 * 
	 * @return 开票日期
	 */
	public UFDate getKpdate() {
		return (UFDate) this.getAttributeValue(FpxxHVO.KPDATE);
	}

	/**
	 * 设置开票日期
	 * 
	 * @param kpdate
	 *            开票日期
	 */
	public void setKpdate(UFDate kpdate) {
		this.setAttributeValue(FpxxHVO.KPDATE, kpdate);
	}

	/**
	 * 获取最后修改时间
	 * 
	 * @return 最后修改时间
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(FpxxHVO.MODIFIEDTIME);
	}

	/**
	 * 设置最后修改时间
	 * 
	 * @param modifiedtime
	 *            最后修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(FpxxHVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * 获取最后修改人
	 * 
	 * @return 最后修改人
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(FpxxHVO.MODIFIER);
	}

	/**
	 * 设置最后修改人
	 * 
	 * @param modifier
	 *            最后修改人
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(FpxxHVO.MODIFIER, modifier);
	}

	/**
	 * 获取不含税金额
	 * 
	 * @return 不含税金额
	 */
	public UFDouble getMoney() {
		return (UFDouble) this.getAttributeValue(FpxxHVO.MONEY);
	}

	/**
	 * 设置不含税金额
	 * 
	 * @param money
	 *            不含税金额
	 */
	public void setMoney(UFDouble money) {
		this.setAttributeValue(FpxxHVO.MONEY, money);
	}

	/**
	 * 获取进项转出税额
	 * 
	 * @return 进项转出税额
	 */
	public UFDouble getOutmoney() {
		return (UFDouble) this.getAttributeValue(FpxxHVO.OUTMONEY);
	}

	/**
	 * 设置进项转出税额
	 * 
	 * @param outmoney
	 *            进项转出税额
	 */
	public void setOutmoney(UFDouble outmoney) {
		this.setAttributeValue(FpxxHVO.OUTMONEY, outmoney);
	}

	/**
	 * 获取进项转出类型
	 * 
	 * @return 进项转出类型
	 */
	public String getOuttype() {
		return (String) this.getAttributeValue(FpxxHVO.OUTTYPE);
	}

	/**
	 * 设置进项转出类型
	 * 
	 * @param outtype
	 *            进项转出类型
	 */
	public void setOuttype(String outtype) {
		this.setAttributeValue(FpxxHVO.OUTTYPE, outtype);
	}

	/**
	 * 获取单据类型编码
	 * 
	 * @return 单据类型编码
	 */
	public String getPk_billtypecode() {
		return (String) this.getAttributeValue(FpxxHVO.PK_BILLTYPECODE);
	}

	/**
	 * 设置单据类型编码
	 * 
	 * @param pk_billtypecode
	 *            单据类型编码
	 */
	public void setPk_billtypecode(String pk_billtypecode) {
		this.setAttributeValue(FpxxHVO.PK_BILLTYPECODE, pk_billtypecode);
	}

	/**
	 * 获取单据类型
	 * 
	 * @return 单据类型
	 */
	public String getPk_billtypeid() {
		return (String) this.getAttributeValue(FpxxHVO.PK_BILLTYPEID);
	}

	/**
	 * 设置单据类型
	 * 
	 * @param pk_billtypeid
	 *            单据类型
	 */
	public void setPk_billtypeid(String pk_billtypeid) {
		this.setAttributeValue(FpxxHVO.PK_BILLTYPEID, pk_billtypeid);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(FpxxHVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(FpxxHVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取发票信息主键
	 * 
	 * @return 发票信息主键
	 */
	public String getPk_head() {
		return (String) this.getAttributeValue(FpxxHVO.PK_HEAD);
	}

	/**
	 * 设置发票信息主键
	 * 
	 * @param pk_head
	 *            发票信息主键
	 */
	public void setPk_head(String pk_head) {
		this.setAttributeValue(FpxxHVO.PK_HEAD, pk_head);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(FpxxHVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(FpxxHVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(FpxxHVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(FpxxHVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取供应商名称
	 * 
	 * @return 供应商名称
	 */
	public String getProvidername() {
		return (String) this.getAttributeValue(FpxxHVO.PROVIDERNAME);
	}

	/**
	 * 设置供应商名称
	 * 
	 * @param providername
	 *            供应商名称
	 */
	public void setProvidername(String providername) {
		this.setAttributeValue(FpxxHVO.PROVIDERNAME, providername);
	}

	/**
	 * 获取税率
	 * 
	 * @return 税率
	 */
	public UFDouble getRate() {
		return (UFDouble) this.getAttributeValue(FpxxHVO.RATE);
	}

	/**
	 * 设置税率
	 * 
	 * @param rate
	 *            税率
	 */
	public void setRate(UFDouble rate) {
		this.setAttributeValue(FpxxHVO.RATE, rate);
	}

	/**
	 * 获取纳税人识别号
	 * 
	 * @return 纳税人识别号
	 */
	public String getRatepayerkey() {
		return (String) this.getAttributeValue(FpxxHVO.RATEPAYERKEY);
	}

	/**
	 * 设置纳税人识别号
	 * 
	 * @param ratepayerkey
	 *            纳税人识别号
	 */
	public void setRatepayerkey(String ratepayerkey) {
		this.setAttributeValue(FpxxHVO.RATEPAYERKEY, ratepayerkey);
	}

	/**
	 * 获取审批时间
	 * 
	 * @return 审批时间
	 */
	public UFDateTime getTaudittime() {
		return (UFDateTime) this.getAttributeValue(FpxxHVO.TAUDITTIME);
	}

	/**
	 * 设置审批时间
	 * 
	 * @param taudittime
	 *            审批时间
	 */
	public void setTaudittime(UFDateTime taudittime) {
		this.setAttributeValue(FpxxHVO.TAUDITTIME, taudittime);
	}

	/**
	 * 获取税金
	 * 
	 * @return 税金
	 */
	public UFDouble getTax() {
		return (UFDouble) this.getAttributeValue(FpxxHVO.TAX);
	}

	/**
	 * 设置税金
	 * 
	 * @param tax
	 *            税金
	 */
	public void setTax(UFDouble tax) {
		this.setAttributeValue(FpxxHVO.TAX, tax);
	}

	/**
	 * 获取税价合计
	 * 
	 * @return 税价合计
	 */
	public UFDouble getTaxtotal() {
		return (UFDouble) this.getAttributeValue(FpxxHVO.TAXTOTAL);
	}

	/**
	 * 设置税价合计
	 * 
	 * @param taxtotal
	 *            税价合计
	 */
	public void setTaxtotal(UFDouble taxtotal) {
		this.setAttributeValue(FpxxHVO.TAXTOTAL, taxtotal);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FpxxHVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(FpxxHVO.TS, ts);
	}

	/**
	 * 获取审批批语
	 * 
	 * @return 审批批语
	 */
	public String getVapprovenote() {
		return (String) this.getAttributeValue(FpxxHVO.VAPPROVENOTE);
	}

	/**
	 * 设置审批批语
	 * 
	 * @param vapprovenote
	 *            审批批语
	 */
	public void setVapprovenote(String vapprovenote) {
		this.setAttributeValue(FpxxHVO.VAPPROVENOTE, vapprovenote);
	}

	/**
	 * 获取单据号
	 * 
	 * @return 单据号
	 */
	public String getVbillcode() {
		return (String) this.getAttributeValue(FpxxHVO.VBILLCODE);
	}

	/**
	 * 设置单据号
	 * 
	 * @param vbillcode
	 *            单据号
	 */
	public void setVbillcode(String vbillcode) {
		this.setAttributeValue(FpxxHVO.VBILLCODE, vbillcode);
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getVnote() {
		return (String) this.getAttributeValue(FpxxHVO.VNOTE);
	}

	/**
	 * 设置备注
	 * 
	 * @param vnote
	 *            备注
	 */
	public void setVnote(String vnote) {
		this.setAttributeValue(FpxxHVO.VNOTE, vnote);
	}

	/**
	 * 获取交易类型编码
	 * 
	 * @return 交易类型编码
	 */
	public String getVtrantypecode() {
		return (String) this.getAttributeValue(FpxxHVO.VTRANTYPECODE);
	}

	/**
	 * 设置交易类型编码
	 * 
	 * @param vtrantypecode
	 *            交易类型编码
	 */
	public void setVtrantypecode(String vtrantypecode) {
		this.setAttributeValue(FpxxHVO.VTRANTYPECODE, vtrantypecode);
	}

	@Override
	
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hxzq.hxzq_FpxxHVO");
	}
}