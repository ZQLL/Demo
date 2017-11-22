package nc.bs.fba_scost.cost.simtools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.fba_scost.cost.pendingbill.rule.SpecialDealPendBill;
import nc.bs.fba_scost.cost.pub.QueryBasePubInfo;
import nc.impl.fba_scost.cost.pub.PrivateMethod;
import nc.itf.fba_scost.cost.billvo.IVTradedPubVO;
import nc.itf.fba_scost.cost.tool.ICostBalanceTool;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.fundbalance.FundBalanceVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.fba_scost.cost.pub.ForwardVO;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_scost.cost.pub.SystemConst;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.totalwin.TotalwinVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_scost.scost.costpara.CostParaVO;
import nc.vo.fba_scost.trade.costadjustment.CostAdjustmentVO;
import nc.vo.fba_sec.secbd.stocksort.StocksortVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.VOUtil;

/**
 * �ɱ����㹤����
 * 
 * @author liangwei
 * 
 */
public class CostingTool implements ICostingTool {

	private CostParaVO costParaVO;

	private ICostBalanceTool balancetool;

	private List<TotalwinVO> totalwinvos = new ArrayList<TotalwinVO>();
	// ���ܲ����ɱ��䶯�Ŀ��
	private Map<String, StockBalanceVO> costchangevomap = new HashMap<String, StockBalanceVO>();

	// Ӧ����Ϣ��ת
	private Map<String, ForwardVO> interestjz = new HashMap<String, ForwardVO>();
	// ���ʼ�ֵ��ת
	private Map<String, ForwardVO> fairvaluejz = new HashMap<String, ForwardVO>();

	@Override
	public Map<String, ForwardVO> getFairvaluejz() {
		return fairvaluejz;
	}

	public void setFairvaluejz(Map<String, ForwardVO> fairvaluejz) {
		this.fairvaluejz = fairvaluejz;
	}

	private boolean isCheckFund;
	private Integer direction;

	@Override
	public Integer getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	private boolean istally;
	private boolean isCheckStock;
	private BilltypeGroupVO currbilltypegroupvo;

	private String currbilltype;
	private String currdate;
	private String[] pk_stocksort;
	private boolean isinit = true;

	PubMethod pm = PubMethod.getInstance();

	@Override
	public String[] getPk_stocksort() {
		return pk_stocksort;
	}

	@Override
	public void setPk_stocksort(String[] pk_stocksort) {
		this.pk_stocksort = pk_stocksort;
	}

	@Override
	public String getCurrbilltype() {
		return currbilltype;
	}

	@Override
	public void setCurrbilltype(String currbilltype) {
		this.currbilltype = currbilltype;
	}

	@Override
	public String getCurrdate() {
		return currdate;
	}

	@Override
	public void setCurrdate(String currdate) {
		this.currdate = currdate;
	}

	@Override
	public BilltypeGroupVO getCurrbilltypegroupvo() {
		return currbilltypegroupvo;
	}

	@Override
	public void setCurrbilltypegroupvo(BilltypeGroupVO currbilltypegroupvo) {
		this.currbilltypegroupvo = currbilltypegroupvo;
	}

	/**
	 * �������Ͳ���� ����Ϊ����list ֵ����Ϊ ���ݱ���+"##"+���� YangJie 2014-03-25
	 */
	private List<PendingBillVO> billtypeclass;

	@Override
	public List<PendingBillVO> getBilltypeclass() {
		return billtypeclass;
	}

	public void setBilltypeclass(List<PendingBillVO> billtypeclass) {
		this.billtypeclass = billtypeclass;
	}

	@Override
	public void setCostParaVO(CostParaVO costParaVO) {
		this.costParaVO = costParaVO;
		this.balancetool = new CostBalanceTool(costParaVO);
	}

	@Override
	public CostParaVO getCostParaVO() {
		return costParaVO;

	}

	@Override
	public ICostBalanceTool getBalancetool() {
		if (balancetool == null) {
			balancetool = new CostBalanceTool(costParaVO);
		}
		return balancetool;
	}

