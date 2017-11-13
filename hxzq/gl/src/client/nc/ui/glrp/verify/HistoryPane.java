package nc.ui.glrp.verify;

/**
 * ��ʷ��¼ui
 * �������ڣ�(2003-5-12 19:24:18)
 * @author������
 */
import java.awt.BorderLayout;
import java.util.List;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.gl.accbook.ALLChooseTableUtil;
import nc.ui.gl.datacache.AccountCache;
import nc.ui.gl.datacache.CurrencyDataCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.pub.DataFormatUtilGL;
import nc.ui.subjvery.ClientInfo;
import nc.vo.bd.account.AccountVO;
import nc.vo.gateway60.accountbook.AccountBookUtil;
import nc.vo.gateway60.itfs.Currency;
import nc.vo.gl.accbook.ColFormatVO;
import nc.vo.glcom.nodecode.GlNodeConst;
import nc.vo.glrp.pub.VerifyMsg;
import nc.vo.glrp.verify.GlVerifyDispLogKey;

public class HistoryPane extends nc.ui.pub.beans.UIPanel implements IHistoryUI, nc.ui.pub.print.IDataSource,
		javax.swing.event.ListSelectionListener {
	private nc.ui.pub.beans.UITablePane ivjTablePane = null;
	private HistoryTableModel ivjTableModel = null;

	class MyMouseMotionAdapter extends java.awt.event.MouseMotionAdapter {
		public void mouseDragged(java.awt.event.MouseEvent e) {
			if (e.getSource() == getFixTable().getTableHeader()) {
				if (getFixTable().getTableHeader().getResizingColumn() != null)
					onColResized();
			}
		}
	}

	class IvjEventHandler implements java.awt.event.MouseListener {
		public void mouseClicked(java.awt.event.MouseEvent e) {

		};

		public void mouseEntered(java.awt.event.MouseEvent e) {
		};

		public void mouseExited(java.awt.event.MouseEvent e) {

		};

		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getSource() == getFixTable().getTableHeader())
				connEtoC1(e);
		};

		public void mouseReleased(java.awt.event.MouseEvent e) {

		};
	}

	private nc.ui.gl.accbook.TableFormatTackle m_TableFormatTackle = null;
	private boolean m_bSum = true;
	private IVerifyView m_View;
	private nc.vo.glrp.verify.FormatVO m_FormatVO;
	/* �̶��� */
	private HistoryFixTableModel m_FixTableModel;
	private nc.ui.pub.beans.UITable m_FixTable;
	private IVerifyModel m_verifyModel;

	/* �̶��е�ǰ��ѡ���� */
	private javax.swing.table.TableColumn m_col = null;
	private int m_maxWidth = 0;
	private IvjEventHandler ivjEventHandler = new IvjEventHandler();

	/**
	 * HistoryPane ������ע�⡣
	 */
	public HistoryPane() {
		super();
		initialize();
	}

	/**
	 * HistoryPane ������ע�⡣
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 */
	public HistoryPane(java.awt.LayoutManager p0) {
		super(p0);
	}

	/**
	 * HistoryPane ������ע�⡣
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 * @param p1
	 *            boolean
	 */
	public HistoryPane(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
	}

	/**
	 * HistoryPane ������ע�⡣
	 * 
	 * @param p0
	 *            boolean
	 */
	public HistoryPane(boolean p0) {
		super(p0);
	}

	/**
	 * connEtoC1: (chkDateRange.change.stateChanged(javax.swing.event.ChangeEvent) -->
	 * VerifyStandardPane.chkDateRange_PropertyChange(Ljava.beans.PropertyChangeEvent;)V)
	 * 
	 * @param arg1
	 *            javax.swing.event.ChangeEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1(java.awt.event.MouseEvent e) {
		try {
			// user code begin {1}
			m_col = getFixTable().getColumnModel().getColumn(getFixTable().getColumnModel().getColumnIndexAtX(e.getX()));
			m_maxWidth = getTablePane().getWidth()
					- (getFixTable().getColumnModel().getTotalColumnWidth() - m_col.getPreferredWidth()) - 10;
			// nc.bs.logging.Logger.debug(e.getX());
			// nc.bs.logging.Logger.debug(m_col==null?"null":m_col.getHeaderValue());
			// user code end

			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * ���ܣ�ֱ�Ӵ�ӡ ���ߣ����� ����ʱ�䣺(2003-5-19 20:35:19) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	public void directPrint() {
		nc.ui.pub.print.PrintDirectEntry printEntry = new nc.ui.pub.print.PrintDirectEntry(this);
		// ���ñ���
		printEntry.setTitle(VerifyMsg.getMSG_HISTORY_PRINTTITLE());

		nc.vo.gl.accbook.ColFormatVO[] formats = getFixTableModel().getFormatVO().getColFormatVOs();
		// ��������
		java.util.Vector vLine1 = new java.util.Vector();
		java.util.Vector vLine0 = new java.util.Vector();
		java.util.Vector align = new java.util.Vector();

		java.util.Vector vColWidths = new java.util.Vector();
		int fixCol = 0;
		int unFixCol = 0;

		Integer iZero = new Integer(0);
		Integer iOne = new Integer(1);
		Integer iTwo = new Integer(2);
		for (int i = 0; i < formats.length; i++) {
			if (formats[i].isVisiablity()) {
				vLine0.add(formats[i].getMultiHeadStr() == null ? formats[i].getColName() : formats[i].getMultiHeadStr());
				// vLine0.add(formats[i].getMultiHeadStr()==null?"":formats[i].getMultiHeadStr());
				vLine1.add(formats[i].getColName());
				/** ������� */
				if (formats[i].isFrozen() == true) {
					formats[i].setColWidth(getFixTable().getColumnModel().getColumn(fixCol).getWidth());
					fixCol++;
				} else {
					formats[i].setColWidth(getTablePane().getTable().getColumnModel().getColumn(unFixCol).getWidth());
					unFixCol++;
				}
				vColWidths.add(new Integer(formats[i].getColWidth()));
				if (formats[i].getAlignment() == nc.vo.gl.accbook.AlignmentConst.LeftAlign) {
					align.add(iZero);
				} else if (formats[i].getAlignment() == nc.vo.gl.accbook.AlignmentConst.CenterAlign) {
					align.add(iOne);
				} else if (formats[i].getAlignment() == nc.vo.gl.accbook.AlignmentConst.RightAlign) {
					align.add(iTwo);
				}

			}
		}
		String[] line1 = new String[vLine1.size()];
		String[] line0 = new String[vLine0.size()];
		int[] iAlignFlag = new int[line1.length];
		for (int i = 0; i < iAlignFlag.length; i++) {
			iAlignFlag[i] = ((Integer) align.elementAt(i)).intValue();
		}
		int[] colWidth = new int[vColWidths.size()];
		for (int i = 0; i < colWidth.length; i++) {
			colWidth[i] = ((Integer) vColWidths.elementAt(i)).intValue();
		}
		vLine1.copyInto(line1);
		vLine0.copyInto(line0);
		String[][] colNames = new String[2][];
		colNames[0] = line0;
		colNames[1] = line1;
		printEntry.setColNames(colNames);
		printEntry.setAlignFlag(iAlignFlag);

		// ���ñ�ͷ

		String[][] sTop = new String[2][1];
		// sTop[0][0] = /*"��ͷ��ѯ����:" + */
		// "��Ŀ��"+getModel().getHistoryCond();
		// sTop[1][0] = "���ڷ�Χ��" + getUI().getlbPeriod().getText();
		// printEntry.setTopStr(sTop);
		// printEntry.setTopStrFixed(true);
		// printEntry.setTopStrColRange(new int[] { line1.length - 1 });
		// ���ñ�β
		String[][] sBottom = new String[2][1];
		// sBottom[0][0] =
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000247")/*@res "��λ��"*/
		// + nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getName();
		sBottom[0][0] = nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000247")/* @res "��λ��" */
				+ new AccountBookUtil().getAccountingBookNameByPk(GlWorkBench.getDefaultMainOrg());

		// sBottom[0][1] =
		// "�Ʊ��ˣ�" + nc.ui.pub.ClientEnvironment.getInstance().getUser().getUser_name();
		// sBottom[1][0] =
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201","UPP20021201-000248")/*@res "��ӡʱ�䣺"*/
		// + nc.ui.pub.ClientEnvironment.getInstance().getBusinessDate().toString();
		sBottom[1][0] = nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000248")/* @res "��ӡʱ�䣺" */
				+ GlWorkBench.getBusiDate();
		// sBottom[1][1] = "�����������";
		printEntry.setBottomStr(sBottom);
		printEntry.setBottomStrAlign(new int[] { 0, 1 });
		printEntry.setBStrColRange(new int[] { line1.length - 1 });
		printEntry.setPageNumDisp(true);
		printEntry.setPageNumAlign(2);

		java.util.Vector vRange = new java.util.Vector();
		for (int i = 0; i < line0.length; i++) {
			if (line0[i].equals(line1[i])) {
				nc.ui.pub.print.datastruct.CellRange rangeTemp = new nc.ui.pub.print.datastruct.CellRange(0, i, 1, i);
				vRange.add(rangeTemp);
			}
		}
		int startCol = 0;
		int endCol = 0;
		boolean flag = false;
		while (startCol < line0.length) {
			flag = startCol == endCol ? false : true;
			if (endCol < line0.length && line0[startCol].equals(line0[endCol])) {
				if (startCol < line0.length - 1)
					endCol++;
				else
					startCol++;
			} else if (flag) {
				if ((endCol - startCol) != 1) {
					nc.ui.pub.print.datastruct.CellRange rangeTemp = new nc.ui.pub.print.datastruct.CellRange(0, startCol, 0,
							endCol - 1);
					vRange.add(rangeTemp);
				}
				startCol = endCol;
			}
		}
		nc.ui.pub.print.datastruct.CellRange[] range = new nc.ui.pub.print.datastruct.CellRange[vRange.size()];
		vRange.copyInto(range);
		printEntry.setCombinCellRange(range);

		// �õ���ǰҳ������
		int iFixCount = 0;
		int iUNFixCount = 0;
		for (int i = 0; i < formats.length; i++) {
			if (formats[i].isVisiablity()) {
				if (i < getFixTableModel().getColumnCount()) {
					iFixCount++;
				} else {
					iUNFixCount++;
				}
			}
		}
		Object[][] data = new Object[getFixTableModel().getRowCount()][line1.length];
		int[] fixLocation = new int[iFixCount];
		int[] unFixLocation = new int[iUNFixCount];
		int iFixIndex = 0;
		int iUnFixIndex = 0;
		for (int i = 0; i < formats.length; i++) {
			if (formats[i].isVisiablity()) {
				if (i < getFixTableModel().getColumnCount()) {
					fixLocation[iFixIndex] = i;
					iFixIndex++;
				} else {
					unFixLocation[iUnFixIndex] = i;
					iUnFixIndex++;
				}
			}
		}
		for (int i = 0; i < getFixTableModel().getRowCount(); i++) {
			int iFIndex = 0;
			for (int j = 0; j < iFixCount; j++) {
				data[i][j] = getFixTableModel().getValueAt(i, fixLocation[iFIndex]);
				if (data[i][j] instanceof Boolean) {
					if (((Boolean) data[i][j]).booleanValue() == true) {
						data[i][j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000127")/* @res "��" */;
					} else {
						data[i][j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000128")/* @res "��" */;
					}
				}
				iFIndex++;
			}
			int iUnFIndex = 0;
			for (int j = iFixCount; j < line1.length; j++) {
				data[i][j] = getTableModel().getValueAt(i, unFixLocation[iUnFIndex] - getFixTableModel().getColumnCount());
				iUnFIndex++;
			}
		}
		printEntry.setData(data);

		printEntry.setFixedCols(fixCol);
		nc.bs.logging.Logger.debug("-------------" + fixCol + "------------------");
		printEntry.setColWidth(colWidth);

		printEntry.preview();
	}

	/**
	 * 
	 * �õ����е���������ʽ���� Ҳ���Ƿ������ж����������ı��ʽ
	 * 
	 */
	public java.lang.String[] getAllDataItemExpress() {
		return new String[] { "BATCHID", "SELECTED", "PAIRNUM", "OPRDATE", "DEBIT_Y", "DEBIT_F", "DEBIT_B", "CREDIT_Y",
				"CREDIT_F", "CREDIT_B", "DEBIT_VOUCHERDATE", "CREDIT_VOUCHERDATE", "CREDIT_BUSINESSDATE", "DEBIT_BUSINESSDATE",
				"CREDIT_ASSVO", "CREDIT_DIGEST", "DEBIT_ASSVO", "DEBIT_DIGEST", "DEBIT_VERIFYNO", "CREDIT_VERIFYNO",
				"DEBIT_DETAILINDEX", "CREDIT_DETAILINDEX", "DEBIT_VOUCHERTYPENAME", "CREDIT_VOUCHERTYPENAME",
				"DISP_DEBIT_VOUCHNO", "DISP_CREDIT_VOUCHNO", "COND_SUBJNAME", "COND_ASSVO", "COND_DATE" };
	}

	/*
	 * �������ֱ��ģ��õ��Ľ����һ���������Ķ��巽ʽ��ͬ�� ��������
	 * 
	 * (1) ÿһ��������(�Ʊ��˳���) ����������չ,û���κ�������ϵ ------------------------------------------ ���\��Ŀ | ��Ŀ01 | ��Ŀ02
	 * ---------------------------------- (����) �� | (��Ŀ01)�� | (��Ŀ02)�� ------------------------------------------ �Ʊ���: (�Ʊ���)
	 * 
	 * (2) (����) ����չ (��Ŀ) ����չ (���) ������ (��Ŀ) ------------------------ ���\��Ŀ | (��Ŀ) �� ------------------------ (����) �� | (���)
	 * ----------------------- �Ʊ���: (�Ʊ���)
	 * 
	 * (3) (����) ����չ (��Ŀ) ����չ (���) ������ (��Ŀ ����) ------------------------ ���\��Ŀ | (��Ŀ) �� ------------------------ (����) �� | (���)
	 * ------------------------ �Ʊ���: (�Ʊ���)
	 * 
	 * ��ӡ���: -------------------------------- ���\��Ŀ | ��Ŀ1 | ��Ŀ2 -------------------------------- 1999 | 100 | 400 2000 | 200 | 500
	 * 3001 | 300 | 600 -------------------------------- �Ʊ���: xxx
	 */
	/**
	 * 
	 * �õ����е��������������� Ҳ���Ƿ������ж��������������
	 * 
	 */
	public java.lang.String[] getAllDataItemNames() {
		return null;
	}

	/**
	 * ���ܣ��õ�key����Ӧ��colformatvo ���ߣ����� ����ʱ�䣺(2003-5-22 10:44:48) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.gl.accbook.ColFormatVO
	 * @param iKey
	 *            int
	 */
	private ColFormatVO getColFormatVO(int iKey) {
		ColFormatVO[] voCols = getTableModel().getFormatVO().getColFormatVOs();
		for (int i = 0; i < voCols.length; i++) {
			if (iKey == voCols[i].getColKey()) {
				return voCols[i];
			}
		}
		return null;
	}

	/**
	 * ���ܣ��õ���ѯ������ӡ���� ���ߣ����� ����ʱ�䣺(2003-5-22 16:43:49) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return java.lang.String[]
	 * @param iKey
	 *            int
	 */
	private String[] getCondPrintData(int iKey) {
		nc.vo.glrp.verify.LogFilterCondVO voCond = getModel().getHistoryCond();
		switch (iKey) {
		case GlVerifyDispLogKey.COND_SUBJNAME:
			AccountVO voSubj = null;
			if (voCond.getSubjPk() != null) {
				voSubj = AccountCache.getInstance().getAccountVOByPK(voCond.getPk_glorgbook(), voCond.getSubjPk());
			} else if (voCond.getSubjCode() != null) {
				voSubj = AccountCache.getInstance().getAccountVOByCode(voCond.getPk_glorgbook(),
						voCond.getSubjCode());
			}
			if (voSubj != null) {
				return new String[] { voSubj.getCode() + " " + voSubj.getName() };
			} else {
				return null;
			}
		case GlVerifyDispLogKey.COND_ASSVO:
			if (voCond.getAssvos() != null) {
				return new String[] { nc.ui.glverifycom.ShowStyleTool.getShowAss(voCond.getPk_glorgbook(), voCond
						.getAssvos()) };
			} else {
				return null;
			}
		case GlVerifyDispLogKey.COND_DATE:
			String beginDate = voCond.getBeginDate() == null ? "":DataFormatUtilGL.formatStringDateTime(voCond.getBeginDate());
			String endDate = voCond.getEndDate() == null ? "":DataFormatUtilGL.formatStringDateTime(voCond.getEndDate());
			return new String[] { (beginDate + nc.ui.ml.NCLangRes.getInstance().getStrByID("20021201", "UPP20021201-000249")/* @res "����" */ + endDate) };
		case GlVerifyDispLogKey.COND_CURR:
			return new String[] { CurrencyDataCache.getInstance().getCurrtypeBypk(voCond.getPk_glorgbook(),
					voCond.getCurPk()).getName() };

		case GlVerifyDispLogKey.COND_OPTYPE:
			return new String[] { (voCond.isRedBlue() ? VerifyMsg.getDISP_REDBLUE() : VerifyMsg.getDISP_VERIFY()) };
		case GlVerifyDispLogKey.COND_VERIFYNO:
			if (voCond.getVerifyNo() != null) {
				return new String[] { voCond.getVerifyNo() };
			} else {
				return null;
			}
		case GlVerifyDispLogKey.COND_ANALYDATE:
			if (voCond.getDateType().equals("voucherDate")) {
				return new String[] { VerifyMsg.getDISP_VOUCHERDATE() };
			} else if (voCond.getDateType().equals("Businessdate")) {
				return new String[] { VerifyMsg.getDISP_BUSINESSDATE() };
			} else {
				return new String[] { VerifyMsg.getDISP_VERIFYDATE() };
			}

		}
		return null;
	}

	/**
	 * 
	 * ������������������飬���������ֻ��Ϊ 1 ���� 2 ���� null : û������ ���� 1 : �������� ���� 2 : ˫������
	 * 
	 */
	public java.lang.String[] getDependentItemExpressByExpress(String itemName) {
		return null;
	}

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-19 15:50:08) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public nc.ui.pub.beans.UITable getFixTable() {
		if (m_FixTable == null) {
			m_FixTable = new nc.ui.pub.beans.UITable();
			m_FixTable.setModel(getFixTableModel());
			m_FixTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			m_FixTable.getSelectionModel().addListSelectionListener(this);
			// m_FixTable.getTableHeader().addMouseMotionListener(new MyMouseMotionAdapter());
			m_FixTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		}
		return m_FixTable;
	}

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-19 15:49:27) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.ui.glrp.verify.HistoryFixTableModel
	 */
	public HistoryFixTableModel getFixTableModel() {
		if (m_FixTableModel == null) {
			m_FixTableModel = new HistoryFixTableModel();
		}
		return m_FixTableModel;
	}

	/**
	 * ���ܣ���ʽ����vo ���ߣ����� ����ʱ�䣺(2003-5-13 8:56:39) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.glrp.verify.FormatVO
	 */
	public nc.vo.glrp.verify.FormatVO getFormatVO() {
		if (m_FormatVO == null) {
			m_FormatVO = new nc.vo.glrp.verify.FormatVO();
			m_FormatVO.setAuxCurrType(ClientInfo.getInstance().bFracUsed());
			m_FormatVO.setSum(m_bSum);
			m_FormatVO.setDateName(ClientInfo.getInstance().getDateName());
		}
		return m_FormatVO;
	}

	/*
	 * �������е��������Ӧ������ ������ ����������� ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]�� ��� itemName ӵ��������� 1 ���������ӡϵͳ����������������ݵ�˳�����ж� String[] �еĴ�ŵ����� 2
	 * ���������ӡϵͳ�������������������������������
	 * 
	 * ģ�� 2 �����: [��Ŀ] ==> [100 200 300 --> 400 500 600]
	 * 
	 * ģ�� 3 �����: ��� getDependItemNamesByName("���") ==
	 * 
	 * [��Ŀ ����] ==> [100 200 300 400 500 600] ���к��� [���� ��Ŀ] ==> [100 400 200 500 300 600] ���к���
	 */
	public java.lang.String[] getItemValuesByExpress(String itemExpress) {
		if (itemExpress.equalsIgnoreCase("BATCHID")) {
			return getPrintData(GlVerifyDispLogKey.BATCHID);
		} else if (itemExpress.equalsIgnoreCase("SELECTED")) {
			return getPrintData(GlVerifyDispLogKey.SELECTED);
		} else if (itemExpress.equalsIgnoreCase("PAIRNUM")) {
			return getPrintData(GlVerifyDispLogKey.PAIRNUM);
		} else if (itemExpress.equalsIgnoreCase("OPRDATE")) {
			return getPrintData(GlVerifyDispLogKey.OPRDATE);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_Y")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_Y);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_F")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_F);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_B")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_B);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_Y")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_Y);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_F")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_F);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_B")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_B);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_VOUCHERDATE")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_VOUCHERDATE);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_VOUCHERDATE")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_VOUCHERDATE);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_BUSINESSDATE")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_BUSINESSDATE);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_BUSINESSDATE")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_BUSINESSDATE);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_ASSVO")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_ASSVO);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_DIGEST")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_DIGEST);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_ASSVO")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_ASSVO);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_DIGEST")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_DIGEST);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_VERIFYNO")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_VERIFYNO);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_VERIFYNO")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_VERIFYNO);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_DETAILINDEX")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_DETAILINDEX);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_DETAILINDEX")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_DETAILINDEX);
		} else if (itemExpress.equalsIgnoreCase("DEBIT_VOUCHERTYPENAME")) {
			return getPrintData(GlVerifyDispLogKey.DEBIT_VOUCHERTYPENAME);
		} else if (itemExpress.equalsIgnoreCase("CREDIT_VOUCHERTYPENAME")) {
			return getPrintData(GlVerifyDispLogKey.CREDIT_VOUCHERTYPENAME);
		} else if (itemExpress.equalsIgnoreCase("DISP_DEBIT_VOUCHNO")) {
			return getPrintData(GlVerifyDispLogKey.DISP_DEBIT_VOUCHNO);
		} else if (itemExpress.equalsIgnoreCase("DISP_CREDIT_VOUCHNO")) {
			return getPrintData(GlVerifyDispLogKey.DISP_CREDIT_VOUCHNO);
		} else if (itemExpress.equalsIgnoreCase("COND_SUBJNAME")) {
			return getCondPrintData(GlVerifyDispLogKey.COND_SUBJNAME);
		} else if (itemExpress.equalsIgnoreCase("COND_ASSVO")) {
			return getCondPrintData(GlVerifyDispLogKey.COND_ASSVO);
		} else if (itemExpress.equalsIgnoreCase("COND_DATE")) {
			return getCondPrintData(GlVerifyDispLogKey.COND_DATE);
		} else if (itemExpress.equalsIgnoreCase("COND_CURR")) {
			return getCondPrintData(GlVerifyDispLogKey.COND_CURR);
		} else if (itemExpress.equalsIgnoreCase("COND_OPTYPE")) {
			return getCondPrintData(GlVerifyDispLogKey.COND_OPTYPE);
		} else if (itemExpress.equalsIgnoreCase("COND_VERIFYNO")) {
			return getCondPrintData(GlVerifyDispLogKey.COND_VERIFYNO);
		} else if (itemExpress.equalsIgnoreCase("COND_ANALYDATE")) {
			return getCondPrintData(GlVerifyDispLogKey.COND_ANALYDATE);
		}

		return null;
	}

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-19 20:42:56) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return nc.ui.glrp.verify.IVerifyModel
	 */
	private IVerifyModel getModel() {
		return m_verifyModel;
	}

	/*
	 * ���ظ�����Դ��Ӧ�Ľڵ����
	 */
	public String getModuleName() {
		return getView().getModuleName();
	}

	/**
	 * ���ܣ��õ���ӡ�������� ���ߣ����� ����ʱ�䣺(2003-5-22 10:14:04) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return java.lang.String[]
	 * @param ikey
	 *            int
	 */
	private String[] getPrintData(int ikey) {
		ColFormatVO voCol = getColFormatVO(ikey);
		String[] sResults = null;
		Object oValue = null;
		if (voCol.isFrozen() == true) {
			sResults = new String[getFixTableModel().getRowCount()];
			for (int i = 0; i < sResults.length; i++) {
				if (ikey == nc.vo.glrp.verify.GlVerifyDispLogKey.SELECTED) {
					sResults[i] = (((Boolean) getFixTableModel().getValueAt(i, ikey, voCol.getDataType())).booleanValue() == true ? VerifyMsg.getDISP_YES()
							: VerifyMsg.getDISP_NO());
				} else {
					oValue = getFixTableModel().getValueAt(i, ikey, voCol.getDataType());
					sResults[i] = (oValue == null ? null : oValue.toString());
				}
			}
		} else {
			sResults = new String[getTableModel().getRowCount()];
			for (int i = 0; i < sResults.length; i++) {
				oValue = getTableModel().getValueAt(i, ikey, voCol.getDataType());
				sResults[i] = (oValue == null ? null : oValue.toString());
			}
		}
		return sResults;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-5-12 19:37:01)
	 * 
	 * @return nc.ui.gl.accbook.TableFormatTackle
	 */
	private nc.ui.gl.accbook.TableFormatTackle getTableFormatTackle() {
		if (m_TableFormatTackle == null) {
			m_TableFormatTackle = new nc.ui.gl.accbook.TableFormatTackle();
		}
		return m_TableFormatTackle;
	}

	/**
	 * ���� TableModel ����ֵ��
	 * 
	 * @return nc.ui.glrp.verify.HistoryTableModel
	 */
	/* ���棺�˷������������ɡ� */
	private HistoryTableModel getTableModel() {
		if (ivjTableModel == null) {
			try {
				ivjTableModel = new nc.ui.glrp.verify.HistoryTableModel();
				// user code begin {1}
				ivjTableModel.setTable(getTablePane().getTable());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTableModel;
	}

	/**
	 * ���� TablePane ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITablePane getTablePane() {
		if (ivjTablePane == null) {
			try {
				ivjTablePane = new nc.ui.pub.beans.UITablePane();
				ivjTablePane.setName("TablePane");
				// user code begin {1}
				getTablePane().getTable().getSelectionModel().addListSelectionListener(this);
				getTablePane().setRowHeaderView(getFixTable());
				getTablePane().setCorner(javax.swing.JScrollPane.UPPER_LEFT_CORNER, getFixTable().getTableHeader());
				getTablePane().getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				getTablePane().getTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePane;
	}

	/**
	 * ���ܣ��õ����� ���ߣ����� ����ʱ�䣺(2003-5-23 15:33:37) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return java.lang.String
	 */
	public String getTitleName() {
		return VerifyMsg.getMSG_HISTORY_TITLE();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-5-12 19:45:42)
	 * 
	 * @return nc.ui.glrp.verify.IVerifyView
	 */
	public IVerifyView getView() {
		return m_View;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		// nc.bs.logging.Logger.debug("--------- δ��׽�����쳣 ---------");
		// nc.bs.logging.Logger.error(exception.getMessage(), exception);
	}

	private ColFormatVO[] initColFormatVO(boolean bFlag) {
		ColFormatVO[] colFormats = new ColFormatVO[24];
		int iCol = -1;

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.SELECTED);
		colFormats[iCol].setColName(VerifyMsg.getDISP_SELECT());
		colFormats[iCol].setColWidth(30);
		colFormats[iCol].setFrozen(true);
		colFormats[iCol].setVisiablity(true);
		colFormats[iCol].setMultiHeadStr(null);
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);
		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_BOOLEAN);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.OPRDATE);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VERIFYDATE());
		colFormats[iCol].setColWidth(70);
		colFormats[iCol].setFrozen(true);
		colFormats[iCol].setVisiablity(true);
		colFormats[iCol].setMultiHeadStr(null);
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.BATCHID);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VERIFY_BATCHNO());
		colFormats[iCol].setColWidth(100);
		colFormats[iCol].setFrozen(true);
		colFormats[iCol].setVisiablity(true);
		colFormats[iCol].setMultiHeadStr(null);
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.PAIRNUM);
		colFormats[iCol].setColName(VerifyMsg.getDISP_SERIAL_NUM());
		colFormats[iCol].setColWidth(30);
		colFormats[iCol].setFrozen(true);
		colFormats[iCol].setVisiablity(false);
		colFormats[iCol].setMultiHeadStr(null);
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_VOUCHERDATE);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VOUCHERDATE());
		colFormats[iCol].setColWidth(70);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_BUSINESSDATE);
		colFormats[iCol].setColName(VerifyMsg.getDISP_BUSINESSDATE());
		colFormats[iCol].setColWidth(70);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(false);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_VERIFYNO);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VERIFYNO());
		colFormats[iCol].setColWidth(40);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DISP_DEBIT_VOUCHNO);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VOUCHERNO());
		colFormats[iCol].setColWidth(60);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_DETAILINDEX);
		colFormats[iCol].setColName(VerifyMsg.getDISP_DETAILIDX());
		colFormats[iCol].setColWidth(40);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_ASSVO);
		colFormats[iCol].setColName(VerifyMsg.getDISP_ASS());
		colFormats[iCol].setColWidth(100);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_DIGEST);
		colFormats[iCol].setColName(VerifyMsg.getDISP_DIGEST());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_Y);
		colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(true);
		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_F);
		colFormats[iCol].setColName(VerifyMsg.getDISP_FRAC());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(ClientInfo.getInstance().bFracUsed());
		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setDataType(68);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_B);
		colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(true);
		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_DEBIT());
		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);

