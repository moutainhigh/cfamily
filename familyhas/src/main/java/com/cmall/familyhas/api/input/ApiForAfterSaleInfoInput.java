package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAfterSaleInfoInput extends RootInput {

	@ZapcomApi(value = "售后单号", require = 1, demo = "RTG140123100003")
	private String afterCode = "";

	public String getAfterCode() {
		return afterCode;
	}

	public void setAfterCode(String afterCode) {
		this.afterCode = afterCode;
	}
	
}
