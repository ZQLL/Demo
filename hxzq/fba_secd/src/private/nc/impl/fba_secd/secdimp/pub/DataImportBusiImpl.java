package nc.impl.fba_secd.secdimp.pub;

import java.util.*;
import nc.bs.dao.BaseDAO;
import nc.itf.fba_secd.secdimp.pub.IDataImportBusiService;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.fba_secd.secdimp.dataimport.AggDataImportVO;
import nc.vo.fba_secd.secdimp.pub.FieldPropVO;
import nc.vo.fba_secd.secdimp.pub.SystemConst;
import nc.vo.pub.BusinessException;

public class DataImportBusiImpl implements IDataImportBusiService {

	@Override
	public void setBusiCacheData(AggDataImportVO aggDataImportVO,
			Map<String, List> result, Map hm_info) throws BusinessException {
		DataImportCacheHdl.getInstance().cacheBusiData(aggDataImportVO, result,
				hm_info);
	}

	@Override
	public Map<String, List<Map<String, FieldPropVO>>> getFailRefData()
			throws BusinessException {
		return DataImportCacheHdl.getInstance().getFailRefData();
	}

	@Override
	public void setHdlData(Map dataMap) throws BusinessException {
		DataImportCacheHdl.getInstance().setHdlCacheData(dataMap);
	}

	@Override
	public Map getOriBusiData() throws BusinessException {
		// TODO 自动生成的方法存根
		return DataImportCacheHdl.getInstance().getOriBusiCacheData();
	}

	@Override
	public String importToDB() throws BusinessException {
		DataImportDBHdl dbHdl = new DataImportDBHdl();
		Map unNormalMetaDataCache = DataImportCacheHdl.getInstance()
				.get_unNormal_MetaData_Cache();
		Map successDataLst = DataImportCacheHdl.getInstance()
				.getSuccessBusiData();
		List headLst = (List) successDataLst.get(SystemConst.HEAD);// 判断是否有要处理的主表数据，如果没有直接返回.
		if (headLst != null && headLst.size() > 0) {
			List saveLst = dbHdl.constructImpVO(successDataLst,
					unNormalMetaDataCache);// 构造VO
			return dbHdl.importToDB(saveLst, unNormalMetaDataCache);// 操作
		} else {
			return "新增0条，更新0条";
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public String importToDBHead() throws BusinessException {
		DataImportDBHdl dbHdl = new DataImportDBHdl();
		Map unNormalMetaDataCache = DataImportCacheHdl.getInstance()
				.get_unNormal_MetaData_Cache();
		Map successDataLst = DataImportCacheHdl.getInstance()
				.getSuccessBusiData();
		List headLst = (List) successDataLst.get(SystemConst.HEAD);// 判断是否有要处理的主表数据，如果没有直接返回.
		if (headLst != null && headLst.size() > 0) {
			// 调用固定字段匹配的方法;
			List saveLst = dbHdl.constructImpVOHead(successDataLst,
					unNormalMetaDataCache);// 构造VO
			return dbHdl.importToDB(saveLst, unNormalMetaDataCache);// 操作
		} else {
			return "新增0条，更新0条";
		}
	}

	@Override
	public Map getImportTitle() throws BusinessException {
		return DataImportCacheHdl.getInstance().getTitleCacheMap();
	}

	@Override
	public void clearBusiCache() throws BusinessException {
		DataImportCacheHdl.getInstance().clear();

	}

	@Override
	public Map getSuccessBusiData() throws BusinessException {
		// TODO 自动生成的方法存根
		return DataImportCacheHdl.getInstance().getSuccessBusiData();
	}

	@Override
	public void delete(String pk_securities, String trade_date)
			throws BusinessException {
		String sql = " delete from sim_trademarket where pk_securities='"
				+ pk_securities + "'" + " and trade_date='" + trade_date
				+ "' and nvl(dr,0)=0";
		new BaseDAO().executeUpdate(sql);
	}

	public Map queryData(String pk_securities, String trade_date)
			throws BusinessException {
		String sql = " select pk_securities,trade_date from sim_trademarket a where  a.pk_securities='"
				+ pk_securities
				+ "'"
				+ " and a.trade_date='"
				+ trade_date
				+ "'and nvl(dr,0)=0";
		return (Map) new BaseDAO().executeQuery(sql, new MapProcessor());
	}

}
