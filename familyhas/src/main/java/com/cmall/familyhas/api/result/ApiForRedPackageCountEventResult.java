package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForRedPackageCountEventResult extends RootResult {

	/**
	 * 活动编号eventCode
	 */
	@ZapcomApi(value="返回活动编号",remark="当前时段的活动编号")
	private String eventCode = "";
	
	/**
	 * 倒计时时间
	 */
	@ZapcomApi(value="返回倒计时时间",remark="当前时段数红包的倒计时时间")
	private Integer  countDown = 30;
	/**
	 * 可玩次数
	 */
	@ZapcomApi(value="剩余数红包次数",remark="剩余数红包次数")
	private Integer  times = 0;
	
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getCountDown() {
		return countDown;
	}

	public void setCountDown(Integer countDown) {
		this.countDown = countDown;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
}
