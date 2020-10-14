package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiDLQAddToolsResult extends RootResult{

	@ZapcomApi(value="操作数据uid")
	private String uid = "";
	
	@ZapcomApi(value="商品编号")
	private String product_code = "";
	
	@ZapcomApi(value="商品名称")
	private String product_name = ""; 
	
	@ZapcomApi(value="商品图片")
	private String product_main_url = "";
	
	@ZapcomApi(value="商品状态")
	private String product_status= "";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

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

	public String getProduct_main_url() {
		return product_main_url;
	}

	public void setProduct_main_url(String product_main_url) {
		this.product_main_url = product_main_url;
	}

	public String getProduct_status() {
		return product_status;
	}

	public void setProduct_status(String product_status) {
		this.product_status = product_status;
	}
	
	
}
