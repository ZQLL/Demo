package nc.vo.sim.iufo;

public interface SIMIufoFuncConst {

	/** DEL 2016年1月28日 */
	// public static final int FUNC_COUNT = 10;
	/** DEL 2016年1月28日 */
	/** 在原来10个基础上新增5个函数 */
	public static final int FUNC_COUNT = 22;

	// public final static String SIM_ZQQCSL = "FBA_SIM_ZQQCSL";// 库存期初数量
	// public final static String SIM_ZQQMSL = "FBA_SIM_ZQQMSL";// 库存期末数量
	// public final static String SIM_ZQQCJE = "FBA_SIM_ZQQCJE";// 库存期初金额
	// public final static String SIM_ZQQMJE = "FBA_SIM_ZQQMJE";// 库存期末金额
	// public final static String SIM_ZQSZQC = "FBA_SIM_ZQSZQC";// 市值期初金额
	// public final static String SIM_ZQSZQM = "FBA_SIM_ZQSZQM";// 市值期末金额
	// public final static String SIM_ZQGYQC = "FBA_SIM_ZQGYQC";// 公允价值期初金额
	// public final static String SIM_ZQGYQM = "FBA_SIM_ZQGYQM";// 公允价值期末金额
	// public final static String SIM_ZQCJFS = "FBA_SIM_ZQCJFS";// 本期发生成交金额
	// public final static String SIM_ZQXSCB = "FBA_SIM_ZQXSCB";// 本期发生销售成本

	public final static String SIM_ZQQCSL = "SIM_ZQQCSL";// 库存期初数量
	public final static String SIM_ZQQMSL = "SIM_ZQQMSL";// 库存期末数量
	public final static String SIM_ZQQCJE = "SIM_ZQQCJE";// 库存期初金额
	public final static String SIM_ZQQMJE = "SIM_ZQQMJE";// 库存期末金额
	public final static String SIM_ZQSZQC = "SIM_ZQSZQC";// 市值期初金额
	public final static String SIM_ZQSZQM = "SIM_ZQSZQM";// 市值期末金额
	public final static String SIM_ZQGYQC = "SIM_ZQGYQC";// 公允价值期初金额
	public final static String SIM_ZQGYQM = "SIM_ZQGYQM";// 公允价值期末金额
	public final static String SIM_ZQCJFS = "SIM_ZQCJFS";// 本期发生成交金额
	public final static String SIM_ZQXSCB = "SIM_ZQXSCB";// 本期发生销售成本
	public final static String SIM_MRHYJZQM = "SIM_MRHYJZQM";// 买合约价值
	public final static String SIM_MCHYJZQM = "SIM_MCHYJZQM";// 卖合约价值

	/** 2016-1-28 jingqt 国金要求添加新的5个函数 其中新增4个是返回的形式不同 ADD START */
	/** 期初持有比例函数 */
	public final static String SIM_QCCYBL = "SIM_QCCYBL";
	/** 期末持有比例函数 */
	public final static String SIM_QMCYBL = "SIM_QMCYBL";
	/** 期初持有比例函数 */
	public final static String SIM_JYRIPYL = "SIM_JYRIPYL";
	/** 持仓数量/总股本占比证券排名（返回编码） */
	public final static String SIM_CCZBPMBM = "SIM_CCZBPMBM";
	/** 持仓数量/总股本占比证券排名（返回名称） */
	public final static String SIM_CCZBPMMC = "SIM_CCZBPMMC";
	/** 持仓数量/总股本占比证券排名（返回PK） */
	public final static String SIM_CCZBPMPK = "SIM_CCZBPMPK";
	/** 成本/净资本占比证券排名（返回编码） */
	public final static String SIM_CBZBPMBM = "SIM_CBZBPMBM";
	/** 成本/净资本占比证券排名（返回名称） */
	public final static String SIM_CBZBPMMC = "SIM_CBZBPMMC";
	/** 成本/净资本占比证券排名（返回PK） */
	public final static String SIM_CBZBPMPK = "SIM_CBZBPMPK";
	/** 2016-1-28 jingqt 国金要求添加新的5个函数 ADD END */

	/** 2017-02-14 lihaibo 华西要求添加取约定式购回报表的期末市值 */
	/** 约定式购回-市值期末金额 */
	public final static String SIM_HXZQSZQM = "SIM_HXZQSZQM";

