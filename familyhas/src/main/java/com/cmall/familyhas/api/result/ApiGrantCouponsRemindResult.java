package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.CouponInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGrantCouponsRemindResult extends RootResultWeb {
	
	@ZapcomApi(value="优惠劵列表",remark="优惠劵列表")
	private List<CouponInfo> couponInfoList=new ArrayList<CouponInfo>();
	
	@ZapcomApi(value="总条数", remark="总条数")
	private int total = 0;
	
	@ZapcomApi(value="是否包含新人券", remark="Y/N")
	private String containXrq = "N";

	public String getContainXrq() {
		return containXrq;
	}

	public void setContainXrq(String containXrq) {
		this.containXrq = containXrq;
	}

	public List<CouponInfo> getCouponInfoList() {
		return couponInfoList;
	}

	public void setCouponInfoList(List<CouponInfo> couponInfoList) {
		this.couponInfoList = couponInfoList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}