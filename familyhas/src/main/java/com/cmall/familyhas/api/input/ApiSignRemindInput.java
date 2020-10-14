package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiSignRemindInput extends RootInput{
	
	@ZapcomApi(value = "签到提醒标记", remark = "1为提醒，0为不提醒",require  = 1)
	private String remindFlag =  "0";

	public String getRemindFlag() {
		return remindFlag;
	}

	public void setRemindFlag(String remindFlag) {
		this.remindFlag = remindFlag;
	}
	
	
}
