package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class LiveProd {
	
	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	
	@ZapcomApi(value = "商品名称")
	private String productName = "";
	
	@ZapcomApi(value = "商品主图")
	private String mainpicUrl = "";
	
	@ZapcomApi(value = "销售价/拼团商品展示价")
	private String sellPrice = "";
	
	@ZapcomApi(value = "市场价/拼团商品原价")
	private String markPrice = "";
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag="";
	
	@ZapcomApi(value="标签集合",remark="秒杀、闪购、拼团、特价、会员日、满减、赠品（最多展示三个）")
	private List<String> tagList = new ArrayList<String>();
	
	// 内部使用暂不传给前端
	@JSONField(serialize = false)
	@ZapcomApi(value = "商品状态",remark="4497153900060002:上架")
	private String productStatus = "";
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();
	
	@ZapcomApi(value="带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
	
	@ZapcomApi(value="是否有货", remark = "抢光了/有货")
	private String stockNum;
	
	@ZapcomApi(value="拼团标识", remark="拼团编码：4497472600010024")
	private String groupBuying = "";
	
	@ZapcomApi(value="是否拼团商品", remark="是：4497472000050001，否：4497472000050002")
	private String productType = "4497472000050002";
	
	@ZapcomApi(value="几人团", remark="需要几人参团，字符串类型的数字")
	private String collagePersonCount;
	
	@ZapcomApi(value = "商品下的所有sku实际库存")
	private int allSkuRealStock = 0;

	public int getAllSkuRealStock() {
		return allSkuRealStock;
	}

	public void setAllSkuRealStock(int allSkuRealStock) {
		this.allSkuRealStock = allSkuRealStock;
	}

	public String getStockNum() {
		return stockNum;
	}

	public void setStockNum(String stockNum) {
		this.stockNum = stockNum;
	}

	public String getGroupBuying() {
		return groupBuying;
	}

	public void setGroupBuying(String groupBuying) {
		this.groupBuying = groupBuying;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCollagePersonCount() {
		return collagePersonCount;
	}

	public void setCollagePersonCount(String collagePersonCount) {
		this.collagePersonCount = collagePersonCount;
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

	public String getMainpicUrl() {
		return mainpicUrl;
	}

	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getMarkPrice() {
		return markPrice;
	}

	public void setMarkPrice(String markPrice) {
		this.markPrice = markPrice;
	}

	public String getProClassifyTag() {
		return proClassifyTag;
	}

	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}

	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}
    
	
}
