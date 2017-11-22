package nc.vo.fba_scost.cost.interestdistill;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class InterestdistillVO extends SuperVO {
	/**
	 * 审批时间
	 */
	public static final String APPROVEDATE = "approvedate";
	/**
	 * 审批人
	 */
	public static final String APPROVER = "approver";
	/**
	 * 制单人
	 */
	public static final String BILLMAKER = "billmaker";
	/**
	 * 单据号
	 */
	public static final String BILLNO = "billno";
	/**
	 * 单据类型编码
	 */
	public static final String BILLTYPECODE = "billtypecode";
	/**
	 * 创建时间
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * 创建人
	 */
	public static final String CREATOR = "creator";
	/**
	 * 计提日期
	 */
	public static final String DISTILL_DATE = "distill_date";
	/**
	 * 生成状态
	 */
	public static final String GENSTATE = "genstate";
	/**
	 * 本期利息计提
	 */
	public static final String INTERESTDISTILL = "interestdistill";
	/**
	 * 制单日期
	 */
	public static final String MAKEDATE = "makedate";
	/**
	 * 修改时间
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * 修改人
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * 自定义数值1
	 */
	public static final String NDEF1 = "ndef1";
	/**
	 * 自定义数值10
	 */
	public static final String NDEF10 = "ndef10";
	/**
	 * 自定义数值2
	 */
	public static final String NDEF2 = "ndef2";
	/**
	 * 自定义数值3
	 */
	public static final String NDEF3 = "ndef3";
	/**
	 * 自定义数值4
	 */
	public static final String NDEF4 = "ndef4";
	/**
	 * 自定义数值5
	 */
	public static final String NDEF5 = "ndef5";
	/**
	 * 自定义数值6
	 */
	public static final String NDEF6 = "ndef6";
	/**
	 * 自定义数值7
	 */
	public static final String NDEF7 = "ndef7";
	/**
	 * 自定义数值8
	 */
	public static final String NDEF8 = "ndef8";
	/**
	 * 自定义数值9
	 */
	public static final String NDEF9 = "ndef9";
	/**
	 * 期末利息余额
	 */
	public static final String NQCLX = "nqclx";
	/**
	 * 期初利息余额
	 */
	public static final String NQMLX = "nqmlx";
	/**
	 * 金融资产属性
	 */
	public static final String PK_ASSETSPROP = "pk_assetsprop";
	/**
	 * 单据类型pk
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * 资金账号
	 */
	public static final String PK_CAPACCOUNT = "pk_capaccount";
	/**
	 * 往来单位
	 */
	public static final String PK_CLIENT = "pk_client";
	/**
	 * 公司
	 */
	public static final String PK_CORP = "pk_corp";
	/**
	 * 成本审核方案
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
	 * 主键
	 */
	public static final String PK_INTERESTDISTILL = "pk_interestdistill";
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
	 * 证券
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
	 * 单据状态
	 */
	public static final String STATE = "state";
	/**
	 * 库存数量
	 */
	public static final String STOCKS_NUM = "stocks_num";
	/**
	 * 库存金额
	 */
	public static final String STOCKS_SUM = "stocks_sum";
	/**
	 * 交易类型编码
	 */
	public static final String TRANSTYPECODE = "transtypecode";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";
	/**
	 * 自定义字符1
	 */
	public static final String VDEF1 = "vdef1";
	/**
	 * 自定义字符10
	 */
	public static final String VDEF10 = "vdef10";
	/**
	 * 自定义字符2
	 */
	public static final String VDEF2 = "vdef2";
	/**
	 * 自定义字符3
	 */
	public static final String VDEF3 = "vdef3";
	/**
	 * 自定义字符4
	 */
	public static final String VDEF4 = "vdef4";
	/**
	 * 自定义字符5
	 */
	public static final String VDEF5 = "vdef5";
	/**
	 * 自定义字符6
	 */
	public static final String VDEF6 = "vdef6";
	/**
	 * 自定义字符7
	 */
	public static final String VDEF7 = "vdef7";
	/**
	 * 自定义字符8
	 */
	public static final String VDEF8 = "vdef8";
	/**
	 * 自定义字符9
	 */
	public static final String VDEF9 = "vdef9";

	/**
	 * 限售开始日期
	 */
	public static final String BEGIN_DATE = "begin_date";
	/**
	 * 限售结束日期
	 */
	public static final String END_DATE = "end_date";

	/**
	 * 获取限售结束日期
	 * 
	 * @return 限售结束日期
	 */
	public UFLiteralDate getEnd_date() {
		return (UFLiteralDate) this
				.getAttributeValue(InterestdistillVO.END_DATE);
	}

	/**
	 * 设置限售结束日期
	 * 
	 * @param end_date
	 *            限售结束日期
	 */
	public void setEnd_date(UFLiteralDate end_date) {
		this.setAttributeValue(InterestdistillVO.END_DATE, end_date);
	}

	/**
	 * 获取限售开始日期
	 * 
	 * @return 限售开始日期
	 */
	public UFLiteralDate getBegin_date() {
		return (UFLiteralDate) this
				.getAttributeValue(InterestdistillVO.BEGIN_DATE);
	}

	/**
	 * 设置限售开始日期
	 * 
	 * @param begin_date
	 *            限售开始日期
	 */
	public void setBegin_date(UFLiteralDate begin_date) {
		this.setAttributeValue(InterestdistillVO.BEGIN_DATE, begin_date);
	}

	/**
	 * 获取审批时间
	 * 
	 * @return 审批时间
	 */
	public UFDateTime getApprovedate() {
		return (UFDateTime) this
				.getAttributeValue(InterestdistillVO.APPROVEDATE);
	}

	/**
	 * 设置审批时间
	 * 
	 * @param approvedate
	 *            审批时间
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.setAttributeValue(InterestdistillVO.APPROVEDATE, approvedate);
	}

	/**
	 * 获取审批人
	 * 
	 * @return 审批人
	 */
	public String getApprover() {
		return (String) this.getAttributeValue(InterestdistillVO.APPROVER);
	}

	/**
	 * 设置审批人
	 * 
	 * @param approver
	 *            审批人
	 */
	public void setApprover(String approver) {
		this.setAttributeValue(InterestdistillVO.APPROVER, approver);
	}

	/**
	 * 获取制单人
	 * 
	 * @return 制单人
	 */
	public String getBillmaker() {
		return (String) this.getAttributeValue(InterestdistillVO.BILLMAKER);
	}

	/**
	 * 设置制单人
	 * 
	 * @param billmaker
	 *            制单人
	 */
	public void setBillmaker(String billmaker) {
		this.setAttributeValue(InterestdistillVO.BILLMAKER, billmaker);
	}

	/**
	 * 获取单据号
	 * 
	 * @return 单据号
	 */
	public String getBillno() {
		return (String) this.getAttributeValue(InterestdistillVO.BILLNO);
	}

	/**
	 * 设置单据号
	 * 
	 * @param billno
	 *            单据号
	 */
	public void setBillno(String billno) {
		this.setAttributeValue(InterestdistillVO.BILLNO, billno);
	}

	/**
	 * 获取单据类型编码
	 * 
	 * @return 单据类型编码
	 */
	public String getBilltypecode() {
		return (String) this.getAttributeValue(InterestdistillVO.BILLTYPECODE);
	}

	/**
	 * 设置单据类型编码
	 * 
	 * @param billtypecode
	 *            单据类型编码
	 */
	public void setBilltypecode(String billtypecode) {
		this.setAttributeValue(InterestdistillVO.BILLTYPECODE, billtypecode);
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this
				.getAttributeValue(InterestdistillVO.CREATIONTIME);
	}

	/**
	 * 设置创建时间
	 * 
	 * @param creationtime
	 *            创建时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(InterestdistillVO.CREATIONTIME, creationtime);
	}

	/**
	 * 获取创建人
	 * 
	 * @return 创建人
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(InterestdistillVO.CREATOR);
	}

	/**
	 * 设置创建人
	 * 
	 * @param creator
	 *            创建人
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(InterestdistillVO.CREATOR, creator);
	}

	/**
	 * 获取计提日期
	 * 
	 * @return 计提日期
	 */
	public UFDate getDistill_date() {
		return (UFDate) this.getAttributeValue(InterestdistillVO.DISTILL_DATE);
	}

	/**
	 * 设置计提日期
	 * 
	 * @param distill_date
	 *            计提日期
	 */
	public void setDistill_date(UFDate distill_date) {
		this.setAttributeValue(InterestdistillVO.DISTILL_DATE, distill_date);
	}

	/**
	 * 获取生成状态
	 * 
	 * @return 生成状态
	 * @see String
	 */
	public Integer getGenstate() {
		return (Integer) this.getAttributeValue(InterestdistillVO.GENSTATE);
	}

	/**
	 * 设置生成状态
	 * 
	 * @param genstate
	 *            生成状态
	 * @see String
	 */
	public void setGenstate(Integer genstate) {
		this.setAttributeValue(InterestdistillVO.GENSTATE, genstate);
	}

	/**
	 * 获取本期利息计提
	 * 
	 * @return 本期利息计提
	 */
	public UFDouble getInterestdistill() {
		return (UFDouble) this
				.getAttributeValue(InterestdistillVO.INTERESTDISTILL);
	}

	/**
	 * 设置本期利息计提
	 * 
	 * @param interestdistill
	 *            本期利息计提
	 */
	public void setInterestdistill(UFDouble interestdistill) {
		this.setAttributeValue(InterestdistillVO.INTERESTDISTILL,
				interestdistill);
	}

	/**
	 * 获取制单日期
	 * 
	 * @return 制单日期
	 */
	public UFDateTime getMakedate() {
		return (UFDateTime) this.getAttributeValue(InterestdistillVO.MAKEDATE);
	}

	/**
	 * 设置制单日期
	 * 
	 * @param makedate
	 *            制单日期
	 */
	public void setMakedate(UFDateTime makedate) {
		this.setAttributeValue(InterestdistillVO.MAKEDATE, makedate);
	}

	/**
	 * 获取修改时间
	 * 
	 * @return 修改时间
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this
				.getAttributeValue(InterestdistillVO.MODIFIEDTIME);
	}

	/**
	 * 设置修改时间
	 * 
	 * @param modifiedtime
	 *            修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(InterestdistillVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * 获取修改人
	 * 
	 * @return 修改人
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(InterestdistillVO.MODIFIER);
	}

	/**
	 * 设置修改人
	 * 
	 * @param modifier
	 *            修改人
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(InterestdistillVO.MODIFIER, modifier);
	}

	/**
	 * 获取自定义数值1
	 * 
	 * @return 自定义数值1
	 */
	public UFDouble getNdef1() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF1);
	}

	/**
	 * 设置自定义数值1
	 * 
	 * @param ndef1
	 *            自定义数值1
	 */
	public void setNdef1(UFDouble ndef1) {
		this.setAttributeValue(InterestdistillVO.NDEF1, ndef1);
	}

	/**
	 * 获取自定义数值10
	 * 
	 * @return 自定义数值10
	 */
	public UFDouble getNdef10() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF10);
	}

	/**
	 * 设置自定义数值10
	 * 
	 * @param ndef10
	 *            自定义数值10
	 */
	public void setNdef10(UFDouble ndef10) {
		this.setAttributeValue(InterestdistillVO.NDEF10, ndef10);
	}

	/**
	 * 获取自定义数值2
	 * 
	 * @return 自定义数值2
	 */
	public UFDouble getNdef2() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF2);
	}

	/**
	 * 设置自定义数值2
	 * 
	 * @param ndef2
	 *            自定义数值2
	 */
	public void setNdef2(UFDouble ndef2) {
		this.setAttributeValue(InterestdistillVO.NDEF2, ndef2);
	}

	/**
	 * 获取自定义数值3
	 * 
	 * @return 自定义数值3
	 */
	public UFDouble getNdef3() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF3);
	}

	/**
	 * 设置自定义数值3
	 * 
	 * @param ndef3
	 *            自定义数值3
	 */
	public void setNdef3(UFDouble ndef3) {
		this.setAttributeValue(InterestdistillVO.NDEF3, ndef3);
	}

	/**
	 * 获取自定义数值4
	 * 
	 * @return 自定义数值4
	 */
	public UFDouble getNdef4() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF4);
	}

	/**
	 * 设置自定义数值4
	 * 
	 * @param ndef4
	 *            自定义数值4
	 */
	public void setNdef4(UFDouble ndef4) {
		this.setAttributeValue(InterestdistillVO.NDEF4, ndef4);
	}

	/**
	 * 获取自定义数值5
	 * 
	 * @return 自定义数值5
	 */
	public UFDouble getNdef5() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF5);
	}

	/**
	 * 设置自定义数值5
	 * 
	 * @param ndef5
	 *            自定义数值5
	 */
	public void setNdef5(UFDouble ndef5) {
		this.setAttributeValue(InterestdistillVO.NDEF5, ndef5);
	}

	/**
	 * 获取自定义数值6
	 * 
	 * @return 自定义数值6
	 */
	public UFDouble getNdef6() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF6);
	}

	/**
	 * 设置自定义数值6
	 * 
	 * @param ndef6
	 *            自定义数值6
	 */
	public void setNdef6(UFDouble ndef6) {
		this.setAttributeValue(InterestdistillVO.NDEF6, ndef6);
	}

	/**
	 * 获取自定义数值7
	 * 
	 * @return 自定义数值7
	 */
	public UFDouble getNdef7() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF7);
	}

	/**
	 * 设置自定义数值7
	 * 
	 * @param ndef7
	 *            自定义数值7
	 */
	public void setNdef7(UFDouble ndef7) {
		this.setAttributeValue(InterestdistillVO.NDEF7, ndef7);
	}

	/**
	 * 获取自定义数值8
	 * 
	 * @return 自定义数值8
	 */
	public UFDouble getNdef8() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF8);
	}

	/**
	 * 设置自定义数值8
	 * 
	 * @param ndef8
	 *            自定义数值8
	 */
	public void setNdef8(UFDouble ndef8) {
		this.setAttributeValue(InterestdistillVO.NDEF8, ndef8);
	}

	/**
	 * 获取自定义数值9
	 * 
	 * @return 自定义数值9
	 */
	public UFDouble getNdef9() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NDEF9);
	}

	/**
	 * 设置自定义数值9
	 * 
	 * @param ndef9
	 *            自定义数值9
	 */
	public void setNdef9(UFDouble ndef9) {
		this.setAttributeValue(InterestdistillVO.NDEF9, ndef9);
	}

	/**
	 * 获取期末利息余额
	 * 
	 * @return 期末利息余额
	 */
	public UFDouble getNqclx() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NQCLX);
	}

	/**
	 * 设置期末利息余额
	 * 
	 * @param nqclx
	 *            期末利息余额
	 */
	public void setNqclx(UFDouble nqclx) {
		this.setAttributeValue(InterestdistillVO.NQCLX, nqclx);
	}

	/**
	 * 获取期初利息余额
	 * 
	 * @return 期初利息余额
	 */
	public UFDouble getNqmlx() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.NQMLX);
	}

	/**
	 * 设置期初利息余额
	 * 
	 * @param nqmlx
	 *            期初利息余额
	 */
	public void setNqmlx(UFDouble nqmlx) {
		this.setAttributeValue(InterestdistillVO.NQMLX, nqmlx);
	}

	/**
	 * 获取金融资产属性
	 * 
	 * @return 金融资产属性
	 */
	public String getPk_assetsprop() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_ASSETSPROP);
	}

	/**
	 * 设置金融资产属性
	 * 
	 * @param pk_assetsprop
	 *            金融资产属性
	 */
	public void setPk_assetsprop(String pk_assetsprop) {
		this.setAttributeValue(InterestdistillVO.PK_ASSETSPROP, pk_assetsprop);
	}

	/**
	 * 获取单据类型pk
	 * 
	 * @return 单据类型pk
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_BILLTYPE);
	}

	/**
	 * 设置单据类型pk
	 * 
	 * @param pk_billtype
	 *            单据类型pk
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(InterestdistillVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * 获取资金账号
	 * 
	 * @return 资金账号
	 */
	public String getPk_capaccount() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_CAPACCOUNT);
	}

	/**
	 * 设置资金账号
	 * 
	 * @param pk_capaccount
	 *            资金账号
	 */
	public void setPk_capaccount(String pk_capaccount) {
		this.setAttributeValue(InterestdistillVO.PK_CAPACCOUNT, pk_capaccount);
	}

	/**
	 * 获取往来单位
	 * 
	 * @return 往来单位
	 */
	public String getPk_client() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_CLIENT);
	}

	/**
	 * 设置往来单位
	 * 
	 * @param pk_client
	 *            往来单位
	 */
	public void setPk_client(String pk_client) {
		this.setAttributeValue(InterestdistillVO.PK_CLIENT, pk_client);
	}

	/**
	 * 获取公司
	 * 
	 * @return 公司
	 */
	public String getPk_corp() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_CORP);
	}

	/**
	 * 设置公司
	 * 
	 * @param pk_corp
	 *            公司
	 */
	public void setPk_corp(String pk_corp) {
		this.setAttributeValue(InterestdistillVO.PK_CORP, pk_corp);
	}

	/**
	 * 获取成本审核方案
	 * 
	 * @return 成本审核方案
	 */
	public String getPk_costplan() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_COSTPLAN);
	}

	/**
	 * 设置成本审核方案
	 * 
	 * @param pk_costplan
	 *            成本审核方案
	 */
	public void setPk_costplan(String pk_costplan) {
		this.setAttributeValue(InterestdistillVO.PK_COSTPLAN, pk_costplan);
	}

	/**
	 * 获取账簿
	 * 
	 * @return 账簿
	 */
	public String getPk_glorgbook() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_GLORGBOOK);
	}

	/**
	 * 设置账簿
	 * 
	 * @param pk_glorgbook
	 *            账簿
	 */
	public void setPk_glorgbook(String pk_glorgbook) {
		this.setAttributeValue(InterestdistillVO.PK_GLORGBOOK, pk_glorgbook);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(InterestdistillVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public String getPk_interestdistill() {
		return (String) this
				.getAttributeValue(InterestdistillVO.PK_INTERESTDISTILL);
	}

	/**
	 * 设置主键
	 * 
	 * @param pk_interestdistill
	 *            主键
	 */
	public void setPk_interestdistill(String pk_interestdistill) {
		this.setAttributeValue(InterestdistillVO.PK_INTERESTDISTILL,
				pk_interestdistill);
	}

	/**
	 * 获取分户地点
	 * 
	 * @return 分户地点
	 */
	public String getPk_operatesite() {
		return (String) this
				.getAttributeValue(InterestdistillVO.PK_OPERATESITE);
	}

	/**
	 * 设置分户地点
	 * 
	 * @param pk_operatesite
	 *            分户地点
	 */
	public void setPk_operatesite(String pk_operatesite) {
		this.setAttributeValue(InterestdistillVO.PK_OPERATESITE, pk_operatesite);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(InterestdistillVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(InterestdistillVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取股东账号
	 * 
	 * @return 股东账号
	 */
	public String getPk_partnaccount() {
		return (String) this
				.getAttributeValue(InterestdistillVO.PK_PARTNACCOUNT);
	}

	/**
	 * 设置股东账号
	 * 
	 * @param pk_partnaccount
	 *            股东账号
	 */
	public void setPk_partnaccount(String pk_partnaccount) {
		this.setAttributeValue(InterestdistillVO.PK_PARTNACCOUNT,
				pk_partnaccount);
	}

	/**
	 * 获取证券
	 * 
	 * @return 证券
	 */
	public String getPk_securities() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_SECURITIES);
	}

	/**
	 * 设置证券
	 * 
	 * @param pk_securities
	 *            证券
	 */
	public void setPk_securities(String pk_securities) {
		this.setAttributeValue(InterestdistillVO.PK_SECURITIES, pk_securities);
	}

	/**
	 * 获取业务小组
	 * 
	 * @return 业务小组
	 */
	public String getPk_selfsgroup() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_SELFSGROUP);
	}

	/**
	 * 设置业务小组
	 * 
	 * @param pk_selfsgroup
	 *            业务小组
	 */
	public void setPk_selfsgroup(String pk_selfsgroup) {
		this.setAttributeValue(InterestdistillVO.PK_SELFSGROUP, pk_selfsgroup);
	}

	/**
	 * 获取库存组织
	 * 
	 * @return 库存组织
	 */
	public String getPk_stocksort() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_STOCKSORT);
	}

	/**
	 * 设置库存组织
	 * 
	 * @param pk_stocksort
	 *            库存组织
	 */
	public void setPk_stocksort(String pk_stocksort) {
		this.setAttributeValue(InterestdistillVO.PK_STOCKSORT, pk_stocksort);
	}

	/**
	 * 获取交易类型pk
	 * 
	 * @return 交易类型pk
	 */
	public String getPk_transtype() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_TRANSTYPE);
	}

	/**
	 * 设置交易类型pk
	 * 
	 * @param pk_transtype
	 *            交易类型pk
	 */
	public void setPk_transtype(String pk_transtype) {
		this.setAttributeValue(InterestdistillVO.PK_TRANSTYPE, pk_transtype);
	}

	/**
	 * 获取凭证主键
	 * 
	 * @return 凭证主键
	 */
	public String getPk_voucher() {
		return (String) this.getAttributeValue(InterestdistillVO.PK_VOUCHER);
	}

	/**
	 * 设置凭证主键
	 * 
	 * @param pk_voucher
	 *            凭证主键
	 */
	public void setPk_voucher(String pk_voucher) {
		this.setAttributeValue(InterestdistillVO.PK_VOUCHER, pk_voucher);
	}

	/**
	 * 获取单据状态
	 * 
	 * @return 单据状态
	 * @see String
	 */
	public Integer getState() {
		return (Integer) this.getAttributeValue(InterestdistillVO.STATE);
	}

	/**
	 * 设置单据状态
	 * 
	 * @param state
	 *            单据状态
	 * @see String
	 */
	public void setState(Integer state) {
		this.setAttributeValue(InterestdistillVO.STATE, state);
	}

	/**
	 * 获取库存数量
	 * 
	 * @return 库存数量
	 */
	public UFDouble getStocks_num() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.STOCKS_NUM);
	}

	/**
	 * 设置库存数量
	 * 
	 * @param stocks_num
	 *            库存数量
	 */
	public void setStocks_num(UFDouble stocks_num) {
		this.setAttributeValue(InterestdistillVO.STOCKS_NUM, stocks_num);
	}

	/**
	 * 获取库存金额
	 * 
	 * @return 库存金额
	 */
	public UFDouble getStocks_sum() {
		return (UFDouble) this.getAttributeValue(InterestdistillVO.STOCKS_SUM);
	}

	/**
	 * 设置库存金额
	 * 
	 * @param stocks_sum
	 *            库存金额
	 */
	public void setStocks_sum(UFDouble stocks_sum) {
		this.setAttributeValue(InterestdistillVO.STOCKS_SUM, stocks_sum);
	}

	/**
	 * 获取交易类型编码
	 * 
	 * @return 交易类型编码
	 */
	public String getTranstypecode() {
		return (String) this.getAttributeValue(InterestdistillVO.TRANSTYPECODE);
	}

	/**
	 * 设置交易类型编码
	 * 
	 * @param transtypecode
	 *            交易类型编码
	 */
	public void setTranstypecode(String transtypecode) {
		this.setAttributeValue(InterestdistillVO.TRANSTYPECODE, transtypecode);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(InterestdistillVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(InterestdistillVO.TS, ts);
	}

	/**
	 * 获取自定义字符1
	 * 
	 * @return 自定义字符1
	 */
	public String getVdef1() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF1);
	}

	/**
	 * 设置自定义字符1
	 * 
	 * @param vdef1
	 *            自定义字符1
	 */
	public void setVdef1(String vdef1) {
		this.setAttributeValue(InterestdistillVO.VDEF1, vdef1);
	}

	/**
	 * 获取自定义字符10
	 * 
	 * @return 自定义字符10
	 */
	public String getVdef10() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF10);
	}

	/**
	 * 设置自定义字符10
	 * 
	 * @param vdef10
	 *            自定义字符10
	 */
	public void setVdef10(String vdef10) {
		this.setAttributeValue(InterestdistillVO.VDEF10, vdef10);
	}

	/**
	 * 获取自定义字符2
	 * 
	 * @return 自定义字符2
	 */
	public String getVdef2() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF2);
	}

	/**
	 * 设置自定义字符2
	 * 
	 * @param vdef2
	 *            自定义字符2
	 */
	public void setVdef2(String vdef2) {
		this.setAttributeValue(InterestdistillVO.VDEF2, vdef2);
	}

	/**
	 * 获取自定义字符3
	 * 
	 * @return 自定义字符3
	 */
	public String getVdef3() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF3);
	}

	/**
	 * 设置自定义字符3
	 * 
	 * @param vdef3
	 *            自定义字符3
	 */
	public void setVdef3(String vdef3) {
		this.setAttributeValue(InterestdistillVO.VDEF3, vdef3);
	}

	/**
	 * 获取自定义字符4
	 * 
	 * @return 自定义字符4
	 */
	public String getVdef4() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF4);
	}

	/**
	 * 设置自定义字符4
	 * 
	 * @param vdef4
	 *            自定义字符4
	 */
	public void setVdef4(String vdef4) {
		this.setAttributeValue(InterestdistillVO.VDEF4, vdef4);
	}

	/**
	 * 获取自定义字符5
	 * 
	 * @return 自定义字符5
	 */
	public String getVdef5() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF5);
	}

	/**
	 * 设置自定义字符5
	 * 
	 * @param vdef5
	 *            自定义字符5
	 */
	public void setVdef5(String vdef5) {
		this.setAttributeValue(InterestdistillVO.VDEF5, vdef5);
	}

	/**
	 * 获取自定义字符6
	 * 
	 * @return 自定义字符6
	 */
	public String getVdef6() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF6);
	}

	/**
	 * 设置自定义字符6
	 * 
	 * @param vdef6
	 *            自定义字符6
	 */
	public void setVdef6(String vdef6) {
		this.setAttributeValue(InterestdistillVO.VDEF6, vdef6);
	}

	/**
	 * 获取自定义字符7
	 * 
	 * @return 自定义字符7
	 */
	public String getVdef7() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF7);
	}

	/**
	 * 设置自定义字符7
	 * 
	 * @param vdef7
	 *            自定义字符7
	 */
	public void setVdef7(String vdef7) {
		this.setAttributeValue(InterestdistillVO.VDEF7, vdef7);
	}

	/**
	 * 获取自定义字符8
	 * 
	 * @return 自定义字符8
	 */
	public String getVdef8() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF8);
	}

	/**
	 * 设置自定义字符8
	 * 
	 * @param vdef8
	 *            自定义字符8
	 */
	public void setVdef8(String vdef8) {
		this.setAttributeValue(InterestdistillVO.VDEF8, vdef8);
	}

	/**
	 * 获取自定义字符9
	 * 
	 * @return 自定义字符9
	 */
	public String getVdef9() {
		return (String) this.getAttributeValue(InterestdistillVO.VDEF9);
	}

	/**
	 * 设置自定义字符9
	 * 
	 * @param vdef9
	 *            自定义字符9
	 */
	public void setVdef9(String vdef9) {
		this.setAttributeValue(InterestdistillVO.VDEF9, vdef9);
	}

	@Override
	
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta(
				"fba_scost.sim_interestdistill");
	}
}