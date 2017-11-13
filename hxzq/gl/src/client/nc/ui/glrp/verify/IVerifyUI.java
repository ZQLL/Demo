package nc.ui.glrp.verify;

/**
 *  功能：总账核销UI接口
 *  作者：宋涛
 *  创建时间：(2003-5-8 15:47:06)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
 */
public interface IVerifyUI extends java.beans.PropertyChangeListener{
/**
 *  功能：得到当前选中的凭证id
 *  作者：宋涛
 *  创建时间：(2003-5-13 11:31:00)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return java.lang.String
 */
String getCurrVouchId() throws Exception;
/**
 * 此处插入方法说明。
 * 创建日期：(2003-5-10 22:36:56)
 */
public boolean getSelectedPane();
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
 * 此处插入方法说明。
 * 创建日期：(2003-5-10 22:36:56)
 */
void initData();
/**
 * a功能：
 *  作者：宋涛
 *  创建时间：(2003-5-12 15:08:51)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 */
void print();
/**
 * 此处插入方法说明。
 * 创建日期：(2003-5-12 22:40:41)
 */
void selectAll();
/**
 *  功能：选中凭证分录数据对应数据
 *  作者：宋涛
 *  创建时间：(2003-5-10 16:36:42)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param voDetail nc.vo.gl.pubvoucher.DetailVO
 * @exception java.lang.Exception 异常说明。
 */
void selectData(nc.vo.glrp.verify.VerifyDetailVO voDetail) throws java.lang.Exception;
/**
 * 此处插入方法说明。
 * 创建日期：(2003-5-12 22:40:51)
 */
void selectNone();
/**
 *  功能：设置风格为即时核销或者事后核销
 *  作者：宋涛
 *  创建时间：(2003-5-8 16:01:49)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param newStyle int
 */
void setStyle(int newStyle);
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
void showErrorMsg(String sMsg) throws Exception;
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
 *  功能：切换页签显示风格
 *  作者：宋涛
 *  创建时间：(2003-5-12 15:42:09)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 */
void switchForm();
/**
 *  功能：提示对话框
 *  作者：
 *  创建时间：v31
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param sMsg java.lang.String
 */
void showSuccessMsg(String sMsg);
}
