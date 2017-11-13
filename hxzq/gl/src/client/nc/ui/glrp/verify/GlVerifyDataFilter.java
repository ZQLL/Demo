package nc.ui.glrp.verify;

import nc.bs.logging.Logger;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.glcom.tools.GLPubProxy;

/**
 *  ���ܣ����˺���ƾ֤��¼���ݹ�����
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-5 9:39:12)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
public class GlVerifyDataFilter implements nc.vo.glrp.com.verify.IDataFilter {
	private nc.vo.glrp.com.verify.IConditionVO m_voCond;
	private nc.vo.glrp.com.verify.VerifyRuleVO m_voRule;
	private nc.vo.glrp.com.verify.IVerifyVO[] m_voDebit;
	private nc.vo.glrp.com.verify.IVerifyVO[] m_voCredit;
/**
 * GlVerifyDataFilter ������ע�⡣
 */
public GlVerifyDataFilter() {
	super();
}
 /**
	* �����ڹ���ʱ������������
	* @roseuid 3E8CFE0D0160
	*/
public void freeLocks() throws Exception {}
 /**
	* �õ���������
	* @return IVerifyVO[]
	* @roseuid 3E894DF100F6
	*/
public nc.vo.glrp.com.verify.IVerifyVO[] getCreditSelectedData() throws Exception {
	return m_voCredit;
}
 /**
	* �õ��跽����
	* @return IVerifyVO[]
	* @roseuid 3E894DAA02BB
	*/
public nc.vo.glrp.com.verify.IVerifyVO[] getDebitSelectedData() throws Exception {
	return m_voDebit;
}
/**
	* ��������
	* @roseuid 3E8CFE420351
	*/
public void onFilterData() throws Exception {
    try {
        nc.vo.glrp.com.verify.IVerifyVO[][] vos;
        vos =
            GLPubProxy.getRemoteVerifydetail().queryVerifyDatas(
                (nc.vo.glrp.verify.FilterCondVO) m_voCond);
        if (vos != null && vos.length == 2) {
            if (vos[0] != null && vos[0].length > 0) {
                m_voDebit = vos[0];
            } else {
                m_voDebit = null;
            }
            if (vos[1] != null && vos[1].length > 0) {
                m_voCredit = vos[1];
            } else {
                m_voCredit = null;
            }
        }
    } catch (Exception e) {
        Logger.error(e.getMessage(), e);
        throw new GlBusinessException(e.getMessage());
    }
}
 /**
	* @param condition - ������������
	* @roseuid 3E8CFDDC00C5
	*/
public void setCondition(nc.vo.glrp.com.verify.IConditionVO condition) throws Exception {
	m_voCond = condition;
}
 /**
	* @param rulevo - ���ú�������vo
	* @roseuid 3E8D13B000AA
	*/
public void setRule(nc.vo.glrp.com.verify.VerifyRuleVO rulevo) throws Exception {
	m_voRule = rulevo;
}
public nc.vo.glrp.com.verify.IConditionVO getM_voCond() {
	return m_voCond;
}
public void setM_voCond(nc.vo.glrp.com.verify.IConditionVO cond) {
	m_voCond = cond;
}
public nc.vo.glrp.com.verify.IVerifyVO[] getM_voCredit() {
	return m_voCredit;
}
public void setM_voCredit(nc.vo.glrp.com.verify.IVerifyVO[] credit) {
	m_voCredit = credit;
}
public nc.vo.glrp.com.verify.IVerifyVO[] getM_voDebit() {
	return m_voDebit;
}
public void setM_voDebit(nc.vo.glrp.com.verify.IVerifyVO[] debit) {
	m_voDebit = debit;
}
public nc.vo.glrp.com.verify.VerifyRuleVO getM_voRule() {
	return m_voRule;
}
public void setM_voRule(nc.vo.glrp.com.verify.VerifyRuleVO rule) {
	m_voRule = rule;
}
}
