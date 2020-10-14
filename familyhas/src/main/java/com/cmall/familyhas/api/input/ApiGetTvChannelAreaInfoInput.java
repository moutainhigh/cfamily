package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetTvChannelAreaInfoInput extends RootInput{
	
	@ZapcomApi(value = "地区编码", remark = "地区编码", demo = "110111", require = 1)
	public String areaCode = "";

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}	
	
}
