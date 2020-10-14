package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class EvaluationImg {

	@ZapcomApi(value = "评价图片")
	private String evaluationImgUrl = "";
	
	@ZapcomApi(value = "是否是视频",remark="0否1是")
	private String isVideo = "";
	
	@ZapcomApi(value = "视频id",remark="CC视频id")
	private String ccvid = "";

	public String getEvaluationImgUrl() {
		return evaluationImgUrl;
	}

	public void setEvaluationImgUrl(String evaluationImgUrl) {
		this.evaluationImgUrl = evaluationImgUrl;
	}

	public String getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(String isVideo) {
		this.isVideo = isVideo;
	}

	public String getCcvid() {
		return ccvid;
	}

	public void setCcvid(String ccvid) {
		this.ccvid = ccvid;
	}
}
