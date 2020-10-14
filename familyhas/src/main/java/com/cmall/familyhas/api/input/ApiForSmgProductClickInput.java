package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForSmgProductClickInput extends RootInput {

	@ZapcomApi(value="节目单编号",require=1)
	private String formId = "";
	
	@ZapcomApi(value="商品编号",require=1)
	private String productCode = "";
	
	@ZapcomApi(value="微信唯一编号",require=1)
	private String unionId = "";

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	
}
