package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HbDetailInfo {
	@ZapcomApi(value = "惠币数量")
	private String hbCount = "";
	
	@ZapcomApi(value = "惠币产生原因描述")
	private String hbDesc = "";
	
	@ZapcomApi(value = "惠币发生日期")
	private String hbCnfmDate;
	
	@ZapcomApi(value = "加减", remark = "0：减，1：加",require = 1, demo = "0")
	private String hbtype;
	
	@ZapcomApi(value = "家有订单号")
	private String hbRelId = "";
	
	@ZapcomApi(value = "惠家有订单号")
	private String appOrdId = "";

	public String getHbCount() {
		return hbCount;
	}

	public void setHbCount(String hbCount) {
		this.hbCount = hbCount;
	}

	public String getHbDesc() {
		return hbDesc;
	}

	public void setHbDesc(String hbDesc) {
		this.hbDesc = hbDesc;
	}

	public String getHbCnfmDate() {
		return hbCnfmDate;
	}

	public void setHbCnfmDate(String hbCnfmDate) {
		this.hbCnfmDate = hbCnfmDate;
	}

	public String getHbtype() {
		return hbtype;
	}

	public void setHbtype(String hbtype) {
		this.hbtype = hbtype;
	}

	public String getHbRelId() {
		return hbRelId;
	}

	public void setHbRelId(String hbRelId) {
		this.hbRelId = hbRelId;
	}

	public String getAppOrdId() {
		return appOrdId;
	}

	public void setAppOrdId(String appOrdId) {
		this.appOrdId = appOrdId;
	}
	
	
	
}
