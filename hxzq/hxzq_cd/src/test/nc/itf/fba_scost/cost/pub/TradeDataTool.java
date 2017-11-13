package nc.itf.fba_scost.cost.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * @author YangJie
 * @since 2014-03-27
 * 
 */
public class TradeDataTool {

	private Map<String, List<IBill>> tradedatamap = null;

	private Map<String, List<ISuperVO>> bonusmap = new HashMap();// 证券分红方案数据
	private boolean isinit = false;

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public IBill[] getData(String key) {
		if (tradedatamap == null) {
			tradedatamap = new HashMap();
		}
		if (tradedatamap.get(key) != null)
			return tradedatamap.get(key).toArray(new IBill[] {});
		else
			return null;
	}

	/**
	 * 设置数据 此方法即为覆盖 一般情形下计算完毕后的更新数据调用此方法
	 * 
	 * @param key
	 */
	public void setData(String key, IBill[] ibills) {
		if (tradedatamap == null) {
			tradedatamap = new HashMap();
		}
		if (ibills == null) {
			tradedatamap.put(key, null);
		} else {
			tradedatamap.put(key, Arrays.asList(ibills));
		}
	}

	/**
	 * 按行增加数据 最初的初始化调用此方法
	 * 
	 * @param key
	 */
	public void addData(String key, IBill ibill) {
		if (tradedatamap == null) {
			tradedatamap = new HashMap();
			List<IBill> list = new ArrayList<IBill>();
			list.add(ibill);
			tradedatamap.put(key, list);
		} else {
			if (!tradedatamap.containsKey(key)) {
				List<IBill> list = new ArrayList<IBill>();
				list.add(ibill);
				tradedatamap.put(key, list);
			} else {
				List<IBill> list = tradedatamap.get(key);
				boolean flag = false;// 是否包含该数据
				for (IBill bill : list) {
					if (bill.getPrimaryKey().equals(ibill.getPrimaryKey())) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					list.add(ibill);
					tradedatamap.put(key, list);
				}
			}
		}

	}

	/**
	 * 增加数据 此方法即为增加 考虑到跨单据推单的情形 此部分需要考虑增加部分与更新部分区别
	 * 
	 * @param key
	 */
	public void addData(String key, IBill[] ibills) {
		if (tradedatamap == null) {
			tradedatamap = new HashMap();
			tradedatamap.put(key, Arrays.asList(ibills));

		} else {
			if (!tradedatamap.containsKey(key)) {
				tradedatamap.put(key, Arrays.asList(ibills));
			} else {
				tradedatamap.get(key).addAll(Arrays.asList(ibills));
			}

		}

	}

	public void clearcache() {
		if (tradedatamap != null) {
			tradedatamap.clear();
			tradedatamap = null;
		}

	}

	public boolean isIsinit() {
		return isinit;
	}

	public void setIsinit(boolean isinit) {
		this.isinit = isinit;
	}

	public Map<String, List<ISuperVO>> getBonusmap() {
		return bonusmap;
	}

	/**
	 * key 日期 10位 value 当日的证券分红方案
	 * 
	 * @param bonusmap
	 */
	public void setBonusmap(ISuperVO[] vos) {
		for (int i = 0; i < vos.length; i++) {
			ISuperVO iSuperVO = vos[i];
			UFDate date = (UFDate) iSuperVO.getAttributeValue("regdate");
			if (date != null && !bonusmap.containsKey(date.toLocalString())) {
				List<ISuperVO> list = new ArrayList();
				list.add(iSuperVO);
				bonusmap.put(date.toLocalString(), list);
			} else if (date != null && bonusmap.containsKey(date.toLocalString())) {
				bonusmap.get(date.toLocalString()).add(iSuperVO);
			}

		}
	}
	public void remove(String key) {
		if (tradedatamap != null) {
			tradedatamap.remove(key);
		}
	}

}
