package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.model.CdogProductComment;
import com.cmall.familyhas.api.model.PcAuthorityLogoModel;
import com.cmall.familyhas.api.model.ProductSkuInfoForApi;
import com.cmall.familyhas.api.model.PropertyInfoForProtuct;
import com.cmall.familyhas.api.model.Propertyinfo;
import com.cmall.familyhas.model.CommonProblem;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.helper.MoneyHelper;

public class ApiGetSkuInfoResult extends RootResult {
	
	@ZapcomApi(value="商品编号")
	private String productCode="";
	
	@ZapcomApi(value="商品名称")
	private String productName="";

	@ZapcomApi(value="销售价")
	private BigDecimal sellPrice=new BigDecimal(0);
	
	@ZapcomApi(value="市场价",remark="商品市场价")
	private BigDecimal marketPrice=new BigDecimal(0);
	
	@ZapcomApi(value="是否在售")
    private Integer flagSale   = 0 ;

	@ZapcomApi(value="商品上下架状态")
    private String productStatus = "";
    
	@ZapcomApi(value="商品视频链接")
	private String videoUrl = "" ;
	
	@ZapcomApi(value="品牌名称")
    private String brandName = "";

	@ZapcomApi(value="品牌编号")
    private String brandCode  = ""  ;

	@ZapcomApi(value="商品图片",remark="原图地址：picOldUrl；<br/>" +
									"新图地址：picNewUrl；<br/>"+
									"图片宽度：width；<br/>" +
									"图片高度：height")
    private PicInfo mainpicUrl = new PicInfo();
	
	@ZapcomApi(value = "商品评论列表")
	private List<CdogProductComment> productComment = new ArrayList<CdogProductComment>();
	
	@ZapcomApi(value = "好评率")
	private String highPraiseRate = "";
	
	@ZapcomApi(value = "全部评价数量")
	private Integer commentSumCounts = 0;
    
	@ZapcomApi(value="轮播图")
    private List<PicInfo> pcPicList = new ArrayList<PicInfo>();

	@ZapcomApi(value="内容图片")
    private List<PicInfo> discriptPicList = new ArrayList<PicInfo>();
	
	@ZapcomApi(value="sku信息")
    private List<ProductSkuInfoForApi> skuList = new ArrayList<ProductSkuInfoForApi>();
	
	@ZapcomApi(value="商品描述")
    private String discriptInfo = "";
	
	@ZapcomApi(value="商品标签")
    private List<String> labelsList = new ArrayList<String>();

	@ZapcomApi(value="最大购买数",remark="程序里暂时默认为99")
    private int maxBuyCount = 0;

	@ZapcomApi(value="商品规格",remark="商品的规格")
	private List<PropertyInfoForProtuct> propertyList = new ArrayList<PropertyInfoForProtuct>();
	
	@ZapcomApi(value="商品属性",remark="商品的自定义属性")
	private List<Propertyinfo> propertyInfoList = new ArrayList<Propertyinfo>();

//	@ZapcomApi(value="促销信息")
//	private List<ActivitySell> activityInfo = new ArrayList<ActivitySell>();

	@ZapcomApi(value="是否参加闪购",remark="1:是；0:否")
	private int flagCheap = 0; 

	@ZapcomApi(value="是否有视频",remark="1:是；0:否")
	private int exitVideo = 0; 
	
	@ZapcomApi(value="是否包含赠品",remark="1:是；0:否")
	private int flagIncludeGift = 0; 

	@ZapcomApi(value="其他相关显示语",remark="赠品")
	private String gift = ""; 
	
	@ZapcomApi(value="权威标志")
	private List<PcAuthorityLogoModel> authorityLogo = new ArrayList<PcAuthorityLogoModel>();
	
	@ZapcomApi(value="闪购结束时间")
	private String endTime = ""; 
	
	@ZapcomApi(value="家有价",remark="文本\"家有价\"")
	private String familyPriceName = ""; 

	@ZapcomApi(value="折扣差价",remark="市场价-当前售价")
	private  BigDecimal discount=new BigDecimal(0);
	
	@ZapcomApi(value="返现金额",remark="返现金额，默认是售价的5%")
	private  BigDecimal disMoney=new BigDecimal(0);

	@ZapcomApi(value="分享链接")
	private String shareUrl = "";
	
