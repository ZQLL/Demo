package nc.impl.fba_secd.secdimp.pub;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.fba_secd.secdimp.pub.SystemConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import java.util.*;
/**
 * �����ظ������ǵ��뷽ʽ�����ݴ���
 * 
 * @author qs
 * 
 */
public class DataImportByImpMode {
	BaseDAO dao = new BaseDAO();
	private int COUNT = 100000;

	/**
	 * �Ƚ����ݿ���������Ҫ��������
	 * 
	 * @author qs
	 * @return rsMap
	 */
	public Map<String, List<SuperVO>> compareData(SuperVO[] vos,
			SuperVO baseVO, String[] queryFldArr, String[] repeatFld,
			String[] updateFld, int impmode) throws BusinessException {
		// ��Map��key������ڱȽϵ�ƴ��key
		Map<String, SuperVO> compareKeyMap = new HashMap<String, SuperVO>();
		// ����
		Map<String, List<SuperVO>> rsMap = new HashMap<String, List<SuperVO>>();
		// ���ڴ�Ÿ�������
		List<SuperVO> updateLst = new ArrayList<SuperVO>();
		// ���ڴ����������
		List<SuperVO> insertLst = new ArrayList<SuperVO>();
		int i = 0;
		SuperVO[] dataBaseVOs1 = null;
		// ��ѯ���ݿ���������
		int count = queryCount(baseVO);
		if (count != 0) {
			i = (count + COUNT - 1) / COUNT;
		}

		for (int j = 1; j <= i; j++) {
			StringBuilder sql = new StringBuilder();
			if (baseVO instanceof nc.vo.fba_sim.simbs.trademarket.TradeMarketVO) {

				sql.append("select * from ");
				sql.append("(");
				sql.append("select rownum r, t.* from ").append("");
				sql.append(baseVO.getTableName()).append(" t ");
				sql.append("where rownum <=");
				sql.append(COUNT * j);
				sql.append("and dr = 0");
				sql.append(")");
				sql.append("where r >");
				sql.append(COUNT * (j - 1));
				sql.append("and pk_securities <> '~'");
				sql.append("and isnull(dr,0)=0");
			} else {

				sql.append("select * from ");
				sql.append("(");
				sql.append("select rownum r, t.* from ").append("");
				sql.append(baseVO.getTableName()).append(" t ");
				sql.append("where rownum <=");
				sql.append(COUNT * j);
				sql.append("and dr = 0");
				sql.append(")");
				sql.append("where r >");
				sql.append(COUNT * (j - 1));
				sql.append("and isnull(dr,0)=0");
			}
			Collection col2 = (Collection) dao.executeQuery(sql.toString(),
					new BeanListProcessor(baseVO.getClass()));
			SuperVO[] dataBaseVOs = (SuperVO[]) col2.toArray(new SuperVO[0]);
			initCompareKeyMap(dataBaseVOs, compareKeyMap, repeatFld);
		}

		int voSize = vos.length;
		for (int j = voSize - 1; j >= 0; j--) {
			SuperVO vo = vos[j];
			String fldKey = getCompareKey(repeatFld, vo);
			if (compareKeyMap.get(fldKey) != null) {
				// ������ظ����򸲸�
				if (impmode == SystemConst.IMPMODE_UPDATE) {
					SuperVO dataBaseVO = compareKeyMap.get(fldKey);
					contractUpdateLst(updateLst, vo, dataBaseVO, updateFld);
				}
			} else {
				// ���ظ�����������
				insertLst.add(vo);
			}
		}

		rsMap.put(SystemConst.UPDATELST, updateLst);
		rsMap.put(SystemConst.INSERTLST, insertLst);
		return rsMap;

	}

	private int queryCount(SuperVO baseVO) throws DAOException {
		StringBuilder countsql = new StringBuilder();
		countsql.append("select count(*) from ");
		countsql.append(baseVO.getTableName()).append(" t ");
		countsql.append("where dr = 0");
		int count = (Integer) dao.executeQuery(countsql.toString(),
				new ColumnProcessor());
		return count;
	}

	private void initCompareKeyMap(SuperVO[] dataBaseVOs,
			Map<String, SuperVO> compareKeyMap, String[] fieldArr) {
		int voSize = dataBaseVOs.length;
		for (int i = 0; i < voSize; i++) {
			SuperVO vo = dataBaseVOs[i];
			String fldValBuf = getCompareKey(fieldArr, vo);
			compareKeyMap.put(fldValBuf.toString(), vo);
		}

	}

	private String getCompareKey(String[] fieldArr, SuperVO vo) {
		StringBuffer fldValBuf = new StringBuffer();
		int compareFldSize = fieldArr.length;
		for (int j = 0; j < compareFldSize; j++) {
			if (vo.getAttributeValue(fieldArr[j]) == null) {
				throw new RuntimeException(fieldArr[j] + "Ϊ��");
			}
			fldValBuf.append(vo.getAttributeValue(fieldArr[j]).toString());
			fldValBuf.append(SystemConst.JOINTAG);
		}
		return fldValBuf.toString();
	}

	private void contractUpdateLst(List<SuperVO> updateLst, SuperVO vo,
			SuperVO dataBaseVO, String[] updateFld) {
		if (updateFld != null && updateFld.length > 0) {
			int fldLen = updateFld.length;
			for (int i = 0; i < fldLen; i++) {
				dataBaseVO.setAttributeValue(updateFld[i],
						vo.getAttributeValue(updateFld[i]));
			}
			updateLst.add(dataBaseVO);
		} else {
			vo.setPrimaryKey(dataBaseVO.getPrimaryKey());
			vo.setStatus(VOStatus.UPDATED);
			updateLst.add(vo);
		}
	}
}
