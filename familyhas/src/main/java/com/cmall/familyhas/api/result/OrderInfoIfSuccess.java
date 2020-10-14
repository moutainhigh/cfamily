package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderInfoIfSuccess {
	
	@ZapcomApi(value="订单号",remark="DD......")
	private String orderCode = "";


	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}
