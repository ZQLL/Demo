package nc.ui.sim.iufo;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.sim.iufo.SIM_Analysis;
import nc.itf.fi.iuforeport.IFuncNCBatch;
import nc.itf.fi.iuforeport.IFuncNcMultiLang;
import nc.itf.fi.iuforeport.ResultVO;
import nc.itf.fi.iuforeport.UfoSimpleObject;
import nc.itf.sim.iufo.ISIMIufoResultService;
import nc.itf.sim.iufo.IufoQuerySIMVO;
import nc.vo.fi.uforeport.IUFOInvokeContext;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.sim.iufo.SIMIufoFuncConst;

public class SIMIufoFuc implements IFuncNCBatch, IFuncNcMultiLang {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7795617305888589902L;

	/**
	 * 执行函数批量调用
	 */
	@Override
	public ResultVO[] callFunc(String[] strFuncName, String[] strParam)
			throws Exception {
		if (strFuncName == null || strFuncName.length <= 0) {
			return null;
		}

		ResultVO[] ret = new ResultVO[strFuncName.length];
		for (int i = 0; i < ret.length; i++) {
			Object o = callFunc(strFuncName[i], strParam[i]);
			ret[i] = new ResultVO();
			ret[i].setFuncName(strFuncName[i]);
			ret[i].setParams(strParam[i]);
			ret[i].setValue(o);
		}
		return ret;
	}

