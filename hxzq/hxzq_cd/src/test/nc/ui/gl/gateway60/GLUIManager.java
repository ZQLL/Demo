package nc.ui.gl.gateway60;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import nc.bcmanage.vo.ReportDBVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.sm.accountmanage.IReportDBService;
import nc.funcnode.ui.AbstractFunclet;
import nc.funcnode.ui.FuncletContext;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.IFunclet;
import nc.gl.glconst.systemtype.SystemtypeConst;
import nc.ui.fipub.FICheckChangeDataSrc;
import nc.ui.gl.cachefeed.CacheRequestFactory;
import nc.ui.gl.cashflowcase.CashFlowAnalyzeExUI;
import nc.ui.gl.detail.ToftPanelView;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.glreportforufo.GlQueryLinkUFOVO;
import nc.ui.gl.pubvoucher.VoucherBridge;
import nc.ui.gl.pubvoucher.VoucherToftPanel;
import nc.ui.gl.remotecall.GlRemoteCallProxy;
import nc.ui.gl.uicfg.ValueChangedAdaptor;
import nc.ui.gl.voucherlist.ListView;
import nc.ui.glpub.IUiPanel;
import nc.ui.glpub.UiManager;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.uif2.IFuncNodeInitDataListener;
import nc.ui.uif2.LoadingPanel;
import nc.vo.bd.intdata.trace.TraceType;
import nc.vo.fipub.utils.uif2.FiUif2MsgUtil;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.glcom.constant.GLStringConst;
import nc.vo.glcom.nodecode.GlNodeConst;
import nc.vo.pub.BusinessException;
import nc.vo.querytemplate.queryscheme.SimpleQuerySchemeVO;

import org.springframework.beans.factory.ListableBeanFactory;

public class GLUIManager extends ToftPanel implements ILinkQuery,ILinkAdd, ILinkMaintain{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UiManager manager = null;
	protected ListableBeanFactory factory;
	private CountDownLatch uilock = null;
	private boolean hasShowed = false;
	private boolean isLinkQuery = false;

	/**
	 * 会计平台联查来源的时候 展现三栏式明细帐
	 */
	public GLUIManager() {
		super();
		// hurh 这种方式导致assembleUI方法中必须启用两个互相依赖加载的线程，
		// 极有可能导致加载失败，将加载延迟到Init方法之后，即initdata里
//		uilock = new CountDownLatch(1);
//		assembleUI();

	}

	final protected void assembleUI() {

		add(new LoadingPanel());
		showUI();
	}
	
	protected void showUI() {
		if(hasShowed)
			return ;
		try {
			manager = new GlHandlerUiManager(null);
		} catch (RuntimeException e) {

		}
		GlWorkBench.getLoginYear();//add by zhaoshya 很多节点打开都会有同步后台时间远程调用， 平衡到打开节点这。
		//下面是4个支持双引擎的账表和2个需要联查的，打开节点就加载双引擎参数
		if(GlNodeConst.GLNODE_ANALYSIS.equals(getFuncletContext().getCurrFuncCode())
				||GlNodeConst.GLNODE_ASSATTRIQUERYBAL.equals(getFuncletContext().getCurrFuncCode())
				||GlNodeConst.GLNODE_ASSBALANCE.equals(getFuncletContext().getCurrFuncCode())
				||GlNodeConst.GLNODE_ASSDETAIL.equals(getFuncletContext().getCurrFuncCode())
				||GlNodeConst.GLNODE_BALANCEBOOK.equals(getFuncletContext().getCurrFuncCode())
				||GlNodeConst.GLNODE_MULTIORGSUBJBAL.equals(getFuncletContext().getCurrFuncCode())){
			try {
				IReportDBService rdbserv = NCLocator.getInstance().lookup(IReportDBService.class);
				ReportDBVO vo = rdbserv.getReportDBVOByCode("20");//财务
				FICheckChangeDataSrc.putMultDataSource(vo == null ? "N/A" : vo.getDsname());
			} catch (Exception e) {
				Logger.error(e.getMessage());
			}
		}
		((AbstractFunclet) manager).init(this.getFuncletContext());
		Object obj = manager.showNext(this.getParameter("classname"));
		String checkp = checkPrerequisite2();
		if (checkp != null && !"".equals(checkp)) {
			FiUif2MsgUtil.showUif2DetailMessage(this, "", new BusinessException(checkp));
			return;
		}
		if (obj instanceof AbstractFunclet) {
			((AbstractFunclet) obj).init(this.getFuncletContext());
		}
		this.removeAll();
		this.add(manager);
	}


