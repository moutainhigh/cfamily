package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class EventListGoodsEntity {

	@ZapcomApi(value="商品参加活动",remark="商品所参加的：内购，特价，闪购，秒杀，拍卖等标签都在改字段存放")
	private List<String> activityList = new ArrayList<String>();
	@ZapcomApi(value = "购买状态", remark = "购买的状态 只有该字段等于1时才可以允许购买按钮点击    状态值对应：1(允许购买),2(活动尚未开始),3(活动已结束),4(活动进行中但是不可购买),5(其他状态位),6(商品下架),7(已打限购上限)", verify = "in=0,1,2,3,4,5,6")
	private int buyStatus= 0;
	@ZapcomApi(value="商品编号")
	private String commodityCode="";
	@ZapcomApi(value="商品标签",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
    private List<String> labelsList = new ArrayList<String>();
	@ZapcomApi(value="商品名称")
	private String commodityName="";
	@ZapcomApi(value="商品图片")
	private String commodityPic="";
	@ZapcomApi(value="现价")
	private BigDecimal currentPrice=new BigDecimal(0);
	@ZapcomApi(value="是否海外购",remark="0代表不是海外购 1代表海外购")
	private String flagTheSea="";
	@ZapcomApi(value="商品参加赠品",remark="该字段为3.8.0版本及以后版本提供，该字段只放赠品")
	private List<String> otherShow = new ArrayList<String>();
	@ZapcomApi(value="商品状态")
	private String productStatus="";
	@ZapcomApi(value="商品规格")
	private List<PropertyInfoForProtuct> propertyList = new ArrayList<PropertyInfoForProtuct>();
	@ZapcomApi(value="月销量")
	private int saleNum=0;
	@ZapcomApi(value="商品对应的sku列表")
	/*private List<ProductSkuInfoForApi> skuList = new ArrayList<ProductSkuInfoForApi>();*/
	private List<SkuGoodsDetail> skuList = new ArrayList<SkuGoodsDetail>();
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag="";
	@ZapcomApi(value="商品参加活动标签",remark="特价，秒杀，闪购，赠品，券等")
	private List<String> tagList = new ArrayList<String>();

	@ZapcomApi(value="带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
	
	@ZapcomApi(value = "原价")
	private BigDecimal originalPrice;
	@ZapcomApi(value = "商品标签图片地址", remark = "3.9.2以后开始使用")
	private String labelsPic;
	@ZapcomApi(value = "拼团标识", remark = "拼团编码：4497472600010024")
	private String groupBuying = "";
	
	@ZapcomApi(value = "是否拼团商品", remark = "是：4497472000050001，否：4497472000050002")
	private String productType = "4497472000050002";

	@ZapcomApi(value = "拼团商品原价", remark = "如果是拼团商品的话，需要显示的划线价（原实际售价）")
	private BigDecimal skuPrice;

	@ZapcomApi(value = "拼团商品展示价", remark = "如果是拼团商品的话，需要显示的拼团购买价格")
	private BigDecimal groupBuyingPrice;

	@ZapcomApi(value = "几人团", remark = "需要几人参团，字符串类型的数字")
	private String collagePersonCount;
	
	@ZapcomApi(value = "库存")
	private int sku_stock   = 0;
	
	@ZapcomApi(value="所有库存和，用于库存不足提醒使用")
	private int allSkuRealStoc=0;
	
	public int getAllSkuRealStoc() {
		return allSkuRealStoc;
	}
	public void setAllSkuRealStoc(int allSkuRealStoc) {
		this.allSkuRealStoc = allSkuRealStoc;
	}
	
	public int getSku_stock() {
		return sku_stock;
	}
	public void setSku_stock(int sku_stock) {
		this.sku_stock = sku_stock;
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
	public String getCollagePersonCount() {
		return collagePersonCount;
	}
	public void setCollagePersonCount(String collagePersonCount) {
		this.collagePersonCount = collagePersonCount;
	}
	public String getLabelsPic() {
		return labelsPic;
	}
	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

		public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
		public String getProClassifyTag() {
		return proClassifyTag;
	}
	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}
	/**
	 * @return the activityList
	 */
	public List<String> getActivityList() {
		return activityList;
	}
	/**
	 * @param activityList the activityList to set
	 */
	public void setActivityList(List<String> activityList) {
		this.activityList = activityList;
	}
	/**
	 * @return the commodityCode
	 */
	public String getCommodityCode() {
		return commodityCode;
	}
	/**
	 * @param commodityCode the commodityCode to set
	 */
	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}
	/**
	 * @return the commodityName
	 */
	public String getCommodityName() {
		return commodityName;
	}
	/**
	 * @param commodityName the commodityName to set
	 */
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	/**
	 * @return the commodityPic
	 */
	public String getCommodityPic() {
		return commodityPic;
	}
	/**
	 * @param commodityPic the commodityPic to set
	 */
	public void setCommodityPic(String commodityPic) {
		this.commodityPic = commodityPic;
	}
	/**
	 * @return the currentPrice
	 */
	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}
	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
	/**
	 * @return the flagTheSea
	 */
	public String getFlagTheSea() {
		return flagTheSea;
	}
	/**
	 * @param flagTheSea the flagTheSea to set
	 */
	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}
	/**
	 * @return the otherShow
	 */
	public List<String> getOtherShow() {
		return otherShow;
	}
	/**
	 * @param otherShow the otherShow to set
	 */
	public void setOtherShow(List<String> otherShow) {
		this.otherShow = otherShow;
	}
	/**
	 * @return the productStatus
	 */
	public String getProductStatus() {
		return productStatus;
	}
	/**
	 * @param productStatus the productStatus to set
	 */
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	/**
	 * @return the propertyList
	 */
	public List<PropertyInfoForProtuct> getPropertyList() {
		return propertyList;
	}
	/**
	 * @param propertyList the propertyList to set
	 */
	public void setPropertyList(List<PropertyInfoForProtuct> propertyList) {
		this.propertyList = propertyList;
	}
	/**
	 * @return the saleNum
	 */
	public int getSaleNum() {
		return saleNum;
	}
	/**
	 * @param saleNum the saleNum to set
	 */
	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}
	public int getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}
	public List<SkuGoodsDetail> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<SkuGoodsDetail> skuList) {
		this.skuList = skuList;
	}
	/**
	 * @return the labelsList
	 */
	public List<String> getLabelsList() {
		return labelsList;
	}
	/**
	 * @param labelsList the labelsList to set
	 */
	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}
	public List<TagInfo> getTagInfoList() {
		return tagInfoList;
	}
	public void setTagInfoList(List<TagInfo> tagInfoList) {
		this.tagInfoList = tagInfoList;
	}
	
	
}
