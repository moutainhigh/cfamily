package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiCallLogInput extends RootInput {

	@ZapcomApi(value="用户手机号", require = 1, verify = {"base=mobile" })
	private String mobile = "";
	@ZapcomApi(value="商品编号",remark="多个商品编号用英文','号拼接",demo="133000,133111")
	private String productCode = "";
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
}
