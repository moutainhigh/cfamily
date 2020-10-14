package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForRedPackageAwardResult extends RootResult {
	
	@ZapcomApi(value="中奖优惠券编号",remark="",demo="")
	private String couponCode = "";
	
	@ZapcomApi(value="中奖状态",remark="1：本次中奖，2：此次活动已中奖（本次机会无效），3：未中奖（本次机会有效）",demo="1")
	private String status = "3";
	
	@ZapcomApi(value="优惠券名称",remark="",demo="10优惠券")
	private String couponTypeName = "";
	
	@ZapcomApi(value="面值",remark="优惠券金额大小",demo="10")
	private String money = "";
	
	@ZapcomApi(value="优惠券使用最小金额限制",remark="满100可用",demo="100")
	private String limitMoney = "100";
	
	@ZapcomApi(value="优惠券金额类型",remark="449748120001：金额券，449748120002：折扣券，449748120003：礼金券",demo="449748120001")
	private String moneyType = "449748120001";
	
	@ZapcomApi(value="使用范围",remark="全场通用",demo="全场通用")
	private String limitScope = "";

	public String getCouponTypeName() {
		return couponTypeName;
	}

	public void setCouponTypeName(String couponTypeName) {
		this.couponTypeName = couponTypeName;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(String limitMoney) {
		this.limitMoney = limitMoney;
	}

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	public String getLimitScope() {
		return limitScope;
	}

	public void setLimitScope(String limitScope) {
		this.limitScope = limitScope;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
