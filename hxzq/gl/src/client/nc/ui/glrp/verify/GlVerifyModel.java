package nc.ui.glrp.verify;

/**
 *  功能：总账核销model
 *  作者：宋涛
 *  创建时间：(2003-5-9 11:57:30)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.itf.glcom.para.GLParaAccessor;
import nc.itf.glcom.para.GLParaValueConst;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.ui.gl.datacache.AccountCache;
import nc.ui.gl.datacache.FreeValueDataCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.glcom.numbertool.GlCurrAmountFormat;
import nc.ui.glcom.numbertool.GlNumberFormat;
import nc.ui.glrp.pub.VerifyTool;
import nc.ui.glverifycom.ShowStyleTool;
import nc.ui.pub.DataFormatUtilGL;
import nc.ui.subjvery.ClientInfo;
import nc.vo.bd.accessor.IBDData;
import nc.vo.bd.account.AccountVO;
import nc.vo.gateway60.itfs.Currency;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.gl.cache.AccsubjDataCache;
import nc.vo.glcom.ass.AssVO;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.glrp.com.verify.IVerifyVO;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.GlVerifyDispLogKey;
import nc.vo.glrp.verify.GlVerifyDisplogVO;
import nc.vo.glrp.verify.VerifyDetailKey;
import nc.vo.glrp.verify.VerifyDetailVO;
import nc.vo.glrp.verifyobj.ObjQryVO;
import nc.vo.glrp.verifyobj.VerifyObjHeaderVO;
import nc.vo.glrp.verifyobj.VerifyObjItemVO;
import nc.vo.glrp.verifyobj.VerifyObjVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class GlVerifyModel implements IVerifyModel {
	private String REDBLUE = "GLRedBlueMatch";

	private String VERIFY = "GLSingleMatch";

	private String LOGNAME = "nc.vo.glrp.verify.VerifyLogVO";

	private nc.vo.glrp.com.verify.VerifyRuleVO m_rulevo;

	private String m_sDateName;

	private nc.vo.glrp.com.verify.VerifyCom m_VerifyCom;

	private java.beans.PropertyChangeSupport listener = new java.beans.PropertyChangeSupport(this);

	private GlCurrAmountFormat m_Formater = null;

	private GlNumberFormat m_NumberFormater = new GlNumberFormat();

	private nc.ui.glverifycom.ShowStyleTool m_GLTool;

	private Integer m_QuantityDigit = null;

	/* 核销对象 */
	private nc.vo.glrp.verifyobj.VerifyObjVO m_verifyObj;

	/* 借方凭证数据 */
	private nc.vo.glrp.verify.VerifyDetailVO[] m_DebitVerify;

	/* 贷方凭证数据 */
	private nc.vo.glrp.verify.VerifyDetailVO[] m_CreditVerify;

	/* 借方选择数据合计 */
	private UFDouble m_debitSum_Y = new UFDouble(0);

	private UFDouble m_debitSum_F = new UFDouble(0);

	private UFDouble m_debitSum_B = new UFDouble(0);

	private UFDouble m_debitSum_J = new UFDouble(0);

	private UFDouble m_debitSum_Q = new UFDouble(0);

	/* 贷方选择数据合计 */
	private UFDouble m_creditSum_Y = new UFDouble(0);

	private UFDouble m_creditSum_F = new UFDouble(0);

	private UFDouble m_creditSum_B = new UFDouble(0);

	private UFDouble m_creditSum_J = new UFDouble(0);

	private UFDouble m_creditSum_Q = new UFDouble(0);

	/* 核销标准 */
	private nc.vo.glrp.verify.VerifyStandardVO m_Standardvo;

	/* 核销ui */
	private IVerifyUI m_ui;

	/* 历史纪录状态 */
	private boolean m_bSum = true;

	private IHistoryUI m_historyUI;

	private nc.vo.glrp.verify.LogFilterCondVO m_historyCondvo;

	private GlVerifyDisplogVO[] m_sumLogs;

	private GlVerifyDisplogVO[] m_detailLogs;

	private java.util.Hashtable m_hashSelectedSumLogs = new java.util.Hashtable();

	private GlVerifyDisplogVO[] m_selectedDetailLogs;

	/* 与总账凭证分录对应的核销凭证分录 */
	private nc.vo.glrp.verify.VerifyDetailVO m_voDetail;

	/* 凭证过滤条件vo */
	private nc.vo.glrp.verify.FilterCondVO m_voQry;

	/* 合法性检查hash */
	private java.util.Hashtable m_hashAss = new java.util.Hashtable();

	/* 选择数据数量计数 */
	private int m_iSelectedCount = 0;

	/* 借方全部数据合计--add by lwg */
	private UFDouble m_debitSumT_Y = new UFDouble(0);

	/* 贷方全部数据合计--add by lwg */

	/* 借方余额数据合计--add by lwg */
	private UFDouble m_debitSumU_Y = new UFDouble(0);

	private UFDouble m_debitSumU_F = new UFDouble(0);

	private UFDouble m_debitSumU_B = new UFDouble(0);

	private UFDouble m_debitSumU_J = new UFDouble(0);

	private UFDouble m_debitSumU_Q = new UFDouble(0);

	/* 贷方余额数据合计--add by lwg */
	private UFDouble m_creditSumU_Y = new UFDouble(0);

	private UFDouble m_creditSumU_F = new UFDouble(0);

	private UFDouble m_creditSumU_B = new UFDouble(0);

	private UFDouble m_creditSumU_J = new UFDouble(0);

	private UFDouble m_creditSumU_Q = new UFDouble(0);

	/* 保存数据的hash表 */
	private java.util.Hashtable m_hash;

	/* 当前是否对照状态 */
	private boolean m_Fetch = false;

	/* 设置对照的主方向 */
	private int m_FetchDirect = 0;// 1为借，-1为贷

	/* 缓存借方凭证数据，用于过滤 */
	private nc.vo.glrp.verify.VerifyDetailVO[] m_FecthDebitVerify;

	/* 缓存贷方凭证数据，用于过滤 */
	private nc.vo.glrp.verify.VerifyDetailVO[] m_FetchCreditVerify;

	private String pk_accountingbook;

	/**
	 * GlVerifyModel 构造子注解。
	 */
	public GlVerifyModel() {
		super();
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-8 10:09:36)
	 * 
	 * @param listener
	 *            java.beans.PropertyChangeListener
	 */
	public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
		this.listener.addPropertyChangeListener(listener);
	}

	/**
	 * 功能：取得核销对象的后继处理 作者：宋涛 创建时间：(2003-6-4 18:40:52) 参数：<|> 返回值： 算法：
	 * 
	 */
	private void afterGetVerifyObj() {
		if (m_verifyObj != null) {
			AccountVO voSubj = AccountCache.getInstance().getAccountVOByPK(getPk_accountingbook(),
					((VerifyObjHeaderVO) m_verifyObj.getParentVO()).getPk_accsubj());
			nc.vo.glrp.verify.FormatVO formatvo = new nc.vo.glrp.verify.FormatVO();
			formatvo.setShowSubjCode(!voSubj.getEndflag().booleanValue());
			firePropertyChange(String.valueOf(VerifyDetailKey.STAT_DISP_CHANGE), null, formatvo);
		}
	}

	/**
	 * 功能：添加凭证分录中的信息 作者：宋涛 创建时间：(2003-6-10 14:34:48) 参数：<|> 返回值： 算法：
	 * 
	 * @param voDetail
	 *            nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	private void appendDetailInfo(VerifyDetailVO[] voDetail) {
		if (voDetail == null || voDetail.length == 0) {
			return;
		}
		Map<String,AccountVO> accountMap = new HashMap<String,AccountVO>();
		for (int i = 0; i < voDetail.length; i++) {
			accountMap.put(voDetail[i].getPk_accsubj(), null);
		}
		
		AccountVO[] account = AccountCache.getInstance().getAccountVOByPKs(voDetail[0].getPk_glorgbook(), 
				accountMap.keySet().toArray(new String[0]),   GlWorkBench.getBusiDate().toLocalString());
		for (int i = 0; i < account.length; i++) {
			accountMap.put(account[i].getPk_accasoa(), account[i]);
		}

		for (int i = 0; i < voDetail.length; i++) {
			AccountVO accountvo = accountMap.get(voDetail[i].getPk_accsubj());
			voDetail[i].setSubjCode(accountvo.getCode());
			voDetail[i].setSubjName(accountvo.getDispname());
		}

	}

	/**
	 * 功能：核销前调用 作者：宋涛 创建时间：(2003-6-4 15:13:28) 参数：<|> 返回值： 算法：
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void beforeVerify() throws java.lang.Exception {
		try {
			VerifyObjVO vo = GLPubProxy.getRemoteVerifyobj().findVerObjVOByPrimaryKey(
					((VerifyObjHeaderVO) getVerifyObject().getParentVO()).getPk_verifyObj());
			if (vo != null) {
				m_verifyObj.setChildrenVO(vo.getChildrenVO());
			} else {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000246")/*
																												 * @res
																												 * "核销对象已被删除，不能进行核销！"
																												 */);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_OBJREMOVED_ERROR());
