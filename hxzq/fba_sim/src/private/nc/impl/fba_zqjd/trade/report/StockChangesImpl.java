package nc.impl.fba_zqjd.trade.report;

import java.util.*;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.itf.fba_zqjd.trade.report.IStockChanges;
import nc.jdbc.framework.processor.BeanListProcessor;
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
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.report.ReportQueryResult;

import com.ufida.dataset.IContext;

/**
 * 证券交易库存变动报表
 * 
 * @author mx
 * 
 */

public class StockChangesImpl implements IStockChanges {

	// 证券交易库存变动表
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
	// 结果集合
	List<NewStockChangeVO> lastResult = new ArrayList<NewStockChangeVO>();
	// 期初融入
	private ArrayList<ZqjdVO> bowResult = null;
	// 期初融入归还
	private ArrayList<ZqjdVO> bowResultRtn = null;
	// 期初卖出
	private ArrayList<ZqjdVO> sellResult = null;
	// 期初买入
	private ArrayList<ZqjdVO> buyResult = null;
	// 本期融入
	private ArrayList<ZqjdVO> nowBowResult = null;
	// 本期融入归还
	private ArrayList<ZqjdVO> nowRtnResult = null;
	// 本期卖出数量和金额
	private ArrayList<ZqjdVO> nowSellResult = null;
	// 本期买入数量和金额
	private ArrayList<ZqjdVO> nowBuyResult = null;
	// 本期负债金额
	private ArrayList<ZqjdVO> nowCostResult = null;
	// 期间收到兑付利息
	private ArrayList<ZqjdVO> nowRecResult = null;
	// 期间转付兑付利息
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
			// 获得查询参数
			SmartContext smct = new SmartContext(context);
			String beginDateStr = (String) smct.getParameterValue("beginDate");
			String endDateStr = (String) smct.getParameterValue("endDate");

			getBeginStockBow(beginDateStr);// 期初融入数量
			getBeginSellNum(beginDateStr);// 期初卖出数量
			for (NewStockChangeVO scvo : lastResult) {
				if (scvo.getStart_sell_num() != null) {
					scvo.setStart_debt(scvo.getStart_sell_num().multiply(100));// 期初负债金额
				}
			}
			getNowBowNum(beginDateStr, endDateStr);// 本期融入数量
			getNowRtnNum(beginDateStr, endDateStr);// 本期融入归还
			getNowSell(beginDateStr, endDateStr);// 本期卖出数量和卖出金额
			getNowBuy(beginDateStr, endDateStr);// 本期买入数量和买入金额
			getNowDebt(beginDateStr, endDateStr);// 本期买入负债成本-->期间买入的销售成本合计
			getNowInterest(beginDateStr, endDateStr);// 期间收到兑付利息和转付兑付利息

			lastResult = StockChangeReportTool.fullWithZero(lastResult);// 数值字段填充

