package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiFXOrders {
	@ZapcomApi(value="今日订单")
	private String todayOrders = "0";
	@ZapcomApi(value="全部")
	private String allOrders = "0";
	public String getTodayOrders() {
		return todayOrders;
	}
	public void setTodayOrders(String todayOrders) {
		this.todayOrders = todayOrders;
	}
	public String getAllOrders() {
		return allOrders;
	}
	public void setAllOrders(String allOrders) {
		this.allOrders = allOrders;
	}
	

	
}
