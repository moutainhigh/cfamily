package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForReturnGoodsInfoInput extends RootInput {

	@ZapcomApi(value = "订单号", remark = "以DD开头的订单号", require = 1, demo = "DD32246104")
	private String orderCode = "";

	@ZapcomApi(value = "sku编码", require = 0, demo = "8019575972")
	private String productCode = "";
	
	@ZapcomApi(value = "LD订单序号", remark="LD订单的订单序号", require = 0, demo = "1")
	private String orderSeq = "0";
	
	@ZapcomApi(value = "换货新单标识", remark="换货新单标识，1：换货新单，0：非换货新单", require = 0, demo = "1")
	private String isChangeGoods = "1";

	
	public String getIsChangeGoods() {
		return isChangeGoods;
	}

	public void setIsChangeGoods(String isChangeGoods) {
		this.isChangeGoods = isChangeGoods;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	
}
