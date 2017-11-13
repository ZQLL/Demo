package nc.ui.cmp.settlement.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;

import nc.cmp.utils.CmpInterfaceProxy;
import nc.cmp.utils.CmpUtils;
import nc.itf.cmp.settlement.ISettlementService;
import nc.ui.cmp.settlement.view.SettlementCard;
import nc.ui.cmp.settlement.view.SettlementList;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.uif2.LoginContext;

public class SettleDefaultAction extends NCAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 2992839090639651936L;

	private LoginContext loginContext;
	private SettlementList listView;
	private SettlementCard edit;
	private AbstractAppModel model;
	private Container container;

	protected ISettlementService service = CmpInterfaceProxy.INSTANCE
			.getSettlementService();

	public Object getValue() {
		if (isListSelected()) {
			SettlementAggVO[] aggvos = null;
			// Object[] object = listView.getModel().getSelectedOperaDatas();
			//update by lihaibo
			Object[] object = listView.getBillListPanel().getMultiSelectedVOs(
					SettlementAggVO.class.getName(),
					SettlementHeadVO.class.getName(),
					SettlementBodyVO.class.getName());
//			if (object != null && object.length > 0) {
//				SettlementBodyVO[] bodyVOs = null;
//				String headPk = null;
//				for (SettlementAggVO aggVO : (SettlementAggVO[])object) {
//					bodyVOs = (SettlementBodyVO[]) aggVO.getChildrenVO();
//					if(bodyVOs == null) {
////						MessageDialog.showErrorDlg(getModel().getContext().getEntranceUI(), "错误",
////								"请选择表体数据！");
//						return null;
//					}
//				}
//			}
			if (object instanceof BillAggVO[]) {
				aggvos = (SettlementAggVO[]) object;
				if (aggvos != null && aggvos.length > 0) {
					SettlementBodyVO[] bodyVOs = null;
					String headPk = null;
					for (SettlementAggVO aggVO : aggvos) {
						try {
							headPk = aggVO.getParentVO().getPrimaryKey();
							bodyVOs = (SettlementBodyVO[]) HYPubBO_Client
									.queryByCondition(SettlementBodyVO.class,
											"pk_settlement = '"+headPk+"'");
							aggVO.setChildrenVO(bodyVOs);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if ((null != object && object.length > 0 ? object[0] instanceof SettlementAggVO
					: false)) {
				aggvos = new SettlementAggVO[object.length];
				
				System.arraycopy(object, 0, aggvos, 0, object.length);
				//update by lihaibo
				if (aggvos != null && aggvos.length > 0) {
					SettlementBodyVO[] bodyVOs = null;
					String headPk = null;
					for (SettlementAggVO aggVO : aggvos) {
						try {
							headPk = aggVO.getParentVO().getPrimaryKey();
							bodyVOs = (SettlementBodyVO[]) HYPubBO_Client
									.queryByCondition(SettlementBodyVO.class,
											"pk_settlement = '"+headPk+"'");
							aggVO.setChildrenVO(bodyVOs);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			return aggvos;
		}
		return edit.getValue();
	}

	public void setEdit(SettlementCard edit) {
		this.edit = edit;
	}

	public SettlementCard getEdit() {
		return edit;
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {

	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	/**
	 * @return the listView
	 */
	public SettlementList getListView() {
		return listView;
	}

	/**
	 * @param listView
	 *            the listView to set
	 */
	public void setListView(SettlementList listView) {
		this.listView = listView;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	/**
	 * @return the loginContext
	 */
	public LoginContext getLoginContext() {
		return loginContext;
	}

	/**
	 * @param loginContext
	 *            the loginContext to set
	 */
	public void setLoginContext(LoginContext loginContext) {
		this.loginContext = loginContext;
	}

	/**
	 * 是否显示列表界面
	 * 
	 * @return
	 */
	public boolean isListSelected() {

		if (listView != null && listView.isShowing()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 获取选择VOs的聚合aggvos
	 * 
	 * @return
	 */
	public SettlementAggVO[] getSelectedAggVOs() {
		Object[] value = null;
		if (isListSelected()) {
			//update by lihaibo
			value = (getListView()).getBillListPanel().getMultiSelectedVOs(
					SettlementAggVO.class.getName(),
					SettlementHeadVO.class.getName(),
					SettlementBodyVO.class.getName());
			
//			if (value != null && value.length > 0) {
//				SettlementBodyVO[] bodyVOs = null;
//				String headPk = null;
//				for (SettlementAggVO aggVO : (SettlementAggVO[])value) {
//					bodyVOs = (SettlementBodyVO[]) aggVO.getChildrenVO();
//					if(bodyVOs == null) {
//						return null;
//					}
//				}
//			}
			// value = (getListView()).getModel().getSelectedOperaDatas();
		} else {
			value = new Object[1];
			value[0] = getModel().getSelectedData();
		}

		if (null == value || value.length == 0) {
			return null;
		}
		SettlementAggVO[] aggs = new SettlementAggVO[value.length];
		System.arraycopy(value, 0, aggs, 0, aggs.length);
		if (!CmpUtils.isListNull(aggs)) {
			//update by lihaibo
			SettlementBodyVO[] bodyVOs = null;
			String headPk = null;
			for (SettlementAggVO aggVO : aggs) {
				try {
					headPk = aggVO.getParentVO().getPrimaryKey();
					bodyVOs = (SettlementBodyVO[]) HYPubBO_Client
							.queryByCondition(SettlementBodyVO.class,
									"pk_settlement = '"+ headPk +"'");
					aggVO.setChildrenVO(bodyVOs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return aggs;
		} else {
			return null;
		}
		// return aggs;
	}

	protected void checkData() throws BusinessException {
		if (CmpUtils.isListNull(this.getSelectedAggVOs())) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607set_0", "03607set-0016")/*
																			 * @res
																			 * "请选择操作行"
																			 */);
		}
	}
}