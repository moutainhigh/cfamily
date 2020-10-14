package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 分销粉丝
 */
public class ApiForAgentQueryFansInfo {
	
	@ZapcomApi(value="头像")
	private String avatar = "";
	@ZapcomApi(value="昵称")
	private String nickname = "";
	@ZapcomApi(value="手机号")
	private String phone = "";
	@ZapcomApi(value="注册时间")
	private String registerTime = "";
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	
}
