package nc.impl.fba_zqjd.trade.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_scost.cost.interest.pub.CalcBuySellInterest;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_zqjd.trade.report.IStockChanges;
import nc.itf.fba_zqjd.trade.report.Izqjd_stockchange;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.smart.context.SmartContext;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.tool.fba_zqjd.pub.SystemConst;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.fba_zqjd.trade.report.NewStockChangeVO;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * ֤ȯ���׿��䶯����
 * 
 * @author ZQ
 * 
 */

public class StockChangesImpl implements IStockChanges {

	// ֤ȯ���׿��䶯��
	public static final String[] STOCKCHANGE_S_FIELD = { "pk_group", "pk_org",
			"pk_assetsprop", "pk_stocksort", "pk_capaccount", "pk_securities",
			"start_bow_num", "start_sell_num", "QCactual_num", "QCbargain_num",
			"start_debt", "QCfzsz", "QCinterest", "now_bow", "now_rtn", "BQJT",
			"now_sell_num", "now_sell_sum", "BQinterest", "now_buy_num",
			"now_buy_sum", "now_debt", "BQJZ", "rec_interest", "turn_interest",
			"end_bow_num", "end_sell_num", "QMactual_num", "QMbargain_num",
			"end_debt", "QMfzsz", "QMinterest", "LJSY", "begin_date",
			"end_date" };

	BaseDAO baseDAO = new BaseDAO();
	// �������ss
	List<NewStockChangeVO> lastResult = new ArrayList<NewStockChangeVO>();
	// �ڳ�����
	private ArrayList<ZqjdVO> bowResult = null;
	// �ڳ�����黹
	private ArrayList<ZqjdVO> bowResultRtn = null;
	// �ڳ�����
	private ArrayList<ZqjdVO> sellResult = null;
	// �ڳ�����
	private ArrayList<ZqjdVO> buyResult = null;
	// ��������
	private ArrayList<ZqjdVO> nowBowResult = null;
	// ��������黹
	private ArrayList<ZqjdVO> nowRtnResult = null;
	// �������������ͽ��
	private ArrayList<ZqjdVO> nowSellResult = null;
	// �������������ͽ��
	private ArrayList<ZqjdVO> nowBuyResult = null;
	// ���ڸ�ծ���
	private ArrayList<ZqjdVO> nowCostResult = null;
	// �ڼ��յ��Ҹ���Ϣ
	private ArrayList<ZqjdVO> nowRecResult = null;
	// �ڼ�ת���Ҹ���Ϣ
	private ArrayList<ZqjdVO> nowTurnResult = null;

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {

		ReportQueryResult queryResult = new ReportQueryResult();
		queryResult.setDataSet(genDataSet(context));
		return queryResult;

	}

