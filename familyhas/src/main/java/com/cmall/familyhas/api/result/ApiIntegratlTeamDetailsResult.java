package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.TeamMember;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiIntegratlTeamDetailsResult extends RootResultWeb {
	
	@ZapcomApi(value="战队成员列表",remark="战队成员列表")
	private List<TeamMember> teamList = new ArrayList<TeamMember>();
	
	@ZapcomApi(value="按钮类型",remark="449748330001 邀请好友    449748330002 加入战队")
	private String buttonType = "";
	
	@ZapcomApi(value="按钮状态",remark="0无效 1有效")
	private String buttionStatus = "0";
	
	@ZapcomApi(value="活动编号",remark="活动编号")
	private String eventCode = "";
	
	@ZapcomApi(value="活动开始时间",remark="活动开始时间")
	private String startTime = "";
	
	@ZapcomApi(value="活动结束时间",remark="活动结束时间")
	private String endTime = "";

	public List<TeamMember> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<TeamMember> teamList) {
		this.teamList = teamList;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getButtionStatus() {
		return buttionStatus;
	}

	public void setButtionStatus(String buttionStatus) {
		this.buttionStatus = buttionStatus;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
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
}