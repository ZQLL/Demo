package nc.ui.gl.amortize.execute.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;

import nc.ui.gl.amortize.execute.view.AmorExeToftPanel;
import nc.ui.glpub.IParent;
import nc.ui.uif2.DefaultExceptionHanler;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.BillManageModel;
import nc.ui.uif2.model.IAppModelDataManager;
import nc.vo.gl.amortize.setting.AmorCycleDetailVO;
import nc.vo.gl.amortize.setting.AmorCycleVO;
import nc.vo.gl.amortize.setting.AmortizeStateEnum;
import nc.vo.gl.amortize.setting.AmortizeVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class QueryVoucherAction extends NCAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected AbstractAppModel model;
	private IAppModelDataManager modelDataManager;
	private BillListView listView;
	
	public QueryVoucherAction() {
		super();
		this.setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("amortize_2","amortize-000021")/*@res "联查凭证"*/);
		this.setCode("QueryVoucher");
	}

	public void doAction(ActionEvent e) throws Exception {
		
		((DefaultExceptionHanler)exceptionHandler).setErrormsg(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("amortize_2","amortize-000022")/*@res "联查凭证失败"*/);
		ArrayList<String> pks = new ArrayList<String>();
		
		Vector<AmortizeVO> amorVOs = getSelectedAmortzieVO();
		if(amorVOs != null){
			for(AmortizeVO amortizevo : amorVOs){
				AmorCycleVO amorCycleVO = amortizevo.getAmorCycleVO();
				if(amorCycleVO.getCycledetail() != null){
					for (CircularlyAccessibleValueObject tmpVO : amorCycleVO.getCycledetail()) {
						String pk_voucher = ((AmorCycleDetailVO)tmpVO).getPk_voucher();
						if(pk_voucher != null)
							pks.add(pk_voucher);
					}
				}
			}
			
			if(pks.size() > 0){
				VoucherVO[] voucherVOs = GLPubProxy.getRemoteVoucher().queryByPks(pks.toArray(new String[0]));
				if(voucherVOs != null && voucherVOs.length>0){
					//有可能根据pk查询出来的凭证已经删除，所以先进行非空判断
					IParent parent = ((AmorExeToftPanel)getListView().getParent().getParent().getParent().getParent().getParent()).getUiManager();
					nc.ui.gl.pubvoucher.VoucherBridge bridge = (nc.ui.gl.pubvoucher.VoucherBridge) parent.showNext("nc.ui.gl.pubvoucher.VoucherBridge");
					bridge.invoke(null, "FIPBATCHSAVE");
					bridge.setVoucher(voucherVOs);
				}
			}
		}
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public void setModelDataManager(IAppModelDataManager modelDataManager) {
		this.modelDataManager = modelDataManager;
	}

	public IAppModelDataManager getModelDataManager() {
		return modelDataManager;
	}
	
	protected boolean isActionEnable() {
		
		Vector<AmortizeVO> selectedAmorVOs = new Vector<AmortizeVO>();
		boolean enable = true;
		BillManageModel model = (BillManageModel)getModel();
		for (int i : model.getSelectedOperaRows()) {
			AmortizeVO amorVO = (AmortizeVO)model.getData().get(i);
			selectedAmorVOs.add(amorVO);
			if(AmortizeStateEnum.UNEXCUTED.toStringValue().equals(amorVO.getState())){
				enable = false;
			}
		}
		return enable;
	}
	
	private Vector<AmortizeVO> getSelectedAmortzieVO() {
		
		Vector<AmortizeVO> amorVOs = new Vector<AmortizeVO>();
		BillManageModel model = (BillManageModel)getModel();
		for (int i : model.getSelectedOperaRows()) {
			AmortizeVO amorVO = (AmortizeVO)model.getData().get(i);
			amorVOs.add(amorVO);
		}
		if (amorVOs.size() == 0) {
			return null;
		}
		return amorVOs;
	}
	
	public BillListView getListView() {
		return listView;
	}

	public void setListView(BillListView listView) {
		this.listView = listView;
	}
}
