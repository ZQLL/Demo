package nc.ui.erm.hxzq_fpxx.ace.handler;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;

/**
 * 单据表头表尾字段编辑前事件处理类
 * 
 * @since 6.0
 * @version 2011-7-7 下午02:51:21
 * @author duy
 */
public class AceHeadTailBeforeEditHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {

	
	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {

		e.setReturnValue(Boolean.TRUE);
		String key = e.getKey();

	}

}
