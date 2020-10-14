package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiQueryDisableSkuInput extends RootInput {

	@ZapcomApi(value = "买家编号", remark = "可为空，默认取当前登录人的编号",demo = "123456")
	private String buyer_code = "";
	
	public String getBuyer_code() {
		return buyer_code;
	}

	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}

}
