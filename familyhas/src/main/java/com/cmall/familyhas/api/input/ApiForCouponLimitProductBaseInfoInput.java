package com.cmall.familyhas.api.input;


import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCouponLimitProductBaseInfoInput extends RootInput{
	@ZapcomApi(value = "商品编号",require=1)
	private String productCodeArr = "";

	public String getProductCodeArr() {
		return productCodeArr;
	}

	public void setProductCodeArr(String productCodeArr) {
		this.productCodeArr = productCodeArr;
	}
	
}
