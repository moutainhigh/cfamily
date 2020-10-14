package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGiveDkIntegralInput extends RootInput{
	@ZapcomApi(value="活动编码",remark="活动编码",require=1)
	private String eventCode;
	@ZapcomApi(value="签到天数",remark="本次签到是第几天",require=1)
	private int jfDay;
	@ZapcomApi(value="奖励积分",remark="奖励积分数量",require=1)
	private int jfNum;
	
	
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public int getJfDay() {
		return jfDay;
	}
	public void setJfDay(int jfDay) {
		this.jfDay = jfDay;
	}
	public int getJfNum() {
		return jfNum;
	}
	public void setJfNum(int jfNum) {
		this.jfNum = jfNum;
	}

	
}
