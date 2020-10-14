package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiForGetCouponNumInput extends RootInput {

	@ZapcomApi(value = "送券类型",  require = 1, remark = "1:注册送券，2:在线支付下单送，3:在线支付支付送4:在线支付收获送5:货到付款下单送6:货到付款收获送7:推荐人送券", demo = "1")
	private String reletiveType = "";

	public String getReletiveType() {
		return reletiveType;
	}

	public void setReletiveType(String reletiveType) {
		this.reletiveType = reletiveType;
	}

}
