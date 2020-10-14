package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiMusicAlbumForGetUserAlbumPicCountInput extends RootInput {
	
	@ZapcomApi(value="openId")
	private String openId="";

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
}
