package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForRedPackageEventResult extends RootResult {

	/**
	 * 活动编号eventCode
	 */
	@ZapcomApi(value="返回活动编号",remark="当前时段的活动编号")
	private String eventCode = "";

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
}
