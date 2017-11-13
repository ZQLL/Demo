package nc.ui.glrp.verify;

/**
 *  功能：核销标准选择条件
 *  作者：宋涛
 *  创建时间：(2003-5-8 10:01:29)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
 */
 import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import nc.bs.logging.Logger;
import nc.ui.gl.datacache.AccountCache;
import nc.ui.gl.view.LayoutPanel;
import nc.ui.glverifycom.AssChooser;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.vo.glrp.verify.VerifyStandardVO;
import nc.vo.glrp.verifyobj.VerifyObjHeaderVO;
import nc.vo.glrp.verifyobj.VerifyObjItemVO;
import nc.vo.glrp.verifyobj.VerifyObjVO;


@SuppressWarnings("serial")
public class VerifyStandardPane extends nc.ui.pub.beans.UIPanel {
	/*核销model*/
	private IVerifyModel m_verifymodel;
	private nc.ui.pub.beans.UICheckBox ivjchkDateRange = null;
	private nc.ui.pub.beans.UICheckBox ivjchkVerifyNo = null;
	private nc.ui.pub.beans.UICheckBox bankSerial = null;
	private nc.ui.pub.beans.UILabel ivjlbDateRangeTail = null;
	private nc.ui.pub.beans.UILabel ivjlbRangeHead = null;
	private nc.ui.pub.beans.UILabel ivjTitle = null;
	private nc.ui.pub.beans.UIPanel ivjAssPane = null;
	private nc.ui.pub.beans.UICheckBox ivjchkAss = null;
	private nc.ui.pub.beans.UICheckBox ivjchkDetailSubj = null;
	private nc.ui.pub.beans.UICheckBox ivjchkMny = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private nc.ui.glverifycom.AssChooser m_AssChooser;
	private nc.ui.pub.beans.UITextField ivjtfDateRange = null;

	private UICheckBox jCheckSelected = null;
class IvjEventHandler implements javax.swing.event.ChangeListener {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			if (e.getSource() == VerifyStandardPane.this.getchkDateRange())
				connEtoC1(e);
			if (e.getSource() == VerifyStandardPane.this.getchkAss())
				connEtoC2(e);
		};
	};
/**
 * VerifyStandardPane 构造子注解。
 */
public VerifyStandardPane() {
	super();
	initialize();
}
/**
 * VerifyStandardPane 构造子注解。
 * @param p0 java.awt.LayoutManager
 */
public VerifyStandardPane(java.awt.LayoutManager p0) {
	super(p0);
}
/**
 * VerifyStandardPane 构造子注解。
 * @param p0 java.awt.LayoutManager
 * @param p1 boolean
 */
public VerifyStandardPane(java.awt.LayoutManager p0, boolean p1) {
	super(p0, p1);
}
/**
 * VerifyStandardPane 构造子注解。
 * @param p0 boolean
 */
public VerifyStandardPane(boolean p0) {
	super(p0);
}
/**
 * 此处插入方法说明。
 * 创建日期：(2003-7-11 9:05:02)
 * 作者：卢文广
 * 功能：当用户选择了按核销对象设置的辅助核算进行核销时，需要检验用户是否选择了该辅助核算
 * 参数说明：
 * @return java.lang.String
 */
public String check() {
	try{
		VerifyObjVO verifyobj=m_verifymodel.getVerifyObject();
		if(((VerifyObjHeaderVO) verifyobj.getParentVO()).getBcontrol().booleanValue()){
			VerifyObjItemVO[] voItem = (VerifyObjItemVO[]) verifyobj.getChildrenVO();

			if(voItem!=null && voItem.length>0){
				nc.vo.glcom.ass.AssVO[] l_selectedAssVOs=getAssChooser().getAssResults();
				if(l_selectedAssVOs==null || l_selectedAssVOs.length<voItem.length){
					return nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000282")/*@res "请选择你在核销对象中设置的辅助核算！"*/;
				}
				else{
					for(int i=0;i<voItem.length;i++){
						String pk_ass=voItem[i].getPk_subjass();
						boolean l_flag=false;//判断是否包含
						for(int j=0;j<l_selectedAssVOs.length;j++){
							if(l_selectedAssVOs[j].getPk_Checktype().equals(pk_ass)){
								l_flag=true;
							}
						}
						if(!l_flag){
							return nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000282")/*@res "请选择你在核销对象中设置的辅助核算！"*/;
						}
					}
					return null;
				}
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}

	} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000283")/*@res "校验时出现异常！"*/;
		}
}
/**
 * Comment
 */
