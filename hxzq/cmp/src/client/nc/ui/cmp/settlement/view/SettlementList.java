package nc.ui.cmp.settlement.view;

import nc.bs.logging.Logger;
import nc.cmp.utils.CmpQueryModulesUtil;
import nc.cmp.utils.CmpUtils;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.cmp.itf.settlement.CmpUIEventType;
import nc.ui.cmp.itf.settlement.TabAddEventType;
import nc.ui.cmp.view.MutilTransBillListView;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.UIState;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import java.util.*;

/**
 * 需要注入nc.ui.cmp.settlement.model.SettleBillListPanelValueSetter
 * 
 * @author liuzz
 * 
 */
public class SettlementList extends MutilTransBillListView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7542183071810958465L;
	private boolean isinitRef = false;
	private Map<String, SettlementAggVO> settleaggMap = null;

	public void setSettleaggMap(Map<String, SettlementAggVO> settleaggMap) {
		this.settleaggMap = settleaggMap;
	}

	public Map<String, SettlementAggVO> getSettleAggMap() {
		if (settleaggMap == null) {
			settleaggMap = new HashMap<String, SettlementAggVO>();
		}
		return settleaggMap;
	}

	@Override
	public void showMeUp() {
		super.showMeUp();
		// 判断是否列表被选中
		getModel().fireEvent(new AppEvent(CmpUIEventType.LEAVE_CARD_TAB));
	}

	@Override
	public boolean canBeHidden() {
		boolean canBeHidden = super.canBeHidden();
		if (canBeHidden) {
			getModel().fireEvent(new AppEvent(CmpUIEventType.LEAVE_LIST_TAB));
		}
		return canBeHidden;
	}

	public SettlementAggVO[] getSelectedSettlementAggVOs() {
		List<SettlementAggVO> list = CmpUtils.makeList();
		for (int row = 0; row < this.getBillListPanel().getHeadBillModel()
				.getRowCount(); row++) {
			if (getBillListPanel().getHeadBillModel().getValueAt(row,
					"isselected") == null)
				continue;
			if (((Boolean) (getBillListPanel().getHeadBillModel().getValueAt(
					row, "isselected"))).booleanValue()) {
				String pk_settlement = (String) getBillListPanel()
						.getHeadBillModel().getValueAt(row, "pk_settlement");
				list.add(getSettleAggMap().get(pk_settlement));
			}
		}
		return list.toArray(new SettlementAggVO[0]);
	}

	@Override
	public void initUI() {

		super.initUI();
		try {
			initRef();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void handleEvent(AppEvent event) {
		try {
			if (event.getType().equals(TabAddEventType.BROW_BILL)) {

			} else if (event.getType().equals(TabAddEventType.EDIT_BILL)) {

			} else if (event.getType().equals(TabAddEventType.LOAD_NEW_TAB)) {

			} else if (event.getType().equals(TabAddEventType.LEAVE_TAB)) {

			}
			// 设置复选框 add by lihaibo
			if (getModel().getUiState() == UIState.EDIT
					|| getModel().getUiState() == UIState.ADD) {
				getBillListPanel().setMultiSelect(false);
				// getBillCardPanel().setBodyMultiSelect(false);
			} else if (getModel().getUiState() == UIState.NOT_EDIT) {
				// getBillCardPanel().setBodyMultiSelect(true);
				getBillListPanel().setMultiSelect(true);
			}
			super.handleEvent(event);
			initRef();
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	/**
	 * 初始化参照
	 * 
	 * @author wuzhwa
	 */
	private void initRef() throws BusinessException {
		if (CmpQueryModulesUtil.isFBMEnable(getModel().getContext()
				.getPk_group())) {
			// 初始化票据类型
			BillItem bi = getBillListPanel().getBodyBillModel().getItemByKey(
					SettlementBodyVO.NOTENUMBER);
			if (bi != null && bi.getComponent() != null
					&& bi.getComponent() instanceof UIRefPane) {
				UIRefPane noteRefPane = new UIRefPane();

				noteRefPane
						.setRefModel((AbstractRefModel) CmpUtils
								.reflectClass("nc.ui.fbm.pub.outerrefmodel.Bill4CmpArApReceiveRefModel"));
				noteRefPane.setButtonVisible(true);
				noteRefPane.setPk_org(getModel().getContext().getPk_org());
				bi.setComponent(noteRefPane);
			}
		}

		// BillItem tradetypeitem =
		// getBillListPanel().getHeadItem(SettlementHeadVO.PK_TRADETYPE);
		// if (tradetypeitem != null && tradetypeitem.getComponent() != null
		// && tradetypeitem.getComponent() instanceof UIRefPane) {
		//
		// for(int
		// i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
		// String pk_tradetype =
		// (String)this.getBillListPanel().getHeadBillModel().getValueAt(i,
		// SettlementHeadVO.TRADERTYPECODE);
		// BilltypeVO billtypeVO = PfDataCache.getBillType(pk_tradetype);
		// this.getBillListPanel().getHeadBillModel().setValueAt(VOUtil.getMultiLangText(billtypeVO,
		// "billtypename"), i, SettlementHeadVO.PK_TRADETYPE);
		// }
		//
		// }
		if (!isinitRef) {
			isinitRef = true;

			BillItem systemcodeitem = getBillListPanel().getBodyItem(
					SettlementBodyVO.SYSTEMCODE);
			if (systemcodeitem != null && systemcodeitem.getComponent() != null
					&& systemcodeitem.getComponent() instanceof UIRefPane) {
				UIRefPane syscodepanl = new UIRefPane();
				nc.ui.cmp.ref.SyscodeRefModel syscodemodel = new nc.ui.cmp.ref.SyscodeRefModel();
				syscodepanl.setRefModel(syscodemodel);
				syscodepanl.setName("来源系统"); /* -=notranslate=- */
				syscodepanl.setAutoCheck(true);
				systemcodeitem.setComponent(syscodepanl);

			}
		}

		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.uif2.editor.BillListView#handleSelectionChanged()
	 */
	@Override
	protected void handleSelectionChanged() {
		super.handleSelectionChanged();

		try {
			this.getBillListPanel()
					.getBodyBillModel()
					.execFormulas(
							new String[] { "notenumber->getColValue(fbm_register, fbmbillno, pk_register, notenumber)" });
		} catch (Exception e) {
		}
	}

	/**
	 * 加载数据
	 */
	public void loadNotenumber() {
		// 元数据需要设置为字符串格式
		Object obj = this.getModel().getSelectedData();
		if (obj != null) {
			SettlementAggVO aggvo = obj.getClass().isArray() ? ((SettlementAggVO[]) obj)[0]
					: (SettlementAggVO) obj;
			List<SettlementBodyVO> lstVO = CmpUtils.makeList();
			for (SettlementBodyVO settlementBodyVO : (SettlementBodyVO[]) aggvo
					.getChildrenVO()) {
				if (settlementBodyVO.getPK_notenumber() != null) {
					lstVO.add(settlementBodyVO);
				}
			}
			try {
				if (lstVO.size() == (aggvo).getChildrenVO().length) {
					this.getBillListPanel()
							.getBodyBillModel()
							.execFormulas(
									new String[] { "notenumber->getColValue(fbm_register, fbmbillno, pk_register, pk_notenumber)" });
				} else {
					if (lstVO.size() > 0) {

						this.getBillListPanel()
								.getBodyBillModel()
								.execFormulasWithVO(
										CmpUtils.covertListToArrays(lstVO,
												SettlementBodyVO.class),
										new String[] { "notenumber->getColValue(fbm_register, fbmbillno, pk_register, pk_notenumber)" });
						this.getModel().directlyUpdate(aggvo);
					}
				}
			} catch (Exception e) {
			}
		}
	}
}
