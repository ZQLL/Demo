package nc.vo.sim.iufo;

public interface SIMIufoFuncConst {

	/** DEL 2016��1��28�� */
	// public static final int FUNC_COUNT = 10;
	/** DEL 2016��1��28�� */
	/** ��ԭ��10������������5������ */
	public static final int FUNC_COUNT = 22;

	// public final static String SIM_ZQQCSL = "FBA_SIM_ZQQCSL";// ����ڳ�����
	// public final static String SIM_ZQQMSL = "FBA_SIM_ZQQMSL";// �����ĩ����
	// public final static String SIM_ZQQCJE = "FBA_SIM_ZQQCJE";// ����ڳ����
	// public final static String SIM_ZQQMJE = "FBA_SIM_ZQQMJE";// �����ĩ���
	// public final static String SIM_ZQSZQC = "FBA_SIM_ZQSZQC";// ��ֵ�ڳ����
	// public final static String SIM_ZQSZQM = "FBA_SIM_ZQSZQM";// ��ֵ��ĩ���
	// public final static String SIM_ZQGYQC = "FBA_SIM_ZQGYQC";// ���ʼ�ֵ�ڳ����
	// public final static String SIM_ZQGYQM = "FBA_SIM_ZQGYQM";// ���ʼ�ֵ��ĩ���
	// public final static String SIM_ZQCJFS = "FBA_SIM_ZQCJFS";// ���ڷ����ɽ����
	// public final static String SIM_ZQXSCB = "FBA_SIM_ZQXSCB";// ���ڷ������۳ɱ�

	public final static String SIM_ZQQCSL = "SIM_ZQQCSL";// ����ڳ�����
	public final static String SIM_ZQQMSL = "SIM_ZQQMSL";// �����ĩ����
	public final static String SIM_ZQQCJE = "SIM_ZQQCJE";// ����ڳ����
	public final static String SIM_ZQQMJE = "SIM_ZQQMJE";// �����ĩ���
	public final static String SIM_ZQSZQC = "SIM_ZQSZQC";// ��ֵ�ڳ����
	public final static String SIM_ZQSZQM = "SIM_ZQSZQM";// ��ֵ��ĩ���
	public final static String SIM_ZQGYQC = "SIM_ZQGYQC";// ���ʼ�ֵ�ڳ����
	public final static String SIM_ZQGYQM = "SIM_ZQGYQM";// ���ʼ�ֵ��ĩ���
	public final static String SIM_ZQCJFS = "SIM_ZQCJFS";// ���ڷ����ɽ����
	public final static String SIM_ZQXSCB = "SIM_ZQXSCB";// ���ڷ������۳ɱ�
	public final static String SIM_MRHYJZQM = "SIM_MRHYJZQM";// ���Լ��ֵ
	public final static String SIM_MCHYJZQM = "SIM_MCHYJZQM";// ����Լ��ֵ

	/** 2016-1-28 jingqt ����Ҫ������µ�5������ ��������4���Ƿ��ص���ʽ��ͬ ADD START */
	/** �ڳ����б������� */
	public final static String SIM_QCCYBL = "SIM_QCCYBL";
	/** ��ĩ���б������� */
	public final static String SIM_QMCYBL = "SIM_QMCYBL";
	/** �ڳ����б������� */
	public final static String SIM_JYRIPYL = "SIM_JYRIPYL";
	/** �ֲ�����/�ܹɱ�ռ��֤ȯ���������ر��룩 */
	public final static String SIM_CCZBPMBM = "SIM_CCZBPMBM";
	/** �ֲ�����/�ܹɱ�ռ��֤ȯ�������������ƣ� */
	public final static String SIM_CCZBPMMC = "SIM_CCZBPMMC";
	/** �ֲ�����/�ܹɱ�ռ��֤ȯ����������PK�� */
	public final static String SIM_CCZBPMPK = "SIM_CCZBPMPK";
	/** �ɱ�/���ʱ�ռ��֤ȯ���������ر��룩 */
	public final static String SIM_CBZBPMBM = "SIM_CBZBPMBM";
	/** �ɱ�/���ʱ�ռ��֤ȯ�������������ƣ� */
	public final static String SIM_CBZBPMMC = "SIM_CBZBPMMC";
	/** �ɱ�/���ʱ�ռ��֤ȯ����������PK�� */
	public final static String SIM_CBZBPMPK = "SIM_CBZBPMPK";
	/** 2016-1-28 jingqt ����Ҫ������µ�5������ ADD END */

	/** 2017-02-14 lihaibo ����Ҫ�����ȡԼ��ʽ���ر������ĩ��ֵ */
	/** Լ��ʽ����-��ֵ��ĩ��� */
	public final static String SIM_HXZQSZQM = "SIM_HXZQSZQM";

