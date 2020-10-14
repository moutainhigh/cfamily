package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetProductBaseInfoInput extends RootInput {

	@ZapcomApi(value="LD商品编号",require=1,remark="支持多条查询")
	private List<String> productCodes= new ArrayList<String>();

	public List<String> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}
	
	
	
}
