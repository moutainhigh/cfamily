package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class VipActivity {
	@ZapcomApi(value="活动唯一标示")
	private String activityCode;
	@ZapcomApi(value="标题")
	private String title;
	@ZapcomApi(value="图片路径")
	private String picUrl;
	@ZapcomApi(value="活动地址")
	private String activityLocation;
	@ZapcomApi(value="活动时间")
	private String activityTime;
	@ZapcomApi(value="星期")
	private String weekNum;
	@ZapcomApi(value="是否结束")
	private String isFinish;
	
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getActivityLocation() {
		return activityLocation;
	}
	public void setActivityLocation(String activityLocation) {
		this.activityLocation = activityLocation;
	}
	
	public String getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}
	
	public String getWeekNum() {
		return weekNum;
	}
	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}
	
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
}
