package nc.ui.glrp.verify;

/**
 *  ���ܣ���ťkeyֵ��̬��
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 11:53:24)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
public class VerifyButtonKey {
	public static final int KEY_QUERY = 0;
	public static final int KEY_VERIFY =1;
	public static final int KEY_AUTOVERIFY=2;
	public static final int KEY_UNVERIFY =3;
	public static final int KEY_REDBLUE =4;
	public static final int KEY_QUERYHISTORY =5;/*��ʷ��ѯ*/
	public static final int KEY_PRINTHISTORY_DIRECT =6;
	public static final int KEY_HISTORYQRY = 7;/*��ѯ*/
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

	//¬�Ĺ����
	public static final int KEY_QUERYALL=18;
		
	public static final int STATE_NODATA = 50;
	public static final int STATE_HASDATA = 51;
	public static final int STATE_HIS_NODATA =52;
	public static final int STATE_HIS_SUM = 53;
	public static final int STATE_HIS_DETAIL =54;
	public static final int STATE_HASSINGLEDATA =55;
	//v31���
	public static final int STATE_FETCH=56;
	public static final int STATE_CANCELFETCH=57;

	//v30���
	public static final int KEY_SELECTALL=19;
	public static final int KEY_SELECTNONE=20;
	public static final int KEY_AUTOREDBLUE=21;
	
	//v31���
	public static final int KEY_CANCELFETCH=22;/*ȡ������*/
	
}
