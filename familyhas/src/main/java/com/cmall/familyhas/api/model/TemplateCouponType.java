package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 专题优惠券类型
 */
public class TemplateCouponType {
	
	/** 默认可以兑换 */
	public static final int EXCHANGE_STATUS_DEFAULT = 0;
	/** 已兑换 */
	public static final int EXCHANGE_STATUS_YES = 1;
	/** 兑光了 */
	public static final int EXCHANGE_STATUS_NONE= 2;
	
	@ZapcomApi(value = "uid")
	private String uid = "";
	
	@ZapcomApi(value = "类型编号")
	private String couponTypeCode = "";
	
	@ZapcomApi(value = "类型名称")
	private String couponTypeName = "";
	
	@ZapcomApi(value = "剩余金额")
	private String surplusMoney = "";
	
	@ZapcomApi(value = "开始时间")
	private String startTime = "";
	
	@ZapcomApi(value = "结束时间")
	private String endTime = "";
	
	@ZapcomApi(value = "有效类型")
	private String validType = "";
	
	@ZapcomApi(value = "有效天数")
	private int validDay = 0;
	
	@ZapcomApi(value = "金额类型")
	private String moneyType = "";
	
	@ZapcomApi(value = "面额")
	private String money = "";
	
	@ZapcomApi(value = "使用下限金额")
	private String limitMoney = "";
	
	@ZapcomApi(value = "使用范围")
	private String limitScope = "";
	
	@ZapcomApi(value = "跳转类型")
	private String actionType = "";
	
	@ZapcomApi(value = "跳转内容")
	private String actionValue = "";
	
	@ZapcomApi(value = "兑换类型")
	private String exchangeType = "";
	
	@ZapcomApi(value = "兑换数值")
	private String exchangeValue = "";
	
	@ZapcomApi(value = "兑换状态: 0 默认、1 已兑换、 2 已兑完")
	private int exchangeStatus = EXCHANGE_STATUS_DEFAULT;

	public String getCouponTypeCode() {
		return couponTypeCode;
	}

	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
	}

	public String getCouponTypeName() {
		return couponTypeName;
	}

	public void setCouponTypeName(String couponTypeName) {
		this.couponTypeName = couponTypeName;
	}

	public String getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(String surplusMoney) {
		this.surplusMoney = surplusMoney;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
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

	public String getLimitScope() {
		return limitScope;
	}

	public void setLimitScope(String limitScope) {
		this.limitScope = limitScope;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getExchangeValue() {
		return exchangeValue;
	}

	public void setExchangeValue(String exchangeValue) {
		this.exchangeValue = exchangeValue;
	}

	public String getValidType() {
		return validType;
	}

	public void setValidType(String validType) {
		this.validType = validType;
	}

	public int getValidDay() {
		return validDay;
	}

	public void setValidDay(int validDay) {
		this.validDay = validDay;
	}

	public int getExchangeStatus() {
		return exchangeStatus;
	}

	public void setExchangeStatus(int exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionValue() {
		return actionValue;
	}

	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}
	
}
