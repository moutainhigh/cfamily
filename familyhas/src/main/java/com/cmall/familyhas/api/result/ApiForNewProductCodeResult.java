package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForNewProductCodeResult extends RootResult {
	
	@ZapcomApi(value = "提供的商品编号")
	private String productCode = "";
	@ZapcomApi(value = "复制后的商品编号", remark = "如果为空则表示未查询到复制的商品")
	private String productCodeCopy = "";
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductCodeCopy() {
		return productCodeCopy;
	}
	public void setProductCodeCopy(String productCodeCopy) {
		this.productCodeCopy = productCodeCopy;
	}
}
