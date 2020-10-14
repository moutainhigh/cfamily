package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiOrderNumberInput extends RootInput {
	@ZapcomApi(value="买家编号")
	private String buyer_code="";

	public String getBuyer_code() {
		return buyer_code;
	}

	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}
	
	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001, wap商城：449747430002, 微信商城：449747430003, PC：449747430004", demo = "123456")
	private String channelId = "449747430001";

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
