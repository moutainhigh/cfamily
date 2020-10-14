package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForSaveAddressAndLoginResult extends RootResultWeb{
	
	@ZapcomApi(value = "用户认证串", remark = "注册成功后返回非空，同登陆成功的token")
	private String userToken = "";

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}
