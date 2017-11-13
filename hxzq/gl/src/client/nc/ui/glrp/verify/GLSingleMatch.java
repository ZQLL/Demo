package nc.ui.glrp.verify;

//Source file: E:\\temp\\SingleMatch.java

import nc.bs.logging.Logger;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.glcom.numbertool.GlNumberFormat;
import nc.vo.gateway60.itfs.Currency;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.glrp.com.verify.ILogVO;
import nc.vo.glrp.com.verify.IMatchTool;
import nc.vo.glrp.com.verify.IVerifyVO;
import nc.vo.glrp.com.verify.LogVOFactory;
import nc.vo.glrp.com.verify.VerifyRuleVO;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.VerifyDetailKey;
import nc.vo.glrp.verify.VerifyDetailVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
/**
 * 单币种借贷核销实现类
 */
public class GLSingleMatch implements IMatchTool {
	/*借方数据*/
	public IVerifyVO[] m_debitvos;
	/*贷方数据*/
	public IVerifyVO[] m_creditvos;
	/*核销规则vo*/
	private VerifyRuleVO m_rulevo;
	/*核销匹配记录*/
	private java.util.ArrayList m_alLogs;
	/*保存数据的hash表*/
	private java.util.Hashtable m_hash;
	private nc.vo.glrp.pub.VoComparer m_comparer;
	
