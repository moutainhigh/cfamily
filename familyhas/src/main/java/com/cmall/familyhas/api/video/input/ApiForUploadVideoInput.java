package com.cmall.familyhas.api.video.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForUploadVideoInput extends RootInput {

	@ZapcomApi(value="视频编号",require=1)
	private String videoCode="";
	
	@ZapcomApi(value="封面图URL",require=1)
	private String picUrl="";
	
	@ZapcomApi(value="播放地址URL",require=1)
	private String linkUrl="";
	
	@ZapcomApi(value="视频标题",require=1)
	private String videoTitle="";

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getVideoCode() {
		return videoCode;
	}

	public void setVideoCode(String videoCode) {
		this.videoCode = videoCode;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	
}
	