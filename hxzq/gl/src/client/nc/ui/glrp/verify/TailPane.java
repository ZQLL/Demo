package nc.ui.glrp.verify;

import java.awt.Component;
import java.awt.GridBagConstraints;

import nc.bs.logging.Logger;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.vo.gateway60.itfs.Currency;
import nc.vo.gateway60.pub.GlBusinessException;

/**
 *  ���ܣ�������ʾ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-4 16:23:45)
 *  ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ���
 *  ע�⣺�ִ�Bug
 */
@SuppressWarnings("serial")
public class TailPane extends nc.ui.pub.beans.UIPanel {
	private nc.ui.pub.beans.UILabel ivjlbName = null;
	private nc.ui.pub.beans.UILabel ivjlbName_b = null;
//	private nc.ui.pub.beans.UILabel ivjlbName_f = null;
	private nc.ui.pub.beans.UITextField ivjtfValue_b = null;
//	private nc.ui.pub.beans.UITextField ivjtfValue_f = null;
	private nc.ui.pub.beans.UITextField ivjtfValue_y = null;
	private nc.ui.pub.beans.UILabel ivjlbName_y = null;

//	private UILabel lab_j = null;
//	private UITextField txt_j = null;
//	private UILabel lab_q = null;
//	private UITextField txt_q = null;
/**
 * TailPane ������ע�⡣
 */
public TailPane() {
	super();
	initialize();
}
/**
 * TailPane ������ע�⡣
 * @param p0 java.awt.LayoutManager
 */
public TailPane(java.awt.LayoutManager p0) {
	super(p0);
}
/**
 * TailPane ������ע�⡣
 * @param p0 java.awt.LayoutManager
 * @param p1 boolean
 */
public TailPane(java.awt.LayoutManager p0, boolean p1) {
	super(p0, p1);
}
/**
 * TailPane ������ע�⡣
 * @param p0 boolean
 */
public TailPane(boolean p0) {
	super(p0);
}
/**
 * ���� lbName ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getlbName() {
	if (ivjlbName == null) {
		try {
			ivjlbName = new nc.ui.pub.beans.UILabel();
			ivjlbName.setName("lbName");
//			ivjlbName.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000261")/*@res "����:"*/);
			ivjlbName.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("subjverify_0","02002004-0021")/*@res "��Ҫʹ��setMsgName�������ø��������ʾ����"*/);
			//ivjlbName.setBackground(new java.awt.Color(245,233,209));
