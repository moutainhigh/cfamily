package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiBottomNavigateInput extends RootInput {

	@ZapcomApi(value ="最新导航版本",remark="最新导航版本")
	private String navigationVersion = "";

	@ZapcomApi(value ="最新广告导航版本",remark="最新广告导航版本")
	private String adNavigationVersion = "";
	
	public String getNavigationVersion() {
		return navigationVersion;
	}

	public void setNavigationVersion(String navigationVersion) {
		this.navigationVersion = navigationVersion;
	}

	public String getAdNavigationVersion() {
		return adNavigationVersion;
	}

	public void setAdNavigationVersion(String adNavigationVersion) {
		this.adNavigationVersion = adNavigationVersion;
	}

}
