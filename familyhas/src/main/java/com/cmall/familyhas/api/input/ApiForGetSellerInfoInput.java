package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 查询商户信息输入参数
 */
public class ApiForGetSellerInfoInput extends RootInput {

	@ZapcomApi(value = "商品编号" ,demo= "",require = 1)
	private String productCode = "";

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
