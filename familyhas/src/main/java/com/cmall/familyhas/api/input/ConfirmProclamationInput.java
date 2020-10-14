package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ConfirmProclamationInput extends RootInput{
	private String userCode;
	private String proclamationCode;
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getProclamationCode() {
		return proclamationCode;
	}
	public void setProclamationCode(String proclamationCode) {
		this.proclamationCode = proclamationCode;
	}
	
}
