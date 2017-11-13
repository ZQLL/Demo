package nc.impl.fba_smt.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;

import nc.bs.logging.Logger;
import nc.itf.fba_smt.report.IStockOutDetailRpt;
import nc.pub.freereport.PubMethod;
import nc.pub.freereport.PubReportDataQuery;
import nc.pub.freereport.ReportConst;
import nc.pub.freereport.ReportDataUtil;
import nc.pub.freereport.SimReportUtils;
import nc.pub.report.SMTReportUtil;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_scost.cost.costplan.CostPlanVO;
import nc.vo.fba_scost.cost.pub.SysInitCache;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_smt.report.StockOutDetailVO;
import nc.vo.fba_smt.report.StockOutDetailViewMeta;
import nc.vo.fba_smt.trade.margintrade.MarginTradeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.report.ReportQueryConUtil;
import nc.vo.pubapp.report.ReportQueryResult;
import nc.vo.trade.voutils.VOUtil;

public class StockOutDetailRptImpl implements IStockOutDetailRpt {

	@Override
	public ReportQueryResult queryDetailData(IContext context)
			throws BusinessException {
		ReportQueryConUtil qryconutil = new ReportQueryConUtil(context);
		ConditionVO[] conVos = (ConditionVO[]) context
				.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
		if (qryconutil.isNull() && (conVos == null || conVos.length == 0)) {
			ReportQueryResult result = new ReportQueryResult();
			DataSet dataset = new DataSet();
			dataset.setMetaData(this.genDataSet());
			result.setDataSet(dataset);
			return result;
		}

		ReportQueryResult queryResult = new ReportQueryResult();
		queryResult.setDataSet(getStockOutDetailDataSet(context, conVos));
		return queryResult;

	}

