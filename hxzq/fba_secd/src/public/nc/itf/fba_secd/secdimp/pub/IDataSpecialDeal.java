package nc.itf.fba_secd.secdimp.pub;

import java.util.List;
import java.util.Map;

import nc.vo.fba_secd.secdimp.dataimport.DataImportVO;

/**
 * 数据导入， 数据特殊处理
 * *****************************************************************
 * ******************************
 * 数据导入如果需要做特殊处理，可以实现此类，但需遵循相应规则：**********************************************
 * 这些接口
 * ，在数据导入时，执行的顺序为：***********************************************************
 * *****
 * 1，specialDealBySpeFile：根据特殊处理文件处理数据，主要考虑行情记录第一行可能不是记录交易日期的数据，所以需要从文件中解析出来
 * 2，specialDealBeforeRepair：对源数据进行调整成标准表格数据形式
 * 3，getQueryCondBeforeConstrast：在作数据对照前，对数据进行过滤的条件
 * 所以需要进行基础数据对照的数据为getQueryCondBeforeConstrast过滤之后的数据
 * 4，getQueryCondAfterConstrast： 基础数据对照后，对数据进行过滤得条件 界面展现的源数据列表为进行了上面4步之后的数据
 * 5，specialDealAfterRepair：对原数据进行处理后，如转换为ncpk值，增加了默认值得处理等等后，再对该数据的处理在该方法中实现
 * 
 * @author hupl
 * 
 */
public interface IDataSpecialDeal {
	/**
	 * dataV数据格式不能改变(附aim_fields，dataV数据列一一对应，但只是选择了源字段的数据列)
	 * 导入时，在原始数据变更为根据配置得到的数据前，进行数据的特殊处理
	 * 
	 * @param aim_fields
	 * @param metaVO
	 *            TODO
	 * @param dataV
	 *            ,tablevo为源数据和原数据对应的表结构
	 * @throws Exception
	 */
	public void specialDealBeforeRepair(String[] aim_fields, List dataV,
			Map hm_tablevo, String tablename, Map ht, DataImportVO metaVO)
			throws Exception;

	/**
	 * dataV数据格式不能改变(附aim_fields，dataV数据列一一对应，是最后要save的数据列)
	 * 导入时，在原始数据变更为根据配置得到的数据后，进行数据的特殊处理
	 * 
	 * @param aim_fields
	 * @param metaVO
	 *            TODO
	 * @param dataV
	 *            ,tablevo为源数据和原数据对应的表结构
	 * @throws Exception
	 */
	public void specialDealAfterRepair(String[] aim_fields, List dataV,
			Map hm_tablevo, String tablename, Map ht, DataImportVO metaVO)
			throws Exception;

	/**
	 * dataV数据格式不能改变(附aim_fields，sd_fields数据列一一对应)
	 * 根据特殊文件处理原始数据，只需修改后台缓存，用于导入数据的时候使用，不在界面展现给客户
	 * 
	 * @param aim_fields
	 * @param dataV
	 * @throws Exception
	 */
	public void specialDealBySpeFile(String filepath, String[] sd_fields,
			String[] aim_fields, List dataV) throws Exception;

	/**
	 * added by cwj 针对CSV文件，根据一定条件过滤原始文件中的内容
	 * 
	 * @param filepath
	 * @param sd_fields
	 * @param aim_fields
	 * @param dataV
	 * @throws Exception
	 */
	public void specialDealTOCVSFile(String filepath, String[] sd_fields,
			String[] aim_fields, List dataV) throws Exception;

	/**
	 * 针对数据源，增加查询条件处理， 比如数据权限, 进行基础数据对照前，查询数据的条件
	 * 考虑到如果一个源字段对应多个目标字段，则建表将有错，所以这里建表时，采用目标字段建表
	 * 这样要求在设置特殊处理类中查询条件时，需要用目标字段作为查询条件
	 */
	public String getQueryCondBeforeConstrast(DataImportVO vo,
			String[] sd_fields, String[] aim_fields, String pk_corp,
			String pk_glorgbook, String userid, String currDate)
			throws Exception;

	/**
	 * 针对数据源，增加查询条件处理， 比如数据权限, 进行基础数据对照后，查询数据的条件
	 * 考虑到如果一个源字段对应多个目标字段，则建表将有错，所以这里建表时，采用目标字段建表
	 * 这样要求在设置特殊处理类中查询条件时，需要用目标字段作为查询条件
	 */
	public String getQueryCondAfterConstrast(DataImportVO vo,
			String[] sd_fields, String[] aim_fields, String pk_corp,
			String pk_glorgbook, String userid, String currDate)
			throws Exception;

	/**
	 * 导入成功后，进行数据的特殊处理
	 * 
	 * @param aim_fields
	 * @param metaVO
	 *            TODO
	 * @param dataV
	 *            ,tablevo为源数据和原数据对应的表结构
	 * @throws Exception
	 */
	public String specialDealAfterImportSuccess(String[] aim_fields,
			List dataV, Map hm_tablevo, String tablename, Map ht,
			DataImportVO metaVO) throws Exception;

}
