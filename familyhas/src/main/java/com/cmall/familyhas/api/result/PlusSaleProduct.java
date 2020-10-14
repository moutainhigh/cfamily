package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 
 * @author Angel Joy
 * @date 2020年5月8日 下午2:13:35
 * @version 
 * @desc
 */
public class PlusSaleProduct {
	
	@ZapcomApi(value="商品编码",remark="")
	private String productCode = "";
	
	@ZapcomApi(value="SKU编码",remark="")
	private String skuCode = "";
	
	@ZapcomApi(value="商品名称",remark="")
	private String productName = "";
	
	@ZapcomApi(value="商品图片",remark="")
	private String productUrl = "";
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag = "";
	
	@ZapcomApi(value="橙意卡价格",remark="")
	private BigDecimal plusVipPrice = new BigDecimal(0);
	
	@ZapcomApi(value="原销售价格",remark="")
	private BigDecimal orgSellPrice = new BigDecimal(0);
	
	@ZapcomApi(value="带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();

	public List<TagInfo> getTagInfoList() {
		return tagInfoList;
	}

	public void setTagInfoList(List<TagInfo> tagInfoList) {
		this.tagInfoList = tagInfoList;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String getProClassifyTag() {
		return proClassifyTag;
	}

	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}

	public BigDecimal getPlusVipPrice() {
		return plusVipPrice;
	}

	public void setPlusVipPrice(BigDecimal plusVipPrice) {
		this.plusVipPrice = plusVipPrice;
	}

	public BigDecimal getOrgSellPrice() {
		return orgSellPrice;
	}

	public void setOrgSellPrice(BigDecimal orgSellPrice) {
		this.orgSellPrice = orgSellPrice;
	}

}
