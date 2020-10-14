package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiTestLDForYWInput extends RootInput {

	@ZapcomApi(value = "商品编号", remark = "不可为空", demo = "131833",require=1)
	private String skuCode = "";

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
}
