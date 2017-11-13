package nc.ui.fba_secd.secdimp.dataimport.change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.pub.lang.UFDouble;

public class DataEventHandler {
	public DataEventHandler(MainPanel billUI) {
		this.billUi = billUI;
	}

	private MainPanel billUi = null;
	String[] hyjz = { "BILL_CODE", "PK_ASSETSPROP", "PK_CAPACCOUNT",
			"PK_SELFSGROUP", "TYPES", "PK_SECURITIES", "LASTPOSITIONS_NUM",
			"LASTPOSITIONS_PRICE", "THISOPEN_NUM", "THISOPEN_PRICE",
			"THISCLOSED_NUM", "THISCLOSED_PRICE", "THISDELIVERY_NUM",
			"THISDELIVERY_PRICE", "THISPOSITIONS_NUM", "THISPOSITIONS_PRICE",
			"LASTSETTLE_PRICE", "THISSETTLE_PRICE", "THISMARKET_VALUE",
			"OFFSETLOSS", "LASTPOSITIONLOSS", "THISPOSITIONLOSS", "THISMARGIN",
			"LASTMARGIN", "MARGINCHANGES", "COMMISION", "FACT_SUM",
			"TRADE_DATE", "NDEF1" };
	String[] zj = { "BILL_CODE", "PK_ASSETSPROP", "PK_CAPACCOUNT",
			"PK_SELFSGROUP", "TYPES", "PK_SECURITIES", "LASTPOSITIONS_NUM",
			"LASTPOSITIONS_PRICE", "THISOPEN_NUM", "THISOPEN_PRICE",
			"THISCLOSED_NUM", "THISCLOSED_PRICE", "THISDELIVERY_NUM",
			"THISDELIVERY_PRICE", "THISPOSITIONS_NUM", "THISPOSITIONS_PRICE",
			"LASTSETTLE_PRICE", "THISSETTLE_PRICE", "THISMARKET_VALUE",
			"OFFSETLOSS", "LASTPOSITIONLOSS", "THISPOSITIONLOSS", "THISMARGIN",
			"LASTMARGIN", "MARGINCHANGES", "COMMISION", "FACT_SUM",
			"TRADE_DATE", "vef1", "vef2", "rgole", "cgole" };