	protected String checkPrerequisite2() {
		IUiPanel currPane = getCurrentPanel();
		if (currPane != null && currPane instanceof ToftPanel) {
			ToftPanel tp = (ToftPanel) currPane;
			try {
				Method m = ToftPanel.class.getDeclaredMethod("checkPrerequisite", new Class[] {});
				m.setAccessible(true);
				return (String) m.invoke(tp);
			} catch (Exception e) {
				Logger.error(e.getMessage(),e);
			}
		}
		return super.checkPrerequisite();
	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return manager.getTitle();
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO Auto-generated method stub
		manager.onButtonClicked(bo);
	}

	/**
	 * 从UIManager移植而来
	 * 
	 * @author congjl
	 * @since 2010-04-15
	 */
	public boolean onClosing() {
		try {
			// add by zhaoshya  由于内存泄露加上下面2行代码
			nc.ui.gl.vouchertools.VoucherDataCenter.getInstance().clearAll();
			ValueChangedAdaptor.clearListener(manager);
			//end add
			return manager.onClosing();
			// return ((ToftPanel)getCurrentPanel()).onClosing();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return true;
		}
	}

	/**
	 * 从UIManager移植而来
	 * 
	 * @author congjl
	 * @since 2010-04-15
	 */
	public void updateButtons() {
		manager.updateButtons();
	}

	/**
	 * 从UIManager移植而来
	 * 
	 * @author congjl
	 * @since 2010-04-15
	 */
	public void updateButton(ButtonObject bo) {
		manager.updateButton(bo);
	}

	/**
	 * @author zhaozh 2010-7-15 上午10:17:08 为联查功能加入
	 * @return
	 */
	public IUiPanel getCurrentPanel() {
		if (null == manager) {
			if(isLinkQuery)
				showLinkUI();
			else
				showUI();
			hasShowed = true;
		}
		return manager.getCurrentPanel();
	}

	private void showLinkUI() {
		if(hasShowed)
			return ;
		try {
			manager = new UiManager(null);
		} catch (RuntimeException e) {

		}
		((AbstractFunclet) manager).init(this.getFuncletContext());
		Object obj = manager.showNext("nc.ui.gl.pubvoucher.VoucherBridge");
		if (obj instanceof AbstractFunclet) {
			((AbstractFunclet) obj).init(this.getFuncletContext());
		}
		this.removeAll();
		this.add(manager);
	}

	public void initData(FuncletInitData data) {
		//ufo联查
		if(data!=null&&data.getInitData()!=null&&data.getInitData() instanceof GlQueryLinkUFOVO){
			Object querydata = data.getInitData();
			if (GlNodeConst.GLNODE_SEQUENCEBOOKS.equals(this.getFuncCode()) ) {
					nc.ui.gl.squencebooks.ToftPanelView view = (nc.ui.gl.squencebooks.ToftPanelView) getCurrentPanel();
					view.invoke(GLStringConst.LINKSQUENCE, querydata);
			}
			if (GlNodeConst.GLNODE_ASSBALANCE.equals(this.getFuncCode())) {
				nc.ui.gl.assbalance.ToftPanelView view = (nc.ui.gl.assbalance.ToftPanelView) getCurrentPanel();
				if (querydata instanceof GlQueryLinkUFOVO) {
					view.doQueryAction((ILinkQueryData)querydata);
				} else {
					view.invoke(GLStringConst.LINKASSBALANCE, querydata);
				}
	
			}
			if (GlNodeConst.GLNODE_DAILYREPORT.equals(this.getFuncCode())) {
				nc.ui.gl.dailyreport.ToftPanelView view = (nc.ui.gl.dailyreport.ToftPanelView) getCurrentPanel();
				view.doQueryAction((ILinkQueryData)querydata);
			}
			if (GlNodeConst.GLNODE_BALANCEBOOK.equals(this.getFuncCode())) {
				nc.ui.gl.balancebooks.ToftPanelView view = (nc.ui.gl.balancebooks.ToftPanelView) getCurrentPanel();
				if (querydata instanceof GlQueryLinkUFOVO) {
					view.doQueryAction((ILinkQueryData)querydata);
				} else {
					view.invoke(GLStringConst.LINKBALANCE, querydata);
				}
			}
			/**
			 * 三栏式明细账界面
			 */
			if (GlNodeConst.GLNODE_DETAIL.equals(this.getFuncCode())) {
				ToftPanelView pane = (ToftPanelView) getCurrentPanel();
				if (querydata instanceof GlQueryLinkUFOVO) {
					pane.invoke(querydata, "LinkUFO");
				} else {
					pane.invoke(querydata, "fipub4detail");
				}
	
			}
			/**
			 * 辅助明细账
			 */
			if (GlNodeConst.GLNODE_ASSDETAIL.equals(this.getFuncCode())) {
				nc.ui.gl.assdetail.ToftPanelView pane = (nc.ui.gl.assdetail.ToftPanelView) getCurrentPanel();
				if (querydata instanceof GlQueryLinkUFOVO) {
					pane.invoke(querydata, "LinkUFO");
				} 
	
			}
			/**
			 * 现金流量分析表
			 */
			if (GlNodeConst.GLNODE_CASHFLOW_ANALYZETABLE.equals(this.getFuncCode())) {
				CashFlowAnalyzeExUI view = (CashFlowAnalyzeExUI) getCurrentPanel();
				if (querydata instanceof GlQueryLinkUFOVO) {
					//联查到现金流量分析表明细界面
					if(((GlQueryLinkUFOVO) querydata).getQueryType().equals(TraceType.DETAIL.getType()+"")){
						view.invoke(querydata, "onLinkDetail");
					//联查到现金流量分析表汇总界面	
					}else{
						view.invoke(querydata, "onLinkQuery");
					}
				} 
			}
		}else{
			//为了区分联查单据还是直接打开总帐制单节点
			if(data != null && data.getInitType() == ILinkType.LINK_TYPE_QUERY) {
				Object initData = data.getInitData();
				boolean linkFlag = false;
				if(initData != null && initData instanceof ILinkQueryData) {
					
					ILinkQueryData linkQueryData = (ILinkQueryData) initData;
					
					Object userObject = linkQueryData.getUserObject();
					
					if(userObject != null && userObject instanceof Collection) {
						Collection collection = (Collection) userObject;
						if(collection.size()>0) {
							Object[] array = collection.toArray();
							if(array[0] instanceof VoucherVO) {
								linkFlag = true;
							}
						}
					}
				}
				
				if(linkFlag) {
					showLinkUI();
				}else {
					assembleUI();
				}
			}else {
				assembleUI();
			}
			
			if (data != null) {
				Object initData = data.getInitData();
				//快捷方式 联查 凭证 初始化数据
				if(initData != null && initData instanceof SimpleQuerySchemeVO) {
					FuncletContext funcletContext = getFuncletContext();
					IFunclet funclet = funcletContext.getFunclet();
					if(funclet != null && funclet instanceof IFuncNodeInitDataListener) {
						IFuncNodeInitDataListener initDataListener = (IFuncNodeInitDataListener) funclet;
						initDataListener.initData(data);
					}
				}else {
					super.initData(data);
				}
			}
		}
	}

