package nc.vo.fba_scost.cost.interest;

import nc.vo.trade.pub.HYBillVO;

public class AggInterest extends HYBillVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7840333829862445386L;

	private Interest parent;
	
	private Rateperiod[] childrenvos;
	
	public Interest getParent() {
		return parent;
	}

	public void setParent(Interest parent) {
		this.parent = parent;
	}

	public void setChildrenVO(Rateperiod[] children) {
		this.childrenvos = children;
	}
	
	public Rateperiod[] getChildrenVO(){
		return childrenvos;
	}

}