	public final static String[] funCodes = new String[] { SIM_ZQQCSL,
			SIM_ZQQMSL, SIM_ZQQCJE, SIM_ZQQMJE, SIM_ZQSZQC, SIM_ZQSZQM,
			SIM_ZQGYQC, SIM_ZQGYQM, SIM_ZQCJFS, SIM_ZQXSCB, SIM_QCCYBL,
			SIM_QMCYBL, SIM_JYRIPYL, SIM_CCZBPMBM, SIM_CCZBPMMC, SIM_CCZBPMPK,
			SIM_CBZBPMBM, SIM_CBZBPMMC, SIM_CBZBPMPK, SIM_MRHYJZQM,
			SIM_MCHYJZQM, SIM_HXZQSZQM, };

	public final static String[] funcNames = new String[] { "库存期初数量", "库存期末数量",
			"库存期初金额", "库存期末金额", "市值期初金额", "市值期末金额", "公允价值期初金额", "公允价值期末金额",
			"本期发生成交金额", "本期发生销售成本", "期初持有比例函数", "期末持有比例函数", "交易日期偏移量",
			"持仓数量/总股本占比证券排名（返回编码）", "持仓数量/总股本占比证券排名（返回名称）",
			"持仓数量/总股本占比证券排名（返回PK）", "成本/净资本占比证券排名（返回编码）", "成本/净资本占比证券排名（返回名称）",
			"成本/净资本占比证券排名（返回PK）", "买合约价值", "卖合约价值", "约定式购回-市值期末金额" };

	public final static String[] funcDesces = new String[] {
			"库存期初数量(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"库存期末数量(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"库存期初金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"库存期末金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"市值期初金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"市值期末金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"公允价值期初金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"公允价值期末金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"本期发生成交金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"本期发生销售成本(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)",
			"期初持有比例函数（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"期末持有比例函数（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"交易日期偏移量（日期,偏移量）",
			"持仓数量/总股本占比证券排名（返回编码）（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"持仓数量/总股本占比证券排名（返回名称）（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"持仓数量/总股本占比证券排名（返回PK）（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"成本/净资本占比证券排名（返回编码）（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"成本/净资本占比证券排名（返回名称）（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"成本/净资本占比证券排名（返回PK）（组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性）",
			"买合约价值（组织,账薄,时间,证券档案,资金账号,币种,库存组织）",
			"买合约价值（组织,账薄,时间,证券档案,资金账号,币种,库存组织）",
			"约定式购回-市值期末金额(组织,账薄,期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性)" };

	// 交易日期偏移量（日期,偏移量）
	public String[] jyparamNames = { "日期", "偏移量" };
	/**
	 * 参数
	 */
	public String[] paramNames = { "组织", /** 编码 **/
	"账薄", /** 编码 **/
	"期间", /** 格式 ： 2010-10-09 **/
	"期间属性", /** 年、月、日 **/
	"证券分类", /** 编码 **/
	"证券档案", /** 编码 **/
	"资金账号", /** 编码 **/
	"业务小组", /** 编码 **/
	"股东账号", /** 编码 **/
	"分户地点", /** 编码 **/
	"往来单位", /** 编码 **/
	"币种", /** 编码 **/
	"返回币种", /** 编码 **/
	"是否限售", /** Y、N、ALL **/
	"发生方向", /** B、S **/
	"排名", /** 排名 **/
	"排序方式", /** A、D **/
	"净资本", /** 净资本 **/
	"库存组织", /** 编码 **/
	"资产属性" }/** 编码 **/
	;
	public String[] paramCodes = { "orgCode", "glbookCode", "period",
			"periodType", "pk_classify", "pk_securities", "pk_capaccount",
			"pk_selfsgroup", "pk_partnaccount", "pk_operatesite", "pk_client",
			"pk_currtype", "pk_returntype", "islimit", "bs", "rank", "ad",
			"pure_capital", "stocksortcode", "assetspropcode" };
	public String[] preiodType = { "年", "月", "日" };
	public String[] bsType = { "B", "S" };// B代表取买入的成交金额，S代表取卖出的成交金额
	public String[] sortType = { "A", "D" };// A代表升序，D代表降序
	public String[] limitType = { "Y", "N", "ALL" };// Y表示取限售的， N表示取所有的
}
