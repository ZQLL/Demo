package nc.ui.glrp.verify;

/**
 *  ���ܣ�����view����ʵ�ֵĽӿ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 16:23:03)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
public interface IVerifyView {
/*����ģ������*/
public String  getModuleName();
/**
 *  ���ܣ���Ӧ��ť�¼�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 16:24:35)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param iBtnIdx int
 * @exception java.lang.Exception �쳣˵����
 */
void onButtonClicked(int iBtnIdx) throws java.lang.Exception;
/**
 *  ���ܣ���ʱ�����������ݣ������ǰƾ֤��¼���ܲμӺ������׳��쳣��
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 16:32:55)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param detailvo nc.vo.gl.pubvoucher.DetailVO
 * @exception java.lang.Exception �쳣˵����
 */
void queryData(nc.vo.gl.pubvoucher.DetailVO detailvo) throws java.lang.Exception;
/**
 *  ���ܣ����ø�����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-12 11:42:32)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param parent nc.ui.glrp.verify.IVerifyConsumer
 */
void setparent(IVerifyConsumer parent);
/**
 *  ���ܣ����÷��Ϊ��ʱ���������º����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 16:28:19)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param iStyle int
 */
void setStyle(int iStyle);
/**
 *  ���ܣ���ʾ������Ϣ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-8 16:59:42)
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
 *  ����ʱ�䣺(2003-5-10 11:33:52)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @param sMsg java.lang.String
 */
void showMsg(String sMsg);
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
