package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ProgramRelProduct {

	@ZapcomApi(value="商品编号")
	private String product_code = "";
	
	@ZapcomApi(value="商品名称")
	private String product_name ="";
	
	@ZapcomApi(value="是否最新期",remark="1001：是  ; 1000：否")
	private String status = "";
	
	@ZapcomApi(value="商品图片")
	private String product_img ="";
	
	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProduct_img() {
		return product_img;
	}

	public void setProduct_img(String product_img) {
		this.product_img = product_img;
	}

	
	
	
}
