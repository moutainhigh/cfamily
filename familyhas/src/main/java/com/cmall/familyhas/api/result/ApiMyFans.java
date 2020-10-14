package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiMyFans {
	@ZapcomApi(value="今日新增粉丝")
	private String todayFans = "0";
	@ZapcomApi(value="全部粉丝")
	private String allFans = "0";
	public String getTodayFans() {
		return todayFans;
	}
	public void setTodayFans(String todayFans) {
		this.todayFans = todayFans;
	}
	public String getAllFans() {
		return allFans;
	}
	public void setAllFans(String allFans) {
		this.allFans = allFans;
	}


	
}
