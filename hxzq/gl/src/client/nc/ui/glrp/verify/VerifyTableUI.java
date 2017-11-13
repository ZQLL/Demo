package nc.ui.glrp.verify;

/**
 *TableUI代理
 * 新增四个方法1.3版使用，1.2版不使用：
	paintCells(Graphics, int, int, int, int)
	paintDraggedArea(Graphics, int, int, TableColumn, int)
	paintGrid(Graphics, int, int, int, int)
	viewIndexForColumn(TableColumn)
 * 修改三个方法（判断版本号）：
	createTableSize(long)
	paint(Graphics, JComponent)
	paintCell(Graphics, Rectangle, int, int)
 * 保留四个方法1.2版使用，1.3版不使用：
	paintVerticalLines(Graphics)
	paintRow(Graphics, int)
	paintHorizontalLines(Graphics)
	paintGrid(Graphics)
 * @修改：张 扬
 *
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.CellEditor;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Keymap;

import nc.ui.pub.beans.UIRefPane;


public class VerifyTableUI extends TableUI {

	protected JTable table;
	protected CellRendererPane rendererPane;

	protected KeyListener keyListener;
	protected FocusListener focusListener;
	protected MouseInputListener mouseInputListener;

	private Hashtable<KeyStroke,ActionListener> registeredKeyStrokes = new Hashtable<KeyStroke,ActionListener>();


	private class NavigationalAction implements ActionListener {
		protected int dx;
		protected int dy;
		protected boolean toggle;

		protected boolean extend;
		protected boolean inSelection;

		protected int anchorRow;
		protected int anchorColumn;
		protected int leadRow;
		protected int leadColumn;

		protected NavigationalAction(int dx, int dy, boolean toggle, boolean extend, boolean inSelection) {
			this.dx = dx;
			this.dy = dy;
			this.toggle = toggle;
			this.extend = extend;
			this.inSelection = inSelection;
		}

		private int clipToRange(int i, int a, int b) {
			return Math.min(Math.max(i, a), b - 1);
		}

		private void moveWithinTableRange(JTable table, int dx, int dy, boolean changeLead) {
			breaker();
			if (changeLead) {
				leadRow = clipToRange(leadRow + dy, 0, table.getRowCount());
				leadColumn = clipToRange(leadColumn + dx, 0, table.getColumnCount());
			} else {
				anchorRow = clipToRange(anchorRow + dy, 0, table.getRowCount());
				anchorColumn = clipToRange(anchorColumn + dx, 0, table.getColumnCount());
			}
		}

		private int selectionSpan(ListSelectionModel sm) {
			return sm.getMaxSelectionIndex() - sm.getMinSelectionIndex() + 1;
		}

		private int compare(int i, ListSelectionModel sm) {
			return compare(i, sm.getMinSelectionIndex(), sm.getMaxSelectionIndex() + 1);
		}

		private int compare(int i, int a, int b) {
			return (i < a) ? -1 : (i >= b) ? 1 : 0;
		}

		private boolean moveWithinSelectedRange(JTable table, int dx, int dy, boolean ignoreCarry) {
			ListSelectionModel rsm = table.getSelectionModel();
			ListSelectionModel csm = table.getColumnModel().getSelectionModel();

			int newAnchorRow = anchorRow + dy;
			int newAnchorColumn = anchorColumn + dx;

			int rowSgn;
			int colSgn;
			int rowCount = selectionSpan(rsm);
			int columnCount = selectionSpan(csm);

			boolean canStayInSelection = (rowCount * columnCount > 1);
			if (canStayInSelection) {
				rowSgn = compare(newAnchorRow, rsm);
				colSgn = compare(newAnchorColumn, csm);
			} else {
				// If there is only one selected cell, there is no point 
				// in trying to stay within the selected area. Move outside 
				// the selection, wrapping at the table boundaries. 
				rowCount = table.getRowCount();
				columnCount = table.getColumnCount();
				rowSgn = compare(newAnchorRow, 0, rowCount);
				colSgn = compare(newAnchorColumn, 0, columnCount);

			}

			anchorRow = newAnchorRow - rowCount * rowSgn;
			anchorColumn = newAnchorColumn - columnCount * colSgn;

			if (!ignoreCarry) {
				return moveWithinSelectedRange(table, rowSgn, colSgn, true);
			}
			return canStayInSelection;
		}

		public void actionPerformed(ActionEvent e) {
			breaker();
			JTable table = (JTable) e.getSource();
			ListSelectionModel rsm = table.getSelectionModel();
			anchorRow = rsm.getAnchorSelectionIndex();
			leadRow = rsm.getLeadSelectionIndex();

			ListSelectionModel csm = table.getColumnModel().getSelectionModel();
			anchorColumn = csm.getAnchorSelectionIndex();
			leadColumn = csm.getLeadSelectionIndex();

			int oldAnchorRow = anchorRow;
			int oldAnchorColumn = anchorColumn;

			if (!inSelection) {
				moveWithinTableRange(table, dx, dy, extend);
				if (!extend) {
					updateSelection(table, anchorRow, anchorColumn, false, extend);
				} else {
					updateSelection(table, leadRow, leadColumn, false, extend);
				}
			} else {
				if (moveWithinSelectedRange(table, dx, dy, false)) {
					rsm.setAnchorSelectionIndex(anchorRow);
					csm.setAnchorSelectionIndex(anchorColumn);
				} else {
					updateSelection(table, anchorRow, anchorColumn, false, false);
				}
			}

			if (table.isEditing()
				&& (oldAnchorRow != rsm.getAnchorSelectionIndex()
					|| oldAnchorColumn != csm.getAnchorSelectionIndex())) {
				table.getCellEditor().stopCellEditing();
			}
		}
	}

	private class PagingAction extends NavigationalAction {

		private boolean forwards;
		private boolean vertically;
		private boolean toLimit;

		private PagingAction(boolean extend, boolean forwards, boolean vertically, boolean toLimit) {
			super(0, 0, false, extend, false);
			this.forwards = forwards;
			this.vertically = vertically;
			this.toLimit = toLimit;
		}

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			if (toLimit) {
				if (vertically) {
					int rowCount = table.getRowCount();
					this.dx = 0;
					this.dy = forwards ? rowCount : -rowCount;
				} else {
					int colCount = table.getColumnCount();
					this.dx = forwards ? colCount : -colCount;
					this.dy = 0;
				}
			} else {
				if (!(table.getParent().getParent() instanceof JScrollPane)) {
					return;
				}

				Dimension delta = table.getParent().getSize();
				ListSelectionModel sm =
					(vertically) ? table.getSelectionModel() : table.getColumnModel().getSelectionModel();

				int start = (extend) ? sm.getLeadSelectionIndex() : sm.getAnchorSelectionIndex();

				if (vertically) {
					Rectangle r = table.getCellRect(start, 0, true);
					r.y += forwards ? delta.height : -delta.height;
					this.dx = 0;
					int newRow = table.rowAtPoint(r.getLocation());
					if (newRow == -1 && forwards) {
						newRow = table.getRowCount();
					}
					this.dy = newRow - start;
				} else {
					Rectangle r = table.getCellRect(0, start, true);
					r.x += forwards ? delta.width : -delta.width;
					int newColumn = table.columnAtPoint(r.getLocation());
					if (newColumn == -1 && forwards) {
						newColumn = table.getColumnCount();
					}
					this.dx = newColumn - start;
					this.dy = 0;
				}
			}
			super.actionPerformed(e);
		}
	}

	//
	//  The Table's Key listener
	//

	/**
	 * This inner class is marked &quot;public&quot; due to a compiler bug.
	 * This class should be treated as a &quot;protected&quot; inner class.
	 * Instantiate it only within subclasses of BasicTableUI.
	 */
	public class KeyHandler implements KeyListener {
		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
			KeyStroke keyStroke; 

			if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) { 
				keyStroke = KeyStroke.getKeyStroke(e.getKeyChar(), e.getModifiers());
			}else { 
				keyStroke = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers());
			} 

			if (registeredKeyStrokes.get(keyStroke) != null) {
				return;
			}

			if (e.getKeyChar() == '\r') {
				return;
			}

			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				return;
			}

			if (table.getRowCount() == 0){
				return;
			}

			int anchorRow = table.getSelectionModel().getAnchorSelectionIndex();
			int anchorColumn = table.getColumnModel().getSelectionModel().getAnchorSelectionIndex();
			if (anchorRow != -1 && anchorColumn != -1 && !table.isEditing()) {
				if (!table.editCellAt(anchorRow, anchorColumn)) {
					return;
				}
			}

			Component editorComp = table.getEditorComponent();
			if (table.isEditing() && editorComp != null) {
				//获得焦点
				if (e.getKeyChar() != KeyEvent.VK_ENTER && e.getKeyChar() != KeyEvent.VK_TAB) {
					if (editorComp instanceof UIRefPane)
						 ((UIRefPane) editorComp).getUITextField().requestFocus();
					else
						editorComp.requestFocus();
				} else {
					if (table.isEditing() && editorComp != null) {
						table.getCellEditor().stopCellEditing();
					}
				}

				if (editorComp instanceof JTextField) {
					JTextField textField = (JTextField) editorComp;
					Keymap keyMap = textField.getKeymap();
					Action action = keyMap.getAction(keyStroke);
					if (action == null) {
						action = keyMap.getDefaultAction();
					}
					if (action != null) {
						ActionEvent ae = new ActionEvent(textField, ActionEvent.ACTION_PERFORMED, String.valueOf(e.getKeyChar()));
						action.actionPerformed(ae);
						e.consume();
					}
				}
				if (editorComp instanceof UIRefPane) {
					JTextField textField = ((UIRefPane) editorComp).getUITextField();
					Keymap keyMap = textField.getKeymap();
					Action action = keyMap.getAction(keyStroke);
					if (action == null) {
						action = keyMap.getDefaultAction();
					}
					if (action != null) {
						ActionEvent ae =
							new ActionEvent(textField, ActionEvent.ACTION_PERFORMED, String.valueOf(e.getKeyChar()));
						action.actionPerformed(ae);
						e.consume();
					}
				}
			}
		}
	}

	public class FocusHandler implements FocusListener {

		private void repaintAnchorCell() {
			int anchorRow = table.getSelectionModel().getAnchorSelectionIndex();
			int anchorColumn = table.getColumnModel().getSelectionModel().getAnchorSelectionIndex();
			Rectangle dirtyRect = table.getCellRect(anchorRow, anchorColumn, false);
			table.repaint(dirtyRect);
		}

		public void focusGained(FocusEvent e) {
			repaintAnchorCell();
		}

		public void focusLost(FocusEvent e) {
			repaintAnchorCell();
		}
	}

	//
	//  The Table's mouse and mouse motion listeners
	//

	/**
	 * This inner class is marked &quot;public&quot; due to a compiler bug.
	 * This class should be treated as a &quot;protected&quot; inner class.
	 * Instantiate it only within subclasses of BasicTableUI.
	 */
	public class MouseInputHandler implements MouseInputListener {

		// Component recieving mouse events during editing. May not be editorComponent.
		private Component dispatchComponent;

		//  The Table's mouse listener methods.

		public void mouseClicked(MouseEvent e) {
		}

		private void setDispatchComponent(MouseEvent e) {
			Component editorComponent = table.getEditorComponent();
			Point p = e.getPoint();
			Point p2 = SwingUtilities.convertPoint(table, p, editorComponent);
			dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, p2.x, p2.y);
		}

		private boolean repostEvent(MouseEvent e) {
			if (dispatchComponent == null) {
				return false;
			}
			MouseEvent e2 = SwingUtilities.convertMouseEvent(table, e, dispatchComponent);
			dispatchComponent.dispatchEvent(e2);
			return true;
		}

		private void setValueIsAdjusting(boolean flag) {
			table.getSelectionModel().setValueIsAdjusting(flag);
			table.getColumnModel().getSelectionModel().setValueIsAdjusting(flag);
		}

		public void mousePressed(MouseEvent e) {
			if (!SwingUtilities.isLeftMouseButton(e)) {
				return;
			}

			Point p = e.getPoint();
			int row = table.rowAtPoint(p);
			int column = table.columnAtPoint(p);
			// The autoscroller can generate drag events outside the Table's range. 
			if ((column == -1) || (row == -1)) {
				return;
			}

			if (table.editCellAt(row, column, e)) {
				setDispatchComponent(e);
				repostEvent(e);
			} else {
				table.requestFocus();
			}

			CellEditor editor = table.getCellEditor();
			if (editor == null || editor.shouldSelectCell(e)) {
				setValueIsAdjusting(true);
				updateSelection(table, row, column, e.isControlDown(), e.isShiftDown());
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (!SwingUtilities.isLeftMouseButton(e)) {
				return;
			}

			repostEvent(e);
			dispatchComponent = null;
			setValueIsAdjusting(false);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		//  The Table's mouse motion listener methods.

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			if (!SwingUtilities.isLeftMouseButton(e)) {
				return;
			}

			repostEvent(e);

			CellEditor editor = table.getCellEditor();
			if (editor == null || editor.shouldSelectCell(e)) {
				Point p = e.getPoint();
				int row = table.rowAtPoint(p);
				int column = table.columnAtPoint(p);
				// The autoscroller can generate drag events outside the Table's range. 
				if ((column == -1) || (row == -1)) {
					return;
				}
				updateSelection(table, row, column, false, true);
			}
		}
	}
	/**
 * 此处插入方法说明。
 * 创建日期：(2001-11-9 15:57:36)
 */
