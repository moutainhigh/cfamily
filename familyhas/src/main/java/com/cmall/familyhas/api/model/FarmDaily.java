package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class FarmDaily {

	@ZapcomApi(value = "创建时间(年月日)")
	private String createTime = "";
	
	@ZapcomApi(value = "动态描述")
	private String description = "";
	
	@ZapcomApi(value = "别人的用户编号")
	private String otherMemberCode = "";
	
	@ZapcomApi(value = "别人的昵称")
	private String otherNickname = "";
	
	@ZapcomApi(value = "水滴克数(获取为+; 减少为-)")
	private String waterNum = "";

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOtherMemberCode() {
		return otherMemberCode;
	}

	public void setOtherMemberCode(String otherMemberCode) {
		this.otherMemberCode = otherMemberCode;
	}

	public String getOtherNickname() {
		return otherNickname;
	}

	public void setOtherNickname(String otherNickname) {
		this.otherNickname = otherNickname;
	}

	public String getWaterNum() {
		return waterNum;
	}

	public void setWaterNum(String waterNum) {
		this.waterNum = waterNum;
	}
	
}
