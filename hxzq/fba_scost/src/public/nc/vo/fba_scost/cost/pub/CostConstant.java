package nc.vo.fba_scost.cost.pub;

/**
 * 常量定义
 * 
 */
public class CostConstant {
	/**
	 * 公允价值常量定义
	 */
	public static String FAIR_BILLTYPE = "HV4B";// 单据类型
	public static String FAIR_FUNNCODE = "HV2020";// 节点号

	public static enum FAIR_GENSTATE {
		ZHUANCHU {
			public int getIndex() {
				return 1;
			}

			public String getName() {
				return "转出";
			}
		},
		JITI {
			public int getIndex() {
				return 2;
			}

			public String getName() {
				return "计提";
			}
		},
		ZHUANRU {
			public int getIndex() {
				return 3;
			}

			public String getName() {
				return "转入";
			}
		};
		public abstract int getIndex();

		public abstract String getName();
	}

	/**
	 * 系统默认期初日期
	 */
	public static String DEFAULT_DATE = "1900-01-01 00:00:00";// 梁薇进行修改2010->1900

	public final static String PAYPREPERIOD = "01";// 逐期付息
	public final static String PAYONCEATEND = "02";// 到期一次还本付息
	public final static String NOINTEREST = "03";// 贴现

	public final static String PAYPREYEAR = "01"; // 逐年付息
	public final static String PAYPREHALFYEAR = "02";// 半年付息
	public final static String PAYPREQUARTER = "03";// 按季付息
	public final static String PAYPREMONTH = "04";// 逐月付息
	public final static String SCP = "05";// 超短融
	// public final static String PAYSELF = "06";// 自定义
	public final static String PAYONCE = "06";// 一次性付息

	public final static String FIXEDRATE = "01";// 固定利率
	public final static String FLOATINGRATE = "02";// 浮动利率
	public final static String PROGRESSIVERATE = "03";// 累进利率

	public final static String FULLPRICE = "01";// 全价交易
	public final static String NETPRICE = "02";// 净价交易
	/**
	 * 应收利息常量
	 */
	public static String FXHB_BILLTYPE = "HV3A";// 还本付息单据类型
	public static String CJT_BILLTYPE = "HV4C";// 应收利息计提
	//
	public static String BUY_TRANSTYPE = "HV3A-0xx-01";// 买入
	public static String SELL_TRANSTYPE = "HV3A-0xx-02";// 卖出、还本
	public static String FX_TRANSTYPE = "HV3A-0xx-06";// 付息
	public static String FORM_TRANSTYPE1 = "HV3F-0xx-01";// 证券转换
	public static String FORM_TRANSTYPE2 = "HV3F-0xx-03";// 债券转托管

	public static enum INTEREST_GENSTATE {
		ZHUANCHU {
			public int getIndex() {
				return 1;
			}

			public String getName() {
				return "转出";
			}
		},
		JITI {
			public int getIndex() {
				return 2;
			}

			public String getName() {
				return "计提";
			}
		},
		ZHUANRU {
			public int getIndex() {
				return 3;
			}

			public String getName() {
				return "转入";
			}
		};
		public abstract int getIndex();

		public abstract String getName();
	}

	/**
	 * 参数常量
	 */
	public static String PARAM_HBFX = "SEC38";// 是否还本付息
	public static String PARAM_FAIRFORWARD = "SEC22";// 公允价值是否结转
	public static String PARAM_INTERESTFORWARD = "SEC17";// 应收利息是否结转
	public static String PARAM_ISCONVERINTEREST = "SEC37";// 自动计算利息时,是否覆盖计算
	public static String PARAM_CALCBUYSELLINTEREST = "SEC18";// 是否计算买、卖应收利息
	public static String PARAM_JTINTEREST = "SEC30";// 牌价倒扎,票面利率正算
	public static String PARAM_JZTAX = "SEC80";// 是否结转税费
	public static String PARAM_ISAUTOEXPIRE = "SEC103";// 是否自动到期
	//

	/**
	 * 其它常量
	 */
	public static String COSTGROUP = "00000000000000000001";// 审核小组 ----成本相关
	public static String HGGROUP = "00000000000000000002";// 回购相关

	public static String CALC_INTEREST_WAY1 = "按牌价倒扎";
	public static String CALC_INTEREST_WAY2 = "按票面利率计算";

	public static String STOCKTRADEVONAME = "nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO";

	/**
	 * 筹资管理VO名
	 */
	public static String BONDSPAYABLEVONAME = "nc.vo.fba_fund.bondspayable.BondsPayableVO";// 应付债券
	public static String GOFINANCINGVONAME = "nc.vo.fba_fund.gofinancing.GoFinancingVO";// 转融通-融资
	public static String INTERBANKLENDINGVONAME = "nc.vo.fba_fund.interbanklending.InterbankLendingVO";// 同业拆借
	public static String PROFITTRANSFERREPOVONAME = "nc.vo.fba_fund.profittransferrepo.ProfitTransferRepoVO";// 收益权转让回购
	public static String SHORTFUNDVONAME = "nc.vo.fba_fund.shortfund.ShortFundVO";// 短期融资
	/**
	 * 筹资管理AGGVO名
	 */
	public static String AGGBONDSPAYABLEVONAME = "nc.vo.fba_fund.bondspayable.AggBondsPayableVO";// 应付债券
	public static String AGGGOFINANCINGVONAME = "nc.vo.fba_fund.gofinancing.AggGoFinancingVO";// 转融通-融资
	public static String AGGINTERBANKLENDINGVONAME = "nc.vo.fba_fund.interbanklending.AggInterbankLendingVO";// 同业拆借
	public static String AGGPROFITTRANSFERREPOVONAME = "nc.vo.fba_fund.profittransferrepo.AggProfitTransferRepoVO";// 收益权转让回购
	public static String AGGSHORTFUNDVONAME = "nc.vo.fba_fund.shortfund.AggShortFundVO";// 短期融资

	public static enum FORWARD_GENSTATE {
		ZHUANCHU {
			public int getIndex() {
				return 1;
			}

			public String getName() {
				return "转出";
			}
		},
		ZHUANRU {
			public int getIndex() {
				return 2;
			}

			public String getName() {
				return "转入";
			}
		},
		ZHUANCHUANDRU {
			public int getIndex() {
				return 3;
			}

			public String getName() {
				return "转出转入";
			}
		};
		public abstract int getIndex();

		public abstract String getName();
	}
}