//		iCol++;
//		colFormats[iCol] = new ColFormatVO();
//		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_J);
//		colFormats[iCol].setColName(VerifyMsg.DISP_GROUP_AMOUNT);
//		colFormats[iCol].setColWidth(80);
//		colFormats[iCol].setFrozen(false);
//		colFormats[iCol].setVisiablity(Currency.isStartGroupCurr(GlWorkBench.getLoginGroup()));
//		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
//		colFormats[iCol].setMultiHeadStr(VerifyMsg.DISP_DEBIT);
//		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_GROUP_AMOUNT);

//		iCol++;
//		colFormats[iCol] = new ColFormatVO();
//		colFormats[iCol].setColKey(GlVerifyDispLogKey.DEBIT_Q);
//		colFormats[iCol].setColName(VerifyMsg.DISP_GLOBAL_AMOUNT);
//		colFormats[iCol].setColWidth(80);
//		colFormats[iCol].setFrozen(false);
//		colFormats[iCol].setVisiablity(Currency.isStartGlobalCurr());
//		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
//		colFormats[iCol].setMultiHeadStr(VerifyMsg.DISP_DEBIT);
//		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_GLOBAL_AMOUNT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_VOUCHERDATE);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VOUCHERDATE());
		colFormats[iCol].setColWidth(70);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_BUSINESSDATE);
		colFormats[iCol].setColName(VerifyMsg.getDISP_BUSINESSDATE());
		colFormats[iCol].setColWidth(70);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(false);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_VERIFYNO);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VERIFYNO());
		colFormats[iCol].setColWidth(40);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.DISP_CREDIT_VOUCHNO);
		colFormats[iCol].setColName(VerifyMsg.getDISP_VOUCHERNO());
		colFormats[iCol].setColWidth(60);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_DETAILINDEX);
		colFormats[iCol].setColName(VerifyMsg.getDISP_DETAILIDX());
		colFormats[iCol].setColWidth(40);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_ASSVO);
		colFormats[iCol].setColName(VerifyMsg.getDISP_ASS());
		colFormats[iCol].setColWidth(100);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_DIGEST);
		colFormats[iCol].setColName(VerifyMsg.getDISP_DIGEST());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(!bFlag);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setAlignment(javax.swing.JLabel.LEFT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_Y);
		colFormats[iCol].setColName(VerifyMsg.getDISP_ORIGIN());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(true);
		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_AMOUNT);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_F);
		colFormats[iCol].setColName(VerifyMsg.getDISP_FRAC());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(ClientInfo.getInstance().bFracUsed());
		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setDataType(68);

		iCol++;
		colFormats[iCol] = new ColFormatVO();
		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_B);
		colFormats[iCol].setColName(VerifyMsg.getDISP_LOCAL());
		colFormats[iCol].setColWidth(80);
		colFormats[iCol].setFrozen(false);
		colFormats[iCol].setVisiablity(true);
		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
		colFormats[iCol].setMultiHeadStr(VerifyMsg.getDISP_CREDIT());
		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_LOCAL_AMOUNT);

