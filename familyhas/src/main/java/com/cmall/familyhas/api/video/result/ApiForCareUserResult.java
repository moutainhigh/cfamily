package com.cmall.familyhas.api.video.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForCareUserResult extends RootResult {

	@ZapcomApi(value="状态",remark="0:已关注，1：取消关注")
	private String status="";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}		
	
	
}
