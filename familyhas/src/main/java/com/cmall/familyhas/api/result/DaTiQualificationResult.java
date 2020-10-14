package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class DaTiQualificationResult extends RootResult{
	@ZapcomApi(value="是否有资格参加抽奖",remark="0为不能参加，1为可以参加")
	private String hasQualification = "0";

	public String getHasQualification() {
		return hasQualification;
	}

	public void setHasQualification(String hasQualification) {
		this.hasQualification = hasQualification;
	}
	
	
}
