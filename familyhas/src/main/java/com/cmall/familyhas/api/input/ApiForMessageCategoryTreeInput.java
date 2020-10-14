package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;


public class ApiForMessageCategoryTreeInput extends RootInput {

	/**app编号*/
	@ZapcomApi(value = "app编号", require = 1)
	private String app_code="";

	public String getApp_code() {
		return app_code;
	}

	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	
	
}
