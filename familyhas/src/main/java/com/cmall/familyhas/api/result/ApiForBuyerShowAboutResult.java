package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForBuyerShowAboutResult extends RootResult {

	@ZapcomApi(value="返回触发功能类型",remark="1:晒单评价;2:晒单点赞;3:晒单关注;4:晒单评论点赞")
	private String backTouchType = "";
	
	@ZapcomApi(value="(关注点赞)/取消",remark="2:晒单点赞;3:晒单关注;4:晒单评论点赞     0:取消成功;1:关注/点赞成功")
	private String cancelOrApprove = "";
	

	public String getCancelOrApprove() {
		return cancelOrApprove;
	}

	public void setCancelOrApprove(String cancelOrApprove) {
		this.cancelOrApprove = cancelOrApprove;
	}

	public String getBackTouchType() {
		return backTouchType;
	}

	public void setBackTouchType(String backTouchType) {
		this.backTouchType = backTouchType;
	}
	
	
}
