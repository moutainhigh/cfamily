package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForFlashSaleInput extends RootInput {
	
	@ZapcomApi(value="渠道编号",remark="449747430001")
	private String channelId = "449747430001";

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
}
