package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/** 
* @author Angel Joy
* @Time 2020年6月11日 上午10:48:59 
* @Version 1.0
* <p>Description:</p>
*/
public class JulyPopularizeProduct {
	
	@ZapcomApi(value="商品编号",remark="118800",demo="118800")
	private String productCode="";
	
	@ZapcomApi(value="商品主图",remark="展示图片",demo="")
	private String mainPicUrl="";
	
	@ZapcomApi(value="分享图片",remark="分享图片",demo="")
	private String sharePic="";
	
	@ZapcomApi(value="商品名称",remark="商品名称",demo="苹果12")
	private String productName="";
	
	@ZapcomApi(value="商品原售价",remark="119.00",demo="119.00")
	private BigDecimal skuPrice;
	
	@ZapcomApi(value="商品实际售价",remark="109.00",demo="109.00")
	private BigDecimal sellPrice=BigDecimal.ZERO;
	
	@ZapcomApi(value="推广赚钱金额",remark="10.00",demo="10.00")
	private BigDecimal saveValue=BigDecimal.ZERO;
	
	@ZapcomApi(value="是否购买过标识",remark="1：购买过，0：未购买过",demo="0")
	private String buyFlag="0";
	
	@ZapcomApi(value="带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getMainPicUrl() {
		return mainPicUrl;
	}

	public void setMainPicUrl(String mainPicUrl) {
		this.mainPicUrl = mainPicUrl;
	}

	public String getSharePic() {
		return sharePic;
	}

	public void setSharePic(String sharePic) {
		this.sharePic = sharePic;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getSaveValue() {
		return saveValue;
	}

	public void setSaveValue(BigDecimal saveValue) {
		this.saveValue = saveValue;
	}

	public String getBuyFlag() {
		return buyFlag;
	}

	public void setBuyFlag(String buyFlag) {
		this.buyFlag = buyFlag;
	}

	public List<TagInfo> getTagInfoList() {
		return tagInfoList;
	}

	public void setTagInfoList(List<TagInfo> tagInfoList) {
		this.tagInfoList = tagInfoList;
	}
	
}
