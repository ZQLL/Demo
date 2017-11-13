package nc.impl.fba_sim.costplugin.stock;

import java.util.Map;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.olap4j.impl.ArrayMap;

public class jisuanjdkcUtils {
	
public UFDouble[] jisuanfzqc(StockBalanceVO stvo){
	
	UFDouble lastnum = new UFDouble(0);
	UFDouble lastJE = new UFDouble(0);
	
	Map map1 = SearchZQJY(stvo);
	Map map2 = SearchZQZH(stvo);
	
	UFDouble MRMC =new UFDouble(map1.get("MRMC").toString()); //�����ծȯ��������������
	UFDouble ZRZC = new UFDouble(map2.get("ZRZC").toString()); //�����ծȯ��ת��ת������
	UFDouble MRMCJE = new UFDouble(map1.get("MRMCJE").toString()); //�����ծȯ������������
	UFDouble ZRZCJE = new UFDouble(map2.get("ZRZCJE").toString()); //�����ծȯ��ת��ת����
	//����������������ծȯת����������������
	if (!MRMC.equals(UFDouble.ZERO_DBL) || !ZRZC.equals(UFDouble.ZERO_DBL)) {
		lastnum = MRMC.add(ZRZC);
		lastJE = MRMCJE.add(ZRZCJE);
		 
	}
	
	return new UFDouble[]{lastnum,lastJE};
	
}	
	
	
	
	
	
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO ֤ȯ���׼�¼֤ȯ����������
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map SearchZQJY(StockBalanceVO stvo) {
		UFDouble MRMC = new UFDouble(0);
		UFDouble MRMCJE = new UFDouble(0);
		Map map = new ArrayMap();
		String pkcapa = stvo.getPk_capaccount();
		String pkglo = stvo.getPk_glorgbook();
		String pksec = stvo.getPk_securities();
		String pkorg = stvo.getPk_org();
		String pkgro = stvo.getPk_group();
		String pksto = stvo.getPk_stocksort();
		UFDate trade_date = stvo.getTrade_date();
		Vector vecmc = new Vector();
		Vector vecmr = new Vector();
		BaseDAO dao = new BaseDAO();
		// ���Ҽ�������֮ǰ������֤ȯ���׼�¼�����ȯ�Ľ�������   ��������� -zq
		String sql1 = "select sum(bargain_num),sum(bargain_sum) from sim_stocktrade where pk_securities ='"
				+ pksec + "' and pk_stocksort ='" + pksto + "' and pk_org ='"
				+ pkorg + "' and pk_group ='" + pkgro + "' and pk_glorgbook ='"
				+ pkglo + "' and pk_assetsprop ='" + "0001SE00000000000004"
				+ "' and pk_capaccount ='" + pkcapa
				+ "' and transtypecode = 'HV3A-0xx-22"
				+ "' and nvl(dr,0)=0 and trade_date <= '"
				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";
		
		String sql2 = "select sum(bargain_num),sum(sellcost) from sim_stocktrade where pk_securities ='"
				+ pksec + "' and pk_stocksort ='" + pksto + "' and pk_org ='"
				+ pkorg + "' and pk_group ='" + pkgro + "' and pk_glorgbook ='"
				+ pkglo + "' and pk_assetsprop ='" + "0001SE00000000000004"
				+ "' and pk_capaccount ='" + pkcapa
				+ "' and transtypecode = 'HV3A-0xx-21"
				+ "' and nvl(dr,0)=0 and trade_date <= '"
				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";
		
		try {
			vecmc =  (Vector) dao.executeQuery(sql1,
					new VectorProcessor());
			
			vecmr =  (Vector) dao.executeQuery(sql2,
					new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if(((Vector)vecmc.get(0)).get(0)!=null){
			MRMC = new UFDouble(((Vector)vecmc.get(0)).get(0).toString());
			MRMCJE = new UFDouble(((Vector)vecmc.get(0)).get(1).toString());
		}
		if(((Vector)vecmr.get(0)).get(0)!=null){
			MRMC = MRMC.sub(new UFDouble(((Vector)vecmr.get(0)).get(0).toString()));
			MRMCJE = MRMCJE.sub(new UFDouble(((Vector)vecmr.get(0)).get(1).toString()));
		}
		map.put("MRMC", MRMC);
		map.put("MRMCJE", MRMCJE);
		return map;

	}

	
	
	/*
	 * @author zq
	 * 
	 * @param StockBalanceVO �ʲ�ת��ծ,��ծת�ʲ���֤ȯת����¼
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map SearchZQZH(StockBalanceVO stvo) {
		UFDouble ZRZC = new UFDouble(0);
		UFDouble ZRZCJE = new UFDouble(0);
		Map map = new ArrayMap();
		String pkcapa = stvo.getPk_capaccount();
		String pkglo = stvo.getPk_glorgbook();
		String pksec = stvo.getPk_securities();
		String pkorg = stvo.getPk_org();
		String pkgro = stvo.getPk_group();
		String pksto = stvo.getPk_stocksort();
		UFDate trade_date = stvo.getTrade_date();
		Vector veczc = new Vector();
		Vector veczr = new Vector();
		BaseDAO dao = new BaseDAO();
		// �ʲ�ת��ծ���������-ת����;ת��֤ȯ�����ǵ�ǰ���ͣ�ת�����ʲ����ԣ�ת��Ϊ��ծ����;
		String sql1 = "select sum(bargain_num2),sum(bargain_sum2) " + "from sim_transformtrade "
				+ "where transtypecode = 'HV3F-0xx-01'" + " and nvl(dr,0)=0"
				+ " and state >= 2"
				+ " and hr_pk_assetsprop = '0001SE00000000000004'"
				+ " and hr_pk_capaccount = '" + pkcapa + "' and pk_glorgbook='"
				+ pkglo + "' and hc_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and pk_securities2 ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <='"
				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";
		
		String sql2 = "select sum(bargain_num),sum(bargain_sum) " + "from sim_transformtrade "
				+ "where transtypecode = 'HV3F-0xx-01'" + " and nvl(dr,0)=0"
				+ " and state >= 2"
				+ " and hr_pk_assetsprop in ('0001SE00000000000001','0001SE00000000000002','0001SE00000000000003')"
				+ " and hc_pk_capaccount = '" + pkcapa + "' and pk_glorgbook='"
				+ pkglo + "' and hc_pk_assetsprop = '0001SE00000000000004'"
				+ " and pk_securities ='" + pksec + "' and pk_org ='" + pkorg
				+ "' and pk_group ='" + pkgro + "' and hr_pk_stocksort = '"
				+ pksto + "' and trade_date <='"
				+ trade_date.toString().substring(0, 10) + " 23:59:59" + "'";

		try {
			veczr = (Vector) dao.executeQuery(sql1,
					new VectorProcessor());
			
			veczc = (Vector) dao.executeQuery(sql2,
					new VectorProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if(((Vector)veczc.get(0)).get(0)!=null){
			ZRZC = new UFDouble(((Vector)veczc.get(0)).get(0).toString());
			ZRZCJE = new UFDouble(((Vector)veczc.get(0)).get(1).toString());
		}
		if(((Vector)veczr.get(0)).get(0)!=null){
			ZRZC = ZRZCJE.sub(new UFDouble(((Vector)veczr.get(0)).get(0).toString()));
			ZRZCJE = ZRZCJE.sub(new UFDouble(((Vector)veczr.get(0)).get(1).toString()));
		}
		
		map.put("ZRZC", ZRZC);
		map.put("ZRZCJE", ZRZCJE);
		return map;

	}
}