	private String pk_accountingbook;
   /**
	* @roseuid 3E8A9C8F01C8
	*/
   public GLSingleMatch() 
   {
	
   }         
/**
 *  功能：将数据添加到hash表中
 *  作者：宋涛
 *  创建时间：(2003-5-5 11:02:39)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param vos nc.vo.glrp.com.verify.IVerifyVO[]
 */
   private void fill2Hash(IVerifyVO[] vos, boolean bDebit) {
	    String[] sKeys =
	        (bDebit ? m_rulevo.getDebtObjKeys() : m_rulevo.getCreditObjKeys());
	    String sCurrKey;
	    Object oCurrValues;
	    for (int i = 0; i < vos.length; i++) {
	        sCurrKey = gethashKey(vos[i], sKeys);
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
	 * 判断数据的辅助核算与自动核销按辅助项中辅助核算是否一致
	 * @param vos 当前待核销的数据
	 * @param sKeys 自动核销规则
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
					for (int j = 0; vo1.getAss()!=null&&j < vo1.getAss().length; j++) {
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
 *  功能：生成log
 *  作者：宋涛
 *  创建时间：(2003-5-5 13:35:17)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param Verifymny nc.vo.pub.lang.UFDouble[]
 * @param voDebit nc.vo.glrp.com.verify.IVerifyVO[]
 * @param voCredit nc.vo.glrp.com.verify.IVerifyVO[]
 */
private void generateLogs(UFDouble[] Verifymny, IVerifyVO voDebit, IVerifyVO voCredit) {
	ILogVO log = LogVOFactory.getNewLogVO(m_rulevo.getLogClassName());
	log.setBatchID(m_rulevo.getBatchId());
	log.setVerifyObjPK(m_rulevo.getDebitVerifyObj());
	log.setOppVerifyObjPK(m_rulevo.getCreditVerifyObj());
	log.setData(voDebit);
	log.setDataPk(voDebit.getPk());
	log.setDebitBalaMny_b(Verifymny[1]);
//	log.setDebitBalaMny_f(Verifymny[1]);
	log.setDebitBalaMny_y(Verifymny[0]);
//	log.setDebitBalaMny_j(Verifymny[3]);
//	log.setDebitBalaMny_q(Verifymny[4]);
	log.setCreDitBalaMny_b(ZERO);
//	log.setCreDitBalaMny_f(ZERO);
	log.setCreDitBalaMny_y(ZERO);
//	log.setCreDitBalaMny_j(ZERO);
//	log.setCreDitBalaMny_q(ZERO);
	log.setFlag(MainFlag);
	log.setOppData(voCredit);
	log.setOppDataPk(voCredit.getPk());
	log.setOprDate(GlWorkBench.getBusiDate());
	log.setOprPk(GlWorkBench.getLoginUser());
	log.setOprType(log.VERIFY);
	log.setPairNum(m_rulevo.getNextPairNum());
	log.setPk_group(GlWorkBench.getLoginGroup());
	m_alLogs.add(log);

	log = LogVOFactory.getNewLogVO(m_rulevo.getLogClassName());
	log.setBatchID(m_rulevo.getBatchId());
	log.setVerifyObjPK(m_rulevo.getCreditVerifyObj());
	log.setOppVerifyObjPK(m_rulevo.getDebitVerifyObj());	
	log.setData(voCredit);
	log.setDataPk(voCredit.getPk());
	log.setDebitBalaMny_b(ZERO);
//	log.setDebitBalaMny_f(ZERO);
	log.setDebitBalaMny_y(ZERO);
//	log.setDebitBalaMny_j(ZERO);
//	log.setDebitBalaMny_q(ZERO);
	log.setCreDitBalaMny_b(Verifymny[1]);
//	log.setCreDitBalaMny_f(Verifymny[1]);
	log.setCreDitBalaMny_y(Verifymny[0]);
//	log.setCreDitBalaMny_j(Verifymny[3]);
//	log.setCreDitBalaMny_q(Verifymny[4]);
	log.setFlag(AssFlag);
	log.setOppData(voDebit);
	log.setOppDataPk(voDebit.getPk());
	log.setOprDate(GlWorkBench.getBusiDate());
	log.setOprPk(GlWorkBench.getLoginUser());
	log.setOprType(log.VERIFY);
	log.setPairNum(m_rulevo.getPairNum());
	log.setPk_group(GlWorkBench.getLoginGroup());
	m_alLogs.add(log);
}
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-5 10:58:26)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return sun.misc.Compare
 */
private nc.vo.glrp.pub.VoComparer getComparer() {
	if(m_comparer==null){
		m_comparer = new nc.vo.glrp.pub.VoComparer();
	}
	return m_comparer;
}
/**
	* @return IVerifyVO[]
	* @roseuid 3E8A9C8F02C2
	*/
public IVerifyVO[] getCreditData() {
	return m_creditvos;
}
/**
	* @return IVerifyVO[]
	* @roseuid 3E8A9C8F02B3
	*/
public IVerifyVO[] getDebitData() {
	return m_debitvos;
}
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-5 11:12:49)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return java.util.Hashtable
 */
private java.util.Hashtable getHash() {
	if(m_hash==null){
		m_hash = new java.util.Hashtable();
	}
	return m_hash;
}
/**
 *  功能：得到hash键值
 *  作者：宋涛
 *  创建时间：(2003-5-5 10:44:49)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return java.lang.String
 * @param verifyvo nc.vo.glrp.com.verify.IVerifyVO
 */
private String gethashKey(IVerifyVO verifyvo, String[] skeys) {
	String shashKey = "";
	if (skeys == null || skeys.length <= 0) {
		return "null";
	}
	Object ob = null;
	for (int i = 0; i < skeys.length; i++) {
		if (skeys[i].equals("Mny")) {
			if (verifyvo.getDirect().intValue() > 0) { /*借方*/
				ob = verifyvo.getDebit_Money_Y();
				shashKey += "_" + (ob == null ? "null" : ob.toString());
//				ob = verifyvo.getDebit_Money_F();
//				shashKey += "_" + (ob == null ? "null" : ob.toString());
				ob = verifyvo.getDebit_Money_B();
				shashKey += "_" + (ob == null ? "null" : ob.toString());
//				ob = verifyvo.getDebit_Money_J();
//				shashKey += "_" + (ob == null ? "null" : ob.toString());
//				ob = verifyvo.getDebit_Money_Q();
//				shashKey += "_" + (ob == null ? "null" : ob.toString());
			} else { /*贷方*/
				ob = verifyvo.getCredit_Money_Y();
				shashKey += "_" + (ob == null ? "null" : ob.toString());
//				ob = verifyvo.getCredit_Money_F();
//				shashKey += "_" + (ob == null ? "null" : ob.toString());
				ob = verifyvo.getCredit_Money_B();
				shashKey += "_" + (ob == null ? "null" : ob.toString());
//				ob = verifyvo.getCredit_Money_J();
//				shashKey += "_" + (ob == null ? "null" : ob.toString());
//				ob = verifyvo.getCredit_Money_Q();
//				shashKey += "_" + (ob == null ? "null" : ob.toString());
			}			
		} else {
			ob = verifyvo.getAttributeValue(skeys[i]);
			shashKey += "_" + (ob == null ? "null" : ob.toString());
		}
		
	}
	return shashKey;
}
/**
	* @return LogVO[]
	* @roseuid 3E8A9C8F02E2
	*/
public ILogVO[] getLogData() {
    if (m_alLogs != null && m_alLogs.size() > 0) {
        try {
            ILogVO[] logs =
                (ILogVO[]) java.lang.reflect.Array.newInstance(
                    ((ILogVO) m_alLogs.get(0)).getClass(),
                    m_alLogs.size());
            logs = (ILogVO[]) m_alLogs.toArray(logs);
            return logs;
        } catch (Exception ex) {
nc.bs.logging.Logger.error(ex.getMessage(), ex);
            return null;
        }
    }
    return null;
}
/**
	* @return String
	* @roseuid 3E8A9C8F0320
	*/
public String getToolName() {
	return "GLSingleMatch";
}
/**
 * 功能：判断两vo是否匹配
 *  作者：宋涛
 *  创建时间：(2003-5-5 13:05:15)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return boolean
 * @param vo1 nc.vo.glrp.com.verify.IVerifyVO
 * @param vo2 nc.vo.glrp.com.verify.IVerifyVO
 */
private boolean isEqual(IVerifyVO vo1, IVerifyVO vo2) {
    if (m_rulevo.getMaxDateError() != null) {
        getComparer().setDaterange(m_rulevo.getMaxDateError().intValue());
        getComparer().setCompareKey(new String[]{m_rulevo.getDateName()});
        return getComparer().doCompare(vo1, vo2) == 0 ? true : false;
    }
    return true;
}
/**
 *  功能：是否已结清
 *  作者：宋涛
 *  创建时间：(2003-5-5 12:52:02)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return boolean
 * @param ufdMny nc.vo.pub.lang.UFDouble[]
 */
private boolean isSettled(UFDouble[] ufdMny) {
	if (ufdMny == null || ufdMny.length < 1) {
		return true;
	}
	for (int i = 0; i < ufdMny.length; i++) {
		if (ufdMny[i].doubleValue()!=ZERO.doubleValue()) { 
			return false;
		}
	}
	return true;
}
/**
 *  功能：判断合法性
 *  作者：宋涛
 *  创建时间：(2003-5-5 11:41:41)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return java.lang.String
 */
private String isValid() {
    if (m_debitvos == null
        || m_debitvos.length <= 0
        || m_creditvos == null
        || m_creditvos.length <= 0) {
        return VerifyMsg.getMSG_NOVERIFYDATA();
    }
    return null;
}
/**
	* @return void
	* @roseuid 3E8A9C8F0301
	*/
public void onMatch() throws Exception {
    try {
        String sMsg = isValid();
        if (sMsg != null) {
            throw new BusinessException(sMsg);
        }
        removeOldData();
        perpareData();

        boolean bDebitLoop = true;
        String shashKey;
        bDebitLoop = (getDebitData().length < getCreditData().length);
        IVerifyVO[] voloop = bDebitLoop ? getDebitData() : getCreditData();
        String[] sObjkeys =
            bDebitLoop ? m_rulevo.getDebtObjKeys() : m_rulevo.getCreditObjKeys();
        UFDouble[] dealMny = new UFDouble[2];
        UFDouble[] oppMny = new UFDouble[2];
        UFDouble[] verifyMny = new UFDouble[2];

        if (m_rulevo.getDateName() != null && m_rulevo.getMaxDateError() != null) {
            getComparer().setCompareKey(new String[] { m_rulevo.getDateName()});
            getComparer().setDaterange(m_rulevo.getMaxDateError().intValue());
        }
        java.util.ArrayList alOpp = null;
        IVerifyVO voOpp = null;
        for (int i = 0; i < voloop.length; i++) {
            if (bDebitLoop) {
                dealMny[0] = voloop[i].getDebit_Money_Y();
//                dealMny[1] = voloop[i].getDebit_Money_F();
                dealMny[1] = voloop[i].getDebit_Money_B();
//                dealMny[3] = voloop[i].getDebit_Money_J();
//                dealMny[4] = voloop[i].getDebit_Money_Q();
            } else {
                dealMny[0] = voloop[i].getCredit_Money_Y();
//                dealMny[1] = voloop[i].getCredit_Money_F();
                dealMny[1] = voloop[i].getCredit_Money_B();
//                dealMny[3] = voloop[i].getCredit_Money_J();
//                dealMny[4] = voloop[i].getCredit_Money_Q();
            }
            if (isSettled(dealMny)) {
                continue;
            }
            shashKey = gethashKey(voloop[i], sObjkeys);
            try {
                alOpp = (java.util.ArrayList) getHash().get(shashKey);
                if (alOpp == null || alOpp.size() == 0) {
                    continue;
                }
            } catch (Exception ex) {
                continue;
            }
            for (int j = 0; j < alOpp.size();) {
                if (isSettled(dealMny)) {
                    break;
                }
                voOpp = (IVerifyVO) alOpp.get(j);
                if (bDebitLoop) {
                    oppMny[0] = voOpp.getCredit_Money_Y();
//                    oppMny[1] = voOpp.getCredit_Money_F();
                    oppMny[1] = voOpp.getCredit_Money_B();
//                    oppMny[3] = voOpp.getCredit_Money_J();
//                    oppMny[4] = voOpp.getCredit_Money_Q();
                } else {
                    oppMny[0] = voOpp.getDebit_Money_Y();
//                    oppMny[1] = voOpp.getDebit_Money_F();
                    oppMny[1] = voOpp.getDebit_Money_B();
//                    oppMny[3] = voOpp.getDebit_Money_J();
//                    oppMny[4] = voOpp.getDebit_Money_Q();
                }
                if (isSettled(oppMny)) {
                    alOpp.remove(j);
                    continue;
                }
                if (!isEqual(voloop[i], voOpp)) {
                    j++;
                    continue;
                }
                /*判断核销金额*/
                /*如果是红对兰则按照兰字金额核销*/
				for (int m = 0;m<verifyMny.length;m++) {
					if (dealMny[m] != null && oppMny[m] != null) {
	                    verifyMny[m] = dealMny[m].abs().compareTo(oppMny[m].abs()) > 0 ? oppMny[m] : dealMny[m];
	                } else {
	                    verifyMny[m] = ZERO;
	                }
				}
//                if (dealMny[0] != null && oppMny[0] != null) {
//                    verifyMny[0] = dealMny[0].abs().compareTo(oppMny[0].abs()) > 0 ? oppMny[0] : dealMny[0];
//                } else {
//                    verifyMny[0] = ZERO;
//                }
//                if (dealMny[1] != null && oppMny[1] != null) {
//                    verifyMny[1] = dealMny[1].abs().compareTo(oppMny[1].abs()) > 0 ? oppMny[1] : dealMny[1];
//                } else {
//                    verifyMny[1] = ZERO;
//                }
//                if (dealMny[2] != null && oppMny[2] != null) {
////                	if(dealMny[2].sub(oppMny[2]).abs().compareTo(dealMny[2].abs())>0)
////                		throw new BusinessException("核销禁止选择一正一负两条记录。");
//                	verifyMny[2] = dealMny[2].abs().compareTo(oppMny[2].abs()) > 0 ? oppMny[2] : dealMny[2];
//                } else {
//                    verifyMny[2] = ZERO;
//                }
                if (isSettled(verifyMny)) {
                    j++;
                    continue;
                }
                /*形成log，回写余额*/
                if (bDebitLoop) {
                    generateLogs(verifyMny, voloop[i], voOpp);
                    if (voOpp.getCredit_Money_Y() != null && !verifyMny[0].equals(ZERO)) {
                        voOpp.setCredit_Money_Y(
                            voOpp.getCredit_Money_Y().sub(verifyMny[0], verifyMny[0].getPower()));
                    }
//                    if (voOpp.getCredit_Money_F() != null && !verifyMny[1].equals(ZERO)) {
//                        voOpp.setCredit_Money_F(
//                            voOpp.getCredit_Money_F().sub(verifyMny[1], verifyMny[1].getPower()));
//                    }
                    if (voOpp.getCredit_Money_B() != null && !verifyMny[1].equals(ZERO)) {
                        voOpp.setCredit_Money_B(
                            voOpp.getCredit_Money_B().sub(verifyMny[1], verifyMny[1].getPower()));
                    }
//                    if (voOpp.getCredit_Money_J() != null && !verifyMny[3].equals(ZERO)) {
//                    	voOpp.setCredit_Money_J(
//                    			voOpp.getCredit_Money_J().sub(verifyMny[3], verifyMny[3].getPower()));
//                    }
//                    if (voOpp.getCredit_Money_Q() != null && !verifyMny[4].equals(ZERO)) {
//                    	voOpp.setCredit_Money_Q(
//                    			voOpp.getCredit_Money_Q().sub(verifyMny[4], verifyMny[4].getPower()));
//                    }

                } else {
                    generateLogs(verifyMny, voOpp, voloop[i]);
                    if (voOpp.getDebit_Money_Y() != null && !verifyMny[0].equals(ZERO)) {
                        voOpp.setDebit_Money_Y(
                            voOpp.getDebit_Money_Y().sub(verifyMny[0], verifyMny[0].getPower()));
                    }
//                    if (voOpp.getDebit_Money_F() != null && !verifyMny[1].equals(ZERO)) {
//                        voOpp.setDebit_Money_F(
//                            voOpp.getDebit_Money_F().sub(verifyMny[1], verifyMny[1].getPower()));
//                    }
                    if (voOpp.getDebit_Money_B() != null && !verifyMny[1].equals(ZERO)) {
                        voOpp.setDebit_Money_B(
                            voOpp.getDebit_Money_B().sub(verifyMny[1], verifyMny[1].getPower()));
                    }
//                    if (voOpp.getDebit_Money_J() != null && !verifyMny[3].equals(ZERO)) {
//                    	voOpp.setDebit_Money_J(
//                    			voOpp.getDebit_Money_J().sub(verifyMny[3], verifyMny[3].getPower()));
//                    }
//                    if (voOpp.getDebit_Money_Q() != null && !verifyMny[4].equals(ZERO)) {
//                    	voOpp.setDebit_Money_Q(
//                    			voOpp.getDebit_Money_Q().sub(verifyMny[4], verifyMny[4].getPower()));
//                    }
                }
				for (int n = 0; n < dealMny.length; n++) {
					if (dealMny[n] != null && !verifyMny[0].equals(ZERO)) {
	                    dealMny[n] = dealMny[n].sub(verifyMny[n], verifyMny[n].getPower());
	                }
                }
//                if (dealMny[0] != null && !verifyMny[0].equals(ZERO)) {
//                    dealMny[0] = dealMny[0].sub(verifyMny[0], verifyMny[0].getPower());
//                }
//                if (dealMny[1] != null && !verifyMny[1].equals(ZERO)) {
//                    dealMny[1] = dealMny[1].sub(verifyMny[1], verifyMny[1].getPower());
//                }
//                if (dealMny[2] != null && !verifyMny[2].equals(ZERO)) {
//                    dealMny[2] = dealMny[2].sub(verifyMny[2], verifyMny[2].getPower());
//                }
                if (bDebitLoop) {
                	//isSettled(new UFDouble[] { voOpp.getCredit_Money_Y(),
//                    voOpp.getCredit_Money_F(),
//                    voOpp.getCredit_Money_B(),voOpp.getCredit_Money_J(),voOpp.getCredit_Money_Q()})) {
//                    alOpp.remove(j);
                    if (isSettled(new UFDouble[] { voOpp.getCredit_Money_Y(),
                    //    voOpp.getCredit_Money_F(),
                        voOpp.getCredit_Money_B()})) {
                        alOpp.remove(j);
                    }
                } else {
                    if (isSettled(new UFDouble[] { voOpp.getDebit_Money_Y(),
                        //voOpp.getDebit_Money_F(),
                        voOpp.getDebit_Money_B()})) {
                        alOpp.remove(j);
                    }
                }
            }
            /*回写余额*/
            if (bDebitLoop) {
                voloop[i].setDebit_Money_Y(dealMny[0]);
               // voloop[i].setDebit_Money_F(dealMny[1]);
                voloop[i].setDebit_Money_B(dealMny[1]);
//                voloop[i].setDebit_Money_J(dealMny[3]);
//                voloop[i].setDebit_Money_Q(dealMny[4]);
            } else {
                voloop[i].setCredit_Money_Y(dealMny[0]);
               // voloop[i].setCredit_Money_F(dealMny[1]);
                voloop[i].setCredit_Money_B(dealMny[1]);
//                voloop[i].setCredit_Money_J(dealMny[3]);
//                voloop[i].setCredit_Money_Q(dealMny[4]);
            }
        }
    } catch (Exception e) {
        Logger.error(e.getMessage(), e);
		throw new GlBusinessException(e.getMessage());
    }
}
/**
 *  功能：准备数据
 *  作者：宋涛
 *  创建时间：(2003-5-5 9:53:27)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 */
private void perpareData() throws Exception {
    try {
        if (getCreditData() == null || getDebitData() == null) {
            return;
        }
        for (int i = 0; i < getDebitData().length; i++) {
            getDebitData()[i].setDebit_Money_Y(
                GlNumberFormat.formatUFDouble(
                    getDebitData()[i].getDebit_Money_Y(),
                    Currency.getCurrDigit(getDebitData()[i].getCurrPk())));
            getDebitData()[i].setDebit_Money_B(
                GlNumberFormat.formatUFDouble(
                    getDebitData()[i].getDebit_Money_B(),
                    Currency.getCurrDigit(Currency.getLocalCurrPK(getPk_accountingbook()))));
        }
        for (int i = 0; i < getCreditData().length; i++) {
            getCreditData()[i].setCredit_Money_Y(
                GlNumberFormat.formatUFDouble(
                    getCreditData()[i].getCredit_Money_Y(),
                    Currency.getCurrDigit(getCreditData()[i].getCurrPk())));
            getCreditData()[i].setCredit_Money_B(
                GlNumberFormat.formatUFDouble(
                    getCreditData()[i].getCredit_Money_B(),
                    Currency.getCurrDigit(Currency.getLocalCurrPK(getPk_accountingbook()))));
            
        }
        /*按日期排序*/
        getComparer().setDaterange(0);
        getComparer().setAscend(m_rulevo.getVerifySeq().booleanValue());
        if (m_rulevo.getDebtSortKeys() != null) {
            int[] iIndex =m_rulevo.getDebtSortKeys()[0].equals("prepareddate") ?
                    new int[] {
                        VerifyDetailKey.VOUCHDATE,
                        VerifyDetailKey.VOUCHERTYPENAME,
                        VerifyDetailKey.DISP_VOUCHERNO,
                        VerifyDetailKey.DETAILINDEX }: 
                	new int[]{
                        VerifyDetailKey.BUSINESSDATE,
                        VerifyDetailKey.VOUCHERTYPENAME,
                        VerifyDetailKey.DISP_VOUCHERNO,
                        VerifyDetailKey.DETAILINDEX };
                nc.vo.glcom.shellsort.CShellSort objShellSort =
                    new nc.vo.glcom.shellsort.CShellSort();
                nc.vo.glcom.sorttool.CVoSortTool objVoSortTool =
                    new nc.vo.glcom.sorttool.CVoSortTool();
                objVoSortTool.setSortIndex(iIndex);
                objShellSort.sort(getDebitData(), objVoSortTool, !m_rulevo.getVerifySeq().booleanValue());
        } else {
            getComparer().setCompareKey(new String[] { m_rulevo.getDateName()});
            sun.misc.Sort.quicksort(getDebitData(), getComparer());
        }
        if (m_rulevo.getCreditSortKeys() != null) {
            int[] iIndex =m_rulevo.getCreditSortKeys()[0].equals("prepareddate") ?
                    new int[] {
                        VerifyDetailKey.VOUCHDATE,
                        VerifyDetailKey.VOUCHERTYPENAME,
                        VerifyDetailKey.DISP_VOUCHERNO,
                        VerifyDetailKey.DETAILINDEX }: 
                	new int[]{
                        VerifyDetailKey.BUSINESSDATE,
                        VerifyDetailKey.VOUCHERTYPENAME,
                        VerifyDetailKey.DISP_VOUCHERNO,
                        VerifyDetailKey.DETAILINDEX };
                nc.vo.glcom.shellsort.CShellSort objShellSort =
                    new nc.vo.glcom.shellsort.CShellSort();
                nc.vo.glcom.sorttool.CVoSortTool objVoSortTool =
                    new nc.vo.glcom.sorttool.CVoSortTool();
                objVoSortTool.setSortIndex(iIndex);
                objShellSort.sort(getCreditData(), objVoSortTool, !m_rulevo.getVerifySeq().booleanValue());        	
        } else {
            getComparer().setCompareKey(new String[] { m_rulevo.getDateName()});
            sun.misc.Sort.quicksort(getCreditData(), getComparer());
        }

        if (getCreditData().length > getDebitData().length) {
            fill2Hash(getCreditData(), false);
        } else {
            fill2Hash(getDebitData(), true);
        }
    } catch (Exception e) {
        Logger.error(e.getMessage(), e);
		throw new GlBusinessException(e.getMessage());
    }
}
/**
 *  功能：清除旧数据
 *  作者：宋涛
 *  创建时间：(2003-5-5 9:47:32)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 */
private void removeOldData() {
	m_alLogs = new java.util.ArrayList();
	getHash().clear();
}
/**
	* @param creditDataVo
	* @return void
	* @roseuid 3E8A9C8F0265
	*/
public void setCreditData(IVerifyVO[] creditDataVo) {
	m_creditvos = creditDataVo;
}
/**
	* @param debtDataVos
	* @return void
	* @roseuid 3E8A9C8F0217
	*/
public void setDebitData(IVerifyVO[] debtDataVos) {
	m_debitvos = debtDataVos;
}
/**
	* @param rulevo
	* @return void
	* @roseuid 3E8A9C8F01D8
	*/
public void setRule(VerifyRuleVO rulevo) {
	m_rulevo = rulevo;
}
/**
 *  功能：根据排序字段排序hash中内容数据
 *  作者：宋涛
 *  创建时间：(2003-5-5 11:10:53)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param sSortKeys java.lang.String[]
 */
private void sortHash(String[] sSortKeys) {
	//java.util.Enumeration em = getHash().keys();
	//String key;
	//while (em.hasMoreElements()) {
		//key = em.nextElement().toString();
		//java.util.ArrayList al = (java.util.ArrayList) getHash().get(key);
		//IVerifyVO[] vo = new IVerifyVO[al.size()];
		//if (al.size() > 0) {
			//vo = (IVerifyVO[]) al.toArray(vo);
		//}
		//getComparer().setCompareKey(sSortKeys);
		//sun.misc.Sort.quicksort(vo,getComparer());
		//getHash().put(key, vo);
	//}
}
public String getPk_accountingbook() {
	return pk_accountingbook;
}
public void setPk_accountingbook(String pkAccountingbook) {
	pk_accountingbook = pkAccountingbook;
}

}
