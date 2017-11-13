package nc.impl.fba_scost.cost.billcheckimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.fba_scost.cost.billcheck.IBillMakeJTD;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.ui.trade.business.HYPubBO_Client;
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
 * ����˵Ľ��뵥��˹����в�����Ӧ�ļ��ᵥ
 * ����˵Ľ��뵥�����ļ��ᵥ�������ʱ��ɾ����Ӧ���ᵥ
 * �ж�ǰ̨����
 * */

public class BillMakeJTDimpl implements IBillMakeJTD {
	@SuppressWarnings("unchecked")
	public void MakeJTD(CheckParaVO checkParaVO) throws BusinessException {
		String sql = "select * from sim_zqjd where nvl(dr,0) = 0 and state >= '2' and transtypecode = 'HV7A-0xx-01'" +
				" and trade_date <='"+checkParaVO.getTrade_date().toString().substring(0, 10)+" 23:59:59'";
		BaseDAO dao = new BaseDAO();
		List<ZqjdVO> yshzqjd = new ArrayList<ZqjdVO>();
		yshzqjd = (List<ZqjdVO>) dao.executeQuery(sql, new BeanListProcessor(
				ZqjdVO.class));
		UFDouble jdfy = null;
		int yjrts = 0;//�ѽ�������
		//��ѯ�������е�ǰʱ��֮ǰ�Ľ��뵥
		if (yshzqjd.size() > 0) {
			Date sdate = null;
			Date edate = null;
			for (int x = 0; x < yshzqjd.size(); x++) {
				ZqjdVO yshzqjdvo = yshzqjd.get(x);
				String begdate = yshzqjdvo.getDate_begin().toString()
						.substring(0, 10);
				String enddate = yshzqjdvo.getDate_end().toString()
						.substring(0, 10);
				String appdate = checkParaVO.getTrade_date().toString()
						.substring(0, 10);
				UFDouble zjdfy = yshzqjdvo.getTrade_fee().setScale(2, UFDouble.ROUND_HALF_UP);// �ܽ������
				int zqx = yshzqjdvo.getDate_end().getDaysAfter(
						yshzqjdvo.getDate_begin()) + 1;// ������=����ʱ�䡪��ʼʱ�� +1

				int res1 = enddate.compareTo(appdate);
				//������뵥����ʱ����ڵ�ǰ����ʱ��
				if (res1 > 0) {
					//��ѯ֮ǰ���һ��������ɵļ��ᵥ
					String sql1 = "select * from sim_zqjd where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07' and date_end = '"
							+ yshzqjdvo.getDate_end()
							+ "' and approvedate < '"
							+ checkParaVO.getTrade_date().toString()
									.substring(0, 10)
							+ " 00:00:00"
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
							+ yshzqjdvo.getContractno()
							+ "'";
					List<ZqjdVO> tt = (List<ZqjdVO>) dao.executeQuery(sql1,
							new BeanListProcessor(ZqjdVO.class));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						sdate = sdf.parse(begdate);
						edate = sdf.parse(appdate);

					} catch (ParseException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					Calendar cal = Calendar.getInstance();
					cal.setTime(sdate);
					long time1 = cal.getTimeInMillis();
					cal.setTime(edate);
					long time2 = cal.getTimeInMillis();
					long between_days = (time2 - time1) / (1000 * 3600 * 24);

					yjrts = Integer.parseInt(String.valueOf(between_days))+1;
                    //����ǵ�һ�μ���
					if (tt.size() == 0) {
						jdfy = zjdfy.div(zqx).multiply(yjrts);
					} 
					//������Ѿ���������Ҫ��ȥ֮ǰ����ķ���
					else {
						jdfy = zjdfy.div(zqx).multiply(yjrts);
						for (int q = 0; q < tt.size(); q++) {
							jdfy = jdfy.sub(tt.get(q).getTrade_fee().setScale(2, UFDouble.ROUND_HALF_UP));
						}
					}
				} 
				//������뵥����ʱ��С�ڵ��ڵ�ǰ����ʱ��
				else {
					String sql1 = "select * from sim_zqjd where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07' and date_end = '"
							+ yshzqjdvo.getDate_end()
							+ "' and approvedate < '"
							+ checkParaVO.getTrade_date().toString()
									.substring(0, 10)
							+ " 00:00:00"
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
							+ yshzqjdvo.getContractno()
							+ "'";
					List<ZqjdVO> tt = (List<ZqjdVO>) dao.executeQuery(sql1,
							new BeanListProcessor(ZqjdVO.class));
					if (tt.size() == 0) {
						jdfy = yshzqjdvo.getTrade_fee().setScale(2, UFDouble.ROUND_HALF_UP);
					} else {
						jdfy = yshzqjdvo.getTrade_fee().setScale(2, UFDouble.ROUND_HALF_UP);
						for (int q = 0; q < tt.size(); q++) {
							jdfy = jdfy.sub(tt.get(q).getTrade_fee().setScale(2, UFDouble.ROUND_HALF_UP));
						}
					}
				}
				// ����Ƿ��Ѿ��������������;
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
				List<ZqjdVO> outdatelist = (List<ZqjdVO>) dao.executeQuery(
						sqlappoveend, new BeanListProcessor(ZqjdVO.class));
				if (outdatelist.size() > 0 && res1 < 0) {
					continue;
				}
				// ��Ҫ���ж�--��������֮������ļ��ᵥ���ݻ����ڣ�������Ҫ���и���
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
						sqlcheckrepeat, new BeanListProcessor(ZqjdVO.class));
				if (mm.size() != 0) {
					for (int k = 0; k < mm.size(); k++) {
						dao.deleteVO(mm.get(k));
					}
					yshzqjdvo.setTranstypecode("HV7A-0xx-07");
					yshzqjdvo.setBargain_sum(null);
					yshzqjdvo.setTrade_fee(jdfy);
					yshzqjdvo.setTrade_date(checkParaVO.getTrade_date());
					yshzqjdvo.setBargain_num(null);
					yshzqjdvo.setBillno(null);
					yshzqjdvo.setTrade_fee(null);
					yshzqjdvo.setApprovedate(new UFDateTime(checkParaVO
							.getTrade_date().toString().substring(0, 10)
							+ " 00:00:00"));
					yshzqjdvo.setPk_secloan(null);
					jtdVO = yshzqjdvo;
					jtdVO.setState(2);
					// ����ò������ݺ�
					IBillcodeManage billCodeManager = AMProxy
							.lookup(IBillcodeManage.class);
					String newCode = billCodeManager.getBillCode_RequiresNew(
							"HV7A", jtdVO.getPk_group(), jtdVO.getPk_org(),
							jtdVO);
					jtdVO.setBillno("JTD"+newCode);
					String sql1 = "select * from scost_pendingbill "
							+ "where trade_date like '%"
							+ yshzqjdvo.getTrade_date().toString()
									.substring(0, 10)
							+ "%' and PK_billtype = 'HV7A-0xx-07'"
							+ " and nvl(dr,0) = 0 and Pk_glorgbook ='"
							+ yshzqjdvo.getPk_glorgbook() + "' and Pk_group ='"
							+ yshzqjdvo.getPk_group() + "' and Pk_org ='"
							+ yshzqjdvo.getPk_org() + "' and Pk_org_v='"
							+ yshzqjdvo.getPk_org_v() + "'";
					Vector vec = (Vector) dao.executeQuery(sql1,
							new VectorProcessor());
					if (vec.size() == 0) {
						PendingBillVO pdbvo = new PendingBillVO();
						pdbvo.setPk_billtype("HV7A-0xx-07");
						pdbvo.setPk_glorgbook(yshzqjdvo.getPk_glorgbook());
						pdbvo.setState(2);
						pdbvo.setPk_group(yshzqjdvo.getPk_group());
						pdbvo.setPk_org(yshzqjdvo.getPk_org());
						pdbvo.setPk_org_v(yshzqjdvo.getPk_org_v());
						pdbvo.setAttributeValue("dr",0);
						pdbvo.setTrade_date(new UFDate(yshzqjdvo.getTrade_date().toString().substring(0,10)+" 00:00:00"));
						dao.insertVO(pdbvo);
					}
					dao.insertVO(jtdVO);
				} else {
					yshzqjdvo.setTranstypecode("HV7A-0xx-07");
					yshzqjdvo.setBargain_sum(null);
					yshzqjdvo.setTrade_date(checkParaVO.getTrade_date());
					yshzqjdvo.setTrade_fee(jdfy);
					yshzqjdvo.setBargain_num(null);
					yshzqjdvo.setBillno(null);
					yshzqjdvo.setApprovedate(new UFDateTime(checkParaVO
							.getTrade_date().toString().substring(0, 10)
							+ " 00:00:00"));
					yshzqjdvo.setPk_secloan(null);
					jtdVO = yshzqjdvo;
					jtdVO.setState(2);
					IBillcodeManage billCodeManager = AMProxy
							.lookup(IBillcodeManage.class);
					String newCode = billCodeManager.getBillCode_RequiresNew(
							"HV7A", jtdVO.getPk_group(), jtdVO.getPk_org(),
							jtdVO);
					jtdVO.setBillno("JTD"+newCode);
					String sql1 = "select * from scost_pendingbill "
							+ "where trade_date like '%"
							+ yshzqjdvo.getTrade_date().toString()
									.substring(0, 10)
							+ "%' and PK_billtype = 'HV7A-0xx-07'"
							+ "and nvl(dr,0)=0 and Pk_glorgbook ='"
							+ yshzqjdvo.getPk_glorgbook() + "' and Pk_group ='"
							+ yshzqjdvo.getPk_group() + "' and Pk_org ='"
							+ yshzqjdvo.getPk_org() + "' and Pk_org_v='"
							+ yshzqjdvo.getPk_org_v() + "'";
					Vector vec = (Vector) dao.executeQuery(sql1,
							new VectorProcessor());
					if (vec.size() == 0) {
						PendingBillVO pdbvo = new PendingBillVO();
						pdbvo.setPk_billtype("HV7A-0xx-07");
						pdbvo.setPk_glorgbook(yshzqjdvo.getPk_glorgbook());
						pdbvo.setState(2);
						pdbvo.setPk_group(yshzqjdvo.getPk_group());
						pdbvo.setPk_org(yshzqjdvo.getPk_org());
						pdbvo.setPk_org_v(yshzqjdvo.getPk_org_v());
						pdbvo.setAttributeValue("dr",0);
						pdbvo.setTrade_date(new UFDate(yshzqjdvo.getTrade_date().toString().substring(0,10)+" 00:00:00"));
						dao.insertVO(pdbvo);
					}
					dao.insertVO(jtdVO);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void DeleteJTD(CheckParaVO checkParaVO) throws BusinessException {
		String sql = "select * from sim_zqjd where nvl(dr,0) = 0 and transtypecode = 'HV7A-0xx-01' ";
		BaseDAO dao = new BaseDAO();
		List<ZqjdVO> yshzqjd = new ArrayList<ZqjdVO>();
		yshzqjd = (List<ZqjdVO>) dao.executeQuery(sql, new BeanListProcessor(
				ZqjdVO.class));
		if (yshzqjd.size() > 0) {
			for (int i = 0; i < yshzqjd.size(); i++) {
				ZqjdVO yshzqjdvo = yshzqjd.get(i);
				String sqlcheckrepeat = "select * from sim_zqjd where nvl(dr,0)=0 and transtypecode = 'HV7A-0xx-07' and approvedate >= '"
						+ checkParaVO.getTrade_date().toString()
								.substring(0, 10)
						+ " 00:00:00"
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
						+ yshzqjdvo.getContractno()+"'";
				List<ZqjdVO> mm = (List<ZqjdVO>) dao.executeQuery(
						sqlcheckrepeat, new BeanListProcessor(ZqjdVO.class));
				if (mm.size() > 0) {
					for (int j = 0; j < mm.size(); j++) {
						ZqjdVO jtdvo = mm.get(j);
						jtdvo.setAttributeValue("dr", 1);
						dao.updateVO(jtdvo, new String[] { "dr" });
					}
				}
			}
		}
	}

	/**
	 * ���ݲ�������У��-----���ؽ�����Բ���������Ч���ǲ������Ͳ���ʹ�� �˱�������
	 */
	public boolean getBooleanFromInitcode(String pk_glorgbook, String initcode)
			throws BusinessException {
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
		} catch (DAOException e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		if (va == null)
			throw ExceptionHandler.createException("��ǰ��֯�£�����:" + initcode
					+ "��û������! ");
		if ("Y".equals(va))
			falg = true;
		return falg;
	}

}