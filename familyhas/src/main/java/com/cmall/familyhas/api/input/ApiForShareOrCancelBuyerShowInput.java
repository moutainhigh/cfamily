package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForShareOrCancelBuyerShowInput extends RootInput {

	@ZapcomApi(value="评价uid",remark="")
	private String evaluationUid = "";
	
	@ZapcomApi(value="分享还是取消分享",remark="0取消,1分享")
	private String shareOrCancel = "";

	public String getEvaluationUid() {
		return evaluationUid;
	}

	public void setEvaluationUid(String evaluationUid) {
		this.evaluationUid = evaluationUid;
	}

	public String getShareOrCancel() {
		return shareOrCancel;
	}

	public void setShareOrCancel(String shareOrCancel) {
		this.shareOrCancel = shareOrCancel;
	}
	
}
