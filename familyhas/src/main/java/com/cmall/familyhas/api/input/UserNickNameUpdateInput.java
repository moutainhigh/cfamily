package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class UserNickNameUpdateInput extends RootInput{
	@ZapcomApi(value="用户编号",remark="用户编号",demo="MI1409161****0",require=1)
	private String memberCode = "";
	@ZapcomApi(value="昵称",remark="用户更新后的昵称",demo="Hell World")
	private String nickname = "";
	@ZapcomApi(value="头像Url",remark="用户头像",demo="http://qhbeta-cfiles.qhw.srnpr.com/cfiles/990aae2a7.jpg")
	private String memberAvatar = "";
	
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMemberAvatar() {
		return memberAvatar;
	}
	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}  
}
