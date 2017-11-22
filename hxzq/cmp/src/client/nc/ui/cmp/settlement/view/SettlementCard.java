package nc.ui.cmp.settlement.view;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.cmp.bill.util.SysInit;
import nc.cmp.utils.AccountProxy;
import nc.cmp.utils.CMPFactory;
import nc.cmp.utils.CmpQueryModulesUtil;
import nc.cmp.utils.CmpUtils;
import nc.cmp.utils.DataUtil;
import nc.cmp.utils.SettleAlgorithm;
import nc.cmp.utils.SettleConvert;
import nc.cmp.utils.SettleUtils;
import nc.cmp.utils.UFDoubleUtils;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.bd.psn.psndoc.IPsndocQueryService;
import nc.itf.cm.prv.CmpConst;
import nc.itf.org.IDeptQryService;
import nc.pubitf.cmp.note.INoteEBLyPubManageService;
import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.pubitf.uapbd.ICustomerPubService;
import nc.pubitf.uapbd.ISupplierPubService_C;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.cmp.bill.commom.TabEventContext;
import nc.ui.cmp.itf.settlement.CmpUIEventType;
import nc.ui.cmp.itf.settlement.TabAddEventType;
import nc.ui.cmp.pub.SummaryBillCellEditor;
import nc.ui.cmp.ref.CMPTradeTypeRefModel;
import nc.ui.cmp.settlement.model.SettleDataManager;
import nc.ui.cmp.settlement.model.SettleModel;
import nc.ui.cmp.settlement.utils.InfoFetcherCache;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.view.MutilTransBillForm;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.ui.tmpub.event.FireEventManager;
import nc.ui.tmpub.filter.DefaultRefWherePartHandler;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.cmp.CMPSysParamConst;
import nc.vo.cmp.SettleStatus;
import nc.vo.cmp.exception.ExceptionHandler;
import nc.vo.cmp.settlement.CmpMsg;
import nc.vo.cmp.settlement.SettleEnumCollection;
import nc.vo.cmp.settlement.SettleEnumCollection.Direction;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.org.DeptVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tmpub.util.StringUtil;
import nc.vo.tmpub.util.VOUtil;
import nc.vo.uif2.LoginContext;
import java.util.*;
@SuppressWarnings({ "restriction", "deprecation" })
public class SettlementCard extends MutilTransBillForm {

	/**
	 * @return the clientSigner
	 */
	public nc.ui.tmpub.security.DefCommonClientSign getClientSigner() {
		return clientSigner;
	}

	/**
	 * @param clientSigner
	 *            the clientSigner to set
	 */
	public void setClientSigner(
			nc.ui.tmpub.security.DefCommonClientSign clientSigner) {
		this.clientSigner = clientSigner;
	}

	private boolean isinitRef = false;
	private static final long serialVersionUID = 5788263787314411879L;
	private SettleDataManager dataModel;

	private Map<String, List<String>> itemMap;

	private CmpMsg msg;

	private SettlementAggVO showBean;

	private DefaultRefWherePartHandler defaultRefWherePartHandler;
	private nc.ui.tmpub.security.DefCommonClientSign clientSigner;
	private nc.ui.pubapp.uif2app.model.AppEventHandlerMediator digitMediator;

	public DefaultRefWherePartHandler getDefaultRefWherePartHandler() {
		return defaultRefWherePartHandler;
	}

	public void setDefaultRefWherePartHandler(
			DefaultRefWherePartHandler defaultRefWherePartHandler) {
		this.defaultRefWherePartHandler = defaultRefWherePartHandler;
	}

	private boolean isSaveOperate = false;
	// 被业务页签加载的时候缓存数据
	private SettlementAggVO loadBean;
	private AccountProxy proxy;

	public SettlementCard() {
		super();
	}

	
	@Override
	public void initUI() {

		super.initUI();
		try {
			initRef();
		} catch (BusinessException e) {
			ExceptionHandler.consume(e);
		}
		// 禁止右键菜单操作，为防止列表的右键行菜单操作
		this.getBillCardPanel().setBodyMenuShow(false);
	}

	private void initRef() throws BusinessException {
		// 初始化一次的

		BillItem orgBillItem = this.getBillCardPanel().getHeadItem(
				SettlementHeadVO.PK_ORG);
		Object pk_org = orgBillItem.getValueObject();

		BillItem groupBillItem = this.getBillCardPanel().getHeadItem(
				SettlementHeadVO.PK_GROUP);
		Object pk_group = groupBillItem.getValueObject();
		if (!isinitRef) {
			isinitRef = true;
			BillItem tradetypeitem = getBillCardPanel().getHeadItem(
					SettlementHeadVO.PK_TRADETYPE);
			if (tradetypeitem != null && tradetypeitem.getComponent() != null
					&& tradetypeitem.getComponent() instanceof UIRefPane) {
				UIRefPane typePane = ((UIRefPane) tradetypeitem.getComponent());
				CMPTradeTypeRefModel typemodel = new CMPTradeTypeRefModel();
				typemodel.setWherePart("pk_group='"
						+ DataUtil.getCurrentGroup() + "'");
				typemodel.setMatchPkWithWherePart(true);
				typePane.setRefModel(typemodel);
				typePane.setAutoCheck(true);
				typePane.setIsBatchData(false);
				typePane.setMultiSelectedEnabled(false);

			}
		}

		if (this.getValue() == null
				|| ((AggregatedValueObject) (this.getValue())).getParentVO() == null) {
			return;
		}
		SettlementAggVO aggvo = (SettlementAggVO) this.getValue();

		SettlementHeadVO headvo = (SettlementHeadVO) aggvo.getParentVO();
		//
		// // 内部结算账户
		// BillItem inneraccountitem =
		// getBillCardPanel().getBodyItem(SettlementBodyVO.PK_INNERACCOUNT);
		// if (!isinitRef) {
		// if (inneraccountitem != null && inneraccountitem.getComponent() !=
		// null) {
		//
		// UIRefPane inneraccountRef = ((UIRefPane)
		// inneraccountitem.getComponent());
		// // inneraccountRef.setPk_org(headvo.getPk_org());
		// // inneraccountRef.getRefModel().setPk_org(headvo.getPk_org());
		//
		// // 设置过滤
		// // StringBuffer orgSQL = new StringBuffer();
		// // String group =
		// WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
		// // if (!StringUtil.isNull(group)) {
		// //
		// orgSQL.append(" and bd_accid.pk_group='").append(group).append("' ");
		// // }
		// // String[] orgs =
		// getModel().getContext().getFuncInfo().getFuncPermissionPkorgs();
		// // if (orgs != null && orgs.length > 0) {
		// //
		// //
		// orgSQL.append(" and ").append(SqlUtil.buildSqlForIn("bd_accid.pk_org",
		// orgs)).append(" ");
		// //
		// orgSQL.append(" and ").append(SqlUtil.buildSqlForIn("bd_accid.pk_ownerorg",
		// orgs)).append(" ");
		// // } else {
		// // orgSQL.append(" and 1=2 ");
		// // }
		// inneraccountRef.getRefModel().setEnvWherePart(" bd_accid.pk_ownerorg='"+pk_org+"' or bd_accid.pk_org ='"+pk_org+"' and pk_currtype='"+pk_currtype+"'");
		// // inneraccountRef.getRefModel().addWherePart(orgSQL.toString());
		// inneraccountRef.setMultiCorpRef(true);
		//
		// }
		// }
		// 现金账户 范围
		BillItem cashaccountitem = getBillCardPanel().getBodyItem(
				SettlementBodyVO.PK_CASHACCOUNT);
		if (cashaccountitem != null && cashaccountitem.getComponent() != null) {
			UIRefPane cashaccountRef = ((UIRefPane) cashaccountitem
					.getComponent());
			AbstractRefModel model = cashaccountRef.getRefModel();
			if (model != null && orgBillItem != null) {
				if (pk_org != null) {
					model.setPk_org(orgBillItem.getValueObject().toString());
				}
			}
		}
		setNotenumberItem(headvo);

		// 部门 范围
		BillItem deptitem = getBillCardPanel().getBodyItem(
				SettlementBodyVO.PK_DEPTDOC);
		if (deptitem != null && deptitem.getComponent() != null) {
			UIRefPane deptref = ((UIRefPane) deptitem.getComponent());
			AbstractRefModel model = deptref.getRefModel();

			if (model != null && orgBillItem != null && groupBillItem != null) {
				if (pk_org != null) {
					model.setPk_org(pk_org.toString());
				}
				if (pk_group != null) {
					model.setPk_group(pk_group.toString());

				}
			}

		}
		// 人员 范围
		BillItem psnitem = getBillCardPanel().getBodyItem(
				SettlementBodyVO.PK_PSNDOC);
		if (psnitem != null && psnitem.getComponent() != null) {
			UIRefPane psnref = ((UIRefPane) psnitem.getComponent());
			AbstractRefModel model = psnref.getRefModel();

			if (model != null && orgBillItem != null) {
				if (pk_org != null) {
					model.setPk_org(orgBillItem.getValueObject().toString());
				}
				if (pk_group != null) {
					model.setPk_group(pk_group.toString());
				}
			}
		}

		setRealPayInfoStatus();
		setItemStatus();

		loadOppInfo();

		BillPanelUtils.setOrgForAllRef(this.getBillCardPanel(), this.getModel()
				.getContext());

		isinitRef = true;
	}

