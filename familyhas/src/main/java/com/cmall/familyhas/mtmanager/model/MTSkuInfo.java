package com.cmall.familyhas.mtmanager.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 提供给mt管家的sku信息
 * @author pang_jhui
 *
 */
public class MTSkuInfo {
	
	@ZapcomApi(value="sku编号")
	private String skuCode;
	
	@ZapcomApi(value="sku名称")
	private String skuName;
	
	@ZapcomApi(value="规格")
	private String skuKeyvalue;
	
	@ZapcomApi(value="售价")
	private BigDecimal sellPrice;
	
	@ZapcomApi(value="库存数")
	private int stockNum;
	
	@ZapcomApi(value="安全库存数")
	private int securityStockNum;
	
	@ZapcomApi(value="sku图片")
	private String skuPicUrl;
	
	@ZapcomApi(value="是否可卖")
	private String saleYn;

	/**
	 * 获取sku编号
	 * @return
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * 获取sku名称
	 * @return
	 */
	public String getSkuName() {
		return skuName;
	}

	/**
	 * 获取规格
	 * @return
	 */
	public String getSkuKeyvalue() {
		return skuKeyvalue;
	}

	/**
	 * 获取销售价格
	 * @return
	 */
	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	/**
	 * 获取库存数量
	 * @return
	 */
	public int getStockNum() {
		return stockNum;
	}

	/**
	 * 获取安全库存数量
	 * @return
	 */
	public int getSecurityStockNum() {
		return securityStockNum;
	}

	/**
	 * 获取图片路径
	 * @return
	 */
	public String getSkuPicUrl() {
		return skuPicUrl;
	}

	/**
	 * 获取是否可卖
	 * @return
	 */
	public String getSaleYn() {
		return saleYn;
	}

	/**
	 * 设置sku编码
	 * @param skuCode
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	/**
	 * 设置sku名称
	 * @param skuName
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	/**
	 * 设置sku规格
	 * @param skuStandard
	 */
	public void setSkuKeyvalue(String skuKeyvalue) {
		this.skuKeyvalue = skuKeyvalue;
	}

	/**
	 * 设置销售价
	 * @param sellPrice
	 */
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * 设置库存
	 * @param stockNum
	 */
	public void setStockNum(int stockNum) {
		this.stockNum = stockNum;
	}

	/**
	 * 设置安全库存
	 * @param securityStockNum
	 */
	public void setSecurityStockNum(int securityStockNum) {
		this.securityStockNum = securityStockNum;
	}

	/**
	 * 设置图片信息
	 * @param skuPicUrl
	 */
	public void setSkuPicUrl(String skuPicUrl) {
		this.skuPicUrl = skuPicUrl;
	}

	/**
	 * 设置是否可卖
	 * @param saleYn
	 */
	public void setSaleYn(String saleYn) {
		this.saleYn = saleYn;
	}

}