public void chkAss_PropertyChange(
	java.beans.PropertyChangeEvent propertyChangeEvent) {
	if (getchkAss().isSelected()) {
		try {
			getAssChooser().setPk_Subj(
				((VerifyObjHeaderVO) m_verifymodel.getVerifyObject().getParentVO())
					.getPk_accsubj(),
				true);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		getAssChooser().setEditable(true);
		getAssChooser().setEnabled(true);
		getAssChooser().setTypeEdited(true);
	} else {
		getAssChooser().setAssVOs(null);
		getAssChooser().setEnabled(false);
		getAssChooser().setEditable(false);
		getAssChooser().setTypeEdited(false);
	}
	return;
}
/**
 * Comment
 */
public void chkDateRange_PropertyChange(
    java.beans.PropertyChangeEvent propertyChangeEvent) {
    if (getchkDateRange().isSelected()) {
        gettfDateRange().setEditable(true);
        gettfDateRange().setEnabled(true);
    } else {
        gettfDateRange().setEnabled(false);
        gettfDateRange().setEnabled(false);
        gettfDateRange().setText(null);
    }
    return;
}
/**
 * connEtoC1:  (chkDateRange.change.stateChanged(javax.swing.event.ChangeEvent) --> VerifyStandardPane.chkDateRange_PropertyChange(Ljava.beans.PropertyChangeEvent;)V)
 * @param arg1 javax.swing.event.ChangeEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC1(javax.swing.event.ChangeEvent arg1) {
	try {
		this.chkDateRange_PropertyChange(null);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (chkAss.change.stateChanged(javax.swing.event.ChangeEvent) --> VerifyStandardPane.chkAss_PropertyChange(Ljava.beans.PropertyChangeEvent;)V)
 * @param arg1 javax.swing.event.ChangeEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC2(javax.swing.event.ChangeEvent arg1) {
	try {

		this.chkAss_PropertyChange(null);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-8 11:01:05)
 *  参数：<|>
 *  返回值：
 *  算法：
 *
 * @return nc.ui.glverifycom.AssChooser
 */
public nc.ui.glverifycom.AssChooser getAssChooser() {
	if (m_AssChooser == null) {
		try {
			m_AssChooser = new nc.ui.glverifycom.AssChooser(true,false);
			m_AssChooser.setName("ccc");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return m_AssChooser;
}
/**
 * 返回 AssPane 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getAssPane() {
	if (ivjAssPane == null) {
		try {
			ivjAssPane = new nc.ui.pub.beans.UIPanel();
			ivjAssPane.setName("AssPane");
			ivjAssPane.setLayout(new java.awt.BorderLayout());
//			ivjAssPane.setBounds(12, 156, 247, 82);
			ivjAssPane.setPreferredSize(new Dimension(290, 100));
			ivjAssPane.add(getAssChooser(),"Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjAssPane;
}
/**
 * 返回 chkAss 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getchkAss() {
	if (ivjchkAss == null) {
		try {
			ivjchkAss = new nc.ui.pub.beans.UICheckBox();
			ivjchkAss.setName("chkAss");
			ivjchkAss.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			ivjchkAss.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000284")/*@res "按辅助项："*/);
			ivjchkAss.setBounds(13, 124, 90, 22);
			ivjchkAss.setFocusPainted(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjchkAss;
}
/**
 * 返回 chkDateRange 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getchkDateRange() {
	if (ivjchkDateRange == null) {
		try {
			ivjchkDateRange = new nc.ui.pub.beans.UICheckBox();
			ivjchkDateRange.setName("chkDateRange");
			ivjchkDateRange.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000285")/*@res "按日期范围： "*/);
			ivjchkDateRange.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000286")/*@res "按日期范围："*/);
			ivjchkDateRange.setFocusPainted(false);
			ivjchkDateRange.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
//			ivjchkDateRange.setBounds(13, 42, 117, 22);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjchkDateRange;
}
/**
 * 返回 chkDetailSubj 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getchkDetailSubj() {
	if (ivjchkDetailSubj == null) {
		try {
			ivjchkDetailSubj = new nc.ui.pub.beans.UICheckBox();
			ivjchkDetailSubj.setName("chkDetailSubj");
			ivjchkDetailSubj.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			ivjchkDetailSubj.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000287")/*@res "按末级科目"*/);
