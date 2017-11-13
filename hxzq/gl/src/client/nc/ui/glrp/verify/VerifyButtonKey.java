package nc.ui.glrp.verify;

/**
 *  功能：按钮key值静态类
 *  作者：宋涛
 *  创建时间：(2003-5-12 11:53:24)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
 */
public class VerifyButtonKey {
	public static final int KEY_QUERY = 0;
	public static final int KEY_VERIFY =1;
	public static final int KEY_AUTOVERIFY=2;
	public static final int KEY_UNVERIFY =3;
	public static final int KEY_REDBLUE =4;
	public static final int KEY_QUERYHISTORY =5;/*历史查询*/
	public static final int KEY_PRINTHISTORY_DIRECT =6;
	public static final int KEY_HISTORYQRY = 7;/*查询*/
	public static final int KEY_SWITCH = 8;
	public static final int KEY_VOUCHER =9;
	public static final int KEY_FETCH =10;
	public static final int KEY_SUM =11;
	public static final int KEY_RETURN =12;
	public static final int KEY_PRINT =13;
	public static final int KEY_REFRESH=14;
	public static final int KEY_HISTORYSELECTALL =15;
	public static final int KEY_HISTORYSELECTNONE = 16;
	public static final int KEY_PRINTHISTORY_TEMPLET =17;

	//卢文广添加
	public static final int KEY_QUERYALL=18;
		
	public static final int STATE_NODATA = 50;
	public static final int STATE_HASDATA = 51;
	public static final int STATE_HIS_NODATA =52;
	public static final int STATE_HIS_SUM = 53;
	public static final int STATE_HIS_DETAIL =54;
	public static final int STATE_HASSINGLEDATA =55;
	//v31添加
	public static final int STATE_FETCH=56;
	public static final int STATE_CANCELFETCH=57;

	//v30添加
	public static final int KEY_SELECTALL=19;
	public static final int KEY_SELECTNONE=20;
	public static final int KEY_AUTOREDBLUE=21;
	
	//v31添加
	public static final int KEY_CANCELFETCH=22;/*取消对照*/
	
}
