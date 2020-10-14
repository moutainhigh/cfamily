package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderReturnInfo {
	//

	// 退款明细

	@ZapcomApi(value = "退款类型")
	private String type;
	@ZapcomApi(value = "金额")
	private String amount;

	@ZapcomApi(value = "到账描述")
	private String describe;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
