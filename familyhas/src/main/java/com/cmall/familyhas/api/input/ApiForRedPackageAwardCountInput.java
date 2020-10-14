package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForRedPackageAwardCountInput extends RootInput {
	
	/**
	 * 当前活动编号
	 */
	@ZapcomApi(value="活动编号",remark="",require=1)
	private String eventCode = "";
	@ZapcomApi(value="数红包次数",remark="",require=1)
	private Integer count = 0;
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

}
