package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class Friend {

	@ZapcomApi(value = "昵称")
	private String nickName = "";
	
	@ZapcomApi(value = "头像")
	private String avatar = "";
	
	@ZapcomApi(value = "是否可偷取标识", remark = "0否 ; 1是")
	private String stealFlag = "0";
	
	@ZapcomApi(value = "好友用户编号")
	private String friendMemberCode = "";

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getStealFlag() {
		return stealFlag;
	}

	public void setStealFlag(String stealFlag) {
		this.stealFlag = stealFlag;
	}

	public String getFriendMemberCode() {
		return friendMemberCode;
	}

	public void setFriendMemberCode(String friendMemberCode) {
		this.friendMemberCode = friendMemberCode;
	}
	
}
