package nc.ui.glrp.verify;

/**
 *  ���ܣ���ѯ�����ӿ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 16:11:43)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
public interface IConditionPane {
/**
 *  ���ܣ�����ȡ�ò�ѯ����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 16:13:18)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @return nc.vo.glrp.verify.FilterCondVO
 */
nc.vo.glrp.verify.FilterCondVO getFilterVO();
/**
 *  ���ܣ��õ���������
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-7 16:20:42)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 * 
 * @return nc.vo.glrp.verify.FilterCondVO
 * @param voDetail nc.vo.gl.pubvoucher.DetailVO
 */
nc.vo.glrp.verify.FilterCondVO getFilterVO(nc.vo.glrp.verify.VerifyDetailVO voDetail) throws Exception;
/**
 * �õ�Ӧ������ 0��ʱ���� 1�º����
 * �������ڣ�(2003-5-11 18:16:47)
 * @param iType int
 */
int getType();
/**
 * ����Ӧ������ 0��ʱ���� 1�º����
 * �������ڣ�(2003-5-11 18:16:47)
 * @param iType int
 */
void setType(int iType);
/**
 * ���ú���model
 * �������ڣ�(2003-5-11 18:19:24)
 * @param verifyModel nc.ui.glrp.verify.IVerifyModel
 */
void setVerifyModel(IVerifyModel verifyModel);
}
