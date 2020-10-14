package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 启动页相关跳转属性 
 * 
 */
public class StartupPageJump {
	
	@ZapcomApi(value = "启动页图片标识", remark="0：不更新；1：更新；2：删除" ,demo="XH0001")
	private String picType = "";
	
	@ZapcomApi(value = "启动页图片地址", remark="启动页图片地址" ,demo="XH0001")
	private String picUrl = "";
	
	@ZapcomApi(value = "是否显示跳转按钮", remark="是否显示跳转按钮(否：4497471600180001，是：4497471600180002)")
	private String ynJumpButton = "";
	
	@ZapcomApi(value = "链接类型", remark="链接类型(URL：4497471600210001，关键词搜索：...2,分类搜索:...3,商品详情:..4,跳转首页:5)")
	private String showmoreLinktype = "";
	
	@ZapcomApi(value = "链接值", remark="链接值")
	private String showmoreLinkvalue = "";
	
	@ZapcomApi(value = "按钮类型", remark="按钮类型(图片：4497471600190001，文本：4497471600190002)")
	private String buttonType = "";
	
	@ZapcomApi(value = "按钮图片", remark="按钮图片")
	private String buttonPic = "";
	
	@ZapcomApi(value = "按钮文字", remark="按钮文字")
	private String buttonText = "";
	
	@ZapcomApi(value = "按钮颜色", remark="按钮颜色")
	private String buttonColor = "";
	
	@ZapcomApi(value = "按钮底色", remark="按钮底色")
	private String buttonBackground = "";
	
	@ZapcomApi(value = "停留时间", remark="停留时间")
	private String residenceTime = "";
	
	@ZapcomApi(value = "倒计时是否显示", remark="倒计时是否显示(是：4497471600200001，否：4497471600200002)")
	private String ynCountdown = "";
	
	

	public String getYnJumpButton() {
		return ynJumpButton;
	}

	public void setYnJumpButton(String ynJumpButton) {
		this.ynJumpButton = ynJumpButton;
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

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getButtonPic() {
		return buttonPic;
	}

	public void setButtonPic(String buttonPic) {
		this.buttonPic = buttonPic;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public String getButtonColor() {
		return buttonColor;
	}

	public void setButtonColor(String buttonColor) {
		this.buttonColor = buttonColor;
	}

	public String getButtonBackground() {
		return buttonBackground;
	}

	public void setButtonBackground(String buttonBackground) {
		this.buttonBackground = buttonBackground;
	}

	public String getResidenceTime() {
		return residenceTime;
	}

	public void setResidenceTime(String residenceTime) {
		this.residenceTime = residenceTime;
	}

	public String getYnCountdown() {
		return ynCountdown;
	}

	public void setYnCountdown(String ynCountdown) {
		this.ynCountdown = ynCountdown;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicType() {
		return picType;
	}

	public void setPicType(String picType) {
		this.picType = picType;
	}
}