	/**
	 * 联查总账数据（包括列表卡片） 单张凭证显示列表界面 多张凭证打开卡片界面
	 */
	@Override
	public void doQueryAction(ILinkQueryData querydata) {

		// todo 先按照节点判断，默认是预算
		if (GlNodeConst.GLNODE_SEQUENCEBOOKS.equals(this.getFuncCode()) ) {
			/**
			 * 如果是会计平台传过来的
			 */
//			if(querydata instanceof DefaultLinkData){
//				VoucherBridge bridage= null;
//				bridage=(VoucherBridge)getCurrentPanel();
//				this.manager.showNext(bridage);
//			}
//			else{
				nc.ui.gl.squencebooks.ToftPanelView view = (nc.ui.gl.squencebooks.ToftPanelView) getCurrentPanel();
				view.invoke(GLStringConst.LINKSQUENCE, querydata);
//			}
		}
		if (GlNodeConst.GLNODE_ASSBALANCE.equals(this.getFuncCode())) {
			nc.ui.gl.assbalance.ToftPanelView view = (nc.ui.gl.assbalance.ToftPanelView) getCurrentPanel();
			if (querydata instanceof GlQueryLinkUFOVO) {
				view.doQueryAction(querydata);
			} else {
				view.invoke(GLStringConst.LINKASSBALANCE, querydata);
			}

		}
		
		if (GlNodeConst.GLNODE_DAILYREPORT.equals(this.getFuncCode())) {
			nc.ui.gl.dailyreport.ToftPanelView view = (nc.ui.gl.dailyreport.ToftPanelView) getCurrentPanel();
			view.doQueryAction(querydata);
		}
		if (GlNodeConst.GLNODE_BALANCEBOOK.equals(this.getFuncCode())) {
			nc.ui.gl.balancebooks.ToftPanelView view = (nc.ui.gl.balancebooks.ToftPanelView) getCurrentPanel();
			if (querydata instanceof GlQueryLinkUFOVO) {
				view.doQueryAction(querydata);
			} else {
				view.invoke(GLStringConst.LINKBALANCE, querydata);
			}
		}
		if (GlNodeConst.GLNODE_VOUCHERPREPARE.equals(this.getFuncCode())) {
			isLinkQuery = true;
			try {
				Object obj = querydata.getUserObject();
				Collection<VoucherVO> vos = (Collection<VoucherVO>) obj;
				if(null != vos && vos.size()>0){
					// hurh 如果制单节点已经打开，类型不同
					if(getCurrentPanel() instanceof VoucherBridge){
						VoucherBridge bridge = (VoucherBridge) getCurrentPanel();
						bridge.setGenerateStyle( vos.size() == 1? 1: 2);
						bridge.setVoucher(vos.toArray(new VoucherVO[0]));
						/** 凭证联查不具有编辑权限*/
						((ListView)bridge.getVoucherUIP()).setCanEdit(false);
						// hurh
						bridge.setEditable(false);
						bridge.setLinkButtons();
						if(vos!=null && vos.size()==1){
							((ListView)bridge.getVoucherUIP()).openVoucher();
						}else if(vos != null && vos.size() > 1){
							// hurh V60连接数优化，联查列表时，加载缓存，注意当有多个核算账簿时，remotecall不支持，处理成多次调用
							HashMap<String, String> bookMap = new HashMap<String, String>();
							for(VoucherVO vo : vos){
								bookMap.put(vo.getPk_accountingbook(), vo.getPk_accountingbook());
							}
							for(String str : bookMap.keySet()){
								GlRemoteCallProxy.callProxy(CacheRequestFactory.newAddVoucherContextVO(getFuncCode(),str));
							}
						}
					}else if(getCurrentPanel() instanceof VoucherToftPanel){
						((VoucherToftPanel)getCurrentPanel()).setLinkButtons();
						((VoucherToftPanel)getCurrentPanel()).getVoucherPanel().getVoucherUI().setEditable(false);
						((VoucherToftPanel)getCurrentPanel()).openVoucher(vos.toArray(new VoucherVO[0])[0], false);
					}else if(getCurrentPanel() instanceof ListView){
						((ListView)getCurrentPanel()).setLinkButtons();
						((ListView)getCurrentPanel()).invoke(vos.toArray(new VoucherVO[0]), "setvouchers");
					}
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		/**
		 * 三栏式明细账界面
		 */
		if (GlNodeConst.GLNODE_DETAIL.equals(this.getFuncCode())) {
			ToftPanelView pane = (ToftPanelView) getCurrentPanel();
			if (querydata instanceof GlQueryLinkUFOVO) {
				pane.invoke(querydata, "LinkUFO");
			} else {
				pane.invoke(querydata, "fipub4detail");
			}

		}
		
		/**
		 * 辅助明细账
		 */
		if (GlNodeConst.GLNODE_ASSDETAIL.equals(this.getFuncCode())) {
			nc.ui.gl.assdetail.ToftPanelView pane = (nc.ui.gl.assdetail.ToftPanelView) getCurrentPanel();
			if (querydata instanceof GlQueryLinkUFOVO) {
				pane.invoke(querydata, "LinkUFO");
			} 

		}
		
//		//总额对账联查
//		  if (GlNodeConst.CONTRAST_QUERY.equals(this.getFuncCode())) {
//		   ResultClientUI view = (ResultClientUI) getCurrentPanel();
//		   view.doQueryAction(querydata);
//		  }

	}

	@Override
	/**
	 * @author zhaozh 2010-8-25 下午03:13:53
	 * 联查新增
	 */
	public void doAddAction(ILinkAddData adddata) {
		isLinkQuery = false;
		Object obj = adddata.getUserObject();
		if (null != obj && obj instanceof Collection) {
			Collection<VoucherVO> vos = (Collection<VoucherVO>) obj;
			ListView view = (ListView) getCurrentPanel();
			
			view.setLinkAddFlag(true);
			
			// hurh
			VoucherVO[] vouchers = vos.toArray(new VoucherVO[0]);
			for(VoucherVO voucher : vouchers){
				if(SystemtypeConst.TXM.equals(voucher.getPk_system())){ // 税务管理暂时处理方式
					continue;
				}
				voucher.setPk_voucher(null);
			}
//			try {
//				vouchers = GLPubProxy.getRemoteVoucher().catVouchers(vos.toArray(new VoucherVO[0]));
//			} catch (BusinessException e) {
//				Logger.error(e.getMessage(), e);
//				return;
//			}
			view.invoke(null, "FIPBATCHSAVE");
			//view.invoke(vos.toArray(new VoucherVO[0]), "setvouchers");
			view.invoke(vouchers, "setvouchers");
			if(vouchers!=null && vouchers.length==1){
				try {
					view.openVoucher();
				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
					Logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		if (GlNodeConst.GLNODE_ASSBALANCE.equals(this.getFuncCode())) {
			nc.ui.gl.assbalance.ToftPanelView view = (nc.ui.gl.assbalance.ToftPanelView) getCurrentPanel();
			view.doMaintainAction(maintaindata);
		}
		
	}
}
