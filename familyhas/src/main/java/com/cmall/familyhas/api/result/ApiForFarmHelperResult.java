package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFarmHelperResult extends RootResult {

	@ZapcomApi(value = "助力人头像集合")
	private List<String> avatarList = new ArrayList<String>();
	
	@ZapcomApi(value = "每人助力获取得雨露提示语")
	private String helpMessage = "";
	
	@ZapcomApi(value = "邀请新用户助力且种树提示语")
	private String newHelpMessage = "";

	public String getHelpMessage() {
		return helpMessage;
	}

	public void setHelpMessage(String helpMessage) {
		this.helpMessage = helpMessage;
	}

	public String getNewHelpMessage() {
		return newHelpMessage;
	}

	public void setNewHelpMessage(String newHelpMessage) {
		this.newHelpMessage = newHelpMessage;
	}

	public List<String> getAvatarList() {
		return avatarList;
	}

	public void setAvatarList(List<String> avatarList) {
		this.avatarList = avatarList;
	}
	
}
