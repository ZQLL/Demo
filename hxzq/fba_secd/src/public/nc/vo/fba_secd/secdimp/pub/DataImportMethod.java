package nc.vo.fba_secd.secdimp.pub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fba_secd.secdimp.pub.IDataImportBusiService;
import nc.itf.fba_secd.secdimp.pub.IDataImportQueryService;
import nc.itf.fba_secd.secdimp.pub.IDataSpecialDeal;
import nc.itf.fba_secd.secdimp.pub.IPubFileService;
import nc.itf.uap.IUAPQueryBS;
import nc.vo.fba_secd.secdimp.dataimport.AggDataImportVO;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO1;
import nc.vo.fba_secd.secdimp.dataimport.DataImportBVO2;
import nc.vo.fba_secd.secdimp.dataimport.DataImportVO;
import nc.vo.fba_secd.secdimp.dataimport.FieldVO;
import nc.vo.fba_secd.secdimp.datasourceset.DataSourceSetVO;
import nc.vo.fba_secd.secdimp.filetool.CSVUtil;
import nc.vo.fba_secd.secdimp.filetool.ExcelUtil;
import nc.vo.fba_secd.secdimp.filetool.StringHdlUtil;
import nc.vo.fba_secd.secdimp.importcond.ImportCondVO;
import nc.vo.fba_secd.secdimp.importfileset.ImportFileSetVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import java.util.*;

/**
 * 
 * 外部数据导入调用的方法
 * 
 * @author liangwei
 * @date 2014-3-26 上午9:57:38
 * @version 1.0.0
 */
public class DataImportMethod {

	IDataImportBusiService dataImportBS = (IDataImportBusiService) NCLocator
			.getInstance().lookup(IDataImportBusiService.class.getName());

	private static DataImportMethod dataImportMethod;

	public static DataImportMethod getInstance() {
		if (dataImportMethod == null) {
			dataImportMethod = new DataImportMethod();
		}
		return dataImportMethod;
	}

