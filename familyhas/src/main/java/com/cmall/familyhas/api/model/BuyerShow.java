package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class BuyerShow {

	@ZapcomApi(value="买家秀uid",remark="")
	private String buyerShowUid = "";
	
	@ZapcomApi(value="晒单人编号",remark="")
	private String memberCode = "";
	
	@ZapcomApi(value="晒单人头像",remark="")
	private String avatar = "";
	
	@ZapcomApi(value="晒单人昵称",remark="")
	private String nickname = "";
	
	@ZapcomApi(value="评论发表时间",remark="")
	private String createTime = "";
	
	@ZapcomApi(value="晒单内容",remark="")
	private String orderAssessment = "";
	
	@ZapcomApi(value="买家秀点赞量",remark="")
	private String approveNum = "0";
	
	@ZapcomApi(value="买家秀阅读量",remark="")
	private String readNum = "0";
	
	@ZapcomApi(value="买家秀评价量",remark="")
	private String evaluationNum = "0";
	
	@ZapcomApi(value="买家秀商品信息",remark="")
	private EvaProduct evaProduct = new EvaProduct();
	
	@ZapcomApi(value = "买家秀图片或者视频list")
	private List<EvaluationImg> evaluationImgList = new ArrayList<EvaluationImg>();
	
	@ZapcomApi(value="当前用户是否点赞",remark="0 否 ; 1 是")
	private String isApprove = "0";
	
	@ZapcomApi(value="当前用户是否关注",remark="0 否 ; 1 是")
	private String isFollow = "0";
	
	@ZapcomApi(value = "商品图片", remark="主要供买家秀入口样式1使用")
	private String prodPicUrl = "";
	
	@ZapcomApi(value="买家秀收益",remark="")
	private String buyerShowMoney = "0";

	public String getBuyerShowMoney() {
		return buyerShowMoney;
	}

	public void setBuyerShowMoney(String buyerShowMoney) {
		this.buyerShowMoney = buyerShowMoney;
	}

	public String getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(String isFollow) {
		this.isFollow = isFollow;
	}

	public String getIsApprove() {
		return isApprove;
	}

	public void setIsApprove(String isApprove) {
		this.isApprove = isApprove;
	}

	public String getProdPicUrl() {
		return prodPicUrl;
	}

	public void setProdPicUrl(String prodPicUrl) {
		this.prodPicUrl = prodPicUrl;
	}

	public String getBuyerShowUid() {
		return buyerShowUid;
	}

	public void setBuyerShowUid(String buyerShowUid) {
		this.buyerShowUid = buyerShowUid;
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

	public String getOrderAssessment() {
		return orderAssessment;
	}

	public void setOrderAssessment(String orderAssessment) {
		this.orderAssessment = orderAssessment;
	}

	public String getApproveNum() {
		return approveNum;
	}

	public void setApproveNum(String approveNum) {
		this.approveNum = approveNum;
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getEvaluationNum() {
		return evaluationNum;
	}

	public void setEvaluationNum(String evaluationNum) {
		this.evaluationNum = evaluationNum;
	}

	public EvaProduct getEvaProduct() {
		return evaProduct;
	}

	public void setEvaProduct(EvaProduct evaProduct) {
		this.evaProduct = evaProduct;
	}

	public List<EvaluationImg> getEvaluationImgList() {
		return evaluationImgList;
	}

	public void setEvaluationImgList(List<EvaluationImg> evaluationImgList) {
		this.evaluationImgList = evaluationImgList;
	}
	
	
	
}
