package nc.impl.fba_scost.cost.billcheckimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.fba_scost.cost.billcheck.IBillMakeJTD;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.vo.am.proxy.AMProxy;
import nc.vo.fba_scost.cost.checkpara.CheckParaVO;
import nc.vo.fba_scost.cost.pendingbill.PendingBillVO;
import nc.vo.fba_scost.pub.exception.ExceptionHandler;
import nc.vo.fba_zqjd.trade.zqjdtrade.ZqjdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/*author by zq
 * 已审核的借入单审核过程中产生相应的计提单
 * 已审核的借入单产生的计提单在弃审的时候删除对应计提单
 * 判断前台参数
 * */

public class BillMakeJTDimpl implements IBillMakeJTD {
	@Override
	@SuppressWarnings("unchecked")
	public void MakeJTD(CheckParaVO checkParaVO) {
		UFDate lastappdate = checkParaVO.getGroupvos()[0].getLastapprovedate();
		try {
			String sql = "select * from " + "sim_zqjd where nvl(dr,0) = 0 "
					+ "and state >= '2' " + "and transtypecode = 'HV7A-0xx-01'"
					+ " and trade_date <='"
					+ checkParaVO.getTrade_date().toString().substring(0, 10)
					+ " 23:59:59'";
			BaseDAO dao = new BaseDAO();
			List<ZqjdVO> yshzqjd = new ArrayList<ZqjdVO>();
			yshzqjd = (List<ZqjdVO>) dao.executeQuery(sql,
					new BeanListProcessor(ZqjdVO.class));
			UFDouble jdfy = new UFDouble(0);
			int yjrts = 0;// 已借入天数
			// 查询出来所有当前时间之前的借入单,有借入单进行如下操作
			if (yshzqjd.size() > 0) {
				Date sdate = null;
				Date edate = null;
				for (int x = 0; x < yshzqjd.size(); x++) {
					ZqjdVO yshzqjdvo = yshzqjd.get(x);
					UFDouble zjdfy = new UFDouble(0);
					int zqx = 0;
					if (yshzqjdvo != null) {
						String begdate = yshzqjdvo.getDate_begin().toString()
								.substring(0, 10);
						String enddate = yshzqjdvo.getDate_end().toString()
								.substring(0, 10);
						String appdate = checkParaVO.getTrade_date().toString()
								.substring(0, 10);
						if (yshzqjdvo.getTrade_fee() != null)
							zjdfy = yshzqjdvo.getTrade_fee().setScale(2,
									UFDouble.ROUND_HALF_UP);// 总借贷费用
						zqx = yshzqjdvo.getDate_end().getDaysAfter(
								yshzqjdvo.getDate_begin()) + 1;// 总期限=结束时间—开始时间
																// +1

						int res1 = enddate.compareTo(appdate);
						// 如果借入单结束时间大于当前审批时间
						if (res1 > 0) {
							// 查询之前最后一次审核生成的计提单
							String sql1 = "select * from sim_zqjd where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07' and date_end = '"
									+ yshzqjdvo.getDate_end()
									+ "' and approvedate <= '"
									+ lastappdate
									+ "' and date_begin ='"
									+ yshzqjdvo.getDate_begin()
									+ "' and date_end ='"
									+ yshzqjdvo.getDate_end()
									+ "' and pk_assetsprop ='"
									+ yshzqjdvo.getPk_assetsprop()
									+ "' and pk_capaccount ='"
									+ yshzqjdvo.getPk_capaccount()
									+ "' and pk_securities ='"
									+ yshzqjdvo.getPk_securities()
									+ "' and pk_stocksort ='"
									+ yshzqjdvo.getPk_stocksort()
									+ "' and contractno='"
									+ yshzqjdvo.getContractno() + "'";
							List<ZqjdVO> tt = (List<ZqjdVO>) dao.executeQuery(
									sql1, new BeanListProcessor(ZqjdVO.class));
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							try {
								sdate = sdf.parse(begdate);
								edate = sdf.parse(appdate);

							} catch (ParseException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
							Calendar cal = Calendar.getInstance();
							cal.setTime(sdate);
							long time1 = cal.getTimeInMillis();
							cal.setTime(edate);
							long time2 = cal.getTimeInMillis();
							long between_days = (time2 - time1)
									/ (1000 * 3600 * 24);

							yjrts = Integer.parseInt(String
									.valueOf(between_days)) + 1;
							// 如果是第一次计提
							if (tt.size() == 0 && zqx > 0) {
								jdfy = zjdfy.div(zqx).multiply(yjrts);
							}
							// 如果是已经计提了则要减去之前计提的费用
							else if ((tt.size() != 0 && zqx > 0)) {
								jdfy = zjdfy.div(zqx).multiply(yjrts);
								for (int q = 0; q < tt.size(); q++) {
									if (tt.get(q).getTrade_fee() != null)
										jdfy = jdfy
												.sub(tt.get(q)
														.getTrade_fee()
														.setScale(
																2,
																UFDouble.ROUND_HALF_UP));
								}
							}
						}
						// 如果借入单结束时间小于等于当前审批时间
						else {
							String sql1 = "select * from sim_zqjd where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07' and date_end = '"
									+ yshzqjdvo.getDate_end()
									+ "' and approvedate <= '"
									+ lastappdate
									+ " 00:00:00"
									+ "' and date_end ='"
									+ yshzqjdvo.getDate_end()
									+ "' and date_begin ='"
									+ yshzqjdvo.getDate_begin()
									+ "' and pk_assetsprop ='"
									+ yshzqjdvo.getPk_assetsprop()
									+ "' and pk_capaccount ='"
									+ yshzqjdvo.getPk_capaccount()
									+ "' and pk_securities ='"
									+ yshzqjdvo.getPk_securities()
									+ "' and pk_stocksort ='"
									+ yshzqjdvo.getPk_stocksort()
									+ "' and contractno='"
									+ yshzqjdvo.getContractno() + "'";
							List<ZqjdVO> tt = (List<ZqjdVO>) dao.executeQuery(
									sql1, new BeanListProcessor(ZqjdVO.class));
							if (tt.size() == 0) {
								if (yshzqjdvo.getTrade_fee() != null)
									jdfy = yshzqjdvo.getTrade_fee().setScale(2,
											UFDouble.ROUND_HALF_UP);
							} else {
								if (yshzqjdvo.getTrade_fee() != null)
									jdfy = yshzqjdvo.getTrade_fee().setScale(2,
											UFDouble.ROUND_HALF_UP);
								for (int q = 0; q < tt.size(); q++) {
									if (tt.get(q).getTrade_fee() != null)
										jdfy = jdfy
												.sub(tt.get(q)
														.getTrade_fee()
														.setScale(
																2,
																UFDouble.ROUND_HALF_UP));
								}
							}
						}
						// 查出是否已经审核至结束日期;
						String sqlappoveend = "select * from sim_zqjd where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07' and approvedate >= '"
								+ yshzqjdvo.getDate_end()
								+ "' and date_begin ='"
								+ yshzqjdvo.getDate_begin()
								+ "' and date_end ='"
								+ yshzqjdvo.getDate_end()
								+ "' and pk_securities ='"
								+ yshzqjdvo.getPk_securities()
								+ "' and pk_capaccount ='"
								+ yshzqjdvo.getPk_capaccount()
								+ "' and pk_assetsprop ='"
								+ yshzqjdvo.getPk_assetsprop()
								+ "' and contractno='"
								+ yshzqjdvo.getContractno() + "'";
						List<ZqjdVO> outdatelist = (List<ZqjdVO>) dao
								.executeQuery(sqlappoveend,
										new BeanListProcessor(ZqjdVO.class));
						if (outdatelist.size() > 0 && res1 < 0) {
							continue;
						}
						// 还要加判断--假设弃审之后，里面的计提单数据还存在，所以需要进行更新
						ZqjdVO jtdVO = new ZqjdVO();
						String sqlcheckrepeat = "select * from sim_zqjd where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07' and approvedate like '"
								+ checkParaVO.getTrade_date().toString()
										.substring(0, 10)
								+ "%' and date_begin ='"
								+ yshzqjdvo.getDate_begin()
								+ "' and date_end ='"
								+ yshzqjdvo.getDate_end()
								+ "' and pk_securities ='"
								+ yshzqjdvo.getPk_securities()
								+ "' and pk_capaccount ='"
								+ yshzqjdvo.getPk_capaccount()
								+ "' and pk_assetsprop ='"
								+ yshzqjdvo.getPk_assetsprop()
								+ "' and contractno='"
								+ yshzqjdvo.getContractno() + "'";
						List<ZqjdVO> mm = (List<ZqjdVO>) dao.executeQuery(
								sqlcheckrepeat, new BeanListProcessor(
										ZqjdVO.class));
						if (mm.size() != 0) {
							for (int k = 0; k < mm.size(); k++) {
								dao.deleteVO(mm.get(k));
							}
							yshzqjdvo.setTranstypecode("HV7A-0xx-07");
							yshzqjdvo.setBargain_sum(null);
							yshzqjdvo.setTrade_fee(jdfy);
							if (checkParaVO.getTrade_date().beforeDate(
									new UFDate(yshzqjdvo.getDate_end()
											.toString().substring(0, 10)
											+ " 00:00:00")))
								yshzqjdvo.setTrade_date(checkParaVO
										.getTrade_date());
							else
								yshzqjdvo.setTrade_date(new UFDate(yshzqjdvo
										.getDate_end().toString()
										.substring(0, 10)));
							yshzqjdvo.setBargain_num(null);
							yshzqjdvo.setBillno(null);
							yshzqjdvo.setTrade_fee(null);
							yshzqjdvo.setFact_sum(null);
							yshzqjdvo.setApprovedate(new UFDateTime(checkParaVO
									.getTrade_date().toString()
									.substring(0, 10)
									+ " 00:00:00"));
							yshzqjdvo.setPk_secloan(null);
							jtdVO = yshzqjdvo;
							jtdVO.setState(2);
							// 后调用产生单据号
							IBillcodeManage billCodeManager = AMProxy
									.lookup(IBillcodeManage.class);
							String newCode = billCodeManager
									.getBillCode_RequiresNew("HV7A",
											jtdVO.getPk_group(),
											jtdVO.getPk_org(), jtdVO);
							jtdVO.setBillno("JTD" + newCode);
							jtdVO.setFact_sum(null);
							String sql1 = "select * from scost_pendingbill "
									+ "where trade_date like '%"
									+ yshzqjdvo.getTrade_date().toString()
											.substring(0, 10)
									+ "%' and PK_billtype = 'HV7A-0xx-07'"
									+ " and nvl(dr,0) = 0 and Pk_glorgbook ='"
									+ yshzqjdvo.getPk_glorgbook()
									+ "' and Pk_group ='"
									+ yshzqjdvo.getPk_group()
									+ "' and Pk_org ='" + yshzqjdvo.getPk_org()
									+ "' and Pk_org_v='"
									+ yshzqjdvo.getPk_org_v() + "'";
							@SuppressWarnings("rawtypes")
							Vector vec = (Vector) dao.executeQuery(sql1,
									new VectorProcessor());
							if (vec.size() == 0) {
								PendingBillVO pdbvo = new PendingBillVO();
								pdbvo.setPk_billtype("HV7A-0xx-07");
								pdbvo.setPk_glorgbook(yshzqjdvo
										.getPk_glorgbook());
								pdbvo.setState(2);
								pdbvo.setPk_group(yshzqjdvo.getPk_group());
								pdbvo.setPk_org(yshzqjdvo.getPk_org());
								pdbvo.setPk_org_v(yshzqjdvo.getPk_org_v());
								pdbvo.setAttributeValue("dr", 0);
								pdbvo.setTrade_date(new UFDate(yshzqjdvo
										.getTrade_date().toString()
										.substring(0, 10)
										+ " 00:00:00"));
								dao.insertVO(pdbvo);
							}
							dao.insertVO(jtdVO);
						} else {
							yshzqjdvo.setTranstypecode("HV7A-0xx-07");
							yshzqjdvo.setBargain_sum(null);
							if (checkParaVO.getTrade_date().beforeDate(
									new UFDate(yshzqjdvo.getDate_end()
											.toString().substring(0, 10)
											+ " 00:00:00")))
								yshzqjdvo.setTrade_date(checkParaVO
										.getTrade_date());
							else
								yshzqjdvo.setTrade_date(new UFDate(yshzqjdvo
										.getDate_end().toString()
										.substring(0, 10)));
							yshzqjdvo.setTrade_fee(jdfy);
							yshzqjdvo.setBargain_num(null);
							yshzqjdvo.setBillno(null);
							yshzqjdvo.setFact_sum(null);
							yshzqjdvo.setApprovedate(new UFDateTime(checkParaVO
									.getTrade_date().toString()
									.substring(0, 10)
									+ " 00:00:00"));
							yshzqjdvo.setPk_secloan(null);
							jtdVO = yshzqjdvo;
							jtdVO.setState(2);
							IBillcodeManage billCodeManager = AMProxy
									.lookup(IBillcodeManage.class);
							String newCode = billCodeManager
									.getBillCode_RequiresNew("HV7A",
											jtdVO.getPk_group(),
											jtdVO.getPk_org(), jtdVO);
							jtdVO.setBillno("JTD" + newCode);
							jtdVO.setFact_sum(null);
							String sql1 = "select * from scost_pendingbill "
									+ "where trade_date like '%"
									+ yshzqjdvo.getTrade_date().toString()
											.substring(0, 10)
									+ "%' and PK_billtype = 'HV7A-0xx-07'"
									+ "and nvl(dr,0)=0 and Pk_glorgbook ='"
									+ yshzqjdvo.getPk_glorgbook()
									+ "' and Pk_group ='"
									+ yshzqjdvo.getPk_group()
									+ "' and Pk_org ='" + yshzqjdvo.getPk_org()
									+ "' and Pk_org_v='"
									+ yshzqjdvo.getPk_org_v() + "'";
							@SuppressWarnings("rawtypes")
							Vector vec = (Vector) dao.executeQuery(sql1,
									new VectorProcessor());
							if (vec.size() == 0) {
								PendingBillVO pdbvo = new PendingBillVO();
								pdbvo.setPk_billtype("HV7A-0xx-07");
								pdbvo.setPk_glorgbook(yshzqjdvo
										.getPk_glorgbook());
								pdbvo.setState(2);
								pdbvo.setPk_group(yshzqjdvo.getPk_group());
								pdbvo.setPk_org(yshzqjdvo.getPk_org());
								pdbvo.setPk_org_v(yshzqjdvo.getPk_org_v());
								pdbvo.setAttributeValue("dr", 0);
								pdbvo.setTrade_date(new UFDate(yshzqjdvo
										.getTrade_date().toString()
										.substring(0, 10)
										+ " 00:00:00"));
								dao.insertVO(pdbvo);
							}
							dao.insertVO(jtdVO);
						}
					} else
						continue;
				}
			}
		} catch (NumberFormatException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void DeleteJTD(CheckParaVO checkParaVO) {
		try {
			String sql = "select a.* from sim_zqjd a"
					+ " left join ("
					+ "select trade_date,approvedate,date_begin,"
					+ "date_end,pk_securities,pk_capaccount,"
					+ "pk_assetsprop,contractno,transtypecode "
					+ "from sim_zqjd "
					+ "where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07')"
					+ " b on a.date_begin = b.date_begin "
					+ "and a.trade_date = b.trade_date "
					+ "and a.date_end = b.date_end "
					+ "and a.pk_securities = b.pk_securities "
					+ "and a.pk_capaccount = b.pk_capaccount "
					+ "and  a.pk_assetsprop = b.pk_assetsprop "
					+ "and a.contractno = b.contractno where nvl(a.dr, 0) = 0  "
					+ "and a.transtypecode = b.transtypecode "
					+ "and a.approvedate >= '"
					+ checkParaVO.getTrade_date().toString().substring(0, 10)
					+ " 00:00:00'";
			BaseDAO dao = new BaseDAO();
			List<ZqjdVO> yshzqjd = new ArrayList<ZqjdVO>();
			yshzqjd = (List<ZqjdVO>) dao.executeQuery(sql,
					new BeanListProcessor(ZqjdVO.class));
			if (yshzqjd.size() > 0) {
				dao.deleteVOList(yshzqjd);
			}
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * 根据参数编码校验-----返回结果仅对布尔类型生效，非布尔类型不得使用 账薄级参数
	 */
	@Override
	public boolean getBooleanFromInitcode(String pk_glorgbook, String initcode) {
		boolean falg = false;
		String va = null;
		StringBuffer sf = new StringBuffer();
		sf.append(" select value from pub_sysinit where pk_org  = '"
				+ pk_glorgbook + "' " + " and initcode = '" + initcode
				+ "' and isnull(dr,0) = 0 ");
		try {
			va = (String) new BaseDAO().executeQuery(sf.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							String name = null;
							if (rs.next()) {
								name = rs.getString("value");
							}
							return name;
						}
					});

			if (va == null)
				throw ExceptionHandler.createException("当前组织下，参数:" + initcode
						+ "，没有设置! ");
			if ("Y".equals(va))
				falg = true;
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return falg;
	}
}
