package com.cmall.familyhas.api.input;


import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class OrderCodeSearchInfoInput extends RootInput{
	@ZapcomApi(value="orderCode",remark="订单号")
	private String orderCode = "";
	@ZapcomApi(value="companyCode",remark="公司编号")
	private String companyCode = "";
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	
}
