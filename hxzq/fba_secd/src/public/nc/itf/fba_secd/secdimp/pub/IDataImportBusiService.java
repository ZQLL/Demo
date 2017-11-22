package nc.itf.fba_secd.secdimp.pub;

import java.util.*;

import nc.vo.fba_secd.secdimp.dataimport.AggDataImportVO;
import nc.vo.fba_secd.secdimp.pub.FieldPropVO;
import nc.vo.pub.BusinessException;

public interface IDataImportBusiService {

	/**
	 * ����ҵ�����ݻ��档 ���ļ���������Դ�е�ԭʼ���ݻ������� ����һ����������ʱʹ�á�
	 * 
	 * @throws BusinessException
	 */
	public void setBusiCacheData(AggDataImportVO aggDataImportVO,
			Map<String, List> result, Map hm_info) throws BusinessException;

	/**
	 * ���ش�����������ݡ� ָ���ղ��ϵ����ݡ�
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, List<Map<String, FieldPropVO>>> getFailRefData()
			throws BusinessException;

	/**
	 * ǰ̨UI����δ���յ����ݺ� �ٴδ�����̨����ȷ��������
	 * 
	 * @param dataMap
	 * @throws BusinessException
	 */
	public void setHdlData(Map dataMap) throws BusinessException;

	/**
	 * �ӻ�����ȡ��ԭʼ��������TITLE��Ϣ
	 * 
	 * @throws BusinessException
	 */
	public Map getImportTitle() throws BusinessException;

	/**
	 * ��ѯ��ԭʼҵ������ ������ʱ����չ��ʹ�� ����ȷ������
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map getOriBusiData() throws BusinessException;

	/**
	 * ��ѯ���ɹ���Ҫ���浽���ݿ������
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map getSuccessBusiData() throws BusinessException;

	/**
	 * �������ݵ����ݿ�
	 * 
	 * @throws BusinessException
	 */
	public String importToDB() throws BusinessException;

	public String importToDBHead() throws BusinessException;

	/**
	 * ���ҵ�񻺴�����
	 * 
	 * @throws BusinessException
	 */
	public void clearBusiCache() throws BusinessException;

	/*
	 * ɾ���ظ�������
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
	 * @author zq ��ѯ��ǰ֤ȯ������
	 * **/
	public Map queryData(String pk_securities, String trade_date)
			throws BusinessException;

}
