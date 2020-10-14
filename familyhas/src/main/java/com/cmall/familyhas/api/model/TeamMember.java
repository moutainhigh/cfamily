package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 积分战队队员
 * @remark 
 * @author 任宏斌
 * @date 2019年3月14日
 */
public class TeamMember {

	@ZapcomApi(value="用户编号",remark="用户编号")
	private String member_code;
	
	@ZapcomApi(value="用户头像")
	private String avatar;
	
	@ZapcomApi(value="用户昵称")
	private String nickname;

	public String getMember_code() {
		return member_code;
	}

	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}

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
	
	
}
