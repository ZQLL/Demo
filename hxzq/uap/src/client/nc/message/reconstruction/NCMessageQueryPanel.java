package nc.message.reconstruction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.action.AbstractNCAction;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.message.msgcenter.msgbox.IMessageBox;
import nc.message.msgcenter.msgbox.MessageBoxUtil;
import nc.message.msgtype.vo.MsgTypeCache;
import nc.message.msgtype.vo.MsgTypeUtils;
import nc.message.msgtype.vo.MsgTypeVO;
import nc.ui.bd.ref.DeptDefaultRefModel2;
import nc.ui.bd.ref.busi.BillTypeDefaultRefModel;
import nc.ui.bd.ref.busi.UserDefaultRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.plaf.basic.BasicActionsBarUI;
import nc.ui.pub.beans.ActionsBar;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.style.Style;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.pub.BusinessException;
import net.miginfocom.swing.MigLayout;

public class NCMessageQueryPanel extends JPanel implements MessageEventListener {
	private static final long serialVersionUID = -1242880109922663265L;

	private JToolBar msgbasebar;
	private HyperAction week;
	private HyperAction omonth;
	private HyperAction tmonth;
	private JPanel qpanel;
	private UICheckBox cbreadbox;

	private JLabel lbsubject;
	private JTextField tfsubject;
	private JLabel lbsender;
	private UIRefPane userref;
	// add by lihaibo
	private JLabel lbbm = null;// 部门
	private UIRefPane bmref = null;// 部门参照
	private JLabel billtype = null;// 单据类型
	private UIRefPane billtyperef = null;// 单据类型参照

	private JLabel lbfrom;
	private JLabel lbdate;
	private JLabel msgtitle;
	private ActionsBar doquery;
	private JToggleButton querybtn;
	private DefaultConstEnum[] combodata;
	private DefaultConstEnum[] fromdata;

	@SuppressWarnings("rawtypes")
	private JComboBox cbbfrom;

	@SuppressWarnings("rawtypes")
	private JComboBox cbbtime;

	private ButtonGroup group;

	private boolean isQuik = true;

	private MessageCenterModel msgmodel;

	public NCMessageQueryPanel() {
		super();
		initModel();
		initUI();
	}

	private void initModel() {
		msgmodel = MessageCenterModel.getInstance();
		msgmodel.addAppEventListener(this);
	}

