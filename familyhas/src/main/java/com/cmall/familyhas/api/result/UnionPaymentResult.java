package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class UnionPaymentResult {
	@ZapcomApi(value="支付链接",remark="")
	private String payUrl = "";

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	
	
}
