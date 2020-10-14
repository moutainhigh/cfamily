package com.cmall.familyhas.move.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class UserResult extends RootResultWeb {

	@ZapcomApi(value="用户编号")
	private String memberCode="";
	
	@ZapcomApi(value="用户token")
	private String token="";

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
	
	
	
}
