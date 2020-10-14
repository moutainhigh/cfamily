package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetRecommendConfigResult extends RootResult {
	@ZapcomApi(value="上报配置", remark="达观:dg_up;百分点:bfd_up;多个用逗号隔开")
	private String upConfig = "";
	@ZapcomApi(value="获取列表配置", remark="达观:dg_get;百分点:bfd_get;仅一个")
	private String getConfig = "";
	
	public String getUpConfig() {
		return upConfig;
	}
	public void setUpConfig(String upConfig) {
		this.upConfig = upConfig;
	}
	
	public String getGetConfig() {
		return getConfig;
	}
	public void setGetConfig(String getConfig) {
		this.getConfig = getConfig;
	}
}
