package nc.cmp.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import nc.bs.arap.util.IArapBillTypeCons;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.uap.lock.PKLock;
import nc.cmp.bill.util.SysInit;
import nc.itf.arap.prv.IBXBillPrivate;
import nc.itf.bd.bankacc.subinfo.IBankAccSubInfoQueryService;
import nc.itf.bd.psn.psndoc.IPsndocQueryService;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.itf.bd.timezone.TimezoneUtil;
import nc.itf.cm.prv.CmpConst;
import nc.itf.cmp.bankacc.CMPaccStatus;
import nc.itf.cmp.bill.ICmpChangeBillQueryService;
import nc.itf.cmp.bill.ICmpPayBillQueryService;
import nc.itf.cmp.bill.ICmpRecBillQueryService;
import nc.itf.cmp.busi.ISettlementInfoFetcher;
import nc.itf.fi.pub.Currency;
import nc.itf.org.IDeptQryService;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.md.data.access.NCObject;
import nc.pubitf.arap.gathering.IArapGatheringBillPubQueryService;
import nc.pubitf.arap.pay.IArapPayBillPubQueryService;
import nc.pubitf.arap.payable.IArapPayableBillPubQueryService;
import nc.pubitf.arap.receivable.IArapReceivableBillPubQueryService;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.pubitf.cmp.note.INoteBankrelatedCodeQueryService;
import nc.pubitf.cmp.paybill.ICmpPayBillPubQueryService;
import nc.pubitf.obm.IObmLogManageService;
import nc.pubitf.org.IOrgUnitPubService;
import nc.pubitf.ps.apply.IApplyPubService;
import nc.pubitf.uapbd.IBankaccPubQueryService;
import nc.pubitf.uapbd.IBankdocPubQueryService;
import nc.pubitf.uapbd.ICustomerPubService;
import nc.pubitf.uapbd.ICustsupPubService;
import nc.pubitf.uapbd.ISupplierPubService;
import nc.vo.arap.basebill.BaseAggVO;
import nc.vo.arap.bx.util.BXConstans;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.bd.accessor.BDDataVO;
import nc.vo.bd.balatype.BalaTypeVO;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.notetype.NotetypeVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.cmp.BusiInfo;
import nc.vo.cmp.BusiStatus;
import nc.vo.cmp.CMPSysParamConst;
import nc.vo.cmp.SettleStatus;
import nc.vo.cmp.SettleType;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.cmp.netpay.PaymentCrumbVO;
import nc.vo.cmp.note.NoteBankrelatedCodeQueryVO;
import nc.vo.cmp.settlement.CheckException;
import nc.vo.cmp.settlement.CmpMsg;
import nc.vo.cmp.settlement.SettleEnumCollection.Direction;
import nc.vo.cmp.settlement.SettleEnumCollection.TransType;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.cmp.settlement.batch.BusiStateChangeVO;
import nc.vo.cmp.util.StringUtils;
import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.fts.commissionpayment.IPaymentConst;
import nc.vo.fts.innertransfer.AggInnerTransferVO;
import nc.vo.fts.innertransfer.InnerTransferBVO;
import nc.vo.ml.MultiLangContext;
import nc.vo.obm.pay.LogMgrRequestVO;
import nc.vo.org.DeptVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tmpub.util.DateUtil;
import nc.vo.tmpub.util.TMCurrencyUtil;
import nc.vo.tmpub.util.VOUtil;

public class SettleUtils {
	private volatile static IBankAccSubInfoQueryService bankAccSubInfoQueryService = null;
	private volatile static IBankaccPubQueryService bankaccPubQueryService = null;
	private volatile static IBankdocPubQueryService bankdocPubQueryService = null;
	private volatile static ICustomerPubService customerPubService = null;
	private volatile static ISupplierPubService supplierPubService = null;

	private SettleUtils() {
	};

	/**
	 * �ѽ�����ϢתΪҵ����Ϣ����Ҫ��BusiInfo
	 * 
	 * @param head
	 * @return
	 */
	public static BusiInfo convertSettleBeanToBusiInfo(SettlementAggVO agg) {
		if (agg == null) {
			return null;
		}
		SettlementHeadVO head = (SettlementHeadVO) agg.getParentVO();

		BusiInfo info = new BusiInfo();
		info.setBill_type(head.getPk_tradetype());
		info.setOperator(head.getLastupdater());
		info.setOperatorDate(head.getLastupdatedate());
		info.setPk_bill(head.getPk_busibill());
		SettlementBodyVO[] items = (SettlementBodyVO[]) agg.getChildrenVO();
		if (!CheckException.checkArraysIsNull(items)) {
			Set<String> ids = new HashSet<String>();
			for (SettlementBodyVO body : items) {
				ids.add(body.getPk_billdetail());
			}
			info.setPk_bill_detail(CmpUtils.covertListToArrays(ids, String.class));
		}
		info.setPk_group(head.getPk_group());
		info.setPk_org(head.getPk_org());
		info.setRawBill(head.getRawBill());
		return info;
	}

	public static String convertMsgToQueryCondition(CmpMsg msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(" pk_busibill = '").append(msg.getBillkey()).append("' and pk_tradetype = '")
				.append(msg.getBilltype()).append("'").append(" and pk_org = '").append(msg.getPk_org())
				.append("' and pk_group = '").append(msg.getPk_group()).append("'").append(" and dr = 0");

		// sb.append(" pk_busibill = '").append(msg.getBillkey()).append("'  and dr = 0");
		return sb.toString();
	}

	public static SettlementHeadVO convertAggVOToStandardBean(SettlementAggVO agg) {
		SettlementHeadVO parentVO = (SettlementHeadVO) agg.getParentVO();
		parentVO.setItems((SettlementBodyVO[]) agg.getChildrenVO());
		return parentVO;
	}

	public static SettlementAggVO convertStandardBeanToAggVO(SettlementHeadVO head) {
		SettlementAggVO agg = new SettlementAggVO();
		agg.setParentVO(head);
		agg.setChildrenVO(head.getItems());
		return agg;
	}

	public static String convertBillTypeToConditionSql(String billtype) {
		StringBuilder sb = new StringBuilder();
		sb.append(" billtype = '").append(billtype).append("'");
		return sb.toString();
	}

