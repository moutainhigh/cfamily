package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class FarmExchangeSku {

	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	@ZapcomApi(value = "sku编号")
	private String skuCode = "";
	@ZapcomApi(value = "sku名称")
	private String skuName = "";
	@ZapcomApi(value = "sku属性值")
	private String skuKey = "";
	@ZapcomApi(value = "sku属性信息")
	private String skuKeyvalue = "";
	@ZapcomApi(value = "sku图片")
	private String skuImg = "";
	@ZapcomApi(value = "是否可兑换", remark = "0否 1是 此属性仅对种植商品有效")
	private String exchangeFlag = "0";
	@ZapcomApi(value = "果树类型")
	private String treeType = "";
	
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
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuKey() {
		return skuKey;
	}
	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}
	public String getSkuKeyvalue() {
		return skuKeyvalue;
	}
	public void setSkuKeyvalue(String skuKeyvalue) {
		this.skuKeyvalue = skuKeyvalue;
	}
	public String getSkuImg() {
		return skuImg;
	}
	public void setSkuImg(String skuImg) {
		this.skuImg = skuImg;
	}
	public String getExchangeFlag() {
		return exchangeFlag;
	}
	public void setExchangeFlag(String exchangeFlag) {
		this.exchangeFlag = exchangeFlag;
	}
	public String getTreeType() {
		return treeType;
	}
	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

}
