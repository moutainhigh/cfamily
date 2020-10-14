package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.CouponForGetInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetCouponForOrderConfirmResult extends RootResultWeb {
	
	@ZapcomApi(value="可领取优惠劵列表",remark="优惠券适合该商品")
	private List<CouponForGetInfo> couponForGetList=new ArrayList<CouponForGetInfo>();


	public List<CouponForGetInfo> getCouponForGetList() {
		return couponForGetList;
	}

	public void setCouponForGetList(List<CouponForGetInfo> couponForGetList) {
		this.couponForGetList = couponForGetList;
	}
	

}
