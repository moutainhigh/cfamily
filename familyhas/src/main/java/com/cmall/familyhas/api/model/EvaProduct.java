package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class EvaProduct {

	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	
	@ZapcomApi(value = "商品名称")
	private String productName = "";
	
	@ZapcomApi(value = "图片")
	private String mainpicUrl = "";
	
	@ZapcomApi(value = "销售价")
	private String sellPrice = "";
	
	@ZapcomApi(value = "市场价")
	private String markPrice = "";
	
	@ZapcomApi(value="标签集合",remark="秒杀、闪购、拼团、特价、会员日、满减、赠品（最多展示三个）")
	private List<String> tagList = new ArrayList<String>();
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag="";
	
	@ZapcomApi(value="几人团",remark="示例：2人团")
	private String manyCollage="";
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();
	

	public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}

	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}

	public String getManyCollage() {
		return manyCollage;
	}

	public void setManyCollage(String manyCollage) {
		this.manyCollage = manyCollage;
	}

	public String getProClassifyTag() {
		return proClassifyTag;
	}

	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
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

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	
}
