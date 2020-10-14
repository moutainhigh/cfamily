package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.cmall.productcenter.model.PicInfo;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topdo.TopUp;

public class TodayProduct {
	@ZapcomApi(value = "商品编号")
	private String id = "";
	@ZapcomApi(value = "商品名称")
	private String name = "";
	@ZapcomApi(value = "商品市场价格")
	private String markPrice = "";
	@ZapcomApi(value = "商品售价")
	private String salePrice = "";
	@ZapcomApi(value = "折扣价",remark="商品折扣价由市场价和售价的差值")
	private String discountPrice = "";
	@ZapcomApi(value = "折扣率",remark="商品折扣率由市场价和售价的比值（如果折扣率为100%  则返回100）")
	private String discount = "";

	@ZapcomApi(value = "已售件数")
	private int saleNo = 0;
	@ZapcomApi(value = "是否有视频",remark="0表示无视频，1表示有视频",demo="0,1")
	private int hasVideo = 0;
	@ZapcomApi(value = "商品列表图（方图）")
	private String productPic = "";
	@ZapcomApi(value = "商品广告图")
	private String adPic = "";
	
	@ZapcomApi(value="轮播图" , remark="商品轮播图")
	private List<PicInfo> pcPicList = new ArrayList<PicInfo>();

	@ZapcomApi(value = "播出时间",demo="18:30")
	private String playTime = "";
	@ZapcomApi(value = "商品标签")
	private List<String> productFlag = new ArrayList<String>();
	@ZapcomApi(value = "商品活动标签",remark="秒杀、闪购、拼团、特价、会员日、满减、领券、赠品（最多展示三个）")
	private List<String> tagList = new ArrayList<String>();
	@ZapcomApi(value = "商品详情连接")
	private String productDetail = "";
	@ZapcomApi(value = "结束时间")
	private String endTime = "";
	@ZapcomApi(value = "直播状态",remark="0表示已播，1表示正在直播，2表示未直播",demo="0,1,2")
	private int playStatus = 0;
	@ZapcomApi(value="库存")
	private int stock = 0;
	@ZapcomApi(value="库存标识",remark="库存>0：有货   ;  库存=0：售罄")
	private String flagStock = "";
	@ZapcomApi(value="商品活动相关显示语",remark="闪购，内购，特价")
	private List<String> activityList = new ArrayList<String>();
	
	@ZapcomApi(value="其他相关显示语",remark="赠品")
	private List<String> otherShow = new ArrayList<String>();
	
	@ZapcomApi(value="广告语")
	private String skuAdv = "";
	
	@ZapcomApi(value="销量")
	private int saleNum = 0;
	
	@ZapcomApi(value="带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();	
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag="";
	
	@ZapcomApi(value = "TV直播链接",remark="直播互动需要此字段")
	private String videoUrlTV = "";
	
	@ZapcomApi(value = "视频回放链接",remark="回放已播出的节目")
	private String playbackUrl = "";
	
	@ZapcomApi(value = "折扣/立省类型")
	private String eventType = "";
	
	@ZapcomApi(value = "折扣/立省金额")
	private String saveValue = "";
	
	@ZapcomApi(value = "节目单编号")
	private String formId = "";
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();
	
	@ZapcomApi(value="关联主持人id",remark="多个主持人逗号分隔")
	private String hostIds = "";

	public String getHostIds() {
		return hostIds;
	}
	public void setHostIds(String hostIds) {
		this.hostIds = hostIds;
	}
	public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}
	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}

	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getVideoUrlTV() {
		return videoUrlTV;
	}
	public void setVideoUrlTV(String videoUrlTV) {
		this.videoUrlTV = videoUrlTV;
	}
	
	public String getPlaybackUrl() {
		return playbackUrl;
	}
	public void setPlaybackUrl(String playbackUrl) {
		this.playbackUrl = playbackUrl;
	}
	public String getProClassifyTag() {
		return proClassifyTag;
	}
	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMarkPrice() {
		return markPrice;
	}
	public void setMarkPrice(String markPrice) {
		this.markPrice = markPrice;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getSaveValue() {
		return saveValue;
	}
	public void setSaveValue(String saveValue) {
		this.saveValue = saveValue;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public int getSaleNo() {
		return saleNo;
	}
	public void setSaleNo(int saleNo) {
		this.saleNo = saleNo;
	}
	public int getHasVideo() {
		return hasVideo;
	}
	public void setHasVideo(int hasVideo) {
		this.hasVideo = hasVideo;
	}
	public String getProductPic() {
		return productPic;
	}
	public void setProductPic(String productPic) {
		this.productPic = productPic;
	}
	public String getAdPic() {
		return adPic;
	}
	public void setAdPic(String adPic) {
		this.adPic = adPic;
	}
	public String getPlayTime() {
		return playTime;
	}
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
	public List<String> getProductFlag() {
		return productFlag;
	}
	public void setProductFlag(List<String> productFlag) {
		this.productFlag = productFlag;
	}
	public String getProductDetail() {
		return productDetail;
	}
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getPlayStatus() {
		return playStatus;
	}
	public void setPlayStatus(int playStatus) {
		this.playStatus = playStatus;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getFlagStock() {
		return flagStock;
	}
	public void setFlagStock(String flagStock) {
		this.flagStock = flagStock;
	}
	public List<String> getActivityList() {
		return activityList;
	}
	public void setActivityList(List<String> activityList) {
		this.activityList = activityList;
	}
	/**
	 * 获取  skuAdv
	 */
	public String getSkuAdv() {
		return skuAdv;
	}
	/**
	 * 设置 
	 * @param skuAdv 
	 */
	public void setSkuAdv(String skuAdv) {
		this.skuAdv = skuAdv;
	}
	/**
	 * 获取  saleNum
	 */
	public int getSaleNum() {
		return saleNum;
	}
	/**
	 * 设置 
	 * @param saleNum 
	 */
	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}
	public List<String> getOtherShow() {
		return otherShow;
	}
	public void setOtherShow(List<String> otherShow) {
		this.otherShow = otherShow;
	}
	public List<PicInfo> getPcPicList() {
		return pcPicList;
	}
	public void setPcPicList(List<PicInfo> pcPicList) {
		this.pcPicList = pcPicList;
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
	
}
