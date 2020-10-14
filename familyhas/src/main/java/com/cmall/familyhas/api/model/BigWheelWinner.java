package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class BigWheelWinner {

	@ZapcomApi(value="中奖人昵称")
	private String nickname="";
	
	@ZapcomApi(value="奖品名称")
	private String jpTitle="";

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getJpTitle() {
		return jpTitle;
	}

	public void setJpTitle(String jpTitle) {
		this.jpTitle = jpTitle;
	}
	
	
}
