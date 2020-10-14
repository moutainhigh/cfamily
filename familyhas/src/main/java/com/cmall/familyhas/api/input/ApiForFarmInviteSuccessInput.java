package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForFarmInviteSuccessInput extends RootInput {

	@ZapcomApi(value = "邀请人用户编号",require = 1)
	private String memberCodeYqr="";

	public String getMemberCodeYqr() {
		return memberCodeYqr;
	}

	public void setMemberCodeYqr(String memberCodeYqr) {
		this.memberCodeYqr = memberCodeYqr;
	}
	
}
