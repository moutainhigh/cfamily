package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiForCheckOldUserInput extends RootInput {

	@ZapcomApi(value = "手机号", remark = "手机号", demo = "18801010101")
	private String mobile = "";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
