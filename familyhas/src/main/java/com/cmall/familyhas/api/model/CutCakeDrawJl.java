package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CutCakeDrawJl {

	@ZapcomApi(value="奖品名称")
	private String jpName = "";
	
	@ZapcomApi(value = "奖品类型", remark = "0:积分; 1:优惠券; 2:特等奖，3：一等奖")
	private String jpType = "";
	
	@ZapcomApi(value="抽奖时间")
	private String createTime = "";
	
	@ZapcomApi(value="用户昵称")
	private String nickName = "";

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getJpType() {
		return jpType;
	}

	public void setJpType(String jpType) {
		this.jpType = jpType;
	}

	public String getJpName() {
		return jpName;
	}

	public void setJpName(String jpName) {
		this.jpName = jpName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