//		iCol++;
//		colFormats[iCol] = new ColFormatVO();
//		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_J);
//		colFormats[iCol].setColName(VerifyMsg.DISP_GROUP_AMOUNT);
//		colFormats[iCol].setColWidth(80);
//		colFormats[iCol].setFrozen(false);
//		colFormats[iCol].setVisiablity(Currency.isStartGroupCurr(GlWorkBench.getLoginGroup()));
//		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
//		colFormats[iCol].setMultiHeadStr(VerifyMsg.DISP_CREDIT);
//		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_GROUP_AMOUNT);

//		iCol++;
//		colFormats[iCol] = new ColFormatVO();
//		colFormats[iCol].setColKey(GlVerifyDispLogKey.CREDIT_Q);
//		colFormats[iCol].setColName(VerifyMsg.DISP_GLOBAL_AMOUNT);
//		colFormats[iCol].setColWidth(80);
//		colFormats[iCol].setFrozen(false);
//		colFormats[iCol].setVisiablity(Currency.isStartGlobalCurr());
//		colFormats[iCol].setAlignment(javax.swing.JLabel.RIGHT);
//		colFormats[iCol].setMultiHeadStr(VerifyMsg.DISP_CREDIT);
//		colFormats[iCol].setDataType(IGlVerifyConst.STYLE_GLOBAL_AMOUNT);

		return colFormats;
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		getFixTable().getTableHeader().addMouseMotionListener(new MyMouseMotionAdapter());
		getFixTable().getTableHeader().addMouseListener(ivjEventHandler);
		// user code end
		// gettablepane().addMouseListener(ivjEventHandler);

	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			initConnections();
			// user code end
			setName("HistoryPane");
			setLayout(new java.awt.BorderLayout());