//			ivjchkDetailSubj.setBounds(199, 76, 97, 22);
			ivjchkDetailSubj.setFocusPainted(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjchkDetailSubj;
}
/**
 * 返回 chkMny 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getchkMny() {
	if (ivjchkMny == null) {
		try {
			ivjchkMny = new nc.ui.pub.beans.UICheckBox();
			ivjchkMny.setName("chkMny");
			ivjchkMny.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			ivjchkMny.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000288")/*@res "金额相等"*/);
//			ivjchkMny.setBounds(106, 76, 80, 22);
			ivjchkMny.setFocusPainted(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjchkMny;
}
/**
 * 返回 chkVerifyNo 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getchkVerifyNo() {
	if (ivjchkVerifyNo == null) {
		try {
			ivjchkVerifyNo = new nc.ui.pub.beans.UICheckBox();
			ivjchkVerifyNo.setName("chkVerifyNo");
			ivjchkVerifyNo.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			ivjchkVerifyNo.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000289")/*@res "按核销号"*/);
//			ivjchkVerifyNo.setBounds(13, 76, 80, 22);
			ivjchkVerifyNo.setFocusPainted(false);
			ivjchkVerifyNo.setSelected(true);
			ivjchkVerifyNo.setEnabled(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjchkVerifyNo;
}
/**
 * 返回 chkVerifyNo 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getBankSerialNo() {
	if (bankSerial == null) {
		try {
			bankSerial = new nc.ui.pub.beans.UICheckBox();
			bankSerial.setName("BankSerialNo");
			bankSerial.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			bankSerial.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002001_0","02002001-0026")/*@res "按对账标识码"*/);
//			bankSerial.setBounds(12, 101, 118, 22);
			bankSerial.setFocusPainted(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return bankSerial;
}
/**
 * 返回 lbDateRangeTail 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getlbDateRangeTail() {
	if (ivjlbDateRangeTail == null) {
		try {
			ivjlbDateRangeTail = new nc.ui.pub.beans.UILabel();
			ivjlbDateRangeTail.setName("lbDateRangeTail");
			ivjlbDateRangeTail.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000290")/*@res "天内"*/);
			ivjlbDateRangeTail.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
//			ivjlbDateRangeTail.setBounds(253, 42, 43, 22);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjlbDateRangeTail;
}
/**
 * 返回 lbRangeHead 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getlbRangeHead() {
	if (ivjlbRangeHead == null) {
		try {
			ivjlbRangeHead = new nc.ui.pub.beans.UILabel();
			ivjlbRangeHead.setName("lbRangeHead");
			ivjlbRangeHead.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000291")/*@res " 日期相差"*/);
			ivjlbRangeHead.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
//			ivjlbRangeHead.setBounds(132, 42, 79, 22);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjlbRangeHead;
}
/**
 *  功能：得到核销标准vo
 *  作者：宋涛
 *  创建时间：(2003-5-12 13:43:52)
 *  参数：<|>
 *  返回值：
 *  算法：
 *
 * @return nc.vo.glrp.verify.VerifyStandardVO
 */
