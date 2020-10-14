package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiOrderActivityDetailsResult {
	@ZapcomApi(value="活动类型")
	private String activityType="";
	@ZapcomApi(value="活动编码")
	private String activityCode="";
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	@ZapcomApi(value="加减",remark="0:减    1:加")
	private int  plusOrMinus=0;
	@ZapcomApi(value="优惠金额")
	private String preferentialMoney="";
	
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public int getPlusOrMinus() {
		return plusOrMinus;
	}
	public void setPlusOrMinus(int plusOrMinus) {
		this.plusOrMinus = plusOrMinus;
	}
	public String getPreferentialMoney() {
		return preferentialMoney;
	}
	public void setPreferentialMoney(String preferentialMoney) {
		this.preferentialMoney = preferentialMoney;
	}
	
	
}
