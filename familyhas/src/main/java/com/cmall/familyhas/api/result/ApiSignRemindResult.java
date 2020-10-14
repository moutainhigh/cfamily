package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiSignRemindResult extends RootResultWeb{
	@ZapcomApi(value = "用户切换签到提醒结果", remark = "用户切换签到提醒结果")
	private String toggleResult;

	public String getToggleResult() {
		return toggleResult;
	}

	public void setToggleResult(String toggleResult) {
		this.toggleResult = toggleResult;
	}
	
}
