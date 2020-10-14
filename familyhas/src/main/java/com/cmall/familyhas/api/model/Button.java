package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class Button {

	@ZapcomApi(value = "功能按钮对应的唯一标识", require = 1, demo = "xxxxx")
	private String buttonCode = "";

	@ZapcomApi(value = "功能按钮上的文字", require = 1, demo = "xxxxx")
	private String buttonTitle = "";
	
	@ZapcomApi(value = "按钮状态",remark="1:按钮展示and正常使用；2：按钮展示不可使用（不可点击）;3：按钮不可用（不展示）")
	private Integer buttonStatus = 1;
	
	@ZapcomApi(value = "小程序跳转连接地址",remark="")
	private String link = "";
	
	@ZapcomApi(value = "微信商城跳转连接地址",remark="")
	private String wxlink = "";

	public String getButtonCode() {
		return buttonCode;
	}

	public void setButtonCode(String buttonCode) {
		this.buttonCode = buttonCode;
	}

	public String getButtonTitle() {
		return buttonTitle;
	}

	public void setButtonTitle(String buttonTitle) {
		this.buttonTitle = buttonTitle;
	}

	public Button(String buttonCode, String buttonTitle) {
		super();
		this.buttonCode = buttonCode;
		this.buttonTitle = buttonTitle;
	}
	
	public Button(String buttonCode, String buttonTitle,Integer buttonStatus) {
		super();
		this.buttonCode = buttonCode;
		this.buttonTitle = buttonTitle;
		this.buttonStatus = buttonStatus;
	}
	
	public Button(String buttonCode, String buttonTitle, Integer buttonStatus, String link, String wxlink) {
		super();
		this.buttonCode = buttonCode;
		this.buttonTitle = buttonTitle;
		this.buttonStatus = buttonStatus;
		this.link = link;
		this.wxlink = wxlink;
	}

	public Button() {
		super();
	}

	public Integer getButtonStatus() {
		return buttonStatus;
	}

	public void setButtonStatus(Integer buttonStatus) {
		this.buttonStatus = buttonStatus;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getWxlink() {
		return wxlink;
	}

	public void setWxlink(String wxlink) {
		this.wxlink = wxlink;
	}
	
}