	public final static String[] funCodes = new String[] { SIM_ZQQCSL,
			SIM_ZQQMSL, SIM_ZQQCJE, SIM_ZQQMJE, SIM_ZQSZQC, SIM_ZQSZQM,
			SIM_ZQGYQC, SIM_ZQGYQM, SIM_ZQCJFS, SIM_ZQXSCB, SIM_QCCYBL,
			SIM_QMCYBL, SIM_JYRIPYL, SIM_CCZBPMBM, SIM_CCZBPMMC, SIM_CCZBPMPK,
			SIM_CBZBPMBM, SIM_CBZBPMMC, SIM_CBZBPMPK, SIM_MRHYJZQM,
			SIM_MCHYJZQM, SIM_HXZQSZQM, };

	public final static String[] funcNames = new String[] { "����ڳ�����", "�����ĩ����",
			"����ڳ����", "�����ĩ���", "��ֵ�ڳ����", "��ֵ��ĩ���", "���ʼ�ֵ�ڳ����", "���ʼ�ֵ��ĩ���",
			"���ڷ����ɽ����", "���ڷ������۳ɱ�", "�ڳ����б�������", "��ĩ���б�������", "��������ƫ����",
			"�ֲ�����/�ܹɱ�ռ��֤ȯ���������ر��룩", "�ֲ�����/�ܹɱ�ռ��֤ȯ�������������ƣ�",
			"�ֲ�����/�ܹɱ�ռ��֤ȯ����������PK��", "�ɱ�/���ʱ�ռ��֤ȯ���������ر��룩", "�ɱ�/���ʱ�ռ��֤ȯ�������������ƣ�",
			"�ɱ�/���ʱ�ռ��֤ȯ����������PK��", "���Լ��ֵ", "����Լ��ֵ", "Լ��ʽ����-��ֵ��ĩ���" };

	public final static String[] funcDesces = new String[] {
			"����ڳ�����(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"�����ĩ����(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"����ڳ����(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"�����ĩ���(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"��ֵ�ڳ����(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"��ֵ��ĩ���(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"���ʼ�ֵ�ڳ����(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"���ʼ�ֵ��ĩ���(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"���ڷ����ɽ����(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"���ڷ������۳ɱ�(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)",
			"�ڳ����б�����������֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"��ĩ���б�����������֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"��������ƫ����������,ƫ������",
			"�ֲ�����/�ܹɱ�ռ��֤ȯ���������ر��룩����֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"�ֲ�����/�ܹɱ�ռ��֤ȯ�������������ƣ�����֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"�ֲ�����/�ܹɱ�ռ��֤ȯ����������PK������֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"�ɱ�/���ʱ�ռ��֤ȯ���������ر��룩����֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"�ɱ�/���ʱ�ռ��֤ȯ�������������ƣ�����֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"�ɱ�/���ʱ�ռ��֤ȯ����������PK������֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ����ԣ�",
			"���Լ��ֵ����֯,�˱�,ʱ��,֤ȯ����,�ʽ��˺�,����,�����֯��",
			"���Լ��ֵ����֯,�˱�,ʱ��,֤ȯ����,�ʽ��˺�,����,�����֯��",
			"Լ��ʽ����-��ֵ��ĩ���(��֯,�˱�,�ڼ�,�ڼ�����,֤ȯ����,֤ȯ����,�ʽ��˺�,ҵ��С��,�ɶ��˺�,�ֻ��ص�,������λ,����,���ر���,�Ƿ�����,��������,����,����ʽ,���ʱ�,�����֯,�ʲ�����)" };

	// ��������ƫ����������,ƫ������
	public String[] jyparamNames = { "����", "ƫ����" };
	/**
	 * ����
	 */
	public String[] paramNames = { "��֯", /** ���� **/
	"�˱�", /** ���� **/
	"�ڼ�", /** ��ʽ �� 2010-10-09 **/
	"�ڼ�����", /** �ꡢ�¡��� **/
	"֤ȯ����", /** ���� **/
	"֤ȯ����", /** ���� **/
	"�ʽ��˺�", /** ���� **/
	"ҵ��С��", /** ���� **/
	"�ɶ��˺�", /** ���� **/
	"�ֻ��ص�", /** ���� **/
	"������λ", /** ���� **/
	"����", /** ���� **/
	"���ر���", /** ���� **/
	"�Ƿ�����", /** Y��N��ALL **/
	"��������", /** B��S **/
	"����", /** ���� **/
	"����ʽ", /** A��D **/
	"���ʱ�", /** ���ʱ� **/
	"�����֯", /** ���� **/
	"�ʲ�����" }/** ���� **/
	;
	public String[] paramCodes = { "orgCode", "glbookCode", "period",
			"periodType", "pk_classify", "pk_securities", "pk_capaccount",
			"pk_selfsgroup", "pk_partnaccount", "pk_operatesite", "pk_client",
			"pk_currtype", "pk_returntype", "islimit", "bs", "rank", "ad",
			"pure_capital", "stocksortcode", "assetspropcode" };
	public String[] preiodType = { "��", "��", "��" };
	public String[] bsType = { "B", "S" };// B����ȡ����ĳɽ���S����ȡ�����ĳɽ����
	public String[] sortType = { "A", "D" };// A��������D������
	public String[] limitType = { "Y", "N", "ALL" };// Y��ʾȡ���۵ģ� N��ʾȡ���е�
}
