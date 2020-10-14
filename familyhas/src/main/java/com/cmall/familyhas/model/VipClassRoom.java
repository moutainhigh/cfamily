package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class VipClassRoom {
	@ZapcomApi(value="记录唯一标示")
	private String infoId;
	@ZapcomApi(value="标题")
	private String title;
	@ZapcomApi(value="图片")
	private String picUrl;
	@ZapcomApi(value="视频地址")
	private String vcrUrl;
	
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getVcrUrl() {
		return vcrUrl;
	}
	public void setVcrUrl(String vcrUrl) {
		this.vcrUrl = vcrUrl;
	}
}
