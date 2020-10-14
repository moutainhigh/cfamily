package com.cmall.familyhas.mtmanager.model;

import java.math.BigDecimal;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 提供给mt管家的产品信息
 * @author pang_jhui
 *
 */
public class MTProductInfo {
	
	
	@ZapcomApi(value="商品编号")
	private String productCode;
	
	@ZapcomApi(value="商品名称")
	private String productName;
	
	@ZapcomApi(value="市场价格")
	private BigDecimal marketPrice;
	
	@ZapcomApi(value="商品主图")
	private String mainPicUrl;
	
	@ZapcomApi(value="商品图片")
	private List<MTProductPic> productPicList;
	
	@ZapcomApi(value="商品品牌")
	private String brandName;
	
	@ZapcomApi(value="是否可售标志：1在售 0不可售")
	private Integer flagSale;
	
	@ZapcomApi(value="商品描述")
	private MTProductDescription mtProductDescription;
	
	@ZapcomApi(value="sku信息")
	private List<MTSkuInfo> mtSkuInfos;

	@ZapcomApi(value="所属分类")
	private List<String> categories;
	
	@ZapcomApi(value="惠家有商品拆单规则")
	private String splitRole = "";
	public String getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public String getMainPicUrl() {
		return mainPicUrl;
	}

	public String getBrandName() {
		return brandName;
	}

	public MTProductDescription getMtProductDescription() {
		return mtProductDescription;
	}

	public List<MTSkuInfo> getMtSkuInfos() {
		return mtSkuInfos;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public void setMainPicUrl(String mainPicUrl) {
		this.mainPicUrl = mainPicUrl;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public void setMtProductDescription(MTProductDescription mtProductDescription) {
		this.mtProductDescription = mtProductDescription;
	}

	public void setMtSkuInfos(List<MTSkuInfo> mtSkuInfos) {
		this.mtSkuInfos = mtSkuInfos;
	}

	public Integer getFlagSale() {
		return flagSale;
	}

	public void setFlagSale(Integer flagSale) {
		this.flagSale = flagSale;
	}

	public List<MTProductPic> getProductPicList() {
		return productPicList;
	}

	public void setProductPicList(List<MTProductPic> productPicList) {
		this.productPicList = productPicList;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getSplitRole() {
		return splitRole;
	}

	public void setSplitRole(String splitRole) {
		this.splitRole = splitRole;
	}
	
}
