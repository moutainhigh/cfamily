package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HudongEventInfo;
import com.cmall.familyhas.api.model.HuodongEventDzpjfRule;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForJFBigWheelHomeResult extends RootResult {

	@ZapcomApi(value = "积分大转盘活动")
	private HudongEventInfo hudongEventInfo = new HudongEventInfo();
	
	@ZapcomApi(value = "积分大转盘奖品信息")
	List<HuodongEventDzpjfRule> list = new ArrayList<HuodongEventDzpjfRule>();
	
	@ZapcomApi(value = "用户总积分")
	private int integralTotal = 0;
	
	@ZapcomApi(value = "剩余抽奖次数")
	private int remainDrawNum = 0;
	
	@ZapcomApi(value = "抽中'我'字的个数")
	private int count1 = 0;
	@ZapcomApi(value = "抽中'爱'字的个数")
	private int count2 = 0;
	@ZapcomApi(value = "抽中'惠'字的个数")
	private int count3 = 0;
	@ZapcomApi(value = "抽中'家'字的个数")
	private int count4 = 0;
	@ZapcomApi(value = "抽中'有'字的个数")
	private int count5 = 0;


	public int getIntegralTotal() {
		return integralTotal;
	}

	public void setIntegralTotal(int integralTotal) {
		this.integralTotal = integralTotal;
	}

	public int getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(int remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}

	public HudongEventInfo getHudongEventInfo() {
		return hudongEventInfo;
	}

	public void setHudongEventInfo(HudongEventInfo hudongEventInfo) {
		this.hudongEventInfo = hudongEventInfo;
	}

	public List<HuodongEventDzpjfRule> getList() {
		return list;
	}

	public void setList(List<HuodongEventDzpjfRule> list) {
		this.list = list;
	}

	public int getCount1() {
		return count1;
	}

	public void setCount1(int count1) {
		this.count1 = count1;
	}

	public int getCount2() {
		return count2;
	}

	public void setCount2(int count2) {
		this.count2 = count2;
	}

	public int getCount3() {
		return count3;
	}

	public void setCount3(int count3) {
		this.count3 = count3;
	}

	public int getCount4() {
		return count4;
	}

	public void setCount4(int count4) {
		this.count4 = count4;
	}

	public int getCount5() {
		return count5;
	}

	public void setCount5(int count5) {
		this.count5 = count5;
	}
	
}
