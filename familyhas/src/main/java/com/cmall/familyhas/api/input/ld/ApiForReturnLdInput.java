package com.cmall.familyhas.api.input.ld;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForReturnLdInput extends RootInput{
	@ZapcomApi(value="订单编号",require=1)
	private String orderCode="123456789";
	@ZapcomApi(value="售后数量",require=1)
	private Integer count = 1;
	@ZapcomApi(value="商品SKU编号",require=1)
	private String skuCode = "123456";

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	
}
