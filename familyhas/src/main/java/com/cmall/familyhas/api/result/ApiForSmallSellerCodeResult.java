package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForSmallSellerCodeResult extends RootResultWeb{
	
	@ZapcomApi(value="订单号",demo="SF03150616100001")
	private String small_seller_code="";
	
	@ZapcomApi(value="下单人手机号",demo="SF03150616100001")
	private String buyer_mobile="";

	public String getSmall_seller_code() {
		return small_seller_code;
	}

	public void setSmall_seller_code(String small_seller_code) {
		this.small_seller_code = small_seller_code;
	}

	public String getBuyer_mobile() {
		return buyer_mobile;
	}

	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}
}
