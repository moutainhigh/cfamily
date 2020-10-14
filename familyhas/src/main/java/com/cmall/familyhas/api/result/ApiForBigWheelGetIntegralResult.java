package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForBigWheelGetIntegralResult extends RootResult {

	@ZapcomApi(value = "剩余积分是否可以抽奖", remark = "0否;1是")
	private int isDraw = 0;
	
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

	public int getIsDraw() {
		return isDraw;
	}

	public void setIsDraw(int isDraw) {
		this.isDraw = isDraw;
	}

	public int getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(int remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}
	
}
