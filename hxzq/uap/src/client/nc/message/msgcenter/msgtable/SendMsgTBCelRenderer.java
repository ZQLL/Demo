package nc.message.msgcenter.msgtable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import nc.message.reconstruction.MessageCenterUIConst;
import nc.message.vo.NCMessage;
import nc.ui.pub.beans.UICheckBox;
import nc.uitheme.ui.ThemeResourceCenter;

public class SendMsgTBCelRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -8476692182647393520L;

	public final ImageIcon iconProHigh = ThemeResourceCenter.getInstance()
			.getImage("themeres/infocenter/important.png");

	public final ImageIcon iconAttach = ThemeResourceCenter.getInstance()
			.getImage("themeres/infocenter/attachment.png");

	/**
	 * update by lihaibo
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel lb = (JLabel) super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		if (column == 0) {// 是否选中列
			UICheckBox jbx = new UICheckBox();
			if (value != null && !value.equals("")
					&& ((Boolean) value).booleanValue())
				jbx.setSelected(true);
			else
				jbx.setSelected(false);
			jbx.setHorizontalAlignment(SwingConstants.CENTER);
			if (isSelected) {
				jbx.setBackground((Color) UIManager
						.get("Table.selectionBackground"));
			} else {
				jbx.setBackground((Color) UIManager.get("Table.background"));
			}
			return jbx;
		}
		if (column == 1) {// 优先级
			NCMessage msg = ((AbstractMsgTBModel) table.getModel())
					.getMessage(row);
			int rbg = 0;
			if (msg.getMessage().getSubcolor() != null
					&& msg.getMessage().getSubcolor().length() > 0) {
				rbg = Integer.valueOf(msg.getMessage().getSubcolor());
			}
			if (value != null && !value.equals("")) {
				lb.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				lb.setIcon(null);
				lb.setText(value.toString());
				if (rbg != 0) {
					lb.setForeground(new Color(rbg));
				} else {
					if (msg.getMessage().getIsread() != null
							&& msg.getMessage().getIsread().booleanValue()) {
						lb.setForeground(MessageCenterUIConst.MCWORDCOLOR);
					} else {
						lb.setForeground(MessageCenterUIConst.MCUNREADCOLOR);
					}
				}
				lb.setHorizontalAlignment(JLabel.LEFT);
			}
			return lb;
			// lb.setHorizontalAlignment(JLabel.CENTER);
			// if(value!=null && value.equals("9"))
			// lb.setIcon(iconProHigh);
			// else
			// lb.setIcon(null);
			// lb.setText("");
			// return lb;
			// }
			// if(column == 2){//附件
			// lb.setHorizontalAlignment(JLabel.CENTER);
			// if(value != null && !value.equals("") && ((Boolean)
			// value).booleanValue())
			// lb.setIcon(iconAttach);
			// else
			// lb.setIcon(null);
			// lb.setText("");
			// return lb;
			// }if(column == 3){//标题
			// NCMessage msg =
			// ((AbstractMsgTBModel)table.getModel()).getMessage(row);
			// int rbg = 0;
			// if(msg.getMessage().getSubcolor()!=null&&msg.getMessage().getSubcolor().length()>0){
			// rbg = Integer.valueOf(msg.getMessage().getSubcolor());
			// }
			// if(value != null && !value.equals("")){
			// lb.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
			// lb.setIcon(null);
			// lb.setText(value.toString());
			// if(rbg!=0){
			// lb.setForeground(new Color(rbg));
			// }else{
			// if(msg.getMessage().getIsread()!=null&&msg.getMessage().getIsread().booleanValue()){
			// lb.setForeground(MessageCenterUIConst.MCWORDCOLOR);
			// }else{
			// lb.setForeground(MessageCenterUIConst.MCUNREADCOLOR);
			// }
			// }
			// lb.setHorizontalAlignment(JLabel.LEFT);
			// }
			// return lb;
		} else {
			lb.setHorizontalAlignment(JLabel.CENTER);
			lb.setForeground(MessageCenterUIConst.MCWORDCOLOR);
			lb.setIcon(null);
			return lb;
		}

	}
}
