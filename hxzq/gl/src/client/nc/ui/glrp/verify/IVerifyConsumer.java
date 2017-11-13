package nc.ui.glrp.verify;

/**
 * 功能：调用核销view必须实现的接口 作者：宋涛 创建时间：(2003-5-7 16:40:57) 使用说明：以及别人可能感兴趣的介绍 注意：现存Bug
 */
public interface IVerifyConsumer {
	/* 返回模块名称 */
	public String getModuleName();

	/**
	 * 功能：显示错误信息 作者：宋涛 创建时间：(2003-5-8 16:59:42) 参数：<|> 返回值： 算法：
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	void showErrorMsg(String sMsg);

	/**
	 * 功能：显示信息 作者：宋涛 创建时间：(2003-5-10 11:33:52) 参数：<|> 返回值： 算法：
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	void showMsg(String sMsg);

	/**
	 * 功能：修改按钮状态 作者：宋涛 创建时间：(2003-5-7 16:44:26) 参数：<|> 返回值： 算法：
	 * 
	 * @param iState
	 *            int
	 */
	void updateButton(int iState);

	/**
	 * 功能：更改标题 作者：宋涛 创建时间：(2003-5-23 15:36:33) 参数：<|> 返回值： 算法：
	 * 
	 * @param sNewTitle
	 *            java.lang.String
	 */
	void updateTitles(String sNewTitle);

	/**
	 * 功能：提示对话框 作者： 创建时间：v31 参数：<|> 返回值： 算法：
	 * 
	 * @param sMsg
	 *            java.lang.String
	 */
	void showSuccessMsg(String sMsg);
}
