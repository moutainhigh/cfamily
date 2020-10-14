package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class FarmSign {

	@ZapcomApi(value = "签到编号")
	private String signCode = "";
	
	@ZapcomApi(value = "签到天数描述",remark = "每天描述不同")
	private String signDay = "";
	
	@ZapcomApi(value = "赠送水滴数")
	private int signWater = 0;

	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public String getSignDay() {
		return signDay;
	}

	public void setSignDay(String signDay) {
		this.signDay = signDay;
	}

	public int getSignWater() {
		return signWater;
	}

	public void setSignWater(int signWater) {
		this.signWater = signWater;
	}
	
	
}
