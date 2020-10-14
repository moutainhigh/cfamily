package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiIssueCouponForWeChatBoundUserInput extends RootInput{

	private String validate_code ="";
	
	private String member_code ="";
	

	public String getMember_code() {
		return member_code;
	}

	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}

	public String getValidate_code() {
		return validate_code;
	}

	public void setValidate_code(String validate_code) {
		this.validate_code = validate_code;
	}
    

}
