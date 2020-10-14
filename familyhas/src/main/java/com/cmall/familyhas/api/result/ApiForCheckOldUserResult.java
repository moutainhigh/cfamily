package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForCheckOldUserResult extends RootResultWeb {
	
	@ZapcomApi(value = "面额")
	private String money="";
	
	@ZapcomApi(value = "优惠券使用开始时间")
	private String startTime="";
	
	@ZapcomApi(value = "优惠券使用结束时间")
	private String endTime="";

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
