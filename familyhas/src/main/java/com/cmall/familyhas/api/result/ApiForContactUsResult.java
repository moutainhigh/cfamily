package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/** 
* @Author fufu
* @Time 2020-8-18 11:13:53 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForContactUsResult extends RootResult {
	
	@ZapcomApi(value="后台配置图片URL",remark="支持多图，用‘|’隔开",demo="")
	private String picUrl = "";
	
	@ZapcomApi(value="后台配置视频url",remark="",demo="")
	private String videoUrl = "";
	
	@ZapcomApi(value="说明文字",remark="",demo="")
	private String desc = "";
	
	@ZapcomApi(value="视频封面图",remark="",demo="")
	private String videoMainPic = "";
	
	@ZapcomApi(value="电话1",remark="",demo="")
	private String phoneOne = "";
	
	@ZapcomApi(value="电话2",remark="",demo="")
	private String phoneTwo = "";

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPhoneOne() {
		return phoneOne;
	}

	public void setPhoneOne(String phoneOne) {
		this.phoneOne = phoneOne;
	}

	public String getPhoneTwo() {
		return phoneTwo;
	}

	public void setPhoneTwo(String phoneTwo) {
		this.phoneTwo = phoneTwo;
	}

	public String getVideoMainPic() {
		return videoMainPic;
	}

	public void setVideoMainPic(String videoMainPic) {
		this.videoMainPic = videoMainPic;
	}
	
	
}
