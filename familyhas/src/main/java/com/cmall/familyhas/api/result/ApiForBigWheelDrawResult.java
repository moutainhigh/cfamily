package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForBigWheelDrawResult extends RootResult {

	@ZapcomApi(value = "剩余抽奖次数", remark = "返回0时,表示没有抽奖次数;返回-1时,表示抽奖失败;其余数值为抽奖成功")
	private int sycs = 0;
	
	@ZapcomApi(value = "奖品编号")
	private String jpCode = "";
	
	//@ZapcomApi(value = "奖品信息")
	//private HuodongEventDzpjpRule huodongEventDzpjpRule;
	
	@ZapcomApi(value = "抽奖编码")
	private String jpCodeSeq;
	
	@ZapcomApi(value = "每次抽奖扣减积分数")
	private int drawIntegral = 0;
	
	@ZapcomApi(value = "用户总积分")
	private int totalIntegral = 0;

	public int getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(int totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public int getDrawIntegral() {
		return drawIntegral;
	}

	public void setDrawIntegral(int drawIntegral) {
		this.drawIntegral = drawIntegral;
	}

	public int getSycs() {
		return sycs;
	}

	public void setSycs(int sycs) {
		this.sycs = sycs;
	}

	public String getJpCode() {
		return jpCode;
	}

	public void setJpCode(String jpCode) {
		this.jpCode = jpCode;
	}

	public String getJpCodeSeq() {
		return jpCodeSeq;
	}

	public void setJpCodeSeq(String jpCodeSeq) {
		this.jpCodeSeq = jpCodeSeq;
	}

	
}
