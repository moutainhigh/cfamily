package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.cmall.familyhas.api.model.BusinessLicenseModel;
import com.cmall.familyhas.api.model.CdogProductComment;
import com.cmall.familyhas.api.model.CityInfo;
import com.cmall.familyhas.api.model.ProductSkuInfoForApi;
import com.cmall.familyhas.api.model.ProvinceInfo;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.CategoryBaseInfo;
import com.cmall.productcenter.model.PicInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogo;
import com.srnpr.xmassystem.modelproduct.PlusModelCommonProblem;
import com.srnpr.xmassystem.modelproduct.PlusModelPropertyInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetSkuInfoNewResult extends RootResult {
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag="";

	@ZapcomApi(value="商品编号")
	private String productCode="";
	
	@ZapcomApi(value="商品名称")
	private String productName="";

	@ZapcomApi(value="商品上下架状态")
    private String productStatus = "";
    
	@ZapcomApi(value="商品视频链接")
	private String videoUrl = "" ;
	
	@ZapcomApi(value="商品介绍视频")
	private String productDescVideo = "" ;
	
	@ZapcomApi(value="商品视频封面图")
	private String videoMainPic = "" ;
	
	@ZapcomApi(value="商品直播视频链接")
	private String liveUrl = "" ;
	
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

	@ZapcomApi(value="最大购买数",remark="程序里暂时默认为99")
    private int maxBuyCount = 0;

	@ZapcomApi(value="商品规格",remark="商品的规格")
	private List<PlusModelSkuPropertyInfo> propertyList = new ArrayList<PlusModelSkuPropertyInfo>();
	
	@ZapcomApi(value="商品属性",remark="商品的自定义属性")
	private List<PlusModelPropertyInfo> propertyInfoList = new ArrayList<PlusModelPropertyInfo>();

	@ZapcomApi(value="是否参加闪购",remark="1:是；0:否")
	private int flagCheap = 0; 

	@ZapcomApi(value="是否有视频",remark="1:是；0:否")
	private int exitVideo = 0; 
	
	@ZapcomApi(value="是否包含赠品",remark="1:是；0:否")
	private int flagIncludeGift = 0; 

	@ZapcomApi(value="其他相关显示语",remark="赠品")
	private String gift = ""; 
	
	@ZapcomApi(value="权威标志")
	private List<PlusModelAuthorityLogo> authorityLogo = new ArrayList<PlusModelAuthorityLogo>();
	
	@ZapcomApi(value="闪购结束时间")
	private String endTime = ""; 
	
	@ZapcomApi(value="返现金额",remark="返现金额，默认是售价的5%")
	private  BigDecimal disMoney=new BigDecimal(0);
	
	@ZapcomApi(value="是否显示返现金额",remark="1显示，其他：不显示")
	private  int isShowDisMoney=0;

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

	@ZapcomApi(value="商品活动相关显示语",remark="闪购，内购,...")
	private String priceLabel = "";

	@ZapcomApi(value="是否参加促销活动",remark="0未参加，1正在参加活动")
	private int vipSecKill = 0;
	
	@ZapcomApi(value="按钮相关",remark="callBtn:电话；<br/>shopCarBtn:加入购物车；<br/>buyBtn:立即购买；<br/>获取到map中对应的值为1的时候(已转移到获取sku价格跟库存接口上)")
	private Map<String,Integer> buttonMap = new HashMap<String, Integer>();

	@ZapcomApi(value="服务器时间")
	private String sysDateTime = DateUtil.getSysDateTimeString();
	
	@ZapcomApi(value="其他相关显示语",remark="赠品")
	private List<String> otherShow = new ArrayList<String>();

	@ZapcomApi(value="是否海外购",remark="0:否，1:是")
    private String flagTheSea = "0" ;
	
	@ZapcomApi(value="商品标签",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品(3.9.2已作废)")
    private List<String> labelsList = new ArrayList<String>();

	@ZapcomApi(value="商品标签对应的图片地址",remark="3.9.2开始用")
    private String labelsPic = "" ;
	
	@ZapcomApi(value="活动图片",remark="396需求 活动图片，比如618活动的横条")
	private PicInfo eventLabelPic = new PicInfo();
	
	@ZapcomApi(value="活动图片跳转链接",remark="396需求 活动图片跳转链接")
	private String eventLabelPicSkip = "";
	
	@ZapcomApi(value="惠家有活动编号",remark="CX2015072800004")
	private String eventCode = "";
	
	@ZapcomApi(value="LD活动编号",remark="123456")
	private String ldEventCode = "";
	
	@ZapcomApi(value="常见问题",remark="现在只有跨境通商品有常见问题,2016-01-06增加麦乐购")
    private List<PlusModelCommonProblem> commonProblem = new ArrayList<PlusModelCommonProblem>();

	@ZapcomApi(value="默认配送地址",remark="未登录时返回可配送地址的第一个")
	private CityInfo defaultAddress = new CityInfo();
	
	@ZapcomApi(value="支持配送地址列表")
	private List<ProvinceInfo> addressList = new ArrayList<ProvinceInfo>();

	@ZapcomApi(value="商品所属分类")
	private List<CategoryBaseInfo> categoryList = new ArrayList<CategoryBaseInfo>();
	
	@ZapcomApi(value="商品广告语",remark="卖点信息")
    private String sellingPoint = "" ;
	
	@ZapcomApi(value="图文详情的页面链接",remark="值暂时为空")
    private String tuwenUrl = "" ;
	@ZapcomApi(value="规格参数的页面链接",remark="值暂时为空")
    private String guigeUrl = "" ;
	@ZapcomApi(value="常见问题的页面链接",remark="值暂时为空")
    private String FQAUrl = "" ;
	
	@ZapcomApi(value="提示语",remark="商品详情页提示语信息",demo="")
	private List<String> tips = new ArrayList<String>();
	
	@ZapcomApi(value="惠惠农场入口参数",remark="如果里面地址和logo都为空则不展示",demo="")
	private ShowFormInfo hhFarm = new ShowFormInfo();
	
	@ZapcomApi(value="顶部参数")
	private TopGuide topGuide = new TopGuide();
	
	@ZapcomApi(value="供应商编号")
	private String smallSellerCode = "";
	
	@ZapcomApi(value="供应商名称")
	private String sellerCompanyName = "";
	
	@ZapcomApi(value="购买状态", remark="状态值对应：1(允许购买),2(活动尚未开始),3(活动已结束),4(活动进行中但是不可购买),5(其他状态位)")
	private String buyStatus = "1";
	
	@ZapcomApi(value="是否展示营业执照",remark = "0不展示，1展示")
	private String isShowBusinessLicense = "0";
	
	@ZapcomApi(value="是否展示营业执照",remark = "0不展示，1展示")
	private BusinessLicenseModel businessLicense ;
	
	@ZapcomApi(value="是否橙意卡商品",demo = "0不是，1是",remark="如果是橙意卡商品则购买时需要跳转到特定页面")
	private int plusFlag = 0;
	
	@ZapcomApi(value="是否支持在线支付立减",demo = "N：不支持，Y：支持",remark="Y")
	private String supplyOnlinePayCut = "N";
	
	@ZapcomApi(value="立减金额",demo = "10.00",remark="10.00")
	private BigDecimal onlinePayCut = BigDecimal.ZERO;
	
	@ZapcomApi(value="小程序推广赚分享的参数内容",remark = "")
	private String shortContent = "";
	
	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}

	public String getProClassifyTag() {
		return proClassifyTag;
	}

	public BigDecimal getOnlinePayCut() {
		return onlinePayCut;
	}

	public void setOnlinePayCut(BigDecimal onlinePayCut) {
		this.onlinePayCut = onlinePayCut;
	}

	public String getSupplyOnlinePayCut() {
		return supplyOnlinePayCut;
	}

	public void setSupplyOnlinePayCut(String supplyOnlinePayCut) {
		this.supplyOnlinePayCut = supplyOnlinePayCut;
	}
	
	public ShowFormInfo getHhFarm() {
		return hhFarm;
	}

	public void setHhFarm(ShowFormInfo hhFarm) {
		this.hhFarm = hhFarm;
	}

	public TopGuide getTopGuide() {
		return topGuide;
	}

	public void setTopGuide(TopGuide topGuide) {
		this.topGuide = topGuide;
	}

	public String getProductDescVideo() {
		return productDescVideo;
	}

	public void setProductDescVideo(String productDescVideo) {
		this.productDescVideo = productDescVideo;
	}

	public String getVideoMainPic() {
		return videoMainPic;
	}

	public void setVideoMainPic(String videoMainPic) {
		this.videoMainPic = videoMainPic;
	}
	
	public String getIsShowBusinessLicense() {
		return isShowBusinessLicense;
	}

	public int getPlusFlag() {
		return plusFlag;
	}

	public void setPlusFlag(int plusFlag) {
		this.plusFlag = plusFlag;
	}

	public void setIsShowBusinessLicense(String isShowBusinessLicense) {
		this.isShowBusinessLicense = isShowBusinessLicense;
	}

	public BusinessLicenseModel getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(BusinessLicenseModel businessLicense) {
		this.businessLicense = businessLicense;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public PicInfo getMainpicUrl() {
		return mainpicUrl;
	}

	public void setMainpicUrl(PicInfo mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}

	public List<CdogProductComment> getProductComment() {
		return productComment;
	}

	public void setProductComment(List<CdogProductComment> productComment) {
		this.productComment = productComment;
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

	public List<ProductSkuInfoForApi> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<ProductSkuInfoForApi> skuList) {
		this.skuList = skuList;
	}

	public int getMaxBuyCount() {
		return maxBuyCount;
	}

	public void setMaxBuyCount(int maxBuyCount) {
		this.maxBuyCount = maxBuyCount;
	}

	public List<PlusModelSkuPropertyInfo> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<PlusModelSkuPropertyInfo> propertyList) {
		this.propertyList = propertyList;
	}

	public List<PlusModelPropertyInfo> getPropertyInfoList() {
		return propertyInfoList;
	}

	public void setPropertyInfoList(List<PlusModelPropertyInfo> propertyInfoList) {
		this.propertyInfoList = propertyInfoList;
	}

	public int getFlagCheap() {
		return flagCheap;
	}

	public void setFlagCheap(int flagCheap) {
		this.flagCheap = flagCheap;
	}

	public int getExitVideo() {
		return exitVideo;
	}

	public void setExitVideo(int exitVideo) {
		this.exitVideo = exitVideo;
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


	public List<PlusModelAuthorityLogo> getAuthorityLogo() {
		return authorityLogo;
	}

	public void setAuthorityLogo(List<PlusModelAuthorityLogo> authorityLogo) {
		this.authorityLogo = authorityLogo;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getDisMoney() {
		return disMoney;
	}

	public void setDisMoney(BigDecimal disMoney) {
		this.disMoney = disMoney;
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

	public int getVipSpecialActivity() {
		return vipSpecialActivity;
	}

	public void setVipSpecialActivity(int vipSpecialActivity) {
		this.vipSpecialActivity = vipSpecialActivity;
	}

	public String getVipSpecialTip() {
		return vipSpecialTip;
	}

	public void setVipSpecialTip(String vipSpecialTip) {
		this.vipSpecialTip = vipSpecialTip;
	}

	public String getVipSpecialPrice() {
		return vipSpecialPrice;
	}

	public void setVipSpecialPrice(String vipSpecialPrice) {
		this.vipSpecialPrice = vipSpecialPrice;
	}

	public String getPriceLabel() {
		return priceLabel;
	}

	public void setPriceLabel(String priceLabel) {
		this.priceLabel = priceLabel;
	}

	public int getVipSecKill() {
		return vipSecKill;
	}

	public void setVipSecKill(int vipSecKill) {
		this.vipSecKill = vipSecKill;
	}

	public Map<String, Integer> getButtonMap() {
		return buttonMap;
	}

	public void setButtonMap(Map<String, Integer> buttonMap) {
		this.buttonMap = buttonMap;
	}

	public String getSysDateTime() {
		return sysDateTime;
	}

	public void setSysDateTime(String sysDateTime) {
		this.sysDateTime = sysDateTime;
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

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public CityInfo getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(CityInfo defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public List<ProvinceInfo> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<ProvinceInfo> addressList) {
		this.addressList = addressList;
	}

	public List<PlusModelCommonProblem> getCommonProblem() {
		return commonProblem;
	}

	public void setCommonProblem(List<PlusModelCommonProblem> commonProblem) {
		this.commonProblem = commonProblem;
	}

	public List<CategoryBaseInfo> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryBaseInfo> categoryList) {
		this.categoryList = categoryList;
	}

	public String getLabelsPic() {
		return labelsPic;
	}

	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	public String getTuwenUrl() {
		return tuwenUrl;
	}

	public void setTuwenUrl(String tuwenUrl) {
		this.tuwenUrl = tuwenUrl;
	}

	public String getGuigeUrl() {
		return guigeUrl;
	}

	public void setGuigeUrl(String guigeUrl) {
		this.guigeUrl = guigeUrl;
	}

	@JSONField(name = "fqaurl")
	public String getFQAUrl() {
		return FQAUrl;
	}

	public void setFQAUrl(String fQAUrl) {
		FQAUrl = fQAUrl;
	}

	public List<String> getTips() {
		return tips;
	}

	public void setTips(List<String> tips) {
		this.tips = tips;
	}

	public PicInfo getEventLabelPic() {
		return eventLabelPic;
	}

	public void setEventLabelPic(PicInfo eventLabelPic) {
		this.eventLabelPic = eventLabelPic;
	}

	public String getEventLabelPicSkip() {
		return eventLabelPicSkip;
	}

	public void setEventLabelPicSkip(String eventLabelPicSkip) {
		this.eventLabelPicSkip = eventLabelPicSkip;
	}

	public int getIsShowDisMoney() {
		return isShowDisMoney;
	}

	public void setIsShowDisMoney(int isShowDisMoney) {
		this.isShowDisMoney = isShowDisMoney;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public String getSellerCompanyName() {
		return sellerCompanyName;
	}

	public void setSellerCompanyName(String sellerCompanyName) {
		this.sellerCompanyName = sellerCompanyName;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getLdEventCode() {
		return ldEventCode;
	}

	public void setLdEventCode(String ldEventCode) {
		this.ldEventCode = ldEventCode;
	}

}
