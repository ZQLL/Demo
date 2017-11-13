package nc.ui.glrp.verify;

/**
 *  功能：
 *  作者：宋涛
 *  创建时间：(2003-5-5 16:16:19)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
 */
public class VerifyDetailKey {
	public static final int SUBJPK = 0;
	public static final int SUBJCODE = 1;
	public static final int SUBJNAME = 2;
	public static final int CURPK = 3;
	public static final int CURCODE = 4;
	public static final int CURNAME = 5;
	public static final int ASSID = 6;
	public static final int ASSVO = 7;
	public static final int SELECTED = 8;
	public static final int VOUCHDATE = 9;
	public static final int BUSINESSDATE = 10;
	public static final int VERIFYNO = 11;
	public static final int VOUCHNO = 12;
	public static final int ASSCODE = 13;
	public static final int ASSNAME = 14;
	public static final int DIGEST = 15;
	public static final int DEBIT_MNY_S = 16;//数量
	public static final int DEBIT_MNY_Y = 17;//原币
	public static final int DEBIT_MNY_F = 18;//辅币
	public static final int DEBIT_MNY_B = 19;//组织本币
	public static final int DEBIT_MNY_J = 53;//集团本币
	public static final int DEBIT_MNY_Q = 54;//全局本币
	public static final int CREDIT_MNY_S = 20;
	public static final int CREDIT_MNY_Y = 21;
	public static final int CREDIT_MNY_F = 22;
	public static final int CREDIT_MNY_B = 23;
	public static final int CREDIT_MNY_J = 55;
	public static final int CREDIT_MNY_Q = 56;
	
	
	
	/*余额*/
	public static final int DEBIT_YE_S = 24;
	public static final int DEBIT_YE_Y = 25;
	public static final int DEBIT_YE_F = 26;
	public static final int DEBIT_YE_B = 27;
	public static final int DEBIT_YE_J = 57;
	public static final int DEBIT_YE_Q = 58;
	public static final int CREDIT_YE_S = 28;
	public static final int CREDIT_YE_Y = 29;
	public static final int CREDIT_YE_F = 30;
	public static final int CREDIT_YE_B = 31;
	public static final int CREDIT_YE_J = 59;
	public static final int CREDIT_YE_Q = 60;
	/*本次结算*/
	public static final int DEBIT_JS_S = 32;
	public static final int DEBIT_JS_Y = 33;
	public static final int DEBIT_JS_F = 34;
	public static final int DEBIT_JS_B = 35;
	public static final int DEBIT_JS_J = 61;
	public static final int DEBIT_JS_Q = 62;
	public static final int CREDIT_JS_S = 36;
	public static final int CREDIT_JS_Y = 37;
	public static final int CREDIT_JS_F = 38;
	public static final int CREDIT_JS_B = 39;
	public static final int CREDIT_JS_J = 63;
	public static final int CREDIT_JS_Q = 64;
	/*未结算余额*/
	public static final int DEBIT_UNJS_S = 40;
	public static final int DEBIT_UNJS_Y = 41;
	public static final int DEBIT_UNJS_F = 42;
	public static final int DEBIT_UNJS_B = 43;
	public static final int DEBIT_UNJS_J = 65;
	public static final int DEBIT_UNJS_Q = 66;
	public static final int CREDIT_UNJS_S = 44;
	public static final int CREDIT_UNJS_Y = 45;
	public static final int CREDIT_UNJS_F = 46;
	public static final int CREDIT_UNJS_B = 47;
	public static final int CREDIT_UNJS_J = 67;
	public static final int CREDIT_UNJS_Q = 68;
	/*分录号*/
	public static final int DETAILINDEX = 48;
	/*凭证类别*/
	public static final int VOUCHERTYPE =49;
	public static final int VOUCHERTYPENAME =50;
	public static final int VOUCHERTYPESUBNAME =51;
	/*显示凭证号*/
	public static final int DISP_VOUCHERNO = 52;

	/*结算合计*/
	public static final int DEBIT_SUM_S = 101;
	public static final int DEBIT_SUM_Y = 102;
	public static final int DEBIT_SUM_F = 103;
	public static final int DEBIT_SUM_B = 104;
	public static final int DEBIT_SUM_J = 115;
	public static final int DEBIT_SUM_Q = 116;
	public static final int CREDIT_SUM_S = 105;
	public static final int CREDIT_SUM_Y = 106;
	public static final int CREDIT_SUM_F = 107;
	public static final int CREDIT_SUM_B = 108;
	public static final int CREDIT_SUM_J = 117;
	public static final int CREDIT_SUM_Q = 118;

	/*编辑器设置事件名称*/
	public static final int ENT_CREDIT_Y =109;
	public static final int ENT_CREDIT_F =110;
	public static final int ENT_CREDIT_B =111;
	public static final int ENT_CREDIT_J =119;
	public static final int ENT_CREDIT_Q =120;
	public static final int ENT_DEBIT_Y =112;
	public static final int ENT_DEBIT_F =113;
	public static final int ENT_DEBIT_B =114;
	public static final int ENT_DEBIT_J =121;
	public static final int ENT_DEBIT_Q =122;

	/*显示状态*/
	public static final int STAT_DISP_CHANGE =130;

	/*全部合计--add by lwg*/
	public static final int DEBIT_SUMT_S = 201;
	public static final int DEBIT_SUMT_Y = 202;
	public static final int DEBIT_SUMT_F = 203;
	public static final int DEBIT_SUMT_B = 204;
	public static final int DEBIT_SUMT_J = 209;
	public static final int DEBIT_SUMT_Q = 210;
	public static final int CREDIT_SUMT_S = 205;
	public static final int CREDIT_SUMT_Y = 206;
	public static final int CREDIT_SUMT_F = 207;
	public static final int CREDIT_SUMT_B = 208;
	public static final int CREDIT_SUMT_J = 211;
	public static final int CREDIT_SUMT_Q = 212;

	/*未核销余额合计--add by lwg*/
	public static final int DEBIT_SUMU_S = 301;
	public static final int DEBIT_SUMU_Y = 302;
	public static final int DEBIT_SUMU_F = 303;
	public static final int DEBIT_SUMU_B = 304;
	public static final int DEBIT_SUMU_J = 309;
	public static final int DEBIT_SUMU_Q = 310;
	public static final int CREDIT_SUMU_S = 305;
	public static final int CREDIT_SUMU_Y = 306;
	public static final int CREDIT_SUMU_F = 307;
	public static final int CREDIT_SUMU_B = 308;
	public static final int CREDIT_SUMU_J = 311;
	public static final int CREDIT_SUMU_Q = 312;
	
		
}
