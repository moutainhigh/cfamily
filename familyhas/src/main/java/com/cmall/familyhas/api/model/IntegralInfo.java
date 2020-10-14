package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class IntegralInfo {
	@ZapcomApi(value = "积分数量")
	private String accmCount = "";
	
	@ZapcomApi(value = "积分产生原因描述")
	private String accmDesc = "";
	
	@ZapcomApi(value = "积分发生日期")
	private String accmCnfmDate;
	
	@ZapcomApi(value = "加减", remark = "0：减，1：加",require = 1, demo = "0")
	private String accmtype;
	
	@ZapcomApi(value = "家有订单号")
	private String accmRelId = "";
	
	@ZapcomApi(value = "惠家有订单号")
	private String appOrdId = "";
	

	public String getAccmRelId() {
		return accmRelId;
	}

	public void setAccmRelId(String accmRelId) {
		this.accmRelId = accmRelId;
	}

	public String getAccmtype() {
		return accmtype;
	}

	public String getAccmCount() {
		return accmCount;
	}

	public void setAccmCount(String accmCount) {
		this.accmCount = accmCount;
	}

	public void setAccmtype(String accmtype) {
		this.accmtype = accmtype;
	}

	public String getAccmDesc() {
		return accmDesc;
	}

	public void setAccmDesc(String accmDesc) {
		this.accmDesc = accmDesc;
	}

	public String getAccmCnfmDate() {
		return accmCnfmDate;
	}

	public void setAccmCnfmDate(String accmCnfmDate) {
		this.accmCnfmDate = accmCnfmDate;
	}

	public String getAppOrdId() {
		return appOrdId;
	}

	public void setAppOrdId(String appOrdId) {
		this.appOrdId = appOrdId;
	}

}