public VerifyStandardVO getStandardVO() {
	VerifyStandardVO vo = new VerifyStandardVO();
	java.util.ArrayList<String> al = new java.util.ArrayList<String>();
	if (getchkDateRange().isSelected()) {
		if (gettfDateRange().getText() != null
			&& gettfDateRange().getText().length() > 0) {
			vo.setDateRange(new Integer(gettfDateRange().getText().trim()));
		} else {
			vo.setDateRange(new Integer(0));
		}
	} else {
		vo.setDateRange(null);
	}
	if (getchkVerifyNo().isSelected()) {
		al.add("VerifyNo");
	}
	if (getBankSerialNo().isSelected()) {
		al.add("netbankflag");
	}
	if (getchkMny().isSelected()) {
		al.add("Mny");
	}
	if (getchkDetailSubj().isSelected()) {
		al.add("pk_accsubj");
	}
	/*等待控件完成！*/
	if (getchkAss().isSelected()) {
		getAssChooser().stopEditing();
		nc.vo.glcom.ass.AssVO[] voAss = getAssChooser().getAssResults();
		if (voAss != null) {
			for (int i = 0; i < voAss.length; i++) {
				al.add("fzhs_" + voAss[i].getPk_Checktype() + voAss[i].getPk_Checkvalue());
			}
		}
	}
	if (al.size() > 0) {
		String[] sItems = new String[al.size()];
		al.toArray(sItems);
		vo.setObj(sItems);
	} else {
		vo.setObj(null);
	}
	//begin--20041208--添加是否按已选对象进行自动核销的控制
	vo.setVerifySelected(getJCheckSelected().isSelected());
	//end
	return vo;
}
/**
 * 返回 tfDateRange 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField gettfDateRange() {
	if (ivjtfDateRange == null) {
		try {
			ivjtfDateRange = new nc.ui.pub.beans.UITextField();
			ivjtfDateRange.setName("tfDateRange");
//			ivjtfDateRange.setBounds(212, 42, 36, 20);
			ivjtfDateRange.setPreferredSize(new Dimension(36, 20));
			ivjtfDateRange.setTextType("TextInt");
			ivjtfDateRange.setMaxLength(3);
			ivjtfDateRange.setEditable(false);
			ivjtfDateRange.setEnabled(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjtfDateRange;
}
/**
 * 返回 Title 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getTitle() {
	if (ivjTitle == null) {
		try {
			ivjTitle = new nc.ui.pub.beans.UILabel();
			ivjTitle.setName("Title");
			ivjTitle.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			ivjTitle.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000292")/*@res "核销标准："*/);
//			ivjTitle.setBounds(13, 8, 150, 22);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjTitle;
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {
	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// nc.bs.logging.Logger.debug("--------- 未捕捉到的异常 ---------");
	// nc.bs.logging.Logger.error(exception.getMessage(), exception);
}
/**
 * 初始化连接
 * @exception java.lang.Exception 异常说明。
 */
/* 警告：此方法将重新生成。 */
private void initConnections() throws java.lang.Exception {
	getchkDateRange().addChangeListener(ivjEventHandler);
	getchkAss().addChangeListener(ivjEventHandler);
}
/**
 * 初始化数据
 * 创建日期：(2003-5-11 15:40:18)
 */
