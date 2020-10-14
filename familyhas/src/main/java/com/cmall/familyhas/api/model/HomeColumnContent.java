package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 首页版式栏目内容
 * @author ligj 
 *
 */
public class HomeColumnContent {
	
	@ZapcomApi(value = "图片地址;558商品评价封面图地址")
	private String picture = "";
	
	@ZapcomApi(value = "链接类型",remark="4497471600020001：URL\n"+
							"4497471600020002：关键词搜索"+
							"4497471600020003：分类搜索\n"+
							"4497471600020004：商品详情\n" +
							"4497471600020005：显示浮层\n" +
							"4497471600020006: 抽奖")
	private String showmoreLinktype = "";
	
	@ZapcomApi(value = "链接值")
	private String showmoreLinkvalue = "";
	
	@ZapcomApi(value = "是否分享",remark="链接类型为“URL”此字段有效;\n" +
							"449746250001：是\n"+
							"449746250002：否")
	private String isShare = "";
	
	@ZapcomApi(value = "商品信息",remark="链接类型为“商品详情：4497471600020004”此字段有效")
	private HomeColumnContentProductInfo productInfo = new HomeColumnContentProductInfo();
	
	@ZapcomApi(value = "开始时间")
	private String startTime = "";
	
	@ZapcomApi(value = "结束时间")
	private String endTime = "";
	
	@ZapcomApi(value = "位置")
	private int posion = 0;
	
	@ZapcomApi(value = "标题")
	private String title = "";
	
	@ZapcomApi(value = "标题颜色")
	private String titleColor = "";
	
	@ZapcomApi(value = "描述")
	private String description = "";
	
	@ZapcomApi(value = "描述颜色")
	private String descriptionColor = "";
	
	@ZapcomApi(value = "分享标题")
	private String shareTitle = "";
	
	@ZapcomApi(value = "分享内容")
	private String shareContent = "";
	
	@ZapcomApi(value = "分享链接")
	private String shareLink = "";
	
	@ZapcomApi(value = "分享图片")
	private String sharePic = "";

	@ZapcomApi(value = "TV直播链接",remark="栏目类型为TV直播时此字段有效:4497471600010010")
	private String videoUrlTV = "";

	@ZapcomApi(value = "TV直播封面图片",remark="栏目类型为TV直播时此字段有效:4497471600010010")
	private String picUrl = "";
	
	@ZapcomApi(value = "图片高度",remark="栏目类型为一栏广告或轮播广告时生效时此字段有效:4497471600010002,4497471600010001")
	private int picHeight = 0;
	
	@ZapcomApi(value="楼层模板")
	private String floorModel = "";		
	
	@ZapcomApi(value = "封面商品视频链接",remark="栏目类型为视频直播模板时此字段有效:4497471600010020")
	private String videoLink = "";
	
	@ZapcomApi(value = "图片高度",remark="栏目类型为两栏或多栏广告或左两栏、右两栏推荐时此字段有效;558商品评价封面图高度")
	private int picOriginHeight = 0;
	
	@ZapcomApi(value = "图片宽度",remark="栏目类型为两栏或多栏广告或左两栏、右两栏推荐时此字段有效;558商品评价封面图宽度")
	private int picOriginWidth = 0;
	
	@ZapcomApi(value = "是否抢光",remark="449746250001：是,449746250002：否")
	private String isLoot = ""; 
	
	@ZapcomApi(value="几人团",remark="示例：2人团")
	private String manyCollage="";
	
	@ZapcomApi(value="拼团类型",remark="拼团类型：（4497473400050001：普通团，4497473400050002：邀新团）")
	private String collageType="4497473400050001";
	
	@ZapcomApi(value="栏目编号",remark="COL150316100001")
	private String columnCode="";
	
	
	@ZapcomApi(value="分类编号",remark="针对跳转为商品列表类型,搜索为关键字‘分类名称’,再加上分类编号作为优化补充")
	private String categoryCode="";
	
	@ZapcomApi(value="活动商品进行的进度",remark="目前参与进度显示的商品活动有 拼团列表，秒杀列表，闪购列表")
	private String rateOfProgress="";
	
	public String getRateOfProgress() {
		return rateOfProgress;
	}

	public void setRateOfProgress(String rateOfProgress) {
		this.rateOfProgress = rateOfProgress;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getColumnCode() {
		return columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public int getPicOriginHeight() {
		return picOriginHeight;
	}

	public void setPicOriginHeight(int picOriginHeight) {
		this.picOriginHeight = picOriginHeight;
	}

	public int getPicOriginWidth() {
		return picOriginWidth;
	}

	public void setPicOriginWidth(int picOriginWidth) {
		this.picOriginWidth = picOriginWidth;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getShowmoreLinktype() {
		return showmoreLinktype;
	}

	public void setShowmoreLinktype(String showmoreLinktype) {
		this.showmoreLinktype = showmoreLinktype;
	}

	public String getShowmoreLinkvalue() {
		return showmoreLinkvalue;
	}

	public void setShowmoreLinkvalue(String showmoreLinkvalue) {
		this.showmoreLinkvalue = showmoreLinkvalue;
	}

	public String getIsShare() {
		return isShare;
	}

	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}

	public HomeColumnContentProductInfo getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(HomeColumnContentProductInfo productInfo) {
		this.productInfo = productInfo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getPosion() {
		return posion;
	}

	public void setPosion(int posion) {
		this.posion = posion;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionColor() {
		return descriptionColor;
	}

	public void setDescriptionColor(String descriptionColor) {
		this.descriptionColor = descriptionColor;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	public String getSharePic() {
		return sharePic;
	}

	public void setSharePic(String sharePic) {
		this.sharePic = sharePic;
	}

	public String getVideoUrlTV() {
		return videoUrlTV;
	}

	public void setVideoUrlTV(String videoUrlTV) {
		this.videoUrlTV = videoUrlTV;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(int picHeight) {
		this.picHeight = picHeight;
	}

	public String getFloorModel() {
		return floorModel;
	}

	public void setFloorModel(String floorModel) {
		this.floorModel = floorModel;
	}

	public String getIsLoot() {
		return isLoot;
	}

	public void setIsLoot(String isLoot) {
		this.isLoot = isLoot;
	}

	public String getManyCollage() {
		return manyCollage;
	}

	public void setManyCollage(String manyCollage) {
		this.manyCollage = manyCollage;
	}

	public String getCollageType() {
		return collageType;
	}

	public void setCollageType(String collageType) {
		this.collageType = collageType;
	}

}
