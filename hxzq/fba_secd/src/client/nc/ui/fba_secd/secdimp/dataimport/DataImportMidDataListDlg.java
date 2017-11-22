package nc.ui.fba_secd.secdimp.dataimport;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTabbedPane;
import nc.vo.fba_secd.secdimp.pub.SystemConst;

public class DataImportMidDataListDlg extends UIDialog implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private BillTabbedPane ivjTabPanel = null;

	private BillScrollPane ivjHeadListTable = null;

	private BillScrollPane ivjBodyListTable = null;

	private UIButton m_OKButton = null;

	private UIButton m_CancelButton = null;

	private String[] _headColCodes_cn;

	private String[] _bodyColCodes_cn;

	private String[] _head_sd_fields;

	private String[] _body_sd_fields;

	private Map headTitleMap;

	private Map bodyTitleMap;

	private Vector _head_dataV;

	private Vector _body_dataV;

	public DataImportMidDataListDlg(Container parent) {
		super(parent);
	}

	public DataImportMidDataListDlg(Container parent, Map v, Map title,
			Map hm_info) {
		super(parent);
		Map<String, List<String>> sd_fields_cn_map = (Map<String, List<String>>) hm_info
				.get(SystemConst.SD_FIELDS_CN);
		List<String> head_fields_cn = sd_fields_cn_map.get(SystemConst.HEAD);
		_headColCodes_cn = head_fields_cn.toArray(new String[] {});
		List<String> body_fields_cn = sd_fields_cn_map.get(SystemConst.BODY);
		if (body_fields_cn != null) {
			_bodyColCodes_cn = body_fields_cn.toArray(new String[] {});
		}

		Map<String, List<String>> sd_fields_map = (Map<String, List<String>>) hm_info
				.get(SystemConst.SD_FIELDS);
		List<String> head_sd_fields = sd_fields_map.get(SystemConst.HEAD);
		_head_sd_fields = head_sd_fields.toArray(new String[] {});

		List<String> body_sd_fields = sd_fields_map.get(SystemConst.BODY);
		if (body_sd_fields != null) {
			_body_sd_fields = body_sd_fields.toArray(new String[] {});
		}

		// 主表数据
		List headLst = (List) v.get(SystemConst.HEAD);
		// 子表数据
		List bodyLst = (List) v.get(SystemConst.BODY);
		Map headTitle = (Map) title.get(SystemConst.HEAD);
		Map bodyTitle = (Map) title.get(SystemConst.BODY);

		_head_dataV = getVector(headLst);
		if (bodyLst != null) {
			_body_dataV = getVector(bodyLst);
		}

		headTitleMap = headTitle;
		bodyTitleMap = bodyTitle;
		initUI();
	}

	private Vector getVector(List list) {
		Vector v = new Vector(list.size());
		for (int i = 0; i < list.size(); i++) {
			v.add(list.get(i));
		}
		return v;
	}

	private BillTabbedPane getListTable() {
		if (ivjTabPanel == null) {
			ivjTabPanel = new BillTabbedPane();
			handleHeadTable();
			ivjTabPanel.insertTab("主表数据", null, ivjHeadListTable, "", 0);
			// ivjTabPanel.add(ivjHeadListTable);
			headleBodyTable();

		}
		return ivjTabPanel;
	}

	private void headleBodyTable() {
		if (_bodyColCodes_cn == null || _bodyColCodes_cn.length == 0) {
			return;
		}
		if (ivjBodyListTable == null) {
			ivjBodyListTable = new BillScrollPane();
			try {
				// 设置表头
				BillItem[] biBodyItems = new BillItem[_bodyColCodes_cn.length];
				for (int i = 0; i < _bodyColCodes_cn.length; i++) {
					biBodyItems[i] = new BillItem();
					biBodyItems[i].setKey(_bodyColCodes_cn[i]);
					biBodyItems[i].setName(_bodyColCodes_cn[i]);
					biBodyItems[i].setWidth(100);
					biBodyItems[i].setEdit(false);
					biBodyItems[i].setShow(true);
				}

				BillModel oModel = new BillModel();
				oModel.setBodyItems(biBodyItems);
				ivjBodyListTable.setTableModel(oModel);
				ivjBodyListTable.setRowNOShow(true);
				ivjBodyListTable.getTable().setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjBodyListTable.updateUI();

				if (bodyTitleMap != null && bodyTitleMap.size() > 0) {
					// 设置表数据
					if (_body_dataV != null) {
						Vector lresult = new Vector();
						if (_body_dataV.size() > 0) {
							for (int n = 0; n < _body_dataV.size(); n++) {
								Map data = (Map) _body_dataV.get(n);
								Vector result = new Vector();
								for (int i = 0; i < _body_sd_fields.length; i++) {
									String field = _body_sd_fields[i];
									if (field.contains(SystemConst.perfix)) {
										String[] fields = field.split("\\"
												+ SystemConst.perfix);
										String temp = "";
										for (int j = 0; j < fields.length; j++) {

											/**** 取出 titmap的所有key值 ---liangwei *****/
											Set keyset = bodyTitleMap.keySet();
											Iterator it = keyset.iterator();
											/****************/
											// 对key值做判断转换
											while (it.hasNext()) {
												String key = (String) it.next();
												if (key.contains(fields[j])) {
													fields[j] = key;
												}
											}
											// int col =
											// Integer.parseInt(titmap.get(fields[j]).toString());
											temp += data.get(fields[j]) + "+";
										}
										result.add(temp.substring(0,
												temp.length() - 1));
									} else {
										// int col =
										// Integer.parseInt(titmap.get(field +
										// i).toString());
										result.add(data.get(field));
									}
								}
								lresult.add(result);
							}
						}

						ivjBodyListTable.getTableModel().setDataVector(lresult);
					}
				} else {
					ivjBodyListTable.getTableModel().setDataVector(_body_dataV);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, "错误", e.getMessage());
			}
		}
		ivjTabPanel.insertTab("子表数据", null, ivjBodyListTable, "", 1);
		// ivjTabPanel.add(ivjBodyListTable);

	}

	private void handleHeadTable() {
		if (ivjHeadListTable == null) {
			ivjHeadListTable = new BillScrollPane();
			try {
				// 设置表头
				BillItem[] biBodyItems = new BillItem[_headColCodes_cn.length];
				for (int i = 0; i < _headColCodes_cn.length; i++) {
					biBodyItems[i] = new BillItem();
					biBodyItems[i].setKey(_headColCodes_cn[i]);
					biBodyItems[i].setName(_headColCodes_cn[i]);
					biBodyItems[i].setWidth(100);
					biBodyItems[i].setEdit(false);
					biBodyItems[i].setShow(true);
				}

				BillModel oModel = new BillModel();
				oModel.setBodyItems(biBodyItems);
				ivjHeadListTable.setTableModel(oModel);
				ivjHeadListTable.setRowNOShow(true);
				ivjHeadListTable.getTable().setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjHeadListTable.updateUI();

				if (headTitleMap != null && headTitleMap.size() > 0) {
					// 设置表数据
					if (_head_dataV != null) {
						Vector lresult = new Vector();
						if (_head_dataV.size() > 0) {
							for (int n = 0; n < _head_dataV.size(); n++) {
								Map data = (Map) _head_dataV.get(n);
								Vector result = new Vector();
								for (int i = 0; i < _head_sd_fields.length; i++) {
									String field = _head_sd_fields[i];
									if (field.contains(SystemConst.perfix)) {
										String[] fields = field.split("\\"
												+ SystemConst.perfix);
										String temp = "";
										for (int j = 0; j < fields.length; j++) {

											/**** 取出 titmap的所有key值 ---liangwei *****/
											Set keyset = headTitleMap.keySet();
											Iterator it = keyset.iterator();
											/****************/
											// 对key值做判断转换
											while (it.hasNext()) {
												String key = (String) it.next();
												if (key.contains(fields[j])) {
													fields[j] = key;
												}
											}
											// int col =
											// Integer.parseInt(titmap.get(fields[j]).toString());
											temp += data.get(fields[j]) + "+";
										}
										result.add(temp.substring(0,
												temp.length() - 1));
									} else {
										// int col =
										// Integer.parseInt(titmap.get(field +
										// i).toString());
										result.add(data.get(field));
									}
								}
								lresult.add(result);
							}
						}

						ivjHeadListTable.getTableModel().setDataVector(lresult);
					}
				} else {
					ivjHeadListTable.getTableModel().setDataVector(_head_dataV);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, "错误", e.getMessage());
			}
		}
	}

	public UIButton getCancelButton() {
		if (m_CancelButton == null) {
			m_CancelButton = new UIButton("取消");
			m_CancelButton.addActionListener(this);
		}
		return m_CancelButton;
	}

	public UIButton getOKButton() {
		if (m_OKButton == null) {
			m_OKButton = new UIButton("确定");
			m_OKButton.addActionListener(this);
		}
		return m_OKButton;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == m_OKButton) {
				closeOK();
			} else if (e.getSource() == m_CancelButton) {
				closeCancel();
			}
		} catch (Exception ee) {
			Logger.error(ee.getMessage(), ee);
			MessageDialog.showErrorDlg(this, "错误", ee.getMessage());
		}
	}

	private void initUI() {
		if (getTitle() == null) {
			setTitle("要导入数据列表");
		}
		setResizable(true);
		setSize(700, 500);
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		UIPanel p1 = new UIPanel();
		p1.setBorder(new EtchedBorder());
		p1.setPreferredSize(new Dimension(0, 40));
		p1.setLayout(new FlowLayout());
		p1.add(getOKButton());
		p1.add(getCancelButton());
		pane.add(p1, BorderLayout.SOUTH);
		pane.add(getListTable(), BorderLayout.CENTER);
	}
}