public static void breaker() {
	nc.bs.logging.Logger.debug("");
}
	/**
	 * Creates the focus listener for handling keyboard navigation in the JTable.
	 */
	protected FocusListener createFocusListener() {
		return new FocusHandler();
	}
//
//  Factory methods for the Listeners
//

	/**
	 * Creates the key listener for handling keyboard navigation in the JTable.
	 */
	protected KeyListener createKeyListener() {
		return new KeyHandler();
	}
	/**
	 * Creates the mouse listener for the JTable.
	 */
	protected MouseInputListener createMouseInputListener() {
		return new MouseInputHandler();
	}
//
// Size Methods
//

private Dimension createTableSize(long width) {
	if (System.getProperty("java.vm.version").startsWith("1.2")) {
		int height = table.getRowCount() * (table.getRowHeight() + table.getRowMargin());
		int totalMarginWidth = table.getColumnModel().getColumnMargin() * table.getColumnCount();
		// Width is always positive. The call to abs() is a workaround for 
		// a bug in the 1.1.6 JIT on Windows. 
		long widthWithMargin = Math.abs(width) + totalMarginWidth;
		if (widthWithMargin > Integer.MAX_VALUE) {
			widthWithMargin = Integer.MAX_VALUE;
		}
		return new Dimension((int) widthWithMargin, height);
	} else {

		int height = 0;
		int rowCount = table.getRowCount();
		if (rowCount > 0 && table.getColumnCount() > 0) {
			Rectangle r = table.getCellRect(rowCount - 1, 0, true);
			height = r.y + r.height;
		}
		// Width is always positive. The call to abs() is a workaround for 
		// a bug in the 1.1.6 JIT on Windows. 
		long tmp = Math.abs(width);
		if (tmp > Integer.MAX_VALUE) {
			tmp = Integer.MAX_VALUE;
		}
		return new Dimension((int) tmp, height);
	}
}


	public static ComponentUI createUI(JComponent c) {
		return new VerifyTableUI();
	}

	public Dimension getMaximumSize(JComponent c) {
		long width = 0;
		Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn aColumn = (TableColumn)enumeration.nextElement();
			width = width + aColumn.getMaxWidth();
		}
		return createTableSize(width);
	}

	public Dimension getMinimumSize(JComponent c) {
		long width = 0;
		Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn aColumn = (TableColumn)enumeration.nextElement();
			width = width + aColumn.getMinWidth();
		}
		return createTableSize(width);
	}

	public Dimension getPreferredSize(JComponent c) {
		long width = 0;
		Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn aColumn = (TableColumn)enumeration.nextElement();
			width = width + aColumn.getPreferredWidth();
		}
		return createTableSize(width);
	}

	protected void installDefaults() {
		LookAndFeel.installColorsAndFont(table, "Table.background","Table.foreground", "Table.font");

		Color sbg = table.getSelectionBackground();
		if (sbg == null || sbg instanceof UIResource) {
			table.setSelectionBackground(UIManager.getColor("Table.selectionBackground"));
		}

		Color sfg = table.getSelectionForeground();
		if (sfg == null || sfg instanceof UIResource) {
			table.setSelectionForeground(UIManager.getColor("Table.selectionForeground"));
		}

		Color gridColor = table.getGridColor();
		if (gridColor == null || gridColor instanceof UIResource) {
			table.setGridColor(UIManager.getColor("Table.gridColor"));
		}

		Container parent = table.getParent(); 
		if (parent != null) {
			parent = parent.getParent();  
			if (parent != null && parent instanceof JScrollPane) {
				LookAndFeel.installBorder((JScrollPane)parent, "Table.scrollPaneBorder");
			}
		}
	}