	/**
	 * �����ʽ� direction=0 �������룩 =1 ����֧����
	 * 
	 * @param tradevo
	 * @param direction
	 * @throws Exception
	 * @author libin
	 * @date 2012-10-16 ����8:06:33
	 */
	@Override
	public void updateFunds(SuperVO tradevo) throws Exception {
		if (!isCheckFund
				|| getDirection() == null// ������ʽ�
				|| getDirection().intValue() == SystemConst.funddirection[2]// �ʽ�û�б䶯������ʽ�
				|| !costParaVO.getCostplanvo().getIsbusinessplan()
						.booleanValue()) {// ���Ǻ��㷽���������ʽ�
			return;
		}

		UFDouble fact_sum = (UFDouble) tradevo.getAttributeValue("fact_sum");
		// String pk_billtype = (String)
		// tradevo.getAttributeValue("transtypecode");
		if (fact_sum == null || fact_sum.compareTo(new UFDouble(0)) == 0) {// ʵ���ո�Ϊ0�������
			return;
		}
		String key = VOUtil.getCombinesKey(tradevo, costParaVO.getCostplanvo()
				.getFundsFieldArray());
		key = getCurrbilltypegroupvo().getPk_billtypegroup() + key;
		FundBalanceVO fundbalancevo = getBalancetool().getFundbalanceVO(key,
				this);
		if (fundbalancevo == null) {// û���ڳ�
			fundbalancevo = getBalancetool()
					.getFundbalanceVOByVO(tradevo, this);
		}
		if (direction == SystemConst.funddirection[0]) {// ����
			fact_sum = fundbalancevo.getStocks_sum().add(fact_sum);
		} else if (direction == SystemConst.funddirection[1]) {// ֧��
			fact_sum = fundbalancevo.getStocks_sum().sub(fact_sum);
		}
		fact_sum = pm.setScale(fact_sum, true, true);
		// String pk_capaccount = (String)
		// tradevo.getAttributeValue("pk_capaccount");
		if (fact_sum.compareTo(new UFDouble(0)) < 0) {
			handleException(tradevo, null, SystemConst.error[3]);
		}
		fundbalancevo.setStocks_sum(fact_sum);
		fundbalancevo.setPk_billtypegroup(this.getCurrbilltypegroupvo()
				.getPk_billtypegroup());
		fundbalancevo.setPk_group((String) tradevo
				.getAttributeValue("pk_group"));
		fundbalancevo.setPk_org((String) tradevo.getAttributeValue("pk_org"));
		fundbalancevo.setPk_org_v((String) tradevo
				.getAttributeValue("pk_org_v"));
		fundbalancevo.setPk_glorgbook((String) tradevo
				.getAttributeValue("pk_glorgbook"));
		BanlanceQueryKeyVO queryvo = new BanlanceQueryKeyVO();
		queryvo.setKey(key);
		queryvo.setTrade_date(getCurrdate());
		getBalancetool().updateFundbalanceVO(queryvo, fundbalancevo);

	}

	@Override
	public void addTotalwinVO(IVTradedPubVO tradevo) {
		totalwinvos.add(getNewTotalwinVO(tradevo));
	}

	@Override
	public TotalwinVO getNewTotalwinVO(IVTradedPubVO tradevo) {
		TotalwinVO totalwinvo = new TotalwinVO();
		// totalwinvo.setPk_corp(getPk_corp());
		totalwinvo
				.setPk_glorgbook(costParaVO.getCostplanvo().getPk_glorgbook());
		totalwinvo.setTrade_date(tradevo.getTrade_date());
		totalwinvo.setPk_costplan(costParaVO.getCostplanvo().getPk_costplan());
		totalwinvo.setTablename(tradevo.getTableName());
		totalwinvo.setPk_stocktrade(tradevo.getPrimaryKey());
		totalwinvo.setBargain_num(tradevo.getBargain_num());
		totalwinvo.setBargain_sum(tradevo.getBargain_sum());
		totalwinvo.setSellcost(tradevo.getSellcost());
		totalwinvo.setTotal_win(tradevo.getTotal_win());
		totalwinvo.setFact_sum(tradevo.getFact_sum());
		return totalwinvo;
	}

