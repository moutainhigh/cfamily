package com.cmall.familyhas.api.game.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiForGetTaskInfoResult {
	
	@ZapcomApi(value="任务类型",remark="4497471600530001：邀请好友喂青蛙，4497471600530002：邀请好友下载APP，4497471600530003：下单购买，4497471600530004：浏览X分钟",demo="4497471600530001")
	private String taskType = "4497471600530001";
	
	@ZapcomApi(value="任务描述",remark="邀请好友喂青蛙",demo="邀请好友喂青蛙")
	private String taskDesc = "";
	
	@ZapcomApi(value="任务完成状态",remark="4497471600540001：去完成，4497471600540002：可领取，4497471600540003：已完成",demo="4497471600540001")
	private String taskStatus = "";
	
	@ZapcomApi(value="奖励次数",remark="完成任务可领取游戏次数",demo="2")
	private String gameTimes = "";
	
	@ZapcomApi(value="重复次数",remark="可重复完成次数",demo="1")
	private String repeatTimes = "";

	public String getGameTimes() {
		return gameTimes;
	}

	public void setGameTimes(String gameTimes) {
		this.gameTimes = gameTimes;
	}

	public String getRepeatTimes() {
		return repeatTimes;
	}

	public void setRepeatTimes(String repeatTimes) {
		this.repeatTimes = repeatTimes;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	

}