public void initData() {
	try {
		VerifyObjVO verifyobj = m_verifymodel.getVerifyObject();
		String pk_orgbook = ((VerifyObjHeaderVO)verifyobj.getParentVO()).getPk_glorgbook();
		getAssChooser().setPk_glorgbook(pk_orgbook);
		if (((VerifyObjHeaderVO) verifyobj.getParentVO())
			.getBcontrol()
			.booleanValue()) {
			VerifyObjItemVO[] voItem = (VerifyObjItemVO[]) verifyobj.getChildrenVO();
			getchkAss().setSelected(true);
			//////zsh
			if (getchkAss().isSelected())
			{
				try
				{
					getAssChooser().setPk_Subj(
						((VerifyObjHeaderVO) m_verifymodel.getVerifyObject().getParentVO())
							.getPk_accsubj(),
						true);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
				getAssChooser().setEditable(true);
				getAssChooser().setEnabled(true);
				getAssChooser().setTypeEdited(true);
			}
			else
			{
				getAssChooser().setAssVOs(null);
				getAssChooser().setEnabled(false);
				getAssChooser().setEditable(false);
				getAssChooser().setTypeEdited(false);
			}

			if (voItem != null && voItem.length > 0) {
				getchkAss().setEnabled(false);
			}
		} else {
			getchkAss().setSelected(false);
			getchkAss().setEnabled(true);
			getAssChooser().setEditable(true);

		}
		nc.vo.bd.account.AccountVO voSubj =
			AccountCache.getInstance().getAccountVOByPK(((VerifyObjHeaderVO)verifyobj.getParentVO()).getPk_glorgbook(),
				((VerifyObjHeaderVO) verifyobj.getParentVO()).getPk_accsubj());
		if(voSubj.getEndflag().booleanValue()){
			getchkDetailSubj().setSelected(true);
			getchkDetailSubj().setEnabled(false);
		}else{
			getchkDetailSubj().setSelected(false);
			getchkDetailSubj().setEnabled(true);
		}
	} catch (Exception e) {
		Logger.error(e.getMessage(), e);
	}
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		setName("VerifyStandardPane");
		setLayout(new FlowLayout(FlowLayout.LEFT));
		LayoutPanel panel1 = new LayoutPanel(2, 3);
		panel1.setOpaque(false);
		panel1.add(1, 1, getTitle());
		panel1.add(2, 1, getchkDateRange());
		panel1.add(2, 2, getlbRangeHead());
		UIPanel panel = new UIPanel(new BorderLayout());
		panel.add(gettfDateRange(),BorderLayout.WEST);
		panel.add(getlbDateRangeTail(),BorderLayout.CENTER);
		panel1.add(2, 3, panel);

		LayoutPanel panel2 = new LayoutPanel(1, 3);
		panel2.setOpaque(false);
		panel2.add(1, 1, getchkVerifyNo());
		panel2.add(1, 2, getchkMny());
		panel2.add(1, 3, getchkDetailSubj());
		
		LayoutPanel panel3 = new LayoutPanel(2, 2);
		panel3.setOpaque(false);
		panel3.add(1, 1, getBankSerialNo());
		panel3.add(1, 2, getJCheckSelected());
		panel3.add(2, 1, getchkAss());

		add(panel1);
		add(panel2);
		add(panel3);
		add(getAssPane());
//		add(getTitle(), getTitle().getName());
//		add(getchkDateRange(), getchkDateRange().getName());
//		add(getlbDateRangeTail(), getlbDateRangeTail().getName());
//		add(getlbRangeHead(), getlbRangeHead().getName());
//		add(getchkMny(), getchkMny().getName());
//		add(getchkDetailSubj(), getchkDetailSubj().getName());
//		add(getchkAss(), getchkAss().getName());
//		add(getAssPane(), getAssPane().getName());
//		add(getBankSerialNo(), getBankSerialNo().getName());
//		add(gettfDateRange(), gettfDateRange().getName());
//		add(getJCheckSelected(), null);
//		add(getchkVerifyNo(), getchkVerifyNo().getName());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		VerifyStandardPane aVerifyStandardPane;
		aVerifyStandardPane = new VerifyStandardPane();
		frame.setContentPane(aVerifyStandardPane);
		frame.setSize(aVerifyStandardPane.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
//		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("nc.ui.pub.beans.UIPanel 的 main() 中发生异常");
		nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}
}
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-8 11:01:05)
 *  参数：<|>
 *  返回值：
 *  算法：
 *
 * @param newAssChooser nc.ui.glverifycom.AssChooser
 */
public void setAssChooser(nc.ui.glverifycom.AssChooser newAssChooser) {
	m_AssChooser = newAssChooser;
}
/**
 * 设置核销model
 * 创建日期：(2003-5-11 15:36:49)
 * @param verifyModel nc.ui.glrp.verify.IVerifyModel
 */
public void setVerifyModel(IVerifyModel verifyModel) {
	m_verifymodel =verifyModel;
}
	/**
	 * This method initializes jCheckBox
	 *
	 * @return javax.swing.JCheckBox
	 */
	private UICheckBox getJCheckSelected() {
		if (jCheckSelected == null) {
			jCheckSelected = new UICheckBox();
//			jCheckSelected.setBounds(130, 105, 123, 21);
			jCheckSelected.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000293")/*@res "只核销已选记录"*/);
			jCheckSelected.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			jCheckSelected.setName("");
		}
		return jCheckSelected;
	}
	public void setStatus(int iButton){
		if(iButton==VerifyButtonKey.KEY_AUTOVERIFY){
			getchkVerifyNo().setVisible(true);

			//modified by chengsc 08-05-04
			getchkMny().setBounds(106, 76, 80, 22);
			//////////////////////////////////////
			getchkMny().setVisible(true);
			getchkDetailSubj().setVisible(true);
			getJCheckSelected().setVisible(true);
			getTitle().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000292")/*@res "核销标准："*/);
		}else if(iButton==VerifyButtonKey.KEY_AUTOREDBLUE){
			getchkVerifyNo().setVisible(false);

			//modified by chengsc 08-05-04
			getchkMny().setBounds(13, 76, 80, 22);
			getchkMny().setVisible(true);
			//////////////////////////////////////

			getchkDetailSubj().setVisible(false);
			getJCheckSelected().setVisible(false);
//			getTitle().setText("红蓝对冲标准：");
			getTitle().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000400")/*@res "红蓝对冲标准："*/);
		}
	}

 }