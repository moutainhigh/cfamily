package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Title;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class DaTiSearchResult extends RootResult{
	@ZapcomApi(value="当前是否有活动",remark="0为无活动，1为有活动")
	private int hasActivity = 0;
	
	@ZapcomApi(value="用户是否已参加当前进行的活动",remark="0为已参加，1为未参加")
	private int isJoin = 0;
	
	@ZapcomApi(value="题目",remark="题目")
	private List<Title> titles = new ArrayList<>();
	
	@ZapcomApi(value="当前活动编号",remark="")
	private String eventCode = "";



	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public int getHasActivity() {
		return hasActivity;
	}

	public void setHasActivity(int hasActivity) {
		this.hasActivity = hasActivity;
	}

	public int getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(int isJoin) {
		this.isJoin = isJoin;
	}

	public List<Title> getTitles() {
		return titles;
	}

	public void setTitles(List<Title> titles) {
		this.titles = titles;
	}

	

	
	
	
	
}
