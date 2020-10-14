package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForReturnGoodsResult extends RootResult {

	@ZapcomApi(value="售后单号",require=1,demo="RGR160303100002")
	private String afterCode="";

	public String getAfterCode() {
		return afterCode;
	}

	public void setAfterCode(String afterCode) {
		this.afterCode = afterCode;
	}
	
	
}
