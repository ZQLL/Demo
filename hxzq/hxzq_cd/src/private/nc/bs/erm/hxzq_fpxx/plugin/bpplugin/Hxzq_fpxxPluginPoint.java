package nc.bs.erm.hxzq_fpxx.plugin.bpplugin;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;

/**
 * ��׼���ݵ���չ�����
 * 
 */
public enum Hxzq_fpxxPluginPoint implements IPluginPoint {
  /**
   * ����
   */
  APPROVE,
  /**
   * ɾ��
   */
  DELETE,
  /**
   * ����
   */
  INSERT,

  /**
   * ����
   */
  SEND_APPROVE,

  /**
   * ȡ�����
   */
  UNAPPROVE,

  /**
   * �ջ�
   */
  UNSEND_APPROVE,

  /**
   * ����
   */
  UPDATE,
  
  /**
   * �ű�ɾ��
   */
  SCRIPT_DELETE,
  /**
   * �ű�����
   */
  SCRIPT_INSERT,

  /**
   * �ű�����
   */
  SCRIPT_UPDATE;

  @Override
  public String getComponent() {
    return "hxzq_cd";
  }

  @Override
  public String getModule() {
    return "erm";
  }

  @Override
  public String getPoint() {
    return this.getClass().getName() + "." + this.name();
  }

}
