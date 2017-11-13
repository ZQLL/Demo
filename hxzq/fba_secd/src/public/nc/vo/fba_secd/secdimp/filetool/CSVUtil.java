package nc.vo.fba_secd.secdimp.filetool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;

/*******************************************************************************
 * CSV工具类 主要功能： 1. 读写CSV
 * 
 * @author hpl
 * @version 1.0
 ******************************************************************************/
public class CSVUtil {
	public static List<String> readHeadKey(File file) throws IOException  {
		List<String> list=new ArrayList<String>();
		ExcelCSVParser parser = null;
		FileInputStream inputStream = null;
		try {
			 inputStream=new FileInputStream(file);
			 parser = new ExcelCSVParser(new InputStreamReader(inputStream,
																				"ISO-8859-1"));
			String[] t;
			String headkey;
			while ((t = parser.getLine()) != null) {
				for (int i = 0; i < t.length; i++) {
					if (t[i] != null) {
						headkey = convertISOToGBK(t[i]);
						if (headkey != null && !headkey.trim().equals(""))
							list.add(headkey.trim());
					}
				}
				break;
			}
		} catch (IOException e) {
			Logger.error(e.getMessage());
			throw e;
		} finally{
			parser.close();
			inputStream.close();
		}
		return list;
	}

	public static List<List<String>> readCSV(File file) throws Exception {
		List<List<String>> list = new ArrayList<List<String>>();
		ExcelCSVParser parser = null;
		FileInputStream inputStream = null;
		try {
			 inputStream=new FileInputStream(file);
			 parser = new ExcelCSVParser(new InputStreamReader(inputStream,
																				"ISO-8859-1"));
			String[] t;
			List<String> listData;
			String temp;
			while ((t = parser.getLine()) != null) {
				listData = new ArrayList<String>();
				Integer length=t.length;
				for (int i = 0; i < length; i++) {
					temp = convertISOToGBK(t[i]);
					if (temp != null) {
						temp = temp.trim();
						if (temp.equalsIgnoreCase("null"))
							temp = "";
					}
					listData.add(temp);
				}
				list.add(listData);
			}
		} catch (IOException e) {
			Logger.error(e.getMessage());
			throw e;
		}finally{
	
			parser.close();
		}
		return list;
	}

	/**
	 * 将ISO编码格式的字符串转换成GBK编码字符串
	 * 
	 * @param str 用ISO编码的字符串
	 * @return GBK编码的字符串
	 * @throws UnsupportedEncodingException 
	 */
	private static String convertISOToGBK(String str) throws UnsupportedEncodingException {
		String convertedStr = null;
		convertedStr = new String(str.getBytes("ISO-8859-1"), "GBK");
		return convertedStr;
	}

	public static Map<String, Integer> createHeadKey(List<List<String>> listRow) {
		Map<String, Integer> hsHeadKey = new HashMap<String, Integer>();
		List<String> listCell = listRow.get(0);
		if (listCell != null) {
			hsHeadKey = new HashMap<String, Integer>();
			int iSize = listCell.size();
			for (int i = 0; i < iSize; i++) {
				if (listCell.get(i) != null){
					String keyVal = StringHdlUtil.handlerKey(listCell.get(i).toLowerCase());
					hsHeadKey.put(keyVal, Integer.valueOf(i));
				}
			}
		}
		return hsHeadKey;
	}

	public static String getData(String colName, Map<String, Integer> hsHeadKey,
			List<String> listCell) {
		Integer iIndex = hsHeadKey.get(colName.toLowerCase());
		if (iIndex != null && listCell.size() > iIndex.intValue())
			return listCell.get(iIndex.intValue());
		else
			return null;
	}
	
}