//			setSize(647, 319);
			add(getTablePane(), BorderLayout.CENTER);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initTable(isSum());
		// user code end
	}

	private void initTable(boolean bFlag) {
		nc.ui.pub.beans.UITable table = getTablePane().getTable();
		// nc.ui.gl.balancesubjass.TableModel model=new nc.ui.gl.balancesubjass.TableModel();

		// �õ�TableFormatVO
		ColFormatVO[] colVOs = initColFormatVO(bFlag);
		// m_TableFormatTackle =new nc.ui.gl.accbook.TableFormatTackle();
		getTableFormatTackle().setColFormatVO(colVOs);
		table.setAutoscrolls(true);
		// ��TableModel������TableFormatVO
		getTableModel().setFormatVO(getTableFormatTackle());
		getFixTableModel().setFormatVO(getTableFormatTackle());

		// ��Table������TableModel
		table.setModel(getTableModel());

		getTableModel().fireTableStructureChanged();
		getFixTableModel().fireTableStructureChanged();

		// ����Table,fixTable�ĸ�ʽ
		getTableFormatTackle().setColWidth(table);
		getTableFormatTackle().setVisiablity(table);
		getTableFormatTackle().setMultiHead(table);
		getTableFormatTackle().setFixColWidth(getFixTable());
		getTableFormatTackle().setFixVisiablity(getFixTable());
		getTableFormatTackle().setFixMultiHead(getFixTable());
		getTableFormatTackle().setAlignment(table);
		// getTableFormatTackle().setAlignment(getFixTable());
		ALLChooseTableUtil.addCheckCellRenderer(getFixTable(),0);
	}

	/*
	 * ���ظ��������Ƿ�Ϊ������ ������ɲ������㣻��������ֻ��Ϊ�ַ������� �硰������Ϊ�������������롱Ϊ��������
	 */
	public boolean isNumber(String itemExpress) {
		return false;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-5-12 19:42:50)
	 * 
	 * @return boolean
	 */
	public boolean isSum() {
		return m_bSum;
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			HistoryPane aHistoryPane;
			aHistoryPane = new HistoryPane();
			frame.setContentPane(aHistoryPane);
			frame.setSize(aHistoryPane.getSize());
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

	private void onColResized() {
		if (getFixTable().getColumnModel().getTotalColumnWidth() > getTablePane().getWidth() - 10) {
			// if (getFixTable().getColumnModel().getTotalColumnWidth()
			// > getTablePane().getWidth()) {
			// m_col.setPreferredWidth(m_prefWidth);
			// } else
			// m_col.setPreferredWidth(m_col.getPreferredWidth() - 5);
			m_col.setWidth(m_maxWidth);
			m_col.setPreferredWidth(m_maxWidth);
		}
		getFixTable().setPreferredScrollableViewportSize(
				new java.awt.Dimension(getFixTable().getColumnModel().getTotalColumnWidth(), getFixTable().getHeight()));
	}

	/**
	 * ���ܣ���ӡ ���ߣ����� ����ʱ�䣺(2003-5-12 15:08:51) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	public void print(boolean bDirect) {
		if (bDirect) {
			directPrint();
		} else {
			templetPrint();
		}
	}

	/**
	 * This method gets called when a bound property is changed.
	 * 
	 * @param evt
	 *            A PropertyChangeEvent object describing the event source and the property that has changed.
	 */
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		int iKey = Integer.parseInt(evt.getPropertyName());
		switch (iKey) {
		case GlVerifyDispLogKey.CHANGE_STATE:
			setSum(((nc.vo.pub.lang.UFBoolean) evt.getNewValue()).booleanValue());
			break;
		case GlVerifyDispLogKey.SELECTED:
			updateUi();
			break;
		}
	}

	public void resetFormat(nc.vo.glrp.verify.FormatVO format) {

		List<int[]> arr = new java.util.ArrayList<int[]>();

		arr.add(new int[] { GlVerifyDispLogKey.SELECTED });
		arr.add(new int[] { GlVerifyDispLogKey.OPRDATE });
		arr.add(new int[] { GlVerifyDispLogKey.BATCHID });
		arr.add(new int[] { GlVerifyDispLogKey.OPRDATE });
		arr.add(new int[] { GlVerifyDispLogKey.DEBIT_Y });
		arr.add(new int[] { GlVerifyDispLogKey.DEBIT_B });
		arr.add(new int[] { GlVerifyDispLogKey.CREDIT_Y });
		arr.add(new int[] { GlVerifyDispLogKey.CREDIT_B });

		if (!format.isSum()) {
			arr.add(new int[] { GlVerifyDispLogKey.PAIRNUM });
			// if (format.getDateName().equalsIgnoreCase("VoucherDate")) {
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_VOUCHERDATE });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_VOUCHERDATE });
			// } else if (format.getDateName().equalsIgnoreCase("BusinessDate")) {
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_BUSINESSDATE });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_BUSINESSDATE });
			// }
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_VOUCHNO });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_VOUCHNO });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_ASSVO });
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_ASSVO });
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_DIGEST });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_DIGEST });
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_VERIFYNO });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_VERIFYNO });
			arr.add(new int[] { GlVerifyDispLogKey.DISP_CREDIT_VOUCHNO });
			arr.add(new int[] { GlVerifyDispLogKey.DISP_DEBIT_VOUCHNO });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_DETAILINDEX });
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_DETAILINDEX });
		}
		// if (format.isAuxCurrType()) {
		// arr.add(new int[] { GlVerifyDispLogKey.DEBIT_F });
		// arr.add(new int[] { GlVerifyDispLogKey.CREDIT_F });
		// }
		if (Currency.isStartGroupCurr(GlWorkBench.getLoginGroup())) {
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_J });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_J });
		}
		if (Currency.isStartGlobalCurr()) {
			arr.add(new int[] { GlVerifyDispLogKey.DEBIT_Q });
			arr.add(new int[] { GlVerifyDispLogKey.CREDIT_Q });
		}
		setTableColVisibility(arr);
		updateUi();
	}

	/**
	 *���û��ܻ���ϸ״̬ �������ڣ�(2003-5-12 19:16:47)
	 * 
	 * @param bSum
	 *            boolean
	 */
	public void setbSum(boolean bSum) {
		m_bSum = bSum;
		getFormatVO().setSum(bSum);
		resetFormat(getFormatVO());
	}

	/**
	 * a���ܣ� ���ߣ����� ����ʱ�䣺(2003-5-13 8:56:39) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param newFormatVO
	 *            nc.vo.glrp.verify.FormatVO
	 */
	public void setFormatVO(nc.vo.glrp.verify.FormatVO newFormatVO) {
		m_FormatVO = newFormatVO;
	}

	/**
	 * ���ܣ�������ʷ��¼��ѯ���� ���ߣ����� ����ʱ�䣺(2003-5-13 8:41:45) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param voCond
	 *            nc.vo.glrp.verify.LogFilterCondVO
	 */
	public void setHisToryCond(nc.vo.glrp.verify.LogFilterCondVO voCond) {
		if (voCond.getDateType().equals("opdate")) { /* �������� */
			getFormatVO().setDateName(ClientInfo.getInstance().getDateName());
		} else {
			getFormatVO().setDateName(voCond.getDateType());
		}
		resetFormat(getFormatVO());
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-5-12 19:42:50)
	 * 
	 * @param newSum
	 *            boolean
	 */
	public void setSum(boolean newSum) {
		m_bSum = newSum;
		getFormatVO().setSum(newSum);
		resetFormat(getFormatVO());
	}

	/**
	 *�Ѳ����е�������Ϊ�ɼ����� ������Ϊһ��һάint���飬��������еĵ�һ��Ԫ��==BalanceSubjAssBSKey.K_AssVOs���ڶ���Ԫ��Ϊ��Ӧ�������������е�λ�ã�������Ԫ��Ϊ��������Ƽ�ֵ ���� ������ֻ����һ��Ԫ��Ϊ�е�Key
	 */
	private void setTableColVisibility(List colsNum) {
		nc.ui.pub.beans.UITable table = getTablePane().getTable();
		// nc.ui.pub.beans.UITable fixTable = (nc.ui.pub.beans.UITable) (getUITablePane().getRowHeader().getComponents()[0]);
		nc.ui.gl.accbook.TableFormatTackle vo = ((HistoryTableModel) table.getModel()).getFormatVO();
		ColFormatVO[] colFormats = vo.getColFormatVOs();

		for (int i = 0; i < colFormats.length; i++) {
			boolean flag = false;

			for (int j = 0; j < colsNum.size(); j++) {
				int[] iKey = (int[]) colsNum.get(j);
				if (colFormats[i].getColKey() == iKey[0]) {
					// if (colFormats[i].getColKey() == BalanceSubjAssBSKey.K_AssVOs) {
					// int[] info = (int[]) colFormats[i].getUserData();
					// if (info[0] == iKey[1] && info[1] == iKey[2])
					// flag = true;
					// }
					// else
					flag = true;

					if (flag)
						break;
				}

			}

			colFormats[i].setVisiablity(flag);

		}

		vo.setFixVisiablity(getFixTable());
		vo.setFixMultiHead(getFixTable());
		vo.setVisiablity(table);
		vo.setMultiHead(table);

		vo.setAlignment(table);
		// vo.setFixAlignment(fixTable);
	}

	/**
	 * ���ܣ���ui�б���model������ ���ߣ����� ����ʱ�䣺(2003-5-8 15:56:01) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param newModel
	 *            nc.ui.glrp.verify.IVerifyModel
	 */
	public void setVerifyModel(IVerifyModel newModel) {
		getTableModel().setModel(newModel);
		getFixTableModel().setModel(newModel);
		m_verifyModel = newModel;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-5-12 19:45:42)
	 * 
	 * @param newView
	 *            nc.ui.glrp.verify.IVerifyView
	 */
	public void setView(IVerifyView newView) {
		m_View = newView;
	}

	/**
	 * ���ܣ���ʾ������Ϣ ���ߣ����� ����ʱ�䣺(2003-5-8 16:57:43) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	public void showErrorMsg(String sMsg) throws Exception {
		getView().showErrorMsg(sMsg);
	}

	/**
	 * ���ܣ���ʾ��Ϣ ���ߣ����� ����ʱ�䣺(2003-5-10 11:33:08) ������<|> ����ֵ�� �㷨��
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	public void showMsg(String sMsg) {
		getView().showMsg(sMsg);
	}

	/**
	 * �����༭״̬ �������ڣ�(2003-5-11 15:24:07)
	 */
	public void stopEdit() {
	}

	/**
	 * ���ܣ�ģ���ӡ ���ߣ����� ����ʱ�䣺(2003-5-22 14:25:59) ������<|> ����ֵ�� �㷨��
	 * 
	 */
	private void templetPrint() {
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null, this);
		/* �������� */
		String pk_group = WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
		if (getModel().getHistoryState()) { /* ���� */
			print.setTemplateID(pk_group, GlNodeConst.GLNODE_VERIFY_TACKLE, GlWorkBench.getLoginUser(), null,
					"SUM");
		} else { /* ��ϸ */
			print.setTemplateID(pk_group, GlNodeConst.GLNODE_VERIFY_TACKLE, GlWorkBench.getLoginUser(), null,
					"DETAIL");
		}
		
		print.setSelected(print.getTemplateID());
		
		if (print.selectTemplate() > 0)
			print.preview();
		else
			// �û�ȡ��
			return;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-5-12 22:29:25)
	 */
	public void updateUi() {
		getTablePane().getTable().setRowHeight(getTablePane().getTable().getRowHeight());
		getFixTable().setRowHeight(getFixTable().getRowHeight());
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		try {
			if (e.getSource() == getFixTable().getSelectionModel()) {
				int i = getFixTable().getSelectedRow();
				getTablePane().getTable().setRowSelectionInterval(i, i);// getSelectionModel().set
			}
			if (e.getSource() == getTablePane().getTable().getSelectionModel()) {
				int i = getTablePane().getTable().getSelectedRow();
				getFixTable().setRowSelectionInterval(i, i);// getSelectionModel().set
			}
		} catch (Exception ex) {
		}
	}
}