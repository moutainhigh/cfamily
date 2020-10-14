package com.cmall.familyhas.mtmanager.inputresult;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForStockPriceInput extends RootInput {

	@ZapcomApi(value = "sku编号", require=1, remark = "", demo = "8019123456")
	private String skuCode = "";

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
}
