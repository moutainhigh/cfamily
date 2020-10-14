package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForRedPackageAwardInput extends RootInput {
	/**
	 * 当前活动编号
	 */
	@ZapcomApi(value="活动编号",remark="",require=1)
	private String eventCode = "";

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	

}
