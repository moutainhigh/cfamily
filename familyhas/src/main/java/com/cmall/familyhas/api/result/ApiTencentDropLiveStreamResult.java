package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiTencentDropLiveStreamResult extends RootResult {
	@ZapcomApi(value = "直播时长", remark = "直播时长,单位:秒")
	private String keeplivetime;

	public String getKeeplivetime() {
		return keeplivetime;
	}

	public void setKeeplivetime(String keeplivetime) {
		this.keeplivetime = keeplivetime;
	}
	
}
