package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Button;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 秒杀商品列表页。
 * @author Angel Joy
 *
 */
public class RegularToNewProduct {
	
	@ZapcomApi(value="商品编码",remark="")
	private String productCode = "";
	
	@ZapcomApi(value="商品名称",remark="")
	private String productName = "";
	
	@ZapcomApi(value="商品图片",remark="")
	private String productUrl = "";
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag = "";
	
	@ZapcomApi(value="销售价格",remark="")
	private BigDecimal sellPrice = new BigDecimal(0);

	@ZapcomApi(value="带样式的标签列表")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
	
	@ZapcomApi(value="标签",remark="券，赠品，满减")
	private List<String> tagList = new ArrayList<String>();
	
	@ZapcomApi(value="sku关联商品下的所有sku实际库存")
	private int allSkuRealStock = 0;

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

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public List<TagInfo> getTagInfoList() {
		return tagInfoList;
	}

	public void setTagInfoList(List<TagInfo> tagInfoList) {
		this.tagInfoList = tagInfoList;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public int getAllSkuRealStock() {
		return allSkuRealStock;
	}

	public void setAllSkuRealStock(int allSkuRealStock) {
		this.allSkuRealStock = allSkuRealStock;
	}
	
	
}
