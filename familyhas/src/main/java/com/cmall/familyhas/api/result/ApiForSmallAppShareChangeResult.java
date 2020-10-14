package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForSmallAppShareChangeResult extends RootResult {

	@ZapcomApi(value = "小程序推广赚分享参数唯一编号", remark = "推广赚,用于区分分销")
	private String shortCode = "";

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	
}