//			ivjlbName.setPreferredSize(new java.awt.Dimension(80, 22));
//			ivjlbName.setMaximumSize(ivjlbName.getPreferredSize());
//			ivjlbName.setMinimumSize(ivjlbName.getPreferredSize());
			ivjlbName.setRequestFocusEnabled(false);
			ivjlbName.setToolTipText(ivjlbName.getText());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjlbName;
}
/**
 * ���� lbName_b ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getlbName_b() {
	if (ivjlbName_b == null) {
		try {
			ivjlbName_b = new nc.ui.pub.beans.UILabel();
			ivjlbName_b.setName("lbName_b");
			ivjlbName_b.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("subjverify_0","02002004-0022")/*@res "��:"*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjlbName_b;
}
/**
 * ���� lbName_f ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
//private nc.ui.pub.beans.UILabel getlbName_f() {
//	if (ivjlbName_f == null) {
//		try {
//			ivjlbName_f = new nc.ui.pub.beans.UILabel();
//			ivjlbName_f.setName("lbName_f");
//			ivjlbName_f.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000263")/*@res "��:"*/);
//			//ivjlbName_f.setBackground(new java.awt.Color(245,233,209));
//			ivjlbName_f.setMaximumSize(new java.awt.Dimension(30, 22));
//			ivjlbName_f.setPreferredSize(new java.awt.Dimension(25, 22));
//			ivjlbName_f.setMinimumSize(new java.awt.Dimension(30, 22));
//			// user code begin {1}
////60x			ivjlbName_f.setVisible(ClientInfo.getInstance().bFracUsed());
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjlbName_f;
//}
/**
 * ���� lbName_y ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getlbName_y() {
	if (ivjlbName_y == null) {
		try {
			ivjlbName_y = new nc.ui.pub.beans.UILabel();
			ivjlbName_y.setName("lbName_y");
			ivjlbName_y.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000264")/*@res "ԭ:"*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjlbName_y;
}
/**
 * ���� tfValue_b ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField gettfValue_b() {
	if (ivjtfValue_b == null) {
		try {
			ivjtfValue_b = new nc.ui.pub.beans.UITextField();
			ivjtfValue_b.setName("tfValue_b");
			ivjtfValue_b.setText("");
			//ivjtfValue_b.setBackground(new java.awt.Color(245,233,209));
			ivjtfValue_b.setNumPoint(0);
			ivjtfValue_b.setDisabledTextColor(new java.awt.Color(245,233,209));
//			ivjtfValue_b.setPreferredSize(new java.awt.Dimension(90, 20));
			ivjtfValue_b.setEditable(false);
			ivjtfValue_b.setTextType("TextStr");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjtfValue_b;
}
/**
 * ���� tfValue_f ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
//private nc.ui.pub.beans.UITextField gettfValue_f() {
//	if (ivjtfValue_f == null) {
//		try {
//			ivjtfValue_f = new nc.ui.pub.beans.UITextField();
//			ivjtfValue_f.setName("tfValue_f");
//			ivjtfValue_f.setText("");
//			//ivjtfValue_f.setBackground(new java.awt.Color(245,233,209));
//			ivjtfValue_f.setNumPoint(0);
//			ivjtfValue_f.setDisabledTextColor(new java.awt.Color(245,233,209));
//			ivjtfValue_f.setPreferredSize(new java.awt.Dimension(90, 20));
//			ivjtfValue_f.setTextType("TextStr");
//			ivjtfValue_f.setEditable(false);
//			// user code begin {1}
////60x			ivjtfValue_f.setVisible(ClientInfo.getInstance().bFracUsed());
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjtfValue_f;
//}
/**
 * ���� tfValue_y ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField gettfValue_y() {
	if (ivjtfValue_y == null) {
		try {
			ivjtfValue_y = new nc.ui.pub.beans.UITextField();
			ivjtfValue_y.setName("tfValue_y");
			ivjtfValue_y.setAutoscrolls(false);
			ivjtfValue_y.setText("");
			//ivjtfValue_y.setBackground(new java.awt.Color(245,233,209));
			ivjtfValue_y.setNumPoint(0);
			ivjtfValue_y.setDisabledTextColor(new java.awt.Color(245,233,209));
//			ivjtfValue_y.setPreferredSize(new java.awt.Dimension(90, 20));
			ivjtfValue_y.setEditable(false);
			ivjtfValue_y.setTextType("TextStr");
			ivjtfValue_y.setRequestFocusEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjtfValue_y;
}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		 nc.bs.logging.Logger.debug("--------- δ��׽�����쳣 ---------");
		 Logger.error(exception.getMessage(), exception);
		throw new GlBusinessException(exception.getMessage());
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-5-10 22:25:39)
	 */
	public void initData() {
		gettfValue_b().setText("");
//		gettfValue_f().setText("");
		gettfValue_y().setText("");
	}
	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			setName("TailPane");
			setLayout(new java.awt.GridBagLayout());
			setSize(539, 34);

			java.awt.GridBagConstraints constraintslbName = new java.awt.GridBagConstraints();
			constraintslbName.insets = new java.awt.Insets(5, 18, 7, 0);
			constraintslbName.weightx = 0;
			add(getlbName(), constraintslbName, GridBagConstraints.RELATIVE, 1,
					2, 1);

			java.awt.GridBagConstraints constraintslbName_y = new java.awt.GridBagConstraints();
			constraintslbName_y.gridx = 2; constraintslbName_y.gridy = 1;
			constraintslbName_y.insets = new java.awt.Insets(5, 3, 7, 3);
			constraintslbName_y.anchor = GridBagConstraints.EAST;
			add(getlbName_y(), constraintslbName_y,GridBagConstraints.RELATIVE, 1, 1, 2);

			java.awt.GridBagConstraints constraintstfValue_y = new java.awt.GridBagConstraints();
			constraintstfValue_y.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintstfValue_y.insets = new java.awt.Insets(7, 2, 7, 8);
			constraintstfValue_y.weightx = 100;
			constraintstfValue_y.anchor = GridBagConstraints.WEST;
			add(gettfValue_y(), constraintstfValue_y,
					GridBagConstraints.RELATIVE, 1, 1, 2);


			GridBagConstraints constraintslbName_b = new java.awt.GridBagConstraints();
			constraintslbName_b.insets = new java.awt.Insets(5, 3, 7, 2);
			constraintslbName_b.anchor = GridBagConstraints.EAST;
			add(getlbName_b(), constraintslbName_b,GridBagConstraints.RELATIVE, 1, 1, 2);

			java.awt.GridBagConstraints constraintstfValue_b = new java.awt.GridBagConstraints();
			constraintstfValue_b.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintstfValue_b.insets = new java.awt.Insets(7, 2, 7, 8);
			constraintstfValue_b.weightx = 100;
			constraintstfValue_b.anchor = GridBagConstraints.WEST;
			add(gettfValue_b(), constraintstfValue_b,GridBagConstraints.RELATIVE, 1, 1, 1);

			if(Currency.isStartGroupCurr(GlWorkBench.getLoginGroup())){
//				GridBagConstraints constraintslbName_j = new java.awt.GridBagConstraints();
//				constraintslbName_j.insets = new java.awt.Insets(5, 3, 7, 2);
//				constraintslbName_j.anchor = GridBagConstraints.EAST;
//				add(getLab_j(), constraintslbName_j,GridBagConstraints.RELATIVE, 1, 1, 2);

//				java.awt.GridBagConstraints constraintstfValue_j = new java.awt.GridBagConstraints();
//				constraintstfValue_j.fill = java.awt.GridBagConstraints.HORIZONTAL;
//				constraintstfValue_j.insets = new java.awt.Insets(7, 2, 7, 8);
//				constraintstfValue_j.weightx = 100;
//				constraintstfValue_j.anchor = GridBagConstraints.WEST;
//				add(getTxt_j(), constraintstfValue_j,GridBagConstraints.RELATIVE, 1, 1, 1);
			}
			if(Currency.isStartGlobalCurr()){
//				GridBagConstraints constraintslbName_q = new java.awt.GridBagConstraints();
//				constraintslbName_q.insets = new java.awt.Insets(5, 3, 7, 2);
//				constraintslbName_q.anchor = GridBagConstraints.EAST;
//				add(getLab_q(), constraintslbName_q,GridBagConstraints.RELATIVE, 1, 1, 2);

//				java.awt.GridBagConstraints constraintstfValue_q = new java.awt.GridBagConstraints();
//				constraintstfValue_q.fill = java.awt.GridBagConstraints.HORIZONTAL;
//				constraintstfValue_q.insets = new java.awt.Insets(7, 2, 7, 8);
//				constraintstfValue_q.weightx = 100;
//				constraintstfValue_q.anchor = GridBagConstraints.WEST;
//				add(getTxt_q(), constraintstfValue_q,GridBagConstraints.RELATIVE, 1, 1, 1);
			}

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}

	}

	public void add(Component c, GridBagConstraints gbc, int x, int y, int gridwidth,
			int gridheight) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weighty = 0;
		add(c, gbc);
	}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		TailPane aTailPane;
		aTailPane = new TailPane();
		frame.setContentPane(aTailPane);
		frame.setSize(aTailPane.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("nc.ui.pub.beans.UIPanel �� main() �з����쳣");
		nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}
}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-22 9:12:54)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param bDebit boolean
 */
