package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForBigWheelInviteSuccessInput extends RootInput {

	@ZapcomApi(value = "邀请人用户编号")
	private String memberCodeYqr="";
	
	@ZapcomApi(value = "活动编号")
	private String eventCode="";
	
	//@ZapcomApi(value = "被邀请人用户编号")
	//private String memberCodeByqr="";

	public String getMemberCodeYqr() {
		return memberCodeYqr;
	}

	public void setMemberCodeYqr(String memberCodeYqr) {
		this.memberCodeYqr = memberCodeYqr;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
	
}
