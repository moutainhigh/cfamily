package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiWechatNetParamsResult  extends RootResultWeb{
	@ZapcomApi(value="mac")
	private String mac = "";
	@ZapcomApi(value="当前时间")
	private String tradetime = "";
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getTradetime() {
		return tradetime;
	}

	public void setTradetime(String tradetime) {
		this.tradetime = tradetime;
	}
	
	
}