	private DataSet getStockOutDetailDataSet(IContext context,
			ConditionVO[] conVos) throws BusinessException {
		String dateValue = SimReportUtils.getDateCon(conVos);// ȡ�ò�ѯ�����е���������
		String org = SimReportUtils.getOrg(conVos);
		String glBook = SimReportUtils.getGLBook(conVos);
		String de_group = PubMethod.getInstance().getDeGroup(org);
		boolean isrzrq = false;
		try {
			isrzrq = SysInitCache.getInstance()
					.getIsRzrq(de_group, org, glBook);
		} catch (Exception e1) {
			Logger.error(e1.getMessage());
		}
		if (!isrzrq) {
			throw new BusinessException("��ǰҵ��Ԫ����������ȯ���ţ�");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("pk_org", org);
		map.put("pk_group", de_group);
		map.put("pk_glorgbook", glBook);

		// UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue))
		// .getDateBefore(ReportConst.DAY);
		UFDate startDate = new UFDate(SimReportUtils.getStartDate(dateValue));
		String endDate = SimReportUtils.getEndDate(dateValue);
		CostPlanVO costPlanVO = PubMethod.getInstance().getCostPlanVO(org,
				glBook, de_group);

		PubReportDataQuery query = new PubReportDataQuery();
		SmtReportQueryUtil querySmt = new SmtReportQueryUtil();
		List<StockBalanceVO> firBal = null;
		// ���Ӳ�ѯ�������(�˲� ��֯ ���)
		String condition = SMTReportUtil.getStockBalaCondition(
				ReportConst.FUND_B_MARK, conVos, ReportConst.STOCKYR);// ��ѯ���ڿ��
		try {
			// ��ѯ����ڳ�
			firBal = query.getStockBalance(condition, startDate.toString(),
					costPlanVO);
		} catch (BusinessException e) {
			throw new BusinessException("�ڳ���ѯ����");
		}

		String key = null;
		// �����ڳ�����������д��ڣ���ֱ�Ӹ�ֵ����������ڣ�������
		int firSize = firBal.size();
		UFDouble zero = new UFDouble(0);
		StockOutDetailVO drv = null;
		StockBalanceVO sbv = null;
		// String[] manFieldV = SimReportUtils.getManFieldArray(costPlanVO);
		String[] manFieldV = new String[] { "pk_assetsprop", "pk_group",
				"pk_org", "pk_glorgbook", "pk_capaccount", "begin_date",
				"end_date", "pk_securities" };
		Map<String, StockOutDetailVO> trademap = new HashMap<String, StockOutDetailVO>();
		PubMethod pm = PubMethod.getInstance();
		for (int i = 0; i < firSize; i++) {
			sbv = firBal.get(i);
			if (zero.equals(sbv.getStocks_num())
					&& zero.equals(sbv.getStocks_sum())) {
				continue;
			}
			key = VOUtil.getCombinesKey(sbv, manFieldV);
			if (trademap.containsKey(key)) {
				drv = trademap.get(key);
			} else {
				drv = new StockOutDetailVO();
				for (int j = 0; j < manFieldV.length; j++) {
					drv.setAttributeValue(manFieldV[j],
							sbv.getAttributeValue(manFieldV[j]));
				}
				trademap.put(key, drv);
			}

			drv.setFirstnum(pm.add(drv.getFirstnum(), sbv.getStocks_num()));
			drv.setFirstsum(pm.add(drv.getFirstsum(), sbv.getStocks_sum()));
			drv.setTrade_date(startDate);// �������ɱ�����ͨ��ʱ����ˣ�����д��ʱ��
		}
		Map<String, UFDouble> closeMap = query.getClosePrice(endDate);
		// ��ͻ��ڳ�֤ȯ(�ڳ�֤ȯ)1
		List<SuperVO> rcvos = null;
		try {
			rcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCJL);
			trademap = assignTransferData(trademap, rcvos, manFieldV, 1,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ��ͻ��ڳ�֤ȯ����");
		}
		// ��������(����ڳ�) 2
		List<SuperVO> hgrcvos = null;
		try {
			hgrcvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.HGRCJL);
			trademap = assignTransferData(trademap, hgrcvos, manFieldV, 2,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ���ڿ�������������ݳ���");
		}
		// �ͻ��黹֤ȯ(�ڳ��黹) 3
		List<SuperVO> rcghvos = null;
		try {
			rcghvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.RCGHJL);
			trademap = assignTransferData(trademap, rcghvos, manFieldV, 3,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯ�ͻ��黹֤ȯ����");
		}
		// תΪ����֤ȯ(��¼) 4
		List<SuperVO> yqzqvos = null;
		try {
			yqzqvos = querySmt.getMarginData(condition, startDate.toString(),
					endDate, costPlanVO, ReportConst.YQJL);
			trademap = assignTransferData(trademap, yqzqvos, manFieldV, 4,
					startDate);// �������
		} catch (Exception e) {
			throw new BusinessException("��ѯתΪ���ڼ�¼����");
		}

		for (StockOutDetailVO vo : trademap.values()) {
			// �������� = �ڳ�+��������
			UFDouble addnum = pm.add(vo.getFirstnum(),// �ڳ�
					pm.add(vo.getRczqnum(),// �ڳ�֤ȯ
							vo.getHgrcnum()));// ����ڳ�
			UFDouble addsum = pm.add(vo.getFirstsum(),// �ڳ�
					pm.add(vo.getRczqsum(),// �ڳ�֤ȯ
							vo.getHgrcsum()));// ����ڳ�
			// ��������
			UFDouble subnum = pm.add(vo.getRcghnum(), vo.getYqjlnum());// �ڳ��黹+���ڼ�¼
			UFDouble subsum = pm.add(vo.getRcghsum(), vo.getYqjlsum());// �ڳ��黹+���ڼ�¼
			// ��ĩ���= �������� - ��������
			UFDouble endnum = pm.add(vo.getEndnum(), addnum).sub(subnum);
			UFDouble endsum = pm.add(vo.getEndsum(), addsum).sub(subsum);
			vo.setEndnum(endnum);
			vo.setEndsum(endsum);
			// ���̼�
			UFDouble closeprice = closeMap.get(vo.getPk_securities());
			vo.setCloseprice(closeprice);
			// ֤ȯ��ֵ = ���̼�*��ĩ�������
			UFDouble marketvalue = pm.multiply(closeprice, endnum);
			vo.setMarketValue(marketvalue);
			// Ǳӯ(��) = ֤ȯ��ֵ-��ĩ�����
			vo.setProfit(pm.sub(marketvalue, endsum));
		}
		Collection<StockOutDetailVO> c = trademap.values();
		return PubMethod.getInstance().convertVOToArray(genDataSet(),
				new ArrayList(c));
	}

	private MetaData genDataSet() {
		List<Field> list = new ArrayList<Field>();

		for (String key : StockOutDetailViewMeta.FUNDTRADE_S_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		for (String key : StockOutDetailViewMeta.EXT_D_FIELD) {
			Field field = ReportDataUtil.createDoubleField(key);
			list.add(field);
		}

		for (String key : StockOutDetailViewMeta.EXT_DATE_FIELD) {
			Field field = ReportDataUtil.createStringFiled(key);
			list.add(field);
		}

		Field[] fields = new Field[list.size()];
		list.toArray(fields);
		return new MetaData(fields);
	}

	/**
	 * 
	 * @param trademap
	 *            ��������VO��map
	 * @param mtvos
	 *            �ɹ�ת����(flag=0) ����ת�ɹ���List
	 * @param manFieldV
	 * @param closeMap
	 * @return
	 */
	private Map<String, StockOutDetailVO> assignTransferData(
			Map<String, StockOutDetailVO> trademap, List<SuperVO> mtvos,
			String[] manFieldV, int flag, UFDate startDate) {
		SuperVO mvto = null;
		StockOutDetailVO drv = null;
		PubMethod pm = PubMethod.getInstance();
		// ����ȥ�������֯���� ��Ϊת��ͨ������ڿ� �黹֤��� �������������ʾ����
		manFieldV = new String[] { "pk_assetsprop", "pk_group", "pk_org",
				"pk_glorgbook", "pk_capaccount", "begin_date", "end_date",
				"pk_securities" };
		if (mtvos != null && mtvos.size() > 0) {
			for (int i = 0; i < mtvos.size(); i++) {
				mvto = (MarginTradeVO) mtvos.get(i);
				String key = VOUtil.getCombinesKey(mvto, manFieldV);
				if (trademap.containsKey(key)) {// �Ѿ�����
					drv = trademap.get(key);
				} else {
					drv = new StockOutDetailVO();
					for (int j = 0; j < manFieldV.length; j++) {
						drv.setAttributeValue(manFieldV[j],
								mvto.getAttributeValue(manFieldV[j]));
					}
					trademap.put(key, drv);
				}
				if (1 == flag) {// �ڳ���¼
					drv.setRczqnum(pm.add(drv.getRczqnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRczqsum(pm.add(drv.getRczqsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (2 == flag) {// ��������(����ڳ�)
					drv.setHgrcnum(pm.add(drv.getHgrcnum(),
							((MarginTradeVO) mvto).getBargain_num()));
				} else if (3 == flag) {// �ڳ��黹
					drv.setRcghnum(pm.add(drv.getRcghnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setRcghsum(pm.add(drv.getRcghsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				} else if (4 == flag) {// ���ڼ�¼
					drv.setYqjlnum(pm.add(drv.getYqjlnum(),
							((MarginTradeVO) mvto).getBargain_num()));
					drv.setYqjlsum(pm.add(drv.getYqjlsum(),
							((MarginTradeVO) mvto).getBargain_sum()));
				}
				drv.setTrade_date(startDate);// �������ɱ�����ͨ��ʱ����ˣ�����д��ʱ��
			}
		}
		return trademap;
	}

}
