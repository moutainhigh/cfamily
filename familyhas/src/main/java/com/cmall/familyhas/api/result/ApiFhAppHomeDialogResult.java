package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * @descriptions 3.9.6版本 App首页弹窗 - 广告弹窗 - 返回信息实体
 * 
 * @author Yangcl
 * @date 2016-5-5-下午5:05:02
 * @version 1.0.0
 */
public class ApiFhAppHomeDialogResult extends RootResult {
	
	@ZapcomApi(value = "活动开始时间")
	private String startTime = "";
	
	@ZapcomApi(value = "活动结束时间")
	private String endTime = "";
	
	@ZapcomApi(value = "系统当前时间")
	private String curentTime = "";
	
	@ZapcomApi(value = "页面url")
	private String eventUrl = "";
	
	@ZapcomApi(value = "弹出次数")
	private int popCount = 0;
	
	private boolean flag = false;  // 标识这条实体记录是否已经赋值成功。

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

	public String getCurentTime() {
		return curentTime;
	}

	public void setCurentTime(String curentTime) {
		this.curentTime = curentTime;
	}

	public String getEventUrl() {
		return eventUrl;
	}

	public void setEventUrl(String eventUrl) {
		this.eventUrl = eventUrl;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getPopCount() {
		return popCount;
	}

	public void setPopCount(int popCount) {
		this.popCount = popCount;
	}
	
}
