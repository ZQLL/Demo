package nc.itf.fba_scost.cost.billcheck;

import java.util.List;

import nc.bs.fba_scost.cost.simtools.CostingTool;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;

public abstract interface GZISpecialBusiness {
	public abstract ISuperVO[] query(BilltypeGroupVO paramBilltypeGroupVO,
			CostingTool costingtool, TradeDataTool paramTradeDataTool,
			CheckClassTool checkclasstool) throws BusinessException, Exception;

	public abstract String[] insert(BilltypeGroupVO paramBilltypeGroupVO,
			CostingTool costingtool, ISuperVO[] paramArrayOfISuperVO)
			throws BusinessException;

	List<String> queryzhd();

}