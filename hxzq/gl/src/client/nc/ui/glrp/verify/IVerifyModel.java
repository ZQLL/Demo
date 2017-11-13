package nc.ui.glrp.verify;

import nc.vo.glrp.verify.GlVerifyDisplogVO;

/**
 * 功能：核销model实现接口 作者：宋涛 创建时间：(2003-5-8 14:39:39) 使用说明：以及别人可能感兴趣的介绍 注意：现存Bug
 */
public interface IVerifyModel extends java.beans.PropertyChangeListener {
	/**
	 * 此处插入方法说明。 创建日期：(2001-9-8 10:09:36)
	 * 
	 * @param listener
	 *            java.beans.PropertyChangeListener
	 */
	void addPropertyChangeListener(java.beans.PropertyChangeListener listener);

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-8 10:24:25)
	 * 
	 * @param propertyName
	 *            java.lang.String
	 * @param oldValue
	 *            java.lang.Object
	 * @param newValue
	 *            java.lang.Object
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue);

	/**
	 * 功能：得到model中的数据 作者：宋涛 创建时间：(2003-5-9 10:56:41) 参数：<|>true 借方，false 贷方 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO[]
	 * @param bDebit
	 *            boolean
	 */
	nc.vo.glrp.verify.VerifyDetailVO[] getData(boolean bDebit);

	/**
	 * 功能：得到历史查询条件 作者：宋涛 创建时间：(2003-5-19 20:45:00) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.LogFilterCondVO
	 */
	nc.vo.glrp.verify.LogFilterCondVO getHistoryCond();

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-9 11:55:36) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 */
	boolean getHistoryState();

	/**
	 * 功能：取得核销记录中的数据 作者：宋涛 创建时间：(2003-5-9 10:08:41) 参数：<|> 返回值： 算法：
	 * 
	 * @return java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 */
	Object getHistoryValueAt(int iRow, int iKey, int iStyle);

	/**
	 * 功能：得到历史纪录数量 作者：宋涛 创建时间：(2003-5-9 11:50:30) 参数：true 汇总状态，false 详细状态 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.GlVerifyDisplogVO[]
	 * @param bState
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	int getHistroyDataCount() throws java.lang.Exception;

	/**
	 * 功能：得到凭证过滤查询条件vo 作者：宋涛 创建时间：(2003-5-19 19:35:50) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.FilterCondVO
	 */
	nc.vo.glrp.verify.FilterCondVO getQryVo();

	/**
	 * 功能：取得凭证分录中的数据 作者：宋涛 创建时间：(2003-5-9 10:08:41) 参数：<|> 返回值： 算法：
	 * 
	 * @return java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @param bDebit
	 *            boolean
	 */
	Object getValueAt(int iRow, int iKey, int iStyle, boolean bDebit);

	/**
	 * 功能：根据凭证分录主键得到核销凭证分录 作者：宋涛 创建时间：(2003-5-14 11:38:55) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO
	 * @param pk_detail
	 *            java.lang.String
	 */
	nc.vo.glrp.verify.VerifyDetailVO getVerifyDetailByDetailpk(String pk_detail);

	/**
	 * 功能：得到与总账凭证分录对应的核销凭证分录 作者：宋涛 创建时间：(2003-5-14 11:38:55) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verify.VerifyDetailVO
	 * @param pk_detail
	 *            java.lang.String
	 */
	nc.vo.glrp.verify.VerifyDetailVO getVerifyDetailvo();

	/**
	 * 功能：得到核销对象 作者：宋涛 创建时间：(2003-5-10 16:19:33) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 * @param pk_subj
	 *            java.lang.String
	 * @param assvos
	 *            nc.vo.glcom.ass.AssVO[]
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	nc.vo.glrp.verifyobj.VerifyObjVO getVerifyObject() throws java.lang.Exception;

	/**
	 * 功能：根据科目主键得到对应的核销对象 作者：宋涛 创建时间：(2003-5-10 16:19:33) 参数：<|> 返回值： 算法：
	 * 
	 * @return nc.vo.glrp.verifyobj.VerifyObjVO
	 * @param pk_subj
	 *            java.lang.String
	 * @param assvos
	 *            nc.vo.glcom.ass.AssVO[]
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	nc.vo.glrp.verifyobj.VerifyObjVO getVerifyObject(String pk_subj) throws java.lang.Exception;

	/**
	 * 功能：判断当前辅助核算组合是否符合核销对象要求 作者：宋涛 创建时间：(2003-5-14 14:28:26) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param voDetail
	 *            nc.bs.glrp.verify.VerifyDetail
	 * @param verifyObj
	 *            nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	boolean isAssLegal(nc.vo.glcom.ass.AssVO[] assvo, nc.vo.glrp.verifyobj.VerifyObjVO verifyObj) throws Exception;

	/**
	 * 功能：判断当前凭证分录是否符合条件参加核销 作者：宋涛 创建时间：(2003-5-14 14:28:26) 参数：<|> 返回值： 算法：
	 * 
	 * @return boolean
	 * @param voDetail
	 *            nc.bs.glrp.verify.VerifyDetail
	 * @param verifyObj
	 *            nc.vo.glrp.verifyobj.VerifyObjVO
	 */
	boolean isDataLegal(nc.vo.glrp.verify.VerifyDetailVO voDetail, nc.vo.glrp.verifyobj.VerifyObjVO verifyObj) throws Exception;

	/**
	 * 功能：自动红冲 作者：lwg 创建时间：(2004-04-02 16:26:24) 参数：<|> 返回值： 算法：
	 * 
	 */
	void onAutoRedBlue(boolean bDebit) throws Exception;

	/**
	 * 功能：自动核销 作者：宋涛 创建时间：(2003-5-8 16:26:24) 参数：<|> 返回值： 算法：
	 * 
	 */
	void onAutoVerify() throws Exception;

	/**
	 * 功能：历史数据全选 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	void onHistorySelectAll();

	/**
	 * 功能：历史数据全消 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	void onHistorySelectNone();

	/**
	 * 功能：过滤参加核销的数据 作者：宋涛 创建时间：(2003-5-8 16:21:41) 参数：<|> 返回值： 算法：
	 * 
	 * @param voCond
	 *            nc.vo.glrp.verify.FilterCondVO
	 */
	void onQuery(nc.vo.glrp.verify.FilterCondVO voCond) throws Exception;

	/**
	 * 功能：过滤历史纪录 作者：宋涛 创建时间：(2003-5-8 16:50:56) 参数：<|> 返回值： 算法：
	 * 
	 */
	void onQueryHisRecord(nc.vo.glrp.verify.LogFilterCondVO voCond) throws Exception;

	/**
	 * 功能：执行红兰对冲操作 作者：宋涛 创建时间：(2003-5-10 11:39:14) 参数：<|> 返回值： 算法：
	 * 
	 * @param bForce
	 *            boolean
	 */
	void onRedBlue(boolean bForce) throws Exception;

	/**
	 * 功能：数据全选 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	void onSelectAll();

	/**
	 * 功能：全选按钮 作者：lwg 创建时间：(2004-03-30 15:31:53) 参数：<|> 返回值： 算法： version:v30
	 */
	void onSelectAll_2();

	/**
	 * 功能：数据全消 作者：宋涛 创建时间：(2003-5-14 15:31:53) 参数：<|> 返回值： 算法：
	 * 
	 */
	void onSelectNone();

	/**
	 * 功能：全消按钮 作者：lwg 创建时间：(2004-03-30 15:31:53) 参数：<|> 返回值： 算法： verison:v30
	 */
	void onSelectNone_2();

	/**
	 * 功能：执行核销 作者：宋涛 创建时间：(2003-5-8 16:14:08) 参数：<|> 返回值：true 强制核销，false非强制核销 算法：
	 * 
	 * @param bForce
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	void onVerify(boolean bForce) throws java.lang.Exception;

	/**
	 * a功能： 作者：宋涛 创建时间：(2003-5-9 11:56:09) 参数：<|> 返回值： 算法：
	 * 
	 * @param bnewState
	 *            boolean
	 */
	void setHistoryState(boolean bnewState);

	/**
	 * 设置历史纪录ui 创建日期：(2003-5-12 20:01:07)
	 * 
	 * @param newHistoryUi
	 *            nc.ui.glrp.verify.IHistoryUI
	 */
	void setHistoryUI(IHistoryUI newHistoryUi);

	/**
	 * 功能：设置model中的历史纪录数据 作者：宋涛 创建时间：(2003-5-9 11:46:56) 参数：<|> 返回值： 算法：
	 * 
	 * @param oValue
	 *            java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	void setHistoryValueAt(Object oValue, int iRow, int iKey) throws java.lang.Exception;

	/**
	 * 功能：设置model中的数据 作者：宋涛 创建时间：(2003-5-9 11:46:56) 参数：<|> 返回值： 算法：
	 * 
	 * @param oValue
	 *            java.lang.Object
	 * @param iRow
	 *            int
	 * @param iKey
	 *            int
	 * @param bDebit
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	void setValueAt(Object oValue, int iRow, int iKey, boolean bDebit) throws java.lang.Exception;

	/**
	 * 功能：将核销标准保存到model中 作者：宋涛 创建时间：(2003-5-9 13:48:34) 参数：<|> 返回值： 算法：
	 * 
	 * @param vo
	 *            nc.vo.glrp.verify.VerifyStandardVO
	 */
	void setVerifyStandardVO(nc.vo.glrp.verify.VerifyStandardVO vo);

	/**
	 * 功能：在model中添加ui的引用 作者：宋涛 创建时间：(2003-5-8 15:49:52) 参数：<|> 返回值： 算法：
	 * 
	 * @param newUi
	 *            nc.ui.glrp.verify.IVerifyUI
	 */
	void setVerifyUI(IVerifyUI newUi);

	/**
	 * 功能：执行反核销 作者：宋涛 创建时间：(2003-5-8 16:14:49) 参数：<|> 返回值： 算法：
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	void unVerify() throws java.lang.Exception;

	void setFetch(boolean newFetch);

	boolean isFetch();

	/**
	 * 功能：点击对照
	 * 
	 * @throws java.lang.Exception
	 */
	void onFetch() throws java.lang.Exception;

	/**
	 * 功能：点击取消对照
	 * 
	 * @throws java.lang.Exception
	 */
	void onCancelFetch() throws java.lang.Exception;

	GlVerifyDisplogVO[] getSelectedLogs();
	
	/**
	 * 功能：点击取消对照 hurh
	 * 
	 */
	public void setPk_accountingbook(String pkAccountingbook);

}