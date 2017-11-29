package nc.ui.erm.hxzq_fpxx.action;

import nc.desktop.ui.ServerTimeProxy;
import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.hxzq.fpxx.FpxxBillVO;
import nc.vo.hxzq.fpxx.FpxxHVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.uif2.LoginContext;

/**
 * ���ݸ���ʱ��ͷ���崦��
 * 
 * @since 6.0
 * @version 2011-7-7 ����02:31:23
 * @author duy
 */
public class CopyActionProcessor implements ICopyActionProcessor<FpxxBillVO> {

	@Override
	public void processVOAfterCopy(FpxxBillVO billVO, LoginContext context) {
		this.processHeadVO(billVO, context);
	}

	private void processHeadVO(FpxxBillVO vo, LoginContext context) {
		UFDateTime datetime = ServerTimeProxy.getInstance().getServerTime();
		FpxxHVO hvo = vo.getParentVO();
		// ���ÿմ���
		hvo.setPrimaryKey(null);
		hvo.setVbillcode(null);
		hvo.setApprover(null);
		hvo.setTaudittime(null);
		hvo.setModifier(null);
		hvo.setModifiedtime(null);
		hvo.setCreator(null);
		hvo.setCreationtime(null);
		// hvo.setTs(null);
		// ����Ĭ��ֵ
		hvo.setDbilldate(datetime.getDate());
		hvo.setPk_group(context.getPk_group());
		hvo.setPk_org(context.getPk_org());
		hvo.setAttributeValue("fstatusflag", BillStatusEnum.FREE.value());
	}

}
