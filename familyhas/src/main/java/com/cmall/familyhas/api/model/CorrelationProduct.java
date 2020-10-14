package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 支付成功页关联商品
 * @remark 
 * @author 任宏斌
 * @date 2019年12月25日
 */
public class CorrelationProduct {

	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	
	@ZapcomApi(value = "商品名称")
	private String productName = "";
	
	@ZapcomApi(value = "商品价格")
	private BigDecimal productPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value = "商品主图")
	private String productPic = "";

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

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductPic() {
		return productPic;
	}

	public void setProductPic(String productPic) {
		this.productPic = productPic;
	}
	
}