/**
 * Register all keyboard actions on the JTable.
 */
protected void installKeyboardActions() {
	int shift = ActionEvent.SHIFT_MASK;
	int ctrl = ActionEvent.CTRL_MASK;
	int cShft = shift | ctrl;

	registerKey(KeyEvent.VK_RIGHT, 0, 1, 0);
	registerKey(KeyEvent.VK_LEFT, 0, -1, 0);
	registerKey(KeyEvent.VK_DOWN, 0, 0, 1);
	registerKey(KeyEvent.VK_UP, 0, 0, -1);

	registerKey("KP_RIGHT", 1, 0);
	registerKey("KP_LEFT", -1, 0);
	registerKey("KP_DOWN", 0, 1);
	registerKey("KP_UP", 0, -1);

	registerKey(KeyEvent.VK_RIGHT, shift, 1, 0);
	registerKey(KeyEvent.VK_LEFT, shift, -1, 0);
	registerKey(KeyEvent.VK_DOWN, shift, 0, 1);
	registerKey(KeyEvent.VK_UP, shift, 0, -1);

	registerKey("shift KP_RIGHT", 1, 0);
	registerKey("shift KP_LEFT", -1, 0);
	registerKey("shift KP_DOWN", 0, 1);
	registerKey("shift KP_UP", 0, -1);

	registerScrollKey(KeyEvent.VK_PAGE_UP, 0, false, true, false);
	registerScrollKey(KeyEvent.VK_PAGE_DOWN, 0, true, true, false);
	registerScrollKey(KeyEvent.VK_HOME, 0, false, false, true);
	registerScrollKey(KeyEvent.VK_END, 0, true, false, true);

	registerScrollKey(KeyEvent.VK_PAGE_UP, shift, false, true, false);
	registerScrollKey(KeyEvent.VK_PAGE_DOWN, shift, true, true, false);
	registerScrollKey(KeyEvent.VK_HOME, shift, false, false, true);
	registerScrollKey(KeyEvent.VK_END, shift, true, false, true);

	registerScrollKey(KeyEvent.VK_PAGE_UP, ctrl, false, false, false);
	registerScrollKey(KeyEvent.VK_PAGE_DOWN, ctrl, true, false, false);
	registerScrollKey(KeyEvent.VK_HOME, ctrl, false, true, true);
	registerScrollKey(KeyEvent.VK_END, ctrl, true, true, true);

	registerScrollKey(KeyEvent.VK_PAGE_UP, cShft, false, false, false);
	registerScrollKey(KeyEvent.VK_PAGE_DOWN, cShft, true, false, false);
	registerScrollKey(KeyEvent.VK_HOME, cShft, false, true, true);
	registerScrollKey(KeyEvent.VK_END, cShft, true, true, true);

	registerKey(KeyEvent.VK_TAB, 0, 1, 0, true, false, true);
	registerKey(KeyEvent.VK_TAB, shift, -1, 0, true, false, true);
	//registerKey(KeyEvent.VK_ENTER,     0,  0,  1, true, false, true);
	//registerKey(KeyEvent.VK_ENTER, shift,  0, -1, true, false, true); 
	//修改
	registerKey(KeyEvent.VK_ENTER, 0, 1, 0, true, false, true);
	registerKey(KeyEvent.VK_ENTER, shift, -1, 0, true, false, true);

	registerKeyboardAction(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			table.selectAll();
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_A, ctrl));

	registerKeyboardAction(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			table.removeEditor();
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));

	registerKeyboardAction(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			breaker();
			ListSelectionModel rsm = table.getSelectionModel();
			int anchorRow = rsm.getAnchorSelectionIndex();
			ListSelectionModel csm = table.getColumnModel().getSelectionModel();
			int anchorColumn = csm.getAnchorSelectionIndex();
			table.editCellAt(anchorRow, anchorColumn);
			Component other = table.hasFocus() ? table.getEditorComponent() : table;
			other.requestFocus();
			//if(other instanceof nc.ui.pub.beans.UIRefPane){
				//((nc.ui.pub.beans.UIRefPane)other).onButtonClicked();
			//}
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));

}
	/**
	 * Attaches listeners to the JTable.
	 */
	protected void installListeners() {
		focusListener = createFocusListener();
		keyListener = createKeyListener();
		mouseInputListener = createMouseInputListener();

		table.addFocusListener(focusListener);
		table.addKeyListener(keyListener);
		table.addMouseListener(mouseInputListener);
		table.addMouseMotionListener(mouseInputListener);
	}
