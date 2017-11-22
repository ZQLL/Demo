package nc.vo.fba_scost.cost.costplan;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class CostPlanVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	public static final String CREATIONTIME = "creationtime";
	public static final String CREATOR = "creator";
	public static final String IF_CAPACCOUNT = "if_capaccount";
	public static final String IF_CLIENT = "if_client";
	public static final String IF_GLORGBOOK = "if_glorgbook";
	public static final String IF_OPERATESITE = "if_operatesite";
	public static final String IF_ORG = "if_org";
	public static final String IF_PARTNACCOUNT = "if_partnaccount";
	public static final String IF_SELFSGROUP = "if_selfsgroup";
	public static final String IS_GROUP = "is_group";
	public static final String ISBUSINESSPLAN = "isbusinessplan";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String MODIFIER = "modifier";
	public static final String PK_COSTPLAN = "pk_costplan";
	public static final String PK_GLORGBOOK = "pk_glorgbook";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String PLANCODE = "plancode";
	public static final String PLANNAME = "planname";
	public static final String TS = "ts";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF10 = "vdef10";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF5 = "vdef5";
	public static final String VDEF6 = "vdef6";
	public static final String VDEF7 = "vdef7";
	public static final String VDEF8 = "vdef8";
	public static final String VDEF9 = "vdef9";

	public UFDateTime getCreationtime() {
		return (UFDateTime) getAttributeValue("creationtime");
	}

	public void setCreationtime(UFDateTime creationtime) {
		setAttributeValue("creationtime", creationtime);
	}

	public String getCreator() {
		return (String) getAttributeValue("creator");
	}

	public void setCreator(String creator) {
		setAttributeValue("creator", creator);
	}

	public UFBoolean getIf_capaccount() {
		return (UFBoolean) getAttributeValue("if_capaccount");
	}

	public void setIf_capaccount(UFBoolean if_capaccount) {
		setAttributeValue("if_capaccount", if_capaccount);
	}

	public UFBoolean getIf_client() {
		return (UFBoolean) getAttributeValue("if_client");
	}

	public void setIf_client(UFBoolean if_client) {
		setAttributeValue("if_client", if_client);
	}

	public UFBoolean getIf_glorgbook() {
		return (UFBoolean) getAttributeValue("if_glorgbook");
	}

	public void setIf_glorgbook(UFBoolean if_glorgbook) {
		setAttributeValue("if_glorgbook", if_glorgbook);
	}

	public UFBoolean getIf_operatesite() {
		return (UFBoolean) getAttributeValue("if_operatesite");
	}

	public void setIf_operatesite(UFBoolean if_operatesite) {
		setAttributeValue("if_operatesite", if_operatesite);
	}

	public UFBoolean getIf_org() {
		return (UFBoolean) getAttributeValue("if_org");
	}

	public void setIf_org(UFBoolean if_org) {
		setAttributeValue("if_org", if_org);
	}

	public UFBoolean getIf_partnaccount() {
		return (UFBoolean) getAttributeValue("if_partnaccount");
	}

	public void setIf_partnaccount(UFBoolean if_partnaccount) {
		setAttributeValue("if_partnaccount", if_partnaccount);
	}

	public UFBoolean getIf_selfsgroup() {
		return (UFBoolean) getAttributeValue("if_selfsgroup");
	}

	public void setIf_selfsgroup(UFBoolean if_selfsgroup) {
		setAttributeValue("if_selfsgroup", if_selfsgroup);
	}

	public UFBoolean getIs_group() {
		return (UFBoolean) getAttributeValue("is_group");
	}

	public void setIs_group(UFBoolean is_group) {
		setAttributeValue("is_group", is_group);
	}

	public UFBoolean getIsbusinessplan() {
		return (UFBoolean) getAttributeValue("isbusinessplan");
	}

	public void setIsbusinessplan(UFBoolean isbusinessplan) {
		setAttributeValue("isbusinessplan", isbusinessplan);
	}

	public UFDateTime getModifiedtime() {
		return (UFDateTime) getAttributeValue("modifiedtime");
	}

	public void setModifiedtime(UFDateTime modifiedtime) {
		setAttributeValue("modifiedtime", modifiedtime);
	}

	public String getModifier() {
		return (String) getAttributeValue("modifier");
	}

	public void setModifier(String modifier) {
		setAttributeValue("modifier", modifier);
	}

	public String getPk_costplan() {
		return (String) getAttributeValue("pk_costplan");
	}

	public void setPk_costplan(String pk_costplan) {
		setAttributeValue("pk_costplan", pk_costplan);
	}

	public String getPk_glorgbook() {
		return (String) getAttributeValue("pk_glorgbook");
	}

	public void setPk_glorgbook(String pk_glorgbook) {
		setAttributeValue("pk_glorgbook", pk_glorgbook);
	}

	public String getPk_group() {
		return (String) getAttributeValue("pk_group");
	}

	public void setPk_group(String pk_group) {
		setAttributeValue("pk_group", pk_group);
	}

	public String getPk_org() {
		return (String) getAttributeValue("pk_org");
	}

	public void setPk_org(String pk_org) {
		setAttributeValue("pk_org", pk_org);
	}

	public String getPk_org_v() {
		return (String) getAttributeValue("pk_org_v");
	}

	public void setPk_org_v(String pk_org_v) {
		setAttributeValue("pk_org_v", pk_org_v);
	}

	public String getPlancode() {
		return (String) getAttributeValue("plancode");
	}

	public void setPlancode(String plancode) {
		setAttributeValue("plancode", plancode);
	}

	public String getPlanname() {
		return (String) getAttributeValue("planname");
	}

	public void setPlanname(String planname) {
		setAttributeValue("planname", planname);
	}

	public UFDateTime getTs() {
		return (UFDateTime) getAttributeValue("ts");
	}

	public void setTs(UFDateTime ts) {
		setAttributeValue("ts", ts);
	}

	public String getVdef1() {
		return (String) getAttributeValue("vdef1");
	}

	public void setVdef1(String vdef1) {
		setAttributeValue("vdef1", vdef1);
	}

	public String getVdef10() {
		return (String) getAttributeValue("vdef10");
	}

	public void setVdef10(String vdef10) {
		setAttributeValue("vdef10", vdef10);
	}

	public String getVdef2() {
		return (String) getAttributeValue("vdef2");
	}

	public void setVdef2(String vdef2) {
		setAttributeValue("vdef2", vdef2);
	}

	public String getVdef3() {
		return (String) getAttributeValue("vdef3");
	}

	public void setVdef3(String vdef3) {
		setAttributeValue("vdef3", vdef3);
	}

	public String getVdef4() {
		return (String) getAttributeValue("vdef4");
	}

	public void setVdef4(String vdef4) {
		setAttributeValue("vdef4", vdef4);
	}

	public String getVdef5() {
		return (String) getAttributeValue("vdef5");
	}

	public void setVdef5(String vdef5) {
		setAttributeValue("vdef5", vdef5);
	}

	public String getVdef6() {
		return (String) getAttributeValue("vdef6");
	}

	public void setVdef6(String vdef6) {
		setAttributeValue("vdef6", vdef6);
	}

	public String getVdef7() {
		return (String) getAttributeValue("vdef7");
	}

	public void setVdef7(String vdef7) {
		setAttributeValue("vdef7", vdef7);
	}

	public String getVdef8() {
		return (String) getAttributeValue("vdef8");
	}

	public void setVdef8(String vdef8) {
		setAttributeValue("vdef8", vdef8);
	}

	public String getVdef9() {
		return (String) getAttributeValue("vdef9");
	}

	public void setVdef9(String vdef9) {
		setAttributeValue("vdef9", vdef9);
	}

	public String[] getCostFieldArray() {
		List<String> fieldlist = new ArrayList();

		fieldlist.add("pk_group");
		fieldlist.add("pk_org");
		fieldlist.add("pk_assetsprop");
		fieldlist.add("pk_stocksort");
		if (getIf_glorgbook().booleanValue()) {
			fieldlist.add("pk_glorgbook");
		}
		if (getIf_capaccount().booleanValue()) {
			fieldlist.add("pk_capaccount");
		}
		if (getIf_selfsgroup().booleanValue()) {
			fieldlist.add("pk_selfsgroup");
		}
		if (getIf_partnaccount().booleanValue()) {
			fieldlist.add("pk_partnaccount");
		}
		if (getIf_operatesite().booleanValue()) {
			fieldlist.add("pk_operatesite");
		}
		if (getIf_client().booleanValue()) {
			fieldlist.add("pk_client");
		}
		fieldlist.add("begin_date");
		fieldlist.add("end_date");
		fieldlist.add("pk_securities");
		return (String[]) fieldlist.toArray(new String[fieldlist.size()]);
	}

	public String[] getFundCostFieldArray() {
		List<String> fieldlist = new ArrayList();

		fieldlist.add("pk_group");
		fieldlist.add("pk_org");
		fieldlist.add("pk_assetsprop");
		fieldlist.add("pk_stocksort");
		fieldlist.add("contractno");

		return (String[]) fieldlist.toArray(new String[fieldlist.size()]);
	}

	public String[] getCostFieldArray_hc() {
		List<String> fieldlist = new ArrayList();

		fieldlist.add("pk_group");
		fieldlist.add("pk_org");
		fieldlist.add("hc_pk_assetsprop");
		fieldlist.add("hc_pk_stocksort");
		if (getIf_glorgbook().booleanValue()) {
			fieldlist.add("pk_glorgbook");
		}
		if (getIf_capaccount().booleanValue()) {
			fieldlist.add("hc_pk_capaccount");
		}
		if (getIf_selfsgroup().booleanValue()) {
			fieldlist.add("hc_pk_selfsgroup");
		}
		if (getIf_partnaccount().booleanValue()) {
			fieldlist.add("hc_pk_partnaccount");
		}
		if (getIf_operatesite().booleanValue()) {
			fieldlist.add("hc_pk_operatesite");
		}
		if (getIf_client().booleanValue()) {
			fieldlist.add("hc_pk_client");
		}
		fieldlist.add("hc_begin_date");
		fieldlist.add("hc_end_date");
		fieldlist.add("pk_securities");
		return (String[]) fieldlist.toArray(new String[fieldlist.size()]);
	}

	public String[] getFundsFieldArray() {
		List<String> fieldlist = new ArrayList();

		fieldlist.add("pk_group");
		fieldlist.add("pk_org");
		if (getIf_glorgbook().booleanValue()) {
			fieldlist.add("pk_glorgbook");
		}
		if (getIf_capaccount().booleanValue()) {
			fieldlist.add("pk_capaccount");
		}
		if (getIf_selfsgroup().booleanValue()) {
			fieldlist.add("pk_selfsgroup");
		}
		if (getIf_partnaccount().booleanValue()) {
			fieldlist.add("pk_partnaccount");
		}
		if (getIf_operatesite().booleanValue()) {
			fieldlist.add("pk_operatesite");
		}
		if (getIf_client().booleanValue()) {
			fieldlist.add("pk_client");
		}
		return (String[]) fieldlist.toArray(new String[fieldlist.size()]);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("fba_sim.CostplanVO");
	}
}
