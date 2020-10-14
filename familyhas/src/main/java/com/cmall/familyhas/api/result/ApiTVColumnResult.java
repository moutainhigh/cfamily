package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiTVColumnResult extends ApiHomeColumnResult {
	
	@ZapcomApi(value="视频文件路径")
	private String tvUrl = "";

	public String getTvUrl() {
		return tvUrl;
	}

	public void setTvUrl(String tvUrl) {
		this.tvUrl = tvUrl;
	}
	
}