package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForActivityCouponInput extends RootInput{
	
	
	@ZapcomApi(value="活动编号",remark="活动编号",require=1)
	private String activityCode="";
	
	@ZapcomApi(value="手机号",remark="手机号",require=1)
	private String mobile="";
	
	@ZapcomApi(value="是否校验",remark="是否可以重复领(1:不可以，2：可以)",require=1)
	private String validateFlag = "";

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(String validateFlag) {
		this.validateFlag = validateFlag;
	}
	
}