	/**
	 * ������ҵ��Ʊ�����ֶ�
	 * 
	 * @param bodys
	 * @throws BusinessException
	 */
	public static void handleBodyNoteInfo(SettlementBodyVO[] bodys) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		// ��ҵ��Ʊ
		Collection notetypevos = dao.retrieveByClause(NotetypeVO.class, " noteclass=2");
		for (SettlementBodyVO settlementBodyVO : bodys) {
			if (settlementBodyVO.getPk_notetype() != null) {
				for (NotetypeVO typevo : (NotetypeVO[]) notetypevos.toArray(new NotetypeVO[] {})) {
					if (typevo.getPk_notetype().equals(settlementBodyVO.getPk_notetype())) {
						// ������ҵ��Ʊ��Ϣ
						settlementBodyVO.setIsbillrecord(UFBoolean.TRUE);
						break;
					} else {
						settlementBodyVO.setIsbillrecord(UFBoolean.FALSE);
					}
				}

			} else {
				settlementBodyVO.setIsbillrecord(UFBoolean.FALSE);
			}
		}
	}

	/**
	 * ��������Ϣ
	 * 
	 * @param bodys
	 * @throws BusinessException
	 */
	public static void handleBodyLocalCurrency(UFDate date, SettlementBodyVO[] bodys) throws BusinessException {
		if (date == null) {
			return;
		}
		for (SettlementBodyVO settlebody : bodys) {
			UFDouble groupCurrRate = TMCurrencyUtil.getGroupCurrRate(settlebody.getPk_group(), settlebody.getPk_org(),
					settlebody.getPk_currtype(), date);
			UFDouble globalCurrRate = TMCurrencyUtil.getGlobalCurrRate(settlebody.getPk_org(),
					settlebody.getPk_currtype(), date);
			UFDouble orgCurrRate = TMCurrencyUtil.getOrgCurrRate(settlebody.getPk_org(), settlebody.getPk_currtype(),
					date);

			settlebody.setLocalrate(orgCurrRate);
			settlebody.setGrouprate(groupCurrRate);
			settlebody.setGlobalrate(globalCurrRate);
			if (settlebody.getDirection().equals(Direction.PAY.VALUE)) {
				settlebody.setPaylocal(TMCurrencyUtil.getOrgLocalMoney(settlebody.getPk_org(),
						settlebody.getPk_currtype(), settlebody.getPay(), orgCurrRate, date));
				settlebody.setGrouppaylocal(TMCurrencyUtil.getGroupLocalMoney(settlebody.getPk_group(),
						settlebody.getPk_org(), settlebody.getPk_currtype(), settlebody.getPay(), groupCurrRate,
						orgCurrRate, date));
				settlebody.setGlobalpaylocal(TMCurrencyUtil.getGlobalLocalMoney(settlebody.getPk_org(),
						settlebody.getPk_currtype(), settlebody.getPay(), globalCurrRate, orgCurrRate, date));

			} else if (settlebody.getDirection().equals(Direction.REC.VALUE)) {
				settlebody.setReceivelocal(TMCurrencyUtil.getOrgLocalMoney(settlebody.getPk_org(),
						settlebody.getPk_currtype(), settlebody.getReceive(), orgCurrRate, date));
				settlebody.setGroupreceivelocal(TMCurrencyUtil.getGroupLocalMoney(settlebody.getPk_group(),
						settlebody.getPk_org(), settlebody.getPk_currtype(), settlebody.getReceive(), groupCurrRate,
						orgCurrRate, date));
				settlebody.setGlobalreceivelocal(TMCurrencyUtil.getGlobalLocalMoney(settlebody.getPk_org(),
						settlebody.getPk_currtype(), settlebody.getReceive(), globalCurrRate, orgCurrRate, date));
			}
			if (settlebody.getAgentreceiveprimal() != null) {
				// ����
				settlebody.setAgentreceivelocal(TMCurrencyUtil.getOrgLocalMoney(settlebody.getPk_org(),
						settlebody.getPk_currtype(), settlebody.getAgentreceiveprimal(), orgCurrRate, date));
				settlebody.setGroupagentreceivelocal(TMCurrencyUtil.getGroupLocalMoney(settlebody.getPk_group(),
						settlebody.getPk_org(), settlebody.getPk_currtype(), settlebody.getAgentreceiveprimal(),
						groupCurrRate, orgCurrRate, date));
				settlebody.setGlobalagentreceivelocal(TMCurrencyUtil.getGlobalLocalMoney(settlebody.getPk_org(),
						settlebody.getPk_currtype(), settlebody.getPay(), globalCurrRate, orgCurrRate, date));
			}
		}
	}
	public static void handleBodys4Account(SettlementBodyVO[] bodys) throws BusinessException {
		handleBodys4Account(bodys, false);
	}
	/**
	 * �����˻���Ϣ����������
	 * 
	 * @param bodys
	 * @throws BusinessException
	 */
	public static void handleBodys4Account(SettlementBodyVO[] bodys,boolean is4Settle) throws BusinessException {
		for (int i = 0; i < bodys.length; i++) {
			SettlementBodyVO body = bodys[i];
			String pk_cashaccount = body.getPk_cashaccount();
			String pk_account = body.getPk_account();
			String pk_notetype = body.getPk_notetype();
			// ˳�����д�Ʊ�ݡ��ֽ���
			if (pk_account != null) {
				body.setFundformcode(CmpConst.BANK_DEPOSIT);
			} else if (pk_notetype != null) {
				if (body.getFundformcode() == null) {
					// ������ݵ�ֵΪ����Ҫ�жϴ������򲻴���
					// ����Ʊ�ݴ��ݵ�ֵ�ж��Ǹ�Ʊ������Ʊ
					if (body.getDirection().equals(Direction.PAY.VALUE)) {
						// ��Ʊ
						body.setFundformcode(CmpConst.BILL_PAYDEPOSIT);
					} else {
						// ��Ʊ
						body.setFundformcode(CmpConst.BILL_RECDEPOSIT);
					}
				}
			} else if (pk_cashaccount != null) {
				// ����ֽ��˻���Ϊ�յĻ��������ֽ���
				body.setFundformcode(CmpConst.HAND_CASH);
			} else {
				// Ĭ��ֵ
				body.setFundformcode(null);
			}
			// �ڲ��˻�����
			BankAccbasVO bankaccbasvo = null;
			if (pk_account != null) {

				bankaccbasvo = getAccbasVObySubBankAccPK(body.getPk_account());
				if (bankaccbasvo != null) {
					// ��������
					body.setPk_bank(bankaccbasvo.getPk_bankdoc());
					// ���������˻��˺�
					body.setAccountnum(bankaccbasvo.getAccnum());
				}
				if(is4Settle){
				
				}else{
//					if(payFinanceVO.getOripaymode()!=null && (payFinanceVO.getOripaymode().equals(IPaymentConst.Pay_Group)||payFinanceVO.getOripaymode().equals(IPaymentConst.Pay_Back))){
//						// �ز�֧������֧������£�ȡ�ⲿ����
//						settleBodyVO.setPk_account(getfundVO(payFinanceVO, aggVO).getPk_fundbankaccount_p());
//					}else{
//						settleBodyVO.setPk_account(payFinanceVO.getPk_inneraccount_p());
//					}
					if(body.getPk_billbalatype()!=null&& (body.getPk_billbalatype().equals(IPaymentConst.Pay_Group)||body.getPk_billbalatype().equals(IPaymentConst.Pay_Back))){
						// �ز�֧������֧������£�ȡ�ⲿ����,ҵ�񵥾��޸�ʱ�ڲ��˻������޸�
					}else if (bankaccbasvo != null && UFBoolean.TRUE.equals(bankaccbasvo.getIsinneracc())) {
						body.setPk_Inneraccount(body.getPk_account());
					} else{
						body.setPk_Inneraccount(null);
					}
				}
//				/*
//				 * 2011-10-28 ���Զ���գ���֤�ز�ҵ���ʵ�֡� else {
//				 * body.setPk_Inneraccount(null); }
//				 */
			} else {
				body.setPk_Inneraccount(null);
			}
			if(!is4Settle){
				// ������Ϣ�޸Ĳ��ܴ���Է���Ϣ
				String pk_oppaccount = body.getPk_oppaccount();
				if (pk_oppaccount != null) {
	
					bankaccbasvo = getAccbasVObySubBankAccPK(pk_oppaccount);
					if (bankaccbasvo != null) {
						// �Է�����
						body.setPk_oppbank(bankaccbasvo.getPk_bankdoc());
						// �����Է������˻�����
						body.setOppaccname(bankaccbasvo.getAccname());
						body.setOppaccount(bankaccbasvo.getAccnum());
					}
					if (bankaccbasvo != null && bankaccbasvo.getIsinneracc() != null
							&& bankaccbasvo.getIsinneracc().booleanValue()) {
						// ���ڲ��˻����ڲ���λ�����Ƿ����ڲ�����
						body.setPk_innerorg(bankaccbasvo.getPk_org());
					} else {
						body.setPk_innerorg(null);
					}
				} else {
					body.setPk_innerorg(null);
				}
			}

		}
		boolean isFormDB = false;
		for (int i = 0; i < bodys.length; i++) {
			SettlementBodyVO body = bodys[i];
			if (getNoteType(false).get(body.getPk_notetype()) != null) {
				body.setIsbillrecord(UFBoolean.TRUE);
			} else {
				isFormDB = true;
				if (!isFormDB && getNoteType(false).get(body.getPk_notetype()) != null) {
					body.setIsbillrecord(UFBoolean.TRUE);
				} else {
					body.setIsbillrecord(UFBoolean.FALSE);
				}
			}
		}
	}

	private static Map<String, NotetypeVO> noteTypeMap = null;

	/**
	 * ��ȡ��ҵ��Ʊ��Ϣ
	 * 
	 * @param isFormDB
	 *            �Ƿ����²����ݿ�,true:�ǣ�false:��
	 * @return
	 * @throws BusinessException
	 */
	private synchronized  static Map<String, NotetypeVO> getNoteType(boolean isFormDB) throws BusinessException {
		if (noteTypeMap == null) {
			noteTypeMap = new HashMap<String, NotetypeVO>();
		}
		if (isFormDB) {
			BaseDAO dao = new BaseDAO();
			Collection notetypevos = dao.retrieveByClause(NotetypeVO.class, " noteclass=2");
			noteTypeMap = new HashMap<String, NotetypeVO>();
			for (NotetypeVO typevo : (NotetypeVO[]) notetypevos.toArray(new NotetypeVO[] {})) {
				noteTypeMap.put(typevo.getPk_notetype(), typevo);
			}
		}
		return noteTypeMap;
	}

	/**
	 * ���ñ�������ⲿ������Ϣ
	 * 
	 * @param aggvo
	 * @throws BusinessException
	 */
	public static void handlebody4Transtype(SettlementAggVO aggvo) throws BusinessException {
		Integer direction = ((SettlementHeadVO) aggvo.getParentVO()).getDirection();
		if (direction.equals(Direction.CHANGE.VALUE)) {
			// ���˵�����ҵ�ڲ���ת������Ҫ�������ⲿ����
			for (SettlementBodyVO body : (SettlementBodyVO[]) aggvo.getChildrenVO()) {
				body.setTranstype(null);
			}
			return;
		}
		Map<String, CustomerVO> customerMap = getCustomerVOs(aggvo);
		Map<String, SupplierVO> supplierMap = getSupplierVOs(aggvo);

		// �ո�����Ҫ�������ⲿ����
		for (SettlementBodyVO body : (SettlementBodyVO[]) aggvo.getChildrenVO()) {
			CustomerVO customerVO = null;
			SupplierVO supplierVO = null;
			if (body.getPk_trader() != null && customerMap != null) {
				customerVO = customerMap.get(body.getPk_trader());
			}
			if (customerVO != null) {
				// ���ݿͻ��ж�
				body.setTranstype(customerVO.getCustprop().equals(1) ? TransType.IN.VALUE : TransType.OUT.VALUE);
				continue;
			}
			if (body.getPk_trader() != null && supplierMap != null) {
				supplierVO = supplierMap.get(body.getPk_trader());
			}
			if (supplierVO != null) {
				// ���ݹ�Ӧ���ж�
				body.setTranstype(supplierVO.getSupprop().equals(1) ? TransType.IN.VALUE : TransType.OUT.VALUE);
				continue;
			}
			// ���ڷ�Χ�ڣ��϶����ⲿ����
			body.setTranstype(TransType.OUT.VALUE);
		}
	}

	/**
	 * ������Ϣ
	 * 
	 * @param aggvo
	 * @throws BusinessException
	 */
	public static void handleSettleInfo(SettlementAggVO... aggVOs) throws BusinessException {
		handleSettleInfo(false,aggVOs);
	}
	public static void handleSettleInfo(boolean is4Settle,SettlementAggVO... aggVOs) throws BusinessException {
		for (SettlementAggVO aggvo : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) aggvo.getParentVO();
			SettlementBodyVO[] bodys = (SettlementBodyVO[]) aggvo.getChildrenVO();

			SettleUtils.handleBodys4Account(bodys,is4Settle);
			SettleUtils.handlebody4Transtype(aggvo);
			// if(head.getSettledate()==null){
			// // �����Ѿ��������Ϣ����ά����֯ҵ������
			// SettleUtils.handlebodys4ORGDate(CMPaccStatus.SAVEACCOUNT, bodys);
			// }
			SettleUtils.handBody4Tradername(is4Settle,head.getPk_org(), head.getDirection(), bodys);
			SettleUtils.handleBodyNoteInfo(bodys);
			SettleUtils.fillSettleBankrelated_code(head, bodys);
			handleMaterialInfo(bodys);
		}
		// Ĭ��ֵ
		SettleUtils.convertZeroToNull(aggVOs);
		// Ʊ����Ϣ
		handleNoteInfo(aggVOs);
		// �޸�״̬
		SettleUtils.handleModifyInfo(aggVOs);
	}
	public static void handleModifyInfo(SettlementAggVO... aggVOs) {
		for (SettlementAggVO aggvo : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) aggvo.getParentVO();
			if(head.getPrimaryKey() != null){
			SettlementBodyVO[] bodys = (SettlementBodyVO[]) aggvo.getChildrenVO();
			SettleUtils.fillSettlementVOInfo(DataUtil.getCurrentUser(), SettlementHeadVO.MODIFIER, head);
			SettleUtils.fillSettlementVOInfo(DataUtil.getCurrentUser(), SettlementHeadVO.MODIFIER, bodys);
			SettleUtils.fillSettlementVOInfo(DataUtil.getCurrentTime(), SettlementHeadVO.MODIFIEDTIME, head);
			SettleUtils.fillSettlementVOInfo(DataUtil.getCurrentTime(), SettlementHeadVO.MODIFIEDTIME, bodys);
			}
		}
	}

	/**
	 * ����Ʊ����Ϣ
	 * 
	 * @param aggvos
	 * @throws BusinessException
	 */
	public static void handleNoteInfo(SettlementAggVO... aggvos) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		// ��ҵ��Ʊ
		Collection notetypevos = dao.retrieveByClause(NotetypeVO.class, " noteclass=2");
		Map<String, NotetypeVO> mapNote = CmpUtils.makeMap();
		for (NotetypeVO typevo : (NotetypeVO[]) notetypevos.toArray(new NotetypeVO[] {})) {
			mapNote.put(typevo.getPrimaryKey(), typevo);
		}
		for (SettlementAggVO aggvo : aggvos) {
			for (SettlementBodyVO settlementBodyVO : (SettlementBodyVO[]) aggvo.getChildrenVO()) {
				if (settlementBodyVO.getPk_notetype() == null) {
					settlementBodyVO.setPk_notenumber(null);
					// settlementBodyVO.setNotenumber(null);
					continue;
				}
				NotetypeVO typevo = mapNote.get(settlementBodyVO.getPk_notetype());
				if (typevo != null && typevo.getPk_notetype().equals(settlementBodyVO.getPk_notetype())) {
					// ������ҵ��Ʊ��Ϣ
					settlementBodyVO.setPk_notenumber(settlementBodyVO.getNotenumber());
				}
			}

		}
	}

	/**
	 * �������Ϸ�����Ϣ
	 * 
	 * @param bodyvos
	 * @throws BusinessException
	 */
	public static void handleMaterialInfo(SettlementBodyVO... bodyvos) throws BusinessException {
		// Map<String, MaterialVO> queryMaterialBaseInfoByPks(String[] pks,
		// String[] fields)
		List<String> materalLst = CmpUtils.makeList();
		for (SettlementBodyVO settlementBodyVO : bodyvos) {
			if (settlementBodyVO.getPk_invbasdoc() != null) {
				materalLst.add(settlementBodyVO.getPk_invbasdoc());
			}
		}
		if (materalLst.size() == 0) {
			return;
		}

		Map<String, MaterialVO> queryMaterialBaseInfoByPks = NCLocator
				.getInstance()
				.lookup(nc.pubitf.uapbd.IMaterialPubService.class)
				.queryMaterialBaseInfoByPks(CmpUtils.covertListToArrays(materalLst, String.class),
						new String[] { MaterialVO.PK_MARBASCLASS, MaterialVO.PK_MATERIAL });
		for (SettlementBodyVO settlementBodyVO : bodyvos) {
			if (settlementBodyVO.getPk_invbasdoc() != null) {
				settlementBodyVO.setPk_invcl(queryMaterialBaseInfoByPks.get(settlementBodyVO.getPk_invbasdoc())
						.getPk_marbasclass());
			}
		}
	}

	/**
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	private static Map<String, CustomerVO> getCustomerVOs(SettlementAggVO aggvo) throws BusinessException {
		List<String> traderlist = CmpUtils.makeList();
		for (SettlementBodyVO body : (SettlementBodyVO[]) aggvo.getChildrenVO()) {
			Integer tradertype = body.getTradertype();
			if (tradertype != null
					&& (tradertype.equals(CmpConst.TradeObjType_CUSTOMER) || tradertype
							.equals(CmpConst.TradeObjType_KeShang)) && body.getPk_trader() != null) {
				traderlist.add(body.getPk_trader());
			}
		}

		if (traderlist.size() > 0) {
			Map<String, CustomerVO> map = new HashMap<String, CustomerVO>();
			CustomerVO[] customerVOs = getCustomerpubService().getCustomerVO(traderlist.toArray(new String[] {}), null);
			if (CmpUtils.isListNull(customerVOs))
				return null;
			for (CustomerVO vo : customerVOs) {
				map.put(vo.getPrimaryKey(), vo);
			}
			return map;
		} else
			return null;
	}

	/**
	 * ��ȡ��Ӧ����Ϣ
	 * 
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	private static Map<String, SupplierVO> getSupplierVOs(SettlementAggVO aggvo) throws BusinessException {
		List<String> traderlist = CmpUtils.makeList();
		for (SettlementBodyVO body : (SettlementBodyVO[]) aggvo.getChildrenVO()) {
			Integer tradertype = body.getTradertype();
			if (tradertype != null
					&& (tradertype.equals(CmpConst.TradeObjType_SUPPLIER) || tradertype
							.equals(CmpConst.TradeObjType_KeShang)) && body.getPk_trader() != null) {
				traderlist.add(body.getPk_trader());
			}
		}
		if (traderlist.size() > 0) {

			Map<String, SupplierVO> map = new HashMap<String, SupplierVO>();
			SupplierVO[] supplierVOs = getSupplierpubService().getSupplierVO(traderlist.toArray(new String[] {}), null);
			if (CmpUtils.isListNull(supplierVOs))
				return null;
			for (SupplierVO vo : supplierVOs) {
				if (vo != null) {
					map.put(vo.getPrimaryKey(), vo);
				}
			}
			return map;
		} else
			return null;
	}

	/**
	 * ��̨ʹ�ò���settleMeta
	 * 
	 * @param bill
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	public static SettlementBodyVO[] getSettleMetas(Object bill, String billtype, String pk_group, int autoCombin)
			throws BusinessException {
		ISettlementInfoFetcher fetcher = BusiBuffer.INSTANCE.getInfoFetcher(billtype, pk_group);
		SettlementBodyVO[] settlementBodyVOs = (SettlementBodyVO[]) fetcher.getSettleMetaInfo(bill);
		if (autoCombin == CmpConst.ONE_TO_MANY) {
			// �Զ��ϲ�
			return getIncorporateSettlementBodyVO(settlementBodyVOs);
		}
		
		return settlementBodyVOs;
	}

	private static SettlementBodyVO[] getIncorporateSettlementBodyVO(SettlementBodyVO[] settlementBodyVOs)
			throws BusinessException {
		// ���������Ϣ����Ϊ�� ����С��1�� ֱ�ӷ��� ���ϲ�
		if (settlementBodyVOs == null || settlementBodyVOs.length < 2) {
			return settlementBodyVOs;
		}

		// Map<String, List<SettlementBodyVO>> map = new HashMap<String,
		// List<SettlementBodyVO>>();
		//
		// for (SettlementBodyVO settlementBodyVO : settlementBodyVOs) {
		// // �ϲ����� 2011-10-24 modify songllb �����˻������֡����㷽ʽ���Է����С��Է��˻����Է��������ơ��Է����͡�
		// // �ʽ�ƻ���Ŀ����֧��Ŀ������ٶȡ�Ʊ�����͡�Ʊ�ݺš����š��ֽ��˻�����Ŀ����Ŀ�׶Ρ��ֽ�������Ŀ
		//
		//
		// StringBuilder keyBuilder = new StringBuilder();
		//
		// // �ϲ����� ���׶������� + ���׶��� + ���������˻� + �տ������˻� + ���� + ����
		// //�����˻�
		// keyBuilder.append(settlementBodyVO.getPk_account()).append("-");
		// // ����
		// keyBuilder.append(settlementBodyVO.getPk_currtype()).append("-");
		// // ���㷽ʽ
		// keyBuilder.append(settlementBodyVO.getPk_balatype()).append("-");
		//
		// // �Է�����
		// keyBuilder.append(settlementBodyVO.getPk_oppbank()).append("-");
		// //�Է��˻�
		// keyBuilder.append(settlementBodyVO.getPk_oppaccount()).append("-");
		// //�Է���������
		// keyBuilder.append(settlementBodyVO.getTradername()).append("-");
		// keyBuilder.append(settlementBodyVO.getPk_trader()).append("-");
		// //�Է�����
		// keyBuilder.append(settlementBodyVO.getTradertype()).append("-");
		// // �ʽ�ƻ���Ŀ
		// keyBuilder.append(settlementBodyVO.getPk_plansubj()).append("-");
		// //��֧��Ŀ
		// keyBuilder.append(settlementBodyVO.getPk_costsubj()).append("-");
		// //����ٶ�
		// keyBuilder.append(settlementBodyVO.getPay_type()).append("-");
		// // Ʊ������
		// keyBuilder.append(settlementBodyVO.getPk_notetype()).append("-");
		// // Ʊ�ݺ�
		// keyBuilder.append(settlementBodyVO.getNotenumber()).append("-");
		// //����
		// keyBuilder.append(settlementBodyVO.getPk_deptdoc()).append("-");
		// // �ֽ��˻�
		// keyBuilder.append(settlementBodyVO.getPk_cashaccount()).append("-");
		// // ��Ŀ
		// keyBuilder.append(settlementBodyVO.getPk_job()).append("-");
		// //��Ŀ�׶�
		// keyBuilder.append(settlementBodyVO.getPk_jobphase()).append("-");
		// // �ֽ�������Ŀ
		// keyBuilder.append(settlementBodyVO.getPk_cashflow()).append("-");
		//
		//
		// // keyBuilder.append(settlementBodyVO.getTradertype()).append("-");
		// //
		// // keyBuilder.append(settlementBodyVO.getPk_trader()).append("-");
		// //
		// // keyBuilder.append(settlementBodyVO.getPk_account()).append("-");
		// //
		// //
		// keyBuilder.append(settlementBodyVO.getPk_oppaccount()).append("-");
		// //
		// // keyBuilder.append(settlementBodyVO.getPk_currtype()).append("-");
		// //
		// // keyBuilder.append(settlementBodyVO.getDirection()).append("-");
		// String key = keyBuilder.toString();
		// if (map.containsKey(key)) {
		// map.get(key).add(settlementBodyVO);
		// } else {
		// List<SettlementBodyVO> list = new ArrayList<SettlementBodyVO>();
		// list.add(settlementBodyVO);
		// map.put(key, list);
		// }
		//
		// }
		//
		// List<SettlementBodyVO> rsnList = new ArrayList<SettlementBodyVO>();
		//
		// Iterator it = map.entrySet().iterator();
		//
		// while (it.hasNext()) {
		// Map.Entry entry = (Map.Entry) it.next();
		// List<SettlementBodyVO> list = (List<SettlementBodyVO>)
		// entry.getValue();
		// SettlementBodyVO settlementBodyVO = null;
		//
		// for (int i = 0; i < list.size(); i++) {
		// if (i == 0) {
		// settlementBodyVO = list.get(0);
		// rsnList.add(settlementBodyVO);
		// } else {
		// SettlementBodyVO body = list.get(i);
		//
		// settlementBodyVO.setPay(UFDoubleUtils.add(settlementBodyVO.getPay(),
		// body.getPay()));
		// settlementBodyVO.setPaylocal(UFDoubleUtils.add(settlementBodyVO.getPaylocal(),
		// body.getPaylocal()));
		// settlementBodyVO.setGrouppaylocal(UFDoubleUtils.add(settlementBodyVO.getGrouppaylocal(),
		// body.getGrouppaylocal()));
		// settlementBodyVO.setGlobalpaylocal(UFDoubleUtils.add(settlementBodyVO.getGlobalpaylocal(),
		// body.getGlobalpaylocal()));
		//
		// settlementBodyVO.setReceive(UFDoubleUtils.add(settlementBodyVO.getReceive(),
		// body.getReceive()));
		// settlementBodyVO.setReceivelocal(UFDoubleUtils.add(settlementBodyVO.getReceivelocal(),
		// body.getReceivelocal()));
		// settlementBodyVO.setGroupreceivelocal(UFDoubleUtils.add(settlementBodyVO.getGroupreceivelocal(),
		// body.getGroupreceivelocal()));
		// settlementBodyVO.setGlobalreceivelocal(UFDoubleUtils.add(settlementBodyVO.getGlobalreceivelocal(),
		// body.getGlobalreceivelocal()));
		// }
		// }
		//
		// }
		return CmpUtils.dealBodys(settlementBodyVOs).toArray(new SettlementBodyVO[] {});
		// return rsnList.toArray(new SettlementBodyVO[] {});
	}

	/** ����ֵתΪĬ��ֵ���������ݿ�δ��Ĭ��ֵ���ӽ���õ��༭���vo����� */
	public static SettlementBodyVO[] convertNullToDefaultValue(SettlementBodyVO... bodyvos) {
		if (bodyvos == null) {
			return bodyvos;
		}
		for (SettlementBodyVO bodyvo : bodyvos) {
			if (bodyvo.getReceive() == null) {
				bodyvo.setReceive(UFDouble.ZERO_DBL);
			}
			if (bodyvo.getReceivelocal() == null) {
				bodyvo.setReceivelocal(UFDouble.ZERO_DBL);
			}
			if (bodyvo.getGroupreceivelocal() == null) {
				bodyvo.setGroupreceivelocal(UFDouble.ZERO_DBL);
			}
			if (bodyvo.getGlobalreceivelocal() == null) {
				bodyvo.setGlobalreceivelocal(UFDouble.ZERO_DBL);
			}

			if (bodyvo.getPaylocal() == null) {
				bodyvo.setPaylocal(UFDouble.ZERO_DBL);
			}
			if (bodyvo.getPay() == null) {
				bodyvo.setPay(UFDouble.ZERO_DBL);
			}
			if (bodyvo.getGrouppaylocal() == null) {
				bodyvo.setGrouppaylocal(UFDouble.ZERO_DBL);
			}
			if (bodyvo.getGlobalpaylocal() == null) {
				bodyvo.setGlobalpaylocal(UFDouble.ZERO_DBL);
			}

			if (bodyvo.getGrouppaylocal_last() == null) {
				bodyvo.setGrouppaylocal_last(UFDouble.ZERO_DBL);
			}
			if (bodyvo.getGlobalpaylocal_last() == null) {
				bodyvo.setGlobalpaylocal_last(UFDouble.ZERO_DBL);
			}

			// if (bodyvo.getModifyflag() == null) {
			// bodyvo.setModifyflag(CmpConst.MODIFY_FLAG);
			// }
			if (bodyvo.getTallystatus() == null) {
				bodyvo.setTallystatus(CMPaccStatus.NOACCOUNT.getStatus());
			}
		}
		return bodyvos;
	}

	/**
	 * ��ά��0������
	 * 
	 * @param bodyvos
	 */
	public static void convertZeroToNull(SettlementBodyVO[] bodyvos) {
		if (bodyvos == null) {
			return;
		}
		for (SettlementBodyVO bodyvo : bodyvos) {
			if (bodyvo.getDirection().equals(0)) {
				bodyvo.setPaylocal(null);
				bodyvo.setPay(null);
				bodyvo.setGlobalpaylocal(null);
				bodyvo.setGrouppaylocal(null);

				if (bodyvo.getReceive() == null) {
					bodyvo.setReceive(UFDouble.ZERO_DBL);
				}
				if (bodyvo.getReceivelocal() == null) {
					bodyvo.setReceivelocal(UFDouble.ZERO_DBL);
				}
			} else {

				bodyvo.setReceive(null);
				bodyvo.setReceivelocal(null);
				bodyvo.setGlobalreceivelocal(null);
				bodyvo.setGroupreceivelocal(null);
				if (bodyvo.getPaylocal() == null) {
					bodyvo.setPaylocal(UFDouble.ZERO_DBL);
				}
				if (bodyvo.getPay() == null) {
					bodyvo.setPay(UFDouble.ZERO_DBL);
				}
				// bodyvo.setGlobalcommreceivelocal(null);
				// bodyvo.setAgentreceiveprimal(null);
				// bodyvo.setAgentreceivelocal(null);

			}
			if (bodyvo.getTallystatus() == null) {
				bodyvo.setTallystatus(CMPaccStatus.NOACCOUNT.getStatus());
			}
			if (bodyvo.getModifyflag() == null) {
				bodyvo.setModifyflag(CmpConst.MODIFY_FLAG);
			}
		}
	}

	/**
	 * ��ά��0������
	 * 
	 * @param bodyvos
	 */
	public static void convertZeroToNull(SettlementAggVO... aggs) {
		if (aggs == null) {
			return;
		}
		for (SettlementAggVO settlementAggVO : aggs) {
			convertZeroToNull((SettlementBodyVO[]) settlementAggVO.getChildrenVO());
		}
	}

	/**
	 * �õ������Ӧ��ҵ��agg
	 * 
	 * @param tradrType
	 *            --�������ͣ�pk_busibill--ҵ�񵥾�����
	 * @return String[] ---pk_settlement[]
	 */
	public static AggregatedValueObject getBuisAggVO(String tradrType, String pk_busibill, String pk_group)
			throws BusinessException {

		String billType = SettleUtils.getbilltype(tradrType, pk_group);
		Object obj = null;
		AggregatedValueObject[] bill = new AggregatedValueObject[1];
		if ("F5".equals(billType)) {
			ICmpPayBillQueryService iCmpPayBillQueryService = NCLocator.getInstance().lookup(
					ICmpPayBillQueryService.class);
			obj = iCmpPayBillQueryService.queryBillById(pk_busibill);
			bill[0] = (AggregatedValueObject) obj;
		} else if ("F4".equals(billType)) {
			ICmpRecBillQueryService iCmpRecBillQueryService = NCLocator.getInstance().lookup(
					ICmpRecBillQueryService.class);
			obj = iCmpRecBillQueryService.queryBillById(pk_busibill);
			bill[0] = (AggregatedValueObject) obj;
		} else if ("F6".equals(billType)) {
			ICmpChangeBillQueryService iCmpChangeBillQueryService = NCLocator.getInstance().lookup(
					ICmpChangeBillQueryService.class);
			obj = iCmpChangeBillQueryService.queryBillById(pk_busibill);
			bill[0] = (AggregatedValueObject) obj;
		} else if (IArapBillTypeCons.F0.equals(billType)) {
			bill = NCLocator.getInstance().lookup(IArapReceivableBillPubQueryService.class)
					.findBillByPrimaryKey(new String[] { pk_busibill });
		} else if (IArapBillTypeCons.F1.equals(billType)) {
			bill = NCLocator.getInstance().lookup(IArapPayableBillPubQueryService.class)
					.findBillByPrimaryKey(new String[] { pk_busibill });
		} else if (IArapBillTypeCons.F2.equals(billType)) {
			bill = NCLocator.getInstance().lookup(IArapGatheringBillPubQueryService.class)
					.findBillByPrimaryKey(new String[] { pk_busibill });
		} else if (IArapBillTypeCons.F3.equals(billType)) {
			bill = NCLocator.getInstance().lookup(IArapPayBillPubQueryService.class)
					.findBillByPrimaryKey(new String[] { pk_busibill });
		} else if (BXConstans.BX_DJLXBM.equals(billType)) {
			bill = NCLocator.getInstance().lookup(IBXBillPrivate.class)
					.queryVOsByPrimaryKeys(new String[] { pk_busibill }, BXConstans.BX_DJDL)
					.toArray(new AggregatedValueObject[] {});
		}
		// else if(IArapBillTypeCons.F0S.equals(billType)){
		// bill = NCLocator.getInstance().lookup(
		// IArapSupReceivableBillPubQueryService.class).findBillByPrimaryKey(new
		// String[]{pk_busibill});
		// }else if(IArapBillTypeCons.F1C.equals(billType)){
		// bill = NCLocator.getInstance().lookup(
		// IArapCusPayableBillPubQueryService.class).findBillByPrimaryKey(new
		// String[]{pk_busibill});
		// }else if(IArapBillTypeCons.F2S.equals(billType)){
		// bill = NCLocator.getInstance().lookup(
		// IArapSupGatheringBillPubQueryService.class).findBillByPrimaryKey(new
		// String[]{pk_busibill});
		// }else if(IArapBillTypeCons.F3C.equals(billType)){
		// bill = NCLocator.getInstance().lookup(
		// IArapCusPayBillPubQueryService.class).findBillByPrimaryKey(new
		// String[]{pk_busibill});
		// }
		return bill[0];
	}

	public static SettlementAggVO combinAggBean(SettlementAggVO aggBean) throws BusinessException {
		SettlementHeadVO head = (SettlementHeadVO) aggBean.getParentVO();
		SettlementAggVO cloneAgg = new SettlementAggVO();
		SettlementBodyVO[] cloneBodys = CmpUtils.CloneObj((SettlementBodyVO[]) aggBean.getChildrenVO());
		cloneBodys = CMPFactory.createAlgorithm(head.getArithmetic()).sumBodyVOToUI(cloneBodys);
		cloneAgg.setParentVO(head);

		cloneAgg.setChildrenVO(cloneBodys);
		SettleUtils.convertZeroToNull(cloneAgg);
		return cloneAgg;
	}

	/**
	 * �õ�������ͷ����
	 * 
	 * @param SettlementAggVO
	 *            []
	 * @return String[] ---pk_settlement[]
	 */
	public static SettlementHeadVO[] convertSettlementAggVOToSettlementHeadVOArray(SettlementAggVO... aggs) {
		SettlementHeadVO[] heads = new SettlementHeadVO[aggs.length];
		for (int i = 0; i < aggs.length; i++) {
			heads[i] = (SettlementHeadVO) aggs[i].getParentVO();
		}
		return heads;
	}

	/**
	 * �õ�������ͷ����
	 * 
	 * @param SettlementAggVO
	 *            []
	 * @return String[] ---pk_settlement[]
	 */
	public static String[] convertSettlementAggVOToSettlementHeadPkArray(SettlementAggVO... aggs) {
		SettlementHeadVO[] heads = convertSettlementAggVOToSettlementHeadVOArray(aggs);
		List<String> headPkList = CmpUtils.makeList();
		for (SettlementHeadVO head : heads) {
			headPkList.add(head.getPrimaryKey());
		}
		String[] headPks = new String[headPkList.size()];
		headPkList.toArray(headPks);
		return headPks;
	}

	/**
	 * �õ������������
	 * 
	 * @param SettlementAggVO
	 *            []
	 * @return SettlementBodyVO[]
	 */
	public static SettlementBodyVO[] convertSettlementAggVOToSettlementBodyVOArray(SettlementAggVO... aggs) {
		List<SettlementBodyVO> bodyList = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggs) {
			SettlementBodyVO[] bodys = (SettlementBodyVO[]) settlementAggVO.getChildrenVO();
			bodyList.addAll(Arrays.asList(bodys));
		}
		SettlementBodyVO[] bodys = new SettlementBodyVO[bodyList.size()];
		bodyList.toArray(bodys);
		return bodys;
	}

	/**
	 * �õ������ͷpk�ͽ����������Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementAggVO
	 * @return Map<String, SettlementBodyVO[]> key--SettleheadPk
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, SettlementBodyVO[]> convertSettlementAggVOToHeadPkToBodyMap(SettlementAggVO... aggs)
			throws BusinessException {
		Map<String, SettlementBodyVO[]> headpkToBodyMap = CmpUtils.makeMap();
		for (SettlementAggVO agg : aggs) {
			headpkToBodyMap.put(agg.getParentVO().getPrimaryKey(), (SettlementBodyVO[]) agg.getChildrenVO());
		}
		return headpkToBodyMap;
	}

	/**
	 * �õ��ʽ𵥾ݱ���pk�ͽ������Ķ�Ӧ��ϵ ����ٶ��Ե��ʽ𵥺˽�����Ϣ��һ��һ����������漰���У�����VO����������е���
	 * 
	 * @param SettlementAggVO
	 * @return Map<String, SettlementBodyVO[]> key--SettleheadPk
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, SettlementBodyVO> convertSettlementAggVOToFtsBodyPkToBodyMap(SettlementAggVO... aggs)
			throws BusinessException {
		Map<String, SettlementBodyVO> ftsbodypkTosettleBodyMap = CmpUtils.makeMap();
		for (SettlementAggVO settlementAggVO : aggs) {
			SettlementBodyVO[] bodys = (SettlementBodyVO[]) settlementAggVO.getChildrenVO();
			for (SettlementBodyVO body : bodys) {
				ftsbodypkTosettleBodyMap.put(body.getPk_ftsbilldetail(), body);
			}
		}
		return ftsbodypkTosettleBodyMap;
	}

	/**
	 * �õ��ʽ𵥾ݱ���pk�ͽ������pk�Ķ�Ӧ��ϵ ����ٶ��Ե��ʽ𵥺˽�����Ϣ��һ��һ����������漰���У�����VO����������е���
	 * 
	 * @param SettlementAggVO
	 * @return Map<String, SettlementBodyVO[]> key--SettleheadPk
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, String> convertSettlementAggVOToFtsBodyPkToSettleBodyPKMap(SettlementAggVO... aggs)
			throws BusinessException {
		Map<String, String> ftsbodypkTosettleBodyPkMap = CmpUtils.makeMap();
		for (SettlementAggVO agg : aggs) {
			SettlementBodyVO[] bodys = (SettlementBodyVO[]) agg.getChildrenVO();
			for (SettlementBodyVO body : bodys) {
				ftsbodypkTosettleBodyPkMap.put(body.getPk_ftsbilldetail(), body.getPk_detail());
			}
		}
		return ftsbodypkTosettleBodyPkMap;
	}

	/**
	 * �õ��ʽ���תagg����pk���˻سи�ԭ��Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementAggVO
	 * @return Map<String, SettlementBodyVO[]> key--SettleheadPk
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, String> convertAggInnerTransferVOsToFtsBodyPkToUnAcceptReasonMap(
			AggInnerTransferVO... aggInnerTransfers) throws BusinessException {
		Map<String, String> tsBodyPkToUnAcceptReasonMap = CmpUtils.makeMap();
		for (AggInnerTransferVO agg : aggInnerTransfers) {
			InnerTransferBVO[] bodys = (InnerTransferBVO[]) agg.getChildrenVO();
			for (InnerTransferBVO body : bodys) {
				tsBodyPkToUnAcceptReasonMap.put(body.getPrimaryKey(), body.getUnacceptreason());
			}
		}
		return tsBodyPkToUnAcceptReasonMap;
	}

	/**
	 * �õ�ҵ�񵥾ݱ���pk�ͽ������pk�Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementAggVO
	 * @return Map<String, SettlementBodyVO[]> key--SettleheadPk
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, String> convertSettlementAggVOToBusiBodyPkToSettleBodyPKMap(SettlementAggVO agg)
			throws BusinessException {
		Map<String, String> busibodypkTosettleBodyPkMap = CmpUtils.makeMap();
		SettlementBodyVO[] bodys = (SettlementBodyVO[]) agg.getChildrenVO();
		for (SettlementBodyVO body : bodys) {
			busibodypkTosettleBodyPkMap.put(body.getPk_billdetail(), body.getPk_detail());
		}
		return busibodypkTosettleBodyPkMap;
	}

	/**
	 * �õ��������ͺͽ������pk�Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementAggVO
	 *            []
	 * @return Map<String, List<SettlementAggVO>> key--pk_tradertype
	 *         ,value--List<SettlementAggVO>
	 */
	public static Map<String, List<SettlementAggVO>> convertSettlementAggVOsToPk_tradertypeToSettlementAggVOListMap(
			SettlementAggVO... aggs) throws BusinessException {
		Map<String, List<SettlementAggVO>> Pk_tradertypeToSettlementAggVOListMap = CmpUtils.makeMap();
		SettlementHeadVO head = null;
		String splitStr = "@@@@@@@@@";
		for (SettlementAggVO settlementAggVO : aggs) {
			head = ((SettlementHeadVO) settlementAggVO.getParentVO());
			String key = head.getPk_tradetype() + splitStr + head.getPk_group();
			if (!head.getIsindependent().equals(UFBoolean.TRUE) && head.getPk_tradetype() == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
						"03607set-0469")/* @res "������Ϣȱ�ٽ���������Ϣ��" */);
			} else if (key != null) {
				if (Pk_tradertypeToSettlementAggVOListMap.get(key) == null) {
					List<SettlementAggVO> settleaggList = CmpUtils.makeList();
					settleaggList.add(settlementAggVO);
					Pk_tradertypeToSettlementAggVOListMap.put(key, settleaggList);
				} else {
					Pk_tradertypeToSettlementAggVOListMap.get(key).add(settlementAggVO);
				}
			}
		}
		return Pk_tradertypeToSettlementAggVOListMap;
	}

	/**
	 * �õ�ҵ���ͷpk�ͽ���Agg�Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementBodyVO
	 * @return Map<String, SettlementAggVO[]> key--SettleheadPk
	 *         ,value--SettlementAggVO
	 */
	public static Map<String, SettlementAggVO> convertSettlementAggVOToBusiHeadPkToAggMap(SettlementAggVO... aggs)
			throws BusinessException {
		Map<String, SettlementAggVO> headpkToAggMap = CmpUtils.makeMap();
		for (SettlementAggVO agg : aggs) {
			SettlementHeadVO head = (SettlementHeadVO) agg.getParentVO();
			headpkToAggMap.put(head.getPk_busibill(), agg);
		}
		return headpkToAggMap;
	}

	/**
	 * �õ������ͷpk�ͽ���Agg�Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementAggVO
	 * @return Map<String, SettlementAggVO[]> key--SettleheadPk
	 *         ,value--SettlementAggVO
	 */
	public static Map<String, SettlementAggVO> convertSettlementAggVOToHeadPkToAggMap(SettlementAggVO... aggs)
			throws BusinessException {
		Map<String, SettlementAggVO> headpkToAggMap = CmpUtils.makeMap();
		for (SettlementAggVO agg : aggs) {
			headpkToAggMap.put(agg.getParentVO().getPrimaryKey(), agg);
		}
		return headpkToAggMap;
	}

	/**
	 * �õ�AggregatedValueObject��ͷpk��AggregatedValueObject�Ķ�Ӧ��ϵ
	 * 
	 * @param AggregatedValueObject
	 * @return Map<String, SettlementAggVO[]> key--SettleheadPk
	 *         ,value--SettlementAggVO
	 */
	public static Map<String, AggregatedValueObject> convertAggregatedValueObjectsToHeadPkToAggMap(
			AggregatedValueObject... aggs) throws BusinessException {
		Map<String, AggregatedValueObject> headpkToAggMap = CmpUtils.makeMap();
		for (AggregatedValueObject agg : aggs) {
			headpkToAggMap.put(agg.getParentVO().getPrimaryKey(), agg);
		}
		return headpkToAggMap;
	}

	/**
	 * �õ���������ҵ������pk��Ӧ��ϵ
	 * 
	 * @param SettlementBodyVO
	 * @return Map<String, String> key--SettleBodyPk ,value--BusiBodyPk
	 */
	public static Map<String, String> convertSettlementBodyVOToSettleBodyPkToBusiBodyPkMap(SettlementBodyVO... bodys) {
		Map<String, String> settlebodyPkTobusiBodyPkMap = CmpUtils.makeMap();
		for (SettlementBodyVO body : bodys) {
			settlebodyPkTobusiBodyPkMap.put(body.getPk_detail(), body.getPk_billdetail());
		}
		return settlebodyPkTobusiBodyPkMap;
	}

	/**
	 * �õ��������pk�ͽ������Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementBodyVO
	 * @return Map<String, SettlementBodyVO[]> key--pk_detail
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, SettlementBodyVO> convertSettlementAggVOToBodyVOPkToBodyVOMap(SettlementAggVO... aggs)
			throws BusinessException {
		Map<String, SettlementBodyVO> bodypkToBodyVOMap = CmpUtils.makeMap();

		for (SettlementAggVO agg : aggs) {
			SettlementBodyVO[] bodys = (SettlementBodyVO[]) agg.getChildrenVO();
			for (SettlementBodyVO body : bodys) {
				bodypkToBodyVOMap.put(body.getPrimaryKey(), body);
			}
		}
		return bodypkToBodyVOMap;
	}

	/**
	 * �жϽ�����Ϣ�Ƿ�����ڲ��˻�
	 * 
	 * @param SettlementBodyVO
	 */
	public static boolean isExistInnerAccount(SettlementAggVO agg) {
		SettlementBodyVO[] bodys = (SettlementBodyVO[]) agg.getChildrenVO();
		for (SettlementBodyVO settlementBodyVO : bodys) {
			if (settlementBodyVO.getPk_Inneraccount() != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �жϽ�����Ϣ���嶼���ڲ��ʻ�
	 * 
	 * @param SettlementBodyVO
	 */
	public static boolean isAllInnerAccount(SettlementAggVO agg) {
		SettlementBodyVO[] bodys = (SettlementBodyVO[]) agg.getChildrenVO();
		for (SettlementBodyVO settlementBodyVO : bodys) {
			if (settlementBodyVO.getPk_Inneraccount() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ͨ���������͵õ����ݴ���
	 * **/
	public static String getDjdlByPk_tradetype(String pk_tradetype, String pkGroup) throws BusinessException {
		String djdl = null;
		String billtype = getbilltype(pk_tradetype, pkGroup);
		if ("F4".equals(billtype)) {
			djdl = "sj";
		} else if ("F5".equals(billtype)) {
			djdl = "fj";
		} else if ("F6".equals(billtype)) {
			djdl = "hj";
		} else if ("F2".equals(billtype)) {
			djdl = "sk";
		} else if ("F3".equals(billtype)) {
			djdl = "fk";
		} else if ("264X".equals(billtype)||"263X".equals(billtype)) {
			djdl = "bx";
		}
		return djdl;
	}

	/**
	 * �õ������������жԷ������˻�
	 * */
	public static List<String> getOppAccountList(SettlementBodyVO[] bodys) {
		List<String> oppaccountList = ArrayUtil.makeList();
		for (SettlementBodyVO body : bodys) {
			if (body.getPk_oppaccount() != null) {
				oppaccountList.add(body.getPk_oppaccount());
			}
		}
		return oppaccountList;
	}

	/**
	 * �õ��������������ڲ������˻�
	 * */
	public static List<String> getInnerAccountList(SettlementBodyVO[] bodys) {
		List<String> inneraccountList = ArrayUtil.makeList();
		for (SettlementBodyVO body : bodys) {
			if (body.getPk_Inneraccount() != null) {
				inneraccountList.add(body.getPk_Inneraccount());
			}
		}
		return inneraccountList;
	}

	/**
	 * �õ��������������ڲ���Ա��λ
	 * */
	public static List<String> getInnerorgList(SettlementBodyVO[] bodys) {
		List<String> innerorgList = ArrayUtil.makeList();
		for (SettlementBodyVO body : bodys) {
			if (body.getPk_innerorg() != null) {
				innerorgList.add(body.getPk_innerorg());
			}
		}
		return innerorgList;
	}

	/**
	 * �����ж�ǩ�ֺͷ�ǩ��ʱ�Ƿ񴫻��ƽ̨��Ч���ݻ��Ƿ���Ч���� ��ǩ�ֺͷ�ǩ��ԭ����ǰ���ã������ʽ𵥾�ת�˳ɹ����������������ʹ�ô˷����ж�
	 * ϸ������������ڲ��˻�������ʾ���ݲ��������ʽ���ص����̣�ǩ��ʱ��Ч����������ڲ��˻�������ʾ���ݲ����ʽ����̡�
	 * */
	@Deprecated
	public static boolean judgeIsSendTODapAtSignPointBySettlementAgg(SettlementAggVO settleagg)
			throws BusinessException {

		SettlementHeadVO head = (SettlementHeadVO) settleagg.getParentVO();
		// SettlementBodyVO[] bodys
		// =(SettlementBodyVO[])settleagg.getChildrenVO();
		boolean isexistInnerAccount = SettleUtils.isExistInnerAccount(settleagg);
		if (!isexistInnerAccount) {// ���ʽ����̣�ǩ��ʱ�����ƽ̨
			return true;
		}
		// �ʽ�����
		String pkFtsbill = head.getPk_ftsbill();
		boolean iscommpay = head.getIscommpay() == null ? false : head.getIscommpay().booleanValue();
		boolean isindependent = head.getIsindependent() == null ? false : head.getIsindependent().booleanValue();
		String djdl = SettleUtils.getDjdlByPk_tradetype(head.getPk_tradetype(), head.getPk_group());
		if (pkFtsbill == null) {// ��Ա��λ��������ε��ݣ�ǩ��ʱ�������ƽ̨��ת�˳ɹ�ʱ�����ƽ̨
			return false;
		} else if (pkFtsbill != null) {
			// ��Ա��λ���� ���� �ǳи���ת�˳ɹ�ʱ�������������ȷ�Ϻ�ǩ��ʱ��
			if (!isindependent && !iscommpay) {
				return true;
			}

			// �ʽ���֯���� �ϡ��� �ηǳи� ��ת�˳ɹ�ʱ�������������ȷ�Ϻ�ǩ��ʱ��
			if (isindependent && !iscommpay) {
				return true;
			}

			// ��Ա��λ���� ����(������)�и������ǩ��ʱ������ת�˳ɹ�ʱ��(�缯�ţ��ǿ缯��ͬ)
			// �ʽ���֯���� ����(������) �и� ���ǩ��ʱ������ת�˳ɹ�ʱ��
			if (iscommpay && ("fj".equals(djdl) || "fk".equals(djdl))) {
				return false;
			}

			// �ʽ���֯���� ���Σ��տ��ࣩ �и� ת�˳ɹ�ʱ�������������ȷ�Ϻ�ǩ��ʱ��
			if (isindependent && iscommpay && ("sj".equals(djdl) || "sk".equals(djdl))) {
				return true;
			}

		}
		return true;

	}

	public static boolean judgeIsSendToDapBySettlementAgg(Boolean isSignPoing, SettlementAggVO settleagg)
			throws BusinessException {
		SettlementHeadVO head = (SettlementHeadVO) settleagg.getParentVO();
		boolean isexistInnerAccount = SettleUtils.isExistInnerAccount(settleagg);
		if (head.getPk_tradetype() == null) {
			return false;
		}
		String billtype = getbilltype(head.getPk_tradetype(), head.getPk_group());
		if (!isexistInnerAccount) {// ���ʽ����̣�ǩ��ʱ�����ƽ̨
			// �жϲ���
			Map<String, String> dapPara = SettleUtils.getDAPPara(settleagg);

			if (billtype.equals("F4") && billtype.equals("F5")) {
				// �տ���㵥��������㵥���ݲ�������
				String dapStr = dapPara.get(head.getPk_org() + head.getPk_tradetype());
				return dapStr.equals(CMPSysParamConst.CONST_SINGTODAP);
			} else {
				// ���տ���㵥��������㵥����ͨ����ǩ����Ч
				return true;
			}
		}
		// �ʽ�����
		String pkFtsbill = head.getPk_ftsbill();
		boolean iscommpay = head.getIscommpay() == null ? false : head.getIscommpay().booleanValue();
		boolean isindependent = head.getIsindependent() == null ? false : head.getIsindependent().booleanValue();
		String djdl = SettleUtils.getDjdlByPk_tradetype(head.getPk_tradetype(), head.getPk_group());
		if (pkFtsbill == null) {
			// ��Ա��λ��������ε��ݣ�ǩ��ʱ�������ƽ̨��ת�˳ɹ�ʱ�����ƽ̨
			return false;
		} else if (pkFtsbill != null) {
			// ��Ա��λ���� ���� �ǳи���ת�˳ɹ�ʱ�������������ȷ�Ϻ�ǩ��ʱ��
			if (isSignPoing && !isindependent && !iscommpay) {
				return true;
			}

			// �ʽ���֯���� �ϡ��� �ηǳи� ��ת�˳ɹ�ʱ�������������ȷ�Ϻ�ǩ��ʱ��
			if (isSignPoing && isindependent && !iscommpay) {
				return true;
			}

			// ��Ա��λ���� ����(������)�и������ǩ��ʱ������ת�˳ɹ�ʱ��(�缯�ţ��ǿ缯��ͬ)
			// �ʽ���֯���� ����(������) �и� ���ǩ��ʱ������ת�˳ɹ�ʱ��
			if (isSignPoing && iscommpay && ("fj".equals(djdl) || "fk".equals(djdl) || "bx".equals(djdl))) {
				return false;
			}

			// �ʽ���֯���� ���Σ��տ��ࣩ �и� ת�˳ɹ�ʱ�������������ȷ�Ϻ�ǩ��ʱ��
			if (isindependent && iscommpay && ("sj".equals(djdl) || "sk".equals(djdl))) {
				return true;
			}

		}
		return true;
	}

	/**
	 * @return Map<String,String> key--��ת����������value--��ת����Ӧ��ҵ����Ϣ������
	 * */
	public static Map<String, String> convertAggInnerTransferVOToFtsBodyPKTobusiBodyPKMap(
			AggInnerTransferVO... aggInnerTransferVOs) {
		Map<String, String> pk_ftsdetailToPk_busidetailMap = new HashMap<String, String>();
		for (AggInnerTransferVO aggInnerTransferVO : aggInnerTransferVOs) {
			InnerTransferBVO[] innerTransferBVOs = aggInnerTransferVO.getItem();
			for (InnerTransferBVO innerTransferBVO : innerTransferBVOs) {
				pk_ftsdetailToPk_busidetailMap.put(innerTransferBVO.getPk_innertransfer_b(),
						innerTransferBVO.getPk_sourcebillrowid());
			}
		}
		return pk_ftsdetailToPk_busidetailMap;
	}

	/**
	 * @return Map<String,String> key--��ת����������value--��Ӧ�Ľ�����Ϣ������
	 * */
	public static Map<String, String> convertSettlementAggVOsToFtsBodyPKToSettleBodyPKMap(
			SettlementAggVO... settlementAggVOs) {
		Map<String, String> pk_ftsdetailToPk_detailMap = new HashMap<String, String>();
		for (SettlementAggVO settlementAggVO : settlementAggVOs) {
			SettlementBodyVO[] settlementBodyVOs = (SettlementBodyVO[]) settlementAggVO.getChildrenVO();
			for (SettlementBodyVO settlementBodyVO : settlementBodyVOs) {
				pk_ftsdetailToPk_detailMap.put(settlementBodyVO.getPk_ftsbilldetail(), settlementBodyVO.getPk_detail());
			}
		}
		return pk_ftsdetailToPk_detailMap;
	}

	/**
	 * @return Map<String,String> key--��ת����������value--��ת����Ӧ�Ľ�����Ϣ������
	 * */
	public static Map<String, String> convertAggInnerTransferVOToFtsBodyPKToSettleBodyPKMap(
			SettlementAggVO upSettleAgg, AggInnerTransferVO... aggInnerTransferVOs) throws BusinessException {
		Map<String, String> pk_ftsdetailToPk_busidetailMap = convertAggInnerTransferVOToFtsBodyPKTobusiBodyPKMap(aggInnerTransferVOs);
		// Map<String,String>
		// pk_busidetailToPk_detailMap=convertSettlementAggVOToBusiBodyPkToSettleBodyPKMap(upSettleAgg);
		Map<String, String> pk_ftsdetailToPk_detailMap = new HashMap<String, String>();
		Set<String> ftskeySet = pk_ftsdetailToPk_busidetailMap.keySet();
		for (Iterator iterator = ftskeySet.iterator(); iterator.hasNext();) {
			String pk_ftsdetail = (String) iterator.next();

			// ��ת���������ֽ������PK�ǽ�����Ϣ��PK
			String pk_detail = pk_ftsdetailToPk_busidetailMap.get(pk_ftsdetail);
			// String pk_detail=pk_busidetailToPk_detailMap.get(pk_billdetail);
			pk_ftsdetailToPk_detailMap.put(pk_ftsdetail, pk_detail);
		}
		return pk_ftsdetailToPk_detailMap;
	}

	// �����������
	public static void fillSettlementBodyVOsJeAndHL(SettlementBodyVO[] bodys) throws BusinessException {
		for (SettlementBodyVO settlementBodyVO : bodys) {
			fillSettlementBodyJeAndHL(settlementBodyVO);
		}
	}

	/**
	 * 
	 * @param body
	 * @param local
	 */
	public static void fillSettlementBodyLocol(SettlementBodyVO body, UFDouble local) {
		// @wuzhwa ȡ���������ƣ�
		// TODO �Ƿ���Ҫ���ݻ��ʼ���
		// if
		// (body.getPay()!=null&&body.getPay().compareTo(UFDouble.ZERO_DBL)>0) {
		if (body.getPay() != null) {
			body.setPaylocal(local);

		} else if (body.getReceive() != null) {
			body.setReceivelocal(local);
		}
	}

	public static void fillSettlementBodyJeAndHL(SettlementBodyVO body) throws BusinessException {
		if (CmpUtils.isObjectNull(body.getBilldate())) {
			// ��������ΪNull����������
			return;
		}
		UFDouble[] doubles = computeYFB(body);
		fillSettlementBodyLocol(body, doubles[2]);
		body.setLocalrate(doubles[4]);
	}

	/**
	 * ¼�뵥�ݵ�ʱ��ԭ�������άϵ�Ź��еĹ�ϵ�Լ����� �����������ʵ�ʵ������ά����ԭ�������۸����۱� ���ʵ�ҵ���ϵ�߼�
	 * 
	 * @return [0]ybje[1]fbje[2]bbje[3]zfhv[4]zbhv
	 * 
	 *         ���е�һЩ��Ŀ���ܿգ�λ��ʼ�ղ���
	 * 
	 * @throws BusinessException
	 */
	public static UFDouble[] computeYFB(SettlementBodyVO body) throws BusinessException {

		String pkOrg = body.getPk_org();
		String pkCurrtype = body.getPk_currtype();

		UFDouble primal = getSettlementBodyPrimal(body);
		UFDouble zbhv = getRate(body);
		UFDate date = body.getBilldate();
		return Currency.computeYFB(pkOrg, 1, pkCurrtype, primal, null, null, null, zbhv, date);
	}

	public static UFDouble getSettlementBodyPrimal(SettlementBodyVO body) {
		UFDouble primal = null;
		if (body.getPay() != null && body.getPay().compareTo(UFDouble.ZERO_DBL) > 0) {
			primal = body.getPay();
		} else if (body.getReceive() != null && body.getReceive().compareTo(UFDouble.ZERO_DBL) > 0) {
			primal = body.getReceive();
		}
		return primal;
	}

	public static UFDouble getRate(SettlementBodyVO body) throws BusinessException {
		if (body.getPk_org() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0612")/* @res "������֯��ȡ����ʱ����ֵ֯����Ϊ��" */);
		}
		if (body.getPk_currtype() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0613")/* @res "��ȡ����ʱ������ֵ����Ϊ��" */);
		}

		String pkOrg = body.getPk_org();
		String pkCurrtype = body.getPk_currtype();
		UFDate billdate = body.getBilldate();

		return TMCurrencyUtil.getOrgCurrRate(pkOrg, pkCurrtype, billdate);
	}

	// ����ͷ��ԭ�Һͱ���
	public static void fillSettlementHeadPrimalAndOrglocalField(SettlementAggVO settleaggvo) {
		SettlementHeadVO headVO = (SettlementHeadVO) settleaggvo.getParentVO();
		SettlementBodyVO[] bodys = (SettlementBodyVO[]) settleaggvo.getChildrenVO();
	
		int paynum = 0;
		int recnum = 0;
		UFDouble payPrimal = UFDouble.ZERO_DBL;
		UFDouble recPrimal = UFDouble.ZERO_DBL;

		UFDouble payOrgLocal = UFDouble.ZERO_DBL;
		UFDouble recOrgLocal = UFDouble.ZERO_DBL;

		UFDouble payGroupLocal = UFDouble.ZERO_DBL;
		UFDouble recGroupLocal = UFDouble.ZERO_DBL;

		UFDouble payGlobalLocal = UFDouble.ZERO_DBL;
		UFDouble recGlobalLocal = UFDouble.ZERO_DBL;

		UFDouble primal = null;
		UFDouble orgLocal = null;
		UFDouble groupLocal = null;
		UFDouble globalLocal = null;
		for (SettlementBodyVO settlementBodyVO : bodys) {
			if (settlementBodyVO.getPay() != null) {
				payPrimal = payPrimal.add(settlementBodyVO.getPay());
				if (!CmpUtils.isObjectNull(settlementBodyVO.getBilldate())) {
					payOrgLocal = payOrgLocal.add(settlementBodyVO.getPaylocal()==null?UFDouble.ZERO_DBL:settlementBodyVO.getPaylocal());
					payGroupLocal = payGroupLocal.add(settlementBodyVO.getGrouppaylocal()==null?UFDouble.ZERO_DBL:settlementBodyVO.getGrouppaylocal());
					payGlobalLocal = payGlobalLocal.add(settlementBodyVO.getGlobalpaylocal()==null?UFDouble.ZERO_DBL:settlementBodyVO.getGlobalpaylocal());
				}
				paynum++;
			} else if (settlementBodyVO.getReceive() != null) {
				recPrimal = recPrimal.add(settlementBodyVO.getReceive());
				if (!CmpUtils.isObjectNull(settlementBodyVO.getBilldate())) {
					recOrgLocal = recOrgLocal.add(settlementBodyVO.getReceivelocal()==null?UFDouble.ZERO_DBL:settlementBodyVO.getReceivelocal());
					recGroupLocal = recGroupLocal.add(settlementBodyVO.getGroupreceivelocal()==null?UFDouble.ZERO_DBL:settlementBodyVO.getGroupreceivelocal());
					recGlobalLocal = recGlobalLocal.add(settlementBodyVO.getGlobalreceivelocal()==null?UFDouble.ZERO_DBL:settlementBodyVO.getGlobalreceivelocal());
				}
				recnum++;
			}
		}
		if (paynum != 0 && recnum == 0) {
			primal = payPrimal;
			orgLocal = payOrgLocal;
			groupLocal = payGroupLocal;
			orgLocal = payGlobalLocal;
		} else if (paynum == 0 && recnum != 0) {
			primal = recPrimal;
			orgLocal = recOrgLocal;
			groupLocal = recGroupLocal;
			globalLocal = recGlobalLocal;
		} else if (paynum != 0 && recnum != 0) {
			primal = recPrimal;
			orgLocal = recOrgLocal;
			groupLocal = recGroupLocal;
			globalLocal = recGroupLocal;
		}
		headVO.setPrimal(primal);
		headVO.setOrglocal(orgLocal);
		headVO.setGrouplocal(groupLocal);
		headVO.setGlobaloacl(globalLocal);
	}

	/**
	 * �õ�ҵ�񵥾ݱ���pk�ͽ������Ķ�Ӧ��ϵ ��������ת��û�в���ҵ������
	 * 
	 * @param SettlementAggVO
	 * @return Map<String, SettlementBodyVO[]> key--busibodyPk
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, SettlementBodyVO> convertSettlementAggVOToBusiBodyPkToSettleBodyMap(SettlementAggVO agg)
			throws BusinessException {
		Map<String, SettlementBodyVO> busibodypkTosettleBodyPkMap = CmpUtils.makeMap();
		SettlementBodyVO[] bodys = (SettlementBodyVO[]) agg.getChildrenVO();
		for (SettlementBodyVO body : bodys) {
			busibodypkTosettleBodyPkMap.put(body.getPk_billdetail(), body);
		}
		return busibodypkTosettleBodyPkMap;
	}

	/**
	 * �õ�ҵ�񵥾ݱ���pk�ͽ����������Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementAggVO
	 *            []
	 * @return Map<String, SettlementBodyVO[]> key--busibodyPk
	 *         ,value--SettlementBodyVO[]
	 */
	public static Map<String, SettlementBodyVO[]> convertSettlementAggVOToBusiBodyPkToSettleBodyArrayMap(
			SettlementAggVO... aggs) throws BusinessException {
		Map<String, List<SettlementBodyVO>> busibodypkTosettleBodyListMap = convertSettlementAggVOToBusiBodyPkToSettleBodyListMap(aggs);
		Map<String, SettlementBodyVO[]> busibodypkTosettleBodyArrayMap = CmpUtils.makeMap();
		Set<String> busiBodyPKSet = busibodypkTosettleBodyListMap.keySet();
		for (Iterator iterator = busiBodyPKSet.iterator(); iterator.hasNext();) {
			String pk_billdetail = (String) iterator.next();
			List<SettlementBodyVO> busiBodyPkList = busibodypkTosettleBodyListMap.get(pk_billdetail);
			SettlementBodyVO[] bodys = new SettlementBodyVO[busiBodyPkList.size()];
			busiBodyPkList.toArray(bodys);
			busibodypkTosettleBodyArrayMap.put(pk_billdetail, bodys);
		}
		return busibodypkTosettleBodyArrayMap;
	}

	/**
	 * �õ�ҵ�񵥾ݱ���pk�ͽ����������Ķ�Ӧ��ϵ
	 * 
	 * @param SettlementAggVO
	 *            []
	 * @return Map<String, SettlementBodyVO[]> key--busibodyPk
	 *         ,value--List<SettlementBodyVO>
	 */
	public static Map<String, List<SettlementBodyVO>> convertSettlementAggVOToBusiBodyPkToSettleBodyListMap(
			SettlementAggVO... aggs) throws BusinessException {
		Map<String, List<SettlementBodyVO>> busibodypkTosettleBodyListMap = CmpUtils.makeMap();
		for (SettlementAggVO settlementAggVO : aggs) {
			SettlementBodyVO[] bodys = (SettlementBodyVO[]) settlementAggVO.getChildrenVO();
			for (int i = 0; i < bodys.length; i++) {
				SettlementBodyVO bodyVO = bodys[i];
				List<SettlementBodyVO> bodyList = busibodypkTosettleBodyListMap.get(bodyVO.getPk_billdetail());
				if (bodyList == null) {
					bodyList = CmpUtils.makeList();
					busibodypkTosettleBodyListMap.put(bodyVO.getPk_billdetail(), bodyList);
				}
				bodyList.add(bodyVO);
			}
		}
		return busibodypkTosettleBodyListMap;
	}

	/**
	 * ����ʹ��Ԫ���ݵĳ־ò�
	 * 
	 * @param SettlementAggVO
	 *            []
	 * @return NCObject[]
	 */
	public static NCObject[] convertSettlementAggVOsToNCObjects(SettlementAggVO... aggs) {
		List<NCObject> ncObjList = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggs) {
			NCObject ncobj = NCObject.newInstance(settlementAggVO);
			ncObjList.add(ncobj);
		}
		NCObject[] ncObjs = new NCObject[ncObjList.size()];
		ncObjList.toArray(ncObjs);
		return ncObjs;
	}

	/**
	 * �����ƽ̨ǰ����
	 * */
	public static BusiStateChangeVO[] convertSettlementAggVOToBusiStateChangeVOs(SettlementAggVO... settleAggs)
			throws BusinessException {
		List<BusiStateChangeVO> busiStateChangeVOList = CmpUtils.makeList();
		for (SettlementAggVO aggVO : settleAggs) {
			BusiInfo busiInfo = SettleUtils.convertSettleBeanToBusiInfo(aggVO);
			busiInfo.setBudgetCheck(aggVO.isBudgetCheck());
			busiInfo.setHasZjjhCheck(aggVO.isHasZjjhCheck());
			busiInfo.setSettle(aggVO.isSettle());
			busiInfo.setJkCheck(aggVO.isErmCheck());
			Map<String, List<SettlementBodyVO>> busiBodyPkToSettleBodyListMap = SettleUtils
					.convertSettlementAggVOToBusiBodyPkToSettleBodyListMap(aggVO);
			// ��ȡ���Դ�ƽ̨�ĵ���
			// if
			// (SettleUtils.judgeIsSendTODapAtSignPointBySettlementAgg(aggVO)) {
			BusiStateChangeVO busiStateChangeVO = new BusiStateChangeVO();
			busiStateChangeVO.setBusiInfo(busiInfo);

			busiStateChangeVO.setDetailMap(busiBodyPkToSettleBodyListMap);
			// }
			busiStateChangeVOList.add(busiStateChangeVO);
		}
		return busiStateChangeVOList.toArray(new BusiStateChangeVO[] {});
	}

	/**
	 * ���ݿ����ж��ǿͻ����ǹ�Ӧ������
	 * 
	 * @param pk_trader
	 * @return
	 * @throws BusinessException
	 */
	public static int getKeShangTypeByOrg(String pk_org, Integer direction) throws BusinessException {
		ICustsupPubService service = CmpInterfaceProxy.INSTANCE.getCustsupPubService();
		String pk_trader = null;
		if (direction.equals(Direction.PAY.VALUE)) {
			// Ĭ�Ϲ�Ӧ��
			pk_trader = service.queryCustsupPkByOrgPk(pk_org, true);
			if (pk_trader != null) {
				return CmpConst.TradeObjType_SUPPLIER;
			}
			pk_trader = service.queryCustsupPkByOrgPk(pk_org, false);
			if (pk_trader != null) {
				return CmpConst.TradeObjType_CUSTOMER;
			}
		} else {
			// Ĭ�Ͽͻ�
			pk_trader = service.queryCustsupPkByOrgPk(pk_org, false);
			if (pk_trader != null) {
				return CmpConst.TradeObjType_CUSTOMER;
			}
			pk_trader = service.queryCustsupPkByOrgPk(pk_org, true);
			if (pk_trader != null) {
				return CmpConst.TradeObjType_SUPPLIER;
			}
		}

		return CmpConst.TradeObjType_Never;
	}

	/**
	 * ���ݿ����ж��ǿͻ����ǹ�Ӧ������
	 * 
	 * @param pk_trader
	 * @return
	 * @throws BusinessException
	 */
	public static String getKeShangByOrg(String pk_org) throws BusinessException {
		ICustsupPubService service = CmpInterfaceProxy.INSTANCE.getCustsupPubService();
		String pk_trader = service.queryCustsupPkByOrgPk(pk_org, false);
		if (pk_trader != null) {
			return pk_trader;
		}
		pk_trader = service.queryCustsupPkByOrgPk(pk_org, true);
		if (pk_trader != null) {
			return pk_trader;
		}
		return null;
	}
	public static void handBody4Tradername(String pk_org, Integer direction, SettlementBodyVO... settlementBodyVOs)
	throws BusinessException {
		handBody4Tradername(false,pk_org, direction, settlementBodyVOs);
	}
	/**
	 * �Ա���Ķ������ƽ��д���
	 * 
	 * @param pk_org
	 * @param settlementBodyVOs
	 * @throws BusinessException
	 */
	public static void handBody4Tradername(boolean is4Settle,String pk_org, Integer direction, SettlementBodyVO... settlementBodyVOs)
			throws BusinessException {
		Map<String, String> customerMap = null;
		Map<String, String> supplierMap = new HashMap<String, String>();
		Map<String, String> deptMap = new HashMap<String, String>();
		Map<String, String> psndocMap = new HashMap<String, String>();
		List<String> customerList = new ArrayList<String>();
		List<String> supplierList = new ArrayList<String>();
		List<String> deptList = new ArrayList<String>();
		List<String> psndocList = new ArrayList<String>();
		for (SettlementBodyVO bodyvo : settlementBodyVOs) {
			if (bodyvo.getTradertype() == null) {
				bodyvo.setTradertype(CmpConst.TradeObjType_KeShang);
			}
		}
		for (SettlementBodyVO bodyvo : settlementBodyVOs) {
			if (bodyvo.getTradertype() == null || bodyvo.getPk_trader() == null) {
				// ��
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_KeShang) {
				// ����ⲿ����Ϊ���̣����п��ܻ��¿ͻ���Ҳ���ܻ��ǹ�Ӧ�̣����������Ҫ����
				customerList.add(bodyvo.getPk_trader());
				supplierList.add(bodyvo.getPk_trader());

			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_CUSTOMER) {
				// �ͻ�
				customerList.add(bodyvo.getPk_trader());
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SUPPLIER) {
				// ��Ӧ��
				supplierList.add(bodyvo.getPk_trader());
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Department) {
				// ����
				deptList.add(bodyvo.getPk_trader());
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Person) {
				// ����
				psndocList.add(bodyvo.getPk_trader());
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SanHu) {
				// ɢ��
				customerList.add(bodyvo.getPk_trader());
				supplierList.add(bodyvo.getPk_trader());
			}

		}
		// ȡֵ
		try {
			// �ͻ�
			if (customerList.size() > 0) {
//				customerMap = new HashMap<String, String>();
				customerMap = getCustomerVOS(customerList.toArray(new String[] {}));
//				CustomerVO[] customervos = NCLocator.getInstance().lookup(ICustomerPubService.class)
//						.getCustomerVO(customerList.toArray(new String[] {}), null);
//				if (!CmpUtils.isListNull(customervos)) {
//					for (CustomerVO customerVO : customervos) {
//						if (customerVO == null) {
//							continue;
//						}
//						customerMap.put(
//								customerVO.getPrimaryKey(),
//								String.valueOf(customerVO.getAttributeValue(CustomerVO.NAME
//										+ CmpUtils.getCurrentLangSeq())));
//					}
//				}
			}
			// ��Ӧ��
			if (supplierList.size() > 0) {
				supplierMap = new HashMap<String, String>();
				SupplierVO[] suppliervos = getSupplierpubService().getSupplierVO(supplierList.toArray(new String[] {}),
						null);
				if (!CmpUtils.isListNull(suppliervos)) {
					for (SupplierVO supplierVO : suppliervos) {
						if (supplierVO == null) {
							continue;
						}

						supplierMap.put(supplierVO.getPrimaryKey(), VOUtil
								.getMultiLangText(supplierVO, SupplierVO.NAME));
					}
				}
			}
			// ����
			if (deptList.size() > 0) {
				deptMap = new HashMap<String, String>();
				DeptVO[] deptVOs = NCLocator.getInstance().lookup(IDeptQryService.class).queryAllDeptVOsByOrgID(pk_org);
				if (!CmpUtils.isListNull(deptVOs)) {
					for (DeptVO deptVO : deptVOs) {
						if (deptVO == null) {
							continue;
						}
						deptMap.put(deptVO.getPrimaryKey(), VOUtil.getMultiLangText(deptVO, DeptVO.NAME));
					}
				}
			}
			// ��Ա
			if (psndocList.size() > 0) {
				psndocMap = new HashMap<String, String>();
				PsndocVO[] psndocvos = NCLocator
						.getInstance()
						.lookup(IPsndocQueryService.class)
						.queryPsndocByPks(psndocList.toArray(new String[] {}),
								new String[] { PsndocVO.PK_PSNDOC, PsndocVO.NAME, PsndocVO.NAME2, PsndocVO.NAME3, PsndocVO.NAME4, PsndocVO.NAME5, PsndocVO.NAME6 });
				if (!CmpUtils.isListNull(psndocvos)) {
					for (PsndocVO psndocVO : psndocvos) {
						if (psndocVO == null) {
							continue;
						}
						psndocMap.put(psndocVO.getPrimaryKey(), VOUtil.getMultiLangText(psndocVO, PsndocVO.NAME));
					}
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		// �Ա�����д���
		for (SettlementBodyVO bodyvo : settlementBodyVOs) {

			if (bodyvo.getTradertype() == null) {
				// ��
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_KeShang) {

				if (direction.equals(Direction.PAY.VALUE)) {

					// Ĭ����ѡ��Ӧ��
					if (supplierMap != null && supplierMap.size() > 0) {
						bodyvo.setTradertype(CmpConst.TradeObjType_SUPPLIER);
						if (bodyvo.getTradername() == null) {
							bodyvo.setTradername(supplierMap.get(bodyvo.getPk_trader()));
						}
						continue;
					}
					// �п���Ҳ���ǿͻ�
					if (customerMap != null && customerMap.size() > 0) {
						bodyvo.setTradertype(CmpConst.TradeObjType_CUSTOMER);

						if (bodyvo.getTradername() == null) {
							bodyvo.setTradername(customerMap.get(bodyvo.getPk_trader()));
						}
						continue;
					}
				} else if (direction.equals(Direction.REC.VALUE)) {
					// Ĭ����ѡ��ͻ�
					if (customerMap != null && customerMap.size() > 0) {
						bodyvo.setTradertype(CmpConst.TradeObjType_CUSTOMER);
						if (bodyvo.getTradername() == null) {
							bodyvo.setTradername(customerMap.get(bodyvo.getPk_trader()));
						}
						continue;
					}
					// �п���Ҳ���ǹ�Ӧ��
					// ��Ӧ��
					if (supplierMap != null && supplierMap.size() > 0) {
						bodyvo.setTradertype(CmpConst.TradeObjType_SUPPLIER);
						if (bodyvo.getTradername() == null) {
							bodyvo.setTradername(supplierMap.get(bodyvo.getPk_trader()));
						}
						continue;
					}
				}
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_CUSTOMER) {
				// �ͻ�
				if (customerMap != null && customerMap.size() > 0) {
					if (bodyvo.getTradername() == null) {
						bodyvo.setTradername(customerMap.get(bodyvo.getPk_trader()));
					}
					// bodyvo.setTradertype(CmpConst.TradeObjType_CUSTOMER);
				}
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SUPPLIER) {
				// ��Ӧ��
				if (supplierMap != null && supplierMap.size() > 0) {
					// bodyvo.setTradertype(CmpConst.TradeObjType_SUPPLIER);
					if (bodyvo.getTradername() == null) {
						bodyvo.setTradername(supplierMap.get(bodyvo.getPk_trader()));
					}
				}
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Department) {
				// ����
				if (deptMap != null && deptMap.size() > 0) {
					// bodyvo.setTradertype(CmpConst.TradeObjType_Department);
					if (bodyvo.getTradername() == null) {
						bodyvo.setTradername(deptMap.get(bodyvo.getPk_trader()));
					}
				}
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_Person) {
				// ����
				if (psndocMap != null && psndocMap.size() > 0) {
					// bodyvo.setTradertype(CmpConst.TradeObjType_Person);
					if (bodyvo.getTradername() == null) {
						bodyvo.setTradername(psndocMap.get(bodyvo.getPk_trader()));
					}
				}
			} else if (bodyvo.getTradertype() == CmpConst.TradeObjType_SanHu) {
				// Ĭ����ѡ��ͻ�
				if (customerMap != null && customerMap.size() > 0) {
					if (bodyvo.getTradername() == null) {
						bodyvo.setTradername(customerMap.get(bodyvo.getPk_trader()));
					}
					continue;
				}
				// �п���Ҳ���ǹ�Ӧ��
				// ��Ӧ��
				if (supplierMap != null && supplierMap.size() > 0) {
					if (bodyvo.getTradername() == null) {
						bodyvo.setTradername(supplierMap.get(bodyvo.getPk_trader()));
					}
					continue;
				}
			}
		}
	}
	private static Map<String,String> getCustomerVOS(String ... pks){
		Map<String,String> customerMap = new HashMap<String, String>();
		IGeneralAccessor	accessor = GeneralAccessorFactory.getAccessor(IBDMetaDataIDConst.CUSTSUPPLIER);
		
		for (String pk : pks) {
			BDDataVO bdData = (BDDataVO)accessor.getDocByPk(pk);
			if(bdData != null){
				MultiLangText name = bdData.getName();
				customerMap.put(pk, name.getText(MultiLangContext.getInstance().getCurrentLangSeq()-1));
			}
			
			
		}
		return customerMap;
	}
	/**
	 * ���������֯��汾��Ϣ
	 * 
	 * @param bodys
	 * @throws BusinessException
	 */
	@Deprecated
	public static void handlebodys4ORGDate(CMPaccStatus status, SettlementBodyVO... bodys) throws BusinessException {
		if (CmpUtils.isListNull(bodys)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0614")/* @res "������Ϣ������Ϣ����Ϊ�գ�ת����ʱ����Ϣʧ�ܣ�������Ϣ" */);

		}
		for (SettlementBodyVO settlementBodyVO : bodys) {
			setORGUFDateInfo(status, settlementBodyVO);
		}
	}

	@Deprecated
	private static void setORGUFDateInfo(CMPaccStatus status, SettlementBodyVO settlementBodyVO)
			throws BusinessException {

		UFDateTime ufDateTime = new UFDateTime(System.currentTimeMillis());

		if (settlementBodyVO.getFundformcode() != null
				&& settlementBodyVO.getFundformcode().equals(CmpConst.BANK_DEPOSIT)) {
			UFDateTime orgbusitime = getORGUFDateTimeByOrg(settlementBodyVO.getPk_org(), ufDateTime);

			// �����˻������������˻��ӻ�ʱ�����ж�XXX
			// modify:���ɸ�����֯ʱ������ȡ��ʱ��
			// ҵ������
			if ((status == null || status.equals(CMPaccStatus.SAVEACCOUNT)) && settlementBodyVO.getBilldate() != null) {

				// settlementBodyVO.setOrgbusidate(getORGUFDate(settlementBodyVO.getPk_account(),
				// settlementBodyVO
				// .getBilldate()));
				settlementBodyVO.setOrgbusidate(getORGUFDateByOrg(settlementBodyVO.getPk_org(),
						settlementBodyVO.getBilldate()));
				settlementBodyVO.setOrgbusitime(orgbusitime);
			}

			// ǩ������
			if ((status == null || status.equals(CMPaccStatus.SIGNACCOUNT)) && settlementBodyVO.getSigndate() != null) {
				// settlementBodyVO.setOrgsigndate(getORGUFDate(settlementBodyVO.getPk_account(),
				// settlementBodyVO
				// .getSigndate()));
				settlementBodyVO.setOrgsigndate(getORGUFDateByOrg(settlementBodyVO.getPk_org(),
						settlementBodyVO.getSigndate()));
				settlementBodyVO.setOrgsigntime(orgbusitime);
			}

			// ��������
			if ((status == null || status.equals(CMPaccStatus.SUCCESSACCOUNT))
					&& settlementBodyVO.getTallydate() != null) {
				// settlementBodyVO.setOrgsettledate(getORGUFDate(settlementBodyVO.getPk_account(),
				// settlementBodyVO
				// .getTallydate()));
				settlementBodyVO.setOrgsettledate(getORGUFDateByOrg(settlementBodyVO.getPk_org(),
						settlementBodyVO.getTallydate()));
				settlementBodyVO.setOrgsettletime(orgbusitime);
			}
		} else {
			// �ֽ��˻�����֯ʱ���ҵ��ʱ��һ�£�����ת��
			// FIXIME Ʊ���˻��Ƿ���Ҫ���ݶ�ʱ��
			// Ʊ���˻����ֽ��ʻ�
			// ҵ������
			if ((status == null || status.equals(CMPaccStatus.SAVEACCOUNT)) && settlementBodyVO.getBilldate() != null) {

				settlementBodyVO.setOrgbusidate(UFLiteralDate.getDate(settlementBodyVO.getBilldate().toDate()));
				settlementBodyVO.setOrgbusitime(ufDateTime);
			}

			// ǩ������
			if ((status == null || status.equals(CMPaccStatus.SIGNACCOUNT)) && settlementBodyVO.getSigndate() != null) {
				settlementBodyVO.setOrgsigndate(UFLiteralDate.getDate(settlementBodyVO.getSigndate().toDate()));
				settlementBodyVO.setOrgsigntime(ufDateTime);
			}

			// ��������
			if ((status == null || status.equals(CMPaccStatus.SUCCESSACCOUNT))
					&& settlementBodyVO.getTallydate() != null) {
				settlementBodyVO.setOrgsettledate(UFLiteralDate.getDate(settlementBodyVO.getTallydate().toDate()));
				settlementBodyVO.setOrgsettletime(ufDateTime);
			}
		}
	}

	/**
	 * ���������֯��汾��Ϣ
	 * 
	 * @param bodys
	 * @throws BusinessException
	 */
	// public static void handlebodys4ORGDate(CMPaccStatus status,UFDate date,
	// SettlementBodyVO... bodys) throws BusinessException {
	// if (CmpUtils.isListNull(bodys)) {
	// throw new
	// BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
	// "03607set-0614")/* @res "������Ϣ������Ϣ����Ϊ�գ�ת����ʱ����Ϣʧ�ܣ�������Ϣ" */);
	//
	// }
	// for (SettlementBodyVO settlementBodyVO : bodys) {
	// setORGUFDateInfo(status, settlementBodyVO,date);
	// }
	// }
	// private static void setORGUFDateInfo(CMPaccStatus status,
	// SettlementBodyVO settlementBodyVO,UFDate date)
	// throws BusinessException {
	// if(date==null)
	// return;
	// UFDateTime ufDateTime = new UFDateTime(System.currentTimeMillis());
	//
	// if (settlementBodyVO.getFundformcode() != null
	// && settlementBodyVO.getFundformcode().equals(CmpConst.BANK_DEPOSIT)) {
	// UFDateTime orgbusitime =
	// getORGUFDateTimeByOrg(settlementBodyVO.getPk_org(), ufDateTime);
	//
	// // �����˻������������˻��ӻ�ʱ�����ж�XXX
	// // modify:���ɸ�����֯ʱ������ȡ��ʱ��
	// // ҵ������
	// if ((status == null || status.equals(CMPaccStatus.SAVEACCOUNT))) {
	//
	// settlementBodyVO.setOrgbusidate(getORGUFDateByOrg(settlementBodyVO.getPk_org(),
	// date));
	// settlementBodyVO.setOrgbusitime(orgbusitime);
	// }
	//
	// // ǩ������
	// if ((status == null || status.equals(CMPaccStatus.SIGNACCOUNT)) ) {
	// settlementBodyVO.setOrgsigndate(getORGUFDateByOrg(settlementBodyVO.getPk_org(),
	// date));
	// settlementBodyVO.setOrgsigntime(orgbusitime);
	// }
	//
	// // ��������
	// if ((status == null || status.equals(CMPaccStatus.SUCCESSACCOUNT))) {
	// settlementBodyVO.setOrgsettledate(getORGUFDateByOrg(settlementBodyVO.getPk_org(),
	// date));
	// settlementBodyVO.setOrgsettletime(orgbusitime);
	// }
	// } else {
	// // �ֽ��˻�����֯ʱ���ҵ��ʱ��һ�£�����ת��
	// // FIXIME Ʊ���˻��Ƿ���Ҫ���ݶ�ʱ��
	// // Ʊ���˻����ֽ��ʻ�
	// // ҵ������
	// if ((status == null || status.equals(CMPaccStatus.SAVEACCOUNT)) && date
	// != null) {
	//
	// settlementBodyVO.setOrgbusidate(UFLiteralDate.getDate(date.toDate()));
	// settlementBodyVO.setOrgbusitime(ufDateTime);
	// }
	//
	// // ǩ������
	// if ((status == null || status.equals(CMPaccStatus.SIGNACCOUNT)) && date
	// != null) {
	// settlementBodyVO.setOrgsigndate(UFLiteralDate.getDate(date.toDate()));
	// settlementBodyVO.setOrgsigntime(ufDateTime);
	// }
	//
	// // ��������
	// if ((status == null || status.equals(CMPaccStatus.SUCCESSACCOUNT))
	// && date != null) {
	// settlementBodyVO.setOrgsettledate(UFLiteralDate.getDate(date.toDate()));
	// settlementBodyVO.setOrgsettletime(ufDateTime);
	// }
	// }
	// }
	/**
	 * ��ȡ��֯����
	 * 
	 * @param pk_account
	 * @param date
	 * @return
	 * @throws BusinessException
	 * 
	 */
	@Deprecated
	public static UFLiteralDate getORGUFDate(String pk_account, UFDate date) throws BusinessException {
		if (StringUtil.isEmpty(pk_account) || date == null) {
			// ��ȡ��֯�����쳣
			return null;
		}
		return date.toUFLiteralDate(getTimeZoneBySubAcc(pk_account));
	}

	@Deprecated
	public static UFLiteralDate getORGUFDateByOrg(String pk_org, UFDate date) throws BusinessException {
		if (StringUtil.isEmpty(pk_org) || date == null) {
			// ��ȡ��֯�����쳣
			return null;
		}
		UFLiteralDate convertToOrgDate = null;
		try {
			convertToOrgDate = DateUtil.convertToOrgDate(pk_org, date);
		} catch (Exception e) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0775")/* @res "��ȡ��ǰ��֯ʱ��ʧ�ܣ�����ά����֯��ʱ����Ϣ" */);
		}
		if (convertToOrgDate == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0775")/* @res "��ȡ��ǰ��֯ʱ��ʧ�ܣ�����ά����֯��ʱ����Ϣ" */);
		}
		return convertToOrgDate;
	}

	/**
	 * ��ȡ��֯ʱ��
	 * 
	 * @param pk_account
	 * @param datetime
	 * @return
	 * @throws BusinessException
	 */
	// @Deprecated
	// public static UFDateTime getORGUFDateTime(String pk_account, UFDateTime
	// datetime) throws BusinessException {
	// if (StringUtil.isEmpty(pk_account) || datetime == null) {
	// // ��ȡ��֯ʱ���쳣
	// return null;
	// }
	//
	// TimeZone tz = getTimeZoneBySubAcc(pk_account);
	// return new UFDateTime(datetime.toStdString(tz));
	// }
	@Deprecated
	public static UFDateTime getORGUFDateTimeByOrg(String pk_org, UFDateTime datetime) throws BusinessException {
		if (StringUtil.isEmpty(pk_org) || datetime == null) {
			// ��ȡ��֯ʱ���쳣
			return null;
		}

		// ȡ�ʽ���֯������
		try {
			OrgVO[] orgVOs = NCLocator
					.getInstance()
					.lookup(IOrgUnitPubService.class)
					.getOrgs(new String[] { pk_org },
							new String[] { OrgVO.PK_ORG, OrgVO.PK_VID, OrgVO.PK_GROUP, OrgVO.PK_TIMEZONE });
			if (!CmpUtils.isListNull(orgVOs)) {
				OrgVO orgVO = orgVOs[0];
				TimeZone fundOrgTimeZone = TimezoneUtil.getTimeZone(orgVO.getPk_timezone());
				String orgTallyTimeStr = datetime.toStdString(fundOrgTimeZone);

				UFDateTime orgTallyDatetime = new UFDateTime(orgTallyTimeStr);
				return orgTallyDatetime;
			} else {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
						"03607set-0788")/* @res "�޷��ҵ���֯" */);
			}
		} catch (BusinessException e) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0775")/* @res "��ȡ��ǰ��֯ʱ��ʧ�ܣ�����ά����֯��ʱ����Ϣ" */);
		}
	}

	/**
	 * ���������˻��ӻ���ȡʱ��
	 * 
	 * @param pk_account
	 * @return
	 * @throws BusinessException
	 */
	public static TimeZone getTimeZoneBySubAcc(String pk_account) throws BusinessException {
		// ��ȡ���е�����Ϣ
		BankAccbasVO bankAccbasVO = getAccbasVObySubBankAccPK(pk_account);

		if (bankAccbasVO == null || bankAccbasVO.getPk_bankdoc() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0615")/* @res "���������˻��ӻ���ȡ���е�����Ϣʧ��" */);
		}
		// ���û�ȡʱ���ӿ�
		TimeZone tz = getBankdocPubQueryService().getTimeZoneVoByBankdoc(bankAccbasVO.getPk_bankdoc());
		if (tz == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0616",null,new String[]{bankAccbasVO.getAccname()})/* @res "��ȡ�˻�[{0}]ʱ��ʧ��" */);
		}

		return tz;
	}

	/**
	 * ���������˻��ӻ���ȡ�����˻���Ϣ
	 * 
	 * @param pk_account
	 * @return
	 * @throws BusinessException
	 */
	public static BankAccbasVO getAccbasVObySubBankAccPK(String pk_account) throws BusinessException {
		BankAccSubVO[] bankSubVOS = null;
		BankAccbasVO bankaccbasvo = null;
		// ��ȡ�����˻��ӻ���Ϣ
		if (pk_account != null) {

			bankSubVOS = getAnkAccSubInfoQueryService().querySubInfosByPKs(new String[] { pk_account });
		} else {
			// �˳�
			return null;
		}
		// ��ȡ�����˻���Ϣ
		if (!CmpUtils.isListNull(bankSubVOS)) {
			bankaccbasvo = getBankaccPubQueryService().queryAccbasInfByAccID(bankSubVOS[0].getPk_bankaccbas(), null);

		} else {
			// �˳�
			return null;
		}
		return bankaccbasvo;
	}

	public static IBankaccPubQueryService getBankaccPubQueryService() {
		if (bankaccPubQueryService == null) {
			bankaccPubQueryService = NCLocator.getInstance().lookup(IBankaccPubQueryService.class);
		}
		return bankaccPubQueryService;
	}

	public static IBankAccSubInfoQueryService getAnkAccSubInfoQueryService() {
		if (bankAccSubInfoQueryService == null) {
			bankAccSubInfoQueryService = NCLocator.getInstance().lookup(IBankAccSubInfoQueryService.class);
		}
		return bankAccSubInfoQueryService;
	}

	public static IBankdocPubQueryService getBankdocPubQueryService() {
		if (bankdocPubQueryService == null) {
			bankdocPubQueryService = NCLocator.getInstance().lookup(IBankdocPubQueryService.class);
		}
		return bankdocPubQueryService;
	}

	public static ICustomerPubService getCustomerpubService() {
		if (customerPubService == null) {
			customerPubService = NCLocator.getInstance().lookup(ICustomerPubService.class);
		}
		return customerPubService;
	}

	public static ISupplierPubService getSupplierpubService() {
		if (supplierPubService == null) {
			supplierPubService = NCLocator.getInstance().lookup(ISupplierPubService.class);
		}
		return supplierPubService;
	}
	
	/**
	 * ���ݽ������ͻ�ȡ
	 * 
	 * @param pk_tradetype
	 * @return
	 * @throws BusinessException
	 */
	public static String getbilltype(String pk_tradetype, String pkGroup) throws BusinessException {
		String parentBilltype = null;
		BilltypeVO billType = null;
		if (pkGroup != null) {
			billType = PfDataCache.getBillTypeInfo(pkGroup, pk_tradetype);
		} else {
			billType = PfDataCache.getBillTypeInfo(InvocationInfoProxy.getInstance().getGroupId(), pk_tradetype);
		}
		if (billType == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0467",null,new String[]{pk_tradetype})/* @res "���ݽ�������{0}��ȡ��������ʧ�ܣ������������ã�" */);
		} else {
			parentBilltype = billType.getParentbilltype();
		}
		return parentBilltype;
	}

	/**
	 * @param aggVOs
	 * @param aggvosFromUnit
	 * @param aggvosFrom9999
	 * @return
	 */
	public static void departSettlementAggVOs(List<SettlementAggVO> aggvosFromUnit,
			List<SettlementAggVO> aggvosFrom9999, SettlementAggVO... aggVOs) {
		// List<SettlementAggVO> aggvosFromUnit = CmpUtils.makeList(); // ��λ����
		// List<SettlementAggVO> aggvosFrom9999 = CmpUtils.makeList(); // �ʽ���֯����
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
			if (head.getIsindependent().booleanValue()) {
				aggvosFrom9999.add(settlementAggVO);
			} else {
				aggvosFromUnit.add(settlementAggVO);
			}
		}
	}

	/**
	 * ��ȡ����ƾ֤������Ϣ,Ҳ����Ч���ж�����֮һ�� ������ƾ֤��������Ч��
	 * 
	 * @param aggVOs
	 * @throws BusinessException
	 */
	public static Map<String, String> getDAPPara(SettlementAggVO... aggVOs) throws BusinessException {
		// ��ȡ����ƾ֤������Ϣ
		// ���ڼ�¼����֯�ո�����㵥�����ƾ֤����
		Map<String, String> mapPara = new HashMap<String, String>();
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
			String pk_billtype = SettleUtils.getbilltype(head.getPk_tradetype(), head.getPk_group());
			String key = head.getPk_org() + head.getPk_tradetype();
			if (pk_billtype != null && pk_billtype.equals("F4")) {
				if (!mapPara.containsKey(key)) {
					String cmp36 = SysInit.getParaString(head.getPk_org(), CMPSysParamConst.CMP36);
					if (cmp36 == null
							|| ( !cmp36.equals(CMPSysParamConst.CONST_SINGTODAP)
							&& !cmp36.equals(CMPSysParamConst.CONST_SETTLETODAP))) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
								"03607set-0618")/* @res "����������ݵ�CMP36��������" */);
					}
					mapPara.put(key, cmp36);
				}

			} else if (pk_billtype != null && pk_billtype.equals("F5")) {
				if (!mapPara.containsKey(key)) {
					String cmp37 = SysInit.getParaString(head.getPk_org(), CMPSysParamConst.CMP37);
					if (cmp37 == null
							|| ( !cmp37.equals(CMPSysParamConst.CONST_SINGTODAP)
							&& !cmp37.equals(CMPSysParamConst.CONST_SETTLETODAP))) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
								"03607set-0619")/* @res "����������ݵ�CMP37��������" */);
					}
					mapPara.put(key, cmp37);
				}
			}
		}
		// if(mapPara.size()==0){
		// throw new BusinessException("��ȡ������Ϣʧ�ܣ����鵱ǰ��֯�������ݵ�����ƾ֤������Ϣ����");
		// }
		return mapPara;
	}

	/**
	 * ���ݵ��ݲ��������ж��Ƿ���Ч
	 * 
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	public static UFBoolean isBillEffect(UFBoolean isSign, SettlementAggVO settlementAggVO) throws BusinessException {
		UFBoolean isSignEffect = UFBoolean.TRUE;
		SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
		String pk_billtype = SettleUtils.getbilltype(head.getPk_tradetype(), head.getPk_group());
		if (pk_billtype != null && pk_billtype.equals("F4")) {

			String cmp36 = SysInit.getParaString(head.getPk_org(), CMPSysParamConst.CMP36);
			if (cmp36 == null 
					|| ( !cmp36.equals(CMPSysParamConst.CONST_SINGTODAP)
					&& !cmp36.equals(CMPSysParamConst.CONST_SETTLETODAP))) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
						"03607set-0618")/* @res "����������ݵ�CMP36��������" */);
			}

			isSignEffect = cmp36.equals(CMPSysParamConst.CONST_SINGTODAP) ? UFBoolean.TRUE : UFBoolean.FALSE;
		} else if (pk_billtype != null && pk_billtype.equals("F5")) {

			String cmp37 = SysInit.getParaString(head.getPk_org(), CMPSysParamConst.CMP37);
			if (cmp37 == null
					|| (!cmp37.equals(CMPSysParamConst.CONST_SINGTODAP) && !cmp37
							.equals(CMPSysParamConst.CONST_SETTLETODAP))) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
						"03607set-0619")/* @res "����������ݵ�CMP37��������" */);
			}
			isSignEffect = cmp37.equals(CMPSysParamConst.CONST_SINGTODAP) ? UFBoolean.TRUE : UFBoolean.FALSE;
		}
		if (isSign.equals(isSignEffect)) {
			return UFBoolean.TRUE;
		}
		return UFBoolean.FALSE;
	}

	/**
	 * ������Ч��Ϣ
	 * 
	 * @param isCanEffect
	 *            �Ƿ�ﵽҵ����Ч���������ֹ�����ģ�ǩ�־Ϳ�����Ч�ˣ�����������Ч��
	 * @param aggVOs
	 * @throws BusinessException
	 */
	public static void handleSettleInfoEffect(boolean isSettleOperate, SettlementAggVO... aggVOs)
			throws BusinessException {
		Map<String, String> dapPara = SettleUtils.getDAPPara(aggVOs);
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
			boolean isSettleEffect = true; // ����ʱ��Ч
			if (head.getPk_tradetype() == null) {
				isSettleEffect = false;
			} else if (SettleUtils.getbilltype(head.getPk_tradetype(), head.getPk_group()).equals("F6")) {
				// ���˽��㵥û�в���
				isSettleEffect = false;
			} else if (SettleUtils.getbilltype(head.getPk_tradetype(), head.getPk_group()).equals("F5")
					|| SettleUtils.getbilltype(head.getPk_tradetype(), head.getPk_group()).equals("F4")) {

				String dapStr = dapPara.get(head.getPk_org() + head.getPk_tradetype());
				isSettleEffect = dapStr.equals(CMPSysParamConst.CONST_SETTLETODAP);
			}
			if (isSettleOperate && isSettleEffect) {
				head.setEffectstatus(BusiStatus.Effet.getBillStatusSubKind());
				head.setIsbusieffect(UFBoolean.TRUE);
			} else if (isSettleOperate && isSettleEffect) {

			} else {
				head.setEffectstatus(null);
				head.setIsbusieffect(null);
			}
		}

	}

	/**
	 * FIXME ����������(���н��㣬���޸�)
	 * 
	 * @param busidate
	 * @param aggVOs
	 */
	public static void fillSettleDate(UFDate busidate, SettlementAggVO... aggVOs) {
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
			head.setSettledate(busidate);
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				body.setTallydate(busidate);
			}
		}
	}

	/**
	 * �������ж�������Ϣ
	 * 
	 * @param aggvo
	 * @throws BusinessException
	 */
	public static void fillSettleBankrelated_code(SettlementHeadVO head, SettlementBodyVO[] bodyvos)
			throws BusinessException {
		UFBoolean cmp52 = SysinitAccessor.getInstance().getParaBoolean(head.getPk_org(), CMPSysParamConst.CMP52);
		if (UFBoolean.TRUE.equals(cmp52)) {
			// ŷ��ҵ�񣬸���Ʊ�ݺŴ������˱�ʶ��
			INoteBankrelatedCodeQueryService noteBankrelatedCodeQueryService = NCLocator.getInstance().lookup(INoteBankrelatedCodeQueryService.class);

			List<NoteBankrelatedCodeQueryVO> queryvolist = new ArrayList<NoteBankrelatedCodeQueryVO>();

			Map<String, SettlementBodyVO> map = CmpUtils.makeMap();
			for (SettlementBodyVO childrenVO : bodyvos) {
				if (StringUtils.isNotNullWithTrim(childrenVO.getNotenumber())) {
					NoteBankrelatedCodeQueryVO queryVO = new NoteBankrelatedCodeQueryVO();

					queryVO.setBill_number(childrenVO.getNotenumber());
					queryVO.setPk_notetype(childrenVO.getPk_notetype());
					queryVO.setPk_bankacc(childrenVO.getPk_account());
					
					if(childrenVO.getDirection().equals(Direction.PAY.VALUE)){
						queryVO.setPk_currtype(childrenVO.getPk_currtype_last());
					}else{
						queryVO.setPk_currtype(childrenVO.getPk_currtype());
					}
					queryVO.setPk_org(head.getPk_org());
					String hashkey = queryVO.getPk_org() + queryVO.getBill_number() + queryVO.getPk_notetype()+ queryVO.getPk_bankacc() + queryVO.getPk_currtype();

					map.put(hashkey, childrenVO);
					queryvolist.add(queryVO);
				}
			}

			Map<String, String> noteBankrelatedCode = noteBankrelatedCodeQueryService.getNoteBankrelatedCode(queryvolist);
			if (noteBankrelatedCode != null) {
				for (Map.Entry<String, String> entry : noteBankrelatedCode.entrySet()) {
					SettlementBodyVO superVO = map.get(entry.getKey());
					if (superVO != null) {
						superVO.setBankrelated_code(entry.getValue());
					}

				}
			}	
		}
		// ���OBM��װ�ˣ���ֱ�ӻ�ȡ���ж�����
		if (nc.cmp.utils.CmpQueryModulesUtil.isOBMEnable(head.getPk_group())) {
			List<SettlementBodyVO> bodyList = CmpUtils.makeList();
			for (SettlementBodyVO body : bodyvos) {
				// ��ȡ���ж�����
				if (body.getBankrelated_code() == null) {
					bodyList.add(body);
				}
			}
			String[] obmLogReconciliationCodes = CmpInterfaceProxy.INSTANCE.getObmLogManageService().creatObmLogReconciliationCodes(bodyList.size());
			for (int i = 0; i < bodyList.size(); i++) {
				bodyList.get(i).setBankrelated_code(obmLogReconciliationCodes[i]);
			}
		}
		
	}

	/**
	 * ���ǩ������
	 * 
	 * @param busidate
	 * @param aggVOs
	 */
	public static void fillSignDate(UFDate busidate, SettlementAggVO... aggVOs) {
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
			head.setSigndate(busidate);
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				body.setSigndate(busidate);
			}
		}
	}

	/**
	 * ���÷�����Ϣ
	 * 
	 * @param isIng
	 * @param aggVOs
	 */
	public static void fillSettlementReverseStatus(boolean isIng, SettlementAggVO... aggVOs) {
		// fillSettleDate(null, aggVOs);
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
			head.setSettletype(SettleType.NON_SETTLE.getStatus());
			// head.setIsbusieffect(UFBoolean.FALSE);
			// head.setEffectstatus(isIng
			// ?BusiStatus.EffectHandling.getBillStatusSubKind():BusiStatus.EffectNever.getBillStatusSubKind());
			// head.setSettledate(null);
			if (head.getDirection().equals(Direction.REC.VALUE)) {
				head.setSettlestatus(isIng ? SettleStatus.RECEIVING.getStatus() : SettleStatus.NONESETTLE.getStatus());
			} else if (head.getDirection().equals(Direction.PAY.VALUE)) {
				head.setSettlestatus(isIng ? SettleStatus.PAYING.getStatus() : SettleStatus.NONESETTLE.getStatus());
			} else if (head.getDirection().equals(Direction.CHANGE.VALUE)) {
				head.setSettlestatus(isIng ? SettleStatus.CHANGEING.getStatus() : SettleStatus.NONESETTLE.getStatus());
			}

			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {

				if (head.getDirection().equals(Direction.REC.VALUE)) {
					body.setSettlestatus(isIng ? SettleStatus.RECEIVING.getStatus() : SettleStatus.NONESETTLE
							.getStatus());

				} else if (head.getDirection().equals(Direction.PAY.VALUE)) {
					body.setSettlestatus(isIng ? SettleStatus.PAYING.getStatus() : SettleStatus.NONESETTLE.getStatus());

				} else if (head.getDirection().equals(Direction.CHANGE.VALUE)) {
					body.setSettlestatus(isIng ? SettleStatus.CHANGEING.getStatus() : SettleStatus.NONESETTLE
							.getStatus());
				}
				if (body.getSigndate() != null) {
					body.setTallystatus(CMPaccStatus.SIGNACCOUNT.getStatus());
				} else {
					body.setTallystatus(CMPaccStatus.SAVEACCOUNT.getStatus());
				}
			}
		}

	}

	/**
	 * ���AGGVOֵ
	 * 
	 * @param <T>
	 * @param tvalue
	 * @param fieldname
	 * @param aggVOs
	 */
	public static <T> void fillSettlementFieldAggInfo(T tvalue, String fieldname, SettlementAggVO... aggVOs) {
		for (SettlementAggVO settlementAggVO : aggVOs) {
			fillSettlementVOInfo(tvalue, fieldname, (SettlementHeadVO) settlementAggVO.getParentVO());
			fillSettlementVOInfo(tvalue, fieldname, (SettlementBodyVO[]) settlementAggVO.getChildrenVO());
		}
	}

	/**
	 * ��䵥��ֵ
	 * 
	 * @param <T>
	 * @param tvalue
	 * @param fieldname
	 * @param aggVOs
	 */
	public static <T> void fillSettlementVOInfo(T tvalue, String fieldname, SuperVO... vos) {
		for (SuperVO vo : vos) {
			vo.setAttributeValue(fieldname, tvalue);
		}
	}

	/**
	 * ���ز���ص��ڲ��˻���Ϣ
	 * 
	 * @param agg
	 * @throws BusinessException
	 */
	public static void fillInnerOrgByHB(SettlementAggVO agg) throws BusinessException {
		String[] srcInfo = getHBSrcPK(agg);
		if (srcInfo == null) {
			return;
		}
		Map<String, String> queryInnerAccount = NCLocator.getInstance()
				.lookup(nc.pubitf.ps.apply.IApplyPubService.class)
				.queryInnerAccount(srcInfo[1], new String[] { srcInfo[0] });
		if (queryInnerAccount != null && queryInnerAccount.size() != 0 && queryInnerAccount.get(srcInfo[0]) != null) {
			// �ز������ڲ��ʻ�ֵ
			fillSettlementVOInfo(queryInnerAccount.get(srcInfo[0]), SettlementBodyVO.PK_INNERACCOUNT,
					(SuperVO[]) agg.getChildrenVO());
		}
	}

	/**
	 * ���ݵ�ǰ�����˻����ڲ��˻����ж��Ƿ�ز�ҵ��
	 * 
	 * @param agg
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isHBBusiness(SettlementAggVO agg) throws BusinessException {

		// �����˻��ж��Ƿ��ǻز�ҵ��
		for (SettlementBodyVO body : (SettlementBodyVO[]) agg.getChildrenVO()) {
			BankAccbasVO bankaccbasvo = getAccbasVObySubBankAccPK(body.getPk_account());
			if (bankaccbasvo == null) {
				continue;
			}

			if (bankaccbasvo != null && UFBoolean.TRUE.equals(bankaccbasvo.getIsinneracc())) {
				// ������ڲ��˻����ǻز�ҵ��
				return false;
			} else if (body.getPk_Inneraccount() != null) {
				// ���ⲿ�˻����ڲ��˻���Ϊ�գ�������ǻز�֧��ҵ��
				return true;
			}
		}
		// // �жϸ����ų�
		// String[] srcInfo = getHBSrcPK (agg);
		// if(srcInfo==null){
		// return false;
		// }
		//
		// Map<String, String> queryInnerAccount =
		// NCLocator.getInstance().lookup(
		// nc.pubitf.ps.apply.IApplyPubService.class).queryInnerAccount(srcInfo[1],
		// new String[] { srcInfo[0] });
		// if (queryInnerAccount != null && queryInnerAccount.size() != 0 &&
		// queryInnerAccount.get(srcInfo[0]) != null) {
		// return true;
		// }
		return false;
	}

	/**
	 * ��ȡ��Դ��Ϣ-- ���ڻز�ҵ��--�����ų�ҵ��
	 * 
	 * @param agg
	 * @return
	 * @throws BusinessException
	 */
	private static String[] getHBSrcPK(SettlementAggVO agg) throws BusinessException {
		SettlementHeadVO Headvo = (SettlementHeadVO) agg.getParentVO();
		String billtype = SettleUtils.getbilltype(Headvo.getPk_tradetype(), Headvo.getPk_group());
		String src_billtype = null;
		String src_billpk = null;

		if (!CmpQueryModulesUtil.isPSEnable(Headvo.getPk_group())) {
			return null;
		}
		if (billtype.equalsIgnoreCase(CmpConst.CONST_FITYPE_F3)) {
			// IArapPayBillPubQueryService queryService =
			// NCLocator.getInstance().lookup(IArapPayBillPubQueryService.class);
			// BaseAggVO[] aggvos = queryService.findBillByPrimaryKey(new
			// String[]{getHead().getPk_busibill()});
			// if(CmpUtils.isListNull(aggvos)){
			// return;
			// }
			// PayBillVO head = (PayBillVO)aggvos[0].getParent();
			// head.get
		} else if (billtype.equalsIgnoreCase(CmpConst.CONST_CMPTYPE_F5)) {
			ICmpPayBillPubQueryService queryService = NCLocator.getInstance().lookup(ICmpPayBillPubQueryService.class);
			BillAggVO[] aggvos = queryService.findBillByPrimaryKey(new String[] { Headvo.getPk_busibill() });
			if (CmpUtils.isListNull(aggvos)) {
				return null;
			}
			nc.vo.cmp.bill.BillVO head = (nc.vo.cmp.bill.BillVO) aggvos[0].getParent();
			src_billtype = head.getUp_billtype();
			src_billpk = head.getPk_upbill();
		} else {
			return null;
		}
		/**
		 * Map<String, String>
		 * nc.pubitf.ps.apply.IApplyPubService.queryInnerAccount(String
		 * billtype, String[] pks) throws BusinessException �������������ͣ����ݵ�PK����
		 * ����ֵ���Դ������ĵ���PKΪkey��PK��Ӧ�ĸ����˻�(�ڲ��˻�)Ϊvalue��HashMap ע�⣺
		 * 1�������������PK��Ӧ�ĵ��ݲ��ǻذ�ҵ��(���ʽΪί���ϼ��ذ�֧��)���򷵻�ֵΪ�գ� 2���������̺���á�
		 */
		if (src_billtype == null || src_billpk == null) {
			return null;
		}

		// �Ǹ����ųɲ�����
		if (!("36D1".equalsIgnoreCase(src_billtype) || "36D7".equalsIgnoreCase(src_billtype))) {
			return null;
		}
		return new String[] { src_billpk, src_billtype };
	}

	/**
	 * ��ѯ�����ų̵ĵ����Ƿ񱻶��� Map<String, Boolean>
	 * nc.pubitf.ps.apply.IApplyPubService.isPsBillFrozen(String billtype,
	 * String[] pks) throws BusinessException
	 */
	public static boolean isPsBillFrozen(SettlementAggVO agg) throws BusinessException {

		SettlementHeadVO Headvo = (SettlementHeadVO) agg.getParentVO();
		// SettlementBodyVO[] bodys = (SettlementBodyVO[])agg.getChildrenVO();
		if (Headvo.getIsindependent() != null && Headvo.getIsindependent().booleanValue()
				&& Headvo.getPk_tradetype() == null) {
			// ����������Ϣ�ҵ�������û�е�ʱ�򲻼��
			return false;
		}
		String billtype = SettleUtils.getbilltype(Headvo.getPk_tradetype(), Headvo.getPk_group());

		String src_billtype = null;
		String src_billpk = null;
		if (Headvo.getIsindependent().booleanValue() || !CmpQueryModulesUtil.isPSEnable(Headvo.getPk_group())) {
			return false;
		}

		if (billtype.equalsIgnoreCase("F3") && CmpQueryModulesUtil.isAREnable(Headvo.getPk_group())) {
			/**
			 * Լ��ȡPayBillItemVO�������Ϣ top_billid ���ε���id , top_itemid���ε�����id,
			 * top_billtype���ε�������
			 */
			IArapPayBillPubQueryService queryService = NCLocator.getInstance()
					.lookup(IArapPayBillPubQueryService.class);
			BaseAggVO[] aggvos = queryService.findBillByPrimaryKey(new String[] { Headvo.getPk_busibill() });
			if (CmpUtils.isListNull(aggvos)) {
				return false;
			}

			PayBillItemVO[] PayBillItemVOs = (PayBillItemVO[]) aggvos[0].getChildrenVO();
			src_billtype = PayBillItemVOs[0].getTop_billtype();
			src_billpk = PayBillItemVOs[0].getTop_billid();

		} else if (billtype.equalsIgnoreCase("F5")) {
			ICmpPayBillPubQueryService queryService = NCLocator.getInstance().lookup(ICmpPayBillPubQueryService.class);
			BillAggVO[] aggvos = queryService.findBillByPrimaryKey(new String[] { Headvo.getPk_busibill() });
			if (CmpUtils.isListNull(aggvos)) {
				return false;
			}
			nc.vo.cmp.bill.BillVO head = (nc.vo.cmp.bill.BillVO) aggvos[0].getParent();
			src_billtype = head.getUp_billtype();
			src_billpk = head.getPk_upbill();
		} else {
			return false;
		}
		if (billtype == null || src_billpk == null) {
			return false;
		}

		Map<String, Boolean> psBillFrozen = NCLocator.getInstance().lookup(IApplyPubService.class)
				.isPsBillFrozen(src_billtype, new String[] { src_billpk });
		if (psBillFrozen == null || psBillFrozen.size() == 0 || psBillFrozen.get(src_billpk) == null
				|| !psBillFrozen.get(src_billpk)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ɾ����¼��Ϣ
	 * 
	 * @param aggvo
	 * @throws BusinessException
	 */
	public static void deleteObmLog(SettlementAggVO aggvo) throws BusinessException {
		SettlementHeadVO head = (SettlementHeadVO) aggvo.getParentVO();
		String id = head.getPrimaryKey();
		List<PaymentCrumbVO> list = InterfaceLocator.getInterfaceLocator().getPaymentService().queryCrumb(id);
		String pk_group = head.getPk_group();
		String pk_org = head.getPk_org();
		String pk_user = head.getLastupdater();
		if (!CheckException.checkContionsIsNull(list)) {
			Set<String> yus = new HashSet<String>();
			for (PaymentCrumbVO vo : list) {
				yus.add(vo.getYurref());

			}
			LogMgrRequestVO logvo = LogMgrRequestVO.createDelRequest(yus.toArray(new String[] {}));

			logvo.setPk_group(pk_group);
			logvo.setPk_org(pk_org);
			logvo.setPk_user(pk_user);

			// ɾ��OBM��־
			NCLocator.getInstance().lookup(IObmLogManageService.class).deleteObmLog(logvo);
		}
	}

	public static void addDynamicLock(AggregatedValueObject... settleAggs) throws BusinessException {
		if (settleAggs == null || settleAggs.length == 0 || settleAggs[0] == null) {
			return;
		}
		List<String> list = Lists.newArrayList();
		for (AggregatedValueObject aggregatedValueObject : settleAggs) {
			list.add(aggregatedValueObject.getParentVO().getPrimaryKey());
		}
		String[] pks = new String[settleAggs.length];
		list.toArray(pks);
		PKLock.getInstance().addBatchDynamicLock(pks);
	}

	public static void checkTs(AggregatedValueObject... settleAggs) throws BusinessException {

		String sql = null;
		for (AggregatedValueObject agg : settleAggs) {
			String tablename = ((SuperVO) agg.getParentVO()).getTableName();
			String tablePk = ((SuperVO) agg.getParentVO()).getPKFieldName();
			String pk = agg.getParentVO().getPrimaryKey();
			String ts = ((UFDateTime) ((SuperVO) agg.getParentVO()).getAttributeValue("ts")).toString();
			sql = "update " + tablename + " set dr=dr where " + tablePk + " = ? and ts = ? ";
			int count = executeTsUpdate(sql, pk, ts);
			if (count < 1) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
						"03607set-0620")/* @res "�����ѱ����£�" */);
			}
		}

	}

	public static void checkTs(SettlementBodyVO... bodyVOs) throws BusinessException {

		String sql = null;
		for (SettlementBodyVO bodyVO : bodyVOs) {
			String tablename = bodyVO.getTableName();
			String tablePk = bodyVO.getPKFieldName();
			String pk = bodyVO.getPrimaryKey();
			String ts = bodyVO.getTs().toString();
			sql = "update " + tablename + " set dr=dr where " + tablePk + " = ? and ts = ? ";
			int count = executeTsUpdate(sql, pk, ts);
			if (count < 1) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
						"03607set-0620")/* @res "�����ѱ����£�" */);
			}
		}

	}

	public static void addDynamicLock(SettlementBodyVO... bodyVOs) throws BusinessException {
		if (bodyVOs == null || bodyVOs.length == 0 || bodyVOs[0] == null) {
			return;
		}
		List<String> list = Lists.newArrayList();
		for (SettlementBodyVO bodyVO : bodyVOs) {
			list.add(bodyVO.getPrimaryKey());
		}
		boolean isLock = PKLock.getInstance().addBatchDynamicLock(list.toArray(new String[0]));
		if(!isLock){
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607cash_0","03607cash-0098")/*@res "����ʧ�ܣ��������ڱ�ʹ�ã����Ժ����ԡ�"*/);
            
		}
		
	}

	public static int executeTsUpdate(String sql, String pk, String ts) {
		PersistenceManager pm = null;
		Connection con = null ;
		PreparedStatement stmt = null ;
		int count = 0;
		try {
			pm = PersistenceManager.getInstance();
			JdbcSession jdbc = pm.getJdbcSession();
			con = jdbc.getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, pk);
			stmt.setString(2, ts);
			count = stmt.executeUpdate();
		} catch (DbException e) {
		} catch (SQLException e) {
		} finally {
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
			if(pm != null){
				try {
					pm.release();
				} catch (Exception e) {
				}
			}
			
		
		}
		
		return count;
	}

	/**
	 * �Ƚ϶���
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean isObjEqual(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}

	}

	/**
	 * ������֯IDȡ����ID OrgVO[]
	 * nc.pubitf.org.cache.IOrgUnitPubService_C.getOrgs(String[] orgIDs,
	 * String[] fieldnames) throws BusinessException
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public static String getGroupPKByOrgPk(String pk_org) throws BusinessException {
		OrgVO[] orgs = CmpInterfaceProxy.INSTANCE.getOrgUnitPubService_C().getOrgs(new String[] { pk_org },
				new String[] { OrgVO.PK_ORG, OrgVO.PK_GROUP });
		if (CmpUtils.isListNull(orgs)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3607set_0",
					"03607set-0825")/* @res "������֯��Ϣʧ��" */);
		} else {
			return orgs[0].getPk_group();
		}
	}

	/**
	 * ֧�ֳ�Ա��λ������㹺��֧�� ����ֶ�
	 * 
	 * @param aggvo
	 * @throws BusinessException
	 */
	public static void fillSettleinfoByLastPayInfo(SettlementAggVO aggvo) throws BusinessException {
		SettlementHeadVO head = (SettlementHeadVO) aggvo.getParentVO();

		Integer settlestatus = head.getSettlestatus() == null ? SettleStatus.NONESETTLE.getStatus() : head
				.getSettlestatus();

		if (head.getDirection() != null && head.getDirection().equals(Direction.PAY.VALUE)
				&& settlestatus.equals(SettleStatus.NONESETTLE.getStatus())) {
			// ֻ�и���ġ�δ����Ĳ���
			for (SettlementBodyVO body : (SettlementBodyVO[]) aggvo.getChildrenVO()) {
				if (body.getModifyflag() == null) {
					body.setModifyflag(CmpConst.MODIFY_FLAG);
				}
				String modifyflag = body.getModifyflag();

				String[] flags = modifyflag.split(",");
				String flag = flags[20];
				if (flag.equalsIgnoreCase("y")) {
					// ������Ϣ�޸Ĺ�������ͬ��
					continue;
				}
				fillSettleinfoByLastPayInfo(body);
			}
		}
	}

	/**
	 * ֧�ֳ�Ա��λ������㹺��֧�� ����ֶ�
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	public static void fillSettleinfoByLastPayInfo(SettlementBodyVO bodyvo) throws BusinessException {

		if (bodyvo.getModifyflag() == null) {
			bodyvo.setModifyflag(CmpConst.MODIFY_FLAG);
		}
		String modifyflag = bodyvo.getModifyflag();

		String[] flags = modifyflag.split(",");
		String flag = flags[20];
		if (flag.equalsIgnoreCase("y")) {
			return;
		}

		bodyvo.setPk_currtype_last(bodyvo.getPk_currtype());
		bodyvo.setChangebalance(UFDouble.ZERO_DBL);
		bodyvo.setChangerate(UFDouble.ONE_DBL);

		bodyvo.setPay_last(bodyvo.getPay());

		bodyvo.setPaylocal_last(bodyvo.getPaylocal());
		bodyvo.setPaylocalrate_last(bodyvo.getLocalrate());

		bodyvo.setGrouppayrate_last(bodyvo.getGrouprate());
		bodyvo.setGrouppaylocal_last(bodyvo.getGrouppaylocal());

		bodyvo.setGlobalpayrate_last(bodyvo.getGlobalrate());
		bodyvo.setGlobalpaylocal_last(bodyvo.getGlobalpaylocal());
	}

	/**
	 * ���öһ������Ϣ
	 * 
	 * @param bodyVO
	 * @param billdate
	 * @throws BusinessException
	 */
	public static void setRealPayInfo(SettlementBodyVO bodyVO, UFDate busi_billdate) throws BusinessException {

		UFDate billdate = busi_billdate; // ��������
		// ��֯������Ϣ
		UFDouble orgCurrRate = TMCurrencyUtil
				.getOrgCurrRate(bodyVO.getPk_org(), bodyVO.getPk_currtype_last(), billdate);
		UFDouble orgLocalMoney = TMCurrencyUtil.getOrgLocalMoney(bodyVO.getPk_org(), bodyVO.getPk_currtype_last(),
				bodyVO.getPay_last(), orgCurrRate, billdate);
		bodyVO.setPaylocal_last(orgLocalMoney);
		bodyVO.setPaylocalrate_last(orgCurrRate);

		// ���ű�����Ϣ
		// String groupLocalCurrPK =
		// TMCurrencyUtil.getGroupLocalCurrPK(DataUtil.getCurrentGroup());
		UFDouble groupCurrRate = TMCurrencyUtil.getGroupCurrRate(DataUtil.getCurrentGroup(), bodyVO.getPk_org(),
				bodyVO.getPk_currtype_last(), billdate);
		UFDouble groupLocalMoney = TMCurrencyUtil.getGroupLocalMoney(DataUtil.getCurrentGroup(), bodyVO.getPk_org(),
				bodyVO.getPk_currtype_last(), bodyVO.getPay_last(), groupCurrRate, bodyVO.getPaylocalrate_last(),
				billdate);

		bodyVO.setGrouppaylocal_last(groupLocalMoney == null ? UFDouble.ZERO_DBL:groupLocalMoney);
		bodyVO.setGrouppayrate_last(groupCurrRate);

		// ȫ�ֱ�����Ϣ
		// String gLobalCurrPK = TMCurrencyUtil.getGLobalCurrPK();
		UFDouble globalCurrRate = TMCurrencyUtil.getGlobalCurrRate(bodyVO.getPk_org(), bodyVO.getPk_currtype_last(),
				billdate);
		UFDouble globalLocalMoney = TMCurrencyUtil.getGlobalLocalMoney(bodyVO.getPk_org(),
				bodyVO.getPk_currtype_last(), bodyVO.getPay_last(), globalCurrRate, bodyVO.getPaylocalrate_last(),
				billdate);

		bodyVO.setGlobalpaylocal_last(globalLocalMoney == null ? UFDouble.ZERO_DBL:globalLocalMoney);
		bodyVO.setGlobalpayrate_last(globalCurrRate);
		if (bodyVO.getChangerate() != null) {

			// ��Ҳ��=�������ҽ��-���뱾�ҽ�� = last - first
			bodyVO.setChangebalance(bodyVO.getPaylocal_last().sub(bodyVO.getPaylocal()));
		} else {
			// bodyVO.setPay_last(null);
			bodyVO.setChangebalance(null);
		}
	}
	/**
	 * �����Ҳ�����ҵ����Ϣ�������ݺ�Ĵ���
	 * @param bodys
	 */
	public static void setRealChangeBalance(SettlementBodyVO[] bodys)throws BusinessException{
		for (SettlementBodyVO bodyVO : bodys) {
			if(!bodyVO.getDirection().equals(Direction.PAY.VALUE)){
				continue;
			}
			if (bodyVO.getChangerate() != null) {
				if(bodyVO.getPk_currtype().equals(bodyVO.getPk_currtype_last())){
					fillSettleinfoByLastPayInfo(bodyVO);
				}else{
					// ʵ�����=����ԭ�ҽ����׻���ʡ�
					bodyVO.setPay_last(bodyVO.getPay().div(bodyVO.getChangerate()));
					setRealPayInfo(bodyVO, bodyVO.getBilldate());
				}
			} else {
				// bodyVO.setPay_last(null);
				bodyVO.setChangebalance(null);
			}
		}
		
	}
	/**
	 * �����ͷ�Ľ���״̬��ǰ̨��Ҫ�ã���������
	 * 
	 * @param aggvos
	 * @throws BusinessException
	 */
	public static void setSettleStatus(SettlementAggVO... aggvos) throws BusinessException {

		boolean isHasNotSettle = false;
		boolean isHasPaying = false;
		boolean isHasReceiving = false;
		boolean isHasPayFAIL = false;
		boolean isHasEreset = false;
		boolean isHasSuccesssettle = false;
		boolean isHasSuccesssettleForNet = false;
		boolean isHasPrechange = false;
		UFDate settledate = null;

		for (SettlementAggVO settlementAggVO : aggvos) {
			SettlementHeadVO head = (SettlementHeadVO) settlementAggVO.getParentVO();
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				if (body.getSettlestatus() == null) {
					isHasNotSettle = true;
					break;
				}
				SettleStatus status = SettleStatus.valueof(body.getSettlestatus());

				if (status == null || status.equals(SettleStatus.NONESETTLE)) {
					// δ����
					isHasNotSettle = true;
					break;
				}
				if (status.equals(SettleStatus.PAYING)) {
					// �����л��տ���
					isHasPaying = true;
				} else if (status.equals(SettleStatus.RECEIVING)) {
					// �����л��տ���
					isHasReceiving = true;
				} else if (status.equals(SettleStatus.PAYFAIL)) {
					// ֧��ʧ��
					isHasPayFAIL = true;
				} else if (status.equals(SettleStatus.SETTLERESET)) {
					// ������
					isHasEreset = true;
					if (settledate == null || settledate.before(body.getTallydate())) {
						settledate = body.getTallydate();
					}
					break;
				} else if (status.equals(SettleStatus.PRECHANGE)) {
					// �����
					isHasPrechange = true;
					break;
				} else if (status.equals(SettleStatus.SUCCESSSETTLE)) {
					if (body.getPk_balatype() == null) {
						isHasSuccesssettle = true;
					} else {
						BalaTypeVO btvo = InterfaceLocator.getInterfaceLocator().getBalaQry()
								.findBalaTypeVOByPK(body.getPk_balatype());
						if (btvo.getNetbankflag() == null || btvo.getNetbankflag().equals(UFBoolean.FALSE)) {
							isHasSuccesssettle = true;
						} else {
							isHasSuccesssettleForNet = true;
						}
					}
					if (settledate == null || settledate.before(body.getTallydate())) {
						settledate = body.getTallydate();
					}
				}
			}
			if (isHasNotSettle) {
				// �����δ֧���ģ���ͷ״̬�϶���δ����
				head.setSettlestatus(SettleStatus.NONESETTLE.getStatus());
			} else if (isHasPrechange) {
				// ���������ģ��϶���δ֧��
				head.setSettlestatus(SettleStatus.NONESETTLE.getStatus());
			} else if (isHasEreset) {
				// ����н�����ģ��϶��ǳɹ���
				head.setSettlestatus(SettleStatus.SUCCESSSETTLE.getStatus());
			} else if (isHasPaying) {
				// ֧����
				head.setSettlestatus(SettleStatus.PAYING.getStatus());
			} else if (isHasReceiving) {
				// �տ���
				head.setSettlestatus(SettleStatus.RECEIVING.getStatus());
			} else if (isHasPayFAIL && (isHasSuccesssettleForNet || isHasSuccesssettle)) {
				// ���������гɹ���ʧ��
				head.setSettlestatus(SettleStatus.SUCCESSPART.getStatus());
			}else if(isHasPayFAIL&&!(isHasNotSettle||isHasEreset||isHasPaying|| isHasPrechange||isHasReceiving||isHasSuccesssettle||isHasSuccesssettleForNet)){
				// ֻ������֧��ʧ�ܵ����
				head.setSettlestatus(SettleStatus.PAYFAIL.getStatus());
			}
			else if (isHasPayFAIL && !(isHasSuccesssettleForNet || isHasSuccesssettle)) {
				// ����������ʧ��
				head.setSettlestatus(SettleStatus.NONESETTLE.getStatus());
			} else {
				// ����
				head.setSettlestatus(SettleStatus.SUCCESSSETTLE.getStatus());
			}
			// ���ý�������
			if (head.getSettlestatus().equals(SettleStatus.SUCCESSSETTLE.getStatus())) {
				head.setSettledate(settledate);
			} else {
				head.setSettledate(null);
				head.setPk_executor(null);
				head.setIsverify(UFBoolean.FALSE);
			}
			head.setStatus(VOStatus.UPDATED);
		}
	}

	/**
	 * ������Ϣ���� ��ȡδ������ֹ�������Ϣ
	 * 
	 * @param aggVOs
	 * @return
	 * @throws BusinessException
	 */
	public static List<SettlementAggVO> filterSettleInfo4HandSettleFlag4UnSettle(SettlementAggVO... aggVOs)
			throws BusinessException {

		Map<String, BalaTypeVO> map = new HashMap<String, BalaTypeVO>();
		
		List<SettlementAggVO> agglst = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head= (SettlementHeadVO)settlementAggVO.getParentVO();
			if (UFBoolean.TRUE.equals(head.getIsreset())
					|| head.getDirection().equals(Direction.REC.VALUE)) {
				// ��嵥�ݻ��տ���㵥���ù���
				agglst.add(settlementAggVO);
				continue;
			}
			
			
			
			SettlementAggVO aggvo = new SettlementAggVO();
			aggvo.setParentVO(head);
			List<SettlementBodyVO> bodyLst = CmpUtils.makeList();
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				if (body.getSettlestatus() != null
						&& !body.getSettlestatus().equals(SettleStatus.NONESETTLE.getStatus())) {
					continue;
				}
				
				UFDouble  pay =  body.getPay();
				if(pay != null && pay.toBigDecimal().signum() != 1){
					bodyLst.add(body);
					continue;
				}
				String pk_balatype = body.getPk_balatype();

				// �ֹ�����״̬
				if (pk_balatype == null) {
					bodyLst.add(body);
				} else {
					BalaTypeVO btvo = null ;
					if(!map.containsKey(pk_balatype)){
						btvo = InterfaceLocator.getInterfaceLocator().getBalaQry()
								.findBalaTypeVOByPK(pk_balatype);
						map.put(pk_balatype, btvo);
					}else{
						btvo = map.get(pk_balatype);
					}
					
					if (btvo.getNetbankflag() == null || btvo.getNetbankflag().equals(UFBoolean.FALSE)) {
						bodyLst.add(body);
					}
				}
			}
			if (bodyLst.size() > 0) {
				aggvo.setChildrenVO(bodyLst.toArray(new SettlementBodyVO[0]));
				agglst.add(aggvo);
			}
		}
		return agglst;
	}

	/**
	 * ������Ϣ���� ��ȡ�ѽ�����ֹ�������Ϣ
	 * 
	 * @param aggVOs
	 * @return
	 * @throws BusinessException
	 */
	public static List<SettlementAggVO> filterSettleInfo4HandSettleFlag4Settle(SettlementAggVO... aggVOs)
			throws BusinessException {
		// return filterSettleInfo4NetFlag(true,0, aggVOs);
		List<SettlementAggVO> agglst = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO head= (SettlementHeadVO)settlementAggVO.getParentVO();
			if(Integer.valueOf(SettleStatus.SUCCESSSETTLE.getStatus()).equals(head.getSettlestatus())
					&& (UFBoolean.TRUE.equals(head.getIsreset())||head.getDirection().equals(Direction.REC.VALUE))){
				// �ѽ���ĺ�嵥�ݻ��տ���㵥���ù���
				agglst.add(settlementAggVO);
				continue;
			}
			SettlementAggVO aggvo = new SettlementAggVO();
			aggvo.setParentVO(head);
			List<SettlementBodyVO> bodyLst = CmpUtils.makeList();
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				if ((body.getSettlestatus() == null || body.getSettlestatus().equals(
						SettleStatus.NONESETTLE.getStatus()))) {
					continue;
				}

				// �ֹ�����״̬
				if (body.getPk_balatype() == null) {
					bodyLst.add(body);
				} else {
					BalaTypeVO btvo = InterfaceLocator.getInterfaceLocator().getBalaQry()
							.findBalaTypeVOByPK(body.getPk_balatype());
					if (btvo.getNetbankflag() == null || btvo.getNetbankflag().equals(UFBoolean.FALSE)) {
						bodyLst.add(body);
					}
				}
			}
			if (bodyLst.size() > 0) {
				aggvo.setChildrenVO(bodyLst.toArray(new SettlementBodyVO[0]));
				agglst.add(aggvo);
			}
		}
		return agglst;
	}

	/**
	 * ������Ϣ���� ��ȡ�ѽ�����ֹ�������Ϣ
	 * 
	 * @param aggVOs
	 * @return
	 * @throws BusinessException
	 */
	public static List<SettlementAggVO> filterSettleInfo4HandSettle(SettlementAggVO... aggVOs) throws BusinessException {
		// return filterSettleInfo4NetFlag(true,0, aggVOs);
		List<SettlementAggVO> agglst = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementAggVO aggvo = new SettlementAggVO();
			aggvo.setParentVO(settlementAggVO.getParentVO());
			List<SettlementBodyVO> bodyLst = CmpUtils.makeList();
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				// �ֹ�����״̬
				if (body.getPk_balatype() == null) {
					bodyLst.add(body);
				} else {
					BalaTypeVO btvo = InterfaceLocator.getInterfaceLocator().getBalaQry()
							.findBalaTypeVOByPK(body.getPk_balatype());
					if (btvo.getNetbankflag() == null || btvo.getNetbankflag().equals(UFBoolean.FALSE)) {
						bodyLst.add(body);
					}
				}
			}
			if (bodyLst.size() > 0) {
				aggvo.setChildrenVO(bodyLst.toArray(new SettlementBodyVO[0]));
				agglst.add(aggvo);
			}
		}
		return agglst;
	}

	/**
	 * ������Ϣ���� ��ȡδ���������������Ϣ
	 * 
	 * @param isHandSettleFlag
	 * @param aggVOs
	 * @return
	 * @throws BusinessException
	 */
	public static List<SettlementAggVO> filterSettleInfo4NetSettleFlagUnSettle(SettlementAggVO... aggVOs)
			throws BusinessException {
		// return filterSettleInfo4NetFlag(false,0, aggVOs);

		List<SettlementAggVO> agglst = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementHeadVO headvo = (SettlementHeadVO) settlementAggVO.getParentVO();
			if(headvo.getBusistatus() == null || !headvo.getBusistatus().equals(BusiStatus.Sign.getBillStatusKind())){
				// ��ǩ��̬����
				continue;
			}
			if(headvo.getSettlestatus()!=null &&  SettleStatus.SUCCESSSETTLE.getStatus()==headvo.getSettlestatus() ){
				// �ѽ��������
				continue;
			}
			
			SettlementAggVO aggvo = new SettlementAggVO();
			aggvo.setParentVO(headvo);
			
			
			List<SettlementBodyVO> bodyLst = CmpUtils.makeList();
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				
				if (body.getSettlestatus() != null
						&& !(body.getSettlestatus().equals(SettleStatus.NONESETTLE.getStatus()) || body
								.getSettlestatus().equals(SettleStatus.PAYFAIL.getStatus()))) {
					continue;
				}
				if (body.getPk_balatype() != null) {
					BalaTypeVO btvo = InterfaceLocator.getInterfaceLocator().getBalaQry()
							.findBalaTypeVOByPK(body.getPk_balatype());
					if (btvo.getNetbankflag() != null && btvo.getNetbankflag().equals(UFBoolean.TRUE)) {
						bodyLst.add(body);
					}
				}
			}

			if (bodyLst.size() > 0) {
				aggvo.setChildrenVO(bodyLst.toArray(new SettlementBodyVO[0]));
				agglst.add(aggvo);
			}
		}
		return agglst;
	}

	/**
	 * ������Ϣ���� ��ȡ�ѽ��������������Ϣ
	 * 
	 * @param isHandSettleFlag
	 * @param aggVOs
	 * @return
	 * @throws BusinessException
	 */
	public static List<SettlementAggVO> filterSettleInfo4NetSettleFlagSettle(SettlementAggVO... aggVOs)
			throws BusinessException {
		// return filterSettleInfo4NetFlag(false,0, aggVOs);

		List<SettlementAggVO> agglst = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementAggVO aggvo = new SettlementAggVO();
			aggvo.setParentVO(settlementAggVO.getParentVO());
			List<SettlementBodyVO> bodyLst = CmpUtils.makeList();
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				if (body.getSettlestatus() == null
						|| body.getSettlestatus().equals(SettleStatus.NONESETTLE.getStatus())) {
					continue;
				}
				if (body.getPk_balatype() != null) {
					BalaTypeVO btvo = InterfaceLocator.getInterfaceLocator().getBalaQry()
							.findBalaTypeVOByPK(body.getPk_balatype());
					if (btvo.getNetbankflag() != null && btvo.getNetbankflag().equals(UFBoolean.TRUE)) {
						bodyLst.add(body);
					}
				}
			}

			if (bodyLst.size() > 0) {
				aggvo.setChildrenVO(bodyLst.toArray(new SettlementBodyVO[0]));
				agglst.add(aggvo);
			}
		}
		return agglst;
	}

	/**
	 * ������Ϣ���� ��ȡ�ѽ��������������Ϣ
	 * 
	 * @param isHandSettleFlag
	 * @param aggVOs
	 * @return
	 * @throws BusinessException
	 */
	public static List<SettlementAggVO> filterSettleInfo4NetSettleFlag(SettlementAggVO... aggVOs)
			throws BusinessException {

		List<SettlementAggVO> agglst = CmpUtils.makeList();
		for (SettlementAggVO settlementAggVO : aggVOs) {
			SettlementAggVO aggvo = new SettlementAggVO();
			aggvo.setParentVO(settlementAggVO.getParentVO());
			List<SettlementBodyVO> bodyLst = CmpUtils.makeList();
			for (SettlementBodyVO body : (SettlementBodyVO[]) settlementAggVO.getChildrenVO()) {
				if (body.getPk_balatype() != null) {
					BalaTypeVO btvo = InterfaceLocator.getInterfaceLocator().getBalaQry()
							.findBalaTypeVOByPK(body.getPk_balatype());
					if (btvo.getNetbankflag() != null && btvo.getNetbankflag().equals(UFBoolean.TRUE)) {
						bodyLst.add(body);
					}
				}
			}

			if (bodyLst.size() > 0) {
				aggvo.setChildrenVO(bodyLst.toArray(new SettlementBodyVO[0]));
				agglst.add(aggvo);
			}
		}
		return agglst;
	}
	
	/**
	 * �жϽ�����Ϣ�Ƿ��Ѿ��Ƶ���
	 * @param head
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isMakeBill(SettlementHeadVO head)throws BusinessException{
		// ��ʱ��ô�����Ժ���Կ���ͨ�������ֶ������ٲ�ѯ
		FipRelationInfoVO infovo = new FipRelationInfoVO();
		String billType = (String) head.getAttributeValue(SettlementHeadVO.PK_TRADETYPE);
		infovo.setPk_billtype(billType);
		infovo.setRelationID((String) head.getAttributeValue(SettlementHeadVO.PK_BUSIBILL));
		
		if(infovo.getPk_billtype()==null || infovo.getRelationID() == null){
			// ȱ��ҵ�񵥾���Ϣ������������Ϣ����Ϊδ�Ƶ���
			return false;
		}
		infovo.setPk_group( (String)head.getAttributeValue(SettlementHeadVO.PK_GROUP));
		infovo.setPk_org((String)head.getAttributeValue(SettlementHeadVO.PK_ORG));
		UFBoolean[][] queryDesBillState = CmpInterfaceProxy.INSTANCE.getFipRelationQueryService().queryDesBillState(new FipRelationInfoVO[]{ infovo},null);
		 
		boolean isMakeBill = queryDesBillState==null || queryDesBillState[0]==null || queryDesBillState[0][0]==null? false :queryDesBillState[0][0].booleanValue();
		
		return isMakeBill;
	}
}