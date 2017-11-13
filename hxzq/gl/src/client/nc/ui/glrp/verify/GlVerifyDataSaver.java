package nc.ui.glrp.verify;

import nc.bs.logging.Logger;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.glrp.verify.GlVerifyDisplogVO;
import nc.vo.glrp.verify.LogFilterCondVO;
import nc.vo.glrp.verify.VerifyLogVO;

/**
 * 功能：总账核销核销数据保存类 作者：宋涛 创建时间：(2003-5-5 9:41:22) 使用说明：以及别人可能感兴趣的介绍 注意：现存Bug
 */
public class GlVerifyDataSaver implements nc.vo.glrp.com.verify.IDataSaver {
	private LogFilterCondVO m_logCond;
	private VerifyLogVO[] m_logvo;
	private GlVerifyDisplogVO[] m_displogvo;

	/**
	 * GlVerifyDataSaver 构造子注解。
	 */
	public GlVerifyDataSaver() {
		super();
	}

	/**
	 * @param condition
	 * @return ILogVO[]
	 * @roseuid 3E8958700338
	 */
	public nc.vo.glrp.com.verify.ILogVO[] getAllLogData(nc.vo.glrp.com.verify.IConditionVO condition) throws Exception {
		try {
			return GLPubProxy.getRemoteVerifyLog().getLogData((nc.vo.glrp.verify.LogFilterCondVO) condition);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param condition
	 * @return ILogVO[]
	 * @roseuid 3E89589A0144
	 */
	public nc.vo.glrp.com.verify.ILogVO[] getBatchLogData(nc.vo.glrp.com.verify.IConditionVO condition) throws Exception {
		try {
			return getAllLogData(condition);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
		// throw new nc.vo.pub.BusinessException("保存组件GlVerifyDataSaver没有提供相应实现！");
		// return null;
	}

	/**
	 * @return IdisplayVO[]
	 * @roseuid 3E8959540098
	 */
	public nc.vo.glrp.com.verify.IdisplayVO[] getDispLogData() throws Exception {
		try {
			return GLPubProxy.getRemoteVerifyLog().getDispLogs(m_logCond);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param condition
	 * @return ILogVO[]
	 * @roseuid 3E8959060183
	 */
	public nc.vo.glrp.com.verify.ILogVO[] getPairLogData(nc.vo.glrp.com.verify.IConditionVO condition) throws Exception {
		try {
			return getAllLogData(condition);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
		// throw new nc.vo.pub.BusinessException("保存组件GlVerifyDataSaver没有提供相应实现！");
		// return null;
	}

	/**
	 * @return void
	 * @roseuid 3E895A2E0115
	 */
	public void onUnverify() throws Exception {
		//onUnverify(m_displogvo);
	}

	/**
	 * @param vo
	 * @roseuid 3E8D3C18018E
	 */
	public void onUnverify(nc.vo.glrp.com.verify.IdisplayVO[] vo,String pk_accountingbook) throws Exception {
		try {
			GLPubProxy.getRemoteVerifyLog().unVerifyWithDisp((nc.vo.glrp.verify.GlVerifyDisplogVO[]) vo,pk_accountingbook);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param logs
	 * @return void
	 * @roseuid 3E895A390089
	 */
	public void onUnverify(nc.vo.glrp.com.verify.ILogVO[] logs) throws Exception {
		try {
			GLPubProxy.getRemoteVerifyLog().unVerify((nc.vo.glrp.verify.VerifyLogVO[]) logs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
		// throw new nc.vo.pub.BusinessException("保存组件GlVerifyDataSaver没有提供相应实现！");
	}

	/**
	 * @param logs
	 * @return void
	 * @roseuid 3E895A0D0183
	 */
	public void onVerfy(nc.vo.glrp.com.verify.ILogVO[] logs) throws Exception {
		try {
			GLPubProxy.getRemoteVerifyLog().verify((VerifyLogVO[]) logs);
		} catch (Exception e) {
			// Logger.error(e.getMessage(), e);
			// throw e;
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * @return void
	 * @roseuid 3E8959FB004A
	 */
	public void onVerify() throws Exception {
		onVerfy(m_logvo);
	}

	/**
	 * @param vo
	 * @roseuid 3E8D3C4300C1
	 */
	public void onVerify(nc.vo.glrp.com.verify.IdisplayVO[] vo) throws Exception {
		try {
			GLPubProxy.getRemoteVerifyLog().verify((VerifyLogVO[]) vo);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
		// throw new nc.vo.pub.BusinessException("保存组件GlVerifyDataSaver没有提供相应实现！");
	}

	/**
	 * @param newValue
	 *            - 模拟核销确认
	 * @return void
	 * @roseuid 3E8A3B1A029F
	 */
	public void onVerifyConfirm(nc.vo.pub.lang.UFBoolean newValue) throws Exception {
		try {
			GLPubProxy.getRemoteVerifyLog().onVerifyConfirm(newValue);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
		// throw new nc.vo.pub.BusinessException("保存组件GlVerifyDataSaver没有提供相应实现！");
	}

	/**
	 * @param condition
	 * @return void
	 * @roseuid 3E89599301B2
	 */
	public void setCondition(nc.vo.glrp.com.verify.IConditionVO condition) throws Exception {
		try {
			m_logCond = (LogFilterCondVO) condition;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param credit
	 * @return void
	 * @roseuid 3E8959D203C5
	 */
	public void setCreditData(nc.vo.glrp.com.verify.IVerifyVO[] credit) throws Exception {
	}

	/**
	 * @param debitData
	 * @return void
	 * @roseuid 3E8959AF027D
	 */
	public void setDebitData(nc.vo.glrp.com.verify.IVerifyVO[] debitData) throws Exception {
	}

	/**
	 * @param logs
	 * @return void
	 * @roseuid 3E89597502FA
	 */
	public void setLogData(nc.vo.glrp.com.verify.ILogVO[] logs) throws Exception {
		try {
			m_logvo = (VerifyLogVO[]) logs;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

//	/**
//	 * @param newValue
//	 *            - 设置核销规则
//	 * @return void
//	 * @roseuid 3E8A3AF60389
//	 */
//	public void setVerifyRuleVO(nc.vo.glrp.com.verify.VerifyRuleVO newValue) throws Exception {
//		// m_rulevo = newValue;
//	}
}
