package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HomeNav {

	@ZapcomApi(value = "编码")
	private String navCode;
	@ZapcomApi(value = "名称")
	private String navName;
	@ZapcomApi(value = "位置")
	private String position;
	@ZapcomApi(value = "图标")
	private String icon;
	@ZapcomApi(value = "图标原宽")
	private Integer iconWith;
	@ZapcomApi(value = "图标原高")
	private Integer iconHeight;
	@ZapcomApi(value = "备选名称")
	private String remark;
	@ZapcomApi(value = "是否开启默认显示")
	private String defaultFlag;
	@ZapcomApi(value = "仅分销用户可见",remark = "否：449748600001,是：449748600002")
	private String isfxShow;
	@ZapcomApi(value = "公众号渠道编码", remark="公众号渠道编码" )
	private String outChannelId = "";
	public String getOutChannelId() {
		return outChannelId;
	}
	public void setOutChannelId(String outChannelId) {
		this.outChannelId = outChannelId;
	}
	
	public String getIsfxShow() {
		return isfxShow;
	}
	public void setIsfxShow(String isfxShow) {
		this.isfxShow = isfxShow;
	}
	public String getNavCode() {
		return navCode;
	}
	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}
	public String getNavName() {
		return navName;
	}
	public void setNavName(String navName) {
		this.navName = navName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getIconWith() {
		return iconWith;
	}
	public void setIconWith(Integer iconWith) {
		this.iconWith = iconWith;
	}
	public Integer getIconHeight() {
		return iconHeight;
	}
	public void setIconHeight(Integer iconHeight) {
		this.iconHeight = iconHeight;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
}
