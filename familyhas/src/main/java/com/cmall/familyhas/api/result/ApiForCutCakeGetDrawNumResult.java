package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForCutCakeGetDrawNumResult extends RootResult {

	@ZapcomApi(value="剩余切蛋糕次数")
	private int cutCakeNum = 0;
	
	@ZapcomApi(value="能否下单增加切蛋糕次数", remark = "0：否，1：是")
	private String canAddNum = "1";
	
	@ZapcomApi(value="今日是否送祝福", remark = "0：否，1：是")
	private String isBlessing = "0";

	public String getIsBlessing() {
		return isBlessing;
	}

	public void setIsBlessing(String isBlessing) {
		this.isBlessing = isBlessing;
	}

	public String getCanAddNum() {
		return canAddNum;
	}

	public void setCanAddNum(String canAddNum) {
		this.canAddNum = canAddNum;
	}

	public int getCutCakeNum() {
		return cutCakeNum;
	}

	public void setCutCakeNum(int cutCakeNum) {
		this.cutCakeNum = cutCakeNum;
	}
	
}
