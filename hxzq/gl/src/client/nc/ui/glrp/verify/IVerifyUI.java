package nc.ui.glrp.verify;

/**
 *  ���ܣ����˺���UI�ӿ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-8 15:47:06)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
public interface IVerifyUI extends java.beans.PropertyChangeListener{
/**
 *  ���ܣ��õ���ǰѡ�е�ƾ֤id
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-13 11:31:00)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @return java.lang.String
 */
String getCurrVouchId() throws Exception;
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-10 22:36:56)
 */
public boolean getSelectedPane();
/**
 *  ���ܣ��õ�����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-23 15:33:37)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @return java.lang.String
 */
String getTitleName();
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-10 22:36:56)
 */
void initData();
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:08:51)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 */
void print();
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-12 22:40:41)
 */
void selectAll();
/**
 *  ���ܣ�ѡ��ƾ֤��¼���ݶ�Ӧ����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:36:42)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param voDetail nc.vo.gl.pubvoucher.DetailVO
 * @exception java.lang.Exception �쳣˵����
 */
void selectData(nc.vo.glrp.verify.VerifyDetailVO voDetail) throws java.lang.Exception;
/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-12 22:40:51)
 */
void selectNone();
/**
 *  ���ܣ����÷��Ϊ��ʱ���������º����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-8 16:01:49)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param newStyle int
 */
void setStyle(int newStyle);
/**
 *  ���ܣ���ui�б���model������
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-8 15:56:01)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param newModel nc.ui.glrp.verify.IVerifyModel
 */
void setVerifyModel(IVerifyModel newModel);
/**
 *  ���ܣ�����view
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 16:07:32)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param newView nc.ui.glrp.verify.IVerifyView
 */
void setView(IVerifyView newView);
/**
 *  ���ܣ���ʾ������Ϣ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-8 16:57:43)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param sMsg java.lang.String
 */
void showErrorMsg(String sMsg) throws Exception;
/**
 *  ���ܣ���ʾ��Ϣ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-10 11:33:08)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param sMsg java.lang.String
 */
void showMsg(String sMsg);
/**
 * �����༭״̬
 * �������ڣ�(2003-5-11 15:24:07)
 */
void stopEdit();
/**
 *  ���ܣ��л�ҳǩ��ʾ���
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:42:09)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 */
void switchForm();
/**
 *  ���ܣ���ʾ�Ի���
 *  ���ߣ�
 *  ����ʱ�䣺v31
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param sMsg java.lang.String
 */
void showSuccessMsg(String sMsg);
}
