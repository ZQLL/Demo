package nc.ui.glrp.verify;

import nc.vo.glrp.verify.GlVerifyDisplogVO;

/**
 * ���ܣ�����modelʵ�ֽӿ� ���ߣ����� ����ʱ�䣺(2003-5-8 14:39:39) ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ��� ע�⣺�ִ�Bug
 */
public interface IVerifyModel extends java.beans.PropertyChangeListener {
	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-8 10:09:36)
	 * 
	 * @param listener
	 *            java.beans.PropertyChangeListener
	 */
	void addPropertyChangeListener(java.beans.PropertyChangeListener listener);

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-8 10:24:25)
	 * 
	 * @param propertyName
	 *            java.lang.String
	 * @param oldValue
	 *            java.lang.Object
	 * @param newValue
	 *            java.lang.Object
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue);

	/**
	 * ���ܣ��õ�model�е����� ���ߣ����� ����ʱ�䣺(2003-5-9 10:56:41) ������<|>true �跽��false ���� ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO[]
	 * @param bDebit
	 *            boolean
	 */
	nc.vo.glrp.verify.VerifyDetailVO[] getData(boolean bDebit);

	/**
	 * ���ܣ��õ���ʷ��ѯ���� ���ߣ����� ����ʱ�䣺(2003-5-19 20:45:00) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verify.LogFilterCondVO
	 */
	nc.vo.glrp.verify.LogFilterCondVO getHistoryCond();

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-9 11:55:36) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return boolean
	 */
	boolean getHistoryState();

	/**
	 * ���ܣ�ȡ�ú�����¼�е����� ���ߣ����� ����ʱ�䣺(2003-5-9 10:08:41) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 */
	Object getHistoryValueAt(int iRow, int iKey, int iStyle);

	/**
	 * ���ܣ��õ���ʷ��¼���� ���ߣ����� ����ʱ�䣺(2003-5-9 11:50:30) ������true ����״̬��false ��ϸ״̬ ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verify.GlVerifyDisplogVO[]
	 * @param bState
	 *            boolean
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	int getHistroyDataCount() throws java.lang.Exception;

	/**
	 * ���ܣ��õ�ƾ֤���˲�ѯ����vo ���ߣ����� ����ʱ�䣺(2003-5-19 19:35:50) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verify.FilterCondVO
	 */
	nc.vo.glrp.verify.FilterCondVO getQryVo();

	/**
	 * ���ܣ�ȡ��ƾ֤��¼�е����� ���ߣ����� ����ʱ�䣺(2003-5-9 10:08:41) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @param bDebit
	 *            boolean
	 */
	Object getValueAt(int iRow, int iKey, int iStyle, boolean bDebit);

	/**
	 * ���ܣ�����ƾ֤��¼�����õ�����ƾ֤��¼ ���ߣ����� ����ʱ�䣺(2003-5-14 11:38:55) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO
	 * @param pk_detail
	 *            java.lang.String
	 */
	nc.vo.glrp.verify.VerifyDetailVO getVerifyDetailByDetailpk(String pk_detail);

	/**
	 * ���ܣ��õ�������ƾ֤��¼��Ӧ�ĺ���ƾ֤��¼ ���ߣ����� ����ʱ�䣺(2003-5-14 11:38:55) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO
	 * @param pk_detail
	 *            java.lang.String
	 */
	nc.vo.glrp.verify.VerifyDetailVO getVerifyDetailvo();

	/**
	 * ���ܣ��õ��������� ���ߣ����� ����ʱ�䣺(2003-5-10 16:19:33) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 * @param pk_subj
	 *            java.lang.String
	 * @param assvos
	 *            nc.vo.glcom.ass.AssVO[]
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	nc.vo.glrp.verifyobj.VerifyObjVO getVerifyObject() throws java.lang.Exception;

	/**
	 * ���ܣ����ݿ�Ŀ�����õ���Ӧ�ĺ������� ���ߣ����� ����ʱ�䣺(2003-5-10 16:19:33) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 * @param pk_subj
	 *            java.lang.String
	 * @param assvos
	 *            nc.vo.glcom.ass.AssVO[]
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	nc.vo.glrp.verifyobj.VerifyObjVO getVerifyObject(String pk_subj) throws java.lang.Exception;

	/**
	 * ���ܣ��жϵ�ǰ������������Ƿ���Ϻ�������Ҫ�� ���ߣ����� ����ʱ�䣺(2003-5-14 14:28:26) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return boolean
	 * @param voDetail
	 *            nc.bs.glrp.verify.VerifyDetail
	 * @param verifyObj
	 *            nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	boolean isAssLegal(nc.vo.glcom.ass.AssVO[] assvo, nc.vo.glrp.verifyobj.VerifyObjVO verifyObj) throws Exception;

	/**
	 * ���ܣ��жϵ�ǰƾ֤��¼�Ƿ���������μӺ��� ���ߣ����� ����ʱ�䣺(2003-5-14 14:28:26) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return boolean
	 * @param voDetail
	 *            nc.bs.glrp.verify.VerifyDetail
	 * @param verifyObj
	 *            nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	boolean isDataLegal(nc.vo.glrp.verify.VerifyDetailVO voDetail, nc.vo.glrp.verifyobj.VerifyObjVO verifyObj) throws Exception;

	/**
	 * ���ܣ��Զ���� ���ߣ�lwg ����ʱ�䣺(2004-04-02 16:26:24) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	void onAutoRedBlue(boolean bDebit) throws Exception;

	/**
	 * ���ܣ��Զ����� ���ߣ����� ����ʱ�䣺(2003-5-8 16:26:24) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	void onAutoVerify() throws Exception;

	/**
	 * ���ܣ���ʷ����ȫѡ ���ߣ����� ����ʱ�䣺(2003-5-14 15:31:53) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	void onHistorySelectAll();

	/**
	 * ���ܣ���ʷ����ȫ�� ���ߣ����� ����ʱ�䣺(2003-5-14 15:31:53) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	void onHistorySelectNone();

	/**
	 * ���ܣ����˲μӺ��������� ���ߣ����� ����ʱ�䣺(2003-5-8 16:21:41) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param voCond
	 *            nc.vo.glrp.verify.FilterCondVO
	 */
	void onQuery(nc.vo.glrp.verify.FilterCondVO voCond) throws Exception;

	/**
	 * ���ܣ�������ʷ��¼ ���ߣ����� ����ʱ�䣺(2003-5-8 16:50:56) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	void onQueryHisRecord(nc.vo.glrp.verify.LogFilterCondVO voCond) throws Exception;

	/**
	 * ���ܣ�ִ�к����Գ���� ���ߣ����� ����ʱ�䣺(2003-5-10 11:39:14) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param bForce
	 *            boolean
	 */
	void onRedBlue(boolean bForce) throws Exception;

	/**
	 * ���ܣ�����ȫѡ ���ߣ����� ����ʱ�䣺(2003-5-14 15:31:53) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	void onSelectAll();

	/**
	 * ���ܣ�ȫѡ��ť ���ߣ�lwg ����ʱ�䣺(2004-03-30 15:31:53) ������<|> ����ֵ�� �㷨�� version:v30
	 */
	void onSelectAll_2();

	/**
	 * ���ܣ�����ȫ�� ���ߣ����� ����ʱ�䣺(2003-5-14 15:31:53) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	void onSelectNone();

	/**
	 * ���ܣ�ȫ����ť ���ߣ�lwg ����ʱ�䣺(2004-03-30 15:31:53) ������<|> ����ֵ�� �㷨�� verison:v30
	 */
	void onSelectNone_2();

	/**
	 * ���ܣ�ִ�к��� ���ߣ����� ����ʱ�䣺(2003-5-8 16:14:08) ������<|> ����ֵ��true ǿ�ƺ�����false��ǿ�ƺ��� �㷨��
	 * 
	 * @param bForce
	 *            boolean
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	void onVerify(boolean bForce) throws java.lang.Exception;

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-9 11:56:09) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param bnewState
	 *            boolean
	 */
	void setHistoryState(boolean bnewState);

	/**
	 * ������ʷ��¼ui �������ڣ�(2003-5-12 20:01:07)
	 * 
	 * @param newHistoryUi
	 *            nc.ui.glrp.verify.IHistoryUI
	 */
	void setHistoryUI(IHistoryUI newHistoryUi);

	/**
	 * ���ܣ�����model�е���ʷ��¼���� ���ߣ����� ����ʱ�䣺(2003-5-9 11:46:56) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param oValue
	 *            java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	void setHistoryValueAt(Object oValue, int iRow, int iKey) throws java.lang.Exception;

	/**
	 * ���ܣ�����model�е����� ���ߣ����� ����ʱ�䣺(2003-5-9 11:46:56) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param oValue
	 *            java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @param bDebit
	 *            boolean
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	void setValueAt(Object oValue, int iRow, int iKey, boolean bDebit) throws java.lang.Exception;

	/**
	 * ���ܣ���������׼���浽model�� ���ߣ����� ����ʱ�䣺(2003-5-9 13:48:34) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param vo
	 *            nc.vo.glrp.verify.VerifyStandardVO
	 */
	void setVerifyStandardVO(nc.vo.glrp.verify.VerifyStandardVO vo);

	/**
	 * ���ܣ���model�����ui������ ���ߣ����� ����ʱ�䣺(2003-5-8 15:49:52) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param newUi
	 *            nc.ui.glrp.verify.IVerifyUI
	 */
	void setVerifyUI(IVerifyUI newUi);

	/**
	 * ���ܣ�ִ�з����� ���ߣ����� ����ʱ�䣺(2003-5-8 16:14:49) ������<|> ����ֵ�� �㷨��
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	void unVerify() throws java.lang.Exception;

	void setFetch(boolean newFetch);

	boolean isFetch();

	/**
	 * ���ܣ��������
	 * 
	 * @throws java.lang.Exception
	 */
	void onFetch() throws java.lang.Exception;

	/**
	 * ���ܣ����ȡ������
	 * 
	 * @throws java.lang.Exception
	 */
	void onCancelFetch() throws java.lang.Exception;

	GlVerifyDisplogVO[] getSelectedLogs();
	
	/**
	 * ���ܣ����ȡ������ hurh
	 * 
	 */
	public void setPk_accountingbook(String pkAccountingbook);

}