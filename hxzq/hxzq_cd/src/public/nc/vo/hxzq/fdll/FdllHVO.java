package nc.vo.hxzq.fdll;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class FdllHVO extends SuperVO {
	/**
	 * 审批人
	 */
	public static final String APPROVER = "approver";
	/**
	 * 业务类型
	 */
	public static final String BUSITYPE = "busitype";
	/**
	 * 调整时间
	 */
	public static final String CHANGETIME = "changetime";
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
	 * 单据状态
	 */
	public static final String FSTATUSFLAG = "fstatusflag";
	/**
	 * 是否生效
	 */
	public static final String ISEFFECT = "iseffect";
	/**
	 * 基准利率类型
	 */
	public static final String LL_TPYE = "ll_tpye";
	/**
	 * 最后修改时间
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * 最后修改人
	 */
	public static final String MODIFIER = "modifier";
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
	 * 浮动利率主键
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
	 * 基准利率
	 */
	public static final String RATE = "rate";
	/**
	 * 审批时间
	 */
	public static final String TAUDITTIME = "taudittime";
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
		return (String) this.getAttributeValue(FdllHVO.APPROVER);
	}

	/**
	 * 设置审批人
	 * 
	 * @param approver
	 *            审批人
	 */
	public void setApprover(String approver) {
		this.setAttributeValue(FdllHVO.APPROVER, approver);
	}

	/**
	 * 获取业务类型
	 * 
	 * @return 业务类型
	 */
	public String getBusitype() {
		return (String) this.getAttributeValue(FdllHVO.BUSITYPE);
	}

	/**
	 * 设置业务类型
	 * 
	 * @param busitype
	 *            业务类型
	 */
	public void setBusitype(String busitype) {
		this.setAttributeValue(FdllHVO.BUSITYPE, busitype);
	}

	/**
	 * 获取调整时间
	 * 
	 * @return 调整时间
	 */
	public UFDateTime getChangetime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.CHANGETIME);
	}

	/**
	 * 设置调整时间
	 * 
	 * @param changetime
	 *            调整时间
	 */
	public void setChangetime(UFDateTime changetime) {
		this.setAttributeValue(FdllHVO.CHANGETIME, changetime);
	}

	/**
	 * 获取制单时间
	 * 
	 * @return 制单时间
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.CREATIONTIME);
	}

	/**
	 * 设置制单时间
	 * 
	 * @param creationtime
	 *            制单时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(FdllHVO.CREATIONTIME, creationtime);
	}

	/**
	 * 获取制单人
	 * 
	 * @return 制单人
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(FdllHVO.CREATOR);
	}

	/**
	 * 设置制单人
	 * 
	 * @param creator
	 *            制单人
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(FdllHVO.CREATOR, creator);
	}

	/**
	 * 获取来源单据表体ID
	 * 
	 * @return 来源单据表体ID
	 */
	public String getCsrcbid() {
		return (String) this.getAttributeValue(FdllHVO.CSRCBID);
	}

	/**
	 * 设置来源单据表体ID
	 * 
	 * @param csrcbid
	 *            来源单据表体ID
	 */
	public void setCsrcbid(String csrcbid) {
		this.setAttributeValue(FdllHVO.CSRCBID, csrcbid);
	}

	/**
	 * 获取来源单据ID
	 * 
	 * @return 来源单据ID
	 */
	public String getCsrcid() {
		return (String) this.getAttributeValue(FdllHVO.CSRCID);
	}

	/**
	 * 设置来源单据ID
	 * 
	 * @param csrcid
	 *            来源单据ID
	 */
	public void setCsrcid(String csrcid) {
		this.setAttributeValue(FdllHVO.CSRCID, csrcid);
	}

	/**
	 * 获取来源单据类型编码
	 * 
	 * @return 来源单据类型编码
	 */
	public String getCsrctype() {
		return (String) this.getAttributeValue(FdllHVO.CSRCTYPE);
	}

	/**
	 * 设置来源单据类型编码
	 * 
	 * @param csrctype
	 *            来源单据类型编码
	 */
	public void setCsrctype(String csrctype) {
		this.setAttributeValue(FdllHVO.CSRCTYPE, csrctype);
	}

	/**
	 * 获取交易类型
	 * 
	 * @return 交易类型
	 */
	public String getCtrantypeid() {
		return (String) this.getAttributeValue(FdllHVO.CTRANTYPEID);
	}

	/**
	 * 设置交易类型
	 * 
	 * @param ctrantypeid
	 *            交易类型
	 */
	public void setCtrantypeid(String ctrantypeid) {
		this.setAttributeValue(FdllHVO.CTRANTYPEID, ctrantypeid);
	}

	/**
	 * 获取单据日期
	 * 
	 * @return 单据日期
	 */
	public UFDate getDbilldate() {
		return (UFDate) this.getAttributeValue(FdllHVO.DBILLDATE);
	}

	/**
	 * 设置单据日期
	 * 
	 * @param dbilldate
	 *            单据日期
	 */
	public void setDbilldate(UFDate dbilldate) {
		this.setAttributeValue(FdllHVO.DBILLDATE, dbilldate);
	}

	/**
	 * 获取自定义项1
	 * 
	 * @return 自定义项1
	 */
	public String getDef1() {
		return (String) this.getAttributeValue(FdllHVO.DEF1);
	}

	/**
	 * 设置自定义项1
	 * 
	 * @param def1
	 *            自定义项1
	 */
	public void setDef1(String def1) {
		this.setAttributeValue(FdllHVO.DEF1, def1);
	}

	/**
	 * 获取自定义项10
	 * 
	 * @return 自定义项10
	 */
	public String getDef10() {
		return (String) this.getAttributeValue(FdllHVO.DEF10);
	}

	/**
	 * 设置自定义项10
	 * 
	 * @param def10
	 *            自定义项10
	 */
	public void setDef10(String def10) {
		this.setAttributeValue(FdllHVO.DEF10, def10);
	}

	/**
	 * 获取自定义项11
	 * 
	 * @return 自定义项11
	 */
	public String getDef11() {
		return (String) this.getAttributeValue(FdllHVO.DEF11);
	}

	/**
	 * 设置自定义项11
	 * 
	 * @param def11
	 *            自定义项11
	 */
	public void setDef11(String def11) {
		this.setAttributeValue(FdllHVO.DEF11, def11);
	}

	/**
	 * 获取自定义项12
	 * 
	 * @return 自定义项12
	 */
	public String getDef12() {
		return (String) this.getAttributeValue(FdllHVO.DEF12);
	}

	/**
	 * 设置自定义项12
	 * 
	 * @param def12
	 *            自定义项12
	 */
	public void setDef12(String def12) {
		this.setAttributeValue(FdllHVO.DEF12, def12);
	}

	/**
	 * 获取自定义项13
	 * 
	 * @return 自定义项13
	 */
	public String getDef13() {
		return (String) this.getAttributeValue(FdllHVO.DEF13);
	}

	/**
	 * 设置自定义项13
	 * 
	 * @param def13
	 *            自定义项13
	 */
	public void setDef13(String def13) {
		this.setAttributeValue(FdllHVO.DEF13, def13);
	}

	/**
	 * 获取自定义项14
	 * 
	 * @return 自定义项14
	 */
	public String getDef14() {
		return (String) this.getAttributeValue(FdllHVO.DEF14);
	}

	/**
	 * 设置自定义项14
	 * 
	 * @param def14
	 *            自定义项14
	 */
	public void setDef14(String def14) {
		this.setAttributeValue(FdllHVO.DEF14, def14);
	}

	/**
	 * 获取自定义项15
	 * 
	 * @return 自定义项15
	 */
	public String getDef15() {
		return (String) this.getAttributeValue(FdllHVO.DEF15);
	}

	/**
	 * 设置自定义项15
	 * 
	 * @param def15
	 *            自定义项15
	 */
	public void setDef15(String def15) {
		this.setAttributeValue(FdllHVO.DEF15, def15);
	}

	/**
	 * 获取自定义项16
	 * 
	 * @return 自定义项16
	 */
	public String getDef16() {
		return (String) this.getAttributeValue(FdllHVO.DEF16);
	}

	/**
	 * 设置自定义项16
	 * 
	 * @param def16
	 *            自定义项16
	 */
	public void setDef16(String def16) {
		this.setAttributeValue(FdllHVO.DEF16, def16);
	}

	/**
	 * 获取自定义项17
	 * 
	 * @return 自定义项17
	 */
	public String getDef17() {
		return (String) this.getAttributeValue(FdllHVO.DEF17);
	}

	/**
	 * 设置自定义项17
	 * 
	 * @param def17
	 *            自定义项17
	 */
	public void setDef17(String def17) {
		this.setAttributeValue(FdllHVO.DEF17, def17);
	}

	/**
	 * 获取自定义项18
	 * 
	 * @return 自定义项18
	 */
	public String getDef18() {
		return (String) this.getAttributeValue(FdllHVO.DEF18);
	}

	/**
	 * 设置自定义项18
	 * 
	 * @param def18
	 *            自定义项18
	 */
	public void setDef18(String def18) {
		this.setAttributeValue(FdllHVO.DEF18, def18);
	}

	/**
	 * 获取自定义项19
	 * 
	 * @return 自定义项19
	 */
	public String getDef19() {
		return (String) this.getAttributeValue(FdllHVO.DEF19);
	}

	/**
	 * 设置自定义项19
	 * 
	 * @param def19
	 *            自定义项19
	 */
	public void setDef19(String def19) {
		this.setAttributeValue(FdllHVO.DEF19, def19);
	}

	/**
	 * 获取自定义项2
	 * 
	 * @return 自定义项2
	 */
	public String getDef2() {
		return (String) this.getAttributeValue(FdllHVO.DEF2);
	}

	/**
	 * 设置自定义项2
	 * 
	 * @param def2
	 *            自定义项2
	 */
	public void setDef2(String def2) {
		this.setAttributeValue(FdllHVO.DEF2, def2);
	}

	/**
	 * 获取自定义项20
	 * 
	 * @return 自定义项20
	 */
	public String getDef20() {
		return (String) this.getAttributeValue(FdllHVO.DEF20);
	}

	/**
	 * 设置自定义项20
	 * 
	 * @param def20
	 *            自定义项20
	 */
	public void setDef20(String def20) {
		this.setAttributeValue(FdllHVO.DEF20, def20);
	}

	/**
	 * 获取自定义项3
	 * 
	 * @return 自定义项3
	 */
	public String getDef3() {
		return (String) this.getAttributeValue(FdllHVO.DEF3);
	}

	/**
	 * 设置自定义项3
	 * 
	 * @param def3
	 *            自定义项3
	 */
	public void setDef3(String def3) {
		this.setAttributeValue(FdllHVO.DEF3, def3);
	}

	/**
	 * 获取自定义项4
	 * 
	 * @return 自定义项4
	 */
	public String getDef4() {
		return (String) this.getAttributeValue(FdllHVO.DEF4);
	}

	/**
	 * 设置自定义项4
	 * 
	 * @param def4
	 *            自定义项4
	 */
	public void setDef4(String def4) {
		this.setAttributeValue(FdllHVO.DEF4, def4);
	}

	/**
	 * 获取自定义项5
	 * 
	 * @return 自定义项5
	 */
	public String getDef5() {
		return (String) this.getAttributeValue(FdllHVO.DEF5);
	}

	/**
	 * 设置自定义项5
	 * 
	 * @param def5
	 *            自定义项5
	 */
	public void setDef5(String def5) {
		this.setAttributeValue(FdllHVO.DEF5, def5);
	}

	/**
	 * 获取自定义项6
	 * 
	 * @return 自定义项6
	 */
	public String getDef6() {
		return (String) this.getAttributeValue(FdllHVO.DEF6);
	}

	/**
	 * 设置自定义项6
	 * 
	 * @param def6
	 *            自定义项6
	 */
	public void setDef6(String def6) {
		this.setAttributeValue(FdllHVO.DEF6, def6);
	}

	/**
	 * 获取自定义项7
	 * 
	 * @return 自定义项7
	 */
	public String getDef7() {
		return (String) this.getAttributeValue(FdllHVO.DEF7);
	}

	/**
	 * 设置自定义项7
	 * 
	 * @param def7
	 *            自定义项7
	 */
	public void setDef7(String def7) {
		this.setAttributeValue(FdllHVO.DEF7, def7);
	}

	/**
	 * 获取自定义项8
	 * 
	 * @return 自定义项8
	 */
	public String getDef8() {
		return (String) this.getAttributeValue(FdllHVO.DEF8);
	}

	/**
	 * 设置自定义项8
	 * 
	 * @param def8
	 *            自定义项8
	 */
	public void setDef8(String def8) {
		this.setAttributeValue(FdllHVO.DEF8, def8);
	}

	/**
	 * 获取自定义项9
	 * 
	 * @return 自定义项9
	 */
	public String getDef9() {
		return (String) this.getAttributeValue(FdllHVO.DEF9);
	}

	/**
	 * 设置自定义项9
	 * 
	 * @param def9
	 *            自定义项9
	 */
	public void setDef9(String def9) {
		this.setAttributeValue(FdllHVO.DEF9, def9);
	}

	/**
	 * 获取单据状态
	 * 
	 * @return 单据状态
	 * @see String
	 */
	public Integer getFstatusflag() {
		return (Integer) this.getAttributeValue(FdllHVO.FSTATUSFLAG);
	}

	/**
	 * 设置单据状态
	 * 
	 * @param fstatusflag
	 *            单据状态
	 * @see String
	 */
	public void setFstatusflag(Integer fstatusflag) {
		this.setAttributeValue(FdllHVO.FSTATUSFLAG, fstatusflag);
	}

	/**
	 * 获取是否生效
	 * 
	 * @return 是否生效
	 */
	public UFBoolean getIseffect() {
		return (UFBoolean) this.getAttributeValue(FdllHVO.ISEFFECT);
	}

	/**
	 * 设置是否生效
	 * 
	 * @param iseffect
	 *            是否生效
	 */
	public void setIseffect(UFBoolean iseffect) {
		this.setAttributeValue(FdllHVO.ISEFFECT, iseffect);
	}

	/**
	 * 获取基准利率类型
	 * 
	 * @return 基准利率类型
	 */
	public String getLl_tpye() {
		return (String) this.getAttributeValue(FdllHVO.LL_TPYE);
	}

	/**
	 * 设置基准利率类型
	 * 
	 * @param ll_tpye
	 *            基准利率类型
	 */
	public void setLl_tpye(String ll_tpye) {
		this.setAttributeValue(FdllHVO.LL_TPYE, ll_tpye);
	}

	/**
	 * 获取最后修改时间
	 * 
	 * @return 最后修改时间
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.MODIFIEDTIME);
	}

	/**
	 * 设置最后修改时间
	 * 
	 * @param modifiedtime
	 *            最后修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(FdllHVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * 获取最后修改人
	 * 
	 * @return 最后修改人
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(FdllHVO.MODIFIER);
	}

	/**
	 * 设置最后修改人
	 * 
	 * @param modifier
	 *            最后修改人
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(FdllHVO.MODIFIER, modifier);
	}

	/**
	 * 获取单据类型编码
	 * 
	 * @return 单据类型编码
	 */
	public String getPk_billtypecode() {
		return (String) this.getAttributeValue(FdllHVO.PK_BILLTYPECODE);
	}

	/**
	 * 设置单据类型编码
	 * 
	 * @param pk_billtypecode
	 *            单据类型编码
	 */
	public void setPk_billtypecode(String pk_billtypecode) {
		this.setAttributeValue(FdllHVO.PK_BILLTYPECODE, pk_billtypecode);
	}

	/**
	 * 获取单据类型
	 * 
	 * @return 单据类型
	 */
	public String getPk_billtypeid() {
		return (String) this.getAttributeValue(FdllHVO.PK_BILLTYPEID);
	}

	/**
	 * 设置单据类型
	 * 
	 * @param pk_billtypeid
	 *            单据类型
	 */
	public void setPk_billtypeid(String pk_billtypeid) {
		this.setAttributeValue(FdllHVO.PK_BILLTYPEID, pk_billtypeid);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(FdllHVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(FdllHVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取浮动利率主键
	 * 
	 * @return 浮动利率主键
	 */
	public String getPk_head() {
		return (String) this.getAttributeValue(FdllHVO.PK_HEAD);
	}

	/**
	 * 设置浮动利率主键
	 * 
	 * @param pk_head
	 *            浮动利率主键
	 */
	public void setPk_head(String pk_head) {
		this.setAttributeValue(FdllHVO.PK_HEAD, pk_head);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(FdllHVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(FdllHVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(FdllHVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(FdllHVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取基准利率
	 * 
	 * @return 基准利率
	 */
	public UFDouble getRate() {
		return (UFDouble) this.getAttributeValue(FdllHVO.RATE);
	}

	/**
	 * 设置基准利率
	 * 
	 * @param rate
	 *            基准利率
	 */
	public void setRate(UFDouble rate) {
		this.setAttributeValue(FdllHVO.RATE, rate);
	}

	/**
	 * 获取审批时间
	 * 
	 * @return 审批时间
	 */
	public UFDateTime getTaudittime() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.TAUDITTIME);
	}

	/**
	 * 设置审批时间
	 * 
	 * @param taudittime
	 *            审批时间
	 */
	public void setTaudittime(UFDateTime taudittime) {
		this.setAttributeValue(FdllHVO.TAUDITTIME, taudittime);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FdllHVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(FdllHVO.TS, ts);
	}

	/**
	 * 获取审批批语
	 * 
	 * @return 审批批语
	 */
	public String getVapprovenote() {
		return (String) this.getAttributeValue(FdllHVO.VAPPROVENOTE);
	}

	/**
	 * 设置审批批语
	 * 
	 * @param vapprovenote
	 *            审批批语
	 */
	public void setVapprovenote(String vapprovenote) {
		this.setAttributeValue(FdllHVO.VAPPROVENOTE, vapprovenote);
	}

	/**
	 * 获取单据号
	 * 
	 * @return 单据号
	 */
	public String getVbillcode() {
		return (String) this.getAttributeValue(FdllHVO.VBILLCODE);
	}

	/**
	 * 设置单据号
	 * 
	 * @param vbillcode
	 *            单据号
	 */
	public void setVbillcode(String vbillcode) {
		this.setAttributeValue(FdllHVO.VBILLCODE, vbillcode);
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getVnote() {
		return (String) this.getAttributeValue(FdllHVO.VNOTE);
	}

	/**
	 * 设置备注
	 * 
	 * @param vnote
	 *            备注
	 */
	public void setVnote(String vnote) {
		this.setAttributeValue(FdllHVO.VNOTE, vnote);
	}

	/**
	 * 获取交易类型编码
	 * 
	 * @return 交易类型编码
	 */
	public String getVtrantypecode() {
		return (String) this.getAttributeValue(FdllHVO.VTRANTYPECODE);
	}

	/**
	 * 设置交易类型编码
	 * 
	 * @param vtrantypecode
	 *            交易类型编码
	 */
	public void setVtrantypecode(String vtrantypecode) {
		this.setAttributeValue(FdllHVO.VTRANTYPECODE, vtrantypecode);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hxzq.FdllHVO");
	}
}