public void setDebit(boolean bDebit) {
	//if(bDebit){
		//getlbName().setText("�跽���κ������:");
	//}else{
		//getlbName().setText("�������κ������:");
	//}
}

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-9 12:52:21) ������<|> ����ֵ�� �㷨��
	 *
	 * @param snewName
	 *            java.lang.String
	 */
	public void setMsgName(String snewName) {
		getlbName().setText(snewName == null ? "" : snewName);
		getlbName().setToolTipText(snewName == null ? "" : snewName);
	}
/**
 * a���ܣ�
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-9 12:52:53)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param snewValue java.lang.String
 */
public void setValue(String snewValue) {
	//getlbValue().setText(snewValue==null?"":snewValue);
}
/**
 *  ���ܣ����ñ���ֵ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-22 9:46:52)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param sValue java.lang.String
 */
public void setValue_b(String sValue) {
	gettfValue_b().setText(sValue);
	gettfValue_b().setToolTipText(sValue);
}
/**
 *  ���ܣ����ø���ֵ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-22 9:46:52)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param sValue java.lang.String
 */
//public void setValue_f(String sValue) {
//	gettfValue_f().setText(sValue);
//	gettfValue_f().setToolTipText(sValue);
//}
/**
 *  ���ܣ�����ԭ��ֵ
 *  ���ߣ�����
 *  ����ʱ�䣺(2003-5-22 9:46:52)
 *  ������<|>
 *  ����ֵ��
 *  �㷨��
 *
 * @param sValue java.lang.String
 */
