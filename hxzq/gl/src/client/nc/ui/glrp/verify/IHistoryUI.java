package nc.ui.glrp.verify;

/**
 * ��ʷ��ѯui
 * �������ڣ�(2003-5-12 19:13:00)
 * @author������
 */
public interface IHistoryUI extends java.beans.PropertyChangeListener{
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
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 15:08:51)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 */
void print(boolean bDirect);
/**
 *���û��ܻ���ϸ״̬
 * �������ڣ�(2003-5-12 19:16:47)
 * @param bSum boolean
 */
void setbSum(boolean bSum);
/**
 *  ���ܣ�������ʷ��¼��ѯ����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-13 8:41:45)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param voCond nc.vo.glrp.verify.LogFilterCondVO
 */
void setHisToryCond(nc.vo.glrp.verify.LogFilterCondVO voCond);
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
void showErrorMsg(String sMsg)throws Exception;
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
 * �˴����뷽��˵����
 * �������ڣ�(2003-5-12 22:28:12)
 */
void updateUi();
}
