package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiRealMoney {
	@ZapcomApi(value="可提现收益")
	private String realMoney = "0";
	@ZapcomApi(value="已提现收益")
	private String payMoney = "0";
	@ZapcomApi(value="是否可提现标识")
	private boolean haveApply = false;
	
	public boolean isHaveApply() {
		return haveApply;
	}
	public void setHaveApply(boolean haveApply) {
		this.haveApply = haveApply;
	}
	public String getRealMoney() {
		return realMoney;
	}
	public void setRealMoney(String realMoney) {
		this.realMoney = realMoney;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	
}
