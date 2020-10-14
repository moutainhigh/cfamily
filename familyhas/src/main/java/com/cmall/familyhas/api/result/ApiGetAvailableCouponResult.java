package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.CouponInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetAvailableCouponResult extends RootResultWeb{
	
	@ZapcomApi(value="可用优惠劵列表",remark="优惠券未失效，适合该订单的商品")
	private List<CouponInfo> couponList=new ArrayList<CouponInfo>();

	@ZapcomApi(value="不可用优惠劵列表",remark="优惠券未失效，但是不适合该订单")
	private List<CouponInfo> disableCouponList=new ArrayList<CouponInfo>();

	@ZapcomApi(value="可用优惠劵数量",remark="可用优惠劵数量")
	private int couponCount=0;
	
	@ZapcomApi(value="不可用优惠劵数量",remark="不可用优惠劵数量")
	private int disableCouponCount=0;

	public List<CouponInfo> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponInfo> couponList) {
		this.couponList = couponList;
	}

	public List<CouponInfo> getDisableCouponList() {
		return disableCouponList;
	}

	public void setDisableCouponList(List<CouponInfo> disableCouponList) {
		this.disableCouponList = disableCouponList;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

	public int getDisableCouponCount() {
		return disableCouponCount;
	}

	public void setDisableCouponCount(int disableCouponCount) {
		this.disableCouponCount = disableCouponCount;
	}

	
}
