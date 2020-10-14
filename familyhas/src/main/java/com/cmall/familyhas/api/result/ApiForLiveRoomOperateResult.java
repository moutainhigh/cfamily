package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForLiveRoomOperateResult extends RootResult {

	@ZapcomApi(value = "是否禁止评论", remark = "是否关闭评论 0否 1是")
	private String ifStopComment = "";

	public String getIfStopComment() {
		return ifStopComment;
	}

	public void setIfStopComment(String ifStopComment) {
		this.ifStopComment = ifStopComment;
	}
	
}