	@Override
	public boolean isCheckStock() {
		return isCheckStock;
	}

	/**
	 * ������Ϣͳһ��������ֻ����ҵ����Ϣ
	 * 
	 * @param vo
	 * @param bvo
	 * @param error
	 * @throws BusinessException
	 */
	@Override
	public void handleException(SuperVO vo, SuperVO bvo, String error)
			throws BusinessException {
		String pk_securities = null;
		if (bvo != null) {
			pk_securities = (String) bvo.getAttributeValue("pk_securities");
		} else {
			pk_securities = (String) vo.getAttributeValue("pk_securities");
		}
		UFDate trade_date = (UFDate) vo.getAttributeValue("trade_date");
		// String pk_billtype = (String) vo.getAttributeValue("pk_billtype");
		// String bill_code = (String) vo.getAttributeValue("bill_code");
		/**
		 * ��ѯ֤ȯ��������
		 */
		String name = null;
		StringBuffer sf = new StringBuffer();
		sf.append("select name from  sec_securities where pk_securities = '"
				+ pk_securities + "' ");
		try {
			name = (String) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							String name = null;
							if (rs.next()) {
								name = rs.getString("name");
							}
							return name;
						}
					});
		} catch (DAOException e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		throw new BusinessException(" ��������:" + trade_date.toLocalString()
				+ ", �ʲ�����:" + name + " " + error);

	}

	@Override
	public boolean getIsinit() {
		return isinit;
	}

	@Override
	public void setIsinit(boolean isinit) {
		this.isinit = isinit;
	}

	@Override
	public boolean getIstally() {
		return istally;
	}

	@Override
	public void setIstally(boolean istally) {
		this.istally = istally;
	}

	/**
	 * ���������ɱ��������Ŀ��
	 * 
	 * @param stockbalancevo
	 * @param costFieldArray
	 * @author liangwei
	 */
	@Override
	public void addCostChangeBalanceVO(StockBalanceVO stockbalancevo,
			String[] costFieldArray) {
		// ȥ�����ά��
		List list = new ArrayList();
		for (int i = 0; i < costFieldArray.length; i++) {
			String temp = costFieldArray[i];
			if (!"pk_stocksort".equals(temp)) {
				list.add(temp);
			}
		}
		String[] newCostFieldArray = (String[]) list.toArray(new String[list
				.size()]);
		String key = VOUtil.getCombinesKey(stockbalancevo, newCostFieldArray);
		if (costchangevomap.containsKey(key)) {// ��ӹ�һ�εĿ���¼������Ҫ�����һ�Ρ�
			return;
		}
		costchangevomap.put(key, stockbalancevo);
	}

	@Override
	public void saveCostChangeVOs() throws Exception {
		List<CostAdjustmentVO> listvos = getCostChangeVOs();
		int size = listvos.size();
		if (size == 0) {
			return;
		}
		String date = getCurrdate();
		String pk_group = costParaVO.getCheckParavo().getPk_group();
		String pk_org = costParaVO.getCheckParavo().getPk_org();
		String pk_user = costParaVO.getCheckParavo().getPk_user();
		String[] billcodes = pm.getBatchBillCodes(
				SystemConst.BillType_CostAdjustment, pk_group, pk_org, null,
				listvos.size());
		for (int i = 0; i < size; i++) {
			CostAdjustmentVO vo = listvos.get(i);
			vo.setAttributeValue("dr", 0);
			vo.setTrade_date(new UFDate(date));
			vo.setCreationtime(pm.getDateTime(date));
			vo.setCreator(pk_user);
			vo.setBillmaker(pk_user);
			vo.setMakedate(pm.getDateTime(date));
			vo.setApprover(pk_user);
			vo.setApprovedate(pm.getDateTime(date));
			vo.setBillno(billcodes[i]);
			vo.setState(SystemConst.statecode[3]);
		}
		// �м�����ɱ����������ݣ��Ա���ˡ������߼���ѯ
		new SpecialDealPendBill().add(listvos.toArray(new SuperVO[] {}));
		PrivateMethod.getInstance().getBaseDAO().insertVOList(listvos);
	}

	/**
	 * 
	 * ���ؼ����ĳɱ������� ��57�Ĳ��죺���ﲻ�����ڳ����ܲ����ĳɱ��������ù��ܷ����ڳ����˴���
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CostAdjustmentVO> getCostChangeVOs() throws Exception {

		List<CostAdjustmentVO> vos = new ArrayList<CostAdjustmentVO>();
		// ����Ҫ��ƽ�Ŀ��ƽ��
		for (StockBalanceVO vo : costchangevomap.values()) {
			vos.addAll(calculateCost(vo));// ����ɱ�������
		}

		return vos;
	}

	/**
	 * ����ɱ��������������ɳɱ�������
	 * 
	 * @param balancevo
	 * @return
	 * @throws Exception
	 *             List<CostAdjustmentVO> TODO������˵����
	 */
	@Override
	public List<CostAdjustmentVO> calculateCost(StockBalanceVO balancevo)
			throws Exception {
		String trade_date = getCurrdate();
		String[] costFieldArray = getCostParaVO().getCostplanvo()
				.getCostFieldArray();
		String pk_assetsprop = balancevo.getPk_assetsprop();
		String key = null;
		StockBalanceVO vo = null;
		UFDouble stocks_sum = null;// �ܽ��
		UFDouble stocks_num = null;// ������
		List<StockBalanceVO> list = new ArrayList<StockBalanceVO>();
		BanlanceQueryKeyVO sbqkvo = new BanlanceQueryKeyVO();
		// sbqkvo.setKey(key);
		sbqkvo.setPk_assetsprop(pk_assetsprop);
		sbqkvo.setTrade_date(trade_date);
		BaseDAO dao = PrivateMethod.getInstance().getBaseDAO();
		for (String pk_stocksort : SystemConst.pk_stocksort) {
			if (((StocksortVO) dao
					.retrieveByPK(StocksortVO.class, pk_stocksort)).getIstp()
					.booleanValue()) {
				balancevo.setPk_stocksort(pk_stocksort);
				key = getCurrbilltypegroupvo().getPk_billtypegroup()
						+ VOUtil.getCombinesKey(balancevo, costFieldArray);
				sbqkvo.setKey(key);
				sbqkvo.setPk_stocksort(pk_stocksort);
				vo = balancetool.getStockbalanceVO(sbqkvo, this);
				if (vo == null) {
					continue;
				}
				stocks_sum = pm.add(stocks_sum, vo.getStocks_sum());
				stocks_num = pm.add(stocks_num, vo.getStocks_num());
				list.add(vo);
			}
		}
		if (list.size() < 2) {// ��ƽ�Ŀ����������Ļ�������Ҫ��ƽ
			return new ArrayList<CostAdjustmentVO>();
		}
		// �����stocks_num=0���������������²���Ҫ���е���
		if (stocks_num.doubleValue() <= 0) {
			return new ArrayList<CostAdjustmentVO>();
		}
		UFDouble price = pm.div(stocks_sum, stocks_num);// ����
		UFDouble[] stock_sums = new UFDouble[list.size()];// ��������
		double[] difference = new double[list.size()];// ����ǰ����
		int j = 0;// ��¼�������Ŀ�,��������β��
		int k = 0;// ����������
		UFDouble maxstock_num = new UFDouble(0);
		double enddifference = 0;
		boolean force = false;// ǿ�Ƶ���
		for (int i = 0; i < list.size(); i++) {
			stock_sums[i] = pm.multiply(price, list.get(i).getStocks_num());
			stock_sums[i] = pm.setScale(stock_sums[i], true, true);
			difference[i] = pm.sub(stock_sums[i], list.get(i).getStocks_sum())
					.doubleValue();
			enddifference += difference[i];
			if (list.get(i).getStocks_num().doubleValue() == 0
					&& difference[i] != 0) {
				force = true;// ����Ϊ0���ɱ��������Ϊ0
			}
			if (difference[i] > 0.01 || difference[i] < -0.01) {// һ��Ǯ������������������гɱ�����
				k++;
			}
			if (list.get(i).getStocks_num().compareTo(maxstock_num) > 0) {
				maxstock_num = list.get(i).getStocks_num();
				j = i;
			}
		}
		if (k < 2 && !force) {// ��ƽ�Ŀ����������Ļ������Ҳ���Ҫǿ�Ƶ�ƽ
			return new ArrayList<CostAdjustmentVO>();
		}
		difference[j] -= enddifference;// ����β������
		List<CostAdjustmentVO> costchangevos = new ArrayList<CostAdjustmentVO>();
		CostAdjustmentVO[] costchangevoarray = new CostAdjustmentVO[list.size()];

		for (int i = 0; i < costchangevoarray.length; i++) {
			if (difference[i] == 0) {
				stocks_sum = pm.sub(stocks_sum, stock_sums[i]);
				continue;
			}
			costchangevoarray[i] = getCostchangeVO(list.get(i), costFieldArray);
			costchangevoarray[i].setBargain_sum(new UFDouble(difference[i]));
			list.get(i).setStocks_sum(stock_sums[i]);
			stocks_sum = pm.sub(stocks_sum, stock_sums[i]);
			costchangevos.add(costchangevoarray[i]);
			// ���ڳɱ���������������¿�棬���ڴ�����³ɱ�
			// ����β���ȷ��������Ŀ���ܺ�Ҫ������ǰ�Ŀ�棬�������Ŀ��=�ܿ��-�ѵ�����Ŀ��
			if (i == list.size() - 1) {
				// β��������Ŀ����
				list.get(j).setStocks_sum(
						list.get(j).getStocks_sum().add(stocks_sum));
			}
		}
		for (int i = 0; i < list.size(); i++) {
			StockBalanceVO stockBalanceVO = list.get(i);
			sbqkvo.setKey(getCurrbilltypegroupvo().getPk_billtypegroup()
					+ VOUtil.getCombinesKey(stockBalanceVO, costFieldArray));
			sbqkvo.setPk_stocksort(stockBalanceVO.getPk_stocksort());
			balancetool.updateStockbalanceVO(sbqkvo, stockBalanceVO);
		}
		return costchangevos;
	}

	/**
	 * ���ɳɱ��䶯��
	 * 
	 * @param balancevo
	 * @param costFieldArray
	 * @return
	 * @throws Exception
	 */
	@Override
	public CostAdjustmentVO getCostchangeVO(StockBalanceVO balancevo,
			String[] costFieldArray) throws Exception {
		CostAdjustmentVO costchangevo = new CostAdjustmentVO();
		for (String key : costFieldArray) {
			costchangevo.setAttributeValue(key,
					balancevo.getAttributeValue(key));
		}
		/** JINGQT 2016��4��8�� �ɱ��������޷���ʾ������֯���������� ADD START */
		costchangevo.setAttributeValue(CostAdjustmentVO.PK_ORG_V,
				balancevo.getAttributeValue(StockBalanceVO.PK_ORG_V));
		/** JINGQT 2016��4��8�� �ɱ��������޷���ʾ������֯���������� ADD END */
		costchangevo.setPk_securities(balancevo.getPk_securities());
		costchangevo.setPk_billtype(SystemConst.BillType_CostAdjustment);
		costchangevo.setTranstypecode(SystemConst.TransType_CostAdjustment);
		return costchangevo;
	}

	/**
	 * �������۳ɱ�
	 * 
	 * @param trade_date
	 * @param pk_assetsprop
	 * @param pk_stocksort
	 * @param key
	 * @param bargain_num
	 * @param ishaszj
	 * @return
	 * @throws Exception
	 *             UFDouble TODO������˵����
	 */
	@Override
	public UFDouble getSellcost(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, UFDouble bargain_num, boolean ishaszj)
			throws Exception {
		if (bargain_num == null || bargain_num.doubleValue() == 0) {
			return new UFDouble(0);
		}
		UFDouble stocks_sum = null;
		UFDouble stocks_num = null;
		UFDouble sellcost = null;
		StockBalanceVO vo = null;
		String pk_stocksort = queryvo.getPk_stocksort();
		StocksortVO stocksortvo = pm.getStocksortVO(pk_stocksort);
		if (!stocksortvo.getIstp().booleanValue()) {// �ÿⲻ�����ƽ��ֱ��ȡ���ۼ���ɱ���
			vo = balancetool.getStockbalanceVO(queryvo, costingtool);
			if (vo == null) {
				return null;
			}
			stocks_num = vo.getStocks_num();// ������� ������ �������
			stocks_sum = vo.getStocks_sum();// ������� ������ �����
			if (stocks_num != null && stocks_num.compareTo(bargain_num) == 0) {
				return stocks_sum;
			}
			sellcost = pm.multiply(stocks_sum, bargain_num);
			sellcost = pm.div(sellcost, stocks_num);
			return pm.setScale(sellcost, true, true);
		} else {// �ÿ�����ƽ
			if (ishaszj) {// ����֤��ɱ����Ȱ�������ɱ���ˣ����û�н�棬��ȥȡƽ���ɱ�
				vo = balancetool.getStockbalanceVO(queryvo, costingtool);
				if (vo != null) {
					stocks_num = vo.getStocks_num();
					if (stocks_num != null && stocks_num.doubleValue() != 0) {
						stocks_sum = vo.getStocks_sum();
						if (stocks_num.compareTo(bargain_num) == 0) {
							return stocks_sum;
						} else {
							sellcost = pm.multiply(stocks_sum, bargain_num);
							sellcost = pm.div(sellcost, stocks_num);
							return pm.setScale(sellcost, true, true);
						}
					}
				}
			}
			for (String pk_stocksort_tp : SystemConst.pk_stocksort) {// ƽ���ɱ�
				if (pm.getStocksortVO(pk_stocksort_tp).getIstp().booleanValue()) {
					queryvo.setPk_stocksort(pk_stocksort_tp);
					vo = balancetool.getStockbalanceVO(queryvo, costingtool);
					if (vo != null) {
						stocks_sum = pm.add(stocks_sum, vo.getStocks_sum());
						stocks_num = pm.add(stocks_num, vo.getStocks_num());
					}
				}
			}
			if (stocks_num == null || stocks_num.doubleValue() == 0) {
				// ���ԭ
				queryvo.setPk_stocksort(pk_stocksort);
				return null;
			}
			if (ishaszj) {
				queryvo.setPk_stocksort(SystemConst.pk_zjstocksort);
				vo = balancetool.getStockbalanceVO(queryvo, costingtool);
				if (vo != null) {// ȥ��֤��ĳɱ�
					stocks_sum = pm.sub(stocks_sum, vo.getStocks_sum());
					stocks_num = pm.sub(stocks_num, vo.getStocks_num());
				}
			}
			if (stocks_num != null && stocks_num.compareTo(bargain_num) == 0) {
				// ���ԭ
				queryvo.setPk_stocksort(pk_stocksort);
				return stocks_sum;
			}
			sellcost = pm.multiply(stocks_sum, bargain_num);
			sellcost = pm.div(sellcost, stocks_num);

			// ���ԭ
			queryvo.setPk_stocksort(pk_stocksort);
			return pm.setScale(sellcost, true, true);
		}
	}

	/**
	 * �������۳ɱ� ----������Ȩ�� (���漰��֤��͵�ƽ)<BR>
	 * 2015��9��24�� ������Ȩ�������۳ɱ�ʱ�����ܲ�ѯʵʱ�Ŀ�棬��Ҫʹ�ô������Ŀ����Ϣ
	 * 
	 * @param bargain_num
	 *            ����
	 * @param ʹ�õĿ����Ϣ
	 * @return ���۳ɱ�
	 * @author JINGQT
	 */
	@Override
	public UFDouble getSellcostForOptionsTrade(UFDouble bargain_num,
			StockBalanceVO vo) throws Exception {
		if (bargain_num == null || bargain_num.doubleValue() == 0) {
			return UFDouble.ZERO_DBL;
		}
		UFDouble stocks_sum = null;
		UFDouble stocks_num = null;
		UFDouble sellcost = null;

		if (vo == null) {
			return null;
		}
		stocks_num = vo.getStocks_num();// ������� ������ �������
		stocks_sum = vo.getStocks_sum();// ������� ������ �����
		if (stocks_num != null && stocks_num.compareTo(bargain_num) == 0) {
			return stocks_sum;
		}
		sellcost = pm.multiply(stocks_sum, bargain_num);
		sellcost = pm.div(sellcost, stocks_num);
		return pm.setScale(sellcost, true, true);
	}

	/**
	 * ˰��ת����
	 * 
	 * @param trade_date
	 * @param pk_assetsprop
	 * @param pk_stocksort
	 * @param key
	 * @param bargain_num
	 * @return
	 * @throws Exception
	 *             UFDouble TODO������˵����
	 */
	@Override
	public UFDouble getTaxOutcost(BanlanceQueryKeyVO queryvo,
			ICostingTool costingtool, UFDouble bargain_num) throws Exception {
		String pk_glorg = costingtool.getCostParaVO().getCheckParavo()
				.getPk_glorgbook();
		boolean isjztax = new QueryBasePubInfo().getBooleanFromInitcode(
				pk_glorg, CostConstant.PARAM_JZTAX);
		if (!isjztax)
			return new UFDouble(0);
		if (bargain_num == null || bargain_num.doubleValue() == 0) {
			return new UFDouble(0);
		}
		UFDouble stocks_tax = null;
		UFDouble stocks_num = null;
		UFDouble taxoutcost = null;
		StockBalanceVO vo = null;
		String pk_stocksort = queryvo.getPk_stocksort();
		StocksortVO stocksortvo = pm.getStocksortVO(pk_stocksort);
		if (!stocksortvo.getIstp().booleanValue()) {// �ÿⲻ�����ƽ��ֱ��ȡ���ۼ���˰��
			vo = balancetool.getStockbalanceVO(queryvo, costingtool);
			if (vo == null)
				return UFDouble.ZERO_DBL;
			stocks_num = vo.getStocks_num();
			stocks_tax = vo.getStocks_tax();
			if (stocks_num != null && stocks_num.compareTo(bargain_num) == 0) {
				return stocks_tax;
			}
			taxoutcost = pm.multiply(stocks_tax, bargain_num);
			taxoutcost = pm.div(taxoutcost, stocks_num);
			return pm.setScale(taxoutcost, true, true);
		}
		for (String pk_stocksort_tp : SystemConst.pk_stocksort) {
			if (pm.getStocksortVO(pk_stocksort_tp).getIstp().booleanValue()) {
				queryvo.setPk_stocksort(pk_stocksort_tp);
				vo = balancetool.getStockbalanceVO(queryvo, costingtool);
				if (vo != null) {
					stocks_tax = pm.add(stocks_tax, vo.getStocks_tax());
					stocks_num = pm.add(stocks_num, vo.getStocks_num());
				}
			}
		}
		queryvo.setPk_stocksort(SystemConst.pk_zjstocksort);
		vo = balancetool.getStockbalanceVO(queryvo, costingtool);
		if (vo != null) {// ȥ��֤���˰��
			stocks_tax = pm.sub(stocks_tax, vo.getStocks_tax());
			stocks_num = pm.sub(stocks_num, vo.getStocks_num());
		}
		if (stocks_num != null && stocks_num.compareTo(bargain_num) == 0) {
			// ���ԭ
			queryvo.setPk_stocksort(pk_stocksort);
			return stocks_tax;
		}
		taxoutcost = pm.multiply(stocks_tax, bargain_num);
		taxoutcost = pm.div(taxoutcost, stocks_num);
		// ���ԭ
		queryvo.setPk_stocksort(pk_stocksort);
		return pm.setScale(taxoutcost, true, true);
	}

	@Override
	public boolean isCheckFund() {
		return isCheckFund;
	}

	@Override
	public void setCheckFund(boolean isCheckFund) {
		this.isCheckFund = isCheckFund;
	}

	@Override
	public Map<String, ForwardVO> getInterestjz() {
		return interestjz;
	}

	public void setInterestjz(Map<String, ForwardVO> interestjz) {
		this.interestjz = interestjz;
	}

}
