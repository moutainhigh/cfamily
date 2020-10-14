package com.cmall.familyhas.api.model;

import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class DailyWinningInformation {

	@ZapcomApi(value="中奖日期")
	private String  zjTime;
	
	@ZapcomApi(value="中奖信息")
	private List<HuodongEventJl> list;

	public String getZjTime() {
		return zjTime;
	}

	public void setZjTime(String zjTime) {
		this.zjTime = zjTime;
	}

	public List<HuodongEventJl> getList() {
		return list;
	}

	public void setList(List<HuodongEventJl> list) {
		this.list = list;
	}

	
}
