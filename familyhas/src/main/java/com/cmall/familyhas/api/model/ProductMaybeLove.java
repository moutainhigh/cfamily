package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ProductMaybeLove {

	@ZapcomApi(value = "商品编号")
	private String procuctCode = "";

	@ZapcomApi(value = "商品名称")
	private String productNameString = "";

	@ZapcomApi(value = "商品销售价")
	private String productPrice = "";

	@ZapcomApi(value = "商品图片")
	private String mainpic_url = "";

	@ZapcomApi(value = "市场价")
	private String market_price = "";

	@ZapcomApi(value = "是否海外购", remark = "0:否，1:是")
	private String flagTheSea = "0";

	@ZapcomApi(value = "商品标签", remark = "LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
	private List<String> labelsList = new ArrayList<String>();
	
	@ZapcomApi(value="商品标签图片地址",remark="3.9.2以后开始使用")
	private String labelsPic = "";
	@ZapcomApi(value="推荐结果唯一标识id",remark="wangqx 4月5号添加,从百分点请求推荐结果返回的推荐商品唯一标示,由于改用达观数据平台,此字段存达观数据平台的requestId")
	private String recommendId = "";

	@ZapcomApi(value = "小用户编号")
	private String smallSellerCode = "";
	@ZapcomApi(value="拼团标识", remark="拼团编码：4497472600010024")
	private String groupBuying = "";
	@ZapcomApi(value="是否拼团商品", remark="是：4497472000050001，否：4497472000050002")
	private String productType = "4497472000050002";
	
	@ZapcomApi(value="拼团商品原价", remark="如果是拼团商品的话，需要显示的划线价（原实际售价）")
	private BigDecimal skuPrice;
	
	@ZapcomApi(value="拼团商品展示价", remark="如果是拼团商品的话，需要显示的拼团购买价格")
	private BigDecimal groupBuyingPrice;
	
	@ZapcomApi(value="几人团", remark="需要几人参团，字符串类型的数字")
	private String collagePersonCount;
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();

	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag;
	
	public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}

	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}

	public String getCollagePersonCount() {
		return collagePersonCount;
	}

	public void setCollagePersonCount(String collagePersonCount) {
		this.collagePersonCount = collagePersonCount;
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

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public BigDecimal getGroupBuyingPrice() {
		return groupBuyingPrice;
	}

	public void setGroupBuyingPrice(BigDecimal groupBuyingPrice) {
		this.groupBuyingPrice = groupBuyingPrice;
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
	

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public String getProcuctCode() {
		return procuctCode;
	}

	public void setProcuctCode(String procuctCode) {
		this.procuctCode = procuctCode;
	}

	public String getProductNameString() {
		return productNameString;
	}

	public void setProductNameString(String productNameString) {
		this.productNameString = productNameString;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getMainpic_url() {
		return mainpic_url;
	}

	public void setMainpic_url(String mainpic_url) {
		this.mainpic_url = mainpic_url;
	}

	public String getFlagTheSea() {
		return flagTheSea;
	}

	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}

	public String getLabelsPic() {
		return labelsPic;
	}

	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}

	public String getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}
	
	
    
}
