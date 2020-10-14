package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiPredictMoney {
	@ZapcomApi(value="今日收益")
	private String todayMoney = "0";
	@ZapcomApi(value="分销收益")
	private String fxMoney = "0";
	@ZapcomApi(value="粉丝分销收益")
	private String fsMoney = "0";
	public String getTodayMoney() {
		return todayMoney;
	}
	public void setTodayMoney(String todayMoney) {
		this.todayMoney = todayMoney;
	}
	public String getFxMoney() {
		return fxMoney;
	}
	public void setFxMoney(String fxMoney) {
		this.fxMoney = fxMoney;
	}
	public String getFsMoney() {
		return fsMoney;
	}
	public void setFsMoney(String fsMoney) {
		this.fsMoney = fsMoney;
	}

	
}
