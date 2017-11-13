package nc.ui.glrp.verify;

/**
 * ���ܣ����ú���view����ʵ�ֵĽӿ� ���ߣ����� ����ʱ�䣺(2003-5-7 16:40:57) ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ��� ע�⣺�ִ�Bug
 */
public interface IVerifyConsumer {
	/* ����ģ������ */
	public String getModuleName();

	/**
	 * ���ܣ���ʾ������Ϣ ���ߣ����� ����ʱ�䣺(2003-5-8 16:59:42) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	void showErrorMsg(String sMsg);

	/**
	 * ���ܣ���ʾ��Ϣ ���ߣ����� ����ʱ�䣺(2003-5-10 11:33:52) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	void showMsg(String sMsg);

	/**
	 * ���ܣ��޸İ�ť״̬ ���ߣ����� ����ʱ�䣺(2003-5-7 16:44:26) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param iState
	 *            int
	 */
	void updateButton(int iState);

	/**
	 * ���ܣ����ı��� ���ߣ����� ����ʱ�䣺(2003-5-23 15:36:33) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sNewTitle
	 *            java.lang.String
	 */
	void updateTitles(String sNewTitle);

	/**
	 * ���ܣ���ʾ�Ի��� ���ߣ� ����ʱ�䣺v31 ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	void showSuccessMsg(String sMsg);
}
