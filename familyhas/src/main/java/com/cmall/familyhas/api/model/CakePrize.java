package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CakePrize {

	@ZapcomApi(value="奖品编号")
	private String jpCode = "";
	
	@ZapcomApi(value="奖品名称")
	private String jpName = "";
	
	@ZapcomApi(value="优惠劵类型编号")
	private String couponTypeCode = "";
	
	@ZapcomApi(value="优惠劵金额")
	private int couponAmount = 0;
	
	@ZapcomApi(value="积分数")
	private int jfNum = 0;
	
	@ZapcomApi(value="中奖概率(%)")
	private int jpZjgl = 0;
	
	@ZapcomApi(value="奖品类型 : 0:积分; 1:优惠券; 2:特等奖，3：一等奖")
	private String jpType = "";

	public String getJpCode() {
		return jpCode;
	}

	public void setJpCode(String jpCode) {
		this.jpCode = jpCode;
	}

	public String getJpName() {
		return jpName;
	}

	public void setJpName(String jpName) {
		this.jpName = jpName;
	}

	public String getCouponTypeCode() {
		return couponTypeCode;
	}

	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
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

	public int getJpZjgl() {
		return jpZjgl;
	}

	public void setJpZjgl(int jpZjgl) {
		this.jpZjgl = jpZjgl;
	}

	public String getJpType() {
		return jpType;
	}

	public void setJpType(String jpType) {
		this.jpType = jpType;
	}
	
	
}
