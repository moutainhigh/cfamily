package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class BuyerShowEvaluation {

	@ZapcomApi(value="买家秀评论uid",remark="")
	private String buyerShowEvaUid = "";
	
	@ZapcomApi(value="评论人编号",remark="")
	private String memberCode = "";
	
	@ZapcomApi(value="评论人头像",remark="")
	private String avatar = "";
	
	@ZapcomApi(value="评论人昵称",remark="")
	private String nickname = "";
	
	@ZapcomApi(value="评论时间",remark="")
	private String createTime = "";
	
	@ZapcomApi(value="评论内容",remark="")
	private String contentEvaluation = "";
	
	@ZapcomApi(value="买家秀评论点赞量",remark="")
	private String evaApproveNum = "0";
	
	@ZapcomApi(value="当前用户是否点赞",remark="0 否 ; 1 是")
	private String isApprove = "0";

	public String getIsApprove() {
		return isApprove;
	}

	public void setIsApprove(String isApprove) {
		this.isApprove = isApprove;
	}

	public String getBuyerShowEvaUid() {
		return buyerShowEvaUid;
	}

	public void setBuyerShowEvaUid(String buyerShowEvaUid) {
		this.buyerShowEvaUid = buyerShowEvaUid;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContentEvaluation() {
		return contentEvaluation;
	}

	public void setContentEvaluation(String contentEvaluation) {
		this.contentEvaluation = contentEvaluation;
	}

	public String getEvaApproveNum() {
		return evaApproveNum;
	}

	public void setEvaApproveNum(String evaApproveNum) {
		this.evaApproveNum = evaApproveNum;
	}
	
}
