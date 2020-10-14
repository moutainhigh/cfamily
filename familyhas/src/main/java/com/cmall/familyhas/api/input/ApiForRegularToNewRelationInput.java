package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForRegularToNewRelationInput extends RootInput {
	/**
	 * 推荐用户编码
	 */
	@ZapcomApi(value="推荐用户编码",remark="",require=1)
	private String member_code = "";
	/**
	 * 推荐用户编码
	 */
	@ZapcomApi(value="活动编号",remark="",require=1)
	private String event_code = "";

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getMember_code() {
		return member_code;
	}

	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}

}
