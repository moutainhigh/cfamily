package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForRedPackageCountInput extends RootInput {
	/**
	 * 当前活动编号
	 */
	@ZapcomApi(value="活动类型",remark="449748210008：抢红包，449748210009：数红包",require=1)
	private String eventTypeCode = "449748210009";

	public String getEventTypeCode() {
		return eventTypeCode;
	}

	public void setEventTypeCode(String eventTypeCode) {
		this.eventTypeCode = eventTypeCode;
	}

	
	

}
