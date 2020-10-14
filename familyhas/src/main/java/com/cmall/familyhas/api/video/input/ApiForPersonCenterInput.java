package com.cmall.familyhas.api.video.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForPersonCenterInput extends RootInput {

	@ZapcomApi(value="用户编号",require=1)
	private String memberCode="";

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}	
}
	