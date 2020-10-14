package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class AdvertisementInfo {

	@ZapcomApi(value="广告入口类型",require=1,demo="ADTP001",remark="ADTP001:个人中心 ,ADTP002:支付成功")
	private String adverEntrType = "";
	@ZapcomApi(value="图片链接")
	private String imgUrl = "";
	@ZapcomApi(value="图片高度")
	private int imgHeight = 0;
	@ZapcomApi(value="图片宽度")
	private int imgWidth = 0;
	@ZapcomApi(value="url链接")
	private String urlLink = "";
	@ZapcomApi(value="分享图片")
	private String shareImgUrl = "";
	@ZapcomApi(value="分享标题")
	private String shareTitle = "";
	@ZapcomApi(value="分享内容")
	private String shareContent = "";
	@ZapcomApi(value="点击事件类型(是否分享)",require=1,demo="N",remark="Y:是 ,N:否")
	private String isShare = "N";
	public int getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}
	public String getUrlLink() {
		return urlLink;
	}
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}
	public String getAdverEntrType() {
		return adverEntrType;
	}
	public void setAdverEntrType(String adverEntrType) {
		this.adverEntrType = adverEntrType;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public String getShareImgUrl() {
		return shareImgUrl;
	}
	public void setShareImgUrl(String shareImgUrl) {
		this.shareImgUrl = shareImgUrl;
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
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	

}