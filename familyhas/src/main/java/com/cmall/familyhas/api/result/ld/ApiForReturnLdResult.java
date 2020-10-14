package com.cmall.familyhas.api.result.ld;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForReturnLdResult extends RootResult{
	@ZapcomApi(value="售后编号",remark="售后编号")
	private String afterSaleCodeApp;

	public String getAfterSaleCodeApp() {
		return afterSaleCodeApp;
	}

	public void setAfterSaleCodeApp(String afterSaleCodeApp) {
		this.afterSaleCodeApp = afterSaleCodeApp;
	}
	
	
}