	public List getExportData(Map<String, Object> parseFile) throws Exception {
		Map<String, Object> sheetdata1 = new HashMap();
		Map<String, Object> sheetdata2 = new HashMap();
		HashSet<String> hySet = new HashSet();
		Map<String, Object> srjeMap = new HashMap();
		Map<String, Object> srbzjMap = new HashMap();
		Map<String, Object> srfkykMap = new HashMap();
		for (String str : parseFile.keySet()) {
			if (str.subSequence(0, 1).equals("1")) {
				sheetdata1.put(str,
						parseFile.get(str) == null ? "" : parseFile.get(str));
				if ((parseFile.get(str) != null)
						&& ((parseFile.get(str).toString().startsWith("I")) || (parseFile
								.get(str).toString().startsWith("T")))) {
					hySet.add(parseFile.get(str).toString());
				}
			}
			if (str.subSequence(0, 1).equals("2")) {
				sheetdata2.put(str, parseFile.get(str));
			}
		}
		String[] sheet1 = (String[]) sheetdata1.keySet().toArray(new String[0]);
		String[] sheet2 = (String[]) sheetdata2.keySet().toArray(new String[0]);

		orderByRowAndCol(sheet1);
		orderByRowAndCol(sheet2);

		Object accout = parseFile.get("1sheet5row3col");
		Object date = parseFile.get("1sheet5row8col");
		Object nowisusesum = parseFile.get("1sheet18row8col");
		Object nowbzjsum = parseFile.get("1sheet17row8col");
		Object nowcqsum = parseFile.get("1sheet13row3col");
		Object nowfdyksum = parseFile.get("1sheet18row3col");
		Object rgole = parseFile.get("1sheet13row3col");// 当日存取合计

		HashMap<String, Object> pcykMap = new HashMap();
		HashMap<String, Object> sxfkMap = new HashMap();
		for (int m = 0; m < sheet2.length; m++) {
			if (sheetdata2.get(sheet2[m]) != null) {
				if (sheetdata2.get(sheet2[m]).toString().startsWith("I")
						&& !sheetdata2.get(sheet2[m]).toString().contains("IC")
						&& !sheetdata2.get(sheet2[m]).toString().contains("IH")) {
					if (pcykMap.containsKey("IPC")) {
						pcykMap.put("IPC",
								new UFDouble(pcykMap.get("IPC") == null ? "0"
										: pcykMap.get("IPC").toString())
										.add(new UFDouble(sheetdata2.get(
												sheet2[(m + 4)]).toString())));
					} else {
						pcykMap.put("IPC", sheetdata2.get(sheet2[(m + 4)]));
					}
					if (sxfkMap.containsKey("ISX")) {
						sxfkMap.put("ISX",
								new UFDouble(sxfkMap.get("ISX") == null ? "0"
										: sxfkMap.get("ISX").toString())
										.add(new UFDouble(sheetdata2.get(
												sheet2[(m + 3)]).toString())));
					} else {
						sxfkMap.put("ISX", sheetdata2.get(sheet2[(m + 3)]));
					}
				}
				if (sheetdata2.get(sheet2[m]).toString().startsWith("IC")) {
					if (pcykMap.containsKey("ICPC")) {
						pcykMap.put("ICPC", new UFDouble(
								pcykMap.get("ICPC") == null ? "0" : pcykMap
										.get("ICPC").toString())
								.add(new UFDouble(sheetdata2.get(
										sheet2[(m + 4)]).toString())));
					} else {
						pcykMap.put("ICPC", sheetdata2.get(sheet2[(m + 4)]));
					}
					if (sxfkMap.containsKey("ICSX")) {
						sxfkMap.put("ICSX", new UFDouble(
								sxfkMap.get("ICSX") == null ? "0" : sxfkMap
										.get("ICSX").toString())
								.add(new UFDouble(sheetdata2.get(
										sheet2[(m + 3)]).toString())));
					} else {
						sxfkMap.put("ICSX", sheetdata2.get(sheet2[(m + 3)]));
					}
				}
				if (sheetdata2.get(sheet2[m]).toString().startsWith("IH")) {
					if (pcykMap.containsKey("IHPC")) {
						pcykMap.put("IHPC", new UFDouble(
								pcykMap.get("IHPC") == null ? "0" : pcykMap
										.get("IHPC").toString())
								.add(new UFDouble(sheetdata2.get(
										sheet2[(m + 4)]).toString())));
					} else {
						pcykMap.put("IHPC", sheetdata2.get(sheet2[(m + 4)]));
					}
					if (sxfkMap.containsKey("IHSX")) {
						sxfkMap.put("IHSX", new UFDouble(
								sxfkMap.get("IHSX") == null ? "0" : sxfkMap
										.get("IHSX").toString())
								.add(new UFDouble(sheetdata2.get(
										sheet2[(m + 3)]).toString())));
					} else {
						sxfkMap.put("IHSX", sheetdata2.get(sheet2[(m + 3)]));
					}
				}
				if (sheetdata2.get(sheet2[m]).toString().startsWith("T")) {
					if (pcykMap.containsKey("TPC")) {
						pcykMap.put("TPC",
								new UFDouble(pcykMap.get("TPC") == null ? "0"
										: pcykMap.get("TPC").toString())
										.add(new UFDouble(sheetdata2.get(
												sheet2[(m + 4)]).toString())));
					} else {
						pcykMap.put("TPC", sheetdata2.get(sheet2[(m + 4)]));
					}
					if (sxfkMap.containsKey("TSX")) {
						sxfkMap.put("TSX",
								new UFDouble(sxfkMap.get("TSX") == null ? "0"
										: sxfkMap.get("TSX").toString())
										.add(new UFDouble(sheetdata2.get(
												sheet2[(m + 3)]).toString())));
					} else {
						sxfkMap.put("TSX", sheetdata2.get(sheet2[(m + 3)]));
					}
				}
			}
		}
		if (pcykMap.get("IPC") == null) {
			pcykMap.put("IPC", Integer.valueOf(0));
		}
		if (sxfkMap.get("ISX") == null) {
			sxfkMap.put("ISX", Integer.valueOf(0));
		}
		if (pcykMap.get("TPC") == null) {
			pcykMap.put("TPC", Integer.valueOf(0));
		}
		if (sxfkMap.get("TSX") == null) {
			sxfkMap.put("TSX", Integer.valueOf(0));
		}
		HashMap<String, Object> fdykMap = new HashMap();
		// 交易保证金map
		HashMap<String, Object> jybzjMap = new HashMap();
		HashMap<String, Object> mccMap = new HashMap();
		HashMap<String, Object> maiccMap = new HashMap();
		String rowcolmcckey;
		UFDouble mcctotal;
		Object sbfobj = null;
		for (int m = 0; m < sheet1.length; m++) {
			// 取中金所申报费
			if ((sheetdata1.get(sheet1[m]).equals("中金所申报费"))
					&& (sheetdata1.get(sheet1[(m - 1)]).equals("合计"))) {
				sbfobj = sheetdata1.get(sheet1[(m + 1)]);
			}

			if ((sheetdata1.get(sheet1[m]).equals("浮动盈亏"))
					&& (sheetdata1.get(sheet1[(m + 1)]).equals("交易保证金"))) {
				String[] splits = sheet1[m].split("sheet");
				String[] splitr = splits[1].split("row");
				for (int i = Integer.parseInt(splitr[0]); i > 0; i++) {
					String rowcolkey = splits[0] + "sheet" + (i + 1)
							+ "row1col";
					if ((!sheetdata1.get(rowcolkey).toString().startsWith("I"))
							&&

							(!sheetdata1.get(rowcolkey).toString()
									.startsWith("T"))) {
						break;
					}
					String rowcolykkey = splits[0] + "sheet" + (i + 1) + "row"
							+ splitr[1];
					if (fdykMap.containsKey(sheetdata1.get(rowcolkey)
							.toString())) {
						if ((fdykMap.get(sheetdata1.get(rowcolkey).toString()) != null)
								&& (sheetdata1.get(rowcolykkey) != null)) {
							UFDouble yktotal = new UFDouble(fdykMap.get(
									sheetdata1.get(rowcolkey).toString())
									.toString());
							UFDouble newyk = new UFDouble(sheetdata1.get(
									rowcolykkey).toString());
							yktotal = yktotal.add(newyk);
							fdykMap.put(sheetdata1.get(rowcolkey).toString(),
									yktotal);
						}
					} else
						fdykMap.put(sheetdata1.get(rowcolkey).toString(),
								sheetdata1.get(rowcolykkey));
					
					//交易保证金
					String rowcolbzjkey = splits[0] + "sheet" + (i + 1) + "row"
							+ "9col";
					if (jybzjMap.containsKey(sheetdata1.get(rowcolkey)
							.toString())) {
						if ((jybzjMap.get(sheetdata1.get(rowcolkey).toString()) != null)
								&& (sheetdata1.get(rowcolbzjkey) != null)) {
							UFDouble bzjtotal = new UFDouble(jybzjMap.get(
									sheetdata1.get(rowcolkey).toString())
									.toString());
							UFDouble newbzj = new UFDouble(sheetdata1.get(
									rowcolbzjkey).toString());
							bzjtotal = bzjtotal.add(newbzj);
							jybzjMap.put(sheetdata1.get(rowcolkey).toString(),
									bzjtotal);
						}
					} else
						jybzjMap.put(sheetdata1.get(rowcolkey).toString(),
								sheetdata1.get(rowcolbzjkey));

					rowcolmcckey = splits[0] + "sheet" + (i + 1) + "row2col";
					String rowcolmaicckey = splits[0] + "sheet" + (i + 1)
							+ "row4col";
					String rowcoljjsjkey = splits[0] + "sheet" + (i + 1)
							+ "row7col";
					if ((mccMap.containsKey(sheetdata1.get(rowcolkey)
							.toString()))
							&& (mccMap
									.get(sheetdata1.get(rowcolkey).toString()) != null)) {
						UFDouble mcc = new UFDouble(
								sheetdata1.get(rowcolmcckey) == null ? "0.0"
										: sheetdata1.get(rowcolmcckey)
												.toString());
						UFDouble maicc = new UFDouble(
								sheetdata1.get(rowcolmaicckey) == null ? "0.0"
										: sheetdata1.get(rowcolmaicckey)
												.toString());
						UFDouble jjsj = new UFDouble(
								sheetdata1.get(rowcoljjsjkey) == null ? "0.0"
										: sheetdata1.get(rowcoljjsjkey)
												.toString());
						mcctotal = new UFDouble(mccMap.get(
								sheetdata1.get(rowcolkey).toString())
								.toString());
						UFDouble maicctotal = new UFDouble(maiccMap.get(
								sheetdata1.get(rowcolkey).toString())
								.toString());

						mcctotal = mcctotal.add(mcc.multiply(jjsj));
						maicctotal = maicctotal.add(maicc.multiply(jjsj));
						mccMap.put(sheetdata1.get(rowcolkey).toString(),
								mcctotal);
						maiccMap.put(sheetdata1.get(rowcolkey).toString(),
								maicctotal);
					} else {
						UFDouble mcc = new UFDouble(
								sheetdata1.get(rowcolmcckey) == null ? "0.0"
										: sheetdata1.get(rowcolmcckey)
												.toString());
						UFDouble maicc = new UFDouble(
								sheetdata1.get(rowcolmaicckey) == null ? "0.0"
										: sheetdata1.get(rowcolmaicckey)
												.toString());
						UFDouble jjsj = new UFDouble(
								sheetdata1.get(rowcoljjsjkey) == null ? "0.0"
										: sheetdata1.get(rowcoljjsjkey)
												.toString());
						mccMap.put(sheetdata1.get(rowcolkey).toString(),
								mcc.multiply(jjsj));
						maiccMap.put(sheetdata1.get(rowcolkey).toString(),
								maicc.multiply(jjsj));
					}
				}
			}
		}
		StringBuffer sqllastdate = new StringBuffer();
		sqllastdate
				.append(" select * from ( select   calendardate from bd_workcalendar  wc ");
		sqllastdate.append(" left join bd_workcalendardate wcd");
		sqllastdate.append(" on wc.pk_workcalendar  = wcd.pk_workcalendar ");
		sqllastdate.append(" where wc.enablestate = 2");
		sqllastdate.append(" and nvl(wc.dr,0) = 0  ");
		sqllastdate.append(" and nvl(wcd.dr,0)=0   ");
		sqllastdate.append(" and wcd.datetype= 0 ");
		sqllastdate
				.append(" and wcd.calendardate <substr('"
						+ date
						+ "',0,10)  order by wcd.calendardate  desc) where  rownum=1  ");
		Logger.debug("工作日" + date);
		Object lastdate = null;
		lastdate = getItf().executeQuery(sqllastdate.toString(),
				new ColumnProcessor("calendardate"));
		if (lastdate == null) {
			lastdate = date;
		}
		Logger.debug("上一个工作日" + lastdate.toString());
		StringBuffer sql;
		if ((lastdate != null) && (lastdate.toString() != null)) {
			for (String strhy : hySet) {
				String outsystemvalue = "";
				if (strhy.startsWith("T")) {
					outsystemvalue = "TF0000";
				} else if (strhy.startsWith("IC")) {
					outsystemvalue = "IC0000";
				} else if (strhy.startsWith("IH")) {
					outsystemvalue = "IH0000";
				} else {
					outsystemvalue = "IF0000";
				}
				sql = new StringBuffer();
				sql.append("select  distinct a.thissettle_price    from sim_stockfutures  a  inner join sec_securities b on a.pk_securities =b.pk_securities where    b.pk_securities in(select distinct d.insystempk from sec_datacontrast c inner join sec_datacontritem d on c.pk_datacontrast=d.pk_datacontrast where c.pk_org='"
						+

						this.billUi.getE().getContext().getPk_org()
						+

						"' and d.outsystemvalue like '"
						+ outsystemvalue
						+ "%' )  "
						+ "    and substr(a.trade_date,0,10) ='"
						+ lastdate.toString()
						+ "' and isnull(a.dr,0)=0 and a.pk_org='"
						+ this.billUi.getE().getContext().getPk_org() + "'\t ");
				Object[] resultje = new String[1];
				resultje = (Object[]) getItf().executeQuery(sql.toString(),
						new ArrayProcessor());
				if ((resultje != null) && (resultje[0] != null)) {
					srjeMap.put(strhy, new UFDouble(resultje[0].toString()));
				} else {
					int showYesNoDlg = MessageDialog.showYesNoDlg(this.billUi,
							"提示", "未获取到" + outsystemvalue + "上一个工作日["
									+ lastdate.toString() + "]的可用金额是否继续？");
					if (showYesNoDlg != 4) {
						return null;
					}
				}
				StringBuffer sqlbzj = new StringBuffer();
				sqlbzj.append("select  distinct a.thismargin   from sim_stockfutures a  inner join sec_securities b on a.pk_securities =b.pk_securities where    b.pk_securities in(select distinct d.insystempk from sec_datacontrast c inner join sec_datacontritem d on c.pk_datacontrast=d.pk_datacontrast where c.pk_org='"
						+

						this.billUi.getE().getContext().getPk_org()
						+

						"' and d.outsystemvalue like '"
						+ outsystemvalue
						+ "%' )  "
						+ "    and substr(a.trade_date,0,10) ='"
						+ lastdate.toString()
						+ "' and isnull(a.dr,0)=0 and a.pk_org='"
						+ this.billUi.getE().getContext().getPk_org() + "'\t ");
				Object[] resultbzj = new String[1];
				resultbzj = (Object[]) getItf().executeQuery(sqlbzj.toString(),
						new ArrayProcessor());
				if ((resultbzj != null) && (resultbzj[0] != null)) {
					srbzjMap.put(strhy, new UFDouble(resultbzj[0].toString()));
				} else {
					int showYesNoDlg = MessageDialog.showYesNoDlg(this.billUi,
							"提示", "未获取到" + outsystemvalue + "上一个工作日["
									+ lastdate.toString() + "]的保证金是否继续？");
					if (showYesNoDlg != 4) {
						return null;
					}
				}
				StringBuffer sql2 = new StringBuffer();
				sql2.append("select  distinct a.profloss  from sim_stockfutures  a  inner join sec_securities b on a.pk_securities =b.pk_securities where    b.pk_securities  in(select distinct d.insystempk from sec_datacontrast c inner join sec_datacontritem d on c.pk_datacontrast=d.pk_datacontrast where c.pk_org='"
						+

						this.billUi.getE().getContext().getPk_org()
						+

						"' and d.outsystemvalue like '"
						+ outsystemvalue
						+ "%' )  "
						+ "    and substr(a.trade_date,0,10) ='"
						+ lastdate.toString()
						+ "' and isnull(a.dr,0)=0 and a.pk_org='"
						+ this.billUi.getE().getContext().getPk_org() + "'\t ");
				Object[] resultsrfkyk = new String[1];
				resultsrfkyk = (Object[]) getItf().executeQuery(
						sql2.toString(), new ArrayProcessor());
				if ((resultsrfkyk != null) && (resultsrfkyk[0] != null)) {
					srfkykMap.put(strhy,
							new UFDouble(resultsrfkyk[0].toString()));
				} else {
					int showYesNoDlg = MessageDialog.showYesNoDlg(this.billUi,
							"提示", "未获取到" + outsystemvalue + "上一个工作日["
									+ lastdate.toString() + "]的浮动盈亏是否继续？");
					if (showYesNoDlg != 4) {
						return null;
					}
				}
			}
		} else {
			int showYesNoDlg = MessageDialog.showYesNoDlg(this.billUi, "提示",
					"未获取到上一个工作日是否继续？");
			if (showYesNoDlg != 1) {
				return null;
			}
		}
		List exportData = new ArrayList();
		for (String strhy : hySet) {
			if (exportData.size() == 4) {
				break;
			}
			String hy = null;
			boolean hasSame = false;
			if (strhy.startsWith("T")) {
				if ((exportData.size() > 0)) {
					for (int i = 0; i < exportData.size(); i++) {
						if (("TF0000"
								.equals(((Map) exportData.get(i)).get("6")))) {
							hasSame = true;
							break;
						}
					}
					if (hasSame)
						continue;
				}
				hy = "TF0000";
			} else if (strhy.startsWith("IC")) {
				if ((exportData.size() > 0)) {
					for (int i = 0; i < exportData.size(); i++) {
						if (("IC0000"
								.equals(((Map) exportData.get(i)).get("6")))) {
							hasSame = true;
							break;
						}
					}
					if (hasSame)
						continue;
				}
				hy = "IC0000";
			} else if (strhy.startsWith("IH")) {
				if ((exportData.size() > 0)) {
					for (int i = 0; i < exportData.size(); i++) {
						if (("IH0000"
								.equals(((Map) exportData.get(i)).get("6")))) {
							hasSame = true;
							break;
						}
					}
					if (hasSame)
						continue;
				}
				hy = "IH0000";
			} else {
				if ((exportData.size() > 0)) {
					for (int i = 0; i < exportData.size(); i++) {
						if (("IF0000"
								.equals(((Map) exportData.get(i)).get("6")))) {
							hasSame = true;
							break;
						}
					}
					if (hasSame)
						continue;
				}
				hy = "IF0000";
			}
			Map row1 = new LinkedHashMap();
			row1.put("3", accout);
			row1.put("6", hy);
			Iterator<String> its;
			if (strhy.startsWith("T")) {
				row1.put("17", Integer.valueOf(0));
				row1.put("18", nowisusesum);
				row1.put("23", nowbzjsum);
				row1.put("24", Integer.valueOf(0));
				row1.put("7", Integer.valueOf(0));

				UFDouble mcc = new UFDouble(0.0D);
				for (String str : mccMap.keySet()) {
					if (str.substring(0, 1).contains(hy.substring(0, 1))) {
						mcc = mcc.add(new UFDouble(mccMap.get(str).toString()));
					}
				}
				UFDouble maicc = new UFDouble(0.0D);
				for (its = maiccMap.keySet().iterator(); its.hasNext();) {
					String str = (String) its.next();
					if (str.substring(0, 1).contains(hy.substring(0, 1))) {
						maicc = maicc.add(new UFDouble(maiccMap.get(str)
								.toString()));
					}
				}
				row1.put("29", mcc.multiply(10000.0D));
				row1.put("30", maicc.multiply(10000.0D));
			} else if (strhy.startsWith("IC")) {
				row1.put("7", nowcqsum);

				row1.put("18", Integer.valueOf(0));
				row1.put("23", Integer.valueOf(0));

				UFDouble mcc = new UFDouble(0.0D);
				for (String str : mccMap.keySet()) {
					if (str.substring(0, 1).contains(hy.substring(0, 1))
							&& str.startsWith("IC")) {
						mcc = mcc.add(new UFDouble(mccMap.get(str).toString()));
					}
				}
				UFDouble maicc = new UFDouble(0.0D);
				for (its = maiccMap.keySet().iterator(); its.hasNext();) {
					String str = (String) its.next();
					if (str.substring(0, 1).contains(hy.substring(0, 1))
							&& str.startsWith("IC")) {
						maicc = maicc.add(new UFDouble(maiccMap.get(str)
								.toString()));
					}
				}
				row1.put("29", mcc.multiply(200.0D));
				row1.put("30", maicc.multiply(200.0D));
			} else if (strhy.startsWith("IH")) {
				row1.put("7", nowcqsum);

				row1.put("18", Integer.valueOf(0));
				row1.put("23", Integer.valueOf(0));

				UFDouble mcc = new UFDouble(0.0D);
				for (String str : mccMap.keySet()) {
					if (str.substring(0, 1).contains(hy.substring(0, 1))
							&& str.startsWith("IH")) {
						mcc = mcc.add(new UFDouble(mccMap.get(str).toString()));
					}
				}
				UFDouble maicc = new UFDouble(0.0D);
				for (its = maiccMap.keySet().iterator(); its.hasNext();) {
					String str = (String) its.next();
					if (str.substring(0, 1).contains(hy.substring(0, 1))
							&& str.startsWith("IH")) {
						maicc = maicc.add(new UFDouble(maiccMap.get(str)
								.toString()));
					}
				}
				row1.put("29", mcc.multiply(300.0D));
				row1.put("30", maicc.multiply(300.0D));
			} else {
				row1.put("7", nowcqsum);

				row1.put("18", Integer.valueOf(0));
				row1.put("23", Integer.valueOf(0));

				UFDouble mcc = new UFDouble(0.0D);
				for (String str : mccMap.keySet()) {
					if (str.substring(0, 1).contains(hy.substring(0, 1))
							&& !str.startsWith("IC") && !str.startsWith("IH")) {
						mcc = mcc.add(new UFDouble(mccMap.get(str).toString()));
					}
				}
				UFDouble maicc = new UFDouble(0.0D);
				for (its = maiccMap.keySet().iterator(); its.hasNext();) {
					String str = (String) its.next();
					if (str.substring(0, 1).contains(hy.substring(0, 1))
							&& !str.startsWith("IC") && !str.startsWith("IH")) {
						maicc = maicc.add(new UFDouble(maiccMap.get(str)
								.toString()));
					}
				}
				row1.put("29", mcc.multiply(300.0D));
				row1.put("30", maicc.multiply(300.0D));
			}
			if (srjeMap.get(strhy) != null) {
				row1.put("17", srjeMap.get(strhy));
			} else {
				row1.put("17", Integer.valueOf(0));
			}
			if (srbzjMap.get(strhy) != null) {
				row1.put("24", srbzjMap.get(strhy));
			} else {
				row1.put("24", Integer.valueOf(0));
			}
			if (strhy.startsWith("IC")) {
				if (pcykMap.get(strhy.substring(0, 2) + "PC") == null) {
					row1.put("20", Integer.valueOf(0));
				} else {
					row1.put("20", pcykMap.get(strhy.substring(0, 2) + "PC"));
				}
			} else if (strhy.startsWith("IH")) {
				if (pcykMap.get(strhy.substring(0, 2) + "PC") == null) {
					row1.put("20", Integer.valueOf(0));
				} else {
					row1.put("20", pcykMap.get(strhy.substring(0, 2) + "PC"));
				}
			} else {
				if (pcykMap.get(strhy.substring(0, 1) + "PC") == null) {
					row1.put("20", Integer.valueOf(0));
				} else {
					row1.put("20", pcykMap.get(strhy.substring(0, 1) + "PC"));
				}
			}
			if (srfkykMap.get(strhy) != null) {
				row1.put("21", srfkykMap.get(strhy));
			} else {
				row1.put("21", Integer.valueOf(0));
			}
			UFDouble fdyk = new UFDouble();
			for (String str : fdykMap.keySet()) {
				if (str.substring(0, 1).contains(hy.substring(0, 1))
						&& !str.contains("IC") && !str.contains("IH")
						&& !hy.contains("IC") && !hy.contains("IH")) {
					fdyk = fdyk.add(new UFDouble(fdykMap.get(str).toString()));
					continue;
				} else if (str.substring(0, 1).contains(hy.substring(0, 1))
						&& str.contains("IC") && hy.contains("IC")) {
					fdyk = fdyk.add(new UFDouble(fdykMap.get(str).toString()));
					continue;
				} else if (str.substring(0, 1).contains(hy.substring(0, 1))
						&& str.contains("IH") && hy.contains("IH")) {
					fdyk = fdyk.add(new UFDouble(fdykMap.get(str).toString()));
					continue;
				}
			}
			row1.put("22", fdyk);
			//交易保证金
			UFDouble jybzj = new UFDouble();
			for (String str : jybzjMap.keySet()) {
				if (str.substring(0, 1).contains(hy.substring(0, 1))
						&& !str.contains("IC") && !str.contains("IH")
						&& !hy.contains("IC") && !hy.contains("IH")) {
					jybzj = jybzj.add(new UFDouble(jybzjMap.get(str).toString()));
					continue;
				} else if (str.substring(0, 1).contains(hy.substring(0, 1))
						&& str.contains("IC") && hy.contains("IC")) {
					jybzj = jybzj.add(new UFDouble(jybzjMap.get(str).toString()));
					continue;
				} else if (str.substring(0, 1).contains(hy.substring(0, 1))
						&& str.contains("IH") && hy.contains("IH")) {
					jybzj = jybzj.add(new UFDouble(jybzjMap.get(str).toString()));
					continue;
				}
			}
			row1.put("23", jybzj);
			
			// 取入金和出金
			row1.put("31", UFDouble.ZERO_DBL);
			row1.put("32", UFDouble.ZERO_DBL);
			UFDouble gole = new UFDouble(rgole.toString());
			if (gole.compareTo(new UFDouble(0)) > 0) {
				row1.put("31", rgole);
			} else {
				row1.put("32", rgole);
			}

			if (strhy.startsWith("IC")) {
				// 中金锁申报费
				Double sbf = 0.0;
				if (sbfobj != null) {
					sbf = new Double(sbfobj.toString());
					sbf = Math.abs(sbf);
				}
				if (sxfkMap.get(strhy.substring(0, 2) + "SX") == null) {
					row1.put("26", Integer.valueOf(0) + sbf);
				} else {
					row1.put("26",
							new Double(sxfkMap
									.get(strhy.substring(0, 2) + "SX")
									.toString())
									+ sbf);
				}
			} else if (strhy.startsWith("IH")) {
				if (sxfkMap.get(strhy.substring(0, 2) + "SX") == null) {
					row1.put("26", Integer.valueOf(0));
				} else {
					row1.put("26", sxfkMap.get(strhy.substring(0, 2) + "SX"));
				}
			} else {
				if (sxfkMap.get(strhy.substring(0, 1) + "SX") == null) {
					row1.put("26", Integer.valueOf(0));
				} else {
					row1.put("26", sxfkMap.get(strhy.substring(0, 1) + "SX"));
				}
			}

			// if (sxfkMap.get(strhy.substring(0, 1) + "SX") == null) {
			// row1.put("26", Integer.valueOf(0));
			// } else {
			// row1.put("26", sxfkMap.get(strhy.substring(0, 1) + "SX"));
			// }
			row1.put("28", date);
			exportData.add(row1);
		}
		return exportData;
	}

