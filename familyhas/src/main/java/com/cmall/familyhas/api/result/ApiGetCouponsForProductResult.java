package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.CouponForGetInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetCouponsForProductResult extends RootResultWeb {
	
	@ZapcomApi(value="可领取优惠劵列表",remark="优惠券适合该商品")
	private List<CouponForGetInfo> couponForGetList=new ArrayList<CouponForGetInfo>();

	@ZapcomApi(value="可用优惠劵数量",remark="可用优惠劵数量")
	private int couponForGetCount=0;
	
	@ZapcomApi(value="可领取优惠劵展示名称",remark="'满300减10元'，'10元'")
	private List<String> couponNames=new ArrayList<String>();
	
	@ZapcomApi(value="券标识(区分小程序分销使用),1为分销券 0为普通配置的券")
	private String  ifDistributionCoupon ="0";
	
	@ZapcomApi(value="已经领完分销券(小程序)此时显示倒计时")
	private String  systemTime ="";

	@ZapcomApi(value="分销券活动是否有效 1：是，0：否")
	private String  fxActIsValid  ="0";
	
	@ZapcomApi(value="分销券面值金额")
	private BigDecimal  fxCouponMoney  =BigDecimal.ZERO;
	
	
	public String getFxActIsValid() {
		return fxActIsValid;
	}

	public void setFxActIsValid(String fxActIsValid) {
		this.fxActIsValid = fxActIsValid;
	}

	public BigDecimal getFxCouponMoney() {
		return fxCouponMoney;
	}

	public void setFxCouponMoney(BigDecimal fxCouponMoney) {
		this.fxCouponMoney = fxCouponMoney;
	}

	public String getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}

	public String getIfDistributionCoupon() {
		return ifDistributionCoupon;
	}

	public void setIfDistributionCoupon(String ifDistributionCoupon) {
		this.ifDistributionCoupon = ifDistributionCoupon;
	}

	public List<CouponForGetInfo> getCouponForGetList() {
		return couponForGetList;
	}

	public void setCouponForGetList(List<CouponForGetInfo> couponForGetList) {
		this.couponForGetList = couponForGetList;
	}

	public int getCouponForGetCount() {
		return couponForGetCount;
	}

	public void setCouponForGetCount(int couponForGetCount) {
		this.couponForGetCount = couponForGetCount;
	}

	public List<String> getCouponNames() {
		return couponNames;
	}

	public void setCouponNames(List<String> couponNames) {
		this.couponNames = couponNames;
	}
	

}
