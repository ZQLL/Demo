package nc.itf.fba_scost.cost.billcheck;

import java.util.List;
import java.util.Map;

import nc.bs.fba_scost.cost.simtools.CostingTool;
import nc.itf.fba_scost.cost.pub.TradeDataTool;
import nc.itf.fba_scost.cost.tool.CheckClassTool;
import nc.vo.fba_scost.cost.billtypegroup.BilltypeGroupVO;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_sim.simtrade.transformtrade.TransformtradeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public abstract interface GZISpecialBusiness {
	public abstract ISuperVO[] query(BilltypeGroupVO paramBilltypeGroupVO,
			CostingTool costingtool, TradeDataTool paramTradeDataTool,CheckClassTool checkclasstool)
			throws BusinessException, Exception;

	public abstract String[] insert(
			BilltypeGroupVO paramBilltypeGroupVO, CostingTool costingtool,
			ISuperVO[] paramArrayOfISuperVO) throws BusinessException;
	        List<String> queryzhd();
	 
}