package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCouponTypeExchangeInput extends RootInput {
	
	@ZapcomApi(value="优惠券编号", remark="", require = 1)
	private String couponTypeCode = "";
	@ZapcomApi(value="优惠券UID", remark="", require = 1)
	private String uid = "";
	
	public String getCouponTypeCode() {
		return couponTypeCode;
	}
	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
