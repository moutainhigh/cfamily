package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HudongEventInfo {

	@ZapcomApi(value = "活动编号")
	private String eventCode;
	
	@ZapcomApi(value = "活动名称")
	private String eventName;
	
	@ZapcomApi(value = "开始时间")
	private String beginTime;
	
	@ZapcomApi(value = "结束时间")
	private String endTime;
	
	@ZapcomApi(value = "活动类型", remark = "449748210004:福利大转盘")
	private String eventTypeCode;
	
	@ZapcomApi(value = "活动状态", remark = "4497472700020002:已发布")
	private String eventStatus;
	
	@ZapcomApi(value = "大转盘分享标题")
	private String dzpShareTitle;
	
	@ZapcomApi(value = "大转盘分享描述")
	private String dzpShareDesc;

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEventTypeCode() {
		return eventTypeCode;
	}

	public void setEventTypeCode(String eventTypeCode) {
		this.eventTypeCode = eventTypeCode;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getDzpShareTitle() {
		return dzpShareTitle;
	}

	public void setDzpShareTitle(String dzpShareTitle) {
		this.dzpShareTitle = dzpShareTitle;
	}

	public String getDzpShareDesc() {
		return dzpShareDesc;
	}

	public void setDzpShareDesc(String dzpShareDesc) {
		this.dzpShareDesc = dzpShareDesc;
	}
	
}
