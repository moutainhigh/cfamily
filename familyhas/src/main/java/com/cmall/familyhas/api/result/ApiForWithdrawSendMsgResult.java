package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForWithdrawSendMsgResult extends RootResult {

	@ZapcomApi(value = "手机号", remark = "发送短信的手机号(中间四位用*代替)")
	private String mobilePhone = "";

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
}