	public IUAPQueryBS getItf() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	public void orderByRowAndCol(String[] sheet) {
		for (int m = 0; m < sheet.length; m++) {
			String temps = "";
			for (int j = sheet.length - 1; j > m; j--) {
				if (getRowCol(sheet[m]).intValue() > getRowCol(sheet[j])
						.intValue()) {
					temps = sheet[m];

					sheet[m] = sheet[j];

					sheet[j] = temps;
				}
			}
		}
	}

	public Integer getRowCol(String temps) {
		Integer num = null;

		String[] splits = temps.split("sheet");
		String[] splitr = splits[1].split("row");
		if (Integer.parseInt(splitr[1].substring(0, splitr[1].length() - 3)) < 10) {
			num = new Integer(splitr[0] + "0"
					+ splitr[1].substring(0, splitr[1].length() - 3));
		} else {
			num = new Integer(splitr[0]
					+ splitr[1].substring(0, splitr[1].length() - 3));
		}
		return num;
	}

	public LinkedHashMap getColNames(int type) {
		LinkedHashMap map = new LinkedHashMap();
		if (type == 0) {
			for (int i = 0; i < this.zj.length; i++) {
				map.put(Integer.valueOf(i + 1), this.zj[i]);
			}
		} else {
			for (int i = 0; i < this.hyjz.length; i++) {
				map.put(Integer.valueOf(i + 1), this.hyjz[i]);
			}
		}
		return map;
	}
}
