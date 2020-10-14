package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiTencentUploadFileSignInput extends RootInput {
	@ZapcomApi(value = "超时时间", remark = "单位为秒", demo = "")
	private String expireTime = "7776000";

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	
}
