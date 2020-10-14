package com.cmall.familyhas.api.video.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForVideoOperateInput extends RootInput {

	@ZapcomApi(value="操作类型",require=1,remark="R:点赞，C:评论，S:视频点击，D:视频删除，F:分享")
	private String doType="";
	
	@ZapcomApi(value="视频编号",require=1)
	private String videoCode="";
	
	@ZapcomApi(value="评论内容")
	private String comments="";
	
	@ZapcomApi(value="评论人")
	private String commentsUser="";

	public String getDoType() {
		return doType;
	}

	public void setDoType(String doType) {
		this.doType = doType;
	}

	public String getCommentsUser() {
		return commentsUser;
	}

	public void setCommentsUser(String commentsUser) {
		this.commentsUser = commentsUser;
	}

	public String getVideoCode() {
		return videoCode;
	}

	public void setVideoCode(String videoCode) {
		this.videoCode = videoCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
	