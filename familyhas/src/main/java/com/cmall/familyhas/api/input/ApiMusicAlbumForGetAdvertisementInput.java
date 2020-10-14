package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiMusicAlbumForGetAdvertisementInput extends RootInput {
	
	@ZapcomApi(value="页面类型编号",require=1,demo="44975022001",remark="44975022001:首页 ,44975022002：个人中心")
	private String pageType="";

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
}
