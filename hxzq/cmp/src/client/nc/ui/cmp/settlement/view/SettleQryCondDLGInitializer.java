package nc.ui.cmp.settlement.view;

import nc.ui.pubapp.uif2app.query2.IQueryConditionDLGInitializer;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.tm.query.MainOrgWithPermissionOrgFilter;
import nc.ui.tm.query.QueryTempMainOrgFilterBaseDoc;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.cmp.settlement.SettlementBodyVO;

/**
 * 现金查询模板初始化
 * 
 * @author aishm
 * 
 */
@SuppressWarnings("restriction")
public class SettleQryCondDLGInitializer implements IQueryConditionDLGInitializer {
	private AbstractUIAppModel model;

	@Override
	public void initQueryConditionDLG(QueryConditionDLGDelegator condDLGDelegator) {

		// 设置主组织参照只包含用户有权限的组织
		condDLGDelegator.setRefFilter("pk_org", new MainOrgWithPermissionOrgFilter(model));

		String[] fields = { "pk_costsubj", "pk_plansubj", "pk_cashflow" };

		for (String field : fields) {
			new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", field).addEditorListener();
			new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", "items." + field).addEditorListener();

		}

		
		
		new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", "items." + SettlementBodyVO.PK_ACCOUNT).addEditorListener();
		new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", "items." + SettlementBodyVO.PK_CASHACCOUNT).addEditorListener();
		new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", "items." + SettlementBodyVO.PK_DEPTDOC).addEditorListener();
		new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", "items." + SettlementBodyVO.PK_PSNDOC).addEditorListener();
		new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", "items." + SettlementBodyVO.PK_INNERACCOUNT).addEditorListener();
		
		new QueryTempMainOrgFilterBaseDoc(condDLGDelegator, "pk_org", "costcenter").addEditorListener();
	}

	public AbstractUIAppModel getModel() {
		return model;
	}

	public void setModel(AbstractUIAppModel model) {
		this.model = model;
	}
}
