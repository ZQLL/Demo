package nc.ui.pub.msg.acceptor;

import java.awt.event.ActionEvent;

import nc.bs.pub.pf.PfMessageUtil;
import nc.message.reconstruction.INCMessageActionProcessor;
import nc.message.vo.NCMessage;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.vo.pf.msg.MessageActionVO;
import nc.vo.pub.msg.MessageVO;

@SuppressWarnings("serial")
public class FlowStateAction extends AbstractSingleApproveAction {

	public FlowStateAction(NCMessage message,
			INCMessageActionProcessor processor, MessageActionVO actionvo) {
		super(message, processor, actionvo);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MessageVO msgVO = PfMessageUtil
				.transferNCMessageToMessageVO(getMessage());

		String billtype = msgVO.getPk_billtype();
		String billid = msgVO.getBillPK();
		Integer workflowType = msgVO.getWorkflowtype();

		FlowStateDlg dlg = new FlowStateDlg(getParent(), billtype, billid,
				workflowType);
		dlg.showModal();
	}

	
	@Override
	String getName() {
		// update by lihaibo
		return "历史审批情况"/* @res "处理情况" */;
		// return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
		// "pfworkflow63", "2PLATFORM-00084")/* @res "处理情况" */;
	}

}
