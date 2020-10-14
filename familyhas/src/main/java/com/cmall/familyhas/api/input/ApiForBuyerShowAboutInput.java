package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForBuyerShowAboutInput extends RootInput {
	
	@ZapcomApi(value="触发功能类型",remark="1:晒单评价;2:晒单点赞;3:晒单关注;4:晒单评论点赞")
	private String touchType = "";

	@ZapcomApi(value="买家秀评论uid",remark="touchType = 4 时提供")
	private String buyerShowEvaUid = "";
	
	@ZapcomApi(value="买家秀uid",remark="touchType = 1或者2 时提供")
	private String buyerShowUid = "";
	
	@ZapcomApi(value="对买家秀的评论内容",remark="touchType = 1 时提供")
	private String contentEvaluation = "";
	
	@ZapcomApi(value="买家秀评论用户编号",remark="touchType = 3 时提供")
	private String evaMemberCode = "";

	public String getTouchType() {
		return touchType;
	}

	public void setTouchType(String touchType) {
		this.touchType = touchType;
	}

	public String getBuyerShowEvaUid() {
		return buyerShowEvaUid;
	}

	public void setBuyerShowEvaUid(String buyerShowEvaUid) {
		this.buyerShowEvaUid = buyerShowEvaUid;
	}

	public String getBuyerShowUid() {
		return buyerShowUid;
	}

	public void setBuyerShowUid(String buyerShowUid) {
		this.buyerShowUid = buyerShowUid;
	}

	public String getContentEvaluation() {
		return contentEvaluation;
	}

	public void setContentEvaluation(String contentEvaluation) {
		this.contentEvaluation = contentEvaluation;
	}

	public String getEvaMemberCode() {
		return evaMemberCode;
	}

	public void setEvaMemberCode(String evaMemberCode) {
		this.evaMemberCode = evaMemberCode;
	}
	
	
}