			getEndResult();// 期末融入数量 卖出数量 负债金额

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		MetaData metaData = getMetaData();
		StockChangeReportTool.setColType(metaData);// 设置数值类型元数据字段
		DataSet dataSet = convertVOToArray(metaData, lastResult);
		return dataSet;

	}

	/**
	 * 期末库存计算
	 */
	private void getEndResult() {
		for (NewStockChangeVO scvo : lastResult) {
			// 期末融入数量 = 期初融入数量 + 本期融入数量 - 本期融入归还数量
			scvo.setEnd_bow_num(scvo.getStart_bow_num().add(scvo.getNow_bow())
					.sub(scvo.getNow_rtn()));
			// 期末卖出数量 = 期初卖出数量 + 本期卖出数量 - 本期买入数量
			scvo.setEnd_sell_num(scvo.getStart_sell_num()
					.add(scvo.getNow_sell_num()).sub(scvo.getNow_buy_num()));
			// 期末负债金额 = 期初负债金额 + 本期卖出金额 - 本期买入的负债成本
			scvo.setEnd_debt(scvo.getStart_debt().add(scvo.getNow_sell_sum())
					.sub(scvo.getNow_debt()));
		}
	}

	/**
	 * 查询债券借贷交易记录，计算期间收到兑付利息和转付兑付利息
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
	 * 查询证券交易表，计算本期买入负债成本（期间买入的销售成本合计）
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
	 * 查询证券交易表，计算本期买入数量和金额
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getNowBuy(String beginDateStr, String endDateStr) {
		try {
			String sql_buyNum_now = StockChangeReportTool.getNowBuySql(
					beginDateStr, endDateStr);
			nowBuyResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_buyNum_now, new BeanListProcessor(ZqjdVO.class));
			if (isNotNull(nowBuyResult)) {// 本期买入不为空
				for (NewStockChangeVO scvo : lastResult) {
					for (ZqjdVO zqjdTallyVO : nowBuyResult) {
						if (isEquals(zqjdTallyVO, scvo)) {
							scvo.setNow_buy_num(zqjdTallyVO.getBargain_num());// 本期买入数量
																				// bargain_num
							scvo.setNow_buy_sum(zqjdTallyVO.getBargain_sum());// 本期买入金额
																				// bargain_sum(买入单的实际收付字段)
						}
					}
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 查询证券交易表，计算本期卖出数量和卖出金额
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
			if (isNotNull(nowSellResult)) {// 本期卖出不为空
				for (NewStockChangeVO scvo : lastResult) {
					for (ZqjdVO zqjdTallyVO : nowSellResult) {
						if (isEquals(zqjdTallyVO, scvo)) {
							scvo.setNow_sell_num(zqjdTallyVO.getBargain_num());// 本期卖出数量
																				// bargain_num
							scvo.setNow_sell_sum(zqjdTallyVO.getBargain_sum());// 本期卖出金额
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
	 * 计算期初融入数量
	 * 
	 * @param beginDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getBeginStockBow(String beginDateStr) {
		try {
			String sql_bow = StockChangeReportTool.getStartSql(
					SystemConst.PK_BILLTYPECODE_HV7A_01, beginDateStr);

			bowResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(sql_bow,// 融入
					new BeanListProcessor(ZqjdVO.class));

			String sql_rtn = StockChangeReportTool.getStartSql(
					SystemConst.PK_BILLTYPECODE_HV7A_02, beginDateStr);
			bowResultRtn = (ArrayList<ZqjdVO>) baseDAO.executeQuery(sql_rtn,// 融入归还
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
					// 融入数量计算
					if (isNotNull(bowResultRtn)) {// 不为空 融入数量=融入-融入归还
						for (int j = 0; j < bowResultRtn.size(); j++) {
							if (isEquals(bowResultRtn.get(j), scvo)) {// 维度
								UFDouble sub = new UFDouble();
								sub = bowResult
										.get(i)
										.getBargain_num()
										.sub(bowResultRtn.get(j)
												.getBargain_num());
								scvo.setStart_bow_num(sub);
							}
						}
					} else {// 为空 融入数量==融入成交数量
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
	 * 计算期初卖出数量
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

			if (isNotNull(sellResult)) {// 非空
				if (isNotNull(buyResult)) {// 继续判断买入是否为空
					// 买入和卖出都不为空 卖出数量=卖出数量-买入数量 卖出list.size>=买入list.size
					for (int i = 0; i < lastResult.size(); i++) {
						for (int j = 0; j < sellResult.size(); j++) {
							for (int j2 = 0; j2 < buyResult.size(); j2++) {
								if (isEquals(buyResult.get(j2),
										sellResult.get(j))
										&& isEquals(sellResult.get(j),
												lastResult.get(i))) {// 维度
									UFDouble sub = new UFDouble();
									// 卖出-买入
									sub = sellResult
											.get(i)
											.getBargain_num()
											.sub(buyResult.get(j2)
													.getBargain_num());
									lastResult.get(i).setStart_sell_num(sub);// 设置期初卖出数量
								}
							}
						}
					}
				} else {// 买入为空 卖出数量==卖出数量
					for (int i = 0; i < lastResult.size(); i++) {
						for (int j = 0; j < sellResult.size(); j++) {
							if (isEquals(sellResult.get(j), lastResult.get(i))) {// 维度
								lastResult.get(i).setStart_sell_num(
										sellResult.get(j).getBargain_num());// 设置期初卖出数量
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
	 * 计算本期融入数量
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 */
	@SuppressWarnings("unchecked")
	private void getNowBowNum(String beginDateStr, String endDateStr) {
		try {
			String sql_bow_now = "select * from sim_zqjd a"
					+ " where a.state >= 2"
					+ " and nvl(a.dr,0) = 0"
					+ " and a.pk_assetsprop in ('0001SE00000000000004','0001SE00000000000005','0001SE00000000000006')"
					+ " and a.transtypecode = 'HV7A-0xx-01'"
					+ " and trade_date between '" + beginDateStr + "'"
					+ " and '" + endDateStr + "|| 23:59:59'";
			nowBowResult = (ArrayList<ZqjdVO>) baseDAO.executeQuery(
					sql_bow_now, new BeanListProcessor(ZqjdVO.class));
			List<NewStockChangeVO> tempList = new ArrayList<NewStockChangeVO>();

			for (ZqjdVO zqjdVO : nowBowResult) {// 转换为StockChangeVOList
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
			// 合并集合 期初融入 本期融入
			Set<NewStockChangeVO> set1 = new HashSet<NewStockChangeVO>();
			set1.addAll(lastResult);
			set1.addAll(tempList);
			lastResult.clear();
			lastResult.addAll(set1);
			if (isNotNull(nowBowResult)) {// 非空
				for (ZqjdVO zqvo : nowBowResult) {
					for (NewStockChangeVO scvo : lastResult) {
						if (isEquals(zqvo, scvo)) {// 是否包含当前结果集的证券
							scvo.setNow_bow(zqvo.getBargain_num());// 包含当前证券
																	// 设置本期融入数量
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 计算本期融入归还
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
			// 判断本期融入归还是否为空
			if (isNotNull(nowRtnResult)) {// 非空
				for (ZqjdVO zqvo : nowRtnResult) {
					for (NewStockChangeVO scvo : lastResult) {
						if (isEquals(zqvo, scvo)) {// 是否包含当前结果集的证券
							scvo.setNow_rtn(zqvo.getBargain_num());// 包含当前证券
																	// 设置本期融入归还数量

						}
					}
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 判断两个VO的维度字段是否相同 1.集团 pk_group 2.组织 pk_org 3.库存组织 pk_stocksort 4.资金账号
	 * pk_capaccount 5.证券主键 pk_securities ps:资产属性判断在sql语句中进行 此处不再判断
	 * 
	 * @return true 相同 false 不同
	 */
	private Boolean isEquals(ZqjdVO vo1, NewStockChangeVO vo2) {
		if (vo1.getPk_securities().equals(vo2.getPk_securities())
				&& vo1.getPk_org().equals(vo2.getPk_org())
				&& vo1.getPk_stocksort().equals(vo2.getPk_stocksort())
				&& vo1.getPk_capaccount().equals(vo2.getPk_capaccount())
				&& vo1.getPk_group().equals(vo2.getPk_group())) {
			return true;// 相同
		}
		return false;// 不同
	}

	private boolean isEquals(ZqjdVO vo1, ZqjdVO vo2) {
		if (vo1.getPk_securities().equals(vo2.getPk_securities())
				&& vo1.getPk_org().equals(vo2.getPk_org())
				&& vo1.getPk_stocksort().equals(vo2.getPk_stocksort())
				&& vo1.getPk_capaccount().equals(vo2.getPk_capaccount())
				&& vo1.getPk_group().equals(vo2.getPk_group())) {
			return true;// 相同
		}
		return false;// 不同
	}

	@SuppressWarnings("unused")
	private boolean isEquals(ZqjdTallyVO vo1, StockChangeVO vo2) {
		if (vo1.getPk_securities().equals(vo2.getPk_securities())
				&& vo1.getPk_org().equals(vo2.getPk_org())
				&& vo1.getPk_stocksort().equals(vo2.getPk_stocksort())
				&& vo1.getPk_capaccount().equals(vo2.getPk_capaccount())
				&& vo1.getPk_group().equals(vo2.getPk_group())) {
			return true;// 相同
		}
		return false;// 不同
	}

	/**
	 * 集合非空判断
	 * 
	 * @param arrayList
	 * @return false空 true非空
	 */
	private Boolean isNotNull(ArrayList<? extends SuperVO> arrayList) {

		if (arrayList == null || arrayList.size() == 0)
			return false;

		return true;
	}

	/**
	 * 根据VO字段 向元数据添加字段
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
	 * @return 填充数据集 返回结果集
	 */
	public DataSet convertVOToArray(MetaData metaData,
			List<NewStockChangeVO> vos) {
		Field[] mfields = metaData.getFields();
		Object[][] results = new Object[vos.size()][];
		Class<NewStockChangeVO> cls = NewStockChangeVO.class;
		java.lang.reflect.Field[] rfields = cls.getDeclaredFields();
		try {

			for (int i = 0; i < vos.size(); i++) {
				Object[] row = new Object[mfields.length];
				for (int j = 0; j < mfields.length; j++) {
					for (java.lang.reflect.Field rfield : rfields) {
						rfield.setAccessible(true);
						if (mfields[j].getFldname().equals(rfield.getName())) {
							if (rfield.getType().getName()
									.equals(UFDateTime.class.getName())) {// 日期类型
								row[j] = (UFDateTime) rfield.get(vos.get(i));
							} else if (rfield.getType().getName()
									.equals(UFDouble.class.getName())) {// UFDouble
								// UFDouble ud = new UFDouble();
								UFDouble temp = (UFDouble) rfield.get(vos
										.get(i));
								row[j] = temp;
							} else {// 字符串
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
