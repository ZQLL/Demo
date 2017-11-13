package nc.ui.glrp.verify;

//Source file: E:\\temp\\RedBlueMatch.java
import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.glcom.numbertool.GlNumberFormat;
import nc.vo.gateway60.itfs.Currency;
import nc.vo.glrp.com.verify.ILogVO;
import nc.vo.glrp.com.verify.IMatchTool;
import nc.vo.glrp.com.verify.IVerifyVO;
import nc.vo.glrp.com.verify.LogVOFactory;
import nc.vo.glrp.com.verify.VerifyRuleVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 红蓝对冲实现类
 */
public class GLRedBlueMatch implements IMatchTool {
	/* 借方数据 */
	public IVerifyVO[] m_debitvos;

	/* 贷方数据 */
	public IVerifyVO[] m_creditvos;

	/* 核销规则vo */
	private VerifyRuleVO m_rulevo;

	/* 核销匹配记录 */
	private java.util.ArrayList m_alLogs;

	private nc.vo.glrp.pub.VoComparer m_comparer;
	
	private String pk_accountingbook;

	/**
	 * @roseuid 3E8A9C90014B
	 */
	public GLRedBlueMatch() {
	}

	/**
	 * 执行单一方向的红蓝对冲 创建日期：(2003-5-13 22:48:26)
	 * 
	 * @param voPositive
	 *            nc.vo.glrp.com.verify.IVerifyVO[]
	 * @param voNegative
	 *            nc.vo.glrp.com.verify.IVerifyVO[]
	 * @param bDebit
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void doMatch(ArrayList alPositive, ArrayList alNegative, boolean bDebit) throws java.lang.Exception {
		if (alPositive.size() < 1 || alNegative.size() < 1) {
			return;
		}
		UFDouble[] dealMny = new UFDouble[2];
		UFDouble[] oppMny = new UFDouble[2];
		UFDouble[] verifyMny = new UFDouble[2];
		IVerifyVO voNegative = null;
		for (int i = 0; i < alPositive.size(); i++) {
			IVerifyVO voPositive = (IVerifyVO) alPositive.get(i);
			if (bDebit) {
				dealMny[0] = voPositive.getDebit_Money_Y();
//				dealMny[1] = voPositive.getDebit_Money_F();
				dealMny[1] = voPositive.getDebit_Money_B();
//				dealMny[3] = voPositive.getDebit_Money_J();
//				dealMny[4] = voPositive.getDebit_Money_Q();
			} else {
				dealMny[0] = voPositive.getCredit_Money_Y();
//				dealMny[1] = voPositive.getCredit_Money_F();
				dealMny[1] = voPositive.getCredit_Money_B();
//				dealMny[3] = voPositive.getCredit_Money_J();
//				dealMny[4] = voPositive.getCredit_Money_Q();
			}
			if (isSettled(dealMny)) {
				continue;
			}
			for (int j = 0; j < alNegative.size();) {
				if (isSettled(dealMny)) {
					break;
				}
				voNegative = (IVerifyVO) alNegative.get(j);
				// v5.0
				if (!isEqual(voNegative, voPositive)) {
					// if (m_rulevo.getMaxDateError() != null && Math.abs(UFDate.getDaysBetween(voNegative.getDate(), voPositive.getDate())) > m_rulevo.getMaxDateError().intValue()) {
					j++;
					continue;
				}
//				 if(bDebit && !voNegative.getDebit_Money_B().abs().equals(voPositive.getDebit_Money_B().abs())){
//				 j++;continue;
//				 }
				// end
				if (bDebit) {
					oppMny[0] = voNegative.getDebit_Money_Y();
//					oppMny[1] = voNegative.getDebit_Money_F();
					oppMny[1] = voNegative.getDebit_Money_B();
//					oppMny[3] = voNegative.getDebit_Money_J();
//					oppMny[4] = voNegative.getDebit_Money_Q();
				} else {
					oppMny[0] = voNegative.getCredit_Money_Y();
//					oppMny[1] = voNegative.getCredit_Money_F();
					oppMny[1] = voNegative.getCredit_Money_B();
//					oppMny[3] = voNegative.getCredit_Money_J();
//					oppMny[4] = voNegative.getCredit_Money_Q();
				}
				if (isSettled(oppMny)) {
					alNegative.remove(j);
					continue;
				}
				
				for (int m = 0; m < verifyMny.length; m++) {
					if (dealMny[m] != null && oppMny[m] != null) {
						verifyMny[m] = dealMny[m].abs().compareTo(oppMny[m].abs()) > 0 ? oppMny[m] : dealMny[m];
					} else {
						verifyMny[m] = ZERO;
					}
				}
				if (dealMny[0] != null && oppMny[0] != null) {
					verifyMny[0] = dealMny[0].abs().compareTo(oppMny[0].abs()) > 0 ? oppMny[0] : dealMny[0];
				} else {
					verifyMny[0] = ZERO;
				}
				if (dealMny[1] != null && oppMny[1] != null) {
					verifyMny[1] = dealMny[1].abs().compareTo(oppMny[1].abs()) > 0 ? oppMny[1] : dealMny[1];
				} else {
					verifyMny[1] = ZERO;
				}
//				if (dealMny[2] != null && oppMny[2] != null) {
//					verifyMny[2] = dealMny[2].abs().compareTo(oppMny[2].abs()) > 0 ? oppMny[2] : dealMny[2];
//				} else {
//					verifyMny[2] = ZERO;
//				}
				if (isSettled(verifyMny)) {
					j++;
					continue;
				}

				/* 形成log，回写余额 */
				generateLogs(verifyMny, voPositive, voNegative, bDebit);
				//TODO 该方法需要重写或添加新币种
				if (bDebit) {
					if (voNegative.getDebit_Money_Y() != null && !verifyMny[0].equals(ZERO)) {
						if (isSameSign(voNegative.getDebit_Money_Y(), verifyMny[0])) {
							voNegative.setDebit_Money_Y(voNegative.getDebit_Money_Y().sub(verifyMny[0], verifyMny[0].getPower()));
							if (dealMny[0] != null) {
								dealMny[0] = dealMny[0].add(verifyMny[0], verifyMny[0].getPower());
							}
						} else {
							voNegative.setDebit_Money_Y(voNegative.getDebit_Money_Y().add(verifyMny[0], verifyMny[0].getPower()));
							if (dealMny[0] != null) {
								dealMny[0] = dealMny[0].sub(verifyMny[0], verifyMny[0].getPower());
							}
						}
					}
//					if (voNegative.getDebit_Money_F() != null && !verifyMny[1].equals(ZERO)) {
//						if (isSameSign(voNegative.getDebit_Money_F(), verifyMny[1])) {
//							voNegative.setDebit_Money_F(voNegative.getDebit_Money_F().sub(verifyMny[1], verifyMny[1].getPower()));
//							if (dealMny[1] != null) {
//								dealMny[1] = dealMny[1].add(verifyMny[1], verifyMny[1].getPower());
//							}
//						} else {
//							voNegative.setDebit_Money_F(voNegative.getDebit_Money_F().add(verifyMny[1], verifyMny[1].getPower()));
//							if (dealMny[1] != null) {
//								dealMny[1] = dealMny[1].sub(verifyMny[1], verifyMny[1].getPower());
//							}
//						}
//					}
					if (voNegative.getDebit_Money_B() != null && !verifyMny[1].equals(ZERO)) {
						if (isSameSign(voNegative.getDebit_Money_B(), verifyMny[1])) {
							voNegative.setDebit_Money_B(voNegative.getDebit_Money_B().sub(verifyMny[1], verifyMny[1].getPower()));
							if (dealMny[1] != null) {
								dealMny[1] = dealMny[1].add(verifyMny[1], verifyMny[1].getPower());
							}
						} else {
							voNegative.setDebit_Money_B(voNegative.getDebit_Money_B().add(verifyMny[1], verifyMny[1].getPower()));
							if (dealMny[1] != null) {
								dealMny[1] = dealMny[1].sub(verifyMny[1], verifyMny[1].getPower());
							}
						}
					}
					if (isSettled(new UFDouble[] {
							voNegative.getDebit_Money_Y(),
//							voNegative.getDebit_Money_F(),
							voNegative.getDebit_Money_B()
//							voNegative.getDebit_Money_J(),
//							voNegative.getDebit_Money_Q() 
							})) {
						alNegative.remove(j);
					}
				} else {
					if (voNegative.getCredit_Money_Y() != null && !verifyMny[0].equals(ZERO)) {
						if (isSameSign(voNegative.getCredit_Money_Y(), verifyMny[0])) {
							voNegative.setCredit_Money_Y(voNegative.getCredit_Money_Y().sub(verifyMny[0], verifyMny[0].getPower()));
							if (dealMny[0] != null) {
								dealMny[0] = dealMny[0].add(verifyMny[0], verifyMny[0].getPower());
							}
						} else {
							voNegative.setCredit_Money_Y(voNegative.getCredit_Money_Y().add(verifyMny[0], verifyMny[0].getPower()));
							if (dealMny[0] != null) {
								dealMny[0] = dealMny[0].sub(verifyMny[0], verifyMny[0].getPower());
							}
						}
					}
//					if (voNegative.getCredit_Money_F() != null && !verifyMny[1].equals(ZERO)) {
//						if (isSameSign(voNegative.getCredit_Money_F(), verifyMny[1])) {
//							voNegative.setCredit_Money_F(voNegative.getCredit_Money_F().sub(verifyMny[1], verifyMny[1].getPower()));
//							if (dealMny[1] != null) {
//								dealMny[1] = dealMny[1].add(verifyMny[1], verifyMny[1].getPower());
//							}
//						} else {
//							voNegative.setCredit_Money_F(voNegative.getCredit_Money_F().add(verifyMny[1], verifyMny[1].getPower()));
//							if (dealMny[1] != null) {
//								dealMny[1] = dealMny[1].sub(verifyMny[1], verifyMny[1].getPower());
//							}
//						}
//					}
					if (voNegative.getCredit_Money_B() != null && !verifyMny[1].equals(ZERO)) {
						if (isSameSign(voNegative.getCredit_Money_B(), verifyMny[1])) {
							voNegative.setCredit_Money_B(voNegative.getCredit_Money_B().sub(verifyMny[1], verifyMny[1].getPower()));
							if (dealMny[1] != null) {
								dealMny[1] = dealMny[1].add(verifyMny[1], verifyMny[1].getPower());
							}
						} else {
							voNegative.setCredit_Money_B(voNegative.getCredit_Money_B().add(verifyMny[1], verifyMny[1].getPower()));
							if (dealMny[1] != null) {
								dealMny[1] = dealMny[1].sub(verifyMny[1], verifyMny[1].getPower());
							}
						}
					}
					if (isSettled(new UFDouble[] {
							voNegative.getCredit_Money_Y(),
//							voNegative.getCredit_Money_F(),
							voNegative.getCredit_Money_B() })) {
						alNegative.remove(j);
					}

				}

			}

			/* 回写余额 */
			if (bDebit) {
				voPositive.setDebit_Money_Y(dealMny[0]);
//				voPositive.setDebit_Money_F(dealMny[1]);
				voPositive.setDebit_Money_B(dealMny[1]);
			} else {
				voPositive.setCredit_Money_Y(dealMny[0]);
//				voPositive.setCredit_Money_F(dealMny[1]);
				voPositive.setCredit_Money_B(dealMny[1]);
			}
		}
	}

	/**
	 * 功能：生成log 作者：宋涛 创建时间：(2003-5-5 13:35:17) 参数：<|> 返回值： 算法：
	 * 
	 * @param Verifymny
	 *            nc.vo.pub.lang.UFDouble[]
	 * @param voDebit
	 *            nc.vo.glrp.com.verify.IVerifyVO[]
	 * @param voCredit
	 *            nc.vo.glrp.com.verify.IVerifyVO[]
	 */
	private void generateLogs(UFDouble[] Verifymny, IVerifyVO voDebit, IVerifyVO voCredit, boolean bDebit) {
		ILogVO log = LogVOFactory.getNewLogVO(m_rulevo.getLogClassName());
		log.setBatchID(m_rulevo.getBatchId());
		log.setData(voDebit);
		log.setDataPk(voDebit.getPk());
		log.setFlag(MainFlag);
		log.setOppData(voCredit);
		log.setOppDataPk(voCredit.getPk());
		log.setOprDate(GlWorkBench.getBusiDate());
		log.setOprPk(GlWorkBench.getLoginUser());
		log.setOprType(ILogVO.REDBLUE);
		log.setPairNum(m_rulevo.getNextPairNum());
		log.setPk_group(GlWorkBench.getLoginGroup());
		if (bDebit) {
			log.setVerifyObjPK(m_rulevo.getDebitVerifyObj());
			log.setOppVerifyObjPK(m_rulevo.getDebitVerifyObj());
			log.setCreDitBalaMny_b(ZERO);
//			log.setCreDitBalaMny_f(ZERO);
			log.setCreDitBalaMny_y(ZERO);
//			log.setCreDitBalaMny_j(ZERO);
//			log.setCreDitBalaMny_q(ZERO);
//			if (isSameSign(voDebit.getDebit_Money_Q(), Verifymny[4])) {
//				log.setDebitBalaMny_q(Verifymny[4]);
//			} else {
//				log.setDebitBalaMny_q(Verifymny[4].multiply(-1));
//			}
//			if (isSameSign(voDebit.getDebit_Money_J(), Verifymny[3])) {
//				log.setDebitBalaMny_j(Verifymny[3]);
//			} else {
//				log.setDebitBalaMny_j(Verifymny[3].multiply(-1));
//			}
			if (isSameSign(voDebit.getDebit_Money_B(), Verifymny[1])) {
				log.setDebitBalaMny_b(Verifymny[1]);
			} else {
				log.setDebitBalaMny_b(Verifymny[1].multiply(-1));
			}
//			if (isSameSign(voDebit.getDebit_Money_F(), Verifymny[1])) {
//				log.setDebitBalaMny_f(Verifymny[1]);
//			} else {
//				log.setDebitBalaMny_f(Verifymny[1].multiply(-1));
//			}
			if (isSameSign(voDebit.getDebit_Money_Y(), Verifymny[0])) {
				log.setDebitBalaMny_y(Verifymny[0]);
			} else {
				log.setDebitBalaMny_y(Verifymny[0].multiply(-1));
			}
		} else {
			log.setVerifyObjPK(m_rulevo.getCreditVerifyObj());
			log.setOppVerifyObjPK(m_rulevo.getCreditVerifyObj());
//			if (isSameSign(voDebit.getCredit_Money_Q(), Verifymny[4])) {
//				log.setCreDitBalaMny_q(Verifymny[4]);
//			} else {
//				log.setCreDitBalaMny_q(Verifymny[4].multiply(-1));
//			}
//			if (isSameSign(voDebit.getCredit_Money_J(), Verifymny[3])) {
//				log.setCreDitBalaMny_j(Verifymny[3]);
//			} else {
//				log.setCreDitBalaMny_j(Verifymny[3].multiply(-1));
//			}
			if (isSameSign(voDebit.getCredit_Money_B(), Verifymny[1])) {
				log.setCreDitBalaMny_b(Verifymny[1]);
			} else {
				log.setCreDitBalaMny_b(Verifymny[1].multiply(-1));
			}
//			if (isSameSign(voDebit.getCredit_Money_F(), Verifymny[1])) {
//				log.setCreDitBalaMny_f(Verifymny[1]);
//			} else {
//				log.setCreDitBalaMny_f(Verifymny[1].multiply(-1));
//			}
			if (isSameSign(voDebit.getCredit_Money_Y(), Verifymny[0])) {
				log.setCreDitBalaMny_y(Verifymny[0]);
			} else {
				log.setCreDitBalaMny_y(Verifymny[0].multiply(-1));
			}
			log.setDebitBalaMny_b(ZERO);
//			log.setDebitBalaMny_f(ZERO);
			log.setDebitBalaMny_y(ZERO);
//			log.setDebitBalaMny_j(ZERO);
//			log.setDebitBalaMny_q(ZERO);
		}
		generateLogs(Verifymny, voDebit, bDebit, log);
		m_alLogs.add(log);

		log = LogVOFactory.getNewLogVO(m_rulevo.getLogClassName());
		log.setBatchID(m_rulevo.getBatchId());
		log.setData(voCredit);
		log.setDataPk(voCredit.getPk());
		log.setFlag(AssFlag);
		log.setOppData(voDebit);
		log.setOppDataPk(voDebit.getPk());
		log.setOprDate(GlWorkBench.getBusiDate());
		log.setOprPk(GlWorkBench.getLoginUser());
		log.setOprType(ILogVO.REDBLUE);
		log.setPairNum(m_rulevo.getPairNum());
		log.setPk_group(GlWorkBench.getLoginGroup());
		if (bDebit) {
			log.setVerifyObjPK(m_rulevo.getDebitVerifyObj());
			log.setOppVerifyObjPK(m_rulevo.getDebitVerifyObj());
			log.setCreDitBalaMny_b(ZERO);
//			log.setCreDitBalaMny_f(ZERO);
			log.setCreDitBalaMny_y(ZERO);
//			log.setCreDitBalaMny_j(ZERO);
//			log.setCreDitBalaMny_q(ZERO);
//			if (isSameSign(voCredit.getDebit_Money_Q(), Verifymny[4])) {
//				log.setDebitBalaMny_q(Verifymny[4]);
//			} else {
//				log.setDebitBalaMny_q(Verifymny[4].multiply(-1));
//			}
//			if (isSameSign(voCredit.getDebit_Money_J(), Verifymny[3])) {
//				log.setDebitBalaMny_j(Verifymny[3]);
//			} else {
//				log.setDebitBalaMny_j(Verifymny[3].multiply(-1));
//			}
			if (isSameSign(voCredit.getDebit_Money_B(), Verifymny[1])) {
				log.setDebitBalaMny_b(Verifymny[1]);
			} else {
				log.setDebitBalaMny_b(Verifymny[1].multiply(-1));
			}
//			if (isSameSign(voCredit.getDebit_Money_F(), Verifymny[1])) {
//				log.setDebitBalaMny_f(Verifymny[1]);
//			} else {
//				log.setDebitBalaMny_f(Verifymny[1].multiply(-1));
//			}
			if (isSameSign(voCredit.getDebit_Money_Y(), Verifymny[0])) {
				log.setDebitBalaMny_y(Verifymny[0]);
			} else {
				log.setDebitBalaMny_y(Verifymny[0].multiply(-1));
			}
		} else {
			log.setVerifyObjPK(m_rulevo.getCreditVerifyObj());
			log.setOppVerifyObjPK(m_rulevo.getCreditVerifyObj());
//			if (isSameSign(voCredit.getCredit_Money_Q(), Verifymny[4])) {
//				log.setCreDitBalaMny_q(Verifymny[4]);
//			} else {
//				log.setCreDitBalaMny_q(Verifymny[4].multiply(-1));
//			}
//			if (isSameSign(voCredit.getCredit_Money_J(), Verifymny[3])) {
//				log.setCreDitBalaMny_j(Verifymny[3]);
//			} else {
//				log.setCreDitBalaMny_j(Verifymny[3].multiply(-1));
//			}
			if (isSameSign(voCredit.getCredit_Money_B(), Verifymny[1])) {
				log.setCreDitBalaMny_b(Verifymny[1]);
			} else {
				log.setCreDitBalaMny_b(Verifymny[1].multiply(-1));
			}
//			if (isSameSign(voCredit.getCredit_Money_F(), Verifymny[1])) {
//				log.setCreDitBalaMny_f(Verifymny[1]);
//			} else {
//				log.setCreDitBalaMny_f(Verifymny[1].multiply(-1));
//			}
			if (isSameSign(voCredit.getCredit_Money_Y(), Verifymny[0])) {
				log.setCreDitBalaMny_y(Verifymny[0]);
			} else {
				log.setCreDitBalaMny_y(Verifymny[0].multiply(-1));
			}
			log.setDebitBalaMny_b(ZERO);
//			log.setDebitBalaMny_f(ZERO);
			log.setDebitBalaMny_y(ZERO);
//			log.setDebitBalaMny_j(ZERO);
//			log.setDebitBalaMny_q(ZERO);
		}
		generateLogs(Verifymny, voCredit, bDebit, log);
		m_alLogs.add(log);
	}
	
	private void generateLogs(UFDouble[] Verifymny, IVerifyVO vo,
			boolean bDebit, ILogVO resultLog) {
		if (bDebit) {
			resultLog.setVerifyObjPK(m_rulevo.getDebitVerifyObj());
			resultLog.setOppVerifyObjPK(m_rulevo.getDebitVerifyObj());
			resultLog.setCreDitBalaMny_b(ZERO);
//			resultLog.setCreDitBalaMny_f(ZERO);
			resultLog.setCreDitBalaMny_y(ZERO);
//			resultLog.setCreDitBalaMny_j(ZERO);
//			resultLog.setCreDitBalaMny_q(ZERO);
//			if (isSameSign(vo.getDebit_Money_Q(), Verifymny[4])) {
//				resultLog.setDebitBalaMny_q(Verifymny[4]);
//			} else {
//				resultLog.setDebitBalaMny_q(Verifymny[4].multiply(-1));
//			}
//			if (isSameSign(vo.getDebit_Money_J(), Verifymny[3])) {
//				resultLog.setDebitBalaMny_j(Verifymny[3]);
//			} else {
//				resultLog.setDebitBalaMny_j(Verifymny[3].multiply(-1));
//			}
			if (isSameSign(vo.getDebit_Money_B(), Verifymny[1])) {
				resultLog.setDebitBalaMny_b(Verifymny[1]);
			} else {
				resultLog.setDebitBalaMny_b(Verifymny[1].multiply(-1));
			}
//			if (isSameSign(vo.getDebit_Money_F(), Verifymny[1])) {
//				resultLog.setDebitBalaMny_f(Verifymny[1]);
//			} else {
//				resultLog.setDebitBalaMny_f(Verifymny[1].multiply(-1));
//			}
			if (isSameSign(vo.getDebit_Money_Y(), Verifymny[0])) {
				resultLog.setDebitBalaMny_y(Verifymny[0]);
			} else {
				resultLog.setDebitBalaMny_y(Verifymny[0].multiply(-1));
			}
		} else {
			resultLog.setVerifyObjPK(m_rulevo.getCreditVerifyObj());
			resultLog.setOppVerifyObjPK(m_rulevo.getCreditVerifyObj());
//			if (isSameSign(vo.getCredit_Money_Q(), Verifymny[4])) {
//				resultLog.setCreDitBalaMny_q(Verifymny[4]);
//			} else {
//				resultLog.setCreDitBalaMny_q(Verifymny[4].multiply(-1));
//			}
//			if (isSameSign(vo.getCredit_Money_J(), Verifymny[3])) {
//				resultLog.setCreDitBalaMny_j(Verifymny[3]);
//			} else {
//				resultLog.setCreDitBalaMny_j(Verifymny[3].multiply(-1));
//			}
			if (isSameSign(vo.getCredit_Money_B(), Verifymny[1])) {
				resultLog.setCreDitBalaMny_b(Verifymny[1]);
			} else {
				resultLog.setCreDitBalaMny_b(Verifymny[1].multiply(-1));
			}
//			if (isSameSign(vo.getCredit_Money_F(), Verifymny[1])) {
//				resultLog.setCreDitBalaMny_f(Verifymny[1]);
//			} else {
//				resultLog.setCreDitBalaMny_f(Verifymny[1].multiply(-1));
//			}
			if (isSameSign(vo.getCredit_Money_Y(), Verifymny[0])) {
				resultLog.setCreDitBalaMny_y(Verifymny[0]);
			} else {
				resultLog.setCreDitBalaMny_y(Verifymny[0].multiply(-1));
			}
			resultLog.setDebitBalaMny_b(ZERO);
//			resultLog.setDebitBalaMny_f(ZERO);
			resultLog.setDebitBalaMny_y(ZERO);
//			resultLog.setDebitBalaMny_j(ZERO);
//			resultLog.setDebitBalaMny_q(ZERO);
		}
	}
	

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-5 10:58:26) 参数：<|> 返回值： 算法：
	 * 
	 * @return sun.misc.Compare
	 */
	private nc.vo.glrp.pub.VoComparer getComparer() {
		if (m_comparer == null) {
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
	 * @return LogVO[]
	 * @roseuid 3E8A9C8F02E2
	 */
	public ILogVO[] getLogData() {
		if (m_alLogs != null && m_alLogs.size() > 0) {
			try {
				ILogVO[] logs = (ILogVO[]) java.lang.reflect.Array.newInstance(((ILogVO) m_alLogs.get(0)).getClass(), m_alLogs.size());
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
		return "GLRedBlueMatch";
	}

	/**
	 * 功能：是否红字凭证记录 作者：宋涛 创建时间：(2003-5-14 9:22:32) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param voDetail
	 *            nc.vo.glrp.com.verify.IVerifyVO
	 * @param bDebit
	 *            boolean
	 */
	private boolean isRed(IVerifyVO voDetail, boolean bDebit) {
//		nc.vo.pub.lang.UFDouble[] dMny = new nc.vo.pub.lang.UFDouble[5];
		List<UFDouble> dMny = new ArrayList<UFDouble>();
		if (bDebit) {
//			dMny[0] = voDetail.getDebit_Money_Y();
//			dMny[1] = voDetail.getDebit_Money_F();
//			dMny[2] = voDetail.getDebit_Money_B();
//			dMny[3] = voDetail.getDebit_Money_J();
//			dMny[4] = voDetail.getDebit_Money_Q();
			dMny.add(voDetail.getDebit_Money_Y());
			dMny.add(voDetail.getDebit_Money_B());
//			dMny.add(voDetail.getDebit_Money_J());
//			dMny.add(voDetail.getDebit_Money_Q());
		} else {
//			dMny[0] = voDetail.getCredit_Money_Y();
//			dMny[1] = voDetail.getCredit_Money_F();
//			dMny[2] = voDetail.getCredit_Money_B();
//			dMny[3] = voDetail.getCredit_Money_J();
//			dMny[4] = voDetail.getCredit_Money_Q();
			dMny.add(voDetail.getCredit_Money_Y());
			dMny.add(voDetail.getCredit_Money_B());
//			dMny.add(voDetail.getCredit_Money_J());
//			dMny.add(voDetail.getCredit_Money_Q());
		}
//		if (dMny[0] != null && !dMny[0].equals(ZERO)) {
//			return dMny[0].doubleValue() < 0;
//		} else if (dMny[2] != null && !dMny[2].equals(ZERO)) {
//			return dMny[2].doubleValue() < 0;
//		} else if (dMny[1] != null && !dMny[1].equals(ZERO)) {
//			return dMny[1].doubleValue() < 0;
//		}
		for(UFDouble ufDouble:dMny){
			if(ufDouble != null && !ufDouble.equals(ZERO)){
				return ufDouble.doubleValue() < 0;
			}
		}
		return false;
	}

	/**
	 * 功能：判断两个数是否同方向 作者：宋涛 创建时间：(2003-5-14 10:07:50) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param value1
	 *            nc.vo.pub.lang.UFDouble
	 * @param value2
	 *            nc.vo.pub.lang.UFDouble
	 */
	public static boolean isSameSign(UFDouble value1, UFDouble value2) {
		boolean bNegtive1 = value1.toString().startsWith("-");
		boolean bNegtive2 = value2.toString().startsWith("-");
		return (bNegtive1 == bNegtive2);
	}

	/**
	 * 功能：是否已结清 作者：宋涛 创建时间：(2003-5-5 12:52:02) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param ufdMny
	 *            nc.vo.pub.lang.UFDouble[]
	 */
	private boolean isSettled(UFDouble[] ufdMny) {
		if (ufdMny == null || ufdMny.length < 1) {
			return true;
		}
		for (int i = 0; i < ufdMny.length; i++) {
			if (!ufdMny[i].equals(ZERO)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return void
	 * @roseuid 3E8A9C8F0301
	 */
	public void onMatch() throws Exception {
		try {
			m_alLogs = new java.util.ArrayList();

			ArrayList alPositive = new ArrayList();
			ArrayList alNegative = new ArrayList();
			if (getDebitData() != null && getDebitData().length > 1) {
				for (int i = 0; i < getDebitData().length; i++) {
					getDebitData()[i].setDebit_Money_Y(GlNumberFormat.formatUFDouble(getDebitData()[i].getDebit_Money_Y(), Currency.getCurrDigit(getDebitData()[i].getCurrPk())));
//					getDebitData()[i].setDebit_Money_F(GlNumberFormat.formatUFDouble(getDebitData()[i].getDebit_Money_F(), ClientInfo.getInstance().getFracDigit()));
					getDebitData()[i].setDebit_Money_B(GlNumberFormat.formatUFDouble(getDebitData()[i].getDebit_Money_B(), Currency.getCurrDigit(Currency.getLocalCurrPK(getPk_accountingbook()))));
//					getDebitData()[i].setDebit_Money_J(GlNumberFormat.formatUFDouble(getDebitData()[i].getDebit_Money_J(), Currency.getCurrDigit(Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()))));
//					getDebitData()[i].setDebit_Money_Q(GlNumberFormat.formatUFDouble(getDebitData()[i].getDebit_Money_Q(), Currency.getCurrDigit(Currency.getGlobalCurrPk(null))));
				}
				/* 按日期排序 */
				getComparer().setDaterange(0);
				getComparer().setAscend(m_rulevo.getVerifySeq().booleanValue());
				getComparer().setCompareKey(new String[] { m_rulevo.getDateName() });
				if (m_rulevo.getDateName() != null && m_rulevo.getMaxDateError() != null) {
					getComparer().setCompareKey(new String[] { m_rulevo.getDateName() });
					getComparer().setDaterange(m_rulevo.getMaxDateError().intValue());
				}
				sun.misc.Sort.quicksort(getDebitData(), getComparer());
				for (int i = 0; i < getDebitData().length; i++) {
					if (isRed(getDebitData()[i], true)) {
						alNegative.add(getDebitData()[i]);
					} else {
						alPositive.add(getDebitData()[i]);
					}
				}
				doMatch(alPositive, alNegative, true);
			}
			if (getCreditData() != null && getCreditData().length > 1) {
				for (int i = 0; i < getCreditData().length; i++) {
					getCreditData()[i].setCredit_Money_Y(GlNumberFormat.formatUFDouble(getCreditData()[i].getCredit_Money_Y(), Currency.getCurrDigit(getCreditData()[i].getCurrPk())));
//					getCreditData()[i].setCredit_Money_F(GlNumberFormat.formatUFDouble(getCreditData()[i].getCredit_Money_F(), ClientInfo.getInstance().getFracDigit()));
					getCreditData()[i].setCredit_Money_B(GlNumberFormat.formatUFDouble(getCreditData()[i].getCredit_Money_B(), Currency.getCurrDigit(Currency.getLocalCurrPK(getPk_accountingbook()))));
//					getCreditData()[i].setCredit_Money_J(GlNumberFormat.formatUFDouble(getCreditData()[i].getCredit_Money_J(), Currency.getCurrDigit(Currency.getGroupCurrpk(GlWorkBench.getLoginGroup()))));
//					getCreditData()[i].setCredit_Money_Q(GlNumberFormat.formatUFDouble(getCreditData()[i].getCredit_Money_Q(), Currency.getCurrDigit(Currency.getGlobalCurrPk(null))));
				}
				/* 按日期排序 */
				getComparer().setDaterange(0);
				getComparer().setAscend(m_rulevo.getVerifySeq().booleanValue());
				getComparer().setCompareKey(new String[] { m_rulevo.getDateName() });
				if (m_rulevo.getDateName() != null && m_rulevo.getMaxDateError() != null) {
					getComparer().setCompareKey(new String[] { m_rulevo.getDateName() });
					getComparer().setDaterange(m_rulevo.getMaxDateError().intValue());
				}
				sun.misc.Sort.quicksort(getCreditData(), getComparer());
				for (int i = 0; i < getCreditData().length; i++) {
					if (isRed(getCreditData()[i], false)) {
						alNegative.add(getCreditData()[i]);
					} else {
						alPositive.add(getCreditData()[i]);
					}
				}
				doMatch(alPositive, alNegative, false);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
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

	private boolean isEqual(IVerifyVO vo1, IVerifyVO vo2) {
		if (m_rulevo.getMaxDateError() != null) {
			getComparer().setDaterange(m_rulevo.getMaxDateError().intValue());
			getComparer().setCompareKey(new String[] { m_rulevo.getDateName() });
			return getComparer().doCompare(vo1, vo2) == 0 ? true : false;
		}
		return true;
	}

	public String getPk_accountingbook() {
		return pk_accountingbook;
	}

	public void setPk_accountingbook(String pkAccountingbook) {
		pk_accountingbook = pkAccountingbook;
	}
	
}
