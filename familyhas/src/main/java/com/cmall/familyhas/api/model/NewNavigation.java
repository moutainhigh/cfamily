package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class NewNavigation {
	
	@ZapcomApi(value="选中前图片url")
	private String beforePicUrl = "";
	
	@ZapcomApi(value="选中后图片url")
	private String afterPicUrl = "";
	
	@ZapcomApi(value="导航名字")
	private String navigationName = "";
	
	@ZapcomApi(value="导航类型",remark="首页:4497467900040001;分类:4497467900040002;购物车:4497467900040003;我的:4497467900040004;广告导航:4497467900040006;搜索栏右栏广告:4497467900040008;导航背景1:4497467900040009;导航背景2:4497467900040010;导航背景3:4497467900040011;")
	private String navigationType = "";
	
	@ZapcomApi(value="选中前背景颜色")
	private String beforeFontColor = "";
	
	@ZapcomApi(value="选中后背景颜色")
	private String afterFontColor = "";
	
	@ZapcomApi(value="广告导航跳转类型",remark="URL:4497471600020001;关键词搜索:4497471600020002;分类搜索:4497471600020003;商品详情:4497471600020004;抽奖:4497471600020006,首页导航:4497471600020013")
	private String adNavigationKey = "";
	
	@ZapcomApi(value="广告导航跳转值",remark="广告导航跳转类型是首页导航时 这个值为 标签编号")
	private String navigationValue = "";
	
	@ZapcomApi(value = "小程序是否展示", remark="449746250002:不展示,449746250001:展示")
	private String is_show = "";
	
	@ZapcomApi(value = "标签名称", remark="广告导航跳转类型是首页导航时 这个值为 标签名称称")
	private String nav_name = "";
	
	
	
	public String getNav_name() {
		return nav_name;
	}

	public void setNav_name(String nav_name) {
		this.nav_name = nav_name;
	}

	public String getIs_show() {
		return is_show;
	}

	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}

	public String getBeforePicUrl() {
		return beforePicUrl;
	}

	public void setBeforePicUrl(String beforePicUrl) {
		this.beforePicUrl = beforePicUrl;
	}

	public String getAfterPicUrl() {
		return afterPicUrl;
	}

	public void setAfterPicUrl(String afterPicUrl) {
		this.afterPicUrl = afterPicUrl;
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

	public String getBeforeFontColor() {
		return beforeFontColor;
	}

	public void setBeforeFontColor(String beforeFontColor) {
		this.beforeFontColor = beforeFontColor;
	}

	public String getAfterFontColor() {
		return afterFontColor;
	}

	public void setAfterFontColor(String afterFontColor) {
		this.afterFontColor = afterFontColor;
	}

	public String getAdNavigationKey() {
		return adNavigationKey;
	}

	public void setAdNavigationKey(String adNavigationKey) {
		this.adNavigationKey = adNavigationKey;
	}

	public String getNavigationValue() {
		return navigationValue;
	}

	public void setNavigationValue(String navigationValue) {
		this.navigationValue = navigationValue;
	}
	
}
