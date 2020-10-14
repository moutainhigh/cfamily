package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class AdvertiseColumnmentInfo {

	@ZapcomApi(value="图片链接")
	private String imgUrl = "";
	@ZapcomApi(value="图片高度")
	private int imgHeight = 0;
	@ZapcomApi(value="图片宽度")
	private int imgWidth = 0;
	@ZapcomApi(value="url链接")
	private String urlLink = "";
	@ZapcomApi(value="排序号")
	private int sort_num = 0;
	@ZapcomApi(value="分享图片")
	private String shareImgUrl = "";
	@ZapcomApi(value="分享标题")
	private String shareTitle = "";
	@ZapcomApi(value="分享内容")
	private String shareContent = "";
	@ZapcomApi(value="点击事件类型(是否分享)",require=1,demo="449746250001",remark="449746250001:是 ,449746250002:否")
	private String isShare = "449746250002";
	
	//小程序跳转相关配置参数
	@ZapcomApi(value="小程序APP_ID")
	private String appId = "";
	@ZapcomApi(value="小程序PATH")
	private String path = "";
	@ZapcomApi(value="跳转类型",demo="4497471600640002",remark="4497471600640001:跳转小程序是 4497471600640002:跳转URL")
	private String jumpType = "4497471600640002";
	
	
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
	public int getSort_num() {
		return sort_num;
	}
	public void setSort_num(int sort_num) {
		this.sort_num = sort_num;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getJumpType() {
		return jumpType;
	}
	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}

}