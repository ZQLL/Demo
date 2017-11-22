package nc.vo.fba_secd.secdimp.dataimport;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class DataImportVO extends SuperVO {
	/**
	 * 目标表
	 */
	public static final String AIM_TABLE = "aim_table";
	/**
	 * 创建时间
	 */
	public static final String CREATIONTIME = "creationtime";
	/**
	 * 创建人
	 */
	public static final String CREATOR = "creator";
	/**
	 * 数据来源
	 */
	public static final String DATAFROM = "datafrom";
	/**
	 * 自定义项1
	 */
	public static final String DEF1 = "def1";
	/**
	 * 自定义项2
	 */
	public static final String DEF2 = "def2";
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
	 * 是否业务单据
	 */
	public static final String ISBUSIBILL = "isbusibill";
	/**
	 * 修改时间
	 */
	public static final String MODIFIEDTIME = "modifiedtime";
	/**
	 * 修改人
	 */
	public static final String MODIFIER = "modifier";
	/**
	 * 单据类型
	 */
	public static final String PK_BILLTYPE = "pk_billtype";
	/**
	 * 主键
	 */
	public static final String PK_DATAIMPORT = "pk_dataimport";
	/**
	 * 集团
	 */
	public static final String PK_GROUP = "pk_group";
	/**
	 * 导入项目主键
	 */
	public static final String PK_IMPORTPROJ = "pk_importproj";
	/**
	 * 元数据
	 */
	public static final String PK_METADATA = "pk_metadata";
	/**
	 * 组织
	 */
	public static final String PK_ORG = "pk_org";
	/**
	 * 组织版本
	 */
	public static final String PK_ORG_V = "pk_org_v";
	/**
	 * 关联字段
	 */
	public static final String RELEVAFIELDNAME = "relevafieldname";
	/**
	 * 源数据
	 */
	public static final String SD_DESCRIBE = "sd_describe";
	/**
	 * 特殊处理类
	 */
	public static final String SPECIALCLASS = "specialclass";
	/**
	 * 特殊处理文件
	 */
	public static final String SPECIALFILE = "specialfile";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";

	/**
	 * 获取目标表
	 * 
	 * @return 目标表
	 */
	public String getAim_table() {
		return (String) this.getAttributeValue(DataImportVO.AIM_TABLE);
	}

	/**
	 * 设置目标表
	 * 
	 * @param aim_table
	 *            目标表
	 */
	public void setAim_table(String aim_table) {
		this.setAttributeValue(DataImportVO.AIM_TABLE, aim_table);
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public UFDateTime getCreationtime() {
		return (UFDateTime) this.getAttributeValue(DataImportVO.CREATIONTIME);
	}

	/**
	 * 设置创建时间
	 * 
	 * @param creationtime
	 *            创建时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.setAttributeValue(DataImportVO.CREATIONTIME, creationtime);
	}

	/**
	 * 获取创建人
	 * 
	 * @return 创建人
	 */
	public String getCreator() {
		return (String) this.getAttributeValue(DataImportVO.CREATOR);
	}

	/**
	 * 设置创建人
	 * 
	 * @param creator
	 *            创建人
	 */
	public void setCreator(String creator) {
		this.setAttributeValue(DataImportVO.CREATOR, creator);
	}

	/**
	 * 获取数据来源
	 * 
	 * @return 数据来源
	 * @see String
	 */
	public Integer getDatafrom() {
		return (Integer) this.getAttributeValue(DataImportVO.DATAFROM);
	}

	/**
	 * 设置数据来源
	 * 
	 * @param datafrom
	 *            数据来源
	 * @see String
	 */
	public void setDatafrom(Integer datafrom) {
		this.setAttributeValue(DataImportVO.DATAFROM, datafrom);
	}

	/**
	 * 获取自定义项1
	 * 
	 * @return 自定义项1
	 */
	public String getDef1() {
		return (String) this.getAttributeValue(DataImportVO.DEF1);
	}

	/**
	 * 设置自定义项1
	 * 
	 * @param def1
	 *            自定义项1
	 */
	public void setDef1(String def1) {
		this.setAttributeValue(DataImportVO.DEF1, def1);
	}

	/**
	 * 获取自定义项2
	 * 
	 * @return 自定义项2
	 */
	public String getDef2() {
		return (String) this.getAttributeValue(DataImportVO.DEF2);
	}

	/**
	 * 设置自定义项2
	 * 
	 * @param def2
	 *            自定义项2
	 */
	public void setDef2(String def2) {
		this.setAttributeValue(DataImportVO.DEF2, def2);
	}

	/**
	 * 获取自定义项3
	 * 
	 * @return 自定义项3
	 */
	public String getDef3() {
		return (String) this.getAttributeValue(DataImportVO.DEF3);
	}

	/**
	 * 设置自定义项3
	 * 
	 * @param def3
	 *            自定义项3
	 */
	public void setDef3(String def3) {
		this.setAttributeValue(DataImportVO.DEF3, def3);
	}

	/**
	 * 获取自定义项4
	 * 
	 * @return 自定义项4
	 */
	public String getDef4() {
		return (String) this.getAttributeValue(DataImportVO.DEF4);
	}

	/**
	 * 设置自定义项4
	 * 
	 * @param def4
	 *            自定义项4
	 */
	public void setDef4(String def4) {
		this.setAttributeValue(DataImportVO.DEF4, def4);
	}

	/**
	 * 获取自定义项5
	 * 
	 * @return 自定义项5
	 */
	public String getDef5() {
		return (String) this.getAttributeValue(DataImportVO.DEF5);
	}

	/**
	 * 设置自定义项5
	 * 
	 * @param def5
	 *            自定义项5
	 */
	public void setDef5(String def5) {
		this.setAttributeValue(DataImportVO.DEF5, def5);
	}

	/**
	 * 获取是否业务单据
	 * 
	 * @return 是否业务单据
	 */
	public UFBoolean getIsbusibill() {
		return (UFBoolean) this.getAttributeValue(DataImportVO.ISBUSIBILL);
	}

	/**
	 * 设置是否业务单据
	 * 
	 * @param isbusibill
	 *            是否业务单据
	 */
	public void setIsbusibill(UFBoolean isbusibill) {
		this.setAttributeValue(DataImportVO.ISBUSIBILL, isbusibill);
	}

	/**
	 * 获取修改时间
	 * 
	 * @return 修改时间
	 */
	public UFDateTime getModifiedtime() {
		return (UFDateTime) this.getAttributeValue(DataImportVO.MODIFIEDTIME);
	}

	/**
	 * 设置修改时间
	 * 
	 * @param modifiedtime
	 *            修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.setAttributeValue(DataImportVO.MODIFIEDTIME, modifiedtime);
	}

	/**
	 * 获取修改人
	 * 
	 * @return 修改人
	 */
	public String getModifier() {
		return (String) this.getAttributeValue(DataImportVO.MODIFIER);
	}

	/**
	 * 设置修改人
	 * 
	 * @param modifier
	 *            修改人
	 */
	public void setModifier(String modifier) {
		this.setAttributeValue(DataImportVO.MODIFIER, modifier);
	}

	/**
	 * 获取单据类型
	 * 
	 * @return 单据类型
	 */
	public String getPk_billtype() {
		return (String) this.getAttributeValue(DataImportVO.PK_BILLTYPE);
	}

	/**
	 * 设置单据类型
	 * 
	 * @param pk_billtype
	 *            单据类型
	 */
	public void setPk_billtype(String pk_billtype) {
		this.setAttributeValue(DataImportVO.PK_BILLTYPE, pk_billtype);
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public String getPk_dataimport() {
		return (String) this.getAttributeValue(DataImportVO.PK_DATAIMPORT);
	}

	/**
	 * 设置主键
	 * 
	 * @param pk_dataimport
	 *            主键
	 */
	public void setPk_dataimport(String pk_dataimport) {
		this.setAttributeValue(DataImportVO.PK_DATAIMPORT, pk_dataimport);
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return (String) this.getAttributeValue(DataImportVO.PK_GROUP);
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.setAttributeValue(DataImportVO.PK_GROUP, pk_group);
	}

	/**
	 * 获取导入项目主键
	 * 
	 * @return 导入项目主键
	 */
	public String getPk_importproj() {
		return (String) this.getAttributeValue(DataImportVO.PK_IMPORTPROJ);
	}

	/**
	 * 设置导入项目主键
	 * 
	 * @param pk_importproj
	 *            导入项目主键
	 */
	public void setPk_importproj(String pk_importproj) {
		this.setAttributeValue(DataImportVO.PK_IMPORTPROJ, pk_importproj);
	}

	/**
	 * 获取元数据
	 * 
	 * @return 元数据
	 */
	public String getPk_metadata() {
		return (String) this.getAttributeValue(DataImportVO.PK_METADATA);
	}

	/**
	 * 设置元数据
	 * 
	 * @param pk_metadata
	 *            元数据
	 */
	public void setPk_metadata(String pk_metadata) {
		this.setAttributeValue(DataImportVO.PK_METADATA, pk_metadata);
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return (String) this.getAttributeValue(DataImportVO.PK_ORG);
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.setAttributeValue(DataImportVO.PK_ORG, pk_org);
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return (String) this.getAttributeValue(DataImportVO.PK_ORG_V);
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.setAttributeValue(DataImportVO.PK_ORG_V, pk_org_v);
	}

	/**
	 * 获取关联字段
	 * 
	 * @return 关联字段
	 * @see String
	 */
	public String getRelevafieldname() {
		return (String) this.getAttributeValue(DataImportVO.RELEVAFIELDNAME);
	}

	/**
	 * 设置关联字段
	 * 
	 * @param relevafieldname
	 *            关联字段
	 * @see String
	 */
	public void setRelevafieldname(String relevafieldname) {
		this.setAttributeValue(DataImportVO.RELEVAFIELDNAME, relevafieldname);
	}

	/**
	 * 获取源数据
	 * 
	 * @return 源数据
	 * @see String
	 */
	public String getSd_describe() {
		return (String) this.getAttributeValue(DataImportVO.SD_DESCRIBE);
	}

	/**
	 * 设置源数据
	 * 
	 * @param sd_describe
	 *            源数据
	 * @see String
	 */
	public void setSd_describe(String sd_describe) {
		this.setAttributeValue(DataImportVO.SD_DESCRIBE, sd_describe);
	}

	/**
	 * 获取特殊处理类
	 * 
	 * @return 特殊处理类
	 */
	public String getSpecialclass() {
		return (String) this.getAttributeValue(DataImportVO.SPECIALCLASS);
	}

	/**
	 * 设置特殊处理类
	 * 
	 * @param specialclass
	 *            特殊处理类
	 */
	public void setSpecialclass(String specialclass) {
		this.setAttributeValue(DataImportVO.SPECIALCLASS, specialclass);
	}

	/**
	 * 获取特殊处理文件
	 * 
	 * @return 特殊处理文件
	 */
	public String getSpecialfile() {
		return (String) this.getAttributeValue(DataImportVO.SPECIALFILE);
	}

	/**
	 * 设置特殊处理文件
	 * 
	 * @param specialfile
	 *            特殊处理文件
	 */
	public void setSpecialfile(String specialfile) {
		this.setAttributeValue(DataImportVO.SPECIALFILE, specialfile);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(DataImportVO.TS);
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(DataImportVO.TS, ts);
	}

	@Override
	
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_secd.DataImportVO");
	}
}