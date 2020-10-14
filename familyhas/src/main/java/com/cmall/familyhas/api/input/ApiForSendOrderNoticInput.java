package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForSendOrderNoticInput extends RootInput {
	
	@ZapcomApi(value="订单号",remark="通知支付成功订单号",require=1)
	private String orderCode = "";
	
	@ZapcomApi(value="需要通知的微信openId",remark="需要通知的微信openId",require=1)
	private String openId = "";
	

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	
}
