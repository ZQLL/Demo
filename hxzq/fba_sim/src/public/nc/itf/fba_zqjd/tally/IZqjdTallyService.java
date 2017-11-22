package nc.itf.fba_zqjd.tally;

import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.vo.fba_scost.tally.zqjdtally.ZqjdTallyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 提供负债类 债券买卖使用
 * 
 * @author Administrator
 * 
 */
public interface IZqjdTallyService {

	// 卖出台账
	void saveSellTally(ITrade_Data tradeVO) throws BusinessException;

	void deleteSellTally(ITrade_Data tradeVO) throws BusinessException;

	// 买入台账
	void saveBuyTally(ITrade_Data tradeVO, UFDouble sellscost)
			throws BusinessException;

	void deleteBuyTally(ITrade_Data tradeVO) throws BusinessException;

	ZqjdTallyVO gatherSellBuyBalance(ITrade_Data tradeVO)
			throws BusinessException;

}
