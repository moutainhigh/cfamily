package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiOrderDetailsBalancePayResult {
	@ZapcomApi(value="余额支付金额")
	private String sumDueMoney = "";
	@ZapcomApi(value="余额支付类型",remark="449746280009:微公社支付")
	private String payType = "";
	
	public String getSumDueMoney() {
		return sumDueMoney;
	}
	public void setSumDueMoney(String sumDueMoney) {
		this.sumDueMoney = sumDueMoney;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
}
