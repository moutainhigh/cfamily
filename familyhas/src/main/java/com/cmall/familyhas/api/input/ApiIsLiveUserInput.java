package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiIsLiveUserInput extends RootInput{

	@ZapcomApi("手机号")
	private String mobile = "";

	/**
	 * 返回: the mobile <br>
	 * 
	 * 时间: 2016-8-2 下午3:52:06
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 参数: mobile the mobile to set <br>
	 * 
	 * 时间: 2016-8-2 下午3:52:06
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
