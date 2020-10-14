package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFillShipmentsTipsResult extends RootResult {

	@ZapcomApi(value = "退换货信息提示 ", require = 1, demo = "xxx" )
	private String tips = "";

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}
	
}
