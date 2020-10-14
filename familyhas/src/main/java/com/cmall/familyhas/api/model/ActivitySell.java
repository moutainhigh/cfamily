package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 促销活动
 * @author 李国杰
 *
 */
public class ActivitySell {

	@ZapcomApi(value = "活动编号")
	private String activityCode = "";

	@ZapcomApi(value = "活动名称")
	private String activityName = "";

	@ZapcomApi(value = "活动描述")
	private String remark = "";

	@ZapcomApi(value = "活动价")
	private BigDecimal activityPrice = new BigDecimal(0.00);
	
	@ZapcomApi(value = "开始时间")
	private String startTime = "";

	@ZapcomApi(value = "结束时间")
	private String endTime = "";
	
	@ZapcomApi(value="是否闪购",remark="1:是；0:否")
	private int flagCheap = 0; 
	
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getActivityPrice() {
		return activityPrice;
	}
	public void setActivityPrice(BigDecimal activityPrice) {
		this.activityPrice = activityPrice;
	}
	public int getFlagCheap() {
		return flagCheap;
	}
	public void setFlagCheap(int flagCheap) {
		this.flagCheap = flagCheap;
	}
	
}
