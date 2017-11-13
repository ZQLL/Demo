package nc.impl.fba_zqjd.trade.report;

import java.util.List;

import nc.pub.smart.metadata.MetaData;
import nc.vo.fba_zqjd.trade.report.NewStockChangeVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * ծȯ������䶯������
 * @author mx
 *
 */
public class StockChangeReportTool {
	
	
	/**
	 * @param str
	 * @return 
	 * ��ʼ���ڸ�ʽת������������ƴ��
	 */
	public static UFDateTime getBeginDate(String str) {

		UFDateTime beginDate = new UFDateTime();
		if (str.length() != 0 && str != null) {
			if (str.length() <= 10) {
				str = str.concat(" 00:00:00");
			}
			beginDate = new UFDateTime(str);
		}
		return beginDate;
	}

	/**
	 * @param str
	 * @return
	 * �������ڸ�ʽת������������ƴ��
	 */
	public static UFDateTime getEndDate(String str) {

		UFDateTime endDate = new UFDateTime();
		if (str.length() != 0 && str != null) {
			if (str.length() <= 10) {
				str = str.concat(" 23:59:59");
			}
			endDate = new UFDateTime(str);
		}
		return endDate;
	}
		/**
		 * @param transtypecode ��������
		 * @param beginDate		��ʼ����
		 * @return ��ѯ�ڳ����SQL(֤ȯ���ױ�)
		 */
	public static String getStartSql(String transtypecode, String beginDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("select max(a.pk_group) pk_group,");
		sb.append("max(a.pk_org) pk_org,");
		sb.append("max(a.pk_assetsprop) pk_assetsprop,");
		sb.append("max(a.pk_stocksort) pk_stocksort,");
		sb.append("max(a.pk_securities) pk_securities,");
		sb.append("max(a.pk_capaccount) pk_capaccount,");
		sb.append("a.pk_securities,");
		sb.append("sum(a.bargain_num) bargain_num ");
		sb.append("from sim_zqjd a ");
		sb.append("where a.state >= 2 " );
		sb.append("and isnull(a.dr,0) = 0 " );
		sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
		sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
		sb.append("and trade_date < \'" + beginDate + "\' ");
		sb.append("group by a.pk_securities");

		return sb.toString();
	}

		/**
		 * @param transtypecode ��������
		 * @param beginDate     ��ʼ����
		 * @param endDate       ��������
		 * @return ��ѯ���ڿ��SQL(֤ȯ���ױ�)
		 */
		public static String getNowSql(String transtypecode, String beginDate,
				String endDate) {
			StringBuffer sb = new StringBuffer();
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
			sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
			sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
			sb.append("and trade_date between \'" + beginDate + "\' and \'" + endDate + "|| 23:59:59\'");
			sb.append("group by a.pk_securities");

			return sb.toString();
		}
		
		/**
		 * @param field		��ѯ�ֶ� ����or���
		 * @param beginDate	��ʼ����
		 * @param endDate	��������
		 * @return ��ѯ��Ӧ�ֶα�������SQL
		 */
		public static String getNowSellSql(String beginDate,
				String endDate){
			StringBuffer sb = new StringBuffer();
			sb.append("select max(a.pk_group) pk_group,");
			sb.append("max(a.pk_org) pk_org,");
			sb.append("max(a.pk_assetsprop) pk_assetsprop,");
			sb.append("max(a.pk_stocksort) pk_stocksort,");
			sb.append("max(a.pk_securities) pk_securities,");
			sb.append("max(a.pk_capaccount) pk_capaccount,");
			sb.append("a.pk_securities,");
			sb.append("sum(a.bargain_num) bargain_num, ");
			sb.append("sum(a.fact_sum) bargain_sum ");
			sb.append("from sim_stocktrade a ");
			sb.append("where isnull(a.dr,0) = 0 " );
			//2015��5��25�� zhaoxmc ��������״̬�ж�
			sb.append("and a.state >='2'");
			sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
			sb.append("and a.transtypecode = 'HV3A-0xx-22' ");//������
			sb.append("and trade_date between \'" + beginDate + "\' and \'" + endDate + "|| 23:59:59\'");
			sb.append("group by a.pk_securities");

			return sb.toString();
//			String sql = "select a.pk_securities,sum(a." + field + ") " + field
//					+ " from sim_zqjdtally a where " + field + ">0"
//					+ "and trade_date between \'" + beginDate + "\' and \'"
//					+ endDate + "\'" + "group by a.pk_securities";
//			return sql;
		}
		
