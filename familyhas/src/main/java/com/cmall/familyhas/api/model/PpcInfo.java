package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PpcInfo {
	@ZapcomApi(value = "储值金数量")
	private String ppcCount = "";
	
	@ZapcomApi(value = "储值金产生原因描述")
	private String ppcDesc = "";
	
	@ZapcomApi(value = "储值金发生日期")
	private String ppcCnfmDate;
	
	@ZapcomApi(value = "加减", remark = "0：减，1：加",require = 1, demo = "0")
	private String ppctype;
	
	@ZapcomApi(value = "家有订单号")
	private String ppcRelId = "";
	
	@ZapcomApi(value = "惠家有订单号")
	private String appOrdId = "";		

	public String getPpcCount() {
		return ppcCount;
	}

	public void setPpcCount(String ppcCount) {
		this.ppcCount = ppcCount;
	}

	public String getPpcDesc() {
		return ppcDesc;
	}

	public void setPpcDesc(String ppcDesc) {
		this.ppcDesc = ppcDesc;
	}

	public String getPpcCnfmDate() {
		return ppcCnfmDate;
	}

	public void setPpcCnfmDate(String ppcCnfmDate) {
		this.ppcCnfmDate = ppcCnfmDate;
	}

	public String getPpctype() {
		return ppctype;
	}

	public void setPpctype(String ppctype) {
		this.ppctype = ppctype;
	}

	public String getPpcRelId() {
		return ppcRelId;
	}

	public void setPpcRelId(String ppcRelId) {
		this.ppcRelId = ppcRelId;
	}

	public String getAppOrdId() {
		return appOrdId;
	}

	public void setAppOrdId(String appOrdId) {
		this.appOrdId = appOrdId;
	}

}
