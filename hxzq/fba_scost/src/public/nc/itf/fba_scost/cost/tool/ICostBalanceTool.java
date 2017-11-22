package nc.itf.fba_scost.cost.tool;

import nc.vo.fba_scost.cost.fundbalance.FundBalanceVO;
import nc.vo.fba_scost.cost.inventoryinfo.InventoryInfoVO;
import nc.vo.fba_scost.cost.pub.BanlanceQueryKeyVO;
import nc.vo.fba_scost.cost.stockbalance.StockBalanceVO;
import nc.vo.fba_scost.cost.transferbalance.TransferBalanceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public abstract interface ICostBalanceTool {
	public abstract IScostCalCacheManager getScostcalcachemanager();

	public abstract void setScostcalcachemanager(
			IScostCalCacheManager paramIScostCalCacheManager);

	public abstract ICostBalanceTool getInstance();

	public abstract StockBalanceVO getStockbalanceVO(
			BanlanceQueryKeyVO paramBanlanceQueryKeyVO,
			ICostingTool paramICostingTool) throws BusinessException;

	public abstract TransferBalanceVO getTransferbalanceVO(String paramString,
			ICostingTool paramICostingTool) throws BusinessException;

	public abstract TransferBalanceVO getTransferBalanceVOByVO(
			SuperVO paramSuperVO, ICostingTool paramICostingTool)
			throws BusinessException;

	public abstract FundBalanceVO getFundbalanceVO(String paramString,
			ICostingTool paramICostingTool) throws BusinessException;

	public abstract FundBalanceVO getFundbalanceVOByVO(SuperVO paramSuperVO,
			ICostingTool paramICostingTool);

	public abstract void updateStockbalanceVO(
			BanlanceQueryKeyVO paramBanlanceQueryKeyVO,
			StockBalanceVO paramStockBalanceVO) throws BusinessException;

	public abstract void updateTransferBalanceVO(
			BanlanceQueryKeyVO paramBanlanceQueryKeyVO,
			TransferBalanceVO paramTransferBalanceVO) throws BusinessException;

	public abstract void updateFundbalanceVO(
			BanlanceQueryKeyVO paramBanlanceQueryKeyVO,
			FundBalanceVO paramFundBalanceVO) throws BusinessException;

	public abstract StockBalanceVO getStockbalanceVOByVO(SuperVO paramSuperVO,
			ICostingTool paramICostingTool) throws BusinessException;

	public abstract void clearAllCache();

	public abstract void updateAll(String paramString) throws Exception;

	public abstract InventoryInfoVO getInventoryInfoVO(
			BanlanceQueryKeyVO paramBanlanceQueryKeyVO,
			ICostingTool paramICostingTool) throws BusinessException;

	public abstract InventoryInfoVO getInventoryInfoVOByVO(
			SuperVO paramSuperVO, ICostingTool paramICostingTool)
			throws BusinessException;

	public abstract void updateInventoryInfoVO(
			BanlanceQueryKeyVO paramBanlanceQueryKeyVO,
			InventoryInfoVO paramInventoryInfoVO) throws BusinessException;
}
