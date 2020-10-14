package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class UserNickNameUpdateResult extends RootResultWeb{
	@ZapcomApi(value = "是否成功：error|success")
	private String status = "";
	@ZapcomApi(value = "返回结果消息提示")
	private String msg = "";
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	} 
	
}