	private DataSet genDataSet(IContext context) {
		try {
			// ��ò�ѯ����
			SmartContext smct = new SmartContext(context);
			String beginDateStr = (String) smct.getParameterValue("beginDate");
			String endDateStr = (String) smct.getParameterValue("endDate");
			getBeginStockBow(beginDateStr);// �ڳ���������
			getBeginSellNum(beginDateStr);// �ڳ���������
//			for (NewStockChangeVO scvo : lastResult) {
//				// ��׼�Ĳ���
//				if (scvo.getStart_sell_num() != null) {
//					scvo.setStart_debt(scvo.getStart_sell_num().multiply(100));// �ڳ���ծ���
//				}
//
//			}
			getNowBowNum(beginDateStr, endDateStr);// ������������
			getNowRtnNum(beginDateStr, endDateStr);// ��������黹
			getNowSell(beginDateStr, endDateStr);// ���������������������
			getNowBuy(beginDateStr, endDateStr);// ��������������������
			getNowDebt(beginDateStr, endDateStr);// �������븺ծ�ɱ�-->�ڼ���������۳ɱ��ϼ�
			getNowInterest(beginDateStr, endDateStr);// �ڼ��յ��Ҹ���Ϣ��ת���Ҹ���Ϣ

			lastResult = StockChangeReportTool.fullWithZero(lastResult);// ��ֵ�ֶ����

			getEndResult();// ��ĩ�������� �������� ��ծ���

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		MetaData metaData = getMetaData();
		StockChangeReportTool.setColType(metaData);// ������ֵ����Ԫ�����ֶ�
		DataSet dataSet = newconvertVOToArray(metaData, lastResult);
		return dataSet;

	}

	/*
	 * ��ȡ�˲� -zq
	 */

	@SuppressWarnings("rawtypes")
	public String getPK_glorgbook(NewStockChangeVO scvo) {
		String Pk_glorgbook = "";
		String pkorg = scvo.getPk_org();
		String pkgro = scvo.getPk_group();
		Vector vec = new Vector();
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_glorgbook from sim_stockbalance where nvl(dr,0)=0 and pk_org ='"
				+ pkorg
				+ "' and pk_group ='"
				+ pkgro
				+ "' and rownum<=1 order by ts desc";
		try {
			vec = (Vector) dao.executeQuery(sql, new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		Pk_glorgbook = ((Vector) vec.get(0)).get(0).toString();
		return Pk_glorgbook;
	}

	/**
	 * ��ĩ������
	 */
	private void getEndResult() {
		for (NewStockChangeVO scvo : lastResult) {
			// ��ĩ�������� = �ڳ��������� + ������������ - ��������黹����
			scvo.setEnd_bow_num(scvo.getStart_bow_num().add(scvo.getNow_bow())
					.sub(scvo.getNow_rtn()));
			// ��ĩ�������� = �ڳ��������� + ������������ - ������������
			scvo.setEnd_sell_num(scvo.getStart_sell_num()
					.add(scvo.getNow_sell_num()).sub(scvo.getNow_buy_num()));
			// ��ĩ��ծ��� = �ڳ���ծ��� + ����������� - ��������ĸ�ծ�ɱ�
			scvo.setEnd_debt(scvo.getStart_debt().add(scvo.getNow_sell_sum())
					.sub(scvo.getNow_debt()));
		}
	}

	/**
	 * ��ѯծȯ������׼�¼�������ڼ��յ��Ҹ���Ϣ��ת���Ҹ���Ϣ
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getNowInterest(String beginDateStr, String endDateStr) {
		try {
			String sql_rec_now = StockChangeReportTool.getInterestSql(
					SystemConst.PK_BILLTYPECODE_HV7A_05, beginDateStr,
					endDateStr);
			nowRecResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_rec_now, new BeanListProcessor(ZqjdVO.class));
			if (isNotNull(nowRecResult)) {
				for (NewStockChangeVO scvo : lastResult) {
					for (ZqjdVO zqjdVO : nowRecResult) {
						if (isEquals(zqjdVO, scvo)) {
							scvo.setRec_interest(zqjdVO.getInterest_sum());
						}
					}
				}
			}
			String sql_turn_now = StockChangeReportTool.getInterestSql(
					SystemConst.PK_BILLTYPECODE_HV7A_06, beginDateStr,
					endDateStr);
			nowTurnResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_turn_now, new BeanListProcessor(ZqjdVO.class));
			if (isNotNull(nowTurnResult)) {
				for (NewStockChangeVO scvo : lastResult) {
					for (ZqjdVO zqjdVO : nowTurnResult) {
						if (isEquals(zqjdVO, scvo)) {
							scvo.setTurn_interest(zqjdVO.getInterest_sum());
						}
					}
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ��ѯ֤ȯ���ױ����㱾�����븺ծ�ɱ����ڼ���������۳ɱ��ϼƣ�
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getNowDebt(String beginDateStr, String endDateStr) {
		try {
			String sql_cost_now = StockChangeReportTool.getNowDebtSql(
					SystemConst.PK_BILLTYPECODE_HV7A_04, beginDateStr,
					endDateStr);
			nowCostResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_cost_now, new BeanListProcessor(ZqjdVO.class));
			if (isNotNull(nowCostResult)) {
				for (NewStockChangeVO scvo : lastResult) {
					for (ZqjdVO zqjdVO : nowCostResult) {
						if (isEquals(zqjdVO, scvo)) {
							scvo.setNow_debt(zqjdVO.getSellcost());
						}
					}
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ��ѯ֤ȯ���ױ����㱾�����������ͽ��
	 * 
	 * @param beginDateStr
	 * @param endDateStrS
	 */
	@SuppressWarnings("unchecked")
	private void getNowBuy(String beginDateStr, String endDateStr) {
		try {
			String sql_buyNum_now = StockChangeReportTool.getNowBuySql(
					beginDateStr, endDateStr);
			nowBuyResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_buyNum_now, new BeanListProcessor(ZqjdVO.class));
			if (isNotNull(nowBuyResult)) {// �������벻Ϊ��
				for (NewStockChangeVO scvo : lastResult) {
					for (ZqjdVO zqjdTallyVO : nowBuyResult) {
						if (isEquals(zqjdTallyVO, scvo)) {
							scvo.setNow_buy_num(zqjdTallyVO.getBargain_num());// ������������
																				// bargain_num
							scvo.setNow_buy_sum(zqjdTallyVO.getBargain_sum());// ����������
																				// bargain_sum(���뵥��ʵ���ո��ֶ�)
						}
					}
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ��ѯ֤ȯ���ױ����㱾�������������������
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getNowSell(String beginDateStr, String endDateStr) {
		try {
			String sql_sellNum_now = StockChangeReportTool.getNowSellSql(
					beginDateStr, endDateStr);
			nowSellResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_sellNum_now, new BeanListProcessor(ZqjdVO.class));
			if (isNotNull(nowSellResult)) {// ����������Ϊ��
				for (NewStockChangeVO scvo : lastResult) {
					for (ZqjdVO zqjdTallyVO : nowSellResult) {
						if (isEquals(zqjdTallyVO, scvo)) {
							scvo.setNow_sell_num(zqjdTallyVO.getBargain_num());// ������������
																				// bargain_num
							scvo.setNow_sell_sum(zqjdTallyVO.getBargain_sum());// �����������
																				// bargain_sum
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * �����ڳ���������
	 * 
	 * @param beginDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getBeginStockBow(String beginDateStr) {
		try {
			String sql_bow = StockChangeReportTool.getStartSql(
					SystemConst.PK_BILLTYPECODE_HV7A_01, beginDateStr);

			bowResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(sql_bow,// ����
					new BeanListProcessor(ZqjdVO.class));

			String sql_rtn = StockChangeReportTool.getStartSql(
					SystemConst.PK_BILLTYPECODE_HV7A_02, beginDateStr);
			bowResultRtn = (ArrayList<ZqjdVO>) baseDAO.executeQuery(sql_rtn,// ����黹
					new BeanListProcessor(ZqjdVO.class));
			if (isNotNull(bowResult)) {
				for (int i = 0; i < bowResult.size(); i++) {
					NewStockChangeVO scvo = new NewStockChangeVO();
					scvo.setPk_group(bowResult.get(i).getPk_group());
					scvo.setPk_org(bowResult.get(i).getPk_org());
					scvo.setPk_stocksort(bowResult.get(i).getPk_stocksort());
					scvo.setPk_assetsprop(bowResult.get(i).getPk_assetsprop());
					scvo.setPk_capaccount(bowResult.get(i).getPk_capaccount());
					scvo.setPk_securities(bowResult.get(i).getPk_securities());
					// ������������
					if (isNotNull(bowResultRtn)) {// ��Ϊ�� ��������=����-����黹
						for (int j = 0; j < bowResultRtn.size(); j++) {
							if (isEquals(bowResultRtn.get(j), scvo)) {// ά��
								UFDouble sub = new UFDouble();
								sub = bowResult
										.get(i)
										.getBargain_num()
										.sub(bowResultRtn.get(j)
												.getBargain_num());
								scvo.setStart_bow_num(sub);
							}
						}
					} else {// Ϊ�� ��������==����ɽ�����
						scvo.setStart_bow_num(bowResult.get(i).getBargain_num());
					}
					lastResult.add(scvo);
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * �����ڳ���������
	 * 
	 * @param beginDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getBeginSellNum(String beginDateStr) {
		try {
			String sql_sell = StockChangeReportTool.getStartSql(
					SystemConst.PK_BILLTYPECODE_HV7A_03, beginDateStr);
			sellResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(sql_sell,
					new BeanListProcessor(ZqjdVO.class));

			String sql_buy = StockChangeReportTool.getStartSql(
					SystemConst.PK_BILLTYPECODE_HV7A_04, beginDateStr);
			buyResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(sql_buy,
					new BeanListProcessor(ZqjdVO.class));

			if (isNotNull(sellResult)) {// �ǿ�
				if (isNotNull(buyResult)) {// �����ж������Ƿ�Ϊ��
					// �������������Ϊ�� ��������=��������-�������� ����list.size>=����list.size
					for (int i = 0; i < lastResult.size(); i++) {
						for (int j = 0; j < sellResult.size(); j++) {
							for (int j2 = 0; j2 < buyResult.size(); j2++) {
								if (isEquals(buyResult.get(j2),
										sellResult.get(j))
										&& isEquals(sellResult.get(j),
												lastResult.get(i))) {// ά��
									UFDouble sub = new UFDouble();
									// ����-����
									sub = sellResult
											.get(i)
											.getBargain_num()
											.sub(buyResult.get(j2)
													.getBargain_num());
									lastResult.get(i).setStart_sell_num(sub);// �����ڳ���������
								}
							}
						}
					}
				} else {// ����Ϊ�� ��������==��������
					for (int i = 0; i < lastResult.size(); i++) {
						for (int j = 0; j < sellResult.size(); j++) {
							if (isEquals(sellResult.get(j), lastResult.get(i))) {// ά��
								lastResult.get(i).setStart_sell_num(
										sellResult.get(j).getBargain_num());// �����ڳ���������
							}
						}

					}
				}

			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

	}

	/**
	 * ���㱾����������
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getNowBowNum(String beginDateStr, String endDateStr) {
		Izqjd_stockchange izqsc = NCLocator.getInstance().lookup(
				Izqjd_stockchange.class);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("select max(a.pk_group) pk_group,");
			sb.append("max(a.pk_org) pk_org,");
			sb.append("max(a.pk_assetsprop) pk_assetsprop,");
			sb.append("max(a.pk_stocksort) pk_stocksort,");
			sb.append("max(a.pk_securities) pk_securities,");
			sb.append("max(a.pk_capaccount) pk_capaccount,");
			sb.append("a.pk_securities,");
			sb.append("sum(a.bargain_num) bargain_num ");
			sb.append("from sim_zqjd a ");
			sb.append("where a.state >= 2 ");
			sb.append("and isnull(a.dr,0) = 0 " );
			sb.append("and a.pk_assetsprop in ('0001SE00000000000004','0001SE00000000000005','0001SE00000000000006') ");
			sb.append("and a.transtypecode = 'HV7A-0xx-01' ");
			sb.append("and trade_date between \'" + beginDateStr + "\' and \'" + endDateStr + "|| 23:59:59\'");
			sb.append("group by a.pk_securities");
			nowBowResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sb.toString(), new BeanListProcessor(ZqjdVO.class));
			Collection<NewStockChangeVO> tempList = new ArrayList<NewStockChangeVO>();

			for (ZqjdVO zqjdVO : nowBowResult) {// ת��ΪStockChangeVOList
				NewStockChangeVO scvo = new NewStockChangeVO();
				scvo.setPk_group(zqjdVO.getPk_group());
				scvo.setPk_org(zqjdVO.getPk_org());
				scvo.setPk_stocksort(zqjdVO.getPk_stocksort());
				scvo.setPk_assetsprop(zqjdVO.getPk_assetsprop());
				scvo.setPk_capaccount(zqjdVO.getPk_capaccount());
				scvo.setPk_securities(zqjdVO.getPk_securities());
				scvo.setNow_bow(zqjdVO.getBargain_num());
				tempList.add(scvo);
			}
			// �ϲ����� �ڳ����� ��������
			Set<NewStockChangeVO> set1 = new HashSet<NewStockChangeVO>();
			// set1.addAll(lastResult);
			set1.addAll(tempList);
			lastResult.clear();
			lastResult.addAll(set1);
			if (isNotNull(nowBowResult)) {// �ǿ�
				for (ZqjdVO zqvo : nowBowResult) {
					for (NewStockChangeVO scvo : lastResult) {
						if (isEquals(zqvo, scvo)) {// �Ƿ������ǰ�������֤ȯ
							scvo.setNow_bow(zqvo.getBargain_num());// ������ǰ֤ȯ
																	// ���ñ�����������
							// �ڳ���ծ��������ծ�����ؼ���
							UFDouble QCMRMC = new UFDouble(izqsc
									.SearchQCZQJY(scvo, beginDateStr).get("MRMC")
									.toString()); // �����ծȯ��������������
							UFDouble QCZRZC = new UFDouble(izqsc
									.SearchQCZQZH(scvo, beginDateStr).get("ZRZC")
									.toString()); // �����ծȯ��ת��ת������
							UFDouble QCZRZCTG = new UFDouble(izqsc
									.SearchQCZQZHTG(scvo, endDateStr).get("ZRZC")
									.toString()); // �����ծȯ�йܵ�ת��ת������
							UFDouble QCMRMCJE = new UFDouble(izqsc
									.SearchQCZQJY(scvo, beginDateStr).get("MRMCJE")
									.toString()); // �����ծȯ������������
							UFDouble QCZRZCJE = new UFDouble(izqsc
									.SearchQCZQZH(scvo, beginDateStr).get("ZRZCJE")
									.toString()); // �����ծȯ��ת��ת����
							UFDouble QCZRZCTGJE = new UFDouble(izqsc
									.SearchQCZQZHTG(scvo, endDateStr).get("ZRZCJE")
									.toString()); // �����ծȯ�йܵ�ת��ת������
							// �����ڳ�ʵ������
							scvo.setQCactual_num(izqsc.SearchQCActual_num(scvo,
									beginDateStr));

							// �����������ڳ���ծ����
							scvo.setQCbargain_num(QCMRMC.add(QCZRZC).add(QCZRZCTG));

							// �����������ڳ���ծ���
							scvo.setStart_debt(QCMRMCJE.add(QCZRZCJE).add(QCZRZCTGJE));
							// ���½��������ڳ���ծ��ֵ��
							UFDouble qcclose_price = izqsc.SearchQCclose_price(scvo,
									beginDateStr);
							scvo.setQCfzsz(QCMRMC.add(QCZRZC).multiply(qcclose_price));

							// ��ĩ��ծ��������ծ�����ؼ���
							UFDouble QMMRMC = new UFDouble(izqsc
									.SearchQMZQJY(scvo, endDateStr).get("MRMC").toString()); // �����ծȯ��������������
							UFDouble QMZRZC = new UFDouble(izqsc
									.SearchQMZQZH(scvo, endDateStr).get("ZRZC").toString()); // �����ծȯ��ת��ת������
							UFDouble QMZRZCTG = new UFDouble(izqsc
									.SearchQMZQZHTG(scvo, endDateStr).get("ZRZC")
									.toString()); // �����ծȯ�йܵ�ת��ת������
							UFDouble QMMRMCJE = new UFDouble(izqsc
									.SearchQMZQJY(scvo, endDateStr).get("MRMCJE")
									.toString()); // �����ծȯ������������
							UFDouble QMZRZCJE = new UFDouble(izqsc
									.SearchQMZQZH(scvo, endDateStr).get("ZRZCJE")
									.toString()); // �����ծȯ��ת��ת����
							UFDouble QMZRZCTGJE = new UFDouble(izqsc
									.SearchQMZQZHTG(scvo, endDateStr).get("ZRZCJE")
									.toString()); // �����ծȯ�йܵ�ת��ת����
							// ������������ĩ��ծ����
							scvo.setQMbargain_num(QMMRMC.add(QMZRZC).add(QMZRZCTG));

							// ������������ĩ��ծ���
							scvo.setEnd_debt(QMMRMCJE.add(QMZRZCJE).add(QMZRZCTGJE));
							// ���½��������ڳ���ծ��ֵ��
							UFDouble qmclose_price = izqsc.SearchQMclose_price(scvo,
									endDateStr);
							scvo.setQMfzsz(QMMRMC.add(QMZRZC).multiply(qmclose_price));

							// ������ĩʵ������
							scvo.setQMactual_num(izqsc.SearchQMActual_num(scvo, endDateStr));

							// ���ñ��ڽ�תӦ����Ϣ
							scvo.setBQJT(izqsc.SearchBQJT(scvo, beginDateStr, endDateStr));

							// ���ñ�������Ӧ����Ϣ
							scvo.setBQinterest(izqsc.SearchBQinterest(scvo, beginDateStr,
									endDateStr));

							// ���ñ��������תӦ����Ϣ
							scvo.setBQJZ(izqsc.SearchBQJZ(scvo, beginDateStr, endDateStr));

							// �O����ĩӦ����Ϣ --��������Ϣ����*��ծ����
							CalcBuySellInterest buysell = new CalcBuySellInterest();
							UFDouble qclx = buysell
									.calcInterestTradedate(QMMRMC.add(QMZRZC), scvo
											.getPk_securities(),
											new UFDate(beginDateStr.substring(0, 10)
													+ " 26:59:59"), getPK_glorgbook(scvo));
							UFDouble qmlx = buysell.calcInterestTradedate(
									QMMRMC.add(QMZRZC), scvo.getPk_securities(),
									new UFDate(endDateStr.substring(0, 10) + " 26:59:59"),
									getPK_glorgbook(scvo));
							scvo.setQMinterest(qmlx);
							int tt = qclx.compareTo(new UFDouble(0));
							if (tt <= 0) {
								scvo.setQCinterest(new UFDouble(0));
							} else
								scvo.setQCinterest(qclx);

//							// ��׼�Ĳ���
//							if (scvo.getStart_sell_num() != null) {
//								scvo.setStart_debt(scvo.getStart_sell_num().multiply(100));// �ڳ���ծ���
//							}

							// �ۼ�������ʱ���ܣ�һ������Ϊ��
							scvo.setLJSY(new UFDouble(0));
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ���㱾������黹
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getNowRtnNum(String beginDateStr, String endDateStr) {
		try {
			String sql_rtn_now = StockChangeReportTool.getNowSql(
					SystemConst.PK_BILLTYPECODE_HV7A_02, beginDateStr,
					endDateStr);

			nowRtnResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_rtn_now, new BeanListProcessor(ZqjdVO.class));
			// �жϱ�������黹�Ƿ�Ϊ��
			if (isNotNull(nowRtnResult)) {// �ǿ�
				for (ZqjdVO zqvo : nowRtnResult) {
					for (NewStockChangeVO scvo : lastResult) {
						if (isEquals(zqvo, scvo)) {// �Ƿ������ǰ�������֤ȯ
							scvo.setNow_rtn(zqvo.getBargain_num());// ������ǰ֤ȯ
																	// ���ñ�������黹����

						}
					}
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * �ж�����VO��ά���ֶ��Ƿ���ͬ 1.���� pk_group 2.��֯ pk_org 3.�����֯ pk_stocksort 4.�ʽ��˺�
	 * pk_capaccount 5.֤ȯ���� pk_securities ps:�ʲ������ж���sql����н��� �˴������ж�
	 * 
	 * @return true ��ͬ false ��ͬ
	 */
	private Boolean isEquals(ZqjdVO vo1, NewStockChangeVO vo2) {
		if (vo1.getPk_securities().equals(vo2.getPk_securities())
				&& vo1.getPk_org().equals(vo2.getPk_org())
				&& vo1.getPk_stocksort().equals(vo2.getPk_stocksort())
				&& vo1.getPk_capaccount().equals(vo2.getPk_capaccount())
				&& vo1.getPk_group().equals(vo2.getPk_group())) {
			return true;// ��ͬ
		}
		return false;// ��ͬ
	}

	private boolean isEquals(ZqjdVO vo1, ZqjdVO vo2) {
		if (vo1.getPk_securities().equals(vo2.getPk_securities())
				&& vo1.getPk_org().equals(vo2.getPk_org())
				&& vo1.getPk_stocksort().equals(vo2.getPk_stocksort())
				&& vo1.getPk_capaccount().equals(vo2.getPk_capaccount())
				&& vo1.getPk_group().equals(vo2.getPk_group())) {
			return true;// ��ͬ
		}
		return false;// ��ͬ
	}

	@SuppressWarnings("unused")
	private boolean isEquals(ZqjdTallyVO vo1, StockChangeVO vo2) {
		if (vo1.getPk_securities().equals(vo2.getPk_securities())
				&& vo1.getPk_org().equals(vo2.getPk_org())
				&& vo1.getPk_stocksort().equals(vo2.getPk_stocksort())
				&& vo1.getPk_capaccount().equals(vo2.getPk_capaccount())
				&& vo1.getPk_group().equals(vo2.getPk_group())) {
			return true;// ��ͬ
		}
		return false;// ��ͬ
	}

	/**
	 * ���Ϸǿ��ж�
	 * 
	 * @param arrayList
	 * @return false�� true�ǿ�
	 */
	private Boolean isNotNull(ArrayList<? extends SuperVO> arrayList) {

		if (arrayList == null || arrayList.size() == 0)
			return false;

		return true;
	}

	/**
	 * ����VO�ֶ� ��Ԫ��������ֶ�
	 * 
	 * @return
	 */
	public MetaData getMetaData() {
		List<Field> list = new ArrayList<Field>();

		for (String key : STOCKCHANGE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}
		return new MetaData(list.toArray(new Field[0]));
	}

	/**
	 * @param metaData
	 * @param vos
	 * @return ������ݼ� ���ؽ����
	 */
	public DataSet convertVOToArray(MetaData metaData, List<StockChangeVO> vos) {
		Field[] mfields = metaData.getFields();
		Object[][] results = new Object[vos.size()][];
		Class<StockChangeVO> cls = StockChangeVO.class;
		java.lang.reflect.Field[] rfields = cls.getDeclaredFields();
		try {

			for (int i = 0; i < vos.size(); i++) {
				Object[] row = new Object[mfields.length];
				for (int j = 0; j < mfields.length; j++) {
					for (java.lang.reflect.Field rfield : rfields) {
						rfield.setAccessible(true);
						if (mfields[j].getFldname().equals(rfield.getName())) {
							if (rfield.getType().getName()
									.equals(UFDateTime.class.getName())) {// ��������
								row[j] = (UFDateTime) rfield.get(vos.get(i));
							} else if (rfield.getType().getName()
									.equals(UFDouble.class.getName())) {// UFDouble
								// UFDouble ud = new UFDouble();
								UFDouble temp = (UFDouble) rfield.get(vos
										.get(i));
								row[j] = temp;
							} else {// �ַ���
								row[j] = rfield.get(vos.get(i));
							}
						}
					}

					// row[j] =
					// vos.get(i).getAttributeValue(fields[j].getFldname());
				}
				results[i] = row;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataSet dataSet = new DataSet();
		dataSet.setMetaData(metaData);
		dataSet.setDatas(results);

		return dataSet;
	}

	public DataSet newconvertVOToArray(MetaData metaData,
			List<NewStockChangeVO> vos) {
		Field[] mfields = metaData.getFields();
		Object[][] results = new Object[vos.size()][];
		Class<NewStockChangeVO> ncls = NewStockChangeVO.class;
		java.lang.reflect.Field[] rfields = ncls.getDeclaredFields();

		try {

			for (int i = 0; i < vos.size(); i++) {
				Object[] row = new Object[mfields.length];
				for (int j = 0; j < mfields.length; j++) {
					for (java.lang.reflect.Field rfield : rfields) {
						rfield.setAccessible(true);
						if (mfields[j].getFldname().equals(rfield.getName())) {
							if (rfield.getType().getName()
									.equals(UFDateTime.class.getName())) {// ��������
								row[j] = (UFDateTime) rfield.get(vos.get(i));
							} else if (rfield.getType().getName()
									.equals(UFDouble.class.getName())) {// UFDouble
								// UFDouble ud = new UFDouble();
								UFDouble temp = (UFDouble) rfield.get(vos
										.get(i));
								row[j] = temp;
							} else {// �ַ���
								row[j] = rfield.get(vos.get(i));
							}
						}
					}

					// row[j] =
					// vos.get(i).getAttributeValue(fields[j].getFldname());
				}
				results[i] = row;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataSet dataSet = new DataSet();
		dataSet.setMetaData(metaData);
		dataSet.setDatas(results);

		return dataSet;
	}
}
