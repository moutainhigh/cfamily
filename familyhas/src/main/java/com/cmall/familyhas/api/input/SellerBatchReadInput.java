package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class SellerBatchReadInput extends RootInput{
	private String  newsCodes  ="";
	private  String  userCode  =  "";
	public String getNewsCodes() {
		return newsCodes;
	}
	public void setNewsCodes(String newsCodes) {
		this.newsCodes = newsCodes;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
}
