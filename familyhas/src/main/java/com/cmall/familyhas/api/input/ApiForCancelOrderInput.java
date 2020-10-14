package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCancelOrderInput extends RootInput {

	@ZapcomApi(value="订单编号", require = 1,remark="取消订单的订单编号",demo="DD123456789")
	private String orderCode = "";

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	
}