public void setValue_y(String sValue) {
	gettfValue_y().setText(sValue);
	gettfValue_y().setToolTipText(sValue);
}

//	private UILabel getLab_j() {
//		if (lab_j == null) {
//			try {
//				lab_j = new UILabel();
//				lab_j.setName("lbName_b");
//				lab_j.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("subjverify_0","02002004-0023")/*@res "��:"*/);
//				// user code begin {1}
//				// user code end
//			} catch (java.lang.Throwable ivjExc) {
//				// user code begin {2}
//				// user code end
//				handleException(ivjExc);
//			}
//		}
//		return lab_j;
//	}

//	private nc.ui.pub.beans.UITextField getTxt_j() {
//		if (txt_j == null) {
//			try {
//				txt_j = new nc.ui.pub.beans.UITextField();
//				txt_j.setName("txt_j");
//				txt_j.setAutoscrolls(false);
//				txt_j.setText("");
//				// ivjtfValue_y.setBackground(new java.awt.Color(245,233,209));
//				txt_j.setNumPoint(0);
//				txt_j.setDisabledTextColor(new java.awt.Color(245, 233, 209));
//				txt_j.setPreferredSize(new java.awt.Dimension(90, 20));
//				txt_j.setEditable(false);
//				txt_j.setTextType("TextStr");
//				txt_j.setRequestFocusEnabled(false);
//				// user code begin {1}
//				// user code end
//			} catch (java.lang.Throwable ivjExc) {
//				// user code begin {2}
//				// user code end
//				handleException(ivjExc);
//			}
//		}
//		return txt_j;
//	}

//	public void setTxt_j(String value) {
//		getTxt_j().setText(value);
//		getTxt_j().setToolTipText(value);
//	}

//	private UILabel getLab_q() {
//		if (lab_q == null) {
//			try {
//				lab_q = new UILabel();
//				lab_q.setName("lbName_b");
//				lab_q.setText(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("subjverify_0","02002004-0024")/*@res "ȫ:"*/);
//			} catch (java.lang.Throwable ivjExc) {
//				handleException(ivjExc);
//			}
//		}
//		return lab_q;
//	}

//	private nc.ui.pub.beans.UITextField getTxt_q() {
//		if (txt_q == null) {
//			try {
//				txt_q = new nc.ui.pub.beans.UITextField();
//				txt_q.setName("txt_q");
//				txt_q.setAutoscrolls(false);
//				txt_q.setText("");
//				// ivjtfValue_y.setBackground(new java.awt.Color(245,233,209));
//				txt_q.setNumPoint(0);
//				txt_q.setDisabledTextColor(new java.awt.Color(245, 233, 209));
//				txt_q.setPreferredSize(new java.awt.Dimension(90, 20));
//				txt_q.setEditable(false);
//				txt_q.setTextType("TextStr");
//				txt_q.setRequestFocusEnabled(false);
//			} catch (java.lang.Throwable ivjExc) {
//				handleException(ivjExc);
//			}
//		}
//		return txt_q;
//	}

//	public void setTxt_q(String value) {
//		getTxt_q().setText(value);
//		getTxt_q().setToolTipText(value);
//	}

}