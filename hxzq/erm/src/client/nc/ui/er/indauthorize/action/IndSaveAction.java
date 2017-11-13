package nc.ui.er.indauthorize.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.er.indauthorize.IIndAuthorizeQueryService;
import nc.itf.er.indauthorize.IIndAuthorizeService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.arap.engine.IActionRuntime;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.ui.erm.util.ErUiUtil;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.actions.batch.BatchRefreshAction;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.batch.BatchSaveAction;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.er.indauthorize.IndAuthorizeVO;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.ArrayUtils;

public class IndSaveAction extends BatchSaveAction {

	/**
	 * @author liansg
	 */
	private static final long serialVersionUID = 1L;
	private String psndoc = null;
	private IActionRuntime actionRunntimeV0 = null;
	private BatchRefreshAction indRefreshAction;

	public BatchRefreshAction getIndRefreshAction() {
		return indRefreshAction;
	}

	public void setIndRefreshAction(BatchRefreshAction indRefreshAction) {
		this.indRefreshAction = indRefreshAction;
	}

	public void setActionRunntimeV0(IActionRuntime runtime) {
		actionRunntimeV0 = runtime;
	}

	protected IActionRuntime getActionRunntimeV0() {
		return actionRunntimeV0;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// ����ʱ����༭״̬��������ʱ������Ҫ����ֹ����ָ���
		if (getEditor().getModel().getRowCount() > 0) {
			getEditor().getBillCardPanel().stopEditing();
		}
		BatchOperateVO operVO = this.getModel().getCurrentSaveObject();
		Object[] addObjs = operVO.getAddObjs();
		Object[] updObjs = operVO.getUpdObjs();
		Object[] delObjs = operVO.getDelObjs();

		// �ж����������Ҫ��������
		if (ArrayUtils.isEmpty(addObjs) && ArrayUtils.isEmpty(updObjs)
				&& ArrayUtils.isEmpty(delObjs)) {
			if (this.getModel().getUiState() == UIState.EDIT) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("ersetting_0",
								"02011001-0013")/* @res " ���޸����ݺ󣬽��б��棡" */);
			}
		}

		Object[] indVOs = this.getModel().getRows().toArray();
		// ��Ա��Ӧ�ĵ�������
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		// ��Ա
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		for (Object svo : indVOs) {
			String code = (String) ((IndAuthorizeVO) svo)
					.getAttributeValue("pk_operator");
			UFDate startDate = (UFDate) ((IndAuthorizeVO) svo)
					.getAttributeValue("startdate");
			UFDate endDate = (UFDate) ((IndAuthorizeVO) svo)
					.getAttributeValue("enddate");
			String billtype = (String) ((IndAuthorizeVO) svo)
					.getAttributeValue("pk_billtypeid");

			if (code == null || "".equals(code)) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersetting_0", "02011001-0014")/*
																	 * @res
																	 * "����Ա����Ϊ��"
																	 */);
			}
			if (startDate == null) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersetting_0", "02011001-0015")/*
																	 * @res
																	 * "��ʼ���ڲ���Ϊ��"
																	 */);
			}
			if (endDate == null) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersetting_0", "02011001-0016")/*
																	 * @res
																	 * "�������ڲ���Ϊ��"
																	 */);
			}
			if (billtype == null || "".equals(billtype)) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersetting_0", "02011001-0017")/*
																	 * @res
																	 * "�������Ͳ���Ϊ��"
																	 */);
			}
			if (startDate.compareTo(endDate) > 0) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersetting_0", "02011001-0018")/*
																	 * @res
																	 * "��ʼ���ڲ��ܴ��ڽ������ڣ�����������!"
																	 */);
			}

			if (map.get(code) != null && map.get(code).contains(billtype)) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersetting_0", "02011001-0019")/*
																	 * @res
																	 * "����Ա������Ȩ��ͬ�ĵ������ͣ����������룡"
																	 */);
			} else if (map.get(code) == null) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(billtype);
				map.put(code, list);
				map2.put(code, (IndAuthorizeVO) svo);
			} else if (!map.get(code).contains(billtype)) {
				ArrayList<String> list = map.get(code);
				list.add(billtype);
				map.put(code, list);
				map2.put(code, (IndAuthorizeVO) svo);
			}
		}
		for (Object obj : indVOs) {
			String psndoc = getPsndocByUser();
			String billtypeid = (String) ((IndAuthorizeVO) obj)
					.getAttributeValue("pk_billtypeid");
			String pk_org = (String) ((IndAuthorizeVO) obj)
					.getAttributeValue("pk_org");
			// //���ӣ������������er_indauthorize �е�������Ϊpk_billtypeid�����
			String sql = "select distinct pk_billtypecode from bd_billtype where pk_billtypeid=?";
			SQLParameter param = new SQLParameter();
			Object[] arrs = null;
			param.addParam(billtypeid);
			List res = (List) DBCacheQueryFacade.runQueryByPk(sql, param,
					new ArrayListProcessor());
			String billtype = null;
			if (res != null && res.size() != 0) {
				arrs = (Object[]) res.get(0);
				billtype = ((String) arrs[0]);
			}
			((IndAuthorizeVO) obj).setPk_user(psndoc);
			((IndAuthorizeVO) obj).setType(1);
			((IndAuthorizeVO) obj).setEnddate(((IndAuthorizeVO) obj)
					.getEnddate().asEnd());
			((IndAuthorizeVO) obj).setBilltype(billtype);
			((IndAuthorizeVO) obj).setPk_billtypeid(billtypeid);
			((IndAuthorizeVO) obj).setPk_org(pk_org);
		}

		super.doAction(e);
		
		/**
		 * update by lihaibo
		 * ����ȫ����������
		 */
		if (addObjs != null && addObjs.length > 0) {

			String key = "";
			for (Object o : map2.keySet()) {
				key = o.toString();
				// ��ѯ��������
				String tj = "";
				for (Object indvo : indVOs) {
					if (((IndAuthorizeVO) indvo).getPk_operator().equals(key)) {
						tj += " and pk_billtypeid != '"
								+ ((IndAuthorizeVO) indvo).getPk_billtypeid()
								+ "' ";
					}
				}
				String pk_group = getModel().getContext().getPk_group();
				BilltypeVO[] billtypeVOs = (BilltypeVO[]) HYPubBO_Client
						.queryByCondition(
								BilltypeVO.class,
								"(pk_billtypecode like '263X-Cxx-%' or pk_billtypecode like '264X-Cxx-%' or pk_billtypecode = '2647') and parentbilltype != '~' and pk_group = '"
										+ pk_group + "'" + tj);
				if (billtypeVOs != null && billtypeVOs.length > 0) {
					Object[] indVOs2 = new Object[billtypeVOs.length];

					int i = 0;
					for (BilltypeVO billtypeVO : billtypeVOs) {
						IndAuthorizeVO indAuthorizeVO = new IndAuthorizeVO();
						indAuthorizeVO
								.setPk_operator(key);
						indAuthorizeVO
								.setStartdate((UFDate) ((IndAuthorizeVO)map2.get(key))
										.getAttributeValue("startdate"));
						indAuthorizeVO
								.setEnddate((UFDate) ((IndAuthorizeVO)map2.get(key))
										.getAttributeValue("enddate"));
						indAuthorizeVO
								.setPk_org((String) ((IndAuthorizeVO)map2.get(key))
										.getAttributeValue("pk_org"));
						indAuthorizeVO.setPk_group(pk_group);
						indAuthorizeVO.setPk_billtypeid(billtypeVO
								.getPk_billtypeid());
						indAuthorizeVO.setBilltype(billtypeVO
								.getPk_billtypecode());
						indAuthorizeVO.setType(1);
						indAuthorizeVO.setPk_user(getPsndocByUser());
						indVOs2[i] = indAuthorizeVO;
						i++;
					}
					// ������������ӿ�
					BatchOperateVO batchOperateVO = new BatchOperateVO();
					batchOperateVO.setAddObjs(indVOs2);
					this.getModel().getService().batchSave(batchOperateVO);
					getIndRefreshAction().doAction(e);
				}
			}
		}
	}

	private String getPsndocByUser() throws BusinessException {
		if (psndoc == null) {
			String cid = ErUiUtil.getPk_user();
			String[] queryPsnidAndDeptid = null;
			try {
				queryPsnidAndDeptid = NCLocator.getInstance()
						.lookup(IIndAuthorizeQueryService.class)
						.queryPsnidAndDeptid(cid, ErUiUtil.getPK_group());
			} catch (ComponentException e2) {
				ExceptionHandler.consume(e2);
			} catch (BusinessException e2) {
				throw e2;
			}
			if (queryPsnidAndDeptid == null)
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("2011", "UPP2011-000065")/*
																			 * @res
																			 * "����Ա��Ӧ��ҵ��ԱΪ�գ��˽ڵ㲻���ã�"
																			 */);
			else
				psndoc = queryPsnidAndDeptid[0];

		}
		return psndoc;
	}

}