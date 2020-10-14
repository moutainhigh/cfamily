package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ProductHotSale {

	@ZapcomApi(value = "商品编号")
	private String procuctCode = "";

	@ZapcomApi(value = "商品名称")
	private String productName = "";

	@ZapcomApi(value = "商品销售价")
	private String sellPrice = "";
	
	@ZapcomApi(value = "商品原价")
	private String skuPrice = "";

	@ZapcomApi(value = "商品图片")
	private String mainpic_url = "";

	@ZapcomApi(value = "商品标签", remark = "LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
	private List<String> labelsList = new ArrayList<String>();

	@ZapcomApi(value = "商品标签图片地址", remark = "3.9.2以后开始使用")
	private String labelsPic = "";

	@ZapcomApi(value = "小用户编号")
	private String smallSellerCode = "";
	
	@ZapcomApi(value = "商品分类标签(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag = "";
	
	@ZapcomApi(value = "活动类型编码")
	private String eventTypeCode = "";
	
	@ZapcomApi(value = "活动类型名称")
	private String eventTypeName = "";

	@ZapcomApi(value = "几人团", remark = "需要几人参团，字符串类型的数字")
	private String collagePersonCount;

	public String getProcuctCode() {
		return procuctCode;
	}

	public void setProcuctCode(String procuctCode) {
		this.procuctCode = procuctCode;
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

	public String getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getMainpic_url() {
		return mainpic_url;
	}

	public void setMainpic_url(String mainpic_url) {
		this.mainpic_url = mainpic_url;
	}

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public String getLabelsPic() {
		return labelsPic;
	}

	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public String getProClassifyTag() {
		return proClassifyTag;
	}

	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}

	public String getEventTypeCode() {
		return eventTypeCode;
	}

	public void setEventTypeCode(String eventTypeCode) {
		this.eventTypeCode = eventTypeCode;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public String getCollagePersonCount() {
		return collagePersonCount;
	}

	public void setCollagePersonCount(String collagePersonCount) {
		this.collagePersonCount = collagePersonCount;
	}

}
