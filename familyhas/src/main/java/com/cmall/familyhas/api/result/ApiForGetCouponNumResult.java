package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetCouponNumResult extends RootResult {
	@ZapcomApi(value = "优惠券剩余张数", remark = "优惠券剩余张数", demo = "1")
	private int couponNum;
	
	@ZapcomApi(value = "优惠券面额", remark = "优惠券面额", demo = "1")
	private int money;

	@ZapcomApi(value = "折扣券张数", remark = "折扣券张数", demo = "1")
	private int many;



	public int getMany() {
		return many;
	}

	public void setMany(int many) {
		this.many = many;
	}

	public int getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(int couponNum) {
		this.couponNum = couponNum;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

}
