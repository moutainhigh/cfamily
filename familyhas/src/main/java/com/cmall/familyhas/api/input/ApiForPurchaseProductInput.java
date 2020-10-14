package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForPurchaseProductInput extends RootInput{

	@ZapcomApi(value="skuCodes",require=1)
	private String skuCodes ="";

	public String getSkuCodes() {
		return skuCodes;
	}

	public void setSkuCodes(String skuCodes) {
		this.skuCodes = skuCodes;
	}

}
