package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class APiForCouponTypeExchangeResult extends RootResultWeb {
	
	@ZapcomApi(value="优惠券编号", remark="")
	private String couponTypeCode;

	public String getCouponTypeCode() {
		return couponTypeCode;
	}

	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
	}
	
}
