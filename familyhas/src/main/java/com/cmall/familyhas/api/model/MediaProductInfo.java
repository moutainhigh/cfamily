package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class MediaProductInfo {

	@ZapcomApi(value="商品名称")
	private String productName = "";
	
	@ZapcomApi(value="商品编号")
	private String productCode = "";
	
	@ZapcomApi(value="当前售价")
	private BigDecimal sellprice = BigDecimal.ZERO;
	
	@ZapcomApi(value="市场价")
	private BigDecimal markPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value="商品主图")
	private String mainPicUrl = "";

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public BigDecimal getSellprice() {
		return sellprice;
	}

	public void setSellprice(BigDecimal sellprice) {
		this.sellprice = sellprice;
	}

	public BigDecimal getMarkPrice() {
		return markPrice;
	}

	public void setMarkPrice(BigDecimal markPrice) {
		this.markPrice = markPrice;
	}

	public String getMainPicUrl() {
		return mainPicUrl;
	}

	public void setMainPicUrl(String mainPicUrl) {
		this.mainPicUrl = mainPicUrl;
	}
	
}
