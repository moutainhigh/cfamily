package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiQuerySpecialEventResult extends RootResultWeb{
	
	@ZapcomApi(value = "活动编号")
	private String event_code = "";
	
	@ZapcomApi(value = "商品编号", remark = "")
	private String product_code = "";
	
	@ZapcomApi(value = "sku编号", remark = "")
	private String sku_code = "";
	
	@ZapcomApi(value = "商品图")
	private String product_pic = "";
	
	@ZapcomApi(value = "用户是否可购买", remark = "0不可购买，1可购买")
	private String buy_flag = "0";

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getProduct_pic() {
		return product_pic;
	}

	public void setProduct_pic(String product_pic) {
		this.product_pic = product_pic;
	}

	public String getBuy_flag() {
		return buy_flag;
	}

	public void setBuy_flag(String buy_flag) {
		this.buy_flag = buy_flag;
	}
	
}
