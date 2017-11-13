package nc.bs.fba_scost.cost.interest.pub;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.itf.fba_scost.cost.pub.ITrade_Data;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.vo.fba_scost.cost.interestdistill.InterestdistillVO;
import nc.vo.fba_scost.cost.pub.CostConstant;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

/**
 * Ӧ����Ϣ���롢ת�롢������ת��ת������Ϣ��Ӧ����Ϣ�����
 * �Ƶ��ڷ�����
 * ����ѼƵ��ڷ�����
 */
public class InterestSaveClear {
	
	public void saveInterestDistill(ICostingTool costingtool,ITrade_Data vo)
			throws BusinessException{		
		//У��
		if(check(vo))
			return;
		//��ʼ����
		BulidDistillVOInfo info = new BulidDistillVOInfo();
		List<InterestdistillVO> list = new ArrayList<InterestdistillVO>();
		InterestdistillVO distillvo = info.buildInterestdistill(costingtool, vo);
		if(distillvo != null && distillvo.getInterestdistill() != null 
				&& distillvo.getInterestdistill().doubleValue() != 0 ){
			if(vo.getGenway() == CostConstant.FORWARD_GENSTATE.ZHUANCHU.getIndex()){
				list.add(distillvo);
			}else if(vo.getGenway() == CostConstant.FORWARD_GENSTATE.ZHUANRU.getIndex()){
				//��
			}else if(vo.getGenway() == CostConstant.FORWARD_GENSTATE.ZHUANCHUANDRU.getIndex()){
				InterestdistillVO invo = info.buildInterestdistillVO_in(costingtool, vo);
				list.add(invo);
				list.add(distillvo);
			}
		}
		if(list.size() > 0){
			new BaseDAO().insertVOArray(list.toArray(new InterestdistillVO[0]));
		}
	}
	
	public boolean check(ITrade_Data vo) throws BusinessException{
		boolean falg = true;
		String pk_securities = vo.getPk_securities();
		QueryInterestBaseInfo info = new QueryInterestBaseInfo();
		boolean iszq = info.isSecuritiesAcc(pk_securities);
		if(iszq)
			falg = false;
		return falg;
	}
	/**
	 * ���Ӧ����Ϣ
	 */
	public void clearDistillInterest(ICostingTool costingtool)throws BusinessException{
		String pk_group = costingtool.getCostParaVO().getCheckParavo().getPk_group();
		String pk_org = costingtool.getCostParaVO().getCheckParavo().getPk_org();
		String pk_glbook = costingtool.getCostParaVO().getCheckParavo().getPk_glorgbook();
		UFDate trade_date = costingtool.getCostParaVO().getCheckParavo().getTrade_date();
		StringBuffer sf = new StringBuffer();
		sf.append(" delete from sim_interestdistill   where pk_org = '"+pk_org+"'  ");
		sf.append("  and pk_group = '"+pk_group+"' and isnull(dr,0) = 0   ");
		sf.append("  and distill_date >= '"+trade_date.asBegin()+"'  ");
		sf.append("  and pk_glorgbook = '"+pk_glbook+"' ");
		sf.append("  and genstate <> "+CostConstant.INTEREST_GENSTATE.JITI.getIndex()+" ");
		new BaseDAO().executeUpdate(sf.toString());
	}
}