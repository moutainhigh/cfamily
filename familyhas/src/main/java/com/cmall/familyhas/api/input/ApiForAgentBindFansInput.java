package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAgentBindFansInput extends RootInput {
	
	@ZapcomApi(value = "邀请人编号", require = 1)
	private String fxrCode = "";

	public String getFxrCode() {
		return fxrCode;
	}

	public void setFxrCode(String fxrCode) {
		this.fxrCode = fxrCode;
	}
	
}
