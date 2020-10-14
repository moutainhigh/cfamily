package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiMusicAlbumForRecordClickNumInput extends RootInput {

	@ZapcomApi(value = "openId")
	private String openId = "";
	@ZapcomApi(value = "advertiseCode",require=1)
	private String advertiseCode="";
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getAdvertiseCode() {
		return advertiseCode;
	}
	public void setAdvertiseCode(String advertiseCode) {
		this.advertiseCode = advertiseCode;
	}
	
}
