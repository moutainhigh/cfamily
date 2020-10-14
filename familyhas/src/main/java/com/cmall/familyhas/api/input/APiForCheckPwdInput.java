package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiForCheckPwdInput extends RootInput {

	@ZapcomApi(value = "登录密码", require=1, remark = "登录密码", demo = "123456")
	private String password = "";

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
