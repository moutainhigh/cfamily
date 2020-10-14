package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetProductInfoInput extends RootInput{
	
	
	@ZapcomApi(value="商品编号",remark="多个用“,”号拼接",require=1)
	private String productCodes = "";

	@ZapcomApi(value="用户编号")
	private String memberCode = "";

	public String getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(String productCodes) {
		this.productCodes = productCodes;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	
}
