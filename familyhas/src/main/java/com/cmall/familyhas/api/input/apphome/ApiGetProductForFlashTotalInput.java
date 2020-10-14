package com.cmall.familyhas.api.input.apphome;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetProductForFlashTotalInput extends RootInput {

	@ZapcomApi(value="商品编号",require=1)
	private String product_code="";
	
	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	
}
	