	private void setItemStatus() {
		// 禁止结算单修改对方信息
		setBodyItemEnable(SettlementBodyVO.ACCOUNTNUM, UFBoolean.FALSE);
		setBodyItemEnable(SettlementBodyVO.OPPACCOUNT, UFBoolean.FALSE);
		setBodyItemEnable(SettlementBodyVO.ACCOUNTTYPE, UFBoolean.FALSE);
		setBodyItemEnable(SettlementBodyVO.PK_OPPACCOUNT, UFBoolean.FALSE);
		setBodyItemEnable(SettlementBodyVO.OPPBANK, UFBoolean.FALSE);
		setBodyItemEnable(SettlementBodyVO.OPPACCNAME, UFBoolean.FALSE);
	}

	@Override
	
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if (getDefaultRefWherePartHandler() != null) {
			getDefaultRefWherePartHandler().fireFilter(e.getKey(), e.getPos(),
					e.getTableCode());
		}
		if (e.getKey().equals(SettlementBodyVO.NOTENUMBER)) {
			SettlementBodyVO bodyVO = (SettlementBodyVO) getBillCardPanel()
					.getBillModel().getBodyValueRowVO(e.getRow(),
							SettlementBodyVO.class.getName());
			if (UFBoolean.TRUE.equals(getCMP52Para(bodyVO.getPk_org()))) {
				if (e.getOldValue() != null && e.getValue() == null) {
					// 空白票据清空，国际化自动带入的银行对账标识码需要清空
					bodyVO.setBankrelated_code(null);
					getBillCardPanel().getBillModel().setBodyRowVO(bodyVO,
							e.getRow());
				}
			}
			BillItem bi = getBillCardPanel().getBodyItem(
					SettlementBodyVO.NOTENUMBER);
			if (bi.getComponent() != null
					&& bi.getComponent() instanceof UIRefPane) {
				UIRefPane noteRefPane = (UIRefPane) bi.getComponent();
				if (noteRefPane != null && noteRefPane.getRefModel() != null) {
					setValue(noteRefPane.getRefModel().getPkValue(),
							e.getRow(), SettlementBodyVO.PK_NOTENUMBER);
				}
			} else {
				setValue(null, e.getRow(), SettlementBodyVO.PK_NOTENUMBER);

			}

		}

