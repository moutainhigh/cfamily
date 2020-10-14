package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForFarmInviteSuccessNoLoginInput extends RootInput {

	@ZapcomApi(value = "邀请人用户编号",require = 1)
	private String memberCodeYqr = "";
	
	@ZapcomApi(value = "被邀请人用户信息",require = 1)
	private String inviteInfoJson = "";

	public String getMemberCodeYqr() {
		return memberCodeYqr;
	}

	public void setMemberCodeYqr(String memberCodeYqr) {
		this.memberCodeYqr = memberCodeYqr;
	}

	public String getInviteInfoJson() {
		return inviteInfoJson;
	}

	public void setInviteInfoJson(String inviteInfoJson) {
		this.inviteInfoJson = inviteInfoJson;
	}
	
}
