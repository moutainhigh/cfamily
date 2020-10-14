package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class HongbaoyuSearchResult extends RootResultWeb{
	
	@ZapcomApi(value="当前活动状态",remark="0为未开始,1为进行中,2为已结束,3为倒计时")
	private Integer actState = 0;//
	
	@ZapcomApi(value="距离活动结束时间",remark="此字段在活动进行中状态时有效")
	private long endSeconds = 0;//
	
	@ZapcomApi(value="距离下一活动开始时间",remark="距离下一档开始时间")
	private long nextActSeconds = 0;//
	
	@ZapcomApi(value="下一活动开始时间",remark="下一档开始时间")
	private String nextActTime = "";//

	public String getNextActTime() {
		return nextActTime;
	}

	public void setNextActTime(String nextActTime) {
		this.nextActTime = nextActTime;
	}

	public Integer getActState() {
		return actState;
	}

	public void setActState(Integer actState) {
		this.actState = actState;
	}

	public long getEndSeconds() {
		return endSeconds;
	}

	public void setEndSeconds(long endSeconds) {
		this.endSeconds = endSeconds;
	}

	public long getNextActSeconds() {
		return nextActSeconds;
	}

	public void setNextActSeconds(long nextActSeconds) {
		this.nextActSeconds = nextActSeconds;
	}
	
	
	
}
