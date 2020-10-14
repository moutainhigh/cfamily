package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class UpdateLDProductPicInput extends RootInput{
	@ZapcomApi(value="商品编码",remark = "可为空", demo = "")
	private String productCode="";
	
	@ZapcomApi(value="时间戳1",remark = "可为空", demo = "")
	private String timestamp1="";
	
	@ZapcomApi(value="时间戳2",remark = "可为空", demo = "")
	private String timestamp2="";

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTimestamp1() {
		return timestamp1;
	}

	public void setTimestamp1(String timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public String getTimestamp2() {
		return timestamp2;
	}

	public void setTimestamp2(String timestamp2) {
		this.timestamp2 = timestamp2;
	}
	
	
}