		/**
		 * @param field		��ѯ�ֶ� ����or���
		 * @param beginDate	��ʼ����
		 * @param endDate	��������
		 * @return ��ѯ��Ӧ�ֶα�������SQL(���׼�¼��ծ���뵥)
		 */
		public static String getNowBuySql(String beginDate,
				String endDate){
			StringBuffer sb = new StringBuffer();
			sb.append("select max(a.pk_group) pk_group,");
			sb.append("max(a.pk_org) pk_org,");
			sb.append("max(a.pk_assetsprop) pk_assetsprop,");
			sb.append("max(a.pk_stocksort) pk_stocksort,");
			sb.append("max(a.pk_securities) pk_securities,");
			sb.append("max(a.pk_capaccount) pk_capaccount,");
			sb.append("a.pk_securities,");
			sb.append("sum(a.bargain_num) bargain_num, ");
			sb.append("sum(a.fact_sum) bargain_sum ");
			sb.append("from sim_stocktrade a ");
			sb.append("where isnull(a.dr,0) = 0 " );
			sb.append("and a.state >='2'");
			sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
			sb.append("and a.transtypecode = 'HV3A-0xx-21' ");//���뵥
			sb.append("and trade_date between \'" + beginDate + "\' and \'" + endDate + "|| 23:59:59\'");
			sb.append("group by a.pk_securities");

			return sb.toString();
//			String sql = "select a.pk_securities,sum(a." + field + ") " + field
//					+ " from sim_zqjdtally a where " + field + "<0"
//					+ "and trade_date between \'" + beginDate + "\' and \'"
//					+ endDate + "\'" + "group by a.pk_securities";
//			return sql;
		}
		/**
		 * @param string
		 * @param beginDate
		 * @param endDate
		 * @return �������븺ծ�ɱ�sql
		 */
		public static String getNowDebtSql(String transtypecode, String beginDate,
				String endDate) {
			StringBuffer sb = new StringBuffer();
			sb.append("select max(a.pk_group) pk_group,");
			sb.append("max(a.pk_org) pk_org,");
			sb.append("max(a.pk_assetsprop) pk_assetsprop,");
			sb.append("max(a.pk_stocksort) pk_stocksort,");
			sb.append("max(a.pk_securities) pk_securities,");
			sb.append("max(a.pk_capaccount) pk_capaccount,");
			sb.append("a.pk_securities,");
			sb.append("sum(a.sellcost) sellcost ");
			sb.append("from sim_stocktrade a ");
			sb.append("where a.state >= 2 ");
			sb.append("and isnull(a.dr,0) = 0 " );
			sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
			sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
			sb.append("and trade_date between \'" + beginDate + "\' and \'" + endDate + "|| 23:59:59\'");
			sb.append("group by a.pk_securities");

			return sb.toString();
		}
		// ��ϢSQL
		public static String getInterestSql(String transtypecode, String beginDate,
				String endDate){
			StringBuffer sb = new StringBuffer();
			sb.append("select max(a.pk_group) pk_group,");
			sb.append("max(a.pk_org) pk_org,");
			sb.append("max(a.pk_assetsprop) pk_assetsprop,");
			sb.append("max(a.pk_stocksort) pk_stocksort,");
			sb.append("max(a.pk_securities) pk_securities,");
			sb.append("max(a.pk_capaccount) pk_capaccount,");
			sb.append("a.pk_securities,");
			sb.append("sum(a.interest_sum) interest_sum ");
			sb.append("from sim_zqjd a ");
			sb.append("where a.state >= 2 ");
			sb.append("and isnull(a.dr,0) = 0 " );
			sb.append("and a.pk_assetsprop = '0001SE00000000000004' ");
			sb.append("and a.transtypecode = \'" + transtypecode + "\' ");
			sb.append("and trade_date between \'" + beginDate + "\' and \'" + endDate + "|| 23:59:59\'");
			sb.append("group by a.pk_securities");

			return sb.toString();
//			String sql = "select a.pk_securities,a.interest_sum from sim_zqjd a";
//			return sql;
		}
	/**
	 * @param meta
	 * @return
	 * ��Ԫ�����ֶ�����Ϊ��ֵ����
	 */
	public static MetaData setColType(MetaData meta){
		
		/*"start_bow_num", "start_sell_num", "start_debt", "now_bow",
		"now_rtn", "now_sell_num", "now_sell_sum", "now_buy_num",
		"now_buy_sum", "now_debt", "rec_interest", "turn_interest",
		"end_bow_num", "end_sell_num", "end_debt" };*/
		//����Ԫ�����ֶ����� java.sql.Types  public static final int DECIMAL = 3;
		meta.getField("start_bow_num").setDbColumnType(3);//1
		meta.getField("start_bow_num").setPrecision(100);
		
		meta.getField("start_sell_num").setDbColumnType(3);//2
		meta.getField("start_sell_num").setPrecision(100);
		
		meta.getField("start_debt").setDbColumnType(3);//3
		meta.getField("start_debt").setPrecision(100);
		
		meta.getField("now_bow").setDbColumnType(3);//4
		meta.getField("now_bow").setPrecision(100);
		
		meta.getField("now_rtn").setDbColumnType(3);//5
		meta.getField("now_rtn").setPrecision(100);
		
		meta.getField("now_sell_num").setDbColumnType(3);//6
		meta.getField("now_sell_num").setPrecision(100);
		
		meta.getField("now_sell_sum").setDbColumnType(3);//7
		meta.getField("now_sell_sum").setPrecision(100);
		
		meta.getField("now_buy_num").setDbColumnType(3);//8
		meta.getField("now_buy_num").setPrecision(100);
		
		meta.getField("now_buy_sum").setDbColumnType(3);//9
		meta.getField("now_buy_sum").setPrecision(100);
		
		meta.getField("now_debt").setDbColumnType(3);//10
		meta.getField("now_debt").setPrecision(100);
		
		meta.getField("rec_interest").setDbColumnType(3);//11
		meta.getField("rec_interest").setPrecision(100);
		
		meta.getField("turn_interest").setDbColumnType(3);//12
		meta.getField("turn_interest").setPrecision(100);
		
		meta.getField("end_bow_num").setDbColumnType(3);//13
		meta.getField("end_bow_num").setPrecision(100);
		
		meta.getField("end_sell_num").setDbColumnType(3);//14
		meta.getField("end_sell_num").setPrecision(100);
		
		meta.getField("end_debt").setDbColumnType(3);//15
		meta.getField("end_debt").setPrecision(100);
		
		return meta;
	}
	/**
	 * �����ֵ�ֶ� ������ĩ������
	 * @param lastResult
	 * @return
	 */
	public static List<NewStockChangeVO> fullWithZero(List<NewStockChangeVO> lastResult) {
		for (NewStockChangeVO scvo : lastResult) {
			
			UFDouble uf = new UFDouble(0);
			if(scvo.getStart_bow_num()==null){
				scvo.setStart_bow_num(uf);
			}
			if(scvo.getNow_bow()==null){
				scvo.setNow_bow(uf);
			}
			if(scvo.getNow_rtn()==null){
				scvo.setNow_rtn(uf);
			}
			if(scvo.getStart_sell_num()==null){
				scvo.setStart_sell_num(uf);
			}
			if(scvo.getNow_sell_num()==null){
				scvo.setNow_sell_num(uf);
			}
			if(scvo.getNow_buy_num()==null)
			{
				scvo.setNow_buy_num(uf);
			}
			if(scvo.getStart_debt()==null){
				scvo.setStart_debt(uf);
			}
			if(scvo.getNow_sell_sum()==null)
			{
				scvo.setNow_sell_sum(uf);
			}
			if(scvo.getNow_debt()==null){
				scvo.setNow_debt(uf);
			}
		}
		return lastResult;
		
	}

	
}