//  Installation

	public void installUI(JComponent c) {
		table = (JTable)c;

		rendererPane = new CellRendererPane();
		table.add(rendererPane);

		installDefaults();
		installListeners();
		installKeyboardActions();
	}
private int lastVisibleRow(Rectangle clip) {
	int lastIndex = table.rowAtPoint(new Point(0, clip.y + clip.height - 1));
	// If the table does not have enough rows to fill the view we'll get -1.
	// Replace this with the index of the last row.
	if (lastIndex == -1) {
		lastIndex = table.getRowCount() - 1;
	}
	return lastIndex;
}
//
//  Paint methods and support
//

/** Paint a representation of the <code>table</code> instance
 * that was set in installUI().
 */
public void paint(Graphics g, JComponent c) {
	if (System.getProperty("java.vm.version").startsWith("1.2")) {
		Rectangle oldClipBounds = g.getClipBounds();
		Rectangle clipBounds = new Rectangle(oldClipBounds);
		int tableWidth = table.getColumnModel().getTotalColumnWidth();
		clipBounds.width = Math.min(clipBounds.width, tableWidth);
		g.setClip(clipBounds);

		// Paint the grid
		paintGrid(g);

		// Paint the rows
		int firstIndex = table.rowAtPoint(new Point(0, clipBounds.y));
		int lastIndex = lastVisibleRow(clipBounds);

		Rectangle rowRect = new Rectangle(0, 0, tableWidth, table.getRowHeight() + table.getRowMargin());
		rowRect.y = firstIndex * rowRect.height;

		for (int index = firstIndex; index <= lastIndex; index++) {
			// Paint any rows that need to be painted
			if (rowRect.intersects(clipBounds)) {
				paintRow(g, index);
			}
			rowRect.y += rowRect.height;
		}
		g.setClip(oldClipBounds);
	} else {
		if (table.getRowCount() <= 0 || table.getColumnCount() <= 0) {
			return;
		}
		Rectangle clip = g.getClipBounds();
		Point minLocation = clip.getLocation();
		Point maxLocation = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
		int rMin = table.rowAtPoint(minLocation);
		int rMax = table.rowAtPoint(maxLocation);
		// This should never happen.
		if (rMin == -1) {
			rMin = 0;
		}
		// If the table does not have enough rows to fill the view we'll get -1.
		// Replace this with the index of the last row.
		if (rMax == -1) {
			rMax = table.getRowCount() - 1;
		}
		int cMin = table.columnAtPoint(minLocation);
		int cMax = table.columnAtPoint(maxLocation);
		// This should never happen.
		if (cMin == -1) {
			cMin = 0;
		}
		// If the table does not have enough columns to fill the view we'll get -1.
		// Replace this with the index of the last column.
		if (cMax == -1) {
			cMax = table.getColumnCount() - 1;
		}

		// Paint the grid.
		paintGrid(g, rMin, rMax, cMin, cMax);

		// Paint the cells. 
		paintCells(g, rMin, rMax, cMin, cMax);
	}
}
protected void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
	if (System.getProperty("java.vm.version").startsWith("1.2")) {
		// The cellRect is inset by half the intercellSpacing before painted
		int spacingHeight = table.getRowMargin();
		int spacingWidth = table.getColumnModel().getColumnMargin();

		// Round so that when the spacing is 1 the cell does not paint obscure lines.
		cellRect.setBounds(
			cellRect.x + spacingWidth / 2,
			cellRect.y + spacingHeight / 2,
			cellRect.width - spacingWidth,
			cellRect.height - spacingHeight);

		if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
			Component component = table.getEditorComponent();
			component.setBounds(cellRect);
			component.validate();
		} else {
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component component = table.prepareRenderer(renderer, row, column);

			if (component.getParent() == null) {
				rendererPane.add(component);
			}
			rendererPane.paintComponent(
				g,
				component,
				table,
				cellRect.x,
				cellRect.y,
				cellRect.width,
				cellRect.height,
				true);
		}
		// Have to restore the cellRect back to it's orginial size
		cellRect.setBounds(
			cellRect.x - spacingWidth / 2,
			cellRect.y - spacingHeight / 2,
			cellRect.width + spacingWidth,
			cellRect.height + spacingHeight);
	} else {
		if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
			Component component = table.getEditorComponent();
			component.setBounds(cellRect);
			component.validate();
		} else {
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component component = table.prepareRenderer(renderer, row, column);
			rendererPane.paintComponent(
				g,
				component,
				table,
				cellRect.x,
				cellRect.y,
				cellRect.width,
				cellRect.height,
				true);
		}
	}
}
	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
		JTableHeader header = table.getTableHeader();
		TableColumn draggedColumn = (header == null) ? null : header.getDraggedColumn();

		TableColumnModel cm = table.getColumnModel();
		int columnMargin = cm.getColumnMargin();

		for (int row = rMin; row <= rMax; row++) {
			Rectangle cellRect = table.getCellRect(row, cMin, false);
			for (int column = cMin; column <= cMax; column++) {
				TableColumn aColumn = cm.getColumn(column);
				int columnWidth = aColumn.getWidth();
				cellRect.width = columnWidth - columnMargin;
				if (aColumn != draggedColumn) {
					paintCell(g, cellRect, row, column);
				}
				cellRect.x += columnWidth;
			}
		}

		// Paint the dragged column if we are dragging. 
		if (draggedColumn != null) {
			paintDraggedArea(g, rMin, rMax, draggedColumn, header.getDraggedDistance());
		}

		// Remove any renderers that may be left in the rendererPane. 
		rendererPane.removeAll();
	}
	private void paintDraggedArea(Graphics g, int rMin, int rMax, TableColumn draggedColumn, int distance) {
		int draggedColumnIndex = viewIndexForColumn(draggedColumn); 
		
		Rectangle minCell = table.getCellRect(rMin, draggedColumnIndex, true); 
	Rectangle maxCell = table.getCellRect(rMax, draggedColumnIndex, true); 
	    
	Rectangle vacatedColumnRect = minCell.union(maxCell); 

	// Paint a gray well in place of the moving column. 
	g.setColor(table.getParent().getBackground());
	g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, 
		   vacatedColumnRect.width, vacatedColumnRect.height);	    

	// Move to the where the cell has been dragged. 
	vacatedColumnRect.x += distance;

	// Fill the background. 
	g.setColor(table.getBackground());
	g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y,
		   vacatedColumnRect.width, vacatedColumnRect.height);
 
	// Paint the vertical grid lines if necessary.
	if (table.getShowVerticalLines()) {
	    g.setColor(table.getGridColor());
	    int x1 = vacatedColumnRect.x;
	    int y1 = vacatedColumnRect.y;
	    int x2 = x1 + vacatedColumnRect.width - 1;
	    int y2 = y1 + vacatedColumnRect.height - 1;
	    // Left
	    g.drawLine(x1-1, y1, x1-1, y2);
	    // Right
	    g.drawLine(x2, y1, x2, y2);
	}

	for(int row = rMin; row <= rMax; row++) { 
	    // Render the cell value
	    Rectangle r = table.getCellRect(row, draggedColumnIndex, false); 
	    r.x += distance;
	    paintCell(g, r, row, draggedColumnIndex);
 
	    // Paint the (lower) horizontal grid line if necessary.
	    if (table.getShowHorizontalLines()) {
		g.setColor(table.getGridColor());
		Rectangle rcr = table.getCellRect(row, draggedColumnIndex, true); 
		rcr.x += distance;
		int x1 = rcr.x;
		int y1 = rcr.y;
		int x2 = x1 + rcr.width - 1;
		int y2 = y1 + rcr.height - 1;
		g.drawLine(x1, y2, x2, y2);
	    }
	}
	}
	/*
	 * Paints the grid lines within <I>aRect</I>, using the grid
	 * color set with <I>setGridColor</I>. Paints vertical lines
	 * if <code>getShowVerticalLines()</code> returns true and paints
	 * horizontal lines if <code>getShowHorizontalLines()</code>
	 * returns true.
	 */
	private void paintGrid(Graphics g) {
		g.setColor(table.getGridColor());

		if (table.getShowHorizontalLines()) {
			paintHorizontalLines(g);
		}
		if (table.getShowVerticalLines()) {
			paintVerticalLines(g);
		}
	}
