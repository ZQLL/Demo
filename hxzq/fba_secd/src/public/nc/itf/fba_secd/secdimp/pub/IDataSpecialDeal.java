package nc.itf.fba_secd.secdimp.pub;

import java.util.List;
import java.util.Map;

import nc.vo.fba_secd.secdimp.dataimport.DataImportVO;

/**
 * ���ݵ��룬 �������⴦��
 * *****************************************************************
 * ******************************
 * ���ݵ��������Ҫ�����⴦������ʵ�ִ��࣬������ѭ��Ӧ����**********************************************
 * ��Щ�ӿ�
 * �������ݵ���ʱ��ִ�е�˳��Ϊ��***********************************************************
 * *****
 * 1��specialDealBySpeFile���������⴦���ļ��������ݣ���Ҫ���������¼��һ�п��ܲ��Ǽ�¼�������ڵ����ݣ�������Ҫ���ļ��н�������
 * 2��specialDealBeforeRepair����Դ���ݽ��е����ɱ�׼���������ʽ
 * 3��getQueryCondBeforeConstrast���������ݶ���ǰ�������ݽ��й��˵�����
 * ������Ҫ���л������ݶ��յ�����ΪgetQueryCondBeforeConstrast����֮�������
 * 4��getQueryCondAfterConstrast�� �������ݶ��պ󣬶����ݽ��й��˵����� ����չ�ֵ�Դ�����б�Ϊ����������4��֮�������
 * 5��specialDealAfterRepair����ԭ���ݽ��д������ת��Ϊncpkֵ��������Ĭ��ֵ�ô���ȵȺ��ٶԸ����ݵĴ����ڸ÷�����ʵ��
 * 
 * @author hupl
 * 
 */
public interface IDataSpecialDeal {
	/**
	 * dataV���ݸ�ʽ���ܸı�(��aim_fields��dataV������һһ��Ӧ����ֻ��ѡ����Դ�ֶε�������)
	 * ����ʱ����ԭʼ���ݱ��Ϊ�������õõ�������ǰ���������ݵ����⴦��
	 * 
	 * @param aim_fields
	 * @param metaVO
	 *            TODO
	 * @param dataV
	 *            ,tablevoΪԴ���ݺ�ԭ���ݶ�Ӧ�ı�ṹ
	 * @throws Exception
	 */
	public void specialDealBeforeRepair(String[] aim_fields, List dataV,
			Map hm_tablevo, String tablename, Map ht, DataImportVO metaVO)
			throws Exception;

	/**
	 * dataV���ݸ�ʽ���ܸı�(��aim_fields��dataV������һһ��Ӧ�������Ҫsave��������)
	 * ����ʱ����ԭʼ���ݱ��Ϊ�������õõ������ݺ󣬽������ݵ����⴦��
	 * 
	 * @param aim_fields
	 * @param metaVO
	 *            TODO
	 * @param dataV
	 *            ,tablevoΪԴ���ݺ�ԭ���ݶ�Ӧ�ı�ṹ
	 * @throws Exception
	 */
	public void specialDealAfterRepair(String[] aim_fields, List dataV,
			Map hm_tablevo, String tablename, Map ht, DataImportVO metaVO)
			throws Exception;

	/**
	 * dataV���ݸ�ʽ���ܸı�(��aim_fields��sd_fields������һһ��Ӧ)
	 * ���������ļ�����ԭʼ���ݣ�ֻ���޸ĺ�̨���棬���ڵ������ݵ�ʱ��ʹ�ã����ڽ���չ�ָ��ͻ�
	 * 
	 * @param aim_fields
	 * @param dataV
	 * @throws Exception
	 */
	public void specialDealBySpeFile(String filepath, String[] sd_fields,
			String[] aim_fields, List dataV) throws Exception;

	/**
	 * added by cwj ���CSV�ļ�������һ����������ԭʼ�ļ��е�����
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
	 * �������Դ�����Ӳ�ѯ�������� ��������Ȩ��, ���л������ݶ���ǰ����ѯ���ݵ�����
	 * ���ǵ����һ��Դ�ֶζ�Ӧ���Ŀ���ֶΣ��򽨱��д��������ｨ��ʱ������Ŀ���ֶν���
	 * ����Ҫ�����������⴦�����в�ѯ����ʱ����Ҫ��Ŀ���ֶ���Ϊ��ѯ����
	 */
	public String getQueryCondBeforeConstrast(DataImportVO vo,
			String[] sd_fields, String[] aim_fields, String pk_corp,
			String pk_glorgbook, String userid, String currDate)
			throws Exception;

	/**
	 * �������Դ�����Ӳ�ѯ�������� ��������Ȩ��, ���л������ݶ��պ󣬲�ѯ���ݵ�����
	 * ���ǵ����һ��Դ�ֶζ�Ӧ���Ŀ���ֶΣ��򽨱��д��������ｨ��ʱ������Ŀ���ֶν���
	 * ����Ҫ�����������⴦�����в�ѯ����ʱ����Ҫ��Ŀ���ֶ���Ϊ��ѯ����
	 */
	public String getQueryCondAfterConstrast(DataImportVO vo,
			String[] sd_fields, String[] aim_fields, String pk_corp,
			String pk_glorgbook, String userid, String currDate)
			throws Exception;

	/**
	 * ����ɹ��󣬽������ݵ����⴦��
	 * 
	 * @param aim_fields
	 * @param metaVO
	 *            TODO
	 * @param dataV
	 *            ,tablevoΪԴ���ݺ�ԭ���ݶ�Ӧ�ı�ṹ
	 * @throws Exception
	 */
	public String specialDealAfterImportSuccess(String[] aim_fields,
			List dataV, Map hm_tablevo, String tablename, Map ht,
			DataImportVO metaVO) throws Exception;

}
