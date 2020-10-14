package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class VipBargain {
	@ZapcomApi(value="商品编码")
	private String productCode;
	@ZapcomApi(value="商品名称")
	private String productName;
	@ZapcomApi(value="销售价")
	private String sellPrice;
	@ZapcomApi(value="优惠价")
	private String favorablePrice;
	@ZapcomApi(value="库存数")
	private int stockCount;
	@ZapcomApi(value="是否抢光")
	private boolean isLoot;
	@ZapcomApi(value="图片")
	private String productImg;
	@ZapcomApi(value="是否下架")
	private boolean isDown;
	
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
	
	public String getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public String getFavorablePrice() {
		return favorablePrice;
	}
	public void setFavorablePrice(String favorablePrice) {
		this.favorablePrice = favorablePrice;
	}
	
	public int getStockCount() {
		return stockCount;
	}
	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}
	
	public boolean isLoot() {
		return isLoot;
	}
	public void setLoot(boolean isLoot) {
		this.isLoot = isLoot;
	}
	
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	
	public boolean isDown() {
		return isDown;
	}
	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}
}