//			throw new GlBusinessException(e.getMessage());
			throw new Exception(VerifyMsg.getMSG_OBJREMOVED_ERROR());
		}
	}

	/**
	 * 功能：计算选中数据的合计 作者：宋涛 创建时间：(2003-5-9 15:48:55) 参数：<|> 返回值： 算法：
	 * 
	 * @param bDebit
	 *            boolean
	 */
	private UFDouble calculateSum(nc.vo.glrp.verify.VerifyDetailVO[] voDetail, int iKey) {
		UFDouble ufResult = new UFDouble(0);
		if (voDetail == null) {
			return VerifyMsg.ZERO;
		}
		Object oValue = null;
		for (int i = 0; i < voDetail.length; i++) {
			if (!voDetail[i].isSelected().booleanValue()) {
				continue;
			}
			try {
				oValue = voDetail[i].getValue(iKey);
				if (oValue != null) {
					ufResult = ufResult.add((UFDouble) oValue);
				}
			} catch (Exception e) {
				return VerifyMsg.ZERO;
			}
		}
		return ufResult;
	}

	/**
	 * 功能：根据详细log 计算得到汇总vo 作者：宋涛 创建时间：(2003-5-10 15:33:34) 参数：<|> 返回值： 算法：
	 * 
	 * @param detaillogs
	 *            nc.vo.glrp.verify.GlVerifyDisplogVO[]
	 */
	private void calculateSumLogs(GlVerifyDisplogVO[] detaillogs) {
		if (detaillogs == null) {
			return;
		}
		java.util.Hashtable hash = new java.util.Hashtable();
		nc.vo.glrp.verify.GlVerifyDisplogVO vo = null;
		for (int i = 0; i < detaillogs.length; i++) {
			Object oValue = hash.get(detaillogs[i].getBatchid());
			if (oValue == null) {
				vo = new nc.vo.glrp.verify.GlVerifyDisplogVO();
				vo.setBatchid(detaillogs[i].getBatchid());
				vo.setOprDate(detaillogs[i].getOprDate());
				vo.setPk_curr(detaillogs[i].getPk_curr());

			} else {
				vo = (GlVerifyDisplogVO) oValue;
			}
			if (vo.getCredit_b() == null) {
				vo.setCredit_b(detaillogs[i].getCredit_b());
			} else if (detaillogs[i].getCredit_b() != null) {
				vo.setCredit_b(vo.getCredit_b().add(detaillogs[i].getCredit_b()));
			}
			if (vo.getCredit_f() == null) {
				vo.setCredit_f(detaillogs[i].getCredit_f());
			} else if (detaillogs[i].getCredit_f() != null) {
				vo.setCredit_f(vo.getCredit_f().add(detaillogs[i].getCredit_f()));
			}
			if (vo.getCredit_y() == null) {
				vo.setCredit_y(detaillogs[i].getCredit_y());
			} else if (detaillogs[i].getCredit_y() != null) {
				vo.setCredit_y(vo.getCredit_y().add(detaillogs[i].getCredit_y()));
			}
			if (vo.getCredit_j() == null) {
				vo.setCredit_j(detaillogs[i].getCredit_j());
			} else if (detaillogs[i].getCredit_j() != null) {
				vo.setCredit_j(vo.getCredit_j().add(detaillogs[i].getCredit_j()));
			}
			if (vo.getCredit_q() == null) {
				vo.setCredit_q(detaillogs[i].getCredit_q());
			} else if (detaillogs[i].getCredit_q() != null) {
				vo.setCredit_q(vo.getCredit_q().add(detaillogs[i].getCredit_q()));
			}

			if (vo.getDebit_b() == null) {
				vo.setDebit_b(detaillogs[i].getDebit_b());
			} else if (detaillogs[i].getDebit_b() != null) {
				vo.setDebit_b(vo.getDebit_b().add(detaillogs[i].getDebit_b()));
			}
			if (vo.getDebit_f() == null) {
				vo.setDebit_f(detaillogs[i].getDebit_f());
			} else if (detaillogs[i].getDebit_f() != null) {
				vo.setDebit_f(vo.getDebit_f().add(detaillogs[i].getDebit_f()));
			}
			if (vo.getDebit_y() == null) {
				vo.setDebit_y(detaillogs[i].getDebit_y());
			} else if (detaillogs[i].getDebit_y() != null) {
				vo.setDebit_y(vo.getDebit_y().add(detaillogs[i].getDebit_y()));
			}
			if (vo.getDebit_j() == null) {
				vo.setDebit_j(detaillogs[i].getDebit_j());
			} else if (detaillogs[i].getDebit_j() != null) {
				vo.setDebit_j(vo.getDebit_j().add(detaillogs[i].getDebit_j()));
			}
			if (vo.getDebit_q() == null) {
				vo.setDebit_q(detaillogs[i].getDebit_q());
			} else if (detaillogs[i].getDebit_q() != null) {
				vo.setDebit_q(vo.getDebit_q().add(detaillogs[i].getDebit_q()));
			}

			hash.put(detaillogs[i].getBatchid(), vo);
		}
		try {
			java.util.Enumeration em = hash.elements();
			java.util.ArrayList al = new java.util.ArrayList();
			while (em.hasMoreElements()) {
				al.add(em.nextElement());
			}
			if (al.size() > 0) {
				m_sumLogs = new GlVerifyDisplogVO[al.size()];
				m_sumLogs = (GlVerifyDisplogVO[]) al.toArray(m_sumLogs);
			} else {
				m_sumLogs = null;
			}
			if (m_sumLogs != null && m_sumLogs.length > 1) {
				int[] iIndex = new int[] { GlVerifyDispLogKey.OPRDATE, GlVerifyDispLogKey.BATCHID };
				nc.vo.glcom.shellsort.CShellSort objShellSort = new nc.vo.glcom.shellsort.CShellSort();
				nc.vo.glcom.sorttool.CVoSortTool objVoSortTool = new nc.vo.glcom.sorttool.CVoSortTool();
				objVoSortTool.setSortIndex(iIndex);
				objShellSort.sort(m_sumLogs, objVoSortTool, false);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 功能：计算全部数据的合计 作者：卢文广 创建时间：(2004-03-02 15:48:55) 参数：<|> 返回值： 算法： 版本：ncv30--sp1
	 * 
	 * @param bDebit
	 *            boolean
	 */
	private UFDouble calculateSumT(nc.vo.glrp.verify.VerifyDetailVO[] voDetail, int iKey) {
		UFDouble ufResult = new UFDouble(0);
		if (voDetail == null) {
			return VerifyMsg.ZERO;
		}
		Object oValue = null;
		for (int i = 0; i < voDetail.length; i++) {
			// if (!voDetail[i].isSelected().booleanValue()) {
			// continue;
			// }
			try {
				oValue = voDetail[i].getValue(iKey);
				if (oValue != null) {
					ufResult = ufResult.add((UFDouble) oValue);
				}
			} catch (Exception e) {
				return VerifyMsg.ZERO;
			}
		}
		return ufResult;
	}

	/**
	 * 功能：计算未核销数据的合计 作者：卢文广 创建时间：(2004-03-02 15:48:55) 参数：<|> 返回值： 算法： 版本：ncv30--sp1
	 * 
	 * @param bDebit
	 *            boolean
	 */
	private UFDouble calculateSumU(nc.vo.glrp.verify.VerifyDetailVO[] voDetail, int iKey) {
		UFDouble ufResult = new UFDouble(0);
		if (voDetail == null) {
			return VerifyMsg.ZERO;
		}
		Object oValue = null;
		for (int i = 0; i < voDetail.length; i++) {
			if (voDetail[i].isSelected().booleanValue()) {
				switch (iKey) {
				case VerifyDetailKey.DEBIT_UNJS_Y:
				case VerifyDetailKey.CREDIT_UNJS_Y: {
					if (voDetail[i].getDirect().intValue() == 1) {
						if (!voDetail[i].getDebit_Money_Y().equals(voDetail[i].getBalancedebitamount())) {
							ufResult = ufResult.add(voDetail[i].getBalancedebitamount());
							ufResult = ufResult.sub(voDetail[i].getDebit_Money_Y());
						}
					} else {
						if (!voDetail[i].getCredit_Money_Y().equals(voDetail[i].getBalancecreditamount())) {
							ufResult = ufResult.add(voDetail[i].getBalancecreditamount());
							ufResult = ufResult.sub(voDetail[i].getCredit_Money_Y());
						}
					}
					break;
				}
				case VerifyDetailKey.DEBIT_UNJS_F:
//				case VerifyDetailKey.CREDIT_UNJS_F: {
//					if (voDetail[i].getDirect().intValue() == 1) {
//						if (!voDetail[i].getDebit_Money_F().equals(voDetail[i].getBalancefracdebitamount())) {
//							ufResult = ufResult.add(voDetail[i].getBalancefracdebitamount());
//							ufResult = ufResult.sub(voDetail[i].getDebit_Money_F());
//						}
//					} else {
//						if (!voDetail[i].getCredit_Money_F().equals(voDetail[i].getBalancefraccreditamount())) {
//							ufResult = ufResult.add(voDetail[i].getBalancefraccreditamount());
//							ufResult = ufResult.sub(voDetail[i].getCredit_Money_F());
//						}
//					}
//					break;
//				}
				case VerifyDetailKey.DEBIT_UNJS_B:
				case VerifyDetailKey.CREDIT_UNJS_B: {
					if (voDetail[i].getDirect().intValue() == 1) {
						if (!voDetail[i].getDebit_Money_B().equals(voDetail[i].getBalancelocaldebitamount())) {
							ufResult = ufResult.add(voDetail[i].getBalancelocaldebitamount());
							ufResult = ufResult.sub(voDetail[i].getDebit_Money_B());
						}
					} else {
						if (!voDetail[i].getCredit_Money_B().equals(voDetail[i].getBalancelocalcreditamount())) {
							ufResult = ufResult.add(voDetail[i].getBalancelocalcreditamount());
							ufResult = ufResult.sub(voDetail[i].getCredit_Money_B());
						}
					}
					break;
				}
				}
				continue;
			}

			try {
				oValue = voDetail[i].getValue(iKey);
				if (oValue != null) {
					ufResult = ufResult.add((UFDouble) oValue);
				}
			} catch (Exception e) {
				return VerifyMsg.ZERO;
			}
		}
		return ufResult;
	}

	/**
	 * 功能：将数据添加到hash表中 作者：lwg 创建时间：(2004-04-02 11:02:39) 参数：<|> 返回值： 算法： verison:v30
	 * 
	 * @param vos
	 *            nc.vo.glrp.com.verify.IVerifyVO[]
	 * 
	 * 
	 */
	private void fill2Hash(IVerifyVO[] vos, boolean bDebit) throws Exception {
		String[] sKeys = getStandardPk();
		// (bDebit ? m_rulevo.getDebtObjKeys() : m_rulevo.getCreditObjKeys());
		String sCurrKey;
		Object oCurrValues;
		for (int i = 0; i < vos.length; i++) {
			sCurrKey = gethashKeyForRedBlue(vos[i], sKeys);
			// sCurrKey = gethashKey(vos[i], sKeys);
			oCurrValues = getHash().get(sCurrKey);
			if (oCurrValues == null) {
				oCurrValues = new java.util.ArrayList();
			}
			if(isEqual(vos[i], sKeys)){
				((java.util.ArrayList) oCurrValues).add(vos[i]);
				getHash().put(sCurrKey, oCurrValues);
			}
		}
	}
	
	/**
	 * 判断数据的辅助核算与自动红蓝对冲按辅助项中辅助核算是否一致
	 * @param vos 当前待红蓝对冲的数据
	 * @param sKeys 红蓝对冲核销标准
	 * @return
	 * add by suhtb
	 */
	private boolean isEqual(IVerifyVO vo,String[] sKeys){
		boolean result = true;
		VerifyDetailVO vo1 = (VerifyDetailVO)vo;
		if(null!=sKeys&&sKeys.length>0){
			for (int i = 0; i < sKeys.length; i++) {
				if(null != sKeys[i]&&sKeys[i].startsWith("fzhs_")){
					String asstype = sKeys[i].substring(5, 25);
					String assvalue = sKeys[i].substring(25);
					for (int j = 0; j < vo1.getAss().length; j++) {
						if (vo1.getAss()[j].getPk_Checktype().equals(asstype)) {
							if(!"null".equals(assvalue)&&!isExists(vo1.getAss()[j].getPk_Checkvalue(), assvalue)){
								result = false;
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 判断pk_checkvalue是否存在于assvalue中
	 * @param pk_checkvalue 比如pk_checkvalue="100288100000000000K2"
	 * @param assvalue 比如assvalue="100288100000000000K1,100288100000000000K2"
	 * @return
	 * add by suhtb
	 */
	private boolean isExists(String pk_checkvalue,String assvalue){
		String[] tmpArr = assvalue.split(",");
		if(null!=tmpArr&&tmpArr.length>0){
			for(int i=0;i<tmpArr.length;i++){
				if(null!=tmpArr[i]&&tmpArr[i].equals(pk_checkvalue)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-8 10:13:10)
	 * 
	 * @param evt
	 *            java.beans.PropertyChangeEvent
	 */
	public void firePropertyChange(java.beans.PropertyChangeEvent evt) {
		this.listener.firePropertyChange(evt);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-8 10:24:25)
	 * 
	 * @param propertyName
	 *            java.lang.String
	 * @param oldValue
	 *            java.lang.Object
	 * @param newValue
	 *            java.lang.Object
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		this.listener.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-9 15:41:27) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	public nc.vo.glrp.verify.VerifyDetailVO[] getCreditVerify() {
		return m_CreditVerify;
	}

	/**
	 * 功能：得到model中的数据 作者：宋涛 创建时间：(2003-5-9 10:56:41) 参数：<|>true 借方，false 贷方 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO[]
	 * @param bDebit
	 *            boolean
	 */
	public nc.vo.glrp.verify.VerifyDetailVO[] getData(boolean bDebit) {
		if (bDebit) {
			return getDebitVerify();
		} else {
			return getCreditVerify();
		}
	}

	/**
	 * 功能： 作者：宋涛 创建时间：(2003-5-9 15:38:51) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	public nc.vo.glrp.verify.VerifyDetailVO[] getDebitVerify() {
		return m_DebitVerify;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-12 20:50:18)
	 */
	private nc.ui.glcom.numbertool.GlCurrAmountFormat getFormater() {
		if (m_Formater == null)
			m_Formater = new nc.ui.glcom.numbertool.GlCurrAmountFormat();
		return m_Formater;
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-13 15:21:41) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.ui.glverifycom.ShowStyleTool
	 */
	private nc.ui.glverifycom.ShowStyleTool getGLTool() {
		if (m_GLTool == null) {
			m_GLTool = new ShowStyleTool();
		}
		return m_GLTool;
	}

	/**
	 * a功能： 作者：lwg 创建时间：(2004-04-02 11:12:49) 参数：<|> 返回值： 算法：
	 * 
	 * @return java.util.Hashtable
	 */
	private java.util.Hashtable getHash() {
		if (m_hash == null) {
			m_hash = new java.util.Hashtable();
		}
		return m_hash;
	}

	/**
	 * 功能：得到hash键值 作者：lwg 创建时间：(2004-04-02 10:44:49) 参数：<|> 返回值： 算法： verison:v30
	 * 
	 * @return java.lang.String
	 * @param verifyvo
	 *            nc.vo.glrp.com.verify.IVerifyVO
	 */
	private String gethashKey(IVerifyVO verifyvo, String[] skeys) {
		String shashKey = "";
		if (skeys == null || skeys.length <= 0) {
			return "null";
		}
		Object ob = null;
		for (int i = 0; i < skeys.length; i++) {
			if (skeys[i].equals("Mny")) {
				if (verifyvo.getDirect().intValue() > 0) { /* 借方 */
					ob = verifyvo.getDebit_Money_Y();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
//					ob = verifyvo.getDebit_Money_F();
//					shashKey += "_" + (ob == null ? "null" : ob.toString());
					ob = verifyvo.getDebit_Money_B();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
				} else { /* 贷方 */
					ob = verifyvo.getCredit_Money_Y();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
//					ob = verifyvo.getCredit_Money_F();
//					shashKey += "_" + (ob == null ? "null" : ob.toString());
					ob = verifyvo.getCredit_Money_B();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
				}
			} else {
				ob = verifyvo.getAttributeValue(skeys[i]);
				shashKey += "_" + (ob == null ? "null" : ob.toString());
			}

		}
		return shashKey;
	}

	private String gethashKeyForRedBlue(IVerifyVO verifyvo, String[] skeys) {
		String shashKey = "";
		if (skeys == null || skeys.length <= 0) {
			return "null";
		}
		Object ob = null;
		for (int i = 0; i < skeys.length; i++) {
			if (skeys[i].equals("Mny")) {
				if (verifyvo.getDirect().intValue() > 0) { /* 借方 */
					ob = verifyvo.getDebit_Money_Y().abs();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
//					ob = verifyvo.getDebit_Money_F().abs();
//					shashKey += "_" + (ob == null ? "null" : ob.toString());
					ob = verifyvo.getDebit_Money_B().abs();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
				} else { /* 贷方 */
					ob = verifyvo.getCredit_Money_Y().abs();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
//					ob = verifyvo.getCredit_Money_F().abs();
//					shashKey += "_" + (ob == null ? "null" : ob.toString());
					ob = verifyvo.getCredit_Money_B().abs();
					shashKey += "_" + (ob == null ? "null" : ob.toString());
				}
			} else {
				ob = verifyvo.getAttributeValue(skeys[i]);
				shashKey += "_" + (ob == null ? "null" : ob.toString());
			}

		}
		return shashKey;
	}

	/**
	 * 功能：得到历史查询条件 作者：宋涛 创建时间：(2003-5-19 20:45:00) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.LogFilterCondVO
	 */
	public nc.vo.glrp.verify.LogFilterCondVO getHistoryCond() {
		return m_historyCondvo;
	}

	/**
	 * 功能： 作者：宋涛 创建时间：(2003-5-9 11:55:36) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 */
	public boolean getHistoryState() {
		return m_bSum;
	}

	/**
	 * 功能：取得历史纪录中的数据 作者：宋涛 创建时间：(2003-5-9 10:08:41) 参数：<|> 返回值： 算法：
	 * 
	 * @return java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @param bDebit
	 *            boolean
	 */
	public Object getHistoryValueAt(int iRow, int iKey, int iStyle) {
		GlVerifyDisplogVO vo = null;
		Object objtemp = null;
		try {
			if (getHistoryState()) {
				vo = m_sumLogs[iRow];
			} else {
				vo = m_selectedDetailLogs[iRow];
			}
			objtemp = vo.getValue(iKey);
			if (objtemp != null && objtemp.toString().trim().length() > 0) {
				/* 辅助核算的显示 */
				if (iKey == GlVerifyDispLogKey.DEBIT_ASSVO || iKey == GlVerifyDispLogKey.CREDIT_ASSVO) {
					objtemp = ShowStyleTool.getShowAss(
					// ClientInfo.getInstance().getPk_corp(),
							getPk_accountingbook(), (nc.vo.glcom.ass.AssVO[]) objtemp);
				}
				if(iKey == GlVerifyDispLogKey.DEBIT_VOUCHERDATE || iKey == GlVerifyDispLogKey.CREDIT_VOUCHERDATE
						||iKey == GlVerifyDispLogKey.CREDIT_BUSINESSDATE || iKey == GlVerifyDispLogKey.DEBIT_BUSINESSDATE
						||iKey == GlVerifyDispLogKey.OPRDATE){
					objtemp = objtemp==null?null:DataFormatUtilGL.formatDate(objtemp).getValue();
				}
				switch (iStyle) {
				case IGlVerifyConst.STYLE_QUANTITY: // 数量
					objtemp = getNumberFormater().format(((UFDouble) objtemp).toDouble(), getQuantityDigit().intValue());
					break;
				case IGlVerifyConst.STYLE_AMOUNT: // 原币
					Object currtype = vo.getValue(GlVerifyDispLogKey.PK_CURR);
					if (currtype == null)
						break;
					try {
						objtemp = getFormater().formatAmount((UFDouble) objtemp, currtype.toString());
					} catch (Exception errr) {
						objtemp = getFormater().formatAmount((UFDouble) objtemp, Currency.getLocalCurrPK(getPk_accountingbook()));
					}
					break;
				case 68: // 辅币
					if (!ClientInfo.getInstance(getPk_accountingbook()).bFracUsed())
						break;
					objtemp = getFormater().formatAmount((UFDouble) objtemp,
							ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
					break;
				case IGlVerifyConst.STYLE_LOCAL_AMOUNT: // 组织本币
					if (Currency.getLocalCurrPK(getPk_accountingbook()) == null)
						break;
					objtemp = getFormater().formatAmount((UFDouble) objtemp, Currency.getLocalCurrPK(getPk_accountingbook()));
					break;
				case IGlVerifyConst.STYLE_BOOLEAN: // boolean
					if (((UFBoolean) objtemp).booleanValue() == true) {
						return VerifyMsg.YES;
					} else {
						return VerifyMsg.NO;
					}
//				case IGlVerifyConst.STYLE_GROUP_AMOUNT: // 集团本币
//					if (Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()) != null) {
//						objtemp = getFormater().formatAmount((UFDouble) objtemp, Currency.getLocalCurrPK(getPk_accountingbook()));
//					}
//					break;
//				case IGlVerifyConst.STYLE_GLOBAL_AMOUNT: // 全局本币
//					if (Currency.getGlobalCurrPk(null) != null) {
//						objtemp = getFormater().formatAmount((UFDouble) objtemp, Currency.getLocalCurrPK(getPk_accountingbook()));
//					}
//					break;
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
		return objtemp;
	}

	/**
	 * 功能：得到历史纪录数量 作者：宋涛 创建时间：(2003-5-9 11:50:30) 参数：true 汇总状态，false 详细状态 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.GlVerifyDisplogVO[]
	 * @param bState
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public int getHistroyDataCount() throws java.lang.Exception {
		try {
			if (getHistoryState()) {
				return m_sumLogs == null ? 0 : m_sumLogs.length;
			} else {
				return m_selectedDetailLogs == null ? 0 : m_selectedDetailLogs.length;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-13 14:29:47)
	 * 
	 * @return nc.ui.glcom.numbertool.GlNumberFormat
	 */
	private nc.ui.glcom.numbertool.GlNumberFormat getNumberFormater() {
		return m_NumberFormater;
	}

	/**
	 * 功能：得到凭证过滤查询条件vo 作者：宋涛 创建时间：(2003-5-19 19:35:50) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.FilterCondVO
	 */
	public nc.vo.glrp.verify.FilterCondVO getQryVo() {
		return m_voQry;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-12 20:54:58)
	 * 
	 * @return java.lang.Integer
	 */
	private java.lang.Integer getQuantityDigit() throws Exception {
		if (m_QuantityDigit == null) {
			try {
				m_QuantityDigit = GLPubProxy.getRemoteGlPara().getQuantityDigit(
						ClientInfo.getInstance(getPk_accountingbook()).getPk_corp());
			} catch (Exception e) {
				m_QuantityDigit = new Integer(2);
			}
		}
		return m_QuantityDigit;
	}

	/**
	 * 功能：生成核销规则vo 作者：宋涛 创建时间：(2003-5-9 14:05:39) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.com.verify.VerifyRuleVO
	 */
	private nc.vo.glrp.com.verify.VerifyRuleVO getRulevo() {
		if (m_rulevo == null) {
			m_rulevo = new nc.vo.glrp.com.verify.VerifyRuleVO();
			m_rulevo.setLogClassName(LOGNAME);
			m_rulevo.setDateName(m_sDateName);
			m_rulevo.setSimVerify(nc.vo.pub.lang.UFBoolean.FALSE);
			m_rulevo.setVerifySeq(UFBoolean.valueOf(ClientInfo.getInstance(getPk_accountingbook()).getFarthest()));
			m_rulevo.setFracInuse(UFBoolean.valueOf(ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()));
			m_rulevo.sethasAgio(nc.vo.pub.lang.UFBoolean.FALSE);
			m_rulevo.setStrictMatch(nc.vo.pub.lang.UFBoolean.TRUE);
			m_rulevo.setSortAscend(UFBoolean.valueOf(ClientInfo.getInstance(getPk_accountingbook()).getFarthest()));
			m_rulevo.setMultiMnyVerify(nc.vo.pub.lang.UFBoolean.FALSE);
			try {
				m_rulevo.setBatchId(GLPubProxy.getRemoteVerifyLog().getVerifyBatchid());
				m_rulevo.setPairNum(new Integer(0));
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return m_rulevo;
	}

	/**
	 * 功能：取得选择的贷方数据 作者：宋涛 创建时间：(2003-5-10 11:13:18) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	public VerifyDetailVO[] getSelectedCredit() {
		VerifyDetailVO[] voDetail = getCreditVerify();
		if (voDetail == null) {
			return null;
		}
		java.util.ArrayList al = new java.util.ArrayList();
		int iLeng = voDetail.length;
		for (int i = 0; i < iLeng; i++) {
			if (voDetail[i] != null && voDetail[i].isSelected().booleanValue()) {
				al.add(voDetail[i]);
			}
		}
		if (al.size() > 0) {
			VerifyDetailVO[] vos = new VerifyDetailVO[al.size()];
			vos = (VerifyDetailVO[]) al.toArray(vos);
			return vos;
		}
		return null;
	}

	/**
	 * 功能：取得有选择标志的借方数据 作者：宋涛 创建时间：(2003-5-10 11:12:28) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	public VerifyDetailVO[] getSelectedDebit() {
		VerifyDetailVO[] voDetail = getDebitVerify();
		if (voDetail == null) {
			return null;
		}
		java.util.ArrayList al = new java.util.ArrayList();
		int iLeng = voDetail.length;
		for (int i = 0; i < iLeng; i++) {
			if (voDetail[i] != null && voDetail[i].isSelected().booleanValue()) {
				al.add(voDetail[i]);
			}
		}
		if (al.size() > 0) {
			VerifyDetailVO[] vos = new VerifyDetailVO[al.size()];
			vos = (VerifyDetailVO[]) al.toArray(vos);
			return vos;
		}
		return null;
	}

	/**
	 * 功能：得到选中的log记录 作者：宋涛 创建时间：(2003-5-10 15:44:29) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.GlVerifyDisplogVO[]
	 */
	public GlVerifyDisplogVO[] getSelectedLogs() {
		if (m_detailLogs == null || m_sumLogs == null) {
			return null;
		}
		java.util.ArrayList al = new java.util.ArrayList();
		if (getHistoryState()) { /* 汇总状态 */
			for (int i = 0; i < m_detailLogs.length; i++) {
				if (m_hashSelectedSumLogs.get(m_detailLogs[i].getBatchid()) != null) {
					m_detailLogs[i].setbSelected(VerifyMsg.TRUE);
					al.add(m_detailLogs[i]);
				}
			}
		} else { /* 明细状态 */
			for (int i = 0; i < m_detailLogs.length; i++) {
				if (m_hashSelectedSumLogs.get(m_detailLogs[i].getBatchid()) == null) {
					m_detailLogs[i].setbSelected(VerifyMsg.FALSE);
				}
				if (m_detailLogs[i].getbSelected().booleanValue()) {
					al.add(m_detailLogs[i]);
				}
			}
		}
		if (al.size() > 0) {
			GlVerifyDisplogVO[] vos = new GlVerifyDisplogVO[al.size()];
			vos = (GlVerifyDisplogVO[]) al.toArray(vos);
			return vos;
		}
		return null;
	}

	/**
	 * 功能：得到自动红蓝对冲的标准 作者：lwg 创建时间：(2004-04-02 16:19:33) 参数：<|> 返回值： 算法： version:v30
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	private String[] getStandardPk() throws Exception {
		java.util.ArrayList al = new java.util.ArrayList();
		// chengsc
		String[] args = m_Standardvo.getObj();
		for (int i = 0; i < args.length; i++) {
			al.add(args[i]);
		}
		// al.add("VerifyNo");
		// al.add("pk_accsubj");

		VerifyObjVO verifyObj = getVerifyObject();
		if (verifyObj == null || verifyObj.getParentVO() == null) {
			throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_NO_VERIFYOBJ());
		}
		VerifyObjHeaderVO voHead = (VerifyObjHeaderVO) verifyObj.getParentVO();
		if (voHead.getBcontrol().booleanValue()) { /* 严格按照辅助核算控制 */
			VerifyObjItemVO[] items = (VerifyObjItemVO[]) verifyObj.getChildrenVO();
			String pk_bdinfo = null;
			for (int i = 0; i < items.length; i++) {
				pk_bdinfo = items[i].getPk_subjass();
				al.add("control_" + pk_bdinfo);
			}
		}
		// 如果不严格控制看看是否选择了严格按照辅助核算控制的标准
		else {
			if (this.m_Standardvo.getObj() != null) {
				String[] fzItems = this.m_Standardvo.getObj();
				for (int i = 0; i < fzItems.length; i++) {
					if (fzItems[i].startsWith("fzhs")) {
						al.add(fzItems[i].trim());
					}
				}
			}
		}

		if (al.size() > 0) {
			String[] sItems = new String[al.size()];
			al.toArray(sItems);
			return sItems;
		} else {
			return null;
		}
	}
	
	private static String getPKToName(String docPk, String mid) {
		if (docPk == null) {
			return docPk;
		}
		IGeneralAccessor accessor = GeneralAccessorFactory.getAccessor(mid);
		IBDData bddata = accessor.getDocByPk(docPk);
		if (bddata!=null) {
			return bddata.getName().toString();
		}
		return null;
	}

	/**
	 * 功能：取得凭证分录中的数据 作者：宋涛 创建时间：(2003-5-9 10:08:41) 参数：<|> 返回值： 算法：
	 * 
	 * @return java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @param bDebit
	 *            boolean
	 */
	public Object getValueAt(int iRow, int iKey, int iStyle, boolean bDebit) {
		nc.vo.glrp.verify.VerifyDetailVO vo = null;
		Object objtemp = null;
		try {
			if (bDebit) {
				vo = getDebitVerify()[iRow];
			} else {
				vo = getCreditVerify()[iRow];
			}
			objtemp = vo.getValue(iKey);
			if (objtemp != null) {
				if(iKey == VerifyDetailKey.BUSINESSDATE){
					objtemp=vo.getValue(VerifyDetailKey.BUSINESSDATE)==null?null:DataFormatUtilGL.formatDate(vo.getValue(VerifyDetailKey.BUSINESSDATE)).getValue();
				}
				if(iKey == VerifyDetailKey.VOUCHDATE){
					objtemp=vo.getValue(VerifyDetailKey.VOUCHDATE)==null?null:DataFormatUtilGL.formatDate(vo.getValue(VerifyDetailKey.VOUCHDATE)).getValue();
				}
				if (iKey == VerifyDetailKey.ASSVO) {
					objtemp = nc.ui.glverifycom.ShowStyleTool.getShowAss(getPk_accountingbook()/**
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * ClientInfo.getInstance().getPk_corp()
					 */
					, (nc.vo.glcom.ass.AssVO[]) objtemp);
				}else if(iKey == VerifyDetailKey.DISP_VOUCHERNO) {
					String namestr = getPKToName((String)vo.getValue(VerifyDetailKey.VOUCHERTYPE), IBDMetaDataIDConst.VOUCHERTYPE);
					if (namestr!=null) {
						return namestr+"_"+objtemp;
					}
				}
				switch (iStyle) {
				case IGlVerifyConst.STYLE_QUANTITY: // 数量
					objtemp = getNumberFormater().format(((UFDouble) objtemp).toDouble(), getQuantityDigit().intValue());
					break;
				case IGlVerifyConst.STYLE_AMOUNT: // 原币
					Object currtype = vo.getValue(VerifyDetailKey.CURPK);
					if (currtype != null) {
						try {
							objtemp = getFormater().formatAmount(((UFDouble) objtemp), currtype.toString());
						} catch (Exception errr) {
							objtemp = getFormater().formatAmount(((UFDouble) objtemp),
									Currency.getLocalCurrPK(getPk_accountingbook()));
						}
					}
					break;
				// case 68: // 辅币
				// if (!ClientInfo.getInstance().bFracUsed())
				// break;
				// objtemp = getFormater().formatAmount(((UFDouble) objtemp), ClientInfo.getInstance().getPk_fracpk());
				// break;
				case IGlVerifyConst.STYLE_LOCAL_AMOUNT: // 本币
					if (Currency.getLocalCurrPK(getPk_accountingbook()) != null) {
						objtemp = getFormater().formatAmount(((UFDouble) objtemp),
								Currency.getLocalCurrPK(getPk_accountingbook()));
					}
					break;
				case IGlVerifyConst.STYLE_BOOLEAN:// boolean
					if (((UFBoolean) objtemp).booleanValue() == true) {
						return VerifyMsg.YES;
					} else {
						return VerifyMsg.NO;
					}
//				case IGlVerifyConst.STYLE_GROUP_AMOUNT:
//					if (Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()) != null) {
//						objtemp = getFormater().formatAmount(((UFDouble) objtemp),
//								Currency.getLocalCurrPK(getPk_accountingbook()));
//					}
//					break;
//				case IGlVerifyConst.STYLE_GLOBAL_AMOUNT:
//					if (Currency.getGlobalCurrPk(null) != null) {
//						objtemp = getFormater().formatAmount(((UFDouble) objtemp), Currency.getGlobalCurrPk(null));
//					}
//					break;
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return null;
		}
		return objtemp;
	}

	/**
	 * 功能：得到核销组件 作者：宋涛 创建时间：(2003-5-9 14:37:20) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.com.verify.VerifyCom
	 */
	private nc.vo.glrp.com.verify.VerifyCom getVerifyCom() {
		if (m_VerifyCom == null) {
			try {
				m_VerifyCom = new nc.vo.glrp.com.verify.VerifyCom();
				m_VerifyCom.setDataFilter(new GlVerifyDataFilter());
				m_VerifyCom.setDataSaver(new GlVerifyDataSaver());
				m_VerifyCom.addMatchTools(REDBLUE, new GLRedBlueMatch());
				m_VerifyCom.addMatchTools(VERIFY, new GLSingleMatch());
				m_VerifyCom.setRule(getRulevo());
				m_VerifyCom.setPk_accountingbook(getPk_accountingbook());
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return m_VerifyCom;
	}

	/**
	 * 功能：根据凭证分录主键得到核销凭证分录 作者：宋涛 创建时间：(2003-5-14 11:38:55) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO
	 * @param pk_detail
	 *            java.lang.String
	 */
	public nc.vo.glrp.verify.VerifyDetailVO getVerifyDetailByDetailpk(String pk_detail) {
		if (pk_detail == null) {
			m_voDetail = null;
			return null;
		}
		try {
			m_voDetail = GLPubProxy.getRemoteVerifydetail().getVerifyDetailbyDetailpk(pk_detail);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			m_voDetail = null;
		}
		return m_voDetail;
	}

	public void setVerifyDetailVO(nc.vo.glrp.verify.VerifyDetailVO detail) {
		m_voDetail = detail;
	}

	public void setVerifyObject(nc.vo.glrp.verifyobj.VerifyObjVO verifyobj) {
		m_verifyObj = verifyobj;
	}

	/**
	 * 功能：得到与总账凭证分录对应的核销凭证分录 作者：宋涛 创建时间：(2003-5-14 11:38:55) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO
	 * @param pk_detail
	 *            java.lang.String
	 */
	public nc.vo.glrp.verify.VerifyDetailVO getVerifyDetailvo() {
		return m_voDetail;
	}

	/**
	 * 功能：得到核销对象 作者：宋涛 创建时间：(2003-5-10 16:19:33) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	public nc.vo.glrp.verifyobj.VerifyObjVO getVerifyObject() {
		return m_verifyObj;
	}

	/**
	 * 功能：根据科目主键得到对应的核销对象 作者：宋涛 创建时间：(2003-5-10 16:19:33) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 * @param pk_subj
	 *            java.lang.String
	 * @param assvos
	 *            nc.vo.glcom.ass.AssVO[]
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* (non-Javadoc)
	 * @see nc.ui.glrp.verify.IVerifyModel#getVerifyObject(java.lang.String)
	 */
	public nc.vo.glrp.verifyobj.VerifyObjVO getVerifyObject(String pk_subj) throws java.lang.Exception {

		try {
			if (pk_subj == null || pk_subj.length() <= 0) {
				m_verifyObj = null;
			} else {
				// AccountVO[] vos =
				// getGLTool().getAllUpSubjectVO(ClientInfo.getInstance().getPk_corp(),
				// pk_subj);
				String[] subj_pks = VerifyTool.getInstance().getUpLevelSubjPKs(getPk_accountingbook(), pk_subj);
				if (subj_pks != null) {
					for (int i = 0; i < subj_pks.length; i++) {
						m_verifyObj = ClientInfo.getInstance(getPk_accountingbook()).getVerifyObjectByPk_subj(subj_pks[i]);
						if (m_verifyObj != null) {
							afterGetVerifyObj();
							return m_verifyObj;
						}
					}
				} else {
					throw new Exception();
				}
				ObjQryVO qryVO = new ObjQryVO();
				qryVO.setPk_glorgbook(getPk_accountingbook());
				qryVO.setPk_corp(ClientInfo.getInstance(getPk_accountingbook()).getPk_corp());
				qryVO.setPk_sob(ClientInfo.getInstance(getPk_accountingbook()).getPk_Sob());
				qryVO.setPk_accSubjs(subj_pks);
				// if (vos != null) {
				// String[] pks = new String[1 + vos.length];
				// pks[0] = pk_subj;
				// for (int i = 0; i < vos.length; i++) {
				// pks[i + 1] = vos[i].getPk_accsubj();
				// }
				// }
				m_verifyObj = GLPubProxy.getRemoteVerifyobj().findObjbyCond(qryVO);
				if (m_verifyObj == null || m_verifyObj.getParentVO() == null) {
					m_verifyObj = null;
					return null;
				}
				/* 向核销对象中添加科目编码和名称信息 */
				AccountVO voSubj = AccsubjDataCache.getInstance().getAccountVOByPK(
						((VerifyObjHeaderVO) m_verifyObj.getParentVO()).getPk_accsubj());
				((VerifyObjHeaderVO) m_verifyObj.getParentVO()).setAccSubjCode(voSubj.getCode());
				((VerifyObjHeaderVO) m_verifyObj.getParentVO()).setAccSubjName(voSubj.getName());
				ClientInfo.getInstance(getPk_accountingbook()).putVerifyObject(pk_subj, m_verifyObj);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			m_verifyObj = null;
			throw e;
		}
		afterGetVerifyObj();
		return m_verifyObj;
	}

	/**
	 * 功能：得到核销对象主键 作者：宋涛 创建时间：(2003-5-10 16:19:33) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	private String getVerifyObjectPk() {
		try {
			return m_verifyObj.getParentVO().getPrimaryKey();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-10 10:17:47) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.ui.glrp.verify.IVerifyUI
	 */
	private IVerifyUI getVerifyUI() {
		return m_ui;
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// m_sDateName = ClientInfo.getInstance().getDateName();
			String defaultbook = GlWorkBench.getDefaultMainOrg();
			if (defaultbook != null && !"".equals(defaultbook)) {
				m_sDateName = GLParaAccessor.getVerifyDate(defaultbook);
			} else {
				m_sDateName = "";
			}

			// if (m_sDateName.equalsIgnoreCase("VoucherDate")) {
			if (m_sDateName.equalsIgnoreCase(GLParaValueConst.GL080_VOUCHERDATE)) {
				m_sDateName = "prepareddate";
			} else {
				m_sDateName = "Businessdate";
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-5-12 22:52:23)
	 */
	private void initSum() throws Exception {
		try {
			m_debitSum_Y = VerifyMsg.ZERO;
			m_debitSum_F = VerifyMsg.ZERO;
			m_debitSum_B = VerifyMsg.ZERO;
			m_debitSum_J = VerifyMsg.ZERO;
			m_debitSum_Q = VerifyMsg.ZERO;
			m_creditSum_Y = VerifyMsg.ZERO;
			m_creditSum_F = VerifyMsg.ZERO;
			m_creditSum_B = VerifyMsg.ZERO;
			m_creditSum_J = VerifyMsg.ZERO;
			m_creditSum_Q = VerifyMsg.ZERO;
			String sSum = getFormater().formatAmount(VerifyMsg.ZERO, Currency.getLocalCurrPK(getPk_accountingbook()));

			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_B), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_F), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Y), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_J), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Q), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_B), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_F), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Y), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_J), null, sSum);
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Q), null, sSum);

			/* add by lwg */
			String curPk = (m_voQry.getDebitCond() == null || m_voQry.getDebitCond().getCurPk() == null) ? m_voQry
					.getCreditCond().getCurPk() : m_voQry.getDebitCond().getCurPk();
			// 借
			m_debitSumT_Y = calculateSumT(getDebitVerify(), VerifyDetailKey.DEBIT_MNY_Y);
			String sSumT_Y = getFormater().formatAmount(m_debitSumT_Y, curPk);
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMT_Y), null, sSumT_Y);

//			m_debitSumT_F = calculateSumT(getDebitVerify(), VerifyDetailKey.DEBIT_JS_F);
//			m_debitSumT_B = calculateSumT(getDebitVerify(), VerifyDetailKey.DEBIT_JS_B);
//			m_debitSumT_J = calculateSumT(getDebitVerify(), VerifyDetailKey.DEBIT_JS_J);
//			m_debitSumT_Q = calculateSumT(getDebitVerify(), VerifyDetailKey.DEBIT_JS_Q);
//
//			m_creditSumT_Y = calculateSumT(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Y);
//			m_creditSumT_F = calculateSumT(getCreditVerify(), VerifyDetailKey.CREDIT_JS_F);
//			m_creditSumT_B = calculateSumT(getCreditVerify(), VerifyDetailKey.CREDIT_JS_B);
//			m_creditSumT_J = calculateSumT(getCreditVerify(), VerifyDetailKey.CREDIT_JS_J);
//			m_creditSumT_Q = calculateSumT(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Q);
			// 格式化
			m_debitSumU_Y = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_Y);
			String sSumU_Y = getFormater().formatAmount(m_debitSumU_Y, curPk);
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_Y), null, sSumU_Y);

			// m_debitSumU_F = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_F);
			// String sSumU_F = null;
			// if (ClientInfo.getInstance().bFracUsed() && ClientInfo.getInstance().getPk_fracpk() != null) {
			// sSumU_F = getFormater().formatAmount(m_debitSumU_F, ClientInfo.getInstance().getPk_fracpk());
			// } else {
			// sSumU_F = m_debitSumU_F.toString();
			// }
			// firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_F), null, sSumU_F);

			String sSumU_B = null;
			m_debitSumU_B = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_B);
			if (Currency.getLocalCurrPK(getPk_accountingbook()) != null) {
				sSumU_B = getFormater().formatAmount(m_debitSumU_B, Currency.getLocalCurrPK(getPk_accountingbook()));
			} else {
				sSumU_B = m_debitSumU_B.toString();
			}
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_B), null, sSumU_B);

			String sSumU_J = null;
			m_debitSumU_J = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_J);
			if (Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()) != null) {
				sSumU_J = getFormater().formatAmount(m_debitSumU_B, Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()));
			} else {
				sSumU_J = m_debitSumU_B.toString();
			}
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_J), null, sSumU_J);

			String sSumU_Q = null;
			m_debitSumU_Q = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_Q);

			if (Currency.getGlobalCurrPk(null) != null) {
				sSumU_Q = getFormater().formatAmount(m_debitSumU_Q, Currency.getGlobalCurrPk(null));
			} else {
				sSumU_Q = m_debitSumU_Q.toString();
			}
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_Q), null, sSumU_Q);

			// 贷
			m_creditSumU_Y = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_Y);
			String sCreditSumU_Y = getFormater().formatAmount(m_creditSumU_Y, curPk);
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_Y), null, sCreditSumU_Y);

			// String sCreditSumU_F = null;
			// m_creditSumU_F = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_F);
			// if (ClientInfo.getInstance().bFracUsed() && ClientInfo.getInstance().getPk_fracpk() != null) {
			// sCreditSumU_F = getFormater().formatAmount(m_creditSumU_F, ClientInfo.getInstance().getPk_fracpk());
			// } else {
			// sCreditSumU_F = m_creditSumU_F.toString();
			// }
			// firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_F), null, sCreditSumU_F);

			String sCreditSumU_B = null;
			m_creditSumU_B = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_B);
			if (Currency.getLocalCurrPK(getPk_accountingbook()) != null) {
				sCreditSumU_B = getFormater().formatAmount(m_creditSumU_B, Currency.getLocalCurrPK(getPk_accountingbook()));
			} else {
				sCreditSumU_B = m_creditSumU_B.toString();
			}
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_B), null, sCreditSumU_B);

			String sCreditSumU_J = null;
			m_creditSumU_J = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_J);
			if (Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()) != null) {
				sCreditSumU_J = getFormater().formatAmount(m_creditSumU_J, Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()));
			} else {
				sCreditSumU_J = m_creditSumU_J.toString();
			}
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_J), null, sCreditSumU_J);

			String sCreditSumU_Q = null;
			m_creditSumU_Q = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_Q);
			if (Currency.getGlobalCurrPk(null) != null) {
				sCreditSumU_Q = getFormater().formatAmount(m_creditSumU_Q, Currency.getGlobalCurrPk(null));
			} else {
				sCreditSumU_Q = m_creditSumU_Q.toString();
			}
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_Q), null, sCreditSumU_Q);

		} catch (Exception e) {
			// Logger.error(e.getMessage(), e);
			// throw e;
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 功能：判断当前辅助核算组合是否符合核销对象要求 作者：宋涛 创建时间：(2003-5-14 14:28:26) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param voDetail
	 *            nc.bs.glrp.verify.VerifyDetail
	 * @param verifyObj
	 *            nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	public boolean isAssLegal(nc.vo.glcom.ass.AssVO[] assvo, nc.vo.glrp.verifyobj.VerifyObjVO verifyObj) throws Exception {
		try {
			if (verifyObj == null || verifyObj.getParentVO() == null) {
				throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_NO_VERIFYOBJ());
				// return false;
			}
			VerifyObjHeaderVO voHead = (VerifyObjHeaderVO) verifyObj.getParentVO();
			if (!voHead.getBcontrol().booleanValue()) {
				return true;
			}
			if (assvo == null) {
				return false;
			}

			if (voHead.getBcontrol().booleanValue()) { /* 严格按照辅助核算控制 */
				VerifyObjItemVO[] items = (VerifyObjItemVO[]) verifyObj.getChildrenVO();
				if (assvo == null || assvo.length < items.length) {
					return false;
				} else {
					String pk_bdinfo = null;
					for (int i = 0; i < items.length; i++) {
						pk_bdinfo = items[i].getPk_subjass();
						int j = 0;
						for (j = 0; j < assvo.length; j++) {
							if (assvo[j].getPk_Checktype().equals(pk_bdinfo))
								break;
						}
						if (j == assvo.length) {
							return false;
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 功能：判断当前凭证分录是否符合条件参加核销 作者：宋涛 创建时间：(2003-5-14 14:28:26) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param voDetail
	 *            nc.bs.glrp.verify.VerifyDetail
	 * @param verifyObj
	 *            nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	public boolean isDataLegal(nc.vo.glrp.verify.VerifyDetailVO voDetail, nc.vo.glrp.verifyobj.VerifyObjVO verifyObj)
			throws Exception {
		try {
			if (voDetail == null || verifyObj == null) {
				return false;
			}
			VerifyObjHeaderVO voHead = (VerifyObjHeaderVO) verifyObj.getParentVO();
			if (voHead.getBcontrol().booleanValue()) { /* 严格按照辅助核算控制 */
				VerifyObjItemVO[] items = (VerifyObjItemVO[]) verifyObj.getChildrenVO();
				nc.vo.glcom.ass.AssVO[] assvo = voDetail.getAss();
				if (assvo == null || assvo.length < items.length) {
					return false;
				} else {
					String pk_bdinfo = null;
					for (int i = 0; i < items.length; i++) {
						pk_bdinfo = items[i].getPk_subjass();
						int j = 0;
						for (j = 0; j < assvo.length; j++) {
							if (assvo[j].getPk_Checktype().equals(pk_bdinfo))
								break;
						}
						if (j == assvo.length) {
							return false;
						}
					}
				}
			}
			if (voDetail.getDirect().intValue() > 0) { /* 借方 */
				if (voDetail.getBalancedebitamount() != null && !voDetail.getBalancedebitamount().equals(VerifyMsg.ZERO)) {
					return true;
				} else if (voDetail.getBalancefracdebitamount() != null
						&& !voDetail.getBalancefracdebitamount().equals(VerifyMsg.ZERO)) {
					return true;
				} else if (voDetail.getBalancelocaldebitamount() != null
						&& !voDetail.getBalancelocaldebitamount().equals(VerifyMsg.ZERO)) {
					return true;
				}
			} else { /* 贷方 */
				if (voDetail.getBalancecreditamount() != null && !voDetail.getBalancecreditamount().equals(VerifyMsg.ZERO)) {
					return true;
				} else if (voDetail.getBalancefraccreditamount() != null
						&& !voDetail.getBalancefraccreditamount().equals(VerifyMsg.ZERO)) {
					return true;
				} else if (voDetail.getBalancelocalcreditamount() != null
						&& !voDetail.getBalancelocalcreditamount().equals(VerifyMsg.ZERO)) {
					return true;
				}
			}
			return true;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 功能：判断金额数组是否相等 作者：宋涛 创建时间：(2003-5-10 11:17:50) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param debit
	 *            nc.vo.glrp.verify.VerifyDetailVO[]
	 * @param credit
	 *            nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	private boolean isEqual(UFDouble[] debit, UFDouble[] credit) {
		if (debit == null || credit == null || debit.length != credit.length) {
			return false;
		}
		for (int i = 0; i < debit.length; i++) {
			if (debit[i] == null && credit[i] == null) {
				continue;
			} else if (debit[i] != null && credit[i] != null) {
				if (debit[i].compareTo(credit[i]) == 0) {
					continue;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 功能：当前选择的数据是否合法， 判断核销对象按照辅助核算严格匹配的情况下， 是否选择了多个不同的取值 作者：宋涛 创建时间：(2003-6-3 12:21:44) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 */
	private boolean isSelectedDataLegal(VerifyDetailVO voDetail) throws Exception {
		VerifyObjVO verifyObj = getVerifyObject();
		if (verifyObj == null || verifyObj.getParentVO() == null) {
			throw new Exception(nc.vo.glrp.pub.VerifyMsg.getMSG_NO_VERIFYOBJ());
		}
		VerifyObjHeaderVO voHead = (VerifyObjHeaderVO) verifyObj.getParentVO();
		if (!voHead.getBcontrol().booleanValue()) {
			return true;
		}
		AssVO[] assvo = voDetail.getAss();
		if (assvo == null) {
			return false;
		}
		/* 严格按照辅助核算控制 */
		VerifyObjItemVO[] items = (VerifyObjItemVO[]) verifyObj.getChildrenVO();
		if (assvo == null || assvo.length < items.length) {
			return false;
		} else {
			String pk_bdinfo = null;
			for (int i = 0; i < items.length; i++) {
				pk_bdinfo = items[i].getPk_subjass();
				int j = 0;
				for (j = 0; j < assvo.length; j++) {
					if (assvo[j].getPk_Checktype().equals(pk_bdinfo)) {
						if (m_hashAss.get(pk_bdinfo) != null
								&& !m_hashAss.get(pk_bdinfo).toString().equals("" + assvo[j].getPk_Checkvalue())) {
							return false;
						} else {
							m_hashAss.put(pk_bdinfo, "" + assvo[j].getPk_Checkvalue());
						}
						break;
					}
				}
			}
		}
		return true;
	}
	
	private void autoVerifyControl(VerifyDetailVO voDetail,Map<String, List<VerifyDetailVO>> map) throws Exception {
		VerifyObjVO verifyObj = getVerifyObject();
		AssVO[] assvo = voDetail.getAss();
		if (assvo == null) {
			return;
		}
		/* 严格按照辅助核算控制 */
		VerifyObjItemVO[] items = (VerifyObjItemVO[]) verifyObj.getChildrenVO();
		if (assvo == null || assvo.length < items.length) {
			return;
		} else {
			String pk_bdinfo = null;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < items.length; i++) {
				pk_bdinfo = items[i].getPk_subjass();
				int j = 0;
				for (j = 0; j < assvo.length; j++) {
					if (assvo[j].getPk_Checktype().equals(pk_bdinfo)) {
						sb.append(assvo[j].getPk_Checktype()).append(assvo[j].getPk_Checkvalue());
						break;
					}
				}
			}
			List<VerifyDetailVO> verifyDetailVOList = null;
			if(map.get(sb.toString())!=null){
				verifyDetailVOList = (List<VerifyDetailVO>)map.get(sb.toString());
			}else{
				verifyDetailVOList = new ArrayList<VerifyDetailVO>();
			}
			verifyDetailVOList.add(voDetail);
			map.put(sb.toString(), verifyDetailVOList);
		}
	}

	/**
	 * 功能：执行自动红蓝对冲操作 作者：lwg 创建时间：(2004-04-02 11:39:59) 参数：<|> 返回值： 算法：
	 * 
	 * @param bForce
	 *            boolean
	 */
	public void onAutoRedBlue(boolean bDebit) throws Exception {
		beforeVerify();
		onSelectNone();

		// UFDouble ZERO = new UFDouble(0);
		// UFDouble[] ufZero = new UFDouble[] { ZERO, ZERO, ZERO };
		// if (voDebit == null && voCredit == null) {
		// getVerifyUI().showErrorMsg(VerifyMsg.MSG_NOVERIFYDATA);
		// throw new Exception(VerifyMsg.MSG_MANUAL_ERROR);
		// }
		// if (!bForce) {
		// if ((voDebit != null
		// && !isEqual(new UFDouble[] { m_debitSum_Y, m_debitSum_F, m_debitSum_B
		// }, ufZero))
		// || (voCredit != null
		// && !isEqual(new UFDouble[] { m_creditSum_Y, m_creditSum_F,
		// m_creditSum_B },
		// ufZero))) {
		// throw new
		// nc.vo.pub.BusinessException(VerifyMsg.MSG_REDBLUE_UNLIKENESS);
		// }
		// }
		try {
			getRulevo().setBatchId(GLPubProxy.getRemoteVerifyLog().getVerifyBatchid());
			getRulevo().setPairNum(new Integer(0));
			// getRulevo().setCreditObjKeys(null);
			// getRulevo().setDebtObjKeys(null);
			getRulevo().setCreditObjKeys(m_Standardvo.getObj());
			getRulevo().setDebtObjKeys(m_Standardvo.getObj());
			getRulevo().setCreditSortKeys(null);
			getRulevo().setDebtSortKeys(null);
			getRulevo().setDateName(m_sDateName);
			getRulevo().setMaxDateError(m_Standardvo.getDateRange());
			getRulevo().setVerifyMatchSeq(new String[] { REDBLUE });
			getRulevo().setCreditVerifyObj(getVerifyObjectPk());
			getRulevo().setDebitVerifyObj(getVerifyObjectPk());
			/* 核销币种 */
			// if (voDebit != null) {
			// getRulevo().setVerifyCurr(voDebit[0].getCurrPk());
			// } else if (voCredit != null) {
			// getRulevo().setVerifyCurr(voCredit[0].getCurrPk());
			// }
			getHash().clear();
			onSelectAll(bDebit);
			// 参考fill2Hash进行分组
			if (bDebit) {
				fill2Hash(getDebitVerify(), bDebit);
			} else {
				fill2Hash(getCreditVerify(), bDebit);
			}

			java.util.Enumeration enu = getHash().keys();

			// 从此处开始循环开始红冲
			while (enu.hasMoreElements()) {
				Object key = enu.nextElement();
				java.util.ArrayList al = (java.util.ArrayList) getHash().get(key);
				VerifyDetailVO[] voDebit = null;
				VerifyDetailVO[] voCredit = null;
				if (bDebit) {
					voDebit = new VerifyDetailVO[al.size()];
					al.toArray(voDebit);
					getRulevo().setVerifyCurr(voDebit[0].getCurrPk());
				} else if (!bDebit) {
					voCredit = new VerifyDetailVO[al.size()];
					al.toArray(voCredit);
					getRulevo().setVerifyCurr(voCredit[0].getCurrPk());
				}
				getVerifyCom().removeData();
				getVerifyCom().setDebitData(voDebit);
				getVerifyCom().setCreditData(voCredit);
				getVerifyCom().onVerify();

			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_MANUAL_ERROR() + e.getMessage());
			throw new Exception(VerifyMsg.getMSG_MANUAL_ERROR());
		}
		// getVerifyUI().showMsg(VerifyMsg.MSG_VERIFY_SUCCEED);
		getVerifyUI().showSuccessMsg(VerifyMsg.getMSG_VERIFY_SUCCEED());

	}

	/**
	 * 功能：自动核销 作者：宋涛 创建时间：(2003-5-8 16:26:24) 参数：<|> 返回值： 算法：
	 * 
	 */
	public void onAutoVerify() throws Exception {
		beforeVerify();
		// adjust 20041208
		if (!m_Standardvo.isVerifySelected()) {
			onSelectAll();
		}
		if (getCreditVerify() == null || getDebitVerify() == null) {
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_NOVERIFYDATA());
//			throw new Exception(VerifyMsg.getMSG_MANUAL_ERROR());
			throw new Exception(VerifyMsg.getMSG_NOVERIFYDATA());
		}
		try {
			getRulevo().setBatchId(GLPubProxy.getRemoteVerifyLog().getVerifyBatchid());
			getRulevo().setPairNum(new Integer(0));
			getRulevo().setDateName(m_sDateName);
			getRulevo().setVerifyMatchSeq(new String[] { VERIFY });
			getRulevo().setCreditObjKeys(m_Standardvo.getObj());
			getRulevo().setDebtObjKeys(m_Standardvo.getObj());
			getRulevo().setCreditSortKeys(new String[] { m_sDateName, "vouchertypeName", "DispVouchNO", "detailindex" });
			getRulevo().setDebtSortKeys(new String[] { m_sDateName, "vouchertypeName", "DispVouchNO", "detailindex" });
			getRulevo().setMaxDateError(m_Standardvo.getDateRange());
			getRulevo().setCreditVerifyObj(getVerifyObjectPk());
			getRulevo().setDebitVerifyObj(getVerifyObjectPk());
			/* 核销币种 */
			getRulevo().setVerifyCurr(getCreditVerify()[0].getCurrPk());
			getVerifyCom().removeData();
			// adjust 20041208
			if (m_Standardvo.isVerifySelected()) {
				getVerifyCom().setCreditData(getSelectedCredit());
				getVerifyCom().setDebitData(getSelectedDebit());
			} else {
				getVerifyCom().setCreditData(getCreditVerify());
				getVerifyCom().setDebitData(getDebitVerify());
			}
			//按辅助核算严格控制
			VerifyObjVO verifyObj = getVerifyObject();
			VerifyObjHeaderVO voHead = (VerifyObjHeaderVO) verifyObj.getParentVO();
			if (voHead.getBcontrol().booleanValue()) {
				Map<String, List<VerifyDetailVO>> debitMap = new HashMap<String, List<VerifyDetailVO>>();
				VerifyDetailVO[] debitDatas = (VerifyDetailVO[])getVerifyCom().getDebitData();
				if(debitDatas!=null&&debitDatas.length>0){
					for(VerifyDetailVO debitData:debitDatas){
						autoVerifyControl(debitData, debitMap);
					}
				}
				Map<String, List<VerifyDetailVO>> creditMap = new HashMap<String, List<VerifyDetailVO>>();
				VerifyDetailVO[] creditDatas = (VerifyDetailVO[])getVerifyCom().getCreditData();
				if(creditDatas!=null&&creditDatas.length>0){
					for(VerifyDetailVO creditData:creditDatas){
						autoVerifyControl(creditData, creditMap);
					}
				}
				if(debitMap.size()>0){
					List<VerifyDetailVO> debitList = new ArrayList<VerifyDetailVO>();
					List<VerifyDetailVO> creditList = new ArrayList<VerifyDetailVO>();
					Iterator<String> keyIter = debitMap.keySet().iterator();
					while(keyIter.hasNext()){
						String key = keyIter.next();
						List<VerifyDetailVO> verifyDetailVOList = creditMap.get(key);
						if(verifyDetailVOList!=null&&verifyDetailVOList.size()>0){
							debitList.addAll(debitMap.get(key));
							creditList.addAll(verifyDetailVOList);
							getVerifyCom().setDebitData(debitList.toArray(new VerifyDetailVO[0]));
							getVerifyCom().setCreditData(creditList.toArray(new VerifyDetailVO[0]));
							getVerifyCom().onVerify();
							debitList.clear();
							creditList.clear();
						}
					}
				}
			}else{
				getVerifyCom().onVerify();
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new Exception(VerifyMsg.getMsg_Autoverify_Error());
		}
		getVerifyUI().showSuccessMsg(VerifyMsg.getMSG_VERIFY_SUCCEED());

	}

	/**
	 * 功能：历史数据全选 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	public void onHistorySelectAll() {
		try {
			int iCount = getHistroyDataCount();
			for (int i = 0; i < iCount; i++) {
				setHistoryValueAt(VerifyMsg.TRUE, i, GlVerifyDispLogKey.SELECTED);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 功能：历史数据全消 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	public void onHistorySelectNone() {
		try {
			int iCount = getHistroyDataCount();
			for (int i = 0; i < iCount; i++) {
				setHistoryValueAt(VerifyMsg.FALSE, i, GlVerifyDispLogKey.SELECTED);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public void setRslt(nc.vo.glrp.verify.FilterCondVO voCond, nc.vo.glrp.verify.VerifyDetailVO[][] vos) throws Exception {
		try {
			m_voQry = voCond;
			m_hashAss.clear();
			m_iSelectedCount = 0;
			setDateName(voCond);
			getVerifyCom().onFilterDataEx(voCond, vos);
			// VerifyDetailVO[][] filteredData = filterByDataPower(getVerifyCom().getDebitData(),getVerifyCom().getCreditData());
			VerifyDetailVO[][] filteredData = new VerifyDetailVO[][] { (VerifyDetailVO[]) getVerifyCom().getDebitData(),
					(VerifyDetailVO[]) getVerifyCom().getCreditData() };
			if (filteredData[0] != null) {
				setDebitVerify(filteredData[0]);
				appendDetailInfo(filteredData[0]);
				// v31
				setFecthDebitVerify(filteredData[0].clone());
			} else {
				setDebitVerify(null);
			}
			if (filteredData[1] != null) {
				setCreditVerify(filteredData[1]);
				appendDetailInfo(filteredData[1]);
				// v31
				setFetchCreditVerify(filteredData[1].clone());
			} else {
				setCreditVerify(null);
			}
			// if (getCreditVerify() == null && getDebitVerify() == null) {
			// getVerifyUI().showErrorMsg(VerifyMsg.MSG_NOFATCHDATA);
			// }
			if (m_Fetch) {
				if (m_FetchDirect == 1) {
					setCreditVerify(null);
				} else if (m_FetchDirect == -1) {
					setDebitVerify(null);
				}
			}
			initSum();
			firePropertyChange(String.valueOf(VerifyDetailKey.SELECTED), null, "updateall");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_QUERY_ERROR());
			throw new Exception(VerifyMsg.getMSG_QUERY_ERROR());
		}
	}

	/**
	 * 功能：过滤参加核销的数据 作者：宋涛 创建时间：(2003-5-8 16:21:41) 参数：<|> 返回值： 算法：
	 * 
	 * @param voCond
	 *            nc.vo.glrp.verify.FilterCondVO
	 */
	public void onQuery(nc.vo.glrp.verify.FilterCondVO voCond) throws Exception {
		try {
			m_voQry = voCond;
			m_hashAss.clear();
			m_iSelectedCount = 0;
			setDateName(voCond);
			getVerifyCom().onFilterData(voCond);
			VerifyDetailVO[] deibtvos = (VerifyDetailVO[]) getVerifyCom().getDebitData();
			VerifyDetailVO[] credtvos = (VerifyDetailVO[]) getVerifyCom().getCreditData();
			if (deibtvos != null) {
				setDebitVerify(deibtvos);
				appendDetailInfo(deibtvos);
				// v31
				setFecthDebitVerify(deibtvos.clone());
			} else {
				setDebitVerify(null);
			}
			if (credtvos != null) {
				setCreditVerify(credtvos);
				appendDetailInfo(credtvos);
				// v31
				setFetchCreditVerify(credtvos.clone());
			} else {
				setCreditVerify(null);
			}
			if (m_Fetch) {
				if (m_FetchDirect == 1) {
					setCreditVerify(null);
				} else if (m_FetchDirect == -1) {
					setDebitVerify(null);
				}
			}
			initSum();
			firePropertyChange(String.valueOf(VerifyDetailKey.SELECTED), null, "updateall");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_QUERY_ERROR());
			throw new Exception(VerifyMsg.getMSG_QUERY_ERROR());
		}
	}

	/**
	 * @param debitData
	 * @param creditData
	 * @return
	 */
	// private VerifyDetailVO[][] filterByDataPower(IVerifyVO[] debitData, IVerifyVO[] creditData) {
	//
	// VerifyDataPowerFilter filter = new VerifyDataPowerFilter();
	// List<String> illegalAssList = new ArrayList<String>();
	// VerifyDetailVO[] debitFilterData = filterOneSide(filter, debitData, illegalAssList);
	// VerifyDetailVO[] creditFilterData = filterOneSide(filter, creditData, illegalAssList);
	// return new VerifyDetailVO[][] { debitFilterData, creditFilterData };
	// }
	/**
	 * @param filter
	 * @param data
	 * @param illegalAssList
	 * @return
	 */
	// private VerifyDetailVO[] filterOneSide(VerifyDataPowerFilter filter, IVerifyVO[] data, List<String> illegalAssList) {
	//
	// if (data == null)
	// return null;
	// VerifyDetailVO[] castdata = (VerifyDetailVO[]) data;
	// List<VerifyDetailVO> legalList = new ArrayList<VerifyDetailVO>();
	// for (VerifyDetailVO vo : castdata) {
	// if (vo.getAssid() == null) {
	// legalList.add(vo);
	// } else {
	//
	// if (!illegalAssList.contains(vo.getAssid())) {
	//
	// if (filter.isLegal(vo.getAssid())) {
	// legalList.add(vo);
	// } else {
	// illegalAssList.add(vo.getAssid());
	// }
	// }
	// }
	// }
	//
	// return legalList.toArray(new VerifyDetailVO[0]);
	// }
	/**
	 * 功能：过滤历史纪录 作者：宋涛 创建时间：(2003-5-8 16:50:56) 参数：<|> 返回值： 算法：
	 * 
	 */
	public void onQueryHisRecord(nc.vo.glrp.verify.LogFilterCondVO voCond) throws Exception {
		try {
			m_sumLogs = null;
			m_detailLogs = null;
			m_hashSelectedSumLogs.clear();
			m_historyCondvo = voCond;
			setHistoryState(true);
			getVerifyCom().setCondition(voCond);
			m_detailLogs = (GlVerifyDisplogVO[]) getVerifyCom().getDispLogData();
			if (m_detailLogs == null || m_detailLogs.length <= 0) {
				m_historyUI.showMsg(VerifyMsg.getMSG_NOFATCHDATA());
				firePropertyChange(String.valueOf(GlVerifyDispLogKey.SELECTED), null, "updateall");
				return;
			}
			calculateSumLogs(m_detailLogs);
			firePropertyChange(String.valueOf(GlVerifyDispLogKey.SELECTED), null, "updateall");
		} catch (Exception e) {
			// Logger.error(e.getMessage(), e);
			Logger.error(e.getMessage(), e);
			m_historyUI.showErrorMsg(VerifyMsg.getMSG_HISTORYQRY_ERROR());
			throw new Exception(VerifyMsg.getMSG_HISTORYQRY_ERROR());
		}
		m_historyUI.showMsg(VerifyMsg.getMSG_QRYFINISHED());
	}

	/**
	 * 功能：执行红蓝对冲操作 作者：宋涛 创建时间：(2003-5-10 11:39:59) 参数：<|> 返回值： 算法：
	 * 
	 * @param bForce
	 *            boolean
	 */
	public void onRedBlue(boolean bForce) throws Exception {
		beforeVerify();
		VerifyDetailVO[] voDebit = getSelectedDebit();
		VerifyDetailVO[] voCredit = getSelectedCredit();
		UFDouble ZERO = new UFDouble(0);
		UFDouble[] ufZero = new UFDouble[] { ZERO, ZERO, ZERO };
		if (voDebit == null && voCredit == null) {
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_NOVERIFYDATA());
//			throw new Exception(VerifyMsg.getMSG_MANUAL_ERROR());
			throw new Exception(VerifyMsg.getMSG_NOVERIFYDATA());
		}
		if (!bForce) {
			if ((voDebit != null && !isEqual(new UFDouble[] { m_debitSum_Y, m_debitSum_F, m_debitSum_B }, ufZero))
					|| (voCredit != null && !isEqual(new UFDouble[] { m_creditSum_Y, m_creditSum_F, m_creditSum_B }, ufZero))) {
				throw new nc.vo.pub.BusinessException(VerifyMsg.getMSG_REDBLUE_UNLIKENESS());
			}
		}
		try {
			getRulevo().setBatchId(GLPubProxy.getRemoteVerifyLog().getVerifyBatchid());
			getRulevo().setPairNum(0);
			getRulevo().setCreditObjKeys(null);
			getRulevo().setDebtObjKeys(null);
			getRulevo().setCreditSortKeys(null);
			getRulevo().setDebtSortKeys(null);
			getRulevo().setDateName(m_sDateName);
			getRulevo().setMaxDateError(null);
			getRulevo().setVerifyMatchSeq(new String[] { REDBLUE });
			getRulevo().setCreditVerifyObj(getVerifyObjectPk());
			getRulevo().setDebitVerifyObj(getVerifyObjectPk());
			/* 核销币种 */
			if (voDebit != null) {
				getRulevo().setVerifyCurr(voDebit[0].getCurrPk());
			} else if (voCredit != null) {
				getRulevo().setVerifyCurr(voCredit[0].getCurrPk());
			}
			getVerifyCom().removeData();
			getVerifyCom().setDebitData(voDebit);
			getVerifyCom().setCreditData(voCredit);
			getVerifyCom().onVerify();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_MANUAL_ERROR() + e.getMessage());
			throw new Exception(VerifyMsg.getMSG_MANUAL_ERROR());
		}
		getVerifyUI().showMsg(VerifyMsg.getMSG_VERIFY_SUCCEED());

	}

	/**
	 * 功能：数据全选 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	public void onSelectAll() {
		int loop = 0;
		String sSum = null;
		if (getDebitVerify() != null) {
			loop = getDebitVerify().length;
			for (int i = 0; i < loop; i++) {
				getDebitVerify()[i].setSelected(VerifyMsg.TRUE);
				getDebitVerify()[i].setDebit_Money_B(getDebitVerify()[i].getBalancelocaldebitamount());
//				getDebitVerify()[i].setDebit_Money_F(getDebitVerify()[i].getBalancefracdebitamount());
				getDebitVerify()[i].setDebit_Money_Y(getDebitVerify()[i].getBalancedebitamount());
//				getDebitVerify()[i].setDebit_Money_J(getDebitVerify()[i].getBalancegroupdebitamount());
//				getDebitVerify()[i].setDebit_Money_Q(getDebitVerify()[i].getBalanceglobaldebitamount());

			}
			try {
				m_debitSum_Y = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_Y);
				sSum = getFormater().formatAmount(m_debitSum_Y, getDebitVerify()[0].getCurrPk());
				firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Y), null, sSum);

				// if (ClientInfo.getInstance().bFracUsed()) {
				// m_debitSum_F = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_F);
				// sSum = getFormater().formatAmount(m_debitSum_F, ClientInfo.getInstance().getPk_fracpk());
				// firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_F), null, sSum);
				// }
				if (Currency.isStartGroupCurr(GlWorkBench.getLoginGroup())) {
					m_debitSum_J = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_J);
					sSum = getFormater().formatAmount(m_debitSum_J, Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()));
					firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_J), null, sSum);
				}
				if (Currency.isStartGlobalCurr()) {
					m_debitSum_Q = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_Q);
					sSum = getFormater().formatAmount(m_debitSum_Q, Currency.getGlobalCurrPk(""));
					firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Q), null, sSum);
				}
				m_debitSum_B = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_B);
				sSum = getFormater().formatAmount(m_debitSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
				firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_B), null, sSum);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		if (getCreditVerify() != null) {
			loop = getCreditVerify().length;
			for (int i = 0; i < loop; i++) {
				getCreditVerify()[i].setSelected(VerifyMsg.TRUE);
				getCreditVerify()[i].setCredit_Money_B(getCreditVerify()[i].getBalancelocalcreditamount());
//				getCreditVerify()[i].setCredit_Money_F(getCreditVerify()[i].getBalancefraccreditamount());
				getCreditVerify()[i].setCredit_Money_Y(getCreditVerify()[i].getBalancecreditamount());
//				getCreditVerify()[i].setCredit_Money_J(getCreditVerify()[i].getBalancegroupcreditamount());
//				getCreditVerify()[i].setCredit_Money_Q(getCreditVerify()[i].getBalanceglobalcreditamount());
			}
			try {
				m_creditSum_Y = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Y);
				sSum = getFormater().formatAmount(m_creditSum_Y, getCreditVerify()[0].getCurrPk());
				firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Y), null, sSum);

				// if (ClientInfo.getInstance().bFracUsed()) {
				// m_creditSum_F = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_F);
				// sSum = getFormater().formatAmount(m_creditSum_F, ClientInfo.getInstance().getPk_fracpk());
				// firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_F), null, sSum);
				// }
				if (Currency.isStartGroupCurr(GlWorkBench.getLoginGroup())) {
					m_creditSum_J = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_J);
					sSum = getFormater().formatAmount(m_creditSum_J, Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()));
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_J), null, sSum);
				}
				if (Currency.isStartGlobalCurr()) {
					m_creditSum_Q = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Q);
					sSum = getFormater().formatAmount(m_creditSum_Q, Currency.getGlobalCurrPk(""));
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Q), null, sSum);
				}
				m_creditSum_B = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_B);
				sSum = getFormater().formatAmount(m_creditSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
				firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_B), null, sSum);
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * 功能：单方向数据全选 作者：lwg 创建时间：(2004-04-02 15:31:53) 参数：<|> 返回值： 算法： version:v30
	 */
	public void onSelectAll(boolean bDebit) {
		int loop = 0;
		String sSum = null;
		if (bDebit) {
			if (getDebitVerify() != null) {
				loop = getDebitVerify().length;
				for (int i = 0; i < loop; i++) {
					getDebitVerify()[i].setSelected(VerifyMsg.TRUE);
					getDebitVerify()[i].setDebit_Money_B(getDebitVerify()[i].getBalancelocaldebitamount());
//					getDebitVerify()[i].setDebit_Money_F(getDebitVerify()[i].getBalancefracdebitamount());
					getDebitVerify()[i].setDebit_Money_Y(getDebitVerify()[i].getBalancedebitamount());
				}
				try {
					m_debitSum_Y = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_Y);
					sSum = getFormater().formatAmount(m_debitSum_Y, getDebitVerify()[0].getCurrPk());
					firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Y), null, sSum);

					if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()) {
						m_debitSum_F = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_F);
						sSum = getFormater().formatAmount(m_debitSum_F,
								ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
						firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_F), null, sSum);
					}
					m_debitSum_B = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_B);
					sSum = getFormater().formatAmount(m_debitSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
					firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_B), null, sSum);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		} else if (!bDebit) {
			if (getCreditVerify() != null) {
				loop = getCreditVerify().length;
				for (int i = 0; i < loop; i++) {
					getCreditVerify()[i].setSelected(VerifyMsg.TRUE);
					getCreditVerify()[i].setCredit_Money_B(getCreditVerify()[i].getBalancelocalcreditamount());
//					getCreditVerify()[i].setCredit_Money_F(getCreditVerify()[i].getBalancefraccreditamount());
					getCreditVerify()[i].setCredit_Money_Y(getCreditVerify()[i].getBalancecreditamount());
				}
				try {
					m_creditSum_Y = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Y);
					sSum = getFormater().formatAmount(m_creditSum_Y, getCreditVerify()[0].getCurrPk());
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Y), null, sSum);

					if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()) {
						m_creditSum_F = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_F);
						sSum = getFormater().formatAmount(m_creditSum_F,
								ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
						firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_F), null, sSum);
					}
					m_creditSum_B = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_B);
					sSum = getFormater().formatAmount(m_creditSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_B), null, sSum);
				} catch (Exception ex) {
nc.bs.logging.Logger.error(ex.getMessage(), ex);
				}
			}
		}
	}

	// 获取全选的标准
	private VerifyDetailVO getSelectAllCZ() {
		int loop = 0;
		if (getCreditVerify() != null) {
			loop = getCreditVerify().length;
			for (int i = 0; i < loop; i++) {
				if (getCreditVerify()[i].isSelected().booleanValue())
					return getCreditVerify()[i];
			}
		}

		if (getDebitVerify() != null) {
			loop = getDebitVerify().length;
			for (int i = 0; i < loop; i++) {
				if (getDebitVerify()[i].isSelected().booleanValue())
					return getDebitVerify()[i];
			}
		}

		if (getCreditVerify() != null)
			return getCreditVerify()[0];

		if (getDebitVerify() != null)
			return getDebitVerify()[0];

		return null;
	}

	/**
	 * 功能：响应界面端全选按钮 作者：lwg 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：缺省为贷方第一条数据作为全选依据，如果用户已经做了选择， 则以用户选择的分录作为全选依据 version:v30
	 */
	public void onSelectAll_2() {
		int loop = 0;
		String sSum = null;
		VerifyObjVO verifyObj = getVerifyObject();
		VerifyObjHeaderVO voHead = (VerifyObjHeaderVO) verifyObj.getParentVO();
		VerifyDetailVO dVo = getSelectAllCZ();
		m_hashAss.clear();
		if (dVo != null) {
			if (dVo.getAssid() != null && dVo.getAss() == null)
				dVo.setAss(FreeValueDataCache.getInstance().getAssvosByID(dVo.getAssid()));
			if (dVo.getAss() != null) {
				for (int i = 0; i < dVo.getAss().length; i++) {
					AssVO array_element = dVo.getAss()[i];
					m_hashAss.put(array_element.getPk_Checktype(), array_element.getPk_Checkvalue());
				}
			}
		}

		if (getDebitVerify() != null) {
			loop = getDebitVerify().length;
			if (!voHead.getBcontrol().booleanValue()) {
				for (int i = 0; i < loop; i++) {
					getDebitVerify()[i].setSelected(VerifyMsg.TRUE);
					getDebitVerify()[i].setDebit_Money_B(getDebitVerify()[i].getBalancelocaldebitamount());
//					getDebitVerify()[i].setDebit_Money_F(getDebitVerify()[i].getBalancefracdebitamount());
					getDebitVerify()[i].setDebit_Money_Y(getDebitVerify()[i].getBalancedebitamount());
					// add by hurh
//					getDebitVerify()[i].setDebit_Money_J(getDebitVerify()[i].getBalancegroupdebitamount());
//					getDebitVerify()[i].setDebit_Money_Q(getDebitVerify()[i].getBalanceglobaldebitamount());
				}
			} else {
				for (int i = 0; i < loop; i++) {
					if (getDebitVerify()[i].getAssid().equals(dVo.getAssid())) {
						getDebitVerify()[i].setSelected(VerifyMsg.TRUE);
						getDebitVerify()[i].setDebit_Money_B(getDebitVerify()[i].getBalancelocaldebitamount());
//						getDebitVerify()[i].setDebit_Money_F(getDebitVerify()[i].getBalancefracdebitamount());
						getDebitVerify()[i].setDebit_Money_Y(getDebitVerify()[i].getBalancedebitamount());
						// add by hurh
//						getDebitVerify()[i].setDebit_Money_J(getDebitVerify()[i].getBalancegroupdebitamount());
//						getDebitVerify()[i].setDebit_Money_Q(getDebitVerify()[i].getBalanceglobaldebitamount());
					}
				}

			}
			try {
				m_debitSum_Y = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_Y);
				sSum = getFormater().formatAmount(m_debitSum_Y, getDebitVerify()[0].getCurrPk());
				firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Y), null, sSum);

				m_debitSumU_Y = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_Y);
				sSum = getFormater().formatAmount(m_debitSumU_Y, getDebitVerify()[0].getCurrPk());
				firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_Y), null, sSum);

				if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()) {
					m_debitSum_F = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_F);
					sSum = getFormater()
							.formatAmount(m_debitSum_F, ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
					firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_F), null, sSum);

					m_debitSumU_F = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_F);
					sSum = getFormater().formatAmount(m_debitSumU_F,
							ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
					firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_F), null, sSum);
				}
				m_debitSum_B = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_B);
				sSum = getFormater().formatAmount(m_debitSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
				firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_B), null, sSum);

				m_debitSumU_B = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_B);
				sSum = getFormater().formatAmount(m_debitSumU_B, Currency.getLocalCurrPK(getPk_accountingbook()));
				firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_B), null, sSum);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		if (getCreditVerify() != null) {
			if (getDebitVerify() == null) {
				for (int i = 0; i < getCreditVerify().length; i++) {
					getCreditVerify()[i].setSelected(VerifyMsg.TRUE);
				}
			} else {
				loop = getCreditVerify().length;
				if (!voHead.getBcontrol().booleanValue()) {
					for (int i = 0; i < loop; i++) {
						setBalaMount(i);

					}
				} else {
					for (int i = 0; i < loop; i++) {
						if (getCreditVerify()[i].getAssid().equals(dVo.getAssid())) {
							setBalaMount(i);
						}
					}

				}
				try {
					m_creditSum_Y = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Y);
					sSum = getFormater().formatAmount(m_creditSum_Y, getCreditVerify()[0].getCurrPk());
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Y), null, sSum);

					m_creditSumU_Y = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_Y);
					sSum = getFormater().formatAmount(m_creditSumU_Y, getCreditVerify()[0].getCurrPk());
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_Y), null, sSum);

					if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()) {
						m_creditSum_F = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_F);
						sSum = getFormater().formatAmount(m_creditSum_F,
								ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
						firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_F), null, sSum);
						m_creditSumU_F = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_F);
						sSum = getFormater().formatAmount(m_creditSumU_F,
								ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
						firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_F), null, sSum);
					}
					m_creditSum_B = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_B);
					sSum = getFormater().formatAmount(m_creditSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_B), null, sSum);
					m_creditSumU_B = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_B);
					sSum = getFormater().formatAmount(m_creditSumU_B, Currency.getLocalCurrPK(getPk_accountingbook()));
					firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_B), null, sSum);
				} catch (Exception ex) {
nc.bs.logging.Logger.error(ex.getMessage(), ex);
				}
			}
		}

	}

	private void setBalaMount(int i) {
		getCreditVerify()[i].setSelected(VerifyMsg.TRUE);
		getCreditVerify()[i].setCredit_Money_B(getCreditVerify()[i].getBalancelocalcreditamount());
//		getCreditVerify()[i].setCredit_Money_F(getCreditVerify()[i].getBalancefraccreditamount());
		getCreditVerify()[i].setCredit_Money_Y(getCreditVerify()[i].getBalancecreditamount());
		// add by hurh
//		getCreditVerify()[i].setCredit_Money_J(getDebitVerify()[i].getBalancegroupcreditamount());
//		getCreditVerify()[i].setCredit_Money_Q(getDebitVerify()[i].getBalanceglobalcreditamount());
	}

	/**
	 * 功能：数据全消 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	public void onSelectNone() {
		int loop = 0;
		if (getDebitVerify() != null) {
			loop = getDebitVerify().length;
			for (int i = 0; i < loop; i++) {
				getDebitVerify()[i].setSelected(VerifyMsg.FALSE);
				getDebitVerify()[i].setDebit_Money_B(VerifyMsg.ZERO);
//				getDebitVerify()[i].setDebit_Money_F(VerifyMsg.ZERO);
				getDebitVerify()[i].setDebit_Money_Y(VerifyMsg.ZERO);
//				getDebitVerify()[i].setDebit_Money_J(VerifyMsg.ZERO);
//				getDebitVerify()[i].setDebit_Money_Q(VerifyMsg.ZERO);
			}
		}
		if (getCreditVerify() != null) {
			loop = getCreditVerify().length;
			for (int i = 0; i < loop; i++) {
				getCreditVerify()[i].setSelected(VerifyMsg.FALSE);
				getCreditVerify()[i].setCredit_Money_B(VerifyMsg.ZERO);
//				getCreditVerify()[i].setCredit_Money_F(VerifyMsg.ZERO);
				getCreditVerify()[i].setCredit_Money_Y(VerifyMsg.ZERO);
//				getCreditVerify()[i].setCredit_Money_J(VerifyMsg.ZERO);
//				getCreditVerify()[i].setCredit_Money_Q(VerifyMsg.ZERO);
			}
		}
		try {
			initSum();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 功能：响应界面端全消按钮 作者：lwg 创建时间：(2004-03-30 15:31:53) 参数：<|> 返回值： 算法： verison:v30
	 */
	public void onSelectNone_2() {
		onSelectNone();
	}

	public void onSelectNone_2_bak() {

		try {
			int loop = 0;
			nc.vo.pub.lang.UFBoolean FALSE = nc.vo.pub.lang.UFBoolean.FALSE;
			if (getDebitVerify() != null) {
				loop = getDebitVerify().length;
				for (int i = 0; i < loop; i++) {
					if (getDebitVerify()[i].isSelected().booleanValue()) {
						setValueAt(FALSE, i, VerifyDetailKey.SELECTED, true);
					}
				}
			}
			if (getCreditVerify() != null) {
				loop = getCreditVerify().length;
				for (int i = 0; i < loop; i++) {
					if (getCreditVerify()[i].isSelected().booleanValue()) {
						setValueAt(FALSE, i, VerifyDetailKey.SELECTED, false);
					}
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 功能：执行核销 作者：宋涛 创建时间：(2003-5-8 16:14:08) 参数：<|> 返回值：true 强制核销，false非强制核销 算法：
	 * 
	 * @param bForce
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public void onVerify(boolean bForce) throws Exception {
		beforeVerify();
		VerifyDetailVO[] voDebit = getSelectedDebit();
		VerifyDetailVO[] voCredit = getSelectedCredit();
		if (voDebit == null || voCredit == null) {
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_NOVERIFYDATA());
//			throw new Exception(VerifyMsg.getMSG_MANUAL_ERROR());
			throw new Exception(VerifyMsg.getMSG_NOVERIFYDATA());
		}
		if (!bForce && !isEqual(new UFDouble[] { m_debitSum_Y, m_debitSum_B }, new UFDouble[] { m_creditSum_Y, m_creditSum_B })) {
			throw new nc.vo.pub.BusinessException(VerifyMsg.getMSG_VERIFYDATA_UNLIKENESS());
		}
		try {
			getRulevo().setBatchId(GLPubProxy.getRemoteVerifyLog().getVerifyBatchid());
			getRulevo().setPairNum(new Integer(0));
			getRulevo().setCreditObjKeys(null);
			getRulevo().setDebtObjKeys(null);
			getRulevo().setCreditSortKeys(null);
			getRulevo().setDebtSortKeys(null);
			getRulevo().setDateName(m_sDateName);
			getRulevo().setMaxDateError(null);
			getRulevo().setVerifyMatchSeq(new String[] { VERIFY });
			getRulevo().setCreditVerifyObj(getVerifyObjectPk());
			getRulevo().setDebitVerifyObj(getVerifyObjectPk());
			/* 核销币种 */
			getRulevo().setVerifyCurr(getCreditVerify()[0].getCurrPk());
			getVerifyCom().removeData();
			getVerifyCom().setDebitData(voDebit);
			getVerifyCom().setCreditData(voCredit);
			getVerifyCom().onVerify();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			getVerifyUI().showErrorMsg(VerifyMsg.getMSG_MANUAL_ERROR() + e.getMessage());
			throw new Exception(VerifyMsg.getMSG_MANUAL_ERROR());
		}
		// getVerifyUI().showMsg(VerifyMsg.MSG_VERIFY_SUCCEED);
		getVerifyUI().showSuccessMsg(VerifyMsg.getMSG_VERIFY_SUCCEED());

	}

	/**
	 * 功能：相应属性变化事件 作者：宋涛 创建时间：(2003-5-10 10:03:35) 参数：<|> 返回值： 算法：
	 * 
	 * @param evt
	 *            java.beans.PropertyChangeEvent
	 */
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-9 15:41:27) 参数：<|> 返回值： 算法：
	 * 
	 * @param newCreditVerify
	 *            nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	public void setCreditVerify(nc.vo.glrp.verify.VerifyDetailVO[] newCreditVerify) {
		m_CreditVerify = newCreditVerify;
	}

	/**
	 * 功能：设置日期名称 作者：宋涛 创建时间：(2003-5-9 14:20:51) 参数：<|> 返回值： 算法：
	 * 
	 * @param voCond
	 *            nc.vo.glrp.verify.FilterCondVO
	 */
	private void setDateName(nc.vo.glrp.verify.FilterCondVO voCond) {
		if (voCond != null) {
			if (voCond.getDebitCond() != null && voCond.getDebitCond().getDateType() != null) {
				m_sDateName = voCond.getDebitCond().getDateType();
			} else if (voCond.getCreditCond() != null && voCond.getCreditCond().getDateType() != null) {
				m_sDateName = voCond.getCreditCond().getDateType();
			}
		}
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-9 15:38:51) 参数：<|> 返回值： 算法：
	 * 
	 * @param newDebitVerify
	 *            nc.vo.glrp.verify.VerifyDetailVO[]
	 */
	public void setDebitVerify(nc.vo.glrp.verify.VerifyDetailVO[] newDebitVerify) {
		m_DebitVerify = newDebitVerify;
	}

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-9 11:56:09) 参数：<|> 返回值： 算法：
	 * 
	 * @param bnewState
	 *            boolean
	 */
	public void setHistoryState(boolean bnewState) {
		if (m_bSum == bnewState) {
			return;
		}
		if (!bnewState) {
			m_selectedDetailLogs = getSelectedLogs();
		}
		m_bSum = bnewState;
		firePropertyChange(String.valueOf(GlVerifyDispLogKey.CHANGE_STATE), null, nc.vo.pub.lang.UFBoolean.valueOf(bnewState));
	}

	/**
	 * 设置历史纪录ui 创建日期：(2003-5-12 20:01:07)
	 * 
	 * @param newHistoryUi
	 *            nc.ui.glrp.verify.IHistoryUI
	 */
	public void setHistoryUI(IHistoryUI newHistoryUi) {
		m_historyUI = newHistoryUi;
	}

	/**
	 * 功能：设置model中的历史纪录数据 作者：宋涛 创建时间：(2003-5-9 11:46:56) 参数：<|> 返回值： 算法：
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
	 *                异常说明。
	 */
	public void setHistoryValueAt(Object oValue, int iRow, int iKey) throws Exception {
		try {
			GlVerifyDisplogVO vo = null;
			if (getHistoryState()) {
				vo = m_sumLogs[iRow];
			} else {
				vo = m_selectedDetailLogs[iRow];
			}
			Object oOldValue = vo.getValue(iKey);
			switch (iKey) {
			case nc.vo.glrp.verify.GlVerifyDispLogKey.SELECTED:
				oValue = nc.vo.pub.lang.UFBoolean.valueOf(oValue.toString());
				if (((nc.vo.pub.lang.UFBoolean) oOldValue).booleanValue() != ((nc.vo.pub.lang.UFBoolean) oValue).booleanValue())
					vo.setbSelected((nc.vo.pub.lang.UFBoolean) oValue);
				/* 添加汇总状态的变化 */
				if (getHistoryState()) {
					if (((nc.vo.pub.lang.UFBoolean) oValue).booleanValue()) {
						m_hashSelectedSumLogs.put(vo.getBatchid(), vo.getBatchid());
					} else {
						m_hashSelectedSumLogs.remove(vo.getBatchid());
					}
					if (m_detailLogs != null)
						for (int i = 0; i < m_detailLogs.length; i++) {
							if (m_hashSelectedSumLogs.get(m_detailLogs[i].getBatchid()) == null) {
								m_detailLogs[i].setbSelected(VerifyMsg.FALSE);
							} else {
								m_detailLogs[i].setbSelected(VerifyMsg.TRUE);
							}
						}
				}
				break;
			default:
				if (oOldValue == null && oValue == null) {
					return;
				} else if (oOldValue != null && oValue != null && oOldValue.equals(oValue)) {
					return;
				} else {
					vo.setValue(iKey, oValue);
				}
				break;
			}
			firePropertyChange(String.valueOf(iKey), null, oValue);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 功能：贷方本币 作者：宋涛 创建时间：(2003-5-10 14:15:58) 参数：<|> 返回值： 算法：
	 * 
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
	public void setValue_Crebit_Js_B(int iRow, UFDouble oValue) throws Exception {
		try {
			UFDouble oldValue = getCreditVerify()[iRow].getCredit_Money_B();
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getCreditVerify()[iRow]
						.getValue(VerifyDetailKey.CREDIT_YE_B))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getCreditVerify()[iRow].getValue(VerifyDetailKey.CREDIT_YE_B)).abs()) > 0) {
					return;
				}
			}
			getCreditVerify()[iRow].setValue(VerifyDetailKey.CREDIT_JS_B, oValue);
			m_creditSum_B = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_B);
			String sSum = null;
			if (Currency.getLocalCurrPK(getPk_accountingbook()) != null) {
				sSum = getFormater().formatAmount(m_creditSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
			} else {
				sSum = m_creditSum_B.toString();
			}

			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_B), null, sSum);

			/* add by lwg */
			String sSumU_B = null;
			m_creditSumU_B = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_B);
			if (Currency.getLocalCurrPK(getPk_accountingbook()) != null) {
				sSumU_B = getFormater().formatAmount(m_creditSumU_B, Currency.getLocalCurrPK(getPk_accountingbook()));
			} else {
				sSumU_B = m_creditSumU_B.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_B), null, sSumU_B);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 
	 * @author CongJinliang
	 * @since 2010-6-4
	 * @param iRow
	 * @param oValue
	 * @throws Exception
	 *             从setValue_Crebit_Js_B拷贝来的代码
	 */
	public void setValue_Crebit_Js_J(int iRow, UFDouble oValue) throws Exception {
		try {
			UFDouble oldValue = (UFDouble) getCreditVerify()[iRow].getValue(VerifyDetailKey.CREDIT_JS_J);
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getCreditVerify()[iRow]
						.getValue(VerifyDetailKey.CREDIT_YE_J))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getCreditVerify()[iRow].getValue(VerifyDetailKey.CREDIT_YE_J)).abs()) > 0) {
					return;
				}
			}
			getCreditVerify()[iRow].setValue(VerifyDetailKey.CREDIT_JS_J, oValue);
			m_creditSum_J = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_J);
			String sSum = null;
			String currtype = Currency.getGroupCurrpk(GlWorkBench.getLoginGroup());
			if (currtype != null) {
				sSum = getFormater().formatAmount(m_creditSum_J, currtype);
			} else {
				sSum = m_creditSum_J.toString();
			}

			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_J), null, sSum);

			/* add by lwg */
			String sSumU_J = null;
			m_creditSumU_J = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_J);
			if (currtype != null) {
				sSumU_J = getFormater().formatAmount(m_creditSumU_J, currtype);
			} else {
				sSumU_J = m_creditSumU_J.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_J), null, sSumU_J);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 
	 * @author CongJinliang
	 * @since 2010-6-4
	 * @param iRow
	 * @param oValue
	 * @throws Exception
	 *             从setValue_Crebit_Js_B拷贝来的代码
	 */
	public void setValue_Crebit_Js_Q(int iRow, UFDouble oValue) throws Exception {// TODO
		try {
			UFDouble oldValue = (UFDouble) getCreditVerify()[iRow].getValue(VerifyDetailKey.CREDIT_JS_Q);
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getCreditVerify()[iRow]
						.getValue(VerifyDetailKey.CREDIT_YE_Q))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getCreditVerify()[iRow].getValue(VerifyDetailKey.CREDIT_YE_Q)).abs()) > 0) {
					return;
				}
			}
			getCreditVerify()[iRow].setValue(VerifyDetailKey.CREDIT_JS_Q, oValue);
			m_creditSum_Q = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Q);
			String sSum = null;
			String currtype = Currency.getGlobalCurrPk(null);
			if (currtype != null) {
				sSum = getFormater().formatAmount(m_creditSum_Q, currtype);
			} else {
				sSum = m_creditSum_Q.toString();
			}

			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Q), null, sSum);

			/* add by lwg */
			String sSumU_Q = null;
			m_creditSumU_Q = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_Q);
			if (currtype != null) {
				sSumU_Q = getFormater().formatAmount(m_creditSumU_Q, currtype);
			} else {
				sSumU_Q = m_creditSumU_Q.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_Q), null, sSumU_Q);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 功能：贷方辅币 作者：宋涛 创建时间：(2003-5-10 14:15:46) 参数：<|> 返回值： 算法：
	 * 
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
//	public void setValue_Crebit_Js_F(int iRow, UFDouble oValue) throws Exception {
//		try {
////			UFDouble oldValue = getCreditVerify()[iRow].getCredit_Money_F();
//			if (oldValue == null && oValue == null) {
//				return;
//			} else if (oldValue != null && oValue != null) {
//				if (oldValue.compareTo(oValue) == 0) {
//					return;
//				}
//			}
//			if (oValue != null) {
//				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getCreditVerify()[iRow]
//						.getValue(VerifyDetailKey.CREDIT_YE_F))
//						|| ((UFDouble) oValue).abs().compareTo(
//								((UFDouble) getCreditVerify()[iRow].getValue(VerifyDetailKey.CREDIT_YE_F)).abs()) > 0) {
//					return;
//				}
//			}
//			getCreditVerify()[iRow].setValue(VerifyDetailKey.CREDIT_JS_F, oValue);
//			m_creditSum_F = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_F);
//			String sSum = null;
//			if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()
//					&& ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk() != null) {
//				sSum = getFormater().formatAmount(m_creditSum_F, ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
//			} else {
//				sSum = m_creditSum_F.toString();
//			}
//			/* 刷新合计 */
//			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_F), null, sSum);
//
//			/* add by lwg */
//			String sSumU_F = null;
//			m_creditSumU_F = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_F);
//			if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()
//					&& ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk() != null) {
//				sSumU_F = getFormater().formatAmount(m_creditSumU_F,
//						ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
//			} else {
//				sSumU_F = m_creditSumU_F.toString();
//			}
//			/* 刷新余额合计 */
//			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_F), null, sSumU_F);
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//			throw e;
//		}
//	}

	/**
	 * 功能：贷方原币 作者：宋涛 创建时间：(2003-5-10 14:15:35) 参数：<|> 返回值： 算法：
	 * 
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
	public void setValue_Crebit_Js_Y(int iRow, UFDouble oValue) throws Exception {
		try {
			UFDouble oldValue = getCreditVerify()[iRow].getCredit_Money_Y();
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getCreditVerify()[iRow]
						.getValue(VerifyDetailKey.CREDIT_YE_Y))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getCreditVerify()[iRow].getValue(VerifyDetailKey.CREDIT_YE_Y)).abs()) > 0) {
					return;
				}
			}
			getCreditVerify()[iRow].setValue(VerifyDetailKey.CREDIT_JS_Y, oValue);
			String sCurrpk = getCreditVerify()[iRow].getCurrPk();
			if (sCurrpk.equals(Currency.getLocalCurrPK(getPk_accountingbook()))) {
				setValueAt(oValue, iRow, VerifyDetailKey.CREDIT_JS_B, false);
			}
			if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()
					&& sCurrpk.equals(ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk())) {
				setValueAt(oValue, iRow, VerifyDetailKey.CREDIT_JS_F, false);
			}
			m_creditSum_Y = calculateSum(getCreditVerify(), VerifyDetailKey.CREDIT_JS_Y);
			String pk_cur = getCreditVerify()[iRow].getCurrPk();
			String sSum = null;
			if (pk_cur != null) {
				sSum = getFormater().formatAmount(m_creditSum_Y, pk_cur);
			} else {
				sSum = m_creditSum_Y.toString();
			}
			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUM_Y), null, sSum);

			/* add by lwg */
			String sSumU_Y = null;
			m_creditSumU_Y = calculateSumU(getCreditVerify(), VerifyDetailKey.CREDIT_UNJS_Y);
			if (pk_cur != null) {
				sSumU_Y = getFormater().formatAmount(m_creditSumU_Y, pk_cur);
			} else {
				sSumU_Y = m_creditSumU_Y.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.CREDIT_SUMU_Y), null, sSumU_Y);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 功能：借方本币 作者：宋涛 创建时间：(2003-5-10 14:15:16) 参数：<|> 返回值： 算法：
	 * 
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
	public void setValue_Debit_Js_B(int iRow, UFDouble oValue) throws Exception {
		try {
			UFDouble oldValue = getDebitVerify()[iRow].getDebit_Money_B();
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getDebitVerify()[iRow]
						.getValue(VerifyDetailKey.DEBIT_YE_B))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getDebitVerify()[iRow].getValue(VerifyDetailKey.DEBIT_YE_B)).abs()) > 0) {
					return;
				}
			}
			getDebitVerify()[iRow].setValue(VerifyDetailKey.DEBIT_JS_B, oValue);
			m_debitSum_B = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_B);
			String sSum = null;
			if (Currency.getLocalCurrPK(getPk_accountingbook()) != null) {
				sSum = getFormater().formatAmount(m_debitSum_B, Currency.getLocalCurrPK(getPk_accountingbook()));
			} else {
				sSum = m_debitSum_B.toString();
			}
			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_B), null, sSum);

			/* add by lwg */
			String sSumU_B = null;
			m_debitSumU_B = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_B);

			if (Currency.getLocalCurrPK(getPk_accountingbook()) != null) {
				sSumU_B = getFormater().formatAmount(m_debitSumU_B, Currency.getLocalCurrPK(getPk_accountingbook()));
			} else {
				sSumU_B = m_debitSumU_B.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_B), null, sSumU_B);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @author CongJinliang
	 * @since 2010-6-4
	 * @param iRow
	 * @param oValue
	 * @throws Exception
	 *             该方法copy自setValue_Debit_Js_B
	 */
	public void setValue_Debit_Js_J(int iRow, UFDouble oValue) throws Exception {
		try {
			UFDouble oldValue = (UFDouble) getDebitVerify()[iRow].getValue(VerifyDetailKey.DEBIT_JS_J);
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getDebitVerify()[iRow]
						.getValue(VerifyDetailKey.DEBIT_YE_J))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getDebitVerify()[iRow].getValue(VerifyDetailKey.DEBIT_YE_J)).abs()) > 0) {
					return;
				}
			}
			getDebitVerify()[iRow].setValue(VerifyDetailKey.DEBIT_JS_J, oValue);
			m_debitSum_J = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_J);
			String sSum = null;
			String currtype = Currency.getGroupCurrpk(GlWorkBench.getLoginGroup());
			if (currtype != null) {
				sSum = getFormater().formatAmount(m_debitSum_J, currtype);
			} else {
				sSum = m_debitSum_J.toString();
			}
			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_J), null, sSum);

			/* add by lwg */
			String sSumU_J = null;
			m_debitSumU_J = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_J);

			if (currtype != null) {
				sSumU_J = getFormater().formatAmount(m_debitSumU_J, currtype);
			} else {
				sSumU_J = m_debitSumU_J.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_J), null, sSumU_J);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 
	 * @author CongJinliang
	 * @since 2010-6-4
	 * @param iRow
	 * @param oValue
	 * @throws Exception
	 *             该方法copy自setValue_Debit_Js_B
	 */
	public void setValue_Debit_Js_Q(int iRow, UFDouble oValue) throws Exception {
		try {
			UFDouble oldValue = (UFDouble) getDebitVerify()[iRow].getValue(VerifyDetailKey.DEBIT_JS_Q);
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getDebitVerify()[iRow]
						.getValue(VerifyDetailKey.DEBIT_YE_Q))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getDebitVerify()[iRow].getValue(VerifyDetailKey.DEBIT_YE_Q)).abs()) > 0) {
					return;
				}
			}
			getDebitVerify()[iRow].setValue(VerifyDetailKey.DEBIT_JS_Q, oValue);
			m_debitSum_Q = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_Q);
			String sSum = null;
			String currtype = Currency.getGlobalCurrPk(null);
			if (currtype != null) {
				sSum = getFormater().formatAmount(m_debitSum_Q, currtype);
			} else {
				sSum = m_debitSum_Q.toString();
			}
			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Q), null, sSum);

			/* add by lwg */
			String sSumU_Q = null;
			m_debitSumU_Q = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_Q);

			if (currtype != null) {
				sSumU_Q = getFormater().formatAmount(m_debitSumU_Q, currtype);
			} else {
				sSumU_Q = m_debitSumU_Q.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_Q), null, sSumU_Q);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 功能：借方辅币 作者：宋涛 创建时间：(2003-5-10 14:14:58) 参数：<|> 返回值： 算法：
	 * 
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
//	public void setValue_Debit_Js_F(int iRow, UFDouble oValue) throws Exception {
//		try {
//			UFDouble oldValue = getDebitVerify()[iRow].getDebit_Money_F();
//			if (oldValue == null && oValue == null) {
//				return;
//			} else if (oldValue != null && oValue != null) {
//				if (oldValue.compareTo(oValue) == 0) {
//					return;
//				}
//			}
//			if (oValue != null) {
//				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getDebitVerify()[iRow]
//						.getValue(VerifyDetailKey.DEBIT_YE_F))
//						|| ((UFDouble) oValue).abs().compareTo(
//								((UFDouble) getDebitVerify()[iRow].getValue(VerifyDetailKey.DEBIT_YE_F)).abs()) > 0) {
//					return;
//				}
//			}
//			getDebitVerify()[iRow].setValue(VerifyDetailKey.DEBIT_JS_F, oValue);
//			m_debitSum_F = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_F);
//			String sSum = null;
//			if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()
//					&& ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk() != null) {
//				sSum = getFormater().formatAmount(m_debitSum_F, ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
//			} else {
//				sSum = m_debitSum_F.toString();
//			}
//			/* 刷新合计 */
//			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_F), null, sSum);
//
//			/* add by lwg */
//			String sSumU_F = null;
//			m_debitSumU_F = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_F);
//
//			if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()
//					&& ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk() != null) {
//				sSumU_F = getFormater()
//						.formatAmount(m_debitSumU_F, ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk());
//			} else {
//				sSumU_F = m_debitSumU_F.toString();
//			}
//			/* 刷新余额合计 */
//			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_F), null, sSumU_F);
//
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//			throw e;
//		}
//	}

	/**
	 * 功能：借方原币 作者：宋涛 创建时间：(2003-5-10 14:14:45) 参数：<|> 返回值： 算法：
	 * 
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
	public void setValue_Debit_Js_Y(int iRow, UFDouble oValue) throws Exception {
		try {
			UFDouble oldValue = getDebitVerify()[iRow].getDebit_Money_Y();
			if (oldValue == null && oValue == null) {
				return;
			} else if (oldValue != null && oValue != null) {
				if (oldValue.compareTo(oValue) == 0) {
					return;
				}
			}
			if (oValue != null) {
				if (!GLRedBlueMatch.isSameSign((UFDouble) oValue, (UFDouble) getDebitVerify()[iRow]
						.getValue(VerifyDetailKey.DEBIT_YE_Y))
						|| ((UFDouble) oValue).abs().compareTo(
								((UFDouble) getDebitVerify()[iRow].getValue(VerifyDetailKey.DEBIT_YE_Y)).abs()) > 0) {
					return;
				}
			}
			getDebitVerify()[iRow].setValue(VerifyDetailKey.DEBIT_JS_Y, oValue);
			String sCurrpk = getDebitVerify()[iRow].getCurrPk();
			if (sCurrpk.equals(Currency.getLocalCurrPK(getPk_accountingbook()))) {
				setValueAt(oValue, iRow, VerifyDetailKey.DEBIT_JS_B, true);
			}
			if (ClientInfo.getInstance(getPk_accountingbook()).bFracUsed()
					&& sCurrpk.equals(ClientInfo.getInstance(getPk_accountingbook()).getPk_fracpk())) {
				setValueAt(oValue, iRow, VerifyDetailKey.DEBIT_JS_F, true);
			}
			m_debitSum_Y = calculateSum(getDebitVerify(), VerifyDetailKey.DEBIT_JS_Y);
			String sSum = null;
			if (sCurrpk != null) {
				sSum = getFormater().formatAmount(m_debitSum_Y, sCurrpk);
			} else {
				sSum = m_debitSum_Y.toString();
			}
			/* 刷新合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUM_Y), null, sSum);

			/* add by lwg */
			String sSumU_Y = null;
			m_debitSumU_Y = calculateSumU(getDebitVerify(), VerifyDetailKey.DEBIT_UNJS_Y);
			if (sCurrpk != null) {
				sSumU_Y = getFormater().formatAmount(m_debitSumU_Y, sCurrpk);
			} else {
				sSumU_Y = m_debitSumU_Y.toString();
			}
			/* 刷新余额合计 */
			firePropertyChange(String.valueOf(VerifyDetailKey.DEBIT_SUMU_Y), null, sSumU_Y);

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 功能：设置选择标志 作者：宋涛 创建时间：(2003-5-10 13:58:41) 参数：<|> 返回值： 算法：
	 * 
	 * @param bDebit
	 *            boolean
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
	protected void setValue_Selected(boolean bDebit, int iRow, nc.vo.pub.lang.UFBoolean oValue) throws Exception {
		try {
			VerifyDetailVO vo = null;
			if (bDebit) {
				vo = getDebitVerify()[iRow];
			} else {
				vo = getCreditVerify()[iRow];
			}
			if (vo.isSelected().booleanValue() == oValue.booleanValue()) {
				return;
			}
			if (oValue.booleanValue() && !isSelectedDataLegal(vo)) {
//				getVerifyUI().showErrorMsg(VerifyMsg.getMSG_MULTISELECT_ERROR());
//				return;
				throw new Exception(VerifyMsg.getMSG_MULTISELECT_ERROR());
			}
			if (oValue.booleanValue()) {
				m_iSelectedCount++;
			} else {
				m_iSelectedCount--;
			}
			if (m_iSelectedCount < 1) {
				m_iSelectedCount = 0;
				m_hashAss.clear();
			}
			vo.setSelected(oValue);
			if (bDebit) {
				if (oValue.booleanValue()) {
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_Y), iRow, VerifyDetailKey.DEBIT_JS_Y, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_F), iRow, VerifyDetailKey.DEBIT_JS_F, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_B), iRow, VerifyDetailKey.DEBIT_JS_B, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_J), iRow, VerifyDetailKey.DEBIT_JS_J, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_Q), iRow, VerifyDetailKey.DEBIT_JS_Q, bDebit);
				} else {
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_Y, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_F, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_B, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_J, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_Q, bDebit);
				}
			} else {
				if (oValue.booleanValue()) {
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_Y), iRow, VerifyDetailKey.CREDIT_JS_Y, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_F), iRow, VerifyDetailKey.CREDIT_JS_F, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_B), iRow, VerifyDetailKey.CREDIT_JS_B, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_J), iRow, VerifyDetailKey.CREDIT_JS_J, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_Q), iRow, VerifyDetailKey.CREDIT_JS_Q, bDebit);
				} else {
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_Y, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_F, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_B, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_J, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_Q, bDebit);
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 功能：设置model中的数据 作者：宋涛 创建时间：(2003-5-9 11:46:56) 参数：<|> 返回值： 算法：
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
	 *                异常说明。
	 */
	public void setValueAt(Object oValue, int iRow, int iKey, boolean bDebit) throws Exception {
		try {
			VerifyDetailVO vo = null;
			if (bDebit) {
				vo = getDebitVerify()[iRow];
			} else {
				vo = getCreditVerify()[iRow];
			}
			Object oOldValue = vo.getValue(iKey);
			switch (iKey) {
			case VerifyDetailKey.SELECTED:
				if (m_Fetch) {
					setPreviousUnSelected(bDebit);
					setValue_FetchSelected(bDebit, iRow, nc.vo.pub.lang.UFBoolean.valueOf(oValue.toString()));

				} else {
					setValue_Selected(bDebit, iRow, nc.vo.pub.lang.UFBoolean.valueOf(oValue.toString()));
				}
				break;
			case VerifyDetailKey.DEBIT_JS_Y:
				setValue_Debit_Js_Y(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
//			case VerifyDetailKey.DEBIT_JS_F:
//				setValue_Debit_Js_F(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
//				break;
			case VerifyDetailKey.DEBIT_JS_B:
				setValue_Debit_Js_B(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
			case VerifyDetailKey.DEBIT_JS_J:
				setValue_Debit_Js_J(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
			case VerifyDetailKey.DEBIT_JS_Q:
				setValue_Debit_Js_Q(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
			case VerifyDetailKey.CREDIT_JS_Y:
				setValue_Crebit_Js_Y(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
//			case VerifyDetailKey.CREDIT_JS_F:
//				setValue_Crebit_Js_F(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
//				break;
			case VerifyDetailKey.CREDIT_JS_B:
				setValue_Crebit_Js_B(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
			case VerifyDetailKey.CREDIT_JS_J:
				setValue_Crebit_Js_J(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
			case VerifyDetailKey.CREDIT_JS_Q:
				setValue_Crebit_Js_Q(iRow, oValue == null ? null : new UFDouble(oValue.toString()));
				break;
			default:
				if (oOldValue == null && oValue == null) {
					return;
				} else if (oOldValue != null && oValue != null && oOldValue.equals(oValue)) {
					return;
				} else {
					vo.setValue(iKey, oValue);
				}
				break;
			}
			firePropertyChange(String.valueOf(iKey), null, oValue);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	/**
	 * 功能：保存核销标准 作者：宋涛 创建时间：(2003-5-9 13:49:34) 参数：<|> 返回值： 算法：
	 * 
	 * @param vo
	 *            nc.vo.glrp.verify.VerifyStandardVO
	 */
	public void setVerifyStandardVO(nc.vo.glrp.verify.VerifyStandardVO vo) {
		m_Standardvo = vo;
	}

	/**
	 * 功能：在model中添加ui的引用 作者：宋涛 创建时间：(2003-5-8 15:49:52) 参数：<|> 返回值： 算法：
	 * 
	 * @param newUi
	 *            nc.ui.glrp.verify.IVerifyUI
	 */
	public void setVerifyUI(IVerifyUI newUi) {
		m_ui = newUi;
	}

	/**
	 * 功能：执行反核销 作者：宋涛 创建时间：(2003-5-8 16:14:49) 参数：<|> 返回值： 算法：
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public void unVerify() throws Exception {
		try {
			GlVerifyDisplogVO[] logs = getSelectedLogs();
			if (logs == null || logs.length <= 0) {
				m_historyUI.showMsg(VerifyMsg.getMSG_CANNTUNVERIFY());
				return;
			}
			getVerifyCom().onUnVerify(logs,getPk_accountingbook());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			m_historyUI.showErrorMsg(VerifyMsg.getMsg_unverify_error());
			throw new Exception(VerifyMsg.getMsg_unverify_error());
		}
	}

	/**
	 * 功能：设置是否处于对照状态
	 * 
	 * @param newFetch
	 */
	public void setFetch(boolean newFetch) {
		m_Fetch = newFetch;
	}

	public boolean isFetch() {
		return m_Fetch;
	}

	/**
	 * 功能：设置对照的主方向
	 * 
	 * @param newFetch
	 */
	public void setFetchDirect(int newFetchDirect) {
		m_FetchDirect = newFetchDirect;
	}

	public int getFetchDirect() {
		return m_FetchDirect;
	}

	/**
	 * 功能：设置对照选择标志 作者： 创建时间：(2005-1-10 13:58:41) 参数：<|> 返回值： 算法：
	 * 
	 * @param bDebit
	 *            boolean
	 * @param iRow
	 *            int
	 * @param oValue
	 *            java.lang.Object
	 */
	protected void setValue_FetchSelected(boolean bDebit, int iRow, nc.vo.pub.lang.UFBoolean oValue) throws Exception {
		try {
			if (m_FetchDirect == 0) {
				if (bDebit)
					m_FetchDirect = 1;
				else
					m_FetchDirect = -1;
			}

			// if(m_FetchDirect==1 && !bDebit) return ;
			// if(m_FetchDirect==-1 && bDebit) return ;

			VerifyDetailVO vo = null;
			if (bDebit) {
				vo = getDebitVerify()[iRow];
			} else {
				vo = getCreditVerify()[iRow];
			}
			if (vo.isSelected().booleanValue() == oValue.booleanValue()) {
				return;
			}
			// if (oValue.booleanValue() && !isSelectedDataLegal(vo)) {
			// getVerifyUI().showErrorMsg(VerifyMsg.MSG_MULTISELECT_ERROR);
			// return;
			// }
			if (oValue.booleanValue()) {
				m_iSelectedCount++;
			} else {
				m_iSelectedCount--;
			}
			if (m_iSelectedCount < 1) {
				m_iSelectedCount = 0;
				m_hashAss.clear();
			}
			vo.setSelected(oValue);
			if (bDebit) {
				if (oValue.booleanValue()) {
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_Y), iRow, VerifyDetailKey.DEBIT_JS_Y, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_F), iRow, VerifyDetailKey.DEBIT_JS_F, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_B), iRow, VerifyDetailKey.DEBIT_JS_B, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_J), iRow, VerifyDetailKey.DEBIT_JS_J, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.DEBIT_YE_Q), iRow, VerifyDetailKey.DEBIT_JS_Q, bDebit);
				} else {
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_Y, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_F, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_B, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_J, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.DEBIT_JS_Q, bDebit);
				}
				if (m_FetchDirect == 1) {
					setCreditVerify(oValue.booleanValue()?getFetchDetailVOs(getFetchCreditVerify(),vo):getFetchCreditVerify());
					// initSum();
				}

			} else {
				if (oValue.booleanValue()) {
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_Y), iRow, VerifyDetailKey.CREDIT_JS_Y, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_F), iRow, VerifyDetailKey.CREDIT_JS_F, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_B), iRow, VerifyDetailKey.CREDIT_JS_B, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_J), iRow, VerifyDetailKey.CREDIT_JS_J, bDebit);
					setValueAt(vo.getValue(VerifyDetailKey.CREDIT_YE_Q), iRow, VerifyDetailKey.CREDIT_JS_Q, bDebit);
				} else {
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_Y, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_F, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_B, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_J, bDebit);
					setValueAt(null, iRow, VerifyDetailKey.CREDIT_JS_Q, bDebit);
				}
				if (m_FetchDirect == -1) {
					setDebitVerify(oValue.booleanValue()?getFetchDetailVOs(getFecthDebitVerify(), vo):getFecthDebitVerify());
					// initSum();
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
	}

	private boolean matchAssInFetch(VerifyDetailVO src, VerifyDetailVO dest) {
		int c = 0;
		AssVO[] array1 = src.getAss();
		if (array1 == null)
			c++;
		AssVO[] array2 = dest.getAss();
		if (array2 == null)
			c++;
		if (c == 1)
			return false;
		if (c == 2)
			return true;

		if ((array1.length == array2.length) && array1.length == 0) {
			return true;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		for (AssVO vo : array2) {
			map.put(vo.getPk_Checktype(), vo.getPk_Checkvalue());
		}
		for (AssVO vo : array1) {
			if (map.get(vo.getPk_Checktype()) == null || !map.get(vo.getPk_Checktype()).equals(vo.getPk_Checkvalue())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 功能:根据选择的VO筛选出符合条件的VO
	 * 
	 * @param vos
	 * @param filtervo
	 * @return
	 */
	private VerifyDetailVO[] getFetchDetailVOs(VerifyDetailVO[] vos, VerifyDetailVO filtervo) {
		if (vos == null || filtervo == null)
			return null;
		Vector vec = new Vector();

		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getPk_accsubj().equals(filtervo.getPk_accsubj())) {
				// if ((vos[i].getAssid() == null && filtervo.getAssid() == null) || (vos[i].getAssid() != null &&
				// filtervo.getAssid() != null && vos[i].getAssid().equals(filtervo.getAssid()))) {
				if (matchAssInFetch(vos[i], filtervo)) {
					if (m_FetchDirect == 1 && vos[i].getLocalcreditamount().equals(filtervo.getLocaldebitamount())) {
						vec.addElement(vos[i]);
					} else if (m_FetchDirect == -1 && vos[i].getLocaldebitamount().equals(filtervo.getLocalcreditamount())) {
						vec.addElement(vos[i]);
					}
				}
			}
		}

		VerifyDetailVO[] retVOs = null;
		if (vec.size() > 0) {
			retVOs = new VerifyDetailVO[vec.size()];
			vec.copyInto(retVOs);
		}
		return retVOs;
	}

	/**
	 * 功能：设置前一个选择的为非选择
	 * 
	 */
	private void setPreviousUnSelected(boolean bDebit) throws Exception {
		if (m_FetchDirect == 1) {
			for (int i = 0; i < getDebitVerify().length; i++) {
				if (getDebitVerify()[i].isSelected().booleanValue() && bDebit) {
					setValue_FetchSelected(bDebit, i, nc.vo.pub.lang.UFBoolean.FALSE);
				}
			}
		} else if (m_FetchDirect == -1) {
			for (int i = 0; i < getCreditVerify().length; i++) {
				if (getCreditVerify()[i].isSelected().booleanValue() && !bDebit) {
					setValue_FetchSelected(bDebit, i, nc.vo.pub.lang.UFBoolean.FALSE);
				}
			}
		}
	}

	/**
	 * @return 返回 m_FecthDebitVerify。
	 */
	public nc.vo.glrp.verify.VerifyDetailVO[] getFecthDebitVerify() {
		return m_FecthDebitVerify;
	}

	/**
	 * @param fecthDebitVerify
	 *            要设置的 m_FecthDebitVerify。
	 */
	public void setFecthDebitVerify(nc.vo.glrp.verify.VerifyDetailVO[] fecthDebitVerify) {
		m_FecthDebitVerify = fecthDebitVerify;
	}

	/**
	 * @return 返回 m_FetchCreditVerify。
	 */
	public nc.vo.glrp.verify.VerifyDetailVO[] getFetchCreditVerify() {
		return m_FetchCreditVerify;
	}

	/**
	 * @param fetchCreditVerify
	 *            要设置的 m_FetchCreditVerify。
	 */
	public void setFetchCreditVerify(nc.vo.glrp.verify.VerifyDetailVO[] fetchCreditVerify) {
		m_FetchCreditVerify = fetchCreditVerify;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.glrp.verify.IVerifyModel#onCancelFetch()
	 */
	public void onCancelFetch() throws Exception {
		// TODO 自动生成方法存根
		setFetch(false);
		setFetchDirect(0);
		onQuery(m_voQry);
		// m_hashAss.clear();
		// m_iSelectedCount = 0;
		//
		// for(int i=0;i<getFecthDebitVerify().length;i++){
		// if(getFecthDebitVerify()[i].isSelected().booleanValue()){
		// getFecthDebitVerify()[i].setSelected(nc.vo.pub.lang.UFBoolean.FALSE);
		// }
		// }
		// for(int i=0;i<getFetchCreditVerify().length;i++){
		// if(getFetchCreditVerify()[i].isSelected().booleanValue()){
		// getFetchCreditVerify()[i].setSelected(nc.vo.pub.lang.UFBoolean.FALSE);
		// }
		// }
		//
		// setDebitVerify((VerifyDetailVO[])getFecthDebitVerify().clone());
		// setCreditVerify((VerifyDetailVO[])getFetchCreditVerify().clone());
		//
		// initSum();
		// firePropertyChange(String.valueOf(VerifyDetailKey.SELECTED),null,"updateall");
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.glrp.verify.IVerifyModel#onFetch()
	 */
	public void onFetch() throws Exception {
		// TODO 自动生成方法存根
		setFetch(true);
		setFetchDirect(0);
		onQuery(m_voQry);
		// m_hashAss.clear();
		// m_iSelectedCount = 0;
		//
		// for(int i=0;i<getFecthDebitVerify().length;i++){
		// if(getFecthDebitVerify()[i].isSelected().booleanValue()){
		// getFecthDebitVerify()[i].setSelected(nc.vo.pub.lang.UFBoolean.FALSE);
		// }
		// }
		// for(int i=0;i<getFetchCreditVerify().length;i++){
		// if(getFetchCreditVerify()[i].isSelected().booleanValue()){
		// getFetchCreditVerify()[i].setSelected(nc.vo.pub.lang.UFBoolean.FALSE);
		// }
		// }
		//
		// setDebitVerify((VerifyDetailVO[])getFecthDebitVerify().clone());
		// setCreditVerify((VerifyDetailVO[])getFetchCreditVerify().clone());
		//
		// initSum();
		// firePropertyChange(String.valueOf(VerifyDetailKey.SELECTED),null,"updateall");

	}

	public String getPk_accountingbook() {
		if (pk_accountingbook == null) {
			pk_accountingbook = GlWorkBench.getDefaultMainOrg();
		}
		return pk_accountingbook;
	}

	public void setPk_accountingbook(String pkAccountingbook) {
		pk_accountingbook = pkAccountingbook;
	}

}