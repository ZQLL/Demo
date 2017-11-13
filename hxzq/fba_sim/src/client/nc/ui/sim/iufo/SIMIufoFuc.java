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
	 * ִ�к�����������
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
	 * ������������
	 */
	@Override
	public Object callFunc(String strFuncName, String strParam)
			throws Exception {
		try {
			String[] params = convertUFOParam(strParam);
			ISIMIufoResultService iufoService = (ISIMIufoResultService) NCLocator
					.getInstance().lookup(ISIMIufoResultService.class);
			/** 2016��1��28�� jingqt ���ں���SIM_JYRIPYL����������ƫ�������Ĳ���ֻ������������Ҫ������������ֵ */
			IufoQuerySIMVO vo = null;
			if (strFuncName.equals(SIMIufoFuncConst.funCodes[12])) {
				// ��������ƫ����
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
			Logger.debug(" ��ѯ������ " + e.toString() + " ");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��������ƫ����
	 * 
	 * @param strParam
	 *            ����
	 * @return ����VO
	 * @author jingqt
	 * @date 2016-1-28 ����4:56:59
	 */
	private IufoQuerySIMVO getJYRIPYLVO(String[] params) {
		IufoQuerySIMVO vo = new IufoQuerySIMVO();
		vo.setDate(params[0]);
		vo.setOffset(params[1]);
		return vo;
	}

	public IufoQuerySIMVO getHYJZQMVO(String[] params) throws BusinessException {
		IufoQuerySIMVO vo = new IufoQuerySIMVO();
		// ��֯ DEL by jingqt
		// String pk_org =
		// IUFOInvokeContext.getInstance().getCorporation().getPk_org();
		StringBuffer sb = new StringBuffer();
		// ��
		sb.append(IUFOInvokeContext.getInstance().getAccountYear());
		sb.append("-");
		// ��
		sb.append(IUFOInvokeContext.getInstance().getAccountMonth());
		// ��
		sb.append("-01  00:00:00");
		String Period = sb.toString();
		/** ��Ӽ���ֵ,��֯���˱� jingqt 2016��1��29�� ADD START */
		SIM_Analysis ana = new SIM_Analysis();
		String pk_org = ana.getPK_orgFromCode(params[0]);
		vo.setPk_org(pk_org);
		vo.setPk_group(ana.getPK_GroupFromCode(pk_org));
		vo.setPk_glBookCode(ana.getPK_glBookFromCode(params[1]));
		/** ��Ӽ���ֵ,��֯���˱� jingqt 2016��1��29�� ADD END */
		vo.setPeriod(params[2]);

		vo.setPk_securities(params[3]);
		vo.setPk_capaccount(params[4]);

		vo.setPk_currtype(params[5]);

		vo.setStocksortcode(params[6]);

		return vo;
	}

	// �ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����
	// "period", "periodType",
	// "pk_classify","pk_securities", "pk_capaccount", "pk_selfsgroup",
	// "pk_partnaccount", "pk_operatesite", "pk_client", "pk_currtype",
	// "pk_returntype", "islimit", "bs"
	// ,"rank","ad","pure_capital","stocksortcode","assetspropcode"
	public IufoQuerySIMVO constructSimVO(String[] params)
			throws BusinessException {
		IufoQuerySIMVO vo = new IufoQuerySIMVO();
		// ��֯ DEL by jingqt
		// String pk_org =
		// IUFOInvokeContext.getInstance().getCorporation().getPk_org();
		StringBuffer sb = new StringBuffer();
		// ��
		sb.append(IUFOInvokeContext.getInstance().getAccountYear());
		sb.append("-");
		// ��
		sb.append(IUFOInvokeContext.getInstance().getAccountMonth());
		// ��
		sb.append("-01  00:00:00");
		String Period = sb.toString();
		/** ��Ӽ���ֵ,��֯���˱� jingqt 2016��1��29�� ADD START */
		SIM_Analysis ana = new SIM_Analysis();
		String pk_org = ana.getPK_orgFromCode(params[0]);
		vo.setPk_org(pk_org);
		vo.setPk_group(ana.getPK_GroupFromCode(pk_org));
		vo.setPk_glBookCode(ana.getPK_glBookFromCode(params[1]));
		/** ��Ӽ���ֵ,��֯���˱� jingqt 2016��1��29�� ADD END */
		vo.setPeriod(Period);
		/** ��Ϊ�����������������Ҫ����ƶ�2 */
		vo.setPeriodType(params[1 + 2]);// preiodType = { "��", "��", "��" };
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
			vo.setBs(params[12 + 2]);// bsType = { "B", "S" }; ��������
		if (params.length >= 16)
			vo.setRank(params[13 + 2]);// ����
		if (params.length >= 17)
			vo.setAd(params[14 + 2]);// sortType = {"A" , "D"}; ����ʽ
		/** ��Ӽ���ֵ jingqt 2016��1��29�� ADD START */
		if (params.length >= 18)
			vo.setPure_capital(params[15 + 2]);
		/** ��Ӽ���ֵ jingqt 2016��1��29�� ADD END */
		if (params.length >= 19)
			vo.setStocksortcode(params[16 + 2]);
		if (params.length >= 20)
			vo.setAssetspropcode(params[17 + 2]);
		return vo;
	}

	/**
	 * ������鹫ʽ�ĺϷ��ԣ����ع�ʽ��֤����ʾ��Ϣ
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
	 * ��鵥����ʽ�ĺϷ��ԣ����ع�ʽ��֤����ʾ��Ϣ
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
			return "���������󣡣�";
		String[] params = convertUFOParam(strParam);
		if (params == null
				|| params.length != SIMIufoFuncConst.paramNames.length)
			return "�������󣡣�";
		return null;
	}

	/**
	 * ���ݺ������Ʒ��ظú�����������Ϣ
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
	 * ���ݺ������Ʒ��ظú�����ʽ��Ϣ
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
	 * �õ�ָ��ģ��ĺ�������
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
	 * ���غ�������
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
		return "֤ȯ��Ӫ����";
	}

	/**
	 * ����ָ��ģ����ָ��λ�õĺ���������
	 */
	@Override
	public String getFuncName(int iModulePos, int iFuncPos, String[] strChName) {
		return SIMIufoFuncConst.funCodes[iFuncPos];
	}

	/**
	 * ���ݺ������Ƶõ��ú�������ֵ������ 0//����; 1//����; 2//�ַ�; 3//����; 4//����; 5//������;
	 * 6//����ȷ������ֵΪString ��double������;
	 */
	@Override
	public byte getFuncType(String strFuncName) {
		return 1;
	}

	/**
	 * ϵͳ�ӿڣ��жϺ����Ƿ������ģ���µĺ���
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
	 * ģ���б�
	 */
	@Override
	public UfoSimpleObject[] getModuleList() {
		UfoSimpleObject[] usos = new UfoSimpleObject[1];
		usos[0] = new UfoSimpleObject(0, "֤ȯ��Ӫ����");
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

	// ---------�ӿ�IFuncNcMultiLang
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
