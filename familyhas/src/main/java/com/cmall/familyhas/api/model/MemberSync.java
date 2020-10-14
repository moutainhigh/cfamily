package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class MemberSync {

	@ZapcomApi(value = "用户手机号")
	private String mobile="";
	
	@ZapcomApi(value = "用户昵称")
	private String nickname="";
	
	@ZapcomApi(value = "用户头像")
	private String avatar="";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
}