	/**
	 * 单个函数调用
	 */
	@Override
	public Object callFunc(String strFuncName, String strParam)
			throws Exception {
		try {
			String[] params = convertUFOParam(strParam);
			ISIMIufoResultService iufoService = (ISIMIufoResultService) NCLocator
					.getInstance().lookup(ISIMIufoResultService.class);
			/** 2016年1月28日 jingqt 由于函数SIM_JYRIPYL（交易日期偏移量）的参数只有两个，故需要单独做参数的值 */
			IufoQuerySIMVO vo = null;
			if (strFuncName.equals(SIMIufoFuncConst.funCodes[12])) {
				// 交易日期偏移量
				vo = this.getJYRIPYLVO(params);
			}else if(strFuncName.equals(SIMIufoFuncConst.funCodes[19]) || strFuncName.equals(SIMIufoFuncConst.funCodes[20])){
				vo = this.getHYJZQMVO(params);
			}
			else {
				vo = constructSimVO(params);
			}
			Object result = iufoService.getFuncResult(strFuncName, vo);
			if (result instanceof UFDouble) {
				return ((UFDouble) result).doubleValue();
			} else if (result instanceof String) {
				return result.toString();
			} else if (result instanceof Integer) {
				return result;
			}

		} catch (Exception e) {
			Logger.debug(" 查询出错！！ " + e.toString() + " ");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 交易日期偏移量
	 * 
	 * @param strParam
	 *            参数
	 * @return 参数VO
	 * @author jingqt
	 * @date 2016-1-28 下午4:56:59
	 */
	private IufoQuerySIMVO getJYRIPYLVO(String[] params) {
		IufoQuerySIMVO vo = new IufoQuerySIMVO();
		vo.setDate(params[0]);
		vo.setOffset(params[1]);
		return vo;
	}

	public IufoQuerySIMVO getHYJZQMVO(String[] params) throws BusinessException {
		IufoQuerySIMVO vo = new IufoQuerySIMVO();
		// 组织 DEL by jingqt
		// String pk_org =
		// IUFOInvokeContext.getInstance().getCorporation().getPk_org();
		StringBuffer sb = new StringBuffer();
		// 年
		sb.append(IUFOInvokeContext.getInstance().getAccountYear());
		sb.append("-");
		// 月
		sb.append(IUFOInvokeContext.getInstance().getAccountMonth());
		// 日
		sb.append("-01  00:00:00");
		String Period = sb.toString();
		/** 添加集团值,组织，账薄 jingqt 2016年1月29日 ADD START */
		SIM_Analysis ana = new SIM_Analysis();
		String pk_org = ana.getPK_orgFromCode(params[0]);
		vo.setPk_org(pk_org);
		vo.setPk_group(ana.getPK_GroupFromCode(pk_org));
		vo.setPk_glBookCode(ana.getPK_glBookFromCode(params[1]));
		/** 添加集团值,组织，账薄 jingqt 2016年1月29日 ADD END */
		vo.setPeriod(params[2]);

		vo.setPk_securities(params[3]);
		vo.setPk_capaccount(params[4]);

		vo.setPk_currtype(params[5]);

		vo.setStocksortcode(params[6]);

		return vo;
	}

	// 期间,期间属性,证券分类,证券档案,资金账号,业务小组,股东账号,分户地点,往来单位,币种,返回币种,是否限售,发生方向,排名,排序方式,净资本,库存组织,资产属性
	// "period", "periodType",
	// "pk_classify","pk_securities", "pk_capaccount", "pk_selfsgroup",
	// "pk_partnaccount", "pk_operatesite", "pk_client", "pk_currtype",
	// "pk_returntype", "islimit", "bs"
	// ,"rank","ad","pure_capital","stocksortcode","assetspropcode"
	public IufoQuerySIMVO constructSimVO(String[] params)
			throws BusinessException {
		IufoQuerySIMVO vo = new IufoQuerySIMVO();
		// 组织 DEL by jingqt
		// String pk_org =
		// IUFOInvokeContext.getInstance().getCorporation().getPk_org();
		StringBuffer sb = new StringBuffer();
		// 年
		sb.append(IUFOInvokeContext.getInstance().getAccountYear());
		sb.append("-");
		// 月
		sb.append(IUFOInvokeContext.getInstance().getAccountMonth());
		// 日
		sb.append("-01  00:00:00");
		String Period = sb.toString();
		/** 添加集团值,组织，账薄 jingqt 2016年1月29日 ADD START */
		SIM_Analysis ana = new SIM_Analysis();
		String pk_org = ana.getPK_orgFromCode(params[0]);
		vo.setPk_org(pk_org);
		vo.setPk_group(ana.getPK_GroupFromCode(pk_org));
		vo.setPk_glBookCode(ana.getPK_glBookFromCode(params[1]));
		/** 添加集团值,组织，账薄 jingqt 2016年1月29日 ADD END */
		vo.setPeriod(Period);
		/** 因为添加两个参数，故需要向后移动2 */
		vo.setPeriodType(params[1 + 2]);// preiodType = { "年", "月", "日" };
		vo.setPk_classify(params[2 + 2]);
		vo.setPk_securities(params[3 + 2]);
		vo.setPk_capaccount(params[4 + 2]);
		vo.setPk_selfsgroup(params[5 + 2]);
		vo.setPk_partnaccount(params[6 + 2]);
		if (params.length >= 10)
			vo.setPk_operatesite(params[7 + 2]);
		if (params.length >= 11)
			vo.setPk_client(params[8 + 2]);
		if (params.length >= 12)
			vo.setPk_currtype(params[9 + 2]);
		if (params.length >= 13)
			vo.setPk_returntype(params[10 + 2]);
		if (params.length >= 14)
			vo.setIslimit(params[11 + 2]);// limitType = {"Y", "N", "ALL"};
		if (params.length >= 15)
			vo.setBs(params[12 + 2]);// bsType = { "B", "S" }; 发生方向
		if (params.length >= 16)
			vo.setRank(params[13 + 2]);// 排名
		if (params.length >= 17)
			vo.setAd(params[14 + 2]);// sortType = {"A" , "D"}; 排序方式
		/** 添加集团值 jingqt 2016年1月29日 ADD START */
		if (params.length >= 18)
			vo.setPure_capital(params[15 + 2]);
		/** 添加集团值 jingqt 2016年1月29日 ADD END */
		if (params.length >= 19)
			vo.setStocksortcode(params[16 + 2]);
		if (params.length >= 20)
			vo.setAssetspropcode(params[17 + 2]);
		return vo;
	}

	/**
	 * 批量检查公式的合法性，返回公式验证的提示信息
	 */
	@Override
	public String[] checkFunc(String[] strFuncName, String[] strParam)
			throws Exception {
		if (strFuncName == null || strFuncName.length <= 0)
			return null;
		String[] retStr = new String[strFuncName.length];
		for (int i = 0; i < strFuncName.length; i++) {
			retStr[i] = checkFunc(strFuncName[i], strParam[i]);
		}
		return retStr;
	}

	/**
	 * 检查单个公式的合法性，返回公式验证的提示信息
	 */
	@Override
	public String checkFunc(String strFuncName, String strParam)
			throws Exception {
		boolean result = false;
		for (int i = 0; i < SIMIufoFuncConst.FUNC_COUNT; i++) {
			if (SIMIufoFuncConst.funCodes[i].equals(strFuncName)) {
				result = true;
				break;
			}
		}
		if (!result)
			return "函数名错误！！";
		String[] params = convertUFOParam(strParam);
		if (params == null
				|| params.length != SIMIufoFuncConst.paramNames.length)
			return "参数错误！！";
		return null;
	}

	/**
	 * 根据函数名称返回该函数的描述信息
	 */
	@Override
	public String getFuncDesc(String strFuncName) {
		for (int i = 0; i < SIMIufoFuncConst.FUNC_COUNT; i++) {
			if (SIMIufoFuncConst.funCodes[i].equalsIgnoreCase(strFuncName)) {
				return SIMIufoFuncConst.funcNames[i];
			}
		}
		return null;
	}

	/**
	 * 根据函数名称返回该函数格式信息
	 */
	@Override
	public String getFuncForm(String strFuncName) {
		for (int i = 0; i < SIMIufoFuncConst.FUNC_COUNT; i++) {
			if (SIMIufoFuncConst.funCodes[i].equalsIgnoreCase(strFuncName)) {
				return SIMIufoFuncConst.funcDesces[i];
			}
		}
		return null;
	}

	/**
	 * 得到指定模块的函数数组
	 */
	@Override
	public UfoSimpleObject[] getFuncList(int iModulePos) {
		UfoSimpleObject[] usos = new UfoSimpleObject[SIMIufoFuncConst.FUNC_COUNT];
		for (int i = 0; i < SIMIufoFuncConst.FUNC_COUNT; i++) {
			usos[i] = new UfoSimpleObject(i, SIMIufoFuncConst.funCodes[i]);
		}
		return usos;
	}

	/**
	 * 返回函数个数
	 */
	@Override
	public int getFuncCount(int iServPos) {
		return SIMIufoFuncConst.FUNC_COUNT;
	}

	@Override
	public String doFuncRefer(String strFuncName) {
		return null;
	}

	@Override
	public boolean getDefaultParaFlag() {
		return false;
	}

	@Override
	public int getModuleCount() {
		return 1;
	}

	@Override
	public String getModuleDesc(int iPos) {
		return "证券自营函数";
	}

	/**
	 * 返回指定模块中指定位置的函数的名称
	 */
	@Override
	public String getFuncName(int iModulePos, int iFuncPos, String[] strChName) {
		return SIMIufoFuncConst.funCodes[iFuncPos];
	}

	/**
	 * 根据函数名称得到该函数返回值的类型 0//整型; 1//浮点; 2//字符; 3//数组; 4//矩阵; 5//立方体;
	 * 6//不能确定返回值为String 或double的类型;
	 */
	@Override
	public byte getFuncType(String strFuncName) {
		return 1;
	}

	/**
	 * 系统接口，判断函数是否是这个模块下的函数
	 */
	@Override
	public boolean isModuleFunc(String strFuncName) {
		for (int i = 0; i < SIMIufoFuncConst.FUNC_COUNT; i++) {
			if (SIMIufoFuncConst.funCodes[i].equalsIgnoreCase(strFuncName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 模块列表
	 */
	@Override
	public UfoSimpleObject[] getModuleList() {
		UfoSimpleObject[] usos = new UfoSimpleObject[1];
		usos[0] = new UfoSimpleObject(0, "证券自营函数");
		return usos;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public boolean hasReferDlg(String strFuncName) {
		return false;
	}

	@Override
	public String setCalEnv(String[] env) {
		return null;
	}

	@Override
	public boolean setDefaultPara() {
		return true;
	}

	private String[] convertUFOParam(String parameter) {
		List<String> list = new ArrayList<String>();
		int size = parameter.length();
		StringBuffer buffer = new StringBuffer();
		boolean flag = false;
		for (int i = 0; i < size; i++) {
			char c = parameter.charAt(i);
			if (c == '\r') {
				flag = true;
			} else if (c == '\n') {
				if (flag) {
					flag = false;
					String value1 = buffer.toString().trim();
					if (PubAppTool.isNull(value1)) {
						value1 = null;
					}
					list.add(value1);
					buffer.setLength(0);
				} else {
					buffer.append("\n");
				}
			} else {
				if (flag) {
					buffer.append("\r");
					flag = false;
				}
				buffer.append(c);
			}
		}
		String[] ret = new String[list.size()];
		ret = list.toArray(ret);
		return ret;
	}

	public IUFOInvokeContext getIufoContext() {
		return IUFOInvokeContext.getInstance();
	}

	@Override
	public String getFuncDisName(String strFuncName) {
		return null;
	}

	// ---------接口IFuncNcMultiLang
	@Override
	public boolean isTranslate(String sFuncName) {
		return false;
	}

	@Override
	public String[] toDisplayString(String sFuncNam, String[] sParam) {
		return sParam;
	}

	@Override
	public String[] toSaveString(String sFuncName, String[] sParam) {
		return sParam;
	}
}
