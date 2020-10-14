package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetShopCarAndCustAmtForCrmInput extends RootInput {

	@ZapcomApi(value = "用户custId")
	private String custId = "";
	
	@ZapcomApi(value = "用户memberCode")
	private String memberCode = "";

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	
	
}
