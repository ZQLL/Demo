package nc.itf.fba_secd.secdimp.pub;

import java.util.*;

import nc.vo.fba_secd.secdimp.dataimport.AggDataImportVO;
import nc.vo.fba_secd.secdimp.pub.FieldPropVO;
import nc.vo.pub.BusinessException;

public interface IDataImportBusiService {

	/**
	 * 设置业务数据缓存。 将文件或者数据源中的原始数据缓存起来 供下一步处理数据时使用。
	 * 
	 * @throws BusinessException
	 */
	public void setBusiCacheData(AggDataImportVO aggDataImportVO,
			Map<String, List> result, Map hm_info) throws BusinessException;

	/**
	 * 返回存在问题的数据。 指对照不上的数据。
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, List<Map<String, FieldPropVO>>> getFailRefData()
			throws BusinessException;

	/**
	 * 前台UI处理未对照的数据后 再次传给后台的正确的数据行
	 * 
	 * @param dataMap
	 * @throws BusinessException
	 */
	public void setHdlData(Map dataMap) throws BusinessException;

	/**
	 * 从缓存中取到原始导入数据TITLE信息
	 * 
	 * @throws BusinessException
	 */
	public Map getImportTitle() throws BusinessException;

	/**
	 * 查询出原始业务数据 供导入时界面展现使用 仅正确的数据
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map getOriBusiData() throws BusinessException;

	/**
	 * 查询出成功的要保存到数据库的数据
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map getSuccessBusiData() throws BusinessException;

	/**
	 * 导入数据到数据库
	 * 
	 * @throws BusinessException
	 */
	public String importToDB() throws BusinessException;

	public String importToDBHead() throws BusinessException;

	/**
	 * 清除业务缓存数据
	 * 
	 * @throws BusinessException
	 */
	public void clearBusiCache() throws BusinessException;

	/*
	 * 删除重复的行情
	 * 
	 * @author zq
	 * 
	 * @param pk_securities
	 * 
	 * @param trade_date
	 * 
	 * @throws BusinessException
	 */
	public void delete(String pk_securities, String trade_date)
			throws BusinessException;

	/**
	 * @author zq 查询当前证券的行情
	 * **/
	public Map queryData(String pk_securities, String trade_date)
			throws BusinessException;

}
