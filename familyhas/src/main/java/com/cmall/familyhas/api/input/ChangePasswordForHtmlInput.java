package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ChangePasswordForHtmlInput extends RootInput {

	
	@ZapcomApi(value = "新密码", require = 1, demo = "123456", remark = "新密码，长度为6-40位，支持特殊字符。", verify = {
			"minlength=6", "maxlength=40" })
	private String new_password="";

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	
	
	
}
