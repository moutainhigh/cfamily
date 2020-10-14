package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCouponCodeExchangeInput extends RootInput{
	@ZapcomApi(value="优惠码",remark="优惠码",require=1)
	private String couponCode="";

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

}
