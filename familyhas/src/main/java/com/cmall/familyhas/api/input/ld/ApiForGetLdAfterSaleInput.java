package com.cmall.familyhas.api.input.ld;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetLdAfterSaleInput extends RootInput{
	@ZapcomApi(value="用户编号",require=1)
	private String memberCode="123456789";

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	
}
