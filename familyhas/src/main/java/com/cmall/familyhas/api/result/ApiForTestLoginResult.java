package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 
 * @author xiegj
 * 短信验证登录输出类
 */
public class ApiForTestLoginResult extends RootResult {

	@ZapcomApi(value = "用户认证串", remark = "登陆成功后返回非空，用于需要用户授权api_token的操作")
	private String userToken = "";

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
