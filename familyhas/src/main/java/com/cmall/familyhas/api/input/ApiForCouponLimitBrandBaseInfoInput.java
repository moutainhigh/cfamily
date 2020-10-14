package com.cmall.familyhas.api.input;


import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCouponLimitBrandBaseInfoInput extends RootInput{
	@ZapcomApi(value = "品牌编号",require=1)
	private String brandCodeArr = "";

	public String getBrandCodeArr() {
		return brandCodeArr;
	}

	public void setBrandCodeArr(String brandCodeArr) {
		this.brandCodeArr = brandCodeArr;
	}
	
}
