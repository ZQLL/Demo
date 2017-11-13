package nc.ui.fba_sim.simbs.interest.action;

import java.awt.event.ActionEvent;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.actions.SaveAction;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.IEditor;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.fba_sim.simbs.interest.AggInterest;
import nc.vo.fba_sim.simbs.interest.Interest;
import nc.vo.fba_sim.simbs.interest.InterestControl;
import nc.vo.fba_sim.simbs.interest.Rateperiod;
import nc.vo.pub.BusinessException;

public class InterestSaveAction extends SaveAction {
	private static final long serialVersionUID = -9180200857014900410L;

	public void doAction(ActionEvent e) throws Exception {
		if ((UIState.ADD == getModel().getUiState()) || (isPmInfoChange())) {
			validate(getEditor().getValue());
			AggInterest aggvo = (AggInterest) getEditor().getValue();
			InterestControl control = new InterestControl();
			Interest interest = aggvo.getParentVO();
			control.check(interest);
			Rateperiod[] bodyvos = control.calcRateperiodInfo(interest, true);
			aggvo.setChildren(Rateperiod.class, bodyvos);
			getEditor().setValue(aggvo);
		}
		super.doAction(e);
	}

	private boolean isPmInfoChange() throws BusinessException {
		AggInterest aggvo = (AggInterest) getEditor().getValue();
		Interest newObj = aggvo.getParentVO();
		if (newObj.getPk_interest() == null) {
			return true;
		}
		IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class);
		Interest oldObj = (Interest) queryBS.retrieveByPK(Interest.class,
				newObj.getPk_interest());
		if (oldObj == null) {
			return true;
		}
		if (getEditor() == null) {
			return false;
		}
		BillItem[] showItems = ((BillForm) getEditor()).getBillCardPanel()
				.getHeadShowItems();
		for (BillItem billItem : showItems) {
			Object newVal = newObj.getAttributeValue(billItem.getKey());

			Object oldVal = oldObj.getAttributeValue(billItem.getKey());
			if (billItem.getKey().equals("vdef1")) {
				if (newVal == null && oldVal == null) {
					return false;
				}
				if (newVal == null && oldVal != null) {
					return true;
				}
				if (newVal != null && oldVal == null) {
					return true;
				}
			}
			if (!newVal.equals(oldVal)) {
				return true;
			}
		}
		return false;
	}
}
