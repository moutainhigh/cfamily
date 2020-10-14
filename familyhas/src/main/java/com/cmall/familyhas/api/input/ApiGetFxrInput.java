package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetFxrInput extends RootInput{
	
	@ZapcomApi(value = "微信号", remark = "微信号", demo = "13133111133", require = 1)
	public String wxCode = "";

	public String getWxCode() {
		return wxCode;
	}

	public void setWxCode(String wxCode) {
		this.wxCode = wxCode;
	}

	
	
}
