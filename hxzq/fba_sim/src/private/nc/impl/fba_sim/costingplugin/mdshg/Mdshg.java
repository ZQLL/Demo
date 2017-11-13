package nc.impl.fba_sim.costingplugin.mdshg;

import java.util.List;

import nc.impl.fba_sim.costingplugin.AbstractMdshg;
import nc.itf.fba_scost.cost.tool.ICostingTool;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.fba_scost.cost.pub.PubMethod;
import nc.vo.fba_sim.simtrade.hgtally.HgtallyVO;
import nc.vo.fba_sim.simtrade.mdshg.MdshgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class Mdshg extends AbstractMdshg {

	/**
	 * 审核时调用,业务实现类在这里实现自己的业务逻辑
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @throws Exception
	 */
	protected void calculateWhenCheck(ICostingTool costingtool, MdshgVO vo)
			throws Exception {
		/**
		 * 买断式回购交易记录；交易类型：购回融资、购回融券、回购融资、回购融券；不能为空字段：首次结算金额、到期结算金额；
		 * 
		 * @author lihbj
		 */
		if ("HV3I-0xx-01".equals(vo.getTranstypecode())
				|| "HV3I-0xx-02".equals(vo.getTranstypecode())
				|| "HV3I-0xx-03".equals(vo.getTranstypecode())
				|| "HV3I-0xx-04".equals(vo.getTranstypecode())) {
			if (vo.getB_settleamounts() == null
					|| vo.getD_settleamounts() == null) {
				throw new Exception(
						"审核日期："
								+ costingtool.getCurrdate()
								+ " 买断式回购交易记录；交易类型：购回融资、购回融券、回购融资、回购融券；不能为空字段：首次结算金额、到期结算金额；");
			} else if (vo.getB_settleamounts().compareTo(UFDouble.ZERO_DBL) == 0
					|| vo.getD_settleamounts().compareTo(UFDouble.ZERO_DBL) == 0) {
				throw new Exception(
						"审核日期："
								+ costingtool.getCurrdate()
								+ " 买断式回购交易记录；交易类型：购回融资、购回融券、回购融资、回购融券；不能为空字段：首次结算金额、到期结算金额；");
			}
		}

		// 传送台账处理
		// String opercode = vo.getBillno().replace("HV3I",
		// vo.getBilltypecode());
		String pk_glorgbook = costingtool.getCostParaVO().getCostplanvo()
				.getPk_glorgbook();
		vo.setOpercode(vo.getBillno());
		vo.setPk_glorgbook(pk_glorgbook);

		String transtypecode = vo.getTranstypecode();// 交易类型
		if (transtypecode.equals("HV3I-0xx-03")
				|| transtypecode.equals("HV3I-0xx-04")
				|| transtypecode.equals("HV3I-0xx-05")) {// 买断式融资（融券）回购,信用拆入
			HgtallyVO hgtallyvo = new HgtallyVO();

			hgtallyvo.setPk_group(vo.getPk_group());
			hgtallyvo.setPk_org(vo.getPk_org());
			hgtallyvo.setPk_org_v(vo.getPk_org_v());
			hgtallyvo.setPk_bourse(vo.getPk_bourse());

			hgtallyvo.setPk_bill(vo.getPk_mdshg());
			hgtallyvo.setPk_hgtype(vo.getPk_hgtype());
			hgtallyvo.setOpercode(vo.getBillno());

			hgtallyvo.setPk_billtype(vo.getPk_billtype());
			hgtallyvo.setPk_capaccount(vo.getPk_capaccount());
			hgtallyvo.setPk_operatesite(vo.getPk_operatesite());
			hgtallyvo.setPk_client(vo.getPk_client());
			hgtallyvo.setPk_selfsgroup(vo.getPk_selfsgroup());
			hgtallyvo.setPk_glorgbook(pk_glorgbook);
			hgtallyvo.setPk_busitype(vo.getPk_busitype());

			hgtallyvo.setOperdate(vo.getTrade_date().asBegin());
			hgtallyvo.setBegindate(vo.getB_settledate().asBegin());
			hgtallyvo.setPk_opbill(vo.getPk_mdshg());
			hgtallyvo.setEnddate(vo.getD_settledate().asBegin());
			hgtallyvo.setBegindistill_date(vo.getB_settledate());

			hgtallyvo.setHg_qmze(vo.getAccrual_sum() != null ? vo
					.getAccrual_sum() : pm.sub(vo.getD_settleamounts(),
					vo.getB_settleamounts()));
			hgtallyvo.setHgdays(vo.getHgdays());
			hgtallyvo.setPk_currtype(vo.getPk_currtype());
			hgtallyvo.setIsdq(new UFBoolean(false));
			hgtallyvo.setOperindex(Integer.valueOf(0));
			hgtallyvo.setAttributeValue("dr", 0);

			getBaseDao().insertVO(hgtallyvo);
		} else {
			String tradedate = costingtool.getCostParaVO().getCheckParavo()
					.getTrade_date().asBegin().toString();
			MdshgVO[] ghvos = getMdshgVOS(vo, tradedate);
			if (ghvos != null && ghvos.length > 0) {
				vo.setPk_hgbill(ghvos[0].getPk_mdshg());
				HgtallyVO[] hgtallvos = getHgtallyVO(ghvos[0], tradedate);
				if ((hgtallvos != null) && (hgtallvos.length > 0)) {
					UFDouble had_interest = hgtallvos[0].getHad_interest();
					vo.setHad_interest(had_interest);

					HgtallyVO tallyvo = (HgtallyVO) hgtallvos[0].clone();
					tallyvo.setPk_hgtally(null);
					tallyvo.setIsdq(new UFBoolean(true));
					tallyvo.setOperdate(vo.getTrade_date());
					tallyvo.setPk_billtype(vo.getPk_billtype());
					tallyvo.setOperindex(Integer.valueOf(vo.getFactdays()
							.intValue()) + 1);
					// tallyvo.setPk_opbill(vo.getPk_mdshg());
					// tallyvo.setPk_optype(vo.getPk_billtype());
					getBaseDao().insertVO(tallyvo);
				}
			}
		}
	}

	/**
	 * 弃审时调用,业务实现类在这里实现自己的业务逻辑
	 * 
	 * @param costingtool
	 * @param tradevo
	 * @throws Exception
	 */
	protected void calculateWhenUnCheck(ICostingTool costingtool, MdshgVO vo)
			throws Exception {
		// 删除台账处理 未完待续
	}

	protected MdshgVO[] getMdshgVOS(MdshgVO vo, String date) throws Exception {
		List<?> list = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select a.* from sim_mdshg a where a.pk_mdshg='");
		sb.append(vo.getPk_hgbill());
		sb.append("' and isnull(a.dr,0)=0");

		list = (List<?>) getBaseDao().executeQuery(sb.toString(),
				new BeanListProcessor(MdshgVO.class));

		return (MdshgVO[]) list.toArray(new MdshgVO[0]);
	}

	protected HgtallyVO[] getHgtallyVO(MdshgVO vo, String date)
			throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.* from sim_hgtally s");
		sb.append(" where s.pk_group='");
		sb.append(vo.getPk_group());
		sb.append("' and s.pk_org='");
		sb.append(vo.getPk_org());
		sb.append("' and s.pk_bill='");
		sb.append(vo.getPk_mdshg());
		sb.append("' and s.enddate='");
		sb.append(date);
		sb.append("'");
		if (vo.getPk_glorgbook() != null) {
			sb.append(" and s.pk_glorgbook='");
			sb.append(vo.getPk_glorgbook());
			sb.append("'");
		}
		sb.append(" order by s.operindex desc");
		List<?> list = (List<?>) getBaseDao().executeQuery(sb.toString(),
				new BeanListProcessor(HgtallyVO.class));
		if ((list != null) && (list.size() > 0)) {
			return (HgtallyVO[]) list.toArray(new HgtallyVO[0]);
		}
		return null;
	}

}
