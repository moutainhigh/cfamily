package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForCutCakeDrawResult extends RootResult {

	@ZapcomApi(value="剩余切蛋糕次数")
	private int cutCakeNum = 0;
	
	@ZapcomApi(value = "奖品名称")
	private String jpName = "";
	
	@ZapcomApi(value="优惠券金额")
	private int couponAmount = 0;
	
	@ZapcomApi(value="积分数")
	private int jfNum = 0;
	
	@ZapcomApi(value = "奖品类型", remark = "0:积分; 1:优惠券; 2:特等奖，3：一等奖")
	private String jpType = "";
	
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

	public String getJpName() {
		return jpName;
	}

	public void setJpName(String jpName) {
		this.jpName = jpName;
	}

	public int getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}

	public int getJfNum() {
		return jfNum;
	}

	public void setJfNum(int jfNum) {
		this.jfNum = jfNum;
	}

	public String getJpType() {
		return jpType;
	}

	public void setJpType(String jpType) {
		this.jpType = jpType;
	}
	
}
