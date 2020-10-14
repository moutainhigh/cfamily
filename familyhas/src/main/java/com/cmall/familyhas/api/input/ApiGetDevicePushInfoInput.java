package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetDevicePushInfoInput extends RootInput{

	@ZapcomApi(value="手机号",remark="需要查询的用户手机号，多个用“,”号拼接",require=1)
	private String mobile = "";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}
