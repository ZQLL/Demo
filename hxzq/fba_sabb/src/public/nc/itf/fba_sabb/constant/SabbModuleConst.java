package nc.itf.fba_sabb.constant;

public interface SabbModuleConst {

	public final static String Entity_AgreedBB = "fba_sabb.AgreedbbVO";// 约定式购回主实体全路径
	public final static String Entity_ImpawnRepo = "fba_sabb.ImpawnRepoVO";// 股票质押式回购主实体全路径
	public final static String Entity_DailyInterest = "fba_sabb.DailyInterestVO";// 每日利息主实体全路径

	// 约购
	public final static String BILLTYPE_AGREEDBBZC = "HV5A-0xx-01";// 初始交易
	public final static String BILLTYPE_AGREEDBBZQ = "HV5A-0xx-02";// 股息入账
	public final static String BILLTYPE_AGREEDBBTQGH = "HV5A-0xx-03";// 回购到期
	public final static String BILLTYPE_AGREEDBBYQ = "HV5A-0xx-04";// 约定式购回逾期
	public final static String BILLTYPE_AGREEDBBJX = "HV5A-0xx-05";// 约定式购回结息

	// 股票质押式
	public final static String BILLTYPE_IMPAWNREPO_NORMAL = "HV5B-0xx-01";// 初始交易
	public final static String BILLTYPE_IMPAWNREPO_EXT = "HV5B-0xx-02";// 季度结息
	public final static String BILLTYPE_IMPAWNREPO_PRECOMPLETE = "HV5B-0xx-03";// 回购到期
	public final static String BILLTYPE_IMPAWNREPO_OVERDUE = "HV5B-0xx-04";// 股票质押式回购逾期
	public final static String BILLTYPE_IMPAWNREPO_SETTLEINTEREST = "HV5B-0xx-05";// 股票质押式回购结息

	// 每日利息
	public final static String BILLTYPE_IMPAWNREPO_MRJX = "HV5K-0xx-01";// 股票质押式回购每日计息
	public final static String BILLTYPE_AGREEDBB_MRJX = "HV5K-0xx-02";// 约定式购回每日计息
}
