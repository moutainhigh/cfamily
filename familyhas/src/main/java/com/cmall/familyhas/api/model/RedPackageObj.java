package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class RedPackageObj {
	
	@ZapcomApi(value="是否配置了摇一摇")
	private Boolean isHas = false;

	@ZapcomApi(value="显示图片")
	private String showPicURL = "";
	
	@ZapcomApi(value="导航名字")
	private String navigationName = "";
	
	@ZapcomApi(value="导航类型",remark="首页:4497467900040001;分类:4497467900040002;购物车:4497467900040003;我的:4497467900040004;广告导航:4497467900040006;摇一摇:4497467900040007")
	private String navigationType = "";
	
	@ZapcomApi(value="链接类型url,关键词,商品分类..")
	private String linkType = "";
	
	@ZapcomApi(value="图片点击跳转值",remark="1：摇一摇 2：h5页面,3:小程序,4:首页导航,5:直播页面,6:视频页面")
	private String linkVal = "";
	
	@ZapcomApi(value="进入点击后的原生页面后的操作(摇一摇)链接跳转,首页导航的nav_code")
	private String linkTo = "";
	
	@ZapcomApi(value="小程序原始id")
	private String minProgramId = "";
	
	public Boolean getIsHas() {
		return isHas;
	}

	public void setIsHas(Boolean isHas) {
		this.isHas = isHas;
	}
	
	public String getShowPicURL() {
		return showPicURL;
	}

	public void setShowPicURL(String showPicURL) {
		this.showPicURL = showPicURL;
	}

	public String getNavigationName() {
		return navigationName;
	}

	public void setNavigationName(String navigationName) {
		this.navigationName = navigationName;
	}

	public String getNavigationType() {
		return navigationType;
	}

	public void setNavigationType(String navigationType) {
		this.navigationType = navigationType;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkVal() {
		return linkVal;
	}

	public void setLinkVal(String linkVal) {
		this.linkVal = linkVal;
	}

	public String getLinkTo() {
		return linkTo;
	}

	public void setLinkTo(String linkTo) {
		this.linkTo = linkTo;
	}

	public String getMinProgramId() {
		return minProgramId;
	}

	public void setMinProgramId(String minProgramId) {
		this.minProgramId = minProgramId;
	}
}