	@ZapcomApi(value="近30天销量")
	private String saleNum = "0";
	
	@ZapcomApi(value="是否被收藏",remark="0未收藏，1已收藏")
	private int collectionProduct = 0;

	@ZapcomApi(value="是否参加内购",remark="0未参加，1正在内购")
	private int vipSpecialActivity = 0;
	
	@ZapcomApi(value="内购提示信息",remark="内购每个账号每月最多购买5种商品，单个商品每月限买2件")
	private String vipSpecialTip = "";
	
	@ZapcomApi(value="内购价格",remark="内购价格，正在参加内购活动时此字段有效")
	private String vipSpecialPrice = "0";

	@ZapcomApi(value="促销系统限购数提示",remark="限购99件")
	private String limitBuyTip = "限购99件";

	@ZapcomApi(value="商品活动相关显示语",remark="闪购，内购,...")
	private String priceLabel = "";

	@ZapcomApi(value="是否参加促销活动",remark="0未参加，1正在参加活动")
	private int vipSecKill = 0;
	
	@ZapcomApi(value="按钮相关",remark="callBtn:电话；<br/>shopCarBtn:加入购物车；<br/>buyBtn:立即购买；<br/>获取到map中对应的值为1的时候")
	private Map<String,Integer> buttonMap = new HashMap<String, Integer>();

	@ZapcomApi(value="sku最低销售价")
	private String minSellPrice="";
	
	@ZapcomApi(value="sku最高销售价")
	private String maxSellPrice="";
	
	@ZapcomApi(value="服务器时间")
	private String sysDateTime = DateUtil.getSysDateTimeString();
	
	@ZapcomApi(value="其他相关显示语",remark="赠品")
	private List<String> otherShow = new ArrayList<String>();

	@ZapcomApi(value="是否海外购",remark="0:否，1:是")
    private String flagTheSea = "0" ;
	
	@ZapcomApi(value="常见问题",remark="现在只有跨境通商品有常见问题")
    private List<CommonProblem> commonProblem = new ArrayList<CommonProblem>();
	
	@ZapcomApi(value="小程序详情页是否展示分销价",remark="0:否，1:是")
    private String showFXPrice = "0" ;
	
	
	public String getShowFXPrice() {
		return showFXPrice;
	}

	public void setShowFXPrice(String showFXPrice) {
		this.showFXPrice = showFXPrice;
	}

