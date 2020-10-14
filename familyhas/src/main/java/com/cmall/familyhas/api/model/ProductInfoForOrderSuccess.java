package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 扫码购落地页显示商品信息
 * @author 任宏斌
 */
public class ProductInfoForOrderSuccess {

	@ZapcomApi(value="商品编号")
	private String productCode;
	@ZapcomApi(value="商品名称")
	private String productName;
	@ZapcomApi(value="购买数量")
	private int productNum;
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductNum() {
		return productNum;
	}
	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}
	
}
