package nc.tool.fba_zqjd.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.vo.fba_scost.cost.pub.SystemConst;

/**
 * �����ʲ����Թ�����
 * 
 * @author zhaoxmc
 * 
 */
public class AssetsPropTool {

	/**
	 * ��ȡ��ծ���ʲ�����
	 * 
	 * @return
	 */
	public List<String> getDebtAssets() {

		List<String> debts = new ArrayList<String  >();
		//�޸�ɾ����ծ��ȡ����ܸ�����
		debts.add(SystemConst.Pk_AssetsProp5);
		debts.add(SystemConst.Pk_AssetsProp6);
		
		return debts;
	}

	/**
	 * ��ծ������ʲ��Ƿ����������ʲ�����
	 * 
	 * @param pk_assetsprop
	 * @return
	 */
	public boolean whetherDebtAssetsContainIt(String pk_assetsprop) {

		if (StringUtils.isEmpty(pk_assetsprop))
			return false;

		return getDebtAssets().contains(pk_assetsprop);

	}
}