private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
	g.setColor(table.getGridColor());

	Rectangle minCell = table.getCellRect(rMin, cMin, true);
	Rectangle maxCell = table.getCellRect(rMax, cMax, true);

	if (table.getShowHorizontalLines()) {
		int tableWidth = maxCell.x + maxCell.width;
		int y = minCell.y;
		for (int row = rMin; row <= rMax; row++) {

			y += table.getRowHeight();
			g.drawLine(0, y - 1, tableWidth - 1, y - 1);
		}
	}
	if (table.getShowVerticalLines()) {
		TableColumnModel cm = table.getColumnModel();
		int tableHeight = maxCell.y + maxCell.height;
		int x = minCell.x;
		for (int column = cMin; column <= cMax; column++) {
			x += cm.getColumn(column).getWidth();
			g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
		}
	}
}
/*
 * This method paints horizontal lines regardless of whether the
 * table is set to paint them automatically.
*/
private void paintHorizontalLines(Graphics g) {
	Rectangle rect = g.getClipBounds();

	int firstIndex = table.rowAtPoint(new Point(0, rect.y));
	int lastIndex = lastVisibleRow(rect);

	int tableWidth = table.getColumnModel().getTotalColumnWidth();

	int delta = table.getRowHeight() + table.getRowMargin();
	int y = delta * firstIndex;

	for (int index = firstIndex; index <= lastIndex; index++) {
		y += delta;
		g.drawLine(0, y - 1, tableWidth - 1, y - 1);
	}
}
	private void paintRow(Graphics g, int row) {
		Rectangle rect = g.getClipBounds();
		int column = 0;
		boolean drawn = false;
		int draggedColumnIndex = -1;
		Rectangle draggedCellRect = null;
		Dimension spacing = table.getIntercellSpacing();
		JTableHeader header = table.getTableHeader();

		// Set up the cellRect
		Rectangle cellRect = new Rectangle();
		cellRect.height = table.getRowHeight() + spacing.height;
		cellRect.y = row * cellRect.height;

		Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns();

		// Paint the non-dragged table cells first
		while (enumeration.hasMoreElements()) {
			TableColumn aColumn = (TableColumn)enumeration.nextElement();

			cellRect.width = aColumn.getWidth() + spacing.width;
			if (cellRect.intersects(rect)) {
				drawn = true;
				if ((header == null) || (aColumn != header.getDraggedColumn())) {
					paintCell(g, cellRect, row, column);
				}
				else {
					// Paint a gray well in place of the moving column
					// This would be unnecessary if we drew the grid more cleverly
					g.setColor(table.getParent().getBackground());
					g.fillRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
					draggedCellRect = new Rectangle(cellRect);
					draggedColumnIndex = column;
				}
			}
			else {
				if (drawn)
					// Don't need to iterate through the rest
					break;
			}

			cellRect.x += cellRect.width;
			column++;
		}

		// paint the dragged cell if we are dragging
		if (draggedColumnIndex != -1 && draggedCellRect != null) {
			draggedCellRect.x += header.getDraggedDistance();

			// Fill the background
			g.setColor(table.getBackground());
			g.fillRect(draggedCellRect.x, draggedCellRect.y,
					   draggedCellRect.width, draggedCellRect.height);

			// paint grid if necessary.
			g.setColor(table.getGridColor());
			int x1 = draggedCellRect.x;
			int y1 = draggedCellRect.y;
			int x2 = x1 + draggedCellRect.width - 1;
			int y2 = y1 + draggedCellRect.height - 1;
			if (table.getShowVerticalLines()) {
			// Left
				// g.drawLine(x1-1, y1, x1-1, y2);
			// Right
				g.drawLine(x2, y1, x2, y2);
			}
			// Bottom
			if (table.getShowHorizontalLines()) {
				g.drawLine(x1, y2, x2, y2);
			}

			// Render the cell value
			paintCell(g, draggedCellRect, row, draggedColumnIndex);
		}
	}
	/*
	 * This method paints vertical lines regardless of whether the
	 * table is set to paint them automatically.
	 */
	private void paintVerticalLines(Graphics g) {
		int x = 0;
		int count = table.getColumnCount();
	TableColumnModel cm = table.getColumnModel(); 
		int columnMargin = cm.getColumnMargin(); 
		int rowHeight = table.getRowHeight() + table.getRowMargin(); 
	int tableHeight = rowHeight * table.getRowCount(); 

		for (int index = 0; index < count; index ++) {
	    x += cm.getColumn(index).getWidth() + columnMargin; 
			g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
		}
	}
	private void registerKey(int keyEvent, int mask, int dx, int dy) { 
	boolean toggle = (mask & ActionEvent.CTRL_MASK) != 0;
	boolean extend = (mask & ActionEvent.SHIFT_MASK) != 0; 
		registerKey(keyEvent, mask, dx, dy, toggle, extend, false);
	}
	private void registerKey(int keyEvent, int mask, int dx, int dy, 
			     boolean toggle, boolean extend, boolean inSelection) { 
	registerKeyboardAction(
		 new NavigationalAction(dx, dy, toggle, extend, inSelection),
		 KeyStroke.getKeyStroke(keyEvent, mask));
	}
	/* This version should only be used for events which are new
	 * in JDK 1.2 like "shift KP_UP".
	 */
	private void registerKey(String keyEvent, int dx, int dy) { 
	// This is a less than perfect way of doing things:
	KeyStroke ks = 	KeyStroke.getKeyStroke(keyEvent);
	int mask = ks.getModifiers();
	boolean toggle = (mask & ActionEvent.CTRL_MASK) != 0;
	boolean extend = (mask & ActionEvent.SHIFT_MASK) != 0; 
	
		registerKey(keyEvent, dx, dy, toggle, extend, false);
	}
	/* This version should only be used for events which are new
	 * in JDK 1.2 like "shift KP_UP".
	 */
	private void registerKey(String keyEvent, int dx, int dy, 
			     boolean toggle, boolean extend, boolean inSelection) { 
		registerKeyboardAction(
		 new NavigationalAction(dx, dy, toggle, extend, inSelection),
		 KeyStroke.getKeyStroke(keyEvent));
	}
	private void registerKeyboardAction(ActionListener action, KeyStroke keyStroke) { 
		registeredKeyStrokes.put(keyStroke, action); 
		table.registerKeyboardAction(action, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT); 
	}
	private void registerScrollKey(int keyEvent, int mask, boolean forwards, 
								   boolean vertically, boolean toLimit) { 
		boolean extend = (mask & ActionEvent.SHIFT_MASK) != 0; 
		registerKeyboardAction(
				 new PagingAction(extend, forwards, vertically, toLimit),
				 KeyStroke.getKeyStroke(keyEvent, mask));
	}
	protected void uninstallDefaults() {}
	protected void uninstallKeyboardActions() {
		Enumeration<KeyStroke> keyStrokes = registeredKeyStrokes.keys(); 
		while (keyStrokes.hasMoreElements()) { 
		    KeyStroke keyStroke = (KeyStroke)keyStrokes.nextElement(); 
		    table.unregisterKeyboardAction(keyStroke);
		}
	
	}
	protected void uninstallListeners() {
		table.removeFocusListener(focusListener);
		table.removeKeyListener(keyListener);
		table.removeMouseListener(mouseInputListener);
		table.removeMouseMotionListener(mouseInputListener);

		focusListener = null;
		keyListener = null;
		mouseInputListener = null;
	}
