package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiProdGoodEvaluationInfoInput extends RootInput {

	@ZapcomApi(value="商品编号",remark="查询商品评价和详情",require=1)
	private String product_code = "";

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	
}