	public BigDecimal getMarketPrice() {
//		return marketPrice.setScale(0, BigDecimal.ROUND_DOWN);
		return MoneyHelper.roundHalfUp(marketPrice);  // 兼容小数 - Yangcl
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getSellPrice() {
//		return sellPrice.setScale(0, BigDecimal.ROUND_DOWN);
		return MoneyHelper.roundHalfUp(sellPrice);  // 兼容小数 - Yangcl
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Integer getFlagSale() {
		return flagSale;
	}

	public void setFlagSale(Integer flagSale) {
		this.flagSale = flagSale;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getMaxBuyCount() {
		return maxBuyCount;
	}

	public void setMaxBuyCount(int maxBuyCount) {
		this.maxBuyCount = maxBuyCount;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<PropertyInfoForProtuct> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<PropertyInfoForProtuct> propertyList) {
		this.propertyList = propertyList;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getDiscriptInfo() {
		return discriptInfo;
	}

	public void setDiscriptInfo(String discriptInfo) {
		this.discriptInfo = discriptInfo;
	}

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public BigDecimal getDisMoney() {
		return disMoney.setScale(2, BigDecimal.ROUND_DOWN);	//截取小数点儿后
	}

	public void setDisMoney(BigDecimal disMoney) {
		this.disMoney = disMoney;
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

	public PicInfo getMainpicUrl() {
		return mainpicUrl;
	}

	public void setMainpicUrl(PicInfo mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}

	public List<PicInfo> getPcPicList() {
		return pcPicList;
	}

	public void setPcPicList(List<PicInfo> pcPicList) {
		this.pcPicList = pcPicList;
	}

	public List<PicInfo> getDiscriptPicList() {
		return discriptPicList;
	}

	public void setDiscriptPicList(List<PicInfo> discriptPicList) {
		this.discriptPicList = discriptPicList;
	}

	public int getExitVideo() {
		return exitVideo;
	}

	public void setExitVideo(int exitVideo) {
		this.exitVideo = exitVideo;
	}

	public List<Propertyinfo> getPropertyInfoList() {
		return propertyInfoList;
	}

	public void setPropertyInfoList(List<Propertyinfo> propertyInfoList) {
		this.propertyInfoList = propertyInfoList;
	}

	public String getFamilyPriceName() {
		return familyPriceName;
	}

	public void setFamilyPriceName(String familyPriceName) {
		this.familyPriceName = familyPriceName;
	}

	public List<ProductSkuInfoForApi> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<ProductSkuInfoForApi> skuList) {
		this.skuList = skuList;
	}

	public int getFlagCheap() {
		return flagCheap;
	}

	public void setFlagCheap(int flagCheap) {
		this.flagCheap = flagCheap;
	}

	public int getFlagIncludeGift() {
		return flagIncludeGift;
	}

	public void setFlagIncludeGift(int flagIncludeGift) {
		this.flagIncludeGift = flagIncludeGift;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public List<PcAuthorityLogoModel> getAuthorityLogo() {
		return authorityLogo;
	}

	public void setAuthorityLogo(List<PcAuthorityLogoModel> authorityLogo) {
		this.authorityLogo = authorityLogo;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(String saleNum) {
		this.saleNum = saleNum;
	}

	public int getCollectionProduct() {
		return collectionProduct;
	}

	public void setCollectionProduct(int collectionProduct) {
		this.collectionProduct = collectionProduct;
	}

	public String getSysDateTime() {
		return sysDateTime;
	}

	public void setSysDateTime(String sysDateTime) {
		this.sysDateTime = sysDateTime;
	}

	public int getVipSpecialActivity() {
		return vipSpecialActivity;
	}

	public void setVipSpecialActivity(int vipSpecialActivity) {
		this.vipSpecialActivity = vipSpecialActivity;
	}

	public String getVipSpecialPrice() {
		return vipSpecialPrice;
	}

	public void setVipSpecialPrice(String vipSpecialPrice) {
		this.vipSpecialPrice = vipSpecialPrice;
	}

	public String getVipSpecialTip() {
		return vipSpecialTip;
	}

	public void setVipSpecialTip(String vipSpecialTip) {
		this.vipSpecialTip = vipSpecialTip;
	}

	public String getLimitBuyTip() {
		return limitBuyTip;
	}

	public void setLimitBuyTip(String limitBuyTip) {
		this.limitBuyTip = limitBuyTip;
	}

	public String getPriceLabel() {
		return priceLabel;
	}

	public void setPriceLabel(String priceLabel) {
		this.priceLabel = priceLabel;
	}

	public Map<String, Integer> getButtonMap() {
		return buttonMap;
	}

	public void setButtonMap(Map<String, Integer> buttonMap) {
		this.buttonMap = buttonMap;
	}

	public String getMinSellPrice() {
		return minSellPrice;
	}

	public void setMinSellPrice(String minSellPrice) {
		this.minSellPrice = minSellPrice;
	}

	public String getMaxSellPrice() {
		return maxSellPrice;
	}

	public void setMaxSellPrice(String maxSellPrice) {
		this.maxSellPrice = maxSellPrice;
	}

	public int getVipSecKill() {
		return vipSecKill;
	}

	public void setVipSecKill(int vipSecKill) {
		this.vipSecKill = vipSecKill;
	}

	public List<String> getOtherShow() {
		return otherShow;
	}

	public void setOtherShow(List<String> otherShow) {
		this.otherShow = otherShow;
	}

	public String getFlagTheSea() {
		return flagTheSea;
	}

	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}

	public String getHighPraiseRate() {
		return highPraiseRate;
	}

	public void setHighPraiseRate(String highPraiseRate) {
		this.highPraiseRate = highPraiseRate;
	}

	public Integer getCommentSumCounts() {
		return commentSumCounts;
	}

	public void setCommentSumCounts(Integer commentSumCounts) {
		this.commentSumCounts = commentSumCounts;
	}

	public List<CommonProblem> getCommonProblem() {
		return commonProblem;
	}

	public void setCommonProblem(List<CommonProblem> commonProblem) {
		this.commonProblem = commonProblem;
	}

	public List<CdogProductComment> getProductComment() {
		return productComment;
	}

	public void setProductComment(List<CdogProductComment> productComment) {
		this.productComment = productComment;
	}
	
}
