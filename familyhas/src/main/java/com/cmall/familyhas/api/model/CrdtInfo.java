package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CrdtInfo {
	@ZapcomApi(value = "暂存款数量")
	private String crdtCount = "";
	
	@ZapcomApi(value = "暂存款产生原因描述")
	private String crdtDesc = "";
	
	@ZapcomApi(value = "暂存款发生日期")
	private String crdtCnfmDate;
	
	@ZapcomApi(value = "加减", remark = "0：减，1：加",require = 1, demo = "0")
	private String crdtType;
	
	@ZapcomApi(value = "家有订单号")
	private String crdtRelId = "";
	
	@ZapcomApi(value = "惠家有订单号")
	private String appOrdId = "";			

	public String getCrdtCount() {
		return crdtCount;
	}

	public void setCrdtCount(String crdtCount) {
		this.crdtCount = crdtCount;
	}

	public String getCrdtDesc() {
		return crdtDesc;
	}

	public void setCrdtDesc(String crdtDesc) {
		this.crdtDesc = crdtDesc;
	}

	public String getCrdtCnfmDate() {
		return crdtCnfmDate;
	}

	public void setCrdtCnfmDate(String crdtCnfmDate) {
		this.crdtCnfmDate = crdtCnfmDate;
	}

	public String getCrdtType() {
		return crdtType;
	}

	public void setCrdtType(String crdtType) {
		this.crdtType = crdtType;
	}

	public String getCrdtRelId() {
		return crdtRelId;
	}

	public void setCrdtRelId(String crdtRelId) {
		this.crdtRelId = crdtRelId;
	}

	public String getAppOrdId() {
		return appOrdId;
	}

	public void setAppOrdId(String appOrdId) {
		this.appOrdId = appOrdId;
	}

}
