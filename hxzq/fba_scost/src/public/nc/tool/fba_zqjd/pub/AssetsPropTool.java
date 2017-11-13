package nc.tool.fba_zqjd.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.vo.fba_scost.cost.pub.SystemConst;

/**
 * 金融资产属性工具类
 * 
 * @author zhaoxmc
 * 
 */
public class AssetsPropTool {

	/**
	 * 获取负债类资产集合
	 * 
	 * @return
	 */
	public List<String> getDebtAssets() {

		List<String> debts = new ArrayList<String  >();
		//修改删除了债卷，取消规避该类型
		debts.add(SystemConst.Pk_AssetsProp5);
		debts.add(SystemConst.Pk_AssetsProp6);
		
		return debts;
	}

	/**
	 * 负债类金融资产是否包含传入的资产属性
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
