package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiVipExclusiveEnterInput extends RootInput {
	@ZapcomApi(value="活动标示")
	private String activityCode = "";
	@ZapcomApi(value="姓名")
	private String name = "";
	@ZapcomApi(value="电话")
	private String phone = "";
	@ZapcomApi(value="身份证号")
	private String personalNum = "";
	@ZapcomApi(value="城市")
	private String city = "";
	
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
