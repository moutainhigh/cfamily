package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 惠惠农场任务
 * @remark 
 * @author 任宏斌
 * @date 2020年2月10日
 */
public class FarmTask {

	@ZapcomApi(value = "任务编号")
	private String taskCode = "";
	@ZapcomApi(value = "任务名称")
	private String taskName = "";
	@ZapcomApi(value = "任务描述")
	private String taskDescription = "";
	@ZapcomApi(value = "任务类别")
	private String taskType = "";
	@ZapcomApi(value = "任务次数")
	private int taskNum = 0;
	@ZapcomApi(value = "完成次数")
	private int alreadyNum = 0;
	@ZapcomApi(value = "奖励水滴数")
	private int awardWaterNum = 0;
	@ZapcomApi(value = "浏览秒数")
	private int browseSecond = 0;
	@ZapcomApi(value = "待浏览商品编号", remark = "用户浏览商品任务")
	private String productCode = "";
	@ZapcomApi(value = "待浏览专题连接", remark = "用户浏览专题任务")
	private String pageLink = "";
	@ZapcomApi(value = "按钮完成状态", remark = "0:去完成 1:已完成 2:领取")
	private String buttonStatus = "";
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public int getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	public int getAlreadyNum() {
		return alreadyNum;
	}
	public void setAlreadyNum(int alreadyNum) {
		this.alreadyNum = alreadyNum;
	}
	public int getAwardWaterNum() {
		return awardWaterNum;
	}
	public void setAwardWaterNum(int awardWaterNum) {
		this.awardWaterNum = awardWaterNum;
	}
	public int getBrowseSecond() {
		return browseSecond;
	}
	public void setBrowseSecond(int browseSecond) {
		this.browseSecond = browseSecond;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPageLink() {
		return pageLink;
	}
	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}
	public String getButtonStatus() {
		return buttonStatus;
	}
	public void setButtonStatus(String buttonStatus) {
		this.buttonStatus = buttonStatus;
	}

}
