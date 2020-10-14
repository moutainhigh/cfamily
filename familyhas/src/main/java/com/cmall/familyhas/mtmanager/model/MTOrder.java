package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 
 * MT 
 * @author xiegj
 */

public class MTOrder {

	@ZapcomApi(value="订单编号", remark="DD123456") 
	private String order_code="";
	@ZapcomApi(value="商品编号", remark="8016123456")
	private String product_code="";
	@ZapcomApi(value="产品编号", remark="8019123456")
	private String Sku_code="";
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getSku_code() {
		return Sku_code;
	}
	public void setSku_code(String sku_code) {
		Sku_code = sku_code;
	}
	
}
