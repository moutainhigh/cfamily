package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiRegIsUserInput extends RootInput{
	
	@ZapcomApi(value = "验证的手机号", remark = "用户手机号", demo = "13133111133", require = 1,verify = {"regex=^1[0-9]{10}$" })
	public String mobile = "";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
