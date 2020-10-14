package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForRedPackageCountAwardResult extends RootResult {
	
	@ZapcomApi(value="中奖优惠券编号",remark="",demo="")
	private String couponCode = "";
	
	@ZapcomApi(value="中奖状态",remark="1：本次中奖，2：次数已用完，3：未中奖（本次机会有效）,4：未达标（数红包次数未满足最低要求）",demo="1")
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
	
	/**
	 * 可玩次数
	 */
	@ZapcomApi(value="剩余数红包次数",remark="剩余数红包次数")
	private Integer  times = 0;
	

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

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
