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
public class FlashSaleProduct {
	
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
	
	@ZapcomApi(value="秒杀价格",remark="")
	private BigDecimal killPrice = new BigDecimal(0);
	
	@ZapcomApi(value="原销售价格",remark="")
	private BigDecimal orgSellPrice = new BigDecimal(0);

	@ZapcomApi(value="按钮",remark="去抢购：4497477800080020、提醒我：4497477800080021、取消预约：4497477800080023")
	private List<Button> buttons = new ArrayList<Button>();

	@ZapcomApi(value="带样式的标签列表")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
	
	@ZapcomApi(value="标签",remark="券，赠品，满减")
	private List<String> tagList = new ArrayList<String>();
	
	@ZapcomApi(value="sku关联商品下的所有sku实际库存")
	private int allSkuRealStock = 0;
	
	@ZapcomApi(value="活动商品进行的进度",remark="目前参与进度显示的商品活动有 拼团列表，秒杀列表，闪购列表")
	private String rateOfProgress="";
	

	public String getRateOfProgress() {
		return rateOfProgress;
	}

	public void setRateOfProgress(String rateOfProgress) {
		this.rateOfProgress = rateOfProgress;
	}
	
	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	

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

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public BigDecimal getKillPrice() {
		return killPrice;
	}

	public void setKillPrice(BigDecimal killPrice) {
		this.killPrice = killPrice;
	}

	public BigDecimal getOrgSellPrice() {
		return orgSellPrice;
	}

	public void setOrgSellPrice(BigDecimal orgSellPrice) {
		this.orgSellPrice = orgSellPrice;
	}

	public int getAllSkuRealStock() {
		return allSkuRealStock;
	}

	public void setAllSkuRealStock(int allSkuRealStock) {
		this.allSkuRealStock = allSkuRealStock;
	}
	
}