	/**
	 * 根据导入界面配置信息，将对照信息进行配对
	 * 
	 * @param vo
	 * @return Hashtable
	 */
	public Map<String, Object> getConstInfoByDataImportVO(
			AggDataImportVO aggDataImportVO) {
		// 表体选择字段对照关系
		Map<String, Map<String, String>> ht_aim_sd = new HashMap<String, Map<String, String>>();// key:目标字段,value:源字段
		Map<String, Map<String, String>> ht_sd_aim = new HashMap<String, Map<String, String>>();// key:源字段,value:目标字段
		Map<String, List<String>> sd_field = new HashMap<String, List<String>>();// 源字段
		Map<String, List<String>> aim_field = new HashMap<String, List<String>>();// 目标字段
		Map<String, List<String>> sd_field_cn = new HashMap<String, List<String>>();// 源字段字段中文名称=相应目标字段中文名称
		String aimfield;
		String sdfield;
		String sdfield_cn;
		DataImportBVO1[] dataImportBVO1 = (DataImportBVO1[]) aggDataImportVO
				.getChildren(DataImportBVO1.class);
		DataImportBVO2[] dataImportBVO2 = (DataImportBVO2[]) aggDataImportVO
				.getChildren(DataImportBVO2.class);
		if (null != dataImportBVO1 && dataImportBVO1.length > 0) {
			Map<String, String> aim_sd_head = new HashMap<String, String>();
			Map<String, String> sd_aim_head = new HashMap<String, String>();
			List<String> sd_field_cn_head = new ArrayList<String>();
			List<String> sd_field_head = new ArrayList<String>();
			List<String> aim_field_head = new ArrayList<String>();
			for (int i = 0; i < dataImportBVO1.length; i++) {
				Object aim_fieldObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.AIM_FIELD);
				Object ds_fieldObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DS_FIELD);
				Object is_nullObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IF_NULL);
				Object dataType = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DATATYPE);
				Object aim_field_cnObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.AIM_FIELD_CN);
				Object expsystem = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DEF_EXPSYS);
				Object if_ref = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IF_REF);
				Object refVal = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DEF_PKBDINFO);
				Object if_formular = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IF_FORMULAR);
				Object impFormular = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IMPFORMULAR);
				if ("Y".equals(if_ref.toString())) {
					if (expsystem == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString() + "没有配置外系统，请确认");
					}
					if (refVal == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString() + "没有配置参照值，请确认");
					}
				}
				if ("Y".equals(if_formular.toString())) {
					if (impFormular == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString()
								+ "是否公式打勾，但没有设置公式，请确认");
					}
				}
				if ("N".equals(is_nullObj.toString())) {
					if (ds_fieldObj == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString()
								+ "导入文件字段没有配置，请确认");
					}
				}
				if (aim_fieldObj == null || ds_fieldObj == null) {
					continue;
				}
				if (dataType == null) {
					throw new RuntimeException(aim_field_cnObj.toString()
							+ ":没有设置数据类型，请确认");
				}
				aimfield = aim_fieldObj.toString();
				sdfield = ds_fieldObj.toString();
				sdfield_cn = aim_field_cnObj.toString();

				sd_field_head.add(sdfield);
				aim_field_head.add(aimfield);
				// 如果没有中文名称，则显示英文字段名
				if (sdfield_cn == null || "".equals(sdfield_cn.trim()))
					sd_field_cn_head.add(sdfield);
				else
					sd_field_cn_head.add(sdfield_cn);
				aim_sd_head.put(aimfield, sdfield);
				sd_aim_head.put(sdfield, aimfield);
			}
			ht_aim_sd.put(SystemConst.HEAD, aim_sd_head);
			ht_sd_aim.put(SystemConst.HEAD, sd_aim_head);
			sd_field_cn.put(SystemConst.HEAD, sd_field_cn_head);
			sd_field.put(SystemConst.HEAD, sd_field_head);
			aim_field.put(SystemConst.HEAD, aim_field_head);
		}
		if (null != dataImportBVO2 && dataImportBVO2.length > 0) {
			Map<String, String> aim_sd_body = new HashMap<String, String>();
			Map<String, String> sd_aim_body = new HashMap<String, String>();
			List<String> sd_field_cn_body = new ArrayList<String>();
			List<String> sd_field_body = new ArrayList<String>();
			List<String> aim_field_body = new ArrayList<String>();
			for (int i = 0; i < dataImportBVO2.length; i++) {
				Object aim_fieldObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO2.AIM_FIELD);
				Object ds_fieldObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO2.DS_FIELD);
				Object aim_field_cnObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO2.AIM_FIELD_CN);
				Object is_nullObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO2.IF_NULL);
				Object dataType = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO2.DATATYPE);
				if ("N".equals(is_nullObj.toString())) {
					if (ds_fieldObj == null) {
						throw new RuntimeException(
								aim_field_cnObj != null ? aim_field_cnObj
										.toString()
										: "" + ":" + aim_fieldObj != null ? aim_fieldObj
												.toString() : ""
												+ "导入文件不允许为空字段没有配置，请确认");
					}
				}
				if (aim_fieldObj == null || ds_fieldObj == null) {
					continue;
				}
				if (dataType == null) {
					throw new RuntimeException(aim_field_cnObj.toString()
							+ ":没有设置数据类型，请确认");
				}
				aimfield = aim_fieldObj.toString();
				sdfield = ds_fieldObj.toString();
				sdfield_cn = aim_field_cnObj.toString();
				sd_field_body.add(sdfield);
				aim_field_body.add(aimfield);
				// 如果没有中文名称，则显示英文字段名
				if (sdfield_cn == null || sdfield_cn.trim().equals(""))
					sd_field_cn_body.add(sdfield);
				else
					sd_field_cn_body.add(sdfield_cn);
				aim_sd_body.put(aimfield, sdfield);
				sd_aim_body.put(sdfield, aimfield);
			}

			ht_aim_sd.put(SystemConst.BODY, aim_sd_body);
			ht_sd_aim.put(SystemConst.BODY, sd_aim_body);
			sd_field_cn.put(SystemConst.BODY, sd_field_cn_body);
			sd_field.put(SystemConst.BODY, sd_field_body);
			aim_field.put(SystemConst.BODY, aim_field_body);
		}

		Map<String, Object> hm_info = new HashMap<String, Object>();
		hm_info.put(SystemConst.HT_AIM_SD, ht_aim_sd);
		hm_info.put(SystemConst.HT_SD_AIM, ht_sd_aim);
		hm_info.put(SystemConst.AIM_FIELDS, aim_field);
		hm_info.put(SystemConst.SD_FIELDS, sd_field);
		hm_info.put(SystemConst.SD_FIELDS_CN, sd_field_cn);
		return hm_info;
	}

	public Map<String, Object> getConstInfoByDataImportVOHead(
			AggDataImportVO aggDataImportVO) {
		// 表体选择字段对照关系
		Map<String, Map<String, String>> ht_aim_sd = new HashMap<String, Map<String, String>>();// key:目标字段,value:源字段
		Map<String, Map<String, String>> ht_sd_aim = new HashMap<String, Map<String, String>>();// key:源字段,value:目标字段
		Map<String, List<String>> sd_field = new HashMap<String, List<String>>();// 源字段
		Map<String, List<String>> aim_field = new HashMap<String, List<String>>();// 目标字段
		Map<String, List<String>> sd_field_cn = new HashMap<String, List<String>>();// 源字段字段中文名称=相应目标字段中文名称
		String aimfield;
		String sdfield;
		String sdfield_cn;
		DataImportBVO1[] dataImportBVO1 = (DataImportBVO1[]) aggDataImportVO
				.getChildren(DataImportBVO1.class);
		DataImportBVO2[] dataImportBVO2 = (DataImportBVO2[]) aggDataImportVO
				.getChildren(DataImportBVO2.class);
		if (null != dataImportBVO1 && dataImportBVO1.length > 0) {
			Map<String, String> aim_sd_head = new HashMap<String, String>();
			Map<String, String> sd_aim_head = new HashMap<String, String>();
			List<String> sd_field_cn_head = new ArrayList<String>();
			List<String> sd_field_head = new ArrayList<String>();
			List<String> aim_field_head = new ArrayList<String>();
			for (int i = 0; i < dataImportBVO1.length; i++) {
				Object aim_fieldObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.AIM_FIELD);
				Object ds_fieldObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DS_FIELD);
				Object is_nullObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IF_NULL);
				Object dataType = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DATATYPE);
				Object aim_field_cnObj = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.AIM_FIELD_CN);
				Object expsystem = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DEF_EXPSYS);
				Object if_ref = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IF_REF);
				Object refVal = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.DEF_PKBDINFO);
				Object if_formular = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IF_FORMULAR);
				Object impFormular = dataImportBVO1[i]
						.getAttributeValue(DataImportBVO1.IMPFORMULAR);
				if ("Y".equals(if_ref.toString())) {
					if (expsystem == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString() + "没有配置外系统，请确认");
					}
					if (refVal == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString() + "没有配置参照值，请确认");
					}
				}
				if ("Y".equals(if_formular.toString())) {
					if (impFormular == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString()
								+ "是否公式打勾，但没有设置公式，请确认");
					}
				}
				if ("N".equals(is_nullObj.toString())) {
					if (ds_fieldObj == null) {
						throw new RuntimeException(aim_field_cnObj.toString()
								+ ":" + aim_fieldObj.toString()
								+ "导入文件字段没有配置，请确认");
					}
				}
				if (aim_fieldObj == null || ds_fieldObj == null) {
					continue;
				}
				if (dataType == null) {
					throw new RuntimeException(aim_field_cnObj.toString()
							+ ":没有设置数据类型，请确认");
				}
				aimfield = aim_fieldObj.toString();
				sdfield = ds_fieldObj.toString();
				sdfield_cn = aim_field_cnObj.toString();

				sd_field_head.add(sdfield);
				aim_field_head.add(aimfield);
				// 如果没有中文名称，则显示英文字段名
				if (sdfield_cn == null || "".equals(sdfield_cn.trim()))
					sd_field_cn_head.add(sdfield);
				else
					sd_field_cn_head.add(sdfield_cn);
				aim_sd_head.put(aimfield, sdfield);
				sd_aim_head.put(sdfield, aimfield);
			}
			ht_aim_sd.put(SystemConst.HEAD, aim_sd_head);
			ht_sd_aim.put(SystemConst.HEAD, sd_aim_head);
			sd_field_cn.put(SystemConst.HEAD, sd_field_cn_head);
			sd_field.put(SystemConst.HEAD, sd_field_head);
			aim_field.put(SystemConst.HEAD, aim_field_head);
		}
		if (null != dataImportBVO2 && dataImportBVO2.length > 0) {
			Map<String, String> aim_sd_body = new HashMap<String, String>();
			Map<String, String> sd_aim_body = new HashMap<String, String>();
			List<String> sd_field_cn_body = new ArrayList<String>();
			List<String> sd_field_body = new ArrayList<String>();
			List<String> aim_field_body = new ArrayList<String>();
			for (int i = 0; i < dataImportBVO2.length; i++) {
				Object aim_fieldObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO1.AIM_FIELD);
				Object ds_fieldObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO1.DS_FIELD);
				Object aim_field_cnObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO1.AIM_FIELD_CN);
				Object is_nullObj = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO1.IF_NULL);
				Object dataType = dataImportBVO2[i]
						.getAttributeValue(DataImportBVO1.DATATYPE);
				if ("N".equals(is_nullObj.toString())) {
					if (ds_fieldObj == null) {
						throw new RuntimeException(
								aim_field_cnObj != null ? aim_field_cnObj
										.toString()
										: "" + ":" + aim_fieldObj != null ? aim_fieldObj
												.toString() : ""
												+ "导入文件不允许为空字段没有配置，请确认");
					}
				}
				if (aim_fieldObj == null || ds_fieldObj == null) {
					continue;
				}
				if (dataType == null) {
					throw new RuntimeException(aim_field_cnObj.toString()
							+ ":没有设置数据类型，请确认");
				}
				aimfield = aim_fieldObj.toString();
				sdfield = ds_fieldObj.toString();
				sdfield_cn = aim_field_cnObj.toString();
				sd_field_body.add(sdfield);
				aim_field_body.add(aimfield);
				// 如果没有中文名称，则显示英文字段名
				if (sdfield_cn == null || sdfield_cn.trim().equals(""))
					sd_field_cn_body.add(sdfield);
				else
					sd_field_cn_body.add(sdfield_cn);
				aim_sd_body.put(aimfield, sdfield);
				sd_aim_body.put(sdfield, aimfield);
			}

			ht_aim_sd.put(SystemConst.BODY, aim_sd_body);
			ht_sd_aim.put(SystemConst.BODY, sd_aim_body);
			sd_field_cn.put(SystemConst.BODY, sd_field_cn_body);
			sd_field.put(SystemConst.BODY, sd_field_body);
			aim_field.put(SystemConst.BODY, aim_field_body);
		}

		Map<String, Object> hm_info = new HashMap<String, Object>();
		hm_info.put(SystemConst.HT_AIM_SD, ht_aim_sd);
		hm_info.put(SystemConst.HT_SD_AIM, ht_sd_aim);
		hm_info.put(SystemConst.AIM_FIELDS, aim_field);
		hm_info.put(SystemConst.SD_FIELDS, sd_field);
		hm_info.put(SystemConst.SD_FIELDS_CN, sd_field_cn);
		return hm_info;
	}

	/**
	 * 根据数据源（中间表）模式导入数据
	 * 
	 * @param setvo
	 * @param isserver
	 * @param metaVO
	 * @param wheresql
	 * @param sd_fields
	 * @param aim_fields
	 * @param ht_aim_sd
	 * @param ht_sd_aim
	 * @throws Exception
	 *             void TODO（参数说明）
	 */
	public void getImportDataByDS(DataSourceSetVO setvo, boolean isserver,
			AggDataImportVO aggDataImportVO, String wheresql, Map hm_info)
			throws Exception {
		if (null == aggDataImportVO)
			return;
		// 如果考虑性能问题，这里实际可以根据特殊处理类getQueryCond方法是否有值类判断,目前先直接=true
		boolean isTableVoQuery = true;
		if (isserver) {
			IDataImportQueryService service = (IDataImportQueryService) NCLocator
					.getInstance().lookup(
							IDataImportQueryService.class.getName());
			// 如果是服务器端，直接从后台得到数据，并将数据在后台缓存
			service.getDataBySql(setvo, aggDataImportVO, isTableVoQuery,
					hm_info);
		} else {
			// 如果是客户端，需要先取到数据，再传到后台，并将数据在后台缓存
			getDataBySql(setvo, aggDataImportVO, isTableVoQuery, hm_info);
		}
	}

	/**
	 * 根据JDBC/ODBC数据源表名，得到该表的字段类型和长度
	 */
	private List<FieldVO> getTableVO(Connection con, DataSourceSetVO setvo,
			String tablename, String[] fields) throws Exception {
		List<String> v = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			v.add(fields[i].toLowerCase());
		}

		List<FieldVO> v_vos = new ArrayList<FieldVO>();
		DatabaseMetaData metadata = con.getMetaData();
		String catalog = con.getCatalog();
		String schemaPattern = PubMethod.getSchema(setvo.getDstype(),
				setvo.getDsuser());
		String tableNamePattern = tablename;// JDBC/ODBC数据源表名
		String columnNamePattern = null;
		// 得到表字段属性
		ResultSet rs = metadata.getColumns(catalog, schemaPattern,
				tableNamePattern, columnNamePattern);
		String columnName;
		short dataType;
		int columnSize = -1;
		int precision = -1;
		FieldVO fieldvo;

		ResultSetMetaData dma = rs.getMetaData();
		int count = dma.getColumnCount();
		boolean isJDBC = false;
		boolean isODBC = false;
		for (int i = 0; i < count; i++) {
			String cname = dma.getColumnName(i + 1);
			if (cname.equalsIgnoreCase("COLUMN_SIZE")) {
				isJDBC = true;
			} else if (cname.equalsIgnoreCase("LENGTH")) {
				isODBC = true;
			}
			if (isJDBC || isODBC)
				break;
		}

		while (rs.next()) {
			// 列的名称
			columnName = rs.getString("COLUMN_NAME");
			// SQL type from java.sql.Types
			dataType = rs.getShort("DATA_TYPE");
			// 列的大小
			if (isJDBC) {
				columnSize = rs.getInt("COLUMN_SIZE");// JDBC数据源
				precision = rs.getInt("DECIMAL_DIGITS");// 精度
			} else if (isODBC) {
				columnSize = rs.getInt("LENGTH");// ODBC数据源
				precision = rs.getInt("SCALE");// 精度
			}
			columnName = columnName.toLowerCase();
			if (v.contains(columnName)) {
				fieldvo = new FieldVO();
				fieldvo.setName(columnName);
				fieldvo.setSqlType(dataType);
				fieldvo.setMaxLength(columnSize);
				fieldvo.setPrecision(precision);

				v_vos.add(fieldvo);
			}
		}
		// //2012-06-04 MeiChao add 加入从Excel内取字段属性的代码
		// if (v_vos.size() < 1 && metadata.getDatabaseProductName() != null
		// && "EXCEL".equals(metadata.getDatabaseProductName())) {
		// Statement stmt = con.createStatement();
		// ResultSet rset = stmt.executeQuery("select * from " + tablename);
		// int jjj = rset.getMetaData().getColumnCount();
		// for (int i = 0; i < jjj; i++) {
		// String columnname = rset.getMetaData().getColumnName(i + 1);
		// int datatype = rset.getMetaData().getColumnType(i + 1);
		// int columnsize = rset.getMetaData().getColumnDisplaySize(i + 1);
		// int scale = rset.getMetaData().getScale(i + 1);
		// if (v.contains(columnname.toLowerCase())) {
		// fieldvo = new FieldVO();
		// fieldvo.setName(columnname);
		// fieldvo.setSqlType(datatype);
		// fieldvo.setMaxLength(columnsize);
		// fieldvo.setPrecision(((datatype == 6 || datatype == 7 || datatype ==
		// 8) && scale < 1) ? 8
		// : scale);
		//
		// v_vos.add(fieldvo);
		// }
		// }
		//
		// }
		return v_vos;
	}

	/**
	 * 解析csv文件信息为主子表
	 * 
	 * @author ZQ
	 * @param metaVO
	 * @param filepath
	 * @param hm_info
	 * @throws Exception
	 */

	public void getDataByFilePathHead(AggDataImportVO aggDataImportVO,
			String filepath, Map hm_info) throws Exception {
		DataImportVO parentVO = (DataImportVO) aggDataImportVO.getParent();
		String relevaFieldName = (String) parentVO
				.getAttributeValue(DataImportVO.RELEVAFIELDNAME);
		Map<String, List> map_v = new HashMap<String, List>();
		List<String> row_v = null;
		List<String> row_b = null;

		try {
			File f = new File(filepath);
			File[] files = f.listFiles();
			if (files == null || files.length == 0) {
				throw new Exception("导入文件为空！或者 导入文件存在问题");
			}
			List<List<String>> head_data_v = new ArrayList<List<String>>();// 文件数据
			List<List<String>> body_data_v = new ArrayList<List<String>>();// 文件数据
			Map<String, Integer> headtitmap = new HashMap<String, Integer>();// 文件头字段名
			Map<String, Integer> bodytitmap = new HashMap<String, Integer>();// 文件头字段名
			int firstRow = 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					continue;
				}

				if (files[i].getName().toLowerCase().endsWith(".csv")) {
					List<List<String>> vRow = CSVUtil.readCSV(files[i]);
					Map<String, Integer> ht = CSVUtil.createHeadKey(vRow);
					int iRow = vRow.size();
					for (int j = 1; j < iRow; j++) {
						row_v = new ArrayList<String>();
						row_b = new ArrayList<String>();
						List<String> vCell = vRow.get(j);
						Map<String, List<String>> sd_fields_map = (Map<String, List<String>>) hm_info
								.get(SystemConst.SD_FIELDS);
						List<String> sd_field_lst = sd_fields_map
								.get(SystemConst.HEAD);// CSV仅支持单表，所以仅取HEAD.
						String[] sd_fields = sd_field_lst
								.toArray(new String[] {});
						int length = sd_fields.length;
						int now = 0;
						for (int n = 0; n < length; n++) {
							String[] temp = sd_fields[n].split("\\"
									+ SystemConst.perfix);
							for (int m = 0; m < temp.length; m++) {
								String fieldm = temp[m];
								if (!temp[0].equals("提前还款比例")
										&& !temp[0].equals("a")) {
									row_v.add(CSVUtil
											.getData(fieldm, ht, vCell));
								}
								// 处理文件头字段名字，当出现下面字段，赋值到子表中
								if (temp[0].equals("起息日")) {
									row_b.add(CSVUtil
											.getData(fieldm, ht, vCell));
									if (j == 1 && firstRow == 0) {
										String fieldmVal = StringHdlUtil
												.handlerKey(fieldm);
										bodytitmap.put(fieldmVal + 1,
												bodytitmap.size());
									}
								} else if (temp[0].equals("到期日")) {
									row_b.add(CSVUtil
											.getData(fieldm, ht, vCell));
									if (j == 1 && firstRow == 0) {
										String fieldmVal = StringHdlUtil
												.handlerKey(fieldm);
										bodytitmap.put(fieldmVal + 3,
												bodytitmap.size());
									}
								} else if (temp[0].equals("票面利率")) {
									row_b.add(CSVUtil
											.getData(fieldm, ht, vCell));
									if (j == 1 && firstRow == 0) {
										String fieldmVal = StringHdlUtil
												.handlerKey(fieldm);
										bodytitmap.put(fieldmVal + 2,
												bodytitmap.size());
									}
								} else if (temp[0].equals("提前还款比例")) {
									row_b.add(CSVUtil
											.getData(fieldm, ht, vCell));
									if (j == 1 && firstRow == 0) {
										String fieldmVal = StringHdlUtil
												.handlerKey(fieldm);
										bodytitmap.put(fieldmVal + 0,
												bodytitmap.size());
									}
								} else if (temp[0].equals("a")) {
									continue;
								}

								if (j == 1 && firstRow == 0
										&& !temp[0].equals("提前还款比例")) {
									String fieldmVal = StringHdlUtil
											.handlerKey(fieldm);
									headtitmap.put(fieldmVal + (now - 1),
											headtitmap.size());
								}
								now++;
							}
						}
						head_data_v.add(row_v);
						body_data_v.add(row_b);
					}
				} else if (files[i].getName().toLowerCase().endsWith(".xls")) {// excel导入
					Map<String, List<List<List<String>>>> sheetMap = ExcelUtil
							.readExcel(files[i].getPath(), 2);
					for (String key : sheetMap.keySet()) {
						List<List<List<String>>> vSheet = sheetMap.get(key);
						if (vSheet == null || vSheet.size() == 0) {
							continue;
						}
						boolean isReleFld = false;
						List<List<String>> vRow = vSheet.get(0);
						if (vRow == null || vRow.size() == 0) {
							continue;
						}
						Map<String, Integer> ht = ExcelUtil.createHeadKey(vRow);
						int iRow = vRow.size();
						Map<String, Integer> map = new HashMap<String, Integer>();
						for (int j = 1; j < iRow; j++) {
							row_v = new ArrayList<String>();
							List<String> vCell = vRow.get(j);
							// 待处理，hm_info存储结构已发生变革.需根据sheet信息动太取HEAD与BODY。
							Map<String, List<String>> sd_fields_map = (Map<String, List<String>>) hm_info
									.get(SystemConst.SD_FIELDS);
							List<String> sd_field_lst = null;
							sd_field_lst = sd_fields_map.get(key);
							String[] sd_fields = sd_field_lst
									.toArray(new String[] {});
							if (sd_fields == null || sd_fields.length == 0) {
								throw new BusinessException(
										"导入文件出现异常，请确认文件是否正确。");
							}
							int length = sd_fields.length;
							for (int n = 0; n < length; n++) {
								String[] temp = sd_fields[n].split("\\"
										+ SystemConst.perfix);
								for (int m = 0; m < temp.length; m++) {
									if (relevaFieldName != null
											&& relevaFieldName.equals(temp[m])) {
										isReleFld = true;
									}

									Object dataObj = ExcelUtil.getData(temp[m],
											ht, vCell);

									if (dataObj != null) {
										row_v.add(dataObj.toString());
									} else {
										row_v.add("");
									}
									// row_v.add((String)
									// ExcelUtil.getData(temp[m], ht, vCell));
									// 处理文件头字段名字
									if (j == 1 && firstRow == 0) {
										String fieldmVal = StringHdlUtil
												.handlerKey(temp[m]);

										if (SystemConst.HEAD.equals(key)) {
											headtitmap.put(fieldmVal + n,
													headtitmap.size());
										} else {
											bodytitmap.put(fieldmVal + n,
													bodytitmap.size());
										}
									}
								}
							}
							if (!isReleFld && relevaFieldName != null) {// 说明没有在字段对照中配置，因为主子表中关联字段在后台需要设置.所以将字段加到titmap中
								row_v.add((String) ExcelUtil.getData(
										relevaFieldName, ht, vCell));
								if (j == 1 && firstRow == 0) {
									if (SystemConst.HEAD.equals(key)) {
										headtitmap.put(
												relevaFieldName + length,
												headtitmap.size());
									} else {
										bodytitmap.put(
												relevaFieldName + length,
												bodytitmap.size());
									}
								}
							}
							if (SystemConst.HEAD.equals(key)) {
								head_data_v.add(row_v);
							} else {
								body_data_v.add(row_v);
							}
						}

					}
				}
				if (head_data_v != null && head_data_v.size() > 0) {
					List<Object> result = new ArrayList<Object>();
					result.add(0, head_data_v);
					result.add(1, null);// 文件导入时，无需过滤，所以无需表结构
					result.add(2, headtitmap);
					map_v.put(SystemConst.HEAD, result);
				}

				if (body_data_v != null && body_data_v.size() > 0) {
					List<Object> bodyResult = new ArrayList<Object>();
					bodyResult.add(0, body_data_v);
					bodyResult.add(1, null);// 文件导入时，无需过滤，所以无需表结构
					bodyResult.add(2, bodytitmap);
					map_v.put(SystemConst.BODY, bodyResult);
				}

				++firstRow;
			}
			// 根据特殊文件处理原始数据
			// dataSpecialDealBySpeFile(metaVO, sd_fields, aim_fields, data_v);

			dataImportBS.setBusiCacheData(aggDataImportVO, map_v, hm_info);
		} catch (Exception e) {
			dataImportBS.clearBusiCache();
			throw e;
		}
	}

	public void getDataByFilePath(AggDataImportVO aggDataImportVO,
			String filepath, Map hm_info) throws Exception {
		DataImportVO parentVO = (DataImportVO) aggDataImportVO.getParent();
		String relevaFieldName = (String) parentVO
				.getAttributeValue(DataImportVO.RELEVAFIELDNAME);
		Map<String, List> map_v = new HashMap<String, List>();
		List<String> row_v = null;

		try {
			File f = new File(filepath);
			File[] files = f.listFiles();
			if (files == null || files.length == 0) {
				throw new Exception("导入文件为空！或者 导入文件存在问题");
			}
			String titleKey = SystemConst.HEAD;
			List<List<String>> head_data_v = new ArrayList<List<String>>();// 文件数据
			List<List<String>> body_data_v = new ArrayList<List<String>>();// 文件数据
			Map<String, Integer> headtitmap = new HashMap<String, Integer>();// 文件头字段名
			Map<String, Integer> bodytitmap = new HashMap<String, Integer>();// 文件头字段名
			int firstRow = 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					continue;
				}

				if (files[i].getName().toLowerCase().endsWith(".csv")) {
					List<List<String>> vRow = CSVUtil.readCSV(files[i]);
					Map<String, Integer> ht = CSVUtil.createHeadKey(vRow);
					int iRow = vRow.size();
					for (int j = 1; j < iRow; j++) {
						row_v = new ArrayList<String>();
						List<String> vCell = vRow.get(j);
						Map<String, List<String>> sd_fields_map = (Map<String, List<String>>) hm_info
								.get(SystemConst.SD_FIELDS);
						List<String> sd_field_lst = sd_fields_map
								.get(SystemConst.HEAD);// CSV仅支持单表，所以仅取HEAD.
						String[] sd_fields = sd_field_lst
								.toArray(new String[] {});
						int length = sd_fields.length;
						for (int n = 0; n < length; n++) {
							String[] temp = sd_fields[n].split("\\"
									+ SystemConst.perfix);
							for (int m = 0; m < temp.length; m++) {
								String fieldm = temp[m];
								row_v.add(CSVUtil.getData(fieldm, ht, vCell));
								// 处理文件头字段名字
								if (j == 1 && firstRow == 0) {
									String fieldmVal = StringHdlUtil
											.handlerKey(fieldm);
									headtitmap.put(fieldmVal + n,
											headtitmap.size());
								}
							}
						}
						head_data_v.add(row_v);
					}
				} else if (files[i].getName().toLowerCase().endsWith(".xls")) {// excel导入
					Map<String, List<List<List<String>>>> sheetMap = ExcelUtil
							.readExcel(files[i].getPath(), 2);
					for (String key : sheetMap.keySet()) {
						List<List<List<String>>> vSheet = sheetMap.get(key);
						if (vSheet == null || vSheet.size() == 0) {
							continue;
						}
						boolean isReleFld = false;
						List<List<String>> vRow = vSheet.get(0);
						if (vRow == null || vRow.size() == 0) {
							continue;
						}
						Map<String, Integer> ht = ExcelUtil.createHeadKey(vRow);
						int iRow = vRow.size();
						Map<String, Integer> map = new HashMap<String, Integer>();
						for (int j = 1; j < iRow; j++) {
							row_v = new ArrayList<String>();
							List<String> vCell = vRow.get(j);
							// 待处理，hm_info存储结构已发生变革.需根据sheet信息动太取HEAD与BODY。
							Map<String, List<String>> sd_fields_map = (Map<String, List<String>>) hm_info
									.get(SystemConst.SD_FIELDS);
							List<String> sd_field_lst = null;
							sd_field_lst = sd_fields_map.get(key);
							String[] sd_fields = sd_field_lst
									.toArray(new String[] {});
							if (sd_fields == null || sd_fields.length == 0) {
								throw new BusinessException(
										"导入文件出现异常，请确认文件是否正确。");
							}
							int length = sd_fields.length;
							for (int n = 0; n < length; n++) {
								String[] temp = sd_fields[n].split("\\"
										+ SystemConst.perfix);
								for (int m = 0; m < temp.length; m++) {
									if (relevaFieldName != null
											&& relevaFieldName.equals(temp[m])) {
										isReleFld = true;
									}

									Object dataObj = ExcelUtil.getData(temp[m],
											ht, vCell);

									if (dataObj != null) {
										row_v.add(dataObj.toString());
									} else {
										row_v.add("");
									}
									// row_v.add((String)
									// ExcelUtil.getData(temp[m], ht, vCell));
									// 处理文件头字段名字
									if (j == 1 && firstRow == 0) {
										String fieldmVal = StringHdlUtil
												.handlerKey(temp[m]);

										if (SystemConst.HEAD.equals(key)) {
											headtitmap.put(fieldmVal + n,
													headtitmap.size());
										} else {
											bodytitmap.put(fieldmVal + n,
													bodytitmap.size());
										}
									}
								}
							}
							if (!isReleFld && relevaFieldName != null) {// 说明没有在字段对照中配置，因为主子表中关联字段在后台需要设置.所以将字段加到titmap中
								row_v.add((String) ExcelUtil.getData(
										relevaFieldName, ht, vCell));
								if (j == 1 && firstRow == 0) {
									if (SystemConst.HEAD.equals(key)) {
										headtitmap.put(
												relevaFieldName + length,
												headtitmap.size());
									} else {
										bodytitmap.put(
												relevaFieldName + length,
												bodytitmap.size());
									}
								}
							}
							if (SystemConst.HEAD.equals(key)) {
								head_data_v.add(row_v);
							} else {
								body_data_v.add(row_v);
							}
						}

					}
				}
				if (head_data_v != null && head_data_v.size() > 0) {
					List<Object> result = new ArrayList<Object>();
					result.add(0, head_data_v);
					result.add(1, null);// 文件导入时，无需过滤，所以无需表结构
					result.add(2, headtitmap);
					map_v.put(SystemConst.HEAD, result);
				}

				if (body_data_v != null && body_data_v.size() > 0) {
					List<Object> bodyResult = new ArrayList<Object>();
					bodyResult.add(0, body_data_v);
					bodyResult.add(1, null);// 文件导入时，无需过滤，所以无需表结构
					bodyResult.add(2, bodytitmap);
					map_v.put(SystemConst.BODY, bodyResult);
				}

				++firstRow;
			}
			// 根据特殊文件处理原始数据
			// dataSpecialDealBySpeFile(metaVO, sd_fields, aim_fields, data_v);

			dataImportBS.setBusiCacheData(aggDataImportVO, map_v, hm_info);
		} catch (Exception e) {
			dataImportBS.clearBusiCache();
			throw e;
		}
	}

	/**
	 * CSV文件特殊处理
	 * 
	 * @param vo
	 * @param sd_fields
	 * @param aim_fields
	 * @param data_v
	 */
	private void csvDataSpecialDeal(DataImportVO vo, String[] sd_fields,
			String[] aim_fields, List data_v) {
		if (vo == null)
			return;
		String classname = vo.getSpecialclass();
		String filepath = vo.getSpecialfile();
		if (classname == null)
			return;
		try {
			IDataSpecialDeal idsd = (IDataSpecialDeal) Class.forName(classname)
					.newInstance();
			// 查询条件的特殊处理,如增加参照,日期等等
			if (idsd != null) {
				idsd.specialDealTOCVSFile(filepath, sd_fields, aim_fields,
						data_v);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}

	private void dataSpecialDealBySpeFile(DataImportVO vo, String[] sd_fields,
			String[] aim_fields, List data_v) {
		if (vo == null)
			return;
		String classname = vo.getSpecialclass();
		String filepath = vo.getSpecialfile();
		String tablename = vo.getAim_table();
		if (tablename == null || classname == null || filepath == null)
			return;
		try {
			IDataSpecialDeal idsd = (IDataSpecialDeal) Class.forName(classname)
					.newInstance();
			// 查询条件的特殊处理,如增加参照,日期等等
			if (idsd != null) {
				idsd.specialDealBySpeFile(filepath, sd_fields, aim_fields,
						data_v);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}

	/**
	 * 备份文件
	 * 
	 * @param vo
	 * @param hitMsg
	 * @param isserver
	 * @throws Exception
	 */
	public void bakFile(ImportFileSetVO vo, String hitMsg) throws Exception {
		String filepath = vo.getFilepath();
		String bakfilepath = vo.getBakfilepath();
		UFBoolean isserver = vo.getIsserver();
		if (isserver.booleanValue()) {
			IPubFileService service = (IPubFileService) NCLocator.getInstance()
					.lookup(IPubFileService.class.getName());
			service.bakFile(filepath, bakfilepath);
		} else {
			File fromFile = new File(filepath);
			try {
				copy(fromFile, bakfilepath);
			} catch (Exception e) {
				throw new Exception("数据已经正常导入 " + hitMsg + "但备份文件"
						+ fromFile.getName() + "失败，请确认文件没有打开!");
			}
		}
	}

	/**
	 * 备份文件
	 * 
	 * @param vo
	 * @param isserver
	 * @throws Exception
	 */
	public void bakFile(ImportFileSetVO vo) throws Exception {
		String filepath = vo.getFilepath();
		String bakfilepath = vo.getBakfilepath();
		UFBoolean isserver = vo.getIsserver();
		if (isserver.booleanValue()) {
			IPubFileService service = (IPubFileService) NCLocator.getInstance()
					.lookup(IPubFileService.class.getName());
			service.bakFile(filepath, bakfilepath);
		} else {
			File fromFile = new File(filepath);
			try {
				copy(fromFile, bakfilepath);
			} catch (Exception e) {
				throw new Exception("数据已经正常导入，但备份文件" + fromFile.getName()
						+ "失败，请确认文件没有打开!");
			}
		}
	}

	/**
	 * 客户端 文件复制
	 * 
	 * @param fromFile
	 * @param toFileName
	 * @throws Exception
	 */
	private void copy(File fromFile, String toFileName) throws Exception {
		File[] fs = fromFile.listFiles();
		FileInputStream in;
		FileOutputStream out;
		boolean isdel = true;
		boolean flag = false;
		File file = null;
		for (int i = 0; i < fs.length; i++) {
			String toFile = toFileName + "\\" + fs[i].getName();
			if (fs[i].isDirectory()
					|| (!fs[i].getName().toLowerCase().endsWith(".csv") && !fs[i]
							.getName().toLowerCase().endsWith(".xls"))) {
				continue;
			}
			file = new File(toFileName);
			if (!file.exists()) {// 如果目标文件夹不存在，自动创建
				flag = file.mkdir();
				if (!flag)
					throw new Exception("创建备份文件夹异常");
			}
			file = new File(toFile);
			if (file.exists()) {// 如果备份文件中存在该文件，则将原备份文件增加序号，再备份新的文件
				bakOldFile(file);
			}
			// 复制文件
			in = new FileInputStream(fs[i]);
			out = new FileOutputStream(toFile);
			byte[] context = new byte[1024];
			int length;
			while ((length = in.read(context)) != -1) {
				out.write(context, 0, length);
				out.flush();
			}
			out.close();
			in.close();
			// 删除原文件
			flag = fs[i].delete();
			if (!flag)
				isdel = false;
		}
		if (!isdel)
			throw new Exception("文件删除异常");
	}

	/**
	 * 将文件夹下以文件名开头的文件，按序号重新命名
	 * 
	 * @param file
	 */
	private void bakOldFile(File file) throws Exception {
		int maxvalue = 0;
		int cntvalue = 0;
		int index1 = 0;
		int index2 = 0;
		String filename = file.getPath();
		File parentfile = file.getParentFile();
		File[] filelist = parentfile.listFiles();// 寻找相同的文件，取最大的后缀
		for (int j = 0; j < filelist.length; j++) {
			String bakfilename = filelist[j].getPath();
			if (bakfilename.startsWith(filename)) {
				index1 = bakfilename.indexOf("(");
				index2 = bakfilename.indexOf(")");
				if (index1 > 0 && index2 > 0)
					cntvalue = Integer.parseInt(bakfilename.substring(
							index1 + 1, index2));
				if (cntvalue > maxvalue)
					maxvalue = cntvalue;

			}
		}
		// 将原来备份文件名增加序号
		maxvalue = maxvalue + 1;
		boolean bMoved = file.renameTo(new File(filename + "("
				+ Integer.toString(maxvalue) + ")"));
		if (!bMoved)
			throw new Exception("数据已经正常导入，但备份文件" + filename + "失败，请确认文件没有打开!");
	}

	public Map getImportTitle() throws BusinessException {
		IDataImportBusiService busiService = NCLocator.getInstance().lookup(
				IDataImportBusiService.class);
		return busiService.getImportTitle();
	}

	public Map getImportDataByCache() throws BusinessException {
		IDataImportBusiService busiService = NCLocator.getInstance().lookup(
				IDataImportBusiService.class);
		return busiService.getSuccessBusiData();
	}

	public void getDataBySql(DataSourceSetVO setvo, AggDataImportVO aggVO,
			boolean isTableVoQuery, Map hm_info) throws Exception {
		Map sd_fields = (Map) hm_info.get(SystemConst.SD_FIELDS);
		handlerSdFields(sd_fields, aggVO);
		Connection con = null;
		String querysqlhead = getSqlByType(sd_fields, SystemConst.HEAD, aggVO);// 判断是否主子表,如果主子表,按主子表做二次查询.
		String querysqlbody = getSqlByType(sd_fields, SystemConst.BODY, aggVO);
		try {
			String password = setvo.getPassword();
			// Encode encode = new Encode();
			// password = encode.encode(password.trim());
			con = PubMethod.getInstance().getConnect(setvo.getDriverclass(),
					setvo.getDsurl(), setvo.getDsuser(), password);
			List<Object> headDataLst = getDataByTypeCode(sd_fields, con,
					SystemConst.HEAD, querysqlhead);
			List<Object> bodyDataLst = getDataByTypeCode(sd_fields, con,
					SystemConst.BODY, querysqlbody);
			Map<String, List> map_v = new HashMap<String, List>();
			map_v.put(SystemConst.HEAD, headDataLst);
			map_v.put(SystemConst.BODY, bodyDataLst);
			IDataImportBusiService dS = (IDataImportBusiService) NCLocator
					.getInstance().lookup(
							IDataImportBusiService.class.getName());
			dS.setBusiCacheData(aggVO, map_v, hm_info);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	private void handlerSdFields(Map sd_fields, AggDataImportVO aggVO) {
		Map<String, String> varHeadFldMap = new HashMap<String, String>();// 主表要添加的字段
		Map<String, String> varBodyFldMap = new HashMap<String, String>();// 子表要添加的字段
		DataImportVO parentVO = aggVO.getParentVO();
		String releFld = parentVO.getRelevafieldname();
		if (releFld != null && !"".equals(releFld)) {
			varHeadFldMap.put(releFld, releFld);
			varBodyFldMap.put(releFld, releFld);
		}

		DataImportBVO1[] dataImportBVO1 = (DataImportBVO1[]) aggVO
				.getChildren(DataImportBVO1.class);
		DataImportBVO2[] dataImportBVO2 = (DataImportBVO2[]) aggVO
				.getChildren(DataImportBVO2.class);
		addVarHeadFld(dataImportBVO1, varHeadFldMap);
		addVarBodyFld(dataImportBVO2, varBodyFldMap);

		List<String> headLst = (List<String>) sd_fields.get(SystemConst.HEAD);
		int headSize = headLst.size();
		for (int i = 0; i < headSize; i++) {
			String fld = headLst.get(i);
			if (varHeadFldMap.get(fld) != null) {
				varHeadFldMap.remove(fld);
			}
		}
		Set<String> headKeySet = varHeadFldMap.keySet();
		Iterator<String> headIt = headKeySet.iterator();
		while (headIt.hasNext()) {
			headLst.add(headIt.next());
		}
		if (sd_fields.get(SystemConst.BODY) != null) {
			List<String> bodyLst = (List<String>) sd_fields
					.get(SystemConst.BODY);
			int bodySize = bodyLst.size();
			for (int i = 0; i < bodySize; i++) {
				String fld = bodyLst.get(i);
				if (varBodyFldMap.get(fld) != null) {
					varBodyFldMap.remove(fld);
				}
			}
			Set<String> bodyKeySet = varBodyFldMap.keySet();
			Iterator<String> bodyIt = bodyKeySet.iterator();
			while (bodyIt.hasNext()) {
				bodyLst.add(bodyIt.next());
			}
		}

	}

	private void addVarHeadFld(DataImportBVO1[] dataImportBVO1,
			Map<String, String> varHeadFldMap) {

		if (dataImportBVO1 != null && dataImportBVO1.length > 0) {
			int bvoLen = dataImportBVO1.length;
			for (int i = 0; i < bvoLen; i++) {
				DataImportBVO1 bvo = dataImportBVO1[i];
				if (bvo.getIf_formular().booleanValue()) {
					FormulaParseFather formulaParse = new nc.bs.pub.formulaparse.FormulaParse();
					String impformular = (String) bvo
							.getAttributeValue(DataImportBVO1.IMPFORMULAR);
					if (impformular == null || "".equals(impformular)) {
						throw new RuntimeException(bvo.getAim_field_cn()
								+ " [是否公式]打勾，但您没有设置公式内容，请确认！");
					}
					formulaParse.setExpress(impformular);
					VarryVO[] varrys = formulaParse.getVarryArray();
					if (varrys == null || varrys.length == 0) {
						throw new RuntimeException(impformular + "公式配置不正确请确认");
					}
					for (int j = 0; j < varrys.length; j++) {
						String[] varries = varrys[j].getVarry();
						if (varries != null) {
							for (int k = 0; k < varries.length; k++) {
								varHeadFldMap.put(varries[k], varries[k]);
							}
						}
					}
				}
			}
		}

	}

	private void addVarBodyFld(DataImportBVO2[] dataImportBVO2,
			Map<String, String> varBodyFldMap) {
		if (dataImportBVO2 != null && dataImportBVO2.length > 0) {
			int bvoLen = dataImportBVO2.length;
			for (int i = 0; i < bvoLen; i++) {
				DataImportBVO2 bvo = dataImportBVO2[i];
				if (bvo.getIf_formular().booleanValue()) {
					FormulaParseFather formulaParse = new nc.bs.pub.formulaparse.FormulaParse();
					String impformular = (String) bvo
							.getAttributeValue(DataImportBVO2.IMPFORMULAR);
					formulaParse.setExpress(impformular);
					VarryVO[] varrys = formulaParse.getVarryArray();
					for (int j = 0; j < varrys.length; j++) {
						String[] varries = varrys[j].getVarry();
						if (varries != null) {
							for (int k = 0; k < varries.length; k++) {
								varBodyFldMap.put(varries[k], varries[k]);
							}
						}
					}
				}
			}
		}
	}

	private String getSqlByType(Map sd_fields, String type,
			AggDataImportVO aggVO) throws BusinessException {
		DataImportVO parentVO = aggVO.getParentVO();
		String tableDesc = parentVO.getSd_describe();
		String tableArr[] = tableDesc.split(",");
		if (type.equals(SystemConst.BODY)) {
			if (tableArr.length == 1) {
				return null;
			}
		}
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select ");
		List<String> fldLst = (List<String>) sd_fields.get(type);
		int lstSize = fldLst.size();
		for (int i = 0; i < lstSize; i++) {
			if (i > 0) {
				sqlBuf.append(",");
			}
			sqlBuf.append(fldLst.get(i));
		}
		sqlBuf.append(" from ");

		if (type.equals(SystemConst.HEAD)) {
			sqlBuf.append(tableArr[0]);
		} else {
			sqlBuf.append(tableArr[1]);
		}

		if (type.equals(SystemConst.HEAD)) {
			appendWhereCondByCondSet(sqlBuf, SystemConst.HEAD, parentVO);
		} else {
			appendWhereCondByCondSet(sqlBuf, SystemConst.BODY, parentVO);
		}
		return sqlBuf.toString();
	}

	/**
	 * 根据导入条件设置增加where条件
	 * 
	 * @param sqlBuf
	 * @param tableType
	 * @param parentVO
	 * @throws BusinessException
	 */
	private void appendWhereCondByCondSet(StringBuffer sqlBuf,
			String tableType, DataImportVO parentVO) throws BusinessException {
		String pk_importProj = parentVO.getPk_importproj();
		StringBuffer conBuf = new StringBuffer();
		conBuf.append(" pk_importproj='" + pk_importProj + "'");
		if (SystemConst.HEAD.equals(tableType)) {
			conBuf.append(" and tabletype=1");
		} else {
			conBuf.append(" and tabletype=2");
		}
		conBuf.append("and isnull(dr,0)=0");
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Collection<ImportCondVO> vos = query.retrieveByClause(
				ImportCondVO.class, conBuf.toString());
		if (null != vos && vos.size() > 0) {
			for (ImportCondVO vo : vos) {
				sqlBuf.append(" and " + vo.getWherecond());
			}
		}
	}

	private List<Object> getDataByTypeCode(Map sd_fields, Connection con,
			String typecode, String querysqlstr) throws Exception {
		if (querysqlstr == null || "".equals(querysqlstr)) {
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> return_v = new ArrayList<Object>();
		try {
			stmt = con.prepareStatement(querysqlstr);
			rs = stmt.executeQuery();
			int index = 0;
			List headLst = (List) sd_fields.get(typecode);
			List<List<Object>> data_v = new ArrayList<List<Object>>();
			int fieldcnt = headLst.size();
			while (rs.next()) {
				index = 1;
				Vector<Object> row_v = new Vector<Object>();
				while (index <= fieldcnt) {
					row_v.add(rs.getObject(index++));
				}
				data_v.add(row_v);
			}
			Map titleMap = new HashMap();
			for (int i = 0; i < fieldcnt; i++) {
				titleMap.put(headLst.get(i) + "" + i, i);
			}
			return_v.add(0, data_v);
			return_v.add(1, null);
			return_v.add(2, titleMap);
		} catch (SQLException e) {
			throw new BusinessException(e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return return_v;
	}

}
