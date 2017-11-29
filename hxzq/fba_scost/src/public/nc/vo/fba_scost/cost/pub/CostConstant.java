package nc.vo.fba_scost.cost.pub;

/**
 * ��������
 * 
 */
public class CostConstant {
	/**
	 * ���ʼ�ֵ��������
	 */
	public static String FAIR_BILLTYPE = "HV4B";// ��������
	public static String FAIR_FUNNCODE = "HV2020";// �ڵ��

	public static enum FAIR_GENSTATE {
		ZHUANCHU {
			public int getIndex() {
				return 1;
			}

			public String getName() {
				return "ת��";
			}
		},
		JITI {
			public int getIndex() {
				return 2;
			}

			public String getName() {
				return "����";
			}
		},
		ZHUANRU {
			public int getIndex() {
				return 3;
			}

			public String getName() {
				return "ת��";
			}
		};
		public abstract int getIndex();

		public abstract String getName();
	}

	/**
	 * ϵͳĬ���ڳ�����
	 */
	public static String DEFAULT_DATE = "1900-01-01 00:00:00";// ��ޱ�����޸�2010->1900

	public final static String PAYPREPERIOD = "01";// ���ڸ�Ϣ
	public final static String PAYONCEATEND = "02";// ����һ�λ�����Ϣ
	public final static String NOINTEREST = "03";// ����

	public final static String PAYPREYEAR = "01"; // ���긶Ϣ
	public final static String PAYPREHALFYEAR = "02";// ���긶Ϣ
	public final static String PAYPREQUARTER = "03";// ������Ϣ
	public final static String PAYPREMONTH = "04";// ���¸�Ϣ
	public final static String SCP = "05";// ������
	// public final static String PAYSELF = "06";// �Զ���
	public final static String PAYONCE = "06";// һ���Ը�Ϣ

	public final static String FIXEDRATE = "01";// �̶�����
	public final static String FLOATINGRATE = "02";// ��������
	public final static String PROGRESSIVERATE = "03";// �۽�����

	public final static String FULLPRICE = "01";// ȫ�۽���
	public final static String NETPRICE = "02";// ���۽���
	/**
	 * Ӧ����Ϣ����
	 */
	public static String FXHB_BILLTYPE = "HV3A";// ������Ϣ��������
	public static String CJT_BILLTYPE = "HV4C";// Ӧ����Ϣ����
	//
	public static String BUY_TRANSTYPE = "HV3A-0xx-01";// ����
	public static String SELL_TRANSTYPE = "HV3A-0xx-02";// ����������
	public static String FX_TRANSTYPE = "HV3A-0xx-06";// ��Ϣ
	public static String FORM_TRANSTYPE1 = "HV3F-0xx-01";// ֤ȯת��
	public static String FORM_TRANSTYPE2 = "HV3F-0xx-03";// ծȯת�й�

	public static enum INTEREST_GENSTATE {
		ZHUANCHU {
			public int getIndex() {
				return 1;
			}

			public String getName() {
				return "ת��";
			}
		},
		JITI {
			public int getIndex() {
				return 2;
			}

			public String getName() {
				return "����";
			}
		},
		ZHUANRU {
			public int getIndex() {
				return 3;
			}

			public String getName() {
				return "ת��";
			}
		};
		public abstract int getIndex();

		public abstract String getName();
	}

	/**
	 * ��������
	 */
	public static String PARAM_HBFX = "SEC38";// �Ƿ񻹱���Ϣ
	public static String PARAM_FAIRFORWARD = "SEC22";// ���ʼ�ֵ�Ƿ��ת
	public static String PARAM_INTERESTFORWARD = "SEC17";// Ӧ����Ϣ�Ƿ��ת
	public static String PARAM_ISCONVERINTEREST = "SEC37";// �Զ�������Ϣʱ,�Ƿ񸲸Ǽ���
	public static String PARAM_CALCBUYSELLINTEREST = "SEC18";// �Ƿ��������Ӧ����Ϣ
	public static String PARAM_JTINTEREST = "SEC30";// �Ƽ۵���,Ʊ����������
	public static String PARAM_JZTAX = "SEC80";// �Ƿ��ת˰��
	public static String PARAM_ISAUTOEXPIRE = "SEC103";// �Ƿ��Զ�����
	//

	/**
	 * ��������
	 */
	public static String COSTGROUP = "00000000000000000001";// ���С�� ----�ɱ����
	public static String HGGROUP = "00000000000000000002";// �ع����

	public static String CALC_INTEREST_WAY1 = "���Ƽ۵���";
	public static String CALC_INTEREST_WAY2 = "��Ʊ�����ʼ���";

	public static String STOCKTRADEVONAME = "nc.vo.fba_sim.simtrade.stocktrade.StocktradeVO";

	/**
	 * ���ʹ���VO��
	 */
	public static String BONDSPAYABLEVONAME = "nc.vo.fba_fund.bondspayable.BondsPayableVO";// Ӧ��ծȯ
	public static String GOFINANCINGVONAME = "nc.vo.fba_fund.gofinancing.GoFinancingVO";// ת��ͨ-����
	public static String INTERBANKLENDINGVONAME = "nc.vo.fba_fund.interbanklending.InterbankLendingVO";// ͬҵ���
	public static String PROFITTRANSFERREPOVONAME = "nc.vo.fba_fund.profittransferrepo.ProfitTransferRepoVO";// ����Ȩת�ûع�
	public static String SHORTFUNDVONAME = "nc.vo.fba_fund.shortfund.ShortFundVO";// ��������
	/**
	 * ���ʹ���AGGVO��
	 */
	public static String AGGBONDSPAYABLEVONAME = "nc.vo.fba_fund.bondspayable.AggBondsPayableVO";// Ӧ��ծȯ
	public static String AGGGOFINANCINGVONAME = "nc.vo.fba_fund.gofinancing.AggGoFinancingVO";// ת��ͨ-����
	public static String AGGINTERBANKLENDINGVONAME = "nc.vo.fba_fund.interbanklending.AggInterbankLendingVO";// ͬҵ���
	public static String AGGPROFITTRANSFERREPOVONAME = "nc.vo.fba_fund.profittransferrepo.AggProfitTransferRepoVO";// ����Ȩת�ûع�
	public static String AGGSHORTFUNDVONAME = "nc.vo.fba_fund.shortfund.AggShortFundVO";// ��������

	public static enum FORWARD_GENSTATE {
		ZHUANCHU {
			public int getIndex() {
				return 1;
			}

			public String getName() {
				return "ת��";
			}
		},
		ZHUANRU {
			public int getIndex() {
				return 2;
			}

			public String getName() {
				return "ת��";
			}
		},
		ZHUANCHUANDRU {
			public int getIndex() {
				return 3;
			}

			public String getName() {
				return "ת��ת��";
			}
		};
		public abstract int getIndex();

		public abstract String getName();
	}
}
