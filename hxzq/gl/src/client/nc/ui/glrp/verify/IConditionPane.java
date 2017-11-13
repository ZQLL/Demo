package nc.ui.glrp.verify;

/**
 *  功能：查询条件接口
 *  作者：宋涛
 *  创建时间：(2003-5-7 16:11:43)
 *  使用说明：以及别人可能感兴趣的介绍
 *  注意：现存Bug
 */
public interface IConditionPane {
/**
 *  功能：用于取得查询条件
 *  作者：宋涛
 *  创建时间：(2003-5-7 16:13:18)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return nc.vo.glrp.verify.FilterCondVO
 */
nc.vo.glrp.verify.FilterCondVO getFilterVO();
/**
 *  功能：得到过滤条件
 *  作者：宋涛
 *  创建时间：(2003-5-7 16:20:42)
 *  参数：<|>
 *  返回值：
 *  算法：
 * 
 * @return nc.vo.glrp.verify.FilterCondVO
 * @param voDetail nc.vo.gl.pubvoucher.DetailVO
 */
nc.vo.glrp.verify.FilterCondVO getFilterVO(nc.vo.glrp.verify.VerifyDetailVO voDetail) throws Exception;
/**
 * 得到应用类型 0即时核销 1事后核销
 * 创建日期：(2003-5-11 18:16:47)
 * @param iType int
 */
int getType();
/**
 * 设置应用类型 0即时核销 1事后核销
 * 创建日期：(2003-5-11 18:16:47)
 * @param iType int
 */
void setType(int iType);
/**
 * 设置核销model
 * 创建日期：(2003-5-11 18:19:24)
 * @param verifyModel nc.ui.glrp.verify.IVerifyModel
 */
void setVerifyModel(IVerifyModel verifyModel);
}
