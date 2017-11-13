package nc.vo.fba_sim.pub;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.fba_sim.pub.ISimPubStockInfoService;
import nc.vo.pub.lang.UFDouble;

/**
 * 取得总股本，最新收盘价和利率的函数
 * 
 * @author cwj
 * 
 */
public class SimINFOPubMethod {

	private static SimINFOPubMethod method = new SimINFOPubMethod();

	private Hashtable<String, Hashtable<String, UFDouble[]>> ht_captial = new Hashtable<String, Hashtable<String, UFDouble[]>>();

	private SimINFOPubMethod() {
		super();
	}

	public static SimINFOPubMethod getInstance() {
		return method;
	}

	// 取得单支券的收盘价
	public UFDouble getClosePrice(String trade_date, String pk_securities)
			throws Exception {
		ISimPubStockInfoService fairDistillService = (ISimPubStockInfoService) NCLocator
				.getInstance().lookup(ISimPubStockInfoService.class.getName());
		return fairDistillService.getStockClosePrice(trade_date, pk_securities);
	}

	// 取得总股本
	public UFDouble getCaptial(String trade_date, String pk_securities)
			throws Exception {
		if (trade_date == null || pk_securities == null) {
			return null;
		}
		if (!ht_captial.containsKey(trade_date)) {
			getHt_Captial(trade_date);
		}
		Hashtable<String, UFDouble[]> ht = ht_captial.get(trade_date);
		// if (!ht.containsKey(pk_securities)) {
		// getHt_Captial(trade_date);
		// }
		if (!ht.containsKey(pk_securities)) {
			return null;
		}
		return ht.get(pk_securities)[0];
	}

	// 取得流通盘
	public UFDouble getPassNum(String trade_date, String pk_securities)
			throws Exception {
		if (trade_date == null || pk_securities == null) {
			return null;
		}
		if (!ht_captial.containsKey(trade_date)) {
			getHt_Captial(trade_date);
		}
		Hashtable<String, UFDouble[]> ht = ht_captial.get(trade_date);
		return ht.get(pk_securities)[1];
	}

	private void getHt_Captial(String trade_date) throws Exception {
		ISimPubStockInfoService fairDistillService = (ISimPubStockInfoService) NCLocator
				.getInstance().lookup(ISimPubStockInfoService.class.getName());
		Hashtable<String, UFDouble[]> ht = fairDistillService
				.getCaptialHT(trade_date);
		if (ht != null) {
			ht_captial.put(trade_date, ht);
		}
	}

	public void removeCaptialHt(String trade_date) throws Exception {
		ht_captial.clear();
	}
}