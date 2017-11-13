package nc.ui.glrp.verify;

/**
 * 历史查询ui
 * 创建日期：(2003-5-12 19:13:00)
 * @author：宋涛
 */
public interface IHistoryUI extends java.beans.PropertyChangeListener{
/**
 *  功能：得到标题
 *  作者：宋涛
 *  创建时间：(2003-5-23 15:33:37)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return java.lang.String
 */
String getTitleName();
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-12 15:08:51)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 */
void print(boolean bDirect);
/**
 *设置汇总或详细状态
 * 创建日期：(2003-5-12 19:16:47)
 * @param bSum boolean
 */
void setbSum(boolean bSum);
/**
 *  功能：设置历史纪录查询条件
 *  作者：宋涛
 *  创建时间：(2003-5-13 8:41:45)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param voCond nc.vo.glrp.verify.LogFilterCondVO
 */
void setHisToryCond(nc.vo.glrp.verify.LogFilterCondVO voCond);
/**
 *  功能：在ui中保存model的引用
 *  作者：宋涛
 *  创建时间：(2003-5-8 15:56:01)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param newModel nc.ui.glrp.verify.IVerifyModel
 */
void setVerifyModel(IVerifyModel newModel);
/**
 *  功能：设置view
 *  作者：宋涛
 *  创建时间：(2003-5-10 16:07:32)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param newView nc.ui.glrp.verify.IVerifyView
 */
void setView(IVerifyView newView);
/**
 *  功能：显示错误信息
 *  作者：宋涛
 *  创建时间：(2003-5-8 16:57:43)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param sMsg java.lang.String
 */
void showErrorMsg(String sMsg)throws Exception;
/**
 *  功能：显示信息
 *  作者：宋涛
 *  创建时间：(2003-5-10 11:33:08)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param sMsg java.lang.String
 */
void showMsg(String sMsg);
/**
 * 结束编辑状态
 * 创建日期：(2003-5-11 15:24:07)
 */
void stopEdit();
/**
 * 此处插入方法说明。
 * 创建日期：(2003-5-12 22:28:12)
 */
void updateUi();
}
