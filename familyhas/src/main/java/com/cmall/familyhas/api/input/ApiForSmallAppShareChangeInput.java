package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForSmallAppShareChangeInput extends RootInput {

	@ZapcomApi(value="小程序推广赚分享的参数内容",remark = "")
	private String shortContent = "";

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}
	
}
