package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HudongEventInfo;
import com.cmall.familyhas.api.model.HuodongEventDzpjpRule;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForBigWheelHomeResult extends RootResult {

	@ZapcomApi(value = "大转盘活动")
	private HudongEventInfo hudongEventInfo = new HudongEventInfo();
	
	@ZapcomApi(value = "大转盘奖品信息")
	private List<HuodongEventDzpjpRule> list = new ArrayList<HuodongEventDzpjpRule>();

	//@ZapcomApi(value = "被邀请人信息")
	//private List<MemberSync> list2 = new ArrayList<MemberSync>();
	
	@ZapcomApi(value = "剩余抽奖次数")
	private int remainDrawNum = 0;
	
	@ZapcomApi(value = "每次抽奖扣减积分数")
	private int drawIntegral = 0;
	
	@ZapcomApi(value = "用户总积分")
	private int totalIntegral = 0;

	public int getDrawIntegral() {
		return drawIntegral;
	}

	public void setDrawIntegral(int drawIntegral) {
		this.drawIntegral = drawIntegral;
	}

	public int getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(int totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public HudongEventInfo getHudongEventInfo() {
		return hudongEventInfo;
	}

	public void setHudongEventInfo(HudongEventInfo hudongEventInfo) {
		this.hudongEventInfo = hudongEventInfo;
	}

	public List<HuodongEventDzpjpRule> getList() {
		return list;
	}

	public void setList(List<HuodongEventDzpjpRule> list) {
		this.list = list;
	}

	public int getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(int remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}

	
}