	private void initUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder());
		add(getMsgBaseBar(), BorderLayout.NORTH);
		add(getQueryPanel(), BorderLayout.SOUTH);
	}

	@SuppressWarnings("serial")
	private JToolBar getMsgBaseBar() {
		if (msgbasebar == null) {
			msgbasebar = new JToolBar() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;
					Paint oldPaint = g2d.getPaint();
					Rectangle gRect = new Rectangle(0, 0, this.getWidth(),
							this.getHeight());
					GradientPaint gp = new GradientPaint(gRect.x, gRect.y,
							new Color(0xffffff), gRect.x, gRect.y
									+ gRect.height, new Color(0xe5e5e5));
					g2d.setPaint(gp);
					g2d.fill(gRect);
					g2d.setPaint(oldPaint);
				}
			};
			msgbasebar.setPreferredSize(new Dimension(220, 32));
			msgbasebar.setFloatable(false);
			msgbasebar.setBorder(new ButtomLineBorder(
					MessageCenterUIConst.QUREYBARBORDER));
			msgbasebar.setOpaque(false);
			msgbasebar.addSeparator(new Dimension(10, 32));
			msgbasebar.add(getlbtitle());
			msgbasebar.add(getBarElemPanel());
			msgbasebar.setRollover(true);
		}
		return msgbasebar;
	}

	@SuppressWarnings("serial")
	private JPanel getQueryPanel() {
		if (qpanel == null) {
			qpanel = new JPanel() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;
					Paint oldPaint = g2d.getPaint();
					Rectangle gRect = new Rectangle(0, 0, this.getWidth(),
							this.getHeight());
					GradientPaint gp = new GradientPaint(gRect.x, gRect.y,
							new Color(0xffffff), gRect.x, gRect.y
									+ gRect.height, new Color(0xf0f0f0));
					g2d.setPaint(gp);
					g2d.fill(gRect);
					g2d.setPaint(oldPaint);
				}
			};

			qpanel.setPreferredSize(new Dimension(getQueryPanel().getWidth(),
					32));
			qpanel.setBorder(new ButtomLineBorder(
					MessageCenterUIConst.DISPQUREYBORDER));
			qpanel.setLayout(new BorderLayout());

			JPanel filterpanel = new JPanel();
			filterpanel.setOpaque(false);
			filterpanel
					.setLayout(new MigLayout(
							"",
							"[]rel[]unrel[]unrel[]rel[]unrel[]unrel[]rel[]unrel[]rel[]",
							"[center]"));
			filterpanel.add(getlbsubject());
			filterpanel.add(gettfsubject());

			filterpanel.add(getSepratorPane(30, 5));
			filterpanel.add(getlbsender());
			filterpanel.add(getUserRef());

			filterpanel.add(getSepratorPane(30, 5));
			filterpanel.add(getBillType());// 单据类型
			filterpanel.add(getBillTypeRef());// 单据类型参照

			filterpanel.add(getSepratorPane(30, 5));
			filterpanel.add(getlbBm());// 部门
			filterpanel.add(getBmRef());// 部门主键

			// filterpanel.add(getSepratorPane(30, 5));
			// filterpanel.add(getlbfrom());
			// filterpanel.add(gettffrom());

			filterpanel.add(getSepratorPane(30, 5));
			filterpanel.add(getlbdate());
			filterpanel.add(getCbbtime());

			JPanel doquerypanel = new JPanel();
			doquerypanel.setOpaque(false);
			doquerypanel.add(getdoQeury());
			doquerypanel.add(new JSeparator());

			qpanel.add(filterpanel, BorderLayout.CENTER);
			qpanel.add(doquerypanel, BorderLayout.EAST);
		}
		return qpanel;
	}

	private JPanel getSepratorPane(int width, int hight) {
		JPanel sp = new JPanel();
		sp.setBorder(BorderFactory.createEmptyBorder());
		sp.setPreferredSize(new Dimension(width, hight));
		sp.setOpaque(false);
		return sp;
	}

	private JLabel getlbsubject() {
		if (lbsubject == null) {
			lbsubject = new JLabel();
			lbsubject.setText(NCLangRes.getInstance().getStrByID("ncmessage",
					"msgboxres-000016")/* 主题 */);
		}
		return lbsubject;
	}

	private JTextField gettfsubject() {
		if (tfsubject == null) {
			tfsubject = new JTextField();
			tfsubject.setPreferredSize(new Dimension(120, 20));
		}
		return tfsubject;
	}

	private JLabel getlbsender() {
		if (lbsender == null) {
			lbsender = new JLabel();
			lbsender.setText(NCLangRes.getInstance().getStrByID("ncmessage",
					"msgboxres-000017")/* 发送人 */);
		}
		return lbsender;
	}

	private UIRefPane getUserRef() {
		if (userref == null) {
			userref = new UIRefPane();
			UserDefaultRefModel userrefmodel = new UserDefaultRefModel();
			userrefmodel.setAllUserVisible(true);
			userref.setRefModel(userrefmodel);
			userref.setPreferredSize(new Dimension(120, 20));
		}
		return userref;
	}

	private JLabel getlbBm() {
		if (lbbm == null) {
			lbbm = new JLabel();
			lbbm.setText(NCLangRes.getInstance().getStrByID("ncmessage", "部门")/* 部门 */);
		}
		return lbbm;
	}

	private UIRefPane getBmRef() {
		if (bmref == null) {
			bmref = new UIRefPane();
			DeptDefaultRefModel2 deptrefmodel = new DeptDefaultRefModel2();
			// deptrefmodel.setRootVisible(true);
			bmref.setRefModel(deptrefmodel);
			bmref.setPreferredSize(new Dimension(120, 20));
		}
		return bmref;
	}

	private JLabel getBillType() {
		if (billtype == null) {
			billtype = new JLabel();
			billtype.setText(NCLangRes.getInstance().getStrByID("ncmessage",
					"单据类型")/* 部门 */);
		}
		return billtype;
	}

	private UIRefPane getBillTypeRef() {
		if (billtyperef == null) {
			billtyperef = new UIRefPane();
			BillTypeDefaultRefModel billtyperefmodel = new BillTypeDefaultRefModel(
					null);
			billtyperefmodel
					.setWherePart("pk_billtypecode like '263X-Cxx-%' or pk_billtypecode like '264X-Cxx-%'");
			// deptrefmodel.setRootVisible(true);
			billtyperef.setRefModel(billtyperefmodel);
			billtyperef.setPreferredSize(new Dimension(120, 20));
		}
		return billtyperef;
	}

	private JLabel getlbfrom() {
		if (lbfrom == null) {
			lbfrom = new JLabel();
			lbfrom.setText(NCLangRes.getInstance().getStrByID("ncmessage",
					"msgboxres-000018")/* 来源 */);
		}
		return lbfrom;
	}

	@SuppressWarnings("rawtypes")
	private JComboBox gettffrom() {
		if (cbbfrom == null) {
			cbbfrom = new JComboBox();
			cbbfrom.setPreferredSize(new Dimension(120, 20));
		}
		return cbbfrom;
	}

	private JLabel getlbdate() {
		if (lbdate == null) {
			lbdate = new JLabel();
			lbdate.setText(NCLangRes.getInstance().getStrByID("ncmessage",
					"msgboxres-000019")/* 日期 */);
		}
		return lbdate;
	}

	private JLabel getlbtitle() {
		if (msgtitle == null) {
			msgtitle = new JLabel();
			msgtitle.setText(NCLangRes.getInstance().getStrByID("ncmessage",
					"msgboxres-000020")/* 消息类型 */);
		}
		return msgtitle;
	}

	@SuppressWarnings("rawtypes")
	private JComboBox getCbbtime() {
		if (cbbtime == null) {
			cbbtime = new JComboBox();
			cbbtime.setPreferredSize(new Dimension(120, 20));
			initComboData();
		}
		return cbbtime;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComboData() {
		combodata = new DefaultConstEnum[6];
		combodata[0] = new DefaultConstEnum(-1, "");
		combodata[1] = new DefaultConstEnum(7, NCLangRes.getInstance()
				.getStrByID("ncmessage", "msgboxres-000021")/* 一周内 */);
		combodata[2] = new DefaultConstEnum(30, NCLangRes.getInstance()
				.getStrByID("ncmessage", "msgboxres-000022")/* 一个月内 */);
		combodata[3] = new DefaultConstEnum(90, NCLangRes.getInstance()
				.getStrByID("ncmessage", "msgboxres-000023")/* 三个月内 */);
		combodata[4] = new DefaultConstEnum(182, NCLangRes.getInstance()
				.getStrByID("ncmessage", "msgboxres-000024")/* 半年内 */);
		combodata[5] = new DefaultConstEnum(365, NCLangRes.getInstance()
				.getStrByID("ncmessage", "msgboxres-000025")/* 一年内 */);
		DefaultComboBoxModel cbmodel = new DefaultComboBoxModel(combodata);
		getCbbtime().setModel(cbmodel);
	}

	private ActionsBar getdoQeury() {
		if (doquery == null) {
			doquery = new ActionsBar();
			doquery.setShowActionText(true);
			doquery.setOpaque(false);
			doquery.setBtnHeight(20);
			doquery.addAction(new QryAction("qry", NCLangRes.getInstance()
					.getStrByID("ncmessage", "msgboxres-000026")/* 查询 */));
		}
		return doquery;
	}

	@SuppressWarnings("serial")
	private class QryAction extends AbstractNCAction {

		public QryAction(String code, String name) {
			super(code, name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			MsgQueryInfo info = getCurrQueryInfo();
			info.setQiukQry(false);
			fireQuery(info);
			isQuik = false;
		}
	}

	@SuppressWarnings("serial")
	private JPanel getBarElemPanel() {
		JPanel elempane = new JPanel();
		elempane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 4));
		elempane.setOpaque(false);
		ActionsBar abar = new ActionsBar() {
			@Override
			protected void setUI(ComponentUI newUI) {
				super.setUI(new BasicActionsBarUI() {
					@Override
					protected AbstractButton createButton(Action action) {
						AbstractButton btn = super.createButton(action);
						btn.setFocusPainted(false);
						return btn;
					}
				});
			}
		};

		abar.setShowActionText(true);
		abar.addAction(getWeekHyper());
		abar.addAction(getOmonthHyper());
		abar.addAction(getTmonthHyper());
		abar.setBtnHeight(20);
		abar.setOpaque(false);
		elempane.add(abar);

		JSeparator timebox = new JSeparator();
		timebox.setPreferredSize(new Dimension(50, getPreferredSize().height));
		elempane.add(timebox);
		elempane.add(getReadBox());
		JSeparator boxquery = new JSeparator();
		boxquery.setPreferredSize(new Dimension(35, getPreferredSize().height));
		elempane.add(boxquery);
		elempane.add(getQueryButton());
		elempane.add(new JSeparator());

		return elempane;
	}

	private ButtonGroup getTimeBtnGrp() {
		if (group == null)
			group = new ButtonGroup();
		return group;
	}

	private UICheckBox getReadBox() {
		if (cbreadbox == null) {
			// update by lihaibo
			//cbreadbox = new UICheckBox(NCLangRes.getInstance().getStrByID(
			//		"ncmessage", "msgboxres-000027")/* 包含已读消息 */);
			cbreadbox = new UICheckBox("已处理消息");
			cbreadbox.setOpaque(false);
			cbreadbox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MsgQueryInfo info = getCurrQueryInfo();
					info.setQiukQry(isQuik);
					fireQuery(info);
				}
			});
		}
		return cbreadbox;
	}

	private JToggleButton getQueryButton() {
		if (querybtn == null) {
			querybtn = new JToggleButton();
			querybtn.setText(NCLangRes.getInstance().getStrByID("ncmessage",
					"msgboxres-000026")/* 查询 */);
			querybtn.setIconTextGap(10);
			querybtn.setIcon(ThemeResourceCenter.getInstance().getImage(
					"themeres/infocenter/shrink.png"));
			querybtn.setHorizontalTextPosition(SwingConstants.LEFT);
			querybtn.setUI(new QueryUI());
			querybtn.setSelected(false);
			querybtn.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
			querybtn.setOpaque(false);
			querybtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			querybtn.setBorder(BorderFactory.createEmptyBorder());
			querybtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doQueryPanelLayout();
				}
			});
		}
		return querybtn;
	}

	private void doQueryPanelLayout() {
		if (getQueryButton().isSelected()) {
			getQueryPanel().setPreferredSize(
					new Dimension(getQueryPanel().getWidth(), 35));
			getQueryButton().setText(
					NCLangRes.getInstance().getStrByID("ncmessage",
							"msgboxres-000028")/* 隐藏 */);
			getQueryButton().setIcon(
					ThemeResourceCenter.getInstance().getImage(
							"themeres/infocenter/stretch.png"));
		} else {
			getQueryPanel().setPreferredSize(
					new Dimension(getQueryPanel().getWidth(), 0));
			getQueryButton().setText(
					NCLangRes.getInstance().getStrByID("ncmessage",
							"msgboxres-000026")/* 查询 */);
			getQueryButton().setIcon(
					ThemeResourceCenter.getInstance().getImage(
							"themeres/infocenter/shrink.png"));
		}
		getQueryPanel().getParent().doLayout();
		repaint();
	}

	private HyperAction getWeekHyper() {
		if (week == null) {
			week = new HyperAction("oneweek", NCLangRes.getInstance()
					.getStrByID("ncmessage", "msgboxres-000021")/* 一周内 */, true);
			week.putValue(Action.ACTION_COMMAND_KEY, "7");
		}
		return week;
	}

	private HyperAction getOmonthHyper() {
		if (omonth == null) {
			omonth = new HyperAction("onemon", NCLangRes.getInstance()
					.getStrByID("ncmessage", "msgboxres-000029")/* 一月内 */,
					false);
			omonth.putValue(Action.ACTION_COMMAND_KEY, "30");
		}
		return omonth;
	}

	private HyperAction getTmonthHyper() {
		if (tmonth == null) {
			tmonth = new HyperAction("threemon", NCLangRes.getInstance()
					.getStrByID("ncmessage", "msgboxres-000030")/* 三月内 */,
					false);
			tmonth.putValue(Action.ACTION_COMMAND_KEY, "90");
		}
		return tmonth;
	}

	@SuppressWarnings("serial")
	private class HyperAction extends AbstractNCAction {
		private boolean selected = false;

		public HyperAction(String code, String name, boolean selected) {
			super(code, name);
			this.selected = selected;
			init();
		}

		private void init() {
			putValue(SELECTED_KEY, selected);
			putValue("buttonGroup", getTimeBtnGrp());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			MsgQueryInfo info = getCurrQueryInfo();
			info.setQiukQry(true);
			fireQuery(info);
			clearQuerySet();
			isQuik = true;
		}

		public void setSelected(boolean isselected) {
			selected = isselected;
			putValue(SELECTED_KEY, selected);
		}
	}

	private class QueryUI extends BasicButtonUI {
		protected void paintText(Graphics g, JComponent c, Rectangle textRect,
				String text) {
			super.paintText(g, c, textRect, text);
		}
	}

	private void clearQuerySet() {
		gettfsubject().setText("");
		getUserRef().setPK(null);
		getBmRef().setPK(null);// 部门主键
		getBillTypeRef().setPK(null);// 单据类型主键
		gettffrom().setSelectedIndex(0);
		getCbbtime().setSelectedIndex(0);
	}

	private MsgQueryInfo getCurrQueryInfo() {
		getUserRef().stopEditing();
		getBmRef().stopEditing();// 部门参照
		getBillTypeRef().stopEditing();// 单据类型参照
		MsgQueryInfo info = new MsgQueryInfo();
		info.setShowQry(getQueryButton().isSelected());
		info.setHandle(getReadBox().isSelected());
		info.setQuikdate(getTimeBtnGrp().getSelection().getActionCommand());
		info.setSubject(gettfsubject().getText());
		info.setSender(getUserRef().getRefPKs());
		String[] btypes = getBillTypeRef().getRefPKs();// 单据类型参照
		if (btypes != null && btypes.length > 0) {
			info.setBillType(btypes);// 单据类型参照
		}
		String[] bmpks = getBmRef().getRefPKs();// 部门参照
		if (bmpks != null && bmpks.length > 0) {
			String bmpk = bmpks[0];
//			 bmpk = "1001A1100000000009RF";
			try {
				Vector vec = (Vector) NCLocator
						.getInstance()
						.lookup(IUAPQueryBS.class)
						.executeQuery(
								"select distinct(b.detail) from er_bxzb a,sm_msg_content b WHERE a.deptid = '"
										+ bmpk
										+ "' AND instr(b.detail, a.pk_jkbx) > 0 and b.domainflag = 'erm'",
								new VectorProcessor());

				Vector vec2 = (Vector) NCLocator
						.getInstance()
						.lookup(IUAPQueryBS.class)
						.executeQuery(
								"select distinct(b.detail) from er_jkzb a,sm_msg_content b WHERE a.deptid = '"
										+ bmpk
										+ "' AND instr(b.detail, a.pk_jkbx) > 0 AND b.domainflag = 'erm'",
								new VectorProcessor());
				String[] sts = new String[vec.size() + vec2.size()];
				String[] bmpkss = new String[vec.size()];
				String[] bmpkss2 = new String[vec2.size()];
				if (vec != null && vec.size() > 0) {
					for (int i = 0; i < vec.size(); i++) {
						String str = new String();
						str = ((Vector) vec.get(i)).get(0).toString();
						bmpkss[i] = str;
					}
				}
				if (vec2 != null && vec2.size() > 0) {
					for (int i = 0; i < vec2.size(); i++) {
						String str = new String();
						str = ((Vector) vec2.get(i)).get(0).toString();
						bmpkss2[i] = str;
					}
				}
				// 将两个数组合并
				System.arraycopy(bmpkss, 0, sts, 0, bmpkss.length);// 将第二个数组与第一个数组合并
				System.arraycopy(bmpkss2, 0, sts, bmpkss.length, bmpkss2.length);// 将第二个数组与第一个数组合并
				if(sts != null && sts.length > 0) {
					info.setDetail(sts);// 部门参照
				} else {
					String[] ss = new String[1];
					ss[0] = "000000000000000";
					info.setDetail(ss);// 部门参照
				}
				
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		DefaultConstEnum dce = (DefaultConstEnum) getCbbtime()
				.getSelectedItem();
		info.setSenddate(dce == null ? null : dce.getValue().toString());
		DefaultConstEnum dce1 = (DefaultConstEnum) gettffrom()
				.getSelectedItem();
		info.setFrom(dce1 == null ? null : dce1.getValue().toString());
		return info;
	}

	private void fireQuery(MsgQueryInfo qryinfo) {
		msgmodel.queryMessageByCondition(qryinfo);
	}

	private void setFromComboData(String msgboxcode) {
		if (MessageBoxUtil.isClassifiedBox(msgboxcode)) {
			List<MsgTypeVO> typelist = MsgTypeUtils.getMsgTypeByBox(msgboxcode);
			setFromComboData(typelist.toArray(new MsgTypeVO[0]));
		} else {
			Map<String, MsgTypeVO> typemap = MsgTypeCache.getInstance()
					.getAllMsgTypes();
			setFromComboData(typemap.values().toArray(new MsgTypeVO[0]));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setFromComboData(MsgTypeVO[] vos) {
		if (vos != null && vos.length > 0) {
			fromdata = new DefaultConstEnum[vos.length + 1];
			fromdata[0] = new DefaultConstEnum("", "");

			for (int i = 0; i < vos.length; i++)
				fromdata[i + 1] = new DefaultConstEnum(vos[i].getTypecode(),
						vos[i].getDispName());
		} else {
			fromdata = new DefaultConstEnum[1];
			fromdata[0] = new DefaultConstEnum("", "");
		}

		DefaultComboBoxModel cbmodel = new DefaultComboBoxModel(fromdata);
		gettffrom().setModel(cbmodel);
	}

	@Override
	public void handleMsgEvent(MessageEvent me) {
		if (me.getEventcode().equals(MessageEvent.BOXCHANGE)) {
			Object object = me.getEventObject();
			if (object instanceof IMessageBox[]) {
				IMessageBox[] messagebox = (IMessageBox[]) object;

				if (messagebox[0] != null)
					messagebox[0].setMsgQueryInfo(getCurrQueryInfo());

				if (messagebox[1] != null) {
					setReadBoxVisibilityCurrentSelectedBox(messagebox[1]
							.getBoxCode());
					setFromComboData(messagebox[1].getBoxCode());
					MsgQueryInfo info = messagebox[1].getMsgQueryInfo();
					setQueryUIData(info);
				}
			}
		}
	}

	private void setReadBoxVisibilityCurrentSelectedBox(String boxcode) {
		if (boxcode.equals(MessageBoxUtil.SENDBOX)
				|| boxcode.equals(MessageBoxUtil.DELETEBOX))
			getReadBox().setEnabled(false);
		else
			getReadBox().setEnabled(true);
	}

	private void setQueryUIData(MsgQueryInfo info) {
		if (info != null) {
			processReadBox(info.isHandle());
			getQueryButton().setSelected(info.isShowQry());
			HyperAction haction = getActionByData(info.getQuikdate());
			haction.setSelected(true);
			gettfsubject().setText(info.getSubject());
			getUserRef().setPKs(info.getSender());
			String[] details = new String[1];
			if (info.getDetail() != null && info.getDetail().length > 0) {
				String detail = info.getDetail()[0];
				try {
					Vector vec = (Vector) NCLocator
							.getInstance()
							.lookup(IUAPQueryBS.class)
							.executeQuery(
									"select distinct(a.deptid) from er_bxzb a,sm_msg_content b WHERE instr('"
											+ detail + "', a.pk_jkbx) > 0",
									new VectorProcessor());
					if (vec != null && vec.size() > 0) {
						details[0] = ((Vector) vec.get(0)).get(0).toString();
						getBmRef().setPKs(details);// 部门主键
					}
					Vector vec2 = (Vector) NCLocator
							.getInstance()
							.lookup(IUAPQueryBS.class)
							.executeQuery(
									"select distinct(a.deptid) from er_jkzb a,sm_msg_content b WHERE instr('"
											+ detail + "', a.pk_jkbx) > 0",
									new VectorProcessor());
					if (vec2 != null && vec2.size() > 0) {
						details[0] = ((Vector) vec2.get(0)).get(0).toString();
						getBmRef().setPKs(details);// 部门主键
					}
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			}
			getBillTypeRef().setPKs(info.getBillType());// 单据类型主键
			gettffrom().setSelectedItem(getSelectDCE(fromdata, info.getFrom()));
			getCbbtime().setSelectedItem(
					getSelectDCE(combodata, info.getSenddate()));
		} else {
			processReadBox(false);
			getQueryButton().setSelected(false);
			getWeekHyper().setSelected(true);
			clearQuerySet();
		}
		doQueryPanelLayout();
	}

	private HyperAction getActionByData(String days) {
		if (days.equals("30"))
			return getOmonthHyper();

		if (days.equals("90"))
			return getTmonthHyper();

		return getWeekHyper();
	}

	private DefaultConstEnum getSelectDCE(DefaultConstEnum[] combodata,
			String value) {
		if (combodata != null && combodata.length > 0) {
			for (DefaultConstEnum dce : combodata) {
				if (dce.getValue().equals(value))
					return dce;
			}
		}
		return null;
	}

	private void processReadBox(boolean isreadshow) {
		getReadBox().setVisible(true);
		// update by lihaibo
		//getReadBox().setText(
		//		NCLangRes.getInstance().getStrByID("ncmessage",
		//				"msgboxres-000031")/* 包含已处理 */);
		getReadBox().setText("已处理消息");
		getReadBox().setSelected(isreadshow);
	}

}