//  Uninstallation

	public void uninstallUI(JComponent c) {
		uninstallDefaults();
		uninstallListeners();
		uninstallKeyboardActions();

		table.remove(rendererPane);
		rendererPane = null;
		table = null;
	}
	private static void updateSelection(JTable table, int rowIndex, int columnIndex,
								   boolean toggle, boolean extend) {
		// Autoscrolling support.
		Rectangle cellRect = table.getCellRect(rowIndex, columnIndex, false);
		if (cellRect != null) {
			table.scrollRectToVisible(cellRect);
		}

		ListSelectionModel rsm = table.getSelectionModel();
		ListSelectionModel csm = table.getColumnModel().getSelectionModel();

		// Update column selection model
		updateSelectionModel(csm, columnIndex, toggle, extend);

		// Update row selection model
		updateSelectionModel(rsm, rowIndex, toggle, extend);
	}
	private static void updateSelectionModel(ListSelectionModel sm, int index,
								 boolean toggle, boolean extend) {
		if (!extend) {
			if (!toggle) {
				sm.setSelectionInterval(index, index);
			}
			else {
				if (sm.isSelectedIndex(index)) {
					sm.removeSelectionInterval(index, index);
				}
				else {
					sm.addSelectionInterval(index, index);
				}
			}
		}
		else {
			sm.setLeadSelectionIndex(index);
		}
	}
	private int viewIndexForColumn(TableColumn aColumn) {
		TableColumnModel cm = table.getColumnModel();
		for (int column = 0; column < cm.getColumnCount(); column++) {
			if (cm.getColumn(column) == aColumn) {
				return column;
			}
		}
		return -1;
	}
} // End of Class BasicTableUI
