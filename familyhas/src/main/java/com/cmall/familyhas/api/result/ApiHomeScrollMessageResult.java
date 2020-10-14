package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ScrollMessage;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiHomeScrollMessageResult extends RootResult {
	
	@ZapcomApi(value = "滚动消息列表")
	private List<ScrollMessage> messageList = new ArrayList<ScrollMessage>();
	@ZapcomApi(value = "每条展示时间", remark = "单位：秒")
	private Integer eachTime = 0;
	@ZapcomApi(value = "间隔时间", remark = "单位：秒")
	private Integer intervalTime = 0;

	public List<ScrollMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<ScrollMessage> messageList) {
		this.messageList = messageList;
	}

	public Integer getEachTime() {
		return eachTime;
	}

	public void setEachTime(Integer eachTime) {
		this.eachTime = eachTime;
	}

	public Integer getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}
}
