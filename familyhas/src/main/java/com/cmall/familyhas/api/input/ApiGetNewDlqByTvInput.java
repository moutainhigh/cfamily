package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetNewDlqByTvInput extends RootInput{

	@ZapcomApi(value="tv编号",require=1)
	private String tvNumber = "";

	@ZapcomApi(value="来源",require=1)
	private String pType = "";
	
	public String getTvNumber() {
		return tvNumber;
	}

	public void setTvNumber(String tvNumber) {
		this.tvNumber = tvNumber;
	}

	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}
	
	
}
