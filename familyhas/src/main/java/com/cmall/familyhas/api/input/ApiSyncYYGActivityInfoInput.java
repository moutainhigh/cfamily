package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiSyncYYGActivityInfoInput extends RootInput{

	@ZapcomApi(value="产品id",require=1)
	private String productID = "";
	
	@ZapcomApi(value="商品编号")
	private String productCode = "";
	
	@ZapcomApi(value="sku编号")
	private String skuCode = "";

	@ZapcomApi(value="期号",require=1)
	private String periodNum = "";
	
	@ZapcomApi(value="订单号",require=1)
	private String orderNo = "";
	
	@ZapcomApi(value="支付金额",require=1)
	private String payMoney = "";

	@ZapcomApi(value="加密验证参数",require=1)
	private String mac = "";

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getPeriodNum() {
		return periodNum;
	}

	public void setPeriodNum(String periodNum) {
		this.periodNum = periodNum;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	
}