		if (e.getPos() == 1) {
			try {
				setItemMap(getChangedItems(e, e.getRow()));
			} catch (BusinessException e1) {
				setValue(null, e.getRow(), SettlementBodyVO.NOTENUMBER);
				setValue(null, e.getRow(), SettlementBodyVO.PK_NOTENUMBER);
				return;
			}
		}
		try {
			if (e.getKey().equals(SettlementBodyVO.PK_OPPACCOUNT)) {

				// setOppBankInfo(e.getRow());

			} else if (e.getKey().equals(SettlementBodyVO.PK_ACCOUNT)) {

				setAccountInfo(e.getRow());

			} else if (e.getKey().equals(SettlementBodyVO.PK_CURRTYPE_LAST)) {// 实际结算支付信息处理。
				// 支持手工修改套汇汇率及金额，系统自动互相倒算。
				setRealPayInfo(e);
			} else if (e.getKey().equals(SettlementBodyVO.PAY)
					|| e.getKey().equals(SettlementBodyVO.PAY_LAST)
					|| e.getKey().equals(SettlementBodyVO.CHANGERATE)) {
				setRealPayInfo_money(e);
			} else if (e.getKey().equals(SettlementBodyVO.PAY_LAST)) {
				SettlementBodyVO bodyVO = (SettlementBodyVO) getBillCardPanel()
						.getBillModel().getBodyValueRowVO(e.getRow(),
								SettlementBodyVO.class.getName());
				BillItem billdateItem = getBillCardPanel().getHeadItem(
						SettlementHeadVO.BUSI_BILLDATE);
				if (billdateItem == null) {
					return;
				}

				UFDate billdate = new UFDate(billdateItem.getValue()); // 单据日期
				SettleUtils.setRealPayInfo(bodyVO, billdate);
				getBillCardPanel().getBillModel().setBodyRowVO(bodyVO,
						e.getRow());
			}
			/*
			 * 根据主组织、币种、银行账号和票据类型和已经使用的空白票据号， 查出一张未领用的空白票据号，设置到界面上 modify by
			 * aishm 2013-04-25
			 */
			else if (e.getKey().equals(SettlementBodyVO.PK_NOTETYPE)) {
				doBlanknote_no(e.getRow());
			}

		} catch (BusinessException e1) {
			ShowStatusBarMsgUtil.showErrorMsg(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"3607set_0", "03607set-0061")/* @res "错误" */,
					e1.getMessage(), getModel().getContext());
		}

	}

	/**
	 * 设置票据号
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	private void setNotenumberItem(SettlementHeadVO headvo)
			throws BusinessException {
		// 元数据需要设置为字符串格式
		if (CmpQueryModulesUtil.isFBMEnable(getModel().getContext()
				.getPk_group())) {

			BillItem bi = getBillCardPanel().getBodyItem(
					SettlementBodyVO.NOTENUMBER);
			if (bi != null && bi.getComponent() != null
					&& bi.getComponent() instanceof UIRefPane) {
				UIRefPane noteRefPane = new UIRefPane();
				if (headvo.getDirection() != null
						&& headvo.getDirection().equals(Direction.REC.VALUE)) {
					noteRefPane
							.setRefModel((AbstractRefModel) CmpUtils
									.reflectClass("nc.ui.fbm.pub.outerrefmodel.Bill4CmpArApReceiveRefModel"));

				} else {
					noteRefPane
							.setRefModel((AbstractRefModel) CmpUtils
									.reflectClass("nc.ui.fbm.pub.outerrefmodel.Bill4CmpArApPayRefModel"));
				}
				noteRefPane.setAutoCheck(false);
				noteRefPane.setButtonVisible(true);
				noteRefPane.setPk_org(getModel().getContext().getPk_org());
				noteRefPane.getRefModel().setPk_org(
						getModel().getContext().getPk_org());
				//
				//
				int explaModelIndex = getBillCardPanel().getBillModel()
						.getBodyColByKey(SettlementBodyVO.NOTENUMBER);
				int explaColIndex = getBillCardPanel().getBillTable()
						.convertColumnIndexToView(explaModelIndex);

				if (explaColIndex > -1) {
					getBillCardPanel()
							.getBillTable()
							.getColumnModel()
							.getColumn(explaColIndex)
							.setCellEditor(
									new SummaryBillCellEditor(noteRefPane));
				}

				bi.setComponent(noteRefPane);
			}
		}
	}

	/**
	 * 需要记录以前编辑过的
	 */
	public Map<String, List<String>> getChangedItems(BillEditEvent e,
			int rowFromE) throws BusinessException {

		String key = e.getKey();
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		row = row < 0 ? rowFromE : row;
		SettlementBodyVO body = (SettlementBodyVO) getBillCardPanel()
				.getBillModel("items").getBodyValueRowVO(row,
						SettlementBodyVO.class.getName());
		String pk_billdetail = body.getPk_billdetail();
		Integer rowkey = body.getBusilineno();
		if (!body.getModifyflag().equalsIgnoreCase(CmpConst.MODIFY_FLAG)) {
			setItemMap(SettleConvert.INSTANCE
					.convert(new SettlementBodyVO[] { body }));
		} else {
			setItemMap(null);
		}

		if (key.equals(SettlementBodyVO.PK_ACCOUNT)) {
			DefaultConstEnum old = null;
			if (e.getOldValue() != null) {
				old = new DefaultConstEnum(e.getOldValue().toString(), e
						.getOldValue().toString());
			}
			// DefaultConstEnum old = new
			// DefaultConstEnum(e.getOldValue(),e.getOldValue().toString());
			String oldValue = null;
			String value = null;
			if (old != null) {
				oldValue = (String) old.getValue();
			}
			DefaultConstEnum newvalue = null;
			if (e.getValue() != null) {
				newvalue = new DefaultConstEnum(e.getValue().toString(), e
						.getValue().toString());
			}

			if (newvalue != null) {
				value = (String) newvalue.getValue();
			}
			if (oldValue != null && !oldValue.equals(value)
					|| (oldValue == null && value != null)) {
				rowkey = rowkey == null ? row : rowkey;
				List<String> itemList = getItemMap().get(pk_billdetail);
				if (itemList == null) {
					itemList = new ArrayList<String>();
					getItemMap().put(pk_billdetail, itemList);
				}
				if (!itemList.contains(SettlementBodyVO.PK_ACCOUNT)) {
					itemList.add(SettlementBodyVO.PK_ACCOUNT);
				}
				if (!itemList.contains(SettlementBodyVO.PK_BANK)) {
					itemList.add(SettlementBodyVO.PK_BANK);
				}

				if (this.getValue(row, SettlementBodyVO.BUSILINENO) == null) {
					this.setValue(row, row, SettlementBodyVO.BUSILINENO);
				}
				recordFieldVauleChange(e, getItemMap(), "pk_cashaccount");
				setBankInfo(rowFromE);
			}
		} else if (key.equals("pk_cashaccount")) {
			recordFieldVauleChange(e, getItemMap(), key);
			recordFieldVauleChange(e, getItemMap(), SettlementBodyVO.PK_ACCOUNT);
			recordFieldVauleChange(e, getItemMap(), SettlementBodyVO.PK_BANK);

		} else if (key.equals("oppaccount")) {
			try {
				DefaultConstEnum old = null;
				if (e.getOldValue() != null) {
					old = new DefaultConstEnum(e.getOldValue().toString(), e
							.getOldValue().toString());
				}
				// DefaultConstEnum old = new
				// DefaultConstEnum(e.getOldValue(),e.getOldValue().toString());
				DefaultConstEnum newvalue = null;
				if (e.getOldValue() != null) {
					newvalue = new DefaultConstEnum(e.getValue().toString(), e
							.getValue().toString());
				}
				// DefaultConstEnum newvalue = new
				// DefaultConstEnum(e.getValue(),e.getValue().toString());
				// if(old == null )
				if ((old == null && newvalue != null) || !old.equals(newvalue)) {
					List<String> itemList = getItemMap().get(pk_billdetail);
					if (itemList == null) {
						itemList = new ArrayList<String>();
						getItemMap().put(pk_billdetail, itemList);
					}
					if (!itemList.contains("pk_oppaccount")) {
						itemList.add("pk_oppaccount");
					}
					if (!itemList.contains("pk_oppbank")) {
						itemList.add("pk_oppbank");
					}
					setOppBankInfo(rowFromE);
					if (this.getValue(row, "rowkey") == null) {
						this.setValue(row, row, "rowkey");
					}
				}
			} catch (Exception e1) {
				ExceptionHandler.consume(e1);
			}
		} else if (CmpConst.REVERSE_MAPPING_MAP.containsKey(key)) {
			recordFieldVauleChange(e, getItemMap(), key);
		}

		SettleConvert.INSTANCE.convertReverse(getItemMap(),
				new SettlementBodyVO[] { body });
		getBillCardPanel().getBillModel("items").setValueAt(
				body.getModifyflag(), row, SettlementBodyVO.MODIFYFLAG);
		return getItemMap();
	}

	private void recordFieldVauleChange(BillEditEvent e,
			Map<String, List<String>> fieldMap, String fieldname) {
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		row = row < 0 ? e.getRow() : row;
		SettlementBodyVO body = (SettlementBodyVO) getBillCardPanel()
				.getBillModel("items").getBodyValueRowVO(row,
						SettlementBodyVO.class.getName());
		String pk_billdetail = body.getPk_billdetail();
		Integer rowkey = body.getBusilineno();
		DefaultConstEnum old = null;
		if (e.getOldValue() != null) {
			old = new DefaultConstEnum(e.getOldValue().toString(), e
					.getOldValue().toString());
		}
		String oldValue = null;
		String value = null;
		if (old != null) {
			Object oldObj = old.getValue();
			if (oldObj instanceof Integer) {
				oldObj = oldObj == null ? null : oldObj.toString();
			}
			oldValue = (String) oldObj == null ? (String) old.getName()
					: (String) oldObj;
		}
		if (e.getValue() == null) {
			value = null;
		} else if (e.getValue() instanceof DefaultConstEnum) {
			DefaultConstEnum newvalue = (DefaultConstEnum) e.getValue();
			if (newvalue != null) {
				Object newObj = newvalue.getValue();
				newObj = newObj == null ? null : newObj.toString();
				value = (String) newObj == null ? (String) newvalue.getName()
						: (String) newObj;
			}
		} else if (e.getValue() instanceof String) {
			value = e.getValue() == null ? null : (String) e.getValue();
		}
		if (oldValue != null && !oldValue.equals(value)
				|| (oldValue == null && value != null)) {
			rowkey = rowkey == null ? row : rowkey;
			List<String> itemList = getItemMap().get(pk_billdetail);
			if (itemList == null) {
				itemList = new ArrayList<String>();
				getItemMap().put(pk_billdetail, itemList);
			}
			if (!itemList.contains(fieldname)) {
				itemList.add(fieldname);
			}
			if (this.getValue(row, SettlementBodyVO.BUSILINENO) == null) {
				this.setValue(row, row, SettlementBodyVO.BUSILINENO);
			}
		}
	}

	
	@Override
	public void handleEvent(AppEvent event) {
		try {
			if (event.getType().equals(TabAddEventType.BROW_BILL)) {

				handleBrowBill((TabEventContext) event.getContextObject());
				setComponentVisible(true);
				getModel().setUiState(UIState.NOT_EDIT);
				loadNotenumber();
			} else if (event.getType().equals(TabAddEventType.EDIT_BILL)) {

				handleditBillEvent((TabEventContext) event.getContextObject());
				((SettleModel) getModel()).setBeLoaded(true);
				setOrgForAllRef();
				loadNotenumber();
				this.getBillCardPanel().setBodyMenuShow(false);
				this.setEditable(true);
			} else if (event.getType().equals(TabAddEventType.LOAD_NEW_TAB)) {
				// 添加业务页签监听是为了通知业务页签的刷新
				getModel().addAppEventListener(
						(AppEventListener) event.getSource());
				getModel().addAppEventListener(
						(AppEventListener) event.getContextObject());
				((SettleModel) getModel()).setBeLoaded(true);
			} else if (event.getType().equals(TabAddEventType.LEAVE_TAB)) {
				if (getModel().getUiState().equals(UIState.EDIT)) {
					throw new BusinessException("111");
				}
			}

			super.handleEvent(event);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	/**
	 * 加载数据
	 */
	public void loadNotenumber() {
		// 元数据需要设置为字符串格式
		Object obj = this.getValue();
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
			if (lstVO.size() == (aggvo).getChildrenVO().length) {
				this.getBillCardPanel()
						.getBillModel()
						.execFormulas(
								new String[] { "notenumber->getColValue(fbm_register, fbmbillno, pk_register, pk_notenumber)" });
			} else {
				if (lstVO.size() > 0) {

					this.getBillCardPanel()
							.getBillModel()
							.execFormulasWithVO(
									CmpUtils.covertListToArrays(lstVO,
											SettlementBodyVO.class),
									new String[] { "notenumber->getColValue(fbm_register, fbmbillno, pk_register, pk_notenumber)" });
					this.getModel().directlyUpdate(aggvo);
				}
			}
		}
	}

	public void loadOppInfo() {

		try {
			// 元数据需要设置为字符串格式
			Object obj = this.getValue();
			if (obj != null) {
				SettlementAggVO aggvo = obj.getClass().isArray() ? ((SettlementAggVO[]) obj)[0]
						: (SettlementAggVO) obj;
				List<SettlementBodyVO> lstVO = CmpUtils.makeList();
				for (SettlementBodyVO settlementBodyVO : (SettlementBodyVO[]) aggvo
						.getChildrenVO()) {
					if (settlementBodyVO.getPk_oppaccount() != null) {
						lstVO.add(settlementBodyVO);
					}
				}
				if (lstVO.size() == (aggvo).getChildrenVO().length) {
					// this.getBillCardPanel().getBillModel().execFormulas(
					// new String[] {
					// "oppaccount->getcolvalue(bd_bankaccbas ,accnum ,pk_bankaccbas ,getcolvalue(bd_bankaccsub ,pk_bankaccbas ,pk_bankaccsub ,pk_oppaccount))"
					// });
				} else {
					if (lstVO.size() > 0) {

						// this.getBillCardPanel().getBillModel().execFormulasWithVO(CmpUtils.covertListToArrays(lstVO,
						// SettlementBodyVO.class),
						// new String[] {
						// "oppaccount->getcolvalue(bd_bankaccbas ,accnum ,pk_bankaccbas ,getcolvalue(bd_bankaccsub ,pk_bankaccbas ,pk_bankaccsub ,pk_oppaccount))"
						// });
						// this.getModel().directlyUpdate(aggvo);
					}
				}
			}
		} catch (Exception e) {
			ExceptionHandler.consume(e);
		}
	}

	/**
	 * 
	 */
	private void setOrgForAllRef() {
		if (showBean == null || showBean.getParentVO() == null) {
			return;
		}
		SettlementHeadVO settlementHeadVO = (SettlementHeadVO) showBean
				.getParentVO();
		LoginContext context = new LoginContext();
		context.setPk_loginUser(WorkbenchEnvironment.getInstance()
				.getLoginUser().getPrimaryKey());
		context.setPk_group(settlementHeadVO.getPk_group());
		context.setPk_org(settlementHeadVO.getPk_org());
		BillPanelUtils.setOrgForAllRef(getBillCardPanel(), context);

	}

	private void handleditBillEvent(TabEventContext contextObject)
			throws BusinessException {

		showBean = dataModel.queryByBillId(contextObject.getMsg().getBillkey());
		SettlementAggVO oldsettleAgg = (SettlementAggVO) contextObject
				.getSettleInfo();
		msg = contextObject.getMsg();
		SettlementBodyVO[] metas = (SettlementBodyVO[]) InfoFetcherCache.INSTANCE
				.getSettleMetas(msg.getBilltype(), contextObject.getBean());
		SettleUtils.convertZeroToNull(metas);
		// maji_TODO 此处需要调整
		if (oldsettleAgg != null
				|| msg.getOperateStatus() != SettleEnumCollection.OperateType.COPY.VALUE) {
			SettlementBodyVO[] newsettleBodys = metas;
			getChangeTabSettlementAgg(oldsettleAgg, msg, newsettleBodys);
			doEdit(metas, oldsettleAgg);
		} else {
			SettlementBodyVO[] bodys = dataModel.findBodysByCopyMap(msg
					.getCopyMap());
			if (bodys == null) {
				showBean = null;
				doEdit(metas, oldsettleAgg);
			} else {
				showBean.setChildrenVO(bodys);
				doEdit(metas, oldsettleAgg);
			}
		}
		setLoadBean(showBean);
		setValue(showBean);

	}

	/**
	 * 业务场景：新增状态下 业务页签 切 结算页签 切 业务页签 切 结算页签 结算属性不需要改变；业务属性，如承付等需要时刻同步改变
	 * 主要作用是将更改后的业务属性的改变同步到缓存的结算VO中
	 * */
	private void getChangeTabSettlementAgg(SettlementAggVO oldsettleAgg,
			CmpMsg msg, SettlementBodyVO[] newsettleBodys) {
		if (oldsettleAgg == null) {
			return;
		}
		SettlementHeadVO newhead = new SettlementHeadVO();
		CmpUtils.processHeadVO(newhead, msg);
		// 得到表头的带出项

		// 得到表体的带出项
	}

	private void doEdit(SettlementBodyVO[] metas, SettlementAggVO settleAgg)
			throws BusinessException {
		SettleAlgorithm algorithm = CMPFactory.createAlgorithm(msg
				.getApplyCombine());
		SettlementBodyVO[] items = null;
		// 第一次切到结算页签
		if (settleAgg == null) {
			// 数据库里没有说明是第一次新增
			if (showBean == null) {
				List<SettlementBodyVO> bodyList = algorithm
						.sumBusinfoByMeta(metas);
				items = CmpUtils.covertListToArrays(bodyList,
						SettlementBodyVO.class);
				// 数据库里有了说明是修改
			} else {
				items = (SettlementBodyVO[]) showBean.getChildrenVO();
				setItemMap(SettleConvert.INSTANCE.convert(items));
				items = algorithm.dealBodyData4BusiUpdate(getItemMap(), metas,
						items, false);
			}
			// 反复的修改
		} else {
			items = (SettlementBodyVO[]) settleAgg.getChildrenVO();
			setItemMap(SettleConvert.INSTANCE.convert(items));
			items = algorithm.dealBodyData4BusiUpdate(getItemMap(), metas,
					items, false);
		}
		// showBean = showBean == null ? new SettlementAggVO() : showBean;
		if (showBean == null) {
			showBean = new SettlementAggVO();
			showBean.setParentVO(new SettlementHeadVO());
		}
		CmpUtils.processHeadVO((SettlementHeadVO) showBean.getParentVO(), msg);
		showBean.setChildrenVO(items);
	}

	@SuppressWarnings("unchecked")
	private void handleBrowBill(TabEventContext contextObject)
			throws BusinessException {
		initRef();
		SettlementAggVO aggBean = dataModel.queryByBillId(contextObject
				.getMsg().getBillkey());
		if (aggBean != null) {

			// 浏览态直接取数据库的数据，不自动合并。
			setLoadBean(aggBean);
			SettleUtils.convertNullToDefaultValue((SettlementBodyVO[]) aggBean
					.getChildrenVO());
			// SettlementAggVO combinAgg = SettleUtils.combinAggBean(aggBean);
			SettleUtils.convertZeroToNull(aggBean);
			// setValue(combinAgg);
			setValue(aggBean);

			this.execLoadFormula();
			// int result = ((SettleModel)
			// getModel()).findBusinessData(combinAgg);
			int result = ((SettleModel) getModel()).findBusinessData(aggBean);
			if (result == -1) {
				// ((SettleModel) getModel()).getData().add(combinAgg);
				((SettleModel) getModel()).getData().add(aggBean);
			}

			// ((nc.ui.cmp.settlement.model.SettleModel)
			// getModel()).directlyUpdateWithOutEvent(combinAgg);
			((nc.ui.cmp.settlement.model.SettleModel) getModel())
					.directlyUpdateWithOutEvent(aggBean);

		} else {
			setLoadBean(null);
			setValue(null);
		}
	}

	
	@Override
	protected void execLoadFormula() {
		super.execLoadFormula();
		SettlementAggVO aggvo = (SettlementAggVO) this.getValue();
		if (aggvo == null || aggvo.getParentVO() == null) {
			return;
		}
		try {
			// setExtLoadFormula();
			initRef();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public void execFormula() {
		execLoadFormula();
	}

	private void setExtLoadFormula() {
		SettlementAggVO aggvo = (SettlementAggVO) this.getValue();

		if (aggvo == null || aggvo.getChildrenVO() == null)
			return;

		if (this.getBillCardPanel().getBodyItem("tradername") != null) {

			// this.getBillCardPanel().getBodyItem("tradername").setLoadFormula(new
			// String[]{supplierFormula});
			int col = this.getBillCardPanel().getBodyColByKey("tradername");
			// TODO 看能否通过缓存来获取相关档案值
			Map<String, String> customerMap = null;
			Map<String, String> supplierMap = new HashMap<String, String>();
			Map<String, String> deptMap = new HashMap<String, String>();
			Map<String, String> psndocMap = new HashMap<String, String>();
			List<String> customerList = new ArrayList<String>();
			List<String> supplierList = new ArrayList<String>();
			List<String> deptList = new ArrayList<String>();
			List<String> psndocList = new ArrayList<String>();

			for (int i = 0; i < aggvo.getChildrenVO().length; i++) {
				SettlementBodyVO bodyvo = (SettlementBodyVO) aggvo
						.getChildrenVO()[i];
				if (bodyvo.getTradertype() == null
						|| bodyvo.getPk_trader() == null) {
					// 无
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_CUSTOMER) {
					// 客户
					customerList.add(bodyvo.getPk_trader());

				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SUPPLIER) {
					// 供应商
					supplierList.add(bodyvo.getPk_trader());
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Department) {
					// 部门
					deptList.add(bodyvo.getPk_trader());
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Person) {
					// 个人
					psndocList.add(bodyvo.getPk_trader());
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SanHu) {
					// 散户
					customerList.add(bodyvo.getPk_trader());
					supplierList.add(bodyvo.getPk_trader());
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_KeShang) {
					// 散户
					customerList.add(bodyvo.getPk_trader());
					supplierList.add(bodyvo.getPk_trader());
				}
			}
			try {
				// 客户
				if (customerList.size() > 0) {
					customerMap = new HashMap<String, String>();
					CustomerVO[] customervos = NCLocator
							.getInstance()
							.lookup(ICustomerPubService.class)
							.getCustomerVO(
									customerList.toArray(new String[] {}),
									new String[] { CustomerVO.PK_CUSTOMER,
											CustomerVO.NAME, CustomerVO.NAME2,
											CustomerVO.NAME3, CustomerVO.NAME4,
											CustomerVO.NAME5, CustomerVO.NAME6 });
					if (customervos != null) {
						for (CustomerVO customerVO : customervos) {
							customerMap.put(customerVO.getPrimaryKey(), VOUtil
									.getMultiLangText(customerVO,
											CustomerVO.NAME));
						}
					}
				}
				// 供应商
				if (supplierList.size() > 0) {
					supplierMap = new HashMap<String, String>();
					SupplierVO[] suppliervos = NCLocator
							.getInstance()
							.lookup(ISupplierPubService_C.class)
							.getSupplierVO(
									supplierList.toArray(new String[] {}), null);
					if (suppliervos != null) {
						for (SupplierVO supplierVO : suppliervos) {
							supplierMap.put(supplierVO.getPrimaryKey(),
									supplierMap.put(supplierVO.getPrimaryKey(),
											VOUtil.getMultiLangText(supplierVO,
													SupplierVO.NAME)));
						}
					}
				}
				// 部门
				if (deptList.size() > 0) {
					deptMap = new HashMap<String, String>();
					DeptVO[] deptVOs = NCLocator
							.getInstance()
							.lookup(IDeptQryService.class)
							.queryAllDeptVOsByOrgID(
									getModel().getContext().getPk_org());
					if (deptVOs != null) {
						for (DeptVO deptVO : deptVOs) {
							deptMap.put(deptVO.getPrimaryKey(), VOUtil
									.getMultiLangText(deptVO, DeptVO.NAME));
						}
					}
				}
				// 业务员
				if (psndocList.size() > 0) {
					psndocMap = new HashMap<String, String>();
					PsndocVO[] psndocvos = NCLocator
							.getInstance()
							.lookup(IPsndocQueryService.class)
							.queryPsndocByPks(
									psndocList.toArray(new String[] {}),
									new String[] { PsndocVO.PK_PSNDOC,
											PsndocVO.NAME, PsndocVO.NAME2,
											PsndocVO.NAME3, PsndocVO.NAME4,
											PsndocVO.NAME5, PsndocVO.NAME6 });
					if (psndocvos != null) {
						for (PsndocVO psndocVO : psndocvos) {
							psndocMap.put(psndocVO.getPrimaryKey(), VOUtil
									.getMultiLangText(psndocVO, PsndocVO.NAME));
						}
					}
				}
			} catch (BusinessException e) {

				Logger.error(e);
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}

			for (int i = 0; i < aggvo.getChildrenVO().length; i++) {
				SettlementBodyVO bodyvo = (SettlementBodyVO) aggvo
						.getChildrenVO()[i];
				if (bodyvo.getTradertype() == null) {
					// 无
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_CUSTOMER) {
					// 客户
					if (customerMap != null && customerMap.size() > 0) {
						this.getBillCardPanel()
								.getBodyPanel()
								.getTableModel()
								.setValueAt(
										customerMap.get(bodyvo.getPk_trader()),
										i, col);
					}
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SUPPLIER) {
					// 供应商
					if (supplierMap != null && supplierMap.size() > 0) {
						this.getBillCardPanel()
								.getBodyPanel()
								.getTableModel()
								.setValueAt(
										supplierMap.get(bodyvo.getPk_trader()),
										i, col);
					}
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Department) {
					// 部门
					if (deptMap != null && deptMap.size() > 0) {
						this.getBillCardPanel()
								.getBodyPanel()
								.getTableModel()
								.setValueAt(deptMap.get(bodyvo.getPk_trader()),
										i, col);
					}
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Person) {
					// 个人
					if (psndocMap != null && psndocMap.size() > 0) {
						this.getBillCardPanel()
								.getBodyPanel()
								.getTableModel()
								.setValueAt(
										psndocMap.get(bodyvo.getPk_trader()),
										i, col);
					}
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SanHu) {
					// 散户
					if (customerMap != null && customerMap.size() > 0) {
						// 先判断是不是客户
						if (customerMap.get(bodyvo.getPk_trader()) != null) {
							this.getBillCardPanel()
									.getBodyPanel()
									.getTableModel()
									.setValueAt(
											customerMap.get(bodyvo
													.getPk_trader()), i, col);
						} else {
							this.getBillCardPanel()
									.getBodyPanel()
									.getTableModel()
									.setValueAt(
											supplierMap.get(bodyvo
													.getPk_trader()), i, col);
						}
					}
				} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_KeShang) {
					// 散户
					if (customerMap != null && customerMap.size() > 0) {
						// 先判断是不是客户
						if (customerMap.get(bodyvo.getPk_trader()) != null) {
							this.getBillCardPanel()
									.getBodyPanel()
									.getTableModel()
									.setValueAt(
											customerMap.get(bodyvo
													.getPk_trader()), i, col);
						} else {
							this.getBillCardPanel()
									.getBodyPanel()
									.getTableModel()
									.setValueAt(
											supplierMap.get(bodyvo
													.getPk_trader()), i, col);
						}
					}
				}
			}

		}
	}

	public void dealBodys(SettlementBodyVO[] bodys) throws BusinessException {
		List<SettlementBodyVO> bodyList = new ArrayList<SettlementBodyVO>();
		for (SettlementBodyVO body : bodys) {
			bodyList.add(body);
		}
		Collections.sort(bodyList, new SettlementBodyVO());
		getBillCardPanel().getBillModel().setBodyDataVO(null);

		bodys = bodyList.toArray(new SettlementBodyVO[0]);
		SettleUtils.convertZeroToNull(bodys);
		SettleUtils.handleBodys4Account(bodys);
		for (int i = 0; i < bodys.length; i++) {

			SettlementBodyVO bodyVO = bodys[i];
			int tradertype = bodyVO.getTradertype();
			if (tradertype != CmpConst.TradeObjType_Never) {
				String pk_trader = bodyVO.getPk_trader();
				String tradername = null;
				if (tradertype == 0) {
					// 客户
					tradername = getColValue("bd_customer", "name",
							"pk_customer", pk_trader);
					if (tradername == null)
						tradername = getColValue("bd_customer", "name",
								"pk_customer", bodyVO.getPk_cubasdoc());
				} else if (tradertype == 1) {
					// 供应商
					tradername = getColValue("bd_supplier", "name",
							"pk_supplier", pk_trader);
					if (tradername == null)
						tradername = getColValue("bd_supplier", "name",
								"pk_supplier", bodyVO.getPk_cubasdoc());
				} else if (tradertype == CmpConst.TradeObjType_Department) {
					// 部门
					tradername = getColValue("org_dept", "name", "pk_dept",
							pk_trader);

				} else if (tradertype == CmpConst.TradeObjType_Person) {
					// 个人
					tradername = getColValue("bd_psndoc", "name", "pk_psndoc",
							pk_trader);
				} else if (tradertype == CmpConst.TradeObjType_SanHu) {

					// 散户 从客商信息中取？？？？
					tradername = getColValue("bd_customer", "name",
							"pk_customer", pk_trader);
					if (tradername == null) {
						tradername = getColValue("bd_supplier", "name",
								"pk_supplier", pk_trader);
					}
				}
				bodyVO.setTradername(tradername);
			} else {
				if (bodyVO.getPk_cubasdoc() != null) {

					String tradername = getColValue("bd_customer", "name",
							"pk_customer", bodyVO.getPk_cubasdoc());
					if (tradername == null) {
						tradername = getColValue("bd_supplier", "name",
								"pk_supplier", bodyVO.getPk_cubasdoc());
					}
					bodyVO.setTradername(tradername);
				}
			}
			getBillCardPanel().getBillModel().addLine();

			getBillCardPanel().getBillModel().setBodyRowVO(bodyVO, i);
			setValue(bodyVO.getNotenumber(), i, "noteno");
		}

	}

	// public void setCurrencyDigit(String pk_currtype, int row) throws
	// BusinessException {
	// // try {
	// int rateDigit = 0;
	// rateDigit = Currency.getCurrDigit(pk_currtype);
	// for (String name : new String[] { "pay", "receive" }) {
	// BillItem item = getBillCardPanel().getBillModel().getItemByKey(name);
	// if (item != null) {
	// if (pk_currtype == null) {
	// item.setDecimalDigits(2);
	// } else {
	// item.setDecimalDigits(item.getDecimalListener().getDecimalFromSource(row,
	// pk_currtype));
	// }
	// if (getValue(row, name) != null) {
	// setValue(((getValue(row, name))), row, name);
	// }
	// }
	// }
	// BillItem localrate =
	// getBillCardPanel().getBillModel().getItemByKey("localrate");
	// localrate.setDecimalDigits(rateDigit);
	// if (getValue(row, "localrate") != null) {
	// setValue(((UFDouble) (getValue(row, "localrate"))).setScale(rateDigit,
	// 4), row, "localrate");
	// }
	// }

	public Object getValue(int row, String key) {
		return getBillCardPanel().getBillModel().getValueAt(row, key);
	}

	public void setValue(Object value, int row, String key) {
		getBillCardPanel().getBillModel().setValueAt(value, row, key);
	}

	/**
	 * 
	 * @param table
	 * @param column
	 * @param pk
	 * @param id
	 * @return
	 */
	public String getColValue(String table, String column, String pk, String id) {
		nc.ui.pub.formulaparse.FormulaParse parser = new nc.ui.pub.formulaparse.FormulaParse();
		parser.setExpress("aim->getColValue(" + table + "," + column + "," + pk
				+ ",id);");
		parser.addVariable("id", id);
		String aim = parser.getValue();
		return aim;
	}

	
	@Override
	public boolean canBeHidden() {
		if (this.getModel().getUiState().equals(UIState.EDIT)) {
			ShowStatusBarMsgUtil.showErrorMsg(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"3607set_0", "03607set-0805")/* @res "消息" */,
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"3607set_0", "03607set-0806")/*
														 * @res
														 * "结算页签编辑态不允许切换到将业务页签"
														 */
					, getModel().getContext());
		}
		getModel().fireEvent(new AppEvent(CmpUIEventType.LEAVE_CARD_TAB));
		return super.canBeHidden();
	}

	
	@Override
	public void showMeUp() {
		super.showMeUp();
		getModel().fireEvent(new AppEvent(CmpUIEventType.LEAVE_LIST_TAB));

		loadNotenumber();
	}

	public Map<String, List<String>> getItemMap() {
		if (itemMap == null) {
			itemMap = new HashMap<String, List<String>>();
		}
		return itemMap;
	}

	public void setItemMap(Map<String, List<String>> itemMap) {
		this.itemMap = itemMap;
	}

	public SettleDataManager getDataModel() {
		return dataModel;
	}

	public void setDataModel(SettleDataManager dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * 从写的原因 表头的主键不能设置，不知道有什麽样的解决方案
	 */
	
	@Override
	public void setValue(Object object) {
		super.setValue(object);
		Object valueObject = getBillCardPanel().getHeadItem(
				SettlementHeadVO.PK_SETTLEMENT).getValueObject();
		int rowCount = getBillCardPanel().getBillModel().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			getBillCardPanel().getBillModel().setValueAt(valueObject, i,
					SettlementHeadVO.PK_SETTLEMENT);
			FireEventManager.fireBodyItemEditEvent(getModel(), billCardPanel,
					SettlementBodyVO.PK_CURRTYPE, i);
		}
	}

	/**
	 * 保存操作拿到界面上的值 其他操作拿到数据库里原始的值
	 */
	@Override
	
	public Object getValue() {
		if (isSaveOperate()) {
			return super.getValue();
		} else {
			if (getModel().getUiState() == UIState.EDIT) {
				return this.getBillCardPanel().getBillValueVO(
						SettlementAggVO.class.getName(),
						SettlementHeadVO.class.getName(),
						SettlementBodyVO.class.getName());
			} else if (((SettleModel) getModel()).isBeLoaded()
					&& getLoadBean() != null) {
				return getLoadBean();

			} else {
				return getModel().getSelectedData();
			}
		}
	}

	public boolean isSaveOperate() {
		return isSaveOperate;
	}

	public void setSaveOperate(boolean isSaveOperate) {
		this.isSaveOperate = isSaveOperate;
	}

	public SettlementAggVO getLoadBean() {
		return loadBean;
	}

	public void setLoadBean(SettlementAggVO loadBean) {
		this.loadBean = loadBean;
	}

	public AccountProxy getProxy() {
		if (proxy == null) {
			proxy = new AccountProxy();
		}
		return proxy;
	}

	public void setDigitMediator(
			nc.ui.pubapp.uif2app.model.AppEventHandlerMediator digitMediator) {
		this.digitMediator = digitMediator;
	}

	public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getDigitMediator() {
		return digitMediator;
	}

	private void setBankInfo(int row) throws BusinessException {
		BillItem pkbankItem = getBillCardPanel().getBodyItem(
				SettlementBodyVO.PK_BANK);
		if (pkbankItem != null && pkbankItem.getComponent() != null
				&& pkbankItem.getComponent() instanceof UIRefPane) {
			BillItem pkAccountItem = getBillCardPanel().getBodyItem(
					SettlementBodyVO.PK_ACCOUNT);
			getBillCardPanel().getBillModel().setValueAt(
					pkAccountItem.getValue(),
					row,
					getBillCardPanel()
							.getBodyColByKey(SettlementBodyVO.PK_BANK));
		}
	}

	/**
	 * 处理对方
	 * 
	 * @param row
	 * @throws BusinessException
	 */
	private void setOppBankInfo(int row) throws BusinessException {
		BillItem pkbankItem = getBillCardPanel().getBodyItem(
				SettlementBodyVO.PK_OPPBANK);
		BillItem pkAccountItem = getBillCardPanel().getBodyItem(
				SettlementBodyVO.PK_OPPACCOUNT);
		if (pkAccountItem == null) {
			return;
		}
		String pk_account = pkAccountItem.getValue();
		if (pkbankItem != null && pkbankItem.getComponent() != null
				&& pkbankItem.getComponent() instanceof UIRefPane) {
			getBillCardPanel().getBillModel().setValueAt(
					pkAccountItem.getValue(),
					row,
					getBillCardPanel().getBodyColByKey(
							SettlementBodyVO.PK_OPPBANK));
		}
		if (pk_account != null) {
			// 处理户名
			BankAccbasVO accbasVObySubBankAccPK = SettleUtils
					.getAccbasVObySubBankAccPK(pk_account);
			getBillCardPanel().getBillModel().setValueAt(
					accbasVObySubBankAccPK.getAccname(),
					row,
					getBillCardPanel().getBodyColByKey(
							SettlementBodyVO.OPPACCNAME));
		} else {
			getBillCardPanel().getBillModel().setValueAt(
					null,
					row,
					getBillCardPanel().getBodyColByKey(
							SettlementBodyVO.OPPACCNAME));
		}
	}

	/**
	 * 处理本方
	 * 
	 * @param row
	 * @throws BusinessException
	 */
	private void setAccountInfo(int row) throws BusinessException {

		BillItem pkAccountItem = getBillCardPanel().getBodyItem(
				SettlementBodyVO.PK_ACCOUNT);
		if (pkAccountItem == null) {
			return;
		}
		String pk_account = pkAccountItem.getValue();
		if (pk_account != null) {
			// 处理账号
			BankAccbasVO bankaccbasvo = SettleUtils
					.getAccbasVObySubBankAccPK(pk_account);
			if (bankaccbasvo != null
					&& UFBoolean.TRUE.equals(bankaccbasvo.getIsinneracc())) {
				getBillCardPanel().getBillModel().setValueAt(
						pk_account,
						row,
						getBillCardPanel().getBodyColByKey(
								SettlementBodyVO.PK_INNERACCOUNT));
			} else {
				/*
				 * 根据主组织、币种、银行账号和票据类型和已经使用的空白票据号， 查出一张未领用的空白票据号，设置到界面上 modify by
				 * aishm 2013-04-25
				 */
				doBlanknote_no(row);
			}

			getBillCardPanel().getBillModel().setValueAt(
					bankaccbasvo.getAccnum(),
					row,
					getBillCardPanel().getBodyColByKey(
							SettlementBodyVO.ACCOUNTNUM));
		} else {
			getBillCardPanel().getBillModel().setValueAt(
					null,
					row,
					getBillCardPanel().getBodyColByKey(
							SettlementBodyVO.ACCOUNTNUM));
		}
	}

	/**
	 * 获取空白票据号，并设置到对应列的票据号上
	 * 
	 * @param event
	 */
	private void doBlanknote_no(int row) {
		BillCardPanel panel = getBillCardPanel();
		if (panel.getHeadTailItem(SettlementHeadVO.DIRECTION).getValueObject() != null// 付款时才需要带出
				&& panel.getHeadTailItem(SettlementHeadVO.DIRECTION)
						.getValueObject().equals(Direction.PAY.VALUE)) {
			String pk_org = (String) panel.getHeadTailItem(
					SettlementHeadVO.PK_ORG).getValueObject();
			String pk_curr = (String) panel.getBodyValueAt(row,
					SettlementBodyVO.PK_CURRTYPE);
			String pk_acc = (String) panel.getBodyValueAt(row,
					SettlementBodyVO.PK_ACCOUNT);
			String billtype = (String) panel.getBodyValueAt(row,
					SettlementBodyVO.PK_NOTETYPE);
			String notenum = null;
			if (StringUtil.isNotNull(pk_org) && StringUtil.isNotNull(pk_curr)
					&& StringUtil.isNotNull(pk_acc)
					&& StringUtil.isNotNull(billtype)) {
				int rows = panel.getRowCount();
				List<String> billnums = new ArrayList<String>();
				for (int i = 0; i < rows; i++) {
					String num = (String) panel.getBodyValueAt(i,
							SettlementBodyVO.NOTENUMBER);
					if (StringUtil.isNotNull(num)) {
						billnums.add(num);
					}
				}
				notenum = NCLocator
						.getInstance()
						.lookup(INoteEBLyPubManageService.class)
						.getLyNoteNum(pk_org, pk_curr, pk_acc, billtype,
								billnums.toArray(new String[0]));
			}
			if (StringUtil.isNotNull(notenum)) {
				panel.setBodyValueAt(notenum, row, SettlementBodyVO.NOTENUMBER);
				panel.setBodyValueAt(null, row, SettlementBodyVO.PK_NOTENUMBER);
			}
		}
	}

	/**
	 * 设置实际付款信息
	 * 
	 * @param e
	 * @throws BusinessException
	 */
	private void setRealPayInfo(BillEditEvent e) throws BusinessException {
		SettlementBodyVO bodyVO = (SettlementBodyVO) getBillCardPanel()
				.getBillModel().getBodyValueRowVO(e.getRow(),
						SettlementBodyVO.class.getName());

		// 如果前后值相同，那么不清空本方账户信息
		if (e.getOldValue() != null && !e.getOldValue().equals(e.getValue())) {
			// 清空信息,币种不可能为Null值，所以不需要判断null
			if (e.getKey().equals(SettlementBodyVO.PK_CURRTYPE_LAST)) {
				bodyVO.setPk_account(null);
			}
		}

		if (e.getKey().equals(SettlementBodyVO.PK_CURRTYPE_LAST)
				&& e.getValue().equals(bodyVO.getPk_currtype())) {
			// 币种相同
			SettleUtils.fillSettleinfoByLastPayInfo(bodyVO);
		} else if (e.getKey().equals(SettlementBodyVO.CHANGERATE)) {
			if (bodyVO.getChangerate() == null) {
				return;
			}
			BillItem billdateItem = getBillCardPanel().getHeadItem(
					SettlementHeadVO.BUSI_BILLDATE);
			if (billdateItem == null) {
				return;
			}
			UFDate billdate = new UFDate(billdateItem.getValue()); // 单据日期
			SettleUtils.setRealPayInfo(bodyVO, billdate);
		} else {
			// 币种不同，需要计算
			BillItem billdateItem = getBillCardPanel().getHeadItem(
					SettlementHeadVO.BUSI_BILLDATE);

			if (billdateItem == null) {
				return;
			}

			if (bodyVO.getPk_Inneraccount() != null
					|| bodyVO.getPk_innerorg() != null) {
				SettleUtils.fillSettleinfoByLastPayInfo(bodyVO);
			}

			UFDate billdate = new UFDate(billdateItem.getValue()); // 单据日期

			CurrencyRateUtil.getInstanceByOrg(bodyVO.getPk_org());
			// 注意来源和目的方向
			UFDouble rate = CurrencyRateUtil.getExchangeRate(
					bodyVO.getPk_org(), bodyVO.getPk_currtype_last(),
					bodyVO.getPk_currtype(), billdate);
			if (UFDouble.ZERO_DBL.equals(rate)) {
				rate = null;
			}
			bodyVO.setChangerate(rate);// 套汇汇率
			if (rate != null) {

				// 实付金额=付款原币金额÷套汇汇率。
				bodyVO.setPay_last(bodyVO.getPay().div(bodyVO.getChangerate()));
			}
			SettleUtils.setRealPayInfo(bodyVO, billdate);
		}
		getBillCardPanel().getBillModel().setBodyRowVO(bodyVO, e.getRow());
	}

	/**
	 * 设置实际付款信息
	 * 
	 * @param e
	 * @throws BusinessException
	 */
	private void setRealPayInfo_money(BillEditEvent e) throws BusinessException {
		SettlementBodyVO bodyVO = (SettlementBodyVO) getBillCardPanel()
				.getBillModel().getBodyValueRowVO(e.getRow(),
						SettlementBodyVO.class.getName());
		// 原金额 = 实付金额*套汇汇率
		if (e.getKey().equals(SettlementBodyVO.PAY)) {
			if (bodyVO.getChangerate() != null) {
				bodyVO.setPay_last(bodyVO.getPay().div(bodyVO.getChangerate()));
			}
		}

		if (e.getKey().equals(SettlementBodyVO.PAY_LAST)) {
			// 套汇汇率变化
			bodyVO.setChangerate(bodyVO.getPay().div(bodyVO.getPay_last()));//
		} else if (e.getKey().equals(SettlementBodyVO.CHANGERATE)) {
			bodyVO.setPay_last(bodyVO.getPay().div(bodyVO.getChangerate()));
		}
		// 组织本币信息
		bodyVO.setPaylocal_last(bodyVO.getPaylocalrate_last().multiply(
				bodyVO.getPay_last()));

		// 集团本币信息
		bodyVO.setGrouppaylocal_last(UFDoubleUtils.multiply(
				bodyVO.getGrouppayrate_last(), bodyVO.getPay_last()));

		// 全局本币信息
		bodyVO.setGlobalpaylocal_last(UFDoubleUtils.multiply(
				bodyVO.getGlobalpayrate_last(), (bodyVO.getPay_last())));

		if (bodyVO.getChangerate() != null) {

			// 汇兑差额=卖出本币金额-买入本币金额 = last - first
			bodyVO.setChangebalance(bodyVO.getPaylocal_last().sub(
					bodyVO.getPaylocal()));
		} else {
			bodyVO.setChangebalance(null);
		}
		getBillCardPanel().getBillModel().setBodyRowVO(bodyVO, e.getRow());
	}

	/**
	 * 设置字段控制信息
	 * 
	 * @throws BusinessException
	 */
	private void setRealPayInfoStatus() throws BusinessException {
		SettlementBodyVO bodyVO = (SettlementBodyVO) getBillCardPanel()
				.getBillModel().getBodyValueRowVO(0,
						SettlementBodyVO.class.getName());
		boolean iscmp52true = isCMP52True(bodyVO.getPk_org());
		if (iscmp52true
				|| bodyVO.getPk_currtype_last() == null
				|| bodyVO.getPk_Inneraccount() != null
				|| bodyVO.getPk_innerorg() != null
				|| (bodyVO.getSettlestatus() != null && bodyVO
						.getSettlestatus().equals(
								SettleStatus.SUCCESSSETTLE.getStatus()))) {

			setBodyItemEnable(SettlementBodyVO.PK_CURRTYPE_LAST,
					UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.PAY_LAST, UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.PAYLOCAL_LAST, UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.PAYLOCALRATE_LAST,
					UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.GROUPPAYLOCAL_LAST,
					UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.GROUPPAYRATE_LAST,
					UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.GLOBALPAYLOCAL_LAST,
					UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.GLOBALPAYRATE_LAST,
					UFBoolean.FALSE);
			setBodyItemEnable(SettlementBodyVO.CHANGERATE, UFBoolean.FALSE);
		} else {
			setBodyItemEnable(SettlementBodyVO.PK_CURRTYPE_LAST, UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.PAY_LAST, UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.PAYLOCAL_LAST, UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.PAYLOCALRATE_LAST,
					UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.GROUPPAYLOCAL_LAST,
					UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.GROUPPAYRATE_LAST,
					UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.GLOBALPAYLOCAL_LAST,
					UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.GLOBALPAYRATE_LAST,
					UFBoolean.TRUE);
			setBodyItemEnable(SettlementBodyVO.CHANGERATE, UFBoolean.TRUE);

		}
		setBodyItemEnable(SettlementBodyVO.CHANGEBALANCE, UFBoolean.FALSE);
	}

	private boolean isCMP52True(String pk_org) {

		boolean flag = false;

		UFBoolean cmp52 = null;
		try {
			cmp52 = SysInit.getParaBoolean(pk_org, CMPSysParamConst.CMP52);
		} catch (BusinessException e) {
			ExceptionHandler.consume(e);
		}

		if (UFBoolean.TRUE.equals(cmp52)) {
			flag = true;
		}

		return flag;

	}

	private void setBodyItemEnable(String key, UFBoolean isEnabled) {
		BillItem item = getBillCardPanel().getBodyItem(key);
		if (item != null) {
			item.setEnabled(isEnabled.booleanValue());
			item.setEdit(isEnabled.booleanValue());
		}
	}

	/**
	 * 获取参数
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	private UFBoolean getCMP52Para(String pk_org) {
		UFBoolean param = UFBoolean.FALSE;
		try {
			param = SysInit.getParaBoolean(pk_org, CMPSysParamConst.CMP52) == null ? null
					: SysInit.getParaBoolean(pk_org, CMPSysParamConst.CMP52);
		} catch (BusinessException e) {
			Logger.error(e);
		}
		return param;
	}
}
