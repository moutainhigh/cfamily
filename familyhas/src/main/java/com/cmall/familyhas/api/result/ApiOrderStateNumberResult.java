package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiOrderStateNumberResult{
	@ZapcomApi(value="订单状态编号")
	private String orderStatus = "";
	@ZapcomApi(value="该状态下数量")
	private String number = "0";
	@ZapcomApi(value="订单状态名称")
	private String statusCode = "";

	
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
}
