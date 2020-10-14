package com.cmall.familyhas.api.result;

import java.util.Map;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiVipExclusiveGoodResult extends RootResult {
	@ZapcomApi(value="加价购数据")
	private Map<String, Object> priceUpData;
	@ZapcomApi(value="积分兑换数据")
	private Map<String, Object> pointData;
	@ZapcomApi(value="会员尊享数据")
	private Map<String, Object> bargainData;
	@ZapcomApi(value="活动数据")
	private Map<String, Object> activityData;
	@ZapcomApi(value="视频数据")
	private Map<String, Object> videoData;
	
	public Map<String, Object> getPriceUpData() {
		return priceUpData;
	}
	public void setPriceUpData(Map<String, Object> priceUpData) {
		this.priceUpData = priceUpData;
	}
	
	public Map<String, Object> getPointData() {
		return pointData;
	}
	public void setPointData(Map<String, Object> pointData) {
		this.pointData = pointData;
	}
	
	public Map<String, Object> getBargainData() {
		return bargainData;
	}
	public void setBargainData(Map<String, Object> bargainData) {
		this.bargainData = bargainData;
	}
	
	public Map<String, Object> getActivityData() {
		return activityData;
	}
	public void setActivityData(Map<String, Object> activityData) {
		this.activityData = activityData;
	}
	
	public Map<String, Object> getVideoData() {
		return videoData;
	}
	public void setVideoData(Map<String, Object> videoData) {
		this.videoData = videoData;
	}
}
