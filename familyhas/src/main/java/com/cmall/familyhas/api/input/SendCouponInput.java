package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class SendCouponInput extends RootInput {

	@ZapcomApi(value = "用户编码", require = 1, remark = "用户编码,逗号隔开")
	private String memberCodes = "";
	@ZapcomApi(value = "活动编号", require = 1, remark = "")
	private String activityCode = "";
	@ZapcomApi(value = "操作编号")
	private String operate_id ;
	
	public String getOperate_id() {
		return operate_id;
	}
	public void setOperate_id(String operate_id) {
		this.operate_id = operate_id;
	}
	public String getMemberCodes() {
		return memberCodes;
	}
	public void setMemberCodes(String memberCodes) {
		this.memberCodes = memberCodes;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}	
}
