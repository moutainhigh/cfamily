package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.cmall.productcenter.model.PicAllInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 追加评论
 */
public class ProductCommentAppend {

	@ZapcomApi(value = "评论内容")
	private String commentContent = "";
	@ZapcomApi(value = "评论时间")
	private String commentTime = "";
	@ZapcomApi(value = "追评天数")
	private String commentDay = "";
	@ZapcomApi(value = "回复内容")
	private String replyContent = "";
	@ZapcomApi(value = "回复时间")
	private String replyTime = "";
	@ZapcomApi(value = "追评图片")
	private List<PicAllInfo> commentPhotoList = new ArrayList<PicAllInfo>();
	
	@ZapcomApi(value="商品追评视频",remark="追评视频信息")
	private List<CcVideo>  videoList = new ArrayList<CcVideo>();
	
	

	public List<CcVideo> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<CcVideo> videoList) {
		this.videoList = videoList;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public List<PicAllInfo> getCommentPhotoList() {
		return commentPhotoList;
	}

	public void setCommentPhotoList(List<PicAllInfo> commentPhotoList) {
		this.commentPhotoList = commentPhotoList;
	}

	public String getCommentDay() {
		return commentDay;
	}

	public void setCommentDay(String commentDay) {
		this.commentDay = commentDay;
	}

}
