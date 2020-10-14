package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGiveDkIntegralResult extends RootResultWeb {
	
	@ZapcomApi(value = "奖励积分")
	private String integral;

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}		


}
