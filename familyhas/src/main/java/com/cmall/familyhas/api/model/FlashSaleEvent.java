package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class FlashSaleEvent {

	@ZapcomApi(value="时间",remark="秒杀开始时间",demo="10:00")
	private String startTime="";
	@ZapcomApi(value="秒杀状态",remark="秒杀状态：（1：已开抢），（2：抢购中），（3：即将开抢），（4：明日开抢）",demo="2")
	private String status="2";
	@ZapcomApi(value="选中状态",remark="选中状态：（0：未选中），（1：选中）",demo="1")
	private String checkStatus="0";
	@ZapcomApi(value="秒杀活动编号",remark="获取秒杀列表时续入参此编号",demo="CX2015072700005")
	private String eventCode="";
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
	
}
