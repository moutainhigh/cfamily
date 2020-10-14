package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ReturnGoodsInfo {

	@ZapcomApi(value="SKU编码",demo="15646")
	private String sku_code="";
	
	@ZapcomApi(value="已退件数",demo="15646")
	private int return_num=0;
	
	@ZapcomApi(value="退货进行中件数",demo="15646")
	private int occupy_num=0;

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public int getReturn_num() {
		return return_num;
	}

	public void setReturn_num(int return_num) {
		this.return_num = return_num;
	}

	public int getOccupy_num() {
		return occupy_num;
	}

	public void setOccupy_num(int occupy_num) {
		this.occupy_num = occupy_num;
	}
	
	
}
