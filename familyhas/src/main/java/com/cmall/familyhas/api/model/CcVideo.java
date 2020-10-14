package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CcVideo {
	@ZapcomApi(value = "视频CCVID")
	private String ccvid = "" ;

	@ZapcomApi(value = "视频图片")
	private String img = "";
	
	@ZapcomApi(value = "视频时长",remark="单位：秒")
	private Integer time = 0;
	
	@ZapcomApi(value = "视频状态",remark="0：未处理好视频，1：正常可播视频，2：其他状态（违规视频）")
	private Integer status = 1;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCcvid() {
		return ccvid;
	}

	public void setCcvid(String ccvid) {
		this.ccvid = ccvid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	
	
}
