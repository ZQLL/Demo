package nc.ui.glrp.verify;

/**
 *  功能：核销view必须实现的接口
 *  作者：宋涛
 *  创建时间：(2003-5-7 16:23:03)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
 */
public interface IVerifyView {
/*返回模块名称*/
public String  getModuleName();
/**
 *  功能：相应按钮事件
 *  作者：宋涛
 *  创建时间：(2003-5-7 16:24:35)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param iBtnIdx int
 * @exception java.lang.Exception 异常说明。
 */
void onButtonClicked(int iBtnIdx) throws java.lang.Exception;
/**
 *  功能：即时核销过滤数据，如果当前凭证分录不能参加核销则抛出异常！
 *  作者：宋涛
 *  创建时间：(2003-5-7 16:32:55)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param detailvo nc.vo.gl.pubvoucher.DetailVO
 * @exception java.lang.Exception 异常说明。
 */
void queryData(nc.vo.gl.pubvoucher.DetailVO detailvo) throws java.lang.Exception;
/**
 *  功能：设置父容器
 *  作者：宋涛
 *  创建时间：(2003-5-12 11:42:32)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param parent nc.ui.glrp.verify.IVerifyConsumer
 */
void setparent(IVerifyConsumer parent);
/**
 *  功能：设置风格为即时核销或者事后核销
 *  作者：宋涛
 *  创建时间：(2003-5-7 16:28:19)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param iStyle int
 */
void setStyle(int iStyle);
/**
 *  功能：显示错误信息
 *  作者：宋涛
 *  创建时间：(2003-5-8 16:59:42)
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
 *  创建时间：(2003-5-10 11:33:52)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @param sMsg java.lang.String
 */
void showMsg(String sMsg);
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
