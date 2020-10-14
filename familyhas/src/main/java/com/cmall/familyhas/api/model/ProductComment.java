package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.cmall.productcenter.model.PicAllInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 商品评论
 */
public class ProductComment {

	@ZapcomApi(value = "uid")
	private String uid = "";
	@ZapcomApi(value = "SKU编号")
	private String skuCode = "";
	@ZapcomApi(value = "颜色")
	private String skuColor = "";
	@ZapcomApi(value = "款式")
	private String skuStyle = "";
	@ZapcomApi(value = "评论内容")
	private String commentContent = "";
	@ZapcomApi(value = "评论时间")
	private String commentTime = "";
	@ZapcomApi(value = "回复内容")
	private String replyContent = "";
	@ZapcomApi(value = "回复时间")
	private String replyTime = "";
	@ZapcomApi(value = "评分类型")
	private String gradeType = "";
	@ZapcomApi(value = "评分")
	private String grade = "";
	@ZapcomApi(value = "用户手机号")
	private String userMobile = "";
	@ZapcomApi(value = "用户头像")
	private String userFace = "";
	@ZapcomApi(value = "评价图片")
	private List<PicAllInfo> commentPhotoList = new ArrayList<PicAllInfo>();	
	@ZapcomApi(value = "追评列表")
	private List<ProductCommentAppend> commentAppendList = new ArrayList<ProductCommentAppend>();;
	@ZapcomApi(value="赞成数")
	private int  approveNum = 0;
	@ZapcomApi(value="反对数")
	private int  opposeNum = 0;
	@ZapcomApi(value="对赞成的的态度：Y做了支持点击操作   N无操作")
	private String  approveFlag = "";
	@ZapcomApi(value="对反对的的态度：Y做了反对点击操作 N无操作")
	private String  opposeFlag = "";
	@ZapcomApi(value="商品评价视频",remark="评价视频信息")
	private List<CcVideo>  videoList = new ArrayList<CcVideo>();
	
	@ZapcomApi(value = "是否分享到买家秀", remark="0否1是,不满足买家秀条件为空")
	private String isShare = "";
	@ZapcomApi(value = "是否取消分享", remark="0否1是,不满足买家秀条件为空")
	private String isCancel = "";
	
	public String getIsShare() {
		return isShare;
	}

	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}

	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public List<CcVideo> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<CcVideo> videoList) {
		this.videoList = videoList;
	}

	public int getApproveNum() {
		return approveNum;
	}

	public void setApproveNum(int approveNum) {
		this.approveNum = approveNum;
	}

	public int getOpposeNum() {
		return opposeNum;
	}

	public void setOpposeNum(int opposeNum) {
		this.opposeNum = opposeNum;
	}

	public String getApproveFlag() {
		return approveFlag;
	}

	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}

	public String getOpposeFlag() {
		return opposeFlag;
	}

	public void setOpposeFlag(String opposeFlag) {
		this.opposeFlag = opposeFlag;
	}


	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuColor() {
		return skuColor;
	}

	public void setSkuColor(String skuColor) {
		this.skuColor = skuColor;
	}

	public String getSkuStyle() {
		return skuStyle;
	}

	public void setSkuStyle(String skuStyle) {
		this.skuStyle = skuStyle;
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

	public String getGradeType() {
		return gradeType;
	}

	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserFace() {
		return userFace;
	}

	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}

	public List<PicAllInfo> getCommentPhotoList() {
		return commentPhotoList;
	}

	public void setCommentPhotoList(List<PicAllInfo> commentPhotoList) {
		this.commentPhotoList = commentPhotoList;
	}

	public List<ProductCommentAppend> getCommentAppendList() {
		return commentAppendList;
	}

	public void setCommentAppendList(List<ProductCommentAppend> commentAppendList) {
		this.commentAppendList = commentAppendList;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
