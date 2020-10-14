package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 促销活动
 * @author 李国杰
 *
 */
public class ProductPunishModel {

	@ZapcomApi(value = "商品编号")
	private String productCode = "";

	@ZapcomApi(value = "商品名称")
	private String productName = "";
	
	@ZapcomApi(value = "商品成本")
	private BigDecimal productCost ;
	
	@ZapcomApi(value = "商品售价")
	private BigDecimal productSellPrice;

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

	

	public BigDecimal getProductCost() {
		return productCost;
	}

	public void setProductCost(BigDecimal productCost) {
		this.productCost = productCost;
	}

	public BigDecimal getProductSellPrice() {
		return productSellPrice;
	}

	public void setProductSellPrice(BigDecimal productSellPrice) {
		this.productSellPrice = productSellPrice;
	}
	
}
